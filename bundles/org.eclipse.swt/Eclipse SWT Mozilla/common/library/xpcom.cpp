/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

#ifndef NO__1Call__I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__I)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__I)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1Call__I_FUNC);
	rc = (jint)((jint (*)())arg0)();
	XPCOM_NATIVE_EXIT(env, that, _1Call__I_FUNC);
	return rc;
}
#endif

#ifndef NO__1Call__IIIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__IIIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1Call__IIIIII_FUNC);
	rc = (jint)((SWT_XREInitEmbedding)arg0)((nsILocalFile *)arg1, (nsILocalFile *)arg2, (nsIDirectoryServiceProvider *)arg3, (nsStaticModuleInfo const *)arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, _1Call__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1Call__III_3BII_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__III_3BII_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__III_3BII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6)
{
	jbyte *lparg3=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1Call__III_3BII_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((nsWriteSegmentFun)arg0)((nsIInputStream *)arg1, (void *)arg2, (const char *)lparg3, arg4, arg5, (PRUint32 *)lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1Call__III_3BII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1NS_1GetComponentManager
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetComponentManager)(JNIEnv *env, jclass that, jintArray arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetComponentManager)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1GetComponentManager_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)NS_GetComponentManager((nsIComponentManager**)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1GetComponentManager_FUNC);
	return rc;
}
#endif

#ifndef NO__1NS_1GetServiceManager
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetServiceManager)(JNIEnv *env, jclass that, jintArray arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetServiceManager)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1GetServiceManager_FUNC);
	if (arg0) if ((lparg0 = env->GetIntArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)NS_GetServiceManager((nsIServiceManager**)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1GetServiceManager_FUNC);
	return rc;
}
#endif

#ifndef NO__1NS_1InitXPCOM2
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1InitXPCOM2)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1InitXPCOM2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1InitXPCOM2_FUNC);
	rc = (jint)NS_InitXPCOM2((nsIServiceManager **)arg0, (nsIFile *)arg1, (nsIDirectoryServiceProvider *)arg2);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1InitXPCOM2_FUNC);
	return rc;
}
#endif

#ifndef NO__1NS_1NewLocalFile
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1NewLocalFile)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1NewLocalFile)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1NewLocalFile_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)NS_NewLocalFile(*(nsAString *)arg0, arg1, (nsILocalFile**)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1NewLocalFile_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint))(*(jint **)arg1)[arg0])(arg1);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IID
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IID)(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IID_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jdouble))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IID_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIF
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIF)(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIF_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jfloat))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIF_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIIIIIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIIIIIIIIIIISI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIIIIIISI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jint arg16);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIIIIIISI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jint arg16)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIIIIIIIIIISI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jshort, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIIIIIIIIIISI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIIIII_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIII_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jintArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jintArray arg9)
{
	jint *lparg9=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIIII_3I_FUNC);
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, lparg9);
fail:
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIIII_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jcharArray arg8, jintArray arg9, jintArray arg10);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jcharArray arg8, jintArray arg9, jintArray arg10)
{
	jchar *lparg8=NULL;
	jint *lparg9=NULL;
	jint *lparg10=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIII_3C_3I_3I_FUNC);
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetIntArrayElements(arg10, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8, lparg9, lparg10);
fail:
	if (arg10 && lparg10) env->ReleaseIntArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIII_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIII_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIII_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7)
{
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIII_3I_FUNC);
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIIIJII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIJII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jlong arg6, jint arg7, jint arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIJII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jlong arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIJII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jlong, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIJII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIII_3CIIIII_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3CIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jintArray arg12, jintArray arg13);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3CIIIII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jintArray arg12, jintArray arg13)
{
	jchar *lparg6=NULL;
	jint *lparg12=NULL;
	jint *lparg13=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3CIIIII_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg12) if ((lparg12 = env->GetIntArrayElements(arg12, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetIntArrayElements(arg13, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jchar *, jint, jint, jint, jint, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, arg7, arg8, arg9, arg10, arg11, lparg12, lparg13);
fail:
	if (arg13 && lparg13) env->ReleaseIntArrayElements(arg13, lparg13, 0);
	if (arg12 && lparg12) env->ReleaseIntArrayElements(arg12, lparg12, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3CIIIII_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIII_3C_3BIIIII_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3BIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jintArray arg13, jintArray arg14);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3BIIIII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jintArray arg13, jintArray arg14)
{
	jchar *lparg6=NULL;
	jbyte *lparg7=NULL;
	jint *lparg13=NULL;
	jint *lparg14=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3C_3BIIIII_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetIntArrayElements(arg13, NULL)) == NULL) goto fail;
	if (arg14) if ((lparg14 = env->GetIntArrayElements(arg14, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jchar *, jbyte *, jint, jint, jint, jint, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, arg8, arg9, arg10, arg11, arg12, lparg13, lparg14);
fail:
	if (arg14 && lparg14) env->ReleaseIntArrayElements(arg14, lparg14, 0);
	if (arg13 && lparg13) env->ReleaseIntArrayElements(arg13, lparg13, 0);
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3C_3BIIIII_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIII_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
{
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3C_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIII_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIII_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8)
{
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3I_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIII_3B_3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3B_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6, jint arg7)
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_3B_3BI_FUNC);
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jbyte *, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, lparg6, arg7);
fail:
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_3B_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIII_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jcharArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jcharArray arg5)
{
	jchar *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_3C_FUNC);
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIII_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_3I_FUNC);
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIII_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_3I_3I_3I_FUNC);
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIIJJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIJJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIJJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIJJJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jlong, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIJJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3B_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jbyte *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3BI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3BI_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3BII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3BII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3BII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3BII_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jbyte *, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3BII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3B_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3B_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jbyteArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3B_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4, jbyteArray arg5)
{
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3B_3B_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jbyte *, jbyte *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3B_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3C_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3CIJI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CIJI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CIJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3CIJI_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jchar *, jint, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3CIJI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3CJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3CJJJ_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jchar *, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3CJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3C_3CI_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jcharArray arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C_3CI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4, jcharArray arg5, jint arg6, jintArray arg7)
{
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3C_3CI_3I_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jchar *, jchar *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3C_3CI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3I_FUNC);
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3I_3I_FUNC);
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIII_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jlongArray arg4)
{
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3J_FUNC);
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jlong arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jlong arg3, jlong arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIIJJJJJJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIJJJJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8, jlongArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIJJJJJJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8, jlongArray arg9)
{
	jlong *lparg9=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIJJJJJJ_3J_FUNC);
	if (arg9) if ((lparg9 = env->GetLongArrayElements(arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jlong, jlong, jlong, jlong, jlong, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, lparg9);
fail:
	if (arg9 && lparg9) env->ReleaseLongArrayElements(arg9, lparg9, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIJJJJJJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, nsID *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2II_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2II_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jintArray arg6)
{
	nsID _arg3, *lparg3=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2II_3I_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, nsID *, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2II_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3BI_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3BI_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5)
{
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3BI_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3BI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3BS
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BS)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jshort arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BS)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jshort arg4)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3BS_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jshort))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3BS_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3B_3B_3BI_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3B_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3B_3BI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_3B_3BI_3I_FUNC);
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
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_3B_3BI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3B_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jcharArray arg4)
{
	jbyte *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_3C_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3B_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jbyte *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3)
{
	jchar *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3CI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3CI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jint arg4)
{
	jchar *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3CI_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3CI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg6=NULL;
	jchar *lparg7=NULL;
	jchar *lparg8=NULL;
	jchar *lparg9=NULL;
	jint *lparg10=NULL;
	jint *lparg11=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetCharArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetIntArrayElements(arg10, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = env->GetIntArrayElements(arg11, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint, jchar *, jchar *, jchar *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8, lparg9, lparg10, lparg11);
fail:
	if (arg11 && lparg11) env->ReleaseIntArrayElements(arg11, lparg11, 0);
	if (arg10 && lparg10) env->ReleaseIntArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseCharArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3CI_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3CI_3I_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3CI_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3C_3C_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3C_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jchar *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3C_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3C_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3C_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3C_3I_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3I_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3I_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3C_3I_3I_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jintArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3I_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jintArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jchar *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3I_3I_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jchar *, jint *, jint *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3I_3I_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3C_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jintArray arg4)
{
	jchar *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jchar *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3II
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3II)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3II_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3II_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3IJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3IJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3IJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jlong arg4)
{
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3IJ_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3IJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3I_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3I_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3I_3I_3I_3I_FUNC);
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
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3I_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__III_3I_3I_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jlongArray arg5)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3I_3I_3J_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *, jint *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3I_3I_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong))(*(jint **)arg1)[arg0])(arg1, arg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJIIJIIIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIIJIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jint, jlong, jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIIJIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJIIJIIIIIIIIISJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIIIIIISJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jlong arg16);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIIIIIISJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jlong arg16)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIIJIIIIIIIIISJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jint, jlong, jint, jint, jint, jint, jint, jint, jint, jint, jint, jshort, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIIJIIIIIIIIISJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJIIJ_3I_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jintArray arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJ_3I_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jintArray arg6, jlongArray arg7)
{
	jint *lparg6=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIIJ_3I_3J_FUNC);
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jint, jlong, jint *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIIJ_3I_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJII_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJII_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJII_3I_FUNC);
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJIJII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIJII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlong arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIJII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlong arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIJII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jlong, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIJII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJIJJ_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIJJ_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlong arg4, jlong arg5, jintArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIJJ_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlong arg4, jlong arg5, jintArray arg6, jintArray arg7, jintArray arg8)
{
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIJJ_3I_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jlong, jlong, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIJJ_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJIJ_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIJ_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlong arg4, jintArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIJ_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlong arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIJ_3I_3I_3I_FUNC);
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jlong, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIJ_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJI_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jint arg3, jlongArray arg4)
{
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJI_3J_FUNC);
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJI_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJIJ_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIJ_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jlong arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIJ_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jlong arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
{
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJIJ_3C_3I_3I_FUNC);
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint, jlong, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJIJ_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJI_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jcharArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jcharArray arg5)
{
	jchar *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJI_3C_FUNC);
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJI_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJI_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jint arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJI_3I_FUNC);
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJI_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJI_3CJJIJI_3J_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3CJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jlong arg7, jlong arg8, jint arg9, jlong arg10, jint arg11, jlongArray arg12, jlongArray arg13);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3CJJIJI_3J_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jlong arg7, jlong arg8, jint arg9, jlong arg10, jint arg11, jlongArray arg12, jlongArray arg13)
{
	jchar *lparg6=NULL;
	jlong *lparg12=NULL;
	jlong *lparg13=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJI_3CJJIJI_3J_3J_FUNC);
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg12) if ((lparg12 = env->GetLongArrayElements(arg12, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetLongArrayElements(arg13, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jint, jchar *, jlong, jlong, jint, jlong, jint, jlong *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, arg7, arg8, arg9, arg10, arg11, lparg12, lparg13);
fail:
	if (arg13 && lparg13) env->ReleaseLongArrayElements(arg13, lparg13, 0);
	if (arg12 && lparg12) env->ReleaseLongArrayElements(arg12, lparg12, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJI_3CJJIJI_3J_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jlong arg8, jlong arg9, jint arg10, jlong arg11, jint arg12, jlongArray arg13, jlongArray arg14);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jlong arg8, jlong arg9, jint arg10, jlong arg11, jint arg12, jlongArray arg13, jlongArray arg14)
{
	jchar *lparg6=NULL;
	jbyte *lparg7=NULL;
	jlong *lparg13=NULL;
	jlong *lparg14=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J_FUNC);
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetLongArrayElements(arg13, NULL)) == NULL) goto fail;
	if (arg14) if ((lparg14 = env->GetLongArrayElements(arg14, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jint, jchar *, jbyte *, jlong, jlong, jint, jlong, jint, jlong *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, arg8, arg9, arg10, arg11, arg12, lparg13, lparg14);
fail:
	if (arg14 && lparg14) env->ReleaseLongArrayElements(arg14, lparg14, 0);
	if (arg13 && lparg13) env->ReleaseLongArrayElements(arg13, lparg13, 0);
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJJIJ_3C_3I_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJIJ_3C_3I_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jint arg6, jlong arg7, jcharArray arg8, jintArray arg9, jlongArray arg10);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJIJ_3C_3I_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jint arg6, jlong arg7, jcharArray arg8, jintArray arg9, jlongArray arg10)
{
	jchar *lparg8=NULL;
	jint *lparg9=NULL;
	jlong *lparg10=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJIJ_3C_3I_3J_FUNC);
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetLongArrayElements(arg10, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jlong, jint, jlong, jchar *, jint *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8, lparg9, lparg10);
fail:
	if (arg10 && lparg10) env->ReleaseLongArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJIJ_3C_3I_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJJJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJJJJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jlong, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJJJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlongArray arg7)
{
	jlong *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJJ_3J_FUNC);
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jlong, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJ_3B_3BJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jbyteArray arg5, jbyteArray arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3B_3BJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jbyteArray arg5, jbyteArray arg6, jlong arg7)
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJ_3B_3BJ_FUNC);
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jbyte *, jbyte *, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, lparg6, arg7);
fail:
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJ_3B_3BJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJ_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jintArray arg5)
{
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJ_3I_FUNC);
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJ_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlong arg4, jlongArray arg5)
{
	jlong *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJ_3J_FUNC);
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3B_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jbyte *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3BJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3BJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3BJ_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jbyte *, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3BJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3BJI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3BJI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3BJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5, jint arg6)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3BJI_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jbyte *, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3BJI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3B_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4, jbyteArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3B_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jbyteArray arg4, jbyteArray arg5)
{
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3B_3B_FUNC);
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jbyte *, jbyte *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3B_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3CIJI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CIJI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CIJI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3CIJI_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jchar *, jint, jlong, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3CIJI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3CJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3CJJJ_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jchar *, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3CJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3C_3CI_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3C_3CI_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jcharArray arg4, jcharArray arg5, jint arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3C_3CI_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jcharArray arg4, jcharArray arg5, jint arg6, jlongArray arg7)
{
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3C_3CI_3J_FUNC);
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jchar *, jchar *, jint, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3C_3CI_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3I_FUNC);
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3I_3I_FUNC);
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlong arg3, jlongArray arg4)
{
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3J_FUNC);
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jobject arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jobject arg3)
{
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, nsID *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jobject arg3, jlong arg4, jlong arg5, jlongArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jobject arg3, jlong arg4, jlong arg5, jlongArray arg6)
{
	nsID _arg3, *lparg3=NULL;
	jlong *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetLongArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, nsID *, jlong, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseLongArrayElements(arg6, lparg6, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jobject arg3, jlongArray arg4)
{
	nsID _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, nsID *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3B_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jbyte *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3BI_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3BJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jlong arg4)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3BJ_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jbyte *, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3BJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3BJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jlong arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jlong arg4, jlongArray arg5)
{
	jbyte *lparg3=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3BJ_3J_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jbyte *, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3BJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3BS
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BS)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jshort arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BS)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jshort arg4)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3BS_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jbyte *, jshort))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3BS_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3B_3B_3BJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3B_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jlong arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3B_3BJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jlong arg6, jlongArray arg7)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3B_3B_3BJ_3J_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jbyte *, jbyte *, jbyte *, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3B_3B_3BJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3B_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jbyteArray arg3, jcharArray arg4)
{
	jbyte *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3B_3C_FUNC);
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jbyte *, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3B_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3CI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3CI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jint arg4)
{
	jchar *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3CI_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jint))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3CI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg6=NULL;
	jchar *lparg7=NULL;
	jchar *lparg8=NULL;
	jchar *lparg9=NULL;
	jint *lparg10=NULL;
	jint *lparg11=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetCharArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetIntArrayElements(arg10, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = env->GetIntArrayElements(arg11, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jint, jchar *, jchar *, jchar *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8, lparg9, lparg10, lparg11);
fail:
	if (arg11 && lparg11) env->ReleaseIntArrayElements(arg11, lparg11, 0);
	if (arg10 && lparg10) env->ReleaseIntArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseCharArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3CI_3J_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3J_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jlongArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3J_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jlongArray arg6, jintArray arg7, jintArray arg8)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3CI_3J_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetLongArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jint, jlong *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseLongArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3CI_3J_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3C_3C_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jchar *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3C_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3C_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3C_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3J_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3C_3J_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg5=NULL;
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3J_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jlong *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3J_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jlongArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jlongArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9)
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg5=NULL;
	jlong *lparg6=NULL;
	jchar *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetLongArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jchar *, jlong *, jlong *, jchar *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseLongArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3C_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jcharArray arg3, jlongArray arg4)
{
	jchar *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3J_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jchar *, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3I_FUNC);
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IIJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlong arg2, jlongArray arg3)
{
	jlong *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3J_FUNC);
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	nsID _arg2, *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4, jintArray arg5)
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jlong arg3)
{
	nsID _arg2, *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jlong))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jlong arg3, jobject arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jlong arg3, jobject arg4, jlongArray arg5)
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jlong, nsID *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jintArray arg4)
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jlongArray arg4)
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, nsID *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5)
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jbyte *, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbyteArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbyteArray arg7)
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jbyte *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
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
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5)
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jbyte *, jbyte *, jlong))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5, jbyteArray arg6, jbyteArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5, jbyteArray arg6, jbyteArray arg7)
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jbyte *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jbyte *, jbyte *, jlong, jbyte *, jbyte *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	nsID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jlongArray arg3)
{
	nsID _arg2, *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, nsID *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IISIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IISIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jshort, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IISIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__IISJIJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISJIJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jlong arg3, jint arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISJIJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jlong arg3, jint arg4, jlong arg5)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IISJIJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jshort, jlong, jint, jlong))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IISJIJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BII_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BII_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6)
{
	jbyte *lparg2=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BII_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BII_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BIJ_3J_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BIJ_3J_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jlong arg4, jlongArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BIJ_3J_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jlong arg4, jlongArray arg5, jintArray arg6)
{
	jbyte *lparg2=NULL;
	jlong *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BIJ_3J_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, jlong, jlong *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BIJ_3J_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jobject arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jobject arg4, jintArray arg5)
{
	jbyte *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BI_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BI_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BI_3J_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3J_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jlongArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3J_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jlongArray arg4, jintArray arg5)
{
	jbyte *lparg2=NULL;
	jlong *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_3J_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint, jlong *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_3J_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlong arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BJ_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jlong))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlong arg3, jobject arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlong arg3, jobject arg4, jlongArray arg5)
{
	jbyte *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jlong, nsID *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BJ_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJ_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlong arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJ_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlong arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BJ_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jlong, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BJ_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, nsID *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jlong arg4)
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, nsID *, jlong))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, nsID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jobject arg3, jlongArray arg4)
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, nsID *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3B
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3BI_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3B_3BII_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3BII_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jbyte *, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3BII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3B_3BII_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jlongArray arg7)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3BII_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jbyte *, jint, jint, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3BII_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3B_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3B_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jlongArray arg4)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jbyte *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3I_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jlongArray arg4)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3I_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jint *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3I_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlongArray arg3)
{
	jbyte *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3B_3J_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlongArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jlongArray arg3, jintArray arg4)
{
	jbyte *lparg2=NULL;
	jlong *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3J_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jbyte *, jlong *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3J_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3C_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3CIIII
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIIII)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CIIII_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CIIII_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3CIJJJ
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIJJJ)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIJJJ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CIJJJ_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jlong, jlong, jlong))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CIJJJ_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3CI_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CI_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jintArray arg4)
{
	jchar *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CI_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CI_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3CJ_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CJ_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jlong arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CJ_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jlong arg3, jlongArray arg4)
{
	jchar *lparg2=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CJ_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jlong, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CJ_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3C_3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C_3C)(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jcharArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jcharArray arg3)
{
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3C_3C_FUNC);
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jchar *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3C_3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3F
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3F)(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3F)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloatArray arg2)
{
	jfloat *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3F_FUNC);
	if (arg2) if ((lparg2 = env->GetFloatArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jfloat *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseFloatArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3F_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3I_3I_3I_FUNC);
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
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I_3I_3I_3I_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3I_3I_3I_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jint *, jint *, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3I_3I_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3)
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I_3J_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3, jintArray arg4)
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3J_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jlong *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3J_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3I_3J_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jlongArray arg3, jlongArray arg4)
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3J_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *, jlong *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3J_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2)
{
	jlong *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3J_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2, jlongArray arg3)
{
	jlong *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3J_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3J_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3J_3J_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4)
{
	jlong *lparg2=NULL;
	jlong *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3J_3J_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong *, jlong *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3J_3J_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3J_3J_3J_3J_3J_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J_3J_3J_3J_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4, jlongArray arg5, jlongArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J_3J_3J_3J_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4, jlongArray arg5, jlongArray arg6, jintArray arg7)
{
	jlong *lparg2=NULL;
	jlong *lparg3=NULL;
	jlong *lparg4=NULL;
	jlong *lparg5=NULL;
	jlong *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3J_3J_3J_3J_3J_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetLongArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jlong *, jlong *, jlong *, jlong *, jlong *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseLongArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3J_3J_3J_3J_3J_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3S
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3S)(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3S)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2)
{
	jshort *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3S_FUNC);
	if (arg2) if ((lparg2 = env->GetShortArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jshort *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseShortArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3S_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3SI_3I_3I
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jint arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SI_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jshort *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3SI_3I_3I_FUNC);
	if (arg2) if ((lparg2 = env->GetShortArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jshort *, jint, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseShortArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3SI_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO__1VtblCall__II_3SJ_3I_3J
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jlong arg3, jintArray arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SJ_3I_3J)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jlong arg3, jintArray arg4, jlongArray arg5)
{
	jshort *lparg2=NULL;
	jint *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3SJ_3I_3J_FUNC);
	if (arg2) if ((lparg2 = env->GetShortArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jshort *, jlong, jint *, jlong *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseShortArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3SJ_3I_3J_FUNC);
	return rc;
}
#endif

#ifndef NO__1XPCOMGlueLoadXULFunctions
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueLoadXULFunctions)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueLoadXULFunctions)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1XPCOMGlueLoadXULFunctions_FUNC);
	rc = (jint)XPCOMGlueLoadXULFunctions((const nsDynamicFunctionLoad *)arg0);
	XPCOM_NATIVE_EXIT(env, that, _1XPCOMGlueLoadXULFunctions_FUNC);
	return rc;
}
#endif

#ifndef NO__1XPCOMGlueShutdown
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueShutdown)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueShutdown)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1XPCOMGlueShutdown_FUNC);
	rc = (jint)XPCOMGlueShutdown();
	XPCOM_NATIVE_EXIT(env, that, _1XPCOMGlueShutdown_FUNC);
	return rc;
}
#endif

#ifndef NO__1XPCOMGlueStartup
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueStartup)(JNIEnv *env, jclass that, jbyteArray arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueStartup)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1XPCOMGlueStartup_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)XPCOMGlueStartup((const char *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1XPCOMGlueStartup_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1Length
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1Length)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1Length)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1Length_FUNC);
	rc = (jint)((nsEmbedCString *)arg0)->Length();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1delete
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedCString_1delete)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedCString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1delete_FUNC);
	delete (nsEmbedCString *)arg0;
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1delete_FUNC);
}
#endif

#ifndef NO__1nsEmbedCString_1get
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1get)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1get)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1get_FUNC);
	rc = (jint)((nsEmbedCString *)arg0)->get();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1new__
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1new___FUNC);
	rc = (jint)new nsEmbedCString();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1new___FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1new__II
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__II)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1new__II_FUNC);
	rc = (jint)new nsEmbedCString((const char *)arg0, arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1new__II_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1new___3BI
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new___3BI)(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new___3BI)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1new___3BI_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new nsEmbedCString((const char *)lparg0, arg1);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1new___3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1Length
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1Length)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1Length)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1Length_FUNC);
	rc = (jint)((nsEmbedString *)arg0)->Length();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1delete
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedString_1delete)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1delete_FUNC);
	delete (nsEmbedString *)arg0;
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1delete_FUNC);
}
#endif

#ifndef NO__1nsEmbedString_1get
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1get)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1get)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1get_FUNC);
	rc = (jint)((nsEmbedString *)arg0)->get();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1new__
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1new__)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1new__)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1new___FUNC);
	rc = (jint)new nsEmbedString();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1new___FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1new___3C
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1new___3C)(JNIEnv *env, jclass that, jcharArray arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1new___3C)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1new___3C_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new nsEmbedString((PRUnichar *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1new___3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsID_1Equals
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsID_1Equals)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsID_1Equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsID_1Equals_FUNC);
	rc = (jint)((nsID *)arg0)->Equals(*(nsID *)arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsID_1Equals_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsID_1delete
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsID_1delete)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsID_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	XPCOM_NATIVE_ENTER(env, that, _1nsID_1delete_FUNC);
	delete (nsID *)arg0;
	XPCOM_NATIVE_EXIT(env, that, _1nsID_1delete_FUNC);
}
#endif

#ifndef NO__1nsID_1new
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsID_1new)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsID_1new)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsID_1new_FUNC);
	rc = (jint)new nsID();
	XPCOM_NATIVE_EXIT(env, that, _1nsID_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIMemory_1Alloc
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIMemory_1Alloc)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIMemory_1Alloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIMemory_1Alloc_FUNC);
	rc = (jint)((nsIMemory *)arg0)->Alloc((size_t)arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsIMemory_1Alloc_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIMemory_1Realloc
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIMemory_1Realloc)(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIMemory_1Realloc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIMemory_1Realloc_FUNC);
	rc = (jint)((nsIMemory *)arg0)->Realloc((void *)arg1, (size_t)arg2);
	XPCOM_NATIVE_EXIT(env, that, _1nsIMemory_1Realloc_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIScriptContext_1GetNativeContext
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptContext_1GetNativeContext)(JNIEnv *env, jclass that, jint arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptContext_1GetNativeContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptContext_1GetNativeContext_FUNC);
	rc = (jint)((nsIScriptContext *)arg0)->GetNativeContext();
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptContext_1GetNativeContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIScriptGlobalObject_1EnsureScriptEnvironment
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject_1EnsureScriptEnvironment)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject_1EnsureScriptEnvironment)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptGlobalObject_1EnsureScriptEnvironment_FUNC);
	rc = (jint)((nsIScriptGlobalObject *)arg0)->EnsureScriptEnvironment(arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptGlobalObject_1EnsureScriptEnvironment_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIScriptGlobalObject_1GetScriptContext
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject_1GetScriptContext)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject_1GetScriptContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptGlobalObject_1GetScriptContext_FUNC);
	rc = (jint)((nsIScriptGlobalObject *)arg0)->GetScriptContext(arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptGlobalObject_1GetScriptContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIScriptGlobalObject_1GetScriptGlobal
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject_1GetScriptGlobal)(JNIEnv *env, jclass that, jint arg0, jint arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject_1GetScriptGlobal)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptGlobalObject_1GetScriptGlobal_FUNC);
	rc = (jint)((nsIScriptGlobalObject *)arg0)->GetScriptGlobal(arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptGlobalObject_1GetScriptGlobal_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I)(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2);
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	nsDynamicFunctionLoad _arg1, *lparg1=NULL;
	XPCOM_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I_FUNC);
	if (arg1) if ((lparg1 = getnsDynamicFunctionLoadFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	XPCOM_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2);
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

#ifndef NO_memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II)(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2);
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

#ifndef NO_nsDynamicFunctionLoad_1sizeof
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(nsDynamicFunctionLoad_1sizeof)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL XPCOM_NATIVE(nsDynamicFunctionLoad_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, nsDynamicFunctionLoad_1sizeof_FUNC);
	rc = (jint)nsDynamicFunctionLoad_sizeof();
	XPCOM_NATIVE_EXIT(env, that, nsDynamicFunctionLoad_1sizeof_FUNC);
	return rc;
}
#endif


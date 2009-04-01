/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

#if (!defined(NO__1Call__I) && !defined(JNI64)) || (!defined(NO__1Call__J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__I)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__I)(JNIEnv *env, jclass that, jintLong arg0)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__J)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__J)(JNIEnv *env, jclass that, jintLong arg0)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1Call__I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1Call__J_FUNC);
#endif
	rc = (jint)((jint (*)())arg0)();
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1Call__I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1Call__J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1Call__IIIIII) && !defined(JNI64)) || (!defined(NO__1Call__JJJJJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__IIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__IIIIII)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__JJJJJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1Call__JJJJJI)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1Call__IIIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1Call__JJJJJI_FUNC);
#endif
	rc = (jint)((SWT_XREInitEmbedding)arg0)((nsILocalFile *)arg1, (nsILocalFile *)arg2, (nsIDirectoryServiceProvider *)arg3, (nsStaticModuleInfo const *)arg4, arg5);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1Call__IIIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1Call__JJJJJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1Call__III_3BII_3I) && !defined(JNI64)) || (!defined(NO__1Call__JJJ_3BII_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1Call__III_3BII_3I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1Call__III_3BII_3I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6)
#else
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1Call__JJJ_3BII_3I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1Call__JJJ_3BII_3I)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6)
#endif
{
	jbyte *lparg3=NULL;
	jint *lparg6=NULL;
	jintLong rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1Call__III_3BII_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1Call__JJJ_3BII_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jintLong)((nsWriteSegmentFun)arg0)((nsIInputStream *)arg1, (void *)arg2, (const char *)lparg3, arg4, arg5, (PRUint32 *)lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1Call__III_3BII_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1Call__JJJ_3BII_3I_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO__1NS_1GetComponentManager
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetComponentManager)(JNIEnv *env, jclass that, jintLongArray arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetComponentManager)
	(JNIEnv *env, jclass that, jintLongArray arg0)
{
	jintLong *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1GetComponentManager_FUNC);
	if (arg0) if ((lparg0 = env->GetIntLongArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)NS_GetComponentManager((nsIComponentManager**)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseIntLongArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1GetComponentManager_FUNC);
	return rc;
}
#endif

#ifndef NO__1NS_1GetServiceManager
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetServiceManager)(JNIEnv *env, jclass that, jintLongArray arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1GetServiceManager)
	(JNIEnv *env, jclass that, jintLongArray arg0)
{
	jintLong *lparg0=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1GetServiceManager_FUNC);
	if (arg0) if ((lparg0 = env->GetIntLongArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)NS_GetServiceManager((nsIServiceManager**)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseIntLongArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1GetServiceManager_FUNC);
	return rc;
}
#endif

#ifndef NO__1NS_1InitXPCOM2
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1InitXPCOM2)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1InitXPCOM2)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1InitXPCOM2_FUNC);
	rc = (jint)NS_InitXPCOM2((nsIServiceManager **)arg0, (nsIFile *)arg1, (nsIDirectoryServiceProvider *)arg2);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1InitXPCOM2_FUNC);
	return rc;
}
#endif

#ifndef NO__1NS_1NewLocalFile
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1NewLocalFile)(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1NewLocalFile)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1NewLocalFile_FUNC);
	if (arg2) if ((lparg2 = env->GetIntLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)NS_NewLocalFile(*(nsAString *)arg0, arg1, (nsILocalFile**)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntLongArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1NewLocalFile_FUNC);
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II)(JNIEnv *env, jclass that, jint arg0, jintLong arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II)(JNIEnv *env, jclass that, jint arg0, jintLong arg1)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong))(*(jintLong **)arg1)[arg0])(arg1);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IID) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJD) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IID)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jdouble arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IID)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jdouble arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJD)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jdouble arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJD)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jdouble arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IID_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJD_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jdouble))(*(jintLong **)arg1)[arg0])(arg1, arg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IID_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJD_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIF) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJF) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIF)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloat arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIF)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloat arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJF)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloat arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJF)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloat arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIF_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJF_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jfloat))(*(jintLong **)arg1)[arg0])(arg1, arg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIF_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJF_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIIIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIIIIIIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIIIIIIIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIIIIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIIIIIIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIIIIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIIIIIIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIIIIIIIIIIIISI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIIIIIIIIIIIISI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIIIIIISI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jint arg16);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIIIIIIIIIISI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jint arg16)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIIIIIIIIIISI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jint arg16);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIIIIIIIIIISI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jint arg16)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIIIIIIIIIISI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIIIIIIIIIIIISI_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jshort, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIIIIIIIIIISI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIIIIIIIIIIIISI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIIIII_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIIIII_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jcharArray arg8, jintArray arg9, jintArray arg10);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jcharArray arg8, jintArray arg9, jintArray arg10)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jcharArray arg8, jintArray arg9, jintArray arg10);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jcharArray arg8, jintArray arg9, jintArray arg10)
#endif
{
	jchar *lparg8=NULL;
	jint *lparg9=NULL;
	jint *lparg10=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIII_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIIIII_3C_3I_3I_FUNC);
#endif
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetIntArrayElements(arg10, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jint, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8, lparg9, lparg10);
fail:
	if (arg10 && lparg10) env->ReleaseIntArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIII_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIIIII_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIIIJII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIIIJII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jlong arg6, jint arg7, jint arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIIIJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jlong arg6, jint arg7, jint arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jlong arg6, jint arg7, jint arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIIIJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jlong arg6, jint arg7, jint arg8)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIIIJII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIIIJII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jlong, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIIIJII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIIIJII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIII_3CIIIII_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIII_3CIIIII_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3CIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jintArray arg12, jintArray arg13);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3CIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jintArray arg12, jintArray arg13)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3CIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jintArray arg12, jintArray arg13);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3CIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jintArray arg12, jintArray arg13)
#endif
{
	jchar *lparg6=NULL;
	jint *lparg12=NULL;
	jint *lparg13=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3CIIIII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIII_3CIIIII_3I_3I_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg12) if ((lparg12 = env->GetIntArrayElements(arg12, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetIntArrayElements(arg13, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jchar *, jint, jint, jint, jint, jint, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, arg7, arg8, arg9, arg10, arg11, lparg12, lparg13);
fail:
	if (arg13 && lparg13) env->ReleaseIntArrayElements(arg13, lparg13, 0);
	if (arg12 && lparg12) env->ReleaseIntArrayElements(arg12, lparg12, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3CIIIII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIII_3CIIIII_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIII_3C_3BIIIII_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIII_3C_3BIIIII_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3BIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jintArray arg13, jintArray arg14);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3BIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jintArray arg13, jintArray arg14)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3C_3BIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jintArray arg13, jintArray arg14);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3C_3BIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jintArray arg13, jintArray arg14)
#endif
{
	jchar *lparg6=NULL;
	jbyte *lparg7=NULL;
	jint *lparg13=NULL;
	jint *lparg14=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3C_3BIIIII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIII_3C_3BIIIII_3I_3I_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetIntArrayElements(arg13, NULL)) == NULL) goto fail;
	if (arg14) if ((lparg14 = env->GetIntArrayElements(arg14, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jchar *, jbyte *, jint, jint, jint, jint, jint, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, arg8, arg9, arg10, arg11, arg12, lparg13, lparg14);
fail:
	if (arg14 && lparg14) env->ReleaseIntArrayElements(arg14, lparg14, 0);
	if (arg13 && lparg13) env->ReleaseIntArrayElements(arg13, lparg13, 0);
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3C_3BIIIII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIII_3C_3BIIIII_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIII_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIII_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#endif
{
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIII_3C_3I_3I_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIII_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIII_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIII_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7)
#endif
{
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIII_3I_3I_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIII_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIII_3B_3BI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIII_3B_3BI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6, jint arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jbyteArray arg5, jbyteArray arg6, jint arg7)
#endif
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_3B_3BI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIII_3B_3BI_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jbyte *, jbyte *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, lparg6, arg7);
fail:
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_3B_3BI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIII_3B_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIII_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIII_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jcharArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jcharArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jcharArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jcharArray arg5)
#endif
{
	jchar *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIII_3C_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIII_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIII_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIII_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
#endif
{
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIII_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIII_3I_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIII_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIIJJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIIJJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIIJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIIJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIIJJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIIJJJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jlong, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIIJJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIIJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4)
#endif
{
	jbyte *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3B_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3BI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3BI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jbyteArray arg4, jint arg5)
#endif
{
	jbyte *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3BI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3BI_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jbyte *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3BI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4)
#endif
{
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3C_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3CIJI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3CIJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7)
#endif
{
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3CIJI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3CIJI_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jchar *, jint, jlong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3CIJI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3CIJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3CJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3CJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7)
#endif
{
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3CJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3CJJJ_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jchar *, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3CJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3CJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3C_3CI_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3C_3CI_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jcharArray arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3C_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jcharArray arg5, jint arg6, jintArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3C_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jcharArray arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3C_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jcharArray arg4, jcharArray arg5, jint arg6, jintArray arg7)
#endif
{
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3C_3CI_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3C_3CI_3I_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jchar *, jchar *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3C_3CI_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3C_3CI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintArray arg4)
#endif
{
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3I_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIII_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJII_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlongArray arg4)
#endif
{
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIII_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJII_3J_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIII_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJII_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIIJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJIJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jlong arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jlong arg4)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIIJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJIJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIIJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJIJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3)
#endif
{
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, nsID *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4)
#endif
{
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, nsID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3)
#endif
{
	jbyte *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3B_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3BI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3BI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4)
#endif
{
	jbyte *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3BI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3BI_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jbyte *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3BI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3BI_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3BI_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5)
#endif
{
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3BI_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3BI_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jbyte *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3BI_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3BI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3B_3B_3BI_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3B_3B_3BI_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3B_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3B_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B_3B_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B_3B_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jint arg6, jintArray arg7)
#endif
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_3B_3BI_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3B_3B_3BI_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jbyte *, jbyte *, jbyte *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_3B_3BI_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3B_3B_3BI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3B_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3B_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jcharArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jcharArray arg4)
#endif
{
	jbyte *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3B_3C_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jbyte *, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3B_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3B_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3B_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jbyteArray arg3, jintArray arg4)
#endif
{
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3B_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3B_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jbyte *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3B_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3B_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3)
#endif
{
	jchar *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3CI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3CI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jint arg4)
#endif
{
	jchar *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3CI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3CI_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3CI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3CI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3C_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11)
#endif
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
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetCharArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetIntArrayElements(arg10, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = env->GetIntArrayElements(arg11, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *, jint, jchar *, jchar *, jchar *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8, lparg9, lparg10, lparg11);
fail:
	if (arg11 && lparg11) env->ReleaseIntArrayElements(arg11, lparg11, 0);
	if (arg10 && lparg10) env->ReleaseIntArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseCharArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3CI_3I_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3CI_3I_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3CI_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3CI_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3CI_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jint arg5, jintArray arg6, jintArray arg7, jintArray arg8)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3CI_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3CI_3I_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *, jint, jint *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3CI_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3CI_3I_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3C_3C_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3C_3C_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3C_3C_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *, jchar *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3C_3C_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3C_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3C_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3C_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3C_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3C_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3C_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3C_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3C_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3C_3I_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3C_3I_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3I_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3C_3I_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *, jint *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3I_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3C_3I_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3C_3I_3I_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jintArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3C_3I_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jintArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jintArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jcharArray arg4, jintArray arg5, jintArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jchar *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3C_3I_3I_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jchar *, jint *, jint *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3C_3I_3I_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3C_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3C_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jcharArray arg3, jintArray arg4)
#endif
{
	jchar *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3C_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3C_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jchar *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3C_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3C_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3)
#endif
{
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__III_3I_3I_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJI_3I_3I_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__III_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJI_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
#endif
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__III_3I_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJI_3I_3I_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint *, jint *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__III_3I_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJI_3I_3I_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJI_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJIIJIIIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJIIJIIIIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJIIJIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJIIJIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIIJIIIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJIIJIIIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint, jint, jlong, jint, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIIJIIIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJIIJIIIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJIIJIIIIIIIIISJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJIIJIIIIIIIIISJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIIIIIISJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jlong arg16);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJIIIIIIIIISJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jlong arg16)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJIIJIIIIIIIIISJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jlong arg16);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJIIJIIIIIIIIISJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12, jint arg13, jint arg14, jshort arg15, jlong arg16)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIIJIIIIIIIIISJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJIIJIIIIIIIIISJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint, jint, jlong, jint, jint, jint, jint, jint, jint, jint, jint, jint, jshort, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIIJIIIIIIIIISJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJIIJIIIIIIIIISJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJIIJ_3I_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJIIJ_3I_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jintArray arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJIIJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jintArray arg6, jlongArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJIIJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jintArray arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJIIJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jint arg4, jlong arg5, jintArray arg6, jlongArray arg7)
#endif
{
	jint *lparg6=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJIIJ_3I_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJIIJ_3I_3J_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint, jint, jlong, jint *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJIIJ_3I_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJIIJ_3I_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJI_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJI_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jlongArray arg4)
#endif
{
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJI_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJI_3J_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJI_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJI_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJI_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJIIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jint arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jint arg7)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJIJ_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJIJ_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIJ_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jlong arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJIJ_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jlong arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJIJ_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jlong arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJIJ_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jlong arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#endif
{
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJIJ_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJIJ_3C_3I_3I_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint, jlong, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJIJ_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJIJ_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJI_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJI_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jcharArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jcharArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJI_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jcharArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJI_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jcharArray arg5)
#endif
{
	jchar *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJI_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJI_3C_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJI_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJI_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJI_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJI_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jint arg4, jintArray arg5)
#endif
{
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJI_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJI_3I_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJI_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJI_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJI_3CJJIJI_3J_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJI_3CJJIJI_3J_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3CJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jlong arg7, jlong arg8, jint arg9, jlong arg10, jint arg11, jlongArray arg12, jlongArray arg13);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3CJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jlong arg7, jlong arg8, jint arg9, jlong arg10, jint arg11, jlongArray arg12, jlongArray arg13)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJI_3CJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jlong arg7, jlong arg8, jint arg9, jlong arg10, jint arg11, jlongArray arg12, jlongArray arg13);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJI_3CJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jlong arg7, jlong arg8, jint arg9, jlong arg10, jint arg11, jlongArray arg12, jlongArray arg13)
#endif
{
	jchar *lparg6=NULL;
	jlong *lparg12=NULL;
	jlong *lparg13=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJI_3CJJIJI_3J_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJI_3CJJIJI_3J_3J_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg12) if ((lparg12 = env->GetLongArrayElements(arg12, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetLongArrayElements(arg13, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jint, jchar *, jlong, jlong, jint, jlong, jint, jlong *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, arg7, arg8, arg9, arg10, arg11, lparg12, lparg13);
fail:
	if (arg13 && lparg13) env->ReleaseLongArrayElements(arg13, lparg13, 0);
	if (arg12 && lparg12) env->ReleaseLongArrayElements(arg12, lparg12, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJI_3CJJIJI_3J_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJI_3CJJIJI_3J_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJI_3C_3BJJIJI_3J_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jlong arg8, jlong arg9, jint arg10, jlong arg11, jint arg12, jlongArray arg13, jlongArray arg14);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jlong arg8, jlong arg9, jint arg10, jlong arg11, jint arg12, jlongArray arg13, jlongArray arg14)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJI_3C_3BJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jlong arg8, jlong arg9, jint arg10, jlong arg11, jint arg12, jlongArray arg13, jlongArray arg14);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJI_3C_3BJJIJI_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jint arg5, jcharArray arg6, jbyteArray arg7, jlong arg8, jlong arg9, jint arg10, jlong arg11, jint arg12, jlongArray arg13, jlongArray arg14)
#endif
{
	jchar *lparg6=NULL;
	jbyte *lparg7=NULL;
	jlong *lparg13=NULL;
	jlong *lparg14=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJI_3C_3BJJIJI_3J_3J_FUNC);
#endif
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg13) if ((lparg13 = env->GetLongArrayElements(arg13, NULL)) == NULL) goto fail;
	if (arg14) if ((lparg14 = env->GetLongArrayElements(arg14, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jint, jchar *, jbyte *, jlong, jlong, jint, jlong, jint, jlong *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, lparg6, lparg7, arg8, arg9, arg10, arg11, arg12, lparg13, lparg14);
fail:
	if (arg14 && lparg14) env->ReleaseLongArrayElements(arg14, lparg14, 0);
	if (arg13 && lparg13) env->ReleaseLongArrayElements(arg13, lparg13, 0);
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJI_3C_3BJJIJI_3J_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJJIJ_3C_3I_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJJIJ_3C_3I_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJIJ_3C_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jint arg6, jlong arg7, jcharArray arg8, jintArray arg9, jlongArray arg10);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJIJ_3C_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jint arg6, jlong arg7, jcharArray arg8, jintArray arg9, jlongArray arg10)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJIJ_3C_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jint arg6, jlong arg7, jcharArray arg8, jintArray arg9, jlongArray arg10);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJIJ_3C_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jint arg6, jlong arg7, jcharArray arg8, jintArray arg9, jlongArray arg10)
#endif
{
	jchar *lparg8=NULL;
	jint *lparg9=NULL;
	jlong *lparg10=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJIJ_3C_3I_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJJIJ_3C_3I_3J_FUNC);
#endif
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetLongArrayElements(arg10, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jlong, jint, jlong, jchar *, jint *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, lparg8, lparg9, lparg10);
fail:
	if (arg10 && lparg10) env->ReleaseLongArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJIJ_3C_3I_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJJIJ_3C_3I_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJJJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJJJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJJJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJJJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6, jlong arg7, jlong arg8)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJJJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJJJJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jlong, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJJJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJJJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJ_3B_3BJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJ_3B_3BJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jbyteArray arg5, jbyteArray arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jbyteArray arg5, jbyteArray arg6, jlong arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJ_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jbyteArray arg5, jbyteArray arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJ_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jbyteArray arg5, jbyteArray arg6, jlong arg7)
#endif
{
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJ_3B_3BJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJ_3B_3BJ_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jbyte *, jbyte *, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, lparg6, arg7);
fail:
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJ_3B_3BJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJ_3B_3BJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJJ_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJJ_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlongArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlong arg4, jlongArray arg5)
#endif
{
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJJ_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJJ_3J_FUNC);
#endif
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJJ_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4)
#endif
{
	jbyte *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_3B_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ_3BJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ_3BJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jbyteArray arg4, jlong arg5)
#endif
{
	jbyte *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3BJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_3BJ_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jbyte *, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3BJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_3BJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ_3CIJI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ_3CIJI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3CIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jint arg5, jlong arg6, jint arg7)
#endif
{
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3CIJI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_3CIJI_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jchar *, jint, jlong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3CIJI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_3CIJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ_3CJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ_3CJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3CJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jlong arg5, jlong arg6, jlong arg7)
#endif
{
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3CJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_3CJJJ_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jchar *, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, arg6, arg7);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3CJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_3CJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ_3C_3CI_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ_3C_3CI_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3C_3CI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jcharArray arg5, jint arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3C_3CI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jcharArray arg5, jint arg6, jlongArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3C_3CI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jcharArray arg5, jint arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3C_3CI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jcharArray arg4, jcharArray arg5, jint arg6, jlongArray arg7)
#endif
{
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3C_3CI_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_3C_3CI_3J_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jchar *, jchar *, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3C_3CI_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_3C_3CI_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jintArray arg4)
#endif
{
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_3I_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJJ_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJJ_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jlongArray arg4)
#endif
{
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJJ_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJJ_3J_FUNC);
#endif
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJJ_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3)
#endif
{
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, nsID *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4)
#endif
{
	nsID _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, nsID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3)
#endif
{
	jbyte *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3B_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3BI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3BI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jint arg4)
#endif
{
	jbyte *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3BI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3BI_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jbyte *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3BI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3BJ_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3BJ_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jlong arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jlong arg4, jlongArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jlong arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jlong arg4, jlongArray arg5)
#endif
{
	jbyte *lparg3=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3BJ_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3BJ_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jbyte *, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3BJ_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3BJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3B_3B_3BJ_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3B_3B_3BJ_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3B_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jlong arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3B_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jlong arg6, jlongArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3B_3B_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jlong arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3B_3B_3BJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jbyteArray arg4, jbyteArray arg5, jlong arg6, jlongArray arg7)
#endif
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3B_3B_3BJ_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3B_3B_3BJ_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jbyte *, jbyte *, jbyte *, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3B_3B_3BJ_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3B_3B_3BJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3B_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3B_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jcharArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3B_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jbyteArray arg3, jcharArray arg4)
#endif
{
	jbyte *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3B_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3B_3C_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jbyte *, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3B_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3B_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3CI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3CI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jint arg4)
#endif
{
	jchar *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3CI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3CI_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3CI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3CI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3C_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jcharArray arg6, jcharArray arg7, jcharArray arg8, jcharArray arg9, jintArray arg10, jintArray arg11)
#endif
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
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetCharArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetCharArrayElements(arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = env->GetIntArrayElements(arg10, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = env->GetIntArrayElements(arg11, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jint, jchar *, jchar *, jchar *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8, lparg9, lparg10, lparg11);
fail:
	if (arg11 && lparg11) env->ReleaseIntArrayElements(arg11, lparg11, 0);
	if (arg10 && lparg10) env->ReleaseIntArrayElements(arg10, lparg10, 0);
	if (arg9 && lparg9) env->ReleaseCharArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseCharArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3CI_3J_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3CI_3J_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3J_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jlongArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3CI_3J_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jlongArray arg6, jintArray arg7, jintArray arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3CI_3J_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jlongArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3CI_3J_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jint arg5, jlongArray arg6, jintArray arg7, jintArray arg8)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3CI_3J_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3CI_3J_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetLongArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jint, jlong *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, arg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseLongArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3CI_3J_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3CI_3J_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3C_3C_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3C_3C_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3C_3C_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jchar *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3C_3C_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3C_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3C_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jcharArray arg5, jintArray arg6, jintArray arg7)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jchar *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3C_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetCharArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseCharArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3C_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3C_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3C_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jintArray arg5)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3C_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3C_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3C_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3C_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3C_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3C_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3C_3J_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3C_3J_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jcharArray arg6, jintArray arg7, jintArray arg8)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg5=NULL;
	jchar *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3J_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3C_3J_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetCharArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jlong *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8);
fail:
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseCharArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3J_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3C_3J_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jlongArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jlongArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jlongArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jcharArray arg4, jlongArray arg5, jlongArray arg6, jcharArray arg7, jintArray arg8, jintArray arg9)
#endif
{
	jchar *lparg3=NULL;
	jchar *lparg4=NULL;
	jlong *lparg5=NULL;
	jlong *lparg6=NULL;
	jchar *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetCharArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetLongArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetCharArrayElements(arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = env->GetIntArrayElements(arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = env->GetIntArrayElements(arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jchar *, jlong *, jlong *, jchar *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6, lparg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) env->ReleaseIntArrayElements(arg9, lparg9, 0);
	if (arg8 && lparg8) env->ReleaseIntArrayElements(arg8, lparg8, 0);
	if (arg7 && lparg7) env->ReleaseCharArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseLongArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseCharArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3C_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3C_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3C_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jcharArray arg3, jlongArray arg4)
#endif
{
	jchar *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3C_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3C_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jchar *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3C_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3C_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jintArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jintArray arg3)
#endif
{
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IIJ_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJJ_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IIJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlongArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlongArray arg3)
#endif
{
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IIJ_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJJ_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IIJ_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jobject arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jobject arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jobject arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jobject arg4, jintArray arg5)
#endif
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jint, nsID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jobject arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jobject arg4, jlongArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jobject arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jobject arg4, jlongArray arg5)
#endif
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jlong, nsID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jintArray arg4)
#endif
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, nsID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jlongArray arg4)
#endif
{
	nsID _arg2, *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, nsID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jbyte *, jbyte *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbyteArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbyteArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbyteArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jbyteArray arg6, jbyteArray arg7)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jbyte *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jbyte *, jbyte *, jint, jbyte *, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jbyte *, jbyte *, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5, jbyteArray arg6, jbyteArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5, jbyteArray arg6, jbyteArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5, jbyteArray arg6, jbyteArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jbyteArray arg3, jbyteArray arg4, jlong arg5, jbyteArray arg6, jbyteArray arg7)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg6=NULL;
	jbyte *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetByteArrayElements(arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetByteArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jbyte *, jbyte *, jlong, jbyte *, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, lparg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseByteArrayElements(arg7, lparg7, 0);
	if (arg6 && lparg6) env->ReleaseByteArrayElements(arg6, lparg6, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jintArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jintArray arg3)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlongArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlongArray arg3)
#endif
{
	nsID _arg2, *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = getnsIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, nsID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) setnsIDFields(env, arg2, lparg2);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IISIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJSIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jint arg3, jint arg4, jint arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJSIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jint arg3, jint arg4, jint arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJSIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jint arg3, jint arg4, jint arg5)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IISIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJSIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jshort, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IISIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJSIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__IISJIJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJSJIJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISJIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jlong arg3, jint arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IISJIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jlong arg3, jint arg4, jlong arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJSJIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jlong arg3, jint arg4, jlong arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJSJIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshort arg2, jlong arg3, jint arg4, jlong arg5)
#endif
{
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IISJIJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJSJIJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jshort, jlong, jint, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IISJIJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJSJIJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2)
#endif
{
	jbyte *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3)
#endif
{
	jbyte *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BI_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BII_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BII_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BII_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6)
#endif
{
	jbyte *lparg2=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BII_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint, jint, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BII_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BII_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BIJ_3J_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BIJ_3J_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BIJ_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlong arg4, jlongArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BIJ_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlong arg4, jlongArray arg5, jintArray arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BIJ_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlong arg4, jlongArray arg5, jintArray arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BIJ_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlong arg4, jlongArray arg5, jintArray arg6)
#endif
{
	jbyte *lparg2=NULL;
	jlong *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BIJ_3J_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BIJ_3J_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = env->GetIntArrayElements(arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint, jlong, jlong *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BIJ_3J_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BIJ_3J_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jobject arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jobject arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jobject arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jobject arg4, jintArray arg5)
#endif
{
	jbyte *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint, nsID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BI_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BI_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BI_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BI_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BI_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5)
#endif
{
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BI_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BI_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BI_3J_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BI_3J_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlongArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BI_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlongArray arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlongArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BI_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jint arg3, jlongArray arg4, jintArray arg5)
#endif
{
	jbyte *lparg2=NULL;
	jlong *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BI_3J_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BI_3J_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint, jlong *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BI_3J_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BI_3J_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3)
#endif
{
	jbyte *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BJ_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3, jobject arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3, jobject arg4, jlongArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3, jobject arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlong arg3, jobject arg4, jlongArray arg5)
#endif
{
	jbyte *lparg2=NULL;
	nsID _arg4, *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getnsIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jlong, nsID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) setnsIDFields(env, arg4, lparg4);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jint arg4)
#endif
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, nsID *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlong arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlong arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlong arg4)
#endif
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, nsID *, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jintArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, nsID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jobject arg3, jlongArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	nsID _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getnsIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, nsID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) setnsIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3B) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3B) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3)
#endif
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3B_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jbyte *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3B_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3BI) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3BI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jint arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3BI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jint arg4)
#endif
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3BI_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3BI_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jbyte *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3BI_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3BI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3B_3BII_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3B_3BII_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3BII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3BII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7)
#endif
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3BII_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3B_3BII_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jbyte *, jbyte *, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3BII_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3B_3BII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3B_3BII_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3B_3BII_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3BII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jlongArray arg7)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3BII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jlongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3BII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jlongArray arg7)
#endif
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3BII_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3B_3BII_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetByteArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jbyte *, jbyte *, jint, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseLongArrayElements(arg7, lparg7, 0);
	if (arg4 && lparg4) env->ReleaseByteArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3BII_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3B_3BII_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3B_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3B_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3B_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jbyte *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3B_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3B_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3B_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jlongArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3B_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3B_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetByteArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jbyte *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3B_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3B_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3)
#endif
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jintArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3I_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3I_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jintArray arg3, jlongArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3I_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3I_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jint *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3I_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3I_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3)
#endif
{
	jbyte *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3B_3J_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3B_3J_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3B_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3B_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jbyteArray arg2, jlongArray arg3, jintArray arg4)
#endif
{
	jbyte *lparg2=NULL;
	jlong *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3B_3J_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3B_3J_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jbyte *, jlong *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3B_3J_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3B_3J_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2)
#endif
{
	jchar *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3C_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3CIIII) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3CIIII) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#endif
{
	jchar *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CIIII_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3CIIII_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CIIII_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3CIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3CIJJJ) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3CIJJJ) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CIJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CIJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CIJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jlong arg4, jlong arg5, jlong arg6)
#endif
{
	jchar *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CIJJJ_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3CIJJJ_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint, jlong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CIJJJ_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3CIJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3CI_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3CI_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jintArray arg4)
#endif
{
	jchar *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CI_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3CI_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CI_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3CI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3CJ_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3CJ_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3CJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3CJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jlongArray arg4)
#endif
{
	jchar *lparg2=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3CJ_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3CJ_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3CJ_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3CJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3C_3C) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3C_3C) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jcharArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jcharArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jcharArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jcharArray arg3)
#endif
{
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3C_3C_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3C_3C_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetCharArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jchar *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseCharArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3C_3C_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3C_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3F) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3F) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3F)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloatArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3F)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloatArray arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3F)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloatArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3F)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jfloatArray arg2)
#endif
{
	jfloat *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3F_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3F_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetFloatArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jfloat *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseFloatArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3F_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3F_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2)
#endif
{
	jint *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3)
#endif
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3I_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3I_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4)
#endif
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3I_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3I_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3I_3I_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3I_3I_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
#endif
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3I_3I_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *, jint *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3I_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3I_3I_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3I_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3I_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3)
#endif
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3I_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3I_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3I_3J_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3I_3J_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jintArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jintArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3J_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jintArray arg4)
#endif
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3J_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3I_3J_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *, jlong *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3J_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3I_3J_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3I_3J_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3I_3J_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3I_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3I_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2, jlongArray arg3, jlongArray arg4)
#endif
{
	jint *lparg2=NULL;
	jlong *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3I_3J_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3I_3J_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetIntArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint *, jlong *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3I_3J_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3I_3J_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2)
#endif
{
	jlong *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3J_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3J_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3)
#endif
{
	jlong *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3J_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3J_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3J_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3J_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3J_3J_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3J_3J_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3J_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3J_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3J_3J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlongArray arg2, jlongArray arg3, jlongArray arg4)
#endif
{
	jlong *lparg2=NULL;
	jlong *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3J_3J_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3J_3J_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetLongArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetLongArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetLongArrayElements(arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong *, jlong *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) env->ReleaseLongArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseLongArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3J_3J_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3J_3J_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3S) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3S) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3S)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3S)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3S)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3S)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2)
#endif
{
	jshort *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3S_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3S_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetShortArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jshort *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseShortArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3S_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3S_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3SI_3I_3I) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3SI_3I_3I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jint arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jint arg3, jintArray arg4, jintArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3SI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jint arg3, jintArray arg4, jintArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3SI_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jint arg3, jintArray arg4, jintArray arg5)
#endif
{
	jshort *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3SI_3I_3I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3SI_3I_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetShortArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetIntArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jshort *, jint, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseShortArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3SI_3I_3I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3SI_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO__1VtblCall__II_3SJ_3I_3J) && !defined(JNI64)) || (!defined(NO__1VtblCall__IJ_3SJ_3I_3J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jlong arg3, jintArray arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__II_3SJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jlong arg3, jintArray arg4, jlongArray arg5)
#else
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3SJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jlong arg3, jintArray arg4, jlongArray arg5);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1VtblCall__IJ_3SJ_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jshortArray arg2, jlong arg3, jintArray arg4, jlongArray arg5)
#endif
{
	jshort *lparg2=NULL;
	jint *lparg4=NULL;
	jlong *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__II_3SJ_3I_3J_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1VtblCall__IJ_3SJ_3I_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = env->GetShortArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jshort *, jlong, jint *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseLongArrayElements(arg5, lparg5, 0);
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg2 && lparg2) env->ReleaseShortArrayElements(arg2, lparg2, 0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__II_3SJ_3I_3J_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1VtblCall__IJ_3SJ_3I_3J_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO__1XPCOMGlueLoadXULFunctions
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueLoadXULFunctions)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1XPCOMGlueLoadXULFunctions)
	(JNIEnv *env, jclass that, jintLong arg0)
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
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1Length)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedCString_1Length)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1Length_FUNC);
	rc = (jint)((nsEmbedCString *)arg0)->Length();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1delete
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedCString_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedCString_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1delete_FUNC);
	delete (nsEmbedCString *)arg0;
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1delete_FUNC);
}
#endif

#ifndef NO__1nsEmbedCString_1get
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1get)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1get)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1get_FUNC);
	rc = (jintLong)((nsEmbedCString *)arg0)->get();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1new__
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1new___FUNC);
	rc = (jintLong)new nsEmbedCString();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1new___FUNC);
	return rc;
}
#endif

#if (!defined(NO__1nsEmbedCString_1new__II) && !defined(JNI64)) || (!defined(NO__1nsEmbedCString_1new__JI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__II)(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
#else
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__JI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new__JI)(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
#endif
{
	jintLong rc = 0;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1new__II_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1new__JI_FUNC);
#endif
	rc = (jintLong)new nsEmbedCString((const char *)arg0, arg1);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1new__II_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1new__JI_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO__1nsEmbedCString_1new___3BI
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new___3BI)(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedCString_1new___3BI)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedCString_1new___3BI_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)new nsEmbedCString((const char *)lparg0, arg1);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedCString_1new___3BI_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1Length
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1Length)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsEmbedString_1Length)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1Length_FUNC);
	rc = (jint)((nsEmbedString *)arg0)->Length();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1Length_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1delete
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedString_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsEmbedString_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1delete_FUNC);
	delete (nsEmbedString *)arg0;
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1delete_FUNC);
}
#endif

#ifndef NO__1nsEmbedString_1get
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedString_1get)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedString_1get)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1get_FUNC);
	rc = (jintLong)((nsEmbedString *)arg0)->get();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1get_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1new__
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedString_1new__)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedString_1new__)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1new___FUNC);
	rc = (jintLong)new nsEmbedString();
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1new___FUNC);
	return rc;
}
#endif

#ifndef NO__1nsEmbedString_1new___3C
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedString_1new___3C)(JNIEnv *env, jclass that, jcharArray arg0);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsEmbedString_1new___3C)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsEmbedString_1new___3C_FUNC);
	if (arg0) if ((lparg0 = env->GetCharArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)new nsEmbedString((PRUnichar *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, _1nsEmbedString_1new___3C_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsID_1Equals
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsID_1Equals)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsID_1Equals)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsID_1Equals_FUNC);
	rc = (jint)((nsID *)arg0)->Equals(*(nsID *)arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsID_1Equals_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsID_1delete
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsID_1delete)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL XPCOM_NATIVE(_1nsID_1delete)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	XPCOM_NATIVE_ENTER(env, that, _1nsID_1delete_FUNC);
	delete (nsID *)arg0;
	XPCOM_NATIVE_EXIT(env, that, _1nsID_1delete_FUNC);
}
#endif

#ifndef NO__1nsID_1new
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsID_1new)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsID_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsID_1new_FUNC);
	rc = (jintLong)new nsID();
	XPCOM_NATIVE_EXIT(env, that, _1nsID_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIMemory_1Alloc
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIMemory_1Alloc)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIMemory_1Alloc)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIMemory_1Alloc_FUNC);
	rc = (jintLong)((nsIMemory *)arg0)->Alloc((size_t)arg1);
	XPCOM_NATIVE_EXIT(env, that, _1nsIMemory_1Alloc_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIMemory_1Realloc
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIMemory_1Realloc)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIMemory_1Realloc)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIMemory_1Realloc_FUNC);
	rc = (jintLong)((nsIMemory *)arg0)->Realloc((void *)arg1, (size_t)arg2);
	XPCOM_NATIVE_EXIT(env, that, _1nsIMemory_1Realloc_FUNC);
	return rc;
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2J) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2);
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#else
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__JLorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2);
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__JLorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2J)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
#endif
{
	nsDynamicFunctionLoad _arg1, *lparg1=NULL;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2J_FUNC);
#endif
	if (arg1) if ((lparg1 = getnsDynamicFunctionLoadFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2J_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I) && !defined(JNI64)) || (!defined(NO_memmove__JLorg_eclipse_swt_internal_mozilla_nsID_2I) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2);
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__JLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2);
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__JLorg_eclipse_swt_internal_mozilla_nsID_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	nsID _arg1, *lparg1=NULL;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, memmove__JLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getnsIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) setnsIDFields(env, arg1, lparg1);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, memmove__JLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II) && !defined(JNI64)) || (!defined(NO_memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2JI) && defined(JNI64))
#ifndef JNI64
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2);
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
extern "C" JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2);
JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	nsID _arg0, *lparg0=NULL;
#ifndef JNI64
	XPCOM_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II_FUNC);
#else
	XPCOM_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = getnsIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setnsIDFields(env, arg0, lparg0);
#ifndef JNI64
	XPCOM_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II_FUNC);
#else
	XPCOM_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2JI_FUNC);
#endif
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


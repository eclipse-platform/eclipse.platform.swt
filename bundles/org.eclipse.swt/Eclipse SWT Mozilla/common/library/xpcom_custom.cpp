/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

#ifndef NO_strlen_1PRUnichar
JNIEXPORT jint JNICALL XPCOM_NATIVE(strlen_1PRUnichar)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc;
	XPCOM_NATIVE_ENTER(env, that, strlen_1PRUnichar_FUNC);
	{
	const PRUnichar* lparg0 = NULL;
	if (arg0) lparg0 = (const PRUnichar *)arg0;
	PRUint32 len = 0;
	if (lparg0 != NULL)	while (*lparg0++ != 0) len++;
	rc = (jint)len;
	}
	XPCOM_NATIVE_EXIT(env, that, strlen_1PRUnichar_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1DefineFunction
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1DefineFunction)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1, jbyteArray arg2, jintLong arg3, jint arg4, jint arg5)
{
	jbyte *lpmozillaPath=NULL;
	jbyte *lparg2=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1DefineFunction_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_DefineFunction(arg0, arg1, lparg2, arg3, arg4, arg5);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_DefineFunction");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong, jintLong, jbyte *, jintLong, jint, jint))fp)(arg0, arg1, lparg2, arg3, arg4, arg5);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_DefineFunction");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jbyte *, jintLong, jint, jint))fp)(arg0, arg1, lparg2, arg3, arg4, arg5);
		}
#endif /* _WIN32 */
	}
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1DefineFunction_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1EvaluateUCScriptForPrincipals
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1JS_1EvaluateUCScriptForPrincipals)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1, jintLong arg2, jcharArray arg3, jint arg4, jbyteArray arg5, jint arg6, jintLongArray arg7)
{
	jbyte *lpmozillaPath=NULL;
	jchar *lparg3=NULL;
	jbyte *lparg5=NULL;
	jintLong *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1EvaluateUCScriptForPrincipals_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntLongArrayElements(arg7, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_EvaluateUCScriptForPrincipals(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, lparg7);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_EvaluateUCScriptForPrincipals");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jint)((jint (*)(jintLong, jintLong, jintLong, jchar *, jint, jbyte *, jint, jintLong *))fp)(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, lparg7);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_EvaluateUCScriptForPrincipals");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jintLong, jchar *, jint, jbyte *, jint, jintLong *))fp)(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, lparg7);
		}
#endif /* _WIN32 */
	}
fail:
	if (arg7 && lparg7) env->ReleaseIntLongArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1EvaluateUCScriptForPrincipals_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1GetGlobalObject
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1GetGlobalObject)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1GetGlobalObject_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_GetGlobalObject(arg0);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_GetGlobalObject");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong))fp)(arg0);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_GetGlobalObject");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1GetGlobalObject_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1GetGlobalObject
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1GetGlobalForScopeChain)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1GetGlobalForScopeChain_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_GetGlobalForScopeChain(arg0);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_GetGlobalForScopeChain");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong))fp)(arg0);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_GetGlobalForScopeChain");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1GetGlobalForScopeChain_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1NewObject
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1NewObject)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1NewObject_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_GetGlobalObject(arg0, arg1, arg2, arg3);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_NewObject");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong, jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2, arg3);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_NewObject");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong, jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2, arg3);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1NewObject_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1ValueToString
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1ValueToString)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1ValueToString_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_ValueToString(arg0, arg1, arg2, arg3);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_ValueToString");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong, jintLong))fp)(arg0, arg1);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_ValueToString");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1ValueToString_FUNC);
	return rc;
}
#endif



JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JSVAL_1TO_1IMPL2)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JSVAL_TO_IMPL2(arg0);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JSVAL_TO_IMPL2");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong))fp)(arg0);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JSVAL_TO_IMPL2");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	return rc;
}

#ifndef NO__1JS_1GetStringCharsZ
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1GetStringCharsZ)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1GetStringCharsZ_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_GetStringCharsZ(arg0, arg1, arg2, arg3);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_GetStringCharsZ");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong, jintLong))fp)(arg0, arg1);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_GetStringCharsZ");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1GetStringCharsZ_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1TypeOfValue
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1TypeOfValue)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1TypeOfValue_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_TypeOfValue(arg0, arg1);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_TypeOfValue");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong, jintLong))fp)(arg0, arg1);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_TypeOfValue");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1TypeOfValue_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1GetStringLength
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1GetStringLength)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0)
{
	jbyte *lpmozillaPath=NULL;
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1GetStringLength_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_GetStringLength(arg0);
*/
	{
	
#ifdef _WIN32
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				fp = GetProcAddress(hm, "JS_GetStringLength");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (*)(jintLong))fp)(arg0);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_GetStringLength");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jintLong)((jint (CALLING_CONVENTION*)(jintLong))fp)(arg0);
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1GetStringLength_FUNC);
	return rc;
}
#endif





#ifndef NO__1NS_1Free
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1NS_1Free)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0)
{
	jbyte *lpmozillaPath=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1NS_1Free_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
/*
	NS_Free((void*)arg0);
*/
	{
	
#ifdef _WIN32
		XPCOM_LOAD_FUNCTION(fp, NS_Free)
		if (fp) {
			((jint (*)(void *))fp)((void *)arg0);
			rc = 1;
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "NS_Free");
			}
			initialized = 1;
		}
		if (fp) {
			((jint (CALLING_CONVENTION*)(void *))fp)((void *)arg0);
			rc = 1;
		}
#endif /* _WIN32 */
	}
fail:
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1NS_1Free_FUNC);
	return rc;
}
#endif
}

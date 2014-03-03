/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "xpcom24_custom.h"

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

#ifndef NO_CALLBACK_1GetScriptableFlags24
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(CALLBACK_1GetScriptableFlags24)(JNIEnv *env, jclass that, jintLong arg0);
static jintLong CALLBACK_1GetScriptableFlags24;
static jint proc_CALLBACK_1GetScriptableFlags24() {
	return ((jint (CALLING_CONVENTION*)())CALLBACK_1GetScriptableFlags24)();
}
static jintLong CALLBACK_GetScriptableFlags24(jintLong func) {
	CALLBACK_1GetScriptableFlags24 = func;
	return (jintLong)proc_CALLBACK_1GetScriptableFlags24;
}
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(CALLBACK_1GetScriptableFlags24)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, CALLBACK_1GetScriptableFlags24_FUNC);
	rc = (jintLong)CALLBACK_GetScriptableFlags24(arg0);
	XPCOM_NATIVE_EXIT(env, that, CALLBACK_1GetScriptableFlags24_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1DefineFunction24
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1DefineFunction24)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jintLong arg3, jint arg4, jint arg5);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1DefineFunction24)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jintLong arg3, jint arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jintLong result = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1DefineFunction24_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	result = (jintLong)JS_DefineFunction((JSContext *)arg0, (JSObject *)arg1, (const char *)lparg2, (JSNative)arg3, arg4, arg5);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1DefineFunction24_FUNC);
	return result;
}
#endif

#ifndef NO__1JS_1EvaluateUCScriptForPrincipals24
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1JS_1EvaluateUCScriptForPrincipals24) (JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1, jintLong arg2, jcharArray arg3, jint arg4, jbyteArray arg5, jint arg6, jintLong arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1JS_1EvaluateUCScriptForPrincipals24)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, jintLong arg0, jintLong arg1, jintLong arg2, jcharArray arg3, jint arg4, jbyteArray arg5, jint arg6, jintLong arg7)
{
	jbyte *lpmozillaPath=NULL;
	jchar *lparg3=NULL;
	jbyte *lparg5=NULL;
	jint rc = 0;

	XPCOM_NATIVE_ENTER(env, that, _1JS_1EvaluateUCScriptForPrincipals24_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_EvaluateUCScriptForPrincipals(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, arg7);
*/
	{
	
		/*
		 * As of XULRunner 24 an AutoCxPusher-derived instance must be used to push the current JS context
		 * on the stack, see https://bugzilla.mozilla.org/show_bug.cgi?id=911161.  In earlier supported
		 * XULRunner releases this was done with an nsIJSContextStack.  This JSAutoCompartment creation must
		 * happen on the execution stack, and therefore cannot be invoked dynamically.
		 */
		JSAutoCompartment ac((JSContext*)arg0, (JSObject*)arg1);

#ifdef _WIN32
		/*
		 * VC++6.0 cannot create a lookup symbol for JS_EvaluateUCScriptForPrincipals() that matches the symbol
		 * exported by XULRunner 24's mozjs library because VC++6.0 does not define wchar_t as a native type.
		 * The workaround is to dynamically look up the function's mangled name and invoke it directly.
		 */
		static int initialized = 0;
		static FARPROC fp = NULL;
		if (!initialized) {
			HMODULE hm = LoadLibrary((const char *)lpmozillaPath);
			if (hm) {
				/* try the 32-bit signature first */
				fp = GetProcAddress(hm, "?JS_EvaluateUCScriptForPrincipals@@YAHPAUJSContext@@PAVJSObject@@PAUJSPrincipals@@PB_WIPBDIPAVValue@JS@@@Z");
				if (!fp) {
					/* fall back to the 64-bit signature */
					fp = GetProcAddress(hm, "?JS_EvaluateUCScriptForPrincipals@@YAHPEAUJSContext@@PEAVJSObject@@PEAUJSPrincipals@@PEB_WIPEBDIPEAVValue@JS@@@Z");
				}
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jint)((jint (*)(jintLong, jintLong, jintLong, jchar *, jint, jbyte *, jint, jintLong))fp)(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, arg7);
		}
#else
		rc = (jint)JS_EvaluateUCScriptForPrincipals((JSContext*)arg0, (JSObject*)arg1, (JSPrincipals*)arg2, (const jschar *)lparg3, arg4, (const char*)lparg5, arg6, (JS::Value*)arg7);
#endif /* _WIN32 */
	}
fail:
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1EvaluateUCScriptForPrincipals_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1GetGlobalForScopeChain24
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1GetGlobalForScopeChain24) (JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1JS_1GetGlobalForScopeChain24)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong result = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1GetGlobalForScopeChain24_FUNC);
	result = (jintLong)JS_GetGlobalForScopeChain((JSContext*)arg0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1GetGlobalForScopeChain24_FUNC);
	return result;
}
#endif

#ifndef NO__1nsIScriptContext24_1GetNativeContext
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIScriptContext24_1GetNativeContext)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIScriptContext24_1GetNativeContext)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptContext24_1GetNativeContext_FUNC);
	rc = (jintLong)((nsIScriptContext *)arg0)->GetNativeContext();
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptContext24_1GetNativeContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIScriptGlobalObject24_1EnsureScriptEnvironment
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject24_1EnsureScriptEnvironment)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject24_1EnsureScriptEnvironment)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptGlobalObject24_1EnsureScriptEnvironment_FUNC);
	rc = (jint)((nsIScriptGlobalObject *)arg0)->EnsureScriptEnvironment();
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptGlobalObject24_1EnsureScriptEnvironment_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIScriptGlobalObject24_1GetGlobalJSObject
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject24_1GetGlobalJSObject)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject24_1GetGlobalJSObject)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptGlobalObject24_1GetGlobalJSObject_FUNC);
	rc = (jintLong)((nsIGlobalObject *)arg0)->GetGlobalJSObject();
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptGlobalObject24_1GetGlobalJSObject_FUNC);
	return rc;
}
#endif

#ifndef NO__1nsIScriptGlobalObject24_1GetScriptContext
extern "C" JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject24_1GetScriptContext)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL XPCOM_NATIVE(_1nsIScriptGlobalObject24_1GetScriptContext)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1nsIScriptGlobalObject24_1GetScriptContext_FUNC);
	rc = (jintLong)((nsIScriptGlobalObject *)arg0)->GetScriptContext();
	XPCOM_NATIVE_EXIT(env, that, _1nsIScriptGlobalObject24_1GetScriptContext_FUNC);
	return rc;
}
#endif

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

#ifndef NO__1JS_1EvaluateUCScriptForPrincipals24
extern "C" JNIEXPORT jint JNICALL XPCOM_NATIVE(_1JS_1EvaluateUCScriptForPrincipals24) (JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jcharArray arg3, jint arg4, jbyteArray arg5, jint arg6, jintLongArray arg7);
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1JS_1EvaluateUCScriptForPrincipals24)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jcharArray arg3, jint arg4, jbyteArray arg5, jint arg6, jintLongArray arg7)
{
	jchar *lparg3=NULL;
	jbyte *lparg5=NULL;
	jintLong *lparg7=NULL;
	jint rc = 0;

	/*
	 * As of XULRunner 24 an AutoCxPusher-derived instance must be used to push the current JS context
	 * on the stack, see https://bugzilla.mozilla.org/show_bug.cgi?id=911161.  In earlier supported
	 * XULRunner releases this was done with an nsIJSContextStack.
	 */
	JSAutoCompartment ac((JSContext*)arg0, (JSObject*)arg1);

	XPCOM_NATIVE_ENTER(env, that, _1JS_1EvaluateUCScriptForPrincipals24_FUNC);
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetIntLongArrayElements(arg7, NULL)) == NULL) goto fail;
	rc = (jint)JS_EvaluateUCScriptForPrincipals((JSContext*)arg0, (JSObject*)arg1, (JSPrincipals*)arg2, lparg3, arg4, lparg5, arg6, (jsval*)lparg7);
fail:
	if (arg7 && lparg7) env->ReleaseIntLongArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
 	XPCOM_NATIVE_EXIT(env, that, _1JS_1EvaluateUCScriptForPrincipals24_FUNC);
	return rc;
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

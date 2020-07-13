/********************************************************************************
 * Copyright (c) 2020 Equo
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Guillermo Zunino, Equo - initial implementation
 ********************************************************************************/


#include "swt.h"
#include "chromiumlib_structs.h"
#include "chromiumlib_stats.h"

#ifndef ChromiumLib_NATIVE
#define ChromiumLib_NATIVE(func) Java_org_eclipse_swt_internal_chromium_lib_ChromiumLib_##func
#endif

#ifndef NO_cefswt_1cstring_1to_1java
JNIEXPORT jstring JNICALL ChromiumLib_NATIVE(cefswt_1cstring_1to_1java)
(JNIEnv *env, jclass that, jintLong arg0)
{
	jstring rc = 0;
	ChromiumLib_NATIVE_ENTER(env, that, cefswt_1cstring_1to_1java_FUNC);
	rc = (*env)->NewStringUTF(env, (const char *)arg0);
	ChromiumLib_NATIVE_EXIT(env, that, cefswt_1cstring_1to_1java_FUNC);
	return rc;
}
#endif

#ifndef NO_cefswt_1cefstring_1to_1java
JNIEXPORT jstring JNICALL ChromiumLib_NATIVE(cefswt_1cefstring_1to_1java)
(JNIEnv *env, jclass that, jintLong arg0)
{
	jstring rc = 0;
	ChromiumLib_NATIVE_ENTER(env, that, cefswt_1cefstring_1to_1java_FUNC);
	const char* rc1 = cefswt_cefstring_to_java((void *)arg0);
	rc = (*env)->NewStringUTF(env, rc1);
	ChromiumLib_NATIVE_EXIT(env, that, cefswt_1cefstring_1to_1java_FUNC);
	return rc;
}
#endif

#ifndef NO_cefswt_1request_1to_1java
JNIEXPORT jstring JNICALL ChromiumLib_NATIVE(cefswt_1request_1to_1java)
(JNIEnv *env, jclass that, jintLong arg0)
{
	jstring rc = 0;
	ChromiumLib_NATIVE_ENTER(env, that, cefswt_1request_1to_1java_FUNC);
	const char* rc1 = cefswt_request_to_java((void *)arg0);
	rc = (*env)->NewStringUTF(env, rc1);
	ChromiumLib_NATIVE_EXIT(env, that, cefswt_1request_1to_1java_FUNC);
	return rc;
}
#endif

#ifndef NO_cefswt_1cookie_1to_1java
JNIEXPORT jstring JNICALL ChromiumLib_NATIVE(cefswt_1cookie_1to_1java)
(JNIEnv *env, jclass that, jintLong arg0)
{
	jstring rc = 0;
	ChromiumLib_NATIVE_ENTER(env, that, cefswt_1cookie_1to_1java_FUNC);
	const char* rc1 = cefswt_cookie_to_java((void *)arg0);
	rc = (*env)->NewStringUTF(env, rc1);
	ChromiumLib_NATIVE_EXIT(env, that, cefswt_1cookie_1to_1java_FUNC);
	return rc;
}
#endif

#ifndef NO_cefswt_1cookie_1value
JNIEXPORT jstring JNICALL ChromiumLib_NATIVE(cefswt_1cookie_1value)
(JNIEnv *env, jclass that, jintLong arg0)
{
	jstring rc = 0;
	ChromiumLib_NATIVE_ENTER(env, that, cefswt_1cookie_1value_FUNC);
	const char* rc1 = cefswt_cookie_value((void *)arg0);
	rc = (*env)->NewStringUTF(env, rc1);
	ChromiumLib_NATIVE_EXIT(env, that, cefswt_1cookie_1value_FUNC);
	return rc;
}
#endif

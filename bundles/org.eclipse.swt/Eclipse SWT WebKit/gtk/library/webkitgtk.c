/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "webkitgtk_structs.h"
#include "webkitgtk_stats.h"

#define WebKitGTK_NATIVE(func) Java_org_eclipse_swt_internal_webkit_WebKitGTK_##func

#ifndef NO_JSClassDefinition_1sizeof
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(JSClassDefinition_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, JSClassDefinition_1sizeof_FUNC);
	rc = (jint)JSClassDefinition_sizeof();
	WebKitGTK_NATIVE_EXIT(env, that, JSClassDefinition_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSClassCreate
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSClassCreate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSClassCreate_FUNC);
	rc = (jintLong)JSClassCreate((const JSClassDefinition*)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSClassCreate_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSContextGetGlobalObject
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSContextGetGlobalObject)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSContextGetGlobalObject_FUNC);
	rc = (jintLong)JSContextGetGlobalObject((JSContextRef)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSContextGetGlobalObject_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSEvaluateScript
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSEvaluateScript)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jint arg4, jintLongArray arg5)
{
	jintLong *lparg5=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSEvaluateScript_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jintLong)JSEvaluateScript((JSContextRef)arg0, (JSStringRef)arg1, (JSObjectRef)arg2, (JSStringRef)arg3, arg4, (JSValueRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntLongArrayElements(env, arg5, lparg5, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSEvaluateScript_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSObjectGetPrivate
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSObjectGetPrivate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSObjectGetPrivate_FUNC);
	rc = (jintLong)JSObjectGetPrivate((JSObjectRef)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSObjectGetPrivate_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSObjectGetProperty
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSObjectGetProperty)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLongArray arg3)
{
	jintLong *lparg3=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSObjectGetProperty_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)JSObjectGetProperty((JSContextRef)arg0, (JSObjectRef)arg1, (JSStringRef)arg2, (JSValueRef*)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSObjectGetProperty_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSObjectGetPropertyAtIndex
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSObjectGetPropertyAtIndex)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLongArray arg3)
{
	jintLong *lparg3=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSObjectGetPropertyAtIndex_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)JSObjectGetPropertyAtIndex((JSContextRef)arg0, (JSObjectRef)arg1, (unsigned)arg2, (JSValueRef*)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSObjectGetPropertyAtIndex_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSObjectMake
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSObjectMake)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSObjectMake_FUNC);
	rc = (jintLong)JSObjectMake((JSContextRef)arg0, (JSClassRef)arg1, (void *)arg2);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSObjectMake_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSObjectMakeArray
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSObjectMakeArray)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSObjectMakeArray_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)JSObjectMakeArray((JSContextRef)arg0, (size_t)arg1, (const struct OpaqueJSValue * const*)lparg2, (JSValueRef*)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSObjectMakeArray_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSObjectMakeFunctionWithCallback
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSObjectMakeFunctionWithCallback)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSObjectMakeFunctionWithCallback_FUNC);
	rc = (jintLong)JSObjectMakeFunctionWithCallback((JSContextRef)arg0, (JSStringRef)arg1, (JSObjectCallAsFunctionCallback)arg2);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSObjectMakeFunctionWithCallback_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSObjectSetProperty
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1JSObjectSetProperty)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jint arg4, jintLongArray arg5)
{
	jintLong *lparg5=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSObjectSetProperty_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	JSObjectSetProperty((JSContextRef)arg0, (JSObjectRef)arg1, (JSStringRef)arg2, (JSValueRef)arg3, (JSPropertyAttributes)arg4, (JSValueRef *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntLongArrayElements(env, arg5, lparg5, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSObjectSetProperty_FUNC);
}
#endif

#ifndef NO__1JSStringCreateWithUTF8CString
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSStringCreateWithUTF8CString)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSStringCreateWithUTF8CString_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)JSStringCreateWithUTF8CString((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSStringCreateWithUTF8CString_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSStringGetLength
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSStringGetLength)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSStringGetLength_FUNC);
	rc = (jintLong)JSStringGetLength((JSStringRef)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSStringGetLength_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSStringGetMaximumUTF8CStringSize
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSStringGetMaximumUTF8CStringSize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSStringGetMaximumUTF8CStringSize_FUNC);
	rc = (jintLong)JSStringGetMaximumUTF8CStringSize((JSStringRef)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSStringGetMaximumUTF8CStringSize_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSStringGetUTF8CString
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSStringGetUTF8CString)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSStringGetUTF8CString_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)JSStringGetUTF8CString((JSStringRef)arg0, (char *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSStringGetUTF8CString_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSStringIsEqualToUTF8CString
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1JSStringIsEqualToUTF8CString)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSStringIsEqualToUTF8CString_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)JSStringIsEqualToUTF8CString((JSStringRef)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSStringIsEqualToUTF8CString_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSStringRelease
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1JSStringRelease)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1JSStringRelease_FUNC);
	JSStringRelease((JSStringRef)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSStringRelease_FUNC);
}
#endif

#ifndef NO__1JSValueGetType
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1JSValueGetType)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueGetType_FUNC);
	rc = (jint)JSValueGetType((JSContextRef)arg0, (JSValueRef)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueGetType_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueIsObjectOfClass
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1JSValueIsObjectOfClass)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueIsObjectOfClass_FUNC);
	rc = (jint)JSValueIsObjectOfClass((JSContextRef)arg0, (JSValueRef)arg1, (JSClassRef)arg2);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueIsObjectOfClass_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueMakeBoolean
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSValueMakeBoolean)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueMakeBoolean_FUNC);
	rc = (jintLong)JSValueMakeBoolean((JSContextRef)arg0, (bool)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueMakeBoolean_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueMakeNumber
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSValueMakeNumber)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueMakeNumber_FUNC);
	rc = (jintLong)JSValueMakeNumber((JSContextRef)arg0, (double)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueMakeNumber_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueMakeString
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSValueMakeString)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueMakeString_FUNC);
	rc = (jintLong)JSValueMakeString((JSContextRef)arg0, (JSStringRef)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueMakeString_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueMakeUndefined
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSValueMakeUndefined)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueMakeUndefined_FUNC);
	rc = (jintLong)JSValueMakeUndefined((JSContextRef)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueMakeUndefined_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueToBoolean
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1JSValueToBoolean)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueToBoolean_FUNC);
	rc = (jint)JSValueToBoolean((JSContextRef)arg0, (JSValueRef)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueToBoolean_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueToNumber
JNIEXPORT jdouble JNICALL WebKitGTK_NATIVE(_1JSValueToNumber)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jdouble rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueToNumber_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jdouble)JSValueToNumber((JSContextRef)arg0, (JSValueRef)arg1, (JSValueRef*)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueToNumber_FUNC);
	return rc;
}
#endif

#ifndef NO__1JSValueToStringCopy
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1JSValueToStringCopy)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1JSValueToStringCopy_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntLongArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)JSValueToStringCopy((JSContextRef)arg0, (JSValueRef)arg1, (JSValueRef*)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntLongArrayElements(env, arg2, lparg2, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1JSValueToStringCopy_FUNC);
	return rc;
}
#endif

#ifndef NO__1SOUP_1IS_1SESSION
JNIEXPORT jboolean JNICALL WebKitGTK_NATIVE(_1SOUP_1IS_1SESSION)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1SOUP_1IS_1SESSION_FUNC);
	rc = (jboolean)SOUP_IS_SESSION(arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1SOUP_1IS_1SESSION_FUNC);
	return rc;
}
#endif

#ifndef NO__1SoupCookie_1expires
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1SoupCookie_1expires)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1SoupCookie_1expires_FUNC);
	rc = (jintLong)((SoupCookie *)arg0)->expires;
	WebKitGTK_NATIVE_EXIT(env, that, _1SoupCookie_1expires_FUNC);
	return rc;
}
#endif

#ifndef NO__1SoupMessage_1method
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1SoupMessage_1method)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1SoupMessage_1method_FUNC);
	((SoupMessage *)arg0)->method = ((const char *)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1SoupMessage_1method_FUNC);
}
#endif

#ifndef NO__1SoupMessage_1request_1body
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1SoupMessage_1request_1body)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1SoupMessage_1request_1body_FUNC);
	rc = (jintLong)((SoupMessage *)arg0)->request_body;
	WebKitGTK_NATIVE_EXIT(env, that, _1SoupMessage_1request_1body_FUNC);
	return rc;
}
#endif

#ifndef NO__1SoupMessage_1request_1headers
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1SoupMessage_1request_1headers)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1SoupMessage_1request_1headers_FUNC);
	rc = (jintLong)((SoupMessage *)arg0)->request_headers;
	WebKitGTK_NATIVE_EXIT(env, that, _1SoupMessage_1request_1headers_FUNC);
	return rc;
}
#endif

#ifndef NO__1WEBKIT_1IS_1WEB_1FRAME
JNIEXPORT jboolean JNICALL WebKitGTK_NATIVE(_1WEBKIT_1IS_1WEB_1FRAME)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1WEBKIT_1IS_1WEB_1FRAME_FUNC);
	rc = (jboolean)WEBKIT_IS_WEB_FRAME(arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1WEBKIT_1IS_1WEB_1FRAME_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1auth_1authenticate
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1auth_1authenticate)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1auth_1authenticate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	soup_auth_authenticate((SoupAuth *)arg0, (const char *)lparg1, (const char *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1auth_1authenticate_FUNC);
}
#endif

#ifndef NO__1soup_1auth_1get_1host
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1auth_1get_1host)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1auth_1get_1host_FUNC);
	rc = (jintLong)soup_auth_get_host((SoupAuth *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1auth_1get_1host_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1auth_1get_1scheme_1name
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1auth_1get_1scheme_1name)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1auth_1get_1scheme_1name_FUNC);
	rc = (jintLong)soup_auth_get_scheme_name((SoupAuth *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1auth_1get_1scheme_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1cookie_1jar_1add_1cookie
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1cookie_1jar_1add_1cookie)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1cookie_1jar_1add_1cookie_FUNC);
	soup_cookie_jar_add_cookie((SoupCookieJar *)arg0, (SoupCookie *)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1cookie_1jar_1add_1cookie_FUNC);
}
#endif

#ifndef NO__1soup_1cookie_1jar_1all_1cookies
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1cookie_1jar_1all_1cookies)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1cookie_1jar_1all_1cookies_FUNC);
	rc = (jintLong)soup_cookie_jar_all_cookies((SoupCookieJar *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1cookie_1jar_1all_1cookies_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1cookie_1jar_1delete_1cookie
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1cookie_1jar_1delete_1cookie)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1cookie_1jar_1delete_1cookie_FUNC);
	soup_cookie_jar_delete_cookie((SoupCookieJar *)arg0, (SoupCookie *)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1cookie_1jar_1delete_1cookie_FUNC);
}
#endif

#ifndef NO__1soup_1cookie_1jar_1get_1cookies
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1cookie_1jar_1get_1cookies)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1cookie_1jar_1get_1cookies_FUNC);
	rc = (jintLong)soup_cookie_jar_get_cookies((SoupCookieJar *)arg0, (SoupURI *)arg1, arg2);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1cookie_1jar_1get_1cookies_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1cookie_1jar_1get_1type
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1cookie_1jar_1get_1type)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1cookie_1jar_1get_1type_FUNC);
	rc = (jintLong)soup_cookie_jar_get_type();
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1cookie_1jar_1get_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1cookie_1parse
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1cookie_1parse)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1cookie_1parse_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)soup_cookie_parse((const char *)lparg0, (SoupURI *)arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1cookie_1parse_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1message_1body_1append
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1message_1body_1append)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1message_1body_1append_FUNC);
	soup_message_body_append((SoupMessageBody *)arg0, (SoupMemoryUse)arg1, (gconstpointer)arg2, (gsize)arg3);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1message_1body_1append_FUNC);
}
#endif

#ifndef NO__1soup_1message_1body_1flatten
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1message_1body_1flatten)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1message_1body_1flatten_FUNC);
	soup_message_body_flatten((SoupMessageBody *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1message_1body_1flatten_FUNC);
}
#endif

#ifndef NO__1soup_1message_1get_1uri
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1message_1get_1uri)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1message_1get_1uri_FUNC);
	rc = (jintLong)soup_message_get_uri((SoupMessage *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1message_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1message_1headers_1append
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1message_1headers_1append)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1message_1headers_1append_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	soup_message_headers_append((SoupMessageHeaders *)arg0, (const char *)lparg1, (const char *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1message_1headers_1append_FUNC);
}
#endif

#ifndef NO__1soup_1session_1add_1feature_1by_1type
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1session_1add_1feature_1by_1type)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1session_1add_1feature_1by_1type_FUNC);
	soup_session_add_feature_by_type((SoupSession *)arg0, (GType)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1session_1add_1feature_1by_1type_FUNC);
}
#endif

#ifndef NO__1soup_1session_1feature_1attach
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1session_1feature_1attach)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1session_1feature_1attach_FUNC);
	soup_session_feature_attach((SoupSessionFeature *)arg0, (SoupSession *)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1session_1feature_1attach_FUNC);
}
#endif

#ifndef NO__1soup_1session_1feature_1detach
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1session_1feature_1detach)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1session_1feature_1detach_FUNC);
	soup_session_feature_detach((SoupSessionFeature *)arg0, (SoupSession *)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1session_1feature_1detach_FUNC);
}
#endif

#ifndef NO__1soup_1session_1get_1feature
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1session_1get_1feature)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1session_1get_1feature_FUNC);
	rc = (jintLong)soup_session_get_feature((SoupSession *)arg0, (GType)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1session_1get_1feature_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1uri_1free
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1soup_1uri_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1uri_1free_FUNC);
	soup_uri_free((SoupURI *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1uri_1free_FUNC);
}
#endif

#ifndef NO__1soup_1uri_1new
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1uri_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1uri_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)soup_uri_new((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1uri_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1soup_1uri_1to_1string
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1soup_1uri_1to_1string)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1soup_1uri_1to_1string_FUNC);
	rc = (jintLong)soup_uri_to_string((SoupURI *)arg0, arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1soup_1uri_1to_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1download_1cancel
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1download_1cancel)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1download_1cancel_FUNC);
	webkit_download_cancel((WebKitDownload *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1download_1cancel_FUNC);
}
#endif

#ifndef NO__1webkit_1download_1get_1current_1size
JNIEXPORT jlong JNICALL WebKitGTK_NATIVE(_1webkit_1download_1get_1current_1size)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jlong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1download_1get_1current_1size_FUNC);
	rc = (jlong)webkit_download_get_current_size((WebKitDownload *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1download_1get_1current_1size_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1download_1get_1status
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1download_1get_1status)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1download_1get_1status_FUNC);
	rc = (jint)webkit_download_get_status((WebKitDownload *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1download_1get_1status_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1download_1get_1suggested_1filename
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1download_1get_1suggested_1filename)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1download_1get_1suggested_1filename_FUNC);
	rc = (jintLong)webkit_download_get_suggested_filename((WebKitDownload *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1download_1get_1suggested_1filename_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1download_1get_1total_1size
JNIEXPORT jlong JNICALL WebKitGTK_NATIVE(_1webkit_1download_1get_1total_1size)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jlong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1download_1get_1total_1size_FUNC);
	rc = (jlong)webkit_download_get_total_size((WebKitDownload *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1download_1get_1total_1size_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1download_1get_1uri
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1download_1get_1uri)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1download_1get_1uri_FUNC);
	rc = (jintLong)webkit_download_get_uri((WebKitDownload *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1download_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1download_1set_1destination_1uri
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1download_1set_1destination_1uri)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1download_1set_1destination_1uri_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	webkit_download_set_destination_uri((WebKitDownload *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1download_1set_1destination_1uri_FUNC);
}
#endif

#ifndef NO__1webkit_1get_1default_1session
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1get_1default_1session)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1get_1default_1session_FUNC);
	rc = (jintLong)webkit_get_default_session();
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1get_1default_1session_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1major_1version
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1major_1version)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1major_1version_FUNC);
	rc = (jint)webkit_major_version();
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1major_1version_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1micro_1version
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1micro_1version)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1micro_1version_FUNC);
	rc = (jint)webkit_micro_version();
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1micro_1version_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1minor_1version
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1minor_1version)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1minor_1version_FUNC);
	rc = (jint)webkit_minor_version();
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1minor_1version_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1network_1request_1get_1message
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1network_1request_1get_1message)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1network_1request_1get_1message_FUNC);
	rc = (jintLong)webkit_network_request_get_message((WebKitNetworkRequest *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1network_1request_1get_1message_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1network_1request_1get_1uri
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1network_1request_1get_1uri)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1network_1request_1get_1uri_FUNC);
	rc = (jintLong)webkit_network_request_get_uri((WebKitNetworkRequest *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1network_1request_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1network_1request_1new
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1network_1request_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1network_1request_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)webkit_network_request_new((const gchar *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1network_1request_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1soup_1auth_1dialog_1get_1type
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1soup_1auth_1dialog_1get_1type)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1soup_1auth_1dialog_1get_1type_FUNC);
	rc = (jintLong)webkit_soup_auth_dialog_get_type();
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1soup_1auth_1dialog_1get_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1data_1source_1get_1data
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1data_1source_1get_1data)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1data_1source_1get_1data_FUNC);
	rc = (jintLong)webkit_web_data_source_get_data((WebKitWebDataSource *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1data_1source_1get_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1data_1source_1get_1encoding
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1data_1source_1get_1encoding)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1data_1source_1get_1encoding_FUNC);
	rc = (jintLong)webkit_web_data_source_get_encoding((WebKitWebDataSource *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1data_1source_1get_1encoding_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1frame_1get_1data_1source
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1frame_1get_1data_1source)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1frame_1get_1data_1source_FUNC);
	rc = (jintLong)webkit_web_frame_get_data_source((WebKitWebFrame *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1frame_1get_1data_1source_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1frame_1get_1global_1context
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1frame_1get_1global_1context)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1frame_1get_1global_1context_FUNC);
	rc = (jintLong)webkit_web_frame_get_global_context((WebKitWebFrame *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1frame_1get_1global_1context_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1frame_1get_1load_1status
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1web_1frame_1get_1load_1status)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1frame_1get_1load_1status_FUNC);
	rc = (jint)webkit_web_frame_get_load_status((WebKitWebFrame *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1frame_1get_1load_1status_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1frame_1get_1parent
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1frame_1get_1parent)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1frame_1get_1parent_FUNC);
	rc = (jintLong)webkit_web_frame_get_parent((WebKitWebFrame *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1frame_1get_1parent_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1frame_1get_1title
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1frame_1get_1title)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1frame_1get_1title_FUNC);
	rc = (jintLong)webkit_web_frame_get_title((WebKitWebFrame *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1frame_1get_1title_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1frame_1get_1uri
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1frame_1get_1uri)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1frame_1get_1uri_FUNC);
	rc = (jintLong)webkit_web_frame_get_uri((WebKitWebFrame *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1frame_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1frame_1get_1web_1view
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1frame_1get_1web_1view)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1frame_1get_1web_1view_FUNC);
	rc = (jintLong)webkit_web_frame_get_web_view((WebKitWebFrame *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1frame_1get_1web_1view_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1policy_1decision_1download
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1policy_1decision_1download)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1policy_1decision_1download_FUNC);
	webkit_web_policy_decision_download((WebKitWebPolicyDecision *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1policy_1decision_1download_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1policy_1decision_1ignore
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1policy_1decision_1ignore)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1policy_1decision_1ignore_FUNC);
	webkit_web_policy_decision_ignore((WebKitWebPolicyDecision *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1policy_1decision_1ignore_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1view_1can_1go_1back
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1can_1go_1back)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1can_1go_1back_FUNC);
	rc = (jint)webkit_web_view_can_go_back((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1can_1go_1back_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1can_1go_1forward
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1can_1go_1forward)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1can_1go_1forward_FUNC);
	rc = (jint)webkit_web_view_can_go_forward((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1can_1go_1forward_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1can_1show_1mime_1type
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1can_1show_1mime_1type)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1can_1show_1mime_1type_FUNC);
	rc = (jint)webkit_web_view_can_show_mime_type((WebKitWebView *)arg0, (const gchar *)arg1);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1can_1show_1mime_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1execute_1script
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1execute_1script)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1execute_1script_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	webkit_web_view_execute_script((WebKitWebView *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1execute_1script_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1view_1get_1load_1status
JNIEXPORT jint JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1get_1load_1status)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1get_1load_1status_FUNC);
	rc = (jint)webkit_web_view_get_load_status((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1get_1load_1status_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1get_1main_1frame
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1get_1main_1frame)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1get_1main_1frame_FUNC);
	rc = (jintLong)webkit_web_view_get_main_frame((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1get_1main_1frame_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1get_1progress
JNIEXPORT jdouble JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1get_1progress)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jdouble rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1get_1progress_FUNC);
	rc = (jdouble)webkit_web_view_get_progress((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1get_1progress_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1get_1settings
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1get_1settings)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1get_1settings_FUNC);
	rc = (jintLong)webkit_web_view_get_settings((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1get_1settings_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1get_1title
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1get_1title)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1get_1title_FUNC);
	rc = (jintLong)webkit_web_view_get_title((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1get_1title_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1get_1uri
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1get_1uri)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1get_1uri_FUNC);
	rc = (jintLong)webkit_web_view_get_uri((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1get_1uri_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1get_1window_1features
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1get_1window_1features)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1get_1window_1features_FUNC);
	rc = (jintLong)webkit_web_view_get_window_features((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1get_1window_1features_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1go_1back
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1go_1back)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1go_1back_FUNC);
	webkit_web_view_go_back((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1go_1back_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1view_1go_1forward
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1go_1forward)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1go_1forward_FUNC);
	webkit_web_view_go_forward((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1go_1forward_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1view_1load_1string
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1load_1string)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jbyteArray arg2, jbyteArray arg3, jbyteArray arg4)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1load_1string_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	webkit_web_view_load_string((WebKitWebView *)arg0, (const gchar *)lparg1, (const gchar *)lparg2, (const gchar *)lparg3, (const gchar *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1load_1string_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1view_1load_1uri
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1load_1uri)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1load_1uri_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	webkit_web_view_load_uri((WebKitWebView *)arg0, (const gchar *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1load_1uri_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1view_1new
JNIEXPORT jintLong JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1new_FUNC);
	rc = (jintLong)webkit_web_view_new();
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1webkit_1web_1view_1reload
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1reload)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1reload_FUNC);
	webkit_web_view_reload((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1reload_FUNC);
}
#endif

#ifndef NO__1webkit_1web_1view_1stop_1loading
JNIEXPORT void JNICALL WebKitGTK_NATIVE(_1webkit_1web_1view_1stop_1loading)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1webkit_1web_1view_1stop_1loading_FUNC);
	webkit_web_view_stop_loading((WebKitWebView *)arg0);
	WebKitGTK_NATIVE_EXIT(env, that, _1webkit_1web_1view_1stop_1loading_FUNC);
}
#endif

#ifndef NO_memmove
JNIEXPORT void JNICALL WebKitGTK_NATIVE(memmove)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
{
	JSClassDefinition _arg1, *lparg1=NULL;
	WebKitGTK_NATIVE_ENTER(env, that, memmove_FUNC);
	if (arg1) if ((lparg1 = getJSClassDefinitionFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	WebKitGTK_NATIVE_EXIT(env, that, memmove_FUNC);
}
#endif


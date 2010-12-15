/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others. All rights reserved.
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
#include "webkit_win32_structs.h"
#include "webkit_win32_stats.h"

#define WebKit_win32_NATIVE(func) Java_org_eclipse_swt_internal_webkit_WebKit_1win32_##func

#ifndef NO_CFArrayGetCount
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFArrayGetCount)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFArrayGetCount)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFArrayGetCount_FUNC);
	rc = (jint)CFArrayGetCount((CFArrayRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFArrayGetCount_FUNC);
	return rc;
}
#endif

#ifndef NO_CFArrayGetValueAtIndex
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFArrayGetValueAtIndex)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFArrayGetValueAtIndex)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFArrayGetValueAtIndex_FUNC);
	rc = (jintLong)CFArrayGetValueAtIndex((CFArrayRef)arg0, (CFIndex)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, CFArrayGetValueAtIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDataCreate
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFDataCreate)(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFDataCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFDataCreate_FUNC);
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)CFDataCreate((CFAllocatorRef)arg0, (const UInt8 *)lparg1, (CFIndex)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	WebKit_win32_NATIVE_EXIT(env, that, CFDataCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDataGetBytePtr
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFDataGetBytePtr)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFDataGetBytePtr)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFDataGetBytePtr_FUNC);
	rc = (jintLong)CFDataGetBytePtr((CFDataRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFDataGetBytePtr_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDataGetLength
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFDataGetLength)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFDataGetLength)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFDataGetLength_FUNC);
	rc = (jint)CFDataGetLength((CFDataRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFDataGetLength_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDictionaryCreate
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFDictionaryCreate)(JNIEnv *env, jclass that, jint arg0, jintLongArray arg1, jintLongArray arg2, jint arg3, jintLong arg4, jintLong arg5);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFDictionaryCreate)
	(JNIEnv *env, jclass that, jint arg0, jintLongArray arg1, jintLongArray arg2, jint arg3, jintLong arg4, jintLong arg5)
{
	jintLong *lparg1=NULL;
	jintLong *lparg2=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFDictionaryCreate_FUNC);
	if (arg1) if ((lparg1 = env->GetIntLongArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetIntLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)CFDictionaryCreate((CFAllocatorRef)arg0, (const void **)lparg1, (const void **)lparg2, (CFIndex)arg3, (const CFDictionaryKeyCallBacks *)arg4, (const CFDictionaryValueCallBacks *)arg5);
fail:
	if (arg2 && lparg2) env->ReleaseIntLongArrayElements(arg2, lparg2, 0);
	if (arg1 && lparg1) env->ReleaseIntLongArrayElements(arg1, lparg1, 0);
	WebKit_win32_NATIVE_EXIT(env, that, CFDictionaryCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_CFDictionaryGetCount
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFDictionaryGetCount)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFDictionaryGetCount)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFDictionaryGetCount_FUNC);
	rc = (jint)CFDictionaryGetCount((CFDictionaryRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFDictionaryGetCount_FUNC);
	return rc;
}
#endif

#ifndef NO_CFHTTPCookieCreateWithResponseHeaderFields
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieCreateWithResponseHeaderFields)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieCreateWithResponseHeaderFields)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieCreateWithResponseHeaderFields_FUNC);
	rc = (jintLong)CFHTTPCookieCreateWithResponseHeaderFields((CFAllocatorRef)arg0, (CFDictionaryRef)arg1, (CFURLRef)arg2);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieCreateWithResponseHeaderFields_FUNC);
	return rc;
}
#endif

#ifndef NO_CFHTTPCookieGetFlags
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieGetFlags)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieGetFlags)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieGetFlags_FUNC);
	rc = (jintLong)CFHTTPCookieGetFlags((CFHTTPCookieRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieGetFlags_FUNC);
	return rc;
}
#endif

#ifndef NO_CFHTTPCookieGetName
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieGetName)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieGetName)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieGetName_FUNC);
	rc = (jintLong)CFHTTPCookieGetName((CFHTTPCookieRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieGetName_FUNC);
	return rc;
}
#endif

#ifndef NO_CFHTTPCookieGetValue
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieGetValue)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieGetValue)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieGetValue_FUNC);
	rc = (jintLong)CFHTTPCookieGetValue((CFHTTPCookieRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieGetValue_FUNC);
	return rc;
}
#endif

#ifndef NO_CFHTTPCookieStorageCopyCookies
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageCopyCookies)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageCopyCookies)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieStorageCopyCookies_FUNC);
	rc = (jintLong)CFHTTPCookieStorageCopyCookies((CFHTTPCookieStorageRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieStorageCopyCookies_FUNC);
	return rc;
}
#endif

#ifndef NO_CFHTTPCookieStorageCopyCookiesForURL
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageCopyCookiesForURL)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageCopyCookiesForURL)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieStorageCopyCookiesForURL_FUNC);
	rc = (jintLong)CFHTTPCookieStorageCopyCookiesForURL((CFHTTPCookieStorageRef)arg0, (CFURLRef)arg1, (Boolean)arg2);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieStorageCopyCookiesForURL_FUNC);
	return rc;
}
#endif

#ifndef NO_CFHTTPCookieStorageDeleteCookie
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageDeleteCookie)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageDeleteCookie)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieStorageDeleteCookie_FUNC);
	CFHTTPCookieStorageDeleteCookie((CFHTTPCookieStorageRef)arg0, (CFHTTPCookieRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieStorageDeleteCookie_FUNC);
}
#endif

#ifndef NO_CFHTTPCookieStorageSetCookie
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageSetCookie)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFHTTPCookieStorageSetCookie)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKit_win32_NATIVE_ENTER(env, that, CFHTTPCookieStorageSetCookie_FUNC);
	CFHTTPCookieStorageSetCookie((CFHTTPCookieStorageRef)arg0, (CFHTTPCookieRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, CFHTTPCookieStorageSetCookie_FUNC);
}
#endif

#ifndef NO_CFRelease
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFRelease)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFRelease)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKit_win32_NATIVE_ENTER(env, that, CFRelease_FUNC);
	CFRelease((CFTypeRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFRelease_FUNC);
}
#endif

#ifndef NO_CFRetain
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFRetain)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFRetain)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFRetain_FUNC);
	rc = (jint)CFRetain((CFTypeRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFRetain_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringCreateWithCharacters
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFStringCreateWithCharacters)(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFStringCreateWithCharacters)
	(JNIEnv *env, jclass that, jintLong arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFStringCreateWithCharacters_FUNC);
	if (arg1) if ((lparg1 = env->GetCharArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)CFStringCreateWithCharacters((CFAllocatorRef)arg0, (const UniChar *)lparg1, (CFIndex)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	WebKit_win32_NATIVE_EXIT(env, that, CFStringCreateWithCharacters_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringGetCharacterAtIndex
extern "C" JNIEXPORT jchar JNICALL WebKit_win32_NATIVE(CFStringGetCharacterAtIndex)(JNIEnv *env, jclass that, jintLong arg0, jint arg1);
JNIEXPORT jchar JNICALL WebKit_win32_NATIVE(CFStringGetCharacterAtIndex)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jchar rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFStringGetCharacterAtIndex_FUNC);
	rc = (jchar)CFStringGetCharacterAtIndex((CFStringRef)arg0, (CFIndex)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, CFStringGetCharacterAtIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringGetCharactersPtr
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFStringGetCharactersPtr)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFStringGetCharactersPtr)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFStringGetCharactersPtr_FUNC);
	rc = (jintLong)CFStringGetCharactersPtr((CFStringRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFStringGetCharactersPtr_FUNC);
	return rc;
}
#endif

#ifndef NO_CFStringGetLength
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFStringGetLength)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(CFStringGetLength)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFStringGetLength_FUNC);
	rc = (jint)CFStringGetLength((CFStringRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFStringGetLength_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLCreateWithString
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFURLCreateWithString)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFURLCreateWithString)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFURLCreateWithString_FUNC);
	rc = (jintLong)CFURLCreateWithString((CFAllocatorRef)arg0, (CFStringRef)arg1, (CFURLRef)arg2);
	WebKit_win32_NATIVE_EXIT(env, that, CFURLCreateWithString_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLRequestCopyHTTPRequestBody
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFURLRequestCopyHTTPRequestBody)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(CFURLRequestCopyHTTPRequestBody)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, CFURLRequestCopyHTTPRequestBody_FUNC);
	rc = (jintLong)CFURLRequestCopyHTTPRequestBody((CFURLRequestRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, CFURLRequestCopyHTTPRequestBody_FUNC);
	return rc;
}
#endif

#ifndef NO_CFURLRequestSetHTTPRequestBody
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFURLRequestSetHTTPRequestBody)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(CFURLRequestSetHTTPRequestBody)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKit_win32_NATIVE_ENTER(env, that, CFURLRequestSetHTTPRequestBody_FUNC);
	CFURLRequestSetHTTPRequestBody((CFMutableURLRequestRef)arg0, (CFDataRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, CFURLRequestSetHTTPRequestBody_FUNC);
}
#endif

#ifndef NO_JSClassCreate
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSClassCreate)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSClassCreate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSClassCreate_FUNC);
	rc = (jintLong)JSClassCreate((const JSClassDefinition*)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSClassCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_JSClassDefinition_1sizeof
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSClassDefinition_1sizeof)(JNIEnv *env, jclass that);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSClassDefinition_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSClassDefinition_1sizeof_FUNC);
	rc = (jint)JSClassDefinition_sizeof();
	WebKit_win32_NATIVE_EXIT(env, that, JSClassDefinition_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_JSClassRetain
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSClassRetain)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSClassRetain)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSClassRetain_FUNC);
	rc = (jintLong)JSClassRetain((JSClassRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSClassRetain_FUNC);
	return rc;
}
#endif

#ifndef NO_JSContextGetGlobalObject
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSContextGetGlobalObject)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSContextGetGlobalObject)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSContextGetGlobalObject_FUNC);
	rc = (jintLong)JSContextGetGlobalObject((JSContextRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSContextGetGlobalObject_FUNC);
	return rc;
}
#endif

#ifndef NO_JSEvaluateScript
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSEvaluateScript)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jint arg4, jintLongArray arg5);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSEvaluateScript)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jint arg4, jintLongArray arg5)
{
	jintLong *lparg5=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSEvaluateScript_FUNC);
	if (arg5) if ((lparg5 = env->GetIntLongArrayElements(arg5, NULL)) == NULL) goto fail;
	rc = (jintLong)JSEvaluateScript((JSContextRef)arg0, (JSStringRef)arg1, (JSObjectRef)arg2, (JSStringRef)arg3, arg4, (JSValueRef *)lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntLongArrayElements(arg5, lparg5, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSEvaluateScript_FUNC);
	return rc;
}
#endif

#ifndef NO_JSGlobalContextCreate
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSGlobalContextCreate)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSGlobalContextCreate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSGlobalContextCreate_FUNC);
	rc = (jint)JSGlobalContextCreate((JSClassRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSGlobalContextCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_JSGlobalContextRetain
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSGlobalContextRetain)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSGlobalContextRetain)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSGlobalContextRetain_FUNC);
	rc = (jint)JSGlobalContextRetain((JSGlobalContextRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSGlobalContextRetain_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectGetPrivate
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectGetPrivate)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectGetPrivate)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectGetPrivate_FUNC);
	rc = (jintLong)JSObjectGetPrivate((JSObjectRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectGetPrivate_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectGetProperty
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectGetProperty)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLongArray arg3);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectGetProperty)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLongArray arg3)
{
	jintLong *lparg3=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectGetProperty_FUNC);
	if (arg3) if ((lparg3 = env->GetIntLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)JSObjectGetProperty((JSContextRef)arg0, (JSObjectRef)arg1, (JSStringRef)arg2, (JSValueRef*)lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntLongArrayElements(arg3, lparg3, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectGetProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectGetPropertyAtIndex
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectGetPropertyAtIndex)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLongArray arg3);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectGetPropertyAtIndex)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLongArray arg3)
{
	jintLong *lparg3=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectGetPropertyAtIndex_FUNC);
	if (arg3) if ((lparg3 = env->GetIntLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)JSObjectGetPropertyAtIndex((JSContextRef)arg0, (JSObjectRef)arg1, (unsigned)arg2, (JSValueRef*)lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntLongArrayElements(arg3, lparg3, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectGetPropertyAtIndex_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectIsFunction
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSObjectIsFunction)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSObjectIsFunction)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectIsFunction_FUNC);
	rc = (jint)JSObjectIsFunction((JSContextRef)arg0, (JSObjectRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectIsFunction_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectMake
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSObjectMake)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSObjectMake)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectMake_FUNC);
	rc = (jint)JSObjectMake((JSContextRef)arg0, (JSClassRef)arg1, (void *)arg2);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectMake_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectMakeArray
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectMakeArray)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSObjectMakeArray)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2, jintLongArray arg3)
{
	jintLong *lparg2=NULL;
	jintLong *lparg3=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectMakeArray_FUNC);
	if (arg2) if ((lparg2 = env->GetIntLongArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntLongArrayElements(arg3, NULL)) == NULL) goto fail;
	rc = (jintLong)JSObjectMakeArray((JSContextRef)arg0, (size_t)arg1, (const struct OpaqueJSValue * const*)lparg2, (JSValueRef*)lparg3);
fail:
	if (arg3 && lparg3) env->ReleaseIntLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseIntLongArrayElements(arg2, lparg2, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectMakeArray_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectMakeFunctionWithCallback
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSObjectMakeFunctionWithCallback)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSObjectMakeFunctionWithCallback)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectMakeFunctionWithCallback_FUNC);
	rc = (jint)JSObjectMakeFunctionWithCallback((JSContextRef)arg0, (JSStringRef)arg1, (JSObjectCallAsFunctionCallback)arg2);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectMakeFunctionWithCallback_FUNC);
	return rc;
}
#endif

#ifndef NO_JSObjectSetProperty
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(JSObjectSetProperty)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLongArray arg5);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(JSObjectSetProperty)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLongArray arg5)
{
	jintLong *lparg5=NULL;
	WebKit_win32_NATIVE_ENTER(env, that, JSObjectSetProperty_FUNC);
	if (arg5) if ((lparg5 = env->GetIntLongArrayElements(arg5, NULL)) == NULL) goto fail;
	JSObjectSetProperty((JSContextRef)arg0, (JSObjectRef)arg1, (JSStringRef)arg2, (JSValueRef)arg3, (JSPropertyAttributes)arg4, (JSValueRef *)lparg5);
fail:
	if (arg5 && lparg5) env->ReleaseIntLongArrayElements(arg5, lparg5, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSObjectSetProperty_FUNC);
}
#endif

#ifndef NO_JSStringCreateWithUTF8CString
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSStringCreateWithUTF8CString)(JNIEnv *env, jclass that, jbyteArray arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSStringCreateWithUTF8CString)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSStringCreateWithUTF8CString_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)JSStringCreateWithUTF8CString((const char*)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSStringCreateWithUTF8CString_FUNC);
	return rc;
}
#endif

#ifndef NO_JSStringGetLength
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSStringGetLength)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSStringGetLength)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSStringGetLength_FUNC);
	rc = (jint)JSStringGetLength((JSStringRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSStringGetLength_FUNC);
	return rc;
}
#endif

#ifndef NO_JSStringGetMaximumUTF8CStringSize
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSStringGetMaximumUTF8CStringSize)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSStringGetMaximumUTF8CStringSize)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSStringGetMaximumUTF8CStringSize_FUNC);
	rc = (jintLong)JSStringGetMaximumUTF8CStringSize((JSStringRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSStringGetMaximumUTF8CStringSize_FUNC);
	return rc;
}
#endif

#ifndef NO_JSStringGetUTF8CString
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSStringGetUTF8CString)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSStringGetUTF8CString)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jintLong arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSStringGetUTF8CString_FUNC);
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)JSStringGetUTF8CString((JSStringRef)arg0, (char *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSStringGetUTF8CString_FUNC);
	return rc;
}
#endif

#ifndef NO_JSStringIsEqualToUTF8CString
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSStringIsEqualToUTF8CString)(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSStringIsEqualToUTF8CString)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSStringIsEqualToUTF8CString_FUNC);
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)JSStringIsEqualToUTF8CString((JSStringRef)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSStringIsEqualToUTF8CString_FUNC);
	return rc;
}
#endif

#ifndef NO_JSStringRelease
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(JSStringRelease)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(JSStringRelease)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	WebKit_win32_NATIVE_ENTER(env, that, JSStringRelease_FUNC);
	JSStringRelease((JSStringRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSStringRelease_FUNC);
}
#endif

#ifndef NO_JSValueGetType
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSValueGetType)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSValueGetType)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueGetType_FUNC);
	rc = (jint)JSValueGetType((JSContextRef)arg0, (JSValueRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueGetType_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueIsObjectOfClass
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSValueIsObjectOfClass)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSValueIsObjectOfClass)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueIsObjectOfClass_FUNC);
	rc = (jint)JSValueIsObjectOfClass((JSContextRef)arg0, (JSValueRef)arg1, (JSClassRef)arg2);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueIsObjectOfClass_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueMakeBoolean
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeBoolean)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeBoolean)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueMakeBoolean_FUNC);
	rc = (jintLong)JSValueMakeBoolean((JSContextRef)arg0, (bool)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueMakeBoolean_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueMakeNull
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeNull)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeNull)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueMakeNull_FUNC);
	rc = (jintLong)JSValueMakeNull((JSContextRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueMakeNull_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueMakeNumber
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeNumber)(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeNumber)
	(JNIEnv *env, jclass that, jintLong arg0, jdouble arg1)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueMakeNumber_FUNC);
	rc = (jintLong)JSValueMakeNumber((JSContextRef)arg0, (double)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueMakeNumber_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueMakeString
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeString)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeString)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueMakeString_FUNC);
	rc = (jintLong)JSValueMakeString((JSContextRef)arg0, (JSStringRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueMakeString_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueMakeUndefined
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeUndefined)(JNIEnv *env, jclass that, jintLong arg0);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueMakeUndefined)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueMakeUndefined_FUNC);
	rc = (jintLong)JSValueMakeUndefined((JSContextRef)arg0);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueMakeUndefined_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueProtect
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(JSValueProtect)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(JSValueProtect)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	WebKit_win32_NATIVE_ENTER(env, that, JSValueProtect_FUNC);
	JSValueProtect((JSContextRef)arg0, (JSValueRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueProtect_FUNC);
}
#endif

#ifndef NO_JSValueToBoolean
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSValueToBoolean)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(JSValueToBoolean)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueToBoolean_FUNC);
	rc = (jint)JSValueToBoolean((JSContextRef)arg0, (JSValueRef)arg1);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueToBoolean_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueToNumber
extern "C" JNIEXPORT jdouble JNICALL WebKit_win32_NATIVE(JSValueToNumber)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2);
JNIEXPORT jdouble JNICALL WebKit_win32_NATIVE(JSValueToNumber)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jdouble rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueToNumber_FUNC);
	if (arg2) if ((lparg2 = env->GetIntLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jdouble)JSValueToNumber((JSContextRef)arg0, (JSValueRef)arg1, (JSValueRef*)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntLongArrayElements(arg2, lparg2, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueToNumber_FUNC);
	return rc;
}
#endif

#ifndef NO_JSValueToStringCopy
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueToStringCopy)(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(JSValueToStringCopy)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLongArray arg2)
{
	jintLong *lparg2=NULL;
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, JSValueToStringCopy_FUNC);
	if (arg2) if ((lparg2 = env->GetIntLongArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)JSValueToStringCopy((JSContextRef)arg0, (JSValueRef)arg1, (JSValueRef*)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseIntLongArrayElements(arg2, lparg2, 0);
	WebKit_win32_NATIVE_EXIT(env, that, JSValueToStringCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_WebKitCreateInstance
extern "C" JNIEXPORT jint JNICALL WebKit_win32_NATIVE(WebKitCreateInstance)(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jbyteArray arg2, jintLongArray arg3);
JNIEXPORT jint JNICALL WebKit_win32_NATIVE(WebKitCreateInstance)
	(JNIEnv *env, jclass that, jbyteArray arg0, jintLong arg1, jbyteArray arg2, jintLongArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, WebKitCreateInstance_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntLongArrayElements(arg3, NULL)) == NULL) goto fail;
/*
	rc = (jint)WebKitCreateInstance(lparg0, arg1, lparg2, lparg3);
*/
	{
		LOAD_FUNCTION(fp, WebKitCreateInstance)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jbyte *, jintLong, jbyte *, jintLong *))fp)(lparg0, arg1, lparg2, lparg3);
		}
	}
fail:
	if (arg3 && lparg3) env->ReleaseIntLongArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	WebKit_win32_NATIVE_EXIT(env, that, WebKitCreateInstance_FUNC);
	return rc;
}
#endif

#ifndef NO_kCFCopyStringDictionaryKeyCallBacks
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(kCFCopyStringDictionaryKeyCallBacks)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(kCFCopyStringDictionaryKeyCallBacks)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, kCFCopyStringDictionaryKeyCallBacks_FUNC);
	rc = (jintLong)&kCFCopyStringDictionaryKeyCallBacks;
	WebKit_win32_NATIVE_EXIT(env, that, kCFCopyStringDictionaryKeyCallBacks_FUNC);
	return rc;
}
#endif

#ifndef NO_kCFTypeDictionaryValueCallBacks
extern "C" JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(kCFTypeDictionaryValueCallBacks)(JNIEnv *env, jclass that);
JNIEXPORT jintLong JNICALL WebKit_win32_NATIVE(kCFTypeDictionaryValueCallBacks)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	WebKit_win32_NATIVE_ENTER(env, that, kCFTypeDictionaryValueCallBacks_FUNC);
	rc = (jintLong)&kCFTypeDictionaryValueCallBacks;
	WebKit_win32_NATIVE_EXIT(env, that, kCFTypeDictionaryValueCallBacks_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove
extern "C" JNIEXPORT void JNICALL WebKit_win32_NATIVE(memmove)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2);
JNIEXPORT void JNICALL WebKit_win32_NATIVE(memmove)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2)
{
	JSClassDefinition _arg1, *lparg1=NULL;
	WebKit_win32_NATIVE_ENTER(env, that, memmove_FUNC);
	if (arg1) if ((lparg1 = getJSClassDefinitionFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	WebKit_win32_NATIVE_EXIT(env, that, memmove_FUNC);
}
#endif


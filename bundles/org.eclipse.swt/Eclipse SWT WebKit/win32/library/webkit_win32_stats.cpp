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
#include "webkit_win32_stats.h"

#ifdef NATIVE_STATS

int WebKit_win32_nativeFunctionCount = 64;
int WebKit_win32_nativeFunctionCallCount[64];
char * WebKit_win32_nativeFunctionNames[] = {
	"CFArrayGetCount",
	"CFArrayGetValueAtIndex",
	"CFDataCreate",
	"CFDataGetBytePtr",
	"CFDataGetLength",
	"CFDictionaryCreate",
	"CFDictionaryGetCount",
	"CFHTTPCookieCreateWithResponseHeaderFields",
	"CFHTTPCookieGetFlags",
	"CFHTTPCookieGetName",
	"CFHTTPCookieGetValue",
	"CFHTTPCookieStorageCopyCookies",
	"CFHTTPCookieStorageCopyCookiesForURL",
	"CFHTTPCookieStorageDeleteCookie",
	"CFHTTPCookieStorageSetCookie",
	"CFRelease",
	"CFRetain",
	"CFStringCreateWithCharacters",
	"CFStringGetCharacterAtIndex",
	"CFStringGetCharactersPtr",
	"CFStringGetLength",
	"CFURLCreateWithString",
	"CFURLRequestCopyHTTPRequestBody",
	"CFURLRequestSetHTTPRequestBody",
	"JSClassCreate",
	"JSClassDefinition_1sizeof",
	"JSClassRetain",
	"JSContextGetGlobalObject",
	"JSEvaluateScript",
	"JSGlobalContextCreate",
	"JSGlobalContextRetain",
	"JSObjectCallAsFunctionProc_1CALLBACK",
	"JSObjectGetPrivate",
	"JSObjectGetProperty",
	"JSObjectGetPropertyAtIndex",
	"JSObjectGetPropertyProc_1CALLBACK",
	"JSObjectHasPropertyProc_1CALLBACK",
	"JSObjectIsFunction",
	"JSObjectMake",
	"JSObjectMakeArray",
	"JSObjectMakeFunctionWithCallback",
	"JSObjectSetProperty",
	"JSStringCreateWithUTF8CString",
	"JSStringGetLength",
	"JSStringGetMaximumUTF8CStringSize",
	"JSStringGetUTF8CString",
	"JSStringIsEqualToUTF8CString",
	"JSStringRelease",
	"JSValueGetType",
	"JSValueIsObjectOfClass",
	"JSValueMakeBoolean",
	"JSValueMakeNull",
	"JSValueMakeNumber",
	"JSValueMakeString",
	"JSValueMakeUndefined",
	"JSValueProtect",
	"JSValueToBoolean",
	"JSValueToNumber",
	"JSValueToStringCopy",
	"WebKitCreateInstance",
	"kCFCopyStringDictionaryKeyCallBacks",
	"kCFTypeDictionaryValueCallBacks",
	"memmove",
	"willPerformClientRedirectToURL_1CALLBACK",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(WebKit_1win32_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return WebKit_win32_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(WebKit_1win32_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return env->NewStringUTF(WebKit_win32_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(WebKit_1win32_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return WebKit_win32_nativeFunctionCallCount[index];
}

#endif

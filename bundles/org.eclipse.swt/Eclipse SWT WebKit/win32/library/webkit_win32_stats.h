/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others. All rights reserved.
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

#ifdef NATIVE_STATS
extern int WebKit_win32_nativeFunctionCount;
extern int WebKit_win32_nativeFunctionCallCount[];
extern char* WebKit_win32_nativeFunctionNames[];
#define WebKit_win32_NATIVE_ENTER(env, that, func) WebKit_win32_nativeFunctionCallCount[func]++;
#define WebKit_win32_NATIVE_EXIT(env, that, func) 
#else
#ifndef WebKit_win32_NATIVE_ENTER
#define WebKit_win32_NATIVE_ENTER(env, that, func) 
#endif
#ifndef WebKit_win32_NATIVE_EXIT
#define WebKit_win32_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	CFArrayGetCount_FUNC,
	CFArrayGetValueAtIndex_FUNC,
	CFDataCreate_FUNC,
	CFDataGetBytePtr_FUNC,
	CFDataGetLength_FUNC,
	CFDictionaryCreate_FUNC,
	CFHTTPCookieCreateWithResponseHeaderFields_FUNC,
	CFHTTPCookieGetFlags_FUNC,
	CFHTTPCookieGetName_FUNC,
	CFHTTPCookieGetValue_FUNC,
	CFHTTPCookieStorageCopyCookies_FUNC,
	CFHTTPCookieStorageCopyCookiesForURL_FUNC,
	CFHTTPCookieStorageDeleteCookie_FUNC,
	CFHTTPCookieStorageSetCookie_FUNC,
	CFRelease_FUNC,
	CFStringCreateWithCharacters_FUNC,
	CFStringGetCharacterAtIndex_FUNC,
	CFStringGetCharactersPtr_FUNC,
	CFStringGetLength_FUNC,
	CFURLCreateWithString_FUNC,
	CFURLRequestCopyHTTPRequestBody_FUNC,
	CFURLRequestCreateMutableCopy_FUNC,
	CFURLRequestSetHTTPRequestBody_FUNC,
	CFURLRequestSetURL_FUNC,
	JSClassCreate_FUNC,
	JSClassDefinition_1sizeof_FUNC,
	JSClassRetain_FUNC,
	JSContextGetGlobalObject_FUNC,
	JSEvaluateScript_FUNC,
	JSGlobalContextRetain_FUNC,
	JSObjectCallAsFunctionProc_1CALLBACK_FUNC,
	JSObjectGetPrivate_FUNC,
	JSObjectGetProperty_FUNC,
	JSObjectGetPropertyAtIndex_FUNC,
	JSObjectGetPropertyProc_1CALLBACK_FUNC,
	JSObjectHasPropertyProc_1CALLBACK_FUNC,
	JSObjectMake_FUNC,
	JSObjectMakeArray_FUNC,
	JSObjectMakeFunctionWithCallback_FUNC,
	JSObjectSetProperty_FUNC,
	JSStringCreateWithUTF8CString_FUNC,
	JSStringGetLength_FUNC,
	JSStringGetMaximumUTF8CStringSize_FUNC,
	JSStringGetUTF8CString_FUNC,
	JSStringIsEqualToUTF8CString_FUNC,
	JSStringRelease_FUNC,
	JSValueGetType_FUNC,
	JSValueIsObjectOfClass_FUNC,
	JSValueMakeBoolean_FUNC,
	JSValueMakeNull_FUNC,
	JSValueMakeNumber_FUNC,
	JSValueMakeString_FUNC,
	JSValueMakeUndefined_FUNC,
	JSValueToBoolean_FUNC,
	JSValueToNumber_FUNC,
	JSValueToStringCopy_FUNC,
	WebKitCreateInstance_FUNC,
	kCFCopyStringDictionaryKeyCallBacks_FUNC,
	kCFTypeDictionaryValueCallBacks_FUNC,
	memmove_FUNC,
	willPerformClientRedirectToURL_1CALLBACK_FUNC,
} WebKit_win32_FUNCS;

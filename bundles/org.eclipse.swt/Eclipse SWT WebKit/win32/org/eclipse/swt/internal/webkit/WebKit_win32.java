/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/** @jniclass flags=cpp */
public class WebKit_win32 extends C {
	//public static final GUID CLSID_DOMEventListener = IIDFromString("{AC3D1BC3-4976-4431-8A19-4812C5EFE39C}"); //$NON-NLS-1$
	public static final GUID CLSID_WebCookieManager = IIDFromString("{3F35F332-BB2B-49b3-AEDD-27B317687E07}"); //$NON-NLS-1$
	public static final GUID CLSID_WebMutableURLRequest = IIDFromString("{a062ecc3-bb1b-4694-a569-f59e0ad6be0c}"); //$NON-NLS-1$
	public static final GUID CLSID_WebPreferences = IIDFromString("{67B89F90-F778-438B-ABBF-34D1ACBF8651}"); //$NON-NLS-1$
	public static final GUID CLSID_WebURLCredential = IIDFromString("{7433F53B-7FE9-484a-9432-72909457A646}"); //$NON-NLS-1$
	public static final GUID CLSID_WebView = IIDFromString("{d6bca079-f61c-4e1e-b453-32a0477d02e3}"); //$NON-NLS-1$
	//public static final GUID IID_IDOMEventListener = IIDFromString("{7f8a0a96-f864-44fb-87fb-c5f03666e8e6}"); //$NON-NLS-1$
	public static final GUID IID_IWebCookieManager = IIDFromString("{7053FE94-3623-444f-A298-209A90879A8C}"); //$NON-NLS-1$
	public static final GUID IID_IWebDownloadDelegate = IIDFromString("{16A32AE6-C862-40cd-9225-2CAF823F40F9}"); //$NON-NLS-1$
	public static final GUID IID_IWebErrorPrivate = IIDFromString("{19FED49C-7016-48a6-B5C6-07ADE116531B}"); //$NON-NLS-1$
	public static final GUID IID_IWebFrameLoadDelegate = IIDFromString("{3354665B-84BA-4fdf-B35E-BF5CF9D96026}"); //$NON-NLS-1$
	public static final GUID IID_IWebFramePrivate = IIDFromString("{A1657D07-4881-4475-9D10-76548731D448}"); //$NON-NLS-1$
	public static final GUID IID_IWebIBActions = IIDFromString("{8F0E3A30-B924-44f8-990A-1AE61ED6C632}"); //$NON-NLS-1$
	public static final GUID IID_IWebMutableURLRequest = IIDFromString("{C4042773-371F-427e-AFA9-9D4B358A0D93}"); //$NON-NLS-1$
	public static final GUID IID_IWebMutableURLRequestPrivate = IIDFromString("{AD675B60-2CE9-478c-B2AA-CAD643FF18AC}"); //$NON-NLS-1$;
	public static final GUID IID_IWebPolicyDelegate = IIDFromString("{9B0BAE6C-A496-4000-9E22-2E89F0747401}"); //$NON-NLS-1$
	public static final GUID IID_IWebPreferences = IIDFromString("{0930D594-A5A3-46e1-858E-AB17A13CD28E}"); //$NON-NLS-1$
	public static final GUID IID_IWebResourceLoadDelegate = IIDFromString("{AF3289AA-90DB-4ca4-A112-A1E5F0517953}"); //$NON-NLS-1$
	public static final GUID IID_IWebUIDelegate = IIDFromString("{042B7EE3-A5A4-4a8f-8C33-775CD9E89C7C}"); //$NON-NLS-1$
	public static final GUID IID_IWebURLCredential = IIDFromString("{A1E9D765-FACE-4189-BBE3-AED7EBF65EBD}"); //$NON-NLS-1$
	public static final GUID IID_IWebView = IIDFromString("{174BBEFD-058E-49C7-91DF-6F110AA4AC28}"); //$NON-NLS-1$
	public static final GUID IID_IWebViewPrivate = IIDFromString("{44914369-DEB5-4fcf-A6A3-30C02E73154F}"); //$NON-NLS-1$

	public static final int CFHTTPCookieSessionOnlyFlag   = 1 << 1;
	public static final int FontSmoothingTypeWindows = 4;
	public static final int kCFStringEncodingUTF8 = 0x08000100;

	public static final int kJSTypeUndefined = 0;
	public static final int kJSTypeNull = 1;
	public static final int kJSTypeBoolean = 2;
	public static final int kJSTypeNumber = 3;
	public static final int kJSTypeString = 4;
	public static final int kJSTypeObject = 5;

	public static final int WebURLCredentialPersistenceForSession = 1;
	public static final int WebURLErrorBadURL = -1000;
	public static final int WebURLErrorServerCertificateNotYetValid = -1204;
	public static final int WebURLErrorSecureConnectionFailed = -1200;

static GUID IIDFromString (String lpsz) {
	int length = lpsz.length ();
	char[] buffer = new char[length + 1];
	lpsz.getChars (0, length, buffer, 0);
	GUID lpiid = new GUID ();
	if (COM.IIDFromString (buffer, lpiid) == COM.S_OK) return lpiid;
	return null;
}

/**
 * @param theArray cast=(CFArrayRef)
 */
public static final native int CFArrayGetCount (long theArray);
/**
 * @param theArray cast=(CFArrayRef)
 * @param idx cast=(CFIndex)
 */
public static final native long CFArrayGetValueAtIndex (long theArray, int idx);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param bytes cast=(const UInt8 *)
 * @param length cast=(CFIndex)
 */
public static final native long CFDataCreate (long allocator, byte [] bytes, int length);
/**
 * @param theData cast=(CFDataRef)
 */
public static final native long CFDataGetBytePtr (long theData);
/**
 * @param theData cast=(CFDataRef)
 */
public static final native int CFDataGetLength (long theData);
/**
 * @param allocator	cast=(CFAllocatorRef)
 * @param keys cast=(const void **)
 * @param values cast=(const void **)
 * @param numValues cast=(CFIndex)
 * @param keyCallBacks cast=(const CFDictionaryKeyCallBacks *)
 * @param valueCallBacks cast=(const CFDictionaryValueCallBacks *)
 */
public static final native long CFDictionaryCreate (long allocator, long[] keys, long[] values, int numValues, long keyCallBacks, long valueCallBacks);
/**
 * @param inAllocator cast=(CFAllocatorRef)
 * @param headerFields cast=(CFDictionaryRef)
 * @param inURL	cast=(CFURLRef)
 */
public static final native long CFHTTPCookieCreateWithResponseHeaderFields (long inAllocator, long headerFields, long inURL);
/**
 * @param inCookie cast=(CFHTTPCookieRef)
 */
public static final native int CFHTTPCookieGetFlags (long inCookie);
/**
 * @param inCookie cast=(CFHTTPCookieRef)
 */
public static final native long CFHTTPCookieGetName (long inCookie);
/**
 * @param inCookie cast=(CFHTTPCookieRef)
 */
public static final native long CFHTTPCookieGetValue (long inCookie);
/**
 * @param inCookieStorage cast=(CFHTTPCookieStorageRef)
 */
public static final native long CFHTTPCookieStorageCopyCookies (long inCookieStorage);
/**
 * @param inCookieStorage cast=(CFHTTPCookieStorageRef)
 * @param inURL cast=(CFURLRef)
 * @param sendSecureCookies cast=(Boolean)
 */
public static final native long CFHTTPCookieStorageCopyCookiesForURL (long inCookieStorage, long inURL, boolean sendSecureCookies);
/**
 * @param inCookieStorage cast=(CFHTTPCookieStorageRef)
 * @param inCookie cast=(CFHTTPCookieRef)
 */
public static final native void CFHTTPCookieStorageDeleteCookie (long inCookieStorage, long inCookie);
/**
 * @param inCookieStorage cast=(CFHTTPCookieStorageRef)
 * @param inCookie cast=(CFHTTPCookieRef)
 */
public static final native void CFHTTPCookieStorageSetCookie (long inCookieStorage, long inCookie);
/**
 * @param cf cast=(CFTypeRef)
 */
public static final native void CFRelease (long cf);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param chars cast=(const UniChar *)
 * @param numChars cast=(CFIndex)
 */
public static final native int CFStringCreateWithCharacters (long alloc, char[] chars, int numChars);
/**
 * @param theString cast=(CFStringRef)
 * @param idx cast=(CFIndex)
 */
public static final native char CFStringGetCharacterAtIndex (long theString, int idx);
/**
 * @param theString cast=(CFStringRef)
 */
public static final native long CFStringGetCharactersPtr (long theString);
/**
 * @param theString cast=(CFStringRef)
 */
public static final native int CFStringGetLength (long theString);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param URLString cast=(CFStringRef)
 * @param baseURL cast=(CFURLRef)
 */
public static final native long CFURLCreateWithString (long allocator, long URLString, long baseURL);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param origRequest cast=(CFURLRequestRef)
 */
public static final native long CFURLRequestCreateMutableCopy (long	alloc, long origRequest);
/**
 * @param request cast=(CFURLRequestRef)
 */
public static final native long CFURLRequestCopyHTTPRequestBody (long request);
/**
 * @param mutableHTTPRequest cast=(CFMutableURLRequestRef)
 * @param httpBody cast=(CFDataRef)
 */
public static final native void CFURLRequestSetHTTPRequestBody (long mutableHTTPRequest, long httpBody);
/**
 * @param mutableRequest cast=(CFMutableURLRequestRef)
 * @param url cast=(CFURLRef)
 */
public static final native void CFURLRequestSetURL (long mutableRequest, long url);

/**
 * @param definition cast=(const JSClassDefinition*)
 */
public static final native long JSClassCreate (long definition);
/**
 * @param jsClass cast=(JSClassRef)
 */
public static final native long JSClassRetain (long jsClass);
/**
 * @param ctx cast=(JSContextRef)
 */
public static final native long JSContextGetGlobalObject (long ctx);
/**
 * @param ctx cast=(JSContextRef)
 * @param script cast=(JSStringRef)
 * @param thisObject cast=(JSObjectRef)
 * @param sourceURL cast=(JSStringRef)
 * @param exception cast=(JSValueRef *)
 */
public static final native long JSEvaluateScript (long ctx, long script, long thisObject, long sourceURL, int startingLineNumber, long[] exception);
/**
 * @param ctx cast=(JSGlobalContextRef)
 */
public static final native int JSGlobalContextRetain (long ctx);
/**
 * @param object cast=(JSObjectRef)
 */
public static final native long JSObjectGetPrivate (long object);
/**
 * @param ctx cast=(JSContextRef)
 * @param object cast=(JSObjectRef)
 * @param propertyName cast=(JSStringRef)
 * @param exception cast=(JSValueRef*)
 */
public static final native long JSObjectGetProperty (long ctx, long object, long propertyName, long[] exception);
/**
 * @param ctx cast=(JSContextRef)
 * @param object cast=(JSObjectRef)
 * @param propertyIndex cast=(unsigned)
 * @param exception cast=(JSValueRef*)
 */
public static final native long JSObjectGetPropertyAtIndex (long ctx, long object, int propertyIndex, long[] exception);
/**
 * @param ctx cast=(JSContextRef)
 * @param jsClass cast=(JSClassRef)
 * @param data cast=(void *)
 */
public static final native int JSObjectMake (long ctx, long jsClass, long data);
/**
 * @param ctx cast=(JSContextRef)
 * @param argumentCount cast=(size_t)
 * @param arguments cast=(const struct OpaqueJSValue * const*)
 * @param exception cast=(JSValueRef*)
 */
public static final native long JSObjectMakeArray (long ctx, long argumentCount, long[] arguments, long[] exception);
/**
 * @param ctx cast=(JSContextRef)
 * @param name cast=(JSStringRef)
 * @param callAsFunction cast=(JSObjectCallAsFunctionCallback)
 */
public static final native int JSObjectMakeFunctionWithCallback (long ctx, long name, long callAsFunction);
/**
 * @param ctx cast=(JSContextRef)
 * @param object cast=(JSObjectRef)
 * @param propertyName cast=(JSStringRef)
 * @param value cast=(JSValueRef)
 * @param attributes cast=(JSPropertyAttributes)
 * @param exception cast=(JSValueRef *)
 */
public static final native void JSObjectSetProperty (long ctx, long object, long propertyName, long value, long attributes, long[] exception);
/**
 * @param string cast=(const char*)
 */
public static final native long JSStringCreateWithUTF8CString (byte[] string);
/**
 * @param string cast=(JSStringRef)
 */
public static final native int JSStringGetLength (long string);
/**
 * @param string cast=(JSStringRef)
 */
public static final native long JSStringGetMaximumUTF8CStringSize (long string);
/**
 * @param string cast=(JSStringRef)
 * @param buffer cast=(char *)
 * @param bufferSize cast=(size_t)
 */
public static final native int JSStringGetUTF8CString (long string, byte[] buffer, long bufferSize);
/**
 * @param a cast=(JSStringRef)
 * @param b	cast=(const char *)
 */
public static final native int JSStringIsEqualToUTF8CString (long a, byte[] b);
/**
 * @param string cast=(JSStringRef)
 */
public static final native void JSStringRelease (long string);
/**
 * @param ctx cast=(JSContextRef)
 * @param value cast=(JSValueRef)
 */
public static final native int JSValueGetType (long ctx, long value);
/**
 * @param ctx cast=(JSContextRef)
 * @param value	cast=(JSValueRef)
 * @param jsClass	cast=(JSClassRef)
 */
public static final native int JSValueIsObjectOfClass (long ctx, long value, long jsClass);
/**
 * @param ctx cast=(JSContextRef)
 * @param b cast=(bool)
 */
public static final native long JSValueMakeBoolean (long ctx, long b);
/**
 * @param ctx cast=(JSContextRef)
 */
public static final native long JSValueMakeNull (long ctx);
/**
 * @param ctx cast=(JSContextRef)
 * @param number cast=(double)
 */
public static final native long JSValueMakeNumber (long ctx, double number);
/**
 * @param ctx cast=(JSContextRef)
 * @param string cast=(JSStringRef)
 */
public static final native long JSValueMakeString (long ctx, long string);
/**
 * @param ctx cast=(JSContextRef)
 */
public static final native long JSValueMakeUndefined (long ctx);
/**
 * @param ctx cast=(JSContextRef)
 * @param value cast=(JSValueRef)
 * @param exception cast=(JSValueRef*)
 */
public static final native double JSValueToNumber (long ctx, long value, long[] exception);
/**
 * @param ctx cast=(JSContextRef)
 * @param value cast=(JSValueRef)
 * @param exception cast=(JSValueRef*)
 */
public static final native long JSValueToStringCopy (long ctx, long value, long[] exception);
/** @method flags=const address*/
public static final native long kCFCopyStringDictionaryKeyCallBacks ();
/** @method flags=const address*/
public static final native long kCFTypeDictionaryValueCallBacks ();

public static final native int JSClassDefinition_sizeof ();
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, JSClassDefinition src, long size);
/** @method flags=no_gen */
public static final native int WebKitCreateInstance (byte[] rclsid, long pUnkOuter, byte[] refiid, long[] ppvObject);
public static final int WebKitCreateInstance (GUID clsid, long pUnkOuter, GUID iid, long[] ppvObject) {
	byte[] rclsid = new byte[GUID.sizeof];
	OS.IIDFromString ((clsid.toString () + '\0').toCharArray (), rclsid);
	byte[] refiid = new byte[GUID.sizeof];
	OS.IIDFromString ((iid.toString () + '\0').toCharArray (), refiid);
	return WebKitCreateInstance (rclsid, pUnkOuter, refiid, ppvObject);
}

/** @method flags=no_gen */
public static final native long JSObjectCallAsFunctionProc_CALLBACK (long func);
/** @method flags=no_gen */
public static final native long JSObjectGetPropertyProc_CALLBACK (long func);
/** @method flags=no_gen */
public static final native long JSObjectHasPropertyProc_CALLBACK (long func);
/** @method flags=no_gen */
public static final native long willPerformClientRedirectToURL_CALLBACK (long func);

}

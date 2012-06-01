/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;


import java.io.*;
import java.net.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

class WebKit extends WebBrowser {
	IWebView webView;
	int /*long*/ webViewWindowHandle, webViewData;
	int refCount = 0;
	int lastKeyCode, lastCharCode;

	WebDownloadDelegate webDownloadDelegate;
	WebFrameLoadDelegate webFrameLoadDelegate;
	WebPolicyDelegate webPolicyDelegate;
	WebResourceLoadDelegate webResourceLoadDelegate;
	WebUIDelegate webUIDelegate;

	boolean ignoreDispose;
	boolean loadingText = false;
	boolean traverseNext = true;
	boolean traverseOut = false;
	boolean untrustedText;
	String lastNavigateURL;
	BrowserFunction eventFunction;

	static int prefsIdentifier;
	static int /*long*/ ExternalClass;
	static boolean LibraryLoaded = false;
	static String LibraryLoadError;
	static Callback JSObjectHasPropertyProc;
	static Callback JSObjectGetPropertyProc;
	static Callback JSObjectCallAsFunctionProc;
	static final int MAX_PROGRESS = 100;
	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String CHARSET_UTF8 = "UTF-8"; //$NON-NLS-1$
	static final String CLASSNAME_EXTERNAL = "External"; //$NON-NLS-1$
	static final String EMPTY_STRING = ""; //$NON-NLS-1$
	static final String FUNCTIONNAME_CALLJAVA = "callJava"; //$NON-NLS-1$
	static final String HEADER_SETCOOKIE = "Set-Cookie"; //$NON-NLS-1$
	static final String POST = "POST"; //$NON-NLS-1$
	static final String PROPERTY_LENGTH = "length"; //$NON-NLS-1$
	static final String PROTOCOL_HTTPS = "https://"; //$NON-NLS-1$
	static final String PROTOCOL_FILE = "file://"; //$NON-NLS-1$
	static final String PROTOCOL_HTTP = "http://"; //$NON-NLS-1$
	static final String USER_AGENT = "user-agent"; //$NON-NLS-1$
	static final String URI_FILEROOT = "file:///"; //$NON-NLS-1$

	/* event strings */
	static final String DOMEVENT_DRAGSTART = "dragstart"; //$NON-NLS-1$
	static final String DOMEVENT_KEYDOWN = "keydown"; //$NON-NLS-1$
	static final String DOMEVENT_KEYPRESS = "keypress"; //$NON-NLS-1$
	static final String DOMEVENT_KEYUP = "keyup"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEDOWN = "mousedown"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEUP = "mouseup"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEMOVE = "mousemove"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEOUT = "mouseout"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEOVER = "mouseover"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEWHEEL = "mousewheel"; //$NON-NLS-1$

static {
	/*
	* Attempt to load the swt-webkit library.  This will only succeed if the Apple
	* Application Support package is on the user's Windows Path environment variable. 
	*/
	try {
		Library.loadLibrary ("swt-webkit"); // $NON-NLS-1$
		LibraryLoaded = true;
	} catch (Throwable e) {
	}
	
	/*
	* If needed, add the Apple Application Support package's directory to the library
	* lookup path and try to load the swt-webkit library again.
	*/
	if (!LibraryLoaded) {
		/*
		 * Locate the Apple Application Support directory (if installed) and add its path to the library lookup path.
		 *
		 * As of Safari 5.1.4, the Apple Application Support directory is in the Safari installation directory,
		 * which is pointed to by registry entry "HKEY_LOCAL_MACHINE\SOFTWARE\Apple Computer, Inc.\Safari".
		 *
		 * With earlier versions of Safari the Apple Application Support is installed in a stand-alone location, which
		 * is pointed to by registry entry "HKEY_LOCAL_MACHINE\SOFTWARE\Apple Inc.\Apple Application Support\InstallDir".
		 */

		String AASDirectory = readInstallDir ("SOFTWARE\\Apple Computer, Inc.\\Safari"); //$NON-NLS-1$
		if (AASDirectory != null) {
			AASDirectory += "\\Apple Application Support"; //$NON-NLS-1$
			if (!new File(AASDirectory).exists()) {
				AASDirectory = null;
			}
		}

		if (AASDirectory == null) {
			AASDirectory = readInstallDir ("SOFTWARE\\Apple Inc.\\Apple Application Support"); //$NON-NLS-1$
		}

		if (AASDirectory != null) {
			TCHAR buffer = new TCHAR (0, AASDirectory, true);
			boolean success = OS.SetDllDirectory (buffer); /* should succeed on XP+SP1 and newer */
			if (success) {
				try {
					Library.loadLibrary ("swt-webkit"); //$NON-NLS-1$
					LibraryLoaded = true;
				} catch (Throwable e) {
					LibraryLoadError = "Failed to load the swt-webkit library"; //$NON-NLS-1$
					if (Device.DEBUG) System.out.println ("Failed to load swt-webkit library. Apple Application Support directory path: " + AASDirectory); //$NON-NLS-1$
				}
			} else {
				LibraryLoadError = "Failed to add the Apple Application Support package to the library lookup path.  "; //$NON-NLS-1$
				LibraryLoadError += "To use a SWT.WEBKIT-style Browser prepend " + AASDirectory + " to your Windows 'Path' environment variable and restart."; //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			LibraryLoadError = "Safari must be installed to use a SWT.WEBKIT-style Browser"; //$NON-NLS-1$
		}
	}

	if (LibraryLoaded) {
		JSObjectHasPropertyProc = new Callback (WebKit.class, "JSObjectHasPropertyProc", 3); //$NON-NLS-1$
		if (JSObjectHasPropertyProc.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		JSObjectGetPropertyProc = new Callback (WebKit.class, "JSObjectGetPropertyProc", 4); //$NON-NLS-1$
		if (JSObjectGetPropertyProc.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		JSObjectCallAsFunctionProc = new Callback (WebKit.class, "JSObjectCallAsFunctionProc", 6); //$NON-NLS-1$
		if (JSObjectCallAsFunctionProc.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		NativeClearSessions = new Runnable () {
			public void run () {
				int /*long*/[] result = new int /*long*/[1];
				int hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebCookieManager, 0, WebKit_win32.IID_IWebCookieManager, result);
				if (hr != COM.S_OK || result[0] == 0) {
					return;
				}
				IWebCookieManager cookieManager = new IWebCookieManager (result[0]);
				int /*long*/[] storage = new int /*long*/[1];
				hr = cookieManager.cookieStorage (storage);
				cookieManager.Release ();
				if (hr != COM.S_OK || storage[0] == 0) {
					return;
				}
				int /*long*/ cookies = WebKit_win32.CFHTTPCookieStorageCopyCookies (storage[0]);
				if (cookies != 0) {
					int count = WebKit_win32.CFArrayGetCount (cookies);
					for (int i = 0; i < count; i++) {
						int /*long*/ cookie = WebKit_win32.CFArrayGetValueAtIndex (cookies, i);
						int /*long*/ flags = WebKit_win32.CFHTTPCookieGetFlags (cookie);
						if ((flags & WebKit_win32.CFHTTPCookieSessionOnlyFlag) != 0) {
							WebKit_win32.CFHTTPCookieStorageDeleteCookie (storage[0], cookie);
						}
					}
					WebKit_win32.CFRelease (cookies);
				}
				// WebKit_win32.CFRelease (storage[0]);	//intentionally commented, causes crash
			}
		};

		NativeGetCookie = new Runnable () {
			public void run () {
				int /*long*/[] result = new int /*long*/[1];
				int hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebCookieManager, 0, WebKit_win32.IID_IWebCookieManager, result);
				if (hr != COM.S_OK || result[0] == 0) {
					return;
				}
				IWebCookieManager cookieManager = new IWebCookieManager (result[0]);
				int /*long*/[] storage = new int /*long*/[1];
				hr = cookieManager.cookieStorage (storage);
				cookieManager.Release ();
				if (hr != COM.S_OK || storage[0] == 0) {
					return;
				}
				char[] chars = CookieUrl.toCharArray ();
				int /*long*/ string = WebKit_win32.CFStringCreateWithCharacters (0, chars, chars.length);
				if (string != 0) {
					int /*long*/ cfUrl = WebKit_win32.CFURLCreateWithString (0, string, 0);
					if (cfUrl != 0) {
						boolean secure = CookieUrl.startsWith (PROTOCOL_HTTPS);
						int /*long*/ cookiesArray = WebKit_win32.CFHTTPCookieStorageCopyCookiesForURL (storage[0], cfUrl, secure);
						if (cookiesArray != 0) {
							int count = WebKit_win32.CFArrayGetCount (cookiesArray);
							for (int i = 0; i < count; i++) {
								int /*long*/ cookie = WebKit_win32.CFArrayGetValueAtIndex (cookiesArray, i);
								if (cookie != 0) {
									int /*long*/ cookieName = WebKit_win32.CFHTTPCookieGetName (cookie);
									if (cookieName != 0) {
										String name = stringFromCFString (cookieName);
										if (CookieName.equals (name)) {
											int /*long*/ value = WebKit_win32.CFHTTPCookieGetValue (cookie);
											if (value != 0) CookieValue = stringFromCFString (value);
											break;
										}
									}
								}
							}
							WebKit_win32.CFRelease (cookiesArray);
						}
						WebKit_win32.CFRelease (cfUrl);
					}
					WebKit_win32.CFRelease (string);
				}
				// WebKit_win32.CFRelease (storage[0]);	//intentionally commented, causes crash
			}
		};

		NativeSetCookie = new Runnable () {
			public void run () {
				int /*long*/[] result = new int /*long*/[1];
				int hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebCookieManager, 0, WebKit_win32.IID_IWebCookieManager, result);
				if (hr != COM.S_OK || result[0] == 0) {
					return;
				}
				IWebCookieManager cookieManager = new IWebCookieManager (result[0]);
				int /*long*/[] storage = new int /*long*/[1];
				hr = cookieManager.cookieStorage (storage);
				cookieManager.Release ();
				if (hr != COM.S_OK || storage[0] == 0) {
					return;
				}

				char[] chars = CookieUrl.toCharArray ();
				int /*long*/ string = WebKit_win32.CFStringCreateWithCharacters (0, chars, chars.length);
				if (string != 0) {
					int /*long*/ cfUrl = WebKit_win32.CFURLCreateWithString (0, string, 0);
					if (cfUrl != 0) {
						chars = CookieValue.toCharArray ();
						int /*long*/ value = WebKit_win32.CFStringCreateWithCharacters (0, chars, chars.length);
						if (value != 0) {
							chars = HEADER_SETCOOKIE.toCharArray ();
							int /*long*/ key = WebKit_win32.CFStringCreateWithCharacters (0, chars, chars.length);
							if (key != 0) {
								int /*long*/ headers = WebKit_win32.CFDictionaryCreate (0, new int /*long*/[] {key}, new int /*long*/[] {value}, 1, WebKit_win32.kCFCopyStringDictionaryKeyCallBacks (), WebKit_win32.kCFTypeDictionaryValueCallBacks ());
								if (headers != 0) {
									int /*long*/ cookies = WebKit_win32.CFHTTPCookieCreateWithResponseHeaderFields (0, headers, cfUrl);
									if (cookies != 0) {
										int /*long*/ cookie = WebKit_win32.CFArrayGetValueAtIndex (cookies, 0);
										if (cookie != 0) {
											WebKit_win32.CFHTTPCookieStorageSetCookie (storage[0], cookie);
											CookieResult = true;
										}
										WebKit_win32.CFRelease (cookies);
									}
									WebKit_win32.CFRelease (headers);
								}
								WebKit_win32.CFRelease (key);
							}
							WebKit_win32.CFRelease (value);
						}
						WebKit_win32.CFRelease (cfUrl);
					}
					WebKit_win32.CFRelease (string);
				}
				// WebKit_win32.CFRelease (storage[0]);	//intentionally commented, causes crash
			}
		};

		if (NativePendingCookies != null) {
			SetPendingCookies (NativePendingCookies);
		}
		NativePendingCookies = null;
	}
}

static int /*long*/ createBSTR (String string) {
	char[] data = (string + '\0').toCharArray ();
	return COM.SysAllocString (data);
}

static String error (int code) {
	throw new SWTError ("WebKit error " + code); //$NON-NLS-1$
}

static String extractBSTR (int /*long*/ bstrString) {
	int size = COM.SysStringByteLen (bstrString);
	if (size == 0) return EMPTY_STRING;
	char[] buffer = new char[(size + 1) / 2]; // add one to avoid rounding errors
	COM.MoveMemory (buffer, bstrString, size);
	return new String (buffer);
}

static Browser findBrowser (int /*long*/ webView) {
	if (webView == 0) return null;
	IWebView iwebView = new IWebView (webView);
	int /*long*/[] result = new int /*long*/[1];
	int hr = iwebView.hostWindow (result);
	if (hr == COM.S_OK && result[0] != 0) {
		Widget widget = Display.getCurrent ().findWidget (result[0]);
		if (widget != null && widget instanceof Browser) return (Browser)widget;
	}
	return null;
}

static int /*long*/ JSObjectCallAsFunctionProc (int /*long*/ ctx, int /*long*/ function, int /*long*/ thisObject, int /*long*/ argumentCount, int /*long*/ arguments, int /*long*/ exception) {
	WebKit_win32.JSGlobalContextRetain (ctx);
	if (WebKit_win32.JSValueIsObjectOfClass (ctx, thisObject, ExternalClass) == 0) {
		return WebKit_win32.JSValueMakeUndefined (ctx);
	}
	int /*long*/ ptr = WebKit_win32.JSObjectGetPrivate (thisObject);
	int /*long*/[] handle = new int /*long*/[1];
	C.memmove (handle, ptr, C.PTR_SIZEOF);
	Browser browser = findBrowser (handle[0]);
	if (browser == null) return WebKit_win32.JSValueMakeUndefined (ctx);
	WebKit webkit = (WebKit)browser.webBrowser;
	return webkit.callJava (ctx, function, thisObject, argumentCount, arguments, exception);
}

static int /*long*/ JSObjectGetPropertyProc (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName, int /*long*/ exception) {
	byte[] bytes = null;
	try {
		bytes = (FUNCTIONNAME_CALLJAVA + '\0').getBytes (CHARSET_UTF8);
	} catch (UnsupportedEncodingException e) {
		bytes = (FUNCTIONNAME_CALLJAVA + '\0').getBytes ();
	} 
	int /*long*/ name = WebKit_win32.JSStringCreateWithUTF8CString (bytes);
	int /*long*/ addr = WebKit_win32.JSObjectCallAsFunctionProc_CALLBACK (WebKit.JSObjectCallAsFunctionProc.getAddress ());
	int /*long*/ function = WebKit_win32.JSObjectMakeFunctionWithCallback (ctx, name, addr);
	WebKit_win32.JSStringRelease (name);
	return function;
}

static int /*long*/ JSObjectHasPropertyProc (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName) {
	byte[] bytes = null;
	try {
		bytes = (FUNCTIONNAME_CALLJAVA + '\0').getBytes (CHARSET_UTF8);
	} catch (UnsupportedEncodingException e) {
		bytes = (FUNCTIONNAME_CALLJAVA + '\0').getBytes ();
	}
	return WebKit_win32.JSStringIsEqualToUTF8CString (propertyName, bytes);
}

static String readInstallDir (String keyString) {
	int /*long*/[] phkResult = new int /*long*/[1];
	TCHAR key = new TCHAR (0, keyString, true);
	if (OS.RegOpenKeyEx (OS.HKEY_LOCAL_MACHINE, key, 0, OS.KEY_READ, phkResult) == 0) {
		int[] lpcbData = new int[1];
		TCHAR buffer = new TCHAR (0, "InstallDir", true); //$NON-NLS-1$
		int result = OS.RegQueryValueEx (phkResult[0], buffer, 0, null, (TCHAR)null, lpcbData);
		if (result == 0) {
			TCHAR lpData = new TCHAR (0, lpcbData[0] / TCHAR.sizeof);
			result = OS.RegQueryValueEx (phkResult[0], buffer, 0, null, lpData, lpcbData);
			if (result == 0) {
				OS.RegCloseKey (phkResult[0]);
				return lpData.toString (0, lpData.strlen ());
			}
		}
		OS.RegCloseKey (phkResult[0]);
    }
    return null;
}

static String stringFromCFString (int /*long*/ cfString) {
	if (cfString == 0) return null;
	int length = WebKit_win32.CFStringGetLength (cfString);
	int /*long*/ ptr = WebKit_win32.CFStringGetCharactersPtr (cfString);
	char[] chars = new char[length];
	if (ptr != 0) {
		OS.MoveMemory (chars, ptr, length);
	} else {
		for (int j = 0; j < length; j++) {
			chars[j] = WebKit_win32.CFStringGetCharacterAtIndex (cfString, j);
		}
	}
	return new String (chars);
}

static String stringFromJSString (int /*long*/ jsString) {
	if (jsString == 0) return null;
	int length = WebKit_win32.JSStringGetLength (jsString);
	byte[] bytes = new byte[length + 1];
	WebKit_win32.JSStringGetUTF8CString (jsString, bytes, length + 1);
	return new String (bytes);
}

public boolean back () {
	int[] result = new int[1];
	webView.goBack (result);
	return result[0] != 0;
}

int /*long*/ callJava (int /*long*/ ctx, int /*long*/ func, int /*long*/ thisObject, int /*long*/ argumentCount, int /*long*/ arguments, int /*long*/ exception) {
	Object returnValue = null;
	if (argumentCount == 3) {
		int /*long*/[] result = new int /*long*/[1];
		C.memmove (result, arguments, C.PTR_SIZEOF);
		int type = WebKit_win32.JSValueGetType (ctx, result[0]);
		if (type == WebKit_win32.kJSTypeNumber) {
			int index = ((Double)convertToJava (ctx, result[0])).intValue ();
			result[0] = 0;
			if (index > 0) {
				Object key = new Integer (index);
				C.memmove (result, arguments + C.PTR_SIZEOF, C.PTR_SIZEOF);
				type = WebKit_win32.JSValueGetType (ctx, result[0]);
				if (type == WebKit_win32.kJSTypeString) {
					String token = (String)convertToJava (ctx, result[0]);
					BrowserFunction function = (BrowserFunction)functions.get (key);
					if (function != null && token.equals (function.token)) {
						try {
							C.memmove (result, arguments + 2 * C.PTR_SIZEOF, C.PTR_SIZEOF);
							Object temp = convertToJava (ctx, result[0]);
							if (temp instanceof Object[]) {
								Object[] args = (Object[])temp;
								try {
									returnValue = function.function (args);
								} catch (Exception e) {
									/* exception during function invocation */
									returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
								}
							}
						} catch (IllegalArgumentException e) {
							/* invalid argument value type */
							if (function.isEvaluate) {
								/* notify the function so that a java exception can be thrown */
								function.function (new String[] {WebBrowser.CreateErrorString (new SWTException (SWT.ERROR_INVALID_RETURN_VALUE).getLocalizedMessage ())});
							}
							returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
						}
					}
				}
			}
		}
	}
	return convertToJS (ctx, returnValue);
}

public boolean close () {
	return shouldClose ();
}

Object convertToJava (int /*long*/ ctx, int /*long*/ value) {
	int type = WebKit_win32.JSValueGetType (ctx, value);
	switch (type) {
		case WebKit_win32.kJSTypeBoolean: {
			int result = (int)WebKit_win32.JSValueToNumber (ctx, value, null);
			return new Boolean (result != 0);
		}
		case WebKit_win32.kJSTypeNumber: {
			double result = WebKit_win32.JSValueToNumber (ctx, value, null);
			return new Double (result);
		}
		case WebKit_win32.kJSTypeString: {
			int /*long*/ string = WebKit_win32.JSValueToStringCopy (ctx, value, null);
			if (string == 0) return ""; //$NON-NLS-1$
			int /*long*/ length = WebKit_win32.JSStringGetMaximumUTF8CStringSize (string);
			byte[] bytes = new byte[(int)/*64*/length];
			length = WebKit_win32.JSStringGetUTF8CString (string, bytes, length);
			WebKit_win32.JSStringRelease (string);
			try {
				/* length-1 is needed below to exclude the terminator character */
				return new String (bytes, 0, (int)/*64*/length - 1, CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				return new String (bytes);
			}
		}
		case WebKit_win32.kJSTypeNull:
			// FALL THROUGH
		case WebKit_win32.kJSTypeUndefined: return null;
		case WebKit_win32.kJSTypeObject: {
			byte[] bytes = null;
			try {
				bytes = (PROPERTY_LENGTH + '\0').getBytes (CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				bytes = (PROPERTY_LENGTH + '\0').getBytes ();
			}
			int /*long*/ propertyName = WebKit_win32.JSStringCreateWithUTF8CString (bytes);
			int /*long*/ valuePtr = WebKit_win32.JSObjectGetProperty (ctx, value, propertyName, null);
			WebKit_win32.JSStringRelease (propertyName);
			type = WebKit_win32.JSValueGetType (ctx, valuePtr);
			if (type == WebKit_win32.kJSTypeNumber) {
				int length = (int)WebKit_win32.JSValueToNumber (ctx, valuePtr, null);
				Object[] result = new Object[length];
				for (int i = 0; i < length; i++) {
					int /*long*/ current = WebKit_win32.JSObjectGetPropertyAtIndex (ctx, value, i, null);
					if (current != 0) {
						result[i] = convertToJava (ctx, current);
					}
				}
				return result;
			}
		}
	}
	SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	return null;
}

int /*long*/ convertToJS (int /*long*/ ctx, Object value) {
	if (value == null) {
		return WebKit_win32.JSValueMakeNull (ctx);
	}
	if (value instanceof String) {
		byte[] bytes = null;
		try {
			bytes = ((String)value + '\0').getBytes (CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			bytes = ((String)value + '\0').getBytes ();
		}
		int /*long*/ stringRef = WebKit_win32.JSStringCreateWithUTF8CString (bytes);
		int /*long*/ result = WebKit_win32.JSValueMakeString (ctx, stringRef);
		WebKit_win32.JSStringRelease (stringRef);
		return result;
	}
	if (value instanceof Boolean) {
		return WebKit_win32.JSValueMakeBoolean (ctx, ((Boolean)value).booleanValue () ? 1 : 0);
	}
	if (value instanceof Number) {
		return WebKit_win32.JSValueMakeNumber (ctx, ((Number)value).doubleValue ());
	}
	if (value instanceof Object[]) {
		Object[] arrayValue = (Object[]) value;
		int length = arrayValue.length;
		int /*long*/[] arguments = new int /*long*/[length];
		for (int i = 0; i < length; i++) {
			Object javaObject = arrayValue[i];
			int /*long*/ jsObject = convertToJS (ctx, javaObject);
			arguments[i] = jsObject;
		}
		return WebKit_win32.JSObjectMakeArray (ctx, length, arguments, null);
	}
	SWT.error (SWT.ERROR_INVALID_RETURN_VALUE);
	return 0;
}

public void create (Composite parent, int style) {
	if (!LibraryLoaded) {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES, null, LibraryLoadError == null ? null : " [" + LibraryLoadError + ']'); //$NON-NLS-1$
	}

	if (ExternalClass == 0) {
		JSClassDefinition jsClassDefinition = new JSClassDefinition ();
		byte[] bytes = (CLASSNAME_EXTERNAL + '\0').getBytes ();
		jsClassDefinition.className = C.malloc (bytes.length);
		OS.memmove (jsClassDefinition.className, bytes, bytes.length);

		/* custom callbacks for hasProperty, getProperty and callAsFunction */
		int /*long*/ addr = WebKit_win32.JSObjectHasPropertyProc_CALLBACK (JSObjectHasPropertyProc.getAddress ());
		jsClassDefinition.hasProperty = addr;
		addr = WebKit_win32.JSObjectGetPropertyProc_CALLBACK (JSObjectGetPropertyProc.getAddress ());
		jsClassDefinition.getProperty = addr;

		int /*long*/ classDefinitionPtr = C.malloc (JSClassDefinition.sizeof);
		WebKit_win32.memmove (classDefinitionPtr, jsClassDefinition, JSClassDefinition.sizeof);
		ExternalClass = WebKit_win32.JSClassCreate (classDefinitionPtr);
		WebKit_win32.JSClassRetain (ExternalClass);
	}

	int /*long*/[] result = new int /*long*/[1];
	int hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebView, 0, WebKit_win32.IID_IWebView, result);
	if (hr != COM.S_OK || result[0] == 0) {
		browser.dispose ();
		error (hr);
	}
	webView = new IWebView (result[0]);
	webViewData = C.malloc (C.PTR_SIZEOF);
	C.memmove (webViewData, new int /*long*/[] {webView.getAddress ()}, C.PTR_SIZEOF);
	hr = webView.setHostWindow (browser.handle);
	if (hr != COM.S_OK) {
		browser.dispose ();
		error (hr);
	}
	hr = webView.initWithFrame (new RECT (), 0, 0);
	if (hr != COM.S_OK) {
		browser.dispose ();
		error (hr);
	}
	result[0] = 0;
	hr = webView.QueryInterface (WebKit_win32.IID_IWebViewPrivate, result);
	if (hr != COM.S_OK || result[0] == 0) {
		browser.dispose ();
		error (hr);
	}
	IWebViewPrivate webViewPrivate = new IWebViewPrivate (result[0]);
	result[0] = 0;
	hr = webViewPrivate.viewWindow (result);
	if (hr != COM.S_OK || result[0] == 0) {
		browser.dispose ();
		error (hr);
	}
	webViewPrivate.Release ();
	webViewWindowHandle = result[0];

	webFrameLoadDelegate = new WebFrameLoadDelegate (browser);
	hr = webView.setFrameLoadDelegate (webFrameLoadDelegate.getAddress ());
	if (hr != COM.S_OK) {
		browser.dispose ();
		error (hr);
	}
	webUIDelegate = new WebUIDelegate (browser);
	hr = webView.setUIDelegate (webUIDelegate.getAddress ());
	if (hr != COM.S_OK) {
		browser.dispose ();
		error (hr);
	}

	webResourceLoadDelegate = new WebResourceLoadDelegate (browser);
	hr = webView.setResourceLoadDelegate (webResourceLoadDelegate.getAddress ());
	if (hr != COM.S_OK) {
		browser.dispose ();
		error (hr);
	}

	webDownloadDelegate = new WebDownloadDelegate (browser);
	hr = webView.setDownloadDelegate (webDownloadDelegate.getAddress ());
	if (hr != COM.S_OK) {
		browser.dispose ();
		error (hr);
	}

	webPolicyDelegate = new WebPolicyDelegate (browser);
	hr = webView.setPolicyDelegate (webPolicyDelegate.getAddress ());
	if (hr != COM.S_OK) {
		browser.dispose ();
		error (hr);
	}

	initializeWebViewPreferences ();

	Listener listener = new Listener () {
		public void handleEvent (Event e) {
			switch (e.type) {
				case SWT.Dispose: {
					/* make this handler run after other dispose listeners */
					if (ignoreDispose) {
						ignoreDispose = false;
						break;
					}
					ignoreDispose = true;
					browser.notifyListeners (e.type, e);
					e.type = SWT.NONE;
					onDispose ();
					break;
				}
				case SWT.FocusIn: {
					OS.SetFocus (webViewWindowHandle);
					break;
				}
				case SWT.Resize: {
					Rectangle bounds = browser.getClientArea ();
					OS.SetWindowPos (webViewWindowHandle, 0, bounds.x, bounds.y, bounds.width, bounds.height, OS.SWP_DRAWFRAME);
					break;
				}
				case SWT.Traverse: {
					if (traverseOut) {
						e.doit = true;
						traverseOut = false;
					} else {
						e.doit = false;
					}
					break;
				}
			}
		}
	};
	browser.addListener (SWT.Dispose, listener);
	browser.addListener (SWT.KeyDown, listener); /* needed for tabbing into the Browser */
	browser.addListener (SWT.FocusIn, listener);
	browser.addListener (SWT.Resize, listener);
	browser.addListener (SWT.Traverse, listener);

	eventFunction = new BrowserFunction (browser, "HandleWebKitEvent") { //$NON-NLS-1$
		public Object function (Object[] arguments) {
			return handleEvent (arguments) ? Boolean.TRUE : Boolean.FALSE;
		};	
	};
}

public boolean execute (String script) {
	int /*long*/[] result = new int /*long*/[1];
	int hr = webView.mainFrame (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return false;
	}
	IWebFrame frame = new IWebFrame (result[0]);
	int /*long*/ context = frame.globalContext ();
	frame.Release ();
	if (context == 0) {
		return false;
	}
	byte[] bytes = null;
	try {
		bytes = (script + '\0').getBytes ("UTF-8"); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = (script + '\0').getBytes ();
	}
	int /*long*/ scriptString = WebKit_win32.JSStringCreateWithUTF8CString (bytes);
	if (scriptString == 0) return false;
	try {
		bytes = (getUrl () + '\0').getBytes ("UTF-8"); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = (getUrl () + '\0').getBytes ();
	}
	int /*long*/ urlString = WebKit_win32.JSStringCreateWithUTF8CString (bytes);
	if (urlString == 0) {
		WebKit_win32.JSStringRelease (scriptString);
		return false;
	}
	int /*long*/ evalResult = WebKit_win32.JSEvaluateScript (context, scriptString, 0, urlString, 0, null);
	WebKit_win32.JSStringRelease (urlString);
	WebKit_win32.JSStringRelease (scriptString);
	return evalResult != 0;
}

public boolean forward () {
	int[] result = new int[1];
	webView.goForward (result);
	return result[0] != 0;
}

public String getBrowserType () {
	return "webkit"; //$NON-NLS-1$
}

public String getText () {
	int /*long*/[] result = new int /*long*/[1];
	int hr = webView.mainFrame (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return EMPTY_STRING;
	}
	IWebFrame mainFrame = new IWebFrame (result[0]);
	result[0] = 0;
	hr = mainFrame.dataSource (result);
	mainFrame.Release ();
	if (hr != COM.S_OK || result[0] == 0) {
		return EMPTY_STRING;
	}
	IWebDataSource dataSource = new IWebDataSource (result[0]);
	result[0] = 0;
	hr = dataSource.representation (result);
	dataSource.Release ();
	if (hr != COM.S_OK || result[0] == 0) {
		return EMPTY_STRING;
	}
	IWebDocumentRepresentation representation = new IWebDocumentRepresentation (result[0]);
	result[0] = 0;
	hr = representation.documentSource (result);
	representation.Release ();
	if (hr != COM.S_OK || result[0] == 0) {
		return EMPTY_STRING;
	}
	String source = extractBSTR (result[0]);
	COM.SysFreeString (result[0]);
	return source;	
}

public String getUrl () {
	return webFrameLoadDelegate.getUrl ();
}

boolean handleEvent (Object[] arguments) {

	/*
	* DOM events are currently received by hooking DOM listeners
	* in javascript that invoke this method via a BrowserFunction.
	* Document.addListener is not implemented on WebKit on windows.
	* The argument lists received here are:
	* 
	* For key events:
	* 	argument 0: type (String)
	* 	argument 1: keyCode (Double)
	* 	argument 2: charCode (Double)
	* 	argument 3: altKey (Boolean)
	* 	argument 4: ctrlKey (Boolean)
	* 	argument 5: shiftKey (Boolean)
	* 	argument 6: metaKey (Boolean)
	* 	returns doit
	* 
	* For mouse events
	* 	argument 0: type (String)
	* 	argument 1: screenX (Double)
	* 	argument 2: screenY (Double)
	* 	argument 3: detail (Double)
	* 	argument 4: button (Double)
	* 	argument 5: altKey (Boolean)
	* 	argument 6: ctrlKey (Boolean)
	* 	argument 7: shiftKey (Boolean)
	* 	argument 8: metaKey (Boolean)
	* 	argument 9: hasRelatedTarget (Boolean)
	* 	returns doit
	*/

	String type = (String)arguments[0];
	if (type.equals (DOMEVENT_KEYDOWN)) {
		int keyCode = translateKey (((Double)arguments[1]).intValue ());
		lastKeyCode = keyCode;
		switch (keyCode) {
			case SWT.SHIFT:
			case SWT.CONTROL:
			case SWT.ALT:
			case SWT.CAPS_LOCK:
			case SWT.NUM_LOCK:
			case SWT.SCROLL_LOCK:
			case SWT.COMMAND:
//			case SWT.ESC:
			case SWT.TAB:
			case SWT.PAUSE:
//			case SWT.BS:
			case SWT.INSERT:
			case SWT.DEL:
			case SWT.HOME:
			case SWT.END:
			case SWT.PAGE_UP:
			case SWT.PAGE_DOWN:
			case SWT.ARROW_DOWN:
			case SWT.ARROW_UP:
			case SWT.ARROW_LEFT:
			case SWT.ARROW_RIGHT:
			case SWT.F1:
			case SWT.F2:
			case SWT.F3:
			case SWT.F4:
			case SWT.F5:
			case SWT.F6:
			case SWT.F7:
			case SWT.F8:
			case SWT.F9:
			case SWT.F10:
			case SWT.F11:
			case SWT.F12: {
				/* keypress events will not be received for these keys, so send KeyDowns for them now */

				Event keyEvent = new Event ();
				keyEvent.widget = browser;
				keyEvent.type = type.equals (DOMEVENT_KEYDOWN) ? SWT.KeyDown : SWT.KeyUp;
				keyEvent.keyCode = keyCode;
				switch (keyCode) {
					case SWT.BS: keyEvent.character = SWT.BS; break;
					case SWT.DEL: keyEvent.character = SWT.DEL; break;
					case SWT.ESC: keyEvent.character = SWT.ESC; break;
					case SWT.TAB: keyEvent.character = SWT.TAB; break;
				}
				lastCharCode = keyEvent.character;
				keyEvent.stateMask =
					(((Boolean)arguments[3]).booleanValue () ? SWT.ALT : 0) |
					(((Boolean)arguments[4]).booleanValue () ? SWT.CTRL : 0) |
					(((Boolean)arguments[5]).booleanValue () ? SWT.SHIFT : 0) |
					(((Boolean)arguments[6]).booleanValue () ? SWT.COMMAND : 0);
				keyEvent.stateMask &= ~keyCode;		/* remove current keydown if it's a state key */
				if (!sendKeyEvent (keyEvent) || browser.isDisposed ()) return false;
				break;
			}
		}
		return true;
	}

	if (type.equals (DOMEVENT_KEYPRESS)) {
		/*
		* if keydown could not determine a keycode for this key then it's a
		* key for which key events are not sent (eg.- the Windows key)
		*/
		if (lastKeyCode == 0) return true;

		lastCharCode = ((Double)arguments[2]).intValue ();
		if (((Boolean)arguments[4]).booleanValue () && (0 <= lastCharCode && lastCharCode <= 0x7F)) {
			if ('a' <= lastCharCode && lastCharCode <= 'z') lastCharCode -= 'a' - 'A';
			if (64 <= lastCharCode && lastCharCode <= 95) lastCharCode -= 64;
		}

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyDown;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.character = (char)lastCharCode;
		keyEvent.stateMask =
			(((Boolean)arguments[3]).booleanValue () ? SWT.ALT : 0) |
			(((Boolean)arguments[4]).booleanValue () ? SWT.CTRL : 0) |
			(((Boolean)arguments[5]).booleanValue () ? SWT.SHIFT : 0) |
			(((Boolean)arguments[6]).booleanValue () ? SWT.COMMAND : 0);
		return sendKeyEvent (keyEvent) && !browser.isDisposed ();
	}

	if (type.equals (DOMEVENT_KEYUP)) {
		int keyCode = translateKey (((Double)arguments[1]).intValue ());
		if (keyCode == 0) {
			/* indicates a key for which key events are not sent */
			return true;
		}
		if (keyCode != lastKeyCode) {
			/* keyup does not correspond to the last keydown */
			lastKeyCode = keyCode;
			lastCharCode = 0;
		}

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyUp;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.character = (char)lastCharCode;
		keyEvent.stateMask =
			(((Boolean)arguments[3]).booleanValue () ? SWT.ALT : 0) |
			(((Boolean)arguments[4]).booleanValue () ? SWT.CTRL : 0) |
			(((Boolean)arguments[5]).booleanValue () ? SWT.SHIFT : 0) |
			(((Boolean)arguments[6]).booleanValue () ? SWT.COMMAND : 0);
		switch (lastKeyCode) {
			case SWT.SHIFT:
			case SWT.CONTROL:
			case SWT.ALT:
			case SWT.COMMAND: {
				keyEvent.stateMask |= lastKeyCode;
			}
		}
		browser.notifyListeners (keyEvent.type, keyEvent);
		lastKeyCode = lastCharCode = 0;
		return keyEvent.doit && !browser.isDisposed ();
	}

	/* mouse events */

	/*
	 * MouseOver and MouseOut events are fired any time the mouse enters or exits
	 * any element within the Browser.  To ensure that SWT events are only
	 * fired for mouse movements into or out of the Browser, do not fire an
	 * event if there is a related target element.
	 */
	if (type.equals (DOMEVENT_MOUSEOVER) || type.equals (DOMEVENT_MOUSEOUT)) {
		if (((Boolean)arguments[9]).booleanValue ()) return true;
	}

	/*
	 * The position of mouse events is received in screen-relative coordinates
	 * in order to handle pages with frames, since frames express their event
	 * coordinates relative to themselves rather than relative to their top-
	 * level page.  Convert screen-relative coordinates to be browser-relative.
	 */
	Point position = new Point (((Double)arguments[1]).intValue (), ((Double)arguments[2]).intValue ());
	position = browser.getDisplay ().map (null, browser, position); 

	Event mouseEvent = new Event ();
	mouseEvent.widget = browser;
	mouseEvent.x = position.x;
	mouseEvent.y = position.y;
	int mask =
		(((Boolean)arguments[5]).booleanValue () ? SWT.ALT : 0) |
		(((Boolean)arguments[6]).booleanValue () ? SWT.CTRL : 0) |
		(((Boolean)arguments[7]).booleanValue () ? SWT.SHIFT : 0);
	mouseEvent.stateMask = mask;

	if (type.equals (DOMEVENT_MOUSEDOWN)) {
		mouseEvent.type = SWT.MouseDown;
		mouseEvent.count = ((Double)arguments[3]).intValue ();
		mouseEvent.button = ((Double)arguments[4]).intValue ();
		browser.notifyListeners (mouseEvent.type, mouseEvent);
		if (browser.isDisposed ()) return true;
		if (((Double)arguments[3]).intValue () == 2) {
			mouseEvent = new Event ();
			mouseEvent.type = SWT.MouseDoubleClick;
			mouseEvent.widget = browser;
			mouseEvent.x = position.x;
			mouseEvent.y = position.y;
			mouseEvent.stateMask = mask;
			mouseEvent.count = ((Double)arguments[3]).intValue ();
			mouseEvent.button = ((Double)arguments[4]).intValue ();
			browser.notifyListeners (mouseEvent.type, mouseEvent);
		}
		return true;
	}

	if (type.equals (DOMEVENT_MOUSEUP)) {
		mouseEvent.type = SWT.MouseUp;
		mouseEvent.count = ((Double)arguments[3]).intValue ();
		mouseEvent.button = ((Double)arguments[4]).intValue ();
		switch (mouseEvent.button) {
			case 1: mouseEvent.stateMask |= SWT.BUTTON1; break;
			case 2: mouseEvent.stateMask |= SWT.BUTTON2; break;
			case 3: mouseEvent.stateMask |= SWT.BUTTON3; break;
			case 4: mouseEvent.stateMask |= SWT.BUTTON4; break;
			case 5: mouseEvent.stateMask |= SWT.BUTTON5; break;
		}
	} else if (type.equals (DOMEVENT_MOUSEMOVE)) {
		mouseEvent.type = SWT.MouseMove;
	} else if (type.equals (DOMEVENT_MOUSEWHEEL)) {
		mouseEvent.type = SWT.MouseWheel;
		mouseEvent.count = ((Double)arguments[3]).intValue ();
	} else if (type.equals (DOMEVENT_MOUSEOVER)) {
		mouseEvent.type = SWT.MouseEnter;
	} else if (type.equals (DOMEVENT_MOUSEOUT)) {
		mouseEvent.type = SWT.MouseExit;
		if (mouseEvent.x < 0) mouseEvent.x = -1;
		if (mouseEvent.y < 0) mouseEvent.y = -1;
	} else if (type.equals (DOMEVENT_DRAGSTART)) {
		mouseEvent.type = SWT.DragDetect;
		mouseEvent.button = ((Double)arguments[4]).intValue () + 1;
		switch (mouseEvent.button) {
			case 1: mouseEvent.stateMask |= SWT.BUTTON1; break;
			case 2: mouseEvent.stateMask |= SWT.BUTTON2; break;
			case 3: mouseEvent.stateMask |= SWT.BUTTON3; break;
			case 4: mouseEvent.stateMask |= SWT.BUTTON4; break;
			case 5: mouseEvent.stateMask |= SWT.BUTTON5; break;
		}
	}

	browser.notifyListeners (mouseEvent.type, mouseEvent);
	return true;
}

public boolean isBackEnabled () {
	int /*long*/[] address = new int /*long*/[1];
	int hr = webView.QueryInterface (WebKit_win32.IID_IWebIBActions, address);
	if (hr != COM.S_OK || address[0] == 0) {
		return false;
	}
	IWebIBActions webIBActions = new IWebIBActions (address[0]);
	int[] result = new int[1];
	webIBActions.canGoBack (webView.getAddress (), result);
	webIBActions.Release ();
	return result[0] != 0;
}

public boolean isFocusControl () {
	int /*long*/ hwndFocus = OS.GetFocus ();
	return hwndFocus != 0 && hwndFocus == webViewWindowHandle;
}

public boolean isForwardEnabled () {
	int /*long*/[] address = new int /*long*/[1];
	int hr = webView.QueryInterface (WebKit_win32.IID_IWebIBActions, address);
	if (hr != COM.S_OK || address[0] == 0) {
		return false;
	}
	IWebIBActions webIBActions = new IWebIBActions (address[0]);
	int[] result = new int[1];
	webIBActions.canGoForward (webView.getAddress (), result);
	webIBActions.Release ();
	return result[0] != 0;
}

void onDispose () {
	/* Browser could have been disposed by one of the Dispose listeners */
	if (!browser.isDisposed ()) {
		/* invoke onbeforeunload handlers but don't prompt with message box */
		if (!browser.isClosing) {
			webUIDelegate.prompt = false;
			shouldClose ();
			webUIDelegate.prompt = true;
		}
	}

	Enumeration elements = functions.elements ();
	while (elements.hasMoreElements ()) {
		((BrowserFunction)elements.nextElement ()).dispose (false);
	}
	functions = null;

	eventFunction.dispose();
	eventFunction = null;
	C.free (webViewData);

	webView.setPreferences (0);
	webView.setHostWindow (0);
	webView.setFrameLoadDelegate (0);
	webView.setResourceLoadDelegate (0);
	webView.setUIDelegate (0);
	webView.setPolicyDelegate (0);
	webView.setDownloadDelegate (0);
	webView.Release();
	webView = null;
	webDownloadDelegate = null;
	webFrameLoadDelegate = null;
	webPolicyDelegate = null;
	webResourceLoadDelegate = null;
	webUIDelegate = null;
	lastNavigateURL = null;
}

public void refresh () {
	webFrameLoadDelegate.html = null;
	int /*long*/[] result = new int /*long*/[1];
	int hr = webView.QueryInterface (WebKit_win32.IID_IWebIBActions, result);
	if (hr != COM.S_OK || result[0] == 0) {
		return;
	}
	IWebIBActions webIBActions = new IWebIBActions (result[0]);
	webIBActions.reload (webView.getAddress ());
	webIBActions.Release ();
}

boolean sendKeyEvent (Event event) {
	/*
	 * browser.traverse() is called through dislay.translateTraversal() for all
	 * traversal types except SWT.TRAVERSE_MNEMONIC. So, override
	 * WebBrowser.sendKeyEvent() so that when it is called from handleEvent(),
	 * browser.traverse() is not called again.
	 */
	boolean doit = true;
	switch (event.keyCode) {
		case SWT.ESC:
		case SWT.CR:
		case SWT.ARROW_DOWN:
		case SWT.ARROW_RIGHT:
		case SWT.ARROW_UP:
		case SWT.ARROW_LEFT:
		case SWT.TAB:
		case SWT.PAGE_DOWN:
		case SWT.PAGE_UP:
			break;
		default: {
			if (translateMnemonics ()) {
				if (event.character != 0 && (event.stateMask & (SWT.ALT | SWT.CTRL)) == SWT.ALT) {
					int traversal = SWT.TRAVERSE_MNEMONIC;
					boolean oldEventDoit = event.doit;
					event.doit = true;	
					doit = !browser.traverse (traversal, event);
					event.doit = oldEventDoit;
				}
			}
			break;
		}
	}
	if (doit) {
		browser.notifyListeners (event.type, event);
		doit = event.doit; 
	}
	return doit;
}

public boolean setText (String html, boolean trusted) {
	/*
	* If this.html is not null then the about:blank page is already being loaded,
	* so no navigate is required.  Just set the html that is to be shown.
	*/
	boolean blankLoading = webFrameLoadDelegate.html != null;
	webFrameLoadDelegate.html = html;
	untrustedText = !trusted;
	if (blankLoading) return true;

	int /*long*/[] result = new int /*long*/[1];
	int hr = webView.mainFrame (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return false;
	}
	IWebFrame frame = new IWebFrame (result[0]);

	result[0] = 0;
	hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebMutableURLRequest, 0, WebKit_win32.IID_IWebMutableURLRequest, result);
	if (hr != COM.S_OK || result[0] == 0) {
		frame.Release ();
		return false;
	}
	IWebMutableURLRequest request = new IWebMutableURLRequest (result[0]);

	int /*long*/ urlString = createBSTR (ABOUT_BLANK);
	hr = request.setURL (urlString);
	COM.SysFreeString (urlString);

	if (hr == COM.S_OK) {
		hr = frame.loadRequest (request.getAddress ());
	}
	frame.Release ();
	request.Release ();
	return hr == COM.S_OK;
}

public boolean setUrl (String url, String postData, String[] headers) {
	if (url.length () == 0) return false;
	/*
	* WebKit attempts to open the exact url string that is passed to it and
	* will not infer a protocol if it's not specified.  Detect the case of an
	* invalid URL string and try to fix it by prepending an appropriate protocol.
	*/
	try {
		new URL (url);
	} catch (MalformedURLException e) {
		String testUrl = null;
		if (new File (url).isAbsolute ()) {
			/* appears to be a local file */
			testUrl = PROTOCOL_FILE + url; 
		} else {
			testUrl = PROTOCOL_HTTP + url;
		}
		try {
			new URL (testUrl);
			url = testUrl;		/* adding the protocol made the url valid */
		} catch (MalformedURLException e2) {
			/* adding the protocol did not make the url valid, so do nothing */
		}
	}
	webFrameLoadDelegate.html = null;
	lastNavigateURL = url;
	int /*long*/[] result = new int /*long*/[1];
	int hr = webView.mainFrame (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return false;
	}
	IWebFrame frame = new IWebFrame (result[0]);

	result[0] = 0;
	hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebMutableURLRequest, 0, WebKit_win32.IID_IWebMutableURLRequest, result);
	if (hr != COM.S_OK || result[0] == 0) {
		frame.Release ();
		return false;
	}
	IWebMutableURLRequest request = new IWebMutableURLRequest (result[0]);

	if (postData != null) { //TODO: POST
//    	webResourceLoadDelegate.postData = postData;
//    	int /*long*/ postString = createBSTR (POST);
//		hr = request.setHTTPMethod (postString);
//		COM.SysFreeString (postString);
//		
//		result[0] = 0;
//		hr = request.QueryInterface (WebKit_win32.IID_IWebMutableURLRequestPrivate, result);
//		if (hr == COM.S_OK && result[0] != 0) {
//			IWebMutableURLRequestPrivate requestPrivate = new IWebMutableURLRequestPrivate(result[0]);
//			int cfRequest = requestPrivate.cfRequest();
//			byte[] bytes = postData.getBytes();
//			int /*long*/ data = WebKit_win32.CFDataCreate(0, bytes, bytes.length);
//			if (data != 0)WebKit_win32.CFURLRequestSetHTTPRequestBody(cfRequest, data);
//			
//			int /*long*/ dataGet = WebKit_win32.CFURLRequestCopyHTTPRequestBody(cfRequest);
//			int length = WebKit_win32.CFDataGetLength(dataGet);
//			int /*long*/ bytePtr = WebKit_win32.CFDataGetBytePtr(dataGet);
//		}
	}
	hr = COM.S_OK;	//TODO: once post code is completed, remove this line if not required
	if (headers != null) {
		for (int i = 0; i < headers.length; i++) {
			String current = headers[i];
			if (current != null) {
				int index = current.indexOf (':');
				if (index != -1) {
					String key = current.substring (0, index).trim ();
					String value = current.substring (index + 1).trim ();
					if (key.length () > 0 && value.length () > 0) {
						int /*long*/ valueString = createBSTR (value);
						if (key.equalsIgnoreCase (USER_AGENT)) {
							/*
							* Feature of WebKit.  The user-agent header value cannot be overridden
							* here.  The workaround is to temporarily set the value on the WebView
							* and then remove it after the loading of the request has begun.
							*/
							hr = webView.setCustomUserAgent (valueString);
						} else {
							int /*long*/ keyString = createBSTR (key);
							hr = request.setValue (valueString, keyString);
							COM.SysFreeString (keyString);
						}
						COM.SysFreeString (valueString);
					}
				}
			}
		}
	}
	if (hr == COM.S_OK) {
		int /*long*/ urlString = createBSTR (url);
		hr = request.setURL (urlString);
		COM.SysFreeString (urlString);
		if (hr == COM.S_OK) {
			hr = frame.loadRequest (request.getAddress ());
		}
		webView.setCustomUserAgent (0);
	}
	frame.Release ();
	request.Release ();
	return hr == COM.S_OK;
}

boolean shouldClose () {
	if (!jsEnabled) return true;

	int /*long*/[] address = new int /*long*/[1];
	int hr = webView.QueryInterface (WebKit_win32.IID_IWebViewPrivate, address);
	if (hr != COM.S_OK || address[0] == 0) {
		return false;
	}
	IWebViewPrivate webViewPrivate = new IWebViewPrivate (address[0]);
	int[] result = new int[1];
	/* This function will fire the before unload handler for a page */
	webViewPrivate.shouldClose (result);
	webViewPrivate.Release ();
	return result[0] != 0;
}

public void stop () {
	webFrameLoadDelegate.html = null;
	int /*long*/[] result = new int /*long*/[1];
	int hr = webView.QueryInterface (WebKit_win32.IID_IWebIBActions, result);
	if (hr != COM.S_OK || result[0] == 0) {
		return;
	}
	IWebIBActions webIBActions = new IWebIBActions (result[0]);
	webIBActions.stopLoading (webView.getAddress ());
	webIBActions.Release ();
}

void initializeWebViewPreferences () {
	/* 
	 * Try to create separate preferences for each webview using different identifier for each webview. 
	 * Otherwise all the webviews use the shared preferences.
	 */
	int /*long*/[] result = new int /*long*/[1];
	int hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebPreferences, 0, WebKit_win32.IID_IWebPreferences, result);
	if (hr == COM.S_OK && result[0] != 0) {
		IWebPreferences preferences = new IWebPreferences (result[0]);
		result[0] = 0;
		hr = preferences.initWithIdentifier (createBSTR (String.valueOf (prefsIdentifier++)), result);
		preferences.Release ();
		if (hr == COM.S_OK && result[0] != 0) {
			preferences = new IWebPreferences (result[0]);
			webView.setPreferences (preferences.getAddress());
			preferences.Release ();
		}
	}
	
	result[0] = 0;
	hr = webView.preferences (result);
	if (hr == COM.S_OK && result[0] != 0) {
		IWebPreferences preferences = new IWebPreferences (result[0]);
		preferences.setJavaScriptEnabled (1);
		preferences.setJavaScriptCanOpenWindowsAutomatically (1);
		preferences.setJavaEnabled (0);	/* disable applets */
		preferences.setTabsToLinks (1);
		preferences.setFontSmoothing (WebKit_win32.FontSmoothingTypeWindows);
		preferences.Release ();
	}
}

}

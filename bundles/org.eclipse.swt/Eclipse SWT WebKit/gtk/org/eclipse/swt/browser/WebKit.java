/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;


import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class WebKit extends WebBrowser {
	int /*long*/ webView, webViewData, scrolledWindow;
	int failureCount, lastKeyCode, lastCharCode;
	String postData;
	String[] headers;
	boolean ignoreDispose, loadingText, untrustedText;
	byte[] htmlBytes;
	BrowserFunction eventFunction;

	static int /*long*/ ExternalClass, PostString;
	static boolean LibraryLoaded;

	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String CHARSET_UTF8 = "UTF-8"; //$NON-NLS-1$
	static final String CLASSNAME_EXTERNAL = "External"; //$NON-NLS-1$
	static final String ENCODING_FORM = "Content-Type: application/x-www-form-urlencoded"; //$NON-NLS-1$
	static final String FUNCTIONNAME_CALLJAVA = "callJava"; //$NON-NLS-1$
	static final String OBJECTNAME_EXTERNAL = "external"; //$NON-NLS-1$
	static final String PROPERTY_LENGTH = "length"; //$NON-NLS-1$
	static final String PROPERTY_PROXYHOST = "network.proxy_host"; //$NON-NLS-1$
	static final String PROPERTY_PROXYPORT = "network.proxy_port"; //$NON-NLS-1$
	static final String PROTOCOL_FILE = "file://"; //$NON-NLS-1$
	static final String PROTOCOL_HTTP = "http://"; //$NON-NLS-1$
	static final String URI_FILEROOT = "file:///"; //$NON-NLS-1$
	static final String USER_AGENT = "user-agent"; //$NON-NLS-1$
	static final int MAX_PORT = 65535;
	static final int MAX_PROGRESS = 100;
	static final int[] MIN_VERSION = {1, 2, 0};
	static final char SEPARATOR_FILE = System.getProperty ("file.separator").charAt (0); //$NON-NLS-1$
	static final int STOP_PROPOGATE = 1;

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

	/* WebKit signal data */
	static final int HOVERING_OVER_LINK = 1;
	static final int NOTIFY_PROGRESS = 2;
	static final int NAVIGATION_POLICY_DECISION_REQUESTED = 3;
	static final int NOTIFY_TITLE = 4;
	static final int POPULATE_POPUP = 5;
	static final int STATUS_BAR_TEXT_CHANGED = 6;
	static final int CREATE_WEB_VIEW = 7;
	static final int WEB_VIEW_READY = 8;
	static final int NOTIFY_LOAD_STATUS = 9;
	static final int RESOURCE_REQUEST_STARTING = 10;
	static final int DOWNLOAD_REQUESTED = 11;
	static final int MIME_TYPE_POLICY_DECISION_REQUESTED = 12;
	static final int CLOSE_WEB_VIEW = 13;
	static final int WINDOW_OBJECT_CLEARED = 14;
	static final int CONSOLE_MESSAGE = 15;

	static final String KEY_CHECK_SUBWINDOW = "org.eclipse.swt.internal.control.checksubwindow"; //$NON-NLS-1$

	// the following Callbacks are never freed
	static Callback Proc2, Proc3, Proc4, Proc5, Proc6;
	static Callback JSObjectHasPropertyProc, JSObjectGetPropertyProc, JSObjectCallAsFunctionProc;

	static {
		try {
			Library.loadLibrary ("swt-webkit"); // $NON-NLS-1$
			LibraryLoaded = true;
		} catch (Throwable e) {
			/* do not initialize the Callbacks below */
			return;
		}

		Proc2 = new Callback (WebKit.class, "Proc", 2); //$NON-NLS-1$
		if (Proc2.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Proc3 = new Callback (WebKit.class, "Proc", 3); //$NON-NLS-1$
		if (Proc3.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Proc4 = new Callback (WebKit.class, "Proc", 4); //$NON-NLS-1$
		if (Proc4.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Proc5 = new Callback (WebKit.class, "Proc", 5); //$NON-NLS-1$
		if (Proc5.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Proc6 = new Callback (WebKit.class, "Proc", 6); //$NON-NLS-1$
		if (Proc6.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		JSObjectHasPropertyProc = new Callback (WebKit.class, "JSObjectHasPropertyProc", 3); //$NON-NLS-1$
		if (JSObjectHasPropertyProc.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		JSObjectGetPropertyProc = new Callback (WebKit.class, "JSObjectGetPropertyProc", 4); //$NON-NLS-1$
		if (JSObjectGetPropertyProc.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		JSObjectCallAsFunctionProc = new Callback (WebKit.class, "JSObjectCallAsFunctionProc", 6); //$NON-NLS-1$
		if (JSObjectCallAsFunctionProc.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		NativeClearSessions = new Runnable () {
			public void run () {
				if (!LibraryLoaded) return;
				int /*long*/ session = WebKitGTK.webkit_get_default_session ();
				int type = WebKitGTK.soup_cookie_jar_get_type ();
				int /*long*/ jar = WebKitGTK.soup_session_get_feature (session, type);
				if (jar == 0) return;
				int /*long*/ cookies = WebKitGTK.soup_cookie_jar_all_cookies (jar);
				int length = OS.g_slist_length (cookies);
				int /*long*/ current = cookies;
				for (int i = 0; i < length; i++) {
					int /*long*/ cookie = OS.g_slist_data (current);
					int /*long*/ expires = WebKitGTK.SoupCookie_expires (cookie);
					if (expires == 0) {
						/* indicates a session cookie */
						WebKitGTK.soup_cookie_jar_delete_cookie (jar, cookie);
					}
					OS.g_free (cookie);
					current = OS.g_slist_next (current);
				}
			}
		};

		NativeGetCookie = new Runnable () {
			public void run () {
				if (!LibraryLoaded) return;
				int /*long*/ session = WebKitGTK.webkit_get_default_session ();
				int type = WebKitGTK.soup_cookie_jar_get_type ();
				int /*long*/ jar = WebKitGTK.soup_session_get_feature (session, type);
				if (jar == 0) return;
				byte[] bytes = Converter.wcsToMbcs (null, CookieUrl, true);
				int /*long*/ uri = WebKitGTK.soup_uri_new (bytes);
				if (uri == 0) return;
				int /*long*/ cookies = WebKitGTK.soup_cookie_jar_get_cookies (jar, uri, 0);
				WebKitGTK.soup_uri_free (uri);
				if (cookies == 0) return;
				int length = OS.strlen (cookies);
				bytes = new byte[length];
				C.memmove (bytes, cookies, length);
				OS.g_free (cookies);
				String allCookies = new String (Converter.mbcsToWcs (null, bytes));
				StringTokenizer tokenizer = new StringTokenizer (allCookies, ";"); //$NON-NLS-1$
				while (tokenizer.hasMoreTokens ()) {
					String cookie = tokenizer.nextToken ();
					int index = cookie.indexOf ('=');
					if (index != -1) {
						String name = cookie.substring (0, index).trim ();
						if (name.equals (CookieName)) {
							CookieValue = cookie.substring (index + 1).trim ();
							return;
						}
					}
				}

			}
		};

		NativeSetCookie = new Runnable () {
			public void run () {
				if (!LibraryLoaded) return;
				int /*long*/ session = WebKitGTK.webkit_get_default_session ();
				int type = WebKitGTK.soup_cookie_jar_get_type ();
				int /*long*/ jar = WebKitGTK.soup_session_get_feature (session, type);
				if (jar == 0) {
					/* this happens if a navigation has not occurred yet */
					WebKitGTK.soup_session_add_feature_by_type (session, type);
					jar = WebKitGTK.soup_session_get_feature (session, type);
				}
				if (jar == 0) return;
				byte[] bytes = Converter.wcsToMbcs (null, CookieUrl, true);
				int /*long*/ uri = WebKitGTK.soup_uri_new (bytes);
				if (uri == 0) return;
				bytes = Converter.wcsToMbcs (null, CookieValue, true);
				int /*long*/ cookie = WebKitGTK.soup_cookie_parse (bytes, uri);
				if (cookie != 0) {
					WebKitGTK.soup_cookie_jar_add_cookie (jar, cookie);
					// the following line is intentionally commented
					// WebKitGTK.soup_cookie_free (cookie);
					CookieResult = true;
				}
				WebKitGTK.soup_uri_free (uri);
			}
		};

		if (NativePendingCookies != null) {
			SetPendingCookies (NativePendingCookies);
			NativePendingCookies = null;
		}
	}

static Browser findBrowser (int /*long*/ webView) {
	if (webView == 0) return null;
	int /*long*/ parent = OS.gtk_widget_get_parent (webView);
	parent = OS.gtk_widget_get_parent (parent);
	return (Browser)Display.getCurrent ().findWidget (parent);
}

static boolean isInstalled () {
	if (!LibraryLoaded) return false;
	// TODO webkit_check_version() should take care of the following, but for some
	// reason this symbol is missing from the latest build.  If it is present in
	// Linux distro-provided builds then replace the following with this call.
	int major = WebKitGTK.webkit_major_version ();
	int minor = WebKitGTK.webkit_minor_version ();
	int micro = WebKitGTK.webkit_micro_version ();
	return major > MIN_VERSION[0] ||
		(major == MIN_VERSION[0] && minor > MIN_VERSION[1]) ||
		(major == MIN_VERSION[0] && minor == MIN_VERSION[1] && micro >= MIN_VERSION[2]);
}

static int /*long*/ JSObjectCallAsFunctionProc (int /*long*/ ctx, int /*long*/ function, int /*long*/ thisObject, int /*long*/ argumentCount, int /*long*/ arguments, int /*long*/ exception) {
	if (WebKitGTK.JSValueIsObjectOfClass (ctx, thisObject, ExternalClass) == 0) {
		return WebKitGTK.JSValueMakeUndefined (ctx);
	}
	int /*long*/ ptr = WebKitGTK.JSObjectGetPrivate (thisObject);
	int /*long*/[] handle = new int /*long*/[1];
	C.memmove (handle, ptr, C.PTR_SIZEOF);
	Browser browser = findBrowser (handle[0]);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	return webkit.callJava (ctx, function, thisObject, argumentCount, arguments, exception);
}

static int /*long*/ JSObjectGetPropertyProc (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName, int /*long*/ exception) {
	byte[] bytes = null;
	try {
		bytes = (FUNCTIONNAME_CALLJAVA + '\0').getBytes (CHARSET_UTF8); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = Converter.wcsToMbcs (null, FUNCTIONNAME_CALLJAVA, true);
	} 
	int /*long*/ name = WebKitGTK.JSStringCreateWithUTF8CString (bytes);
	int /*long*/ function = WebKitGTK.JSObjectMakeFunctionWithCallback (ctx, name, JSObjectCallAsFunctionProc.getAddress ());
	WebKitGTK.JSStringRelease (name);
	return function;
}

static int /*long*/ JSObjectHasPropertyProc (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName) {
	byte[] bytes = null;
	try {
		bytes = (FUNCTIONNAME_CALLJAVA + '\0').getBytes (CHARSET_UTF8); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = Converter.wcsToMbcs (null, FUNCTIONNAME_CALLJAVA, true);
	}
	return WebKitGTK.JSStringIsEqualToUTF8CString (propertyName, bytes);
}

static int /*long*/ Proc (int /*long*/ handle, int /*long*/ user_data) {
	Browser browser = findBrowser (handle);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	return webkit.webViewProc (handle, user_data);
}

static int /*long*/ Proc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ user_data) {
	if (OS.GTK_IS_SCROLLED_WINDOW (handle)) {
		/*
		 * Stop the propagation of events that are not consumed by WebKit, before
		 * they reach the parent embedder.  These events have already been received.
		 */
		return user_data;
	}

	int /*long*/ webView;
	if (WebKitGTK.WEBKIT_IS_WEB_FRAME (handle)) {
		webView = WebKitGTK.webkit_web_frame_get_web_view (handle);
	} else {
		webView = handle;
	}
	Browser browser = findBrowser (webView); 
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	if (webView == handle) {
		return webkit.webViewProc (handle, arg0, user_data);
	} else {
		return webkit.webFrameProc (handle, arg0, user_data);
	}
}

static int /*long*/ Proc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ user_data) {
	Browser browser = findBrowser (handle);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	return webkit.webViewProc (handle, arg0, arg1, user_data);
}

static int /*long*/ Proc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ user_data) {
	int /*long*/ webView;
	if (WebKitGTK.SOUP_IS_SESSION (handle)) {
		webView = user_data;
	} else {
		webView = handle;
	}
	Browser browser = findBrowser (webView);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	if (webView == handle) {
		return webkit.webViewProc (handle, arg0, arg1, arg2, user_data);
	} else {
		return webkit.sessionProc (handle, arg0, arg1, arg2, user_data);
	}
}

static int /*long*/ Proc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ user_data) {
	Browser browser = findBrowser (handle);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	return webkit.webViewProc (handle, arg0, arg1, arg2, arg3, user_data);
}

int /*long*/ sessionProc (int /*long*/ session, int /*long*/ msg, int /*long*/ auth, int /*long*/ retrying, int /*long*/ user_data) {
	/* authentication challenges are currently the only notification received from the session */
	if (retrying == 0) {
		failureCount = 0;
	} else {
		if (++failureCount >= 3) return 0;
	}

	int /*long*/ uri = WebKitGTK.soup_message_get_uri (msg);
	int /*long*/ uriString = WebKitGTK.soup_uri_to_string (uri, 0);
	int length = C.strlen (uriString);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, uriString, length);
	OS.g_free (uriString);
	String location = new String (MozillaDelegate.mbcsToWcs (null, bytes));

	for (int i = 0; i < authenticationListeners.length; i++) {
		AuthenticationEvent event = new AuthenticationEvent (browser);
		event.location = location;
		authenticationListeners[i].authenticate (event);
		if (!event.doit) {
			OS.g_signal_stop_emission_by_name (session, WebKitGTK.authenticate);
			return 0;
		}
		if (event.user != null && event.password != null) {
			byte[] userBytes = Converter.wcsToMbcs (null, event.user, true);
			byte[] passwordBytes = Converter.wcsToMbcs (null, event.password, true);
			WebKitGTK.soup_auth_authenticate (auth, userBytes, passwordBytes);
			OS.g_signal_stop_emission_by_name (session, WebKitGTK.authenticate);
			return 0;
		}
	}
	return 0;
}

int /*long*/ webFrameProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case NOTIFY_LOAD_STATUS: return webframe_notify_load_status (handle, arg0);
		default: return 0;
	}
}

int /*long*/ webViewProc (int /*long*/ handle, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case CLOSE_WEB_VIEW: return webkit_close_web_view (handle);
		case WEB_VIEW_READY: return webkit_web_view_ready (handle);
		default: return 0;
	}
}

int /*long*/ webViewProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case CREATE_WEB_VIEW: return webkit_create_web_view (handle, arg0);
		case DOWNLOAD_REQUESTED: return webkit_download_requested (handle, arg0);
		case NOTIFY_LOAD_STATUS: return webkit_notify_load_status (handle, arg0);
		case NOTIFY_PROGRESS: return webkit_notify_progress (handle, arg0);
		case NOTIFY_TITLE: return webkit_notify_title (handle, arg0);
		case POPULATE_POPUP: return webkit_populate_popup (handle, arg0);
		case STATUS_BAR_TEXT_CHANGED: return webkit_status_bar_text_changed (handle, arg0);
		default: return 0;
	}
}

int /*long*/ webViewProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case HOVERING_OVER_LINK: return webkit_hovering_over_link (handle, arg0, arg1);
		default: return 0;
	}
}

int /*long*/ webViewProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case CONSOLE_MESSAGE: return webkit_console_message (handle, arg0, arg1, arg2);
		case WINDOW_OBJECT_CLEARED: return webkit_window_object_cleared (handle, arg0, arg1, arg2);
		default: return 0;
	}
}

int /*long*/ webViewProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case MIME_TYPE_POLICY_DECISION_REQUESTED: return webkit_mime_type_policy_decision_requested (handle, arg0, arg1, arg2, arg3);
		case NAVIGATION_POLICY_DECISION_REQUESTED: return webkit_navigation_policy_decision_requested (handle, arg0, arg1, arg2, arg3);
		case RESOURCE_REQUEST_STARTING: return webkit_resource_request_starting (handle, arg0, arg1, arg2, arg3);
		default: return 0;
	}
}

public void create (Composite parent, int style) {
	if (ExternalClass == 0) {
		if (Device.DEBUG) {
			System.out.println("WebKit version " + major + "." + minor + "." + micro); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		JSClassDefinition jsClassDefinition = new JSClassDefinition ();
		byte[] bytes = Converter.wcsToMbcs (null, CLASSNAME_EXTERNAL, true);
		jsClassDefinition.className = C.malloc (bytes.length);
		OS.memmove (jsClassDefinition.className, bytes, bytes.length);
		jsClassDefinition.hasProperty = JSObjectHasPropertyProc.getAddress ();
		jsClassDefinition.getProperty = JSObjectGetPropertyProc.getAddress ();
		int /*long*/ classDefinitionPtr = C.malloc (JSClassDefinition.sizeof);
		WebKitGTK.memmove (classDefinitionPtr, jsClassDefinition, JSClassDefinition.sizeof);
		ExternalClass = WebKitGTK.JSClassCreate (classDefinitionPtr);

		bytes = Converter.wcsToMbcs (null, "POST", true); //$NON-NLS-1$
		PostString = C.malloc (bytes.length);
		C.memmove (PostString, bytes, bytes.length);
	}

    scrolledWindow = OS.gtk_scrolled_window_new (0, 0);
    OS.gtk_scrolled_window_set_policy (scrolledWindow, OS.GTK_POLICY_AUTOMATIC, OS.GTK_POLICY_AUTOMATIC);
    webView = WebKitGTK.webkit_web_view_new ();
    webViewData = C.malloc (C.PTR_SIZEOF);
    C.memmove (webViewData, new int /*long*/[] {webView}, C.PTR_SIZEOF);
    OS.gtk_container_add (scrolledWindow, webView);
    OS.gtk_container_add (browser.handle, scrolledWindow);
    OS.gtk_widget_show (scrolledWindow);
	OS.gtk_widget_show (webView);
	OS.g_signal_connect (webView, WebKitGTK.close_web_view, Proc2.getAddress (), CLOSE_WEB_VIEW);
	OS.g_signal_connect (webView, WebKitGTK.console_message, Proc5.getAddress (), CONSOLE_MESSAGE);
	OS.g_signal_connect (webView, WebKitGTK.create_web_view, Proc3.getAddress (), CREATE_WEB_VIEW);
	OS.g_signal_connect (webView, WebKitGTK.download_requested, Proc3.getAddress (), DOWNLOAD_REQUESTED);
	OS.g_signal_connect (webView, WebKitGTK.hovering_over_link, Proc4.getAddress (), HOVERING_OVER_LINK);
	OS.g_signal_connect (webView, WebKitGTK.mime_type_policy_decision_requested, Proc6.getAddress (), MIME_TYPE_POLICY_DECISION_REQUESTED);
	OS.g_signal_connect (webView, WebKitGTK.navigation_policy_decision_requested, Proc6.getAddress (), NAVIGATION_POLICY_DECISION_REQUESTED);
    OS.g_signal_connect (webView, WebKitGTK.notify_load_status, Proc3.getAddress (), NOTIFY_LOAD_STATUS);
    OS.g_signal_connect (webView, WebKitGTK.notify_progress, Proc3.getAddress (), NOTIFY_PROGRESS);
    OS.g_signal_connect (webView, WebKitGTK.notify_title, Proc3.getAddress (), NOTIFY_TITLE);
    OS.g_signal_connect (webView, WebKitGTK.populate_popup, Proc3.getAddress (), POPULATE_POPUP);
    OS.g_signal_connect (webView, WebKitGTK.resource_request_starting, Proc6.getAddress (), RESOURCE_REQUEST_STARTING);
    OS.g_signal_connect (webView, WebKitGTK.status_bar_text_changed, Proc3.getAddress (), STATUS_BAR_TEXT_CHANGED);
    OS.g_signal_connect (webView, WebKitGTK.web_view_ready, Proc2.getAddress (), WEB_VIEW_READY);
    OS.g_signal_connect (webView, WebKitGTK.window_object_cleared, Proc5.getAddress (), WINDOW_OBJECT_CLEARED);

	/* Callback to get events before WebKit receives and consumes them */
	OS.g_signal_connect (scrolledWindow, OS.event, Proc3.getAddress (), 0);
	/*
	* Callbacks to get the events not consumed by WebKit, and to block 
	* them so that they don't get propagated to the parent handle twice.  
	* This hook is set after WebKit and is therefore called after WebKit's 
	* handler because GTK dispatches events in their order of registration.
	*/
	OS.g_signal_connect (scrolledWindow, OS.button_press_event, Proc3.getAddress (), STOP_PROPOGATE);
	OS.g_signal_connect (scrolledWindow, OS.button_release_event, Proc3.getAddress (), STOP_PROPOGATE);
	OS.g_signal_connect (scrolledWindow, OS.key_press_event, Proc3.getAddress (), STOP_PROPOGATE);
	OS.g_signal_connect (scrolledWindow, OS.key_release_event, Proc3.getAddress (), STOP_PROPOGATE);
	OS.g_signal_connect (scrolledWindow, OS.scroll_event, Proc3.getAddress (), STOP_PROPOGATE);
	OS.g_signal_connect (scrolledWindow, OS.motion_notify_event, Proc3.getAddress (), STOP_PROPOGATE);

    int /*long*/ settings = WebKitGTK.webkit_web_view_get_settings (webView);
    OS.g_object_set (settings, WebKitGTK.javascript_can_open_windows_automatically, 1, 0);
    OS.g_object_set (settings, WebKitGTK.enable_universal_access_from_file_uris, 1, 0);

    Listener listener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.Dispose: {
					/* make this handler run after other dispose listeners */
					if (ignoreDispose) {
						ignoreDispose = false;
						break;
					}
					ignoreDispose = true;
					browser.notifyListeners (event.type, event);
					event.type = SWT.NONE;
					onDispose (event);
					break;
				}
				case SWT.FocusIn: {
					OS.gtk_widget_grab_focus (webView);
					break;
				}
				case SWT.Resize: {
					onResize (event);
					break;
				}
			}
		}
	};
	browser.addListener (SWT.Dispose, listener);
	browser.addListener (SWT.FocusIn, listener);
	browser.addListener (SWT.KeyDown, listener);
	browser.addListener (SWT.Resize, listener);

	/*
	* Ensure that our Authenticate listener is at the front of the signal
	* queue by removing the default Authenticate listener, adding ours,
	* and then re-adding the default listener.
	*/
	int /*long*/ session = WebKitGTK.webkit_get_default_session ();
	int /*long*/ originalAuth = WebKitGTK.soup_session_get_feature (session, WebKitGTK.webkit_soup_auth_dialog_get_type ());
	WebKitGTK.soup_session_feature_detach (originalAuth, session);
	OS.g_signal_connect (session, WebKitGTK.authenticate, Proc5.getAddress (), webView);
	WebKitGTK.soup_session_feature_attach (originalAuth, session);

	/*
	* Check for proxy values set as documented java properties and update the
	* session to use these values if needed.
	*/
	String proxyHost = System.getProperty (PROPERTY_PROXYHOST);
	String proxyPortString = System.getProperty (PROPERTY_PROXYPORT);
	int port = -1;
	if (proxyPortString != null) {
		try {
			int value = Integer.valueOf (proxyPortString).intValue ();
			if (0 <= value && value <= MAX_PORT) port = value;
		} catch (NumberFormatException e) {
			/* do nothing, java property has non-integer value */
		}
	}
	if (proxyHost != null || port != -1) {
		if (!proxyHost.startsWith (PROTOCOL_HTTP)) {
			proxyHost = PROTOCOL_HTTP + proxyHost;
		}
		proxyHost += ":" + port; //$NON-NLS-1$
		byte[] bytes = Converter.wcsToMbcs (null, proxyHost, true);
		int /*long*/ uri = WebKitGTK.soup_uri_new (bytes);
		if (uri != 0) {
			OS.g_object_set (session, WebKitGTK.SOUP_SESSION_PROXY_URI, uri, 0);
			WebKitGTK.soup_uri_free (uri);
		}
	}

	eventFunction = new BrowserFunction (browser, "HandleWebKitEvent") { //$NON-NLS-1$
		public Object function(Object[] arguments) {
			return handleEvent (arguments) ? Boolean.TRUE : Boolean.FALSE;
		};	
	};

	/*
	* Bug in WebKitGTK.  MouseOver/MouseLeave events are not consistently sent from
	* the DOM when the mouse enters and exits the browser control, see
	* https://bugs.webkit.org/show_bug.cgi?id=35246.  As a workaround for sending
	* MouseEnter/MouseExit events, swt's default mouse enter/exit mechanism is used,
	* but in order to do this the Browser's default sub-window check behavior must
	* be changed.
	*/
	browser.setData (KEY_CHECK_SUBWINDOW, Boolean.FALSE);
}

void addEventHandlers (boolean top) {
	if (top) {
		StringBuffer buffer = new StringBuffer ("window.SWTkeyhandler = function SWTkeyhandler(e) {"); //$NON-NLS-1$
		buffer.append ("try {e.returnValue = HandleWebKitEvent(e.type, e.keyCode, e.charCode, e.altKey, e.ctrlKey, e.shiftKey, e.metaKey);} catch (e) {}};"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('keydown', SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('keypress', SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('keyup', SWTkeyhandler, true);"); //$NON-NLS-1$
		execute (buffer.toString ());

		buffer = new StringBuffer ("window.SWTmousehandler = function SWTmousehandler(e) {"); //$NON-NLS-1$
		buffer.append ("try {e.returnValue = HandleWebKitEvent(e.type, e.screenX, e.screenY, e.detail, e.button + 1, e.altKey, e.ctrlKey, e.shiftKey, e.metaKey, e.relatedTarget != null);} catch (e) {}};"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mousedown', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mouseup', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mousemove', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('mousewheel', SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("document.addEventListener('dragstart', SWTmousehandler, true);"); //$NON-NLS-1$

		/*
		* The following two lines are intentionally commented because they cannot be used to
		* consistently send MouseEnter/MouseExit events until https://bugs.webkit.org/show_bug.cgi?id=35246
		* is fixed.
		*/ 
		//buffer.append ("document.addEventListener('mouseover', SWTmousehandler, true);"); //$NON-NLS-1$
		//buffer.append ("document.addEventListener('mouseout', SWTmousehandler, true);"); //$NON-NLS-1$

		execute (buffer.toString ());
	} else {
		StringBuffer buffer = new StringBuffer ("for (var i = 0; i < frames.length; i++) {"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('keydown', window.SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('keypress', window.SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('keyup', window.SWTkeyhandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mousedown', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mouseup', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mousemove', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mouseover', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mouseout', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('mousewheel', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ("frames[i].document.addEventListener('dragstart', window.SWTmousehandler, true);"); //$NON-NLS-1$
		buffer.append ('}');
		execute (buffer.toString ());
	}
}

public boolean back () {
	if (WebKitGTK.webkit_web_view_can_go_back (webView) == 0) return false;
	WebKitGTK.webkit_web_view_go_back (webView);
	return true;
}

public boolean close () {
	return close (true);
}

boolean close (boolean showPrompters) {
	if (!jsEnabled) return true;

	String message1 = Compatibility.getMessage("SWT_OnBeforeUnload_Message1"); // $NON-NLS-1$
	String message2 = Compatibility.getMessage("SWT_OnBeforeUnload_Message2"); // $NON-NLS-1$
	String functionName = EXECUTE_ID + "CLOSE"; // $NON-NLS-1$
	StringBuffer buffer = new StringBuffer ("function "); // $NON-NLS-1$
	buffer.append (functionName);
	buffer.append ("(win) {\n"); // $NON-NLS-1$
	buffer.append ("var fn = win.onbeforeunload; if (fn != null) {try {var str = fn(); "); // $NON-NLS-1$
	if (showPrompters) {
		buffer.append ("if (str != null) { "); // $NON-NLS-1$
		buffer.append ("var result = confirm('"); // $NON-NLS-1$
		buffer.append (message1);
		buffer.append ("\\n\\n'+str+'\\n\\n"); // $NON-NLS-1$
		buffer.append (message2);
		buffer.append ("');"); // $NON-NLS-1$
		buffer.append ("if (!result) return false;}"); // $NON-NLS-1$
	}	
	buffer.append ("} catch (e) {}}"); // $NON-NLS-1$
	buffer.append ("try {for (var i = 0; i < win.frames.length; i++) {var result = "); // $NON-NLS-1$
	buffer.append (functionName);
	buffer.append ("(win.frames[i]); if (!result) return false;}} catch (e) {} return true;"); // $NON-NLS-1$
	buffer.append ("\n};"); // $NON-NLS-1$
	execute (buffer.toString ());

	Boolean result = (Boolean)evaluate ("return " + functionName +"(window);"); // $NON-NLS-1$ // $NON-NLS-2$
	if (result == null) return false;
	return result.booleanValue ();
}

public boolean execute (String script) {
	int /*long*/ frame = WebKitGTK.webkit_web_view_get_main_frame (webView);
	int /*long*/ context = WebKitGTK.webkit_web_frame_get_global_context (frame);

	byte[] bytes = null;
	try {
		bytes = (script + '\0').getBytes (CHARSET_UTF8); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = Converter.wcsToMbcs (null, script, true);
	}
	int /*long*/ scriptString = WebKitGTK.JSStringCreateWithUTF8CString (bytes);

	try {
		bytes = (getUrl () + '\0').getBytes (CHARSET_UTF8); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = Converter.wcsToMbcs (null, getUrl (), true);
	}
	int /*long*/ urlString = WebKitGTK.JSStringCreateWithUTF8CString (bytes);

	int /*long*/ result = WebKitGTK.JSEvaluateScript (context, scriptString, 0, urlString, 0, null);
	WebKitGTK.JSStringRelease (urlString);
	WebKitGTK.JSStringRelease (scriptString);
	return result != 0;
}

public boolean forward () {
	if (WebKitGTK.webkit_web_view_can_go_forward (webView) == 0) return false;
	WebKitGTK.webkit_web_view_go_forward (webView);
	return true;
}

public String getBrowserType () {
	return "webkit"; //$NON-NLS-1$
}

public String getText () {
	int /*long*/ frame = WebKitGTK.webkit_web_view_get_main_frame (webView);
	int /*long*/ source = WebKitGTK.webkit_web_frame_get_data_source (frame);
	if (source == 0) return "";	//$NON-NLS-1$
	int /*long*/ data = WebKitGTK.webkit_web_data_source_get_data (source);
	if (data == 0) return "";	//$NON-NLS-1$

	int /*long*/ encoding = WebKitGTK.webkit_web_data_source_get_encoding (source);
	int length = OS.strlen (encoding);
	byte[] bytes = new byte [length];
	OS.memmove (bytes, encoding, length);
	String encodingString = new String (Converter.mbcsToWcs (null, bytes));

	int /*long*/ string = OS.GString_str (data);
	length = OS.GString_len (string);
	bytes = new byte[length];
	C.memmove (bytes, string, length);

	try {
		return new String (bytes, encodingString);
	} catch (UnsupportedEncodingException e) {
	}
	return new String (Converter.mbcsToWcs (null, bytes));
}

public String getUrl () {
	int /*long*/ uri = WebKitGTK.webkit_web_view_get_uri (webView);

	/* WebKit auto-navigates to about:blank at startup */
	if (uri == 0) return ABOUT_BLANK;

	int length = OS.strlen (uri);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, uri, length);

	String url = new String (Converter.mbcsToWcs (null, bytes));
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url.equals (URI_FILEROOT)) {
		url = ABOUT_BLANK;
	} else {
		length = URI_FILEROOT.length ();
		if (url.startsWith (URI_FILEROOT) && url.charAt (length) == '#') {
			url = ABOUT_BLANK + url.substring (length);
		}
	}
	return url;
}

boolean handleEvent (Object[] arguments) {

	/*
	* DOM events are currently received by hooking DOM listeners
	* in javascript that invoke this method via a BrowserFunction.
	* It should be possible to replace this mechanism with more
	* typical callbacks from C once WebKitGTK enhancement request
	* https://bugs.webkit.org/show_bug.cgi?id=33590 is completed.
	* In the meantime, the argument lists received here are:
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
			case SWT.ESC:
			case SWT.TAB:
			case SWT.PAUSE:
			case SWT.BS:
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
				final int stateMask = keyEvent.stateMask;
				if (!sendKeyEvent (keyEvent) || browser.isDisposed ()) return false;

				if (browser.isFocusControl ()) {
					if (keyCode == SWT.TAB && (stateMask & (SWT.CTRL | SWT.ALT)) == 0) {
						browser.getDisplay ().asyncExec (new Runnable () {
							public void run () {
								if (browser.isDisposed ()) return;
								if (browser.getDisplay ().getFocusControl () == null) {
									int traversal = (stateMask & SWT.SHIFT) != 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
									browser.traverse (traversal);
								}
							}
						});
					}
				}
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

	if (type.equals(DOMEVENT_KEYUP)) {
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

	/*
	* The following is intentionally commented because MouseOver and MouseOut events
	* are not being hooked until https://bugs.webkit.org/show_bug.cgi?id=35246 is fixed.
	*/ 
	//if (type.equals (DOMEVENT_MOUSEOVER) || type.equals (DOMEVENT_MOUSEOUT)) {
	//	if (((Boolean)arguments[9]).booleanValue ()) return true;
	//}

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
		(((Boolean)arguments[7]).booleanValue () ? SWT.SHIFT : 0) |
		(((Boolean)arguments[8]).booleanValue () ? SWT.COMMAND : 0);
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
	} else if (type.equals (DOMEVENT_MOUSEMOVE)) {
		mouseEvent.type = SWT.MouseMove;
	} else if (type.equals (DOMEVENT_MOUSEWHEEL)) {
		mouseEvent.type = SWT.MouseWheel;
		mouseEvent.count = ((Double)arguments[3]).intValue ();

	/*
	* The following is intentionally commented because MouseOver and MouseOut events
	* are not being hooked until https://bugs.webkit.org/show_bug.cgi?id=35246 is fixed.
	*/ 
	//} else if (type.equals (DOMEVENT_MOUSEOVER)) {
	//	mouseEvent.type = SWT.MouseEnter;
	//} else if (type.equals (DOMEVENT_MOUSEOUT)) {
	//	mouseEvent.type = SWT.MouseExit;

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
		browser.notifyListeners (mouseEvent.type, mouseEvent);
		/*
		* Bug in WebKitGTK.  Dragging an image quickly and repeatedly can cause
		* WebKitGTK to take the mouse grab indefinitely and lock up the display,
		* see https://bugs.webkit.org/show_bug.cgi?id=32840.  The workaround is
		* to veto all drag attempts.
		*/
		return false;
	}

	browser.notifyListeners (mouseEvent.type, mouseEvent);
	return true;
}

int /*long*/ handleLoadCommitted (int /*long*/ uri, boolean top) {
	int length = OS.strlen (uri);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, uri, length);
	String url = new String (Converter.mbcsToWcs (null, bytes));
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url.equals (URI_FILEROOT)) {
		url = ABOUT_BLANK;
	} else {
		length = URI_FILEROOT.length ();
		if (url.startsWith (URI_FILEROOT) && url.charAt (length) == '#') {
			url = ABOUT_BLANK + url.substring (length);
		}
	}

	/*
	* Each invocation of setText() causes webkit_notify_load_status to be invoked
	* twice, once for the initial navigate to about:blank, and once for the auto-navigate
	* to about:blank that WebKit does when webkit_web_view_load_string is invoked.  If
	* this is the first webkit_notify_load_status callback received for a setText()
	* invocation then do not send any events or re-install registered BrowserFunctions. 
	*/
	if (top && url.startsWith(ABOUT_BLANK) && htmlBytes != null) return 0;

	LocationEvent event = new LocationEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	event.location = url;
	event.top = top;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changed (event);
	}
	return 0;
}

int /*long*/ handleLoadFinished (int /*long*/ uri, boolean top) {
	int length = OS.strlen (uri);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, uri, length);
	String url = new String (Converter.mbcsToWcs (null, bytes));
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url.equals (URI_FILEROOT)) {
		url = ABOUT_BLANK;
	} else {
		length = URI_FILEROOT.length ();
		if (url.startsWith (URI_FILEROOT) && url.charAt (length) == '#') {
			url = ABOUT_BLANK + url.substring (length);
		}
	}

	/*
	 * If htmlBytes is not null then there is html from a previous setText() call
	 * waiting to be set into the about:blank page once it has completed loading. 
	 */
	if (top && htmlBytes != null) {
		if (url.startsWith(ABOUT_BLANK)) {
			loadingText = true;
			byte[] mimeType = Converter.wcsToMbcs (null, "text/html", true);  //$NON-NLS-1$
			byte[] encoding = Converter.wcsToMbcs (null, CHARSET_UTF8, true);  //$NON-NLS-1$
			byte[] uriBytes;
			if (untrustedText) {
				uriBytes = Converter.wcsToMbcs (null, ABOUT_BLANK, true);
			} else {
				uriBytes = Converter.wcsToMbcs (null, URI_FILEROOT, true);
			}
			WebKitGTK.webkit_web_view_load_string (webView, htmlBytes, mimeType, encoding, uriBytes);
			htmlBytes = null;
		}
	}

	/*
	* The webkit_web_view_load_string() invocation above will trigger a second
	* webkit_web_view_load_string callback when it is completed.  Wait for this
	* second callback to come before sending the title or completed events.
	*/
	if (!loadingText) {
		/*
		* To be consistent with other platforms a title event should be fired
		* when a top-level page has completed loading.  A page with a <title>
		* tag will do this automatically when the notify::title signal is received.
		* However a page without a <title> tag will not do this by default, so fire
		* the event here with the page's url as the title.
		*/
		if (top) {
			int /*long*/ frame = WebKitGTK.webkit_web_view_get_main_frame (webView);
			int /*long*/ title = WebKitGTK.webkit_web_frame_get_title (frame);
			if (title == 0) {
				TitleEvent newEvent = new TitleEvent (browser);
				newEvent.display = browser.getDisplay ();
				newEvent.widget = browser;
				newEvent.title = url;
				for (int i = 0; i < titleListeners.length; i++) {
					titleListeners[i].changed (newEvent);
				}
				if (browser.isDisposed ()) return 0;
			}
		}

		ProgressEvent progress = new ProgressEvent (browser);
		progress.display = browser.getDisplay ();
		progress.widget = browser;
		progress.current = MAX_PROGRESS;
		progress.total = MAX_PROGRESS;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].completed (progress);
		}
	}
	loadingText = false;

	return 0;
}

public boolean isBackEnabled () {
	return WebKitGTK.webkit_web_view_can_go_back (webView) != 0;
}

public boolean isForwardEnabled () {
	return WebKitGTK.webkit_web_view_can_go_forward (webView) != 0;
}

void onDispose (Event e) {
	/* Browser could have been disposed by one of the Dispose listeners */
	if (!browser.isDisposed()) {
		/* invoke onbeforeunload handlers */
		if (!browser.isClosing) {
			close (false);
		}
	}

	Enumeration elements = functions.elements ();
	while (elements.hasMoreElements ()) {
		((BrowserFunction)elements.nextElement ()).dispose (false);
	}
	functions = null;

	C.free (webViewData);
	postData = null;
	headers = null;
	htmlBytes = null;
}

void onResize (Event e) {
	Rectangle rect = browser.getClientArea ();
	OS.gtk_widget_set_size_request (scrolledWindow, rect.width, rect.height);
}

void openDownloadWindow (final int /*long*/ webkitDownload) {
	final Shell shell = new Shell ();
	String msg = Compatibility.getMessage ("SWT_FileDownload"); //$NON-NLS-1$
	shell.setText (msg);
	GridLayout gridLayout = new GridLayout ();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout (gridLayout);

	int /*long*/ name = WebKitGTK.webkit_download_get_suggested_filename (webkitDownload);
	int length = OS.strlen (name);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, name, length);
	String nameString = new String (Converter.mbcsToWcs (null, bytes));
	int /*long*/ url = WebKitGTK.webkit_download_get_uri (webkitDownload);
	length = OS.strlen (url);
	bytes = new byte[length];
	OS.memmove (bytes, url, length);
	String urlString = new String (Converter.mbcsToWcs (null, bytes));
	msg = Compatibility.getMessage ("SWT_Download_Location", new Object[] {nameString, urlString}); //$NON-NLS-1$
	Label nameLabel = new Label (shell, SWT.WRAP);
	nameLabel.setText (msg);
	GridData data = new GridData ();
	Monitor monitor = browser.getMonitor ();
	int maxWidth = monitor.getBounds ().width / 2;
	int width = nameLabel.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	data.widthHint = Math.min (width, maxWidth);
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	nameLabel.setLayoutData (data);

	final Label statusLabel = new Label (shell, SWT.NONE);
	statusLabel.setText (Compatibility.getMessage ("SWT_Download_Started")); //$NON-NLS-1$
	data = new GridData (GridData.FILL_BOTH);
	statusLabel.setLayoutData (data);

	final Button cancel = new Button (shell, SWT.PUSH);
	cancel.setText (Compatibility.getMessage ("SWT_Cancel")); //$NON-NLS-1$
	data = new GridData ();
	data.horizontalAlignment = GridData.CENTER;
	cancel.setLayoutData (data);
	final Listener cancelListener = new Listener () {
		public void handleEvent (Event event) {
			WebKitGTK.webkit_download_cancel (webkitDownload);
		}
	};
	cancel.addListener (SWT.Selection, cancelListener);

	OS.g_object_ref (webkitDownload);
	final Display display = browser.getDisplay ();
	final int INTERVAL = 500;
	display.timerExec (INTERVAL, new Runnable () {
		public void run () {
			int status = WebKitGTK.webkit_download_get_status (webkitDownload);
			if (shell.isDisposed () || status == WebKitGTK.WEBKIT_DOWNLOAD_STATUS_FINISHED || status == WebKitGTK.WEBKIT_DOWNLOAD_STATUS_CANCELLED) {
				shell.dispose ();
				display.timerExec (-1, this);
				OS.g_object_unref (webkitDownload);
				return;
			}
			if (status == WebKitGTK.WEBKIT_DOWNLOAD_STATUS_ERROR) {
				statusLabel.setText (Compatibility.getMessage ("SWT_Download_Error")); //$NON-NLS-1$
				display.timerExec (-1, this);
				OS.g_object_unref (webkitDownload);
				cancel.removeListener (SWT.Selection, cancelListener);
				cancel.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event event) {
						shell.dispose ();
					}
				});
				return;
			}

			long current = WebKitGTK.webkit_download_get_current_size (webkitDownload) / 1024L;
			long total = WebKitGTK.webkit_download_get_total_size (webkitDownload) / 1024L;
			String message = Compatibility.getMessage ("SWT_Download_Status", new Object[] {new Long(current), new Long(total)}); //$NON-NLS-1$
			statusLabel.setText (message);
			display.timerExec (INTERVAL, this);
		}
	});

	shell.pack ();
	shell.open ();
}

public void refresh () {
	WebKitGTK.webkit_web_view_reload (webView);
}

public boolean setText (String html, boolean trusted) {
	/* convert the String containing HTML to an array of bytes with UTF-8 data */
	byte[] bytes = null;
	try {
		bytes = (html + '\0').getBytes (CHARSET_UTF8); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = Converter.wcsToMbcs (null, html, true);
	}

	/*
	* If this.htmlBytes is not null then the about:blank page is already being loaded,
	* so no navigate is required.  Just set the html that is to be shown.
	*/
	boolean blankLoading = htmlBytes != null;
	htmlBytes = bytes;
	untrustedText = !trusted;
	if (blankLoading) return true;

	byte[] uriBytes = Converter.wcsToMbcs (null, ABOUT_BLANK, true);
	WebKitGTK.webkit_web_view_load_uri (webView, uriBytes);
	return true;
}

public boolean setUrl (String url, String postData, String[] headers) {
	this.postData = postData;
	this.headers = headers;

	/*
	* WebKitGTK attempts to open the exact url string that is passed to it and
	* will not infer a protocol if it's not specified.  Detect the case of an
	* invalid URL string and try to fix it by prepending an appropriate protocol.
	*/
	try {
		new URL(url);
	} catch (MalformedURLException e) {
		String testUrl = null;
		if (url.charAt (0) == SEPARATOR_FILE) {
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

	/*
	* Feature of WebKit.  The user-agent header value cannot be overridden
	* by changing it in the resource request.  The workaround is to detect
	* here whether the user-agent is being overridden, and if so, temporarily
	* set the value on the WebView when initiating the load request and then
	* remove it afterwards.
	*/
	int /*long*/ settings = WebKitGTK.webkit_web_view_get_settings (webView);
	if (headers != null) {
		for (int i = 0; i < headers.length; i++) {
			String current = headers[i];
			if (current != null) {
				int index = current.indexOf (':');
				if (index != -1) {
					String key = current.substring (0, index).trim ();
					String value = current.substring (index + 1).trim ();
					if (key.length () > 0 && value.length () > 0) {
						if (key.equalsIgnoreCase (USER_AGENT)) {
							byte[] bytes = Converter.wcsToMbcs (null, value, true);
							OS.g_object_set (settings, WebKitGTK.user_agent, bytes, 0);
						}
					}
				}
			}
		}
	}
	byte[] uriBytes = Converter.wcsToMbcs (null, url, true);
	WebKitGTK.webkit_web_view_load_uri (webView, uriBytes);
	OS.g_object_set (settings, WebKitGTK.user_agent, 0, 0);
	return true;
}

public void stop () {
	WebKitGTK.webkit_web_view_stop_loading (webView);
}

int /*long*/ webframe_notify_load_status (int /*long*/ web_frame, int /*long*/ pspec) {
	int status = WebKitGTK.webkit_web_frame_get_load_status (web_frame);
	switch (status) {
		case WebKitGTK.WEBKIT_LOAD_COMMITTED: {
			int /*long*/ uri = WebKitGTK.webkit_web_frame_get_uri (web_frame);
			return handleLoadCommitted (uri, false);
		}
		case WebKitGTK.WEBKIT_LOAD_FINISHED: {
			/*
			* If this frame navigation was isolated to this frame (eg.- a link was
			* clicked in the frame, as opposed to this frame being created in
			* response to navigating to a main document containing frames) then
			* treat this as a completed load.
			*/
			int /*long*/ parentFrame = WebKitGTK.webkit_web_frame_get_parent (web_frame);
			if (WebKitGTK.webkit_web_frame_get_load_status (parentFrame) == WebKitGTK.WEBKIT_LOAD_FINISHED) {
				int /*long*/ uri = WebKitGTK.webkit_web_frame_get_uri (web_frame);
				return handleLoadFinished (uri, false);
			}
		}
	}
	return 0;
}

int /*long*/ webkit_close_web_view (int /*long*/ web_view) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	for (int i = 0; i < closeWindowListeners.length; i++) {
		closeWindowListeners[i].close (newEvent);
	}
	browser.dispose ();
	return 0;
}

int /*long*/ webkit_console_message (int /*long*/ web_view, int /*long*/ message, int /*long*/ line, int /*long*/ source_id) {
	return 1;	/* stop the message from being written to stderr */
}

int /*long*/ webkit_create_web_view (int /*long*/ web_view, int /*long*/ frame) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	newEvent.required = true;
	if (openWindowListeners != null) {
		for (int i = 0; i < openWindowListeners.length; i++) {
			openWindowListeners[i].open (newEvent);
		}
	}
	Browser browser = null;
	if (newEvent.browser != null && newEvent.browser.webBrowser instanceof WebKit) {
		browser = newEvent.browser;
	}
	if (browser != null && !browser.isDisposed ()) {
		return ((WebKit)browser.webBrowser).webView;
	}
	return 0;
}

int /*long*/ webkit_download_requested (int /*long*/ web_view, int /*long*/ download) {
	int /*long*/ name = WebKitGTK.webkit_download_get_suggested_filename (download);
	int length = OS.strlen (name);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, name, length);
	String nameString = new String (Converter.mbcsToWcs (null, bytes));
	FileDialog dialog = new FileDialog (browser.getShell (), SWT.OPEN);
	dialog.setFileName (nameString);
	String title = Compatibility.getMessage ("SWT_FileDownload"); //$NON-NLS-1$
	dialog.setText (title);
	String path = dialog.open ();

	if (path != null) {
		path = URI_FILEROOT + path;
		byte[] uriBytes = Converter.wcsToMbcs (null, path, true);
		WebKitGTK.webkit_download_set_destination_uri (download, uriBytes);
		openDownloadWindow (download);
	} else {
		WebKitGTK.webkit_download_cancel (download);
	}
	return 1;
}

int /*long*/ webkit_hovering_over_link (int /*long*/ web_view, int /*long*/ title, int /*long*/ uri) {
	if (uri != 0) {
		int length = OS.strlen (uri);
		byte[] bytes = new byte[length];
		OS.memmove (bytes, uri, length);
		String text = new String (Converter.mbcsToWcs (null, bytes));
		StatusTextEvent event = new StatusTextEvent (browser);
		event.display = browser.getDisplay ();
		event.widget = browser;
		event.text = text;
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed (event);
		}
	}
	return 0;
}

int /*long*/ webkit_mime_type_policy_decision_requested (int /*long*/ web_view, int /*long*/ frame, int /*long*/ request, int /*long*/ mimetype, int /*long*/ policy_decision) {
	boolean canShow = WebKitGTK.webkit_web_view_can_show_mime_type (webView, mimetype) != 0;
	if (!canShow) {
		WebKitGTK.webkit_web_policy_decision_download (policy_decision);
		return 1;
	}
	return 0;
}

int /*long*/ webkit_navigation_policy_decision_requested (int /*long*/ web_view, int /*long*/ frame, int /*long*/ request, int /*long*/ navigation_action, int /*long*/ policy_decision) {
	if (loadingText) {
		/* 
		 * WebKit is auto-navigating to about:blank in response to a
		 * webkit_web_view_load_string() invocation.  This navigate
		 * should always proceed without sending an event since it is
		 * preceded by an explicit navigate to about:blank in setText().
		 */
		return 0;
	}

	int /*long*/ uri = WebKitGTK.webkit_network_request_get_uri (request);
	int length = OS.strlen (uri);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, uri, length);

	String url = new String (Converter.mbcsToWcs (null, bytes));
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url.equals (URI_FILEROOT)) {
		url = ABOUT_BLANK;
	} else {
		length = URI_FILEROOT.length ();
		if (url.startsWith (URI_FILEROOT) && url.charAt (length) == '#') {
			url = ABOUT_BLANK + url.substring (length);
		}
	}

	LocationEvent newEvent = new LocationEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	newEvent.location = url;
	newEvent.doit = true;
	if (locationListeners != null) {
		for (int i = 0; i < locationListeners.length; i++) {
			locationListeners[i].changing (newEvent);
		}
	}
	if (newEvent.doit && !browser.isDisposed ()) {
		if (jsEnabledChanged) {
			jsEnabledChanged = false;
			int /*long*/ settings = WebKitGTK.webkit_web_view_get_settings (webView);
			OS.g_object_set (settings, WebKitGTK.enable_scripts, jsEnabled ? 1 : 0, 0);
		}

		/* hook status change signal if frame is a newly-created sub-frame */
		int /*long*/ mainFrame = WebKitGTK.webkit_web_view_get_main_frame (webView);
		if (frame != mainFrame) {
			int id = OS.g_signal_handler_find (frame, OS.G_SIGNAL_MATCH_FUNC | OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, Proc3.getAddress (), NOTIFY_LOAD_STATUS);
			if (id == 0) {
				OS.g_signal_connect (frame, WebKitGTK.notify_load_status, Proc3.getAddress (), NOTIFY_LOAD_STATUS);
			}
		}

		/*
		* The following line is intentionally commented.  For some reason, invoking 
		* webkit_web_policy_decision_use(policy_decision) causes the Flash plug-in
		* to crash when navigating to a page with Flash.  Since returning from this
		* callback without invoking webkit_web_policy_decision_ignore(policy_decision)
		* implies that the page should be loaded, it's fine to not invoke
		* webkit_web_policy_decision_use(policy_decision) here. 
		*/
		//WebKitGTK.webkit_web_policy_decision_use (policy_decision);
	} else {
		WebKitGTK.webkit_web_policy_decision_ignore (policy_decision);
	}
	return 0;
}

int /*long*/ webkit_notify_load_status (int /*long*/ web_view, int /*long*/ pspec) {
	int status = WebKitGTK.webkit_web_view_get_load_status (webView);
	switch (status) {
		case WebKitGTK.WEBKIT_LOAD_COMMITTED: {
			int /*long*/ uri = WebKitGTK.webkit_web_view_get_uri (webView);
			return handleLoadCommitted (uri, true);
		}
		case WebKitGTK.WEBKIT_LOAD_FINISHED: {
			int /*long*/ uri = WebKitGTK.webkit_web_view_get_uri (webView);
			return handleLoadFinished (uri, true);
		}
	}
	return 0;
}

int /*long*/ webkit_notify_progress (int /*long*/ web_view, int /*long*/ pspec) {
	ProgressEvent event = new ProgressEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	event.current = (int)(WebKitGTK.webkit_web_view_get_progress (webView) * MAX_PROGRESS);
	event.total = MAX_PROGRESS;
	for (int i = 0; i < progressListeners.length; i++) {
		progressListeners[i].changed (event);				
	}
	return 0;
}

int /*long*/ webkit_notify_title (int /*long*/ web_view, int /*long*/ pspec) {
	int /*long*/ title = WebKitGTK.webkit_web_view_get_title (webView);
	String titleString;
	if (title == 0) {
		titleString = ""; //$NON-NLS-1$
	} else {
		int length = OS.strlen (title);
		byte[] bytes = new byte[length];
		OS.memmove (bytes, title, length);
		titleString = new String (Converter.mbcsToWcs (null, bytes));
	}
	TitleEvent event = new TitleEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	event.title = titleString;
	for (int i = 0; i < titleListeners.length; i++) {
		titleListeners[i].changed (event);
	}
	return 0;
}

int /*long*/ webkit_populate_popup (int /*long*/ web_view, int /*long*/ webkit_menu) {
	Point pt = browser.getDisplay ().getCursorLocation ();
	Event event = new Event ();
	event.x = pt.x;
	event.y = pt.y;
	browser.notifyListeners (SWT.MenuDetect, event);
	if (!event.doit) {
		/* clear the menu */
		int /*long*/ children = OS.gtk_container_get_children (webkit_menu);
		int /*long*/ current = children;
		while (current != 0) {
			int /*long*/ item = OS.g_list_data (current);
			OS.gtk_container_remove (webkit_menu, item);
			current = OS.g_list_next (current);
		}
		OS.g_list_free (children);
		return 0;
	}
	Menu menu = browser.getMenu ();
	if (menu != null && !menu.isDisposed ()) {
		if (pt.x != event.x || pt.y != event.y) {
			menu.setLocation (event.x, event.y);
		}
		menu.setVisible (true);
		/* clear the menu */
		int /*long*/ children = OS.gtk_container_get_children (webkit_menu);
		int /*long*/ current = children;
		while (current != 0) {
			int /*long*/ item = OS.g_list_data (current);
			OS.gtk_container_remove (webkit_menu, item);
			current = OS.g_list_next (current);
		}
		OS.g_list_free (children);
	}
	return 0;
}

int /*long*/ webkit_resource_request_starting (int /*long*/ web_view, int /*long*/ web_frame, int /*long*/ web_resource, int /*long*/ request, int /*long*/ response) {
	if (postData != null || headers != null) {
		int /*long*/ message = WebKitGTK.webkit_network_request_get_message (request);
		if (message == 0) {
			headers = null;
			postData = null;
		} else {
			if (postData != null) {
				WebKitGTK.SoupMessage_method (message, PostString);
				int /*long*/ body = WebKitGTK.SoupMessage_request_body (message);
				byte[] bytes = Converter.wcsToMbcs (null, postData, false);
				int /*long*/ data = C.malloc (bytes.length);
				C.memmove (data, bytes, bytes.length);
				WebKitGTK.soup_message_body_append (body, WebKitGTK.SOUP_MEMORY_TAKE, data, bytes.length);
				WebKitGTK.soup_message_body_flatten (body);

				if (headers == null) headers = new String[0];
				String[] temp = new String[headers.length + 1];
				System.arraycopy (headers, 0, temp, 0, headers.length);
				temp[headers.length] = ENCODING_FORM;
				headers = temp;
				postData = null;
			}

			/* headers */
			int /*long*/ requestHeaders = WebKitGTK.SoupMessage_request_headers (message);
			for (int i = 0; i < headers.length; i++) {
				String current = headers[i];
				if (current != null) {
					int index = current.indexOf (':');
					if (index != -1) {
						String key = current.substring (0, index).trim ();
						String value = current.substring (index + 1).trim ();
						if (key.length () > 0 && value.length () > 0) {
							byte[] nameBytes = Converter.wcsToMbcs (null, key, true);
							byte[] valueBytes = Converter.wcsToMbcs (null, value, true);
							WebKitGTK.soup_message_headers_append (requestHeaders, nameBytes, valueBytes);
						}
					}
				}
			}
			headers = null;
		}
	}

	return 0;
}

int /*long*/ webkit_status_bar_text_changed (int /*long*/ web_view, int /*long*/ text) {
	int length = OS.strlen (text);
	byte[] bytes = new byte[length];
	OS.memmove (bytes, text, length);
	StatusTextEvent statusText = new StatusTextEvent (browser);
	statusText.display = browser.getDisplay ();
	statusText.widget = browser;
	statusText.text = new String (Converter.mbcsToWcs (null, bytes));
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed (statusText);
	}
	return 0;
}

int /*long*/ webkit_web_view_ready (int /*long*/ web_view) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;

	int /*long*/ settings = WebKitGTK.webkit_web_view_get_window_features (webView);
	int[] result = new int[1];
	OS.g_object_get (settings, WebKitGTK.locationbar_visible, result, 0);
	newEvent.addressBar = result[0] != 0;
	result[0] = 0;
	OS.g_object_get (settings, WebKitGTK.menubar_visible, result, 0);
	newEvent.menuBar = result[0] != 0;
	result[0] = 0;
	OS.g_object_get (settings, WebKitGTK.statusbar_visible, result, 0);
	newEvent.statusBar = result[0] != 0;
	result[0] = 0;
	OS.g_object_get (settings, WebKitGTK.toolbar_visible, result, 0);
	newEvent.toolBar = result[0] != 0;
	result[0] = 0;
	OS.g_object_get (settings, WebKitGTK.x, result, 0);
	int x = result[0];
	result[0] = 0;
	OS.g_object_get (settings, WebKitGTK.y, result, 0);
	int y = result[0];
	result[0] = 0;
	OS.g_object_get (settings, WebKitGTK.width, result, 0);
	int width = result[0];
	result[0] = 0;
	OS.g_object_get (settings, WebKitGTK.height, result, 0);
	int height = result[0];
	result[0] = 0;
	if (x != -1 && y != -1) {
		newEvent.location = new Point (x,y);
	}
	if (width != -1 && height != -1) {
		newEvent.size = new Point (width,height);
	}
	for (int i = 0; i < visibilityWindowListeners.length; i++) {
		visibilityWindowListeners[i].show (newEvent);
	}
	return 0;
}

int /*long*/ webkit_window_object_cleared (int /*long*/ web_view, int /*long*/ frame, int /*long*/ context, int /*long*/ window_object) {
	int /*long*/ globalObject = WebKitGTK.JSContextGetGlobalObject (context);
	int /*long*/ externalObject = WebKitGTK.JSObjectMake (context, ExternalClass, webViewData);
	byte[] bytes = null;
	try {
		bytes = (OBJECTNAME_EXTERNAL + '\0').getBytes (CHARSET_UTF8);
	} catch (UnsupportedEncodingException e) {
		bytes = Converter.wcsToMbcs (null, OBJECTNAME_EXTERNAL, true);
	} 
	int /*long*/ name = WebKitGTK.JSStringCreateWithUTF8CString (bytes);
	WebKitGTK.JSObjectSetProperty (context, globalObject, name, externalObject, 0, null);
	WebKitGTK.JSStringRelease (name);
	Enumeration elements = functions.elements ();
	while (elements.hasMoreElements ()) {
		BrowserFunction current = (BrowserFunction)elements.nextElement ();
		execute (current.functionString);
	}
	int /*long*/ mainFrame = WebKitGTK.webkit_web_view_get_main_frame (webView);
	boolean top = mainFrame == frame;
	addEventHandlers (top);
	return 0;
}

int /*long*/ callJava (int /*long*/ ctx, int /*long*/ func, int /*long*/ thisObject, int /*long*/ argumentCount, int /*long*/ arguments, int /*long*/ exception) {
	Object returnValue = null;
	if (argumentCount == 2) {
		int /*long*/[] result = new int /*long*/[1];
		C.memmove (result, arguments, C.PTR_SIZEOF);
		int type = WebKitGTK.JSValueGetType (ctx, result[0]);
		if (type == WebKitGTK.kJSTypeNumber) {
			int index = ((Double)convertToJava (ctx, result[0])).intValue ();
			result[0] = 0;
			if (index > 0) {
				Object key = new Integer (index);
				BrowserFunction function = (BrowserFunction)functions.get (key);
				if (function != null) {
					try {
						C.memmove (result, arguments + C.PTR_SIZEOF, C.PTR_SIZEOF);
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
	return convertToJS (ctx, returnValue);
}

int /*long*/ convertToJS (int /*long*/ ctx, Object value) {
	if (value == null) {
		return WebKitGTK.JSValueMakeUndefined (ctx);
	}
	if (value instanceof String) {
		byte[] bytes = null;
		try {
			bytes = ((String)value + '\0').getBytes (CHARSET_UTF8); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			bytes = Converter.wcsToMbcs (null, (String)value, true);
		}
		int /*long*/ stringRef = WebKitGTK.JSStringCreateWithUTF8CString (bytes);
		int /*long*/ result = WebKitGTK.JSValueMakeString (ctx, stringRef);
		WebKitGTK.JSStringRelease (stringRef);
		return result;
	}
	if (value instanceof Boolean) {
		return WebKitGTK.JSValueMakeBoolean (ctx, ((Boolean)value).booleanValue () ? 1 : 0);
	}
	if (value instanceof Number) {
		return WebKitGTK.JSValueMakeNumber (ctx, ((Number)value).doubleValue ());
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
		return WebKitGTK.JSObjectMakeArray (ctx, length, arguments, null);
	}
	SWT.error (SWT.ERROR_INVALID_RETURN_VALUE);
	return 0;
}

Object convertToJava (int /*long*/ ctx, int /*long*/ value) {
	int type = WebKitGTK.JSValueGetType (ctx, value);
	switch (type) {
		case WebKitGTK.kJSTypeBoolean: {
			int result = WebKitGTK.JSValueToBoolean (ctx, value);
			return new Boolean (result != 0);
		}
		case WebKitGTK.kJSTypeNumber: {
			double result = WebKitGTK.JSValueToNumber (ctx, value, null);
			return new Double(result);
		}
		case WebKitGTK.kJSTypeString: {
			int /*long*/ string = WebKitGTK.JSValueToStringCopy (ctx, value, null);
			if (string == 0) return ""; //$NON-NLS-1$
			int /*long*/ length = WebKitGTK.JSStringGetMaximumUTF8CStringSize (string);
			byte[] bytes = new byte[(int)/*64*/length];
			length = WebKitGTK.JSStringGetUTF8CString (string, bytes, length);
			WebKitGTK.JSStringRelease (string);
			try {
				/* length-1 is needed below to exclude the terminator character */
				return new String (bytes, 0, (int)/*64*/length - 1, CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				return new String (Converter.mbcsToWcs (null, bytes));
			}
		}
		case WebKitGTK.kJSTypeNull:
			// FALL THROUGH
		case WebKitGTK.kJSTypeUndefined: return null;
		case WebKitGTK.kJSTypeObject: {
			byte[] bytes = null;
			try {
				bytes = (PROPERTY_LENGTH + '\0').getBytes (CHARSET_UTF8); //$NON-NLS-1$
			} catch (UnsupportedEncodingException e) {
				bytes = Converter.wcsToMbcs (null, PROPERTY_LENGTH, true);
			}
			int /*long*/ propertyName = WebKitGTK.JSStringCreateWithUTF8CString (bytes);
			int /*long*/ valuePtr = WebKitGTK.JSObjectGetProperty (ctx, value, propertyName, null);
			WebKitGTK.JSStringRelease (propertyName);
			type = WebKitGTK.JSValueGetType (ctx, valuePtr);
			if (type == WebKitGTK.kJSTypeNumber) {
				int length = (int)WebKitGTK.JSValueToNumber (ctx, valuePtr, null);
				Object[] result = new Object[length];
				for (int i = 0; i < length; i++) {
					int /*long*/ current = WebKitGTK.JSObjectGetPropertyAtIndex (ctx, value, i, null);
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

}

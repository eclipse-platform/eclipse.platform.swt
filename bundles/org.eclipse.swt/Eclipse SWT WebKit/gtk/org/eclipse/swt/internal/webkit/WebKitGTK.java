/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others. All rights reserved.
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
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

public class WebKitGTK extends C {

	public static boolean LibraryLoaded;

	/**
	 * Internal version of "Webkit glue code", used mainly during webkit2 port.
	 * Used to make it easier to support users on bugzilla. Updated by hand.
	 */
	public static final String swtWebkitGlueCodeVersion = " SWT Glue code version: 56.0";
	public static final String swtWebkitGlueCodeVersionInfo = " info: +BrowserFunction with private GDBus, +WebKitExtension Folder versioning,"
			+ " +WebKitExtension OSGI support, +setUrl(..postData..), setCookie(), getCookie +mouseDown/Focus +WebKit2 only";


	/**
	 * Temporary variable used during webkit2 port, to debug various problems for situations when it's time consuming to rebuild/debug.
	 * e.g debug issues in compiled eclipse builds, OSGI loading of the extension etc..
	 * Should be removed once webkit2 (and it's enhancements) are completed, no need to keep these msgs around.
	 */
	static {
		try {
			Library.loadLibrary ("swt-webkit"); // $NON-NLS-1$
			LibraryLoaded = true;
		} catch (Throwable e) {
		}

		String swt_lib_versions = OS.getEnvironmentalVariable (OS.SWT_LIB_VERSIONS); // Note, this is read in multiple places.
		if (swt_lib_versions != null && swt_lib_versions.equals("1")) {
			System.out.println("SWT_LIB  WebKit2   WebKitGTK:"+ webkit_get_major_version()+"."+ webkit_get_minor_version() + "."
					+ webkit_get_micro_version() + "  (WebKitGTK >=2.5 is WebKit2) " + swtWebkitGlueCodeVersion + swtWebkitGlueCodeVersionInfo);
		}
	};

	/** Constants */
	public static final int kJSTypeUndefined = 0;
	public static final int kJSTypeNull = 1;
	public static final int kJSTypeBoolean = 2;
	public static final int kJSTypeNumber = 3;
	public static final int kJSTypeString = 4;
	public static final int kJSTypeObject = 5;
	public static final int WEBKIT_DOWNLOAD_STATUS_ERROR = -1;
	public static final int WEBKIT_DOWNLOAD_STATUS_CANCELLED = 2;
	public static final int WEBKIT_DOWNLOAD_STATUS_FINISHED = 3;

	public static final int WEBKIT2_LOAD_STARTED = 0;
	public static final int WEBKIT2_LOAD_REDIRECTED = 1;
	public static final int WEBKIT2_LOAD_COMMITTED = 2;
	public static final int WEBKIT2_LOAD_FINISHED = 3;

	public static final int WEBKIT_POLICY_DECISION_TYPE_NAVIGATION_ACTION = 0;
	public static final int WEBKIT_POLICY_DECISION_TYPE_NEW_WINDOW_ACTION = 1;
	public static final int WEBKIT_POLICY_DECISION_TYPE_RESPONSE = 2;

	public static final int WEBKIT_CREDENTIAL_PERSISTENCE_NONE = 0;
	public static final int WEBKIT_CREDENTIAL_PERSISTENCE_FOR_SESSION = 1;
	public static final int WEBKIT_CREDENTIAL_PERSISTENCE_PERMANENT = 2;

	public static final int WEBKIT_TLS_ERRORS_POLICY_IGNORE = 0;

	public static final int G_TLS_CERTIFICATE_UNKNOWN_CA = 0;
	public static final int G_TLS_CERTIFICATE_BAD_IDENTITY = 1;
	public static final int G_TLS_CERTIFICATE_NOT_ACTIVATED = 2;
	public static final int G_TLS_CERTIFICATE_EXPIRED = 3;
	public static final int G_TLS_CERTIFICATE_REVOKED = 4;
	public static final int G_TLS_CERTIFICATE_INSECURE = 5;
	public static final int G_TLS_CERTIFICATE_GENERIC_ERROR = 6;
	public static final int G_TLS_CERTIFICATE_VALIDATE_ALL = 7;

	public static final int WEBKIT_WEBSITE_DATA_COOKIES = 1 << 8;


	/** Signals */

	// Authentication.
	public static final byte[] authenticate = ascii ("authenticate"); // $NON-NLS-1$

	// TLS load failure signal
	public static final byte[] load_failed_with_tls_errors = ascii ("load-failed-with-tls-errors"); // $NON-NLS-1$

	// Close webview
	public static final byte[] close = ascii ("close"); // $NON-NLS-1$

	// Context menu signals.
	public static final byte[] context_menu = ascii ("context-menu"); // $NON-NLS-1$

	// Create webView
	public static final byte[] create = ascii ("create"); // $NON-NLS-1$

	// Policy decision signals.
	public static final byte[] decide_policy = ascii ("decide-policy"); // $NON-NLS-1$
	public static final byte[] decide_destination = ascii ("decide-destination"); // $NON-NLS-1$

	// Download signal
	public static final byte[] download_started = ascii ("download-started"); // $NON-NLS-1$
	public static final byte[] failed = ascii ("failed"); // $NON-NLS-1$
	public static final byte[] finished = ascii ("finished"); // $NON-NLS-1$

	// Webkit2 extension
	public static final byte[] initialize_web_extensions = ascii ("initialize-web-extensions");

	// Status text signals
	public static final byte[] mouse_target_changed = ascii ("mouse-target-changed"); // $NON-NLS-1$

	// Load changed/page reload.
	public static final byte[] load_changed = ascii ("load-changed"); // $NON-NLS-1$

	// Load progress/estimation/notification mechanism.
	public static final byte[] notify_estimated_load_progress = ascii ("notify::estimated-load-progress"); // $NON-NLS-1$

	// Notify that the webpage title has changed.
	public static final byte[] notify_title = ascii ("notify::title"); // $NON-NLS-1$

	// Signal to indicate when the view should be shown to user. I.e, page load is complete.
	public static final byte[] ready_to_show = ascii ("ready-to-show"); // $NON-NLS-1$

	/** Properties: */
	// Webkit2: https://webkitgtk.org/reference/webkit2gtk/unstable/WebKitSettings.html#WebKitSettings.properties
	//
	// Developer Note:
	// - Webkit2 documentation doesn't explicitly say if g_object_(set|get) is safe to use, but
	//   I've confirmed with webkitgtk+ developers on IRC (freenode#webkitgtk+ <mcatanzaro>) that it is in fact still
	//   safe to use g_object_(set|get) for updating properties.
	//   Note:
	//    - On webkit2 you can also use the newly introduced functions for getting/setting settings as well as g_object_set().
	public static final byte[] default_charset = ascii ("default-charset"); // $NON-NLS-1$

	public static final byte[] enable_javascript = ascii ("enable-javascript"); // $NON-NLS-1$
	public static final byte[] enable_developer_extras = ascii ("enable-developer-extras");

	public static final byte[] enable_webgl = ascii("enable-webgl"); // $NON-NLS-1$

	public static final byte[] enable_back_forward_navigation_gestures = ascii("enable-back-forward-navigation-gestures"); // $NON-NLS-1$

	// Since 2.14
	public static final byte[] allow_universal_access_from_file_urls = ascii ("allow-universal-access-from-file-urls"); // $NON-NLS-1$

	public static final byte[] user_agent = ascii ("user-agent"); // $NON-NLS-1$

	public static final byte[] javascript_can_open_windows_automatically = ascii ("javascript-can-open-windows-automatically");

	public static final byte[] locationbar_visible = ascii ("locationbar-visible"); // $NON-NLS-1$
	public static final byte[] menubar_visible = ascii ("menubar-visible"); // $NON-NLS-1$
	public static final byte[] statusbar_visible = ascii ("statusbar-visible"); // $NON-NLS-1$
	public static final byte[] toolbar_visible = ascii ("toolbar-visible"); // $NON-NLS-1$

	protected static byte [] ascii (String name) {
	int length = name.length ();
	char [] chars = new char [length];
	name.getChars (0, length, chars, 0);
	byte [] buffer = new byte [length + 1];
	for (int i=0; i<length; i++) {
		buffer [i] = (byte) chars [i];
	}
	return buffer;
}

/** @method flags=dynamic */
public static final native long _JSObjectGetProperty (long ctx, long object, long propertyName, long [] exception);
public static final long JSObjectGetProperty (long ctx, long object, long propertyName, long [] exception) {
	lock.lock();
	try {
		return _JSObjectGetProperty (ctx, object, propertyName, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSObjectGetPropertyAtIndex (long ctx, long object, int propertyIndex, long [] exception);
public static final long JSObjectGetPropertyAtIndex (long ctx, long object, int propertyIndex, long [] exception) {
	lock.lock();
	try {
		return _JSObjectGetPropertyAtIndex (ctx, object, propertyIndex, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSObjectMakeArray (long ctx, long argumentCount, long [] arguments, long [] exception);
public static final long JSObjectMakeArray (long ctx, long argumentCount, long [] arguments, long [] exception) {
	lock.lock();
	try {
		return _JSObjectMakeArray (ctx, argumentCount, arguments, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSStringCreateWithUTF8CString (byte[] string);
public static final long JSStringCreateWithUTF8CString (byte[] string) {
	lock.lock();
	try {
		return _JSStringCreateWithUTF8CString (string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSStringGetMaximumUTF8CStringSize (long string);
public static final long JSStringGetMaximumUTF8CStringSize (long string) {
	lock.lock();
	try {
		return _JSStringGetMaximumUTF8CStringSize (string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSStringGetUTF8CString (long string, byte[] buffer, long bufferSize);
public static final long JSStringGetUTF8CString (long string, byte[] buffer, long bufferSize) {
	lock.lock();
	try {
		return _JSStringGetUTF8CString (string, buffer, bufferSize);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _JSStringRelease (long string);
public static final void JSStringRelease (long string) {
	lock.lock();
	try {
		_JSStringRelease (string);
	} finally {
		lock.unlock();
	}
}

// Signature: 	   void webkit_javascript_result_unref (WebKitJavascriptResult *js_result);
// Type Note:      WebKitJavascriptResult -> gpointer -> jintLong
/** @method flags=dynamic */
public static final native void _webkit_javascript_result_unref(long js_result);
public static final void webkit_javascript_result_unref(long js_result) {
	lock.lock();
	try {
		_webkit_javascript_result_unref (js_result);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _JSValueGetType (long ctx, long value);
public static final int JSValueGetType (long ctx, long value) {
	lock.lock();
	try {
		return _JSValueGetType (ctx, value);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSValueMakeBoolean (long ctx, int bool);
public static final long JSValueMakeBoolean (long ctx, int bool) {
	lock.lock();
	try {
		return _JSValueMakeBoolean (ctx, bool);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSValueMakeNumber (long ctx, double number);
public static final long JSValueMakeNumber (long ctx, double number) {
	lock.lock();
	try {
		return _JSValueMakeNumber (ctx, number);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSValueMakeString (long ctx, long string);
public static final long JSValueMakeString (long ctx, long string) {
	lock.lock();
	try {
		return _JSValueMakeString (ctx, string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSValueMakeUndefined (long ctx);
public static final long JSValueMakeUndefined (long ctx) {
	lock.lock();
	try {
		return _JSValueMakeUndefined (ctx);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native double _JSValueToNumber (long ctx, long value, long [] exception);
public static final double JSValueToNumber (long ctx, long value, long [] exception) {
	lock.lock();
	try {
		return _JSValueToNumber (ctx, value, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSValueToStringCopy (long ctx, long value, long [] exception);
public static final long JSValueToStringCopy (long ctx, long value, long [] exception) {
	lock.lock();
	try {
		return _JSValueToStringCopy (ctx, value, exception);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start libsoup natives --------------------- */

/** @method flags=dynamic */
public static final native long _soup_cookie_get_name (long cookie);
public static final long soup_cookie_get_name (long cookie) {
	lock.lock();
	try {
		return _soup_cookie_get_name (cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_cookie_get_value (long cookie);
public static final long soup_cookie_get_value (long cookie) {
	lock.lock();
	try {
		return _soup_cookie_get_value (cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_cookie_parse (byte[] header, long origin);
public static final long soup_cookie_parse (byte[] header, long origin) {
	lock.lock();
	try {
		return _soup_cookie_parse (header, origin);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_message_headers_append (long headers, byte[] name, byte[] value);
public static final void soup_message_headers_append (long headers, byte[] name, byte[] value) {
	lock.lock();
	try {
		_soup_message_headers_append (headers, name, value);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_uri_free (long uri);
public static final void soup_uri_free (long uri) {
	lock.lock();
	try {
		_soup_uri_free (uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_uri_new (byte[] uri_string);
public static final long soup_uri_new (byte[] uri_string) {
	lock.lock();
	try {
		return _soup_uri_new (uri_string);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start WebKitGTK natives --------------------- */

/** @method flags=dynamic */
public static final native void _webkit_authentication_request_authenticate (long request, long credential);
public static final void webkit_authentication_request_authenticate (long request, long credential) {
	lock.lock();
	try {
		_webkit_authentication_request_authenticate (request, credential);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_authentication_request_cancel (long request);
public static final void webkit_authentication_request_cancel (long request) {
	lock.lock();
	try {
		_webkit_authentication_request_cancel (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native boolean _webkit_authentication_request_is_retry (long request);
public static final boolean webkit_authentication_request_is_retry (long request) {
	lock.lock();
	try {
		return _webkit_authentication_request_is_retry (request);
	} finally {
		lock.unlock();
	}
}

/**
 * @method flags=dynamic
 */
public static final native void _webkit_cookie_manager_add_cookie (long cookie_manager, long cookie, long cancellable, long cb, long user_data);
public static final void webkit_cookie_manager_add_cookie (long cookie_manager, long cookie, long cancellable, long cb, long user_data) {
	lock.lock();
	try {
		_webkit_cookie_manager_add_cookie (cookie_manager, cookie, cancellable, cb, user_data);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native boolean _webkit_cookie_manager_add_cookie_finish (long cookie_manager, long result, long error []);
public static final boolean webkit_cookie_manager_add_cookie_finish (long cookie_manager, long result, long error []) {
	lock.lock();
	try {
		return _webkit_cookie_manager_add_cookie_finish (cookie_manager, result, error);
	} finally {
		lock.unlock();
	}
}

/**
 * @method flags=dynamic
 */
public static final native void _webkit_cookie_manager_get_cookies (long cookie_manager, byte [] uri, long cancellable, long cb, long user_data);
public static final void webkit_cookie_manager_get_cookies (long cookie_manager, byte [] uri, long cancellable, long cb, long user_data) {
	lock.lock();
	try {
		_webkit_cookie_manager_get_cookies (cookie_manager, uri, cancellable, cb, user_data);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_cookie_manager_get_cookies_finish (long cookie_manager, long result, long error []);
public static final long webkit_cookie_manager_get_cookies_finish (long cookie_manager, long result, long error []) {
	lock.lock();
	try {
		return _webkit_cookie_manager_get_cookies_finish (cookie_manager, result, error);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_credential_free (long credential);
public static final void webkit_credential_free (long credential) {
	lock.lock();
	try {
		_webkit_credential_free (credential);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_context_allow_tls_certificate_for_host(long webKitWebContext, long GTlsCertificate, byte[] constGCharHost);
public static final long webkit_web_context_allow_tls_certificate_for_host(long webKitWebContext, long GTlsCertificate, byte[] constGCharHost) {
	// since 2.6
	lock.lock();
	try {
		return _webkit_web_context_allow_tls_certificate_for_host(webKitWebContext, GTlsCertificate, constGCharHost);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_context_get_type ();
public static final long webkit_web_context_get_type () {
	lock.lock();
	try {
		return _webkit_web_context_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_credential_new (byte[] username, byte[] password, int persistence);
public static final long webkit_credential_new (byte[] username, byte[] password, int persistence) {
	lock.lock();
	try {
		return _webkit_credential_new (username, password, persistence);
	} finally {
		lock.unlock();
	}
}


/** @method flags=dynamic */
public static final native int _webkit_dom_event_target_add_event_listener (long target, byte[] name, long handler, int bubble, long userData);
public static final int webkit_dom_event_target_add_event_listener (long target, byte[] name, long handler, int bubble, long userData) {
	lock.lock();
	try {
		return _webkit_dom_event_target_add_event_listener (target, name, handler, bubble, userData);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_alt_key (long self);
public static final int webkit_dom_mouse_event_get_alt_key (long self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_alt_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native short _webkit_dom_mouse_event_get_button (long self);
public static final short webkit_dom_mouse_event_get_button (long self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_button (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_ctrl_key (long self);
public static final int webkit_dom_mouse_event_get_ctrl_key (long self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_ctrl_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_meta_key (long self);
public static final int webkit_dom_mouse_event_get_meta_key (long self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_meta_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_mouse_event_get_screen_x (long self);
public static final long webkit_dom_mouse_event_get_screen_x (long self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_screen_x (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_mouse_event_get_screen_y (long self);
public static final long webkit_dom_mouse_event_get_screen_y (long self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_screen_y (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_shift_key (long self);
public static final int webkit_dom_mouse_event_get_shift_key (long self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_shift_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_ui_event_get_char_code (long self);
public static final long webkit_dom_ui_event_get_char_code (long self) {
	lock.lock();
	try {
		return _webkit_dom_ui_event_get_char_code (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_ui_event_get_detail (long self);
public static final long webkit_dom_ui_event_get_detail (long self) {
	lock.lock();
	try {
		return _webkit_dom_ui_event_get_detail (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_ui_event_get_key_code (long self);
public static final long webkit_dom_ui_event_get_key_code (long self) {
	lock.lock();
	try {
		return _webkit_dom_ui_event_get_key_code (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_cancel (long download);
public static final void webkit_download_cancel (long download) {
	lock.lock();
	try {
		_webkit_download_cancel (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_received_data_length (long download);
public static final long webkit_download_get_received_data_length (long download) {
	lock.lock();
	try {
		return _webkit_download_get_received_data_length (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_request (long download);
public static final long webkit_download_get_request (long download) {
	lock.lock();
	try {
		return _webkit_download_get_request (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_response (long download);
public static final long webkit_download_get_response (long download) {
	lock.lock();
	try {
		return _webkit_download_get_response (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_type ();
public static final long webkit_download_get_type () {
	lock.lock();
	try {
		return _webkit_download_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_uri_response_get_content_length (long response);
public static final long webkit_uri_response_get_content_length (long response) {
	lock.lock();
	try {
		return _webkit_uri_response_get_content_length (response);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_web_view (long download);
public static final long webkit_download_get_web_view (long download) {
	lock.lock();
	try {
		return _webkit_download_get_web_view (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_set_allow_overwrite (long download, boolean allowed);
public static final void webkit_download_set_allow_overwrite (long download, boolean allowed) {
	lock.lock();
	try {
		_webkit_download_set_allow_overwrite (download, allowed);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_set_destination (long download, byte[] destination_uri);
public static final void webkit_download_set_destination (long download, byte[] destination_uri) {
	lock.lock();
	try {
		_webkit_download_set_destination (download, destination_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native boolean _webkit_hit_test_result_context_is_link (long hit_test_result);
public static final boolean webkit_hit_test_result_context_is_link (long hit_test_result) {
	lock.lock();
	try {
		return _webkit_hit_test_result_context_is_link (hit_test_result);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_hit_test_result_get_link_uri (long hit_test_result);
public static final long webkit_hit_test_result_get_link_uri (long hit_test_result) {
	lock.lock();
	try {
		return _webkit_hit_test_result_get_link_uri (hit_test_result);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_hit_test_result_get_link_title (long hit_test_result);
public static final long webkit_hit_test_result_get_link_title (long hit_test_result) {
	lock.lock();
	try {
		return _webkit_hit_test_result_get_link_title (hit_test_result);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_get_major_version ();
public static final int webkit_get_major_version () {
	lock.lock();
	try {
		return _webkit_get_major_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_get_micro_version ();
public static final int webkit_get_micro_version () {
	lock.lock();
	try {
		return _webkit_get_micro_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_get_minor_version ();
public static final int webkit_get_minor_version () {
	lock.lock();
	try {
		return _webkit_get_minor_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_navigation_policy_decision_get_request (long decision);
public static final long webkit_navigation_policy_decision_get_request (long decision) {
	lock.lock();
	try {
		return _webkit_navigation_policy_decision_get_request (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_policy_decision_download (long decision);
public static final void webkit_policy_decision_download (long decision) {
	lock.lock();
	try {
		_webkit_policy_decision_download (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_policy_decision_ignore (long decision);
public static final void webkit_policy_decision_ignore (long decision) {
	lock.lock();
	try {
		_webkit_policy_decision_ignore (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_context_get_default ();
public static final long webkit_web_context_get_default () {
	lock.lock();
	try {
		return _webkit_web_context_get_default ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_context_get_cookie_manager (long context);
public static final long webkit_web_context_get_cookie_manager (long context) {
	lock.lock();
	try {
		return _webkit_web_context_get_cookie_manager (context);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_context_get_website_data_manager (long context);
public static final long webkit_web_context_get_website_data_manager (long context) {
lock.lock();
try {
	return _webkit_web_context_get_website_data_manager (context);
} finally {
	lock.unlock();
}
}

/** @method flags=dynamic */
public static final native void _webkit_web_context_set_tls_errors_policy(long context, int policy);
public static final void webkit_web_context_set_tls_errors_policy (long context, int policy) {
	lock.lock();
	try {
		_webkit_web_context_set_tls_errors_policy (context, policy);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_can_go_back (long web_view);
public static final int webkit_web_view_can_go_back (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_can_go_back (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_main_resource (long web_view);
public static final long webkit_web_view_get_main_resource (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_main_resource (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_context (long web_view);
public static final long webkit_web_view_get_context (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_context (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_can_go_forward (long web_view);
public static final int webkit_web_view_can_go_forward (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_can_go_forward (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_can_show_mime_type (long web_view, long mime_type);
public static final int webkit_web_view_can_show_mime_type (long web_view, long mime_type) {
	lock.lock();
	try {
		return _webkit_web_view_can_show_mime_type (web_view, mime_type);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native double _webkit_web_view_get_estimated_load_progress (long web_view);
public static final double webkit_web_view_get_estimated_load_progress (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_estimated_load_progress (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_page_id (long web_view);
public static final long webkit_web_view_get_page_id (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_page_id (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_settings (long web_view);
public static final long webkit_web_view_get_settings (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_settings (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_title (long web_view);
public static final long webkit_web_view_get_title (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_title (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_uri (long web_view);
public static final long webkit_web_view_get_uri (long web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_uri (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_window_properties (long webView);
/** WebKitWindowProperties * webkit_web_view_get_window_properties (WebKitWebView *web_view); */
public static final long webkit_web_view_get_window_properties (long webView) {
	lock.lock();
	try {
		return _webkit_web_view_get_window_properties (webView);
	} finally {
		lock.unlock();
	}
}

/**
 * @method flags=dynamic
 * @param rectangle cast=(GdkRectangle *),flags=no_in
 */
public static final native void _webkit_window_properties_get_geometry (long webKitWindowProperties, GdkRectangle rectangle);
public static final void webkit_window_properties_get_geometry (long webKitWindowProperties, GdkRectangle rectangle ) {
	lock.lock();
	try {
		_webkit_window_properties_get_geometry (webKitWindowProperties, rectangle);
	} finally {
		lock.unlock();
	}
}



/** @method flags=dynamic */
public static final native void _webkit_web_view_go_back (long web_view);
public static final void webkit_web_view_go_back (long web_view) {
	lock.lock();
	try {
		_webkit_web_view_go_back (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_go_forward (long web_view);
public static final void webkit_web_view_go_forward (long web_view) {
	lock.lock();
	try {
		_webkit_web_view_go_forward (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_html (long web_view, byte[] content, byte[] base_uri);
public static final void webkit_web_view_load_html (long web_view, byte[] content, byte[] base_uri) {
	lock.lock();
	try {
		_webkit_web_view_load_html (web_view, content, base_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_bytes (long web_view, long bytes, byte [] mime_type, byte [] encoding, byte [] base_uri);
public static final void webkit_web_view_load_bytes (long web_view, long bytes, byte [] mime_type, byte [] encoding, byte [] base_uri) {
	lock.lock();
	try {
		_webkit_web_view_load_bytes (web_view, bytes, mime_type, encoding, base_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_request (long web_view, long request);
public static final void webkit_web_view_load_request (long web_view, long request) {
	lock.lock();
	try {
		_webkit_web_view_load_request (web_view, request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_uri (long web_view, byte[] uri);
public static final void webkit_web_view_load_uri (long web_view, byte[] uri) {
	lock.lock();
	try {
		_webkit_web_view_load_uri (web_view, uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_new ();
public static final long webkit_web_view_new () {
	lock.lock();
	try {
		return _webkit_web_view_new ();
	} finally {
		lock.unlock();
	}
}


/** @method flags=dynamic */ // @param context cast=(WebKitWebContext*)  @param directory cast=(const gchar *)
public static final native void _webkit_web_context_set_web_extensions_directory (long context, byte[] directory);
public static final void webkit_web_context_set_web_extensions_directory (long context, byte[] directory) {
	lock.lock();
	try {
		_webkit_web_context_set_web_extensions_directory (context, directory);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_context_set_web_extensions_initialization_user_data(long /* int */ context, long /* int */ user_data);
public static final void webkit_web_context_set_web_extensions_initialization_user_data(long /* int */ context,
		long /* int */ user_data) {
	lock.lock();
	try {
		_webkit_web_context_set_web_extensions_initialization_user_data(context, user_data);
	} finally {
		lock.unlock();
	}
}


/**
 * @method flags=dynamic
 * @param js_result cast=(gpointer)
 */
public static final native long _webkit_javascript_result_get_global_context(long js_result);
/** JSGlobalContextRef webkit_javascript_result_get_global_context (WebKitJavascriptResult *js_result);  */
public static final long webkit_javascript_result_get_global_context(long js_result) {
	lock.lock();
	try {
		return _webkit_javascript_result_get_global_context (js_result);
	} finally {
		lock.unlock();
	}
}

/**
 * @method flags=dynamic
 * @param js_result cast=(gpointer)
 */
public static final native long _webkit_javascript_result_get_value(long js_result);
/** JSValueRef webkit_javascript_result_get_value (WebKitJavascriptResult *js_result); */
public static final long webkit_javascript_result_get_value(long js_result) {
	lock.lock();
	try {
		return _webkit_javascript_result_get_value (js_result);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_reload (long web_view);
public static final void webkit_web_view_reload (long web_view) {
	lock.lock();
	try {
		_webkit_web_view_reload (web_view);
	} finally {
		lock.unlock();
	}
}


/** @method flags=dynamic */
public static final native void _webkit_web_view_run_javascript (long web_view, byte [] script, long cancellable, long  callback, long user_data);
/** 			    void webkit_web_view_run_javascript (WebKitWebView *web_view, const gchar *script, GCancellable *cancellable, GAsyncReadyCallback callback, gpointer user_data); **/
public static final void webkit_web_view_run_javascript (long web_view, byte[] script, long cancellable, long  callback, long user_data) {
	lock.lock();
	try {
		_webkit_web_view_run_javascript (web_view, script, cancellable, callback, user_data);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_resource_get_data (long webKitWebResource, long gCancellable, long GAsyncReadyCallback, long user_data);
public static final void webkit_web_resource_get_data (long webKitWebResource, long gCancellable, long GAsyncReadyCallback, long user_data) {
	lock.lock();
	try {
		_webkit_web_resource_get_data (webKitWebResource, gCancellable, GAsyncReadyCallback, user_data);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_resource_get_data_finish(long WebKitWebResource, long GAsyncResult, long [] gsize, long GError[]);
public static final long webkit_web_resource_get_data_finish(long WebKitWebResource, long GAsyncResult, long [] gsize, long GError[]) {
	lock.lock();
	try {
		return _webkit_web_resource_get_data_finish(WebKitWebResource, GAsyncResult, gsize, GError);
	} finally {
		lock.unlock();
	}
}


/**
 * @method flags=dynamic
 * @param gerror cast=(GError **)
 */
public static final native long _webkit_web_view_run_javascript_finish(long web_view, long GAsyncResult, long [] gerror);
/**WebKitJavascriptResult * webkit_web_view_run_javascript_finish (WebKitWebView *web_view, GAsyncResult *result, GError **error);*/
public static long webkit_web_view_run_javascript_finish(long web_view, long GAsyncResult, long [] gerror) {
	lock.lock();
	try {
		return _webkit_web_view_run_javascript_finish (web_view, GAsyncResult, gerror);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_stop_loading (long web_view);
public static final void webkit_web_view_stop_loading (long web_view) {
	lock.lock();
	try {
		_webkit_web_view_stop_loading (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_website_data_manager_clear (long manager, long types, long timespan, long cancellable, long callback, long user_data);
public static final void webkit_website_data_manager_clear (long manager, long types, long timespan, long cancellable, long callback, long user_data) {
	lock.lock();
	try {
		_webkit_website_data_manager_clear (manager, types, timespan, cancellable, callback, user_data);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_response_policy_decision_get_request (long decision);
public static final long  webkit_response_policy_decision_get_request (long decision) { // never called
	lock.lock();
	try {
		return _webkit_response_policy_decision_get_request (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_response_policy_decision_get_response (long decision);
public static final long  webkit_response_policy_decision_get_response (long decision) {
	lock.lock();
	try {
		return _webkit_response_policy_decision_get_response (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_uri_request_new (byte[] uri);
public static final long  webkit_uri_request_new (byte[] uri) {
	lock.lock();
	try {
		return _webkit_uri_request_new (uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_uri_request_get_http_headers (long request);
public static final long  webkit_uri_request_get_http_headers (long request) {
	lock.lock();
	try {
		return _webkit_uri_request_get_http_headers (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_uri_request_get_uri (long request);
public static final long  webkit_uri_request_get_uri (long request) {
	lock.lock();
	try {
		return _webkit_uri_request_get_uri (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_uri_response_get_mime_type (long responce);
public static final long  webkit_uri_response_get_mime_type (long response) {
	lock.lock();
	try {
		return _webkit_uri_response_get_mime_type (response);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start SWT natives --------------------- */
public static final native int GdkRectangle_sizeof();

}

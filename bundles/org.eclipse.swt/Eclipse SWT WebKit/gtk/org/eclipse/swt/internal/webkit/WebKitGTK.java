/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others. All rights reserved.
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
	public static boolean WEBKIT1, WEBKIT2;
	public static final String Webkit1AssertMsg = "Webkit2 code reached by webkit1"; // $NON-NLS-1$
	public static final String Webkit2AssertMsg = "Webkit1 code reached by webkit2"; // $NON-NLS-1$

	/**
	 * Internal version of "Webkit glue code", used mainly during webkit2 port.
	 * Used to make it easier to support users on bugzilla. Updated by hand.
	 */
	public static final String swtWebkitGlueCodeVersion = " SWT Glue code version: 54.0";
	public static final String swtWebkitGlueCodeVersionInfo = " info: +BrowserFunction/GDBus, +WebkitExtension Folder versioning, +WebKitExtension OSGI support, +setUrl(..postData..), -setCookie(), -getCookie +mouseDown/Focus";


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

		if (LibraryLoaded) {
			String webkit2 = System.getenv("SWT_WEBKIT2"); // $NON-NLS-1$
			int webkit2VersionFunction = webkit_get_major_version();
			if (webkit2VersionFunction != 0) { // SWT_WEBKIT2 env variable is not set but webkit2 was loaded as fallback
				webkit2 = "1";
			}
			WEBKIT2 = webkit2 != null && webkit2.equals("1"); // $NON-NLS-1$
			WEBKIT1 = !WEBKIT2;
		}

		String swt_lib_versions = OS.getEnvironmentalVariable (OS.SWT_LIB_VERSIONS); // Note, this is read in multiple places.
		if (swt_lib_versions != null && swt_lib_versions.equals("1")) {
			if (WEBKIT1) {
				System.out.println("SWT_LIB  Webkit1   Webkitgtk:"+ webkit_major_version() +"."+ webkit_minor_version() + "." + webkit_micro_version() + "  (webkitgtk < 2.5 is Webkit1)");
			}
			if (WEBKIT2) {
				System.out.println("SWT_LIB  Webkit2   Webkitgtk:"+ webkit_get_major_version()+"."+ webkit_get_minor_version() + "."
						+ webkit_get_micro_version() + "  (webkitgtk >=2.5 is Webkit2) " + swtWebkitGlueCodeVersion + swtWebkitGlueCodeVersionInfo);
			}
		}
	};

	/** Constants */
	public static final int kJSTypeUndefined = 0;
	public static final int kJSTypeNull = 1;
	public static final int kJSTypeBoolean = 2;
	public static final int kJSTypeNumber = 3;
	public static final int kJSTypeString = 4;
	public static final int kJSTypeObject = 5;
	public static final int SOUP_MEMORY_TAKE = 1;
	public static final int WEBKIT_DOWNLOAD_STATUS_ERROR = -1;
	public static final int WEBKIT_DOWNLOAD_STATUS_CANCELLED = 2;
	public static final int WEBKIT_DOWNLOAD_STATUS_FINISHED = 3;
	public static final int WEBKIT_LOAD_COMMITTED = 1;
	public static final int WEBKIT_LOAD_FINISHED = 2;

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

	public static final int WEBKIT_WEBSITE_DATA_COOKIES = 1 << 8; // Webkit2


	/** Signals */

	// Authentication.
	public static final byte[] authenticate = ascii ("authenticate"); // $NON-NLS-1$		// Webkit1 & Webkit2

	// TLS load failure signal
	// Webkit2 only
	public static final byte[] load_failed_with_tls_errors = ascii ("load-failed-with-tls-errors"); // $NON-NLS-1$

	// Close webview
	public static final byte[] close_web_view = ascii ("close-web-view"); // $NON-NLS-1$   // Webkit1
	public static final byte[] close = ascii ("close"); // $NON-NLS-1$					   // Webkit2

	// Supress javascript execution warnings from bleeding into SWT's console.
	public static final byte[] console_message = ascii ("console-message"); // $NON-NLS-1$ // Webkit1. (On W2 see 'console-message-sent'). Not printed to stderr by the looks.

	// Context menu signals.
	public static final byte[] populate_popup = ascii ("populate-popup"); // $NON-NLS-1$   // Webkit1, deprecated in 1.10.
	public static final byte[] context_menu = ascii ("context-menu"); // $NON-NLS-1$       // Webkit2.

	// Create webView
	public static final byte[] create_web_view = ascii ("create-web-view"); // $NON-NLS-1$ // Webkit1
	public static final byte[] create = ascii ("create"); // $NON-NLS-1$				   // Webkit2

	// Policy decision signals.
	public static final byte[] mime_type_policy_decision_requested = ascii ("mime-type-policy-decision-requested"); // $NON-NLS-1$   // Webkit1
	public static final byte[] navigation_policy_decision_requested = ascii ("navigation-policy-decision-requested"); // $NON-NLS-1$ // Webkit1
	public static final byte[] decide_policy = ascii ("decide-policy"); // $NON-NLS-1$		// Webkit2
	public static final byte[] decide_destination = ascii ("decide-destination"); // $NON-NLS-1$	// Webkit2

	// Download signal
	public static final byte[] download_requested = ascii ("download-requested"); // $NON-NLS-1$	// Webkit1
	public static final byte[] download_started = ascii ("download-started"); // $NON-NLS-1$		// Webkit2  (has 3 signals for downloading)
	public static final byte[] failed = ascii ("failed"); // $NON-NLS-1$							// Webkit2
	public static final byte[] finished = ascii ("finished"); // $NON-NLS-1$						// Webkit2

	// Webkit2 extension
	public static final byte[] initialize_web_extensions = ascii ("initialize-web-extensions");         // Webkit2. Extension exists only on w2. Since 2.4

	// Status text signals
	public static final byte[] hovering_over_link = ascii ("hovering-over-link"); // $NON-NLS-1$   		// Webkit1 -> StatusTextListener.changed()
	public static final byte[] mouse_target_changed = ascii ("mouse-target-changed"); // $NON-NLS-1$	// Webkit2 -> StatusTextListener.changed()
	/*  Webkit1 only.
	 *  On webkit2 & newer browsers 'window.status=txt' has no effect anymore.
	 *  Status bar only updated when you hover mouse over hyperlink. See signals above.*/
	public static final byte[] status_bar_text_changed = ascii ("status-bar-text-changed"); // $NON-NLS-1$    // Webkit1. Doesn't exist on W2 due to security risk.

	// Load changed/page reload.
	public static final byte[] window_object_cleared = ascii ("window-object-cleared"); // $NON-NLS-1$  // Webkit1. On W2 this is found in the webextension. On w2, 'load-changed' is used.
	public static final byte[] load_changed = ascii ("load-changed"); // $NON-NLS-1$ // Webkit2 only, to implement equivalent of webkit1 window_object_cleared

	// Load progress/estimation/notification mechanism.
	public static final byte[] notify_load_status = ascii ("notify::load-status"); // $NON-NLS-1$                           // Webkit1
	public static final byte[] notify_progress = ascii ("notify::progress"); // $NON-NLS-1$									// ->Webkit1 Progress.changed()
	public static final byte[] notify_estimated_load_progress = ascii ("notify::estimated-load-progress"); // $NON-NLS-1$   // ->Webkit2 Progress.changed()

	// Notify that the webpage title has changed.
	public static final byte[] notify_title = ascii ("notify::title"); // $NON-NLS-1$	// Webkit1, Webkit2.

	// Intercept a page load request to inject postData and custom headers.
	public static final byte[] resource_request_starting = ascii ("resource-request-starting"); // $NON-NLS-1$ // Webkit1.
	public static final byte[] resource_load_started = ascii ("resource-load-started"); // $NON-NLS-1$         // Webkit1. (unused, left over?)
	// api for this doesn't exist in Webkitgtk (2.18). Bug 527738.


	// Signal to indicate when the view should be shown to user. I.e, page load is complete.
	public static final byte[] web_view_ready = ascii ("web-view-ready"); // $NON-NLS-1$	// Webkit1
	public static final byte[] ready_to_show = ascii ("ready-to-show"); // $NON-NLS-1$		// Webkit2



	/** Properties: */
	// Webkit1: https://webkitgtk.org/reference/webkitgtk/unstable/WebKitWebSettings.html#WebKitWebSettings.properties
	// Webkit2: https://webkitgtk.org/reference/webkit2gtk/unstable/WebKitSettings.html#WebKitSettings.properties
	//
	// Developer Note:
	// - Webkit1 documentation suggested to use g_object_(set|get) to modify properties.
	// - Webkit2 documentation doesn't explicitly say if g_object_(set|get) is safe to use, but
	//   I've confirmed with webkitgtk+ developers on IRC (freenode#webkitgtk+ <mcatanzaro>) that it is in fact still
	//   safe to use g_object_(set|get) for updating properties.
	//   Note:
	//    - Some settings in Webkit2 have changed. It's not safe to use webkit1 settings on webkit2.
	//    - On webkit2 you can also use the newly introduced functions for getting/setting settings as well as g_object_set().
	public static final byte[] default_encoding = ascii ("default-encoding"); // $NON-NLS-1$	// Webkit1 only
	public static final byte[] default_charset = ascii ("default-charset"); // $NON-NLS-1$	    // Webkit2 only

	public static final byte[] enable_scripts = ascii ("enable-scripts"); // $NON-NLS-1$		// Webkit1 only.
	public static final byte[] enable_javascript = ascii ("enable-javascript"); // $NON-NLS-1$	// Webkit2 only

	public static final byte[] enable_webgl = ascii("enable-webgl"); // $NON-NLS-1$				// Webkit1 & Webkit2

	public static final byte[] enable_universal_access_from_file_uris = ascii ("enable-universal-access-from-file-uris"); // $NON-NLS-1$  // Webkit1
	public static final byte[] allow_universal_access_from_file_urls = ascii ("allow-universal-access-from-file-urls"); // $NON-NLS-1$    // Webkit2 Since 2.14

	public static final byte[] user_agent = ascii ("user-agent"); // $NON-NLS-1$				// Webkit1 & Webkit2

	public static final byte[] javascript_can_open_windows_automatically = ascii ("javascript-can-open-windows-automatically"); // $NON-NLS-1$	// Webkit1 & Webit2

	public static final byte[] locationbar_visible = ascii ("locationbar-visible"); // $NON-NLS-1$		// Webkit1 (Settings) & Webkit2 (Properties)
	public static final byte[] menubar_visible = ascii ("menubar-visible"); // $NON-NLS-1$				// Webkit1 (Settings) & Webkit2 (Properties)
	public static final byte[] statusbar_visible = ascii ("statusbar-visible"); // $NON-NLS-1$			// Webkit1 (Settings) & Webkit2 (Properties)
	public static final byte[] toolbar_visible = ascii ("toolbar-visible"); // $NON-NLS-1$				// Webkit1 (Settings) & Webkit2 (Properties)

	// Webki1 only (Settings). (In Webkit2 height/width/x/y are stored in "geometry" of 'Properties')
	public static final byte[] height = ascii ("height"); // $NON-NLS-1$	// Webkit1 only
	public static final byte[] width = ascii ("width"); // $NON-NLS-1$		// Wekbit1 only
	public static final byte[] x = ascii ("x"); // $NON-NLS-1$				// Webkit1 only
	public static final byte[] y = ascii ("y"); // $NON-NLS-1$				// Webkit1 only

	public static final byte[] SOUP_SESSION_PROXY_URI = ascii ("proxy-uri"); // $NON-NLS-1$		// libsoup

	/** DOM events */
	public static final byte[] dragstart = ascii ("dragstart"); // $NON-NLS-1$		// Webkit1
	public static final byte[] keydown = ascii ("keydown"); // $NON-NLS-1$			// Webkit1
	public static final byte[] keypress = ascii ("keypress"); // $NON-NLS-1$        // Webkit1
	public static final byte[] keyup = ascii ("keyup"); // $NON-NLS-1$              // Webkit1
	public static final byte[] mousedown = ascii ("mousedown"); // $NON-NLS-1$      // Webkit1
	public static final byte[] mousemove = ascii ("mousemove"); // $NON-NLS-1$      // Webkit1
	public static final byte[] mouseup = ascii ("mouseup"); // $NON-NLS-1$          // Webkit1
	public static final byte[] mousewheel = ascii ("mousewheel"); // $NON-NLS-1$    // Webkit1


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
public static final native long _JSClassCreate (long definition);
public static final long JSClassCreate (long definition) {
	lock.lock();
	try {
		return _JSClassCreate (definition);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSContextGetGlobalObject (long ctx);
public static final long JSContextGetGlobalObject (long ctx) {
	lock.lock();
	try {
		return _JSContextGetGlobalObject (ctx);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSEvaluateScript (long ctx, long script, long thisObject, long sourceURL, int startingLineNumber, long [] exception);
public static final long JSEvaluateScript (long ctx, long script, long thisObject, long sourceURL, int startingLineNumber, long [] exception) {
	lock.lock();
	try {
		return _JSEvaluateScript (ctx, script, thisObject, sourceURL, startingLineNumber, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _JSObjectGetPrivate (long object);
public static final long JSObjectGetPrivate (long object) {
	lock.lock();
	try {
		return _JSObjectGetPrivate (object);
	} finally {
		lock.unlock();
	}
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
public static final native long _JSObjectMake (long ctx, long jsClass, long data);
public static final long JSObjectMake (long ctx, long jsClass, long data) {
	lock.lock();
	try {
		return _JSObjectMake (ctx, jsClass, data);
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
public static final native long _JSObjectMakeFunctionWithCallback (long ctx, long name, long callAsFunction);
public static final long JSObjectMakeFunctionWithCallback (long ctx, long name, long callAsFunction) {
	lock.lock();
	try {
		return _JSObjectMakeFunctionWithCallback (ctx, name, callAsFunction);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _JSObjectSetProperty (long ctx, long object, long propertyName, long value, int attributes, long [] exception);
public static final void JSObjectSetProperty (long ctx, long object, long propertyName, long value, int attributes, long [] exception) {
	lock.lock();
	try {
		_JSObjectSetProperty (ctx, object, propertyName, value, attributes, exception);
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
public static final native long _JSStringGetLength (long string);
public static final long JSStringGetLength (long string) {
	lock.lock();
	try {
		return _JSStringGetLength (string);
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
public static final native int _JSStringIsEqualToUTF8CString (long a, byte[] b);
public static final int JSStringIsEqualToUTF8CString (long a, byte[] b) {
	lock.lock();
	try {
		return _JSStringIsEqualToUTF8CString (a, b);
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
public static final native int _JSValueIsObjectOfClass (long ctx, long value, long jsClass);
public static final int JSValueIsObjectOfClass (long ctx, long value, long jsClass) {
	lock.lock();
	try {
		return _JSValueIsObjectOfClass (ctx, value, jsClass);
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
public static final native void _soup_auth_authenticate (long auth, byte[] username, byte[] password);
public static final void soup_auth_authenticate (long auth, byte[] username, byte[] password) {
	lock.lock();
	try {
		_soup_auth_authenticate (auth, username, password);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_auth_get_host (long auth);
public static final long soup_auth_get_host (long auth) {
	lock.lock();
	try {
		return _soup_auth_get_host (auth);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_auth_get_scheme_name (long auth);
public static final long soup_auth_get_scheme_name (long auth) {
	lock.lock();
	try {
		return _soup_auth_get_scheme_name (auth);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_cookie_free (long cookie);
public static final void soup_cookie_free (long cookie) {
	lock.lock();
	try {
		_soup_cookie_free (cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_cookie_jar_add_cookie (long jar, long cookie);
public static final void soup_cookie_jar_add_cookie (long jar, long cookie) {
	lock.lock();
	try {
		_soup_cookie_jar_add_cookie (jar, cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_cookie_jar_all_cookies (long jar);
public static final long soup_cookie_jar_all_cookies (long jar) {
	lock.lock();
	try {
		return _soup_cookie_jar_all_cookies (jar);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_cookie_jar_delete_cookie (long jar, long cookie);
public static final void soup_cookie_jar_delete_cookie (long jar, long cookie) {
	lock.lock();
	try {
		_soup_cookie_jar_delete_cookie (jar, cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_cookie_jar_get_cookies (long jar, long uri, int for_http);
public static final long soup_cookie_jar_get_cookies (long jar, long uri, int for_http) {
	lock.lock();
	try {
		return _soup_cookie_jar_get_cookies (jar, uri, for_http);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_cookie_jar_get_type ();
public static final long soup_cookie_jar_get_type () {
	lock.lock();
	try {
		return _soup_cookie_jar_get_type ();
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
public static final native void _soup_message_body_append (long body, int use, long data, long length);
public static final void soup_message_body_append (long body, int use, long data, long length) {
	lock.lock();
	try {
		_soup_message_body_append (body, use, data, length);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_message_body_flatten (long body);
public static final void soup_message_body_flatten (long body) {
	lock.lock();
	try {
		_soup_message_body_flatten (body);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_message_get_uri (long msg);
public static final long soup_message_get_uri (long msg) {
	lock.lock();
	try {
		return _soup_message_get_uri (msg);
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
public static final native void _soup_session_add_feature_by_type (long session, long type);
public static final void soup_session_add_feature_by_type (long session, long type) {
	lock.lock();
	try {
		_soup_session_add_feature_by_type (session, type);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_session_get_feature (long session, long feature_type);
public static final long soup_session_get_feature (long session, long feature_type) {
	lock.lock();
	try {
		return _soup_session_get_feature (session, feature_type);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_session_feature_attach (long feature, long session);
public static final void soup_session_feature_attach (long feature, long session) {
	lock.lock();
	try {
		_soup_session_feature_attach (feature, session);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _soup_session_get_type ();
public static final long soup_session_get_type () {
	lock.lock();
	try {
		return _soup_session_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_session_feature_detach (long feature, long session);
public static final void soup_session_feature_detach (long feature, long session) {
	lock.lock();
	try {
		_soup_session_feature_detach (feature, session);
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

/** @method flags=dynamic */
public static final native long _soup_uri_to_string (long uri, int just_path_and_query);
public static final long soup_uri_to_string (long uri, int just_path_and_query) {
	lock.lock();
	try {
		return _soup_uri_to_string (uri, just_path_and_query);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start WebKitGTK natives --------------------- */

/** @method flags=dynamic */
public static final native void _webkit_authentication_request_authenticate (long request, long credential);
public static final void webkit_authentication_request_authenticate (long request, long credential) {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_authentication_request_is_retry (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_credential_free (long credential);
public static final void webkit_credential_free (long credential) {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT1 || WEBKIT2;
	lock.lock();
	try {
		_webkit_download_cancel (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_current_size (long download);
public static final long webkit_download_get_current_size (long download) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_current_size (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_received_data_length (long download);
public static final long webkit_download_get_received_data_length (long download) {
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_received_data_length (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_network_request (long download);
public static final long webkit_download_get_network_request (long download) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_network_request (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_download_get_status (long download);
public static final int webkit_download_get_status (long download) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_status (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_suggested_filename (long download);
public static final long webkit_download_get_suggested_filename (long download) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_suggested_filename (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_request (long download);
public static final long webkit_download_get_request (long download) {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_response (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_total_size (long download);
public static final long webkit_download_get_total_size (long download) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_total_size (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_type ();
public static final long webkit_download_get_type () {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_uri_response_get_content_length (response);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_uri (long download);
public static final long webkit_download_get_uri (long download) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_uri (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_web_view (long download);
public static final long webkit_download_get_web_view (long download) {
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_download_get_web_view (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_new (long request);
public static final long webkit_download_new (long request) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_download_new (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_set_allow_overwrite (long download, boolean allowed);
public static final void webkit_download_set_allow_overwrite (long download, boolean allowed) {
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		_webkit_download_set_allow_overwrite (download, allowed);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_set_destination_uri (long download, byte[] destination_uri);
public static final void webkit_download_set_destination_uri (long download, byte[] destination_uri) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		_webkit_download_set_destination_uri (download, destination_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_set_destination (long download, byte[] destination_uri);
public static final void webkit_download_set_destination (long download, byte[] destination_uri) {
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		_webkit_download_set_destination (download, destination_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_start (long download);
public static final void webkit_download_start (long download) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		_webkit_download_start (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_favicon_database_set_path (long database, long path);
public static final void webkit_favicon_database_set_path (long database, long path) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		_webkit_favicon_database_set_path (database, path);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_get_default_session ();
public static final long webkit_get_default_session () {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_get_default_session ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_get_favicon_database ();
public static final long webkit_get_favicon_database () {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_get_favicon_database ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native boolean _webkit_hit_test_result_context_is_link (long hit_test_result);
public static final boolean webkit_hit_test_result_context_is_link (long hit_test_result) {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_hit_test_result_get_link_title (hit_test_result);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_major_version ();
public static final int webkit_major_version () {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_major_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_get_major_version ();
public static final int webkit_get_major_version () {
//	assert WEBKIT2;  //Corner case, this function is called in order to determine WEBKIT2 flag. Can't use in assert.
	lock.lock();
	try {
		return _webkit_get_major_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_micro_version ();
public static final int webkit_micro_version () {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_micro_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_get_micro_version ();
public static final int webkit_get_micro_version () {
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_get_micro_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_minor_version ();
public static final int webkit_minor_version () {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_minor_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_get_minor_version ();
public static final int webkit_get_minor_version () {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_navigation_policy_decision_get_request (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_network_request_get_message (long request);
public static final long webkit_network_request_get_message (long request) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_network_request_get_message (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_network_request_get_uri (long request);
public static final long webkit_network_request_get_uri (long request) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_network_request_get_uri (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_network_request_new (byte[] uri);
public static final long webkit_network_request_new (byte[] uri) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_network_request_new (uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_policy_decision_download (long decision);
public static final void webkit_policy_decision_download (long decision) {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		_webkit_policy_decision_ignore (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_soup_auth_dialog_get_type ();
public static final long webkit_soup_auth_dialog_get_type () {
	// Can't find reference for this. Currently used only by webkit1 thou, probably webkit1-only.
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_soup_auth_dialog_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_context_get_default ();
public static final long webkit_web_context_get_default () {
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_web_context_get_default ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_context_get_website_data_manager (long context);
public static final long webkit_web_context_get_website_data_manager (long context) {
	assert WEBKIT2 : Webkit2AssertMsg; // Since 2.10
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		_webkit_web_context_set_tls_errors_policy (context, policy);
	} finally {
		lock.unlock();
	}
}


/** @method flags=dynamic */
public static final native long _webkit_web_data_source_get_data (long data_source);
public static final long webkit_web_data_source_get_data (long data_source) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_data_source_get_data (data_source);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_data_source_get_encoding (long data_source);
public static final long webkit_web_data_source_get_encoding (long data_source) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_data_source_get_encoding (data_source);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_frame_get_data_source (long frame);
public static final long webkit_web_frame_get_data_source (long frame) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_data_source (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_frame_get_global_context (long frame);
public static final long webkit_web_frame_get_global_context (long frame) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_global_context (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_frame_get_load_status (long frame);
public static final int webkit_web_frame_get_load_status (long frame) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_load_status (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_frame_get_parent (long frame);
public static final long webkit_web_frame_get_parent (long frame) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_parent (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_frame_get_title (long frame);
public static final long webkit_web_frame_get_title (long frame) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_title (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_frame_get_type ();
public static final long webkit_web_frame_get_type () {
	// Can't find reference. Probably a webkit1 macro.
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_frame_get_uri (long frame);
public static final long webkit_web_frame_get_uri (long frame) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_uri (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_frame_get_web_view (long frame);
public static final long webkit_web_frame_get_web_view (long frame) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_frame_get_web_view (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_policy_decision_download (long decision);
public static final void webkit_web_policy_decision_download (long decision) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		_webkit_web_policy_decision_download (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_policy_decision_ignore (long decision);
public static final void webkit_web_policy_decision_ignore (long decision) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		_webkit_web_policy_decision_ignore (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_can_go_back (long web_view);
public static final int webkit_web_view_can_go_back (long web_view) {
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT1 || WEBKIT2;
	lock.lock();
	try {
		return _webkit_web_view_can_show_mime_type (web_view, mime_type);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_execute_script (long web_view, byte[] script);
public static final void webkit_web_view_execute_script (long web_view, byte[] script) { // never called
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		_webkit_web_view_execute_script (web_view, script);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_dom_document (long web_view);
public static final long webkit_web_view_get_dom_document (long web_view) {
	assert WEBKIT1 : Webkit1AssertMsg;
	//TODO - guard from being called on webkit2 (webkit_web_view_get_dom_document)
	lock.lock();
	try {
		return _webkit_web_view_get_dom_document (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native double _webkit_web_view_get_estimated_load_progress (long web_view);
public static final double webkit_web_view_get_estimated_load_progress (long web_view) {
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_web_view_get_estimated_load_progress (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_get_load_status (long web_view);
public static final int webkit_web_view_get_load_status (long web_view) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_view_get_load_status (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_main_frame (long web_view);
public static final long webkit_web_view_get_main_frame (long web_view) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_view_get_main_frame (web_view);
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
public static final native double _webkit_web_view_get_progress (long web_view);
public static final double webkit_web_view_get_progress (long web_view) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_view_get_progress (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_settings (long web_view);
public static final long webkit_web_view_get_settings (long web_view) {
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT1 || WEBKIT2;
	lock.lock();
	try {
		return _webkit_web_view_get_title (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_type ();
public static final long webkit_web_view_get_type () {
	// TODO Bug 514859 Investigate if this is a webkit1 only function or if it can be used on webkit2 also.
	// can't find reference for it. Could be a macro.
	lock.lock();
	try {
		return _webkit_web_view_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_uri (long web_view);
public static final long webkit_web_view_get_uri (long web_view) {
	assert WEBKIT1 || WEBKIT2;
	lock.lock();
	try {
		return _webkit_web_view_get_uri (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_window_features (long web_view);
public static final long webkit_web_view_get_window_features (long web_view) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		return _webkit_web_view_get_window_features (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_web_view_get_window_properties (long webView);
/** WebKitWindowProperties * webkit_web_view_get_window_properties (WebKitWebView *web_view); */
public static final long webkit_web_view_get_window_properties (long webView) {
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT1 || WEBKIT2;
	lock.lock();
	try {
		_webkit_web_view_load_bytes (web_view, bytes, mime_type, encoding, base_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_string (long web_view, byte[] content, byte[] mime_type, byte[] encoding, byte[] base_uri);
public static final void webkit_web_view_load_string (long web_view, byte[] content, byte[] mime_type, byte[] encoding, byte[] base_uri) {
	assert WEBKIT1 : Webkit1AssertMsg;
	lock.lock();
	try {
		_webkit_web_view_load_string (web_view, content, mime_type, encoding, base_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_request (long web_view, long request);
public static final void webkit_web_view_load_request (long web_view, long request) {
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT2;
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
	assert WEBKIT2;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT1 || WEBKIT2;
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
	assert WEBKIT2 : Webkit2AssertMsg; // Since 2.16
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
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
	assert WEBKIT2 : Webkit2AssertMsg;
	lock.lock();
	try {
		return _webkit_uri_response_get_mime_type (response);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start SWT natives --------------------- */

public static final native int JSClassDefinition_sizeof ();
public static final native int GdkRectangle_sizeof();

/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (long dest, JSClassDefinition src, long size);

/**
 * @method flags=getter
 * @param cookie cast=(SoupCookie *)
 */
public static final native long _SoupCookie_expires (long cookie);
public static final long SoupCookie_expires (long cookie) {
	lock.lock();
	try {
		return _SoupCookie_expires (cookie);
	} finally {
		lock.unlock();
	}
}

/**
 * @method flags=setter
 * @param message cast=(SoupMessage *)
 * @param method cast=(const char *)
 */
public static final native void _SoupMessage_method (long message, long method);
public static final void SoupMessage_method (long message, long method) {
	lock.lock();
	try {
		_SoupMessage_method (message, method);
	} finally {
		lock.unlock();
	}
}

/**
 * @method flags=getter
 * @param message cast=(SoupMessage *)
 */
public static final native long _SoupMessage_request_body (long message);
public static final long SoupMessage_request_body (long message) {
	lock.lock();
	try {
		return _SoupMessage_request_body (message);
	} finally {
		lock.unlock();
	}
}

/**
 * @method flags=getter
 * @param message cast=(SoupMessage *)
 */
public static final native long _SoupMessage_request_headers (long message);
public static final long SoupMessage_request_headers (long message) {
	lock.lock();
	try {
		return _SoupMessage_request_headers (message);
	} finally {
		lock.unlock();
	}
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others. All rights reserved.
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
	}

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

	public static final int WEBKIT_USER_SCRIPT_INJECT_AT_DOCUMENT_START = 0;
	public static final int WEBKIT_USER_CONTENT_INJECT_TOP_FRAME = 1;

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
public static final native long JSObjectGetProperty(long ctx, long object, long propertyName, long [] exception);

/** @method flags=dynamic */
public static final native long JSObjectGetPropertyAtIndex(long ctx, long object, int propertyIndex, long [] exception);

/** @method flags=dynamic */
public static final native long JSStringCreateWithUTF8CString(byte[] string);

/** @method flags=dynamic */
public static final native long JSStringGetMaximumUTF8CStringSize(long string);

/** @method flags=dynamic */
public static final native long JSStringGetUTF8CString(long string, byte[] buffer, long bufferSize);

/** @method flags=dynamic */
public static final native void JSStringRelease(long string);

// Signature: 	   void webkit_javascript_result_unref (WebKitJavascriptResult *js_result);
// Type Note:      WebKitJavascriptResult -> gpointer -> jlong
/** @method flags=dynamic */
public static final native void webkit_javascript_result_unref(long js_result);

/** @method flags=dynamic */
public static final native int JSValueGetType(long ctx, long value);

/** @method flags=dynamic */
public static final native double JSValueToNumber(long ctx, long value, long [] exception);

/** @method flags=dynamic */
public static final native long JSValueToStringCopy(long ctx, long value, long [] exception);

/* --------------------- start libsoup natives --------------------- */

/** @method flags=dynamic */
public static final native long soup_cookie_get_name(long cookie);

/** @method flags=dynamic */
public static final native long soup_cookie_get_value(long cookie);

/** @method flags=dynamic */
public static final native long soup_cookie_parse(byte[] header, long origin);

/** @method flags=dynamic */
public static final native void soup_message_headers_append(long headers, byte[] name, byte[] value);

/** @method flags=dynamic */
public static final native void soup_uri_free(long uri);

/** @method flags=dynamic */
public static final native long soup_uri_new(byte[] uri_string);

/* --------------------- start WebKitGTK natives --------------------- */

/** @method flags=dynamic */
public static final native void webkit_authentication_request_authenticate(long request, long credential);

/** @method flags=dynamic */
public static final native void webkit_authentication_request_cancel(long request);

/** @method flags=dynamic */
public static final native boolean webkit_authentication_request_is_retry(long request);

/**
 * @method flags=dynamic
 */
public static final native void webkit_cookie_manager_add_cookie(long cookie_manager, long cookie, long cancellable, long cb, long user_data);

/** @method flags=dynamic */
public static final native boolean webkit_cookie_manager_add_cookie_finish(long cookie_manager, long result, long[] error );

/**
 * @method flags=dynamic
 */
public static final native void webkit_cookie_manager_get_cookies(long cookie_manager, byte [] uri, long cancellable, long cb, long user_data);

/** @method flags=dynamic */
public static final native long webkit_cookie_manager_get_cookies_finish(long cookie_manager, long result, long[] error );

/** @method flags=dynamic */
public static final native void webkit_credential_free(long credential);

/** @method flags=dynamic */
public static final native long webkit_web_context_allow_tls_certificate_for_host(long webKitWebContext, long GTlsCertificate, byte[] constGCharHost);

/** @method flags=dynamic */
public static final native long webkit_web_context_get_type();

/** @method flags=dynamic */
public static final native long webkit_credential_new(byte[] username, byte[] password, int persistence);


/** @method flags=dynamic */
public static final native int webkit_dom_event_target_add_event_listener(long target, byte[] name, long handler, int bubble, long userData);

/** @method flags=dynamic */
public static final native int webkit_dom_mouse_event_get_alt_key(long self);

/** @method flags=dynamic */
public static final native short webkit_dom_mouse_event_get_button(long self);

/** @method flags=dynamic */
public static final native int webkit_dom_mouse_event_get_ctrl_key(long self);

/** @method flags=dynamic */
public static final native int webkit_dom_mouse_event_get_meta_key(long self);

/** @method flags=dynamic */
public static final native long webkit_dom_mouse_event_get_screen_x(long self);

/** @method flags=dynamic */
public static final native long webkit_dom_mouse_event_get_screen_y(long self);

/** @method flags=dynamic */
public static final native int webkit_dom_mouse_event_get_shift_key(long self);

/** @method flags=dynamic */
public static final native long webkit_dom_ui_event_get_char_code(long self);

/** @method flags=dynamic */
public static final native long webkit_dom_ui_event_get_detail(long self);

/** @method flags=dynamic */
public static final native long webkit_dom_ui_event_get_key_code(long self);

/** @method flags=dynamic */
public static final native void webkit_download_cancel(long download);

/** @method flags=dynamic */
public static final native long webkit_download_get_received_data_length(long download);

/** @method flags=dynamic */
public static final native long webkit_download_get_request(long download);

/** @method flags=dynamic */
public static final native long webkit_download_get_response(long download);

/** @method flags=dynamic */
public static final native long webkit_download_get_type();

/** @method flags=dynamic */
public static final native long webkit_uri_response_get_content_length(long response);

/** @method flags=dynamic */
public static final native long webkit_download_get_web_view(long download);

/** @method flags=dynamic */
public static final native void webkit_download_set_allow_overwrite(long download, boolean allowed);

/** @method flags=dynamic */
public static final native void webkit_download_set_destination(long download, byte[] destination_uri);

/** @method flags=dynamic */
public static final native boolean webkit_hit_test_result_context_is_link(long hit_test_result);

/** @method flags=dynamic */
public static final native long webkit_hit_test_result_get_link_uri(long hit_test_result);

/** @method flags=dynamic */
public static final native long webkit_hit_test_result_get_link_title(long hit_test_result);

/** @method flags=dynamic */
public static final native int webkit_get_major_version();

/** @method flags=dynamic */
public static final native int webkit_get_micro_version();

/** @method flags=dynamic */
public static final native int webkit_get_minor_version();

/** @method flags=dynamic */
public static final native long webkit_navigation_policy_decision_get_request(long decision);

/** @method flags=dynamic */
public static final native void webkit_policy_decision_download(long decision);

/** @method flags=dynamic */
public static final native void webkit_policy_decision_ignore(long decision);

/** @method flags=dynamic */
public static final native long webkit_web_context_get_default();

/** @method flags=dynamic */
public static final native long webkit_web_context_get_cookie_manager(long context);

/** @method flags=dynamic */
public static final native long webkit_web_context_get_website_data_manager(long context);

/** @method flags=dynamic */
public static final native long webkit_web_context_get_security_manager(long context);

/** @method flags=dynamic */
public static final native void webkit_web_context_set_tls_errors_policy(long context, int policy);

/** @method flags=dynamic */
public static final native void webkit_web_context_register_uri_scheme(long context, byte[] scheme, long callback, long user_data, long user_data_destroy_func);

/** @method flags=dynamic */
public static final native int webkit_web_view_can_go_back(long web_view);

/** @method flags=dynamic */
public static final native long webkit_web_view_get_main_resource(long web_view);

/** @method flags=dynamic */
public static final native long webkit_web_view_get_context(long web_view);

/** @method flags=dynamic */
public static final native int webkit_web_view_can_go_forward(long web_view);

/** @method flags=dynamic */
public static final native int webkit_web_view_can_show_mime_type(long web_view, long mime_type);

/** @method flags=dynamic */
public static final native double webkit_web_view_get_estimated_load_progress(long web_view);

/** @method flags=dynamic */
public static final native long webkit_web_view_get_page_id(long web_view);

/** @method flags=dynamic */
public static final native long webkit_web_view_get_settings(long web_view);

/** @method flags=dynamic */
public static final native long webkit_web_view_get_user_content_manager(long web_view);

/** @method flags=dynamic */
public static final native long webkit_web_view_get_title(long web_view);

/** @method flags=dynamic */
public static final native long webkit_web_view_get_uri(long web_view);

/** @method flags=dynamic */
/* WebKitWindowProperties * webkit_web_view_get_window_properties (WebKitWebView *web_view); */
public static final native long webkit_web_view_get_window_properties(long webView);

/**
 * @method flags=dynamic
 * @param rectangle cast=(GdkRectangle *),flags=no_in
 */
public static final native void webkit_window_properties_get_geometry(long webKitWindowProperties, GdkRectangle rectangle);



/** @method flags=dynamic */
public static final native void webkit_web_view_go_back(long web_view);

/** @method flags=dynamic */
public static final native void webkit_web_view_go_forward(long web_view);

/** @method flags=dynamic */
public static final native void webkit_web_view_load_html(long web_view, byte[] content, byte[] base_uri);

/** @method flags=dynamic */
public static final native void webkit_web_view_load_bytes(long web_view, long bytes, byte [] mime_type, byte [] encoding, byte [] base_uri);

/** @method flags=dynamic */
public static final native void webkit_web_view_load_request(long web_view, long request);

/** @method flags=dynamic */
public static final native void webkit_web_view_load_uri(long web_view, byte[] uri);

/** @method flags=dynamic */
public static final native long webkit_web_view_new();
/** @method flags=dynamic */
public static final native long webkit_web_view_new_with_related_view(long web_view);

/**
 * @method flags=dynamic
 * @param js_result cast=(gpointer)
 */
/* JSGlobalContextRef webkit_javascript_result_get_global_context (WebKitJavascriptResult *js_result);  */
public static final native long webkit_javascript_result_get_global_context(long js_result);

/**
 * @method flags=dynamic
 * @param js_result cast=(gpointer)
 */
/* JSValueRef webkit_javascript_result_get_value (WebKitJavascriptResult *js_result); */
public static final native long webkit_javascript_result_get_value(long js_result);

/** @method flags=dynamic */
public static final native void webkit_web_view_reload(long web_view);


/** @method flags=dynamic */
/* 			    void webkit_web_view_run_javascript (WebKitWebView *web_view, const gchar *script, GCancellable *cancellable, GAsyncReadyCallback callback, gpointer user_data); **/
public static final native void webkit_web_view_run_javascript(long web_view, byte [] script, long cancellable, long  callback, long user_data);

/** @method flags=dynamic */
public static final native void webkit_web_resource_get_data(long webKitWebResource, long gCancellable, long GAsyncReadyCallback, long user_data);

/** @method flags=dynamic */
public static final native long webkit_web_resource_get_data_finish(long WebKitWebResource, long GAsyncResult, long [] gsize, long[] GError);


/**
 * @method flags=dynamic
 * @param gerror cast=(GError **)
 */
/*WebKitJavascriptResult * webkit_web_view_run_javascript_finish (WebKitWebView *web_view, GAsyncResult *result, GError **error);*/
public static final native long webkit_web_view_run_javascript_finish(long web_view, long GAsyncResult, long [] gerror);

/** @method flags=dynamic */
public static final native void webkit_web_view_stop_loading(long web_view);

/** @method flags=dynamic */
public static final native void webkit_website_data_manager_clear(long manager, long types, long timespan, long cancellable, long callback, long user_data);

/** @method flags=dynamic */
public static final native long webkit_response_policy_decision_get_request(long decision);

/** @method flags=dynamic */
public static final native long webkit_response_policy_decision_get_response(long decision);

/** @method flags=dynamic */
public static final native long webkit_uri_request_new(byte[] uri);

/** @method flags=dynamic */
public static final native long webkit_uri_request_get_http_headers(long request);

/** @method flags=dynamic */
public static final native long webkit_uri_request_get_uri(long request);

/** @method flags=dynamic */
public static final native long webkit_uri_response_get_mime_type(long responce);

/**
 * @method flags=dynamic
 * @param content_type flags=no_out
 */
public static final native void webkit_uri_scheme_request_finish(long request, long stream, long stream_length, byte[] content_type);

/** @method flags=dynamic */
public static final native long webkit_uri_scheme_request_get_uri (long request);

/** @method flags=dynamic */
public static final native long webkit_uri_scheme_request_get_web_view(long request);

/** @method flags=dynamic */
public static final native long webkit_user_content_manager_add_script(long manager, long script);

/** @method flags=dynamic */
public static final native void webkit_user_content_manager_remove_all_scripts(long manager);

/** @method flags=dynamic */
public static final native void webkit_security_manager_register_uri_scheme_as_secure(long security_manager, byte[] scheme);

/**
 * @method flags=dynamic
 * @param source flags=no_out
 */
public static final native long webkit_user_script_new (byte[] source, int injected_frames, int injection_time, long allow_list, long block_list);

/** @method flags=dynamic */
public static final native void webkit_user_script_unref (long user_script);

/* --------------------- start SWT natives --------------------- */
public static final native int GdkRectangle_sizeof();

}

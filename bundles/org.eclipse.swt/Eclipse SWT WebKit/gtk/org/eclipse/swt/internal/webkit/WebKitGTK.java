/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others. All rights reserved.
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


import org.eclipse.swt.internal.C;

public class WebKitGTK extends C {

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

	/** Signals */
	public static final byte[] authenticate = ascii ("authenticate"); // $NON-NLS-1$
	public static final byte[] close_web_view = ascii ("close-web-view"); // $NON-NLS-1$
	public static final byte[] console_message = ascii ("console-message"); // $NON-NLS-1$
	public static final byte[] create_web_view = ascii ("create-web-view"); // $NON-NLS-1$
	public static final byte[] download_requested = ascii ("download-requested"); // $NON-NLS-1$
	public static final byte[] hovering_over_link = ascii ("hovering-over-link"); // $NON-NLS-1$
	public static final byte[] mime_type_policy_decision_requested = ascii ("mime-type-policy-decision-requested"); // $NON-NLS-1$
	public static final byte[] navigation_policy_decision_requested = ascii ("navigation-policy-decision-requested"); // $NON-NLS-1$
	public static final byte[] notify_load_status = ascii ("notify::load-status"); // $NON-NLS-1$
	public static final byte[] notify_progress = ascii ("notify::progress"); // $NON-NLS-1$
	public static final byte[] notify_title = ascii ("notify::title"); // $NON-NLS-1$
	public static final byte[] populate_popup = ascii ("populate-popup"); // $NON-NLS-1$
	public static final byte[] resource_request_starting = ascii ("resource_request_starting"); // $NON-NLS-1$
	public static final byte[] status_bar_text_changed = ascii ("status-bar-text-changed"); // $NON-NLS-1$
	public static final byte[] web_view_ready = ascii ("web-view-ready"); // $NON-NLS-1$
	public static final byte[] window_object_cleared = ascii ("window-object-cleared"); // $NON-NLS-1$

	/** Properties */
	public static final byte[] default_encoding = ascii ("default-encoding"); // $NON-NLS-1$
	public static final byte[] enable_scripts = ascii ("enable-scripts"); // $NON-NLS-1$
	public static final byte[] enable_universal_access_from_file_uris = ascii ("enable-universal-access-from-file-uris"); // $NON-NLS-1$
	public static final byte[] height = ascii ("height"); // $NON-NLS-1$
	public static final byte[] javascript_can_open_windows_automatically = ascii ("javascript-can-open-windows-automatically"); // $NON-NLS-1$
	public static final byte[] locationbar_visible = ascii ("locationbar-visible"); // $NON-NLS-1$
	public static final byte[] menubar_visible = ascii ("menubar-visible"); // $NON-NLS-1$
	public static final byte[] SOUP_SESSION_PROXY_URI = ascii ("proxy-uri"); // $NON-NLS-1$
	public static final byte[] statusbar_visible = ascii ("statusbar-visible"); // $NON-NLS-1$
	public static final byte[] toolbar_visible = ascii ("toolbar-visible"); // $NON-NLS-1$
	public static final byte[] user_agent = ascii ("user-agent"); // $NON-NLS-1$
	public static final byte[] width = ascii ("width"); // $NON-NLS-1$
	public static final byte[] x = ascii ("x"); // $NON-NLS-1$
	public static final byte[] y = ascii ("y"); // $NON-NLS-1$

	/** DOM events */
	public static final byte[] dragstart = ascii ("dragstart"); // $NON-NLS-1$
	public static final byte[] keydown = ascii ("keydown"); // $NON-NLS-1$
	public static final byte[] keypress = ascii ("keypress"); // $NON-NLS-1$
	public static final byte[] keyup = ascii ("keyup"); // $NON-NLS-1$
	public static final byte[] mousedown = ascii ("mousedown"); // $NON-NLS-1$
	public static final byte[] mousemove = ascii ("mousemove"); // $NON-NLS-1$
	public static final byte[] mouseup = ascii ("mouseup"); // $NON-NLS-1$
	public static final byte[] mousewheel = ascii ("mousewheel"); // $NON-NLS-1$

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
public static final native int /*long*/ _JSClassCreate (int /*long*/ definition);
public static final int /*long*/ JSClassCreate (int /*long*/ definition) {
	lock.lock();
	try {
		return _JSClassCreate (definition);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSContextGetGlobalObject (int /*long*/ ctx);
public static final int /*long*/ JSContextGetGlobalObject (int /*long*/ ctx) {
	lock.lock();
	try {
		return _JSContextGetGlobalObject (ctx);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSEvaluateScript (int /*long*/ ctx, int /*long*/ script, int /*long*/ thisObject, int /*long*/ sourceURL, int startingLineNumber, int /*long*/[] exception);
public static final int /*long*/ JSEvaluateScript (int /*long*/ ctx, int /*long*/ script, int /*long*/ thisObject, int /*long*/ sourceURL, int startingLineNumber, int /*long*/[] exception) {
	lock.lock();
	try {
		return _JSEvaluateScript (ctx, script, thisObject, sourceURL, startingLineNumber, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSObjectGetPrivate (int /*long*/ object);
public static final int /*long*/ JSObjectGetPrivate (int /*long*/ object) {
	lock.lock();
	try {
		return _JSObjectGetPrivate (object);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSObjectGetProperty (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName, int /*long*/[] exception);
public static final int /*long*/ JSObjectGetProperty (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName, int /*long*/[] exception) {
	lock.lock();
	try {
		return _JSObjectGetProperty (ctx, object, propertyName, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSObjectGetPropertyAtIndex (int /*long*/ ctx, int /*long*/ object, int propertyIndex, int /*long*/[] exception);
public static final int /*long*/ JSObjectGetPropertyAtIndex (int /*long*/ ctx, int /*long*/ object, int propertyIndex, int /*long*/[] exception) {
	lock.lock();
	try {
		return _JSObjectGetPropertyAtIndex (ctx, object, propertyIndex, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSObjectMake (int /*long*/ ctx, int /*long*/ jsClass, int /*long*/ data);
public static final int /*long*/ JSObjectMake (int /*long*/ ctx, int /*long*/ jsClass, int /*long*/ data) {
	lock.lock();
	try {
		return _JSObjectMake (ctx, jsClass, data);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSObjectMakeArray (int /*long*/ ctx, int /*long*/ argumentCount, int /*long*/[] arguments, int /*long*/[] exception);
public static final int /*long*/ JSObjectMakeArray (int /*long*/ ctx, int /*long*/ argumentCount, int /*long*/[] arguments, int /*long*/[] exception) {
	lock.lock();
	try {
		return _JSObjectMakeArray (ctx, argumentCount, arguments, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSObjectMakeFunctionWithCallback (int /*long*/ ctx, int /*long*/ name, int /*long*/ callAsFunction);
public static final int /*long*/ JSObjectMakeFunctionWithCallback (int /*long*/ ctx, int /*long*/ name, int /*long*/ callAsFunction) {
	lock.lock();
	try {
		return _JSObjectMakeFunctionWithCallback (ctx, name, callAsFunction);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _JSObjectSetProperty (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName, int /*long*/ value, int attributes, int /*long*/[] exception);
public static final void JSObjectSetProperty (int /*long*/ ctx, int /*long*/ object, int /*long*/ propertyName, int /*long*/ value, int attributes, int /*long*/[] exception) {
	lock.lock();
	try {
		_JSObjectSetProperty (ctx, object, propertyName, value, attributes, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSStringCreateWithUTF8CString (byte[] string);
public static final int /*long*/ JSStringCreateWithUTF8CString (byte[] string) {
	lock.lock();
	try {
		return _JSStringCreateWithUTF8CString (string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSStringGetLength (int /*long*/ string);
public static final int /*long*/ JSStringGetLength (int /*long*/ string) {
	lock.lock();
	try {
		return _JSStringGetLength (string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSStringGetMaximumUTF8CStringSize (int /*long*/ string);
public static final int /*long*/ JSStringGetMaximumUTF8CStringSize (int /*long*/ string) {
	lock.lock();
	try {
		return _JSStringGetMaximumUTF8CStringSize (string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSStringGetUTF8CString (int /*long*/ string, byte[] buffer, int /*long*/ bufferSize);
public static final int /*long*/ JSStringGetUTF8CString (int /*long*/ string, byte[] buffer, int /*long*/ bufferSize) {
	lock.lock();
	try {
		return _JSStringGetUTF8CString (string, buffer, bufferSize);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _JSStringIsEqualToUTF8CString (int /*long*/ a, byte[] b);
public static final int JSStringIsEqualToUTF8CString (int /*long*/ a, byte[] b) {
	lock.lock();
	try {
		return _JSStringIsEqualToUTF8CString (a, b);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _JSStringRelease (int /*long*/ string);
public static final void JSStringRelease (int /*long*/ string) {
	lock.lock();
	try {
		_JSStringRelease (string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _JSValueGetType (int /*long*/ ctx, int /*long*/ value);
public static final int JSValueGetType (int /*long*/ ctx, int /*long*/ value) {
	lock.lock();
	try {
		return _JSValueGetType (ctx, value);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _JSValueIsObjectOfClass (int /*long*/ ctx, int /*long*/ value, int /*long*/ jsClass);
public static final int JSValueIsObjectOfClass (int /*long*/ ctx, int /*long*/ value, int /*long*/ jsClass) {
	lock.lock();
	try {
		return _JSValueIsObjectOfClass (ctx, value, jsClass);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSValueMakeBoolean (int /*long*/ ctx, int bool);
public static final int /*long*/ JSValueMakeBoolean (int /*long*/ ctx, int bool) {
	lock.lock();
	try {
		return _JSValueMakeBoolean (ctx, bool);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSValueMakeNumber (int /*long*/ ctx, double number);
public static final int /*long*/ JSValueMakeNumber (int /*long*/ ctx, double number) {
	lock.lock();
	try {
		return _JSValueMakeNumber (ctx, number);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSValueMakeString (int /*long*/ ctx, int /*long*/ string);
public static final int /*long*/ JSValueMakeString (int /*long*/ ctx, int /*long*/ string) {
	lock.lock();
	try {
		return _JSValueMakeString (ctx, string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSValueMakeUndefined (int /*long*/ ctx);
public static final int /*long*/ JSValueMakeUndefined (int /*long*/ ctx) {
	lock.lock();
	try {
		return _JSValueMakeUndefined (ctx);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native double _JSValueToNumber (int /*long*/ ctx, int /*long*/ value, int /*long*/[] exception);
public static final double JSValueToNumber (int /*long*/ ctx, int /*long*/ value, int /*long*/[] exception) {
	lock.lock();
	try {
		return _JSValueToNumber (ctx, value, exception);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _JSValueToStringCopy (int /*long*/ ctx, int /*long*/ value, int /*long*/[] exception);
public static final int /*long*/ JSValueToStringCopy (int /*long*/ ctx, int /*long*/ value, int /*long*/[] exception) {
	lock.lock();
	try {
		return _JSValueToStringCopy (ctx, value, exception);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start libsoup natives --------------------- */

/** @method flags=dynamic */
public static final native void _soup_auth_authenticate (int /*long*/ auth, byte[] username, byte[] password);
public static final void soup_auth_authenticate (int /*long*/ auth, byte[] username, byte[] password) {
	lock.lock();
	try {
		_soup_auth_authenticate (auth, username, password);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_auth_get_host (int /*long*/ auth);
public static final int /*long*/ soup_auth_get_host (int /*long*/ auth) {
	lock.lock();
	try {
		return _soup_auth_get_host (auth);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_auth_get_scheme_name (int /*long*/ auth);
public static final int /*long*/ soup_auth_get_scheme_name (int /*long*/ auth) {
	lock.lock();
	try {
		return _soup_auth_get_scheme_name (auth);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_cookie_free (int /*long*/ cookie);
public static final void soup_cookie_free (int /*long*/ cookie) {
	lock.lock();
	try {
		_soup_cookie_free (cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_cookie_jar_add_cookie (int /*long*/ jar, int /*long*/ cookie);
public static final void soup_cookie_jar_add_cookie (int /*long*/ jar, int /*long*/ cookie) {
	lock.lock();
	try {
		_soup_cookie_jar_add_cookie (jar, cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_cookie_jar_all_cookies (int /*long*/ jar);
public static final int /*long*/ soup_cookie_jar_all_cookies (int /*long*/ jar) {
	lock.lock();
	try {
		return _soup_cookie_jar_all_cookies (jar);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_cookie_jar_delete_cookie (int /*long*/ jar, int /*long*/ cookie);
public static final void soup_cookie_jar_delete_cookie (int /*long*/ jar, int /*long*/ cookie) {
	lock.lock();
	try {
		_soup_cookie_jar_delete_cookie (jar, cookie);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_cookie_jar_get_cookies (int /*long*/ jar, int /*long*/ uri, int for_http);
public static final int /*long*/ soup_cookie_jar_get_cookies (int /*long*/ jar, int /*long*/ uri, int for_http) {
	lock.lock();
	try {
		return _soup_cookie_jar_get_cookies (jar, uri, for_http);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_cookie_jar_get_type ();
public static final int /*long*/ soup_cookie_jar_get_type () {
	lock.lock();
	try {
		return _soup_cookie_jar_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_cookie_parse (byte[] header, int /*long*/ origin);
public static final int /*long*/ soup_cookie_parse (byte[] header, int /*long*/ origin) {
	lock.lock();
	try {
		return _soup_cookie_parse (header, origin);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_message_body_append (int /*long*/ body, int use, int /*long*/ data, int /*long*/ length);
public static final void soup_message_body_append (int /*long*/ body, int use, int /*long*/ data, int /*long*/ length) {
	lock.lock();
	try {
		_soup_message_body_append (body, use, data, length);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_message_body_flatten (int /*long*/ body);
public static final void soup_message_body_flatten (int /*long*/ body) {
	lock.lock();
	try {
		_soup_message_body_flatten (body);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_message_get_uri (int /*long*/ msg);
public static final int /*long*/ soup_message_get_uri (int /*long*/ msg) {
	lock.lock();
	try {
		return _soup_message_get_uri (msg);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_message_headers_append (int /*long*/ headers, byte[] name, byte[] value);
public static final void soup_message_headers_append (int /*long*/ headers, byte[] name, byte[] value) {
	lock.lock();
	try {
		_soup_message_headers_append (headers, name, value);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_session_add_feature_by_type (int /*long*/ session, int /*long*/ type);
public static final void soup_session_add_feature_by_type (int /*long*/ session, int /*long*/ type) {
	lock.lock();
	try {
		_soup_session_add_feature_by_type (session, type);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_session_get_feature (int /*long*/ session, int /*long*/ feature_type);
public static final int /*long*/ soup_session_get_feature (int /*long*/ session, int /*long*/ feature_type) {
	lock.lock();
	try {
		return _soup_session_get_feature (session, feature_type);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_session_feature_attach (int /*long*/ feature, int /*long*/ session);
public static final void soup_session_feature_attach (int /*long*/ feature, int /*long*/ session) {
	lock.lock();
	try {
		_soup_session_feature_attach (feature, session);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_session_get_type ();
public static final int /*long*/ soup_session_get_type () {
	lock.lock();
	try {
		return _soup_session_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_session_feature_detach (int /*long*/ feature, int /*long*/ session);
public static final void soup_session_feature_detach (int /*long*/ feature, int /*long*/ session) {
	lock.lock();
	try {
		_soup_session_feature_detach (feature, session);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _soup_uri_free (int /*long*/ uri);
public static final void soup_uri_free (int /*long*/ uri) {
	lock.lock();
	try {
		_soup_uri_free (uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_uri_new (byte[] uri_string);
public static final int /*long*/ soup_uri_new (byte[] uri_string) {
	lock.lock();
	try {
		return _soup_uri_new (uri_string);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _soup_uri_to_string (int /*long*/ uri, int just_path_and_query);
public static final int /*long*/ soup_uri_to_string (int /*long*/ uri, int just_path_and_query) {
	lock.lock();
	try {
		return _soup_uri_to_string (uri, just_path_and_query);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start WebKitGTK natives --------------------- */

/** @method flags=dynamic */
public static final native int _webkit_dom_event_target_add_event_listener (int /*long*/ target, byte[] name, int /*long*/ handler, int bubble, int /*long*/ userData);
public static final int webkit_dom_event_target_add_event_listener (int /*long*/ target, byte[] name, int /*long*/ handler, int bubble, int /*long*/ userData) {
	lock.lock();
	try {
		return _webkit_dom_event_target_add_event_listener (target, name, handler, bubble, userData);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_alt_key (int /*long*/ self);
public static final int webkit_dom_mouse_event_get_alt_key (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_alt_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native short _webkit_dom_mouse_event_get_button (int /*long*/ self);
public static final short webkit_dom_mouse_event_get_button (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_button (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_ctrl_key (int /*long*/ self);
public static final int webkit_dom_mouse_event_get_ctrl_key (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_ctrl_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_meta_key (int /*long*/ self);
public static final int webkit_dom_mouse_event_get_meta_key (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_meta_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_mouse_event_get_screen_x (int /*long*/ self);
public static final long webkit_dom_mouse_event_get_screen_x (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_screen_x (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_mouse_event_get_screen_y (int /*long*/ self);
public static final long webkit_dom_mouse_event_get_screen_y (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_screen_y (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_dom_mouse_event_get_shift_key (int /*long*/ self);
public static final int webkit_dom_mouse_event_get_shift_key (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_mouse_event_get_shift_key (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_ui_event_get_char_code (int /*long*/ self);
public static final long webkit_dom_ui_event_get_char_code (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_ui_event_get_char_code (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_ui_event_get_detail (int /*long*/ self);
public static final long webkit_dom_ui_event_get_detail (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_ui_event_get_detail (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_dom_ui_event_get_key_code (int /*long*/ self);
public static final long webkit_dom_ui_event_get_key_code (int /*long*/ self) {
	lock.lock();
	try {
		return _webkit_dom_ui_event_get_key_code (self);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_cancel (int /*long*/ download);
public static final void webkit_download_cancel (int /*long*/ download) {
	lock.lock();
	try {
		_webkit_download_cancel (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_current_size (int /*long*/ download);
public static final long webkit_download_get_current_size (int /*long*/ download) {
	lock.lock();
	try {
		return _webkit_download_get_current_size (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_download_get_network_request (int /*long*/ download);
public static final int /*long*/ webkit_download_get_network_request (int /*long*/ download) {
	lock.lock();
	try {
		return _webkit_download_get_network_request (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_download_get_status (int /*long*/ download);
public static final int webkit_download_get_status (int /*long*/ download) {
	lock.lock();
	try {
		return _webkit_download_get_status (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_download_get_suggested_filename (int /*long*/ download);
public static final int /*long*/ webkit_download_get_suggested_filename (int /*long*/ download) {
	lock.lock();
	try {
		return _webkit_download_get_suggested_filename (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native long _webkit_download_get_total_size (int /*long*/ download);
public static final long webkit_download_get_total_size (int /*long*/ download) {
	lock.lock();
	try {
		return _webkit_download_get_total_size (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_download_get_uri (int /*long*/ download);
public static final int /*long*/ webkit_download_get_uri (int /*long*/ download) {
	lock.lock();
	try {
		return _webkit_download_get_uri (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_download_new (int /*long*/ request);
public static final int /*long*/ webkit_download_new (int /*long*/ request) {
	lock.lock();
	try {
		return _webkit_download_new (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_set_destination_uri (int /*long*/ download, byte[] destination_uri);
public static final void webkit_download_set_destination_uri (int /*long*/ download, byte[] destination_uri) {
	lock.lock();
	try {
		_webkit_download_set_destination_uri (download, destination_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_download_start (int /*long*/ download);
public static final void webkit_download_start (int /*long*/ download) {
	lock.lock();
	try {
		_webkit_download_start (download);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_get_default_session ();
public static final int /*long*/ webkit_get_default_session () {
	lock.lock();
	try {
		return _webkit_get_default_session ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_major_version ();
public static final int webkit_major_version () {
	lock.lock();
	try {
		return _webkit_major_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_micro_version ();
public static final int webkit_micro_version () {
	lock.lock();
	try {
		return _webkit_micro_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_minor_version ();
public static final int webkit_minor_version () {
	lock.lock();
	try {
		return _webkit_minor_version ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_network_request_get_message (int /*long*/ request);
public static final int /*long*/ webkit_network_request_get_message (int /*long*/ request) {
	lock.lock();
	try {
		return _webkit_network_request_get_message (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_network_request_get_uri (int /*long*/ request);
public static final int /*long*/ webkit_network_request_get_uri (int /*long*/ request) {
	lock.lock();
	try {
		return _webkit_network_request_get_uri (request);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_network_request_new (byte[] uri);
public static final int /*long*/ webkit_network_request_new (byte[] uri) {
	lock.lock();
	try {
		return _webkit_network_request_new (uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_soup_auth_dialog_get_type ();
public static final int /*long*/ webkit_soup_auth_dialog_get_type () {
	lock.lock();
	try {
		return _webkit_soup_auth_dialog_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_data_source_get_data (int /*long*/ data_source);
public static final int /*long*/ webkit_web_data_source_get_data (int /*long*/ data_source) {
	lock.lock();
	try {
		return _webkit_web_data_source_get_data (data_source);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_data_source_get_encoding (int /*long*/ data_source);
public static final int /*long*/ webkit_web_data_source_get_encoding (int /*long*/ data_source) {
	lock.lock();
	try {
		return _webkit_web_data_source_get_encoding (data_source);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_frame_get_data_source (int /*long*/ frame);
public static final int /*long*/ webkit_web_frame_get_data_source (int /*long*/ frame) {
	lock.lock();
	try {
		return _webkit_web_frame_get_data_source (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_frame_get_global_context (int /*long*/ frame);
public static final int /*long*/ webkit_web_frame_get_global_context (int /*long*/ frame) {
	lock.lock();
	try {
		return _webkit_web_frame_get_global_context (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_frame_get_load_status (int /*long*/ frame);
public static final int webkit_web_frame_get_load_status (int /*long*/ frame) {
	lock.lock();
	try {
		return _webkit_web_frame_get_load_status (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_frame_get_parent (int /*long*/ frame);
public static final int /*long*/ webkit_web_frame_get_parent (int /*long*/ frame) {
	lock.lock();
	try {
		return _webkit_web_frame_get_parent (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_frame_get_title (int /*long*/ frame);
public static final int /*long*/ webkit_web_frame_get_title (int /*long*/ frame) {
	lock.lock();
	try {
		return _webkit_web_frame_get_title (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_frame_get_type ();
public static final int /*long*/ webkit_web_frame_get_type () {
	lock.lock();
	try {
		return _webkit_web_frame_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_frame_get_uri (int /*long*/ frame);
public static final int /*long*/ webkit_web_frame_get_uri (int /*long*/ frame) {
	lock.lock();
	try {
		return _webkit_web_frame_get_uri (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_frame_get_web_view (int /*long*/ frame);
public static final int /*long*/ webkit_web_frame_get_web_view (int /*long*/ frame) {
	lock.lock();
	try {
		return _webkit_web_frame_get_web_view (frame);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_policy_decision_download (int /*long*/ decision);
public static final void webkit_web_policy_decision_download (int /*long*/ decision) {
	lock.lock();
	try {
		_webkit_web_policy_decision_download (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_policy_decision_ignore (int /*long*/ decision);
public static final void webkit_web_policy_decision_ignore (int /*long*/ decision) {
	lock.lock();
	try {
		_webkit_web_policy_decision_ignore (decision);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_can_go_back (int /*long*/ web_view);
public static final int webkit_web_view_can_go_back (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_can_go_back (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_can_go_forward (int /*long*/ web_view);
public static final int webkit_web_view_can_go_forward (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_can_go_forward (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_can_show_mime_type (int /*long*/ web_view, int /*long*/ mime_type);
public static final int webkit_web_view_can_show_mime_type (int /*long*/ web_view, int /*long*/ mime_type) {
	lock.lock();
	try {
		return _webkit_web_view_can_show_mime_type (web_view, mime_type);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_execute_script (int /*long*/ web_view, byte[] script);
public static final void webkit_web_view_execute_script (int /*long*/ web_view, byte[] script) {
	lock.lock();
	try {
		_webkit_web_view_execute_script (web_view, script);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_get_dom_document (int /*long*/ web_view);
public static final int /*long*/ webkit_web_view_get_dom_document (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_dom_document (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int _webkit_web_view_get_load_status (int /*long*/ web_view);
public static final int webkit_web_view_get_load_status (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_load_status (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_get_main_frame (int /*long*/ web_view);
public static final int /*long*/ webkit_web_view_get_main_frame (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_main_frame (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native double _webkit_web_view_get_progress (int /*long*/ web_view);
public static final double webkit_web_view_get_progress (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_progress (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_get_settings (int /*long*/ web_view);
public static final int /*long*/ webkit_web_view_get_settings (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_settings (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_get_title (int /*long*/ web_view);
public static final int /*long*/ webkit_web_view_get_title (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_title (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_get_type ();
public static final int /*long*/ webkit_web_view_get_type () {
	lock.lock();
	try {
		return _webkit_web_view_get_type ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_get_uri (int /*long*/ web_view);
public static final int /*long*/ webkit_web_view_get_uri (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_uri (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_get_window_features (int /*long*/ web_view);
public static final int /*long*/ webkit_web_view_get_window_features (int /*long*/ web_view) {
	lock.lock();
	try {
		return _webkit_web_view_get_window_features (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_go_back (int /*long*/ web_view);
public static final void webkit_web_view_go_back (int /*long*/ web_view) {
	lock.lock();
	try {
		_webkit_web_view_go_back (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_go_forward (int /*long*/ web_view);
public static final void webkit_web_view_go_forward (int /*long*/ web_view) {
	lock.lock();
	try {
		_webkit_web_view_go_forward (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_string (int /*long*/ web_view, byte[] content, byte[] mime_type, byte[] encoding, byte[] base_uri);
public static final void webkit_web_view_load_string (int /*long*/ web_view, byte[] content, byte[] mime_type, byte[] encoding, byte[] base_uri) {
	lock.lock();
	try {
		_webkit_web_view_load_string (web_view, content, mime_type, encoding, base_uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_load_uri (int /*long*/ web_view, byte[] uri);
public static final void webkit_web_view_load_uri (int /*long*/ web_view, byte[] uri) {
	lock.lock();
	try {
		_webkit_web_view_load_uri (web_view, uri);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native int /*long*/ _webkit_web_view_new ();
public static final int /*long*/ webkit_web_view_new () {
	lock.lock();
	try {
		return _webkit_web_view_new ();
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_reload (int /*long*/ web_view);
public static final void webkit_web_view_reload (int /*long*/ web_view) {
	lock.lock();
	try {
		_webkit_web_view_reload (web_view);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _webkit_web_view_stop_loading (int /*long*/ web_view);
public static final void webkit_web_view_stop_loading (int /*long*/ web_view) {
	lock.lock();
	try {
		_webkit_web_view_stop_loading (web_view);
	} finally {
		lock.unlock();
	}
}

/* --------------------- start SWT natives --------------------- */

public static final native int JSClassDefinition_sizeof ();

/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove (int /*long*/ dest, JSClassDefinition src, int /*long*/ size);

/**
 * @method flags=getter
 * @param cookie cast=(SoupCookie *)
 */
public static final native int /*long*/ _SoupCookie_expires (int /*long*/ cookie);
public static final int /*long*/ SoupCookie_expires (int /*long*/ cookie) {
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
public static final native void _SoupMessage_method (int /*long*/ message, int /*long*/ method);
public static final void SoupMessage_method (int /*long*/ message, int /*long*/ method) {
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
public static final native int /*long*/ _SoupMessage_request_body (int /*long*/ message);
public static final int /*long*/ SoupMessage_request_body (int /*long*/ message) {
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
public static final native int /*long*/ _SoupMessage_request_headers (int /*long*/ message);
public static final int /*long*/ SoupMessage_request_headers (int /*long*/ message) {
	lock.lock();
	try {
		return _SoupMessage_request_headers (message);
	} finally {
		lock.unlock();
	}
}

}

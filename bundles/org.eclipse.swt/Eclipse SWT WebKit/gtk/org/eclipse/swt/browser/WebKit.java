/*******************************************************************************
 * Copyright (c) 2010, 2022 IBM Corporation and others.
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
 *     Red Hat Inc. - generification
 *******************************************************************************/
package org.eclipse.swt.browser;


import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.internal.webkit.GdkRectangle;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * VERSIONS:
 * Versioning for webkit is somewhat confusing because it's trying to incorporate webkit, gtk and (various linux distribution) versions.
 * The way they version webkitGTK is different from webkit.
 *   WebkitGTK:
 *    2.5 is webkit2.                    [2.4-..)  is Gtk3.
 *  Further, linux distributions might refer to webkit2 bindings linked against gtk3 differently.
 *  E.g on Fedora:
 *     webkitgtk4 = webkit2 / Gtk3
 *     webkit2gtk3 = WebKit2/ Gtk3
 *
 * Webkit2 loading:
 * - Dynamic bindings are auto generated and linked when the @dynamic keyword is used in WebKitGTK.java
 *   Unlike in OS.java, you don't have to add any code saying what lib the dynamic method is linked to. It's auto-linked to webkit lib by default.
 * - At no point should you have hard-compiled code, because this will cause crashes on older machines without webkit2.
 *   (the exception is the webextension, because it runs as a separate process and is only loaded dynamically).
 * - Try to keep all of your logic in Java and avoid writing custom C-code. (I went down this pit). Because if you
 *   use native code, then you have to write dynamic native code (get function pointers, cast types etc.. big pain in the ass).
 * - Don't try to add webkit2 include flags to pkg-config, as this will tie the swt-glue code to specific webkit versions. Thou shall not do this.
 *
 * EVENT_HANDLING_DOC:
 * - On webkit2, signals are implemented via regular gtk mechanism, hook events and pass them along as we receive them.
 *   I haven't found a need to use the dom events, because webkitgtk seems to adequately meet the requirements via regular gtk
 *   events, but maybe I missed something? Who knows.
 *
 * setUrl(..) with 'post data' was implemented in a very hacky way, via native Java due to missing webkit2gtk api.
 * It's the best that could be done at the time, but it could result in strange behavior like some webpages loading in funky ways if post-data is used.
 *
 * Some good resources that I found are as following:
 * - Webkit2 reference: https://webkitgtk.org/reference/webkit2gtk/stable/
 *
 * - To understand GDBus, consider reading this guide:
 *   http://www.cs.grinnell.edu/~rebelsky/Courses/CSC195/2013S/Outlines/
 *   And then see the relevant reference I made in WebkitGDBus.java.
 *   Note, DBus is not the same as GDBus. GDBus is an implementation of the DBus protocol (with it's own quirks).
 *
 * - This is a good starting point for webkit2 extension reading:
 *   https://blogs.igalia.com/carlosgc/2013/09/10/webkit2gtk-web-process-extensions/
 *
 *   [April 2018]
 *   Note on WebKitContext:
 *    We only use a single webcontext, so WebKitGTK.webkit_web_context_get_default() works well for getting this when
 *    needed.
 *
 *
 *
 * ~May the force be with you.
 */
class WebKit extends WebBrowser {
	long webView;
	long pageId;

	int failureCount, lastKeyCode, lastCharCode;

	boolean ignoreDispose;
	boolean tlsError;
	long tlsErrorCertificate;
	String tlsErrorUriString;
	URI tlsErrorUri;
	String tlsErrorType;

	boolean firstLoad = true;
	static boolean FirstCreate = true;

	/**
	 * Stores the browser which is opening a new browser window,
	 * during a WebKit {@code create} signal. This browser
	 * must be passed to a newly created browser as "related".
	 *
	 * See bug 579257.
	 */
	private static Browser parentBrowser;

	/**
	 * Timeout used for javascript execution / deadlock detection.
	 * Loosely based on the 10s limit commonly found in browsers.
	 * (Except for SWT browser we use 3s as chunks of the UI is blocked).
	 * https://www.nczonline.net/blog/2009/01/05/what-determines-that-a-script-is-long-running/
	 * https://stackoverflow.com/questions/3030024/maximum-execution-time-for-javascript
	 */
	static final int ASYNC_EXEC_TIMEOUT_MS = 10000;

	/** Workaround for bug 522733 */
	static boolean bug522733FirstInstanceCreated = false;

	/** Part of workaround in Bug 527738. Prevent old request overring newer request */
	static AtomicInteger w2_bug527738LastRequestCounter = new AtomicInteger();

	/**
	 * Webkit2: In a few situations, evaluate() should not wait for it's asynchronous callback to finish.
	 * This is to avoid deadlocks, see Bug 512001.<br>
	 * 0 means evaluate should wait for callback. <br>
	 * >0 means evaluate should not block. In this case 'null' is returned. This condition is rare. <br>
	 *
	 * <p>Note: This has to be *static*.
	 * Webkit2 seems to share one event queue, as such two webkit2 instances can interfere with each other.
	 * An example of this interfering is when you open a link in a javadoc hover. The new webkit2 in the new tab
	 * interferes with the old instance in the hoverbox.
	 * As such, any locks should apply to all webkit2 instances.</p>
	 */
	private static int nonBlockingEvaluate = 0;

	static Map<LONG, Integer> webKitDownloadStatus = new HashMap<> ();

	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String CLASSNAME_EXTERNAL = "External"; //$NON-NLS-1$
	static final String FUNCTIONNAME_CALLJAVA = "callJava"; //$NON-NLS-1$
	static final String HEADER_CONTENTTYPE = "content-type"; //$NON-NLS-1$
	static final String MIMETYPE_FORMURLENCODED = "application/x-www-form-urlencoded"; //$NON-NLS-1$
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
	static final int SENTINEL_KEYPRESS = -1;
	static final char SEPARATOR_FILE = File.separatorChar;
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

	static final byte[] SWT_PROTOCOL = Converter.wcsToMbcs("swt", true); // $NON-NLS-1$
	static final byte[] JSON_MIME_TYPE = Converter.wcsToMbcs("application/json", true); // $NON-NLS-1$

	/* WebKit signal data */
	static final int NOTIFY_PROGRESS = 1;
	static final int NOTIFY_TITLE = 2;
	static final int CREATE_WEB_VIEW = 3;
	static final int WEB_VIEW_READY = 4;
	static final int CLOSE_WEB_VIEW = 5;
	static final int LOAD_CHANGED = 6;
	static final int DECIDE_POLICY = 7;
	static final int MOUSE_TARGET_CHANGED = 8;
	static final int CONTEXT_MENU = 9;
	static final int AUTHENTICATE = 10;
	static final int DECIDE_DESTINATION = 11;
	static final int FAILED = 12;
	static final int FINISHED = 13;
	static final int DOWNLOAD_STARTED = 14;
	static final int WIDGET_EVENT = 15; // Used for events like keyboard/mouse input. See Bug 528549 and Bug 533833.
	static final int LOAD_FAILED_TLS = 16;

	static final String KEY_CHECK_SUBWINDOW = "org.eclipse.swt.internal.control.checksubwindow"; //$NON-NLS-1$

	static final String SWT_WEBKITGTK_VERSION = "org.eclipse.swt.internal.webkitgtk.version"; //$NON-NLS-1$

	/* the following Callbacks are never freed */
	static Callback Proc2, Proc3, Proc4, Proc5, JSDOMEventProc, RequestProc;

	/** Flag indicating whether TLS errors (like self-signed certificates) are to be ignored. */
	static final boolean ignoreTls;

	static {
			Proc2 = new Callback (WebKit.class, "Proc", 2); //$NON-NLS-1$
			Proc3 = new Callback (WebKit.class, "Proc", 3); //$NON-NLS-1$
			Proc4 = new Callback (WebKit.class, "Proc", 4); //$NON-NLS-1$
			Proc5 = new Callback (WebKit.class, "Proc", 5); //$NON-NLS-1$
			new Webkit2AsyncToSync();

			JSDOMEventProc = new Callback (WebKit.class, "JSDOMEventProc", 3); //$NON-NLS-1$
			RequestProc = new Callback (WebKit.class, "RequestProc", 2); //$NON-NLS-1$

			NativeClearSessions = () -> {
				if (!WebKitGTK.LibraryLoaded) return;
				if (WebKitGTK.webkit_get_minor_version() >= 16) {
					long context = WebKitGTK.webkit_web_context_get_default();
					long manager = WebKitGTK.webkit_web_context_get_website_data_manager (context);
					WebKitGTK.webkit_website_data_manager_clear(manager, WebKitGTK.WEBKIT_WEBSITE_DATA_COOKIES, 0, 0, 0, 0);
				} else {
					System.err.println("SWT WebKit: clear sessions only supported on WebKitGtk version 2.16 and above. "
							+ "Your version is: " + internalGetWebKitVersionStr());
				}
			};

			NativeGetCookie = () -> {
				if (!WebKitGTK.LibraryLoaded) return;
				if (WebKitGTK.webkit_get_minor_version() >= 20) {
					CookieValue = Webkit2AsyncToSync.getCookie(CookieUrl, CookieName);
				} else {
					System.err.println("SWT WebKit: getCookie() only supported on WebKitGTK version 2.20 and above. "
							+ "Your version is: " + internalGetWebKitVersionStr());
				}
			};

			NativeSetCookie = () -> {
				if (!WebKitGTK.LibraryLoaded) return;
				if (WebKitGTK.webkit_get_minor_version() >= 20) {
					CookieResult = Webkit2AsyncToSync.setCookie(CookieUrl, CookieValue);
				} else {
					System.err.println("SWT WebKit: setCookie() only supported on WebKitGTK version 2.20 and above. "
							+ "Your version is: " + internalGetWebKitVersionStr());
				}
			};

			if (NativePendingCookies != null) {
				SetPendingCookies (NativePendingCookies);
				NativePendingCookies = null;
			}
			ignoreTls = "true".equals(System.getProperty("org.eclipse.swt.internal.webkitgtk.ignoretlserrors"));
	}

	@Override
	public void createFunction(BrowserFunction function) {
		super.createFunction(function);
		updateUserScript();
	}

	@Override
	public void destroyFunction (BrowserFunction function) {
		super.destroyFunction(function);
		updateUserScript();
	}

	void updateUserScript() {
		// Maintain a script bundle of BrowserFunctions to be injected on page navigation or reload.
		long manager = WebKitGTK.webkit_web_view_get_user_content_manager(webView);
		WebKitGTK.webkit_user_content_manager_remove_all_scripts(manager);
		if (!functions.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (BrowserFunction function : functions.values()) {
				sb.append(function.functionString);
			}
			sb.append('\0');
			byte[] scriptData = sb.toString().getBytes(StandardCharsets.UTF_8);
			long script = WebKitGTK.webkit_user_script_new(
					scriptData, WebKitGTK.WEBKIT_USER_SCRIPT_INJECT_AT_DOCUMENT_START, WebKitGTK.WEBKIT_USER_CONTENT_INJECT_TOP_FRAME, 0, 0);
			WebKitGTK.webkit_user_content_manager_add_script(manager, script);
			WebKitGTK.webkit_user_script_unref(script);
		}
	}

	private static String getInternalErrorMsg () {
		String reportErrMsg = "Please report this issue *with steps to reproduce* via:\n"
				+ " https://bugs.eclipse.org/bugs/enter_bug.cgi?"
				+ "alias=&assigned_to=platform-swt-inbox%40eclipse.org&attach_text=&blocked=&bug_file_loc=http%3A%2F%2F&bug_severity=normal"
				+ "&bug_status=NEW&comment=&component=SWT&contenttypeentry=&contenttypemethod=autodetect&contenttypeselection=text%2Fplain"
				+ "&data=&defined_groups=1&dependson=&description=&flag_type-1=X&flag_type-11=X&flag_type-12=X&flag_type-13=X&flag_type-14=X"
				+ "&flag_type-15=X&flag_type-16=X&flag_type-2=X&flag_type-4=X&flag_type-6=X&flag_type-7=X&flag_type-8=X&form_name=enter_bug"
				+ "&keywords=&maketemplate=Remember%20values%20as%20bookmarkable%20template&op_sys=Linux&product=Platform&qa_contact="
				+ "&rep_platform=PC&requestee_type-1=&requestee_type-2=&short_desc=webkit2_BrowserProblem";

		return reportErrMsg + "\nFor bug report, please atatch this stack trace:\n" + getStackTrace();
	}


	private static String getStackTrace() {
		// Get a stacktrace. Note, this doesn't actually throw anything, we just get the stacktrace.
		StringWriter sw = new StringWriter();
		new Throwable("").printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	@Override
	String getJavaCallDeclaration() {
		// callJava does a synchronous XMLHttpRequest, which is handled by RequestProc.
		return "if (!window.callJava) { window.callJava = function(index, token, args) {\n"
				+ "var xhr = new XMLHttpRequest();\n"
				+ "var uri = 'swt://browserfunction/' + index + '/' + token + '?' + encodeURIComponent(JSON.stringify(args));\n"
				+ "xhr.open('POST', uri, false);\n"
				+ "xhr.send(null);\n"
				+ "return JSON.parse(xhr.responseText);\n"
				+ "}}\n";
	}

	/**
	 * Gets the webkit version, within an <code>int[3]</code> array with
	 * <code>{major, minor, micro}</code> version
	 */
	private static int[] internalGetWebkitVersion(){
		int [] vers = new int[3];
		vers[0] = WebKitGTK.webkit_get_major_version ();
		vers[1] = WebKitGTK.webkit_get_minor_version ();
		vers[2] = WebKitGTK.webkit_get_micro_version ();
		return vers;
	}

	private static String internalGetWebKitVersionStr () {
		int [] vers = internalGetWebkitVersion();
		return String.valueOf(vers[0]) + "." + String.valueOf(vers[1]) + "." + String.valueOf(vers[2]);
	}


static String getString (long strPtr) {
	int length = C.strlen (strPtr);
	byte [] buffer = new byte [length];
	C.memmove (buffer, strPtr, length);
	return new String (Converter.mbcsToWcs (buffer));
}

static Browser FindBrowser (long webView) {
	if (webView == 0) return null;
	long parent = GTK.gtk_widget_get_parent (webView);
	return (Browser)Display.getCurrent ().findWidget (parent);
}

static boolean IsInstalled () {
	if (!WebKitGTK.LibraryLoaded) return false;
	// TODO webkit_check_version() should take care of the following, but for some
	// reason this symbol is missing from the latest build.  If it is present in
	// Linux distro-provided builds then replace the following with this call.
	int [] vers = internalGetWebkitVersion();
	int major = vers[0], minor = vers[1], micro = vers[2];
	return major > MIN_VERSION[0] ||
		(major == MIN_VERSION[0] && minor > MIN_VERSION[1]) ||
		(major == MIN_VERSION[0] && minor == MIN_VERSION[1] && micro >= MIN_VERSION[2]);
}

static long JSDOMEventProc (long arg0, long event, long user_data) {
	if (user_data == WIDGET_EVENT) {
		/*
		* Only consider using GDK events to create SWT events to send if JS is disabled
		* in one or more WebKit instances (indicates that this instance may not be
		* receiving events from the DOM).  This check is done up-front for performance.
		*/
		final Browser browser = FindBrowser (arg0);
		if (browser != null && user_data == WIDGET_EVENT){
			/* this instance does need to use the GDK event to create an SWT event to send */
			switch (GDK.GDK_EVENT_TYPE (event)) {
				case GDK.GDK_KEY_PRESS: {
					if (browser.isFocusControl ()) {
						int [] key = new int [1];
						int [] state = new int[1];
						if (GTK.GTK4) {
							key[0] = GDK.gdk_key_event_get_keyval(event);
							state[0] = GDK.gdk_event_get_modifier_state(event);
						} else {
							GDK.gdk_event_get_keyval(event, key);
							GDK.gdk_event_get_state(event, state);
						}

						switch (key[0]) {
							case GDK.GDK_ISO_Left_Tab:
							case GDK.GDK_Tab: {
								if ((state[0] & (GDK.GDK_CONTROL_MASK | GDK.GDK_MOD1_MASK)) == 0) {
									browser.getDisplay ().asyncExec (() -> {
										if (browser.isDisposed ()) return;
										if (browser.getDisplay ().getFocusControl () == null) {
											int traversal = (state[0] & GDK.GDK_SHIFT_MASK) != 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
											browser.traverse (traversal);
										}
									});
								}
								break;
							}
							case GDK.GDK_Escape: {
								Event keyEvent = new Event ();
								keyEvent.widget = browser;
								keyEvent.type = SWT.KeyDown;
								keyEvent.keyCode = keyEvent.character = SWT.ESC;
								if ((state[0] & GDK.GDK_MOD1_MASK) != 0) keyEvent.stateMask |= SWT.ALT;
								if ((state[0] & GDK.GDK_SHIFT_MASK) != 0) keyEvent.stateMask |= SWT.SHIFT;
								if ((state[0]& GDK.GDK_CONTROL_MASK) != 0) keyEvent.stateMask |= SWT.CONTROL;
								try { // to avoid deadlocks, evaluate() should not block during listener. See Bug 512001
									  // I.e, evaluate() can be called and script will be executed, but no return value will be provided.
									nonBlockingEvaluate++;
								browser.webBrowser.sendKeyEvent (keyEvent);
								} catch (Exception e) {
									throw e;
								} finally {
									nonBlockingEvaluate--;
								}
								return 1;
							}
						}
					}
					break;
				}
			}
			if (browser != null) {
				GTK3.gtk_widget_event (browser.handle, event);
			}
		}
		return 0;
	}
	return 0;
}

static long RequestProc (long request, long user_data) {
	// Custom protocol handler (swt://) for BrowserFunction callbacks.
	// Note that a response must be sent regardless of any errors, otherwise the caller will hang.
	String response = "null";

	long webView = WebKitGTK.webkit_uri_scheme_request_get_web_view(request);
	Browser browser = FindBrowser(webView);
	if (browser != null) {
		BrowserFunction function = null;
		Object[] args = null;

		long uriPtr = WebKitGTK.webkit_uri_scheme_request_get_uri(request);
		String uriStr = Converter.cCharPtrToJavaString(uriPtr, false);
		try {
			URI uri = new URI(uriStr);
			String[] parts = uri.getPath().split("/");
			int index = Integer.parseInt(parts[1]);
			String token = parts[2];

			WebKit webkit = (WebKit)browser.webBrowser;
			function = webkit.functions.get(index);
			if (function != null && !function.token.equals(token)) {
				function = null;
			}

			args = (Object[]) JSON.parse(uri.getQuery());
		} catch (URISyntaxException | IllegalArgumentException | IndexOutOfBoundsException | ClassCastException e) {
		}

		if (function != null) {
			Object result;
			try {
				result = function.function(args);
			} catch (Exception e) {
				result = WebBrowser.CreateErrorString(e.getLocalizedMessage());
			}
			response = JSON.stringify(result);
		}
	}

	long[] outBytes = new long[1];
	long dataPtr = OS.g_utf16_to_utf8(response.toCharArray(), response.length(), null, outBytes, null);
	long stream = OS.g_memory_input_stream_new_from_data(dataPtr, outBytes[0], OS.addressof_g_free());
	WebKitGTK.webkit_uri_scheme_request_finish(request, stream, outBytes[0], JSON_MIME_TYPE);
	OS.g_object_unref(stream);
	return 0;
}

static long Proc (long handle, long user_data) {
	long webView  = handle;

	if (user_data == FINISHED) {
		// Special case, callback from WebKitDownload instead of webview.
		long webKitDownload = handle;
		return webkit_download_finished(webKitDownload);
	}

	Browser browser = FindBrowser (webView);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	return webkit.webViewProc (handle, user_data);
}

static long Proc (long handle, long arg0, long user_data) {
	// As a note, don't use instance checks like 'G_TYPE_CHECK_INSTANCE_TYPE '
	// to determine difference between webview and webcontext as these
	// don't seem to work reliably for all clients. For some clients they always return true.
	// Instead use user_data.

	{ // Deal with Special cases where callback comes not from webview. Handle is not a webview.

		if (user_data == DOWNLOAD_STARTED) {
			// This callback comes from WebKitWebContext as oppose to the WebView. So handle is WebContext not Webview.
			// user_function (WebKitWebContext *context, WebKitDownload  *download,  gpointer  user_data)
			long webKitDownload = arg0;
			webkit_download_started(webKitDownload);
			return 0;
		}

		if (user_data == DECIDE_DESTINATION) {
			// This callback comes from WebKitDownload, so handle is WebKitDownload not webview.
			// gboolean  user_function (WebKitDownload *download, gchar   *suggested_filename, gpointer  user_data)
			long webKitDownload = handle;
			long suggested_filename = arg0;
			return webkit_download_decide_destination(webKitDownload,suggested_filename);
		}

		if (user_data == FAILED) {
			// void user_function (WebKitDownload *download, GError *error, gpointer user_data)
			long webKitDownload = handle;
			return webkit_download_failed(webKitDownload);
		}
	}

	{ // Callbacks connected with a WebView.
		assert handle != 0 : "Webview shouldn't be null here";
		long webView = handle;
		Browser browser = FindBrowser (webView);
		if (browser == null) return 0;
		WebKit webkit = (WebKit)browser.webBrowser;
		return webkit.webViewProc (webView, arg0, user_data);
	}
}

static long Proc (long handle, long arg0, long arg1, long user_data) {
	Browser browser = FindBrowser (handle);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;
	return webkit.webViewProc (handle, arg0, arg1, user_data);
}

static long Proc (long handle, long arg0, long arg1, long arg2, long user_data) {
	long webView = handle;
	Browser browser = FindBrowser (webView);
	if (browser == null) return 0;
	WebKit webkit = (WebKit)browser.webBrowser;

	return webkit.webViewProc (handle, arg0, arg1, arg2, user_data);
}

/**
 * gboolean user_function (WebKitWebView *web_view, WebKitAuthenticationRequest *request, gpointer user_data)
 * - https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#WebKitWebView-authenticate
 */
long webkit_authenticate (long web_view, long request){

	/* authentication challenges are currently the only notification received from the session */
	if (!WebKitGTK.webkit_authentication_request_is_retry(request)) {
		failureCount = 0;
	} else {
		if (++failureCount >= 3) return 0;
	}

	String location = getUrl();

	for (int i = 0; i < authenticationListeners.length; i++) {
		AuthenticationEvent event = new AuthenticationEvent (browser);
		event.location = location;

		try { // to avoid deadlocks, evaluate() should not block during authentication listener. See Bug 512001
			  // I.e, evaluate() can be called and script will be executed, but no return value will be provided.
			nonBlockingEvaluate++;
			authenticationListeners[i].authenticate (event);
		} catch (Exception e) {
			throw e;
		} finally {
			nonBlockingEvaluate--;
		}

		if (!event.doit) {
			WebKitGTK.webkit_authentication_request_cancel (request);
			return 0;
		}
		if (event.user != null && event.password != null) {
			byte[] userBytes = Converter.wcsToMbcs (event.user, true);
			byte[] passwordBytes = Converter.wcsToMbcs (event.password, true);
			long credentials = WebKitGTK.webkit_credential_new (userBytes, passwordBytes, WebKitGTK.WEBKIT_CREDENTIAL_PERSISTENCE_NONE);
			WebKitGTK.webkit_authentication_request_authenticate(request, credentials);
			WebKitGTK.webkit_credential_free(credentials);
			return 0;
		}
	}
	return 0;
}

long webViewProc (long handle, long user_data) {
	switch ((int)user_data) {
		case CLOSE_WEB_VIEW: return webkit_close_web_view (handle);
		case WEB_VIEW_READY: return webkit_web_view_ready (handle);
		default: return 0;
	}
}

long webViewProc (long handle, long arg0, long user_data) {
	switch ((int)user_data) {
		case CREATE_WEB_VIEW: return webkit_create_web_view (handle, arg0);
		case LOAD_CHANGED: return webkit_load_changed (handle, (int) arg0, user_data);
		case NOTIFY_PROGRESS: return webkit_notify_progress (handle, arg0);
		case NOTIFY_TITLE: return webkit_notify_title (handle, arg0);
		case AUTHENTICATE: return webkit_authenticate (handle, arg0);
		default: return 0;
	}
}

long webViewProc (long handle, long arg0, long arg1, long user_data) {
	switch ((int)user_data) {
		case MOUSE_TARGET_CHANGED: return webkit_mouse_target_changed (handle, arg0, arg1); // Webkit2 only.
		case DECIDE_POLICY: return webkit_decide_policy(handle, arg0, (int)arg1, user_data);
		default: return 0;
	}
}

long webViewProc (long handle, long arg0, long arg1, long arg2, long user_data) {
	switch ((int)user_data) {
		case CONTEXT_MENU: return webkit_context_menu(handle, arg0, arg1, arg2);
		case LOAD_FAILED_TLS: return webkit_load_failed_tls(handle, arg0, arg1, arg2);
		default: return 0;
	}
}

@Override
public void create (Composite parent, int style) {
	int [] vers = internalGetWebkitVersion();
	System.setProperty(SWT_WEBKITGTK_VERSION,
			String.format("%s.%s.%s", vers[0], vers[1], vers[2])); // $NON-NLS-1$
	if (Device.DEBUG) {
		System.out.println(String.format("WebKit version %s.%s.%s", vers[0], vers[1], vers[2])); //$NON-NLS-1$
	}
	/*
	 * Set this Browser instance to Webki2AsyncToSync in order for cookie
	 * functionality to work. See bug 522181.
	 */
	Webkit2AsyncToSync.setCookieBrowser(browser);

	if (FirstCreate) {
		FirstCreate = false;
		// Register the swt:// custom protocol for BrowserFunction calls via XMLHttpRequest
		long context = WebKitGTK.webkit_web_context_get_default();
		WebKitGTK.webkit_web_context_register_uri_scheme(context, SWT_PROTOCOL, RequestProc.getAddress(), 0, 0);
		long security = WebKitGTK.webkit_web_context_get_security_manager(context);
		WebKitGTK.webkit_security_manager_register_uri_scheme_as_secure(security, SWT_PROTOCOL);
	}

	Composite parentShell = parent.getParent();
	Browser parentBrowser = WebKit.parentBrowser;
	if (parentBrowser == null && parentShell != null) {
		Control[] children = parentShell.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Browser) {
				parentBrowser = (Browser) children[i];
				break;
			}
		}
	}

	if (parentBrowser == null) {
		webView = WebKitGTK.webkit_web_view_new();
	} else {
		webView = WebKitGTK.webkit_web_view_new_with_related_view(((WebKit)parentBrowser.webBrowser).webView);
	}

	// Bug 522733 Webkit2 workaround for crash
	//   As of Webkitgtk 2.18, webkitgtk2 crashes if the first instance of webview is not referenced when JVM shuts down.
	//   There is a exit handler that tries to dereference the first instance [which if not referenced]
	//   leads to a crash. This workaround would benefit from deeper investigation (find root cause etc...).
	// [edit] Bug 530678. Note, it seems that as of Webkit2.18, webkit auto-disposes itself if parent get's disposed.
	//        While not directly related, see onDispose() for how to deal with disposal of this.
	if (!bug522733FirstInstanceCreated && vers[0] == 2 && vers[1] >= 18) {
		bug522733FirstInstanceCreated = true;
		OS.g_object_ref(webView);
	}
	if (ignoreTls) {
		WebKitGTK.webkit_web_context_set_tls_errors_policy(WebKitGTK.webkit_web_view_get_context(webView),
				WebKitGTK.WEBKIT_TLS_ERRORS_POLICY_IGNORE);
		System.out.println("***WARNING: WebKitGTK is configured to ignore TLS errors via -Dorg.eclipse.swt.internal.webkitgtk.ignoretlserrors=true .");
		System.out.println("***WARNING: Please use for development purposes only!");
	}

	// Webkit2 Signal Documentation: https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#WebKitWebView--title
	if (GTK.GTK4) {
		OS.swt_fixed_add(browser.handle, webView);
	} else {
		GTK3.gtk_container_add (browser.handle, webView);
	}

	OS.g_signal_connect (webView, WebKitGTK.close, Proc2.getAddress (), CLOSE_WEB_VIEW);
	OS.g_signal_connect (webView, WebKitGTK.ready_to_show, Proc2.getAddress (), WEB_VIEW_READY);
	OS.g_signal_connect (webView, WebKitGTK.decide_policy, Proc4.getAddress (), DECIDE_POLICY);
	OS.g_signal_connect (webView, WebKitGTK.mouse_target_changed, Proc4.getAddress (), MOUSE_TARGET_CHANGED);
	OS.g_signal_connect (webView, WebKitGTK.context_menu, Proc5.getAddress (), CONTEXT_MENU);
	OS.g_signal_connect (webView, WebKitGTK.load_failed_with_tls_errors, Proc5.getAddress (), LOAD_FAILED_TLS);

	// GtkWidget* user_function (WebKitWebView *web_view, WebKitNavigationAction *navigation_action,  gpointer  user_data)
	OS.g_signal_connect (webView, WebKitGTK.create, 						Proc3.getAddress (), CREATE_WEB_VIEW);
	//void user_function (WebKitWebView  *web_view,  WebKitLoadEvent load_event,  gpointer  user_data)
	OS.g_signal_connect (webView, WebKitGTK.load_changed, 					Proc3.getAddress (), LOAD_CHANGED);
	// Property change: of 'estimated-load-progress'   args: webview, pspec
	OS.g_signal_connect (webView, WebKitGTK.notify_estimated_load_progress, Proc3.getAddress (), NOTIFY_PROGRESS);

	// gboolean user_function (WebKitWebView *web_view,  WebKitAuthenticationRequest *request,  gpointer user_data)
	OS.g_signal_connect (webView, WebKitGTK.authenticate, 					Proc3.getAddress (), AUTHENTICATE);

	// (!) Note this one's a 'webContext' signal, not webview. See:
	// https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebContext.html#WebKitWebContext-download-started
	OS.g_signal_connect (WebKitGTK.webkit_web_context_get_default(), WebKitGTK.download_started, Proc3.getAddress (), DOWNLOAD_STARTED);

	GTK.gtk_widget_show (webView);
	GTK.gtk_widget_show (browser.handle);

	// Webview 'title' property
	OS.g_signal_connect (webView, WebKitGTK.notify_title, 						Proc3.getAddress (), NOTIFY_TITLE);

	if (!GTK.GTK4) {
		OS.g_signal_connect (webView, OS.button_press_event, JSDOMEventProc.getAddress (), WIDGET_EVENT);
		OS.g_signal_connect (webView, OS.button_release_event, JSDOMEventProc.getAddress (), WIDGET_EVENT);
		OS.g_signal_connect (webView, OS.focus_in_event, JSDOMEventProc.getAddress (), 	WIDGET_EVENT);
		OS.g_signal_connect (webView, OS.focus_out_event, JSDOMEventProc.getAddress (), WIDGET_EVENT);
	}
	// if connecting any other special gtk event to webkit, add SWT.* to w2_passThroughSwtEvents above.

	this.pageId = WebKitGTK.webkit_web_view_get_page_id (webView);

	if (!GTK.GTK4) {
		OS.g_signal_connect (webView, OS.key_press_event, JSDOMEventProc.getAddress (),  	WIDGET_EVENT);
		OS.g_signal_connect (webView, OS.key_release_event, JSDOMEventProc.getAddress (),	WIDGET_EVENT);
		OS.g_signal_connect (webView, OS.scroll_event, JSDOMEventProc.getAddress (), 		WIDGET_EVENT);
		OS.g_signal_connect (webView, OS.motion_notify_event, JSDOMEventProc.getAddress (), WIDGET_EVENT);
	}

	byte[] utfBytes = Converter.wcsToMbcs ("UTF-8", true); // $NON-NLS-1$

	long settings = WebKitGTK.webkit_web_view_get_settings (webView);
	OS.g_object_set (settings, WebKitGTK.javascript_can_open_windows_automatically, 1, 0);
	OS.g_object_set (settings, WebKitGTK.enable_webgl, 1, 0);
	OS.g_object_set (settings, WebKitGTK.enable_developer_extras, 1, 0);

	OS.g_object_set (settings, WebKitGTK.default_charset, utfBytes, 0);
	if (WebKitGTK.webkit_get_minor_version() >= 14) {
		OS.g_object_set (settings, WebKitGTK.allow_universal_access_from_file_urls, 1, 0);
		if (WebKitGTK.webkit_get_minor_version() >= 24) {
			OS.g_object_set (settings, WebKitGTK.enable_back_forward_navigation_gestures, 1, 0);
		}
	} else {
		System.err.println("SWT WEBKIT: Warning, you are using Webkitgtk below version 2.14. Your version is: "
				+ "Your version is: " + internalGetWebKitVersionStr()
				+ "\nJavascript execution limited to same origin due to unimplemented feature of this version.");
	}

	Listener listener = event -> {
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
				if (webView != 0)
					GTK.gtk_widget_grab_focus (webView);
				break;
			}
			case SWT.Resize: {
				onResize (event);
				break;
			}
		}
	};
	browser.addListener (SWT.Dispose, listener);
	browser.addListener (SWT.FocusIn, listener);
	browser.addListener (SWT.KeyDown, listener);
	browser.addListener (SWT.Resize, listener);

	/*
	* Bug in WebKitGTK.  MouseOver/MouseLeave events are not consistently sent from
	* the DOM when the mouse enters and exits the browser control, see
	* https://bugs.webkit.org/show_bug.cgi?id=35246.  As a workaround for sending
	* MouseEnter/MouseExit events, swt's default mouse enter/exit mechanism is used,
	* but in order to do this the Browser's default sub-window check behavior must
	* be changed.
	*/
	browser.setData (KEY_CHECK_SUBWINDOW, Boolean.FALSE);

	/*
	 * Bug in WebKitGTK.  In WebKitGTK 1.10.x a crash can occur if an
	 * attempt is made to show a browser before a size has been set on
	 * it.  The workaround is to temporarily give it a size that forces
	 * the native resize events to fire.
	 */
	int major = vers[0], minor = vers[1];
	if (major == 1 && minor >= 10) {
		Rectangle minSize = browser.computeTrim (0, 0, 2, 2);
		Point size = browser.getSize ();
		size.x += minSize.width; size.y += minSize.height;
		browser.setSize (size);
		size.x -= minSize.width; size.y -= minSize.height;
		browser.setSize (size);
	}
}

@Override
public boolean back () {
	if (WebKitGTK.webkit_web_view_can_go_back (webView) == 0) return false;
	WebKitGTK.webkit_web_view_go_back (webView);
	return true;
}

@Override
public boolean close () {
	return close (true);
}

// Developer note:
// @return true = leads to disposal. In Browser.java, user is told widget is disposed. Ex in Snippe326 close button is grayed out.
//         false = blocks disposal. In Browser.java, user is told widget was not disposed.
// See Snippet326.
boolean close (boolean showPrompters) {
	// don't execute any JavaScript if it's disabled or requested to get disabled
	// we need to check jsEnabledOnNextPage here because jsEnabled is updated asynchronously
	// and may not reflect the proper state (bug 571746 and bug 567881)
	if (!jsEnabled || !jsEnabledOnNextPage) return true;

	String message1 = Compatibility.getMessage("SWT_OnBeforeUnload_Message1"); // $NON-NLS-1$
	String message2 = Compatibility.getMessage("SWT_OnBeforeUnload_Message2"); // $NON-NLS-1$
	String functionName = EXECUTE_ID + "CLOSE"; // $NON-NLS-1$
	StringBuilder buffer = new StringBuilder ("function "); // $NON-NLS-1$
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
	nonBlockingExecute (buffer.toString ());

	Boolean result;
	/*
	 * Sometimes if a disposal is already underway (ex parent shell disposed), then
	 * Javascript execution can throw. We have to account for that.
	 */
	try {
		result = (Boolean)evaluate ("return " + functionName +"(window);"); // $NON-NLS-1$ // $NON-NLS-2$
		if (result == null) return true; // Default to assume that webkit is disposed and allow disposal of Browser.
	} catch (SWTException e) {
		return true; // Permit browser to be disposed if javascript execution failed.
	}
	return result.booleanValue ();
}


private boolean isJavascriptEnabled() {
	// If you try to run Javascript while Javascript is turned off, then an exception is thrown.
	return webkit_settings_get(WebKitGTK.enable_javascript) != 0;
}

@Override
void nonBlockingExecute(String script) {
	try {
		nonBlockingEvaluate++;
		execute(script);
	} finally {
		nonBlockingEvaluate--;
	}
}

@Override
public boolean execute (String script) {
	if (!isJavascriptEnabled()) {
		System.err.println("SWT Webkit Warning: Attempting to execute javascript when javascript is dissabled."
				+ "Execution has no effect. Script:\n" + script);
		return false;
	}
	try {
		Webkit2AsyncToSync.runjavascript(script, this.browser, webView);
	} catch (SWTException e) {
		return false;
	}
	return true;
}

/**
 * Webkit2 introduces async api. However SWT has sync execution model. This class it to convert async api to sync.
 *
 * Be careful about using these methods in synchronous callbacks from webkit, as those can cause deadlocks. (See inner javadocs).
 *
 * The mechanism generates an ID for each callback and waits for that callback to complete.
 */
private static class Webkit2AsyncToSync {
	/** We need a way to associate a Browser instance with this class for cookie functionality */
	private static Browser cookieBrowser;
	private static Callback runjavascript_callback;
	private static Callback getText_callback;
	private static Callback setCookie_callback;
	private static Callback getCookie_callback;

	static {
		runjavascript_callback = new Callback(Webkit2AsyncToSync.class, "runjavascript_callback", void.class, new Type[] {long.class, long.class, long.class});
		getText_callback = new Callback(Webkit2AsyncToSync.class, "getText_callback", void.class, new Type[] {long.class, long.class, long.class});
		setCookie_callback = new Callback(Webkit2AsyncToSync.class, "setCookie_callback", void.class, new Type[] {long.class, long.class, long.class});
		getCookie_callback = new Callback(Webkit2AsyncToSync.class, "getCookie_callback", void.class, new Type[] {long.class, long.class, long.class});
	}

	/** Object used to return data from callback to original call */
	private static class Webkit2AsyncReturnObj {
		boolean callbackFinished = false;
		Object returnValue = null; // As note, if browser is disposed during excution, null is returned.

		/** 0=no error. >0 means error. **/
		int errorNum = 0;
		String errorMsg;

		/** Set to true if call timed out. Not set by javascript execution itself */
		boolean swtAsyncTimeout;
	}

	/**
	 * Every callback is tagged with a unique ID.
	 * The ID is used for the callback to find the object via which data is returned
	 * and allow the original call to finish.
	 *
	 * Note: The reason each callback is tagged with an ID is because two(or more) subsequent
	 * evaluate() calls can be started before the first callback comes back.
	 * As such, there would be ambiguity as to which call a callback belongs to, which in turn causes deadlocks.
	 * This is typically seen when a webkit2 signal (e.g closeListener) makes a call to evaluate(),
	 * when the closeListener was triggered by evaluate("window.close()").
	 * An example test case where this is seen is:
	 * org.eclipse.swt.tests.junit.Test_org_eclipse_swt_browser_Browser.test_execute_and_closeListener()
	 */
	private static class CallBackMap {
		private static HashMap<Integer, Webkit2AsyncReturnObj> callbackMap = new HashMap<>();

		static int putObject(Webkit2AsyncReturnObj obj) {
			int id = getNextId();
			callbackMap.put(id, obj);
			return id;
		}
		static Webkit2AsyncReturnObj getObj(int id) {
			return callbackMap.get(id);
		}
		static void removeObject(int id) {
			callbackMap.remove(id);
			removeId(id);
		}

		// Mechanism to generate unique ID's
		private static int nextCallbackId = 1;
		private static HashSet<Integer> usedCallbackIds = new HashSet<>();
		static int getNextId() {
			int value = 0;
			boolean unique = false;
			while (unique == false) {
				value = nextCallbackId;
				unique = !usedCallbackIds.contains(value);
				if (nextCallbackId != Integer.MAX_VALUE)
					nextCallbackId++;
				else
					nextCallbackId = 1;
			}
			usedCallbackIds.add(value);
			return value;
		}
		private static void removeId(int id) {
			usedCallbackIds.remove(id);
		}
	}

	static Object evaluate (String script, Browser browser, long webView)  {
//		/* Wrap script around a temporary function for backwards compatibility,
//		 * user can specify 'return', which may not be at the beginning of the script.
//		 *  Valid scripts:
//		 *      'hi'
//		 *  	return 'hi'
//		 *  	var x = 1; return 'hi'
//		 */
		String swtUniqueExecFunc = "SWTWebkit2TempFunc" + CallBackMap.getNextId() + "()";
		String wrappedScript = "function " + swtUniqueExecFunc +"{" + script + "}; " + swtUniqueExecFunc;
		return runjavascript(wrappedScript, browser, webView);

	}

	/**
	 * Run javascript, wait for a return value.
	 *
	 * Developer note:
	 * Be EXTRA careful with this method, it can cause deadlocks in situations where
	 * javascript is executed in a callback that provides a return value to webkit.
	 * In otherwords, if webkit does a sync callback (one that requires a return value),
	 * then running javascript will lead to a deadlock because webkit will not execute
	 * the javascript until it's sync callback finished.
	 * As a note, SWT's callback mechanism hard-codes 'long' return even when a callback
	 * is actually 'void'. So reference webkit callback signature documentation and not
	 * SWT implementation.
	 *
	 * If in doubt, you should use nonBlockingExecute() where possible :-).
	 */
	static Object runjavascript(String script, Browser browser, long webView) {
		if (nonBlockingEvaluate > 0) {
			// Execute script, but do not wait for async call to complete. (assume it does). Bug 512001.
			WebKitGTK.webkit_web_view_run_javascript(webView, Converter.wcsToMbcs(script, true), 0, 0, 0);
			return null;
		} else {
			// Callback logic: Initiate an async callback and wait for it to finish.
			// The callback comes back in runjavascript_callback(..) below.
			Consumer <Integer> asyncFunc = (callbackId) ->
				WebKitGTK.webkit_web_view_run_javascript(webView, Converter.wcsToMbcs(script, true), 0, runjavascript_callback.getAddress(), callbackId);

			Webkit2AsyncReturnObj retObj = execAsyncAndWaitForReturn(browser, asyncFunc, " The following javascript was executed:\n" + script +"\n\n");

			if (retObj.swtAsyncTimeout) {
				return null;
			} else if (retObj.errorNum != 0) {
				throw new SWTException(retObj.errorNum, retObj.errorMsg +"\nScript that was evaluated:\n" + script);
			} else {
				// This is also the implicit case where browser was disposed while javascript was executing. It returns null.
				return retObj.returnValue;
			}
		}
	}

	@SuppressWarnings("unused") // Only called directly from C (from javascript).
	private static void runjavascript_callback (long GObject_source, long GAsyncResult, long user_data) {
		int callbackId = (int) user_data;
		Webkit2AsyncReturnObj retObj = CallBackMap.getObj(callbackId);

		if (retObj != null) { // retObj can be null if there was a timeout.
			long [] gerror = new long [1]; // GError **
			long js_result = WebKitGTK.webkit_web_view_run_javascript_finish(GObject_source, GAsyncResult, gerror);
			if (js_result == 0) {
				long errMsg = OS.g_error_get_message(gerror[0]);
				String msg = Converter.cCharPtrToJavaString(errMsg, false);
				OS.g_error_free(gerror[0]);

				retObj.errorNum = SWT.ERROR_FAILED_EVALUATE;
				retObj.errorMsg = msg != null ? msg : "";
			} else {
				long context = WebKitGTK.webkit_javascript_result_get_global_context (js_result);
				long value = WebKitGTK.webkit_javascript_result_get_value (js_result);

				try {
					retObj.returnValue = convertToJava(context, value);
				} catch (IllegalArgumentException ex) {
					retObj.errorNum = SWT.ERROR_INVALID_RETURN_VALUE;
					retObj.errorMsg = "Type of return value not is not valid. For supported types see: Browser.evaluate() JavaDoc";
				}
				WebKitGTK.webkit_javascript_result_unref (js_result);
			}
			retObj.callbackFinished = true;
		}
		Display.getCurrent().wake();
	}

	static String getText(Browser browser, long webView) {
		long WebKitWebResource = WebKitGTK.webkit_web_view_get_main_resource(webView);
		if (WebKitWebResource == 0) { // No page yet loaded.
			return "";
		}
		if (nonBlockingEvaluate > 0) {
			System.err.println("SWT Webkit Warning: getText() called inside a synchronous callback, which can lead to a deadlock.\n"
					+ "Avoid using getText in OpenWindowListener, Authentication listener and when webkit is about to change to a new page\n"
					+ "Return value is empty string '' instead of actual text");
			return "";
		}

		Consumer<Integer> asyncFunc = (callbackId) -> WebKitGTK.webkit_web_resource_get_data(WebKitWebResource, 0, getText_callback.getAddress(), callbackId);
		Webkit2AsyncReturnObj retObj = execAsyncAndWaitForReturn(browser, asyncFunc, " getText() was called");

		if (retObj.swtAsyncTimeout)
			return "SWT WEBKIT TIMEOUT ERROR";
		else
			return (String) retObj.returnValue;
	}

	@SuppressWarnings("unused") // Callback only called only by C directly
	private static void getText_callback(long WebResource, long GAsyncResult, long user_data) {
		int callbackId = (int) user_data;
		Webkit2AsyncReturnObj retObj = CallBackMap.getObj(callbackId);

		long [] gsize_len = new long [1];
		long [] gerrorRes = new long [1]; // GError **
		long guchar_data = WebKitGTK.webkit_web_resource_get_data_finish(WebResource, GAsyncResult, gsize_len, gerrorRes);
		if (gerrorRes[0] != 0 || guchar_data == 0) {
			OS.g_error_free(gerrorRes[0]);
			retObj.returnValue = (String) "";
		} else {
			int len = (int) gsize_len[0];
			byte[] buffer = new byte [len];
			C.memmove (buffer, guchar_data, len);
			String text = Converter.byteToStringViaHeuristic(buffer);
			retObj.returnValue = text;
		}

		retObj.callbackFinished = true;
		Display.getCurrent().wake();
	}

	/**
	 * Associates a Browser instance with this class, mainly so we can get its Display
	 * and check for disposal.
	 * @param toSet the Browser instance to set
	 */
	static void setCookieBrowser (Browser toSet) {
		if (toSet != null) cookieBrowser = toSet;
	}

	static boolean setCookie(String cookieUrl, String cookieValue) {
		long context = WebKitGTK.webkit_web_context_get_default();
		long cookieManager = WebKitGTK.webkit_web_context_get_cookie_manager(context);
		byte[] bytes = Converter.wcsToMbcs (cookieUrl, true);
		long uri = WebKitGTK.soup_uri_new (bytes);
		if (uri == 0) {
			System.err.println("SWT WebKit: SoupURI == 0 when setting cookie");
			return false;
		}
		bytes = Converter.wcsToMbcs (cookieValue, true);
		long soupCookie = WebKitGTK.soup_cookie_parse (bytes, uri);

		if (nonBlockingEvaluate > 0) {
			System.err.println("SWT Webkit: setCookie() called inside a synchronous callback, which can lead to a deadlock.\n"
					+ "Return value is false.");
			return false;
		}

		Consumer<Integer> asyncFunc = (callbackID) -> WebKitGTK.webkit_cookie_manager_add_cookie(cookieManager, soupCookie, 0,
				setCookie_callback.getAddress(), callbackID);
		Webkit2AsyncReturnObj retObj = execAsyncAndWaitForReturn(cookieBrowser, asyncFunc, " setCookie() was called");

		WebKitGTK.soup_uri_free (uri);

		if (retObj.swtAsyncTimeout) {
			return false;
		} else {
			return (Boolean) retObj.returnValue;
		}
	}

	@SuppressWarnings("unused") // Callback only called only by C directly
	private static void setCookie_callback(long cookieManager, long result, long user_data) {
		int callbackID = (int) user_data;
		Webkit2AsyncReturnObj retObj = CallBackMap.getObj(callbackID);

		long [] error = new long [1];
		retObj.returnValue = WebKitGTK.webkit_cookie_manager_add_cookie_finish(cookieManager, result, error);

		if (error[0] != 0) {
			long errorMessageC = OS.g_error_get_message(error[0]);
			String errorMessageStr = Converter.cCharPtrToJavaString(errorMessageC, false);
			System.err.println("SWT WebKit: error setting cookie: " + errorMessageStr);
			OS.g_error_free(error[0]);
		}

		retObj.callbackFinished = true;
		Display.getCurrent().wake();

	}

	static String getCookie(String cookieUrl, String cookieName) {
		long context = WebKitGTK.webkit_web_context_get_default();
		long cookieManager = WebKitGTK.webkit_web_context_get_cookie_manager(context);
		byte[] uri = Converter.wcsToMbcs (cookieUrl, true);
		if (nonBlockingEvaluate > 0) {
			System.err.println("SWT Webkit: getCookie() called inside a synchronous callback, which can lead to a deadlock.\n"
					+ "Return value is an empty string '' instead of actual cookie value.");
			return "";
		}

		/*
		 * We package the cookie name and callbackID into a GVariant which can be passed to the callback.
		 * The callbackID is necessary so we can find our way back to the correct Browser instance, and
		 * the cookie name is necessary as the field could have been modified by the time the callback
		 * triggers.
		 */
		Consumer<Integer> asyncFunc = (callbackID) -> WebKitGTK.webkit_cookie_manager_get_cookies(cookieManager, uri, 0,
				getCookie_callback.getAddress(), GDBus.convertJavaToGVariant(new Object [] {cookieName, callbackID}));
		Webkit2AsyncReturnObj retObj = execAsyncAndWaitForReturn(cookieBrowser, asyncFunc, " getCookie() was called");

		if (retObj.swtAsyncTimeout) {
			return "SWT WEBKIT TIMEOUT ERROR";
		} else {
			return (String) retObj.returnValue;
		}
	}

	@SuppressWarnings("unused") // Callback only called only by C directly
	private static void getCookie_callback(long cookieManager, long result, long user_data) {
		Object resultObject = GDBus.convertGVariantToJava(user_data);

		// We are expecting a GVariant tuple, anything else means something went wrong
		if (resultObject instanceof Object []) {
			// Unpack callback ID and cookie name
			Object [] nameAndId = (Object []) resultObject;
			String cookieName = (String) nameAndId[0];
			int callbackId = ((Number) nameAndId[1]).intValue();
			Webkit2AsyncReturnObj retObj = CallBackMap.getObj(callbackId);

			// Get GSList of cookies
			long [] error = new long [1];
			long cookieList = WebKitGTK.webkit_cookie_manager_get_cookies_finish(cookieManager, result, error);
			if (error[0] != 0) {
				long errorMessageC = OS.g_error_get_message(error[0]);
				String errorMessageStr = Converter.cCharPtrToJavaString(errorMessageC, false);
				System.err.println("SWT WebKit: error getting cookie: " + errorMessageStr);
				OS.g_error_free(error[0]);
				retObj.returnValue = (String) "";
			}

			int length = OS.g_slist_length (cookieList);
			long current = cookieList;
			for (int i = 0; i < length; i++) {
				long soupCookie = OS.g_slist_data (current);
				long soupName = WebKitGTK.soup_cookie_get_name(soupCookie);
				String soupNameStr = Converter.cCharPtrToJavaString(soupName, false);
				if (soupNameStr != null && soupNameStr.equals(cookieName)) {
					long soupValue = WebKitGTK.soup_cookie_get_value(soupCookie);
					retObj.returnValue = Converter.cCharPtrToJavaString(soupValue, false);
					break;
				}
				current = OS.g_slist_next (current);
			}
			OS.g_slist_free (cookieList);

			retObj.callbackFinished = true;
			Display.getCurrent().wake();
		} else {
			System.err.println("SWT WebKit: something went wrong unpacking GVariant tuple for getCookie_callback");
		}
	}

	/**
	 * You should check 'retObj.swtAsyncTimeout' after making a call to this.
	 */
	private static Webkit2AsyncReturnObj execAsyncAndWaitForReturn(Browser browser, Consumer<Integer> asyncFunc, String additionalErrorInfo) {
		Webkit2AsyncReturnObj retObj = new Webkit2AsyncReturnObj();
		int callbackId = CallBackMap.putObject(retObj);
		asyncFunc.accept(callbackId);
		final Instant timeOut = Instant.now().plusMillis(ASYNC_EXEC_TIMEOUT_MS);
		while (!browser.isDisposed()) {
			if (retObj.callbackFinished)
				break;
			else if (Instant.now().isAfter(timeOut)) {
				System.err.println("SWT call to Webkit timed out after " + ASYNC_EXEC_TIMEOUT_MS
						+ "ms. No return value will be provided.\n"
						+ "Possible reasons:\n"
						+ "1) Problem: Your javascript needs more than " + ASYNC_EXEC_TIMEOUT_MS +"ms to execute.\n"
						+ "   Solution: Don't run such javascript, it blocks Eclipse's UI. SWT currently allows such code to complete, but this error is thrown \n"
						+ "     and the return value of execute()/evalute() will be false/null.\n\n"
						+ "2) However, if you believe that your application should execute as expected (in under" + ASYNC_EXEC_TIMEOUT_MS + " ms),\n"
						+ " then it might be a deadlock in SWT/Browser/webkit2 logic.\n"
						+ " I.e, it might be a bug in SWT (e.g this does not occur on Windows/Cocoa, but occurs on Linux). If you believe it to be a bug in SWT, then\n"
						+ getInternalErrorMsg()
						+ "\n Additional information about the error is as following:\n"
						+ additionalErrorInfo);
				retObj.swtAsyncTimeout = true;
				break;
			}
			else {
				if (GTK.GTK4) {
					OS.g_main_context_iteration (0, true);
				} else {
					GTK3.gtk_main_iteration_do (true);
				}
			}
		}
		CallBackMap.removeObject(callbackId);
		return retObj;
	}
}

@Override
public Object evaluate (String script) throws SWTException {
	if ("".equals(script)) {
		return null; // A litte optimization. Sometimes evaluate() is called with a generated script, where the generated script is sometimes empty.
	}
	if (!isJavascriptEnabled()) {
		return null;
	}
	return Webkit2AsyncToSync.evaluate(script, this.browser, webView);
}

@Override
public boolean forward () {
	if (webView == 0) {
		assert false;
		System.err.println("SWT Webkit: forward() called after widget disposed. Should not have happened.\n" + getInternalErrorMsg());
		return false; // Disposed.
	}
	if (WebKitGTK.webkit_web_view_can_go_forward (webView) == 0) return false;
	WebKitGTK.webkit_web_view_go_forward (webView);
	return true;
}

@Override
public String getBrowserType () {
	return "webkit"; //$NON-NLS-1$
}

@Override
public String getText () {
	return Webkit2AsyncToSync.getText(browser, webView);
}

@Override
public String getUrl () {
	if (webView == 0) {
		assert false;
		System.err.println("SWT Webkit: getUrl() called after widget disposed. Should not have happened.\n" + getInternalErrorMsg());
		return null; // Disposed.
	}
	long uri = WebKitGTK.webkit_web_view_get_uri (webView);

	/* WebKit auto-navigates to about:blank at startup */
	if (uri == 0) return ABOUT_BLANK;

	int length = C.strlen (uri);
	byte[] bytes = new byte[length];
	C.memmove (bytes, uri, length);

	String url = new String (Converter.mbcsToWcs (bytes));
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

boolean handleDOMEvent (long event, int type) {
	/*
	* This method handles JS events that are received through the DOM
	* listener API that was introduced in WebKitGTK 1.4.
	*/
	String typeString = null;
	boolean isMouseEvent = false;
	switch (type) {
		case SWT.DragDetect: {
			typeString = "dragstart"; //$NON-NLS-1$
			isMouseEvent = true;
			break;
		}
		case SWT.MouseDown: {
			typeString = "mousedown"; //$NON-NLS-1$
			isMouseEvent = true;
			break;
		}
		case SWT.MouseMove: {
			typeString = "mousemove"; //$NON-NLS-1$
			isMouseEvent = true;
			break;
		}
		case SWT.MouseUp: {
			typeString = "mouseup"; //$NON-NLS-1$
			isMouseEvent = true;
			break;
		}
		case SWT.MouseWheel: {
			typeString = "mousewheel"; //$NON-NLS-1$
			isMouseEvent = true;
			break;
		}
		case SWT.KeyDown: {
			typeString = "keydown"; //$NON-NLS-1$
			break;
		}
		case SWT.KeyUp: {
			typeString = "keyup"; //$NON-NLS-1$
			break;
		}
		case SENTINEL_KEYPRESS: {
			typeString = "keypress"; //$NON-NLS-1$
			break;
		}
	}

	if (isMouseEvent) {
		int screenX = (int)WebKitGTK.webkit_dom_mouse_event_get_screen_x (event);
		int screenY = (int)WebKitGTK.webkit_dom_mouse_event_get_screen_y (event);
		int button = (int)WebKitGTK.webkit_dom_mouse_event_get_button (event) + 1;
		boolean altKey = WebKitGTK.webkit_dom_mouse_event_get_alt_key (event) != 0;
		boolean ctrlKey = WebKitGTK.webkit_dom_mouse_event_get_ctrl_key (event) != 0;
		boolean shiftKey = WebKitGTK.webkit_dom_mouse_event_get_shift_key (event) != 0;
		boolean metaKey = WebKitGTK.webkit_dom_mouse_event_get_meta_key (event) != 0;
		int detail = (int)WebKitGTK.webkit_dom_ui_event_get_detail (event);
		boolean hasRelatedTarget = false; //WebKitGTK.webkit_dom_mouse_event_get_related_target (event) != 0;
		return handleMouseEvent(typeString, screenX, screenY, detail, button, altKey, ctrlKey, shiftKey, metaKey, hasRelatedTarget);
	}

	/* key event */
	int keyEventState = 0;
	long eventPtr = GTK3.gtk_get_current_event ();
	if (eventPtr != 0) {
		int eventType = GDK.gdk_event_get_event_type(eventPtr);
		int [] state = new int[1];
		if (GTK.GTK4) {
			state[0] = GDK.gdk_event_get_modifier_state(eventPtr);
		} else {
			GDK.gdk_event_get_state(eventPtr, state);
		}
		switch (eventType) {
			case GDK.GDK_KEY_PRESS:
			case GDK.GDK_KEY_RELEASE:
				keyEventState = state[0];
				break;
		}
		if (GTK.GTK4) {
			OS.g_object_unref(eventPtr);
		} else {
			GDK.gdk_event_free (eventPtr);
		}
	}
	int keyCode = (int)WebKitGTK.webkit_dom_ui_event_get_key_code (event);
	int charCode = (int)WebKitGTK.webkit_dom_ui_event_get_char_code (event);
	boolean altKey = (keyEventState & GDK.GDK_MOD1_MASK) != 0;
	boolean ctrlKey = (keyEventState & GDK.GDK_CONTROL_MASK) != 0;
	boolean shiftKey = (keyEventState & GDK.GDK_SHIFT_MASK) != 0;
	return handleKeyEvent(typeString, keyCode, charCode, altKey, ctrlKey, shiftKey, false);
}

boolean handleEventFromFunction (Object[] arguments) {
	/*
	* Prior to WebKitGTK 1.4 there was no API for hooking DOM listeners.
	* As a workaround, eventFunction was introduced to capture JS events
	* and report them back to the java side.  This method handles these
	* events by extracting their arguments and passing them to the
	* handleKeyEvent()/handleMouseEvent() event handler methods.
	*/

	/*
	* The arguments for key events are:
	* 	argument 0: type (String)
	* 	argument 1: keyCode (Double)
	* 	argument 2: charCode (Double)
	* 	argument 3: altKey (Boolean)
	* 	argument 4: ctrlKey (Boolean)
	* 	argument 5: shiftKey (Boolean)
	* 	argument 6: metaKey (Boolean)
	* 	returns doit
	*
	* The arguments for mouse events are:
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
	if (type.equals (DOMEVENT_KEYDOWN) || type.equals (DOMEVENT_KEYPRESS) || type.equals (DOMEVENT_KEYUP)) {
		return handleKeyEvent(
			type,
			((Double)arguments[1]).intValue (),
			((Double)arguments[2]).intValue (),
			((Boolean)arguments[3]).booleanValue (),
			((Boolean)arguments[4]).booleanValue (),
			((Boolean)arguments[5]).booleanValue (),
			((Boolean)arguments[6]).booleanValue ());
	}

	return handleMouseEvent(
		type,
		((Double)arguments[1]).intValue (),
		((Double)arguments[2]).intValue (),
		((Double)arguments[3]).intValue (),
		(arguments[4] != null ? ((Double)arguments[4]).intValue () : 0) + 1,
		((Boolean)arguments[5]).booleanValue (),
		((Boolean)arguments[6]).booleanValue (),
		((Boolean)arguments[7]).booleanValue (),
		((Boolean)arguments[8]).booleanValue (),
		((Boolean)arguments[9]).booleanValue ());
}

boolean handleKeyEvent (String type, int keyCode, int charCode, boolean altKey, boolean ctrlKey, boolean shiftKey, boolean metaKey) {
	if (type.equals (DOMEVENT_KEYDOWN)) {
		keyCode = translateKey (keyCode);
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
				keyEvent.stateMask = (altKey ? SWT.ALT : 0) | (ctrlKey ? SWT.CTRL : 0) | (shiftKey ? SWT.SHIFT : 0) | (metaKey ? SWT.COMMAND : 0);
				keyEvent.stateMask &= ~keyCode;		/* remove current keydown if it's a state key */
				final int stateMask = keyEvent.stateMask;
				if (!sendKeyEvent (keyEvent) || browser.isDisposed ()) return false;

				if (browser.isFocusControl ()) {
					if (keyCode == SWT.TAB && (stateMask & (SWT.CTRL | SWT.ALT)) == 0) {
						browser.getDisplay ().asyncExec (() -> {
							if (browser.isDisposed ()) return;
							if (browser.getDisplay ().getFocusControl () == null) {
								int traversal = (stateMask & SWT.SHIFT) != 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
								browser.traverse (traversal);
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

		lastCharCode = charCode;
		if (ctrlKey && (0 <= lastCharCode && lastCharCode <= 0x7F)) {
			if ('a' <= lastCharCode && lastCharCode <= 'z') lastCharCode -= 'a' - 'A';
			if (64 <= lastCharCode && lastCharCode <= 95) lastCharCode -= 64;
		}

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyDown;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.character = (char)lastCharCode;
		keyEvent.stateMask = (altKey ? SWT.ALT : 0) | (ctrlKey ? SWT.CTRL : 0) | (shiftKey ? SWT.SHIFT : 0) | (metaKey ? SWT.COMMAND : 0);
		return sendKeyEvent (keyEvent) && !browser.isDisposed ();
	}

	/* keyup */

	keyCode = translateKey (keyCode);
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
	keyEvent.stateMask = (altKey ? SWT.ALT : 0) | (ctrlKey ? SWT.CTRL : 0) | (shiftKey ? SWT.SHIFT : 0) | (metaKey ? SWT.COMMAND : 0);
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

boolean handleMouseEvent (String type, int screenX, int screenY, int detail, int button, boolean altKey, boolean ctrlKey, boolean shiftKey, boolean metaKey, boolean hasRelatedTarget) {
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
	Point position = new Point (screenX, screenY);
	position = browser.getDisplay ().map (null, browser, position);

	Event mouseEvent = new Event ();
	mouseEvent.widget = browser;
	mouseEvent.x = position.x;
	mouseEvent.y = position.y;
	int mask = (altKey ? SWT.ALT : 0) | (ctrlKey ? SWT.CTRL : 0) | (shiftKey ? SWT.SHIFT : 0) | (metaKey ? SWT.COMMAND : 0);
	mouseEvent.stateMask = mask;

	if (type.equals (DOMEVENT_MOUSEDOWN)) {
		mouseEvent.type = SWT.MouseDown;
		mouseEvent.count = detail;
		mouseEvent.button = button;
		browser.notifyListeners (mouseEvent.type, mouseEvent);
		if (browser.isDisposed ()) return true;
		if (detail == 2) {
			mouseEvent = new Event ();
			mouseEvent.type = SWT.MouseDoubleClick;
			mouseEvent.widget = browser;
			mouseEvent.x = position.x;
			mouseEvent.y = position.y;
			mouseEvent.stateMask = mask;
			mouseEvent.count = detail;
			mouseEvent.button = button;
			browser.notifyListeners (mouseEvent.type, mouseEvent);
		}
		return true;
	}

	if (type.equals (DOMEVENT_MOUSEUP)) {
		mouseEvent.type = SWT.MouseUp;
		mouseEvent.count = detail;
		mouseEvent.button = button;
	} else if (type.equals (DOMEVENT_MOUSEMOVE)) {
		mouseEvent.type = SWT.MouseMove;
	} else if (type.equals (DOMEVENT_MOUSEWHEEL)) {
		mouseEvent.type = SWT.MouseWheel;
		mouseEvent.count = detail;

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
		mouseEvent.button = button;
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

long handleLoadCommitted (long uri, boolean top) {
	int length = C.strlen (uri);
	byte[] bytes = new byte[length];
	C.memmove (bytes, uri, length);
	String url = new String (Converter.mbcsToWcs (bytes));
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

	LocationEvent event = new LocationEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	event.location = url;
	event.top = top;
	Runnable fireLocationChanged = () ->  {
		if (browser.isDisposed ()) return;
		for (int i = 0; i < locationListeners.length; i++) {
			locationListeners[i].changed (event);
		}
	};
	browser.getDisplay().asyncExec(fireLocationChanged);
	return 0;
}

/**
 * This method is reached by:
 * Webkit2: WebKitWebView load-changed signal
 * 	- void user_function (WebKitWebView  *web_view, WebKitLoadEvent load_event, gpointer user_data)
 *  - https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#WebKitWebView-load-changed
 *  - Note: As there is no return value, safe to fire asynchronously.
 */
private void fireProgressCompletedEvent(){
	Runnable fireProgressEvents = () -> {
		if (browser.isDisposed() || progressListeners == null) return;
		ProgressEvent progress = new ProgressEvent (browser);
		progress.display = browser.getDisplay ();
		progress.widget = browser;
		progress.current = MAX_PROGRESS;
		progress.total = MAX_PROGRESS;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].completed (progress);
		}
	};
	browser.getDisplay().asyncExec(fireProgressEvents);
}

@Override
public boolean isBackEnabled () {
	if (webView == 0)
		return false; //disposed.
	return WebKitGTK.webkit_web_view_can_go_back (webView) != 0;
}

@Override
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

	for (BrowserFunction function : functions.values()) {
		function.dispose(false);
	}
	functions = null;

	if (WebKitGTK.webkit_get_minor_version() >= 18) {
		// Bug 530678.
		// * As of Webkit 2.18, (it seems) webkitGtk auto-disposes itself when the parent is disposed.
		// * This can cause a deadlock inside Webkit process if WebkitGTK widget's parent is disposed during a callback.
		//   This is because webkit process is waiting for it's callback to finish which never completes
		//   because parent's disposal also disposed webkitGTK widget. (Note Webkit process vs WebkitGtk widget).
		// * To break the deadlock, we unparent webkitGtk temporarily and unref (dispose) it later after callback is done.
		//
		// If you change dispose logic, to check that you haven't introduced memory leaks, test via:
		// org.eclipse.swt.tests.junit.memoryleak.Test_Memory_Leak.test_Browser()
		OS.g_object_ref (webView);
		if(GTK.GTK4) {
			GTK.gtk_widget_unparent(webView);
		} else {
			GTK3.gtk_container_remove (GTK.gtk_widget_get_parent (webView), webView);
		}
		long webViewTempRef = webView;
		Display.getDefault().asyncExec(() -> OS.g_object_unref(webViewTempRef));
		webView = 0;
	}
}

void onResize (Event e) {
	Rectangle rect = DPIUtil.autoScaleUp(browser.getClientArea ());
	if (webView == 0)
		return;
	GTK.gtk_widget_set_size_request (webView, rect.width, rect.height);
}

void openDownloadWindow (final long webkitDownload, final String suggested_filename) {
	final Shell shell = new Shell ();
	String msg = Compatibility.getMessage ("SWT_FileDownload"); //$NON-NLS-1$
	shell.setText (msg);
	GridLayout gridLayout = new GridLayout ();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout (gridLayout);

	String nameString = suggested_filename;

	long request = WebKitGTK.webkit_download_get_request(webkitDownload);
	long url = WebKitGTK.webkit_uri_request_get_uri(request);

	int length = C.strlen (url);
	byte[] bytes = new byte[length];
	C.memmove (bytes, url, length);
	String urlString = new String (Converter.mbcsToWcs (bytes));
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
	final Listener cancelListener = event -> {
		webKitDownloadStatus.put(new LONG(webkitDownload), WebKitGTK.WEBKIT_DOWNLOAD_STATUS_CANCELLED);
		WebKitGTK.webkit_download_cancel (webkitDownload);
	};
	cancel.addListener (SWT.Selection, cancelListener);

	OS.g_object_ref (webkitDownload);
	final Display display = browser.getDisplay ();
	final int INTERVAL = 500;
	display.timerExec (INTERVAL, new Runnable () {
		@Override
		public void run () {
			int status =  webKitDownloadStatus.containsKey(new LONG(webkitDownload)) ? webKitDownloadStatus.get(new LONG(webkitDownload)) : 0;
			if (shell.isDisposed () || status == WebKitGTK.WEBKIT_DOWNLOAD_STATUS_FINISHED || status == WebKitGTK.WEBKIT_DOWNLOAD_STATUS_CANCELLED) {
				shell.dispose ();
				display.timerExec (-1, this);
				OS.g_object_unref (webkitDownload);
				webKitDownloadStatus.remove(new LONG(webkitDownload));
				return;
			}
			if (status == WebKitGTK.WEBKIT_DOWNLOAD_STATUS_ERROR) {
				statusLabel.setText (Compatibility.getMessage ("SWT_Download_Error")); //$NON-NLS-1$
				display.timerExec (-1, this);
				OS.g_object_unref (webkitDownload);
				cancel.removeListener (SWT.Selection, cancelListener);
				cancel.addListener (SWT.Selection, event -> shell.dispose ());
				webKitDownloadStatus.remove(new LONG(webkitDownload));
				return;
			}

			long current = WebKitGTK.webkit_download_get_received_data_length(webkitDownload) / 1024L;
			long response = WebKitGTK.webkit_download_get_response(webkitDownload);
			long total = WebKitGTK.webkit_uri_response_get_content_length(response) / 1024L;
			String message = Compatibility.getMessage ("SWT_Download_Status", new Object[] {current, total}); //$NON-NLS-1$
			statusLabel.setText (message);
			display.timerExec (INTERVAL, this);
		}
	});

	shell.pack ();
	shell.open ();
}

@Override
public void refresh () {
	if (webView == 0)
		return; //disposed.
	WebKitGTK.webkit_web_view_reload (webView);
}

@Override
public boolean setText (String html, boolean trusted) {
	/* convert the String containing HTML to an array of bytes with UTF-8 data */
	byte[] html_bytes = (html + '\0').getBytes (StandardCharsets.UTF_8); //$NON-NLS-1$

	w2_bug527738LastRequestCounter.incrementAndGet();
	byte[] uriBytes;
	if (!trusted) {
		uriBytes = Converter.wcsToMbcs (ABOUT_BLANK, true);
	} else {
		uriBytes = Converter.wcsToMbcs (URI_FILEROOT, true);
	}
	WebKitGTK.webkit_web_view_load_html (webView, html_bytes, uriBytes);

	return true;
}

@Override
public boolean setUrl (String url, String postData, String[] headers) {
	w2_bug527738LastRequestCounter.incrementAndGet();

	if (webView == 0)
		return false; // disposed.

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
	long settings = WebKitGTK.webkit_web_view_get_settings (webView);
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
							byte[] bytes = Converter.wcsToMbcs (value, true);
							OS.g_object_set (settings, WebKitGTK.user_agent, bytes, 0);
						}
					}
				}
			}
		}
	}

	byte[] uriBytes = Converter.wcsToMbcs (url, true);

	if (postData==null && headers != null) {
		long request = WebKitGTK.webkit_uri_request_new (uriBytes);
		long requestHeaders = WebKitGTK.webkit_uri_request_get_http_headers (request);
		if (requestHeaders != 0) {
			addRequestHeaders(requestHeaders, headers);
		}
		WebKitGTK.webkit_web_view_load_request (webView, request);
		OS.g_object_set (settings, WebKitGTK.user_agent, 0, 0);
		return true;
	}

	// Bug 527738
	// Webkit2 doesn't have api to set url with data. (2.18). While we wait for them to implement,
	// this  workaround uses java to query a server and then manually populate webkit with content.
	// This should be version guarded and replaced with proper functions once webkit2 has implemented api.
	if (postData != null) {
		final String base_url = url;

		// Use Webkit User-Agent
		long [] user_agent_str_ptr = new long [1];
		OS.g_object_get (settings, WebKitGTK.user_agent, user_agent_str_ptr, 0);
		final String userAgent = Converter.cCharPtrToJavaString(user_agent_str_ptr[0], true);
		final int lastRequest = w2_bug527738LastRequestCounter.incrementAndGet(); // Webkit 2 only
		Thread send_request = new Thread(() -> {
			String html = null;
			String mime_type = null;
			String encoding_type = null;
			try {
				URL base = new URL(base_url);
				URLConnection url_conn = base.openConnection();
				if (url_conn instanceof HttpURLConnection) {
					HttpURLConnection conn = (HttpURLConnection) url_conn;

					{ // Configure connection.
						conn.setRequestMethod("POST"); //$NON-NLS-1$

						// Use Webkit Accept
						conn.setRequestProperty( "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"); //$NON-NLS-1$ $NON-NLS-2$

						conn.setRequestProperty("User-Agent", userAgent); //$NON-NLS-1$
						conn.setDoOutput(true); // because default value is false

						// Set headers
						if (headers != null) {
							for (String header : headers) {
								int index = header.indexOf(':');
								if (index > 0) {
									String key = header.substring(0, index).trim();
									String value = header.substring(index + 1).trim();
									conn.setRequestProperty(key, value);
								}
							}
						}
					}

					{ // Query server
						try (OutputStream out = conn.getOutputStream()) {
							out.write(postData.getBytes());
						}

						StringBuilder response = new StringBuilder();
						try (BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
							char [] cbuff = new char[4096];
							while (buff.read(cbuff, 0, cbuff.length) > 0) {
								response.append(new String(cbuff));
								Arrays.fill(cbuff, '\0');
							}
						}
						html = response.toString();
					}

					{ // Extract result meta data
						// Get Media Type from Content-Type
						String content_type = conn.getContentType();
						int paramaterSeparatorIndex = content_type.indexOf(';');
						mime_type = paramaterSeparatorIndex > 0 ? content_type.substring(0, paramaterSeparatorIndex) : content_type;

						// Get Encoding if defined
						if (content_type.indexOf(';') > 0) {
							String [] attrs = content_type.split(";");
							for (String attr : attrs) {
								int i = attr.indexOf('=');
								if (i > 0) {
									String key = attr.substring(0, i).trim();
									String value = attr.substring(i + 1).trim();
									if ("charset".equalsIgnoreCase(key)) { //$NON-NLS-1$
										encoding_type = value;
									}
								}
							}
						}
					}
				}
			} catch (IOException e) { // MalformedURLException is an IOException also.
				html = e.getMessage();
			} finally {
				if (html != null && lastRequest == w2_bug527738LastRequestCounter.get()) {
					final String final_html = html;
					final String final_mime_type = mime_type;
					final String final_encoding_type = encoding_type;
					Display.getDefault().syncExec(() -> {
						byte [] html_bytes = Converter.wcsToMbcs(final_html, false);
						byte [] mime_type_bytes = final_mime_type != null ? Converter.javaStringToCString(final_mime_type) : Converter.javaStringToCString("text/plain");
						byte [] encoding_bytes = final_encoding_type != null ? Converter.wcsToMbcs(final_encoding_type, true) : new byte [] {0};
						long gByte = OS.g_bytes_new(html_bytes, html_bytes.length);
						WebKitGTK.webkit_web_view_load_bytes (webView, gByte, mime_type_bytes, encoding_bytes, uriBytes);
						OS.g_bytes_unref (gByte); // as per glib/tests/keyfile:test_bytes()..
						OS.g_object_set (settings, WebKitGTK.user_agent, 0, 0);
					});
				}
			}
		});
		send_request.start();
	} else {
		WebKitGTK.webkit_web_view_load_uri (webView, uriBytes);
	}

	if (postData == null) {
		OS.g_object_set (settings, WebKitGTK.user_agent, 0, 0);
	}
	return true;
}

@Override
public void stop () {
	WebKitGTK.webkit_web_view_stop_loading (webView);
}

/**
 * WebKitWebView 'close' signal
 * void user_function (WebKitWebView *web_view, gpointer user_data); // observe *no* return value.
 * https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#WebKitWebView-close
 */
long webkit_close_web_view (long web_view) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	Runnable fireCloseWindowListeners = () -> {
		if (browser.isDisposed()) return;
		for (int i = 0; i < closeWindowListeners.length; i++) {
			closeWindowListeners[i].close (newEvent);
		}
		browser.dispose ();
	};
	// On WebKit2 this signal doesn't expect a return value.
	// As such, we can safley execute the SWT listeners later to avoid deadlocks. See bug 512001
	browser.getDisplay().asyncExec(fireCloseWindowListeners);
	return 0;
}

long webkit_create_web_view (long web_view, long frame) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	newEvent.required = true;
	Runnable fireOpenWindowListeners = () -> {
		if (openWindowListeners != null) {
			for (int i = 0; i < openWindowListeners.length; i++) {
				openWindowListeners[i].open (newEvent);
			}
		}
	};
	try {
		nonBlockingEvaluate++; 	  // running evaluate() inside openWindowListener and waiting for return leads to deadlock. Bug 512001
		parentBrowser = browser;
		fireOpenWindowListeners.run();// Permit evaluate()/execute() to execute scripts in listener, but do not provide return value.
	} catch (Exception e) {
		throw e; // rethrow execption if thrown, but decrement counter first.
	} finally {
		parentBrowser = null;
		nonBlockingEvaluate--;
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

static long webkit_download_started(long webKitDownload) {
	OS.g_signal_connect(webKitDownload, WebKitGTK.decide_destination, Proc3.getAddress(), DECIDE_DESTINATION);
	OS.g_signal_connect(webKitDownload, WebKitGTK.failed, Proc3.getAddress(), FAILED);
	OS.g_signal_connect(webKitDownload, WebKitGTK.finished, Proc2.getAddress(), FINISHED);
	return 1;
}


static long webkit_download_decide_destination(long webKitDownload, long suggested_filename) {
	final String fileName = getString(suggested_filename);
	long webView = WebKitGTK.webkit_download_get_web_view(webKitDownload);
	if (webView != 0) {
		Browser browser = FindBrowser (webView);
		if (browser == null || browser.isDisposed() || browser.isClosing) return 0;

		FileDialog dialog = new FileDialog (browser.getShell (), SWT.SAVE);
		dialog.setFileName (fileName);
		String title = Compatibility.getMessage ("SWT_FileDownload"); //$NON-NLS-1$
		dialog.setText (title);
		String path = dialog.open ();
		if (path != null) {
			path = URI_FILEROOT + path;
			byte[] uriBytes = Converter.wcsToMbcs (path, true);

			if (WebKitGTK.webkit_get_minor_version() >= 6) {
				WebKitGTK.webkit_download_set_allow_overwrite (webKitDownload, true);
			}
			WebKitGTK.webkit_download_set_destination (webKitDownload, uriBytes);
			((WebKit)browser.webBrowser).openDownloadWindow(webKitDownload, fileName);
		}
	}
	return 0;
}

static long webkit_download_finished(long download) {
	// A failed signal may have been recorded prior. The finish signal is now being called.
	if (!webKitDownloadStatus.containsKey(new LONG(download))) {
		webKitDownloadStatus.put(new LONG(download), WebKitGTK.WEBKIT_DOWNLOAD_STATUS_FINISHED);
	}
	return 0;
}

static long webkit_download_failed(long download) {
	// A cancel may have been issued resulting in this signal call. Preserve the original cause.
	if (!webKitDownloadStatus.containsKey(new LONG(download))) {
		webKitDownloadStatus.put(new LONG(download), WebKitGTK.WEBKIT_DOWNLOAD_STATUS_ERROR);
	}
	return 0;
}

/**
 * WebkitWebView mouse-target-changed
 * - void user_function (WebKitWebView *web_view, WebKitHitTestResult *hit_test_result, guint modifiers, gpointer user_data)
 * - https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#WebKitWebView-mouse-target-changed
 * */
long webkit_mouse_target_changed (long web_view, long hit_test_result, long modifiers) {
	if (WebKitGTK.webkit_hit_test_result_context_is_link(hit_test_result)){
		long uri = WebKitGTK.webkit_hit_test_result_get_link_uri(hit_test_result);
		long title = WebKitGTK.webkit_hit_test_result_get_link_title(hit_test_result);
		return webkit_hovering_over_link(web_view, title, uri);
	}

	return 0;
}

/**
 * Webkit2: WebkitWebView mouse-target-change
 * - Normally this signal is called for many different events, e.g hoveing over an image.
 *   But in our case, in webkit_mouse_target_changed() we filter out everything except mouse_over_link events.
 *
 *   Since there is no return value, it is safe to run asynchronously.
 */
long webkit_hovering_over_link (long web_view, long title, long uri) {
	if (uri != 0) {
		int length = C.strlen (uri);
		byte[] bytes = new byte[length];
		C.memmove (bytes, uri, length);
		String text = new String (Converter.mbcsToWcs (bytes));
		StatusTextEvent event = new StatusTextEvent (browser);
		event.display = browser.getDisplay ();
		event.widget = browser;
		event.text = text;
		Runnable fireStatusTextListener = () -> {
			if (browser.isDisposed() || statusTextListeners == null) return;
			for (int i = 0; i < statusTextListeners.length; i++) {
				statusTextListeners[i].changed (event);
			}
		};
		browser.getDisplay().asyncExec(fireStatusTextListener);
	}
	return 0;
}

long webkit_decide_policy (long web_view, long decision, int decision_type, long user_data) {
	switch (decision_type) {
	case WebKitGTK.WEBKIT_POLICY_DECISION_TYPE_NAVIGATION_ACTION:
		long request = WebKitGTK. webkit_navigation_policy_decision_get_request(decision);
		if (request == 0){
			return 0;
		}
		long uri = WebKitGTK.webkit_uri_request_get_uri (request);
		String url = getString(uri);
		/*
		* If the URI indicates that the page is being rendered from memory
		* (via setText()) then set it to about:blank to be consistent with IE.
		*/
		if (url.equals (URI_FILEROOT)) {
			url = ABOUT_BLANK;
		} else {
			int length = URI_FILEROOT.length ();
			if (url.startsWith (URI_FILEROOT) && url.charAt (length) == '#') {
				url = ABOUT_BLANK + url.substring (length);
			}
		}

		LocationEvent newEvent = new LocationEvent (browser);
		newEvent.display = browser.getDisplay ();
		newEvent.widget = browser;
		newEvent.location = url;
		newEvent.doit = true;

		try {
			nonBlockingEvaluate++;
			if (locationListeners != null) {
				for (int i = 0; i < locationListeners.length; i++) {
					locationListeners[i].changing (newEvent);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			nonBlockingEvaluate--;
		}

		if (newEvent.doit && !browser.isDisposed ()) {
			if (jsEnabled != jsEnabledOnNextPage) {
				jsEnabled = jsEnabledOnNextPage;
				webkit_settings_set(WebKitGTK.enable_javascript, jsEnabled ? 1 : 0);
			}
		}
		if(!newEvent.doit){
			WebKitGTK.webkit_policy_decision_ignore (decision);
		}
		break;
	case WebKitGTK.WEBKIT_POLICY_DECISION_TYPE_NEW_WINDOW_ACTION:
		break;
	case WebKitGTK.WEBKIT_POLICY_DECISION_TYPE_RESPONSE:
		long response = WebKitGTK.webkit_response_policy_decision_get_response(decision);
		long mime_type = WebKitGTK.webkit_uri_response_get_mime_type(response);
		boolean canShow = WebKitGTK.webkit_web_view_can_show_mime_type (webView, mime_type) != 0;
		if (!canShow) {
			WebKitGTK.webkit_policy_decision_download (decision);
			return 1;
		}
		break;
	default:
		/* Making no decision results in webkit_policy_decision_use(). */
		return 0;
	}
	return 0;
}

long webkit_load_changed (long web_view, int status, long user_data) {
	switch (status) {
		case WebKitGTK.WEBKIT2_LOAD_COMMITTED: {
			long uri = WebKitGTK.webkit_web_view_get_uri (webView);
			return handleLoadCommitted (uri, true);
		}
		case WebKitGTK.WEBKIT2_LOAD_FINISHED: {
			if (firstLoad) {
				GtkAllocation allocation = new GtkAllocation ();
				GTK.gtk_widget_get_allocation(browser.handle, allocation);
				if (GTK.GTK4) {
					GTK4.gtk_widget_size_allocate (browser.handle, allocation, -1);
				} else {
					GTK3.gtk_widget_size_allocate(browser.handle, allocation);
				}
				firstLoad = false;
			}

			fireProgressCompletedEvent();

			/*
			 * If there is a pending TLS error, handle it by prompting the user for input.
			 * This is done by popping up a message box and asking if the user would like
			 * ignore warnings for this host. Clicking yes will do so, clicking no will
			 * load the previous page.
			 *
			 *  Not applicable if the ignoreTls flag has been set. See bug 531341.
			 */
			if (tlsError && !ignoreTls) {
				tlsError = false;
				String javaHost = tlsErrorUri.getHost();
				MessageBox prompt = new MessageBox (browser.getShell(), SWT.YES | SWT.NO);
				prompt.setText(SWT.getMessage("SWT_InvalidCert_Title"));
				String specific = tlsErrorType.isEmpty() ? "\n\n" : "\n\n" + tlsErrorType + "\n\n";
				String message = SWT.getMessage("SWT_InvalidCert_Message", new Object[] {javaHost}) +
						specific + SWT.getMessage("SWT_InvalidCert_Connect");
				prompt.setMessage(message);
				int result = prompt.open();
				if (result == SWT.YES) {
					long webkitcontext = WebKitGTK.webkit_web_view_get_context(web_view);
					if (javaHost != null) {
						byte [] host = Converter.javaStringToCString(javaHost);
						WebKitGTK.webkit_web_context_allow_tls_certificate_for_host(webkitcontext, tlsErrorCertificate, host);
						WebKitGTK.webkit_web_view_reload (web_view);
					} else {
						System.err.println("***ERROR: Unable to parse host from URI!");
					}
				} else {
					back();
				}
				// De-reference Webkit certificate so it can be freed
				if (tlsErrorCertificate != 0) {
					OS.g_object_unref (tlsErrorCertificate);
					tlsErrorCertificate = 0;
				}
			}

			return 0;
		}
	}
	return 0;
}

/**
 * Called in cases where a web page failed to load due to TLS errors
 * (self-signed certificates, as an example).
 */
long webkit_load_failed_tls (long web_view, long failing_uri, long certificate, long error) {
	if (!ignoreTls) {
		// Set tlsError flag so that the user can be prompted once this "bad" page has finished loading
		tlsError = true;
		OS.g_object_ref(certificate);
		tlsErrorCertificate = certificate;
		convertUri (failing_uri);
		switch ((int)error) {
			case WebKitGTK.G_TLS_CERTIFICATE_UNKNOWN_CA: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_UnknownCA");
				break;
			}
			case WebKitGTK.G_TLS_CERTIFICATE_BAD_IDENTITY: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_BadIdentity");
				break;
			}
			case WebKitGTK.G_TLS_CERTIFICATE_NOT_ACTIVATED: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_NotActivated");
				break;
			}
			case WebKitGTK.G_TLS_CERTIFICATE_EXPIRED: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_Expired");
				break;
			}
			case WebKitGTK.G_TLS_CERTIFICATE_REVOKED: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_Revoked");
				break;
			}
			case WebKitGTK.G_TLS_CERTIFICATE_INSECURE: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_Insecure");
				break;
			}
			case WebKitGTK.G_TLS_CERTIFICATE_GENERIC_ERROR: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_GenericError");
				break;
			}
			case WebKitGTK.G_TLS_CERTIFICATE_VALIDATE_ALL: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_ValidateAll");
				break;
			}
			default: {
				tlsErrorType = SWT.getMessage("SWT_InvalidCert_GenericError");
				break;
			}
		}
	}
	return 0;
}

/**
 * Converts a WebKit URI into a Java URI object.
 *
 * @param webkitUri a long pointing to the URI in C string form (gchar *)
 * @throws URISyntaxException if the string violates RFC 2396, or is otherwise
 * malformed
 */
void convertUri (long webkitUri) {
	try {
		tlsErrorUriString = Converter.cCharPtrToJavaString(webkitUri, false);
		tlsErrorUri = new URI (tlsErrorUriString);
	} catch (URISyntaxException e) {
		System.err.println("***ERROR: Malformed URI from WebKit!");
		return;
	}
}

/**
 * Triggered by a change in property. (both gdouble[0,1])
 * Webkit2: WebkitWebview notify::estimated-load-progress
 *  https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#WebKitWebView--estimated-load-progress
 *
 *  No return value required. Thus safe to run asynchronously.
 */
long webkit_notify_progress (long web_view, long pspec) {
	ProgressEvent event = new ProgressEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	double progress = 0;
	progress = WebKitGTK.webkit_web_view_get_estimated_load_progress (webView);
	event.current = (int) (progress * MAX_PROGRESS);
	event.total = MAX_PROGRESS;
	Runnable fireProgressChangedEvents = () -> {
		if (browser.isDisposed() || progressListeners == null) return;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].changed (event);
		}
	};
	browser.getDisplay().asyncExec(fireProgressChangedEvents);
	return 0;
}

/**
 * Triggerd by webkit's 'notify::title' signal and forwarded to this function.
 * The signal doesn't have documentation (2.15.4), but is mentioned here:
 * https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#webkit-web-view-get-title
 *
 * It doesn't look it would require a return value, so running in asyncExec should be fine.
 */
long webkit_notify_title (long web_view, long pspec) {
	long title = WebKitGTK.webkit_web_view_get_title (webView);
	String titleString;
	if (title == 0) {
		titleString = ""; //$NON-NLS-1$
	} else {
		int length = C.strlen (title);
		byte[] bytes = new byte[length];
		C.memmove (bytes, title, length);
		titleString = new String (Converter.mbcsToWcs (bytes));
	}
	TitleEvent event = new TitleEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	event.title = titleString;
	Runnable fireTitleListener = () -> {
		for (int i = 0; i < titleListeners.length; i++) {
			titleListeners[i].changed (event);
		}
	};
	browser.getDisplay().asyncExec(fireTitleListener);
	return 0;
}

long webkit_context_menu (long web_view, long context_menu, long eventXXX, long hit_test_result) {
	Point pt = browser.getDisplay ().getCursorLocation (); // might break on Wayland? Wouldn't hurt to verify.
	Event event = new Event ();
	event.x = pt.x;
	event.y = pt.y;
	browser.notifyListeners (SWT.MenuDetect, event);
	if (!event.doit) {
		// Do not display the menu
		return 1;
	}

	Menu menu = browser.getMenu ();
	if (menu != null && !menu.isDisposed ()) {
		if (pt.x != event.x || pt.y != event.y) {
			menu.setLocation (event.x, event.y);
		}
		menu.setVisible (true);
		// Do not display the webkit menu
		return 1;
	}
	return 0;
}

private void addRequestHeaders(long requestHeaders, String[] headers){
	for (int i = 0; i < headers.length; i++) {
		String current = headers[i];
		if (current != null) {
			int index = current.indexOf (':');
			if (index != -1) {
				String key = current.substring (0, index).trim ();
				String value = current.substring (index + 1).trim ();
				if (key.length () > 0 && value.length () > 0) {
					byte[] nameBytes = Converter.wcsToMbcs (key, true);
					byte[] valueBytes = Converter.wcsToMbcs (value, true);
					WebKitGTK.soup_message_headers_append (requestHeaders, nameBytes, valueBytes);
				}
			}
		}
	}

}

/**
 * Emitted after "create" on the newly created WebKitWebView when it should be displayed to the user.
 * Webkit2 signal: ready-to-show
 *   https://webkitgtk.org/reference/webkitgtk/unstable/webkitgtk-webkitwebview.html#WebKitWebView-web-view-ready
 * Note in webkit2, no return value has to be provided in callback.
 */
long webkit_web_view_ready (long web_view) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;

	long properties = WebKitGTK.webkit_web_view_get_window_properties(webView);
	newEvent.addressBar = webkit_settings_get(properties, WebKitGTK.locationbar_visible) != 0;
	newEvent.menuBar = webkit_settings_get(properties, WebKitGTK.menubar_visible) != 0;
	newEvent.statusBar = webkit_settings_get(properties, WebKitGTK.statusbar_visible) != 0;
	newEvent.toolBar = webkit_settings_get(properties, WebKitGTK.toolbar_visible) != 0;

	GdkRectangle rect = new GdkRectangle();
	WebKitGTK.webkit_window_properties_get_geometry(properties, rect);
	newEvent.location = new Point(Math.max(0, rect.x),Math.max(0, rect.y));

	int width = rect.width;
	int height = rect.height;
	if (height == 100 && width == 100) {
		// On Webkit2, if no height/width is specified, then minimum (which is 100) is allocated to popus.
		// This makes popups very small.
		// For better cross-platform consistency (Win/Cocoa/Gtk), we give more reasonable defaults (2/3 the size of a screen).
		Rectangle primaryMonitorBounds = browser.getDisplay ().getPrimaryMonitor().getBounds();
		height = (int) (primaryMonitorBounds.height * 0.66);
		width = (int) (primaryMonitorBounds.width * 0.66);
	}
	newEvent.size = new Point(width, height);

	Runnable fireVisibilityListeners = () -> {
		if (browser.isDisposed()) return;
		for (int i = 0; i < visibilityWindowListeners.length; i++) {
			visibilityWindowListeners[i].show (newEvent);
		}
	};
	// Postpone execution of listener, to avoid deadlocks in case evaluate() is
	// called in the listener while another signal is being handled. See bug 512001.
	// evaluate() can safely be called in this listener with no adverse effects.
	browser.getDisplay().asyncExec(fireVisibilityListeners);
	return 0;
}

/**
 * @return An integer value for the property is returned. For boolean settings, 0 indicates false,
 * 1 indicates true. -1= is error.
 */
private int webkit_settings_get(byte [] property) {
	if (webView == 0) { // already disposed.
		return -1; // error.
	}
	long settings = WebKitGTK.webkit_web_view_get_settings (webView);
	return webkit_settings_get(settings, property);
}

/** @return An integer value for the property is returned. For boolean settings, 0 indicates false, 1 indicates true */
private int webkit_settings_get(long settings, byte[] property) {
	int[] result = new int[1];
	OS.g_object_get (settings, property, result, 0);
	return result[0];
}

private void webkit_settings_set(byte [] property, int value) {
	if (webView == 0) { // already disposed.
		return;
	}
	long settings = WebKitGTK.webkit_web_view_get_settings (webView);
	OS.g_object_set(settings, property, value, 0);
}

static Object convertToJava (long ctx, long value) {
	int type = WebKitGTK.JSValueGetType (ctx, value);
	switch (type) {
		case WebKitGTK.kJSTypeBoolean: {
			int result = (int)WebKitGTK.JSValueToNumber (ctx, value, null);
			return result != 0;
		}
		case WebKitGTK.kJSTypeNumber: {
			double result = WebKitGTK.JSValueToNumber (ctx, value, null);
			return Double.valueOf(result);
		}
		case WebKitGTK.kJSTypeString: {
			long string = WebKitGTK.JSValueToStringCopy (ctx, value, null);
			if (string == 0) return ""; //$NON-NLS-1$
			long length = WebKitGTK.JSStringGetMaximumUTF8CStringSize (string);
			byte[] bytes = new byte[(int)length];
			length = WebKitGTK.JSStringGetUTF8CString (string, bytes, length);
			WebKitGTK.JSStringRelease (string);
			/* length-1 is needed below to exclude the terminator character */
			return new String (bytes, 0, (int)length - 1, StandardCharsets.UTF_8);
		}
		case WebKitGTK.kJSTypeNull:
			// FALL THROUGH
		case WebKitGTK.kJSTypeUndefined: return null;
		case WebKitGTK.kJSTypeObject: {
			byte[] bytes = (PROPERTY_LENGTH + '\0').getBytes (StandardCharsets.UTF_8); //$NON-NLS-1$
			long propertyName = WebKitGTK.JSStringCreateWithUTF8CString (bytes);
			long valuePtr = WebKitGTK.JSObjectGetProperty (ctx, value, propertyName, null);
			WebKitGTK.JSStringRelease (propertyName);
			type = WebKitGTK.JSValueGetType (ctx, valuePtr);
			if (type == WebKitGTK.kJSTypeNumber) {
				int length = (int)WebKitGTK.JSValueToNumber (ctx, valuePtr, null);
				Object[] result = new Object[length];
				for (int i = 0; i < length; i++) {
					long current = WebKitGTK.JSObjectGetPropertyAtIndex (ctx, value, i, null);
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

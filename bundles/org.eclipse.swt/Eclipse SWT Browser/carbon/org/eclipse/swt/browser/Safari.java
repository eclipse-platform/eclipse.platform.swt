/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.util.Enumeration;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class Safari extends WebBrowser {
	
	/* Objective-C WebView delegate */
	int delegate;
	
	/* Carbon HIView handle */
	int webViewHandle, webView;
	int windowBoundsHandler;
	int preferences;
	
	boolean changingLocation, hasNewFocusElement;
	String lastHoveredLinkURL, lastNavigateURL;
	String html;
	int identifier;
	int resourceCount;
	int lastMouseMoveX, lastMouseMoveY;
	String url = ""; //$NON-NLS-1$
	Point location;
	Point size;
	boolean statusBar = true, toolBar = true, ignoreDispose;
	//TEMPORARY CODE
//	boolean doit;

	static boolean Initialized;
	static Callback Callback3, Callback7;

	static final int MIN_SIZE = 16;
	static final int MAX_PROGRESS = 100;
	static final String WebElementLinkURLKey = "WebElementLinkURL"; //$NON-NLS-1$
	static final String AGENT_STRING = "Safari/412.0"; /* Safari version on OSX 10.4 initial release */ //$NON-NLS-1$
	static final String URI_FROMMEMORY = "file:///"; //$NON-NLS-1$
	static final String PROTOCOL_FILE = "file://"; //$NON-NLS-1$
	static final String PROTOCOL_HTTP = "http://"; //$NON-NLS-1$
	static final String URI_APPLEWEBDATA = "applewebdata://"; //$NON-NLS-1$
	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String HEADER_SETCOOKIE = "Set-Cookie"; //$NON-NLS-1$
	static final String ADD_WIDGET_KEY = "org.eclipse.swt.internal.addWidget"; //$NON-NLS-1$
	static final String BROWSER_WINDOW = "org.eclipse.swt.browser.Browser.Window"; //$NON-NLS-1$
	static final String SAFARI_EVENTS_FIX_KEY = "org.eclipse.swt.internal.safariEventsFix"; //$NON-NLS-1$

	/* event strings */
	static final String DOMEVENT_KEYUP = "keyup"; //$NON-NLS-1$
	static final String DOMEVENT_KEYDOWN = "keydown"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEDOWN = "mousedown"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEUP = "mouseup"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEMOVE = "mousemove"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEWHEEL = "mousewheel"; //$NON-NLS-1$
	static final String DOMEVENT_FOCUSIN = "DOMFocusIn"; //$NON-NLS-1$
	static final String DOMEVENT_FOCUSOUT = "DOMFocusOut"; //$NON-NLS-1$

	static {
		NativeClearSessions = new Runnable() {
			public void run() {
				int storage = Cocoa.objc_msgSend (Cocoa.C_NSHTTPCookieStorage, Cocoa.S_sharedHTTPCookieStorage);
				int cookies = Cocoa.objc_msgSend (storage, Cocoa.S_cookies);
				int count = Cocoa.objc_msgSend (cookies, Cocoa.S_count);
				for (int i = 0; i < count; i++) {
					int cookie = Cocoa.objc_msgSend (cookies, Cocoa.S_objectAtIndex, i);
					boolean isSession = Cocoa.objc_msgSend (cookie, Cocoa.S_isSessionOnly) != 0;
					if (isSession) {
						Cocoa.objc_msgSend (storage, Cocoa.S_deleteCookie, cookie);
					}
				}
			}
		};

		NativeGetCookie = new Runnable () {
			public void run () {
				int storage = Cocoa.objc_msgSend (Cocoa.C_NSHTTPCookieStorage, Cocoa.S_sharedHTTPCookieStorage);
				int length = CookieUrl.length ();
				char[] buffer = new char[length];
				CookieUrl.getChars (0, length, buffer, 0);
				int urlString = OS.CFStringCreateWithCharacters (0, buffer, length);
				int url = Cocoa.objc_msgSend (Cocoa.C_NSURL, Cocoa.S_URLWithString, urlString);
				OS.CFRelease (urlString);
				int cookies = Cocoa.objc_msgSend (storage, Cocoa.S_cookiesForURL, url);
				int count = Cocoa.objc_msgSend (cookies, Cocoa.S_count);
				if (count == 0) return;

				length = CookieName.length ();
				buffer = new char[length];
				CookieName.getChars (0, length, buffer, 0);
				int name = OS.CFStringCreateWithCharacters (0, buffer, length);
				for (int i = 0; i < count; i++) {
					int current = Cocoa.objc_msgSend (cookies, Cocoa.S_objectAtIndex, i);
					int currentName = Cocoa.objc_msgSend (current, Cocoa.S_name);
					if (Cocoa.objc_msgSend (currentName, Cocoa.S_compare, name) == Cocoa.NSOrderedSame) {
						int value = Cocoa.objc_msgSend (current, Cocoa.S_value);
						length = OS.CFStringGetLength (value);
						buffer = new char[length];
						CFRange range = new CFRange ();
						range.length = length;
						OS.CFStringGetCharacters (value, range, buffer);
						CookieValue = new String (buffer);
						OS.CFRelease (name);
						return;
					}
				}
				OS.CFRelease (name);
			}
		};

		NativeSetCookie = new Runnable () {
			public void run () {
				int length = CookieUrl.length ();
				char[] buffer = new char[length];
				CookieUrl.getChars (0, length, buffer, 0);
				int urlString = OS.CFStringCreateWithCharacters (0, buffer, length);
				int url = Cocoa.objc_msgSend (Cocoa.C_NSURL, Cocoa.S_URLWithString, urlString);
				OS.CFRelease (urlString);

				length = CookieValue.length ();
				buffer = new char[length];
				CookieValue.getChars (0, length, buffer, 0);
				int value = OS.CFStringCreateWithCharacters (0, buffer, length);
				length = HEADER_SETCOOKIE.length ();
				buffer = new char[length];
				HEADER_SETCOOKIE.getChars (0, length, buffer, 0);
				int key = OS.CFStringCreateWithCharacters (0, buffer, length);
				int headers = Cocoa.objc_msgSend (Cocoa.C_NSMutableDictionary, Cocoa.S_dictionaryWithCapacity, 1);
				Cocoa.objc_msgSend (headers, Cocoa.S_setValue, value, key);
				OS.CFRelease (key);
				OS.CFRelease (value);

				int cookies = Cocoa.objc_msgSend (Cocoa.C_NSHTTPCookie, Cocoa.S_cookiesWithResponseHeaderFields, headers, url);
				if (Cocoa.objc_msgSend (cookies, Cocoa.S_count) == 0) return;
				int cookie = Cocoa.objc_msgSend (cookies, Cocoa.S_objectAtIndex, 0);
				int storage = Cocoa.objc_msgSend (Cocoa.C_NSHTTPCookieStorage, Cocoa.S_sharedHTTPCookieStorage);
				Cocoa.objc_msgSend (storage, Cocoa.S_setCookie, cookie);
				CookieResult = true;
			}
		};
	}

public void create (Composite parent, int style) {
	/*
	* Note.  Loading the webkit bundle on Jaguar causes a crash.
	* The workaround is to detect any OS prior to 10.30 and fail
	* without crashing.
	*/
	if (OS.VERSION < 0x1030) {
		browser.dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	
	/*
	* Bug in Safari on OSX 10.5 (Leopard) only.  VoiceOver no longer follows focus when
	* HIWebViewCreate is used to create a WebView.  The VoiceOver cursor (activated by
	* Control+Alt+arrows) continues to work, but keyboard focus is not tracked.  The fix
	* is to create the WebView with HICocoaViewCreate (api introduced in OSX 10.5) when
	* running on OSX 10.5.
	*/
	int outControl[] = new int[1];
	if (OS.VERSION >= 0x1050) {
		webView = Cocoa.objc_msgSend(Cocoa.objc_msgSend(Cocoa.C_WebView, Cocoa.S_alloc), Cocoa.S_initWithFrame_frameName_groupName, new NSRect(), 0, 0);
		if (webView != 0) {
			Cocoa.HICocoaViewCreate(webView, 0, outControl);
			webViewHandle = outControl[0];
			Cocoa.objc_msgSend(webView, Cocoa.S_release);
		}
	} else {
		Cocoa.HIWebViewCreate(outControl);
		webViewHandle = outControl[0];
		if (webViewHandle != 0) {
			webView = Cocoa.HIWebViewGetWebView(webViewHandle);
		}
	}
	if (webViewHandle == 0) {
		browser.dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);
	}

	Display display = browser.getDisplay();
	display.setData(ADD_WIDGET_KEY, new Object[] {new Integer(webViewHandle), browser});

	/*
	* WebKit's DOM listener api became functional in OSX 10.4.  If OSX 10.4 or 
	* later is detected then override the default event mechanism to not send key
	* events and some mouse events so that the browser can send them by listening
	* to the DOM instead.
	*/
	if (!(OS.VERSION < 0x1040)) {
		browser.setData(SAFARI_EVENTS_FIX_KEY);
	}

	/*
	* Bug in Safari.  For some reason, every application must contain
	* a visible window that has never had a WebView or mouse move events
	* are not delivered.  This seems to happen after a browser has been
	* either hidden or disposed in any window.  The fix is to create a
	* single transparent overlay window that is disposed when the display
	* is disposed.
	*/
	if (display.getData(BROWSER_WINDOW) == null) {
		Rect bounds = new Rect ();
		OS.SetRect (bounds, (short) 0, (short) 0, (short) 1, (short) 1);
		final int[] outWindow = new int[1];
		OS.CreateNewWindow(OS.kOverlayWindowClass, 0, bounds, outWindow);
		OS.ShowWindow(outWindow[0]);
		OS.HIObjectSetAccessibilityIgnored (outWindow[0], true);
		display.disposeExec(new Runnable() {
			public void run() {
				if (outWindow[0] != 0) {
					OS.DisposeWindow(outWindow[0]);
				}
				outWindow[0] = 0;
			}
		});
		display.setData(BROWSER_WINDOW, outWindow);
	}
	
	/*
	* Bug in Safari. The WebView does not draw properly if it is embedded as
	* sub view of the browser handle.  The fix is to add the web view to the
	* window root control and resize it on top of the browser handle.
	* 
	* Note that when the browser is reparented, the web view has to
	* be reparented by hand by hooking kEventControlOwningWindowChanged.
	*/
	int window = OS.GetControlOwner(browser.handle);
	int[] contentView = new int[1];
	OS.HIViewFindByID(OS.HIViewGetRoot(window), OS.kHIViewWindowContentID(), contentView);
	OS.HIViewAddSubview(contentView[0], webViewHandle);
	OS.HIViewChangeFeatures(webViewHandle, OS.kHIViewFeatureIsOpaque, 0);

	/*
	* Bug in Safari. The WebView does not receive mouse and key events when it is added
	* to a visible top window.  It is assumed that Safari hooks its own event listener
	* when the top window emits the kEventWindowShown event. The workaround is to send a
	* fake kEventWindowShown event to the top window after the WebView has been added
	* to the HIView (after the top window is visible) to give Safari a chance to hook
	* events.
	*/
	OS.HIViewSetVisible(webViewHandle, true);	
	if (browser.getShell().isVisible()) {
		int[] showEvent = new int[1];
		OS.CreateEvent(0, OS.kEventClassWindow, OS.kEventWindowShown, 0.0, OS.kEventAttributeUserEvent, showEvent);
		OS.SetEventParameter(showEvent[0], OS.kEventParamDirectObject, OS.typeWindowRef, 4, new int[] {OS.GetControlOwner(browser.handle)});
		OS.SendEventToEventTarget(showEvent[0], OS.GetWindowEventTarget(window));
		if (showEvent[0] != 0) OS.ReleaseEvent(showEvent[0]);
	}

	/*
	* This code is intentionally commented. Setting a group name is the right thing
	* to do in order to avoid multiple open window requests. For some reason, Safari
	* crashes when requested to reopen the same window if that window was previously
	* closed. This may be because that window was not correctly closed. 
	*/	
//	String groupName = "MyDocument"; //$NON-NLS-1$
//	int length = groupName.length();
//	char[] buffer = new char[length];
//	groupName.getChars(0, length, buffer, 0);
//	int groupNameString = OS.CFStringCreateWithCharacters(0, buffer, length);
//	// [webView setGroupName:@"MyDocument"];
//	WebKit.objc_msgSend(webView, WebKit.S_setGroupName, groupNameString);
//	OS.CFRelease(groupNameString);
	
	final int notificationCenter = Cocoa.objc_msgSend(Cocoa.C_NSNotificationCenter, Cocoa.S_defaultCenter);

	Listener listener = new Listener() {
		public void handleEvent(Event e) {
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

					OS.RemoveEventHandler(windowBoundsHandler);
					windowBoundsHandler = 0;

					e.display.setData(ADD_WIDGET_KEY, new Object[] {new Integer(webViewHandle), null});

					Cocoa.objc_msgSend(webView, Cocoa.S_setFrameLoadDelegate, 0);
					Cocoa.objc_msgSend(webView, Cocoa.S_setResourceLoadDelegate, 0);
					Cocoa.objc_msgSend(webView, Cocoa.S_setUIDelegate, 0);
					Cocoa.objc_msgSend(webView, Cocoa.S_setPolicyDelegate, 0);
					Cocoa.objc_msgSend(webView, Cocoa.S_setDownloadDelegate, 0);
					Cocoa.objc_msgSend(notificationCenter, Cocoa.S_removeObserver, delegate);
					
					Cocoa.objc_msgSend(delegate, Cocoa.S_release);
					OS.DisposeControl(webViewHandle);
					webView = webViewHandle = 0;
					html = null;
					lastHoveredLinkURL = lastNavigateURL = null;

					Enumeration elements = functions.elements ();
					while (elements.hasMoreElements ()) {
						((BrowserFunction)elements.nextElement ()).dispose (false);
					}
					functions = null;

					if (preferences != 0) {
						Cocoa.objc_msgSend (preferences, Cocoa.S_release);
					}
					preferences = 0;
					break;
				}
				case SWT.FocusIn: {
					hasNewFocusElement = true;
					OS.SetKeyboardFocus(OS.GetControlOwner(browser.handle), webViewHandle, (short)-1);
					break;
				}
			}
		}
	};
	browser.addListener(SWT.Dispose, listener);
	browser.addListener(SWT.FocusIn, listener);
	browser.addListener(SWT.KeyDown, listener); /* needed to make browser traversable */
	
	if (Callback3 == null) Callback3 = new Callback(this.getClass(), "eventProc3", 3); //$NON-NLS-1$
	int callback3Address = Callback3.getAddress();
	if (callback3Address == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);

	int[] mask = new int[] {
		OS.kEventClassKeyboard, OS.kEventRawKeyDown,
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlGetClickActivation,
		OS.kEventClassControl, OS.kEventControlSetCursor,
		OS.kEventClassTextInput, OS.kEventTextInputUnicodeForKeyEvent,
	};
	OS.InstallEventHandler(OS.GetControlEventTarget(webViewHandle), callback3Address, mask.length / 2, mask, webViewHandle, null);
	int[] mask1 = new int[] {
		OS.kEventClassControl, OS.kEventControlBoundsChanged,
		OS.kEventClassControl, OS.kEventControlVisibilityChanged,
		OS.kEventClassControl, OS.kEventControlOwningWindowChanged,
	};
	OS.InstallEventHandler(OS.GetControlEventTarget(browser.handle), callback3Address, mask1.length / 2, mask1, browser.handle, null);
	int[] mask2 = new int[] {
		OS.kEventClassWindow, OS.kEventWindowBoundsChanged,
	};
	int[] outRef = new int[1];
	OS.InstallEventHandler(OS.GetWindowEventTarget(window), callback3Address, mask2.length / 2, mask2, browser.handle, outRef);
	windowBoundsHandler = outRef[0];

	if (Callback7 == null) Callback7 = new Callback(this.getClass(), "eventProc7", 7); //$NON-NLS-1$
	int callback7Address = Callback7.getAddress();
	if (callback7Address == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	
	// delegate = [[WebResourceLoadDelegate alloc] init eventProc];
	delegate = Cocoa.objc_msgSend(Cocoa.C_WebKitDelegate, Cocoa.S_alloc);
	delegate = Cocoa.objc_msgSend(delegate, Cocoa.S_initWithProc, callback7Address, webViewHandle);

	// [webView setFrameLoadDelegate:delegate];
	Cocoa.objc_msgSend(webView, Cocoa.S_setFrameLoadDelegate, delegate);
		
	// [webView setResourceLoadDelegate:delegate];
	Cocoa.objc_msgSend(webView, Cocoa.S_setResourceLoadDelegate, delegate);

	// [webView setUIDelegate:delegate];
	Cocoa.objc_msgSend(webView, Cocoa.S_setUIDelegate, delegate);
	
	/* register delegate for all notifications sent out from webview */
	Cocoa.objc_msgSend(notificationCenter, Cocoa.S_addObserver_selector_name_object, delegate, Cocoa.S_handleNotification, 0, webView);
	
	// [webView setPolicyDelegate:delegate];
	Cocoa.objc_msgSend(webView, Cocoa.S_setPolicyDelegate, delegate);

	// [webView setDownloadDelegate:delegate];
	Cocoa.objc_msgSend(webView, Cocoa.S_setDownloadDelegate, delegate);

	// [webView setApplicationNameForUserAgent:applicationName];
	int length = AGENT_STRING.length();
	char[] chars = new char[length];
	AGENT_STRING.getChars(0, length, chars, 0);
	int sHandle = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(webView, Cocoa.S_setApplicationNameForUserAgent, sHandle);
	OS.CFRelease(sHandle);

	if (OS.VERSION < 0x1050 && display.getActiveShell() == browser.getShell()) {
		Cocoa.objc_msgSend(Cocoa.objc_msgSend(webView, Cocoa.S_window), Cocoa.S_makeKeyWindow);
	}

	if (!Initialized) {
		Initialized = true;
		/* disable applets */
		int preferences = Cocoa.objc_msgSend(Cocoa.C_WebPreferences, Cocoa.S_standardPreferences);
		Cocoa.objc_msgSend(preferences, Cocoa.S_setJavaEnabled, 0);
	}
}

static int eventProc3(int nextHandler, int theEvent, int userData) {
	Widget widget = Display.getCurrent().findWidget(userData);
	if (widget instanceof Browser) {
		return ((Safari)((Browser)widget).webBrowser).handleCallback(nextHandler, theEvent);
	}
	return OS.eventNotHandledErr;
}

static int eventProc7(int webview, int userData, int selector, int arg0, int arg1, int arg2, int arg3) {
	Widget widget = Display.getCurrent().findWidget(userData);
	if (widget instanceof Browser) {
		return ((Safari)((Browser)widget).webBrowser).handleCallback(selector, arg0, arg1, arg2, arg3);
	}
	return 0;
}

static String getString (int ptr) {
	int length = OS.CFStringGetLength (ptr);
	char[] buffer = new char[length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (ptr, range, buffer);
	return new String (buffer);
}

public boolean back() {
	html = null;
	return Cocoa.objc_msgSend(webView, Cocoa.S_goBack) != 0;
}

public boolean execute(String script) {
	int length = script.length();
	char[] buffer = new char[length];
	script.getChars(0, length, buffer, 0);
	int string = OS.CFStringCreateWithCharacters(0, buffer, length);

	int value = Cocoa.objc_msgSend(webView, Cocoa.S_stringByEvaluatingJavaScriptFromString, string);
	OS.CFRelease(string);
	return value != 0;
}

public boolean forward() {
	html = null;
	return Cocoa.objc_msgSend(webView, Cocoa.S_goForward) != 0;
}

public String getBrowserType () {
	return "safari"; //$NON-NLS-1$
}

public String getText() {
	int mainFrame = Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame);
	int dataSource = Cocoa.objc_msgSend(mainFrame, Cocoa.S_dataSource);
	if (dataSource == 0) return "";	//$NON-NLS-1$
	int representation = Cocoa.objc_msgSend(dataSource, Cocoa.S_representation);
	if (representation == 0) return "";	//$NON-NLS-1$
	int source = Cocoa.objc_msgSend(representation, Cocoa.S_documentSource);
	if (source == 0) return "";	//$NON-NLS-1$
	int length = OS.CFStringGetLength(source);
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(source, range, buffer);
	return new String(buffer);
}

public String getUrl() {
	return url;
}

int handleCallback(int nextHandler, int theEvent) {
	int eventKind = OS.GetEventKind(theEvent);
	switch (OS.GetEventClass(theEvent)) {
		case OS.kEventClassControl:
			switch (eventKind) {
				case OS.kEventControlGetClickActivation: {
					OS.SetEventParameter (theEvent, OS.kEventParamClickActivation, OS.typeClickActivationResult, 4, new int [] {OS.kActivateAndHandleClick});
					return OS.noErr;
				}
				case OS.kEventControlSetCursor: {
					return OS.noErr;
				}
				case OS.kEventControlDraw: {
					/*
					 * Bug on Safari. The web view cannot be obscured by other views above it.
					 * This problem is specified in the apple documentation for HiWebViewCreate.
					 * The workaround is to don't draw the web view when it is not visible.
					 */
					if (!browser.isVisible ()) return OS.noErr;
					break;
				}
				case OS.kEventControlOwningWindowChanged: {
					/* Reparent the web view handler */
					int window = OS.GetControlOwner(browser.handle);
					int[] contentView = new int[1];
					OS.HIViewFindByID(OS.HIViewGetRoot(window), OS.kHIViewWindowContentID(), contentView);
					OS.HIViewAddSubview(contentView[0], webViewHandle);
					
					/* Reset the kEventWindowBoundsChanged handler */
					OS.RemoveEventHandler(windowBoundsHandler);
					int[] mask2 = new int[] {
						OS.kEventClassWindow, OS.kEventWindowBoundsChanged,
					};
					int[] outRef = new int[1];
					OS.InstallEventHandler(OS.GetWindowEventTarget(window), Callback3.getAddress(), mask2.length / 2, mask2, browser.handle, outRef);
					windowBoundsHandler = outRef[0];
					break;
				}
				case OS.kEventControlBoundsChanged:
				case OS.kEventControlVisibilityChanged: {
					/*
					 * Bug on Safari. The web view cannot be obscured by other views above it.
					 * This problem is specified in the apple documentation for HiWebViewCreate.
					 * The workaround is to hook kEventControlVisibilityChanged on the browser
					 * and move the browser out of the screen when hidden and restore its bounds
					 * when shown.
					 */
					CGRect bounds = new CGRect();
					if (!browser.isVisible()) {
						bounds.x = bounds.y = -MIN_SIZE;
						bounds.width = bounds.height = MIN_SIZE;
						OS.HIViewSetFrame(webViewHandle, bounds);
					} else {
						OS.HIViewGetBounds(browser.handle, bounds);
						int[] contentView = new int[1];
						OS.HIViewFindByID(OS.HIViewGetRoot(OS.GetControlOwner(browser.handle)), OS.kHIViewWindowContentID(), contentView);
						OS.HIViewConvertRect(bounds, browser.handle, contentView[0]);
						/* 
						* Bug in Safari.  For some reason, the web view will display incorrectly or
						* blank depending on its contents, if its size is set to a value smaller than
						* MIN_SIZE. It will not display properly even after the size is made larger.
						* The fix is to avoid setting sizes smaller than MIN_SIZE. 
						*/
						if (bounds.width <= MIN_SIZE) bounds.width = MIN_SIZE;
						if (bounds.height <= MIN_SIZE) bounds.height = MIN_SIZE;
						OS.HIViewSetFrame(webViewHandle, bounds);
					}
					break;
				}
			}
		case OS.kEventClassWindow:
			switch (eventKind) {
				case OS.kEventWindowBoundsChanged:
					/*
					 * Bug on Safari. Resizing the height of a Shell containing a Browser at
					 * a fixed location causes the Browser to redraw at a wrong location.
					 * The web view is a HIView container that internally hosts
					 * a Cocoa NSView that uses a coordinates system with the origin at the
					 * bottom left corner of a window instead of the coordinates system used
					 * in Carbon that starts at the top left corner. The workaround is to
					 * reposition the web view every time the Shell of the Browser is resized.
					 * 
					 * Note the size should not be updated if the browser is hidden.
					 */
					if (browser.isVisible()) {
						CGRect oldBounds = new CGRect();
						OS.GetEventParameter (theEvent, OS.kEventParamOriginalBounds, OS.typeHIRect, null, CGRect.sizeof, null, oldBounds);
						CGRect bounds = new CGRect();
						OS.GetEventParameter (theEvent, OS.kEventParamCurrentBounds, OS.typeHIRect, null, CGRect.sizeof, null, bounds);
						if (oldBounds.height == bounds.height) break;
						OS.HIViewGetBounds(browser.handle, bounds);
						int[] contentView = new int[1];
						OS.HIViewFindByID(OS.HIViewGetRoot(OS.GetControlOwner(browser.handle)), OS.kHIViewWindowContentID(), contentView);
						OS.HIViewConvertRect(bounds, browser.handle, contentView[0]);
						/* 
						* Bug in Safari.  For some reason, the web view will display incorrectly or
						* blank depending on its contents, if its size is set to a value smaller than
						* MIN_SIZE. It will not display properly even after the size is made larger.
						* The fix is to avoid setting sizes smaller than MIN_SIZE. 
						*/
						if (bounds.width <= MIN_SIZE) bounds.width = MIN_SIZE;
						if (bounds.height <= MIN_SIZE) bounds.height = MIN_SIZE;
						bounds.x++;
						/* Note that the bounds needs to change */
						OS.HIViewSetFrame(webViewHandle, bounds);
						bounds.x--;
						OS.HIViewSetFrame(webViewHandle, bounds);
					}
			}
		case OS.kEventClassKeyboard:
			switch (eventKind) {
				case OS.kEventRawKeyDown: {
					/*
					* Bug in Safari. The WebView blocks the propagation of certain Carbon events
					* such as kEventRawKeyDown. On the Mac, Carbon events propagate from the
					* Focus Target Handler to the Control Target Handler, Window Target and finally
					* the Application Target Handler. It is assumed that WebView hooks its events
					* on the Window Target and does not pass kEventRawKeyDown to the next handler.
					* Since kEventRawKeyDown events never make it to the Application Target Handler,
					* the Application Target Handler never gets to emit kEventTextInputUnicodeForKeyEvent
					* used by SWT to send a SWT.KeyDown event.
					* The workaround is to hook kEventRawKeyDown on the Control Target Handler which gets
					* called before the WebView hook on the Window Target Handler. Then, forward this event
					* directly to the Application Target Handler. Note that if in certain conditions Safari
					* does not block the kEventRawKeyDown, then multiple kEventTextInputUnicodeForKeyEvent
					* events might be generated as a result of this workaround.
					*/
					//TEMPORARY CODE
//					doit = false;
//					OS.SendEventToEventTarget(theEvent, OS.GetApplicationEventTarget());
//					if (!doit) return OS.noErr;

					int[] length = new int[1];
					int status = OS.GetEventParameter (theEvent, OS.kEventParamKeyUnicodes, OS.typeUnicodeText, null, 4, length, (char[])null);
					if (status == OS.noErr && length[0] != 0) {
						int[] modifiers = new int[1];
						OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
						char[] chars = new char[1];
						OS.GetEventParameter (theEvent, OS.kEventParamKeyUnicodes, OS.typeUnicodeText, null, 2, null, chars);
						if ((modifiers[0] & OS.cmdKey) != 0) {
							switch (chars[0]) {
								case 'v': {
									Cocoa.objc_msgSend (webView, Cocoa.S_paste);
									return OS.noErr;
								}
								case 'c': {
									Cocoa.objc_msgSend (webView, Cocoa.S_copy);
									return OS.noErr;
								}
								case 'x': {
									Cocoa.objc_msgSend (webView, Cocoa.S_cut);
									return OS.noErr;
								}
							}
						}
					}
					/*
					* Bug in Carbon.  OSX crashes if a HICocoaView is disposed during a key event,
					* presumably as a result of attempting to use it after its refcount has reached
					* 0.  The workaround is to temporarily add an extra ref to the view and its
					* ancestor while the DOM listener is handling the event, in case the
					* Browser gets disposed in a callback.
					*/
					int handle = webViewHandle, root = OS.HIViewGetSuperview (webViewHandle);
					OS.CFRetain (handle);
					OS.CFRetain (root);
					int result = OS.CallNextEventHandler (nextHandler, theEvent);
					OS.CFRelease (handle);
					OS.CFRelease (root);
					return result;
				}
			}
		case OS.kEventClassTextInput:
			switch (eventKind) {
				case OS.kEventTextInputUnicodeForKeyEvent: {
					/*
					* Note.  This event is received from the Window Target therefore after it was received
					* by the Focus Target. The SWT.KeyDown event is sent by SWT on the Focus Target. If it
					* is received here, then the SWT.KeyDown doit flag must have been left to the value
					* true.  For package visibility reasons we cannot access the doit flag directly.
					* 
					* Sequence of events when the user presses a key down
					* 
					* .Control Target - kEventRawKeyDown
					* 	.forward to ApplicationEventTarget
					* 		.Focus Target kEventTextInputUnicodeForKeyEvent - SWT emits SWT.KeyDown - 
					* 			blocks further propagation if doit false. Browser does not know directly about
					* 			the doit flag value.
					* 			.Window Target kEventTextInputUnicodeForKeyEvent - if received, Browser knows 
					* 			SWT.KeyDown is not blocked and event should be sent to WebKit
					*  Return from Control Target - kEventRawKeyDown: let the event go to WebKit if doit true 
					*  (eventNotHandledErr) or stop it (noErr).
					*/
					//TEMPORARY CODE
//					doit = true;
					break;
				}
			}
	}
	return OS.eventNotHandledErr;
}

/* Here we dispatch all WebView upcalls. */
int handleCallback(int selector, int arg0, int arg1, int arg2, int arg3) {
	int ret = 0;
	// for meaning of selector see WebKitDelegate methods in webkit.c
	switch (selector) {
		case 1: didFailProvisionalLoadWithError(arg0, arg1); break;
		case 2: didFinishLoadForFrame(arg0); break;
		case 3: didReceiveTitle(arg0, arg1); break;
		case 4: didStartProvisionalLoadForFrame(arg0); break;
		case 5: didFinishLoadingFromDataSource(arg0, arg1); break;
		case 6: didFailLoadingWithError(arg0, arg1, arg2); break;
		case 7: ret = identifierForInitialRequest(arg0, arg1); break;
		case 8: ret = willSendRequest(arg0, arg1, arg2, arg3); break;
		case 9: handleNotification(arg0); break;
		case 10: didCommitLoadForFrame(arg0); break;
		case 11: ret = createWebViewWithRequest(arg0); break;
		case 12: webViewShow(arg0); break;
		case 13: setFrame(arg0); break;
		case 14: webViewClose(); break;
		case 15: ret = contextMenuItemsForElement(arg0, arg1); break;
		case 16: setStatusBarVisible(arg0); break;
		case 17: setResizable(arg0); break;
		case 18: setToolbarsVisible(arg0); break;
		case 19: decidePolicyForMIMEType(arg0, arg1, arg2, arg3); break;
		case 20: decidePolicyForNavigationAction(arg0, arg1, arg2, arg3); break;
		case 21: decidePolicyForNewWindowAction(arg0, arg1, arg2, arg3); break;
		case 22: unableToImplementPolicyWithError(arg0, arg1); break;
		case 23: setStatusText(arg0); break;
		case 24: webViewFocus(); break;
		case 25: webViewUnfocus(); break;
		case 26: runJavaScriptAlertPanelWithMessage(arg0); break;
		case 27: ret = runJavaScriptConfirmPanelWithMessage(arg0); break;
		case 28: runOpenPanelForFileButtonWithResultListener(arg0); break;
		case 29: decideDestinationWithSuggestedFilename(arg0, arg1); break;
		case 30: mouseDidMoveOverElement(arg0, arg1); break;
		case 31: didChangeLocationWithinPageForFrame(arg0); break;
		case 32: handleEvent(arg0); break;
		case 33: windowScriptObjectAvailable(arg0); break;
		case 34: ret = callJava(arg0, arg1, arg2); break;
		case 35: didReceiveAuthenticationChallengefromDataSource(arg0, arg1, arg2); break;
	}
	return ret;
}

public boolean isBackEnabled() {
	return Cocoa.objc_msgSend(webView, Cocoa.S_canGoBack) != 0;
}

public boolean isForwardEnabled() {
	return Cocoa.objc_msgSend(webView, Cocoa.S_canGoForward) != 0;
}

public void refresh() {
	Cocoa.objc_msgSend(webView, Cocoa.S_reload, 0);
}

public boolean setText(String html) {
	/*
	* Bug in Safari.  The web view segment faults in some circumstances
	* when the text changes during the location changing callback.  The
	* fix is to defer the work until the callback is done. 
	*/
	if (changingLocation) {
		this.html = html;
	} else {
		_setText(html);
	}
	return true;
}
	
void _setText(String html) {	
	int length = html.length();
	char[] buffer = new char[length];
	html.getChars(0, length, buffer, 0);
	int string = OS.CFStringCreateWithCharacters(0, buffer, length);

	length = URI_FROMMEMORY.length();
	buffer = new char[length];
	URI_FROMMEMORY.getChars(0, length, buffer, 0);
	int URLString = OS.CFStringCreateWithCharacters(0, buffer, length);
	
	/*
	* Note.  URLWithString uses autorelease.  The resulting URL
	* does not need to be released.
	* URL = [NSURL URLWithString:(NSString *)URLString]
	*/	
	int URL = Cocoa.objc_msgSend(Cocoa.C_NSURL, Cocoa.S_URLWithString, URLString);
	OS.CFRelease(URLString);
	
	//mainFrame = [webView mainFrame];
	int mainFrame = Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame);
	
	//[mainFrame loadHTMLString:(NSString *) string baseURL:(NSURL *)URL];
	Cocoa.objc_msgSend(mainFrame, Cocoa.S_loadHTMLStringbaseURL, string, URL);
	OS.CFRelease(string);
}

public boolean setUrl(String url) {
	html = null;

	if (url.indexOf('/') == 0) {
		url = PROTOCOL_FILE + url;
	} else if (url.indexOf(':') == -1) {
		url = PROTOCOL_HTTP + url;
	}

	int inURL = 0;
	char[] chars = new char[url.length()];
	url.getChars(0, chars.length, chars, 0);
	int str = OS.CFStringCreateWithCharacters(0, chars, chars.length);
	if (str != 0) {
		char[] unescapedChars = new char[] {'%', '#'};
		int unescapedStr = OS.CFStringCreateWithCharacters(0, unescapedChars, unescapedChars.length);
		int escapedStr = OS.CFURLCreateStringByAddingPercentEscapes(OS.kCFAllocatorDefault, str, unescapedStr, 0, OS.kCFStringEncodingUTF8);
		if (escapedStr != 0) {
			inURL = OS.CFURLCreateWithString(OS.kCFAllocatorDefault, escapedStr, 0);
			OS.CFRelease(escapedStr);
		}
		if (unescapedStr != 0) OS.CFRelease(unescapedStr);
		OS.CFRelease(str);
	}
	if (inURL == 0) return false;

	//request = [NSURLRequest requestWithURL:(NSURL*)inURL];
	int request = Cocoa.objc_msgSend(Cocoa.C_NSURLRequest, Cocoa.S_requestWithURL, inURL);
	OS.CFRelease(inURL);

	//mainFrame = [webView mainFrame];
	int mainFrame = Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame);

	//[mainFrame loadRequest:request];
	Cocoa.objc_msgSend(mainFrame, Cocoa.S_loadRequest, request);

	return true;
}

public void stop() {
	html = null;
	Cocoa.objc_msgSend(webView, Cocoa.S_stopLoading, 0);
}

/* WebFrameLoadDelegate */
void didChangeLocationWithinPageForFrame(int frame) {
	//id url= [[[[frame provisionalDataSource] request] URL] absoluteString];
	int dataSource = Cocoa.objc_msgSend(frame, Cocoa.S_dataSource);
	int request = Cocoa.objc_msgSend(dataSource, Cocoa.S_request);
	int url = Cocoa.objc_msgSend(request, Cocoa.S_URL);
	int s = Cocoa.objc_msgSend(url, Cocoa.S_absoluteString);	
	int length = OS.CFStringGetLength(s);
	if (length == 0) return;
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(s, range, buffer);
	String url2 = new String(buffer);
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FROMMEMORY)) url2 = ABOUT_BLANK;

	final Display display = browser.getDisplay();
	boolean top = frame == Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame);
	if (top) {
		StatusTextEvent statusText = new StatusTextEvent(browser);
		statusText.display = display;
		statusText.widget = browser;
		statusText.text = url2;
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed(statusText);
		}
	}
	LocationEvent location = new LocationEvent(browser);
	location.display = display;
	location.widget = browser;
	location.location = url2;
	location.top = top;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changed(location);
	}
}

void didFailProvisionalLoadWithError(int error, int frame) {
	if (frame == Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame)) {
		/*
		* Feature on Safari.  The identifier is used here as a marker for the events 
		* related to the top frame and the URL changes related to that top frame as 
		* they should appear on the location bar of a browser.  It is expected to reset
		* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
		* the identifierForInitialRequest event is received.  However, Safari fires
		* the didFinishLoadingFromDataSource event before the entire content of the
		* top frame is loaded.  It is possible to receive multiple willSendRequest 
		* events in this interval, causing the Browser widget to send unwanted
		* Location.changing events.  For this reason, the identifier is reset to 0
		* when the top frame has either finished loading (didFinishLoadForFrame
		* event) or failed (didFailProvisionalLoadWithError).
		*/
		identifier = 0;
	}
}

void didFinishLoadForFrame(int frame) {
	hookDOMFocusListeners(frame);
	hookDOMMouseListeners(frame);
	if (frame == Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame)) {
		hookDOMKeyListeners(frame);

		final Display display = browser.getDisplay();
		/*
		* To be consistent with other platforms a title event should be fired when a
		* page has completed loading.  A page with a <title> tag will do this
		* automatically when the didReceiveTitle callback is received.  However a page
		* without a <title> tag will not do this by default, so fire the event
		* here with the page's url as the title.
		*/
		int dataSource = Cocoa.objc_msgSend(frame, Cocoa.S_dataSource);
		if (dataSource != 0) {
			int title = Cocoa.objc_msgSend(dataSource, Cocoa.S_pageTitle);
			if (title == 0) {	/* page has no title */
				final TitleEvent newEvent = new TitleEvent(browser);
				newEvent.display = display;
				newEvent.widget = browser;
				newEvent.title = url;
				for (int i = 0; i < titleListeners.length; i++) {
					final TitleListener listener = titleListeners[i];
					/*
					* Note on WebKit.  Running the event loop from a Browser
					* delegate callback breaks the WebKit (stop loading or
					* crash).  The workaround is to invoke Display.asyncExec()
					* so that the Browser does not crash if this is attempted.
					*/
					display.asyncExec(
						new Runnable() {
							public void run() {
								if (!display.isDisposed() && !browser.isDisposed()) {
									listener.changed(newEvent);
								}
							}
						}
					);
				}
			}
		}
		final ProgressEvent progress = new ProgressEvent(browser);
		progress.display = display;
		progress.widget = browser;
		progress.current = MAX_PROGRESS;
		progress.total = MAX_PROGRESS;
		for (int i = 0; i < progressListeners.length; i++) {
			final ProgressListener listener = progressListeners[i];
			/*
			* Note on WebKit.  Running the event loop from a Browser
			* delegate callback breaks the WebKit (stop loading or
			* crash).  The ProgressBar widget currently touches the
			* event loop every time the method setSelection is called.  
			* The workaround is to invoke Display.asyncExec() so that
			* the Browser does not crash when the user updates the 
			* selection of the ProgressBar.
			*/
			display.asyncExec(
				new Runnable() {
					public void run() {
						if (!display.isDisposed() && !browser.isDisposed()) {
							listener.completed(progress);
						}
					}
				}
			);
		}

		/* re-install registered functions */
		Enumeration elements = functions.elements ();
		while (elements.hasMoreElements ()) {
			BrowserFunction function = (BrowserFunction)elements.nextElement ();
			execute (function.functionString);
		}

		/*
		* Feature on Safari.  The identifier is used here as a marker for the events 
		* related to the top frame and the URL changes related to that top frame as 
		* they should appear on the location bar of a browser.  It is expected to reset
		* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
		* the identifierForInitialRequest event is received.  Howeever, Safari fires
		* the didFinishLoadingFromDataSource event before the entire content of the
		* top frame is loaded.  It is possible to receive multiple willSendRequest 
		* events in this interval, causing the Browser widget to send unwanted
		* Location.changing events.  For this reason, the identifier is reset to 0
		* when the top frame has either finished loading (didFinishLoadForFrame
		* event) or failed (didFailProvisionalLoadWithError).
		*/
		identifier = 0;
	}
}

void hookDOMFocusListeners(int frame) {
	/*
	* These listeners only need to be hooked for OSX 10.4 (Tiger).  The WebKit on
	* OSX < 10.4 does not send these DOM events, and tab traversals that exit
	* Safari are handled as of OSX 10.5 as a result of using HICocoaViewCreate,
	* which makes these listeners unnecessary.
	*/
	if (!(0x1040 <= OS.VERSION && OS.VERSION < 0x1050)) return;

	int document = Cocoa.objc_msgSend(frame, Cocoa.S_DOMDocument);
	if (document == 0) return;

	String string = DOMEVENT_FOCUSIN;
	int length = string.length();
	char[] chars = new char[length];
	string.getChars(0, length, chars, 0);
	int ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);

	string = DOMEVENT_FOCUSOUT;
	length = string.length();
	chars = new char[length];
	string.getChars(0, length, chars, 0);
	ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);
}
	
void hookDOMKeyListeners(int frame) {
	/*
	* WebKit's DOM listener api became functional in OSX 10.4, so if an earlier
	* version than this is detected then do not hook the DOM listeners.
	*/
	if (OS.VERSION < 0x1040) return;

	int document = Cocoa.objc_msgSend(frame, Cocoa.S_DOMDocument);
	if (document == 0) return;

	String string = DOMEVENT_KEYDOWN;
	int length = string.length();
	char[] chars = new char[length];
	string.getChars(0, length, chars, 0);
	int ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);

	string = DOMEVENT_KEYUP;
	length = string.length();
	chars = new char[length];
	string.getChars(0, length, chars, 0);
	ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);
}

void hookDOMMouseListeners(int frame) {
	/*
	* WebKit's DOM listener api became functional in OSX 10.4, so if an earlier
	* version than this is detected then do not hook the DOM listeners.
	*/
	if (OS.VERSION < 0x1040) return;

	int document = Cocoa.objc_msgSend(frame, Cocoa.S_DOMDocument);
	if (document == 0) return;

	String string = DOMEVENT_MOUSEDOWN;
	int length = string.length();
	char[] chars = new char[length];
	string.getChars(0, length, chars, 0);
	int ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);

	string = DOMEVENT_MOUSEUP;
	length = string.length();
	chars = new char[length];
	string.getChars(0, length, chars, 0);
	ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);

	string = DOMEVENT_MOUSEMOVE;
	length = string.length();
	chars = new char[length];
	string.getChars(0, length, chars, 0);
	ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);

	string = DOMEVENT_MOUSEWHEEL;
	length = string.length();
	chars = new char[length];
	string.getChars(0, length, chars, 0);
	ptr = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(document, Cocoa.S_addEventListener, ptr, delegate, 0);
	OS.CFRelease(ptr);
}

void didReceiveTitle(int title, int frame) {
	if (frame == Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame)) {
		int length = OS.CFStringGetLength(title);
		char[] buffer = new char[length];
		CFRange range = new CFRange();
		range.length = length;
		OS.CFStringGetCharacters(title, range, buffer);
		String newTitle = new String(buffer);
		TitleEvent newEvent = new TitleEvent(browser);
		newEvent.display = browser.getDisplay();
		newEvent.widget = browser;
		newEvent.title = newTitle;
		for (int i = 0; i < titleListeners.length; i++) {
			titleListeners[i].changed(newEvent);
		}
	}
}

void didStartProvisionalLoadForFrame(int frame) {
	/* 
	* This code is intentionally commented.  WebFrameLoadDelegate:didStartProvisionalLoadForFrame is
	* called before WebResourceLoadDelegate:willSendRequest and
	* WebFrameLoadDelegate:didCommitLoadForFrame.  The resource count is reset when didCommitLoadForFrame
	* is received for the top frame.
	*/
//	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
//	if (frame == WebKit.objc_msgSend(webView, WebKit.S_mainFrame)) {
//		/* reset resource status variables */
//		resourceCount= 0;
//	}
}

void didCommitLoadForFrame(int frame) {
	//id url= [[[[frame provisionalDataSource] request] URL] absoluteString];
	int dataSource = Cocoa.objc_msgSend(frame, Cocoa.S_dataSource);
	int request = Cocoa.objc_msgSend(dataSource, Cocoa.S_request);
	int url = Cocoa.objc_msgSend(request, Cocoa.S_URL);
	int s = Cocoa.objc_msgSend(url, Cocoa.S_absoluteString);	
	int length = OS.CFStringGetLength(s);
	if (length == 0) return;
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(s, range, buffer);
	String url2 = new String(buffer);
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FROMMEMORY)) url2 = ABOUT_BLANK;

	final Display display = browser.getDisplay();
	boolean top = frame == Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame);
	if (top) {
		/* reset resource status variables */
		resourceCount = 0;		
		this.url = url2;
		
		final ProgressEvent progress = new ProgressEvent(browser);
		progress.display = display;
		progress.widget = browser;
		progress.current = 1;
		progress.total = MAX_PROGRESS;
		for (int i = 0; i < progressListeners.length; i++) {
			final ProgressListener listener = progressListeners[i];
			/*
			* Note on WebKit.  Running the event loop from a Browser
			* delegate callback breaks the WebKit (stop loading or
			* crash).  The widget ProgressBar currently touches the
			* event loop every time the method setSelection is called.  
			* The workaround is to invoke Display.asyncexec so that
			* the Browser does not crash when the user updates the 
			* selection of the ProgressBar.
			*/
			display.asyncExec(
				new Runnable() {
					public void run() {
						if (!display.isDisposed() && !browser.isDisposed())
							listener.changed(progress);
					}
				}
			);
		}
		
		StatusTextEvent statusText = new StatusTextEvent(browser);
		statusText.display = display;
		statusText.widget = browser;
		statusText.text = url2;
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed(statusText);
		}
	}
	LocationEvent location = new LocationEvent(browser);
	location.display = display;
	location.widget = browser;
	location.location = url2;
	location.top = top;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changed(location);
	}
}

void windowScriptObjectAvailable (int windowScriptObject) {
	String objectName = "external"; //$NON-NLS-1$
	char[] chars = new char[objectName.length ()];
	objectName.getChars (0, chars.length, chars, 0);
	int str = OS.CFStringCreateWithCharacters (0, chars, chars.length);
	if (str != 0) {
		Cocoa.objc_msgSend (windowScriptObject, Cocoa.S_setValue, delegate, str);
		OS.CFRelease (str);
	}
}

/* WebResourceLoadDelegate */

void didFinishLoadingFromDataSource(int identifier, int dataSource) {
	/*
	* Feature on Safari.  The identifier is used here as a marker for the events 
	* related to the top frame and the URL changes related to that top frame as 
	* they should appear on the location bar of a browser.  It is expected to reset
	* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
	* the identifierForInitialRequest event is received.  Howeever, Safari fires
	* the didFinishLoadingFromDataSource event before the entire content of the
	* top frame is loaded.  It is possible to receive multiple willSendRequest 
	* events in this interval, causing the Browser widget to send unwanted
	* Location.changing events.  For this reason, the identifier is reset to 0
	* when the top frame has either finished loading (didFinishLoadForFrame
	* event) or failed (didFailProvisionalLoadWithError).
	*/
	// this code is intentionally commented
	//if (this.identifier == identifier) this.identifier = 0;
}

void didFailLoadingWithError(int identifier, int error, int dataSource) {
	/*
	* Feature on Safari.  The identifier is used here as a marker for the events 
	* related to the top frame and the URL changes related to that top frame as 
	* they should appear on the location bar of a browser.  It is expected to reset
	* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
	* the identifierForInitialRequest event is received.  Howeever, Safari fires
	* the didFinishLoadingFromDataSource event before the entire content of the
	* top frame is loaded.  It is possible to receive multiple willSendRequest 
	* events in this interval, causing the Browser widget to send unwanted
	* Location.changing events.  For this reason, the identifier is reset to 0
	* when the top frame has either finished loading (didFinishLoadForFrame
	* event) or failed (didFailProvisionalLoadWithError).
	*/
	// this code is intentionally commented
	//if (this.identifier == identifier) this.identifier = 0;
}

void didReceiveAuthenticationChallengefromDataSource (int identifier, int challenge, int dataSource) {
	/*
	 * Do not invoke the listeners if this challenge has been failed too many
	 * times because a listener is likely giving incorrect credentials repeatedly
	 * and will do so indefinitely.
	 */
	int count = Cocoa.objc_msgSend (challenge, Cocoa.S_previousFailureCount);
	if (count < 3) {
		for (int i = 0; i < authenticationListeners.length; i++) {
			AuthenticationEvent event = new AuthenticationEvent (browser);
			event.location = lastNavigateURL;
			authenticationListeners[i].authenticate (event);
			if (!event.doit) {
				int challengeSender = Cocoa.objc_msgSend (challenge, Cocoa.S_sender);
				Cocoa.objc_msgSend (challengeSender, Cocoa.S_cancelAuthenticationChallenge, challenge);
				return;
			}
			if (event.user != null && event.password != null) {
				int challengeSender = Cocoa.objc_msgSend (challenge, Cocoa.S_sender);
				int length = event.user.length ();
				char[] buffer = new char[length];
				event.user.getChars (0, length, buffer, 0);
				int user = OS.CFStringCreateWithCharacters (0, buffer, length);
				length = event.password.length ();
				buffer = new char[length];
				event.password.getChars (0, length, buffer, 0);
				int password = OS.CFStringCreateWithCharacters (0, buffer, length);
				int credential = Cocoa.objc_msgSend (Cocoa.C_NSURLCredential, Cocoa.S_credentialWithUser, user, password, Cocoa.NSURLCredentialPersistenceForSession);
				Cocoa.objc_msgSend (challengeSender, Cocoa.S_useCredential, credential, challenge);
				OS.CFRelease (password);
				OS.CFRelease (user);
				return;
			}
		}
	}

	/* no listener handled the challenge, so try to invoke the native panel */
	int cls = Cocoa.C_WebPanelAuthenticationHandler;
	if (cls != 0) {
		int method = Cocoa.class_getClassMethod (cls, Cocoa.S_sharedHandler);
		if (method != 0) {
			int handler = Cocoa.objc_msgSend (cls, Cocoa.S_sharedHandler);
			if (handler != 0) {
				int window = Cocoa.objc_msgSend (webView, Cocoa.S_window);
				Cocoa.objc_msgSend (handler, Cocoa.S_startAuthentication, challenge, window);
				return;
			}
		}
	}

	/* the native panel was not available, so show a custom dialog */
	String[] userReturn = new String[1], passwordReturn = new String[1];
	int proposedCredential = Cocoa.objc_msgSend (challenge, Cocoa.S_proposedCredential);
	if (proposedCredential != 0) {
		int user = Cocoa.objc_msgSend (proposedCredential, Cocoa.S_user);
		userReturn[0] = getString (user);
		boolean hasPassword = Cocoa.objc_msgSend (proposedCredential, Cocoa.S_hasPassword) != 0;
		if (hasPassword) {
			int password = Cocoa.objc_msgSend (proposedCredential, Cocoa.S_password);
			passwordReturn[0] = getString (password);
		}
	}

	int space = Cocoa.objc_msgSend (challenge, Cocoa.S_protectionSpace);
	int host = Cocoa.objc_msgSend (space, Cocoa.S_host);
	String hostString = getString (host) + ':';
	int port = Cocoa.objc_msgSend (space, Cocoa.S_port);
	hostString += port;
	int realm = Cocoa.objc_msgSend (space, Cocoa.S_realm);
	String realmString = getString (realm);
	boolean result = showAuthenticationDialog (userReturn, passwordReturn, hostString, realmString);
	int challengeSender = Cocoa.objc_msgSend (challenge, Cocoa.S_sender);
	if (!result) {
		Cocoa.objc_msgSend (challengeSender, Cocoa.S_cancelAuthenticationChallenge, challenge);
		return;
	}

	int length = userReturn[0].length ();
	char[] buffer = new char[length];
	userReturn[0].getChars (0, length, buffer, 0);
	int user = OS.CFStringCreateWithCharacters (0, buffer, length);
	length = passwordReturn[0].length ();
	buffer = new char[length];
	passwordReturn[0].getChars (0, length, buffer, 0);
	int password = OS.CFStringCreateWithCharacters (0, buffer, length);
	int credential = Cocoa.objc_msgSend (Cocoa.C_NSURLCredential, Cocoa.S_credentialWithUser, user, password, Cocoa.NSURLCredentialPersistenceForSession);
	Cocoa.objc_msgSend (challengeSender, Cocoa.S_useCredential, credential, challenge);
	OS.CFRelease (password);
	OS.CFRelease (user);
}

boolean showAuthenticationDialog (final String[] user, final String[] password, String host, String realm) {
	final Shell shell = new Shell (browser.getShell ());
	shell.setLayout (new GridLayout ());
	String title = SWT.getMessage ("SWT_Authentication_Required"); //$NON-NLS-1$
	shell.setText (title);
	Label label = new Label (shell, SWT.WRAP);
	label.setText (Compatibility.getMessage ("SWT_Enter_Username_and_Password", new String[] {realm, host})); //$NON-NLS-1$

	GridData data = new GridData ();
	Monitor monitor = browser.getMonitor ();
	int maxWidth = monitor.getBounds ().width * 2 / 3;
	int width = label.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	data.widthHint = Math.min (width, maxWidth);
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	label.setLayoutData (data);

	Label userLabel = new Label (shell, SWT.NONE);
	userLabel.setText (SWT.getMessage ("SWT_Username")); //$NON-NLS-1$

	final Text userText = new Text (shell, SWT.BORDER);
	if (user[0] != null) userText.setText (user[0]);
	data = new GridData ();
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	userText.setLayoutData (data);

	Label passwordLabel = new Label (shell, SWT.NONE);
	passwordLabel.setText (SWT.getMessage ("SWT_Password")); //$NON-NLS-1$

	final Text passwordText = new Text (shell, SWT.PASSWORD | SWT.BORDER);
	if (password[0] != null) passwordText.setText (password[0]);
	data = new GridData ();
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	passwordText.setLayoutData (data);

	final boolean[] result = new boolean[1];
	final Button[] buttons = new Button[2];
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			user[0] = userText.getText();
			password[0] = passwordText.getText();
			result[0] = event.widget == buttons[1];
			shell.close();
		}	
	};

	Composite composite = new Composite (shell, SWT.NONE);
	data = new GridData ();
	data.horizontalAlignment = GridData.END;
	composite.setLayoutData (data);
	composite.setLayout (new GridLayout (2, true));
	buttons[0] = new Button (composite, SWT.PUSH);
	buttons[0].setText (SWT.getMessage("SWT_Cancel")); //$NON-NLS-1$
	buttons[0].setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	buttons[0].addListener (SWT.Selection, listener);
	buttons[1] = new Button (composite, SWT.PUSH);
	buttons[1].setText (SWT.getMessage("SWT_OK")); //$NON-NLS-1$
	buttons[1].setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	buttons[1].addListener (SWT.Selection, listener);

	shell.setDefaultButton (buttons[1]);
	shell.pack ();
	shell.open ();
	Display display = browser.getDisplay ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}

	return result[0];
}

int identifierForInitialRequest(int request, int dataSource) {
	final Display display = browser.getDisplay();
	final ProgressEvent progress = new ProgressEvent(browser);
	progress.display = display;
	progress.widget = browser;
	progress.current = resourceCount;
	progress.total = Math.max(resourceCount, MAX_PROGRESS);
	for (int i = 0; i < progressListeners.length; i++) {
		final ProgressListener listener = progressListeners[i];
		/*
		* Note on WebKit.  Running the event loop from a Browser
		* delegate callback breaks the WebKit (stop loading or
		* crash).  The widget ProgressBar currently touches the
		* event loop every time the method setSelection is called.  
		* The workaround is to invoke Display.asyncexec so that
		* the Browser does not crash when the user updates the 
		* selection of the ProgressBar.
		*/
		display.asyncExec(
			new Runnable() {
				public void run() {
					if (!display.isDisposed() && !browser.isDisposed())
						listener.changed(progress);
				}
			}
		);
	}

	/*
	* Note.  numberWithInt uses autorelease.  The resulting object
	* does not need to be released.
	* identifier = [NSNumber numberWithInt: resourceCount++]
	*/	
	int identifier = Cocoa.objc_msgSend(Cocoa.C_NSNumber, Cocoa.S_numberWithInt, resourceCount++);
		
	if (this.identifier == 0) {
		int frame = Cocoa.objc_msgSend(dataSource, Cocoa.S_webFrame);
		if (frame == Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame)) this.identifier = identifier;
	}
	return identifier;
		
}

int willSendRequest(int identifier, int request, int redirectResponse, int dataSource) {
	return request;
}

/* handleNotification */

void handleNotification(int notification) {	
}

/* UIDelegate */
int createWebViewWithRequest(int request) {
	WindowEvent newEvent = new WindowEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.required = true;
	if (openWindowListeners != null) {
		for (int i = 0; i < openWindowListeners.length; i++) {
			openWindowListeners[i].open(newEvent);
		}
	}

	int webView = 0;
	Browser browser = null;
	if (newEvent.browser != null && newEvent.browser.webBrowser instanceof Safari) {
		browser = newEvent.browser;
	}
	if (browser != null && !browser.isDisposed()) {
		webView = ((Safari)browser.webBrowser).webView;
		
		if (request != 0) {
			//mainFrame = [webView mainFrame];
			int mainFrame= Cocoa.objc_msgSend(webView, Cocoa.S_mainFrame);

			//[mainFrame loadRequest:request];
			Cocoa.objc_msgSend(mainFrame, Cocoa.S_loadRequest, request);
		}
	}
	return webView;
}

void webViewShow(int sender) {
	/*
	* Feature on WebKit.  The Safari WebKit expects the application
	* to create a new Window using the Objective C Cocoa API in response
	* to UIDelegate.createWebViewWithRequest. The application is then
	* expected to use Objective C Cocoa API to make this window visible
	* when receiving the UIDelegate.webViewShow message.  For some reason,
	* a window created with the Carbon API hosting the new browser instance
	* does not redraw until it has been resized.  The fix is to increase the
	* size of the Shell and restore it to its initial size.
	*/
	Shell parent = browser.getShell();
	Point pt = parent.getSize();
	parent.setSize(pt.x+1, pt.y);
	parent.setSize(pt.x, pt.y);
	WindowEvent newEvent = new WindowEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	if (location != null) newEvent.location = location;
	if (size != null) newEvent.size = size;
	/*
	* Feature in Safari.  Safari's tool bar contains
	* the address bar.  The address bar is displayed
	* if the tool bar is displayed. There is no separate
	* notification for the address bar.
	* Feature in Safari.  The menu bar is always
	* displayed. There is no notification to hide
	* the menu bar.
	*/
	newEvent.addressBar = toolBar;
	newEvent.menuBar = true;
	newEvent.statusBar = statusBar;
	newEvent.toolBar = toolBar;
	for (int i = 0; i < visibilityWindowListeners.length; i++) {
		visibilityWindowListeners[i].show(newEvent);
	}
	location = null;
	size = null;
}

void setFrame(int frame) {
	float[] dest = new float[4];
	OS.memmove(dest, frame, 16);
	/* convert to SWT system coordinates */
	Rectangle bounds = browser.getDisplay().getBounds();
	location = new Point((int)dest[0], bounds.height - (int)dest[1] - (int)dest[3]);
	size = new Point((int)dest[2], (int)dest[3]);
}

void webViewFocus() {
}

void webViewUnfocus() {
}

void runJavaScriptAlertPanelWithMessage(int message) {
	int length = OS.CFStringGetLength(message);
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(message, range, buffer);
	String text = new String(buffer);

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.ICON_WARNING);
	messageBox.setText("Javascript");	//$NON-NLS-1$
	messageBox.setMessage(text);
	messageBox.open();
}

int runJavaScriptConfirmPanelWithMessage(int message) {
	int length = OS.CFStringGetLength(message);
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(message, range, buffer);
	String text = new String(buffer);

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
	messageBox.setText("Javascript");	//$NON-NLS-1$
	messageBox.setMessage(text);
	return messageBox.open() == SWT.OK ? 1 : 0;
}

void runOpenPanelForFileButtonWithResultListener(int resultListener) {
	FileDialog dialog = new FileDialog(browser.getShell(), SWT.NONE);
	String result = dialog.open();
	if (result == null) {
		Cocoa.objc_msgSend(resultListener, Cocoa.S_cancel);
		return;
	}
	int length = result.length();
	char[] buffer = new char[length];
	result.getChars(0, length, buffer, 0);
	int filename = OS.CFStringCreateWithCharacters(0, buffer, length);
	Cocoa.objc_msgSend(resultListener, Cocoa.S_chooseFilename, filename);
	OS.CFRelease(filename);
}

void webViewClose() {
	Shell parent = browser.getShell();
	WindowEvent newEvent = new WindowEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	for (int i = 0; i < closeWindowListeners.length; i++) {
		closeWindowListeners[i].close(newEvent);
	}
	browser.dispose();
	if (parent.isDisposed()) return;
	/*
	* Feature on WebKit.  The Safari WebKit expects the application
	* to create a new Window using the Objective C Cocoa API in response
	* to UIDelegate.createWebViewWithRequest. The application is then
	* expected to use Objective C Cocoa API to make this window visible
	* when receiving the UIDelegate.webViewShow message.  For some reason,
	* a window created with the Carbon API hosting the new browser instance
	* does not redraw until it has been resized.  The fix is to increase the
	* size of the Shell and restore it to its initial size.
	*/
	Point pt = parent.getSize();
	parent.setSize(pt.x+1, pt.y);
	parent.setSize(pt.x, pt.y);
}

int contextMenuItemsForElement(int element, int defaultMenuItems) {
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point();
	OS.GetGlobalMouse(pt);
	Event event = new Event();
	event.x = pt.h;
	event.y = pt.v;
	browser.notifyListeners(SWT.MenuDetect, event);
	if (!event.doit || browser.isDisposed()) return 0;
	Menu menu = browser.getMenu();
	if (menu != null && !menu.isDisposed()) {
		if (event.x != pt.h || event.y != pt.v) {
			menu.setLocation(event.x, event.y);
		}
		menu.setVisible(true);
		return 0;
	}
	return defaultMenuItems;
}

void setStatusBarVisible(int visible) {
	/* Note.  Webkit only emits the notification when the status bar should be hidden. */
	statusBar = visible != 0;
}

void setStatusText(int text) {
	int length = OS.CFStringGetLength(text);
	if (length == 0) return;
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(text, range, buffer);

	StatusTextEvent statusText = new StatusTextEvent(browser);
	statusText.display = browser.getDisplay();
	statusText.widget = browser;
	statusText.text = new String(buffer);
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed(statusText);
	}
}

void setResizable(int visible) {
}

void setToolbarsVisible(int visible) {
	/* Note.  Webkit only emits the notification when the tool bar should be hidden. */
	toolBar = visible != 0;
}

void mouseDidMoveOverElement (int elementInformation, int modifierFlags) {
	if (elementInformation == 0) return;
	if (!browser.isEnabled ()) return;

	int length = WebElementLinkURLKey.length();
	char[] chars = new char[length];
	WebElementLinkURLKey.getChars(0, length, chars, 0);
	int key = OS.CFStringCreateWithCharacters(0, chars, length);
	int value = Cocoa.objc_msgSend(elementInformation, Cocoa.S_valueForKey, key);
	OS.CFRelease(key);
	if (value == 0) {
		/* not currently over a link */
		if (lastHoveredLinkURL == null) return;
		lastHoveredLinkURL = null;
		StatusTextEvent statusText = new StatusTextEvent(browser);
		statusText.display = browser.getDisplay();
		statusText.widget = browser;
		statusText.text = "";	//$NON-NLS-1$
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed(statusText);
		}
		return;
	}

	int stringPtr = Cocoa.objc_msgSend(value, Cocoa.S_absoluteString);
	length = OS.CFStringGetLength(stringPtr);
	String urlString;
	if (length == 0) {
		urlString = "";	//$NON-NLS-1$
	} else {
		chars = new char[length];
		CFRange range = new CFRange();
		range.length = length;
		OS.CFStringGetCharacters(stringPtr, range, chars);
		urlString = new String(chars);
	}
	if (urlString.equals(lastHoveredLinkURL)) return;

	lastHoveredLinkURL = urlString;
	StatusTextEvent statusText = new StatusTextEvent(browser);
	statusText.display = browser.getDisplay();
	statusText.widget = browser;
	statusText.text = urlString;
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed(statusText);
	}
}

/* PolicyDelegate */

void decidePolicyForMIMEType(int type, int request, int frame, int listener) {
	boolean canShow = Cocoa.objc_msgSend(Cocoa.C_WebView, Cocoa.S_canShowMIMEType, type) != 0;
	Cocoa.objc_msgSend(listener, canShow ? Cocoa.S_use : Cocoa.S_download);
}

void decidePolicyForNavigationAction(int actionInformation, int request, int frame, int listener) {
	int url = Cocoa.objc_msgSend(request, Cocoa.S_URL);
	if (url == 0) {
		/* indicates that a URL with an invalid format was specified */
		Cocoa.objc_msgSend(listener, Cocoa.S_ignore);
		return;
	}
	int s = Cocoa.objc_msgSend(url, Cocoa.S_absoluteString);
	int length = OS.CFStringGetLength(s);
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(s, range, buffer);
	String url2 = new String(buffer);
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FROMMEMORY)) url2 = ABOUT_BLANK;

	if (url2.startsWith (URI_APPLEWEBDATA)) {
		/* listeners should not be notified of internal transitions like this */
		Cocoa.objc_msgSend(listener, Cocoa.S_use);
	} else {
		LocationEvent newEvent = new LocationEvent(browser);
		newEvent.display = browser.getDisplay();
		newEvent.widget = browser;
		newEvent.location = url2;
		newEvent.doit = true;
		if (locationListeners != null) {
			changingLocation = true;
			for (int i = 0; i < locationListeners.length; i++) {
				locationListeners[i].changing(newEvent);
			}
			changingLocation = false;
		}
		if (newEvent.doit) {
			if (jsEnabledChanged) {
				jsEnabledChanged = false;
				if (preferences == 0) {
					preferences = Cocoa.objc_msgSend (Cocoa.C_WebPreferences, Cocoa.S_alloc);
					Cocoa.objc_msgSend (preferences, Cocoa.S_init);
					Cocoa.objc_msgSend (webView, Cocoa.S_setPreferences, preferences);
				}
				Cocoa.objc_msgSend (preferences, Cocoa.S_setJavaScriptEnabled, jsEnabled ? 1 : 0);
			}
			lastNavigateURL = url2;
		}
		Cocoa.objc_msgSend(listener, newEvent.doit ? Cocoa.S_use : Cocoa.S_ignore);
	}

	if (html != null && !browser.isDisposed()) {
		String html = this.html;
		this.html = null;
		_setText(html);
	}
}

void decidePolicyForNewWindowAction(int actionInformation, int request, int frameName, int listener) {
	Cocoa.objc_msgSend(listener, Cocoa.S_use);
}

void unableToImplementPolicyWithError(int error, int frame) {
}

/* WebDownload */

void decideDestinationWithSuggestedFilename (int download, int filename) {
	int length = OS.CFStringGetLength(filename);
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(filename, range, buffer);
	String name = new String(buffer);
	FileDialog dialog = new FileDialog(browser.getShell(), SWT.SAVE);
	dialog.setText(SWT.getMessage ("SWT_FileDownload")); //$NON-NLS-1$
	dialog.setFileName(name);
	String path = dialog.open();
	if (path == null) {
		/* cancel pressed */
		Cocoa.objc_msgSend(download, Cocoa.S_cancel);
		return;
	}
	length = path.length();
	char[] chars = new char[length];
	path.getChars(0, length, chars, 0);
	int result = OS.CFStringCreateWithCharacters(0, chars, length);
	Cocoa.objc_msgSend(download, Cocoa.S_setDestinationAllowOverwrite, result, 1);
	OS.CFRelease(result);
}

/* DOMEventListener */

void handleEvent(int evt) {
	if (!browser.isEnabled ()) return;

	int type = Cocoa.objc_msgSend(evt, Cocoa.S_type);
	int length = OS.CFStringGetLength(type);
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(type, range, buffer);
	String typeString = new String(buffer);

	if (typeString.equals(DOMEVENT_FOCUSIN)) {
		hasNewFocusElement = true;
		return;
	}
	if (typeString.equals(DOMEVENT_FOCUSOUT)) {
		hasNewFocusElement = false;
		return;
	}

	boolean ctrl = Cocoa.objc_msgSend(evt, Cocoa.S_ctrlKey) != 0;
	boolean shift = Cocoa.objc_msgSend(evt, Cocoa.S_shiftKey) != 0;
	boolean alt = Cocoa.objc_msgSend(evt, Cocoa.S_altKey) != 0;
	boolean meta = Cocoa.objc_msgSend(evt, Cocoa.S_metaKey) != 0;

	if (DOMEVENT_KEYDOWN.equals(typeString) || DOMEVENT_KEYUP.equals(typeString)) {
		int keyCode = Cocoa.objc_msgSend(evt, Cocoa.S_keyCode);
		int charCode = Cocoa.objc_msgSend(evt, Cocoa.S_charCode);

		Event keyEvent = new Event();
		keyEvent.widget = browser;
		if (DOMEVENT_KEYDOWN.equals(typeString)) {
			keyEvent.type = SWT.KeyDown;
		} else {
			keyEvent.type = SWT.KeyUp;
		}
		keyEvent.keyCode = translateKey(keyCode);
		/*
		* Safari maps the Delete key's character to 0xf728.  Detect
		* this key and set its character to SWT.DEL so that the
		* Browser's key events are consistent with other controls.
		*/
		if (keyEvent.keyCode == SWT.DEL) {
			keyEvent.character = SWT.DEL;
		} else {
			keyEvent.character = (char)charCode;
		}
		keyEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		browser.notifyListeners(keyEvent.type, keyEvent);
		if (browser.isDisposed()) {
			Cocoa.objc_msgSend(evt, Cocoa.S_preventDefault);
			return;
		}

		boolean doit = keyEvent.doit;
		/*
		* Bug in Safari.  As a result of using HIWebViewCreate on OSX versions < 10.5 (Leopard), attempting
		* to traverse out of Safari backwards (Shift+Tab) leaves it in a strange state where Safari no
		* longer has focus but still receives keys.  The Carbon-based Safari examples have the same
		* problem.  The workaround is to only allow forward Tab traversals in the Browser on OSX < 10.5.
		*/
		if (doit && OS.VERSION < 0x1050 && keyEvent.keyCode == SWT.TAB && (keyEvent.stateMask & SWT.SHIFT) != 0) {
			doit = false;
		}
		if (!doit) {
			Cocoa.objc_msgSend(evt, Cocoa.S_preventDefault);
		} else {
			if (!hasNewFocusElement && keyEvent.keyCode == SWT.TAB && DOMEVENT_KEYUP.equals(typeString)) {
				browser.traverse(SWT.TRAVERSE_TAB_NEXT);
				hasNewFocusElement = false;
			}
		}
		return;
	}

	/* mouse event */

	int clientX = Cocoa.objc_msgSend(evt, Cocoa.S_clientX);
	int clientY = Cocoa.objc_msgSend(evt, Cocoa.S_clientY);
	int detail = Cocoa.objc_msgSend(evt, Cocoa.S_detail);

	Event mouseEvent = new Event ();
	mouseEvent.widget = browser;
	mouseEvent.x = clientX; mouseEvent.y = clientY;
	mouseEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);

	if (DOMEVENT_MOUSEDOWN.equals (typeString)) {
		mouseEvent.type = SWT.MouseDown;
		int button = Cocoa.objc_msgSend(evt, Cocoa.S_button);
		mouseEvent.button = button + 1;
		mouseEvent.count = detail;
	} else if (DOMEVENT_MOUSEUP.equals (typeString)) {
		mouseEvent.type = SWT.MouseUp;
		int button = Cocoa.objc_msgSend(evt, Cocoa.S_button);
		mouseEvent.button = button + 1;
		mouseEvent.count = detail;
		switch (mouseEvent.button) {
			case 1: mouseEvent.stateMask |= SWT.BUTTON1; break;
			case 2: mouseEvent.stateMask |= SWT.BUTTON2; break;
			case 3: mouseEvent.stateMask |= SWT.BUTTON3; break;
			case 4: mouseEvent.stateMask |= SWT.BUTTON4; break;
			case 5: mouseEvent.stateMask |= SWT.BUTTON5; break;
		}
	} else if (DOMEVENT_MOUSEMOVE.equals (typeString)) {
		/*
		* Feature in Safari.  Spurious and redundant mousemove events are received in
		* various contexts, including following every MouseUp.  The workaround is to
		* not fire MouseMove events whose x and y values match the last MouseMove.  
		*/
		if (mouseEvent.x == lastMouseMoveX && mouseEvent.y == lastMouseMoveY) return;
		mouseEvent.type = SWT.MouseMove;
		lastMouseMoveX = mouseEvent.x; lastMouseMoveY = mouseEvent.y;
	} else if (DOMEVENT_MOUSEWHEEL.equals (typeString)) {
		mouseEvent.type = SWT.MouseWheel;
		int delta = Cocoa.objc_msgSend(evt, Cocoa.S_wheelDelta);
		mouseEvent.count = delta / 120;
	}

	browser.notifyListeners (mouseEvent.type, mouseEvent);
	if (browser.isDisposed()) return;
	if (detail == 2 && DOMEVENT_MOUSEDOWN.equals (typeString)) {
		int button = Cocoa.objc_msgSend(evt, Cocoa.S_button);
		mouseEvent = new Event ();
		mouseEvent.widget = browser;
		mouseEvent.x = clientX; mouseEvent.y = clientY;
		mouseEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		mouseEvent.type = SWT.MouseDoubleClick;
		mouseEvent.button = button + 1;
		mouseEvent.count = detail;
		browser.notifyListeners (mouseEvent.type, mouseEvent);
	}
}

/* external */

Object convertToJava (int value) {
	if (Cocoa.objc_msgSend (value, Cocoa.S_isKindOfClass, Cocoa.C_NSString) != 0) {
		int length = Cocoa.objc_msgSend (value, Cocoa.S_length);
		char[] buffer = new char[length];
		Cocoa.objc_msgSend (value, Cocoa.S_getCharacters_, buffer);
		return new String (buffer);
	}
	if (Cocoa.objc_msgSend (value, Cocoa.S_isKindOfClass, Cocoa.C_NSNumber) != 0) {
		int ptr = Cocoa.objc_msgSend (value, Cocoa.S_objCType);
		byte[] type = new byte[1];
		OS.memmove (type, ptr, 1);
		if (type[0] == 'c' || type[0] == 'B') {
			int result = Cocoa.objc_msgSend (value, Cocoa.S_boolValue);
			return new Boolean (result != 0);
		}
		if ("islqISLQfd".indexOf (type[0]) != -1) { //$NON-NLS-1$
			double result = Cocoa.objc_msgSend_fpret (value, Cocoa.S_doubleValue);
			return new Double (result);
		}
	}
	if (Cocoa.objc_msgSend (value, Cocoa.S_isKindOfClass, Cocoa.C_WebScriptObject) != 0) {
		String string = "length"; //$NON-NLS-1$
		char[] chars = new char[string.length ()];
		string.getChars (0, chars.length, chars, 0);
		int str = OS.CFStringCreateWithCharacters (0, chars, chars.length);
		int numberValue = Cocoa.objc_msgSend (value, Cocoa.S_valueForKey, str);
		OS.CFRelease (str);
		int length = Cocoa.objc_msgSend (numberValue, Cocoa.S_intValue);
		Object[] arguments = new Object[length];
		for (int i = 0; i < length; i++) {
			int current = Cocoa.objc_msgSend (value, Cocoa.S_webScriptValueAtIndex, i);
			if (current != 0) {
				arguments[i] = convertToJava (current);
			}
		}
		return arguments;
	}
	if (Cocoa.objc_msgSend (value, Cocoa.S_isKindOfClass, Cocoa.C_WebUndefined) != 0) {
		return null;
	}

	SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	return null;
}

int convertToJS (Object value) {
	if (value == null) {
		return Cocoa.objc_msgSend (Cocoa.C_WebUndefined, Cocoa.S_undefined);
	}
	if (value instanceof String) {
		String result = (String)value;
		char[] chars = new char[result.length ()];
		result.getChars (0, chars.length, chars, 0);
		return OS.CFStringCreateWithCharacters (0, chars, chars.length);
	}
	if (value instanceof Boolean) {
		int booleanValue = ((Boolean)value).booleanValue () ? 1 : 0;
		return Cocoa.objc_msgSend (Cocoa.C_NSNumber, Cocoa.S_numberWithBool, booleanValue);
	}
	if (value instanceof Number) {
		double doubleValue = ((Number)value).doubleValue ();
		return Cocoa.objc_msgSend (Cocoa.C_NSNumber, Cocoa.S_numberWithDouble, doubleValue);
	}
	if (value instanceof Object[]) {
		Object[] arrayValue = (Object[])value;
		int length = arrayValue.length;
		if (length > 0) {
			int array = Cocoa.objc_msgSend (Cocoa.C_NSMutableArray, Cocoa.S_arrayWithCapacity, length);
			for (int i = 0; i < length; i++) {
				Object currentObject = arrayValue[i];
				int jsObject = convertToJS (currentObject);
				Cocoa.objc_msgSend (array, Cocoa.S_addObject, jsObject);
			}
			return array;
		}
	}
	SWT.error (SWT.ERROR_INVALID_RETURN_VALUE);
	return 0;
}

int /*long*/ callJava (int /*long*/ index, int /*long*/ args, int /*long*/ arg1) {
	Object returnValue = null;
	if (Cocoa.objc_msgSend (index, Cocoa.S_isKindOfClass, Cocoa.C_NSNumber) != 0) {
		int functionIndex = Cocoa.objc_msgSend (index, Cocoa.S_intValue);
		Object key = new Integer (functionIndex);
		BrowserFunction function = (BrowserFunction)functions.get (key);
		if (function != null) {
			try {
				Object temp = convertToJava (args);
				if (temp instanceof Object[]) {
					Object[] arguments = (Object[])temp;
					try {
						returnValue = function.function (arguments);
					} catch (Exception e) {
						/* exception during function invocation */
						returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
					}
				}
			} catch (IllegalArgumentException e) {
				/* invalid argument value type */
				if (function.isEvaluate) {
					/* notify the evaluate function so that a java exception can be thrown */
					function.function (new String[] {WebBrowser.CreateErrorString (new SWTException (SWT.ERROR_INVALID_RETURN_VALUE).getLocalizedMessage ())});
				}
				returnValue = WebBrowser.CreateErrorString (e.getLocalizedMessage ());
			}
		}
	}
	try {
		return convertToJS (returnValue);
	} catch (SWTException e) {
		return convertToJS (WebBrowser.CreateErrorString (e.getLocalizedMessage ()));
	}
}
}

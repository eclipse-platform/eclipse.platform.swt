/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
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
import java.util.Enumeration;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class WebKit extends WebBrowser {
	WebView webView;
	WebPreferences preferences;
	SWTWebViewDelegate delegate;
	boolean loadingText, untrustedText;
	String lastHoveredLinkURL, lastNavigateURL;
	String html;
	long /*int*/ identifier;
	int resourceCount;
	String url = ""; //$NON-NLS-1$
	Point location;
	Point size;
	boolean statusBar = true, toolBar = true, ignoreDispose;
	int lastMouseMoveX, lastMouseMoveY;
	//TEMPORARY CODE
//	boolean doit;

	static long /*int*/ delegateClass;
	static boolean Initialized;
	// the following Callbacks are never freed
	static Callback Callback3, Callback4, Callback5, Callback6, Callback7;

	static final int MIN_SIZE = 16;
	static final int MAX_PROGRESS = 100;
	static final String WebElementLinkURLKey = "WebElementLinkURL"; //$NON-NLS-1$
	static final String AGENT_STRING = "Safari/522.0"; /* Safari version on OSX 10.5 initial release */ //$NON-NLS-1$
	static final String URI_FILEROOT = "file:///"; //$NON-NLS-1$
	static final String PROTOCOL_FILE = "file://"; //$NON-NLS-1$
	static final String PROTOCOL_HTTP = "http://"; //$NON-NLS-1$
	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String HEADER_SETCOOKIE = "Set-Cookie"; //$NON-NLS-1$
	static final String POST = "POST"; //$NON-NLS-1$
	static final String USER_AGENT = "user-agent"; //$NON-NLS-1$
	static final String ADD_WIDGET_KEY = "org.eclipse.swt.internal.addWidget"; //$NON-NLS-1$
	static final String WEBKIT_EVENTS_FIX_KEY = "org.eclipse.swt.internal.webKitEventsFix"; //$NON-NLS-1$
	static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};

	/* event strings */
	static final String DOMEVENT_KEYUP = "keyup"; //$NON-NLS-1$
	static final String DOMEVENT_KEYDOWN = "keydown"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEDOWN = "mousedown"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEUP = "mouseup"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEMOVE = "mousemove"; //$NON-NLS-1$
	static final String DOMEVENT_MOUSEWHEEL = "mousewheel"; //$NON-NLS-1$

	static {
		NativeClearSessions = new Runnable() {
			public void run() {
				NSHTTPCookieStorage storage = NSHTTPCookieStorage.sharedHTTPCookieStorage();
				NSArray cookies = storage.cookies();
				int count = (int)/*64*/cookies.count ();
				for (int i = 0; i < count; i++) {
					NSHTTPCookie cookie = new NSHTTPCookie(cookies.objectAtIndex(i));
					if (cookie.isSessionOnly()) {
						storage.deleteCookie(cookie);
					}
				}
			}
		};

		NativeGetCookie = new Runnable () {
			public void run () {
				NSHTTPCookieStorage storage = NSHTTPCookieStorage.sharedHTTPCookieStorage ();
				NSURL url = NSURL.URLWithString (NSString.stringWith (CookieUrl));
				NSArray cookies = storage.cookiesForURL (url);
				int count = (int)/*64*/cookies.count ();
				if (count == 0) return;

				NSString name = NSString.stringWith (CookieName);
				for (int i = 0; i < count; i++) {
					NSHTTPCookie current = new NSHTTPCookie (cookies.objectAtIndex (i));
					if (current.name ().compare (name) == OS.NSOrderedSame) {
						CookieValue = current.value ().getString ();
						return;
					}
				}
			}
		};

		NativeSetCookie = new Runnable () {
			public void run () {
				NSURL url = NSURL.URLWithString (NSString.stringWith (CookieUrl));
				NSMutableDictionary headers = NSMutableDictionary.dictionaryWithCapacity (1);
				headers.setValue (NSString.stringWith (CookieValue), NSString.stringWith (HEADER_SETCOOKIE));
				NSArray cookies = NSHTTPCookie.cookiesWithResponseHeaderFields (headers, url);
				if (cookies.count () == 0) return;
				NSHTTPCookieStorage storage = NSHTTPCookieStorage.sharedHTTPCookieStorage ();
				NSHTTPCookie cookie = new NSHTTPCookie (cookies.objectAtIndex (0));
				storage.setCookie (cookie);
				CookieResult = true;
			}
		};

		if (NativePendingCookies != null) {
			SetPendingCookies (NativePendingCookies);
		}
		NativePendingCookies = null;
	}

public void create (Composite parent, int style) {
	if (delegateClass == 0) {
		Class webKitClass = this.getClass();
		Callback3 = new Callback(webKitClass, "browserProc", 3); //$NON-NLS-1$
		long /*int*/ proc3 = Callback3.getAddress();
		if (proc3 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback4 = new Callback(webKitClass, "browserProc", 4); //$NON-NLS-1$
		long /*int*/ proc4 = Callback4.getAddress();
		if (proc4 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback5 = new Callback(webKitClass, "browserProc", 5); //$NON-NLS-1$
		long /*int*/ proc5 = Callback5.getAddress();
		if (proc5 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback6 = new Callback(webKitClass, "browserProc", 6); //$NON-NLS-1$
		long /*int*/ proc6 = Callback6.getAddress();
		if (proc6 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback7 = new Callback(webKitClass, "browserProc", 7); //$NON-NLS-1$
		long /*int*/ proc7 = Callback7.getAddress();
		if (proc7 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		long /*int*/ setFrameProc = OS.CALLBACK_webView_setFrame_(proc4);
		if (setFrameProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		String className = "SWTWebViewDelegate"; //$NON-NLS-1$
		byte[] types = {'*','\0'};
		int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
		delegateClass = OS.objc_allocateClassPair (OS.class_NSObject, className, 0);

		OS.class_addIvar(delegateClass, SWT_OBJECT, size, (byte)align, types);
		OS.class_addMethod(delegateClass, OS.sel_webView_didChangeLocationWithinPageForFrame_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_didFailProvisionalLoadWithError_forFrame_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_didFinishLoadForFrame_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_didReceiveTitle_forFrame_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_didStartProvisionalLoadForFrame_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_didCommitLoadForFrame_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_resource_didFinishLoadingFromDataSource_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_resource_didReceiveAuthenticationChallenge_fromDataSource_, proc6, "@:@@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_resource_didFailLoadingWithError_fromDataSource_, proc6, "@:@@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_identifierForInitialRequest_fromDataSource_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_resource_willSendRequest_redirectResponse_fromDataSource_, proc7, "@:@@@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_createWebViewWithRequest_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webViewShow_, proc3, "@:@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webViewClose_, proc3, "@:@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_contextMenuItemsForElement_defaultMenuItems_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_setStatusBarVisible_, proc4, "@:@B"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_setResizable_, proc4, "@:@B"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_setToolbarsVisible_, proc4, "@:@B"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_setStatusText_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webViewFocus_, proc3, "@:@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webViewUnfocus_, proc3, "@:@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_runBeforeUnloadConfirmPanelWithMessage_initiatedByFrame_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_runJavaScriptAlertPanelWithMessage_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_runJavaScriptAlertPanelWithMessage_initiatedByFrame_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_runJavaScriptConfirmPanelWithMessage_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_runJavaScriptConfirmPanelWithMessage_initiatedByFrame_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_runOpenPanelForFileButtonWithResultListener_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_mouseDidMoveOverElement_modifierFlags_, proc5, "@:@@I"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_printFrameView_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_decidePolicyForMIMEType_request_frame_decisionListener_, proc7, "@:@@@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_decidePolicyForNavigationAction_request_frame_decisionListener_, proc7, "@:@@@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener_, proc7, "@:@@@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_unableToImplementPolicyWithError_frame_, proc5, "@:@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_download_decideDestinationWithSuggestedFilename_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_handleEvent_, proc3, "@:@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_setFrame_, setFrameProc, "@:@{NSRect}"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_webView_windowScriptObjectAvailable_, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_callJava, proc6, "@:@@@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_callRunBeforeUnloadConfirmPanelWithMessage, proc4, "@:@@"); //$NON-NLS-1$
		OS.class_addMethod(delegateClass, OS.sel_createPanelDidEnd, proc5, "@:@@@"); //$NON-NLS-1$
		OS.objc_registerClassPair(delegateClass);

 		long /*int*/ metaClass = OS.objc_getMetaClass (className);
		OS.class_addMethod(metaClass, OS.sel_isSelectorExcludedFromWebScript_, proc3, "@:@"); //$NON-NLS-1$
		OS.class_addMethod(metaClass, OS.sel_webScriptNameForSelector_, proc3, "@:@"); //$NON-NLS-1$
	}

	/*
	* Override the default event mechanism to not send key events so
	* that the browser can send them by listening to the DOM instead.
	*/
	browser.setData(WEBKIT_EVENTS_FIX_KEY);

	WebView webView = (WebView)new WebView().alloc();
	if (webView == null) SWT.error(SWT.ERROR_NO_HANDLES);
	webView.initWithFrame(browser.view.frame(), null, null);
	webView.setAutoresizingMask(OS.NSViewWidthSizable | OS.NSViewHeightSizable);
	if (webView.respondsToSelector(OS.sel__setDashboardBehavior)) {
		OS.objc_msgSend(webView.id, OS.sel__setDashboardBehavior, 2, 1);
	}
	final SWTWebViewDelegate delegate = (SWTWebViewDelegate)new SWTWebViewDelegate().alloc().init();
	Display display = browser.getDisplay();
	display.setData(ADD_WIDGET_KEY, new Object[] {delegate, browser});
	this.delegate = delegate;
	this.webView = webView;
	browser.view.addSubview(webView);

	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
				case SWT.FocusIn:
					WebKit.this.webView.window().makeFirstResponder(WebKit.this.webView);
					break;
				case SWT.Dispose: {
					/* make this handler run after other dispose listeners */
					if (ignoreDispose) {
						ignoreDispose = false;
						break;
					}
					ignoreDispose = true;
					browser.notifyListeners (e.type, e);
					e.type = SWT.NONE;

					/* Browser could have been disposed by one of the Dispose listeners */
					if (!browser.isDisposed()) {
						/* invoke onbeforeunload handlers */
						if (!browser.isClosing) {
							close (false);
						}

						e.display.setData(ADD_WIDGET_KEY, new Object[] {delegate, null});
					}

					WebKit.this.webView.setFrameLoadDelegate(null);
					WebKit.this.webView.setResourceLoadDelegate(null);
					WebKit.this.webView.setUIDelegate(null);
					WebKit.this.webView.setPolicyDelegate(null);
					WebKit.this.webView.setDownloadDelegate(null);

					WebKit.this.webView.release();
					WebKit.this.webView = null;
					WebKit.this.delegate.release();
					WebKit.this.delegate = null;
					html = null;
					lastHoveredLinkURL = lastNavigateURL = null;

					Enumeration elements = functions.elements ();
					while (elements.hasMoreElements ()) {
						((BrowserFunction)elements.nextElement ()).dispose (false);
					}
					functions = null;

					if (preferences != null) preferences.release ();
					preferences = null;
					break;
				}
			}
		}
	};
	browser.addListener(SWT.Dispose, listener);
	browser.addListener(SWT.KeyDown, listener); /* needed for tabbing into the Browser */
	browser.addListener(SWT.FocusIn, listener);

	webView.setFrameLoadDelegate(delegate);
	webView.setResourceLoadDelegate(delegate);
	webView.setUIDelegate(delegate);	
	webView.setPolicyDelegate(delegate);
	webView.setDownloadDelegate(delegate);
	webView.setApplicationNameForUserAgent(NSString.stringWith(AGENT_STRING));

	if (!Initialized) {
		Initialized = true;
		/* disable applets */
		WebPreferences.standardPreferences().setJavaEnabled(false);
	}
}

public boolean back() {
	html = null;	
	return webView.goBack();
}

static long /*int*/ browserProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0) {
	if (id == delegateClass) {
		if (sel == OS.sel_isSelectorExcludedFromWebScript_) {
			return isSelectorExcludedFromWebScript (arg0) ? 1 : 0;
		} else if (sel == OS.sel_webScriptNameForSelector_) {
			return webScriptNameForSelector (arg0);
		}
	}

	Display d = Display.getCurrent();
	if (d == null || d.isDisposed()) return 0;
	Widget widget = d.findWidget(id);
	if (widget == null) return 0;
	WebKit webKit = (WebKit)((Browser)widget).webBrowser;
	if (sel == OS.sel_webViewShow_) {
		webKit.webViewShow(arg0);
	} else if (sel == OS.sel_webViewClose_) {
		webKit.webViewClose(arg0);
	} else if (sel == OS.sel_webViewFocus_) {
		webKit.webViewFocus(arg0);
	} else if (sel == OS.sel_webViewUnfocus_) {
		webKit.webViewUnfocus(arg0);
	} else if (sel == OS.sel_handleEvent_) {
		webKit.handleEvent(arg0);
	}
	return 0;
}

static long /*int*/ browserProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1) {
	Display d = Display.getCurrent();
	if (d == null || d.isDisposed()) return 0;
	Widget widget = d.findWidget(id);
	if (widget == null) return 0;
	WebKit webKit = (WebKit)((Browser)widget).webBrowser;
	if (sel == OS.sel_webView_didChangeLocationWithinPageForFrame_) {
		webKit.webView_didChangeLocationWithinPageForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_didFinishLoadForFrame_) {
		webKit.webView_didFinishLoadForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_didStartProvisionalLoadForFrame_) {
		webKit.webView_didStartProvisionalLoadForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_didCommitLoadForFrame_) {
		webKit.webView_didCommitLoadForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_setFrame_) {
		webKit.webView_setFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_createWebViewWithRequest_) {
		return webKit.webView_createWebViewWithRequest(arg0, arg1);		
	} else if (sel == OS.sel_webView_setStatusBarVisible_) {
		webKit.webView_setStatusBarVisible(arg0, arg1 != 0);
	} else if (sel == OS.sel_webView_setResizable_) {
		webKit.webView_setResizable(arg0, arg1 != 0);
	} else if (sel == OS.sel_webView_setStatusText_) {
		webKit.webView_setStatusText(arg0, arg1);
	} else if (sel == OS.sel_webView_setToolbarsVisible_) {
		webKit.webView_setToolbarsVisible(arg0, arg1 != 0);
	} else if (sel == OS.sel_webView_runJavaScriptAlertPanelWithMessage_) {
		webKit.webView_runJavaScriptAlertPanelWithMessage(arg0, arg1);
	} else if (sel == OS.sel_webView_runJavaScriptConfirmPanelWithMessage_) {
		return webKit.webView_runJavaScriptConfirmPanelWithMessage(arg0, arg1);
	} else if (sel == OS.sel_webView_runOpenPanelForFileButtonWithResultListener_) {
		webKit.webView_runOpenPanelForFileButtonWithResultListener(arg0, arg1);
	} else if (sel == OS.sel_download_decideDestinationWithSuggestedFilename_) {
		webKit.download_decideDestinationWithSuggestedFilename(arg0, arg1);
	} else if (sel == OS.sel_webView_printFrameView_) {
		webKit.webView_printFrameView(arg0, arg1);
	} else if (sel == OS.sel_webView_windowScriptObjectAvailable_) {
		webKit.webView_windowScriptObjectAvailable (arg0, arg1);
	} else if (sel == OS.sel_callRunBeforeUnloadConfirmPanelWithMessage) {
		return webKit.callRunBeforeUnloadConfirmPanelWithMessage (arg0, arg1).id;
	}
	return 0;
}

static long /*int*/ browserProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2) {
	Display d = Display.getCurrent();
	if (d == null || d.isDisposed()) return 0;
	Widget widget = d.findWidget(id);
	if (widget == null) return 0;
	WebKit webKit = (WebKit)((Browser)widget).webBrowser;
	if (sel == OS.sel_webView_didFailProvisionalLoadWithError_forFrame_) {
		webKit.webView_didFailProvisionalLoadWithError_forFrame(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_didReceiveTitle_forFrame_) {
		webKit.webView_didReceiveTitle_forFrame(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_resource_didFinishLoadingFromDataSource_) {
		webKit.webView_resource_didFinishLoadingFromDataSource(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_identifierForInitialRequest_fromDataSource_) {
		return webKit.webView_identifierForInitialRequest_fromDataSource(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_contextMenuItemsForElement_defaultMenuItems_) {
		return webKit.webView_contextMenuItemsForElement_defaultMenuItems(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_mouseDidMoveOverElement_modifierFlags_) {
		webKit.webView_mouseDidMoveOverElement_modifierFlags(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_unableToImplementPolicyWithError_frame_) {
		webKit.webView_unableToImplementPolicyWithError_frame(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_runBeforeUnloadConfirmPanelWithMessage_initiatedByFrame_) {
		return webKit.webView_runBeforeUnloadConfirmPanelWithMessage_initiatedByFrame(arg0, arg1, arg2) ? 1 : 0;
	} else if (sel == OS.sel_webView_runJavaScriptAlertPanelWithMessage_initiatedByFrame_) {
		webKit.webView_runJavaScriptAlertPanelWithMessage(arg0, arg1);
	} else if (sel == OS.sel_webView_runJavaScriptConfirmPanelWithMessage_initiatedByFrame_) {
		return webKit.webView_runJavaScriptConfirmPanelWithMessage(arg0, arg1);
	} else if (sel == OS.sel_createPanelDidEnd) {
		webKit.createPanelDidEnd(arg0, arg1, arg2);
	}
	return 0;
}

static long /*int*/ browserProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3) {
	Display d = Display.getCurrent();
	if (d == null || d.isDisposed()) return 0;
	Widget widget = d.findWidget(id);
	if (widget == null) return 0;
	WebKit webKit = (WebKit)((Browser)widget).webBrowser;
	if (sel == OS.sel_webView_resource_didFailLoadingWithError_fromDataSource_) {
		webKit.webView_resource_didFailLoadingWithError_fromDataSource(arg0, arg1, arg2, arg3);
	} else if (sel == OS.sel_webView_resource_didReceiveAuthenticationChallenge_fromDataSource_) {
		webKit.webView_resource_didReceiveAuthenticationChallenge_fromDataSource(arg0, arg1, arg2, arg3);
	} else if (sel == OS.sel_callJava) {
		id result = webKit.callJava(arg0, arg1, arg2, arg3);
		return result == null ? 0 : result.id;
	}
	return 0;
}

static long /*int*/ browserProc(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4) {
	Display d = Display.getCurrent();
	if (d == null || d.isDisposed()) return 0;
	Widget widget = d.findWidget(id);
	if (widget == null) return 0;
	WebKit webKit = (WebKit)((Browser)widget).webBrowser;
	if (sel == OS.sel_webView_resource_willSendRequest_redirectResponse_fromDataSource_) {
		return webKit.webView_resource_willSendRequest_redirectResponse_fromDataSource(arg0, arg1, arg2, arg3, arg4);
	} else if (sel == OS.sel_webView_decidePolicyForMIMEType_request_frame_decisionListener_) {
		webKit.webView_decidePolicyForMIMEType_request_frame_decisionListener(arg0, arg1, arg2, arg3, arg4);
	} else if (sel == OS.sel_webView_decidePolicyForNavigationAction_request_frame_decisionListener_) {
		webKit.webView_decidePolicyForNavigationAction_request_frame_decisionListener(arg0, arg1, arg2, arg3, arg4);
	} else if (sel == OS.sel_webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener_) {
		webKit.webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener(arg0, arg1, arg2, arg3, arg4);
	}
	return 0;
}

static boolean isSelectorExcludedFromWebScript (long /*int*/ aSelector) {
	return !(aSelector == OS.sel_callJava || aSelector == OS.sel_callRunBeforeUnloadConfirmPanelWithMessage);
}

static long /*int*/ webScriptNameForSelector (long /*int*/ aSelector) {
	if (aSelector == OS.sel_callJava) {
		return NSString.stringWith ("callJava").id; //$NON-NLS-1$
	}
	if (aSelector == OS.sel_callRunBeforeUnloadConfirmPanelWithMessage) {
		return NSString.stringWith ("callRunBeforeUnloadConfirmPanelWithMessage").id; //$NON-NLS-1$
	}
	return 0;
}

public boolean close () {
	return close (true);
}

boolean close (boolean showPrompters) {
	if (!jsEnabled) return true;

	String functionName = EXECUTE_ID + "CLOSE"; // $NON-NLS-1$
	StringBuffer buffer = new StringBuffer ("function "); // $NON-NLS-1$
	buffer.append (functionName);
	buffer.append ("(win) {\n"); // $NON-NLS-1$
	buffer.append ("var fn = win.onbeforeunload; if (fn != null) {try {var str = fn(); "); // $NON-NLS-1$
	if (showPrompters) {
		buffer.append ("if (str != null) { "); // $NON-NLS-1$
		buffer.append ("var result = window.external.callRunBeforeUnloadConfirmPanelWithMessage(str);"); // $NON-NLS-1$
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
	WebFrame frame = webView.mainFrame();
	long /*int*/ context = frame.globalContext();

	byte[] bytes = null;
	try {
		bytes = (script + '\0').getBytes("UTF-8"); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = (script + '\0').getBytes();
	}
	long /*int*/ scriptString = OS.JSStringCreateWithUTF8CString(bytes);

	try {
		bytes = (getUrl() + '\0').getBytes("UTF-8"); //$NON-NLS-1$
	} catch (UnsupportedEncodingException e) {
		bytes = (getUrl() + '\0').getBytes();
	}
	long /*int*/ urlString = OS.JSStringCreateWithUTF8CString(bytes);

	long /*int*/ result = OS.JSEvaluateScript(context, scriptString, 0, urlString, 0, null);
	OS.JSStringRelease(urlString);
	OS.JSStringRelease(scriptString);
	return result != 0;
}

public boolean forward () {
	html = null;
	return webView.goForward();
}

public String getBrowserType () {
	return "webkit"; //$NON-NLS-1$
}

public String getText() {
	WebFrame mainFrame = webView.mainFrame();
	WebDataSource dataSource = mainFrame.dataSource();
	if (dataSource == null) return "";	//$NON-NLS-1$
	WebDocumentRepresentation representation = dataSource.representation();
	if (representation == null) return "";	//$NON-NLS-1$
	NSString source = representation.documentSource();
	if (source == null) return "";	//$NON-NLS-1$
	return source.getString();
}

public String getUrl() {
	/* WebKit auto-navigates to about:blank at startup */
	if (url.length() == 0) return ABOUT_BLANK;

	return url;
}

public boolean isBackEnabled() {
	return webView.canGoBack();
}

public boolean isForwardEnabled() {
	return webView.canGoForward();
}

public void refresh() {
	html = null;
	webView.reload(null);
}

public boolean setText(String html, boolean trusted) {
	/*
	* If this.html is not null then the about:blank page is already being loaded,
	* so no navigate is required.  Just set the html that is to be shown.
	*/
	boolean blankLoading = this.html != null;
	this.html = html;
	untrustedText = !trusted;
	if (blankLoading) return true;

	NSURL inURL = NSURL.URLWithString(NSString.stringWith (ABOUT_BLANK));
	NSURLRequest request = NSURLRequest.requestWithURL(inURL);
	WebFrame mainFrame = webView.mainFrame();
	mainFrame.loadRequest(request);
	return true;
}

public boolean setUrl(String url, String postData, String[] headers) {
	html = null;
	lastNavigateURL = url;

	if (url.indexOf('/') == 0) {
		url = PROTOCOL_FILE + url;
	} else if (url.indexOf(':') == -1) {
		url = PROTOCOL_HTTP + url;
	}

	NSString str = NSString.stringWith(url);
	NSString unescapedStr = NSString.stringWith("%#"); //$NON-NLS-1$
	long /*int*/ ptr = OS.CFURLCreateStringByAddingPercentEscapes(0, str.id, unescapedStr.id, 0, OS.kCFStringEncodingUTF8);
	NSString escapedString = new NSString(ptr);
	NSURL inURL = NSURL.URLWithString(escapedString);
	OS.CFRelease(ptr);
	NSMutableURLRequest request = (NSMutableURLRequest)NSMutableURLRequest.requestWithURL(inURL);
	if (postData != null) {
		request.setHTTPMethod(NSString.stringWith(POST));
		byte[] bytes = postData.getBytes();
		NSData data = NSData.dataWithBytes(bytes, bytes.length);
		request.setHTTPBody(data);
	}
	if (headers != null) {
		for (int i = 0; i < headers.length; i++) {
			String current = headers[i];
			if (current != null) {
				int index = current.indexOf(':');
				if (index != -1) {
					String key = current.substring(0, index).trim();
					String value = current.substring(index + 1).trim();
					if (key.length() > 0 && value.length() > 0) {
						if (key.equalsIgnoreCase(USER_AGENT)) {
							/*
							* Feature of WebKit.  The user-agent header value cannot be overridden
							* here.  The workaround is to temporarily set the value on the WebView
							* and then remove it after the loading of the request has begun.
							*/
							webView.setCustomUserAgent(NSString.stringWith(value));
						} else {
							request.setValue(NSString.stringWith(value), NSString.stringWith(key));						
						}
					}
				}
			}
		}
	}
	WebFrame mainFrame = webView.mainFrame();
	mainFrame.loadRequest(request);
	webView.setCustomUserAgent(null);
	return true;
}

public void stop() {
	html = null;
	webView.stopLoading(null);
}

boolean translateMnemonics() {
	return false;
}

/* WebFrameLoadDelegate */

void webView_didChangeLocationWithinPageForFrame(long /*int*/ sender, long /*int*/ frameID) {
	WebFrame frame = new WebFrame(frameID);
	WebDataSource dataSource = frame.dataSource();
	NSURLRequest request = dataSource.request();
	NSURL url = request.URL();
	NSString s = url.absoluteString();
	int length = (int)/*64*/s.length();
	if (length == 0) return;
	String url2 = s.getString();
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FILEROOT)) {
		url2 = ABOUT_BLANK;
	} else {
		length = URI_FILEROOT.length ();
		if (url2.startsWith (URI_FILEROOT) && url2.charAt (length) == '#') {
			url2 = ABOUT_BLANK + url2.substring (length);
		}
	}

	final Display display = browser.getDisplay();
	boolean top = frameID == webView.mainFrame().id;
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

void webView_didFailProvisionalLoadWithError_forFrame(long /*int*/ sender, long /*int*/ error, long /*int*/ frame) {
	if (frame == webView.mainFrame().id) {
		/*
		* Feature on WebKit.  The identifier is used here as a marker for the events 
		* related to the top frame and the URL changes related to that top frame as 
		* they should appear on the location bar of a browser.  It is expected to reset
		* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
		* the identifierForInitialRequest event is received.  However, WebKit fires
		* the didFinishLoadingFromDataSource event before the entire content of the
		* top frame is loaded.  It is possible to receive multiple willSendRequest 
		* events in this interval, causing the Browser widget to send unwanted
		* Location.changing events.  For this reason, the identifier is reset to 0
		* when the top frame has either finished loading (didFinishLoadForFrame
		* event) or failed (didFailProvisionalLoadWithError).
		*/
		identifier = 0;
	}

	NSError nserror = new NSError(error);
	long /*int*/ errorCode = nserror.code();
	if (OS.NSURLErrorBadURL < errorCode) return;

	NSURL failingURL = null;
	NSDictionary info = nserror.userInfo();
	if (info != null) {
		id id = info.valueForKey(NSString.stringWith("NSErrorFailingURLKey")); //$NON-NLS-1$
		if (id != null) failingURL = new NSURL(id);
	}

	if (failingURL != null && OS.NSURLErrorServerCertificateNotYetValid <= errorCode && errorCode <= OS.NSURLErrorSecureConnectionFailed) {
		/* handle invalid certificate error */
		id certificates = info.objectForKey(NSString.stringWith("NSErrorPeerCertificateChainKey")); //$NON-NLS-1$

		long /*int*/[] policySearch = new long /*int*/[1];
		long /*int*/[] policyRef = new long /*int*/[1];
		long /*int*/[] trustRef = new long /*int*/[1];
		boolean success = false;
		int result = OS.SecPolicySearchCreate(OS.CSSM_CERT_X_509v3, 0, 0, policySearch);
		if (result == 0 && policySearch[0] != 0) {
			result = OS.SecPolicySearchCopyNext(policySearch[0], policyRef);
			if (result == 0 && policyRef[0] != 0) {
				result = OS.SecTrustCreateWithCertificates(certificates.id, policyRef[0], trustRef);
				if (result == 0 && trustRef[0] != 0) {
					SFCertificateTrustPanel panel = SFCertificateTrustPanel.sharedCertificateTrustPanel();
					String failingUrlString = failingURL.absoluteString().getString();
					String message = Compatibility.getMessage("SWT_InvalidCert_Message", new Object[] {failingUrlString}); //$NON-NLS-1$
					panel.setAlternateButtonTitle(NSString.stringWith(Compatibility.getMessage("SWT_Cancel"))); //$NON-NLS-1$
					panel.setShowsHelp(true);
					failingURL.retain();
					NSWindow window = browser.getShell().view.window();
					panel.beginSheetForWindow(window, delegate, OS.sel_createPanelDidEnd, failingURL.id, trustRef[0], NSString.stringWith(message));
					success = true;
				}
			}
		}

		if (trustRef[0] != 0) OS.CFRelease(trustRef[0]);
		if (policyRef[0] != 0) OS.CFRelease(policyRef[0]);
		if (policySearch[0] != 0) OS.CFRelease(policySearch[0]);
		if (success) return;
	}

	/* handle other types of errors */
	NSString description = nserror.localizedDescription();
	if (description != null) {
		String descriptionString = description.getString();
		String message = failingURL != null ? failingURL.absoluteString().getString() + "\n\n" : ""; //$NON-NLS-1$ //$NON-NLS-2$
		message += Compatibility.getMessage ("SWT_Page_Load_Failed", new Object[] {descriptionString}); //$NON-NLS-1$
		MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.ICON_ERROR);
		messageBox.setMessage(message);
		messageBox.open();
	}
}

void createPanelDidEnd(long /*int*/ sheet, long /*int*/ returnCode, long /*int*/ contextInfo) {
	NSURL failingURL = new NSURL(contextInfo);
	failingURL.autorelease();
	if (returnCode != OS.NSFileHandlingPanelOKButton) return;	/* nothing more to do */

	long /*int*/ method = OS.class_getClassMethod(OS.class_NSURLRequest, OS.sel_setAllowsAnyHTTPSCertificate);
	if (method != 0) {
		OS.objc_msgSend(OS.class_NSURLRequest, OS.sel_setAllowsAnyHTTPSCertificate, 1, failingURL.host().id);
		setUrl(failingURL.absoluteString().getString(), null, null);
	}
}

void webView_didFinishLoadForFrame(long /*int*/ sender, long /*int*/ frameID) {
	if (frameID == webView.mainFrame().id) {
		/*
		 * If html is not null then there is html from a previous setText() call
		 * waiting to be set into the about:blank page once it has completed loading. 
		 */
		if (html != null) {
			if (getUrl().startsWith(ABOUT_BLANK)) {
				loadingText = true;
				NSString string = NSString.stringWith(html);
				NSString URLString;
				if (untrustedText) {
					URLString = NSString.stringWith(ABOUT_BLANK);
				} else {
					URLString = NSString.stringWith(URI_FILEROOT);
				}
				NSURL URL = NSURL.URLWithString(URLString);
				WebFrame mainFrame = webView.mainFrame();
				mainFrame.loadHTMLString(string, URL);
				html = null;
			}
		}

		/*
		* The loadHTMLString() invocation above will trigger a second webView_didFinishLoadForFrame
		* callback when it is completed.  If text was just set into the browser then wait for this
		* second callback to come before sending the title or completed events.
		*/
		if (!loadingText) {
			/*
			* To be consistent with other platforms a title event should be fired when a
			* page has completed loading.  A page with a <title> tag will do this
			* automatically when the didReceiveTitle callback is received.  However a page
			* without a <title> tag will not do this by default, so fire the event
			* here with the page's url as the title.
			*/
			Display display = browser.getDisplay();
			WebFrame frame = new WebFrame(frameID);
			WebDataSource dataSource = frame.dataSource();
			if (dataSource != null) {
				NSString title = dataSource.pageTitle();
				if (title == null) {	/* page has no title */
					TitleEvent newEvent = new TitleEvent(browser);
					newEvent.display = display;
					newEvent.widget = browser;
					newEvent.title = getUrl();
					for (int i = 0; i < titleListeners.length; i++) {
						titleListeners[i].changed(newEvent);
					}
					if (browser.isDisposed()) return;
				}
			}

			ProgressEvent progress = new ProgressEvent(browser);
			progress.display = display;
			progress.widget = browser;
			progress.current = MAX_PROGRESS;
			progress.total = MAX_PROGRESS;
			for (int i = 0; i < progressListeners.length; i++) {
				progressListeners[i].completed(progress);
			}
		}
		loadingText = false;
		if (browser.isDisposed()) return;

		/*
		* Feature on WebKit.  The identifier is used here as a marker for the events 
		* related to the top frame and the URL changes related to that top frame as 
		* they should appear on the location bar of a browser.  It is expected to reset
		* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
		* the identifierForInitialRequest event is received.  However, WebKit fires
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

void hookDOMKeyListeners(long /*int*/ frameID) {
	WebFrame frame = new WebFrame(frameID);
	DOMDocument document = frame.DOMDocument();
	if (document == null) return;

	NSString type = NSString.stringWith(DOMEVENT_KEYDOWN);
	document.addEventListener(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_KEYUP);
	document.addEventListener(type, delegate, false);
}

void hookDOMMouseListeners(long /*int*/ frameID) {
	WebFrame frame = new WebFrame(frameID);
	DOMDocument document = frame.DOMDocument();
	if (document == null) return;

	NSString type = NSString.stringWith(DOMEVENT_MOUSEDOWN);
	document.addEventListener(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_MOUSEUP);
	document.addEventListener(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_MOUSEMOVE);
	document.addEventListener(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_MOUSEWHEEL);
	document.addEventListener(type, delegate, false);
}

void webView_didReceiveTitle_forFrame(long /*int*/ sender, long /*int*/ titleID, long /*int*/ frameID) {
	if (frameID == webView.mainFrame().id) {
		NSString title = new NSString(titleID);
		String newTitle = title.getString();
		TitleEvent newEvent = new TitleEvent(browser);
		newEvent.display = browser.getDisplay();
		newEvent.widget = browser;
		newEvent.title = newTitle;
		for (int i = 0; i < titleListeners.length; i++) {
			titleListeners[i].changed(newEvent);
		}
	}
}

void webView_didStartProvisionalLoadForFrame(long /*int*/ sender, long /*int*/ frameID) {
	/* 
	* This code is intentionally commented.  WebFrameLoadDelegate:didStartProvisionalLoadForFrame is
	* called before WebResourceLoadDelegate:willSendRequest and
	* WebFrameLoadDelegate:didCommitLoadForFrame.  The resource count is reset when didCommitLoadForFrame
	* is received for the top frame.
	*/
//	if (frameID == webView.mainFrame().id) {
//		/* reset resource status variables */
//		resourceCount= 0;
//	}
}

void webView_didCommitLoadForFrame(long /*int*/ sender, long /*int*/ frameID) {
	WebFrame frame = new WebFrame(frameID);
	WebDataSource dataSource = frame.dataSource();
	NSURLRequest request = dataSource.request();
	NSURL url = request.URL();
	NSString s = url.absoluteString();
	int length = (int)/*64*/s.length();
	if (length == 0) return;
	String url2 = s.getString();
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FILEROOT)) {
		url2 = ABOUT_BLANK;
	} else {
		length = URI_FILEROOT.length ();
		if (url2.startsWith (URI_FILEROOT) && url2.charAt (length) == '#') {
			url2 = ABOUT_BLANK + url2.substring (length);
		}
	}

	Display display = browser.getDisplay();
	boolean top = frameID == webView.mainFrame().id;
	if (top) {
		/* reset resource status variables */
		resourceCount = 0;		
		this.url = url2;

		/*
		* Each invocation of setText() causes webView_didCommitLoadForFrame to be invoked
		* twice, once for the initial navigate to about:blank, and once for the auto-navigate
		* to about:blank that WebKit does when loadHTMLString is invoked.  If this is the
		* first webView_didCommitLoadForFrame callback received for a setText() invocation
		* then do not send any events or re-install registered BrowserFunctions. 
		*/
		if (url2.startsWith(ABOUT_BLANK) && html != null) return;

		/* re-install registered functions */
		Enumeration elements = functions.elements ();
		while (elements.hasMoreElements ()) {
			BrowserFunction function = (BrowserFunction)elements.nextElement ();
			execute (function.functionString);
		}

		ProgressEvent progress = new ProgressEvent(browser);
		progress.display = display;
		progress.widget = browser;
		progress.current = 1;
		progress.total = MAX_PROGRESS;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].changed(progress);
		}
		if (browser.isDisposed()) return;

		StatusTextEvent statusText = new StatusTextEvent(browser);
		statusText.display = display;
		statusText.widget = browser;
		statusText.text = url2;
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed(statusText);
		}
		if (browser.isDisposed()) return;

		hookDOMKeyListeners(frameID);
	}

	hookDOMMouseListeners(frameID);

	LocationEvent location = new LocationEvent(browser);
	location.display = display;
	location.widget = browser;
	location.location = url2;
	location.top = top;
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changed(location);
	}
}

void webView_windowScriptObjectAvailable (long /*int*/ webView, long /*int*/ windowScriptObject) {
	NSObject scriptObject = new NSObject (windowScriptObject);
	NSString key = NSString.stringWith ("external"); //$NON-NLS-1$
	scriptObject.setValue (delegate, key);
}

/* WebResourceLoadDelegate */

void webView_resource_didFinishLoadingFromDataSource(long /*int*/ sender, long /*int*/ identifier, long /*int*/ dataSource) {
	/*
	* Feature on WebKit.  The identifier is used here as a marker for the events 
	* related to the top frame and the URL changes related to that top frame as 
	* they should appear on the location bar of a browser.  It is expected to reset
	* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
	* the identifierForInitialRequest event is received.  However, WebKit fires
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

void webView_resource_didFailLoadingWithError_fromDataSource(long /*int*/ sender, long /*int*/ identifier, long /*int*/ error, long /*int*/ dataSource) {
	/*
	* Feature on WebKit.  The identifier is used here as a marker for the events 
	* related to the top frame and the URL changes related to that top frame as 
	* they should appear on the location bar of a browser.  It is expected to reset
	* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
	* the identifierForInitialRequest event is received.  However, WebKit fires
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

void webView_resource_didReceiveAuthenticationChallenge_fromDataSource (long /*int*/ sender, long /*int*/ identifier, long /*int*/ challenge, long /*int*/ dataSource) {
	NSURLAuthenticationChallenge nsChallenge = new NSURLAuthenticationChallenge (challenge);

	/*
	 * Do not invoke the listeners if this challenge has been failed too many
	 * times because a listener is likely giving incorrect credentials repeatedly
	 * and will do so indefinitely.
	 */
	if (nsChallenge.previousFailureCount () < 3) {
		for (int i = 0; i < authenticationListeners.length; i++) {
			AuthenticationEvent event = new AuthenticationEvent (browser);
			event.location = lastNavigateURL;
			authenticationListeners[i].authenticate (event);
			if (!event.doit) {
				id challengeSender = nsChallenge.sender ();
				OS.objc_msgSend (challengeSender.id, OS.sel_cancelAuthenticationChallenge_, challenge);
				return;
			}
			if (event.user != null && event.password != null) {
				id challengeSender = nsChallenge.sender ();
				NSString user = NSString.stringWith (event.user);
				NSString password = NSString.stringWith (event.password);
				NSURLCredential credential = NSURLCredential.credentialWithUser (user, password, OS.NSURLCredentialPersistenceForSession);
				OS.objc_msgSend (challengeSender.id, OS.sel_useCredential_forAuthenticationChallenge_, credential.id, challenge);
				return;
			}
		}
	}

	/* no listener handled the challenge, so try to invoke the native panel */
	long /*int*/ cls = OS.class_WebPanelAuthenticationHandler;
	if (cls != 0) {
		long /*int*/ method = OS.class_getClassMethod (cls, OS.sel_sharedHandler);
		if (method != 0) {
			long /*int*/ handler = OS.objc_msgSend (cls, OS.sel_sharedHandler);
			if (handler != 0) {
				OS.objc_msgSend (handler, OS.sel_startAuthentication, challenge, webView.window ().id);
				return;
			}
		}
	}

	/* the native panel was not available, so show a custom dialog */
	String[] userReturn = new String[1], passwordReturn = new String[1];
	NSURLCredential proposedCredential = nsChallenge.proposedCredential ();
	if (proposedCredential != null) {
		userReturn[0] = proposedCredential.user ().getString ();
		if (proposedCredential.hasPassword ()) {
			passwordReturn[0] = proposedCredential.password ().getString ();
		}
	}
	NSURLProtectionSpace space = nsChallenge.protectionSpace ();
	String host = space.host ().getString () + ':' + space.port ();
	String realm = space.realm ().getString ();
	boolean result = showAuthenticationDialog (userReturn, passwordReturn, host, realm);
	if (!result) {
		id challengeSender = nsChallenge.sender ();
		OS.objc_msgSend (challengeSender.id, OS.sel_cancelAuthenticationChallenge_, challenge);
		return;
	}
	id challengeSender = nsChallenge.sender ();
	NSString user = NSString.stringWith (userReturn[0]);
	NSString password = NSString.stringWith (passwordReturn[0]);
	NSURLCredential credential = NSURLCredential.credentialWithUser (user, password, OS.NSURLCredentialPersistenceForSession);
	OS.objc_msgSend (challengeSender.id, OS.sel_useCredential_forAuthenticationChallenge_, credential.id, challenge);
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

long /*int*/ webView_identifierForInitialRequest_fromDataSource(long /*int*/ sender, long /*int*/ request, long /*int*/ dataSourceID) {
	ProgressEvent progress = new ProgressEvent(browser);
	progress.display = browser.getDisplay();
	progress.widget = browser;
	progress.current = resourceCount;
	progress.total = Math.max(resourceCount, MAX_PROGRESS);
	for (int i = 0; i < progressListeners.length; i++) {
		progressListeners[i].changed(progress);
	}
	if (browser.isDisposed()) return 0;

	NSNumber identifier = NSNumber.numberWithInt(resourceCount++);
	if (this.identifier == 0) {
		WebDataSource dataSource = new WebDataSource(dataSourceID);
		WebFrame frame = dataSource.webFrame();
		if (frame.id == webView.mainFrame().id) this.identifier = identifier.id;
	}
	return identifier.id;
		
}

long /*int*/ webView_resource_willSendRequest_redirectResponse_fromDataSource(long /*int*/ sender, long /*int*/ identifier, long /*int*/ request, long /*int*/ redirectResponse, long /*int*/ dataSource) {
	NSURLRequest nsRequest = new NSURLRequest (request);
	NSURL url = nsRequest.URL ();
	if (url.isFileURL ()) {
		NSMutableURLRequest newRequest = new NSMutableURLRequest (nsRequest.mutableCopy ());
		newRequest.autorelease ();
		newRequest.setCachePolicy (OS.NSURLRequestReloadIgnoringLocalCacheData);
		return newRequest.id;
	}
	return request;
}

/* UIDelegate */

long /*int*/ webView_createWebViewWithRequest(long /*int*/ sender, long /*int*/ request) {
	WindowEvent newEvent = new WindowEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.required = true;
	if (openWindowListeners != null) {
		for (int i = 0; i < openWindowListeners.length; i++) {
			openWindowListeners[i].open(newEvent);
		}
	}
	WebView result = null;
	Browser browser = null;
	if (newEvent.browser != null && newEvent.browser.webBrowser instanceof WebKit) {
		browser = newEvent.browser;
	}
	if (browser != null && !browser.isDisposed()) {
		result = ((WebKit)browser.webBrowser).webView;
		if (request != 0) {
			WebFrame mainFrame = result.mainFrame();
			mainFrame.loadRequest(new NSURLRequest(request));
		}
	}
	return result != null ? result.id : 0;
}

void webViewShow(long /*int*/ sender) {
	/*
	* Feature on WebKit.  WebKit expects the application to
	* create a new Window using the Objective C Cocoa API in response
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
	* Feature in WebKit.  WebKit's tool bar contains
	* the address bar.  The address bar is displayed
	* if the tool bar is displayed. There is no separate
	* notification for the address bar.
	* 
	* Feature of OSX.  The menu bar is always displayed.
	* There is no notification to hide the menu bar.
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

void webView_setFrame(long /*int*/ sender, long /*int*/ frame) {
	NSRect rect = new NSRect();
	OS.memmove(rect, frame, NSRect.sizeof);
	/* convert to SWT system coordinates */
	Rectangle bounds = browser.getDisplay().getBounds();
	location = new Point((int)rect.x, bounds.height - (int)rect.y - (int)rect.height);
	size = new Point((int)rect.width, (int)rect.height);
}

void webViewFocus(long /*int*/ sender) {
}

void webViewUnfocus(long /*int*/ sender) {
}

NSNumber callRunBeforeUnloadConfirmPanelWithMessage(long /*int*/ messageID, long /*int*/ arg) {
	boolean result = webView_runBeforeUnloadConfirmPanelWithMessage_initiatedByFrame (0, messageID, 0);
	return NSNumber.numberWithBool (result);
}

boolean webView_runBeforeUnloadConfirmPanelWithMessage_initiatedByFrame(long /*int*/ sender, long /*int*/ messageID, long /*int*/ frame) {
	NSString message = new NSString(messageID);
	StringBuffer text = new StringBuffer(Compatibility.getMessage("SWT_OnBeforeUnload_Message1")); //$NON-NLS-1$
	text.append("\n\n"); //$NON-NLS-1$
	text.append(message.getString());
	text.append("\n\n"); //$NON-NLS-1$
	text.append(Compatibility.getMessage("SWT_OnBeforeUnload_Message2")); //$NON-NLS-1$
	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION | SWT.SHEET);
	messageBox.setMessage(text.toString());
	return messageBox.open() == SWT.OK;
}

void webView_runJavaScriptAlertPanelWithMessage(long /*int*/ sender, long /*int*/ messageID) {
	NSString message = new NSString(messageID);
	String text = message.getString();

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.ICON_WARNING);
	messageBox.setText("Javascript");	//$NON-NLS-1$
	messageBox.setMessage(text);
	messageBox.open();
}

int webView_runJavaScriptConfirmPanelWithMessage(long /*int*/ sender, long /*int*/ messageID) {
	NSString message = new NSString(messageID);
	String text = message.getString();

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
	messageBox.setText("Javascript");	//$NON-NLS-1$
	messageBox.setMessage(text);
	return messageBox.open() == SWT.OK ? 1 : 0;
}

void webView_runOpenPanelForFileButtonWithResultListener(long /*int*/ sender, long /*int*/ resultListenerID) {
	FileDialog dialog = new FileDialog(browser.getShell(), SWT.NONE);
	String result = dialog.open();
	WebOpenPanelResultListener resultListener = new WebOpenPanelResultListener(resultListenerID);
	if (result == null) {
		resultListener.cancel();
		return;
	}
	resultListener.chooseFilename(NSString.stringWith(result));
}

void webViewClose(long /*int*/ sender) {
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
	* Feature on WebKit.  WebKit expects the application to
	* create a new Window using the Objective C Cocoa API in response
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

long /*int*/ webView_contextMenuItemsForElement_defaultMenuItems(long /*int*/ sender, long /*int*/ element, long /*int*/ defaultMenuItems) {
	Point pt = browser.getDisplay().getCursorLocation();
	Event event = new Event();
	event.x = pt.x;
	event.y = pt.y;
	browser.notifyListeners(SWT.MenuDetect, event);
	Menu menu = browser.getMenu();
	if (!event.doit) return 0;
	if (menu != null && !menu.isDisposed()) {
		if (event.x != pt.x || event.y != pt.y) {
			menu.setLocation(event.x, event.y);
		}
		menu.setVisible(true);
		return 0;
	}
	return defaultMenuItems;
}

void webView_setStatusBarVisible(long /*int*/ sender, boolean visible) {
	/* Note.  Webkit only emits the notification when the status bar should be hidden. */
	statusBar = visible;
}

void webView_setStatusText(long /*int*/ sender, long /*int*/ textID) {
	NSString text = new NSString(textID);
	int length = (int)/*64*/text.length();
	if (length == 0) return;

	StatusTextEvent statusText = new StatusTextEvent(browser);
	statusText.display = browser.getDisplay();
	statusText.widget = browser;
	statusText.text = text.getString();
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed(statusText);
	}
}

void webView_setResizable(long /*int*/ sender, boolean visible) {
}

void webView_setToolbarsVisible(long /*int*/ sender, boolean visible) {
	/* Note.  Webkit only emits the notification when the tool bar should be hidden. */
	toolBar = visible;
}

void webView_mouseDidMoveOverElement_modifierFlags (long /*int*/ sender, long /*int*/ elementInformationID, long /*int*/ modifierFlags) {
	if (elementInformationID == 0) return;

	NSString key = NSString.stringWith(WebElementLinkURLKey);
	NSDictionary elementInformation = new NSDictionary(elementInformationID);
	id value = elementInformation.valueForKey(key);
	if (value == null) {
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

	NSString url = new NSURL(value.id).absoluteString();
	int length = (int)/*64*/url.length();
	String urlString;
	if (length == 0) {
		urlString = "";	//$NON-NLS-1$
	} else {
		urlString = url.getString();
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

void webView_printFrameView (long /*int*/ sender, long /*int*/ frameViewID) {
	WebFrameView view = new WebFrameView(frameViewID);
	boolean viewPrint = view.documentViewShouldHandlePrint();
	if (viewPrint) {
		view.printDocumentView();
		return;
	}
	NSPrintInfo info = NSPrintInfo.sharedPrintInfo();
	NSPrintOperation operation = view.printOperationWithPrintInfo(info);
	if (operation != null) operation.runOperation();
}

/* PolicyDelegate */

void webView_decidePolicyForMIMEType_request_frame_decisionListener(long /*int*/ sender, long /*int*/ type, long /*int*/ request, long /*int*/ frame, long /*int*/ listenerID) {
	boolean canShow = WebView.canShowMIMEType(new NSString(type));
	WebPolicyDecisionListener listener = new WebPolicyDecisionListener(listenerID);
	if (canShow) {
		listener.use();
	} else {
		listener.download();
	}
}

void webView_decidePolicyForNavigationAction_request_frame_decisionListener(long /*int*/ sender, long /*int*/ actionInformation, long /*int*/ request, long /*int*/ frame, long /*int*/ listenerID) {
	NSURL url = new NSURLRequest(request).URL();
	WebPolicyDecisionListener listener = new WebPolicyDecisionListener(listenerID);

	if (loadingText) {
		/* 
		 * WebKit is auto-navigating to about:blank in response to a loadHTMLString()
		 * invocation.  This navigate should always proceed without sending an event
		 * since it is preceded by an explicit navigate to about:blank in setText().
		 */
		listener.use();
		return;
	}
	if (url == null) {
		/* indicates that a URL with an invalid format was specified */
		listener.ignore();
		return;
	}
	if (url.isFileURL() && getUrl().startsWith(ABOUT_BLANK) && untrustedText) {
		/* indicates an attempt to access the local file system from untrusted content */
		listener.ignore();
		return;
	}
	NSString s = url.absoluteString();
	String url2 = s.getString();
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FILEROOT)) {
		url2 = ABOUT_BLANK;
	} else {
		int length = URI_FILEROOT.length ();
		if (url2.startsWith (URI_FILEROOT) && url2.charAt (length) == '#') {
			url2 = ABOUT_BLANK + url2.substring (length);
		}
	}

	LocationEvent newEvent = new LocationEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.location = url2;
	newEvent.doit = true;
	if (locationListeners != null) {
		for (int i = 0; i < locationListeners.length; i++) {
			locationListeners[i].changing(newEvent);
		}
	}
	if (newEvent.doit) {
		if (jsEnabled != jsEnabledOnNextPage) {
			jsEnabled = jsEnabledOnNextPage;
			if (preferences == null) {
				preferences = (WebPreferences)new WebPreferences ().alloc ().init ();
				webView.setPreferences (preferences);
			}
			preferences.setJavaScriptEnabled (jsEnabled);
		}
		listener.use();
		lastNavigateURL = url2;
	} else {
		listener.ignore();
	}
}

void webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener(long /*int*/ sender, long /*int*/ actionInformation, long /*int*/ request, long /*int*/ frameName, long /*int*/ listenerID) {
	WebPolicyDecisionListener listener = new WebPolicyDecisionListener(listenerID);
	listener.use();
}

void webView_unableToImplementPolicyWithError_frame(long /*int*/ sender, long /*int*/ error, long /*int*/ frame) {
}

/* WebDownload */

void download_decideDestinationWithSuggestedFilename(long /*int*/ downloadId, long /*int*/ filename) {
	NSString string = new NSString(filename);
	String name = string.getString();
	FileDialog dialog = new FileDialog(browser.getShell(), SWT.SAVE);
	dialog.setText(SWT.getMessage ("SWT_FileDownload")); //$NON-NLS-1$
	dialog.setFileName(name);
	String path = dialog.open();
	NSURLDownload download = new NSURLDownload(downloadId);
	if (path == null) {
		/* cancel pressed */
		download.cancel();
		return;
	}
	download.setDestination(NSString.stringWith(path), true);
}

/* DOMEventListener */

void handleEvent(long /*int*/ evtId) {
	NSString string = new NSString(OS.objc_msgSend(evtId, OS.sel_type));
	String type = string.getString();

	if (DOMEVENT_KEYDOWN.equals(type) || DOMEVENT_KEYUP.equals(type)) {
		DOMKeyboardEvent event = new DOMKeyboardEvent(evtId);

		boolean ctrl = event.ctrlKey();
		boolean shift = event.shiftKey();
		boolean alt = event.altKey();
		boolean meta = event.metaKey();
		int keyCode = event.keyCode();
		int charCode = event.charCode();

		Event keyEvent = new Event();
		keyEvent.widget = browser;
		int eventType = DOMEVENT_KEYDOWN.equals(type) ? SWT.KeyDown : SWT.KeyUp;
		keyEvent.type = eventType;
		int translatedKey = translateKey (keyCode);
		keyEvent.keyCode = translatedKey;
		keyEvent.character = (char)charCode;
		int stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		keyEvent.stateMask = stateMask;

		boolean doit = true;
		if (keyEvent.type == SWT.KeyDown) {
			doit = sendKeyEvent(keyEvent);
		} else {
			browser.notifyListeners(keyEvent.type, keyEvent);
			doit = keyEvent.doit;
		}
		if (!doit) {
			event.preventDefault();
		} else {
			if (eventType == SWT.KeyDown && stateMask == SWT.COMMAND) {
				if (translatedKey == 'v') {
					webView.paste (webView);
					event.preventDefault();
				} else if (translatedKey == 'c') {
					webView.copy (webView);
					event.preventDefault();
				} else if (translatedKey == 'x') {
					webView.cut (webView);
					event.preventDefault();
				}
			}
		}
		return;
	}

	if (DOMEVENT_MOUSEWHEEL.equals(type)) {
		DOMWheelEvent event = new DOMWheelEvent(evtId);

		/*
		 * The position of mouse events is received in screen-relative coordinates
		 * in order to handle pages with frames, since frames express their event
		 * coordinates relative to themselves rather than relative to their top-
		 * level page.  Convert screen-relative coordinates to be browser-relative.
		 */
		int screenX = event.screenX();
		int screenY = event.screenY();
		Point position = new Point(screenX, screenY);
		position = browser.getDisplay().map(null, browser, position);

		int delta = event.wheelDelta();
		boolean ctrl = event.ctrlKey();
		boolean shift = event.shiftKey();
		boolean alt = event.altKey();
		boolean meta = event.metaKey();
		Event mouseEvent = new Event();
		mouseEvent.type = SWT.MouseWheel;
		mouseEvent.widget = browser;
		mouseEvent.x = position.x; mouseEvent.y = position.y;
		mouseEvent.count = delta / 120;
		mouseEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		browser.notifyListeners (mouseEvent.type, mouseEvent);
		return;
	}

	/* mouse event */

	DOMMouseEvent event = new DOMMouseEvent(evtId);

	/*
	 * The position of mouse events is received in screen-relative coordinates
	 * in order to handle pages with frames, since frames express their event
	 * coordinates relative to themselves rather than relative to their top-
	 * level page.  Convert screen-relative coordinates to be browser-relative.
	 */
	int screenX = event.screenX();
	int screenY = event.screenY();
	Point position = new Point(screenX, screenY);
	position = browser.getDisplay().map(null, browser, position);

	int detail = event.detail();
	int button = event.button();
	boolean ctrl = event.ctrlKey();
	boolean shift = event.shiftKey();
	boolean alt = event.altKey();
	boolean meta = event.metaKey();

	Event mouseEvent = new Event ();
	mouseEvent.widget = browser;
	mouseEvent.x = position.x; mouseEvent.y = position.y;
	mouseEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
	if (DOMEVENT_MOUSEDOWN.equals (type)) {
		mouseEvent.type = SWT.MouseDown;
		mouseEvent.button = button + 1;
		mouseEvent.count = detail;
	} else if (DOMEVENT_MOUSEUP.equals (type)) {
		mouseEvent.type = SWT.MouseUp;
		mouseEvent.button = button + 1;
		mouseEvent.count = detail;
		switch (mouseEvent.button) {
			case 1: mouseEvent.stateMask |= SWT.BUTTON1; break;
			case 2: mouseEvent.stateMask |= SWT.BUTTON2; break;
			case 3: mouseEvent.stateMask |= SWT.BUTTON3; break;
			case 4: mouseEvent.stateMask |= SWT.BUTTON4; break;
			case 5: mouseEvent.stateMask |= SWT.BUTTON5; break;
		}
	} else if (DOMEVENT_MOUSEMOVE.equals (type)) {
		/*
		* Bug in WebKit.  Spurious and redundant mousemove events are received in
		* various contexts, including following every MouseUp.  The workaround is
		* to not fire MouseMove events whose x and y values match the last MouseMove  
		*/
		if (mouseEvent.x == lastMouseMoveX && mouseEvent.y == lastMouseMoveY) return;
		mouseEvent.type = SWT.MouseMove;
		lastMouseMoveX = mouseEvent.x; lastMouseMoveY = mouseEvent.y;
	}

	browser.notifyListeners (mouseEvent.type, mouseEvent);
	if (detail == 2 && DOMEVENT_MOUSEDOWN.equals (type)) {
		mouseEvent = new Event ();
		mouseEvent.widget = browser;
		mouseEvent.x = position.x; mouseEvent.y = position.y;
		mouseEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		mouseEvent.type = SWT.MouseDoubleClick;
		mouseEvent.button = button + 1;
		mouseEvent.count = detail;
		browser.notifyListeners (mouseEvent.type, mouseEvent);
	}
}

/* external */

Object convertToJava (long /*int*/ value) {
	NSObject object = new NSObject (value);
	long /*int*/ clazz = OS.objc_lookUpClass ("NSString"); //$NON-NLS-1$
	if (object.isKindOfClass (clazz)) {
		NSString string = new NSString (value);
		return string.getString ();
	}
	clazz = OS.objc_lookUpClass ("NSNumber"); //$NON-NLS-1$
	if (object.isKindOfClass (clazz)) {
		NSNumber number = new NSNumber (value);
		long /*int*/ ptr = number.objCType ();
		byte[] type = new byte[1];
		OS.memmove (type, ptr, 1);
		if (type[0] == 'c' || type[0] == 'B') {
			return new Boolean (number.boolValue ());
		}
		if ("islqISLQfd".indexOf (type[0]) != -1) { //$NON-NLS-1$
			return new Double (number.doubleValue ());
		}
	}
	clazz = OS.objc_lookUpClass ("WebScriptObject"); //$NON-NLS-1$
	if (object.isKindOfClass (clazz)) {
		WebScriptObject script = new WebScriptObject (value);
		id id = script.valueForKey (NSString.stringWith ("length")); //$NON-NLS-1$
		if (id == null) { /* not a JS array */
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		int length = new NSNumber (id).intValue ();
		Object[] arguments = new Object[length];
		for (int i = 0; i < length; i++) {
			id current = script.webScriptValueAtIndex (i);
			if (current != null) {
				arguments[i] = convertToJava (current.id);
			}
		}
		return arguments;
	}
	clazz = OS.objc_lookUpClass ("WebUndefined"); //$NON-NLS-1$
	if (object.isKindOfClass (clazz)) {
		return null;
	}

	SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	return null;
}

NSObject convertToJS (Object value) {
	if (value == null) {
		long /*int*/ result = OS.objc_msgSend (OS.class_NSNull, OS.sel_null);
		return result != 0 ? new NSObject (result) : null;
	}
	if (value instanceof String) {
		return NSString.stringWith ((String)value);
	}
	if (value instanceof Boolean) {
		return NSNumber.numberWithBool (((Boolean)value).booleanValue ());
	}
	if (value instanceof Number) {
		return NSNumber.numberWithDouble (((Number)value).doubleValue ());
	}
	if (value instanceof Object[]) {
		Object[] arrayValue = (Object[]) value;
		int length = arrayValue.length;
		NSMutableArray array = NSMutableArray.arrayWithCapacity (length);
		for (int i = 0; i < length; i++) {
			Object currentObject = arrayValue[i];
			array.addObject (convertToJS (currentObject));
		}
		return array;
	}
	SWT.error (SWT.ERROR_INVALID_RETURN_VALUE);
	return null;
}

NSObject callJava (long /*int*/ index, long /*int*/ token, long /*int*/ args, long /*int*/ arg1) {
	Object returnValue = null;
	NSObject object = new NSObject (index);
	long /*int*/ clazz = OS.objc_lookUpClass ("NSNumber"); //$NON-NLS-1$
	if (object.isKindOfClass (clazz)) {
		NSNumber number = new NSNumber (index);
		Object key = new Integer (number.intValue ());
		object = new NSObject (token);
		clazz = OS.objc_lookUpClass ("NSString"); //$NON-NLS-1$
		if (object.isKindOfClass (clazz)) {
			NSString tokenString = new NSString (token);
			BrowserFunction function = (BrowserFunction)functions.get (key);
			if (function != null && tokenString.getString ().equals (function.token)) {
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
	}
	try {
		return convertToJS (returnValue);
	} catch (SWTException e) {
		/* invalid return value type */
		return convertToJS (WebBrowser.CreateErrorString (e.getLocalizedMessage ()));
	}
}

}

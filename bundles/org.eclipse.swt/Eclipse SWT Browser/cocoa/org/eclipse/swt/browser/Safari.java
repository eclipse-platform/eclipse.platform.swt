/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.widgets.*;

class Safari extends WebBrowser {
	WebView webView;
	SWTWebViewDelegate delegate;
	int jniRef;
	boolean changingLocation;
	String lastHoveredLinkURL;
	String html;
	int identifier;
	int resourceCount;
	String url = "";
	Point location;
	Point size;
	boolean statusBar = true, toolBar = true, ignoreDispose;
	int lastMouseMoveX, lastMouseMoveY;
	//TEMPORARY CODE
//	boolean doit;

	static boolean Initialized;
	static Callback Callback2, Callback3, Callback4, Callback5, Callback6, Callback7;

	static final int MIN_SIZE = 16;
	static final int MAX_PROGRESS = 100;
	static final String WebElementLinkURLKey = "WebElementLinkURL"; //$NON-NLS-1$
	static final String AGENT_STRING = "Safari/unknown"; //$NON-NLS-1$
	static final String URI_FROMMEMORY = "file:///"; //$NON-NLS-1$
	static final String PROTOCOL_FILE = "file:"; //$NON-NLS-1$
	static final String PROTOCOL_HTTP = "http:"; //$NON-NLS-1$
	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String SAFARI_EVENTS_FIX_KEY = "org.eclipse.swt.internal.safariEventsFix"; //$NON-NLS-1$

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
				int count = cookies.count();
				for (int i = 0; i < count; i++) {
					NSHTTPCookie cookie = new NSHTTPCookie(cookies.objectAtIndex(i));
					if (cookie.isSessionOnly()) {
						storage.deleteCookie(cookie);
					}
				}
			}
		};
	}

public void create (Composite parent, int style) {
	
	String className = "SWTWebViewDelegate";
	if (OS.objc_lookUpClass(className) == 0) {
		Class safaryClass = this.getClass();
		Callback2 = new Callback(safaryClass, "browserProc", 2);
		int proc2 = Callback2.getAddress();
		if (proc2 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback3 = new Callback(safaryClass, "browserProc", 3);
		int proc3 = Callback3.getAddress();
		if (proc3 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback4 = new Callback(safaryClass, "browserProc", 4);
		int proc4 = Callback4.getAddress();
		if (proc4 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback5 = new Callback(safaryClass, "browserProc", 5);
		int proc5 = Callback5.getAddress();
		if (proc5 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback6 = new Callback(safaryClass, "browserProc", 6);
		int proc6 = Callback6.getAddress();
		if (proc6 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		Callback7 = new Callback(safaryClass, "browserProc", 7);
		int proc7 = Callback7.getAddress();
		if (proc7 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		
		int cls = OS.objc_allocateClassPair(OS.class_WebView, className, 0);
		OS.class_addIvar(cls, "tag", OS.PTR_SIZEOF, (byte)(Math.log(OS.PTR_SIZEOF) / Math.log(2)), "i");
		OS.class_addMethod(cls, OS.sel_tag, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_setTag_1, proc3, "@:i");
		OS.class_addMethod(cls, OS.sel_webView_1didChangeLocationWithinPageForFrame_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1didFailProvisionalLoadWithError_1forFrame_1, proc5, "@:@@@");
		OS.class_addMethod(cls, OS.sel_webView_1didFinishLoadForFrame_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1didReceiveTitle_1forFrame_1, proc5, "@:@@@");
		OS.class_addMethod(cls, OS.sel_webView_1didStartProvisionalLoadForFrame_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1didCommitLoadForFrame_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1resource_1didFinishLoadingFromDataSource_1, proc5, "@:@@@");
		OS.class_addMethod(cls, OS.sel_webView_1resource_1didFailLoadingWithError_1fromDataSource_1, proc6, "@:@@@@");
		OS.class_addMethod(cls, OS.sel_webView_1identifierForInitialRequest_1fromDataSource_1, proc5, "@:@@@");
		OS.class_addMethod(cls, OS.sel_webView_1resource_1willSendRequest_1redirectResponse_1fromDataSource_1, proc7, "@:@@@@@");
		OS.class_addMethod(cls, OS.sel_handleNotification_1, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_webView_1createWebViewWithRequest_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webViewShow_1, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_webView_1setFrame_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webViewClose_1, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_webView_1contextMenuItemsForElement_1defaultMenuItems_1, proc5, "@:@@@");
		OS.class_addMethod(cls, OS.sel_webView_1setStatusBarVisible_1, proc4, "@:@B");
		OS.class_addMethod(cls, OS.sel_webView_1setResizable_1, proc4, "@:@B");
		OS.class_addMethod(cls, OS.sel_webView_1setToolbarsVisible_1, proc4, "@:@B");
		OS.class_addMethod(cls, OS.sel_webView_1setStatusText_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webViewFocus_1, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_webViewUnfocus_1, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_webView_1runJavaScriptAlertPanelWithMessage_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1runJavaScriptConfirmPanelWithMessage_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1runOpenPanelForFileButtonWithResultListener_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1mouseDidMoveOverElement_1modifierFlags_1, proc5, "@:@@I");
		OS.class_addMethod(cls, OS.sel_webView_1printFrameView_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_webView_1decidePolicyForMIMEType_1request_1frame_1decisionListener_1, proc7, "@:@@@@@");
		OS.class_addMethod(cls, OS.sel_webView_1decidePolicyForNavigationAction_1request_1frame_1decisionListener_1, proc7, "@:@@@@@");
		OS.class_addMethod(cls, OS.sel_webView_1decidePolicyForNewWindowAction_1request_1newFrameName_1decisionListener_1, proc7, "@:@@@@@");
		OS.class_addMethod(cls, OS.sel_webView_1unableToImplementPolicyWithError_1frame_1, proc5, "@:@@@");
		OS.class_addMethod(cls, OS.sel_download_1decideDestinationWithSuggestedFilename_1, proc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_handleEvent_1, proc3, "@:@");
		OS.objc_registerClassPair(cls);
	}

	/*
	* Override the default event mechanism to not send key events so
	* that the browser can send them by listening to the DOM instead.
	*/
	browser.setData(SAFARI_EVENTS_FIX_KEY);

	WebView webView = (WebView)new WebView().alloc();
	if (webView == null) SWT.error(SWT.ERROR_NO_HANDLES);
	webView.initWithFrame(browser.view.frame(), null, null);
	webView.setAutoresizingMask(OS.NSViewWidthSizable | OS.NSViewHeightSizable);
	jniRef = OS.NewGlobalRef(this);
	if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	final SWTWebViewDelegate delegate = (SWTWebViewDelegate)new SWTWebViewDelegate().alloc().init();
	delegate.setTag(jniRef);
	this.delegate = delegate;
	this.webView = webView;
	browser.view.addSubview_(webView);
	
	final NSNotificationCenter notificationCenter = NSNotificationCenter.defaultCenter();

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

					Safari.this.webView.setFrameLoadDelegate(null);
					Safari.this.webView.setResourceLoadDelegate(null);
					Safari.this.webView.setUIDelegate(null);
					Safari.this.webView.setPolicyDelegate(null);
					Safari.this.webView.setDownloadDelegate(null);
					notificationCenter.removeObserver(delegate);
					
					Safari.this.webView.release();
					Safari.this.webView = null;
					Safari.this.delegate.release();
					Safari.this.delegate = null;
					OS.DeleteGlobalRef(jniRef);
					jniRef = 0;
					html = null;
					lastHoveredLinkURL = null;
					break;
				}
			}
		}
	};
	browser.addListener(SWT.Dispose, listener);
				
	webView.setFrameLoadDelegate(delegate);
	webView.setResourceLoadDelegate(delegate);
	webView.setUIDelegate(delegate);	
	notificationCenter.addObserver(delegate, OS.sel_handleNotification_1, null, webView);
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

static int browserProc(int delegate, int sel) {
	if (sel == OS.sel_tag) {
		int[] tag = new int[1];
		OS.object_getInstanceVariable(delegate, "tag", tag);	
		return tag[0];
	}
	return 0;
}

static int browserProc(int id, int sel, int arg0) {
	if (sel == OS.sel_setTag_1) {
		OS.object_setInstanceVariable(id, "tag", arg0);
		return 0;
	}
	int jniRef = OS.objc_msgSend(id, OS.sel_tag);
	if (jniRef == 0 || jniRef == -1) return 0;
	Safari widget = (Safari)OS.JNIGetObject(jniRef);
	if (widget == null) return 0;
	if (sel == OS.sel_handleNotification_1) {
		widget.handleNotification(arg0);
	} else if (sel == OS.sel_webViewShow_1) {
		widget.webViewShow(arg0);
	} else if (sel == OS.sel_webViewClose_1) {
		widget.webViewClose(arg0);
	} else if (sel == OS.sel_webViewFocus_1) {
		widget.webViewFocus(arg0);
	} else if (sel == OS.sel_webViewUnfocus_1) {
		widget.webViewUnfocus(arg0);
	} else if (sel == OS.sel_handleEvent_1) {
		widget.handleEvent(arg0);
	}
	return 0;
}

static int browserProc(int id, int sel, int arg0, int arg1) {
	if (sel == OS.sel_setTag_1) {
		OS.object_setInstanceVariable(id, "tag", arg0);
		return 0;
	}
	int jniRef = OS.objc_msgSend(id, OS.sel_tag);
	if (jniRef == 0 || jniRef == -1) return 0;
	Safari widget = (Safari)OS.JNIGetObject(jniRef);
	if (widget == null) return 0;
	if (sel == OS.sel_webView_1didChangeLocationWithinPageForFrame_1) {
		widget.webView_didChangeLocationWithinPageForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_1didFinishLoadForFrame_1) {
		widget.webView_didFinishLoadForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_1didStartProvisionalLoadForFrame_1) {
		widget.webView_didStartProvisionalLoadForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_1didCommitLoadForFrame_1) {
		widget.webView_didCommitLoadForFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_1setFrame_1) {
		widget.webView_setFrame(arg0, arg1);
	} else if (sel == OS.sel_webView_1createWebViewWithRequest_1) {
		return widget.webView_createWebViewWithRequest(arg0, arg1);		
	} else if (sel == OS.sel_webView_1setStatusBarVisible_1) {
		widget.webView_setStatusBarVisible(arg0, arg1);
	} else if (sel == OS.sel_webView_1setResizable_1) {
		widget.webView_setResizable(arg0, arg1);
	} else if (sel == OS.sel_webView_1setStatusText_1) {
		widget.webView_setStatusText(arg0, arg1);
	} else if (sel == OS.sel_webView_1setToolbarsVisible_1) {
		widget.webView_setToolbarsVisible(arg0, arg1);
	} else if (sel == OS.sel_webView_1runJavaScriptAlertPanelWithMessage_1) {
		widget.webView_runJavaScriptAlertPanelWithMessage(arg0, arg1);
	} else if (sel == OS.sel_webView_1runJavaScriptConfirmPanelWithMessage_1) {
		return widget.webView_runJavaScriptConfirmPanelWithMessage(arg0, arg1);
	} else if (sel == OS.sel_webView_1runOpenPanelForFileButtonWithResultListener_1) {
		widget.webView_runOpenPanelForFileButtonWithResultListener(arg0, arg1);
	} else if (sel == OS.sel_download_1decideDestinationWithSuggestedFilename_1) {
		widget.download_decideDestinationWithSuggestedFilename(arg0, arg1);
	} else if (sel == OS.sel_webView_1printFrameView_1) {
		widget.webView_printFrameView(arg0, arg1);
	}
	return 0;
}

static int browserProc(int id, int sel, int arg0, int arg1, int arg2) {
	int jniRef = OS.objc_msgSend(id, OS.sel_tag);
	if (jniRef == 0 || jniRef == -1) return 0;
	Safari widget = (Safari)OS.JNIGetObject(jniRef);
	if (widget == null) return 0;
	if (sel == OS.sel_webView_1didFailProvisionalLoadWithError_1forFrame_1) {
		widget.webView_didFailProvisionalLoadWithError_forFrame(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_1didReceiveTitle_1forFrame_1) {
		widget.webView_didReceiveTitle_forFrame(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_1resource_1didFinishLoadingFromDataSource_1) {
		widget.webView_resource_didFinishLoadingFromDataSource(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_1identifierForInitialRequest_1fromDataSource_1) {
		return widget.webView_identifierForInitialRequest_fromDataSource(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_1contextMenuItemsForElement_1defaultMenuItems_1) {
		return widget.webView_contextMenuItemsForElement_defaultMenuItems(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_1mouseDidMoveOverElement_1modifierFlags_1) {
		widget.webView_mouseDidMoveOverElement_modifierFlags(arg0, arg1, arg2);
	} else if (sel == OS.sel_webView_1unableToImplementPolicyWithError_1frame_1) {
		widget.webView_unableToImplementPolicyWithError_frame(arg0, arg1, arg2);
	}
	return 0;
}

static int browserProc(int id, int sel, int arg0, int arg1, int arg2, int arg3) {
	int jniRef = OS.objc_msgSend(id, OS.sel_tag);
	if (jniRef == 0 || jniRef == -1) return 0;
	Safari widget = (Safari)OS.JNIGetObject(jniRef);
	if (widget == null) return 0;
	if (sel == OS.sel_webView_1resource_1didFailLoadingWithError_1fromDataSource_1) {
		widget.webView_resource_didFailLoadingWithError_fromDataSource(arg0, arg1, arg2, arg3);
	}	
	return 0;
}

static int browserProc(int id, int sel, int arg0, int arg1, int arg2, int arg3, int arg4) {
	int jniRef = OS.objc_msgSend(id, OS.sel_tag);
	if (jniRef == 0 || jniRef == -1) return 0;
	Safari widget = (Safari)OS.JNIGetObject(jniRef);
	if (widget == null) return 0;
	if (sel == OS.sel_webView_1resource_1willSendRequest_1redirectResponse_1fromDataSource_1) {
		return widget.webView_resource_willSendRequest_redirectResponse_fromDataSource(arg0, arg1, arg2, arg3, arg4);
	} else if (sel == OS.sel_webView_1decidePolicyForMIMEType_1request_1frame_1decisionListener_1) {
		widget.webView_decidePolicyForMIMEType_request_frame_decisionListener(arg0, arg1, arg2, arg3, arg4);
	} else if (sel == OS.sel_webView_1decidePolicyForNavigationAction_1request_1frame_1decisionListener_1) {
		widget.webView_decidePolicyForNavigationAction_request_frame_decisionListener(arg0, arg1, arg2, arg3, arg4);
	} else if (sel == OS.sel_webView_1decidePolicyForNewWindowAction_1request_1newFrameName_1decisionListener_1) {
		widget.webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener(arg0, arg1, arg2, arg3, arg4);
	}
	return 0;
}

public boolean execute(String script) {
	return webView.stringByEvaluatingJavaScriptFromString(NSString.stringWith(script)) != null;
}

public boolean forward() {
	html = null;
	return webView.goForward();
}

public String getText() {
	WebFrame mainFrame = webView.mainFrame();
	WebDataSource dataSource = mainFrame.dataSource();
	if (dataSource == null) return "";	//$NON-NLS-1$
	WebDocumentRepresentation representation = dataSource.representation();
	if (representation == null) return "";	//$NON-NLS-1$
	NSString source = representation.documentSource();
	if (source == null) return "";	//$NON-NLS-1$
	char[] buffer = new char[source.length()];
	source.getCharacters_(buffer);
	return new String(buffer);
}

public String getUrl() {
	return url;
}

public boolean isBackEnabled() {
	return webView.canGoBack();
}

public boolean isForwardEnabled() {
	return webView.canGoForward();
}

public void refresh() {
	webView.reload(null);
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
	NSString string = NSString.stringWith(html);
	NSString URLString = NSString.stringWith(URI_FROMMEMORY);
	NSURL URL = NSURL.static_URLWithString_(URLString);
	WebFrame mainFrame = webView.mainFrame();
	mainFrame.loadHTMLString(string, URL);
}

public boolean setUrl(String url) {
	html = null;

	NSURL inURL;
	if (url.startsWith(PROTOCOL_FILE)) {
		url = url.substring(PROTOCOL_FILE.length());
	}
	boolean isHttpURL = url.indexOf('/') != 0;
	if (isHttpURL) {
		if (url.indexOf(':') == -1) {
			url = PROTOCOL_HTTP + "//" + url; //$NON-NLS-1$
		}
		inURL = NSURL.static_URLWithString_(NSString.stringWith(url.toString()));
	} else {
		inURL = NSURL.static_fileURLWithPath_(NSString.stringWith(url.toString()));
	}
	if (inURL == null) return false;

	NSURLRequest request = NSURLRequest.static_requestWithURL_(inURL);
	WebFrame mainFrame= webView.mainFrame();
	mainFrame.loadRequest(request);
	return true;
}

public void stop() {
	html = null;
	webView.stopLoading(null);
}

/* WebFrameLoadDelegate */

void webView_didChangeLocationWithinPageForFrame(int sender, int frameID) {
	WebFrame frame = new WebFrame(frameID);
	WebDataSource dataSource = frame.dataSource();
	NSURLRequest request = dataSource.request();
	NSURL url = request.URL();
	NSString s = url.absoluteString();
	int length = s.length();
	if (length == 0) return;
	char[] buffer = new char[length];
	s.getCharacters_(buffer);
	String url2 = new String(buffer);
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FROMMEMORY)) url2 = ABOUT_BLANK;

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

void webView_didFailProvisionalLoadWithError_forFrame(int sender, int error, int frame) {
	if (frame == webView.mainFrame().id) {
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

void webView_didFinishLoadForFrame(int sender, int frameID) {
	hookDOMMouseListeners(frameID);
	if (frameID == webView.mainFrame().id) {
		hookDOMKeyListeners(frameID);

		final Display display = browser.getDisplay();
		/*
		* To be consistent with other platforms a title event should be fired when a
		* page has completed loading.  A page with a <title> tag will do this
		* automatically when the didReceiveTitle callback is received.  However a page
		* without a <title> tag will not do this by default, so fire the event
		* here with the page's url as the title.
		*/
		WebFrame frame = new WebFrame(frameID);
		WebDataSource dataSource = frame.dataSource();
		if (dataSource != null) {
			NSString title = dataSource.pageTitle();
			if (title == null) {	/* page has no title */
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

void hookDOMKeyListeners(int frameID) {
	WebFrame frame = new WebFrame(frameID);
	DOMDocument document = frame.DOMDocument();

	NSString type = NSString.stringWith(DOMEVENT_KEYDOWN);
	document.addEventListener_listener_useCapture(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_KEYUP);
	document.addEventListener_listener_useCapture(type, delegate, false);
}

void hookDOMMouseListeners(int frameID) {
	WebFrame frame = new WebFrame(frameID);
	DOMDocument document = frame.DOMDocument();

	NSString type = NSString.stringWith(DOMEVENT_MOUSEDOWN);
	document.addEventListener_listener_useCapture(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_MOUSEUP);
	document.addEventListener_listener_useCapture(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_MOUSEMOVE);
	document.addEventListener_listener_useCapture(type, delegate, false);

	type = NSString.stringWith(DOMEVENT_MOUSEWHEEL);
	document.addEventListener_listener_useCapture(type, delegate, false);
}

void webView_didReceiveTitle_forFrame(int sender, int titleID, int frameID) {
	if (frameID == webView.mainFrame().id) {
		NSString title = new NSString(titleID);
		char[] buffer = new char[title.length()];
		title.getCharacters_(buffer);
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

void webView_didStartProvisionalLoadForFrame(int sender, int frameID) {
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

void webView_didCommitLoadForFrame(int sender, int frameID) {
	WebFrame frame = new WebFrame(frameID);
	WebDataSource dataSource = frame.dataSource();
	NSURLRequest request = dataSource.request();
	NSURL url = request.URL();
	NSString s = url.absoluteString();
	int length = s.length();
	if (length == 0) return;
	char[] buffer = new char[length];
	s.getCharacters_(buffer);
	String url2 = new String(buffer);
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FROMMEMORY)) url2 = ABOUT_BLANK;

	final Display display = browser.getDisplay();
	boolean top = frameID == webView.mainFrame().id;
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

/* WebResourceLoadDelegate */

void webView_resource_didFinishLoadingFromDataSource(int sender, int identifier, int dataSource) {
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
	// this code is intentionally commented
	//if (this.identifier == identifier) this.identifier = 0;
}

void webView_resource_didFailLoadingWithError_fromDataSource(int sender, int identifier, int error, int dataSource) {
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
	// this code is intentionally commented
	//if (this.identifier == identifier) this.identifier = 0;
}

int webView_identifierForInitialRequest_fromDataSource(int sender, int request, int dataSourceID) {
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

	NSNumber identifier = NSNumber.numberWithInt(resourceCount++);
	if (this.identifier == 0) {
		WebDataSource dataSource = new WebDataSource(dataSourceID);
		WebFrame frame = dataSource.webFrame();
		if (frame.id == webView.mainFrame().id) this.identifier = identifier.id;
	}
	return identifier.id;
		
}

int webView_resource_willSendRequest_redirectResponse_fromDataSource(int sender, int identifier, int request, int redirectResponse, int dataSource) {
	return request;
}

/* handleNotification */

void handleNotification(int notification) {	
}

/* UIDelegate */
int webView_createWebViewWithRequest(int sender, int request) {
	WindowEvent newEvent = new WindowEvent(browser);
	newEvent.display = browser.getDisplay();
	newEvent.widget = browser;
	newEvent.required = true;
	if (openWindowListeners != null) {
		for (int i = 0; i < openWindowListeners.length; i++) {
			openWindowListeners[i].open(newEvent);
		}
	}
	Browser browser = null;
	if (newEvent.browser != null && newEvent.browser.webBrowser instanceof Safari) {
		browser = newEvent.browser;
	}
	if (browser != null && !browser.isDisposed()) {		
		if (request != 0) {
			WebFrame mainFrame = webView.mainFrame();
			mainFrame.loadRequest(new NSURLRequest(request));
		}
	}
	return webView.id;
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

void webView_setFrame(int sender, int frame) {
	float[] dest = new float[4];
	OS.memmove(dest, frame, 16);
	/* convert to SWT system coordinates */
	Rectangle bounds = browser.getDisplay().getBounds();
	location = new Point((int)dest[0], bounds.height - (int)dest[1] - (int)dest[3]);
	size = new Point((int)dest[2], (int)dest[3]);
}

void webViewFocus(int sender) {
}

void webViewUnfocus(int sender) {
}

void webView_runJavaScriptAlertPanelWithMessage(int sender, int messageID) {
	NSString message = new NSString(messageID);
	char[] buffer = new char[message.length()];
	message.getCharacters_(buffer);
	String text = new String(buffer);

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.ICON_WARNING);
	messageBox.setText("Javascript");	//$NON-NLS-1$
	messageBox.setMessage(text);
	messageBox.open();
}

int webView_runJavaScriptConfirmPanelWithMessage(int sender, int messageID) {
	NSString message = new NSString(messageID);
	char[] buffer = new char[message.length()];
	message.getCharacters_(buffer);
	String text = new String(buffer);

	MessageBox messageBox = new MessageBox(browser.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
	messageBox.setText("Javascript");	//$NON-NLS-1$
	messageBox.setMessage(text);
	return messageBox.open() == SWT.OK ? 1 : 0;
}

void webView_runOpenPanelForFileButtonWithResultListener(int sender, int resultListenerID) {
	FileDialog dialog = new FileDialog(browser.getShell(), SWT.NONE);
	String result = dialog.open();
	WebOpenPanelResultListener resultListener = new WebOpenPanelResultListener(resultListenerID);
	if (result == null) {
		resultListener.cancel();
		return;
	}
	resultListener.chooseFilename(NSString.stringWith(result));
}

void webViewClose(int sender) {
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

int webView_contextMenuItemsForElement_defaultMenuItems(int sender, int element, int defaultMenuItems) {
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

void webView_setStatusBarVisible(int sender, int visible) {
	/* Note.  Webkit only emits the notification when the status bar should be hidden. */
	statusBar = visible != 0;
}

void webView_setStatusText(int sender, int textID) {
	NSString text = new NSString(textID);
	int length = text.length();
	if (length == 0) return;
	char[] buffer = new char[length];
	text.getCharacters_(buffer);

	StatusTextEvent statusText = new StatusTextEvent(browser);
	statusText.display = browser.getDisplay();
	statusText.widget = browser;
	statusText.text = new String(buffer);
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed(statusText);
	}
}

void webView_setResizable(int sender, int visible) {
}

void webView_setToolbarsVisible(int sender, int visible) {
	/* Note.  Webkit only emits the notification when the tool bar should be hidden. */
	toolBar = visible != 0;
}

void webView_mouseDidMoveOverElement_modifierFlags (int sender, int elementInformationID, int modifierFlags) {
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
	int length = url.length();
	String urlString;
	if (length == 0) {
		urlString = "";	//$NON-NLS-1$
	} else {
		char[] buffer = new char[length];
		url.getCharacters_(buffer);
		urlString = new String(buffer);
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

void webView_printFrameView (int sender, int frameViewID) {
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

void webView_decidePolicyForMIMEType_request_frame_decisionListener(int sender, int type, int request, int frame, int listenerID) {
	boolean canShow = WebView.canShowMIMEType(new NSString(type));
	WebPolicyDecisionListener listener = new WebPolicyDecisionListener(listenerID);
	if (canShow) {
		listener.use();
	} else {
		listener.download();
	}
}

void webView_decidePolicyForNavigationAction_request_frame_decisionListener(int sender, int actionInformation, int request, int frame, int listenerID) {
	NSURL url = new NSURLRequest(request).URL();
	WebPolicyDecisionListener listener = new WebPolicyDecisionListener(listenerID);
	if (url == null) {
		/* indicates that a URL with an invalid format was specified */
		listener.ignore();
		return;
	}
	NSString s = url.absoluteString();
	char[] buffer = new char[s.length()];
	s.getCharacters_(buffer);
	String url2 = new String(buffer);
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url2.equals (URI_FROMMEMORY)) url2 = ABOUT_BLANK;

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
		listener.use();
	} else {
		listener.ignore();
	}
	if (html != null && !browser.isDisposed()) {
		String html = this.html;
		this.html = null;
		_setText(html);
	}
}

void webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener(int sender, int actionInformation, int request, int frameName, int listenerID) {
	WebPolicyDecisionListener listener = new WebPolicyDecisionListener(listenerID);
	listener.use();
}

void webView_unableToImplementPolicyWithError_frame(int sender, int error, int frame) {
}

/* WebDownload */

void download_decideDestinationWithSuggestedFilename(int downloadId, int filename) {
	NSString string = new NSString(filename);
	char[] buffer = new char[string.length()];
	string.getCharacters_(buffer);
	String name = new String(buffer);
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

void handleEvent(int evtId) {
	DOMEvent evt = new DOMEvent(evtId);
	NSString string = evt.type();
	char[] buffer = new char[string.length()];
	string.getCharacters_(buffer);
	String type = new String(buffer);

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
		if (DOMEVENT_KEYDOWN.equals(type)) {
			keyEvent.type = SWT.KeyDown;
		} else {
			keyEvent.type = SWT.KeyUp;
		}
		keyEvent.keyCode = translateKey(keyCode);
		keyEvent.character = (char)charCode;
		keyEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		browser.notifyListeners(keyEvent.type, keyEvent);
		if (!keyEvent.doit) {
			event.preventDefault();
		}
		return;
	}

	if (DOMEVENT_MOUSEWHEEL.equals(type)) {
		DOMWheelEvent event = new DOMWheelEvent(evtId);
		int clientX = event.clientX();
		int clientY = event.clientY();
		int delta = event.wheelDelta();
		boolean ctrl = event.ctrlKey();
		boolean shift = event.shiftKey();
		boolean alt = event.altKey();
		boolean meta = event.metaKey();
		Event mouseEvent = new Event();
		mouseEvent.type = SWT.MouseWheel;
		mouseEvent.widget = browser;
		mouseEvent.x = clientX; mouseEvent.y = clientY;
		mouseEvent.count = delta / 120;
		mouseEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		browser.notifyListeners (mouseEvent.type, mouseEvent);
		return;
	}

	/* mouse event */

	DOMMouseEvent event = new DOMMouseEvent(evtId);

	int clientX = event.clientX();
	int clientY = event.clientY();
	int detail = event.detail();
	int button = event.button();
	boolean ctrl = event.ctrlKey();
	boolean shift = event.shiftKey();
	boolean alt = event.altKey();
	boolean meta = event.metaKey();

	Event mouseEvent = new Event ();
	mouseEvent.widget = browser;
	mouseEvent.x = clientX; mouseEvent.y = clientY;
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
		* Bug in Safari.  Spurious and redundant mousemove events are received in
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
		mouseEvent.x = clientX; mouseEvent.y = clientY;
		mouseEvent.stateMask = (alt ? SWT.ALT : 0) | (ctrl ? SWT.CTRL : 0) | (shift ? SWT.SHIFT : 0) | (meta ? SWT.COMMAND : 0);
		mouseEvent.type = SWT.MouseDoubleClick;
		mouseEvent.button = button + 1;
		mouseEvent.count = detail;
		browser.notifyListeners (mouseEvent.type, mouseEvent);
	}
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.internal.*;

class WebKit {
		
static {
	Library.loadLibrary("swt-webkit"); //$NON-NLS-1$
	WebInitForCarbon();
}
	
/* Objective-C class ids */
static final int C_NSNotificationCenter = WebKit.objc_getClass("NSNotificationCenter"); //$NON-NLS-1$
static final int C_NSNumber = WebKit.objc_getClass("NSNumber"); //$NON-NLS-1$
static final int C_NSURL = WebKit.objc_getClass("NSURL"); //$NON-NLS-1$
static final int C_NSURLRequest = WebKit.objc_getClass("NSURLRequest"); //$NON-NLS-1$
static final int C_WebKitDelegate = WebKit.objc_getClass("WebKitDelegate"); //$NON-NLS-1$
	
/* Objective-C method selectors */
static final int S_absoluteString = WebKit.sel_registerName("absoluteString"); //$NON-NLS-1$
static final int S_addObserver_selector_name_object = WebKit.sel_registerName("addObserver:selector:name:object:"); //$NON-NLS-1$
static final int S_alloc = WebKit.sel_registerName("alloc"); //$NON-NLS-1$
static final int S_autorelease = WebKit.sel_registerName("autorelease"); //$NON-NLS-1$
static final int S_canGoBack = WebKit.sel_registerName("canGoBack"); //$NON-NLS-1$
static final int S_canGoForward = WebKit.sel_registerName("canGoForward"); //$NON-NLS-1$
static final int S_dataSource = WebKit.sel_registerName("dataSource"); //$NON-NLS-1$
static final int S_defaultCenter = WebKit.sel_registerName("defaultCenter"); //$NON-NLS-1$
static final int S_goBack = WebKit.sel_registerName("goBack:"); //$NON-NLS-1$
static final int S_goForward = WebKit.sel_registerName("goForward:"); //$NON-NLS-1$
static final int S_handleNotification = WebKit.sel_registerName("handleNotification:"); //$NON-NLS-1$
static final int S_ignore = WebKit.sel_registerName("ignore"); //$NON-NLS-1$
static final int S_initialRequest = WebKit.sel_registerName("initialRequest"); //$NON-NLS-1$
static final int S_initWithProc = WebKit.sel_registerName("initWithProc:user_data:"); //$NON-NLS-1$
static final int S_loadHTMLStringbaseURL = WebKit.sel_registerName("loadHTMLString:baseURL:"); //$NON-NLS-1$
static final int S_loadRequest = WebKit.sel_registerName("loadRequest:"); //$NON-NLS-1$
static final int S_mainFrame = WebKit.sel_registerName("mainFrame"); //$NON-NLS-1$
static final int S_name = WebKit.sel_registerName("name"); //$NON-NLS-1$
static final int S_numberWithInt = WebKit.sel_registerName("numberWithInt:"); //$NON-NLS-1$
static final int S_provisionalDataSource = WebKit.sel_registerName("provisionalDataSource"); //$NON-NLS-1$
static final int S_release = WebKit.sel_registerName("release"); //$NON-NLS-1$
static final int S_reload = WebKit.sel_registerName("reload:"); //$NON-NLS-1$
static final int S_retain = WebKit.sel_registerName("retain"); //$NON-NLS-1$
static final int S_removeObserver_name_object = WebKit.sel_registerName("removeObserver:name:object:"); //$NON-NLS-1$
static final int S_requestWithURL = WebKit.sel_registerName("requestWithURL:"); //$NON-NLS-1$
static final int S_request = WebKit.sel_registerName("request"); //$NON-NLS-1$
static final int S_retainCount = WebKit.sel_registerName("retainCount"); //$NON-NLS-1$
static final int S_setFrameLoadDelegate = WebKit.sel_registerName("setFrameLoadDelegate:"); //$NON-NLS-1$
static final int S_setGroupName = WebKit.sel_registerName("setGroupName:"); //$NON-NLS-1$
static final int S_setPolicyDelegate = WebKit.sel_registerName("setPolicyDelegate:"); //$NON-NLS-1$
static final int S_setResourceLoadDelegate = WebKit.sel_registerName("setResourceLoadDelegate:"); //$NON-NLS-1$
static final int S_setStatusText = WebKit.sel_registerName("setStatusText:"); //$NON-NLS-1$
static final int S_setUIDelegate = WebKit.sel_registerName("setUIDelegate:"); //$NON-NLS-1$
static final int S_stopLoading = WebKit.sel_registerName("stopLoading:"); //$NON-NLS-1$
static final int S_stringByEvaluatingJavaScriptFromString = WebKit.sel_registerName("stringByEvaluatingJavaScriptFromString:"); //$NON-NLS-1$
static final int S_takeStringURLFrom = WebKit.sel_registerName("takeStringURLFrom:"); //$NON-NLS-1$
static final int S_use = WebKit.sel_registerName("use"); //$NON-NLS-1$
static final int S_webFrame = WebKit.sel_registerName("webFrame"); //$NON-NLS-1$
static final int S_URL = WebKit.sel_registerName("URL"); //$NON-NLS-1$
static final int S_URLWithString = WebKit.sel_registerName("URLWithString:"); //$NON-NLS-1$
	
/* WebKit */
static final native int HIWebViewCreate(int[] outView);
static final native int HIWebViewGetWebView(int inView);
static final native void WebInitForCarbon();
	
/* OBJ-C runtime primitives */
static final native int objc_getClass(byte[] className);
static final native int objc_msgSend(int object, int selector);
static final native int objc_msgSend(int object, int selector, int arg0);
static final native int objc_msgSend(int object, int selector, int arg0, int arg1);
static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2);
static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2, int arg3);
static final native int sel_registerName(byte[] selectorName);

static int sel_registerName(String selector) {
	byte[] buffer0 = selector.getBytes();
	int length = buffer0.length;
	byte[] buffer = new byte[length+1];
	System.arraycopy(buffer0, 0, buffer, 0, length);
	return WebKit.sel_registerName(buffer);
}
	
static int objc_getClass(String className) {
	byte[] buffer0 = className.getBytes();
	int length = buffer0.length;
	byte[] buffer = new byte[length+1];
	System.arraycopy(buffer0, 0, buffer, 0, length);
	return WebKit.objc_getClass(buffer);
}
}

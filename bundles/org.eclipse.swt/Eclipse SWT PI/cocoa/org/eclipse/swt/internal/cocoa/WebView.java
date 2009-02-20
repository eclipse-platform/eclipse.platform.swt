/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebView extends NSView {

public WebView() {
	super();
}

public WebView(int /*long*/ id) {
	super(id);
}

public WebView(id id) {
	super(id);
}

public boolean canGoBack() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canGoBack);
}

public boolean canGoForward() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canGoForward);
}

public static boolean canShowMIMEType(NSString MIMEType) {
	return OS.objc_msgSend_bool(OS.class_WebView, OS.sel_canShowMIMEType_, MIMEType != null ? MIMEType.id : 0);
}

public boolean goBack() {
	return OS.objc_msgSend_bool(this.id, OS.sel_goBack);
}

public boolean goForward() {
	return OS.objc_msgSend_bool(this.id, OS.sel_goForward);
}

public WebView initWithFrame(NSRect frame, NSString frameName, NSString groupName) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_frameName_groupName_, frame, frameName != null ? frameName.id : 0, groupName != null ? groupName.id : 0);
	return result == this.id ? this : (result != 0 ? new WebView(result) : null);
}

public WebFrame mainFrame() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_mainFrame);
	return result != 0 ? new WebFrame(result) : null;
}

public void reload(id sender) {
	OS.objc_msgSend(this.id, OS.sel_reload_, sender != null ? sender.id : 0);
}

public void setApplicationNameForUserAgent(NSString applicationName) {
	OS.objc_msgSend(this.id, OS.sel_setApplicationNameForUserAgent_, applicationName != null ? applicationName.id : 0);
}

public void setDownloadDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDownloadDelegate_, delegate != null ? delegate.id : 0);
}

public void setFrameLoadDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setFrameLoadDelegate_, delegate != null ? delegate.id : 0);
}

public void setPolicyDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setPolicyDelegate_, delegate != null ? delegate.id : 0);
}

public void setPreferences(WebPreferences prefs) {
	OS.objc_msgSend(this.id, OS.sel_setPreferences_, prefs != null ? prefs.id : 0);
}

public void setResourceLoadDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setResourceLoadDelegate_, delegate != null ? delegate.id : 0);
}

public void setUIDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setUIDelegate_, delegate != null ? delegate.id : 0);
}

public void stopLoading(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stopLoading_, sender != null ? sender.id : 0);
}

public NSString stringByEvaluatingJavaScriptFromString(NSString script) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_stringByEvaluatingJavaScriptFromString_, script != null ? script.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}

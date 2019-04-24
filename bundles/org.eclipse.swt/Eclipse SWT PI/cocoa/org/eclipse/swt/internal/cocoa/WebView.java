/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebView extends NSView {

public WebView() {
	super();
}

public WebView(long id) {
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

public void copy(id sender) {
	OS.objc_msgSend(this.id, OS.sel_copy_, sender != null ? sender.id : 0);
}

public void cut(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cut_, sender != null ? sender.id : 0);
}

public boolean goBack() {
	return OS.objc_msgSend_bool(this.id, OS.sel_goBack);
}

public boolean goForward() {
	return OS.objc_msgSend_bool(this.id, OS.sel_goForward);
}

public WebView initWithFrame(NSRect frame, NSString frameName, NSString groupName) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_frameName_groupName_, frame, frameName != null ? frameName.id : 0, groupName != null ? groupName.id : 0);
	return result == this.id ? this : (result != 0 ? new WebView(result) : null);
}

public WebFrame mainFrame() {
	long result = OS.objc_msgSend(this.id, OS.sel_mainFrame);
	return result != 0 ? new WebFrame(result) : null;
}

public void paste(id sender) {
	OS.objc_msgSend(this.id, OS.sel_paste_, sender != null ? sender.id : 0);
}

public void reload(id sender) {
	OS.objc_msgSend(this.id, OS.sel_reload_, sender != null ? sender.id : 0);
}

public void setApplicationNameForUserAgent(NSString applicationNameForUserAgent) {
	OS.objc_msgSend(this.id, OS.sel_setApplicationNameForUserAgent_, applicationNameForUserAgent != null ? applicationNameForUserAgent.id : 0);
}

public void setCustomUserAgent(NSString customUserAgent) {
	OS.objc_msgSend(this.id, OS.sel_setCustomUserAgent_, customUserAgent != null ? customUserAgent.id : 0);
}

public void setDownloadDelegate(id downloadDelegate) {
	OS.objc_msgSend(this.id, OS.sel_setDownloadDelegate_, downloadDelegate != null ? downloadDelegate.id : 0);
}

public void setFrameLoadDelegate(id frameLoadDelegate) {
	OS.objc_msgSend(this.id, OS.sel_setFrameLoadDelegate_, frameLoadDelegate != null ? frameLoadDelegate.id : 0);
}

public void setPolicyDelegate(id policyDelegate) {
	OS.objc_msgSend(this.id, OS.sel_setPolicyDelegate_, policyDelegate != null ? policyDelegate.id : 0);
}

public void setPreferences(WebPreferences preferences) {
	OS.objc_msgSend(this.id, OS.sel_setPreferences_, preferences != null ? preferences.id : 0);
}

public void setResourceLoadDelegate(id resourceLoadDelegate) {
	OS.objc_msgSend(this.id, OS.sel_setResourceLoadDelegate_, resourceLoadDelegate != null ? resourceLoadDelegate.id : 0);
}

public void setUIDelegate(id UIDelegate) {
	OS.objc_msgSend(this.id, OS.sel_setUIDelegate_, UIDelegate != null ? UIDelegate.id : 0);
}

public void stopLoading(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stopLoading_, sender != null ? sender.id : 0);
}

}

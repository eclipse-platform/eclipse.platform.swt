/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class WindowCreator {
	XPCOMObject supports;
	XPCOMObject windowCreator;
	int refCount = 0;

public WindowCreator() {
	createCOMInterfaces();
}

int AddRef() {
	refCount++;
	return refCount;
}

void createCOMInterfaces() {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	
	windowCreator = new XPCOMObject(new int[]{2, 0, 0, 3}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return CreateChromeWindow(args[0], args[1], args[2]);}
	};		
}

void disposeCOMInterfaces() {
	if (supports != null) {
		supports.dispose();
		supports = null;
	}	
	if (windowCreator != null) {
		windowCreator.dispose();
		windowCreator = null;	
	}
}

int getAddress() {
	return windowCreator.getAddress();
}

int queryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);
	
	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {supports.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIWindowCreator.NS_IWINDOWCREATOR_IID)) {
		XPCOM.memmove(ppvObject, new int[] {windowCreator.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	
	XPCOM.memmove(ppvObject, new int[] {0}, 4);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release() {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces();
	return refCount;
}
	
/* nsIWindowCreator */

int CreateChromeWindow(int parent, int chromeFlags, int _retval) {
	nsIWebBrowserChrome browserChromeParent = new nsIWebBrowserChrome(parent);
	int[] aWebBrowser = new int[1];
	int rc = browserChromeParent.GetWebBrowser(aWebBrowser);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (aWebBrowser[0] == 0) Browser.error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIWebBrowser webBrowser = new nsIWebBrowser(aWebBrowser[0]);
	int[] result = new int[1];
	rc = webBrowser.QueryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (result[0] == 0) Browser.error(XPCOM.NS_ERROR_NO_INTERFACE);
	webBrowser.Release();
	
	nsIBaseWindow baseWindow = new nsIBaseWindow(result[0]);
	result[0] = 0;
	int[] aParentNativeWindow = new int[1];
	rc = baseWindow.GetParentNativeWindow(aParentNativeWindow);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (aParentNativeWindow[0] == 0) Browser.error(XPCOM.NS_ERROR_NO_INTERFACE);
	baseWindow.Release();

	Display display = Display.getCurrent();
	Browser src = (Browser)display.findWidget(aParentNativeWindow[0]);
	WindowEvent event = new WindowEvent(src);
	event.display = display;
	event.widget = src;
	for (int i = 0; i < src.openWindowListeners.length; i++)
		src.openWindowListeners[i].open(event);
	Browser browser = event.browser;
	boolean doit = browser != null && !browser.isDisposed();
	if (doit) {
		int address = browser.webBrowserChrome.getAddress();
		nsIWebBrowserChrome webBrowserChrome = new nsIWebBrowserChrome(address);
		webBrowserChrome.AddRef();
		XPCOM.memmove(_retval, new int[] {address}, 4);
	}		
	return doit ? XPCOM.NS_OK : XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}
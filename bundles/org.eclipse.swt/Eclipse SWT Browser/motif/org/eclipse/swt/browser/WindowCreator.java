/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
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
	if (parent == 0) return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
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
	Browser src = Browser.findBrowser(aParentNativeWindow[0]);
	final Browser browser;
	boolean doit = false;
	if ((chromeFlags & nsIWebBrowserChrome.CHROME_MODAL) != 0) {
		/*
		* Feature on Mozilla.  On platforms that lack a native dialog, Mozilla sends a
		* requests for a new Browser instance in a modal window. e.g. on Windows, Mozilla
		* brings up automatically a native Print Dialog in response to the javascript
		* command window.print() whereas on Linux Mozilla requests a new modal window
		* and a Browser to display an emulated HTML based print dialog. For this reason,
		* modal requests are handled here and not exposed to the user.
		*/
		final Shell shell = new Shell(src.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setLayout(new FillLayout());
		browser = new Browser(shell, SWT.NONE);
		browser.addVisibilityWindowListener(new VisibilityWindowListener() {
			public void hide(WindowEvent event) {
			}
			public void show(WindowEvent event) {
				if (event.location != null) shell.setLocation(event.location);
				if (event.size != null) {
					Point size = event.size;
					shell.setSize(shell.computeSize(size.x, size.y));
				}
				shell.open();
			}
		});
		browser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				shell.close();
			}
		});
		doit = true;
	} else {
		WindowEvent event = new WindowEvent(src);
		event.display = display;
		event.widget = src;
		event.required = true;
		for (int i = 0; i < src.openWindowListeners.length; i++)
			src.openWindowListeners[i].open(event);
		browser = event.browser;
		doit = browser != null && !browser.isDisposed();
		if (doit) {
			browser.addressBar = (chromeFlags & nsIWebBrowserChrome.CHROME_LOCATIONBAR) != 0;
			browser.menuBar = (chromeFlags & nsIWebBrowserChrome.CHROME_MENUBAR) != 0;
			browser.statusBar = (chromeFlags & nsIWebBrowserChrome.CHROME_STATUSBAR) != 0;
			browser.toolBar = (chromeFlags & nsIWebBrowserChrome.CHROME_TOOLBAR) != 0;
		}
	}
	if (doit) {
		int address = browser.webBrowserChrome.getAddress();
		nsIWebBrowserChrome webBrowserChrome = new nsIWebBrowserChrome(address);
		webBrowserChrome.AddRef();
		XPCOM.memmove(_retval, new int[] {address}, 4);
	}
	return doit ? XPCOM.NS_OK : XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}

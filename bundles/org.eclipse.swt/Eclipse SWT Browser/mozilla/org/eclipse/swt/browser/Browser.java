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

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.mozilla.*;

public class Browser extends Composite {
	
	BrowserSite browserSite;
	nsIWebBrowser webBrowser;

	static nsIAppShell appShell;
	static AppFileLocProvider locProvider; 
	static int browserCount;

	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.browser."; //$NON-NLS-1$

public Browser(Composite parent, int style) { 
	super(parent,style);
	
	browserCount++;
	int[] result = new int[1];
	if (browserCount == 1) {
		String mozillaPath = GRE.mozillaPath;
		if (mozillaPath == null) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_FAILURE));

		locProvider = new AppFileLocProvider();
		locProvider.AddRef();

		int[] retVal = new int[1];
		int rc = XPCOM.NS_NewLocalFile(mozillaPath, true, retVal);
		if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
		if (retVal[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NULL_POINTER));
			
		nsILocalFile localFile = new nsILocalFile(retVal[0]);
		rc = XPCOM.NS_InitEmbedding(localFile.getAddress(), locProvider.getAddress());
		if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
		localFile.Release(); 

		rc = XPCOM.NS_GetComponentManager(result);
		if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
		if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_NOINTERFACE));
		
		nsIComponentManager componentManager = new nsIComponentManager(result[0]);
		result[0] = 0;
		rc = componentManager.CreateInstance(XPCOM.NS_APPSHELL_CID, 0, nsIAppShell.NS_IAPPSHELL_IID, result);
		if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
		if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_NOINTERFACE));		
		componentManager.Release();
		
		appShell = new nsIAppShell(result[0]); 
		rc = appShell.Create(null, null);
		if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
		rc = appShell.Spinup();
		if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	}
	int rc = XPCOM.NS_GetComponentManager(result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_NOINTERFACE));
	
	nsIComponentManager componentManager = new nsIComponentManager(result[0]);
	result[0] = 0;
	nsID NS_IWEBBROWSER_CID = new nsID("F1EAC761-87E9-11d3-AF80-00A024FFC08C"); //$NON-NLS-1$
	rc = componentManager.CreateInstance(NS_IWEBBROWSER_CID, 0, nsIWebBrowser.NS_IWEBBROWSER_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_NOINTERFACE));	
	componentManager.Release();
	
	webBrowser = new nsIWebBrowser(result[0]); 

	browserSite = new BrowserSite(this);
	browserSite.AddRef();
	
	XPCOMObject webBrowserChrome = browserSite.webBrowserChrome;
	rc = webBrowser.SetContainerWindow(webBrowserChrome.getAddress());
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
			
	rc = webBrowser.QueryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIBaseWindow baseWindow = new nsIBaseWindow(result[0]);	
	Rectangle rect = getClientArea();
	if (rect.isEmpty()) {
		rect.width = 1;
		rect.height = 1;
	}
	rc = baseWindow.InitWindow(handle, 0, 0, 0, rect.width, rect.height);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_FAILURE));
	rc = baseWindow.Create();
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_FAILURE));
	rc = baseWindow.SetVisibility(true);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_FAILURE));
	baseWindow.Release();
	
	XPCOMObject weakReference = browserSite.weakReference;
	rc = webBrowser.AddWebBrowserListener(weakReference.getAddress(), nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));

	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose: onDispose(); break;
				case SWT.Resize: onResize(); break;
				case SWT.FocusIn: onFocusGained(event);	break;
				case SWT.FocusOut: onFocusLost(event); break;
			}
		}
	};	
	int[] folderEvents = new int[]{
		SWT.Dispose,
		SWT.Resize,  
		SWT.FocusIn, 
		SWT.FocusOut, 
	};
	for (int i = 0; i < folderEvents.length; i++) {
		addListener(folderEvents[i], listener);
	}
}

public void addLocationListener(LocationListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	browserSite.addLocationListener(listener);
}

public void addProgressListener(ProgressListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	browserSite.addProgressListener(listener);
}

public void addStatusTextListener(StatusTextListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	browserSite.addStatusTextListener(listener);
}

public boolean back() {
	checkWidget();
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);		 	
	rc = webNavigation.GoBack();	
	webNavigation.Release();
	
	return rc == XPCOM.NS_OK;
}

protected void checkSubclass() {
	String name = getClass().getName();
	int index = name.lastIndexOf('.');
	if (!name.substring(0, index + 1).equals(PACKAGE_PREFIX)) {
		SWT.error(SWT.ERROR_INVALID_SUBCLASS);
	}
}

public boolean forward() {
	checkWidget();
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);
	rc = webNavigation.GoForward();
	webNavigation.Release();
	
	return rc == XPCOM.NS_OK;
}

public String getUrl() {
	checkWidget();           
	int[] aContentDOMWindow = new int[1];
	int rc = webBrowser.GetContentDOMWindow(aContentDOMWindow);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (aContentDOMWindow[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
			
	nsIDOMWindow domWindow = new nsIDOMWindow(aContentDOMWindow[0]);           
	int[] result = new int[1];
	rc = domWindow.QueryInterface(nsIDOMWindowInternal.NS_IDOMWINDOWINTERNAL_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	domWindow.Release();
         
	nsIDOMWindowInternal domWindowInternal = new nsIDOMWindowInternal(result[0]);
	int[] aLocation = new int[1];  
	rc = domWindowInternal.GetLocation(aLocation);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (aLocation[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	domWindowInternal.Release();

	nsIDOMLocation domLocation = new nsIDOMLocation(aLocation[0]); 
    nsString _retval = new nsString();
	rc = domLocation.ToString(_retval.getAddress());
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	domLocation.Release();          
	String url = _retval.toString();
	_retval.dispose();
                
	return url;
}

nsIWebBrowser getWebBrowser() {
	if (webBrowser == null) return null;
	webBrowser.AddRef();
	return webBrowser;	
}

void onDispose() {
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIBaseWindow baseWindow = new nsIBaseWindow(result[0]);
	rc = baseWindow.Destroy();
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	baseWindow.Release();
	
	browserSite.Release();
	webBrowser.Release();
	
	browserCount--;
	if (browserCount == 0) {
		if (appShell != null) {
			// Shutdown the appshell service.
			rc = appShell.Spindown();
			if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
			appShell.Release();
			appShell = null;
		}
		locProvider.Release();
		locProvider = null;
		XPCOM.NS_TermEmbedding();
	}
}

void onFocusGained(Event e) {
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebBrowserFocus.NS_IWEBBROWSERFOCUS_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIWebBrowserFocus webBrowserFocus = new nsIWebBrowserFocus(result[0]);
	rc = webBrowserFocus.Activate();
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	webBrowserFocus.Release();
}
	
void onFocusLost(Event e) {
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebBrowserFocus.NS_IWEBBROWSERFOCUS_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIWebBrowserFocus webBrowserFocus = new nsIWebBrowserFocus(result[0]);
	rc = webBrowserFocus.Deactivate();
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	webBrowserFocus.Release();
}

void onResize() {
	Rectangle rect = getClientArea();
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIBaseWindow baseWindow = new nsIBaseWindow(result[0]);
	rc = baseWindow.SetPositionAndSize(rect.x, rect.y, rect.width, rect.height, true);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	baseWindow.Release();
}

public void refresh() {
	checkWidget();
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);		 	
	rc = webNavigation.Reload(nsIWebNavigation.LOAD_FLAGS_NONE);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));	
	webNavigation.Release();
}

public void removeLocationListener(LocationListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	browserSite.removeLocationListener(listener);
}

public void removeProgressListener(ProgressListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	browserSite.removeProgressListener(listener);
}

public void removeStatusTextListener(StatusTextListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	browserSite.removeStatusTextListener(listener);
}

public boolean setUrl(String url) {
	checkWidget();
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));

	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);
    char[] arg = url.toCharArray(); 
    char[] c = new char[arg.length+1];
    System.arraycopy(arg,0,c,0,arg.length);
	rc = webNavigation.LoadURI(c, nsIWebNavigation.LOAD_FLAGS_NONE, 0, 0, 0);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	onFocusGained(null); // Fixes the keyboard INput Tag issues
	webNavigation.Release();
	return true;
}

public void stop() {
	checkWidget();
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	if (result[0] == 0) throw new SWTError(XPCOM.errorMsg(XPCOM.NS_ERROR_NO_INTERFACE));
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);	 	
	rc = webNavigation.Stop(nsIWebNavigation.STOP_ALL);
	if (rc != XPCOM.NS_OK) throw new SWTError(XPCOM.errorMsg(rc));
	webNavigation.Release();
}

void setWebBrowser(nsIWebBrowser aWebBrowser) {
	if (webBrowser != null) webBrowser.Release();
	webBrowser = aWebBrowser;
	if (webBrowser != null) webBrowser.AddRef();
}
}
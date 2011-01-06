/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

class WebPolicyDelegate {
	COMObject iWebPolicyDelegate;
	int refCount = 0;

	Browser browser;

WebPolicyDelegate () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	iWebPolicyDelegate = new COMObject (new int[] {2, 0, 0, 5, 5, 5, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return decidePolicyForNavigationAction (args[0], args[1], args[2], args[3], args[4]);}
		public int /*long*/ method4 (int /*long*/[] args) {return decidePolicyForNewWindowAction (args[0], args[1], args[2], args[3], args[4]);}
		public int /*long*/ method5 (int /*long*/[] args) {return decidePolicyForMIMEType (args[0], args[1], args[2], args[3], args[4]);}
		public int /*long*/ method6 (int /*long*/[] args) {return unableToImplementPolicyWithError (args[0], args[1], args[2]);}
	};
}

int decidePolicyForMIMEType (int /*long*/ webView, int /*long*/ type, int /*long*/ request, int /*long*/ frame, int /*long*/ listener) {
	IWebView iwebView = new IWebView (webView);
	int[] canShow = new int[1];
	iwebView.canShowMIMEType (type, canShow);
	IWebPolicyDecisionListener pdListener = new IWebPolicyDecisionListener (listener);
	if (canShow[0] != 0) {
		pdListener.use ();
	} else {
		pdListener.download ();
	}
	return COM.S_OK;
}

int decidePolicyForNavigationAction (int /*long*/ webView, int /*long*/ actionInformation, int /*long*/ request, int /*long*/ frame, int /*long*/ listener) {
	IWebURLRequest iwebUrlRequest = new IWebURLRequest (request);
	int /*long*/[] result = new int /*long*/[1];
	int hr = iwebUrlRequest.URL (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	String url = WebKit.extractBSTR (result[0]);
	COM.SysFreeString (result[0]);
	IWebPolicyDecisionListener pdListener = new IWebPolicyDecisionListener (listener);
	WebKit webKit = (WebKit)browser.webBrowser;
	if (webKit.loadingText) {
		/* 
		 * WebKit is auto-navigating to about:blank in response to a loadHTMLString()
		 * invocation.  This navigate should always proceed without sending an event
		 * since it is preceded by an explicit navigate to about:blank in setText().
		 */
		pdListener.use ();
		return COM.S_OK;
	}
	if (url.length () == 0) {
		pdListener.ignore ();
		return COM.S_OK;
	}
	if (url.startsWith (WebKit.PROTOCOL_FILE) && webKit.getUrl ().startsWith (WebKit.ABOUT_BLANK) && webKit.untrustedText) {
		/* indicates an attempt to access the local file system from untrusted content */
		pdListener.ignore ();
		return COM.S_OK;
	}
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (url.equals (WebKit.URI_FILEROOT)) {
		url = WebKit.ABOUT_BLANK;
	} else {
		int length = WebKit.URI_FILEROOT.length ();
		if (url.startsWith (WebKit.URI_FILEROOT) && url.charAt (length) == '#') {
			url = WebKit.ABOUT_BLANK + url.substring (length);
		}
	}
	LocationEvent newEvent = new LocationEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	newEvent.location = url;
	newEvent.doit = true;
	LocationListener[] locationListeners = webKit.locationListeners;
	if (locationListeners != null) {
		for (int i = 0; i < locationListeners.length; i++) {
			locationListeners[i].changing (newEvent);
		}
	}
	if (newEvent.doit) {
		if (webKit.jsEnabledChanged) {
			webKit.jsEnabledChanged = false;
			IWebView view = new IWebView (webView);
			result[0] = 0;
			hr = view.preferences (result);
			if (hr == COM.S_OK && result[0] != 0) {
				IWebPreferences preferences = new IWebPreferences (result[0]);
				hr = preferences.setJavaScriptEnabled (webKit.jsEnabled ? 1 : 0);
				view.setPreferences (preferences.getAddress());
				preferences.Release ();
			}
		}
		pdListener.use ();
		webKit.lastNavigateURL = url;
	} else {
		pdListener.ignore ();
	}
	return COM.S_OK;
}

int decidePolicyForNewWindowAction (int /*long*/ webView, int /*long*/ actionInformation, int /*long*/ request, int /*long*/ frameName, int /*long*/ listener) {
	IWebPolicyDecisionListener pdListener = new IWebPolicyDecisionListener (listener);
	pdListener.use();
	return COM.S_OK;
}

protected void disposeCOMInterfaces () {
	if (iWebPolicyDelegate != null) {
		iWebPolicyDelegate.dispose ();
		iWebPolicyDelegate = null;
	}	
}

int /*long*/ getAddress () {
	return iWebPolicyDelegate.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID ();
	COM.MoveMemory (guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID (guid, COM.IIDIUnknown)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebPolicyDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebPolicyDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID (guid, WebKit_win32.IID_IWebPolicyDelegate)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebPolicyDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebPolicyDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}

	COM.MoveMemory (ppvObject, new int /*long*/[] {0}, OS.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) {
		disposeCOMInterfaces ();
	}
	return refCount;
}

void setBrowser (Browser browser) {
	this.browser = browser;
}

int unableToImplementPolicyWithError (int /*long*/ webView, int /*long*/ error, int /*long*/ frame) {
	if (browser.isDisposed ()) return COM.S_OK;

	IWebError iweberror = new IWebError (error);
	String failingURL = null;
	int /*long*/[] result = new int /*long*/[1];
	int hr = iweberror.failingURL (result);
	if (hr == COM.S_OK && result[0] != 0) {
		failingURL = WebKit.extractBSTR (result[0]);
		COM.SysFreeString (result[0]);
	}
	result[0] = 0;
	hr = iweberror.localizedDescription (result);
	if (hr != COM.S_OK || result[0] == 0) {
    	return COM.S_OK;
    }
	String description = WebKit.extractBSTR (result[0]);
	COM.SysFreeString (result[0]);

	String message = failingURL != null ? failingURL + "\n\n" : ""; //$NON-NLS-1$ //$NON-NLS-2$
	message += Compatibility.getMessage ("SWT_Page_Load_Failed", new Object[] {description}); //$NON-NLS-1$
	MessageBox messageBox = new MessageBox (browser.getShell (), SWT.OK | SWT.ICON_ERROR);
	messageBox.setMessage (message);
	messageBox.open ();
	return COM.S_OK;
}

}

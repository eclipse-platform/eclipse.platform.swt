/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

WebPolicyDelegate (Browser browser) {
	createCOMInterfaces ();
	this.browser = browser;
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	iWebPolicyDelegate = new COMObject (new int[] {2, 0, 0, 5, 5, 5, 3}) {
		@Override
		public long method0 (long[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long method1 (long[] args) {return AddRef ();}
		@Override
		public long method2 (long[] args) {return Release ();}
		@Override
		public long method3 (long[] args) {return decidePolicyForNavigationAction (args[0], args[1], args[2], args[3], args[4]);}
		@Override
		public long method4 (long[] args) {return decidePolicyForNewWindowAction (args[0], args[1], args[2], args[3], args[4]);}
		@Override
		public long method5 (long[] args) {return decidePolicyForMIMEType (args[0], args[1], args[2], args[3], args[4]);}
		@Override
		public long method6 (long[] args) {return unableToImplementPolicyWithError (args[0], args[1], args[2]);}
	};
}

int decidePolicyForMIMEType (long webView, long type, long request, long frame, long listener) {
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

int decidePolicyForNavigationAction (long webView, long actionInformation, long request, long frame, long listener) {
	IWebURLRequest iwebUrlRequest = new IWebURLRequest (request);
	long[] result = new long[1];
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
		for (LocationListener locationListener : locationListeners) {
			locationListener.changing (newEvent);
		}
	}
	if (newEvent.doit) {
		if (webKit.jsEnabled != webKit.jsEnabledOnNextPage) {
			webKit.jsEnabled = webKit.jsEnabledOnNextPage;
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

int decidePolicyForNewWindowAction (long webView, long actionInformation, long request, long frameName, long listener) {
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

long getAddress () {
	return iWebPolicyDelegate.getAddress ();
}

int QueryInterface (long riid, long ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID ();
	COM.MoveMemory (guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID (guid, COM.IIDIUnknown)) {
		OS.MoveMemory (ppvObject, new long[] {iWebPolicyDelegate.getAddress ()}, C.PTR_SIZEOF);
		new IUnknown (iWebPolicyDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID (guid, WebKit_win32.IID_IWebPolicyDelegate)) {
		OS.MoveMemory (ppvObject, new long[] {iWebPolicyDelegate.getAddress ()}, C.PTR_SIZEOF);
		new IUnknown (iWebPolicyDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}

	OS.MoveMemory (ppvObject, new long[] {0}, C.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) {
		disposeCOMInterfaces ();
	}
	return refCount;
}

int unableToImplementPolicyWithError (long webView, long error, long frame) {
	if (browser.isDisposed ()) return COM.S_OK;

	IWebError iweberror = new IWebError (error);
	String failingURL = null;
	long[] result = new long[1];
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

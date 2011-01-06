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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class WebResourceLoadDelegate {
	COMObject iWebResourceLoadDelegate;
	int refCount = 0;

	Browser browser;
	String postData;

WebResourceLoadDelegate () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	iWebResourceLoadDelegate = new COMObject (new int[] {2, 0, 0, 4, 6, 4, 4, 4, 4, 3, 4, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return identifierForInitialRequest (args[0], args[1], args[2], args[3]);}
		public int /*long*/ method4 (int /*long*/[] args) {return willSendRequest (args[0], args[1], args[2], args[3], args[4], args[5]);}
		public int /*long*/ method5 (int /*long*/[] args) {return didReceiveAuthenticationChallenge (args[0], args[1], args[2], args[3]);}
		public int /*long*/ method6 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method7 (int /*long*/[] args) {return COM.S_OK;}
		public int /*long*/ method8 (int /*long*/[] args) {return COM.S_OK;}
		public int /*long*/ method9 (int /*long*/[] args) {return COM.S_OK;}
		public int /*long*/ method10 (int /*long*/[] args) {return COM.S_OK;}
		public int /*long*/ method11 (int /*long*/[] args) {return COM.E_NOTIMPL;}
	};
}

int didReceiveAuthenticationChallenge (int /*long*/ webView, int /*long*/ identifier, int /*long*/ challenge, int /*long*/ dataSource) {
	IWebURLAuthenticationChallenge authenticationChallenge = new IWebURLAuthenticationChallenge (challenge);
	/*
	 * Do not invoke the listeners if this challenge has been failed too many
	 * times because a listener is likely giving incorrect credentials repeatedly
	 * and will do so indefinitely.
	 */
	int[] count = new int[1];
	int hr = authenticationChallenge.previousFailureCount (count);
	int /*long*/[] result = new int /*long*/[1];
	if (hr == COM.S_OK && count[0] < 3) {
		AuthenticationListener[] authenticationListeners = browser.webBrowser.authenticationListeners;
		for (int i = 0; i < authenticationListeners.length; i++) {
			AuthenticationEvent event = new AuthenticationEvent (browser);
			event.location = ((WebKit)browser.webBrowser).lastNavigateURL;
			authenticationListeners[i].authenticate (event);
			if (!event.doit) {
				hr = authenticationChallenge.sender (result);
				if (hr != COM.S_OK || result[0] == 0) {
					return COM.S_OK;
				}
				IWebURLAuthenticationChallengeSender challengeSender = new IWebURLAuthenticationChallengeSender (result[0]);
				challengeSender.cancelAuthenticationChallenge (challenge);
				challengeSender.Release ();
				return COM.S_OK;
			}
			if (event.user != null && event.password != null) {
				hr = authenticationChallenge.sender (result);
				if (hr != COM.S_OK || result[0] == 0) continue;

				IWebURLAuthenticationChallengeSender challengeSender = new IWebURLAuthenticationChallengeSender (result[0]);
				result[0] = 0;
				hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebURLCredential, 0, WebKit_win32.IID_IWebURLCredential, result);
				if (hr == COM.S_OK && result[0] != 0) {
					IWebURLCredential credential = new IWebURLCredential (result[0]);
					int /*long*/ user = WebKit.createBSTR (event.user);
					int /*long*/ password = WebKit.createBSTR (event.password);
					credential.initWithUser (user, password, WebKit_win32.WebURLCredentialPersistenceForSession);
					challengeSender.useCredential (credential.getAddress (), challenge);
					credential.Release ();
				}
				challengeSender.Release ();
				return COM.S_OK;
			}
		}
	}

	/* show a custom authentication dialog */

	String[] userReturn = new String[1], passwordReturn = new String[1];
	result[0] = 0;
	hr = authenticationChallenge.proposedCredential (result);
	if (hr == COM.S_OK && result[0] != 0) {
		IWebURLCredential proposedCredential = new IWebURLCredential(result[0]);
		result[0] = 0;
		hr = proposedCredential.user (result);
		if (hr == COM.S_OK && result[0] != 0) {
			userReturn[0] = WebKit.extractBSTR (result[0]);
			COM.SysFreeString (result[0]);
			int[] value = new int[1];
			hr = proposedCredential.hasPassword (value);
			if (hr == COM.S_OK && value[0] != 0) {
				result[0] = 0;
				hr = proposedCredential.password (result);
				if (hr == COM.S_OK && result[0] != 0) {
					passwordReturn[0] = WebKit.extractBSTR (result[0]);
					COM.SysFreeString (result[0]);
				}
			}
		}
		proposedCredential.Release ();
	}
	result[0] = 0;
	hr = authenticationChallenge.protectionSpace (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
    }
	IWebURLProtectionSpace space = new IWebURLProtectionSpace (result[0]);
	String host = null, realm = null;
	result[0] = 0;
	hr = space.host (result);
	if (hr == COM.S_OK && result[0] != 0) {
		host = WebKit.extractBSTR (result[0]);
		COM.SysFreeString (result[0]);
		int[] port = new int[1];
		hr = space.port (port);
		if (hr == COM.S_OK) {
			host += ":" + port[0]; //$NON-NLS-1$
			result[0] = 0;
			hr = space.realm (result);
			if (hr == COM.S_OK && result[0] != 0) {
				realm = WebKit.extractBSTR (result[0]);
				COM.SysFreeString (result[0]);
		    }
		}
	}
	space.Release ();
	boolean response = showAuthenticationDialog (userReturn, passwordReturn, host, realm);
	result[0] = 0;
	hr = authenticationChallenge.sender (result);
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	IWebURLAuthenticationChallengeSender challengeSender = new IWebURLAuthenticationChallengeSender (result[0]);
	if (!response) {
		challengeSender.cancelAuthenticationChallenge (challenge);
		challengeSender.Release ();
		return COM.S_OK;
	}
	result[0] = 0;
	hr = WebKit_win32.WebKitCreateInstance (WebKit_win32.CLSID_WebURLCredential, 0, WebKit_win32.IID_IWebURLCredential, result);
	if (hr == COM.S_OK && result[0] != 0) {
		IWebURLCredential credential = new IWebURLCredential (result[0]);
		int /*long*/ user = WebKit.createBSTR (userReturn[0]);
		int /*long*/ password = WebKit.createBSTR (passwordReturn[0]);
		credential.initWithUser (user, password, WebKit_win32.WebURLCredentialPersistenceForSession);
		challengeSender.useCredential (credential.getAddress (), challenge);
		credential.Release ();
	}
	challengeSender.Release ();
	return COM.S_OK;
}

void disposeCOMInterfaces () {
	if (iWebResourceLoadDelegate != null) {
		iWebResourceLoadDelegate.dispose ();
		iWebResourceLoadDelegate = null;
	}	
}

int /*long*/ getAddress () {
	return iWebResourceLoadDelegate.getAddress ();
}

int identifierForInitialRequest (int /*long*/ webView, int /*long*/ request, int /*long*/ dataSource, int /*long*/ identifier) {
	if (browser.isDisposed ()) return COM.S_OK;

	/* send progress event iff request is for top-level frame */

	IWebDataSource source = new IWebDataSource (dataSource);
	int /*long*/[] frame = new int /*long*/[1];
	int hr = source.webFrame (frame);
	if (hr != COM.S_OK || frame[0] == 0) {
    	return COM.S_OK;
    }
	new IWebFrame (frame[0]).Release ();
	int /*long*/[] mainFrame = new int /*long*/[1];
	IWebView iWebView = new IWebView (webView);
	hr = iWebView.mainFrame (mainFrame);
	if (hr != COM.S_OK || mainFrame[0] == 0) {
    	return COM.S_OK;
    }
	new IWebFrame (mainFrame[0]).Release ();
	if (frame[0] == mainFrame[0]) {
		int /*long*/ ptr = OS.malloc (8);
		iWebView.estimatedProgress (ptr);
		double[] estimate = new double[1];
		OS.MoveMemory (estimate, ptr, 8);
		OS.free (ptr);
		int progress = (int)(estimate[0] * 100);

		ProgressEvent progressEvent = new ProgressEvent (browser);
		progressEvent.display = browser.getDisplay ();
		progressEvent.widget = browser;
		progressEvent.current = progress;
		progressEvent.total = Math.max (progress, WebKit.MAX_PROGRESS);
		ProgressListener[] progressListeners = browser.webBrowser.progressListeners;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].changed (progressEvent);
		}
	}
	return COM.S_OK;
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID ();
	COM.MoveMemory (guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID (guid, COM.IIDIUnknown)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebResourceLoadDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebResourceLoadDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID (guid, WebKit_win32.IID_IWebResourceLoadDelegate)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebResourceLoadDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebResourceLoadDelegate.getAddress ()).AddRef ();
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

boolean showAuthenticationDialog (final String[] user, final String[] password, String host, String realm) {
	Shell parent = browser.getShell ();
	final Shell shell = new Shell (parent);
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
	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			user[0] = userText.getText ();
			password[0] = passwordText.getText ();
			result[0] = event.widget == buttons[1];
			shell.close ();
		}	
	};

	Composite composite = new Composite (shell, SWT.NONE);
	data = new GridData ();
	data.horizontalAlignment = GridData.END;
	composite.setLayoutData (data);
	composite.setLayout (new GridLayout (2, true));
	buttons[0] = new Button (composite, SWT.PUSH);
	buttons[0].setText (SWT.getMessage ("SWT_Cancel")); //$NON-NLS-1$
	buttons[0].setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	buttons[0].addListener (SWT.Selection, listener);
	buttons[1] = new Button (composite, SWT.PUSH);
	buttons[1].setText (SWT.getMessage ("SWT_OK")); //$NON-NLS-1$
	buttons[1].setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	buttons[1].addListener (SWT.Selection, listener);

	shell.setDefaultButton (buttons[1]);
	shell.pack ();
	Rectangle parentSize = parent.getBounds ();
	Rectangle shellSize = shell.getBounds ();
	int x = parent.getLocation().x + (parentSize.width - shellSize.width) / 2;
	int y = parent.getLocation().y + (parentSize.height - shellSize.height) / 2;
	shell.setLocation (x, y);
	shell.open ();
	Display display = browser.getDisplay ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}

	return result[0];
}

int willSendRequest (int /*long*/ webView, int /*long*/ identifier, int /*long*/ request, int /*long*/ redirectResponse, int /*long*/ dataSource, int /*long*/ newRequest) {
	IWebURLRequest req = new IWebURLRequest (request);
	int /*long*/[] result = new int /*long*/ [1];
	int hr = req.URL (result);
	if (hr == COM.S_OK && result[0] != 0) {
		String url = WebKit.extractBSTR (result[0]);
		COM.SysFreeString (result[0]);
		/*
		 * file://c|/ doesn't work on Webkit but works on other browsers. 
		 * So change file:// to file:/// to be consistent
		 */
		if (url.startsWith (WebKit.PROTOCOL_FILE) && !url.startsWith (WebKit.URI_FILEROOT)) {
			int length = WebKit.PROTOCOL_FILE.length ();
			url = WebKit.URI_FILEROOT + url.substring (length);
			result[0] = 0;

			hr = req.mutableCopy (result);
			if (hr == COM.S_OK && result[0] != 0) {
				IWebMutableURLRequest mReq = new IWebMutableURLRequest (result[0]);
				int /*long*/ urlString = WebKit.createBSTR (url);
				mReq.setURL (urlString);
				OS.MoveMemory (newRequest, new int /*long*/[] {mReq.getAddress ()}, C.PTR_SIZEOF);
				return COM.S_OK;
			}
		}
	}
	req.AddRef ();
	OS.MoveMemory (newRequest, new int /*long*/[] {request}, C.PTR_SIZEOF);
	return COM.S_OK;
}

}

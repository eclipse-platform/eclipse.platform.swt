/*******************************************************************************
 * Copyright (c) 2003, 2012 IBM Corporation and others.
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
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class WindowCreator2 {
	XPCOMObject supports;
	XPCOMObject windowCreator;
	XPCOMObject windowCreator2;
	int refCount = 0;

WindowCreator2 () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
	};

	windowCreator = new XPCOMObject (new int[] {2, 0, 0, 3}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return CreateChromeWindow (args[0], (int)/*64*/args[1], args[2]);}
	};

	windowCreator2 = new XPCOMObject (new int[] {2, 0, 0, 3, 6}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return CreateChromeWindow (args[0], (int)/*64*/args[1], args[2]);}
		public long /*int*/ method4 (long /*int*/[] args) {return CreateChromeWindow2 (args[0], (int)/*64*/args[1], (int)/*64*/args[2], args[3], args[4], args[5]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (windowCreator != null) {
		windowCreator.dispose ();
		windowCreator = null;	
	}

	if (windowCreator2 != null) {
		windowCreator2.dispose ();
		windowCreator2 = null;	
	}
}

long /*int*/ getAddress () {
	return windowCreator.getAddress ();
}

int QueryInterface (long /*int*/ riid, long /*int*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIWindowCreator.NS_IWINDOWCREATOR_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {windowCreator.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIWindowCreator2.NS_IWINDOWCREATOR2_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {windowCreator2.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}

	XPCOM.memmove (ppvObject, new long /*int*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}
	
/* nsIWindowCreator */

int CreateChromeWindow (long /*int*/ parent, int chromeFlags, long /*int*/ _retval) {
	return CreateChromeWindow2 (parent, chromeFlags, 0, 0, 0, _retval);
}

/* nsIWindowCreator2 */

int CreateChromeWindow2 (long /*int*/ parent, int chromeFlags, int contextFlags, long /*int*/ uri, long /*int*/ cancel, long /*int*/ _retval) {
	if (parent == 0 && (chromeFlags & nsIWebBrowserChrome.CHROME_OPENAS_CHROME) == 0) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}
	Browser src = null; 
	if (parent != 0) {
		nsIWebBrowserChrome browserChromeParent = new nsIWebBrowserChrome (parent);
		long /*int*/[] aWebBrowser = new long /*int*/[1];
		int rc = browserChromeParent.GetWebBrowser (aWebBrowser);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (aWebBrowser[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);

		nsIWebBrowser webBrowser = new nsIWebBrowser (aWebBrowser[0]);
		long /*int*/[] result = new long /*int*/[1];
		rc = webBrowser.QueryInterface (nsIBaseWindow.NS_IBASEWINDOW_10_IID, result);
		if (rc != XPCOM.NS_OK) {
			rc = webBrowser.QueryInterface (nsIBaseWindow.NS_IBASEWINDOW_IID, result);
			if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		}
		if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
		webBrowser.Release ();

		nsIBaseWindow baseWindow = new nsIBaseWindow (result[0]);
		result[0] = 0;
		long /*int*/[] aParentNativeWindow = new long /*int*/[1];
		rc = baseWindow.GetParentNativeWindow (aParentNativeWindow);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (aParentNativeWindow[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
		baseWindow.Release ();

		src = Mozilla.findBrowser (aParentNativeWindow[0]);
	}
	final Browser browser;
	boolean doit = true;
	if ((chromeFlags & nsIWebBrowserChrome.CHROME_OPENAS_CHROME) != 0) {
		/*
		* Mozilla will request a new Browser in a modal window in order to emulate a native
		* dialog that is not available to it (eg.- a print dialog on Linux).  For this
		* reason modal requests are handled here so that the user is not exposed to them.
		*/
		int style = SWT.NONE;
		if ((chromeFlags & nsIWebBrowserChrome.CHROME_WINDOW_POPUP) == 0) {
			/* add dialog trim for all windows except pop-ups */
			style |= SWT.DIALOG_TRIM;
		}
		if ((chromeFlags & nsIWebBrowserChrome.CHROME_MODAL) != 0) style |= SWT.APPLICATION_MODAL; 
		final Shell shell = src == null ?
			new Shell (style) :
			new Shell (src.getShell(), style);
		shell.setLayout (new FillLayout ());
		browser = new Browser (shell, src == null ? SWT.MOZILLA : src.getStyle () & SWT.MOZILLA);
		browser.addVisibilityWindowListener (new VisibilityWindowListener () {
			public void hide (WindowEvent event) {
			}
			public void show (WindowEvent event) {
				if (event.location != null) shell.setLocation (event.location);
				if (event.size != null) {
					Point size = event.size;
					shell.setSize (shell.computeSize (size.x, size.y));
				}
				shell.open ();
			}
		});
		browser.addCloseWindowListener (new CloseWindowListener () {
			public void close (WindowEvent event) {
				shell.close ();
			}
		});
		if (uri != 0) {
			nsIURI location = new nsIURI (uri);
			long /*int*/ aSpec = XPCOM.nsEmbedCString_new ();
			if (location.GetSpec (aSpec) == XPCOM.NS_OK) {
				int length = XPCOM.nsEmbedCString_Length (aSpec);
				if (length > 0) {
					long /*int*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
					byte[] dest = new byte[length];
					XPCOM.memmove (dest, buffer, length);
					browser.setUrl (new String (dest));
				}
			}
			XPCOM.nsEmbedCString_delete (aSpec);
		}
	} else {
		WindowEvent event = new WindowEvent (src);
		event.display = src.getDisplay ();
		event.widget = src;
		event.required = true;
		for (int i = 0; i < src.webBrowser.openWindowListeners.length; i++) {
			src.webBrowser.openWindowListeners[i].open (event);
		}
		browser = event.browser;

		/* Ensure that the Browser provided by the client is valid for use */ 
		doit = browser != null && !browser.isDisposed ();
		if (doit) {
			String platform = Platform.PLATFORM;
			boolean isMozillaNativePlatform = platform.equals ("gtk") || platform.equals ("motif"); //$NON-NLS-1$ //$NON-NLS-2$
			doit = isMozillaNativePlatform || (browser.getStyle () & SWT.MOZILLA) != 0;
		}
	}
	if (doit) {
		Mozilla mozilla = (Mozilla)browser.webBrowser;
		mozilla.isChild = true;
		long /*int*/ chromePtr = mozilla.webBrowserChrome.getAddress ();
		nsIWebBrowserChrome webBrowserChrome = new nsIWebBrowserChrome (chromePtr);
		webBrowserChrome.SetChromeFlags (chromeFlags);
		webBrowserChrome.AddRef ();
		XPCOM.memmove (_retval, new long /*int*/[] {chromePtr}, C.PTR_SIZEOF);
	} else {
		if (cancel != 0) {
			XPCOM.memmove (cancel, new boolean[] {true});
		}
	}
	return doit ? XPCOM.NS_OK : XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}

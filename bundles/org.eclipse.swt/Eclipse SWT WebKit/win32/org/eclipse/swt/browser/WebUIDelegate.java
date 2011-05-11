/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class WebUIDelegate {
	COMObject iWebUIDelegate;
	int refCount = 0;

	String lastHoveredLinkURL;
	Browser browser;
	Point size;
	Point location;
	boolean menuBar = true, toolBar = true, statusBar = true;
	boolean prompt = true;

WebUIDelegate (Browser browser) {
	createCOMInterfaces ();
	this.browser = browser;
}

int AddRef () {
	refCount++;
	return refCount;
}

int canTakeFocus (int /*long*/ sender, int forward, int /*long*/ result) {
	OS.MoveMemory (result, new int[] {1}, 4); /* BOOL */
	return COM.S_OK;
}

int contextMenuItemsForElement (int /*long*/ sender, int /*long*/ element, int /*long*/ defaultItemsHMenu, int /*long*/ resultHMenu) {
	Point pt = browser.getDisplay ().getCursorLocation ();
	Event event = new Event ();
	event.x = pt.x;
	event.y = pt.y;
	browser.notifyListeners (SWT.MenuDetect, event);
	if (event.doit) {
		Menu menu = browser.getMenu ();
		if (menu != null && !menu.isDisposed ()) {
			if (event.x != pt.x || event.y != pt.y) {
				menu.setLocation (event.x, event.y);
			}
			menu.setVisible (true);
		} else {
			OS.MoveMemory (resultHMenu, new int /*long*/[] {defaultItemsHMenu}, C.PTR_SIZEOF);
			return COM.S_OK;
		}
	}
	OS.MoveMemory (resultHMenu, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return COM.S_OK;
}

void createCOMInterfaces () {
	iWebUIDelegate = new COMObject (new int[] {2, 0, 0, 3, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 4, 4, 2, 3, 4, 4, 3, 3, 3, 3, 5, 3, 1, 3, 2, 2, 2, 2, 3, 2, 3, 1, 1, 0, 0, 1, 1, 2, 2, 2, 2, 3, 5, 2, 2, 3, 1, 2, 2, 4, 10, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args)  {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args)  {return createWebViewWithRequest (args[0], args[1], args[2]);}
		public int /*long*/ method4 (int /*long*/[] args)  {return webViewShow (args[0]);}
		public int /*long*/ method5 (int /*long*/[] args)  {return webViewClose (args[0]);}
		public int /*long*/ method6 (int /*long*/[] args)  {return COM.E_NOTIMPL;}
		public int /*long*/ method7 (int /*long*/[] args)  {return COM.E_NOTIMPL;}
		public int /*long*/ method8 (int /*long*/[] args)  {return COM.E_NOTIMPL;}
		public int /*long*/ method9 (int /*long*/[] args)  {return COM.E_NOTIMPL;}
		public int /*long*/ method10 (int /*long*/[] args) {return setStatusText (args[0], args[1]);}
		public int /*long*/ method11 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method12 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method13 (int /*long*/[] args) {return setToolbarsVisible (args[0], (int)/*64*/args[1]);}
		public int /*long*/ method14 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method15 (int /*long*/[] args) {return setStatusBarVisible (args[0], (int)/*64*/args[1]);}
		public int /*long*/ method16 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method17 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method18 (int /*long*/[] args) {return setFrame (args[0], args[1]);}
		public int /*long*/ method19 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method20 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method21 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method22 (int /*long*/[] args) {return runJavaScriptAlertPanelWithMessage (args[0], args[1]);}
		public int /*long*/ method23 (int /*long*/[] args) {return runJavaScriptConfirmPanelWithMessage (args[0], args[1], args[2]);}
		public int /*long*/ method24 (int /*long*/[] args) {return runJavaScriptTextInputPanelWithPrompt (args[0], args[1], args[2], args[3]);}
		public int /*long*/ method25 (int /*long*/[] args) {return runBeforeUnloadConfirmPanelWithMessage (args[0], args[1], args[2], args[3]);}
		public int /*long*/ method26 (int /*long*/[] args) {return runOpenPanelForFileButtonWithResultListener (args[0], args[1]);}
		public int /*long*/ method27 (int /*long*/[] args) {return mouseDidMoveOverElement (args[0], args[1], (int)/*64*/args[2]);}
		public int /*long*/ method28 (int /*long*/[] args) {return contextMenuItemsForElement (args[0], args[1], args[2], args[3]);}
		public int /*long*/ method29 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method30 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method31 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method32 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method33 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method34 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method35 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method36 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method37 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method38 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method39 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method40 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method41 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method42 (int /*long*/[] args) {return canTakeFocus (args[0], (int)/*64*/args[1], args[2]);}
		public int /*long*/ method43 (int /*long*/[] args) {return takeFocus (args[0], (int)/*64*/args[1]);}
		public int /*long*/ method44 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method45 (int /*long*/[] args) {return COM.S_OK;}
		public int /*long*/ method46 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method47 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method48 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method49 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method50 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method51 (int /*long*/[] args) {return printFrame (args[0], args[1]);}
		public int /*long*/ method52 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method53 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method54 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method55 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method56 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method57 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method58 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method59 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method60 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method61 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method62 (int /*long*/[] args) {return setMenuBarVisible (args[0], (int)/*64*/args[1]);}
		public int /*long*/ method63 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method64 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method65 (int /*long*/[] args) {return COM.E_NOTIMPL;}
	};
}

int createWebViewWithRequest (int /*long*/ sender, int /*long*/ request, int /*long*/ webView) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	newEvent.required = true;
	OpenWindowListener[] openWindowListeners = browser.webBrowser.openWindowListeners;
	for (int i = 0; i < openWindowListeners.length; i++) {
		openWindowListeners[i].open (newEvent);
	}
	IWebView iwebview = null;
	Browser browser = null;
	if (newEvent.browser != null && newEvent.browser.webBrowser instanceof WebKit) {
		browser = newEvent.browser;
	}
	if (browser != null && !browser.isDisposed ()) {
		iwebview = ((WebKit)browser.webBrowser).webView;
		OS.MoveMemory (webView, new int /*long*/[] {iwebview.getAddress ()}, OS.PTR_SIZEOF);
		if (request != 0) {
			IWebURLRequest req = new IWebURLRequest (request);
			int /*long*/[] result = new int /*long*/[1];
			int hr = req.URL (result);
			if (hr != COM.S_OK || result[0] == 0) {
				return COM.S_OK;
			}
			String sUrl = WebKit.extractBSTR (result[0]);
			COM.SysFreeString (result[0]);
			if (sUrl.length () != 0) {
				result[0] = 0;
				hr = iwebview.mainFrame (result);
				if (hr != COM.S_OK || result[0] == 0) {
					return COM.S_OK;
				}
				IWebFrame mainFrame = new IWebFrame (result[0]);
				mainFrame.loadRequest (req.getAddress ());
				mainFrame.Release ();
			}
		}
		return COM.S_OK;
	}
	return COM.E_NOTIMPL;
}

protected void disposeCOMInterfaces () {
	if (iWebUIDelegate != null) {
		iWebUIDelegate.dispose ();
		iWebUIDelegate = null;
	}	
}

int /*long*/ getAddress () {
	return iWebUIDelegate.getAddress ();
}

int mouseDidMoveOverElement (int /*long*/ sender, int /*long*/ elementInformation, int modifierFlags) {
	if (elementInformation == 0) return COM.S_OK;

	IPropertyBag info = new IPropertyBag (elementInformation);
	int /*long*/ key = WebKit.createBSTR ("WebElementLinkURLKey"); //$NON-NLS-N$
	int /*long*/ hHeap = OS.GetProcessHeap ();
	int /*long*/ resultPtr = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, VARIANT.sizeof);
	int hr = info.Read (key, resultPtr, null);
	if (hr != COM.S_OK || resultPtr == 0) {
		return COM.S_OK;
	}
	String value = null;
	VARIANT v = new VARIANT ();
	COM.MoveMemory (v, resultPtr, VARIANT.sizeof);
	if (v.vt == COM.VT_BSTR) value = WebKit.extractBSTR (v.lVal);
	OS.HeapFree (hHeap, 0, resultPtr);
	StatusTextListener[] statusTextListeners = browser.webBrowser.statusTextListeners;
	if (value == null || value.length () == 0) {
		/* not currently over a link */
		if (lastHoveredLinkURL == null) return COM.S_OK;
		lastHoveredLinkURL = null;
		StatusTextEvent statusText = new StatusTextEvent (browser);
		statusText.display = browser.getDisplay ();
		statusText.widget = browser;
		statusText.text = "";	//$NON-NLS-1$
		for (int i = 0; i < statusTextListeners.length; i++) {
			statusTextListeners[i].changed (statusText);
		}
		return COM.S_OK;
	}
	if (value.equals (lastHoveredLinkURL)) return COM.S_OK;
	lastHoveredLinkURL = value;
	StatusTextEvent statusText = new StatusTextEvent (browser);
	statusText.display = browser.getDisplay ();
	statusText.widget = browser;
	statusText.text = value;
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed (statusText);
	}
	return COM.S_OK;
}

int printFrame (int /*long*/ webView, int /*long*/ frame) {
	IWebFrame iwebFrame = new IWebFrame (frame);
	PRINTDLG pd = new PRINTDLG ();
	pd.lStructSize = PRINTDLG.sizeof;
	pd.Flags = OS.PD_RETURNDC;
	if (!OS.PrintDlg (pd)) return COM.S_OK;
	int /*long*/ printDC = pd.hDC;

	int /*long*/[] result = new int /*long*/[1];
	int hr = iwebFrame.QueryInterface (WebKit_win32.IID_IWebFramePrivate, result);
	if (hr != COM.S_OK || result[0] == 0) {
		return COM.S_OK;
	}
	IWebFramePrivate privateFrame = new IWebFramePrivate (result[0]);
	privateFrame.setInPrintingMode (1, printDC);
	int[] count = new int[1];
	hr = privateFrame.getPrintedPageCount (printDC, count);
	if (hr != COM.S_OK || count[0] == 0) {
		privateFrame.Release ();
		return COM.S_OK;
	}
	int pageCount = count[0];
	String jobName = null;
	result[0] = 0;
	hr = iwebFrame.dataSource (result);
	if (hr == COM.S_OK && result[0] != 0) {
		IWebDataSource dataSource = new IWebDataSource (result[0]);
		result[0] = 0;
		hr = dataSource.pageTitle (result);
		dataSource.Release ();
		if (hr == COM.S_OK && result[0] != 0) {
			jobName = WebKit.extractBSTR (result[0]);
			COM.SysFreeString (result[0]);
		}
	}
	DOCINFO di = new DOCINFO ();
	di.cbSize = DOCINFO.sizeof;
	int /*long*/ hHeap = OS.GetProcessHeap ();
	int /*long*/ lpszDocName = 0;
	if (jobName != null && jobName.length () != 0) {
		/* Use the character encoding for the default locale */
		TCHAR buffer = new TCHAR (0, jobName, true);
		int byteCount = buffer.length () * TCHAR.sizeof;
		lpszDocName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpszDocName, buffer, byteCount);
		di.lpszDocName = lpszDocName;
	}
	int rc = OS.StartDoc (printDC, di);
	if (lpszDocName != 0) OS.HeapFree (hHeap, 0, lpszDocName);
	if (rc >= 0) {
		for (int i = 0; i < pageCount; i++) {
			OS.StartPage (printDC);
			privateFrame.spoolPages (printDC, i, i, null);
			OS.EndPage (printDC);
		}
		privateFrame.setInPrintingMode (0, printDC);
		OS.EndDoc (printDC);
	}
	privateFrame.Release ();
	return COM.S_OK;
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID ();
	COM.MoveMemory (guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID (guid, COM.IIDIUnknown)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebUIDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebUIDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID (guid, WebKit_win32.IID_IWebUIDelegate)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebUIDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebUIDelegate.getAddress ()).AddRef ();
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

int runBeforeUnloadConfirmPanelWithMessage (int /*long*/ sender, int /*long*/ message, int /*long*/ initiatedByFrame, int /*long*/ result) {
	if (!prompt) return COM.S_OK;

	Shell parent = browser.getShell ();
	String string = WebKit.extractBSTR (message);
	StringBuffer text = new StringBuffer (Compatibility.getMessage ("SWT_OnBeforeUnload_Message1")); //$NON-NLS-1$
	text.append ("\n\n"); //$NON-NLS-1$
	text.append (string);
	text.append ("\n\n"); //$NON-NLS-1$
	text.append (Compatibility.getMessage ("SWT_OnBeforeUnload_Message2")); //$NON-NLS-1$
	MessageBox box = new MessageBox (parent, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
	box.setMessage (text.toString ());
	int[] response = new int[1];
	response[0] = box.open () == SWT.OK ? 1 : 0;
	OS.MoveMemory (result, response, 4); /* BOOL */
	return COM.S_OK;
}

int runJavaScriptAlertPanelWithMessage (int /*long*/ sender, int /*long*/ message) {
	String messageString = WebKit.extractBSTR (message);
	showAlertMessage ("Javascript", messageString);	//$NON-NLS-1$
	return COM.S_OK;
}

int runJavaScriptConfirmPanelWithMessage (int /*long*/ sender, int /*long*/ message, int /*long*/ result) {
	String messageString = WebKit.extractBSTR (message);
	int[] response = new int[1];
	response[0] = showConfirmPanel ("Javascript", messageString) == SWT.OK ? 1 : 0;	//$NON-NLS-1$
	OS.MoveMemory (result, response, 4); /* BOOL */
	return COM.S_OK;
}

int runJavaScriptTextInputPanelWithPrompt (int /*long*/ sender, int /*long*/ message, int /*long*/ defaultText, int /*long*/ result) {
	String messageString = WebKit.extractBSTR (message);
	String defaultTextString = WebKit.extractBSTR (defaultText);
	String resultString = showTextPrompter ("Javascript", messageString, defaultTextString); //$NON-NLS-1$
	int /*long*/[] response = new int /*long*/[1];
	if (resultString != null) {
		response[0] = WebKit.createBSTR (resultString);
	}
	OS.MoveMemory (result, response, C.PTR_SIZEOF);
	return COM.S_OK;
}

int runOpenPanelForFileButtonWithResultListener (int /*long*/ sender, int /*long*/ resultListener) {
	Shell parent = browser.getShell ();
	FileDialog dialog = new FileDialog (parent, SWT.NONE);
	String result = dialog.open ();
	IWebOpenPanelResultListener listener = new IWebOpenPanelResultListener (resultListener);
	if (result == null) {
		listener.cancel ();
	} else {
		listener.chooseFilename (WebKit.createBSTR (result));
	}
	return COM.S_OK;
}

int setFrame (int /*long*/ sender, int /*long*/ frame) {
	RECT rect = new RECT ();
	COM.MoveMemory (rect, frame, RECT.sizeof);
	/* convert to SWT system coordinates */
	location = browser.getDisplay ().map (browser, null, rect.left, rect.top);
	int x = rect.right - rect.left;
	int y = rect.bottom - rect.top;
	if (y < 0 || x < 0 || (x == 0 && y == 0)) return COM.S_OK;
	size = new Point (x, y);
	return COM.S_OK;
}

int setMenuBarVisible (int /*long*/ sender, int visible) {
	/* Note.  Webkit only emits the notification when the status bar should be hidden. */
	menuBar = visible == 1;
	return COM.S_OK;
}

int setStatusBarVisible (int /*long*/ sender, int visible) {
	/* Note.  Webkit only emits the notification when the status bar should be hidden. */
	statusBar = visible == 1;
	return COM.S_OK;
}

int setStatusText (int /*long*/ sender, int /*long*/ text) {
	String statusText = WebKit.extractBSTR (text);
	if (statusText.length () == 0) return COM.S_OK;
	StatusTextEvent statusTextEvent = new StatusTextEvent (browser);
	statusTextEvent.display = browser.getDisplay ();
	statusTextEvent.widget = browser;
	statusTextEvent.text = statusText;
	StatusTextListener[] statusTextListeners = browser.webBrowser.statusTextListeners;
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed (statusTextEvent);
	}
	return COM.S_OK;
}

int setToolbarsVisible (int /*long*/ sender, int visible) {
	/* Note.  Webkit only emits the notification when the status bar should be hidden. */
	toolBar = visible == 1;
	return COM.S_OK;
}

void showAlertMessage (String title, String message) {
	Shell parent = browser.getShell ();
	final Shell dialog = new Shell (parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	GridLayout layout = new GridLayout (2, false);
	layout.horizontalSpacing = 10;
	layout.verticalSpacing = 20;
	layout.marginWidth = layout.marginHeight = 10;
	dialog.setLayout (layout);
	dialog.setText (title);

	Label label = new Label (dialog, SWT.NONE);
	Image image = dialog.getDisplay ().getSystemImage (SWT.ICON_WARNING);
	label.setImage (image);

	label = new Label (dialog, SWT.WRAP);
	label.setText (message);
	Monitor monitor = parent.getMonitor ();
	int maxWidth = monitor.getBounds().width * 2 / 3;
	int width = label.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	GridData data = new GridData (SWT.FILL, SWT.CENTER, true, false);
	data.widthHint = Math.min (width, maxWidth);
	label.setLayoutData (data);

	Button ok = new Button (dialog, SWT.PUSH);
	ok.setText (SWT.getMessage ("SWT_OK")); //$NON-NLS-1$
	width = ok.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	GridData layoutData = new GridData ();
	layoutData.horizontalAlignment = SWT.CENTER;
	layoutData.verticalAlignment = SWT.CENTER;
	layoutData.horizontalSpan = 2;
	layoutData.widthHint = Math.max (width, 75);
	ok.setLayoutData (layoutData);

	ok.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			dialog.dispose ();
		}
	});
	
	dialog.setDefaultButton (ok);
	dialog.pack ();
	Rectangle parentSize = parent.getBounds ();
	Rectangle dialogSize = dialog.getBounds ();
	int x = parent.getLocation ().x + (parentSize.width - dialogSize.width) / 2;
	int y = parent.getLocation ().y + (parentSize.height - dialogSize.height) / 2;
	dialog.setLocation (x, y);
	dialog.open ();
	Display display = browser.getDisplay ();
	while (!dialog.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
}

int showConfirmPanel (String title, String message) {
	Shell parent = browser.getShell ();
	final Shell dialog = new Shell (parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	GridLayout layout = new GridLayout (2, false);
	layout.horizontalSpacing = 10;
	layout.verticalSpacing = 20;
	layout.marginWidth = layout.marginHeight = 10;
	dialog.setLayout (layout);
	dialog.setText (title);

	Label label = new Label (dialog, SWT.NONE);
	Image image = dialog.getDisplay ().getSystemImage (SWT.ICON_QUESTION);
	label.setImage (image);
	label.setLayoutData (new GridData ());

	label = new Label (dialog, SWT.WRAP);
	label.setText (message);
	Monitor monitor = parent.getMonitor ();
	int maxWidth = monitor.getBounds ().width * 2 / 3;
	int width = label.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	GridData data = new GridData (SWT.FILL, SWT.CENTER, true, false);
	data.widthHint = Math.min (width, maxWidth);
	label.setLayoutData (data);

	Composite buttons = new Composite (dialog, SWT.NONE);
	data = new GridData (SWT.CENTER, SWT.CENTER, true, true, 2, 1);
	buttons.setLayoutData (data);
	buttons.setLayout (new GridLayout (2, true));

	Button ok = new Button (buttons, SWT.PUSH);
	ok.setText (SWT.getMessage ("SWT_OK")); //$NON-NLS-1$
	GridData layoutData = new GridData ();
	layoutData.horizontalAlignment = SWT.CENTER;
	layoutData.verticalAlignment = SWT.CENTER;
	ok.setLayoutData (layoutData);

	Button cancel = new Button (buttons, SWT.PUSH);
	cancel.setText (SWT.getMessage ("SWT_Cancel")); //$NON-NLS-1$
	cancel.setLayoutData (layoutData);
	width = cancel.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	layoutData.widthHint = Math.max (width, 75);

	final int[] result = new int[1];
	ok.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			result[0] = SWT.OK;
			dialog.dispose ();
		}
	});
	cancel.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			result[0] = SWT.CANCEL;
			dialog.dispose ();
		}
	});

	dialog.setDefaultButton (ok);
	dialog.pack ();
	Rectangle parentSize = parent.getBounds ();
	Rectangle dialogSize = dialog.getBounds ();
	int x = parent.getLocation ().x + (parentSize.width - dialogSize.width) / 2;
	int y = parent.getLocation ().y + (parentSize.height - dialogSize.height) / 2;
	dialog.setLocation (x, y);
	dialog.open ();
	Display display = browser.getDisplay ();
	while (!dialog.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	return result[0];
}

String showTextPrompter (String title, String message, String defaultText) {
	Shell parent = browser.getShell ();
	final Shell dialog = new Shell (parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	dialog.setLayout (new GridLayout ());
	dialog.setText (title);

	Label label = new Label (dialog, SWT.NONE);
	label.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	label.setText (message);

	final Text textBox = new Text (dialog, SWT.SINGLE | SWT.BORDER);
	GridData data = new GridData (GridData.FILL_HORIZONTAL);
	data.widthHint = 300;
	textBox.setLayoutData (data);
	textBox.setText (defaultText);

	Composite buttons = new Composite (dialog, SWT.NONE);
	buttons.setLayout (new GridLayout (2, true));
	buttons.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_CENTER));
	Button ok = new Button (buttons, SWT.PUSH);
	ok.setText (SWT.getMessage ("SWT_OK")); //$NON-NLS-1$
	ok.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));

	final String[] result = new String[1];
	ok.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			result[0] = textBox.getText ();
			dialog.dispose ();
		}
	});
	Button cancel = new Button (buttons, SWT.PUSH);
	cancel.setText (SWT.getMessage ("SWT_Cancel")); //$NON-NLS-1$
	cancel.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	cancel.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			dialog.dispose ();
		}
	});

	dialog.setDefaultButton (ok);
	dialog.pack ();
	Rectangle parentSize = parent.getBounds ();
	Rectangle dialogSize = dialog.getBounds ();
	int x = parent.getLocation ().x + (parentSize.width - dialogSize.width) / 2;
	int y = parent.getLocation ().y + (parentSize.height - dialogSize.height) / 2;
	dialog.setLocation (x, y);
	dialog.open ();
	Display display = browser.getDisplay ();
	while (!dialog.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	return result[0];
}

int takeFocus (int /*long*/ sender, int forward) {
	int traveralCode = forward == 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
	((WebKit)browser.webBrowser).traverseOut = true;
	browser.traverse (traveralCode);
	return COM.S_OK;
}

int webViewClose (int /*long*/ sender) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	CloseWindowListener[] closeWindowListeners = browser.webBrowser.closeWindowListeners;
	for (int i = 0; i < closeWindowListeners.length; i++) {
		closeWindowListeners[i].close (newEvent);
	}
	browser.dispose ();
	return COM.S_OK;
}

int webViewFrame (int /*long*/ sender, int /*long*/ frame) {
	RECT rect = new RECT ();
	OS.MoveMemory (frame, rect, RECT.sizeof);
	return COM.S_OK;
}

int webViewShow (int /*long*/ sender) {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	if (location != null) newEvent.location = location;
	if (size != null) newEvent.size = size;
	/*
	* Feature in WebKit.  WebKit's tool bar contains
	* the address bar.  The address bar is displayed
	* if the tool bar is displayed. There is no separate
	* notification for the address bar.
	*/
	newEvent.addressBar = toolBar;
	newEvent.menuBar = menuBar;
	newEvent.statusBar = statusBar;
	newEvent.toolBar = toolBar;
	VisibilityWindowListener[] visibilityWindowListeners = browser.webBrowser.visibilityWindowListeners;
	for (int i = 0; i < visibilityWindowListeners.length; i++) {
		visibilityWindowListeners[i].show (newEvent);
	}
	location = null;
	size = null;
	return COM.S_OK;
}

}

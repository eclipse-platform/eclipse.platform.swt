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


import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.webkit.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class WebDownloadDelegate {
	COMObject iWebDownloadDelegate;

	Browser browser;
	int refCount = 0;
	int status = -1;
	long size, totalSize;
	String url;

	static final int DOWNLOAD_FINISHED = 0;
	static final int DOWNLOAD_CANCELLED = 1;
	static final int DOWNLOAD_ERROR = 3;

WebDownloadDelegate (Browser browser) {
	createCOMInterfaces ();
	this.browser = browser;
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	iWebDownloadDelegate = new COMObject (new int[] {2, 0, 0, 2, 2, 2, 2, 2, 2, 2, 3, 3, 4, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return decideDestinationWithSuggestedFilename (args[0], args[1]);}
		public int /*long*/ method4 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method5 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method6 (int /*long*/[] args) {return didFailWithError (args[0], args[1]);}
		public int /*long*/ method7 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method8 (int /*long*/[] args) {return didReceiveDataOfLength (args[0], (int)/*64*/args[1]);}
		public int /*long*/ method9 (int /*long*/[] args) {return didReceiveResponse (args[0], args[1]);}
		public int /*long*/ method10 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method11 (int /*long*/[] args) {return COM.E_NOTIMPL;}
		public int /*long*/ method12 (int /*long*/[] args) {return willSendRequest (args[0], args[1], args[2], args[3]);}
		public int /*long*/ method13 (int /*long*/[] args) {return didBegin (args[0]);}
		public int /*long*/ method14 (int /*long*/[] args) {return didFinish (args[0]);}
	};
}

int decideDestinationWithSuggestedFilename (int /*long*/ download, int /*long*/ filename) {
	String name = WebKit.extractBSTR (filename);
	FileDialog dialog = new FileDialog (browser.getShell(), SWT.SAVE);
	dialog.setText (SWT.getMessage ("SWT_FileDownload")); //$NON-NLS-1$
	dialog.setFileName (name);
	dialog.setOverwrite (true);
	String path = dialog.open ();
	IWebDownload iwebdownload = new IWebDownload (download);
	iwebdownload.setDeletesFileUponFailure (0);
	if (path == null) {
		/*
		* Bug in WebKit.  Failure to set a non-null destination on the IWebDownload results in
		* a crash, even when the download is being cancelled.
		*/
		iwebdownload.setDestination (WebKit.createBSTR (""), 1); //$NON-NLS-1$
		iwebdownload.cancel();
		iwebdownload.Release();
	} else {
		File file = new File (path);
		if (file.exists ()) file.delete ();
		iwebdownload.setDestination (WebKit.createBSTR (path), 1);
		openDownloadWindow (iwebdownload, path);
	}
	return COM.S_OK;
}

int didBegin (int /*long*/ download) {
	new IWebDownload (download).AddRef ();
	status = -1;
	size = 0;
	totalSize = 0;
	url = null;
	return COM.S_OK;
}

int didFailWithError (int /*long*/ download, int /*long*/ error) {
	new IWebDownload (download).Release ();
	status = DOWNLOAD_ERROR;
	return COM.S_OK;
}

int didFinish (int /*long*/ download) {
	new IWebDownload (download).Release ();
	status = DOWNLOAD_FINISHED;
	return COM.S_OK;
}

int didReceiveDataOfLength (int /*long*/ download, int length) {
	 size += length;
	 return COM.S_OK;
}

int didReceiveResponse (int /*long*/ download, int /*long*/ response) {
	if (response != 0) {
		IWebURLResponse urlResponse = new IWebURLResponse (response);
		long[] size = new long[1];
		int hr = urlResponse.expectedContentLength (size);
		if (hr == COM.S_OK) totalSize = size[0];
		int /*long*/[] result = new int /*long*/[1];
		hr = urlResponse.URL (result);
		if (hr == COM.S_OK && result[0] != 0) {
			url = WebKit.extractBSTR (result[0]);
			COM.SysFreeString (result[0]);
		}
	}
	return COM.S_OK;
}

void disposeCOMInterfaces () {
	if (iWebDownloadDelegate != null) {
		iWebDownloadDelegate.dispose ();
		iWebDownloadDelegate = null;
	}	
}

int /*long*/ getAddress () {
	return iWebDownloadDelegate.getAddress ();
}

void openDownloadWindow (final IWebDownload download, String name) {
	final Shell shell = new Shell ();
	shell.setText (Compatibility.getMessage ("SWT_FileDownload"));	//$NON-NLS-1$
	GridLayout gridLayout = new GridLayout ();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout (gridLayout);

	Label nameLabel = new Label (shell, SWT.WRAP);
	nameLabel.setText (Compatibility.getMessage ("SWT_Download_Location", new Object[] {name, url})); //$NON-NLS-1$
	GridData data = new GridData ();
	Monitor monitor = browser.getMonitor ();
	int maxWidth = monitor.getBounds ().width / 2;
	int width = nameLabel.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
	data.widthHint = Math.min (width, maxWidth);
	data.horizontalAlignment = GridData.FILL;
	data.grabExcessHorizontalSpace = true;
	nameLabel.setLayoutData (data);

	final Label statusLabel = new Label (shell, SWT.NONE);
	statusLabel.setText (Compatibility.getMessage ("SWT_Download_Started")); //$NON-NLS-1$
	data = new GridData (GridData.FILL_BOTH);
	statusLabel.setLayoutData (data);

	final Button cancel = new Button (shell, SWT.PUSH);
	cancel.setText (Compatibility.getMessage ("SWT_Cancel")); //$NON-NLS-1$
	data = new GridData ();
	data.horizontalAlignment = GridData.CENTER;
	cancel.setLayoutData (data);
	final Listener cancelListener = new Listener () {
		public void handleEvent (Event event) {
			download.cancel ();
			status = DOWNLOAD_CANCELLED;
			download.Release ();
		}
	};
	cancel.addListener (SWT.Selection, cancelListener);

	final Display display = browser.getDisplay ();
	final int INTERVAL = 500;
	display.timerExec (INTERVAL, new Runnable () {
		public void run () {
			if (shell.isDisposed () || status == DOWNLOAD_FINISHED || status == DOWNLOAD_CANCELLED) {
				shell.dispose ();
				return;
			}
			if (status == DOWNLOAD_ERROR) {
				statusLabel.setText (Compatibility.getMessage ("SWT_Download_Error")); //$NON-NLS-1$
				cancel.removeListener (SWT.Selection, cancelListener);
				cancel.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event event) {
						shell.dispose ();
					}
				});
				return;
			}
			long current = size / 1024L;
			long total = totalSize / 1024L;
			String message = Compatibility.getMessage ("SWT_Download_Status", new Object[] {new Long(current), new Long(total)}); //$NON-NLS-1$
			statusLabel.setText (message);
			display.timerExec (INTERVAL, this);
		}
	});
	shell.pack ();
	shell.open ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID ();
	COM.MoveMemory (guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID (guid, COM.IIDIUnknown)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebDownloadDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebDownloadDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID (guid, WebKit_win32.IID_IWebDownloadDelegate)) {
		COM.MoveMemory (ppvObject, new int /*long*/[] {iWebDownloadDelegate.getAddress ()}, OS.PTR_SIZEOF);
		new IUnknown (iWebDownloadDelegate.getAddress ()).AddRef ();
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

int willSendRequest (int /*long*/ download, int /*long*/ request, int /*long*/ redirectResponse, int /*long*/ finalRequest) {
	IWebMutableURLRequest req = new IWebMutableURLRequest (request);
	req.AddRef ();
	OS.MoveMemory (finalRequest, new int /*long*/[] {request}, C.PTR_SIZEOF);
	return COM.S_OK;
}

}

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
		@Override
		public long method0 (long[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long method1 (long[] args) {return AddRef ();}
		@Override
		public long method2 (long[] args) {return Release ();}
		@Override
		public long method3 (long[] args) {return decideDestinationWithSuggestedFilename (args[0], args[1]);}
		@Override
		public long method4 (long[] args) {return COM.E_NOTIMPL;}
		@Override
		public long method5 (long[] args) {return COM.E_NOTIMPL;}
		@Override
		public long method6 (long[] args) {return didFailWithError (args[0], args[1]);}
		@Override
		public long method7 (long[] args) {return COM.E_NOTIMPL;}
		@Override
		public long method8 (long[] args) {return didReceiveDataOfLength (args[0], (int)args[1]);}
		@Override
		public long method9 (long[] args) {return didReceiveResponse (args[0], args[1]);}
		@Override
		public long method10 (long[] args) {return COM.E_NOTIMPL;}
		@Override
		public long method11 (long[] args) {return COM.E_NOTIMPL;}
		@Override
		public long method12 (long[] args) {return willSendRequest (args[0], args[1], args[2], args[3]);}
		@Override
		public long method13 (long[] args) {return didBegin (args[0]);}
		@Override
		public long method14 (long[] args) {return didFinish (args[0]);}
	};
}

int decideDestinationWithSuggestedFilename (long download, long filename) {
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

int didBegin (long download) {
	new IWebDownload (download).AddRef ();
	status = -1;
	size = 0;
	totalSize = 0;
	url = null;
	return COM.S_OK;
}

int didFailWithError (long download, long error) {
	new IWebDownload (download).Release ();
	status = DOWNLOAD_ERROR;
	return COM.S_OK;
}

int didFinish (long download) {
	new IWebDownload (download).Release ();
	status = DOWNLOAD_FINISHED;
	return COM.S_OK;
}

int didReceiveDataOfLength (long download, int length) {
	size += length;
	return COM.S_OK;
}

int didReceiveResponse (long download, long response) {
	if (response != 0) {
		IWebURLResponse urlResponse = new IWebURLResponse (response);
		long[] size = new long[1];
		int hr = urlResponse.expectedContentLength (size);
		if (hr == COM.S_OK) totalSize = size[0];
		long[] result = new long[1];
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

long getAddress () {
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
	final Listener cancelListener = event -> {
		download.cancel ();
		status = DOWNLOAD_CANCELLED;
		download.Release ();
	};
	cancel.addListener (SWT.Selection, cancelListener);

	final Display display = browser.getDisplay ();
	final int INTERVAL = 500;
	display.timerExec (INTERVAL, new Runnable () {
		@Override
		public void run () {
			if (shell.isDisposed () || status == DOWNLOAD_FINISHED || status == DOWNLOAD_CANCELLED) {
				shell.dispose ();
				return;
			}
			if (status == DOWNLOAD_ERROR) {
				statusLabel.setText (Compatibility.getMessage ("SWT_Download_Error")); //$NON-NLS-1$
				cancel.removeListener (SWT.Selection, cancelListener);
				cancel.addListener (SWT.Selection, event -> shell.dispose ());
				return;
			}
			long current = size / 1024L;
			long total = totalSize / 1024L;
			String message = Compatibility.getMessage ("SWT_Download_Status", new Object[] {current, total}); //$NON-NLS-1$
			statusLabel.setText (message);
			display.timerExec (INTERVAL, this);
		}
	});
	shell.pack ();
	shell.open ();
}

int QueryInterface (long riid, long ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
	GUID guid = new GUID ();
	COM.MoveMemory (guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID (guid, COM.IIDIUnknown)) {
		OS.MoveMemory (ppvObject, new long[] {iWebDownloadDelegate.getAddress ()}, C.PTR_SIZEOF);
		new IUnknown (iWebDownloadDelegate.getAddress ()).AddRef ();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID (guid, WebKit_win32.IID_IWebDownloadDelegate)) {
		OS.MoveMemory (ppvObject, new long[] {iWebDownloadDelegate.getAddress ()}, C.PTR_SIZEOF);
		new IUnknown (iWebDownloadDelegate.getAddress ()).AddRef ();
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

int willSendRequest (long download, long request, long redirectResponse, long finalRequest) {
	IWebMutableURLRequest req = new IWebMutableURLRequest (request);
	req.AddRef ();
	OS.MoveMemory (finalRequest, new long[] {request}, C.PTR_SIZEOF);
	return COM.S_OK;
}

}

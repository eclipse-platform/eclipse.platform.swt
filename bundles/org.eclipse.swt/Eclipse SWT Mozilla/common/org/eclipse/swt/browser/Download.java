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

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This class implements the nsIDownload interface for mozilla
 * versions 1.4 - 1.7.x.  For mozilla version 1.8.x this interface
 * is implemented by class nsIDownload_1_8.  Later versions of
 * mozilla do not need to call this interface. 
 */
class Download {
	XPCOMObject supports;
	XPCOMObject download;
	XPCOMObject progressDialog;
	XPCOMObject webProgressListener;
	nsIHelperAppLauncher helperAppLauncher;
	int refCount = 0;

	Shell shell;
	Label status;
	Button cancel;
	
Download () {
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
	
	download = new XPCOMObject (new int[] {2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return Init (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public long /*int*/ method4 (long /*int*/[] args) {return GetSource (args[0]);}
		public long /*int*/ method5 (long /*int*/[] args) {return GetTarget (args[0]);}
		public long /*int*/ method6 (long /*int*/[] args) {return GetPersist (args[0]);}
		public long /*int*/ method7 (long /*int*/[] args) {return GetPercentComplete (args[0]);}
		public long /*int*/ method8 (long /*int*/[] args) {return GetDisplayName (args[0]);}
		public long /*int*/ method9 (long /*int*/[] args) {return SetDisplayName (args[0]);}
		public long /*int*/ method10 (long /*int*/[] args) {return GetStartTime (args[0]);}
		public long /*int*/ method11 (long /*int*/[] args) {return GetMIMEInfo (args[0]);}
		public long /*int*/ method12 (long /*int*/[] args) {return GetListener (args[0]);}
		public long /*int*/ method13 (long /*int*/[] args) {return SetListener (args[0]);}
		public long /*int*/ method14 (long /*int*/[] args) {return GetObserver (args[0]);}
		public long /*int*/ method15 (long /*int*/[] args) {return SetObserver (args[0]);}
	};
	
	progressDialog = new XPCOMObject (new int[] {2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return Init (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);}
		public long /*int*/ method4 (long /*int*/[] args) {return GetSource (args[0]);}
		public long /*int*/ method5 (long /*int*/[] args) {return GetTarget (args[0]);}
		public long /*int*/ method6 (long /*int*/[] args) {return GetPersist (args[0]);}
		public long /*int*/ method7 (long /*int*/[] args) {return GetPercentComplete (args[0]);}
		public long /*int*/ method8 (long /*int*/[] args) {return GetDisplayName (args[0]);}
		public long /*int*/ method9 (long /*int*/[] args) {return SetDisplayName (args[0]);}
		public long /*int*/ method10 (long /*int*/[] args) {return GetStartTime (args[0]);}
		public long /*int*/ method11 (long /*int*/[] args) {return GetMIMEInfo (args[0]);}
		public long /*int*/ method12 (long /*int*/[] args) {return GetListener (args[0]);}
		public long /*int*/ method13 (long /*int*/[] args) {return SetListener (args[0]);}
		public long /*int*/ method14 (long /*int*/[] args) {return GetObserver (args[0]);}
		public long /*int*/ method15 (long /*int*/[] args) {return SetObserver (args[0]);}
		public long /*int*/ method16 (long /*int*/[] args) {return Open (args[0]);}
		public long /*int*/ method17 (long /*int*/[] args) {return GetCancelDownloadOnClose (args[0]);}
		public long /*int*/ method18 (long /*int*/[] args) {return SetCancelDownloadOnClose ((int)/*64*/args[0]);}
		public long /*int*/ method19 (long /*int*/[] args) {return GetDialog (args[0]);}
		public long /*int*/ method20 (long /*int*/[] args) {return SetDialog (args[0]);}
	};
	
	webProgressListener = new XPCOMObject (new int[] {2, 0, 0, 4, 6, 3, 4, 3}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return OnStateChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3]);}
		public long /*int*/ method4 (long /*int*/[] args) {return OnProgressChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5]);}
		public long /*int*/ method5 (long /*int*/[] args) {return OnLocationChange (args[0], args[1], args[2]);}
		public long /*int*/ method6 (long /*int*/[] args) {return OnStatusChange (args[0], args[1], (int)/*64*/args[2], args[3]);}
		public long /*int*/ method7 (long /*int*/[] args) {return OnSecurityChange (args[0], args[1], (int)/*64*/args[2]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (download != null) {
		download.dispose ();
		download = null;	
	}
	if (progressDialog != null) {
		progressDialog.dispose ();
		progressDialog = null;
	}
	if (webProgressListener != null) {
		webProgressListener.dispose ();
		webProgressListener = null;
	}
}

long /*int*/ getAddress () {
	return progressDialog.getAddress ();
}

int QueryInterface (long /*int*/ riid, long /*int*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIDownload.NS_IDOWNLOAD_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {download.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIProgressDialog.NS_IPROGRESSDIALOG_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {progressDialog.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {webProgressListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
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

/* nsIDownload */

/* Note. The argument startTime is defined as a PRInt64. This translates into two java ints. */
int Init (long /*int*/ aSource, long /*int*/ aTarget, long /*int*/ aDisplayName, long /*int*/ aMIMEInfo, long /*int*/ startTime1, long /*int*/ startTime2, long /*int*/ aPersist) {
	nsIURI source = new nsIURI (aSource);
	long /*int*/ aSpec = XPCOM.nsEmbedCString_new ();
	int rc = source.GetHost (aSpec);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	int length = XPCOM.nsEmbedCString_Length (aSpec);
	long /*int*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove (dest, buffer, length);
	XPCOM.nsEmbedCString_delete (aSpec);
	String url = new String (dest);

	/*
	* As of mozilla 1.7 the second argument of the nsIDownload interface's 
	* Init function changed from nsILocalFile to nsIURI.  Detect which of
	* these interfaces the second argument implements and act accordingly.  
	*/
	String filename = null;
	nsISupports supports = new nsISupports (aTarget);
	long /*int*/[] result = new long /*int*/[1];
	rc = supports.QueryInterface (!Mozilla.IsPre_4 ? nsIURI.NS_IURI_10_IID : nsIURI.NS_IURI_IID, result);
	if (rc == XPCOM.NS_OK) {	/* >= 1.7 */
		nsIURI target = new nsIURI (result[0]);
		result[0] = 0;
		long /*int*/ aPath = XPCOM.nsEmbedCString_new ();
		rc = target.GetPath (aPath);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		length = XPCOM.nsEmbedCString_Length (aPath);
		buffer = XPCOM.nsEmbedCString_get (aPath);
		dest = new byte[length];
		XPCOM.memmove (dest, buffer, length);
		XPCOM.nsEmbedCString_delete (aPath);
		filename = new String (dest);
		int separator = filename.lastIndexOf (System.getProperty ("file.separator"));	//$NON-NLS-1$
		filename = filename.substring (separator + 1);
		target.Release ();
	} else {	/* < 1.7 */
		nsILocalFile target = new nsILocalFile (aTarget);
		long /*int*/ aNativeTarget = XPCOM.nsEmbedString_new ();
		rc = target.GetLeafName (aNativeTarget);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		length = XPCOM.nsEmbedString_Length (aNativeTarget);
		buffer = XPCOM.nsEmbedString_get (aNativeTarget);
		char[] chars = new char[length];
		XPCOM.memmove (chars, buffer, length * 2);
		XPCOM.nsEmbedString_delete (aNativeTarget);
		filename = new String (chars);
	}

	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			if (event.widget == cancel) {
				shell.close ();
			}
			if (helperAppLauncher != null) {
				helperAppLauncher.Cancel ();
				helperAppLauncher.Release ();
			}
			shell = null;
			helperAppLauncher = null;
		}
	};
	shell = new Shell (SWT.DIALOG_TRIM);
	String msg = Compatibility.getMessage ("SWT_Download_File", new Object[] {filename}); //$NON-NLS-1$
	shell.setText (msg);
	GridLayout gridLayout = new GridLayout ();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout(gridLayout);
	msg = Compatibility.getMessage ("SWT_Download_Location", new Object[] {filename, url}); //$NON-NLS-1$
	new Label (shell, SWT.WRAP).setText (msg);
	status = new Label (shell, SWT.WRAP);
	msg = Compatibility.getMessage ("SWT_Download_Started"); //$NON-NLS-1$
	status.setText (msg);
	GridData data = new GridData ();
	data.grabExcessHorizontalSpace = true;
	data.grabExcessVerticalSpace = true;
	status.setLayoutData (data);

	cancel = new Button (shell, SWT.PUSH);
	cancel.setText (SWT.getMessage ("SWT_Cancel")); //$NON-NLS-1$
	data = new GridData ();
	data.horizontalAlignment = GridData.CENTER;
	cancel.setLayoutData (data);
	cancel.addListener (SWT.Selection, listener);
	shell.addListener (SWT.Close, listener);
	shell.pack ();
	shell.open ();
	return XPCOM.NS_OK;
}

int GetSource (long /*int*/ aSource) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetTarget (long /*int*/ aTarget) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetPersist (long /*int*/ aPersist) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetPercentComplete (long /*int*/ aPercentComplete) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDisplayName (long /*int*/ aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDisplayName (long /*int*/ aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetStartTime (long /*int*/ aStartTime) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetMIMEInfo (long /*int*/ aMIMEInfo) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetListener (long /*int*/ aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetListener (long /*int*/ aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetObserver (long /*int*/ aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetObserver (long /*int*/ aObserver) {
	if (aObserver != 0) {
		nsISupports supports = new nsISupports (aObserver);
		long /*int*/[] result = new long /*int*/[1];
		int rc = supports.QueryInterface (nsIHelperAppLauncher.NS_IHELPERAPPLAUNCHER_IID, result);
		if (rc != XPCOM.NS_OK) Mozilla.error (rc);
		if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
		helperAppLauncher = new nsIHelperAppLauncher (result[0]);
	}
	return XPCOM.NS_OK;
}

/* nsIProgressDialog */
int Open (long /*int*/ aParent) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetCancelDownloadOnClose (long /*int*/ aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetCancelDownloadOnClose (int aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDialog (long /*int*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDialog (long /*int*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIWebProgressListener */

int OnStateChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int aStateFlags, int aStatus) {
	if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		if (helperAppLauncher != null) helperAppLauncher.Release ();
		helperAppLauncher = null;
		if (shell != null && !shell.isDisposed ()) shell.dispose ();
		shell = null;
	}
	return XPCOM.NS_OK;
}

int OnProgressChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
	int currentKBytes = aCurTotalProgress / 1024;
	int totalKBytes = aMaxTotalProgress / 1024;
	if (shell != null && !shell.isDisposed ()) {
		Object[] arguments = {new Integer (currentKBytes), new Integer (totalKBytes)};
		String statusMsg = Compatibility.getMessage ("SWT_Download_Status", arguments); //$NON-NLS-1$
		status.setText (statusMsg);
		shell.layout (true);
		shell.getDisplay ().update ();
	}
	return XPCOM.NS_OK;
}

int OnLocationChange (long /*int*/ aWebProgress, long /*int*/ aRequest, long /*int*/ aLocation) {
	return XPCOM.NS_OK;
}

int OnStatusChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int aStatus, long /*int*/ aMessage) {
	return XPCOM.NS_OK;
}

int OnSecurityChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int state) {
	return XPCOM.NS_OK;
}
}

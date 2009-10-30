/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
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
 * version 1.8.x.  For mozilla versions 1.4 - 1.7.x this interface
 * is implemented by class nsIDownload.  Mozilla versions later 
 * than 1.8.x do not need to call this interface. 
 */
class Download_1_8 {
	XPCOMObject supports;
	XPCOMObject download;
	XPCOMObject progressDialog;
	XPCOMObject webProgressListener;
	nsICancelable cancelable;
	int refCount = 0;

	Shell shell;
	Label status;
	Button cancel;

	static final boolean is32 = C.PTR_SIZEOF == 4;

Download_1_8 () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};

	download = new XPCOMObject (new int[] {2, 0, 0, 4, 6, 3, 4, 3, is32 ? 10 : 6, is32 ? 8 : 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return OnStateChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3]);}
		public int /*long*/ method4 (int /*long*/[] args) {return OnProgressChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5]);}
		public int /*long*/ method5 (int /*long*/[] args) {return OnLocationChange (args[0], args[1], args[2]);}
		public int /*long*/ method6 (int /*long*/[] args) {return OnStatusChange (args[0], args[1], (int)/*64*/args[2], args[3]);}
		public int /*long*/ method7 (int /*long*/[] args) {return OnSecurityChange (args[0], args[1], (int)/*64*/args[2]);}
		public int /*long*/ method8 (int /*long*/[] args) {
			if (args.length == 10) {
				return OnProgressChange64_32 (args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
			} else {
				return OnProgressChange64 (args[0], args[1], args[2], args[3], args[4], args[5]);
			}
		}
		public int /*long*/ method9 (int /*long*/[] args) {
			if (args.length == 8) {
				return Init_32 (args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
			} else {
				return Init (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
			}
		}
		public int /*long*/ method10 (int /*long*/[] args) {return GetTargetFile (args[0]);}
		public int /*long*/ method11 (int /*long*/[] args) {return GetPercentComplete (args[0]);}
		public int /*long*/ method12 (int /*long*/[] args) {return GetAmountTransferred (args[0]);}
		public int /*long*/ method13 (int /*long*/[] args) {return GetSize (args[0]);}
		public int /*long*/ method14 (int /*long*/[] args) {return GetSource (args[0]);}
		public int /*long*/ method15 (int /*long*/[] args) {return GetTarget (args[0]);}
		public int /*long*/ method16 (int /*long*/[] args) {return GetCancelable (args[0]);}
		public int /*long*/ method17 (int /*long*/[] args) {return GetDisplayName (args[0]);}
		public int /*long*/ method18 (int /*long*/[] args) {return GetStartTime (args[0]);}
		public int /*long*/ method19 (int /*long*/[] args) {return GetMIMEInfo (args[0]);}
	};

	progressDialog = new XPCOMObject (new int[] {2, 0, 0, 4, 6, 3, 4, 3, is32 ? 10 : 6, is32 ? 8 : 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return OnStateChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3]);}
		public int /*long*/ method4 (int /*long*/[] args) {return OnProgressChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5]);}
		public int /*long*/ method5 (int /*long*/[] args) {return OnLocationChange (args[0], args[1], args[2]);}
		public int /*long*/ method6 (int /*long*/[] args) {return OnStatusChange (args[0], args[1], (int)/*64*/args[2], args[3]);}
		public int /*long*/ method7 (int /*long*/[] args) {return OnSecurityChange (args[0], args[1], (int)/*64*/args[2]);}
		public int /*long*/ method8 (int /*long*/[] args) {
			if (args.length == 10) {
				return OnProgressChange64_32 (args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
			} else {
				return OnProgressChange64 (args[0], args[1], args[2], args[3], args[4], args[5]);
			}
		}
		public int /*long*/ method9 (int /*long*/[] args) {
			if (args.length == 8) {
				return Init_32 (args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
			} else {
				return Init (args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
			}
		}
		public int /*long*/ method10 (int /*long*/[] args) {return GetTargetFile (args[0]);}
		public int /*long*/ method11 (int /*long*/[] args) {return GetPercentComplete (args[0]);}
		public int /*long*/ method12 (int /*long*/[] args) {return GetAmountTransferred (args[0]);}
		public int /*long*/ method13 (int /*long*/[] args) {return GetSize (args[0]);}
		public int /*long*/ method14 (int /*long*/[] args) {return GetSource (args[0]);}
		public int /*long*/ method15 (int /*long*/[] args) {return GetTarget (args[0]);}
		public int /*long*/ method16 (int /*long*/[] args) {return GetCancelable (args[0]);}
		public int /*long*/ method17 (int /*long*/[] args) {return GetDisplayName (args[0]);}
		public int /*long*/ method18 (int /*long*/[] args) {return GetStartTime (args[0]);}
		public int /*long*/ method19 (int /*long*/[] args) {return GetMIMEInfo (args[0]);}
		public int /*long*/ method20 (int /*long*/[] args) {return Open (args[0]);}
		public int /*long*/ method21 (int /*long*/[] args) {return GetCancelDownloadOnClose (args[0]);}
		public int /*long*/ method22 (int /*long*/[] args) {return SetCancelDownloadOnClose ((int)/*64*/args[0]);}
		public int /*long*/ method23 (int /*long*/[] args) {return GetObserver (args[0]);}
		public int /*long*/ method24 (int /*long*/[] args) {return SetObserver (args[0]);}
		public int /*long*/ method25 (int /*long*/[] args) {return GetDialog (args[0]);}
		public int /*long*/ method26 (int /*long*/[] args) {return SetDialog (args[0]);}
	};

	webProgressListener = new XPCOMObject (new int[] {2, 0, 0, 4, 6, 3, 4, 3}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return OnStateChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3]);}
		public int /*long*/ method4 (int /*long*/[] args) {return OnProgressChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5]);}
		public int /*long*/ method5 (int /*long*/[] args) {return OnLocationChange (args[0], args[1], args[2]);}
		public int /*long*/ method6 (int /*long*/[] args) {return OnStatusChange (args[0], args[1], (int)/*64*/args[2], args[3]);}
		public int /*long*/ method7 (int /*long*/[] args) {return OnSecurityChange (args[0], args[1], (int)/*64*/args[2]);}
	};
}

void disposeCOMInterfaces() {
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

int /*long*/ getAddress () {
	return progressDialog.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIDownload_1_8.NS_IDOWNLOAD_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {download.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIProgressDialog_1_8.NS_IPROGRESSDIALOG_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {progressDialog.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {webProgressListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

/* nsIDownload */

/* Note. The argument startTime is defined as a PRInt64. This translates into two java ints. */
int Init_32 (int /*long*/ aSource, int /*long*/ aTarget, int /*long*/ aDisplayName, int /*long*/ aMIMEInfo, int /*long*/ startTime1, int /*long*/ startTime2, int /*long*/ aTempFile, int /*long*/ aCancelable) {
	long startTime = (startTime2 << 32) + startTime1;
	return Init (aSource, aTarget, aDisplayName, aMIMEInfo, startTime, aTempFile, aCancelable);
}

int Init (int /*long*/ aSource, int /*long*/ aTarget, int /*long*/ aDisplayName, int /*long*/ aMIMEInfo, long startTime, int /*long*/ aTempFile, int /*long*/ aCancelable) {
	cancelable = new nsICancelable (aCancelable);
	nsIURI source = new nsIURI (aSource);
	int /*long*/ aSpec = XPCOM.nsEmbedCString_new ();
	int rc = source.GetHost (aSpec);
	if (rc != XPCOM.NS_OK) Mozilla.error(rc);
	int length = XPCOM.nsEmbedCString_Length (aSpec);
	int /*long*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove (dest, buffer, length);
	XPCOM.nsEmbedCString_delete (aSpec);
	String url = new String (dest);

	nsIURI target = new nsIURI (aTarget);
	int /*long*/ aPath = XPCOM.nsEmbedCString_new ();
	rc = target.GetPath (aPath);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	length = XPCOM.nsEmbedCString_Length (aPath);
	buffer = XPCOM.nsEmbedCString_get (aPath);
	dest = new byte[length];
	XPCOM.memmove (dest, buffer, length);
	XPCOM.nsEmbedCString_delete (aPath);
	String filename = new String (dest);
	int separator = filename.lastIndexOf (System.getProperty ("file.separator"));	//$NON-NLS-1$
	filename = filename.substring (separator + 1);

	Listener listener = new Listener() {
		public void handleEvent (Event event) {
			if (event.widget == cancel) {
				shell.close ();
			}
			if (cancelable != null) {
				int rc = cancelable.Cancel (XPCOM.NS_BINDING_ABORTED);
				if (rc != XPCOM.NS_OK) Mozilla.error (rc);
			}
			shell = null;
			cancelable = null;
		}
	};
	shell = new Shell (SWT.DIALOG_TRIM);
	String msg = Compatibility.getMessage ("SWT_Download_File", new Object[] {filename}); //$NON-NLS-1$
	shell.setText (msg);
	GridLayout gridLayout = new GridLayout ();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout (gridLayout);
	msg = Compatibility.getMessage ("SWT_Download_Location", new Object[] {filename, url}); //$NON-NLS-1$
	new Label (shell, SWT.SIMPLE).setText (msg);
	status = new Label (shell, SWT.SIMPLE);
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

int GetAmountTransferred (int /*long*/ arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetCancelable (int /*long*/ arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDisplayName (int /*long*/ aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetMIMEInfo (int /*long*/ aMIMEInfo) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetPercentComplete (int /*long*/ aPercentComplete) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetSize (int /*long*/ arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetSource (int /*long*/ aSource) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetStartTime (int /*long*/ aStartTime) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetTarget (int /*long*/ aTarget) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetTargetFile (int /*long*/ arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIProgressDialog */
int GetCancelDownloadOnClose (int /*long*/ aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDialog (int /*long*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetObserver (int /*long*/ aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int Open (int /*long*/ aParent) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetCancelDownloadOnClose (int aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDialog (int /*long*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetObserver (int /*long*/ aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIWebProgressListener */

int OnLocationChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ aLocation) {
	return XPCOM.NS_OK;
}

int OnProgressChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
	return OnProgressChange64 (aWebProgress, aRequest, aCurSelfProgress, aMaxSelfProgress, aCurTotalProgress, aMaxTotalProgress);
}

/* Note. The last 4 args in the original interface are defined as PRInt64. These each translate into two java ints. */
int OnProgressChange64_32 (int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ aCurSelfProgress1, int /*long*/ aCurSelfProgress2, int /*long*/ aMaxSelfProgress1, int /*long*/ aMaxSelfProgress2, int /*long*/ aCurTotalProgress1, int /*long*/ aCurTotalProgress2, int /*long*/ aMaxTotalProgress1, int /*long*/ aMaxTotalProgress2) {
	long aCurSelfProgress = (aCurSelfProgress2 << 32) + aCurSelfProgress1;
	long aMaxSelfProgress = (aMaxSelfProgress2 << 32) + aMaxSelfProgress1;
	long aCurTotalProgress = (aCurTotalProgress2 << 32) + aCurTotalProgress1;
	long aMaxTotalProgress = (aMaxTotalProgress2 << 32) + aMaxTotalProgress1;
	return OnProgressChange64 (aWebProgress, aRequest, aCurSelfProgress, aMaxSelfProgress, aCurTotalProgress, aMaxTotalProgress);
}

int OnProgressChange64 (int /*long*/ aWebProgress, int /*long*/ aRequest, long aCurSelfProgress, long aMaxSelfProgress, long aCurTotalProgress, long aMaxTotalProgress) {
	long currentKBytes = aCurTotalProgress / 1024;
	long totalKBytes = aMaxTotalProgress / 1024;
	if (shell != null && !shell.isDisposed ()) {
		Object[] arguments = {new Long (currentKBytes), new Long (totalKBytes)};
		String statusMsg = Compatibility.getMessage ("SWT_Download_Status", arguments); //$NON-NLS-1$
		status.setText (statusMsg);
		shell.layout (true);
		shell.getDisplay ().update ();
	}
	return XPCOM.NS_OK;
}

int OnSecurityChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int state) {
	return XPCOM.NS_OK;
}

int OnStateChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int aStateFlags, int aStatus) {
	if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		cancelable = null;
		if (shell != null && !shell.isDisposed ()) shell.dispose ();
		shell = null;
	}
	return XPCOM.NS_OK;
}	

int OnStatusChange (int /*long*/ aWebProgress, int /*long*/ aRequest, int aStatus, int /*long*/ aMessage) {
	return XPCOM.NS_OK;
}		
}

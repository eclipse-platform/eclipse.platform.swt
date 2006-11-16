/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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

public Download_1_8() {
	createCOMInterfaces();
}

int AddRef() {
	refCount++;
	return refCount;
}

void createCOMInterfaces() {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};

	download = new XPCOMObject(new int[]{2, 0, 0, 4, 6, 3, 4, 3, 10, 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return OnStateChange(args[0], args[1], args[2], args[3]);}
		public int method4(int[] args) {return OnProgressChange(args[0], args[1], args[2], args[3], args[4], args[5]);}
		public int method5(int[] args) {return OnLocationChange(args[0], args[1], args[2]);}
		public int method6(int[] args) {return OnStatusChange(args[0], args[1], args[2], args[3]);}
		public int method7(int[] args) {return OnSecurityChange(args[0], args[1], args[2]);}
		public int method8(int[] args) {return OnProgressChange64(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);}
		public int method9(int[] args) {return Init(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);}
		public int method10(int[] args) {return GetTargetFile(args[0]);}
		public int method11(int[] args) {return GetPercentComplete(args[0]);}
		public int method12(int[] args) {return GetAmountTransferred(args[0]);}
		public int method13(int[] args) {return GetSize(args[0]);}
		public int method14(int[] args) {return GetSource(args[0]);}
		public int method15(int[] args) {return GetTarget(args[0]);}
		public int method16(int[] args) {return GetCancelable(args[0]);}
		public int method17(int[] args) {return GetDisplayName(args[0]);}
		public int method18(int[] args) {return GetStartTime(args[0]);}
		public int method19(int[] args) {return GetMIMEInfo(args[0]);}
	};

	progressDialog = new XPCOMObject(new int[]{2, 0, 0, 4, 6, 3, 4, 3, 10, 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return OnStateChange(args[0], args[1], args[2], args[3]);}
		public int method4(int[] args) {return OnProgressChange(args[0], args[1], args[2], args[3], args[4], args[5]);}
		public int method5(int[] args) {return OnLocationChange(args[0], args[1], args[2]);}
		public int method6(int[] args) {return OnStatusChange(args[0], args[1], args[2], args[3]);}
		public int method7(int[] args) {return OnSecurityChange(args[0], args[1], args[2]);}
		public int method8(int[] args) {return OnProgressChange64(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);}
		public int method9(int[] args) {return Init(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);}
		public int method10(int[] args) {return GetTargetFile(args[0]);}
		public int method11(int[] args) {return GetPercentComplete(args[0]);}
		public int method12(int[] args) {return GetAmountTransferred(args[0]);}
		public int method13(int[] args) {return GetSize(args[0]);}
		public int method14(int[] args) {return GetSource(args[0]);}
		public int method15(int[] args) {return GetTarget(args[0]);}
		public int method16(int[] args) {return GetCancelable(args[0]);}
		public int method17(int[] args) {return GetDisplayName(args[0]);}
		public int method18(int[] args) {return GetStartTime(args[0]);}
		public int method19(int[] args) {return GetMIMEInfo(args[0]);}
		public int method20(int[] args) {return Open(args[0]);}
		public int method21(int[] args) {return GetCancelDownloadOnClose(args[0]);}
		public int method22(int[] args) {return SetCancelDownloadOnClose(args[0]);}
		public int method23(int[] args) {return GetObserver(args[0]);}
		public int method24(int[] args) {return SetObserver(args[0]);}
		public int method25(int[] args) {return GetDialog(args[0]);}
		public int method26(int[] args) {return SetDialog(args[0]);}
	};

	webProgressListener = new XPCOMObject(new int[]{2, 0, 0, 4, 6, 3, 4, 3}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return OnStateChange(args[0], args[1], args[2],args[3]);}
		public int method4(int[] args) {return OnProgressChange(args[0], args[1], args[2],args[3],args[4],args[5]);}
		public int method5(int[] args) {return OnLocationChange(args[0], args[1], args[2]);}
		public int method6(int[] args) {return OnStatusChange(args[0], args[1], args[2],args[3]);}
		public int method7(int[] args) {return OnSecurityChange(args[0], args[1], args[2]);}
	};
}

void disposeCOMInterfaces() {
	if (supports != null) {
		supports.dispose();
		supports = null;
	}	
	if (download != null) {
		download.dispose();
		download = null;	
	}
	if (progressDialog != null) {
		progressDialog.dispose();
		progressDialog = null;	
	}
	if (webProgressListener != null) {
		webProgressListener.dispose();
		webProgressListener = null;	
	}
}

int getAddress() {
	return progressDialog.getAddress();
}

int QueryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);

	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {supports.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIDownload_1_8.NS_IDOWNLOAD_IID)) {
		XPCOM.memmove(ppvObject, new int[] {download.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIProgressDialog_1_8.NS_IPROGRESSDIALOG_IID)) {
		XPCOM.memmove(ppvObject, new int[] {progressDialog.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID)) {
		XPCOM.memmove(ppvObject, new int[] {webProgressListener.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	XPCOM.memmove(ppvObject, new int[] {0}, 4);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release() {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces();
	return refCount;
}

/* nsIDownload */

/* Note. The argument startTime is defined as a PRInt64. This translates into two java ints. */
public int Init(int aSource, int aTarget, int aDisplayName, int aMIMEInfo, int startTime1, int startTime2, int aTempFile, int aCancelable) {
	cancelable = new nsICancelable(aCancelable);
	nsIURI source = new nsIURI(aSource);
	int aSpec = XPCOM.nsEmbedCString_new();
	int rc = source.GetHost(aSpec);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	int length = XPCOM.nsEmbedCString_Length(aSpec);
	int buffer = XPCOM.nsEmbedCString_get(aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove(dest, buffer, length);
	XPCOM.nsEmbedCString_delete(aSpec);
	String url = new String(dest);

	nsIURI target = new nsIURI(aTarget);
	int aPath = XPCOM.nsEmbedCString_new();
	rc = target.GetPath(aPath);
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	length = XPCOM.nsEmbedCString_Length(aPath);
	buffer = XPCOM.nsEmbedCString_get(aPath);
	dest = new byte[length];
	XPCOM.memmove(dest, buffer, length);
	XPCOM.nsEmbedCString_delete(aPath);
	String filename = new String(dest);
	int separator = filename.lastIndexOf(System.getProperty("file.separator"));	//$NON-NLS-1$
	filename = filename.substring(separator + 1);

	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			if (event.widget == cancel) {
				shell.close();
			}
			if (cancelable != null) {
				int rc = cancelable.Cancel(XPCOM.NS_BINDING_ABORTED);
				if (rc != XPCOM.NS_OK) Browser.error(rc);
			}
			shell = null;
			cancelable = null;
		}
	};
	shell = new Shell(SWT.DIALOG_TRIM);
	String msg = Compatibility.getMessage("SWT_Download_File", new Object[] {filename}); //$NON-NLS-1$
	shell.setText(msg);
	GridLayout gridLayout = new GridLayout();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout(gridLayout);
	msg = Compatibility.getMessage("SWT_Download_Location", new Object[] {filename, url}); //$NON-NLS-1$
	new Label(shell, SWT.SIMPLE).setText(msg);
	status = new Label(shell, SWT.SIMPLE);
	msg = Compatibility.getMessage("SWT_Download_Started"); //$NON-NLS-1$
	status.setText(msg);
	GridData data = new GridData ();
	data.grabExcessHorizontalSpace = true;
	data.grabExcessVerticalSpace = true;
	status.setLayoutData (data);
	
	cancel = new Button(shell, SWT.PUSH);
	cancel.setText(SWT.getMessage("SWT_Cancel")); //$NON-NLS-1$
	data = new GridData ();
	data.horizontalAlignment = GridData.CENTER;
	cancel.setLayoutData (data);
	cancel.addListener(SWT.Selection, listener);
	shell.addListener(SWT.Close, listener);
	shell.pack();
	shell.open();
	return XPCOM.NS_OK;
}

int GetAmountTransferred(int arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetCancelable(int arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetDisplayName(int aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetMIMEInfo(int aMIMEInfo) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetPercentComplete(int aPercentComplete) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetSize(int arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetSource(int aSource) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetStartTime(int aStartTime) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetTarget(int aTarget) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetTargetFile(int arg0) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIProgressDialog */
public int GetCancelDownloadOnClose(int aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetDialog(int aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetObserver(int aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int Open(int aParent) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetCancelDownloadOnClose(int aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetDialog(int aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetObserver(int aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIWebProgressListener */

int OnLocationChange(int aWebProgress, int aRequest, int aLocation) {
	return XPCOM.NS_OK;
}

int OnProgressChange(int aWebProgress, int aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
	return OnProgressChange64(aWebProgress, aRequest, aCurSelfProgress, 0, aMaxSelfProgress, 0, aCurTotalProgress, 0, aMaxTotalProgress, 0);
}

/* Note. The last 4 args in the original interface are defined as PRInt64. These each translate into two java ints. */
int OnProgressChange64(int aWebProgress, int aRequest, int aCurSelfProgress1, int aCurSelfProgress2, int aMaxSelfProgress1, int aMaxSelfProgress2, int aCurTotalProgress1, int aCurTotalProgress2, int aMaxTotalProgress1, int aMaxTotalProgress2) {
	long aCurTotalProgress = (aCurTotalProgress2 << 32) + aCurTotalProgress1;
	long aMaxTotalProgress = (aMaxTotalProgress2 << 32) + aMaxTotalProgress1;
	long currentKBytes = aCurTotalProgress / 1024;
	long totalKBytes = aMaxTotalProgress / 1024;
	if (shell != null & !shell.isDisposed()) {
		Object[] arguments = {new Long(currentKBytes), new Long(totalKBytes)};
		String statusMsg = Compatibility.getMessage("SWT_Download_Status", arguments); //$NON-NLS-1$
		status.setText(statusMsg);
		shell.layout(true);
		shell.getDisplay().update();
	}
	return XPCOM.NS_OK;
}

int OnSecurityChange(int aWebProgress, int aRequest, int state) {
	return XPCOM.NS_OK;
}

int OnStateChange(int aWebProgress, int aRequest, int aStateFlags, int aStatus) {
	if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		cancelable = null;
		if (shell != null && !shell.isDisposed()) shell.dispose();
		shell = null;
	}
	return XPCOM.NS_OK;
}	

int OnStatusChange(int aWebProgress, int aRequest, int aStatus, int aMessage) {
	return XPCOM.NS_OK;
}		
}

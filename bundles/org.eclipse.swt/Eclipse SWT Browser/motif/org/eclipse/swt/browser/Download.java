/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
	
public Download() {
	createCOMInterfaces();
}

int AddRef() {
	refCount++;
	return refCount;
}

void createCOMInterfaces() {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	
	download = new XPCOMObject(new int[]{2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return Init(args[0], args[1], args[2],args[3], args[4], args[5], args[6]);}
		public int method4(int[] args) {return GetSource(args[0]);}
		public int method5(int[] args) {return GetTarget(args[0]);}
		public int method6(int[] args) {return GetPersist(args[0]);}
		public int method7(int[] args) {return GetPercentComplete(args[0]);}
		public int method8(int[] args) {return GetDisplayName(args[0]);}
		public int method9(int[] args) {return SetDisplayName(args[0]);}
		public int method10(int[] args) {return GetStartTime(args[0]);}
		public int method11(int[] args) {return GetMIMEInfo(args[0]);}
		public int method12(int[] args) {return GetListener(args[0]);}
		public int method13(int[] args) {return SetListener(args[0]);}
		public int method14(int[] args) {return GetObserver(args[0]);}
		public int method15(int[] args) {return SetObserver(args[0]);}
	};
	
	progressDialog = new XPCOMObject(new int[]{2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return Init(args[0], args[1], args[2],args[3], args[4], args[5], args[6]);}
		public int method4(int[] args) {return GetSource(args[0]);}
		public int method5(int[] args) {return GetTarget(args[0]);}
		public int method6(int[] args) {return GetPersist(args[0]);}
		public int method7(int[] args) {return GetPercentComplete(args[0]);}
		public int method8(int[] args) {return GetDisplayName(args[0]);}
		public int method9(int[] args) {return SetDisplayName(args[0]);}
		public int method10(int[] args) {return GetStartTime(args[0]);}
		public int method11(int[] args) {return GetMIMEInfo(args[0]);}
		public int method12(int[] args) {return GetListener(args[0]);}
		public int method13(int[] args) {return SetListener(args[0]);}
		public int method14(int[] args) {return GetObserver(args[0]);}
		public int method15(int[] args) {return SetObserver(args[0]);}
		public int method16(int[] args) {return Open(args[0]);}
		public int method17(int[] args) {return GetCancelDownloadOnClose(args[0]);}
		public int method18(int[] args) {return SetCancelDownloadOnClose(args[0]);}
		public int method19(int[] args) {return GetDialog(args[0]);}
		public int method20(int[] args) {return SetDialog(args[0]);}
	};
	
	webProgressListener = new XPCOMObject(new int[]{2, 0, 0, 4, 6, 3, 4, 3}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
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

int queryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);
	
	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {supports.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIDownload.NS_IDOWNLOAD_IID)) {
		XPCOM.memmove(ppvObject, new int[] {download.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIProgressDialog.NS_IPROGRESSDIALOG_IID)) {
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
public int Init(int aSource, int aTarget, int aDisplayName, int aMIMEInfo, int startTime1, int startTime2, int aPersist) {
	nsIURI source = new nsIURI(aSource);
	int aSpec = XPCOM.nsEmbedCString_new();
	source.GetHost(aSpec);
	int length = XPCOM.nsEmbedCString_Length(aSpec);
	int buffer = XPCOM.nsEmbedCString_get(aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove(dest, buffer, length);
	XPCOM.nsEmbedCString_delete(aSpec);
	String url = new String(dest);
	
	nsILocalFile target = new nsILocalFile(aTarget);
	int aNativeTarget = XPCOM.nsEmbedCString_new();
	target.GetNativeLeafName(aNativeTarget);
	length = XPCOM.nsEmbedCString_Length(aSpec);
	buffer = XPCOM.nsEmbedCString_get(aSpec);
	dest = new byte[length];
	XPCOM.memmove(dest, buffer, length);
	XPCOM.nsEmbedCString_delete(aNativeTarget);
	String file = new String(dest);
	
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			if (event.widget == cancel) {
				shell.close();
			}
			if (helperAppLauncher != null) {
				helperAppLauncher.Cancel();
				helperAppLauncher.Release();
			}
			shell = null;
			helperAppLauncher = null;
		}
	};
	shell = new Shell(SWT.DIALOG_TRIM);
	String msg = Compatibility.getMessage("SWT_Download_File", new Object[] {file});
	shell.setText(msg);
	GridLayout gridLayout = new GridLayout();
	gridLayout.marginHeight = 15;
	gridLayout.marginWidth = 15;
	gridLayout.verticalSpacing = 20;
	shell.setLayout(gridLayout);
	msg = Compatibility.getMessage("SWT_Download_Location", new Object[] {file, url});	
	new Label(shell, SWT.SIMPLE).setText(msg);
	status = new Label(shell, SWT.SIMPLE);
	msg = Compatibility.getMessage("SWT_Download_Started");	
	status.setText(msg);
	GridData data = new GridData ();
	data.grabExcessHorizontalSpace = true;
	data.grabExcessVerticalSpace = true;
	status.setLayoutData (data);
	
	cancel = new Button(shell, SWT.PUSH);
	cancel.setText(SWT.getMessage("SWT_Cancel"));
	data = new GridData ();
	data.horizontalAlignment = GridData.CENTER;
	cancel.setLayoutData (data);
	cancel.addListener(SWT.Selection, listener);
	shell.addListener(SWT.Close, listener);
	shell.pack();
	shell.open();
	return XPCOM.NS_OK;
}

public int GetSource(int aSource) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetTarget(int aTarget) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetPersist(int aPersist) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetPercentComplete(int aPercentComplete) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetDisplayName(int aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetDisplayName(int aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetStartTime(int aStartTime) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetMIMEInfo(int aMIMEInfo) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetListener(int aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetListener(int aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetObserver(int aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetObserver(int aObserver) {
	if (aObserver != 0) {
		nsISupports supports = new nsISupports(aObserver);
		int[] result = new int[1];
		int rc = supports.QueryInterface(nsIHelperAppLauncher.NS_IHELPERAPPLAUNCHER_IID, result);
		if (rc != XPCOM.NS_OK) Browser.error(rc);
		if (result[0] == 0) Browser.error(XPCOM.NS_ERROR_NO_INTERFACE);	
		
		helperAppLauncher = new nsIHelperAppLauncher(result[0]);
	}
	return XPCOM.NS_OK;
}

/* nsIProgressDialog */
public int Open(int aParent) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetCancelDownloadOnClose(int aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetCancelDownloadOnClose(int aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int GetDialog(int aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int SetDialog(int aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIWebProgressListener */

int OnStateChange(int aWebProgress, int aRequest, int aStateFlags, int aStatus) {
	if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		if (helperAppLauncher != null) helperAppLauncher.Release();
		helperAppLauncher = null;
		if (shell != null && !shell.isDisposed()) shell.dispose();
		shell = null;
	}
	return XPCOM.NS_OK;
}	

int OnProgressChange(int aWebProgress, int aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
	int currentBytes = aCurTotalProgress / 1024;
	int totalBytes = aMaxTotalProgress / 1024;
	if (shell != null & !shell.isDisposed()) {
		Object[] arguments = {new Integer(currentBytes), new Integer(totalBytes)};
		String statusMsg = Compatibility.getMessage("SWT_Download_Status", arguments);
		status.setText(statusMsg);
		
		shell.layout(true);
		shell.getDisplay().update();
	}
	return XPCOM.NS_OK;
}		

int OnLocationChange(int aWebProgress, int aRequest, int aLocation) {
	return XPCOM.NS_OK;
}
  
int OnStatusChange(int aWebProgress, int aRequest, int aStatus, int aMessage) {
	return XPCOM.NS_OK;
}		

int OnSecurityChange(int aWebProgress, int aRequest, int state) {
	return XPCOM.NS_OK;
}
}

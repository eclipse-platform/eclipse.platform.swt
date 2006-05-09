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
import org.eclipse.swt.internal.gtk.OS;
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
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
	};
	
	download = new XPCOMObject(new int[]{2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return Init(args[0], args[1], args[2],args[3], args[4], args[5], args[6]);}
		public int /*long*/ method4(int /*long*/[] args) {return GetSource(args[0]);}
		public int /*long*/ method5(int /*long*/[] args) {return GetTarget(args[0]);}
		public int /*long*/ method6(int /*long*/[] args) {return GetPersist(args[0]);}
		public int /*long*/ method7(int /*long*/[] args) {return GetPercentComplete(args[0]);}
		public int /*long*/ method8(int /*long*/[] args) {return GetDisplayName(args[0]);}
		public int /*long*/ method9(int /*long*/[] args) {return SetDisplayName(args[0]);}
		public int /*long*/ method10(int /*long*/[] args) {return GetStartTime(args[0]);}
		public int /*long*/ method11(int /*long*/[] args) {return GetMIMEInfo(args[0]);}
		public int /*long*/ method12(int /*long*/[] args) {return GetListener(args[0]);}
		public int /*long*/ method13(int /*long*/[] args) {return SetListener(args[0]);}
		public int /*long*/ method14(int /*long*/[] args) {return GetObserver(args[0]);}
		public int /*long*/ method15(int /*long*/[] args) {return SetObserver(args[0]);}
	};
	
	progressDialog = new XPCOMObject(new int[]{2, 0, 0, 7, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return Init(args[0], args[1], args[2],args[3], args[4], args[5], args[6]);}
		public int /*long*/ method4(int /*long*/[] args) {return GetSource(args[0]);}
		public int /*long*/ method5(int /*long*/[] args) {return GetTarget(args[0]);}
		public int /*long*/ method6(int /*long*/[] args) {return GetPersist(args[0]);}
		public int /*long*/ method7(int /*long*/[] args) {return GetPercentComplete(args[0]);}
		public int /*long*/ method8(int /*long*/[] args) {return GetDisplayName(args[0]);}
		public int /*long*/ method9(int /*long*/[] args) {return SetDisplayName(args[0]);}
		public int /*long*/ method10(int /*long*/[] args) {return GetStartTime(args[0]);}
		public int /*long*/ method11(int /*long*/[] args) {return GetMIMEInfo(args[0]);}
		public int /*long*/ method12(int /*long*/[] args) {return GetListener(args[0]);}
		public int /*long*/ method13(int /*long*/[] args) {return SetListener(args[0]);}
		public int /*long*/ method14(int /*long*/[] args) {return GetObserver(args[0]);}
		public int /*long*/ method15(int /*long*/[] args) {return SetObserver(args[0]);}
		public int /*long*/ method16(int /*long*/[] args) {return Open(args[0]);}
		public int /*long*/ method17(int /*long*/[] args) {return GetCancelDownloadOnClose(args[0]);}
		public int /*long*/ method18(int /*long*/[] args) {return SetCancelDownloadOnClose(args[0]);}
		public int /*long*/ method19(int /*long*/[] args) {return GetDialog(args[0]);}
		public int /*long*/ method20(int /*long*/[] args) {return SetDialog(args[0]);}
	};
	
	webProgressListener = new XPCOMObject(new int[]{2, 0, 0, 4, 6, 3, 4, 3}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return OnStateChange(args[0], args[1], args[2],args[3]);}
		public int /*long*/ method4(int /*long*/[] args) {return OnProgressChange(args[0], args[1], args[2],args[3],args[4],args[5]);}
		public int /*long*/ method5(int /*long*/[] args) {return OnLocationChange(args[0], args[1], args[2]);}
		public int /*long*/ method6(int /*long*/[] args) {return OnStatusChange(args[0], args[1], args[2],args[3]);}
		public int /*long*/ method7(int /*long*/[] args) {return OnSecurityChange(args[0], args[1], args[2]);}
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

int /*long*/ getAddress() {
	return progressDialog.getAddress();
}

int /*long*/ QueryInterface(int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);
	
	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int /*long*/[] {supports.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIDownload.NS_IDOWNLOAD_IID)) {
		XPCOM.memmove(ppvObject, new int /*long*/[] {download.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIProgressDialog.NS_IPROGRESSDIALOG_IID)) {
		XPCOM.memmove(ppvObject, new int /*long*/[] {progressDialog.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID)) {
		XPCOM.memmove(ppvObject, new int /*long*/[] {webProgressListener.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return XPCOM.NS_OK;
	}
	XPCOM.memmove(ppvObject, new int /*long*/[] {0}, OS.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release() {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces();
	return refCount;
}

/* nsIDownload */

/* Note. The argument startTime is defined as a PRInt64. This translates into two java ints. */
public int /*long*/ Init(int /*long*/ aSource, int /*long*/ aTarget, int /*long*/ aDisplayName, int /*long*/ aMIMEInfo, int /*long*/ startTime1, int /*long*/ startTime2, int /*long*/ aPersist) {
	nsIURI source = new nsIURI(aSource);
	int /*long*/ aSpec = XPCOM.nsEmbedCString_new();
	source.GetHost(aSpec);
	int length = XPCOM.nsEmbedCString_Length(aSpec);
	int /*long*/ buffer = XPCOM.nsEmbedCString_get(aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove(dest, buffer, length);
	XPCOM.nsEmbedCString_delete(aSpec);
	String url = new String(dest);
	
	nsILocalFile target = new nsILocalFile(aTarget);
	int /*long*/ aNativeTarget = XPCOM.nsEmbedCString_new();
	target.GetNativeLeafName(aNativeTarget);
	length = XPCOM.nsEmbedCString_Length(aNativeTarget);
	buffer = XPCOM.nsEmbedCString_get(aNativeTarget);
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

public int /*long*/ GetSource(int /*long*/ aSource) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetTarget(int /*long*/ aTarget) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetPersist(int /*long*/ aPersist) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetPercentComplete(int /*long*/ aPercentComplete) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetDisplayName(int /*long*/ aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ SetDisplayName(int /*long*/ aDisplayName) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetStartTime(int /*long*/ aStartTime) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetMIMEInfo(int /*long*/ aMIMEInfo) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetListener(int /*long*/ aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ SetListener(int /*long*/ aListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetObserver(int /*long*/ aObserver) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ SetObserver(int /*long*/ aObserver) {
	if (aObserver != 0) {
		nsISupports supports = new nsISupports(aObserver);
		int /*long*/[] result = new int /*long*/[1];
		int rc = supports.QueryInterface(nsIHelperAppLauncher.NS_IHELPERAPPLAUNCHER_IID, result);
		if (rc != XPCOM.NS_OK) Browser.error(rc);
		if (result[0] == 0) Browser.error(XPCOM.NS_ERROR_NO_INTERFACE);	
		
		helperAppLauncher = new nsIHelperAppLauncher(result[0]);
	}
	return XPCOM.NS_OK;
}

/* nsIProgressDialog */
public int /*long*/ Open(int /*long*/ aParent) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetCancelDownloadOnClose(int /*long*/ aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ SetCancelDownloadOnClose(int /*long*/ aCancelDownloadOnClose) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ GetDialog(int /*long*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

public int /*long*/ SetDialog(int /*long*/ aDialog) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsIWebProgressListener */

int /*long*/ OnStateChange(int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ aStateFlags, int /*long*/ aStatus) {
	if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		if (helperAppLauncher != null) helperAppLauncher.Release();
		helperAppLauncher = null;
		if (shell != null && !shell.isDisposed()) shell.dispose();
		shell = null;
	}
	return XPCOM.NS_OK;
}	

int /*long*/ OnProgressChange(int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ aCurSelfProgress, int /*long*/ aMaxSelfProgress, int /*long*/ aCurTotalProgress, int /*long*/ aMaxTotalProgress) {
	int currentBytes = (int)/*64*/aCurTotalProgress / 1024;
	int totalBytes = (int)/*64*/aMaxTotalProgress / 1024;
	if (shell != null & !shell.isDisposed()) {
		Object[] arguments = {new Integer(currentBytes), new Integer(totalBytes)};
		String statusMsg = Compatibility.getMessage("SWT_Download_Status", arguments);
		status.setText(statusMsg);
		
		shell.layout(true);
		shell.getDisplay().update();
	}
	return XPCOM.NS_OK;
}		

int /*long*/ OnLocationChange(int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ aLocation) {
	return XPCOM.NS_OK;
}
  
int /*long*/ OnStatusChange(int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ aStatus, int /*long*/ aMessage) {
	return XPCOM.NS_OK;
}		

int /*long*/ OnSecurityChange(int /*long*/ aWebProgress, int /*long*/ aRequest, int /*long*/ state) {
	return XPCOM.NS_OK;
}
}

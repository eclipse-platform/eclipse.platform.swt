/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class HelperAppLauncherDialog {
	XPCOMObject supports;
	XPCOMObject helperAppLauncherDialog;
	int refCount = 0;

public HelperAppLauncherDialog() {
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
	
	helperAppLauncherDialog = new XPCOMObject(new int[]{2, 0, 0, 3, 5}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return Show(args[0], args[1], args[2]);}
		public int method4(int[] args) {return PromptForSaveToFile(args[0], args[1], args[2], args[3], args[4]);}
	};		
}

void disposeCOMInterfaces() {
	if (supports != null) {
		supports.dispose();
		supports = null;
	}	
	if (helperAppLauncherDialog != null) {
		helperAppLauncherDialog.dispose();
		helperAppLauncherDialog = null;	
	}
}

int getAddress() {
	return helperAppLauncherDialog.getAddress();
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
	if (guid.Equals(nsIHelperAppLauncherDialog.NS_IHELPERAPPLAUNCHERDIALOG_IID)) {
		XPCOM.memmove(ppvObject, new int[] {helperAppLauncherDialog.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	
	XPCOM.memmove(ppvObject, new int[] {0}, 4);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release() {
	refCount--;
	/*
	* Note.  This instance lives as long as the download it is binded to.
	* Its reference count is expected to go down to 0 when the download
	* has completed or when it has been cancelled. E.g. when the user
	* cancels the File Dialog, cancels or closes the Download Dialog
	* and when the Download Dialog goes away after the download is completed.
	*/
	if (refCount == 0) disposeCOMInterfaces();
	return refCount;
}

/* nsIHelperAppLauncherDialog */

public int Show(int aLauncher, int aContext, int aForced) {
	nsIHelperAppLauncher helperAppLauncher = new nsIHelperAppLauncher(aLauncher);
	return helperAppLauncher.SaveToDisk(0, false);
}

public int PromptForSaveToFile(int aLauncher, int aWindowContext, int aDefaultFile, int aSuggestedFileExtension, int _retval) {
	nsIHelperAppLauncher helperAppLauncher = new nsIHelperAppLauncher(aLauncher);
	int length = XPCOM.nsCRT_strlen_PRUnichar(aDefaultFile);
	char[] dest = new char[length];
	XPCOM.memmove(dest, aDefaultFile, length * 2);
	String defaultFile = new String(dest);

	length = XPCOM.nsCRT_strlen_PRUnichar(aSuggestedFileExtension);
	dest = new char[length];
	XPCOM.memmove(dest, aSuggestedFileExtension, length * 2);
	String suggestedFileExtension = new String(dest);
	
	Shell shell = new Shell();
	FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
	fileDialog.setFileName(defaultFile);
	fileDialog.setFilterExtensions(new String[] {suggestedFileExtension});
	String name = fileDialog.open();
	shell.close();
	if (name == null) {
		int rc = helperAppLauncher.Cancel();
		if (rc != XPCOM.NS_OK) Browser.error(rc);
		return XPCOM.NS_OK;
	}
	int[] result = new int[1];
	nsString path = new nsString(name);
	int rc = XPCOM.NS_NewLocalFile(path.getAddress(), true, result);
	path.dispose();
	if (rc != XPCOM.NS_OK) Browser.error(rc);
	if (result[0] == 0) Browser.error(XPCOM.NS_ERROR_NULL_POINTER);
	/* Our own nsIDownload has been registered during the Browser initialization. It will be invoked by Mozilla. */
	XPCOM.memmove(_retval, result, 4);	
	return XPCOM.NS_OK;
}

}

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIHelperAppLauncher extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	public static final String NS_IHELPERAPPLAUNCHER_IID_STRING =
		"9503d0fe-4c9d-11d4-98d0-001083010e9b";

	public static final nsID NS_IHELPERAPPLAUNCHER_IID =
		new nsID(NS_IHELPERAPPLAUNCHER_IID_STRING);

	public nsIHelperAppLauncher(int address) {
		super(address);
	}

	public int GetMIMEInfo(int[] aMIMEInfo) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aMIMEInfo);
	}

	public int GetSource(int[] aSource) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSource);
	}

	public int GetSuggestedFileName(int[] aSuggestedFileName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aSuggestedFileName);
	}

	public int SaveToDisk(int aNewFileLocation, boolean aRememberThisPreference) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aNewFileLocation, aRememberThisPreference);
	}

	public int LaunchWithApplication(int aApplication, boolean aRememberThisPreference) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aApplication, aRememberThisPreference);
	}

	public int Cancel() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress());
	}

	public int SetWebProgressListener(int aWebProgressListener) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aWebProgressListener);
	}

	public int CloseProgressWindow() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress());
	}

	public int GetTargetFile(int[] aTargetFile) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aTargetFile);
	}
	
	public int GetTimeDownloadStarted(int aTimeDownloadStarted) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aTimeDownloadStarted);
	}
}
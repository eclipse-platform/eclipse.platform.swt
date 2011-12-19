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
 * -  Copyright (C) 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIHelperAppLauncher_8 extends nsICancelable {

	static final int LAST_METHOD_ID = nsICancelable.LAST_METHOD_ID + 11;

	public static final String NS_IHELPERAPPLAUNCHER_IID_STR =
		"d9a19faf-497b-408c-b995-777d956b72c0";

	public static final nsID NS_IHELPERAPPLAUNCHER_IID =
		new nsID(NS_IHELPERAPPLAUNCHER_IID_STR);

	public nsIHelperAppLauncher_8(int /*long*/ address) {
		super(address);
	}

	public int GetMIMEInfo(int /*long*/[] aMIMEInfo) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 1, getAddress(), aMIMEInfo);
	}

	public int GetSource(int /*long*/[] aSource) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 2, getAddress(), aSource);
	}

	public int GetSuggestedFileName(int /*long*/ aSuggestedFileName) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 3, getAddress(), aSuggestedFileName);
	}

	public int SaveToDisk(int /*long*/ aNewFileLocation, int aRememberThisPreference) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 4, getAddress(), aNewFileLocation, aRememberThisPreference);
	}

	public int LaunchWithApplication(int /*long*/ aApplication, int aRememberThisPreference) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 5, getAddress(), aApplication, aRememberThisPreference);
	}

	public int SetWebProgressListener(int /*long*/ aWebProgressListener) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 6, getAddress(), aWebProgressListener);
	}

	public int CloseProgressWindow() {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 7, getAddress());
	}

	public int GetTargetFile(int /*long*/[] aTargetFile) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 8, getAddress(), aTargetFile);
	}

	public int GetTargetFileIsExecutable(int[] aTargetFileIsExecutable) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 9, getAddress(), aTargetFileIsExecutable);
	}

	public int GetTimeDownloadStarted(int /*long*/ aTimeDownloadStarted) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 10, getAddress(), aTimeDownloadStarted);
	}
	
	public int GetContentLength(int /*long*/ aContentLength) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 11, getAddress(), aContentLength);
	}
}

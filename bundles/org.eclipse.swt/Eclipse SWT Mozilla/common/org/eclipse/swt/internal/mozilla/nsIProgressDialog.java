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
 * -  Copyright (C) 2003, 2008 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIProgressDialog extends nsIDownload {

	static final int LAST_METHOD_ID = nsIDownload.LAST_METHOD_ID + 5;

	public static final String NS_IPROGRESSDIALOG_IID_STR =
		"88a478b3-af65-440a-94dc-ed9b154d2990";

	public static final nsID NS_IPROGRESSDIALOG_IID =
		new nsID(NS_IPROGRESSDIALOG_IID_STR);

	public nsIProgressDialog(int /*long*/ address) {
		super(address);
	}

	public int Open(int /*long*/ aParent) {
		return XPCOM.VtblCall(nsIDownload.LAST_METHOD_ID + 1, getAddress(), aParent);
	}

	public int GetCancelDownloadOnClose(int[] aCancelDownloadOnClose) {
		return XPCOM.VtblCall(nsIDownload.LAST_METHOD_ID + 2, getAddress(), aCancelDownloadOnClose);
	}

	public int SetCancelDownloadOnClose(int aCancelDownloadOnClose) {
		return XPCOM.VtblCall(nsIDownload.LAST_METHOD_ID + 3, getAddress(), aCancelDownloadOnClose);
	}

	public int GetDialog(int /*long*/[] aDialog) {
		return XPCOM.VtblCall(nsIDownload.LAST_METHOD_ID + 4, getAddress(), aDialog);
	}

	public int SetDialog(int /*long*/ aDialog) {
		return XPCOM.VtblCall(nsIDownload.LAST_METHOD_ID + 5, getAddress(), aDialog);
	}
}

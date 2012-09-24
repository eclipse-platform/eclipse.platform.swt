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

public class nsIDownload_1_8 extends nsITransfer {

	static final int LAST_METHOD_ID = nsITransfer.LAST_METHOD_ID + 10;

	public static final String NS_IDOWNLOAD_IID_STR =
		"9e1fd9f2-9727-4926-85cd-f16c375bba6d";

	public static final nsID NS_IDOWNLOAD_IID =
		new nsID(NS_IDOWNLOAD_IID_STR);

	public nsIDownload_1_8(long /*int*/ address) {
		super(address);
	}

	public int GetTargetFile(long /*int*/[] aTargetFile) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 1, getAddress(), aTargetFile);
	}

	public int GetPercentComplete(int[] aPercentComplete) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 2, getAddress(), aPercentComplete);
	}

	public int GetAmountTransferred(long /*int*/ aAmountTransferred) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 3, getAddress(), aAmountTransferred);
	}

	public int GetSize(long /*int*/ aSize) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 4, getAddress(), aSize);
	}

	public int GetSource(long /*int*/[] aSource) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 5, getAddress(), aSource);
	}

	public int GetTarget(long /*int*/[] aTarget) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 6, getAddress(), aTarget);
	}

	public int GetCancelable(long /*int*/[] aCancelable) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 7, getAddress(), aCancelable);
	}

	public int GetDisplayName(long /*int*/[] aDisplayName) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 8, getAddress(), aDisplayName);
	}

	public int GetStartTime(long[] aStartTime) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 9, getAddress(), aStartTime);
	}

	public int GetMIMEInfo(long /*int*/[] aMIMEInfo) {
		return XPCOM.VtblCall(nsITransfer.LAST_METHOD_ID + 10, getAddress(), aMIMEInfo);
	}
}

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

public class nsIWebProgressListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 5;

	public static final String NS_IWEBPROGRESSLISTENER_IID_STRING =
		"570F39D1-EFD0-11d3-B093-00A024FFC08C";

	public static final nsID NS_IWEBPROGRESSLISTENER_IID =
		new nsID(NS_IWEBPROGRESSLISTENER_IID_STRING);

	public nsIWebProgressListener(int address) {
		super(address);
	}

	public static final int STATE_START = 1;

	public static final int STATE_REDIRECTING = 2;

	public static final int STATE_TRANSFERRING = 4;

	public static final int STATE_NEGOTIATING = 8;

	public static final int STATE_STOP = 16;

	public static final int STATE_IS_REQUEST = 65536;

	public static final int STATE_IS_DOCUMENT = 131072;

	public static final int STATE_IS_NETWORK = 262144;

	public static final int STATE_IS_WINDOW = 524288;

	public static final int STATE_IS_INSECURE = 4;

	public static final int STATE_IS_BROKEN = 1;

	public static final int STATE_IS_SECURE = 2;

	public static final int STATE_SECURE_HIGH = 262144;

	public static final int STATE_SECURE_MED = 65536;

	public static final int STATE_SECURE_LOW = 131072;

	public int OnStateChange(int aWebProgress, int aRequest, int aStateFlags, int aStatus) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aWebProgress, aRequest, aStateFlags, aStatus);
	}

	public int OnProgressChange(int aWebProgress, int aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aWebProgress, aRequest, aCurSelfProgress, aMaxSelfProgress, aCurTotalProgress, aMaxTotalProgress);
	}

	public int OnLocationChange(int aWebProgress, int aRequest, int location) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aWebProgress, aRequest, location);
	}

	public int OnStatusChange(int aWebProgress, int aRequest, int aStatus, char[] aMessage) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aWebProgress, aRequest, aStatus, aMessage);
	}

	public int OnSecurityChange(int aWebProgress, int aRequest, int state) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aWebProgress, aRequest, state);
	}
}
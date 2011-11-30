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

public class nsIWebProgressListener2_2 extends nsIWebProgressListener {

	static final int LAST_METHOD_ID = nsIWebProgressListener.LAST_METHOD_ID + 2;

	public static final String NS_IWEBPROGRESSLISTENER2_IID_STR =
		"dde39de0-e4e0-11da-8ad9-0800200c9a66";

	public static final nsID NS_IWEBPROGRESSLISTENER2_IID =
		new nsID(NS_IWEBPROGRESSLISTENER2_IID_STR);

	public nsIWebProgressListener2_2(int /*long*/ address) {
		super(address);
	}

	public int OnProgressChange64(int /*long*/ aWebProgress, int /*long*/ aRequest, long aCurSelfProgress, long aMaxSelfProgress, long aCurTotalProgress, long aMaxTotalProgress) {
		return XPCOM.VtblCall(nsIWebProgressListener.LAST_METHOD_ID + 1, getAddress(), aWebProgress, aRequest, aCurSelfProgress, aMaxSelfProgress, aCurTotalProgress, aMaxTotalProgress);
	}
}

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

public class nsIInputStream extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 5;

	public static final String NS_IINPUTSTREAM_IID_STR =
		"fa9c7f6c-61b3-11d4-9877-00c04fa0cf4a";

	public static final nsID NS_IINPUTSTREAM_IID =
		new nsID(NS_IINPUTSTREAM_IID_STR);

	public nsIInputStream(int /*long*/ address) {
		super(address);
	}

	public int Close() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress());
	}

	public int Available(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), _retval);
	}

	public int Read(byte[] aBuf, int aCount, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aBuf, aCount, _retval);
	}

	public int ReadSegments(int /*long*/ aWriter, int /*long*/ aClosure, int aCount, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aWriter, aClosure, aCount, _retval);
	}

	public int IsNonBlocking(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), _retval);
	}
}

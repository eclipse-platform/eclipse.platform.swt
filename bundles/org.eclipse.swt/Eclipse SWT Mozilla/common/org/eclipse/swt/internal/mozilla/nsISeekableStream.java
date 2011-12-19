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

public class nsISeekableStream extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_ISEEKABLESTREAM_IID_STR =
		"8429d350-1040-4661-8b71-f2a6ba455980";

	public static final nsID NS_ISEEKABLESTREAM_IID =
		new nsID(NS_ISEEKABLESTREAM_IID_STR);

	public nsISeekableStream(int /*long*/ address) {
		super(address);
	}

	public static final int NS_SEEK_SET = 0;
	public static final int NS_SEEK_CUR = 1;
	public static final int NS_SEEK_END = 2;

	public int Seek(int whence, long offset) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), whence, offset);
	}

	public int Tell(long[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), _retval);
	}

	public int SetEOF() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}
}

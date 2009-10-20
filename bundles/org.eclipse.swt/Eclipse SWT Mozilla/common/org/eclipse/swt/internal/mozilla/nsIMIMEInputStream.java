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
 * -  Copyright (C) 2003, 2009 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class  nsIMIMEInputStream extends nsIInputStream {

	static final int LAST_METHOD_ID = nsIInputStream.LAST_METHOD_ID + 4;

	public static final String NS_IMIMEINPUTSTREAM_IID_STR =
		"dcbce63c-1dd1-11b2-b94d-91f6d49a3161";

	public static final nsID NS_IMIMEINPUTSTREAM_IID =
		new nsID(NS_IMIMEINPUTSTREAM_IID_STR);

	public  nsIMIMEInputStream(int /*long*/ address) {
		super(address);
	}

	public int GetAddContentLength(int[] aAddContentLength) {
		return XPCOM.VtblCall(nsIInputStream.LAST_METHOD_ID + 1, getAddress(), aAddContentLength);
	}

	public int SetAddContentLength(int aAddContentLength) {
		return XPCOM.VtblCall(nsIInputStream.LAST_METHOD_ID + 2, getAddress(), aAddContentLength);
	}

	public int AddHeader(byte[] name, byte[] value) {
		return XPCOM.VtblCall(nsIInputStream.LAST_METHOD_ID + 3, getAddress(), name, value);
	}

	public int SetData(int /*long*/ stream) {
		return XPCOM.VtblCall(nsIInputStream.LAST_METHOD_ID + 4, getAddress(), stream);
	}
}
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
 * -  Copyright (C) 2003, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIInputStream extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 5;

	static final String NS_IINPUTSTREAM_IID_STR = "fa9c7f6c-61b3-11d4-9877-00c04fa0cf4a";
	static final String NS_IINPUTSTREAM_24_IID_STR = "53cdbc97-c2d7-4e30-b2c3-45b2ee79db18";

	static {
		IIDStore.RegisterIID(nsIInputStream.class, MozillaVersion.VERSION_BASE, new nsID(NS_IINPUTSTREAM_IID_STR));
		IIDStore.RegisterIID(nsIInputStream.class, MozillaVersion.VERSION_XR24, new nsID(NS_IINPUTSTREAM_24_IID_STR));
	}

	public nsIInputStream(long /*int*/ address) {
		super(address);
	}

	public int Available(int[] _retval) {
		if (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24)) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(this.getMethodIndex("available"), getAddress(), _retval);
	}

	public int Available(long[] _retval) {
		if (!IsXULRVersionOrLater(MozillaVersion.VERSION_XR24)) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(this.getMethodIndex("available"), getAddress(), _retval);
	}

	public int Read(byte[] aBuf, int aCount, int[] _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("read"), getAddress(), aBuf, aCount, _retval);
	}
}

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
 * -  Copyright (C) 2003, 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIWebBrowserStream extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_IWEBBROWSERSTREAM_IID_STR =
		"86d02f0e-219b-4cfc-9c88-bd98d2cce0b8";

	public static final nsID NS_IWEBBROWSERSTREAM_IID =
		new nsID(NS_IWEBBROWSERSTREAM_IID_STR);

	public nsIWebBrowserStream(long /*int*/ address) {
		super(address);
	}

	public int OpenStream(long /*int*/ aBaseURI, long /*int*/ aContentType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aBaseURI, aContentType);
	}

	public int AppendToStream(long /*int*/ aData, int aLen) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aData, aLen);
	}

	public int CloseStream() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}
}

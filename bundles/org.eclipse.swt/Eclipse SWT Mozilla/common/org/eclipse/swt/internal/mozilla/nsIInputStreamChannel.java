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

public class nsIInputStreamChannel extends nsIChannel {

	static final int LAST_METHOD_ID = nsIChannel.LAST_METHOD_ID + 3;

	public static final String NS_IINPUTSTREAMCHANNEL_IID_STRING =
		"560a64ce-6d66-44db-b38e-864469c52d03";

	public static final nsID NS_IINPUTSTREAMCHANNEL_IID =
		new nsID(NS_IINPUTSTREAMCHANNEL_IID_STRING);

	public nsIInputStreamChannel(int address) {
		super(address);
	}

	public int SetURI(int aURI) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aURI);
	}

	public int GetContentStream(int[] aContentStream) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aContentStream);
	}

	public int SetContentStream(int aContentStream) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aContentStream);
	}
}
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

public class nsIURIContentListener  {

	static final int LAST_METHOD_ID = 8;

	public static final String NS_IURICONTENTLISTENER_IID_STRING =
		"94928AB3-8B63-11D3-989D-001083010E9B";

	public static final nsID NS_IURICONTENTLISTENER_IID =
		new nsID(NS_IURICONTENTLISTENER_IID_STRING);

	private int address;

	public nsIURIContentListener(int address) {
		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}

	public int OnStartURIOpen(int aURI, int[] _retval) {
		return XPCOM.VtblCall(0, getAddress(), aURI, _retval);
	}
	
	public int DoContent(byte[] aContentType, boolean aIsContentPreferred, int aRequest, int[] aContentHandler, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int IsPreferred(byte[] aContentType, byte[] aDesiredContentType, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int CanHandleContent(byte[] aContentType, boolean aIsContentPreferred, int[] aDesiredContentType, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int GetLoadCookie(int[] aLoadCookie) {
		return XPCOM.VtblCall(4, getAddress(), aLoadCookie);
	}

	public int SetLoadCookie(int aLoadCookie) {
		return XPCOM.VtblCall(5, getAddress(), aLoadCookie);
	}

	public int GetParentContentListener(int[] aParentContentListener) {
		return XPCOM.VtblCall(5, getAddress(), aParentContentListener);
	}
	
	public int SetParentContentListener(int[] aParentContentListener) {
		return XPCOM.VtblCall(5, getAddress(), aParentContentListener);
	}
	
}
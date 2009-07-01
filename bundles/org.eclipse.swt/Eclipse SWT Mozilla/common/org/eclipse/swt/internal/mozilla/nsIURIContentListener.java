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

public class nsIURIContentListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 8;

	public static final String NS_IURICONTENTLISTENER_IID_STR =
		"94928ab3-8b63-11d3-989d-001083010e9b";

	public static final nsID NS_IURICONTENTLISTENER_IID =
		new nsID(NS_IURICONTENTLISTENER_IID_STR);

	public nsIURIContentListener(int /*long*/ address) {
		super(address);
	}

	public int OnStartURIOpen(int /*long*/ aURI, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aURI, _retval);
	}

	public int DoContent(byte[] aContentType, int aIsContentPreferred, int /*long*/ aRequest, int /*long*/[] aContentHandler, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aContentType, aIsContentPreferred, aRequest, aContentHandler, _retval);
	}

	public int IsPreferred(byte[] aContentType, int /*long*/[] aDesiredContentType, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aContentType, aDesiredContentType, _retval);
	}

	public int CanHandleContent(byte[] aContentType, int aIsContentPreferred, int /*long*/[] aDesiredContentType, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aContentType, aIsContentPreferred, aDesiredContentType, _retval);
	}

	public int GetLoadCookie(int /*long*/[] aLoadCookie) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aLoadCookie);
	}

	public int SetLoadCookie(int /*long*/ aLoadCookie) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aLoadCookie);
	}

	public int GetParentContentListener(int /*long*/[] aParentContentListener) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aParentContentListener);
	}

	public int SetParentContentListener(int /*long*/ aParentContentListener) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aParentContentListener);
	}
}

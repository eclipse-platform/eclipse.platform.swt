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

public class nsIWebBrowser extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 7;

	public static final String NS_IWEBBROWSER_IID_STRING =
		"69E5DF00-7B8B-11d3-AF61-00A024FFC08C";

	public static final nsID NS_IWEBBROWSER_IID =
		new nsID(NS_IWEBBROWSER_IID_STRING);

	public nsIWebBrowser(int address) {
		super(address);
	}

	public int AddWebBrowserListener(int aListener, nsID aIID) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aListener, aIID);
	}

	public int RemoveWebBrowserListener(int aListener, nsID aIID) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aListener, aIID);
	}

	public int GetContainerWindow(int[] aContainerWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aContainerWindow);
	}

	public int SetContainerWindow(int aContainerWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aContainerWindow);
	}

	public int GetParentURIContentListener(int[] aParentURIContentListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aParentURIContentListener);
	}

	public int SetParentURIContentListener(int aParentURIContentListener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aParentURIContentListener);
	}

	public int GetContentDOMWindow(int[] aContentDOMWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aContentDOMWindow);
	}
}
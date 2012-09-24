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
 * -  Copyright (C) 2003, 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIWebBrowser extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 9 : 7);

	public static final String NS_IWEBBROWSER_IID_STR =
		"69e5df00-7b8b-11d3-af61-00a024ffc08c";

	public static final String NS_IWEBBROWSER_10_IID_STR =
		"33e9d001-caab-4ba9-8961-54902f197202";
	
	public static final nsID NS_IWEBBROWSER_IID =
		new nsID(NS_IWEBBROWSER_IID_STR);

	public static final nsID NS_IWEBBROWSER_10_IID =
		new nsID(NS_IWEBBROWSER_10_IID_STR);
	
	public nsIWebBrowser(long /*int*/ address) {
		super(address);
	}

	public int AddWebBrowserListener(long /*int*/ aListener, nsID aIID) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aListener, aIID);
	}

	public int RemoveWebBrowserListener(long /*int*/ aListener, nsID aIID) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aListener, aIID);
	}

	public int GetContainerWindow(long /*int*/[] aContainerWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aContainerWindow);
	}

	public int SetContainerWindow(long /*int*/ aContainerWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aContainerWindow);
	}

	public int GetParentURIContentListener(long /*int*/[] aParentURIContentListener) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aParentURIContentListener);
	}

	public int SetParentURIContentListener(long /*int*/ aParentURIContentListener) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aParentURIContentListener);
	}

	public int GetContentDOMWindow(long /*int*/[] aContentDOMWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aContentDOMWindow);
	}

	public int GetIsActive(int[] aIsActive) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aIsActive);
	}

	public int SetIsActive(int aIsActive) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aIsActive);
	}
}

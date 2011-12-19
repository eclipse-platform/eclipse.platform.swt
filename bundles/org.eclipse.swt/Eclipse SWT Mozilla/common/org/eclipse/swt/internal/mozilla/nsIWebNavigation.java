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

public class nsIWebNavigation extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 13;

	public static final String NS_IWEBNAVIGATION_IID_STR =
		"f5d9e7b0-d930-11d3-b057-00a024ffc08c";

	public static final nsID NS_IWEBNAVIGATION_IID =
		new nsID(NS_IWEBNAVIGATION_IID_STR);

	public nsIWebNavigation(int /*long*/ address) {
		super(address);
	}

	public int GetCanGoBack(int[] aCanGoBack) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aCanGoBack);
	}

	public int GetCanGoForward(int[] aCanGoForward) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aCanGoForward);
	}

	public int GoBack() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}

	public int GoForward() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress());
	}

	public int GotoIndex(int index) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), index);
	}

	public static final int LOAD_FLAGS_MASK = 65535;
	public static final int LOAD_FLAGS_NONE = 0;
	public static final int LOAD_FLAGS_IS_REFRESH = 16;
	public static final int LOAD_FLAGS_IS_LINK = 32;
	public static final int LOAD_FLAGS_BYPASS_HISTORY = 64;
	public static final int LOAD_FLAGS_REPLACE_HISTORY = 128;
	public static final int LOAD_FLAGS_BYPASS_CACHE = 256;
	public static final int LOAD_FLAGS_BYPASS_PROXY = 512;
	public static final int LOAD_FLAGS_CHARSET_CHANGE = 1024;

	public int LoadURI(char[] uri, int loadFlags, int /*long*/ referrer, int /*long*/ postData, int /*long*/ headers) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), uri, loadFlags, referrer, postData, headers);
	}

	public int Reload(int reloadFlags) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), reloadFlags);
	}

	public static final int STOP_NETWORK = 1;
	public static final int STOP_CONTENT = 2;
	public static final int STOP_ALL = 3;

	public int Stop(int stopFlags) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), stopFlags);
	}

	public int GetDocument(int /*long*/[] aDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aDocument);
	}

	public int GetCurrentURI(int /*long*/[] aCurrentURI) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aCurrentURI);
	}

	public int GetReferringURI(int /*long*/[] aReferringURI) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aReferringURI);
	}

	public int GetSessionHistory(int /*long*/[] aSessionHistory) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aSessionHistory);
	}

	public int SetSessionHistory(int /*long*/ aSessionHistory) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aSessionHistory);
	}
}

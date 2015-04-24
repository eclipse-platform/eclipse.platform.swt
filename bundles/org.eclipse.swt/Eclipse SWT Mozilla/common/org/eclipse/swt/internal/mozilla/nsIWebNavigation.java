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


public class nsIWebNavigation extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 14 : 13);

	static final String NS_IWEBNAVIGATION_IID_STR = "f5d9e7b0-d930-11d3-b057-00a024ffc08c";
	static final String NS_IWEBNAVIGATION_24_IID_STR = "28404f7e-0f17-4dc3-a21a-2074d8659b02";
	static final String NS_IWEBNAVIGATION_31_IID_STR = "b7568a50-4c50-442c-a6be-3a340a48d89a";

	static {
		IIDStore.RegisterIID(nsIWebNavigation.class, MozillaVersion.VERSION_BASE, new nsID(NS_IWEBNAVIGATION_IID_STR));
		IIDStore.RegisterIID(nsIWebNavigation.class, MozillaVersion.VERSION_XR24, new nsID(NS_IWEBNAVIGATION_24_IID_STR));
		IIDStore.RegisterIID(nsIWebNavigation.class, MozillaVersion.VERSION_XR31, new nsID(NS_IWEBNAVIGATION_31_IID_STR));
	}

	public nsIWebNavigation(long /*int*/ address) {
		super(address);
	}

	public int GetCanGoBack(int[] aCanGoBack) {
		return XPCOM.VtblCall(this.getGetterIndex("canGoBack"), getAddress(), aCanGoBack);
	}

	public int GetCanGoForward(int[] aCanGoForward) {
		return XPCOM.VtblCall(this.getGetterIndex("canGoForward"), getAddress(), aCanGoForward);
	}

	public int GoBack() {
		return XPCOM.VtblCall(this.getMethodIndex("goBack"), getAddress());
	}

	public int GoForward() {
		return XPCOM.VtblCall(this.getMethodIndex("goForward"), getAddress());
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

	public int LoadURI(char[] uri, int loadFlags, long /*int*/ referrer, long /*int*/ postData, long /*int*/ headers) {
		return XPCOM.VtblCall(this.getMethodIndex("loadURI"), getAddress(), uri, loadFlags, referrer, postData, headers);
	}

	public int Reload(int reloadFlags) {
		return XPCOM.VtblCall(this.getMethodIndex("reload"), getAddress(), reloadFlags);
	}

	public static final int STOP_NETWORK = 1;
	public static final int STOP_CONTENT = 2;
	public static final int STOP_ALL = 3;

	public int Stop(int stopFlags) {
		return XPCOM.VtblCall(this.getMethodIndex("stop"), getAddress(), stopFlags);
	}

	public int GetCurrentURI(long /*int*/[] aCurrentURI) {
		return XPCOM.VtblCall(this.getGetterIndex("currentURI"), getAddress(), aCurrentURI);
	}
}

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
 * -  Copyright (C) 2006 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIPrefService extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	public static final String NS_IPREFSERVICE_IID_STR =
		"decb9cc7-c08f-4ea5-be91-a8fc637ce2d2";

	public static final nsID NS_IPREFSERVICE_IID =
		new nsID(NS_IPREFSERVICE_IID_STR);

	public nsIPrefService(long /*int*/ address) {
		super(address);
	}

	public int ReadUserPrefs(long /*int*/ aFile) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aFile);
	}

	public int ResetPrefs() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress());
	}

	public int ResetUserPrefs() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}

	public int SavePrefFile(long /*int*/ aFile) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aFile);
	}

	public int GetBranch(byte[] aPrefRoot, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aPrefRoot, _retval);
	}

	public int GetDefaultBranch(byte[] aPrefRoot, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aPrefRoot, _retval);
	}
}

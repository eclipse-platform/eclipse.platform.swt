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

public class nsICookie extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 9;

	public static final String NS_ICOOKIE_IID_STR =
		"e9fcb9a4-d376-458f-b720-e65e7df593bc";

	public static final nsID NS_ICOOKIE_IID =
		new nsID(NS_ICOOKIE_IID_STR);

	public nsICookie(int /*long*/ address) {
		super(address);
	}

	public int GetName(int /*long*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aName);
	}

	public int GetValue(int /*long*/ aValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aValue);
	}

	public int GetIsDomain(int[] aIsDomain) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aIsDomain);
	}

	public int GetHost(int /*long*/ aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aHost);
	}

	public int GetPath(int /*long*/ aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aPath);
	}

	public int GetIsSecure(int[] aIsSecure) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aIsSecure);
	}

	public int GetExpires(long[] aExpires) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aExpires);
	}

	public static final int STATUS_UNKNOWN = 0;

	public static final int STATUS_ACCEPTED = 1;

	public static final int STATUS_DOWNGRADED = 2;

	public static final int STATUS_FLAGGED = 3;

	public static final int STATUS_REJECTED = 4;

	public int GetStatus(int /*long*/ aStatus) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aStatus);
	}

	public static final int POLICY_UNKNOWN = 0;

	public static final int POLICY_NONE = 1;

	public static final int POLICY_NO_CONSENT = 2;

	public static final int POLICY_IMPLICIT_CONSENT = 3;

	public static final int POLICY_EXPLICIT_CONSENT = 4;

	public static final int POLICY_NO_II = 5;

	public int GetPolicy(int /*long*/ aPolicy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aPolicy);
	}
}

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

public class nsIURI extends nsISupports {
	
	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 32 : 26);

	public static final String NS_IURI_IID_STR =
		"07a22cc0-0ce5-11d3-9331-00104ba0fd40";

	public static final String NS_IURI_10_IID_STR =
		"395fe045-7d18-4adb-a3fd-af98c8a1af11";

	public static final nsID NS_IURI_IID =
		new nsID(NS_IURI_IID_STR);
	
	public static final nsID NS_IURI_10_IID =
			new nsID(NS_IURI_10_IID_STR);

	public nsIURI(long /*int*/ address) {
		super(address);
	}

	public int GetSpec(long /*int*/ aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aSpec);
	}

	public int SetSpec(long /*int*/ aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSpec);
	}

	public int GetPrePath(long /*int*/ aPrePath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aPrePath);
	}

	public int GetScheme(long /*int*/ aScheme) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aScheme);
	}

	public int SetScheme(long /*int*/ aScheme) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aScheme);
	}

	public int GetUserPass(long /*int*/ aUserPass) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aUserPass);
	}

	public int SetUserPass(long /*int*/ aUserPass) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aUserPass);
	}

	public int GetUsername(long /*int*/ aUsername) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aUsername);
	}

	public int SetUsername(long /*int*/ aUsername) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aUsername);
	}

	public int GetPassword(long /*int*/ aPassword) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aPassword);
	}

	public int SetPassword(long /*int*/ aPassword) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aPassword);
	}

	public int GetHostPort(long /*int*/ aHostPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aHostPort);
	}

	public int SetHostPort(long /*int*/ aHostPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aHostPort);
	}

	public int GetHost(long /*int*/ aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aHost);
	}

	public int SetHost(long /*int*/ aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aHost);
	}

	public int GetPort(int[] aPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aPort);
	}

	public int SetPort(int aPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aPort);
	}

	public int GetPath(long /*int*/ aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aPath);
	}

	public int SetPath(long /*int*/ aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), aPath);
	}

	public int Equals(long /*int*/ other, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), other, _retval);
	}

	public int SchemeIs(byte[] scheme, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 21, getAddress(), scheme, _retval);
	}

	public int Clone(long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), _retval);
	}

	public int Resolve(long /*int*/ relativePath, long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), relativePath, _retval);
	}

	public int GetAsciiSpec(long /*int*/ aAsciiSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 24, getAddress(), aAsciiSpec);
	}

	public int GetAsciiHost(long /*int*/ aAsciiHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aAsciiHost);
	}

	public int GetOriginCharset(long /*int*/ aOriginCharset) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aOriginCharset);
	}
	
	public int GetRef(long /*int*/ aRef) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), aRef);
	}

	public int SetRef(long /*int*/ aRef) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 28, getAddress(), aRef);
	}

	public int EqualsExceptRef(long /*int*/ other, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), other, _retval);
	}

	public int CloneIgnoringRef(long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 30, getAddress(), _retval);
	}

	public int GetSpecIgnoringRef(long /*int*/ aSpecIgnoringRef) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 31, getAddress(), aSpecIgnoringRef);
	}

	public int GetHasRef(int[] aHasRef) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 32, getAddress(), aHasRef);
	}
}

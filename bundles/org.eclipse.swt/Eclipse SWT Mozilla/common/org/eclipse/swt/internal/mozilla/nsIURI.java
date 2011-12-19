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

public class nsIURI extends nsISupports {
	
	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (Is8 ? 32 : 26);

	public static final String NS_IURI_IID_STR =
		"07a22cc0-0ce5-11d3-9331-00104ba0fd40";

	public static final String NS_IURI_8_IID_STR =
		"395fe045-7d18-4adb-a3fd-af98c8a1af11";

	public static final nsID NS_IURI_IID =
		new nsID(NS_IURI_IID_STR);
	
	public static final nsID NS_IURI_8_IID =
			new nsID(NS_IURI_8_IID_STR);

	public nsIURI(int /*long*/ address) {
		super(address);
	}

	public int GetSpec(int /*long*/ aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aSpec);
	}

	public int SetSpec(int /*long*/ aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSpec);
	}

	public int GetPrePath(int /*long*/ aPrePath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aPrePath);
	}

	public int GetScheme(int /*long*/ aScheme) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aScheme);
	}

	public int SetScheme(int /*long*/ aScheme) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aScheme);
	}

	public int GetUserPass(int /*long*/ aUserPass) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aUserPass);
	}

	public int SetUserPass(int /*long*/ aUserPass) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aUserPass);
	}

	public int GetUsername(int /*long*/ aUsername) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aUsername);
	}

	public int SetUsername(int /*long*/ aUsername) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aUsername);
	}

	public int GetPassword(int /*long*/ aPassword) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aPassword);
	}

	public int SetPassword(int /*long*/ aPassword) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aPassword);
	}

	public int GetHostPort(int /*long*/ aHostPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aHostPort);
	}

	public int SetHostPort(int /*long*/ aHostPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aHostPort);
	}

	public int GetHost(int /*long*/ aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aHost);
	}

	public int SetHost(int /*long*/ aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aHost);
	}

	public int GetPort(int[] aPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aPort);
	}

	public int SetPort(int aPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aPort);
	}

	public int GetPath(int /*long*/ aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aPath);
	}

	public int SetPath(int /*long*/ aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), aPath);
	}

	public int Equals(int /*long*/ other, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), other, _retval);
	}

	public int SchemeIs(byte[] scheme, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 21, getAddress(), scheme, _retval);
	}

	public int Clone(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), _retval);
	}

	public int Resolve(int /*long*/ relativePath, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), relativePath, _retval);
	}

	public int GetAsciiSpec(int /*long*/ aAsciiSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 24, getAddress(), aAsciiSpec);
	}

	public int GetAsciiHost(int /*long*/ aAsciiHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aAsciiHost);
	}

	public int GetOriginCharset(int /*long*/ aOriginCharset) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aOriginCharset);
	}
	
	public int GetRef(int /*long*/ aRef) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), aRef);
	}

	public int SetRef(int /*long*/ aRef) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 28, getAddress(), aRef);
	}

	public int EqualsExceptRef(int /*long*/ other, int[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), other, _retval);
	}

	public int CloneIgnoringRef(int /*long*/[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 30, getAddress(), _retval);
	}

	public int GetSpecIgnoringRef(int /*long*/ aSpecIgnoringRef) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 31, getAddress(), aSpecIgnoringRef);
	}

	public int GetHasRef(int[] aHasRef) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 32, getAddress(), aHasRef);
	}
}

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

public class nsIURI extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 26;

	public static final String NS_IURI_IID_STRING =
		"07a22cc0-0ce5-11d3-9331-00104ba0fd40";

	public static final nsID NS_IURI_IID =
		new nsID(NS_IURI_IID_STRING);

	public nsIURI(int address) {
		super(address);
	}

	public int GetSpec(int aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aSpec);
	}

	public int SetSpec(int aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSpec);
	}

	public int GetPrePath(int aPrePath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aPrePath);
	}

	public int GetScheme(int aScheme) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aScheme);
	}

	public int SetScheme(int aScheme) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aScheme);
	}

	public int GetUserPass(int userPass) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), userPass);
	}

	public int SetUserPass(int userPass) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), userPass);
	}

	public int GetUsername(int aUsername) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aUsername);
	}

	public int SetUsername(int aUsername) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aUsername);
	}

	public int GetPassword(int aPassword) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aPassword);
	}

	public int SetPassword(int aPassword) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aPassword);
	}

	public int GetHostPort(int aHostPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aHostPort);
	}

	public int SetHostPort(int aHostPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aHostPort);
	}

	public int GetHost(int aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aHost);
	}

	public int SetHost(int aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aHost);
	}

	public int GetPort(int[] aPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aPort);
	}

	public int SetPort(int aPort) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aPort);
	}

	public int GetPath(int aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aPath);
	}

	public int SetPath(int aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), aPath);
	}

	public int Equals(int other, boolean[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), other, _retval);
	}

	public int SchemeIs(byte[] scheme, boolean[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 21, getAddress(), scheme, _retval);
	}

	public int Clone(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), _retval);
	}

	public int Resolve(int relativePath, int _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), relativePath, _retval);
	}

	public int GetAsciiSpec(int aAsciiSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 24, getAddress(), aAsciiSpec);
	}

	public int GetAsciiHost(int aAsciiHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aAsciiHost);
	}

	public int GetOriginCharset(int aOriginCharset) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aOriginCharset);
	}
}
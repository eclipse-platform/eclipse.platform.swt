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

public class nsIDOMLocation extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 20;

	public static final String NS_IDOMLOCATION_IID_STRING =
		"a6cf906d-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMLOCATION_IID =
		new nsID(NS_IDOMLOCATION_IID_STRING);

	public nsIDOMLocation(int address) {
		super(address);
	}

	public int GetHash(int aHash) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1,getAddress(), aHash);
	}

	public int SetHash(int aHash) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2,getAddress(), aHash);
	}

	public int GetHost(int aHost) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3,getAddress(), aHost);
	}

	public int SetHost(int aHost) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4,getAddress(), aHost);
	}

	public int GetHostname(int aHostname) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5,getAddress(), aHostname);
	}

	public int SetHostname(int aHostname) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6,getAddress(), aHostname);
	}

	public int GetHref(int aHref) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7,getAddress(), aHref);
	}

	public int SetHref(int aHref) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8,getAddress(), aHref);
	}

	public int GetPathname(int aPathname) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9,getAddress(), aPathname);
	}

	public int SetPathname(int aPathname) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10,getAddress(), aPathname);
	}

	public int GetPort(int aPort) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11,getAddress(), aPort);
	}

	public int SetPort(int aPort) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12,getAddress(), aPort);
	}

	public int GetProtocol(int aProtocol) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13,getAddress(), aProtocol);
	}

	public int SetProtocol(int aProtocol) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14,getAddress(), aProtocol);
	}

	public int GetSearch(int aSearch) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15,getAddress(), aSearch);
	}

	public int SetSearch(int aSearch) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16,getAddress(), aSearch);
	}

	public int Reload(boolean forceget) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), forceget);
	}

	public int Replace(int url) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), url);
	}

	public int Assign(int url) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), url);
	}

	public int ToString(int retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), retVal);
	}
}
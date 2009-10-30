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
 * -  Copyright (C) 2003, 2009 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsISSLStatus extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 7;

	public static final String NS_ISSLSTATUS_IID_STR =
		"cfede939-def1-49be-81ed-d401b3a07d1c";

	public static final nsID NS_ISSLSTATUS_IID =
		new nsID(NS_ISSLSTATUS_IID_STR);

	public  nsISSLStatus(int /*long*/ address) {
		super(address);
	}

	public int GetServerCert(int /*long*/[] aServerCert) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aServerCert);
	}

	public int GetCipherName(int /*long*/[] aCipherName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aCipherName);
	}

	public int GetKeyLength(int[] aKeyLength) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aKeyLength);
	}

	public int GetSecretKeyLength(int[] aSecretKeyLength) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aSecretKeyLength);
	}

	public int GetIsDomainMismatch(int[] aIsDomainMismatch) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aIsDomainMismatch);
	}

	public int GetIsNotValidAtThisTime(int[] aIsNotValidAtThisTime) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aIsNotValidAtThisTime);
	}

	public int GetIsUntrusted(int[] aIsUntrusted) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aIsUntrusted);
	}
}

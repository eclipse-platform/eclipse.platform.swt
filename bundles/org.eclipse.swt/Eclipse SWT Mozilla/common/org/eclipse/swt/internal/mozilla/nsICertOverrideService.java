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


public class nsICertOverrideService extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	static final String NS_ICERTOVERRIDESERVICE_IID_STR = "31738d2a-77d3-4359-84c9-4be2f38fb8c5";

	static {
		IIDStore.RegisterIID(nsICertOverrideService.class, MozillaVersion.VERSION_BASE, new nsID(NS_ICERTOVERRIDESERVICE_IID_STR));
	}

	public nsICertOverrideService(long /*int*/ address) {
		super(address);
	}

	public static final int ERROR_UNTRUSTED = 1;
	public static final int ERROR_MISMATCH = 2;
	public static final int ERROR_TIME = 4;

	public int RememberValidityOverride(long /*int*/ aHostName, int aPort, long /*int*/ aCert, int aOverrideBits, int aTemporary) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aHostName, aPort, aCert, aOverrideBits, aTemporary);
	}
}

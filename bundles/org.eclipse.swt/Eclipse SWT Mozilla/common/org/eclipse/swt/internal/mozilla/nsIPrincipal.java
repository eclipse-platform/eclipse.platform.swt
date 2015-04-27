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
 * -  Copyright (C) 2003, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIPrincipal extends nsISerializable {

	static final int LAST_METHOD_ID = nsISerializable.LAST_METHOD_ID + (IsXULRunner31 () ? 19 : (IsXULRunner24() ? 21 : (IsXULRunner10() ? 26 : 23)));

	static final String NS_IPRINCIPAL_IID_STR = "b8268b9a-2403-44ed-81e3-614075c92034";
	static final String NS_IPRINCIPAL_10_IID_STR = "b406a2db-e547-4c95-b8e2-ad09ecb54ce0";
	static final String NS_IPRINCIPAL_24_IID_STR = "dbda8bb0-3023-4aec-ad98-8e9931a29d70";
	static final String NS_IPRINCIPAL_31_IID_STR = "204555e7-04ad-4cc8-9f0e-840615cc43e8";

	static {
		IIDStore.RegisterIID(nsIPrincipal.class, MozillaVersion.VERSION_BASE, new nsID(NS_IPRINCIPAL_IID_STR));
		IIDStore.RegisterIID(nsIPrincipal.class, MozillaVersion.VERSION_XR10, new nsID(NS_IPRINCIPAL_10_IID_STR));
		IIDStore.RegisterIID(nsIPrincipal.class, MozillaVersion.VERSION_XR24, new nsID(NS_IPRINCIPAL_24_IID_STR));
		IIDStore.RegisterIID(nsIPrincipal.class, MozillaVersion.VERSION_XR31, new nsID(NS_IPRINCIPAL_31_IID_STR));
	}

	public nsIPrincipal(long /*int*/ address) {
		super(address);
	}

	public static final int ENABLE_DENIED = 1;
	public static final int ENABLE_UNKNOWN = 2;
	public static final int ENABLE_WITH_USER_PERMISSION = 3;
	public static final int ENABLE_GRANTED = 4;

	public int GetJSPrincipals(long /*int*/ cx, long /*int*/[] _retval) {
		if (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24)) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(this.getMethodIndex("getJSPrincipals"), getAddress(), cx, _retval);
	}
}

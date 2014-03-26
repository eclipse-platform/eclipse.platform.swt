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


public class nsICookie extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 9;

	static final String NS_ICOOKIE_IID_STR = "e9fcb9a4-d376-458f-b720-e65e7df593bc";
	static final String NS_ICOOKIE_24_IID_STR = "8684966b-1877-4f0f-8155-be4490b96bf7";

	static {
		IIDStore.RegisterIID(nsICookie.class, MozillaVersion.VERSION_BASE, new nsID(NS_ICOOKIE_IID_STR));
		IIDStore.RegisterIID(nsICookie.class, MozillaVersion.VERSION_XR24, new nsID(NS_ICOOKIE_24_IID_STR));
	}

	public nsICookie(long /*int*/ address) {
		super(address);
	}

	public int GetName(long /*int*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aName);
	}

	public int GetHost(long /*int*/ aHost) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aHost);
	}

	public int GetPath(long /*int*/ aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aPath);
	}

	public int GetExpires(long[] aExpires) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aExpires);
	}
}

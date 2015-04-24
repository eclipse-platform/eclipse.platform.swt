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


public class nsIInterfaceInfo extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + ((IsXULRVersionOrLater (MozillaVersion.VERSION_XR10)) ? 21 : 20);
	
	static final String NS_IINTERFACEINFO_IID_STR = "215dbe04-94a7-11d2-ba58-00805f8a5dd7";
	static final String NS_IINTERFACEINFO_10_IID_STR = "1affa260-8965-4612-869a-78af4ccfb287";

	static {
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_BASE, new nsID(NS_IINTERFACEINFO_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR10, new nsID(NS_IINTERFACEINFO_10_IID_STR));
	}

	public nsIInterfaceInfo(long /*int*/ address) {
		super(address);
	}

	public int GetMethodInfoForName(byte[] methodName, int [] index, long /*int*/[] result) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + ((IsXULRVersionOrLater (MozillaVersion.VERSION_XR10)) ? 9 : 8), getAddress(), methodName, index, result);
	}
}

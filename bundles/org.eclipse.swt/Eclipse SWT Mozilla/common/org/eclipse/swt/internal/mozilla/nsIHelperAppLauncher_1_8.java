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


public class nsIHelperAppLauncher_1_8 extends nsICancelable {

	static final int LAST_METHOD_ID = nsICancelable.LAST_METHOD_ID + (MozillaVersion.CheckVersion(MozillaVersion.VERSION_XR1_9) ? 10 : 9);

	static final String NS_IHELPERAPPLAUNCHER_IID_STR = "99a0882d-2ff9-4659-9952-9ac531ba5592";
	static final String NS_IHELPERAPPLAUNCHER_1_9_IID_STR = "cc75c21a-0a79-4f68-90e1-563253d0c555";

	static {
		IIDStore.RegisterIID(nsIHelperAppLauncher_1_8.class, MozillaVersion.VERSION_BASE, new nsID(NS_IHELPERAPPLAUNCHER_IID_STR));
		IIDStore.RegisterIID(nsIHelperAppLauncher_1_8.class, MozillaVersion.VERSION_XR1_9, new nsID(NS_IHELPERAPPLAUNCHER_1_9_IID_STR));
	}

	public nsIHelperAppLauncher_1_8(long /*int*/ address) {
		super(address);
	}

	public int SaveToDisk(long /*int*/ aNewFileLocation, int aRememberThisPreference) {
		return XPCOM.VtblCall(nsICancelable.LAST_METHOD_ID + 4, getAddress(), aNewFileLocation, aRememberThisPreference);
	}
}

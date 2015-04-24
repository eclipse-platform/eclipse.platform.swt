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


public class nsIXPCSecurityManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 3 : 4);

	static final String NS_IXPCSECURITYMANAGER_IID_STR = "31431440-f1ce-11d2-985a-006008962422";
	static final String NS_IXPCSECURITYMANAGER_31_IID_STR = "d4d21714-116b-4851-a785-098c5dfea523";

	static {
		IIDStore.RegisterIID(nsIXPCSecurityManager.class, MozillaVersion.VERSION_BASE, new nsID(NS_IXPCSECURITYMANAGER_IID_STR));
		IIDStore.RegisterIID(nsIXPCSecurityManager.class, MozillaVersion.VERSION_XR31, new nsID(NS_IXPCSECURITYMANAGER_31_IID_STR));
	}

	public nsIXPCSecurityManager(long /*int*/ address) {
		super(address);
	}
}

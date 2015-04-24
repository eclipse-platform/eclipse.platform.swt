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


public class nsIComponentManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 7 : (IsXULRunner24() ? 6 : 4));

	static final String NS_ICOMPONENTMANAGER_IID_STR = "a88e5a60-205a-4bb1-94e1-2628daf51eae";
	static final String NS_ICOMPONENTMANAGER_24_IID_STR = "1d940426-5fe5-42c3-84ae-a300f2d9ebd5";
	static final String NS_ICOMPONENTMANAGER_31_IID_STR = "d604ffc3-1ba3-4f6c-b65f-1ed4199364c3";

	static {
		IIDStore.RegisterIID(nsIComponentManager.class, MozillaVersion.VERSION_BASE, new nsID(NS_ICOMPONENTMANAGER_IID_STR));
		IIDStore.RegisterIID(nsIComponentManager.class, MozillaVersion.VERSION_XR24, new nsID(NS_ICOMPONENTMANAGER_24_IID_STR));
		IIDStore.RegisterIID(nsIComponentManager.class, MozillaVersion.VERSION_XR31, new nsID(NS_ICOMPONENTMANAGER_31_IID_STR));
	}

	public nsIComponentManager(long /*int*/ address) {
		super(address);
	}

	public int CreateInstance(nsID aClass, long /*int*/ aDelegate, nsID aIID, long /*int*/[] result) {
		return XPCOM.VtblCall(this.getMethodIndex("createInstance"), getAddress(), aClass, aDelegate, aIID, result);
	}

	public int CreateInstanceByContractID(byte[] aContractID, long /*int*/ aDelegate, nsID aIID, long /*int*/[] result) {
		return XPCOM.VtblCall(this.getMethodIndex("createInstanceByContractID"), getAddress(), aContractID, aDelegate, aIID, result);
	}
}

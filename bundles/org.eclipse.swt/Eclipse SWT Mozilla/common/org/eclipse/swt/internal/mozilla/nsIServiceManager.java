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
 * -  Copyright (C) 2003, 2008 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIServiceManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_ISERVICEMANAGER_IID_STR =
		"8bb35ed9-e332-462d-9155-4a002ab5c958";

	public static final nsID NS_ISERVICEMANAGER_IID =
		new nsID(NS_ISERVICEMANAGER_IID_STR);

	public nsIServiceManager(int /*long*/ address) {
		super(address);
	}

	public int GetService(nsID aClass, nsID aIID, int /*long*/[] result) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aClass, aIID, result);
	}

	public int GetServiceByContractID(byte[] aContractID, nsID aIID, int /*long*/[] result) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aContractID, aIID, result);
	}

	public int IsServiceInstantiated(nsID aClass, nsID aIID, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aClass, aIID, _retval);
	}

	public int IsServiceInstantiatedByContractID(byte[] aContractID, nsID aIID, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aContractID, aIID, _retval);
	}
}

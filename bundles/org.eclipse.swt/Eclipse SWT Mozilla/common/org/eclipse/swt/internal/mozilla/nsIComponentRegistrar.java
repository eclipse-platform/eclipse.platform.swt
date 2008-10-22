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

public class nsIComponentRegistrar extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 12;

	public static final String NS_ICOMPONENTREGISTRAR_IID_STR =
		"2417cbfe-65ad-48a6-b4b6-eb84db174392";

	public static final nsID NS_ICOMPONENTREGISTRAR_IID =
		new nsID(NS_ICOMPONENTREGISTRAR_IID_STR);

	public nsIComponentRegistrar(int /*long*/ address) {
		super(address);
	}

	public int AutoRegister(int /*long*/ aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aSpec);
	}

	public int AutoUnregister(int /*long*/ aSpec) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSpec);
	}

	public int RegisterFactory(nsID aClass, byte[] aClassName, byte[] aContractID, int /*long*/ aFactory) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aClass, aClassName, aContractID, aFactory);
	}

	public int UnregisterFactory(nsID aClass, int /*long*/ aFactory) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aClass, aFactory);
	}

	public int RegisterFactoryLocation(nsID aClass, byte[] aClassName, byte[] aContractID, int /*long*/ aFile, byte[] aLoaderStr, byte[] aType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aClass, aClassName, aContractID, aFile, aLoaderStr, aType);
	}

	public int UnregisterFactoryLocation(nsID aClass, int /*long*/ aFile) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aClass, aFile);
	}

	public int IsCIDRegistered(nsID aClass, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aClass, _retval);
	}

	public int IsContractIDRegistered(byte[] aContractID, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aContractID, _retval);
	}

	public int EnumerateCIDs(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), _retval);
	}

	public int EnumerateContractIDs(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), _retval);
	}

	public int CIDToContractID(nsID aClass, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aClass, _retval);
	}

	public int ContractIDToCID(byte[] aContractID, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aContractID, _retval);
	}
}

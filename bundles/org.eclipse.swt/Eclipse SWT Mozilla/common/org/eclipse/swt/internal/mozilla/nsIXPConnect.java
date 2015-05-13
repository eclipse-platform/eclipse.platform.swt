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
 * -  Copyright (C) 2012, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIXPConnect extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 39 : (IsXULRunner24() ? 38 : 27));

	static final String NS_IXPCONNECT_IID_STR = "a995b541-d514-43f1-ac0e-f49746c0b063";
	static final String NS_IXPCONNECT_24_IID_STR = "3bc074e6-2102-40a4-8c84-38b002c9e2f1";
	static final String NS_IXPCONNECT_31_IID_STR = "3d5a6320-8764-11e3-baa7-0800200c9a66";

	static {
		IIDStore.RegisterIID(nsIXPConnect.class, MozillaVersion.VERSION_BASE, new nsID(NS_IXPCONNECT_IID_STR));
		IIDStore.RegisterIID(nsIXPConnect.class, MozillaVersion.VERSION_XR24, new nsID(NS_IXPCONNECT_24_IID_STR));
		IIDStore.RegisterIID(nsIXPConnect.class, MozillaVersion.VERSION_XR31, new nsID(NS_IXPCONNECT_31_IID_STR));
	}

	public nsIXPConnect(long /*int*/ address) {
		super(address);
	}

	public int JSValToVariant(long /*int*/ cx, long /*int*/ aJSVal, long /*int*/[] _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("jSValToVariant"), getAddress(), cx, aJSVal, _retval);
	}

	public int VariantToJS(long /*int*/ ctx, long /*int*/ scope, long /*int*/ value, long /*int*/ _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("variantToJS"), getAddress(), ctx, scope, value, _retval);
	}
}

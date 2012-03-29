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
 * -  Copyright (C) 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIXPConnect extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 27;

	public static final String NS_IXPCONNECT_IID_STR =
		"a995b541-d514-43f1-ac0e-f49746c0b063";

	public static final nsID NS_IXPCONNECT_IID =
		new nsID(NS_IXPCONNECT_IID_STR);

	public nsIXPConnect(int /*long*/ address) {
		super(address);
	}

	public int JSValToVariant(int /*long*/ cx, int /*long*/ aJSVal, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), cx, aJSVal, _retval);
	}

	public int VariantToJS(int /*long*/ ctx, int /*long*/ scope, int /*long*/ value, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 33, getAddress(), ctx, scope, value, _retval);
	}
}

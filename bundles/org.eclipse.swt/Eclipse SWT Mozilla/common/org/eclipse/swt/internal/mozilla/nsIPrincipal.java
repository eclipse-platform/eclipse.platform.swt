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

	static final int LAST_METHOD_ID = nsISerializable.LAST_METHOD_ID + (IsXULRunner17 ? 27 : (IsXULRunner10 ? 26 : 23));

	public static final String NS_IPRINCIPAL_IID_STR =
		"b8268b9a-2403-44ed-81e3-614075c92034";

	public static final nsID NS_IPRINCIPAL_IID =
		new nsID(NS_IPRINCIPAL_IID_STR);

	public static final String NS_IPRINCIPAL_10_IID_STR =
		"b406a2db-e547-4c95-b8e2-ad09ecb54ce0";

	public static final nsID NS_IPRINCIPAL_10_IID =
		new nsID(NS_IPRINCIPAL_10_IID_STR);
	
	public static final String NS_IPRINCIPAL_17_IID_STR =
		"825ffce8-962d-11e1-aef3-8f2b6188709b";

	public static final nsID NS_IPRINCIPAL_17_IID =
		new nsID(NS_IPRINCIPAL_17_IID_STR);

	public nsIPrincipal(long /*int*/ address) {
		super(address);
	}

	public static final int ENABLE_DENIED = 1;
	public static final int ENABLE_UNKNOWN = 2;
	public static final int ENABLE_WITH_USER_PERMISSION = 3;
	public static final int ENABLE_GRANTED = 4;

	public int GetJSPrincipals(long /*int*/ cx, long /*int*/[] _retval) {
		if (IsXULRunner17) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 5 : 4), getAddress(), cx, _retval);
	}
}

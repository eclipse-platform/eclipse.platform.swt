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
 * -  Copyright (C) 2003, 2009 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsISecurityCheckedComponent extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_ISECURITYCHECKEDCOMPONENT_IID_STR =
		"0dad9e8c-a12d-4dcb-9a6f-7d09839356e1";

	public static final nsID NS_ISECURITYCHECKEDCOMPONENT_IID =
		new nsID(NS_ISECURITYCHECKEDCOMPONENT_IID_STR);

	public nsISecurityCheckedComponent(int /*long*/ address) {
		super(address);
	}

	public int CanCreateWrapper(int /*long*/ iid, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), iid, _retval);
	}

	public int CanCallMethod(int /*long*/ iid, char[] methodName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), iid, methodName, _retval);
	}

	public int CanGetProperty(int /*long*/ iid, char[] propertyName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), iid, propertyName, _retval);
	}

	public int CanSetProperty(int /*long*/ iid, char[] propertyName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), iid, propertyName, _retval);
	}
}

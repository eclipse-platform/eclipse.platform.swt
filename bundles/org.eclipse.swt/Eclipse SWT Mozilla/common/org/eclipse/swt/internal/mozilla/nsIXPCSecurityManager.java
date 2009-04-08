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

public class nsIXPCSecurityManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_IXPCSECURITYMANAGER_IID_STR =
		"31431440-f1ce-11d2-985a-006008962422";

	public static final nsID NS_IXPCSECURITYMANAGER_IID =
		new nsID(NS_IXPCSECURITYMANAGER_IID_STR);

	public nsIXPCSecurityManager(int /*long*/ address) {
		super(address);
	}

	public static final int HOOK_CREATE_WRAPPER = 1;

	public static final int HOOK_CREATE_INSTANCE = 2;

	public static final int HOOK_GET_SERVICE = 4;

	public static final int HOOK_CALL_METHOD = 8;

	public static final int HOOK_GET_PROPERTY = 16;

	public static final int HOOK_SET_PROPERTY = 32;

	public static final int HOOK_ALL = 63;

	public int CanCreateWrapper(int /*long*/ aJSContext, nsID aIID, int /*long*/ aObj, int /*long*/ aClassInfo, int /*long*/[] aPolicy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aJSContext, aIID, aObj, aClassInfo, aPolicy);
	}

	public int CanCreateInstance(int /*long*/ aJSContext, nsID aCID) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aJSContext, aCID);
	}

	public int CanGetService(int /*long*/ aJSContext, nsID aCID) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aJSContext, aCID);
	}

	public static final int ACCESS_CALL_METHOD = 0;

	public static final int ACCESS_GET_PROPERTY = 1;

	public static final int ACCESS_SET_PROPERTY = 2;

	public int CanAccess(int aAction, int /*long*/ aCallContext, int /*long*/ aJSContext, int /*long*/ aJSObject, int /*long*/ aObj, int /*long*/ aClassInfo, int /*long*/ aName, int /*long*/[] aPolicy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aAction, aCallContext, aJSContext, aJSObject, aObj, aClassInfo, aName, aPolicy);
	}
}
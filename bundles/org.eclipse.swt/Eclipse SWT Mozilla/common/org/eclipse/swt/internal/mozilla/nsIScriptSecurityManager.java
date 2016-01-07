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


public class nsIScriptSecurityManager extends nsIXPCSecurityManager {

	static final int LAST_METHOD_ID = nsIXPCSecurityManager.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24) ? 21 : (IsXULRunner10() ? 27 : 26));

	static final String NS_ISCRIPTSECURITYMANAGER_IID_STR = "3fffd8e8-3fea-442e-a0ed-2ba81ae197d5";
	static final String NS_ISCRIPTSECURITYMANAGER_191_IID_STR = "f8e350b9-9f31-451a-8c8f-d10fea26b780";
	static final String NS_ISCRIPTSECURITYMANAGER_10_IID_STR = "50eda256-4dd2-4c7c-baed-96983910af9f";
	static final String NS_ISCRIPTSECURITYMANAGER_24_IID_STR = "ae486501-ec57-4ec8-a565-6880ca4ae6c4";

	static {
		IIDStore.RegisterIID(nsIScriptSecurityManager.class, MozillaVersion.VERSION_BASE, new nsID(NS_ISCRIPTSECURITYMANAGER_IID_STR));
		IIDStore.RegisterIID(nsIScriptSecurityManager.class, MozillaVersion.VERSION_XR1_9_1, new nsID(NS_ISCRIPTSECURITYMANAGER_191_IID_STR));
		IIDStore.RegisterIID(nsIScriptSecurityManager.class, MozillaVersion.VERSION_XR10, new nsID(NS_ISCRIPTSECURITYMANAGER_10_IID_STR));
		IIDStore.RegisterIID(nsIScriptSecurityManager.class, MozillaVersion.VERSION_XR24, new nsID(NS_ISCRIPTSECURITYMANAGER_24_IID_STR));
	}

	public nsIScriptSecurityManager(long /*int*/ address) {
		super(address);
	}

	public static final int STANDARD = 0;
	public static final int LOAD_IS_AUTOMATIC_DOCUMENT_REPLACEMENT = 1;
	public static final int ALLOW_CHROME = 2;
	public static final int DISALLOW_INHERIT_PRINCIPAL = 4;
	public static final int DISALLOW_SCRIPT_OR_DATA = 4;
	public static final int DISALLOW_SCRIPT = 8;

	public int GetSystemPrincipal(long /*int*/[] _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("getSystemPrincipal"), getAddress(), _retval);
	}

	public int GetSubjectPrincipal(long /*int*/[] _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("getSubjectPrincipal"), getAddress(), _retval);
	}
	public int GetObjectPrincipal(long /*int*/ cx, long /*int*/ object, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24) ? 14 : (IsXULRunner10() ? 10 : 11)), getAddress(), cx, object, _retval);
	}
//	public int GetSystemPrincipal(long /*int*/[] _retval) {
//		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (IsXULRunner24 ? 8 : (IsXULRunner10 ? 10 : 11)), getAddress(), _retval);
//	}
//	public int GetSystemPrincipal(long /*int*/[] _retval) {
//		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (IsXULRunner24 ? 8 : (IsXULRunner10 ? 10 : 11)), getAddress(), _retval);
//	}

}

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
 * -  Copyright (C) 2014 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIScriptGlobalObject extends nsISupports {

	static final String NS_ISCRIPTGLOBALOBJECT_1_9_IID_STR = "6afecd40-0b9a-4cfd-8c42-0f645cd91829";
	static final String NS_ISCRIPTGLOBALOBJECT_1_9_2_IID_STR = "e9f3f2c1-2d94-4722-bbd4-2bf6fdf42f48";
	static final String NS_ISCRIPTGLOBALOBJECT_10_IID_STR = "08f73284-26e3-4fa6-bf89-8326f92a94b3";
	static final String NS_ISCRIPTGLOBALOBJECT_24_IID_STR = "de24b30a-12c6-4e5f-a85e-90cdfb6c5451";
	static final String NS_ISCRIPTGLOBALOBJECT_31_IID_STR = "876f83bd-6314-460a-a0-45-1c-8f-46-2f-b8-e1";

	static {
		IIDStore.RegisterIID(nsIScriptGlobalObject.class, MozillaVersion.VERSION_BASE, new nsID(NS_ISCRIPTGLOBALOBJECT_1_9_IID_STR));
		IIDStore.RegisterIID(nsIScriptGlobalObject.class, MozillaVersion.VERSION_XR1_9_2, new nsID(NS_ISCRIPTGLOBALOBJECT_1_9_2_IID_STR));
		IIDStore.RegisterIID(nsIScriptGlobalObject.class, MozillaVersion.VERSION_XR10, new nsID(NS_ISCRIPTGLOBALOBJECT_10_IID_STR));
		IIDStore.RegisterIID(nsIScriptGlobalObject.class, MozillaVersion.VERSION_XR24, new nsID(NS_ISCRIPTGLOBALOBJECT_24_IID_STR));
		IIDStore.RegisterIID(nsIScriptGlobalObject.class, MozillaVersion.VERSION_XR31, new nsID(NS_ISCRIPTGLOBALOBJECT_31_IID_STR));
	}

	public nsIScriptGlobalObject(long /*int*/ address) {
		super(address);
	}
}

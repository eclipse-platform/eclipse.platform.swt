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


public class nsIScriptContext extends nsISupports {

	static final String NS_ISCRIPTCONTEXT_1_9_IID_STR = "e7b9871d-3adc-4bf7-850d-7fb9554886bf";
	static final String NS_ISCRIPTCONTEXT_1_9_2_IID_STR = "87482b5e-e019-4df5-9bc2-b2a51b1f2d28";
	static final String NS_ISCRIPTCONTEXT_10_IID_STR = "2e583bf4-3c1f-432d-8283-8dee7eccc88b";
	static final String NS_ISCRIPTCONTEXT_24_IID_STR = "ef0c91ce-14f6-41c9-a577-a6ebdc6d447b";
	static final String NS_ISCRIPTCONTEXT_31_IID_STR = "274840b6-7349-4798-be-24-bd-75-a6-46-99-b7";

	static {
		IIDStore.RegisterIID(nsIScriptContext.class, MozillaVersion.VERSION_BASE, new nsID(NS_ISCRIPTCONTEXT_1_9_IID_STR));
		IIDStore.RegisterIID(nsIScriptContext.class, MozillaVersion.VERSION_XR1_9_2, new nsID(NS_ISCRIPTCONTEXT_1_9_2_IID_STR));
		IIDStore.RegisterIID(nsIScriptContext.class, MozillaVersion.VERSION_XR10, new nsID(NS_ISCRIPTCONTEXT_10_IID_STR));
		IIDStore.RegisterIID(nsIScriptContext.class, MozillaVersion.VERSION_XR24, new nsID(NS_ISCRIPTCONTEXT_24_IID_STR));
		IIDStore.RegisterIID(nsIScriptContext.class, MozillaVersion.VERSION_XR31, new nsID(NS_ISCRIPTCONTEXT_31_IID_STR));
	}
	
	public nsIScriptContext(long /*int*/ address) {
		super(address);
	}
}

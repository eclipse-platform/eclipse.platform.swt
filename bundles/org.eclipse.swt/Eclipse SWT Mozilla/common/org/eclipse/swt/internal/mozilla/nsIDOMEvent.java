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


public class nsIDOMEvent extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24) ? 25 : (IsXULRunner10() ? 12 : 10));

	static final String NS_IDOMEVENT_IID_STR = "a66b7b80-ff46-bd97-0080-5f8ae38add32";
	static final String NS_IDOMEVENT_10_IID_STR = "e85cff74-951f-45c1-be0c-89442ea2f500";
	static final String NS_IDOMEVENT_24_IID_STR = "02d54f52-a1f5-4ad2-b560-36f14012935e";

	static {
		IIDStore.RegisterIID(nsIDOMEvent.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOMEVENT_IID_STR));
		IIDStore.RegisterIID(nsIDOMEvent.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOMEVENT_10_IID_STR));
		IIDStore.RegisterIID(nsIDOMEvent.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOMEVENT_24_IID_STR));
	}

	public nsIDOMEvent(long /*int*/ address) {
		super(address);
	}

	public static final int CAPTURING_PHASE = 1;
	public static final int AT_TARGET = 2;
	public static final int BUBBLING_PHASE = 3;

	public int GetType(long /*int*/ aType) {
		return XPCOM.VtblCall(this.getGetterIndex("type"), getAddress(), aType);
	}

	public int GetCurrentTarget(long /*int*/[] aCurrentTarget) {
		return XPCOM.VtblCall(this.getGetterIndex("currentTarget"), getAddress(), aCurrentTarget);
	}

	public int PreventDefault() {
		return XPCOM.VtblCall(this.getMethodIndex("preventDefault"), getAddress());
	}
}

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


public class nsIDOMUIEvent extends nsIDOMEvent {

	static final int LAST_METHOD_ID = nsIDOMEvent.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR10) ? 13 : 3);

	static final String NS_IDOMUIEVENT_IID_STR = "a6cf90c3-15b3-11d2-932e-00805f8add32";
	static final String NS_IDOMUIEVENT_10_IID_STR = "af3f130e-0c22-4613-a150-780a46c22e3a";
	static final String NS_IDOMUIEVENT_24_IID_STR = "d73852f8-7bd6-477d-8233-117dbf83860b";

	static {
		IIDStore.RegisterIID(nsIDOMUIEvent.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOMUIEVENT_IID_STR));
		IIDStore.RegisterIID(nsIDOMUIEvent.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOMUIEVENT_10_IID_STR));
		IIDStore.RegisterIID(nsIDOMUIEvent.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOMUIEVENT_24_IID_STR));
	}

	public nsIDOMUIEvent(long /*int*/ address) {
		super(address);
	}

	public int GetDetail(int[] aDetail) {
		return XPCOM.VtblCall(this.getGetterIndex("detail"), getAddress(), aDetail);
	}

	/* the following constants are defined in Mozilla 10 */
	public static final int SCROLL_PAGE_UP = -32768;
	public static final int SCROLL_PAGE_DOWN = 32768;
}

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


public class nsIDOMWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 144 : (IsXULRunner24() ? 140 : (IsXULRunner10() ? 129 : 17)));

	static final String NS_IDOMWINDOW_IID_STR = "a6cf906b-15b3-11d2-932e-00805f8add32";
	static final String NS_IDOMWINDOW_10_IID_STR = "8f577294-d572-4473-94b1-d2c5a74a2a74";
	static final String NS_IDOMWINDOW_24_IID_STR = "be62660a-e3f6-409c-a4a9-378364a9526f";
	static final String NS_IDOMWINDOW_31_IID_STR = "1b4a23a2-2ccf-4690-9da7-f3a7a8308381";

	static {
		IIDStore.RegisterIID(nsIDOMWindow.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOMWINDOW_IID_STR));
		IIDStore.RegisterIID(nsIDOMWindow.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOMWINDOW_10_IID_STR));
		IIDStore.RegisterIID(nsIDOMWindow.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOMWINDOW_24_IID_STR));
		IIDStore.RegisterIID(nsIDOMWindow.class, MozillaVersion.VERSION_XR31, new nsID(NS_IDOMWINDOW_31_IID_STR));
	}

	public nsIDOMWindow(long /*int*/ address) {
		super(address);
	}

	public int GetDocument(long /*int*/[] aDocument) {
		return XPCOM.VtblCall(this.getGetterIndex("document"), getAddress(), aDocument);
	}

	public int GetTop(long /*int*/[] aTop) {
		if (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24)) return GetRealTop(aTop);
		return XPCOM.VtblCall(this.getGetterIndex("top"), getAddress(), aTop);
	}

	public int GetRealTop(long /*int*/[] aTop) {
		if (!IsXULRVersionOrLater(MozillaVersion.VERSION_XR24)) return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
		return XPCOM.VtblCall(this.getGetterIndex("realTop"), getAddress(), aTop);
	}

	public int GetFrames(long /*int*/[] aFrames) {
		return XPCOM.VtblCall(this.getGetterIndex("frames"), getAddress(), aFrames);
	}
}

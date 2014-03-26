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

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner24() ? 140 : (IsXULRunner10() ? 129 : 17));

	static final String NS_IDOMWINDOW_IID_STR = "a6cf906b-15b3-11d2-932e-00805f8add32";
	static final String NS_IDOMWINDOW_10_IID_STR = "8f577294-d572-4473-94b1-d2c5a74a2a74";
	static final String NS_IDOMWINDOW_24_IID_STR = "be62660a-e3f6-409c-a4a9-378364a9526f";

	static {
		IIDStore.RegisterIID(nsIDOMWindow.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOMWINDOW_IID_STR));
		IIDStore.RegisterIID(nsIDOMWindow.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOMWINDOW_10_IID_STR));
		IIDStore.RegisterIID(nsIDOMWindow.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOMWINDOW_24_IID_STR));
	}

	public nsIDOMWindow(long /*int*/ address) {
		super(address);
	}

	public int GetDocument(long /*int*/[] aDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + ((IsXULRunner10() || IsXULRunner24()) ? 3 : 1), getAddress(), aDocument);
	}

	public int GetTop(long /*int*/[] aTop) {
		if (IsXULRunner24()) return GetRealTop(aTop);
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10() ? 21 : 3), getAddress(), aTop);
	}
	
	public int GetRealTop(long /*int*/[] aTop) {
		if (!IsXULRunner24()) return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), aTop);
	}
	
	public int GetFrames(long /*int*/[] aFrames) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner24() ? 66 : (IsXULRunner10() ? 62 : 5)), getAddress(), aFrames);
	}
}

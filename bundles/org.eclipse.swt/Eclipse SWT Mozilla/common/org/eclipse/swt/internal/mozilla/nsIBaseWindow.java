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

public class nsIBaseWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner17 ? 23 : (IsXULRunner10 ? 22 : 24));

	public static final String NS_IBASEWINDOW_IID_STR =
		"046bc8a0-8015-11d3-af70-00a024ffc08c";

	public static final String NS_IBASEWINDOW_10_IID_STR =
		"7144ac8b-6702-4a4b-a73d-d1d4e9717e46";

	public static final nsID NS_IBASEWINDOW_IID =
		new nsID(NS_IBASEWINDOW_IID_STR);

	public static final nsID NS_IBASEWINDOW_10_IID =
		new nsID(NS_IBASEWINDOW_10_IID_STR);
		
	public nsIBaseWindow(long /*int*/ address) {
		super(address);
	}

	public int InitWindow(long /*int*/ parentNativeWindow, long /*int*/ parentWidget, int x, int y, int cx, int cy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), parentNativeWindow, parentWidget, x, y, cx, cy);
	}

	public int Create() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress());
	}

	public int Destroy() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}

	public int SetPositionAndSize(int x, int y, int cx, int cy, int fRepaint) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), x, y, cx, cy, fRepaint);
	}

	public int GetParentNativeWindow(long /*int*/[] aParentNativeWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aParentNativeWindow);
	}

	public int SetVisibility(int aVisibility) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner17 ? 17 : 16), getAddress(), aVisibility);
	}

	public int SetFocus() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner17 ? 21 : (IsXULRunner10 ? 20 : 22)), getAddress());
	}
}

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
 * -  Copyright (C) 2003, 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIEmbeddingSiteWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 8;

	public static final String NS_IEMBEDDINGSITEWINDOW_IID_STR =
		"3e5432cd-9568-4bd1-8cbe-d50aba110743";

	public static final nsID NS_IEMBEDDINGSITEWINDOW_IID =
		new nsID(NS_IEMBEDDINGSITEWINDOW_IID_STR);

	public nsIEmbeddingSiteWindow(long /*int*/ address) {
		super(address);
	}

	public static final int DIM_FLAGS_POSITION = 1;
	public static final int DIM_FLAGS_SIZE_INNER = 2;
	public static final int DIM_FLAGS_SIZE_OUTER = 4;

	public int SetDimensions(int flags, int x, int y, int cx, int cy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), flags, x, y, cx, cy);
	}

	public int GetDimensions(int flags, int[] x, int[] y, int[] cx, int[] cy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), flags, x, y, cx, cy);
	}

	public int SetFocus() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}

	public int GetVisibility(int[] aVisibility) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aVisibility);
	}

	public int SetVisibility(int aVisibility) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aVisibility);
	}

	public int GetTitle(long /*int*/[] aTitle) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aTitle);
	}

	public int SetTitle(char[] aTitle) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aTitle);
	}

	public int GetSiteWindow(long /*int*/[] aSiteWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aSiteWindow);
	}
}

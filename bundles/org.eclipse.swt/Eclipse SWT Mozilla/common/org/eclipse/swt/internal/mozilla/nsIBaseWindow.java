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
 * -  Copyright (C) 2003, 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIBaseWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 22 : 24);

	public static final String NS_IBASEWINDOW_IID_STR =
		"046bc8a0-8015-11d3-af70-00a024ffc08c";

	public static final String NS_IBASEWINDOW_10_IID_STR =
		"7144ac8b-6702-4a4b-a73d-d1d4e9717e46";

	public static final nsID NS_IBASEWINDOW_IID =
		new nsID(NS_IBASEWINDOW_IID_STR);

	public static final nsID NS_IBASEWINDOW_10_IID =
		new nsID(NS_IBASEWINDOW_10_IID_STR);
		
	public nsIBaseWindow(int /*long*/ address) {
		super(address);
	}

	public int InitWindow(int /*long*/ parentNativeWindow, int /*long*/ parentWidget, int x, int y, int cx, int cy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), parentNativeWindow, parentWidget, x, y, cx, cy);
	}

	public int Create() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress());
	}

	public int Destroy() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}

	public int SetPosition(int x, int y) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), x, y);
	}

	public int GetPosition(int[] x, int[] y) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), x, y);
	}

	public int SetSize(int cx, int cy, int fRepaint) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), cx, cy, fRepaint);
	}

	public int GetSize(int[] cx, int[] cy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), cx, cy);
	}

	public int SetPositionAndSize(int x, int y, int cx, int cy, int fRepaint) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), x, y, cx, cy, fRepaint);
	}

	public int GetPositionAndSize(int[] x, int[] y, int[] cx, int[] cy) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), x, y, cx, cy);
	}

	public int Repaint(int force) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), force);
	}

	public int GetParentWidget(int /*long*/[] aParentWidget) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aParentWidget);
	}

	public int SetParentWidget(int /*long*/ aParentWidget) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aParentWidget);
	}

	public int GetParentNativeWindow(int /*long*/[] aParentNativeWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aParentNativeWindow);
	}

	public int SetParentNativeWindow(int /*long*/ aParentNativeWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aParentNativeWindow);
	}

	public int GetVisibility(int[] aVisibility) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aVisibility);
	}

	public int SetVisibility(int aVisibility) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aVisibility);
	}

	public int GetEnabled(int[] aEnabled) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aEnabled);
	}

	public int SetEnabled(int aEnabled) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aEnabled);
	}

	public int GetBlurSuppression(int[] aBlurSuppression) {
		if (IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), aBlurSuppression);
	}

	public int SetBlurSuppression(int aBlurSuppression) {
		if (IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), aBlurSuppression);
	}

	public int GetMainWidget(int /*long*/[] aMainWidget) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 19 : 21), getAddress(), aMainWidget);
	}

	public int SetFocus() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 20 : 22), getAddress());
	}

	public int GetTitle(int /*long*/[] aTitle) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 21 : 23), getAddress(), aTitle);
	}

	public int SetTitle(char[] aTitle) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 22 : 24), getAddress(), aTitle);
	}
}

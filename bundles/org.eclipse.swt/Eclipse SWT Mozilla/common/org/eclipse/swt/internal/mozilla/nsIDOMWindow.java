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
 * -  Copyright (C) 2003, 2005 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIDOMWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 17;

	public static final String NS_IDOMWINDOW_IID_STR =
		"a6cf906b-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMWINDOW_IID =
		new nsID(NS_IDOMWINDOW_IID_STR);

	public nsIDOMWindow(int /*long*/ address) {
		super(address);
	}

	public int GetDocument(int /*long*/[] aDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aDocument);
	}

	public int GetParent(int /*long*/[] aParent) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aParent);
	}

	public int GetTop(int /*long*/[] aTop) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aTop);
	}

	public int GetScrollbars(int /*long*/[] aScrollbars) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aScrollbars);
	}

	public int GetFrames(int /*long*/[] aFrames) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aFrames);
	}

	public int GetName(int /*long*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aName);
	}

	public int SetName(int /*long*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aName);
	}

	public int GetTextZoom(float[] aTextZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aTextZoom);
	}

	public int SetTextZoom(float aTextZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aTextZoom);
	}

	public int GetScrollX(int[] aScrollX) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aScrollX);
	}

	public int GetScrollY(int[] aScrollY) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aScrollY);
	}

	public int ScrollTo(int xScroll, int yScroll) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), xScroll, yScroll);
	}

	public int ScrollBy(int xScrollDif, int yScrollDif) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), xScrollDif, yScrollDif);
	}

	public int GetSelection(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), _retval);
	}

	public int ScrollByLines(int numLines) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), numLines);
	}

	public int ScrollByPages(int numPages) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), numPages);
	}

	public int SizeToContent() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress());
	}
}
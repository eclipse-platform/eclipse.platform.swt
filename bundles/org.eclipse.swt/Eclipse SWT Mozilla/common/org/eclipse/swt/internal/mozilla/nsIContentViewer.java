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
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIContentViewer extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 22;

	public static final String NS_ICONTENTVIEWER_IID_STRING =
		"a6cf9056-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_ICONTENTVIEWER_IID =
		new nsID(NS_ICONTENTVIEWER_IID_STRING);

	public nsIContentViewer(int address) {
		super(address);
	}

	public int Init(int aParentWidget, int aDeviceContext, int aBounds) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aParentWidget, aDeviceContext, aBounds);
	}

	public int GetContainer(int[] aContainer) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aContainer);
	}

	public int SetContainer(int aContainer) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aContainer);
	}

	public int LoadStart(int aDoc) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aDoc);
	}

	public int LoadComplete(int aStatus) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aStatus);
	}

	public int Unload() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress());
	}

	public int Close() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress());
	}

	public int Destroy() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress());
	}

	public int Stop() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress());
	}

	public int GetDOMDocument(int[] aDOMDocument) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aDOMDocument);
	}

	public int SetDOMDocument(int aDOMDocument) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aDOMDocument);
	}

	public int GetBounds(int aBounds) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aBounds);
	}

	public int SetBounds(int aBounds) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), aBounds);
	}

	public int GetPreviousViewer(int[] aPreviousViewer) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), aPreviousViewer);
	}

	public int SetPreviousViewer(int aPreviousViewer) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aPreviousViewer);
	}

	public int Move(int aX, int aY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), aX, aY);
	}

	public int Show() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress());
	}

	public int Hide() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress());
	}

	public int GetEnableRendering(boolean[] aEnableRendering) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), aEnableRendering);
	}

	public int SetEnableRendering(boolean aEnableRendering) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), aEnableRendering);
	}

	public int GetSticky(boolean[] aSticky) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), aSticky);
	}

	public int SetSticky(boolean aSticky) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), aSticky);
	}
}
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

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10() ? 22 : 24);

	static final String NS_IBASEWINDOW_IID_STR ="046bc8a0-8015-11d3-af70-00a024ffc08c";
	static final String NS_IBASEWINDOW_10_IID_STR = "7144ac8b-6702-4a4b-a73d-d1d4e9717e46";
	static final String NS_IBASEWINDOW_24_IID_STR = "9da319f3-eee6-4504-81a5-6a19cf6215bf";

	static {
		IIDStore.RegisterIID(nsIBaseWindow.class, MozillaVersion.VERSION_BASE, new nsID(NS_IBASEWINDOW_IID_STR));
		IIDStore.RegisterIID(nsIBaseWindow.class, MozillaVersion.VERSION_XR10, new nsID(NS_IBASEWINDOW_10_IID_STR));
		IIDStore.RegisterIID(nsIBaseWindow.class, MozillaVersion.VERSION_XR24, new nsID(NS_IBASEWINDOW_24_IID_STR));
	}

	public nsIBaseWindow(long /*int*/ address) {
		super(address);
	}

	public int InitWindow(long /*int*/ parentNativeWindow, long /*int*/ parentWidget, int x, int y, int cx, int cy) {
		return XPCOM.VtblCall(this.getMethodIndex("initWindow"), getAddress(), parentNativeWindow, parentWidget, x, y, cx, cy);
	}

	public int Create() {
		return XPCOM.VtblCall(this.getMethodIndex("create"), getAddress());
	}

	public int Destroy() {
		return XPCOM.VtblCall(this.getMethodIndex("destroy"), getAddress());
	}

	public int SetPositionAndSize(int x, int y, int cx, int cy, int fRepaint) {
		return XPCOM.VtblCall(this.getMethodIndex("setPositionAndSize"), getAddress(), x, y, cx, cy, fRepaint);
	}

	public int GetParentNativeWindow(long /*int*/[] aParentNativeWindow) {
		return XPCOM.VtblCall(this.getGetterIndex("parentNativeWindow"), getAddress(), aParentNativeWindow);
	}

	public int SetVisibility(int aVisibility) {
		return XPCOM.VtblCall(this.getSetterIndex("visibility"), getAddress(), aVisibility);
	}

	public int SetFocus() {
		return XPCOM.VtblCall(this.getMethodIndex("setFocus"), getAddress());
	}
}

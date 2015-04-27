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
 * -  Copyright (C) 2011, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIFocusManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR10) ? 18 : 17);

	static final String NS_IFOCUSMANAGER_IID_STR = "cd6040a8-243f-412a-8a16-0bf2aa1083b9";
	static final String NS_IFOCUSMANAGER_10_IID_STR = "51db277b-7ee7-4bce-9b84-fd2efcd2c8bd";

	static {
		IIDStore.RegisterIID(nsIFocusManager.class, MozillaVersion.VERSION_BASE, new nsID(NS_IFOCUSMANAGER_IID_STR));
		IIDStore.RegisterIID(nsIFocusManager.class, MozillaVersion.VERSION_XR10, new nsID(NS_IFOCUSMANAGER_10_IID_STR));
	}

	public nsIFocusManager(long /*int*/ address) {
		super(address);
	}

	public int GetFocusedElement(long /*int*/[] aFocusedElement) {
		return XPCOM.VtblCall(this.getGetterIndex("focusedElement"), getAddress(), aFocusedElement);
	}

	public int SetFocus(long /*int*/ aElement, int aFlags) {
		return XPCOM.VtblCall(this.getMethodIndex("setFocus"), getAddress(), aElement, aFlags);
	}

	public static final int FLAG_RAISE = 1;
	public static final int FLAG_NOSCROLL = 2;
	public static final int FLAG_NOSWITCHFRAME = 4;
	public static final int FLAG_BYMOUSE = 4096;
	public static final int FLAG_BYKEY = 8192;
	public static final int FLAG_BYMOVEFOCUS = 16384;
	public static final int MOVEFOCUS_FORWARD = 1;
	public static final int MOVEFOCUS_BACKWARD = 2;
	public static final int MOVEFOCUS_FORWARDDOC = 3;
	public static final int MOVEFOCUS_BACKWARDDOC = 4;
	public static final int MOVEFOCUS_FIRST = 5;
	public static final int MOVEFOCUS_LAST = 6;
	public static final int MOVEFOCUS_ROOT = 7;
	public static final int MOVEFOCUS_CARET = 8;
}

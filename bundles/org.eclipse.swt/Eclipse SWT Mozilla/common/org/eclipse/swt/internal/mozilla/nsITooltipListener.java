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
 * -  Copyright (C) 2004, 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsITooltipListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_ITOOLTIPLISTENER_IID_STR =
		"44b78386-1dd2-11b2-9ad2-e4eee2ca1916";

	public static final nsID NS_ITOOLTIPLISTENER_IID =
		new nsID(NS_ITOOLTIPLISTENER_IID_STR);

	public nsITooltipListener(int /*long*/ address) {
		super(address);
	}

	public int OnShowTooltip(int aXCoords, int aYCoords, char[] aTipText) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aXCoords, aYCoords, aTipText);
	}

	public int OnHideTooltip() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress());
	}
}

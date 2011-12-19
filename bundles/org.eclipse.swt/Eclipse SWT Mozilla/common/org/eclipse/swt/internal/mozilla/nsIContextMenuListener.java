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

public class nsIContextMenuListener extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 1;

	public static final String NS_ICONTEXTMENULISTENER_IID_STR =
		"3478b6b0-3875-11d4-94ef-0020183bf181";

	public static final nsID NS_ICONTEXTMENULISTENER_IID =
		new nsID(NS_ICONTEXTMENULISTENER_IID_STR);

	public nsIContextMenuListener(int /*long*/ address) {
		super(address);
	}

	public static final int CONTEXT_NONE = 0;
	public static final int CONTEXT_LINK = 1;
	public static final int CONTEXT_IMAGE = 2;
	public static final int CONTEXT_DOCUMENT = 4;
	public static final int CONTEXT_TEXT = 8;
	public static final int CONTEXT_INPUT = 16;

	public int OnShowContextMenu(int aContextFlags, int /*long*/ aEvent, int /*long*/ aNode) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aContextFlags, aEvent, aNode);
	}
}

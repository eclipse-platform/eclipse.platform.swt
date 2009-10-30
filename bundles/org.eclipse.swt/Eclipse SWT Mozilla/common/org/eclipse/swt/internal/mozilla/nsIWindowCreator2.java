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
 * -  Copyright (C) 2003, 2008 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIWindowCreator2 extends nsIWindowCreator {

	static final int LAST_METHOD_ID = nsIWindowCreator.LAST_METHOD_ID + 1;

	public static final String NS_IWINDOWCREATOR2_IID_STR =
		"f673ec81-a4b0-11d6-964b-eb5a2bf216fc";

	public static final nsID NS_IWINDOWCREATOR2_IID =
		new nsID(NS_IWINDOWCREATOR2_IID_STR);

	public nsIWindowCreator2(int /*long*/ address) {
		super(address);
	}

	public static final int PARENT_IS_LOADING_OR_RUNNING_TIMEOUT = 1;

	public int CreateChromeWindow2(int /*long*/ parent, int chromeFlags, int contextFlags, int /*long*/ uri, int[] cancel, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIWindowCreator.LAST_METHOD_ID + 1, getAddress(), parent, chromeFlags, contextFlags, uri, cancel, _retval);
	}
}

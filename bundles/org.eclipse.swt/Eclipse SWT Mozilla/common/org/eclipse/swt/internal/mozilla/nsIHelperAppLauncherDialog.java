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

public class nsIHelperAppLauncherDialog extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_IHELPERAPPLAUNCHERDIALOG_IID_STR =
		"d7ebddf0-4c84-11d4-807a-00600811a9c3";

	public static final nsID NS_IHELPERAPPLAUNCHERDIALOG_IID =
		new nsID(NS_IHELPERAPPLAUNCHERDIALOG_IID_STR);

	public nsIHelperAppLauncherDialog(int /*long*/ address) {
		super(address);
	}

	public int Show(int /*long*/ aLauncher, int /*long*/ aContext) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aLauncher, aContext);
	}

	public int PromptForSaveToFile(int /*long*/ aWindowContext, char[] aDefaultFile, char[] aSuggestedFileExtension, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aWindowContext, aDefaultFile, aSuggestedFileExtension, _retval);
	}

	public int ShowProgressDialog(int /*long*/ aLauncher, int /*long*/ aContext) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aLauncher, aContext);
	}
}

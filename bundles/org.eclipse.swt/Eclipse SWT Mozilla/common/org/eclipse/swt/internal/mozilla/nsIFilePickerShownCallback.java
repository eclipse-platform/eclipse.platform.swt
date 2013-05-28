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
 * -  Copyright (C) 2012, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIFilePickerShownCallback extends nsISupports {

	public static final String NS_IFILEPICKER_IID_STR =
		"0d79adad-b244-49A5-9997-2a8cad93fc44";

	public static final nsID NS_IFILEPICKER_IID =
		new nsID (NS_IFILEPICKER_IID_STR);

	public nsIFilePickerShownCallback (long /*int*/ address) {
		super (address);
	}

	public int Done (int aResult) {
		return XPCOM.VtblCall (nsISupports.LAST_METHOD_ID + 1, getAddress(), aResult);
	}
}

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

public class nsIDOMEventTarget_1_9 extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_IDOMEVENTTARGET_IID_STR =
		"1c773b30-d1cf-11d2-bd95-00805f8ae3f4";

	public static final nsID NS_IDOMEVENTTARGET_IID =
		new nsID(NS_IDOMEVENTTARGET_IID_STR);

	public nsIDOMEventTarget_1_9(int /*long*/ address) {
		super(address);
	}

	public int AddEventListener(int /*long*/ type, int /*long*/ listener, int useCapture) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), type, listener, useCapture);
	}

	public int RemoveEventListener(int /*long*/ type, int /*long*/ listener, int useCapture) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), type, listener, useCapture);
	}

	public int DispatchEvent(int /*long*/ evt, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), evt, _retval);
	}
}

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

public class nsIControllers extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 11;

	public static final String NS_ICONTROLLERS_IID_STRING =
		"A5ED3A01-7CC7-11d3-BF87-00105A1B0627";

	public static final nsID NS_ICONTROLLERS_IID =
		new nsID(NS_ICONTROLLERS_IID_STRING);

	public nsIControllers(int address) {
		super(address);
	}

	public int GetCommandDispatcher(int[] aCommandDispatcher) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1,getAddress(), aCommandDispatcher);
	}

	public int SetCommandDispatcher(int aCommandDispatcher) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2,getAddress(), aCommandDispatcher);
	}

	public int GetControllerForCommand(byte[] command, int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), command, retVal);
	}

	public int InsertControllerAt(int index, int controller) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), index, controller);
	}

	public int RemoveControllerAt(int index, int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), index, retVal);
	}

	public int GetControllerAt(int index, int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), index, retVal);
	}

	public int AppendController(int controller) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), controller);
	}

	public int RemoveController(int controller) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), controller);
	}

	public int GetControllerId(int controller, int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), controller, retVal);
	}

	public int GetControllerById(int controllerID, int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), controllerID, retVal);
	}

	public int GetControllerCount(int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), retVal);
	}
}
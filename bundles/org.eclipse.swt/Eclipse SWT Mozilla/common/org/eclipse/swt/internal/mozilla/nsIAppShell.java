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

public class nsIAppShell extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 8;

	public static final String NS_IAPPSHELL_IID_STR =
		"a0757c31-eeac-11d1-9ec1-00aa002fb821";

	public static final nsID NS_IAPPSHELL_IID =
		new nsID(NS_IAPPSHELL_IID_STR);

	public nsIAppShell(int /*long*/ address) {
		super(address);
	}

	public int Create(int /*long*/ argc, int /*long*/[] argv) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), argc, argv);
	}

	public int Run() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress());
	}

	public int Spinup() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress());
	}

	public int Spindown() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress());
	}

	public int ListenToEventQueue(int /*long*/ aQueue, int aListen) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aQueue, aListen);
	}

	public int GetNativeEvent(int /*long*/ aRealEvent, int /*long*/[] aEvent) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aRealEvent, aEvent);
	}

	public int DispatchNativeEvent(int aRealEvent, int /*long*/ aEvent) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aRealEvent, aEvent);
	}

	public int Exit() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress());
	}
}

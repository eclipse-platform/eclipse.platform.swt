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

public class nsIEventSink extends nsISupports {
	
	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 4;

	public static final String NS_IEVENTSINK_IID_STRING =
		"C0D3A7C8-1DD1-11B2-8903-ADCD22D004AB";

	public static final nsID NS_IEVENTSINK_IID =
		new nsID(NS_IEVENTSINK_IID_STRING);
		
	public nsIEventSink(int address) {
		super(address);
	}
		
	public int DispatchEvent(int anEvent, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), anEvent, _retval);
	}
	
	public int DragEvent(int aMessage, short aMouseGlobalX, short aMouseGlobalY, short aKeyModifiers, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aMessage, aMouseGlobalX, aMouseGlobalY, aKeyModifiers, _retval);
	}
	
	public int Scroll(boolean aVertical, short aNumLines, short aMouseLocalX, short aMouseLocalY, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aVertical, aNumLines, aMouseLocalX, aMouseLocalY, _retval);

	}
	
	public int Idle() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress());
	}
}
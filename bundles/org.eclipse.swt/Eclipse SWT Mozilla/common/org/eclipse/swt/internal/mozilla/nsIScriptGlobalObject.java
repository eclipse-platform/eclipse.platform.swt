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

public class nsIScriptGlobalObject extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	public static final String NS_ISCRIPTGLOBALOBJECT_IID_STRING =
		"2b16fc80-fa41-11d1-9bc3-0060088ca6b3";

	public static final nsID NS_ISCRIPTGLOBALOBJECT_IID =
		new nsID(NS_ISCRIPTGLOBALOBJECT_IID_STRING);

	public nsIScriptGlobalObject(int address) {
		super(address);
	}

	public int SetContext(int[] aContext) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aContext);
	}

	public int GetContext(int[] aContext) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aContext);
	}

	public int SetNewDocument(int[] aDocument, boolean aRemoveEventListeners, boolean aClearScope) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int SetDocShell(int[] aDocShell) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aDocShell);
	}

	public int GetDocShell(int[] aDocShell) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aDocShell);
	}

	public int SetOpenerWindow(int aOpener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aOpener);
	}
}
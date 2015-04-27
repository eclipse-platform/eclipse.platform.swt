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
 * -  Copyright (C) 2003, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIDOMEventTarget extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24) ? 5 : 3);

	static final String NS_IDOMEVENTTARGET_IID_STR = "1c773b30-d1cf-11d2-bd95-00805f8ae3f4";
	static final String NS_IDOMEVENTTARGET_10_IID_STR = "1797d5a4-b12a-428d-9eef-a0e13839728c";
	static final String NS_IDOMEVENTTARGET_24_IID_STR = "31e92e56-4d23-4a4a-9cfe-a6d12cf434bc";
	static final String NS_IDOMEVENTTARGET_31_IID_STR = "b128448c-7b53-4769-92cb-cd6eafee676c";

	static {
		IIDStore.RegisterIID(nsIDOMEventTarget.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOMEVENTTARGET_IID_STR));
		IIDStore.RegisterIID(nsIDOMEventTarget.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOMEVENTTARGET_10_IID_STR));
		IIDStore.RegisterIID(nsIDOMEventTarget.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOMEVENTTARGET_24_IID_STR));
		IIDStore.RegisterIID(nsIDOMEventTarget.class, MozillaVersion.VERSION_XR31, new nsID(NS_IDOMEVENTTARGET_31_IID_STR));
	}

	public nsIDOMEventTarget(long /*int*/ address) {
		super(address);
	}

	public int AddEventListener(long /*int*/ type, long /*int*/ listener, int useCapture) {
		if (IsXULRVersionOrLater(MozillaVersion.VERSION_XR10)) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(this.getMethodIndex("addEventListener"), getAddress(), type, listener, useCapture);
	}

	public int AddEventListener(long /*int*/ type, long /*int*/ listener, int useCapture, int wantsUntrusted, int _argc) {
		if (!IsXULRVersionOrLater(MozillaVersion.VERSION_XR10)) return AddEventListener(type, listener, useCapture);
		return XPCOM.VtblCall(this.getMethodIndex("addEventListener"), getAddress(), type, listener, useCapture, wantsUntrusted, _argc);
	}

	public int RemoveEventListener(long /*int*/ type, long /*int*/ listener, int useCapture) {
		return XPCOM.VtblCall(this.getMethodIndex("removeEventListener"), getAddress(), type, listener, useCapture);
	}
}

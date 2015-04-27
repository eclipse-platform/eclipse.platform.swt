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
 * -  Copyright (C) 2003, 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIMemory extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24) ? 6 : 5);

	static final String NS_IMEMORY_IID_STR = "59e7e77a-38e4-11d4-8cf5-0060b0fc14a3";
	static final String NS_IMEMORY_24_IID_STR = "6aef11c4-8615-44a6-9711-98f43805693d";

	static {
		IIDStore.RegisterIID(nsIMemory.class, MozillaVersion.VERSION_BASE, new nsID(NS_IMEMORY_IID_STR));
		IIDStore.RegisterIID(nsIMemory.class, MozillaVersion.VERSION_XR24, new nsID(NS_IMEMORY_24_IID_STR));
	}

	public nsIMemory(long /*int*/ address) {
		super(address);
	}

	public long /*int*/ Alloc(int size) {
		return XPCOM.nsIMemory_Alloc(getAddress(), size);
	}

	public int Free(long /*int*/ ptr) {
		return XPCOM.VtblCall(this.getMethodIndex("free"), getAddress(), ptr);
	}
}

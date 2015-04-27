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


public class nsITransfer extends nsIWebProgressListener2 {

	static final int LAST_METHOD_ID = nsIWebProgressListener2.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24) ? 2 : 1);

	static final String NS_ITRANSFER_IID_STR = "23c51569-e9a1-4a92-adeb-3723db82ef7c";
	static final String NS_ITRANSFER_24_IID_STR = "b1c81100-9d66-11e2-9e96-0800200c9a66";

	static {
		IIDStore.RegisterIID(nsITransfer.class, MozillaVersion.VERSION_BASE, new nsID(NS_ITRANSFER_IID_STR));
		IIDStore.RegisterIID(nsITransfer.class, MozillaVersion.VERSION_XR24, new nsID(NS_ITRANSFER_24_IID_STR));
	}

	public nsITransfer(long /*int*/ address) {
		super(address);
	}
}


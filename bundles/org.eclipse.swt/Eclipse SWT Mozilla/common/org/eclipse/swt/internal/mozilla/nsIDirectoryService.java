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

public class nsIDirectoryService extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 3;

	public static final String NS_IDIRECTORYSERVICE_IID_STR =
		"57a66a60-d43a-11d3-8cc2-00609792278c";

	public static final nsID NS_IDIRECTORYSERVICE_IID =
		new nsID(NS_IDIRECTORYSERVICE_IID_STR);

	public nsIDirectoryService(long /*int*/ address) {
		super(address);
	}

	public int Init() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress());
	}

	public int RegisterProvider(long /*int*/ prov) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), prov);
	}

	public int UnregisterProvider(long /*int*/ prov) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), prov);
	}
}

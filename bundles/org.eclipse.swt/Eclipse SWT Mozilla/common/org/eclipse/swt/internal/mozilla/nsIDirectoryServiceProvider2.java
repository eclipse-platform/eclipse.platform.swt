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
 * -  Copyright (C) 2003, 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIDirectoryServiceProvider2 extends nsIDirectoryServiceProvider {

	static final int LAST_METHOD_ID = nsIDirectoryServiceProvider.LAST_METHOD_ID + 1;

	public static final String NS_IDIRECTORYSERVICEPROVIDER2_IID_STRING =
		"2f977d4b-5485-11d4-87e2-0010a4e75ef2";

	public static final nsID NS_IDIRECTORYSERVICEPROVIDER2_IID =
		new nsID(NS_IDIRECTORYSERVICEPROVIDER2_IID_STRING);

	public nsIDirectoryServiceProvider2(int /*long*/ address) {
		super(address);
	}

	public int GetFiles(byte[] prop, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDirectoryServiceProvider.LAST_METHOD_ID + 1, getAddress(), prop, _retval);
	}
}

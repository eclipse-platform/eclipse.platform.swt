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

public class nsIFile extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner17 ? 62 : 45);

	public static final String NS_IFILE_IID_STR =
		"c8c0a080-0868-11d3-915f-d9d889d48e3c";
	
	public static final String NS_IFILE_17_IID_STR =
		"272a5020-64f5-485c-a8c4-44b2882ae0a2";

	public static final nsID NS_IFILE_IID =
		new nsID(NS_IFILE_IID_STR);

	public static final nsID NS_IFILE_17_IID =
		new nsID(NS_IFILE_17_IID_STR);

	public nsIFile(long /*int*/ address) {
		super(address);
	}

	public static final int NORMAL_FILE_TYPE = 0;
	public static final int DIRECTORY_TYPE = 1;

	public int Create(int type, int permissions) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), type, permissions);
	}

	public int GetLeafName(long /*int*/ aLeafName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aLeafName);
	}

	public int GetPath(long /*int*/ aPath) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), aPath);
	}
}

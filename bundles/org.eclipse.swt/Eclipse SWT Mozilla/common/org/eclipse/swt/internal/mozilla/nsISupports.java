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
 * -  Copyright (C) 2003, 2005 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsISupports {

	static final boolean IsSolaris;
	static {
		String osName = System.getProperty ("os.name").toLowerCase (); //$NON-NLS-1$
		IsSolaris = osName.startsWith ("sunos") || osName.startsWith("solaris"); //$NON-NLS-1$
	}
	
	static final int FIRST_METHOD_ID = IsSolaris ? 2 : 0;
	static final int LAST_METHOD_ID = FIRST_METHOD_ID + 2;
	
	public static final String NS_ISUPPORTS_IID_STR =
		"00000000-0000-0000-c000-000000000046";

	public static final nsID NS_ISUPPORTS_IID =
		new nsID(NS_ISUPPORTS_IID_STR);

	int /*long*/ address;

	public nsISupports(int /*long*/ address) {
		this.address = address;
	}

	public int /*long*/ getAddress() {
		return this.address;
	}

	public int QueryInterface(nsID uuid, int /*long*/[] result) {
		return XPCOM.VtblCall(FIRST_METHOD_ID, getAddress(), uuid, result);
	}

	public int AddRef() {
		return XPCOM.VtblCall(FIRST_METHOD_ID + 1, getAddress());
	}

	public int Release() {
		return XPCOM.VtblCall(FIRST_METHOD_ID + 2, getAddress());
	}
}
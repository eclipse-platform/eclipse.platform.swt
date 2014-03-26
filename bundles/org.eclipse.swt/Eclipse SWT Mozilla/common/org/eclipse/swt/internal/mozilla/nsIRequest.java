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


public class nsIRequest extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	static final String NS_IREQUEST_IID_STR = "ef6bfbd2-fd46-48d8-96b7-9f8f0fd387fe";

	static {
		IIDStore.RegisterIID(nsIRequest.class, MozillaVersion.VERSION_BASE, new nsID(NS_IREQUEST_IID_STR));
	}

	public nsIRequest(long /*int*/ address) {
		super(address);
	}

	public int GetName(long /*int*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aName);
	}

	public static final int LOAD_NORMAL = 0;
	public static final int LOAD_BACKGROUND = 1;
	public static final int INHIBIT_CACHING = 128;
	public static final int INHIBIT_PERSISTENT_CACHING = 256;
	public static final int LOAD_BYPASS_CACHE = 512;
	public static final int LOAD_FROM_CACHE = 1024;
	public static final int VALIDATE_ALWAYS = 2048;
	public static final int VALIDATE_NEVER = 4096;
	public static final int VALIDATE_ONCE_PER_SESSION = 8192;
}

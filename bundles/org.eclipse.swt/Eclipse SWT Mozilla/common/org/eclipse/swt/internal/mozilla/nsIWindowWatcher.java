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


public class nsIWindowWatcher extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 12 : 11);

	static final String NS_IWINDOWWATCHER_IID_STR = "002286a8-494b-43b3-8ddd-49e3fc50622b";
	static final String NS_IWINDOWWATCHER_31_IID_STR = "67bc1691-fbaf-484a-a15b-c96212b45034";

	static {
		IIDStore.RegisterIID(nsIWindowWatcher.class, MozillaVersion.VERSION_BASE, new nsID(NS_IWINDOWWATCHER_IID_STR));
		IIDStore.RegisterIID(nsIWindowWatcher.class, MozillaVersion.VERSION_XR31, new nsID(NS_IWINDOWWATCHER_31_IID_STR));
	}

	public nsIWindowWatcher(long /*int*/ address) {
		super(address);
	}

	public int SetWindowCreator(long /*int*/ creator) {
		return XPCOM.VtblCall(this.getMethodIndex("setWindowCreator"), getAddress(), creator);
	}

	public int GetChromeForWindow(long /*int*/ aWindow, long /*int*/[] _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("getChromeForWindow"), getAddress(), aWindow, _retval);
	}
}

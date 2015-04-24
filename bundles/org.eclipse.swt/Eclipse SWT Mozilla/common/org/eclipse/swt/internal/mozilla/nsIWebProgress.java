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


public class nsIWebProgress extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 7 : (IsXULRunner24() ? 6 : 4));

	static final String NS_IWEBPROGRESS_IID_STR = "570f39d0-efd0-11d3-b093-00a024ffc08c";
	static final String NS_IWEBPROGRESS_24_IID_STR = "1c3437b0-9e2c-11e2-9e96-0800200c9a66";
	static final String NS_IWEBPROGRESS_31_IID_STR = "bd0efb3b-1c81-4fb0-b16d-576a2be48a95";

	static {
		IIDStore.RegisterIID(nsIWebProgress.class, MozillaVersion.VERSION_BASE, new nsID(NS_IWEBPROGRESS_IID_STR));
		IIDStore.RegisterIID(nsIWebProgress.class, MozillaVersion.VERSION_XR24, new nsID(NS_IWEBPROGRESS_24_IID_STR));
		IIDStore.RegisterIID(nsIWebProgress.class, MozillaVersion.VERSION_XR31, new nsID(NS_IWEBPROGRESS_31_IID_STR));
	}

	public nsIWebProgress(long /*int*/ address) {
		super(address);
	}

	public static final int NOTIFY_STATE_REQUEST = 1;
	public static final int NOTIFY_STATE_DOCUMENT = 2;
	public static final int NOTIFY_STATE_NETWORK = 4;
	public static final int NOTIFY_STATE_WINDOW = 8;
	public static final int NOTIFY_STATE_ALL = 15;
	public static final int NOTIFY_PROGRESS = 16;
	public static final int NOTIFY_STATUS = 32;
	public static final int NOTIFY_SECURITY = 64;
	public static final int NOTIFY_LOCATION = 128;
	public static final int NOTIFY_ALL = 255;

	public int GetDOMWindow(long /*int*/[] aDOMWindow) {
		return XPCOM.VtblCall(this.getGetterIndex("DOMWindow"), getAddress(), aDOMWindow);
	}
}

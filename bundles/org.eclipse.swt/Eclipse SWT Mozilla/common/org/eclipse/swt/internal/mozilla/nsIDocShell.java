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


public class nsIDocShell extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 146 : 51);

	static final String NS_IDOCSHELL_IID_STR = "69e5de00-7b8b-11d3-af61-00a024ffc08c";
	static final String NS_IDOCSHELL_1_8_IID_STR = "9f0c7461-b9a4-47f6-b88c-421dce1bce66";
	static final String NS_IDOCSHELL_1_9_IID_STR = "7d1cf6b9-daa3-476d-8f9f-9eb2a971a95c";
	static final String NS_IDOCSHELL_1_9_1_IID_STR = "dc4daea1-b43d-406f-bd62-c2ee879192ad";
	static final String NS_IDOCSHELL_1_9_2_IID_STR = "8adfb831-1053-4a19-884d-bcdad7277b4b";
	static final String NS_IDOCSHELL_10_IID_STR = "0666adf8-8738-4ca7-a917-0348f47d2f40";
	static final String NS_IDOCSHELL_24_IID_STR = "f453d2ee-bac7-46f9-a553-df918f0cc0d0";
	static final String NS_IDOCSHELL_31_IID_STR = "e46d924d-c20f-4add-8cf5-1e1c817b2181";

	static {
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOCSHELL_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR1_8, new nsID(NS_IDOCSHELL_1_8_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR1_9, new nsID(NS_IDOCSHELL_1_9_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR1_9_1, new nsID(NS_IDOCSHELL_1_9_1_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR1_9_2, new nsID(NS_IDOCSHELL_1_9_2_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOCSHELL_10_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOCSHELL_24_IID_STR));
		IIDStore.RegisterIID(nsIDocShell.class, MozillaVersion.VERSION_XR31, new nsID(NS_IDOCSHELL_31_IID_STR));
	}

	public nsIDocShell(long /*int*/ address) {
		super(address);
	}

	public int LoadStream(long /*int*/ aStream, long /*int*/ aURI, long /*int*/ aContentType, long /*int*/ aContentCharset, long /*int*/ aLoadInfo) {
		return XPCOM.VtblCall(this.getMethodIndex("loadStream"), getAddress(), aStream, aURI, aContentType, aContentCharset, aLoadInfo);
	}
}

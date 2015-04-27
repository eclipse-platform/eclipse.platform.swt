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


public class nsIChannel extends nsIRequest {

	static final int LAST_METHOD_ID = nsIRequest.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24)? 21 : (IsXULRunner10() ? 19 : 16));

	static final String NS_ICHANNEL_IID_STR = "c63a055a-a676-4e71-bf3c-6cfa11082018";
	static final String NS_ICHANNEL_10_IID_STR = "06f6ada3-7729-4e72-8d3f-bf8ba630ff9b";
	static final String NS_ICHANNEL_24_IID_STR = "2a8a7237-c1e2-4de7-b669-2002af29e42d";

	public nsIChannel(long /*int*/ address) {
		super(address);
	}

	static {
		IIDStore.RegisterIID(nsIChannel.class, MozillaVersion.VERSION_BASE, new nsID(NS_ICHANNEL_IID_STR));
		IIDStore.RegisterIID(nsIChannel.class, MozillaVersion.VERSION_XR10, new nsID(NS_ICHANNEL_10_IID_STR));
		IIDStore.RegisterIID(nsIChannel.class, MozillaVersion.VERSION_XR24, new nsID(NS_ICHANNEL_24_IID_STR));
	}

	public int GetURI(long /*int*/[] aURI) {
		return XPCOM.VtblCall(this.getGetterIndex("URI"), getAddress(), aURI);
	}

	public int SetNotificationCallbacks(long /*int*/ aNotificationCallbacks) {
		return XPCOM.VtblCall(this.getSetterIndex("notificationCallbacks"), getAddress(), aNotificationCallbacks);
	}

	public static final int LOAD_DOCUMENT_URI = 65536;
	public static final int LOAD_RETARGETED_DOCUMENT_URI = 131072;
	public static final int LOAD_REPLACE = 262144;
	public static final int LOAD_INITIAL_DOCUMENT_URI = 524288;
	public static final int LOAD_TARGETED = 1048576;
	public static final int LOAD_CALL_CONTENT_SNIFFERS = 2097152;
	public static final int LOAD_CLASSIFY_URI = 4194304;
	public static final int DISPOSITION_INLINE = 0;
	public static final int DISPOSITION_ATTACHMENT = 1;
}

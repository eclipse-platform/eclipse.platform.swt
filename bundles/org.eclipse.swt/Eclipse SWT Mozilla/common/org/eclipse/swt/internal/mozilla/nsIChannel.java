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

public class nsIChannel extends nsIRequest {

	static final int LAST_METHOD_ID = nsIRequest.LAST_METHOD_ID + (IsXULRunner10 ? 19 : 16);

	public static final String NS_ICHANNEL_IID_STR =
		"c63a055a-a676-4e71-bf3c-6cfa11082018";

	public static final String NS_ICHANNEL_10_IID_STR =
		"06f6ada3-7729-4e72-8d3f-bf8ba630ff9b";

	public static final nsID NS_ICHANNEL_IID =
		new nsID(NS_ICHANNEL_IID_STR);

	public static final nsID NS_ICHANNEL_10_IID =
		new nsID(NS_ICHANNEL_10_IID_STR);

	public nsIChannel(int /*long*/ address) {
		super(address);
	}

	public int GetOriginalURI(int /*long*/[] aOriginalURI) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 1, getAddress(), aOriginalURI);
	}

	public int SetOriginalURI(int /*long*/ aOriginalURI) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 2, getAddress(), aOriginalURI);
	}

	public int GetURI(int /*long*/[] aURI) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 3, getAddress(), aURI);
	}

	public int GetOwner(int /*long*/[] aOwner) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 4, getAddress(), aOwner);
	}

	public int SetOwner(int /*long*/ aOwner) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 5, getAddress(), aOwner);
	}

	public int GetNotificationCallbacks(int /*long*/[] aNotificationCallbacks) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 6, getAddress(), aNotificationCallbacks);
	}

	public int SetNotificationCallbacks(int /*long*/ aNotificationCallbacks) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 7, getAddress(), aNotificationCallbacks);
	}

	public int GetSecurityInfo(int /*long*/[] aSecurityInfo) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 8, getAddress(), aSecurityInfo);
	}

	public int GetContentType(int /*long*/ aContentType) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 9, getAddress(), aContentType);
	}

	public int SetContentType(int /*long*/ aContentType) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 10, getAddress(), aContentType);
	}

	public int GetContentCharset(int /*long*/ aContentCharset) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 11, getAddress(), aContentCharset);
	}

	public int SetContentCharset(int /*long*/ aContentCharset) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 12, getAddress(), aContentCharset);
	}

	public int GetContentLength(int[] aContentLength) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 13, getAddress(), aContentLength);
	}

	public int SetContentLength(int aContentLength) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 14, getAddress(), aContentLength);
	}

	public int Open(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 15, getAddress(), _retval);
	}

	public int AsyncOpen(int /*long*/ aListener, int /*long*/ aContext) {
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 16, getAddress(), aListener, aContext);
	}

	public int GetContentDisposition(int[] aContentDisposition) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 17, getAddress(), aContentDisposition);
	}

	public int GetContentDispositionFilename(int /*long*/ aContentDispositionFilename) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 18, getAddress(), aContentDispositionFilename);
	}

	public int GetContentDispositionHeader(int /*long*/ aContentDispositionHeader) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIRequest.LAST_METHOD_ID + 19, getAddress(), aContentDispositionHeader);
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

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

public class nsILoadGroup extends nsIRequest {

	static final int LAST_METHOD_ID = nsIRequest.LAST_METHOD_ID + 10;

	public static final String NS_ILOADGROUP_IID_STRING =
		"3de0a31c-feaf-400f-9f1e-4ef71f8b20cc";

	public static final nsID NS_ILOADGROUP_IID =
		new nsID(NS_ILOADGROUP_IID_STRING);

	public nsILoadGroup(int address) {
		super(address);
	}

	public int GetGroupObserver(int[] aGroupObserver) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aGroupObserver);
	}

	public int SetGroupObserver(int aGroupObserver) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aGroupObserver);
	}

	public int GetDefaultLoadRequest(int[] aDefaultLoadRequest) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aDefaultLoadRequest);
	}

	public int SetDefaultLoadRequest(int aDefaultLoadRequest) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aDefaultLoadRequest);
	}

	public int AddRequest(int aRequest, int aContext) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aRequest, aContext);
	}

	public int RemoveRequest(int aRequest, int aContext, int aStatus) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aRequest, aContext, aStatus);
	}

	public int GetRequests(int[] requests) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), requests);
	}

	public int GetActiveCount(int[] aActiveCount) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aActiveCount);
	}

	public int GetNotificationCallbacks(int[] notificationCallbacks) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), notificationCallbacks);
	}

	public int SetNotificationCallbacks(int notificationCallbacks) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), notificationCallbacks);
	}
}
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
 * -  Copyright (C) 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIHttpChannel extends nsIChannel {

	static final int LAST_METHOD_ID = nsIChannel.LAST_METHOD_ID + 19;

	public static final String NS_IHTTPCHANNEL_IID_STR =
		"9277fe09-f0cc-4cd9-bbce-581dd94b0260";

	public static final nsID NS_IHTTPCHANNEL_IID =
		new nsID(NS_IHTTPCHANNEL_IID_STR);

	public nsIHttpChannel(int /*long*/ address) {
		super(address);
	}

	public int GetRequestMethod(int /*long*/ aRequestMethod) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 1, getAddress(), aRequestMethod);
	}

	public int SetRequestMethod(int /*long*/ aRequestMethod) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 2, getAddress(), aRequestMethod);
	}

	public int GetReferrer(int /*long*/[] aReferrer) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 3, getAddress(), aReferrer);
	}

	public int SetReferrer(int /*long*/ aReferrer) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 4, getAddress(), aReferrer);
	}

	public int GetRequestHeader(int /*long*/ aHeader, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 5, getAddress(), aHeader, _retval);
	}

	public int SetRequestHeader(int /*long*/ aHeader, int /*long*/ aValue, int aMerge) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 6, getAddress(), aHeader, aValue, aMerge);
	}

	public int VisitRequestHeaders(int /*long*/ aVisitor) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 7, getAddress(), aVisitor);
	}

	public int GetAllowPipelining(int[] aAllowPipelining) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 8, getAddress(), aAllowPipelining);
	}

	public int SetAllowPipelining(int aAllowPipelining) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 9, getAddress(), aAllowPipelining);
	}

	public int GetRedirectionLimit(int[] aRedirectionLimit) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 10, getAddress(), aRedirectionLimit);
	}

	public int SetRedirectionLimit(int aRedirectionLimit) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 11, getAddress(), aRedirectionLimit);
	}

	public int GetResponseStatus(int[] aResponseStatus) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 12, getAddress(), aResponseStatus);
	}

	public int GetResponseStatusText(int /*long*/ aResponseStatusText) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 13, getAddress(), aResponseStatusText);
	}

	public int GetRequestSucceeded(int[] aRequestSucceeded) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 14, getAddress(), aRequestSucceeded);
	}

	public int GetResponseHeader(int /*long*/ header, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 15, getAddress(), header, _retval);
	}

	public int SetResponseHeader(int /*long*/ header, int /*long*/ value, int merge) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 16, getAddress(), header, value, merge);
	}

	public int VisitResponseHeaders(int /*long*/ aVisitor) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 17, getAddress(), aVisitor);
	}

	public int IsNoStoreResponse(int[] _retval) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 18, getAddress(), _retval);
	}

	public int IsNoCacheResponse(int[] _retval) {
		return XPCOM.VtblCall(nsIChannel.LAST_METHOD_ID + 19, getAddress(), _retval);
	}
}

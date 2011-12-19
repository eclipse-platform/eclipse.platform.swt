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
 * -  Copyright (C) 2003, 2009 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIX509CertValidity extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 8;

	public static final String NS_IX509CERTVALIDITY_IID_STR =
		"e701dfd8-1dd1-11b2-a172-ffa6cc6156ad";

	public static final nsID NS_IX509CERTVALIDITY_IID =
		new nsID(NS_IX509CERTVALIDITY_IID_STR);

	public nsIX509CertValidity(int /*long*/ address) {
		super(address);
	}

	public int GetNotBefore(int /*long*/ aNotBefore) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aNotBefore);
	}

	public int GetNotBeforeLocalTime(int /*long*/ aNotBeforeLocalTime) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aNotBeforeLocalTime);
	}

	public int GetNotBeforeLocalDay(int /*long*/ aNotBeforeLocalDay) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aNotBeforeLocalDay);
	}

	public int GetNotBeforeGMT(int /*long*/ aNotBeforeGMT) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aNotBeforeGMT);
	}

	public int GetNotAfter(int /*long*/ aNotAfter) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aNotAfter);
	}

	public int GetNotAfterLocalTime(int /*long*/ aNotAfterLocalTime) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aNotAfterLocalTime);
	}

	public int GetNotAfterLocalDay(int /*long*/ aNotAfterLocalDay) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aNotAfterLocalDay);
	}

	public int GetNotAfterGMT(int /*long*/ aNotAfterGMT) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aNotAfterGMT);
	}
}

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

public class nsICertificateDialogs extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 6;

	public static final String NS_ICERTIFICATEDIALOGS_IID_STR =
		"a03ca940-09be-11d5-ac5d-000064657374";

	public static final nsID NS_ICERTIFICATEDIALOGS_IID =
		new nsID(NS_ICERTIFICATEDIALOGS_IID_STR);

	public nsICertificateDialogs(int /*long*/ address) {
		super(address);
	}

	public int ConfirmDownloadCACert(int /*long*/ ctx, int /*long*/ cert, int[] trust, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), ctx, cert, trust, _retval);
	}

	public int NotifyCACertExists(int /*long*/ ctx) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), ctx);
	}

	public int SetPKCS12FilePassword(int /*long*/ ctx, int /*long*/ password, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), ctx, password, _retval);
	}

	public int GetPKCS12FilePassword(int /*long*/ ctx, int /*long*/ password, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), ctx, password, _retval);
	}

	public int ViewCert(int /*long*/ ctx, int /*long*/ cert) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), ctx, cert);
	}

	public int CrlImportStatusDialog(int /*long*/ ctx, int /*long*/ crl) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), ctx, crl);
	}
}

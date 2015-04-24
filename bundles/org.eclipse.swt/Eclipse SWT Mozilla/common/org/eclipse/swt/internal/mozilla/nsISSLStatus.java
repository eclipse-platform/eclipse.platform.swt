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


public class nsISSLStatus extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + ((IsXULRunner10() || IsXULRunner24() || IsXULRunner31()) ? 8 : 7);

	static final String NS_ISSLSTATUS_IID_STR = "cfede939-def1-49be-81ed-d401b3a07d1c";
	static final String NS_ISSLSTATUS_10_IID_STR = "3f1fcd83-c5a9-4cd1-a250-7676ca7c7e34";

	static {
		IIDStore.RegisterIID(nsISSLStatus.class, MozillaVersion.VERSION_BASE, new nsID(NS_ISSLSTATUS_IID_STR));
		IIDStore.RegisterIID(nsISSLStatus.class, MozillaVersion.VERSION_XR10, new nsID(NS_ISSLSTATUS_10_IID_STR));
	}

	public nsISSLStatus(long /*int*/ address) {
		super(address);
	}

	public int GetServerCert(long /*int*/[] aServerCert) {
		return XPCOM.VtblCall(this.getGetterIndex("serverCert"), getAddress(), aServerCert);
	}

	public int GetIsDomainMismatch(int[] aIsDomainMismatch) {
		return XPCOM.VtblCall(this.getGetterIndex("isDomainMismatch"), getAddress(), aIsDomainMismatch);
	}

	public int GetIsNotValidAtThisTime(int[] aIsNotValidAtThisTime) {
		return XPCOM.VtblCall(this.getGetterIndex("isNotValidAtThisTime"), getAddress(), aIsNotValidAtThisTime);
	}

	public int GetIsUntrusted(int[] aIsUntrusted) {
		return XPCOM.VtblCall(this.getGetterIndex("isUntrusted"), getAddress(), aIsUntrusted);
	}
}

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


public class nsIX509Cert extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner31 () ? 27 : (IsXULRunner24() ? 26 : 27));

	static final String NS_IX509CERT_IID_STR = "f0980f60-ee3d-11d4-998b-00b0d02354a0";
	static final String NS_IX509CERT_24_IID_STR = "45b24b0a-6189-4b05-af0b-8d4d66d57c59";
	static final String NS_IX509CERT_31_IID_STR = "6286dd8c-c1a1-11e3-941d-180373d97f24";

	static {
		IIDStore.RegisterIID(nsIX509Cert.class, MozillaVersion.VERSION_BASE, new nsID(NS_IX509CERT_IID_STR));
		IIDStore.RegisterIID(nsIX509Cert.class, MozillaVersion.VERSION_XR24, new nsID(NS_IX509CERT_24_IID_STR));
		IIDStore.RegisterIID(nsIX509Cert.class, MozillaVersion.VERSION_XR31, new nsID(NS_IX509CERT_31_IID_STR));
	}

	public nsIX509Cert(long /*int*/ address) {
		super(address);
	}

	public int GetCommonName(long /*int*/ aCommonName) {
		return XPCOM.VtblCall(this.getGetterIndex("commonName"), getAddress(), aCommonName);
	}

	public int GetIssuerCommonName(long /*int*/ aIssuerCommonName) {
		return XPCOM.VtblCall(this.getGetterIndex("issuerCommonName"), getAddress(), aIssuerCommonName);
	}

	public int GetValidity(long /*int*/[] aValidity) {
		return XPCOM.VtblCall(this.getGetterIndex("validity"), getAddress(), aValidity);
	}

	public static final int UNKNOWN_CERT = 0;
	public static final int CA_CERT = 1;
	public static final int USER_CERT = 2;
	public static final int EMAIL_CERT = 4;
	public static final int SERVER_CERT = 8;
	public static final int VERIFIED_OK = 0;
	public static final int NOT_VERIFIED_UNKNOWN = 1;
	public static final int CERT_REVOKED = 2;
	public static final int CERT_EXPIRED = 4;
	public static final int CERT_NOT_TRUSTED = 8;
	public static final int ISSUER_NOT_TRUSTED = 16;
	public static final int ISSUER_UNKNOWN = 32;
	public static final int INVALID_CA = 64;
	public static final int USAGE_NOT_ALLOWED = 128;
	public static final int CERT_USAGE_SSLClient = 0;
	public static final int CERT_USAGE_SSLServer = 1;
	public static final int CERT_USAGE_SSLServerWithStepUp = 2;
	public static final int CERT_USAGE_SSLCA = 3;
	public static final int CERT_USAGE_EmailSigner = 4;
	public static final int CERT_USAGE_EmailRecipient = 5;
	public static final int CERT_USAGE_ObjectSigner = 6;
	public static final int CERT_USAGE_UserCertImport = 7;
	public static final int CERT_USAGE_VerifyCA = 8;
	public static final int CERT_USAGE_ProtectedObjectSigner = 9;
	public static final int CERT_USAGE_StatusResponder = 10;
	public static final int CERT_USAGE_AnyCA = 11;
}

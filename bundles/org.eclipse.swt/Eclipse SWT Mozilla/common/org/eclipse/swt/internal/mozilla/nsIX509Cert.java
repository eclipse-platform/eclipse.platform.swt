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
 * -  Copyright (C) 2003, 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIX509Cert extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 27;

	public static final String NS_IX509CERT_IID_STR =
		"f0980f60-ee3d-11d4-998b-00b0d02354a0";

	public static final nsID NS_IX509CERT_IID =
		new nsID(NS_IX509CERT_IID_STR);

	public nsIX509Cert(long /*int*/ address) {
		super(address);
	}

	public int GetNickname(long /*int*/ aNickname) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aNickname);
	}

	public int GetEmailAddress(long /*int*/ aEmailAddress) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aEmailAddress);
	}

	public int GetEmailAddresses(int[] length, long /*int*/[] addresses) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), length, addresses);
	}

	public int ContainsEmailAddress(long /*int*/ aEmailAddress, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aEmailAddress, _retval);
	}

	public int GetSubjectName(long /*int*/ aSubjectName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aSubjectName);
	}

	public int GetCommonName(long /*int*/ aCommonName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aCommonName);
	}

	public int GetOrganization(long /*int*/ aOrganization) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aOrganization);
	}

	public int GetOrganizationalUnit(long /*int*/ aOrganizationalUnit) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aOrganizationalUnit);
	}

	public int GetSha1Fingerprint(long /*int*/ aSha1Fingerprint) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aSha1Fingerprint);
	}

	public int GetMd5Fingerprint(long /*int*/ aMd5Fingerprint) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aMd5Fingerprint);
	}

	public int GetTokenName(long /*int*/ aTokenName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aTokenName);
	}

	public int GetIssuerName(long /*int*/ aIssuerName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aIssuerName);
	}

	public int GetSerialNumber(long /*int*/ aSerialNumber) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aSerialNumber);
	}

	public int GetIssuerCommonName(long /*int*/ aIssuerCommonName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aIssuerCommonName);
	}

	public int GetIssuerOrganization(long /*int*/ aIssuerOrganization) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aIssuerOrganization);
	}

	public int GetIssuerOrganizationUnit(long /*int*/ aIssuerOrganizationUnit) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aIssuerOrganizationUnit);
	}

	public int GetIssuer(long /*int*/[] aIssuer) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aIssuer);
	}

	public int GetValidity(long /*int*/[] aValidity) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aValidity);
	}

	public int GetDbKey(long /*int*/[] aDbKey) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), aDbKey);
	}

	public int GetWindowTitle(long /*int*/[] aWindowTitle) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), aWindowTitle);
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

	public int GetChain(long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 21, getAddress(), _retval);
	}

	public int GetUsagesArray(int ignoreOcsp, int[] verified, int[] count, long /*int*/[] usages) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), ignoreOcsp, verified, count, usages);
	}

	public int GetUsagesString(int ignoreOcsp, int[] verified, long /*int*/ usages) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), ignoreOcsp, verified, usages);
	}

	public int VerifyForUsage(int usage, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 24, getAddress(), usage, _retval);
	}

	public int GetASN1Structure(long /*int*/[] aASN1Structure) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aASN1Structure);
	}

	public int GetRawDER(int[] length, long /*int*/[] data) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), length, data);
	}

	public int Equals(long /*int*/ other, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), other, _retval);
	}
}

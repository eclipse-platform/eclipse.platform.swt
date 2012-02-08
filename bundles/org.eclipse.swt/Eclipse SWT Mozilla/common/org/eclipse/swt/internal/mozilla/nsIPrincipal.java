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

public class nsIPrincipal extends nsISerializable {

	static final int LAST_METHOD_ID = nsISerializable.LAST_METHOD_ID + (Is8 ? 26 : 23);

	public static final String NS_IPRINCIPAL_IID_STR =
		"b8268b9a-2403-44ed-81e3-614075c92034";

	public static final nsID NS_IPRINCIPAL_IID =
		new nsID(NS_IPRINCIPAL_IID_STR);

	public static final String NS_IPRINCIPAL_8_IID_STR =
		"b406a2db-e547-4c95-b8e2-ad09ecb54ce0";

	public static final nsID NS_IPRINCIPAL_8_IID =
		new nsID(NS_IPRINCIPAL_8_IID_STR);

	public nsIPrincipal(int /*long*/ address) {
		super(address);
	}

	public static final int ENABLE_DENIED = 1;
	public static final int ENABLE_UNKNOWN = 2;
	public static final int ENABLE_WITH_USER_PERMISSION = 3;
	public static final int ENABLE_GRANTED = 4;

	public int GetPreferences(int /*long*/[] prefBranch, int /*long*/[] id, int /*long*/[] subjectName, int /*long*/[] grantedList, int /*long*/[] deniedList, int[] isTrusted) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 1, getAddress(), prefBranch, id, subjectName, grantedList, deniedList, isTrusted);
	}

	public int Equals(int /*long*/ other, int[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 2, getAddress(), other, _retval);
	}

	public int EqualsIgnoringDomain(int /*long*/ other, int[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 3, getAddress(), other, _retval);
	}

	public int GetHashValue(int[] aHashValue) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 4 : 3), getAddress(), aHashValue);
	}

	public int GetJSPrincipals(int /*long*/ cx, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 5 : 4), getAddress(), cx, _retval);
	}

	public int GetSecurityPolicy(int /*long*/[] aSecurityPolicy) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 6 : 5), getAddress(), aSecurityPolicy);
	}

	public int SetSecurityPolicy(int /*long*/ aSecurityPolicy) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 7 : 6), getAddress(), aSecurityPolicy);
	}

	public int CanEnableCapability(byte[] capability, int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 8 : 7), getAddress(), capability, _retval);
	}

	public int SetCanEnableCapability(byte[] capability, short canEnable) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 9 : 8), getAddress(), capability, canEnable);
	}

	public int IsCapabilityEnabled(byte[] capability, int /*long*/ annotation, int[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 10 : 9), getAddress(), capability, annotation, _retval);
	}

	public int EnableCapability(byte[] capability, int /*long*/[] annotation) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 11 : 10), getAddress(), capability, annotation);
	}

	public int RevertCapability(byte[] capability, int /*long*/[] annotation) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 12 : 11), getAddress(), capability, annotation);
	}

	public int DisableCapability(byte[] capability, int /*long*/[] annotation) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 13 : 12), getAddress(), capability, annotation);
	}

	public int GetURI(int /*long*/[] aURI) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 14 : 13), getAddress(), aURI);
	}

	public int GetDomain(int /*long*/[] aDomain) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 15 : 14), getAddress(), aDomain);
	}

	public int SetDomain(int /*long*/ aDomain) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 16 : 15), getAddress(), aDomain);
	}

	public int GetOrigin(int /*long*/[] aOrigin) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 17 : 16), getAddress(), aOrigin);
	}

	public int GetHasCertificate(int[] aHasCertificate) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 18 : 17), getAddress(), aHasCertificate);
	}

	public int GetFingerprint(int /*long*/ aFingerprint) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 19 : 18), getAddress(), aFingerprint);
	}

	public int GetPrettyName(int /*long*/ aPrettyName) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 20 : 19), getAddress(), aPrettyName);
	}

	public int Subsumes(int /*long*/ other, int[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 21 : 20), getAddress(), other, _retval);
	}

	public int CheckMayLoad(int /*long*/ uri, int report) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 22 : 21), getAddress(), uri, report);
	}

	public int GetSubjectName(int /*long*/ aSubjectName) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 23 : 22), getAddress(), aSubjectName);
	}

	public int GetCertificate(int /*long*/[] aCertificate) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (Is8 ? 24 : 23), getAddress(), aCertificate);
	}
	
	public int GetCsp(int /*long*/[] aCsp) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 25, getAddress(), aCsp);
	}

	public int SetCsp(int /*long*/ aCsp) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 26, getAddress(), aCsp);
	}
}

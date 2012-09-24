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

public class nsIPrincipal extends nsISerializable {

	static final int LAST_METHOD_ID = nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 26 : 23);

	public static final String NS_IPRINCIPAL_IID_STR =
		"b8268b9a-2403-44ed-81e3-614075c92034";

	public static final nsID NS_IPRINCIPAL_IID =
		new nsID(NS_IPRINCIPAL_IID_STR);

	public static final String NS_IPRINCIPAL_10_IID_STR =
		"b406a2db-e547-4c95-b8e2-ad09ecb54ce0";

	public static final nsID NS_IPRINCIPAL_10_IID =
		new nsID(NS_IPRINCIPAL_10_IID_STR);

	public nsIPrincipal(long /*int*/ address) {
		super(address);
	}

	public static final int ENABLE_DENIED = 1;
	public static final int ENABLE_UNKNOWN = 2;
	public static final int ENABLE_WITH_USER_PERMISSION = 3;
	public static final int ENABLE_GRANTED = 4;

	public int GetPreferences(long /*int*/[] prefBranch, long /*int*/[] id, long /*int*/[] subjectName, long /*int*/[] grantedList, long /*int*/[] deniedList, int[] isTrusted) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 1, getAddress(), prefBranch, id, subjectName, grantedList, deniedList, isTrusted);
	}

	public int Equals(long /*int*/ other, int[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 2, getAddress(), other, _retval);
	}

	public int EqualsIgnoringDomain(long /*int*/ other, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 3, getAddress(), other, _retval);
	}

	public int GetHashValue(int[] aHashValue) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 4 : 3), getAddress(), aHashValue);
	}

	public int GetJSPrincipals(long /*int*/ cx, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 5 : 4), getAddress(), cx, _retval);
	}

	public int GetSecurityPolicy(long /*int*/[] aSecurityPolicy) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 6 : 5), getAddress(), aSecurityPolicy);
	}

	public int SetSecurityPolicy(long /*int*/ aSecurityPolicy) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 7 : 6), getAddress(), aSecurityPolicy);
	}

	public int CanEnableCapability(byte[] capability, long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 8 : 7), getAddress(), capability, _retval);
	}

	public int SetCanEnableCapability(byte[] capability, short canEnable) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 9 : 8), getAddress(), capability, canEnable);
	}

	public int IsCapabilityEnabled(byte[] capability, long /*int*/ annotation, int[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 10 : 9), getAddress(), capability, annotation, _retval);
	}

	public int EnableCapability(byte[] capability, long /*int*/[] annotation) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 11 : 10), getAddress(), capability, annotation);
	}

	public int RevertCapability(byte[] capability, long /*int*/[] annotation) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 12 : 11), getAddress(), capability, annotation);
	}

	public int DisableCapability(byte[] capability, long /*int*/[] annotation) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 13 : 12), getAddress(), capability, annotation);
	}

	public int GetURI(long /*int*/[] aURI) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 14 : 13), getAddress(), aURI);
	}

	public int GetDomain(long /*int*/[] aDomain) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 15 : 14), getAddress(), aDomain);
	}

	public int SetDomain(long /*int*/ aDomain) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 16 : 15), getAddress(), aDomain);
	}

	public int GetOrigin(long /*int*/[] aOrigin) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 17 : 16), getAddress(), aOrigin);
	}

	public int GetHasCertificate(int[] aHasCertificate) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 18 : 17), getAddress(), aHasCertificate);
	}

	public int GetFingerprint(long /*int*/ aFingerprint) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 19 : 18), getAddress(), aFingerprint);
	}

	public int GetPrettyName(long /*int*/ aPrettyName) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 20 : 19), getAddress(), aPrettyName);
	}

	public int Subsumes(long /*int*/ other, int[] _retval) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 21 : 20), getAddress(), other, _retval);
	}

	public int CheckMayLoad(long /*int*/ uri, int report) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 22 : 21), getAddress(), uri, report);
	}

	public int GetSubjectName(long /*int*/ aSubjectName) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 23 : 22), getAddress(), aSubjectName);
	}

	public int GetCertificate(long /*int*/[] aCertificate) {
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + (IsXULRunner10 ? 24 : 23), getAddress(), aCertificate);
	}
	
	public int GetCsp(long /*int*/[] aCsp) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 25, getAddress(), aCsp);
	}

	public int SetCsp(long /*int*/ aCsp) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISerializable.LAST_METHOD_ID + 26, getAddress(), aCsp);
	}
}

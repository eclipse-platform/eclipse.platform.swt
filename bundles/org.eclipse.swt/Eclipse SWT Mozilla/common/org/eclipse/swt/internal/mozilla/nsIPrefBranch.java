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
 * -  Copyright (C) 2003, 2008 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIPrefBranch extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 18;

	public static final String NS_IPREFBRANCH_IID_STR =
		"56c35506-f14b-11d3-99d3-ddbfac2ccf65";

	public static final nsID NS_IPREFBRANCH_IID =
		new nsID(NS_IPREFBRANCH_IID_STR);

	public nsIPrefBranch(int /*long*/ address) {
		super(address);
	}

	public static final int PREF_INVALID = 0;

	public static final int PREF_STRING = 32;

	public static final int PREF_INT = 64;

	public static final int PREF_BOOL = 128;

	public int GetRoot(int /*long*/[] aRoot) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aRoot);
	}

	public int GetPrefType(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aPrefName, _retval);
	}

	public int GetBoolPref(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aPrefName, _retval);
	}

	public int SetBoolPref(byte[] aPrefName, int aValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aPrefName, aValue);
	}

	public int GetCharPref(byte[] aPrefName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aPrefName, _retval);
	}

	public int SetCharPref(byte[] aPrefName, byte[] aValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aPrefName, aValue);
	}

	public int GetIntPref(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aPrefName, _retval);
	}

	public int SetIntPref(byte[] aPrefName, int aValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aPrefName, aValue);
	}

	public int GetComplexValue(byte[] aPrefName, nsID aType, int /*long*/[] aValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aPrefName, aType, aValue);
	}

	public int SetComplexValue(byte[] aPrefName, nsID aType, int /*long*/ aValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aPrefName, aType, aValue);
	}

	public int ClearUserPref(byte[] aPrefName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aPrefName);
	}

	public int LockPref(byte[] aPrefName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aPrefName);
	}

	public int PrefHasUserValue(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aPrefName, _retval);
	}

	public int PrefIsLocked(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aPrefName, _retval);
	}

	public int UnlockPref(byte[] aPrefName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aPrefName);
	}

	public int DeleteBranch(byte[] aStartingAt) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aStartingAt);
	}

	public int GetChildList(byte[] aStartingAt, int[] aCount, int /*long*/[] aChildArray) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aStartingAt, aCount, aChildArray);
	}

	public int ResetBranch(byte[] aStartingAt) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aStartingAt);
	}
}

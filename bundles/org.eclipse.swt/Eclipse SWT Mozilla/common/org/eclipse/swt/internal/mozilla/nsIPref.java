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

public class nsIPref extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 45;

	public static final String NS_IPREF_IID_STRING =
		"a22ad7b0-ca86-11d1-a9a4-00805f8a7ac4";

	public static final nsID NS_IPREF_IID =
		new nsID(NS_IPREF_IID_STRING);

	public nsIPref(int address) {
		super(address);
	}

	public int ReadUserPrefs(int aFile) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aFile);
	}

	public int ResetPrefs() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress());
	}

	public int ResetUserPrefs() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress());
	}

	public int SavePrefFile(int aFile) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aFile);
	}

	public int GetBranch(byte[] aPrefRoot, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aPrefRoot, _retval);
	}

	public int GetDefaultBranch(byte[] aPrefRoot, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aPrefRoot, _retval);
	}

	public static final int ePrefInvalid = 0;

	public static final int ePrefLocked = 1;

	public static final int ePrefUserset = 2;

	public static final int ePrefConfig = 4;

	public static final int ePrefRemote = 8;

	public static final int ePrefLilocal = 16;

	public static final int ePrefString = 32;

	public static final int ePrefInt = 64;

	public static final int ePrefBool = 128;

	public static final int ePrefValuetypeMask = 224;

	public int GetRoot(int[] aRoot) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aRoot);
	}

	public int GetPrefType(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aPrefName, _retval);
	}

	public int GetBoolPref(byte[] aPrefName, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aPrefName, _retval);
	}

	public int SetBoolPref(byte[] aPrefName, int aValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aPrefName, aValue);
	}

	public int GetCharPref(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aPrefName, _retval);
	}

	public int SetCharPref(byte[] aPrefName, byte[] aValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aPrefName, aValue);
	}

	public int GetIntPref(byte[] aPrefName, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), aPrefName, _retval);
	}

	public int SetIntPref(byte[] aPrefName, int aValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), aPrefName, aValue);
	}

	public int GetComplexValue(byte[] aPrefName, nsID aType, int[] aValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aPrefName, aType, aValue);
	}

	public int SetComplexValue(byte[] aPrefName, nsID aType, int aValue) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), aPrefName, aType, aValue);
	}

	public int ClearUserPref(byte[] aPrefName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), aPrefName);
	}

	public int PrefIsLocked(byte[] aPrefName, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), aPrefName, _retval);
	}

	public int LockPref(byte[] aPrefName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), aPrefName);
	}

	public int UnlockPref(byte[] aPrefName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), aPrefName);
	}

	public int ResetBranch(byte[] aStartingAt) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), aStartingAt);
	}

	public int DeleteBranch(byte[] aStartingAt) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), aStartingAt);
	}

	public int GetChildList(byte[] aStartingAt, int[] aCount, int[] aChildArray) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int AddObserver(byte[] aDomain, int aObserver, boolean aHoldWeak) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress(), aDomain, aObserver, aHoldWeak);
	}

	public int RemoveObserver(byte[] aDomain, int aObserver) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 25, getAddress(), aDomain, aObserver);
	}

	public int CopyCharPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 26, getAddress(), pref, _retval);
	}

	public int CopyDefaultCharPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 27, getAddress(), pref, _retval);
	}

	public int GetDefaultBoolPref(byte[] pref, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 28, getAddress(), pref, _retval);
	}

	public int GetDefaultIntPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 29, getAddress(), pref, _retval);
	}

	public int SetDefaultBoolPref(byte[] pref, boolean value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 30, getAddress(), pref, value);
	}

	public int SetDefaultCharPref(byte[] pref, byte[] value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 31, getAddress(), pref, value);
	}

	public int SetDefaultIntPref(byte[] pref, int value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 32, getAddress(), pref, value);
	}

	public int CopyUnicharPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 33, getAddress(), pref, _retval);
	}

	public int CopyDefaultUnicharPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 34, getAddress(), pref, _retval);
	}

	public int SetUnicharPref(byte[] pref, char[] value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 35, getAddress(), pref, value);
	}

	public int SetDefaultUnicharPref(byte[] pref, char[] value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 36, getAddress(), pref, value);
	}

	public int GetLocalizedUnicharPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 37, getAddress(), pref, _retval);
	}

	public int GetDefaultLocalizedUnicharPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 38, getAddress(), pref, _retval);
	}

	public int GetFilePref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 39, getAddress(), pref, _retval);
	}

	public int SetFilePref(byte[] pref, int value, boolean setDefault) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 40, getAddress(), pref, value, setDefault);
	}

	public int GetFileXPref(byte[] pref, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 41, getAddress(), pref, _retval);
	}

	public int SetFileXPref(byte[] pref, int value) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 42, getAddress(), pref, value);
	}

	public int RegisterCallback(byte[] domain, int callback, int closure) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 43, getAddress(), domain, callback, closure);
	}

	public int UnregisterCallback(byte[] domain, int callback, int closure) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 44, getAddress(), domain, callback, closure);
	}

	public int EnumerateChildren(byte[] parent, int callback, int data) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 45, getAddress(), parent, callback, data);
	}
}
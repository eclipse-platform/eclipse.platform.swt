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
 * -  Copyright (C) 2003, 2005 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIProfile extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

	public static final String NS_IPROFILE_IID_STR =
		"02b0625a-e7f3-11d2-9f5a-006008a6efe9";

	public static final nsID NS_IPROFILE_IID =
		new nsID(NS_IPROFILE_IID_STR);

	public nsIProfile(int /*long*/ address) {
		super(address);
	}

	public int GetProfileCount(int[] aProfileCount) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aProfileCount);
	}

	public int GetProfileList(int[] length, int /*long*/[] profileNames) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), length, profileNames);
	}

	public int ProfileExists(char[] profileName, boolean[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), profileName, _retval);
	}

	public int GetCurrentProfile(int /*long*/[] aCurrentProfile) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aCurrentProfile);
	}

	public int SetCurrentProfile(char[] aCurrentProfile) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aCurrentProfile);
	}

	public static final int SHUTDOWN_PERSIST = 1;

	public static final int SHUTDOWN_CLEANSE = 2;

	public int ShutDownCurrentProfile(int shutDownType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), shutDownType);
	}

	public int CreateNewProfile(char[] profileName, char[] nativeProfileDir, char[] langcode, boolean useExistingDir) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), profileName, nativeProfileDir, langcode, useExistingDir);
	}

	public int RenameProfile(char[] oldName, char[] newName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), oldName, newName);
	}

	public int DeleteProfile(char[] name, boolean canDeleteFiles) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), name, canDeleteFiles);
	}

	public int CloneProfile(char[] profileName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), profileName);
	}
}
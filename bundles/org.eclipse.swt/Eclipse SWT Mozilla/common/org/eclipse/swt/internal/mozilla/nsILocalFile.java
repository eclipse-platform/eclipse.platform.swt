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

public class nsILocalFile extends nsIFile {

	static final int LAST_METHOD_ID = nsIFile.LAST_METHOD_ID + 17;

	public static final String NS_ILOCALFILE_IID_STRING =
		"aa610f20-a889-11d3-8c81-000064657374";

	public static final nsID NS_ILOCALFILE_IID =
		new nsID(NS_ILOCALFILE_IID_STRING);

	public nsILocalFile(int address) {
		super(address);
	}

	public int InitWithPath(int filePath) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), filePath);
	}

	public int InitWithNativePath(char[] filePath) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), filePath);
	}

	public int InitWithFile(int aFile) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aFile);
	}

	public int GetFollowLinks(boolean[] followLinks) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4,getAddress(), followLinks);
	}

	public int SetFollowLinks(boolean followLinks) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5,getAddress(), followLinks);
	}

	public int OpenNSPRFileDesc(int flags, int mode, int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), flags, mode, retVal);
	}

	public int OpenANSIFileDesc(byte[] mode, int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), mode, retVal);
	}

	public int Load(int[] retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), retVal);
	}

	public int GetDiskSpaceAvailable(long[] aDiskSpaceAvailable) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9,getAddress(), aDiskSpaceAvailable);
	}

	public int AppendRelativePath(int relativeFilePath) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), relativeFilePath);
	}

	public int AppendRelativeNativePath(char[] relativeFilePath) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), relativeFilePath);
	}

	public int GetPersistentDescriptor(int aPersistentDescriptor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12,getAddress(), aPersistentDescriptor);
	}

	public int SetPersistentDescriptor(char[] aPersistentDescriptor) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13,getAddress(), aPersistentDescriptor);
	}

	public int Reveal() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress());
	}

	public int Launch() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress());
	}

	public int GetRelativeDescriptor(int fromFile, int retVal) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), fromFile, retVal);
	}

	public int SetRelativeDescriptor(int fromFile, char[] relativeDesc) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), fromFile, relativeDesc);
	}
}
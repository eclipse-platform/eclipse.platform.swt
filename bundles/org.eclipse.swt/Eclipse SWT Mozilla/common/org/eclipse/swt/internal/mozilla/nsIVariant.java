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

public class nsIVariant extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 27 : 26);

	public static final String NS_IVARIANT_IID_STR =
		"6c9eb060-8c6a-11d5-90f3-0010a4e73d9a";

	public static final String NS_IVARIANT_10_IID_STR =
		"81e4c2de-acac-4ad6-901a-b5fb1b851a0d";

	public static final nsID NS_IVARIANT_IID =
		new nsID(NS_IVARIANT_IID_STR);

	public static final nsID NS_IVARIANT_10_IID =
		new nsID(NS_IVARIANT_10_IID_STR);

	public nsIVariant(long /*int*/ address) {
		super(address);
	}

	public int GetDataType(short[] aDataType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aDataType);
	}

	public int GetAsInt8(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), _retval);
	}

	public int GetAsInt16(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), _retval);
	}

	public int GetAsInt32(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), _retval);
	}

	public int GetAsInt64(long[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), _retval);
	}

	public int GetAsUint8(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), _retval);
	}

	public int GetAsUint16(short[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), _retval);
	}

	public int GetAsUint32(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), _retval);
	}

	public int GetAsUint64(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), _retval);
	}

	public int GetAsFloat(float[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), _retval);
	}

	public int GetAsDouble(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), _retval);
	}

	public int GetAsBool(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), _retval);
	}

	public int GetAsChar(byte[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), _retval);
	}

	public int GetAsWChar(char[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), _retval);
	}

	public int GetAsID(long /*int*/ retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), retval);
	}

	public int GetAsAString(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), _retval);
	}

	public int GetAsDOMString(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), _retval);
	}

	public int GetAsACString(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), _retval);
	}

	public int GetAsAUTF8String(long /*int*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), _retval);
	}

	public int GetAsString(long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), _retval);
	}

	public int GetAsWString(long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 21, getAddress(), _retval);
	}

	public int GetAsISupports(long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), _retval);
	}

	public int GetAsJSVal(long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), _retval);
	}

	public int GetAsInterface(long /*int*/[] iid, long /*int*/[] iface) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 24 : 23), getAddress(), iid, iface);
	}

	public int GetAsArray(short[] type, long /*int*/ iid, int[] count, long /*int*/[] ptr) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 25 : 24), getAddress(), type, iid, count, ptr);
	}

	public int GetAsStringWithSize(int[] size, long /*int*/[] str) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 26 : 25), getAddress(), size, str);
	}

	public int GetAsWStringWithSize(int[] size, long /*int*/[] str) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 27 : 26), getAddress(), size, str);
	}
}

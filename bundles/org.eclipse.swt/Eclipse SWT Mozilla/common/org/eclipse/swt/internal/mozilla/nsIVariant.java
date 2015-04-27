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


public class nsIVariant extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + ((IsXULRunner10() || IsXULRunner24() || IsXULRunner31()) ? 27 : 26);

	static final String NS_IVARIANT_IID_STR = "6c9eb060-8c6a-11d5-90f3-0010a4e73d9a";
	static final String NS_IVARIANT_10_IID_STR = "81e4c2de-acac-4ad6-901a-b5fb1b851a0d";

	static {
		IIDStore.RegisterIID(nsIVariant.class, MozillaVersion.VERSION_BASE, new nsID(NS_IVARIANT_IID_STR));
		IIDStore.RegisterIID(nsIVariant.class, MozillaVersion.VERSION_XR10, new nsID(NS_IVARIANT_10_IID_STR));
	}

	public nsIVariant(long /*int*/ address) {
		super(address);
	}

	public int GetDataType(short[] aDataType) {
		return XPCOM.VtblCall(this.getGetterIndex("dataType"), getAddress(), aDataType);
	}

	public int GetAsInt32(int[] _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("getAsInt32"), getAddress(), _retval);
	}

	public int GetAsDouble(long /*int*/ _retval) {
		return XPCOM.VtblCall(this.getMethodIndex("getAsDouble"), getAddress(), _retval);
	}

	public int GetAsBool(int[] _retval) {
		/* mozilla's representation of boolean changed from 4 bytes to 1 byte as of XULRunner 4.x */
		if (nsISupports.IsXULRVersionOrLater(MozillaVersion.VERSION_XR10)) {
			byte[] byteValue = new byte[1];
			int rc = XPCOM.VtblCall(this.getMethodIndex("getAsBool"), getAddress(), byteValue);
			_retval[0] = (int)byteValue[0];
			return rc;
		}
		return XPCOM.VtblCall(this.getMethodIndex("getAsBool"), getAddress(), _retval);
	}

	public int GetAsArray(short[] type, long /*int*/ iid, int[] count, long /*int*/[] ptr) {
		return XPCOM.VtblCall(this.getMethodIndex("getAsArray"), getAddress(), type, iid, count, ptr);
	}

	public int GetAsWStringWithSize(int[] size, long /*int*/[] str) {
		return XPCOM.VtblCall(this.getMethodIndex("getAsWStringWithSize"), getAddress(), size, str);
	}
}

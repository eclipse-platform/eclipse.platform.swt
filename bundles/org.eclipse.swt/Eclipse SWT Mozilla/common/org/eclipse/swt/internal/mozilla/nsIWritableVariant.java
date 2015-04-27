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


public class nsIWritableVariant extends nsIVariant {

	static final int LAST_METHOD_ID = nsIVariant.LAST_METHOD_ID + 31;

	static final String NS_IWRITABLEVARIANT_IID_STR = "5586a590-8c82-11d5-90f3-0010a4e73d9a";

	static {
		IIDStore.RegisterIID(nsIWritableVariant.class, MozillaVersion.VERSION_BASE, new nsID(NS_IWRITABLEVARIANT_IID_STR));
	}

	public nsIWritableVariant(long /*int*/ address) {
		super(address);
	}

	public int SetAsDouble(double aValue) {
		return XPCOM.VtblCall(this.getMethodIndex("setAsDouble"), getAddress(), aValue);
	}

	public int SetAsBool(int aValue) {
		/* mozilla's representation of boolean changed from 4 bytes to 1 byte as of XULRunner 4.x */
		if (IsXULRVersionOrLater(MozillaVersion.VERSION_XR10)) {
			return XPCOM.VtblCall(this.getMethodIndex("setAsBool"), getAddress(), (byte)aValue);
		}
		return XPCOM.VtblCall(this.getMethodIndex("setAsBool"), getAddress(), aValue);
	}

	public int SetAsArray(short type, long /*int*/ iid, int count, long /*int*/ ptr) {
		return XPCOM.VtblCall(this.getMethodIndex("setAsArray"), getAddress(), type, iid, count, ptr);
	}

	public int SetAsWStringWithSize(int size, char[] str) {
		return XPCOM.VtblCall(this.getMethodIndex("setAsWStringWithSize"), getAddress(), size, str);
	}

	public int SetAsEmpty() {
		return XPCOM.VtblCall(this.getMethodIndex("setAsEmpty"), getAddress());
	}

	public int SetAsEmptyArray() {
		return XPCOM.VtblCall(this.getMethodIndex("setAsEmptyArray"), getAddress());
	}
}

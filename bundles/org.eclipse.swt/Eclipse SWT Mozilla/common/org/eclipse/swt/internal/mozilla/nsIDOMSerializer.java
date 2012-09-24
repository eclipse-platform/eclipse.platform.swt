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

public class nsIDOMSerializer extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_IDOMSERIALIZER_IID_STR =
		"a6cf9123-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMSERIALIZER_IID =
		new nsID(NS_IDOMSERIALIZER_IID_STR);

	public nsIDOMSerializer(long /*int*/ address) {
		super(address);
	}

	public int SerializeToString(long /*int*/ root, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), root, _retval);
	}

	public int SerializeToStream(long /*int*/ root, long /*int*/ stream, byte[] charset) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), root, stream, charset);
	}
}

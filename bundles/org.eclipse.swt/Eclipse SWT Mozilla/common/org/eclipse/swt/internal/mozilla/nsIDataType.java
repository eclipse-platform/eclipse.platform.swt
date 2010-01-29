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

public class nsIDataType extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 0;

	public static final String NS_IDATATYPE_IID_STR =
		"4d12e540-83d7-11d5-90ed-0010a4e73d9a";

	public static final nsID NS_IDATATYPE_IID =
		new nsID(NS_IDATATYPE_IID_STR);

	public nsIDataType(int /*long*/ address) {
		super(address);
	}

	public static final short VTYPE_INT8 = 0;

	public static final short VTYPE_INT16 = 1;

	public static final short VTYPE_INT32 = 2;

	public static final short VTYPE_INT64 = 3;

	public static final short VTYPE_UINT8 = 4;

	public static final short VTYPE_UINT16 = 5;

	public static final short VTYPE_UINT32 = 6;

	public static final short VTYPE_UINT64 = 7;

	public static final short VTYPE_FLOAT = 8;

	public static final short VTYPE_DOUBLE = 9;

	public static final short VTYPE_BOOL = 10;

	public static final short VTYPE_CHAR = 11;

	public static final short VTYPE_WCHAR = 12;

	public static final short VTYPE_VOID = 13;

	public static final short VTYPE_ID = 14;

	public static final short VTYPE_DOMSTRING = 15;

	public static final short VTYPE_CHAR_STR = 16;

	public static final short VTYPE_WCHAR_STR = 17;

	public static final short VTYPE_INTERFACE = 18;

	public static final short VTYPE_INTERFACE_IS = 19;

	public static final short VTYPE_ARRAY = 20;

	public static final short VTYPE_STRING_SIZE_IS = 21;

	public static final short VTYPE_WSTRING_SIZE_IS = 22;

	public static final short VTYPE_UTF8STRING = 23;

	public static final short VTYPE_CSTRING = 24;

	public static final short VTYPE_ASTRING = 25;

	public static final short VTYPE_EMPTY_ARRAY = 254;

	public static final short VTYPE_EMPTY = 255;
}

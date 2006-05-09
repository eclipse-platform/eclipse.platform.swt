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
 * -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsID {
	
	public int m0;
	public short m1;
	public short m2;
	public byte[] m3 = new byte[8];
	public static final int sizeof = 16;

public nsID() {
}

public nsID(String id) {
	Parse(id);
}
	
public boolean Equals(nsID other) {
	int /*long*/ ptr = XPCOM.nsID_new();
	XPCOM.memmove(ptr, this, nsID.sizeof);
	int /*long*/ otherPtr = XPCOM.nsID_new();
	XPCOM.memmove(otherPtr, other, nsID.sizeof);
	boolean result = XPCOM.nsID_Equals(ptr, otherPtr);
	XPCOM.nsID_delete(ptr);
	XPCOM.nsID_delete(otherPtr);
	return result;
}

public boolean Parse(String aIDStr) {
	int /*long*/ ptr = XPCOM.nsID_new();
	boolean result = XPCOM.nsID_Parse(ptr, aIDStr);
	XPCOM.memmove(this, ptr, nsID.sizeof);
	XPCOM.nsID_delete(ptr);
	return result;
}	
}
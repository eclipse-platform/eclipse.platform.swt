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

/** @jniclass flags=cpp */
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
	int /*long*/ ptr = XPCOM.nsID_new ();
	XPCOM.memmove (ptr, this, nsID.sizeof);
	int /*long*/ otherPtr = XPCOM.nsID_new ();
	XPCOM.memmove (otherPtr, other, nsID.sizeof);
	boolean result = XPCOM.nsID_Equals (ptr, otherPtr) != 0;
	XPCOM.nsID_delete (ptr);
	XPCOM.nsID_delete (otherPtr);
	return result;
}

public void Parse (String aIDStr) {
	if (aIDStr == null) throw new Error ();
	int i = 0;
	for (; i < 8; i++) m0 = (m0 << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16);
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 13; i++) m1 = (short)((m1 << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 18; i++) m2 = (short)((m2 << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 21; i++) m3[0] = (byte)((m3[0] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 23; i++) m3[1] = (byte)((m3[1] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 26; i++) m3[2] = (byte)((m3[2] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 28; i++) m3[3] = (byte)((m3[3] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 30; i++) m3[4] = (byte)((m3[4] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 32; i++) m3[5] = (byte)((m3[5] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 34; i++) m3[6] = (byte)((m3[6] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
	for (; i < 36; i++) m3[7] = (byte)((m3[7] << 4) + Integer.parseInt (aIDStr.substring (i, i + 1), 16));
}

}
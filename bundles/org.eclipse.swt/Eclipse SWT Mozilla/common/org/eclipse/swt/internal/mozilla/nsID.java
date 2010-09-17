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

	static final String zeros = "00000000"; //$NON-NLS-1$

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

public void Parse(String aIDStr) {
	if(aIDStr == null) throw new Error ();
	int i = 0;
	for (; i < 8; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m0 = (m0 << 4) + digit;
	}
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 13; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m1 = (short)((m1 << 4) + digit);
	}
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 18; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m2 = (short)((m2 << 4) + digit);
	}
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 21; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[0] = (byte)((m3[0] << 4) + digit);
	}
	for (; i < 23; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[1] = (byte)((m3[1] << 4) + digit);
	}
	if (aIDStr.charAt (i++) != '-') throw new Error ();
	for (; i < 26; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[2] = (byte)((m3[2] << 4) + digit);
	}
	for (; i < 28; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[3] = (byte)((m3[3] << 4) + digit);
	}
	for (; i < 30; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[4] = (byte)((m3[4] << 4) + digit);
	}
	for (; i < 32; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[5] = (byte)((m3[5] << 4) + digit);
	}
	for (; i < 34; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[6] = (byte)((m3[6] << 4) + digit);
	}
	for (; i < 36; i++) {
		int digit = Character.digit (aIDStr.charAt (i), 16);
		if (digit == -1) throw new Error ();
		m3[7] = (byte)((m3[7] << 4) + digit);
	}
}

static String toHex (int v, int length) {
	String t = Integer.toHexString (v).toUpperCase ();
	int tlen = t.length ();
	if (tlen > length) {
		t = t.substring (tlen - length);
	}
	return zeros.substring (0, Math.max (0, length - tlen)) + t;
}

public String toString () {
	return '{' + toHex (m0, 8) + '-' + 
    	toHex (m1, 4) + '-' + 
    	toHex (m2, 4) + '-' + 
    	toHex (m3[0], 2) + toHex (m3[1], 2) + '-' + 
    	toHex (m3[2], 2) + toHex (m3[3], 2) + toHex (m3[4], 2) + toHex (m3[5], 2) + toHex (m3[6], 2) + toHex (m3[7], 2) + '}';
}

}

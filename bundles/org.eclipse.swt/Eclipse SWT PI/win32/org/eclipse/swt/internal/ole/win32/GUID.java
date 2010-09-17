/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public final class GUID {
	public int Data1;
	public short Data2;
	public short Data3;
	public byte[] Data4 = new byte[8];
	public static final int sizeof = COM.GUID_sizeof ();

	static final String zeros = "00000000"; //$NON-NLS-1$

static String toHex (int v, int length) {
	String t = Integer.toHexString (v).toUpperCase ();
	int tlen = t.length ();
	if (tlen > length) {
		t = t.substring (tlen - length);
	}
	return zeros.substring (0, Math.max (0, length - tlen)) + t;
}

public String toString () {
	return '{' + toHex (Data1, 8) + '-' + 
    	toHex (Data2, 4) + '-' + 
    	toHex (Data3, 4) + '-' + 
    	toHex (Data4[0], 2) + toHex (Data4[1], 2) + '-' + 
    	toHex (Data4[2], 2) + toHex (Data4[3], 2) + toHex (Data4[4], 2) + toHex (Data4[5], 2) + toHex (Data4[6], 2) + toHex (Data4[7], 2) + '}';
}

}

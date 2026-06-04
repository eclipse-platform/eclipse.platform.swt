/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

import java.util.Arrays;

/**
 * A char[] buffer wrapper with optional null-terminator support,
 * used to pass Java strings to Win32 Unicode (W) APIs via JNI.
 *
 * @jniclass flags=no_gen
 */
public class TCHAR {
	public char [] chars;

public final static int sizeof = 2;

public TCHAR (int length) {
	chars = new char [length];
}

public TCHAR (char ch, boolean terminate) {
	this (terminate ? new char [] {ch, '\0'} : new char [] {ch}, false);
}

public TCHAR (char [] chars, boolean terminate) {
	int charCount = chars.length;
	if (terminate) {
		if (charCount == 0 || (charCount > 0 && chars [charCount - 1] != 0)) {
			char [] newChars = new char [charCount + 1];
			System.arraycopy (chars, 0, newChars, 0, charCount);
			chars = newChars;
		}
	}
	this.chars = chars;
}

public TCHAR (String string, boolean terminate) {
	this (getChars (string, terminate), false);
}

static char [] getChars (String string, boolean terminate) {
	int length = string.length ();
	char [] chars = new char [length + (terminate ? 1 : 0)];
	string.getChars (0, length, chars, 0);
	return chars;
}

public void clear() {
	Arrays.fill (chars, (char) 0);
}

public int length () {
	return chars.length;
}

public int strlen () {
	for (int i=0; i<chars.length; i++) {
		if (chars [i] == '\0') return i;
	}
	return chars.length;
}

public int tcharAt (int index) {
	return chars [index];
}

@Override
public String toString () {
	return toString (0, length ());
}

public String toString (int start, int length) {
	return new String (chars, start, length);
}

}


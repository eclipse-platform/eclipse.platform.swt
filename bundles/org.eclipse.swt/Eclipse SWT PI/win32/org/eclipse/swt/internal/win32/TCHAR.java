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
 * This class implements the conversions between unicode characters
 * and the <em>platform supported</em> representation for characters.
 * <p>
 * Note that unicode characters which can not be found in the platform
 * encoding will be converted to an arbitrary platform specific character.
 * </p>
 *
 * @jniclass flags=no_gen
 */
public class TCHAR {
	public char [] chars;

public final static int sizeof = 2;

public TCHAR (int codePage, int length) {
	chars = new char [length];
}

public TCHAR (int codePage, char ch, boolean terminate) {
	this (codePage, terminate ? new char [] {ch, '\0'} : new char [] {ch}, false);
}

public TCHAR (int codePage, char [] chars, boolean terminate) {
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

public TCHAR (int codePage, String string, boolean terminate) {
	this (codePage, getChars (string, terminate), false);
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


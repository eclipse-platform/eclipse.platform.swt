/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;


import org.eclipse.swt.internal.gtk.OS;

/**
 * This class implements the conversions between unicode characters
 * and the <em>platform supported</em> representation for characters.
 * <p>
 * Note that, unicode characters which can not be found in the platform
 * encoding will be converted to an arbitrary platform specific character.
 * </p>
 */
public final class Converter {
	public static final byte [] NullByteArray = new byte [1];
	public static final byte [] EmptyByteArray = new byte [0];
	public static final char [] EmptyCharArray = new char [0];

/**
 * Returns the default code page for the platform where the
 * application is currently running.
 *
 * @return the default code page
 */
public static String defaultCodePage () {
	return "UTF8";
}

public static char [] mbcsToWcs (String codePage, byte [] buffer) {
	int /*long*/ [] items_written = new int /*long*/ [1];
	int /*long*/ ptr = OS.g_utf8_to_utf16 (buffer, buffer.length, null, items_written, null);
	if (ptr == 0) return EmptyCharArray;
	int length = (int)/*64*/items_written [0];
	char [] chars = new char [length];
	OS.memmove (chars, ptr, length * 2);
	OS.g_free (ptr);
	return chars;
}

public static byte [] wcsToMbcs (String codePage, String string, boolean terminate) {
	int length = string.length ();
	char [] buffer = new char [length];
	string.getChars (0, length, buffer, 0);
	return wcsToMbcs (codePage, buffer, terminate);
}

public static byte [] wcsToMbcs (String codePage, char [] buffer, boolean terminate) {
	int /*long*/ [] items_read = new int /*long*/ [1], items_written = new int /*long*/ [1];
	/*
	* Note that g_utf16_to_utf8()  stops converting 
	* when it finds the first NULL.
	*/
	int /*long*/ ptr = OS.g_utf16_to_utf8 (buffer, buffer.length, items_read, items_written, null);
	if (ptr == 0) return terminate ? NullByteArray : EmptyByteArray;
	int written = (int)/*64*/items_written [0];
	byte [] bytes = new byte [written + (terminate ? 1 : 0)];
	OS.memmove (bytes, ptr, written);
	OS.g_free (ptr);
	return bytes;
}

}

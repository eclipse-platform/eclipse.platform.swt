package org.eclipse.swt.internal;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

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
	int [] items_written = new int [1];
	int ptr = OS.g_utf8_to_utf16 (buffer, buffer.length, null, items_written, null);
	if (ptr == 0) return EmptyCharArray;
	int length = items_written [0];
	char [] chars = new char [length];
	OS.memmove (chars, ptr, length * 2);
	OS.g_free (ptr);
	return chars;
}

public static byte [] wcsToMbcs (String codePage, String string) {
	return wcsToMbcs (codePage, string, false);
}

public static byte [] wcsToMbcs (String codePage, String string, boolean terminate) {
	int length = string.length ();
	char [] buffer = new char [length];
	string.getChars (0, length, buffer, 0);
	return wcsToMbcs (codePage, buffer, terminate);
}

public static byte [] wcsToMbcs (String codePage, char [] buffer) {
	return wcsToMbcs (codePage, buffer, false);
}

public static byte [] wcsToMbcs (String codePage, char [] buffer, boolean terminate) {
	int [] items_read = new int [1], items_written = new int [1];
	int ptr = OS.g_utf16_to_utf8 (buffer, buffer.length, items_read, items_written, null);
	if (ptr == 0) return terminate ? NullByteArray : EmptyByteArray;
	int written = items_written [0];
	//TEMPORARY CODE - convertion stops at the first NULL
	if (items_read [0] != buffer.length) written++;
	byte [] bytes = new byte [written + (terminate ? 1 : 0)];
	OS.memmove (bytes, ptr, written);
	OS.g_free (ptr);
	return bytes;
}

}

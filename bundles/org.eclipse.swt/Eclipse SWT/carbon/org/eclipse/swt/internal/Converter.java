package org.eclipse.swt.internal;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;

/**
 * This class implements the conversions between unicode characters
 * and the <em>platform supported</em> representation for characters.
 * <p>
 * Note that, unicode characters which can not be found in the platform
 * encoding will be converted to an arbitrary platform specific character.
 * </p>
 */
 
public final class Converter {

	static final byte [] NULL_BYTE_ARRAY = new byte [1];
	static final byte [] EMPTY_BYTE_ARRAY = new byte [0];
	static final char [] EMPTY_CHAR_ARRAY = new char [0];
	
	static String CodePage;
	static byte[] Unicode;
		
	
	static {	
		Unicode = getAsciiBytes("UCS-2");
	}

/**
 * Returns the default code page for the platform where the
 * application is currently running.
 *
 * @return the default code page
 */	
public static String defaultCodePage () {
	return CodePage;
}

static byte[] getAsciiBytes (String str) {
	int length = str.length ();
	byte [] buffer = new byte [length + 1];
	for (int i=0; i<length; i++) {
		buffer [i] = (byte)str.charAt (i);
	}
	return buffer;
}

static String getAsciiString (byte [] buffer) {
	int length = buffer.length;
	char [] chars = new char [length];
	for (int i=0; i<length; i++) {
		chars [i] = (char)buffer [i];
	}
	return new String (chars);
}

/**
 * Converts an array of bytes representing the platform's encoding,
 * in the given code page, of some character data into an array of
 * matching unicode characters.
 *
 * @param codePage the code page to use for conversion
 * @param buffer the array of bytes to be converted
 * @return the unicode conversion
 */
public static char [] mbcsToWcs (String codePage, byte [] buffer) {

	/* Check for the simple cases */
	if (buffer == null) {
		return EMPTY_CHAR_ARRAY;
	}
	int length = buffer.length;
	if (length == 0) {
		return EMPTY_CHAR_ARRAY;
	}
	
	String s= new String(buffer);
	int n= s.length();
	char[] chars= new char[n];
	s.getChars(0, n, chars, 0);
	return chars;
}

/**
 * Free any cached resources.
 */	
public static void release () {
}

/**
 * Converts an array of chars (containing unicode data) to an array
 * of bytes representing the platform's encoding, of those characters
 * in the given code page.
 *
 * @param codePage the code page to use for conversion
 * @param buffer the array of chars to be converted
 * @return the platform encoding
 */
public static byte [] wcsToMbcs (String codePage, char [] buffer) {
	return wcsToMbcs (codePage, buffer, false);
}

/**
 * Converts an array of chars (containing unicode data) to an array
 * of bytes representing the platform's encoding, of those characters
 * in the given code page. If the termination flag is true, the resulting
 * byte data will be null (zero) terminated.
 *
 * @param codePage the code page to use for conversion
 * @param buffer the array of chars to be converted
 * @param terminate <code>true</code> if the result should be null terminated and false otherwise.
 * @return the platform encoding
 */
public static byte [] wcsToMbcs (String codePage, char [] buffer, boolean terminate) {

	/* Check for the simple cases */
	if (buffer == null) {
		return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
	}
	int length = buffer.length;
	if (length == 0) {
		return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
	}

	String s= new String(buffer);
	byte[] b= s.getBytes();
	if (!terminate)
		return b;
		
	byte[] b2= new byte[b.length+1];
	System.arraycopy(b, 0, b2, 0, b.length);
	return b2;
}

/**
 * Converts a String (containing unicode data) to an array
 * of bytes representing the platform's encoding, of those characters
 * in the given code page.
 *
 * @param codePage the code page to use for conversion
 * @param string the string to be converted
 * @return the platform encoding
 */
public static byte [] wcsToMbcs (String codePage, String string) {
	return wcsToMbcs (codePage, string, false);
}

/**
 * Converts a String (containing unicode data) to an array
 * of bytes representing the platform's encoding, of those characters
 * in the given code page. If the termination flag is true, the resulting
 * byte data will be null (zero) terminated.
 *
 * @param codePage the code page to use for conversion
 * @param string the string to be converted
 * @param terminate <code>true</code> if the result should be null terminated and false otherwise.
 * @return the platform encoding
 */
public static byte [] wcsToMbcs (String codePage, String string, boolean terminate) {
	if (terminate) {
		if (string == null) return NULL_BYTE_ARRAY;
		int count = string.length ();
		char [] buffer = new char [count + 1];
		string.getChars (0, count, buffer, 0);
		return wcsToMbcs (codePage, buffer, false);
	} else {
		if (string == null) return EMPTY_BYTE_ARRAY;
		int count = string.length ();
		char [] buffer = new char [count];
		string.getChars (0, count, buffer, 0);
		return wcsToMbcs (codePage, buffer, false);
	}
}

}

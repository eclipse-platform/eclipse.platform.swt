package org.eclipse.swt.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.win32.*;

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
	
	static int CodePage;
	static {
		CodePage = OS.GetACP ();
	}

/**
 * Returns the default code page for the platform where the
 * application is currently running.
 *
 * @return the default code page
 */	
public static int defaultCodePage () {
	return CodePage;
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
public static char [] mbcsToWcs (int codePage, byte [] buffer) {
	
	/* Check for the simple cases */
	if (codePage < 0 || buffer == null) {
		return EMPTY_CHAR_ARRAY;
	}
	int length = buffer.length;
	if (length == 0) {
		return EMPTY_CHAR_ARRAY;
	}
	
	/*
	 * Optimize for English ASCII encoding.  If no conversion is
	 * performed, it is safe to return any object that will also not
	 * be converted if this routine is called again with the result.
	 * This ensures that double conversion will not be performed
	 * on the same bytes.  Note that this relies on the fact that
	 * lead bytes are never in the range 0..0x7F.
	 */	
	char [] lpWideCharStr = new char [length];
	for (int i=0; i<length; i++) {
		if ((buffer [i] & 0xFF) <= 0x7F) {
			lpWideCharStr [i] = (char) buffer [i]; // all bytes <= 0x7F, so no ((char) (buffer[i]&0xFF)) needed
		} else {
			/* Convert from DBCS to UNICODE */
			int cp = codePage != 0 ? codePage : CodePage;
			int cchWideChar = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, length, null, 0);
			if (cchWideChar == 0) return EMPTY_CHAR_ARRAY;
			lpWideCharStr = new char [cchWideChar];
			OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, length, lpWideCharStr, cchWideChar);
			return lpWideCharStr;
		}
	}
	return lpWideCharStr;
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
public static byte [] wcsToMbcs (int codePage, char [] buffer) {
	return wcsToMbcs (0, buffer, false);
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
public static byte [] wcsToMbcs (int codePage, char [] buffer, boolean terminate) {

	/* Check for the simple cases */
	if (codePage < 0 || buffer == null) {
		return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
	}
	int length = buffer.length;
	if (length == 0) {
		return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
	}

	/*
	 * Optimize for English ASCII encoding.  This optimization
	 * relies on the fact that lead bytes can never be in the
	 * range 0..0x7F.
	 */
	byte [] mbcs = new byte [(terminate) ? length + 1 : length];
	for (int i=0; i<length; i++) {
		if ((buffer [i] & 0xFFFF) <= 0x7F) {
			mbcs [i] = (byte) buffer [i];
		} else {
			/* Convert from UNICODE to DBCS */
			int cp = codePage != 0 ? codePage : CodePage;
			int cchMultiByte = OS.WideCharToMultiByte (cp, 0, buffer, length, null, 0, null, null);
			if (cchMultiByte == 0) return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
			byte [] lpMultiByteStr = new byte [(terminate) ? cchMultiByte + 1 : cchMultiByte];
			OS.WideCharToMultiByte (cp, 0, buffer, length, lpMultiByteStr, cchMultiByte, null, null);
			return lpMultiByteStr;
		}
	}
	return mbcs;
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
public static byte [] wcsToMbcs (int codePage, String string) {
	return wcsToMbcs (0, string, false);
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
public static byte [] wcsToMbcs (int codePage, String string, boolean terminate) {
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

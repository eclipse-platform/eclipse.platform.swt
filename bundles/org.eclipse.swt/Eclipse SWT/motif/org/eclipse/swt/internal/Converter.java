package org.eclipse.swt.internal;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.motif.*;

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

	static final Object LOCK = new Object ();
	
	/* Converter cache */
	static String LastMBToWCCodePage;
	static String LastWCToMBCodePage;
	static int LastWCToMB;
	static int LastMBToWC;
	
	/* Buffers cache */
	static int BufferSize;
	static int BufferTimes2;
	static int BufferTimes4;
	
	static {			
		Unicode = getAsciiBytes("UCS-2");

		int length, item = OS.nl_langinfo (OS.CODESET);
		if (item != 0 && (length = OS.strlen (item)) > 0) {
			byte [] buffer = new byte [length];
			OS.memmove (buffer, item, length);
			CodePage = new String (buffer);
			if (OS.IsSunOS) {
				if (length > 3 && CodePage.indexOf ("ISO") == 0) {
					CodePage = CodePage.substring (3, length);
				}
			}
		} else {
			if (OS.IsLinux) CodePage = "ISO-8859-1";
			else if (OS.IsAIX) CodePage = "ISO8859-1";
			else if (OS.IsSunOS) CodePage = "8859-1";
			else CodePage = "iso8859_1";
		}
		
		BufferSize = 512;
		BufferTimes2 = OS.XtMalloc (BufferSize * 2);
		BufferTimes4 = OS.XtMalloc (BufferSize * 4);
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
	
	/*
	 * Optimize for English ASCII encoding.  If no conversion is
	 * performed, it is safe to return any object that will also not
	 * be converted if this routine is called again with the result.
	 * This ensures that double conversion will not be performed
	 * on the same bytes.  Note that this relies on the fact that
	 * lead bytes are never in the range 0..0x7F.
	 */	
	char [] wideCharStr = new char [length];
	for (int i=0; i<length; i++) {
		if ((buffer [i] & 0xFF) <= 0x7F) {
			wideCharStr [i] = (char) buffer [i]; // all bytes <= 0x7F, so no ((char) (buffer[i]&0xFF)) needed
		} else {
			synchronized (LOCK) {
				String cp = codePage != null ? codePage : CodePage;
				if (LastMBToWC != 0 && !cp.equals (LastMBToWCCodePage)) {
					OS.iconv_close (LastMBToWC);
					LastMBToWC = 0;
				}
				if (LastMBToWC == 0) {
					LastMBToWCCodePage = cp;
					LastMBToWC = OS.iconv_open (Unicode, getAsciiBytes (cp));
					if (LastMBToWC == -1) LastMBToWC = 0;
				}
				int cd = LastMBToWC;
				if (cd == 0) return EMPTY_CHAR_ARRAY;
				int inBytes = length;
				int outBytes = length * 2;
				int ptr1, ptr2;
				if (length <= BufferSize * 2) {
					ptr1 = BufferTimes2;
					ptr2 = BufferTimes4;
				} else {
					ptr1 = OS.XtMalloc (inBytes);
					ptr2 = OS.XtMalloc (outBytes);
				}
				int [] inBuf = {ptr1};
				int [] inBytesLeft = {inBytes};
				int [] outBuf = {ptr2};
				int [] outBytesLeft = {outBytes};
				OS.memmove (ptr1, buffer, inBytes);
				int result = OS.iconv (cd, inBuf, inBytesLeft, outBuf, outBytesLeft);
				outBytes = outBuf [0] - ptr2;
				wideCharStr = new char [outBytes / 2];
				OS.memmove (wideCharStr, ptr2, outBytes);
				if (ptr1 != BufferTimes2) OS.XtFree (ptr1);
				if (ptr2 != BufferTimes4) OS.XtFree (ptr2);
			}
			return wideCharStr;
		}
	}
	return wideCharStr;
}

/**
 * Free any cached resources.
 */	
public static void release () {
	synchronized (LOCK) {
		if (BufferTimes2 != 0) OS.XtFree (BufferTimes2);
		if (BufferTimes4 != 0) OS.XtFree (BufferTimes4);
		if (LastWCToMB != 0) OS.iconv_close (LastWCToMB);
		if (LastMBToWC != 0) OS.iconv_close (LastMBToWC);
		LastMBToWC = LastWCToMB = BufferTimes4 = BufferTimes2 = 0;
	}
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
			synchronized (LOCK) {
				String cp = codePage != null ? codePage : CodePage;
				if (LastWCToMB != 0 && !cp.equals (LastWCToMBCodePage)) {
					OS.iconv_close (LastWCToMB);
					LastWCToMB = 0;
				}
				if (LastWCToMB == 0) {
					LastWCToMBCodePage = cp;
					LastWCToMB = OS.iconv_open (getAsciiBytes (cp), Unicode);
					if (LastWCToMB == -1) LastWCToMB = 0;
				}
				int cd = LastWCToMB;
				if (cd == 0) return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
				int inBytes = length * 2;
				int outBytes = length * 4;
				int ptr1, ptr2;
				if (length <= BufferSize) {
					ptr1 = BufferTimes2;
					ptr2 = BufferTimes4;
				} else {
					ptr1 = OS.XtMalloc (inBytes);
					ptr2 = OS.XtMalloc (outBytes);
				}
				int [] inBuf = {ptr1};
				int [] inBytesLeft = {inBytes};
				int [] outBuf = {ptr2};
				int [] outBytesLeft = {outBytes};
				OS.memmove (ptr1, buffer, inBytes);
				while (inBytesLeft [0] > 0) {
					OS.iconv (cd, inBuf, inBytesLeft, outBuf, outBytesLeft);
					if (inBytesLeft [0] != 0) {
						inBuf [0] += 2;
						inBytesLeft [0] -= 2;
					}
				}
				outBytes = outBuf [0] - ptr2;
				mbcs = new byte [outBytes];
				OS.memmove (mbcs, ptr2, outBytes);
				if (ptr1 != BufferTimes2) OS.XtFree (ptr1);
				if (ptr2 != BufferTimes4) OS.XtFree (ptr2);
			}
			return mbcs;
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

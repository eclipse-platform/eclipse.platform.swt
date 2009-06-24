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
package org.eclipse.swt.internal;


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
	static final byte[] UCS2;
	static final byte[] UTF8;

	static final Object LOCK = new Object ();
	
	/* Converter cache */
	static boolean LastMbcsToUCS2Failed, LastUCS2ToMbcsFailed;
	static String LastMbcsToUCS2CodePage;
	static String LastUCS2ToMbcsCodePage;
	static int LastUCS2ToMbcs = -1;
	static int LastUTF8ToMbcs = -1;
	static int LastMbcsToUCS2 = -1;
	static int LastMbcsToUTF8 = -1;
	static int UTF8ToUCS2 = -1;
	static int UCS2ToUTF8 = -1;
	
	/* Buffers cache */
	static int BufferSize;
	static int MbcsBuffer, Ucs2Buffer, Utf8Buffer;
	
	static {
		if (OS.IsHPUX) {
			UCS2 = getAsciiBytes("ucs2");
			UTF8 = getAsciiBytes("utf8");
		} else {
			UCS2 = getAsciiBytes("UCS-2");
			UTF8 = getAsciiBytes("UTF-8");
		}

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
			else if (OS.IsHPUX) CodePage = "iso88591";
			else CodePage = "iso8859_1";
		}
		
		/*
		* The buffers can hold up to 512 unicode characters when converting
		* from UCS-2 to any MBCS (including UTF-8). And they can hold
		* at least 512 MBCS characters when converting from any MBCS to
		* UCS-2.
		*/
		BufferSize = 512;
		Ucs2Buffer = OS.XtMalloc (BufferSize * 2);
		Utf8Buffer = OS.XtMalloc (BufferSize * 6);
		MbcsBuffer = OS.XtMalloc (BufferSize * 6);
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
				/*
				* Feature in Solaris.  Some Solaris machines do not provide an iconv
				* decoder/encoder that converts directly from/to any MBCS encoding to/from
				* USC-2.  The fix is to convert to UTF-8 enconding first and them
				* convert to UCS-2. 
				*/
				String cp = codePage != null ? codePage : CodePage;
				if (cp != LastMbcsToUCS2CodePage && !cp.equals (LastMbcsToUCS2CodePage)) {
					if (LastMbcsToUCS2 != -1) OS.iconv_close (LastMbcsToUCS2);
					if (LastMbcsToUTF8 != -1) OS.iconv_close (LastMbcsToUTF8);
					LastMbcsToUCS2 = LastMbcsToUTF8 = -1;
					LastMbcsToUCS2CodePage = cp;
					LastMbcsToUCS2Failed = false;
				}
				int cd = LastMbcsToUCS2;
				if (cd == -1 && !LastMbcsToUCS2Failed) {
					cd = LastMbcsToUCS2 = OS.iconv_open (UCS2, getAsciiBytes (cp));
				}
				if (cd == -1) {
					LastMbcsToUCS2Failed = true;
					cd = UTF8ToUCS2;
					if (cd == -1) cd = UTF8ToUCS2 = OS.iconv_open (UCS2, UTF8);
					if (cd == -1) return EMPTY_CHAR_ARRAY;
					cd = LastMbcsToUTF8;
					if (cd == -1) cd = LastMbcsToUTF8 = OS.iconv_open (UTF8, getAsciiBytes (cp));
				}
				if (cd == -1) return EMPTY_CHAR_ARRAY;
				boolean utf8 = cd == LastMbcsToUTF8;
				int inByteCount = length;
				int outByteCount = utf8 ? length * 6 : length * 2;
				int ptr1 = 0, ptr2 = 0, ptr3 = 0;
				if (length <= BufferSize) {
					ptr1 = MbcsBuffer;
					ptr2 = Utf8Buffer;
					ptr3 = Ucs2Buffer;
				} else {
					ptr1 = OS.XtMalloc (inByteCount);
					if (utf8) ptr2 = OS.XtMalloc (length * 6);
					ptr3 = OS.XtMalloc (length * 2);
				}
				int ptr = utf8 ? ptr2 : ptr3;
				int [] inBuffer = {ptr1};
				int [] inBytesLeft = {inByteCount};
				int [] outBuffer = {ptr};
				int [] outBytesLeft = {outByteCount};
				OS.memmove (ptr1, buffer, inByteCount);
				OS.iconv (cd, inBuffer, inBytesLeft, outBuffer, outBytesLeft);
				outByteCount = outBuffer [0] - ptr;
				if (utf8) {
					cd = UTF8ToUCS2;
					inByteCount = outByteCount;
					outByteCount = length * 2;
					inBuffer[0] = ptr2;
					inBytesLeft[0] = inByteCount;
					outBuffer[0] = ptr3;
					outBytesLeft [0]= outByteCount;
					OS.iconv (cd, inBuffer, inBytesLeft, outBuffer, outBytesLeft);
					outByteCount = outBuffer [0] - ptr3;
				}
				wideCharStr = new char [outByteCount / 2];
				OS.memmove (wideCharStr, ptr3, outByteCount);
				if (ptr1 != 0 && ptr1 != MbcsBuffer) OS.XtFree (ptr1);
				if (ptr2 != 0 && ptr2 != Utf8Buffer) OS.XtFree (ptr2);
				if (ptr3 != 0 && ptr3 != Ucs2Buffer) OS.XtFree (ptr3);
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
		if (Ucs2Buffer != 0) OS.XtFree (Ucs2Buffer);
		if (Utf8Buffer != 0) OS.XtFree (Utf8Buffer);
		if (MbcsBuffer != 0) OS.XtFree (MbcsBuffer);
		if (LastUCS2ToMbcs != -1) OS.iconv_close (LastUCS2ToMbcs);
		if (LastUTF8ToMbcs != -1) OS.iconv_close (LastUTF8ToMbcs);
		if (LastMbcsToUCS2 != -1) OS.iconv_close (LastMbcsToUCS2);
		if (LastMbcsToUTF8 != -1) OS.iconv_close (LastMbcsToUTF8);
		if (UTF8ToUCS2 != -1) OS.iconv_close (UTF8ToUCS2);
		if (UCS2ToUTF8 != -1) OS.iconv_close (UCS2ToUTF8);
		LastUCS2ToMbcs = LastUTF8ToMbcs = LastMbcsToUCS2 = LastMbcsToUTF8 = UTF8ToUCS2 = UCS2ToUTF8 -1;
		Ucs2Buffer = Utf8Buffer = MbcsBuffer = 0;
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
				/*
				* Feature in Solaris.  Some Solaris machines do not provide an iconv
				* decoder/encoder that converts directly from/to any MBCS encoding to/from
				* USC-2.  The fix is to convert to UTF-8 enconding first and them
				* convert to UCS-2. 
				*/
				String cp = codePage != null ? codePage : CodePage;
				if (cp != LastUCS2ToMbcsCodePage && !cp.equals (LastUCS2ToMbcsCodePage)) {
					if (LastUCS2ToMbcs != -1) OS.iconv_close (LastUCS2ToMbcs);
					if (LastUTF8ToMbcs != -1) OS.iconv_close (LastUTF8ToMbcs);
					LastUCS2ToMbcs = LastUTF8ToMbcs = -1;
					LastUCS2ToMbcsCodePage = cp;
				}
				int cd = LastUCS2ToMbcs;
				if (cd == -1 && !LastUCS2ToMbcsFailed) {
					cd = LastUCS2ToMbcs = OS.iconv_open (getAsciiBytes (cp), UCS2);
				}
				if (cd == -1) {
					LastUCS2ToMbcsFailed = true;
					cd = LastUTF8ToMbcs;
					if (cd == -1) cd = LastUTF8ToMbcs = OS.iconv_open (getAsciiBytes (cp), UTF8);
					if (cd == -1) return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
					cd = UCS2ToUTF8;
					if (cd == -1) cd = UCS2ToUTF8 = OS.iconv_open (UTF8, UCS2);
				}				
				if (cd == -1) return (terminate) ? NULL_BYTE_ARRAY : EMPTY_BYTE_ARRAY;
				boolean utf8 = cd == UCS2ToUTF8;
				int inByteCount = length * 2;
				int outByteCount = length * 6;
				int ptr1 = 0, ptr2 = 0, ptr3 = 0;
				if (length <= BufferSize) {
					ptr1 = Ucs2Buffer;
					ptr2 = Utf8Buffer;
					ptr3 = MbcsBuffer;
				} else {
					ptr1 = OS.XtMalloc (inByteCount);
					if (utf8) ptr2 = OS.XtMalloc (outByteCount);
					ptr3 = OS.XtMalloc (outByteCount);
				}
				int ptr = utf8 ? ptr2 : ptr3;
				int [] inBuffer = {ptr1};
				int [] inBytesLeft = {inByteCount};
				int [] outBuffer = {ptr};
				int [] outBytesLeft = {outByteCount};
				OS.memmove (ptr1, buffer, inByteCount);
				while (inBytesLeft [0] > 0) {
					OS.iconv (cd, inBuffer, inBytesLeft, outBuffer, outBytesLeft);
					if (inBytesLeft [0] != 0) {
						inBuffer [0] += 2;
						inBytesLeft [0] -= 2;
					}
				}
				outByteCount = outBuffer [0] - ptr;
				if (utf8) {
					cd = LastUTF8ToMbcs;
					inByteCount = outByteCount;
					outByteCount = length * 6;
					inBuffer[0] = ptr2;
					inBytesLeft[0] = inByteCount;
					outBuffer[0] = ptr3;
					outBytesLeft [0]= outByteCount;
					OS.iconv (cd, inBuffer, inBytesLeft, outBuffer, outBytesLeft);
					outByteCount = outBuffer [0] - ptr3;
				}
				mbcs = new byte [terminate ? outByteCount + 1 : outByteCount];
				OS.memmove (mbcs, ptr3, outByteCount);
				if (ptr1 != 0 && ptr1 != Ucs2Buffer) OS.XtFree (ptr1);
				if (ptr2 != 0 && ptr2 != Utf8Buffer) OS.XtFree (ptr2);
				if (ptr3 != 0 && ptr3 != MbcsBuffer) OS.XtFree (ptr3);
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

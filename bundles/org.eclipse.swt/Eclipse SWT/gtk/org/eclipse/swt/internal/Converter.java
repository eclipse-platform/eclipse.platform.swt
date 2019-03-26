/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal;


import java.io.*;
import java.nio.*;
import java.nio.charset.*;

import org.eclipse.swt.internal.gtk.*;

/**
 * About this class:
 * #################
 * This class implements the conversions between unicode characters
 * and the platform supported representation for characters.
 *
 * Note that, unicode characters which can not be found in the platform
 * encoding will be converted to an arbitrary platform specific character.
 *
 * This class is tested via: org.eclipse.swt.tests.gtk.Test_GtkTextEncoding
 *
 * About JNI & string conversion:
 * #############################
 * - Regular JNI String conversion usually uses a modified UTF-8, see:  https://en.wikipedia.org/wiki/UTF-8#Modified_UTF-8
 * - And in JNI, normally (env*)->GetStringUTFChars(..) is used to convert a javaString into a C string.
 *     See: http://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/functions.html#GetStringUTFChars
 *
 * However, the modified UTF-8 only works well with C system functions as it doesn't contain embedded nulls
 * and is null terminated.
 *
 * But because the modified UTF-8 only supports up to 3 bytes (and not up to 4 as regular UTF-8), characters
 * that require 4 bytes (e.g emojos) are not translated properly from Java to C.
 *
 * To work around this issue, we convert the Java string to a byte array on the Java side manually and then  pass it to C.
 *   See: http://stackoverflow.com/questions/32205446/getting-true-utf-8-characters-in-java-jni
 *
 * Note:
 * Java uses UTF-16 Wide characters internally to represent a string.
 * C uses UTF-8 Multibyte characters (null terminated) to represent a string.
 *
 * About encoding on Linux/Gtk & it's relevance to SWT:
 * ####################################################
 *
 * UTF-* = variable length encoding.
 *
 * UTF-8 = minimum is 8 bits, max is 6 bytes, but rarely goes beyond 4 bytes. Gtk & most of web uses this.
 * UTF-16 = minimum is 16 bits. Java's string are stored this way.
 *          UTF-16 can be
 *            Big    Endian  : 65 = 00000000  01000001    # Human friendly, reads left to right.
 *            Little Endian  : 65 = 01000001  00000000    # Intel x86 and also AMD64 / x86-64 series of processors use the little-endian [1]
 *            											  #  i.e, we in SWT often have to deal with UTF-16 LE
 * Some terminology:
 * - "Code point" is the numerical value of unicode character.
 * - All of UTF-* have the same letter to code-point mapping,
 *   but UTF-8/16/32 have different "back-ends".
 *
 *   Illustration:
 *   (char) = (code point) = (back end).
 *       A =     65       = 01000001    	   UTF-8
 *       				  = 00000000  01000001 UTF-16 BE
 *       				  = 01000001  00000000 UTF-16 LE
 *
 * - Byte Order Marks (BOM) are a few bytes at the start of a *file* indicating which endianess is used.
 *   Problem: Gtk/webkit often don't give us BOM's.
 *   (further reading *3)
 *
 * - We can reliably encode character to a backend (A -> UTF-8/16), but the other way round is
 *   guess work since byte order marks are often missing and UTF-16 bits are technically valid UTF-8.
 *   (see Converter.heuristic for details).
 *   We could improve our heuristic by using something like http://jchardet.sourceforge.net/.
 *
 * - Glib has some conversion functions:
 *   g_utf16_to_utf8
 *   g_utf8_to_utf16
 *
 * - So does java: (e.g null terminated UTF-8)
 *   ("myString" + '\0').getBytes(StandardCharsets.UTF-8)
 *
 * - I suggest using Java functions where possible to avoid memory leaks.
 *   (Yes, they happen and are big-pain-in-the-ass to find https://bugs.eclipse.org/bugs/show_bug.cgi?id=533995)
 *
 *
 * Learning about encoding:
 * #########################
 * I suggest the following 3 videos to understand ASCII/UTF-8/UTF-16[LE|BE]/UTF-32 encoding:
 * Overview: https://www.youtube.com/watch?v=MijmeoH9LT4
 * Details:
 * Part-1: https://www.youtube.com/watch?v=B1Sf1IhA0j4
 * Part-2: https://www.youtube.com/watch?v=-oYfv794R9s
 * Part-3: https://www.youtube.com/watch?v=vLBtrd9Ar28
 *
 * Also read all of this:
 * http://kunststube.net/encoding/
 * and this:
 * https://www.joelonsoftware.com/2003/10/08/the-absolute-minimum-every-software-developer-absolutely-positively-must-know-about-unicode-and-character-sets-no-excuses/
 *
 * And lastly, good utf-8 reference: https://en.wikipedia.org/wiki/UTF-8#Description
 *
 * You should now be a master of encoding. I wish you luck on your journey.
 *
 * [1] https://en.wikipedia.org/wiki/Endianness
 * [2] https://en.wikipedia.org/wiki/Byte_order_mark
 * [3] BOM's: http://unicode.org/faq/utf_bom.html#BOM
 */
public final class Converter {
	public static final byte [] NullByteArray = new byte [1];
	public static final byte [] EmptyByteArray = new byte [0];
	public static final char [] EmptyCharArray = new char [0];


/**
 * Convert a "C" multibyte UTF-8 string byte array into a Java UTF-16 Wide character array.
 *
 * @param buffer - byte buffer with C bytes representing a string.
 * @return char array representing the string. Usually used for String construction like: new String(mbcsToWcs(..))
 */
public static char [] mbcsToWcs (byte [] buffer) {
	long [] items_written = new long [1];
	long ptr = OS.g_utf8_to_utf16 (buffer, buffer.length, null, items_written, null);
	if (ptr == 0) return EmptyCharArray;
	int length = (int)items_written [0];
	char [] chars = new char [length];
	C.memmove (chars, ptr, length * 2);
	OS.g_free (ptr);
	return chars;
}

/**
 * Convert a Java UTF-16 Wide character string into a C UTF-8 Multibyte byte array.
 *
 * This algorithm stops when it finds the first NULL character. I.e, if your Java String has embedded NULL
 * characters, then the returned string will only go up to the first NULL character.
 *
 *
 * @param string - a regular Java String
 * @param terminate - if <code>true</code> the byte buffer should be terminated with a null character.
 * @return byte array that can be passed to a native function.
 */
public static byte [] wcsToMbcs (String string, boolean terminate) {
	int length = string.length ();
	char [] buffer = new char [length];
	string.getChars (0, length, buffer, 0);
	return wcsToMbcs (buffer, terminate);
}

/**
 * Given a java String, convert it to a regular null terimnated C string,
 * to be used when calling a native C function.
 * @param string A java string.
 * @return a pointer to a C String. In C, this would be a 'char *'
 */
public static byte [] javaStringToCString (String string) {
	return wcsToMbcs(string, true);
}

/**
 * This method takes a 'C' pointer (char *) or (gchar *), reads characters up to the terminating symbol '\0' and
 * converts it into a Java String.
 *
 * Note: In SWT we don't use JNI's native String functions because of the 3 vs 4 byte issue explained in Class description.
 * Instead we pass a character pointer from C to java and convert it to a String in Java manually.
 *
 * @param cCharPtr - A char * or a gchar *. Which will be freed up afterwards.
 * @param freecCharPtr - "true" means free up memory pointed to by cCharPtr.
 * 			CAREFUL! If this string is part of a struct (ex GError), and a specialized
 * 			free function (like g_error_free(..) is called on the whole struct, then you
 * 			should not free up individual struct members with this function,
 * 			as otherwise you can get unpredictable behavior).
 * @return a Java String object.
 */
public static String cCharPtrToJavaString(long cCharPtr, boolean freecCharPtr) {
	int length = C.strlen (cCharPtr);
	byte[] buffer = new byte [length];
	C.memmove (buffer, cCharPtr, length);
	if (freecCharPtr) {
		OS.g_free (cCharPtr);
	}
	return new String (mbcsToWcs (buffer));
}

/**
 * Convert a Java UTF-16 Wide character array into a C UTF-8 Multibyte byte array.
 *
 * This algorithm stops when it finds the first NULL character. I.e, if your Java String has embedded NULL
 * characters, then the returned string will only go up to the first NULL character.
 *
 * @param chars - a regular Java String
 * @param terminate - if <code>true</code> the byte buffer should be terminated with a null character.
 * @return byte array that can be passed to a native function.
 */
public static byte [] wcsToMbcs (char [] chars, boolean terminate) {
	long [] items_read = new long [1], items_written = new long [1];
	/*
	* Note that g_utf16_to_utf8()  stops converting
	* when it finds the first NULL.
	*/
	long ptr = OS.g_utf16_to_utf8 (chars, chars.length, items_read, items_written, null);
	if (ptr == 0) return terminate ? NullByteArray : EmptyByteArray;
	int written = (int)items_written [0];
	byte [] bytes = new byte [written + (terminate ? 1 : 0)];
	C.memmove (bytes, ptr, written);
	OS.g_free (ptr);
	return bytes;
}



/**
 * Convert a Java UTF-16 Wide character into a single C UTF-8 Multibyte character
 * that you can pass to a native function.
 * @param ch - Java UTF-16 wide character.
 * @return C UTF-8 Multibyte character.
 */
public static char wcsToMbcs (char ch) {
	int key = ch & 0xFFFF;
	if (key <= 0x7F) return ch;
	byte [] buffer = wcsToMbcs (new char [] {ch}, false);
	if (buffer.length == 1) return (char) buffer [0];
	if (buffer.length == 2) {
		return (char) (((buffer [0] & 0xFF) << 8) | (buffer [1] & 0xFF));
	}
	return 0;
}

/**
 * Convert C UTF-8 Multibyte character into a Java UTF-16 Wide character.
 *
 * @param ch - C Multibyte UTF-8 character
 * @return Java UTF-16 Wide character
 */
public static char mbcsToWcs (char ch) {
	int key = ch & 0xFFFF;
	if (key <= 0x7F) return ch;
	byte [] buffer;
	if (key <= 0xFF) {
		buffer = new byte [1];
		buffer [0] = (byte) key;
	} else {
		buffer = new byte [2];
		buffer [0] = (byte) ((key >> 8) & 0xFF);
		buffer [1] = (byte) (key & 0xFF);
	}
	char [] result = mbcsToWcs (buffer);
	if (result.length == 0) return 0;
	return result [0];
}

/**
 * Given a byte array with unknown encoding, try to decode it via (relatively simple) heuristic.
 * This is useful when we're not provided the encoding by OS/library.<br>
 *
 * Current implementation only supports standard java charsets but can be extended as needed.
 * This method could be improved by using http://jchardet.sourceforge.net/ <br>
 *
 * Run time is O(a * n) where a is a constant that varies depending on the size of input n, but roughly 1-20)
 *
 * @param bytes raw bits from the OS.
 * @return String based on the most pop
 */
public static String byteToStringViaHeuristic(byte [] bytes) {
	/*
	 * Technical notes:
	 * - Given a sequence of bytes, UTF-8 and UTF-16 cannot determined deterministically (1*).
	 * - However, UTF-16 has a lot of null bytes when code points are mostly in the 0-255 range (using only 2nd byte),
	 *   a byte sequence with many null bytes is likely UTF-16.
	 * - Valid UTF-8 technically can contain null bytes, but it's rare.
	 *
	 * Some times it can get confused if it receives two non-null bytes. e.g (E with two dots on top) = (UTF-16  [01,04])
	 * It can either mean a valid set of UTF-8 characters or a single UTF-16 character.
	 * This issue typically only occurs for very short sequences 1-5 characters of very special characters).
	 * Improving the heuristic for such corner cases is complicated. We'd have to implement a mechanism
	 * that would be aware of character frequencies and assign a score to the probability of each mapping.
	 *
	 * [1] https://softwareengineering.stackexchange.com/questions/187169/how-to-detect-the-encoding-of-a-file
	 */
	// Base cases
	if ((bytes.length == 0) ||
		(bytes.length == 1 && bytes[0] == 0)) {
		return "";
	}

	// Test if it's valid UTF-8.
	// Note, ASCII is a subset of UTF-8.
	try {
		CharsetDecoder charDecoder = StandardCharsets.UTF_8.newDecoder();
		charDecoder.onMalformedInput(CodingErrorAction.REPORT);
		charDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
		String text = charDecoder.decode(ByteBuffer.wrap(bytes)).toString();

		// No exception thrown means that we have valid UTF-8 "bit string". However, valid UTF-8 bit string doesn't mean it's the corect decoding.
		// We have assert correctness via an educated guess
		boolean probablyUTF8 = true;

		{
			// Problem 1: It might be UTF-16 since at the binary level UTF-16 can be valid UTF-8. (null is a valid utf-8 character).
			// Solution: Count nulls to try to guess if it's UTF-16.
			// Verified via
			// 		org.eclipse.swt.tests.gtk.Test_GtkConverter.test_HeuristicUTF16_letters()
			// 		org.eclipse.swt.tests.gtk.Test_GtkConverter.test_HeuristicUTF16_letter()
			double nullBytePercentageForUtf16 = 0.01;  // if more than this % null bytes, then it's probably utf-16.
			int nullCount = 0;
			for (byte b : bytes) {
				if (b == 0)
					nullCount++;
			}
			double nullPercentage = (double) nullCount / (double) bytes.length;
			if (nullPercentage > nullBytePercentageForUtf16) {
				probablyUTF8 = false;
			}
		}

		// Problem 2: Valid UTF-8 bit string can map to invalid code points (i.e undefined unicode)
		// Solution 2: verify that every character is a valid code point.
		if (probablyUTF8) {
			char [] chars = text.toCharArray();

			for (int i = 0; i < chars.length; i++) {
				int codePoint = Character.codePointAt(chars, i);
				if (!Character.isValidCodePoint(codePoint)) {
					probablyUTF8 = false;
					break;
				}
			}
		}

		// Problem 3: Short 2-byte sequences are very ambiguous.
		//            E.g Unicode Hyphen U+2010 (which looks like a '-') ( which btw different from the ascii U+002D  '-' Hyphen-Minus)
		//		          can be miss-understood as 16 (Synchronous Idle) & 32 (Space).
		// Solution: Unless we have two valid alphabet characters, it's probably a single utf-16 character.
		//           However, this leads to the problem that single non-alphabetic unicode characters are not recognized correctly.
		//           Below code is left in case recognizing alphabetic characters is of higher priority than exotic unicode once.
//		if (probablyUTF8) {
//			if (bytes.length == 2) {
//				char [] chars = text.toCharArray();
//				for (int i = 0; i < chars.length; i++) {
//					int codePoint = Character.codePointAt(chars, i);
//					if (!Character.isAlphabetic(codePoint)) {
//						probablyUTF8 = false;
//						break;
//					}
//				}
//			}
//		}

		if (!probablyUTF8) {
			return new String (bytes, StandardCharsets.UTF_16LE);
		} else {
			return text;
		}
	} catch (CharacterCodingException e) {
	}

	// Invalid UTF-8. Try other character sets.
	Charset [] commonWebCharSets = new Charset[] {StandardCharsets.UTF_16LE, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_16BE, StandardCharsets.UTF_16};
	for (Charset setToTry : commonWebCharSets) {
		try {
			CharsetDecoder charDecoder = setToTry.newDecoder();
			charDecoder.onMalformedInput(CodingErrorAction.REPORT);
			charDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
			return charDecoder.decode(ByteBuffer.wrap(bytes)).toString();
		} catch (CharacterCodingException e) {}
	}

	// Could not determine encoding.
	// Return error string with stack trace to help users determine which function lead to a failed decoding.
	StringWriter sw = new StringWriter();
	new Throwable("").printStackTrace(new PrintWriter(sw));
	return "SWT: Failed to decode byte buffer. Encoding is not ASCII/UTF-8/UTF-16[LE|BE|BOM]/ISO_8859_1. Stack trace:\n" + sw.toString();
}

}

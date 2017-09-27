/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;


import org.eclipse.swt.internal.gtk.*;

/**
 * This class implements the conversions between unicode characters
 * and the <em>platform supported</em> representation for characters.
 * <p>
 * Note that, unicode characters which can not be found in the platform
 * encoding will be converted to an arbitrary platform specific character.
 *
 * Note:
 * Regular JNI String conversion usually uses a modified UTF-8, see:
 * https://en.wikipedia.org/wiki/UTF-8#Modified_UTF-8
 * And in JNI, normally (env*)->GetStringUTFChars(..) is used to convert a javaString into a C string. See:
 * http://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/functions.html#GetStringUTFChars
 * However, the modified UTF-8 only works well with C system functions as it doesn't contain embedded nulls
 * and is null terminated.
 * But because the modified UTF-8 only supports up to 3 bytes (and not up to 4 as regular UTF-8), characters
 * that require 4 bytes (e.g emojos) are not translated properly from Java to C.
 * To work around this issue, we convert the Java string to a byte array on the Java side manually and then
 * pass it to C. See:
 * http://stackoverflow.com/questions/32205446/getting-true-utf-8-characters-in-java-jni
 *
 * Note:
 * Java uses UTF-16 Wide characters internally to represent a string.
 * C uses UTF-8 Multibyte characters (null terminated) to represent a string.
 *
 * </p>
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
	long /*int*/ [] items_written = new long /*int*/ [1];
	long /*int*/ ptr = OS.g_utf8_to_utf16 (buffer, buffer.length, null, items_written, null);
	if (ptr == 0) return EmptyCharArray;
	int length = (int)/*64*/items_written [0];
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
public static String cCharPtrToJavaString(long /*int*/ cCharPtr, boolean freecCharPtr) {
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
	long /*int*/ [] items_read = new long /*int*/ [1], items_written = new long /*int*/ [1];
	/*
	* Note that g_utf16_to_utf8()  stops converting
	* when it finds the first NULL.
	*/
	long /*int*/ ptr = OS.g_utf16_to_utf8 (chars, chars.length, items_read, items_written, null);
	if (ptr == 0) return terminate ? NullByteArray : EmptyByteArray;
	int written = (int)/*64*/items_written [0];
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

}

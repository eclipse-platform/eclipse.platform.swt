package org.eclipse.swt.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
	public static final char [] NullCharArray = new char [1];
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
	//SLOW AND BOGUS
	String cp = codePage == null ? defaultCodePage () : codePage;
	try {
		return new String (buffer, cp).toCharArray ();
	} catch (Exception e) {
		return NullCharArray;
	}
}

/* TEMPORARY CODE */
public static byte [] wcsToMbcs (String codePage, String string) {
	return wcsToMbcs (codePage, string, false);
}

public static byte [] wcsToMbcs (String codePage, String string, boolean terminate) {
	//SLOW AND BOGUS
	int count = string.length ();
	if (terminate) count++;
	char [] buffer = new char [count];
	string.getChars (0, string.length (), buffer, 0);
	return wcsToMbcs (codePage, buffer, false);
}

/* TEMPORARY CODE */
public static byte [] wcsToMbcs (String codePage, char [] buffer) {
	return wcsToMbcs (codePage, buffer, false);
}

public static byte [] wcsToMbcs (String codePage, char [] buffer, boolean terminate) {
	//SLOW AND BOGUS
	String cp = codePage == null ? defaultCodePage () : codePage;
	try {
		if (!terminate) return new String (buffer).getBytes (cp);
		byte [] buffer1 = new String (buffer).getBytes (cp);
		byte [] buffer2 = new byte [buffer1.length + 1];
		System.arraycopy (buffer1, 0, buffer2, 0, buffer1.length);
		return buffer2;
	} catch (Exception e) {
		return terminate ? NullByteArray : EmptyByteArray;
	}
}

}

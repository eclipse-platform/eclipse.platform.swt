package org.eclipse.swt.internal;

import org.eclipse.swt.internal.photon.*;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
	int length = buffer.length;
	if (length == 0) return EmptyCharArray;
	if (buffer [length - 1] != 0) {
		byte [] newBuffer = new byte [length + 1];
		System.arraycopy (buffer, 0, newBuffer, 0, length);
		buffer = newBuffer; 	
	}
	String string = OS.NewStringUTF (buffer);
	if (string != null) return string.toCharArray ();
	return EmptyCharArray;
}
public static byte [] wcsToMbcs (String codePage, String string, boolean terminate) {
	int length = OS.GetStringUTFLength (string);
	if (length == 0) return terminate ? NullByteArray : EmptyByteArray;
	byte[] buffer = new byte [terminate ? length + 1 : length];
	OS.GetStringUTFRegion (string, 0, string.length (), buffer);	
	return buffer;
}
public static byte [] wcsToMbcs (String codePage, char [] buffer, boolean terminate) {
	return wcsToMbcs (codePage, new String (buffer), terminate);
}
}

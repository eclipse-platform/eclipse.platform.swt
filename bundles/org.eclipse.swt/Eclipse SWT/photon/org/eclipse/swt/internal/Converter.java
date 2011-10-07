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

import java.io.UnsupportedEncodingException;

 
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
	try {
		return new String (buffer, defaultCodePage ()).toCharArray ();
	} catch (UnsupportedEncodingException e) {
		return EmptyCharArray;
	}
}
public static byte [] wcsToMbcs (String codePage, String string, boolean terminate) {
	try {
		if (!terminate) return string.getBytes (defaultCodePage ());
		byte [] buffer1 = string.getBytes (defaultCodePage ());
		byte [] buffer2 = new byte [buffer1.length + 1];
		System.arraycopy (buffer1, 0, buffer2, 0, buffer1.length);
		return buffer2;
	} catch (UnsupportedEncodingException e) {
		return terminate ? NullByteArray : EmptyByteArray;
	}
}
public static byte [] wcsToMbcs (String codePage, char [] buffer, boolean terminate) {
	try {
		if (!terminate) return new String (buffer).getBytes (defaultCodePage ());
		byte [] buffer1 = new String (buffer).getBytes (defaultCodePage ());
		byte [] buffer2 = new byte [buffer1.length + 1];
		System.arraycopy (buffer1, 0, buffer2, 0, buffer1.length);
		return buffer2;
	} catch (UnsupportedEncodingException e) {
		return terminate ? NullByteArray : EmptyByteArray;
	}
}
}

package org.eclipse.swt.internal;

import java.util.*;
import java.io.*;

public class Compatability {

/**
 * Answers the double conversion of the most negative (i.e.
 * closest to negative infinity) integer value which is
 * greater than the argument.
 *
 * @author		OTI
 * @version		initial
 *
 * @param		d		the value to be converted
 * @return		the ceiling of the argument.
 */
public static double ceil (double d) {
	return Math.ceil(d);
}

/** Returns the power function.
 * 
 * @param a1
 * @param a2
 * @return the power
 */
public static int pow (int a1, int a2) {
	return Math.pow (a1, a2);
}

/**
 * Loads a library if the underlying platform supports this.
 * If not, it is assumed that the library in question was 
 * properly made available in some other fashion (that is,
 * it returns true in this case).
 *
 * @param name the name of the library to load
 * @return true if the library is available
 */
public static boolean loadLibrary (String name) {
	try {
		System.loadLibrary (name);
	} catch (UnsatisfiedLinkError e) {
		return false;
	}
	return true;
}

/**
 * Test if the character is a whitespace character.
 *
 * @param ch the character to test
 * @return true if the character is whitespace
 */
public static boolean isWhitespace (char ch) {
	return Character.isWhitespace (ch);
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static InputStream newFileInputStream (String filename) throws IOError {
	return new FileInputStream(filename);
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static OutputStream newFileOutputStream (String filename) throws IOError {
	return new FileOutputStream(filename);
}

private static ResourceBundle msgs = null;

/**
 * Returns the NLS'ed message for the given argument.
 * 
 * @param key the key to look up
 * @return the message for the given key
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 */
public static String getMessage(String key) {
	String answer = key;
	
	if (key == null) {
		error (ERROR_NULL_ARGUMENT);
	}
	if (msgs == null) {
		try {
			msgs = ResourceBundle.getBundle("org.eclipse.swt.SWTMessages");
		} catch (MissingResourceException ex) {
			answer = key + " (no resource bundle)";
		}
	}
	if (msgs != null) {
		try {
			answer = msgs.getString(key);
		} catch (MissingResourceException ex2) {}
	}
	return answer;
}
	
/**
 * Interrupt the current thread. Note that this is not
 * available on CLDC.
 */
public static void interrupt() {
	Thread.currentThread ().interrupt ();
}

}


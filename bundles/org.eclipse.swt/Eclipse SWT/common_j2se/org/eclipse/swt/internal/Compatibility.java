package org.eclipse.swt.internal;

import java.io.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.swt.SWT;

/**
 * This class is a placeholder for utility methods commonly
 * used on J2SE platforms but not supported on some J2ME
 * profiles.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms.
 * </p>
 * <p>
 * IMPORTANT: some of the methods have been modified from their
 * J2SE parents. For example, exceptions thrown may differ since
 * J2ME's set of exceptions is a subset of J2SE's one. Refer to
 * the description of each method for specific changes.
 * </p>
 */
public final class Compatibility {

/**
 * Answers the most negative (i.e. closest to negative infinity)
 * integer value which is greater than the given rational number.
 * 
 * @param p the numerator of the rational number
 * @param q the denominator of the rational number (must be different from zero)
 * @return the ceiling of the rational number.
 */
public static int ceil(int p, int q) {
	return (int)Math.ceil((float)p / q);
}

/**
 * Returns 2 raised to the power of the argument.
 *
 * @param n an int value between 0 and 30 (inclusive)
 * @return 2 raised to the power of the argument
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the argument is not between 0 and 30 (inclusive)</li>
 * </ul>
 */
public static int pow2(int n) {
	if (n >= 1 && n <= 30)
		return 2 << (n - 1);
	else if (n != 0) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return 1;
}

/**
 * Loads a library if the underlying platform supports this.
 * If not, it is assumed that the library in question was 
 * properly made available in some other fashion.
 *
 * @param name the name of the library to load
 *
 * @exception SecurityException
 *   if the library was not allowed to be loaded
 * @exception SWTError <ul>
 *    <li>ERROR_FAILED_LOAD_LIBRARY - if the library could not be loaded</li>
 * </ul>
 */
public static void loadLibrary(String name) {
	try {
		System.loadLibrary (name);
	} catch (UnsatisfiedLinkError e) {
		SWT.error(SWT.ERROR_FAILED_LOAD_LIBRARY,e);
	}
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static InputStream newFileInputStream(String filename) throws IOException {
	return new FileInputStream(filename);
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static OutputStream newFileOutputStream(String filename) throws IOException {
	return new FileOutputStream(filename);
}

/**
 * Answers whether the character is a letter.
 *
 * @param c the character
 * @return true when the character is a letter
 */
public static boolean isLetter(char c) {
	return Character.isLetter(c);
}

/**
 * Answers whether the character is a letter or a digit.
 *
 * @param c the character
 * @return true when the character is a letter or a digit
 */
public static boolean isLetterOrDigit(char c) {
	return Character.isLetterOrDigit(c);
}

/**
 * Answers whether the character is a Unicode space character.
 *
 * @param c	 the character
 * @return true when the character is a Unicode space character
 */
public static boolean isSpaceChar(char c) {
	return Character.isSpaceChar(c);
}

/**
 * Answers whether the character is a whitespace character.
 *
 * @param ch the character to test
 * @return true if the character is whitespace
 */
public static boolean isWhitespace(char c) {
	return Character.isWhitespace(c);
}

/**
 * Execute a program in a separate platform process if the
 * underlying platform support this.
 * <p>
 * The new process inherits the environment of the caller.
 * </p>
 *
 * @param program the name of the program to execute
 *
 * @exception IOException
 *  if the program cannot be executed
 * @exception SecurityException
 *  if the current SecurityManager disallows program execution
 */
public static void exec(String prog) throws java.io.IOException {
	Runtime.getRuntime().exec(prog);
}

/**
 * Execute progArray[0] in a separate platform process if the
 * underlying platform support this.
 * <p>
 * The new process inherits the environment of the caller.
 * <p>
 *
 * @param progArray array containing the program to execute and its arguments
 *
 * @exception IOException
 *  if the program cannot be executed
 * @exception	SecurityException
 *  if the current SecurityManager disallows program execution
 */
public static void exec(String[] progArray) throws java.io.IOException{
	Runtime.getRuntime().exec(progArray);
}

private static ResourceBundle msgs = null;

/**
 * Returns the NLS'ed message for the given argument. This is only being
 * called from SWT.
 * 
 * @param key the key to look up
 * @return the message for the given key
 * 
 * @see SWT#getMessage
 */
public static String getMessage(String key) {
	String answer = key;
	
	if (key == null) {
		SWT.error (SWT.ERROR_NULL_ARGUMENT);
	}	
	if (msgs == null) {
		try {
			msgs = ResourceBundle.getBundle("org.eclipse.swt.internal.SWTMessages");
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
 * Interrupt the current thread. 
 * <p>
 * Note that this is not available on CLDC.
 * </p>
 */
public static void interrupt() {
	Thread.currentThread().interrupt();
}

/**
 * Copies a range of characters into a new String.
 *
 * @param buffer the StringBuffer we want to copy from
 * @param start the offset of the first character
 * @param end the offset one past the last character
 * @return a new String containing the characters from start to end - 1
 *
 * @exception	IndexOutOfBoundsException 
 *   when <code>start < 0, start > end</code> or <code>end > length()</code>
 */
public static String substring(StringBuffer buffer, int start, int end) {
	return buffer.substring(start, end);
}

}

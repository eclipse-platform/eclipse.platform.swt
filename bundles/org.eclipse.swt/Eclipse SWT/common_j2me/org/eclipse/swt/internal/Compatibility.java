package org.eclipse.swt.internal;

import org.eclipse.swt.SWT;
import java.io.*;

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
	int res = p / q;
	if ((p % q == 0) || 
		(res < 0) ||
		((res == 0) && ((p < 0 && q > 0) || (p > 0 && q < 0))))
		return res;
	else
		return res + 1;
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
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static InputStream newFileInputStream (String filename) throws IOException {
	throw new IOException();
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 */
public static OutputStream newFileOutputStream (String filename) throws IOException {
	throw new IOException();
}

/**
 * Answers whether the character is a letter.
 *
 * @param c the character
 * @return true when the character is a letter
 */
public static boolean isLetter(char c) {
	return (Character.isUpperCase(c)) || (Character.isLowerCase(c));
}

/**
 * Answers whether the character is a letter or a digit.
 *
 * @param c the character
 * @return true when the character is a letter or a digit
 */
public static boolean isLetterOrDigit(char c) {
	return isLetter(c) || Character.isDigit(c);
}

/**
 * Answers whether the character is a Unicode space character.
 *
 * @param c	 the character
 * @return true when the character is a Unicode space character
 */
public static boolean isSpaceChar(char c) {
	return c == ' ';
}

/**
 * Answers whether the character is whitespace character.
 *
 * @param c the character
 * @return true when the character is a whitespace character
 */
public static boolean isWhitespace(char c) {
	// Optimized case for ASCII
	if ((c >= 0x1c && c <= 0x20) || (c >= 0x9 && c <= 0xd)) 
		return true;
	return false;
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
	throw new IOException();
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
	throw new IOException();
}

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
	
	if (key == null)
		SWT.error (SWT.ERROR_NULL_ARGUMENT);	
	if (key.equals( "SWT_Yes"))
		return "Yes";
	if (key.equals("SWT_No"))
		return "No";
	if (key.equals("SWT_OK")) 
		return "OK";
	if (key.equals("SWT_Cancel"))
	 	return "Cancel";
	if (key.equals("SWT_Abort"))
	 	return "Abort";
	if (key.equals("SWT_Retry"))
	 	return "Retry";
	if (key.equals("SWT_Ignore"))
	 	return "Ignore";
	if (key.equals("SWT_Sample"))
	 	return "Sample";
	if (key.equals("SWT_A_Sample_Text"))
	 	return "A Sample Text";
	if (key.equals("SWT_Selection"))
	 	return "Selection";
	if (key.equals("SWT_Current_Selection"))
	 	return "Current Selection";
	if (key.equals("SWT_Character_set"))
	 	return "Character set";
	if (key.equals("SWT_Font"))
	 	return "Font";
	if (key.equals("SWT_Extended_style"))
	 	return "Extended style";
	if (key.equals("SWT_Size"))
	 	return "Size";
	if (key.equals("SWT_Style"))
	 	return "Style";
	 	
	return key;
}
	
/**
 * Interrupt the current thread. 
 * <p>
 * Note that this is not available on CLDC.
 * </p>
 */
public static void interrupt() {
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
	return buffer.toString().substring(start, end);
}

}

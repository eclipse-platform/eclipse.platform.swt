/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

 
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;

import org.eclipse.swt.*;

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
 * J2SE parents. Refer to the description of each method for 
 * specific changes.
 * </p>
 * <ul>
 * <li>Exceptions thrown may differ since J2ME's set of 
 * exceptions is a subset of J2SE's one.
 * </li>
 * <li>The range of the mathematic functions is subject to
 * change.
 * </li>		
 * </ul>
 */
public final class Compatibility {

/**
 * Answers the most negative (i.e. closest to negative infinity)
 * integer value which is greater than the number obtained by dividing
 * the first argument p by the second argument q.
 * 
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the ceiling of the rational number p / q.
 */
public static int ceil(int p, int q) {
	return (int)Math.ceil((float)p / q);
}

/**
 * Answers whether the indicated file exists or not.
 * 
 * @param parent the file's parent directory
 * @param child the file's name
 * @return true if the file exists
 */
public static boolean fileExists(String parent, String child) {
	return new File (parent, child).exists();
}

/**
 * Answers the result of rounding to the closest integer the number obtained 
 * by dividing the first argument p by the second argument q.
 * <p>
 * IMPORTANT: the j2me version has an additional restriction on
 * the arguments. p must be within the range 0 - 32767 (inclusive).
 * q must be within the range 1 - 32767 (inclusive).
 * </p>
 * 
 * @param p numerator
 * @param q denominator (must be different from zero)
 * @return the closest integer to the rational number p / q
 */
public static int round(int p, int q) {
	return Math.round((float)p / q);
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
 * Create a DeflaterOutputStream if such things are supported.
 * 
 * @param stream the output stream
 * @return a deflater stream or <code>null</code>
 * @exception IOException
 * 
 * @since 3.4
 */
public static OutputStream newDeflaterOutputStream(OutputStream stream, int level) throws IOException {
	return new DeflaterOutputStream(stream, new Deflater(level));
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 * @exception IOException
 */
public static InputStream newFileInputStream(String filename) throws IOException {
	return new FileInputStream(filename);
}

/**
 * Open a file if such things are supported.
 * 
 * @param filename the name of the file to open
 * @return a stream on the file if it could be opened.
 * @exception IOException
 */
public static OutputStream newFileOutputStream(String filename) throws IOException {
	return new FileOutputStream(filename);
}

/**
 * Create an InflaterInputStream if such things are supported.
 * 
 * @param stream the input stream
 * @return a inflater stream or <code>null</code>
 * @exception IOException
 * 
 * @since 3.3
 */
public static InputStream newInflaterInputStream(InputStream stream) throws IOException {
	return new BufferedInputStream(new InflaterInputStream(stream));
}

/**
 * Execute prog[0] in a separate platform process if the
 * underlying platform supports this.
 * <p>
 * The new process inherits the environment of the caller.
 * <p>
 *
 * @param prog array containing the program to execute and its arguments
 * @param envp
 *            array of strings, each element of which has environment
 *            variable settings in the format name=value
 * @param workingDir
 *            the working directory of the new process, or null if the new
 *            process should inherit the working directory of the caller
 *
 * @exception IOException
 *  if the program cannot be executed
 * @exception	SecurityException
 *  if the current SecurityManager disallows program execution
 *
 * @since 3.6
 */
public static void exec(String[] prog, String[] envp, String workingDir) throws java.io.IOException{
	Runtime.getRuntime().exec(prog, null, workingDir != null ? new File(workingDir) : null);
}

private static ResourceBundle msgs = null;

/**
 * Returns the NLS'ed message for the given argument. This is only being
 * called from SWT.
 * 
 * @param key the key to look up
 * @return the message for the given key
 * 
 * @see SWT#getMessage(String)
 */
public static String getMessage(String key) {
	String answer = key;
	
	if (key == null) {
		SWT.error (SWT.ERROR_NULL_ARGUMENT);
	}	
	if (msgs == null) {
		try {
			msgs = ResourceBundle.getBundle("org.eclipse.swt.internal.SWTMessages"); //$NON-NLS-1$
		} catch (MissingResourceException ex) {
			answer = key + " (no resource bundle)"; //$NON-NLS-1$
		}
	}
	if (msgs != null) {
		try {
			answer = msgs.getString(key);
		} catch (MissingResourceException ex2) {}
	}
	return answer;
}

public static String getMessage(String key, Object[] args) {
	String answer = key;
	
	if (key == null || args == null) {
		SWT.error (SWT.ERROR_NULL_ARGUMENT);
	}
	if (msgs == null) {
		try {
			msgs = ResourceBundle.getBundle("org.eclipse.swt.internal.SWTMessages"); //$NON-NLS-1$
		} catch (MissingResourceException ex) {
			answer = key + " (no resource bundle)"; //$NON-NLS-1$
		}
	}
	if (msgs != null) {
		try {
			MessageFormat formatter = new MessageFormat("");			
			formatter.applyPattern(msgs.getString(key));			
			answer = formatter.format(args);
		} catch (MissingResourceException ex2) {}
	}
	return answer;
}

}

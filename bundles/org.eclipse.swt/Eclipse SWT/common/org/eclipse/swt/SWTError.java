package org.eclipse.swt;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;

/**
 * This error is thrown whenever an unrecoverable error
 * occurs internally in SWT. The message text and error code 
 * provide a further description of the problem. The exception
 * has a <code>throwable</code> field which holds the underlying
 * throwable that caused the problem (if this information is
 * available (i.e. it may be null)).
 * <p>
 * SWTErrors are thrown when something fails internally which
 * either leaves SWT in an unknown state (eg. the o/s call to
 * remove an item from a list returns an error code) or when SWT
 * is left in a known-to-be-unrecoverable state (eg. it runs out
 * of callback resources). SWTErrors should not occur in typical
 * programs, although "high reliability" applications should
 * still catch them.
 * </p><p>
 * This class also provides support methods used by SWT to match
 * error codes to the appropriate exception class (SWTError, 
 * SWTException, or IllegalArgumentException) and to provide
 * human readable strings for SWT error codes.
 * </p>
 *
 * @see SWTException
 * @see SWT#error
 */

public class SWTError extends Error {
	public int code;
	public Throwable throwable;

/**
 * Constructs a new instance of this class with its 
 * walkback filled in. The error code is set to an
 * unspecified value.
 */
public SWTError () {
	this (SWT.ERROR_UNSPECIFIED);
}

/**
 * Constructs a new instance of this class with its 
 * walkback and message filled in. The error code is
 * set to an unspecified value.
 *
 * @param message the detail message for the exception
 */
public SWTError (String message) {
	this (SWT.ERROR_UNSPECIFIED, message);
}

/**
 * Constructs a new instance of this class with its 
 * walkback and error code filled in.
 *
 * @param code the SWT error code
 */
public SWTError (int code) {
	this (code, SWT.findErrorText (code));
}

/**
 * Constructs a new instance of this class with its 
 * walkback, error code and message filled in.
 *
 * @param code the SWT error code
 * @param message the detail message for the exception
 */
public SWTError (int code, String message) {
	super (message);
	this.code = code;
}

/**
 *  Returns the string describing this SWTError object.
 *  <p>
 *  It is combined with the message string of the Throwable
 *  which caused this SWTError (if this information is available).
 *  </p>
 *  @return the error message string of this SWTError object
 */
public String getMessage() {
	if (throwable == null)
		return super.getMessage();
	else
		return super.getMessage() + " (" + throwable.toString() + ")";
}

/**
 * Outputs a printable representation of this error's
 * walkback on the standard error stream.
 * <p>
 * Note: printStackTrace(PrintStream) and printStackTrace(PrintWriter)
 * are not provided in order to maintain compatibility with CLDC.
 * </p>
 */
public synchronized void printStackTrace() {
	super.printStackTrace();
	if (throwable != null) {
		System.err.println("*** Stack trace of contained error ***");
		throwable.printStackTrace();
	}
}

}

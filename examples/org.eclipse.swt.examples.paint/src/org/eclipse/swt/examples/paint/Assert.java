package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/**
 * Provides a means of verifying that assertions are met
 */
public class Assert extends Error {
	/**
	 * Construct an Assert exception
	 */
	private Assert() {
		super("Assertion failed: <no reason given>");
	}
	private Assert(String message) {
		super("Assertion failed: " + message);
	}

	/**
	 * Raise an error
	 */
	public static final void raise() {
		throw new Assert();
	}
	public static final void raise(String message) {
		if (message == null) raise();
		throw new Assert(message);
	}

	/**
	 * Test an assertion
	 */
	public static final void assert(boolean condition) {
		if (! condition) raise();
	}
	public static final void assert(boolean condition, String message) {
		if (! condition) raise(message);
	}		
}

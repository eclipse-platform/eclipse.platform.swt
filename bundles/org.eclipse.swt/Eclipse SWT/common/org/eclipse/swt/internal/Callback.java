package org.eclipse.swt.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;

/**
 * Instances of this class represent entry points into Java
 * which can be invoked from operating system level callback
 * routines.
 * <p>
 * IMPORTANT: A callback is only valid when invoked on the
 * thread which created it. The results are undefined (and
 * typically bad) when a callback is passed out to the 
 * operating system (or other code) in such a way that the
 * callback is called from a different thread.
 */

public class Callback {
	
	Object object;
	String method, signature;
	int argCount, address;
	boolean isStatic, isArrayBased;
	
	/* SWT Version - Mmmm (M=major, mmm=minor) */
	
	/**
	 * SWT Major version number (must be >= 0)
	 */
	public static int MAJOR_VERSION = 2;
	
	/**
	 * SWT Minor version number (must be in the range 0..999)
	 */
	public static int MINOR_VERSION = 2;
	
	/**
	 * SWT revision number (must be >= 0)
	 */
	public static int REVISION = 0;

	/* Load the SWT library */
	static {
		loadLibrary ("swt");
	}

/**
 * Constructs a new instance of this class given an object
 * to send the message to, a string naming the method to
 * invoke and an argument count. Note that, if the object
 * is an instance of <code>Class</code> it is assumed that
 * the method is a static method on that class.
 *
 * @param object the object to send the message to
 * @param method the name of the method to invoke
 * @param argCount the number of arguments that the method takes
 */
public Callback (Object object, String method, int argCount) {
	this (object, method, argCount, false);
}

/**
 * Constructs a new instance of this class given an object
 * to send the message to, a string naming the method to
 * invoke, an argument count and a flag indicating whether
 * or not the arguments will be passed in an array. Note 
 * that, if the object is an instance of <code>Class</code>
 * it is assumed that the method is a static method on that
 * class.
 *
 * @param object the object to send the message to
 * @param method the name of the method to invoke
 * @param argCount the number of arguments that the method takes
 * @param isArrayBased <code>true</code> if the arguments should be passed in an array and false otherwise
 */
public Callback (Object object, String method, int argCount, boolean isArrayBased) {

	/* Set the callback fields */
	this.object = object;
	this.method = method;
	this.argCount = argCount;
	isStatic = object instanceof Class;
	this.isArrayBased = isArrayBased;
	
	/* Inline the common cases */
	if (isArrayBased) {
		signature = "([I)I";
	} else {
		switch (argCount) {
			case 0: signature = "()I"; break;
			case 1: signature = "(I)I"; break;
			case 2: signature = "(II)I"; break;
			case 3: signature = "(III)I"; break;
			case 4: signature = "(IIII)I"; break;
			default:
				signature = "(";
				for (int i=0; i<argCount; i++) signature += "I";
				signature += ")I";
		}
	}
	
	/* Bind the address */
	address = bind (this);
}

/**
 * Allocates the native level resources associated with the
 * callback. This method is only invoked from within the
 * constructor for the argument.
 *
 * @param callback the callback to bind
 */
static native synchronized int bind (Callback callback);

/**
 * Releases the native level resources associated with the callback,
 * and removes all references between the callback and
 * other objects. This helps to prevent (bad) application code
 * from accidentally holding onto extraneous garbage.
 */
public void dispose () {
	if (object == null) return;
	unbind (this);
	object = method = signature = null;
	address = 0;
}

/**
 * Returns the address of a block of machine code which will
 * invoke the callback represented by the receiver.
 *
 * @return the callback address
 */
public int getAddress () {
	return address;
}

/**
 * Returns the SWT version as an integer in the standard format
 * <em>Mmmm</em> where <em>M</em> is the major version number
 * and <em>mmm</em> is the minor version number.
 *
 * @return the version of the currently running SWT
 */
public static int getVersion () {
	return MAJOR_VERSION * 1000 + MINOR_VERSION;
}

/**
 * Returns the SWT platform name.
 *
 * @return the platform name of the currently running SWT
 */
public static native String getPlatform ();

/**
 * Returns the OS name.
 *
 * @return the os name of the currently running SWT
 */
static String getOS () {
	String name = System.getProperty("os.name");
	if (name.regionMatches(true, 0, "win", 0, 3)) return "win32";
	if (name.regionMatches(true, 0, "sun", 0, 3)) return "solaris";
	return name.toLowerCase();
}

/**
 * Returns the SWT revision number as an integer. Revision changes
 * occur as a result of non-API breaking bug fixes.
 *
 * @return the revision number of the currently running SWT
 */
public static int getRevision () {
	return REVISION;
}

/**
 * Indicates whether or not callbacks which are triggered at the
 * native level should cause the messages described by the matching
 * <code>Callback</code> objects to be invoked. This method is used
 * to safely shut down SWT when it is run within environments
 * which can generate spurious events.
 * <p>
 * Note: This should not be called by application code.
 * </p>
 *
 * @param ignore true if callbacks should not be invoked
 */
public static final native synchronized void setEnabled (boolean enable);

/**
 * Returns whether or not callbacks which are triggered at the
 * native level should cause the messages described by the matching
 * <code>Callback</code> objects to be invoked. This method is used
 * to safely shut down SWT when it is run within environments
 * which can generate spurious events.
 * <p>
 * Note: This should not be called by application code.
 * </p>
 *
 * @return true if callbacks should not be invoked
 */
public static final native synchronized boolean getEnabled ();

/**
 * This might be called directly from native code in environments
 * which can generate spurious events. Check before removing it.
 *
 * @deprecated
 *
 * @param ignore true if callbacks should not be invoked
 */
static final void ignoreCallbacks (boolean ignore) {
	setEnabled(!ignore);
} 

/*
 * Loads the SWT shared library that matches the version
 * of the Java code which is currently running. 
 */
public static void loadLibrary () {
	loadLibrary ("swt");
}

/**
 * Loads the shared library that matches the version of the
 * Java code which is currently running.  SWT shared libraries
 * follow an encoding scheme where the major, minor and revision
 * numbers are embedded in the library name and this along with
 * <code>name</code> is used to load the library.  If this fails,
 * <code>name</code> is used in another attempt to load the library,
 * this time ignoring the SWT version encoding scheme.
 *
 * @param name the name of the library to load
 */
public static void loadLibrary (String name) {
	/* Include os name to support same window system on 
	 * different operating systems 
	 */
	String newName = name + "-" + getOS () + "-" + MAJOR_VERSION;

	/* Force 3 digits in minor version number */
	if (MINOR_VERSION < 10) {
		newName += "00";
	} else {
		if (MINOR_VERSION < 100) newName += "0";
	}
	newName += MINOR_VERSION;
	
	/* No "r" until first revision */
	if (REVISION > 0) newName += "r" + REVISION;
	try {
		System.loadLibrary (newName);
	} catch (UnsatisfiedLinkError e) {
		try {
			System.loadLibrary (name);
		} catch (UnsatisfiedLinkError e2) {
			throw e;
		}
	}
}

/**
 * Immediately wipes out all native level state associated
 * with <em>all</em> callbacks.
 * <p>
 * <b>WARNING:</b> This operation is <em>extremely</em> dangerous,
 * and should never be performed by application code.
 * </p>
 */
public static final native synchronized void reset ();

/**
 * Releases the native level resources associated with the callback.
 *
 * @see #dispose
 */
static final native synchronized void unbind (Callback callback);

}

package org.eclipse.swt.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class Library {

	/* SWT Version - Mmmm (M=major, mmm=minor) */
	
	/**
	 * SWT Major version number (must be >= 0)
	 */
	static int MAJOR_VERSION = 2;
	
	/**
	 * SWT Minor version number (must be in the range 0..999)
	 */
	static int MINOR_VERSION = 23;
	
	/**
	 * SWT revision number (must be >= 0)
	 */
	static int REVISION = 0;

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
 * Returns the OS name.
 *
 * @return the os name of the currently running SWT
 */
static String getOS () {
	String name = System.getProperty("os.name");
	if (name == null) return "unknown";
	name = name.toLowerCase ();
	if (name.indexOf ("windows ce") == 0) return "win32-ce";
	if (name.indexOf ("win") == 0) return "win32";
	if (name.indexOf ("sun") == 0) return "solaris";
	return name;
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
	/*
     * Include OS name to support same window system
     * on different operating systems.
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

}

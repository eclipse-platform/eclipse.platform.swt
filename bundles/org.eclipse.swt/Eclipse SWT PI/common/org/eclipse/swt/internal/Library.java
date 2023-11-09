/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.Attributes;

public class Library {

	/* SWT Version - Mmmm (M=major, mmm=minor) */

	/**
	 * SWT Major version number (must be >= 0)
	 */
	static int MAJOR_VERSION = 4;

	/**
	 * SWT Minor version number (must be in the range 0..999)
	 */
	static int MINOR_VERSION = 963;

	/**
	 * SWT revision number (must be >= 0)
	 */
	static int REVISION = 5;

	/**
	 * The JAVA and SWT versions
	 */
	public static final int JAVA_VERSION, SWT_VERSION;
	public static final String USER_HOME;

	static final String SEPARATOR;
	static final String DELIMITER;

	static final String JAVA_LIB_PATH = "java.library.path";
	static final String SWT_LIB_PATH = "swt.library.path";

	static final String SUFFIX_64 = "-64";	//$NON-NLS-1$
	static final String SWT_LIB_DIR;

static {
	DELIMITER = System.lineSeparator(); //$NON-NLS-1$
	SEPARATOR = File.separator;
	USER_HOME = System.getProperty ("user.home");
	SWT_LIB_DIR = ".swt" + SEPARATOR + "lib" + SEPARATOR + os() + SEPARATOR + arch(); //$NON-NLS-1$ $NON-NLS-2$
	JAVA_VERSION = parseVersion(System.getProperty("java.version")); //$NON-NLS-1$
	SWT_VERSION = SWT_VERSION(MAJOR_VERSION, MINOR_VERSION);
}

static String arch() {
	String osArch = System.getProperty("os.arch"); //$NON-NLS-1$
	if (osArch.equals ("amd64")) return "x86_64"; //$NON-NLS-1$ $NON-NLS-2$
	return osArch;
}

static String os() {
	String osName = System.getProperty("os.name"); //$NON-NLS-1$
	if (osName.equals ("Linux")) return "linux"; //$NON-NLS-1$ $NON-NLS-2$
	if (osName.equals ("Mac OS X")) return "macosx"; //$NON-NLS-1$ $NON-NLS-2$
	if (osName.startsWith ("Win")) return "win32"; //$NON-NLS-1$ $NON-NLS-2$
	return osName;
}

static void chmod(String permision, String path) {
	if (os().equals ("win32")) return; //$NON-NLS-1$
	try {
		Runtime.getRuntime ().exec (new String []{"chmod", permision, path}).waitFor(); //$NON-NLS-1$
	} catch (Throwable e) {
		try {
			new File(path).setExecutable(true);
		} catch (Throwable e1) {
		}
	}
}

/* Use method instead of in-lined constants to avoid compiler warnings */
static long longConst() {
	return 0x1FFFFFFFFL;
}

static int parseVersion(String version) {
	if (version == null) return 0;
	int major = 0, minor = 0, micro = 0;
	int length = version.length(), index = 0, start = 0;
	while (index < length && Character.isDigit(version.charAt(index))) index++;
	try {
		if (start < length) major = Integer.parseInt(version.substring(start, index));
	} catch (NumberFormatException e) {}
	start = ++index;
	while (index < length && Character.isDigit(version.charAt(index))) index++;
	try {
		if (start < length) minor = Integer.parseInt(version.substring(start, index));
	} catch (NumberFormatException e) {}
	start = ++index;
	while (index < length && Character.isDigit(version.charAt(index))) index++;
	try {
		if (start < length) micro = Integer.parseInt(version.substring(start, index));
	} catch (NumberFormatException e) {}
	return JAVA_VERSION(major, minor, micro);
}

/**
 * Returns the Java version number as an integer.
 *
 * @return the version
 */
public static int JAVA_VERSION (int major, int minor, int micro) {
	return (major << 16) + (minor << 8) + micro;
}

/**
 * Returns the SWT version number as an integer.
 *
 * @return the version
 */
public static int SWT_VERSION (int major, int minor) {
	return major * 1000 + minor;
}

private static boolean extractResource(String resourceName, File outFile) {
	try (InputStream inputStream = Library.class.getResourceAsStream (resourceName)) {
		if (inputStream == null) return false;
		Files.copy(inputStream, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	} catch (Throwable e) {
		return false;
	}

	return true;
}

/**
 * Extract file with 'mappedName' into path 'extractToFilePath'.
 * Does not overwrite existing file.
 * Does not leave trash on error.
 * @param extractToFilePath full path of where the file is to be extacted to, inc name of file,
 *                          e.g /home/USER/.swt/lib/linux/x86_64/libswt-MYLIB-gtk-4826.so
 * @param mappedName file to be searched in jar.
 * @return	true upon success, failure if something went wrong.
 */
static boolean extract (String extractToFilePath, String mappedName) {
	File file = new File(extractToFilePath);
	if (file.exists ()) return true;

	// Write to temp file first, so that other processes don't see
	// partially written library on disk
	File tempFile;
	try {
		tempFile = File.createTempFile (file.getName(), ".tmp", file.getParentFile()); //$NON-NLS-1$
	} catch (Throwable e) {
		return false;
				}

	// Extract resource
	String resourceName = "/" + mappedName.replace('\\', '/'); //$NON-NLS-1$
	if (!extractResource (resourceName, tempFile)) {
		tempFile.delete();
		return false;
			}

	// Make it executable
	chmod ("755", tempFile.getPath()); //$NON-NLS-1$

	// "Publish" file now that it's ready to use.
	// If there is a file already, then someone published while we were
	// extracting, just delete our file and consider it a success.
	try {
		Files.move (tempFile.toPath(), file.toPath());
	} catch (Throwable e) {
		tempFile.delete();
	}

	return true;
}

static boolean isLoadable () {
	URL url = Platform.class.getClassLoader ().getResource ("org/eclipse/swt/internal/Library.class"); //$NON-NLS-1$
	if (!url.getProtocol ().equals ("jar")) { //$NON-NLS-1$
		/* SWT is presumably running in a development environment */
		return true;
	}

	Attributes attributes = null;
	try {
		URLConnection connection = url.openConnection();
		if (!(connection instanceof JarURLConnection jc)) {
			/* should never happen for a "jar:" url */
			return false;
		}
		attributes = jc.getMainAttributes();
	} catch (IOException e) {
		/* should never happen for a valid SWT jar with the expected manifest values */
		return false;
	}

	String os = os ();
	String arch = arch ();
	String manifestOS = attributes.getValue ("SWT-OS"); //$NON-NLS-1$
	String manifestArch = attributes.getValue ("SWT-Arch"); //$NON-NLS-1$
	if (arch.equals (manifestArch) && os.equals (manifestOS)) {
		return true;
	}

	return false;
}

static boolean load (String libName, StringBuilder message) {
	try {
		if (libName.contains (SEPARATOR)) {
			System.load (libName);
		} else {
			System.loadLibrary (libName);
		}
		return true;
	} catch (UnsatisfiedLinkError e) {
		if (message.length() == 0) message.append(DELIMITER);
		message.append('\t');
		message.append(e.getMessage());
		message.append(DELIMITER);
	}
	return false;
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
	loadLibrary (name, true);
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
 * @param mapName true if the name should be mapped, false otherwise
 */
public static void loadLibrary (String name, boolean mapName) {
	String prop = System.getProperty ("sun.arch.data.model"); //$NON-NLS-1$
	if (prop == null) prop = System.getProperty ("com.ibm.vm.bitmode"); //$NON-NLS-1$
	if (prop != null) {
		if ("32".equals (prop)) { //$NON-NLS-1$
			throw new UnsatisfiedLinkError ("Cannot load 64-bit SWT libraries on 32-bit JVM"); //$NON-NLS-1$
		}
	}

	/* Compute the library name and mapped name */
	final int candidates = 3;
	String[] libNames = new String[candidates], mappedNames = new String[candidates];
	if (mapName) {
		String version = getVersionString ();
		libNames[0] = name + "-" + Platform.PLATFORM + "-" + version;  //$NON-NLS-1$ //$NON-NLS-2$
		libNames[1] = name + "-" + Platform.PLATFORM;  //$NON-NLS-1$
		libNames[2] = name;
		for (int i = 0; i < candidates; i++) {
			mappedNames[i] = mapLibraryName (libNames[i]);
		}
	} else {
		for (int i = 0; i < candidates; i++) {
			libNames[i] = mappedNames[i] = name;
		}
	}

	StringBuilder message = new StringBuilder();

	/* Try loading library from swt library path */
	String path = System.getProperty (SWT_LIB_PATH); //$NON-NLS-1$
	if (path != null) {
		path = new File (path).getAbsolutePath ();
		for (int i = 0; i < candidates; i++) {
			if ((i == 0 || mapName) && load (path + SEPARATOR + mappedNames[i], message)) return;
		}
	}

	/* Try loading library from java library path */
	for (int i = 0; i < candidates; i++) {
		if ((i == 0 || mapName) && load (libNames[i], message)) return;
	}

	/* Try loading library from the tmp directory if swt library path is not specified.
	 * Create the tmp folder if it doesn't exist. Tmp folder looks like this:
	 * ~/.swt/lib/<platform>/<arch>/
	 */
	String[] fileNames = new String[candidates];
	for (int i = 0; i < candidates; i++) {
		fileNames[i] = mappedNames[i];
	}
	if (path == null) {
		path = USER_HOME;
		File dir = new File (path, SWT_LIB_DIR);
		if ((dir.exists () && dir.isDirectory ()) || dir.mkdirs ()) { // Create if not exist.
			path = dir.getAbsolutePath ();
		} else {
			/* fall back to using the home dir directory */
			for (int i = 0; i < candidates; i++) {
				fileNames[i] = mapLibraryName (libNames[i] + SUFFIX_64);
			}
		}
		for (int i = 0; i < candidates; i++) {
			if ((i == 0 || mapName) && load (path + SEPARATOR + fileNames[i], message)) return;
		}
	}

	/* Try extracting and loading library from jar. */
	if (path != null) {
		for (int i = 0; i < candidates; i++) {
			if ((i == 0 || mapName) && extract (path + SEPARATOR + fileNames[i], mappedNames[i])) {
				if (load(path + SEPARATOR + fileNames[i], message)) return;
			}
		}
	}

	/* Failed to find the library */
	throw new UnsatisfiedLinkError ("Could not load SWT library. Reasons: " + message.toString()); //$NON-NLS-1$
}

static String mapLibraryName (String libName) {
	return mapLibraryName(libName, true);
}

static String mapLibraryName (String libName, boolean replaceDylib) {
	/* SWT libraries in the Macintosh use the extension .jnilib but the some VMs map to .dylib. */
	libName = System.mapLibraryName (libName);
	String ext = ".dylib"; //$NON-NLS-1$
	if (libName.endsWith(ext) && replaceDylib) {
		libName = libName.substring(0, libName.length() - ext.length()) + ".jnilib"; //$NON-NLS-1$
	}
	return libName;
}

/**
 * @return String Combined SWT version like 4826
 */
public static String getVersionString () {
	String version = System.getProperty ("swt.version"); //$NON-NLS-1$
	if (version == null) {
		version = "" + MAJOR_VERSION; //$NON-NLS-1$
		/* Force 3 digits in minor version number */
		if (MINOR_VERSION < 10) {
			version += "00"; //$NON-NLS-1$
		} else {
			if (MINOR_VERSION < 100) version += "0"; //$NON-NLS-1$
		}
		version += MINOR_VERSION;
		/* No "r" until first revision */
		if (REVISION > 0) version += "r" + REVISION; //$NON-NLS-1$
	}
	return version;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.program;

import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent programs and
 * their associated file extensions in the operating
 * system.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#program">Program snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Program {
	String name;
	String command;
	String iconName;
	static final String [] ARGUMENTS = new String [] {"%1", "%l", "%L"};

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program () {
}

static int createDotNetString (String string) {
	if (string == null) return 0;
	int length = string.length ();
	char [] buffer = new char [length + 1];
	string.getChars (0, length, buffer, 0);
	return OS.gcnew_String (buffer);
}

static String createJavaString (int ptr) {
	int charArray = OS.String_ToCharArray (ptr);
	char[] chars = new char[OS.String_Length (ptr)];
	OS.memcpy (chars, charArray, chars.length * 2);
	OS.GCHandle_Free (charArray);
	return new String (chars);
}

/**
 * Finds the program that is associated with an extension.
 * The extension may or may not begin with a '.'.  Note that
 * a <code>Display</code> must already exist to guarantee that
 * this method returns an appropriate result.
 *
 * @param extension the program extension
 * @return the program or <code>null</code>
 *
 * @exception IllegalArgumentException <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static Program findProgram (String extension) {
	if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (extension.length () == 0) return null;
	if (extension.charAt (0) != '.') extension = "." + extension; //$NON-NLS-1$
	if (extension.length() > 0xff) return null;
	int key = createDotNetString (extension);
	int classesRoot = OS.Registry_ClassesRoot ();
	int registryKey = OS.RegistryKey_OpenSubKey (classesRoot, key);
	OS.GCHandle_Free (key);
	OS.GCHandle_Free (classesRoot);
	if (registryKey == 0) return null;
	Program program = null;
	int value = OS.RegistryKey_GetValue (registryKey, 0);
	if (value != 0) {
		program = getProgram (value);
		OS.GCHandle_Free (value);
	}
	OS.GCHandle_Free (registryKey);
	return program;
}

/**
 * Answer all program extensions in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	String [] extensions = new String [1024];
	int classesRoot = OS.Registry_ClassesRoot ();
	int subKeys = OS.RegistryKey_GetSubKeyNames (classesRoot);
	OS.GCHandle_Free (classesRoot);
	int count = 0, length = OS.ICollection_Count (subKeys);
	for (int i=0; i<length; i++) {
		int key = OS.IList_default (subKeys, i);
		String extension = createJavaString (key);
		OS.GCHandle_Free (key);
		if (extension.length () > 0 && extension.charAt (0) == '.') {
			if (count == extensions.length) {
				String [] newExtensions = new String [extensions.length + 1024];
				System.arraycopy (extensions, 0, newExtensions, 0, extensions.length);
				extensions = newExtensions;
			}
			extensions [count++] = extension;
		}

	}
	OS.GCHandle_Free (subKeys);
	if (count != extensions.length) {
		String [] newExtension = new String [count];
		System.arraycopy (extensions, 0, newExtension, 0, count);
		extensions = newExtension;
	}
	return extensions;
}

static int getKeyValue (int key, boolean expand) {
	int value = OS.RegistryKey_GetValue (key, 0);
	if (value != 0) {
		if (expand) {
			int expandedValue = OS.Environment_ExpandEnvironmentVariables (value);
			OS.GCHandle_Free (value);
			value = expandedValue;
		}	
	}
	return value;
}

static Program getProgram (int key) {
	int classesRoot = OS.Registry_ClassesRoot ();
	int registryKey = OS.RegistryKey_OpenSubKey (classesRoot, key);
	OS.GCHandle_Free (classesRoot);
	/* Name */
	int name = getKeyValue (registryKey, false);
	String programName = createJavaString (name == 0 ? key : name);
	OS.GCHandle_Free (name);
	/* Command */
	int shellCommand = createDotNetString ("shell");
	int shellKey = OS.RegistryKey_OpenSubKey (registryKey, shellCommand);
	OS.GCHandle_Free (shellCommand);
	int command = 0;
	if (shellKey != 0) {
		command = getKeyValue (shellKey, true);
		if (command == 0) {
			int openCommand = createDotNetString ("open");
			int openKey = OS.RegistryKey_OpenSubKey (shellKey, openCommand);
			if (openKey != 0) {
				int commandCommand = createDotNetString ("command");
				int commandKey = OS.RegistryKey_OpenSubKey (openKey, commandCommand);
				if (commandKey != 0) {
					command = getKeyValue (commandKey, true);
					OS.GCHandle_Free (commandKey);			
				}
				OS.GCHandle_Free (commandCommand);
				OS.GCHandle_Free (openKey);
			}
			OS.GCHandle_Free (openCommand);
		}
	}
	OS.GCHandle_Free (shellKey);
	int iconName = 0;
	if (command != 0) {
		int defaultIconCommand = createDotNetString ("DefaultIcon");
		int defaultIconKey = OS.RegistryKey_OpenSubKey (registryKey, defaultIconCommand);
		if (defaultIconKey != 0) {
			iconName = getKeyValue (defaultIconKey, true);
			OS.GCHandle_Free (defaultIconKey);
		}
		OS.GCHandle_Free (defaultIconCommand);
	}
	OS.GCHandle_Free (registryKey);
	if (command == 0) return null;
	Program program = new Program ();
	program.name = programName;
	program.command = createJavaString (command);
	OS.GCHandle_Free (command);
	program.iconName = iconName != 0 ? createJavaString (iconName) : "";
	OS.GCHandle_Free (iconName);
	return program;
}

/**
 * Answers all available programs in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	Program [] programs = new Program [1024];
	int classesRoot = OS.Registry_ClassesRoot ();
	int subKeyNames = OS.RegistryKey_GetSubKeyNames (classesRoot);
	OS.GCHandle_Free (classesRoot);
	int count = 0;
	int length = OS.ICollection_Count (subKeyNames);
	for (int i = 0; i < length; i++) {
		int keyName = OS.IList_default (subKeyNames, i);
		Program program = getProgram (keyName);
		OS.GCHandle_Free (keyName);
		if (program != null) {
			if (count == programs.length) {
				Program [] newPrograms = new Program [programs.length + 1024];
				System.arraycopy (programs, 0, newPrograms, 0, programs.length);
				programs = newPrograms;
			}
			programs [count++] = program;
		}
	}
	OS.GCHandle_Free (subKeyNames);
	if (count != programs.length) {
		Program [] newPrograms = new Program [count];
		System.arraycopy (programs, 0, newPrograms, 0, count);
		programs = newPrograms;
	}
	return programs;
}

/**
 * Launches the operating system executable associated with the file or
 * URL (http:// or https://).  If the file is an executable then the
 * executable is launched.  Note that a <code>Display</code> must already
 * exist to guarantee that this method returns an appropriate result.
 *
 * @param fileName the file or program name or URL (http:// or https://)
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public static boolean launch (String fileName) {
    return launch(fileName, null);
}

/**
 * Launches the operating system executable associated with the file or
 * URL (http:// or https://).  If the file is an executable then the
 * executable is launched.  If a valid working directory is specified 
 * it is used as the working directory for the launched program.
 * Note that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @param fileName the file or program name or URL (http:// or https://)
 * @param workingDir the name of the working directory or null
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 *    <li>ERROR_INVALID_ARGUMENT when workingDir is not valid</li>
 * </ul>
 * 
 * @since 3.6
 */
public static boolean launch (String fileName, String workingDir) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);	
	int hHeap = Win32.GetProcessHeap ();
	int length = fileName.length ();
	char [] buffer = new char [length + 1];
	fileName.getChars (0, length, buffer, 0);
	int byteCount = buffer.length  * 2;
	int lpFile = Win32.HeapAlloc (hHeap, Win32.HEAP_ZERO_MEMORY, byteCount);
	Win32.MoveMemory (lpFile, buffer, byteCount);
	SHELLEXECUTEINFOW info = new SHELLEXECUTEINFOW ();
	info.cbSize = SHELLEXECUTEINFOW.sizeof;
	info.lpFile = lpFile;
	info.nShow = Win32.SW_SHOW;
	boolean result = Win32.ShellExecuteExW (info);
	if (lpFile != 0) Win32.HeapFree (hHeap, 0, lpFile);
	return result;
}

/**
 * Executes the program with the file as the single argument
 * in the operating system.  It is the responsibility of the
 * programmer to ensure that the file contains valid data for 
 * this program.
 *
 * @param fileName the file or program name
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public boolean execute (String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int index = 0;
	boolean append = true;
	String prefix = command, suffix = ""; //$NON-NLS-1$
	while (index < ARGUMENTS.length) {
		int i = command.indexOf (ARGUMENTS [index]);
		if (i != -1) {
			append = false;
			prefix = command.substring (0, i);
			suffix = command.substring (i + ARGUMENTS [index].length (), command.length ());
			break;
		}
		index++;
	}
	if (append) fileName = " \"" + fileName + "\"";
	String commandLine = prefix + fileName + suffix;
	int length = commandLine.length ();
	char [] buffer = new char [length + 1];
	commandLine.getChars (0, length, buffer, 0);
	STARTUPINFOW lpStartupInfo = new STARTUPINFOW ();
	lpStartupInfo.cb = STARTUPINFOW.sizeof;
	PROCESS_INFORMATION lpProcessInformation = new PROCESS_INFORMATION ();
	boolean success = Win32.CreateProcessW (0, buffer, 0, 0, false, 0, 0, 0, lpStartupInfo, lpProcessInformation);
	if (lpProcessInformation.hProcess != 0) Win32.CloseHandle (lpProcessInformation.hProcess);
	if (lpProcessInformation.hThread != 0) Win32.CloseHandle (lpProcessInformation.hThread);
	return success;
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the receiver in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData () {
	int nIconIndex = 0;
	String fileName = iconName;
	int index = iconName.indexOf (',');
	if (index != -1) {
		fileName = iconName.substring (0, index);
		String iconIndex = iconName.substring (index + 1, iconName.length ()).trim ();
		try {
			nIconIndex = Integer.parseInt (iconIndex);
		} catch (NumberFormatException e) {}
	}
	int length = fileName.length ();
	char [] buffer = new char [length + 1];
	fileName.getChars (0, length, buffer, 0);
	int [] phiconSmall = new int [1], phiconLarge = null;
	Win32.ExtractIconExW (buffer, nIconIndex, phiconLarge, phiconSmall, 1);
	if (phiconSmall [0] == 0) return null;
	int empty = OS.Int32Rect_Empty ();
	int source = OS.Imaging_CreateBitmapSourceFromHIcon (phiconSmall [0], empty, 0);
	Image image = Image.wpf_new (null, SWT.ICON, source);
	OS.GCHandle_Free (empty);
	ImageData imageData = image.getImageData ();
	image.dispose ();
	return imageData;
}

/**
 * Returns the receiver's name.  This is as short and
 * descriptive a name as possible for the program.  If
 * the program has no descriptive name, this string may
 * be the executable name, path or empty.
 *
 * @return the name of the program
 */
public String getName () {
	return name;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param other the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals (Object other) {
	if (this == other) return true;
	if (other instanceof Program) {
		final Program program = (Program) other;
		return name.equals (program.name) && command.equals (program.command)
			&& iconName.equals (program.iconName);
	}
	return false;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
public int hashCode () {
	return name.hashCode () ^ command.hashCode () ^ iconName.hashCode ();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
public String toString () {
	return "Program {" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
}

}

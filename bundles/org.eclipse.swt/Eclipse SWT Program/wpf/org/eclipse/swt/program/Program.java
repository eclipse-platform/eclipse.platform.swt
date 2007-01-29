/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.program;

 
import java.io.IOException;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent programs and
 * their associated file extensions in the operating
 * system.
 */
public final class Program {
	String name;
	String command;
	String iconName;

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
	OS.memmove (chars, charArray, chars.length * 2);
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
	int key = createDotNetString (extension);
	int classesRoot = OS.Registry_ClassesRoot ();
	int registryKey = OS.RegistryKey_OpenSubKey (classesRoot, key);
	OS.GCHandle_Free (key);
	OS.GCHandle_Free (classesRoot);
	if (registryKey == 0) return null;
	Program program = null;
	int value = OS.RegistryKey_GetValue (registryKey, 0);
	if (value != 0) {
		String string = createJavaString (value);
		program = getProgram (string);
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
	//TODO
	return new String[0];
}

static String getKeyValue (String string, boolean expand) {
	int classesRoot = OS.Registry_ClassesRoot ();
	int key = createDotNetString (string);
	int registryKey = OS.RegistryKey_OpenSubKey (classesRoot, key);
	OS.GCHandle_Free (key);
	OS.GCHandle_Free (classesRoot);
	if (registryKey == 0) return null;
	String result = null;
	int value = OS.RegistryKey_GetValue (registryKey, 0);
	if (value != 0) {
		if (expand) {
			int expandedValue = OS.Environment_ExpandEnvironmentVariables (value);
			OS.GCHandle_Free (value);
			value = expandedValue;
		}
		result = createJavaString (value);
		OS.GCHandle_Free (value);		
	}
	OS.GCHandle_Free (registryKey);
	return result;
}

static Program getProgram (String key) {
	/* Name */
	String name = getKeyValue (key, false);
	if (name == null || name.length () == 0) {
		name = key;
	}

	/* Command */
	String DEFAULT_COMMAND = "\\shell"; //$NON-NLS-1$
	String defaultCommand = getKeyValue (key + DEFAULT_COMMAND, true);
	if (defaultCommand == null || defaultCommand.length() == 0) defaultCommand = "open"; //$NON-NLS-1$
	String COMMAND = "\\shell\\" + defaultCommand + "\\command"; //$NON-NLS-1$
	String command = getKeyValue (key + COMMAND, true);
	if (command == null || command.length () == 0) return null;

	/* Icon */
	String DEFAULT_ICON = "\\DefaultIcon"; //$NON-NLS-1$
	String iconName = getKeyValue (key + DEFAULT_ICON, true);
	if (iconName == null) iconName = ""; //$NON-NLS-1$

	Program program = new Program ();
	program.name = name;
	program.command = command;
	program.iconName = iconName;
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
	//TODO compare results with win32.
	Program [] programs = new Program [1024];
	int classesRoot = OS.Registry_ClassesRoot ();
	int names = OS.RegistryKey_GetSubKeyNames (classesRoot);
	OS.GCHandle_Free (classesRoot);
	int length = OS.ICollection_Count (names);
	int count = 0;
	for (int i = 0; i < length; i++) {
		int name = OS.IList_default (names, i);
		String string = createJavaString (name);
		OS.GCHandle_Free (name);
		Program program = getProgram (string);
		if (program != null) {
			if (count == programs.length) {
				Program [] newPrograms = new Program [programs.length + 1024];
				System.arraycopy (programs, 0, newPrograms, 0, programs.length);
				programs = newPrograms;
			}
			programs [count++] = program;
		}
	}
	OS.GCHandle_Free (names);
	if (count != programs.length) {
		Program [] newPrograms = new Program [count];
		System.arraycopy (programs, 0, newPrograms, 0, count);
		programs = newPrograms;
	}
	return programs;
}

/**
 * Launches the executable associated with the file in
 * the operating system.  If the file is an executable,
 * then the executable is launched.  Note that a <code>Display</code>
 * must already exist to guarantee that this method returns
 * an appropriate result.
 *
 * @param fileName the file or program name
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public static boolean launch (String fileName) {
	//TODO
	return false;
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
	boolean quote = true;
	String prefix = command, suffix = ""; //$NON-NLS-1$
	int index = command.indexOf ("%1"); //$NON-NLS-1$
	if (index != -1) {
		int count=0;
		int i=index + 2, length = command.length ();
		while (i < length) {
			if (command.charAt (i) == '"') count++;
			i++;
		}
		quote = count % 2 == 0;
		prefix = command.substring (0, index);
		suffix = command.substring (index + 2, length);
	}
	if (quote) fileName = " \"" + fileName + "\""; //$NON-NLS-1$ //$NON-NLS-2$
	try {
		Compatibility.exec(prefix + fileName + suffix);
	} catch (IOException e) {
		return false;
	}
	return true;
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the receiver in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData () {
	//TODO
	return null;
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
public boolean equals(Object other) {
	if (this == other) return true;
	if (other instanceof Program) {
		final Program program = (Program) other;
		return name.equals(program.name) && command.equals(program.command)
			&& iconName.equals(program.iconName);
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
public int hashCode() {
	return name.hashCode() ^ command.hashCode() ^ iconName.hashCode();
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

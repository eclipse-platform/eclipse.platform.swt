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

 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import java.io.*;

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
	String extension;
	String command;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program () {
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
	String[][] table = loadAssociationTable ();
	if (table == null) return null;
	for (int i = 0; i < table.length; i++) {
		String[] entry = table [i];
		if (entry == null) break;
		String [] exts = expandExtensions (entry [0]);
		for (int j = 0; j < exts.length; j++) {
			String ext = exts[j];
			if (ext == null) break;
			if (ext.endsWith (extension)) {
				Program program = new Program ();
				program.extension = ext;
				program.command = entry [1];
				int index;
				String name = entry [1];
				if ((index = name.indexOf(' ')) != -1) {
					name = name.substring (0, index);
				}
				program.name = name;
				return program;
			}
		}
	}
	return null;
}

/**
 * Answer all program extensions in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	String[][] table = loadAssociationTable ();
	if (table == null) return new String [0];
	int count = 0;
	String[] extensions = new String [50];
	for (int i = 0; i < table.length; i++) {
		String[] entry = table [i];
		if (entry == null) break;
		String [] exts = expandExtensions (entry [0]);
		for (int j = 0; j < exts.length; j++) {
			String ext = exts[j];
			if (ext == null) break;
			if (count == extensions.length) {
				String [] newExtensions = new String [count + 50];
				System.arraycopy (extensions, 0, newExtensions, 0, count);
				extensions = newExtensions;
			}
			extensions[count++] = ext;
		}
	}
	if (count != extensions.length) {
		String [] newExtensions = new String [count];
		System.arraycopy (extensions, 0, newExtensions, 0, count);
		extensions = newExtensions;
	}
	return extensions;
}

/**
 * Answers all available programs in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	String[][] table = loadAssociationTable ();
	if (table == null) return new Program [0];
	int count = 0;
	Program [] programs = new Program [50];
	for (int i = 0; i < table.length; i++) {
		String [] entry = table [i];
		if (entry == null) break;
		String [] extensions = expandExtensions (entry [0]);
		for (int j = 0; j < extensions.length; j++) {
			String extension = extensions[j];
			if (extension == null) break;
			Program program = new Program ();
			program.extension = extension;
			program.command = entry [1];
			int index;
			String name = entry [1];
			if ((index = name.indexOf(' ')) != -1) {
				name = name.substring (0, index);
			}
			program.name = name;
			if (count 	== programs.length) {
				Program [] newPrograms = new Program [count + 50];
				System.arraycopy (programs, 0, newPrograms, 0, count);
				programs = newPrograms;
			}
			programs [count++] = program;
		}
	}
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
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int index = fileName.lastIndexOf ('.');
	if (index == -1) return false;
	String extension = fileName.substring (index, fileName.length ());
	Program program = Program.findProgram (extension);
	if (program != null && program.execute (fileName)) return true;
	try {
		Compatibility.exec(fileName);
		return true;
	} catch (IOException e) {
		return false;
	}
}

static String []  expandExtensions (String ext) {
	int start = 0, index = 0, count = 0, length = ext.length ();
	String[] extensions = new String [5];
	while (index < length) {
			index = ext.indexOf ('|', start);
			if (index == -1) index = length;
			String extension = ext.substring (start, index).trim();
			start = index + 1;
			int bracketStart = extension.indexOf('[');
			if (bracketStart != -1) {
				int bracketEnd = extension.indexOf(']', bracketStart);
				if (bracketEnd != -1) {
					String prefix = extension.substring (0, bracketStart);
					String suffix = extension.substring (bracketEnd + 1, extension.length ());
					String chars = extension.substring (bracketStart + 1, bracketEnd);
					for (int i=0; i<chars.length (); i++) {
						if (count == extensions.length) {
							String [] newExtensions = new String [count + 5];
							System.arraycopy (extensions, 0, newExtensions, 0, count);
							extensions = newExtensions;
						}
						extensions [count++] = prefix + chars.charAt (i) + suffix;
					}
				}
			} else {
				if (count == extensions.length) {
					String [] newExtensions = new String [count + 5];
					System.arraycopy (extensions, 0, newExtensions, 0, count);
					extensions = newExtensions;
				}
				extensions [count++] = extension;
			}
	}
	return extensions;
}

static String [][] loadAssociationTable () {
	FileInputStream is = null;
	try {
		byte[] buffer = Converter.wcsToMbcs (null, "HOME", true);
		int ptr = OS.getenv (buffer);
		if (ptr == 0) return null;
		int length = OS.strlen (ptr);
		if (length == 0) return null;
		buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		String home = new String (Converter.mbcsToWcs (null, buffer));
		is = new FileInputStream (home + "/.ph/pfm/associate.003");
		BufferedReader reader = new BufferedReader (new InputStreamReader (is));
		String line;
		int count = 0;
		String [][] table = new String [50][];
		while ((line = reader.readLine ()) != null) {
			if (line.trim().startsWith ("#")) continue;
			int start = 0, tabIndex = line.indexOf ('\t', start);
			if (tabIndex == -1) continue;
			String extension = line.substring (start, tabIndex);
			start = tabIndex + 1;
			tabIndex = line.indexOf ('\t', start);
			if (tabIndex == -1) continue;
			String command = line.substring (start, tabIndex);
			if (count == table.length) {
				String [][] newTable = new String [table.length + 50][];
				System.arraycopy (table, 0, newTable, 0, table.length);
				table = newTable;
			}
			String[] entry = new String [] {extension, command};
			table [count++] = entry;
		}
		return table;
	} catch (IOException e) {
	} finally {
		try {
			if (is != null) is.close();
		} catch (IOException e) {}
	}
	return null;
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
	int index = -1;
	String prefix = command, suffix = "", location = "";
	String[] locations = {"file://$PWD/@", "$PWD/@", "@"};
	for (int i = 0; i < locations.length; i++) {
		location = locations[i];
		index = command.indexOf (location);
		if (index != -1) break;
	}
	if (index != -1) {
		int start = 0;
		prefix = command.substring (start, index);
		start = index + location.length() + 1;
		if (start < command.length ()) {
			suffix = command.substring (start, command.length ());
		}
	}
	try {
		Compatibility.exec(prefix + " "  + fileName + " " +  suffix);
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
		return extension.equals(program.extension) && name.equals(program.name) &&
			command.equals(program.command);
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
	return extension.hashCode() ^ name.hashCode() ^ command.hashCode();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
public String toString () {
	return "Program {" + name + "}";
}

}

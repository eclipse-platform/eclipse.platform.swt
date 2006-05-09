/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import java.io.IOException;
import java.util.Vector;

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
	if (extension.charAt (0) != '.') extension = '.' + extension;
	return getProgram(extension);
}

/**
 * Answer all program extensions in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	return new String [] {
		".xml",
		".html",
		".java",
		".properties",
		".jar",
		".zip",
		".txt",
		".jpeg",
		".jpg",
		".tiff",
		".gif",
		".png",
	};
}

static Program getProgram (String key) {
	String[] extentions = getExtensions();
	int i = 0;
	while (i < extentions.length) {
		String ext = extentions[i];
		if (ext.equals(key)) break;
		i++;
	}
	if (i == extentions.length) return null;

	/* Name */
	String name = key;
	if (name == null || name.length () == 0) return null;

	/* Command */
	String command = "/usr/bin/open %f";

	/* Icon */
	String iconName = "icon";

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
	return new Program [] {
		getProgram(".html"),
	};
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
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	String[] args= new String[] {"/usr/bin/open", fileName};
	try {
		Compatibility.exec(args);
		return true;
	} catch(IOException ex) {}	
	return false;
}

static String[] parseCommand(String cmd) {
	Vector args = new Vector();
	int sIndex = 0;
	int eIndex;
	while (sIndex < cmd.length()) {
		/* Trim initial white space of argument. */
		while (sIndex < cmd.length() && Compatibility.isWhitespace(cmd.charAt(sIndex))) {
			sIndex++;
		}
		if (sIndex < cmd.length()) {
			/* If the command is a quoted string */
			if (cmd.charAt(sIndex) == '"' || cmd.charAt(sIndex) == '\'') {
				/* Find the terminating quote (or end of line).
				 * This code currently does not handle escaped characters (e.g., " a\"b").
				 */
				eIndex = sIndex + 1;
				while (eIndex < cmd.length() && cmd.charAt(eIndex) != cmd.charAt(sIndex)) eIndex++;
				if (eIndex >= cmd.length()) { 
					/* The terminating quote was not found
					 * Add the argument as is with only one initial quote.
					 */
					args.addElement(cmd.substring(sIndex, eIndex));
				}
				else {
					/* Add the argument, trimming off the quotes. */
					args.addElement(cmd.substring(sIndex+1, eIndex));
				}
				sIndex = eIndex + 1;
			}			
			else {
				/* Use white space for the delimiters. */
				eIndex = sIndex;
				while (eIndex < cmd.length() && !Compatibility.isWhitespace(cmd.charAt(eIndex))) eIndex++;
				args.addElement(cmd.substring(sIndex, eIndex));
				sIndex = eIndex + 1;
			}
		}
	}	
	String[] result = new String[args.size()];
	args.copyInto(result);
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
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	/* Parse the command into its individual arguments. */
	String[] args = parseCommand(command);
	int fileArg = -1;
	int index;
	for (index = 0; index < args.length; index++) {
		int j = args[index].indexOf("%f");
		if (j != -1) {
			String value = args[index];
			fileArg = index;
			args[index] = value.substring(0, j) + fileName + value.substring(j + 2);
		}
	}

	/* If a file name was given but the command did not have "%f" */
	if ((fileName.length() > 0) && (fileArg < 0)) {
		String[] newArgs = new String[args.length + 1];
		for (index = 0; index < args.length; index++) newArgs[index] = args[index];
		newArgs[args.length] = fileName;
		args = newArgs;
	}

	/* Execute the command. */
	try {
		Compatibility.exec(args);
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
	RGB[] rgbs = new RGB[] {
		new RGB(0xff, 0xff, 0xff), 
		new RGB(0x5f, 0x5f, 0x5f),
		new RGB(0x80, 0x80, 0x80),
		new RGB(0xC0, 0xC0, 0xC0),
		new RGB(0xDF, 0xDF, 0xBF),
		new RGB(0xFF, 0xDF, 0x9F),
		new RGB(0x00, 0x00, 0x00),
	};  
	ImageData data = new ImageData(16, 16, 4, new PaletteData(rgbs)	);
	data.transparentPixel = 6; // use black for transparency
	String[] p= {
		"CCCCCCCCGGG",
		"CFAAAAACBGG",
		"CAAAAAACFBG",
		"CAAAAAACBBB",
		"CAAAAAAAAEB",
		"CAAAAAAAAEB",
		"CAAAAAAAAEB",
		"CAAAAAAAAEB",
		"CAAAAAAAAEB",
		"CAAAAAAAAEB",
		"CAAAAAAAAEB",
		"CAAAAAAAAEB",
		"CDDDDDDDDDB",
		"CBBBBBBBBBB",
	};
	for (int y= 0; y < p.length; y++) {
		for (int x= 0; x < 11; x++) {
			data.setPixel(x+3, y+1, p[y].charAt(x)-'A');
		}
	}		
	return data;
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
	return "Program {" + name + "}";
}

}

package org.eclipse.swt.program;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import org.eclipse.swt.widgets.Display;

import java.util.Vector;
import java.io.*;

/**
 * Instances of this class represent programs and
 * their assoicated file extensions in the operating
 * system.
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
 * The extension may or may not begin with a '.'.
 *
 * @param extension the program extension
 * @return the program or nil
 *
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static Program findProgram (String extension) {
	if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (extension.length () == 0) return null;
	return null;
}

/**
 * Answer all program extensions in the operating system.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	return new String[0];
}

/**
 * Answers all available programs in the operating system.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	Program [] programs = new Program [1024];
	byte [] key = new byte [1024];
	int index = 0, count = 0;
//	while (OS.RegEnumKey (OS.HKEY_CLASSES_ROOT, index, key, key.length) != OS.ERROR_NO_MORE_ITEMS) {
//		Program program = getProgram (key);
//		if (program != null) {
//			if (count == programs.length) {
//				Program [] newPrograms = new Program [programs.length + 1024];
//				System.arraycopy (programs, 0, newPrograms, 0, programs.length);
//				programs = newPrograms;
//			}
//			programs [count++] = program;
//		}
//		index++;
//	}
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
 * then the executable is launched.
 *
 * @param fileName the file or program name
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when fileName is null</li>
 *	</ul>
 */
public static boolean launch (String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int index = fileName.lastIndexOf (".");
	if (index == -1) return false;
	String extension = fileName.substring (index, fileName.length ());
	Program program = Program.findProgram (extension);
	if (program != null && program.execute (fileName)) return true;
	try {
		Runtime.getRuntime().exec (fileName);
		return true;
	} catch (IOException e) {
		return false;
	}
}

/**
 * Executes the program with the file as the single argument
 * in the operating system.  It is the responsibility of the
 * programmer to ensure that the file
 *
 * @param fileName the file or program name
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when fileName is null</li>
 *	</ul>
 */
public boolean execute (String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int index = command.indexOf ("%f");
	if (index == -1) return false;
	String prefix = command.substring (0, index);
	String suffix = command.substring (index + 2, command.length ());
	try {
		Runtime.getRuntime ().exec (prefix + '"' + fileName + '"' + suffix);
	} catch (IOException e) {
		return false;
	}
	return true;
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the reciever in the operating
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
 * @return an the name of the program
 */
public String getName () {
	return name;
}

/**
 * Returns true if the receiver and the argument represent
 * the same program.
 * 
 * @return true if the programs are the same
 */
public boolean equals(Object other) {
	if (other instanceof Program) {
		final Program program = (Program) other;
		return extension.equals(program.extension) && name.equals(program.name) &&
			command.equals(program.command);
	}
	return false;
}

/**
 * Returns a hash code suitable for this object.
 * 
 * @return a hash code
 */
public int hashCode() {
	return extension.hashCode() ^ name.hashCode() ^ command.hashCode();
}

public String toString () {
	return "Program {" + name + "}";
}

}

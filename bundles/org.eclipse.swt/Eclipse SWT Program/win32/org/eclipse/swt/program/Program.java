package org.eclipse.swt.program;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import java.io.IOException;

/**
 * Instances of this class represent programs and
 * their assoicated file extensions in the operating
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

/*
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
	if (extension.charAt (0) != '.') extension = "." + extension;
	byte [] key = Converter.wcsToMbcs (0, extension, true);
	int [] phkResult = new int [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return null;
	}	
	int [] lpcbData = new int [] {256};
	byte [] lpData = new byte [lpcbData [0]];			
	int result = OS.RegQueryValueEx (phkResult [0], null, 0, null, lpData, lpcbData);
	OS.RegCloseKey (phkResult [0]);
	if (result != 0) return null;
	return getProgram (lpData);
}

/*
 * Answer all program extensions in the operating system.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	String [] extensions = new String [1024];
	byte [] key = new byte [1024];
	int index = 0, count = 0;
	while (OS.RegEnumKey (OS.HKEY_CLASSES_ROOT, index, key, key.length) != OS.ERROR_NO_MORE_ITEMS) {
		if (key [0] == '.') {
			int length = 0;
			while (length < key.length && key [length] != 0) length++;
			byte [] buffer = new byte [length];
			System.arraycopy (key, 0, buffer, 0, length);
			String extension = new String (Converter.mbcsToWcs (0, buffer));
			if (count == extensions.length) {
				String [] newExtensions = new String [extensions.length + 1024];
				System.arraycopy (extensions, 0, newExtensions, 0, extensions.length);
				extensions = newExtensions;
			}
			extensions [count++] = extension;
		}
		index++;
	}
	if (count != extensions.length) {
		String [] newExtension = new String [count];
		System.arraycopy (extensions, 0, newExtension, 0, count);
		extensions = newExtension;
	}
	return extensions;
}

static String getKeyValue (byte [] key) {
	int [] phkResult = new int [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return null;
	}
	String result = null;
	int [] lpcbData = new int [1];
	if (OS.RegQueryValueEx (phkResult [0], null, 0, null, null, lpcbData) == 0) {
		byte [] lpData = new byte [lpcbData [0]];
		if (OS.RegQueryValueEx (phkResult [0], null, 0, null, lpData, lpcbData) == 0) {
			char [] charArray = Converter.mbcsToWcs (0, lpData);
			result = new String (charArray, 0, charArray.length - 1);
		}
	}
	if (phkResult [0] != 0) OS.RegCloseKey (phkResult [0]);
	return result;
}

static Program getProgram (byte [] key) {

	/* Command */
	byte [] COMMAND = Converter.wcsToMbcs (0, "\\shell\\open\\command", true);
	int length = 0;
	while (length < key.length && key [length] != 0) length++;
	for (int i=0; i<COMMAND.length; i++) key [length + i] = COMMAND [i];
	key [length + COMMAND.length] = 0;
	String command = getKeyValue (key);
	if (command == null || command.length () == 0) return null;

	/* Name */
	key [length] = 0;	
	String name = getKeyValue (key);
	if (name == null || name.length () == 0) return null;
		
	/* Icon */
	byte [] DEFAULT_ICON = Converter.wcsToMbcs (0, "\\DefaultIcon", true);
	for (int i=0; i<DEFAULT_ICON.length; i++) key [length + i] = DEFAULT_ICON [i];
	key [length + DEFAULT_ICON.length] = 0;
	String iconName = getKeyValue (key);
	if (iconName == null || iconName.length () == 0) return null;
	
	Program program = new Program ();
	program.name = name;
	program.command = command;
	program.iconName = iconName;
	return program;
}

/*
 * Answers all available programs in the operating system.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	Program [] programs = new Program [1024];
	byte [] key = new byte [1024];
	int index = 0, count = 0;
	while (OS.RegEnumKey (OS.HKEY_CLASSES_ROOT, index, key, key.length) != OS.ERROR_NO_MORE_ITEMS) {
		Program program = getProgram (key);
		if (program != null) {
			if (count == programs.length) {
				Program [] newPrograms = new Program [programs.length + 1024];
				System.arraycopy (programs, 0, newPrograms, 0, programs.length);
				programs = newPrograms;
			}
			programs [count++] = program;
		}
		index++;
	}
	if (count != programs.length) {
		Program [] newPrograms = new Program [count];
		System.arraycopy (programs, 0, newPrograms, 0, count);
		programs = newPrograms;
	}
	return programs;
}

/*
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
	byte [] OPEN = Converter.wcsToMbcs (0, "open", true);
	byte [] lpFile = Converter.wcsToMbcs (0, fileName, true);
	return OS.ShellExecute (0, OPEN, lpFile, null, null, OS.SW_SHOW) > 32;
}

/*
 * Executes the program with the file as the single argument
 * in the operating system.  It is the responsibility of the
 * programmer to ensure that the file contains valid data for 
 * this program.
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
	boolean quote = true;
	String prefix = command, suffix = "";
	int index = command.indexOf ("%1");
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
	if (quote) fileName = " \"" + fileName + "\"";
	try {
		Runtime.getRuntime ().exec (prefix + fileName + suffix);
	} catch (IOException e) {
		return false;
	}
	return true;
}

/*
 * Returns the receiver's image data.  This is the icon
 * that is associated with the reciever in the operating
 * system.
 *
 * @return the image data for the program
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
		} catch (NumberFormatException e) {};
	}
	byte [] lpszFile = Converter.wcsToMbcs (0, fileName, true);
	int [] phiconSmall = new int[1], phiconLarge = null;
	OS.ExtractIconEx (lpszFile, nIconIndex, phiconLarge, phiconSmall, 1);
	if (phiconSmall [0] == 0) return null;
	Image image = Image.win32_new (null, SWT.ICON, phiconSmall[0]);
	ImageData imageData = image.getImageData ();
	image.dispose ();
	return imageData;
}

/*
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

public String toString () {
	return "Program {" + name + "}";
}

}
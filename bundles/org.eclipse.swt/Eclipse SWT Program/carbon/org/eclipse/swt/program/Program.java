package org.eclipse.swt.program;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
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
	System.out.println("Program.findProgram("+extension+"): nyi");
	if (extension.charAt (0) != '.') extension = "." + extension;
	/* Use the character encoding for the default locale */
	/* AW
	TCHAR key = new TCHAR (0, extension, true);
	int [] phkResult = new int [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return null;
	}	
	int [] lpcbData = new int [] {256};
	TCHAR lpData = new TCHAR (0, lpcbData [0]);
	int result = OS.RegQueryValueEx (phkResult [0], null, 0, null, lpData, lpcbData);
	OS.RegCloseKey (phkResult [0]);
	if (result != 0) return null;
	return getProgram (lpData.toString (0, lpData.strlen ()));
	*/
	return getProgram(extension);
}

/**
 * Answer all program extensions in the operating system.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	String [] extensions = new String [1024];
	/* Use the character encoding for the default locale */
	System.out.println("Program.getExtensions: nyi");
	/* AW
	TCHAR lpName = new TCHAR (0, 1024);
	int [] lpcName = new int [] {lpName.length ()};
	FILETIME ft = new FILETIME ();
	*/
	int dwIndex = 0, count = 0;
	/* AW
	while (OS.RegEnumKeyEx (OS.HKEY_CLASSES_ROOT, dwIndex, lpName, lpcName, null, null, null, ft) != OS.ERROR_NO_MORE_ITEMS) {
		String extension = lpName.toString (0, lpcName [0]);
		lpcName [0] = lpName.length ();
		if (extension.length () > 0 && extension.charAt (0) == '.') {
			if (count == extensions.length) {
				String [] newExtensions = new String [extensions.length + 1024];
				System.arraycopy (extensions, 0, newExtensions, 0, extensions.length);
				extensions = newExtensions;
			}
			extensions [count++] = extension;
		}
		dwIndex++;
	}
	*/
	extensions[count++]= "xml";
	extensions[count++]= "java";
	extensions[count++]= "properties";
	extensions[count++]= "jar";
	extensions[count++]= "zip";
	extensions[count++]= "xml";
	
	if (count != extensions.length) {
		String [] newExtension = new String [count];
		System.arraycopy (extensions, 0, newExtension, 0, count);
		extensions = newExtension;
	}
	return extensions;
}

static Program getProgram (String key) {
	
	System.out.println("Program.getProgram("+key+"): nyi");
	/* Name */
	/* AW
	String name = getKeyValue (key);
	*/
	String name = key;
	if (name == null || name.length () == 0) return null;

	/* Command */
	/* AW
	String COMMAND = "\\shell\\open\\command";
	String command = getKeyValue (key + COMMAND);
	*/
	String command = "/usr/bin/open";
	if (command == null || command.length () == 0) return null;

	/* Icon */
	/* AW
	String DEFAULT_ICON = "\\DefaultIcon";
	String iconName = getKeyValue (key + DEFAULT_ICON);
	*/
	String iconName= "icon";
	if (iconName == null || iconName.length () == 0) return null;
	
	Program program = new Program ();
	program.name = name;
	program.command = command;
	program.iconName = iconName;
	return program;
}

/**
 * Answers all available programs in the operating system.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	System.out.println("Program.getPrograms(): nyi");
	Program [] programs = new Program [1024];
	/* Use the character encoding for the default locale */
	/*
	TCHAR lpName = new TCHAR (0, 1024);
	int [] lpcName = new int [] {lpName.length ()};
	FILETIME ft = new FILETIME ();
	*/
	int dwIndex = 0, count = 0;
	/*
	while (OS.RegEnumKeyEx (OS.HKEY_CLASSES_ROOT, dwIndex, lpName, lpcName, null, null, null, ft) != OS.ERROR_NO_MORE_ITEMS) {	
		String path = lpName.toString (0, lpcName [0]);
		lpcName [0] = lpName.length ();
		Program program = getProgram (path);
		if (program != null) {
			if (count == programs.length) {
				Program [] newPrograms = new Program [programs.length + 1024];
				System.arraycopy (programs, 0, newPrograms, 0, programs.length);
				programs = newPrograms;
			}
			programs [count++] = program;
		}
		dwIndex++;
	}
	*/
	
	programs[count++]= getProgram(".html");

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

	String[] args= new String[] {
		"/usr/bin/open",
		fileName
	};
	try {
		Compatibility.exec(args);
		return true;
	} catch(IOException ex) {
		System.out.println("Program.launch: " + ex);
	}
	
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
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when fileName is null</li>
 *	</ul>
 */
public boolean execute (String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	System.out.println("Program.execute: nyi");
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
		Compatibility.exec(prefix + fileName + suffix);
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
	System.out.println("Program.getImageData: nyi");
	/* AW
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
	*/
	/* Use the character encoding for the default locale */
	/*
	TCHAR lpszFile = new TCHAR (0, fileName, true);
	int [] phiconSmall = new int[1], phiconLarge = null;
	OS.ExtractIconEx (lpszFile, nIconIndex, phiconLarge, phiconSmall, 1);
	if (phiconSmall [0] == 0) return null;
	Image image = Image.win32_new (null, SWT.ICON, phiconSmall[0]);
	*/
	
	Image image = new Image(null, 16, 16);
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
	if (this == other) return true;
	if (other instanceof Program) {
		final Program program = (Program) other;
		return name.equals(program.name) && command.equals(program.command)
			&& iconName.equals(program.iconName);
	}
	return false;
}

/**
 * Returns a hash code suitable for this object.
 * 
 * @return a hash code
 */
public int hashCode() {
	return name.hashCode() ^ command.hashCode() ^ iconName.hashCode();
}

public String toString () {
	return "Program {" + name + "}";
}

}
package org.eclipse.swt.program;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
	if (extension.charAt (0) != '.') extension = "." + extension;
	/* Use the character encoding for the default locale */
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
}

/**
 * Answer all program extensions in the operating system.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	String [] extensions = new String [1024];
	/* Use the character encoding for the default locale */
	TCHAR lpName = new TCHAR (0, 1024);
	int [] lpcName = new int [] {lpName.length ()};
	FILETIME ft = new FILETIME ();
	int dwIndex = 0, count = 0;
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
	if (count != extensions.length) {
		String [] newExtension = new String [count];
		System.arraycopy (extensions, 0, newExtension, 0, count);
		extensions = newExtension;
	}
	return extensions;
}

static Program [] getFileClassPrograms () {
	Program [] programs = new Program [128];
	/* Use the character encoding for the default locale */
	TCHAR lpName = new TCHAR (0, 1024);
	int [] lpcName = new int [] {lpName.length ()};
	FILETIME ft = new FILETIME ();
	int dwIndex = 0, count = 0;
	while (OS.RegEnumKeyEx (OS.HKEY_CLASSES_ROOT, dwIndex, lpName, lpcName, null, null, null, ft) != OS.ERROR_NO_MORE_ITEMS) {	
		String path = lpName.toString (0, lpcName [0]);
		lpcName [0] = lpName.length ();
		Program program = getProgram (path);
		if (program != null) {
			/* Make sure the name is unique */
			boolean isDuplicate = false;
			for (int i = 0; i < count; i++) {
				String value = programs [i].name;
				if (value.equalsIgnoreCase (program.name)) {
					isDuplicate = true;
					break;
				}
			}
			if (!isDuplicate) {
				if (count == programs.length) {
					Program [] newPrograms = new Program [programs.length + 128];
					System.arraycopy (programs, 0, newPrograms, 0, programs.length);
					programs = newPrograms;
				}
				programs [count++] = program;
			}
		}
		dwIndex++;
	}
	if (count != programs.length) {
		Program [] newPrograms = new Program [count];
		System.arraycopy (programs, 0, newPrograms, 0, count);
		programs = newPrograms;
	}
	return programs;
}

static String getKeyValue (String string, String value, boolean expand) {
	/* Use the character encoding for the default locale */
	TCHAR key = new TCHAR (0, string, true);
	int [] phkResult = new int [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return null;
	}
	TCHAR lpValueName = value == null ? null : new TCHAR (0, value, true);
	String result = null;
	int [] lpcbData = new int [1];
	if (OS.RegQueryValueEx (phkResult [0], lpValueName, 0, null, null, lpcbData) == 0) {
		/* Use the character encoding for the default locale */
		TCHAR lpData = new TCHAR (0, lpcbData [0] / TCHAR.sizeof);
		if (OS.RegQueryValueEx (phkResult [0], lpValueName, 0, null, lpData, lpcbData) == 0) {
			if (!OS.IsWinCE && expand) {
				int nSize = OS.ExpandEnvironmentStrings (lpData, null, 0);
				TCHAR lpDst = new TCHAR (0, nSize);
				OS.ExpandEnvironmentStrings (lpData, lpDst, nSize);
				result = lpDst.toString (0, Math.max (0, nSize - 1));
			} else {
				int length = Math.max (0, lpData.length () - 1);
				result = lpData.toString (0, length);
			}
		}
	}
	if (phkResult [0] != 0) OS.RegCloseKey (phkResult [0]);
	return result;
}

static Program getProgram (String key) {

	/* Command */
	String COMMAND = "\\shell\\open\\command";
	String command = getKeyValue (key + COMMAND, null, true);
	if (command == null || command.length () == 0) return null;
	
	/* Name */
	String name = getProgramName(key, command);
	if (name == null || name.length () == 0) return null;

	/* Icon */
	String iconName = getProgramPath(command);
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
	/* 
	* The Windows registry maintains a list of file extensions and their related
	* editors. In addition, W2K and XP maintain a list of applications which can
	* be used to populate the 'Open As' dialog. 2 articles in MSDN describe this
	* mechanism: 'File Associations: Arbitrary File Types' and 'Creating a File
	* Association'.
	* There does not seem to be an API to retrieve the list of programs in the
	* 'Open As' dialog. As a result, on W2K machines and above, we look up the
	* registry as described in the articles above. On Win95 and NT machines, we use
	* the list of applications registered to one or more file classes.
	*/
	if (OS.IsWin95) return getFileClassPrograms ();
	if ((OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) < (4 << 16 | 10)) return getFileClassPrograms ();
	String KEY = "Applications";
	/* Use the character encoding for the default locale */
	TCHAR key = new TCHAR (0, KEY, true);
	int [] phkResult = new int [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return  new Program [0];
	}
	Program [] programs = new Program [128];
	/* Use the character encoding for the default locale */
	TCHAR lpName = new TCHAR (0, 1024);
	int [] lpcName = new int [] {lpName.length ()};
	FILETIME ft = new FILETIME ();
	int dwIndex = 0, count = 0;
	while (OS.RegEnumKeyEx (phkResult[0], dwIndex, lpName, lpcName, null, null, null, ft) != OS.ERROR_NO_MORE_ITEMS) {	
		String path = KEY + "\\" + lpName.toString (0, lpcName [0]);
		lpcName [0] = lpName.length ();
		Program program = getProgram (path);
		if (program != null) {
			if (count == programs.length) {
				Program [] newPrograms = new Program [programs.length + 128];
				System.arraycopy (programs, 0, newPrograms, 0, programs.length);
				programs = newPrograms;
			}
			programs [count++] = program;
		}
		dwIndex++;
	}
	if (count != programs.length) {
		Program [] newPrograms = new Program [count];
		System.arraycopy (programs, 0, newPrograms, 0, count);
		programs = newPrograms;
	}
	return programs;
}

static String getProgramName (String key, String command) {
	String FRIENDLYAPPNAME = "FriendlyAppName";
	/* The FriendlyAppName registry key contains the application name 
	 * and must be searched first. It can be missing.	 */
	String name = getKeyValue (key, FRIENDLYAPPNAME, true);
	if (name != null) {
		/* The value is either a string or a reference to a resource
		 * string located in an exe or dll.		 */
		if (name.charAt (0) == '@') {
			int nResourceIndex = 0;
			String fileName = name;
			int index = name.indexOf (',');
			if (index != -1) {
				fileName = name.substring (1, index);
				String resourceIndex = name.substring (index + 1, name.length ()).trim ();
				try {
					nResourceIndex = Integer.parseInt (resourceIndex);
				} catch (NumberFormatException e) {};
			}
			name = null;
			/* Use the character encoding for the default locale */
			TCHAR lpFileName = new TCHAR (0, fileName, true);
			int hInstance = OS.LoadLibraryEx (lpFileName, 0, OS.LOAD_LIBRARY_AS_DATAFILE);
			if (hInstance != 0) {
				if (nResourceIndex < 0) nResourceIndex = -nResourceIndex;
				int nBufferMax = 1024;
				TCHAR lpBuffer = new TCHAR (0, nBufferMax);
				int nbr = OS.LoadString (hInstance, nResourceIndex, lpBuffer, nBufferMax);
				if (nbr != 0) name = lpBuffer.toString (0, nbr);
				OS.FreeLibrary (hInstance);
			}
		}
		if (name != null && name.length () != 0) return name;
	}
	/* Get the friendly name in the version information of the executable */
	String path = getProgramPath (command);
	if (path == null) return null;
	TCHAR lptstrFileName = new TCHAR (0, path, true);
	int [] lpdwHandle = new int [1];
	int size = OS.GetFileVersionInfoSize (lptstrFileName, lpdwHandle);
	if (size != 0) {
		String langCodepage = null;
		int hHeap = OS.GetProcessHeap ();
		int lpData = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, size);
		if (OS.GetFileVersionInfo(lptstrFileName, 0, size, lpData) != 0) {
			int[] lplpBuffer = new int [1];
			int[] puLen = new int [1];
			short[] codes = new short [2]; 
			/* Get the hexadecimal string representing the language and codepage
			 * used by the String table 
			 */
			if (OS.VerQueryValue(lpData, new TCHAR(0, "\\VarFileInfo\\Translation", true), lplpBuffer, puLen)) {
				if (puLen [0] == 4 && lplpBuffer [0] != 0) {
					OS.MoveMemory(codes, lplpBuffer [0], 4);
					String data = Integer.toHexString(codes [0] & 0xffff);
					while (data.length () < 4) data = "0" + data;
					langCodepage = data;
					data = Integer.toHexString (codes [1] & 0xffff);
					while (data.length () < 4) data = "0" + data;
					langCodepage += data;					
				}	
			}
			if (langCodepage != null) {
				TCHAR lpSubBlock = new TCHAR (0, "\\StringFileInfo\\" + langCodepage + "\\FileDescription", true);
				if (OS.VerQueryValue(lpData, lpSubBlock, lplpBuffer, puLen)) {
					if (lplpBuffer [0] != 0 && puLen [0] != 0) {
						TCHAR description = new TCHAR(0, puLen [0]);
						OS.MoveMemory(description, lplpBuffer [0], puLen [0] * TCHAR.sizeof);
						name = description.toString (0, description.strlen());
					}
				}
			}
		}
		if (lpData != 0) OS.HeapFree (hHeap, 0, lpData);
	}
	return name;
}

static String getProgramPath (String command) {
	String path = null;
	/*
	 * Parse the command which may contain quotes and parameters.
	 * Remove the leading quote if any, and ignore everything
	 * following the executable extension. If the command does not
	 * refer to an executable (.exe file only), return null.	 */	
	int start = command.charAt (0) != '"' ? 0 : 1;
	String extension = ".exe";
	int index = command.toLowerCase ().indexOf (extension);
	if (index > start) path = command.substring (start, index + extension.length ());
	return path;
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
	
	/* Use the character encoding for the default locale */
	int hHeap = OS.GetProcessHeap ();
	TCHAR buffer1 = new TCHAR (0, "open", true);
	int byteCount1 = buffer1.length () * TCHAR.sizeof;
	int lpVerb = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount1);
	OS.MoveMemory (lpVerb, buffer1, byteCount1);
	TCHAR buffer2 = new TCHAR (0, fileName, true);
	int byteCount2 = buffer2.length () * TCHAR.sizeof;
	int lpFile = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount2);
	OS.MoveMemory (lpFile, buffer2, byteCount2);
	
	SHELLEXECUTEINFO info = new SHELLEXECUTEINFO ();
	info.cbSize = SHELLEXECUTEINFO.sizeof;
	info.lpVerb = lpVerb;
	info.lpFile = lpFile;
	info.nShow = OS.SW_SHOW;
	
	boolean result = OS.ShellExecuteEx (info);
		
	if (lpVerb != 0) OS.HeapFree (hHeap, 0, lpVerb);	
	if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);
	
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
	/* Use the character encoding for the default locale */
	TCHAR lpszFile = new TCHAR (0, fileName, true);
	int [] phiconSmall = new int[1], phiconLarge = null;
	OS.ExtractIconEx (lpszFile, nIconIndex, phiconLarge, phiconSmall, 1);
	if (phiconSmall [0] == 0) return null;
	Image image = Image.win32_new (null, SWT.ICON, phiconSmall[0]);
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
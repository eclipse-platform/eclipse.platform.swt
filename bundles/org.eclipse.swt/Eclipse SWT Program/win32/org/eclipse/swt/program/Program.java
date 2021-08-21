/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.program;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

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
	String extension;
	static final String [] ARGUMENTS = new String [] {"%1", "%l", "%L"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program () {
}

static String assocQueryString (int assocStr, TCHAR key, boolean expand) {
	TCHAR pszOut = new TCHAR(0, 1024);
	int[] pcchOut = new int[1];
	pcchOut[0] = pszOut.length();
	int flags = OS.ASSOCF_NOTRUNCATE | OS.ASSOCF_INIT_IGNOREUNKNOWN;
	int result = OS.AssocQueryString (flags, assocStr, key, null, pszOut, pcchOut);
	if (result == OS.E_POINTER) {
		pszOut = new TCHAR(0, pcchOut [0]);
		result = OS.AssocQueryString (flags, assocStr, key, null, pszOut, pcchOut);
	}
	if (result == 0) {
		if (expand) {
			int length = OS.ExpandEnvironmentStrings (pszOut, null, 0);
			if (length != 0) {
				TCHAR lpDst = new TCHAR (0, length);
				OS.ExpandEnvironmentStrings (pszOut, lpDst, length);
				return lpDst.toString (0, Math.max (0, length - 1));
			} else {
				return "";
			}
		} else {
			return pszOut.toString (0, Math.max (0, pcchOut [0] - 1));
		}
	}
	return null;
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
	TCHAR key = new TCHAR (0, extension, true);
	Program program = null;
	String command = assocQueryString (OS.ASSOCSTR_COMMAND, key, true);
	if (command != null) {
		String name = null;
		if (name == null) name = assocQueryString (OS.ASSOCSTR_FRIENDLYDOCNAME, key, false);
		if (name == null) name = assocQueryString (OS.ASSOCSTR_FRIENDLYAPPNAME, key, false);
		if (name == null) name = "";
		String iconName = assocQueryString (OS.ASSOCSTR_DEFAULTICON, key, true);
		if (iconName == null) iconName = "";
		program = new Program ();
		program.name = name;
		program.command = command;
		program.iconName = iconName;
		program.extension = extension;
	}
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
	char [] lpName = new char [1024];
	int [] lpcName = {lpName.length};
	int dwIndex = 0, count = 0;
	while (OS.RegEnumKeyEx (OS.HKEY_CLASSES_ROOT, dwIndex, lpName, lpcName, null, null, null, 0) != OS.ERROR_NO_MORE_ITEMS) {
		String extension = new String (lpName, 0, lpcName [0]);
		lpcName [0] = lpName.length;
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

static String getKeyValue (String string, boolean expand) {
	TCHAR key = new TCHAR (0, string, true);
	long [] phkResult = new long [1];
	if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) != 0) {
		return null;
	}
	String result = null;
	int [] lpcbData = new int [1];
	if (OS.RegQueryValueEx (phkResult [0], (TCHAR) null, 0, null, (TCHAR) null, lpcbData) == 0) {
		result = "";
		int length = lpcbData [0] / TCHAR.sizeof;
		/*
		 * Crash is seen when the size of REG_SZ entry in HKEY_CLASSES_ROOT
		 * is not multiple of a Unicode byte length. The REG_SZ entry in
		 * Windows registry may not have been stored with the proper
		 * terminating null characters: e.g. non null terminated string or a
		 * single byte null terminated. Refer below MSDN article on this:
		 * https://msdn.microsoft.com/en-us/library/windows/desktop/ms724884
		 * %28v=vs.85%29.aspx Hence solution is to adjust the buffer length
		 * accordingly. Refer Bug 157010 for more details.
		 */
		if (lpcbData [0] % TCHAR.sizeof != 0) {
			length++;
		}
		if (length != 0) {
			char [] lpData = new char [length];
			if (OS.RegQueryValueEx (phkResult [0], null, 0, null, lpData, lpcbData) == 0) {
				if (expand) {
					length = OS.ExpandEnvironmentStrings (lpData, null, 0);
					if (length != 0) {
						char [] lpDst = new char [length];
						OS.ExpandEnvironmentStrings (lpData, lpDst, length);
						result = new String (lpDst, 0, length - 1);
					}
				} else {
					result = new String (lpData, 0, length - 1);
				}
			}
		}
	}
	if (phkResult [0] != 0) OS.RegCloseKey (phkResult [0]);
	return result;
}

static Program getProgram (String key, String extension) {

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

	/* Program */
	Program program = new Program ();
	program.name = name;
	program.command = command;
	program.iconName = iconName;
	program.extension = extension;
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
	char [] lpName = new char [1024];
	int [] lpcName = new int [] {lpName.length};
	int dwIndex = 0;
	LinkedHashSet<String> paths = new LinkedHashSet<>();
	// enumerating all class names takes ~ 1/5 of the time:
	while (OS.RegEnumKeyEx (OS.HKEY_CLASSES_ROOT, dwIndex, lpName, lpcName, null, null, null, 0) != OS.ERROR_NO_MORE_ITEMS) {
		String path = new String (lpName, 0, lpcName [0]);
		lpcName [0] = lpName.length;
		paths.add(path);
		dwIndex++;
	}
	//map paths to programs in parallel which takes now ~ 4/5 of time:
	ConcurrentHashMap<String, Program> programs = new ConcurrentHashMap<>(paths.size());
	paths.stream().parallel().forEach(path -> {
		Program program = getProgram(path, null); // getProgram takes most time
		if (program != null) {
			programs.put(path, program);
		}
	});
	// restore sort order and make distinct in terms of Program::equals:
	LinkedHashSet<Program> sortedPrograms = paths.stream().map(name->programs.get(name)).filter(p->p!=null).collect(Collectors.toCollection(LinkedHashSet::new));
	return sortedPrograms.toArray(new Program[sortedPrograms.size()]);
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
 * executable is launched. The program is launched with the specified
 * working directory only when the <code>workingDir</code> exists and
 * <code>fileName</code> is an executable.
 * Note that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @param fileName the file name or program name or URL (http:// or https://)
 * @param workingDir the name of the working directory or null
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 *
 * @since 3.6
 */
public static boolean launch (String fileName, String workingDir) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);

	long hHeap = OS.GetProcessHeap ();
	TCHAR buffer = new TCHAR (0, fileName, true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	long lpFile = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpFile, buffer, byteCount);

	long lpDirectory = 0;
	if (workingDir != null && OS.PathIsExe(lpFile)) {
		TCHAR buffer1 = new TCHAR (0, workingDir, true);
		byteCount = buffer1.length () * TCHAR.sizeof;
		lpDirectory = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpDirectory, buffer1, byteCount);
	}

	SHELLEXECUTEINFO info = new SHELLEXECUTEINFO ();
	info.cbSize = SHELLEXECUTEINFO.sizeof;
	info.lpFile = lpFile;
	info.lpDirectory = lpDirectory;
	info.nShow = OS.SW_SHOW;
	boolean result = OS.ShellExecuteEx (info);
	if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);
	if (lpDirectory != 0) OS.HeapFree (hHeap, 0, lpDirectory);
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
	long hHeap = OS.GetProcessHeap ();
	TCHAR buffer = new TCHAR (0, commandLine, true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	long lpCommandLine = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpCommandLine, buffer, byteCount);
	STARTUPINFO lpStartupInfo = new STARTUPINFO ();
	lpStartupInfo.cb = STARTUPINFO.sizeof;
	PROCESS_INFORMATION lpProcessInformation = new PROCESS_INFORMATION ();
	boolean success = OS.CreateProcess (0, lpCommandLine, 0, 0, false, 0, 0, 0, lpStartupInfo, lpProcessInformation);
	if (lpCommandLine != 0) OS.HeapFree (hHeap, 0, lpCommandLine);
	if (lpProcessInformation.hProcess != 0) OS.CloseHandle (lpProcessInformation.hProcess);
	if (lpProcessInformation.hThread != 0) OS.CloseHandle (lpProcessInformation.hThread);
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
	if (extension != null) {
		SHFILEINFO shfi = new SHFILEINFO ();
		int flags = OS.SHGFI_ICON | OS.SHGFI_SMALLICON | OS.SHGFI_USEFILEATTRIBUTES;
		TCHAR pszPath = new TCHAR (0, extension, true);
		OS.SHGetFileInfo (pszPath.chars, OS.FILE_ATTRIBUTE_NORMAL, shfi, SHFILEINFO.sizeof, flags);
		if (shfi.hIcon != 0) {
			Image image = Image.win32_new (null, SWT.ICON, shfi.hIcon);
			/* Fetch the ImageData at 100% zoom and return */
			ImageData imageData = image.getImageData ();
			image.dispose ();
			return imageData;
		}
	}
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
	if (length > 1 && fileName.charAt (0) == '\"') {
		if (fileName.charAt (length - 1) == '\"') {
			fileName = fileName.substring (1, length - 1);
		}
	}
	TCHAR lpszFile = new TCHAR (0, fileName, true);
	long [] phiconSmall = new long[1], phiconLarge = null;
	OS.ExtractIconEx (lpszFile, nIconIndex, phiconLarge, phiconSmall, 1);
	if (phiconSmall [0] == 0) return null;
	Image image = Image.win32_new (null, SWT.ICON, phiconSmall [0]);
	/* Fetch the ImageData at 100% zoom and return */
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
@Override
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
@Override
public int hashCode() {
	return name.hashCode() ^ command.hashCode() ^ iconName.hashCode();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
@Override
public String toString () {
	return "Program {" + name + "}"; //$NON-NLS-1$ //$NON-NLS-2$
}

}

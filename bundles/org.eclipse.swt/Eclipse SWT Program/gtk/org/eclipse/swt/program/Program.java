/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.program;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import org.eclipse.swt.widgets.Display;

import java.io.*;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Instances of this class represent programs and
 * their assoicated file extensions in the operating
 * system.
 */
public final class Program {	
	String name;
	String extension;
	String command;
	ImageData imageData;
	Display display;

	/* Gnome specific
	 * true if command expects a URI
	 * false if expects a path
	 */
	boolean gnomeExpectUri;

	static private final String   desktopData = "Program_DESKTOP";
		
	static final int DESKTOP_UNKNOWN = 0;
	static final int DESKTOP_KDE = 1;		// Linux
	static final int DESKTOP_GNOME = 2;		// Linux
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program() {
}

/* Determine the desktop for the given display. */
static int getDesktop(Display display) {
	if (display == null) return DESKTOP_UNKNOWN;
	
	// If the desktop type for this display is already known, return it.
	Integer desktopValue = (Integer) display.getData( desktopData );
	if (desktopValue != null) {
		return desktopValue.intValue();
	}
	int desktop = DESKTOP_UNKNOWN;

	if (isGnomeDesktop()) {
		if (gnome_init()) desktop = DESKTOP_GNOME;
	}
	// Save the desktop type on the display itself.
	display.setData( desktopData, new Integer(desktop) );
	return desktop;
}

/**
 * Finds the program that is associated with an extension.
 * The extension may or may not begin with a '.'.
 *
 * @param extension the program extension
 * @return the program or <code>null</code>
 *
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static Program findProgram(String extension) {
	boolean disposeDisplay = false;
	Display display = Display.getCurrent ();
	if (display == null) {
		display = new Display ();
		disposeDisplay = true;
	}
	Program result = findProgram (display, extension);
	if (disposeDisplay) display.dispose ();
	return result;
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static Program findProgram(Display display, String extension) {
	if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (extension.length() == 0) return null;
	if (extension.charAt(0) != '.') extension = "." + extension;
	String command = null;
	String name = null;
	int desktop = getDesktop(display);
	Hashtable mimeInfo = null;
	if (desktop == DESKTOP_GNOME) mimeInfo = gnome_getMimeInfo(display);
	if (mimeInfo == null) return null;

	// Find the data type matching the extension.
	Iterator keys = mimeInfo.keySet().iterator();
	while (name == null && keys.hasNext()) {
		String mimeType = (String) keys.next();
		Vector mimeExts = (Vector) mimeInfo.get(mimeType);
		for (int index = 0; index < mimeExts.size(); index++){
			if (extension.equals(mimeExts.elementAt(index) )) {
				name = mimeType;
				break;
			}
		}
	}			
	if (name == null) return null;

	// Get the corresponding command for the mime type.
	boolean[] gnomeExpectUri = null;
	if (desktop == DESKTOP_GNOME) {
		gnomeExpectUri = new boolean[1];
		command = gnome_getMimeTypeCommand(name, gnomeExpectUri);
	}
	if (command == null) return null;
	
	// Return the corresponding program.
	Program program   = new Program();
	program.name      = name;
	program.command   = command;
	if (desktop == DESKTOP_GNOME) program.gnomeExpectUri = gnomeExpectUri[0];
	program.extension = extension;
	program.display   = display;
	return program;
}

/**
 * Answer all program extensions in the operating system.
 *
 * @return an array of extensions
 */
public static String[] getExtensions() {
	boolean disposeDisplay = false;
	Display display = Display.getCurrent ();
	if (display == null) {
		display = new Display ();
		disposeDisplay = true;
	}
	String [] result = getExtensions (display);
	if (disposeDisplay) display.dispose ();
	return result;
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static String[] getExtensions(Display display) {
	int desktop = getDesktop(display);
	Hashtable mimeInfo = null;
	if (desktop == DESKTOP_GNOME) mimeInfo = gnome_getMimeInfo(display);
	if (mimeInfo == null) return new String[0];

			
	// Create a unique set of the file extensions.
	Vector extensions = new Vector();
	Iterator keys = mimeInfo.keySet().iterator();
	while (keys.hasNext()) {
		String mimeType = (String)keys.next();
		Vector mimeExts = (Vector)mimeInfo.get( mimeType );
		for (int index = 0; index < mimeExts.size(); index++){
			if (!extensions.contains(mimeExts.elementAt(index) )) {
				extensions.addElement(mimeExts.elementAt(index) );
			}
		}
	}
			
	// Return the list of extensions.
	String[] extStrings = new String[extensions.size()];
	for (int index = 0; index < extensions.size(); index++) {
		extStrings[index] = (String)extensions.elementAt(index);
	}			
	return extStrings;
}

/**
 * Answers all available programs in the operating system.
 *
 * @return an array of programs
 */
public static Program[] getPrograms() {
	boolean disposeDisplay = false;
	Display display = Display.getCurrent ();
	if (display == null) {
		display = new Display ();
		disposeDisplay = true;
	}
	Program [] result = getPrograms (display);
	if (disposeDisplay) display.dispose ();
	return result;
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static Program[] getPrograms(Display display) {
	int desktop = getDesktop(display);
	Hashtable mimeInfo = null;
	if (desktop == DESKTOP_GNOME) mimeInfo = gnome_getMimeInfo(display);
	if (mimeInfo == null) return new Program[0];
			
	// Create a list of programs with commands.
	Vector programs = new Vector();
	boolean[] gnomeExpectUri = null;
	if (desktop == DESKTOP_GNOME) gnomeExpectUri = new boolean[1];
	Iterator keys = mimeInfo.keySet().iterator();
	while (keys.hasNext()) {
		String mimeType  = (String)keys.next();
		Vector mimeExts  = (Vector)mimeInfo.get(mimeType);
		String extension = "";
		if (mimeExts.size() > 0){
			extension = (String)mimeExts.elementAt(0);
		}
		String command = null;
		if (desktop == DESKTOP_GNOME) command = gnome_getMimeTypeCommand( mimeType, gnomeExpectUri);
		if (command != null) {
			Program program = new Program ();
			program.name = mimeType;
			program.command = command;
			if (desktop == DESKTOP_GNOME) {
				program.gnomeExpectUri = gnomeExpectUri[0];
				program.imageData = gnome_getMimeIcon(mimeType);
			}
			program.extension = extension;
			program.display = display;
			programs.addElement(program);
		}
	}
			
	// Return the list of programs to the user.
	Program[] programList = new Program[programs.size()];
	for (int index = 0; index < programList.length; index++) {
		programList[index] = (Program)programs.elementAt(index);
	}
	return programList;
}

private static boolean isGnomeDesktop() {
	/*
	 * A Gnome Window Manager is detected by looking for a specific
	 * property on the root window.
	 */
	byte[] name = Converter.wcsToMbcs(null, "_WIN_SUPPORTING_WM_CHECK", true);
	int atom = OS.gdk_atom_intern(name, true);
	if (atom == OS.GDK_NONE) return false;	
	int[] actualType = new int[1];
	int[] actualFormat = new int[1];
	int[] actualLength = new int[1];
	int[] data = new int[1];
	if (!OS.gdk_property_get(OS.GDK_ROOT_PARENT(), atom, OS.XA_CARDINAL,
		0, 1, 0, actualType, actualFormat, actualLength, data)) return false;
	if (data[0] != 0) OS.g_free(data[0]);
	return actualLength[0] > 0;
}

/*
 * Obtain the registered mime type information and
 * return it in a map. The key of each entry
 * in the map is the mime type name. The value is
 * a vector of the associated file extensions.
 */
private static Hashtable gnome_getMimeInfo(Display display) {
	Hashtable mimeInfo = new Hashtable();
	int[] mimeData = new int[1];
	int[] extensionData = new int[1];
	int mimeList = GNOME.gnome_vfs_get_registered_mime_types();
	int mimeElement = mimeList;
	while (mimeElement != 0) {
		OS.memmove (mimeData, mimeElement, 4);
		int mimePtr = mimeData[0];
		int mimeLength = OS.strlen(mimePtr);
		byte[] mimeTypeBuffer = new byte[mimeLength];
		OS.memmove(mimeTypeBuffer, mimePtr, mimeLength);
		String mimeType = new String(Converter.mbcsToWcs(null, mimeTypeBuffer));
		int extensionList = GNOME.gnome_vfs_mime_get_extensions_list(mimePtr);
		if (extensionList != 0) {
			Vector extensions = new Vector();
			int extensionElement = extensionList;
			while (extensionElement != 0) {
				OS.memmove(extensionData, extensionElement, 4);
				int extensionPtr = extensionData[0];
				int extensionLength = OS.strlen(extensionPtr);
				byte[] extensionBuffer = new byte[extensionLength];
				OS.memmove(extensionBuffer, extensionPtr, extensionLength);
				String extension = new String(Converter.mbcsToWcs(null, extensionBuffer));
				extension = '.' + extension;
				extensions.add(extension);
				extensionElement = GNOME.g_list_next(extensionElement); 
			}
			GNOME.gnome_vfs_mime_extensions_list_free(extensionList);
			if (extensions.size() > 0) mimeInfo.put(mimeType, extensions);
		}
		mimeElement = GNOME.g_list_next(mimeElement);
	}
	if (mimeList != 0) GNOME.gnome_vfs_mime_registered_mime_type_list_free(mimeList);
	return mimeInfo;
}

private static String gnome_getMimeTypeCommand(String mimeType, boolean gnomeExpectUri[]) {
	String command = null;
	GnomeVFSMimeApplication application = new GnomeVFSMimeApplication();
	byte[] mimeTypeBuffer = Converter.wcsToMbcs(null, mimeType, true);
	int ptr = GNOME.gnome_vfs_mime_get_default_application(mimeTypeBuffer);
	if (ptr != 0) {
		GNOME.memmove(application, ptr, GnomeVFSMimeApplication.sizeof);
		int	length = OS.strlen(application.command);
		byte[] buffer = new byte[length];
		OS.memmove(buffer, application.command, length);
		command = new String(Converter.mbcsToWcs(null, buffer));
		gnomeExpectUri[0] = application.expects_uris == GNOME.GNOME_VFS_MIME_APPLICATION_ARGUMENT_TYPE_URIS;
		GNOME.gnome_vfs_mime_application_free(ptr);
	}
	return command;
}

private static ImageData gnome_getMimeIcon(String mimeType) {
	byte[] app_id = Converter.wcsToMbcs(null, "swt", true);
	int ptr = OS.g_malloc(app_id.length);
	OS.memmove(ptr, app_id, app_id.length);
	int[] argv =  {ptr, 0};
	/* 
	* Note.  gnome_program_locate_file requires gnome_program_init to have been called at least
	* once otherwise it outputs an error message.  The workaround is to call gnome_program_init
	* with the minimal amount of arguments required to register as a Gnome application. 
	*/
	int program = GNOME.gnome_program_init(app_id, app_id, GNOME.LIBGNOME_MODULE(), 1, argv, GNOME.GNOME_PARAM_NONE);
	OS.g_free(ptr);
	byte[] mimeTypeBuffer = Converter.wcsToMbcs(null, mimeType, true);
	ptr = GNOME.gnome_vfs_mime_get_icon(mimeTypeBuffer);
	if (ptr == 0) return null;
	int path = GNOME.gnome_program_locate_file(program, GNOME.GNOME_FILE_DOMAIN_PIXMAP, ptr, true, 0);
	OS.g_free(ptr);
	OS.g_free(program);
	if (path == 0) return null;
	int length = OS.strlen(path);
	if (length == 0) return null;
	byte[] buffer = new byte[length];
	OS.memmove(buffer, path, length);
	OS.g_free(path);
	String result = new String(Converter.mbcsToWcs(null, buffer));
	ImageData data = null;
	try {
		data = new ImageData(result);
	} catch (Exception e) {
	}
	return data;
}

// Private method for parsing a command line into its arguments.
private static String[] parseCommand(String cmd) {
	Vector args = new Vector();
	int sIndex = 0;
	int eIndex;
	while (sIndex < cmd.length()) {
		// Trim initial white space of argument.
		while (sIndex < cmd.length() && Compatibility.isWhitespace(cmd.charAt(sIndex))) {
			sIndex++;
		}
		if (sIndex < cmd.length()) {
			// If the command is a quoted string
			if (cmd.charAt(sIndex) == '"' || cmd.charAt(sIndex) == '\'') {
				// Find the terminating quote (or end of line).
				// This code currently does not handle escaped characters (e.g., " a\"b").
				eIndex = sIndex + 1;
				while (eIndex < cmd.length() && cmd.charAt(eIndex) != cmd.charAt(sIndex)) {
					eIndex++;
				}
				if (eIndex >= cmd.length()) { // the terminating quote was not found
					// Add the argument as is with only one initial quote.
					args.addElement(cmd.substring(sIndex, eIndex));
				}
				// else add the argument, trimming off the quotes.
				else {
					args.addElement(cmd.substring( sIndex+1, eIndex));
				}
				sIndex = eIndex + 1;
			}
			
			// else use white space for the delimiters.
			else {
				eIndex = sIndex;
				while (eIndex < cmd.length() && !Compatibility.isWhitespace(cmd.charAt(eIndex))) {
					eIndex++;
				}
				args.addElement(cmd.substring(sIndex, eIndex));
				sIndex = eIndex + 1;
			}
		}
	}
	
	String[] strings = new String[args.size()];
	for (int index =0; index < args.size(); index++) {
		strings[index] = (String)args.elementAt(index);
	}
	return strings;
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
public static boolean launch(String fileName) {
	boolean disposeDisplay = false;
	Display display = Display.getCurrent ();
	if (display == null) {
		display = new Display ();
		disposeDisplay = true;
	}
	boolean result = launch (display, fileName);
	if (disposeDisplay) display.dispose ();
	return result;
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static boolean launch(Display display, String fileName) {
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	// If the argument appears to be a data file (it has an extension)
	int index = fileName.lastIndexOf('.');
	if (index > 0) {
		// Find the associated program, if one is defined.
		String extension = fileName.substring(index);
		Program program = Program.findProgram(display, extension); 
		
		// If the associated program is defined and can be executed, return.
		if (program != null && program.execute(fileName)) return true;
	}
	
	// Otherwise, the argument was the program itself.
	try {
		Compatibility.exec(fileName);
		return true;
	} catch (IOException e) {
		return false;
	}
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
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	int desktop = getDesktop(display);
	if (desktop == DESKTOP_GNOME) {		
		if (gnomeExpectUri) {
			/* convert the given path into a URL */
			fileName = "file://" + fileName;
		}

		// Parse the command into its individual arguments.
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
	
		// If a file name was given but the command did not have "%f"
		if ((fileName.length() > 0) && (fileArg < 0)) {
			String[] newArgs = new String[args.length + 1];
			for (index = 0; index < args.length; index++)
				newArgs[index] = args[index];
			newArgs[args.length] = fileName;
			args = newArgs;
		}
	
		// Execute the command.
		try {
			Compatibility.exec(args);
		} catch (IOException e) {
			return false;
		}
		return true;
	}		

	return false;
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the reciever in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData () {
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
		final Program program = (Program)other;
		return display == program.display && extension.equals(program.extension) &&
			name.equals(program.name) && command.equals(program.command);
	}
	return false;
}

/**
 * Returns a hash code suitable for this object.
 * 
 * @return a hash code
 */
public int hashCode() {
	return extension.hashCode() ^ name.hashCode() ^ command.hashCode() ^ display.hashCode();
}

public String toString () {
	return "Program {" + name + "}";
}

static boolean gnome_init () {
	try {
		Library.loadLibrary("swt-gnome");
		return GNOME.gnome_vfs_init();
	} catch (Throwable e) {
		return false;
	}
}
}

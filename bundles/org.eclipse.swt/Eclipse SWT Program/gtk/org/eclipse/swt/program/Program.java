/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.program;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gnome.*;
import org.eclipse.swt.internal.kde.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.util.*;

/**
 * Instances of this class represent programs and
 * their assoicated file extensions in the operating
 * system.
 */
public final class Program {
	String name;
	String command;
	String iconPath;
	Display display;

	/* Gnome specific
	 * true if command expects a URI
	 * false if expects a path
	 */
	boolean gnomeExpectUri;

	static final String DESKTOP_DATA = "Program_DESKTOP";
	static final int DESKTOP_UNKNOWN = 0;
	static final int DESKTOP_GNOME = 1;
	static final int DESKTOP_KDE = 2;
	static final int DESKTOP_CDE = 3;
	static final int PREFERRED_ICON_SIZE = 16;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program() {
}

/* Determine the desktop for the given display. */
static int getDesktop(Display display) {
	if (display == null) return DESKTOP_UNKNOWN;	
	Integer desktopValue = (Integer)display.getData(DESKTOP_DATA);
	if (desktopValue != null) return desktopValue.intValue();
	int desktop = DESKTOP_UNKNOWN;

	/* Get the list of properties on the root window. */
	int /*long*/ xDisplay = OS.GDK_DISPLAY();
	int /*long*/ rootWindow = OS.XDefaultRootWindow(xDisplay);
	int[] numProp = new int[1];
	int /*long*/ propList = OS.XListProperties(xDisplay, rootWindow, numProp);
	int /*long*/ [] property = new int /*long*/ [numProp[0]];
	if (propList != 0) {
		OS.memmove(property, propList, (property.length * OS.PTR_SIZEOF));
		OS.XFree(propList);
	}
	
	/* KDE is detected by checking if the the KWIN_RUNNING exists */
	if (desktop == DESKTOP_UNKNOWN) {
		byte[] kdeName = Converter.wcsToMbcs(null, "KWIN_RUNNING", true);
		int /*long*/ kde = OS.XInternAtom(xDisplay, kdeName, true);
		for (int index = 0; desktop == DESKTOP_UNKNOWN && index < property.length; index++) {
			if (property[index] == OS.None) continue;
			if (property[index] == kde && kde_init()) desktop = DESKTOP_KDE;
		}
	}
	
	/*
	 * Feature in Linux Desktop. There is currently no official way to
	 * determine whether the Gnome window manager or gnome-vfs is
	 * available. Earlier versions including Red Hat 9 and Suse 9 provide
	 * a documented Gnome specific property on the root window 
	 * WIN_SUPPORTING_WM_CHECK. This property is no longer supported in newer
	 * versions such as Fedora Core 2.
	 * The workaround is to simply check that the window manager is a 
	 * compliant one (property _NET_SUPPORTING_WM_CHECK) and to attempt to load 
	 * our native library that depends on gnome-vfs.
	 */
	if (desktop == DESKTOP_UNKNOWN) {
		byte[] gnomeName = Converter.wcsToMbcs(null, "_NET_SUPPORTING_WM_CHECK", true);
		int /*long*/ gnome = OS.XInternAtom(xDisplay, gnomeName, true);
		if (gnome != OS.None && gnome_init()) {
			desktop = DESKTOP_GNOME;
		}
	}

	display.setData(DESKTOP_DATA, new Integer(desktop));
	return desktop;
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
				} else {
					/* Add the argument, trimming off the quotes. */
					args.addElement(cmd.substring(sIndex + 1, eIndex));
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
	
	String[] strings = new String[args.size()];
	for (int index =0; index < args.size(); index++) {
		strings[index] = (String)args.elementAt(index);
	}
	return strings;
}

/**
 * GNOME - Execute the program for the given file. 
 */
boolean gnome_execute(String fileName) {
	if (gnomeExpectUri) {
		/* Convert the given path into a URL */
		fileName = "file://" + fileName;
	}

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
 * GNOME - Get Image Data
 * 
 */ 
ImageData gnome_getImageData() {
	if (iconPath == null) return null;
	try {
		return new ImageData(iconPath);
	} catch (Exception e) {}
	return null;
}

/**
 * GNOME - Get mime types
 * 
 * Obtain the registered mime type information and
 * return it in a map. The key of each entry
 * in the map is the mime type name. The value is
 * a vector of the associated file extensions.
 */
static Hashtable gnome_getMimeInfo() {
	Hashtable mimeInfo = new Hashtable();
	int /*long*/[] mimeData = new int /*long*/[1];
	int /*long*/[] extensionData = new int /*long*/[1];
	int /*long*/ mimeList = GNOME.gnome_vfs_get_registered_mime_types();
	int /*long*/ mimeElement = mimeList;
	while (mimeElement != 0) {
		OS.memmove (mimeData, mimeElement, OS.PTR_SIZEOF);
		int /*long*/ mimePtr = mimeData[0];
		int mimeLength = OS.strlen(mimePtr);
		byte[] mimeTypeBuffer = new byte[mimeLength];
		OS.memmove(mimeTypeBuffer, mimePtr, mimeLength);
		String mimeType = new String(Converter.mbcsToWcs(null, mimeTypeBuffer));
		int /*long*/ extensionList = GNOME.gnome_vfs_mime_get_extensions_list(mimePtr);
		if (extensionList != 0) {
			Vector extensions = new Vector();
			int /*long*/ extensionElement = extensionList;
			while (extensionElement != 0) {
				OS.memmove(extensionData, extensionElement, OS.PTR_SIZEOF);
				int /*long*/ extensionPtr = extensionData[0];
				int extensionLength = OS.strlen(extensionPtr);
				byte[] extensionBuffer = new byte[extensionLength];
				OS.memmove(extensionBuffer, extensionPtr, extensionLength);
				String extension = new String(Converter.mbcsToWcs(null, extensionBuffer));
				extension = '.' + extension;
				extensions.addElement(extension);
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

static Program gnome_getProgram(Display display, String mimeType) {
	Program program = null;
	byte[] mimeTypeBuffer = Converter.wcsToMbcs(null, mimeType, true);
	int /*long*/ ptr = GNOME.gnome_vfs_mime_get_default_application(mimeTypeBuffer);
	if (ptr != 0) {
		program = new Program();
		program.display = display;
		program.name = mimeType;
		GnomeVFSMimeApplication application = new GnomeVFSMimeApplication();
		GNOME.memmove(application, ptr, GnomeVFSMimeApplication.sizeof);
		int length = OS.strlen(application.command);
		byte[] buffer = new byte[length];
		OS.memmove(buffer, application.command, length);		
		program.command = new String(Converter.mbcsToWcs(null, buffer));
		program.gnomeExpectUri = application.expects_uris == GNOME.GNOME_VFS_MIME_APPLICATION_ARGUMENT_TYPE_URIS;
		
		length = OS.strlen(application.id);
		buffer = new byte[length + 1];
		OS.memmove(buffer, application.id, length);
		/* 
		* Note.  gnome_icon_theme_new uses g_object_new to allocate the data it returns.
		* Use g_object_unref to free the pointer it returns.
		*/
		int /*long*/ icon_theme = GNOME.gnome_icon_theme_new();
		int /*long*/ icon_name = GNOME.gnome_icon_lookup(icon_theme, 0, null, buffer, 0, mimeTypeBuffer, 
				GNOME.GNOME_ICON_LOOKUP_FLAGS_NONE, null);
		int /*long*/ path = 0;
		if (icon_name != 0) path = GNOME.gnome_icon_theme_lookup_icon(icon_theme, icon_name, PREFERRED_ICON_SIZE, null, null);
		GNOME.g_object_unref(icon_theme);
		if (path != 0) {
			length = OS.strlen(path);
			if (length > 0) {
				buffer = new byte[length];
				OS.memmove(buffer, path, length);
				program.iconPath = new String(Converter.mbcsToWcs(null, buffer));
			}
			GNOME.g_free(path);
		}
		if (icon_name != 0) GNOME.g_free(icon_name);
		GNOME.gnome_vfs_mime_application_free(ptr);
	}
	return program;
}

static boolean gnome_init() {
	try {
		return GNOME.gnome_vfs_init();
	} catch (Throwable e) {
		return false;
	}
}

static String kde_convertQStringAndFree(int /*long*/ qString) {
	if (qString == 0) return null;
	int /*long*/ qCString = KDE.QString_utf8(qString);
	int /*long*/ charString = KDE.QCString_data(qCString);
	
	int length = OS.strlen(charString);
	byte[] buffer = new byte[length];
	OS.memmove(buffer, charString, length);
	/* Use the character encoding for the default locale */
	String answer = new String(Converter.mbcsToWcs(null, buffer));
		
	KDE.QCString_delete(qCString);
	KDE.QString_delete(qString);
	return answer;
}

static boolean kde_init() {
	try {
		Library.loadLibrary("swt-kde");
	} catch (Throwable e) {
		return false;
	}

	/* Use the character encoding for the default locale */
	byte[] nameBuffer = Converter.wcsToMbcs(null, "SWT", true);
	int /*long*/ qcString = KDE.QCString_new(nameBuffer);
	int /*long*/ ptr = KDE.malloc(nameBuffer.length);
	OS.memmove(ptr, nameBuffer, nameBuffer.length);
	int /*long*/ [] argv = new int /*long*/ []{ptr, 0};
	KDE.KApplication_new(1, argv, qcString, false, false);
	KDE.free(ptr);
	KDE.QCString_delete(qcString);
	return true;
}

boolean kde_execute(String fileName) {
	String urlString = "file://" + fileName;
	/* Use the character encoding for the default locale */
	byte[] buffer = Converter.wcsToMbcs(null, urlString, true);
	int /*long*/ qString = KDE.QString_new(buffer);
	int /*long*/ url = KDE.KURL_new(qString);
	KDE.QString_delete(qString);
	/* Use the character encoding for the default locale */
	buffer = Converter.wcsToMbcs(null, name, true);
	int /*long*/ mimeTypeName = KDE.QString_new(buffer);
	int pid = KDE.KRun_runURL(url, mimeTypeName);
	KDE.KURL_delete(url);
	KDE.QString_delete(mimeTypeName);
	return pid != 0;
}

ImageData kde_getImageData() {
	if (iconPath == null) return null;
	try {
		return new ImageData(iconPath);
	} catch (Exception e) {}
	return null;
}

static Hashtable kde_getMimeInfo() {
	Hashtable mimeInfo = new Hashtable();
	Vector mimeExts = null;
	String mimeType;
	
	/* Get the list of all mime types available. */
	int /*long*/ mimeTypeList = KDE.KMimeType_allMimeTypes();
	int /*long*/ iterator = KDE.KMimeTypeList_begin(mimeTypeList);
	int /*long*/ listEnd = KDE.KMimeTypeList_end(mimeTypeList);
	while (!KDE.KMimeTypeListIterator_equals(iterator, listEnd)) {
		int /*long*/ kMimeType = KDE.KMimeTypeListIterator_dereference(iterator);
		int /*long*/ mimeName = KDE.KMimeType_name(kMimeType);
		mimeType = kde_convertQStringAndFree(mimeName);
		
		/* Get the list of extension patterns. */
		mimeExts = new Vector();
		String extension;
		
		/* Add the mime type to the hash table with its extensions. */
		int /*long*/ patternList = KDE.KMimeType_patterns(kMimeType);
		int /*long*/ patIterator = KDE.QStringList_begin(patternList);
		int /*long*/ patListEnd  = KDE.QStringList_end(patternList);
		while (!KDE.QStringListIterator_equals(patIterator, patListEnd)) {
			/* Get the next extension pattern from the list. */
			int /*long*/ patString = KDE.QStringListIterator_dereference(patIterator);
			extension = kde_convertQStringAndFree(patString);
			int period = extension.indexOf('.');
			if (period != -1) mimeExts.addElement(extension.substring(period));

			/* Advance to the next pattern. */		
			KDE.QStringListIterator_increment(patIterator);
		}
		KDE.QStringListIterator_delete(patIterator);
		KDE.QStringListIterator_delete(patListEnd);
		KDE.QStringList_delete(patternList);
		
		/* If there is at least one extension, save the mime type. */
		if (mimeExts.size() > 0) mimeInfo.put(mimeType, mimeExts);

		/* Advance to the next mime type. */		
		KDE.KMimeTypeListIterator_increment(iterator);
	}
	KDE.KMimeTypeListIterator_delete(iterator);
	KDE.KMimeTypeListIterator_delete(listEnd);
	KDE.KMimeTypeList_delete(mimeTypeList);
	return mimeInfo;
}

static Program kde_getProgram(Display display, String mimeType) {
	Program program = null;
	/* Use the character encoding for the default locale */
	byte[] buffer = Converter.wcsToMbcs(null, mimeType, true);
	int /*long*/ mimeTypeName = KDE.QString_new(buffer);
	int /*long*/ serviceList = KDE.KMimeType_offers(mimeTypeName);
	if (serviceList != 0) {
		KDE.KServiceList_delete(serviceList);
		program = new Program();
		program.display = display;
		program.name = mimeType;
		program.command = "KRun::runURL(url,mimeType)";
		int /*long*/ kMimeType = KDE.KMimeType_mimeType(mimeTypeName);
		if (kMimeType != 0) {
			int /*long*/ mimeIcon = KDE.KMimeType_icon(kMimeType, 0, false);
			int /*long*/ loader = KDE.KGlobal_iconLoader();
			int /*long*/ path = KDE.KIconLoader_iconPath(loader, mimeIcon, KDE.KICON_SMALL, true);
			program.iconPath = kde_convertQStringAndFree(path);
			KDE.QString_delete(mimeIcon);
			KDE.KMimeType_delete(kMimeType);
		}
		
	}
	KDE.QString_delete(mimeTypeName);
	return program;
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
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static Program findProgram(String extension) {
	return findProgram(Display.getCurrent(), extension);
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
static Program findProgram(Display display, String extension) {
	if (extension == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (extension.length() == 0) return null;
	if (extension.charAt(0) != '.') extension = "." + extension;
	int desktop = getDesktop(display);
	Hashtable mimeInfo = null;
	switch (desktop) {
		case DESKTOP_GNOME: mimeInfo = gnome_getMimeInfo(); break;
		case DESKTOP_KDE: mimeInfo = kde_getMimeInfo(); break;
	}
	if (mimeInfo == null) return null;
	String mimeType = null;
	Enumeration keys = mimeInfo.keys();
	while (mimeType == null && keys.hasMoreElements()) {
		String type = (String)keys.nextElement();
		Vector mimeExts = (Vector)mimeInfo.get(type);
		for (int index = 0; index < mimeExts.size(); index++){
			if (extension.equals(mimeExts.elementAt(index))) {
				mimeType = type;
				break;
			}
		}
	}
	if (mimeType == null) return null;
	Program program = null;
	switch (desktop) {
		case DESKTOP_GNOME: program = gnome_getProgram(display, mimeType); break;
		case DESKTOP_KDE: program = kde_getProgram(display, mimeType); break;
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
public static String[] getExtensions() {
	return getExtensions(Display.getCurrent());
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
static String[] getExtensions(Display display) {
	int desktop = getDesktop(display);
	Hashtable mimeInfo = null;
	switch (desktop) {
		case DESKTOP_GNOME: mimeInfo = gnome_getMimeInfo(); break;
		case DESKTOP_KDE: mimeInfo = kde_getMimeInfo(); break;
	}
	if (mimeInfo == null) return new String[0];

	/* Create a unique set of the file extensions. */
	Vector extensions = new Vector();
	Enumeration keys = mimeInfo.keys();
	while (keys.hasMoreElements()) {
		String mimeType = (String)keys.nextElement();
		Vector mimeExts = (Vector)mimeInfo.get(mimeType);
		for (int index = 0; index < mimeExts.size(); index++){
			if (!extensions.contains(mimeExts.elementAt(index))) {
				extensions.addElement(mimeExts.elementAt(index));
			}
		}
	}
			
	/* Return the list of extensions. */
	String[] extStrings = new String[extensions.size()];
	for (int index = 0; index < extensions.size(); index++) {
		extStrings[index] = (String)extensions.elementAt(index);
	}			
	return extStrings;
}

/**
 * Answers all available programs in the operating system.  Note
 * that a <code>Display</code> must already exist to guarantee
 * that this method returns an appropriate result.
 *
 * @return an array of programs
 */
public static Program[] getPrograms() {
	return getPrograms(Display.getCurrent());
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
static Program[] getPrograms(Display display) {
	int desktop = getDesktop(display);
	Hashtable mimeInfo = null;
	switch (desktop) {
		case DESKTOP_GNOME: mimeInfo = gnome_getMimeInfo(); break;
		case DESKTOP_KDE: mimeInfo = kde_getMimeInfo(); break;
	}
	if (mimeInfo == null) return new Program[0];
	Vector programs = new Vector();
	Enumeration keys = mimeInfo.keys();
	while (keys.hasMoreElements()) {
		String mimeType = (String)keys.nextElement();
		Program program = null;
		switch (desktop) {
			case DESKTOP_GNOME: program = gnome_getProgram(display, mimeType); break;
			case DESKTOP_KDE: program = kde_getProgram(display, mimeType); break;
		}
		if (program != null) programs.addElement(program);
	}
	Program[] programList = new Program[programs.size()];
	for (int index = 0; index < programList.length; index++) {
		programList[index] = (Program)programs.elementAt(index);
	}
	return programList;
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
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when fileName is null</li>
 *	</ul>
 */
public static boolean launch(String fileName) {
	return launch(Display.getCurrent(), fileName);
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
static boolean launch(Display display, String fileName) {
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	/* If the argument appears to be a data file (it has an extension) */
	int index = fileName.lastIndexOf('.');
	if (index > 0) {
		/* Find the associated program, if one is defined. */
		String extension = fileName.substring(index);
		Program program = Program.findProgram(display, extension); 
		
		/* If the associated program is defined and can be executed, return. */
		if (program != null && program.execute(fileName)) return true;
	}
	
	/* Otherwise, the argument was the program itself. */
	try {
		Compatibility.exec(fileName);
		return true;
	} catch (IOException e) {
		return false;
	}
}

/**
 * Returns true if the receiver and the argument represent
 * the same program.
 * 
 * @return true if the programs are the same
 */
public boolean equals(Object other) {
	if (this == other) return true;
	if (!(other instanceof Program)) return false;
	Program program = (Program)other;
	return display == program.display && name.equals(program.name) && command.equals(program.command);
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
public boolean execute(String fileName) {
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int desktop = getDesktop(display);
	switch (desktop) {
		case DESKTOP_GNOME: return gnome_execute(fileName);
		case DESKTOP_KDE: return kde_execute(fileName);
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
public ImageData getImageData() {
	switch (getDesktop(display)) {
		case DESKTOP_GNOME: return gnome_getImageData();
		case DESKTOP_KDE: return kde_getImageData();
	}
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
public String getName() {
	return name;
}

/**
 * Returns a hash code suitable for this object.
 * 
 * @return a hash code
 */
public int hashCode() {
	return name.hashCode() ^ command.hashCode() ^ display.hashCode();
}

public String toString() {
	return "Program {" + name + "}";
}
}

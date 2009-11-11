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


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gnome.*;
import org.eclipse.swt.internal.cde.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.util.*;

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
	String iconPath;
	Display display;

	/* Gnome & GIO specific
	 * true if command expects a URI
	 * false if expects a path
	 */
	boolean gnomeExpectUri;
	
	static int modTime;
	static Hashtable mimeTable;
	
	static int /*long*/ cdeShell;

	static final String[] CDE_ICON_EXT = { ".m.pm",   ".l.pm",   ".s.pm",   ".t.pm" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	static final String[] CDE_MASK_EXT = { ".m_m.bm", ".l_m.bm", ".s_m.bm", ".t_m.bm" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	static final String DESKTOP_DATA = "Program_DESKTOP"; //$NON-NLS-1$
	static final String ICON_THEME_DATA = "Program_GNOME_ICON_THEME"; //$NON-NLS-1$
	static final String PREFIX_HTTP = "http://"; //$NON-NLS-1$
	static final String PREFIX_HTTPS = "https://"; //$NON-NLS-1$
	static final int DESKTOP_UNKNOWN = 0;
	static final int DESKTOP_GNOME = 1;
	static final int DESKTOP_GNOME_24 = 2;
	static final int DESKTOP_GIO = 3;
	static final int DESKTOP_CDE = 4;
	static final int PREFERRED_ICON_SIZE = 16;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program() {
}

/* Determine the desktop for the given display. */
static int getDesktop(final Display display) {
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
		if (gnome != OS.None && (OS.GTK_VERSION >= OS.VERSION (2, 2, 0)) && gnome_init()) {
			desktop = DESKTOP_GNOME;
			int /*long*/ icon_theme = GNOME.gnome_icon_theme_new();
			display.setData(ICON_THEME_DATA, new LONG(icon_theme));
			display.addListener(SWT.Dispose, new Listener() {
				public void handleEvent(Event event) {
					LONG gnomeIconTheme = (LONG)display.getData(ICON_THEME_DATA);
					if (gnomeIconTheme == null) return;
					display.setData(ICON_THEME_DATA, null);
					/* 
					 * Note.  gnome_icon_theme_new uses g_object_new to allocate the
					 * data it returns. Use g_object_unref to free the pointer it returns.
					 */
					if (gnomeIconTheme.value != 0) OS.g_object_unref(gnomeIconTheme.value);
				}
			});
			/* Check for the existence of libgio libraries */
			byte[] buffer = Converter.wcsToMbcs(null, "libgio-2.0.so.0", true);
			int /*long*/ libgio = OS.dlopen(buffer, OS.RTLD_LAZY);
			if (libgio != 0) {
				buffer = Converter.wcsToMbcs(null, "g_app_info_launch_default_for_uri", true);
				int /*long*/ g_app_info_launch_default_for_uri = OS.dlsym(libgio, buffer);
				if (g_app_info_launch_default_for_uri != 0) {
					desktop = DESKTOP_GIO;
				}
				OS.dlclose(libgio);
			} else {
				/* Check for libgnomevfs-2 version 2.4 */
				buffer = Converter.wcsToMbcs(null, "libgnomevfs-2.so.0", true);
				int /*long*/ libgnomevfs = OS.dlopen(buffer, OS.RTLD_LAZY);
				if (libgnomevfs != 0) {
					buffer = Converter.wcsToMbcs(null, "gnome_vfs_url_show", true);
					int /*long*/ gnome_vfs_url_show = OS.dlsym(libgnomevfs, buffer);
					if (gnome_vfs_url_show != 0) {
						desktop = DESKTOP_GNOME_24;
					}
					OS.dlclose(libgnomevfs);
				}
			}
		}
	}

	/*
	* On CDE, the atom below may exist without DTWM running. If the atom 
	* below is defined, the CDE database exists and the available
	* applications can be queried.
	*/
	if (desktop == DESKTOP_UNKNOWN) {
		byte[] cdeName = Converter.wcsToMbcs(null, "_DT_SM_PREFERENCES", true);
		int /*long*/ cde = OS.XInternAtom(xDisplay, cdeName, true);
		for (int index = 0; desktop == DESKTOP_UNKNOWN && index < property.length; index++) {
			if (property[index] == OS.None) continue; /* do not match atoms that do not exist */
			if (property[index] == cde && cde_init(display)) desktop = DESKTOP_CDE;
		}
	}

	display.setData(DESKTOP_DATA, new Integer(desktop));
	return desktop;
}

boolean cde_execute(String fileName) {
	/* Use the character encoding for the default locale */
	byte[] action = Converter.wcsToMbcs(null, command, true);
	byte[] fileArg = Converter.wcsToMbcs(null, fileName, true);
	int /*long*/ ptr = OS.g_malloc(fileArg.length);
	OS.memmove(ptr, fileArg, fileArg.length);
	DtActionArg args = new DtActionArg();
	args.argClass = CDE.DtACTION_FILE;
	args.name = ptr;
	long actionID = CDE.DtActionInvoke(cdeShell, action, args, 1, null, null, null, 1, 0, 0);
	OS.g_free(ptr);
	return actionID != 0;
}

static String cde_getAction(String dataType) {
	String action  = null;
	String actions = cde_getAttribute(dataType, CDE.DtDTS_DA_ACTION_LIST);
	if (actions != null) {
		int index = actions.indexOf("Open");
		if (index != -1) {
			action = actions.substring(index, index + 4);
		} else {
			index = actions.indexOf(",");
			action = index != -1 ? actions.substring(0, index) : actions;
		}
	}
	return action;
}

static String cde_getAttribute(String dataType, String attrName) {
	/* Use the character encoding for the default locale */
	byte[] dataTypeBuf = Converter.wcsToMbcs(null, dataType, true);
	byte[] attrNameBuf = Converter.wcsToMbcs(null, attrName, true);
	byte[] optNameBuf = null;
	int /*long*/ attrValue = CDE.DtDtsDataTypeToAttributeValue(dataTypeBuf, attrNameBuf, optNameBuf);
	if (attrValue == 0) return null;
	int length = OS.strlen(attrValue);
	byte[] attrValueBuf = new byte[length];
	OS.memmove(attrValueBuf, attrValue, length);
	CDE.DtDtsFreeAttributeValue(attrValue);
	/* Use the character encoding for the default locale */
	return new String(Converter.mbcsToWcs(null, attrValueBuf));
}

static Hashtable cde_getDataTypeInfo() {
	Hashtable dataTypeInfo = new Hashtable();
	int index;
	int /*long*/ dataTypeList = CDE.DtDtsDataTypeNames();
	if (dataTypeList != 0) {
		/* For each data type name in the list */
		index = 0; 
		int /*long*/ [] dataType = new int /*long*/ [1];
		OS.memmove(dataType, dataTypeList + (index++ * 4), 4);
		while (dataType[0] != 0) {
			int length = OS.strlen(dataType[0]);
			byte[] dataTypeBuf = new byte[length];
			OS.memmove(dataTypeBuf, dataType[0], length);
			/* Use the character encoding for the default locale */
			String dataTypeName = new String(Converter.mbcsToWcs(null, dataTypeBuf));
     		
			/* The data type is valid if it is not an action, and it has an extension and an action. */
			String extension = cde_getExtension(dataTypeName);
			if (!CDE.DtDtsDataTypeIsAction(dataTypeBuf) &&
				extension != null && cde_getAction(dataTypeName) != null) {
				Vector exts = new Vector();
				exts.addElement(extension);
				dataTypeInfo.put(dataTypeName, exts);
			}
			OS.memmove(dataType, dataTypeList + (index++ * 4), 4);
		}
		CDE.DtDtsFreeDataTypeNames(dataTypeList);
	}
	
	return dataTypeInfo;
}

static String cde_getExtension(String dataType) {
	String fileExt = cde_getAttribute(dataType, CDE.DtDTS_DA_NAME_TEMPLATE);
	if (fileExt == null || fileExt.indexOf("%s.") == -1) return null;
	int dot = fileExt.indexOf(".");
	return fileExt.substring(dot);
}

/**
 * CDE - Get Image Data
 * 
 * This method returns the image data of the icon associated with
 * the data type. Since CDE supports multiple sizes of icons, several
 * attempts are made to locate an icon of the desired size and format.
 * CDE supports the sizes: tiny, small, medium and large. The best
 * search order is medium, large, small and then tiny. Althoug CDE supports
 * colour and monochrome bitmaps, only colour icons are tried. (The order is
 * defined by the  cdeIconExt and cdeMaskExt arrays above.)
 */
ImageData cde_getImageData() {
	// TODO
	return null;	
}

static String cde_getMimeType(String extension) {
	String mimeType = null;
	Hashtable mimeInfo = cde_getDataTypeInfo();
	if (mimeInfo == null) return null;
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
	return mimeType;
}

static Program cde_getProgram(Display display, String mimeType) {
	Program program = new Program();
	program.display = display;
	program.name = mimeType;
	program.command = cde_getAction(mimeType);
	program.iconPath = cde_getAttribute(program.name, CDE.DtDTS_DA_ICON);
	return program;
}

static boolean cde_init(Display display) {
	try {
		Library.loadLibrary("swt-cde");
	} catch (Throwable e) {
		return false;
	}

	/* Use the character encoding for the default locale */
	CDE.XtToolkitInitialize();
	int /*long*/ xtContext = CDE.XtCreateApplicationContext ();
	int /*long*/ xDisplay = OS.GDK_DISPLAY();
	byte[] appName = Converter.wcsToMbcs(null, "CDE", true);
	byte[] appClass = Converter.wcsToMbcs(null, "CDE", true);
	int /*long*/ [] argc = new int /*long*/ [] {0};
	CDE.XtDisplayInitialize(xtContext, xDisplay, appName, appClass, 0, 0, argc, 0);
	int /*long*/ widgetClass = CDE.topLevelShellWidgetClass ();
	cdeShell = CDE.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	CDE.XtSetMappedWhenManaged (cdeShell, false);
	CDE.XtResizeWidget (cdeShell, 10, 10, 0);
	CDE.XtRealizeWidget (cdeShell);
	boolean initOK = CDE.DtAppInitialize(xtContext, xDisplay, cdeShell, appName, appName);
	if (initOK) CDE.DtDbLoad();
	return initOK;
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
 * GNOME 2.4 - Execute the program for the given file. 
 */
boolean gnome_24_execute(String fileName) {
	byte[] mimeTypeBuffer = Converter.wcsToMbcs(null, name, true);
	int /*long*/ ptr = GNOME.gnome_vfs_mime_get_default_application(mimeTypeBuffer);
	byte[] fileNameBuffer = Converter.wcsToMbcs(null, fileName, true);
	int /*long*/ uri = GNOME.gnome_vfs_make_uri_from_input_with_dirs(fileNameBuffer, GNOME.GNOME_VFS_MAKE_URI_DIR_CURRENT);
	int /*long*/ list = GNOME.g_list_append(0, uri);
	int result = GNOME.gnome_vfs_mime_application_launch(ptr, list);
	GNOME.gnome_vfs_mime_application_free(ptr);
	GNOME.g_free(uri);
	GNOME.g_list_free(list);
	return result == GNOME.GNOME_VFS_OK;
}

/**
 * GNOME 2.4 - Launch the default program for the given file. 
 */
static boolean gnome_24_launch(String fileName) {
	byte[] fileNameBuffer = Converter.wcsToMbcs(null, fileName, true);
	int /*long*/ uri = GNOME.gnome_vfs_make_uri_from_input_with_dirs(fileNameBuffer, GNOME.GNOME_VFS_MAKE_URI_DIR_CURRENT);
	int result = GNOME.gnome_vfs_url_show(uri);
	GNOME.g_free(uri);
	return (result == GNOME.GNOME_VFS_OK);
}

/**
 * GNOME 2.2 - Execute the program for the given file. 
 */
boolean gnome_execute(String fileName) {
	if (gnomeExpectUri) {
		/* Convert the given path into a URL */
		byte[] fileNameBuffer = Converter.wcsToMbcs(null, fileName, true);
		int /*long*/ uri = GNOME.gnome_vfs_make_uri_from_input(fileNameBuffer);
		if (uri != 0) {
			int length = OS.strlen(uri);
			if (length > 0) {
				byte[] buffer = new byte[length];
				OS.memmove(buffer, uri, length);
				fileName = new String(Converter.mbcsToWcs(null, buffer));
			}
			GNOME.g_free(uri);
		}
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

static String gnome_getMimeType(String extension) {
	String mimeType = null;
	String fileName = "swt" + extension;
	byte[] extensionBuffer = Converter.wcsToMbcs(null, fileName, true);
	int /*long*/ typeName = GNOME.gnome_vfs_mime_type_from_name(extensionBuffer);
	if (typeName != 0) {
		int length = OS.strlen(typeName);
		if (length > 0) {
			byte [] buffer = new byte[length];
			OS.memmove(buffer, typeName, length);
			mimeType = new String(Converter.mbcsToWcs(null, buffer));
		}
	}
	return mimeType;
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
		LONG gnomeIconTheme = (LONG)display.getData(ICON_THEME_DATA);
		int /*long*/ icon_name = GNOME.gnome_icon_lookup(gnomeIconTheme.value, 0, null, buffer, 0, mimeTypeBuffer, 
				GNOME.GNOME_ICON_LOOKUP_FLAGS_NONE, null);
		int /*long*/ path = 0;
		if (icon_name != 0) path = GNOME.gnome_icon_theme_lookup_icon(gnomeIconTheme.value, icon_name, PREFERRED_ICON_SIZE, null, null);
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
	String mimeType = null;
	switch (desktop) {
		case DESKTOP_GIO: mimeType = gio_getMimeType(extension); break;
		case DESKTOP_GNOME_24:
		case DESKTOP_GNOME: mimeType = gnome_getMimeType(extension); break;
		case DESKTOP_CDE: mimeType = cde_getMimeType(extension); break;
	}
	if (mimeType == null) return null;
	Program program = null;
	switch (desktop) {
		case DESKTOP_GIO: program = gio_getProgram(display, mimeType); break;
		case DESKTOP_GNOME_24: 
		case DESKTOP_GNOME: program = gnome_getProgram(display, mimeType); break;
		case DESKTOP_CDE: program = cde_getProgram(display, mimeType); break;
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
		case DESKTOP_GIO: mimeInfo = gio_getMimeInfo(); break;
		case DESKTOP_GNOME_24: break;
		case DESKTOP_GNOME: mimeInfo = gnome_getMimeInfo(); break;
		case DESKTOP_CDE: mimeInfo = cde_getDataTypeInfo(); break;
	}
	if (mimeInfo == null) return new String[0];

	/* Create a unique set of the file extensions. */
	Vector extensions = new Vector();
	Enumeration keys = mimeInfo.keys();
	while (keys.hasMoreElements()) {
		String extension = (String)keys.nextElement();
		extensions.add(extension);
	}
			
	/* Return the list of extensions. */
	String [] extStrings = new String[extensions.size()];
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
		case DESKTOP_GIO: return gio_getPrograms(display);
		case DESKTOP_GNOME_24: break;
		case DESKTOP_GNOME: mimeInfo = gnome_getMimeInfo(); break;
		case DESKTOP_CDE: mimeInfo = cde_getDataTypeInfo(); break;
	}
	if (mimeInfo == null) return new Program[0];
	Vector programs = new Vector();
	Enumeration keys = mimeInfo.keys();
	while (keys.hasMoreElements()) {
		String mimeType = (String)keys.nextElement();
		Program program = null;
		switch (desktop) {
			case DESKTOP_GNOME: program = gnome_getProgram(display, mimeType); break;
			case DESKTOP_CDE: program = cde_getProgram(display, mimeType); break;
		}
		if (program != null) programs.addElement(program);
	}
	Program[] programList = new Program[programs.size()];
	for (int index = 0; index < programList.length; index++) {
		programList[index] = (Program)programs.elementAt(index);
	}
	return programList;
}

ImageData gio_getImageData() {
	if (iconPath == null) return null;
	ImageData data = null;
	try {
		int /*long*/ icon_theme =OS.gtk_icon_theme_get_default();
		byte[] icon = Converter.wcsToMbcs (null, iconPath, true);
		int /*long*/ gicon = OS.g_icon_new_for_string(icon, null);
		int /*long*/ gicon_info = OS.gtk_icon_theme_lookup_by_gicon (icon_theme, gicon, 16/*size*/, 0);
		int /*long*/ pixbuf = OS.gtk_icon_info_load_icon(gicon_info, null);		
		if (pixbuf != 0) {
			int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
			int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
			int height = OS.gdk_pixbuf_get_height(pixbuf);
			int width = OS.gdk_pixbuf_get_width(pixbuf);
			boolean hasAlpha = OS.gdk_pixbuf_get_has_alpha(pixbuf);
			byte[] srcData = new byte[stride * height];
			OS.memmove(srcData, pixels, srcData.length);
			OS.g_object_unref(pixbuf);
			
			if (hasAlpha) {
				PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
				data = new ImageData(width, height, 32, palette, 4, srcData);
				data.bytesPerLine = stride;
				int s = 3, a = 0;
				byte[] alphaData = new byte[width*height];
				for (int y=0; y<height; y++) {
					for (int x=0; x<width; x++) {
						alphaData[a++] = srcData[s];
						srcData[s] = 0;
						s+=4;
					}
				}
				data.alphaData = alphaData;
			} else {
				PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
				data = new ImageData(width, height, 24, palette, 4, srcData);
				data.bytesPerLine = stride;
			}
		}
	} catch (Exception e) {}
	return data;
}

static Hashtable gio_getMimeInfo() {
	int /*long*/ mimeDatabase;
	/*
	* The file 'globs' contain the file extensions  
	* associated to the mime-types. Each line that has 
	* to be parsed corresponds to a different extension 
	* of a mime-type. The template of such line is -
	* application/pdf:*.pdf
	*/
	byte[] buffer = Converter.wcsToMbcs (null, "/usr/share/mime/globs", true);
	mimeDatabase = OS.g_file_new_for_path (buffer);
	int /*long*/ fileInputStream = OS.g_file_read (mimeDatabase, 0, 0);
	if (fileInputStream == 0) {
		OS.g_object_unref (mimeDatabase);
		return null;
	}
	int /*long*/ [] modTimestamp = new int /*long*/ [2];
	buffer = Converter.wcsToMbcs (null, "*", true);
	int /*long*/ fileInfo = OS.g_file_query_info(mimeDatabase, buffer, 0, 0, 0);
	OS.g_file_info_get_modification_time(fileInfo, modTimestamp);
	if (modTime != 0 && modTimestamp[0] == modTime) {
		return mimeTable;
	} else {
		mimeTable = new Hashtable();
		modTime = modTimestamp[0];
		int /*long*/ reader = OS.g_data_input_stream_new (fileInputStream);
		int[] length = new int[1];
		
		if (reader != 0) {
			int /*long*/ linePtr = OS.g_data_input_stream_read_line (reader, length, 0, 0);
			while (linePtr != 0) {
				byte[] lineBytes = new byte[length[0]];
				OS.memmove(lineBytes, linePtr, length[0]);
				String line = new String (Converter.mbcsToWcs (null, lineBytes));
	
				int separatorIndex = line.indexOf (':');
				if (separatorIndex > 0) {
					Vector mimeTypes = new Vector ();
				    String mimeType = line.substring (0, separatorIndex);
					String extensionFormat = line.substring (separatorIndex+1);
					int extensionIndex = extensionFormat.indexOf (".");
					if (extensionIndex > 0) {
						String extension = extensionFormat.substring (extensionIndex);
						mimeTypes.add (mimeType);
						if (mimeTable.containsKey (extension)) {
							/*
							 * If mimeType already exists, it is required to update
							 * the existing key (mime-type) with the new extension. 
							 */
							Vector value = (Vector) mimeTable.get (extension);
							mimeTypes.addAll (value);
						}
						mimeTable.put (extension, mimeTypes);
					}
				}
				linePtr = OS.g_data_input_stream_read_line (reader, length, 0, 0);
			}
		}
		return mimeTable;
	}
}

static String gio_getMimeType(String extension) {
	String mimeType = null;
	Hashtable h = gio_getMimeInfo();
	if (h != null && h.containsKey(extension)) {
		Vector mimeTypes = (Vector) h.get(extension);
		mimeType = (String) mimeTypes.get(0);
	}
	return mimeType;
}

static Program gio_getProgram(Display display, String mimeType) {
	Program program = null;
	byte[] mimeTypeBuffer = Converter.wcsToMbcs (null, mimeType, true);
	int /*long*/ application = OS.g_app_info_get_default_for_type (mimeTypeBuffer, false);
	if (application != 0) {
		if (OS.g_app_info_should_show(application)) {
			program = gio_getProgram(display, application);
		}
	}
	return program;
}

static Program gio_getProgram (Display display, int /*long*/ application) {
	Program program = new Program();
	program.display = display;
	int /*long*/ applicationName = OS.g_app_info_get_name (application);
	int length = OS.strlen (applicationName);
	byte[] buffer = new byte [length];
	OS.memmove (buffer, applicationName, length);		
	program.name = new String (Converter.mbcsToWcs (null, buffer));
	int /*long*/ applicationCommand = OS.g_app_info_get_executable (application);
	length = OS.strlen (applicationName);
	buffer = new byte [length];
	OS.memmove (buffer, applicationCommand, length);		
	program.command = new String (Converter.mbcsToWcs (null, buffer));
	program.gnomeExpectUri = OS.g_app_info_supports_uris(application);
	int /*long*/ applicationId = OS.g_app_info_get_id (application);
	length = OS.strlen(applicationId);
	buffer = new byte[length + 1];
	OS.memmove(buffer, applicationId, length);
	int /*long*/ icon = OS.g_app_info_get_icon(application);
	if (icon != 0) {
		int /*long*/ icon_name = OS.g_icon_to_string(icon);
		if (icon_name != 0) {
			length = OS.strlen(icon_name);
			if (length > 0) {
				buffer = new byte[length];
				OS.memmove(buffer, icon_name, length);
				program.iconPath = new String(Converter.mbcsToWcs(null, buffer));
			}
			OS.g_free(icon_name);
		}
	}
	return program;
}

static Program[] gio_getPrograms(Display display) {
	int /*long*/ applicationList = OS.g_app_info_get_all ();
	Program program;
	Vector programs = new Vector();
	while (applicationList != 0) {
		int /*long*/ application = OS.g_list_data(applicationList);
		if (application != 0) {
			if (OS.g_app_info_should_show(application)) {
				program = gio_getProgram(display, application);
				if (program != null) programs.addElement(program);
			}
		}
		applicationList = OS.g_list_next(applicationList);
	}
	Program[] programList = new Program[programs.size()];
	for (int index = 0; index < programList.length; index++) {
		programList[index] = (Program)programs.elementAt(index);
	}
	return programList;
}

/**
 * GNOME 2.4 - Launch the default program for the given file. 
 */
static boolean gio_launch(String fileName) {
	byte[] fileNameBuffer = Converter.wcsToMbcs (null, fileName, true);
	int /*long*/ file = OS.g_file_new_for_path (fileNameBuffer);
	int /*long*/ uri = OS.g_file_get_uri (file);
	boolean result = OS.g_app_info_launch_default_for_uri (uri, 0, 0);
	
	OS.g_object_unref (file);
	OS.g_free (uri);
	return result;
}

/**
 * GIO - Execute the program for the given file. 
 */
boolean gio_execute(String fileName) {
	byte[] commandBuffer = Converter.wcsToMbcs (null, command, true);
	byte[] nameBuffer = Converter.wcsToMbcs (null, name, true);
	int /*long*/ application = OS.g_app_info_create_from_commandline(commandBuffer, nameBuffer, gnomeExpectUri ? OS.G_APP_INFO_CREATE_SUPPORTS_URIS : OS.G_APP_INFO_CREATE_NONE, 0);
	byte[] fileNameBuffer = Converter.wcsToMbcs (null, fileName, true);
	int /*long*/ file = OS.g_file_new_for_path (fileNameBuffer);
	int /*long*/ list = 0;
	list = OS.g_list_append (list, file);
	boolean result = OS.g_app_info_launch (application, list, 0, 0);

	OS.g_object_unref (file);
	OS.g_object_unref (application);
	return result;
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
public static boolean launch(String fileName) {
	return launch(Display.getCurrent(), fileName);
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
static boolean launch (Display display, String fileName) {
	if (fileName == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	switch (getDesktop (display)) {
		case DESKTOP_GIO:
			if (gio_launch (fileName)) return true;
		case DESKTOP_GNOME_24:
			if (gnome_24_launch (fileName)) return true;
		default:
			int index = fileName.lastIndexOf ('.');
			if (index != -1) {
				String extension = fileName.substring (index);
				Program program = Program.findProgram (display, extension); 
				if (program != null && program.execute (fileName)) return true;
			}
			String lowercaseName = fileName.toLowerCase ();
			if (lowercaseName.startsWith (PREFIX_HTTP) || lowercaseName.startsWith (PREFIX_HTTPS)) {
				Program program = Program.findProgram (display, ".html"); //$NON-NLS-1$
				if (program == null) {
					program = Program.findProgram (display, ".htm"); //$NON-NLS-1$
				}
				if (program != null && program.execute (fileName)) return true;
			}
			break;
	}
	try {
		Compatibility.exec (fileName);
		return true;
	} catch (IOException e) {
		return false;
	}
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when fileName is null</li>
 * </ul>
 */
public boolean execute(String fileName) {
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int desktop = getDesktop(display);
	switch (desktop) {
		case DESKTOP_GIO: return gio_execute(fileName);
		case DESKTOP_GNOME_24: return gnome_24_execute(fileName);
		case DESKTOP_GNOME: return gnome_execute(fileName);
		case DESKTOP_CDE: return cde_execute(fileName);
	}
	return false;
}

/**
 * Returns the receiver's image data.  This is the icon
 * that is associated with the receiver in the operating
 * system.
 *
 * @return the image data for the program, may be null
 */
public ImageData getImageData() {
	switch (getDesktop(display)) {
		case DESKTOP_GIO: return gio_getImageData();
		case DESKTOP_GNOME_24:
		case DESKTOP_GNOME: return gnome_getImageData();
		case DESKTOP_CDE: return cde_getImageData();
	}
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
public String getName() {
	return name;
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
	return name.hashCode() ^ command.hashCode() ^ display.hashCode();
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the program
 */
public String toString() {
	return "Program {" + name + "}";
}
}

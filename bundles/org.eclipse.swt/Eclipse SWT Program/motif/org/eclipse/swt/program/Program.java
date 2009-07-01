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
import org.eclipse.swt.internal.motif.*;
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

	/* Gnome specific
	 * true if command expects a URI
	 * false if expects a path
	 */
	boolean gnomeExpectUri;

	static final String SHELL_HANDLE_KEY = "org.eclipse.swt.internal.motif.shellHandle"; //$NON-NLS-1$
	static final String[] CDE_ICON_EXT = { ".m.pm",   ".l.pm",   ".s.pm",   ".t.pm" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	static final String[] CDE_MASK_EXT = { ".m_m.bm", ".l_m.bm", ".s_m.bm", ".t_m.bm" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	static final String DESKTOP_DATA = "Program_DESKTOP"; //$NON-NLS-1$
	static final String ICON_THEME_DATA = "Program_GNOME_ICON_THEME"; //$NON-NLS-1$
	static final String PREFIX_HTTP = "http://"; //$NON-NLS-1$
	static final String PREFIX_HTTPS = "https://"; //$NON-NLS-1$
	static final int DESKTOP_UNKNOWN = 0;
	static final int DESKTOP_GNOME = 1;
	static final int DESKTOP_GNOME_24 = 2;
	static final int DESKTOP_CDE = 3;
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
	int /*long*/ xDisplay = display.xDisplay;
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
		if (gnome != OS.None && gnome_init()) {
			desktop = DESKTOP_GNOME;
			int /*long*/ icon_theme = GNOME.gnome_icon_theme_new();
			display.setData(ICON_THEME_DATA, new Integer(icon_theme));
			display.addListener(SWT.Dispose, new Listener() {
				public void handleEvent(Event event) {
					Integer gnomeIconTheme = (Integer)display.getData(ICON_THEME_DATA);
					if (gnomeIconTheme == null) return;
					display.setData(ICON_THEME_DATA, null);
					int iconThemeValue = gnomeIconTheme.intValue();
					/* 
					 * Note.  gnome_icon_theme_new uses g_object_new to allocate the
					 * data it returns. Use g_object_unref to free the pointer it returns.
					 */
					if (iconThemeValue != 0) GNOME.g_object_unref(iconThemeValue);
				}
			});
			/* Check for libgnomevfs-2 version 2.4 */
			byte[] buffer = Converter.wcsToMbcs(null, "libgnomevfs-2.so.0", true);
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
	Integer shell = (Integer)display.getData(SHELL_HANDLE_KEY);
	long actionID = 0;
	if (shell != null) {
		int ptr = OS.XtMalloc(fileArg.length);
		OS.memmove(ptr, fileArg, fileArg.length);
		DtActionArg args = new DtActionArg();
		args.argClass = CDE.DtACTION_FILE;
		args.name = ptr;
		actionID = CDE.DtActionInvoke(shell.intValue(), action, args, 1, null, null, null, 1, 0, 0);
		OS.XtFree(ptr);
	}
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
	if (iconPath == null) return null;
	int xDisplay = display.xDisplay;
	int screen  = OS.XDefaultScreenOfDisplay(xDisplay);
	int fgPixel = OS.XWhitePixel(display.xDisplay, OS.XDefaultScreen(xDisplay));
	int bgPixel = OS.XBlackPixel(display.xDisplay, OS.XDefaultScreen(xDisplay));	
	byte[] iconName;
	byte[] maskName = null;
	int pixmap = 0;
	for (int index = 0; index < CDE_ICON_EXT.length && pixmap == 0; index++) {
		/* Use the character encoding for the default locale */
		iconName = Converter.wcsToMbcs(null, iconPath + CDE_ICON_EXT[index], true);
		maskName = Converter.wcsToMbcs(null, iconPath + CDE_MASK_EXT[index], true);
		pixmap = OS.XmGetPixmap(screen, iconName, fgPixel, bgPixel);
		if (pixmap == OS.XmUNSPECIFIED_PIXMAP) pixmap = 0;
	}
	if (pixmap != 0) {
		int type = SWT.ICON;
		/* When creating the mask pixmap, do not use the screen's white and black
		 * pixel for the foreground and background respectively, because on some
		 * X servers (e.g., Solaris) pixel 0 is white and pixel 1 is black. Passing
		 * (screen, name, whitePixel, blackPixel, 1) to get the mask pixmap will
		 * result in an inverted mask. Instead explicitly use 1 (FG) and 0 (BG).
		 */
		int mask = OS.XmGetPixmapByDepth(screen, maskName, 1, 0, 1);
		if (mask == OS.XmUNSPECIFIED_PIXMAP) {
			type = SWT.BITMAP;
			mask = 0;
		}
		Image image = Image.motif_new(display, type, pixmap, mask);
		ImageData imageData = image.getImageData();
		
		/* The pixmaps returned from XmGetPixmap... are cached by Motif
		 * and must be deleted by XmDestroyPixmap. Because they cannot
		 * be deleted directly by XFreePixmap, image.dispose() must not
		 * be called. The following code should do an equivalent image.dispose().
		 */
		OS.XmDestroyPixmap(screen, pixmap);
		if (mask != 0) OS.XmDestroyPixmap(screen, mask); 
		return imageData;		
	}
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
	byte[] appName = Converter.wcsToMbcs(null, "SWT", true);
	int xtContext = OS.XtDisplayToApplicationContext(display.xDisplay);
	Integer shell = (Integer)display.getData(SHELL_HANDLE_KEY);
	boolean initOK = CDE.DtAppInitialize(xtContext, display.xDisplay, shell.intValue(), appName, appName);
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
		Integer gnomeIconTheme = (Integer)display.getData(ICON_THEME_DATA);
		int iconThemeValue = gnomeIconTheme.intValue();
		int /*long*/ icon_name = GNOME.gnome_icon_lookup(iconThemeValue, 0, null, buffer, 0, mimeTypeBuffer, 
				GNOME.GNOME_ICON_LOOKUP_FLAGS_NONE, null);
		int /*long*/ path = 0;
		if (icon_name != 0) path = GNOME.gnome_icon_theme_lookup_icon(iconThemeValue, icon_name, PREFERRED_ICON_SIZE, null, null);
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
		case DESKTOP_GNOME_24:
		case DESKTOP_GNOME: mimeType = gnome_getMimeType(extension); break;
		case DESKTOP_CDE: mimeType = cde_getMimeType(extension); break;
	}
	if (mimeType == null) return null;
	Program program = null;
	switch (desktop) {
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
		case DESKTOP_GNOME_24: break;
		case DESKTOP_GNOME: mimeInfo = gnome_getMimeInfo(); break;
		case DESKTOP_CDE: mimeInfo = cde_getDataTypeInfo(); break;
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

package org.eclipse.swt.program;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
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
	Display display;

	static private final String   cdeShell = "Program_CDE_SHELL";  // hidden shell used for DtAppInitialize and DtActionInvoke
	static private final String[] cdeIconExt = { ".m.pm",   ".l.pm",   ".s.pm",   ".t.pm" };
	static private final String[] cdeMaskExt = { ".m_m.bm", ".l_m.bm", ".s_m.bm", ".t_m.bm" };
	static private final String   desktopData = "Program_DESKTOP";
		
	static final int DESKTOP_UNKNOWN = 0;
	static final int DESKTOP_KDE = 1;		// Linux
	static final int DESKTOP_GNOME = 2;		// Linux
    static final int DESKTOP_CDE = 3;		// Solaris
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program () {
}

/* Determine the desktop for the given display. */
static int getDesktop( Display display ) {
	if (display == null) return DESKTOP_UNKNOWN;
	
	// If the desktop type for this display is already known, return it.
	Integer desktopValue = (Integer) display.getData( desktopData );
	if (desktopValue != null) {
		return desktopValue.intValue();
	}
	
	// Obtain the atoms for the various window manager signature properties.
	// On CDE, the atom below may exist without DTWM running. If the atom 
	// below is defined, the CDE database exists and the available
	// applications can be queried.
	int desktop = DESKTOP_UNKNOWN;
	int xDisplay = display.xDisplay;
	/* Use the character encoding for the default locale */
	byte[] gnomeName = Converter.wcsToMbcs (null, "GNOME_NAME_SERVER", true);
	byte[] cdeName   = Converter.wcsToMbcs (null, "_DT_SM_PREFERENCES", true);
	byte[] kdeName   = Converter.wcsToMbcs (null, "KWIN_RUNNING", true);
	int gnome = OS.XInternAtom( xDisplay, gnomeName, true );
	int cde   = OS.XInternAtom( xDisplay, cdeName, true );
	int kde   = OS.XInternAtom( xDisplay, kdeName, true );
	
	// Get the list of properties on the root window.
	int   rootWindow = OS.XDefaultRootWindow( xDisplay );
	int[] numProp = new int[1];
	int   propList = OS.XListProperties( xDisplay, rootWindow, numProp );
	if (propList == 0) return DESKTOP_UNKNOWN;
	int[] property = new int[ numProp[0] ];
	OS.memmove( property, propList, (property.length * 4) );
	OS.XFree( propList );
	
	// A given WM (desktop) is active if the property exists on the root window.
	for (int index = 0; desktop == DESKTOP_UNKNOWN && index < property.length; index++) {
		if (property[ index ] == OS.None) continue; // do not match atoms that do not exist
		if (property[ index ] == gnome) {
			if (gnome_init()) desktop = DESKTOP_GNOME;		
		}
		if (property[ index ] == cde) {
			if (cde_init( display )) desktop = DESKTOP_CDE;
		}	
		if (property[ index ] == kde) {
			if (kde_init()) desktop = DESKTOP_KDE;
		}	
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
 * @return the program or nil
 *
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when extension is null</li>
 *	</ul>
 */
public static Program findProgram (String extension) {
	return findProgram( Display.getCurrent(), extension );
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static Program findProgram( Display display, String extension ) {
	if (extension == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (extension.length () == 0) return null;
	if (extension.charAt (0) != '.') extension = "." + extension;
	String command = null;
	String name = null;
	int desktop = getDesktop( display );
	Hashtable mimeInfo = null;
	if (desktop == DESKTOP_KDE)   mimeInfo = kde_getMimeInfo();
	if (desktop == DESKTOP_GNOME) mimeInfo = gnome_getMimeInfo();
	if (desktop == DESKTOP_CDE)   mimeInfo = cde_getDataTypeInfo();
	if (mimeInfo == null) return null;

	// Find the data type matching the extension.
	Iterator keys = mimeInfo.keySet().iterator();
	while (name == null && keys.hasNext()) {
		String mimeType = (String) keys.next();
		Vector mimeExts = (Vector) mimeInfo.get( mimeType );
		for (int index = 0; index < mimeExts.size(); index++){
			if (extension.equals( mimeExts.elementAt( index ) )) {
				name = mimeType;
			}
		}
	}			
	if (name == null) return null;

	// Get the corresponding command for the mime type.
	if (desktop == DESKTOP_KDE)   command = kde_getMimeTypeCommand( name );
	if (desktop == DESKTOP_GNOME) command = gnome_getMimeValue( name, "open" );
	if (desktop == DESKTOP_CDE)   command = cde_getAction( name );
	if (command == null) return null;
	
	// Return the corresponding program.
	Program program   = new Program ();
	program.name      = name;
	program.command   = command;
	program.extension = extension;
	program.display   = display;
	return program;
}

/**
 * Answer all program extensions in the operating system.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	return getExtensions( Display.getCurrent() );
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static String[] getExtensions( Display display ) {
	int desktop = getDesktop( display );
	Hashtable mimeInfo = null;
	if (desktop == DESKTOP_KDE)   mimeInfo = kde_getMimeInfo();
	if (desktop == DESKTOP_GNOME) mimeInfo = gnome_getMimeInfo();
	if (desktop == DESKTOP_CDE)   mimeInfo = cde_getDataTypeInfo();
	if (mimeInfo == null) return new String[0];

			
	// Create a unique set of the file extensions.
	Vector extensions = new Vector();
	Iterator keys = mimeInfo.keySet().iterator();
	while (keys.hasNext()) {
		String mimeType = (String) keys.next();
		Vector mimeExts = (Vector) mimeInfo.get( mimeType );
		for (int index = 0; index < mimeExts.size(); index++){
			if (!extensions.contains( mimeExts.elementAt( index ) )) {
				extensions.addElement( mimeExts.elementAt( index ) );
			}
		}
	}
			
	// Return the list of extensions.
	String[] extStrings = new String[ extensions.size() ];
	for (int index = 0; index < extensions.size(); index++) {
		extStrings[ index ] = (String) extensions.elementAt( index );
	}			
	return extStrings;
}

/**
 * Answers all available programs in the operating system.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	return getPrograms( Display.getCurrent() );
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static Program[] getPrograms( Display display ) {
	int desktop = getDesktop( display );
	Hashtable mimeInfo = null;
	if (desktop == DESKTOP_KDE)   mimeInfo = kde_getMimeInfo();
	if (desktop == DESKTOP_GNOME) mimeInfo = gnome_getMimeInfo();
	if (desktop == DESKTOP_CDE)   mimeInfo = cde_getDataTypeInfo();
	if (mimeInfo == null) return new Program[0];
			
	// Create a list of programs with commands.
	Vector programs = new Vector();
	Iterator keys = mimeInfo.keySet().iterator();
	while (keys.hasNext()) {
		String mimeType  = (String) keys.next();
		Vector mimeExts  = (Vector) mimeInfo.get( mimeType );
		String extension = "";
		if (mimeExts.size() > 0){
			extension = (String) mimeExts.elementAt( 0 );
		}
		String command = null;
		if (desktop == DESKTOP_KDE)   command = kde_getMimeTypeCommand( mimeType );
		if (desktop == DESKTOP_GNOME) command = gnome_getMimeValue( mimeType, "open" );
		if (desktop == DESKTOP_CDE)   command = cde_getAction( mimeType );
		if (command != null) {
			Program program   = new Program ();
			program.name      = mimeType;
			program.command   = command;
			program.extension = extension;
			program.display   = display;
			programs.addElement( program );
		}
	}
			
	// Return the list of programs to the user.
	Program[] programList = new Program[ programs.size() ];
	for (int index = 0; index < programList.length; index++) {
		programList[ index ] = (Program) programs.elementAt( index );
	}
	return programList;
}

/*
 * Obtain the registered mime type information and
 * return it in a map. The key of each entry
 * in the map is the mime type name. The value is
 * a vector of the associated file extensions.
 */
  
private static Hashtable gnome_getMimeInfo() {
	Hashtable mimeInfo = new Hashtable();
	
	// Extract the mime info from the system directory.
	String mimeDirectory = gnome_getDataDirectory ("mime-info");
	gnome_getMimeInfoFromDirectory( mimeInfo, new File( mimeDirectory ) );
	
	// Append the mime info from the user's directory (if it exists).
	String userDirectory = gnome_getHomeDirectory();
	if (userDirectory != null) {
		userDirectory = userDirectory + File.separator + ".gnome" + File.separator + "mime-info";
		gnome_getMimeInfoFromDirectory( mimeInfo, new File( userDirectory ) );
	}

	return mimeInfo;
}

// Given a map and a directory, find all of the 
// mime information files (*.mime) and parse them for
// relavent mime type information. Each entry in the
// map corresponds to one mime type, and its
// associated file extensions.

private static void gnome_getMimeInfoFromDirectory( Hashtable info, File directory ) {
	// For each entry in the given directory (if it exists)
	if (directory.exists()) {
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
		
			// If the entry is a subdirectory, process it and
			// merge the mime type into the given map.
			if (files[i].isDirectory()) {
				gnome_getMimeInfoFromDirectory( info, files[i] );
			}
		
			// else if the entry is a mime info file (*.mime)
			else if (files[i].getName().endsWith(".mime")) {
				try {
					// Parse the mime file and merge the info
					// into the given map.
					FileReader in = new FileReader( files[i] );
					BufferedReader reader = new BufferedReader( in );
					gnome_parseMimeFile( info, reader );
					reader.close();
					in.close();
				}
				catch (IOException e) {
					// Ignore file exceptions silently. If we
					// can't read it, the info is not available.
				}
			}
		}
	}
}

private static void gnome_parseMimeFile( Hashtable info, BufferedReader reader ) {
	Vector  mimeExts = null;
	String  mimeType = null;
	boolean saveType = false;
	String  line     = "#";
	while (line != null) {
		
		// Determine if the line contains a mime type name.
		boolean newType = (line.length() > 0 && Compatibility.isLetter( line.charAt(0) ));
				  
		// If there is valid data on this line to be processed
		String data = line.trim();
		if (data.length() > 0 && data.charAt(0) != '#') {
			
			// If this line defines a new mime type
			if (newType) {
				
				// If a previous mime type has not be saved yet
				if (mimeType != null) {
					// Save the type and process this line again.
					saveType = true;
				}
				// else initialize the mime type info
				else {
					int colon = data.indexOf( ':' );
					if (colon != -1) {
						mimeType = data.substring( 0, colon );
					}
					else {
						mimeType = data;
					}
					mimeExts = new Vector();
				}
			}
			
			// else if the line defines a list of extensions
			else if (data.indexOf( "ext" ) == 0 && mimeType != null) {
				
				// Get the extensions defined on the line
				String exts = "";
				int colon = data.indexOf( ':' );
				if ((colon != -1) && ((colon+1) < data.length())) {
					exts = data.substring( (colon+1) ).trim();
				}
				
				// While there are extensions to be processed (use space as separator)
				exts = exts.replace( '\t', ' ' );
				exts = exts.replace( ',', ' ' );
				while (exts.length() != 0) {
					// Extract the next entension from the list
					String newExt;
					int  space = exts.indexOf( ' ' );
					if (space != -1) {
						newExt = exts.substring( 0, space );
						exts = exts.substring( space ).trim();
					}
					else {
						newExt = exts;
						exts = "";
					}
					
					// Prefix an extension with a period.
					if (newExt.charAt(0) != '.') {
						newExt = "." + newExt;
					}
					mimeExts.addElement( newExt );
				} 
			}
			
			// else if the line defines a list of regular expressions
			else if (data.indexOf( "regex" ) == 0 && mimeType != null) {
				// Do nothing with these right now.
			}
		}
		
		
		// If the current mime type is still being processed
		if (!saveType) {
			// Get the next line			
			try {
				line = reader.readLine();
			}
			catch (IOException e) {
				line = null;
			}
		}
		
		// If the current type should be saved or if the end
		// of the file was reached
		if (saveType || (line == null)) {
			// If there is a mime type to be saved
			if (mimeType != null) {
			
				// If the mime type does not exist in the map, add it.
				Vector prevExts = (Vector) info.get( mimeType );
				if (prevExts == null) {
					info.put( mimeType, mimeExts );
				}
		
				// else append the new list of extensions.
				else {
					for (int i = 0; i < mimeExts.size(); i++) {
						prevExts.add( mimeExts.elementAt( i ) );
					}
				}
			}
			mimeType = null;
			mimeExts = null;
			saveType = false;
		}
	}
}

// Private method for parsing a command line into its arguments.
private static String[] parseCommand( String cmd ) {
	Vector args = new Vector();
	int sIndex = 0;
	int eIndex;
	while (sIndex < cmd.length()) {
		// Trim initial white space of argument.
		while (sIndex < cmd.length() && Compatibility.isWhitespace( cmd.charAt(sIndex) )) {
			sIndex++;
		}
		if (sIndex < cmd.length()) {
			// If the command is a quoted string
			if (cmd.charAt(sIndex) == '"' || cmd.charAt(sIndex) == '\''){
				// Find the terminating quote (or end of line).
				// This code currently does not handle escaped characters (e.g., " a\"b").
				eIndex = sIndex + 1;
				while (eIndex < cmd.length() && cmd.charAt(eIndex) != cmd.charAt(sIndex)) {
					eIndex++;
				}
				if (eIndex >= cmd.length()) { // the terminating quote was not found
					// Add the argument as is with only one initial quote.
					args.addElement( cmd.substring( sIndex, eIndex ) );
				}
				// else add the argument, trimming off the quotes.
				else {
					args.addElement( cmd.substring( sIndex+1, eIndex ) );
				}
				sIndex = eIndex + 1;
			}
			
			// else use white space for the delimiters.
			else {
				eIndex = sIndex;
				while (eIndex < cmd.length() && !Compatibility.isWhitespace( cmd.charAt(eIndex) )) {
					eIndex++;
				}
				args.addElement( cmd.substring( sIndex, eIndex ) );
				sIndex = eIndex + 1;
			}
		}
	}
	
	String[] strings = new String[ args.size() ];
	for (int index =0; index < args.size(); index++) {
		strings[ index ] = (String) args.elementAt( index );
	}
	return strings;
}


static String gnome_getDataDirectory(String dirName) {
	/* Use the character encoding for the default locale */
	byte [] nameBuffer = Converter.wcsToMbcs (null, dirName, true);
	int ptr = GNOME.gnome_datadir_file(nameBuffer);
	if (ptr == 0) return null;
	int length = OS.strlen(ptr);
	byte[] dirBuffer = new byte[length];
	OS.memmove(dirBuffer, ptr, length);
	/* Use the character encoding for the default locale */
	return new String(Converter.mbcsToWcs(null, dirBuffer));
}

static String gnome_getHomeDirectory() {
	int ptr = GNOME.g_get_home_dir();
	if (ptr == 0) return null;
	int length = OS.strlen(ptr);
	byte[] homeDirBuffer = new byte[length];
	OS.memmove(homeDirBuffer, ptr, length);
	/* Use the character encoding for the default locale */
	return new String(Converter.mbcsToWcs(null, homeDirBuffer));
}

static String gnome_getMimeType(String name) {
	/* Use the character encoding for the default locale */
	byte [] nameBuffer = Converter.wcsToMbcs (null, name, true);
	int ptr = GNOME.gnome_mime_type(nameBuffer);
	if (ptr == 0) return null;
	int length = OS.strlen(ptr);
	byte[] mimeBuffer = new byte[length];
	OS.memmove(mimeBuffer, ptr, length);
	/* Use the character encoding for the default locale */
	return new String(Converter.mbcsToWcs(null, mimeBuffer));
}

static String gnome_getMimeValue(String mimeType, String key) {
	/* Use the character encoding for the default locale */
	byte [] typeBuffer = Converter.wcsToMbcs (null, mimeType, true);
	byte [] keyBuffer = Converter.wcsToMbcs (null, key, true);
	int ptr = GNOME.gnome_mime_get_value(typeBuffer, keyBuffer);
	if (ptr == 0) return null;
	
	int length = OS.strlen(ptr);
	byte[] valueBuffer = new byte[length];
	OS.memmove(valueBuffer, ptr, length);
	/* Use the character encoding for the default locale */
	return new String(Converter.mbcsToWcs(null, valueBuffer));
}

static boolean kde_init () {
	// TEMPORARY CODE
	if (true) return false;
	
	try {
		Library.loadLibrary("swt-kde");
	} catch (Throwable e) {
		return false;
	}

	/* Use the character encoding for the default locale */
	byte [] nameBuffer = Converter.wcsToMbcs( null, "SWT", true );
	int qcString = KDE.QCString_new( nameBuffer );
	KDE.KApplication_new( qcString );
	KDE.QCString_delete( qcString );
	return true;
}

private static String kde_getMimeTypeCommand( String mimeType ) {
	/* Use the character encoding for the default locale */
	byte [] buffer = Converter.wcsToMbcs (null, mimeType, true);
	int qMimeType = KDE.QString_new( buffer );
	int serviceList = KDE.KMimeType_offers( qMimeType );
	KDE.QString_delete( qMimeType );
	if (serviceList == 0) return null;
	KDE.KServiceList_delete( serviceList );
	return "KRun::runURL(url,mimeType)";
}
/*
 * Obtain the registered mime type information and
 * return it in a map. The key of each entry
 * in the map is the mime type name. The value is
 * a vector of the associated file extensions.
 */
  
private static Hashtable kde_getMimeInfo() {
	Hashtable mimeInfo = new Hashtable();
	Vector    mimeExts = null;
	String    mimeType;
	
	// Get the list of all mime types available.
	int mimeTypeList = KDE.KMimeType_allMimeTypes();
	int iterator = KDE.KMimeTypeList_begin( mimeTypeList );
	int listEnd  = KDE.KMimeTypeList_end( mimeTypeList );
	while (KDE.KMimeTypeListIterator_equals( iterator, listEnd ) == 0) {
		// Get the next KMimeType from the list.
		int kMimeType = KDE.KMimeTypeListIterator_dereference( iterator );
		
		// Get the mime type name.
		int mimeName = KDE.KMimeType_name( kMimeType );
		mimeType = kde_convertQStringAndFree( mimeName );
		
		// Get the list of extension patterns.
		mimeExts = new Vector();
		String extension;
		
		// Add the mime type to the hash table with its extensions.
		int patternList = KDE.KMimeType_patterns( kMimeType );
		int patIterator = KDE.QStringList_begin( patternList );
		int patListEnd  = KDE.QStringList_end( patternList );
		while (KDE.QStringListIterator_equals( patIterator, patListEnd ) == 0) {
			// Get the next extension pattern from the list.
			int patString = KDE.QStringListIterator_dereference( patIterator );
			extension = kde_convertQStringAndFree( patString );
			int period = extension.indexOf( '.' );
			if (period != -1) {
				mimeExts.addElement( extension.substring( period ) );
			}

			// Advance to the next pattern.		
			KDE.QStringListIterator_increment( patIterator );
		}
		KDE.QStringListIterator_delete( patIterator );
		KDE.QStringListIterator_delete( patListEnd );
		KDE.QStringList_delete( patternList );
		
		// If there is at least one extension, save the mime type.
		if (mimeExts.size() > 0) {
			mimeInfo.put( mimeType, mimeExts );
		}

		// Advance to the next mime type.		
		KDE.KMimeTypeListIterator_increment( iterator );
	}
	KDE.KMimeTypeListIterator_delete( iterator );
	KDE.KMimeTypeListIterator_delete( listEnd );
	KDE.KMimeTypeList_delete( mimeTypeList );
	
	return mimeInfo;
}

static String kde_convertQStringAndFree (int qString) {
	int qCString = KDE.QString_utf8 (qString);
	int charString = KDE.QCString_data (qCString);
	
	int length = KDE.strlen (charString);
	byte[] buffer = new byte [length];
	KDE.memmove (buffer, charString, length);
	/* Use the character encoding for the default locale */
	String answer = new String (Converter.mbcsToWcs (null, buffer));
		
	KDE.QCString_delete (qCString);
	KDE.QString_delete (qString);
	return answer;
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
	return launch( Display.getCurrent(), fileName );
}

/*
 *  API: When support for multiple displays is added, this method will
 *       become public and the original method above can be deprecated.
 */
private static boolean launch( Display display, String fileName ) {
	if (fileName == null) SWT.error( SWT.ERROR_NULL_ARGUMENT );
	
	// If the argument appears to be a data file (it has an extension)
	int index = fileName.lastIndexOf('.');
	if (index > 0) {
		
		// Find the associated program, if one is defined.
		String extension = fileName.substring( index );
		Program program = Program.findProgram( display, extension ); 
		
		// If the associated program is defined and can be executed, return.
		if (program != null && program.execute( fileName )) return true;
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
	
	switch (getDesktop( display )) {
		case DESKTOP_KDE: {
			String urlString = "file://" + fileName;
			/* Use the character encoding for the default locale */
			byte[] buffer = Converter.wcsToMbcs( null, urlString, true );
			int qString = KDE.QString_new( buffer );
			int url = KDE.KURL_new( qString );
			KDE.QString_delete( qString );
			/* Use the character encoding for the default locale */
			buffer = Converter.wcsToMbcs (null, name, true);
			int mimeTypeName = KDE.QString_new( buffer );
			int pid = KDE.KRun_runURL( url, mimeTypeName );
			KDE.KURL_delete( url );
			KDE.QString_delete( mimeTypeName );
			return (pid != 0);
		}
		
		case DESKTOP_GNOME: {
			// Parse the command into its individual arguments.
			String[]  args = parseCommand( command );
			int       fileArg = -1;
			int       index;
			for (index=0; index < args.length; index++) {
				int j = args[ index ].indexOf( "%f" );
				if (j != -1) {
					String value = args[ index ];
					fileArg = index;
					args[ index ] = value.substring(0,j) + fileName + value.substring(j+2);
				}
			}
	
			// If a file name was given but the command did not have "%f"
			if ((fileName.length() > 0) && (fileArg < 0)) {
				String[] newArgs = new String[ args.length + 1 ];
				for (index=0; index < args.length; index++)
					newArgs[ index ] = args[ index ];
				newArgs[ args.length ] = fileName;
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
		
		case DESKTOP_CDE: {
			/* Use the character encoding for the default locale */
			byte[] action = Converter.wcsToMbcs( null, command, true );
			byte[] fileArg = Converter.wcsToMbcs( null, fileName, true );
			Integer shell = (Integer) display.getData( cdeShell );
			int actionID = 0;
			if (shell != null) {
				actionID = CDE.DtActionInvoke( shell.intValue(), action, fileArg, 1, null, null, null, 1, 0, 0 );
			}
			return (actionID != 0);
		}
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
	String iconPath = null;
	switch (getDesktop( display )) {
		case DESKTOP_KDE: {
			/* Use the character encoding for the default locale */
			byte [] buffer = Converter.wcsToMbcs (null, name, true);
			int mimeTypeName = KDE.QString_new( buffer );
			int mimeType = KDE.KMimeType_mimeType( mimeTypeName );
			KDE.QString_delete( mimeTypeName );			
			if (mimeType == 0) return null;			
			int mimeIcon = KDE.KMimeType_icon(mimeType, 0, 0);
			int loader = KDE.KGlobal_iconLoader();
			int path = KDE.KIconLoader_iconPath(loader, mimeIcon, KDE.KICON_SMALL, 1);
			if (path == 0) return null;
			iconPath = kde_convertQStringAndFree(path);
			break;
		}
		
		case DESKTOP_GNOME: {
			String fakeFileName = "file" + extension;
			String mime = gnome_getMimeType(fakeFileName);
			if (mime == null) return null;
			iconPath = gnome_getMimeValue(mime, "icon-filename");
			if (iconPath == null) return null;
			break;
		}
		
		case DESKTOP_CDE: {
			return cde_getImageData();
		}
		
		case DESKTOP_UNKNOWN: {
			return null;
		}
	}
	if (iconPath.endsWith ("xpm")) {
		int xDisplay = display.xDisplay;
		int screen  = OS.XDefaultScreenOfDisplay( xDisplay );
		int fgPixel = OS.XWhitePixel( display.xDisplay, OS.XDefaultScreen( xDisplay ) );
		int bgPixel = OS.XBlackPixel( display.xDisplay, OS.XDefaultScreen( xDisplay ) );
		/* Use the character encoding for the default locale */
		byte [] iconName = Converter.wcsToMbcs (null, iconPath, true);
		int pixmap = OS.XmGetPixmap( screen, iconName, fgPixel, bgPixel );
    	if (pixmap == OS.XmUNSPECIFIED_PIXMAP) return null;
		Image image = Image.motif_new (display, SWT.BITMAP, pixmap, 0);
		ImageData imageData = image.getImageData ();
		
		// The pixmap returned from XmGetPixmap is cached by Motif
		// and must be deleted by XmDestroyPixmap. Because it cannot
		// be deleted directly by XFreePixmap, image.dispose() must not
		// be called. The following code should do an equivalent image.dispose().
		OS.XmDestroyPixmap( screen, pixmap );
		return imageData;		
	}
	try {
		return new ImageData (iconPath);
	} catch (Exception e) {
		return null;
	}
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
	} catch (Throwable e) {
		return false;
	}
	return true;
}

/* CDE - Get Attribute Value
 * 
 * This method takes a data type name and an attribute name, and returns
 * the corresponding attribute value.
 */

static String cde_getAttribute(String dataType, String attrName) {
	/* Use the character encoding for the default locale */
	byte [] dataTypeBuf = Converter.wcsToMbcs (null, dataType, true);
    byte [] attrNameBuf = Converter.wcsToMbcs (null, attrName, true);
    byte [] optNameBuf  = null;
	int attrValue = CDE.DtDtsDataTypeToAttributeValue( dataTypeBuf, attrNameBuf, optNameBuf );
	if (attrValue == 0) return null;
    int length = OS.strlen(attrValue);
    byte[] attrValueBuf = new byte[length];
    OS.memmove(attrValueBuf, attrValue, length);
    CDE.DtDtsFreeAttributeValue( attrValue );
	/* Use the character encoding for the default locale */
    return new String(Converter.mbcsToWcs(null, attrValueBuf));
}

/* CDE - Get Default Action of Data Type
 * 
 * This method takes a data type and returns the corresponding default action.
 * By default, the "Open" action is used if it is available. If it is not
 * available, the first action in the list is used. Typically, if Open is not
 * available, there is usually only one action anyways.
 */

static String cde_getAction(String dataType) {
	String action  = null;
	String actions = cde_getAttribute( dataType, CDE.DtDTS_DA_ACTION_LIST );
	if (actions != null) {
		int index = actions.indexOf( "Open" );
		if (index != -1) {
			action = actions.substring( index, index+4 );
		}
		else {
			index = actions.indexOf( "," );
			if (index != -1) {
				action = actions.substring( 0, index );
			}
			else {
				action = actions;
			}
		}
	}
	return action;
}

/* CDE - Get Extension of Data Type
 * 
 * This method takes a data type and returns the corresponding extension.
 * The extension is obtained from the NAME TEMPLATE attribute.
 */

static String cde_getExtension(String dataType) {
    String fileExt = cde_getAttribute( dataType, CDE.DtDTS_DA_NAME_TEMPLATE );
    if (fileExt == null || fileExt.indexOf( "%s." ) == -1) return null;
    int dot = fileExt.indexOf( "." );
    return fileExt.substring( dot );
}

/* CDE - Get Data Types
 * 
 * This method returns the list of data type names available.
 * Each data type returned is valid, meaning it has an action and
 * an extension. 
 */

static Hashtable cde_getDataTypeInfo() {
	Hashtable dataTypeInfo = new Hashtable();
	int index;
	int dataTypeList = CDE.DtDtsDataTypeNames();
	if (dataTypeList != 0) {
		// For each data type name in the list
		index = 0; 
		int dataType = CDE.listElementAt( dataTypeList, index++ );
		while (dataType != 0) {
    		int length = OS.strlen(dataType);
    		byte[] dataTypeBuf = new byte[length];
    		OS.memmove(dataTypeBuf, dataType, length);
			/* Use the character encoding for the default locale */
     		String dataTypeName = new String(Converter.mbcsToWcs(null, dataTypeBuf));
     		
	   		// The data type is valid if it is not an action, and it has an extension and an action.
      		String extension = cde_getExtension( dataTypeName );
   			if (!CDE.DtDtsDataTypeIsAction( dataTypeBuf ) &&
				extension != null && cde_getAction( dataTypeName ) != null) {
				Vector exts = new Vector();
				exts.addElement( extension );
				dataTypeInfo.put( dataTypeName, exts );
	   		}
			dataType = CDE.listElementAt( dataTypeList, index++ );
		}
		CDE.DtDtsFreeDataTypeNames( dataTypeList );
	}
	
    return dataTypeInfo;
}

/* CDE - Get Image Data
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
	int	xDisplay = display.xDisplay;
	int screen  = OS.XDefaultScreenOfDisplay( xDisplay );
	int fgPixel = OS.XWhitePixel( display.xDisplay, OS.XDefaultScreen( xDisplay ) );
	int bgPixel = OS.XBlackPixel( display.xDisplay, OS.XDefaultScreen( xDisplay ) );
	
    String icon = cde_getAttribute( name, CDE.DtDTS_DA_ICON );
    byte [] iconName;
    byte [] maskName = null;
    int    pixmap = 0;
    for (int index = 0; index < cdeIconExt.length && pixmap == 0; index++) {
		/* Use the character encoding for the default locale */
    	iconName = Converter.wcsToMbcs (null, icon + cdeIconExt[ index ], true);
    	maskName = Converter.wcsToMbcs (null, icon + cdeMaskExt[ index ], true);
		pixmap = OS.XmGetPixmap( screen, iconName, fgPixel, bgPixel );
    	if (pixmap == OS.XmUNSPECIFIED_PIXMAP) pixmap = 0;
   }
    
    if (pixmap != 0) {
    	int type = SWT.ICON;
    	// When creating the mask pixmap, do not use the screen's white and black
    	// pixel for the foreground and background respectively, because on some
    	// X servers (e.g., Solaris) pixel 0 is white and pixel 1 is black. Passing
    	// (screen, name, whitePixel, blackPixel, 1) to get the mask pixmap will
    	// result in an inverted mask. Instead explicitly use 1 (FG) and 0 (BG).
   		int mask = OS.XmGetPixmapByDepth( screen, maskName, 1, 0, 1 );
    	if (mask == OS.XmUNSPECIFIED_PIXMAP) {
    		type = SWT.BITMAP;
    		mask = 0;
    	}
		Image image = Image.motif_new (display, type, pixmap, mask );
		ImageData imageData = image.getImageData();
		
		// The pixmaps returned from XmGetPixmap... are cached by Motif
		// and must be deleted by XmDestroyPixmap. Because they cannot
		// be deleted directly by XFreePixmap, image.dispose() must not
		// be called. The following code should do an equivalent image.dispose().
		OS.XmDestroyPixmap( screen, pixmap );
		if (mask != 0) OS.XmDestroyPixmap( screen, mask ); 
		return imageData;		
    }
    return null;	
}

/* CDE - Initialize
 * 
 * This method loads the swt-cde library and initializes CDE itself.
 * The shell created fo DtAppInitialize is kept for DtActionInvoke calls.
 */
static boolean cde_init( Display display ) {
	try {
		Library.loadLibrary("swt-cde");
	} catch (Throwable e) {
		return false;
	}

	/* Use the character encoding for the default locale */
	byte[] appName = Converter.wcsToMbcs( null, "SWT", true );
	int xtContext  = OS.XtDisplayToApplicationContext( display.xDisplay );
	int widgetClass = OS.TopLevelShellWidgetClass();
	int shell = OS.XtAppCreateShell( appName, appName, widgetClass, display.xDisplay, null, 0 );
	boolean initOK  = CDE.DtAppInitialize( xtContext, display.xDisplay, shell, appName, appName );
	if (!initOK) {
		OS.XtDestroyWidget( shell );
	}
	else {
		CDE.DtDbLoad();
		display.setData( cdeShell, new Integer(shell) );
		display.disposeExec( new Runnable() {
			public void run() {
				// This logic assumes that when the corresponding display is
				// being disposed, it must be the current one.
				Integer shell = (Integer) Display.getCurrent().getData( cdeShell );
				if (shell != null) {
					OS.XtDestroyWidget( shell.intValue() );
				}
			}
		});
	}	
	return initOK;
}
}

package org.eclipse.swt.program;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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

	static final int DESKTOP_UNKNOWN = 0;
	static final int DESKTOP_KDE = 1;
	static final int DESKTOP_GNOME = 2;
	static final int Desktop = getDesktop ();
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Program () {
}

static int getDesktop () {
	File root = new File ("/proc");
	if (!root.exists () || !root.isDirectory ()) return DESKTOP_UNKNOWN;
	File [] procDirs = root.listFiles ();
	for (int i=0; i<procDirs.length; i++) {
		String directory = procDirs [i].getAbsolutePath ();
		File file = new File (directory + "/stat");
		if (file.exists ()) {
			String procName = getProcName (file);
			if (procName.indexOf ("gnome") >= 0) {
				return gnome_init() ? DESKTOP_GNOME : DESKTOP_UNKNOWN;		
			}
			if (procName.indexOf ("kdeinit") >= 0) {
				return kde_init () ? DESKTOP_KDE : DESKTOP_UNKNOWN;
			}	
		}
	}
	return DESKTOP_UNKNOWN;
}

static String getProcName (File file) {
	try {
		FileInputStream stream = new FileInputStream (file);
		while (true) {
			char ch = (char) stream.read ();
			if (ch < 0 || ch == 0xFF) return null;
			if (ch == '(') break;
		}
		String name = "";
		while (true) {
			char ch = (char) stream.read ();
			if (ch < 0 || ch == 0xFF) return null;
			if (ch == ')') break;
			name += ch;
		}
		stream.close ();
		return name;	
	} catch (IOException e) {
		return null;		
	}
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
	String command = null;
	String name = "";
	switch (Desktop) {
		case DESKTOP_KDE: {
			String urlString = "file://any." + extension;
			byte [] buffer = Converter.wcsToMbcs (null, urlString, true);
			int qString = KDE.QString_new (buffer);
			int url = KDE.KURL_new (qString);
			KDE.QString_delete (qString);
			int mimeType = KDE.KMimeType_findByURL (url);
			int mimeName = KDE.KMimeType_name (mimeType);
			int service = KDE.KServiceTypeProfile_preferredService (mimeName, 1);
			if (service == 0) return null;
			int execQString = KDE.KService_exec (service);
			command = kde_convertQStringAndFree (execQString);
			break;	
		}
		
		case DESKTOP_GNOME: {
			String fileName = "file" + extension;
			String mimeType = gnome_getMimeType (fileName);
			if (mimeType == null) return null;
			command = gnome_getMimeValue (mimeType, "open");
			if (command == null) return null;
			name = mimeType;
			break;
		}
		
		case DESKTOP_UNKNOWN:
			return null;
	}
	Program program   = new Program ();
	program.name      = name;
	program.command   = command;
	program.extension = extension;
	return program;
}

/*
 * Answer all program extensions in the operating system.
 *
 * @return an array of extensions
 */
public static String [] getExtensions () {
	switch (Desktop) {
		case DESKTOP_KDE:
			Vector names = new Vector ();
			int serviceList = KDE.KService_allServices ();
			int listBeginning = KDE.KServiceList_begin (serviceList);
			int listEnd = KDE.KServiceList_end (serviceList);
			int iterator = KDE.KServiceListIterator_new (listBeginning);
			while (true) {
				if (KDE.KServiceListIterator_equals (iterator, listEnd) != 0) break;
				int kService = KDE.KServiceListIterator_dereference (iterator);
				int serviceType = KDE.KService_type (kService);
				byte [] applicationType = Converter.wcsToMbcs (null, "Application", true);
				int appString = KDE.QString_new (applicationType);
				if (KDE.QString_equals (serviceType, appString) != 0) {
					int appName = KDE.KService_name (kService);
					names.addElement (kde_convertQStringAndFree (appName));
				}
				KDE.QString_delete (appString);
				KDE.KServiceListIterator_increment (iterator);
			}
			KDE.KServiceListIterator_delete (iterator);
			KDE.KServiceList_delete (serviceList);
			String[] appNames = new String [names.size ()];
			for (int i=0; i <names.size (); i++) {
				appNames [i] = (String) names.elementAt (i);
			}
			return appNames;

		case DESKTOP_GNOME:
			// Obtain the mime type/extension information.
			Hashtable mimeInfo = gnome_getMimeInfo();
			int  index;
			
			// Create a sorted set of the file extensions.
			Vector extensions = new Vector();
			Iterator keys = mimeInfo.keySet().iterator();
			while (keys.hasNext()) {
				String mimeType = (String) keys.next();
				Vector mimeExts = (Vector) mimeInfo.get( mimeType );
				for (index = 0; index < mimeExts.size(); index++){
					if (!extensions.contains( mimeExts.elementAt( index ) )) {
						extensions.add( mimeExts.elementAt( index ) );
					}
				}
			}
			
			// Return the list of extensions.
			String[] extStrings = new String[ extensions.size() ];
			for (index = 0; index < extensions.size(); index++) {
				extStrings[ index ] = (String) extensions.elementAt( index );
			}
			
			return extStrings;
	}
	return new String[0];
}

/*
 * Answers all available programs in the operating system.
 *
 * @return an array of programs
 */
public static Program [] getPrograms () {
	switch (Desktop) {
		case DESKTOP_KDE:
			return new Program[0]; // TBD

		case DESKTOP_GNOME:
			// Obtain the mime type/extension information.
			Hashtable mimeInfo = gnome_getMimeInfo();
			Vector programs = new Vector();
			
			// Create a list of programs with commands.
			Iterator keys = mimeInfo.keySet().iterator();
			while (keys.hasNext()) {
				String mimeType  = (String) keys.next();
				String extension = "";
				Vector mimeExts  = (Vector) mimeInfo.get( mimeType );
				if (mimeExts.size() > 0){
					extension = (String) mimeExts.elementAt( 0 );
				}
				String command  = gnome_getMimeValue( mimeType, "open" );
				if (command != null) {
					Program program   = new Program ();
					program.name      = mimeType;
					program.command   = command;
					program.extension = extension;
					programs.add( program );
				}
			}
			
			// Return the list of programs to the user.
			Program[] programList = new Program[ programs.size() ];
			for (int index = 0; index < programList.length; index++) {
				programList[ index ] = (Program) programs.elementAt( index );
			}
			return programList;
	}
	return new Program[0];
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
		boolean newType = (line.length() > 0 && Character.isLetter( line.charAt(0) ));
				  
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
				
				// While there are extensions to be processed
				exts = exts.replace( '\t', ' ' );
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
					mimeExts.add( newExt );
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
				mimeType = null;
				mimeExts = null;
				saveType = false;
			}
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
		while (sIndex < cmd.length() && Character.isWhitespace( cmd.charAt(sIndex) )) {
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
					args.add( cmd.substring( sIndex, eIndex ) );
				}
				// else add the argument, trimming off the quotes.
				else {
					args.add( cmd.substring( sIndex+1, eIndex ) );
				}
				sIndex = eIndex + 1;
			}
			
			// else use white space for the delimiters.
			else {
				eIndex = sIndex;
				while (eIndex < cmd.length() && !Character.isWhitespace( cmd.charAt(eIndex) )) {
					eIndex++;
				}
				args.add( cmd.substring( sIndex, eIndex ) );
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
	byte [] nameBuffer = Converter.wcsToMbcs (null, dirName, true);
	int ptr = GNOME.gnome_datadir_file(nameBuffer);
	if (ptr == 0) return null;
	int length = OS.strlen(ptr);
	byte[] dirBuffer = new byte[length];
	OS.memmove(dirBuffer, ptr, length);
	return new String(Converter.mbcsToWcs(null, dirBuffer));
}

static String gnome_getHomeDirectory() {
	int ptr = GNOME.g_get_home_dir();
	if (ptr == 0) return null;
	int length = OS.strlen(ptr);
	byte[] homeDirBuffer = new byte[length];
	OS.memmove(homeDirBuffer, ptr, length);
	return new String(Converter.mbcsToWcs(null, homeDirBuffer));
}

static String gnome_getMimeType(String name) {
	byte [] nameBuffer = Converter.wcsToMbcs (null, name, true);
	int ptr = GNOME.gnome_mime_type(nameBuffer);
	if (ptr == 0) return null;
	int length = OS.strlen(ptr);
	byte[] mimeBuffer = new byte[length];
	OS.memmove(mimeBuffer, ptr, length);
	return new String(Converter.mbcsToWcs(null, mimeBuffer));
}

static String gnome_getMimeValue(String mimeType, String key) {
	byte [] typeBuffer = Converter.wcsToMbcs (null, mimeType, true);
	byte [] keyBuffer = Converter.wcsToMbcs (null, key, true);
	int ptr = GNOME.gnome_mime_get_value(typeBuffer, keyBuffer);
	if (ptr == 0) return null;
	
	StringBuffer stringBuffer = new StringBuffer();
	int length = OS.strlen(ptr);
	byte[] valueBuffer = new byte[length];
	OS.memmove(valueBuffer, ptr, length);
	return new String(Converter.mbcsToWcs(null, valueBuffer));
}

static boolean kde_init () {
	try {
		Callback.loadLibrary("swt-kde");
	} catch (UnsatisfiedLinkError e) {
		return false;
	}
	String appName = "KDE Platform Support";
	byte [] nameBuffer = Converter.wcsToMbcs (null, appName, true);
	int qcString = KDE.QCString_new (nameBuffer);
	KDE.KApplication_new (qcString);
	KDE.QCString_delete (qcString);
	return true;
}

static String kde_convertQStringAndFree (int qString) {
	int qCString = KDE.QString_utf8 (qString);
	int charString = KDE.QCString_data (qCString);
	
	StringBuffer stringBuffer = new StringBuffer ();
	int length = KDE.strlen (charString);
	byte[] buffer = new byte [length];
	KDE.memmove (buffer, charString, length);
	String answer = new String (Converter.mbcsToWcs (null, buffer));
		
	KDE.QCString_delete (qCString);
	KDE.QString_delete (qString);
	return answer;
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
	
	// If the argument appears to be a data file (it has an extension)
	int index = fileName.lastIndexOf (".");
	if (index > 0) {
		
		// Find the associated program, if one is defined.
		String extension = fileName.substring (index);
		Program program = Program.findProgram (extension);
		
		// If the associated program is defined and can be executed, return.
		if (program != null && program.execute (fileName)) return true;
	}
	
	// Otherwise, the argument was the program itself.
	try {
		Runtime.getRuntime().exec (fileName);
		return true;
	} catch (IOException e) {
		return false;
	}
}

/*
 * Executes the program with the file as the single argument
 * in the operating system.  It is the responsibility of the
 * programmer to ensure that the file contains valid data for 
 * this program.  
 *
 * @param fileName is the argument (typically a file) for the program
 * @return <code>true</code> if the file is launched, otherwise <code>false</code>
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_NULL_ARGUMENT when fileName is null</li>
 *	</ul>
 */
public boolean execute (String fileName) {
	if (fileName == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
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
		Runtime.getRuntime().exec( args );
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
	String iconPath = null;
	switch (Desktop) {
		case DESKTOP_KDE:	
			String urlString = "file://any." + extension;
			byte [] buffer = Converter.wcsToMbcs (null, urlString, true);
			int qString = KDE.QString_new(buffer);
			int url = KDE.KURL_new(qString);
			KDE.QString_delete(qString);			
			int mimeType = KDE.KMimeType_findByURL(url);
			if (mimeType == 0) return null;			
			int mimeIcon = KDE.KMimeType_icon(mimeType, 0, 0);
			int loader = KDE.KGlobal_iconLoader();
			int path = KDE.KIconLoader_iconPath(loader, mimeIcon, KDE.KICON_SMALL, 1);
			iconPath = kde_convertQStringAndFree(path);
			break;
		case DESKTOP_GNOME:
			String fakeFileName = "file." + extension;
			String mime = gnome_getMimeType(fakeFileName);
			if (mime == null) return null;
			iconPath = gnome_getMimeValue(mime, "icon-filename");
			if (iconPath == null) return null;
			break;
		case DESKTOP_UNKNOWN:
			return null;
	}
	if (iconPath.endsWith ("xpm")) {
		//BAD
		Display display = Display.getCurrent (); 
		int xDisplay = display.xDisplay;
		int drawable = OS.XDefaultRootWindow (xDisplay);		
		int [] pixmap_ptr = new int [1], mask_ptr = new int [1];
		byte [] buffer = Converter.wcsToMbcs (null, iconPath, true);
		int result = OS.XpmReadFileToPixmap (xDisplay, drawable, buffer, pixmap_ptr, mask_ptr, 0);
		if (result < 0) return null;
		Image image = Image.motif_new (display, SWT.BITMAP, pixmap_ptr[0], mask_ptr [0]);
		ImageData imageData = image.getImageData ();
		image.dispose ();
		return imageData;		
	}
	try {
		return new ImageData (iconPath);
	} catch (Exception e) {
		return null;
	}
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
static boolean gnome_init () {
	try {
		Callback.loadLibrary("swt-gnome");
	} catch (UnsatisfiedLinkError e) {
		return false;
	}
	return true;
}

}

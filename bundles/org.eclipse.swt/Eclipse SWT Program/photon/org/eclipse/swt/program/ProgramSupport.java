package org.eclipse.swt.program;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import java.io.*;
import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;

/**
 * @deprecated see Java doc for Windows
 *
 * This class provides an OS independent interface to
 * functionality that is OS dependent
 */
public final class ProgramSupport {

static public String getWindowSystem() {
	// Same name as org.eclipse.swt.internal.motif
	return "motif";
}

/**
 * Convenience method to determine whether the current platform
 * has reparenting support.
 * @return boolean is reparenting supported
 */
public static boolean isReparentable() {
	return false;
}

/**
 * Given a filename and registry index, this method will search
 * the platform registry and return the registered large image
 * associated with the filename. If the filename is not
 * registered null is returned. Note: if the platform does not
 * have registry support, null is always returned.
 * The caller is responsible for destroying the Image.
 * @param fileName name of exectuable file
 * @param index registry index
 * @return org.eclipse.swt.graphics.Image of registered icon or 
 *	<code>null</code> if not present
 */
public static Image loadLargeIcon(Display display, String fileName, int index) {
	return loadSmallIcon(display, fileName, index);
}

/**
 * Given a filename and registry index, this method will search
 * the platform registry and return the registered small image
 * associated with the filename. If the filename is not
 * registered null is returned. Note: if the platform does not
 * have registry support, null is always returned.
 * The caller is responsible for destroying the Image.
 * @param fileName name of exectuable file
 * @param index registry index
 * @return org.eclipse.swt.graphics.Image of registered icon or 
 *	<code>null</code> if not present
 */
public static Image loadSmallIcon(Display display, String fileName, int index) {
	return null;
}

/**
 * Given a RichText widget this method will attempt to print the
 * contents of the editor.
 * @param editor the widget that contains the text to be printed
 */
public static void print(org.eclipse.swt.widgets.Control editor) {

}

/**
* Answer the path, name and command line parameters registered
* for a given extension.
* <p>
*
* @param extension the extension to query.  Must provide leading '.'
*
* @return the path, name and command line parameters registered for
*	the extension.  Empty string if the extension is not registered.
*/
public static String queryExtension(String extension) {
	return null;
}

/**
 * Given a file extension (something like .html ) the function
 * returns the full path of the icon image registered to 
 * the extension. The file name has a comma and index appended 
 * to it. This is the offset into an icon registry of the image.
 * @param extension the string that appears after the last dot
 *		of the file name
 * @return String full path of executable file registered to the 
 * 		extension. If no extension has been registered an empty
 * 		string is returned. Appended to the end after a comma
 *		is the offset of the image
 */
public static String queryExtensionIcon(String extension) {
	return "";
}

/**
 * This function querys the registery for a list of all the
 * executable programs registered on the platform.
 * @return String[] an array of strings. Each string contains the
 * 		full path of the executable program. Duplicates may exist 
 *		since multiple extensions may be registered to the same program.
 */
public static String[] queryRegisteredPrograms() {
	return new String[0];
}

/**
 * This method will change the parent of control to the composite.
 * @param control org.eclipse.swt.widgets.Control - control with parent to be changed
 * @param parent org.eclipse.swt.widgets.Widget - the new parent of control
 */
public static void setParent(org.eclipse.swt.widgets.Control control, org.eclipse.swt.widgets.Composite parent) {
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
}

/**
 * Attempts to open the specified file. The effect varies based on
 * the file type and underlying operating system. On win32, the effect
 * is to invoke the ShellExecute API with the command "open". On motif
 * the effect is to lookup the command to invoke using the underlying
 * o/s's mime table support.
 * 
 * @param		file String
 *				a file to open.
 * @return 		boolean
 *				true if successful, false otherwise.
 */
public static boolean shellExecute(String file) {
	return shellExecute(file, null);
}

/**
 * Attempts to open the specified file. The effect varies based on
 * the file type and underlying operating system. On win32, the effect
 * is to invoke the ShellExecute API with the command "open". On motif
 * the effect is to lookup the command to invoke using the underlying
 * o/s's mime table support.
 * 
 * Note: This method will be deprecated in the next release.
 * 
 * @param		file String
 *				a file to open.
 * @param		args String
 *				a String to be passed when the file is opened.
 * @return 		boolean
 *				true if successful, false otherwise.
 */
public static boolean shellExecute(String file, String args) {
//	if (Desktop == DESKTOP_UNKNOWN) return false;
		
	int startIndex = file.lastIndexOf('.');
	if (startIndex > -1) {
		String extension = file.substring(startIndex + 1); 
		String command = queryExtension(extension);
		int index = command.indexOf("%f");
		if (index > -1) {
			command = command.substring(0, index) + file;
			if (args != null) {
				command += " "+args;
			}
			try {
				Runtime.getRuntime().exec(command);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
	}

	return false;
}
















public static String getFriendlyName(String programId) {
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}

public static String queryExtensionProgramId(String extension) {
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}

public static String[] queryInsertables() {
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}	
}

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
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select or enter a file name.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SAVE, OPEN, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String filterPath = "";
	String fileName = "";
	String[] fileNames = new String [0];
	String fullPath = "";
	int /*long*/ handle;
	static final char SEPARATOR = System.getProperty ("file.separator").charAt (0);
	
/**
 * Constructs a new instance of this class given only its
 * parent.
 * <p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FileDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FileDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}
/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	return fileName;
}
/**
 * Returns the paths of all files that were selected
 * in the dialog relative to the filter path.
 * 
 * @return the relative paths of the files
 */
public String [] getFileNames () {
	return fileNames;
}
/**
 * Returns the file extensions which the dialog will
 * use to filter the files it shows.
 *
 * @return the file extensions filter
 */
public String [] getFilterExtensions () {
	return filterExtensions;
}
/**
 * Returns the file names which the dialog will
 * use to filter the files it shows.
 *
 * @return the file name filter
 */
public String [] getFilterNames () {
	return filterNames;
}
/**
 * Returns the directory path that the dialog will use.
 * File names in this path will appear in the dialog,
 * filtered according to the filter extensions.
 *
 * @return the directory path string
 * 
 * @see #setFilterExtensions
 */
public String getFilterPath () {
	return filterPath;
}
/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the first selected file,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	handle = OS.gtk_file_selection_new (titleBytes);
	if (parent != null) {
		OS.gtk_window_set_transient_for (handle, parent.topHandle());
	}
	String answer = null;
	preset ();
	int response = OS.gtk_dialog_run (handle);
	if (response == OS.GTK_RESPONSE_OK) {
		int /*long*/ fileNamePtr = OS.gtk_file_selection_get_filename (handle);
		int /*long*/ utf8Ptr = OS.g_filename_to_utf8 (fileNamePtr, -1, null, null, null);
		int [] items_written = new int [1];
		int /*long*/ utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
		int length = items_written [0];
		char [] buffer = new char [length];
		OS.memmove (buffer, utf16Ptr, length * 2);
		String osAnswer = new String (buffer);
		OS.g_free (utf16Ptr);
		OS.g_free (utf8Ptr);
		answer = interpretOsAnswer (osAnswer);
	}
	OS.gtk_widget_destroy (handle);
	return answer;
}
/**
 * Set the initial filename which the dialog will
 * select by default when opened to the argument,
 * which may be null.  The name will be prefixed with
 * the filter path when one is supplied.
 * 
 * @param string the file name
 */
public void setFileName (String string) {
	fileName = string;
}
/**
 * Set the file extensions which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 *
 * @param extensions the file extension filter
 */
public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}
/**
 * Sets the file names which the dialog will
 * use to filter the files it shows to the argument,
 * which may be null.
 *
 * @param names the file name filter
 */
public void setFilterNames (String [] names) {
	filterNames = names;
}
/**
 * Sets the directory path that the dialog will use
 * to the argument, which may be null. File names in this
 * path will appear in the dialog, filtered according
 * to the filter extensions.
 *
 * @param string the directory path
 * 
 * @see #setFilterExtensions
 */
public void setFilterPath (String string) {
	filterPath = string;
}
void preset() {
	OS.gtk_file_selection_set_select_multiple(handle, (style & SWT.MULTI) != 0);

	/* Calculate the fully-specified file name and convert to bytes */
	StringBuffer stringBuffer = new StringBuffer ();
	if (filterPath == null) {
		filterPath = "";
	} else {
		if (filterPath.length () > 0) {
			stringBuffer.append (filterPath);
			if (filterPath.charAt (filterPath.length () - 1) != SEPARATOR) {
				stringBuffer.append (SEPARATOR);
			}
		}
	}
	if (fileName == null) {
		fileName = "";
	} else {
		stringBuffer.append (fileName);
	}
	fullPath = stringBuffer.toString ();
	int length = fullPath.length ();
	char [] buffer = new char [length + 1];
	fullPath.getChars (0, length, buffer, 0);
	int /*long*/ utf8Ptr = OS.g_utf16_to_utf8 (buffer, -1, null, null, null);
	int /*long*/ fileNamePtr = OS.g_filename_from_utf8 (utf8Ptr, -1, null, null, null);
	OS.gtk_file_selection_set_filename (handle, fileNamePtr);
	OS.g_free (utf8Ptr);
	OS.g_free (fileNamePtr);
				
	/* Set the extension */
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterExtensions.length == 1) {
		String ext = filterExtensions [0];
		byte [] extBytes = Converter.wcsToMbcs (null, ext, true);
		OS.gtk_file_selection_complete (handle, extBytes);
	}	
	fullPath = null;
	fileNames = new String [0];
}
String interpretOsAnswer(String osAnswer) {
	if (osAnswer==null) return null;
	int separatorIndex = osAnswer.lastIndexOf (SEPARATOR);
	if (separatorIndex+1 == osAnswer.length ()) return null;
	
	String answer = fullPath = osAnswer;
	fileName = fullPath.substring (separatorIndex+1);
	filterPath = fullPath.substring (0, separatorIndex);
	if ((style & SWT.MULTI) == 0) {
		fileNames = new String[] {fileName};
	} else {
		int /*long*/ namesPtr = OS.gtk_file_selection_get_selections (handle);
		int /*long*/ namesPtr1 = namesPtr;
		int [] namePtr = new int [1];
		OS.memmove (namePtr, namesPtr1, 4);
		int length = 0;
		while (namePtr[0] != 0) {
			length++;
			namesPtr1+=4;
			OS.memmove(namePtr, namesPtr1, 4);
		}
		fileNames = new String [length];
		namePtr = new int [length];
		OS.memmove (namePtr, namesPtr, length * 4);
		for (int i = 0; i < length; i++) {			
			int /*long*/ utf8Ptr = OS.g_filename_to_utf8 (namePtr [i], -1, null, null, null);
			int [] items_written = new int [1];
			int /*long*/ utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
			char[] buffer = new char [items_written [0]];
			OS.memmove (buffer, utf16Ptr, items_written [0] * 2);
			String name = new String (buffer);
			fileNames [i] = name.substring (name.lastIndexOf (SEPARATOR) + 1);
			OS.g_free (utf16Ptr);
			OS.g_free (utf8Ptr);
		}
		OS.g_strfreev (namesPtr);
	}
	return answer;
}

}

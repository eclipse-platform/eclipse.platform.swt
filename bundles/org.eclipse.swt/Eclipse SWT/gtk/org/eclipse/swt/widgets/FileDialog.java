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
	static final char EXTENSION_SEPARATOR = ';';
	
/**
 * Constructs a new instance of this class given only its parent.
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
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
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
String computeResultChooserDialog () {
	/* MULTI is only valid if the native dialog's action is Open */
	if ((style & (SWT.SAVE | SWT.MULTI)) == SWT.MULTI) {
		int /*long*/ list = OS.gtk_file_chooser_get_filenames (handle);
		int listLength = OS.g_slist_length (list);
		fileNames = new String [listLength];
		int /*long*/ current = list;
		String path = null;
		for (int i = 0; i < listLength; i++) {
			int /*long*/ name = OS.g_slist_data (current);
			int length = OS.strlen (name);
			byte [] buffer = new byte [length];
			OS.memmove (buffer, name, length);
			path = new String (Converter.mbcsToWcs (null, buffer));
			fileNames [i] = path.substring (path.lastIndexOf (SEPARATOR) + 1);
			current = OS.g_slist_next (current);
			OS.g_free (name);
		}
		OS.g_slist_free (list);
		fullPath = path;
	} else {
		int /*long*/ path = OS.gtk_file_chooser_get_filename (handle);
		if (path == 0) return null;
		int length = OS.strlen (path);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, path, length);
		OS.g_free (path);
		fullPath = new String (Converter.mbcsToWcs (null, buffer));
		fileNames = new String [1];
		fileNames[0] = fullPath.substring (fullPath.lastIndexOf (SEPARATOR) + 1);
	}
	int separatorIndex = fullPath.lastIndexOf (SEPARATOR);
	fileName = fullPath.substring (separatorIndex + 1);
	filterPath = fullPath.substring (0, separatorIndex);
	return fullPath;
}
String computeResultClassicDialog () {
	GtkFileSelection selection = new GtkFileSelection ();
	OS.memmove (selection, handle);
	int /*long*/ entry = selection.selection_entry;
	int /*long*/ entryText = OS.gtk_entry_get_text (entry);
	int entryLength = OS.strlen (entryText);
	if (entryLength == 0) {
		int /*long*/ fileList = selection.file_list;
		int /*long*/ listSelection = OS.gtk_tree_view_get_selection (fileList);
		int /*long*/[] model = new int /*long*/[1];
		int /*long*/ selectedList = OS.gtk_tree_selection_get_selected_rows (listSelection, model);
		if (selectedList == 0) return null;
		int listLength = OS.g_list_length (selectedList);
		if (listLength == 0) {
			OS.g_list_free (selectedList);
			return null;
		}
		int /*long*/ path = OS.g_list_nth_data (selectedList, 0);
		int /*long*/ [] ptr = new int /*long*/[1];
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		if (OS.gtk_tree_model_get_iter (model [0], iter, path)) {
			OS.gtk_tree_model_get (model [0], iter, 0, ptr, -1);
		}
		OS.g_free (iter);
		for (int i = 0; i < listLength; i++) {
			OS.gtk_tree_path_free (OS.g_list_nth_data (selectedList, i));
		}
		OS.g_list_free (selectedList);
		if (ptr [0] == 0) return null;
		int length = OS.strlen (ptr [0]);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr [0], length);
		OS.g_free (ptr [0]);
		OS.gtk_entry_set_text (entry, buffer);
	}

	int /*long*/ fileNamePtr = OS.gtk_file_selection_get_filename (handle);
	int /*long*/ utf8Ptr = OS.g_filename_to_utf8 (fileNamePtr, -1, null, null, null);
	int /*long*/ [] items_written = new int /*long*/ [1];
	int /*long*/ utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
	entryLength = (int)/*64*/items_written [0];
	char [] buffer = new char [entryLength];
	OS.memmove (buffer, utf16Ptr, entryLength * 2);
	String osAnswer = new String (buffer);
	OS.g_free (utf16Ptr);
	OS.g_free (utf8Ptr);

	if (osAnswer == null) return null;
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
		int /*long*/ [] namePtr = new int /*long*/ [1];
		OS.memmove (namePtr, namesPtr1, OS.PTR_SIZEOF);
		int length = 0;
		while (namePtr[0] != 0) {
			length++;
			namesPtr1+=OS.PTR_SIZEOF;
			OS.memmove(namePtr, namesPtr1, OS.PTR_SIZEOF);
		}
		fileNames = new String [length];
		namePtr = new int /*long*/ [length];
		OS.memmove (namePtr, namesPtr, length * OS.PTR_SIZEOF);
		for (int i = 0; i < length; i++) {			
			utf8Ptr = OS.g_filename_to_utf8 (namePtr [i], -1, null, null, null);
			items_written = new int /*long*/ [1];
			utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
			buffer = new char [(int)/*64*/items_written [0]];
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
	boolean useChooserDialog = OS.gtk_check_version (2, 4, 10) == 0;
	if (useChooserDialog) {
		return openChooserDialog ();
	} else {
		return openClassicDialog ();
	}
}
String openChooserDialog () {
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	int action = (style & SWT.SAVE) != 0 ?
		OS.GTK_FILE_CHOOSER_ACTION_SAVE :
		OS.GTK_FILE_CHOOSER_ACTION_OPEN;
	Display display = parent.getDisplay ();
	/*
	* Feature in gtk.  If the hicolor theme is not installed then a warning
	* is displayed whenever a GtkFileChooser is created.  The workaround is
	* to turn off the display of warnings during GtkFileChooser creation.
	* http://bugzilla.gnome.org/show_bug.cgi?id=149931 . 
	*/
	boolean oldWarnings = display.getWarnings ();
	display.setWarnings (false);
	handle = OS.gtk_file_chooser_dialog_new (
		titleBytes,
		parent.topHandle (),
		action,
		OS.GTK_STOCK_CANCEL (), OS.GTK_RESPONSE_CANCEL,
		OS.GTK_STOCK_OK (), OS.GTK_RESPONSE_OK,
		0);
	display.setWarnings (oldWarnings);
	OS.g_object_set (handle, OS.show_hidden, 1, 0);
	presetChooserDialog ();
	String answer = null;
	if (OS.gtk_dialog_run (handle) == OS.GTK_RESPONSE_OK) {
		answer = computeResultChooserDialog ();
	}
	OS.gtk_widget_destroy (handle);
	return answer;
}
String openClassicDialog () {
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	handle = OS.gtk_file_selection_new (titleBytes);
	if (parent != null) {
		OS.gtk_window_set_transient_for (handle, parent.topHandle());
	}
	presetClassicDialog ();
	String answer = null;
	if (OS.gtk_dialog_run (handle) == OS.GTK_RESPONSE_OK) {
		answer = computeResultClassicDialog ();
	}
	OS.gtk_widget_destroy (handle);
	return answer;
}
void presetChooserDialog () {
	/* MULTI is only valid if the native dialog's action is Open */   
	if ((style & (SWT.SAVE | SWT.MULTI)) == SWT.MULTI) {
		OS.gtk_file_chooser_set_select_multiple (handle, true);
	}
	if (filterPath == null) filterPath = "";
	if (fileName == null) fileName = "";
	if (filterPath.length () > 0) {
		StringBuffer stringBuffer = new StringBuffer ();
		/* filename must be a full path */
		if (filterPath.charAt (0) != SEPARATOR) {
			stringBuffer.append (SEPARATOR);
		}
		stringBuffer.append (filterPath);
		if (filterPath.charAt (filterPath.length () - 1) != SEPARATOR) {
			stringBuffer.append (SEPARATOR);
		}
		if (fileName.length () > 0) {
			stringBuffer.append (fileName);
		} else {
			/* go into the specified directory */
			stringBuffer.append ('.');
		}
		byte [] buffer = Converter.wcsToMbcs (null, stringBuffer.toString (), true);
		OS.gtk_file_chooser_set_filename (handle, buffer);
	}
	if ((style & SWT.SAVE) != 0 && fileName.length () > 0) {
		byte [] buffer = Converter.wcsToMbcs (null, fileName, true);
		OS.gtk_file_chooser_set_current_name (handle, buffer);
	}

	/* Set the extension filters */
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions == null) filterExtensions = new String [0];
	for (int i = 0; i < filterExtensions.length; i++) {
		if (filterExtensions [i] != null) {
			int /*long*/ filter = OS.gtk_file_filter_new ();
			if (filterNames.length > i && filterNames [i] != null) {
				byte [] name = Converter.wcsToMbcs (null, filterNames [i], true);
				OS.gtk_file_filter_set_name (filter, name);
			} else {
				byte [] name = Converter.wcsToMbcs (null, filterExtensions [i], true);
				OS.gtk_file_filter_set_name (filter, name);
			}
			int start = 0;
			int index = filterExtensions [i].indexOf (EXTENSION_SEPARATOR);
			while (index != -1) {
				String current = filterExtensions [i].substring (start, index);
				byte [] filterString = Converter.wcsToMbcs (null, current, true);
				OS.gtk_file_filter_add_pattern (filter, filterString);
				start = index + 1;
				index = filterExtensions [i].indexOf (EXTENSION_SEPARATOR, start);
			}
			String current = filterExtensions [i].substring (start);
			byte [] filterString = Converter.wcsToMbcs (null, current, true);
			OS.gtk_file_filter_add_pattern (filter, filterString);
			OS.gtk_file_chooser_add_filter (handle, filter);
		}
	}
	fullPath = null;
	fileNames = new String [0];
}
void presetClassicDialog () {
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
				
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions == null) filterExtensions = new String [0];
	fullPath = null;
	fileNames = new String [0];
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
}

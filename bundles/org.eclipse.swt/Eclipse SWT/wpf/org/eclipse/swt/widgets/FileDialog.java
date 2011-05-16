/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;

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
 * Note: Only one of the styles SAVE and OPEN may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#filedialog">FileDialog snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String [] fileNames = new String [0];
	String filterPath = "", fileName = "";  //$NON-NLS-1$//$NON-NLS-2$
	int filterIndex = -1;
	boolean overwrite = false;

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
	this (parent, SWT.APPLICATION_MODAL);
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
 * 
 * @see SWT#SAVE
 * @see SWT#OPEN
 * @see SWT#MULTI
 */
public FileDialog (Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}

/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path, or an
 * empty string if no such file has been selected.
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	return fileName;
}

/**
 * Returns a (possibly empty) array with the paths of all files
 * that were selected in the dialog relative to the filter path.
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
 * Get the 0-based index of the file extension filter
 * which was selected by the user, or -1 if no filter
 * was selected.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @return index the file extension filter index
 * 
 * @see #getFilterExtensions
 * @see #getFilterNames
 * 
 * @since 3.4
 */
public int getFilterIndex () {
	return filterIndex;
}

/**
 * Returns the names that describe the filter extensions
 * which the dialog will use to filter the files it shows.
 *
 * @return the list of filter names
 */
public String [] getFilterNames () {
	return filterNames;
}

/**
 * Returns the directory path that the dialog will use, or an empty
 * string if this is not set.  File names in this path will appear
 * in the dialog, filtered according to the filter extensions.
 *
 * @return the directory path string
 * 
 * @see #setFilterExtensions
 */
public String getFilterPath () {
	return filterPath;
}

/**
 * Returns the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 *
 * @return true if the dialog will prompt for file overwrite, false otherwise
 * 
 * @since 3.4
 */
public boolean getOverwrite () {
	return overwrite;
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
	int dialog;
	if ((style & SWT.SAVE) != 0) {
		dialog = OS.gcnew_SaveFileDialog ();
	} else {
		dialog = OS.gcnew_OpenFileDialog ();
		if ((style & SWT.MULTI) != 0) OS.OpenFileDialog_Multiselect (dialog, true);
	}
	int titlePtr = parent.createDotNetString (title, false);
	OS.FileDialog_Title (dialog, titlePtr);
	OS.GCHandle_Free (titlePtr);
	int fileNamePtr = parent.createDotNetString (fileName, false);
	OS.FileDialog_FileName (dialog, fileNamePtr);
	OS.GCHandle_Free (fileNamePtr);

	if (filterExtensions != null && filterExtensions.length > 0) {
		StringBuffer strFilter = new StringBuffer();
		for (int i=0; i<filterExtensions.length; i++) {
			if (i > 0) strFilter.append("|");
			if (filterNames != null && i < filterNames.length) {
				strFilter.append(filterNames [i]);
			} else {
				strFilter.append(filterExtensions [i]);
			}
			strFilter.append("|");
			strFilter.append(filterExtensions [i]);
		}
		int filterPtr = parent.createDotNetString(strFilter.toString (), false);
		OS.FileDialog_Filter (dialog, filterPtr);
		OS.GCHandle_Free (filterPtr);
		if (filterIndex != -1) OS.FileDialog_FilterIndex (dialog, filterIndex + 1);
	}
	
	int filterPathPtr = parent.createDotNetString (filterPath, false);
	OS.FileDialog_InitialDirectory (dialog, filterPathPtr);
	OS.GCHandle_Free (filterPathPtr);
	
	if ((style & SWT.SAVE) != 0) OS.SaveFileDialog_OverwritePrompt (dialog, overwrite);
	
	int parentHandle = (parent.style & SWT.ON_TOP) == 0 ? parent.shellHandle : 0;
	boolean success = OS.CommonDialog_ShowDialog (dialog, parentHandle);

	/* Set the new path, file name and filter */
	String fullPath = null;
	if (success) {
		int strings = OS.FileDialog_FileNames (dialog);
		int length = OS.ICollection_Count (strings);
		fileNames = new String [length];
		for (int i = 0; i < length; i++) {
			int str = OS.IList_default (strings, i);
			int fileInfo = OS.gcnew_FileInfo (str);
			int name = OS.FileInfo_Name (fileInfo);
			fileNames [i] = Widget.createJavaString (name);
			if (i == 0) {
				int dir = OS.FileInfo_DirectoryName (fileInfo);
				filterPath = Widget.createJavaString (dir);
				OS.GCHandle_Free (dir);
			}
			OS.GCHandle_Free (name);
			OS.GCHandle_Free (fileInfo);
			OS.GCHandle_Free (str);
		}
		OS.GCHandle_Free (strings);
		fullPath = filterPath + "\\" + fileNames [0];
		fileName = fileNames [0];
	} else {
		fileNames = new String [0];
	}
	filterIndex = OS.FileDialog_FilterIndex (dialog) - 1;
	
	OS.GCHandle_Free (dialog);
	/* Answer the full path or null */
	return fullPath;
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
 * <p>
 * The strings are platform specific. For example, on
 * some platforms, an extension filter string is typically
 * of the form "*.extension", where "*.*" matches all files.
 * For filters with multiple extensions, use semicolon as
 * a separator, e.g. "*.jpg;*.png".
 * </p>
 *
 * @param extensions the file extension filter
 * 
 * @see #setFilterNames to specify the user-friendly
 * names corresponding to the extensions
 */
public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}

/**
 * Set the 0-based index of the file extension filter
 * which the dialog will use initially to filter the files
 * it shows to the argument.
 * <p>
 * This is an index into the FilterExtensions array and
 * the FilterNames array.
 * </p>
 *
 * @param index the file extension filter index
 * 
 * @see #setFilterExtensions
 * @see #setFilterNames
 * 
 * @since 3.4
 */
public void setFilterIndex (int index) {
	filterIndex = index;
}

/**
 * Sets the names that describe the filter extensions
 * which the dialog will use to filter the files it shows
 * to the argument, which may be null.
 * <p>
 * Each name is a user-friendly short description shown for
 * its corresponding filter. The <code>names</code> array must
 * be the same length as the <code>extensions</code> array.
 * </p>
 *
 * @param names the list of filter names, or null for no filter names
 * 
 * @see #setFilterExtensions
 */
public void setFilterNames (String [] names) {
	filterNames = names;
}

/**
 * Sets the directory path that the dialog will use
 * to the argument, which may be null. File names in this
 * path will appear in the dialog, filtered according
 * to the filter extensions. If the string is null,
 * then the operating system's default filter path
 * will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the directory path
 * 
 * @see #setFilterExtensions
 */
public void setFilterPath (String string) {
	filterPath = string;
}

/**
 * Sets the flag that the dialog will use to
 * determine whether to prompt the user for file
 * overwrite if the selected file already exists.
 *
 * @param overwrite true if the dialog will prompt for file overwrite, false otherwise
 * 
 * @since 3.4
 */
public void setOverwrite (boolean overwrite) {
	this.overwrite = overwrite;
}
}

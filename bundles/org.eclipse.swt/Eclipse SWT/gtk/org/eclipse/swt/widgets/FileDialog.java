package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
public class FileDialog extends GtkFileDialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String filterPath;
	String fileName = "";
	String fullPath = "";
	
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
 * for all SWT dialog classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * selected in the dialog relative to the filter path,
 * or null if none is available.
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	return fileName;
}
/**
 * Returns the paths of all files that were selected
 * in the dialog relative to the filter path, or null
 * if none are available.
 * 
 * @return the relative paths of the files
 */
public String [] getFileNames () {
	return new String[] {fileName};
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
 * Returns the path which the dialog will use to filter
 * the files it shows.
 *
 * @return the filter path
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
	/*
	 * The only reason this method is not just left out to
	 * fall through to the superclass, is the JavaDoc comment.
	 */
	return super.open();
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
 * Sets the path which the dialog will use to filter
 * the files it shows to the argument, which may be
 * null.
 *
 * @param string the filter path
 */
public void setFilterPath (String string) {
	filterPath = string;
}
void preset() {
	/* Calculate the fully-specified file name and convert to bytes */
	StringBuffer stringBuffer = new StringBuffer ();
	if (filterPath == null) {
		filterPath = "";
	} else {
		if (filterPath.length () > 0) {
			stringBuffer.append (filterPath);
			if (filterPath.charAt (filterPath.length () - 1) != separator) {
				stringBuffer.append (separator);
			}
		}
	}
	if (fileName == null) {
		fileName = "";
	} else {
		stringBuffer.append (fileName);
	}
	fullPath = stringBuffer.toString ();
	byte [] fullPathBytes = Converter.wcsToMbcs (null, fullPath, true);
	OS.gtk_file_selection_set_filename (handle, fullPathBytes);
	
	/* Set the extension */
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterExtensions.length == 1) {
		String ext = filterExtensions [0];
		byte [] extBytes = Converter.wcsToMbcs (null, ext, true);
		OS.gtk_file_selection_complete (handle, extBytes);
	}
	
	fileName = null;
	fullPath = null;
	filterPath = null;
}

void interpretOsAnswer(String osAnswer) {
	if (osAnswer==null) return;
	int separatorIndex = calculateLastSeparatorIndex(osAnswer);
	if (separatorIndex+1 == osAnswer.length()) {
		/*
		 * the selected thing is a directory
		 */
		answer = null;
		return;
	}
	answer = fullPath = osAnswer;
	fileName = answer.substring(separatorIndex+1);
}
}

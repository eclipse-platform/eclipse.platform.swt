package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

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
	String filterPath = "", fileName = "";
	static final String FILTER = "*";
	
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
	return new String [] {fileName};
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
	int parentHandle = 0;
	if (parent != null) parentHandle = parent.shellHandle;
	byte [] title = null;
	if (this.title != null) title = Converter.wcsToMbcs (null, this.title, true);
	byte [] root_dir = null;
	if (filterPath != null) {
		root_dir = Converter.wcsToMbcs (null, filterPath, true);
	}
	
	/* Compute the filter */
	String mask = FILTER;
	/*
	* Photon does not support filter names.
	*/
	if (filterNames == null) filterNames = new String [0];
	/*
	* Photon supports only one filter with multiple patterns
	* separated by commas.
	*/
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterExtensions.length > 0) {
		String comma = ",";
		mask = comma;
		for (int i=0; i<filterExtensions.length; i++) {
			String ext = filterExtensions [i];
			int length = ext.length();
			int end, start = 0;
			do {
				int index = end = ext.indexOf(';', start);
				if (end < 0) end = length;
				String subExt = ext.substring(start, end).trim();
				if (subExt.length() > 0) {
					subExt += comma;
					if (mask.indexOf(comma + subExt) == -1) mask += subExt;
				}
				start = end + 1;
			} while (end < length);
		}
		mask = mask.substring(1, Math.max(1, mask.length() - 1));
	}
	byte [] file_spec = Converter.wcsToMbcs (null, mask, true);
	byte [] btn1_text = null;
	if ((style & SWT.SAVE) != 0) {
		btn1_text = Converter.wcsToMbcs(null, SWT.getMessage("SWT_Save"), true);
	}
	int flags = OS.Pt_FSR_NO_FCHECK;
	PtFileSelectionInfo_t info = new PtFileSelectionInfo_t ();
	OS.PtFileSelection (parentHandle, null, title, root_dir, file_spec, btn1_text, null, null, info, flags);
	if (info.ret == OS.Pt_FSDIALOG_BTN2) return null;
	int length = 0;
	while (length < info.path.length && info.path [length] != 0) length++;
	byte [] path = new byte [length];
	System.arraycopy (info.path, 0, path, 0, length);
	String fullPath = new String (Converter.mbcsToWcs (null, path));
	length = fullPath.length ();
	if (length != 0) {
		int index = length - 1;
		while (index >= 0 && (fullPath.charAt (index) != '/')) --index;
		fileName = fullPath.substring (index + 1, length);
		filterPath = fullPath.substring (0, index);
	}
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

}

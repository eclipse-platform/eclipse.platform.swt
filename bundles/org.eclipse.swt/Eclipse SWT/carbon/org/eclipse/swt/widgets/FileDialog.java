package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.carbon.*;
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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public /*final*/ class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String filterPath = "";
	String [] fileNames = new String[] { "" };
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
}

/**
 * Returns the path of the first file that was
 * selected in the dialog relative to the filter path,
 * or empty string if the dialog was cancelled.
 * 
 * @return the relative path of the file
 */
public String getFileName () {
	if (fileNames.length > 0)
		return fileNames[0];
	return "";
}

/**
 * Returns the paths of all files that were selected
 * in the dialog relative to the filter path, or null
 * if none are available.
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

	int dialog= 0;
	String result= null;
	
	int titleHandle= 0;
	try {
		int parentWindowHandle= 0;
		if (parent != null)
			parentWindowHandle= parent.shellHandle;
		
		titleHandle= OS.CFStringCreateWithCharacters(title);

		int status= 0;
		int flags= 0;
		int[] dialogHandle= new int[1];
		
		if ((style & SWT.SAVE) != 0) {
			status= OS.NavCreatePutFileDialog(flags, titleHandle, parentWindowHandle, dialogHandle, OS.OSType("TEXT"), OS.OSType("KAHL"));
		} else /* if ((style & SWT.OPEN) != 0) */ {
			if ((style & SWT.MULTI) != 0)
				flags |= OS.kNavAllowMultipleFiles;
			status= OS.NavCreateGetFileDialog(flags, titleHandle, parentWindowHandle, dialogHandle);
		}
		
		if (status == 0) {
			dialog= dialogHandle[0];
		} else {
			//System.out.println("FileDialog.open: status " + status);
		}
		
		if (dialog != 0) {
			//System.out.println("FileDialog.open: got dialog");
		
			if ((style & SWT.SAVE) != 0) {
				int fileNameHandle= 0;
				try {
					fileNameHandle= OS.CFStringCreateWithCharacters(fileNames[0]);
					OS.NavDialogSetSaveFileName(dialog, fileNameHandle);
				} finally {
					if (fileNameHandle != 0)
						OS.CFRelease(fileNameHandle);
				}
			}
		
			//System.out.println("FileDialog.open: vor run");
			OS.NavDialogRun(dialog);
			//System.out.println("FileDialog.open: nach run");
		
			int action= OS.NavDialogGetUserAction(dialog);
			switch (action) {
			case OS.kNavUserActionCancel:
				break;
				
			case OS.kNavUserActionOpen:
			case OS.kNavUserActionChoose:			
				fileNames= getPaths(dialog);
				if (fileNames.length > 0)
					result= fileNames[0];
				break;
				
			case OS.kNavUserActionSaveAs:
				result= MacUtil.getStringAndRelease(OS.NavDialogGetSaveFileName(dialog));
				break;
			}
		} else {
			//System.out.println("FileDialog.open: dialog == null");
		}

	} finally {
		if (titleHandle != 0)
			OS.CFRelease(titleHandle);
		if (dialog != 0)
			OS.NavDialogDispose(dialog);
	}
	
	return result;
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
	fileNames = new String[] { string };
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

////////////////////
// Mac stuff
////////////////////

	static String[] getPaths(int dialog) {
		int[] tmp= new int[1];
		OS.NavDialogGetReply(dialog, tmp);
		int reply= tmp[0];
		
		int selection= OS.NavReplyRecordGetSelection(reply);
		OS.AECountItems(selection, tmp);
		int count= tmp[0];
		
		String[] result= new String[count];
		for (int i= 0; i < count; i++) {
			OS.AEGetNthPtr(selection, i+1, tmp);
			result[i]= MacUtil.getStringAndRelease(tmp[0]);
		}
		
		OS.NavDialogDisposeReply(reply);
		
		return result;
	}

}

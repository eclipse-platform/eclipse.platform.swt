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

 
import java.io.*; 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
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
public class FileDialog extends Dialog {
	int dialog;
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String [] fileNames = new String [0];
	String fileName = "";
	String filterPath = "";
	String fullPath;
	boolean cancel = false;
	static final String FILTER = "*";
	static final String SEPARATOR = System.getProperty ("file.separator");

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

int cancelPressed (int widget, int client, int call) {
	cancel = true;
	OS.XtUnmanageChild (widget);
	return 0;
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

int itemSelected (int widget, int client, int call) {
	/* this callback will only be hooked if the dialog style is MULTI */
	int fileList = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_LIST);
	if (fileList == 0) return 0;
	int selectionText = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_TEXT);
	if (selectionText == 0) return 0;
	
	int [] argList = {OS.XmNselectedItems, 0, OS.XmNselectedItemCount, 0};
	OS.XtGetValues (fileList, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	int ptr = 0;
	if (itemCount == 0) {
		int [] argList2 = {OS.XmNdirectory, 0};
		OS.XtGetValues (dialog, argList2, argList2.length / 2);
		ptr = argList2 [1];
	} else {
		int [] buffer = new int [1];
		OS.memmove (buffer, items, 4);
		ptr = buffer [0];
	}
	if (ptr == 0) return 0;
	Display display = parent.getDisplay ();
	int [] table = new int [] {display.tabMapping, display.crMapping};
	int address = OS.XmStringUnparse (
		ptr,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		table,
		table.length,
		OS.XmOUTPUT_ALL);
	if (itemCount == 0) OS.XmStringFree (ptr);
	if (address == 0) return 0;
	int length = OS.strlen (address);
	byte [] buffer = new byte [length + 1];
	OS.memmove (buffer, address, length);
	OS.XtFree (address);
	
	OS.XmTextSetString (selectionText, buffer);
	OS.XmTextSetInsertionPosition (selectionText, OS.XmTextGetLastPosition (selectionText));
	return 0;
}

int okPressed (int widget, int client, int call) {
	String fullPath = null, fileName = null;
	String [] fileNames = null;

	int [] argList = {OS.XmNdirSpec, 0, OS.XmNdirectory, 0};
	OS.XtGetValues (dialog, argList, argList.length / 2);
	
	int xmString1 = argList [1];
	Display display = parent.getDisplay ();
	int [] table = new int [] {display.tabMapping, display.crMapping};
	int ptr = OS.XmStringUnparse (
		xmString1,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		table,
		table.length,
		OS.XmOUTPUT_ALL);
	if (ptr != 0) {
		int length = OS.strlen (ptr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		OS.XtFree (ptr);
		/* Use the character encoding for the default locale */
		fullPath = new String (Converter.mbcsToWcs (null, buffer)).trim();
	}
	OS.XmStringFree (xmString1);
	
	if ((style & SWT.MULTI) != 0) {
		int fileList = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_LIST);
		if (fileList == 0) return 0;
		int [] argList2 = {OS.XmNselectedItems, 0, OS.XmNselectedItemCount, 0};
		OS.XtGetValues (fileList, argList2, argList2.length / 2);
		int items = argList2 [1], itemCount = argList2 [3];
		int [] buffer1 = new int [1];
		fileNames = new String [itemCount];
		boolean match = false;
		for (int i = 0; i < itemCount; i++) {
			OS.memmove (buffer1, items, 4);
			ptr = buffer1 [0];
			int address = OS.XmStringUnparse (
				ptr,
				null,
				OS.XmCHARSET_TEXT,
				OS.XmCHARSET_TEXT,
				table,
				table.length,
				OS.XmOUTPUT_ALL);
			if (address != 0) {
				int length = OS.strlen (address);
				byte [] buffer = new byte [length];
				OS.memmove (buffer, address, length);
				OS.XtFree (address);
				/* Use the character encoding for the default locale */
				String fullFilename = new String (Converter.mbcsToWcs (null, buffer));
				int index = fullFilename.lastIndexOf (SEPARATOR);
				fileNames [i] = fullFilename.substring (index + 1, fullFilename.length ());
				if (fullFilename.equals (fullPath)) match = true;
			}
			items += 4;
		}
		if (match) {
			fileName = fileNames [0];
		} else {
			/* The user has modified the text field such that it doesn't match any
			 * of the selected files, so use this value instead
			 */
			int index = fullPath.lastIndexOf (SEPARATOR);
			fileName = fullPath.substring (index + 1, fullPath.length ());
			fileNames = new String [] {fileName};
		}
	} else {
		int index = fullPath.lastIndexOf (SEPARATOR);
		fileName = fullPath.substring (index + 1, fullPath.length ());
		fileNames = new String [] {fileName};
	}

	// if no file selected then go into the current directory
	if (fileName.equals("")) {
		int [] argList1 = {OS.XmNdirMask, 0};
		OS.XtGetValues (dialog, argList1, argList1.length / 2);
		int directoryHandle = argList1[1];
		int [] argList2 = {OS.XmNpattern,directoryHandle};
		OS.XtSetValues (dialog, argList2, argList2.length / 2);
		OS.XmStringFree (directoryHandle);
		return 0;
	}		
	
	int xmString2 = argList [3];
	ptr = OS.XmStringUnparse (
		xmString2,
		null,
		OS.XmCHARSET_TEXT,
		OS.XmCHARSET_TEXT,
		table,
		table.length,
		OS.XmOUTPUT_ALL);
	if (ptr != 0) {
		int length = OS.strlen (ptr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		OS.XtFree (ptr);
		/* Use the character encoding for the default locale */
		filterPath = new String (Converter.mbcsToWcs (null, buffer));
	}
	OS.XmStringFree (xmString2);
	if (filterPath.endsWith (SEPARATOR)) {
		filterPath = filterPath.substring (0, filterPath.length () - 1);
	}

	this.fullPath = fullPath;
	this.fileName = fileName;
	this.fileNames = fileNames;
	OS.XtUnmanageChild (widget);
	return 0;
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

	/* Get the parent */
	boolean destroyContext;
	Display appContext = Display.getCurrent ();
	if (destroyContext = (appContext == null)) appContext = new Display ();
	int display = appContext.xDisplay;
	int parentHandle = appContext.shellHandle;
	if ((parent != null) && (parent.display == appContext)) {
		if (OS.IsAIX) parent.realizeWidget ();		/* Fix for bug 17507 */
		parentHandle = parent.shellHandle;
	}

	/* Compute the dialog title */	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	String string = title;
	if (string.length () == 0) string = " ";
	/* Use the character encoding for the default locale */
	byte [] buffer1 = Converter.wcsToMbcs (null, string, true);
	int xmStringPtr1 = OS.XmStringParseText (
		buffer1,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	fullPath = null;
	fileNames = new String [0];
	
	/* Compute the filter */
	String mask = FILTER;
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions.length != 0) {
		/* Motif does not support multiple filters, so ignore them
		 * if there are more than one, or if there is a ; separator.
		 */
		if (filterExtensions.length == 1) {
			String filter = filterExtensions [0];
			if (filter.indexOf (';', 0) == -1) mask = filter;
		}
	}
	/* Use the character encoding for the default locale */
	byte [] buffer2 = Converter.wcsToMbcs (null, mask, true);
	int xmStringPtr2 = OS.XmStringParseText (
		buffer2,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Compute the filter path */
	if (filterPath == null) filterPath = "";
	if (!filterPath.endsWith (SEPARATOR)) {
		File dir = new File (filterPath);
		if (dir.exists () && dir.isDirectory ()) filterPath += SEPARATOR;
	}
	/* Use the character encoding for the default locale */
	byte [] buffer3 = Converter.wcsToMbcs (null, filterPath, true);
	int xmStringPtr3 = OS.XmStringParseText (
		buffer3,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);

	/* Create the dialog */
	int [] argList1 = {
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNdialogStyle, OS.XmDIALOG_PRIMARY_APPLICATION_MODAL,
		OS.XmNwidth, OS.XDisplayWidth (display, OS.XDefaultScreen (display)) * 4 / 9,
		OS.XmNpathMode, OS.XmPATH_MODE_FULL,
		OS.XmNdialogTitle, xmStringPtr1,
		OS.XmNpattern, xmStringPtr2,
		OS.XmNdirMask, xmStringPtr3,
	};
	/*
	* Feature in Linux.  For some reason, the XmCreateFileSelectionDialog()
	* will not accept NULL for the widget name.  This works fine on the other
	* Motif platforms and in the other XmCreate calls on Linux.  The fix is
	* to pass in a NULL terminated string, not a NULL pointer.
	*/
	byte [] name = new byte [] {0};
	dialog = OS.XmCreateFileSelectionDialog (parentHandle, name, argList1, argList1.length / 2);
	int child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_HELP_BUTTON);
	if (child != 0) OS.XtUnmanageChild (child);
	OS.XmStringFree (xmStringPtr1);
	OS.XmStringFree (xmStringPtr2);
	OS.XmStringFree (xmStringPtr3);
	
	/*
	 * Can override the selection text field if necessary now that
	 * its initial value has been computed by the platform dialog.
	 */
	if (fileName != null && fileName.length() > 0) {
		/* Use the character encoding for the default locale */
		byte [] buffer4 = Converter.wcsToMbcs (null, fileName, true);
		int xmStringPtr4 = OS.XmStringParseText (
				buffer4,
				0,
				OS.XmFONTLIST_DEFAULT_TAG, 
				OS.XmCHARSET_TEXT, 
				null,
				0,
				0);
		int [] argList2 = {OS.XmNdirSpec, 0};
		OS.XtGetValues (dialog, argList2, argList2.length / 2);
		int oldDirSpec = argList2 [1];
		int newDirSpec = OS.XmStringConcat (oldDirSpec, xmStringPtr4);
		argList2 [1] = newDirSpec;
		OS.XtSetValues (dialog, argList2, argList2.length / 2);
		OS.XmStringFree (xmStringPtr4);
		OS.XmStringFree (oldDirSpec);
		OS.XmStringFree (newDirSpec);
	}

	/* Hook the callbacks. */
	Callback cancelCallback = new Callback (this, "cancelPressed", 3);
	int cancelAddress = cancelCallback.getAddress ();
	OS.XtAddCallback (dialog, OS.XmNcancelCallback, cancelAddress, 0);
	Callback okCallback = new Callback (this, "okPressed", 3);
	int okAddress = okCallback.getAddress ();
	OS.XtAddCallback (dialog, OS.XmNokCallback, okAddress, 0);
	Callback selectCallback = null;
	if ((style & SWT.MULTI) != 0) {
		child = OS.XmFileSelectionBoxGetChild (dialog, OS.XmDIALOG_LIST);
		if (child != 0) {
			int [] argList3 = {OS.XmNselectionPolicy, OS.XmEXTENDED_SELECT};
			OS.XtSetValues(child, argList3, argList3.length / 2);
			selectCallback = new Callback (this, "itemSelected", 3);
			int selectAddress = selectCallback.getAddress ();
			if (selectAddress == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
			OS.XtAddCallback (child, OS.XmNextendedSelectionCallback, selectAddress, 0);
		}
	}

	OS.XtManageChild (dialog);

	// Should be a pure OS message loop (no SWT AppContext)
	while (OS.XtIsRealized (dialog) && OS.XtIsManaged (dialog))
		if (!appContext.readAndDispatch ()) appContext.sleep ();

	/* Destroy the dialog and update the display. */
	if (OS.XtIsRealized (dialog)) OS.XtDestroyWidget (dialog);
	if (destroyContext) appContext.dispose ();
	okCallback.dispose ();
	cancelCallback.dispose ();
	if (selectCallback != null) selectCallback.dispose ();
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

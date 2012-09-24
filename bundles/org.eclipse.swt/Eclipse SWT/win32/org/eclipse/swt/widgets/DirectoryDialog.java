/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select a directory.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#directorydialog">DirectoryDialog snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class DirectoryDialog extends Dialog {
	String message = "", filterPath = "";  //$NON-NLS-1$//$NON-NLS-2$
	String directoryPath;
	
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
public DirectoryDialog (Shell parent) {
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
 */
public DirectoryDialog (Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}

long /*int*/ BrowseCallbackProc (long /*int*/ hwnd, long /*int*/ uMsg, long /*int*/ lParam, long /*int*/ lpData) {
	switch ((int)/*64*/uMsg) {
		case OS.BFFM_INITIALIZED:
			if (filterPath != null && filterPath.length () != 0) {
				/* Use the character encoding for the default locale */
				TCHAR buffer = new TCHAR (0, filterPath.replace ('/', '\\'), true);
				OS.SendMessage (hwnd, OS.BFFM_SETSELECTION, 1, buffer);
			}
			if (title != null && title.length () != 0) {
				/* Use the character encoding for the default locale */
				TCHAR buffer = new TCHAR (0, title, true);
				OS.SetWindowText (hwnd, buffer);
			}
			break;
		case OS.BFFM_VALIDATEFAILEDA:
		case OS.BFFM_VALIDATEFAILEDW:
			/* Use the character encoding for the default locale */
			int length = OS.IsUnicode ? OS.wcslen (lParam) : OS.strlen (lParam);
			TCHAR buffer = new TCHAR (0, length);
			int byteCount = buffer.length () * TCHAR.sizeof;
			OS.MoveMemory (buffer, lParam, byteCount);
			directoryPath = buffer.toString (0, length);
			break;
	}
	return 0;
}

/**
 * Returns the path which the dialog will use to filter
 * the directories it shows.
 *
 * @return the filter path
 * 
 * @see #setFilterPath
 */
public String getFilterPath () {
	return filterPath;
}

/**
 * Returns the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @return the message
 */
public String getMessage () {
	return message;
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the selected directory,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	if (OS.IsWinCE) error (SWT.ERROR_NOT_IMPLEMENTED);
	
	long /*int*/ hHeap = OS.GetProcessHeap ();
	
	/* Get the owner HWND for the dialog */
	long /*int*/ hwndOwner = 0;
	if (parent != null) hwndOwner = parent.handle;

	/* Copy the message to OS memory */
	long /*int*/ lpszTitle = 0;
	if (message.length () != 0) {
		String string = message;
		if (string.indexOf ('&') != -1) {
			int length = string.length ();
			char [] buffer = new char [length * 2];
			int index = 0;
			for (int i=0; i<length; i++) {
				char ch = string.charAt (i);
				if (ch == '&') buffer [index++] = '&';
				buffer [index++] = ch;
			}
			string = new String (buffer, 0, index);
		}
		/* Use the character encoding for the default locale */
		TCHAR buffer = new TCHAR (0, string, true);
		int byteCount = buffer.length () * TCHAR.sizeof;
		lpszTitle = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpszTitle, buffer, byteCount);
	}

	/* Create the BrowseCallbackProc */
	Callback callback = new Callback (this, "BrowseCallbackProc", 4); //$NON-NLS-1$
	long /*int*/ lpfn = callback.getAddress ();
	if (lpfn == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	/* Make the parent shell be temporary modal */
	Dialog oldModal = null;
	Display display = parent.getDisplay ();
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		oldModal = display.getModalDialog ();
		display.setModalDialog (this);
	}
	
	directoryPath = null;
	BROWSEINFO lpbi = new BROWSEINFO ();
	lpbi.hwndOwner = hwndOwner;
	lpbi.lpszTitle = lpszTitle;
	lpbi.ulFlags = OS.BIF_NEWDIALOGSTYLE | OS.BIF_RETURNONLYFSDIRS | OS.BIF_EDITBOX | OS.BIF_VALIDATE;
	lpbi.lpfn = lpfn;
	/*
	* Bug in Windows.  On some hardware configurations, SHBrowseForFolder()
	* causes warning dialogs with the message "There is no disk in the drive
	* Please insert a disk into \Device\Harddisk0\DR0".  This is possibly
	* caused by SHBrowseForFolder() calling internally GetVolumeInformation().
	* MSDN for GetVolumeInformation() says:
	* 
	* "If you are attempting to obtain information about a floppy drive
	* that does not have a floppy disk or a CD-ROM drive that does not 
	* have a compact disc, the system displays a message box asking the 
	* user to insert a floppy disk or a compact disc, respectively. 
	* To prevent the system from displaying this message box, call the 
	* SetErrorMode function with SEM_FAILCRITICALERRORS."
	* 
	* The fix is to save and restore the error mode using SetErrorMode()
	* with the SEM_FAILCRITICALERRORS flag around SHBrowseForFolder().
	*/
	int oldErrorMode = OS.SetErrorMode (OS.SEM_FAILCRITICALERRORS);
	
	/*
	* Bug in Windows.  When a WH_MSGFILTER hook is used to run code
	* during the message loop for SHBrowseForFolder(), running code
	* in the hook can cause a GP.  Specifically, SetWindowText()
	* for static controls seemed to make the problem happen.
	* The fix is to disable async messages while the directory
	* dialog is open.
	* 
	* NOTE:  This only happens in versions of the comctl32.dll
	* earlier than 6.0.
	*/
	boolean oldRunMessages = display.runMessages;
	if (OS.COMCTL32_MAJOR < 6) display.runMessages = false;
	long /*int*/ lpItemIdList = OS.SHBrowseForFolder (lpbi);
	if (OS.COMCTL32_MAJOR < 6) display.runMessages = oldRunMessages;
	OS.SetErrorMode (oldErrorMode);
	
	/* Clear the temporary dialog modal parent */
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		display.setModalDialog (oldModal);
	}
	
	boolean success = lpItemIdList != 0;
	if (success) {
		/* Use the character encoding for the default locale */
		TCHAR buffer = new TCHAR (0, OS.MAX_PATH);
		if (OS.SHGetPathFromIDList (lpItemIdList, buffer)) {
			directoryPath = buffer.toString (0, buffer.strlen ());
			filterPath = directoryPath;
		}
	}

	/* Free the BrowseCallbackProc */
	callback.dispose ();
	
	/* Free the OS memory */
	if (lpszTitle != 0) OS.HeapFree (hHeap, 0, lpszTitle);

	/* Free the pointer to the ITEMIDLIST */
	long /*int*/ [] ppMalloc = new long /*int*/ [1];
	if (OS.SHGetMalloc (ppMalloc) == OS.S_OK) {
		/* void Free (struct IMalloc *this, void *pv); */
		OS.VtblCall (5, ppMalloc [0], lpItemIdList);
	}
	
	/*
	* This code is intentionally commented.  On some
	* platforms, the owner window is repainted right
	* away when a dialog window exits.  This behavior
	* is currently unspecified.
	*/
//	if (hwndOwner != 0) OS.UpdateWindow (hwndOwner);
	
	/* Return the directory path */
	if (!success) return null;
	return directoryPath;
}

/**
 * Sets the path that the dialog will use to filter
 * the directories it shows to the argument, which may
 * be null. If the string is null, then the operating
 * system's default filter path will be used.
 * <p>
 * Note that the path string is platform dependent.
 * For convenience, either '/' or '\' can be used
 * as a path separator.
 * </p>
 *
 * @param string the filter path
 */
public void setFilterPath (String string) {
	filterPath = string;
}

/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @param string the message
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 */
public void setMessage (String string) {
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	message = string;
}

}

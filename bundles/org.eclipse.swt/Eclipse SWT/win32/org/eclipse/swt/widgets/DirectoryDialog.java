/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Martin Karpisek <martin.karpisek@gmail.com> - Bug 443250
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

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
public String open() {
	this.directoryPath = null;

	long [] ppv = new long [1];
	if (COM.CoCreateInstance(COM.CLSID_FileOpenDialog, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_IFileOpenDialog, ppv) == OS.S_OK) {
		IFileDialog fileDialog = new IFileDialog(ppv[0]);

		int[] options = new int[1];
		if (fileDialog.GetOptions(options) == OS.S_OK) {
			options[0] |= OS.FOS_PICKFOLDERS | OS.FOS_FORCEFILESYSTEM | OS.FOS_NOCHANGEDIR;
			fileDialog.SetOptions(options[0]);
		}

		if (title == null) title = "";
		if (title.length() > 0) {
			char[] buffer = new char[title.length() + 1];
			title.getChars(0, title.length(), buffer, 0);
			fileDialog.SetTitle(buffer);
		}

		if (filterPath != null && filterPath.length() > 0) {
			String path = filterPath.replace('/', '\\');
			char[] buffer = new char[path.length() + 1];
			path.getChars(0, path.length(), buffer, 0);
			if (COM.SHCreateItemFromParsingName(buffer, 0, COM.IID_IShellItem, ppv) == OS.S_OK) {
				IShellItem psi = new IShellItem(ppv[0]);
				/*
				 * SetDefaultDirectory does not work if the dialog has
				 * persisted recently used folder. The fix is to clear the
				 * persisted data.
				 */
				fileDialog.ClearClientData();
				fileDialog.SetDefaultFolder(psi);
				psi.Release();
			}
		}

		Display display = parent.getDisplay();
		long hwndOwner = parent.handle;
		display.externalEventLoop = true;
		if (fileDialog.Show(hwndOwner) == OS.S_OK) {
			if (fileDialog.GetResult(ppv) == OS.S_OK) {
				IShellItem psi = new IShellItem(ppv[0]);
				if (psi.GetDisplayName(OS.SIGDN_FILESYSPATH, ppv) == OS.S_OK) {
					long wstr = ppv[0];
					int length = OS.wcslen(wstr);
					char[] buffer = new char[length];
					OS.MoveMemory(buffer, wstr, length * 2);
					OS.CoTaskMemFree(wstr);

					directoryPath = new String(buffer);
				}
				psi.Release();
			}
		}
		display.externalEventLoop = false;

		fileDialog.Release();
	}

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
 * <p>
 * NOTE: This operation is a hint and is not supported on some platforms. For
 * example, on Windows (Vista and later), the <code>DirectoryDialog</code>
 * doesn't have any provision to set a message.
 * </p>
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

/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.nio.file.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

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
	String filterPath = "", fileName = "";
	int filterIndex = 0;
	boolean overwrite = false;
	static final String DEFAULT_FILTER = "*.*";
	static final String LONG_PATH_PREFIX = "\\\\?\\";

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

static Path getItemPath (IShellItem psi) {
	long [] ppsz = new long [1];
	if (psi.GetDisplayName(OS.SIGDN_FILESYSPATH, ppsz) == COM.S_OK) {
		int length = OS.wcslen(ppsz[0]);
		char[] buffer = new char[length];
		OS.MoveMemory(buffer, ppsz[0], length * Character.BYTES);
		OS.CoTaskMemFree(ppsz[0]);
		String path = String.valueOf(buffer);
		/* Feature in Windows. Paths longer than MAX_PATH are returned with a \\?\ prefix */
		if (path.startsWith(LONG_PATH_PREFIX)) {
			path = path.substring(LONG_PATH_PREFIX.length());
		}
		return Paths.get(path);
	}
	return null;
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
	/* Create Common Item Dialog */
	long[] ppv = new long[1];
	int hr;
	if ((style & SWT.SAVE) != 0) {
		hr = COM.CoCreateInstance(COM.CLSID_FileSaveDialog, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_IFileSaveDialog, ppv);
	} else {
		hr = COM.CoCreateInstance(COM.CLSID_FileOpenDialog, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_IFileOpenDialog, ppv);
	}
	if (hr != COM.S_OK) SWT.error(SWT.ERROR_NO_HANDLES);
	IFileDialog fileDialog = new IFileDialog(ppv[0]);

	/* Update dialog options */
	int[] options = new int[1];
	fileDialog.GetOptions(options);
	options[0] |= OS.FOS_FORCEFILESYSTEM | OS.FOS_NOCHANGEDIR;
	options[0] &= ~OS.FOS_FILEMUSTEXIST;
	if ((style & SWT.SAVE) != 0) {
		if (!overwrite) options[0] &= ~OS.FOS_OVERWRITEPROMPT;
	} else {
		if ((style & SWT.MULTI) != 0) options[0] |= OS.FOS_ALLOWMULTISELECT;
	}
	fileDialog.SetOptions(options[0]);

	/* Set dialog title */
	if (!title.isEmpty()) {
		fileDialog.SetTitle((title + "\0").toCharArray());
	}

	/* Apply extension filters */
	String[] filterExtensions = this.filterExtensions;
	String[] filterNames = this.filterNames;
	if (filterExtensions == null || filterExtensions.length == 0) {
		filterExtensions = filterNames = new String[] { DEFAULT_FILTER };
	}
	long hHeap = OS.GetProcessHeap();
	long[] filterSpec = new long[filterExtensions.length * 2];
	for (int i = 0; i < filterExtensions.length; i++) {
		String extension = filterExtensions[i];
		String name = (filterNames != null && i < filterNames.length) ? filterNames[i] : extension;
		/*
		 * Feature in Windows. If a filter name doesn't contain "*.", FileDialog appends
		 * the filter pattern to the name. This might cause filters like (*) to appear
		 * twice. The fix is to strip the pattern and let FileDialog re-append it.
		 *
		 * Note: Registry entry for "Hide extensions for known file types" needs to be
		 * checked before we apply above work-around.
		 */
		if (!name.contains("*.")) {
			/* By default value is on, in case of missing registry entry assumed to be on */
			int[] result = OS.readRegistryDwords(OS.HKEY_CURRENT_USER,
					"Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Advanced", "HideFileExt");
			if (result != null && result[0] == 0) {
				name = name.replace(" (" + extension + ")", "");
			}
		}
		long lpstrName = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, (name.length() + 1) * Character.BYTES);
		long lpstrExt = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, (extension.length() + 1) * Character.BYTES);
		OS.MoveMemory(lpstrName, name.toCharArray(), name.length() * Character.BYTES);
		OS.MoveMemory(lpstrExt, extension.toCharArray(), extension.length() * Character.BYTES);
		filterSpec[i*2] = lpstrName;
		filterSpec[i*2 + 1] = lpstrExt;
	}
	fileDialog.SetFileTypes(filterExtensions.length, filterSpec);
	for (int i = 0; i < filterSpec.length; i++) {
		OS.HeapFree(hHeap, 0, filterSpec[i]);
	}

	/* Enable automatic appending of extensions to saved file names */
	fileDialog.SetDefaultExtension(new char[1]);

	/* Set initial filter */
	fileDialog.SetFileTypeIndex(filterIndex + 1);

	/* Set initial folder */
	if (filterPath != null) {
		char[] path = (filterPath.replace('/', '\\') + "\0").toCharArray();
		if (COM.SHCreateItemFromParsingName(path, 0, COM.IID_IShellItem, ppv) == COM.S_OK) {
			IShellItem psi = new IShellItem(ppv[0]);
			if (filterPath.length() > 0) {
				fileDialog.SetFolder(psi);
			}
			else {
				fileDialog.SetDefaultFolder(psi);
			}
			psi.Release();
		}
	}

	/* Set initial filename */
	if (fileName != null) {
		char[] name = (fileName.replace('/', '\\') + "\0").toCharArray();
		fileDialog.SetFileName(name);
	}

	/* Make the parent shell be temporary modal */
	Dialog oldModal = null;
	Display display = parent.getDisplay();
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		oldModal = display.getModalDialog();
		display.setModalDialog(this);
	}

	/* Open the dialog */
	display.externalEventLoop = true;
	display.sendPreExternalEventDispatchEvent();
	hr = fileDialog.Show(parent.handle);
	display.externalEventLoop = false;
	display.sendPostExternalEventDispatchEvent();

	/* Clear the temporary dialog modal parent */
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		display.setModalDialog(oldModal);
	}

	/* Extract result paths */
	String fullPath = null;
	fileNames = new String[0];
	if (hr == COM.S_OK) {
		if ((style & SWT.SAVE) != 0) {
			if (fileDialog.GetResult(ppv) == COM.S_OK) {
				IShellItem psi = new IShellItem(ppv[0]);
				Path itemPath = getItemPath(psi);
				psi.Release();
				fileName = itemPath.getFileName().toString();
				filterPath = itemPath.getParent().toString();
				fileNames = new String[] { fileName };
				fullPath = itemPath.toString();
			}
		} else {
			if (fileDialog.GetResults(ppv) == COM.S_OK) {
				IShellItemArray psia = new IShellItemArray(ppv[0]);
				int[] piCount = new int[1];
				psia.GetCount(piCount);
				fileNames = new String[piCount[0]];
				Path parentPath = null;
				for (int i = 0; i < piCount[0]; i++) {
					psia.GetItemAt(i, ppv);
					IShellItem psi = new IShellItem(ppv[0]);
					Path itemPath = getItemPath(psi);
					psi.Release();
					if (parentPath == null) {
						parentPath = itemPath.getParent();
						filterPath = parentPath.toString();
						fullPath = itemPath.toString();
					}
					/*
					 * Feature in Windows. Returned items might have different parent folders.
					 * (E.g. when selecting from a virtual folder like Recent Files).
					 * Return full paths names in this case.
					 */
					if (itemPath.getParent().equals(parentPath)) {
						fileNames[i] = itemPath.getFileName().toString();
					} else {
						fileNames[i] = itemPath.toString();
					}
				}
				fileName = fileNames[0];
				psia.Release();
			}
		}

		int[] piIndex = new int[1];
		if (fileDialog.GetFileTypeIndex(piIndex) == COM.S_OK) {
			filterIndex = piIndex[0] - 1;
		}
	}

	fileDialog.Release();

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
 * <p>
 * Note: On Mac, setting the file extension filter affects how
 * app bundles are treated by the dialog. When a filter extension
 * having the app extension (.app) is selected, bundles are treated
 * as files. For all other extension filters, bundles are treated
 * as directories. When no filter extension is set, bundles are
 * treated as files.
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
 * <p>
 * Note: On some platforms where suppressing the overwrite prompt
 * is not supported, the prompt is shown even when invoked with
 * overwrite false.
 * </p>
 *
 * @param overwrite true if the dialog will prompt for file overwrite, false otherwise
 *
 * @since 3.4
 */
public void setOverwrite (boolean overwrite) {
	this.overwrite = overwrite;
}

}

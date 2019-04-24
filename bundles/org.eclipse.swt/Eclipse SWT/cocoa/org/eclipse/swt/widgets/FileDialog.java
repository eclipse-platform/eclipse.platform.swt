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
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

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
	Callback callback_completion_handler;
	Callback callback_overwrite_existing_file;
	NSSavePanel panel;
	NSPopUpButton popup;
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String [] fileNames = new String[0];
	String filterPath = "", fileName = "";
	String fullPath;
	SWTPanelDelegate delegate = null;
	int filterIndex = -1;
	long jniRef = 0;
	long method = 0;
	long methodImpl = 0;
	boolean overwrite = false;
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
	if (Display.getSheetEnabled ()) {
		if (parent != null && (style & SWT.SHEET) != 0) this.style |= SWT.SHEET;
	}
	checkSubclass ();
}

long _completionHandler (long result) {
	handleResponse(result);
	return result;
}

long _overwriteExistingFileCheck (long id, long sel, long str) {
	return 1;
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

void handleResponse (long response) {
	if (parent != null && (style & SWT.SHEET) != 0) {
		NSApplication.sharedApplication().stopModal();
	}
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	display.setModalDialog(null);

	if (popup != null) {
		filterIndex = (int)popup.indexOfSelectedItem();
	} else {
		filterIndex = -1;
	}

	if (response == OS.NSFileHandlingPanelOKButton) {
		NSString filename = panel.filename();
		if ((style & SWT.SAVE) != 0) {
			fullPath = filename.getString();
			fileNames = new String [1];
			fileName = fileNames [0] = filename.lastPathComponent().getString();
			filterPath = filename.stringByDeletingLastPathComponent().getString();
		} else {
			fullPath = filename.getString();
			NSArray filenames = ((NSOpenPanel)panel).filenames();
			int count = (int)filenames.count();
			fileNames = new String[count];

			for (int i = 0; i < count; i++) {
				filename = new NSString(filenames.objectAtIndex(i));
				NSString filenameOnly = filename.lastPathComponent();
				NSString pathOnly = filename.stringByDeletingLastPathComponent();

				if (i == 0) {
					/* Filter path */
					filterPath = pathOnly.getString();

					/* File name */
					fileName = fileNames [0] = filenameOnly.getString();
				} else {
					if (pathOnly.getString().equals (filterPath)) {
						fileNames [i] = filenameOnly.getString();
					} else {
						fileNames [i] = filename.getString();
					}
				}
			}
		}
	}
	releaseHandles();
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
	fullPath = null;
	if ((style & SWT.SAVE) != 0) {
		NSSavePanel savePanel = NSSavePanel.savePanel();
		panel = savePanel;
		if (!overwrite) {
			callback_overwrite_existing_file = new Callback(this, "_overwriteExistingFileCheck", 3);
			long proc = callback_overwrite_existing_file.getAddress();
			if (proc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
			method = OS.class_getInstanceMethod(OS.class_NSSavePanel, OS.sel_overwriteExistingFileCheck);
			if (method != 0) methodImpl = OS.method_setImplementation(method, proc);
		}
	} else {
		NSOpenPanel openPanel = NSOpenPanel.openPanel();
		openPanel.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
		panel = openPanel;
	}

	panel.setCanCreateDirectories(true);
	/*
	 * This line is intentionally commented. Don't show hidden files forcefully,
	 * instead allow File dialog to use the system preference.
	 */
	//	OS.objc_msgSend(panel.id, OS.sel_setShowsHiddenFiles_, true);
	jniRef = 0;
	delegate = null;
	if (filterExtensions != null && filterExtensions.length != 0) {
		delegate = (SWTPanelDelegate)new SWTPanelDelegate().alloc().init();
		jniRef = OS.NewGlobalRef(this);
		if (jniRef == 0) error(SWT.ERROR_NO_HANDLES);
		OS.object_setInstanceVariable(delegate.id, Display.SWT_OBJECT, jniRef);
		panel.setDelegate(delegate);
		NSPopUpButton widget = (NSPopUpButton)new NSPopUpButton().alloc();
		widget.initWithFrame(new NSRect(), false);
		widget.setTarget(delegate);
		widget.setAction(OS.sel_sendSelection_);
		NSMenu menu = widget.menu();
		menu.setAutoenablesItems(false);
		for (int i = 0; i < filterExtensions.length; i++) {
			String str = filterExtensions [i];
			if (filterNames != null && filterNames.length > i) {
				str = filterNames [i];
			}
			NSMenuItem nsItem = (NSMenuItem)new NSMenuItem().alloc();
			nsItem.initWithTitle(NSString.stringWith(str), 0, NSString.string());
			menu.addItem(nsItem);
			nsItem.release();
		}
		int selectionIndex = 0 <= filterIndex && filterIndex < filterExtensions.length ? filterIndex : 0;
		widget.selectItemAtIndex(selectionIndex);
		widget.sizeToFit();
		panel.setAccessoryView(widget);
		popup = widget;

		setAllowedFileType(filterExtensions[selectionIndex]);
		panel.setAllowsOtherFileTypes(true);
		panel.setTreatsFilePackagesAsDirectories(shouldTreatAppAsDirectory(filterExtensions[selectionIndex]));
	} else {
		panel.setTreatsFilePackagesAsDirectories(false);
	}
	panel.setTitle(NSString.stringWith(title != null ? title : ""));
	if (filterPath != null && filterPath.length() > 0) {
		NSString dir = NSString.stringWith(filterPath);
		panel.setDirectoryURL(NSURL.fileURLWithPath(dir));
	}
	if (fileName != null && fileName.length() > 0) {
		panel.setNameFieldStringValue(NSString.stringWith(fileName));
	}

	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	display.setModalDialog(this, panel);
	if (parent != null && (style & SWT.SHEET) != 0) {
		callback_completion_handler = new Callback(this, "_completionHandler", 1);
		long handler = callback_completion_handler.getAddress();
		if (handler == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		OS.beginSheetModalForWindow(panel, parent.view.window(), handler);
		NSApplication.sharedApplication().runModalForWindow(parent.view.window());
	} else {
		long response = panel.runModal();
		handleResponse(response);
	}
	return fullPath;
}

long panel_shouldShowFilename (long id, long sel, long arg0, long arg1) {
	if ((style & SWT.SAVE) != 0) {
		/* All filenames are always disabled in the NSSavePanel, so return from here. */
		return 1;
	}
	NSString path = new NSString(arg1);
	if (filterExtensions != null && filterExtensions.length != 0) {
		NSFileManager manager = NSFileManager.defaultManager();
		long ptr = C.malloc(1);
		boolean found = manager.fileExistsAtPath(path, ptr);
		byte[] isDirectory = new byte[1];
		C.memmove(isDirectory, ptr, 1);
		C.free(ptr);
		if (found) {
			if (isDirectory[0] != 0) {
				return 1;
			} else if (popup != null) {
				String fileName = path.lastPathComponent().getString();
				int filterIndex = (int)popup.indexOfSelectedItem();
				String extensions = filterExtensions [filterIndex];
				int start = 0, length = extensions.length ();
				while (start < length) {
					int index = extensions.indexOf (EXTENSION_SEPARATOR, start);
					if (index == -1) index = length;
					String filter = extensions.substring (start, index).trim ();
					if (filter.equalsIgnoreCase (fileName)) return 1;
					if (filter.equals ("*") || filter.equals ("*.*")) return 1;
					if (filter.startsWith ("*.")) {
						filter = filter.substring (2);
					} else if (filter.startsWith (".")) {
						filter = filter.substring (1);
					}
					if ((fileName.toLowerCase ()).endsWith("." + filter.toLowerCase ())) return 1;
					start = index + 1;
				}
				return 0;
			}
		}
	}
	return 1;
}

void releaseHandles() {
	if (!overwrite) {
		if (method != 0) OS.method_setImplementation(method, methodImpl);
		if (callback_overwrite_existing_file != null) callback_overwrite_existing_file.dispose();
		callback_overwrite_existing_file = null;
	}
	if (callback_completion_handler != null) {
		callback_completion_handler.dispose();
		callback_completion_handler = null;
	}
	if (popup != null) {
		panel.setAccessoryView(null);
		popup.release();
		popup = null;
	}
	if (delegate != null) {
		panel.setDelegate(null);
		delegate.release();
		delegate = null;
	}
	if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
	jniRef = 0;
	panel = null;
}

void sendSelection (long id, long sel, long arg) {
	if (filterExtensions != null && filterExtensions.length > 0) {
		String fileTypes = filterExtensions[(int)popup.indexOfSelectedItem ()];
		panel.setTreatsFilePackagesAsDirectories (shouldTreatAppAsDirectory (fileTypes));
		setAllowedFileType (fileTypes);
	}
	panel.validateVisibleColumns ();
}

void setAllowedFileType (String fileTypes) {
	if (fileTypes == null) return;

	StringTokenizer fileTypesToken = new StringTokenizer(fileTypes, String.valueOf(EXTENSION_SEPARATOR));
	NSMutableArray allowedFileTypes = NSMutableArray.arrayWithCapacity(1);

	while(fileTypesToken.hasMoreTokens()) {
		String fileType = fileTypesToken.nextToken();

		if (fileType.equals("*") || fileType.equals("*.*")) {
			panel.setAllowedFileTypes(null);
			return;
		}
		if (fileType.startsWith("*.")) {
			fileType = fileType.substring(2);
		} else if (fileType.startsWith(".")) {
			fileType = fileType.substring(1);
		}
		/*
		 * In Cocoa, only the part of the file name after the last extension divider (.)
		 * is considered as extension. But, SWT FileDialog supports extensions with more than one (.).
		 * When files with extensions which have more than 1 separator are filtered, they are not
		 * shown as enabled in the File Open Dialog. For example, using tar.gz in the filter
		 * extension doesn't show the tar.gz files enabled in the FileDialog.
		 *
		 * The workaround is for Open FileDialog, add only the extension after the last (.) as the allowed
		 * file type. For example, for tar.gz, only gz is added as the allowed file type.
		 */
		if ((style & SWT.SAVE) == 0) {
			int index = fileType.lastIndexOf(".");
			if (index != -1 && ((index + 1) < fileType.length())) {
				fileType = fileType.substring(index + 1);
			}
		}
		allowedFileTypes.addObject(NSString.stringWith(fileType));
	}

	panel.setAllowedFileTypes(allowedFileTypes);
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
 *
 * @param overwrite true if the dialog will prompt for file overwrite, false otherwise
 *
 * @since 3.4
 */
public void setOverwrite (boolean overwrite) {
	this.overwrite = overwrite;
}

/**
 * Determines if the treatAppAsBundle property has to be enabled for the NSOpenPanel
 * for the filterExtensions passed as a parameter.
 */
boolean shouldTreatAppAsDirectory (String extensions) {
	if ((style & SWT.SAVE) != 0) return false;
	StringTokenizer fileTypesToken = new StringTokenizer (extensions, String.valueOf(EXTENSION_SEPARATOR));
	while (fileTypesToken.hasMoreTokens ()) {
		String fileType = fileTypesToken.nextToken ();
		if (fileType.equals ("*") || fileType.equals ("*.*")) return true;
		if (fileType.equals ("*.app") || fileType.equals (".app")) return false;
	}
	return true;
}
}

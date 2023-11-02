/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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


import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	String filterPath = "";
	String fileName = "";
	String[] fileNames = new String [0];
	String fullPath = "";
	int filterIndex = -1;
	boolean overwrite = false;
	boolean uriMode;
	long handle;
	static final char SEPARATOR = File.separatorChar;
	static final char EXTENSION_SEPARATOR = ';';
	static final char FILE_EXTENSION_SEPARATOR = '.';

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
String computeResultChooserDialog () {
	/* MULTI is only valid if the native dialog's action is Open */
	fullPath = null;
	if ((style & SWT.MULTI) != 0) {
		long list = 0;
		if (GTK.GTK4) {
			list = GTK4.gtk_file_chooser_get_files(handle);
		} else {
			if (uriMode) {
				list = GTK3.gtk_file_chooser_get_uris (handle);
			} else {
				list = GTK3.gtk_file_chooser_get_filenames (handle);
			}
		}

		int listLength;
		if(GTK.GTK4) listLength = OS.g_list_model_get_n_items(list);
		else listLength = OS.g_slist_length (list);
		fileNames = new String [listLength];
		long current = list;
		int writePos = 0;
		for (int i = 0; i < listLength; i++) {
			long name;
			if(GTK.GTK4) name = OS.g_list_model_get_item(list, i);
			else name = OS.g_slist_data (current);
			long utf8Ptr = 0;
			if (uriMode) {
				if (GTK.GTK4) {
					utf8Ptr = OS.g_file_get_uri(name);
				} else {
					utf8Ptr = name;
				}
			} else {
				if (GTK.GTK4) {
					name = OS.g_file_get_path(name);
				}
				utf8Ptr = OS.g_filename_to_utf8 (name, -1, null, null, null);
				if (utf8Ptr == 0) utf8Ptr = OS.g_filename_display_name (name);
			}
			if (name != utf8Ptr) OS.g_free (name);
			if (utf8Ptr != 0) {
				long [] items_written = new long [1];
				long utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
				OS.g_free (utf8Ptr);
				if (utf16Ptr != 0) {
					int clength = (int)items_written [0];
					char [] chars = new char [clength];
					C.memmove (chars, utf16Ptr, clength * 2);
					OS.g_free (utf16Ptr);
					fullPath = new String (chars);
					fileNames [writePos++] = fullPath.substring (fullPath.lastIndexOf (SEPARATOR) + 1);
				}
			}
			if(!GTK.GTK4) current = OS.g_slist_next (current);
		}
		if (writePos != 0 && writePos != listLength) {
			String [] validFileNames = new String [writePos];
			System.arraycopy (fileNames, 0, validFileNames, 0, writePos);
			fileNames = validFileNames;
		}
		if(GTK.GTK4) OS.g_object_unref(list);
		else OS.g_slist_free (list);
	} else {
		long utf8Ptr = 0;
		if (uriMode) {
			if (GTK.GTK4) {
				long file = GTK4.gtk_file_chooser_get_file(handle);
				utf8Ptr = OS.g_file_get_uri(file);
			} else {
				utf8Ptr = GTK3.gtk_file_chooser_get_uri (handle);
			}
		} else {
			long path;
			if (GTK.GTK4) {
				long file = GTK4.gtk_file_chooser_get_file(handle);
				path = OS.g_file_get_path(file);
			} else {
				path = GTK3.gtk_file_chooser_get_filename (handle);
			}

			if (path != 0) {
				utf8Ptr = OS.g_filename_to_utf8 (path, -1, null, null, null);
				if (utf8Ptr == 0) utf8Ptr = OS.g_filename_display_name (path);
				if (path != utf8Ptr) OS.g_free (path);
			}
		}
		if (utf8Ptr != 0) {
			long [] items_written = new long [1];
			long utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
			OS.g_free (utf8Ptr);
			if (utf16Ptr != 0) {
				int clength = (int)items_written [0];
				char [] chars = new char [clength];
				C.memmove (chars, utf16Ptr, clength * 2);
				OS.g_free (utf16Ptr);
				fullPath = new String (chars);
				fileNames = new String [1];
				fileNames[0] = fullPath.substring (fullPath.lastIndexOf (SEPARATOR) + 1);
			}
		}
	}
	filterIndex = -1;
	long filter = GTK.gtk_file_chooser_get_filter (handle);
	if (filter != 0) {
		long filterNamePtr = GTK.gtk_file_filter_get_name (filter);
		if (filterNamePtr != 0) {
			int length = C.strlen (filterNamePtr);
			byte[] buffer = new byte [length];
			C.memmove (buffer, filterNamePtr, length);
			//OS.g_free (filterNamePtr); //GTK owns this pointer - do not free
			String filterName = new String (Converter.mbcsToWcs (buffer));
			for (int i = 0; i < filterExtensions.length; i++) {
				if (filterNames.length > 0) {
					if (filterNames[i].equals(filterName)) {
						filterIndex = i;
						break;
					}
				} else {
					if (filterExtensions[i].equals(filterName)) {
						filterIndex = i;
						break;
					}
				}
			}
		}
	}
	if (fullPath != null) {
		int separatorIndex = fullPath.lastIndexOf (SEPARATOR);
		fileName = fullPath.substring (separatorIndex + 1);
		filterPath = fullPath.substring (0, separatorIndex);
	}
	return fullPath;
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
	return openNativeChooserDialog();
}
/**
 * Open the file chooser dialog using the GtkFileChooserNative API (GTK3.20+) for running applications
 * without direct filesystem access (such as Flatpak). API for GtkFileChoosernative does not
 * give access to any GtkWindow or GtkWidget for the dialog, thus this method omits calls that
 * requires such access. These are be handled by the GtkNativeDialog API.
 *
 * @return a string describing the absolute path of the first selected file, or null
 */
String openNativeChooserDialog () {
	byte [] titleBytes = Converter.wcsToMbcs (title, true);
	int action = (style & SWT.SAVE) != 0 ? GTK.GTK_FILE_CHOOSER_ACTION_SAVE : GTK.GTK_FILE_CHOOSER_ACTION_OPEN;
	long shellHandle = parent.topHandle();
	Display display = parent != null ? parent.getDisplay (): Display.getCurrent();
	handle = GTK.gtk_file_chooser_native_new(titleBytes, shellHandle, action, null, null);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);

	if (uriMode && !GTK.GTK4) {
		// GTK4 file chooser works on GFiles and does not need to worry about this
		GTK3.gtk_file_chooser_set_local_only (handle, false);
	}
	presetChooserDialog ();
	display.addIdleProc ();
	String answer = null;
	int signalId = 0;
	long hookId = 0;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		signalId = OS.g_signal_lookup (OS.map, GTK.GTK_TYPE_WIDGET());
		hookId = OS.g_signal_add_emission_hook (signalId, 0, display.emissionProc, handle, 0);
	}

	int response;
	if (GTK.GTK4) {
		response = SyncDialogUtil.run(display, handle, true);
	} else {
		display.externalEventLoop = true;
		display.sendPreExternalEventDispatchEvent ();
		response = GTK3.gtk_native_dialog_run(handle);
		display.externalEventLoop = false;
		display.sendPostExternalEventDispatchEvent ();
	}

	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.g_signal_remove_emission_hook (signalId, hookId);
	}
	if (response == GTK.GTK_RESPONSE_ACCEPT) {
		answer = computeResultChooserDialog ();
	}
	display.removeIdleProc ();
	return answer;
}

void presetChooserDialog () {
	/* MULTI is only valid if the native dialog's action is Open */
	if ((style & (SWT.SAVE | SWT.MULTI)) == SWT.MULTI) {
		GTK.gtk_file_chooser_set_select_multiple (handle, true);
	}
	if (filterPath == null) filterPath = "";
	if (fileName == null) fileName = "";
	if ((style & SWT.SAVE) != 0) {
		if (fileName.equals ("")) {
			fileName = "Untitled";
		}
		if (filterPath.length () > 0) {
			if (uriMode) {
				byte [] buffer = Converter.wcsToMbcs (filterPath, true);

				if (GTK.GTK4) {
					long file = OS.g_file_new_for_uri(buffer);
					GTK4.gtk_file_chooser_set_current_folder (handle, file, 0);
					OS.g_object_unref(file);
				} else {
					GTK3.gtk_file_chooser_set_current_folder_uri (handle, buffer);
				}

			} else {
				/* filename must be a full path */
				byte [] buffer = Converter.wcsToMbcs (SEPARATOR + filterPath, true);
				/*
				 * in GTK version 2.10, gtk_file_chooser_set_current_folder requires path
				 * to be true canonical path. So using realpath to convert the path to
				 * true canonical path.
				 */
				long ptr = OS.realpath (buffer, null);
				if (ptr != 0) {
					if (GTK.GTK4) {
						long file = OS.g_file_new_for_path(buffer);
						GTK4.gtk_file_chooser_set_current_folder (handle, file, 0);
						OS.g_object_unref(file);
					} else {
						GTK3.gtk_file_chooser_set_current_folder (handle, ptr);
					}
					OS.g_free (ptr);
				}
			}
		}
		if (fileName.length () > 0) {
			StringBuilder filenameWithExt = new StringBuilder();
			filenameWithExt.append (fileName);
			if ((fileName.lastIndexOf (FILE_EXTENSION_SEPARATOR) == -1) &&
					(filterExtensions.length != 0)) {  // Filename doesn't contain the extension and user has provided filter extensions
				String selectedFilter = null;
				if (this.filterIndex == -1) {
					selectedFilter = filterExtensions[0];
				} else {
					selectedFilter = filterExtensions[filterIndex];
				}
				String extFilter = null;
				int index = selectedFilter.indexOf (EXTENSION_SEPARATOR);
				if (index == -1) {
					extFilter = selectedFilter.trim ();
				} else {
					extFilter = selectedFilter.substring (0, index).trim ();
				}

				int separatorIndex = extFilter.lastIndexOf (FILE_EXTENSION_SEPARATOR);
				String extension = extFilter.substring (separatorIndex);

				if (!isGlobPattern (extension)) { //if the extension is of type glob pattern we should not add the extension
					filenameWithExt.append (extension);
				}
			}
			byte [] buffer = Converter.wcsToMbcs (filenameWithExt.toString (), true);
			GTK.gtk_file_chooser_set_current_name (handle, buffer);
		}
	} else {
		StringBuilder stringBuilder = new StringBuilder();
		if (filterPath.length () > 0) {
			if (!uriMode) {
				/* filename must be a full path */
				stringBuilder.append(SEPARATOR);
			}
			stringBuilder.append(filterPath);
			stringBuilder.append(SEPARATOR);
		}
		if (fileName.length () > 0) {
			stringBuilder.append(fileName);
		}
		byte [] buffer = Converter.wcsToMbcs (stringBuilder.toString(), true);

		if (GTK.GTK4) {
			long file;
			if (uriMode) {
				file = OS.g_file_new_for_uri(buffer);
				GTK4.gtk_file_chooser_set_file (handle, file, 0);
			} else {
				file = OS.g_file_new_for_path(buffer);

				if (fileName.length() > 0) {
					GTK4.gtk_file_chooser_set_file (handle, file, 0);
				}
			}

			OS.g_object_unref(file);
		} else {
			if (uriMode) {
				GTK3.gtk_file_chooser_set_uri (handle, buffer);
			} else {
				/*
				 * in GTK version 2.10, gtk_file_chooser_set_current_folder requires path
				 * to be true canonical path. So using realpath to convert the path to
				 * true canonical path.
				 */
				long ptr = OS.realpath (buffer, null);
				if (ptr != 0) {
					if (fileName.length() > 0) {
						GTK3.gtk_file_chooser_set_filename (handle, ptr);
					} else {
						GTK3.gtk_file_chooser_set_current_folder (handle, ptr);
					}
					OS.g_free (ptr);
				}
			}
		}
	}

	/* Set overwrite mode */
	if ((style & SWT.SAVE) != 0) {
		if (GTK.GTK4) {
			// TODO: GTK4 does not this property for the file chooser, not sure what the default behavior is. Must test before trying an alternative.
		} else {
			GTK3.gtk_file_chooser_set_do_overwrite_confirmation (handle, overwrite);
		}
	}

	/* Set the extension filters */
	if (filterNames == null) filterNames = new String [0];
	if (filterExtensions == null) filterExtensions = new String [0];
	long initialFilter = 0;
	for (int i = 0; i < filterExtensions.length; i++) {
		if (filterExtensions [i] != null) {
			long filter = GTK.gtk_file_filter_new ();
			if (filterNames.length > i && filterNames [i] != null) {
				byte [] name = Converter.wcsToMbcs (filterNames [i], true);
				GTK.gtk_file_filter_set_name (filter, name);
			} else {
				byte [] name = Converter.wcsToMbcs (filterExtensions [i], true);
				GTK.gtk_file_filter_set_name (filter, name);
			}
			int start = 0;
			int index = filterExtensions [i].indexOf (EXTENSION_SEPARATOR);
			while (index != -1) {
				String current = filterExtensions [i].substring (start, index);
				byte [] filterString = Converter.wcsToMbcs (current, true);
				GTK.gtk_file_filter_add_pattern (filter, filterString);
				start = index + 1;
				index = filterExtensions [i].indexOf (EXTENSION_SEPARATOR, start);
			}
			String current = filterExtensions [i].substring (start);
			byte [] filterString = Converter.wcsToMbcs (current, true);
			GTK.gtk_file_filter_add_pattern (filter, filterString);
			GTK.gtk_file_chooser_add_filter (handle, filter);
			if (i == filterIndex) {
				initialFilter = filter;
			}
		}
	}
	if (initialFilter != 0) {
		GTK.gtk_file_chooser_set_filter(handle, initialFilter);
	}
	fullPath = null;
	fileNames = new String [0];
}
/**
 * Check whether the file extension is a glob pattern.
 * For example, *.* is a glob pattern which corresponds to all files
 * *.jp* corresponds to all the files with extension starting with jp like jpg,jpeg etc
 * *.jp? corresponds to 3 letter extension starting with jp with any 3rd letter
 * *.[pq]ng this corresponds to *.png and *.qng
 *
 * @param extension file extension as a string
 *
 * @return true if the extension contains any of the glob pattern wildcards
 */
private boolean isGlobPattern (String extension) {
	if (extension.contains ("*") ||
			extension.contains ("?") ||
			(extension.contains ("[") && extension.contains ("]"))) {
		return true;
	}
	return false;
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
/* Sets URI Mode.
 *
 * When the FileDialog is in URI mode it returns
 * a URI (instead of a file name) for the following
 * methods: open() and getFilterPath().
 * The input argment for setFilterPath() should also
 * be a URI.
 */
/*public*/ void setURIMode (boolean uriMode) {
	this.uriMode = uriMode;
}
}

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
	String message = "", filterPath = "";
	static final String SEPARATOR = File.separator;

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
	long shellHandle = parent.topHandle ();
	Display display = parent != null ? parent.getDisplay (): Display.getCurrent ();
	long handle = 0;
	handle = GTK.gtk_file_chooser_native_new(titleBytes, shellHandle, GTK.GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER, null, null);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);

	if (filterPath != null && filterPath.length () > 0) {
		StringBuilder stringBuilder = new StringBuilder ();
		/* filename must be a full path */
		if (!filterPath.startsWith (SEPARATOR)) {
			stringBuilder.append (SEPARATOR);
		}
		stringBuilder.append (filterPath);
		byte [] buffer = Converter.wcsToMbcs (stringBuilder.toString (), true);
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

	GTK3setNativeDialogMessage(handle, message);

	String answer = null;
	display.addIdleProc ();
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
		response = GTK3.gtk_native_dialog_run (handle);
		display.externalEventLoop = false;
		display.sendPostExternalEventDispatchEvent ();
	}

	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.g_signal_remove_emission_hook (signalId, hookId);
	}
	if (response == GTK.GTK_RESPONSE_ACCEPT) {
		long path;
		if (GTK.GTK4) {
			long file = GTK4.gtk_file_chooser_get_file (handle);
			path = OS.g_file_get_path(file);
		} else {
			path = GTK3.gtk_file_chooser_get_filename (handle);
		}

		if (path != 0) {
			long utf8Ptr = OS.g_filename_to_utf8 (path, -1, null, null, null);
			if (utf8Ptr == 0) utf8Ptr = OS.g_filename_display_name (path);
			if (path != utf8Ptr) OS.g_free (path);
			if (utf8Ptr != 0) {
				long [] items_written = new long [1];
				long utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
				OS.g_free (utf8Ptr);
				if (utf16Ptr != 0) {
					int clength = (int)items_written [0];
					char [] chars = new char [clength];
					C.memmove (chars, utf16Ptr, clength * 2);
					OS.g_free (utf16Ptr);
					answer = new String (chars);
					filterPath = answer;
				}
			}
		}
	}
	display.removeIdleProc ();
	return answer;
}


/**
 * GTK3 only function. As of GTK4, gtk_file_chooser_set_extra_widget
 * is no longer available, and the alternatives do not allow for such
 * flexibility to display just the message. Therefore in GTK4, there will
 * be no message displayed in the file chooser dialog.
 */
void GTK3setNativeDialogMessage(long handle, String message) {
	if (GTK.GTK4) return;

	if (message.length () > 0) {
		byte[] buffer = Converter.wcsToMbcs(message, true);
		long box = GTK.gtk_box_new(GTK.GTK_ORIENTATION_HORIZONTAL, 0);
		if (box == 0) error(SWT.ERROR_NO_HANDLES);
		long label = GTK.gtk_label_new (buffer);
		if (label == 0) error(SWT.ERROR_NO_HANDLES);

		GTK3.gtk_container_add(box, label);
		GTK.gtk_widget_show(label);
		GTK3.gtk_label_set_line_wrap(label, true);

		GTK.gtk_box_set_homogeneous(box, false);
		GTK.gtk_label_set_justify(label, GTK.GTK_JUSTIFY_CENTER);
		GTK3.gtk_file_chooser_set_extra_widget(handle, box);
	}
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

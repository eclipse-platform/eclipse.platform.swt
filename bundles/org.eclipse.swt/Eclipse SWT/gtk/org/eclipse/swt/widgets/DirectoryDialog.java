/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

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
	static final String SEPARATOR = System.getProperty ("file.separator");

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
		return openChooserDialog ();
}
String openChooserDialog () {
	byte [] titleBytes = Converter.wcsToMbcs (null, title, true);
	int /*long*/ shellHandle = parent.topHandle ();
	Display display = parent != null ? parent.getDisplay (): Display.getCurrent ();
	int /*long*/ handle = 0;
	if (display.getDismissalAlignment() == SWT.RIGHT) {
		handle = OS.gtk_file_chooser_dialog_new (titleBytes, shellHandle, OS.GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER, OS.GTK_STOCK_CANCEL (), OS.GTK_RESPONSE_CANCEL, OS.GTK_STOCK_OK (), OS.GTK_RESPONSE_OK, 0);
	} else {
		handle = OS.gtk_file_chooser_dialog_new (titleBytes, shellHandle, OS.GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER, OS.GTK_STOCK_OK (), OS.GTK_RESPONSE_OK, OS.GTK_STOCK_CANCEL (), OS.GTK_RESPONSE_CANCEL, 0);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		int /*long*/ group = OS.gtk_window_get_group(0);
		OS.gtk_window_group_add_window (group, handle);
	}
	OS.gtk_window_set_modal (handle, true);
	int /*long*/ pixbufs = OS.gtk_window_get_icon_list (shellHandle);
	if (pixbufs != 0) {
		OS.gtk_window_set_icon_list (handle, pixbufs);
		OS.g_list_free (pixbufs);
	}
	if (filterPath != null && filterPath.length () > 0) {
		StringBuffer stringBuffer = new StringBuffer ();
		/* filename must be a full path */
		if (!filterPath.startsWith (SEPARATOR)) {
			stringBuffer.append (SEPARATOR);
		}
		stringBuffer.append (filterPath);
		byte [] buffer = Converter.wcsToMbcs (null, stringBuffer.toString (), true);
		/*
		* Bug in GTK. GtkFileChooser may crash on GTK versions 2.4.10 to 2.6
		* when setting a file name that is not a true canonical path. 
		* The fix is to use the canonical path.
		*/
		int /*long*/ ptr = OS.realpath (buffer, null);
		if (ptr != 0) {
			OS.gtk_file_chooser_set_current_folder (handle, ptr);
			OS.g_free (ptr);
		}
	}
	if (message.length () > 0) {
		byte [] buffer = Converter.wcsToMbcs (null, message, true);
		int /*long*/ box = 0;
		if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
			box = OS.gtk_box_new (OS.GTK_ORIENTATION_HORIZONTAL, 0);
			OS.gtk_box_set_homogeneous (box, false);
		} else {
			box = OS.gtk_hbox_new (false, 0);
		}
		if (box == 0) error (SWT.ERROR_NO_HANDLES);
		int /*long*/ label = OS.gtk_label_new (buffer);
		if (label == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (box, label);
		OS.gtk_widget_show (label);
		OS.gtk_label_set_line_wrap (label, true);
		OS.gtk_label_set_justify (label, OS.GTK_JUSTIFY_CENTER);
		OS.gtk_file_chooser_set_extra_widget (handle, box);
	}
	String answer = null;
	display.addIdleProc ();
	Dialog oldModal = null;
	if (OS.gtk_window_get_modal (handle)) {
		oldModal = display.getModalDialog ();
		display.setModalDialog (this);
	}
	int signalId = 0;
	int /*long*/ hookId = 0;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		signalId = OS.g_signal_lookup (OS.map, OS.GTK_TYPE_WIDGET());
		hookId = OS.g_signal_add_emission_hook (signalId, 0, display.emissionProc, handle, 0);
	}	
	int response = OS.gtk_dialog_run (handle);
	/*
	* This call to gdk_threads_leave() is a temporary work around
	* to avoid deadlocks when gdk_threads_init() is called by native
	* code outside of SWT (i.e AWT, etc). It ensures that the current
	* thread leaves the GTK lock acquired by the function above. 
	*/
	OS.gdk_threads_leave();
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.g_signal_remove_emission_hook (signalId, hookId);
	}
	if (OS.gtk_window_get_modal (handle)) {
		display.setModalDialog (oldModal);
	}
	if (response == OS.GTK_RESPONSE_OK) {
		int /*long*/ path = OS.gtk_file_chooser_get_filename (handle);
		if (path != 0) {
			int /*long*/ utf8Ptr = OS.g_filename_to_utf8 (path, -1, null, null, null);
			if (utf8Ptr == 0) utf8Ptr = OS.g_filename_display_name (path);
			if (path != utf8Ptr) OS.g_free (path);
			if (utf8Ptr != 0) {
				int /*long*/ [] items_written = new int /*long*/ [1];
				int /*long*/ utf16Ptr = OS.g_utf8_to_utf16 (utf8Ptr, -1, null, items_written, null);
				OS.g_free (utf8Ptr);
				if (utf16Ptr != 0) {
					int clength = (int)/*64*/items_written [0];
					char [] chars = new char [clength];
					OS.memmove (chars, utf16Ptr, clength * 2);
					OS.g_free (utf16Ptr);
					answer = new String (chars);
					filterPath = answer;
				}
			}
		}
	}
	display.removeIdleProc ();
	OS.gtk_widget_destroy (handle);
	return answer;
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

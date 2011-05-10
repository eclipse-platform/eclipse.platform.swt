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
 * Instances of this class are used to inform or warn the user.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING, ICON_WORKING</dd>
 * <dd>OK, OK | CANCEL</dd>
 * <dd>YES | NO, YES | NO | CANCEL</dd>
 * <dd>RETRY | CANCEL</dd>
 * <dd>ABORT | RETRY | IGNORE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ICON_ERROR, ICON_INFORMATION, ICON_QUESTION,
 * ICON_WARNING and ICON_WORKING may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class MessageBox extends Dialog {

	String message = "";
	int /*long*/ handle;
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
public MessageBox (Shell parent) {
	this (parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
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
 * @see SWT#ICON_ERROR
 * @see SWT#ICON_INFORMATION
 * @see SWT#ICON_QUESTION
 * @see SWT#ICON_WARNING
 * @see SWT#ICON_WORKING
 * @see SWT#OK
 * @see SWT#CANCEL
 * @see SWT#YES
 * @see SWT#NO
 * @see SWT#ABORT
 * @see SWT#RETRY
 * @see SWT#IGNORE
 */
public MessageBox (Shell parent, int style) {
	super (parent, checkStyle (parent, checkStyle (style)));
	checkSubclass ();
}

/**
 * Returns the dialog's message, or an empty string if it does not have one.
 * The message is a description of the purpose for which the dialog was opened.
 * This message will be visible in the dialog while it is open.
 *
 * @return the message
 */
public String getMessage () {
	return message;
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

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return the ID of the button that was selected to dismiss the
 *         message box (e.g. SWT.OK, SWT.CANCEL, etc.)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public int open () {
	int /*long*/ parentHandle = (parent != null) ? parent.topHandle() : 0;
	int dialogFlags = OS.GTK_DIALOG_DESTROY_WITH_PARENT;
	if ((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		dialogFlags |= OS.GTK_DIALOG_MODAL;
	}
	int messageType = OS.GTK_MESSAGE_INFO;
	if ((style & (SWT.ICON_WARNING)) != 0)  messageType = OS.GTK_MESSAGE_WARNING;
	if ((style & (SWT.ICON_QUESTION)) != 0) messageType = OS.GTK_MESSAGE_QUESTION;
	if ((style & (SWT.ICON_ERROR)) != 0)    messageType = OS.GTK_MESSAGE_ERROR;
	
	byte [] buffer = Converter.wcsToMbcs (null, fixPercent (message), true);
	handle = OS.gtk_message_dialog_new(parentHandle, dialogFlags, messageType, 0, buffer);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (parentHandle != 0) {
		int /*long*/ pixbufs = OS.gtk_window_get_icon_list (parentHandle);
		if (pixbufs != 0) {
			OS.gtk_window_set_icon_list (handle, pixbufs);
			OS.g_list_free (pixbufs);
		}
	}
	Display display = parent != null ? parent.getDisplay (): Display.getCurrent ();
	createButtons (display.getDismissalAlignment ());
	buffer = Converter.wcsToMbcs(null, title, true);
	OS.gtk_window_set_title(handle,buffer);
	display.addIdleProc ();
	Dialog oldModal = null;
	/*
	* In order to allow the dialog to be modal of it's
	* parent shells, it is required to assign the 
	* dialog to the same window group as of the shells.
	*/
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		int /*long*/ group = OS.gtk_window_get_group(0);
		OS.gtk_window_group_add_window (group, handle);
	}
	
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
	display.removeIdleProc ();
	OS.gtk_widget_destroy (handle);
	return response;
}

private void createButtons (int alignment) {
	if (alignment == SWT.LEFT) {
		if ((style & SWT.OK) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-ok", true), SWT.OK);
		if ((style & SWT.ABORT) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, SWT.getMessage("SWT_Abort"), true), SWT.ABORT);
		if ((style & SWT.RETRY) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, SWT.getMessage("SWT_Retry"), true), SWT.RETRY);
		if ((style & SWT.YES) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-yes", true), SWT.YES);
		if ((style & SWT.NO) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-no", true), SWT.NO);
		if ((style & SWT.IGNORE) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, SWT.getMessage("SWT_Ignore"), true), SWT.IGNORE);
		if ((style & SWT.CANCEL) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-cancel", true), SWT.CANCEL);
	} else {		
		if ((style & SWT.CANCEL) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-cancel", true), SWT.CANCEL);
		if ((style & SWT.OK) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-ok", true), SWT.OK);
		if ((style & SWT.NO) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-no", true), SWT.NO);
		if ((style & SWT.YES) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, "gtk-yes", true), SWT.YES);
		if ((style & SWT.IGNORE) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, SWT.getMessage("SWT_Ignore"), true), SWT.IGNORE);
		if ((style & SWT.RETRY) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, SWT.getMessage("SWT_Retry"), true), SWT.RETRY);
		if ((style & SWT.ABORT) != 0) OS.gtk_dialog_add_button(handle, Converter.wcsToMbcs (null, SWT.getMessage("SWT_Abort"), true), SWT.ABORT);
	}
}

private static int checkStyle (int style) {
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) return style;
	if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) return style;
	if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) return style;
	style = (style & ~mask) | SWT.OK;
	return style;
}

char[] fixPercent (String string) {
	int length = string.length ();
	char [] text = new char [length];
	string.getChars (0, length, text, 0);
	int i = 0, j = 0;
	char [] result = new char [length * 2];
	while (i < length) {
		switch (text [i]) {
			case '%':
				result [j++] = '%';
				break;
		}
		result [j++] = text [i++];
	}
	return result;    
}
}

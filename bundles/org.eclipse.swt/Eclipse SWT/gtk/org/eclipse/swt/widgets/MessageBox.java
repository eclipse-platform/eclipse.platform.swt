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


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class MessageBox extends Dialog {

	String message = "";
	long handle;
	private Map<Integer, String> labels;

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
public int open() {
	long parentHandle = (parent != null) ? parent.topHandle() : 0;
	int dialogFlags = GTK.GTK_DIALOG_DESTROY_WITH_PARENT;
	if ((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		dialogFlags |= GTK.GTK_DIALOG_MODAL;
	}
	int messageType = GTK.GTK_MESSAGE_INFO;
	if ((style & (SWT.ICON_WARNING)) != 0)  messageType = GTK.GTK_MESSAGE_WARNING;
	if ((style & (SWT.ICON_QUESTION)) != 0) messageType = GTK.GTK_MESSAGE_QUESTION;
	if ((style & (SWT.ICON_ERROR)) != 0)    messageType = GTK.GTK_MESSAGE_ERROR;

	byte[] format = Converter.wcsToMbcs("%s", true);
	byte[] buffer = Converter.wcsToMbcs(title, true);
	handle = GTK.gtk_message_dialog_new(parentHandle, dialogFlags, messageType, 0, format, buffer);
	if (handle == 0) error(SWT.ERROR_NO_HANDLES);

	// Copy parent's icon
	if (parentHandle != 0) {
		if (GTK.GTK4) {
			/*
			 * TODO: This may not work as we are setting the icon list of the GtkWindow through
			 * GdkToplevel (which has no way of retrieving the icon list set. See bug 572200.
			 */
			long iconName = GTK4.gtk_window_get_icon_name(parentHandle);
			if (iconName != 0) {
				GTK4.gtk_window_set_icon_name(handle, iconName);
			}
		} else {
			long pixbufs = GTK3.gtk_window_get_icon_list(parentHandle);
			if (pixbufs != 0) {
				GTK3.gtk_window_set_icon_list(handle, pixbufs);
				OS.g_list_free (pixbufs);
			}
		}
	}

	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	createButtons(display.getDismissalAlignment());
	GTK.gtk_message_dialog_format_secondary_text(handle, format, Converter.javaStringToCString(message));

	display.addIdleProc();
	Dialog oldModal = null;
	if (GTK.gtk_window_get_modal(handle)) {
		oldModal = display.getModalDialog();
		display.setModalDialog(this);
	}
	/*
	* In order to allow the dialog to be modal of it's
	* parent shells, it is required to assign the
	* dialog to the same window group as of the shells.
	*/
	long defaultWindowGroup = GTK.gtk_window_get_group(0);
	GTK.gtk_window_group_add_window(defaultWindowGroup, handle);

	int signalId = 0;
	long hookId = 0;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		signalId = OS.g_signal_lookup(OS.map, GTK.GTK_TYPE_WIDGET());
		hookId = OS.g_signal_add_emission_hook(signalId, 0, display.emissionProc, handle, 0);
	}

	int response;
	if (GTK.GTK4) {
		response = SyncDialogUtil.run(display, handle, false);
	} else {
		display.externalEventLoop = true;
		display.sendPreExternalEventDispatchEvent();
		response = GTK3.gtk_dialog_run(handle);
		display.externalEventLoop = false;
		display.sendPostExternalEventDispatchEvent();
	}

	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.g_signal_remove_emission_hook(signalId, hookId);
	}
	if (GTK.gtk_window_get_modal(handle)) {
		display.setModalDialog(oldModal);
	}
	display.removeIdleProc();

	if (GTK.GTK4) {
		GTK4.gtk_window_destroy(handle);
	} else {
		GTK3.gtk_widget_destroy(handle);
	}

	return response;
}

private void createButtons (int alignment) {
	if (alignment == SWT.LEFT) {
		if ((style & SWT.OK) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.OK, "SWT_OK"), SWT.OK);
		if ((style & SWT.ABORT) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.ABORT, "SWT_Abort"), SWT.ABORT);
		if ((style & SWT.RETRY) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.RETRY, "SWT_Retry"), SWT.RETRY);
		if ((style & SWT.YES) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.YES, "SWT_Yes"), SWT.YES);
		if ((style & SWT.NO) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.NO, "SWT_No"), SWT.NO);
		if ((style & SWT.IGNORE) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.IGNORE, "SWT_Ignore"), SWT.IGNORE);
		if ((style & SWT.CANCEL) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.CANCEL, "SWT_Cancel"), SWT.CANCEL);
	} else {
		if ((style & SWT.CANCEL) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.CANCEL, "SWT_Cancel"), SWT.CANCEL);
		if ((style & SWT.OK) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.OK, "SWT_OK"), SWT.OK);
		if ((style & SWT.NO) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.NO, "SWT_No"), SWT.NO);
		if ((style & SWT.YES) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.YES, "SWT_Yes"), SWT.YES);
		if ((style & SWT.IGNORE) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.IGNORE, "SWT_Ignore"), SWT.IGNORE);
		if ((style & SWT.RETRY) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.RETRY, "SWT_Retry"), SWT.RETRY);
		if ((style & SWT.ABORT) != 0) GTK.gtk_dialog_add_button(handle, getLabelForButton(SWT.ABORT, "SWT_Abort"), SWT.ABORT);
	}
}

private byte[] getLabelForButton(int buttonId, String messageId) {
	if (labels !=null && labels.containsKey(buttonId) && labels.get(buttonId)!=null) {
		return Converter.wcsToMbcs (labels.get(buttonId), true);
	}
	return Converter.wcsToMbcs(SWT.getMessage(messageId), true);
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

/**
 * Set custom text for <code>MessageDialog</code>'s buttons:
 *
 * @param labels a <code>Map</code> where a valid 'key' is from below listed
 *               styles:<ul>
 * <li>SWT#OK</li>
 * <li>SWT#CANCEL</li>
 * <li>SWT#YES</li>
 * <li>SWT#NO</li>
 * <li>SWT#ABORT</li>
 * <li>SWT#RETRY</li>
 * <li>SWT#IGNORE</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if labels is null</li>
 * </ul>
 * @since 3.121
 */
public void setButtonLabels(Map<Integer, String> labels) {
	if (labels == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.labels = labels;
}
}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are used used to inform or warn the user.
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
 */
public class MessageBox extends Dialog {

	String message;
	int handle;
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
 * for all SWT dialog classes should include a comment which
 * describes the style constants which are applicable to the class.
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
public MessageBox (Shell parent, int style) {
	super(parent, checkStyle(style));
	checkSubclass ();
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
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @param string the message
 */
public void setMessage (String string) {
	message = string;
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return the ID of the button that was selected to dismiss the
 *         message box (e.g. SWT.OK, SWT.CANCEL, etc...)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public int open () {
	int parentHandle = (parent != null)? parent.topHandle() : 0;
	int dialogFlags = OS.GTK_DIALOG_DESTROY_WITH_PARENT;
	if ((style & (SWT.PRIMARY_MODAL|SWT.APPLICATION_MODAL|SWT.SYSTEM_MODAL)) != 0) {
		dialogFlags |= OS.GTK_DIALOG_MODAL;
	}
	int messageType = OS.GTK_MESSAGE_INFO;
	if ((style & (SWT.ICON_WARNING)) != 0)  messageType = OS.GTK_MESSAGE_WARNING;
	if ((style & (SWT.ICON_QUESTION)) != 0) messageType = OS.GTK_MESSAGE_QUESTION;
	if ((style & (SWT.ICON_ERROR)) != 0)    messageType = OS.GTK_MESSAGE_ERROR;
	
	handle = OS.gtk_message_dialog_new(parentHandle, dialogFlags, messageType, 0, message);
	if (handle==0) SWT.error(SWT.ERROR_NO_HANDLES);
	createButtons();
	int result = OS.gtk_dialog_run(handle);
	OS.gtk_widget_destroy(handle);
	return result;
}

private void createButtons() {	
	if ((style & SWT.OK) != 0) OS.gtk_dialog_add_button(handle, "gtk-ok", SWT.OK);
	if ((style & SWT.CANCEL) != 0) OS.gtk_dialog_add_button(handle, "gtk-cancel", SWT.CANCEL);

	if ((style & SWT.YES) != 0) OS.gtk_dialog_add_button(handle, "gtk-yes", SWT.YES);
	if ((style & SWT.NO) != 0) OS.gtk_dialog_add_button(handle, "gtk-no", SWT.NO);

	if ((style & SWT.ABORT) != 0) OS.gtk_dialog_add_button(handle, SWT.getMessage("SWT_Abort"), SWT.ABORT);
	if ((style & SWT.RETRY) != 0) OS.gtk_dialog_add_button(handle, SWT.getMessage("SWT_Retry"), SWT.RETRY);
	if ((style & SWT.IGNORE) != 0) OS.gtk_dialog_add_button(handle, SWT.getMessage("SWT_Ignore"), SWT.IGNORE);
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
}

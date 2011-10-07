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


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

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
public  class MessageBox extends Dialog {
	String message = "";
	
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

static int checkStyle (int style) {
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) return style;
	if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) return style;
	if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) return style;
	style = (style & ~mask) | SWT.OK;
	return style;
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

	/* Compute the MessageBox style */
	int buttonBits = 0;
	if ((style & SWT.OK) == SWT.OK) buttonBits = OS.MB_OK;
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) buttonBits = OS.MB_OKCANCEL;
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) buttonBits = OS.MB_YESNO;
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) buttonBits = OS.MB_YESNOCANCEL;
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) buttonBits = OS.MB_RETRYCANCEL;
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) buttonBits = OS.MB_ABORTRETRYIGNORE;
	if (buttonBits == 0) buttonBits = OS.MB_OK;

	int iconBits = 0;
	if ((style & SWT.ICON_ERROR) != 0) iconBits = OS.MB_ICONERROR;
	if ((style & SWT.ICON_INFORMATION) != 0) iconBits = OS.MB_ICONINFORMATION;
	if ((style & SWT.ICON_QUESTION) != 0) iconBits = OS.MB_ICONQUESTION;
	if ((style & SWT.ICON_WARNING) != 0) iconBits = OS.MB_ICONWARNING;
	if ((style & SWT.ICON_WORKING) != 0) iconBits = OS.MB_ICONINFORMATION;

	/* Only MB_APPLMODAL is supported on WinCE */
	int modalBits = 0;
	if (OS.IsWinCE) {
		if ((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
			modalBits = OS.MB_APPLMODAL;
		}
	} else {
		if ((style & SWT.PRIMARY_MODAL) != 0) modalBits = OS.MB_APPLMODAL;
		if ((style & SWT.APPLICATION_MODAL) != 0) modalBits = OS.MB_TASKMODAL;
		if ((style & SWT.SYSTEM_MODAL) != 0) modalBits = OS.MB_SYSTEMMODAL;
	}

	int bits = buttonBits | iconBits | modalBits;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) bits |= OS.MB_RTLREADING | OS.MB_RIGHT;
	if ((style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)) == 0) {
		if (parent != null && (parent.style & SWT.MIRRORED) != 0) {
			bits |= OS.MB_RTLREADING | OS.MB_RIGHT;
		}
	}
	
	/*
	* Feature in Windows.  System modal is not supported
	* on Windows 95 and NT.  The fix is to convert system
	* modal to task modal.
	*/
	if ((bits & OS.MB_SYSTEMMODAL) != 0) {
		bits |= OS.MB_TASKMODAL;
		bits &= ~OS.MB_SYSTEMMODAL;
		/* Force a system modal message box to the front */
		bits |= OS.MB_TOPMOST;
	}

	/*
	* Feature in Windows.  In order for MB_TASKMODAL to work,
	* the parent HWND of the MessageBox () call must be NULL.
	* If the parent is not NULL, MB_TASKMODAL behaves the
	* same as MB_APPLMODAL.  The fix to set the parent HWND
	* anyway and not rely on MB_MODAL to work by making the
	* parent be temporarily modal. 
	*/
	int /*long*/ hwndOwner = parent != null ? parent.handle : 0;
	Dialog oldModal = null;
	Display display = null;
	if ((bits & OS.MB_TASKMODAL) != 0) {
		display = parent.getDisplay ();
		oldModal = display.getModalDialog ();
		display.setModalDialog (this);
	}

	/* Open the message box */
	/* Use the character encoding for the default locale */
	TCHAR buffer1 = new TCHAR (0, message, true);
	TCHAR buffer2 = new TCHAR (0, title, true);
	int code = OS.MessageBox (hwndOwner, buffer1, buffer2, bits);
	
	/* Clear the temporarily dialog modal parent */
	if ((bits & OS.MB_TASKMODAL) != 0) {
		display.setModalDialog (oldModal);
	}
	
	/*
	* This code is intentionally commented.  On some
	* platforms, the owner window is repainted right
	* away when a dialog window exits.  This behavior
	* is currently unspecified.
	*/
//	if (hwndOwner != 0) OS.UpdateWindow (hwndOwner);
	
	/* Compute and return the result */
	if (code != 0) {
		int type = bits & 0x0F;
		if (type == OS.MB_OK) return SWT.OK;
		if (type == OS.MB_OKCANCEL) {
			return (code == OS.IDOK) ? SWT.OK : SWT.CANCEL;
		}
		if (type == OS.MB_YESNO) {
			return (code == OS.IDYES) ? SWT.YES : SWT.NO;
		}
		if (type == OS.MB_YESNOCANCEL) {
			if (code == OS.IDYES) return SWT.YES;
			if (code == OS.IDNO) return SWT.NO;
			return SWT.CANCEL;
		}
		if (type == OS.MB_RETRYCANCEL) {
			return (code == OS.IDRETRY) ? SWT.RETRY : SWT.CANCEL;
		}
		if (type == OS.MB_ABORTRETRYIGNORE) {
			if (code == OS.IDRETRY) return SWT.RETRY;
			if (code == OS.IDABORT) return SWT.ABORT;
			return SWT.IGNORE;
		}
	}
	return SWT.CANCEL;
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

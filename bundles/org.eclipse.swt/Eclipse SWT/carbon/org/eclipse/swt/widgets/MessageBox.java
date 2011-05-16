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
import org.eclipse.swt.internal.carbon.*;

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

int createCFString (String id) {
	String string = SWT.getMessage(id);
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	return OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
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
	int alertType = OS.kAlertPlainAlert;
	if ((style & SWT.ICON_ERROR) != 0) alertType = OS.kAlertStopAlert;
	if ((style & SWT.ICON_INFORMATION) != 0) alertType = OS.kAlertNoteAlert;
	if ((style & SWT.ICON_QUESTION) != 0) alertType = OS.kAlertNoteAlert;
	if ((style & SWT.ICON_WARNING) != 0) alertType = OS.kAlertCautionAlert;
	if ((style & SWT.ICON_WORKING) != 0) alertType = OS.kAlertNoteAlert;
	
	int error = 0;
	int explanation = 0;
	String errorString = title;
	String explanationString = message;
	if (errorString != null) {
		char [] buffer = new char [errorString.length ()];
		errorString.getChars (0, buffer.length, buffer, 0);
		error = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	} 
	if (explanationString != null) {
		char [] buffer = new char [explanationString.length ()];
		explanationString.getChars (0, buffer.length, buffer, 0);
		explanation = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);		
	}
	
	AlertStdCFStringAlertParamRec param = new AlertStdCFStringAlertParamRec ();
	param.version = OS.kStdCFStringAlertVersionOne;
	param.position = (short)OS.kWindowAlertPositionParentWindowScreen;
	int defaultStr = 0, cancelStr = 0, otherStr = 0;
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	switch (bits) {
		case SWT.OK:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = OS.kAlertDefaultOKText;
			break;
		case SWT.CANCEL:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = createCFString ("SWT_Cancel");
			break;
		case SWT.OK | SWT.CANCEL:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = OS.kAlertDefaultOKText;
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = OS.kAlertDefaultCancelText;
			break;
		case SWT.YES:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = createCFString ("SWT_Yes");
			break;
		case SWT.NO:
			param.cancelButton = (short)OS.kAlertStdAlertOKButton;
			param.cancelText = defaultStr = createCFString ("SWT_No");
			break;
		case SWT.YES | SWT.NO:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = createCFString ("SWT_Yes");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = cancelStr = createCFString ("SWT_No");
			break;
		case SWT.YES | SWT.NO | SWT.CANCEL:				
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = createCFString ("SWT_Yes");
			param.otherText = cancelStr = createCFString ("SWT_No");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = OS.kAlertDefaultCancelText;
			break;
		case SWT.RETRY | SWT.CANCEL:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = createCFString ("SWT_Retry");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = OS.kAlertDefaultCancelText;
			break;
		case SWT.ABORT | SWT.RETRY | SWT.IGNORE:
			param.defaultButton = (short)OS.kAlertStdAlertOKButton;
			param.defaultText = defaultStr = createCFString ("SWT_Abort");
			param.otherText = cancelStr = createCFString ("SWT_Retry");
			param.cancelButton = (short)OS.kAlertStdAlertCancelButton;
			param.cancelText = otherStr = createCFString ("SWT_Ignore");
			break;
	}
	
	int [] dialogRef= new int [1];
	OS.CreateStandardAlert ((short) alertType, error, explanation, param, dialogRef);
	if (error != 0) OS.CFRelease(error);
	if (explanation != 0) OS.CFRelease(explanation);
	if (defaultStr != 0) OS.CFRelease(defaultStr);
	if (cancelStr != 0) OS.CFRelease(cancelStr);
	if (otherStr != 0) OS.CFRelease(otherStr);
	
	if (dialogRef[0] != 0) {
		/* Force a system modal message box to the front */
		if ((style & SWT.SYSTEM_MODAL) != 0) {
			OS.SetFrontProcessWithOptions (new int [] {0, OS.kCurrentProcess}, OS.kSetFrontProcessFrontWindowOnly);
		}
		Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
		/*
		* Bug in carbon. For some reason, RunStandardAlert() hangs when there are
		* windows opened with kUtilityWindowClass group.  The fix is to temporarily
		* put those windows in the kFloatingWindowClass group.
		*/
		Shell [] shells = display.getShells ();
		for (int i = 0; i < shells.length; i++) {
			Shell shell = shells[i];
			if ((shell.style & SWT.ON_TOP) != 0 && !shell.isDisposed () && shell.isVisible ()) {
				OS.SetWindowGroup (shell.shellHandle, OS.GetWindowGroupOfClass (OS.kFloatingWindowClass));
			} else {
				shells[i] = null;
			}
		}
		display.setModalDialog(this);
		short [] outItemHit = new short [1];
		OS.RunStandardAlert(dialogRef[0], 0, outItemHit);
		display.setModalDialog(null);
		for (int i = 0; i < shells.length; i++) {
			Shell shell = shells[i];
			if (shell != null && !shell.isDisposed ()) {
				OS.SetWindowGroup (shell.shellHandle, shell.getParentGroup ());
			}
		}
		if (outItemHit [0] != 0) {
			switch (bits) {
				case SWT.OK:
					return SWT.OK;
				case SWT.CANCEL:
					return SWT.CANCEL;
				case SWT.OK | SWT.CANCEL:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.OK;
					return SWT.CANCEL;
				case SWT.YES:
					return SWT.YES;
				case SWT.NO:
					return SWT.NO;
				case SWT.YES | SWT.NO:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.YES;
					return SWT.NO;
				case SWT.YES | SWT.NO | SWT.CANCEL:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.YES;
					if (outItemHit [0] == OS.kAlertStdAlertOtherButton) return SWT.NO;
					return SWT.CANCEL;
				case SWT.RETRY | SWT.CANCEL:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.RETRY;
					return SWT.CANCEL;
				case SWT.ABORT | SWT.RETRY | SWT.IGNORE:
					if (outItemHit [0] == OS.kAlertStdAlertOKButton) return SWT.ABORT;
					if (outItemHit [0] == OS.kAlertStdAlertOtherButton) return SWT.RETRY;
					return SWT.IGNORE;
			}
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

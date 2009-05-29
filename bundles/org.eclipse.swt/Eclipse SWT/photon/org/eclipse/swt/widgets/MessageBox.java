/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
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
	String[] buttons = null;
	if ((style & SWT.OK) == SWT.OK) {
		buttons = new String[]{SWT.getMessage ("SWT_OK")};
	}
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) {
		buttons = new String[]{SWT.getMessage ("SWT_OK"), SWT.getMessage ("SWT_Cancel")};
	}
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
		buttons = new String[]{SWT.getMessage ("SWT_Yes"), SWT.getMessage ("SWT_No")};
	}
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)){
		buttons = new String[]{SWT.getMessage ("SWT_Yes"), SWT.getMessage ("SWT_No"), SWT.getMessage ("SWT_Cancel")};
	}
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
		buttons = new String[]{SWT.getMessage ("SWT_Retry"), SWT.getMessage ("SWT_Cancel")};
	}
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
		buttons = new String[]{SWT.getMessage ("SWT_Abort"), SWT.getMessage ("SWT_Retry"), SWT.getMessage ("SWT_Cancel")};
	}
	if (buttons == null) buttons = new String[]{SWT.getMessage ("SWT_OK")};
	
	int phImage = 0;
//	int iconBits = 0;
//	if ((style & SWT.ICON_ERROR) != 0) iconBits = OS.MB_ICONERROR;
//	if ((style & SWT.ICON_INFORMATION) != 0) iconBits = OS.MB_ICONINFORMATION;
//	if ((style & SWT.ICON_QUESTION) != 0) iconBits = OS.MB_ICONQUESTION;
//	if ((style & SWT.ICON_WARNING) != 0) iconBits = OS.MB_ICONWARNING;
//	if ((style & SWT.ICON_WORKING) != 0) iconBits = OS.MB_ICONINFORMATION;

	int parentHandle = 0;
	if (parent != null && OS.PtWidgetIsRealized(parent.shellHandle)) {
		parentHandle = parent.shellHandle;
	}
	byte [] title = null;
	if (this.title != null) title = Converter.wcsToMbcs (null, this.title, true);
	byte [] message = null;
	message = Converter.wcsToMbcs (null, this.message, true);

	int[] buttonsPtr = new int [buttons.length];
	for (int i=0; i<buttons.length; i++) {
		byte[] text = Converter.wcsToMbcs (null, buttons [i], true);
		int textPtr = OS.malloc (text.length);
		OS.memmove (textPtr, text, text.length);
		buttonsPtr [i] = textPtr;
	}

	int button = OS.PtAlert (parentHandle, null, title, phImage, message, null, buttons.length, buttonsPtr, null, 0, buttons.length, OS.Pt_MODAL);

	for (int i=0; i<buttons.length; i++) {
		OS.free (buttonsPtr [i]);
	}

	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
		if (button == 1) return SWT.ABORT;
		if (button == 2) return SWT.RETRY;
		return SWT.IGNORE;
	}
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
		return (button == 1) ? SWT.RETRY : SWT.CANCEL;
	}
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
		if (button == 1) return SWT.YES;
		if (button == 2) return SWT.NO;
		return SWT.CANCEL;
	}
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
		return (button == 1) ? SWT.YES : SWT.NO;
	}
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) {
		return (button == 1) ? SWT.OK : SWT.CANCEL;
	}
	if ((style & SWT.OK) == SWT.OK && button == 1) return SWT.OK;
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

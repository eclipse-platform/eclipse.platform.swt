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
import org.eclipse.swt.internal.cocoa.*;

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
	Callback callback_completion_handler;
	String message = "";
	int userResponse;
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
	if (Display.getSheetEnabled ()) {
		if (parent != null && (style & SWT.SHEET) != 0) this.style |= SWT.SHEET;
	}
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

private int getBits () {
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	return bits;
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
	NSAlert alert = (NSAlert) new NSAlert().alloc().init();
	int alertType = OS.NSInformationalAlertStyle;
	if ((style & SWT.ICON_ERROR) != 0) {
		alertType = OS.NSCriticalAlertStyle;
	}
	if (((style & SWT.ICON_INFORMATION) != 0) || ((style & SWT.ICON_WORKING) != 0) || ((style & SWT.ICON_QUESTION) != 0)) {
		alertType = OS.NSInformationalAlertStyle;
		if (OS.isBigSurOrLater()) {
			alert.setIcon(NSImage.imageNamed(OS.NSImageNameInfo));
		} else {
			NSImage icon = Display.getSystemImageForID(OS.kAlertNoteIcon);
			alert.setIcon(icon);
			icon.release();
		}
	}
	if ((style & SWT.ICON_WARNING) != 0) {
		alertType = OS.NSWarningAlertStyle;
		if (OS.isBigSurOrLater()) {
			alert.setIcon(NSImage.imageNamed(OS.NSImageNameCaution));
		} else {
			NSImage icon = Display.getSystemImageForID(OS.kAlertCautionIcon);
			alert.setIcon(icon);
			icon.release();
		}
	}
	alert.setAlertStyle(alertType);

	int bits = getBits();
	NSString title;
	switch (bits) {
		case SWT.OK:
			title = getLabelForButton(SWT.OK, "SWT_OK");
			alert.addButtonWithTitle(title);
			break;
		case SWT.CANCEL:
			title = getLabelForButton(SWT.CANCEL, "SWT_Cancel");
			alert.addButtonWithTitle(title);
			break;
		case SWT.OK | SWT.CANCEL:
			title = getLabelForButton(SWT.OK, "SWT_OK");
			alert.addButtonWithTitle(title);
			title = getLabelForButton( SWT.CANCEL, "SWT_Cancel");
			alert.addButtonWithTitle(title);
			break;
		case SWT.YES:
			title = getLabelForButton(SWT.YES, "SWT_Yes");
			alert.addButtonWithTitle(title);
			break;
		case SWT.NO:
			title = getLabelForButton(SWT.NO, "SWT_No");
			alert.addButtonWithTitle(title);
			break;
		case SWT.YES | SWT.NO:
			title = getLabelForButton(SWT.YES, "SWT_Yes");
			alert.addButtonWithTitle(title);
			title = getLabelForButton(SWT.NO, "SWT_No");
			alert.addButtonWithTitle(title);
//			no.setKeyEquivalent(NSString.stringWith("\033"));
			break;
		case SWT.YES | SWT.NO | SWT.CANCEL:
			title = getLabelForButton(SWT.YES, "SWT_Yes");
			alert.addButtonWithTitle(title);
			title = getLabelForButton( SWT.CANCEL, "SWT_Cancel");
			alert.addButtonWithTitle(title);
			title = getLabelForButton(SWT.NO, "SWT_No");
			alert.addButtonWithTitle(title);
			break;
		case SWT.RETRY | SWT.CANCEL:
			title = getLabelForButton( SWT.RETRY, "SWT_Retry");
			alert.addButtonWithTitle(title);
			title = getLabelForButton( SWT.CANCEL, "SWT_Cancel");
			alert.addButtonWithTitle(title);
			break;
		case SWT.ABORT | SWT.RETRY | SWT.IGNORE:
			title = getLabelForButton( SWT.ABORT, "SWT_Abort");
			alert.addButtonWithTitle(title);
			title = getLabelForButton( SWT.IGNORE, "SWT_Ignore");
			alert.addButtonWithTitle(title);
			title = getLabelForButton( SWT.RETRY, "SWT_Retry");
			alert.addButtonWithTitle(title);
			break;
	}
	title = NSString.stringWith(this.title != null ? this.title : "");
	NSPanel panel = alert.window();
	panel.setTitle(title);
	NSString message = NSString.stringWith(this.message != null ? this.message : "");
	alert.setMessageText(message);

	long jniRef = 0;
	SWTPanelDelegate delegate = null;
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	if ((style & SWT.SHEET) != 0) {
		delegate = (SWTPanelDelegate)new SWTPanelDelegate().alloc().init();
		jniRef = OS.NewGlobalRef(this);
		if (jniRef == 0) error(SWT.ERROR_NO_HANDLES);
		OS.object_setInstanceVariable(delegate.id, Display.SWT_OBJECT, jniRef);
		display.setModalDialog(this, panel);
		callback_completion_handler = new Callback(this, "_completionHandler", 1);
		long handler = callback_completion_handler.getAddress();
		OS.beginSheetModalForWindow(alert, parent.window, handler);

		if ((style & SWT.APPLICATION_MODAL) != 0) {
			alert.runModal();
		} else {
			NSWindow window = alert.window();
			while (window.isVisible()) {
				if (!display.readAndDispatch()) display.sleep();
			}
		}
	} else {
		display.setModalDialog(this, panel);
		int response = (int)alert.runModal();
		userResponse = handleResponse(bits, response);
	}
	display.setModalDialog(null);
	if (delegate != null) delegate.release();
	if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
	alert.release();
	releaseHandler();
	return userResponse;
}

private NSString getLabelForButton(int buttonId, String messageId) {
	if (labels !=null && labels.containsKey(buttonId) && labels.get(buttonId) != null) {
		return NSString.stringWith(labels.get(buttonId));
	}
	return NSString.stringWith(SWT.getMessage(messageId));
}

long _completionHandler (long result) {
	NSApplication.sharedApplication().stopModal();
	userResponse = handleResponse(getBits(), (int)result);
	return result;
}

int handleResponse (int bits, int response) {
	switch (bits) {
		case SWT.OK:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.OK;
			}
			break;
		case SWT.CANCEL:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.CANCEL;
			}
			break;
		case SWT.OK | SWT.CANCEL:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.OK;
				case OS.NSAlertSecondButtonReturn:
					return SWT.CANCEL;
			}
			break;
		case SWT.YES:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.YES;
			}
			break;
		case SWT.NO:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.NO;
			}
			break;
		case SWT.YES | SWT.NO:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.YES;
				case OS.NSAlertSecondButtonReturn:
					return SWT.NO;
			}
			break;
		case SWT.YES | SWT.NO | SWT.CANCEL:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.YES;
				case OS.NSAlertSecondButtonReturn:
					return SWT.CANCEL;
				case OS.NSAlertThirdButtonReturn:
					return SWT.NO;
			}
			break;
		case SWT.RETRY | SWT.CANCEL:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.RETRY;
				case OS.NSAlertSecondButtonReturn:
					return SWT.CANCEL;
			}
			break;
		case SWT.ABORT | SWT.RETRY | SWT.IGNORE:
			switch (response) {
				case OS.NSAlertFirstButtonReturn:
					return SWT.ABORT;
				case OS.NSAlertSecondButtonReturn:
					return SWT.IGNORE;
				case OS.NSAlertThirdButtonReturn:
					return SWT.RETRY;
			}
			break;
	}
	return SWT.CANCEL;
}

void releaseHandler () {
	if (callback_completion_handler != null) {
		callback_completion_handler.dispose();
		callback_completion_handler = null;
	}
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

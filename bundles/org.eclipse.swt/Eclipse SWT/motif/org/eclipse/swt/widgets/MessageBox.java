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
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

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
	int button;
	String message = ""; //$NON-NLS-1$

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
int activate (int widget, int client, int call) {
	OS.XtUnmanageChild (widget);
	button = client;
	return 0;
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
int createHandle (int parentHandle, int [] argList) {
	if ((style & SWT.ICON_ERROR) != 0) return OS.XmCreateErrorDialog (parentHandle, null, argList, argList.length / 2);
	if ((style & SWT.ICON_INFORMATION) != 0) return OS.XmCreateInformationDialog (parentHandle, null, argList, argList.length / 2);
	if ((style & SWT.ICON_QUESTION) != 0) return OS.XmCreateQuestionDialog (parentHandle, null, argList, argList.length / 2);
	if ((style & SWT.ICON_WARNING) != 0) return OS.XmCreateWarningDialog (parentHandle, null, argList, argList.length / 2);
	if ((style & SWT.ICON_WORKING) != 0) return OS.XmCreateWorkingDialog (parentHandle, null, argList, argList.length / 2);
	return OS.XmCreateMessageDialog (parentHandle, null, argList, argList.length / 2);
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
	/* Compute the dialog title */	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	String string = title;
	if (string.length () == 0) string = " "; //$NON-NLS-1$
	/* Use the character encoding for the default locale */
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmStringPtr = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);
	/*
	* Feature in Motif.  The modal values are only hints
	* to the window manager.  For example Enlightenment treats all modes 
	* as SWT.APPLICATION_MODAL.  The generic Motif
	* Window Manager honours all modes.
	*/
	int dialogStyle = OS.XmDIALOG_MODELESS;
	if ((style & SWT.PRIMARY_MODAL) != 0) dialogStyle = OS.XmDIALOG_PRIMARY_APPLICATION_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) dialogStyle = OS.XmDIALOG_FULL_APPLICATION_MODAL;
	if ((style & SWT.SYSTEM_MODAL) != 0) dialogStyle = OS.XmDIALOG_SYSTEM_MODAL;
	if (dialogStyle == OS.XmDIALOG_MODELESS) {
		dialogStyle = OS.XmDIALOG_PRIMARY_APPLICATION_MODAL;
	}
	boolean defaultPos = parent.isVisible ();
	int [] argList = {
		OS.XmNnoResize, 1,
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNdialogStyle, dialogStyle,
		OS.XmNdialogTitle, xmStringPtr,
		OS.XmNdefaultPosition, defaultPos ? 1 : 0,
	};
	int parentHandle = parent.shellHandle;
	int dialog = createHandle (parentHandle, argList);
	if (dialog == 0) error (SWT.ERROR_NO_HANDLES);
	OS.XmStringFree (xmStringPtr);
	setMessage (dialog);
	setButtons (dialog);
	
	/* Hook the callbacks. */
	Callback callback = new Callback (this, "activate", 3); //$NON-NLS-1$
	int address = callback.getAddress ();
	if (address == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.XtAddCallback (dialog, OS.XmNokCallback, address, OS.XmDIALOG_OK_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNcancelCallback, address, OS.XmDIALOG_CANCEL_BUTTON);
	OS.XtAddCallback (dialog, OS.XmNhelpCallback, address, OS.XmDIALOG_HELP_BUTTON);

	/* Open the dialog and dispatch events. */
	if (!defaultPos) {
		OS.XtRealizeWidget (dialog);
		int[] argList1 = new int[] {
			OS.XmNwidth, 0,
			OS.XmNheight, 0,
		};
		OS.XtGetValues (dialog, argList1, argList1.length / 2);
		Monitor monitor = parent.getMonitor ();
		Rectangle bounds = monitor.getBounds ();
		int x = bounds.x + (bounds.width - argList1 [1]) / 2;
		int y = bounds.y + (bounds.height - argList1 [3]) / 2;
		int[] argList2 = new int[] {
			OS.XmNx, x,
			OS.XmNy, y,
		};
		OS.XtSetValues (dialog, argList2, argList2.length / 2);
	}
	OS.XtManageChild (dialog);

	// Should be a pure OS message loop (no SWT AppContext)
	Display display = parent.display;
	while (OS.XtIsRealized (dialog) && OS.XtIsManaged (dialog))
		if (!display.readAndDispatch()) display.sleep ();

	/* Destroy the dialog and update the display. */
	if (OS.XtIsRealized (dialog)) OS.XtDestroyWidget (dialog);
	callback.dispose ();

	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
		if (button == OS.XmDIALOG_OK_BUTTON) return SWT.YES;
		if (button == OS.XmDIALOG_CANCEL_BUTTON) return SWT.NO;
		return SWT.CANCEL;
	}
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
		return (button == OS.XmDIALOG_OK_BUTTON) ? SWT.YES : SWT.NO;
	}
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) {
		return (button == OS.XmDIALOG_OK_BUTTON) ? SWT.OK : SWT.CANCEL;
	}
	if ((style & SWT.OK) == SWT.OK) return SWT.OK;
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
		return (button == OS.XmDIALOG_OK_BUTTON) ? SWT.RETRY : SWT.CANCEL;
	}
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
		if (button == OS.XmDIALOG_OK_BUTTON) return SWT.ABORT;
		if (button == OS.XmDIALOG_CANCEL_BUTTON) return SWT.RETRY;
		return SWT.IGNORE;
	}
	return SWT.CANCEL;
}
void setButtons (int dialogHandle) {
	
	/* Get the button children */
	OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_OK_BUTTON);
	int cancel = OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_CANCEL_BUTTON);
	int help = OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_HELP_BUTTON);
	OS.XtUnmanageChild (help);

	/* Set the button labels */
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) return;
	if ((style & SWT.OK) == SWT.OK) {
		OS.XtUnmanageChild (cancel);
		return;
	}
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
		OS.XtManageChild (help);
		/* Use the character encoding for the default locale */
		byte [] buffer1 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Yes"), true); //$NON-NLS-1$
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		/* Use the character encoding for the default locale */
		byte [] buffer2 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_No"), true); //$NON-NLS-1$
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		/* Use the character encoding for the default locale */
		byte [] buffer3 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Cancel"), true); //$NON-NLS-1$
		int xmString3 = OS.XmStringParseText (
			buffer3,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2, OS.XmNhelpLabelString, xmString3};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2);  OS.XmStringFree (xmString3);
		return;
	}
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
		/* Use the character encoding for the default locale */
		byte [] buffer1 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Yes"), true); //$NON-NLS-1$
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		/* Use the character encoding for the default locale */
		byte [] buffer2 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_No"), true); //$NON-NLS-1$
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2);
		return;
	}
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
		/* Use the character encoding for the default locale */
		byte [] buffer1 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Retry"), true); //$NON-NLS-1$
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		/* Use the character encoding for the default locale */
		byte [] buffer2 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Cancel"), true); //$NON-NLS-1$
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2);
		return;		
	}
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
		OS.XtManageChild (help);
		/* Use the character encoding for the default locale */
		byte [] buffer1 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Abort"), true); //$NON-NLS-1$
		int xmString1 = OS.XmStringParseText (
			buffer1,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		/* Use the character encoding for the default locale */
		byte [] buffer2 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Retry"), true); //$NON-NLS-1$
		int xmString2 = OS.XmStringParseText (
			buffer2,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		/* Use the character encoding for the default locale */
		byte [] buffer3 = Converter.wcsToMbcs (null, SWT.getMessage("SWT_Ignore"), true); //$NON-NLS-1$
		int xmString3 = OS.XmStringParseText (
			buffer3,
			0,
			OS.XmFONTLIST_DEFAULT_TAG, 
			OS.XmCHARSET_TEXT, 
			null,
			0,
			0);
		int [] argList = {OS.XmNokLabelString, xmString1, OS.XmNcancelLabelString, xmString2, OS.XmNhelpLabelString, xmString3};
		OS.XtSetValues (dialogHandle, argList, argList.length / 2);
		OS.XmStringFree (xmString1);  OS.XmStringFree (xmString2); OS.XmStringFree (xmString3);
		return;		
	}
}
void setMessage (int dialogHandle) {
	String text = message;
	int label = OS.XmMessageBoxGetChild (dialogHandle, OS.XmDIALOG_MESSAGE_LABEL);
	if (label != 0) {
		int [] argList = {OS.XmNfontList, 0};
		OS.XtGetValues (label, argList, argList.length / 2);
		int fontList = argList [1];
		if (fontList != 0) {
			Display display = parent.display;
			int xDisplay = display.xDisplay;
			int screen = OS.XDefaultScreen (xDisplay);
			int width = OS.XDisplayWidth (xDisplay, screen);
			Font font = Font.motif_new (display, fontList);
			text = display.wrapText (message, font, width * 3 / 5);
		}
	}
	/* Use the character encoding for the default locale */
	byte [] buffer = Converter.wcsToMbcs (null, text, true);
	int xmString = OS.XmStringGenerate(buffer, null, OS.XmCHARSET_TEXT, null);
	int [] argList = {OS.XmNmessageString, xmString};
	OS.XtSetValues (dialogHandle, argList, argList.length / 2);
	OS.XmStringFree (xmString);
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

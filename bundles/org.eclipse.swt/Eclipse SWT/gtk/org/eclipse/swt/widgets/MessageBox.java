package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class MessageBox extends Dialog {

	String message;

	// Handles
	int handle;
	int label;
	int buttonOK, buttonCANCEL, buttonYES, buttonNO, buttonABORT, buttonRETRY, buttonIGNORE;

	// While open...
	boolean isWaiting = false;
	int result;

/*
 *   ===  CONSTRUCTORS  ===
 */


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
	createHandle();
}



/*
 *   ===  GET/SET MESSAGE;  OPEN  ===
 */

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
	OS.gtk_widget_show (handle);

// FIXME - WAIT FOR CLICK
// There is a fundamental problem here.
// We can't receive the clicked signal from the buttons, because by the API shape,
// we are not a widget.

	OS.gtk_widget_hide(handle);
	return SWT.OK;
}

/*
 *   ===  HANDLE DANCES  ===
 */

private void createHandle() {
	handle = OS.gtk_dialog_new();
	byte[] bytes = Converter.wcsToMbcs (null, getText(), true);

	label = OS.gtk_label_new (bytes);
	GtkDialog dialog = new GtkDialog();
	OS.memmove (dialog, handle, GtkDialog.sizeof);
	OS.gtk_box_pack_start (dialog.vbox, label, true, true, 5);
	OS.gtk_widget_show (label);
	
	if ((style & SWT.OK) != 0) buttonOK = createButton("OK");
	if ((style & SWT.CANCEL) != 0) buttonCANCEL = createButton("CANCEL");

	if ((style & SWT.YES) != 0) buttonYES = createButton("YES");
	if ((style & SWT.NO) != 0) buttonNO = createButton("NO");

	if ((style & SWT.ABORT) != 0) buttonABORT = createButton("ABORT");
	if ((style & SWT.RETRY) != 0) buttonRETRY = createButton("RETRY");
	if ((style & SWT.IGNORE) != 0) buttonIGNORE = createButton("IGNORE");
}

int createButton(String buttonName) {
	byte[] bytes = Converter.wcsToMbcs (null, buttonName, true);
	int h = OS.gtk_button_new_with_label(bytes);
	GtkDialog dialog = new GtkDialog();
	OS.memmove (dialog, handle, GtkDialog.sizeof);
	OS.gtk_box_pack_start (dialog.action_area, h, true, true, 0);
	signal_connect(h, "clicked", SWT.Selection, 2);
//	WidgetTable.put (h, this);
	OS.gtk_widget_show (h);
	return h;
}

int processEvent (int eventNumber, int int0, int int1, int int2) {
	if (eventNumber == SWT.Selection) processSelection (int0, int1, int2);
	return 0;
}

void processSelection (int int0, int int1, int int2) {
}

/*
 *   ===  AS YET UNCLASSIFIED  ==
 */

private static int checkStyle (int style) {
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) return style;
	if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) return style;
	if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) return style;
	style = (style & ~mask) | SWT.OK;
	return style;
}
private void signal_connect (int handle, String eventName, int swtEvent, int numArgs) {
	byte [] buffer = Converter.wcsToMbcs (null, eventName, true);
	int proc=0;
	switch (numArgs) {
		case 2: proc=Display.getDefault().windowProc2; break;
		case 3: proc=Display.getDefault().windowProc3; break;
		case 4: proc=Display.getDefault().windowProc4; break;
		case 5: proc=Display.getDefault().windowProc5; break;
		default: error(SWT.ERROR_INVALID_ARGUMENT);
	}
	OS.gtk_signal_connect (handle, buffer, proc, swtEvent);
}
}

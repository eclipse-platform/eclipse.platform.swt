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

	int state;

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
	checkSubclass ();
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
	createHandle();
	createMessage(); // includes configuring
	createActionButtons();
	state=0;
	Callback destroyCallback = new Callback (this, "destroyFunc", 2);
	int destroyFunc = destroyCallback.getAddress ();
	byte [] destroy = Converter.wcsToMbcs (null, "delete_event", true);
	OS.gtk_signal_connect_after (handle, destroy, destroyFunc, handle);
	showHandle();
	while(state==0) OS.gtk_main_iteration();
	OS.gtk_widget_destroy(handle);
	return state;
}

/*
 *   ===  Actual handle operations  ===
 */

private void createHandle() {
	handle = OS.gtk_dialog_new();
	if (handle==0) SWT.error(SWT.ERROR_NO_HANDLES);
	if ((style & (SWT.PRIMARY_MODAL|SWT.APPLICATION_MODAL|SWT.SYSTEM_MODAL)) != 0) {
		OS.gtk_window_set_modal(handle, true);
	}
}
private void createMessage() {
	byte[] bytes = Converter.wcsToMbcs (null, getMessage(), true);
	label = OS.gtk_label_new (bytes);
	GtkDialog dialog = new GtkDialog();
	OS.memmove (dialog, handle, GtkDialog.sizeof);
	OS.gtk_box_pack_start (dialog.vbox, label, true, true, 5); // FIXME should we use container_add??
}
private void createActionButtons() {	
	if ((style & SWT.OK) != 0) buttonOK = createButton("OK");
	if ((style & SWT.CANCEL) != 0) buttonCANCEL = createButton("CANCEL");

	if ((style & SWT.YES) != 0) buttonYES = createButton("YES");
	if ((style & SWT.NO) != 0) buttonNO = createButton("NO");

	if ((style & SWT.ABORT) != 0) buttonABORT = createButton("ABORT");
	if ((style & SWT.RETRY) != 0) buttonRETRY = createButton("RETRY");
	if ((style & SWT.IGNORE) != 0) buttonIGNORE = createButton("IGNORE");
}
private void showHandle() {
	OS.gtk_widget_show_all (handle);
	
	boolean hasTitle=false;
	String title = getText();
	if (title!=null) if (title.length()!=0) hasTitle=true;
	
	int decor = 0;
	if (hasTitle) decor |= OS.GDK_DECOR_TITLE;
	GtkWidget widget = new GtkWidget();
	OS.memmove(widget, handle, GtkWidget.sizeof);
	int gdkWindow = widget.window;
	OS.gdk_window_set_decorations(gdkWindow, decor);
	if (hasTitle) {
		byte[] bytes = Converter.wcsToMbcs (null, title, true);
		OS.gtk_window_set_title(handle, bytes);
	}
}
int createButton(String buttonName) {
	System.out.println("Creating button "+buttonName);
	byte[] bytes = Converter.wcsToMbcs (null, buttonName, true);
	int buttonHandle = OS.gtk_button_new_with_label(bytes);
	GtkDialog dialog = new GtkDialog();
	OS.memmove (dialog, handle, GtkDialog.sizeof);
	OS.gtk_box_pack_start (dialog.action_area, buttonHandle, true, true, 0);
	hookSelection(buttonHandle);
	return buttonHandle;
}
private void hookSelection(int h) {
	byte [] clicked = Converter.wcsToMbcs (null, "clicked", true);
	
	Callback okCallback = new Callback (this, "activateFunc", 2);
	int okFunc = okCallback.getAddress ();
	OS.gtk_signal_connect (h, clicked, okFunc, h);

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
int activateFunc(int widget, int callData) {
	if (widget==buttonOK)     { state=SWT.OK;     return 0; }
	if (widget==buttonCANCEL) { state=SWT.CANCEL; return 0; }
	if (widget==buttonYES)    { state=SWT.YES;    return 0; }
	if (widget==buttonNO)     { state=SWT.NO;     return 0; }
	if (widget==buttonABORT)  { state=SWT.ABORT;  return 0; }
	if (widget==buttonRETRY)  { state=SWT.RETRY;  return 0; }
	if (widget==buttonIGNORE) { state=SWT.IGNORE; return 0; }
	return 0;
}
/*
 * We need this because some WMs will give us a cross even when
 * we specifically ask it not to.
 */
int destroyFunc(int widget, int callData) {
	return 1;
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify numeric
 * values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify, Verify</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#spinner">Spinner snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Spinner extends Composite {
	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 * 
	 * @since 3.4
	 */
	public static final int LIMIT;
	
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
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
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Spinner (Composite parent, int style) {
	super (parent, checkStyle (style));
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #removeVerifyListener
 */
void addVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}
static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int width = wHint;
	int height = hHint;
	if (wHint == SWT.DEFAULT) {
		width = DEFAULT_WIDTH;
		int [] argList = {
				OS.XmNmaximumValue, 0,
				OS.XmNdecimalPoints, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		String string = String.valueOf (argList [1]);
		if (argList [3] > 0) {
			StringBuffer buffer = new StringBuffer ();
			buffer.append (string);
			buffer.append (getDecimalSeparator ());
			int count = argList [3] - string.length ();
			while (count >= 0) {
				buffer.append ("0");
				count--;
			}
			string = buffer.toString ();
		}
		byte [] buffer = Converter.wcsToMbcs (getCodePage(), string, true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		int fontList = font.handle;
		width = OS.XmStringWidth (fontList, xmString);
		OS.XmStringFree (xmString);
	}
	if (hHint == SWT.DEFAULT) {
		height = getFontHeight (font.handle);
	}
	Rectangle trim = computeTrim (0, 0, width, height);
	if (hHint == SWT.DEFAULT) {
		int [] argList = {OS.XmNarrowSize, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		trim.height = Math.max (trim.height, argList [1] * 2);
	}
	return new Point (trim.width, trim.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	int [] argList1 = {
			OS.XmNtextField, 0, 
			OS.XmNarrowSize, 0, 
			OS.XmNmarginWidth, 0, 
			OS.XmNmarginHeight, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);	
	int [] argList2 = {
			OS.XmNshadowThickness, 0,
			OS.XmNhighlightThickness, 0};
	OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
	XRectangle rect = new XRectangle ();
	OS.XmWidgetGetDisplayRect (argList1 [1], rect);
	x -= argList1 [5] + argList2 [1] + argList2 [3] + rect.x;
	y -= argList1 [7] + argList2 [1] + argList2 [3] + rect.y;
	width += (argList1 [5] + argList2 [1] + argList2 [3] + rect.x) * 2 + argList1 [3];
	height += (argList1 [7] + argList2 [1] + argList2 [3] + rect.y) * 2;
	return new Rectangle (x, y, width, height);
}
/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void copy () {
	checkWidget ();
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextCopy (argList [1], OS.XtLastTimestampProcessed (xDisplay));
}
void createHandle (int index) {
	int [] argList1 = {
		OS.XmNcolumns, 2,
		OS.XmNdecimalPoints, 0,
		OS.XmNincrementValue, 1,
		OS.XmNminimumValue, 0,
		OS.XmNmaximumValue, 100,
		OS.XmNspinBoxChildType, OS.XmNUMERIC,
		OS.XmNeditable, (style & SWT.READ_ONLY) != 0 ? 0 : 1,
		OS.XmNshadowThickness, 0,
		OS.XmNancestorSensitive, 1,
	};
	int parentHandle = parent.handle;
	handle = OS.XmCreateSimpleSpinBox (parentHandle, null, argList1, argList1.length / 2);
	int [] argList2 = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList2, argList2.length / 2);	
	int textHandle = argList2 [1];
	int [] argList3 = {
			OS.XmNverifyBell, 0,
			OS.XmNcursorPositionVisible, (style & SWT.READ_ONLY) != 0 ? 0 : 1,
	};
	OS.XtSetValues (textHandle, argList3, argList3.length / 2);
	if ((style & SWT.BORDER) == 0) {
		int [] argList4 = new int [] {
			/*
			* Bug in Motif.  Setting the margin width to zero for
			* a single line text field causes the field to draw
			* garbage when the caret is placed at the start of
			* the widget.  The fix is to not set the margin width.
			*/
//			OS.XmNmarginWidth, 0,
			OS.XmNmarginHeight, 0,
			OS.XmNshadowThickness, 0,
		};
		OS.XtSetValues (textHandle, argList4, argList4.length / 2);
	}
	/*
	* Feature in Motif.  The Spinner widget is created with a default
	* drop target.  This is inconsistent with other platforms.
	* To be consistent, disable the default drop target.
	*/
	OS.XmDropSiteUnregister (textHandle);
}
/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget ();
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextCut (argList [1], OS.XtLastTimestampProcessed (xDisplay));
}
void deregister () {
	super.deregister ();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	display.removeWidget (argList[1]);
}
int fontHandle () {
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
 * Returns the number of decimal places used by the receiver.
 *
 * @return the digits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDigits () {
	checkWidget ();
	int [] argList = {OS.XmNdecimalPoints, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
String getDecimalSeparator () {
	int ptr = OS.localeconv_decimal_point ();
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);	
	return new String (Converter.mbcsToWcs (null, buffer));
}
/**
 * Returns the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed.
 *
 * @return the increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getIncrement () {
	checkWidget ();
	int [] argList = {OS.XmNincrementValue, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
 * Returns the maximum value which the receiver will allow.
 *
 * @return the maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMaximum () {
	checkWidget ();
	int [] argList = {OS.XmNmaximumValue, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
 * Returns the minimum value which the receiver will allow.
 *
 * @return the minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinimum () {
	checkWidget ();
	int [] argList = {OS.XmNminimumValue, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
 * Returns the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed.
 *
 * @return the page increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getPageIncrement () {
	checkWidget ();
	return 1;
}
/**
 * Returns the <em>selection</em>, which is the receiver's position.
 *
 * @return the selection 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelection () {
	checkWidget ();	
	int [] argList = {OS.XmNposition, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public String getText () {
	checkWidget();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	
	int ptr = OS.XmTextGetString (argList[1]);
	if (ptr == 0) return "";
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);
	OS.XtFree (ptr);
	return new String (Converter.mbcsToWcs (getCodePage (), buffer));
}
/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Spinner.LIMIT</code>.
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #LIMIT
 * 
 * @since 3.4
 */
public int getTextLimit () {
	checkWidget();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return OS.XmTextGetMaxLength (argList[1]);
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int textHandle = argList [1];
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
	OS.XtAddCallback (handle, OS.XmNmodifyVerifyCallback, windowProc, MODIFY_VERIFY_CALLBACK);	
	OS.XtAddCallback (textHandle, OS.XmNactivateCallback, windowProc, ACTIVATE_CALLBACK);
	OS.XtAddCallback (textHandle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
	OS.XtAddCallback (textHandle, OS.XmNmodifyVerifyCallback, windowProc, MODIFY_VERIFY_CALLBACK);
	OS.XtAddEventHandler (textHandle, OS.ButtonPressMask, false, windowProc, BUTTON_PRESS);
	OS.XtAddEventHandler (textHandle, OS.ButtonReleaseMask, false, windowProc, BUTTON_RELEASE);
	OS.XtAddEventHandler (textHandle, OS.EnterWindowMask, false, windowProc, ENTER_WINDOW);
	OS.XtAddEventHandler (textHandle, OS.LeaveWindowMask, false, windowProc, LEAVE_WINDOW);
	OS.XtAddEventHandler (textHandle, OS.KeyPressMask, false, windowProc, KEY_PRESS);
	OS.XtAddEventHandler (textHandle, OS.KeyReleaseMask, false, windowProc, KEY_RELEASE);
	OS.XtInsertEventHandler (textHandle, OS.FocusChangeMask, false, windowProc, FOCUS_CHANGE, OS.XtListTail);
}
/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void paste () {
	checkWidget ();
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextFieldPaste (argList [1]);
	display.setWarnings (warnings);	
}
void register () {
	super.register ();
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	display.addWidget (argList [1], this);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #addVerifyListener
 */
void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	int [] argList1 = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int textHandle = argList1 [1];
	int [] argList2 = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (textHandle, argList2, argList2.length / 2);
	OS.XmChangeColor (textHandle, pixel);
	OS.XtSetValues (textHandle, argList2, argList2.length / 2);
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	/* 
	* Feature in Motif. Setting the bounds of a XmSimpleSpinBox
	* does not update the size of the inner XmTextFied. The fix
	* is to update its size programmatically.
	*/
	if (resize) {
		int [] argList1 = {
				OS.XmNtextField, 0, 
				OS.XmNarrowSize, 0, 
				OS.XmNmarginWidth, 0, 
				OS.XmNmarginHeight, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		int [] argList2 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
		OS.XtGetValues (argList1 [1], argList2, argList2.length / 2);
		int textWidth = Math.max (width - argList1 [3] - 2 * argList1 [5], 0);
		int textHeight = Math.max (height - 2 * argList1 [7], 0);
		if (textWidth != argList2 [1] || textHeight != argList2 [3]) {
			OS.XtResizeWidget (argList1 [1], textWidth, textHeight, argList2 [5]);
		}
	}
	return super.setBounds (x, y, width, height, move, resize);
}
/**
 * Sets the number of decimal places used by the receiver.
 * <p>
 * The digit setting is used to allow for floating point values in the receiver.
 * For example, to set the selection to a floating point value of 1.37 call setDigits() with 
 * a value of 2 and setSelection() with a value of 137. Similarly, if getDigits() has a value
 * of 2 and getSelection() returns 137 this should be interpreted as 1.37. This applies to all
 * numeric APIs. 
 * </p>
 * 
 * @param value the new digits (must be greater than or equal to zero)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the value is less than zero</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDigits (int value) {
	checkWidget ();
	if (value < 0) error (SWT.ERROR_INVALID_ARGUMENT);
	int [] argList1 = {OS.XmNposition, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int [] argList2 = {OS.XmNdecimalPoints, value, OS.XmNposition, argList1 [1]};
	OS.XtSetValues (handle, argList2, argList2.length / 2);
}
void setForegroundPixel (int pixel) {
	int [] argList1 = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	int [] argList2 = {OS.XmNforeground, pixel};
	OS.XtSetValues (argList1 [1], argList2, argList2.length / 2);
	super.setForegroundPixel (pixel);
}
/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed to
 * the argument, which must be at least one.
 *
 * @param value the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	int [] argList = {OS.XmNincrementValue, value};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is not greater than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	int [] argList1 = {OS.XmNminimumValue, 0, OS.XmNposition, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);	
	if (value <= argList1 [1]) return;
	int position = argList1 [3];
	if (value < position) position = value;
	int [] argList2 = {OS.XmNposition, position, OS.XmNmaximumValue, value};
	OS.XtSetValues (handle, argList2, argList2.length / 2);
}
/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is not less than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be less than the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	int [] argList1 = {OS.XmNmaximumValue, 0, OS.XmNposition, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	if (value >= argList1 [1]) return;
	int position = argList1 [3];
	if (value > position) position = value;
	int [] argList2 = {OS.XmNposition, position, OS.XmNminimumValue, value};
	OS.XtSetValues (handle, argList2, argList2.length / 2);
}
/**
 * Sets the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed
 * to the argument, which must be at least one.
 *
 * @param value the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
}
/**
 * Sets the <em>selection</em>, which is the receiver's
 * position, to the argument. If the argument is not within
 * the range specified by minimum and maximum, it will be
 * adjusted to fall within this range.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int value) {
	checkWidget ();
	int [] argList = {OS.XmNmaximumValue, 0, OS.XmNminimumValue, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	value = Math.min (Math.max (argList [3], value), argList [1]);
	int [] argList1 = {OS.XmNposition, value};
	OS.XtSetValues (handle, argList1, argList1.length / 2);
}
/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Spinner.LIMIT)</code>.
 * Specifying a limit value larger than <code>Spinner.LIMIT</code> sets the
 * receiver's limit to <code>Spinner.LIMIT</code>.
 * </p>
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #LIMIT
 * 
 * @since 3.4
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmTextSetMaxLength (argList[1], limit);
}
/**
 * Sets the receiver's selection, minimum value, maximum
 * value, digits, increment and page increment all at once.
 * <p>
 * Note: This is similar to setting the values individually
 * using the appropriate methods, but may be implemented in a 
 * more efficient fashion on some platforms.
 * </p>
 *
 * @param selection the new selection value
 * @param minimum the new minimum value
 * @param maximum the new maximum value
 * @param digits the new digits value
 * @param increment the new increment value
 * @param pageIncrement the new pageIncrement value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setValues (int selection, int minimum, int maximum, int digits, int increment, int pageIncrement) {
	checkWidget ();
	if (maximum <= minimum) return;
	if (digits < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	selection = Math.min (Math.max (minimum, selection), maximum);
	int [] argList = {
			OS.XmNposition, selection, 
			OS.XmNmaximumValue, maximum,
			OS.XmNminimumValue, minimum,
			OS.XmNincrementValue, increment,
			OS.XmNdecimalPoints, digits};
	OS.XtSetValues (handle, argList, argList.length / 2);	
}

void updateText () {
	int [] argList = {
			OS.XmNtextField, 0,		/* 1 */
			OS.XmNminimumValue, 0,	/* 3 */
			OS.XmNmaximumValue, 0,	/* 5 */
			OS.XmNposition, 0,		/* 7 */
			OS.XmNdecimalPoints, 0	/* 9 */};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = OS.XmTextGetString (argList [1]);
	int position = argList [7];
	int digits = argList [9];
	if (ptr != 0) {
		int length = OS.strlen (ptr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		OS.XtFree (ptr);
		String string = new String (Converter.mbcsToWcs (getCodePage (), buffer));
		try {
			int value;
			if (digits > 0) {
				String decimalSeparator = getDecimalSeparator ();
				int index = string.indexOf (decimalSeparator);
				if (index != -1)  {
					int startIndex = string.startsWith ("+") || string.startsWith ("-") ? 1 : 0;
					String wholePart = startIndex != index ? string.substring (startIndex, index) : "0";
					String decimalPart = string.substring (index + 1);
					if (decimalPart.length () > digits) {
						decimalPart = decimalPart.substring (0, digits);
					} else {
						int i = digits - decimalPart.length ();
						for (int j = 0; j < i; j++) {
							decimalPart = decimalPart + "0";
						}
					}
					int wholeValue = Integer.parseInt (wholePart);
					int decimalValue = Integer.parseInt (decimalPart);
					for (int i = 0; i < digits; i++) wholeValue *= 10;
					value = wholeValue + decimalValue;
					if (string.startsWith ("-")) value = -value;
				} else {
					value = Integer.parseInt (string);
					for (int i = 0; i < digits; i++) value *= 10;
				}
			} else {
				value = Integer.parseInt (string);
			}
			if (argList [3] <= value && value <= argList [5]) {
				position = value;
			}
		} catch (NumberFormatException e) {
		}
	}
	if (position == argList [7]) {
		String string;
		if (digits == 0) {
			string = String.valueOf (position);
		} else {	
			string = String.valueOf (Math.abs (position));
			String decimalSeparator = getDecimalSeparator ();
			int index = string.length () - digits;
			StringBuffer buffer = new StringBuffer ();
			if (position < 0) buffer.append ("-");
			if (index > 0) {
				buffer.append (string.substring (0, index));
				buffer.append (decimalSeparator);
				buffer.append (string.substring (index));
			} else {
				buffer.append ("0");
				buffer.append (decimalSeparator);
				while (index++ < 0) buffer.append ("0");
				buffer.append (string);
			}
			string = buffer.toString ();
		}
		byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.XmTextSetString (argList [1], buffer);
		display.setWarnings (warnings);	
	} else {
		int [] argList2 = {OS.XmNposition, position};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	}
}
int XmNactivateCallback (int w, int client_data, int call_data) {
	postEvent (SWT.DefaultSelection);
	updateText ();
	return 0;
}
int xFocusOut (XFocusChangeEvent xEvent) {
	updateText ();
	return super.xFocusOut (xEvent);
}
int XmNmodifyVerifyCallback (int w, int client_data, int call_data) {
	if (w == handle) {
		if ((style & SWT.WRAP) == 0) {
			XmSpinBoxCallbackStruct struct = new XmSpinBoxCallbackStruct ();
			OS.memmove (struct, call_data, XmSpinBoxCallbackStruct.sizeof);
			if (struct.crossed_boundary != 0) {
				struct.doit = (byte) 0;
				OS.memmove (call_data, struct, XmSpinBoxCallbackStruct.sizeof);
			}
		}
		return 0;
	}
	int result = super.XmNmodifyVerifyCallback (w, client_data, call_data);
	if (result != 0) return result;
	
	/*
	* Feature in Motif.  When XtManageChild() is called for
	* a text widget that has just been created, the contents
	* are assigned and an XmNmodifyVerifyCallback is sent.
	* When this happens, the widget has not been fully
	* initialized null pointer exceptions can occur.  The
	* fix is to check for this case and avoid the callback.
	* Note that application code could never have seen it
	* in the first place.
	*/
	if (font == null) return result;
	
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return result;
	XmTextVerifyCallbackStruct textVerify = new XmTextVerifyCallbackStruct ();
	OS.memmove (textVerify, call_data, XmTextVerifyCallbackStruct.sizeof);
	XmTextBlockRec textBlock = new XmTextBlockRec ();
	OS.memmove (textBlock, textVerify.text, XmTextBlockRec.sizeof);
	byte [] buffer = new byte [textBlock.length];
	OS.memmove (buffer, textBlock.ptr, textBlock.length);
	String codePage = getCodePage ();
	String text = new String (Converter.mbcsToWcs (codePage, buffer));
	Event event = new Event ();
	if (textVerify.event != 0) {
		XKeyEvent xEvent = new XKeyEvent ();
		OS.memmove (xEvent, textVerify.event, XKeyEvent.sizeof);
		event.time = xEvent.time;
		setKeyState (event, xEvent);
	}
	event.start = textVerify.startPos;
	event.end = textVerify.endPos;
	event.text = text;
	String string = text;
	int index = 0;
	int [] argList = {OS.XmNdecimalPoints, 0, OS.XmNminimumValue, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] > 0) {
		String decimalSeparator = getDecimalSeparator ();
		index = string.indexOf (decimalSeparator);
		if (index != -1) {
			string = string.substring (0, index) + string.substring (index + 1);
		}
		index = 0;
	}
	if (string.length () > 0) {
		if (argList [3] < 0 && string.charAt (0) == '-') index++;
	}
	while (index < string.length ()) {
		if (!Character.isDigit (string.charAt (index))) break;
		index++;
	}
	event.doit = index == string.length ();
	sendEvent (SWT.Verify, event);
	String newText = event.text;
	textVerify.doit = (byte) ((event.doit && newText != null) ? 1 : 0);
	if (newText != null && newText != text) {
		OS.XtFree(textBlock.ptr);
		byte [] buffer2 = Converter.wcsToMbcs (codePage, newText, true);
		int length = buffer2.length;
		int ptr = OS.XtMalloc (length);
		OS.memmove (ptr, buffer2, length);
		textBlock.ptr = ptr;
		textBlock.length = buffer2.length - 1;
		OS.memmove (textVerify.text, textBlock, XmTextBlockRec.sizeof);
	}
	OS.memmove (call_data, textVerify, XmTextVerifyCallbackStruct.sizeof);
	return result;
}
int XmNvalueChangedCallback (int w, int client_data, int call_data) {
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (w == argList [1]) {
		sendEvent (SWT.Modify);
	} else {
		XmAnyCallbackStruct struct = new XmAnyCallbackStruct ();
		OS.memmove (struct, call_data, XmAnyCallbackStruct.sizeof);
		if (struct.reason == OS.XmCR_OK) {
			postEvent (SWT.Selection);
		}
	}
	return 0;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
 * objects that allow the user to enter and modify number
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
class Spinner extends Composite {	
	
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
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
 * </p>
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
		int [] argList = {OS.XmNmaximumValue, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		String string = String.valueOf (argList [1]);
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
	state |= HANDLE;
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
 * Returns the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected.
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
 * Returns the single <em>selection</em> that is the receiver's position.
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
void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	int [] argList = {OS.XmNtextField, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int textHandle = argList [1];
	OS.XtAddCallback (handle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
	OS.XtAddCallback (textHandle, OS.XmNactivateCallback, windowProc, ACTIVATE_CALLBACK);
	OS.XtAddCallback (textHandle, OS.XmNvalueChangedCallback, windowProc, VALUE_CHANGED_CALLBACK);
	OS.XtAddCallback (textHandle, OS.XmNmodifyVerifyCallback, windowProc, MODIFY_VERIFY_CALLBACK);
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
 * be notified when the control is selected.
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
		};
	}
	return super.setBounds (x, y, width, height, move, resize);
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
 * modified by when the up/down (or right/left) arrows
 * are pressed to the argument, which must be at least 
 * one.
 *
 * @param increment the new increment (must be greater than zero)
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
	if (value < 0) return;
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
 * value will be ignored if it is negative or is not less than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be nonnegative and less than the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	if (value < 0) return;
	int [] argList1 = {OS.XmNmaximumValue, 0, OS.XmNposition, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	if (value >= argList1 [1]) return;
	int position = argList1 [3];
	if (value > position) position = value;
	int [] argList2 = {OS.XmNposition, position, OS.XmNminimumValue, value};
	OS.XtSetValues (handle, argList2, argList2.length / 2);
}
/**
 * Sets the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected to the argument, which must be at least
 * one.
 *
 * @param pageIncrement the page increment (must be greater than zero)
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
 * Sets the single <em>selection</em> that is the receiver's
 * value to the argument which must be greater than or equal
 * to zero.
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
	int [] argList = {OS.XmNposition, value};
	OS.XtSetValues (handle, argList, argList.length / 2);	
}
void updateText () {
	int [] argList = {
			OS.XmNtextField, 0,		/* 1 */
			OS.XmNminimumValue, 0,	/* 3 */
			OS.XmNmaximumValue, 0,	/* 5 */
			OS.XmNposition, 0};		/* 7 */
	OS.XtGetValues (handle, argList, argList.length / 2);
	int ptr = OS.XmTextGetString (argList [1]);
	int position;
	if (ptr != 0) {
		int length = OS.strlen (ptr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		OS.XtFree (ptr);
		String string = new String (Converter.mbcsToWcs (getCodePage (), buffer));
		try {
			int value = Integer.parseInt (string);			
			if (argList [3] <= value && value <= argList [5]) {
				position = value;
			} else {
				position = argList [3];
			}
		} catch (NumberFormatException e) {
			position = argList [7];
			int [] argList2 = {OS.XmNposition, position + 1};
			OS.XtSetValues (handle, argList2, argList2.length / 2);
		}
	} else {
		position = argList [3];
	}
	int [] argList2 = {OS.XmNposition, position};
	OS.XtSetValues (handle, argList2, argList2.length / 2);	
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
	int result = super.XmNmodifyVerifyCallback (w, client_data, call_data);
	if (result != 0) return result;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return result;
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
	event.doit = textVerify.doit == 1;
	event.text = text;
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

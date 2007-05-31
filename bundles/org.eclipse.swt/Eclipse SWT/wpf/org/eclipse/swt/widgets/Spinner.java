/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.*;
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
 * @since 3.1
 */
public class Spinner extends Composite {
	int textHandle, upHandle, downHandle;
	int increment, pageIncrement, digits, max, min, value;
	
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

Control [] _getChildren () {
	return new Control [0];
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

int backgroundHandle () {
	return textHandle;
}

int backgroundProperty () {
	return OS.Control_BackgroundProperty ();
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	return super.computeSize (handle, wHint, hHint, changed);
}

int createArrow (int direction) { 
	int geometry = OS.gcnew_StreamGeometry ();
	int context = OS.StreamGeometry_Open (geometry);
	int start = 0, point = 0, end = 0;
	switch (direction) {
		case SWT.DOWN:
			start = OS.gcnew_Point (0, 0);
			point = OS.gcnew_Point (3, 3);
			end = OS.gcnew_Point (6, 0);
			break;
		case SWT.UP:
			start = OS.gcnew_Point (0, 3);
			point = OS.gcnew_Point (3, 0);
			end = OS.gcnew_Point (6, 3);
			break;
	}
	OS.StreamGeometryContext_BeginFigure (context, start, true, true);
	OS.StreamGeometryContext_LineTo (context, point, true, true);
	OS.StreamGeometryContext_LineTo (context, end, true, true);
	OS.StreamGeometryContext_Close (context);
	int path = OS.gcnew_Path ();
	OS.Path_Data (path, geometry);
	int padding = OS.gcnew_Thickness (3, 0, 3, 0);
	OS.FrameworkElement_Margin (path, padding);
	int brush = OS.Brushes_Black ();
	OS.Path_Fill (path, brush);
	OS.FrameworkElement_HorizontalAlignment (path, OS.HorizontalAlignment_Center);
	OS.FrameworkElement_VerticalAlignment (path, OS.VerticalAlignment_Center);
	OS.GCHandle_Free (padding);
	OS.GCHandle_Free (start);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (end);
	OS.GCHandle_Free (brush);
	OS.GCHandle_Free (context);
	OS.GCHandle_Free (geometry);
	return path;
}

void createHandle () {
	handle = OS.gcnew_Grid ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int row0 = OS.gcnew_RowDefinition ();
	int row1 = OS.gcnew_RowDefinition ();
	int rows = OS.Grid_RowDefinitions (handle);
	OS.RowDefinitionCollection_Add (rows, row0);
	OS.RowDefinitionCollection_Add (rows, row1);
	int col0 = OS.gcnew_ColumnDefinition ();
	int col1 = OS.gcnew_ColumnDefinition ();
	int columns = OS.Grid_ColumnDefinitions (handle);
	OS.ColumnDefinitionCollection_Add (columns, col0);
	OS.ColumnDefinitionCollection_Add (columns, col1);
	int gridChildren = OS.Panel_Children (handle);
	textHandle = OS.gcnew_TextBox ();
	if (textHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Grid_SetRowSpan (textHandle, 2);
	OS.UIElementCollection_Add (gridChildren, textHandle);
	upHandle = OS.gcnew_RepeatButton ();
	if (upHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int upArrow = createArrow (SWT.UP);
	OS.ContentControl_Content (upHandle, upArrow);
	OS.Grid_SetColumn (upHandle, 1);
	OS.UIElementCollection_Add (gridChildren, upHandle);
	downHandle = OS.gcnew_RepeatButton ();
	if (downHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int downArrow = createArrow (SWT.DOWN);
	OS.ContentControl_Content (downHandle, downArrow);
	OS.Grid_SetColumn (downHandle, 1);
	OS.Grid_SetRow (downHandle, 1);
	OS.UIElementCollection_Add (gridChildren, downHandle);
	int colWidth0 = OS.gcnew_GridLength (10, OS.GridUnitType_Star);
	OS.ColumnDefinition_Width(col0, colWidth0);
	int colWidth1 = OS.gcnew_GridLength (1, OS.GridUnitType_Auto);
	OS.ColumnDefinition_Width (col1, colWidth1);
	OS.GCHandle_Free (colWidth0);
	OS.GCHandle_Free (colWidth1);
	OS.GCHandle_Free (upArrow);
	OS.GCHandle_Free (downArrow);
	OS.GCHandle_Free (row0);
	OS.GCHandle_Free (row1);
	OS.GCHandle_Free (rows);
	OS.GCHandle_Free (col0);
	OS.GCHandle_Free (col1);
	OS.GCHandle_Free (columns);
	OS.GCHandle_Free (gridChildren);
}

void createWidget () {
	super.createWidget();
	increment = 1;
	pageIncrement = 10;
	digits = 0;
	max = 100;
	value = 0;
	updateText ();
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
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
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
	//TODO
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
	if ((style & SWT.READ_ONLY) != 0) return;
	//TODO
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
	return digits;
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
	return increment;
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
	return max;
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
	return min;
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
	return pageIncrement;
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
	return value;
}

void HandleDownClick (int sender, int e) {
	if (!checkEvent (e)) return;
	value -= increment;
	value = Math.max (value, min);
	updateText ();
	postEvent (SWT.Selection);
}

void HandleUpClick (int sender, int e) {
	if (!checkEvent (e)) return;
	value += increment;
	value = Math.min (value, max);
	updateText ();
	postEvent (SWT.Selection);
}

void hookEvents () {
	super.hookEvents();	
	//TEXT
//	int handler = OS.gcnew_TextCompositionEventHandler (jniRef, "HandlePreviewTextInput");
//	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
//	OS.UIElement_PreviewTextInput (handle, handler);
//	OS.GCHandle_Free (handler);
//	handler = OS.gcnew_ExecutedRoutedEventHandler(jniRef, "HandlePreviewExecutedRoutedEvent");
//	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
//	OS.CommandManager_AddPreviewExecutedHandler(handle, handler);
//	OS.GCHandle_Free(handler);
//	handler = OS.gcnew_TextChangedEventHandler (jniRef, "HandleTextChanged");
//	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
//	OS.TextBoxBase_TextChanged (handle, handler);
//	OS.GCHandle_Free(handler);
	//BUTTON
	int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleDownClick");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.ButtonBase_Click (downHandle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleUpClick");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.ButtonBase_Click (upHandle, handler);
	OS.GCHandle_Free (handler);
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
	//TODO
}

void releaseHandle () {
	super.releaseHandle();
	OS.GCHandle_Free (textHandle);
	OS.GCHandle_Free (upHandle);
	OS.GCHandle_Free (downHandle);
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
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 */
void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
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
	digits = value;
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
	increment = value;
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
	max = value;
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
	min = value;
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
	pageIncrement = value;
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
	this.value = value;
	updateText();
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
	if (minimum < 0) return;
	if (maximum <= minimum) return;
	if (digits < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	value = Math.min (Math.max (minimum, selection), maximum);
	setIncrement (increment);
	this.pageIncrement = pageIncrement;
	this.digits = digits;
	this.min = minimum;
	this.max = maximum;
	this.increment = increment;
	this.pageIncrement = pageIncrement;
	updateText();
}

void updateText () {
	String valStr = digits == 0 ? value+"" : new Double (value/Math.pow(10, digits)).toString();
	int strPtr = createDotNetString (valStr, false);
	OS.TextBox_Text (textHandle, strPtr);
	OS.GCHandle_Free (strPtr);
}

String verifyText (String string, int start, int end, Event keyEvent) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
	}
//	int index = 0;
//	if (digits > 0) {
//		String decimalSeparator = getDecimalSeparator ();
//		index = string.indexOf (decimalSeparator);
//		if (index != -1) {
//			string = string.substring (0, index) + string.substring (index + 1);
//		}
//		index = 0;
//	}
//	while (index < string.length ()) {
//		if (!Character.isDigit (string.charAt (index))) break;
//		index++;
//	}
//	event.doit = index == string.length ();
//	if (!OS.IsUnicode && OS.IsDBLocale) {
//		event.start = mbcsToWcsPos (start);
//		event.end = mbcsToWcsPos (end);
//	}
	//TODO
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

}

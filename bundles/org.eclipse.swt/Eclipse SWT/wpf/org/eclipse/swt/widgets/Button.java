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


import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released. 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE 
 * may be specified.
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p><p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified
 * when the ARROW style is specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Button extends Control {
	String text = "";
	Image image;
	int textHandle, imageHandle;
	boolean ignoreSelection, grayed;

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
 * @see SWT#ARROW
 * @see SWT#CHECK
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#TOGGLE
 * @see SWT#FLAT
 * @see SWT#UP
 * @see SWT#DOWN
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected by the user.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group 
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		style |= SWT.NO_FOCUS;
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size = computeSize (handle, wHint, hHint, changed);
	if ((style & SWT.ARROW) == 0) {
		int border = getBorderWidth ();
		size.x += border * 2;
		size.y += border * 2;
	}
	return size;
}

void createArrow () { 
	int geometry = OS.gcnew_StreamGeometry ();
	int context = OS.StreamGeometry_Open (geometry);
	int start = 0, point = 0, end = 0;
	int mask = SWT.RIGHT | SWT.LEFT | SWT.DOWN | SWT.UP;
	switch (style & mask) {
		case SWT.RIGHT:
			start = OS.gcnew_Point (1, 0);
			point = OS.gcnew_Point (4, 3);
			end = OS.gcnew_Point (1, 6);
			break;
		case SWT.LEFT:
			start = OS.gcnew_Point (4, 1);
			point = OS.gcnew_Point (1, 3);
			end = OS.gcnew_Point (4, 6);
			break;
		case SWT.DOWN:
			start = OS.gcnew_Point (0, 1);
			point = OS.gcnew_Point (3, 4);
			end = OS.gcnew_Point (6, 1);
			break;
		case SWT.UP:
			start = OS.gcnew_Point (0, 4);
			point = OS.gcnew_Point (3, 1);
			end = OS.gcnew_Point (6, 4);
			break;
	}
	OS.StreamGeometryContext_BeginFigure (context, start, true, true);
	OS.StreamGeometryContext_LineTo (context, point, true, true);
	OS.StreamGeometryContext_LineTo (context, end, true, true);
	OS.StreamGeometryContext_Close (context);
	int path = OS.gcnew_Path ();
	OS.Path_Data (path, geometry);
	int padding = OS.gcnew_Thickness (3, 3, 3, 3);
	OS.FrameworkElement_Margin (path, padding);
	int brush = OS.Brushes_Black ();
	OS.Path_Fill (path, brush);
	OS.FrameworkElement_Width (path, 6);
	OS.FrameworkElement_Height (path, 6);
	OS.FrameworkElement_HorizontalAlignment (path, OS.HorizontalAlignment_Center);
	OS.FrameworkElement_VerticalAlignment (path, OS.VerticalAlignment_Center);
	OS.ContentControl_Content (handle, path);
	OS.GCHandle_Free (padding);
	OS.GCHandle_Free (start);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (end);
	OS.GCHandle_Free (brush);
	OS.GCHandle_Free (path);
	OS.GCHandle_Free (context);
	OS.GCHandle_Free (geometry);
}

void createHandle () {
	if ((style & SWT.PUSH) == 0) state |= THEME_BACKGROUND;
	int bits =  SWT.TOGGLE | SWT.ARROW | SWT.PUSH | SWT.RADIO | SWT.CHECK;
	switch (style & bits) {
		case SWT.TOGGLE:
			handle = OS.gcnew_ToggleButton ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.ARROW:
			handle = OS.gcnew_Button ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			createArrow ();
			break;
		case SWT.RADIO:
			handle = OS.gcnew_RadioButton ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.CHECK:
			handle = OS.gcnew_CheckBox ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.PUSH:
		default:
			handle = OS.gcnew_Button ();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
	}
	if ((style & SWT.ARROW) != 0) return;
	int value = OS.HorizontalAlignment_Left;
	if ((style & SWT.CENTER) != 0) value = OS.HorizontalAlignment_Center;
	if ((style & SWT.RIGHT) != 0) value = OS.HorizontalAlignment_Right;
	OS.Control_HorizontalContentAlignment (handle, value);
	imageHandle = OS.gcnew_Image ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Image_Stretch (imageHandle, OS.Stretch_None);
	OS.UIElement_Visibility (imageHandle, OS.Visibility_Collapsed);
	textHandle = OS.gcnew_AccessText ();
	if (textHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_VerticalAlignment (textHandle, OS.VerticalAlignment_Center);
	int panel = OS.gcnew_StackPanel ();
	if (panel == 0) error (SWT.ERROR_NO_HANDLES);
	OS.StackPanel_Orientation (panel, OS.Orientation_Horizontal);
	int thickness = OS.gcnew_Thickness (1, 1, 1, 1);
	if (thickness == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (panel, thickness);
	OS.GCHandle_Free(thickness);
	int children = OS.Panel_Children (panel);
	OS.UIElementCollection_Add (children, imageHandle);
	OS.UIElementCollection_Add (children, textHandle);
	OS.ContentControl_Content (handle, panel);
	OS.GCHandle_Free (children);
	OS.GCHandle_Free (panel);
}

int defaultBackground () {
	if ((style & SWT.PUSH) == 0) return OS.SystemColors_ControlColor;
	return 0;
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the alignment will indicate the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the widget does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public boolean getGrayed () {
	checkWidget ();
	if ((style & SWT.CHECK) == 0) return false;
	return grayed;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget ();
	return image;
}

String getNameText () {
	return getText ();
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in. If the receiver is of any other type,
 * this method returns false.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	if ((style & SWT.CHECK) != 0 && grayed) {
		int property = OS.ToggleButton_IsCheckedProperty();
		int value = OS.DependencyObject_GetValue(handle, property);
		OS.GCHandle_Free(property);
		if (value == 0) return true;
		OS.GCHandle_Free(value);
	}
	return OS.ToggleButton_IsChecked (handle);
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * an <code>ARROW</code> button.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) return "";
	return text;
}

void HandleClick (int sender, int e) {
	if (!checkEvent (e)) return;
	if ((style & SWT.CHECK) != 0) {
		if (grayed) {
			if (OS.ToggleButton_IsChecked (handle)) {
				ignoreSelection = true;
				OS.ToggleButton_IsCheckedNullSetter (handle);
				ignoreSelection = false;
			}
		}
	}
	if (!ignoreSelection) postEvent (SWT.Selection);	
}

void hookEvents () {
	super.hookEvents ();
	if ((style & (SWT.TOGGLE | SWT.RADIO | SWT.CHECK)) != 0) {
		int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleClick");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ToggleButton_Checked (handle, handler);
		OS.GCHandle_Free (handler);		
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleClick");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ToggleButton_Unchecked (handle, handler);
		OS.GCHandle_Free (handler);
	} else {
		int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleClick");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ButtonBase_Click (handle, handler);
		OS.GCHandle_Free (handler);
	}
}

boolean mnemonicHit (char ch) {
	//TODO
	return false;
}

boolean mnemonicMatch (char key) {
	return super.mnemonicMatch (textHandle, key);
}

void releaseHandle() {
	super.releaseHandle ();
	if (textHandle != 0) OS.GCHandle_Free (textHandle);
	textHandle = 0;
	if (imageHandle !=0 )OS.GCHandle_Free (imageHandle);
	imageHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	text = null;
	image = null;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Controls how text, images and arrows will be displayed
 * in the receiver. The argument should be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in 
 * which case, the argument indicates the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>, 
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return; 
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		createArrow ();
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int value = OS.HorizontalAlignment_Left;
	if ((style & SWT.CENTER) != 0) value = OS.HorizontalAlignment_Center;
	if ((style & SWT.RIGHT) != 0) value = OS.HorizontalAlignment_Right;
	OS.Control_HorizontalContentAlignment (handle, value);
}

/**
 * Sets the grayed state of the receiver.  This state change 
 * only applies if the control was created with the SWT.CHECK
 * style.
 *
 * @param grayed the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((style & SWT.CHECK) == 0) return;
	boolean checked = getSelection();
	this.grayed = grayed;
	ignoreSelection = true;
	if (checked){
	if (grayed) {
				OS.ToggleButton_IsCheckedNullSetter(handle);
			} else {
				OS.ToggleButton_IsChecked(handle, true);
			}
		}
	setSelection (checked);
	ignoreSelection = false;
}

void setDefault (boolean value) {
	if ((style & SWT.PUSH) == 0) return;
	OS.Button_IsDefault (handle, value);
}

/**
 * Sets the receiver's image to the argument, which may be
 * <code>null</code> indicating that no image should be displayed.
 * <p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p>
 * @param image the image to display on the receiver (may be <code>null</code>)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	this.image = image;
	OS.Image_Source (imageHandle, image != null ? image.handle : 0);
	OS.UIElement_Visibility (imageHandle, image != null ? OS.Visibility_Visible : OS.Visibility_Collapsed);
	OS.UIElement_Visibility (textHandle, image != null && text.length () == 0 ? OS.Visibility_Collapsed : OS.Visibility_Visible);
	int spacing = image != null && text.length ()!= 0 ? 3 : 0;
	int margin = OS.gcnew_Thickness (0, 0, spacing, 0);
	if (margin == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (imageHandle, margin);
	OS.GCHandle_Free (margin);
}

/**
 * Sets the selection state of the receiver, if it is of type <code>CHECK</code>, 
 * <code>RADIO</code>, or <code>TOGGLE</code>.
 *
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	ignoreSelection = true;
	if ((style & SWT.CHECK) != 0 && grayed) {
		OS.ToggleButton_IsCheckedNullSetter (handle);
	} else {
		OS.ToggleButton_IsChecked (handle, selected);
	}
	ignoreSelection = false;
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasized in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p><p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p>
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	if (string.equals (text)) return;
	text = string;
	int strPtr = createDotNetString (text, true);
	if (strPtr == 0) error (SWT.ERROR_NO_HANDLES);
	OS.AccessText_Text (textHandle, strPtr);
	OS.GCHandle_Free (strPtr);
	OS.UIElement_Visibility (textHandle, string.length () == 0 && image != null ? OS.Visibility_Collapsed : OS.Visibility_Visible);
	int spacing = image != null && text.length () != 0 ? 3 : 0;
	int margin = OS.gcnew_Thickness (0, 0, spacing, 0);
	if (margin == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (imageHandle, margin);
	OS.GCHandle_Free (margin);
}

int traversalCode (int key, int event) {
	int code = super.traversalCode (key, event);
	if ((style & SWT.ARROW) != 0) code &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
	if ((style & SWT.RADIO) != 0) code |= SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
	return code;
}

}

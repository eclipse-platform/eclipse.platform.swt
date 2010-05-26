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
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	int imageHandle, textHandle, arrowHandle;
	ToolBar parent;
	Control control;
	String toolTipText;
	Image disabledImage, hotImage;
	boolean ignoreSelection;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group 
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user,
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

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

int createArrow () { 
	int geometry = OS.gcnew_StreamGeometry ();
	int context = OS.StreamGeometry_Open (geometry);
	int start = OS.gcnew_Point (0, 1);
	int point = OS.gcnew_Point (3, 4);
	int end = OS.gcnew_Point (6, 1);
	OS.StreamGeometryContext_BeginFigure (context, start, true, true);
	OS.StreamGeometryContext_LineTo (context, point, true, true);
	OS.StreamGeometryContext_LineTo (context, end, true, true);
	OS.StreamGeometryContext_Close (context);
	int path = OS.gcnew_Path ();
	OS.Path_Data (path, geometry);
	int padding = OS.gcnew_Thickness (3, 0, 0, 0);
	OS.FrameworkElement_Margin (path, padding);
	int brush = OS.Brushes_Black ();
	OS.Path_Fill (path, brush);
	OS.FrameworkElement_Width (path, 6);
	OS.FrameworkElement_Height (path, 6);
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
	if ((style & SWT.SEPARATOR) != 0) {
		handle = OS.gcnew_Separator ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}	
	int bits = SWT.RADIO | SWT.CHECK | SWT.PUSH | SWT.DROP_DOWN;
	switch (style & bits) {
		case SWT.RADIO:
			handle = OS.gcnew_RadioButton();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.CHECK:
			handle = OS.gcnew_CheckBox();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.DROP_DOWN:
			handle = OS.gcnew_Button();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
		case SWT.PUSH:
		default:
			handle = OS.gcnew_Button();
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			break;
	}
	OS.ToolBar_SetOverflowMode (handle, OS.OverflowMode_Never);
	imageHandle = OS.gcnew_Image ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Image_Stretch (imageHandle, OS.Stretch_None);
	OS.UIElement_Visibility (imageHandle, OS.Visibility_Collapsed);
	textHandle = OS.gcnew_AccessText ();
	if (textHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_VerticalAlignment (textHandle, OS.VerticalAlignment_Center);
	OS.FrameworkElement_HorizontalAlignment (textHandle, OS.HorizontalAlignment_Center);
	int panel = OS.gcnew_StackPanel ();
	if (panel == 0) error (SWT.ERROR_NO_HANDLES);
	int orientation = (parent.style & SWT.RIGHT) != 0 ? OS.Orientation_Horizontal : OS.Orientation_Vertical;
	OS.StackPanel_Orientation (panel, orientation);
	int thickness = OS.gcnew_Thickness (1, 1, 1, 1);
	if (thickness == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (panel, thickness);
	OS.GCHandle_Free (thickness);
	int children = OS.Panel_Children (panel);
	OS.UIElementCollection_Add (children, imageHandle);
	OS.UIElementCollection_Add (children, textHandle);
	if ((style & SWT.DROP_DOWN) != 0) {
		arrowHandle = createArrow ();
		if ((parent.style & SWT.RIGHT) != 0) {
			OS.UIElementCollection_Add (children, arrowHandle);
		} else {
			int newPanel = OS.gcnew_StackPanel ();
			OS.StackPanel_Orientation (newPanel, OS.Orientation_Horizontal);
			int horizontalChildren = OS.Panel_Children (newPanel);
			OS.UIElementCollection_Add (horizontalChildren, panel);
			OS.UIElementCollection_Add (horizontalChildren, arrowHandle);
			OS.GCHandle_Free (horizontalChildren);
			OS.GCHandle_Free (panel);
			panel = newPanel;
		}
	}
	OS.ContentControl_Content (handle, panel);
	OS.GCHandle_Free (children);
	OS.GCHandle_Free (panel);
	int margin = OS.gcnew_Thickness (0, 0, 0, 0);
	OS.Control_Padding (handle, margin);
	OS.GCHandle_Free (margin);
}

void deregister () {
	display.removeWidget (handle);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
	int parentHandle = parent.handle;
	int topHandle = control == null  ? topHandle () : control.topHandle ();
	int point = OS.gcnew_Point (0, 0);
	if (point == 0) error (SWT.ERROR_NO_HANDLES);
	int location = OS.UIElement_TranslatePoint (topHandle, point, parentHandle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	int width = (int) OS.FrameworkElement_ActualWidth (topHandle);
	int height = (int) OS.FrameworkElement_ActualHeight (topHandle);
	return new Rectangle (x, y, width, height);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget ();
	return control;
}

/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget ();
	return disabledImage;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget ();
	return OS.UIElement_IsEnabled (handle);
}

/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget ();
	return hotImage;
}

/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
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
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return OS.ToggleButton_IsChecked(handle);
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

Control getWidgetControl () {
	return parent;
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget ();
	return (int) OS.FrameworkElement_ActualWidth (topHandle ());
}

void HandleChecked (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	postEvent (SWT.Selection);
}

void HandleClick (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	Event event = new Event ();
	if ((style & SWT.DROP_DOWN) != 0) {
		int mousePos = OS.Mouse_GetPosition (handle);
		int zero = OS.gcnew_Point (0, OS.FrameworkElement_ActualHeight (topHandle ()));
		int arrowPos = OS.UIElement_TranslatePoint (arrowHandle, zero, handle);
		if (OS.Point_X (mousePos) > OS.Point_X (arrowPos)) {
			event.detail = SWT.ARROW;
			int location = OS.UIElement_TranslatePoint (handle, zero, parent.handle);
			event.x = (int) OS.Point_X (location);
			event.y = (int) OS.Point_Y (location);
			OS.GCHandle_Free (location);
		}
		OS.GCHandle_Free (arrowPos);
		OS.GCHandle_Free (zero);
		OS.GCHandle_Free (mousePos);
	}
	postEvent (SWT.Selection, event);
}

void HandleUnchecked (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	postEvent (SWT.Selection);
}

void HandleMouseEnter (int sender, int e) {
	if (!checkEvent (e)) return;
	updateImages (getEnabled () && parent.getEnabled ());
}

void HandleMouseLeave (int sender, int e) {
	if (!checkEvent (e)) return;
	updateImages (getEnabled () && parent.getEnabled ());
}

void hookEvents() {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((style & (SWT.RADIO | SWT.CHECK)) != 0) {
		int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleChecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ToggleButton_Checked (handle, handler);
		OS.GCHandle_Free (handler);	
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleUnchecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ToggleButton_Unchecked (handle, handler);
		OS.GCHandle_Free (handler);
	} else {
		int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleClick");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		OS.ButtonBase_Click (handle, handler);
		OS.GCHandle_Free (handler);
	}	
	int handler = OS.gcnew_MouseEventHandler (jniRef, "HandleMouseEnter");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.UIElement_MouseEnter (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseEventHandler (jniRef, "HandleMouseLeave");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.UIElement_MouseLeave (handle, handler);
	OS.GCHandle_Free (handler);
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget ();
	return getEnabled () && parent.isEnabled ();
}

void register () { 
	display.addWidget (handle, this);
}

void releaseWidget () {
	super.releaseWidget ();
	control = null;
	toolTipText = null;
	image = disabledImage = hotImage = null;
}

void releaseHandle () {
	super.releaseHandle ();
	if (handle != 0) OS.GCHandle_Free (handle);
	handle = 0;
	if (imageHandle != 0) {
		OS.GCHandle_Free (imageHandle);
		imageHandle = 0;
	}
	if (textHandle != 0) {
		OS.GCHandle_Free (textHandle);
		textHandle = 0;
	}
	if (arrowHandle != 0) {
		OS.GCHandle_Free (arrowHandle);
		arrowHandle = 0;
	}
	parent = null;
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
 * Sets the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget ();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	if (control == null) {
		int property = OS.Control_BackgroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		Control oldControl = this.control;
		if (oldControl != null && !oldControl.isDisposed ()) OS.Panel_SetZIndex (oldControl.handle, 0);
	} else {
		int brush = OS.Brushes_Transparent ();
		OS.Control_Background (handle, brush);
		OS.GCHandle_Free (brush);
		int pt = OS.gcnew_Point (0, 0);
		if (pt == 0) error (SWT.ERROR_NO_HANDLES);
		int loc = OS.UIElement_TranslatePoint (handle, pt, parent.parentingHandle);
		OS.GCHandle_Free (pt);
		OS.Canvas_SetLeft (control.handle, OS.Point_X (loc));
		OS.Canvas_SetTop (control.handle, OS.Point_Y (loc));
		OS.Panel_SetZIndex (control.handle, parent.childCount);
		OS.GCHandle_Free (loc);
	}
	this.control = control;	
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget ();
	OS.UIElement_IsEnabled (handle, enabled);
	updateImages (enabled && parent.getEnabled ());
	if (arrowHandle != 0) {
		OS.UIElement_Opacity (arrowHandle, enabled ? 1 : 0.4);
	}
}

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget ();
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	disabledImage = image;
	updateImages (getEnabled () && parent.getEnabled ());
}

/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget ();
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	hotImage = image;
	updateImages (getEnabled () && parent.getEnabled ());
}

public void setImage (Image image) {
	checkWidget ();
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	super.setImage (image);
	updateImages (getEnabled () && parent.getEnabled ());
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
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
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	ignoreSelection = true;
	OS.ToggleButton_IsChecked (handle, selected);
	ignoreSelection = false;
}

/**
 * Sets the receiver's text. The string may include
 * the mnemonic character.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p>
 * 
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
	if ((style & SWT.SEPARATOR) != 0) return;
	if (string.equals (text)) return;
	super.setText (string);	
	int strPtr = createDotNetString (string, true);
	if (strPtr == 0) error (SWT.ERROR_NO_HANDLES);
	OS.AccessText_Text (textHandle, strPtr);
	OS.GCHandle_Free (strPtr);
	OS.UIElement_Visibility (textHandle, string.length() == 0 && image != null ? OS.Visibility_Collapsed : OS.Visibility_Visible);
	int spacing = image != null && text.length () != 0 ? 3 : 0;
	int margin = (parent.style & SWT.RIGHT) != 0 ? OS.gcnew_Thickness (0, 0, spacing, 0) : OS.gcnew_Thickness (0, 0, 0, spacing);
	OS.FrameworkElement_Margin (imageHandle, margin);
	OS.GCHandle_Free (margin);
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
 * 
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget ();
	toolTipText = string;
	if (string != null && string.length() == 0) string = null;
	int strPtr = createDotNetString (string, false);
	OS.FrameworkElement_ToolTip (handle, strPtr);
	if (strPtr != 0) OS.GCHandle_Free (strPtr);
}

/**
 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget ();
	int controlHandle = control != null ? control.handle : handle;
	OS.FrameworkElement_Width (controlHandle, width);
}

void updateImages (boolean enabled) {
	if ((style & SWT.SEPARATOR) != 0) return;
	Image next = image;
	if (image != null && !enabled && disabledImage != null) next = disabledImage;
	if (image != null && enabled && hotImage != null && OS.UIElement_IsMouseOver (handle)) next = hotImage;
	OS.Image_Source (imageHandle, next != null ? next.handle : 0);	
	OS.UIElement_Visibility (imageHandle, next != null ? OS.Visibility_Visible : OS.Visibility_Collapsed);
	OS.UIElement_Visibility (textHandle, next != null && text.length () == 0 ? OS.Visibility_Collapsed : OS.Visibility_Visible);
	int spacing = next != null && text.length () != 0 ? 3 : 0;
	int margin = (parent.style & SWT.RIGHT) != 0 ? OS.gcnew_Thickness (0, 0, spacing, 0) : OS.gcnew_Thickness (0, 0, 0, spacing);
	if (margin == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (imageHandle, margin);
	OS.GCHandle_Free (margin);
}
}

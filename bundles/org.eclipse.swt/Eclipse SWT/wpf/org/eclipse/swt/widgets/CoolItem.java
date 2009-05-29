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
 * Instances of this class are selectable user interface
 * objects that represent the dynamically positionable
 * areas of a <code>CoolBar</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class CoolItem extends Item {
	CoolBar parent;
	Control control;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CoolBar</code>) and a style value
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
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CoolItem (CoolBar parent, int style) {
	this (parent, style, parent.itemCount);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CoolBar</code>), a style value
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
 * @param index the zero-relative index at which to store the receiver in its parent
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
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CoolItem (CoolBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, index);
	updateLayout (handle);
	setButtonVisibility (OS.Visibility_Collapsed);
}

/**
 * Adds the listener to the collection of listeners that will
 * be notified when the control is selected by the user, by sending it one
 * of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * If <code>widgetSelected</code> is called when the mouse is over
 * the drop-down arrow (or 'chevron') portion of the cool item,
 * the event object detail field contains the value <code>SWT.ARROW</code>,
 * and the x and y fields in the event object represent the point at
 * the bottom left of the chevron, where the menu should be popped up.
 * <code>widgetDefaultSelected</code> is not called.
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
 * 
 * @since 2.0
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createHandle () {
	handle = OS.gcnew_ToolBar ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void createWidget () {
	super.createWidget ();
	int background = OS.Brushes_Transparent ();
	OS.Control_Background (handle, background);
	OS.GCHandle_Free (background);
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a <code>CoolItem</code> is the size that
 * it would best be displayed at. The width hint and height hint arguments
 * allow the caller to ask the instance questions such as "Given a particular
 * width, how high does it need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @return the preferred size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBounds
 * @see #getSize
 * @see Control#getBorderWidth
 * @see Scrollable#computeTrim
 * @see Scrollable#getClientArea
 */
public Point computeSize (int wHint, int hHint) {
	checkWidget ();
	if (isToolBar (control)) {
		return control.computeSize (SWT.DEFAULT, SWT.DEFAULT);	
	}
	int template = OS.Control_Template (handle);
	int partName = createDotNetString ("PART_ToolBarPanel", false);
	int part = OS.FrameworkTemplate_FindName (template, partName, handle);
	int width = wHint, height = hHint;
	width = Math.max (0, width);
	height = Math.max (0, height);
	int margin = OS.FrameworkElement_Margin (part);
	width += OS.Thickness_Left (margin) + OS.Thickness_Right (margin);
	height += OS.Thickness_Top (margin) + OS.Thickness_Bottom (margin);
	OS.GCHandle_Free (partName);
	OS.GCHandle_Free (part);
	OS.GCHandle_Free (margin);
	
	partName = createDotNetString ("ToolBarThumb", false);
	part = OS.FrameworkTemplate_FindName (template, partName, handle);
	width += OS.FrameworkElement_Width (part);	
	OS.GCHandle_Free (partName);
	OS.GCHandle_Free (part);
	OS.GCHandle_Free (template);
	return new Point (width, height);
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
	int topHandle = topHandle ();
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
 * Returns the control that is associated with the receiver.
 *
 * @return the control that is contained by the receiver
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
 * Returns the minimum size that the cool item can
 * be resized to using the cool item's gripper.
 * 
 * @return a point containing the minimum width and height of the cool item, in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public Point getMinimumSize () {
	checkWidget ();
	int width = (int) OS.FrameworkElement_MinWidth (handle);
	int height = (int) OS.FrameworkElement_MinHeight (handle);
	return new Point (width, height);
}

/**
 * Returns the receiver's parent, which must be a <code>CoolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CoolBar getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns a point describing the receiver's ideal size.
 * The x coordinate of the result is the ideal width of the receiver.
 * The y coordinate of the result is the ideal height of the receiver.
 *
 * @return the receiver's ideal size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getPreferredSize () {
	checkWidget ();
	return getSize ();
}

/**
 * Returns a point describing the receiver's size. The
 * x coordinate of the result is the width of the receiver.
 * The y coordinate of the result is the height of the
 * receiver.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSize() {
	checkWidget ();
	int width = (int) OS.FrameworkElement_Width (handle);
	int height = (int) OS.FrameworkElement_Height (handle);
	return new Point (width, height);
}

void HandleSizeChanged (int sender, int e) {
	if (isToolBar (control)) {
		ToolBar toolbar = (ToolBar) control;
		boolean overflow = OS.ToolBar_HasOverflowItems (toolbar.handle);
		toolbar.setButtonVisibility (overflow ? OS.Visibility_Visible : OS.Visibility_Collapsed);
	} else {
		boolean overflow = OS.ToolBar_HasOverflowItems (handle);
		setButtonVisibility (overflow ? OS.Visibility_Visible : OS.Visibility_Collapsed);
		if (control != null) {
			int template = OS.Control_Template (handle);
			int partName = createDotNetString ("PART_ToolBarPanel", false);
			int part = OS.FrameworkTemplate_FindName (template, partName, handle);
			int width = (int) OS.FrameworkElement_ActualWidth (part);
			int height = (int) OS.FrameworkElement_ActualHeight (part);
			control.setSize (width, height);
			OS.GCHandle_Free (part);
			OS.GCHandle_Free (partName);
			OS.GCHandle_Free (template);
		}
	}
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_SizeChangedEventHandler (jniRef, "HandleSizeChanged");
	OS.FrameworkElement_SizeChanged (handle, handler);
	OS.GCHandle_Free (handler);
}

boolean isToolBar (Control control) {
	if (control == null || control.isDisposed ()) return false;
	int type = OS.ToolBar_typeid ();
	boolean result = OS.Type_IsInstanceOfType (type, control.handle);
	OS.GCHandle_Free (type);
	return result;
}

void releaseHandle () {
	super.releaseHandle ();
	if (handle != 0) OS.GCHandle_Free (handle);
	handle = 0;
	parent = null;
}

/**
 * Removes the listener from the collection of listeners that
 * will be notified when the control is selected by the user.
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
 * 
 * @since 2.0
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void setButtonVisibility (byte visibility) {
	int template = OS.Control_Template (handle);
	int overFlowName = createDotNetString ("OverflowGrid", false);
	int overFlowGrid = OS.FrameworkTemplate_FindName (template, overFlowName, handle);
	if (overFlowGrid != 0) {
		OS.UIElement_Visibility (overFlowGrid, visibility);
		OS.GCHandle_Free (overFlowGrid);
	}
	OS.GCHandle_Free (overFlowName);
	int borderName = createDotNetString ("MainPanelBorder", false);
	int border = OS.FrameworkTemplate_FindName (template, borderName, handle);
	if (border != 0) {
		int right = visibility == OS.Visibility_Collapsed ? 0 : 11;
		int margin = OS.gcnew_Thickness (0, 0, right, 0);
		OS.FrameworkElement_Margin (border, margin);
		OS.GCHandle_Free (border);
		OS.GCHandle_Free (margin);
	}
	OS.GCHandle_Free (borderName);
	OS.GCHandle_Free (template);
}

/**
 * Sets the control that is associated with the receiver
 * to the argument.
 *
 * @param control the new control that will be contained by the receiver
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
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if (this.control != null && this.control.isDisposed ()) {
		this.control = null;
	}
	int topHandle = topHandle ();
	int toolbars = OS.ToolBarTray_ToolBars (parent.handle);
	int index = OS.IList_IndexOf (toolbars, topHandle);
	int band = OS.ToolBar_Band (topHandle);
	int bandIndex = OS.ToolBar_BandIndex (topHandle);
	Control oldControl = this.control, newControl = control;	
	this.control = control;
	if (oldControl != null) {
		if (isToolBar (oldControl)) {
			ToolBar toolbar = (ToolBar) oldControl;
			toolbar.setThumbVisibility (OS.Visibility_Collapsed);
			toolbar.setButtonVisibility (OS.Visibility_Collapsed);
			OS.IList_Remove (toolbars, toolbar.handle);
			int children = OS.ToolBarTray_ToolBars (toolbar.trayHandle);
			OS.IList_Add (children, toolbar.handle);
			OS.GCHandle_Free (children);
			OS.IList_Insert (toolbars, index, handle);
			OS.ToolBar_Band (handle, band);
			OS.ToolBar_BandIndex (handle, bandIndex);
		} else {
			int controlHandle = oldControl.topHandle ();
			int items = OS.ItemsControl_Items (handle);
			OS.ItemCollection_Remove (items, controlHandle);
			OS.GCHandle_Free (items);
			int children = OS.Panel_Children (parent.parentingHandle);
			OS.UIElementCollection_Add (children, controlHandle);
			OS.GCHandle_Free (children);
		}
	}
	if (newControl != null) {
		if (isToolBar (newControl)) {
			ToolBar toolbar = (ToolBar) newControl;
			int controlHandle = toolbar.handle;
			int children = OS.ToolBarTray_ToolBars (toolbar.trayHandle);
			OS.IList_Remove (children, controlHandle);
			OS.GCHandle_Free (children);
			OS.IList_Remove (toolbars, handle);
			OS.IList_Insert (toolbars, index, controlHandle);
			
			boolean locked = OS.ToolBarTray_IsLocked (parent.handle);
			boolean overflow = OS.ToolBar_HasOverflowItems (controlHandle);
			toolbar.setThumbVisibility (locked ? OS.Visibility_Collapsed : OS.Visibility_Visible);
			toolbar.setButtonVisibility (overflow ? OS.Visibility_Visible : OS.Visibility_Collapsed);
			OS.ToolBar_Band (controlHandle, band);
			OS.ToolBar_BandIndex (controlHandle, bandIndex);
			double width = OS.FrameworkElement_Width (handle);
			if (width > 0) OS.FrameworkElement_Width (controlHandle, width); 
			double height =  OS.FrameworkElement_Height (handle);
			if (height > 0) OS.FrameworkElement_Height (controlHandle, height);
			double minWidth = OS.FrameworkElement_MinWidth (handle);
			if (minWidth > 0) OS.FrameworkElement_MinWidth (controlHandle, minWidth);
			double minHeight = OS.FrameworkElement_MinHeight (handle);
			if (minHeight > 0) OS.FrameworkElement_MinHeight (controlHandle, minHeight);
			updateLayout (controlHandle);
		} else {		
			int controlHandle = newControl.topHandle ();
			int children = OS.Panel_Children (parent.parentingHandle ());
			OS.UIElementCollection_Remove (children, controlHandle);
			OS.GCHandle_Free (children);
			int items = OS.ItemsControl_Items (handle);
			OS.ItemCollection_Add (items, controlHandle);
			OS.GCHandle_Free (items);
		}
	}
	OS.GCHandle_Free (toolbars);
}

/**
 * Sets the minimum size that the cool item can be resized to
 * using the cool item's gripper, to the point specified by the arguments.
 * 
 * @param width the minimum width of the cool item, in pixels
 * @param height the minimum height of the cool item, in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setMinimumSize (int width, int height) {
	checkWidget ();
	width = Math.max (0, width);
	height = Math.max (0, height);
	OS.FrameworkElement_MinWidth (handle, width);
	OS.FrameworkElement_MinHeight (handle, height);
	if (isToolBar (control)) {
		OS.FrameworkElement_MinWidth (control.handle, width);
		OS.FrameworkElement_MinHeight (control.handle, height);
	}
}

/**
 * Sets the minimum size that the cool item can be resized to
 * using the cool item's gripper, to the point specified by the argument.
 * 
 * @param size a point representing the minimum width and height of the cool item, in pixels
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setMinimumSize (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setMinimumSize (size.x, size.y);
}

/**
 * Sets the receiver's ideal size to the point specified by the arguments.
 *
 * @param width the new ideal width for the receiver
 * @param height the new ideal height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPreferredSize (int width, int height) {
	checkWidget ();
	setSize (width, height);
}

/**
 * Sets the receiver's ideal size to the point specified by the argument.
 *
 * @param size the new ideal size for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPreferredSize (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setPreferredSize (size.x, size.y);
}

/**
 * Sets the receiver's size to the point specified by the arguments.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (int width, int height) {
	checkWidget ();
	width = Math.max (0, width);
	height = Math.max (0, height);
	OS.FrameworkElement_Width (handle, width);
	OS.FrameworkElement_Height (handle, height);
	if (isToolBar (control)) {
		OS.FrameworkElement_Width (control.handle, width);
		OS.FrameworkElement_Height (control.handle, height);
	}
}

/**
 * Sets the receiver's size to the point specified by the argument.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause them to be
 * set to zero instead.
 * </p>
 *
 * @param size the new size for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (Point size) {
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}

int topHandle () {
	if (isToolBar (control)) return control.handle;
	return handle;
}

}

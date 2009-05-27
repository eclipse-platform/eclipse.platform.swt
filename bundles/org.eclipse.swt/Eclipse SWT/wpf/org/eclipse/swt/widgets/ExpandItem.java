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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a expandable item in a expand bar.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see ExpandBar
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ExpandItem extends Item {
	ExpandBar parent;
	Control control;
	int imageHandle, textHandle, contentHandle;
	
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandItem (ExpandBar parent, int style) {
	this (parent, style, checkNull (parent).getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent, a
 * style value describing its behavior and appearance, and the index
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandItem (ExpandBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, style, index);
}

static ExpandBar checkNull (ExpandBar control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;	
}

void createHandle () {
	handle = OS.gcnew_Expander ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int spacing = parent.spacing;
	int thickness = OS.gcnew_Thickness (spacing, spacing, spacing, spacing);
	OS.FrameworkElement_Margin (handle, thickness);	
	OS.GCHandle_Free (thickness);
	imageHandle = OS.gcnew_Image ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Image_Stretch (imageHandle, OS.Stretch_None);
	OS.UIElement_Visibility (imageHandle, OS.Visibility_Collapsed);
	textHandle = OS.gcnew_TextBlock ();
	if (textHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int panel = OS.gcnew_StackPanel ();
	if (panel == 0) error (SWT.ERROR_NO_HANDLES);
	OS.StackPanel_Orientation (panel, OS.Orientation_Horizontal);
	thickness = OS.gcnew_Thickness (1, 1, 1, 1);
	if (thickness == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (panel, thickness);
	OS.GCHandle_Free (thickness);
	int children = OS.Panel_Children (panel);
	OS.UIElementCollection_Add (children, imageHandle);
	OS.UIElementCollection_Add (children, textHandle);
	OS.GCHandle_Free (children);
	OS.HeaderedContentControl_Header (handle, panel);
	OS.GCHandle_Free (panel);
	contentHandle = OS.gcnew_Canvas ();
	if (contentHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.ContentControl_Content (handle, contentHandle);
}

void deregister () {
	display.removeWidget (handle);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the control that is shown when the item is expanded.
 * If no control has been set, return <code>null</code>.
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
 * Returns <code>true</code> if the receiver is expanded,
 * and false otherwise.
 *
 * @return the expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getExpanded () {
	checkWidget ();
	return OS.Expander_IsExpanded (handle);
}

/**
 * Returns the height of the receiver's header 
 *
 * @return the height of the header 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHeaderHeight () {
	checkWidget ();
	int panel = OS.HeaderedContentControl_Header (handle);
	return (int) OS.FrameworkElement_ActualHeight (panel);
}

/**
 * Gets the height of the receiver.
 *
 * @return the height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHeight () {
	checkWidget ();
	return (int) OS.FrameworkElement_Height (contentHandle);
}

/**
 * Returns the receiver's parent, which must be a <code>ExpandBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ExpandBar getParent () {
	checkWidget ();
	return parent;
}

Control getWidgetControl () {
	return parent;
}

void HandleExpanded (int sender, int e) {
	if (!checkEvent (e)) return;
	Event event = new Event ();
	event.item = this;
	parent.notifyListeners (SWT.Expand, event);
}

void HandleCollapsed (int sender, int e) {
	if (!checkEvent (e)) return;
	Event event = new Event ();
	event.item = this;
	parent.notifyListeners (SWT.Collapse, event);
}

void HandleSizeChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	resizeControl ();
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleExpanded");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Expander_Expanded (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleCollapsed");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Expander_Collapsed (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_SizeChangedEventHandler (jniRef, "HandleSizeChanged");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_SizeChanged (contentHandle, handler);
	OS.GCHandle_Free (handler);
}

void register () {
	display.addWidget (handle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	if (handle != 0) OS.GCHandle_Free (handle);
	handle = 0;
	parent = null;
	if (textHandle != 0) OS.GCHandle_Free (textHandle);
	textHandle = 0;
	if (imageHandle !=0 )OS.GCHandle_Free (imageHandle);
	imageHandle = 0;
	if (contentHandle != 0) OS.GCHandle_Free (contentHandle);
	contentHandle = 0;
}

void resizeControl () {
	if (control != null) { 
		int width = (int) OS.FrameworkElement_ActualWidth (contentHandle);
		int height = (int) OS.FrameworkElement_Height (contentHandle);
		control.setSize (width, height);
	}
}

/**
 * Sets the control that is shown when the item is expanded.
 *
 * @param control the new control (or null)
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
	Control oldControl = this.control, newControl = control;
	this.control = control;
	int children = OS.Panel_Children (contentHandle);
	int parentHandle = parent.parentingHandle ();
	int parentChildren = OS.Panel_Children (parentHandle);
	if (newControl != null) {
		int topHandle = newControl.topHandle ();
		OS.UIElementCollection_Remove (parentChildren, topHandle);
		OS.UIElementCollection_Add (children, topHandle);
	}
	if (oldControl != null) {
		int topHandle = oldControl.topHandle ();
		OS.UIElementCollection_Remove (children, topHandle);
		OS.UIElementCollection_Add (parentChildren, topHandle);
	}
	OS.GCHandle_Free (children);
	OS.GCHandle_Free (parentChildren);
}

/**
 * Sets the expanded state of the receiver.
 *
 * @param expanded the new expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpanded (boolean expanded) {
	checkWidget ();
	OS.Expander_IsExpanded(handle, expanded);
}

/**
 * Sets the height of the receiver. This is height of the item when it is expanded, 
 * excluding the height of the header.
 *
 * @param height the new height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHeight (int height) {
	checkWidget ();
	if (height < 0) return;
	OS.FrameworkElement_Height (contentHandle, height);
}

public void setImage (Image image) {
	super.setImage (image);
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	this.image = image;
	OS.Image_Source (imageHandle, image != null ? image.handle : 0);
	OS.UIElement_Visibility (imageHandle, image != null ? OS.Visibility_Visible : OS.Visibility_Collapsed);
	OS.UIElement_Visibility (textHandle, image != null && text.length () == 0 ? OS.Visibility_Collapsed : OS.Visibility_Visible);
	int spacing = image != null && text.length () != 0 ? 3 : 0;
	int margin = OS.gcnew_Thickness (0, 0, spacing, 0);
	if (margin == 0) error(SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (imageHandle, margin);
	OS.GCHandle_Free (margin);
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (string.equals (text)) return;
	text = string;
	int ptr = createDotNetString (text, false);
	if (ptr == 0) error(SWT.ERROR_NO_HANDLES);
	OS.TextBlock_Text (textHandle, ptr);
	OS.GCHandle_Free (ptr);
	OS.UIElement_Visibility (textHandle, text.length() == 0 && image != null ? OS.Visibility_Collapsed : OS.Visibility_Visible);
	int spacing = image != null && text.length () != 0 ? 3 : 0;
	int margin = OS.gcnew_Thickness (0, 0, spacing, 0);
	if (margin == 0) error(SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Margin (imageHandle, margin);
	OS.GCHandle_Free (margin);
}
}

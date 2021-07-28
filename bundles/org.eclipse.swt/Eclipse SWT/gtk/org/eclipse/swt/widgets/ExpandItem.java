/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a expandable item in a expand bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
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
	long clientHandle, boxHandle, labelHandle, imageHandle;
	int width, height;

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
	super (parent, style);
	this.parent = parent;
	createWidget (parent.getItemCount ());
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
	createWidget (index);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void createHandle (int index) {
	state |= HANDLE;
	handle = GTK.gtk_expander_new (null);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	clientHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (clientHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (GTK.GTK4) {
		GTK4.gtk_expander_set_child(handle, clientHandle);
	} else {
		GTK3.gtk_container_add (handle, clientHandle);
	}
	boxHandle = gtk_box_new (GTK.GTK_ORIENTATION_HORIZONTAL, false, 4);
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	labelHandle = GTK.gtk_label_new (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);

	if (GTK.GTK4) {
		imageHandle = GTK4.gtk_picture_new();
		if (imageHandle == 0) error(SWT.ERROR_NO_HANDLES);
		GTK4.gtk_picture_set_can_shrink(imageHandle, false);

		GTK4.gtk_box_append(boxHandle, imageHandle);
		GTK4.gtk_box_append(boxHandle, labelHandle);
	} else {
		imageHandle = GTK.gtk_image_new();
		if (imageHandle == 0) error(SWT.ERROR_NO_HANDLES);

		GTK3.gtk_container_add(boxHandle, imageHandle);
		GTK3.gtk_container_add(boxHandle, labelHandle);
	}

	GTK.gtk_expander_set_label_widget (handle, boxHandle);
	GTK.gtk_widget_set_can_focus (handle, true);
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	showWidget (index);
	parent.createItem (this, style, index);
}

@Override
void deregister() {
	super.deregister();
	display.removeWidget (clientHandle);
	display.removeWidget (boxHandle);
	display.removeWidget (labelHandle);
	display.removeWidget (imageHandle);
}

@Override
void release (boolean destroy) {
	//454940 ExpandBar DND fix.
	//Since controls are now nested under the Item,
	//Item is responsible for it's release.
	if (control != null && !control.isDisposed ()) {
		control.release (destroy);
	}
	super.release (destroy);
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
	super.destroyWidget ();
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
public boolean getExpanded() {
	checkWidget();
	return GTK.gtk_expander_get_expanded(handle);
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
	return DPIUtil.autoScaleDown (getHeaderHeightInPixels ());
}

int getHeaderHeightInPixels() {
	checkWidget();

	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation(GTK.gtk_expander_get_label_widget(handle), allocation);

	return allocation.height;
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
public int getHeight() {
	checkWidget();
	return DPIUtil.autoScaleDown(height);
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
	checkWidget();
	return parent;
}

@Override
long gtk_activate (long widget) {
	Event event = new Event ();
	event.item = this;
	int type = GTK.gtk_expander_get_expanded (handle) ? SWT.Collapse : SWT.Expand;
	parent.sendEvent (type, event);
	return 0;
}

@Override
long gtk_button_press_event (long widget, long event) {
	setFocus ();
	return 0;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	GTK.gtk_widget_set_can_focus (handle, false);
	parent.lastFocus = this;
	return 0;
}

@Override
long gtk_size_allocate (long widget, long allocation) {
	parent.layoutItems();
	return 0;
}

@Override
long gtk_enter_notify_event (long widget, long event) {
	parent.gtk_enter_notify_event(widget, event);
	return 0;
}

boolean hasFocus () {
	return GTK.gtk_widget_has_focus (handle);
}

@Override
void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure (handle, OS.activate, display.getClosure (ACTIVATE), false);
	OS.g_signal_connect_closure (handle, OS.activate, display.getClosure (ACTIVATE_INVERSE), true);
	if (GTK.GTK4) {
		long clickController = GTK4.gtk_gesture_click_new();
		GTK4.gtk_widget_add_controller(handle, clickController);
		OS.g_signal_connect(clickController, OS.pressed, display.gesturePressReleaseProc, GESTURE_PRESSED);

		long motionController = GTK4.gtk_event_controller_motion_new();
		GTK4.gtk_widget_add_controller(handle, motionController);
		GTK.gtk_event_controller_set_propagation_phase(motionController, GTK.GTK_PHASE_TARGET);

		long enterAddress = display.enterMotionCallback.getAddress();
		OS.g_signal_connect (motionController, OS.enter, enterAddress, ENTER);

		OS.g_signal_connect(clientHandle, OS.resize, display.resizeProc, 0);
	} else {
		OS.g_signal_connect_closure(clientHandle, OS.size_allocate, display.getClosure(SIZE_ALLOCATE), true);

		OS.g_signal_connect_closure_by_id (handle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.getClosure (ENTER_NOTIFY_EVENT), false);
		OS.g_signal_connect_closure_by_id (handle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.getClosure (BUTTON_PRESS_EVENT), false);
		OS.g_signal_connect_closure_by_id (handle, display.signalIds [FOCUS_OUT_EVENT], 0, display.getClosure (FOCUS_OUT_EVENT), false);
	}
}

@Override
void register () {
	super.register ();
	display.addWidget (clientHandle, this);
	display.addWidget (boxHandle, this);
	display.addWidget (labelHandle, this);
	display.addWidget (imageHandle, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	clientHandle = boxHandle = labelHandle = imageHandle = 0;
	parent = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (parent.lastFocus == this) parent.lastFocus = null;
	control = null;
}

void resizeControl () {
	if (control != null && !control.isDisposed ()) {
		boolean visible = GTK.gtk_expander_get_expanded (handle);
		GtkAllocation allocation = new GtkAllocation ();
		GTK.gtk_widget_get_allocation (clientHandle, allocation);

		//454940 change in hierarchy
		/* SWT's calls to gtk_widget_size_allocate and gtk_widget_set_allocation
		* causes GTK+ to move the clientHandle's SwtFixed down by the size of the label.
		* These calls can come up from 'shell' and ExpandItem has no control over these calls.
		* This is an undesired side-effect. Client handle's x & y positions should never
		* be incremented as this is an internal sub-container.
		* As of GTK3, the hierarchy is changed, this affected child-size allocation and a fix
		* is now neccessary.
		* See also other 454940 notes and similar fix in: 453827 */
		int x = 0;
		int y = 0;
		int width = allocation.width;
		int height = allocation.height;
		/*
		 * Focus line width is done via CSS in GTK4, and does not contribute
		 * to the size of the widget.
		 */
		if (!GTK.GTK4) {
			int [] property = new int [1];
			GTK3.gtk_widget_style_get (handle, OS.focus_line_width, property, 0);
			y += property [0] * 2;
			height -= property [0] * 2;
		}

		/*
		* Feature in GTK. When the ExpandBar is resize too small the control
		* shows up on top of the vertical scrollbar. This happen because the
		* GtkExpander does not set the size of child smaller than the request
		* size of its parent and because the control is not parented in the
		* hierarchy of the GtkScrolledWindow.
		* The fix is calculate the width ourselves when the scrollbar is visible.
		*/
		ScrollBar vBar = parent.verticalBar;
		if (vBar != null) {
			if (GTK.gtk_widget_get_visible (vBar.handle)) {
				GTK.gtk_widget_get_allocation (parent.scrolledHandle, allocation);
				width = allocation.width - parent.vScrollBarWidth () - 2 * parent.spacing;
			}
		}
		// Bug 479242: Bound calculation is correct without needing to use yScroll in GTK3
		/*
		 * Bug 538114: ExpandBar has no content until resized or collapsed/expanded.
		 * When widget is first created inside ExpandItem's control, the size is allocated
		 * to be zero, and the widget is never shown during a layout operation, similar to
		 * Bug 487757. The fix is to show the control before setting any bounds.
		 */
		if (visible) GTK.gtk_widget_show(control.topHandle ());
		control.setBounds (x, y, width, Math.max (0, height), true, true);

		control.setVisible (visible);
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
	if (this.control == control) return;

	this.control = control;
	if (control != null) {
		//454940 ExpandBar DND fix.
		//Reparenting on the GTK side.
		//Proper hierachy on gtk side is required for DND to function properly.
		//As ExpandItem's child can be created before the ExpandItem, our only
		//option is to reparent the child upon the setControl(..) call.
		//This is simmilar to TabFolder.
		Control.gtk_widget_reparent (control, clientHandle);
	}
	parent.layoutItems();
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
public void setExpanded(boolean expanded) {
	checkWidget();
	GTK.gtk_expander_set_expanded(handle, expanded);
	parent.layoutItems();
}

boolean setFocus () {
	if (!GTK.gtk_widget_get_child_visible (handle)) return false;
	GTK.gtk_widget_set_can_focus (handle, true);
	GTK.gtk_widget_grab_focus (handle);
	// widget could be disposed at this point
	if (isDisposed ()) return false;
	boolean result = GTK.gtk_widget_is_focus (handle);
	if (!result) GTK.gtk_widget_set_can_focus (handle, false);
	return result;
}

void setFontDescription (long font) {
	setFontDescription (handle, font);
	if (labelHandle != 0) setFontDescription (labelHandle, font);
}

void setForegroundRGBA (GdkRGBA rgba) {
	parent.setForegroundGdkRGBA (handle, rgba);
	if (labelHandle != 0) parent.setForegroundGdkRGBA (labelHandle, rgba);
	if (imageHandle != 0) parent.setForegroundGdkRGBA (imageHandle, rgba);
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
	setHeightInPixels(DPIUtil.autoScaleUp(height));
}

void setHeightInPixels (int height) {
	checkWidget ();
	if (height < 0) return;
	this.height = height;
	GTK.gtk_widget_set_size_request (clientHandle, -1, height);
	parent.layoutItems();
}

@Override
public void setImage (Image image) {
	super.setImage (image);

	if (image != null) {
		if (image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (GTK.GTK4) {
			long pixbuf = ImageList.createPixbuf(image);
			long texture = GDK.gdk_texture_new_for_pixbuf(pixbuf);
			OS.g_object_unref(pixbuf);
			GTK4.gtk_picture_set_paintable(imageHandle, texture);
		} else {
			GTK3.gtk_image_set_from_surface(imageHandle, image.surface);
		}
		if (text.length () == 0) GTK.gtk_widget_hide (labelHandle);
		GTK.gtk_widget_show(imageHandle);
	} else {
		if (GTK.GTK4) {
			GTK4.gtk_picture_set_paintable(imageHandle, 0);
		} else {
			GTK3.gtk_image_set_from_surface(imageHandle, 0);
		}
		GTK.gtk_widget_show(labelHandle);
		GTK.gtk_widget_hide(imageHandle);
	}
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((parent.style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (parent.style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		GTK.gtk_widget_set_direction (handle, dir);
		GTK3.gtk_container_forall (handle, display.setDirectionProc, dir);
	}
}

@Override
public void setText (String string) {
	super.setText (string);
	byte [] buffer = Converter.wcsToMbcs (string, true);
	GTK.gtk_label_set_text (labelHandle, buffer);
}

void showWidget (int index) {
	if (GTK.GTK4) {
		GTK4.gtk_box_append(parent.handle, handle);
		gtk_box_set_child_packing (parent.handle, handle, false, false, 0, GTK.GTK_PACK_START);
	} else {
		GTK.gtk_widget_show (handle);
		GTK.gtk_widget_show (clientHandle);
		if (labelHandle != 0)
			GTK.gtk_widget_show (labelHandle);
		if (boxHandle != 0)
			GTK.gtk_widget_show (boxHandle);
		GTK3.gtk_container_add (parent.handle, handle);
		gtk_box_set_child_packing (parent.handle, handle, false, false, 0, GTK.GTK_PACK_START);
	}
}

@Override
long windowProc (long handle, long user_data) {
	switch ((int)user_data) {
		case ACTIVATE_INVERSE: {
			parent.layoutItems();
			return 0;
		}
	}
	return super.windowProc (handle, user_data);
}

@Override
long dpiChanged(long object, long arg0) {
	super.dpiChanged(object, arg0);

	if (image != null) {
		image.internal_gtk_refreshImageForZoom();
		setImage(image);
	}

	return 0;
}
}

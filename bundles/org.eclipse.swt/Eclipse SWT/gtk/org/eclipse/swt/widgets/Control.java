/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.accessibility.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b>
 * <dd>FocusIn, FocusOut, Help, KeyDown, KeyUp, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize</dd>
 * </dl>
 * <p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * Note: Only one of LEFT_TO_RIGHT and RIGHT_TO_LEFT may be specified.
 */
public abstract class Control extends Widget implements Drawable {
	int fixedHandle;
	int drawCount;
	Composite parent;
	Cursor cursor;
	Menu menu;
	Font font;
	String toolTipText;
	Object layoutData;
	Accessible accessible;

Control () {
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
 * @see SWT#BORDER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}

int defaultFont () {
	return display.defaultFont;
}

void deregister () {
	super.deregister ();
	if (fixedHandle != 0) display.removeWidget (fixedHandle);
	int imHandle = imHandle ();
	if (imHandle != 0) display.removeWidget (imHandle);
}

void enableWidget (boolean enabled) {
	int topHandle = topHandle ();
	OS.gtk_widget_set_sensitive (topHandle, enabled);
}

int eventHandle () {
	return handle;
}

void fixFocus () {
	Shell shell = getShell ();
	Control control = this;
	while ((control = control.parent) != null) {
		if (control.setFocus () || control == shell) return;
	}
}

int focusHandle () {
	return handle;
}

int fontHandle () {
	return handle;
}

boolean hasFocus () {
	return OS.GTK_WIDGET_HAS_FOCUS (focusHandle ());
}

void hookEvents () {
	int windowProc2 = display.windowProc2;
	int windowProc3 = display.windowProc3;
	
	/* Connect the keyboard signals */
	int focusHandle = focusHandle ();
	int focusMask = OS.GDK_KEY_PRESS_MASK | OS.GDK_KEY_RELEASE_MASK | OS.GDK_FOCUS_CHANGE_MASK;
	OS.gtk_widget_add_events (focusHandle, focusMask);
	OS.g_signal_connect (focusHandle, OS.popup_menu, windowProc2, POPUP_MENU);
	OS.g_signal_connect (focusHandle, OS.show_help, windowProc3, SHOW_HELP);
	OS.g_signal_connect (focusHandle, OS.key_press_event, windowProc3, KEY_PRESS_EVENT);
	OS.g_signal_connect (focusHandle, OS.key_release_event, windowProc3, KEY_RELEASE_EVENT);
	OS.g_signal_connect (focusHandle, OS.focus, windowProc3, FOCUS);
	OS.g_signal_connect (focusHandle, OS.focus_in_event, windowProc3, FOCUS_IN_EVENT);
	OS.g_signal_connect (focusHandle, OS.focus_out_event, windowProc3, FOCUS_OUT_EVENT);

	/* Connect the mouse signals */
	int eventHandle = eventHandle ();
	int eventMask = OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_PRESS_MASK |
		OS.GDK_BUTTON_RELEASE_MASK | OS.GDK_ENTER_NOTIFY_MASK |
		OS.GDK_LEAVE_NOTIFY_MASK;
	OS.gtk_widget_add_events (eventHandle, eventMask);
	OS.g_signal_connect (eventHandle, OS.button_press_event, windowProc3, BUTTON_PRESS_EVENT);
	OS.g_signal_connect (eventHandle, OS.button_release_event, windowProc3, BUTTON_RELEASE_EVENT);
	OS.g_signal_connect (eventHandle, OS.motion_notify_event, windowProc3, MOTION_NOTIFY_EVENT);
	OS.g_signal_connect (eventHandle, OS.enter_notify_event, windowProc3, ENTER_NOTIFY_EVENT);
	OS.g_signal_connect (eventHandle, OS.leave_notify_event, windowProc3, LEAVE_NOTIFY_EVENT);
	/*
	* Feature in GTK.  Events such as mouse move are propagate up
	* the widget hierarchy and are seen by the parent.  This is the
	* correct GTK behavior but not correct for SWT.  The fix is to
	* hook a signal after and stop the propagation using a negative
	* event number to distinguish this case.
	*/
	OS.g_signal_connect_after (eventHandle, OS.button_press_event, windowProc3, -BUTTON_PRESS_EVENT);
	OS.g_signal_connect_after (eventHandle, OS.button_release_event, windowProc3, -BUTTON_RELEASE_EVENT);
	OS.g_signal_connect_after (eventHandle, OS.motion_notify_event, windowProc3, -MOTION_NOTIFY_EVENT);

	/* Connect the event_after signal for both key and mouse */
	OS.g_signal_connect (eventHandle, OS.event_after, windowProc3, EVENT_AFTER);
	if (focusHandle != eventHandle) {
		OS.g_signal_connect (focusHandle, OS.event_after, windowProc3, EVENT_AFTER);
	}
	
	/* Connect the paint signal */
	int paintHandle = paintHandle ();
	int paintMask = OS.GDK_EXPOSURE_MASK;
	OS.gtk_widget_add_events (paintHandle, paintMask);
	OS.g_signal_connect_after (paintHandle, OS.expose_event, windowProc3, EXPOSE_EVENT);

	/* Connect the Input Method signals */
	OS.g_signal_connect_after (handle, OS.realize, windowProc2, REALIZE);
	OS.g_signal_connect (handle, OS.unrealize, windowProc2, UNREALIZE);
	int imHandle = imHandle ();
	if (imHandle != 0) {
		OS.g_signal_connect (imHandle, OS.commit, windowProc3, COMMIT);
		OS.g_signal_connect (imHandle, OS.preedit_changed, windowProc2, PREEDIT_CHANGED);
	}
}

int hoverProc (int widget) {
	Event event = new Event ();
	int [] x = new int [1], y = new int [1], mask = new int [1];
	OS.gdk_window_get_pointer (0, x, y, mask);
	event.x = x [0];
	event.y = y [0];
	int eventHandle = eventHandle ();
	int window = OS.GTK_WIDGET_WINDOW (eventHandle);
	OS.gdk_window_get_origin (window, x, y);
	event.x -= x [0];
	event.y -= y [0];
	setInputState (event, mask [0]);
	postEvent (SWT.MouseHover, event);
	return 0;
}

int topHandle() {
	if (fixedHandle != 0) return fixedHandle;
	return super.topHandle ();
}

int paintHandle () {
	return handle;
}

int paintWindow () {
	int paintHandle = paintHandle ();
	OS.gtk_widget_realize (paintHandle);
	return OS.GTK_WIDGET_WINDOW (paintHandle);
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @return the preferred size of the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

Control computeTabGroup () {
	if (isTabGroup()) return this;
	return parent.computeTabGroup ();
}

Control[] computeTabList() {
	if (isTabGroup()) {
		if (getVisible() && getEnabled()) {
			return new Control[] {this};
		}
	}
	return new Control[0];
}

Control computeTabRoot () {
	Control[] tabList = parent._getTabList();
	if (tabList != null) {
		int index = 0;
		while (index < tabList.length) {
			if (tabList [index] == this) break;
			index++;
		}
		if (index == tabList.length) {
			if (isTabGroup ()) return this;
		}
	}
	return parent.computeTabRoot ();
}

void createWidget (int index) {
	checkOrientation (parent);
	super.createWidget (index);
	setInitialSize ();
	setZOrder (null, false);
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p><p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
 * @return the preferred size of the control.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	return computeNativeSize (handle, wHint, hHint, changed);	
}

Point computeNativeSize (int h, int wHint, int hHint, boolean changed) {
	int width = OS.GTK_WIDGET_WIDTH (h);
	int height = OS.GTK_WIDGET_HEIGHT (h);
	OS.gtk_widget_set_size_request (h, -1, -1);
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (h, requisition);
	OS.gtk_widget_set_size_request (h, width, height);
	width = wHint == SWT.DEFAULT ? requisition.width : wHint;
	height = hHint == SWT.DEFAULT ? requisition.height : hHint;
	return new Point (width, height);	
}

/**
 * Returns the accessible object for the receiver.
 * If this is the first time this object is requested,
 * then the object is created and returned.
 *
 * @return the accessible object
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Accessible#addAccessibleListener
 * @see Accessible#addAccessibleControlListener
 * 
 * @since 2.0
 */
public Accessible getAccessible () {
	checkWidget ();
	if (accessible == null) {
		accessible = Accessible.internal_new_Accessible (this);
	}
	return accessible;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null).
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	int topHandle = topHandle ();
	int x = OS.GTK_WIDGET_X (topHandle);
	int y = OS.GTK_WIDGET_Y (topHandle);
	int width = OS.GTK_WIDGET_WIDTH (topHandle);
	int height = OS.GTK_WIDGET_HEIGHT (topHandle);
	return new Rectangle (x, y, width, height);
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the argument. The <code>x</code> and 
 * <code>y</code> fields of the rectangle are relative to
 * the receiver's parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param rect the new bounds for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (Rectangle rect) {
	checkWidget ();
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and 
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	setBounds (x, y, width, height, true, true);
}

void moveHandle (int x, int y) {
	int topHandle = topHandle ();
	int parentHandle = parent.parentingHandle ();
	OS.gtk_fixed_move (parentHandle, topHandle, x, y);
}

void resizeHandle (int width, int height) {
	int topHandle = topHandle ();
	OS.gtk_widget_set_size_request (topHandle, width, height);
	if (topHandle != handle) {
		OS.gtk_widget_set_size_request (handle, width, height);
	}

	/*
	* Feature in GTK.  Some widgets do not allocate the size
	* of their internal children in gtk_widget_size_allocate().
	* Instead this is done in gtk_widget_size_request().  This
	* means that the client area of the widget is not correct.
	* The fix is to call gtk_widget_size_request() (and throw
	* the results away).
	*
	* Note: The following widgets rely on this feature:
	* 	GtkScrolledWindow
	* 	GtkNotebook
	* 	GtkFrame
	* 	GtkCombo
	*/
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int topHandle = topHandle ();
	int flags = OS.GTK_WIDGET_FLAGS (topHandle);
	OS.GTK_WIDGET_SET_FLAGS (topHandle, OS.GTK_VISIBLE);
	boolean sameOrigin = true, sameExtent = true;
	if (move) {
		int oldX = OS.GTK_WIDGET_X (topHandle);
		int oldY = OS.GTK_WIDGET_Y (topHandle);
		sameOrigin = x == oldX && y == oldY;
		if (!sameOrigin) moveHandle (x, y);
	}
	if (resize) {
		width = Math.max (1, width);
		height = Math.max (1, height);
		int oldWidth = OS.GTK_WIDGET_WIDTH (topHandle);
		int oldHeight = OS.GTK_WIDGET_HEIGHT (topHandle);
		sameExtent = width == oldWidth && height == oldHeight;
		if (!sameExtent) resizeHandle (width, height);
	}
	if (!sameOrigin || !sameExtent) {
		/*
		* Force the container to allocate the size of its children.
		*/
		int parentHandle = parent.parentingHandle ();
		OS.gtk_container_resize_children (parentHandle);
	}
	if ((flags & OS.GTK_VISIBLE) == 0) {
		OS.GTK_WIDGET_UNSET_FLAGS (topHandle, OS.GTK_VISIBLE);	
	}
	if (!sameOrigin) sendEvent (SWT.Move);
	if (!sameExtent) sendEvent (SWT.Resize);
	return !sameOrigin || !sameExtent;
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null).
 *
 * @return the receiver's location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getLocation () {
	checkWidget();
	int topHandle = topHandle ();
	int x = OS.GTK_WIDGET_X (topHandle);
	int y = OS.GTK_WIDGET_Y (topHandle);
	return new Point (x, y);
}

/**
 * Sets the receiver's location to the point specified by
 * the argument which is relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation(int x, int y) {
	checkWidget();
	setBounds (x, y, 0, 0, true, false);
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
public Point getSize () {
	checkWidget();
	int topHandle = topHandle ();
	int width = OS.GTK_WIDGET_WIDTH (topHandle);
	int height = OS.GTK_WIDGET_HEIGHT (topHandle);
	return new Point (width, height);
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
 * @param height the new height for the receiver
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
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
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
	checkWidget();
	setBounds (0, 0, width, height, false, true);
}

/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void moveAbove (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, true);
}

/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void moveBelow (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, false);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize
 */
public void pack () {
	pack (true);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 * <p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize
 */
public void pack (boolean changed) {
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

/**
 * Sets the layout data associated with the receiver to the argument.
 * 
 * @param layoutData the new layout data for the receiver.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayoutData (Object layoutData) {
	checkWidget();
	this.layoutData = layoutData;
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toControl (int x, int y) {
	checkWidget ();
	int eventHandle = eventHandle ();
	OS.gtk_widget_realize (eventHandle);
	int window = OS.GTK_WIDGET_WINDOW (eventHandle);
	int [] origin_x = new int [1], origin_y = new int [1];
	OS.gdk_window_get_origin (window, origin_x, origin_y);
	return new Point (x - origin_x [0], y - origin_y [0]);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toControl (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toControl (point.x, point.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toDisplay (int x, int y) {
	checkWidget();
	int eventHandle = eventHandle ();
	OS.gtk_widget_realize (eventHandle);
	int window = OS.GTK_WIDGET_WINDOW (eventHandle);
	int [] origin_x = new int [1], origin_y = new int [1];
	OS.gdk_window_get_origin (window, origin_x, origin_y);
	return new Point (origin_x [0] + x, origin_y [0] + y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toDisplay (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toDisplay (point.x, point.y);
}
 
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control gains or loses focus, by sending
 * it one of the messages defined in the <code>FocusListener</code>
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
 * @see FocusListener
 * @see #removeFocusListener
 */
public void addFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.FocusIn,typedListener);
	addListener(SWT.FocusOut,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
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
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard, by sending
 * it one of the messages defined in the <code>KeyListener</code>
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
 * @see KeyListener
 * @see #removeKeyListener
 */
public void addKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when mouse buttons are pressed and released, by sending
 * it one of the messages defined in the <code>MouseListener</code>
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
 * @see MouseListener
 * @see #removeMouseListener
 */
public void addMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseDown,typedListener);
	addListener(SWT.MouseUp,typedListener);
	addListener(SWT.MouseDoubleClick,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse moves, by sending it one of the
 * messages defined in the <code>MouseMoveListener</code>
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
 * @see MouseMoveListener
 * @see #removeMouseMoveListener
 */
public void addMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseMove,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse passes or hovers over controls, by sending
 * it one of the messages defined in the <code>MouseTrackListener</code>
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
 * @see MouseTrackListener
 * @see #removeMouseTrackListener
 */
public void addMouseTrackListener (MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver needs to be painted, by sending it
 * one of the messages defined in the <code>PaintListener</code>
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
 * @see PaintListener
 * @see #removePaintListener
 */
public void addPaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Paint,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when traversal events occur, by sending it
 * one of the messages defined in the <code>TraverseListener</code>
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
 * @see TraverseListener
 * @see #removeTraverseListener
 */
public void addTraverseListener (TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control gains or loses focus.
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
 * @see FocusListener
 * @see #addFocusListener
 */
public void removeFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.FocusIn, listener);
	eventTable.unhook (SWT.FocusOut, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard.
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
 * @see KeyListener
 * @see #addKeyListener
 */
public void removeKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when mouse buttons are pressed and released.
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
 * @see MouseListener
 * @see #addMouseListener
 */
public void removeMouseListener (MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseDown, listener);
	eventTable.unhook (SWT.MouseUp, listener);
	eventTable.unhook (SWT.MouseDoubleClick, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse moves.
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
 * @see MouseMoveListener
 * @see #addMouseMoveListener
 */
public void removeMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseMove, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse passes or hovers over controls.
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
 * @see MouseTrackListener
 * @see #addMouseTrackListener
 */
public void removeMouseTrackListener(MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver needs to be painted.
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
 * @see PaintListener
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see TraverseListener
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

Menu [] findMenus (Control control) {
	if (menu != null && this != control) return new Menu [] {menu};
	return new Menu [0];
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	oldShell.fixShell (newShell, this);
	oldDecorations.fixDecorations (newDecorations, this, menus);
}

/**
 * Forces the receiver to have the <em>keyboard focus</em>, causing
 * all keyboard events to be delivered to it.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setFocus
 */
public boolean forceFocus () {
	checkWidget();
	Shell shell = getShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible ()) return false;
	shell.bringToTop (false);
	int focusHandle = focusHandle ();
	OS.gtk_widget_grab_focus (focusHandle);
	return OS.gtk_widget_is_focus (focusHandle);
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getBackground () {
	checkWidget();
	return Color.gtk_new (display, getBackgroundColor ());
}

GdkColor getBackgroundColor () {
	return getBgColor ();
}

GdkColor getBgColor () {
	int fontHandle = fontHandle ();
	OS.gtk_widget_realize (fontHandle);
	GdkColor color = new GdkColor ();
	OS.gtk_style_get_bg (OS.gtk_widget_get_style (fontHandle), OS.GTK_STATE_NORMAL, color);
	return color;
}

GdkColor getBaseColor () {
	int fontHandle = fontHandle ();
	OS.gtk_widget_realize (fontHandle);
	GdkColor color = new GdkColor ();
	OS.gtk_style_get_base (OS.gtk_widget_get_style (fontHandle), OS.GTK_STATE_NORMAL, color);
	return color;
}

/**
 * Returns the receiver's border width.
 *
 * @return the border width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getBorderWidth () {
	checkWidget();
	return 0;
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
	return (state & DISABLED) == 0;
}

/**
 * Returns the font that the receiver will use to paint textual information.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Font getFont () {
	checkWidget();
	if (font != null) return font;
	return Font.gtk_new (display, defaultFont ());
}
	
int getFontDescription () {
	int fontHandle = fontHandle ();
	return OS.gtk_style_get_font_desc (OS.gtk_widget_get_style (fontHandle));
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getForeground () {
	checkWidget();
	return Color.gtk_new (display, getForegroundColor ());
}

GdkColor getForegroundColor () {
	return getFgColor ();
}

GdkColor getFgColor () {
	int fontHandle = fontHandle ();
	OS.gtk_widget_realize (fontHandle);
	GdkColor color = new GdkColor ();
	OS.gtk_style_get_fg (OS.gtk_widget_get_style (fontHandle), OS.GTK_STATE_NORMAL, color);
	return color;
}

Point getIMCaretPos () {
	return new Point (0, 0);
}

GdkColor getTextColor () {
	int fontHandle = fontHandle ();
	OS.gtk_widget_realize (fontHandle);
	GdkColor color = new GdkColor ();
	OS.gtk_style_get_text (OS.gtk_widget_get_style (fontHandle), OS.GTK_STATE_NORMAL, color);
	return color;
}

/**
 * Returns layout data which is associated with the receiver.
 *
 * @return the receiver's layout data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Object getLayoutData () {
	checkWidget();
	return layoutData;
}

/**
 * Returns the receiver's pop up menu if it has one, or null
 * if it does not. All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget();
	return menu;
}

public Monitor getMonitor () {
	checkWidget();
	Monitor monitor = null;
	int screen = OS.gdk_screen_get_default ();
	if (screen != 0) {
		int monitorNumber = OS.gdk_screen_get_monitor_at_window (screen, paintWindow ());
		GdkRectangle dest = new GdkRectangle ();
		OS.gdk_screen_get_monitor_geometry (screen, monitorNumber, dest);
		monitor = new Monitor ();
		monitor.handle = monitorNumber;
		monitor.x = dest.x;
		monitor.y = dest.y;
		monitor.width = dest.width;
		monitor.height = dest.height;
		monitor.clientX = monitor.x;
		monitor.clientY = monitor.y;
		monitor.clientWidth = monitor.width;
		monitor.clientHeight = monitor.height;
	} else {
		monitor = display.getPrimaryMonitor ();
	}
	return monitor;
}

/**
 * Returns the receiver's parent, which must be a <code>Composite</code>
 * or null when the receiver is a shell that was created with null or
 * a display for a parent.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Composite getParent () {
	checkWidget();
	return parent;
}

Control [] getPath () {
	int count = 0;
	Shell shell = getShell ();
	Control control = this;
	while (control != shell) {
		count++;
		control = control.parent;
	}
	control = this;
	Control [] result = new Control [count];
	while (control != shell) {
		result [--count] = control;
		control = control.parent;
	}
	return result;
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells.
 *
 * @return the receiver's shell
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell() {
	checkWidget();
	return _getShell();
}
Shell _getShell() {
	return parent._getShell();
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}
/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget();
	return OS.GTK_WIDGET_VISIBLE (topHandle ()); 
}

int gtk_button_press_event (int widget, int event) {
	Shell shell = _getShell ();
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.type == OS.GDK_3BUTTON_PRESS) return 0;
	display.dragStartX = (int) gdkEvent.x;
	display.dragStartY = (int) gdkEvent.y;
	display.dragging = false;
	int button = gdkEvent.button;
	int type = gdkEvent.type != OS.GDK_2BUTTON_PRESS ? SWT.MouseDown : SWT.MouseDoubleClick;
	sendMouseEvent (type, button, event);
	int result = 0;
	if ((state & MENU) != 0) {
		if (gdkEvent.button == 3 && gdkEvent.type == OS.GDK_BUTTON_PRESS) {
			if (showMenu ((int)gdkEvent.x_root, (int)gdkEvent.y_root)) {
				result = 1;
			}
		}
	}	
	
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/	
	if (!shell.isDisposed ()) {
		shell.setActiveControl (this);
	}
	return result;
}

int gtk_button_release_event (int widget, int event) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	sendMouseEvent (SWT.MouseUp, gdkEvent.button, event);
	return 0;
}

int gtk_commit (int imcontext, int text) {
	if (text == 0) return 0;
	int length = OS.strlen (text);
	if (length == 0) return 0;
	byte [] buffer = new byte [length];
	OS.memmove (buffer, text, length);
	char [] chars = Converter.mbcsToWcs (null, buffer);
	sendIMKeyEvent (SWT.KeyDown, null, chars);
	return 0;
}

int gtk_enter_notify_event (int widget, int event) {
	GdkEventCrossing gdkEvent = new GdkEventCrossing ();
	OS.memmove (gdkEvent, event, GdkEventCrossing.sizeof);
	if (gdkEvent.mode != OS.GDK_CROSSING_NORMAL) return 0;
	if (gdkEvent.subwindow != 0) return 0;
	sendMouseEvent (SWT.MouseEnter, 0, event);
	return 0;
}

int gtk_event_after (int widget, int gdkEvent) {
	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEventButton.sizeof);
	switch (event.type) {
		case OS.GDK_BUTTON_PRESS: {
			if ((state & MENU) == 0) {
				GdkEventButton gdkEventButton = new GdkEventButton ();
				OS.memmove (gdkEventButton, gdkEvent, GdkEventButton.sizeof);
				if (gdkEventButton.button == 3) {
					showMenu ((int) gdkEventButton.x_root, (int) gdkEventButton.y_root);
				}
			}
			break;
		}
		case OS.GDK_FOCUS_CHANGE: {
			GdkEventFocus gdkEventFocus = new GdkEventFocus ();
			OS.memmove (gdkEventFocus, gdkEvent, GdkEventFocus.sizeof);
			sendFocusEvent (gdkEventFocus.in != 0 ? SWT.FocusIn : SWT.FocusOut);
			break;
		}
	}
	return 0;
}
	
int gtk_expose_event (int widget, int eventPtr) {
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return 0;
	GdkEventExpose gdkEvent = new GdkEventExpose ();
	OS.memmove(gdkEvent, eventPtr, GdkEventExpose.sizeof);
	Event event = new Event ();
	event.count = gdkEvent.count;
	event.x = gdkEvent.area_x;
	event.y = gdkEvent.area_y;
	event.width = gdkEvent.area_width;
	event.height = gdkEvent.area_height;
	GC gc = event.gc = new GC (this);
	Region region = Region.gtk_new (display, gdkEvent.region);
	gc.setClipping (region);
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	return 0;
}

int gtk_focus (int widget, int event) {
	/* Stop GTK traversal for every widget */
	return 1;
}

int gtk_focus_in_event (int widget, int event) {
	// widget could be disposed at this point
	if (handle != 0) {
		Control oldControl = display.imControl;
		if (oldControl != this)  {
			if (oldControl != null && !oldControl.isDisposed ()) {
				int oldIMHandle = oldControl.imHandle ();
				if (oldIMHandle != 0) OS.gtk_im_context_reset (oldIMHandle);
			}
		}
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			int imHandle = imHandle ();
			if (imHandle != 0) OS.gtk_im_context_focus_in (imHandle);
		}
	}
	return 0;
}

int gtk_focus_out_event (int widget, int event) {
	// widget could be disposed at this point
	if (handle != 0) {
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			int imHandle = imHandle ();
			if (imHandle != 0) {
				OS.gtk_im_context_focus_out (imHandle);
			}
		}
	}
	return 0;
}

int gtk_key_press_event (int widget, int event) {
	if (!hasFocus ()) return 0;
	int imHandle = imHandle ();
	if (imHandle != 0) {
		if (OS.gtk_im_context_filter_keypress (imHandle, event)) return 1;
	}
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
	if (translateMnemonic (gdkEvent.keyval, gdkEvent)) return 1;
	if (translateTraversal (gdkEvent)) return 1;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;
	return sendKeyEvent (SWT.KeyDown, gdkEvent) ? 0 : 1;
}

int gtk_key_release_event (int widget, int event) {
	if (!hasFocus ()) return 0;
	int imHandle = imHandle ();
	if (imHandle != 0) {
		if (OS.gtk_im_context_filter_keypress (imHandle, event)) return 1;
	}
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
	return sendKeyEvent (SWT.KeyUp, gdkEvent) ? 0 : 1;
}

int gtk_leave_notify_event (int widget, int event) {
	display.removeMouseHoverTimeout (handle);
	GdkEventCrossing gdkEvent = new GdkEventCrossing ();
	OS.memmove (gdkEvent, event, GdkEventCrossing.sizeof);
	if (gdkEvent.mode != OS.GDK_CROSSING_NORMAL) return 0;
	if (gdkEvent.subwindow != 0) return 0;
	sendMouseEvent (SWT.MouseExit, 0, event);
	return 0;
}

int gtk_mnemonic_activate (int widget, int arg1) {
	int result = 0;
	int eventPtr = OS.gtk_get_current_event ();
	if (eventPtr != 0) {
		GdkEventKey keyEvent = new GdkEventKey ();
		OS.memmove (keyEvent, eventPtr, GdkEventKey.sizeof);
		if (keyEvent.type == OS.GDK_KEY_PRESS) {
			Control focusControl = display.getFocusControl ();
			int focusHandle = focusControl != null ? focusControl.focusHandle () : 0;
			if (focusHandle != 0) {
				display.mnemonicControl = this;
				OS.gtk_widget_event (focusHandle, eventPtr);
				display.mnemonicControl = null;
			}
			result = 1;
		}
		OS.gdk_event_free (eventPtr);
	}
	return result;
}

int gtk_motion_notify_event (int widget, int event) {
	if (hooks (SWT.DragDetect)) {
		if (!display.dragging) {
			int []  state = new int [1];
			OS.gdk_event_get_state (event, state);
			if ((state [0] & OS.GDK_BUTTON1_MASK) != 0) {
				double [] px = new double [1], py = new double [1];
				OS.gdk_event_get_coords (event, px, py);
				if (OS.gtk_drag_check_threshold (handle, display.dragStartX, display.dragStartY, (int) px [0], (int) py [0])){
					display.dragging = true;
					Event e = new Event ();
					e.x = display.dragStartX;
					e.y = display.dragStartY;
					postEvent (SWT.DragDetect, e);
				}
			}
		}
	}
	if (hooks (SWT.MouseHover) || filters (SWT.MouseHover)) {
		display.addMouseHoverTimeout (handle);
	}
	sendMouseEvent (SWT.MouseMove, 0, event);
	return 0;
}

int gtk_popup_menu (int widget) {
	if (!hasFocus()) return 0;
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_pointer (0, x, y, null);
	showMenu (x [0], y [0]);
	return 0;
}

int gtk_preedit_changed (int imcontext) {
	display.showIMWindow (this);
	return 0;
}

int gtk_realize (int widget) {
	int window = OS.GTK_WIDGET_WINDOW (paintHandle());
	int imHandle = imHandle();
	if (imHandle != 0) OS.gtk_im_context_set_client_window (imHandle, window);
	if (cursor != null && cursor.isDisposed()) return 0;
	int gdkCursor = cursor != null ? cursor.handle : 0;
	OS.gdk_window_set_cursor (window, gdkCursor);
	return 0;
}

int gtk_show_help (int widget, int helpType) {
	if (!hasFocus ()) return 0;
	return sendHelpEvent (helpType) ? 1 : 0;
}

int gtk_unrealize (int widget) {
	int imHandle = imHandle ();
	if (imHandle != 0) OS.gtk_im_context_set_client_window (imHandle, 0);
	return 0;	
}

/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data 
 * @return the platform specific GC handle
 */
public int internal_new_GC (GCData data) {
	checkWidget ();
	int window = paintWindow ();
	if (window == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int gdkGC = OS.gdk_gc_new (window);
	if (gdkGC == 0) error (SWT.ERROR_NO_HANDLES);	
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= style & (mask | SWT.MIRRORED);
		}
		data.drawable = window;
		data.device = display;
		data.background = getBackgroundColor ();
		data.foreground = getForegroundColor ();
		data.font = font != null ? font.handle : defaultFont (); 
	}	
	return gdkGC;
}

int imHandle () {
	return 0;
}

/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data 
 */
public void internal_dispose_GC (int gdkGC, GCData data) {
	checkWidget ();
	OS.g_object_unref (gdkGC);
}

/**
 * Returns <code>true</code> if the underlying operating
 * system supports this reparenting, otherwise <code>false</code>
 *
 * @return <code>true</code> if the widget can be reparented, otherwise <code>false</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isReparentable () {
	checkWidget();
	return true;
}
boolean isShowing () {
	/*
	* This is not complete.  Need to check if the
	* widget is obscurred by a parent or sibling.
	*/
	if (!isVisible ()) return false;
	Control control = this;
	while (control != null) {
		Point size = control.getSize ();
		if (size.x == 1 || size.y == 1) {
			return false;
		}
		control = control.parent;
	}
	return true;
}
boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
	int code = traversalCode (0, null);
	if ((code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0) return false;
	return (code & (SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_TAB_NEXT)) != 0;
}
boolean isTabItem () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return false;
		}
	}
	int code = traversalCode (0, null);
	return (code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0;
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

boolean isFocusAncestor () {
	Control control = display.getFocusControl ();
	while (control != null && control != this) {
		control = control.parent;
	}
	return control == this;
}

/**
 * Returns <code>true</code> if the receiver has the user-interface
 * focus, and <code>false</code> otherwise.
 *
 * @return the receiver's focus state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isFocusControl () {
	checkWidget();
	return hasFocus ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget();
	return getVisible () && parent.isVisible ();
}

Decorations menuShell () {
	return parent.menuShell ();
}

boolean mnemonicHit (char key) {
	return false;
}

boolean mnemonicMatch (char key) {
	return false;
}

void register () {
	super.register ();
	if (fixedHandle != 0) display.addWidget (fixedHandle, this);
	int imHandle = imHandle ();
	if (imHandle != 0) display.addWidget (imHandle, this);
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 */
public void redraw () {
	checkWidget();
	int paintHandle = paintHandle ();
	int width = OS.GTK_WIDGET_WIDTH (paintHandle);
	int height = OS.GTK_WIDGET_HEIGHT (paintHandle);
	redrawWidget (0, 0, width, height, true);
}
/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn. 
 * The next time a paint request is processed, that area of
 * the receiver will be painted. If the <code>all</code> flag
 * is <code>true</code>, any children of the receiver which
 * intersect with the specified area will also paint their
 * intersecting areas. If the <code>all</code> flag is 
 * <code>false</code>, the children will not be painted.
 *
 * @param x the x coordinate of the area to draw
 * @param y the y coordinate of the area to draw
 * @param width the width of the area to draw
 * @param height the height of the area to draw
 * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget();
	redrawWidget (x, y, width, height, all);
}

void redrawWidget (int x, int y, int width, int height, boolean all) {
	int window = paintWindow ();
	GdkRectangle rect = new GdkRectangle ();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	OS.gdk_window_invalidate_rect (window, rect, all);
}

void releaseChild () {
	parent.removeControl (this);
}

void releaseHandle () {
	super.releaseHandle ();
	fixedHandle = 0;
}

void releaseWidget () {
	display.removeMouseHoverTimeout (handle);
	super.releaseWidget ();
	int imHandle = imHandle ();
	if (imHandle != 0) {
		OS.gtk_im_context_reset (imHandle);
		OS.gtk_im_context_set_client_window (imHandle, 0);
	}
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	cursor = null;
	toolTipText = null;
	parent = null;
	layoutData = null;
	accessible = null;
}

void sendFocusEvent (int type) {
	Shell shell = _getShell ();
	sendEvent (type);
	switch (type) {
		case SWT.FocusIn: {
			/*
			* It is possible that the shell may be
			* disposed at this point.  If this happens
			* don't send the activate and deactivate
			* events.
			*/
			if (!shell.isDisposed ()) {
				shell.setActiveControl (this);
			}			
			break;
		}
		case SWT.FocusOut: {
			/*
			* It is possible that the shell may be
			* disposed at this point.  If this happens
			* don't send the activate and deactivate
			* events.
			*/
			if (!shell.isDisposed ()) {
				Display display = shell.display;
				Control control = display.getFocusControl ();
				if (control == null || shell != control.getShell () ) {
					shell.setActiveControl (null);
				}
			}
			break;
		}
	}
}

boolean sendHelpEvent (int helpType) {
	Control control = this;
	while (control != null) {
		if (control.hooks (SWT.Help)) {
			control.postEvent (SWT.Help);
			return true;
		}
		control = control.parent;
	}
	return false;
}

char [] sendIMKeyEvent (int type, GdkEventKey keyEvent, char  [] chars) {
	int index = 0, count = 0, state = 0, time = 0;
	if (keyEvent == null) {
		int ptr = OS.gtk_get_current_event ();
		if (ptr != 0) {
			keyEvent = new GdkEventKey ();
			OS.memmove (keyEvent, ptr, GdkEventKey.sizeof);
			OS.gdk_event_free (ptr);
			switch (keyEvent.type) {
				case OS.GDK_KEY_PRESS:
				case OS.GDK_KEY_RELEASE:
					state = keyEvent.state;
					time =  keyEvent.time;
					break;
				default:
					keyEvent = null;
					break;
			}
		}
	}
	if (keyEvent == null) {
		int [] buffer = new int [1];
		OS.gtk_get_current_event_state (buffer);
		state = buffer [0];
		time = OS.gtk_get_current_event_time();
	}
	while (index < chars.length) {
		Event event = new Event ();
		event.time = time;
		if (keyEvent != null && keyEvent.length <= 1) {
			setKeyState (event, keyEvent);
		} else {
			setInputState (event, state);
		}
		event.character = chars [index];
		sendEvent (type, event);
	
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning null.
		*/
		if (isDisposed ()) return null;
		if (event.doit) chars [count++] = chars [index];
		index++;
	}
	if (count == 0) return null;
	if (index != count) {
		char [] result = new char [count];
		System.arraycopy (chars, 0, result, 0, count);
		return result;
	}
	return chars;
}

boolean sendKeyEvent (int type, GdkEventKey keyEvent) {
	int length = keyEvent.length;
	if (length <= 1) {
		Event event = new Event ();
		event.time = keyEvent.time;
		if (!setKeyState (event, keyEvent)) return true;
		sendEvent (type, event);
		// widget could be disposed at this point
	
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning false.
		*/
		if (isDisposed ()) return false;
		return event.doit;
	}
	byte [] buffer = new byte [length];
	OS.memmove (buffer, keyEvent.string, length);
	char [] chars = Converter.mbcsToWcs (null, buffer);
	return sendIMKeyEvent (type, keyEvent, chars) != null;
}

void sendMouseEvent (int type, int button, int gdkEvent) {
	Event event = new Event ();
	event.time = OS.gdk_event_get_time (gdkEvent);
	event.button = button;
	double [] root_x = new double [1], root_y = new double [1];
	OS.gdk_event_get_root_coords (gdkEvent, root_x, root_y);	
	int window = OS.GTK_WIDGET_WINDOW (eventHandle ());
	int [] origin_x = new int [1], origin_y = new int [1];
	OS.gdk_window_get_origin (window, origin_x, origin_y);
	event.x = (int)(root_x [0] - origin_x [0]);
	event.y = (int)(root_y [0] - origin_y [0]);
	int [] state = new int [1];
	OS.gdk_event_get_state (gdkEvent, state);
	setInputState (event, state [0]);
	postEvent (type, event);
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBackground (Color color) {
	checkWidget();
	GdkColor gdkColor = null;
	if (color != null) {
		if (color.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		gdkColor = color.handle;
	}
	setBackgroundColor (gdkColor);
}

void setBackgroundColor (int handle, GdkColor color) {
	int index = OS.GTK_STATE_NORMAL;
	int style = OS.gtk_widget_get_modifier_style (handle);
	int ptr = OS.gtk_rc_style_get_bg_pixmap_name (style, index);
	if (ptr != 0) OS.g_free (ptr);
	String name = color == null ? "<parent>" : "<none>";
	byte[] buffer = Converter.wcsToMbcs (null, name, true);
	ptr = OS.g_malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.gtk_rc_style_set_bg_pixmap_name (style, index, ptr);
	OS.gtk_rc_style_set_bg (style, index, color);
	int flag = OS.gtk_rc_style_get_color_flags(style, index);
	flag = (color == null) ? flag & ~OS.GTK_RC_BG : flag | OS.GTK_RC_BG;
	OS.gtk_rc_style_set_color_flags(style, index, flag);
	OS.gtk_widget_modify_style (handle, style);
}
void setBackgroundColor (GdkColor color) {
	setBackgroundColor(handle, color);
}

/**
 * If the argument is <code>true</code>, causes the receiver to have
 * all mouse events delivered to it until the method is called with
 * <code>false</code> as the argument.
 *
 * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCapture (boolean capture) {
	checkWidget();
	/* FIXME !!!!! */
	/*
	if (capture) {
		OS.gtk_widget_grab_focus (handle);
	} else {
		OS.gtk_widget_grab_default (handle);
	}
	*/
}
/**
 * Sets the receiver's cursor to the cursor specified by the
 * argument, or to the default cursor for that kind of control
 * if the argument is null.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @param cursor the new cursor (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCursor (Cursor cursor) {
	checkWidget();
	if (cursor != null && cursor.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	int gdkCursor = cursor != null ? cursor.handle : 0;
	int window = OS.GTK_WIDGET_WINDOW (paintHandle ());
	if (window != 0) {
		OS.gdk_window_set_cursor (window, gdkCursor);
		OS.gdk_flush ();
	}
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	if (enabled) {
		if ((state & DISABLED) == 0) return;
		state &= ~DISABLED;
	} else {
		if ((state & DISABLED) != 0) return;
		state |= DISABLED;
	}
	boolean fixFocus = !enabled && isFocusAncestor ();
	enableWidget (enabled);
	if (fixFocus) fixFocus ();
}

/**
 * Causes the receiver to have the <em>keyboard focus</em>, 
 * such that all keyboard events will be delivered to it.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #forceFocus
 */
public boolean setFocus () {
	checkWidget();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	return forceFocus ();
}

/**
 * Sets the font that the receiver will use to paint textual information
 * to the font specified by the argument, or to the default font for that
 * kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setFont (Font font) {
	checkWidget();
	this.font = font;
	int fontDesc;
	if (font == null) {
		fontDesc = defaultFont ();
	} else {
		if (font.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		fontDesc = font.handle;
	}
	setFontDescription (fontDesc);
}
	
void setFontDescription (int font) {
	OS.gtk_widget_modify_font (handle, font);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setForeground (Color color) {
	checkWidget();
	GdkColor gdkColor = null;
	if (color != null) {
		if (color.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		gdkColor = color.handle;
	}
	setForegroundColor (gdkColor);
}

void setForegroundColor (GdkColor color) {
	OS.gtk_widget_modify_fg (handle, OS.GTK_STATE_NORMAL, color);
}

void setInitialSize () {
	resizeHandle (1, 1);
	/*
	* Force the container to allocate the size of its children.
	*/
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_resize_children (parentHandle);
}

/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 *
 * @param menu the new pop up menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget();
	if (menu != null) {
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
		if (menu.parent != menuShell ()) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	this.menu = menu;
}

/**
 * Changes the parent of the widget to be the one provided if
 * the underlying operating system supports this feature.
 * Answers <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	checkWidget ();
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	releaseChild ();
	Shell newShell = parent.getShell (), oldShell = getShell ();
	Decorations newDecorations = parent.menuShell (), oldDecorations = menuShell ();
	Menu [] menus = oldShell.findMenus (this);
	if (oldShell != newShell || oldDecorations != newDecorations) {
		fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
		newDecorations.fixAccelGroup ();
		oldDecorations.fixAccelGroup ();
	}
	int topHandle = topHandle ();
	int newParent = parent.parentingHandle();
	int x = OS.GTK_WIDGET_X (topHandle);
	int y = OS.GTK_WIDGET_Y (topHandle);
	OS.gtk_widget_reparent (topHandle, newParent);
	OS.gtk_fixed_move (newParent, topHandle, x, y);
	this.parent = parent;
	setZOrder (null, false);
	return true;
}

boolean setRadioSelection (boolean value) {
	return false;
}

/**
 * If the argument is <code>false</code>, causes subsequent drawing
 * operations in the receiver to be ignored. No drawing of any kind
 * can occur in the receiver until the flag is set to true.
 * Graphics operations that occurred while the flag was
 * <code>false</code> are lost. When the flag is set to <code>true</code>,
 * the entire widget is marked as needing to be redrawn.
 * <p>
 * Note: This operation is a hint and may not be supported on some
 * platforms or for some widgets.
 * </p>
 *
 * @param redraw the new redraw state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #redraw
 * @see #update
 */
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) {
//			redrawWidget (handle, true);
		}
	} else {
		drawCount++;
	}
}

boolean setTabGroupFocus () {
	return setTabItemFocus ();
}
boolean setTabItemFocus () {
	if (!isShowing ()) return false;
	return forceFocus ();
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	Shell shell = _getShell ();
	shell.setToolTipText (eventHandle (), toolTipText = string);
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget();
	int topHandle = topHandle();
	if ((OS.GTK_WIDGET_VISIBLE (topHandle) == visible)) return;
	if (visible) {
		sendEvent (SWT.Show);
		OS.gtk_widget_show (topHandle);
	} else {
		/*
		* Bug in GTK.  Invoking gtk_widget_hide() on a widget that has
		* focus causes a focus_out_event to be sent. If the client disposes
		* the widget inside the event, GTK GP's.  The fix is to reassign focus
		* before hiding the widget.
		* 
		* NOTE: In order to stop the same widget from taking focus,
		* temporarily clear and set the GTK_VISIBLE flag.
		*/
		if (!visible && isFocusAncestor ()) {
			int flags = OS.GTK_WIDGET_FLAGS (topHandle);
			OS.GTK_WIDGET_UNSET_FLAGS (topHandle, OS.GTK_VISIBLE);
			fixFocus ();
			if (isDisposed ()) return;
			if ((flags & OS.GTK_VISIBLE) != 0) {
				OS.GTK_WIDGET_SET_FLAGS (topHandle, OS.GTK_VISIBLE);
			}
		}
		OS.gtk_widget_hide (topHandle);
		sendEvent (SWT.Hide);
	}
}

void setZOrder (Control sibling, boolean above) {
	 setZOrder (sibling, above, true);
}

void setZOrder (Control sibling, boolean above, boolean fixChildren) {
	int topHandle = topHandle ();
	int siblingHandle = sibling != null ? sibling.topHandle () : 0;
	int window = OS.GTK_WIDGET_WINDOW (topHandle);
	if (window != 0) {
		int siblingWindow = sibling != null ? OS.GTK_WIDGET_WINDOW (siblingHandle) : 0;
		if (!OS.GDK_WINDOWING_X11 () || siblingWindow == 0) {
				if (above) {
					OS.gdk_window_raise (window);
				} else {
					OS.gdk_window_lower (window);
				}
		} else {
			XWindowChanges changes = new XWindowChanges ();
			changes.sibling = OS.gdk_x11_drawable_get_xid (siblingWindow);
			changes.stack_mode = above ? OS.Above : OS.Below;
			int xDisplay = OS.gdk_x11_drawable_get_xdisplay (window);
			int xWindow = OS.gdk_x11_drawable_get_xid (window);
			int xScreen = OS.XDefaultScreen (xDisplay);
			int flags = OS.CWStackMode | OS.CWSibling;
			/*
			* Feature in X. If the receiver is a top level, XConfigureWindow ()
			* will fail (with a BadMatch error) for top level shells because top
			* level shells are reparented by the window manager and do not share
			* the same X window parent.  This is the correct behavior but it is
			* unexpected.  The fix is to use XReconfigureWMWindow () instead.
			* When the receiver is not a top level shell, XReconfigureWMWindow ()
			* behaves the same as XConfigureWindow ().
			*/
			OS.XReconfigureWMWindow (xDisplay, xWindow, xScreen, flags, changes);
		}
	}
	if (fixChildren) {
		if (above) {
			parent.moveAbove (topHandle, siblingHandle);
		} else {
			parent.moveBelow (topHandle, siblingHandle);
		}
	}
	/*
	* Make sure that the parent handle is on the bottom of the stack
	* when the parent children are siblings of the parent handle.
	*/
	if (!above && fixChildren && parent.parentingHandle () == parent.fixedHandle) {
		window = OS.GTK_WIDGET_WINDOW (parent.handle);
		if (window != 0) OS.gdk_window_lower (window);
	}
}

boolean showMenu (int x, int y) {
	Event event = new Event ();
	event.x = x;
	event.y = y;
	sendEvent (SWT.MenuDetect, event);
	if (event.doit) {
		if (menu != null && !menu.isDisposed ()) {
			menu.createIMMenu (imHandle());
			if (event.x != x || event.y != y) {
				menu.setLocation (event.x, event.y);
			}
			menu.setVisible (true);
			return true;
		}
	}
	return false;
}

void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

/**
 * Based on the argument, perform one of the expected platform
 * traversal action. The argument should be one of the constants:
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>, 
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>, 
 * <code>SWT.TRAVERSE_ARROW_NEXT</code> and <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>.
 *
 * @param traversal the type of traversal
 * @return true if the traversal succeeded
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean traverse (int traversal) {
	checkWidget ();
	if (!isFocusControl () && !setFocus ()) return false;
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

boolean translateMnemonic (Event event, Control control) {
	if (control == this) return false;
	if (!isVisible () || !isEnabled ()) return false;
	event.doit = this == display.mnemonicControl || mnemonicMatch (event.character);
	return traverse (event);
}

boolean translateMnemonic (int keyval, GdkEventKey gdkEvent) {
	int key = OS.gdk_keyval_to_unicode (keyval);
	if (key < 0x20) return false;
	if (gdkEvent.state == 0) {
		int code = traversalCode (keyval, gdkEvent);
		if ((code & SWT.TRAVERSE_MNEMONIC) == 0) return false;
	} else {
		Shell shell = _getShell ();
		int mask = OS.GDK_CONTROL_MASK | OS.GDK_SHIFT_MASK | OS.GDK_MOD1_MASK;
		if ((gdkEvent.state & mask) != OS.gtk_window_get_mnemonic_modifier (shell.shellHandle)) return false;
	}
	Decorations shell = menuShell ();
	if (shell.isVisible () && shell.isEnabled ()) {
		Event event = new Event ();
		event.detail = SWT.TRAVERSE_MNEMONIC;
		if (setKeyState (event, gdkEvent)) {
			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
		}
	}
	return false;
}

boolean translateTraversal (GdkEventKey keyEvent) {
	int detail = SWT.TRAVERSE_NONE;
	int key = keyEvent.keyval;
	int code = traversalCode (key, keyEvent);
	boolean all = false;
	switch (key) {
		case OS.GDK_Escape: {
			all = true;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		}
		case OS.GDK_KP_Enter:
		case OS.GDK_Return: {
			all = true;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		case OS.GDK_ISO_Left_Tab: 
		case OS.GDK_Tab: {
			boolean next = (keyEvent.state & OS.GDK_SHIFT_MASK) == 0;
			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		}
		case OS.GDK_Up:
		case OS.GDK_Left: 
		case OS.GDK_Down:
		case OS.GDK_Right: {
			boolean next = key == OS.GDK_Down || key == OS.GDK_Right;
			detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		}
		case OS.GDK_Page_Up:
		case OS.GDK_Page_Down: {
			all = true;
			if ((keyEvent.state & OS.GDK_CONTROL_MASK) == 0) return false;
			detail = key == OS.GDK_Page_Down ? SWT.TRAVERSE_PAGE_NEXT : SWT.TRAVERSE_PAGE_PREVIOUS;
			break;
		}
		default:
			return false;
	}
	Event event = new Event ();
	event.doit = (code & detail) != 0;
	event.detail = detail;
	event.time = keyEvent.time;
	if (!setKeyState (event, keyEvent)) return false;
	Shell shell = getShell ();
	Control control = this;
	do {
		if (control.traverse (event)) return true;
		if (!event.doit && control.hooks (SWT.Traverse)) return false;
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
}

int traversalCode (int key, GdkEventKey event) {
	int code = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_TAB_NEXT |  SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_PAGE_NEXT | SWT.TRAVERSE_PAGE_PREVIOUS;
	Shell shell = getShell ();
	if (shell.parent != null) code |= SWT.TRAVERSE_ESCAPE;
	return code;
}

boolean traverse (Event event) {
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the traverse
	* event.  If this happens, return true to stop further
	* event processing.
	*/	
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return true;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:			return true;
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:	return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:		return traverseMnemonic (event.character);	
		case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
	}
	return false;
}

boolean traverseEscape () {
	return false;
}

boolean traverseGroup (boolean next) {
	Control root = computeTabRoot ();
	Control group = computeTabGroup ();
	Control [] list = root.computeTabList ();
	int length = list.length;
	int index = 0;
	while (index < length) {
		if (list [index] == group) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = ((index + offset + length) % length)) != start) {
		Control control = list [index];
		if (!control.isDisposed () && control.setTabGroupFocus ()) {
			if (!isDisposed () && !isFocusControl ()) return true;
		}
	}
	if (group.isDisposed ()) return false;
	return group.setTabGroupFocus ();
}

boolean traverseItem (boolean next) {
	Control [] children = parent._getChildren ();
	int length = children.length;
	int index = 0;
	while (index < length) {
		if (children [index] == this) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		Control child = children [index];
		if (!child.isDisposed () && child.isTabItem ()) {
			if (child.setTabItemFocus ()) return true;
		}
	}
	return false;
}

boolean traverseReturn () {
	return false;
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseMnemonic (char key) {
	return mnemonicHit (key);
}

/**
 * Forces all outstanding paint requests for the widget
 * to be processed before this method returns.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw
 */
public void update () {
	checkWidget ();
	update (false);
}

void update (boolean all) {
	OS.gdk_flush ();
	int window = paintWindow ();
	int gc = OS.gdk_gc_new (window);
	OS.gdk_gc_set_exposures (gc, true);
	OS.gdk_draw_drawable (window, gc, window, 0, 0, 0, 0, 0, 0);
	OS.g_object_unref (gc);
	int event = 0;
	while ((event = OS.gdk_event_get_graphics_expose (window)) != 0) {
		OS.gtk_main_do_event (event);
		OS.gdk_event_free (event);	
	}
	OS.gdk_window_process_updates (window, all);
}
}

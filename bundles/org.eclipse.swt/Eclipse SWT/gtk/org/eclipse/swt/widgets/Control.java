/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.accessibility.gtk.ATK;
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
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize, Traverse,
 *     DragDetect, MenuDetect</dd>
 * </dl>
 * </p><p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public abstract class Control extends Widget implements Drawable {
	int /*long*/ fixedHandle;
	int /*long*/ redrawWindow, enableWindow;
	int drawCount;
	Composite parent;
	Cursor cursor;
	Menu menu;
	Image backgroundImage;
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

int /*long*/ defaultFont () {
	return display.getSystemFont ().handle;
}

void deregister () {
	super.deregister ();
	if (fixedHandle != 0) display.removeWidget (fixedHandle);
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) display.removeWidget (imHandle);
}

boolean drawGripper (int x, int y, int width, int height, boolean vertical) {
	int /*long*/ paintHandle = paintHandle ();
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (paintHandle);
	if (window == 0) return false;
	int orientation = vertical ? OS.GTK_ORIENTATION_HORIZONTAL : OS.GTK_ORIENTATION_VERTICAL;
	OS.gtk_paint_handle (OS.gtk_widget_get_style (paintHandle), window, OS.GTK_STATE_NORMAL, OS.GTK_SHADOW_OUT, null, paintHandle, new byte [1], x, y, width, height, orientation);
	return true;
}

void enableWidget (boolean enabled) {
	OS.gtk_widget_set_sensitive (handle, enabled);
}

int /*long*/ eventHandle () {
	return handle;
}

int /*long*/ eventWindow () {
	int /*long*/ eventHandle = eventHandle ();
	OS.gtk_widget_realize (eventHandle);
	return OS.GTK_WIDGET_WINDOW (eventHandle);
}

void fixFocus (Control focusControl) {
	Shell shell = getShell ();
	Control control = this;
	while (control != shell && (control = control.parent) != null) {
		if (control.setFocus ()) return;
	}
	shell.setSavedFocus (focusControl);
	int /*long*/ focusHandle = shell.vboxHandle;
	OS.GTK_WIDGET_SET_FLAGS (focusHandle, OS.GTK_CAN_FOCUS);
	OS.gtk_widget_grab_focus (focusHandle);
	OS.GTK_WIDGET_UNSET_FLAGS (focusHandle, OS.GTK_CAN_FOCUS);
}

void fixStyle (int /*long*/ handle) {
	/*
	* Feature in GTK.  Some GTK themes apply a different background to
	* the contents of a GtkNotebook.  However, in an SWT TabFolder, the
	* children are not parented below the GtkNotebook widget, and usually
	* have their own GtkFixed.  The fix is to look up the correct style
	* for a child of a GtkNotebook and apply it to any GtkFixed widgets
	* that are direct children of an SWT TabFolder.
	*/
	int /*long*/ childStyle = parent.childStyle ();
	if (childStyle != 0) {
		OS.gtk_widget_set_style (handle, childStyle);
	}
}

int /*long*/ focusHandle () {
	return handle;
}

int /*long*/ fontHandle () {
	return handle;
}

boolean hasFocus () {
	return this == display.getFocusControl();
}

void hookEvents () {
	/* Connect the keyboard signals */
	int /*long*/ focusHandle = focusHandle ();
	int focusMask = OS.GDK_KEY_PRESS_MASK | OS.GDK_KEY_RELEASE_MASK | OS.GDK_FOCUS_CHANGE_MASK;
	OS.gtk_widget_add_events (focusHandle, focusMask);
	OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [POPUP_MENU], 0, display.closures [POPUP_MENU], false);
	OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [SHOW_HELP], 0, display.closures [SHOW_HELP], false);
	OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [KEY_PRESS_EVENT], 0, display.closures [KEY_PRESS_EVENT], false);
	OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [KEY_RELEASE_EVENT], 0, display.closures [KEY_RELEASE_EVENT], false);
	OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [FOCUS], 0, display.closures [FOCUS], false);
	OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [FOCUS_IN_EVENT], 0, display.closures [FOCUS_IN_EVENT], false);
	OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [FOCUS_OUT_EVENT], 0, display.closures [FOCUS_OUT_EVENT], false);

	/* Connect the mouse signals */
	int /*long*/ eventHandle = eventHandle ();
	int eventMask = OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_PRESS_MASK |
		OS.GDK_BUTTON_RELEASE_MASK | OS.GDK_ENTER_NOTIFY_MASK |
		OS.GDK_LEAVE_NOTIFY_MASK;
	OS.gtk_widget_add_events (eventHandle, eventMask);
	OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT], false);
	OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.closures [BUTTON_RELEASE_EVENT], false);
	OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.closures [MOTION_NOTIFY_EVENT], false);
	OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.closures [ENTER_NOTIFY_EVENT], false);
	OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [LEAVE_NOTIFY_EVENT], 0, display.closures [LEAVE_NOTIFY_EVENT], false);
	OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [SCROLL_EVENT], 0, display.closures [SCROLL_EVENT], false);
	/*
	* Feature in GTK.  Events such as mouse move are propagate up
	* the widget hierarchy and are seen by the parent.  This is the
	* correct GTK behavior but not correct for SWT.  The fix is to
	* hook a signal after and stop the propagation using a negative
	* event number to distinguish this case.
	* 
	* The signal is hooked to the fixedHandle to catch events sent to
	* lightweight widgets.
	*/
	int /*long*/ blockHandle = fixedHandle != 0 ? fixedHandle : eventHandle;
	OS.g_signal_connect_closure_by_id (blockHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT_INVERSE], true);
	OS.g_signal_connect_closure_by_id (blockHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.closures [BUTTON_RELEASE_EVENT_INVERSE], true);
	OS.g_signal_connect_closure_by_id (blockHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.closures [MOTION_NOTIFY_EVENT_INVERSE], true);

	/* Connect the event_after signal for both key and mouse */
	OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [EVENT_AFTER], 0, display.closures [EVENT_AFTER], false);
	if (focusHandle != eventHandle) {
		OS.g_signal_connect_closure_by_id (focusHandle, display.signalIds [EVENT_AFTER], 0, display.closures [EVENT_AFTER], false);
	}
	
	/* Connect the paint signal */
	int /*long*/ paintHandle = paintHandle ();
	int paintMask = OS.GDK_EXPOSURE_MASK | OS.GDK_VISIBILITY_NOTIFY_MASK;
	OS.gtk_widget_add_events (paintHandle, paintMask);
	OS.g_signal_connect_closure_by_id (paintHandle, display.signalIds [EXPOSE_EVENT], 0, display.closures [EXPOSE_EVENT_INVERSE], false);
	OS.g_signal_connect_closure_by_id (paintHandle, display.signalIds [VISIBILITY_NOTIFY_EVENT], 0, display.closures [VISIBILITY_NOTIFY_EVENT], false);
	OS.g_signal_connect_closure_by_id (paintHandle, display.signalIds [EXPOSE_EVENT], 0, display.closures [EXPOSE_EVENT], true);

	/* Connect the Input Method signals */
	OS.g_signal_connect_closure_by_id (handle, display.signalIds [REALIZE], 0, display.closures [REALIZE], true);
	OS.g_signal_connect_closure_by_id (handle, display.signalIds [UNREALIZE], 0, display.closures [UNREALIZE], false);
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) {
		OS.g_signal_connect_closure (imHandle, OS.commit, display.closures [COMMIT], false);
		OS.g_signal_connect_closure (imHandle, OS.preedit_changed, display.closures [PREEDIT_CHANGED], false);
	}
	
	if ((state & PARENT_BACKGROUND) != 0) {
		OS.g_signal_connect_closure_by_id (handle, display.signalIds [STYLE_SET], 0, display.closures [STYLE_SET], false);
	}

	int /*long*/ topHandle = topHandle ();
	OS.g_signal_connect_closure_by_id (topHandle, display.signalIds [MAP], 0, display.closures [MAP], true);
}

int /*long*/ hoverProc (int /*long*/ widget) {
	int [] x = new int [1], y = new int [1], mask = new int [1];
	OS.gdk_window_get_pointer (0, x, y, mask);
	sendMouseEvent (SWT.MouseHover, 0, /*time*/0, x [0], y [0], false, mask [0]);
	/* Always return zero in order to cancel the hover timer */
	return 0;
}

int /*long*/ topHandle() {
	if (fixedHandle != 0) return fixedHandle;
	return super.topHandle ();
}

int /*long*/ paintHandle () {
	int /*long*/ topHandle = topHandle ();
	int /*long*/ paintHandle = handle;
	while (paintHandle != topHandle) {
		if ((OS.GTK_WIDGET_FLAGS (paintHandle) & OS.GTK_NO_WINDOW) == 0) break;
		paintHandle = OS.gtk_widget_get_parent (paintHandle);
	}
	return paintHandle;
}

int /*long*/ paintWindow () {
	int /*long*/ paintHandle = paintHandle ();
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
 * @see #pack(boolean)
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

void checkBuffered () {
	style |= SWT.DOUBLE_BUFFERED;
}

void checkBackground () {
	Shell shell = getShell ();
	if (this == shell) return;
	state &= ~PARENT_BACKGROUND;
	Composite composite = parent;
	do {
		int mode = composite.backgroundMode;
		if (mode != SWT.INHERIT_NONE) {
			if (mode == SWT.INHERIT_DEFAULT) {
				Control control = this;
				do {
					if ((control.state & THEME_BACKGROUND) == 0) {
						return;
					}
					control = control.parent;
				} while (control != composite);
			}
			state |= PARENT_BACKGROUND;
			return;
		}
		if (composite == shell) break;		
		composite = composite.parent;
	} while (true);
}

void checkBorder () {
	if (getBorderWidth () == 0) style &= ~SWT.BORDER;
}

int /*long*/ childStyle () {
	return parent.childStyle ();
}

void createWidget (int index) {
	checkOrientation (parent);
	super.createWidget (index);
	checkBackground ();
	checkBuffered ();
	showWidget ();
	setInitialBounds ();
	setZOrder (null, false, false);
	setRelations ();
	checkBorder ();
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
 * @see #pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	return computeNativeSize (handle, wHint, hHint, changed);	
}

Point computeNativeSize (int /*long*/ h, int wHint, int hHint, boolean changed) {
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (h, requisition);
		width = OS.GTK_WIDGET_REQUISITION_WIDTH (h);
		height = OS.GTK_WIDGET_REQUISITION_HEIGHT (h);
	} else if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		int [] reqWidth = new int [1], reqHeight = new int [1];
		OS.gtk_widget_get_size_request (h, reqWidth, reqHeight);
		OS.gtk_widget_set_size_request (h, wHint, hHint);
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (h, requisition);
		OS.gtk_widget_set_size_request (h, reqWidth [0], reqHeight [0]);
		width = wHint == SWT.DEFAULT ? requisition.width : wHint;
		height = hHint == SWT.DEFAULT ? requisition.height : hHint;
	}
	return new Point (width, height);
}

void forceResize () {
	/*
	* Force size allocation on all children of this widget's
	* topHandle.  Note that all calls to gtk_widget_size_allocate()
	* must be preceded by a call to gtk_widget_size_request().
	*/
	int /*long*/ topHandle = topHandle ();
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (topHandle, requisition);
	GtkAllocation allocation = new GtkAllocation ();
	allocation.x = OS.GTK_WIDGET_X (topHandle);
	allocation.y = OS.GTK_WIDGET_Y (topHandle);
	allocation.width = OS.GTK_WIDGET_WIDTH (topHandle);
	allocation.height = OS.GTK_WIDGET_HEIGHT (topHandle);
	OS.gtk_widget_size_allocate (topHandle, allocation);
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
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a shell. In this case, the location is
 * relative to the display.
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
	int /*long*/ topHandle = topHandle ();
	int x = OS.GTK_WIDGET_X (topHandle);
	int y = OS.GTK_WIDGET_Y (topHandle);
	int width = (state & ZERO_WIDTH) != 0 ? 0 : OS.GTK_WIDGET_WIDTH (topHandle);
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : OS.GTK_WIDGET_HEIGHT (topHandle);
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
	setBounds (rect.x, rect.y, Math.max (0, rect.width), Math.max (0, rect.height), true, true);
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and 
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the <code>x</code>
 * and <code>y</code> arguments are relative to the display.
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
	setBounds (x, y, Math.max (0, width), Math.max (0, height), true, true);
}

void markLayout (boolean changed, boolean all) {
	/* Do nothing */
}

void moveHandle (int x, int y) {
	int /*long*/ topHandle = topHandle ();
	int /*long*/ parentHandle = parent.parentingHandle ();
	/*
	* Feature in GTK.  Calling gtk_fixed_move() to move a child causes
	* the whole parent to redraw.  This is a performance problem. The
	* fix is temporarily make the parent not visible during the move.
	* 
	* NOTE: Because every widget in SWT has an X window, the new and
	* old bounds of the child are correctly redrawn.
	*/
	int flags = OS.GTK_WIDGET_FLAGS (parentHandle);
	OS.GTK_WIDGET_UNSET_FLAGS (parentHandle, OS.GTK_VISIBLE);
	OS.gtk_fixed_move (parentHandle, topHandle, x, y);
	if ((flags & OS.GTK_VISIBLE) != 0) {
		OS.GTK_WIDGET_SET_FLAGS (parentHandle, OS.GTK_VISIBLE);	
	}
}

void resizeHandle (int width, int height) {
	int /*long*/ topHandle = topHandle ();
	OS.gtk_widget_set_size_request (topHandle, width, height);
	if (topHandle != handle) OS.gtk_widget_set_size_request (handle, width, height);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int /*long*/ topHandle = topHandle ();
	boolean sameOrigin = true, sameExtent = true;
	if (move) {
		int oldX = OS.GTK_WIDGET_X (topHandle);
		int oldY = OS.GTK_WIDGET_Y (topHandle);
		sameOrigin = x == oldX && y == oldY;
		if (!sameOrigin) {
			if (enableWindow != 0) {
				OS.gdk_window_move (enableWindow, x, y);
			}
			moveHandle (x, y);
		}
	}
	if (resize) {
		int oldWidth = (state & ZERO_WIDTH) != 0 ? 0 : OS.GTK_WIDGET_WIDTH (topHandle);
		int oldHeight = (state & ZERO_HEIGHT) != 0 ? 0 : OS.GTK_WIDGET_HEIGHT (topHandle);
		sameExtent = width == oldWidth && height == oldHeight;
		if (!sameExtent && !(width == 0 && height == 0)) {
			int newWidth = Math.max (1, width);
			int newHeight = Math.max (1, height);
			if (redrawWindow != 0) {
				OS.gdk_window_resize (redrawWindow, newWidth, newHeight);
			}
			if (enableWindow != 0) {
				OS.gdk_window_resize (enableWindow, newWidth, newHeight);
			}
			resizeHandle (newWidth, newHeight);
		}
	}
	if (!sameOrigin || !sameExtent) {
		/*
		* Cause a size allocation this widget's topHandle.  Note that
		* all calls to gtk_widget_size_allocate() must be preceded by
		* a call to gtk_widget_size_request().
		*/
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (topHandle, requisition);
		GtkAllocation allocation = new GtkAllocation ();
		if (move) {
			allocation.x = x;
			allocation.y = y;
		} else {
			allocation.x = OS.GTK_WIDGET_X (topHandle);
			allocation.y = OS.GTK_WIDGET_Y (topHandle);
		}
		if (resize) {
			allocation.width = width;
			allocation.height = height;
		} else {
			allocation.width = OS.GTK_WIDGET_WIDTH (topHandle);
			allocation.height = OS.GTK_WIDGET_HEIGHT (topHandle);
		}
		OS.gtk_widget_size_allocate (topHandle, allocation);
	}
	/*
	* Bug in GTK.  Widgets cannot be sized smaller than 1x1.
	* The fix is to hide zero-sized widgets and show them again
	* when they are resized larger.
	*/
	if (!sameExtent) {
		state = (width == 0) ? state | ZERO_WIDTH : state & ~ZERO_WIDTH;
		state = (height == 0) ? state | ZERO_HEIGHT : state & ~ZERO_HEIGHT;
		if ((state & (ZERO_WIDTH | ZERO_HEIGHT)) != 0) {
			if (enableWindow != 0) {
				OS.gdk_window_hide (enableWindow);
			}
			OS.gtk_widget_hide (topHandle);
		} else {
			if ((state & HIDDEN) == 0) {
				if (enableWindow != 0) {
					OS.gdk_window_show_unraised (enableWindow);
				}
				OS.gtk_widget_show (topHandle);
			}
		}
	}
	int result = 0;
	if (move && !sameOrigin) {
		Control control = findBackgroundControl ();
		if (control != null && control.backgroundImage != null) {
			if (isVisible ()) redrawWidget (0, 0, 0, 0, true, true, true);
		}
		sendEvent (SWT.Move);
		result |= MOVED;
	}
	if (resize && !sameExtent) {
		sendEvent (SWT.Resize);
		result |= RESIZED;
	}
	return result;
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
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
	int /*long*/ topHandle = topHandle ();
	int x = OS.GTK_WIDGET_X (topHandle);
	int y = OS.GTK_WIDGET_Y (topHandle);
	return new Point (x, y);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
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
	setBounds (location.x, location.y, 0, 0, true, false);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
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
	int /*long*/ topHandle = topHandle ();
	int width = (state & ZERO_WIDTH) != 0 ? 0 : OS.GTK_WIDGET_WIDTH (topHandle);
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : OS.GTK_WIDGET_HEIGHT (topHandle);
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
	setBounds (0, 0, Math.max (0, size.x), Math.max (0, size.y), false, true);
}

void setRelations () {
	int /*long*/ parentHandle = parent.parentingHandle ();
	int /*long*/ list = OS.gtk_container_get_children (parentHandle);
	if (list == 0) return;
	int count = OS.g_list_length (list);
	if (count > 1) {
		/*
		 * the receiver is the last item in the list, so its predecessor will
		 * be the second-last item in the list
		 */
		int /*long*/ handle = OS.g_list_nth_data (list, count - 2);
		if (handle != 0) {
			Widget widget = display.getWidget (handle);
			if (widget != null && widget != this) {
				if (widget instanceof Control) {
					Control sibling = (Control)widget;
					sibling.addRelation (this);
				}
			}
		}
	}
	OS.g_list_free (list);
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
	setBounds (0, 0, Math.max (0, width), Math.max (0, height), false, true);
}

/*
 * Answers a boolean indicating whether a Label that precedes the receiver in
 * a layout should be read by screen readers as the recevier's label.
 */
boolean isDescribedByLabel () {
	return true;
}

/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveBelow
 * @see Composite#getChildren
 */
public void moveAbove (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, true, true);
}

/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveAbove
 * @see Composite#getChildren
 */
public void moveBelow (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, false, true);
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
 * @see #computeSize(int, int, boolean)
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
 * @param changed whether or not the receiver's contents have changed
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
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
	int /*long*/ window = eventWindow ();
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
	int /*long*/ window = eventWindow ();
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

void addRelation (Control control) {
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
 * @see PaintListener
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

/*
 * Remove "Labelled by" relations from the receiver.
 */
void removeRelation () {
	if (!isDescribedByLabel ()) return;		/* there will not be any */
	int /*long*/ accessible = OS.gtk_widget_get_accessible (handle);
	if (accessible == 0) return;
	int /*long*/ set = ATK.atk_object_ref_relation_set (accessible);
	int count = ATK.atk_relation_set_get_n_relations (set);
	for (int i = 0; i < count; i++) {
		int /*long*/ relation = ATK.atk_relation_set_get_relation (set, 0);
		ATK.atk_relation_set_remove (set, relation);
	}
	OS.g_object_unref (set);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see TraverseListener
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

boolean dragDetect (int x, int y) {
	return hooks (SWT.DragDetect);
}

boolean dragOverride () {
	return false;
}

boolean filterKey (int keyval, int /*long*/ event) {
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) {
		return OS.gtk_im_context_filter_keypress (imHandle, event);
	}
	return false;
}

Control findBackgroundControl () {
	if ((state & BACKGROUND) != 0 || backgroundImage != null) return this;
	return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundControl () : null;
}

Menu [] findMenus (Control control) {
	if (menu != null && this != control) return new Menu [] {menu};
	return new Menu [0];
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	oldShell.fixShell (newShell, this);
	oldDecorations.fixDecorations (newDecorations, this, menus);
}

int /*long*/ fixedMapProc (int /*long*/ widget) {
	OS.GTK_WIDGET_SET_FLAGS (widget, OS.GTK_MAPPED);
	int /*long*/ widgetList = OS.gtk_container_get_children (widget);
	if (widgetList != 0) {
		int /*long*/ widgets = widgetList;
		while (widgets != 0) {
			int /*long*/ child = OS.g_list_data (widgets);
			if (OS.GTK_WIDGET_VISIBLE (child) && OS.gtk_widget_get_child_visible (child) && !OS.GTK_WIDGET_MAPPED (child)) {
				OS.gtk_widget_map (child);
			}
			widgets = OS.g_list_next (widgets);
		}
		OS.g_list_free (widgetList);
	}
	if ((OS.GTK_WIDGET_FLAGS (widget) & OS.GTK_NO_WINDOW) == 0) {
		OS.gdk_window_show_unraised (OS.GTK_WIDGET_WINDOW (widget));
	}
	return 0;
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
	if (display.focusEvent == SWT.FocusOut) return false;
	Shell shell = getShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible ()) return false;
	shell.bringToTop (false);
	return forceFocus (focusHandle ());
}

boolean forceFocus (int /*long*/ focusHandle) {
	/* When the control is zero sized it must be realized */
	OS.gtk_widget_realize (focusHandle);
	OS.gtk_widget_grab_focus (focusHandle);
	Shell shell = getShell ();
	int /*long*/ shellHandle = shell.shellHandle;
	int /*long*/ handle = OS.gtk_window_get_focus (shellHandle);
	while (handle != 0) {
		if (handle == focusHandle) return true;
		Widget widget = display.getWidget (handle);
		if (widget != null && widget instanceof Control) {
			return widget == this;
		}
		handle = OS.gtk_widget_get_parent (handle);
	}
	return false;
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
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return Color.gtk_new (display, control.getBackgroundColor ());
}

GdkColor getBackgroundColor () {
	return getBgColor ();
}

/**
 * Returns the receiver's background image.
 *
 * @return the background image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Image getBackgroundImage () {
	checkWidget ();
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return control.backgroundImage;
}

GdkColor getBgColor () {
	int /*long*/ fontHandle = fontHandle ();
	OS.gtk_widget_realize (fontHandle);
	GdkColor color = new GdkColor ();
	OS.gtk_style_get_bg (OS.gtk_widget_get_style (fontHandle), OS.GTK_STATE_NORMAL, color);
	return color;
}

GdkColor getBaseColor () {
	int /*long*/ fontHandle = fontHandle ();
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
	
int /*long*/ getFontDescription () {
	int /*long*/ fontHandle = fontHandle ();
	OS.gtk_widget_realize (fontHandle);
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
	int /*long*/ fontHandle = fontHandle ();
	OS.gtk_widget_realize (fontHandle);
	GdkColor color = new GdkColor ();
	OS.gtk_style_get_fg (OS.gtk_widget_get_style (fontHandle), OS.GTK_STATE_NORMAL, color);
	return color;
}

Point getIMCaretPos () {
	return new Point (0, 0);
}

GdkColor getTextColor () {
	int /*long*/ fontHandle = fontHandle ();
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

/**
 * Returns the receiver's monitor.
 * 
 * @return the receiver's monitor
 * 
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget();
	Monitor monitor = null;
	int /*long*/ screen = OS.gdk_screen_get_default ();
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
	return (state & HIDDEN) == 0;
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.type == OS.GDK_3BUTTON_PRESS) return 0;
	Shell shell = _getShell ();
	if ((shell.style & SWT.ON_TOP) != 0) shell.forceActive ();
	display.dragStartX = (int) gdkEvent.x;
	display.dragStartY = (int) gdkEvent.y;
	display.dragging = display.dragOverride = false;
	int /*long*/ result;
	if (gdkEvent.type == OS.GDK_BUTTON_PRESS) {
		display.clickCount = 1;
		int /*long*/ nextEvent = OS.gdk_event_peek ();
		if (nextEvent != 0) {
			int eventType = OS.GDK_EVENT_TYPE (nextEvent);
			if (eventType == OS.GDK_2BUTTON_PRESS) display.clickCount = 2;
			if (eventType == OS.GDK_3BUTTON_PRESS) display.clickCount = 3;
			OS.gdk_event_free (nextEvent);
		}
		result = sendMouseEvent (SWT.MouseDown, gdkEvent.button, display.clickCount, 0, false, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
		if (isDisposed ()) return 1;
		if (gdkEvent.button == 1) {
			display.dragOverride = dragOverride () && dragDetect (display.dragStartX, display.dragStartY);
			if (display.dragOverride) result = 1;
		}
		if ((state & MENU) != 0) {
			if (gdkEvent.button == 3) {
				if (showMenu ((int)gdkEvent.x_root, (int)gdkEvent.y_root)) {
					result = 1;
				}
			}
		}
	} else {
		display.clickCount = 2;
		result = sendMouseEvent (SWT.MouseDoubleClick, gdkEvent.button, display.clickCount, 0, false, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
		if (isDisposed ()) return 1;
	}
	if (!shell.isDisposed ()) shell.setActiveControl (this);
	return result;
}

int /*long*/ gtk_button_release_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	/*
	* Feature in GTK.  When button 4, 5, 6, or 7 is released, GTK
	* does not deliver a corresponding GTK event.  Button 6 and 7
	* are mapped to buttons 4 and 5 in SWT.  The fix is to change
	* the button number of the event to a negative number so that
	* it gets dispatched by GTK.  SWT has been modified to look
	* for negative button numbers.
	*/
	int button = gdkEvent.button;
	switch (button) {
		case -6: button = 4; break;
		case -7: button = 5; break;
	}
	return sendMouseEvent (SWT.MouseUp, button, display.clickCount, 0, false, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
}

int /*long*/ gtk_commit (int /*long*/ imcontext, int /*long*/ text) {
	if (text == 0) return 0;
	int length = OS.strlen (text);
	if (length == 0) return 0;
	byte [] buffer = new byte [length];
	OS.memmove (buffer, text, length);
	char [] chars = Converter.mbcsToWcs (null, buffer);
	sendIMKeyEvent (SWT.KeyDown, null, chars);
	return 0;
}

int /*long*/ gtk_enter_notify_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventCrossing gdkEvent = new GdkEventCrossing ();
	OS.memmove (gdkEvent, event, GdkEventCrossing.sizeof);
	if (gdkEvent.mode != OS.GDK_CROSSING_NORMAL && gdkEvent.mode != OS.GDK_CROSSING_UNGRAB) return 0;
	if ((gdkEvent.state & (OS.GDK_BUTTON1_MASK | OS.GDK_BUTTON2_MASK | OS.GDK_BUTTON3_MASK)) != 0) return 0;
	if (gdkEvent.subwindow != 0) return 0;
	return sendMouseEvent (SWT.MouseEnter, 0, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
}

int /*long*/ gtk_event_after (int /*long*/ widget, int /*long*/ gdkEvent) {
	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEvent.sizeof);
	switch (event.type) {
		case OS.GDK_BUTTON_PRESS: {
			if (widget != eventHandle ()) break;
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
			if (widget != focusHandle ()) break;
			GdkEventFocus gdkEventFocus = new GdkEventFocus ();
			OS.memmove (gdkEventFocus, gdkEvent, GdkEventFocus.sizeof);
			sendFocusEvent (gdkEventFocus.in != 0 ? SWT.FocusIn : SWT.FocusOut);
			break;
		}
	}
	return 0;
}
	
int /*long*/ gtk_expose_event (int /*long*/ widget, int /*long*/ eventPtr) {
	if ((state & OBSCURED) != 0) return 0;
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return 0;
	GdkEventExpose gdkEvent = new GdkEventExpose ();
	OS.memmove(gdkEvent, eventPtr, GdkEventExpose.sizeof);
	Event event = new Event ();
	event.count = gdkEvent.count;
	event.x = gdkEvent.area_x;
	event.y = gdkEvent.area_y;
	event.width = gdkEvent.area_width;
	event.height = gdkEvent.area_height;
	GCData data = new GCData ();
	data.damageRgn = gdkEvent.region;
	GC gc = event.gc = GC.gtk_new (this, data);
	OS.gdk_gc_set_clip_region (gc.handle, gdkEvent.region);
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	return 0;
}

int /*long*/ gtk_focus (int /*long*/ widget, int /*long*/ directionType) {
	/* Stop GTK traversal for every widget */
	return 1;
}

int /*long*/ gtk_focus_in_event (int /*long*/ widget, int /*long*/ event) {
	// widget could be disposed at this point
	if (handle != 0) {
		Control oldControl = display.imControl;
		if (oldControl != this)  {
			if (oldControl != null && !oldControl.isDisposed ()) {
				int /*long*/ oldIMHandle = oldControl.imHandle ();
				if (oldIMHandle != 0) OS.gtk_im_context_reset (oldIMHandle);
			}
		}
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			int /*long*/ imHandle = imHandle ();
			if (imHandle != 0) OS.gtk_im_context_focus_in (imHandle);
		}
	}
	return 0;
}

int /*long*/ gtk_focus_out_event (int /*long*/ widget, int /*long*/ event) {
	// widget could be disposed at this point
	if (handle != 0) {
		if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
			int /*long*/ imHandle = imHandle ();
			if (imHandle != 0) {
				OS.gtk_im_context_focus_out (imHandle);
			}
		}
	}
	return 0;
}

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ event) {
	if (!hasFocus ()) return 0;
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
	
	if (translateMnemonic (gdkEvent.keyval, gdkEvent)) return 1;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;
	
	if (filterKey (gdkEvent.keyval, event)) return 1;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;	
	
	if (translateTraversal (gdkEvent)) return 1;
	// widget could be disposed at this point
	if (isDisposed ()) return 0;
	return super.gtk_key_press_event (widget, event);
}

int /*long*/ gtk_key_release_event (int /*long*/ widget, int /*long*/ event) {
	if (!hasFocus ()) return 0;
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) {
		if (OS.gtk_im_context_filter_keypress (imHandle, event)) return 1;
	}
	return super.gtk_key_release_event (widget, event);
}

int /*long*/ gtk_leave_notify_event (int /*long*/ widget, int /*long*/ event) {
	display.removeMouseHoverTimeout (handle);
	GdkEventCrossing gdkEvent = new GdkEventCrossing ();
	OS.memmove (gdkEvent, event, GdkEventCrossing.sizeof);
	if (gdkEvent.mode != OS.GDK_CROSSING_NORMAL && gdkEvent.mode != OS.GDK_CROSSING_UNGRAB) return 0;
	if ((gdkEvent.state & (OS.GDK_BUTTON1_MASK | OS.GDK_BUTTON2_MASK | OS.GDK_BUTTON3_MASK)) != 0) return 0;
	if (gdkEvent.subwindow != 0) return 0;
	return sendMouseEvent (SWT.MouseExit, 0, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
}

int /*long*/ gtk_mnemonic_activate (int /*long*/ widget, int /*long*/ arg1) {
	int result = 0;
	int /*long*/ eventPtr = OS.gtk_get_current_event ();
	if (eventPtr != 0) {
		GdkEventKey keyEvent = new GdkEventKey ();
		OS.memmove (keyEvent, eventPtr, GdkEventKey.sizeof);
		if (keyEvent.type == OS.GDK_KEY_PRESS) {
			Control focusControl = display.getFocusControl ();
			int /*long*/ focusHandle = focusControl != null ? focusControl.focusHandle () : 0;
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

int /*long*/ gtk_motion_notify_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventMotion gdkEvent = new GdkEventMotion ();
	OS.memmove (gdkEvent, event, GdkEventMotion.sizeof);
	if (!display.dragging) {
		if ((gdkEvent.state & OS.GDK_BUTTON1_MASK) != 0) {
			if (dragDetect (display.dragStartX, display.dragStartY)) {
				if (OS.gtk_drag_check_threshold (handle, display.dragStartX, display.dragStartY, (int) gdkEvent.x, (int) gdkEvent.y)) {
					display.dragging = true;
					sendDragEvent (display.dragStartX, display.dragStartY);
					if (isDisposed ()) return 1;
				}
			}
		}
	}
	if (hooks (SWT.MouseHover) || filters (SWT.MouseHover)) {
		display.addMouseHoverTimeout (handle);
	}
	double x = gdkEvent.x_root, y = gdkEvent.y_root;
	int state = gdkEvent.state;
	if (gdkEvent.is_hint != 0) {
		int [] pointer_x = new int [1], pointer_y = new int [1], mask = new int [1];
		int /*long*/ window = eventWindow ();
		OS.gdk_window_get_pointer (window, pointer_x, pointer_y, mask);
		x = pointer_x [0];
		y = pointer_y [0];
		state = mask [0];
	}
	int result = sendMouseEvent (SWT.MouseMove, 0, gdkEvent.time, x, y, gdkEvent.is_hint != 0, state) ? 0 : 1;
	if (display.dragOverride) result = 1;
	return result;
}

int /*long*/ gtk_popup_menu (int /*long*/ widget) {
	if (!hasFocus()) return 0;
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_pointer (0, x, y, null);
	showMenu (x [0], y [0]);
	return 0;
}

int /*long*/ gtk_preedit_changed (int /*long*/ imcontext) {
	display.showIMWindow (this);
	return 0;
}

int /*long*/ gtk_realize (int /*long*/ widget) {
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) {
		int /*long*/ window = OS.GTK_WIDGET_WINDOW (paintHandle ());
		OS.gtk_im_context_set_client_window (imHandle, window);
	}
	if ((state & PARENT_BACKGROUND) != 0 && (state & BACKGROUND) == 0 && backgroundImage == null) {
		setParentBackground ();
	}
	if (backgroundImage != null) {
		int /*long*/ window = OS.GTK_WIDGET_WINDOW (paintHandle ());
		if (window != 0) OS.gdk_window_set_back_pixmap (window, backgroundImage.pixmap, false);
	}
	return 0;
}

int /*long*/ gtk_scroll_event (int /*long*/ widget, int /*long*/ eventPtr) {
	GdkEventScroll gdkEvent = new GdkEventScroll ();
	OS.memmove (gdkEvent, eventPtr, GdkEventScroll.sizeof);
	switch (gdkEvent.direction) {
		case OS.GDK_SCROLL_UP:
			return sendMouseEvent (SWT.MouseWheel, 0, 3, SWT.SCROLL_LINE, true, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
		case OS.GDK_SCROLL_DOWN:
			return sendMouseEvent (SWT.MouseWheel, 0, -3, SWT.SCROLL_LINE, true, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
		case OS.GDK_SCROLL_LEFT:
			return sendMouseEvent (SWT.MouseDown, 4, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
		case OS.GDK_SCROLL_RIGHT:
			return sendMouseEvent (SWT.MouseDown, 5, gdkEvent.time, gdkEvent.x_root, gdkEvent.y_root, false, gdkEvent.state) ? 0 : 1;
	}
	return 0;
}

int /*long*/ gtk_show_help (int /*long*/ widget, int /*long*/ helpType) {
	if (!hasFocus ()) return 0;
	return sendHelpEvent (helpType) ? 1 : 0;
}

int /*long*/ gtk_style_set (int /*long*/ widget, int /*long*/ previousStyle) {
	if ((state & PARENT_BACKGROUND) != 0 && (state & BACKGROUND) == 0 && backgroundImage == null) {
		setParentBackground ();
	}
	if (backgroundImage != null) {
		setBackgroundPixmap (backgroundImage.pixmap);
	}
	return 0;
}

int /*long*/ gtk_unrealize (int /*long*/ widget) {
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) OS.gtk_im_context_set_client_window (imHandle, 0);
	return 0;	
}

int /*long*/ gtk_visibility_notify_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventVisibility gdkEvent = new GdkEventVisibility ();
	OS.memmove (gdkEvent, event, GdkEventVisibility.sizeof);
	int /*long*/ paintWindow = paintWindow();
	int /*long*/ window = gdkEvent.window;
	if (window == paintWindow) {
		if (gdkEvent.state == OS.GDK_VISIBILITY_FULLY_OBSCURED) {
			state |= OBSCURED;
		} else {
			if ((state & OBSCURED) != 0) {		
				int [] width = new int [1], height = new int [1];
				OS.gdk_drawable_get_size (window, width, height);
				GdkRectangle rect = new GdkRectangle ();
				rect.width = width [0];
				rect.height = height [0];
				OS.gdk_window_invalidate_rect (window, rect, false);
			}
			state &= ~OBSCURED;
		}
	}
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
public int /*long*/ internal_new_GC (GCData data) {
	checkWidget ();
	int /*long*/ window = paintWindow ();
	if (window == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int /*long*/ gdkGC = OS.gdk_gc_new (window);
	if (gdkGC == 0) error (SWT.ERROR_NO_HANDLES);	
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= style & (mask | SWT.MIRRORED);
		}
		data.drawable = window;
		data.device = display;
		data.foreground = getForegroundColor ();
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		data.background = control.getBackgroundColor ();
		data.font = font != null ? font.handle : defaultFont (); 
	}	
	return gdkGC;
}

int /*long*/ imHandle () {
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
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data 
 */
public void internal_dispose_GC (int /*long*/ gdkGC, GCData data) {
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
		if (size.x == 0 || size.y == 0) {
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
 * ancestors up to and including the receiver's nearest ancestor
 * shell are enabled.  Otherwise, <code>false</code> is returned.
 * A disabled control is typically not selectable from the user
 * interface and draws with an inactive or "grayed" look.
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

boolean isFocusAncestor (Control control) {
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
	if (this == display.focusControl) return true;
	return hasFocus ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are visible. Otherwise, <code>false</code> is returned.
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
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) display.addWidget (imHandle, this);
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted,
 * including the background.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 */
public void redraw () {
	checkWidget();
	redraw (false);
}

void redraw (boolean all) {
//	checkWidget();
	if (!OS.GTK_WIDGET_VISIBLE (topHandle ())) return;
	redrawWidget (0, 0, 0, 0, true, all, false);
}

/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn. 
 * The next time a paint request is processed, that area of
 * the receiver will be painted, including the background.
 * If the <code>all</code> flag is <code>true</code>, any
 * children of the receiver which intersect with the specified
 * area will also paint their intersecting areas. If the
 * <code>all</code> flag is <code>false</code>, the children
 * will not be painted.
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
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (!OS.GTK_WIDGET_VISIBLE (topHandle ())) return;
	redrawWidget (x, y, width, height, false, all, false);
}

void redrawChildren () {
}

void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean all, boolean trim) {
	if ((OS.GTK_WIDGET_FLAGS (handle) & OS.GTK_REALIZED) == 0) return;
	int /*long*/ window = paintWindow ();
	GdkRectangle rect = new GdkRectangle ();
	if (redrawAll) {
		int [] w = new int [1], h = new int [1];
		OS.gdk_drawable_get_size (window, w, h);
		rect.width = w [0];
		rect.height = h [0];
	} else {
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
	}
	OS.gdk_window_invalidate_rect (window, rect, all);
}

void release (boolean destroy) {
	Control next = null, previous = null;
	if (destroy && parent != null) {
		Control[] children = parent._getChildren ();
		int index = 0;
		while (index < children.length) {
			if (children [index] == this) break;
			index++;
		}
		if (0 < index && (index + 1) < children.length) {
			next = children [index + 1];
			previous = children [index - 1];
		}
	}
	super.release (destroy);
	if (destroy) {
		if (previous != null) previous.addRelation (next);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	fixedHandle = 0;
	parent = null;
}

void releaseParent () {
	parent.removeControl (this);
}

void releaseWidget () {
	super.releaseWidget ();
	display.removeMouseHoverTimeout (handle);
	int /*long*/ imHandle = imHandle ();
	if (imHandle != 0) {
		OS.gtk_im_context_reset (imHandle);
		OS.gtk_im_context_set_client_window (imHandle, 0);
	}
	if (enableWindow != 0) {
		OS.gdk_window_set_user_data (enableWindow, 0);
		OS.gdk_window_destroy (enableWindow);
		enableWindow = 0;
	}
	redrawWindow = 0;
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	cursor = null;
	toolTipText = null;
	layoutData = null;
	accessible = null;
}

boolean sendDragEvent (int x, int y) {
	Event event = new Event ();
	event.x = x;
	event.y = y;
	postEvent (SWT.DragDetect, event);
	if (isDisposed ()) return false;
	return event.doit;
}

void sendFocusEvent (int type) {
	Shell shell = _getShell ();
	Display display = this.display;
	display.focusControl = this;
	display.focusEvent = type;
	sendEvent (type);
	display.focusControl = null;
	display.focusEvent = SWT.None;
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/
	if (!shell.isDisposed ()) {
		switch (type) {
			case SWT.FocusIn: 
				shell.setActiveControl (this);
				break;
			case SWT.FocusOut:
				if (shell != display.activeShell) {
					shell.setActiveControl (null);
				}
				break;
		}
	}
}

boolean sendHelpEvent (int /*long*/ helpType) {
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

boolean sendMouseEvent (int type, int button, int time, double x, double y, boolean is_hint, int state) {
	return sendMouseEvent (type, button, 0, 0, false, time, x, y, is_hint, state);
}

boolean sendMouseEvent (int type, int button, int count, int detail, boolean send, int time, double x, double y, boolean is_hint, int state) {
	if (!hooks (type) && !filters (type)) return true;
	Event event = new Event ();
	event.time = time;
	event.button = button;
	event.detail = detail;
	event.count = count;
	if (is_hint) {
		event.x = (int)x;
		event.y = (int)y;
	} else {
		int /*long*/ window = eventWindow ();
		int [] origin_x = new int [1], origin_y = new int [1];
		OS.gdk_window_get_origin (window, origin_x, origin_y);
		event.x = (int)x - origin_x [0];
		event.y = (int)y - origin_y [0];
	}
	setInputState (event, state);
	if (send) {
		sendEvent (type, event);
		if (isDisposed ()) return false;
	} else {
		postEvent (type, event);
	}
	return event.doit;
}

void setBackground () {
	if ((state & PARENT_BACKGROUND) != 0 && (state & BACKGROUND) == 0 && backgroundImage == null) {
		setParentBackground ();
	} else {
		setWidgetBackground ();
	}
	redrawWidget (0, 0, 0, 0, true, false, false);
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
	if (((state & BACKGROUND) == 0) && color == null) return;
	GdkColor gdkColor = null;
	if (color != null) {
		if (color.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		gdkColor = color.handle;
	}
	boolean set = false;
	if (gdkColor == null) {
		int /*long*/ style = OS.gtk_widget_get_modifier_style (handle);
		set = (OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_NORMAL) & OS.GTK_RC_BG) != 0;
	} else {
		GdkColor oldColor = getBackgroundColor ();
		set = oldColor.pixel != gdkColor.pixel;
	}
	if (set) {
		if (color == null) {
			state &= ~BACKGROUND;
		} else {
			state |= BACKGROUND;
		}
		setBackgroundColor (gdkColor);
		redrawChildren ();
	}
}

void setBackgroundColor (int /*long*/ handle, GdkColor color) {
	int index = OS.GTK_STATE_NORMAL;
	int /*long*/ style = OS.gtk_widget_get_modifier_style (handle);
	int /*long*/ ptr = OS.gtk_rc_style_get_bg_pixmap_name (style, index);
	if (ptr != 0) OS.g_free (ptr);
	String name = color == null ? "<parent>" : "<none>";
	byte[] buffer = Converter.wcsToMbcs (null, name, true);
	ptr = OS.g_malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.gtk_rc_style_set_bg_pixmap_name (style, index, ptr);
	OS.gtk_rc_style_set_bg (style, index, color);
	int flags = OS.gtk_rc_style_get_color_flags (style, index);
	flags = (color == null) ? flags & ~OS.GTK_RC_BG : flags | OS.GTK_RC_BG;
	OS.gtk_rc_style_set_color_flags (style, index, flags);
	OS.gtk_widget_modify_style (handle, style);
}

void setBackgroundColor (GdkColor color) {
	setBackgroundColor (handle, color);
}

/**
 * Sets the receiver's background image to the image specified
 * by the argument, or to the default system color for the control
 * if the argument is null.  The background image is tiled to fill
 * the available space.
 *
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setBackgroundImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (image == backgroundImage) return;
	this.backgroundImage = image;
	if (backgroundImage != null) {
		setBackgroundPixmap (backgroundImage.pixmap);
		redrawWidget (0, 0, 0, 0, true, false, false);
	} else {
		setWidgetBackground ();
	}
	redrawChildren ();
}

void setBackgroundPixmap (int /*long*/ pixmap) {
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (paintHandle ());
	if (window != 0) OS.gdk_window_set_back_pixmap (window, backgroundImage.pixmap, false);
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
	setCursor (cursor != null ? cursor.handle : 0);
}

void setCursor (int /*long*/ cursor) {
	int /*long*/ window = eventWindow ();
	if (window != 0) {
		OS.gdk_window_set_cursor (window, cursor);
		if (!OS.GDK_WINDOWING_X11 ()) {
			OS.gdk_flush ();
		} else {
			int /*long*/ xDisplay = OS.GDK_DISPLAY ();
			OS.XFlush (xDisplay);
		}
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
	if (((state & DISABLED) == 0) == enabled) return;
	Control control = null;
	boolean fixFocus = false;
	if (!enabled) {
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
	}
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
	enableWidget (enabled);
	if (isDisposed ()) return;
	if (enabled) {
		if (enableWindow != 0) {
			OS.gdk_window_set_user_data (enableWindow, 0);
			OS.gdk_window_destroy (enableWindow);
			enableWindow = 0;
		}
	} else {
		OS.gtk_widget_realize (handle);
		int /*long*/ parentHandle = parent.parentingHandle ();
		int /*long*/ window = OS.GTK_WIDGET_WINDOW (parentHandle);
		Rectangle rect = getBounds ();
		GdkWindowAttr attributes = new GdkWindowAttr ();
		attributes.x = rect.x;
		attributes.y = rect.y;
		attributes.width = rect.width;
		attributes.height = rect.height;
		attributes.event_mask = (0xFFFFFFFF & ~OS.ExposureMask);
		attributes.wclass = OS.GDK_INPUT_ONLY;
		attributes.window_type = OS.GDK_WINDOW_CHILD;
		enableWindow = OS.gdk_window_new (window, attributes, OS.GDK_WA_X | OS.GDK_WA_Y);
		if (enableWindow != 0) {
			int /*long*/ topHandle = topHandle ();
			OS.gdk_window_set_user_data (enableWindow, parentHandle);
			if (!OS.GDK_WINDOWING_X11 ()) {
				OS.gdk_window_raise (enableWindow);
			} else {
				int /*long*/ topWindow = OS.GTK_WIDGET_WINDOW (topHandle);			
				int /*long*/ xDisplay = OS.gdk_x11_drawable_get_xdisplay (topWindow);
				int /*long*/ xWindow = OS.gdk_x11_drawable_get_xid (enableWindow);
				int xScreen = OS.XDefaultScreen (xDisplay);
				int flags = OS.CWStackMode | OS.CWSibling;			
				XWindowChanges changes = new XWindowChanges ();
				changes.sibling = OS.gdk_x11_drawable_get_xid (topWindow);
				changes.stack_mode = OS.Above;
				OS.XReconfigureWMWindow (xDisplay, xWindow, xScreen, flags, changes);
			}
			if (OS.GTK_WIDGET_VISIBLE (topHandle)) OS.gdk_window_show_unraised (enableWindow);
		}
	}
	if (fixFocus) fixFocus (control);
}

/**
 * Causes the receiver to have the <em>keyboard focus</em>, 
 * such that all keyboard events will be delivered to it.  Focus
 * reassignment will respect applicable platform constraints.
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
	if (((state & FONT) == 0) && font == null) return;
	this.font = font;
	int /*long*/ fontDesc;
	if (font == null) {
		fontDesc = defaultFont ();
	} else {
		if (font.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		fontDesc = font.handle;
	}
	if (font == null) {
		state &= ~FONT;
	} else {
		state |= FONT;
	}
	setFontDescription (fontDesc);
}
	
void setFontDescription (int /*long*/ font) {
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
	if (((state & FOREGROUND) == 0) && color == null) return;
	GdkColor gdkColor = null;
	if (color != null) {
		if (color.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		gdkColor = color.handle;
	}
	boolean set = false;
	if (gdkColor == null) {
		int /*long*/ style = OS.gtk_widget_get_modifier_style (handle);
		set = (OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_NORMAL) & OS.GTK_RC_FG) != 0;
	} else {
		GdkColor oldColor = getForegroundColor ();
		set = oldColor.pixel != gdkColor.pixel;
	}
	if (set) {
		if (color == null) {
			state &= ~FOREGROUND;
		} else {
			state |= FOREGROUND;
		}
		setForegroundColor (gdkColor);
	}
}

void setForegroundColor (GdkColor color) {
	setForegroundColor (handle, color);
}

void setInitialBounds () {
	if ((state & ZERO_WIDTH) != 0 && (state & ZERO_HEIGHT) != 0) {
		/*
		* Feature in GTK.  On creation, each widget's allocation is
		* initialized to a position of (-1, -1) until the widget is
		* first sized.  The fix is to set the value to (0, 0) as
		* expected by SWT.
		*/
		int /*long*/ topHandle = topHandle ();
		OS.GTK_WIDGET_SET_X (topHandle, 0);
		OS.GTK_WIDGET_SET_Y (topHandle, 0);
	} else {
		resizeHandle (1, 1);
		forceResize ();
	}
}

/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 * <p>
 * Note: Disposing of a control that has a pop up menu will
 * dispose of the menu.  To avoid this behavior, set the
 * menu to null before the control is disposed.
 * </p>
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

void setOrientation () {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (handle != 0) OS.gtk_widget_set_direction (handle, OS.GTK_TEXT_DIR_RTL);
		if (fixedHandle != 0) OS.gtk_widget_set_direction (fixedHandle, OS.GTK_TEXT_DIR_RTL);
	}
}

/**
 * Changes the parent of the widget to be the one provided if
 * the underlying operating system supports this feature.
 * Returns <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is <code>null</code></li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	checkWidget ();
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	releaseParent ();
	Shell newShell = parent.getShell (), oldShell = getShell ();
	Decorations newDecorations = parent.menuShell (), oldDecorations = menuShell ();
	Menu [] menus = oldShell.findMenus (this);
	if (oldShell != newShell || oldDecorations != newDecorations) {
		fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
		newDecorations.fixAccelGroup ();
		oldDecorations.fixAccelGroup ();
	}
	int /*long*/ topHandle = topHandle ();
	int /*long*/ newParent = parent.parentingHandle();
	int x = OS.GTK_WIDGET_X (topHandle);
	int y = OS.GTK_WIDGET_Y (topHandle);
	OS.gtk_widget_reparent (topHandle, newParent);
	OS.gtk_fixed_move (newParent, topHandle, x, y);
	this.parent = parent;
	setZOrder (null, false, true);
	return true;
}

void setParentBackground () {
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (handle);
	if (window != 0) OS.gdk_window_set_back_pixmap (window, 0, true);
	if (fixedHandle != 0) {
		window = OS.GTK_WIDGET_WINDOW (fixedHandle);
		if (window != 0) OS.gdk_window_set_back_pixmap (window, 0, true);
	}
}

void setParentWindow (int /*long*/ widget) {
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
 * the entire widget is marked as needing to be redrawn.  Nested calls
 * to this method are stacked.
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
 * @see #redraw(int, int, int, int, boolean)
 * @see #update
 */
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) {
			if (redrawWindow != 0) {
				int /*long*/ window = paintWindow ();
				/* Explicitly hiding the window avoids flicker on GTK+ >= 2.6 */
				OS.gdk_window_hide (redrawWindow);
				OS.gdk_window_destroy (redrawWindow);
				OS.gdk_window_set_events (window, OS.gtk_widget_get_events (paintHandle ()));
				redrawWindow = 0;
			}
		}
	} else {
		if (drawCount++ == 0) {
			if ((OS.GTK_WIDGET_FLAGS (handle) & OS.GTK_REALIZED) != 0) {
				int /*long*/ window = paintWindow ();
				Rectangle rect = getBounds ();
				GdkWindowAttr attributes = new GdkWindowAttr ();
				attributes.width = rect.width;
				attributes.height = rect.height;
				attributes.event_mask = OS.GDK_EXPOSURE_MASK;
				attributes.window_type = OS.GDK_WINDOW_CHILD;
				redrawWindow = OS.gdk_window_new (window, attributes, 0);
				if (redrawWindow != 0) {
					int mouseMask = OS.GDK_BUTTON_PRESS_MASK | OS.GDK_BUTTON_RELEASE_MASK |
						OS.GDK_ENTER_NOTIFY_MASK | OS.GDK_LEAVE_NOTIFY_MASK |
						OS.GDK_POINTER_MOTION_MASK | OS.GDK_POINTER_MOTION_HINT_MASK |
						OS.GDK_BUTTON_MOTION_MASK | OS.GDK_BUTTON1_MOTION_MASK | 
						OS.GDK_BUTTON2_MOTION_MASK | OS.GDK_BUTTON3_MOTION_MASK;
					OS.gdk_window_set_events (window, OS.gdk_window_get_events (window) & ~mouseMask);
					OS.gdk_window_set_back_pixmap (redrawWindow, 0, false);
					OS.gdk_window_show (redrawWindow);
				}
			}
		}
	}
}

boolean setTabGroupFocus (boolean next) {
	return setTabItemFocus (next);
}
boolean setTabItemFocus (boolean next) {
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
	setToolTipText (_getShell (), string, toolTipText);
	toolTipText = string;
}

void setToolTipText (Shell shell, String newString, String oldString) {
	shell.setToolTipText (eventHandle (), newString, oldString);
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
	if (((state & HIDDEN) == 0) == visible) return;
	int /*long*/ topHandle = topHandle();
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		state &= ~HIDDEN;
		if ((state & (ZERO_WIDTH | ZERO_HEIGHT)) == 0) {
			if (enableWindow != 0) OS.gdk_window_show_unraised (enableWindow);
			OS.gtk_widget_show (topHandle);
		}
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
		Control control = null;
		boolean fixFocus = false;
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
		state |= HIDDEN;
		if (fixFocus) {
			OS.GTK_WIDGET_UNSET_FLAGS (topHandle, OS.GTK_VISIBLE);
			fixFocus (control);
			if (isDisposed ()) return;
			OS.GTK_WIDGET_SET_FLAGS (topHandle, OS.GTK_VISIBLE);
		}
		OS.gtk_widget_hide (topHandle);
		if (isDisposed ()) return;
		if (enableWindow != 0) OS.gdk_window_hide (enableWindow);
		sendEvent (SWT.Hide);
	}
}

void setZOrder (Control sibling, boolean above, boolean fixRelations) {
	 setZOrder (sibling, above, fixRelations, true);
}

void setZOrder (Control sibling, boolean above, boolean fixRelations, boolean fixChildren) {
	int index = 0, siblingIndex = 0, oldNextIndex = -1;
	Control[] children = null;
	if (fixRelations) {
		/* determine the receiver's and sibling's indexes in the parent */
		children = parent._getChildren ();
		while (index < children.length) {
			if (children [index] == this) break;
			index++;
		}
		if (sibling != null) {
			while (siblingIndex < children.length) {
				if (children [siblingIndex] == sibling) break;
				siblingIndex++;
			}
		}
		/* remove "Labelled by" relationships that will no longer be valid */
		removeRelation ();
		if (index + 1 < children.length) {
			oldNextIndex = index + 1;
			children [oldNextIndex].removeRelation ();
		}
		if (sibling != null) {
			if (above) {
				sibling.removeRelation ();
			} else {
				if (siblingIndex + 1 < children.length) {
					children [siblingIndex + 1].removeRelation ();
				}
			}
		}
	}

	int /*long*/ topHandle = topHandle ();
	int /*long*/ siblingHandle = sibling != null ? sibling.topHandle () : 0;
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (topHandle);
	if (window != 0) {
		int /*long*/ siblingWindow = 0;
		if (sibling != null) {
			if (above && sibling.enableWindow != 0) {
				siblingWindow = enableWindow;
			} else {
				siblingWindow = OS.GTK_WIDGET_WINDOW (siblingHandle);
			}
		}
		int /*long*/ redrawWindow = fixChildren ? parent.redrawWindow : 0;
		if (!OS.GDK_WINDOWING_X11 () || (siblingWindow == 0 && (!above || redrawWindow == 0))) {
			if (above) {
				OS.gdk_window_raise (window);
				if (redrawWindow != 0) OS.gdk_window_raise (redrawWindow);
				if (enableWindow != 0) OS.gdk_window_raise (enableWindow);
			} else {
				if (enableWindow != 0) OS.gdk_window_lower (enableWindow);
				OS.gdk_window_lower (window);
			}
		} else {
			XWindowChanges changes = new XWindowChanges ();
			changes.sibling = OS.gdk_x11_drawable_get_xid (siblingWindow != 0 ? siblingWindow : redrawWindow);
			changes.stack_mode = above ? OS.Above : OS.Below;
			if (redrawWindow != 0 && siblingWindow == 0) changes.stack_mode = OS.Below;
			int /*long*/ xDisplay = OS.gdk_x11_drawable_get_xdisplay (window);
			int /*long*/ xWindow = OS.gdk_x11_drawable_get_xid (window);
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
			if (enableWindow != 0) {
				changes.sibling = OS.gdk_x11_drawable_get_xid (window);
				changes.stack_mode = OS.Above;
				xWindow = OS.gdk_x11_drawable_get_xid (enableWindow);
				OS.XReconfigureWMWindow (xDisplay, xWindow, xScreen, flags, changes);
			}
		}
	}
	if (fixChildren) {
		if (above) {
			parent.moveAbove (topHandle, siblingHandle);
		} else {
			parent.moveBelow (topHandle, siblingHandle);
		}
	}
	/*  Make sure that the parent internal windows are on the bottom of the stack	*/
	if (!above && fixChildren) 	parent.fixZOrder ();

	if (fixRelations) {
		/* determine the receiver's new index in the parent */
		if (sibling != null) {
			if (above) {
				index = siblingIndex - (index < siblingIndex ? 1 : 0);
			} else {
				index = siblingIndex + (siblingIndex < index ? 1 : 0);
			}
		} else {
			if (above) {
				index = 0;
			} else {
				index = children.length - 1;
			}
		}

		/* add new "Labelled by" relations as needed */
		children = parent._getChildren ();
		if (0 < index) {
			children [index - 1].addRelation (this);
		}
		if (index + 1 < children.length) {
			addRelation (children [index + 1]);
		}
		if (oldNextIndex != -1) {
			if (oldNextIndex <= index) oldNextIndex--;
			/* the last two conditions below ensure that duplicate relations are not hooked */
			if (0 < oldNextIndex && oldNextIndex != index && oldNextIndex != index + 1) {
				children [oldNextIndex - 1].addRelation (children [oldNextIndex]);
			}
		}
	}
}

void setWidgetBackground  () {
	if (fixedHandle != 0) {
		int /*long*/ style = OS.gtk_widget_get_modifier_style (fixedHandle);
		OS.gtk_widget_modify_style (fixedHandle, style);
	}
	int /*long*/ style = OS.gtk_widget_get_modifier_style (handle);
	OS.gtk_widget_modify_style (handle, style);
}

boolean showMenu (int x, int y) {
	Event event = new Event ();
	event.x = x;
	event.y = y;
	sendEvent (SWT.MenuDetect, event);
	if (event.doit) {
		if (menu != null && !menu.isDisposed ()) {
			boolean hooksKeys = hooks (SWT.KeyDown) || hooks (SWT.KeyUp);
			menu.createIMMenu (hooksKeys ? imHandle() : 0);
			if (event.x != x || event.y != y) {
				menu.setLocation (event.x, event.y);
			}
			menu.setVisible (true);
			return true;
		}
	}
	return false;
}

void showWidget () {
	// Comment this line to disable zero-sized widgets
	state |= ZERO_WIDTH | ZERO_HEIGHT;
	int /*long*/ topHandle = topHandle ();
	int /*long*/ parentHandle = parent.parentingHandle ();
	parent.setParentWindow (topHandle);
	OS.gtk_container_add (parentHandle, topHandle);
	if (handle != 0 && handle != topHandle) OS.gtk_widget_show (handle);
	if ((state & (ZERO_WIDTH | ZERO_HEIGHT)) == 0) {
		if (fixedHandle != 0) OS.gtk_widget_show (fixedHandle);
	}
	if (fixedHandle != 0) fixStyle (fixedHandle);
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
		if (!control.isDisposed () && control.setTabGroupFocus (next)) {
			return true;
		}
	}
	if (group.isDisposed ()) return false;
	return group.setTabGroupFocus (next);
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
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		Control child = children [index];
		if (!child.isDisposed () && child.isTabItem ()) {
			if (child.setTabItemFocus (next)) return true;
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
 * @see #redraw(int, int, int, int, boolean)
 * @see PaintListener
 * @see SWT#Paint
 */
public void update () {
	checkWidget ();
	update (false, true);
}

void update (boolean all, boolean flush) {
//	checkWidget();
	if (!OS.GTK_WIDGET_VISIBLE (topHandle ())) return; 
	if ((OS.GTK_WIDGET_FLAGS (handle) & OS.GTK_REALIZED) == 0) return;
	int /*long*/ window = paintWindow ();
	if (flush) display.flushExposes (window, all);
	OS.gdk_window_process_updates (window, all);
	OS.gdk_flush ();
}

void updateBackgroundMode () {
	int oldState = state & PARENT_BACKGROUND;
	checkBackground ();
	if (oldState != (state & PARENT_BACKGROUND)) {
		setBackground ();
	}
}

void updateLayout (boolean all) {
	/* Do nothing */
}
}

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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent the "windows"
 * which the desktop or "window manager" is managing.
 * Instances that do not have a parent (that is, they
 * are built using the constructor, which takes a 
 * <code>Display</code> as the argument) are described
 * as <em>top level</em> shells. Instances that do have
 * a parent are described as <em>secondary</em> or
 * <em>dialog</em> shells.
 * <p>
 * Instances are always displayed in one of the maximized, 
 * minimized or normal states:
 * <ul>
 * <li>
 * When an instance is marked as <em>maximized</em>, the
 * window manager will typically resize it to fill the
 * entire visible area of the display, and the instance
 * is usually put in a state where it can not be resized 
 * (even if it has style <code>RESIZE</code>) until it is
 * no longer maximized.
 * </li><li>
 * When an instance is in the <em>normal</em> state (neither
 * maximized or minimized), its appearance is controlled by
 * the style constants which were specified when it was created
 * and the restrictions of the window manager (see below).
 * </li><li>
 * When an instance has been marked as <em>minimized</em>,
 * its contents (client area) will usually not be visible,
 * and depending on the window manager, it may be
 * "iconified" (that is, replaced on the desktop by a small
 * simplified representation of itself), relocated to a
 * distinguished area of the screen, or hidden. Combinations
 * of these changes are also possible.
 * </li>
 * </ul>
 * </p>
 * <p>
 * Note: The styles supported by this class must be treated
 * as <em>HINT</em>s, since the window manager for the
 * desktop on which the instance is visible has ultimate
 * control over the appearance and behavior of decorations
 * and modality. For example, some window managers only
 * support resizable windows and will always assume the
 * RESIZE style, even if it is not set. In addition, if a
 * modality style is not supported, it is "upgraded" to a
 * more restrictive modality style that is supported. For
 * example, if <code>PRIMARY_MODAL</code> is not supported,
 * it would be upgraded to <code>APPLICATION_MODAL</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE</dd>
 * <dd>APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Activate, Close, Deactivate, Deiconify, Iconify</dd>
 * </dl>
 * Class <code>SWT</code> provides two "convenience constants"
 * for the most commonly required style combinations:
 * <dl>
 * <dt><code>SHELL_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application top level shell: (that 
 * is, <code>CLOSE | TITLE | MIN | MAX | RESIZE</code>)
 * </dd>
 * <dt><code>DIALOG_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application dialog shell: (that 
 * is, <code>TITLE | CLOSE | BORDER</code>)
 * </dd>
 * </dl>
 * </p>
 * <p>
 * Note: Only one of the styles APPLICATION_MODAL, MODELESS, 
 * PRIMARY_MODAL and SYSTEM_MODAL may be specified.
 * </p><p>
 * IMPORTANT: This class is not intended to be subclassed.
 * </p>
 *
 * @see Decorations
 * @see SWT
 */
public class Shell extends Decorations {
	int shellHandle, tooltipsHandle;
	boolean hasFocus, mapped;
	int oldX, oldY, oldWidth, oldHeight;
	Control lastActive;
	Region region;

/**
 * Constructs a new instance of this class. This is equivalent
 * to calling <code>Shell((Display) null)</code>.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell () {
	this ((Display) null);
}
/**
 * Constructs a new instance of this class given only the style
 * value describing its behavior and appearance. This is equivalent
 * to calling <code>Shell((Display) null, style)</code>.
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
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 * 
 * @see SWT#BORDER
 * @see SWT#CLOSE
 * @see SWT#MIN
 * @see SWT#MAX
 * @see SWT#RESIZE
 * @see SWT#TITLE
 * @see SWT#NO_TRIM
 * @see SWT#SHELL_TRIM
 * @see SWT#DIALOG_TRIM
 * @see SWT#MODELESS
 * @see SWT#PRIMARY_MODAL
 * @see SWT#APPLICATION_MODAL
 * @see SWT#SYSTEM_MODAL
 */
public Shell (int style) {
	this ((Display) null, style);
}

/**
 * Constructs a new instance of this class given only the display
 * to create it on. It is created with style <code>SWT.SHELL_TRIM</code>.
 * <p>
 * Note: Currently, null can be passed in for the display argument.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the display argument is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param display the display to create the shell on
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell (Display display) {
	this (display, SWT.SHELL_TRIM);
}
/**
 * Constructs a new instance of this class given the display
 * to create it on and a style value describing its behavior
 * and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p><p>
 * Note: Currently, null can be passed in for the display argument.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the display argument is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param display the display to create the shell on
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 * 
 * @see SWT#BORDER
 * @see SWT#CLOSE
 * @see SWT#MIN
 * @see SWT#MAX
 * @see SWT#RESIZE
 * @see SWT#TITLE
 * @see SWT#NO_TRIM
 * @see SWT#SHELL_TRIM
 * @see SWT#DIALOG_TRIM
 * @see SWT#MODELESS
 * @see SWT#PRIMARY_MODAL
 * @see SWT#APPLICATION_MODAL
 * @see SWT#SYSTEM_MODAL
 */
public Shell (Display display, int style) {
	this (display, null, style, 0);
}

Shell (Display display, Shell parent, int style, int handle) {
	super ();
	checkSubclass ();
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = checkStyle (style);
	this.parent = parent;
	this.display = display;
	this.handle = handle;
	createWidget (0);
}

/**
 * Constructs a new instance of this class given only its
 * parent. It is created with style <code>SWT.DIALOG_TRIM</code>.
 * <p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell (Shell parent) {
	this (parent, SWT.DIALOG_TRIM);
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
 * </p><p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 * 
 * @see SWT#BORDER
 * @see SWT#CLOSE
 * @see SWT#MIN
 * @see SWT#MAX
 * @see SWT#RESIZE
 * @see SWT#TITLE
 * @see SWT#NO_TRIM
 * @see SWT#SHELL_TRIM
 * @see SWT#DIALOG_TRIM
 * @see SWT#MODELESS
 * @see SWT#PRIMARY_MODAL
 * @see SWT#APPLICATION_MODAL
 * @see SWT#SYSTEM_MODAL
 */
public Shell (Shell parent, int style) {
	this (parent != null ? parent.display : null, parent, style, 0);
}

public static Shell gtk_new (Display display, int handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle);
}

static int checkStyle (int style) {
	style = Decorations.checkStyle (style);
	if ((style & SWT.ON_TOP) != 0) style &= ~SWT.SHELL_TRIM;
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	int bits = style & ~mask;
	if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
	if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
	return bits;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when operations are performed on the receiver,
 * by sending the listener one of the messages defined in the
 * <code>ShellListener</code> interface.
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
 * @see ShellListener
 * @see #removeShellListener
 */
public void addShellListener (ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Close,typedListener);
	addListener (SWT.Iconify,typedListener);
	addListener (SWT.Deiconify,typedListener);
	addListener (SWT.Activate, typedListener);
	addListener (SWT.Deactivate, typedListener);
}

void adjustTrim () {
	int [] width = new int [1], height = new int [1];
	OS.gtk_window_get_size (shellHandle, width, height);
	int window = OS.GTK_WIDGET_WINDOW (shellHandle);
	GdkRectangle rect = new GdkRectangle ();
	OS.gdk_window_get_frame_extents (window, rect);
	int trimWidth = Math.max (0, rect.width - width [0]);
	int trimHeight = Math.max (0, rect.height - height [0]);
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	if ((style & SWT.NO_TRIM) == 0) {
		hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
		hasResize = (style & SWT.RESIZE) != 0;
		hasBorder = (style & SWT.BORDER) != 0;
	}
	if (hasTitle) {
		if (hasResize)  {
			display.titleResizeTrimWidth = trimWidth;
			display.titleResizeTrimHeight = trimHeight;
			return;
		}
		if (hasBorder) {
			display.titleBorderTrimWidth = trimWidth;
			display.titleBorderTrimHeight = trimHeight;
			return;
		}
		display.titleTrimWidth = trimWidth;
		display.titleTrimHeight = trimHeight;
		return;
	}
	if (hasResize) {
		display.resizeTrimWidth = trimWidth;
		display.resizeTrimHeight = trimHeight;
		return;
	}
	if (hasBorder) {
		display.borderTrimWidth = trimWidth;
		display.borderTrimHeight = trimHeight;
		return;
	}
}

void bringToTop (boolean force) {
	if (!OS.GTK_WIDGET_VISIBLE (shellHandle)) return; 
	if (hasFocus) return;
	Shell shell = display.getActiveShell ();
	if (!force) {
		if (shell == null) return;
		int focusHandle = OS.gtk_window_get_focus (shell.shellHandle);
		if (focusHandle != 0) {
			if (!OS.GTK_WIDGET_HAS_FOCUS (focusHandle)) return;
		}
	}
	if (shell != null) shell.hasFocus = false;
	/*
	* Feature on GTK.  
	*/
	int window = OS.GTK_WIDGET_WINDOW (shellHandle);
	if ((style & SWT.ON_TOP) != 0 && OS.GDK_WINDOWING_X11 ()) {
		int xDisplay = OS.gdk_x11_drawable_get_xdisplay (window);
		int xid = OS.gdk_x11_drawable_get_xid (window);
		OS.gdk_error_trap_push ();
		OS.XSetInputFocus (xDisplay, xid, OS.RevertToParent, OS.gtk_get_current_event_time ());
		OS.gdk_error_trap_pop ();
	} else {
		OS.gdk_window_focus (window, OS.gtk_get_current_event_time ());
	}
	hasFocus = true;
}

/**
 * Requests that the window manager close the receiver in
 * the same way it would be closed when the user clicks on
 * the "close box" or performs some other platform specific
 * key or mouse combination that indicates the window
 * should be removed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #dispose
 */
public void close () {
	checkWidget ();
	closeWidget ();
}
void closeWidget () {
	if (!isEnabled()) return;
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrim (x, y, width, height);
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	trim.x -= trimWidth / 2; trim.y -= trimHeight - (trimWidth / 2);
	trim.width += trimWidth; trim.height += trimHeight;
	if (menuBar != null) {
		int menuBarHeight = OS.GTK_WIDGET_HEIGHT (menuBar.handle);
		trim.y -= menuBarHeight;
		trim.height += menuBarHeight;
	}
	return trim;
}

void createHandle (int index) {
	state |= HANDLE | CANVAS;
	if (handle == 0) {
		int type = OS.GTK_WINDOW_TOPLEVEL;
		if ((style & SWT.ON_TOP) != 0) type = OS.GTK_WINDOW_POPUP;
		shellHandle = OS.gtk_window_new (type);
	} else {
		shellHandle = OS.gtk_plug_new (handle);
	}
	if (shellHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (parent != null) {
		OS.gtk_window_set_transient_for (shellHandle, parent.topHandle ());
		OS.gtk_window_set_destroy_with_parent (shellHandle, true);
	}
	/*
	* Feature in GTK.  The window size must be set when the window
	* is created or it will not be allowed to be resized smaller that the
	* initial size by the user.  The fix is to set the size to zero.
	*/
	OS.gtk_widget_set_size_request (shellHandle, 0, 0);
	OS.gtk_window_set_resizable (shellHandle, true);
	createHandle (index, shellHandle, true);
	OS.gtk_widget_realize (shellHandle);
	int window = OS.GTK_WIDGET_WINDOW (shellHandle);
	int decorations = 0;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.MIN) != 0) decorations |= OS.GDK_DECOR_MINIMIZE;
		if ((style & SWT.MAX) != 0) decorations |= OS.GDK_DECOR_MAXIMIZE;
		if ((style & SWT.RESIZE) != 0) decorations |= OS.GDK_DECOR_RESIZEH;
		if ((style & SWT.BORDER) != 0) decorations |= OS.GDK_DECOR_BORDER;
		if ((style & SWT.MENU) != 0) decorations |= OS.GDK_DECOR_MENU;
		if ((style & SWT.TITLE) != 0) decorations |= OS.GDK_DECOR_TITLE;
		/*
		* Feature in GTK.  Under some Window Managers (Sawmill), in order
		* to get any border at all from the window manager it is necessary to
		* set GDK_DECOR_BORDER.  The fix is to force these bits when any
		* kind of border is requested.
		*/
		if ((style & SWT.RESIZE) != 0) decorations |= OS.GDK_DECOR_BORDER;
	}
	OS.gdk_window_set_decorations (window, decorations);
	OS.gtk_window_set_title (shellHandle, new byte [1]);
	if ((style & SWT.ON_TOP) != 0) {
		OS.gdk_window_set_override_redirect (window, true);
	}
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.RESIZE)) == 0) {
		OS.gtk_container_set_border_width (shellHandle, 1);
		GdkColor color = new GdkColor ();
		OS.gtk_style_get_black (OS.gtk_widget_get_style (shellHandle), color);
		OS.gtk_widget_modify_bg (shellHandle,  OS.GTK_STATE_NORMAL, color);
	}
	int bits = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	boolean modal = (style & bits) != 0;
	//TEMPORARY CODE
	if ((style & SWT.ON_TOP) == 0) modal |= (parent != null && (parent.style & bits) != 0);
	OS.gtk_window_set_modal (shellHandle, modal);
}

boolean hasBorder () {
	return false;
}

void hookEvents () {
	super.hookEvents ();
	int shellMapProc = display.shellMapProc;
	int windowProc3 = display.windowProc3;
	OS.g_signal_connect (shellHandle, OS.map_event, windowProc3, MAP_EVENT);
	OS.g_signal_connect (shellHandle, OS.unmap_event, windowProc3, UNMAP_EVENT);
	OS.g_signal_connect (shellHandle, OS.window_state_event, windowProc3, WINDOW_STATE_EVENT);
	OS.g_signal_connect (shellHandle, OS.size_allocate, windowProc3, SIZE_ALLOCATE);
	OS.g_signal_connect (shellHandle, OS.configure_event, windowProc3, CONFIGURE_EVENT);
	OS.g_signal_connect (shellHandle, OS.delete_event, windowProc3, DELETE_EVENT);
	OS.g_signal_connect (shellHandle, OS.event_after, windowProc3, EVENT_AFTER);
	OS.g_signal_connect (shellHandle, OS.map_event, shellMapProc, 0);
}

public boolean isEnabled () {
	checkWidget ();
	return getEnabled ();
}

public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

void register () {
	super.register ();
	display.addWidget (shellHandle, this);
}

void releaseChild () {
	/* Do nothing */
}

int topHandle () {
	return shellHandle;
}

void fixShell (Shell newShell, Control control) {
	if (this == newShell) return;
	if (control == lastActive) setActiveControl (null);
	if (tooltipsHandle != 0) {
		setToolTipText (control.handle, null);
	}
	newShell.setToolTipText (control.handle, control.toolTipText);
}

public Point getLocation () {
	checkWidget ();
	int [] x = new int [1], y = new int [1];
	OS.gtk_window_get_position (shellHandle, x,y);
	return new Point (x [0], y [0]);
}

public Point getSize () {
	checkWidget ();
	int width = OS.GTK_WIDGET_WIDTH (scrolledHandle);
	int height = OS.GTK_WIDGET_HEIGHT (scrolledHandle);
	if (menuBar != null)  {
		int barHandle = menuBar.handle;
		height += OS.GTK_WIDGET_HEIGHT (barHandle);
	}
	return new Point (width + trimWidth (), height + trimHeight ());
}

/** 
 * Returns the region that defines the shape of the shell.
 *
 * @return the region that defines the shape of the shell
 *	
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 *
 */
public Region getRegion () {
	checkWidget ();
	return region;
}

/**
 * Returns the receiver's input method editor mode. This
 * will be the result of bitwise OR'ing together one or
 * more of the following constants defined in class
 * <code>SWT</code>:
 * <code>NONE</code>, <code>ROMAN</code>, <code>DBCS</code>, 
 * <code>PHONETIC</code>, <code>NATIVE</code>, <code>ALPHA</code>.
 *
 * @return the IME mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 */
public int getImeInputMode () {
	checkWidget();
	return SWT.NONE;
}

Shell _getShell () {
	return this;
}
/**
 * Returns an array containing all shells which are 
 * descendents of the receiver.
 * <p>
 * @return the dialog shells
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Shell [] getShells () {
	checkWidget();
	int count = 0;
	Shell [] shells = display.getShells ();
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.getParent ();
		} while (shell != null && shell != this);
		if (shell == this) count++;
	}
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.getParent ();
		} while (shell != null && shell != this);
		if (shell == this) {
			result [index++] = shells [i];
		}
	}
	return result;
}

int gtk_configure_event (int widget, int event) {
	int [] x = new int [1], y = new int [1];
	OS.gtk_window_get_position (shellHandle, x, y);
	if (oldX != x [0] || oldY != y [0]) {
		oldX = x [0];
		oldY = y [0];
		sendEvent (SWT.Move);
	}
	return 0;
}

int gtk_delete_event (int widget, int event) {
	closeWidget ();
	return 1;
}

int gtk_event_after (int widget, int event) {
	int result = super.gtk_event_after (widget, event);
	if (widget == shellHandle) {
		GdkEvent gdkEvent = new GdkEvent ();
		OS.memmove (gdkEvent, event, GdkEvent.sizeof);
		if (gdkEvent.type == OS.GDK_FOCUS_CHANGE) {
			GdkEventFocus focusEvent = new GdkEventFocus ();
			OS.memmove (focusEvent, event, GdkEventFocus.sizeof);
			hasFocus = focusEvent.in != 0;
			if (hasFocus) {
				postEvent (SWT.Activate);
				if (tooltipsHandle != 0) OS.gtk_tooltips_enable (tooltipsHandle);
			} else {
				postEvent (SWT.Deactivate);
				if (tooltipsHandle != 0) OS.gtk_tooltips_disable (tooltipsHandle);
			}
		}
	}
	return result;
}

int gtk_map_event (int widget, int event) {
	minimized = false;
	sendEvent (SWT.Deiconify);
	return 0;
}

int gtk_size_allocate (int widget, int allocation) {
	int [] width = new int [1], height = new int [1];
	OS.gtk_window_get_size (shellHandle, width, height);
	if (oldWidth != width [0] || oldHeight != height [0]) {
		oldWidth = width [0];
		oldHeight = height [0];
		resizeBounds (width [0], height [0], true);
	}
	return 0;
}

int gtk_unmap_event (int widget, int event) {
	minimized = true;
	sendEvent (SWT.Iconify);
	return 0;
}

int gtk_window_state_event (int widget, int event) {
	GdkEventWindowState gdkEvent = new GdkEventWindowState ();
	OS.memmove (gdkEvent, event, GdkEventWindowState.sizeof);
	minimized = (gdkEvent.new_window_state & OS.GDK_WINDOW_STATE_ICONIFIED) != 0;
	maximized = (gdkEvent.new_window_state & OS.GDK_WINDOW_STATE_MAXIMIZED) != 0;
	return 0;
}

/**
 * Moves the receiver to the top of the drawing order for
 * the display on which it was created (so that all other
 * shells on that display, which are not the receiver's
 * children will be drawn behind it), marks it visible,
 * and sets focus to its default button (if it has one)
 * and asks the window manager to make the shell active.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Control#moveAbove
 * @see Control#setFocus
 * @see Control#setVisible
 * @see Display#getActiveShell
 * @see Decorations#setDefaultButton
 * @see Shell#setActive
 * @see Shell#forceActive
 */
public void open () {
	checkWidget ();
	setVisible (true);
	bringToTop (false);
	if (!restoreFocus ()) {
		int focusHandle = OS.gtk_window_get_focus (shellHandle);
		if (focusHandle == 0 || focusHandle == handle) traverseGroup (true);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when operations are performed on the receiver.
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
 * @see ShellListener
 * @see #addShellListener
 */
public void removeShellListener (ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Close, listener);
	eventTable.unhook (SWT.Iconify,listener);
	eventTable.unhook (SWT.Deiconify,listener);
	eventTable.unhook (SWT.Activate, listener);
	eventTable.unhook (SWT.Deactivate, listener);
}

/**
 * Moves the receiver to the top of the drawing order for
 * the display on which it was created (so that all other
 * shells on that display, which are not the receiver's
 * children will be drawn behind it) and asks the window
 * manager to make the shell active.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.0
 * @see Control#moveAbove
 * @see Control#setFocus
 * @see Control#setVisible
 * @see Display#getActiveShell
 * @see Decorations#setDefaultButton
 * @see Shell#open
 * @see Shell#setActive
 */
public void setActive () {
	checkWidget ();
	bringToTop (false);
}

void setActiveControl (Control control) {
	if (control != null && control.isDisposed ()) control = null;
	if (lastActive != null && lastActive.isDisposed ()) lastActive = null;
	if (lastActive == control) return;
	
	/*
	* Compute the list of controls to be activated and
	* deactivated by finding the first common parent
	* control.
	*/
	Control [] activate = (control == null) ? new Control[0] : control.getPath ();
	Control [] deactivate = (lastActive == null) ? new Control[0] : lastActive.getPath ();
	lastActive = control;
	int index = 0, length = Math.min (activate.length, deactivate.length);
	while (index < length) {
		if (activate [index] != deactivate [index]) break;
		index++;
	}
	
	/*
	* It is possible (but unlikely), that application
	* code could have destroyed some of the widgets. If
	* this happens, keep processing those widgets that
	* are not disposed.
	*/
	for (int i=deactivate.length-1; i>=index; --i) {
		if (!deactivate [i].isDisposed ()) {
			deactivate [i].sendEvent (SWT.Deactivate);
		}
	}
	for (int i=activate.length-1; i>=index; --i) {
		if (!activate [i].isDisposed ()) {
			activate [i].sendEvent (SWT.Activate);
		}
	}
}

void resizeBounds (int width, int height, boolean notify) {
	int menuHeight = 0;
	if (menuBar != null) {
		int menuHandle = menuBar.handle;
		OS.gtk_widget_set_size_request (menuHandle, -1, -1);
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (menuHandle, requisition);
		menuHeight = requisition.height;
		OS.gtk_widget_set_size_request (menuHandle, width, menuHeight);
		height = Math.max (1, height - menuHeight);
	}
	OS.gtk_fixed_move (fixedHandle, scrolledHandle, 0, menuHeight);
	OS.gtk_widget_set_size_request (scrolledHandle, width, height);
	OS.gtk_container_resize_children (fixedHandle);
	if (notify) {
		sendEvent (SWT.Resize);
		if (layout != null) layout.layout (this, false);
	}
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	if (move) {
		int [] x_pos = new int [1], y_pos = new int [1];
		OS.gtk_window_get_position (shellHandle, x_pos, y_pos);
		oldX = x_pos [0];  oldY = y_pos [0];
		OS.gtk_window_move (shellHandle, x, y);
	}
	if (resize) {
		int [] w = new int [1], h = new int [1];
		OS.gtk_window_get_size (shellHandle, w, h);
		oldWidth = w [0];  oldHeight = h [0];
		width = Math.max (1, width - trimWidth ());
		height = Math.max (1, height - trimHeight ());
		OS.gtk_window_resize (shellHandle, width, height);
		resizeBounds (width, height, true);
	}
	return move || resize;
}

/**
 * Sets the input method editor mode to the argument which 
 * should be the result of bitwise OR'ing together one or more
 * of the following constants defined in class <code>SWT</code>:
 * <code>NONE</code>, <code>ROMAN</code>, <code>DBCS</code>, 
 * <code>PHONETIC</code>, <code>NATIVE</code>, <code>ALPHA</code>.
 *
 * @param mode the new IME mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 */
public void setImeInputMode (int mode) {
	checkWidget();
}

void setInitialSize () {
	int width  = OS.gdk_screen_width () * 5 / 8;
	int height = OS.gdk_screen_height () * 5 / 8;
	OS.gtk_widget_set_size_request (scrolledHandle, width, height);
	OS.gtk_window_resize (shellHandle, width, height);
	OS.gtk_container_resize_children (fixedHandle);
}

public void setMaximized (boolean maximized) {
	checkWidget();
	super.setMaximized (maximized);
	if (maximized) {
		OS.gtk_window_maximize (shellHandle);
	} else {
		OS.gtk_window_unmaximize (shellHandle);
	}
}

public void setMenuBar (Menu menu) {
	checkWidget();
	if (menuBar == menu) return;
	boolean both = menu != null && menuBar != null;
	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}
	if (menuBar != null) {
		int menuHandle = menuBar.handle;
		OS.gtk_widget_hide (menuHandle);
		destroyAccelGroup ();
	}
	menuBar = menu;
	if (menuBar != null) {
		int menuHandle = menu.handle;
		OS.gtk_widget_show (menuHandle);
		createAccelGroup ();
		menuBar.addAccelerators (accelGroup);
	}
	int [] width = new int [1], height = new int [1];
	OS.gtk_window_get_size (shellHandle, width, height);
	resizeBounds (width [0], height [0], !both);
}

public void setMinimized (boolean minimized) {
	checkWidget();
	if (this.minimized == minimized) return;
	super.setMinimized (minimized);
	if (minimized) {
		OS.gtk_window_iconify (shellHandle);
	} else {
		OS.gtk_window_deiconify (shellHandle);
		bringToTop (false);
	}
}

/**
 * Sets the shape of the shell to the region specified
 * by the argument.  A null region will restore the default shape.
 * Shell must be created with the style SWT.NO_TRIM.
 *
 * @param rgn the region that defines the shape of the shell
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 *
 */
public void setRegion (Region region) {
	checkWidget ();
	if ((style & SWT.NO_TRIM) == 0) return;
	int window = OS.GTK_WIDGET_WINDOW (shellHandle);
	int shape_region = (region == null) ? 0 : region.handle;
	OS.gdk_window_shape_combine_region (window, shape_region, 0, 0);
	this.region = region;
}

public void setText (String string) {
	super.setText (string);

	/* 
	* GTK bug 82013.  For some reason, if the title string
	* is less that 7 bytes long and is not terminated by
	* a space, some window managers occasionally draw
	* garbage after the last character in  the title.
	* The fix is to pad the title.
	*/
	int length = string.length ();
	char [] chars = new char [Math.max (6, length) + 1];
	string.getChars (0, length , chars, 0);
	for (int i=length; i<chars.length; i++)  chars [i] = ' ';
	byte [] buffer = Converter.wcsToMbcs (null, chars, true);
	OS.gtk_window_set_title (shellHandle, buffer);
}

public void setVisible (boolean visible) {
	checkWidget();
	if ((OS.GTK_WIDGET_MAPPED (shellHandle) == visible)) return;
	if (visible) {
		sendEvent (SWT.Show);
		// widget could be disposed at this point
		if (isDisposed ()) return;
		mapped = false;
		OS.gtk_widget_show (shellHandle);
		while (!isDisposed () && !mapped) {
			OS.gtk_main_iteration ();
		}
		// widget could be disposed at this point
		if (isDisposed ()) return;
		adjustTrim ();
		int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
		if ((style & mask) != 0) {
			OS.gdk_pointer_ungrab (OS.GDK_CURRENT_TIME);
		}
	} else {	
		OS.gtk_widget_hide (shellHandle);
		sendEvent (SWT.Hide);
	}
}

void setZOrder (Control sibling, boolean above) {
	 setZOrder (sibling, above, false);
}

int shellMapProc (int handle, int arg0, int user_data) {
	mapped = true;
	return 0;
}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}
int trimHeight () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.titleResizeTrimHeight;
		if (hasBorder) return display.titleBorderTrimHeight;
		return display.titleTrimHeight;
	}
	if (hasResize) return display.resizeTrimHeight;
	if (hasBorder) return display.borderTrimHeight;
	return 0;
}

int trimWidth () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.titleResizeTrimWidth;
		if (hasBorder) return display.titleBorderTrimWidth;
		return display.titleTrimWidth;
	}
	if (hasResize) return display.resizeTrimWidth;
	if (hasBorder) return display.borderTrimWidth;
	return 0;
}

void deregister () {
	super.deregister ();
	display.removeWidget (shellHandle);
}

public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (isDisposed()) return;

	/*
	* Feature in GTK.  When the active shell is disposed,
	* GTK assigns focus temporarily to the root window
	* unless it has previously been told to do otherwise.
	* The fix is to make the parent be the active top level
	* shell when the child shell is disposed.
	*/
	OS.gtk_widget_hide (shellHandle);
	if (parent != null) {
		Shell activeShell = display.getActiveShell ();
		if (activeShell == this) {
			Shell shell = parent.getShell ();	
			shell.bringToTop (false);
		}
	}
	super.dispose ();
}

/**
 * Moves the receiver to the top of the drawing order for
 * the display on which it was created (so that all other
 * shells on that display, which are not the receiver's
 * children will be drawn behind it) and forces the window
 * manager to make the shell active.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.0
 * @see Control#moveAbove
 * @see Control#setFocus
 * @see Control#setVisible
 * @see Display#getActiveShell
 * @see Decorations#setDefaultButton
 * @see Shell#open
 * @see Shell#setActive
 */
public void forceActive () {
	checkWidget ();
	bringToTop (true);
}

public Rectangle getBounds () {
	checkWidget ();
	int [] x = new int [1], y = new int [1];
	OS.gtk_window_get_position (shellHandle, x, y);
	int width = OS.GTK_WIDGET_WIDTH (scrolledHandle);
	int height = OS.GTK_WIDGET_HEIGHT (scrolledHandle);
	if (menuBar != null)  {
		int barHandle = menuBar.handle;
		height += OS.GTK_WIDGET_HEIGHT (barHandle);
	}
	return new Rectangle (x [0], y [0], width + trimWidth (), height + trimHeight ());
}

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

void releaseShells () {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.releaseResources ();
	}
}

void releaseWidget () {
	releaseShells ();
	destroyAccelGroup ();
	super.releaseWidget ();
	if (tooltipsHandle != 0) OS.g_object_unref (tooltipsHandle);
	tooltipsHandle = 0;
	region = null;
	lastActive = null;
}

void setToolTipText (int widget, String string) {
	byte [] buffer = null;
	if (string != null && string.length () > 0) {
		buffer = Converter.wcsToMbcs (null, string, true);
	}
	if (tooltipsHandle == 0) {
		tooltipsHandle = OS.gtk_tooltips_new ();
		if (tooltipsHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref (tooltipsHandle);
		OS.gtk_object_sink (tooltipsHandle);
	}
	OS.gtk_tooltips_set_tip (tooltipsHandle, widget, buffer, null);
}
}

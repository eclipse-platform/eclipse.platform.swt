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


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
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
	int shellHandle;
	Menu activeMenu;
	int blockedList;
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
	this (display, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE);
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
	this (parent, SWT.TITLE | SWT.CLOSE | SWT.BORDER);
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

public static Shell photon_new (Display display, int handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle);
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

void bringToTop (boolean force) {
	if (!force) {
		if (display.getActiveShell () == null) return;
	}
	OS.PtWindowToFront (shellHandle);
}

static int checkStyle (int style) {
	style = Decorations.checkStyle (style);
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	int bits = style & ~mask;
	if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
	if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
	return bits;
}

void closeWidget () {
	Event event = new Event ();
	event.time = (int) System.currentTimeMillis ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
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
	checkWidget();
	closeWidget ();
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int [] args = {OS.Pt_ARG_WINDOW_RENDER_FLAGS, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int flags = args [1];
	int [] left = new int [1], top = new int [1];
	int [] right = new int [1], bottom = new int [1];
	OS.PtFrameSize (flags, 0, left, top, right, bottom);
	int trimX = x - left [0];
	int trimY = y - top [0];
	int trimWidth = width + left [0] + right [0];
	int trimHeight = height + top [0] + bottom [0];
	if (menuBar != null) {
		PhDim_t dim = new PhDim_t ();
		int menuHandle = menuBar.handle;
		if (!OS.PtWidgetIsRealized (menuHandle)) {
			OS.PtExtentWidgetFamily (menuHandle);
		}
		OS.PtWidgetPreferredSize (menuHandle, dim);
		trimHeight += dim.h;
		trimY -= dim.h;
	}
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}

void createHandle (int index) {
	state |= HANDLE | GRAB | CANVAS;
	if (handle != 0) {
		int clazz = display.PtContainer;
		int [] args = {
			OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
			OS.Pt_ARG_CONTAINER_FLAGS, OS.Pt_HOTKEYS_FIRST, OS.Pt_HOTKEYS_FIRST,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		shellHandle = OS.PtCreateWidget (clazz, handle, args.length / 3, args);
		if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		int parentHandle = 0;
		if (parent != null) parentHandle = parent.topHandle ();
		PhRect_t rect = new PhRect_t ();
		OS.PhWindowQueryVisible (OS.Ph_QUERY_GRAPHICS, 0, OS.PhInputGroup (0), rect);
		int width = (short) ((rect.lr_x - rect.ul_x + 1) * 5 / 8);
		int height = (short) ((rect.lr_y - rect.ul_y + 1) * 5 / 8);
		int decorations = 0;
		int flags =
			OS.Ph_WM_RENDER_MIN | OS.Ph_WM_RENDER_MAX | OS.Ph_WM_RENDER_RESIZE |
			OS.Ph_WM_RENDER_BORDER | OS.Ph_WM_RENDER_MENU | OS.Ph_WM_RENDER_MIN |
			OS.Ph_WM_RENDER_TITLE;
		if ((style & SWT.NO_TRIM) == 0) {
			if ((style & SWT.MIN) != 0) decorations |= OS.Ph_WM_RENDER_MIN;
			if ((style & SWT.MAX) != 0) decorations |= OS.Ph_WM_RENDER_MAX;
			if ((style & SWT.RESIZE) != 0) {
				decorations |= OS.Ph_WM_RENDER_BORDER | OS.Ph_WM_RENDER_RESIZE;
			}
			if ((style & SWT.BORDER) != 0) decorations |= OS.Ph_WM_RENDER_BORDER;
			if ((style & SWT.MENU) != 0) decorations |= OS.Ph_WM_RENDER_MENU;
			if ((style & SWT.TITLE) != 0) decorations |= OS.Ph_WM_RENDER_TITLE;
		}
		int notifyFlags =
			OS.Ph_WM_ICON | OS.Ph_WM_FOCUS |
			OS.Ph_WM_MOVE | OS.Ph_WM_RESIZE;
		int windowState = OS.Ph_WM_STATE_ISFOCUS;
		if ((style & SWT.ON_TOP) != 0) windowState = OS.Ph_WM_STATE_ISFRONT;
		int titlePtr = OS.malloc (1);
		OS.memset(titlePtr, 0, 1);
		int [] args = {
			OS.Pt_ARG_WIDTH, width, 0,
			OS.Pt_ARG_HEIGHT, height, 0,
			OS.Pt_ARG_WINDOW_TITLE, titlePtr, 0,
			OS.Pt_ARG_WINDOW_RENDER_FLAGS, decorations, flags,
			OS.Pt_ARG_WINDOW_MANAGED_FLAGS, 0, OS.Ph_WM_CLOSE,
			OS.Pt_ARG_WINDOW_NOTIFY_FLAGS, notifyFlags, notifyFlags,
			OS.Pt_ARG_WINDOW_STATE, windowState, ~0,
			OS.Pt_ARG_FLAGS, OS.Pt_DELAY_REALIZE, OS.Pt_DELAY_REALIZE,
			OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
			OS.Pt_ARG_CONTAINER_FLAGS, OS.Pt_HOTKEYS_FIRST, OS.Pt_HOTKEYS_FIRST,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		OS.PtSetParentWidget (parentHandle);
		shellHandle = OS.PtCreateWidget (OS.PtWindow (), parentHandle, args.length / 3, args);
		OS.free (titlePtr);
		if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	createScrolledHandle (shellHandle);
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.RESIZE)) == 0) {
		int [] args = {
			OS.Pt_ARG_FLAGS, OS.Pt_HIGHLIGHTED, OS.Pt_HIGHLIGHTED,
			OS.Pt_ARG_BASIC_FLAGS, OS.Pt_ALL_OUTLINES, ~0,
		};
		OS.PtSetResources (scrolledHandle, args.length / 3, args);
	}
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	resizeBounds (args [1], args [4]);
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (shellHandle);
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
	checkWidget();
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (shellHandle, area);
	int width = area.size_w, height = area.size_h;
	int [] args = {OS.Pt_ARG_WINDOW_RENDER_FLAGS, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int flags = args [1];
	int [] left = new int [1], top = new int [1];
	int [] right = new int [1], bottom = new int [1];
	OS.PtFrameSize (flags, 0, left, top, right, bottom);
	width += left [0] + right [0];
	height += top [0] + bottom [0];
	return new Rectangle (area.pos_x, area.pos_y, width, height);
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
	return 0;
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}

public Point getLocation () {
	checkWidget();
	//NOT DONE - shell location is 0,0 when queried before event loop
	return super.getLocation ();
}

public boolean getMaximized () {
	checkWidget();
	int state = OS.PtWindowGetState (shellHandle);
	if (state != -1) return (state & (OS.Ph_WM_STATE_ISMAX | OS.Ph_WM_STATE_ISMAXING)) != 0;
	int [] args = {OS.Pt_ARG_WINDOW_STATE, 0, OS.Ph_WM_STATE_ISMAX};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	return (args [1] & OS.Ph_WM_STATE_ISMAX) != 0;
}

public boolean getMinimized () {
	checkWidget();
	int state = OS.PtWindowGetState (shellHandle);
	if (state != -1) return (state & OS.Ph_WM_STATE_ISICONIFIED) != 0;
	int [] args = {OS.Pt_ARG_WINDOW_STATE, 0, OS.Ph_WM_STATE_ISICONIFIED};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	return (args [1] & OS.Ph_WM_STATE_ISICONIFIED) != 0;
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

public Shell getShell () {
	checkWidget();
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
			shell = shell.parent;
		} while (shell != null && shell != this);
		if (shell == this) count++;
	}
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.parent;
		} while (shell != null && shell != this);
		if (shell == this) {
			result [index++] = shells [i];
		}
	}
	return result;
}

public Point getSize () {
	checkWidget();
	int [] args = {
		OS.Pt_ARG_WINDOW_RENDER_FLAGS, 0, 0,
		OS.Pt_ARG_WIDTH, 0, 0,
		OS.Pt_ARG_HEIGHT, 0, 0,
	};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int flags = args [1];
	int [] left = new int [1], top = new int [1];
	int [] right = new int [1], bottom = new int [1];
	OS.PtFrameSize (flags, 0, left, top, right, bottom);
	int width = args [4] + left [0] + right [0];
	int height = args [7] + top [0] + bottom [0];
	return new Point (width, height);
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	OS.PtAddCallback (shellHandle, OS.Pt_CB_WINDOW, windowProc, OS.Pt_CB_WINDOW);
	OS.PtAddCallback (shellHandle, OS.Pt_CB_RESIZE, windowProc, OS.Pt_CB_RESIZE);
}

int hotkeyProc (int w, int data, int info) {
	if (data != 0) {
		Widget widget = WidgetTable.get (data);
		if (widget instanceof MenuItem) {
			MenuItem item = (MenuItem) widget;
			if (item.isEnabled ()) item.Pt_CB_ACTIVATE (data, info);
		}
	}
	return OS.Pt_CONTINUE;
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
	checkWidget();
	setVisible (true);
	traverseGroup (true);
}

int Pt_CB_RESIZE (int widget, int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	resizeBounds (args [1], args [4]);
	sendEvent(SWT.Resize);
	if (layout != null) layout (false);
	return OS.Pt_CONTINUE;
}

int Pt_CB_WINDOW (int widget, int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	PhWindowEvent_t we = new PhWindowEvent_t ();
	OS.memmove (we, cbinfo.cbdata, PhWindowEvent_t.sizeof);
	switch (we.event_f) {
		case OS.Ph_WM_CLOSE:
			closeWidget ();
			break;
		case OS.Ph_WM_ICON:
			if ((we.state_f & OS.Ph_WM_STATE_ISICONIFIED) != 0) {
				sendEvent (SWT.Iconify);
			} else {
				sendEvent (SWT.Deiconify);
			}
			break;
		case OS.Ph_WM_FOCUS:
			switch ((int) we.event_state) {
				case OS.Ph_WM_EVSTATE_FOCUS: sendEvent (SWT.Activate); break;
				case OS.Ph_WM_EVSTATE_FOCUSLOST: sendEvent (SWT.Deactivate); break;
			}
			break;
		case OS.Ph_WM_MOVE:
			sendEvent (SWT.Move);
			break;
	}
	return OS.Pt_CONTINUE;
}

void register () {
	super.register ();
	WidgetTable.put (shellHandle, this);
}

void realizeWidget() {
	/* Do nothing */
}

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

void releaseShells () {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			/*
			* Feature in Photon.  A shell may have child shells that have been
			* temporarily reparented to NULL because they were shown without
			* showing the parent.  In this case, Photon will not destroy the
			* child shells because they are not in the widget hierarchy.
			* The fix is to detect this case and destroy the shells.
			*/
			if (shell.parent != null && OS.PtWidgetParent (shell.shellHandle) == 0) {
				shell.dispose ();
			} else {
				shell.releaseResources ();
			}
		}
	}
}

void releaseWidget () {
	releaseShells ();
	super.releaseWidget ();
	if (blockedList != 0) OS.PtUnblockWindows (blockedList);
	blockedList = 0;
	lastActive = null;
	region = null;
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

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	checkWidget();
	if (OS.PtWidgetClass (shellHandle) != OS.PtWindow ()) {
		int result = super.setBounds (x, y, width, height, move, resize, events);
		if ((result & RESIZED) != 0) resizeBounds (width, height);
		return result;
	}
	
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	
	if (resize) {
		/* Get the trimings */
		int [] args = {OS.Pt_ARG_WINDOW_RENDER_FLAGS, 0, 0};
		OS.PtGetResources (shellHandle, args.length / 3, args);
		int flags = args [1];
		int [] left = new int [1], top = new int [1];
		int [] right = new int [1], bottom = new int [1];
		OS.PtFrameSize (flags, 0, left, top, right, bottom);
		width = Math.max (width - left [0] - right [0], 0);
		height = Math.max (height - top [0] - bottom [0], 0);
	}
	
	PhArea_t oldArea = new PhArea_t ();
	OS.PtWidgetArea (shellHandle, oldArea);
	
	if (move && resize) {
		PhArea_t area = new PhArea_t ();
		area.pos_x = (short) x;
		area.pos_y = (short) y;
		area.size_w = (short) width;
		area.size_h = (short) height;
		int ptr = OS.malloc (PhArea_t.sizeof);
		OS.memmove (ptr, area, PhArea_t.sizeof);
		OS.PtSetResource (shellHandle, OS.Pt_ARG_AREA, ptr, 0);
		OS.free (ptr);
	} else {
		if (move) {
			PhPoint_t pt = new PhPoint_t ();
			pt.x = (short) x;
			pt.y = (short) y;
			int ptr = OS.malloc (PhPoint_t.sizeof);
			OS.memmove (ptr, pt, PhPoint_t.sizeof);
			OS.PtSetResource (shellHandle, OS.Pt_ARG_POS, ptr, 0);
			OS.free (ptr);
		} else if (resize) {
			int [] args = {
				OS.Pt_ARG_WIDTH, width, 0,
				OS.Pt_ARG_HEIGHT, height, 0,
			};
			OS.PtSetResources (shellHandle, args.length / 3, args);
		}
	}

	/*
	* Feature in Photon.  The shell does not issue WM_SIZE
	* event notificatoin until it is realized.  The fix is
	* to detect size changes and send the events.
	*/
	if (!OS.PtWidgetIsRealized (shellHandle)) {
		PhArea_t newArea = new PhArea_t ();
		OS.PtWidgetArea (shellHandle, newArea);
		boolean sameOrigin = oldArea.pos_x == newArea.pos_x && oldArea.pos_y == newArea.pos_y;
		boolean sameExtent = oldArea.size_w == newArea.size_w && oldArea.size_h == newArea.size_h;
		if (!sameOrigin & move) sendEvent (SWT.Move);
		if (!sameExtent & resize) {
			resizeBounds (newArea.size_w, newArea.size_h);
			sendEvent(SWT.Resize);
			if (layout != null) layout (false);
		}
	}
	
	if (isFocus) caret.setFocus ();
	
	/* Always return 0 */
	return 0;
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

public void setMaximized (boolean maximized) {
	checkWidget();
	int bits = 0;
	if (maximized) bits = OS.Ph_WM_STATE_ISMAX;
	OS.PtSetResource (shellHandle, OS.Pt_ARG_WINDOW_STATE, bits, OS.Ph_WM_STATE_ISMAX);
	if (OS.PtWidgetIsRealized (shellHandle)) {
		PhWindowEvent_t event = new PhWindowEvent_t ();
		event.rid = OS.PtWidgetRid (shellHandle);
		event.event_f = maximized ? OS.Ph_WM_MAX : OS.Ph_WM_RESTORE;
		OS.PtForwardWindowEvent (event);
	}
}

public void setMenuBar (Menu menu) {
	checkWidget();
	if (menuBar == menu) return;
	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}
	if (menuBar != null) {
		int menuHandle = menuBar.handle;
		OS.PtSetResource (menuHandle, OS.Pt_ARG_FLAGS, OS.Pt_DELAY_REALIZE, OS.Pt_DELAY_REALIZE);
		OS.PtUnrealizeWidget (menuBar.handle);
	}
	menuBar = menu;
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int width = args [1], height = args [4];
	if (menuBar != null) {
		int menuHandle = menu.handle;
		args = new int [] {
			OS.Pt_ARG_WIDTH, width, 0,
			OS.Pt_ARG_FLAGS, 0, OS.Pt_DELAY_REALIZE,
		};
		OS.PtSetResources (menuHandle, args.length / 3, args);	
		OS.PtRealizeWidget (menuHandle);
	}
	resizeBounds (width, height);
}

public void setMinimized (boolean minimized) {
	checkWidget();
	int bits = 0;
	if (minimized) bits = OS.Ph_WM_STATE_ISICONIFIED;
	OS.PtSetResource (shellHandle, OS.Pt_ARG_WINDOW_STATE, bits, OS.Ph_WM_STATE_ISICONIFIED);
	if (OS.PtWidgetIsRealized (shellHandle)) {
		PhWindowEvent_t event = new PhWindowEvent_t ();
		event.rid = OS.PtWidgetRid (shellHandle);
		event.event_f = OS.Ph_WM_HIDE;
		event.event_state = (short) (minimized ? OS.Ph_WM_EVSTATE_HIDE : OS.Ph_WM_EVSTATE_UNHIDE);
		OS.PtForwardWindowEvent (event);
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
	// TODO implement setRegion
	this.region = region;
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.PtSetResource (shellHandle, OS.Pt_ARG_WINDOW_TITLE, ptr, 0);
	OS.free (ptr);
}

public void setVisible (boolean visible) {
	checkWidget();
	if (visible == OS.PtWidgetIsRealized (shellHandle)) return;

	/*
	* Feature in Photon.  It is not possible to show a PtWindow
	* whose parent is not realized.  The fix is to temporarily
	* reparent the child shell to NULL and then realize the child
	* shell.
	*/
	if (parent != null) {
		Shell shell = parent.getShell ();
		int parentHandle = shell.shellHandle;
		if (!OS.PtWidgetIsRealized (parentHandle)) {
			OS.PtReParentWidget (shellHandle, visible ? OS.Pt_NO_PARENT : parentHandle);
		}
	}
	
	if (visible) {
		int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
		switch (style & mask) {
			case SWT.PRIMARY_MODAL:
				if (parent != null) {
					int parentHandle = parent.getShell ().shellHandle;
					blockedList = OS.PtBlockWindow (parentHandle, (short) 0, 0);
				}
				break;
			case SWT.APPLICATION_MODAL:
			case SWT.SYSTEM_MODAL:
				blockedList = OS.PtBlockAllWindows (shellHandle, (short) 0, 0);
				break;
		}
	} else {
		if (blockedList != 0) OS.PtUnblockWindows (blockedList);
		blockedList = 0;
	}

	int flags = visible ? 0 : OS.Pt_DELAY_REALIZE;
	OS.PtSetResource (shellHandle, OS.Pt_ARG_FLAGS, flags, OS.Pt_DELAY_REALIZE);
	if (visible) {
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		OS.PtRealizeWidget (shellHandle);
	} else {
		OS.PtUnrealizeWidget (shellHandle);
		sendEvent(SWT.Hide);
		if (isDisposed ()) return;
	}

	/*
	* Feature in Photon.  When a shell is shown, it may have child
	* shells that have been temporarily reparented to NULL because
	* the child was shown before the parent.  The fix is to reparent
	* the child shells back to the correct parent.
	*/
	if (visible) {
		Shell [] shells = getShells ();
		for (int i=0; i<shells.length; i++) {
			int childHandle = shells [i].shellHandle;
			if (OS.PtWidgetParent (childHandle) == 0) {
				OS.PtReParentWidget (childHandle, shellHandle);
			}
		}
	}

	OS.PtSyncWidget (shellHandle);
	OS.PtFlush ();
}

int topHandle () {
	return shellHandle;
}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}

}

/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
import org.eclipse.swt.internal.win32.*;
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
 * </p><p>
 * The <em>modality</em> of an instance may be specified using
 * style bits. The modality style bits are used to determine
 * whether input is blocked for other shells on the display.
 * The <code>PRIMARY_MODAL</code> style allows an instance to block
 * input to its parent. The <code>APPLICATION_MODAL</code> style
 * allows an instance to block input to every other shell in the
 * display. The <code>SYSTEM_MODAL</code> style allows an instance
 * to block input to all shells, including shells belonging to
 * different applications.
 * </p><p>
 * Note: The styles supported by this class are treated
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
 * A modality style may also be "downgraded" to a less
 * restrictive style. For example, most operating systems
 * no longer support <code>SYSTEM_MODAL</code> because
 * it can freeze up the desktop, so this is typically
 * downgraded to <code>APPLICATION_MODAL</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL, SHEET</dd>
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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see Decorations
 * @see SWT
 * @see <a href="http://www.eclipse.org/swt/snippets/#shell">Shell snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Shell extends Decorations {
	Control lastActive;
	boolean closing;
	boolean moved, resized, opened, fullScreen, modified, center;
	int oldX, oldY, oldWidth, oldHeight;
	int oldWindowStyle, oldWindowState;
	
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
 * @see SWT#TOOL
 * @see SWT#NO_TRIM
 * @see SWT#SHELL_TRIM
 * @see SWT#DIALOG_TRIM
 * @see SWT#ON_TOP
 * @see SWT#MODELESS
 * @see SWT#PRIMARY_MODAL
 * @see SWT#APPLICATION_MODAL
 * @see SWT#SYSTEM_MODAL
 * @see SWT#SHEET
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
 * @see SWT#TOOL
 * @see SWT#NO_TRIM
 * @see SWT#SHELL_TRIM
 * @see SWT#DIALOG_TRIM
 * @see SWT#ON_TOP
 * @see SWT#MODELESS
 * @see SWT#PRIMARY_MODAL
 * @see SWT#APPLICATION_MODAL
 * @see SWT#SYSTEM_MODAL
 * @see SWT#SHEET
 */
public Shell (Display display, int style) {
	this (display, null, style, 0, false);
}

Shell (Display display, Shell parent, int style, int handle, boolean embedded) {
	super ();
	checkSubclass ();
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	if (parent != null && parent.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);	
	}
	this.center = parent != null && (style & SWT.SHEET) != 0;
	this.style = checkStyle (parent, style);
	this.parent = parent;
	this.display = display;
	if (handle != 0) {
		if (embedded) {
			this.handle = handle;
		} else {
			shellHandle = handle;
		}
	}
	reskinWidget();
	createWidget ();
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
 *    <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li> 
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li> 
 * </ul>
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
 * @see SWT#ON_TOP
 * @see SWT#TOOL
 * @see SWT#MODELESS
 * @see SWT#PRIMARY_MODAL
 * @see SWT#APPLICATION_MODAL
 * @see SWT#SYSTEM_MODAL
 * @see SWT#SHEET
 */
public Shell (Shell parent, int style) {
	this (parent != null ? parent.display : null, parent, style, 0, false);
}

/**	 
 * Invokes platform specific functionality to allocate a new shell.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Shell</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param display the display for the shell
 * @param handle the handle for the shell
 * @return a new shell object containing the specified display and handle
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public static Shell wpf_new (Display display, int handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle, true);
}

/**	 
 * Invokes platform specific functionality to allocate a new shell
 * that is not embedded.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Shell</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param display the display for the shell
 * @param handle the handle for the shell
 * @return a new shell object containing the specified display and handle
 * 
 * @noreference This method is not intended to be referenced by clients.
 * 
 * @since 3.3
 */
public static Shell internal_new (Display display, int handle) {
	handle = OS.GCHandle_ToHandle (handle);
	return new Shell (display, null, SWT.NO_TRIM, handle, false);
}

static int checkStyle (Shell parent, int style) {
	style = Decorations.checkStyle (style);
	style &= ~SWT.TRANSPARENT;
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	if ((style & SWT.SHEET) != 0) {
		style &= ~SWT.SHEET;
		style |= parent == null ? SWT.SHELL_TRIM : SWT.DIALOG_TRIM;
		if ((style & mask) == 0) {
			style |= parent == null ? SWT.APPLICATION_MODAL : SWT.PRIMARY_MODAL;
		}
	}
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Close,typedListener);
	addListener (SWT.Iconify,typedListener);
	addListener (SWT.Deiconify,typedListener);
	addListener (SWT.Activate, typedListener);
	addListener (SWT.Deactivate, typedListener);
}

void addWidget () {
	if ((style & SWT.ON_TOP) != 0) return;
	if (parent != null) {
		if ((parent.style & SWT.ON_TOP) != 0) return;
		OS.Window_Owner (shellHandle, ((Shell)parent).shellHandle);
	}
}

void bringToTop () {
	if ((style & SWT.ON_TOP) != 0) return;
	OS.Window_Activate (shellHandle);
}

void center () {
	if (parent == null) return;
	Rectangle rect = getBounds ();
	Rectangle parentRect = display.map (parent, null, parent.getClientArea());
	int x = Math.max (parentRect.x, parentRect.x + (parentRect.width - rect.width) / 2);
	int y = Math.max (parentRect.y, parentRect.y + (parentRect.height - rect.height) / 2);
	Rectangle monitorRect = parent.getMonitor ().getClientArea();
	if (x + rect.width > monitorRect.x + monitorRect.width) {
		x = Math.max (monitorRect.x, monitorRect.x + monitorRect.width - rect.width);
	} else {
		x = Math.max (x, monitorRect.x);
	}
	if (y + rect.height > monitorRect.y + monitorRect.height) {
		y = Math.max (monitorRect.y, monitorRect.y + monitorRect.height - rect.height);
	} else {
		y = Math.max (y, monitorRect.y);
	}
	setLocation (x, y);
}

void checkOpened () {
	if (!opened) resized = false;
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
 * @see SWT#Close
 * @see #dispose
 */
public void close () {
	checkWidget ();
	closeWidget ();
}

void createHandle () {
	if (shellHandle != 0) {
		scrolledHandle = OS.ContentControl_Content (shellHandle);
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
		int children = OS.Panel_Children (scrolledHandle);
		handle = OS.UIElementCollection_default (children, 0);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.GCHandle_Free (children);
		return;
	}
	state |= CANVAS;
	if ((style & SWT.ON_TOP) != 0) {
		shellHandle = OS.gcnew_Popup ();
		if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
		if ((style & SWT.NO_TRIM) != 0) OS.Popup_AllowsTransparency (shellHandle, true);
		OS.KeyboardNavigation_SetTabNavigation (shellHandle, OS.KeyboardNavigationMode_None);
		boolean scrolled = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
		createHandle (scrolled, true);
		OS.Popup_Child (shellHandle, super.topHandle());
		OS.Popup_IsOpen (shellHandle, true);
		OS.Popup_IsOpen (shellHandle, false);
		return;
	}
	shellHandle = OS.gcnew_Window();
	if (shellHandle == 0) error(SWT.ERROR_NO_HANDLES);
	if ((style & SWT.NO_TRIM) != 0) OS.Window_AllowsTransparency (shellHandle, true);

	/*
	* Feature in WPF.  ...
	*/
	OS.Window_ShowInTaskbar (shellHandle, false);
	OS.Window_ResizeMode (shellHandle, OS.ResizeMode_NoResize);
	OS.Window_WindowStyle (shellHandle, OS.WindowStyle_None);
	OS.FrameworkElement_Width (shellHandle, 0);
	OS.FrameworkElement_Height (shellHandle, 0);
	OS.Window_Show (shellHandle);
	OS.Window_Hide (shellHandle);
	Rectangle bounds = getMonitor ().getBounds ();
	OS.FrameworkElement_Width (shellHandle, bounds.width * 5 / 8);
	OS.FrameworkElement_Height (shellHandle, bounds.height * 5 / 8);

	int windowStyle = OS.WindowStyle_None, resizeMode = OS.ResizeMode_NoResize;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.TOOL) != 0) {
			windowStyle = OS.WindowStyle_ToolWindow;
		} else if ((style & SWT.SHELL_TRIM) != 0) {
			windowStyle = OS.WindowStyle_SingleBorderWindow;
		}
		if ((style & SWT.RESIZE) != 0) {
			resizeMode |= OS.ResizeMode_CanResize;			
		}
	}
	OS.Window_ShowInTaskbar (shellHandle, parent == null);
	OS.Window_ResizeMode (shellHandle, resizeMode);
	OS.Window_WindowStyle (shellHandle, windowStyle);
	OS.KeyboardNavigation_SetTabNavigation (shellHandle, OS.KeyboardNavigationMode_None);
	
	boolean scrolled = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	createHandle (scrolled, true);
	OS.ContentControl_Content (shellHandle, super.topHandle());
}

void createWidget() {
	super.createWidget ();
	display.addShell (this);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	if ((style & SWT.ON_TOP) != 0) return new Rectangle (x, y, width, height);
	updateLayout (shellHandle);
	int clientX = (int) OS.Window_Left (shellHandle);
	int clientY = (int) OS.Window_Top (shellHandle);
	int w = (int) OS.FrameworkElement_ActualWidth (shellHandle);
	int h = (int) OS.FrameworkElement_ActualHeight (shellHandle);
	int clientWidth = (int) OS.FrameworkElement_ActualWidth (handle);
	int clientHeight = (int) OS.FrameworkElement_ActualHeight (handle);
	int point = OS.gcnew_Point (clientX, clientY);
	int result = OS.Visual_PointFromScreen (shellHandle, point);
	OS.GCHandle_Free (point);
	clientX = -(int) OS.Point_X (result);
	clientY = -(int) OS.Point_Y (result);
	OS.GCHandle_Free (result);
	x -= clientX;
	y -= clientY;
	width += (w - clientWidth);
	height += (h - clientHeight);
	return new Rectangle (x, y, width, height);
}

void deregister () {
	super.deregister ();
	display.removeWidget (shellHandle);
}


void destroyWidget () {
	if (shellHandle != 0 && !closing) {
		if ((style & SWT.ON_TOP) != 0) {
			OS.Popup_IsOpen (shellHandle, false);
		} else {
			OS.Window_Close (shellHandle);
		}
	}
	releaseHandle ();
}

public void dispose () {
	/*
	* This code is intentionally commented.  On some
	* platforms, the owner window is repainted right
	* away when a dialog window exits.  This behavior
	* is currently unspecified.
	*/
//	/*
//	* Note:  It is valid to attempt to dispose a widget
//	* more than once.  If this happens, fail silently.
//	*/
//	if (!isValidWidget ()) return;
//	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
//	Display oldDisplay = display;
	super.dispose ();
	// widget is disposed at this point
//	if (oldDisplay != null) oldDisplay.update ();
}

Control findBackgroundControl () {
	return background != -1 || backgroundImage != null ? this : null;
}

Control findThemeControl () {
	return null;
}

/**
 * If the receiver is visible, moves it to the top of the 
 * drawing order for the display on which it was created 
 * (so that all other shells on that display, which are not 
 * the receiver's children will be drawn behind it) and forces 
 * the window manager to make the shell active.
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
 * @see Decorations#setDefaultButton(Button)
 * @see Shell#open
 * @see Shell#setActive
 */
public void forceActive () {
	checkWidget ();
	if(!isVisible()) return;
	if ((style & SWT.ON_TOP) != 0) return;
//	OS.SetForegroundWindow (handle);
	OS.Window_Activate (shellHandle);
}

/**
 * Returns the receiver's alpha value. The alpha value
 * is between 0 (transparent) and 255 (opaque).
 *
 * @return the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public int getAlpha () {
	checkWidget ();
	return 255;
}

public Rectangle getBounds () {
	checkWidget ();
	int x, y;
	if ((style & SWT.ON_TOP) != 0) {
		x = (int) OS.Popup_HorizontalOffset (shellHandle);
		y = (int) OS.Popup_VerticalOffset (shellHandle);
	} else {
		x = (int) OS.Window_Left (shellHandle);
		y = (int) OS.Window_Top (shellHandle);
	}
	int renderSize = OS.UIElement_RenderSize (shellHandle);
	int width = (int) OS.Size_Width (renderSize);
	int height = (int) OS.Size_Height (renderSize);
	OS.GCHandle_Free (renderSize);
	return new Rectangle (x, y, width, height);
}

/**
 * Returns <code>true</code> if the receiver is currently
 * in fullscreen state, and false otherwise. 
 * <p>
 *
 * @return the fullscreen state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public boolean getFullScreen() {
	checkWidget ();
	return fullScreen; 
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
	checkWidget ();
	//TODO
	return SWT.ROMAN;
}

public Point getLocation () {
	checkWidget ();
	int x, y;
	if ((style & SWT.ON_TOP) != 0) {
		x = (int) OS.Popup_HorizontalOffset (shellHandle);
		y = (int) OS.Popup_VerticalOffset (shellHandle);
	} else {
		x = (int) OS.Window_Left (shellHandle);
		y = (int) OS.Window_Top (shellHandle);
	}
	return new Point (x, y);
}

public boolean getMaximized () {
	checkWidget ();
	return !fullScreen && super.getMaximized ();
}

/**
 * Returns a point describing the minimum receiver's size. The
 * x coordinate of the result is the minimum width of the receiver.
 * The y coordinate of the result is the minimum height of the
 * receiver.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Point getMinimumSize () {
	checkWidget ();
//	int width = Math.max (0, minWidth);
//	int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
//	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
//		width = Math.max (width, OS.GetSystemMetrics (OS.SM_CXMINTRACK));
//	}
//	int height = Math.max (0, minHeight);
//	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
//		if ((style & SWT.RESIZE) != 0) {
//			height = Math.max (height, OS.GetSystemMetrics (OS.SM_CYMINTRACK));
//		} else {
//			RECT rect = new RECT ();
//			int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
//			int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
//			OS.AdjustWindowRectEx (rect, bits1, false, bits2);
//			height = Math.max (height, rect.bottom - rect.top);
//		}
//	}
	int width = (int) OS.FrameworkElement_MinWidth (shellHandle);
	int height = (int) OS.FrameworkElement_MinHeight (shellHandle);
	return new Point (width,  height);
}

/**
 * Gets the receiver's modified state.
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.5
 */
public boolean getModified () {
	checkWidget ();
	return modified;
}

/** 
 * Returns the region that defines the shape of the shell,
 * or null if the shell has the default shape.
 *
 * @return the region that defines the shape of the shell (or null)
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
	/* This method is needed for the @since 3.0 Javadoc tag*/
	checkWidget ();
	return region;
}

public Shell getShell () {
	checkWidget ();
	return this;
}

/**
 * Returns the instance of the ToolBar object representing the tool bar that can appear on the
 * trim of the shell. This will return <code>null</code> if the platform does not support tool bars that
 * not part of the content area of the shell, or if the style of the shell does not support a 
 * tool bar. 
 * <p>
 * 
 * @return a ToolBar object representing the window's tool bar or null.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.7
 */
public ToolBar getToolBar() {
	return null;
}

public boolean getVisible () {
	checkWidget ();
	if ((style & SWT.ON_TOP) != 0)return OS.Popup_IsOpen (shellHandle);
	return super.getVisible ();
}

/**
 * Returns an array containing all shells which are 
 * descendants of the receiver.
 * <p>
 * @return the dialog shells
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Shell [] getShells () {
	checkWidget ();
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

Composite findDeferredControl () {
	return layoutCount > 0 ? this : null;
}

public boolean isEnabled () {
	checkWidget ();
	return getEnabled ();
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_SizeChangedEventHandler (jniRef, "HandleSizeChanged");
	OS.FrameworkElement_SizeChanged (shellHandle, handler);
	OS.GCHandle_Free (handler);
	if ((style & SWT.ON_TOP) != 0) {
		handler = OS.gcnew_EventHandler (jniRef, "HandleClosed");
		OS.Popup_Closed (shellHandle, handler);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_KeyboardFocusChangedEventHandler (jniRef, "HandlePreviewGotKeyboardFocusActivate");
		OS.UIElement_PreviewGotKeyboardFocus (shellHandle, handler);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_KeyboardFocusChangedEventHandler (jniRef, "HandleLostKeyboardFocusDeactivate");
		OS.UIElement_LostKeyboardFocus (shellHandle, handler);
		OS.GCHandle_Free (handler);
	} else {
		handler = OS.gcnew_CancelEventHandler (jniRef, "HandleClosing");
		OS.Window_Closing (shellHandle, handler);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_EventHandler (jniRef, "HandleActivated");
		OS.Window_Activated (shellHandle, handler);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_EventHandler (jniRef, "HandleDeactivated");
		OS.Window_Deactivated (shellHandle, handler);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_EventHandler (jniRef, "HandleLocationChanged");
		OS.Window_LocationChanged (shellHandle, handler);
		OS.GCHandle_Free (handler);
	}
}

void HandleActivated (int sender, int e) {
	if (!checkEvent (e)) return;
	sendEvent (SWT.Activate);
	if (isDisposed ()) return;
	restoreFocus ();
}

void HandleClosed (int sender, int e) {
	if (!checkEvent (e)) return;
	closing = true;
	sendEvent (SWT.Close);
	closing = false;
}

void HandleClosing (int sender, int e) {
	if (!checkEvent (e)) return;
	closing = true;
	boolean doit = false;
	if (isEnabled ()) {
		Event event = new Event ();
		sendEvent (SWT.Close, event);
		doit = event.doit || isDisposed ();
	}
	if (doit) {
		/** 
		* Bug in WPF. When a window has more than
		* two child windows, and all the the child
		* windows are closed, the parent window is not 
		* brought to the front. The work around is to 
		* null out the Owner property of the child window
		* before disposing it.
		*/
		if (shellHandle != 0) OS.Window_Owner (shellHandle, 0);
		if (!isDisposed ()) release (false);
	} else {
		OS.CancelEventArgs_Cancel (e, true);
	}
	closing = false;
}

void HandleDeactivated (int sender, int e) {
	if (!checkEvent (e)) return;
	sendEvent (SWT.Deactivate);
	if (isDisposed ()) return;
	saveFocus ();
}

void HandleLocationChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	int x = (int) OS.Window_Left (shellHandle);
	int y = (int) OS.Window_Top (shellHandle);
	if (!moved || oldX != x || oldY != y) {
		moved = true;
		oldX = x;
		oldY = y;
		sendEvent (SWT.Move);
	}
}

void HandleLostKeyboardFocusDeactivate (int sender, int e) {
	if (isDisposed ()) return;
	if (OS.UIElement_IsKeyboardFocusWithin (handle)) return;
	sendEvent (SWT.Deactivate);
}

void HandleMouseLeave (int sender, int e) {
	super.HandleMouseLeave (sender, e);
	if (!checkEvent (e)) return;
	display.mouseControl = null;
}

void HandlePreviewGotKeyboardFocusActivate (int sender, int e) {
	if (isDisposed ()) return;
	if (!OS.UIElement_IsKeyboardFocusWithin (handle)) return;
	sendEvent (SWT.Activate);
}

void HandleSizeChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	int width = (int) OS.FrameworkElement_ActualWidth (shellHandle);
	int height = (int) OS.FrameworkElement_ActualHeight (shellHandle);
	if (!resized || oldWidth != width || oldHeight != height) {
		resized = true;
		oldWidth = width;
		oldHeight = height; 
		sendEvent (SWT.Resize);
		if (isDisposed ()) return;
		if (layout != null) {
			markLayout (false, false);
			updateLayout (false, false);
		}
	}
}

/**
 * Moves the receiver to the top of the drawing order for
 * the display on which it was created (so that all other
 * shells on that display, which are not the receiver's
 * children will be drawn behind it), marks it visible,
 * sets the focus and asks the window manager to make the
 * shell active.
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
 * @see Decorations#setDefaultButton(Button)
 * @see Shell#setActive
 * @see Shell#forceActive
 */
public void open () {
	checkWidget ();
	bringToTop ();
	if (isDisposed ()) return;
	setVisible (true);
	if (isDisposed ()) return;
	if (!restoreFocus () && !traverseGroup (true)) setFocus (); 
}

public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

void register () {
	super.register ();
	display.addWidget (shellHandle, this);
}

void releaseChildren (boolean destroy) {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (shell != null && !shell.isDisposed ()) {
			shell.release ((style & SWT.ON_TOP) != 0);
		}
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	if (shellHandle != 0) OS.GCHandle_Free (shellHandle);
	shellHandle = 0;
}

void releaseParent () {
	/* Do nothing */
}

void releaseWidget () {
	super.releaseWidget ();
	display.clearModal (this);
	display.removeShell (this);
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Close, listener);
	eventTable.unhook (SWT.Iconify,listener);
	eventTable.unhook (SWT.Deiconify,listener);
	eventTable.unhook (SWT.Activate, listener);
	eventTable.unhook (SWT.Deactivate, listener);
}

void reskinChildren (int flags) {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (shell != null) shell.reskin (flags);
	}
	super.reskinChildren (flags);
}

/**
 * If the receiver is visible, moves it to the top of the 
 * drawing order for the display on which it was created 
 * (so that all other shells on that display, which are not 
 * the receiver's children will be drawn behind it) and asks 
 * the window manager to make the shell active 
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
 * @see Decorations#setDefaultButton(Button)
 * @see Shell#open
 * @see Shell#setActive
 */
public void setActive () {
	checkWidget ();
	if(!isVisible()) return;
	bringToTop ();
	// widget could be disposed at this point
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
	Control [] activate = (control == null) ? new Control [0] : control.getPath ();
	Control [] deactivate = (lastActive == null) ? new Control [0] : lastActive.getPath ();
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

/**
 * Sets the receiver's alpha value which must be
 * between 0 (transparent) and 255 (opaque).
 * <p>
 * This operation requires the operating system's advanced
 * widgets subsystem which may not be available on some
 * platforms.
 * </p>
 * @param alpha the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public void setAlpha (int alpha) {
	checkWidget ();
	/* Not implemented */
}

int setBounds (int x, int y, int width, int height, int flags) {
	if (fullScreen) setFullScreen (false);
	int result = 0;
	if ((flags & MOVED) != 0) {
		int currentX, currentY;
		if ((style & SWT.ON_TOP) != 0) {
			currentX = (int) OS.Popup_HorizontalOffset (shellHandle);
			currentY = (int) OS.Popup_VerticalOffset (shellHandle);
		} else {
			currentX = (int) OS.Window_Left (shellHandle);
			currentY = (int) OS.Window_Top (shellHandle);
		}
		if (currentX != x || currentY != y) {
			moved = true;
			oldX = x;
			oldY = currentY;
			if ((style & SWT.ON_TOP) != 0) {
				OS.Popup_HorizontalOffset (shellHandle, x);
				oldY = y;
				OS.Popup_VerticalOffset (shellHandle, y);
			} else {
				OS.Window_Left (shellHandle, x);
				oldY = y;
				OS.Window_Top (shellHandle, y);
			}
			sendEvent (SWT.Move);
			if (isDisposed ()) return 0;
			result |= MOVED;
		}
	}
	if ((flags & RESIZED) != 0) {
		int currentWidth = (int) OS.FrameworkElement_ActualWidth (shellHandle);
		int currentHeight = (int) OS.FrameworkElement_ActualHeight (shellHandle);
		if (currentWidth != width || currentHeight != height) {
			resized = true;
			oldWidth = width;
			oldHeight = currentHeight;
			OS.FrameworkElement_Width (shellHandle, width);
			oldHeight = height;
			OS.FrameworkElement_Height (shellHandle, height);
			sendEvent (SWT.Resize);
			if (isDisposed ()) return 0;
			result |= RESIZED;
			if (layout != null) {
				markLayout (false, false);
				updateLayout (false, false);
			}
		}
	}
	return result;
}

void setClipping () {
	
}

public void setEnabled (boolean enabled) {
	checkWidget ();
	if (enabled == getEnabled ()) return;
	super.setEnabled (enabled);
	if ((style & SWT.ON_TOP) != 0) return;
	if (enabled && OS.Window_IsActive (shellHandle)) {
		if (!restoreFocus ()) traverseGroup (true);
	}
}

/**
 * Sets the full screen state of the receiver.
 * If the argument is <code>true</code> causes the receiver
 * to switch to the full screen state, and if the argument is
 * <code>false</code> and the receiver was previously switched
 * into full screen state, causes the receiver to switch back
 * to either the maximized or normal states.
 * <p>
 * Note: The result of intermixing calls to <code>setFullScreen(true)</code>, 
 * <code>setMaximized(true)</code> and <code>setMinimized(true)</code> will 
 * vary by platform. Typically, the behavior will match the platform user's 
 * expectations, but not always. This should be avoided if possible.
 * </p>
 * 
 * @param fullScreen the new fullscreen state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setFullScreen (boolean fullScreen) {
	checkWidget ();
	if (this.fullScreen == fullScreen) return;
	if(fullScreen) {
		oldWindowStyle = OS.Window_WindowStyle (shellHandle);
		oldWindowState = OS.Window_WindowState (shellHandle);
		boolean visible = getVisible ();
		OS.Window_Hide (shellHandle);
		OS.Window_WindowStyle (shellHandle, OS.WindowStyle_None);
		if (visible) OS.Window_Show (shellHandle);
		OS.Window_WindowState (shellHandle, OS.WindowState_Maximized);
	} else {
		OS.Window_WindowStyle (shellHandle, oldWindowStyle);
		OS.Window_WindowState (shellHandle, oldWindowState);
		oldWindowState = 0;
		oldWindowStyle = 0;
	}
	this.fullScreen = fullScreen;
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
	checkWidget ();
	//TODO
}

/**
 * Sets the receiver's minimum size to the size specified by the arguments.
 * If the new minimum size is larger than the current size of the receiver,
 * the receiver is resized to the new minimum size.
 *
 * @param width the new minimum width for the receiver
 * @param height the new minimum height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setMinimumSize (int width, int height) {
	checkWidget ();
	int widthLimit = 0, heightLimit = 0;
	int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
//		widthLimit = OS.GetSystemMetrics (OS.SM_CXMINTRACK);
//		if ((style & SWT.RESIZE) != 0) {
//			heightLimit = OS.GetSystemMetrics (OS.SM_CYMINTRACK);
//		} else {
//			RECT rect = new RECT ();
//			int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
//			int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
//			OS.AdjustWindowRectEx (rect, bits1, false, bits2);
//			heightLimit = rect.bottom - rect.top;
//		}
	} 
	int minWidth = Math.max (widthLimit, width);
	int minHeight = Math.max (heightLimit, height);
	OS.FrameworkElement_MinWidth(shellHandle, minWidth);
	OS.FrameworkElement_MinHeight(shellHandle, minHeight);
}

/**
 * Sets the receiver's minimum size to the size specified by the argument.
 * If the new minimum size is larger than the current size of the receiver,
 * the receiver is resized to the new minimum size.
 *
 * @param size the new minimum size for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setMinimumSize (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setMinimumSize (size.x, size.y);
}

/**
 * Sets the receiver's modified state as specified by the argument.
 *
 * @param modified the new modified state for the receiver
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.5
 */
public void setModified (boolean modified) {
	checkWidget ();
	this.modified = modified;
}

void setParent () {
	/* Do nothing.  Not necessary for Shells */
}

/**
 * Sets the shape of the shell to the region specified
 * by the argument.  When the argument is null, the
 * default shape of the shell is restored.  The shell
 * must be created with the style SWT.NO_TRIM in order
 * to specify a region.
 *
 * @param region the region that defines the shape of the shell (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul>  
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
	super.setRegion (region);
}

public void setVisible (boolean visible) {
	checkWidget ();
	if (getVisible () == visible) return;
	
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & mask) != 0) {
		if (visible) {
			display.setModalShell (this);
//				Control control = display._getFocusControl ();
//				if (control != null && !control.isActive ()) {
//					bringToTop ();
//					if (isDisposed ()) return;
//				}
//				int hwndShell = OS.GetActiveWindow ();
//				if (hwndShell == 0) {
//					if (parent != null) hwndShell = parent.handle;
//				}
//				if (hwndShell != 0) {
//					OS.SendMessage (hwndShell, OS.WM_CANCELMODE, 0, 0);
//				}
//				OS.ReleaseCapture ();
		} else {
			display.clearModal (this);
		}
	} else {
		updateModal ();
	}

	if (visible) {
		if (center && !moved) {
			center ();
			if (isDisposed ()) return;
		}
		if ((style & SWT.ON_TOP) != 0) {
			OS.Popup_IsOpen (shellHandle, visible);
		} else {
			OS.Window_Show (shellHandle);
		}
		
		opened = true;
		if (!moved) {
			moved = true;
			Point location = getLocation();
			oldX = location.x;
			oldY = location.y;
			sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
		if (!resized) {
			resized = true;
			Point size = getSize ();
			oldWidth = size.x;// - trimWidth ();
			oldHeight = size.y;// - trimHeight ();
			sendEvent (SWT.Resize);
			if (isDisposed ()) return;
			if (layout != null) {
				markLayout (false, false);
				updateLayout (false, false);
			}
		}
		
	} else {
		if (!closing) {
			if ((style & SWT.ON_TOP) != 0) {
				OS.Popup_IsOpen (shellHandle, visible);
			} else {				
				OS.Window_Hide (shellHandle);
			}
		}
	}
}

int topHandle () {
	return shellHandle;
}

//boolean translateAccelerator (MSG msg) {
//	if (!isEnabled () || !isActive ()) return false;
//	if (menuBar != null && !menuBar.isEnabled ()) return false;
//	return translateMDIAccelerator (msg) || translateMenuAccelerator (msg);
//}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}

void updateModal () {
	int source = OS.PresentationSource_FromVisual (handle);
	if (source != 0) {
		int hwnd = OS.HwndSource_Handle (source);
		Win32.EnableWindow (OS.IntPtr_ToInt32 (hwnd), isActive ());
		OS.GCHandle_Free (hwnd);
		OS.GCHandle_Free (source);
	}
}

}

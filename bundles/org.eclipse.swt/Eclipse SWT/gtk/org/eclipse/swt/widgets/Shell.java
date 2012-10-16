/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
	int /*long*/ shellHandle, tooltipsHandle, tooltipWindow, group, modalGroup;
	boolean mapped, moved, resized, opened, fullScreen, showWithParent, modified, center;
	int oldX, oldY, oldWidth, oldHeight;
	int minWidth, minHeight;
	Control lastActive;
	ToolTip [] toolTips;

	static final int MAXIMUM_TRIM = 128;
	static final int BORDER = 3;

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

Shell (Display display, Shell parent, int style, int /*long*/ handle, boolean embedded) {
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
			state |= FOREIGN_HANDLE;
		}
	}
	reskinWidget();
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

public static Shell gtk_new (Display display, int /*long*/ handle) {
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
public static Shell internal_new (Display display, int /*long*/ handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle, false);
}

static int checkStyle (Shell parent, int style) {
	style = Decorations.checkStyle (style);
	style &= ~SWT.TRANSPARENT;
	if ((style & SWT.ON_TOP) != 0) style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Close,typedListener);
	addListener (SWT.Iconify,typedListener);
	addListener (SWT.Deiconify,typedListener);
	addListener (SWT.Activate, typedListener);
	addListener (SWT.Deactivate, typedListener);
}

void addToolTip (ToolTip toolTip) {
	if (toolTips  == null) toolTips = new ToolTip [4];
	for (int i=0; i<toolTips.length; i++) {
		if (toolTips [i] == null) {
			toolTips [i] = toolTip;
			return;
		}
	}
	ToolTip [] newToolTips = new ToolTip [toolTips.length + 4];
	newToolTips [toolTips.length] = toolTip;
	System.arraycopy (toolTips, 0, newToolTips, 0, toolTips.length);
	toolTips = newToolTips;
}

void adjustTrim () {
	if (display.ignoreTrim) return;
	int width = OS.GTK_WIDGET_WIDTH (shellHandle);
	int height = OS.GTK_WIDGET_HEIGHT (shellHandle);
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (shellHandle);
	GdkRectangle rect = new GdkRectangle ();
	OS.gdk_window_get_frame_extents (window, rect);
	int trimWidth = Math.max (0, rect.width - width);
	int trimHeight = Math.max (0, rect.height - height);
	/*
	* Bug in GTK.  gdk_window_get_frame_extents() fails for various window
	* managers, causing a large incorrect value to be returned as the trim.
	* The fix is to ignore the returned trim values if they are too large.
	*/
	if (trimWidth > MAXIMUM_TRIM || trimHeight > MAXIMUM_TRIM) {
		display.ignoreTrim = true;
		return;
	}
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
	Display display = this.display;
	Shell activeShell = display.activeShell;
	if (activeShell == this) return;
	if (!force) {
		if (activeShell == null) return;
		if (!display.activePending) {
			int /*long*/ focusHandle = OS.gtk_window_get_focus (activeShell.shellHandle);
			if (focusHandle != 0 && !OS.GTK_WIDGET_HAS_FOCUS (focusHandle)) return;
		}
	}
	/*
	* Bug in GTK.  When a shell that is not managed by the window
	* manage is given focus, GTK gets stuck in "focus follows pointer"
	* mode when the pointer is within the shell and its parent when
	* the shell is hidden or disposed. The fix is to use XSetInputFocus()
	* to assign focus when ever the active shell has not managed by
	* the window manager.
	* 
	* NOTE: This bug is fixed in GTK+ 2.6.8 and above.
	*/
	boolean xFocus = false;
	if (activeShell != null) {
		if (OS.GTK_VERSION < OS.VERSION (2, 6, 8)) {
			xFocus = activeShell.isUndecorated ();
		}
		display.activeShell = null;
		display.activePending = true;
	}
	/*
	* Feature in GTK.  When the shell is an override redirect
	* window, gdk_window_focus() does not give focus to the
	* window.  The fix is to use XSetInputFocus() to force
	* the focus.
	*/
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (shellHandle);
	if ((xFocus || (style & SWT.ON_TOP) != 0) && OS.GDK_WINDOWING_X11 ()) {
		int /*long*/ xDisplay = OS.gdk_x11_drawable_get_xdisplay (window);
		int /*long*/ xWindow = OS.gdk_x11_drawable_get_xid (window);
		OS.gdk_error_trap_push ();
		/* Use CurrentTime instead of the last event time to ensure that the shell becomes active */
		OS.XSetInputFocus (xDisplay, xWindow, OS.RevertToParent, OS.CurrentTime);
		OS.gdk_error_trap_pop ();
	} else {
		/*
		* Bug in metacity.  Calling gdk_window_focus() with a timestamp more
		* recent than the last user interaction time can cause windows not
		* to come forward in versions > 2.10.0.  The fix is to use the last
		* user event time. 
		*/
		if (display.windowManager.toLowerCase ().equals ("metacity")) {
			OS.gdk_window_focus (window, display.lastUserEventTime);
		} else {
			OS.gdk_window_focus (window, OS.GDK_CURRENT_TIME);
		}
	}
	display.activeShell = this;
	display.activePending = true;
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

void checkBorder () {
	/* Do nothing */
}

void checkOpen () {
	if (!opened) resized = false;
}

int /*long*/ childStyle () {
	return 0;
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
void closeWidget () {
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrim (x, y, width, height);
	int border = 0;
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0) {
		border = OS.gtk_container_get_border_width (shellHandle);
	}
	if (isCustomResize ()) {
		border = OS.gtk_container_get_border_width (shellHandle);
	}
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	trim.x -= (trimWidth / 2) + border;
	trim.y -= trimHeight - (trimWidth / 2) + border;
	trim.width += trimWidth + border * 2;
	trim.height += trimHeight + border * 2;
	if (menuBar != null) {
		forceResize ();
		int menuBarHeight = OS.GTK_WIDGET_HEIGHT (menuBar.handle);
		trim.y -= menuBarHeight;
		trim.height += menuBarHeight;
	}
	return trim;
}

void createHandle (int index) {
	state |= HANDLE | CANVAS;
	if (shellHandle == 0) {
		if (handle == 0) {
			int type = OS.GTK_WINDOW_TOPLEVEL;
			if ((style & SWT.ON_TOP) != 0) type = OS.GTK_WINDOW_POPUP;
			shellHandle = OS.gtk_window_new (type);
		} else {
			shellHandle = OS.gtk_plug_new (handle);
		}
		if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
		if (parent != null) {
			OS.gtk_window_set_transient_for (shellHandle, parent.topHandle ());
			OS.gtk_window_set_destroy_with_parent (shellHandle, true);
			if (!isUndecorated ()) {
				OS.gtk_window_set_type_hint (shellHandle, OS.GDK_WINDOW_TYPE_HINT_DIALOG);
			} else {
				OS.gtk_window_set_skip_taskbar_hint (shellHandle, true);
			}
		}
		/*
		* Feature in GTK.  The window size must be set when the window
		* is created or it will not be allowed to be resized smaller that the
		* initial size by the user.  The fix is to set the size to zero.
		*/
		if ((style & SWT.RESIZE) != 0) {
			OS.gtk_widget_set_size_request (shellHandle, 0, 0);
			OS.gtk_window_set_resizable (shellHandle, true);
		} else {
			OS.gtk_window_set_resizable (shellHandle, false);
		}
		vboxHandle = OS.gtk_vbox_new (false, 0);
		if (vboxHandle == 0) error (SWT.ERROR_NO_HANDLES);
		createHandle (index, false, true);
		OS.gtk_container_add (vboxHandle, scrolledHandle);
		OS.gtk_box_set_child_packing (vboxHandle, scrolledHandle, true, true, 0, OS.GTK_PACK_END);
		OS.gtk_window_set_title (shellHandle, new byte [1]);
		if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0) {
			OS.gtk_container_set_border_width (shellHandle, 1);
			GdkColor color = new GdkColor ();
			OS.gtk_style_get_black (OS.gtk_widget_get_style (shellHandle), color);
			OS.gtk_widget_modify_bg (shellHandle,  OS.GTK_STATE_NORMAL, color);
		}
		if (isCustomResize ()) {
			OS.gtk_container_set_border_width (shellHandle, BORDER);
		} 
	} else {
		vboxHandle = OS.gtk_bin_get_child (shellHandle);
		if (vboxHandle == 0) error (SWT.ERROR_NO_HANDLES);
		int /*long*/ children = OS.gtk_container_get_children (vboxHandle);
		if (OS.g_list_length (children) > 0) {
			scrolledHandle = OS.g_list_data (children);
		}
		OS.g_list_free (children);
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
		handle = OS.gtk_bin_get_child (scrolledHandle);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	group = OS.gtk_window_group_new ();
	if (group == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	* Feature in GTK.  Realizing the shell triggers a size allocate event,
	* which may be confused for a resize event from the window manager if
	* received too late.  The fix is to realize the window during creation
	* to avoid confusion.
	*/
	OS.gtk_widget_realize (shellHandle);
}

int /*long*/ filterProc (int /*long*/ xEvent, int /*long*/ gdkEvent, int /*long*/ data2) {
	int eventType = OS.X_EVENT_TYPE (xEvent);
	if (eventType != OS.FocusOut && eventType != OS.FocusIn) return 0;
	XFocusChangeEvent xFocusEvent = new XFocusChangeEvent();
	OS.memmove (xFocusEvent, xEvent, XFocusChangeEvent.sizeof);
	switch (eventType) {
		case OS.FocusIn: 
			if (xFocusEvent.mode == OS.NotifyNormal || xFocusEvent.mode == OS.NotifyWhileGrabbed) {
				switch (xFocusEvent.detail) {
					case OS.NotifyNonlinear:
					case OS.NotifyNonlinearVirtual:
					case OS.NotifyAncestor:
						if (tooltipsHandle != 0 && OS.GTK_VERSION < OS.VERSION (2, 12, 0)) {
						    OS.gtk_tooltips_enable (tooltipsHandle);
						}
						display.activeShell = this;
						display.activePending = false;
						sendEvent (SWT.Activate);
						if (isDisposed ()) return 0;
						if (isCustomResize ()) {
							OS.gdk_window_invalidate_rect (OS.GTK_WIDGET_WINDOW (shellHandle), null, false);
						}
						break;
				}
			} 
			break;
		case OS.FocusOut:
			if (xFocusEvent.mode == OS.NotifyNormal || xFocusEvent.mode == OS.NotifyWhileGrabbed) {
				switch (xFocusEvent.detail) {
					case OS.NotifyNonlinear:
					case OS.NotifyNonlinearVirtual:
					case OS.NotifyVirtual:
						if (tooltipsHandle != 0 && OS.GTK_VERSION < OS.VERSION (2, 12, 0)) {
							OS.gtk_tooltips_disable (tooltipsHandle);
						}
						Display display = this.display;
						sendEvent (SWT.Deactivate);
						setActiveControl (null);
						if (display.activeShell == this) {
							display.activeShell = null;
							display.activePending = false;
						}
						if (isDisposed ()) return 0;
						if (isCustomResize ()) {
							OS.gdk_window_invalidate_rect (OS.GTK_WIDGET_WINDOW (shellHandle), null, false);
						}
						break;
				}
			}
			break;
	}
	return 0;
}

Control findBackgroundControl () {
	return (state & BACKGROUND) != 0 || backgroundImage != null ? this : null;
}

Composite findDeferredControl () {
	return layoutCount > 0 ? this : null;
}

/**
 * Returns a ToolBar object representing the tool bar that can be shown in the receiver's
 * trim. This will return <code>null</code> if the platform does not support tool bars that
 * are not part of the content area of the shell, or if the Shell's style does not support 
 * having a tool bar. 
 * <p>
 * 
 * @return a ToolBar object representing the Shell's tool bar, or <ocde>null</code>.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.7
 */
public ToolBar getToolBar() {
	checkWidget ();
	return null;
}

boolean hasBorder () {
	return false;
}

void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [KEY_PRESS_EVENT], 0, display.closures [KEY_PRESS_EVENT], false);
	OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [WINDOW_STATE_EVENT], 0, display.closures [WINDOW_STATE_EVENT], false);
	OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [SIZE_ALLOCATE], 0, display.closures [SIZE_ALLOCATE], false);
	OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [CONFIGURE_EVENT], 0, display.closures [CONFIGURE_EVENT], false);
	OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [DELETE_EVENT], 0, display.closures [DELETE_EVENT], false);
	OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [MAP_EVENT], 0, display.shellMapProcClosure, false);
	OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.closures [ENTER_NOTIFY_EVENT], false);
	OS.g_signal_connect_closure (shellHandle, OS.move_focus, display.closures [MOVE_FOCUS], false);
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (shellHandle);
	OS.gdk_window_add_filter  (window, display.filterProc, shellHandle);
	if (isCustomResize ()) {
		int mask = OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_RELEASE_MASK | OS.GDK_BUTTON_PRESS_MASK |  OS.GDK_ENTER_NOTIFY_MASK | OS.GDK_LEAVE_NOTIFY_MASK;
		OS.gtk_widget_add_events (shellHandle, mask);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [EXPOSE_EVENT], 0, display.closures[EXPOSE_EVENT], false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [LEAVE_NOTIFY_EVENT], 0, display.closures [LEAVE_NOTIFY_EVENT], false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.closures [MOTION_NOTIFY_EVENT], false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT], false);
	}
}

public boolean isEnabled () {
	checkWidget ();
	return getEnabled ();
}

boolean isUndecorated () {
	return
		(style & (SWT.SHELL_TRIM | SWT.BORDER)) == SWT.NONE ||
		(style & (SWT.NO_TRIM | SWT.ON_TOP)) != 0;
}

boolean isCustomResize () {
	return (style & SWT.NO_TRIM) == 0 && (style & (SWT.RESIZE | SWT.ON_TOP)) == (SWT.RESIZE | SWT.ON_TOP);
}

public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

void register () {
	super.register ();
	display.addWidget (shellHandle, this);
}

void releaseParent () {
	/* Do nothing */
}

int /*long*/ topHandle () {
	return shellHandle;
}

void fixActiveShell () {
	if (display.activeShell == this) {
		Shell shell = null;
		if (parent != null && parent.isVisible ()) shell = parent.getShell ();
		if (shell == null && isUndecorated ()) {
			Shell [] shells = display.getShells ();
			for (int i = 0; i < shells.length; i++) {
				if (shells [i] != null && shells [i].isVisible ()) {
					shell = shells [i];
					break;
				}
			}
		}
		if (shell != null) shell.bringToTop (false);
	}
}

void fixShell (Shell newShell, Control control) {
	if (this == newShell) return;
	if (control == lastActive) setActiveControl (null);
	String toolTipText = control.toolTipText;
	if (toolTipText != null) {
		control.setToolTipText (this, null);
		control.setToolTipText (newShell, toolTipText);
	}
}

int /*long*/ fixedSizeAllocateProc(int /*long*/ widget, int /*long*/ allocationPtr) {
	int clientWidth = 0;
	if ((style & SWT.MIRRORED) != 0) clientWidth = getClientWidth ();
	int /*long*/ result = super.fixedSizeAllocateProc (widget, allocationPtr);
	if ((style & SWT.MIRRORED) != 0) moveChildren (clientWidth);
	return result;
}

void fixStyle (int /*long*/ handle) {
}

void forceResize () {
	forceResize (OS.GTK_WIDGET_WIDTH (vboxHandle), OS.GTK_WIDGET_HEIGHT (vboxHandle));
}

void forceResize (int width, int height) {
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (vboxHandle, requisition);
	GtkAllocation allocation = new GtkAllocation ();
	int border = OS.gtk_container_get_border_width (shellHandle);
	allocation.x = border;
	allocation.y = border;
	allocation.width = width;
	allocation.height = height;
	OS.gtk_widget_size_allocate (vboxHandle, allocation);
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
	if (OS.GTK_VERSION >= OS.VERSION (2, 12, 0)) {
		if (OS.gtk_widget_is_composited (shellHandle)) {
			return (int) (OS.gtk_window_get_opacity (shellHandle) * 255);
		}
	}
	return 255;  
}

int getResizeMode (double x, double y) {
	int width = OS.GTK_WIDGET_WIDTH (shellHandle);
	int height = OS.GTK_WIDGET_HEIGHT (shellHandle);
	int border = OS.gtk_container_get_border_width (shellHandle);
	int mode = 0;
	if (y >= height - border) {
		mode = OS.GDK_BOTTOM_SIDE ;
		if (x >= width - border - 16) mode = OS.GDK_BOTTOM_RIGHT_CORNER;
		else if (x <= border + 16) mode = OS.GDK_BOTTOM_LEFT_CORNER;
	} else if (x >= width - border) {
		mode = OS.GDK_RIGHT_SIDE;
		if (y >= height - border - 16) mode = OS.GDK_BOTTOM_RIGHT_CORNER;
		else if (y <= border + 16) mode = OS.GDK_TOP_RIGHT_CORNER;
	} else if (y <= border) {
		mode = OS.GDK_TOP_SIDE;
		if (x <= border + 16) mode = OS.GDK_TOP_LEFT_CORNER;
		else if (x >= width - border - 16) mode = OS.GDK_TOP_RIGHT_CORNER;
	} else if (x <= border) {
		mode = OS.GDK_LEFT_SIDE;
		if (y <= border + 16) mode = OS.GDK_TOP_LEFT_CORNER;
		else if (y >= height - border - 16) mode = OS.GDK_BOTTOM_LEFT_CORNER;
	}
	return mode;
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
public boolean getFullScreen () {
	checkWidget();
	return fullScreen;
}

public Point getLocation () {
	checkWidget ();
	int [] x = new int [1], y = new int [1];
	OS.gtk_window_get_position (shellHandle, x,y);
	return new Point (x [0], y [0]);
}

public boolean getMaximized () {
	checkWidget();
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
	int width = Math.max (1, minWidth + trimWidth ());
	int height = Math.max (1, minHeight + trimHeight ());
	return new Point (width, height);
}

Shell getModalShell () {
	Shell shell = null;
	Shell [] modalShells = display.modalShells;
	if (modalShells != null) {
		int bits = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
		int index = modalShells.length;
		while (--index >= 0) {
			Shell modal = modalShells [index];
			if (modal != null) {
				if ((modal.style & bits) != 0) {
					Control control = this;
					while (control != null) {
						if (control == modal) break;
						control = control.parent;
					}
					if (control != modal) return modal;
					break;
				}
				if ((modal.style & SWT.PRIMARY_MODAL) != 0) {
					if (shell == null) shell = getShell ();
					if (modal.parent == shell) return modal;
				}
			}
		}
	}
	return null;
}

/**
 * Gets the receiver's modified state.
 *
 * @return <code>true</code> if the receiver is marked as modified, or <code>false</code> otherwise
 * 
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

public Point getSize () {
	checkWidget ();
	int width = OS.GTK_WIDGET_WIDTH (vboxHandle);
	int height = OS.GTK_WIDGET_HEIGHT (vboxHandle);
	int border = 0;
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0) {
		border = OS.gtk_container_get_border_width (shellHandle);
	}
	return new Point (width + trimWidth () + 2*border, height + trimHeight () + 2*border);
}

public boolean getVisible () {
	checkWidget();
	return OS.GTK_WIDGET_VISIBLE (shellHandle); 
}

/** 
 * Returns the region that defines the shape of the shell,
 * or <code>null</code> if the shell has the default shape.
 *
 * @return the region that defines the shape of the shell, or <code>null</code>
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
	/* This method is needed for @since 3.0 Javadoc */
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

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			if ((style & SWT.ON_TOP) != 0 && (style & SWT.NO_FOCUS) == 0) {
				forceActive ();
			}
			GdkEventButton gdkEvent = new GdkEventButton ();
			OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
			if (gdkEvent.button == 1) {
				display.resizeLocationX = gdkEvent.x_root;
				display.resizeLocationY = gdkEvent.y_root;
				int [] x = new int [1], y = new int [1];
				OS.gtk_window_get_position (shellHandle, x, y);
				display.resizeBoundsX = x [0];
				display.resizeBoundsY = y [0];
				display.resizeBoundsWidth = OS.GTK_WIDGET_WIDTH (shellHandle);
				display.resizeBoundsHeight = OS.GTK_WIDGET_HEIGHT (shellHandle);
			}
		}
		return 0;
	}
	return super.gtk_button_press_event (widget, event);
}

int /*long*/ gtk_configure_event (int /*long*/ widget, int /*long*/ event) {
	int [] x = new int [1], y = new int [1];
	OS.gtk_window_get_position (shellHandle, x, y);
	if (!moved || oldX != x [0] || oldY != y [0]) {
		moved = true;
		oldX = x [0];
		oldY = y [0];
		sendEvent (SWT.Move);
		// widget could be disposed at this point
	}
	return 0;
}

int /*long*/ gtk_delete_event (int /*long*/ widget, int /*long*/ event) {
	if (isEnabled()) closeWidget ();
	return 1;
}

int /*long*/ gtk_enter_notify_event (int /*long*/ widget, int /*long*/ event) {
	if (widget != shellHandle) {
		return super.gtk_enter_notify_event (widget, event);
	}
	return 0;
}

int /*long*/ gtk_expose_event (int /*long*/ widget, int /*long*/ event) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			GdkEventExpose gdkEventExpose = new GdkEventExpose ();
			OS.memmove (gdkEventExpose, event, GdkEventExpose.sizeof);
			int /*long*/ style = OS.gtk_widget_get_style (widget);
			int /*long*/ window = OS.GTK_WIDGET_WINDOW (widget);
			int [] width = new int [1];
			int [] height = new int [1];
			OS.gdk_drawable_get_size (window, width, height);
			GdkRectangle area = new GdkRectangle ();
			area.x = gdkEventExpose.area_x;
			area.y = gdkEventExpose.area_y;
			area.width = gdkEventExpose.area_width;
			area.height = gdkEventExpose.area_height;
			byte [] detail = Converter.wcsToMbcs (null, "base", true); //$NON-NLS-1$
			int border = OS.gtk_container_get_border_width (widget);
			int state = display.activeShell == this ? OS.GTK_STATE_SELECTED : OS.GTK_STATE_PRELIGHT;
			OS.gtk_paint_flat_box (style, window, state, OS.GTK_SHADOW_NONE, area, widget, detail, 0, 0, width [0], border);
			OS.gtk_paint_flat_box (style, window, state, OS.GTK_SHADOW_NONE, area, widget, detail, 0, height [0] - border, width [0], border);
			OS.gtk_paint_flat_box (style, window, state, OS.GTK_SHADOW_NONE, area, widget, detail, 0, border, border, height [0] - border - border);
			OS.gtk_paint_flat_box (style, window, state, OS.GTK_SHADOW_NONE, area, widget, detail, width [0] - border, border, border, height [0] - border - border);
			OS.gtk_paint_box (style, window, state, OS.GTK_SHADOW_OUT, area, widget, detail, 0, 0, width [0], height [0]);
			return 1;
		}
		return 0;
	}
	return super.gtk_expose_event (widget, event);
}

int /*long*/ gtk_focus (int /*long*/ widget, int /*long*/ directionType) {
	switch ((int)/*64*/directionType) {
		case OS.GTK_DIR_TAB_FORWARD:
		case OS.GTK_DIR_TAB_BACKWARD:
			Control control = display.getFocusControl ();
			if (control != null) {
				if ((control.state & CANVAS) != 0 && (control.style & SWT.EMBEDDED) != 0 && control.getShell () == this) {
					int traversal = directionType == OS.GTK_DIR_TAB_FORWARD ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
					control.traverse (traversal);
					return 1;
				}
			}
			break;
	}
	return super.gtk_focus (widget, directionType);
}

int /*long*/ gtk_leave_notify_event (int /*long*/ widget, int /*long*/ event) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			GdkEventCrossing gdkEvent = new GdkEventCrossing ();
			OS.memmove (gdkEvent, event, GdkEventCrossing.sizeof);
			if ((gdkEvent.state & OS.GDK_BUTTON1_MASK) == 0) {
				int /*long*/ window = OS.GTK_WIDGET_WINDOW (shellHandle);
				OS.gdk_window_set_cursor (window, 0);
				display.resizeMode = 0;
			}
		}
		return 0;
	}
	return super.gtk_leave_notify_event (widget, event);
}

int /*long*/ gtk_move_focus (int /*long*/ widget, int /*long*/ directionType) {
	Control control = display.getFocusControl ();
	if (control != null) {
		int /*long*/ focusHandle = control.focusHandle ();
		OS.gtk_widget_child_focus (focusHandle, (int)/*64*/directionType);
	}
	OS.g_signal_stop_emission_by_name (shellHandle, OS.move_focus);
	return 1;
}

int /*long*/ gtk_motion_notify_event (int /*long*/ widget, int /*long*/ event) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			GdkEventMotion gdkEvent = new GdkEventMotion ();
			OS.memmove (gdkEvent, event, GdkEventMotion.sizeof);
			if ((gdkEvent.state & OS.GDK_BUTTON1_MASK) != 0) {
				int border = OS.gtk_container_get_border_width (shellHandle);
				int dx = (int)(gdkEvent.x_root - display.resizeLocationX);
				int dy = (int)(gdkEvent.y_root - display.resizeLocationY);
				int x = display.resizeBoundsX;
				int y = display.resizeBoundsY;
				int width = display.resizeBoundsWidth;
				int height = display.resizeBoundsHeight;
				int newWidth = Math.max(width - dx, Math.max(minWidth, border + border));
				int newHeight = Math.max(height - dy, Math.max(minHeight, border + border));
				switch (display.resizeMode) {
					case OS.GDK_LEFT_SIDE:
						x += width - newWidth;
						width = newWidth;
						break;
					case OS.GDK_TOP_LEFT_CORNER:
						x += width - newWidth;
						width = newWidth;
						y += height - newHeight;
						height = newHeight;
						break;
					case OS.GDK_TOP_SIDE:
						y += height - newHeight;
						height = newHeight;
						break;
					case OS.GDK_TOP_RIGHT_CORNER:
						width = Math.max(width + dx, Math.max(minWidth, border + border));
						y += height - newHeight;
						height = newHeight;
						break;
					case OS.GDK_RIGHT_SIDE:
						width = Math.max(width + dx, Math.max(minWidth, border + border));
						break;
					case OS.GDK_BOTTOM_RIGHT_CORNER:
						width = Math.max(width + dx, Math.max(minWidth, border + border));
						height = Math.max(height + dy, Math.max(minHeight, border + border));
						break;
					case OS.GDK_BOTTOM_SIDE:
						height = Math.max(height + dy, Math.max(minHeight, border + border));
						break;
					case OS.GDK_BOTTOM_LEFT_CORNER:
						x += width - newWidth;
						width = newWidth;
						height = Math.max(height + dy, Math.max(minHeight, border + border));
						break;
				}
				if (x != display.resizeBoundsX || y != display.resizeBoundsY) {
					OS.gdk_window_move_resize (OS.GTK_WIDGET_WINDOW (shellHandle), x, y, width, height);
				} else {
					OS.gtk_window_resize (shellHandle, width, height);
				}
			} else {
				int mode = getResizeMode (gdkEvent.x, gdkEvent.y);
				if (mode != display.resizeMode) {
					int /*long*/ window = OS.GTK_WIDGET_WINDOW (shellHandle);
					int /*long*/ cursor = OS.gdk_cursor_new (mode);
					OS.gdk_window_set_cursor (window, cursor);
					OS.gdk_cursor_unref (cursor);
					display.resizeMode = mode;
				}
			}
		}
		return 0;
	}
	return super.gtk_motion_notify_event (widget, event);
}

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ event) {
	if (widget == shellHandle) {
		/* Stop menu mnemonics when the shell is disabled */
		if ((state & DISABLED) != 0) return 1;
		
		if (menuBar != null && !menuBar.isDisposed ()) {
			Control focusControl = display.getFocusControl ();
			if (focusControl != null && (focusControl.hooks (SWT.KeyDown) || focusControl.filters (SWT.KeyDown))) {
				int /*long*/ [] accel = new int /*long*/ [1];
				int /*long*/ setting = OS.gtk_settings_get_default ();
				OS.g_object_get (setting, OS.gtk_menu_bar_accel, accel, 0);
				if (accel [0] != 0) {
					int [] keyval = new int [1];
					int [] mods = new int [1];
					OS.gtk_accelerator_parse (accel [0], keyval, mods);
					OS.g_free (accel [0]);
					if (keyval [0] != 0) {
						GdkEventKey keyEvent = new GdkEventKey ();
						OS.memmove (keyEvent, event, GdkEventKey.sizeof);
						int mask = OS.gtk_accelerator_get_default_mod_mask ();
						if (keyEvent.keyval == keyval [0] && (keyEvent.state & mask) == (mods [0] & mask)) {
							return focusControl.gtk_key_press_event (focusControl.focusHandle (), event);
						}
					}
				}
			}
		}
		return 0;
	}
	return super.gtk_key_press_event (widget, event);
}

int /*long*/ gtk_size_allocate (int /*long*/ widget, int /*long*/ allocation) {
	int width = OS.GTK_WIDGET_WIDTH (shellHandle);
	int height = OS.GTK_WIDGET_HEIGHT (shellHandle);
	if (!resized || oldWidth != width || oldHeight != height) {
		oldWidth = width;
		oldHeight = height;
		resizeBounds (width, height, true);
	}
	return 0;
}

int /*long*/ gtk_realize (int /*long*/ widget) {
	int /*long*/ result = super.gtk_realize (widget);
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (shellHandle);
	if ((style & SWT.SHELL_TRIM) != SWT.SHELL_TRIM) {
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
	}
	if ((style & SWT.ON_TOP) != 0) {
		OS.gdk_window_set_override_redirect (window, true);
	}
	return result;
}

int /*long*/ gtk_window_state_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventWindowState gdkEvent = new GdkEventWindowState ();
	OS.memmove (gdkEvent, event, GdkEventWindowState.sizeof);
	minimized = (gdkEvent.new_window_state & OS.GDK_WINDOW_STATE_ICONIFIED) != 0;
	maximized = (gdkEvent.new_window_state & OS.GDK_WINDOW_STATE_MAXIMIZED) != 0;
	fullScreen = (gdkEvent.new_window_state & OS.GDK_WINDOW_STATE_FULLSCREEN) != 0;
	if ((gdkEvent.changed_mask & OS.GDK_WINDOW_STATE_ICONIFIED) != 0) {
		if (minimized) {
			sendEvent (SWT.Iconify);
		} else {
			sendEvent (SWT.Deiconify);
		}
		updateMinimized (minimized);
	}
	return 0;
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
	bringToTop (false);
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

void removeTooTip (ToolTip toolTip) {
	if (toolTips == null) return;
	for (int i=0; i<toolTips.length; i++) {
		if (toolTips [i] == toolTip) {
			toolTips [i] = null;
			return;
		}
	}
}

void reskinChildren (int flags) {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (shell != null) shell.reskin (flags);
	}
	if (toolTips != null) {
		for (int i=0; i<toolTips.length; i++) {
			ToolTip toolTip = toolTips [i];
			if (toolTip != null) toolTip.reskin (flags);
		}
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
	if (OS.GTK_VERSION >= OS.VERSION (2, 12, 0)) {
		if (OS.gtk_widget_is_composited (shellHandle)) {
			alpha &= 0xFF;
			OS.gtk_window_set_opacity (shellHandle, alpha / 255f);
		}
	}
}

void resizeBounds (int width, int height, boolean notify) {
	if (redrawWindow != 0) {
		OS.gdk_window_resize (redrawWindow, width, height);
	}
	if (enableWindow != 0) {
		OS.gdk_window_resize (enableWindow, width, height);
	}
	int border = OS.gtk_container_get_border_width (shellHandle);
	int boxWidth = width - 2*border;
	int boxHeight = height - 2*border;
	OS.gtk_widget_set_size_request (vboxHandle, boxWidth, boxHeight);
	forceResize (boxWidth, boxHeight);
	if (notify) {
		resized = true;
		sendEvent (SWT.Resize);
		if (isDisposed ()) return;
		if (layout != null) {
			markLayout (false, false);
			updateLayout (false);
		}
	}
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	if (fullScreen) setFullScreen (false);
	/*
	* Bug in GTK.  When either of the location or size of
	* a shell is changed while the shell is maximized, the
	* shell is moved to (0, 0).  The fix is to explicitly
	* unmaximize the shell before setting the bounds to
	* anything different from the current bounds.
	*/
	if (getMaximized ()) {
		Rectangle rect = getBounds ();
		boolean sameOrigin = !move || (rect.x == x && rect.y == y);
		boolean sameExtent = !resize || (rect.width == width && rect.height == height);
		if (sameOrigin && sameExtent) return 0;
		setMaximized (false);
	}
	int result = 0;
	if (move) {
		int [] x_pos = new int [1], y_pos = new int [1];
		OS.gtk_window_get_position (shellHandle, x_pos, y_pos);
		OS.gtk_window_move (shellHandle, x, y);
		if (x_pos [0] != x || y_pos [0] != y) {
			moved = true;
			oldX = x;
			oldY = y;
			sendEvent (SWT.Move);
			if (isDisposed ()) return 0;
			result |= MOVED;
		}
	}
	if (resize) {
		width = Math.max (1, Math.max (minWidth, width - trimWidth ()));
		height = Math.max (1, Math.max (minHeight, height - trimHeight ()));
		/*
		* If the shell is created without a RESIZE style bit, and the
		* minWidth/minHeight has been set, allow the resize.
		*/
		if ((style & SWT.RESIZE) != 0 || (minHeight != 0 || minWidth != 0)) OS.gtk_window_resize (shellHandle, width, height);
		boolean changed = width != oldWidth || height != oldHeight;
		if (changed) {
			oldWidth = width;
			oldHeight = height;
			result |= RESIZED;
		}
		resizeBounds (width, height, changed);
	}
	return result;
}

void setCursor (int /*long*/ cursor) {
	if (enableWindow != 0) {
		OS.gdk_window_set_cursor (enableWindow, cursor);
		if (!OS.GDK_WINDOWING_X11 ()) {
			OS.gdk_flush ();
		} else {
			int /*long*/ xDisplay = OS.gdk_x11_display_get_xdisplay(OS.gdk_display_get_default());
			OS.XFlush (xDisplay);
		}
	}
	super.setCursor (cursor);
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (((state & DISABLED) == 0) == enabled) return;
	Display display = this.display;
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
		int /*long*/ parentHandle = shellHandle;
		OS.gtk_widget_realize (parentHandle);
		int /*long*/ window = OS.GTK_WIDGET_WINDOW (parentHandle);
		Rectangle rect = getBounds ();
		GdkWindowAttr attributes = new GdkWindowAttr ();
		attributes.width = rect.width;
		attributes.height = rect.height;
		attributes.event_mask = (0xFFFFFFFF & ~OS.ExposureMask);
		attributes.wclass = OS.GDK_INPUT_ONLY;
		attributes.window_type = OS.GDK_WINDOW_CHILD;
		enableWindow = OS.gdk_window_new (window, attributes, 0);
		if (enableWindow != 0) {
			if (cursor != null) {
				OS.gdk_window_set_cursor (enableWindow, cursor.handle);
				if (!OS.GDK_WINDOWING_X11 ()) {
					OS.gdk_flush ();
				} else {
					int /*long*/ xDisplay = OS.gdk_x11_display_get_xdisplay(OS.gdk_display_get_default());
					OS.XFlush (xDisplay);
				}
			}
			OS.gdk_window_set_user_data (enableWindow, parentHandle);
			OS.gdk_window_show (enableWindow);
		}
	}
	if (fixFocus) fixFocus (control);
	if (enabled && display.activeShell == this) {
		if (!restoreFocus ()) traverseGroup (false);
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
	checkWidget();
	if (fullScreen) {
		OS.gtk_window_fullscreen (shellHandle);
	} else {
		OS.gtk_window_unfullscreen (shellHandle);
		if (maximized) {
			setMaximized (true);
		}
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
	checkWidget();
}

void setInitialBounds () {
	if ((state & FOREIGN_HANDLE) != 0) return;
	int width = OS.gdk_screen_width () * 5 / 8;
	int height = OS.gdk_screen_height () * 5 / 8;
	int /*long*/ screen = OS.gdk_screen_get_default ();
	if (screen != 0) {
		if (OS.gdk_screen_get_n_monitors (screen) > 1) {
			int monitorNumber = OS.gdk_screen_get_monitor_at_window (screen, paintWindow ());
			GdkRectangle dest = new GdkRectangle ();
			OS.gdk_screen_get_monitor_geometry (screen, monitorNumber, dest);
			width = dest.width * 5 / 8;
			height = dest.height * 5 / 8;
		}
	}
	if ((style & SWT.RESIZE) != 0) {
		OS.gtk_window_resize (shellHandle, width, height);
	}
	resizeBounds (width, height, false);
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
		int /*long*/ menuHandle = menuBar.handle;
		OS.gtk_widget_hide (menuHandle);
		destroyAccelGroup ();
	}
	menuBar = menu;
	if (menuBar != null) {
		int /*long*/ menuHandle = menu.handle;
		OS.gtk_widget_show (menuHandle);
		createAccelGroup ();
		menuBar.addAccelerators (accelGroup);
	}
	int width = OS.GTK_WIDGET_WIDTH (vboxHandle);
	int height = OS.GTK_WIDGET_HEIGHT (vboxHandle);
	resizeBounds (width, height, !both);
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
	GdkGeometry geometry = new GdkGeometry ();
	minWidth = geometry.min_width = Math.max (width, trimWidth ()) - trimWidth ();
	minHeight = geometry.min_height = Math.max (height, trimHeight ()) - trimHeight ();
	OS.gtk_window_set_geometry_hints (shellHandle, 0, geometry, OS.GDK_HINT_MIN_SIZE);
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

/*
 * Shells are never labelled by other widgets, so no initialization is needed.
 */
void setRelations() {
}

public void setText (String string) {
	super.setText (string);

	/* 
	* GTK bug 82013.  For some reason, if the title string
	* is less than 7 bytes long and is not terminated by
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
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & mask) != 0) {
		if (visible) {
			display.setModalShell (this);
			OS.gtk_window_set_modal (shellHandle, true);
		} else {
			display.clearModal (this);
			OS.gtk_window_set_modal (shellHandle, false);
		}
	} else {
		updateModal ();
	}
	showWithParent = visible;
	if ((OS.GTK_WIDGET_MAPPED (shellHandle) == visible)) return;
	if (visible) {
		if (center && !moved) {
			center ();
			if (isDisposed ()) return;
		}
		sendEvent (SWT.Show);
		if (isDisposed ()) return;

		/*
		* In order to ensure that the shell is visible
		* and fully painted, dispatch events such as
		* GDK_MAP and GDK_CONFIGURE, until the GDK_MAP
		* event for the shell is received.
		* 
		* Note that if the parent is minimized or withdrawn
		* from the desktop, this should not be done since
		* the shell not will be mapped until the parent is
		* unminimized or shown on the desktop.
		*/
		mapped = false;
		OS.gtk_widget_show (shellHandle);
		if (enableWindow != 0) OS.gdk_window_raise (enableWindow);
		if (isDisposed ()) return;
		if (!OS.GTK_IS_PLUG (shellHandle)) {
			display.dispatchEvents = new int [] {
				OS.GDK_EXPOSE,
				OS.GDK_FOCUS_CHANGE,
				OS.GDK_CONFIGURE,
				OS.GDK_MAP,
				OS.GDK_UNMAP,
				OS.GDK_NO_EXPOSE,
				OS.GDK_VISIBILITY_NOTIFY,
				OS.GDK_WINDOW_STATE 
			};
			Display display = this.display;
			display.putGdkEvents();
			boolean iconic = false;
			Shell shell = parent != null ? parent.getShell() : null;
			do {
				/*
				* This call to gdk_threads_leave() is a temporary work around
				* to avoid deadlocks when gdk_threads_init() is called by native
				* code outside of SWT (i.e AWT, etc). It ensures that the current
				* thread leaves the GTK lock before calling the function below. 
				*/
				OS.gdk_threads_leave();
				OS.g_main_context_iteration (0, false);
				if (isDisposed ()) break;
				iconic = minimized || (shell != null && shell.minimized);
			} while (!mapped && !iconic);
			display.dispatchEvents = null;
			if (isDisposed ()) return;
			if (!iconic) {
				update (true, true);
				if (isDisposed ()) return;
				adjustTrim ();
			}
		}
		mapped = true;

		if ((style & mask) != 0) {
			OS.gdk_pointer_ungrab (OS.GDK_CURRENT_TIME);
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
			oldWidth = size.x - trimWidth ();
			oldHeight = size.y - trimHeight ();
			sendEvent (SWT.Resize);
			if (isDisposed ()) return;
			if (layout != null) {
				markLayout (false, false);
				updateLayout (false);
			}
		}
	} else {
		fixActiveShell ();
		OS.gtk_widget_hide (shellHandle);
		sendEvent (SWT.Hide);
	}
}

void setZOrder (Control sibling, boolean above, boolean fixRelations) {
	/*
	* Bug in GTK+.  Changing the toplevel window Z-order causes
	* X to send a resize event.  Before the shell is mapped, these
	* resize events always have a size of 200x200, causing extra
	* layout work to occur.  The fix is to modify the Z-order only
	* if the shell has already been mapped at least once.
	*/
	/* Shells are never included in labelled-by relations */
	if (mapped) setZOrder (sibling, above, false, false);
}

int /*long*/ shellMapProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ user_data) {
	mapped = true;
	display.dispatchEvents = null;
	return 0;
}

void showWidget () {
	if ((state & FOREIGN_HANDLE) != 0) {
		/*
		* In case of foreign handles, activeShell might not be initialised as 
		* no focusIn events are generated on the window until the window loses
		* and gain focus.
		*/
		if (OS.gtk_window_is_active (shellHandle)) {
			display.activeShell = this;
			display.activePending = true;
		}
		return;
	}
	OS.gtk_container_add (shellHandle, vboxHandle);
	if (scrolledHandle != 0) OS.gtk_widget_show (scrolledHandle);
	if (handle != 0) OS.gtk_widget_show (handle);
	if (vboxHandle != 0) OS.gtk_widget_show (vboxHandle);
}

int /*long*/ sizeAllocateProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ user_data) {
	int offset = 16;
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_pointer (0, x, y, null);
	y [0] += offset;
	int /*long*/ screen = OS.gdk_screen_get_default ();
	if (screen != 0) {
		int monitorNumber = OS.gdk_screen_get_monitor_at_point (screen, x[0], y[0]);
		GdkRectangle dest = new GdkRectangle ();
		OS.gdk_screen_get_monitor_geometry (screen, monitorNumber, dest);
		int width = OS.GTK_WIDGET_WIDTH (handle);
		int height = OS.GTK_WIDGET_HEIGHT (handle);
		if (x[0] + width > dest.x + dest.width) {
			x [0] = (dest.x + dest.width) - width;
		}
		if (y[0] + height > dest.y + dest.height) {
			y[0] = (dest.y + dest.height) - height;
		}
	} 
	OS.gtk_window_move (handle, x [0], y [0]);
	return 0;
}

int /*long*/ sizeRequestProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ user_data) {
	OS.gtk_widget_hide (handle);
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
	if (fullScreen) return 0;
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
	if (fullScreen) return 0;
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

void updateModal () {
	if (OS.GTK_IS_PLUG (shellHandle)) return;
	int /*long*/ group = 0;
	boolean isModalShell = false;
	if (display.getModalDialog () == null) {
		Shell modal = getModalShell ();
		int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
		Composite shell = null;
		if (modal == null) {
			if ((style & mask) != 0) {
				shell = this;
				/*
				* Feature in GTK. If a modal shell is reassigned to
				* a different group, then it's modal state is not.
				* persisted against the new group. 
				* The fix is to reset the modality before it is changed
				* into a different group and then, set back after it
				* assigned into new group.
				*/
				isModalShell = OS.gtk_window_get_modal (shellHandle);
				if (isModalShell) OS.gtk_window_set_modal (shellHandle, false);
			}
		} else {
			shell = modal;
		}
		Composite topModalShell = shell;
		while (shell != null) {
			if ((shell.style & mask) == 0) {
				group = shell.getShell ().group;
				break;
			}
			topModalShell = shell;
			shell = shell.parent;
		}
		/*
		* If a modal shell doesn't have any parent (or modal shell as it's parent), 
		* then we incorrectly add the modal shell to the default group, due to which 
		* children of that modal shell are not interactive. The fix is to ensure 
		* that whenever there is a modal shell in the hierarchy, then we always
		* add the modal shell's group to that modal shell and it's modelless children
		* in a different group.
		*/
		if (group == 0 && topModalShell != null) group = topModalShell.getShell ().group;
	}
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0) && group == 0) { 
		/*
		* Feature in GTK. Starting with GTK version 2.10, GTK
		* doesn't assign windows to a default group. The fix is to
		* get the handle of the default group and add windows to the
		* group.
		*/
		group = OS.gtk_window_get_group(0);
	}
	if (group != 0) {
		OS.gtk_window_group_add_window (group, shellHandle);
		if (isModalShell) OS.gtk_window_set_modal (shellHandle, true);
	} else {
		if (modalGroup != 0) {
			OS.gtk_window_group_remove_window (modalGroup, shellHandle);
		}
	}
	modalGroup = group;
}

void updateMinimized (boolean minimized) {
	Shell[] shells = getShells ();
	for (int i = 0; i < shells.length; i++) {
		boolean update = false;
		Shell shell = shells[i];
		while (shell != null && shell != this && !shell.isUndecorated ()) {
			shell = (Shell) shell.getParent ();
		}
		if (shell != null && shell != this) update = true;
		if (update) {
			if (minimized) {
				if (shells[i].isVisible ()) {
					shells[i].showWithParent = true;
					OS.gtk_widget_hide(shells[i].shellHandle);
				}
			} else {
				if (shells[i].showWithParent) {
					shells[i].showWithParent = false;
					OS.gtk_widget_show(shells[i].shellHandle);
				}
			}
		}
	}
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
	fixActiveShell ();
	OS.gtk_widget_hide (shellHandle);
	super.dispose ();
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
	bringToTop (true);
}

public Rectangle getBounds () {
	checkWidget ();
	int [] x = new int [1], y = new int [1];
	OS.gtk_window_get_position (shellHandle, x, y);
	int width = OS.GTK_WIDGET_WIDTH (vboxHandle);
	int height = OS.GTK_WIDGET_HEIGHT (vboxHandle);
	int border = 0;
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0) {
		border = OS.gtk_container_get_border_width (shellHandle);
	}
	return new Rectangle (x [0], y [0], width + trimWidth () + 2*border, height + trimHeight () + 2*border);
}

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

void releaseChildren (boolean destroy) {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (shell != null && !shell.isDisposed ()) {
			shell.release (false);
		}
	}
	if (toolTips != null) {
		for (int i=0; i<toolTips.length; i++) {
			ToolTip toolTip = toolTips [i];
			if (toolTip != null && !toolTip.isDisposed ()) {
				toolTip.dispose ();
			}
		}
		toolTips = null;
	}
	super.releaseChildren (destroy);
}

void releaseWidget () {
	super.releaseWidget ();
	destroyAccelGroup ();
	display.clearModal (this);
	if (display.activeShell == this) display.activeShell = null;
	if (tooltipsHandle != 0) OS.g_object_unref (tooltipsHandle);
	tooltipsHandle = 0;
	if (group != 0) OS.g_object_unref (group);
	group = modalGroup = 0;
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (shellHandle);
	OS.gdk_window_remove_filter(window, display.filterProc, shellHandle);
	lastActive = null;
}

void setToolTipText (int /*long*/ tipWidget, String string) {
	setToolTipText (tipWidget, tipWidget, string);
}

void setToolTipText (int /*long*/ rootWidget, int /*long*/ tipWidget, String string) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 12, 0)) {
		byte [] buffer = null;
		if (string != null && string.length () > 0) {
			char [] chars = fixMnemonic (string, false);
			buffer = Converter.wcsToMbcs (null, chars, true);
		}
		int /*long*/ oldTooltip = OS.gtk_widget_get_tooltip_text (rootWidget);
		boolean same = false;
		if (buffer == null && oldTooltip == 0) {
			same = true;
		} else if (buffer != null && oldTooltip != 0) {
			same = OS.strcmp (oldTooltip, buffer) == 0;
		}
		if (oldTooltip != 0) OS.g_free(oldTooltip);
		if (same) return;

		int /*long*/ eventPtr = 0;
		if (OS.GTK_VERSION < OS.VERSION (2, 18, 0)) {
			OS.gtk_widget_set_tooltip_text (rootWidget, null);
			/*
			 * Bug in GTK. In GTK 2.12, due to a miscalculation of window
			 * coordinates, using gtk_tooltip_trigger_tooltip_query ()
			 * to update an existing a toboltip will result in the tooltip 
			 * being displayed at a wrong position. The fix is to send out 
			 * 2 fake GDK_MOTION_NOTIFY events (to mimic the GTK call) which 
			 * contain the proper x and y coordinates.
			 */
			int /*long*/ tipWindow = gtk_widget_get_window (rootWidget);
			if (tipWindow != 0) {
				int [] x = new int [1], y = new int [1];
				int /*long*/ window = OS.gdk_window_at_pointer (x, y);
				int /*long*/ [] user_data = new int /*long*/ [1];
				if (window != 0) OS.gdk_window_get_user_data (window, user_data);
				if (tipWidget == user_data [0]) {
					eventPtr = OS.gdk_event_new (OS.GDK_MOTION_NOTIFY);
					GdkEventMotion event = new GdkEventMotion ();
					event.type = OS.GDK_MOTION_NOTIFY;
					event.window = OS.g_object_ref (tipWindow);
					event.x = x [0];
					event.y = y [0];
					OS.gdk_window_get_origin (window, x, y);
					event.x_root = event.x + x [0];
					event.y_root = event.y + y [0];
					OS.memmove (eventPtr, event, GdkEventMotion.sizeof);
					OS.gtk_main_do_event (eventPtr);
				}
			}
		}
		OS.gtk_widget_set_tooltip_text (rootWidget, buffer);
		if (eventPtr != 0) {
			OS.gtk_main_do_event (eventPtr);
			OS.gdk_event_free (eventPtr);
		}
	} else {
		byte [] buffer = null;
		if (string != null && string.length () > 0) {
			char [] chars = fixMnemonic (string, false);
			buffer = Converter.wcsToMbcs (null, chars, true);
		}
		int /*long*/ tipData = OS.gtk_tooltips_data_get(tipWidget);
		if (tipData != 0) {
			int /*long*/ oldTooltip = OS.GTK_TOOLTIPS_GET_TIP_TEXT(tipData);
			if (string == null && oldTooltip == 0) {
				return;
			} else if (string != null && oldTooltip != 0) {
				if (buffer != null) {
					if (OS.strcmp (oldTooltip, buffer) == 0) return;
				}
			}
		}
		if (tooltipsHandle == 0) {
			tooltipsHandle = OS.gtk_tooltips_new ();
			if (tooltipsHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.g_object_ref (tooltipsHandle);
			OS.gtk_object_sink (tooltipsHandle);
		}
	
		/*
		* Feature in GTK.  There is no API to position a tooltip.
		* The fix is to connect to the size_allocate signal for
		* the tooltip window and position it before it is mapped.
		*
		* Bug in Solaris-GTK.  Invoking gtk_tooltips_force_window()
		* can cause a crash in older versions of GTK.  The fix is
		* to avoid this call if the GTK version is older than 2.2.x.
		* The call is to be avoided on GTK versions newer than 2.12.0
		* where it's deprecated.
		*/
		OS.gtk_tooltips_force_window (tooltipsHandle);
		int /*long*/ tipWindow = OS.GTK_TOOLTIPS_TIP_WINDOW (tooltipsHandle);
		if (tipWindow != 0 && tipWindow != tooltipWindow) {
			OS.g_signal_connect (tipWindow, OS.size_allocate, display.sizeAllocateProc, shellHandle);
			tooltipWindow = tipWindow;
		}
		
		/*
		* Bug in GTK.  If the cursor is inside the window when a new
		* tooltip is set and the old tooltip is hidden, the new tooltip
		* is not displayed until the mouse re-enters the window.  The
		* fix is force the new tooltip to be active.
		*/
		boolean set = true;
		if (tipWindow != 0) {
			if ((OS.GTK_WIDGET_FLAGS (tipWidget) & (OS.GTK_REALIZED | OS.GTK_VISIBLE)) != 0) {
				int [] x = new int [1], y = new int [1];
				int /*long*/ window = OS.gdk_window_at_pointer (x, y);
				if (window != 0) {
					int /*long*/ [] user_data = new int /*long*/ [1];
					OS.gdk_window_get_user_data (window, user_data);
					if (tipWidget == user_data [0]) {
						/* 
						* Feature in GTK.  Calling gtk_tooltips_set_tip() positions and
						* shows the tooltip.  If the tooltip is already visible, moving
						* it to a new location in the size_allocate signal causes flashing.
						* The fix is to hide the tip window in the size_request signal
						* and before the new tooltip is forced to be active. 
						*/
						set = false;
						int handler_id = OS.g_signal_connect (tipWindow, OS.size_request, display.sizeRequestProc, shellHandle);
						OS.gtk_tooltips_set_tip (tooltipsHandle, tipWidget, buffer, null);
						OS.gtk_widget_hide (tipWindow);
						int /*long*/ data = OS.gtk_tooltips_data_get (tipWidget);
						OS.GTK_TOOLTIPS_SET_ACTIVE (tooltipsHandle, data);
						OS.gtk_tooltips_set_tip (tooltipsHandle, tipWidget, buffer, null);
						if (handler_id != 0) OS.g_signal_handler_disconnect (tipWindow, handler_id);
					}
				}
			}
		}
		if (set) OS.gtk_tooltips_set_tip (tooltipsHandle, tipWidget, buffer, null);
	}
		
}
}

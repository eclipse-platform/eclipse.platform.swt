/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
 * minimized or normal states:</p>
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
 * <p>
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
 * downgraded to <code>APPLICATION_MODAL</code>.</p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_MOVE, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL, SHEET</dd>
 * <dd>APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Activate, Close, Deactivate, Deiconify, Iconify</dd>
 * </dl>
 * <p>
 * Class <code>SWT</code> provides two "convenience constants"
 * for the most commonly required style combinations:</p>
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
	long shellHandle, tooltipsHandle, tooltipWindow, group, modalGroup;
	boolean mapped, moved, resized, opened, fullScreen, showWithParent, modified, center;
	int oldX, oldY, oldWidth, oldHeight;
	GeometryInterface geometry;
	Control lastActive;
	ToolTip [] toolTips;
	boolean ignoreFocusOut, ignoreFocusIn;
	boolean ignoreFocusOutAfterGrab, grabbedFocus;
	Region originalRegion;

	static final int MAXIMUM_TRIM = 128;
	static final int BORDER = 3;

	static final double SHELL_TO_MONITOR_RATIO = 0.625; // Fractional: 5 / 8

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
 * @see SWT#NO_MOVE
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
 * @see SWT#NO_MOVE
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

Shell (Display display, Shell parent, int style, long handle, boolean embedded) {
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
	if(!GTK.GTK4) {
		geometry = new GdkGeometry();
	}
	else {
		geometry = new SWTGeometry();
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
 * @see SWT#NO_MOVE
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
 * Invokes platform specific functionality to allocate a new shell
 * that is embedded.
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
public static Shell gtk_new (Display display, long handle) {
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
public static Shell internal_new (Display display, long handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle, false);
}

static int checkStyle (Shell parent, int style) {
	style = Decorations.checkStyle (style);
	style &= ~SWT.TRANSPARENT;
	if (parent != null && (style & SWT.ON_TOP) != 0) style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX);
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
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (shellHandle, allocation);
	int width = allocation.width;
	int height = allocation.height;
	GdkRectangle rect = new GdkRectangle ();

	if (!GTK.GTK4) {
		long window = gtk_widget_get_window (shellHandle);
		GDK.gdk_window_get_frame_extents (window, rect);
	}
	int trimWidth = Math.max (0, rect.width - width);
	int trimHeight = Math.max (0, rect.height - height);
	/*
	* Bug in GTK.  gdk_window_get_frame_extents() fails for various window
	* managers, causing a large incorrect value to be returned as the trim.
	* The fix is to ignore the returned trim values if they are too large.
	*
	* Additionally, ignore trim for Shells with SWT.RESIZE and SWT.ON_TOP set.
	* See bug 319612.
	*/
	if (trimWidth > MAXIMUM_TRIM || trimHeight > MAXIMUM_TRIM || isCustomResize()) {
		display.ignoreTrim = true;
		return;
	}
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	if ((style & SWT.NO_TRIM) == 0) {
		hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
		hasResize = (style & SWT.RESIZE) != 0;
		hasBorder = (style & SWT.BORDER) != 0;
	}
	int trimStyle;
	if (hasTitle) {
		if (hasResize) {
			trimStyle = Display.TRIM_TITLE_RESIZE;
		} else if (hasBorder) {
			trimStyle = Display.TRIM_TITLE_BORDER;
		} else {
			trimStyle = Display.TRIM_TITLE;
		}
	} else if (hasResize) {
		trimStyle = Display.TRIM_RESIZE;
	} else if (hasBorder) {
		trimStyle = Display.TRIM_BORDER;
	} else {
		trimStyle = Display.TRIM_NONE;
	}
	Rectangle bounds = getBoundsInPixels();
	int widthAdjustment = display.trimWidths[trimStyle] - trimWidth;
	int heightAdjustment = display.trimHeights[trimStyle] - trimHeight;
	if (widthAdjustment == 0 && heightAdjustment == 0) return;

	bounds.width += widthAdjustment;
	bounds.height += heightAdjustment;
	oldWidth += widthAdjustment;
	oldHeight += heightAdjustment;
	if (!getMaximized()) {
		resizeBounds (width + widthAdjustment, height + heightAdjustment, false);
	}
	display.trimWidths[trimStyle] = trimWidth;
	display.trimHeights[trimStyle] = trimHeight;
}

void bringToTop (boolean force) {
	if (!GTK.gtk_widget_get_visible (shellHandle)) return;
	Display display = this.display;
	Shell activeShell = display.activeShell;
	if (activeShell == this) return;
	if (!force) {
		if (activeShell == null) return;
		if (!display.activePending) {
			long focusHandle = GTK.gtk_window_get_focus (activeShell.shellHandle);
			if (focusHandle != 0 && !GTK.gtk_widget_has_focus (focusHandle)) return;
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
		display.activeShell = null;
		display.activePending = true;
	}
	/*
	* Feature in GTK.  When the shell is an override redirect
	* window, gdk_window_focus() does not give focus to the
	* window.  The fix is to use XSetInputFocus() to force
	* the focus, or gtk_grab_add() for GTK > 3.20.
	*/
	long gdkResource;
	if (GTK.GTK4) {
		gdkResource = gtk_widget_get_surface (shellHandle);
	} else {
		gdkResource = gtk_widget_get_window (shellHandle);
	}
	if ((xFocus || (style & SWT.ON_TOP) != 0)) {
		if (OS.isX11()) {
			long gdkDisplay = GDK.gdk_window_get_display(gdkResource);
			long xDisplay = GDK.gdk_x11_display_get_xdisplay(gdkDisplay);
			long xWindow;
			if (GTK.GTK4) {
				xWindow = GDK.gdk_x11_surface_get_xid(gdkResource);
			} else {
				xWindow = GDK.gdk_x11_window_get_xid (gdkResource);
			}

			GDK.gdk_x11_display_error_trap_push(gdkDisplay);
			/* Use CurrentTime instead of the last event time to ensure that the shell becomes active */
			OS.XSetInputFocus (xDisplay, xWindow, OS.RevertToParent, OS.CurrentTime);
			GDK.gdk_x11_display_error_trap_pop_ignored(gdkDisplay);
		} else {
			long gdkDisplay;
			if (GTK.GTK4) {
				gdkDisplay = GDK.gdk_surface_get_display(gdkResource);
			} else {
				GTK3.gtk_grab_add(shellHandle);
				gdkDisplay = GDK.gdk_window_get_display(gdkResource);
			}
			long seat = GDK.gdk_display_get_default_seat(gdkDisplay);
			if (GTK.GTK4) {
				/* TODO: GTK does not provide a gdk_surface_show, probably will require use of the present api */
			} else {
				GDK.gdk_window_show(gdkResource);
			}
			GDK.gdk_seat_grab(seat, gdkResource, GDK.GDK_SEAT_CAPABILITY_ALL, true, 0, 0, 0, 0);
			/*
			 * Bug 541185: Hover over to open Javadoc popup will make the popup
			 * close instead of gaining focus due to an extra focus out signal sent
			 * after grabbing focus. This triggers SWT.Deactivate handler which closes the shell.
			 * Workaround is to ignore this focus out.
			 */
			grabbedFocus = true;
			ignoreFocusOutAfterGrab = true;
		}
	} else {
		if (GTK.GTK4) {
			GTK4.gdk_toplevel_focus (gdkResource, display.lastUserEventTime);
		} else {
			GDK.gdk_window_focus (gdkResource, display.lastUserEventTime);
		}
	}
	display.activeShell = this;
	display.activePending = true;
}

void center () {
	if (parent == null) return;
	Rectangle rect = getBoundsInPixels ();
	Rectangle parentRect = display.mapInPixels (parent, null, parent.getClientAreaInPixels());
	int x = Math.max (parentRect.x, parentRect.x + (parentRect.width - rect.width) / 2);
	int y = Math.max (parentRect.y, parentRect.y + (parentRect.height - rect.height) / 2);
	Rectangle monitorRect = DPIUtil.autoScaleUp(parent.getMonitor ().getClientArea());
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
	setLocationInPixels (x, y);
}

@Override
void checkBorder () {
	/* Do nothing */
}

@Override
void checkOpen () {
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
void closeWidget () {
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

@Override
Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrimInPixels (x, y, width, height);
	int border = 0;
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0 || isCustomResize()) {
		border = gtk_container_get_border_width_or_margin (shellHandle);
	}
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	trim.x -= (trimWidth / 2) + border;
	trim.y -= trimHeight - (trimWidth / 2) + border;
	trim.width += trimWidth + border * 2;
	trim.height += trimHeight + border * 2;
	if (menuBar != null) {
		forceResize ();
		GtkAllocation allocation = new GtkAllocation ();
		GTK.gtk_widget_get_allocation (menuBar.handle, allocation);
		int menuBarHeight = allocation.height;
		trim.y -= menuBarHeight;
		trim.height += menuBarHeight;
	}
	return trim;
}

@Override
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	if (shellHandle == 0) {
		boolean isChildShell = parent != null;

		if (handle == 0) {
			int type = GTK.GTK_WINDOW_TOPLEVEL;
			if (isChildShell && (style & SWT.ON_TOP) != 0) type = GTK.GTK_WINDOW_POPUP;
			if (GTK.GTK4) {
				// TODO: GTK4 need to handle for GTK_WINDOW_POPUP type
				shellHandle = GTK4.gtk_window_new();
			} else {
				shellHandle = GTK3.gtk_window_new(type);
			}
		} else {
			shellHandle = GTK.gtk_plug_new(handle);
		}
		if (shellHandle == 0) error(SWT.ERROR_NO_HANDLES);

		if (isChildShell) {
			if (GTK.GTK4) {
				GTK.gtk_window_set_transient_for(shellHandle, parent.topHandle());
				GTK.gtk_window_set_destroy_with_parent(shellHandle, true);

				// TODO: GTK4 need case for SWT.MIN
			} else {
				/*
				 * Problems with GTK_WINDOW_POPUP attached to another GTK_WINDOW_POPUP parent
				 * 1) Bug 530138: GTK_WINDOW_POPUP attached to a GTK_WINDOW_POPUP parent
				 * gets positioned relatively to the GTK_WINDOW_POPUP. We want to position it
				 * relatively to the GTK_WINDOW_TOPLEVEL surface. Fix is to set the child popup's transient
				 * parent to the top level window.
				 *
				 * 2) Bug 540166: When a parent popup is destroyed, the child popup sometimes does not
				 * get destroyed and is stuck until its transient top level parent gets destroyed.
				 * Fix is to implement a similar gtk_window_set_destroy_with_parent with its *logical*
				 * parent by connecting a "destroy" signal.
				 */
				if (OS.isWayland()) {
					Composite topLevelParent = parent;
					while (topLevelParent != null && (topLevelParent.style & SWT.ON_TOP) != 0) {
						topLevelParent = parent.getParent();
					}
					// transient parent must be the a toplevel window to position correctly
					if (topLevelParent != null) {
						GTK.gtk_window_set_transient_for(shellHandle, topLevelParent.topHandle());
					} else {
						GTK.gtk_window_set_transient_for(shellHandle, parent.topHandle());
					}
					// this marks the logical parent
					GTK3.gtk_window_set_attached_to(shellHandle, parent.topHandle());
					// implements the gtk_window_set_destroy_with_parent for the *logical* parent
					if (parent != topLevelParent && isMappedToPopup()) {
						parent.popupChild = this;
					}
				} else {
					GTK.gtk_window_set_transient_for(shellHandle, parent.topHandle ());
				}
				GTK.gtk_window_set_destroy_with_parent(shellHandle, true);
				// if child shells are minimizable, we want them to have a
				// taskbar icon, so they can be unminimized
				if ((style & SWT.MIN) == 0) {
					GTK3.gtk_window_set_skip_taskbar_hint(shellHandle, true);
				}
			}
		} else if ((style & SWT.ON_TOP) != 0) {
			/*
			 * gtk_window_set_keep_above is not available in GTK 4.
			 * No replacements were provided. GTK dev suggestion is
			 * to use platform-specific API if this functionality
			 * is necessary.
			 */

			if(!GTK.GTK4)GTK3.gtk_window_set_keep_above(shellHandle, true);
		}

		GTK.gtk_window_set_title(shellHandle, new byte[1]);
		if ((style & SWT.RESIZE) != 0) {
			GTK.gtk_window_set_resizable(shellHandle, true);
		} else {
			GTK.gtk_window_set_resizable(shellHandle, false);
		}
		if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0) {
			gtk_container_set_border_width(shellHandle, 1);
		}
		if ((style & SWT.TOOL) != 0) {
			GTK3.gtk_window_set_type_hint(shellHandle, GDK.GDK_WINDOW_TYPE_HINT_UTILITY);
		}
		if ((style & SWT.NO_TRIM) != 0) {
			GTK.gtk_window_set_decorated(shellHandle, false);
		}
		/*
		 * On Wayland, shells with no SHELL_TRIM style specified have decorations by default.
		 * This fixes the issue that the open editor dialog (Ctrl+E) for Eclipse on Wayland
		 * has a tile bar on top.
		 */
		if (!OS.isX11() && (style & SWT.SHELL_TRIM) == 0) {
			GTK.gtk_window_set_decorated(shellHandle, false);
		}
		if (isCustomResize()) {
			gtk_container_set_border_width(shellHandle, BORDER);
		}
	}

	createHandle (index, false, true);
	vboxHandle = gtk_box_new(GTK.GTK_ORIENTATION_VERTICAL, false, 0);
	if (vboxHandle == 0) error(SWT.ERROR_NO_HANDLES);
	if (GTK.GTK4) {
		GTK4.gtk_box_append(vboxHandle, scrolledHandle);
	} else {
		GTK3.gtk_container_add(vboxHandle, scrolledHandle);
		gtk_box_set_child_packing(vboxHandle, scrolledHandle, true, true, 0, GTK.GTK_PACK_END);
	}

	group = GTK.gtk_window_group_new();
	if (group == 0) error(SWT.ERROR_NO_HANDLES);

	/*
	* Feature in GTK.  Realizing the shell triggers a size allocate event,
	* which may be confused for a resize event from the window manager if
	* received too late.  The fix is to realize the window during creation
	* to avoid confusion.
	*/
	GTK.gtk_widget_realize(shellHandle);
}

@Override
long filterProc (long xEvent, long gdkEvent, long data2) {
	if (OS.isWayland()) return 0;
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
						display.activeShell = this;
						display.activePending = false;
						sendEvent (SWT.Activate);
						if (isDisposed ()) return 0;
						if (isCustomResize ()) {
							GDK.gdk_window_invalidate_rect (gtk_widget_get_window (shellHandle), null, false);
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
						Display display = this.display;
						sendEvent (SWT.Deactivate);
						setActiveControl (null);
						if (display.activeShell == this) {
							display.activeShell = null;
							display.activePending = false;
						}
						if (isDisposed ()) return 0;
						if (isCustomResize ()) {
							GDK.gdk_window_invalidate_rect (gtk_widget_get_window (shellHandle), null, false);
						}
						break;
				}
			}
			break;
	}
	return 0;
}

@Override
Control findBackgroundControl () {
	return (state & BACKGROUND) != 0 || backgroundImage != null ? this : null;
}

@Override
Composite findDeferredControl () {
	return layoutCount > 0 ? this : null;
}

/**
 * Returns a ToolBar object representing the tool bar that can be shown in the receiver's
 * trim. This will return <code>null</code> if the platform does not support tool bars that
 * are not part of the content area of the shell, or if the Shell's style does not support
 * having a tool bar.
 *
 * @return a ToolBar object representing the Shell's tool bar, or <code>null</code>.
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

@Override
boolean hasBorder () {
	return false;
}

@Override
void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect (shellHandle, OS.dpi_changed, display.notifyProc, Widget.DPI_CHANGED);

	if (GTK.GTK4) {
		// TODO: GTK4 see if same signals are required in GTK4 as in GTK3, if so, will require the legacy event controller
		// Replaced "window-state-event" with GdkSurface "notify::state", pass shellHandle as user_data
		GTK.gtk_widget_realize(shellHandle);
		long gdkSurface = gtk_widget_get_surface (shellHandle);
		OS.g_signal_connect (gdkSurface, OS.notify_state, display.notifyProc, shellHandle);
		OS.g_signal_connect (gdkSurface, OS.compute_size, display.computeSizeProc, shellHandle);
		OS.g_signal_connect(shellHandle, OS.notify_default_height, display.notifyProc, Widget.NOTIFY_DEFAULT_HEIGHT);
		OS.g_signal_connect(shellHandle, OS.notify_default_width, display.notifyProc, Widget.NOTIFY_DEFAULT_WIDTH);
		OS.g_signal_connect(shellHandle, OS.notify_maximized, display.notifyProc, Widget.NOTIFY_MAXIMIZED);
	} else {
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [WINDOW_STATE_EVENT], 0, display.getClosure (WINDOW_STATE_EVENT), false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [CONFIGURE_EVENT], 0, display.getClosure (CONFIGURE_EVENT), false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [MAP_EVENT], 0, display.shellMapProcClosure, false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [SIZE_ALLOCATE], 0, display.getClosure (SIZE_ALLOCATE), false);
	}
	if (GTK.GTK4) {
		OS.g_signal_connect_closure (shellHandle, OS.close_request, display.getClosure (CLOSE_REQUEST), false);
		OS.g_signal_connect(shellHandle, OS.notify_is_active, display.windowActiveProc, FOCUS_IN);
		long keyController = GTK4.gtk_event_controller_key_new();
		GTK4.gtk_widget_add_controller(shellHandle, keyController);
		GTK.gtk_event_controller_set_propagation_phase(keyController, GTK.GTK_PHASE_TARGET);
		OS.g_signal_connect (keyController, OS.key_pressed, display.keyPressReleaseProc, KEY_PRESSED);

		long focusController = GTK4.gtk_event_controller_focus_new();
		GTK4.gtk_widget_add_controller(shellHandle, focusController);
		OS.g_signal_connect(focusController, OS.enter, display.focusProc, FOCUS_IN);
		OS.g_signal_connect(focusController, OS.leave, display.focusProc, FOCUS_OUT);

		long enterLeaveController = GTK4.gtk_event_controller_motion_new();
		GTK4.gtk_widget_add_controller(shellHandle, enterLeaveController);

		long enterMotionAddress = display.enterMotionCallback.getAddress();
		OS.g_signal_connect (enterLeaveController, OS.enter, enterMotionAddress, ENTER);
		if (isCustomResize()) {
			long motionController = GTK4.gtk_event_controller_motion_new();
			GTK4.gtk_widget_add_controller(shellHandle, motionController);
			GTK.gtk_event_controller_set_propagation_phase(motionController, GTK.GTK_PHASE_TARGET);

			OS.g_signal_connect (motionController, OS.motion, enterMotionAddress, MOTION);
			long leaveAddress = display.leaveCallback.getAddress();
			OS.g_signal_connect (enterLeaveController, OS.leave, leaveAddress, LEAVE);
		}
	} else {
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [DELETE_EVENT], 0, display.getClosure (DELETE_EVENT), false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [KEY_PRESS_EVENT], 0, display.getClosure (KEY_PRESS_EVENT), false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [FOCUS_IN_EVENT], 0, display.getClosure (FOCUS_IN_EVENT), false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [FOCUS_OUT_EVENT], 0, display.getClosure (FOCUS_OUT_EVENT), false);
		OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.getClosure (ENTER_NOTIFY_EVENT), false);
	}
	OS.g_signal_connect_closure (shellHandle, OS.move_focus, display.getClosure (MOVE_FOCUS), false);
	if (isCustomResize ()) {
		if (!GTK.GTK4) {
			int mask = GDK.GDK_POINTER_MOTION_MASK | GDK.GDK_BUTTON_RELEASE_MASK | GDK.GDK_BUTTON_PRESS_MASK |  GDK.GDK_ENTER_NOTIFY_MASK | GDK.GDK_LEAVE_NOTIFY_MASK;
			GTK3.gtk_widget_add_events (shellHandle, mask);
			OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.getClosure (MOTION_NOTIFY_EVENT), false);
			OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [LEAVE_NOTIFY_EVENT], 0, display.getClosure (LEAVE_NOTIFY_EVENT), false);

			OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [EXPOSE_EVENT], 0, display.getClosure (EXPOSE_EVENT), false);
			OS.g_signal_connect_closure_by_id (shellHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.getClosure (BUTTON_PRESS_EVENT), false);
		}
	}
}

@Override
public boolean isEnabled () {
	checkWidget ();
	return getEnabled ();
}

boolean isUndecorated () {
	return
		(style & (SWT.SHELL_TRIM | SWT.BORDER)) == SWT.NONE ||
		(style & (SWT.NO_TRIM | SWT.ON_TOP)) != 0;
}

/**
 * Determines whether a Shell has both SWT.RESIZE and SWT.ON_TOP set without SWT.NO_TRIM.
 *
 * @return true if this Shell has both SWT.RESIZE and SWT.ON_TOP set without
 * SWT.NO_TRIM, false otherwise.
 */
boolean isCustomResize () {
	return (style & SWT.NO_TRIM) == 0 && (style & (SWT.RESIZE | SWT.ON_TOP)) == (SWT.RESIZE | SWT.ON_TOP);
}

@Override
public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

/**
 * Determines whether a Shell's parent is a popup window. See bug 534554.
 *
 * @return true if the parent of this Shell has style SWT.ON_TOP, false otherwise.
 */
boolean isMappedToPopup () {
	return parent != null && (parent.style & SWT.ON_TOP) != 0;
}

@Override
void register () {
	super.register ();
	display.addWidget (shellHandle, this);
}

@Override
void releaseParent () {
	/* Do nothing */
}

@Override
public void requestLayout () {
	layout (null, SWT.DEFER);
}

@Override
long topHandle () {
	return shellHandle;
}

@Override
long paintHandle () {
	if (GTK.GTK4) {
		return handle;
	} else {
		return super.paintHandle();
	}
}

void fixActiveShell () {
	// Only fix shell for SWT.ON_TOP set, see bug 568550
	if (display.activeShell == this && (style & SWT.ON_TOP) != 0) {
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

@Override
void fixStyle (long handle) {
}

@Override
void forceResize () {
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (vboxHandle, allocation);
	if (OS.isWayland()) {
		/*
		 * Bug 540163: We sometimes are getting the container's allocation
		 * before Shell is fully opened, which gets an incorrect allocation.
		 * Fix is to use the calculated box width/height if bounds have been set.
		 */
		int border = gtk_container_get_border_width_or_margin (shellHandle);
		int boxWidth = oldWidth - 2*border;
		int boxHeight = oldHeight - 2*border;
		if (boxWidth != allocation.width || boxHeight != allocation.height) {
			allocation.width = Math.max(boxWidth, allocation.width);
			allocation.height = Math.max(boxHeight, allocation.height);
		}
	}
	forceResize (allocation.width, allocation.height);
}

void forceResize (int width, int height) {
	int clientWidth = 0;
	if ((style & SWT.MIRRORED) != 0) clientWidth = getClientWidth ();
	GtkAllocation allocation = new GtkAllocation ();
	int border = gtk_container_get_border_width_or_margin (shellHandle);
	allocation.x = border;
	allocation.y = border;
	allocation.width = width;
	allocation.height = height;
	// Call gtk_widget_get_preferred_size() on GTK 3.20+ to prevent warnings.
	// See bug 486068.
	GtkRequisition minimumSize = new GtkRequisition ();
	GtkRequisition naturalSize = new GtkRequisition ();
	GTK.gtk_widget_get_preferred_size (vboxHandle, minimumSize, naturalSize);

	/*
	 * Bug 535075, 536153: On Wayland, we need to set the position of the GtkBox container
	 * relative to the shellHandle to prevent window contents rendered with offset.
	 */
	if (OS.isWayland()) {
		if (GTK.GTK4) {
			double[] window_offset_x = new double[1], window_offset_y = new double[1];
			boolean validTranslation = GTK4.gtk_widget_translate_coordinates(vboxHandle, shellHandle, 0, 0, window_offset_x, window_offset_y);

			if (validTranslation && !isMappedToPopup()) {
				allocation.x += window_offset_x[0];
				allocation.y += window_offset_y[0];
				allocation.height -= window_offset_y[0];
			}
		} else {
			int [] dest_x = new int[1];
			int [] dest_y = new int[1];
			GTK3.gtk_widget_translate_coordinates(vboxHandle, shellHandle, 0, 0, dest_x, dest_y);
			if (dest_x[0] != -1 && dest_y[0] != -1 && !isMappedToPopup()) {
				allocation.x += dest_x[0];
				allocation.y += dest_y[0];
			}
		}
	}

	gtk_widget_size_allocate(vboxHandle, allocation, -1);

	if ((style & SWT.MIRRORED) != 0) moveChildren(clientWidth);
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
	boolean composited;
	if (GTK.GTK4) {
		long display = GDK.gdk_display_get_default();
		composited = GDK.gdk_display_is_composited(display);
	} else {
		long screen = GTK3.gtk_widget_get_screen(shellHandle);
		composited = GDK.gdk_screen_is_composited(screen);
	}
	if (composited) {
		return (int) (GTK.gtk_widget_get_opacity(shellHandle) * 255);
	}
	return 255;
}

int getResizeMode (double x, double y) {
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (shellHandle, allocation);
	int width = allocation.width;
	int height = allocation.height;
	int border = gtk_container_get_border_width_or_margin (shellHandle);
	int mode = 0;
	if (y >= height - border) {
		mode = SWT.CURSOR_SIZES;
		if (x >= width - border - 16) mode = SWT.CURSOR_SIZESE;
		else if (x <= border + 16) mode = SWT.CURSOR_SIZESW;
	} else if (x >= width - border) {
		mode = SWT.CURSOR_SIZEE;
		if (y >= height - border - 16) mode = SWT.CURSOR_SIZESE;
		else if (y <= border + 16) mode = SWT.CURSOR_SIZENE;
	} else if (y <= border) {
		mode = SWT.CURSOR_SIZEN;
		if (x <= border + 16) mode = SWT.CURSOR_SIZENW;
		else if (x >= width - border - 16) mode = SWT.CURSOR_SIZENE;
	} else if (x <= border) {
		mode = SWT.CURSOR_SIZEW;
		if (y <= border + 16) mode = SWT.CURSOR_SIZENW;
		else if (y >= height - border - 16) mode = SWT.CURSOR_SIZESW;
	}
	return mode;
}

/**
 * Returns <code>true</code> if the receiver is currently
 * in fullscreen state, and false otherwise.
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

@Override
Point getLocationInPixels () {
	checkWidget ();
	// Bug in GTK: when shell is moved and then hidden, its location does not get updated.
	// Move it before getting its location.
	if (!getVisible() && moved) {
		setLocationInPixels(oldX, oldY);
	}
	int [] x = new int [1], y = new int [1];
	if (GTK.GTK4) {
		// TODO: GTK4 GtkWindow no longer has the ability to get position
	} else {
		GTK3.gtk_window_get_position (shellHandle, x, y);
	}
	return new Point (x [0], y [0]);
}

@Override
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
	return DPIUtil.autoScaleDown (getMinimumSizeInPixels ());
}

Point getMinimumSizeInPixels () {
	checkWidget ();
	int width = Math.max (1, geometry.getMinWidth() + trimWidth ());
	int height = Math.max (1, geometry.getMinHeight() + trimHeight ());
	return new Point (width, height);
}

/**
 * Returns a point describing the maximum receiver's size. The
 * x coordinate of the result is the maximum width of the receiver.
 * The y coordinate of the result is the maximum height of the
 * receiver.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.116
 */
public Point getMaximumSize () {
	checkWidget ();
	return DPIUtil.autoScaleDown (getMaximumSizeInPixels ());
}

Point getMaximumSizeInPixels () {
	checkWidget ();

	int width = Math.min (Integer.MAX_VALUE, geometry.getMaxWidth() + trimWidth ());
	int height = Math.min (Integer.MAX_VALUE, geometry.getMaxHeight() + trimHeight ());
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

@Override
Point getSizeInPixels () {
	checkWidget ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (vboxHandle, allocation);
	int width = allocation.width;
	int height = allocation.height;
	int border = 0;
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0 || isCustomResize()) {
		border = gtk_container_get_border_width_or_margin (shellHandle);
	}
	return new Point (width + trimWidth () + 2*border, height + trimHeight () + 2*border);
}

@Override
public boolean getVisible () {
	checkWidget();
	return GTK.gtk_widget_get_visible (shellHandle);
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
 */
@Override
public Region getRegion () {
	/* This method is needed for @since 3.0 Javadoc */
	checkWidget ();
	if (originalRegion != null) return originalRegion;
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

@Override
Shell _getShell () {
	return this;
}

/**
 * Returns an array containing all shells which are
 * descendants of the receiver.
 *
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
	for (Shell activeshell : shells) {
		Control shell = activeshell;
		do {
			shell = shell.getParent ();
		} while (shell != null && shell != this);
		if (shell == this) count++;
	}
	int index = 0;
	Shell [] result = new Shell [count];
	for (Shell activeshell : shells) {
		Control shell = activeshell;
		do {
			shell = shell.getParent ();
		} while (shell != null && shell != this);
		if (shell == this) {
			result [index++] = activeshell;
		}
	}
	return result;
}

@Override
long gtk_button_press_event (long widget, long event) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			if (OS.isX11() && (style & SWT.ON_TOP) != 0 && (style & SWT.NO_FOCUS) == 0) {
				forceActive ();
			}
			double [] eventRX = new double [1];
			double [] eventRY = new double [1];
			GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

			int [] eventButton = new int [1];
			GDK.gdk_event_get_button(event, eventButton);

			if (eventButton[0] == 1) {
				display.resizeLocationX = eventRX[0];
				display.resizeLocationY = eventRY[0];
				int [] x = new int [1], y = new int [1];
				GTK3.gtk_window_get_position (shellHandle, x, y);
				display.resizeBoundsX = x [0];
				display.resizeBoundsY = y [0];
				GtkAllocation allocation = new GtkAllocation ();
				GTK.gtk_widget_get_allocation (shellHandle, allocation);
				display.resizeBoundsWidth = allocation.width;
				display.resizeBoundsHeight = allocation.height;
			}
		}
		/**
		 *  Feature in GTK: This handles ungrabbing the keyboard focus from a SWT.ON_TOP window
		 *  if it has editable fields and is running Wayland. Refer to bug 515773.
		 */
		if (requiresUngrab()) {
			long seat = GDK.gdk_event_get_seat(event);
			GDK.gdk_seat_ungrab(seat);
			GTK3.gtk_grab_remove(shellHandle);
		}
		return 0;
	}
	return super.gtk_button_press_event (widget, event);
}

@Override
long gtk_configure_event (long widget, long event) {
	int [] x = new int [1], y = new int [1];
	GTK3.gtk_window_get_position (shellHandle, x, y);

	if (!isVisible ()) {
		return 0; //We shouldn't handle move/resize events if shell is hidden.
	}

	if (!moved || oldX != x [0] || oldY != y [0]) {
		moved = true;
		oldX = x [0];
		oldY = y [0];
		sendEvent (SWT.Move);
		// widget could be disposed at this point
	}
	return 0;
}

@Override
long gtk_close_request (long widget) {
	if (isEnabled()) closeWidget ();
	return 1;
}

@Override
long gtk_delete_event (long widget, long event) {
	if (isEnabled()) closeWidget ();
	return 1;
}

@Override
long gtk_enter_notify_event (long widget, long event) {
	if (widget != shellHandle) {
		return super.gtk_enter_notify_event (widget, event);
	}
	return 0;
}

@Override
long gtk_draw (long widget, long cairo) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			int [] width = new int [1];
			int [] height = new int [1];
			long window = gtk_widget_get_window (widget);
			gdk_window_get_size (window, width, height);
			int border = gtk_container_get_border_width_or_margin (widget);
			long context = GTK.gtk_widget_get_style_context (shellHandle);
			//TODO draw shell frame on GTK3
			GTK.gtk_style_context_save (context);
			GTK.gtk_render_frame (context, cairo, 0, 0, width [0], border);
			GTK.gtk_render_frame (context, cairo, 0, height [0] - border, width [0], border);
			GTK.gtk_render_frame (context, cairo, 0, border, border, height [0] - border - border);
			GTK.gtk_render_frame (context, cairo, width [0] - border, border, border, height [0] - border - border);
			GTK.gtk_render_frame (context, cairo, 0 + 10, 0 + 10, width [0] - 20, height [0] - 20);
			GTK.gtk_style_context_restore (context);
			return 1;
		}
		if (GTK.GTK4) {
			super.gtk_draw(widget, cairo);
		}
		return 0;
	}
	return super.gtk_draw (widget, cairo);
}

@Override
long gtk_focus (long widget, long directionType) {
	switch ((int)directionType) {
		case GTK.GTK_DIR_TAB_FORWARD:
		case GTK.GTK_DIR_TAB_BACKWARD:
			Control control = display.getFocusControl ();
			if (control != null) {
				if ((control.state & CANVAS) != 0 && (control.style & SWT.EMBEDDED) != 0 && control.getShell () == this) {
					int traversal = directionType == GTK.GTK_DIR_TAB_FORWARD ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
					control.traverse (traversal);
					return 1;
				}
			}
			break;
	}
	return super.gtk_focus (widget, directionType);
}

@Override
long gtk_focus_in_event (long widget, long event) {
	if (widget != shellHandle) {
		return super.gtk_focus_in_event (widget, event);
	}
	display.activeShell = this;
	display.activePending = false;
	if (!ignoreFocusIn) {
		sendEvent (SWT.Activate);
	} else {
		ignoreFocusIn = false;
	}
	restoreFocus();
	return 0;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	if (widget != shellHandle) {
		return super.gtk_focus_out_event (widget, event);
	}
	Display display = this.display;
	if (!ignoreFocusOut && !ignoreFocusOutAfterGrab) {
		sendEvent (SWT.Deactivate);
		setActiveControl (null);
	}
	ignoreFocusOutAfterGrab = false;
	if (display.activeShell == this && !ignoreFocusOut) {
		display.activeShell = null;
		display.activePending = false;
	}
	return 0;
}

@Override
long gtk_leave_notify_event (long widget, long event) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			int [] state = new int [1];
			if (GTK.GTK4) {
				state[0] = GDK.gdk_event_get_modifier_state(event);
			} else {
				GDK.gdk_event_get_state(event, state);
			}

			if ((state[0] & GDK.GDK_BUTTON1_MASK) == 0) {
				if (GTK.GTK4) {
					GTK4.gtk_widget_set_cursor (shellHandle, 0);
				} else {
					long window = gtk_widget_get_window (shellHandle);
					GDK.gdk_window_set_cursor (window, 0);
				}
				display.resizeMode = 0;
			}
		}
		return 0;
	}
	return super.gtk_leave_notify_event (widget, event);
}

@Override
long gtk_map (long widget) {
	// No "map-event" signal on GTK4, set mapped here instead
	if (GTK.GTK4) {
		mapped = true;
	}
	return super.gtk_map(widget);
}

@Override
long gtk_move_focus (long widget, long directionType) {
	Control control = display.getFocusControl ();
	if (control != null) {
		long focusHandle = control.focusHandle ();
		GTK.gtk_widget_child_focus (focusHandle, (int)directionType);
	}
	OS.g_signal_stop_emission_by_name (shellHandle, OS.move_focus);
	return 1;
}

@Override
long gtk_motion_notify_event (long widget, long event) {
	if (widget == shellHandle) {
		if (isCustomResize ()) {
			int [] state = new int [1];
			if (GTK.GTK4) {
				state[0] = GDK.gdk_event_get_modifier_state(event);
			} else {
				GDK.gdk_event_get_state(event, state);
			}

			double [] eventRX = new double [1];
			double [] eventRY = new double [1];
			GDK.gdk_event_get_root_coords(event, eventRX, eventRY);
			if ((state[0] & GDK.GDK_BUTTON1_MASK) != 0) {
				int border = gtk_container_get_border_width_or_margin (shellHandle);
				int dx = (int)(eventRX[0] - display.resizeLocationX);
				int dy = (int)(eventRY[0] - display.resizeLocationY);
				int x = display.resizeBoundsX;
				int y = display.resizeBoundsY;
				int width = display.resizeBoundsWidth;
				int height = display.resizeBoundsHeight;
				int newWidth = Math.max(width - dx, Math.max(geometry.getMinWidth(), border + border));
				int newHeight = Math.max(height - dy, Math.max(geometry.getMinHeight(), border + border));
				switch (display.resizeMode) {
					case SWT.CURSOR_SIZEW:
						x += width - newWidth;
						width = newWidth;
						break;
					case SWT.CURSOR_SIZENW:
						x += width - newWidth;
						width = newWidth;
						y += height - newHeight;
						height = newHeight;
						break;
					case SWT.CURSOR_SIZEN:
						y += height - newHeight;
						height = newHeight;
						break;
					case SWT.CURSOR_SIZENE:
						width = Math.max(width + dx, Math.max(geometry.getMinWidth(), border + border));
						y += height - newHeight;
						height = newHeight;
						break;
					case SWT.CURSOR_SIZEE:
						width = Math.max(width + dx, Math.max(geometry.getMinWidth(), border + border));
						break;
					case SWT.CURSOR_SIZESE:
						width = Math.max(width + dx, Math.max(geometry.getMinWidth(), border + border));
						height = Math.max(height + dy, Math.max(geometry.getMinHeight(), border + border));
						break;
					case SWT.CURSOR_SIZES:
						height = Math.max(height + dy, Math.max(geometry.getMinHeight(), border + border));
						break;
					case SWT.CURSOR_SIZESW:
						x += width - newWidth;
						width = newWidth;
						height = Math.max(height + dy, Math.max(geometry.getMinHeight(), border + border));
						break;
				}
				if (x != display.resizeBoundsX || y != display.resizeBoundsY) {
					if (GTK.GTK4) {
						/* TODO: GTK4 no longer exist, will probably need to us gdk_toplevel_begin_move &
						 * gdk_toplevel_begin_resize to provide this functionality
						 */
					} else {
						GDK.gdk_window_move_resize (gtk_widget_get_window (shellHandle), x, y, width, height);
					}
				} else {
					GTK3.gtk_window_resize (shellHandle, width, height);
				}
			} else {
				double [] eventX = new double [1];
				double [] eventY = new double [1];
				if (GTK.GTK4) {
					GDK.gdk_event_get_position(event, eventX, eventY);
				} else {
					GDK.gdk_event_get_coords(event, eventX, eventY);
				}

				int mode = getResizeMode (eventX[0], eventY[0]);
				if (mode != display.resizeMode) {
					long cursor = display.getSystemCursor(mode).handle;
					if (GTK.GTK4) {
						GTK4.gtk_widget_set_cursor (shellHandle, cursor);
					} else {
						long window = gtk_widget_get_window (shellHandle);
						GDK.gdk_window_set_cursor (window, cursor);
					}
					display.resizeMode = mode;
				}
			}
		}
		return 0;
	}
	return super.gtk_motion_notify_event (widget, event);
}

@Override
long gtk_key_press_event (long widget, long event) {
	if (widget == shellHandle) {
		/* Stop menu mnemonics when the shell is disabled */
		if ((state & DISABLED) != 0) return 1;

		if (menuBar != null && !menuBar.isDisposed ()) {
			Control focusControl = display.getFocusControl ();
			if (focusControl != null && (focusControl.hooks (SWT.KeyDown) || focusControl.filters (SWT.KeyDown))) {
				long [] accel = new long [1];
				long setting = GTK.gtk_settings_get_default ();
				OS.g_object_get (setting, GTK.gtk_menu_bar_accel, accel, 0);
				if (accel [0] != 0) {
					int [] keyval = new int [1];
					int [] mods = new int [1];
					GTK.gtk_accelerator_parse (accel [0], keyval, mods);
					OS.g_free (accel [0]);
					if (keyval [0] != 0) {
						int [] key = new int [1];
						int [] state = new int[1];
						if (GTK.GTK4) {
							key[0] = GDK.gdk_key_event_get_keyval(event);
							state[0] = GDK.gdk_event_get_modifier_state(event);
						} else {
							GDK.gdk_event_get_keyval(event, key);
							GDK.gdk_event_get_state(event, state);
						}

						int mask = GTK.gtk_accelerator_get_default_mod_mask ();
						if (key[0] == keyval [0] && (state[0] & mask) == (mods [0] & mask)) {
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

@Override
long gtk_size_allocate (long widget, long allocation) {
	int width, height;
	GdkRectangle monitorSize = new GdkRectangle();
	int[] widthA = new int [1];
	int[] heightA = new int [1];

	/*Bug 577431:
	 * In GTK4 using gtk_window_get_default_size returns the previously set size even
	 * if the window is maximized. Due to this it cannot be used when the shell has
	 * been maximized. To fix this, get the monitor geometry ONLY when the window is
	 * maximized and use this as the dimensions. Furthermore, the headerBar size needs
	 * to be taken into account,otherwise some content will be off screen.
	 *
	 * While this fix allows the usage of the entire horizontal space, the vertical
	 * space is more tricky. Under Wayland, getting the work area of the display is
	 * not possible and not supported. A "hacky" way has been used in GTK4 thus far
	 * to get the header bar height, which is then subtracted from the display height.
	 * This gets the height *mostly* correct, but there is about 10 pixels that
	 * are not used.
	 *
	 * This should be revisited at a later time, when the GTK4 port is more mature.
	 * TODO: Make use of the entire vertical height
	 */
	if (GTK.GTK4) {
		if(!GTK4.gtk_window_is_maximized(shellHandle)) {
			GTK.gtk_window_get_default_size(shellHandle, widthA, heightA);
		}
		else {
			long display = GDK.gdk_display_get_default();
			long monitor = GDK.gdk_display_get_monitor_at_surface(display, paintSurface());
			GDK.gdk_monitor_get_geometry(monitor, monitorSize);
			long header = GTK4.gtk_widget_get_next_sibling(GTK4.gtk_widget_get_first_child(shellHandle));
			int[] headerNaturalHeight = new int[1];
			GTK4.gtk_widget_measure(header, GTK.GTK_ORIENTATION_VERTICAL, 0, null, headerNaturalHeight, null, null);
			widthA[0] = monitorSize.width;
			heightA[0] = monitorSize.height - headerNaturalHeight[0];
		}
	} else {
		GTK3.gtk_window_get_size(shellHandle, widthA, heightA);
	}
	width = widthA[0];
	height = heightA[0];

	//	Bug 474235: on Wayland gtk_size_allocate() is called more frequently, causing an
	//  infinitely recursive resize call. This causes non-resizable Shells/Dialogs to
	//  crash. Fix: only call resizeBounds() on resizable Shells.
	if ((!resized || oldWidth != width || oldHeight != height)
			&& (OS.isWayland() ? ((style & SWT.RESIZE) != 0) : true)) {
		oldWidth = width;
		oldHeight = height;
		resizeBounds (width, height, true); //this is called to resize child widgets when the shell is resized.
	}
	return 0;
}

private void updateDecorations(long gdkResource) {
	if (OS.isX11()) {
		/*
		 * Feature in X11. Configuring window decorations when
		 * 'GTK_CSD=1' is set causes the window to have double
		 * decorations.
		 *
		 * When client-side decorations (CSD) are in use,
		 * server-side decorations (window title, borders, etc)
		 * are hidden and GTK draws them instead. In this case,
		 * configuring GDK window will at best have no effect,
		 * because its decorations are hidden.
		 *
		 * On X11, it's even worse: it will cause server-side
		 * decorations to appear, and window will have double
		 * decorations.
		 *
		 * Current GTK implementation will ignore gdk_surface_set_decorations()
		 * on Wayland and MIR anyway, so I decided to narrow
		 * the workaround to X11 only to get a simpler
		 * implementation. For example, getting CSD status via
		 * 'GTK_CSD' variable is reliable on X11 only: on MIR
		 * and Wayland more settings come into play,
		 * @see 'gtk_window_should_use_csd' in GTK sources.
		 */
		String gtkCsdValue = System.getenv("GTK_CSD");
		if ((gtkCsdValue != null) && gtkCsdValue.equals("1")) return;
	}

	int decorations = 0;
	int functions = 0;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.MIN) != 0) {
			decorations |= GDK.GDK_DECOR_MINIMIZE;
			functions |= GDK.GDK_FUNC_MINIMIZE;
		}
		if ((style & SWT.MAX) != 0) {
			decorations |= GDK.GDK_DECOR_MAXIMIZE;
			functions |= GDK.GDK_FUNC_MAXIMIZE;
		}
		if ((style & SWT.RESIZE) != 0) {
			decorations |= GDK.GDK_DECOR_RESIZEH;
			functions |= GDK.GDK_FUNC_RESIZE;
		}
		if ((style & SWT.BORDER) != 0) decorations |= GDK.GDK_DECOR_BORDER;
		if ((style & SWT.MENU) != 0) decorations |= GDK.GDK_DECOR_MENU;
		if ((style & SWT.TITLE) != 0) decorations |= GDK.GDK_DECOR_TITLE;
		if ((style & SWT.CLOSE) != 0) functions |= GDK.GDK_FUNC_CLOSE;
		/*
		 * Feature in GTK.  Under some Window Managers (Sawmill), in order
		 * to get any border at all from the window manager it is necessary to
		 * set GDK_DECOR_BORDER.  The fix is to force these bits when any
		 * kind of border is requested.
		 */
		if ((style & SWT.RESIZE) != 0) decorations |= GDK.GDK_DECOR_BORDER;
		if ((style & SWT.NO_MOVE) == 0) functions |=  GDK.GDK_FUNC_MOVE;
	}
	if (GTK.GTK4) {
		/*TODO: GTK4 no longer supports specifying hints for window management functions to be available on the window frame
		 * Usually window managers don't do anything with the hint anyways. */
		/*TODO: GTK4 no longer supports specifying hints for window management decorations. There is no direct way to specify the
		 * decorations we want/don't want. May have to implement client-side decorations. Use gdk_toplevel_set_decorated to indicate.
		 * A replacement for SWT.CLOSE in GTK4 is gdk_toplevel_set_deletable */
	} else {
		GDK.gdk_window_set_decorations (gdkResource, decorations);
		/*
		 * For systems running Metacity, this call forces the style hints to
		 * be displayed in a window's titlebar. Otherwise, the decorations
		 * set by the function gdk_window_set_decorations (window,
		 * decorations) are ignored by the window manager.
		 */
		GDK.gdk_window_set_functions(gdkResource, functions);
	}
}

@Override
long gtk_realize (long widget) {
	long result = super.gtk_realize (widget);
	long gdkResource;
	if (GTK.GTK4) {
		gdkResource = gtk_widget_get_surface (shellHandle);
	} else {
		gdkResource = gtk_widget_get_window (shellHandle);
	}
	if ((style & SWT.SHELL_TRIM) != SWT.SHELL_TRIM) {
		updateDecorations(gdkResource);
	} else if ((style & SWT.NO_MOVE) != 0) {
		if (GTK.GTK4) {
			/*TODO: GTK4 no longer supports specifying hints for window management functions to be available on the window frame
			 * Usually window managers don't do anything with the hint anyways. */
		} else {
			// if the GDK_FUNC_ALL bit is present, all the other style
			// bits specified as a parameter will be removed from the window
			GDK.gdk_window_set_functions (gdkResource, GDK.GDK_FUNC_ALL | GDK.GDK_FUNC_MOVE);
		}
	}

	if ((style & SWT.ON_TOP) != 0) {
		if (!GTK.GTK4) GTK3.gtk_window_set_keep_above(shellHandle, true);
	}
	return result;
}

@Override
long gtk_window_state_event (long widget, long event) {
	GdkEventWindowState gdkEvent = new GdkEventWindowState ();
	GTK3.memmove (gdkEvent, event, GdkEventWindowState.sizeof);
	minimized = (gdkEvent.new_window_state & GDK.GDK_WINDOW_STATE_ICONIFIED) != 0;
	maximized = (gdkEvent.new_window_state & GDK.GDK_WINDOW_STATE_MAXIMIZED) != 0;
	fullScreen = (gdkEvent.new_window_state & GDK.GDK_WINDOW_STATE_FULLSCREEN) != 0;
	if ((gdkEvent.changed_mask & GDK.GDK_WINDOW_STATE_ICONIFIED) != 0) {
		if (minimized) {
			sendEvent (SWT.Iconify);
		} else {
			sendEvent (SWT.Deiconify);
		}
		updateMinimized (minimized);
	}
	return 0;
}

@Override
long notifyState (long object, long arg0) {
	// GTK4 equivalent of gtk_window_state_event
	assert GTK.GTK4;
	int gdkSurfaceState = GTK4.gdk_toplevel_get_state (object);
	minimized = (gdkSurfaceState & GDK.GDK_SURFACE_STATE_MINIMIZED) != 0;
	maximized = (gdkSurfaceState & GDK.GDK_SURFACE_STATE_MAXIMIZED) != 0;
	fullScreen = (gdkSurfaceState & GDK.GDK_SURFACE_STATE_FULLSCREEN) != 0;
	if ((gdkSurfaceState & GDK.GDK_SURFACE_STATE_MINIMIZED) != 0) {
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
	setVisible (true);
	// force is necessary, because otherwise it won't do anything
	bringToTop (true);
	if (isDisposed ()) return;
	/*
	 * When no widget has been given focus, or another push button has focus,
	 * give focus to the default button. This avoids overriding the default
	 * button.
	 */
	boolean restored = restoreFocus ();
	if (!restored) {
		restored = traverseGroup (true);
	}
	if (restored) {
		Control focusControl = display.getFocusControl ();
		if (focusControl instanceof Button && (focusControl.style & SWT.PUSH) != 0) {
			restored = false;
		}
	}
	if (!restored) {
		/* If a shell is opened during the FocusOut event of a widget,
		 * it is required to set focus to all shells except for ON_TOP
		 * shells in order to maintain consistency with other platforms.
		 */
		if ((style & SWT.ON_TOP) == 0) display.focusEvent = SWT.None;

		if (defaultButton != null && !defaultButton.isDisposed ()) {
			defaultButton.setFocus ();
		} else {
			setFocus ();
		}
	}
}

@Override
public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	// Needs to be implemented on GTK4/Wayland
	if (!GTK.GTK4 && OS.isX11()) {
		Rectangle clipping = gc.getClipping();
		long shellWindow = gtk_widget_get_window(shellHandle);
		GdkRectangle rect = new GdkRectangle ();
		GDK.gdk_window_get_frame_extents (shellWindow, rect);
		if (clipping.height < rect.height || clipping.width < rect.width) {
			System.err.println("Warning: the GC provided to Shell.print() has a smaller"
					+ " clipping than what is needed to print the Shell trimmings"
					+ " and content. Only the client area will be printed.");
			return super.print(gc);
		}
		long rootWindow = GDK.gdk_get_default_root_window();
		long pixbuf = GDK.gdk_pixbuf_get_from_window(rootWindow, rect.x, rect.y, rect.width, rect.height);
		if (pixbuf != 0) {
			GDK.gdk_cairo_set_source_pixbuf(gc.handle, pixbuf, 0, 0);
			Cairo.cairo_paint(gc.handle);
			OS.g_object_unref(pixbuf);
			return true;
		}
	}
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

@Override
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
	setActiveControl (control, SWT.None);
}

void setActiveControl (Control control, int type) {
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
			Event event = new Event ();
			event.detail = type;
			activate [i].sendEvent (SWT.Activate, event);
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
	boolean composited;
	if (GTK.GTK4) {
		long display = GDK.gdk_display_get_default();
		composited = GDK.gdk_display_is_composited(display);
	} else {
		long screen = GTK3.gtk_widget_get_screen(shellHandle);
		composited = GDK.gdk_screen_is_composited(screen);
	}
	if (composited) {
		GTK.gtk_widget_set_opacity (shellHandle, (double) alpha / 255);
	}
}

void resizeBounds (int width, int height, boolean notify) {
	int border = gtk_container_get_border_width_or_margin (shellHandle);
	if (GTK.GTK4) {
		if (parent != null) {
			GtkAllocation allocation = new GtkAllocation();
			GtkAllocation currentSizeAlloc = new GtkAllocation();
			GTK.gtk_widget_get_allocation(vboxHandle, currentSizeAlloc);
			allocation.width = width;
			allocation.height = height + currentSizeAlloc.y;
			GTK4.gtk_widget_size_allocate(shellHandle, allocation, -1);
		}
	} else {
		if (redrawWindow != 0) {
			GDK.gdk_window_resize (redrawWindow, width, height);
		}
		if (enableWindow != 0) {
			GDK.gdk_window_resize (enableWindow, width, height);
		}
	}
	int boxWidth = width - 2*border;
	int boxHeight = height - 2*border;
	if ((style & SWT.RESIZE) == 0) {
		GTK.gtk_widget_set_size_request (vboxHandle, boxWidth, boxHeight);
	}
	forceResize (boxWidth, boxHeight);
	if (notify) {
		resized = true;
		sendEvent (SWT.Resize);
		if (isDisposed ()) return;
		if (layout != null) {
			markLayout (false, false);
			updateLayout (false);
			if (isCustomResize()) {
				// bug 546961: trigger an extra resize to prevent scrollbar sliders paint problems after custom resize
				forceResize(boxWidth, boxHeight);
			}
		}
	}
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	width = Math.min(width, (2 << 14) - 1);
	height = Math.min(height, (2 << 14) - 1);

	if (fullScreen) setFullScreen (false);
	/*
	* Bug in GTK.  When either of the location or size of
	* a shell is changed while the shell is maximized, the
	* shell is moved to (0, 0).  The fix is to explicitly
	* unmaximize the shell before setting the bounds to
	* anything different from the current bounds.
	*/
	if (getMaximized ()) {
		Rectangle rect = getBoundsInPixels ();
		boolean sameOrigin = !move || (rect.x == x && rect.y == y);
		boolean sameExtent = !resize || (rect.width == width && rect.height == height);
		if (sameOrigin && sameExtent) return 0;
		setMaximized (false);
	}
	int result = 0;
	if (move) {
		if (!GTK.GTK4) {
			int [] x_pos = new int [1], y_pos = new int [1];
			GTK3.gtk_window_get_position(shellHandle, x_pos, y_pos);
			GTK3.gtk_window_move(shellHandle, x, y);
			/*
			 * Bug in GTK: gtk_window_get_position () is not always up-to-date right after
			 * gtk_window_move (). The random delays cause problems like bug 445900.
			 *
			 * The workaround is to wait for the position change to be processed.
			 * The limit 1000 is an experimental value. I've seen cases where about 200
			 * iterations were necessary.
			 */
			for (int i = 0; i < 1000; i++) {
				int [] x2_pos = new int [1], y2_pos = new int [1];
				GTK3.gtk_window_get_position(shellHandle, x2_pos, y2_pos);
				if (x2_pos[0] == x && y2_pos[0] == y) {
					break;
				}
			}
			if (x_pos [0] != x || y_pos [0] != y) {
				moved = true;
				oldX = x;
				oldY = y;
				sendEvent(SWT.Move);
				if (isDisposed ()) return 0;
				result |= MOVED;
			}
		}
	}
	if (resize) {
		width = Math.max (1, Math.max (geometry.getMinWidth(), width - trimWidth ()));
		if (geometry.getMaxWidth() > 0) {
			width = Math.min( width, geometry.getMaxWidth());
		}
		height = Math.max (1, Math.max (geometry.getMinHeight(), height - trimHeight ()));
		if (geometry.getMaxHeight() > 0) {
			height = Math.min(height, geometry.getMaxHeight());
		}
		/*
		* If the shell is created without a RESIZE style bit, and the
		* minWidth/minHeight/maxWidth/maxHeight have been set, allow the resize.
		*/
		if ((style & SWT.RESIZE) != 0 || (geometry.getMinHeight()  != 0 || geometry.getMinWidth()  != 0 || geometry.getMaxHeight()  != 0 || geometry.getMaxWidth()  != 0)) {
			if (GTK.GTK4) {
				/*
				 * On GTK4, GtkWindow size includes the header bar. In order to keep window size allocation of the client area
				 * consistent with previous versions of SWT, we need to include the header bar height in addition to the given height value.
				 */
				long header = GTK4.gtk_widget_get_next_sibling(GTK4.gtk_widget_get_first_child(shellHandle));
				int[] headerNaturalHeight = new int[1];
				GTK4.gtk_widget_measure(header, GTK.GTK_ORIENTATION_VERTICAL, 0, null, headerNaturalHeight, null, null);
				GTK.gtk_window_set_default_size(shellHandle, width, height + headerNaturalHeight[0]);
			} else {
				GTK3.gtk_window_resize (shellHandle, width, height);
			}
		}
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

@Override
void setCursor (long cursor) {
	if (!GTK.GTK4) {
		if (enableWindow != 0) {
			GDK.gdk_window_set_cursor(enableWindow, cursor);
		}
	}

	super.setCursor (cursor);
}

@Override
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
	if (!GTK.GTK4) {
		if (enabled) {
			if (enableWindow != 0) {
				cleanupEnableWindow();
			}
		} else {
			long parentHandle = shellHandle;
			GTK.gtk_widget_realize (parentHandle);
			long window = gtk_widget_get_window (parentHandle);
			Rectangle rect = getBoundsInPixels ();
			GdkWindowAttr attributes = new GdkWindowAttr ();
			attributes.width = rect.width;
			attributes.height = rect.height;
			attributes.event_mask = (0xFFFFFFFF & ~OS.ExposureMask);
			attributes.wclass = GDK.GDK_INPUT_ONLY;
			attributes.window_type = GDK.GDK_WINDOW_CHILD;
			enableWindow = GTK3.gdk_window_new (window, attributes, 0);
			if (enableWindow != 0) {
				if (cursor != null) {
					GDK.gdk_window_set_cursor (enableWindow, cursor.handle);
				}
				GDK.gdk_window_set_user_data (enableWindow, parentHandle);
				GDK.gdk_window_show (enableWindow);
			}
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
 * <code>setMaximized(true)</code>, <code>setMinimized(true)</code> and
 * <code>setMaximumSize</code> will vary by platform.
 * Typically, the behavior will match the platform user's
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
		GTK.gtk_window_fullscreen (shellHandle);
	} else {
		GTK.gtk_window_unfullscreen (shellHandle);
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

@Override
void setInitialBounds() {
	int width = 0, height = 0;

	if ((state & FOREIGN_HANDLE) != 0) {
		GtkAllocation allocation = new GtkAllocation ();
		GTK.gtk_widget_get_allocation (shellHandle, allocation);
		width = allocation.width;
		height = allocation.height;
	} else {
		GdkRectangle dest = new GdkRectangle();

		if (GTK.GTK4) {
			long display = GDK.gdk_display_get_default();
			if (display != 0) {
				long monitor = GDK.gdk_display_get_monitor_at_surface(display, paintSurface());
				GDK.gdk_monitor_get_geometry(monitor, dest);
				width = (int) (dest.width * SHELL_TO_MONITOR_RATIO);
				height = (int) (dest.height * SHELL_TO_MONITOR_RATIO);
			}

			if ((style & SWT.RESIZE) != 0) {
				/*
				 * On GTK4, GtkWindow size includes the header bar. In order to keep window size allocation of the client area
				 * consistent with previous versions of SWT, we need to include the header bar height in addition to the given height value.
				 */
				long header = GTK4.gtk_widget_get_next_sibling(GTK4.gtk_widget_get_first_child(shellHandle));
				int[] headerNaturalHeight = new int[1];
				GTK4.gtk_widget_measure(header, GTK.GTK_ORIENTATION_VERTICAL, 0, null, headerNaturalHeight, null, null);

				GTK.gtk_window_set_default_size(shellHandle, width, height + headerNaturalHeight[0]);
			}
		} else {
			long display = GDK.gdk_display_get_default();
			if (display != 0) {
				long monitor = GDK.gdk_display_get_monitor_at_window(display, paintWindow());
				GDK.gdk_monitor_get_geometry(monitor, dest);
				width = (int) (dest.width * SHELL_TO_MONITOR_RATIO);
				height = (int) (dest.height * SHELL_TO_MONITOR_RATIO);
			}

			if (width == 0 && height == 0) {
				// if the above failed, use gdk_screen_height/width as a fallback
				width = (int) (GDK.gdk_screen_width() * SHELL_TO_MONITOR_RATIO);
				height = (int) (GDK.gdk_screen_height() * SHELL_TO_MONITOR_RATIO);
			}

			if ((style & SWT.RESIZE) != 0) {
				GTK3.gtk_window_resize(shellHandle, width, height);
			}
		}
	}

	resizeBounds(width, height, false);
}

@Override
public void setMaximized (boolean maximized) {
	checkWidget();
	super.setMaximized (maximized);
	if (maximized) {
		GTK.gtk_window_maximize (shellHandle);
	} else {
		GTK.gtk_window_unmaximize (shellHandle);
	}
}

@Override
public void setMenuBar (Menu menu) {
	checkWidget();
	if (menuBar == menu) return;
	boolean both = menu != null && menuBar != null;

	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error(SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error(SWT.ERROR_INVALID_PARENT);
	}

	if (menuBar != null) {
		long menuHandle = menuBar.handle;
		GTK.gtk_widget_hide (menuHandle);

		if (!GTK.GTK4) {
			destroyAccelGroup();
		}
	}
	menuBar = menu;
	if (menuBar != null) {
		long menuHandle = menu.handle;
		GTK.gtk_widget_show (menuHandle);

		if (!GTK.GTK4) {
			createAccelGroup();
			menuBar.addAccelerators(accelGroup);
		}
	}

	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (vboxHandle, allocation);
	int width = allocation.width;
	int height = allocation.height;
	resizeBounds (width, height, !both);
}

@Override
public void setMinimized (boolean minimized) {
	checkWidget();
	if (this.minimized == minimized) return;
	super.setMinimized (minimized);
	if(!GTK.gtk_widget_get_visible(shellHandle)) {
		GTK.gtk_widget_show(shellHandle);
	}
	if (minimized) {
		if (GTK.GTK4) {
			GTK4.gtk_window_minimize(shellHandle);
		} else {
			GTK3.gtk_window_iconify (shellHandle);
		}
	} else {
		if (GTK.GTK4) {
			GTK4.gtk_window_unminimize(shellHandle);
		} else {
			GTK3.gtk_window_deiconify (shellHandle);
		}
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
	setMinimumSize (new Point (width, height));
}

void setMinimumSizeInPixels (int width, int height) {
	checkWidget ();
	geometry.setMinWidth(Math.max (width, trimWidth ()) - trimWidth ());
	geometry.setMinHeight(Math.max (height, trimHeight ()) - trimHeight ());

	if(GTK.GTK4) {
		geometry.setMinSizeRequested(true);
		return;
	}

	int hint = GDK.GDK_HINT_MIN_SIZE;
	if (geometry.getMaxHeight() > 0 || geometry.getMaxWidth() > 0) {
		hint = hint | GDK.GDK_HINT_MAX_SIZE;
	}
	GTK3.gtk_window_set_geometry_hints (shellHandle, 0, (GdkGeometry) geometry, hint);
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
	setMinimumSizeInPixels (DPIUtil.autoScaleUp (size));
}

void setMinimumSizeInPixels (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setMinimumSizeInPixels (size.x, size.y);
}

/**
 * Sets the receiver's maximum size to the size specified by the arguments.
 * If the new maximum size is smaller than the current size of the receiver,
 * the receiver is resized to the new maximum size.
 * <p>
 * Note: The result of intermixing calls to <code>setMaximumSize</code> and
 * <code>setFullScreen(true)</code> will vary by platform.
 * Typically, the behavior will match the platform user's
 * expectations, but not always. This should be avoided if possible.
 * </p>
 * @param width the new maximum width for the receiver
 * @param height the new maximum height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.116
 */
public void setMaximumSize (int width, int height) {
	checkWidget ();
	setMaximumSize (new Point (width, height));
}

/**
 * Sets the receiver's maximum size to the size specified by the argument.
 * If the new maximum size is smaller than the current size of the receiver,
 * the receiver is resized to the new maximum size.
 * <p>
 * Note: The result of intermixing calls to <code>setMaximumSize</code> and
 * <code>setFullScreen(true)</code> will vary by platform.
 * Typically, the behavior will match the platform user's
 * expectations, but not always. This should be avoided if possible.
 * </p>
 * @param size the new maximum size for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.116
 */
public void setMaximumSize (Point size) {
	checkWidget ();
	setMaximumSizeInPixels (DPIUtil.autoScaleUp (size));
}

void setMaximumSizeInPixels (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setMaximumSizeInPixels (size.x, size.y);
}

void setMaximumSizeInPixels (int width, int height) {
	checkWidget ();
	geometry.setMaxWidth(Math.max (width, trimWidth ()) - trimWidth ());
	geometry.setMaxHeight(Math.max (height, trimHeight ()) - trimHeight ());
	int hint = GDK.GDK_HINT_MAX_SIZE;
	if (geometry.getMinWidth() > 0 || geometry.getMinHeight() > 0) {
		hint = hint | GDK.GDK_HINT_MIN_SIZE;
	}
	GTK3.gtk_window_set_geometry_hints (shellHandle, 0, (GdkGeometry) geometry, hint);
}

/**
 * Sets the receiver's modified state as specified by the argument.
 *
 * @param modified the new modified state for the receiver
 *
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
 * <p>
 * NOTE: This method also sets the size of the shell. Clients should
 * not call {@link #setSize} or {@link #setBounds} on this shell.
 * Furthermore, the passed region should not be modified any more.
 * </p>
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
 */
@Override
public void setRegion (Region region) {
	checkWidget ();
	if ((style & SWT.NO_TRIM) == 0) return;

	if (region != null) {
		Rectangle bounds = region.getBounds ();
		setSize (bounds.x + bounds.width, bounds.y + bounds.height);
	}
	Region regionToDispose = null;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		if (originalRegion != null) regionToDispose = this.region;
		originalRegion = region;
		region = mirrorRegion (region);
	} else {
		originalRegion = null;
	}
	super.setRegion (region);
	if (regionToDispose != null) regionToDispose.dispose();
}

//copied from Region:
static void gdk_region_get_rectangles(long region, long [] rectangles, int[] n_rectangles) {
	int num = Cairo.cairo_region_num_rectangles (region);
	if (n_rectangles != null) n_rectangles[0] = num;
	rectangles[0] = OS.g_malloc(GdkRectangle.sizeof * num);
	for (int n = 0; n < num; n++) {
		Cairo.cairo_region_get_rectangle (region, n, rectangles[0] + (n * GdkRectangle.sizeof));
	}
}

static Region mirrorRegion (Region region) {
	if (region == null) return null;

	Region mirrored = new Region (region.getDevice ());

	long rgn = region.handle;
	int [] nRects = new int [1];
	long [] rects = new long [1];
	gdk_region_get_rectangles (rgn, rects, nRects);
	Rectangle bounds = DPIUtil.autoScaleUp(region.getBounds ());
	cairo_rectangle_int_t rect = new cairo_rectangle_int_t();
	for (int i = 0; i < nRects [0]; i++) {
		Cairo.memmove (rect, rects[0] + (i * GdkRectangle.sizeof), GdkRectangle.sizeof);
		rect.x = bounds.x + bounds.width - rect.x - rect.width;
		Cairo.cairo_region_union_rectangle (mirrored.handle, rect);
	}
	if (rects [0] != 0) OS.g_free (rects [0]);
	return mirrored;
}

/*
 * Shells are never labelled by other widgets, so no initialization is needed.
 */
@Override
void setRelations() {
}

@Override
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
	byte [] buffer = Converter.wcsToMbcs (chars, true);
	GTK.gtk_window_set_title (shellHandle, buffer);
}

@Override
public void setVisible (boolean visible) {
	checkWidget();

	if (moved) { //fix shell location if it was moved.
		setLocationInPixels(oldX, oldY);
	}
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & mask) != 0) {
		if (visible) {
			display.setModalShell (this);
			GTK.gtk_window_set_modal (shellHandle, true);
		} else {
			display.clearModal (this);
			GTK.gtk_window_set_modal (shellHandle, false);
		}
		/*
		 * When in full-screen mode, the OS will always consider it to be the top of the display stack unless it is a dialog.
		 * This fix will give modal windows dialog-type priority if the parent is in full-screen, allowing it to be popped
		 * up in front of the full-screen window.
		 */
		if (parent!=null && parent.getShell().getFullScreen()) {
			GTK3.gtk_window_set_type_hint(shellHandle, GDK.GDK_WINDOW_TYPE_HINT_DIALOG);
		}
	} else {
		updateModal ();
	}
	showWithParent = visible;
	if (GTK.gtk_widget_get_mapped (shellHandle) == visible) return;
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
		/*
		 * Bug/Feature in GTK: calling gtk_widget_show for the first time before
		 * gtk_widget_resize assumes the initial default size for any subsequent calls
		 * to gtk_widget_move. This causes issue with shell.setBounds where the
		 * location can only be moved to a limited bound on the screen corresponding
		 * to the default size by the window manager, even when the new resized shell
		 * is smaller and is able to move to further x, y position without being cutoff by
		 * the screen. The fix is to set the default size to the smallest possible (1, 1)
		 * before gtk_widget_show.
		 */
		if (!GTK.GTK4 && (oldWidth == 0 && oldHeight == 0)) {
			int [] init_width = new int[1], init_height = new int[1];
			GTK3.gtk_window_get_size(shellHandle, init_width, init_height);
			GTK3.gtk_window_resize(shellHandle, 1, 1);
			GTK.gtk_widget_show (shellHandle);
			GTK3.gtk_window_resize(shellHandle, init_width[0], init_height[0]);
			resizeBounds (init_width[0], init_height[0], false);
		} else {
			GTK.gtk_widget_show (shellHandle);
		}
		/**
		 *  Feature in GTK: This handles grabbing the keyboard focus from a SWT.ON_TOP window
		 *  if it has editable fields and is running Wayland. Refer to bug 515773.
		 */
		if (!GTK.GTK4) {
			if (enableWindow != 0) GDK.gdk_window_raise(enableWindow);
		}
		if (isDisposed ()) return;
		if (!( !GTK.GTK4 && OS.isX11() && GTK.GTK_IS_PLUG (shellHandle))) {
			display.dispatchEvents = new int [] {
				GDK.GDK_EXPOSE,
				GDK.GDK_FOCUS_CHANGE,
				GDK.GDK_CONFIGURE,
				GDK.GDK_MAP,
				GDK.GDK_UNMAP,
				GDK.GDK_NO_EXPOSE,
				GDK.GDK_WINDOW_STATE
			};
			Display display = this.display;
			display.putGdkEvents();
			boolean iconic = false;
			Shell shell = parent != null ? parent.getShell() : null;
			do {
				if (GTK.GTK4) {
					OS.g_main_context_iteration (0, false);
				} else {
					GTK3.gtk_main_iteration_do (false);
				}
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
			if (!GTK.GTK4) gdk_pointer_ungrab (GTK3.gtk_widget_get_window (shellHandle), GDK.GDK_CURRENT_TIME);
		}
		opened = true;
		if (!moved) {
			moved = true;
			Point location = getLocationInPixels();
			oldX = location.x;
			oldY = location.y;
			sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
		if (!resized) {
			resized = true;
			Point size = getSizeInPixels ();
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
		checkAndUngrabFocus();
		GTK.gtk_widget_hide (shellHandle);
		sendEvent (SWT.Hide);
	}
}

@Override
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

@Override
long shellMapProc (long handle, long arg0, long user_data) {
	mapped = true;
	display.dispatchEvents = null;
	return 0;
}

@Override
void showWidget () {
	if ((state & FOREIGN_HANDLE) != 0) {
		/*
		* In case of foreign handles, activeShell might not be initialised as
		* no focusIn events are generated on the window until the window loses
		* and gain focus.
		*/
		if (GTK.gtk_window_is_active (shellHandle)) {
			display.activeShell = this;
			display.activePending = true;
		}

		if (GTK.GTK4) {
			for (long child = GTK4.gtk_widget_get_first_child(shellHandle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
				GTK.gtk_widget_unparent(child);
			}
		} else {
			long list = GTK3.gtk_container_get_children (shellHandle);
			long listIterator = list;
			while (listIterator != 0) {
				GTK3.gtk_container_remove (shellHandle, OS.g_list_data (listIterator));
				listIterator = OS.g_list_next(listIterator);
			}
			OS.g_list_free (list);
		}
	}

	if (GTK.GTK4) {
		GTK4.gtk_window_set_child (shellHandle, vboxHandle);
	} else {
		GTK3.gtk_container_add (shellHandle, vboxHandle);
	}

	if (scrolledHandle != 0) GTK.gtk_widget_show (scrolledHandle);
	if (handle != 0) GTK.gtk_widget_show (handle);
	if (vboxHandle != 0) GTK.gtk_widget_show (vboxHandle);
}

@Override
long sizeAllocateProc (long handle, long arg0, long user_data) {
	int offset = 16;
	int[] x = new int[1], y = new int[1];
	if (GTK.GTK4) {
		double[] xDouble = new double[1], yDouble = new double[1];
		display.getPointerPosition(xDouble, yDouble);

		x[0] = (int)xDouble[0];
		y[0] = (int)yDouble[0];
	} else {
		display.getWindowPointerPosition(0, x, y, null);
	}

	y[0] += offset;
	GdkRectangle dest = new GdkRectangle ();
	long display = GDK.gdk_display_get_default();
	long monitor = GDK.gdk_display_get_monitor_at_point(display, x[0], y[0]);
	GDK.gdk_monitor_get_geometry(monitor, dest);
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (handle, allocation);
	int width = allocation.width;
	int height = allocation.height;
	if (x[0] + width > dest.x + dest.width) {
		x [0] = (dest.x + dest.width) - width;
	}
	if (y[0] + height > dest.y + dest.height) {
		y[0] = (dest.y + dest.height) - height;
	}
	GTK3.gtk_window_move (handle, x [0], y [0]);
	return 0;
}

@Override
long sizeRequestProc (long handle, long arg0, long user_data) {
	GTK.gtk_widget_hide (handle);
	return 0;
}

@Override
boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}
int trimHeight () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	if (fullScreen) return 0;
	// Shells with both ON_TOP and RESIZE set only use border, not trim.
	// See bug 319612.
	if (isCustomResize()) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.trimHeights [Display.TRIM_TITLE_RESIZE];
		if (hasBorder) return display.trimHeights [Display.TRIM_TITLE_BORDER];
		return display.trimHeights [Display.TRIM_TITLE];
	}
	if (hasResize) return display.trimHeights [Display.TRIM_RESIZE];
	if (hasBorder) return display.trimHeights [Display.TRIM_BORDER];
	return display.trimHeights [Display.TRIM_NONE];
}

int trimWidth () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	if (fullScreen) return 0;
	// Shells with both ON_TOP and RESIZE set only use border, not trim.
	// See bug 319612.
	if (isCustomResize()) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.trimWidths [Display.TRIM_TITLE_RESIZE];
		if (hasBorder) return display.trimWidths [Display.TRIM_TITLE_BORDER];
		return display.trimWidths [Display.TRIM_TITLE];
	}
	if (hasResize) return display.trimWidths [Display.TRIM_RESIZE];
	if (hasBorder) return display.trimWidths [Display.TRIM_BORDER];
	return display.trimWidths [Display.TRIM_NONE];
}

void updateModal () {
	if (!GTK.GTK4 && OS.isX11() && GTK.GTK_IS_PLUG (shellHandle)) return;
	long group = 0;
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
				isModalShell = GTK.gtk_window_get_modal (shellHandle);
				if (isModalShell) GTK.gtk_window_set_modal (shellHandle, false);
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
	if (group == 0) {
		/*
		* Feature in GTK. Starting with GTK version 2.10, GTK
		* doesn't assign windows to a default group. The fix is to
		* get the handle of the default group and add windows to the
		* group.
		*/
		group = GTK.gtk_window_get_group(0);
	}
	if (group != 0) {
		GTK.gtk_window_group_add_window (group, shellHandle);
		if (isModalShell) GTK.gtk_window_set_modal (shellHandle, true);
	} else {
		if (modalGroup != 0) {
			GTK.gtk_window_group_remove_window (modalGroup, shellHandle);
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
					GTK.gtk_widget_hide(shells[i].shellHandle);
				}
			} else {
				if (shells[i].showWithParent) {
					shells[i].showWithParent = false;
					GTK.gtk_widget_show(shells[i].shellHandle);
				}
			}
		}
	}
}

@Override
void deregister () {
	super.deregister ();
	Widget disposed = display.removeWidget (shellHandle);
	if(shellHandle != 0 && !(disposed instanceof Shell)) {
		SWT.error(SWT.ERROR_INVALID_RETURN_VALUE, null, ". Wrong widgetTable entry: " + disposed + " removed for shell: " + this + display.dumpWidgetTableInfo());
	}
	if(Display.strictChecks) {
		Shell[] shells = display.getShells();
		for (Shell shell : shells) {
			if(shell == this) {
				SWT.error(SWT.ERROR_INVALID_RETURN_VALUE, null, ". Disposed shell still in the widgetTable: " + this + display.dumpWidgetTableInfo());
			}
		}
	}
}

boolean requiresUngrab () {
	return OS.isWayland() && (style & SWT.ON_TOP) != 0 && (style & SWT.NO_FOCUS) == 0;
}

/**
 * SWT.ON_TOP shells on Wayland requires gdk_seat_grab to grab keyboard/input focus,
 * the grabbed focus need to be removed when Shell is disposed/hidden.
 */
void checkAndUngrabFocus () {
	/*
	 * Bug 515773, 542104: Wayland POPUP window limitations
	 * In bringToTop(), we grabbed keyboard/pointer focus to popup shell, which needs to
	 * be ungrabbed when the Shell is disposed. There are two cases here:
	 *
	 * 1) If the popup shell is a regular popup window attached to a toplevel window,
	 * like Javadoc popup, we can just ungrab the window.
	 *
	 * 2) If the popup shell is attached to another popup, like auto-completion details,
	 * ungrabbing the focus will also remove the grab for its parent. (To see this,
	 * focus on the completion details page using Tab and hit Esc makes the parent
	 * completion list disposed as well.) To deal with this, we should not ungrab focus
	 * for child popup shells so that focus is restored to the parent popup when the child
	 * is disposed.
	 * **NOTE**: Not ungrabbing focus for child popup restores focus to parent popup
	 * assumes that GdkSeat are the same for parent and child, which seems to be the case.
	 */
	if (requiresUngrab() && !isMappedToPopup() && grabbedFocus) {
		long gdkResource, display;
		if (GTK.GTK4) {
			gdkResource = gtk_widget_get_surface (shellHandle);
			display = GDK.gdk_surface_get_display(gdkResource);
		} else {
			gdkResource = gtk_widget_get_window (shellHandle);
			display = GDK.gdk_window_get_display(gdkResource);
			GTK3.gtk_grab_remove(shellHandle);
		}
		long seat = GDK.gdk_display_get_default_seat(display);
		GDK.gdk_seat_ungrab(seat);
		grabbedFocus = false;
	}
}

@Override
public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (isDisposed()) return;
	fixActiveShell ();
	checkAndUngrabFocus();
	/*
	 * Bug 540166: Dispose the popup child if any when the parent is disposed so that
	 * it does not remain open forever.
	 */
	if (popupChild != null && popupChild.shellHandle != 0 && !popupChild.isDisposed()) {
		popupChild.dispose();
	}
	GTK.gtk_widget_hide (shellHandle);
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

@Override
Rectangle getBoundsInPixels () {
	checkWidget ();
	int [] x = new int [1], y = new int [1];
	if ((state & Widget.DISPOSE_SENT) == 0) {
		if (GTK.GTK4) {
			// TODO: GTK4 GtkWindow no longer has the ability to get position
		} else {
			GTK3.gtk_window_get_position (shellHandle, x, y);
		}
	} else {
		if (GTK.GTK4) {
			/* TODO: GTK4 Coordinate system is now surface relative, therefore can no longer obtain
			 * root coordinates. Ideas include using the popup GdkSurface which allows you to get
			 * parent relative x and y coords. */
		} else {
			GDK.gdk_window_get_root_origin(GTK3.gtk_widget_get_window(shellHandle), x, y);
		}
	}
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (vboxHandle, allocation);
	int width = allocation.width;
	int height = allocation.height;
	int border = 0;
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0 || isCustomResize()) {
		border = gtk_container_get_border_width_or_margin (shellHandle);
	}
	return new Rectangle (x [0], y [0], width + trimWidth () + 2*border, height + trimHeight () + 2*border);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

@Override
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

@Override
void releaseWidget () {
	Region regionToDispose = null;
	if (originalRegion != null) regionToDispose  = region;
	super.releaseWidget ();
	destroyAccelGroup ();
	display.clearModal (this);
	if (display.activeShell == this) display.activeShell = null;
	if (tooltipsHandle != 0) OS.g_object_unref (tooltipsHandle);
	tooltipsHandle = 0;
	if (group != 0) OS.g_object_unref (group);
	group = modalGroup = 0;
	lastActive = null;
	if (regionToDispose != null) {
		regionToDispose.dispose();
	}
}

@Override
Point getWindowOrigin () {
	if (!mapped) {
		/*
		 * Special case: The handle attributes are not initialized until the
		 * shell is made visible, so gdk_window_get_origin () always returns {0, 0}.
		 *
		 * Once the shell is realized, gtk_window_get_position () includes
		 * window trims etc. from the window manager. That's why getLocation ()
		 * is not safe to use for coordinate mappings after the shell has been made visible.
		 */
		return getLocationInPixels ();
	}
	return super.getWindowOrigin( );
}

@Override
Point getSurfaceOrigin () {
	if (!mapped) {
		return getLocationInPixels ();
	}
	return super.getSurfaceOrigin( );
}

}

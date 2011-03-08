/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
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
 * IMPORTANT: This class is not intended to be subclassed.
 * </p>
 *
 * @see Decorations
 * @see SWT
 * @see <a href="http://www.eclipse.org/swt/snippets/#shell">Shell snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Shell extends Decorations {
	int shellHandle, focusProxy;
	boolean reparented, realized, moved, resized, opened, modified, center;
	int oldX, oldY, oldWidth, oldHeight;
	Control lastActive;

	static final  int MAXIMUM_TRIM = 128;
	static final  byte [] WM_DELETE_WINDOW = Converter.wcsToMbcs(null, "WM_DELETE_WINDOW\0");
	static final  byte [] _NET_WM_STATE = Converter.wcsToMbcs(null, "_NET_WM_STATE\0");
	static final  byte [] _NET_WM_STATE_MAXIMIZED_VERT = Converter.wcsToMbcs(null, "_NET_WM_STATE_MAXIMIZED_VERT\0");
	static final  byte [] _NET_WM_STATE_MAXIMIZED_HORZ = Converter.wcsToMbcs(null, "_NET_WM_STATE_MAXIMIZED_HORZ\0");
	static final  byte [] _NET_WM_STATE_FULLSCREEN = Converter.wcsToMbcs(null, "_NET_WM_STATE_FULLSCREEN\0");
	static final int BORDER = 2;
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
			this.shellHandle = handle;
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

public static Shell motif_new (Display display, int handle) {
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
	return new Shell (display, null, SWT.NO_TRIM, handle, false);
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
public void addShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Activate,typedListener);
	addListener(SWT.Close,typedListener);
	addListener(SWT.Deactivate,typedListener);
	addListener(SWT.Iconify,typedListener);
	addListener(SWT.Deiconify,typedListener);
}
void adjustTrim () {
	if (display.ignoreTrim) return;
	if (OS.XtIsSubclass (shellHandle, OS.overrideShellWidgetClass ())) {
		return;
	}
	int [] argList = {OS.XmNoverrideRedirect, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	if (argList [1] != 0) return;
	
	/* Query the trim insets */
	int shellWindow = OS.XtWindow (shellHandle);
	if (shellWindow == 0) return;
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
	
	/* Find the direct child of the root window */
	int [] unused = new int [1];
	int [] rootWindow = new int [1];
	int [] parent = new int [1];
	int [] ptr = new int [1];
	int trimWindow = shellWindow;
	OS.XQueryTree (xDisplay, trimWindow, rootWindow, parent, ptr, unused);
	if (ptr [0] != 0) OS.XFree (ptr [0]);
	if (parent [0] == 0) return;
	while (parent [0] != rootWindow [0]) {
		trimWindow = parent [0];
		OS.XQueryTree (xDisplay, trimWindow, unused, parent, ptr, unused);
		if (ptr [0] != 0) OS.XFree (ptr [0]);
		if (parent [0] == 0) return;	
	}
	
	/*
	 * Translate the coordinates of the shell window to the
	 * coordinates of the direct child of the root window
	 */
	if (shellWindow == trimWindow) return;

	/* Query the border width of the direct child of the root window */
	int [] trimBorder = new int [1];
	int [] trimWidth = new int [1];
	int [] trimHeight = new int [1];
	int [] trimX = new int [1];
	int [] trimY = new int [1];
	OS.XGetGeometry (xDisplay, trimWindow, unused, trimX, trimY, trimWidth, trimHeight, trimBorder, unused);

	/* Query the border width of the direct child of the shell window */
	int [] shellBorder = new int [1];
	int [] shellWidth = new int [1];
	int [] shellHeight = new int [1];
	OS.XGetGeometry (xDisplay, shellWindow, unused, unused, unused, shellWidth, shellHeight, shellBorder, unused);
	
	/* Query the trim-adjusted position of the inner window */
	short [] inner_x = new short [1], inner_y = new short [1];
	OS.XtTranslateCoords (shellHandle, (short) 0, (short) 0, inner_x, inner_y);

	/* Calculate the trim */
	int width = (trimWidth [0] + (trimBorder [0] * 2)) - (shellWidth [0] + (shellBorder [0] * 2));
	int height = (trimHeight [0] + (trimBorder [0] * 2)) - (shellHeight [0] + (shellBorder [0] * 2));
	int leftInset = inner_x [0] - trimX [0];
	int topInset = inner_y [0] - trimY [0];

	/*
	* Depending on the window manager, the algorithm to compute the window
	* trim sometimes chooses the wrong X window, causing a large incorrect
	* value to be calculated.  The fix is to ignore the trim values if they
	* are too large.
	*/
	if (width > MAXIMUM_TRIM || height > MAXIMUM_TRIM) {
		display.ignoreTrim = true;
		return;
	}
	
	/* Update the trim guesses to match the query */
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	if ((style & SWT.NO_TRIM) == 0) {
		hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
		hasResize = (style & SWT.RESIZE) != 0;
		hasBorder = (style & SWT.BORDER) != 0;
	}
	if (hasTitle) {
		if (hasResize)  {
			display.leftTitleResizeWidth = leftInset;
			display.rightTitleResizeWidth = width - leftInset;
			display.topTitleResizeHeight = topInset;
			display.bottomTitleResizeHeight = height - topInset;
			return;
		}
		if (hasBorder) {
			display.leftTitleBorderWidth = leftInset;
			display.rightTitleBorderWidth = width - leftInset;
			display.topTitleBorderHeight = topInset;
			display.bottomTitleBorderHeight = height - topInset;
			return;
		}
		display.leftTitleWidth = leftInset;
		display.rightTitleWidth = width - leftInset;
		display.topTitleHeight = topInset;
		display.bottomTitleHeight = height - topInset;
		return;
	}
	if (hasResize) {
		display.leftResizeWidth = leftInset;
		display.rightResizeWidth = width - leftInset;
		display.topResizeHeight = topInset;
		display.bottomResizeHeight = height - topInset;
		return;
	}
	if (hasBorder) {
		display.leftBorderWidth = leftInset;
		display.rightBorderWidth = width - leftInset;
		display.topBorderHeight = topInset;
		display.bottomBorderHeight = height - topInset;
		return;
	}
}
int borderHandle () {
	return handle;
}
void bringToTop (boolean force) {
	/*
	* Feature in X.  Calling XSetInputFocus() when the
	* widget is not viewable causes an X bad match error.
	* The fix is to call XSetInputFocus() when the widget
	* is viewable.
	*/
	if (minimized) return;
	if (!isVisible ()) return;
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (focusProxy != 0 ? focusProxy : shellHandle);
	if (xWindow == 0) return;
	if (!force) {
		int [] buffer1 = new int [1], buffer2 = new int [1];
		OS.XGetInputFocus (xDisplay, buffer1, buffer2);
		if (buffer1 [0] == 0) return;
		int handle = OS.XtWindowToWidget (xDisplay, buffer1 [0]);
		if (handle == 0) return;
	}
	int shellWindow = OS.XtWindow (shellHandle);
	if (shellWindow != 0) OS.XRaiseWindow (xDisplay, shellWindow);
	OS.XSetInputFocus (xDisplay, xWindow, OS.RevertToParent, OS.CurrentTime);
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
	checkWidget();
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
	/*
	* Feature in Motif.  There is no way to get the single pixel border
	* surrounding a TopLevelShell or a TransientShell. Attempts to set a
	* border on either the shell handle or the main window handle fail.
	* The fix is to set the border on the client area.  Therefore, the
	* border must be added back into the trim.
	*/
	int border = 0;
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0) {
		int [] argList = {OS.XmNborderWidth, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		border = argList [1];
	}
	if (isCustomResize ()) {
		int [] argList = {OS.XmNmainWindowMarginHeight, 0};
		OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
		border = argList [1];
	}
	trim.x -= trimLeft () + border;
	trim.y -= trimTop () + border;
	trim.width += trimWidth () + (border * 2);
	trim.height += trimHeight () + imeHeight () + (border * 2);
	return trim;
}
void createFocusProxy () {
	if (focusProxy != 0) return;
	int [] argList = {OS.XmNx, -1, OS.XmNy, -1, OS.XmNwidth, 1, OS.XmNheight, 1};
	focusProxy = OS.XmCreateDrawingArea (scrolledHandle, null, argList, argList.length / 2);
	if (focusProxy == 0) error (SWT.ERROR_NO_HANDLES);
	OS.XtSetMappedWhenManaged (focusProxy, false);
	OS.XtManageChild (focusProxy);
	OS.XtSetMappedWhenManaged (focusProxy, true);
}
void createHandle (int index) {
	state |= CANVAS;
	if (shellHandle == 0) {
		int decorations = 0;
		if ((style & SWT.NO_TRIM) == 0) {
			if ((style & SWT.MIN) != 0) decorations |= OS.MWM_DECOR_MINIMIZE;
			if ((style & SWT.MAX) != 0) decorations |= OS.MWM_DECOR_MAXIMIZE;
			if ((style & SWT.RESIZE) != 0) decorations |= OS.MWM_DECOR_RESIZEH;
			if ((style & SWT.BORDER) != 0) decorations |= OS.MWM_DECOR_BORDER;
			if ((style & SWT.MENU) != 0) decorations |= OS.MWM_DECOR_MENU;
			if ((style & SWT.TITLE) != 0) decorations |= OS.MWM_DECOR_TITLE;
			/*
			* Feature in Motif.  Under some Window Managers (Sawmill), in order
			* to get any border at all from the window manager it is necessary
			* to set MWM_DECOR_BORDER.  The fix is to force these bits when any
			* kind of border is requested.
			*/
			if ((style & SWT.RESIZE) != 0) decorations |= OS.MWM_DECOR_BORDER;
		}
		
		/*
		* Note: Motif treats the modal values as hints to the Window Manager.
		* For example, Enlightenment treats all modes except for SWT.MODELESS
		* as SWT.APPLICATION_MODAL.  The Motif Window Manager honours all modes.
		*/
		int inputMode = OS.MWM_INPUT_MODELESS;
		if ((style & SWT.PRIMARY_MODAL) != 0) inputMode = OS.MWM_INPUT_PRIMARY_APPLICATION_MODAL;
		if ((style & SWT.APPLICATION_MODAL) != 0) inputMode = OS.MWM_INPUT_FULL_APPLICATION_MODAL;
		if ((style & SWT.SYSTEM_MODAL) != 0) inputMode = OS.MWM_INPUT_SYSTEM_MODAL;
		
		/* 
		* Bug in Motif.  For some reason, if the title string
		* length is not a multiple of 4, Motif occasionally
		* draws garbage after the last character in the title.
		* The fix is to pad the title.
		*/
		byte [] buffer = {(byte)' ', 0, 0, 0};
		int ptr = OS.XtMalloc (buffer.length);
		OS.memmove (ptr, buffer, buffer.length);
		int [] argList1 = {
			OS.XmNmwmInputMode, inputMode,
			OS.XmNmwmDecorations, decorations,
			OS.XmNoverrideRedirect, (style & SWT.ON_TOP) != 0 ? 1 : 0,
			OS.XmNtitle, ptr,
		};
		
		/* 
		* Feature in Motif.  On some Window Managers, when a top level
		* shell is created with no decorations, the Window Manager does
		* not reparent the window regardless of the XmNoverrideRedirect
		* resource.  The fix is to treat the window as if it has been
		* reparented by the Window Manager despite the fact that this
		* has not really happened.
		*/	
		if (isUndecorated ()) {
			reparented = true;
		} 
		
		/*
		* Feature in Motif.  When a top level shell has no parent and is
		* application modal, Motif does not honour the modality.  The fix
		* is to create the shell as a child of a hidden shell handle, the
		* same way that XmNoverrideRedirect shells without parents are
		* created.
		*/
		byte [] appClass = display.appClass;
		if (parent == null && (style & SWT.ON_TOP) == 0 && inputMode != OS.MWM_INPUT_FULL_APPLICATION_MODAL) {
			int xDisplay = display.xDisplay;
			int widgetClass = OS.applicationShellWidgetClass ();
			shellHandle = OS.XtAppCreateShell (display.appName, appClass, widgetClass, xDisplay, argList1, argList1.length / 2);
		} else {
			int widgetClass = OS.transientShellWidgetClass ();
//			if ((style & SWT.ON_TOP) != 0) {
//				widgetClass = OS.OverrideShellWidgetClass ();
//			}
			int parentHandle = display.shellHandle;
			if (parent != null) parentHandle = parent.handle;
			shellHandle = OS.XtCreatePopupShell (appClass, widgetClass, parentHandle, argList1, argList1.length / 2);
		}
		OS.XtFree (ptr);
		if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
		if (handle != 0) {
			OS.XtSetMappedWhenManaged (shellHandle, false);
			OS.XtRealizeWidget (shellHandle);
			OS.XtSetMappedWhenManaged (shellHandle, true);
			int xDisplay = display.xDisplay;
			int xWindow = OS.XtWindow (shellHandle);
			if (xWindow == 0) error (SWT.ERROR_NO_HANDLES);
			/*
			* NOTE:  The embedded parent handle must be realized
			* before embedding and cannot be realized here because
			* the handle belongs to another thread.
			*/
			OS.XReparentWindow (xDisplay, xWindow, handle, 0, 0);
			handle = 0;
		}
		
		/* Create scrolled handle */
		createHandle (index, shellHandle, true);
	} else {
		int [] buffer = new int [1];
		int [] argList = {OS.XmNchildren, 0, OS.XmNnumChildren, 0};
		OS.XtGetValues (shellHandle, argList, argList.length / 2);
		if (argList [3] < 1) error (SWT.ERROR_NO_HANDLES);
		OS.memmove (buffer, argList [1], 4);
		scrolledHandle = buffer [0];
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
		argList [1] = argList [3] = 0;
		OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
		if (argList [3] < 4) error (SWT.ERROR_NO_HANDLES);
		OS.memmove (buffer, argList [1] + (argList [3] - 1) * 4, 4);
		handle = buffer [0];
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	}

	/*
	* Feature in Motif.  There is no way to get the single pixel
	* border surrounding a TopLevelShell or a TransientShell.
	* Attempts to set a border on either the shell handle
	* or the main window handle fail.  The fix is to set the border
	* on the client area.
	*/
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0) {
		int [] argList2 = {OS.XmNborderWidth, 1};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	}
	if (isCustomResize ()) {
		int [] argList2 = {OS.XmNmainWindowMarginWidth, BORDER, OS.XmNmainWindowMarginHeight, BORDER};
		OS.XtSetValues (scrolledHandle, argList2, argList2.length / 2);
	} 
	
	/*
	* Feature in Motif. There is no Motif API to negotiate for the
	* status line. The fix is to force the status line to appear
	* by creating a hidden text widget.  This is much safer than
	* using X API because this may conflict with Motif.
	*
	* Note that  XmNtraversalOn must be set to FALSE or the shell
	* will not take focus when the user clicks on it.
	*/
	if ((style & SWT.ON_TOP) == 0) {
		int [] argList3 = {OS.XmNtraversalOn, 0};
		int textHandle = OS.XmCreateTextField (handle, null, argList3, argList3.length / 2);
		if (textHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}
}
void deregister () {
	super.deregister ();
	display.removeWidget (shellHandle);
}
void destroyWidget () {
	/*
	* Hide the shell before calling XtDestroyWidget ()
	* so that the shell will disappear without having
	* to dispatch events.  Otherwise, the user will be
	* able to interact with the trimmings between the
	* time that the shell is destroyed and the next
	* event is dispatched.
	*/
	if (OS.XtIsRealized (shellHandle)) {
		if (OS.XtIsTopLevelShell (shellHandle)) {
			OS.XtUnmapWidget (shellHandle);
		} else {
			OS.XtPopdown (shellHandle);
		}
	}
	super.destroyWidget ();
}

public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (isDisposed()) return;

	/*
	* This code is intentionally commented.  On some
	* platforms, the owner window is repainted right
	* away when the dialog window exits.  This behavior
	* is currently unspecified.
	*/
//	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
//	Display oldDisplay = display;

	/*
	* Feature in Motif.  When the active shell is disposed,
	* Motif assigns focus temporarily to the root window
	* unless it has previously been told to do otherwise.
	* The fix is to make the parent be the active top level
	* shell when the child shell is disposed.
	*/
	if (parent != null) {
		Shell activeShell = display.getActiveShell ();
		if (activeShell == this) {
			Shell shell = parent.getShell ();
			shell.bringToTop (false);
		}
	}
	super.dispose ();
	
	/*
	* This code intentionally commented.
	*/
//	if (oldDisplay != null) oldDisplay.update ();
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	enableHandle (enabled, shellHandle);
}
Control findBackgroundControl () {
	return (state & BACKGROUND) != 0 || backgroundImage != null ? this : null;
}
Composite findDeferredControl () {
	return layoutCount > 0 ? this : null;
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
public int getBorderWidth () {
	checkWidget();
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	return argList [1];
}
public Rectangle getBounds () {
	checkWidget();
	Rectangle bounds = new Rectangle (0, 0, 0, 0);
	getBounds (null, null, bounds);
	return bounds;
}
void getBounds(Point location, Point size, Rectangle bounds) {
	int x = 0, y = 0;
	if (location != null || bounds != null) {
		/*
		* Bug in Motif.  For some reason, XtTranslateCoords() returns different
		* values depending on whether XtMoveWidget() or XtConfigureWidget() has
		* been called.  This only happens after the shell has been realized.
		* The fix is to use XTranslateCoordinates() instead.
		*/
		if (OS.XtIsRealized (shellHandle)) {
			int xDisplay = OS.XtDisplay (shellHandle);
			int xWindow = OS.XtWindow (shellHandle);
			int[] root_x = new int[1], root_y = new int[1], child = new int[1];
			/* Flush outstanding move and resize requests */
			OS.XSync (xDisplay, false);
			OS.XTranslateCoordinates (xDisplay, xWindow, OS.XDefaultRootWindow (xDisplay), 0, 0, root_x, root_y, child);
			x = root_x [0];
			y = root_y [0];
		} else {
			short [] root_x = new short [1], root_y = new short [1];
			OS.XtTranslateCoords (shellHandle, (short) 0, (short) 0, root_x, root_y);
			x = root_x [0];
			y = root_y [0];
		}
		if (reparented) {
			x -= trimLeft ();
			y -= trimTop ();
		}
	}
	int width = 0, height = 0;
	if (size != null || bounds != null) {
		int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
		OS.XtGetValues (shellHandle, argList, argList.length / 2);
		int border = argList [5];
		int trimWidth = trimWidth (), trimHeight = trimHeight ();
		width = argList [1] + trimWidth + (border * 2);		
		height = argList [3] + trimHeight + (border * 2);
	}
	if (location != null) {
		location.x = x;
		location.y = y;
	}
	if (size != null) {
		size.x = width;
		size.y = height;
	}
	if (bounds != null) {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
	}
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
	int xDisplay = OS.XtDisplay (shellHandle);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow != 0) {
		int property = OS.XInternAtom (xDisplay, _NET_WM_STATE, true);
		if (property != 0) { 
			int[] type = new int[1], format = new int[1], nitems = new int[1], bytes_after = new int[1], atoms = new int[1];
			OS.XGetWindowProperty (xDisplay, xWindow, property, 0, Integer.MAX_VALUE, false, OS.XA_ATOM, type, format, nitems, bytes_after, atoms);
			boolean result = false;
			if (type [0] != OS.None) {
				int fullScreen = OS.XInternAtom (xDisplay, _NET_WM_STATE_FULLSCREEN, true);
				if (fullScreen != 0) {
					int[] atom = new int[1];
					for (int i=0; i<nitems [0]; i++) {
						OS.memmove(atom, atoms [0] + i * 4, 4);
						if (atom [0] == fullScreen) {
							result = true;
							break;
						}
					}
				}
			}
			if (atoms [0] != 0) OS.XFree (atoms [0]);
			return result;
		}
	}
	return false;
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
public Point getLocation () {
	checkWidget();
	Point location = new Point (0, 0);
	getBounds (location, null, null);
	return location;
}
public boolean getMaximized () {
	checkWidget();
	int xDisplay = OS.XtDisplay (shellHandle);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow != 0) {
		int property = OS.XInternAtom (xDisplay, _NET_WM_STATE, true);
		if (property != 0) { 
			int[] type = new int[1], format = new int[1], nitems = new int[1], bytes_after = new int[1], atoms = new int[1];
			OS.XGetWindowProperty (xDisplay, xWindow, property, 0, Integer.MAX_VALUE, false, OS.XA_ATOM, type, format, nitems, bytes_after, atoms);
			boolean result = false;
			if (type [0] != OS.None) {
				int maximizedHorz = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_HORZ, true);
				int maximizedVert = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_VERT, true);
				if (maximizedHorz != 0 && maximizedVert != 0) {
					int[] atom = new int[1];
					for (int i=0; i<nitems [0]; i++) {
						OS.memmove(atom, atoms [0] + i * 4, 4);
						if (atom [0] == maximizedHorz || atom [0] == maximizedVert) {
							result = true;
							break;
						}
					}
				}
			}
			if (atoms [0] != 0) OS.XFree (atoms [0]);
			return result;
		}
	}
	return super.getMaximized ();
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
	int [] argList = {OS.XmNminWidth, 0, OS.XmNminHeight, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	int width = Math.max (1, Math.max (0, argList [1]) + trimWidth ());
	int height = Math.max (1, Math.max (0, argList [3]) + trimHeight ());
	return new Point (width, height);
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
	/* This method is needed for the @since 3.0 Javadoc */
	checkWidget ();
	return region;
}
int getResizeMode (int x, int y) {
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNmainWindowMarginWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	int width = argList [1];
	int height = argList [3];
	int border = argList [5];
	int mode = 0;
	if (y >= height - border) {
		mode = OS.XC_bottom_side ;
		if (x >= width - border - 16) mode = OS.XC_bottom_right_corner;
		else if (x <= border + 16) mode = OS.XC_bottom_left_corner;
	} else if (x >= width - border) {
		mode = OS.XC_right_side;
		if (y >= height - border - 16) mode = OS.XC_bottom_right_corner;
		else if (y <= border + 16) mode = OS.XC_top_right_corner;
	} else if (y <= border) {
		mode = OS.XC_top_side;
		if (x <= border + 16) mode = OS.XC_top_left_corner;
		else if (x >= width - border - 16) mode = OS.XC_top_right_corner;
	} else if (x <= border) {
		mode = OS.XC_left_side;
		if (y <= border + 16) mode = OS.XC_top_left_corner;
		else if (y >= height - border - 16) mode = OS.XC_bottom_left_corner;
	}
	return mode;
}
public Shell getShell () {
	checkWidget();
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
	Point size = new Point (0, 0);
	getBounds (null, size, null);
	return size;
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
	checkWidget();
	if (!OS.XtIsRealized (handle)) return false;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return false;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return false;
	XWindowAttributes attributes = new XWindowAttributes ();
	OS.XGetWindowAttributes (xDisplay, xWindow, attributes);
	if (attributes.map_state == OS.IsViewable && reparented) return true;
	int [] argList = {OS.XmNmappedWhenManaged, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	return minimized && attributes.map_state == OS.IsUnviewable && argList [1] != 0;
}
boolean hasBorder () {
	return false;
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	OS.XtInsertEventHandler (shellHandle, OS.StructureNotifyMask, false, windowProc, STRUCTURE_NOTIFY, OS.XtListTail);
	if (OS.XtIsSubclass (shellHandle, OS.overrideShellWidgetClass ())) return;
	OS.XtInsertEventHandler (shellHandle, OS.FocusChangeMask, false, windowProc, FOCUS_CHANGE, OS.XtListTail);
	int [] argList = {OS.XmNdeleteResponse, OS.XmDO_NOTHING};
	OS.XtSetValues (shellHandle, argList, argList.length / 2);
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay != 0) {
		int atom = OS.XmInternAtom (xDisplay, WM_DELETE_WINDOW, false);	
		OS.XmAddWMProtocolCallback (shellHandle, atom, windowProc, DELETE_WINDOW);
	}
	if (isCustomResize ()) {
		OS.XtAddEventHandler (scrolledHandle, OS.ButtonPressMask, false, windowProc, BUTTON_PRESS);
		OS.XtAddEventHandler (scrolledHandle, OS.PointerMotionMask | OS.PointerMotionHintMask, false, windowProc, POINTER_MOTION);
		OS.XtAddEventHandler (scrolledHandle, OS.LeaveWindowMask, false, windowProc, LEAVE_WINDOW);
		OS.XtInsertEventHandler (scrolledHandle, OS.ExposureMask, false, windowProc, EXPOSURE, OS.XtListTail);
	}
}
int imeHeight () {
	if (!OS.IsDBLocale) return 0;
	int [] argList1 = {OS.XmNheight, 0};
	OS.XtGetValues (shellHandle, argList1, argList1.length / 2);
	int [] argList2 = {OS.XmNheight, 0};
	OS.XtGetValues (scrolledHandle, argList2, argList2.length / 2);
	return argList1 [1] - argList2 [1];
}
public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}
boolean isModal () {
	checkWidget();
	int [] argList = {OS.XmNmwmInputMode, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	return (argList [1] != -1 && argList [1] != OS.MWM_INPUT_MODELESS);
}
boolean isCustomResize () {
	return (style & SWT.NO_TRIM) == 0 && (style & (SWT.RESIZE | SWT.ON_TOP)) == (SWT.RESIZE | SWT.ON_TOP);
}
boolean isUndecorated () {
	return
		(style & (SWT.SHELL_TRIM | SWT.BORDER)) == SWT.NONE ||
		(style & (SWT.NO_TRIM | SWT.ON_TOP)) != 0;
}
public boolean isVisible () {
	checkWidget();
	return getVisible ();
}
void manageChildren () {
	if ((state & FOREIGN_HANDLE) != 0) return;
	OS.XtSetMappedWhenManaged (shellHandle, false);
	super.manageChildren ();
	int width = 0, height = 0;
	if (OS.IsLinux) {
		Monitor monitor = getMonitor ();
		Rectangle rect = monitor.getClientArea ();
		width = rect.width * 5 / 8;
		height = rect.height * 5 / 8;
	} else {
		int xDisplay = OS.XtDisplay (shellHandle);
		if (xDisplay == 0) return;
		width = OS.XDisplayWidth (xDisplay, OS.XDefaultScreen (xDisplay)) * 5 / 8;
		height = OS.XDisplayHeight (xDisplay, OS.XDefaultScreen (xDisplay)) * 5 / 8;
	}
	OS.XtResizeWidget (shellHandle, width, height, 0);
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
	checkWidget();
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
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	/* Allow the busy cursor to be displayed in a disabled shell */
	int xCursor = cursor != null && !enabled ? cursor.handle : OS.None;
	propagateHandle (enabled, shellHandle, xCursor);
}
void realizeWidget () {
	if (realized) return;
	OS.XtRealizeWidget (shellHandle);
	realizeChildren ();
	realized = true;
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
			shell.release (false);
		}
	}
	super.releaseChildren (destroy);
}
void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}
void releaseParent () {
	/* Do nothing */
}
void releaseWidget () {
	super.releaseWidget ();
	lastActive = null;
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
public void removeShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Activate, listener);
	eventTable.unhook(SWT.Close, listener);
	eventTable.unhook(SWT.Deactivate, listener);
	eventTable.unhook(SWT.Iconify,listener);
	eventTable.unhook(SWT.Deiconify,listener);
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
			if (display.postFocusOut) {
				deactivate [i].postEvent (SWT.Deactivate);
			} else {
				deactivate [i].sendEvent (SWT.Deactivate);
			}
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
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	/*
	* Bug in Motif.  When a shell that is maximized is resized to
	* the same size, when the shell is unmaximized, the origin of
	* the shell is moved to (0, 0).  The fix is to detect this case
	* and avoid resizing the shell.
	* 
	* NOTE: When only the size is changed, the shell moves to (0, 0).
	* When only the location is changed, the shell is not moved.  There
	* is no fix for these problems at this time.
	*/
	if (getMaximized ()) {
		Rectangle rect = getBounds ();
		boolean sameOrigin = !move || (rect.x == x && rect.y == y);
		boolean sameExtent = !resize || (rect.width == width && rect.height == height);
		if (sameOrigin && sameExtent) return false;
	}
	if (resize) {
		int [] argList = {OS.XmNminWidth, 0, OS.XmNminHeight, 0};
		OS.XtGetValues (shellHandle, argList, argList.length / 2);
		/*
		* Feature in Motif.  Motif will not allow a window
		* to have a zero width or zero height.  The fix is
		* to ensure these values are never zero.
		*/
		width = Math.max (1, Math.max (argList [1], width - trimWidth ()));
		height = Math.max (1, Math.max (argList [3], height - trimHeight ()));
		updateResizable (width, height);
	}
	if (move && resize) {
		OS.XtConfigureWidget (shellHandle, x, y, width, height, 0);
	} else {
		if (move) OS.XtMoveWidget (shellHandle, x, y);
		if (resize) OS.XtResizeWidget (shellHandle, width, height, 0);
	}
	if (redrawWindow != 0) {
		int xDisplay = OS.XtDisplay (handle);
		OS.XResizeWindow (xDisplay, redrawWindow, width, height);
	}
	if (move && (oldX != x || oldY != y)) {
		moved = true;
		oldX = x + trimLeft ();
		oldY = y + trimTop ();
		sendEvent (SWT.Move);
		if (isDisposed ()) return false;
	}
	if (resize && (width != oldWidth || height != oldHeight)) {
		resized = true;
		oldWidth = width;
		oldHeight = height;
		sendEvent (SWT.Resize);
		if (isDisposed ()) return false;
		if (layout != null) {
			markLayout (false, false);
			updateLayout (false);
		}
	}
	return move || resize;
}
public void setEnabled (boolean enabled) {
	checkWidget ();
	if (enabled == getEnabled ()) return;
	super.setEnabled (enabled);
	if (enabled && this == display.getActiveShell ()) {
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
	if (!OS.XtIsRealized (handle)) realizeWidget ();
	int xDisplay = OS.XtDisplay (shellHandle);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) return;
	int property = OS.XInternAtom (xDisplay, _NET_WM_STATE, true);
	if (property == 0) return;
	int atom = OS.XInternAtom (xDisplay, _NET_WM_STATE_FULLSCREEN, true);
	if (atom == 0) return;
	XClientMessageEvent xEvent = new XClientMessageEvent ();
	xEvent.type = OS.ClientMessage;
	xEvent.send_event = 1;
	xEvent.display = xDisplay;
	xEvent.window = xWindow;
	xEvent.message_type = property;
	xEvent.format = 32;
	xEvent.data [0] = fullScreen ? 1 : 0;
	xEvent.data [1] = atom;
	XWindowAttributes attributes = new XWindowAttributes ();
	OS.XGetWindowAttributes (xDisplay, xWindow, attributes);
	int rootWindow = OS.XRootWindowOfScreen (attributes.screen);
	int event = OS.XtMalloc (XEvent.sizeof);
	OS.memmove (event, xEvent, XClientMessageEvent.sizeof);
	OS.XSendEvent (xDisplay, rootWindow, false, OS.SubstructureRedirectMask|OS.SubstructureNotifyMask, event);
	OS.XSync (xDisplay, false);
	OS.XtFree (event);
}
public void setMaximized (boolean maximized) {
	checkWidget();
	super.setMaximized (maximized);
	if (!OS.XtIsRealized (handle)) realizeWidget ();
	int xDisplay = OS.XtDisplay (shellHandle);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) return;
	int property = OS.XInternAtom (xDisplay, _NET_WM_STATE, true);
	if (property == 0) return;
	int hMaxAtom = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_HORZ, true);
	int vMaxAtom = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_VERT, true);
	if (hMaxAtom == 0 || vMaxAtom == 0) return;
	XClientMessageEvent xEvent = new XClientMessageEvent ();
	xEvent.type = OS.ClientMessage;
	xEvent.send_event = 1;
	xEvent.display = xDisplay;
	xEvent.window = xWindow;
	xEvent.message_type = property;
	xEvent.format = 32;
	xEvent.data [0] = maximized ? 1 : 0;
	xEvent.data [1] = hMaxAtom;
	xEvent.data [2] = vMaxAtom;
	XWindowAttributes attributes = new XWindowAttributes ();
	OS.XGetWindowAttributes (xDisplay, xWindow, attributes);
	int rootWindow = OS.XRootWindowOfScreen (attributes.screen);
	int event = OS.XtMalloc (XEvent.sizeof);
	OS.memmove (event, xEvent, XClientMessageEvent.sizeof);
	OS.XSendEvent (xDisplay, rootWindow, false, OS.SubstructureRedirectMask|OS.SubstructureNotifyMask, event);
	OS.XSync (xDisplay, false);
	OS.XtFree (event);
}
public void setMinimized (boolean minimized) {
	checkWidget();
	if (minimized == this.minimized) return;
	
	/* 
	* Bug in MOTIF.  For some reason, the receiver does not keep the
	* value of the XmNiconic resource up to date when the user minimizes
	* and restores the window.  As a result, a window that is minimized
	* by the user and then restored by the programmer is not restored.
	* This happens because the XmNiconic resource is unchanged when the
	* window is minimized by the user and subsequent attempts to set the
	* resource fail because the new value of the resource is the same as
	* the old value.  The fix is to force XmNiconic to be up to date
	* before setting the desired value.
	*/
	int [] argList = {
		OS.XmNiconic, 0,
		OS.XmNinitialState, 0,
	};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	if ((argList [1] != 0) != this.minimized) {
		argList [1] = this.minimized ? 1 : 0;
		OS.XtSetValues (shellHandle, argList, argList.length / 2);
	}

	/* Minimize or restore the shell */
	super.setMinimized (minimized);
	argList [1] = minimized ? 1 : 0;
	argList [3] = minimized ? OS.IconicState : OS.NormalState;
	OS.XtSetValues (shellHandle, argList, argList.length / 2);

	/* Force the XWindowAttributes to be up to date */
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay != 0) OS.XSync (xDisplay, false);
	
	/* Make the restored shell be the active shell */
	if (!minimized) {
		int [] argList2 = {OS.XmNmappedWhenManaged, 0};
		OS.XtGetValues (shellHandle, argList2, argList2.length / 2);
		if (argList2 [1] != 0) {
			do {
				display.update ();
			} while (!isVisible ());
			setActive ();
		}
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
	int [] argList = {
		OS.XmNminWidth, Math.max (width, trimWidth ()) - trimWidth (),
		OS.XmNminHeight, Math.max (height, trimHeight ()) - trimHeight (),
	};
	OS.XtSetValues (shellHandle, argList, argList.length / 2);
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
void setParentTraversal () {
	/* Do nothing - Child shells do not affect the traversal of their parent shell */
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
	if (region != null && region.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	super.setRegion (region);
}
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	/* Use the character encoding for the default locale */
	if (string.length () == 0) string = " ";
	byte [] buffer1 = Converter.wcsToMbcs (null, string, true);
	
	/* 
	* Bug in Motif.  For some reason, if the title string
	* length is not a multiple of 4, Motif occasionally
	* draws garbage after the last character in the title.
	* The fix is to pad the title.
	*/
	byte [] buffer2 = buffer1;
	int length = buffer1.length - 1;
	if ((length % 4) != 0) {
		buffer2 = new byte [(length + 3) / 4 * 4];
		System.arraycopy (buffer1, 0, buffer2, 0, length);
	}

	/* Set the title for the shell */
	int ptr = OS.XtMalloc (buffer2.length + 1);
	OS.memmove (ptr, buffer2, buffer2.length);
	int [] argList = {OS.XmNtitle, ptr, OS.XmNiconName, ptr};
	OS.XtSetValues (shellHandle, argList, argList.length / 2);
	OS.XtFree (ptr);
}
public void setVisible (boolean visible) {
	checkWidget();
	realizeWidget ();

	/* Show the shell */
	if (visible) {
		if (center && !moved) {
			center ();
			if (isDisposed ()) return;
		}
		sendEvent (SWT.Show);
		if (isDisposed ()) return;

		/* Map the widget */
		OS.XtSetMappedWhenManaged (shellHandle, true);
		if (OS.XtIsTopLevelShell (shellHandle)) {
			OS.XtMapWidget (shellHandle);
		} else {
			OS.XtPopup (shellHandle, OS.XtGrabNone);
		}
		
		/*
		* Force the shell to be fully exposed before returning.
		* This ensures that the shell coordinates are correct
		* when queried directly after showing the shell.
		* 
		* Note that if the parent is minimized or withdrawn
		* from the desktop, this should not be done since
		* the shell will not be mapped until the parent is
		* unminimized or shown on the desktop.
		*/
		boolean iconic = false;
		Shell shell = parent != null ? parent.getShell() : null;
		do {
			display.update ();
			if (isDisposed ()) return;
			iconic = minimized || (shell != null && shell.minimized);
		} while (!isVisible () && !iconic);
		if (!iconic) adjustTrim ();
		
		int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.APPLICATION_MODAL;
		if ((style & mask) != 0) {
			OS.XUngrabPointer (display.xDisplay, OS.CurrentTime);
		}
		opened = true;
		if (!moved) {
			moved = true;
			Point location = getLocation ();
			oldX = location.x + trimLeft ();
			oldY = location.x + trimTop ();
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
		/*
		* Feature in Motif.  When the active shell is disposed,
		* some window managers place focus in a temporary window.
		* The fix is to make the parent be the active top level
		* shell when the child shell is hidden.
		*/
		if (parent != null) {
			Shell activeShell = display.getActiveShell ();
			if (activeShell == this) {
				Shell shell = parent.getShell ();
				shell.bringToTop (false);
			}
		}
	
		/* Hide the shell */
		OS.XtSetMappedWhenManaged (shellHandle, false);
		if (OS.XtIsTopLevelShell (shellHandle)) {
			OS.XtUnmapWidget (shellHandle);
		} else {
			OS.XtPopdown (shellHandle);
		}
	
		/* If the shell is iconified, hide the icon */
		int xDisplay = OS.XtDisplay (shellHandle);
		if (xDisplay == 0) return;
		int xWindow = OS.XtWindow (shellHandle);
		if (xWindow == 0) return;
		OS.XWithdrawWindow (xDisplay, xWindow, OS.XDefaultScreen (xDisplay));
	
		sendEvent (SWT.Hide);
	}
}
void setZOrder (Control control, boolean above) {
	setZOrder (control, above, false);
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
int trimHeight () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) {
			return display.topTitleResizeHeight + display.bottomTitleResizeHeight;
		}
		if (hasBorder) {
			return display.topTitleBorderHeight + display.bottomTitleBorderHeight;
		}
		return display.topTitleHeight + display.bottomTitleHeight;
	}
	if (hasResize) {
		return display.topResizeHeight + display.bottomResizeHeight;
	}
	if (hasBorder) {
		return display.topBorderHeight + display.bottomBorderHeight;
	}
	return 0;
}
int trimLeft () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.leftTitleResizeWidth;
		if (hasBorder) return display.leftTitleBorderWidth;
		return display.leftTitleWidth;
	}
	if (hasResize) return display.leftResizeWidth;
	if (hasBorder) return display.leftBorderWidth;
	return 0;
}
int trimTop () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.topTitleResizeHeight;
		if (hasBorder) return display.topTitleBorderHeight;
		return display.topTitleHeight;
	}
	if (hasResize) return display.topResizeHeight;
	if (hasBorder) return display.topBorderHeight;
	return 0;
}
int trimWidth () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) {
			return display.leftTitleResizeWidth + display.rightTitleResizeWidth;
		}
		if (hasBorder) {
			return display.leftTitleBorderWidth + display.rightTitleBorderWidth;
		}
		return display.leftTitleWidth + display.rightTitleWidth;
	}
	if (hasResize) {
		return display.leftResizeWidth + display.rightResizeWidth;
	}
	if (hasBorder) {
		return display.leftBorderWidth + display.rightBorderWidth;
	}
	return 0;
}
void updateResizable (int width, int height) {
	if ((style & SWT.RESIZE) != 0) return;
	if (!OS.XtIsRealized (shellHandle)) return;
	XSizeHints hints = new XSizeHints ();
	hints.flags = OS.PMinSize | OS.PMaxSize | OS.PPosition;
	hints.min_width = hints.max_width = width;
	hints.min_height = hints.max_height = height;
	OS.XSetWMNormalHints (OS.XtDisplay (shellHandle), OS.XtWindow (shellHandle), hints);
}
int WM_DELETE_WINDOW (int w, int client_data, int call_data) {
	if (!isEnabled ()) return 0;
	Control widget = parent;
	while (widget != null && !(widget.getShell ().isModal ())) {
		widget = widget.parent;
	}
	if (widget == null) {
		Shell [] shells = getShells ();
		for (int i=0; i<shells.length; i++) {
			Shell shell = shells [i];
			if (shell != this && shell.isModal () && shell.isVisible ()) {
				shell.bringToTop (false);
				return 0;
			}
		}
	}
	closeWidget ();
	return 0;
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	if (w == scrolledHandle) {
		if (isCustomResize ()) {
			if ((style & SWT.ON_TOP) != 0 && (style & SWT.NO_FOCUS) == 0) {
				forceActive ();
			}
			XButtonEvent xEvent = new XButtonEvent ();
			OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
			if (xEvent.button == 1) {
				display.resizeLocationX = xEvent.x_root;
				display.resizeLocationY = xEvent.y_root;
				Point loc = new Point (0, 0);
				getBounds(loc, null, null);
				display.resizeBoundsX  = loc.x;
				display.resizeBoundsY  = loc.y;
				int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
				OS.XtGetValues (shellHandle, argList, argList.length / 2);
				display.resizeBoundsWidth = argList [1];
				display.resizeBoundsHeight = argList [3];
			}
		}
		return 0;
	}
	return super.XButtonPress(w, client_data, call_data, continue_to_dispatch);
}
int XExposure (int w, int client_data, int call_data, int continue_to_dispatch) {
	if (w == scrolledHandle) {
		if (isCustomResize ()) {
			int xDisplay = OS.XtDisplay (scrolledHandle);
			if (xDisplay == 0) return 0;
			int xWindow = OS.XtWindow (scrolledHandle);
			if (xWindow == 0) return 0;
			int xGC = OS.XCreateGC (xDisplay, xWindow, 0, null);
			if (xGC == 0) return 0;;
			OS.XSetGraphicsExposures (xDisplay, xGC, false);
			XExposeEvent xEvent = new XExposeEvent ();
			OS.memmove (xEvent, call_data, XExposeEvent.sizeof);
			int damageRgn = OS.XCreateRegion ();
			OS.XtAddExposureToRegion (call_data, damageRgn);
			OS.XSetRegion (xDisplay, xGC, damageRgn);
			int [] argList = {
					OS.XmNwidth, 0,
					OS.XmNheight, 0, 
					OS.XmNmainWindowMarginWidth, 0,
					OS.XmNmainWindowMarginHeight, 0,
					OS.XmNtopShadowColor, 0,
					OS.XmNbottomShadowColor, 0};
			OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
			int width = argList [1];
			int height = argList [3];
			int marginWidth = argList [5];
			int marginHeight = argList [7];
			int topShadowColor  = argList [9];
			int bottomShadowColor  = argList [11];
			OS.XSetForeground (xDisplay, xGC, bottomShadowColor);
			OS.XFillRectangle (xDisplay, xWindow, xGC, width - marginWidth, 0, marginWidth, height);
			OS.XFillRectangle (xDisplay, xWindow, xGC, 0, height - marginHeight, width, marginHeight);
			OS.XSetForeground (xDisplay, xGC, topShadowColor);
			int half = marginWidth / 2;
			OS.XDrawLine (xDisplay, xWindow, xGC, 0, 0, 0, height - 1);
			OS.XDrawLine (xDisplay, xWindow, xGC, half, 0, half, height - half - 1);
			OS.XDrawLine (xDisplay, xWindow, xGC, 0, 0, width - 1, 0);
			OS.XDrawLine (xDisplay, xWindow, xGC, 0, half, width - half - 1, half);
			OS.XFreeGC (xDisplay, xGC);
			OS.XDestroyRegion (damageRgn);
		}
		return 0;
	}
	return super.XExposure (w, client_data, call_data, continue_to_dispatch);
}
int XFocusChange (int w, int client_data, int call_data, int continue_to_dispatch) {
	XFocusChangeEvent xEvent = new XFocusChangeEvent ();
	OS.memmove (xEvent, call_data, XFocusChangeEvent.sizeof);
	int handle = OS.XtWindowToWidget (xEvent.display, xEvent.window);
	if (handle != shellHandle) {
		return super.XFocusChange (w, client_data, call_data, continue_to_dispatch);
	}
	if (xEvent.mode != OS.NotifyNormal) return 0;
	if (xEvent.type == OS.FocusIn && xEvent.detail == OS.NotifyInferior) {
		if (focusProxy != 0) {
			int xWindow = OS.XtWindow (focusProxy);
			int xDisplay = OS.XtDisplay (focusProxy);
			OS.XSetInputFocus (xDisplay, xWindow, OS.RevertToParent, OS.CurrentTime);
		}
	}
	switch (xEvent.detail) {
		case OS.NotifyNonlinear:
		case OS.NotifyNonlinearVirtual: {
			switch (xEvent.type) {
				case OS.FocusIn:
					if (display.postFocusOut) {
						postEvent (SWT.Activate);
					} else {
						sendEvent (SWT.Activate);
					}
					break;
				case OS.FocusOut:
					Display display = this.display;
					if (display.postFocusOut) {
						postEvent (SWT.Deactivate);
					} else {
						sendEvent (SWT.Deactivate);
					}
					Control focusedCombo = display.focusedCombo;
					display.focusedCombo = null;
					if (focusedCombo != null && focusedCombo != this && !focusedCombo.isDisposed ()) {
						display.sendFocusEvent (focusedCombo, SWT.FocusOut);
					}
					break;
			}
		}
	}
	return 0;
}
int XLeaveWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	if (w == scrolledHandle) {
		if (isCustomResize ()) {
			XCrossingEvent xEvent = new XCrossingEvent ();
			OS.memmove (xEvent, call_data, XCrossingEvent.sizeof);
			if ((xEvent.state & OS.Button1Mask) == 0) {
				int xDisplay = OS.XtDisplay (scrolledHandle);
				if (xDisplay != 0) {
					int xWindow = OS.XtWindow (scrolledHandle);
					if (xWindow != 0) {
						OS.XUndefineCursor (xDisplay, xWindow);
					}
				}
				display.resizeMode = 0;
			}
		}
		return 0;
	}
	return super.XLeaveWindow (w, client_data, call_data, continue_to_dispatch);
}
int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
	if (w == scrolledHandle) {
		if (isCustomResize ()) {
			XMotionEvent xEvent = new XMotionEvent ();
			OS.memmove (xEvent, call_data, XMotionEvent.sizeof);
			int xDisplay = xEvent.display;
			if (xDisplay == 0) return 0;
			int xWindow = xEvent.window;
			if (xWindow == 0) return 0;
			if (xEvent.is_hint != 0) {
				int [] rootX = new int [1], rootY = new int [1], windowX = new int [1], windowY = new int [1], unused = new int [1], mask = new int [1];
				if (OS.XQueryPointer (xDisplay, xWindow, unused, unused, rootX, rootY, windowX, windowY, mask) == 0) {
					return 0;
				}
				xEvent.x = windowX [0];
				xEvent.y = windowY [0];
				xEvent.x_root = rootX [0];
				xEvent.y_root = rootY [0];
				xEvent.state = mask [0];
			}
			if ((xEvent.state & OS.Button1Mask) != 0) {
				int [] argList = {OS.XmNmainWindowMarginWidth, 0};
				OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
				int [] argList2 = {OS.XmNminWidth, 0, OS.XmNminHeight, 0, };
				OS.XtGetValues (shellHandle, argList2, argList2.length / 2);
				int minWidth = argList2 [1];
				int minHeight = argList2 [3];
				int border = argList [1];
				int dx = (int)(xEvent.x_root - display.resizeLocationX);
				int dy = (int)(xEvent.y_root - display.resizeLocationY);
				int x = display.resizeBoundsX;
				int y = display.resizeBoundsY;
				int width = display.resizeBoundsWidth;
				int height = display.resizeBoundsHeight;
				int newWidth = Math.max(width - dx, Math.max(minWidth, border + border));
				int newHeight = Math.max(height - dy, Math.max(minHeight, border + border));
				switch (display.resizeMode) {
					case OS.XC_left_side:
						x += width - newWidth;
						width = newWidth;
						break;
					case OS.XC_top_left_corner:
						x += width - newWidth;
						width = newWidth;
						y += height - newHeight;
						height = newHeight;
						break;
					case OS.XC_top_side:
						y += height - newHeight;
						height = newHeight;
						break;
					case OS.XC_top_right_corner:
						width = Math.max(width + dx, Math.max(minWidth, border + border));
						y += height - newHeight;
						height = newHeight;
						break;
					case OS.XC_right_side:
						width = Math.max(width + dx, Math.max(minWidth, border + border));
						break;
					case OS.XC_bottom_right_corner:
						width = Math.max(width + dx, Math.max(minWidth, border + border));
						height = Math.max(height + dy, Math.max(minHeight, border + border));
						break;
					case OS.XC_bottom_side:
						height = Math.max(height + dy, Math.max(minHeight, border + border));
						break;
					case OS.XC_bottom_left_corner:
						x += width - newWidth;
						width = newWidth;
						height = Math.max(height + dy, Math.max(minHeight, border + border));
						break;
				}
				OS.XtConfigureWidget (shellHandle, x, y, width, height, 0);
			} else {
				int mode = getResizeMode (xEvent.x, xEvent.y);
				if (mode != display.resizeMode) {
					if (mode != 0) {
						int cursor = OS.XCreateFontCursor (xDisplay, mode);
						OS.XDefineCursor (xDisplay, xWindow, cursor);
						OS.XFreeCursor(xDisplay, cursor);
					} else {
						OS.XUndefineCursor (xDisplay, xWindow);
					}
					OS.XFlush (xDisplay);
					display.resizeMode = mode;
				}
			}
		}
		return 0;
	}
	return super.XPointerMotion (w, client_data, call_data, continue_to_dispatch);
}
int XStructureNotify (int w, int client_data, int call_data, int continue_to_dispatch) {
	XConfigureEvent xEvent = new XConfigureEvent ();
	OS.memmove (xEvent, call_data, XConfigureEvent.sizeof);
	int handle = OS.XtWindowToWidget (xEvent.display, xEvent.window);
	if (handle != shellHandle) {
		return super.XStructureNotify (w, client_data, call_data, continue_to_dispatch);
	}
	switch (xEvent.type) {
		case OS.ReparentNotify: {
			reparented = true;
			adjustTrim ();
			break;
		}
		case OS.ConfigureNotify:
			int [] root_x = new int [1], root_y = new int [1], child = new int [1];
			OS.XTranslateCoordinates (xEvent.display, xEvent.window, OS.XDefaultRootWindow (xEvent.display), 0, 0, root_x, root_y, child);
			if (!moved || oldX != root_x [0] || oldY != root_y [0]) {
				moved = true;
				oldX = root_x [0];
				oldY = root_y [0];
				sendEvent (SWT.Move);
				if (isDisposed ()) return 0;
			}
			updateResizable (xEvent.width, xEvent.height);
			if (!resized || oldWidth != xEvent.width || oldHeight != xEvent.height) {
				int xEvent1 = OS.XtMalloc (XEvent.sizeof);
				display.resizeWindow = xEvent.window;
				display.resizeWidth = xEvent.width;
				display.resizeHeight = xEvent.height;
				display.resizeCount = 0;
				int checkResizeProc = display.checkResizeProc;
				OS.XCheckIfEvent (xEvent.display, xEvent1, checkResizeProc, 0);
				OS.XtFree (xEvent1);
				if (display.resizeCount == 0) {
					resized = true;
					oldWidth = xEvent.width;
					oldHeight = xEvent.height;
					sendEvent (SWT.Resize);
					if (isDisposed ()) return 0;
					if (layout != null) {
						markLayout (false, false);
						updateLayout (false);
					}
				}
			}
			return 0;
		case OS.UnmapNotify:
			int [] argList = {OS.XmNmappedWhenManaged, 0};
			OS.XtGetValues (shellHandle, argList, argList.length / 2);
			if (argList [1] != 0) {
				minimized = true;
				sendEvent (SWT.Iconify);
			}
			return 0;
		case OS.MapNotify:
			reparented = true;
			if (minimized) {
				minimized = false;
				sendEvent (SWT.Deiconify);
			}
			return 0;
	}
	return 0;
}
}

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
	int shellHandle, focusProxy;
	boolean reparented, realized, configured;
	int oldX, oldY, oldWidth, oldHeight;
	Control lastActive;
	Region region;

	static final  byte [] WM_DELETE_WINDOW = Converter.wcsToMbcs(null, "WM_DELETE_WINDOW\0");
	static final  byte [] _NET_WM_STATE = Converter.wcsToMbcs(null, "_NET_WM_STATE\0");
	static final  byte [] _NET_WM_STATE_MAXIMIZED_VERT = Converter.wcsToMbcs(null, "_NET_WM_STATE_MAXIMIZED_VERT\0");
	static final  byte [] _NET_WM_STATE_MAXIMIZED_HORZ = Converter.wcsToMbcs(null, "_NET_WM_STATE_MAXIMIZED_HORZ\0");
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

public static Shell motif_new (Display display, int handle) {
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
	OS.XSetInputFocus (xDisplay, xWindow, OS.RevertToParent, OS.CurrentTime);
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
void closeWidget () {
	if (!isEnabled ()) return;
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
				return;
			}
		}
	}
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
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.RESIZE)) == 0) {
		int [] argList = {OS.XmNborderWidth, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		border = argList [1];
	}
	trim.x -= trimLeft ();
	trim.y -= trimTop ();
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
	state |= HANDLE | CANVAS;
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
	int orientations = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	if ((style & ~orientations) == SWT.NONE || (style & (SWT.NO_TRIM | SWT.ON_TOP)) != 0) {
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
		int widgetClass = OS.topLevelShellWidgetClass ();
		shellHandle = OS.XtAppCreateShell (display.appName, appClass, widgetClass, xDisplay, argList1, argList1.length / 2);
	} else {
		int widgetClass = OS.transientShellWidgetClass ();
//		if ((style & SWT.ON_TOP) != 0) {
//			widgetClass = OS.OverrideShellWidgetClass ();
//		}
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

	/*
	* Feature in Motif.  There is no way to get the single pixel
	* border surrounding a TopLevelShell or a TransientShell.
	* Attempts to set a border on either the shell handle
	* or the main window handle fail.  The fix is to set the border
	* on the client area.
	*/
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.RESIZE)) == 0) {
		int [] argList2 = {OS.XmNborderWidth, 1};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
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
	* Feature in Motif.  When an override-redirected shell
	* is disposed, Motif does not assign a new active top
	* level shell.  The parent shell appears to be active,
	* but XGetInputFocus returns the root window, not the
	* parent.  The fix is to make the parent be the active
	* top level shell when the child shell is disposed.
	* 
	* Feature in Motif.  When the active shell is disposed,
	* Motif assigns focus temporarily to the root window
	* unless it has previously been told to do otherwise.
	* The fix is to make the parent be the active top level
	* shell when the child shell is disposed.
	*/
	if (parent != null) {
		int [] argList = {OS.XmNoverrideRedirect, 0};
		OS.XtGetValues (shellHandle, argList, argList.length / 2);
		Shell activeShell = display.getActiveShell ();
		if (argList [1] != 0 || activeShell == this) {
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
public int getBorderWidth () {
	checkWidget();
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	return argList [1];
}
public Rectangle getBounds () {
	checkWidget();
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (shellHandle, (short) 0, (short) 0, root_x, root_y);
	if (reparented) {
		root_x [0] -= trimLeft ();
		root_y [0] -= trimTop ();
	}
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	int border = argList [5];
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	int width = argList [1] + trimWidth + (border * 2);
	int height = argList [3] + trimHeight + (border * 2);
	return new Rectangle (root_x [0], root_y [0], width, height);
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
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (shellHandle, (short) 0, (short) 0, root_x, root_y);
	if (reparented) {
		root_x [0] -= trimLeft ();
		root_y [0] -= trimTop ();
	}
	return new Point (root_x [0], root_y [0]);
}
public boolean getMaximized () {
	checkWidget();
	int xDisplay = OS.XtDisplay (shellHandle);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow != 0) {
		int property = OS.XInternAtom (xDisplay, _NET_WM_STATE, true);
		int[] type = new int[1], format = new int[1], nitems = new int[1], bytes_after = new int[1], atoms = new int[1];
		OS.XGetWindowProperty (xDisplay, xWindow, property, 0, Integer.MAX_VALUE, false, OS.XA_ATOM, type, format, nitems, bytes_after, atoms);
		boolean result = false;
		if (type [0] != OS.None) {
			int maximizedHorz = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_HORZ, true);
			int maximizedVert = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_VERT, true);
			int[] atom = new int[1];
			for (int i=0; i<nitems [0]; i++) {
				OS.memmove(atom, atoms [0] + i * 4, 4);
				if (atom [0] == maximizedHorz || atom [0] == maximizedVert) {
					result = true;
					break;
				}
			}
		}
		if (atoms [0] != 0) OS.XFree (atoms [0]);
		return result;
	}
	return super.getMaximized ();
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
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	int border = argList [5];
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	int width = argList [1] + trimWidth + (border * 2);
	int height = argList [3] + trimHeight + (border * 2);
	return new Point (width, height);
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
public boolean isVisible () {
	checkWidget();
	return getVisible ();
}
void manageChildren () {
	OS.XtSetMappedWhenManaged (shellHandle, false);
	super.manageChildren ();
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
	int width = OS.XDisplayWidth (xDisplay, OS.XDefaultScreen (xDisplay)) * 5 / 8;
	int height = OS.XDisplayHeight (xDisplay, OS.XDefaultScreen (xDisplay)) * 5 / 8;
	OS.XtResizeWidget (shellHandle, width, height, 0);
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
	if (!restoreFocus ()) traverseGroup (true);
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	propagateHandle (enabled, shellHandle, OS.None);
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
	super.releaseWidget ();
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
void saveBounds () {
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (shellHandle, (short) 0, (short) 0, root_x, root_y);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	oldX = root_x [0];
	oldY = root_y [0];
	oldWidth = argList [1];
	oldHeight = argList [3];
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
	if (resize) {
		/*
		* Feature in Motif.  Motif will not allow a window
		* to have a zero width or zero height.  The fix is
		* to ensure these values are never zero.
		*/
		width = Math.max (width - trimWidth (), 1);
		height = Math.max (height - trimHeight (), 1);
	}
	if (!reparented || !OS.XtIsRealized (shellHandle)) {
		return super.setBounds (x, y, width, height, move, resize);
	}
	if (move) {
		x += trimLeft ();
		y += trimTop ();
	}		
	if (!configured) saveBounds ();
	configured = true;
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	if (move && resize) {
		OS.XtConfigureWidget (shellHandle, x, y, width, height, 0);
	} else {
		if (move) OS.XtMoveWidget (shellHandle, x, y);
		if (resize) OS.XtResizeWidget (shellHandle, width, height, 0);
	}
	if (isFocus) caret.setFocus ();
	return move || resize;
}
public void setMaximized (boolean maximized) {
	checkWidget();
	super.setMaximized (maximized);
	if (!OS.XtIsRealized (handle)) realizeWidget();
	int xDisplay = OS.XtDisplay (shellHandle);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow != 0) {
		int property = OS.XInternAtom (xDisplay, _NET_WM_STATE, true);
		int maximizedHorz = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_HORZ, true);
		int maximizedVert = OS.XInternAtom (xDisplay, _NET_WM_STATE_MAXIMIZED_VERT, true);
		int[] atoms = new int[]{maximizedHorz, maximizedVert};
		OS.XChangeProperty (xDisplay, xWindow, property, OS.XA_ATOM, 32, OS.PropModeReplace, atoms, atoms.length);
	}
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
void setParentTraversal () {
	/* Do nothing - Child shells do not affect the traversal of their parent shell */
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
	if (!OS.XtIsRealized (shellHandle)) realizeWidget ();
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) return;
	if (region != null) {
		OS.XShapeCombineRegion (xDisplay, xWindow, OS.ShapeBounding, 0, 0, region.handle, OS.ShapeSet);
		OS.XShapeCombineRegion (xDisplay, xWindow, OS.ShapeClip, 0, 0, region.handle, OS.ShapeSet);
	} else {
		OS.XShapeCombineMask (xDisplay, xWindow, OS.ShapeBounding, 0, 0, 0, OS.ShapeSet);
		OS.XShapeCombineMask (xDisplay, xWindow, OS.ShapeClip, 0, 0, 0, OS.ShapeSet);
	}
	this.region = region;
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
		*/
		do {
			display.update ();
		} while (!isVisible ());
		adjustTrim ();
		
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		
		int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.APPLICATION_MODAL;
		if ((style & mask) != 0) {
			OS.XUngrabPointer (display.xDisplay, OS.CurrentTime);
		}
	} else {
	
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
int WM_DELETE_WINDOW (int w, int client_data, int call_data) {
	closeWidget ();
	return 0;
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
					postEvent (SWT.Activate);
					break;
				case OS.FocusOut:
					postEvent (SWT.Deactivate);
					break;
			}
		}
	}
	return 0;
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
			if (reparented) return 0;
			reparented = true;
			short [] root_x = new short [1], root_y = new short [1];
			OS.XtTranslateCoords (shellHandle, (short) 0, (short) 0, root_x, root_y);
			int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
			OS.XtGetValues (shellHandle, argList, argList.length / 2);	
			xEvent.x = root_x [0];  xEvent.y = root_y [0];
			xEvent.width = argList [1];  xEvent.height = argList [3];
			// FALL THROUGH
		}
		case OS.ConfigureNotify:
			if (!reparented) return 0;
			configured = false;
			if (oldX != xEvent.x || oldY != xEvent.y) sendEvent (SWT.Move);
			if (oldWidth != xEvent.width || oldHeight != xEvent.height) {
				int xEvent1 = OS.XtMalloc (XEvent.sizeof);
				display.resizeWindow = xEvent.window;
				display.resizeWidth = xEvent.width;
				display.resizeHeight = xEvent.height;
				display.resizeCount = 0;
				int checkResizeProc = display.checkResizeProc;
				OS.XCheckIfEvent (xEvent.display, xEvent1, checkResizeProc, 0);
				if (display.resizeCount == 0) {
					sendEvent (SWT.Resize);
					if (layout != null) layout (false);
				}
				OS.XtFree (xEvent1);
			}
			if (xEvent.x != 0) oldX = xEvent.x;
			if (xEvent.y != 0) oldY = xEvent.y;
			oldWidth = xEvent.width;
			oldHeight = xEvent.height;
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
			if (minimized) {
				minimized = false;
				sendEvent (SWT.Deiconify);
			}
			return 0;
	}
	return 0;
}
}

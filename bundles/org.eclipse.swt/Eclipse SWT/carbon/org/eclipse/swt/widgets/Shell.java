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


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.CGRect;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

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
	int shellHandle, windowGroup;
	boolean resized;
	Control lastActive;
	Region region;
	Rect rgnRect;

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

void bringToTop (boolean force) {
	if (force) {
		forceActive ();
	} else {
		setActive ();
	}
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
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrim (x, y, width, height);
	Rect rect = new Rect ();
	OS.GetWindowStructureWidths (shellHandle, rect);
	trim.x -= rect.left;
	trim.y -= rect.top;
	trim.width += rect.left + rect.right;
	trim.height += rect.top + rect.bottom;
	return trim;
}

void createHandle () {
	state |= CANVAS | GRAB | HIDDEN;
	int attributes = OS.kWindowStandardHandlerAttribute; // | OS.kWindowCompositingAttribute;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.CLOSE) != 0) attributes |= OS.kWindowCloseBoxAttribute;
		if ((style & SWT.MIN) != 0) attributes |= OS.kWindowCollapseBoxAttribute;
		if ((style & SWT.MAX) != 0) attributes |= OS.kWindowFullZoomAttribute;
		if ((style & SWT.RESIZE) != 0) {
			attributes |= OS.kWindowResizableAttribute;
			/*
			* Bug in the Macintosh.  For some reason, a window has no title bar
			* and the kWindowResizableAttribute, no rubber banding feedback is
			* given while the window is resizing.  The fix is to create the window 
			* with kWindowLiveResizeAttribute in this case.  This is inconsistent
			* with other windows, but the user will get feedback when resizing.
			*/
			if ((style & SWT.TITLE) == 0) attributes |= OS.kWindowLiveResizeAttribute;
		}
	}
	int windowClass = OS.kDocumentWindowClass;
	if ((style & (SWT.CLOSE | SWT.TITLE)) == 0) windowClass = OS.kSheetWindowClass;
//	int windowClass = parent == null ? OS.kDocumentWindowClass : OS.kSheetWindowClass;
//	if ((style & SWT.APPLICATION_MODAL) != 0) windowClass = OS.kMovableModalWindowClass;
//	if ((style & SWT.SYSTEM_MODAL) != 0) windowClass = OS.kModalWindowClass;
	Rect rect = new Rect ();
	OS.GetAvailableWindowPositioningBounds (OS.GetMainDevice (), rect);
	int width = (rect.right - rect.left) * 5 / 8;
	int height = (rect.bottom - rect.top) * 5 / 8;
	OS.SetRect (rect, (short) 0, (short) 0, (short) width, (short) height);
	int [] outWindow = new int [1];
	attributes &= OS.GetAvailableWindowAttributes (windowClass);
	OS.CreateNewWindow (windowClass, attributes, rect, outWindow);
	if (outWindow [0] == 0) error (SWT.ERROR_NO_HANDLES);
	shellHandle = outWindow [0];
	OS.RepositionWindow (shellHandle, 0, OS.kWindowCascadeOnMainScreen);
//	OS.SetThemeWindowBackground (shellHandle, (short) OS.kThemeBrushDialogBackgroundActive, false);
	int [] theRoot = new int [1];
	OS.CreateRootControl (shellHandle, theRoot);
	OS.GetRootControl (shellHandle, theRoot);
	if (theRoot [0] == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		createScrolledHandle (theRoot [0]);
	} else {
		createHandle (theRoot [0]);
	}
	OS.SetControlVisibility (topHandle (), false, false);
	int [] outGroup = new int [1];
	OS.CreateWindowGroup (OS.kWindowGroupAttrHideOnCollapse, outGroup);
	if (outGroup [0] == 0) error (SWT.ERROR_NO_HANDLES);
	windowGroup = outGroup [0];
	if (parent != null) {
		Shell shell = parent.getShell ();
		int parentGroup = shell.windowGroup;
		OS.SetWindowGroup (shellHandle, parentGroup);
		OS.SetWindowGroupParent (windowGroup, parentGroup);
	} else {
		int parentGroup = OS.GetWindowGroupOfClass (windowClass);
		OS.SetWindowGroupParent (windowGroup, parentGroup);
	}
	OS.SetWindowGroupOwner (windowGroup, shellHandle);
}

void createWidget () {
	super.createWidget ();
	layoutControl (false);
}

void deregister () {
	super.deregister ();
	int [] theRoot = new int [1];
	OS.GetRootControl (shellHandle, theRoot);
	display.removeWidget (theRoot [0]);
}

void destroyWidget () {
	Display display = this.display;
	int theWindow = shellHandle;
//	OS.HideWindow (shellHandle);
	releaseHandle ();
	if (theWindow != 0) {
		OS.DisposeWindow (theWindow);
	} 
}

void drawWidget (int control, int damageRgn, int visibleRgn, int theEvent) {
	super.drawWidget (control, damageRgn, visibleRgn, theEvent);
	/*
	* Bug in the Macintosh. In kEventWindowGetRegion, 
	* Carbon assumes the origin of the Region is (0, 0)
	* and ignores the actual origin.  This causes the 
	* window to be shifted for a non zero origin.  Also,
	* the size of the window is the size of the region
	* which may be less then the size specified in
	* setSize or setBounds.
	* The fix is to include (0, 0) and the bottom 
	* right corner of the size in the region and to
	* make these points transparent.
	*/
	if (region == null) return;
	boolean origin = region.contains (0, 0);
	boolean limit = region.contains(rgnRect.right - 1, rgnRect.bottom - 1);
	if (origin && limit) return;
	
	int[] context = new int [1];
	int port = OS.GetWindowPort (shellHandle);
	Rect portRect = new Rect ();
	OS.GetPortBounds (port, portRect);
	OS.QDBeginCGContext (port, context);
	OS.CGContextScaleCTM (context [0], 1, -1);
	OS.CGContextTranslateCTM (context [0], 0, portRect.top - portRect.bottom);
	CGRect cgRect = new CGRect ();
	cgRect.width = 1;
	cgRect.height = 1;
	if (!origin) {
		OS.CGContextClearRect (context [0], cgRect);
	}
	if (!limit) {
		cgRect.x = rgnRect.right - 1;
		cgRect.y = rgnRect.bottom - 1;
		OS.CGContextClearRect (context [0], cgRect);
	}
	OS.CGContextSynchronize (context [0]);
	OS.QDEndCGContext (port, context);
}

Cursor findCursor () {
	return cursor;
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
	OS.SelectWindow (shellHandle);
	OS.SetFrontProcessWithOptions (new int [] {0, OS.kCurrentProcess}, OS.kSetFrontProcessFrontWindowOnly);
}

public Rectangle getClientArea () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowContentRgn, rect);
	return new Rectangle (0, 0, rect.right - rect.left, rect.bottom - rect.top);
}

public Rectangle getBounds () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

int getDrawCount (int control) {
	if (!isTrimHandle (control)) return drawCount;
	return 0;
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
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Point (rect.left, rect.top);
}

public boolean getMaximized () {
	checkWidget();
	//NOT DONE
	return super.getMaximized ();
}

public boolean getMinimized () {
	checkWidget();
	if (!getVisible ()) return super.getMinimized ();
	return OS.IsWindowCollapsed (shellHandle);
}

float [] getParentBackground () {
	return null;
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
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Point (rect.right - rect.left, rect.bottom - rect.top);
}

public boolean getVisible () {
	checkWidget();
    return OS.IsWindowVisible (shellHandle);
}

boolean hasBorder () {
	return false;
}

void hookEvents () {
	super.hookEvents ();
	int mouseProc = display.mouseProc;
	int windowProc = display.windowProc;
	int[] mask1 = new int [] {
		OS.kEventClassWindow, OS.kEventWindowActivated,
		OS.kEventClassWindow, OS.kEventWindowBoundsChanged,
		OS.kEventClassWindow, OS.kEventWindowClose,
		OS.kEventClassWindow, OS.kEventWindowCollapsed,
		OS.kEventClassWindow, OS.kEventWindowDeactivated,
		OS.kEventClassWindow, OS.kEventWindowExpanded,
		OS.kEventClassWindow, OS.kEventWindowHidden,
		OS.kEventClassWindow, OS.kEventWindowShown,
		OS.kEventClassWindow, OS.kEventWindowHitTest,
		OS.kEventClassWindow, OS.kEventWindowGetRegion,
	};
	int windowTarget = OS.GetWindowEventTarget (shellHandle);
	OS.InstallEventHandler (windowTarget, windowProc, mask1.length / 2, mask1, shellHandle, null);
	int [] mask2 = new int [] {
		OS.kEventClassMouse, OS.kEventMouseDown,
		OS.kEventClassMouse, OS.kEventMouseDragged,
//		OS.kEventClassMouse, OS.kEventMouseEntered,
//		OS.kEventClassMouse, OS.kEventMouseExited,
		OS.kEventClassMouse, OS.kEventMouseMoved,
		OS.kEventClassMouse, OS.kEventMouseUp,
		OS.kEventClassMouse, OS.kEventMouseWheelMoved,
	};
	OS.InstallEventHandler (windowTarget, mouseProc, mask2.length / 2, mask2, shellHandle, null);
}

void invalidateVisibleRegion (int control) {
	resetVisibleRegion (control);
	invalidateChildrenVisibleRegion (control);
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}

public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

int kEventWindowActivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowActivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Bug in the Macintosh.  Despite the that a window has scope
	* kWindowActivationScopeNone, it gets kEventWindowActivated
	* events but does not get kEventWindowDeactivated events.  The
	* fix is to ignore kEventWindowActivated events.
	*/
	int [] outScope = new int [1];
	OS.GetWindowActivationScope (shellHandle, outScope); 
	if (outScope [0] == OS.kWindowActivationScopeNone) return result;
	display.setMenuBar (menuBar);
	sendEvent (SWT.Activate);
	if (isDisposed ()) return result;
	restoreFocus ();
	return result;
}

int kEventWindowBoundsChanged (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowBoundsChanged (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] attributes = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAttributes, OS.typeUInt32, null, attributes.length * 4, null, attributes);
	if ((attributes [0] & OS.kWindowBoundsChangeOriginChanged) != 0) {
		sendEvent (SWT.Move);
	}
	if ((attributes [0] & OS.kWindowBoundsChangeSizeChanged) != 0) {
		resized = true;
		layoutControl (false);
		sendEvent (SWT.Resize);
		if (layout != null) layout.layout (this, false);
		if (region != null) {
			OS.GetEventParameter (theEvent, OS.kEventParamCurrentBounds, OS.typeQDRectangle, null, Rect.sizeof, null, rgnRect);
			OS.SetRect (rgnRect, (short) 0, (short) 0, (short) (rgnRect.right - rgnRect.left), (short) (rgnRect.bottom - rgnRect.top));
			OS.ReshapeCustomWindow (shellHandle);
		}
	}
	return result;
}

int kEventWindowClose (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowClose (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	closeWidget ();
	return OS.noErr;
}

int kEventWindowCollapsed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowCollapsed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	minimized = true;
	sendEvent (SWT.Iconify);
	return result;
}

int kEventWindowDeactivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowDeactivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	//TEMPORARY CODE - should be send, but causes a GP
	postEvent (SWT.Deactivate);
	if (isDisposed ()) return result;
	saveFocus ();
	if (savedFocus != null) {
		/*
		* Bug in the Macintosh.  When ClearKeyboardFocus() is called,
		* the control that has focus gets two kEventControlSetFocus
		* events indicating that focus was lost.  The fix is to ignore
		* both of these and send the focus lost event explicitly.
		*/
		display.ignoreFocus = true;
		OS.ClearKeyboardFocus (shellHandle);
		display.ignoreFocus = false;
		//TEMPORARY CODE - should be send, but causes a GP
		if (!savedFocus.isDisposed ()) savedFocus.sendFocusEvent (false, true);
	}
	display.setMenuBar (null);
	return result;
}

int kEventWindowExpanded (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowExpanded (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	minimized = false;
	sendEvent (SWT.Deiconify);
	return result;
}

int kEventWindowGetRegion (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowGetRegion (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (region == null) return OS.eventNotHandledErr;
	short [] regionCode = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamWindowRegionCode , OS.typeWindowRegionCode , null, 2, null, regionCode);
	int [] temp = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamRgnHandle , OS.typeQDRgnHandle , null, 4, null, temp);
	int hRegion = temp [0];
	switch (regionCode [0]) {
		case OS.kWindowContentRgn:
		case OS.kWindowStructureRgn:
			OS.RectRgn (hRegion, rgnRect);
			OS.SectRgn (hRegion, region.handle, hRegion);
			/*
			* Bug in the Macintosh. In kEventWindowGetRegion, 
			* Carbon assumes the origin of the Region is (0, 0)
			* and ignores the actual origin.  This causes the 
			* window to be shifted for a non zero origin.  Also,
			* the size of the window is the size of the region
			* which may be less then the size specified in
			* setSize or setBounds.
			* The fix is to include (0, 0) and the bottom 
			* right corner of the size in the region and to
			* make these points transparent.
			*/
			if (!region.contains (0, 0)) {
				Rect r = new Rect ();
				OS.SetRect (r, (short)0, (short)0, (short)1, (short)1);
				int rectRgn = OS.NewRgn ();
				OS.RectRgn (rectRgn, r);
				OS.UnionRgn (rectRgn, hRegion, hRegion);
				OS.DisposeRgn (rectRgn);
			}
			if (!region.contains (rgnRect.right - 1, rgnRect.bottom - 1)) {
				Rect r = new Rect ();
				OS.SetRect (r, (short) (rgnRect.right - 1), (short) (rgnRect.bottom - 1), rgnRect.right, rgnRect.bottom);
				int rectRgn = OS.NewRgn ();
				OS.RectRgn (rectRgn, r);
				OS.UnionRgn (rectRgn, hRegion, hRegion);
				OS.DisposeRgn (rectRgn);
			}
			return OS.noErr;
		default:
			OS.DiffRgn (hRegion, hRegion, hRegion);
			return OS.noErr;
	}
}

int kEventWindowHidden (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowHidden (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.setWindowVisible (false);
	}
	return OS.eventNotHandledErr;
}

int kEventWindowHitTest (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowHitTest (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (region == null) return OS.eventNotHandledErr;
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
	OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, pt);
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowContentRgn, rect);
	OS.SetPt (pt, (short) (pt.h - rect.left), (short) (pt.v - rect.top));
	int rgn = OS.NewRgn ();
	OS.RectRgn (rgn, rgnRect);
	OS.SectRgn (rgn, region.handle, rgn);
	short inData = OS.PtInRgn (pt, rgn) ? OS.wInContent: OS.wNoHit;
	OS.DisposeRgn (rgn);
	OS.SetEventParameter (theEvent, OS.kEventParamWindowDefPart, OS.typeWindowDefPartCode, 2, new short [] {inData});
	return OS.noErr;
}

int kEventWindowShown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowShown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && shell.getVisible ()) {
			shell.setWindowVisible (true);
		}
	}
	return OS.eventNotHandledErr;
}

void layoutControl (boolean events) {
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short)  OS.kWindowContentRgn, rect);
	int control = scrolledHandle != 0 ? scrolledHandle : handle;
	setBounds (control, 0, 0, rect.right - rect.left, rect.bottom - rect.top, false, true, false);
	super.layoutControl (events);
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
	OS.SelectWindow (shellHandle);
	setVisible (true);
	if (!restoreFocus ()) traverseGroup (true);
}

void register () {
	super.register ();
	int [] theRoot = new int [1];
	OS.GetRootControl (shellHandle, theRoot);
	display.addWidget (theRoot [0], this);
}

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

void releaseShells () {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.dispose ();
	}
}

void releaseWidget () {
	releaseShells ();
	super.releaseWidget ();
	if (windowGroup != 0) OS.ReleaseWindowGroup (windowGroup);
	display.updateQuitMenu ();
	windowGroup = 0;
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
	OS.SelectWindow (shellHandle);
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

public void setBounds (int x, int y, int width, int height) {
	checkWidget ();
	width = Math.max (0, width);
	height = Math.max (0, height);
	Rect rect = new Rect ();
	OS.SetRect (rect, (short) x, (short) y, (short) (x + width), (short) (y + height));
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
}

public void setMenuBar (Menu menu) {
	checkWidget();
	super.setMenuBar (menu);
	if (display.getActiveShell () == this) {
		display.setMenuBar (menuBar);
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

public void setLocation (int x, int y) {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	OS.SetRect (rect, (short) x, (short) y, (short) (x + width), (short) (y + height));
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
}

public void setMaximized (boolean maximized) {
	checkWidget();
	super.setMaximized (maximized);
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	short inPartCode = (short) (maximized ? OS.inZoomOut : OS.inZoomIn);
	//FIXME - returns -50 errParam
	OS.ZoomWindowIdeal (shellHandle, inPartCode, pt);
}

public void setMinimized (boolean minimized) {
	checkWidget();
	if (this.minimized == minimized) return;
	super.setMinimized (minimized);
	if (!minimized && OS.IsWindowCollapsed (shellHandle)) {
		 OS.SelectWindow (shellHandle);
	}
	OS.CollapseWindow (shellHandle, minimized);
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
	if (region == null) {
		rgnRect = null;
	} else {
		if (rgnRect == null) {
			rgnRect = new Rect ();
			OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rgnRect);
			OS.SetRect (rgnRect, (short) 0, (short) 0, (short) (rgnRect.right - rgnRect.left), (short) (rgnRect.bottom - rgnRect.top));	
		}
	}
	this.region = region;
	OS.ReshapeCustomWindow (shellHandle);
}

public void setSize (int width, int height) {
	checkWidget();
	width = Math.max (0, width);
	height = Math.max (0, height);
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	OS.SetRect (rect, rect.left, rect.top, (short)(rect.left + width), (short)(rect.top + height));
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetWindowTitleWithCFString (shellHandle, ptr);
	OS.CFRelease (ptr);
}

public void setVisible (boolean visible) {
	checkWidget();
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	setWindowVisible (visible);
}

void setWindowVisible (boolean visible) {
	if (OS.IsWindowVisible (shellHandle) == visible) return;
	if (visible) {
		if (!resized) {
			sendEvent (SWT.Resize);
			if (layout != null) layout.layout (this, false);
		}
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		int inModalKind = OS.kWindowModalityNone;
		if ((style & SWT.PRIMARY_MODAL) != 0) inModalKind = OS.kWindowModalityWindowModal;
		if ((style & SWT.APPLICATION_MODAL) != 0) inModalKind = OS.kWindowModalityAppModal;
		if ((style & SWT.SYSTEM_MODAL) != 0) inModalKind = OS.kWindowModalitySystemModal;
		if (inModalKind != OS.kWindowModalityNone) {
			int inUnavailableWindow = 0;
			if (parent != null) inUnavailableWindow = OS.GetControlOwner (parent.handle);
			OS.SetWindowModality (shellHandle, inModalKind, inUnavailableWindow);
		}
		int topHandle = topHandle ();
		OS.SetControlVisibility (topHandle, true, false);
		invalidateVisibleRegion (topHandle);
		int [] scope = new int [1];
		if ((style & SWT.ON_TOP) != 0) {
			OS.GetWindowActivationScope (shellHandle, scope);
	    	OS.SetWindowActivationScope (shellHandle, OS.kWindowActivationScopeNone);
		}
		int shellHandle = this.shellHandle;
		OS.RetainWindow (shellHandle);
		OS.ShowWindow (shellHandle);
		OS.ReleaseWindow (shellHandle);
		if (!isDisposed()) {
			if (minimized != OS.IsWindowCollapsed (shellHandle)) {
				OS.CollapseWindow (shellHandle, minimized);
			}
			if ((style & SWT.ON_TOP) != 0) {
				OS.SetWindowActivationScope (shellHandle, scope [0]);
			}
		}
	} else {
    	OS.HideWindow (shellHandle);
		int topHandle = topHandle ();
		OS.SetControlVisibility (topHandle, false, false);
		invalidateVisibleRegion (topHandle);
		sendEvent (SWT.Hide);
	}
	display.updateQuitMenu ();
}

void setZOrder () {
	if (scrolledHandle != 0) OS.HIViewAddSubview (scrolledHandle, handle);
}

void setZOrder (Control control, boolean above) {
	if (above) {
		//NOT DONE - move one window above another
	 	OS.BringToFront (shellHandle);
	 } else {
		int window = control == null ? 0 : OS.GetControlOwner (control.handle);
		OS.SendBehind (shellHandle, window);
	}
}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}

}

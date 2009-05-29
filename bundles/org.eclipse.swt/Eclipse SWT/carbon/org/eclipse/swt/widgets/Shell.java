/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.CGPoint;
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
	int shellHandle, windowGroup;
	boolean resized, moved, drawing, reshape, update, deferDispose, active, disposed, opened, fullScreen, center;
	boolean showWithParent, ignoreBounds;
	int invalRgn;
	Control lastActive;
	Rect rgnRect;
	Rectangle normalBounds;
	int imHandle;
	ToolTip [] toolTips;

	static int DEFAULT_CLIENT_WIDTH = -1;
	static int DEFAULT_CLIENT_HEIGHT = -1;

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
			state |= FOREIGN_HANDLE;
		}
	}
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
 * @since 3.3
 */
public static Shell internal_new (Display display, int handle) {
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

void bringToTop (boolean force) {
	if (getMinimized ()) return;
	if (force) {
		forceActive ();
	} else {
		setActive ();
	}
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
	int attributes = OS.kWindowStandardHandlerAttribute;
	attributes |= OS.kWindowCompositingAttribute;
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
			if (!OS.__BIG_ENDIAN__()) attributes |= OS.kWindowLiveResizeAttribute;
		} 
	} else {
		if ((style & SWT.TOOL) == 0) attributes |= OS.kWindowNoShadowAttribute;
	}
	int windowClass = OS.kDocumentWindowClass;
	if ((style & (SWT.CLOSE | SWT.TITLE)) == 0) windowClass = OS.kSheetWindowClass;
	if ((style & (SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		if ((style & (SWT.CLOSE | SWT.MAX | SWT.MIN)) == 0)  {
			windowClass = (style & SWT.TITLE) != 0 ? OS.kMovableModalWindowClass : OS.kModalWindowClass;
		}
	}
	if (shellHandle == 0) {
		Monitor monitor = getMonitor ();
		Rectangle rect = monitor.getClientArea ();
		int width = rect.width * 5 / 8;
		int height = rect.height * 5 / 8;
		Rect bounds = new Rect ();
		OS.SetRect (bounds, (short) 0, (short) 0, (short) width, (short) height);
		int [] outWindow = new int [1];
		attributes &= OS.GetAvailableWindowAttributes (windowClass);
		OS.CreateNewWindow (windowClass, attributes, bounds, outWindow);
		if (outWindow [0] == 0) error (SWT.ERROR_NO_HANDLES);
		shellHandle = outWindow [0];
		OS.RepositionWindow (shellHandle, 0, OS.kWindowCascadeOnMainScreen);
//		OS.SetThemeWindowBackground (shellHandle, (short) OS.kThemeBrushDialogBackgroundActive, false);
		int [] theRoot = new int [1];
		OS.HIViewFindByID (OS.HIViewGetRoot (shellHandle), OS.kHIViewWindowContentID (), theRoot);
		/*
		* Bug in the Macintosh.  When the window class is kMovableModalWindowClass or
		* kModalWindowClass, HIViewFindByID() fails to find the control identified by
		* kHIViewWindowContentID.  The fix is to call GetRootControl() if the call
		* failed.
		*/
		if (theRoot [0] == 0) OS.GetRootControl (shellHandle, theRoot);		
		if (theRoot [0] == 0) error (SWT.ERROR_NO_HANDLES);
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
			createScrolledHandle (theRoot [0]);
		} else {
			createHandle (theRoot [0]);
		}
		OS.SetControlVisibility (topHandle (), false, false);
	} else {
		int [] theRoot = new int [1];
		OS.HIViewFindByID (shellHandle, OS.kHIViewWindowContentID (), theRoot);
		if (theRoot [0] == 0) OS.GetRootControl (shellHandle, theRoot);
		handle = OS.HIViewGetFirstSubview (theRoot [0]);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		if (OS.IsWindowVisible (shellHandle)) state &= ~HIDDEN;
	}
	int [] outGroup = new int [1];
	OS.CreateWindowGroup (OS.kWindowGroupAttrHideOnCollapse, outGroup);
	if (outGroup [0] == 0) error (SWT.ERROR_NO_HANDLES);
	windowGroup = outGroup [0];
	int parentGroup;
	if ((style & SWT.ON_TOP) != 0) {
		parentGroup = OS.GetWindowGroupOfClass (OS.kFloatingWindowClass);		
	} else {
		if (parent != null) {
			parentGroup = parent.getShell ().windowGroup;
		} else {
			parentGroup = OS.GetWindowGroupOfClass (OS.kDocumentWindowClass);
		}
	}
	OS.SetWindowGroup (shellHandle, parentGroup);
	OS.SetWindowGroupParent (windowGroup, parentGroup);
	OS.SetWindowGroupOwner (windowGroup, shellHandle);
	CGPoint inMinLimits = new CGPoint (), inMaxLimits = new CGPoint ();
	OS.GetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
	if (DEFAULT_CLIENT_WIDTH == -1) DEFAULT_CLIENT_WIDTH = (int) inMinLimits.x;
	if (DEFAULT_CLIENT_HEIGHT == -1) DEFAULT_CLIENT_HEIGHT = 0;
	inMinLimits.y = (int) 0;
	int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
	if ((style & SWT.NO_TRIM) != 0 || (style & trim) == 0) {
		inMinLimits.x = (int) 0;	
	}
	OS.SetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
	int [] docID = new int [1];
	OS.NewTSMDocument ((short)1, new int [] {OS.kUnicodeDocument}, docID, 0);
	if (docID [0] == 0) error (SWT.ERROR_NO_HANDLES);
	imHandle = docID [0];
}

void createWidget () {
	super.createWidget ();
	resizeBounds ();
}

void deregister () {
	super.deregister ();
	int [] theRoot = new int [1];
	OS.GetRootControl (shellHandle, theRoot);
	display.removeWidget (theRoot [0]);
}

void destroyWidget () {
	int theWindow = shellHandle;
	/*
	* Bug in the Macintosh.  Under certain circumstances, yet to
	* be determined, calling HideWindow() and then DisposeWindow()
	* causes a segment fault when an application is exiting.  This
	* seems to happen to large applications.  The fix is to avoid
	* calling HideWindow() when a shell is about to be disposed.
	* 
	* Bug in the Macintosh.  Disposing a window from kEventWindowDeactivated
	* causes a segment fault. The fix is to defer disposing the window until
	* the event loop becomes idle.
	*/
	Display display = this.display;
	Composite parent = this.parent;
	while (!deferDispose && parent != null) {
		Shell shell = parent.getShell ();
		deferDispose = shell.deferDispose;
		parent = shell.parent;
	}
	if (deferDispose) OS.HideWindow (shellHandle);
	releaseHandle ();
	if (theWindow != 0) {
		if (deferDispose) {
			display.addDisposeWindow (theWindow);
		} else {
			OS.DisposeWindow (theWindow);
		}
	} 
}

void drawWidget (int control, int context, int damageRgn, int visibleRgn, int theEvent) {
	super.drawWidget (control, context, damageRgn, visibleRgn, theEvent);
	
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
	if (region == null || region.isDisposed ()) return;
	boolean origin = region.contains (0, 0);
	boolean limit = region.contains(rgnRect.right - 1, rgnRect.bottom - 1);
	if (origin && limit) return;
	
	CGRect cgRect = new CGRect ();
	cgRect.width = 1;
	cgRect.height = 1;
	if (!origin) {
		OS.CGContextClearRect (context, cgRect);
	}
	if (!limit) {
		cgRect.x = rgnRect.right - 1;
		cgRect.y = rgnRect.bottom - 1;
		OS.CGContextClearRect (context, cgRect);
	}
	OS.CGContextSynchronize (context);
}

Control findBackgroundControl () {
	return background != null || backgroundImage != null ? this : null;
}

Composite findDeferredControl () {
	return layoutCount > 0 ? this : null;
}

Cursor findCursor () {
	return cursor;
}

void fixShell (Shell newShell, Control control) {
	if (this == newShell) return;
	if (control == lastActive) setActiveControl (null);
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
	if (!isVisible ()) return;
	OS.SelectWindow (shellHandle);
	OS.SetUserFocusWindow (shellHandle);
	OS.SetFrontProcessWithOptions (new int [] {0, OS.kCurrentProcess}, OS.kSetFrontProcessFrontWindowOnly);
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
	float [] alpha = new float [1];
	if (OS.GetWindowAlpha (shellHandle, alpha) == OS.noErr) {
		return (int)(alpha [0] * 255);
	}
	return 0xFF;
}

public Rectangle getBounds () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
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
	return !fullScreen && super.getMaximized ();
}

public boolean getMinimized () {
	checkWidget();
	if (!getVisible ()) return super.getMinimized ();
	return OS.IsWindowCollapsed (shellHandle);
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
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowStructureWidths (shellHandle, rect);
	CGPoint inMinLimits = new CGPoint (), inMaxLimits = new CGPoint ();
	OS.GetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
	int width = Math.max (1, (int) inMinLimits.x + (rect.left + rect.right));
	int height = Math.max (1, (int) inMinLimits.y + (rect.top + rect.bottom));
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
	return OS.IsWindowModified (shellHandle);
}


float [] getParentBackground () {
	return null;
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
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Point (rect.right - rect.left, rect.bottom - rect.top);
}

float getThemeAlpha () {
	return 1;
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
		OS.kEventClassWindow, OS.kEventWindowCollapsing,
		OS.kEventClassWindow, OS.kEventWindowCollapsed,
		OS.kEventClassWindow, OS.kEventWindowDeactivated,
		OS.kEventClassWindow, OS.kEventWindowDrawContent,
		OS.kEventClassWindow, OS.kEventWindowExpanded,
		OS.kEventClassWindow, OS.kEventWindowGetRegion,
		OS.kEventClassWindow, OS.kEventWindowHidden,
		OS.kEventClassWindow, OS.kEventWindowHitTest,
		OS.kEventClassWindow, OS.kEventWindowShown,
		OS.kEventClassWindow, OS.kEventWindowUpdate,
		OS.kEventClassWindow, OS.kEventWindowGetClickModality,
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

void invalWindowRgn (int window, int rgn) {
	display.needsPaint = true;
	if (invalRgn == 0) invalRgn = OS.NewRgn();
	OS.UnionRgn (rgn, invalRgn, invalRgn);
}

boolean isDrawing () {
	return getDrawing ();
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}

boolean isEnabledCursor () {
	return true;
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
	if (!active) {
		active = true;
		deferDispose = true;
		Display display = this.display;
		display.activeShell = this;
		display.setMenuBar (menuBar);
		if (menuBar != null) OS.DrawMenuBar ();
		sendEvent (SWT.Activate);
		if (isDisposed ()) return result;
		if (!restoreFocus () && !traverseGroup (true)) setFocus ();
		if (isDisposed ()) return result;
		display.activeShell = null;
		Shell parentShell = this;
		while (parentShell.parent != null) {
			parentShell = (Shell) parentShell.parent;
			if (parentShell.fullScreen) {
				break;
			}
		}
		if (!parentShell.fullScreen || menuBar != null) {
			updateSystemUIMode ();
		} else {
			parentShell.updateSystemUIMode ();
		}
		deferDispose = false;
	}
	return result;
}

int kEventWindowBoundsChanged (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowBoundsChanged (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] attributes = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAttributes, OS.typeUInt32, null, attributes.length * 4, null, attributes);
	if ((attributes [0] & OS.kWindowBoundsChangeOriginChanged) != 0) {
		if (!ignoreBounds) {
			moved = true;
			sendEvent (SWT.Move);
			if (isDisposed ()) return OS.noErr;
		}
	}
	if ((attributes [0] & OS.kWindowBoundsChangeSizeChanged) != 0) {
		resizeBounds ();
		if (!ignoreBounds) {
			resized = true;
			sendEvent (SWT.Resize);
			if (isDisposed ()) return OS.noErr;
			if (layout != null) {
				markLayout (false, false);
				updateLayout (false);
			}
		}
		if (region != null && !region.isDisposed()) {
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
	if (isEnabled ()) closeWidget ();
	return OS.noErr;
}

int kEventWindowCollapsed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowCollapsed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	minimized = true;
	sendEvent (SWT.Iconify);
	return result;
}

int kEventWindowCollapsing (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowCollapsing (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	updateMinimized (true);
	return result;
}

int kEventWindowDeactivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowDeactivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (active) {
		active = false;
		deferDispose = true;
		Display display = this.display;
		display.activeShell = this;
		sendEvent (SWT.Deactivate);
		if (isDisposed ()) return result;
		setActiveControl (null);
		if (isDisposed ()) return result;
		display.activeShell = null;
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
			if (!savedFocus.isDisposed ()) savedFocus.sendFocusEvent (SWT.FocusOut, false);
		}
		display.setMenuBar (null);
		deferDispose = false;
	}
	return result;
}

int kEventWindowDrawContent (int nextHandler, int theEvent, int userData) {
	drawing = true;
	int result = OS.CallNextEventHandler (nextHandler, theEvent);
	drawing = false;	
	if (reshape) {
		reshape = false;
		OS.ReshapeCustomWindow (shellHandle);
	}
	return result;
}

int kEventWindowExpanded (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowExpanded (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	minimized = false;
	updateMinimized (false);
	sendEvent (SWT.Deiconify);
	return result;
}

int kEventWindowGetClickModality (int nextHandler, int theEvent, int userData) {
	/*
	* Bug in the Macintosh.  When the user clicks in an Expose window that is
	* disabled because of a modal dialog, the modal dialog does not come to
	* front and is obscured by the Expose window.  The fix is to select the
	* modal dialog.
	*/
	int result = OS.CallNextEventHandler (nextHandler, theEvent);
	int [] modal = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamModalWindow, OS.typeWindowRef, null, 4, null, modal);
	if (modal [0] != 0) OS.SelectWindow (modal [0]);
	
	/*
	* Feature in the Macintosh. ON_TOP shells are in the kFloatingWindowClass window
	* group and are not modal disabled by default. The fix is to detect that it should
	* be disabled and update the event parameters.
	* 
	* Bug in Macintosh.  When a kWindowModalityWindowModal window is active the 
	* default handler of kEventWindowGetClickModality does not properly set the 
	* kEventParamModalClickResult. The fix is to set it ourselves.
	*/
	Shell modalShell = getModalShell ();
	if (modalShell != null) {
		int [] modality = new int [1];
		OS.GetWindowModality (modalShell.shellHandle, modality, null);
		if ((style & SWT.ON_TOP) != 0 || modality [0] == OS.kWindowModalityWindowModal) {
			int clickResult = OS.kHIModalClickIsModal | OS.kHIModalClickAnnounce;
			OS.SetEventParameter (theEvent, OS.kEventParamWindowModality, OS.typeWindowModality, 4, modality);
			OS.SetEventParameter (theEvent, OS.kEventParamModalClickResult, OS.typeModalClickResult, 4, new int[]{clickResult});
			OS.SetEventParameter (theEvent, OS.kEventParamModalWindow, OS.typeWindowRef, 4, new int[]{modalShell.shellHandle});
		}
	}
	return result;
}

int kEventWindowGetRegion (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowGetRegion (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	if (region == null || region.isDisposed ()) return OS.eventNotHandledErr;
	short [] regionCode = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamWindowRegionCode, OS.typeWindowRegionCode , null, 2, null, regionCode);
	int [] temp = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle , null, 4, null, temp);
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
	if (region == null || region.isDisposed ()) return OS.eventNotHandledErr;
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
	invalidateVisibleRegion (topHandle ());
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && shell.getVisible ()) {
			shell.setWindowVisible (true);
		}
	}
	return OS.eventNotHandledErr;
}

int kEventWindowUpdate (int nextHandler, int theEvent, int userData) {
	update = true;
	int result = OS.CallNextEventHandler (nextHandler, theEvent);
	update = false;
	if (invalRgn != 0) {
		OS.InvalWindowRgn (shellHandle, invalRgn);
		OS.DisposeRgn (invalRgn);
		invalRgn = 0;
	}
	return result;
}

void resizeBounds () {
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short)  OS.kWindowContentRgn, rect);
	int control = scrolledHandle != 0 ? scrolledHandle : handle;
	setBounds (control, 0, 0, rect.right - rect.left, rect.bottom - rect.top, false, true, false);
	resizeClientArea ();
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
	if (active) {
		if (!restoreFocus () && !traverseGroup (true)) setFocus ();
	}
}

public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

void register () {
	super.register ();
	int [] theRoot = new int [1];
	OS.GetRootControl (shellHandle, theRoot);
	display.addWidget (theRoot [0], this);
}

void releaseChildren (boolean destroy) {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (shell != null && !shell.isDisposed ()) {
			shell.dispose ();
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

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

void releaseParent () {
	/* Do nothing */
}

void releaseWidget () {
	super.releaseWidget ();
	display.clearModal (this);
	disposed = true;
	if (windowGroup != 0) OS.ReleaseWindowGroup (windowGroup);
	display.updateQuitMenu ();
	if (invalRgn != 0) OS.DisposeRgn (invalRgn);
	if (imHandle != 0) OS.DeleteTSMDocument (imHandle);
	imHandle = invalRgn = windowGroup = 0;
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

void removeTooTip (ToolTip toolTip) {
	if (toolTips == null) return;
	for (int i=0; i<toolTips.length; i++) {
		if (toolTips [i] == toolTip) {
			toolTips [i] = null;
			return;
		}
	}
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
	if (!isVisible ()) return;
	OS.SelectWindow (shellHandle);
	OS.SetUserFocusWindow (shellHandle);
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
	alpha &= 0xFF;
	OS.SetWindowAlpha (shellHandle, alpha / 255f);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	if (fullScreen) setFullScreen (false);
	Rect rect = new Rect ();
	if (!move) {
		OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
		x = rect.left;
		y = rect.top;
	}
	if (!resize) {
		OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
		width = rect.right - rect.left;
		height = rect.bottom - rect.top;
	} else {
		OS.GetWindowStructureWidths (shellHandle, rect);
		CGPoint inMinLimits = new CGPoint (), inMaxLimits = new CGPoint ();
		OS.GetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
		width = Math.max (1, Math.max (width, (int) inMinLimits.x + (rect.left + rect.right)));
		height = Math.max (1, Math.max (height, (int) inMinLimits.y + (rect.top + rect.bottom)));
	}
	if (rgnRect != null) {
		OS.SetRect (rgnRect, (short) 0, (short) 0, (short) width, (short) height);	
	}
	OS.SetRect (rect, (short) x, (short) y, (short) (x + width), (short) (y + height));
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return 0;
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (((state & DISABLED) == 0) == enabled) return;
	super.setEnabled (enabled);
	if (enabled && OS.IsWindowActive (shellHandle)) {
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
	checkWidget ();
	this.fullScreen = fullScreen; 
	if (fullScreen) {
		normalBounds = getBounds ();
		OS.ChangeWindowAttributes (shellHandle, OS.kWindowNoTitleBarAttribute, OS.kWindowResizableAttribute | OS.kWindowLiveResizeAttribute);
		updateSystemUIMode ();
		Rectangle screen = getMonitor ().getBounds ();
		if (menuBar != null && getMonitor ().equals(display.getPrimaryMonitor ())) {
			Rect rect = new Rect ();
			int gdevice = OS.GetMainDevice ();
			OS.GetAvailableWindowPositioningBounds (gdevice, rect);
			screen.height -= rect.top;
			screen.y += rect.top;
		}
		Rect rect = new Rect ();
		OS.SetRect (rect, (short) screen.x, (short) screen.y, (short) (screen.x + screen.width), (short) (screen.y + screen.height));
		OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	} else {
		int attributes = 0;
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
			if (!OS.__BIG_ENDIAN__()) attributes |= OS.kWindowLiveResizeAttribute;
		}
		OS.ChangeWindowAttributes (shellHandle, attributes, OS.kWindowNoTitleBarAttribute);
		OS.SetSystemUIMode (OS.kUIModeNormal, 0);
		if (maximized) {
			setMaximized (true);
		} else {
			Rect rect = new Rect ();
			if (normalBounds != null) OS.SetRect (rect, (short) normalBounds.x, (short) normalBounds.y, (short) (normalBounds.x + normalBounds.width), (short) (normalBounds.y + normalBounds.height));
			OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
		}
		normalBounds = null;
	}
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

public void setMaximized (boolean maximized) {
	checkWidget();
	super.setMaximized (maximized);
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	if (maximized) {
		Rect rect = new Rect ();
		int gdevice = OS.GetMainDevice ();
		OS.GetAvailableWindowPositioningBounds (gdevice, rect);
		pt.h = (short) (rect.right - rect.left);
		pt.v = (short) (rect.bottom - rect.top);
	}
	short inPartCode = (short) (maximized ? OS.inZoomOut : OS.inZoomIn);
	if (!opened) ignoreBounds = true;
	OS.ZoomWindowIdeal (shellHandle, inPartCode, pt);
	if (!opened) ignoreBounds = false;
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
	checkWidget();
	int clientWidth = 0, clientHeight = 0;
	int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
		clientWidth = DEFAULT_CLIENT_WIDTH;
		clientHeight = DEFAULT_CLIENT_HEIGHT;
	}
	Rect rect = new Rect ();
	OS.GetWindowStructureWidths (shellHandle, rect);
	CGPoint inMinLimits = new CGPoint (), inMaxLimits = new CGPoint ();
	OS.GetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
	width = Math.max (width, clientWidth + rect.left + rect.right);
	height = Math.max (height, clientHeight + rect.top + rect.bottom);
	inMinLimits.x = width - (rect.left + rect.right);
	inMinLimits.y = height - (rect.top + rect.bottom);
	OS.SetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
	Point size = getSize ();
	int newWidth = Math.max (size.x, width), newHeight = Math.max (size.y, height);
	if (newWidth != size.x || newHeight != size.y) setSize (newWidth, newHeight);
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
	checkWidget();
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
	OS.SetWindowModified (shellHandle, modified);
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
	/*
	* Bug in the Macintosh.  Calling ReshapeCustomWindow() from a
	* kEventWindowDrawContent handler originating from ShowWindow()
	* will deadlock.  The fix is to detected this case and only call
	* ReshapeCustomWindow() after the default handler is done.
	*/
	if (drawing) {
		reshape = true;
	} else {
		OS.ReshapeCustomWindow (shellHandle);
		redrawWidget (handle, true);
	}
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
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & mask) != 0) {
		if (visible) {
			display.setModalShell (this);
		} else {
			display.clearModal (this);
		}
	} else {
		updateModal ();
	}
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	showWithParent = visible;
	setWindowVisible (visible);
}

void setWindowVisible (boolean visible) {
	if (OS.IsWindowVisible (shellHandle) == visible) return;
	if (visible) {
		if (center && !moved) {
			if (isDisposed ()) return;			
			center ();
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
			if (inUnavailableWindow != 0) OS.CollapseWindow (inUnavailableWindow, false);
		}
		int topHandle = topHandle ();
		OS.SetControlVisibility (topHandle, true, false);
		int [] scope = new int [1];
		if ((style & SWT.ON_TOP) != 0) {
			OS.GetWindowActivationScope (shellHandle, scope);
			OS.SetWindowActivationScope (shellHandle, OS.kWindowActivationScopeNone);
		}

		int shellHandle = this.shellHandle;
		OS.RetainWindow (shellHandle);
		OS.ShowWindow (shellHandle);
		OS.ReleaseWindow (shellHandle);
		if (isDisposed()) return;
		if (minimized != OS.IsWindowCollapsed (shellHandle)) {
			OS.CollapseWindow (shellHandle, minimized);
		}
		if ((style & SWT.ON_TOP) != 0) {
			OS.SetWindowActivationScope (shellHandle, scope [0]);
			OS.BringToFront (shellHandle);
		} else {
			/*
			* Bug in the Macintosh.  ShowWindow() does not activate the shell when an ON_TOP
			* shell is visible. The fix is to detect that the shell was not activated and
			* activate it.
			*/
			if (display.getActiveShell () != this) {
				Shell[] shells = display.getShells();
				for (int i = 0; i < shells.length; i++) {
					Shell shell = shells [i];
					if ((shell.style & SWT.ON_TOP) != 0 && shell.isVisible ()) {
						bringToTop(false);
						break;
					}
				}
			}
		}
		opened = true;
		if (!moved) {
			moved = true;
			sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
		if (!resized) {
			resized = true;
			sendEvent (SWT.Resize);
			if (isDisposed ()) return;
			if (layout != null) {
				markLayout (false, false);
				updateLayout (false);
			}
		}
	} else {
		/*
		* Bug in the Macintosh.  Under certain circumstances, yet to
		* be determined, calling HideWindow() and then DisposeWindow()
		* causes a segment fault when an application is exiting.  This
		* seems to happen to large applications.  The fix is to avoid
		* calling HideWindow() when a shell is about to be disposed.
		*/
		if (!disposed) OS.HideWindow (shellHandle);
		if (isDisposed ()) return;
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

void updateMinimized (boolean minimized) {
	/*
	* Need to handle ON_TOP child shells ourselves, since they
	* are not in the same group hierarchy of this shell.
	*/
	Shell [] shells = getShells ();
	for (int i = 0; i < shells.length; i++) {
		Shell shell = shells [i];
		if (this == shell.parent && (shell.style & SWT.ON_TOP) != 0) {
			if (minimized) {
				if (shells[i].isVisible ()) {
					shells[i].showWithParent = true;
					OS.HideWindow (shell.shellHandle);
				}
			} else {
				if (shells[i].showWithParent) {
					shells[i].showWithParent = false;
					OS.ShowWindow (shell.shellHandle);
				}
			}
		}
	}
}

void updateModal () {
	// do nothing
}

void updateSystemUIMode () {
	if (!getMonitor ().equals (display.getPrimaryMonitor ())) return;
	boolean isActive = false;
	Shell activeShell = display.getActiveShell ();
	Shell current = this;
	while (current != null) {
		if (current.equals (activeShell)) {
			isActive = true;
			break;
		}
		current = (Shell) current.parent;
	}
	if (!isActive) return;
	if (fullScreen) {
		int mode = OS.kUIModeAllHidden;
		if (menuBar != null) {
			mode = OS.kUIModeContentHidden;
		}
		OS.SetSystemUIMode (mode, 0);
	} else {
		OS.SetSystemUIMode (OS.kUIModeNormal, 0);
	}
}

}

package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
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
public /*final*/ class Shell extends Decorations {
	Display display;
	int shellHandle;
	/* AW
	boolean reparented, realized;
	*/
	boolean realized;

	int oldX, oldY, oldWidth, oldHeight;
	Control lastActive;
	// AW
	private boolean fIsEnabled= true; 
	// AW

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
	this (parent != null ? parent.getDisplay () : null, parent, style, 0);
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

public static Shell macosx_new (Display display, int handle) {
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
    /* AW
	if (OS.XtIsSubclass (shellHandle, OS.OverrideShellWidgetClass ())) {
		return;
	}
    */

	/* Query the trim insets */
    /* AW
	int shellWindow = OS.XtWindow (shellHandle);
	if (shellWindow == 0) return;
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
    */

	/* Find the direct child of the root window */
    /* AW
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
    */

	/*
	 * Translate the coordinates of the shell window to the
	 * coordinates of the direct child of the root window
	 */
    /* AW
	if (shellWindow == trimWindow) return;
    */

	/* Query the border width of the direct child of the root window */
    /*
	int [] trimBorder = new int [1];
	int [] trimWidth = new int [1];
	int [] trimHeight = new int [1];
	OS.XGetGeometry (xDisplay, trimWindow, unused, unused, unused, trimWidth, trimHeight, trimBorder, unused);
    */

	/* Query the border width of the direct child of the shell window */
    /*
	int [] shellBorder = new int [1];
	int [] shellWidth = new int [1];
	int [] shellHeight = new int [1];
	OS.XGetGeometry (xDisplay, shellWindow, unused, unused, unused, shellWidth, shellHeight, shellBorder, unused);
    */

	/* Calculate the trim */
    /*
	int width = (trimWidth [0] + (trimBorder [0] * 2)) - (shellWidth [0] + (shellBorder [0] * 2));
	int height = (trimHeight [0] + (trimBorder [0] * 2)) - (shellHeight [0] + (shellBorder [0] * 2));
    */

	/* Update the trim guesses to match the query */
    /*
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	if ((style & SWT.NO_TRIM) == 0) {
		hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
		hasResize = (style & SWT.RESIZE) != 0;
		hasBorder = (style & SWT.BORDER) != 0;
	}
	if (hasTitle) {
		if (hasResize)  {
			display.titleResizeTrimWidth = width;
			display.titleResizeTrimHeight = height;
			return;
		}
		if (hasBorder) {
			display.titleBorderTrimWidth = width;
			display.titleBorderTrimHeight = height;
			return;
		}
		display.titleTrimWidth = width;
		display.titleTrimHeight = height;
		return;
	}
	if (hasResize) {
		display.resizeTrimWidth = width;
		display.resizeTrimHeight = height;
		return;
	}
	if (hasBorder) {
		display.borderTrimWidth = width;
		display.borderTrimHeight = height;
		return;
	}
    */
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
	//System.out.println("Shell.closeWidget: nyi");
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
				shell.bringToTop ();
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
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	trim.x -= trimWidth / 2; trim.y -= trimHeight - (trimWidth / 2);
	trim.width += trimWidth; trim.height += trimHeight + imeHeight ();
	return trim;
}
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	
	int decorations = 0;
    /* AW
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.MIN) != 0) decorations |= NSWindow.MiniaturizableWindowMask;
		if ((style & SWT.MAX) != 0) decorations |= OS.MWM_DECOR_MAXIMIZE;
		if ((style & SWT.RESIZE) != 0) decorations |= NSWindow.ResizableWindowMask;
		if ((style & SWT.BORDER) != 0) decorations |= NSWindow.BorderlessWindowMask;
		if ((style & SWT.MENU) != 0) decorations |= OS.MWM_DECOR_MENU;
		if ((style & SWT.TITLE) != 0) decorations |= NSWindow.TitledWindowMask;
	}
    */
	if (style == SWT.NONE) {
	} else if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.CLOSE) != 0) decorations |= OS.kWindowCloseBoxAttribute;
		if ((style & SWT.MIN) != 0) decorations |= OS.kWindowCollapseBoxAttribute;
		if ((style & SWT.MAX) != 0) decorations |= OS.kWindowFullZoomAttribute;
		if ((style & SWT.RESIZE) != 0) ;
		decorations |= OS.kWindowResizableAttribute /* | OS.kWindowLiveResizeAttribute */;
		//if ((style & SWT.BORDER) == 0) decorations |= OS.kWindowNoShadowAttribute;
	//} else {
	//	decorations |= OS.kWindowNoShadowAttribute;
	}
	
    /* AW
	int inputMode = OS.MWM_INPUT_MODELESS;
	if ((style & SWT.PRIMARY_MODAL) != 0) inputMode = OS.MWM_INPUT_PRIMARY_APPLICATION_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) inputMode = OS.MWM_INPUT_FULL_APPLICATION_MODAL;
	if ((style & SWT.SYSTEM_MODAL) != 0) inputMode = OS.MWM_INPUT_SYSTEM_MODAL;
    */
	int inputMode = OS.kWindowModalityNone;
	if ((style & SWT.PRIMARY_MODAL) != 0) inputMode = OS.kWindowModalityWindowModal;
	if ((style & SWT.APPLICATION_MODAL) != 0) inputMode = OS.kWindowModalityAppModal;
	if ((style & SWT.SYSTEM_MODAL) != 0) inputMode = OS.kWindowModalitySystemModal;	

    /* AW
	byte [] buffer = {(byte)' ', 0, 0, 0};
	int ptr = OS.XtMalloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] argList1 = {
		OS.XmNmwmInputMode, inputMode,
		OS.XmNmwmDecorations, decorations,
		OS.XmNoverrideRedirect, (style & SWT.ON_TOP) != 0 ? 1 : 0,
		OS.XmNtitle, ptr,
	};
	byte [] appClass = display.appClass;
    */
	
	MacRect bounds= new MacRect(100, 100, 100, 100);
	int windowClass= 0;
	short themeBrush= OS.kThemeBrushDialogBackgroundActive;
	if (parent == null && (style & SWT.ON_TOP) == 0) {
        /* AW
		int xDisplay = display.xDisplay;
		int widgetClass = OS.TopLevelShellWidgetClass ();
		shellHandle = OS.XtAppCreateShell (display.appName, appClass, widgetClass, xDisplay, argList1, argList1.length / 2);
        */
		if ((style & SWT.NO_TRIM) != 0)
			windowClass= OS.kHelpWindowClass;
		else {
			windowClass= OS.kDocumentWindowClass;
			//themeBrush= OS.kThemeBrushDocumentWindowBackground;
		}
	} else {
        /* AW
		int widgetClass = OS.TransientShellWidgetClass ();
//		if ((style & SWT.ON_TOP) != 0) {
//			widgetClass = OS.OverrideShellWidgetClass ();
//		}
        */
        /* AW
		int parentHandle = display.shellHandle;
		if (parent != null) parentHandle = parent.handle;
		shellHandle = OS.XtCreatePopupShell (appClass, widgetClass, parentHandle, argList1, argList1.length / 2);
        */
		if (style == SWT.NONE) {
			//System.out.println("Shell.createHandle: SWT.NONE");
			windowClass= OS.kHelpWindowClass;
		} else if ((style & SWT.NO_TRIM) != 0 && (style & SWT.ON_TOP) != 0) {
			//System.out.println("Shell.createHandle: SWT.NO_TRIM | SWT.ON_TOP");
			windowClass= OS.kHelpWindowClass;
		} else if ((style & SWT.NO_TRIM) != 0) {
			//System.out.println("Shell.createHandle: SWT.NO_TRIM");
			windowClass= OS.kHelpWindowClass;
		} else if (inputMode == OS.kWindowModalityAppModal) {
			//System.out.println("Shell.createHandle: kMovableModalWindowClass");
			windowClass= OS.kMovableModalWindowClass;
		} else if (inputMode == OS.kWindowModalitySystemModal) {
			//System.out.println("Shell.createHandle: kModalWindowClass");
			windowClass= OS.kModalWindowClass;
		} else if ((style & SWT.ON_TOP) != 0) {
			//System.out.println("Shell.createHandle: SWT.ON_TOP");
			//windowClass= OS.kFloatingWindowClass;
			windowClass= OS.kHelpWindowClass;
			decorations= 0;
		} else {
			//System.out.println("Shell.createHandle: kDocumentWindowClass");
			windowClass= OS.kDocumentWindowClass;
		}
	}
	
	// check whether window class supports a given decoration 
	if ((decorations & OS.kWindowCloseBoxAttribute) != 0 &&
			windowClass != OS.kDocumentWindowClass &&
				windowClass != OS.kFloatingWindowClass &&
					windowClass != OS.kUtilityWindowClass) {
		decorations&= ~OS.kWindowCloseBoxAttribute;
	}
	if ((decorations & OS.kWindowFullZoomAttribute) != 0 &&
			windowClass != OS.kDocumentWindowClass &&
				windowClass != OS.kFloatingWindowClass &&
					windowClass != OS.kUtilityWindowClass) {
		decorations&= ~OS.kWindowFullZoomAttribute;
	}
	if ((decorations & OS.kWindowCollapseBoxAttribute) != 0 &&
			windowClass != OS.kDocumentWindowClass) {
		decorations&= ~OS.kWindowCollapseBoxAttribute;
	}
	if ((decorations & OS.kWindowCollapseBoxAttribute) != 0 &&
			windowClass != OS.kDocumentWindowClass &&
				windowClass != OS.kMovableModalWindowClass &&
					windowClass != OS.kFloatingWindowClass &&
						windowClass != OS.kUtilityWindowClass &&
							windowClass != OS.kSheetWindowClass) {
		decorations&= ~OS.kWindowCollapseBoxAttribute;
	}

	int[] wHandle= new int[1];
	if (OS.CreateNewWindow(windowClass, decorations | OS.kWindowStandardHandlerAttribute, bounds.getData(), wHandle) == OS.kNoErr)
		shellHandle= wHandle[0];
	else {
		System.out.println("Shell.createHandle: can't create window with these attributes; creating default window");
		if (OS.CreateNewWindow(OS.kDocumentWindowClass, OS.kWindowStandardHandlerAttribute, bounds.getData(), wHandle) == OS.kNoErr)
			shellHandle= wHandle[0];
	}

	if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
	
	if (themeBrush != 0)
		OS.SetThemeWindowBackground(shellHandle, themeBrush, false);
	
	// set modality
	if (inputMode != OS.kWindowModalityNone) {
		int parentShellHandle= 0;
		if (parent != null) parentShellHandle = parent.handle;
		//System.out.println("Shell.createHandle: OS.SetWindowModality: " + inputMode);
		OS.SetWindowModality(shellHandle, inputMode, parentShellHandle);
	}

	/* Create scrolled handle */
	createScrolledHandle (shellHandle);

	/*
	* Feature in Motif.  There is no way to get the single pixel
	* border surrounding a TopLevelShell or a TransientShell.
	* Also, attempts to set a border on either the shell handle
	* or the main window handle fail.  The fix is to set the border
	* on the client area.
	*/
    /* AW
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.RESIZE)) == 0) {
		int [] argList2 = {OS.XmNborderWidth, 1};
		OS.XtSetValues (handle, argList2, argList2.length / 2);
	}
    */
}
void deregister () {
	super.deregister ();
	WidgetTable.remove (shellHandle);
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
    /* AW
	if (OS.XtIsRealized (shellHandle)) {
		if (OS.XtIsTopLevelShell (shellHandle)) {
			OS.XtUnmapWidget (shellHandle);
		} else {
			OS.XtPopdown (shellHandle);
		}
	}
    */
	OS.HideWindow(shellHandle);
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
	*/
    /* AW
	Composite parent = this.parent;
	int [] argList = {OS.XmNoverrideRedirect, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
    */
	super.dispose ();
    /* AW
	if (parent != null && argList [1] != 0) {
		Shell shell = parent.getShell ();
		shell.bringToTop ();
	}
    */

	/*
	* This code intentionally commented.
	*/
//	if (oldDisplay != null) oldDisplay.update ();
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	enableHandle (enabled, shellHandle);
}
public int getBorderWidth () {
	checkWidget();
    /* AW
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	return argList [1];
    */
    return 0;
}
public Rectangle getBounds () {
	checkWidget();
    /* AW
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, root_x, root_y);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	int border = argList [5];
	*/
	
	MacRect contentBounds= new MacRect();
	OS.GetWindowBounds(shellHandle, OS.kWindowContentRgn, contentBounds.getData());
	
	MacRect bounds= new MacRect();
	OS.GetControlBounds(scrolledHandle, bounds.getData());
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	int border= 0;
	/* AW
	int width = argList [1] + trimWidth + (border * 2);
	int height = argList [3] + trimHeight + (border * 2);
	return new Rectangle (root_x [0], root_y [0], width, height);
    */
	int width = bounds.getWidth() + trimWidth + (border * 2);
	int height = bounds.getHeight() + trimHeight + (border * 2);
	Rectangle r= new Rectangle (contentBounds.getX(), contentBounds.getY(), width, height);
	return r;
	
	/*
	OS.GetWindowBounds(shellHandle, OS.kWindowContentRgn, bounds.getData());
	Rectangle r= bounds.toRectangle();
	System.out.println("Shell.getBounds: fixme " + r); // should be the contentView
	return r;
	*/
}
public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}
// AW
public boolean getEnabled () {
	checkWidget();
	return fIsEnabled;
}
// AW
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
    /* AW
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, root_x, root_y);
	return new Point (root_x [0], root_y [0]);
    */
	// returns the location of the contentArea
	MacRect bounds= new MacRect();
	OS.GetWindowBounds(shellHandle, OS.kWindowContentRgn, bounds.getData());
	return bounds.getLocation();
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
	//System.out.println("Shell.getSize: " + shellHandle);
    /* AW
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	int border = argList [5];
	*/
	MacRect bounds= new MacRect();
	OS.GetControlBounds(scrolledHandle, bounds.getData());
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	int border = 0;
	/* AW
	int width = argList [1] + trimWidth + (border * 2);
	int height = argList [3] + trimHeight + (border * 2);
	return new Point (width, height);
    */
	int width = bounds.getWidth() + trimWidth + (border * 2);
	int height = bounds.getHeight() + trimHeight + (border * 2);
	Point p= new Point (width, height);
	//System.out.println("   Shell.getSize: " + p);
	return p;
}
public boolean getVisible () {
	checkWidget();
    /* AW
	if (!OS.XtIsRealized (handle)) return false;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return false;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return false;
	XWindowAttributes attributes = new XWindowAttributes ();
	OS.XGetWindowAttributes (xDisplay, xWindow, attributes);
	if (attributes.map_state == OS.IsViewable) return true;
	int [] argList = {OS.XmNmappedWhenManaged, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	return minimized && attributes.map_state == OS.IsUnviewable && argList [1] != 0;
    */
    return OS.IsWindowVisible(shellHandle);
}
void hookEvents () {
	super.hookEvents ();
    /* AW
	int windowProc = display.windowProc;
	OS.XtAddEventHandler (shellHandle, OS.StructureNotifyMask, false, windowProc, SWT.Resize);
	if (OS.XtIsSubclass (shellHandle, OS.OverrideShellWidgetClass ())) return;
	OS.XtAddEventHandler (shellHandle, OS.FocusChangeMask, false, windowProc, SWT.FocusIn);
	int [] argList = {OS.XmNdeleteResponse, OS.XmDO_NOTHING};
	OS.XtSetValues (shellHandle, argList, argList.length / 2);
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay != 0) {
		int atom = OS.XmInternAtom (xDisplay, WM_DELETE_WINDOW, false);
		OS.XmAddWMProtocolCallback (shellHandle, atom, windowProc, SWT.Dispose);
	}
    */
	Display display= getDisplay();	
	int ref= OS.GetWindowEventTarget(shellHandle);

	int[] mask= new int[] {
		OS.kEventClassMouse, OS.kEventMouseDown,
		OS.kEventClassMouse, OS.kEventMouseMoved,
		OS.kEventClassMouse, OS.kEventMouseDragged,
		OS.kEventClassMouse, OS.kEventMouseUp
	};
	OS.InstallEventHandler(ref, display.fApplicationProc, mask, shellHandle);

	int[] mask2= new int[] {
		OS.kEventClassWindow, OS.kEventWindowActivated,
		OS.kEventClassWindow, OS.kEventWindowDeactivated,
		OS.kEventClassWindow, OS.kEventWindowBoundsChanged,
		OS.kEventClassWindow, OS.kEventWindowClose,
		OS.kEventClassWindow, OS.kEventWindowDrawContent,
	};
	OS.InstallEventHandler(ref, display.fWindowProc, mask2, shellHandle);
}
int imeHeight () {
    /* AW
	if (!IsDBLocale) return 0;
//	realizeWidget ();
	int [] argList1 = {OS.XmNheight, 0};
	OS.XtGetValues (shellHandle, argList1, argList1.length / 2);
	int [] argList2 = {OS.XmNheight, 0};
	OS.XtGetValues (scrolledHandle, argList2, argList2.length / 2);
	return argList1 [1] - argList2 [];
    */
    return 0;
}
public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}
boolean isModal () {
	checkWidget();
    /* AW
	int [] argList = {OS.XmNmwmInputMode, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	return (argList [1] != -1 && argList [1] != OS.MWM_INPUT_MODELESS);
    */
	int[] modalityKind= new int[1];
	if (OS.GetWindowModality(shellHandle, modalityKind, null) == OS.kNoErr)
		return modalityKind[0] != OS.kWindowModalityNone;
	return false;
}
public boolean isVisible () {
	checkWidget();
	return getVisible ();
}
void manageChildren () {
    /* AW
	OS.XtSetMappedWhenManaged (shellHandle, false);
    */
	super.manageChildren ();
	int width = MacUtil.getDisplayWidth() * 5 / 8;
	int height = MacUtil.getDisplayHeight() * 5 / 8;
	OS.SizeWindow(shellHandle, (short)width, (short)height, true);
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
}
int processDispose (Object callData) {
	closeWidget ();
	return 0;
}

int processResize (Object callData) {

	MacRect bounds= new MacRect();

	// outside bounds and location
	OS.GetWindowBounds(shellHandle, OS.kWindowStructureRgn, bounds.getData());
	int x= bounds.getX();
	int y= bounds.getY();

	// inside bounds and location
	OS.GetWindowBounds(shellHandle, OS.kWindowContentRgn, bounds.getData());
	int w= bounds.getWidth();
	int h= bounds.getHeight();
		
	boolean positionChanged= (oldX != x || oldY != y);
	boolean sizeChanged= (oldWidth != w || oldHeight != h);

	if (scrolledHandle != 0 && sizeChanged) {
		handleResize(scrolledHandle, 0, 0, w, h);
	}
	
	if (positionChanged) {
		//System.out.println("Shell.send: SWT.Move");
		sendEvent(SWT.Move);
	}
	
	if (sizeChanged) {
		//System.out.println("Shell.send: SWT.Resize");
		sendEvent (SWT.Resize);
		if (layout != null)
			layout (false);
	}
	
	if (!OS.IsWindowVisible(shellHandle))
		return 0;

	
	if (x != 0) oldX = x;
	if (y != 0) oldY = y;
	oldWidth = w;
	oldHeight = h;

    /* AW
	XConfigureEvent xEvent = new XConfigureEvent ();
	OS.memmove (xEvent, callData, XConfigureEvent.sizeof);
	switch (xEvent.type) {
		case OS.ReparentNotify: {
			if (reparented) return 0;
			reparented = true;
			short [] root_x = new short [1], root_y = new short [1];
			OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, root_x, root_y);
			int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
			OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
			xEvent.x = root_x [0];  xEvent.y = root_y [0];
			xEvent.width = argList [1];  xEvent.height = argList [3];
			// fall through
		}
		case OS.ConfigureNotify:
			if (!reparented) return 0;
			if (oldX != xEvent.x || oldY != xEvent.y) sendEvent (SWT.Move);
			if (oldWidth != xEvent.width || oldHeight != xEvent.height) {
				sendEvent (SWT.Resize);
				if (layout != null) layout (false);
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
    */
	return 0;
}

int processSetFocus (Object callData) {
    /* AW
	XFocusChangeEvent xEvent = new XFocusChangeEvent ();
	OS.memmove (xEvent, callData, XFocusChangeEvent.sizeof);
	int handle = OS.XtWindowToWidget (xEvent.display, xEvent.window);
	*/
	int handle = shellHandle; // AW FIXME fake
	if (handle != shellHandle) return super.processSetFocus (callData);
	
	/* AW
	if (xEvent.mode != OS.NotifyNormal) return 0;
	if (xEvent.detail != OS.NotifyNonlinear) return 0;
	switch (xEvent.type) {
		case OS.FocusIn:
			postEvent (SWT.Activate);
			break;
		case OS.FocusOut:
			postEvent (SWT.Deactivate);
			break;
	}
    */
		
	Menu menuBar= getMenuBar();
	Boolean b= (Boolean) callData;
	
	/*
	if (OS.IsWindowActive(shellHandle) != b.booleanValue()) {
		System.out.println("Shell.processSetFocus: " + b);
		return 0;
	}
	*/
	
	if (b.booleanValue()) {
		if (menuBar != null) {
			OS.SetRootMenu(menuBar.handle);
		}
		postEvent (SWT.Activate);
		/*
		if (!isDisposed())
			restoreFocus ();
		*/
	} else {
		//if (menuBar != null)
		//	OS.ReleaseMenu(menuBar.handle);
		
		/*
		setActiveControl (null);
		
		if (isDisposed()) return 0;
		*/
		postEvent (SWT.Deactivate);
		/*
		if (!isDisposed())
			saveFocus ();
		*/
	}
	return 0;
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	propagateHandle (enabled, shellHandle);
}
void realizeWidget () {
	if (realized) return;
    /* AW
	OS.XtRealizeWidget (shellHandle);
    */
	realizeChildren ();
	realized = true;
}
void register () {
	super.register ();
	WidgetTable.put (shellHandle, this);
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
			shell.releaseWidget ();
			shell.releaseHandle ();
		}
	}
}
void releaseWidget () {
	releaseShells ();
	super.releaseWidget ();
	display = null;
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
void saveBounds () {
/*
	MacRect bounds= new MacRect();
	
	// outside bounds and location
	OS.GetWindowBounds(shellHandle, OS.kWindowStructureRgn, bounds.getData());
	oldX = bounds.getX();
	oldY = bounds.getY();

	// inside bounds and location
	OS.GetWindowBounds(shellHandle, OS.kWindowContentRgn, bounds.getData());
	oldWidth = bounds.getWidth(); 
	oldHeight = bounds.getHeight();
*/
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
	checkWidget();
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	saveBounds ();
	int newWidth = Math.max (width - trimWidth (), 1);
	int newHeight = Math.max (height - trimHeight (), 1);

	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	
	OS.MoveWindow(shellHandle, (short)x, (short)y, false);
	OS.SizeWindow(shellHandle, (short)newWidth, (short)newHeight, true);
	//processResize (null);
	
	if (isFocus) caret.setFocus ();
}
// AW
public void setEnabled (boolean enabled) {
	fIsEnabled= enabled;
	super.setEnabled(enabled);
}
// AW
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
	saveBounds ();
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	OS.MoveWindow(shellHandle, (short)x, (short)y, true);
	//processResize(null);
	if (isFocus) caret.setFocus ();
}
public void setMinimized (boolean minimized) {
	checkWidget();

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
    /* AW
	int [] argList = {OS.XmNiconic, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	if ((argList [1] != 0) != this.minimized) {
		argList [1] = this.minimized ? 1 : 0;
		OS.XtSetValues (shellHandle, argList, argList.length / 2);
	}
    */

	/* Minimize or restore the shell */
    /* AW
	argList [1] = (this.minimized = minimized) ? 1 : 0;
	OS.XtSetValues (shellHandle, argList, argList.length / 2);
    */

	/* Force the XWindowAttributes to be up to date */
    /*
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay != 0) OS.XSync (xDisplay, false);
    */
	
	OS.CollapseWindow(shellHandle, true);
}

public void setSize (int width, int height) {
	checkWidget();
	saveBounds ();
	int newWidth = Math.max (width - trimWidth (), 1);
	int newHeight = Math.max (height - trimHeight (), 1);
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();	
	OS.SizeWindow(shellHandle, (short)newWidth, (short)newHeight, true);
	//processResize (null);
	if (isFocus) caret.setFocus ();
}
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	int sHandle= 0;
	try {
		sHandle= OS.CFStringCreateWithCharacters(string);
		OS.SetWindowTitleWithCFString(shellHandle, sHandle);
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
}
public void setVisible (boolean visible) {
	checkWidget();
	realizeWidget ();
	
	/* Show the shell */
	if (visible) {
		
		sendEvent (SWT.Show);
		if (isDisposed ()) return;

		/* Map the widget */
        /* AW
		OS.XtSetMappedWhenManaged (shellHandle, true);
		if (OS.XtIsTopLevelShell (shellHandle)) {
			OS.XtMapWidget (shellHandle);
		} else {
			OS.XtPopup (shellHandle, OS.XtGrabNone);
		}
        */
		OS.ShowWindow(shellHandle);
		//OS.SelectWindow(shellHandle);
		OS.BringToFront(shellHandle);

		processResize (null);
		
		// find focus
		//if (savedFocus == null)
		//	setFocus();

		/*
		* Force the shell to be fully exposed before returning.
		* This ensures that the shell coordinates are correct
		* when queried directly after showing the shell.
		*/
		do {
			display.update ();
		} while (!isVisible ());

		//adjustTrim ();

		/* Set the saved focus widget */
		/*
		if (savedFocus != null && !savedFocus.isDisposed ()) {
			savedFocus.setFocus ();
		}
		savedFocus = null;
		*/
		
		//sendEvent (SWT.Show);
		
		return;
	}
	
	Shell[] shells= getShells();
	for (int i= 0; i < shells.length; i++) {
		Shell shell= shells[i];
		if (shell.isVisible())
			shell.setVisible(false);
	}
	
	/* Hide the shell */
    /* AW
	OS.XtSetMappedWhenManaged (shellHandle, false);
	if (OS.XtIsTopLevelShell (shellHandle)) {
		OS.XtUnmapWidget (shellHandle);
	} else {
		OS.XtPopdown (shellHandle);
	}
    */
    OS.HideWindow(shellHandle);

	sendEvent (SWT.Hide);
}
int topHandle () {
	return shellHandle;
}
int trimHeight () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
    /* AW
	if (hasTitle) {
		if (hasResize) return display.titleResizeTrimHeight;
		if (hasBorder) return display.titleBorderTrimHeight;
		return display.titleTrimHeight;
	}
	if (hasResize) return display.resizeTrimHeight;
	if (hasBorder) return display.borderTrimHeight;
    */
	return 0;
}
int trimWidth () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
    /* AW
	if (hasTitle) {
		if (hasResize) return display.titleResizeTrimWidth;
		if (hasBorder) return display.titleBorderTrimWidth;
		return display.titleTrimWidth;
	}
	if (hasResize) return display.resizeTrimWidth;
	if (hasBorder) return display.borderTrimWidth;
    */
	return 0;
}
}

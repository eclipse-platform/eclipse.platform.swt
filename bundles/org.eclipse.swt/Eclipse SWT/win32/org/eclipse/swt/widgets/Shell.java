package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

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
	Display display;
	Menu activeMenu;
	int hIMC;
	int [] brushes;
	boolean showWithParent;
	int toolTipHandle, lpstrTip;
	Control lastActive;
	static final int DialogProc;
	static final TCHAR DialogClass = new TCHAR (0, OS.IsWinCE ? "Dialog" : "#32770", true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, DialogClass, lpWndClass);
		DialogProc = lpWndClass.lpfnWndProc;
	}

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
	this (display, OS.IsWinCE ? SWT.NONE : SWT.SHELL_TRIM);
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
	this (parent, OS.IsWinCE ? SWT.NONE : SWT.DIALOG_TRIM);
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
 * 
 * @private
 */
public static Shell win32_new (Display display, int handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle);
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

int callWindowProc (int msg, int wParam, int lParam) {
	if (parent != null) {
		if (handle == 0) return 0;
		switch (msg) {
			case OS.WM_KILLFOCUS:
			case OS.WM_SETFOCUS: 
				return OS.DefWindowProc (handle, msg, wParam, lParam);
		}
		return OS.CallWindowProc (DialogProc, handle, msg, wParam, lParam);
	}
	return super.callWindowProc (msg, wParam, lParam);
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
	OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
}

void createHandle () {
	boolean embedded = handle != 0;
	
	/*
	* On Windows 98 and NT, setting a window to be the
	* top most window using HWND_TOPMOST can result in a
	* parent dialog shell being moved behind its parent
	* if the dialog has a sibling that is currently on top
	* This only occurs using SetWindowPos (), not when the
	* handle is created.
	*/
	/*
	* The following code is intentionally commented.
	*/
//	if ((style & SWT.ON_TOP) != 0) display.lockActiveWindow = true;
	super.createHandle ();
	/*
	* The following code is intentionally commented.
	*/
//	if ((style & SWT.ON_TOP) != 0)  display.lockActiveWindow = false;
	
	if (!embedded) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);	
		bits &= ~(OS.WS_OVERLAPPED | OS.WS_CAPTION);
		if (!OS.IsWinCE) bits |= OS.WS_POPUP;
		if ((style & SWT.TITLE) != 0) bits |= OS.WS_CAPTION;
		if ((style & SWT.NO_TRIM) == 0) {
			if ((style & (SWT.BORDER | SWT.RESIZE)) == 0) bits |= OS.WS_BORDER;
		}
		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
		if (OS.IsWinCE) setMaximized (true);
	}
	if (OS.IsDBLocale) {
		hIMC = OS.ImmCreateContext ();
		if (hIMC != 0) OS.ImmAssociateContext (handle, hIMC);
	}
}

void createWidget () {
	super.createWidget ();
	if (!display.TrimEnabled) return;
	if (!isActive ()) setItemEnabled (OS.SC_CLOSE, false);
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

int findBrush (int pixel) {
	if (pixel == OS.GetSysColor (OS.COLOR_BTNFACE)) {
		return OS.GetSysColorBrush (OS.COLOR_BTNFACE);
	}
	if (pixel == OS.GetSysColor (OS.COLOR_WINDOW)) {
		return OS.GetSysColorBrush (OS.COLOR_WINDOW);
	}
	if (brushes == null) brushes = new int [4];
	LOGBRUSH logBrush = new LOGBRUSH ();
	for (int i=0; i<brushes.length; i++) {
		int hBrush = brushes [i];
		if (hBrush == 0) break;
		OS.GetObject (hBrush, LOGBRUSH.sizeof, logBrush);
		if (logBrush.lbColor == pixel) return hBrush;
	}
	int length = brushes.length;
	int hBrush = brushes [--length];
	if (hBrush != 0) OS.DeleteObject (hBrush);
	System.arraycopy (brushes, 0, brushes, 1, length);
	brushes [0] = hBrush = OS.CreateSolidBrush (pixel);
	return hBrush;
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
	OS.SetForegroundWindow (handle);
}

public Rectangle getBounds () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) return super.getBounds ();
	}
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

public boolean getEnabled () {
	checkWidget ();
	if (!display.TrimEnabled) return super.getEnabled ();
	return (state & DISABLED) == 0;
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
	if (!OS.IsDBLocale) return 0;
	int hIMC = OS.ImmGetContext (handle);
	int [] lpfdwConversion = new int [1], lpfdwSentence = new int [1];
	boolean open = OS.ImmGetOpenStatus (hIMC);
	if (open) open = OS.ImmGetConversionStatus (hIMC, lpfdwConversion, lpfdwSentence);
	OS.ImmReleaseContext (handle, hIMC);
	if (!open) return SWT.NONE;
	int result = 0;
	if ((lpfdwConversion [0] & OS.IME_CMODE_ROMAN) != 0) result |= SWT.ROMAN;
	if ((lpfdwConversion [0] & OS.IME_CMODE_FULLSHAPE) != 0) result |= SWT.DBCS;
	if ((lpfdwConversion [0] & OS.IME_CMODE_KATAKANA) != 0) return result | SWT.PHONETIC;
	if ((lpfdwConversion [0] & OS.IME_CMODE_NATIVE) != 0) return result | SWT.NATIVE;
	return result | SWT.ALPHA;
}

public Point getLocation () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) {
			return super.getLocation ();
		}
	}
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	return new Point (rect.left, rect.top);
}

public Shell getShell () {
	checkWidget ();
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

public boolean isEnabled () {
	checkWidget ();
	return getEnabled ();
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
	bringToTop ();
	/*
	* Feature on WinCE PPC.  A new application becomes
	* the foreground application only if it has at least
	* one visible window before the event loop is started.
	* The workaround is to explicitely force the shell to
	* be the foreground window.
	*/
	if (OS.IsWinCE) OS.SetForegroundWindow (handle);
	OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
	setVisible (true);
	if (!restoreFocus ()) traverseGroup (true);
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
	activeMenu = null;
	display.clearModal (this);
	display = null;
	if (lpstrTip != 0) {
		int hHeap = OS.GetProcessHeap ();
		OS.HeapFree (hHeap, 0, lpstrTip);
	}
	lpstrTip = 0;
	toolTipHandle = 0;
	if (brushes != null) {
		for (int i=0; i<brushes.length; i++) {
			int hBrush = brushes [i];
			if (hBrush != 0) OS.DeleteObject (hBrush);
		}
	}
	brushes = null;
	if (OS.IsDBLocale) {
		if (hIMC != 0) OS.ImmDestroyContext (hIMC);
	}
	lastActive = null;
}

void remove (Menu menu) {
	super.remove (menu);
	if (menu == activeMenu) activeMenu = null;
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

LRESULT selectPalette (int hPalette) {
	int hDC = OS.GetDC (handle);
	int hOld = OS.SelectPalette (hDC, hPalette, false);
	int result = OS.RealizePalette (hDC);
	if (result > 0) {
		OS.InvalidateRect (handle, null, true);
	} else {
		OS.SelectPalette (hDC, hOld, true);
		OS.RealizePalette (hDC);
	}
	OS.ReleaseDC (handle, hDC);
	return (result > 0) ? LRESULT.ONE : LRESULT.ZERO;
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
	bringToTop ();
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

void setBounds (int x, int y, int width, int height, int flags) {
	if (OS.IsWinCE) {
		swFlags = OS.SW_RESTORE;
		if ((style & SWT.NO_TRIM) == 0) {
			/* insert caption when no longer maximized */
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((bits & OS.WS_CAPTION) != OS.WS_CAPTION) {
				bits |= OS.WS_CAPTION;
				OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
			}
		}
	} else {
		if (OS.IsIconic (handle) || OS.IsZoomed (handle)) {
			super.setBounds (x, y, width, height, flags);
			return;
		}
	}
	flags |= OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
	OS.SetWindowPos (handle, 0, x, y, width, height, flags);
}

public void setEnabled (boolean enabled) {
	checkWidget ();
	if (!display.TrimEnabled) {
		super.setEnabled (enabled);
	} else {
		state &= ~DISABLED;
		if (!enabled) state |= DISABLED;
		if (isActive ()) setItemEnabled (OS.SC_CLOSE, enabled);
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
	checkWidget ();
	if (!OS.IsDBLocale) return;
	boolean imeOn = mode != SWT.NONE && mode != SWT.ROMAN;
	int hIMC = OS.ImmGetContext (handle);
	OS.ImmSetOpenStatus (hIMC, imeOn);
	if (imeOn) {
		int [] lpfdwConversion = new int [1], lpfdwSentence = new int [1];
		if (OS.ImmGetConversionStatus (hIMC, lpfdwConversion, lpfdwSentence)) {
			int newBits = 0;
			int oldBits = OS.IME_CMODE_NATIVE | OS.IME_CMODE_KATAKANA;
			if ((mode & SWT.PHONETIC) != 0) {
				newBits = OS.IME_CMODE_KATAKANA | OS.IME_CMODE_NATIVE;
				oldBits = 0;
			} else {
				if ((mode & SWT.NATIVE) != 0) {
					newBits = OS.IME_CMODE_NATIVE;
					oldBits = OS.IME_CMODE_KATAKANA;
				}
			}
			if ((mode & SWT.DBCS) != 0) {
				newBits |= OS.IME_CMODE_FULLSHAPE;
			} else {
				oldBits |= OS.IME_CMODE_FULLSHAPE;
			}
			if ((mode & SWT.ROMAN) != 0) {
				newBits |= OS.IME_CMODE_ROMAN;
			} else {
				oldBits |= OS.IME_CMODE_ROMAN;
			}
			lpfdwConversion [0] |= newBits;  lpfdwConversion [0] &= ~oldBits;
			OS.ImmSetConversionStatus (hIMC, lpfdwConversion [0], lpfdwSentence [0]);
		}
	}
	OS.ImmReleaseContext (handle, hIMC);
}

void setItemEnabled (int cmd, boolean enabled) {
	int hMenu = OS.GetSystemMenu (handle, false);
	if (hMenu == 0) return;
	int flags = OS.MF_ENABLED;
	if (!enabled) flags = OS.MF_DISABLED | OS.MF_GRAYED;
	OS.EnableMenuItem (hMenu, cmd, OS.MF_BYCOMMAND | flags);
}

void setParent () {
	/* Do nothing.  Not necessary for Shells */
}

void setToolTipText (int hwnd, String text) {
	if (OS.IsWinCE) return;
	if (toolTipHandle == 0) {

		/*
		* On Windows 98 and NT, setting a window to be the
		* top most window using HWND_TOPMOST can result in a
		* parent dialog shell being moved behind its parent
		* if the dialog has a sibling that is currently on top
		* This only occurs using SetWindowPos (), not when the
		* handle is created.
		*/
		/*
		* The following code is intentionally commented.
		*/
//		display.lockActiveWindow = true;
		toolTipHandle = OS.CreateWindowEx (
			OS.WS_EX_TOPMOST,
			new TCHAR (0, OS.TOOLTIPS_CLASS, true),
			null,
			OS.TTS_ALWAYSTIP,
			OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
			handle,
			0,
			OS.GetModuleHandle (null),
			null);
		/*
		* The following code is intentionally commented.
		*/
//		display.lockActiveWindow = false;
		if (toolTipHandle == 0) error (SWT.ERROR_NO_HANDLES);
		
		/*
		* Feature in Windows.  Despite the fact that the
		* tool tip text contains \r\n, the tooltip will
		* not honour the new line unless TTM_SETMAXTIPWIDTH
		* is set.  The fix is to set TTM_SETMAXTIPWIDTH to
		* a large value.
		*/
		OS.SendMessage (toolTipHandle, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);
	}
	TOOLINFO lpti = new TOOLINFO ();
	lpti.cbSize = TOOLINFO.sizeof;
	lpti.uId = hwnd;
	lpti.hwnd = handle;
	if (text == null) {
		OS.SendMessage (toolTipHandle, OS.TTM_DELTOOL, 0, lpti);
	} else {
		lpti.uFlags = OS.TTF_IDISHWND | OS.TTF_SUBCLASS;
		lpti.lpszText = OS.LPSTR_TEXTCALLBACK;
		OS.SendMessage (toolTipHandle, OS.TTM_ADDTOOL, 0, lpti);
	}
	OS.SendMessage (toolTipHandle, OS.TTM_UPDATE, 0, 0);
}

void setToolTipText (NMTTDISPINFO lpnmtdi, byte [] buffer) {
	/*
	* Ensure that the current position of the mouse
	* is inside the client area of the shell.  This
	* prevents tool tips from popping up over the
	* shell trimmings.
	*/
	if (!hasCursor ()) return;
	int hHeap = OS.GetProcessHeap ();
	if (lpstrTip != 0) OS.HeapFree (hHeap, 0, lpstrTip);
	int byteCount = buffer.length;
	lpstrTip = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpstrTip, buffer, byteCount);
	lpnmtdi.lpszText = lpstrTip;
}

void setToolTipText (NMTTDISPINFO lpnmtdi, char [] buffer) {
	/*
	* Ensure that the current position of the mouse
	* is inside the client area of the shell.  This
	* prevents tool tips from popping up over the
	* shell trimmings.
	*/
	if (!hasCursor ()) return;
	int hHeap = OS.GetProcessHeap ();
	if (lpstrTip != 0) OS.HeapFree (hHeap, 0, lpstrTip);
	int byteCount = buffer.length * 2;
	lpstrTip = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpstrTip, buffer, byteCount);
	lpnmtdi.lpszText = lpstrTip;
}

public void setVisible (boolean visible) {
	checkWidget ();
	super.setVisible (visible);
	if (showWithParent == visible) return;
	showWithParent = visible;
	if (!OS.IsWinCE) OS.ShowOwnedPopups (handle, visible);
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & mask) != 0) {
		if (visible) {
			display.setModal (this);
			Control control = display.getFocusControl ();
			if (control != null && !control.isActive ()) bringToTop ();
			int hwndShell = OS.GetActiveWindow ();
			if (hwndShell == 0) {
				if (parent != null) hwndShell = parent.handle;
			}
			if (hwndShell != 0) {
				OS.SendMessage (hwndShell, OS.WM_CANCELMODE, 0, 0);
			}
			OS.ReleaseCapture ();
		} else {
			display.clearModal (this);
		}
	}
}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}

void updateModal () {
	if (!display.TrimEnabled) return;
	setItemEnabled (OS.SC_CLOSE, isActive ());
}

int widgetExtStyle () {
	int bits = super.widgetExtStyle ();
	if ((style & SWT.ON_TOP) != 0) bits |= OS.WS_EX_TOPMOST;
	return bits;
}

TCHAR windowClass () {
	return parent != null ? DialogClass : super.windowClass ();
}

int windowProc () {
	return parent != null ? DialogProc : super.windowProc ();
}

int widgetStyle () {
	int bits = super.widgetStyle () & ~OS.WS_POPUP;
	if (handle != 0) return bits | OS.WS_CHILD;
	bits &= ~OS.WS_CHILD;
	/*
	* Feature in WinCE.  Calling CreateWindowEx () with WS_OVERLAPPED
	* and a parent window causes the new window to become a WS_CHILD of
	* the parent instead of a dialog child.  The fix is to use WS_POPUP
	* for a window with a parent.  
	* 
	* Feature in WinCE PPC.  A window without a parent with WS_POPUP
	* always shows on top of the Pocket PC 'Today Screen'. The fix
	* is to not set WS_POPUP for a window without a parent on WinCE
	* devices.
	* 
	* NOTE: WS_POPUP causes CreateWindowEx () to ignore CW_USEDEFAULT
	* and causes the default window location and size to be zero.
	*/
	if (OS.IsWinCE) return parent == null ? bits : bits | OS.WS_POPUP;
	
	/*
	* Use WS_OVERLAPPED for all windows, either dialog or top level
	* so that CreateWindowEx () will respect CW_USEDEFAULT and set
	* the default window location and size.
	* 
	* NOTE:  When a WS_OVERLAPPED window is created, Windows gives
	* the new window WS_CAPTION style bits.  These two constants are
	* as follows:
	* 
	* 	WS_OVERLAPPED = 0
	* 	WS_CAPTION = WS_BORDER | WS_DLGFRAME
	* 
	*/
	return bits | OS.WS_OVERLAPPED | OS.WS_CAPTION;
}

LRESULT WM_ACTIVATE (int wParam, int lParam) {
	if (OS.IsPPC) {
		/*
		* Note: this does not work when we get WM_ACTIVATE prior
		* to adding a listener.
		*/
		if (hooks (SWT.HardKeyDown) || hooks (SWT.HardKeyUp)) {
			int fActive = wParam & 0xFFFF;
			int hwnd = fActive != 0 ? handle : 0;
			for (int bVk=OS.VK_APP1; bVk<=OS.VK_APP6; bVk++) {
				OS.SHSetAppKeyWndAssoc ((byte) bVk, hwnd);
			}
		}
	}
	/*
	* Bug in Windows XP.  When a Shell is deactivated, the
	* IME composition window does not go away. This causes
	* repaint issues.  The fix is to close the IME ourselves
	* when the Shell is deactivated.
	* 
	* Note.  When the Shell is reactivated, the text in the
	* composition window has been lost.
	*/
	if ((OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) >= (5 << 16 | 1)) {
		if ((wParam & 0xFFFF) == 0 && OS.IsDBLocale && hIMC != 0) {
			OS.ImmSetOpenStatus (hIMC, false);
		}
	}
	
	LRESULT result = super.WM_ACTIVATE (wParam, lParam);
	if (parent != null) return LRESULT.ZERO;
	return result;
}

LRESULT WM_CLOSE (int wParam, int lParam) {
	if ((display.TrimEnabled && !isEnabled ()) || !isActive ()) {
		return LRESULT.ZERO;
	}
	return super.WM_CLOSE (wParam, lParam);
}

LRESULT WM_COMMAND (int wParam, int lParam) {
	if (OS.IsPPC) {
		/*
		* Note in WinCE PPC:  Close the Shell when the "Done Button" has
		* been pressed. lParam is either 0 (PocketPC 2002) or the handle
		* to the Shell (PocketPC).
		*/
		int loWord = wParam & 0xFFFF;
		if (loWord == OS.IDOK && (lParam == 0 || lParam == handle)) {
			OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
			return LRESULT.ZERO;			
		}
	}
	/* 
	* Note in WinCE PPC.  Menu events originate from the command bar.
	*/
	if (OS.IsPPC || OS.IsSP) {
		if (menuBar != null) {
			int hwndCB = menuBar.hwndCB;
			if (hwndCB != 0 && lParam == hwndCB) {
				return super.WM_COMMAND (wParam, 0);
			}
		}
	}
	return super.WM_COMMAND (wParam, lParam);
}

LRESULT WM_DESTROY (int wParam, int lParam) {
	LRESULT result = super.WM_DESTROY (wParam, lParam);
	/*
	* When the shell is a WS_CHILD window of a non-SWT
	* window, the destroy code does not get called because
	* the non-SWT window does not call dispose ().  Instead,
	* the destroy code is called here in WM_DESTROY.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.WS_CHILD) != 0) {
		releaseChild ();
		releaseResources ();
	}
	return result;
}

LRESULT WM_MOUSEACTIVATE (int wParam, int lParam) {
	LRESULT result = super.WM_MOUSEACTIVATE (wParam, lParam);
	if (result != null) return result;
	int hittest = lParam & 0xFFFF;
	if (hittest == OS.HTMENU) return null;
	/*
	* Get the current location of the cursor,
	* not the location of the cursor when the
	* WM_MOUSEACTIVATE was generated.  This is
	* strictly incorrect but is necessary in
	* order to support Activate and Deactivate
	* events for embedded widgets that have
	* their own event loop.  In that case, the
	* cursor location reported by GetMessagePos
	* is the one for our event loop, not the
	* embedded widget's event loop.
	*/
	POINT pt = new POINT ();
	if (!OS.GetCursorPos (pt)) {
		int pos = OS.GetMessagePos ();
		pt.x = (short) (pos & 0xFFFF);
		pt.y = (short) (pos >> 16);
	}
	int hwnd = OS.WindowFromPoint (pt);
	if (hwnd == 0) return null;
	Control control = display.findControl (hwnd);
	setActiveControl (control);
	/*
	* This code is intentionally commented.  On some platforms,
	* shells that are created with SWT.NO_TRIM won't take focus
	* when the user clicks in the client area or on the border.
	* This behavior is usedful when emulating tool tip shells
	* Until this behavior is specified, this code will remain
	* commented.
	*/
//	if ((style & SWT.NO_TRIM) != 0) {
//		if (hittest == OS.HTBORDER || hittest == OS.HTCLIENT) {
//			return new LRESULT (OS.MA_NOACTIVATE);
//		}
//	}
	return null;
}

LRESULT WM_NCHITTEST (int wParam, int lParam) {
	if (!isEnabled () || !isActive ()) {
		if (!display.TrimEnabled) return new LRESULT (OS.HTNOWHERE);
		int hittest = callWindowProc (OS.WM_NCHITTEST, wParam, lParam);
		if (hittest == OS.HTCLIENT || hittest == OS.HTMENU) hittest = OS.HTBORDER;
		return new LRESULT (hittest);
	}
	if (menuBar != null && !menuBar.getEnabled ()) {
		int hittest = callWindowProc (OS.WM_NCHITTEST, wParam, lParam);
		if (hittest == OS.HTMENU) hittest = OS.HTBORDER;
		return new LRESULT (hittest);
	}
	return null;
}

LRESULT WM_PALETTECHANGED (int wParam, int lParam) {
	if (wParam != handle) {
		int hPalette = display.hPalette;
		if (hPalette != 0) return selectPalette (hPalette);
	}
	return super.WM_PALETTECHANGED (wParam, lParam);
}

LRESULT WM_QUERYNEWPALETTE (int wParam, int lParam) {
	int hPalette = display.hPalette;
	if (hPalette != 0) return selectPalette (hPalette);
	return super.WM_QUERYNEWPALETTE (wParam, lParam);
}

LRESULT WM_SETCURSOR (int wParam, int lParam) {
	/*
	* Feature in Windows.  When the shell is disabled
	* by a Windows standard dialog (like a MessageBox
	* or FileDialog), clicking in the shell does not
	* bring the shell or the dialog to the front. The
	* fix is to detect this case and bring the shell
	* forward.
	*/
	int msg = lParam >> 16;
	if (msg == OS.WM_LBUTTONDOWN) {
		if (!display.TrimEnabled) {
			Shell modalShell = display.getModalShell ();
			if (modalShell != null && !isActive ()) {
				OS.SetActiveWindow (modalShell.handle);
			}
		}
		if (!OS.IsWindowEnabled (handle)) {
			if (!OS.IsWinCE) {
				int hwndPopup = OS.GetLastActivePopup (handle);
				if (hwndPopup != 0 && hwndPopup != handle) {
					if (WidgetTable.get (hwndPopup) == null) {
						OS.SetActiveWindow (hwndPopup);
					}
				}
			}
		}
	}
	return super.WM_SETCURSOR (wParam, lParam);
}

LRESULT WM_SHOWWINDOW (int wParam, int lParam) {
	LRESULT result = super.WM_SHOWWINDOW (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  If the shell is hidden while the parent
	* is iconic,  Windows shows the shell when the parent is
	* deiconified.  This does not happen if the shell is hidden
	* while the parent is not an icon.  The fix is to track
	* visible state for the shell and refuse to show the shell
	* when the parent is shown.
	*/
	if (lParam == OS.SW_PARENTOPENING) {
		Control control = this;
		while (control != null) {
			Shell shell = control.getShell ();
			if (!shell.showWithParent) return LRESULT.ZERO;
			control = control.parent;
		}
	}
	return result;
}

}

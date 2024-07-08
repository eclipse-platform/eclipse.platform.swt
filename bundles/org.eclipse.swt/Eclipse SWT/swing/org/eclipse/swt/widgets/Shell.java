/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.im.InputContext;
import java.awt.im.InputSubset;
import java.lang.Character.UnicodeBlock;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.internal.swing.CShell;
import org.eclipse.swt.internal.swing.Compatibility;
import org.eclipse.swt.internal.swing.UIThreadUtils;

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
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL</dd>
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
	Menu activeMenu;
//	int hIMC, hwndMDIClient, toolTipHandle, lpstrTip;
	int minWidth = SWT.DEFAULT, minHeight = SWT.DEFAULT;
//	int [] brushes;
//	boolean showWithParent;
	Control lastActive;
//	SHACTIVATEINFO psai;
	Region region;
//	static final int DialogProc;
//	static final TCHAR DialogClass = new TCHAR (0, OS.IsWinCE ? "Dialog" : "#32770", true);
//	static {
//		WNDCLASS lpWndClass = new WNDCLASS ();
//		OS.GetClassInfo (0, DialogClass, lpWndClass);
//		DialogProc = lpWndClass.lpfnWndProc;
//	}

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
	this (display, null, style, null);
}

Shell (Display display, Shell parent, int style, Container handle) {
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
 */
public Shell (Shell parent, int style) {
	this (parent != null ? parent.display : null, parent, style, null);
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
 */
public static Shell swing_new (Display display, Container handle) {
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

//int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
//	if ((style & SWT.TOOL) != 0) {
//		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.BORDER | SWT.RESIZE;
//		if ((style & trim) == 0) return OS.DefWindowProc (hwnd, msg, wParam, lParam);
//	}
//	if (parent != null) {
//		if (handle == 0) return 0;
//		switch (msg) {
//			case OS.WM_KILLFOCUS:
//			case OS.WM_SETFOCUS: 
//				return OS.DefWindowProc (hwnd, msg, wParam, lParam);
//		}
//		return OS.CallWindowProc (DialogProc, hwnd, msg, wParam, lParam);
//	}
//	if (handle == 0) return 0;
//	if (hwndMDIClient != 0) {
//		return OS.DefFrameProc (hwnd, hwndMDIClient, msg, wParam, lParam);
//	}
//	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
//}

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

protected Container createHandle () {
  CShell parentShell = parent != null? (CShell)parent.handle: null;
  return (Container)CShell.Factory.newInstance(this, parentShell, style);
//	boolean embedded = handle != 0;
//	
//	/*
//	* On Windows 98 and NT, setting a window to be the
//	* top most window using HWND_TOPMOST can result in a
//	* parent dialog shell being moved behind its parent
//	* if the dialog has a sibling that is currently on top
//	* This only occurs using SetWindowPos (), not when the
//	* handle is created.
//	*/
//	/*
//	* The following code is intentionally commented.
//	*/
////	if ((style & SWT.ON_TOP) != 0) display.lockActiveWindow = true;
//	super.createHandle ();
//	
//	/*
//	* The following code is intentionally commented.
//	*/
////	if ((style & SWT.ON_TOP) != 0)  display.lockActiveWindow = false;
//	
//	if (!embedded) {
//		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);	
//		bits &= ~(OS.WS_OVERLAPPED | OS.WS_CAPTION);
//		if (!OS.IsWinCE) bits |= OS.WS_POPUP;
//		if ((style & SWT.TITLE) != 0) bits |= OS.WS_CAPTION;
//		if ((style & SWT.NO_TRIM) == 0) {
//			if ((style & (SWT.BORDER | SWT.RESIZE)) == 0) bits |= OS.WS_BORDER;
//		}
//		/*
//		* Bug in Windows.  When the WS_CAPTION bits are cleared using
//		* SetWindowLong(), Windows does not resize the client area of
//		* the window to get rid of the caption until the first resize.
//		* The fix is to use SetWindowPos() with SWP_DRAWFRAME to force
//		* the frame to be redrawn and resized.
//		*/
//		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
//		int flags = OS.SWP_DRAWFRAME | OS.SWP_NOMOVE | OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE;
//		SetWindowPos (handle, 0, 0, 0, 0, 0, flags);
//		if (OS.IsWinCE) setMaximized (true);
//		if (OS.IsPPC) {
//			psai = new SHACTIVATEINFO ();
//			psai.cbSize = SHACTIVATEINFO.sizeof;
//		}
//	}
//	if (OS.IsDBLocale) {
//		hIMC = OS.ImmCreateContext ();
//		if (hIMC != 0) OS.ImmAssociateContext (handle, hIMC);
//	}
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
	Display oldDisplay = display;
	super.dispose ();
  if (oldDisplay != null) oldDisplay.wake();
	// widget is disposed at this point
//	if (oldDisplay != null) oldDisplay.update ();
}

//void enableWidget (boolean enabled) {
//	if (enabled) {
//		state &= ~DISABLED;
//	} else {
//		state |= DISABLED;
//	}
//	if (Display.TrimEnabled) {
//		if (isActive ()) setItemEnabled (OS.SC_CLOSE, enabled);
//	} else {
//		OS.EnableWindow (handle, enabled);
//	}
//}
//
//int findBrush (int pixel) {
//	if (pixel == OS.GetSysColor (OS.COLOR_BTNFACE)) {
//		return OS.GetSysColorBrush (OS.COLOR_BTNFACE);
//	}
//	if (pixel == OS.GetSysColor (OS.COLOR_WINDOW)) {
//		return OS.GetSysColorBrush (OS.COLOR_WINDOW);
//	}
//	if (brushes == null) brushes = new int [4];
//	LOGBRUSH logBrush = new LOGBRUSH ();
//	for (int i=0; i<brushes.length; i++) {
//		int hBrush = brushes [i];
//		if (hBrush == 0) break;
//		OS.GetObject (hBrush, LOGBRUSH.sizeof, logBrush);
//		if (logBrush.lbColor == pixel) return hBrush;
//	}
//	int length = brushes.length;
//	int hBrush = brushes [--length];
//	if (hBrush != 0) OS.DeleteObject (hBrush);
//	System.arraycopy (brushes, 0, brushes, 1, length);
//	brushes [0] = hBrush = OS.CreateSolidBrush (pixel);
//	return hBrush;
//}
//
//Cursor findCursor () {
//	return cursor;
//}
//
//Control findThemeControl () {
//	return null;
//}

void fixShell (Shell newShell, Control control) {
	if (this == newShell) return;
	if (control == lastActive) setActiveControl (null);
//	if (toolTipHandle != 0) {
//		setToolTipText (control.handle, null);
//	}
//	newShell.setToolTipText (control.handle, control.toolTipText);
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
 * @see Decorations#setDefaultButton
 * @see Shell#open
 * @see Shell#setActive
 */
public void forceActive () {
	checkWidget ();
	if(!isVisible()) return;
  ((CShell)handle).toFront();
}

//void forceResize () {
//	/* Do nothing */
//}
//
//public Rectangle getBounds () {
//	checkWidget ();
//	if (!OS.IsWinCE) {
//		if (OS.IsIconic (handle)) return super.getBounds ();
//	}
//	RECT rect = new RECT ();
//	OS.GetWindowRect (handle, rect);
//	int width = rect.right - rect.left;
//	int height = rect.bottom - rect.top;
//	return new Rectangle (rect.left, rect.top, width, height);
//}
//
//public boolean getEnabled () {
//	checkWidget ();
//	return (state & DISABLED) == 0;
//}

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
  // TODO: have a look at setImeInputMethod...
  return SWT.NONE;
//	if (!OS.IsDBLocale) return 0;
//	int hIMC = OS.ImmGetContext (handle);
//	int [] lpfdwConversion = new int [1], lpfdwSentence = new int [1];
//	boolean open = OS.ImmGetOpenStatus (hIMC);
//	if (open) open = OS.ImmGetConversionStatus (hIMC, lpfdwConversion, lpfdwSentence);
//	OS.ImmReleaseContext (handle, hIMC);
//	if (!open) return SWT.NONE;
//	int result = 0;
//	if ((lpfdwConversion [0] & OS.IME_CMODE_ROMAN) != 0) result |= SWT.ROMAN;
//	if ((lpfdwConversion [0] & OS.IME_CMODE_FULLSHAPE) != 0) result |= SWT.DBCS;
//	if ((lpfdwConversion [0] & OS.IME_CMODE_KATAKANA) != 0) return result | SWT.PHONETIC;
//	if ((lpfdwConversion [0] & OS.IME_CMODE_NATIVE) != 0) return result | SWT.NATIVE;
//	return result | SWT.ALPHA;
}

//public Point getLocation () {
//	checkWidget ();
//	if (!OS.IsWinCE) {
//		if (OS.IsIconic (handle)) {
//			return super.getLocation ();
//		}
//	}
//	RECT rect = new RECT ();
//	OS.GetWindowRect (handle, rect);
//	return new Point (rect.left, rect.top);
//}

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
  java.awt.Dimension size = handle.getMinimumSize();
  return new Point(size.width, size.height);
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
//	return new Point (width,  height);
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
	checkWidget ();
	return region;
}

public Shell getShell () {
	checkWidget ();
	return this;
}

//public Point getSize () {
//	checkWidget ();
//	if (!OS.IsWinCE) {
//		if (OS.IsIconic (handle)) return super.getSize ();
//	}
//	RECT rect = new RECT ();
//	OS.GetWindowRect (handle, rect);
//	int width = rect.right - rect.left;
//	int height = rect.bottom - rect.top;
//	return new Point (width, height);
//}

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

Composite findDeferredControl () {
  return layoutCount > 0 ? this : null;
}

protected boolean getTraversalKeyDefault(java.awt.event.KeyEvent ke) {
  return false;
}

public boolean isEnabled () {
	checkWidget ();
	return getEnabled ();
}

public boolean isFocusControl () {
  checkWidget ();
  if(super.isFocusControl()) return true;
  KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
  return keyboardFocusManager.getFocusOwner() == null && keyboardFocusManager.getFocusedWindow() == handle;
}

//public boolean isVisible () {
//	checkWidget ();
//	return getVisible ();
//}
//
//int hwndMDIClient () {
//	if (hwndMDIClient == 0) {
//		int widgetStyle = OS.MDIS_ALLCHILDSTYLES | OS.WS_CHILD | OS.WS_CLIPCHILDREN | OS.WS_CLIPSIBLINGS;
//		hwndMDIClient = OS.CreateWindowEx (
//			0,
//			new TCHAR (0, "MDICLIENT", true),
//			null,
//			widgetStyle, 
//			0, 0, 0, 0,
//			handle,
//			0,
//			OS.GetModuleHandle (null),
//			new CREATESTRUCT ());
////		OS.ShowWindow (hwndMDIClient, OS.SW_SHOW);
//	}
//	return hwndMDIClient;
//}

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
 * @see Decorations#setDefaultButton
 * @see Shell#setActive
 * @see Shell#forceActive
 */
public void open () {
	checkWidget ();
	bringToTop ();
  if (isDisposed ()) return;
  layout(); // Added because the default size is not taken into account be layout managers. cf snippet 109
//	/*
//	* Feature on WinCE PPC.  A new application becomes
//	* the foreground application only if it has at least
//	* one visible window before the event loop is started.
//	* The workaround is to explicitely force the shell to
//	* be the foreground window.
//	*/
//	if (OS.IsWinCE) OS.SetForegroundWindow (handle);
//	OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
	setVisible (true);
  // This is to ensure that splashscreens that do not dispatch the events from the queue have some display.
//  handle.paint(handle.getGraphics());
	if (isDisposed ()) return;
//	/*
//	* Bug in Windows XP.  Despite the fact that an icon has been
//	* set for a window, the task bar displays the wrong icon the
//	* first time the window is made visible with ShowWindow() after
//	* a call to BringToTop(), when a long time elapses between the
//	* ShowWindow() and the time the event queue is read.  The icon
//	* in the window trimming is correct but the one in the task
//	* bar does not get updated.  The fix is to call PeekMessage()
//	* with the flag PM_NOREMOVE and PM_QS_SENDMESSAGE to respond
//	* to a cross thread WM_GETICON.
//	* 
//	* NOTE: This allows other cross thread messages to be delivered,
//	* most notably WM_ACTIVATE.
//	*/
//	MSG msg = new MSG ();
//	int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD | OS.PM_QS_SENDMESSAGE;
//	OS.PeekMessage (msg, 0, 0, 0, flags);
//	if (!restoreFocus () && !traverseGroup (true)) setFocus ();
}

//void releaseHandle () {
//	super.releaseHandle ();
//	hwndMDIClient = 0;
//}

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

void releaseParent () {
  /* Do nothing */
}

void releaseWidget () {
	super.releaseWidget ();
	activeMenu = null;
  final Window window = (Window)handle;
  window.setVisible(false);
  SwingUtilities.invokeLater(new Runnable() {
    public void run() {
      window.dispose();
    }
  });
//	display.clearModal (this);
//	if (lpstrTip != 0) {
//		int hHeap = OS.GetProcessHeap ();
//		OS.HeapFree (hHeap, 0, lpstrTip);
//	}
//	lpstrTip = 0;
//	toolTipHandle = 0;
//	if (brushes != null) {
//		for (int i=0; i<brushes.length; i++) {
//			int hBrush = brushes [i];
//			if (hBrush != 0) OS.DeleteObject (hBrush);
//		}
//	}
//	brushes = null;
//	if (OS.IsDBLocale) {
//		if (hIMC != 0) OS.ImmDestroyContext (hIMC);
//	}
	lastActive = null;
	region = null;
}

void removeMenu (Menu menu) {
	super.removeMenu (menu);
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

//LRESULT selectPalette (int hPalette) {
//	int hDC = OS.GetDC (handle);
//	int hOld = OS.SelectPalette (hDC, hPalette, false);
//	int result = OS.RealizePalette (hDC);
//	if (result > 0) {
//		OS.InvalidateRect (handle, null, true);
//	} else {
//		OS.SelectPalette (hDC, hOld, true);
//		OS.RealizePalette (hDC);
//	}
//	OS.ReleaseDC (handle, hDC);
//	return (result > 0) ? LRESULT.ONE : LRESULT.ZERO;
//}

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
 * @see Decorations#setDefaultButton
 * @see Shell#open
 * @see Shell#setActive
 */
public void setActive () {
	checkWidget ();
	if(!isVisible()) return;
//	bringToTop ();
  ((CShell)handle).forceActive();
  // widget could be disposed at this point
}

Control getActiveControl() {
  return lastActive;
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

//void setBounds (int x, int y, int width, int height, int flags, boolean defer) {
//	super.setBounds (x, y, width, height, flags, false);
//}
//
//public void setEnabled (boolean enabled) {
//	checkWidget ();
//	if (((state & DISABLED) == 0) == enabled) return;
//	super.setEnabled (enabled);
//	if (enabled && handle == OS.GetActiveWindow ()) {
//		if (!restoreFocus ()) traverseGroup (true);
//	}
//}

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
  // TODO: implement probably with something like the following...
  InputContext ic = handle.getInputContext();
  boolean imeOn = mode != SWT.NONE && mode != SWT.ROMAN;
//handle.enableInputMethods(imeOn);
  ic.setCompositionEnabled (imeOn);
  if(imeOn) {
    boolean phonetic = (mode & SWT.PHONETIC) != 0;
    boolean nativecs = (mode & SWT.NATIVE) != 0;
    // PHONETIC > NATIVE > ROMAN = ALPHA ; The former is stronger.
    Character.Subset subset;
    if ((mode & SWT.DBCS) != 0) {
      // double byte characters
      subset = InputSubset.FULLWIDTH_LATIN;
      if (nativecs) {
        subset = UnicodeBlock.HIRAGANA;
      }
      if (phonetic) {
        subset = UnicodeBlock.KATAKANA;
      }
    } else {
      // not double byte characters
      subset = InputSubset.LATIN;
      if (phonetic || nativecs) {
        subset = InputSubset.HALFWIDTH_KATAKANA;
      }
    }
    ic.setCharacterSubsets (new Character.Subset[] { subset });
  }
//	if (!OS.IsDBLocale) return;
//	boolean imeOn = mode != SWT.NONE && mode != SWT.ROMAN;
//	int hIMC = OS.ImmGetContext (handle);
//	OS.ImmSetOpenStatus (hIMC, imeOn);
//	if (imeOn) {
//		int [] lpfdwConversion = new int [1], lpfdwSentence = new int [1];
//		if (OS.ImmGetConversionStatus (hIMC, lpfdwConversion, lpfdwSentence)) {
//			int newBits = 0;
//			int oldBits = OS.IME_CMODE_NATIVE | OS.IME_CMODE_KATAKANA;
//			if ((mode & SWT.PHONETIC) != 0) {
//				newBits = OS.IME_CMODE_KATAKANA | OS.IME_CMODE_NATIVE;
//				oldBits = 0;
//			} else {
//				if ((mode & SWT.NATIVE) != 0) {
//					newBits = OS.IME_CMODE_NATIVE;
//					oldBits = OS.IME_CMODE_KATAKANA;
//				}
//			}
//			if ((mode & SWT.DBCS) != 0) {
//				newBits |= OS.IME_CMODE_FULLSHAPE;
//			} else {
//				oldBits |= OS.IME_CMODE_FULLSHAPE;
//			}
//			if ((mode & SWT.ROMAN) != 0) {
//				newBits |= OS.IME_CMODE_ROMAN;
//			} else {
//				oldBits |= OS.IME_CMODE_ROMAN;
//			}
//			lpfdwConversion [0] |= newBits;  lpfdwConversion [0] &= ~oldBits;
//			OS.ImmSetConversionStatus (hIMC, lpfdwConversion [0], lpfdwSentence [0]);
//		}
//	}
//	OS.ImmReleaseContext (handle, hIMC);
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
  if(Compatibility.IS_JAVA_5_OR_GREATER) {
    handle.setMinimumSize(new java.awt.Dimension(width, height));
  }
//	int widthLimit = 0, heightLimit = 0;
//	int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
//	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
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
//	} 
//	minWidth = Math.max (widthLimit, width);
//	minHeight = Math.max (heightLimit, height);
//	Point size = getSize ();
//	int newWidth = Math.max (size.x, minWidth);
//	int newHeight = Math.max (size.y, minHeight);
//	if (minWidth <= widthLimit) minWidth = SWT.DEFAULT;
//	if (minHeight <= heightLimit) minHeight = SWT.DEFAULT;
//	if (newWidth != size.x || newHeight != size.y) setSize (newWidth, newHeight);
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

//void setItemEnabled (int cmd, boolean enabled) {
//	int hMenu = OS.GetSystemMenu (handle, false);
//	if (hMenu == 0) return;
//	int flags = OS.MF_ENABLED;
//	if (!enabled) flags = OS.MF_DISABLED | OS.MF_GRAYED;
//	OS.EnableMenuItem (hMenu, cmd, OS.MF_BYCOMMAND | flags);
//}

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
	if (region != null && region.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
  // TODO: how to implement that?
//	int hRegion = 0;
//	if (region != null) {
//		hRegion = OS.CreateRectRgn (0, 0, 0, 0);
//		OS.CombineRgn (hRegion, region.handle, hRegion, OS.RGN_OR);
//	}
//	OS.SetWindowRgn (handle, hRegion, true);
	this.region = region;
}

//void setToolTipText (int hwnd, String text) {
//	if (OS.IsWinCE) return;
//	if (toolTipHandle == 0) {
//		toolTipHandle = OS.CreateWindowEx (
//			0,
//			new TCHAR (0, OS.TOOLTIPS_CLASS, true),
//			null,
//			OS.TTS_ALWAYSTIP,
//			OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
//			handle,
//			0,
//			OS.GetModuleHandle (null),
//			null);
//		if (toolTipHandle == 0) error (SWT.ERROR_NO_HANDLES);	
//		/*
//		* Feature in Windows.  Despite the fact that the
//		* tool tip text contains \r\n, the tooltip will
//		* not honour the new line unless TTM_SETMAXTIPWIDTH
//		* is set.  The fix is to set TTM_SETMAXTIPWIDTH to
//		* a large value.
//		*/
//		OS.SendMessage (toolTipHandle, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);
//	}
//	TOOLINFO lpti = new TOOLINFO ();
//	lpti.cbSize = TOOLINFO.sizeof;
//	lpti.uId = hwnd;
//	lpti.hwnd = handle;
//	if (text == null) {
//		OS.SendMessage (toolTipHandle, OS.TTM_DELTOOL, 0, lpti);
//	} else {
//		if (OS.SendMessage (toolTipHandle, OS.TTM_GETTOOLINFO, 0, lpti) != 0) {
//			OS.SendMessage (toolTipHandle, OS.TTM_UPDATE, 0, 0);
//		} else {
//			lpti.uFlags = OS.TTF_IDISHWND | OS.TTF_SUBCLASS;
//			lpti.lpszText = OS.LPSTR_TEXTCALLBACK;
//			OS.SendMessage (toolTipHandle, OS.TTM_ADDTOOL, 0, lpti);
//		}
//	}
//}
//
//void setToolTipText (NMTTDISPINFO lpnmtdi, byte [] buffer) {
//	/*
//	* Ensure that the current position of the mouse
//	* is inside the client area of the shell.  This
//	* prevents tool tips from popping up over the
//	* shell trimmings.
//	*/
//	if (!hasCursor ()) return;
//	int hHeap = OS.GetProcessHeap ();
//	if (lpstrTip != 0) OS.HeapFree (hHeap, 0, lpstrTip);
//	int byteCount = buffer.length;
//	lpstrTip = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//	OS.MoveMemory (lpstrTip, buffer, byteCount);
//	lpnmtdi.lpszText = lpstrTip;
//}
//
//void setToolTipText (NMTTDISPINFO lpnmtdi, char [] buffer) {
//	/*
//	* Ensure that the current position of the mouse
//	* is inside the client area of the shell.  This
//	* prevents tool tips from popping up over the
//	* shell trimmings.
//	*/
//	if (!hasCursor ()) return;
//	int hHeap = OS.GetProcessHeap ();
//	if (lpstrTip != 0) OS.HeapFree (hHeap, 0, lpstrTip);
//	int byteCount = buffer.length * 2;
//	lpstrTip = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
//	OS.MoveMemory (lpstrTip, buffer, byteCount);
//	lpnmtdi.lpszText = lpstrTip;
//}

Control initialFocusedControl;

void setInitialFocusedControl(Control initialFocusedControl) {
  this.initialFocusedControl = initialFocusedControl;
}

public void setVisible (boolean visible) {
	checkWidget ();
	if(handle.isVisible() == visible) {
	  return;
	}
	boolean isFocusable = handle instanceof Window? ((Window)handle).getFocusableWindowState(): true;
  handle.setVisible(visible);
  if(visible) {
    if(initialFocusedControl != null) {
      initialFocusedControl.setFocus();
      initialFocusedControl = null;
    } else {
      if(isFocusable) {
        if (!traverseGroup (true)) setFocus ();
      }
    }
    layout();
  }
//	if (drawCount != 0) {
//		if (((state & HIDDEN) == 0) == visible) return;
//	} else {
//		if (visible == OS.IsWindowVisible (handle)) return;
//	}
//	
//	/*
//	* Feature in Windows.  When ShowWindow() is called used to hide
//	* a window, Windows attempts to give focus to the parent. If the
//	* parent is disabled by EnableWindow(), focus is assigned to
//	* another windows on the desktop.  This means that if you hide
//	* a modal window before the parent is enabled, the parent will
//	* not come to the front.  The fix is to change the modal state
//	* before hiding or showing a window so that this does not occur.
//	*/
//	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
//	if ((style & mask) != 0) {
//		if (visible) {
//			display.setModalShell (this);
//			Control control = display._getFocusControl ();
//			if (control != null && !control.isActive ()) bringToTop ();
//			int hwndShell = OS.GetActiveWindow ();
//			if (hwndShell == 0) {
//				if (parent != null) hwndShell = parent.handle;
//			}
//			if (hwndShell != 0) {
//				OS.SendMessage (hwndShell, OS.WM_CANCELMODE, 0, 0);
//			}
//			OS.ReleaseCapture ();
//		} else {
//			display.clearModal (this);
//		}
//	} else {
//		updateModal ();
//	}
//	
//	/*
//	* Bug in Windows.  Calling ShowOwnedPopups() to hide the
//	* child windows of a hidden window causes the application
//	* to be deactivated.  The fix is to call ShowOwnedPopups()
//	* to hide children before hiding the parent.
//	*/
//	if (showWithParent && !visible) {
//		if (!OS.IsWinCE) OS.ShowOwnedPopups (handle, false);
//	}
//	super.setVisible (visible);
//	if (isDisposed ()) return;
//	if (showWithParent == visible) return;
//	showWithParent = visible;
//	if (visible) {
//		if (!OS.IsWinCE) OS.ShowOwnedPopups (handle, true);
//	}
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

//void updateModal () {
//	if (Display.TrimEnabled) {
//		setItemEnabled (OS.SC_CLOSE, isActive ());
//	} else {
//		OS.EnableWindow (handle, isActive ());
//	}
//}
//
//CREATESTRUCT widgetCreateStruct () {
//	return null;
//}
//
//int widgetParent () {
//	if (handle != 0) return handle;
//	return parent != null ? parent.handle : 0;
//}
//
//int widgetExtStyle () {
//	int bits = super.widgetExtStyle () & ~OS.WS_EX_MDICHILD;
//	if ((style & SWT.TOOL) != 0) bits |= OS.WS_EX_TOOLWINDOW;
//	
//	/*
//	* Feature in Windows.  When a window that does not have a parent
//	* is created, it is automatically added to the Windows Task Bar,
//	* even when it has no title.  The fix is to use WS_EX_TOOLWINDOW
//	* which does not cause the window to appear in the Task Bar.
//	*/
//	if (!OS.IsWinCE) {
//		if (parent == null) {
//			if ((style & SWT.ON_TOP) != 0) {
//				int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
//				if ((style & SWT.NO_TRIM) != 0 || (style & trim) == 0) {
//					bits |= OS.WS_EX_TOOLWINDOW;
//				}
//			}
//		}
//	}
//	
//	/*
//	* Bug in Windows 98 and NT.  Creating a window with the
//	* WS_EX_TOPMOST extended style can result in a dialog shell
//	* being moved behind its parent.  The exact case where this
//	* happens is a shell with two dialog shell children where
//	* each dialog child has another hidden dialog child with
//	* the WS_EX_TOPMOST extended style.  Clicking on either of
//	* the visible dialogs causes them to become active but move
//	* to the back, behind the parent shell.  The fix is to
//	* disallow the WS_EX_TOPMOST extended style on Windows 98
//	* and NT.
//	*/
//	if (parent != null) {
//		if (OS.IsWin95) return bits;
//		if (OS.WIN32_VERSION < OS.VERSION (4, 10)) {
//			return bits;
//		}
//	}
//	if ((style & SWT.ON_TOP) != 0) bits |= OS.WS_EX_TOPMOST;
//	return bits;
//}
//
//TCHAR windowClass () {
//	if (OS.IsSP) return DialogClass;
//	if ((style & SWT.TOOL) != 0) {
//		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.BORDER | SWT.RESIZE;
//		if ((style & trim) == 0) return display.windowShadowClass;
//	}
//	return parent != null ? DialogClass : super.windowClass ();
//}
//
//int windowProc () {
//	if (OS.IsSP) return DialogProc;
//	if ((style & SWT.TOOL) != 0) {
//		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.BORDER | SWT.RESIZE;
//		if ((style & trim) == 0) super.windowProc ();
//	}
//	return parent != null ? DialogProc : super.windowProc ();
//}
//
//int widgetStyle () {
//	int bits = super.widgetStyle ();
//	if (handle != 0) return bits | OS.WS_CHILD;
//	bits &= ~OS.WS_CHILD;
//	/*
//	* Feature in WinCE.  Calling CreateWindowEx () with WS_OVERLAPPED
//	* and a parent window causes the new window to become a WS_CHILD of
//	* the parent instead of a dialog child.  The fix is to use WS_POPUP
//	* for a window with a parent.  
//	* 
//	* Feature in WinCE PPC.  A window without a parent with WS_POPUP
//	* always shows on top of the Pocket PC 'Today Screen'. The fix
//	* is to not set WS_POPUP for a window without a parent on WinCE
//	* devices.
//	* 
//	* NOTE: WS_POPUP causes CreateWindowEx () to ignore CW_USEDEFAULT
//	* and causes the default window location and size to be zero.
//	*/
//	if (OS.IsWinCE) {
//		if (OS.IsSP) return bits | OS.WS_POPUP;
//		return parent == null ? bits : bits | OS.WS_POPUP;
//	}
//	
//	/*
//	* Use WS_OVERLAPPED for all windows, either dialog or top level
//	* so that CreateWindowEx () will respect CW_USEDEFAULT and set
//	* the default window location and size.
//	* 
//	* NOTE:  When a WS_OVERLAPPED window is created, Windows gives
//	* the new window WS_CAPTION style bits.  These two constants are
//	* as follows:
//	* 
//	* 	WS_OVERLAPPED = 0
//	* 	WS_CAPTION = WS_BORDER | WS_DLGFRAME
//	* 
//	*/
//	return bits | OS.WS_OVERLAPPED | OS.WS_CAPTION;
//}
//
//LRESULT WM_ACTIVATE (int wParam, int lParam) {
//	if (OS.IsPPC) {
//		/*
//		* Note: this does not work when we get WM_ACTIVATE prior
//		* to adding a listener.
//		*/
//		if (hooks (SWT.HardKeyDown) || hooks (SWT.HardKeyUp)) {
//			int fActive = wParam & 0xFFFF;
//			int hwnd = fActive != 0 ? handle : 0;
//			for (int bVk=OS.VK_APP1; bVk<=OS.VK_APP6; bVk++) {
//				OS.SHSetAppKeyWndAssoc ((byte) bVk, hwnd);
//			}
//		}
//		/* Restore SIP state when window is activated */
//		if ((wParam & 0xFFFF) != 0) {
//			OS.SHSipPreference (handle, psai.fSipUp == 0 ? OS.SIP_DOWN : OS.SIP_UP);
//		} 
//	}
//
//	/*
//	* Bug in Windows XP.  When a Shell is deactivated, the
//	* IME composition window does not go away. This causes
//	* repaint issues.  The fix is to close the IME ourselves
//	* when the Shell is deactivated.
//	* 
//	* Note.  When the Shell is reactivated, the text in the
//	* composition window has been lost.
//	*/
//	if (OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
//		if ((wParam & 0xFFFF) == 0 && OS.IsDBLocale && hIMC != 0) {
//			OS.ImmSetOpenStatus (hIMC, false);
//		}
//	}
//	
//	LRESULT result = super.WM_ACTIVATE (wParam, lParam);
//	if (parent != null) return LRESULT.ZERO;
//	return result;
//}
//
//LRESULT WM_COMMAND (int wParam, int lParam) {
//	if (OS.IsPPC) {
//		/*
//		* Note in WinCE PPC:  Close the Shell when the "Done Button" has
//		* been pressed. lParam is either 0 (PocketPC 2002) or the handle
//		* to the Shell (PocketPC).
//		*/
//		int loWord = wParam & 0xFFFF;
//		if (loWord == OS.IDOK && (lParam == 0 || lParam == handle)) {
//			OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
//			return LRESULT.ZERO;			
//		}
//	}
//	/*
//	* Feature in Windows.  On PPC, the menu is not actually an HMENU.
//	* By observation, it is a tool bar that is configured to look like
//	* a menu.  Therefore, when the PPC menu sends WM_COMMAND messages,
//	* lParam is not zero because the WM_COMMAND was not sent from a menu.
//	* Sub menu item events originate from the menu bar.  Top menu items
//	* events originate from a tool bar.  The fix is to detect the source
//	* of the WM_COMMAND and set lParam to zero to pretend that the message
//	* came from a real Windows menu, not a tool bar.
//	*/
//	if (OS.IsPPC || OS.IsSP) {
//		if (menuBar != null) {
//			int hwndCB = menuBar.hwndCB;
//			if (lParam != 0 && hwndCB != 0) {
//				if (lParam == hwndCB) {
//					return super.WM_COMMAND (wParam, 0);
//				} else {
//					int hwndChild = OS.GetWindow (hwndCB, OS.GW_CHILD);
//					if (lParam == hwndChild) return super.WM_COMMAND (wParam, 0);					
//				}
//			}
//		}
//	}
//	return super.WM_COMMAND (wParam, lParam);
//}
//
//LRESULT WM_DESTROY (int wParam, int lParam) {
//	LRESULT result = super.WM_DESTROY (wParam, lParam);
//	/*
//	* When the shell is a WS_CHILD window of a non-SWT
//	* window, the destroy code does not get called because
//	* the non-SWT window does not call dispose ().  Instead,
//	* the destroy code is called here in WM_DESTROY.
//	*/
//	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//	if ((bits & OS.WS_CHILD) != 0) {
//		releaseChild ();
//		releaseResources ();
//	}
//	return result;
//}
//
//LRESULT WM_ENTERIDLE (int wParam, int lParam) {
//	LRESULT result = super.WM_ENTERIDLE (wParam, lParam);
//	if (result != null) return result;
//	if (OS.IsWinCE) {
//		if (display.runAsyncMessages (true)) display.wakeThread ();
//	}
//	return result;
//}
//
//LRESULT WM_GETMINMAXINFO (int wParam, int lParam) {
//	LRESULT result = super.WM_GETMINMAXINFO (wParam, lParam);
//	if (result != null) return result;
//	if (minWidth != SWT.DEFAULT || minHeight != SWT.DEFAULT) {
//		MINMAXINFO info = new MINMAXINFO ();
//		OS.MoveMemory (info, lParam, MINMAXINFO.sizeof);
//		if (minWidth != SWT.DEFAULT) info.ptMinTrackSize_x = minWidth;
//		if (minHeight != SWT.DEFAULT) info.ptMinTrackSize_y = minHeight;
//		OS.MoveMemory (lParam, info, MINMAXINFO.sizeof);
//		return LRESULT.ZERO;
//	}
//	return result;
//}
//
//LRESULT WM_MOUSEACTIVATE (int wParam, int lParam) {
//	LRESULT result = super.WM_MOUSEACTIVATE (wParam, lParam);
//	if (result != null) return result;
//	
//	/*
//	* Check for WM_MOUSEACTIVATE when an MDI shell is active
//	* and stop the normal shell activation but allow the mouse
//	* down to be delivered.
//	*/
//	int hittest = (short) (lParam & 0xFFFF);
//	switch (hittest) {
//		case OS.HTERROR:
//		case OS.HTTRANSPARENT:
//		case OS.HTNOWHERE:
//			break;
//		default: {
//			Control control = display._getFocusControl ();
//			if (control != null) {
//				Decorations decorations = control.menuShell ();
//				if (decorations.getShell () == this && decorations != this) {
//					display.ignoreRestoreFocus = true;
//					display.lastHittest = hittest;
//					display.lastHittestControl = null;
//					if (hittest == OS.HTMENU || hittest == OS.HTSYSMENU) {
//						display.lastHittestControl = control;
//						return null;
//					}
//					if (OS.IsWin95 && hittest == OS.HTCAPTION) {
//						display.lastHittestControl = control;
//					}
//					return new LRESULT (OS.MA_NOACTIVATE);
//				}
//			}
//		}
//	}
//	if (hittest == OS.HTMENU) return null;
//	
//	/*
//	* Get the current location of the cursor,
//	* not the location of the cursor when the
//	* WM_MOUSEACTIVATE was generated.  This is
//	* strictly incorrect but is necessary in
//	* order to support Activate and Deactivate
//	* events for embedded widgets that have
//	* their own event loop.  In that case, the
//	* cursor location reported by GetMessagePos
//	* is the one for our event loop, not the
//	* embedded widget's event loop.
//	*/
//	POINT pt = new POINT ();
//	if (!OS.GetCursorPos (pt)) {
//		int pos = OS.GetMessagePos ();
//		pt.x = (short) (pos & 0xFFFF);
//		pt.y = (short) (pos >> 16);
//	}
//	int hwnd = OS.WindowFromPoint (pt);
//	if (hwnd == 0) return null;
//	Control control = display.findControl (hwnd);
//	setActiveControl (control);
//	
//	/*
//	* This code is intentionally commented.  On some platforms,
//	* shells that are created with SWT.NO_TRIM won't take focus
//	* when the user clicks in the client area or on the border.
//	* This behavior is usedful when emulating tool tip shells
//	* Until this behavior is specified, this code will remain
//	* commented.
//	*/
////	if ((style & SWT.NO_TRIM) != 0) {
////		if (hittest == OS.HTBORDER || hittest == OS.HTCLIENT) {
////			return new LRESULT (OS.MA_NOACTIVATE);
////		}
////	}
//	return null;
//}
//
//LRESULT WM_NCHITTEST (int wParam, int lParam) {
//	if (!OS.IsWindowEnabled (handle)) return null;
//	if (!isEnabled () || !isActive ()) {
//		if (!Display.TrimEnabled) return new LRESULT (OS.HTNOWHERE);
//		int hittest = callWindowProc (handle, OS.WM_NCHITTEST, wParam, lParam);
//		if (hittest == OS.HTCLIENT || hittest == OS.HTMENU) hittest = OS.HTBORDER;
//		return new LRESULT (hittest);
//	}
//	if (menuBar != null && !menuBar.getEnabled ()) {
//		int hittest = callWindowProc (handle, OS.WM_NCHITTEST, wParam, lParam);
//		if (hittest == OS.HTMENU) hittest = OS.HTBORDER;
//		return new LRESULT (hittest);
//	}
//	return null;
//}
//
//LRESULT WM_NCLBUTTONDOWN (int wParam, int lParam) {
//	LRESULT result = super.WM_NCLBUTTONDOWN (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* When the normal activation was interruped in WM_MOUSEACTIVATE
//	* because the active shell was an MDI shell, set the active window
//	* to the top level shell but lock the active window and stop focus
//	* changes.  This allows the user to interact the top level shell
//	* in the normal manner.
//	*/
//	if (!display.ignoreRestoreFocus) return result;
//	Display display = this.display;
//	int hwndActive = 0;
//	boolean fixActive = OS.IsWin95 && display.lastHittest == OS.HTCAPTION;
//	if (fixActive) hwndActive = OS.SetActiveWindow (handle);
//	display.lockActiveWindow = true;
//	int code = callWindowProc (handle, OS.WM_NCLBUTTONDOWN, wParam, lParam);
//	display.lockActiveWindow = false;
//	if (fixActive) OS.SetActiveWindow (hwndActive);
//	Control focusControl = display.lastHittestControl;
//	if (focusControl != null && !focusControl.isDisposed ()) {
//		focusControl.setFocus ();
//	}
//	display.lastHittestControl = null;
//	display.ignoreRestoreFocus = false;
//	return new LRESULT (code);
//}
//
//LRESULT WM_PALETTECHANGED (int wParam, int lParam) {
//	if (wParam != handle) {
//		int hPalette = display.hPalette;
//		if (hPalette != 0) return selectPalette (hPalette);
//	}
//	return super.WM_PALETTECHANGED (wParam, lParam);
//}
//
//LRESULT WM_QUERYNEWPALETTE (int wParam, int lParam) {
//	int hPalette = display.hPalette;
//	if (hPalette != 0) return selectPalette (hPalette);
//	return super.WM_QUERYNEWPALETTE (wParam, lParam);
//}
//
//LRESULT WM_SETCURSOR (int wParam, int lParam) {
//	/*
//	* Feature in Windows.  When the shell is disabled
//	* by a Windows standard dialog (like a MessageBox
//	* or FileDialog), clicking in the shell does not
//	* bring the shell or the dialog to the front. The
//	* fix is to detect this case and bring the shell
//	* forward.
//	*/
//	int msg = (short) (lParam >> 16);
//	if (msg == OS.WM_LBUTTONDOWN) {
//		if (!Display.TrimEnabled) {
//			Shell modalShell = display.getModalShell ();
//			if (modalShell != null && !isActive ()) {
//				int hwndModal = modalShell.handle;
//				if (OS.IsWindowEnabled (hwndModal)) {
//					OS.SetActiveWindow (hwndModal);
//				}
//			}
//		}
//		if (!OS.IsWindowEnabled (handle)) {
//			if (!OS.IsWinCE) {
//				int hwndPopup = OS.GetLastActivePopup (handle);
//				if (hwndPopup != 0 && hwndPopup != handle) {
//					if (display.getControl (hwndPopup) == null) {
//						if (OS.IsWindowEnabled (hwndPopup)) {
//							OS.SetActiveWindow (hwndPopup);
//						}
//					}
//				}
//			}
//		}
//	}
//	/*
//	* When the shell that contains a cursor is disabled,
//	* WM_SETCURSOR is called with HTERROR.  Normally,
//	* when a control is disabled, the parent will get
//	* mouse and cursor events.  In the case of a disabled
//	* shell, there is no enabled parent.  In order to
//	* show the cursor when a shell is disabled, it is
//	* necessary to override WM_SETCURSOR when called
//	* with HTERROR to set the cursor but only when the
//	* mouse is in the client area of the shell.
//	*/
//	int hitTest = (short) (lParam & 0xFFFF);
//	if (hitTest == OS.HTERROR) {
//		if (!getEnabled ()) {
//			Control control = display.getControl (wParam);
//			if (control == this && cursor != null) {
//				POINT pt = new POINT ();
//				if (OS.GetCursorPos (pt)) {
//					OS.ScreenToClient (handle, pt);
//					RECT rect = new RECT ();
//					OS.GetClientRect (handle, rect);
//					if (OS.PtInRect (rect, pt)) {
//						OS.SetCursor (cursor.handle);
//						switch (msg) {
//							case OS.WM_LBUTTONDOWN:
//							case OS.WM_RBUTTONDOWN:
//							case OS.WM_MBUTTONDOWN:
//							case OS.WM_XBUTTONDOWN:
//								OS.MessageBeep (OS.MB_OK);
//						}
//						return LRESULT.ONE;
//					}
//				}
//			}
//		}
//	}
//	return super.WM_SETCURSOR (wParam, lParam);
//}
//
//LRESULT WM_SETTINGCHANGE (int wParam, int lParam) {
//	LRESULT result = super.WM_SETTINGCHANGE (wParam, lParam);
//	if (result != null) return result;
//	if (OS.IsPPC) {
//		if (wParam == OS.SPI_SETSIPINFO) {
//			/* 
//			* The SIP is in a new state.  Cache its new value.
//			* Resize the Shell if it has the style SWT.RESIZE.
//			* Note that SHHandleWMSettingChange resizes the
//			* Shell and also updates the cached state.
//			*/
//			if ((style & SWT.RESIZE) != 0) {
//				OS.SHHandleWMSettingChange (handle, wParam, lParam, psai);
//				return LRESULT.ZERO;
//			} else {
//				SIPINFO pSipInfo = new SIPINFO ();
//				pSipInfo.cbSize = SIPINFO.sizeof;
//				OS.SipGetInfo (pSipInfo);
//				psai.fSipUp = pSipInfo.fdwFlags & OS.SIPF_ON;					
//			}
//		}
//	}
//	return result;
//}
//
//LRESULT WM_SHOWWINDOW (int wParam, int lParam) {
//	LRESULT result = super.WM_SHOWWINDOW (wParam, lParam);
//	if (result != null) return result;
//	/*
//	* Bug in Windows.  If the shell is hidden while the parent
//	* is iconic,  Windows shows the shell when the parent is
//	* deiconified.  This does not happen if the shell is hidden
//	* while the parent is not an icon.  The fix is to track
//	* visible state for the shell and refuse to show the shell
//	* when the parent is shown.
//	*/
//	if (lParam == OS.SW_PARENTOPENING) {
//		Control control = this;
//		while (control != null) {
//			Shell shell = control.getShell ();
//			if (!shell.showWithParent) return LRESULT.ZERO;
//			control = control.parent;
//		}
//	}
//	return result;
//}
//
//LRESULT WM_SYSCOMMAND (int wParam, int lParam) {
//	LRESULT result = super.WM_SYSCOMMAND (wParam, lParam);
//	//This code is intentionally commented
////	if (result != null) return result;
////	/*
////	* Feature in Windows.  When a window is minimized, the memory
////	* for the working set of the process.  For applications that
////	* use a lot of memory, when the window is restored, it can take
////	* a long time (sometimes minutes) before the application becomes
////	* responsive.   The fix is to intercept WM_SYSCOMMAND looking
////	* for SC_MINIMIZE and use ShowWindow() with SW_SHOWMINIMIZED to
////	* minimize the window rather than allowing the default window
////	* proc to do it when more that 64Meg of memory is being used.
////	* 
////	* NOTE:  The default window proc activates the next top-level
////	* window in the Z order while ShowWindow () with SW_SHOWMINIMIZED
////	* does not.  There is no fix for this at this time.
////	*/
////	int cmd = wParam & 0xFFF0;
////	switch (cmd) {
////		case OS.SC_MINIMIZE:
////			long memory = Runtime.getRuntime ().totalMemory ();
////			if (memory > 64000000) {
////				OS.ShowWindow (handle, OS.SW_SHOWMINIMIZED);
////				return LRESULT.ZERO;
////			}
////	}
//	return result;
//}
//
//LRESULT WM_WINDOWPOSCHANGING (int wParam, int lParam) {
//	LRESULT result = super.WM_WINDOWPOSCHANGING (wParam,lParam);
//	if (result != null) return result;
//	WINDOWPOS lpwp = new WINDOWPOS ();
//	OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
//	if ((lpwp.flags & OS.SWP_NOSIZE) == 0) {
//		lpwp.cx = Math.max (lpwp.cx, minWidth);
//		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
//		if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
//			lpwp.cx = Math.max (lpwp.cx, OS.GetSystemMetrics (OS.SM_CXMINTRACK));
//		}
//		lpwp.cy = Math.max (lpwp.cy, minHeight);
//		if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
//			if ((style & SWT.RESIZE) != 0) {
//				lpwp.cy = Math.max (lpwp.cy, OS.GetSystemMetrics (OS.SM_CYMINTRACK));
//			} else {
//				RECT rect = new RECT ();
//				int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
//				int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
//				OS.AdjustWindowRectEx (rect, bits1, false, bits2);
//				lpwp.cy = Math.max (lpwp.cy, rect.bottom - rect.top);
//			}
//		}
//		OS.MoveMemory (lParam, lpwp, WINDOWPOS.sizeof);
//	}
//	return result;
//}

public void processEvent(AWTEvent e) {
  int id = e.getID();
  switch(id) {
  case ActionEvent.ACTION_PERFORMED: if(!hooks(SWT.Traverse)) { super.processEvent(e); return; } break;
  case WindowEvent.WINDOW_ACTIVATED: if(!hooks(SWT.Activate)) { super.processEvent(e); return; } break;
  case WindowEvent.WINDOW_DEACTIVATED: if(!hooks(SWT.Deactivate)) { super.processEvent(e); return; } break;
  case WindowEvent.WINDOW_CLOSED: if(!hooks(SWT.Close)) { super.processEvent(e); return; } break;
  case WindowEvent.WINDOW_ICONIFIED: if(!hooks(SWT.Iconify)) { super.processEvent(e); return; } break;
  case WindowEvent.WINDOW_DEICONIFIED: if(!hooks(SWT.Deiconify)) { super.processEvent(e); return; } break;
  case WindowEvent.WINDOW_CLOSING: if(!isEnabled()) { super.processEvent(e); return; } break;
  default: { super.processEvent(e); return; }
  }
  if(isDisposed()) {
    super.processEvent(e);
    return;
  }
  UIThreadUtils.startExclusiveSection(getDisplay());
  if(isDisposed()) {
    UIThreadUtils.stopExclusiveSection();
    super.processEvent(e);
    return;
  }
  try {
    switch(id) {
    case WindowEvent.WINDOW_ACTIVATED: sendEvent(SWT.Activate); break;
    case WindowEvent.WINDOW_DEACTIVATED: sendEvent(SWT.Deactivate); break;
    case WindowEvent.WINDOW_CLOSED: sendEvent(SWT.Close); break;
    case WindowEvent.WINDOW_ICONIFIED: sendEvent(SWT.Iconify); break;
    case WindowEvent.WINDOW_DEICONIFIED: sendEvent(SWT.Deiconify); break;
    case WindowEvent.WINDOW_CLOSING:
      if(isEnabled()) {
        closeWidget();
      }
      break;
    case ActionEvent.ACTION_PERFORMED:
      Event event = new Event();
      event.detail = SWT.TRAVERSE_ESCAPE;
      sendEvent(SWT.Traverse, event);
      if(event.doit) {
        close();
      }
    }
    super.processEvent(e);
  } catch(Throwable t) {
    UIThreadUtils.storeException(t);
  } finally {
    UIThreadUtils.stopExclusiveSection();
  }
}

}

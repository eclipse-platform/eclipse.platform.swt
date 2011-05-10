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
	Menu activeMenu;
	ToolTip [] toolTips;
	int /*long*/ hIMC, hwndMDIClient, lpstrTip, toolTipHandle, balloonTipHandle;
	int minWidth = SWT.DEFAULT, minHeight = SWT.DEFAULT;
	int /*long*/ [] brushes;
	boolean showWithParent, fullScreen, wasMaximized, modified, center;
	String toolTitle, balloonTitle;
	int /*long*/ toolIcon, balloonIcon;
	int /*long*/ windowProc;
	Control lastActive;
	SHACTIVATEINFO psai;
	static /*final*/ int /*long*/ ToolTipProc;
	static final int /*long*/ DialogProc;
	static final TCHAR DialogClass = new TCHAR (0, OS.IsWinCE ? "Dialog" : "#32770", true);
	final static int [] SYSTEM_COLORS = {
		OS.COLOR_BTNFACE,
		OS.COLOR_WINDOW,
		OS.COLOR_BTNTEXT,
		OS.COLOR_WINDOWTEXT,
		OS.COLOR_HIGHLIGHT,
		OS.COLOR_SCROLLBAR,
	};
	final static int BRUSHES_SIZE = 32;
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
	this.handle = handle;
	if (handle != 0 && !embedded) {
		state |= FOREIGN_HANDLE;
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
public static Shell win32_new (Display display, int /*long*/ handle) {
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

int /*long*/ balloonTipHandle () {
	if (balloonTipHandle == 0) createBalloonTipHandle ();
	return balloonTipHandle;
}

int /*long*/ callWindowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (handle == 0) return 0;
	if (hwnd == toolTipHandle || hwnd == balloonTipHandle) {
		return OS.CallWindowProc (ToolTipProc, hwnd, msg, wParam, lParam);
	}
	if (hwndMDIClient != 0) {
		return OS.DefFrameProc (hwnd, hwndMDIClient, msg, wParam, lParam);
	}
	if (windowProc != 0) {
		return OS.CallWindowProc (windowProc, hwnd, msg, wParam, lParam);
	}
	if ((style & SWT.TOOL) != 0) {
		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.BORDER | SWT.RESIZE;
		if ((style & trim) == 0) return OS.DefWindowProc (hwnd, msg, wParam, lParam);
	}
	if (parent != null) {
		switch (msg) {
			case OS.WM_KILLFOCUS:
			case OS.WM_SETFOCUS: 
				return OS.DefWindowProc (hwnd, msg, wParam, lParam);
		}
		return OS.CallWindowProc (DialogProc, hwnd, msg, wParam, lParam);
	}
	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
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

void createBalloonTipHandle () {
	balloonTipHandle = OS.CreateWindowEx (
		0,
		new TCHAR (0, OS.TOOLTIPS_CLASS, true),
		null,
		OS.TTS_ALWAYSTIP | OS.TTS_NOPREFIX | OS.TTS_BALLOON,
		OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
		handle,
		0,
		OS.GetModuleHandle (null),
		null);
	if (balloonTipHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (ToolTipProc == 0) {
		ToolTipProc = OS.GetWindowLongPtr (balloonTipHandle, OS.GWLP_WNDPROC);
	}
	/*
	* Feature in Windows.  Despite the fact that the
	* tool tip text contains \r\n, the tooltip will
	* not honour the new line unless TTM_SETMAXTIPWIDTH
	* is set.  The fix is to set TTM_SETMAXTIPWIDTH to
	* a large value.
	*/
	OS.SendMessage (balloonTipHandle, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);
	display.addControl (balloonTipHandle, this);
	OS.SetWindowLongPtr (balloonTipHandle, OS.GWLP_WNDPROC, display.windowProc);
}

void createHandle () {
	boolean embedded = handle != 0 && (state & FOREIGN_HANDLE) == 0;
	
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
	if (handle == 0 || embedded) {
		super.createHandle ();
	} else {
		state |= CANVAS;
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
			state |= THEME_BACKGROUND;
		}
		windowProc = OS.GetWindowLongPtr (handle, OS.GWL_WNDPROC);
	}
	
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
		/*
		* Bug in Windows.  When the WS_CAPTION bits are cleared using
		* SetWindowLong(), Windows does not resize the client area of
		* the window to get rid of the caption until the first resize.
		* The fix is to use SetWindowPos() with SWP_DRAWFRAME to force
		* the frame to be redrawn and resized.
		*/
		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
		int flags = OS.SWP_DRAWFRAME | OS.SWP_NOMOVE | OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE;
		SetWindowPos (handle, 0, 0, 0, 0, 0, flags);
		if (OS.IsWinCE) _setMaximized (true);
		if (OS.IsPPC) {
			psai = new SHACTIVATEINFO ();
			psai.cbSize = SHACTIVATEINFO.sizeof;
		}
	}
	if (OS.IsDBLocale) {
		hIMC = OS.ImmCreateContext ();
		if (hIMC != 0) OS.ImmAssociateContext (handle, hIMC);
	}
}

void createToolTip (ToolTip toolTip) {
	int id = 0;
	if (toolTips == null) toolTips = new ToolTip [4];
	while (id < toolTips.length && toolTips [id] != null) id++;
	if (id == toolTips.length) {
		ToolTip [] newToolTips = new ToolTip [toolTips.length + 4];
		System.arraycopy (toolTips, 0, newToolTips, 0, toolTips.length);
		toolTips = newToolTips;
	}
	toolTips [id] = toolTip;
	toolTip.id = id + Display.ID_START;
	if (OS.IsWinCE) return;
	TOOLINFO lpti = new TOOLINFO ();
	lpti.cbSize = TOOLINFO.sizeof;
	lpti.hwnd = handle;
	lpti.uId = toolTip.id;
	lpti.uFlags = OS.TTF_TRACK;
	lpti.lpszText = OS.LPSTR_TEXTCALLBACK;
	OS.SendMessage (toolTip.hwndToolTip (), OS.TTM_ADDTOOL, 0, lpti);
}

void createToolTipHandle () {
	toolTipHandle = OS.CreateWindowEx (
		0,
		new TCHAR (0, OS.TOOLTIPS_CLASS, true),
		null,
		OS.TTS_ALWAYSTIP | OS.TTS_NOPREFIX,
		OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
		handle,
		0,
		OS.GetModuleHandle (null),
		null);
	if (toolTipHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (ToolTipProc == 0) {
		ToolTipProc = OS.GetWindowLongPtr (toolTipHandle, OS.GWLP_WNDPROC);
	}
	/*
	* Feature in Windows.  Despite the fact that the
	* tool tip text contains \r\n, the tooltip will
	* not honour the new line unless TTM_SETMAXTIPWIDTH
	* is set.  The fix is to set TTM_SETMAXTIPWIDTH to
	* a large value.
	*/
	OS.SendMessage (toolTipHandle, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);
	display.addControl (toolTipHandle, this);
	OS.SetWindowLongPtr (toolTipHandle, OS.GWLP_WNDPROC, display.windowProc);
}

void deregister () {
	super.deregister ();
	if (toolTipHandle != 0) display.removeControl (toolTipHandle);
	if (balloonTipHandle != 0) display.removeControl (balloonTipHandle);
}

void destroyToolTip (ToolTip toolTip) {
	if (toolTips == null) return;
	toolTips [toolTip.id - Display.ID_START] = null;
	if (OS.IsWinCE) return;
	if (balloonTipHandle != 0) {
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		lpti.uId = toolTip.id;
		lpti.hwnd = handle;
		OS.SendMessage (balloonTipHandle, OS.TTM_DELTOOL, 0, lpti);
	}
	toolTip.id = -1;
}

void destroyWidget () {
	fixActiveShell ();
	super.destroyWidget ();
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

void enableWidget (boolean enabled) {
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
	}
	if (Display.TrimEnabled) {
		if (isActive ()) setItemEnabled (OS.SC_CLOSE, enabled);
	} else {
		OS.EnableWindow (handle, enabled);
	}
}

int /*long*/ findBrush (int /*long*/ value, int lbStyle) {
	if (lbStyle == OS.BS_SOLID) {
		for (int i=0; i<SYSTEM_COLORS.length; i++) {
			if (value == OS.GetSysColor (SYSTEM_COLORS [i])) {
				return OS.GetSysColorBrush (SYSTEM_COLORS [i]);
			}
		}
	}
	if (brushes == null) brushes = new int /*long*/ [BRUSHES_SIZE];
	LOGBRUSH logBrush = new LOGBRUSH ();
	for (int i=0; i<brushes.length; i++) {
		int /*long*/ hBrush = brushes [i];
		if (hBrush == 0) break;
		OS.GetObject (hBrush, LOGBRUSH.sizeof, logBrush);
		switch (logBrush.lbStyle) {
			case OS.BS_SOLID:
				if (lbStyle == OS.BS_SOLID) {
					if (logBrush.lbColor == value) return hBrush;
				}
				break;
			case OS.BS_PATTERN:
				if (lbStyle == OS.BS_PATTERN) {
					if (logBrush.lbHatch == value) return hBrush;
				}
				break;
		}
	}
	int length = brushes.length;
	int /*long*/ hBrush = brushes [--length];
	if (hBrush != 0) OS.DeleteObject (hBrush);
	System.arraycopy (brushes, 0, brushes, 1, length);
	switch (lbStyle) {
		case OS.BS_SOLID:
			hBrush = OS.CreateSolidBrush ((int)/*64*/value);
			break;
		case OS.BS_PATTERN:
			hBrush = OS.CreatePatternBrush (value);
			break;
	}
	return brushes [0] = hBrush;
}

Control findBackgroundControl () {
	return background != -1 || backgroundImage != null ? this : null;
}

Cursor findCursor () {
	return cursor;
}

Control findThemeControl () {
	return null;
}

ToolTip findToolTip (int id) {
	if (toolTips == null) return null;
	id = id - Display.ID_START;
	return 0 <= id && id < toolTips.length ? toolTips [id] : null;
}

void fixActiveShell () {
	/*
	* Feature in Windows.  When the active shell is disposed
	* or hidden, Windows normally makes the parent shell active
	* and assigns focus.  This does not happen when the parent
	* shell is disabled.  Instead, Windows assigns focus to the
	* next shell on the desktop (possibly a shell in another
	* application).  The fix is to activate the disabled parent
	* shell before disposing or hiding the active shell.
	*/
	int /*long*/ hwndParent = OS.GetParent (handle);
	if (hwndParent != 0 && handle == OS.GetActiveWindow ()) {
		if (!OS.IsWindowEnabled (hwndParent) && OS.IsWindowVisible (hwndParent)) {
			OS.SetActiveWindow (hwndParent);
		}
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

void fixToolTip () {
	/*
	* Bug in Windows.  On XP, when a tooltip is
	* hidden due to a time out or mouse press,
	* the tooltip remains active although no
	* longer visible and won't show again until
	* another tooltip becomes active.  If there
	* is only one tooltip in the window,  it will
	* never show again.  The fix is to remove the
	* current tooltip and add it again every time
	* the mouse leaves the control.
	*/
	if (OS.COMCTL32_MAJOR >= 6) {
		if (toolTipHandle == 0) return;
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		if (OS.SendMessage (toolTipHandle, OS.TTM_GETCURRENTTOOL, 0, lpti) != 0) {
			if ((lpti.uFlags & OS.TTF_IDISHWND) != 0) {
				OS.SendMessage (toolTipHandle, OS.TTM_DELTOOL, 0, lpti);
				OS.SendMessage (toolTipHandle, OS.TTM_ADDTOOL, 0, lpti);
			}
		}
	}
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
	OS.SetForegroundWindow (handle);
}

void forceResize () {
	/* Do nothing */
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
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		byte [] pbAlpha = new byte [1];
		if (OS.GetLayeredWindowAttributes (handle, null, pbAlpha, null)) {
			return pbAlpha [0] & 0xFF;
		}
	}
	return 0xFF;
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

ToolTip getCurrentToolTip () {
	if (toolTipHandle != 0) {
		ToolTip tip = getCurrentToolTip (toolTipHandle);
		if (tip != null) return tip;
	}
	if (balloonTipHandle != 0) {
		ToolTip tip = getCurrentToolTip (balloonTipHandle);
		if (tip != null) return tip;
	}
	return null;
}

ToolTip getCurrentToolTip (int /*long*/ hwndToolTip) {
	if (hwndToolTip == 0) return null;
	if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, 0) != 0) {
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, lpti) != 0) {
			if ((lpti.uFlags & OS.TTF_IDISHWND) == 0) return findToolTip ((int)/*64*/lpti.uId);
		}
	}
	return null;
}

public boolean getEnabled () {
	checkWidget ();
	return (state & DISABLED) == 0;
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
	checkWidget ();
	if (!OS.IsDBLocale) return 0;
	int /*long*/ hIMC = OS.ImmGetContext (handle);
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
	int width = Math.max (0, minWidth);
	int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
		width = Math.max (width, OS.GetSystemMetrics (OS.SM_CXMINTRACK));
	}
	int height = Math.max (0, minHeight);
	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
		if ((style & SWT.RESIZE) != 0) {
			height = Math.max (height, OS.GetSystemMetrics (OS.SM_CYMINTRACK));
		} else {
			RECT rect = new RECT ();
			int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
			int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
			OS.AdjustWindowRectEx (rect, bits1, false, bits2);
			height = Math.max (height, rect.bottom - rect.top);
		}
	}
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
	/* This method is needed for the @since 3.0 Javadoc */
	checkWidget ();
	return region;
}

public Shell getShell () {
	checkWidget ();
	return this;
}

public Point getSize () {
	checkWidget ();
	if (!OS.IsWinCE) {
		if (OS.IsIconic (handle)) return super.getSize ();
	}
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Point (width, height);
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
	checkWidget ();
	return null;
}

Composite findDeferredControl () {
	return layoutCount > 0 ? this : null;
}

public boolean isEnabled () {
	checkWidget ();
	return getEnabled ();
}

public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

int /*long*/ hwndMDIClient () {
	if (hwndMDIClient == 0) {
		int widgetStyle = OS.MDIS_ALLCHILDSTYLES | OS.WS_CHILD | OS.WS_CLIPCHILDREN | OS.WS_CLIPSIBLINGS;
		hwndMDIClient = OS.CreateWindowEx (
			0,
			new TCHAR (0, "MDICLIENT", true),
			null,
			widgetStyle, 
			0, 0, 0, 0,
			handle,
			0,
			OS.GetModuleHandle (null),
			new CREATESTRUCT ());
//		OS.ShowWindow (hwndMDIClient, OS.SW_SHOW);
	}
	return hwndMDIClient;
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
	STARTUPINFO lpStartUpInfo = Display.lpStartupInfo;
	if (lpStartUpInfo == null || (lpStartUpInfo.dwFlags & OS.STARTF_USESHOWWINDOW) == 0) {
		bringToTop ();
		if (isDisposed ()) return;
	}
	/*
	* Feature on WinCE PPC.  A new application becomes
	* the foreground application only if it has at least
	* one visible window before the event loop is started.
	* The workaround is to explicitly force the shell to
	* be the foreground window.
	*/
	if (OS.IsWinCE) OS.SetForegroundWindow (handle);
	OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
	setVisible (true);
	if (isDisposed ()) return;
	/*
	* Bug in Windows XP.  Despite the fact that an icon has been
	* set for a window, the task bar displays the wrong icon the
	* first time the window is made visible with ShowWindow() after
	* a call to BringToTop(), when a long time elapses between the
	* ShowWindow() and the time the event queue is read.  The icon
	* in the window trimming is correct but the one in the task
	* bar does not get updated.  The fix is to call PeekMessage()
	* with the flag PM_NOREMOVE and PM_QS_SENDMESSAGE to respond
	* to a cross thread WM_GETICON.
	* 
	* NOTE: This allows other cross thread messages to be delivered,
	* most notably WM_ACTIVATE.
	*/
	MSG msg = new MSG ();
	int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD | OS.PM_QS_SENDMESSAGE;
	OS.PeekMessage (msg, 0, 0, 0, flags);
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
	if (toolTipHandle != 0) display.addControl (toolTipHandle, this);
	if (balloonTipHandle != 0) display.addControl (balloonTipHandle, this);
}

void releaseBrushes () {
	if (brushes != null) {
		for (int i=0; i<brushes.length; i++) {
			if (brushes [i] != 0) OS.DeleteObject (brushes [i]);
		}
	}
	brushes = null;
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
				toolTip.release (false);
			}
		}
	}
	toolTips = null;
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	hwndMDIClient = 0;
}

void releaseParent () {
	/* Do nothing */
}

void releaseWidget () {
	super.releaseWidget ();
	releaseBrushes ();
	activeMenu = null;
	display.clearModal (this);
	if (lpstrTip != 0) {
		int /*long*/ hHeap = OS.GetProcessHeap ();
		OS.HeapFree (hHeap, 0, lpstrTip);
	}
	lpstrTip = 0;
	toolTipHandle = balloonTipHandle = 0;
	if (OS.IsDBLocale) {
		if (hIMC != 0) OS.ImmDestroyContext (hIMC);
	}
	lastActive = null;
	toolTitle = balloonTitle = null;
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

LRESULT selectPalette (int /*long*/ hPalette) {
	int /*long*/ hDC = OS.GetDC (handle);
	int /*long*/ hOld = OS.SelectPalette (hDC, hPalette, false);
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

boolean sendKeyEvent (int type, int msg, int /*long*/ wParam, int /*long*/ lParam, Event event) {
	if (!isEnabled () || !isActive ()) return false;
	return super.sendKeyEvent (type, msg, wParam, lParam, event);
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
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		alpha &= 0xFF;
		int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
		if (alpha == 0xFF) {
			OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits & ~OS.WS_EX_LAYERED);
			int flags = OS.RDW_ERASE | OS.RDW_INVALIDATE | OS.RDW_FRAME | OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, null, 0, flags);
		} else {
			OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits | OS.WS_EX_LAYERED);
			OS.SetLayeredWindowAttributes (handle, 0, (byte)alpha, OS.LWA_ALPHA);
		}
	}
}

void setBounds (int x, int y, int width, int height, int flags, boolean defer) {
	if (fullScreen) setFullScreen (false);
	/*
	* Bug in Windows.  When a window has alpha and
	* SetWindowPos() is called with SWP_DRAWFRAME,
	* the contents of the window are copied rather
	* than allowing the windows underneath to draw.
	* This causes pixel corruption.  The fix is to
	* clear the SWP_DRAWFRAME bits.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE); 
	if ((bits & OS.WS_EX_LAYERED) != 0) {
		flags &= ~OS.SWP_DRAWFRAME;
	}
	super.setBounds (x, y, width, height, flags, false);
}

public void setEnabled (boolean enabled) {
	checkWidget ();
	if (((state & DISABLED) == 0) == enabled) return;
	super.setEnabled (enabled);
	if (enabled && handle == OS.GetActiveWindow ()) {
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
	checkWidget();
	if (this.fullScreen == fullScreen) return;
	int stateFlags = fullScreen ? OS.SW_SHOWMAXIMIZED : OS.SW_RESTORE;
	int styleFlags = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int mask = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
	if ((style & mask) != 0) {
		if (fullScreen) {
			styleFlags &= ~(OS.WS_CAPTION | OS.WS_MAXIMIZEBOX | OS.WS_MINIMIZEBOX);
		} else {
			styleFlags |= OS.WS_CAPTION;
			if ((style & SWT.MAX) != 0) styleFlags |= OS.WS_MAXIMIZEBOX;
			if ((style & SWT.MIN) != 0) styleFlags |= OS.WS_MINIMIZEBOX;
		}
	}
	if (fullScreen) wasMaximized = getMaximized ();
	boolean visible = isVisible ();
	OS.SetWindowLong (handle, OS.GWL_STYLE, styleFlags);
	if (wasMaximized) {
		OS.ShowWindow (handle, OS.SW_HIDE);
		stateFlags = OS.SW_SHOWMAXIMIZED;
	}
	if (visible) OS.ShowWindow (handle, stateFlags);
	OS.UpdateWindow (handle);
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
	if (!OS.IsDBLocale) return;
	boolean imeOn = mode != SWT.NONE;
	int /*long*/ hIMC = OS.ImmGetContext (handle);
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
			if ((mode & (SWT.DBCS | SWT.NATIVE)) != 0) {
				newBits |= OS.IME_CMODE_FULLSHAPE;
			} else {
				oldBits |= OS.IME_CMODE_FULLSHAPE;
			}
			if ((mode & SWT.ROMAN) != 0) {
				newBits |= OS.IME_CMODE_ROMAN;
			} else {
				oldBits |= OS.IME_CMODE_ROMAN;
			}
			lpfdwConversion [0] |= newBits;
			lpfdwConversion [0] &= ~oldBits;
			OS.ImmSetConversionStatus (hIMC, lpfdwConversion [0], lpfdwSentence [0]);
		}
	}
	OS.ImmReleaseContext (handle, hIMC);
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
		widthLimit = OS.GetSystemMetrics (OS.SM_CXMINTRACK);
		if ((style & SWT.RESIZE) != 0) {
			heightLimit = OS.GetSystemMetrics (OS.SM_CYMINTRACK);
		} else {
			RECT rect = new RECT ();
			int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
			int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
			OS.AdjustWindowRectEx (rect, bits1, false, bits2);
			heightLimit = rect.bottom - rect.top;
		}
	} 
	minWidth = Math.max (widthLimit, width);
	minHeight = Math.max (heightLimit, height);
	Point size = getSize ();
	int newWidth = Math.max (size.x, minWidth);
	int newHeight = Math.max (size.y, minHeight);
	if (minWidth <= widthLimit) minWidth = SWT.DEFAULT;
	if (minHeight <= heightLimit) minHeight = SWT.DEFAULT;
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

void setItemEnabled (int cmd, boolean enabled) {
	int /*long*/ hMenu = OS.GetSystemMenu (handle, false);
	if (hMenu == 0) return;
	int flags = OS.MF_ENABLED;
	if (!enabled) flags = OS.MF_DISABLED | OS.MF_GRAYED;
	OS.EnableMenuItem (hMenu, cmd, OS.MF_BYCOMMAND | flags);
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

void setToolTipText (int /*long*/ hwnd, String text) {
	if (OS.IsWinCE) return;
	TOOLINFO lpti = new TOOLINFO ();
	lpti.cbSize = TOOLINFO.sizeof;
	lpti.hwnd = handle;
	lpti.uId = hwnd;
	int /*long*/ hwndToolTip = toolTipHandle ();
	if (text == null) {
		OS.SendMessage (hwndToolTip, OS.TTM_DELTOOL, 0, lpti);
	} else {
		if (OS.SendMessage (hwndToolTip, OS.TTM_GETTOOLINFO, 0, lpti) != 0) {
			OS.SendMessage (hwndToolTip, OS.TTM_UPDATE, 0, 0);
		} else {
			lpti.uFlags = OS.TTF_IDISHWND | OS.TTF_SUBCLASS;
			lpti.lpszText = OS.LPSTR_TEXTCALLBACK;
			OS.SendMessage (hwndToolTip, OS.TTM_ADDTOOL, 0, lpti);
		}
	}
}

void setToolTipText (NMTTDISPINFO lpnmtdi, byte [] buffer) {
	/*
	* Ensure that the current position of the mouse
	* is inside the client area of the shell.  This
	* prevents tool tips from popping up over the
	* shell trimmings.
	*/
	if (!hasCursor ()) return;
	int /*long*/ hHeap = OS.GetProcessHeap ();
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
	int /*long*/ hHeap = OS.GetProcessHeap ();
	if (lpstrTip != 0) OS.HeapFree (hHeap, 0, lpstrTip);
	int byteCount = buffer.length * 2;
	lpstrTip = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpstrTip, buffer, byteCount);
	lpnmtdi.lpszText = lpstrTip;
}

void setToolTipTitle (int /*long*/ hwndToolTip, String text, int icon) {
	/*
	* Bug in Windows.  For some reason, when TTM_SETTITLE
	* is used to set the title of a tool tip, Windows leaks
	* GDI objects.  This happens even when TTM_SETTITLE is
	* called with TTI_NONE and NULL.  The documentation
	* states that Windows copies the icon and that the 
	* programmer must free the copy but does not provide
	* API to get the icon.  For example, when TTM_SETTITLE
	* is called with ICON_ERROR, when TTM_GETTITLE is used
	* to query the title and the icon, the uTitleBitmap
	* field in the TTGETTITLE struct is zero.  The fix
	* is to remember these values, only set them when then
	* change and leak less.
	* 
	* NOTE:  This only happens on Vista.
	*/
	if (hwndToolTip != toolTipHandle && hwndToolTip != balloonTipHandle) {
		return;
	}
	if (hwndToolTip == toolTipHandle) {
		if (text == toolTitle || (toolTitle != null && toolTitle.equals (text))) {
			if (icon == toolIcon) return;
		}
		toolTitle = text;
		toolIcon = icon;
	} else {
		if (hwndToolTip == balloonTipHandle) {
			if (text == balloonTitle || (balloonTitle != null && balloonTitle.equals (text))) {
				if (icon == toolIcon) return;
			}
			balloonTitle = text;
			balloonIcon = icon;
		}
	}
	if (text != null) {
		/*
		* Feature in Windows. The text point to by pszTitle
		* must not exceed 100 characters in length, including
		* the null terminator.
		*/
		if (text.length () > 99) text = text.substring (0, 99);
		TCHAR pszTitle = new TCHAR (getCodePage (), text, true);
		OS.SendMessage (hwndToolTip, OS.TTM_SETTITLE, icon, pszTitle);
	} else {
		OS.SendMessage (hwndToolTip, OS.TTM_SETTITLE, 0, 0);
	}
}

public void setVisible (boolean visible) {
	checkWidget ();
	/*
	* Feature in Windows.  When ShowWindow() is called used to hide
	* a window, Windows attempts to give focus to the parent. If the
	* parent is disabled by EnableWindow(), focus is assigned to
	* another windows on the desktop.  This means that if you hide
	* a modal window before the parent is enabled, the parent will
	* not come to the front.  The fix is to change the modal state
	* before hiding or showing a window so that this does not occur.
	*/
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & mask) != 0) {
		if (visible) {
			display.setModalShell (this);
			if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
				display.setModalDialog (null);
			}
			Control control = display._getFocusControl ();
			if (control != null && !control.isActive ()) {
				bringToTop ();
				if (isDisposed ()) return;
			}
			int /*long*/ hwndShell = OS.GetActiveWindow ();
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
	} else {
		updateModal ();
	}
	
	/*
	* Bug in Windows.  Calling ShowOwnedPopups() to hide the
	* child windows of a hidden window causes the application
	* to be deactivated.  The fix is to call ShowOwnedPopups()
	* to hide children before hiding the parent.
	*/
	if (showWithParent && !visible) {
		if (!OS.IsWinCE) OS.ShowOwnedPopups (handle, false);
	}
	if (!visible) fixActiveShell ();
	if (visible && center && !moved) {
		center ();
		if (isDisposed ()) return;
	}
	super.setVisible (visible);
	if (isDisposed ()) return;
	if (showWithParent != visible) {
		showWithParent = visible;
		if (visible) {
			if (!OS.IsWinCE) OS.ShowOwnedPopups (handle, true);		
		}
	}
	
	/* Make the foreign window parent appear in the task bar */
	if (visible) {
		if (parent != null && (parent.state & FOREIGN_HANDLE) != 0) {
			int /*long*/ hwndParent = parent.handle;
			int style = OS.GetWindowLong (hwndParent, OS.GWL_EXSTYLE);
			if ((style & OS.WS_EX_TOOLWINDOW) != 0) {
				OS.SetWindowLong (hwndParent, OS.GWL_EXSTYLE, style & ~OS.WS_EX_TOOLWINDOW);
				/*
				* Bug in Windows.  The window does not show in the task bar when
				* WS_EX_TOOLWINDOW is removed after the window has already been shown.
				* The fix is to hide and shown the shell. 
				*/
				OS.ShowWindow (hwndParent, OS.SW_HIDE);
				OS.ShowWindow (hwndParent, OS.SW_RESTORE);
			}
		}
	}
}

void subclass () {
	super.subclass ();
	if (ToolTipProc != 0) {
		int /*long*/ newProc = display.windowProc;
		if (toolTipHandle != 0) {
			OS.SetWindowLongPtr (toolTipHandle, OS.GWLP_WNDPROC, newProc);
		}
		if (balloonTipHandle != 0) {
			OS.SetWindowLongPtr (balloonTipHandle, OS.GWLP_WNDPROC, newProc);
		}
	}
}

int /*long*/ toolTipHandle () {
	if (toolTipHandle == 0) createToolTipHandle ();
	return toolTipHandle;
}

boolean translateAccelerator (MSG msg) {
	if (!isEnabled () || !isActive ()) return false;
	if (menuBar != null && !menuBar.isEnabled ()) return false;
	return translateMDIAccelerator (msg) || translateMenuAccelerator (msg);
}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}

void unsubclass () {
	super.unsubclass ();
	if (ToolTipProc != 0) {
		if (toolTipHandle != 0) {
			OS.SetWindowLongPtr (toolTipHandle, OS.GWLP_WNDPROC, ToolTipProc);
		}	
		if (toolTipHandle != 0) {
			OS.SetWindowLongPtr (toolTipHandle, OS.GWLP_WNDPROC, ToolTipProc);
		}
	}
}

void updateModal () {
	if (Display.TrimEnabled) {
		setItemEnabled (OS.SC_CLOSE, isActive ());
	} else {
		OS.EnableWindow (handle, isActive ());
	}
}

CREATESTRUCT widgetCreateStruct () {
	return null;
}

int /*long*/ widgetParent () {
	if (handle != 0) return handle;
	return parent != null ? parent.handle : 0;
}

int widgetExtStyle () {
	int bits = super.widgetExtStyle () & ~OS.WS_EX_MDICHILD;
	if ((style & SWT.TOOL) != 0) bits |= OS.WS_EX_TOOLWINDOW;
	
	/*
	* Feature in Windows.  When a window that does not have a parent
	* is created, it is automatically added to the Windows Task Bar,
	* even when it has no title.  The fix is to use WS_EX_TOOLWINDOW
	* which does not cause the window to appear in the Task Bar.
	*/
	if (!OS.IsWinCE) {
		if (parent == null) {
			if ((style & SWT.ON_TOP) != 0) {
				int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
				if ((style & SWT.NO_TRIM) != 0 || (style & trim) == 0) {
					bits |= OS.WS_EX_TOOLWINDOW;
				}
			}
		}
	}
	
	/*
	* Bug in Windows 98 and NT.  Creating a window with the
	* WS_EX_TOPMOST extended style can result in a dialog shell
	* being moved behind its parent.  The exact case where this
	* happens is a shell with two dialog shell children where
	* each dialog child has another hidden dialog child with
	* the WS_EX_TOPMOST extended style.  Clicking on either of
	* the visible dialogs causes them to become active but move
	* to the back, behind the parent shell.  The fix is to
	* disallow the WS_EX_TOPMOST extended style on Windows 98
	* and NT.
	*/
	if (parent != null) {
		if (OS.IsWin95) return bits;
		if (OS.WIN32_VERSION < OS.VERSION (4, 10)) {
			return bits;
		}
	}
	if ((style & SWT.ON_TOP) != 0) bits |= OS.WS_EX_TOPMOST;
	return bits;
}

TCHAR windowClass () {
	if (OS.IsSP) return DialogClass;
	if ((style & SWT.TOOL) != 0) {
		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.BORDER | SWT.RESIZE;
		if ((style & trim) == 0) return display.windowShadowClass;
	}
	return parent != null ? DialogClass : super.windowClass ();
}

int /*long*/ windowProc () {
	if (windowProc != 0) return windowProc;
	if (OS.IsSP) return DialogProc;
	if ((style & SWT.TOOL) != 0) {
		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.BORDER | SWT.RESIZE;
		if ((style & trim) == 0) return super.windowProc ();
	}
	return parent != null ? DialogProc : super.windowProc ();
}

int /*long*/ windowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (handle == 0) return 0;
	if (hwnd == toolTipHandle || hwnd == balloonTipHandle) {
		switch (msg) {
			case OS.WM_TIMER: {
				if (wParam != ToolTip.TIMER_ID) break;
				ToolTip tip = getCurrentToolTip (hwnd);
				if (tip != null && tip.autoHide) {
					tip.setVisible (false);
				}
				break;
			}
			case OS.WM_LBUTTONDOWN: {
				ToolTip tip = getCurrentToolTip (hwnd);
				if (tip != null) {
					tip.setVisible (false);
					tip.sendSelectionEvent (SWT.Selection);
				}
				break;
			}
		}
		return callWindowProc (hwnd, msg, wParam, lParam);
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

int widgetStyle () {
	int bits = super.widgetStyle ();
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
	if (OS.IsWinCE) {
		if (OS.IsSP) return bits | OS.WS_POPUP;
		return parent == null ? bits : bits | OS.WS_POPUP;
	}
	
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

LRESULT WM_ACTIVATE (int /*long*/ wParam, int /*long*/ lParam) {
	if (OS.IsPPC) {
		/*
		* Note: this does not work when we get WM_ACTIVATE prior
		* to adding a listener.
		*/
		if (hooks (SWT.HardKeyDown) || hooks (SWT.HardKeyUp)) {
			int fActive = OS.LOWORD (wParam);
			int /*long*/ hwnd = fActive != 0 ? handle : 0;
			for (int bVk=OS.VK_APP1; bVk<=OS.VK_APP6; bVk++) {
				OS.SHSetAppKeyWndAssoc ((byte) bVk, hwnd);
			}
		}
		/* Restore SIP state when window is activated */
		if (OS.LOWORD (wParam) != 0) {
			OS.SHSipPreference (handle, psai.fSipUp == 0 ? OS.SIP_DOWN : OS.SIP_UP);
		} 
	}

	/*
	* Bug in Windows XP.  When a Shell is deactivated, the
	* IME composition window does not go away. This causes
	* repaint issues.  The fix is to commit the composition
	* string.
	*/
	if (OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		if (OS.LOWORD (wParam) == 0 && OS.IsDBLocale && hIMC != 0) {
			if (OS.ImmGetOpenStatus (hIMC)) {
				OS.ImmNotifyIME (hIMC, OS.NI_COMPOSITIONSTR, OS.CPS_COMPLETE, 0);
			}
		}
	}
	
	/* Process WM_ACTIVATE */
	LRESULT result = super.WM_ACTIVATE (wParam, lParam);
	if (OS.LOWORD (wParam) == 0) {
		if (lParam == 0 || (lParam != toolTipHandle && lParam != balloonTipHandle)) {
			ToolTip tip = getCurrentToolTip ();
			if (tip != null) tip.setVisible (false);
		}
	}
	return parent != null ? LRESULT.ZERO : result;
}

LRESULT WM_COMMAND (int /*long*/ wParam, int /*long*/ lParam) {
	if (OS.IsPPC) {
		/*
		* Note in WinCE PPC:  Close the Shell when the "Done Button" has
		* been pressed. lParam is either 0 (PocketPC 2002) or the handle
		* to the Shell (PocketPC).
		*/
		int loWord = OS.LOWORD (wParam);
		if (loWord == OS.IDOK && (lParam == 0 || lParam == handle)) {
			OS.PostMessage (handle, OS.WM_CLOSE, 0, 0);
			return LRESULT.ZERO;			
		}
	}
	/*
	* Feature in Windows.  On PPC, the menu is not actually an HMENU.
	* By observation, it is a tool bar that is configured to look like
	* a menu.  Therefore, when the PPC menu sends WM_COMMAND messages,
	* lParam is not zero because the WM_COMMAND was not sent from a menu.
	* Sub menu item events originate from the menu bar.  Top menu items
	* events originate from a tool bar.  The fix is to detect the source
	* of the WM_COMMAND and set lParam to zero to pretend that the message
	* came from a real Windows menu, not a tool bar.
	*/
	if (OS.IsPPC || OS.IsSP) {
		if (menuBar != null) {
			int /*long*/ hwndCB = menuBar.hwndCB;
			if (lParam != 0 && hwndCB != 0) {
				if (lParam == hwndCB) {
					return super.WM_COMMAND (wParam, 0);
				} else {
					int /*long*/ hwndChild = OS.GetWindow (hwndCB, OS.GW_CHILD);
					if (lParam == hwndChild) return super.WM_COMMAND (wParam, 0);					
				}
			}
		}
	}
	return super.WM_COMMAND (wParam, lParam);
}

LRESULT WM_DESTROY (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_DESTROY (wParam, lParam);
	/*
	* When the shell is a WS_CHILD window of a non-SWT
	* window, the destroy code does not get called because
	* the non-SWT window does not call dispose ().  Instead,
	* the destroy code is called here in WM_DESTROY.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.WS_CHILD) != 0) {
		releaseParent ();
		release (false);
	}
	return result;
}

LRESULT WM_ERASEBKGND (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  When a shell is resized by dragging
	* the resize handles, Windows temporarily fills in black
	* rectangles where the new contents of the shell should
	* draw.  The fix is to always draw the background of shells.
	* 
	* NOTE: This only happens on Vista.
	*/
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		drawBackground (wParam);
		return LRESULT.ONE;
	}
	return result;
}

LRESULT WM_ENTERIDLE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_ENTERIDLE (wParam, lParam);
	if (result != null) return result;
	Display display = this.display;
	if (display.runMessages) {
		if (display.runAsyncMessages (false)) display.wakeThread ();
	}
	return result;
}

LRESULT WM_GETMINMAXINFO (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_GETMINMAXINFO (wParam, lParam);
	if (result != null) return result;
	if (minWidth != SWT.DEFAULT || minHeight != SWT.DEFAULT) {
		MINMAXINFO info = new MINMAXINFO ();
		OS.MoveMemory (info, lParam, MINMAXINFO.sizeof);
		if (minWidth != SWT.DEFAULT) info.ptMinTrackSize_x = minWidth;
		if (minHeight != SWT.DEFAULT) info.ptMinTrackSize_y = minHeight;
		OS.MoveMemory (lParam, info, MINMAXINFO.sizeof);
		return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_MOUSEACTIVATE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_MOUSEACTIVATE (wParam, lParam);
	if (result != null) return result;
	
	/*
	* Check for WM_MOUSEACTIVATE when an MDI shell is active
	* and stop the normal shell activation but allow the mouse
	* down to be delivered.
	*/
	int hittest = (short) OS.LOWORD (lParam);
	switch (hittest) {
		case OS.HTERROR:
		case OS.HTTRANSPARENT:
		case OS.HTNOWHERE:
			break;
		default: {
			Control control = display._getFocusControl ();
			if (control != null) {
				Decorations decorations = control.menuShell ();
				if (decorations.getShell () == this && decorations != this) {
					display.ignoreRestoreFocus = true;
					display.lastHittest = hittest;
					display.lastHittestControl = null;
					if (hittest == OS.HTMENU || hittest == OS.HTSYSMENU) {
						display.lastHittestControl = control;
						return null;
					}
					if (OS.IsWin95 && hittest == OS.HTCAPTION) {
						display.lastHittestControl = control;
					}
					return new LRESULT (OS.MA_NOACTIVATE);
				}
			}
		}
	}
	if (hittest == OS.HTMENU) return null;
	
	/*
	* Get the current location of the cursor,
	* not the location of the cursor when the
	* WM_MOUSEACTIVATE was generated.  This is
	* strictly incorrect but is necessary in
	* order to support Activate and Deactivate
	* events for embedded widgets that have
	* their own event loop.  In that case, the
	* cursor location reported by GetMessagePos()
	* is the one for our event loop, not the
	* embedded widget's event loop.
	*/
	POINT pt = new POINT ();
	if (!OS.GetCursorPos (pt)) {
		int pos = OS.GetMessagePos ();
		OS.POINTSTOPOINT (pt, pos);
	}
	int /*long*/ hwnd = OS.WindowFromPoint (pt);
	if (hwnd == 0) return null;
	Control control = display.findControl (hwnd);
	
	/*
	* When a shell is created with SWT.ON_TOP and SWT.NO_FOCUS,
	* do not activate the shell when the user clicks on the
	* the client area or on the border or a control within the
	* shell that does not take focus.
	*/
	if (control != null && (control.state & CANVAS) != 0) {
		if ((control.style & SWT.NO_FOCUS) != 0) {
			int bits = SWT.ON_TOP | SWT.NO_FOCUS;
			if ((style & bits) == bits) {
				if (hittest == OS.HTBORDER || hittest == OS.HTCLIENT) {
					return new LRESULT (OS.MA_NOACTIVATE);
				}
			}
		}
	}
	
	int /*long*/ code = callWindowProc (handle, OS.WM_MOUSEACTIVATE, wParam, lParam);
	setActiveControl (control);
	return new LRESULT (code);
}

LRESULT WM_MOVE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_MOVE (wParam, lParam);
	if (result != null) return result;
	ToolTip tip = getCurrentToolTip ();
	if (tip != null) tip.setVisible (false);
	return result;
}

LRESULT WM_NCHITTEST (int /*long*/ wParam, int /*long*/ lParam) {
	if (!OS.IsWindowEnabled (handle)) return null;
	if (!isEnabled () || !isActive ()) {
		if (!Display.TrimEnabled) return new LRESULT (OS.HTNOWHERE);
		int /*long*/ hittest = callWindowProc (handle, OS.WM_NCHITTEST, wParam, lParam);
		if (hittest == OS.HTCLIENT || hittest == OS.HTMENU) hittest = OS.HTBORDER;
		return new LRESULT (hittest);
	}
	if (menuBar != null && !menuBar.getEnabled ()) {
		int /*long*/ hittest = callWindowProc (handle, OS.WM_NCHITTEST, wParam, lParam);
		if (hittest == OS.HTMENU) hittest = OS.HTBORDER;
		return new LRESULT (hittest);
	}
	return null;
}

LRESULT WM_NCLBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_NCLBUTTONDOWN (wParam, lParam);
	if (result != null) return result;
	/*
	* When the normal activation was interrupted in WM_MOUSEACTIVATE
	* because the active shell was an MDI shell, set the active window
	* to the top level shell but lock the active window and stop focus
	* changes.  This allows the user to interact the top level shell
	* in the normal manner.
	*/
	if (!display.ignoreRestoreFocus) return result;
	Display display = this.display;
	int /*long*/ hwndActive = 0;
	boolean fixActive = OS.IsWin95 && display.lastHittest == OS.HTCAPTION;
	if (fixActive) hwndActive = OS.SetActiveWindow (handle);
	display.lockActiveWindow = true;
	int /*long*/ code = callWindowProc (handle, OS.WM_NCLBUTTONDOWN, wParam, lParam);
	display.lockActiveWindow = false;
	if (fixActive) OS.SetActiveWindow (hwndActive);
	Control focusControl = display.lastHittestControl;
	if (focusControl != null && !focusControl.isDisposed ()) {
		focusControl.setFocus ();
	}
	display.lastHittestControl = null;
	display.ignoreRestoreFocus = false;
	return new LRESULT (code);
}

LRESULT WM_PALETTECHANGED (int /*long*/ wParam, int /*long*/ lParam) {
	if (wParam != handle) {
		int /*long*/ hPalette = display.hPalette;
		if (hPalette != 0) return selectPalette (hPalette);
	}
	return super.WM_PALETTECHANGED (wParam, lParam);
}

LRESULT WM_QUERYNEWPALETTE (int /*long*/ wParam, int /*long*/ lParam) {
	int /*long*/ hPalette = display.hPalette;
	if (hPalette != 0) return selectPalette (hPalette);
	return super.WM_QUERYNEWPALETTE (wParam, lParam);
}

LRESULT WM_SETCURSOR (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Feature in Windows.  When the shell is disabled
	* by a Windows standard dialog (like a MessageBox
	* or FileDialog), clicking in the shell does not
	* bring the shell or the dialog to the front. The
	* fix is to detect this case and bring the shell
	* forward.
	*/
	int msg = OS.HIWORD (lParam);
	if (msg == OS.WM_LBUTTONDOWN) {
		if (!Display.TrimEnabled) {
			Shell modalShell = display.getModalShell ();
			if (modalShell != null && !isActive ()) {
				int /*long*/ hwndModal = modalShell.handle;
				if (OS.IsWindowEnabled (hwndModal)) {
					OS.SetActiveWindow (hwndModal);
				}
			}
		}
		if (!OS.IsWindowEnabled (handle)) {
			if (!OS.IsWinCE) {
				int /*long*/ hwndPopup = OS.GetLastActivePopup (handle);
				if (hwndPopup != 0 && hwndPopup != handle) {
					if (display.getControl (hwndPopup) == null) {
						if (OS.IsWindowEnabled (hwndPopup)) {
							OS.SetActiveWindow (hwndPopup);
						}
					}
				}
			}
		}
	}
	/*
	* When the shell that contains a cursor is disabled,
	* WM_SETCURSOR is called with HTERROR.  Normally,
	* when a control is disabled, the parent will get
	* mouse and cursor events.  In the case of a disabled
	* shell, there is no enabled parent.  In order to
	* show the cursor when a shell is disabled, it is
	* necessary to override WM_SETCURSOR when called
	* with HTERROR to set the cursor but only when the
	* mouse is in the client area of the shell.
	*/
	int hitTest = (short) OS.LOWORD (lParam);
	if (hitTest == OS.HTERROR) {
		if (!getEnabled ()) {
			Control control = display.getControl (wParam);
			if (control == this && cursor != null) {
				POINT pt = new POINT ();
				int pos = OS.GetMessagePos ();
				OS.POINTSTOPOINT (pt, pos);
				OS.ScreenToClient (handle, pt);
				RECT rect = new RECT ();
				OS.GetClientRect (handle, rect);
				if (OS.PtInRect (rect, pt)) {
					OS.SetCursor (cursor.handle);
					switch (msg) {
						case OS.WM_LBUTTONDOWN:
						case OS.WM_RBUTTONDOWN:
						case OS.WM_MBUTTONDOWN:
						case OS.WM_XBUTTONDOWN:
							OS.MessageBeep (OS.MB_OK);
					}
					return LRESULT.ONE;
				}
			}
		}
	}
	return super.WM_SETCURSOR (wParam, lParam);
}

LRESULT WM_SETTINGCHANGE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_SETTINGCHANGE (wParam, lParam);
	if (result != null) return result;
	if (OS.IsPPC) {
		if (wParam == OS.SPI_SETSIPINFO) {
			/* 
			* The SIP is in a new state.  Cache its new value.
			* Resize the Shell if it has the style SWT.RESIZE.
			* Note that SHHandleWMSettingChange resizes the
			* Shell and also updates the cached state.
			*/
			if ((style & SWT.RESIZE) != 0) {
				OS.SHHandleWMSettingChange (handle, wParam, lParam, psai);
				return LRESULT.ZERO;
			} else {
				SIPINFO pSipInfo = new SIPINFO ();
				pSipInfo.cbSize = SIPINFO.sizeof;
				OS.SipGetInfo (pSipInfo);
				psai.fSipUp = pSipInfo.fdwFlags & OS.SIPF_ON;					
			}
		}
	}
	return result;
}

LRESULT WM_SHOWWINDOW (int /*long*/ wParam, int /*long*/ lParam) {
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

LRESULT WM_SYSCOMMAND (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_SYSCOMMAND (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  When the last visible window in
	* a process minimized, Windows swaps out the memory for
	* the process.  The assumption is that the user can no
	* longer interact with the window, so the memory can be
	* released to other applications.  However, for programs
	* that use a lot of memory, swapping the memory back in
	* can take a long time, sometimes minutes.  The fix is
	* to intercept WM_SYSCOMMAND looking for SC_MINIMIZE
	* and use ShowWindow() with SW_SHOWMINIMIZED to minimize
	* the window, rather than running the default window proc.
	* 
	* NOTE:  The default window proc activates the next
	* top-level window in the Z-order while ShowWindow()
	* with SW_SHOWMINIMIZED does not.  There is no fix for
	* this at this time.
	*/
	if (OS.IsWinNT) {
		int cmd = (int)/*64*/wParam & 0xFFF0;
		switch (cmd) {
			case OS.SC_MINIMIZE:
				long memory = Runtime.getRuntime ().totalMemory ();
				if (memory >= 32 * 1024 * 1024) {
					OS.ShowWindow (handle, OS.SW_SHOWMINIMIZED);
					return LRESULT.ZERO;
				}
		}
	}
	return result;
}

LRESULT WM_WINDOWPOSCHANGING (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_WINDOWPOSCHANGING (wParam,lParam);
	if (result != null) return result;
	WINDOWPOS lpwp = new WINDOWPOS ();
	OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
	if ((lpwp.flags & OS.SWP_NOSIZE) == 0) {
		lpwp.cx = Math.max (lpwp.cx, minWidth);
		int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
		if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
			lpwp.cx = Math.max (lpwp.cx, OS.GetSystemMetrics (OS.SM_CXMINTRACK));
		}
		lpwp.cy = Math.max (lpwp.cy, minHeight);
		if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
			if ((style & SWT.RESIZE) != 0) {
				lpwp.cy = Math.max (lpwp.cy, OS.GetSystemMetrics (OS.SM_CYMINTRACK));
			} else {
				RECT rect = new RECT ();
				int bits1 = OS.GetWindowLong (handle, OS.GWL_STYLE);
				int bits2 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
				OS.AdjustWindowRectEx (rect, bits1, false, bits2);
				lpwp.cy = Math.max (lpwp.cy, rect.bottom - rect.top);
			}
		}
		OS.MoveMemory (lParam, lpwp, WINDOWPOS.sizeof);
	}
	return result;
}
}

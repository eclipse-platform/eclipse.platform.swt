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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

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
	NSWindow window;
	SWTWindowDelegate windowDelegate;
	long /*int*/ hostWindowClass;
	NSWindow hostWindow;
	long /*int*/ tooltipOwner, tooltipTag, tooltipUserData;
	int glContextCount;
	boolean opened, moved, resized, fullScreen, center, deferFlushing, scrolling, isPopup;
	Control lastActive;
	Rectangle normalBounds;
	boolean keyInputHappened;
	NSRect currentFrame;
	NSRect fullScreenFrame;
	ToolBar toolBar;
	Map windowEmbedCounts;
	
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

Shell (Display display, Shell parent, int style, long /*int*/handle, boolean embedded) {
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
	if (!Display.getSheetEnabled ()) {
		this.center = parent != null && (style & SWT.SHEET) != 0;
	}
	this.style = checkStyle (parent, style);
	this.parent = parent;
	this.display = display;
	if (handle != 0) {
		if (embedded) {
			view = new NSView(handle);
		} else {
			window = new NSWindow(handle);
			state |= FOREIGN_HANDLE;
		}
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
 * @noreference This method is not intended to be referenced by clients.
 * 
 * @since 3.3
 */
public static Shell internal_new (Display display, long /*int*/ handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle, false);
}

/**	 
 * Invokes platform specific functionality to allocate a new shell
 * that is 'embedded'.  In this case, the handle represents an NSView
 * that acts as an embedded SWT Shell in an AWT Canvas.
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
 * @since 3.5
 */
public static Shell cocoa_new (Display display, long /*int*/ handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle, true);
}

static int checkStyle (Shell parent, int style) {
	style = Decorations.checkStyle (style);
	style &= ~SWT.TRANSPARENT;
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	if ((style & SWT.SHEET) != 0) {
		if (Display.getSheetEnabled ()) {
			style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX);
			if (parent == null) {
				style &= ~SWT.SHEET;
				style |= SWT.SHELL_TRIM;
			}
		} else {
			style &= ~SWT.SHEET;
			style |= parent == null ? SWT.SHELL_TRIM : SWT.DIALOG_TRIM;
		}
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

boolean accessibilityIsIgnored(long /*int*/ id, long /*int*/ sel) {
	// The content view of a shell is always ignored.
	if (id == view.id) return true;
	return super.accessibilityIsIgnored(id, sel);
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

void attachObserversToWindow(NSWindow newWindow) {
	if (newWindow == null || newWindow.id == 0) return;
	long /*int*/ newHostWindowClass = OS.object_getClass(newWindow.id);
	long /*int*/ sendEventImpl = OS.class_getMethodImplementation(newHostWindowClass, OS.sel_sendEvent_);
	if (sendEventImpl == Display.windowCallback3.getAddress()) return;
	hostWindow = newWindow;
	hostWindow.retain();
	long /*int*/ embeddedSubclass = display.createWindowSubclass(newHostWindowClass, "SWTAWTWindow", true);
	OS.object_setClass(hostWindow.id, embeddedSubclass);
	display.addWidget (hostWindow, this);
	hostWindowClass = newHostWindowClass;
	
	if (windowEmbedCounts == null) windowEmbedCounts = new HashMap();
	Integer embedCount = (Integer) windowEmbedCounts.get(hostWindow);
	if (embedCount == null) {
		embedCount = new Integer(0);
	}
	embedCount = new Integer(embedCount.intValue() + 1);
	windowEmbedCounts.put(hostWindow, embedCount);
	
	// Register for notifications. An embedded shell has no control over the host window,
	// so it isn't correct to install a delegate.
	NSNotificationCenter defaultCenter = NSNotificationCenter.defaultCenter();
	defaultCenter.addObserver(windowDelegate, OS.sel_windowDidBecomeKey_, OS.NSWindowDidBecomeKeyNotification, hostWindow);
	defaultCenter.addObserver(windowDelegate, OS.sel_windowDidDeminiaturize_, OS.NSWindowDidDeminiaturizeNotification, hostWindow);
	defaultCenter.addObserver(windowDelegate, OS.sel_windowDidMiniaturize_, OS.NSWindowDidMiniaturizeNotification, hostWindow);
	defaultCenter.addObserver(windowDelegate, OS.sel_windowDidMove_, OS.NSWindowDidMoveNotification, hostWindow);
	defaultCenter.addObserver(windowDelegate, OS.sel_windowDidResize_, OS.NSWindowDidResizeNotification, hostWindow);
	defaultCenter.addObserver(windowDelegate, OS.sel_windowDidResignKey_, OS.NSWindowDidResignKeyNotification, hostWindow);
	defaultCenter.addObserver(windowDelegate, OS.sel_windowWillClose_, OS.NSWindowWillCloseNotification, hostWindow);
}

void becomeKeyWindow (long /*int*/ id, long /*int*/ sel) {
	Shell modal = getModalShell();
	if (modal != null && modal.window != null) {
		modal.window.makeKeyAndOrderFront(null);
		return;
	}
	Display display = this.display;
	display.keyWindow = view.window();
	super.becomeKeyWindow(id, sel);
	display.checkFocus();
	display.keyWindow = null;
}

void bringToTop (boolean force) {
	if (getMinimized ()) return;
	if (force) {
		forceActive ();
	} else {
		setActive ();
	}
}

boolean canBecomeKeyWindow (long /*int*/ id, long /*int*/ sel) {
	if (isPopup) return false;
	// Only answer if SWT created the window.
	if (window != null) {
		if ((style & SWT.NO_FOCUS) != 0) {
			NSEvent nsEvent = NSApplication.sharedApplication().currentEvent();
			if (nsEvent != null && nsEvent.type() == OS.NSLeftMouseDown) {
				NSView contentView = window.contentView();
				if (contentView != null) {
					NSView view = contentView.hitTest(nsEvent.locationInWindow());
					if (view == contentView) return false;
				}
			}
		}
		long /*int*/ styleMask = window.styleMask();
		if (styleMask == OS.NSBorderlessWindowMask || (styleMask & (OS.NSNonactivatingPanelMask | OS.NSDocModalWindowMask | OS.NSResizableWindowMask)) != 0) return true;
	}
	return super.canBecomeKeyWindow (id, sel);
}

void checkOpen () {
	if (!opened) resized = false;
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

void clearDeferFlushing (long /*int*/ id, long /*int*/ sel) {
	deferFlushing = false;
	scrolling = false;
	if (window != null) window.flushWindowIfNeeded();
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
	closeWidget (false);
}

void closeWidget (boolean force) {
	if (display.isDisposed()) return;
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if ((force || event.doit) && !isDisposed ()) dispose ();
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	Point size = super.computeSize (wHint, hHint, changed);
	if (toolBar != null) {
		if (wHint == SWT.DEFAULT && toolBar.itemCount > 0) {
			Point tbSize = toolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);
			size.x = Math.max (tbSize.x, size.x);
		}
	}
	return size;
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrim(x, y, width, height);
	NSRect rect = new NSRect ();
	rect.x = trim.x;
	rect.y = trim.y;
	rect.width = trim.width;
	rect.height = trim.height;
	if (window != null) {
		if (!_getFullScreen() && !fixResize()) {
			double /*float*/ h = rect.height;
			rect = window.frameRectForContentRect(rect);
			rect.y += h-rect.height;
		}
	}
	return new Rectangle ((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
}

void createHandle () {
	state |= HIDDEN;
	if (window == null && view == null) {
		int styleMask = OS.NSBorderlessWindowMask;
		if ((style & (SWT.TOOL | SWT.SHEET)) != 0) {
			window = (NSWindow) new SWTPanel().alloc();
			if ((style & SWT.SHEET) != 0) {
				styleMask |= OS.NSDocModalWindowMask;
			} else {
				styleMask |= OS.NSUtilityWindowMask | OS.NSNonactivatingPanelMask;
			}
 		} else {
 			window = (NSWindow) new SWTWindow().alloc ();
 		}
		if ((style & SWT.NO_TRIM) == 0) {
			if ((style & SWT.TITLE) != 0) styleMask |= OS.NSTitledWindowMask;
			if ((style & SWT.CLOSE) != 0) styleMask |= OS.NSClosableWindowMask;
			if ((style & SWT.MIN) != 0) styleMask |= OS.NSMiniaturizableWindowMask;
			if ((style & SWT.MAX) != 0) styleMask |= OS.NSResizableWindowMask;
			if ((style & SWT.RESIZE) != 0) styleMask |= OS.NSResizableWindowMask;
		}
		NSScreen screen = null;
		NSScreen primaryScreen = new NSScreen(NSScreen.screens().objectAtIndex(0));
		if (parent != null) screen = parentWindow ().screen();
		if (screen == null) screen = primaryScreen;
		window = window.initWithContentRect(new NSRect(), styleMask, OS.NSBackingStoreBuffered, (style & SWT.ON_TOP) != 0, screen);
		if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0 || (style & (SWT.TOOL | SWT.SHEET)) != 0) {
			window.setHasShadow (true);
		}
		if ((style & SWT.TOOL) != 0) {
			// Feature in Cocoa: NSPanels that use NSUtilityWindowMask are always promoted to the floating window layer.
			// Fix is to call setFloatingPanel:NO, which turns off this behavior.
			((NSPanel)window).setFloatingPanel(false);
			// By default, panels hide on deactivation. 
			((NSPanel)window).setHidesOnDeactivate(false);
			// Normally a panel doesn't become key unless something inside it needs to be first responder.
			// TOOL shells always become key, so disable that behavior.
			((NSPanel)window).setBecomesKeyOnlyIfNeeded(false);
		}
		window.setReleasedWhenClosed(true);
		if ((style & SWT.NO_TRIM) == 0) {
			NSSize size = window.minSize();
			size.width = NSWindow.minFrameWidthWithTitle(NSString.string(), styleMask);
			window.setMinSize(size);
		}
		if (fixResize ()) {
			if (window.respondsToSelector(OS.sel_setMovable_)) {
				OS.objc_msgSend(window.id, OS.sel_setMovable_, 0);
			}
		}
		display.cascadeWindow(window, screen);
		NSRect screenFrame = screen.frame();
		double /*float*/ width = screenFrame.width * 5 / 8, height = screenFrame.height * 5 / 8;;
		NSRect frame = window.frame();
		NSRect primaryFrame = primaryScreen.frame();
		frame.y = primaryFrame.height - ((primaryFrame.height - (frame.y + frame.height)) + height);
		frame.width = width;
		frame.height = height;
		window.setFrame(frame, false);
		if ((style & SWT.ON_TOP) != 0) {
			window.setLevel(OS.NSStatusWindowLevel);
		}
		super.createHandle ();
		topView ().setHidden (true);
	} else {
		state &= ~HIDDEN;
		
		if (window != null) {
			// In the FOREIGN_HANDLE case, 'window' is an NSWindow created on our behalf.
			// It may already have a content view, so if it does, grab and retain, since we release()
			// the view at disposal time.  Otherwise, create a new 'view' that will be used as the window's
			// content view in setZOrder.
			view = window.contentView();
			
			if (view == null) {
				super.createHandle();
			} else {
				view.retain();
			}
		} else {
			// In the embedded case, 'view' is already set to the NSView we should add the window's content view to as a subview.
			// In that case we will hold on to the foreign view, create our own SWTCanvasView (which overwrites 'view') and then
			// add it to the foreign view.
			NSView parentView = view;			
			super.createHandle();
			parentView.addSubview(topView());
		}

		style |= SWT.NO_BACKGROUND;
	}
	
	windowDelegate = (SWTWindowDelegate)new SWTWindowDelegate().alloc().init();

	if (window == null) {
		NSWindow hostWindow = view.window();
		attachObserversToWindow(hostWindow);
	} else {
		int behavior = 0;
		if (parent != null) behavior |= OS.NSWindowCollectionBehaviorMoveToActiveSpace;
		if (OS.VERSION >= 0x1070) {
			if (parent == null) {
				behavior = OS.NSWindowCollectionBehaviorFullScreenPrimary;
			}
		}
		if (behavior != 0) window.setCollectionBehavior(behavior);
		window.setAcceptsMouseMovedEvents(true);
		window.setDelegate(windowDelegate);
	}

	if (OS.VERSION < 0x1060) {
		// Force a WindowRef to be created for this window so we can use
		// FindWindow() (see Display.findControl())
		if (window != null) window.windowRef();
	}
	
	NSWindow fieldEditorWindow = window;
	if (fieldEditorWindow == null) fieldEditorWindow = view.window();
	id id = fieldEditorWindow.fieldEditor (true, null);
	if (id != null) {
		OS.object_setClass (id.id, OS.objc_getClass ("SWTEditorView"));
		new NSTextView(id).setAllowsUndo(true);
	}

}

void deferFlushing () {
	deferFlushing = true;
	view.performSelector(OS.sel_clearDeferFlushing, null, 0.0, display.runLoopModes());
}

void deregister () {
	super.deregister ();
	if (window != null) display.removeWidget (window);
	if (windowDelegate != null) display.removeWidget (windowDelegate);
}

void destroyWidget () {
	NSWindow window = this.window;
	if (window != null) window.retain();
	Display display = this.display;
	NSView view = topView();
	if (view != null) view.retain();
	
	boolean sheet = (style & (SWT.SHEET)) != 0;
	releaseHandle ();
	if (window != null) {
		if (sheet) {
			NSApplication application = NSApplication.sharedApplication();
			application.endSheet(window, 0);
		}
		window.close();
	} else if (view != null) {
		view.removeFromSuperview();
	}
	if (view != null) view.release();
	
	// If another shell is not going to become active, clear the menu bar.
	// Don't modify the menu bar if we are an embedded Shell, though.
	if (window != null) {
		if (!display.isDisposed () && display.getShells ().length == 0) {
			display.setMenuBar (null);
		}
		window.release();
	}
}

void drawBackground (long /*int*/ id, NSGraphicsContext context, NSRect rect) {
	if (id != view.id) return;
	if (regionPath != null && background == null) {
		context.saveGraphicsState();
		NSColor.windowBackgroundColor().setFill();
		NSBezierPath.fillRect(rect);
		context.restoreGraphicsState();
		return;
	}
	super.drawBackground (id, context, rect);
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

boolean fixResize () {
	/*
	* Feature in Cocoa.  It is not possible to have a resizable window
	* without the title bar.  The fix is to resize the content view on
	* top of the title bar.
	* 
	* Never do this when the shell is embedded, because the window belongs to the AWT.
	*/
	if (window == null) return false;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.RESIZE) != 0 && (style & (SWT.SHEET | SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX)) == 0) {
			return true;
		}
	}
	return false;
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
	if (!isVisible()) return;
	if (window == null) return;
	makeKeyAndOrderFront ();
	NSApplication application = NSApplication.sharedApplication ();
	application.activateIgnoringOtherApps (true);
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
	if (window == null) return 255;
	return (int)(window.alphaValue() * 255);
}

public Rectangle getBounds () {
	checkWidget();
	if (window != null) {
		NSRect frame = window.frame();
		double /*float*/ y = display.getPrimaryFrame().height - (int)(frame.y + frame.height);
		Rectangle rectangle = new Rectangle ((int)frame.x, (int)y, (int)frame.width, (int)frame.height);
		double /*float*/ scaleFactor = view.window().userSpaceScaleFactor();
		rectangle.x /= scaleFactor;
		rectangle.y /= scaleFactor;
		rectangle.width /= scaleFactor;
		rectangle.height /= scaleFactor;
		return rectangle;
	} else {
		NSRect frame = view.frame();
		// Start from view's origin, (0, 0)
		NSPoint pt = new NSPoint();
		NSRect primaryFrame = display.getPrimaryFrame();
		if (!view.isFlipped ()) {
			pt.y = view.bounds().height - pt.y;
		}
		pt = view.convertPoint_toView_(pt, null);
		pt = view.window().convertBaseToScreen(pt);
		pt.y = primaryFrame.height - pt.y;
		return new Rectangle((int)pt.x, (int)pt.y, (int)frame.width, (int)frame.height);
	}
}

public Rectangle getClientArea () {
	checkWidget();
	NSRect rect;
	if (window != null) {
		if (!fixResize ()) {
			rect = window.contentView().frame();
		} else {
			double /*float*/ scaleFactor = window.userSpaceScaleFactor();
			rect = window.frame();
			rect.width /= scaleFactor;
			rect.height /= scaleFactor;
		}
	} else {
		rect = topView().frame();
	}
	int width = (int)rect.width, height = (int)rect.height;
	if (scrollView != null) {
		NSSize size = new NSSize();
		size.width = width;
		size.height = height;
		size = NSScrollView.contentSizeForFrameSize(size, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0, OS.NSNoBorder);
		width = (int)size.width;
		height = (int)size.height;
	}
	return new Rectangle (0, 0, width, height);
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
	return _getFullScreen ();
}

boolean _getFullScreen () {
	if ((window.collectionBehavior() & OS.NSWindowCollectionBehaviorFullScreenPrimary) != 0) {
		return (window.styleMask() & OS.NSFullScreenWindowMask) != 0 ? true : false;
	}
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
	
	if (window != null) {
		NSRect frame = window.frame();
		double /*float*/ y = display.getPrimaryFrame().height - (int)(frame.y + frame.height);
		Point point = new Point ((int)frame.x, (int)y);
		double /*float*/ scaleFactor = view.window().userSpaceScaleFactor();
		point.x /= scaleFactor;
		point.y /= scaleFactor;
		return point;
	} else {
		// Start from view's origin, (0, 0)
		NSPoint pt = new NSPoint();
		NSRect primaryFrame = display.getPrimaryFrame();
		if (!view.isFlipped ()) {
			pt.y = view.bounds().height - pt.y;
		}
		pt = view.convertPoint_toView_(pt, null);
		pt = view.window().convertBaseToScreen(pt);
		pt.y = primaryFrame.height - pt.y;
		double /*float*/ scaleFactor = view.window().userSpaceScaleFactor();
		pt.x /= scaleFactor;
		pt.y /= scaleFactor;
		return new Point((int)pt.x, (int)pt.y);
	}
}

public boolean getMaximized () {
	checkWidget();
	if (window == null) return false;
	return !_getFullScreen() && window.isZoomed();
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
	return window.isDocumentEdited ();
}

public boolean getMinimized () {
	checkWidget();
	if (!getVisible ()) return super.getMinimized ();
	if (window == null) return false;
	return window.isMiniaturized();
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
	if (window == null) return new Point(0, 0);
	NSSize size = window.minSize();
	return new Point((int)size.width, (int)size.height);
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
	NSRect frame = (window != null ? window.frame() : view.frame());
	Point point = new Point ((int) frame.width, (int) frame.height);
	double /*float*/ scaleFactor = view.window().userSpaceScaleFactor();
	point.x /= scaleFactor;
	point.y /= scaleFactor;
	return point;
}

float getThemeAlpha () {
	return 1;
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
	checkWidget();
	if ((style & SWT.NO_TRIM) == 0) {
		if (toolBar == null) toolBar = new ToolBar(this, SWT.HORIZONTAL | SWT.SMOOTH, true);
	}
	return toolBar;
}

boolean hasBorder () {
	return false;
}

boolean hasRegion () {
	return region != null;
}

void helpRequested(long /*int*/ id, long /*int*/ sel, long /*int*/ theEvent) {
	Control control = display.getFocusControl();
	while (control != null) {
		if (control.hooks (SWT.Help)) {
			control.postEvent (SWT.Help);
			break;
		}
		control = control.parent;
	}
}

void invalidateVisibleRegion () {
	resetVisibleRegion ();
	if (toolBar != null) toolBar.resetVisibleRegion();
	invalidateChildrenVisibleRegion ();
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

boolean isResizing () {
	return (state & RESIZING) != 0;
}

boolean isTransparent() {
	return false;
}

public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

boolean makeFirstResponder (long /*int*/ id, long /*int*/ sel, long /*int*/ responder) {
	Display display = this.display;
	boolean result = super.makeFirstResponder(id, sel, responder);
	if (!display.isDisposed()) display.checkFocus();
	return result;
}

void makeKeyAndOrderFront() {
	/*
	* Bug in Cocoa.  If a child window becomes the key window when its
	* parent window is miniaturized, the parent window appears as if
	* restored to its full size without actually being restored. In this
	* case the parent window does become active when its child is closed
	* and the user is forced to restore the window from the dock.
	* The fix is to be sure that the parent window is deminiaturized before
	* making the child a key window. 
	*/
	if (parent != null) {
		NSWindow parentWindow = parentWindow ();
		if (parentWindow.isMiniaturized()) parentWindow.deminiaturize(null);
	}
	window.makeKeyAndOrderFront (null);
}

void mouseMoved(long /*int*/ id, long /*int*/ sel, long /*int*/ theEvent) {
	super.mouseMoved(id, sel, theEvent);

	/**
	 * Bug in AWT. WebViews need to have a mouseMove: handled by the window so it can generate
	 * DOMMouseMove events and also provide proper feedback to the window. However, the top-level
	 * view in an AWT window does not have the NSWindow as a next responder.
	 * 
	 * Fix is to forward the message to the window if this is an embedded shell (that is, window == null)
	 */
	if (id == view.id && window == null) {
		view.window().mouseMoved(new NSEvent(theEvent));		
	}
}

void noResponderFor(long /*int*/ id, long /*int*/ sel, long /*int*/ selector) {
	/**
	 * Feature in Cocoa.  If the selector is keyDown and nothing has handled the event
	 * a system beep is generated.  There's no need to beep, as many keystrokes in the SWT
	 * are listened for and acted upon but not explicitly handled in a keyDown handler.  Fix is to
	 * not call the default implementation when a keyDown: is being handled. 
	 */
	if (selector != OS.sel_keyDown_) super.noResponderFor(id, sel, selector);
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
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & mask) != 0) {
		display.setModalShell (this);
	} else {
		updateModal ();
	}
	bringToTop (false);
	setWindowVisible (true, true);
	if (isDisposed ()) return;
	if (!restoreFocus () && !traverseGroup (true)) {
		// if the parent shell is minimized, setting focus will cause it
		// to become unminimized.
		if (parent == null || !parentWindow ().isMiniaturized()) {
			setFocus ();
		}
	}
}

NSWindow parentWindow () {
	if (parent == null) return null;
	return parent.view.window();
}

public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

void register () {
	/*
	 * Note that if there are multiple SWT_AWT shells only the last one created
	 * will be associated with the NSWindow. This is okay, and intentional because 
	 * all of the NSWindow overrides operate on the entire window. 
	 */
	super.register ();
	if (window != null) display.addWidget (window, this);
	if (windowDelegate != null) display.addWidget (windowDelegate, this);
}

void releaseChildren (boolean destroy) {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (shell != null && !shell.isDisposed ()) {
			shell.dispose ();
		}
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	if (window != null) window.setDelegate(null);
	removeObserversFromWindow();
	if (windowDelegate != null) windowDelegate.release();
	windowDelegate = null;

	super.releaseHandle ();
	window = null;
}

void releaseParent () {
	/* Do nothing */
}

void releaseWidget () {
	super.releaseWidget ();
	if (toolBar != null) {
		toolBar.dispose();
		toolBar = null;
	}
	if (tooltipTag != 0) {
		view.window().contentView().removeToolTip(tooltipTag);
		tooltipTag = 0;
	}
	display.clearModal (this);
	updateParent (false);
	display.updateQuitMenu();
	lastActive = null;
}

void removeObserversFromWindow () {
	NSNotificationCenter.defaultCenter().removeObserver(windowDelegate);		

	if (hostWindow != null) {
		Integer embedCount = (Integer) windowEmbedCounts.get(hostWindow);
		if (embedCount == null) {
			embedCount = new Integer(0);
		}
		embedCount = new Integer(embedCount.intValue() - 1);
		windowEmbedCounts.put(hostWindow, embedCount);

		if (embedCount.intValue() <= 0) {
			windowEmbedCounts.remove(hostWindow);
			if (hostWindowClass != 0) OS.object_setClass(hostWindow.id, hostWindowClass);
			display.removeWidget(hostWindow);
			hostWindow.release();
			hostWindow = null;
			hostWindowClass = 0;
		}
	}
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
	if (toolBar != null) toolBar.reskin(flags);
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (shell != null) shell.reskin (flags);
	}
	super.reskinChildren (flags);
}

void sendToolTipEvent (boolean enter) {
	if (!isVisible()) return;
	NSWindow eventWindow = view.window();
	if (tooltipTag == 0) {
		NSView view = eventWindow.contentView();
		tooltipTag = view.addToolTipRect(new NSRect(), eventWindow, 0);
		if (tooltipTag != 0) {
			NSTrackingArea trackingArea = new NSTrackingArea(tooltipTag);
			id owner = trackingArea.owner();
			if (owner != null) tooltipOwner = owner.id;
			id userInfo = trackingArea.userInfo();
			if (userInfo != null) {
				tooltipUserData = userInfo.id;
			} else {
				long /*int*/ [] value = new long /*int*/ [1];
				OS.object_getInstanceVariable(tooltipTag, new byte[]{'_','u', 's', 'e', 'r', 'I', 'n', 'f', 'o'}, value);
				tooltipUserData = value[0];
			}
		}
	}
	if (tooltipTag == 0 || tooltipOwner == 0 || tooltipUserData == 0) return;
	NSPoint pt = eventWindow.convertScreenToBase(NSEvent.mouseLocation());
	NSEvent event = NSEvent.enterExitEventWithType(enter ? OS.NSMouseEntered : OS.NSMouseExited, pt, 0, 0, eventWindow.windowNumber(), null, 0, tooltipTag, tooltipUserData);
	OS.objc_msgSend(tooltipOwner, enter ? OS.sel_mouseEntered_ : OS.sel_mouseExited_, event.id);
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
	if (window == null) return;	
	checkWidget ();
	if (!isVisible()) return;
	makeKeyAndOrderFront ();
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
	if (window == null) return;	
	checkWidget ();
	alpha &= 0xFF;
	window.setAlphaValue (alpha / 255f);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	if (window == null) {
		// Embedded shells aren't movable.
		if (move) return;
		if (resize) {
			NSSize frameSize = new NSSize();
			frameSize.width = width;
			frameSize.height = height;
			view.setFrameSize(frameSize);
			return;
		}
	}
	if (_getFullScreen ()) setFullScreen (false);
	boolean sheet = window.isSheet();
	if (sheet && move && !resize) return;
	int screenHeight = (int) display.getPrimaryFrame().height;
	double /*float*/ scaleFactor = window.userSpaceScaleFactor();
	x *= scaleFactor;
	y *= scaleFactor;
	width *= scaleFactor;
	height *= scaleFactor;
	NSRect frame = window.frame();
	if (!move) {
		x = (int)frame.x;
		y = screenHeight - (int)(frame.y + frame.height);
	}
	if (resize) {
		NSSize minSize = window.minSize();
		width = Math.max(width, (int)minSize.width);
		height = Math.max(height, (int)minSize.height);
	} else {
		width = (int)frame.width;
		height = (int)frame.height;
	}
	if (sheet) {
		y = screenHeight - (int)(frame.y + frame.height);
		NSRect parentRect = parent.getShell().window.frame();
		frame.width = width;
		frame.height = height;
		frame.x = parentRect.x + (parentRect.width - frame.width) / 2;
		frame.y = screenHeight - (int)(y + frame.height);
		window.setFrame(frame, isVisible(), true);
	} else {
		frame.x = x;
		frame.y = screenHeight - (int)(y + height);
		frame.width = width;
		frame.height = height;
		window.setFrame(frame, isVisible());
	}
}

void setClipRegion (NSView view) {
	if (regionPath != null) {
		NSView rgnView = topView ();
		if (!rgnView.isFlipped()) rgnView = eventView ();
		NSPoint pt = view.convertPoint_toView_(new NSPoint(), rgnView);
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(-pt.x, -pt.y);
		regionPath.transformUsingAffineTransform(transform);
		regionPath.addClip();
		transform.translateXBy(2*pt.x, 2*pt.y);
		regionPath.transformUsingAffineTransform(transform);
	}
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (((state & DISABLED) == 0) == enabled) return;
	super.setEnabled (enabled);
	if (enabled && window != null && window.isMainWindow()) {
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
	if (window == null) return;
	if (_getFullScreen () == fullScreen) return;
	
	if ((window.collectionBehavior() & OS.NSWindowCollectionBehaviorFullScreenPrimary) != 0) {
		OS.objc_msgSend(window.id, OS.sel_toggleFullScreen_, 0);
		return;
	}

	this.fullScreen = fullScreen;
	if (fullScreen) {
		currentFrame = window.frame();
		window.setShowsResizeIndicator(false); //only hides resize indicator
		if (window.respondsToSelector(OS.sel_setMovable_)) {
			OS.objc_msgSend(window.id, OS.sel_setMovable_, 0);
		}
		
		fullScreenFrame = NSScreen.mainScreen().frame();
		if (getMonitor().equals(display.getPrimaryMonitor ())) {
			if (menuBar != null) {
				double /*float*/ menuBarHt = NSStatusBar.systemStatusBar().thickness();
				fullScreenFrame.height -= menuBarHt;
				OS.SetSystemUIMode(OS.kUIModeContentHidden, 0);
			} 
			else {
				OS.SetSystemUIMode(OS.kUIModeAllHidden, 0);
			}
		}
		window.setFrame(fullScreenFrame, true);
		NSRect contentViewFrame = new NSRect();
		contentViewFrame.width = fullScreenFrame.width;
		contentViewFrame.height = fullScreenFrame.height;
		window.contentView().setFrame(contentViewFrame);
	} else {
		window.setShowsResizeIndicator(true);
		if (window.respondsToSelector(OS.sel_setMovable_)) {
			OS.objc_msgSend(window.id, OS.sel_setMovable_, 1);
		}
		OS.SetSystemUIMode(OS.kUIModeNormal, 0);
		window.setFrame(currentFrame, true);
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
	if (window == null) return;
	if (window.isZoomed () == maximized) return;
	window.zoom (null);
}

public void setMinimized (boolean minimized) {
	checkWidget();
	super.setMinimized (minimized);
	if (window == null) return;
	if (!getVisible()) return;
	if (minimized) {
		window.miniaturize (null);
	} else {
		window.deminiaturize (null);
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
	checkWidget();
	if (window == null) return;
	NSSize size = new NSSize();
	size.width = width;
	size.height = height;
	window.setMinSize(size);
	NSRect frame = window.frame();
	if (width > frame.width || height > frame.height) {
		width = (int)(width > frame.width ? width : frame.width);
		height = (int)(height > frame.height ? height : frame.height);
		setBounds(0, 0, width, height, false, true);
	}
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
	window.setDocumentEdited (modified);
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
	if (window == null) return;
	if (region != null && region.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.region = region;
	if (regionPath != null) regionPath.release();
	regionPath = getPath(region);
	if (region != null) {
		window.setBackgroundColor(NSColor.clearColor());
	} else {
		window.setBackgroundColor(NSColor.windowBackgroundColor());
	}
	updateOpaque ();
	window.contentView().setNeedsDisplay(true);
	if (isVisible() && window.hasShadow()) {
		window.display();
		window.invalidateShadow();
	}
}

void setScrolling () {
	scrolling = true;
	view.performSelector(OS.sel_clearDeferFlushing, null, 0.0, display.runLoopModes());
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (window == null) return;
	super.setText (string);
	NSString str = NSString.stringWith(string);
	window.setTitle(str);
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
	if (window == null) {
		super.setVisible(visible);
	} else {
		setWindowVisible (visible, false);
	}
}

void setWindowVisible (boolean visible, boolean key) {
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	if (window != null && (window.isVisible() == visible)) return;
	if (visible) {
		display.clearPool ();
		if (center && !moved) {
			if (isDisposed ()) return;			
			center ();
		}
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		topView ().setHidden (false);
		invalidateVisibleRegion();
		if (window != null) {
			if ((style & (SWT.SHEET)) != 0) {
				NSApplication application = NSApplication.sharedApplication();
				application.beginSheet(window, parentWindow (), null, 0, 0);
				if (OS.VERSION <= 0x1060 && window.respondsToSelector(OS.sel__setNeedsToUseHeartBeatWindow_)) {
					OS.objc_msgSend(window.id, OS.sel__setNeedsToUseHeartBeatWindow_, 0);
				}
			} else {
				// If the parent window is miniaturized, the window will be shown
				// when its parent is shown.
				boolean parentMinimized = parent != null && parentWindow ().isMiniaturized();
				if (!parentMinimized) {
					if (key && (style & SWT.NO_FOCUS) == 0) {
						makeKeyAndOrderFront ();
					} else {
						window.orderFront (null);
					}
					if (isDisposed()) return;
					if (minimized != window.isMiniaturized()) {
						if (minimized) {
							window.miniaturize (null);
						} else {
							window.deminiaturize (null);
						}
					}
				}
			}
		}
		if (isDisposed()) return;
		updateParent (visible);
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
		updateParent (visible);
		if (window != null) {
			if ((style & (SWT.SHEET)) != 0) {
				NSApplication application = NSApplication.sharedApplication();
				application.endSheet(window, 0);
			} 
			window.orderOut (null);
		}
		if (isDisposed()) return;
		topView ().setHidden (true);
		invalidateVisibleRegion();
		sendEvent (SWT.Hide);
	}
	
	if (isDisposed()) return;
	display.updateQuitMenu();

	if (isDisposed()) return;
	NSView[] hitView = new NSView[1];
	Control control = display.findControl (false, hitView);
	if (control != null && (!control.isActive() || !control.isEnabled())) control = null;
	Control trimControl = control;
	if (trimControl != null && trimControl.isTrim (hitView[0])) trimControl = null;
	display.checkEnterExit (trimControl, null, false);
}

void setZOrder () {
	if (scrollView != null) scrollView.setDocumentView (view);
	if (window == null) return;
	window.setContentView (scrollView != null ? scrollView : view);
	if (fixResize ()) {
		NSRect rect = window.frame();
		rect.x = rect.y = 0;
		double /*float*/ scaleFactor = window.userSpaceScaleFactor();
		rect.width /= scaleFactor;
		rect.height /= scaleFactor;
		window.contentView().setFrame(rect);
	}
}

void setZOrder (Control control, boolean above) {
	if (window == null) return;
	if (!getVisible ()) return;
	if (control == null) {
		if (above) {
			window.orderFront(null);
		} else {
			window.orderBack(null);
		}
	} else {
		NSWindow otherWindow = control.getShell().window;
		window.orderWindow(above ? OS.NSWindowAbove : OS.NSWindowBelow, otherWindow.windowNumber());
	}
}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}

void updateCursorRects(boolean enabled) {
	super.updateCursorRects(enabled);
	if (toolBar != null) toolBar.updateCursorRects(enabled);
};

void updateModal () {
	// do nothing
}

void updateOpaque () {
	if (window == null) return;
	window.setOpaque (region == null && glContextCount == 0);
}

void updateParent (boolean visible) {
	if (window != null) {
		if (visible) {
			if (parent != null && parent.getVisible ()) {
				parentWindow ().addChildWindow (window, OS.NSWindowAbove);
				
				/**
				 * Feature in Cocoa: When a window is added as a child window,
				 * its window level resets to its parent's window level. So, we
				 * have to set the level for ON_TOP child window again.
				 */
				if ((style & SWT.ON_TOP) != 0) {
					window.setLevel(OS.NSStatusWindowLevel);
				}
			}
		} else {
			NSWindow parentWindow = window.parentWindow ();
			if (parentWindow != null) parentWindow.removeChildWindow (window);
		}
	}
	Shell [] shells = getShells ();
	for (int i = 0; i < shells.length; i++) {
		Shell shell = shells [i];
		if (shell.parent == this && shell.getVisible ()) {
			shell.updateParent (visible);
		}
	}
}

void updateSystemUIMode () {
	if ((window.collectionBehavior() & OS.NSWindowCollectionBehaviorFullScreenPrimary) != 0) return;
	if (!getMonitor ().equals (display.getPrimaryMonitor ())) return;
	int mode = display.systemUIMode, options = display.systemUIOptions;
	if (fullScreen) {
		mode = OS.kUIModeAllHidden;
		if (menuBar != null) {
			mode = OS.kUIModeContentHidden;
		}
		options = 0;
	}
	int[] uiMode = new int[1], uiOptions = new int[1];
	OS.GetSystemUIMode(uiMode, uiOptions);
	if (uiMode[0] != mode || uiOptions[0] != options) OS.SetSystemUIMode (mode, options);
	if (fullScreen)	window.setFrame(fullScreenFrame, true);
}

long /*int*/ view_stringForToolTip_point_userData (long /*int*/ id, long /*int*/ sel, long /*int*/ view, long /*int*/ tag, long /*int*/ point, long /*int*/ userData) {
	NSPoint pt = new NSPoint();
	OS.memmove (pt, point, NSPoint.sizeof);
	Control control = display.findControl (false);
	if (control == null) return 0;
	Widget target = control.findTooltip (new NSView (view).convertPoint_toView_ (pt, null));
	String string = target.tooltipText ();
	if (string == null) return 0;
	char[] chars = new char [string.length ()];
	string.getChars (0, chars.length, chars, 0);
	int length = fixMnemonic (chars);
	return NSString.stringWithCharacters (chars, length).id;
}

void viewWillMoveToWindow(long /*int*/ id, long /*int*/ sel, long /*int*/ newWindow) {
	if (window == null) {
		long /*int*/ currentWindow = hostWindow != null ? hostWindow.id : 0;
		if (currentWindow != 0) {
			removeObserversFromWindow();
		}
		if (newWindow != 0) {
			attachObserversToWindow(new NSWindow(newWindow));
		}
	}
}

void windowDidBecomeKey(long /*int*/ id, long /*int*/ sel, long /*int*/ notification) {
	if (window != null) {
		Display display = this.display;
		display.setMenuBar (menuBar);
	}
	sendEvent (SWT.Activate);
	if (isDisposed ()) return;
	if (!restoreFocus () && !traverseGroup (true)) setFocus ();
	if (isDisposed ()) return;
	if ((window.collectionBehavior() & OS.NSWindowCollectionBehaviorFullScreenPrimary) == 0) {
		Shell parentShell = this;
		while (parentShell.parent != null) {
			parentShell = (Shell) parentShell.parent;
			if (parentShell._getFullScreen ()) {
				break;
			}
		}
		if (!parentShell._getFullScreen () || menuBar != null) {
			updateSystemUIMode ();
		} else {
			parentShell.updateSystemUIMode ();
		}
	}
}

void windowDidDeminiturize(long /*int*/ id, long /*int*/ sel, long /*int*/ notification) {
	minimized = false;
	sendEvent(SWT.Deiconify);
}

void windowDidMiniturize(long /*int*/ id, long /*int*/ sel, long /*int*/ notification) {
	minimized = true;
	sendEvent(SWT.Iconify);
}

void windowDidMove(long /*int*/ id, long /*int*/ sel, long /*int*/ notification) {
	moved = true;
	sendEvent(SWT.Move);
}

void windowDidResize(long /*int*/ id, long /*int*/ sel, long /*int*/ notification) {
	if (((window.collectionBehavior() & OS.NSWindowCollectionBehaviorFullScreenPrimary) == 0) && fullScreen) {
		window.setFrame(fullScreenFrame, true);
		NSRect contentViewFrame = new NSRect();
		contentViewFrame.width = fullScreenFrame.width;
		contentViewFrame.height = fullScreenFrame.height;
		window.contentView().setFrame(contentViewFrame);
	}
	if (fixResize ()) {
		NSRect rect = window.frame ();
		rect.x = rect.y = 0;
		double /*float*/ scaleFactor = window.userSpaceScaleFactor();
		rect.width /= scaleFactor;
		rect.height /= scaleFactor;
		window.contentView ().setFrame (rect);
	}
	resized = true;
	sendEvent (SWT.Resize);
	if (isDisposed ()) return;
	if (layout != null) {
		markLayout (false, false);
		updateLayout (false);
	}
}

void windowDidResignKey(long /*int*/ id, long /*int*/ sel, long /*int*/ notification) {
	if (display.isDisposed()) return;
	sendEvent (SWT.Deactivate);
	if (isDisposed ()) return;
	setActiveControl (null);
	if (isDisposed ()) return;
	saveFocus();
}

void windowSendEvent (long /*int*/ id, long /*int*/ sel, long /*int*/ event) {
	NSEvent nsEvent = new NSEvent (event);
	int type = (int)/*64*/nsEvent.type ();
	switch (type) {
		case OS.NSLeftMouseDown:
		case OS.NSRightMouseDown:
		case OS.NSOtherMouseDown:
			display.clickCount = (int)(display.clickCountButton == nsEvent.buttonNumber() ? nsEvent.clickCount() : 1);
			display.clickCountButton = (int)nsEvent.buttonNumber();
			break;
		case OS.NSLeftMouseUp:
		case OS.NSRightMouseUp:
		case OS.NSOtherMouseUp:
		case OS.NSMouseMoved:
			NSView[] hitView = new NSView[1];
			Control control = display.findControl (false, hitView);
			if (control != null && (!control.isActive() || !control.isEnabled())) control = null;
			if (type == OS.NSMouseMoved) {
				Control trimControl = control;
				if (trimControl != null && trimControl.isTrim (hitView[0])) trimControl = null;
				display.checkEnterExit (trimControl, nsEvent, false);
				// Browser will send MouseMoved in response to a DOM event, so don't send it here.
				if (trimControl != null && (trimControl.state & WEBKIT_EVENTS_FIX) != 0) trimControl = null;
				if (trimControl != null) trimControl.sendMouseEvent (nsEvent, type, false);
			}
			
			// Tooltip updating: Find the widget under the cursor. If it changed, clear the tooltip from
			// the last tracked item and send a tooltip event to make it visible on the new widget. 
			Widget target = null;
			if (control != null) {
				NSPoint eventPoint = nsEvent.locationInWindow();
				if (hitView[0] != null) {
					NSWindow eventWindow = nsEvent.window();

					// If a NSMouseMoved happens on an inactive window, convert the
					// event coordinates to the window of the target view.
					if (eventWindow != null && eventWindow != hitView[0].window()) {
						eventPoint = eventWindow.convertBaseToScreen(eventPoint);
						eventPoint = hitView[0].window().convertScreenToBase(eventPoint);
					}
				}				
				target = control.findTooltip (eventPoint);
			}
			
			if (display.tooltipControl != control || display.tooltipTarget != target) {
				Control oldControl = display.tooltipControl;
				Shell oldShell = oldControl != null && !oldControl.isDisposed() ? oldControl.getShell() : null;
				Shell shell = control != null && !control.isDisposed() ? control.getShell() : null;
				if (oldShell != null) oldShell.sendToolTipEvent (false);
				if (shell != null) shell.sendToolTipEvent (true);
			}
			display.tooltipControl = control;
			display.tooltipTarget = target;
			break;
			
		case OS.NSKeyDown:
			/*
			* Feature in Cocoa.  For some reason, Cocoa does not perform accelerators
			* with ESC key code.  The fix is to perform the accelerators ourselves. 
			*/
			if (nsEvent.keyCode() == 53 /* ESC */ && menuBar != null && !menuBar.isDisposed()) {
				if (menuBar.nsMenu.performKeyEquivalent(nsEvent)) {
					return;
				}
			}
			/**
			 * Feature in cocoa.  Control+Tab, Ctrl+Shift+Tab, Ctrl+PageDown and Ctrl+PageUp are
			 * swallowed to handle native traversal. If we find that, force the key event to
			 * the first responder.
			 */
			if ((nsEvent.modifierFlags() & OS.NSControlKeyMask) != 0) {
				NSString chars = nsEvent.characters();
				
				if (chars != null && chars.length() == 1) {
					int firstChar = (int)/*64*/chars.characterAtIndex(0);

					// Shift-tab appears as control-Y.
					switch (firstChar) {
						case '\t':
						case 25:
						case OS.NSPageDownFunctionKey:
						case OS.NSPageUpFunctionKey:
							view.window().firstResponder().keyDown(nsEvent);
							return;
					}
				}
			}
			break;
	}
	
	// Window may have been disposed at this point.
	if (isDisposed()) return;
	super.windowSendEvent (id, sel, event);
}

boolean windowShouldClose(long /*int*/ id, long /*int*/ sel, long /*int*/ window) {
	if (isEnabled()) closeWidget (false);
	return false;
}

void windowWillClose(long /*int*/ id, long /*int*/ sel, long /*int*/ notification) {
	closeWidget(true);
}

}

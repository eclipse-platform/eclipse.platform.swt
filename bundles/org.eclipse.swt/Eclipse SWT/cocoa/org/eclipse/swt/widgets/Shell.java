/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#shell">Shell snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Shell extends Decorations {
	NSWindow window;
	SWTWindowDelegate windowDelegate;
	NSTrackingArea tooltipTrackingArea;
	boolean opened, moved, resized, fullScreen;
	Control lastActive;
	Rectangle normalBounds;
	boolean keyInputHappened;
	
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
	this (display, null, style, 0, false);
}

Shell (Display display, Shell parent, int style, int /*long*/handle, boolean embedded) {
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
	if (handle != 0) {
		if (embedded) {
			view = new NSView(handle);
		} else {
			window = new NSWindow(handle);
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
public static Shell internal_new (Display display, int /*long*/ handle) {
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
 * @since 3.5
 */
public static Shell cocoa_new (Display display, int /*long*/ handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle, true);
}

static int checkStyle (int style) {
	style = Decorations.checkStyle (style);
	style &= ~SWT.TRANSPARENT;
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	int bits = style & ~mask;
	if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
	if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
	return bits;
}

boolean accessibilityIsIgnored(int /*long*/ id, int /*long*/ sel) {
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

void bringToTop (boolean force) {
	if (getMinimized ()) return;
	if (force) {
		forceActive ();
	} else {
		setActive ();
	}
}

boolean canBecomeKeyWindow (int /*long*/ id, int /*long*/ sel) {
	if (window.styleMask () == OS.NSBorderlessWindowMask) return true;
	return super.canBecomeKeyWindow (id, sel);
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
	Rectangle trim = super.computeTrim(x, y, width, height);
	NSRect rect = new NSRect ();
	rect.x = trim.x;
	rect.y = trim.y;
	rect.width = trim.width;
	rect.height = trim.height;
	if (window != null) {
		if (!fixResize()) {
			rect = window.frameRectForContentRect(rect);
		}
	}
	return new Rectangle ((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
}

void createHandle () {
	state |= HIDDEN;
	if (window == null) {
		window = (NSWindow) new SWTWindow ().alloc ();
		int styleMask = OS.NSBorderlessWindowMask;
		if ((style & SWT.NO_TRIM) == 0) {
			if ((style & SWT.TITLE) != 0) styleMask |= OS.NSTitledWindowMask;
			if ((style & SWT.CLOSE) != 0) styleMask |= OS.NSClosableWindowMask;
			if ((style & SWT.MIN) != 0) styleMask |= OS.NSMiniaturizableWindowMask;
			if ((style & SWT.MAX) != 0) styleMask |= OS.NSResizableWindowMask;
			if ((style & SWT.RESIZE) != 0) styleMask |= OS.NSResizableWindowMask;
		}
		NSScreen screen = null;
		NSScreen primaryScreen = new NSScreen(NSScreen.screens().objectAtIndex(0));
		if (parent != null) screen = parent.getShell().window.screen();
		if (screen == null) screen = primaryScreen;
		window = window.initWithContentRect(new NSRect(), styleMask, OS.NSBackingStoreBuffered, false, screen);
		if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0 || (style & SWT.TOOL) != 0) {
			window.setHasShadow (true);
		}
		display.cascadeWindow(window, screen);
		NSRect screenFrame = screen.frame();
		float /*double*/ width = screenFrame.width * 5 / 8, height = screenFrame.height * 5 / 8;;
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
//		int /*long*/ cls = OS.objc_lookUpClass ("SWTWindow");
//		OS.object_setClass(window.id, cls);
		state &= ~HIDDEN;
		//TODO - get the content of the foreign window instead of creating it
		super.createHandle ();
		style |= SWT.NO_BACKGROUND;
	}
	window.setAcceptsMouseMovedEvents(true);
	windowDelegate = (SWTWindowDelegate)new SWTWindowDelegate().alloc().init();
	window.setDelegate(windowDelegate);
	id id = window.fieldEditor (true, null);
	if (id != null) {
		OS.object_setClass (id.id, OS.objc_getClass ("SWTEditorView"));
	}
}

/*
* Feature in Cocoa.  An NSWindow that is the child window of another NSWindow
* will track the movement of its parent window. The work around is to adjust
* the window's level.  
*/

void deregister () {
	super.deregister ();
	if (window != null) display.removeWidget (window);
	if (windowDelegate != null) display.removeWidget (windowDelegate);
}

void destroyWidget () {
	NSWindow window = this.window;
	Display display = this.display;
	releaseHandle ();
	if (window != null) {
		window.close();
	}
	//If another shell is not going to become active, clear the menu bar.
	if (!display.isDisposed () && display.getShells ().length == 0) {
		display.setMenuBar (null);
	}
}

void drawBackground (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
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
	*/
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.RESIZE) != 0 && (style & (SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX)) == 0) {
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
	window.makeKeyAndOrderFront (null);
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
	// TODO: Should we support embedded frame alpha?
	if (window == null) return 255;
	return (int)(window.alphaValue() * 255);
}

public Rectangle getBounds () {
	checkWidget();
	NSRect frame = (window == null ? view.frame() : window.frame());
	float /*double*/ y = display.getPrimaryFrame().height - (int)(frame.y + frame.height);
	return new Rectangle ((int)frame.x, (int)y, (int)frame.width, (int)frame.height);
}

public Rectangle getClientArea () {
	checkWidget();
	NSRect rect;
	if (window != null) {
		rect = window.frame();
		if (!fixResize ()) {
			rect = window.contentRectForFrameRect(rect);
		}
	} else {
		rect = scrollView != null ? scrollView.frame() : view.frame();
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

int getDrawCount () {
	return drawCount;
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
	// TODO: frame is relative to superview. What does getLocation mean in the embedded case?
	NSRect frame = (window != null ? window.frame() : view.frame());
	float /*double*/ y = display.getPrimaryFrame().height - (int)(frame.y + frame.height);
	return new Point ((int)frame.x, (int)y);
}

public boolean getMaximized () {
	checkWidget();
	if (window == null) return false;
	return !fullScreen && window.isZoomed();
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
//	Rect rect = new Rect ();
//	OS.GetWindowStructureWidths (shellHandle, rect);
//	CGPoint inMinLimits = new CGPoint (), inMaxLimits = new CGPoint ();
//	OS.GetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
//	int width = Math.max (1, (int) inMinLimits.x + (rect.left + rect.right));
//	int height = Math.max (1, (int) inMinLimits.y + (rect.top + rect.bottom));
//	return new Point (width, height);
	return null;
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
	NSRect frame = (window != null ? window.frame() : view.frame());
	return new Point ((int) frame.width, (int) frame.height);
}

float getThemeAlpha () {
	return 1;
}

boolean hasBorder () {
	return false;
}

void helpRequested(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
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
	invalidateChildrenVisibleRegion ();
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

boolean makeFirstResponder (int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	if (display.focusControl == null) display.focusControl = display.getFocusControl ();
	boolean result = super.makeFirstResponder(id, sel, notification);
	if (result) {
		Control newFocus = display.getFocusControl ();
		if (newFocus != null && newFocus != display.focusControl) {
			Control oldFocus = display.focusControl;
			display.focusControl = newFocus;
			if (oldFocus != null && !oldFocus.isDisposed ()) oldFocus.sendFocusEvent (SWT.FocusOut, false);
			if (newFocus != null && !newFocus.isDisposed ()) newFocus.sendFocusEvent (SWT.FocusIn, false);
		}
	}
	return result;
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
//	if (active) {
		if (!restoreFocus () && !traverseGroup (true)) setFocus ();
//	}
}

public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
//	int [] outImage = new int [1];
//	CGRect outFrame = new CGRect ();
//	if (OS.HIViewCreateOffscreenImage (handle, 0, outFrame, outImage) == OS.noErr) {
//		int width = OS.CGImageGetWidth (outImage [0]);
//		int height = OS.CGImageGetHeight (outImage [0]);
//	 	CGRect rect = new CGRect();
//	 	rect.width = width;
//		rect.height = height;
//		//TODO - does not draw the browser (cocoa widgets?)
//		OS.HIViewDrawCGImage (gc.handle, rect, outImage [0]);
//		OS.CGImageRelease (outImage [0]);
//	}
//	return true;
	return false;
}

void register () {
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
	display.clearModal (this);
	updateParent (false);
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

void sendToolTipEvent (boolean enter) {
	if (tooltipTrackingArea == null && isVisible()) {
		NSView contentView = window.contentView();
		int /*long*/ tag = contentView.addToolTipRect(new NSRect(), window, 0);
		if (tag != 0) tooltipTrackingArea = new NSTrackingArea(tag);
	}
	if (tooltipTrackingArea == null) return;
	NSDictionary userInfo = tooltipTrackingArea.userInfo();
	NSPoint pt = window.convertScreenToBase(NSEvent.mouseLocation());
	NSEvent event = NSEvent.enterExitEventWithType(enter ? OS.NSMouseEntered : OS.NSMouseExited, pt, 0, 0, window.windowNumber(), null, 0, 0, userInfo.id);
	if (OS.class_NSToolTipManager != 0) {
		OS.objc_msgSend(OS.objc_msgSend(OS.class_NSToolTipManager, OS.sel_sharedToolTipManager), enter ? OS.sel_mouseEntered_ : OS.sel_mouseExited_, event.id);
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
	if (window == null) return;	
	checkWidget ();
	if (!isVisible()) return;
	window.makeKeyAndOrderFront (null);
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
	// Embedded Shells are not resizable.
	if (window == null) return;
	if (fullScreen) setFullScreen (false);
	int screenHeight = (int) display.getPrimaryFrame().height;
	NSRect frame = window.frame();
	if (!move) {
		x = (int)frame.x;
		y = screenHeight - (int)(frame.y + frame.height);
	}
	if (!resize) {
		width = (int)frame.width;
		height = (int)frame.height;
	}
	frame.x = x;
	frame.y = screenHeight - (int)(y + height);
	frame.width = width;
	frame.height = height;
	window.setFrame(frame, false);
}

void setClipRegion (float /*double*/ x, float /*double*/ y) {
	if (regionPath != null) {
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(-x, -y);
		regionPath.transformUsingAffineTransform(transform);
		regionPath.addClip();
		transform.translateXBy(2*x, 2*y);
		regionPath.transformUsingAffineTransform(transform);
	}
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (((state & DISABLED) == 0) == enabled) return;
	super.setEnabled (enabled);
//	if (enabled && OS.IsWindowActive (shellHandle)) {
//		if (!restoreFocus ()) traverseGroup (false);
//	}
}

/**
 * Sets the full screen state of the receiver.
 * If the argument is <code>true</code> causes the receiver
 * to switch to the full screen state, and if the argument is
 * <code>false</code> and the receiver was previously switched
 * into full screen state, causes the receiver to switch back
 * to either the maximmized or normal states.
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
//	if (fullScreen) {
//		normalBounds = getBounds ();
//		OS.ChangeWindowAttributes (shellHandle, OS.kWindowNoTitleBarAttribute, OS.kWindowResizableAttribute | OS.kWindowLiveResizeAttribute);
//		updateSystemUIMode ();
//		Rectangle screen = getMonitor ().getBounds ();
//		if (menuBar != null && getMonitor ().equals(display.getPrimaryMonitor ())) {
//			Rect rect = new Rect ();
//			int gdevice = OS.GetMainDevice ();
//			OS.GetAvailableWindowPositioningBounds (gdevice, rect);
//			screen.height -= rect.top;
//			screen.y += rect.top;
//		}
//		Rect rect = new Rect ();
//		OS.SetRect (rect, (short) screen.x, (short) screen.y, (short) (screen.x + screen.width), (short) (screen.y + screen.height));
//		OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
//	} else {
//		int attributes = 0;
//		if ((style & SWT.RESIZE) != 0) {
//			attributes |= OS.kWindowResizableAttribute;
//			/*
//			* Bug in the Macintosh.  For some reason, a window has no title bar
//			* and the kWindowResizableAttribute, no rubber banding feedback is
//			* given while the window is resizing.  The fix is to create the window 
//			* with kWindowLiveResizeAttribute in this case.  This is inconsistent
//			* with other windows, but the user will get feedback when resizing.
//			*/
//			if ((style & SWT.TITLE) == 0) attributes |= OS.kWindowLiveResizeAttribute;
//			if (!OS.__BIG_ENDIAN__()) attributes |= OS.kWindowLiveResizeAttribute;
//		}
//		OS.ChangeWindowAttributes (shellHandle, attributes, OS.kWindowNoTitleBarAttribute);
//		OS.SetSystemUIMode (OS.kUIModeNormal, 0);
//		if (maximized) {
//			setMaximized (true);
//		} else {
//			Rect rect = new Rect ();
//			if (normalBounds != null) OS.SetRect (rect, (short) normalBounds.x, (short) normalBounds.y, (short) (normalBounds.x + normalBounds.width), (short) (normalBounds.y + normalBounds.height));
//			OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
//		}
//		normalBounds = null;
//	}
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
//	int clientWidth = 0, clientHeight = 0;
//	int trim = SWT.TITLE | SWT.CLOSE | SWT.MIN | SWT.MAX;
//	if ((style & SWT.NO_TRIM) == 0 && (style & trim) != 0) {
//		clientWidth = DEFAULT_CLIENT_WIDTH;
//		clientHeight = DEFAULT_CLIENT_HEIGHT;
//	}
//	Rect rect = new Rect ();
//	OS.GetWindowStructureWidths (shellHandle, rect);
//	CGPoint inMinLimits = new CGPoint (), inMaxLimits = new CGPoint ();
//	OS.GetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
//	width = Math.max (width, clientWidth + rect.left + rect.right);
//	height = Math.max (height, clientHeight + rect.top + rect.bottom);
//	inMinLimits.x = width - (rect.left + rect.right);
//	inMinLimits.y = height - (rect.top + rect.bottom);
//	OS.SetWindowResizeLimits (shellHandle, inMinLimits, inMaxLimits);
//	Point size = getSize ();
//	int newWidth = Math.max (size.x, width), newHeight = Math.max (size.y, height);
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
	checkWidget();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setMinimumSize (size.x, size.y);
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
		window.setOpaque(false);
	} else {
		window.setBackgroundColor(NSColor.windowBackgroundColor());
		window.setOpaque(true);
	}
	window.contentView().setNeedsDisplay(true);
	if (isVisible() && window.hasShadow()) {
		window.display();
		window.invalidateShadow();
	}
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
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		topView ().setHidden (false);
		invalidateVisibleRegion();
		if (key) {
			window.makeKeyAndOrderFront (null);
		} else {
			window.orderFront (null);
		}
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
		window.orderOut (null);
		topView ().setHidden (true);
		invalidateVisibleRegion();
		sendEvent (SWT.Hide);
	}
}

void setZOrder () {
	if (scrollView != null) scrollView.setDocumentView (view);
	if (window == null) return;
	window.setContentView (scrollView != null ? scrollView : view);
	if (fixResize ()) {
		NSRect rect = window.frame();
		rect.x = rect.y = 0;
		window.contentView().setFrame(rect);
	}
}

void setZOrder (Control control, boolean above) {
//	if (above) {
//		//NOT DONE - move one window above another
//	 	OS.BringToFront (shellHandle);
//	 } else {
//		int window = control == null ? 0 : OS.GetControlOwner (control.handle);
//		OS.SendBehind (shellHandle, window);
//	}
}

boolean traverseEscape () {
	if (parent == null) return false;
	if (!isVisible () || !isEnabled ()) return false;
	close ();
	return true;
}

void updateModal () {
	// do nothing
}

void updateParent (boolean visible) {
	if (visible) {
		if (parent != null && parent.getVisible ()) {
			((Shell)parent).window.addChildWindow (window, OS.NSWindowAbove);
		}		
	} else {
		NSWindow parentWindow = window.parentWindow ();
		if (parentWindow != null) parentWindow.removeChildWindow (window);
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
//	if (fullScreen) {
//		int mode = OS.kUIModeAllHidden;
//		if (menuBar != null) {
//			mode = OS.kUIModeContentHidden;
//		}
//		OS.SetSystemUIMode (mode, 0);
//	} else {
//		OS.SetSystemUIMode (OS.kUIModeNormal, 0);
//	}
}

int /*long*/ view_stringForToolTip_point_userData (int /*long*/ id, int /*long*/ sel, int /*long*/ view, int /*long*/ tag, int /*long*/ point, int /*long*/ userData) {
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

void windowDidBecomeKey(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	super.windowDidBecomeKey(id, sel, notification);
	Display display = this.display;
	display.setMenuBar (menuBar);
	sendEvent (SWT.Activate);
	Control control = Display.GetFocusControl(window);
	if (control != null && !control.isDisposed() && control != display.focusControl) {
		display.focusControl = control;
		control.sendFocusEvent(SWT.FocusIn, true);
	}
//	if (!isDisposed ()) {
//		if (!restoreFocus () && !traverseGroup (true)) setFocus ();
//	}
}

void windowDidMove(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	moved = true;
	sendEvent(SWT.Move);
}

void windowDidResize(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	if (fixResize ()) {
		NSRect rect = window.frame ();
		rect.x = rect.y = 0;
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

void windowDidResignKey(int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	super.windowDidResignKey(id, sel, notification);
//	Display display = this.display;
	sendEvent (SWT.Deactivate);
	if (isDisposed ()) return;
	Control control = display.focusControl;
	if (control != null && !control.isDisposed() && control.getShell() == this) {
		display.focusControl = null;
		control.sendFocusEvent(SWT.FocusOut, true);
	}
//	saveFocus ();
//	if (savedFocus != null) {
//		/*
//		* Bug in the Macintosh.  When ClearKeyboardFocus() is called,
//		* the control that has focus gets two kEventControlSetFocus
//		* events indicating that focus was lost.  The fix is to ignore
//		* both of these and send the focus lost event explicitly.
//		*/
//		display.ignoreFocus = true;
//		OS.ClearKeyboardFocus (shellHandle);
//		display.ignoreFocus = false;
//		if (!savedFocus.isDisposed ()) savedFocus.sendFocusEvent (SWT.FocusOut, false);
//	}
}

void windowSendEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ event) {
	NSEvent nsEvent = new NSEvent (event);
	int type = (int)/*64*/nsEvent.type ();
	switch (type) {
		case OS.NSLeftMouseUp:
		case OS.NSRightMouseUp:
		case OS.NSOtherMouseUp:
		case OS.NSMouseMoved:
			NSView[] hitView = new NSView[1];
			Control control = display.findControl (false, hitView);
			if (type == OS.NSMouseMoved) {
				Control trimControl = control;
				if (control != null && control.isTrim (hitView[0])) trimControl = null;
				display.checkEnterExit (trimControl, nsEvent, false);
				if (trimControl != null) trimControl.sendMouseEvent (nsEvent, type, false);
			}
			Widget target = null;
			if (control != null) target = control.findTooltip (nsEvent.locationInWindow());
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
	}
	super.windowSendEvent (id, sel, event);
}

boolean windowShouldClose(int /*long*/ id, int /*long*/ sel, int /*long*/ window) {
	closeWidget ();
	return false;
}

}

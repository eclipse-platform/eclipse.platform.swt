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


import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.accessibility.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b>
 * <dd>DragDetect, FocusIn, FocusOut, Help, KeyDown, KeyUp, MenuDetect, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize, Traverse</dd>
 * </dl>
 * </p><p>
 * Only one of LEFT_TO_RIGHT or RIGHT_TO_LEFT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#control">Control snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public abstract class Control extends Widget implements Drawable {
	/**
	 * the handle to the OS resource 
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int /*long*/ handle;
	Composite parent;
	Cursor cursor;
	Menu menu;
	String toolTipText;
	Object layoutData;
	Accessible accessible;
	Image backgroundImage;
	Region region;
	Font font;
	int drawCount, foreground, background;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Control () {
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
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#BORDER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
 * interface.
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when a drag gesture occurs, by sending it
 * one of the messages defined in the <code>DragDetectListener</code>
 * interface.
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
 * @see DragDetectListener
 * @see #removeDragDetectListener
 * 
 * @since 3.3
 */
public void addDragDetectListener (DragDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.DragDetect,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control gains or loses focus, by sending
 * it one of the messages defined in the <code>FocusListener</code>
 * interface.
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
 * @see FocusListener
 * @see #removeFocusListener
 */
public void addFocusListener (FocusListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.FocusIn,typedListener);
	addListener (SWT.FocusOut,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when help events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>HelpListener</code> interface.
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
 * @see HelpListener
 * @see #removeHelpListener
 */
public void addHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard, by sending
 * it one of the messages defined in the <code>KeyListener</code>
 * interface.
 * <p>
 * When a key listener is added to a control, the control
 * will take part in widget traversal.  By default, all
 * traversal keys (such as the tab key and so on) are
 * delivered to the control.  In order for a control to take
 * part in traversal, it should listen for traversal events.
 * Otherwise, the user can traverse into a control but not
 * out.  Note that native controls such as table and tree
 * implement key traversal in the operating system.  It is
 * not necessary to add traversal listeners for these controls,
 * unless you want to override the default traversal.
 * </p>
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
 * @see KeyListener
 * @see #removeKeyListener
 */
public void addKeyListener (KeyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.KeyUp,typedListener);
	addListener (SWT.KeyDown,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the platform-specific context menu trigger
 * has occurred, by sending it one of the messages defined in
 * the <code>MenuDetectListener</code> interface.
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
 * @see MenuDetectListener
 * @see #removeMenuDetectListener
 *
 * @since 3.3
 */
public void addMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MenuDetect, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when mouse buttons are pressed and released, by sending
 * it one of the messages defined in the <code>MouseListener</code>
 * interface.
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
 * @see MouseListener
 * @see #removeMouseListener
 */
public void addMouseListener (MouseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseDown,typedListener);
	addListener (SWT.MouseUp,typedListener);
	addListener (SWT.MouseDoubleClick,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse passes or hovers over controls, by sending
 * it one of the messages defined in the <code>MouseTrackListener</code>
 * interface.
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
 * @see MouseTrackListener
 * @see #removeMouseTrackListener
 */
public void addMouseTrackListener (MouseTrackListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse moves, by sending it one of the
 * messages defined in the <code>MouseMoveListener</code>
 * interface.
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
 * @see MouseMoveListener
 * @see #removeMouseMoveListener
 */
public void addMouseMoveListener (MouseMoveListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseMove,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the mouse wheel is scrolled, by sending
 * it one of the messages defined in the
 * <code>MouseWheelListener</code> interface.
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
 * @see MouseWheelListener
 * @see #removeMouseWheelListener
 *
 * @since 3.3
 */
public void addMouseWheelListener (MouseWheelListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseWheel, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver needs to be painted, by sending it
 * one of the messages defined in the <code>PaintListener</code>
 * interface.
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
 * @see PaintListener
 * @see #removePaintListener
 */
public void addPaintListener (PaintListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Paint,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when traversal events occur, by sending it
 * one of the messages defined in the <code>TraverseListener</code>
 * interface.
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
 * @see TraverseListener
 * @see #removeTraverseListener
 */
public void addTraverseListener (TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}

int /*long*/ borderHandle () {
	return handle;
}

void checkBackground () {
	Shell shell = getShell ();
	if (this == shell) return;
	state &= ~PARENT_BACKGROUND;
	Composite composite = parent;
	do {
		int mode = composite.backgroundMode;
		if (mode != 0) {
			if (mode == SWT.INHERIT_DEFAULT) {
				Control control = this;
				do {
					if ((control.state & THEME_BACKGROUND) == 0) {
						return;
					}
					control = control.parent;
				} while (control != composite);
			}
			state |= PARENT_BACKGROUND;
			return;
		}
		if (composite == shell) break;
		composite = composite.parent;
	} while (true);	
}

void checkBorder () {
	if (getBorderWidth () == 0) style &= ~SWT.BORDER;
}

void checkBuffered () {
	style &= ~SWT.DOUBLE_BUFFERED;
}

void checkComposited () {
	/* Do nothing */
}

boolean checkHandle (int /*long*/ hwnd) {
	return hwnd == handle;
}

void checkMirrored () {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
		if ((bits & OS.WS_EX_LAYOUTRTL) != 0) style |= SWT.MIRRORED;
	}
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @return the preferred size of the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>preferred size</em> of a control is the size that it would
 * best be displayed at. The width hint and height hint arguments
 * allow the caller to ask a control questions such as "Given a particular
 * width, how high does the control need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p><p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
 * @return the preferred size of the control.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 * @see #getBorderWidth
 * @see #getBounds
 * @see #getSize
 * @see #pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

Widget computeTabGroup () {
	if (isTabGroup ()) return this;
	return parent.computeTabGroup ();
}

Control computeTabRoot () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		int index = 0;
		while (index < tabList.length) {
			if (tabList [index] == this) break;
			index++;
		}
		if (index == tabList.length) {
			if (isTabGroup ()) return this;
		}
	}
	return parent.computeTabRoot ();
}

Widget [] computeTabList () {
	if (isTabGroup ()) {
		if (getVisible () && getEnabled ()) {
			return new Widget [] {this};
		}
	}
	return new Widget [0];
}

void createHandle () {
	int /*long*/ hwndParent = widgetParent ();
	handle = OS.CreateWindowEx (
		widgetExtStyle (),
		windowClass (),
		null,
		widgetStyle (),
		OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
		hwndParent,
		0,
		OS.GetModuleHandle (null),
		widgetCreateStruct ());
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.WS_CHILD) != 0) {
		OS.SetWindowLongPtr (handle, OS.GWLP_ID, handle);
	}
	if (OS.IsDBLocale && hwndParent != 0) {
		int /*long*/ hIMC = OS.ImmGetContext (hwndParent);
		OS.ImmAssociateContext (handle, hIMC);
		OS.ImmReleaseContext (hwndParent, hIMC);
	}
}

void createWidget () {
	state |= DRAG_DETECT;
	foreground = background = -1;
	checkOrientation (parent);
	createHandle ();
	checkBackground ();
	checkBuffered ();
	checkComposited ();
	register ();
	subclass ();
	setDefaultFont ();
	checkMirrored ();
	checkBorder ();
	if ((state & PARENT_BACKGROUND) != 0) {
		setBackground ();
	}
}

int defaultBackground () {
	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOW);
	return OS.GetSysColor (OS.COLOR_BTNFACE);
}

int /*long*/ defaultFont () {
	return display.getSystemFont ().handle;
}

int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
}

void deregister () {
	display.removeControl (handle);
}

void destroyWidget () {
	int /*long*/ hwnd = topHandle ();
	releaseHandle ();
	if (hwnd != 0) {
		OS.DestroyWindow (hwnd);
	}
}

/**
 * Detects a drag and drop gesture.  This method is used
 * to detect a drag gesture when called from within a mouse
 * down listener.
 * 
 * <p>By default, a drag is detected when the gesture
 * occurs anywhere within the client area of a control.
 * Some controls, such as tables and trees, override this
 * behavior.  In addition to the operating system specific
 * drag gesture, they require the mouse to be inside an
 * item.  Custom widget writers can use <code>setDragDetect</code>
 * to disable the default detection, listen for mouse down,
 * and then call <code>dragDetect()</code> from within the
 * listener to conditionally detect a drag.
 * </p>
 *
 * @param event the mouse down event
 * 
 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *  
 * @see DragDetectListener
 * @see #addDragDetectListener
 * 
 * @see #getDragDetect
 * @see #setDragDetect
 * 
 * @since 3.3
 */
public boolean dragDetect (Event event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return dragDetect (event.button, event.count, event.stateMask, event.x, event.y);
}

/**
 * Detects a drag and drop gesture.  This method is used
 * to detect a drag gesture when called from within a mouse
 * down listener.
 * 
 * <p>By default, a drag is detected when the gesture
 * occurs anywhere within the client area of a control.
 * Some controls, such as tables and trees, override this
 * behavior.  In addition to the operating system specific
 * drag gesture, they require the mouse to be inside an
 * item.  Custom widget writers can use <code>setDragDetect</code>
 * to disable the default detection, listen for mouse down,
 * and then call <code>dragDetect()</code> from within the
 * listener to conditionally detect a drag.
 * </p>
 *
 * @param event the mouse down event
 * 
 * @return <code>true</code> if the gesture occurred, and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see DragDetectListener
 * @see #addDragDetectListener
 * 
 * @see #getDragDetect
 * @see #setDragDetect
 * 
 * @since 3.3
 */
public boolean dragDetect (MouseEvent event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return dragDetect (event.button, event.count, event.stateMask, event.x, event.y);
}

boolean dragDetect (int button, int count, int stateMask, int x, int y) {
	if (button != 1 || count != 1) return false;
	boolean dragging = dragDetect (handle, x, y, false, null, null);
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
		if (OS.GetCapture () != handle) OS.SetCapture (handle);
	}
	if (!dragging) {
		/*
		* Feature in Windows.  DragDetect() captures the mouse
		* and tracks its movement until the user releases the
		* left mouse button, presses the ESC key, or moves the
		* mouse outside the drag rectangle.  If the user moves
		* the mouse outside of the drag rectangle, DragDetect()
		* returns true and a drag and drop operation can be
		* started.  When the left mouse button is released or
		* the ESC key is pressed, these events are consumed by
		* DragDetect() so that application code that matches
		* mouse down/up pairs or looks for the ESC key will not
		* function properly.  The fix is to send the missing
		* events when the drag has not started.
		* 
		* NOTE: For now, don't send a fake WM_KEYDOWN/WM_KEYUP
		* events for the ESC key.  This would require computing
		* wParam (the key) and lParam (the repeat count, scan code,
		* extended-key flag, context code, previous key-state flag,
		* and transition-state flag) which is non-trivial.
		*/
		if (button == 1 && OS.GetKeyState (OS.VK_ESCAPE) >= 0) {
			int wParam = 0;
			if ((stateMask & SWT.CTRL) != 0) wParam |= OS.MK_CONTROL;
			if ((stateMask & SWT.SHIFT) != 0) wParam |= OS.MK_SHIFT;
			if ((stateMask & SWT.ALT) != 0) wParam |= OS.MK_ALT;
			if ((stateMask & SWT.BUTTON1) != 0) wParam |= OS.MK_LBUTTON;
			if ((stateMask & SWT.BUTTON2) != 0) wParam |= OS.MK_MBUTTON;
			if ((stateMask & SWT.BUTTON3) != 0) wParam |= OS.MK_RBUTTON;
			if ((stateMask & SWT.BUTTON4) != 0) wParam |= OS.MK_XBUTTON1;
			if ((stateMask & SWT.BUTTON5) != 0) wParam |= OS.MK_XBUTTON2;
			int /*long*/ lParam = OS.MAKELPARAM (x, y);
			OS.SendMessage (handle, OS.WM_LBUTTONUP, wParam, lParam);
		}
		return false;
	}
	return sendDragEvent (button, stateMask, x, y);
}

void drawBackground (int /*long*/ hDC) {
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	drawBackground (hDC, rect);
}

void drawBackground (int /*long*/ hDC, RECT rect) {
	drawBackground (hDC, rect, -1);
}

void drawBackground (int /*long*/ hDC, RECT rect, int pixel) {
	Control control = findBackgroundControl ();
	if (control != null) {
		if (control.backgroundImage != null) {
			fillImageBackground (hDC, control, rect);
			return;
		}
		pixel = control.getBackgroundPixel ();
	}
	if (pixel == -1) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
				control = findThemeControl ();
				if (control != null) {
					fillThemeBackground (hDC, control, rect);
					return;
				}
			}
		}
	}
	if (pixel == -1) pixel = getBackgroundPixel ();
	fillBackground (hDC, pixel, rect);
}

void drawImageBackground (int /*long*/ hDC, int /*long*/ hwnd, int /*long*/ hBitmap, RECT rect) {
	RECT rect2 = new RECT ();
	OS.GetClientRect (hwnd, rect2);
	OS.MapWindowPoints (hwnd, handle, rect2, 2);
	int /*long*/ hBrush = findBrush (hBitmap, OS.BS_PATTERN);
	POINT lpPoint = new POINT ();
	OS.GetWindowOrgEx (hDC, lpPoint);
	OS.SetBrushOrgEx (hDC, -rect2.left - lpPoint.x, -rect2.top - lpPoint.y, lpPoint);
	int /*long*/ hOldBrush = OS.SelectObject (hDC, hBrush);
	OS.PatBlt (hDC, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
	OS.SetBrushOrgEx (hDC, lpPoint.x, lpPoint.y, null);
	OS.SelectObject (hDC, hOldBrush);
}

void drawThemeBackground (int /*long*/ hDC, int /*long*/ hwnd, RECT rect) {
	/* Do nothing */
}

void enableDrag (boolean enabled) {
	/* Do nothing */
}

void enableWidget (boolean enabled) {
	OS.EnableWindow (handle, enabled);
}

void fillBackground (int /*long*/ hDC, int pixel, RECT rect) {
	if (rect.left > rect.right || rect.top > rect.bottom) return;
	int /*long*/ hPalette = display.hPalette;
	if (hPalette != 0) {
		OS.SelectPalette (hDC, hPalette, false);
		OS.RealizePalette (hDC);
	}
	OS.FillRect (hDC, rect, findBrush (pixel, OS.BS_SOLID));
}

void fillImageBackground (int /*long*/ hDC, Control control, RECT rect) {
	if (rect.left > rect.right || rect.top > rect.bottom) return;
	if (control != null) {
		Image image = control.backgroundImage;
		if (image != null) {
			control.drawImageBackground (hDC, handle, image.handle, rect);
		}
	}
}

void fillThemeBackground (int /*long*/ hDC, Control control, RECT rect) {
	if (rect.left > rect.right || rect.top > rect.bottom) return;
	if (control != null) {
		control.drawThemeBackground (hDC, handle, rect);
	}
}

Control findBackgroundControl () {
	if (background != -1 || backgroundImage != null) return this;
	return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundControl () : null;
}

int /*long*/ findBrush (int /*long*/ value, int lbStyle) {
	return parent.findBrush (value, lbStyle);
}

Cursor findCursor () {
	if (cursor != null) return cursor;
	return parent.findCursor ();
}

Control findImageControl () {
	Control control = findBackgroundControl ();
	return control != null && control.backgroundImage != null ? control : null;
}

Control findThemeControl () {
	return background == -1 && backgroundImage == null ? parent.findThemeControl () : null;
}

Menu [] findMenus (Control control) {
	if (menu != null && this != control) return new Menu [] {menu};
	return new Menu [0];
}

char findMnemonic (String string) {
	int index = 0;
	int length = string.length ();
	do {
		while (index < length && string.charAt (index) != '&') index++;
		if (++index >= length) return '\0';
		if (string.charAt (index) != '&') return string.charAt (index);
		index++;
	} while (index < length);
 	return '\0';
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	oldShell.fixShell (newShell, this);
	oldDecorations.fixDecorations (newDecorations, this, menus);
}

void fixFocus (Control focusControl) {
	Shell shell = getShell ();
	Control control = this;
	while (control != shell && (control = control.parent) != null) {
		if (control.setFixedFocus ()) return;
	}
	shell.setSavedFocus (focusControl);
	OS.SetFocus (0);
}

/**
 * Forces the receiver to have the <em>keyboard focus</em>, causing
 * all keyboard events to be delivered to it.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setFocus
 */
public boolean forceFocus () {
	checkWidget ();
	if (display.focusEvent == SWT.FocusOut) return false;
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible () || !isActive ()) return false;
	if (isFocusControl ()) return true;
	shell.setSavedFocus (null);
	/*
	* This code is intentionally commented.
	*
	* When setting focus to a control, it is
	* possible that application code can set
	* the focus to another control inside of
	* WM_SETFOCUS.  In this case, the original
	* control will no longer have the focus
	* and the call to setFocus() will return
	* false indicating failure.
	* 
	* We are still working on a solution at
	* this time.
	*/
//	if (OS.GetFocus () != OS.SetFocus (handle)) return false;
	OS.SetFocus (handle);
	if (isDisposed ()) return false;
	shell.setSavedFocus (this);
	return isFocusControl ();
}

void forceResize () {
	if (parent == null) return;
	WINDOWPOS [] lpwp = parent.lpwp;
	if (lpwp == null) return;
	for (int i=0; i<lpwp.length; i++) {
		WINDOWPOS wp = lpwp [i];
		if (wp != null && wp.hwnd == handle) {
			/*
			* This code is intentionally commented.  All widgets that
			* are created by SWT have WS_CLIPSIBLINGS to ensure that
			* application code does not draw outside of the control.
			*/	
//			int count = parent.getChildrenCount ();
//			if (count > 1) {
//				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//				if ((bits & OS.WS_CLIPSIBLINGS) == 0) wp.flags |= OS.SWP_NOCOPYBITS;
//			}
			SetWindowPos (wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
			lpwp [i] = null;
			return;
		}	
	}
}

/**
 * Returns the accessible object for the receiver.
 * If this is the first time this object is requested,
 * then the object is created and returned.
 *
 * @return the accessible object
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Accessible#addAccessibleListener
 * @see Accessible#addAccessibleControlListener
 * 
 * @since 2.0
 */
public Accessible getAccessible () {
	checkWidget ();
	if (accessible == null) accessible = new_Accessible (this);
	return accessible;
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getBackground () {
	checkWidget ();
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return Color.win32_new (display, control.getBackgroundPixel ());
}

/**
 * Returns the receiver's background image.
 *
 * @return the background image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Image getBackgroundImage () {
	checkWidget ();
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return control.backgroundImage;
}

int getBackgroundPixel () {
	return background != -1 ? background :  defaultBackground ();
}

/**
 * Returns the receiver's border width.
 *
 * @return the border width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getBorderWidth () {
	checkWidget ();
	int /*long*/ borderHandle = borderHandle ();
	int bits1 = OS.GetWindowLong (borderHandle, OS.GWL_EXSTYLE);
	if ((bits1 & OS.WS_EX_CLIENTEDGE) != 0) return OS.GetSystemMetrics (OS.SM_CXEDGE);
	if ((bits1 & OS.WS_EX_STATICEDGE) != 0) return OS.GetSystemMetrics (OS.SM_CXBORDER);
	int bits2 = OS.GetWindowLong (borderHandle, OS.GWL_STYLE);
	if ((bits2 & OS.WS_BORDER) != 0) return OS.GetSystemMetrics (OS.SM_CXBORDER);
	return 0;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null),
 * unless the receiver is a shell. In this case, the location is
 * relative to the display.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (topHandle (), rect);
	int /*long*/ hwndParent = parent == null ? 0 : parent.handle;
	OS.MapWindowPoints (0, hwndParent, rect, 2);
	int width = rect.right - rect.left;
	int height =  rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

int getCodePage () {
	if (OS.IsUnicode) return OS.CP_ACP;
	int /*long*/ hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	LOGFONT logFont = OS.IsUnicode ? (LOGFONT) new LOGFONTW () : new LOGFONTA ();
	OS.GetObject (hFont, LOGFONT.sizeof, logFont);
	int cs = logFont.lfCharSet & 0xFF;
	int [] lpCs = new int [8];
	if (OS.TranslateCharsetInfo (cs, lpCs, OS.TCI_SRCCHARSET)) {
		return lpCs [1];
	}
	return OS.GetACP ();
}

String getClipboardText () {
	String string = "";
	if (OS.OpenClipboard (0)) {
		int /*long*/ hMem = OS.GetClipboardData (OS.IsUnicode ? OS.CF_UNICODETEXT : OS.CF_TEXT);
		if (hMem != 0) {
			/* Ensure byteCount is a multiple of 2 bytes on UNICODE platforms */
			int byteCount = OS.GlobalSize (hMem) / TCHAR.sizeof * TCHAR.sizeof;
			int /*long*/ ptr = OS.GlobalLock (hMem);
			if (ptr != 0) {
				/* Use the character encoding for the default locale */
				TCHAR buffer = new TCHAR (0, byteCount / TCHAR.sizeof);
				OS.MoveMemory (buffer, ptr, byteCount);
				string = buffer.toString (0, buffer.strlen ());
				OS.GlobalUnlock (hMem);
			}
		}
		OS.CloseClipboard ();
	}
	return string;
}

/**
 * Returns the receiver's cursor, or null if it has not been set.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @return the receiver's cursor or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public Cursor getCursor () {
	checkWidget ();
	return cursor;
}

/**
 * Returns <code>true</code> if the receiver is detecting
 * drag gestures, and  <code>false</code> otherwise. 
 *
 * @return the receiver's drag detect state
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public boolean getDragDetect () {
	checkWidget ();
	return (state & DRAG_DETECT) != 0;
}

boolean getDrawing () {
	return drawCount <= 0;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget ();
	return OS.IsWindowEnabled (handle);
}

/**
 * Returns the font that the receiver will use to paint textual information.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Font getFont () {
	checkWidget ();
	if (font != null) return font;
	int /*long*/ hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (hFont == 0) hFont = defaultFont ();
	return Font.win32_new (display, hFont);
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getForeground () {
	checkWidget ();
	return Color.win32_new (display, getForegroundPixel ());
}

int getForegroundPixel () {
	return foreground != -1 ? foreground : defaultForeground ();
}

/**
 * Returns layout data which is associated with the receiver.
 *
 * @return the receiver's layout data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Object getLayoutData () {
	checkWidget ();
	return layoutData;
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null), unless
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
 *
 * @return the receiver's location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getLocation () {
	checkWidget ();
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (topHandle (), rect);
	int /*long*/ hwndParent = parent == null ? 0 : parent.handle;
	OS.MapWindowPoints (0, hwndParent, rect, 2);
	return new Point (rect.left, rect.top);
}

/**
 * Returns the receiver's pop up menu if it has one, or null
 * if it does not. All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Menu getMenu () {
	checkWidget ();
	return menu;
}

/**
 * Returns the receiver's monitor.
 * 
 * @return the receiver's monitor
 * 
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget ();
	if (OS.IsWinCE || OS.WIN32_VERSION < OS.VERSION (4, 10)) {
		return display.getPrimaryMonitor ();
	}
	int /*long*/ hmonitor = OS.MonitorFromWindow (handle, OS.MONITOR_DEFAULTTONEAREST);
	MONITORINFO lpmi = new MONITORINFO ();
	lpmi.cbSize = MONITORINFO.sizeof;
	OS.GetMonitorInfo (hmonitor, lpmi);
	Monitor monitor = new Monitor ();
	monitor.handle = hmonitor;
	monitor.x = lpmi.rcMonitor_left;
	monitor.y = lpmi.rcMonitor_top;
	monitor.width = lpmi.rcMonitor_right - lpmi.rcMonitor_left;
	monitor.height = lpmi.rcMonitor_bottom - lpmi.rcMonitor_top;
	monitor.clientX = lpmi.rcWork_left;
	monitor.clientY = lpmi.rcWork_top;
	monitor.clientWidth = lpmi.rcWork_right - lpmi.rcWork_left;
	monitor.clientHeight = lpmi.rcWork_bottom - lpmi.rcWork_top;
	return monitor;
}

/**
 * Returns the receiver's parent, which must be a <code>Composite</code>
 * or null when the receiver is a shell that was created with null or
 * a display for a parent.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Composite getParent () {
	checkWidget ();
	return parent;
}

Control [] getPath () {
	int count = 0;
	Shell shell = getShell ();
	Control control = this;
	while (control != shell) {
		count++;
		control = control.parent;
	}
	control = this;
	Control [] result = new Control [count];
	while (control != shell) {
		result [--count] = control;
		control = control.parent;
	}
	return result;
}

/** 
 * Returns the region that defines the shape of the control,
 * or null if the control has the default shape.
 *
 * @return the region that defines the shape of the shell (or null)
 *	
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public Region getRegion () {
	checkWidget ();
	return region;
}

/**
 * Returns the receiver's shell. For all controls other than
 * shells, this simply returns the control's nearest ancestor
 * shell. Shells return themselves, even if they are children
 * of other shells.
 *
 * @return the receiver's shell
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getParent
 */
public Shell getShell () {
	checkWidget ();
	return parent.getShell ();
}

/**
 * Returns a point describing the receiver's size. The
 * x coordinate of the result is the width of the receiver.
 * The y coordinate of the result is the height of the
 * receiver.
 *
 * @return the receiver's size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSize () {
	checkWidget ();
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (topHandle (), rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Point (width, height);
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
	if (!getDrawing()) return (state & HIDDEN) == 0;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.WS_VISIBLE) != 0;
}

boolean hasCursor () {
	RECT rect = new RECT ();
	if (!OS.GetClientRect (handle, rect)) return false;
	OS.MapWindowPoints (handle, 0, rect, 2);
	POINT pt = new POINT ();
	return OS.GetCursorPos (pt) && OS.PtInRect (rect, pt);
}

boolean hasFocus () {
	/*
	* If a non-SWT child of the control has focus,
	* then this control is considered to have focus
	* even though it does not have focus in Windows.
	*/
	int /*long*/ hwndFocus = OS.GetFocus ();
	while (hwndFocus != 0) {
		if (hwndFocus == handle) return true;
		if (display.getControl (hwndFocus) != null) {
			return false;
		}
		hwndFocus = OS.GetParent (hwndFocus);
	}
	return false;
}

/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data 
 * @return the platform specific GC handle
 */
public int /*long*/ internal_new_GC (GCData data) {
	checkWidget();
	int /*long*/ hwnd = handle;
	if (data != null && data.hwnd != 0) hwnd = data.hwnd;
	if (data != null) data.hwnd = hwnd;
	int /*long*/ hDC = 0;
	if (data == null || data.ps == null) {
		hDC = OS.GetDC (hwnd);
	} else {
		hDC = OS.BeginPaint (hwnd, data.ps);
	}
	if (hDC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (data != null) {
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			if ((data.style & mask) != 0) {
				data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
			} else {
				int flags = OS.GetLayout (hDC);
				if ((flags & OS.LAYOUT_RTL) != 0) {
					data.style |= SWT.RIGHT_TO_LEFT | SWT.MIRRORED;
				} else {
					data.style |= SWT.LEFT_TO_RIGHT;
				}
			}
		} else {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = display;
		int foreground = getForegroundPixel ();
		if (foreground != OS.GetTextColor (hDC)) data.foreground = foreground;
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		int background = control.getBackgroundPixel ();
		if (background != OS.GetBkColor (hDC)) data.background = background;
		data.font = font != null ? font : Font.win32_new (display, OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0));
		data.uiState = (int)/*64*/OS.SendMessage (hwnd, OS.WM_QUERYUISTATE, 0, 0);
	}
	return hDC;
}

/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Control</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data 
 */
public void internal_dispose_GC (int /*long*/ hDC, GCData data) {
	checkWidget ();
	int /*long*/ hwnd = handle;
	if (data != null && data.hwnd != 0) {
		hwnd = data.hwnd;
	}
	if (data == null || data.ps == null) {
		OS.ReleaseDC (hwnd, hDC);
	} else {
		OS.EndPaint (hwnd, data.ps);
	}
}

boolean isActive () {
	Dialog dialog = display.getModalDialog ();
	if (dialog != null) {
		Shell dialogShell = dialog.parent;
		if (dialogShell != null && !dialogShell.isDisposed ()) {
			if (dialogShell != getShell ()) return false;
		}
	}
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
					if (control != modal) return false;
					break;
				}
				if ((modal.style & SWT.PRIMARY_MODAL) != 0) {
					if (shell == null) shell = getShell ();
					if (modal.parent == shell) return false;
				}
			}
		}
	}
	if (shell == null) shell = getShell ();
	return shell.getEnabled ();
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are enabled.  Otherwise, <code>false</code> is returned.
 * A disabled control is typically not selectable from the user
 * interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget ();
	return getEnabled () && parent.isEnabled ();
}

/**
 * Returns <code>true</code> if the receiver has the user-interface
 * focus, and <code>false</code> otherwise.
 *
 * @return the receiver's focus state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isFocusControl () {
	checkWidget ();
	Control focusControl = display.focusControl;
	if (focusControl != null && !focusControl.isDisposed ()) {
		return this == focusControl;
	}
	return hasFocus ();
}

boolean isFocusAncestor (Control control) {
	while (control != null && control != this && !(control instanceof Shell)) {
		control = control.parent;
	}
	return control == this;
}

/**
 * Returns <code>true</code> if the underlying operating
 * system supports this reparenting, otherwise <code>false</code>
 *
 * @return <code>true</code> if the widget can be reparented, otherwise <code>false</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isReparentable () {
	checkWidget ();
	return true;
}

boolean isShowing () {
	/*
	* This is not complete.  Need to check if the
	* widget is obscured by a parent or sibling.
	*/
	if (!isVisible ()) return false;
	Control control = this;
	while (control != null) {
		Point size = control.getSize ();
		if (size.x == 0 || size.y == 0) {
			return false;
		}
		control = control.parent;
	}
	return true;
	/*
	* Check to see if current damage is included.
	*/
//	if (!OS.IsWindowVisible (handle)) return false;
//	int flags = OS.DCX_CACHE | OS.DCX_CLIPCHILDREN | OS.DCX_CLIPSIBLINGS;
//	int /*long*/ hDC = OS.GetDCEx (handle, 0, flags);
//	int result = OS.GetClipBox (hDC, new RECT ());
//	OS.ReleaseDC (handle, hDC);
//	return result != OS.NULLREGION;
}

boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.WS_TABSTOP) != 0;
}

boolean isTabItem () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return false;
		}
	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.WS_TABSTOP) != 0) return false;
	int /*long*/ code = OS.SendMessage (handle, OS.WM_GETDLGCODE, 0, 0);
	if ((code & OS.DLGC_STATIC) != 0) return false;
	if ((code & OS.DLGC_WANTALLKEYS) != 0) return false;
	if ((code & OS.DLGC_WANTARROWS) != 0) return false;
	if ((code & OS.DLGC_WANTTAB) != 0) return false;
	return true;
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * ancestors up to and including the receiver's nearest ancestor
 * shell are visible. Otherwise, <code>false</code> is returned.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget ();
	if (OS.IsWindowVisible (handle)) return true;
	return getVisible () && parent.isVisible ();
}

void mapEvent (int /*long*/ hwnd, Event event) {
	if (hwnd != handle) {
		POINT point = new POINT ();
		point.x = event.x;
		point.y = event.y;
		OS.MapWindowPoints (hwnd, handle, point, 1);
		event.x = point.x;
		event.y = point.y;
	}
}

void markLayout (boolean changed, boolean all) {
	/* Do nothing */
}

Decorations menuShell () {
	return parent.menuShell ();
}

boolean mnemonicHit (char key) {
	return false;
}

boolean mnemonicMatch (char key) {
	return false;
}

/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveBelow
 * @see Composite#getChildren
 */
public void moveAbove (Control control) {
	checkWidget ();
	int /*long*/ topHandle = topHandle (), hwndAbove = OS.HWND_TOP;
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		int /*long*/ hwnd = control.topHandle ();
		if (hwnd == 0 || hwnd == topHandle) return;
		hwndAbove = OS.GetWindow (hwnd, OS.GW_HWNDPREV);
		/*
		* Bug in Windows.  For some reason, when GetWindow ()
		* with GW_HWNDPREV is used to query the previous window
		* in the z-order with the first child, Windows returns
		* the first child instead of NULL.  The fix is to detect
		* this case and move the control to the top.
		*/
		if (hwndAbove == 0 || hwndAbove == hwnd) {
			hwndAbove = OS.HWND_TOP;
		}
	}
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
	SetWindowPos (topHandle, hwndAbove, 0, 0, 0, 0, flags);
}

/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param control the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Control#moveAbove
 * @see Composite#getChildren
 */
public void moveBelow (Control control) {
	checkWidget ();
	int /*long*/ topHandle = topHandle (), hwndAbove = OS.HWND_BOTTOM;
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		hwndAbove = control.topHandle ();
	} else {
		/*
		* Bug in Windows.  When SetWindowPos() is called
		* with HWND_BOTTOM on a dialog shell, the dialog
		* and the parent are moved to the bottom of the
		* desktop stack.  The fix is to move the dialog
		* to the bottom of the dialog window stack by
		* moving behind the first dialog child.
		*/
		Shell shell = getShell ();
		if (this == shell && parent != null) {
			/* 
			* Bug in Windows.  For some reason, when GetWindow ()
			* with GW_HWNDPREV is used to query the previous window
			* in the z-order with the first child, Windows returns
			* the first child instead of NULL.  The fix is to detect
			* this case and do nothing because the control is already
			* at the bottom.
			*/
			int /*long*/ hwndParent = parent.handle, hwnd = hwndParent;
			hwndAbove = OS.GetWindow (hwnd, OS.GW_HWNDPREV);
			while (hwndAbove != 0 && hwndAbove != hwnd) {
				if (OS.GetWindow (hwndAbove, OS.GW_OWNER) == hwndParent) break;
				hwndAbove = OS.GetWindow (hwnd = hwndAbove, OS.GW_HWNDPREV);
			}
			if (hwndAbove == hwnd) return;
		}
	}
	if (hwndAbove == 0 || hwndAbove == topHandle) return;
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
	SetWindowPos (topHandle, hwndAbove, 0, 0, 0, 0, flags);
}

Accessible new_Accessible (Control control) {
	return Accessible.internal_new_Accessible (this);
}

GC new_GC (GCData data) {
	return GC.win32_new (this, data);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
 */
public void pack () {
	checkWidget ();
	pack (true);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 * <p>
 * If the changed flag is <code>true</code>, it indicates that the receiver's
 * <em>contents</em> have changed, therefore any caches that a layout manager
 * containing the control may have been keeping need to be flushed. When the
 * control is resized, the changed flag will be <code>false</code>, so layout
 * manager caches can be retained. 
 * </p>
 *
 * @param changed whether or not the receiver's contents have changed
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize(int, int, boolean)
 */
public void pack (boolean changed) {
	checkWidget ();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

/**
 * Prints the receiver and all children.
 * 
 * @param gc the gc where the drawing occurs
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public boolean print (GC gc) {
	checkWidget ();
	if (gc == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		int /*long*/ topHandle = topHandle ();
		int /*long*/ hdc = gc.handle;
		int state = 0;
		int /*long*/ gdipGraphics = gc.getGCData().gdipGraphics;
		if (gdipGraphics != 0) {
			int /*long*/ clipRgn = 0;
			Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeNone);
			int /*long*/ rgn = Gdip.Region_new();
			if (rgn == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			Gdip.Graphics_GetClip(gdipGraphics, rgn);
			if (!Gdip.Region_IsInfinite(rgn, gdipGraphics)) {
				clipRgn = Gdip.Region_GetHRGN(rgn, gdipGraphics);
			}
			Gdip.Region_delete(rgn);
			Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeHalf);
			float[] lpXform = null;
			int /*long*/ matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
			if (matrix == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			Gdip.Graphics_GetTransform(gdipGraphics, matrix);
			if (!Gdip.Matrix_IsIdentity(matrix)) {
				lpXform = new float[6];
				Gdip.Matrix_GetElements(matrix, lpXform);
			}
			Gdip.Matrix_delete(matrix);
			hdc = Gdip.Graphics_GetHDC(gdipGraphics);
			state = OS.SaveDC(hdc);
			if (lpXform != null) {
				OS.SetGraphicsMode(hdc, OS.GM_ADVANCED);
				OS.SetWorldTransform(hdc, lpXform);
			}
			if (clipRgn != 0) {
				OS.SelectClipRgn(hdc, clipRgn);
				OS.DeleteObject(clipRgn);
			}
		}
		if (OS.IsWinCE) {
			OS.UpdateWindow (topHandle);
		} else {
			int flags = OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (topHandle, null, 0, flags);
		}
		printWidget (topHandle, hdc, gc);
		if (gdipGraphics != 0) {
			OS.RestoreDC(hdc, state);
			Gdip.Graphics_ReleaseHDC(gdipGraphics, hdc);
		}
		return true;
	}
	return false;
}

void printWidget (int /*long*/ hwnd, int /*long*/ hdc, GC gc) {
	/*
	* Bug in Windows.  For some reason, PrintWindow()
	* returns success but does nothing when it is called
	* on a printer.  The fix is to just go directly to
	* WM_PRINT in this case.
	*/
	boolean success = false;
	if (!(OS.GetDeviceCaps(gc.handle, OS.TECHNOLOGY) == OS.DT_RASPRINTER)) {
		/*
		* Bug in Windows.  When PrintWindow() will only draw that
		* portion of a control that is not obscured by the shell.
		* The fix is temporarily reparent the window to the desktop,
		* call PrintWindow() then reparent the window back.
		*/
		int /*long*/ hwndParent = OS.GetParent (hwnd);
		int /*long*/ hwndShell = hwndParent;
		while (OS.GetParent (hwndShell) != 0) {
			if (OS.GetWindow (hwndShell, OS.GW_OWNER) != 0) break;
			hwndShell = OS.GetParent (hwndShell);
		}
		RECT rect1 = new RECT ();
		OS.GetWindowRect (hwnd, rect1);
		boolean fixPrintWindow = !OS.IsWindowVisible(hwnd);
		if (!fixPrintWindow) {
			RECT rect2 = new RECT ();
			OS.GetWindowRect (hwndShell, rect2);
			OS.IntersectRect (rect2, rect1, rect2);
			fixPrintWindow = !OS.EqualRect (rect2, rect1);
		}
		/*
		* Bug in Windows. PrintWindow does not print portions
		* of the receiver that are removed by regions set int a
		* parent.  The fix is temporarily reparent the window to
		* the desktop, call PrintWindow() then reparent the window
		* back.
		*/
		if (!fixPrintWindow) {
			int /*long*/ rgn = OS.CreateRectRgn(0, 0, 0, 0);
			int /*long*/ parent = OS.GetParent(hwnd);
			while (parent != hwndShell && !fixPrintWindow) {
				if (OS.GetWindowRgn(parent, rgn) != 0) {
					fixPrintWindow = true;
				}
				parent = OS.GetParent(parent);
			}
			OS.DeleteObject(rgn);
		}
		int bits = OS.GetWindowLong (hwnd, OS.GWL_STYLE);
		int /*long*/ hwndInsertAfter = OS.GetWindow (hwnd, OS.GW_HWNDPREV);
		/*
		* Bug in Windows.  For some reason, when GetWindow ()
		* with GW_HWNDPREV is used to query the previous window
		* in the z-order with the first child, Windows returns
		* the first child instead of NULL.  The fix is to detect
		* this case and move the control to the top.
		*/
		if (hwndInsertAfter == 0 || hwndInsertAfter == hwnd) {
			hwndInsertAfter = OS.HWND_TOP;
		}
		if (fixPrintWindow) {
			int x = OS.GetSystemMetrics (OS.SM_XVIRTUALSCREEN);
			int y = OS.GetSystemMetrics (OS.SM_YVIRTUALSCREEN);	
			int width = OS.GetSystemMetrics (OS.SM_CXVIRTUALSCREEN);
			int height = OS.GetSystemMetrics (OS.SM_CYVIRTUALSCREEN);
			int flags = OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_DRAWFRAME;
			if ((bits & OS.WS_VISIBLE) != 0) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 0, 0);
			}
			SetWindowPos (hwnd, 0, x + width, y + height, 0, 0, flags);
			OS.SetParent (hwnd, 0);
			if ((bits & OS.WS_VISIBLE) != 0) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 1, 0);
			}
		}
		if ((bits & OS.WS_VISIBLE) == 0) {
			OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 1, 0);
		}
		success = OS.PrintWindow (hwnd, hdc, 0);
		if ((bits & OS.WS_VISIBLE) == 0) {
			OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 0, 0);
		}
		if (fixPrintWindow) {
			if ((bits & OS.WS_VISIBLE) != 0) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 0, 0);
			}
			OS.SetParent (hwnd, hwndParent);
			OS.MapWindowPoints (0, hwndParent, rect1, 2);
			int flags = OS.SWP_NOSIZE | OS.SWP_NOACTIVATE | OS.SWP_DRAWFRAME;
			SetWindowPos (hwnd, hwndInsertAfter, rect1.left, rect1.top, rect1.right - rect1.left, rect1.bottom - rect1.top, flags);
			if ((bits & OS.WS_VISIBLE) != 0) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 1, 0);
			}
		}
	}
	
	/*
	* Bug in Windows.  For some reason, PrintWindow() fails
	* when it is called on a push button.  The fix is to
	* detect the failure and use WM_PRINT instead.  Note
	* that WM_PRINT cannot be used all the time because it
	* fails for browser controls when the browser has focus.
	*/
	if (!success) {
		int flags = OS.PRF_CLIENT | OS.PRF_NONCLIENT | OS.PRF_ERASEBKGND | OS.PRF_CHILDREN;
		OS.SendMessage (hwnd, OS.WM_PRINT, hdc, flags);
	}
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted,
 * including the background.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update()
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#DOUBLE_BUFFERED
 */
public void redraw () {
	checkWidget ();
	redraw (false);
}

void redraw (boolean all) {
//	checkWidget ();
	if (!OS.IsWindowVisible (handle)) return;
	if (OS.IsWinCE) {
		OS.InvalidateRect (handle, null, true);
	} else {
		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
		if (all) flags |= OS.RDW_ALLCHILDREN;
		OS.RedrawWindow (handle, null, 0, flags);
	}
}
/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn. 
 * The next time a paint request is processed, that area of
 * the receiver will be painted, including the background.
 * If the <code>all</code> flag is <code>true</code>, any
 * children of the receiver which intersect with the specified
 * area will also paint their intersecting areas. If the
 * <code>all</code> flag is <code>false</code>, the children
 * will not be painted.
 *
 * @param x the x coordinate of the area to draw
 * @param y the y coordinate of the area to draw
 * @param width the width of the area to draw
 * @param height the height of the area to draw
 * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update()
 * @see PaintListener
 * @see SWT#Paint
 * @see SWT#NO_BACKGROUND
 * @see SWT#NO_REDRAW_RESIZE
 * @see SWT#NO_MERGE_PAINTS
 * @see SWT#DOUBLE_BUFFERED
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget ();
	if (width <= 0 || height <= 0) return;
	if (!OS.IsWindowVisible (handle)) return;
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	if (OS.IsWinCE) {
		OS.InvalidateRect (handle, rect, true);
	} else {
		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
		if (all) flags |= OS.RDW_ALLCHILDREN;
		OS.RedrawWindow (handle, rect, 0, flags);
	}
}

boolean redrawChildren () {
	if (!OS.IsWindowVisible (handle)) return false;
	Control control = findBackgroundControl ();
	if (control == null) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
				OS.InvalidateRect (handle, null, true);
				return true;
			}
		}
	} else {
		if (control.backgroundImage != null) {
			OS.InvalidateRect (handle, null, true);
			return true;
		}
	}
	return false;
}

void register () {
	display.addControl (handle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
	parent = null;
}

void releaseParent () {
	parent.removeControl (this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (OS.IsDBLocale) {
		OS.ImmAssociateContext (handle, 0);
	}
	if (toolTipText != null) {
		setToolTipText (getShell (), null);
	}
	toolTipText = null;
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	backgroundImage = null;
	menu = null;
	cursor = null;
	unsubclass ();
	deregister ();
	layoutData = null;
	if (accessible != null) {
		accessible.internal_dispose_Accessible ();
	}
	accessible = null;
	region = null;
	font = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when a drag gesture occurs.
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
 * @see DragDetectListener
 * @see #addDragDetectListener
 * 
 * @since 3.3
 */
public void removeDragDetectListener(DragDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.DragDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control gains or loses focus.
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
 * @see FocusListener
 * @see #addFocusListener
 */
public void removeFocusListener(FocusListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.FocusIn, listener);
	eventTable.unhook (SWT.FocusOut, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see HelpListener
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard.
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
 * @see KeyListener
 * @see #addKeyListener
 */
public void removeKeyListener(KeyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the platform-specific context menu trigger has
 * occurred.
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
 * @see MenuDetectListener
 * @see #addMenuDetectListener
 *
 * @since 3.3
 */
public void removeMenuDetectListener (MenuDetectListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MenuDetect, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse passes or hovers over controls.
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
 * @see MouseTrackListener
 * @see #addMouseTrackListener
 */
public void removeMouseTrackListener(MouseTrackListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when mouse buttons are pressed and released.
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
 * @see MouseListener
 * @see #addMouseListener
 */
public void removeMouseListener (MouseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseDown, listener);
	eventTable.unhook (SWT.MouseUp, listener);
	eventTable.unhook (SWT.MouseDoubleClick, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse moves.
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
 * @see MouseMoveListener
 * @see #addMouseMoveListener
 */
public void removeMouseMoveListener(MouseMoveListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseMove, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse wheel is scrolled.
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
 * @see MouseWheelListener
 * @see #addMouseWheelListener
 *
 * @since 3.3
 */
public void removeMouseWheelListener (MouseWheelListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseWheel, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver needs to be painted.
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
 * @see PaintListener
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see TraverseListener
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

void showWidget (boolean visible) {
	int /*long*/ topHandle = topHandle ();
	OS.ShowWindow (topHandle, visible ? OS.SW_SHOW : OS.SW_HIDE);
	if (handle != topHandle) OS.ShowWindow (handle, visible ? OS.SW_SHOW : OS.SW_HIDE);
}

boolean sendFocusEvent (int type) {
	Shell shell = getShell ();
	
	/*
	* Feature in Windows.  During the processing of WM_KILLFOCUS,
	* when the focus window is queried using GetFocus(), it has
	* already been assigned to the new window.  The fix is to
	* remember the control that is losing or gaining focus and
	* answer it during WM_KILLFOCUS.  If a WM_SETFOCUS occurs
	* during WM_KILLFOCUS, the focus control needs to be updated
	* to the current control.  At any other time, the focus
	* control matches Windows.
	*/
	Display display = this.display;
	display.focusEvent = type;
	display.focusControl = this;
	sendEvent (type);
	// widget could be disposed at this point
	display.focusEvent = SWT.None;
	display.focusControl = null;

	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/	
	if (!shell.isDisposed ()) {
		switch (type) {
			case SWT.FocusIn:
				shell.setActiveControl (this);
				break;
			case SWT.FocusOut:
				if (shell != display.getActiveShell ()) {
					shell.setActiveControl (null);
				}
				break;
		}
	}
	return true;
}

void sendMove () {
	sendEvent (SWT.Move);
}

void sendResize () {
	sendEvent (SWT.Resize);
}

void setBackground () {
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	if (control.backgroundImage != null) {
		Shell shell = getShell ();
		shell.releaseBrushes ();
		setBackgroundImage (control.backgroundImage.handle);
	} else {
		setBackgroundPixel (control.background == -1 ? control.defaultBackground() : control.background);
	}
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on Windows the background of a Button cannot be changed.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBackground (Color color) {
	checkWidget ();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	if (pixel == background) return;
	background = pixel;
	updateBackgroundColor ();
}

/**
 * Sets the receiver's background image to the image specified
 * by the argument, or to the default system color for the control
 * if the argument is null.  The background image is tiled to fill
 * the available space.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on Windows the background of a Button cannot be changed.
 * </p>
 * @param image the new image (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 *    <li>ERROR_INVALID_ARGUMENT - if the argument is not a bitmap</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setBackgroundImage (Image image) {
	checkWidget ();
	if (image != null) {
		if (image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (image.type != SWT.BITMAP) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (backgroundImage == image) return;
	backgroundImage = image;
	Shell shell = getShell ();
	shell.releaseBrushes ();
	updateBackgroundImage ();
}

void setBackgroundImage (int /*long*/ hBitmap) {
	if (OS.IsWinCE) {
		OS.InvalidateRect (handle, null, true);
	} else {
		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
		OS.RedrawWindow (handle, null, 0, flags);
	}
}

void setBackgroundPixel (int pixel) {
	if (OS.IsWinCE) {
		OS.InvalidateRect (handle, null, true);
	} else {
		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
		OS.RedrawWindow (handle, null, 0, flags);
	}
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and 
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the <code>x</code>
 * and <code>y</code> arguments are relative to the display.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (int x, int y, int width, int height) {
	checkWidget ();
	int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
	setBounds (x, y, Math.max (0, width), Math.max (0, height), flags);
}

void setBounds (int x, int y, int width, int height, int flags) {
	setBounds (x, y, width, height, flags, true);
}

void setBounds (int x, int y, int width, int height, int flags, boolean defer) {
	if (findImageControl () != null) {
		if (backgroundImage == null) flags |= OS.SWP_NOCOPYBITS;
	} else {
		if (OS.GetWindow (handle, OS.GW_CHILD) == 0) {
			if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
				if (findThemeControl () != null) flags |= OS.SWP_NOCOPYBITS;
			}
		}
	}
	int /*long*/ topHandle = topHandle ();
	if (defer && parent != null) {
		forceResize ();
		if (parent.lpwp != null) {
			int index = 0;
			WINDOWPOS [] lpwp = parent.lpwp;
			while (index < lpwp.length) {
				if (lpwp [index] == null) break;
				index ++;
			}
			if (index == lpwp.length) {
				WINDOWPOS [] newLpwp = new WINDOWPOS [lpwp.length + 4];
				System.arraycopy (lpwp, 0, newLpwp, 0, lpwp.length);
				parent.lpwp = lpwp = newLpwp;
			}
			WINDOWPOS wp = new WINDOWPOS ();
			wp.hwnd = topHandle;
			wp.x = x;
			wp.y = y;
			wp.cx = width;
			wp.cy = height;
			wp.flags = flags;
			lpwp [index] = wp;
			return;
		}
	}
	SetWindowPos (topHandle, 0, x, y, width, height, flags);
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the argument. The <code>x</code> and 
 * <code>y</code> fields of the rectangle are relative to
 * the receiver's parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param rect the new bounds for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setBounds (Rectangle rect) {
	checkWidget ();
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

/**
 * If the argument is <code>true</code>, causes the receiver to have
 * all mouse events delivered to it until the method is called with
 * <code>false</code> as the argument.  Note that on some platforms,
 * a mouse button must currently be down for capture to be assigned.
 *
 * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCapture (boolean capture) {
	checkWidget ();
	if (capture) {
		OS.SetCapture (handle);
	} else {
		if (OS.GetCapture () == handle) {
			OS.ReleaseCapture ();
		}
	}
}

void setCursor () {
	int /*long*/ lParam = OS.MAKELPARAM (OS.HTCLIENT, OS.WM_MOUSEMOVE);
	OS.SendMessage (handle, OS.WM_SETCURSOR, handle, lParam);
}

/**
 * Sets the receiver's cursor to the cursor specified by the
 * argument, or to the default cursor for that kind of control
 * if the argument is null.
 * <p>
 * When the mouse pointer passes over a control its appearance
 * is changed to match the control's cursor.
 * </p>
 *
 * @param cursor the new cursor (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCursor (Cursor cursor) {
	checkWidget ();
	if (cursor != null && cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	if (OS.IsWinCE) {
		int /*long*/ hCursor = cursor != null ? cursor.handle : 0;
		OS.SetCursor (hCursor);
		return;
	}
	int /*long*/ hwndCursor = OS.GetCapture ();
	if (hwndCursor == 0) {
		POINT pt = new POINT ();
		if (!OS.GetCursorPos (pt)) return;
		int /*long*/ hwnd = hwndCursor = OS.WindowFromPoint (pt);
		while (hwnd != 0 && hwnd != handle) {
			hwnd = OS.GetParent (hwnd);
		}
		if (hwnd == 0) return;
	}
	Control control = display.getControl (hwndCursor);
	if (control == null) control = this;
	control.setCursor ();
}

void setDefaultFont () {
	int /*long*/ hFont = display.getSystemFont ().handle;
	OS.SendMessage (handle, OS.WM_SETFONT, hFont, 0);
}

/**
 * Sets the receiver's drag detect state. If the argument is
 * <code>true</code>, the receiver will detect drag gestures,
 * otherwise these gestures will be ignored.
 *
 * @param dragDetect the new drag detect state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public void setDragDetect (boolean dragDetect) {
	checkWidget ();
	if (dragDetect) {
		state |= DRAG_DETECT;
	} else {
		state &= ~DRAG_DETECT;
	}
	enableDrag (dragDetect);
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget ();
	/*
	* Feature in Windows.  If the receiver has focus, disabling
	* the receiver causes no window to have focus.  The fix is
	* to assign focus to the first ancestor window that takes
	* focus.  If no window will take focus, set focus to the
	* desktop.
	*/
	Control control = null;
	boolean fixFocus = false;
	if (!enabled) {
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
	}
	enableWidget (enabled);
	if (fixFocus) fixFocus (control);
}

boolean setFixedFocus () {
	if ((style & SWT.NO_FOCUS) != 0) return false;
	return forceFocus ();
}

/**
 * Causes the receiver to have the <em>keyboard focus</em>, 
 * such that all keyboard events will be delivered to it.  Focus
 * reassignment will respect applicable platform constraints.
 *
 * @return <code>true</code> if the control got focus, and <code>false</code> if it was unable to.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #forceFocus
 */
public boolean setFocus () {
	checkWidget ();
	if ((style & SWT.NO_FOCUS) != 0) return false;
	return forceFocus ();
}

/**
 * Sets the font that the receiver will use to paint textual information
 * to the font specified by the argument, or to the default font for that
 * kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setFont (Font font) {
	checkWidget ();
	int /*long*/ hFont = 0;
	if (font != null) { 
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		hFont = font.handle;
	}
	this.font = font;
	if (hFont == 0) hFont = defaultFont ();
	OS.SendMessage (handle, OS.WM_SETFONT, hFont, 1);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setForeground (Color color) {
	checkWidget ();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	if (pixel == foreground) return;
	foreground = pixel;
	setForegroundPixel (pixel);
}

void setForegroundPixel (int pixel) {
	OS.InvalidateRect (handle, null, true);
}

/**
 * Sets the layout data associated with the receiver to the argument.
 * 
 * @param layoutData the new layout data for the receiver.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayoutData (Object layoutData) {
	checkWidget ();
	this.layoutData = layoutData;
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
	int flags = OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE;
	/*
	* Feature in WinCE.  The SWP_DRAWFRAME flag for SetWindowPos()
	* causes a WM_SIZE message to be sent even when the SWP_NOSIZE
	* flag is specified.  The fix is to set SWP_DRAWFRAME only when
	* not running on WinCE.
	*/
	if (!OS.IsWinCE) flags |= OS.SWP_DRAWFRAME;
	setBounds (x, y, 0, 0, flags);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null), unless 
 * the receiver is a shell. In this case, the point is 
 * relative to the display. 
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
 * <p>
 * Note: Disposing of a control that has a pop up menu will
 * dispose of the menu.  To avoid this behavior, set the
 * menu to null before the control is disposed.
 * </p>
 *
 * @param menu the new pop up menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
 *    <li>ERROR_INVALID_PARENT - if the menu is not in the same widget tree</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget ();
	if (menu != null) {
		if (menu.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
		if (menu.parent != menuShell ()) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	this.menu = menu;
}

boolean setRadioFocus (boolean tabbing) {
	return false;
}

boolean setRadioSelection (boolean value) {
	return false;
}

/**
 * If the argument is <code>false</code>, causes subsequent drawing
 * operations in the receiver to be ignored. No drawing of any kind
 * can occur in the receiver until the flag is set to true.
 * Graphics operations that occurred while the flag was
 * <code>false</code> are lost. When the flag is set to <code>true</code>,
 * the entire widget is marked as needing to be redrawn.  Nested calls
 * to this method are stacked.
 * <p>
 * Note: This operation is a hint and may not be supported on some
 * platforms or for some widgets.
 * </p>
 *
 * @param redraw the new redraw state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #redraw(int, int, int, int, boolean)
 * @see #update()
 */
public void setRedraw (boolean redraw) {
	checkWidget ();
	/*
	 * Feature in Windows.  When WM_SETREDRAW is used to turn
	 * off drawing in a widget, it clears the WS_VISIBLE bits
	 * and then sets them when redraw is turned back on.  This
	 * means that WM_SETREDRAW will make a widget unexpectedly
	 * visible.  The fix is to track the visibility state while
	 * drawing is turned off and restore it when drawing is
	 * turned back on.
	 */
	if (drawCount == 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & OS.WS_VISIBLE) == 0) state |= HIDDEN;
	}
	if (redraw) {
		if (--drawCount == 0) {
			int /*long*/ topHandle = topHandle ();
			OS.SendMessage (topHandle, OS.WM_SETREDRAW, 1, 0);
			if (handle != topHandle) OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			if ((state & HIDDEN) != 0) {
				state &= ~HIDDEN;
				OS.ShowWindow (topHandle, OS.SW_HIDE);
				if (handle != topHandle) OS.ShowWindow (handle, OS.SW_HIDE);
			} else {
				if (OS.IsWinCE) {
					OS.InvalidateRect (topHandle, null, true);
					if (handle != topHandle) OS.InvalidateRect (handle, null, true);
				} else {
					int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
					OS.RedrawWindow (topHandle, null, 0, flags);
				}
			}
		}
	} else {
		if (drawCount++ == 0) {
			int /*long*/ topHandle = topHandle ();
			OS.SendMessage (topHandle, OS.WM_SETREDRAW, 0, 0);
			if (handle != topHandle) OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		}
	}
}

/**
 * Sets the shape of the control to the region specified
 * by the argument.  When the argument is null, the
 * default shape of the control is restored.
 *
 * @param region the region that defines the shape of the control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul>  
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setRegion (Region region) {
	checkWidget ();
	if (region != null && region.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	int /*long*/ hRegion = 0;
	if (region != null) {
		hRegion = OS.CreateRectRgn (0, 0, 0, 0);
		OS.CombineRgn (hRegion, region.handle, hRegion, OS.RGN_OR);
	}
	OS.SetWindowRgn (handle, hRegion, true);
	this.region = region;
}

boolean setSavedFocus () {
	return forceFocus ();
}

/**
 * Sets the receiver's size to the point specified by the arguments.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 *
 * @param width the new width for the receiver
 * @param height the new height for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (int width, int height) {
	checkWidget ();
	int flags = OS.SWP_NOMOVE | OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
	setBounds (0, 0, Math.max (0, width), Math.max (0, height), flags);
}

/**
 * Sets the receiver's size to the point specified by the argument.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause them to be
 * set to zero instead.
 * </p>
 *
 * @param size the new size for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (Point size) {
	checkWidget ();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}

boolean setTabItemFocus () {
	if (!isShowing ()) return false;
	return forceFocus ();
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
 * 
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget ();
	toolTipText = string;
	setToolTipText (getShell (), string);
}

void setToolTipText (Shell shell, String string) {
	shell.setToolTipText (handle, string);
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if (!getDrawing()) {
		if (((state & HIDDEN) == 0) == visible) return;
	} else {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if (((bits & OS.WS_VISIBLE) != 0) == visible) return;
	}
	if (visible) {
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
	}
	
	/*
	* Feature in Windows.  If the receiver has focus, hiding
	* the receiver causes no window to have focus.  The fix is
	* to assign focus to the first ancestor window that takes
	* focus.  If no window will take focus, set focus to the
	* desktop.
	*/
	Control control = null;
	boolean fixFocus = false;
	if (!visible) {
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
	}
	if (!getDrawing()) {
		state = visible ? state & ~HIDDEN : state | HIDDEN;
	} else {
		showWidget (visible);
		if (isDisposed ()) return;
	}
	if (!visible) {
		sendEvent (SWT.Hide);
		if (isDisposed ()) return;
	}
	if (fixFocus) fixFocus (control);
}

void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

void subclass () {
	int /*long*/ oldProc = windowProc ();
	int /*long*/ newProc = display.windowProc;
	if (oldProc == newProc) return;
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, newProc);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toControl (int x, int y) {
	checkWidget ();
	POINT pt = new POINT ();
	pt.x = x;  pt.y = y; 
	OS.ScreenToClient (handle, pt);
	return new Point (pt.x, pt.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toControl (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toControl (point.x, point.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param x the x coordinate to be translated
 * @param y the y coordinate to be translated
 * @return the translated coordinates
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public Point toDisplay (int x, int y) {
	checkWidget ();
	POINT pt = new POINT ();
	pt.x = x;  pt.y = y; 
	OS.ClientToScreen (handle, pt);
	return new Point (pt.x, pt.y);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param point the point to be translated (must not be null)
 * @return the translated coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point toDisplay (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toDisplay (point.x, point.y);
}

int /*long*/ topHandle () {
	return handle;
}

boolean translateAccelerator (MSG msg) {
	return menuShell ().translateAccelerator (msg);
}

boolean translateMnemonic (Event event, Control control) {
	if (control == this) return false;
	if (!isVisible () || !isEnabled ()) return false;
	event.doit = mnemonicMatch (event.character);
	return traverse (event);
}

boolean translateMnemonic (MSG msg) {
	if (msg.wParam < 0x20) return false;
	int /*long*/ hwnd = msg.hwnd;
	if (OS.GetKeyState (OS.VK_MENU) >= 0) {
		int /*long*/ code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
		if ((code & OS.DLGC_WANTALLKEYS) != 0) return false;
		if ((code & OS.DLGC_BUTTON) == 0) return false;
	}
	Decorations shell = menuShell ();
	if (shell.isVisible () && shell.isEnabled ()) {
		display.lastAscii = (int)/*64*/msg.wParam;
		display.lastNull = display.lastDead = false;
		Event event = new Event ();
		event.detail = SWT.TRAVERSE_MNEMONIC;
		if (setKeyState (event, SWT.Traverse, msg.wParam, msg.lParam)) {
			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
		}
	}
	return false;
}

boolean translateTraversal (MSG msg) {
	int /*long*/ hwnd = msg.hwnd;
	int key = (int)/*64*/msg.wParam;
	if (key == OS.VK_MENU) {
		OS.SendMessage (hwnd, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
		return false;
	}
	int detail = SWT.TRAVERSE_NONE;
	boolean doit = true, all = false;
	boolean lastVirtual = false;
	int lastKey = key, lastAscii = 0;
	switch (key) {
		case OS.VK_ESCAPE: {
			all = true;
			lastAscii = 27;
			int /*long*/ code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
			if ((code & OS.DLGC_WANTALLKEYS) != 0) {
				/*
				* Use DLGC_HASSETSEL to determine that the control
				* is a text widget.  A text widget normally wants
				* all keys except VK_ESCAPE.  If this bit is not
				* set, then assume the control wants all keys,
				* including VK_ESCAPE.
				*/
				if ((code & OS.DLGC_HASSETSEL) == 0) doit = false;
			}
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		}
		case OS.VK_RETURN: {
			all = true;
			lastAscii = '\r';
			int /*long*/ code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
			if ((code & OS.DLGC_WANTALLKEYS) != 0) doit = false;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		case OS.VK_TAB: {
			lastAscii = '\t';
			boolean next = OS.GetKeyState (OS.VK_SHIFT) >= 0;
			int /*long*/ code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
			if ((code & (OS.DLGC_WANTTAB | OS.DLGC_WANTALLKEYS)) != 0) {
				/*
				* Use DLGC_HASSETSEL to determine that the control is a
				* text widget.  If the control is a text widget, then
				* Ctrl+Tab and Shift+Tab should traverse out of the widget.
				* If the control is not a text widget, the correct behavior
				* is to give every character, including Tab, Ctrl+Tab and
				* Shift+Tab to the control.
				*/
				if ((code & OS.DLGC_HASSETSEL) != 0) {
					if (next && OS.GetKeyState (OS.VK_CONTROL) >= 0) {
						doit = false;
					}
				} else {
					doit = false;
				}
			}
			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		}
		case OS.VK_UP:
		case OS.VK_LEFT:
		case OS.VK_DOWN:
		case OS.VK_RIGHT: {
			/*
			* On WinCE SP there is no tab key.  Focus is assigned
			* using the VK_UP and VK_DOWN keys, not with VK_LEFT
			* or VK_RIGHT.
			*/
			if (OS.IsSP) {
				if (key == OS.VK_LEFT || key == OS.VK_RIGHT) return false;
			}
			lastVirtual = true;
			int /*long*/ code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
			if ((code & (OS.DLGC_WANTARROWS /*| OS.DLGC_WANTALLKEYS*/)) != 0) doit = false;
			boolean next = key == OS.VK_DOWN || key == OS.VK_RIGHT;
			if (parent != null && (parent.style & SWT.MIRRORED) != 0) {
				if (key == OS.VK_LEFT || key == OS.VK_RIGHT) next = !next;
			}
			detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		}
		case OS.VK_PRIOR:
		case OS.VK_NEXT: {
			all = true;
			lastVirtual = true;
			if (OS.GetKeyState (OS.VK_CONTROL) >= 0) return false;
			int /*long*/ code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
			if ((code & OS.DLGC_WANTALLKEYS) != 0) {
				/*
				* Use DLGC_HASSETSEL to determine that the control is a
				* text widget.  If the control is a text widget, then
				* Ctrl+PgUp and Ctrl+PgDn should traverse out of the widget.
				*/
				if ((code & OS.DLGC_HASSETSEL) == 0) doit = false;
			}
			detail = key == OS.VK_PRIOR ? SWT.TRAVERSE_PAGE_PREVIOUS : SWT.TRAVERSE_PAGE_NEXT;
			break;
		}
		default:
			return false;
	}
	Event event = new Event ();
	event.doit = doit;
	event.detail = detail;
	display.lastKey = lastKey;
	display.lastAscii = lastAscii;
	display.lastVirtual = lastVirtual;
	display.lastNull = display.lastDead = false;
	if (!setKeyState (event, SWT.Traverse, msg.wParam, msg.lParam)) return false;
	Shell shell = getShell ();
	Control control = this;
	do {
		if (control.traverse (event)) {
			OS.SendMessage (hwnd, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			return true;
		}
		if (!event.doit && control.hooks (SWT.Traverse)) return false;
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
}

boolean traverse (Event event) {
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the traverse
	* event.  If this happens, return true to stop further
	* event processing.
	*/	
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return true;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:			return true;
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:	return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:		return traverseMnemonic (event.character);	
		case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
	}
	return false;
}

/**
 * Based on the argument, perform one of the expected platform
 * traversal action. The argument should be one of the constants:
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>, 
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>, 
 * <code>SWT.TRAVERSE_ARROW_NEXT</code> and <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>.
 *
 * @param traversal the type of traversal
 * @return true if the traversal succeeded
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean traverse (int traversal) {
	checkWidget ();
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

boolean traverseEscape () {
	return false;
}

boolean traverseGroup (boolean next) {
	Control root = computeTabRoot ();
	Widget group = computeTabGroup ();
	Widget [] list = root.computeTabList ();
	int length = list.length;
	int index = 0;
	while (index < length) {
		if (list [index] == group) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = ((index + offset + length) % length)) != start) {
		Widget widget = list [index];
		if (!widget.isDisposed () && widget.setTabGroupFocus ()) {
			return true;
		}
	}
	if (group.isDisposed ()) return false;
	return group.setTabGroupFocus ();
}

boolean traverseItem (boolean next) {
	Control [] children = parent._getChildren ();
	int length = children.length;
	int index = 0;
	while (index < length) {
		if (children [index] == this) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	if (index == length) return false;
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		Control child = children [index];
		if (!child.isDisposed () && child.isTabItem ()) {
			if (child.setTabItemFocus ()) return true;
		}
	}
	return false;
}

boolean traverseMnemonic (char key) {
	if (mnemonicHit (key)) {
		OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
		return true;
	}
	return false;
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseReturn () {
	return false;
}

void unsubclass () {
	int /*long*/ newProc = windowProc ();
	int /*long*/ oldProc = display.windowProc;
	if (oldProc == newProc) return;
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, newProc);
}

/**
 * Forces all outstanding paint requests for the widget
 * to be processed before this method returns. If there
 * are no outstanding paint request, this method does
 * nothing.
 * <p>
 * Note: This method does not cause a redraw.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw()
 * @see #redraw(int, int, int, int, boolean)
 * @see PaintListener
 * @see SWT#Paint
 */
public void update () {
	checkWidget ();
	update (false);
}

void update (boolean all) {
//	checkWidget ();
	if (OS.IsWinCE) {
		OS.UpdateWindow (handle);
	} else {
		int flags = OS.RDW_UPDATENOW;
		if (all) flags |= OS.RDW_ALLCHILDREN;
		OS.RedrawWindow (handle, null, 0, flags);
	}
}

void updateBackgroundColor () {
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	setBackgroundPixel (control.background);
}

void updateBackgroundImage () {
	Control control = findBackgroundControl ();
	Image image = control != null ? control.backgroundImage : backgroundImage;
	setBackgroundImage (image != null ? image.handle : 0);
}

void updateBackgroundMode () {
	int oldState = state & PARENT_BACKGROUND;
	checkBackground ();
	if (oldState != (state & PARENT_BACKGROUND)) {
		setBackground ();
	}
}

void updateFont (Font oldFont, Font newFont) {
	if (getFont ().equals (oldFont)) setFont (newFont);
}

void updateImages () {
	/* Do nothing */
}

void updateLayout (boolean resize, boolean all) {
	/* Do nothing */
}

CREATESTRUCT widgetCreateStruct () {
	return null;
}

int widgetExtStyle () {
	int bits = 0;
	if (!OS.IsPPC) {
		if ((style & SWT.BORDER) != 0) bits |= OS.WS_EX_CLIENTEDGE;
	}
//	if ((style & SWT.BORDER) != 0) {
//		if ((style & SWT.FLAT) == 0) bits |= OS.WS_EX_CLIENTEDGE;
//	}
	/*
	* Feature in Windows NT.  When CreateWindowEx() is called with
	* WS_EX_LAYOUTRTL or WS_EX_NOINHERITLAYOUT, CreateWindowEx()
	* fails to create the HWND. The fix is to not use these bits.
	*/
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) {
		return bits;
	} 
	bits |= OS.WS_EX_NOINHERITLAYOUT;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) bits |= OS.WS_EX_LAYOUTRTL;
	return bits;
}

int /*long*/ widgetParent () {
	return parent.handle;
}

int widgetStyle () {
	/* Force clipping of siblings by setting WS_CLIPSIBLINGS */
	int bits = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_CLIPSIBLINGS;
//	if ((style & SWT.BORDER) != 0) {
//		if ((style & SWT.FLAT) != 0) bits |= OS.WS_BORDER;
//	}
	if (OS.IsPPC) {
		if ((style & SWT.BORDER) != 0) bits |= OS.WS_BORDER;
	}
	return bits;
	
	/*
	* This code is intentionally commented.  When clipping
	* of both siblings and children is not enforced, it is
	* possible for application code to draw outside of the
	* control.
	*/
//	int bits = OS.WS_CHILD | OS.WS_VISIBLE;
//	if ((style & SWT.CLIP_SIBLINGS) != 0) bits |= OS.WS_CLIPSIBLINGS;
//	if ((style & SWT.CLIP_CHILDREN) != 0) bits |= OS.WS_CLIPCHILDREN;
//	return bits;
}

/**
 * Changes the parent of the widget to be the one provided if
 * the underlying operating system supports this feature.
 * Returns <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is <code>null</code></li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	checkWidget ();
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	releaseParent ();
	Shell newShell = parent.getShell (), oldShell = getShell ();
	Decorations newDecorations = parent.menuShell (), oldDecorations = menuShell ();
	if (oldShell != newShell || oldDecorations != newDecorations) {
		Menu [] menus = oldShell.findMenus (this);
		fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	}
	int /*long*/ topHandle = topHandle ();
	if (OS.SetParent (topHandle, parent.handle) == 0) return false;
	this.parent = parent;
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE; 
	SetWindowPos (topHandle, OS.HWND_BOTTOM, 0, 0, 0, 0, flags);
	return true;
}

abstract TCHAR windowClass ();

abstract int /*long*/ windowProc ();

int /*long*/ windowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = null;
	switch (msg) {
		case OS.WM_ACTIVATE:			result = WM_ACTIVATE (wParam, lParam); break;
		case OS.WM_CAPTURECHANGED:		result = WM_CAPTURECHANGED (wParam, lParam); break;
		case OS.WM_CHANGEUISTATE:		result = WM_CHANGEUISTATE (wParam, lParam); break;
		case OS.WM_CHAR:				result = WM_CHAR (wParam, lParam); break;
		case OS.WM_CLEAR:				result = WM_CLEAR (wParam, lParam); break;
		case OS.WM_CLOSE:				result = WM_CLOSE (wParam, lParam); break;
		case OS.WM_COMMAND:				result = WM_COMMAND (wParam, lParam); break;
		case OS.WM_CONTEXTMENU:			result = WM_CONTEXTMENU (wParam, lParam); break;
		case OS.WM_CTLCOLORBTN:
		case OS.WM_CTLCOLORDLG:
		case OS.WM_CTLCOLOREDIT:
		case OS.WM_CTLCOLORLISTBOX:
		case OS.WM_CTLCOLORMSGBOX:
		case OS.WM_CTLCOLORSCROLLBAR:
		case OS.WM_CTLCOLORSTATIC:		result = WM_CTLCOLOR (wParam, lParam); break;
		case OS.WM_CUT:					result = WM_CUT (wParam, lParam); break;
		case OS.WM_DESTROY:				result = WM_DESTROY (wParam, lParam); break;
		case OS.WM_DRAWITEM:			result = WM_DRAWITEM (wParam, lParam); break;
		case OS.WM_ENDSESSION:			result = WM_ENDSESSION (wParam, lParam); break;
		case OS.WM_ENTERIDLE:			result = WM_ENTERIDLE (wParam, lParam); break;
		case OS.WM_ERASEBKGND:			result = WM_ERASEBKGND (wParam, lParam); break;
		case OS.WM_GETDLGCODE:			result = WM_GETDLGCODE (wParam, lParam); break;
		case OS.WM_GETFONT:				result = WM_GETFONT (wParam, lParam); break;
		case OS.WM_GETOBJECT:			result = WM_GETOBJECT (wParam, lParam); break;
		case OS.WM_GETMINMAXINFO:		result = WM_GETMINMAXINFO (wParam, lParam); break;
		case OS.WM_HELP:				result = WM_HELP (wParam, lParam); break;
		case OS.WM_HSCROLL:				result = WM_HSCROLL (wParam, lParam); break;
		case OS.WM_IME_CHAR:			result = WM_IME_CHAR (wParam, lParam); break;
		case OS.WM_IME_COMPOSITION:		result = WM_IME_COMPOSITION (wParam, lParam); break;
		case OS.WM_IME_COMPOSITION_START:	result = WM_IME_COMPOSITION_START (wParam, lParam); break;
		case OS.WM_IME_ENDCOMPOSITION:	result = WM_IME_ENDCOMPOSITION (wParam, lParam); break;
		case OS.WM_INITMENUPOPUP:		result = WM_INITMENUPOPUP (wParam, lParam); break;
		case OS.WM_INPUTLANGCHANGE:		result = WM_INPUTLANGCHANGE (wParam, lParam); break;
		case OS.WM_HOTKEY:				result = WM_HOTKEY (wParam, lParam); break;		
		case OS.WM_KEYDOWN:				result = WM_KEYDOWN (wParam, lParam); break;
		case OS.WM_KEYUP:				result = WM_KEYUP (wParam, lParam); break;
		case OS.WM_KILLFOCUS:			result = WM_KILLFOCUS (wParam, lParam); break;
		case OS.WM_LBUTTONDBLCLK:		result = WM_LBUTTONDBLCLK (wParam, lParam); break;
		case OS.WM_LBUTTONDOWN:			result = WM_LBUTTONDOWN (wParam, lParam); break;
		case OS.WM_LBUTTONUP:			result = WM_LBUTTONUP (wParam, lParam); break;
		case OS.WM_MBUTTONDBLCLK:		result = WM_MBUTTONDBLCLK (wParam, lParam); break;
		case OS.WM_MBUTTONDOWN:			result = WM_MBUTTONDOWN (wParam, lParam); break;
		case OS.WM_MBUTTONUP:			result = WM_MBUTTONUP (wParam, lParam); break;
		case OS.WM_MEASUREITEM:			result = WM_MEASUREITEM (wParam, lParam); break;
		case OS.WM_MENUCHAR:			result = WM_MENUCHAR (wParam, lParam); break;
		case OS.WM_MENUSELECT:			result = WM_MENUSELECT (wParam, lParam); break;
		case OS.WM_MOUSEACTIVATE:		result = WM_MOUSEACTIVATE (wParam, lParam); break;
		case OS.WM_MOUSEHOVER:			result = WM_MOUSEHOVER (wParam, lParam); break;
		case OS.WM_MOUSELEAVE:			result = WM_MOUSELEAVE (wParam, lParam); break;
		case OS.WM_MOUSEMOVE:			result = WM_MOUSEMOVE (wParam, lParam); break;
		case OS.WM_MOUSEWHEEL:			result = WM_MOUSEWHEEL (wParam, lParam); break;
		case OS.WM_MOVE:				result = WM_MOVE (wParam, lParam); break;
		case OS.WM_NCACTIVATE:			result = WM_NCACTIVATE (wParam, lParam); break;
		case OS.WM_NCCALCSIZE:			result = WM_NCCALCSIZE (wParam, lParam); break;
		case OS.WM_NCHITTEST:			result = WM_NCHITTEST (wParam, lParam); break;
		case OS.WM_NCLBUTTONDOWN:		result = WM_NCLBUTTONDOWN (wParam, lParam); break;
		case OS.WM_NCPAINT:				result = WM_NCPAINT (wParam, lParam); break;
		case OS.WM_NOTIFY:				result = WM_NOTIFY (wParam, lParam); break;
		case OS.WM_PAINT:				result = WM_PAINT (wParam, lParam); break;
		case OS.WM_PALETTECHANGED:		result = WM_PALETTECHANGED (wParam, lParam); break;
		case OS.WM_PARENTNOTIFY:		result = WM_PARENTNOTIFY (wParam, lParam); break;
		case OS.WM_PASTE:				result = WM_PASTE (wParam, lParam); break;
		case OS.WM_PRINT:				result = WM_PRINT (wParam, lParam); break;
		case OS.WM_PRINTCLIENT:			result = WM_PRINTCLIENT (wParam, lParam); break;
		case OS.WM_QUERYENDSESSION:		result = WM_QUERYENDSESSION (wParam, lParam); break;
		case OS.WM_QUERYNEWPALETTE:		result = WM_QUERYNEWPALETTE (wParam, lParam); break;
		case OS.WM_QUERYOPEN:			result = WM_QUERYOPEN (wParam, lParam); break;
		case OS.WM_RBUTTONDBLCLK:		result = WM_RBUTTONDBLCLK (wParam, lParam); break;
		case OS.WM_RBUTTONDOWN:			result = WM_RBUTTONDOWN (wParam, lParam); break;
		case OS.WM_RBUTTONUP:			result = WM_RBUTTONUP (wParam, lParam); break;
		case OS.WM_SETCURSOR:			result = WM_SETCURSOR (wParam, lParam); break;
		case OS.WM_SETFOCUS:			result = WM_SETFOCUS (wParam, lParam); break;
		case OS.WM_SETFONT:				result = WM_SETFONT (wParam, lParam); break;
		case OS.WM_SETTINGCHANGE:		result = WM_SETTINGCHANGE (wParam, lParam); break;
		case OS.WM_SETREDRAW:			result = WM_SETREDRAW (wParam, lParam); break;
		case OS.WM_SHOWWINDOW:			result = WM_SHOWWINDOW (wParam, lParam); break;
		case OS.WM_SIZE:				result = WM_SIZE (wParam, lParam); break;
		case OS.WM_SYSCHAR:				result = WM_SYSCHAR (wParam, lParam); break;
		case OS.WM_SYSCOLORCHANGE:		result = WM_SYSCOLORCHANGE (wParam, lParam); break;
		case OS.WM_SYSCOMMAND:			result = WM_SYSCOMMAND (wParam, lParam); break;
		case OS.WM_SYSKEYDOWN:			result = WM_SYSKEYDOWN (wParam, lParam); break;
		case OS.WM_SYSKEYUP:			result = WM_SYSKEYUP (wParam, lParam); break;
		case OS.WM_TIMER:				result = WM_TIMER (wParam, lParam); break;
		case OS.WM_UNDO:				result = WM_UNDO (wParam, lParam); break;
		case OS.WM_UPDATEUISTATE:		result = WM_UPDATEUISTATE (wParam, lParam); break;
		case OS.WM_VSCROLL:				result = WM_VSCROLL (wParam, lParam); break;
		case OS.WM_WINDOWPOSCHANGED:	result = WM_WINDOWPOSCHANGED (wParam, lParam); break;
		case OS.WM_WINDOWPOSCHANGING:	result = WM_WINDOWPOSCHANGING (wParam, lParam); break;
		case OS.WM_XBUTTONDBLCLK:		result = WM_XBUTTONDBLCLK (wParam, lParam); break;
		case OS.WM_XBUTTONDOWN:			result = WM_XBUTTONDOWN (wParam, lParam); break;
		case OS.WM_XBUTTONUP:			result = WM_XBUTTONUP (wParam, lParam); break;
	}
	if (result != null) return result.value;
	return callWindowProc (hwnd, msg, wParam, lParam);
}

LRESULT WM_ACTIVATE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_CAPTURECHANGED (int /*long*/ wParam, int /*long*/ lParam) {
	return wmCaptureChanged (handle, wParam, lParam);
}

LRESULT WM_CHANGEUISTATE (int /*long*/ wParam, int /*long*/ lParam) {
	if ((state & IGNORE_WM_CHANGEUISTATE) != 0) return LRESULT.ZERO;
	return null;
}

LRESULT WM_CHAR (int /*long*/ wParam, int /*long*/ lParam) {
	return wmChar (handle, wParam, lParam);
}

LRESULT WM_CLEAR (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_CLOSE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_COMMAND (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* When the WM_COMMAND message is sent from a
	* menu, the HWND parameter in LPARAM is zero.
	*/
	if (lParam == 0) {
		Decorations shell = menuShell ();
		if (shell.isEnabled ()) {
			int id = OS.LOWORD (wParam);
			MenuItem item = display.getMenuItem (id);
			if (item != null && item.isEnabled ()) {
				return item.wmCommandChild (wParam, lParam);
			}
		}
		return null;
	}
	Control control = display.getControl (lParam);
	if (control == null) return null;
	return control.wmCommandChild (wParam, lParam);
}

LRESULT WM_CONTEXTMENU (int /*long*/ wParam, int /*long*/ lParam) {
	return wmContextMenu (handle, wParam, lParam);
}

LRESULT WM_CTLCOLOR (int /*long*/ wParam, int /*long*/ lParam) {
	int /*long*/ hPalette = display.hPalette;
	if (hPalette != 0) {
		OS.SelectPalette (wParam, hPalette, false);
		OS.RealizePalette (wParam);
	}
	Control control = display.getControl (lParam);
	if (control == null) return null;
	return control.wmColorChild (wParam, lParam);
}

LRESULT WM_CUT (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_DESTROY (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_DRAWITEM (int /*long*/ wParam, int /*long*/ lParam) {
	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	if (struct.CtlType == OS.ODT_MENU) {
		MenuItem item = display.getMenuItem (struct.itemID);
		if (item == null) return null;
		return item.wmDrawChild (wParam, lParam);
	}
	Control control = display.getControl (struct.hwndItem);
	if (control == null) return null;
	return control.wmDrawChild (wParam, lParam);
}

LRESULT WM_ENDSESSION (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_ENTERIDLE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_ERASEBKGND (int /*long*/ wParam, int /*long*/ lParam) {
	if ((state & DRAW_BACKGROUND) != 0) {
		if (findImageControl () != null) return LRESULT.ONE;
	}
	if ((state & THEME_BACKGROUND) != 0) {
		if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
			if (findThemeControl () != null) return LRESULT.ONE;
		}
	}
	return null;
}

LRESULT WM_GETDLGCODE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_GETFONT (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_GETOBJECT (int /*long*/ wParam, int /*long*/ lParam) {
	if (accessible != null) {
		int /*long*/ result = accessible.internal_WM_GETOBJECT (wParam, lParam);
		if (result != 0) return new LRESULT (result);
	}
	return null;
}

LRESULT WM_GETMINMAXINFO (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_HOTKEY (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_HELP (int /*long*/ wParam, int /*long*/ lParam) {
	if (OS.IsWinCE) return null;
	HELPINFO lphi = new HELPINFO ();
	OS.MoveMemory (lphi, lParam, HELPINFO.sizeof);
	Decorations shell = menuShell ();
	if (!shell.isEnabled ()) return null;
	if (lphi.iContextType == OS.HELPINFO_MENUITEM) {
		MenuItem item = display.getMenuItem (lphi.iCtrlId);
		if (item != null && item.isEnabled ()) {
			Widget widget = null;
			if (item.hooks (SWT.Help)) {
				widget = item;
			} else {
				Menu menu = item.parent;
				if (menu.hooks (SWT.Help)) widget = menu;
			}
			if (widget != null) {
				int /*long*/ hwndShell = shell.handle;
				OS.SendMessage (hwndShell, OS.WM_CANCELMODE, 0, 0);
				widget.postEvent (SWT.Help);
				return LRESULT.ONE;
			}
		}
		return null;
	}
	if (hooks (SWT.Help)) {
		postEvent (SWT.Help);
		return LRESULT.ONE;
	}
	return null;
}

LRESULT WM_HSCROLL (int /*long*/ wParam, int /*long*/ lParam) {
	Control control = display.getControl (lParam);
	if (control == null) return null;
	return control.wmScrollChild (wParam, lParam);
}

LRESULT WM_IME_CHAR (int /*long*/ wParam, int /*long*/ lParam) {
	return wmIMEChar (handle, wParam, lParam);
}

LRESULT WM_IME_COMPOSITION (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_IME_COMPOSITION_START (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_IME_ENDCOMPOSITION (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_INITMENUPOPUP (int /*long*/ wParam, int /*long*/ lParam) {
	
	/* Ignore WM_INITMENUPOPUP for an accelerator */
	if (display.accelKeyHit) return null;

	/*
	* If the high order word of LPARAM is non-zero,
	* the menu is the system menu and we can ignore
	* WPARAM.  Otherwise, use WPARAM to find the menu.
	*/
	Shell shell = getShell ();
	Menu oldMenu = shell.activeMenu, newMenu = null;
	if (OS.HIWORD (lParam) == 0) {
		newMenu = menuShell ().findMenu (wParam);
		if (newMenu != null) newMenu.update ();
	}	
	Menu menu = newMenu;
	while (menu != null && menu != oldMenu) {
		menu = menu.getParentMenu ();
	}
	if (menu == null) {
		menu = shell.activeMenu;
		while (menu != null) {
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the hide
			* event.  If this happens, stop searching up the
			* ancestor list because there is no longer a link
			* to follow.
			*/
			menu.sendEvent (SWT.Hide);
			if (menu.isDisposed ()) break;
			menu = menu.getParentMenu ();
			Menu ancestor = newMenu;
			while (ancestor != null && ancestor != menu) {
				ancestor = ancestor.getParentMenu ();
			}
			if (ancestor != null) break;
		}
	}
	
	/*
	* The shell and the new menu may be disposed because of
	* sending the hide event to the ancestor menus but setting
	* a field to null in a disposed shell is not harmful.
	*/
	if (newMenu != null && newMenu.isDisposed ()) newMenu = null;
	shell.activeMenu = newMenu;
	
	/* Send the show event */
	if (newMenu != null && newMenu != oldMenu) {
		newMenu.sendEvent (SWT.Show);
		// widget could be disposed at this point
	}
	return null;
}

LRESULT WM_INPUTLANGCHANGE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_KEYDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	return wmKeyDown (handle, wParam, lParam);
}

LRESULT WM_KEYUP (int /*long*/ wParam, int /*long*/ lParam) {
	return wmKeyUp (handle, wParam, lParam);
}

LRESULT WM_KILLFOCUS (int /*long*/ wParam, int /*long*/ lParam) {
	return wmKillFocus (handle, wParam, lParam);
}

LRESULT WM_LBUTTONDBLCLK (int /*long*/ wParam, int /*long*/ lParam) {
	return wmLButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_LBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	return wmLButtonDown (handle, wParam, lParam);
}

LRESULT WM_LBUTTONUP (int /*long*/ wParam, int /*long*/ lParam) {
	return wmLButtonUp (handle, wParam, lParam);
}

LRESULT WM_MBUTTONDBLCLK (int /*long*/ wParam, int /*long*/ lParam) {
	return wmMButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_MBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	return wmMButtonDown (handle, wParam, lParam);
}

LRESULT WM_MBUTTONUP (int /*long*/ wParam, int /*long*/ lParam) {
	return wmMButtonUp (handle, wParam, lParam);
}

LRESULT WM_MEASUREITEM (int /*long*/ wParam, int /*long*/ lParam) {
	MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, MEASUREITEMSTRUCT.sizeof);
	if (struct.CtlType == OS.ODT_MENU) {
		MenuItem item = display.getMenuItem (struct.itemID);
		if (item == null) return null;
		return item.wmMeasureChild (wParam, lParam);
	}
	int /*long*/ hwnd = OS.GetDlgItem (handle, struct.CtlID);
	Control control = display.getControl (hwnd);
	if (control == null) return null;
	return control.wmMeasureChild (wParam, lParam);
}

LRESULT WM_MENUCHAR (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Feature in Windows.  When the user types Alt+<key>
	* and <key> does not match a mnemonic in the System
	* menu or the menu bar, Windows beeps.  This beep is
	* unexpected and unwanted by applications that look
	* for Alt+<key>.  The fix is to detect the case and
	* stop Windows from beeping by closing the menu.
	*/
	int type = OS.HIWORD (wParam);
	if (type == 0 || type == OS.MF_SYSMENU) {
		display.mnemonicKeyHit = false;
		return new LRESULT (OS.MAKELRESULT (0, OS.MNC_CLOSE));
	}
	return null;
}

LRESULT WM_MENUSELECT (int /*long*/ wParam, int /*long*/ lParam) {
	int code = OS.HIWORD (wParam);
	Shell shell = getShell ();
	if (code == 0xFFFF && lParam == 0) {
		Menu menu = shell.activeMenu;
		while (menu != null) {
			/*
			* When the user cancels any menu that is not the
			* menu bar, assume a mnemonic key was pressed to open
			* the menu from WM_SYSCHAR.  When the menu was invoked
			* using the mouse, this assumption is wrong but not
			* harmful.  This variable is only used in WM_SYSCHAR
			* and WM_SYSCHAR is only sent after the user has pressed
			* a mnemonic.
			*/
			display.mnemonicKeyHit = true;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the hide
			* event.  If this happens, stop searching up the
			* parent list because there is no longer a link
			* to follow.
			*/
			menu.sendEvent (SWT.Hide);
			if (menu.isDisposed ()) break;
			menu = menu.getParentMenu ();
		}
		/*
		* The shell may be disposed because of sending the hide
		* event to the last active menu menu but setting a field
		* to null in a destroyed widget is not harmful.
		*/
		shell.activeMenu = null;
		return null;
	}
	if ((code & OS.MF_SYSMENU) != 0) return null;
	if ((code & OS.MF_HILITE) != 0) {
		MenuItem item = null;
		Decorations menuShell = menuShell ();
		if ((code & OS.MF_POPUP) != 0) {
			int index = OS.LOWORD (wParam);
			MENUITEMINFO info = new MENUITEMINFO ();
			info.cbSize = MENUITEMINFO.sizeof;
			info.fMask = OS.MIIM_SUBMENU;
			if (OS.GetMenuItemInfo (lParam, index, true, info)) {
				Menu newMenu = menuShell.findMenu (info.hSubMenu);
				if (newMenu != null) item = newMenu.cascade;
			}	
		} else {
			Menu newMenu = menuShell.findMenu (lParam);
			if (newMenu != null) {
				int id = OS.LOWORD (wParam);
				item = display.getMenuItem (id);
			}
			Menu oldMenu = shell.activeMenu;
			if (oldMenu != null) {
				Menu ancestor = oldMenu;
				while (ancestor != null && ancestor != newMenu) {
					ancestor = ancestor.getParentMenu ();
				}
				if (ancestor == newMenu) {
					ancestor = oldMenu;
					while (ancestor != newMenu) {
						/*
						* It is possible (but unlikely), that application
						* code could have disposed the widget in the hide
						* event or the item about to be armed.  If this
						* happens, stop searching up the ancestor list
						* because there is no longer a link to follow.
						*/
						ancestor.sendEvent (SWT.Hide);
						if (ancestor.isDisposed ()) break;
						ancestor = ancestor.getParentMenu ();
						if (ancestor == null) break;
					}
					/*
					* The shell and/or the item could be disposed when
					* processing hide events from above.  If this happens,
					* ensure that the shell is not accessed and that no
					* arm event is sent to the item.
					*/
					if (!shell.isDisposed ()) {
						if (newMenu != null && newMenu.isDisposed ()) {
							newMenu = null;
						}
						shell.activeMenu = newMenu;
					}
					if (item != null && item.isDisposed ()) item = null;
				}
			}
		}
		if (item != null) item.sendEvent (SWT.Arm);
	}
	return null;
}

LRESULT WM_MOUSEACTIVATE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_MOUSEHOVER (int /*long*/ wParam, int /*long*/ lParam) {
	return wmMouseHover (handle, wParam, lParam);
}

LRESULT WM_MOUSELEAVE (int /*long*/ wParam, int /*long*/ lParam) {
	if (OS.COMCTL32_MAJOR >= 6) getShell ().fixToolTip ();
	return wmMouseLeave (handle, wParam, lParam);
}

LRESULT WM_MOUSEMOVE (int /*long*/ wParam, int /*long*/ lParam) {
	return wmMouseMove (handle, wParam, lParam);
}

LRESULT WM_MOUSEWHEEL (int /*long*/ wParam, int /*long*/ lParam) {
	return wmMouseWheel (handle, wParam, lParam);
}

LRESULT WM_MOVE (int /*long*/ wParam, int /*long*/ lParam) {
	state |= MOVE_OCCURRED;
	if (findImageControl () != null) {
		if (this != getShell ()) redrawChildren ();
	} else {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
				if (OS.IsWindowVisible (handle)) {
					if (findThemeControl () != null) redrawChildren ();
				}
			}
		}
	}
	if ((state & MOVE_DEFERRED) == 0) sendEvent (SWT.Move);
	// widget could be disposed at this point
	return null;
}

LRESULT WM_NCACTIVATE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_NCCALCSIZE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_NCHITTEST (int /*long*/ wParam, int /*long*/ lParam) {
	if (!OS.IsWindowEnabled (handle)) return null;
	if (!isActive ()) return new LRESULT (OS.HTTRANSPARENT);
	return null;
}

LRESULT WM_NCLBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_NCPAINT (int /*long*/ wParam, int /*long*/ lParam) {
	return wmNCPaint (handle, wParam, lParam);
}

LRESULT WM_NOTIFY (int /*long*/ wParam, int /*long*/ lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	return wmNotify (hdr, wParam, lParam);
}

LRESULT WM_PAINT (int /*long*/ wParam, int /*long*/ lParam) {
	return wmPaint (handle, wParam, lParam);
}

LRESULT WM_PALETTECHANGED (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_PARENTNOTIFY (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_PASTE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_PRINT (int /*long*/ wParam, int /*long*/ lParam) {
	return wmPrint (handle, wParam, lParam);
}

LRESULT WM_PRINTCLIENT (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_QUERYENDSESSION (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_QUERYNEWPALETTE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_QUERYOPEN (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_RBUTTONDBLCLK (int /*long*/ wParam, int /*long*/ lParam) {
	return wmRButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_RBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	return wmRButtonDown (handle, wParam, lParam);
}

LRESULT WM_RBUTTONUP (int /*long*/ wParam, int /*long*/ lParam) {
	return wmRButtonUp (handle, wParam, lParam);
}

LRESULT WM_SETCURSOR (int /*long*/ wParam, int /*long*/ lParam) {
	int hitTest = (short) OS.LOWORD (lParam);
 	if (hitTest == OS.HTCLIENT) {
		Control control = display.getControl (wParam);
		if (control == null) return null;
		Cursor cursor = control.findCursor ();
		if (cursor != null) {
			OS.SetCursor (cursor.handle);
			return LRESULT.ONE;
		}
	}
	return null;
}

LRESULT WM_SETFOCUS (int /*long*/ wParam, int /*long*/ lParam) {
	return wmSetFocus (handle, wParam, lParam);
}

LRESULT WM_SETTINGCHANGE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_SETFONT (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_SETREDRAW (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_SHOWWINDOW (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_SIZE (int /*long*/ wParam, int /*long*/ lParam) {
	state |= RESIZE_OCCURRED;
	if ((state & RESIZE_DEFERRED) == 0) sendEvent (SWT.Resize);
	// widget could be disposed at this point
	return null;
}

LRESULT WM_SYSCHAR (int /*long*/ wParam, int /*long*/ lParam) {
	return wmSysChar (handle, wParam, lParam);
}

LRESULT WM_SYSCOLORCHANGE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_SYSCOMMAND (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Check to see if the command is a system command or
	* a user menu item that was added to the System menu.
	* When a user item is added to the System menu,
	* WM_SYSCOMMAND must always return zero.
	* 
	* NOTE: This is undocumented.
	*/
	if ((wParam & 0xF000) == 0) {
		Decorations shell = menuShell ();
		if (shell.isEnabled ()) {
			MenuItem item = display.getMenuItem (OS.LOWORD (wParam));
			if (item != null) item.wmCommandChild (wParam, lParam);
		}
		return LRESULT.ZERO;
	}

	/* Process the System Command */
	int cmd = (int)/*64*/wParam & 0xFFF0;
	switch (cmd) {
		case OS.SC_CLOSE:
			int /*long*/ hwndShell = menuShell ().handle;
			int bits = OS.GetWindowLong (hwndShell, OS.GWL_STYLE);
			if ((bits & OS.WS_SYSMENU) == 0) return LRESULT.ZERO;
			break;
		case OS.SC_KEYMENU:
			/*
			* When lParam is zero, one of F10, Shift+F10, Ctrl+F10 or
			* Ctrl+Shift+F10 was pressed.  If there is no menu bar and
			* the focus control is interested in keystrokes, give the
			* key to the focus control.  Normally, F10 with no menu bar
			* moves focus to the System menu but this can be achieved
			* using Alt+Space.  To allow the application to see F10,
			* avoid running the default window proc.
			* 
			* NOTE:  When F10 is pressed, WM_SYSCOMMAND is sent to the
			* shell, not the focus control.  This is undocumented Windows
			* behavior.
			*/
			if (lParam == 0) {
				Decorations shell = menuShell ();
				Menu menu = shell.getMenuBar ();
				if (menu == null) {
					Control control = display._getFocusControl ();
					if (control != null) {
						if (control.hooks (SWT.KeyDown) || control.hooks (SWT.KeyUp)) {
							display.mnemonicKeyHit = false;
							return LRESULT.ZERO;
						}
					}
				}
			} else {
				/*
				* When lParam is not zero, Alt+<key> was pressed.  If the
				* application is interested in keystrokes and there is a
				* menu bar, check to see whether the key that was pressed
				* matches a mnemonic on the menu bar.  Normally, Windows
				* matches the first character of a menu item as well as
				* matching the mnemonic character.  To allow the application
				* to see the keystrokes in this case, avoid running the default
				* window proc.
				* 
				* NOTE: When the user types Alt+Space, the System menu is
				* activated.  In this case the application should not see
				* the keystroke.
				*/
				if (hooks (SWT.KeyDown) || hooks (SWT.KeyUp)) {
					if (lParam != ' ') {
						Decorations shell = menuShell ();
						Menu menu = shell.getMenuBar ();
						if (menu != null) {
							char key = Display.mbcsToWcs ((int)/*64*/lParam);
							if (key != 0) {
								key = Character.toUpperCase (key);
								MenuItem [] items = menu.getItems ();
								for (int i=0; i<items.length; i++) {
									MenuItem item = items [i];
									String text = item.getText ();
									char mnemonic = findMnemonic (text);
									if (text.length () > 0 && mnemonic == 0) {
										char ch = text.charAt (0);
										if (Character.toUpperCase (ch) == key) {
											display.mnemonicKeyHit = false;
											return LRESULT.ZERO;
										}
									}
								}
							}
						} else {
							display.mnemonicKeyHit = false;
						}
					}
				}
			}
			// FALL THROUGH
		case OS.SC_HSCROLL:
		case OS.SC_VSCROLL:
			/*
			* Do not allow keyboard traversal of the menu bar
			* or scrolling when the shell is not enabled.
			*/
			Decorations shell = menuShell ();
			if (!shell.isEnabled () || !shell.isActive ()) {
				return LRESULT.ZERO;
			}
			break;
		case OS.SC_MINIMIZE:
			/* Save the focus widget when the shell is minimized */
			menuShell ().saveFocus ();
			break;
	}
	return null;
}

LRESULT WM_SYSKEYDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	return wmSysKeyDown (handle, wParam, lParam);
}

LRESULT WM_SYSKEYUP (int /*long*/ wParam, int /*long*/ lParam) {
	return wmSysKeyUp (handle, wParam, lParam);
}

LRESULT WM_TIMER (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_UNDO (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_UPDATEUISTATE (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT WM_VSCROLL (int /*long*/ wParam, int /*long*/ lParam) {
	Control control = display.getControl (lParam);
	if (control == null) return null;
	return control.wmScrollChild (wParam, lParam);
}

LRESULT WM_WINDOWPOSCHANGED (int /*long*/ wParam, int /*long*/ lParam) {
	try {
		display.resizeCount++;
		int /*long*/ code = callWindowProc (handle, OS.WM_WINDOWPOSCHANGED, wParam, lParam);
		return code == 0 ? LRESULT.ZERO : new LRESULT (code);
	} finally {
		--display.resizeCount;
	}
}

LRESULT WM_WINDOWPOSCHANGING (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Bug in Windows.  When WM_SETREDRAW is used to turn off drawing
	* for a control and the control is moved or resized, Windows does
	* not redraw the area where the control once was in the parent.
	* The fix is to detect this case and redraw the area.
	*/
	if (!getDrawing()) {
		Shell shell = getShell ();
		if (shell != this) {
			WINDOWPOS lpwp = new WINDOWPOS ();
			OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
			if ((lpwp.flags & OS.SWP_NOMOVE) == 0 || (lpwp.flags & OS.SWP_NOSIZE) == 0) {
				RECT rect = new RECT ();
				OS.GetWindowRect (topHandle (), rect);
				int width = rect.right - rect.left;
				int height = rect.bottom - rect.top;
				if (width != 0 && height != 0) {
					int /*long*/ hwndParent = parent == null ? 0 : parent.handle;
					OS.MapWindowPoints (0, hwndParent, rect, 2);
					if (OS.IsWinCE) {
						OS.InvalidateRect (hwndParent, rect, true);
					} else {
						int /*long*/ rgn1 = OS.CreateRectRgn (rect.left, rect.top, rect.right, rect.bottom);
						int /*long*/ rgn2 = OS.CreateRectRgn (lpwp.x, lpwp.y, lpwp.x + lpwp.cx, lpwp.y + lpwp.cy);
						OS.CombineRgn (rgn1, rgn1, rgn2, OS.RGN_DIFF);
						int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
						OS.RedrawWindow (hwndParent, null, rgn1, flags);
						OS.DeleteObject (rgn1);
						OS.DeleteObject (rgn2);
					}
				}
			}
		}
	}
	return null;
}

LRESULT WM_XBUTTONDBLCLK (int /*long*/ wParam, int /*long*/ lParam) {
	return wmXButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_XBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	return wmXButtonDown (handle, wParam, lParam);
}

LRESULT WM_XBUTTONUP (int /*long*/ wParam, int /*long*/ lParam) {
	return wmXButtonUp (handle, wParam, lParam);
}

LRESULT wmColorChild (int /*long*/ wParam, int /*long*/ lParam) {
	Control control = findBackgroundControl ();
	if (control == null) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
				control = findThemeControl ();
				if (control != null) {
					RECT rect = new RECT ();
					OS.GetClientRect (handle, rect);
					OS.SetTextColor (wParam, getForegroundPixel ());
					OS.SetBkColor (wParam, getBackgroundPixel ());
					fillThemeBackground (wParam, control, rect);
					OS.SetBkMode (wParam, OS.TRANSPARENT);
					return new LRESULT (OS.GetStockObject (OS.NULL_BRUSH));
				}
			}
		}
		if (foreground == -1) return null;
	}
	if (control == null) control = this;
	int forePixel = getForegroundPixel ();
	int backPixel = control.getBackgroundPixel ();
	OS.SetTextColor (wParam, forePixel);
	OS.SetBkColor (wParam, backPixel);
	if (control.backgroundImage != null) {
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		int /*long*/ hwnd = control.handle;
		int /*long*/ hBitmap = control.backgroundImage.handle;
		OS.MapWindowPoints (handle, hwnd, rect, 2);
		POINT lpPoint = new POINT ();
		OS.GetWindowOrgEx (wParam, lpPoint);
		OS.SetBrushOrgEx (wParam, -rect.left - lpPoint.x, -rect.top - lpPoint.y, lpPoint);
		int /*long*/ hBrush = findBrush (hBitmap, OS.BS_PATTERN);
		if ((state & DRAW_BACKGROUND) != 0) {
			int /*long*/ hOldBrush = OS.SelectObject (wParam, hBrush);
			OS.MapWindowPoints (hwnd, handle, rect, 2);
			OS.PatBlt (wParam, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
			OS.SelectObject (wParam, hOldBrush);
		}
		OS.SetBkMode (wParam, OS.TRANSPARENT);
		return new LRESULT (hBrush);
	}
	int /*long*/ hBrush = findBrush (backPixel, OS.BS_SOLID);
	if ((state & DRAW_BACKGROUND) != 0) {
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		int /*long*/ hOldBrush = OS.SelectObject (wParam, hBrush);
		OS.PatBlt (wParam, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
		OS.SelectObject (wParam, hOldBrush);
	}
	return new LRESULT (hBrush);
}

LRESULT wmCommandChild (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT wmDrawChild (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT wmMeasureChild (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT wmNotify (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	Control control = display.getControl (hdr.hwndFrom);
	if (control == null) return null;
	return control.wmNotifyChild (hdr, wParam, lParam);
}

LRESULT wmNotifyChild (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT wmScrollChild (int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

}


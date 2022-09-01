/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stefan Xenos (Google) - bug 468854 - Add a requestLayout method to Control
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT, FLIP_TEXT_DIRECTION</dd>
 * <dt><b>Events:</b>
 * <dd>DragDetect, FocusIn, FocusOut, Help, KeyDown, KeyUp, MenuDetect, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, MouseWheel, MouseHorizontalWheel, MouseVerticalWheel, Move,
 *     Paint, Resize, Traverse</dd>
 * </dl>
 * <p>
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
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public long handle;
	Composite parent;
	Cursor cursor;
	Menu menu, activeMenu;
	String toolTipText;
	Object layoutData;
	Accessible accessible;
	Image backgroundImage;
	Region region;
	Font font;
	int drawCount, foreground, background, backgroundAlpha = 255;

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
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
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
 * be notified when gesture events are generated for the control,
 * by sending it one of the messages defined in the
 * <code>GestureListener</code> interface.
 * <p>
 * NOTE: If <code>setTouchEnabled(true)</code> has previously been
 * invoked on the receiver then <code>setTouchEnabled(false)</code>
 * must be invoked on it to specify that gesture events should be
 * sent instead of touch events.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows and Cocoa.
 * SWT doesn't send Gesture or Touch events on GTK.
 * </p>
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
 * @see GestureListener
 * @see #removeGestureListener
 * @see #setTouchEnabled
 *
 * @since 3.7
 */
public void addGestureListener (GestureListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Gesture, typedListener);
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
 * be notified when touch events occur, by sending it
 * one of the messages defined in the <code>TouchListener</code>
 * interface.
 * <p>
 * NOTE: You must also call <code>setTouchEnabled(true)</code> to
 * specify that touch events should be sent, which will cause gesture
 * events to not be sent.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows and Cocoa.
 * SWT doesn't send Gesture or Touch events on GTK.
 * </p>
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
 * @see TouchListener
 * @see #removeTouchListener
 * @see #setTouchEnabled
 *
 * @since 3.7
 */
public void addTouchListener (TouchListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Touch,typedListener);
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

int binarySearch (int [] indices, int start, int end, int index) {
	int low = start, high = end - 1;
	while (low <= high) {
		int mid = (low + high) >>> 1;
		if (indices [mid] == index) return mid;
		if (indices [mid] < index) {
			low = mid + 1;
		} else {
			high = mid - 1;
		}
	}
	return -low - 1;
}

long borderHandle () {
	return handle;
}

void checkBackground () {
	Shell shell = getShell ();
	if (this == shell) return;
	state &= ~PARENT_BACKGROUND;
	Composite composite = parent;
	do {
		int mode = composite.backgroundMode;
		if (mode != 0 || backgroundAlpha == 0) {
			if (mode == SWT.INHERIT_DEFAULT || backgroundAlpha == 0) {
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
	if (getBorderWidthInPixels () == 0) style &= ~SWT.BORDER;
}

void checkBuffered () {
	style &= ~SWT.DOUBLE_BUFFERED;
}

void checkComposited () {
	/* Do nothing */
}

void checkMirrored () {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
		if ((bits & OS.WS_EX_LAYOUTRTL) != 0) style |= SWT.MIRRORED;
	}
}

/**
 * Returns the preferred size (in points) of the receiver.
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
	return computeSize(wHint, hHint, true);
}

/**
 * Returns the preferred size (in points) of the receiver.
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
public Point computeSize (int wHint, int hHint, boolean changed){
	checkWidget ();
	wHint = (wHint != SWT.DEFAULT ? DPIUtil.autoScaleUp(wHint) : wHint);
	hHint = (hHint != SWT.DEFAULT ? DPIUtil.autoScaleUp(hHint) : hHint);
	return DPIUtil.autoScaleDown(computeSizeInPixels(wHint, hHint, changed));
}

Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidthInPixels ();
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
	long hwndParent = widgetParent ();
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
}

void checkGesture () {
	int value = OS.GetSystemMetrics (OS.SM_DIGITIZER);
	if ((value & (OS.NID_READY | OS.NID_MULTI_INPUT)) != 0) {
		/*
		 * Feature in Windows 7: All gestures are enabled by default except GID_ROTATE.
		 * Enable it explicitly by calling SetGestureConfig.
		 */
		GESTURECONFIG config = new GESTURECONFIG();
		config.dwID = OS.GID_ROTATE;
		config.dwWant = 1;
		config.dwBlock = 0;
		OS.SetGestureConfig (handle, 0, 1, config, GESTURECONFIG.sizeof);
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
	checkGesture ();
	if ((state & PARENT_BACKGROUND) != 0) {
		setBackground ();
	}
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_BTNFACE);
}

long defaultFont () {
	return display.getSystemFont ().handle;
}

int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
}

void deregister () {
	display.removeControl (handle);
}

@Override
void destroyWidget () {
	long hwnd = topHandle ();
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
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
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
	Point loc = event.getLocationInPixels();
	return dragDetect (event.button, event.count, event.stateMask, loc.x, loc.y);
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
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
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
	return dragDetect (event.button, event.count, event.stateMask, DPIUtil.autoScaleUp(event.x), DPIUtil.autoScaleUp(event.y)); // To Pixels
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
			long lParam = OS.MAKELPARAM (x, y);
			OS.SendMessage (handle, OS.WM_LBUTTONUP, wParam, lParam);
		}
		return false;
	}
	return sendDragEvent (button, stateMask, x, y);
}

void drawBackground (long hDC) {
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	drawBackground (hDC, rect);
}

void drawBackground (long hDC, RECT rect) {
	drawBackground (hDC, rect, -1, 0, 0);
}

void drawBackground (long hDC, RECT rect, int pixel, int tx, int ty) {
	Control control = findBackgroundControl ();
	if (control != null) {
		if (control.backgroundImage != null) {
			fillImageBackground (hDC, control, rect, tx, ty);
			return;
		}
		pixel = control.getBackgroundPixel ();
	}
	if (pixel == -1) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.IsAppThemed ()) {
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

void drawImageBackground (long hDC, long hwnd, long hBitmap, RECT rect, int tx, int ty) {
	RECT rect2 = new RECT ();
	OS.GetClientRect (hwnd, rect2);
	OS.MapWindowPoints (hwnd, handle, rect2, 2);
	long hBrush = findBrush (hBitmap, OS.BS_PATTERN);
	POINT lpPoint = new POINT ();
	OS.GetWindowOrgEx (hDC, lpPoint);
	OS.SetBrushOrgEx (hDC, -rect2.left - lpPoint.x - tx, -rect2.top - lpPoint.y - ty, lpPoint);
	long hOldBrush = OS.SelectObject (hDC, hBrush);
	OS.PatBlt (hDC, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
	OS.SetBrushOrgEx (hDC, lpPoint.x, lpPoint.y, null);
	OS.SelectObject (hDC, hOldBrush);
}

void drawThemeBackground (long hDC, long hwnd, RECT rect) {
	/* Do nothing */
}

void enableDrag (boolean enabled) {
	/* Do nothing */
}

void maybeEnableDarkSystemTheme() {
	maybeEnableDarkSystemTheme(handle);
}

void enableWidget (boolean enabled) {
	OS.EnableWindow (handle, enabled);
}

void fillBackground (long hDC, int pixel, RECT rect) {
	if (rect.left > rect.right || rect.top > rect.bottom) return;
	OS.FillRect (hDC, rect, findBrush (pixel, OS.BS_SOLID));
}

void fillImageBackground (long hDC, Control control, RECT rect, int tx, int ty) {
	if (rect.left > rect.right || rect.top > rect.bottom) return;
	if (control != null) {
		Image image = control.backgroundImage;
		if (image != null) {
			control.drawImageBackground (hDC, handle, image.handle, rect, tx, ty);
		}
	}
}

void fillThemeBackground (long hDC, Control control, RECT rect) {
	if (rect.left > rect.right || rect.top > rect.bottom) return;
	if (control != null) {
		control.drawThemeBackground (hDC, handle, rect);
	}
}

Control findBackgroundControl () {
	if ((background != -1 || backgroundImage != null) && backgroundAlpha > 0) return this;
	return (parent != null && (state & PARENT_BACKGROUND) != 0) ? parent.findBackgroundControl () : null;
}

long findBrush (long value, int lbStyle) {
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
			if (control.setFocus ()) return;
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
			OS.SetWindowPos (wp.hwnd, 0, wp.x, wp.y, wp.cx, wp.cy, wp.flags);
			lpwp [i] = null;
			return;
		}
	}
}

/**
 * Returns the accessible object for the receiver.
 * <p>
 * If this is the first time this object is requested,
 * then the object is created and returned. The object
 * returned by getAccessible() does not need to be disposed.
 * </p>
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
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on some versions of Windows the background of a TabFolder,
 * is a gradient rather than a solid color.
 * </p>
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Color getBackground () {
	checkWidget ();
	if (backgroundAlpha == 0) {
		Color color =  Color.win32_new (display, background, 0);
		return color;
	}
	else {
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		return Color.win32_new (display, control.getBackgroundPixel (), backgroundAlpha);
	}
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
 * Returns the receiver's border width in points.
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
	return DPIUtil.autoScaleDown(getBorderWidthInPixels ());
}

int getBorderWidthInPixels () {
	long borderHandle = borderHandle ();
	int bits1 = OS.GetWindowLong (borderHandle, OS.GWL_EXSTYLE);
	if ((bits1 & OS.WS_EX_CLIENTEDGE) != 0) return OS.GetSystemMetrics (OS.SM_CXEDGE);
	if ((bits1 & OS.WS_EX_STATICEDGE) != 0) return OS.GetSystemMetrics (OS.SM_CXBORDER);
	int bits2 = OS.GetWindowLong (borderHandle, OS.GWL_STYLE);

	if ((bits2 & OS.WS_BORDER) != 0) {
		/*
		 * For compatibility reasons, isUseWsBorder() shall not change layout size
		 * compared to previously used WS_EX_CLIENTEDGE. Removing this workaround
		 * saves screen space, but could break some layouts.
		 */
		if (isUseWsBorder ())
			return OS.GetSystemMetrics (OS.SM_CXEDGE);

		return OS.GetSystemMetrics (OS.SM_CXBORDER);
	}

	return 0;
}

/**
 * Returns a rectangle describing the receiver's size and location in points
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
public Rectangle getBounds (){
	checkWidget ();
	return DPIUtil.autoScaleDown(getBoundsInPixels ());
}

Rectangle getBoundsInPixels () {
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (topHandle (), rect);
	long hwndParent = parent == null ? 0 : parent.handle;
	OS.MapWindowPoints (0, hwndParent, rect, 2);
	int width = rect.right - rect.left;
	int height =  rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

int getCodePage () {
	return OS.CP_ACP;
}

String getClipboardText () {
	String string = "";
	if (OS.OpenClipboard (0)) {
		long hMem = OS.GetClipboardData (OS.CF_UNICODETEXT);
		if (hMem != 0) {
			/* Ensure byteCount is a multiple of 2 bytes on UNICODE platforms */
			int byteCount = OS.GlobalSize (hMem) / TCHAR.sizeof * TCHAR.sizeof;
			long ptr = OS.GlobalLock (hMem);
			if (ptr != 0) {
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
	long hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
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
 * to its parent in points (or its display if its parent is null), unless
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
	return DPIUtil.autoScaleDown(getLocationInPixels());
}

Point getLocationInPixels () {
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (topHandle (), rect);
	long hwndParent = parent == null ? 0 : parent.handle;
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
@Override
public Menu getMenu () {
	checkWidget ();
	return menu;
}

/**
 * Returns the receiver's monitor.
 *
 * @return the receiver's monitor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget ();
	long hmonitor = OS.MonitorFromWindow (handle, OS.MONITOR_DEFAULTTONEAREST);
	return display.getMonitor (hmonitor);
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public int getOrientation () {
	checkWidget ();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
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
 * Returns a point describing the receiver's size in points. The
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
public Point getSize (){
	checkWidget ();
	return DPIUtil.autoScaleDown(getSizeInPixels ());
}

Point getSizeInPixels () {
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (topHandle (), rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Point (width, height);
}

/**
 * Calculates a slightly different color, e.g. for the hot state of a button.
 * @param pixel the color to start with
 */
int getSlightlyDifferentColor(int pixel) {
	return getDifferentColor(pixel, 0.1);
}

/**
 * Calculates a different color, e.g. for the checked state of a toggle button
 * or to highlight a selected button.
 * @param pixel the color to start with
 */
int getDifferentColor(int pixel) {
	return getDifferentColor(pixel, 0.2);
}

/**
 * @param factor must be between [0..1]. The bounds are not checked
 */
int getDifferentColor(int pixel, double factor) {
	int red = pixel & 0xFF;
	int green = (pixel & 0xFF00) >> 8;
	int blue = (pixel & 0xFF0000) >> 16;
	red += calcDiff(red, factor);
	green += calcDiff(green, factor);
	blue += calcDiff(blue, factor);
	return (red & 0xFF) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 16);
}

long /* int */ calcDiff(int component, double factor) {
	if (component > 127) {
		return Math.round(component * -1 * factor);
	} else {
		return Math.round((255 - component) * factor);
	}
}

/**
 * Calculates a slightly different background color, e.g. for highlighting the sort column
 * in a table or tree. This method produces less contrast that {@link #getSlightlyDifferentColor(int)}.
 * @param pixel the color to start with
 */
int getSlightlyDifferentBackgroundColor(int pixel) {
	int offset = 8;
	int red = pixel & 0xFF;
	int green = (pixel & 0xFF00) >> 8;
	int blue = (pixel & 0xFF0000) >> 16;
	red = red > 127 ? red-offset : red+offset;
	green = green > 127 ? green-offset : green+offset;
	blue = blue > 127 ? blue-offset : blue+offset;
	return (red & 0xFF) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 16);
}

/**
 * Returns the text direction of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the text direction style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.102
 */
public int getTextDirection() {
	checkWidget ();
	int flags = OS.WS_EX_LAYOUTRTL | OS.WS_EX_RTLREADING;
	int bits  = OS.GetWindowLong (handle, OS.GWL_EXSTYLE) & flags;
	return bits == 0 || bits == flags ? SWT.LEFT_TO_RIGHT : SWT.RIGHT_TO_LEFT;
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
 * Returns <code>true</code> if this control is set to send touch events, or
 * <code>false</code> if it is set to send gesture events instead.  This method
 * also returns <code>false</code> if a touch-based input device is not detected
 * (this can be determined with <code>Display#getTouchEnabled()</code>).  Use
 * {@link #setTouchEnabled(boolean)} to switch the events that a control sends
 * between touch events and gesture events.
 *
 * @return <code>true</code> if the control is set to send touch events, or <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setTouchEnabled
 * @see Display#getTouchEnabled
 *
 * @since 3.7
 */
public boolean getTouchEnabled () {
	checkWidget ();
	return OS.IsTouchWindow (handle, null);
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

boolean hasCustomBackground() {
	return background != -1;
}

boolean hasCustomForeground() {
	return foreground != -1;
}

boolean hasFocus () {
	/*
	* If a non-SWT child of the control has focus,
	* then this control is considered to have focus
	* even though it does not have focus in Windows.
	*/
	long hwndFocus = OS.GetFocus ();
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
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public long internal_new_GC (GCData data) {
	checkWidget();
	long hwnd = handle;
	if (data != null && data.hwnd != 0) hwnd = data.hwnd;
	if (data != null) data.hwnd = hwnd;
	long hDC = 0;
	if (data == null || data.ps == null) {
		hDC = OS.GetDC (hwnd);
	} else {
		hDC = OS.BeginPaint (hwnd, data.ps);
	}
	if (hDC == 0) error(SWT.ERROR_NO_HANDLES);
	if (data != null) {
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
		data.device = display;
		int foreground = getForegroundPixel ();
		if (foreground != OS.GetTextColor (hDC)) data.foreground = foreground;
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		int background = control.getBackgroundPixel ();
		if (background != OS.GetBkColor (hDC)) data.background = background;
		data.font = font != null ? font : Font.win32_new (display, OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0));
		data.uiState = (int)OS.SendMessage (hwnd, OS.WM_QUERYUISTATE, 0, 0);
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
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public void internal_dispose_GC (long hDC, GCData data) {
	checkWidget ();
	long hwnd = handle;
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
		Point size = control.getSizeInPixels ();
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
//	long hDC = OS.GetDCEx (handle, 0, flags);
//	int result = OS.GetClipBox (hDC, new RECT ());
//	OS.ReleaseDC (handle, hDC);
//	return result != OS.NULLREGION;
}

boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (Control element : tabList) {
			if (element == this) return true;
		}
	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.WS_TABSTOP) != 0;
}

boolean isTabItem () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (Control element : tabList) {
			if (element == this) return false;
		}
	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.WS_TABSTOP) != 0) return false;
	long code = OS.SendMessage (handle, OS.WM_GETDLGCODE, 0, 0);
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

/**
 * Custom theming: whether to use WS_BORDER instead of WS_EX_CLIENTEDGE for SWT.BORDER
 * Intended for override.
 */
boolean isUseWsBorder () {
	return (display != null) && display.useWsBorderAll;
}

@Override
void mapEvent (long hwnd, Event event) {
	if (hwnd != handle) {
		POINT point = new POINT ();
		Point loc = event.getLocationInPixels();
		point.x = loc.x;
		point.y = loc.y;
		OS.MapWindowPoints (hwnd, handle, point, 1);
		event.setLocationInPixels(point.x, point.y);
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
	long topHandle = topHandle (), hwndAbove = OS.HWND_TOP;
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		long hwnd = control.topHandle ();
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
	OS.SetWindowPos (topHandle, hwndAbove, 0, 0, 0, 0, flags);
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
	long topHandle = topHandle (), hwndAbove = OS.HWND_BOTTOM;
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
			long hwndParent = parent.handle, hwnd = hwndParent;
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
	OS.SetWindowPos (topHandle, hwndAbove, 0, 0, 0, 0, flags);
}

Accessible new_Accessible (Control control) {
	return Accessible.internal_new_Accessible (this);
}

@Override
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
	/*
	 * Since computeSize is overridden by Custom classes like CCombo
	 * etc... hence we cannot call computeSizeInPixels directly.
	 */
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
	long topHandle = topHandle ();
	long hdc = gc.handle;
	int state = 0;
	long gdipGraphics = gc.getGCData().gdipGraphics;
	if (gdipGraphics != 0) {
		long clipRgn = 0;
		Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeNone);
		long rgn = Gdip.Region_new();
		if (rgn == 0) error(SWT.ERROR_NO_HANDLES);
		Gdip.Graphics_GetClip(gdipGraphics, rgn);
		if (!Gdip.Region_IsInfinite(rgn, gdipGraphics)) {
			clipRgn = Gdip.Region_GetHRGN(rgn, gdipGraphics);
		}
		Gdip.Region_delete(rgn);
		Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeHalf);
		float[] lpXform = null;
		long matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
		if (matrix == 0) error(SWT.ERROR_NO_HANDLES);
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
	int flags = OS.RDW_UPDATENOW | OS.RDW_ALLCHILDREN;
	OS.RedrawWindow (topHandle, null, 0, flags);
	printWidget (topHandle, hdc, gc);
	if (gdipGraphics != 0) {
		OS.RestoreDC(hdc, state);
		Gdip.Graphics_ReleaseHDC(gdipGraphics, hdc);
	}
	return true;
}

void printWidget (long hwnd, long hdc, GC gc) {
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
		long hwndParent = OS.GetParent (hwnd);
		long hwndShell = hwndParent;
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
		* Bug in Windows. PrintWindow() does not print portions
		* of the receiver that are clipped out using SetWindowRgn()
		* in a parent.  The fix is temporarily reparent the window
		* to the desktop, call PrintWindow() then reparent the window
		* back.
		*/
		if (!fixPrintWindow) {
			long rgn = OS.CreateRectRgn(0, 0, 0, 0);
			long parent = OS.GetParent(hwnd);
			while (parent != hwndShell && !fixPrintWindow) {
				if (OS.GetWindowRgn(parent, rgn) != 0) {
					fixPrintWindow = true;
				}
				parent = OS.GetParent(parent);
			}
			OS.DeleteObject(rgn);
		}
		int bits1 = OS.GetWindowLong (hwnd, OS.GWL_STYLE);
		int bits2 = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
		long hwndInsertAfter = OS.GetWindow (hwnd, OS.GW_HWNDPREV);
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
			if ((bits1 & OS.WS_VISIBLE) != 0) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 0, 0);
			}
			OS.SetWindowPos (hwnd, 0, x + width, y + height, 0, 0, flags);
			OS.SetWindowLong (hwnd, OS.GWL_STYLE, (bits1 & ~OS.WS_CHILD) | OS.WS_POPUP);
			OS.SetWindowLong (hwnd, OS.GWL_EXSTYLE, bits2 | OS.WS_EX_TOOLWINDOW);
			Shell shell = getShell ();
			Control savedFocus = shell.savedFocus;
			OS.SetParent (hwnd, 0);
			shell.setSavedFocus (savedFocus);
			if ((bits1 & OS.WS_VISIBLE) != 0) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 1, 0);
			}
		}
		if ((bits1 & OS.WS_VISIBLE) == 0) {
			OS.ShowWindow (hwnd, OS.SW_SHOW);
		}
		success = OS.PrintWindow (hwnd, hdc, 0);
		if ((bits1 & OS.WS_VISIBLE) == 0) {
			OS.ShowWindow (hwnd, OS.SW_HIDE);
		}
		if (fixPrintWindow) {
			if ((bits1 & OS.WS_VISIBLE) != 0) {
				OS.DefWindowProc (hwnd, OS.WM_SETREDRAW, 0, 0);
			}
			OS.SetWindowLong (hwnd, OS.GWL_STYLE, bits1);
			OS.SetWindowLong (hwnd, OS.GWL_EXSTYLE, bits2);
			OS.SetParent (hwnd, hwndParent);
			OS.MapWindowPoints (0, hwndParent, rect1, 2);
			int flags = OS.SWP_NOSIZE | OS.SWP_NOACTIVATE | OS.SWP_DRAWFRAME;
			OS.SetWindowPos (hwnd, hwndInsertAfter, rect1.left, rect1.top, rect1.right - rect1.left, rect1.bottom - rect1.top, flags);
			if ((bits1 & OS.WS_VISIBLE) != 0) {
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
 * Requests that this control and all of its ancestors be repositioned by
 * their layouts at the earliest opportunity. This should be invoked after
 * modifying the control in order to inform any dependent layouts of
 * the change.
 * <p>
 * The control will not be repositioned synchronously. This method is
 * fast-running and only marks the control for future participation in
 * a deferred layout.
 * <p>
 * Invoking this method multiple times before the layout occurs is an
 * inexpensive no-op.
 *
 * @since 3.105
 */
public void requestLayout () {
	getShell ().layout (new Control[] {this}, SWT.DEFER);
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted,
 * including the background.
 * <p>
 * Schedules a paint request if the invalidated area is visible
 * or becomes visible later. It is not necessary for the caller
 * to explicitly call {@link #update()} after calling this method,
 * but depending on the platform, the automatic repaints may be
 * delayed considerably.
 * </p>
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
	redrawInPixels (null,false);
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
 * <p>
 * Schedules a paint request if the invalidated area is visible
 * or becomes visible later. It is not necessary for the caller
 * to explicitly call {@link #update()} after calling this method,
 * but depending on the platform, the automatic repaints may be
 * delayed considerably.
 * </p>
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
	x = DPIUtil.autoScaleUp(x);
	y = DPIUtil.autoScaleUp(y);
	width = DPIUtil.autoScaleUp(width);
	height = DPIUtil.autoScaleUp(height);
	if (width <= 0 || height <= 0) return;

	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);

	redrawInPixels(rect, all);
}

void redrawInPixels (RECT rect, boolean all) {
	if (!OS.IsWindowVisible (handle)) return;
	/*
	 * For many years, it also used RDW_FRAME here for no apparent reason.
	 * This caused Bug 565613, and also is a performance issue. Should the
	 * need for RDW_FRAME arise again, add a function parameter and only
	 * use it where it is indeed necessary.
	 */
	int flags = OS.RDW_ERASE | OS.RDW_INVALIDATE;
	if (all) flags |= OS.RDW_ALLCHILDREN;
	OS.RedrawWindow (handle, rect, 0, flags);
}

boolean redrawChildren () {
	if (!OS.IsWindowVisible (handle)) return false;
	Control control = findBackgroundControl ();
	if (control == null) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.IsAppThemed ()) {
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

@Override
void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
	parent = null;
}

@Override
void releaseParent () {
	parent.removeControl (this);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
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
 * be notified when gesture events are generated for the control.
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
 * @see GestureListener
 * @see #addGestureListener
 *
 * @since 3.7
 */
public void removeGestureListener (GestureListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Gesture, listener);
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
 * be notified when touch events occur.
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
 * @see TouchListener
 * @see #addTouchListener
 *
 * @since 3.7
 */
public void removeTouchListener(TouchListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Touch, listener);
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

int resolveTextDirection() {
	/*
	 * For generic Controls do nothing here. Text-enabled Controls will resolve
	 * AUTO text direction according to their text content.
	 */
	return SWT.NONE;
}

void showWidget (boolean visible) {
	long topHandle = topHandle ();
	OS.ShowWindow (topHandle, visible ? OS.SW_SHOW : OS.SW_HIDE);
	if (handle != topHandle) OS.ShowWindow (handle, visible ? OS.SW_SHOW : OS.SW_HIDE);
}

@Override
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

boolean sendGestureEvent (GESTUREINFO gi) {
	/**
	 * Feature in Windows 7.  GID_BEGIN and GID_END events bubble up through the window
	 * hierarchy for legacy support.  Ignore events not targeted for this control.
	 */
	if (gi.hwndTarget != handle) return true;
	Event event = new Event ();
	int type = 0;
	Point globalPt = new Point(gi.x, gi.y);
	Point point = toControlInPixels(globalPt.x, globalPt.y);
	event.setLocationInPixels(point.x, point.y);
	switch (gi.dwID) {
		case OS.GID_ZOOM:
			type = SWT.Gesture;
			event.detail = SWT.GESTURE_MAGNIFY;
			int fingerDistance = (int)gi.ullArguments;
			if ((gi.dwFlags & OS.GF_BEGIN) != 0) {
				event.detail = SWT.GESTURE_BEGIN;
				display.magStartDistance = display.lastDistance = fingerDistance;
			} else if ((gi.dwFlags & OS.GF_END) != 0) {
				event.detail = SWT.GESTURE_END;
			}

			/*
			* The gi.ullArguments is the distance between the fingers.
			* Scale factor is relative to that original value.
			*/
			if (fingerDistance == display.lastDistance && event.detail == SWT.GESTURE_MAGNIFY) return true;
			if (fingerDistance != 0) event.magnification = fingerDistance / display.magStartDistance;
			display.lastDistance = fingerDistance;
			break;
		case OS.GID_PAN:
			type = SWT.Gesture;
			event.detail = SWT.GESTURE_PAN;
			if ((gi.dwFlags & OS.GF_BEGIN) != 0) {
				event.detail = SWT.GESTURE_BEGIN;
				display.lastX = point.x;
				display.lastY = point.y;
			} else if ((gi.dwFlags & OS.GF_END) != 0) {
				event.detail = SWT.GESTURE_END;
			}
			if (display.lastX == point.x && display.lastY == point.y && event.detail == SWT.GESTURE_PAN) return true;
			event.xDirection = point.x - display.lastX;
			event.yDirection = point.y - display.lastY;
			display.lastX = point.x;
			display.lastY = point.y;
			break;
		case OS.GID_ROTATE:
			type = SWT.Gesture;
			event.detail = SWT.GESTURE_ROTATE;
			double rotationInRadians = OS.GID_ROTATE_ANGLE_FROM_ARGUMENT (gi.ullArguments);
			if ((gi.dwFlags & OS.GF_BEGIN) != 0) {
				event.detail = SWT.GESTURE_BEGIN;
				display.rotationAngle = rotationInRadians;
			} else if ((gi.dwFlags & OS.GF_END) != 0) {
				event.detail = SWT.GESTURE_END;
			}

			/*
			* Feature in Win32. Rotation events are sent even when the fingers are at rest.
			* If the current rotation is the same as the last one received don't send the event.
			*/
			if (display.rotationAngle == rotationInRadians && event.detail == SWT.GESTURE_ROTATE) return true;
			event.rotation = rotationInRadians * 180.0 / Math.PI;
			display.rotationAngle = rotationInRadians;
			break;
		default:
			// Unknown gesture -- ignore.
			break;
	}

	if (type == 0) return true;
	setInputState (event, type);
	sendEvent (type, event);
	return event.doit;
}

void sendMove () {
	sendEvent (SWT.Move);
}

void sendResize () {
	sendEvent (SWT.Resize);
}

void sendTouchEvent (TOUCHINPUT touchInput []) {
	Event event = new Event ();
	POINT pt = new POINT ();
	OS.GetCursorPos (pt);
	OS.ScreenToClient (handle, pt);
	event.setLocationInPixels(pt.x, pt.y);
	Touch [] touches = new Touch [touchInput.length];
	Monitor monitor = getMonitor ();
	for (int i = 0; i < touchInput.length; i++) {
		TOUCHINPUT ti = touchInput [i];
		TouchSource inputSource = display.findTouchSource (ti.hSource, monitor);
		int state = 0;
		if ((ti.dwFlags & OS.TOUCHEVENTF_DOWN) != 0) state = SWT.TOUCHSTATE_DOWN;
		if ((ti.dwFlags & OS.TOUCHEVENTF_UP) != 0) state = SWT.TOUCHSTATE_UP;
		if ((ti.dwFlags & OS.TOUCHEVENTF_MOVE) != 0) state = SWT.TOUCHSTATE_MOVE;
		boolean primary = (ti.dwFlags & OS.TOUCHEVENTF_PRIMARY) != 0;
		int x = OS.TOUCH_COORD_TO_PIXEL (ti.x);
		int y = OS.TOUCH_COORD_TO_PIXEL (ti.y);
		touches [i] = new Touch (ti.dwID, inputSource, state, primary, x, y);
	}
	event.touches = touches;
	setInputState (event, SWT.Touch);
	postEvent (SWT.Touch, event);
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
	_setBackground (color);
	if (color != null) {
		this.updateBackgroundMode ();
	}
}

private void _setBackground (Color color) {
	int pixel = -1;
	int alpha = 255;
	if (color != null) {
		if (color.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
		alpha = color.getAlpha();
	}
	if (pixel == background && alpha == backgroundAlpha) return;
	background = pixel;
	backgroundAlpha = alpha;
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
	if (backgroundImage == image && backgroundAlpha > 0) return;
	backgroundAlpha = 255;
	backgroundImage = image;
	Shell shell = getShell ();
	shell.releaseBrushes ();
	updateBackgroundImage ();
}

void setBackgroundImage (long hBitmap) {
	int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
	OS.RedrawWindow (handle, null, 0, flags);
}

void setBackgroundPixel (int pixel) {
	int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
	OS.RedrawWindow (handle, null, 0, flags);
}

/**
 * Sets the receiver's size and location in points to the rectangular
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
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
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
public void setBounds(int x, int y, int width, int height) {
	checkWidget ();
	x = DPIUtil.autoScaleUp(x);
	y = DPIUtil.autoScaleUp(y);
	width = DPIUtil.autoScaleUp(width);
	height = DPIUtil.autoScaleUp(height);
	setBoundsInPixels(x, y, width, height);
}

void setBoundsInPixels (int x, int y, int width, int height) {
	int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
	setBoundsInPixels (x, y, Math.max (0, width), Math.max (0, height), flags);
}

void setBoundsInPixels (int x, int y, int width, int height, int flags) {
	setBoundsInPixels (x, y, width, height, flags, true);
}

void setBoundsInPixels (int x, int y, int width, int height, int flags, boolean defer) {
	if (findImageControl () != null) {
		if (backgroundImage == null) flags |= OS.SWP_NOCOPYBITS;
	} else {
		if (OS.GetWindow (handle, OS.GW_CHILD) == 0) {
			if (OS.IsAppThemed ()) {
				if (findThemeControl () != null) flags |= OS.SWP_NOCOPYBITS;
			}
		}
	}
	long topHandle = topHandle ();
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
	OS.SetWindowPos (topHandle, 0, x, y, width, height, flags);
}

/**
 * Sets the receiver's size and location in points to the rectangular
 * area specified by the argument. The <code>x</code> and
 * <code>y</code> fields of the rectangle are relative to
 * the receiver's parent (or its display if its parent is null).
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
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
	setBoundsInPixels(DPIUtil.autoScaleUp(rect));
}

void setBoundsInPixels (Rectangle rect) {
	setBoundsInPixels (rect.x, rect.y, rect.width, rect.height);
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
	long lParam = OS.MAKELPARAM (OS.HTCLIENT, OS.WM_MOUSEMOVE);
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
	if (cursor != null && cursor.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	long hwndCursor = OS.GetCapture ();
	if (hwndCursor == 0) {
		POINT pt = new POINT ();
		if (!OS.GetCursorPos (pt)) return;
		long hwnd = hwndCursor = OS.WindowFromPoint (pt);
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
	long hFont = display.getSystemFont ().handle;
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
	long hFont = 0;
	if (font != null) {
		if (font.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
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
		if (color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
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
	x = DPIUtil.autoScaleUp(x);
	y = DPIUtil.autoScaleUp(y);
	setLocationInPixels(x, y);
}

void setLocationInPixels (int x, int y) {
	int flags = OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_DRAWFRAME;
	setBoundsInPixels (x, y, 0, 0, flags);
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
	location = DPIUtil.autoScaleUp(location);
	setLocationInPixels(location.x, location.y);
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
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
		if (menu.parent != menuShell ()) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	this.menu = menu;
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.7
 */
public void setOrientation (int orientation) {
	checkWidget ();
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~SWT.MIRRORED;
	style &= ~flags;
	style |= orientation & flags;
	style &= ~SWT.FLIP_TEXT_DIRECTION;
	updateOrientation ();
	checkMirrored ();
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
			long topHandle = topHandle ();
			OS.SendMessage (topHandle, OS.WM_SETREDRAW, 1, 0);
			if (handle != topHandle) OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			if ((state & HIDDEN) != 0) {
				state &= ~HIDDEN;
				OS.ShowWindow (topHandle, OS.SW_HIDE);
				if (handle != topHandle) OS.ShowWindow (handle, OS.SW_HIDE);
			} else {
				int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
				OS.RedrawWindow (topHandle, null, 0, flags);
			}
		}
	} else {
		if (drawCount++ == 0) {
			long topHandle = topHandle ();
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
	long hRegion = 0;
	if (region != null) {
		hRegion = OS.CreateRectRgn (0, 0, 0, 0);
		OS.CombineRgn (hRegion, region.handle, hRegion, OS.RGN_OR);
	}
	OS.SetWindowRgn (handle, hRegion, true);
	this.region = region;
}

/**
 * Sets the receiver's size to the point specified by the arguments.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause that
 * value to be set to zero instead.
 * </p>
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
 * </p>
 *
 * @param width the new width in points for the receiver
 * @param height the new height in points for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSize (int width, int height) {
	checkWidget ();
	width = DPIUtil.autoScaleUp(width);
	height = DPIUtil.autoScaleUp(height);
	setSizeInPixels(width, height);
}

void setSizeInPixels (int width, int height) {
	int flags = OS.SWP_NOMOVE | OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
	setBoundsInPixels (0, 0, Math.max (0, width), Math.max (0, height), flags);
}

/**
 * Sets the receiver's size to the point specified by the argument.
 * <p>
 * Note: Attempting to set the width or height of the
 * receiver to a negative number will cause them to be
 * set to zero instead.
 * </p>
 * <p>
 * Note: On GTK, attempting to set the width or height of the
 * receiver to a number higher or equal 2^14 will cause them to be
 * set to (2^14)-1 instead.
 * </p>
 *
 * @param size the new size in points for the receiver
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
	size = DPIUtil.autoScaleUp(size);
	setSizeInPixels(size.x, size.y);
}

@Override
boolean setTabItemFocus () {
	if (!isShowing ()) return false;
	return forceFocus ();
}

/**
 * Sets the base text direction (a.k.a. "paragraph direction") of the receiver,
 * which must be one of the constants <code>SWT.LEFT_TO_RIGHT</code>,
 * <code>SWT.RIGHT_TO_LEFT</code>, or <code>SWT.AUTO_TEXT_DIRECTION</code>.
 * <p>
 * <code>setOrientation</code> would override this value with the text direction
 * that is consistent with the new orientation.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows.
 * It doesn't set the base text direction on GTK and Cocoa.
 * </p>
 *
 * @param textDirection the base text direction style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#LEFT_TO_RIGHT
 * @see SWT#RIGHT_TO_LEFT
 * @see SWT#AUTO_TEXT_DIRECTION
 * @see SWT#FLIP_TEXT_DIRECTION
 *
 * @since 3.102
 */
public void setTextDirection(int textDirection) {
	checkWidget ();
	textDirection &= (SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT);
	updateTextDirection (textDirection);
	if (textDirection == AUTO_TEXT_DIRECTION) {
		state |= HAS_AUTO_DIRECTION;
	} else {
		state &= ~HAS_AUTO_DIRECTION;
	}
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
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
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
	if (!Objects.equals(string, toolTipText)) {
		toolTipText = string;
		setToolTipText (getShell (), string);
	}
}

void setToolTipText (Shell shell, String string) {
	shell.setToolTipText (handle, string);
}

/**
 * Sets whether this control should send touch events (by default controls do not).
 * Setting this to <code>false</code> causes the receiver to send gesture events
 * instead.  No exception is thrown if a touch-based input device is not
 * detected (this can be determined with <code>Display#getTouchEnabled()</code>).
 *
 * @param enabled the new touch-enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    </ul>
 *
 * @see Display#getTouchEnabled
 *
 * @since 3.7
 */
public void setTouchEnabled(boolean enabled) {
	checkWidget();
	if (enabled) {
		OS.RegisterTouchWindow(handle, 0);
	} else {
		OS.UnregisterTouchWindow(handle);
	}
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
	long oldProc = windowProc ();
	long newProc = display.windowProc;
	if (oldProc == newProc) return;
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, newProc);
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
 * @param x the x coordinate in points to be translated
 * @param y the y coordinate in points to be translated
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
	return DPIUtil.autoScaleDown(toControlInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y)));
}

Point toControlInPixels (int x, int y) {
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
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
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
	point = DPIUtil.autoScaleUp(point);
	return DPIUtil.autoScaleDown(toControlInPixels(point.x, point.y));
}

/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
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
	return DPIUtil.autoScaleDown(toDisplayInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y)));
}

Point toDisplayInPixels (int x, int y) {
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
 * NOTE: To properly map a rectangle or a corner of a rectangle on a right-to-left platform, use
 * {@link Display#map(Control, Control, Rectangle)}.
 * </p>
 *
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
	point = DPIUtil.autoScaleUp(point);
	return DPIUtil.autoScaleDown(toDisplayInPixels(point.x, point.y));
}

long topHandle () {
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
	long hwnd = msg.hwnd;
	if (OS.GetKeyState (OS.VK_MENU) >= 0) {
		long code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
		if ((code & OS.DLGC_WANTALLKEYS) != 0) return false;
		if ((code & OS.DLGC_BUTTON) == 0) return false;
	}
	Decorations shell = menuShell ();
	if (shell.isVisible () && shell.isEnabled ()) {
		display.lastAscii = (int)msg.wParam;
		display.lastDead = false;
		Event event = new Event ();
		event.detail = SWT.TRAVERSE_MNEMONIC;
		if (setKeyState (event, SWT.Traverse, msg.wParam, msg.lParam)) {
			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
		}
	}
	return false;
}

boolean translateTraversal (MSG msg) {
	long hwnd = msg.hwnd;
	int key = (int)msg.wParam;
	if (key == OS.VK_MENU) {
		if ((msg.lParam & 0x40000000) == 0) {
			OS.SendMessage (hwnd, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
		}
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
			long code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
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
			long code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
			if ((code & OS.DLGC_WANTALLKEYS) != 0) doit = false;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		case OS.VK_TAB: {
			lastAscii = '\t';
			boolean next = OS.GetKeyState (OS.VK_SHIFT) >= 0;
			long code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
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
			lastVirtual = true;
			long code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
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
			long code = OS.SendMessage (hwnd, OS.WM_GETDLGCODE, 0, 0);
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
	display.lastDead = false;
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
 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
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

/**
 * Performs a platform traversal action corresponding to a <code>KeyDown</code> event.
 *
 * <p>Valid traversal values are
 * <code>SWT.TRAVERSE_NONE</code>, <code>SWT.TRAVERSE_MNEMONIC</code>,
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
 * If <code>traversal</code> is <code>SWT.TRAVERSE_NONE</code> then the Traverse
 * event is created with standard values based on the KeyDown event.  If
 * <code>traversal</code> is one of the other traversal constants then the Traverse
 * event is created with this detail, and its <code>doit</code> is taken from the
 * KeyDown event.
 * </p>
 *
 * @param traversal the type of traversal, or <code>SWT.TRAVERSE_NONE</code> to compute
 * this from <code>event</code>
 * @param event the KeyDown event
 *
 * @return <code>true</code> if the traversal succeeded
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public boolean traverse (int traversal, Event event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return traverse (traversal, event.character, event.keyCode, event.keyLocation, event.stateMask, event.doit);
}

/**
 * Performs a platform traversal action corresponding to a <code>KeyDown</code> event.
 *
 * <p>Valid traversal values are
 * <code>SWT.TRAVERSE_NONE</code>, <code>SWT.TRAVERSE_MNEMONIC</code>,
 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_ARROW_NEXT</code>, <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>,
 * <code>SWT.TRAVERSE_PAGE_NEXT</code> and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
 * If <code>traversal</code> is <code>SWT.TRAVERSE_NONE</code> then the Traverse
 * event is created with standard values based on the KeyDown event.  If
 * <code>traversal</code> is one of the other traversal constants then the Traverse
 * event is created with this detail, and its <code>doit</code> is taken from the
 * KeyDown event.
 * </p>
 *
 * @param traversal the type of traversal, or <code>SWT.TRAVERSE_NONE</code> to compute
 * this from <code>event</code>
 * @param event the KeyDown event
 *
 * @return <code>true</code> if the traversal succeeded
 *
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public boolean traverse (int traversal, KeyEvent event) {
	checkWidget ();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	return traverse (traversal, event.character, event.keyCode, event.keyLocation, event.stateMask, event.doit);
}

boolean traverse (int traversal, char character, int keyCode, int keyLocation, int stateMask, boolean doit) {
	if (traversal == SWT.TRAVERSE_NONE) {
		switch (keyCode) {
			case SWT.ESC: {
				traversal = SWT.TRAVERSE_ESCAPE;
				doit = true;
				break;
			}
			case SWT.CR: {
				traversal = SWT.TRAVERSE_RETURN;
				doit = true;
				break;
			}
			case SWT.ARROW_DOWN:
			case SWT.ARROW_RIGHT: {
				traversal = SWT.TRAVERSE_ARROW_NEXT;
				doit = false;
				break;
			}
			case SWT.ARROW_UP:
			case SWT.ARROW_LEFT: {
				traversal = SWT.TRAVERSE_ARROW_PREVIOUS;
				doit = false;
				break;
			}
			case SWT.TAB: {
				traversal = (stateMask & SWT.SHIFT) != 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
				doit = true;
				break;
			}
			case SWT.PAGE_DOWN: {
				if ((stateMask & SWT.CTRL) != 0) {
					traversal = SWT.TRAVERSE_PAGE_NEXT;
					doit = true;
				}
				break;
			}
			case SWT.PAGE_UP: {
				if ((stateMask & SWT.CTRL) != 0) {
					traversal = SWT.TRAVERSE_PAGE_PREVIOUS;
					doit = true;
				}
				break;
			}
			default: {
				if (character != 0 && (stateMask & (SWT.ALT | SWT.CTRL)) == SWT.ALT) {
					traversal = SWT.TRAVERSE_MNEMONIC;
					doit = true;
				}
				break;
			}
		}
	}

	Event event = new Event ();
	event.character = character;
	event.detail = traversal;
	event.doit = doit;
	event.keyCode = keyCode;
	event.keyLocation = keyLocation;
	event.stateMask = stateMask;

	Shell shell = getShell ();
	boolean all = false;
	switch (traversal) {
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS: {
			all = true;
			// FALL THROUGH
		}
		case SWT.TRAVERSE_ARROW_NEXT:
		case SWT.TRAVERSE_ARROW_PREVIOUS:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS: {
			/* traversal is a valid traversal action */
			break;
		}
		case SWT.TRAVERSE_MNEMONIC: {
			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
		}
		default: {
			/* traversal is not a valid traversal action */
			return false;
		}
	}

	Control control = this;
	do {
		if (control.traverse (event)) {
			OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			return true;
		}
		if (!event.doit && control.hooks (SWT.Traverse)) return false;
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
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
	long newProc = windowProc ();
	long oldProc = display.windowProc;
	if (oldProc == newProc) return;
	OS.SetWindowLongPtr (handle, OS.GWLP_WNDPROC, newProc);
}

/**
 * Forces all outstanding paint requests for the widget
 * to be processed before this method returns. If there
 * are no outstanding paint request, this method does
 * nothing.
 * <p>Note:</p>
 * <ul>
 * <li>This method does not cause a redraw.</li>
 * <li>Some OS versions forcefully perform automatic deferred painting.
 * This method does nothing in that case.</li>
 * </ul>
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
	int flags = OS.RDW_UPDATENOW;
	if (all) flags |= OS.RDW_ALLCHILDREN;
	OS.RedrawWindow (handle, null, 0, flags);
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

void updateLayout (boolean resize, boolean all) {
	/* Do nothing */
}

void updateOrientation () {
	int bits  = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		bits |= OS.WS_EX_LAYOUTRTL;
	} else {
		bits &= ~OS.WS_EX_LAYOUTRTL;
	}
	bits &= ~OS.WS_EX_RTLREADING;
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	OS.InvalidateRect (handle, null, true);
}

boolean updateTextDirection (int textDirection) {
	if (textDirection == AUTO_TEXT_DIRECTION) {
		textDirection = resolveTextDirection();
		state |= HAS_AUTO_DIRECTION;
	} else {
		state &= ~HAS_AUTO_DIRECTION;
	}
	if (textDirection == 0) return false;
	int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	/*
	* OS.WS_EX_RTLREADING means that the text direction is opposite to the
	* natural one for the current layout. So text direction would be RTL when
	* one and only one of the flags OS.WS_EX_LAYOUTRTL | OS.WS_EX_RTLREADING is
	* on.
	*/
	int flags = OS.WS_EX_LAYOUTRTL | OS.WS_EX_RTLREADING;
	boolean oldRtl = ((bits & flags) != 0 && (bits & flags) != flags);
	boolean newRtl = textDirection == SWT.RIGHT_TO_LEFT;
	if (newRtl == oldRtl) return false;
	oldRtl = (bits & OS.WS_EX_LAYOUTRTL) != 0;
	if (newRtl != oldRtl) {
		bits |= OS.WS_EX_RTLREADING;
		style |= SWT.FLIP_TEXT_DIRECTION;
	} else {
		bits &= ~OS.WS_EX_RTLREADING;
		style &= ~SWT.FLIP_TEXT_DIRECTION;
	}
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	OS.InvalidateRect (handle, null, true);
	return true;
}

CREATESTRUCT widgetCreateStruct () {
	return null;
}

int widgetExtStyle () {
	int bits = 0;

	if (!isUseWsBorder ()) {
		if ((style & SWT.BORDER) != 0) bits |= OS.WS_EX_CLIENTEDGE;
	}

	bits |= OS.WS_EX_NOINHERITLAYOUT;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) bits |= OS.WS_EX_LAYOUTRTL;
	if ((style & SWT.FLIP_TEXT_DIRECTION) != 0) bits |= OS.WS_EX_RTLREADING;
	return bits;
}

long widgetParent () {
	return parent.handle;
}

int widgetStyle () {
	/* Force clipping of siblings by setting WS_CLIPSIBLINGS */
	int bits = OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_CLIPSIBLINGS;

	if (isUseWsBorder ()) {
		if ((style & SWT.BORDER) != 0) bits |= OS.WS_BORDER;
	}

	return bits;
}

/**
 * Changes the parent of the widget to be the one provided.
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
	if (parent.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	releaseParent ();
	Shell newShell = parent.getShell (), oldShell = getShell ();
	Decorations newDecorations = parent.menuShell (), oldDecorations = menuShell ();
	if (oldShell != newShell || oldDecorations != newDecorations) {
		Menu [] menus = oldShell.findMenus (this);
		fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	}
	long topHandle = topHandle ();
	if (OS.SetParent (topHandle, parent.handle) == 0) return false;
	this.parent = parent;
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
	OS.SetWindowPos (topHandle, OS.HWND_BOTTOM, 0, 0, 0, 0, flags);
	reskin (SWT.ALL);
	return true;
}

abstract TCHAR windowClass ();

abstract long windowProc ();

long windowProc (long hwnd, int msg, long wParam, long lParam) {
	Display display = this.display;
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
		case OS.WM_GESTURE:				result = WM_GESTURE (wParam, lParam); break;
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
		case OS.WM_MOUSEHWHEEL:			result = WM_MOUSEHWHEEL (wParam, lParam); break;
		case OS.WM_MOVE:				result = WM_MOVE (wParam, lParam); break;
		case OS.WM_NCACTIVATE:			result = WM_NCACTIVATE (wParam, lParam); break;
		case OS.WM_NCCALCSIZE:			result = WM_NCCALCSIZE (wParam, lParam); break;
		case OS.WM_NCHITTEST:			result = WM_NCHITTEST (wParam, lParam); break;
		case OS.WM_NCLBUTTONDOWN:		result = WM_NCLBUTTONDOWN (wParam, lParam); break;
		case OS.WM_NCPAINT:				result = WM_NCPAINT (wParam, lParam); break;
		case OS.WM_NOTIFY:				result = WM_NOTIFY (wParam, lParam); break;
		case OS.WM_PAINT:				result = WM_PAINT (wParam, lParam); break;
		case OS.WM_ENTERMENULOOP:		result = WM_ENTERMENULOOP (wParam, lParam); break;
		case OS.WM_EXITMENULOOP:		result = WM_EXITMENULOOP (wParam, lParam); break;
		case OS.WM_ENTERSIZEMOVE:		result = WM_ENTERSIZEMOVE (wParam, lParam); break;
		case OS.WM_EXITSIZEMOVE:		result = WM_EXITSIZEMOVE (wParam, lParam); break;
		case OS.WM_PARENTNOTIFY:		result = WM_PARENTNOTIFY (wParam, lParam); break;
		case OS.WM_PASTE:				result = WM_PASTE (wParam, lParam); break;
		case OS.WM_PRINT:				result = WM_PRINT (wParam, lParam); break;
		case OS.WM_PRINTCLIENT:			result = WM_PRINTCLIENT (wParam, lParam); break;
		case OS.WM_QUERYENDSESSION:		result = WM_QUERYENDSESSION (wParam, lParam); break;
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
		case OS.WM_TABLET_FLICK:		result = WM_TABLET_FLICK (wParam, lParam); break;
		case OS.WM_TIMER:				result = WM_TIMER (wParam, lParam); break;
		case OS.WM_TOUCH:				result = WM_TOUCH (wParam, lParam); break;
		case OS.WM_UNDO:				result = WM_UNDO (wParam, lParam); break;
		case OS.WM_UNINITMENUPOPUP:		result = WM_UNINITMENUPOPUP (wParam, lParam); break;
		case OS.WM_UPDATEUISTATE:		result = WM_UPDATEUISTATE (wParam, lParam); break;
		case OS.WM_VSCROLL:				result = WM_VSCROLL (wParam, lParam); break;
		case OS.WM_WINDOWPOSCHANGED:	result = WM_WINDOWPOSCHANGED (wParam, lParam); break;
		case OS.WM_WINDOWPOSCHANGING:	result = WM_WINDOWPOSCHANGING (wParam, lParam); break;
		case OS.WM_XBUTTONDBLCLK:		result = WM_XBUTTONDBLCLK (wParam, lParam); break;
		case OS.WM_XBUTTONDOWN:			result = WM_XBUTTONDOWN (wParam, lParam); break;
		case OS.WM_XBUTTONUP:			result = WM_XBUTTONUP (wParam, lParam); break;
		case OS.WM_DPICHANGED:			result = WM_DPICHANGED (wParam, lParam); break;
	}
	if (result != null) return result.value;
	// widget could be disposed at this point
	display.sendPreExternalEventDispatchEvent ();
	try {
		return callWindowProc (hwnd, msg, wParam, lParam);
	} finally {
		// widget could be disposed at this point
		display.sendPostExternalEventDispatchEvent ();
	}
}

LRESULT WM_ACTIVATE (long wParam, long lParam) {
	return null;
}

LRESULT WM_CAPTURECHANGED (long wParam, long lParam) {
	return wmCaptureChanged (handle, wParam, lParam);
}

LRESULT WM_CHANGEUISTATE (long wParam, long lParam) {
	if ((state & IGNORE_WM_CHANGEUISTATE) != 0) return LRESULT.ZERO;
	return null;
}

LRESULT WM_CHAR (long wParam, long lParam) {
	return wmChar (handle, wParam, lParam);
}

LRESULT WM_CLEAR (long wParam, long lParam) {
	return null;
}

LRESULT WM_CLOSE (long wParam, long lParam) {
	return null;
}

LRESULT WM_COMMAND (long wParam, long lParam) {
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

LRESULT WM_CONTEXTMENU (long wParam, long lParam) {
	return wmContextMenu (handle, wParam, lParam);
}

LRESULT WM_CTLCOLOR (long wParam, long lParam) {
	Control control = display.getControl (lParam);
	if (control == null) return null;
	return control.wmColorChild (wParam, lParam);
}

LRESULT WM_CUT (long wParam, long lParam) {
	return null;
}

LRESULT WM_DESTROY (long wParam, long lParam) {
	OS.KillTimer (this.handle, Menu.ID_TOOLTIP_TIMER);
	return null;
}

LRESULT WM_DPICHANGED (long wParam, long lParam) {
	// Map DPI to Zoom and compare
	int nativeZoom = DPIUtil.mapDPIToZoom (OS.HIWORD (wParam));
	int newSWTZoom = DPIUtil.getZoomForAutoscaleProperty (nativeZoom);
	int oldSWTZoom = DPIUtil.getDeviceZoom();

	// Throw the DPI change event if zoom value changes
	if (newSWTZoom != oldSWTZoom) {
		Event event = new Event();
		event.type = SWT.ZoomChanged;
		event.widget = this;
		event.detail = newSWTZoom;
		event.doit = true;
		notifyListeners(SWT.ZoomChanged, event);
		return LRESULT.ZERO;
	}
	return LRESULT.ONE;
}

LRESULT WM_DRAWITEM (long wParam, long lParam) {
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

LRESULT WM_ENDSESSION (long wParam, long lParam) {
	return null;
}

LRESULT WM_ENTERIDLE (long wParam, long lParam) {
	return null;
}

LRESULT WM_ENTERMENULOOP (long wParam, long lParam) {
	display.externalEventLoop = true;
	return null;
}

LRESULT WM_ENTERSIZEMOVE (long wParam, long lParam) {
	display.externalEventLoop = true;
	return null;
}

LRESULT WM_ERASEBKGND (long wParam, long lParam) {
	if ((state & DRAW_BACKGROUND) != 0) {
		if (findImageControl () != null) return LRESULT.ONE;
	}
	if ((state & THEME_BACKGROUND) != 0) {
		if (OS.IsAppThemed ()) {
			if (findThemeControl () != null) return LRESULT.ONE;
		}
	}
	return null;
}

LRESULT WM_EXITMENULOOP (long wParam, long lParam) {
	display.externalEventLoop = false;
	return null;
}

LRESULT WM_EXITSIZEMOVE (long wParam, long lParam) {
	display.externalEventLoop = false;
	return null;
}

LRESULT WM_GESTURE (long wParam, long lParam) {
	if (hooks (SWT.Gesture) || filters (SWT.Gesture)) {
		GESTUREINFO gi = new GESTUREINFO ();
		gi.cbSize = GESTUREINFO.sizeof;
		if (OS.GetGestureInfo (lParam, gi)) {
			if (!sendGestureEvent (gi)) {
				OS.CloseGestureInfoHandle (lParam);
				return LRESULT.ZERO;
			}
		}
	}
	return null;
}

LRESULT WM_GETDLGCODE (long wParam, long lParam) {
	return null;
}

LRESULT WM_GETFONT (long wParam, long lParam) {
	return null;
}

LRESULT WM_GETOBJECT (long wParam, long lParam) {
	if (accessible != null) {
		long result = accessible.internal_WM_GETOBJECT (wParam, lParam);
		if (result != 0) return new LRESULT (result);
	}
	return null;
}

LRESULT WM_GETMINMAXINFO (long wParam, long lParam) {
	return null;
}

LRESULT WM_HOTKEY (long wParam, long lParam) {
	return null;
}

LRESULT WM_HELP (long wParam, long lParam) {
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
				long hwndShell = shell.handle;
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

LRESULT WM_HSCROLL (long wParam, long lParam) {
	Control control = display.getControl (lParam);
	if (control == null) return null;
	return control.wmScrollChild (wParam, lParam);
}

LRESULT WM_IME_CHAR (long wParam, long lParam) {
	return wmIMEChar (handle, wParam, lParam);
}

LRESULT WM_IME_COMPOSITION (long wParam, long lParam) {
	return null;
}

LRESULT WM_IME_COMPOSITION_START (long wParam, long lParam) {
	return null;
}

LRESULT WM_IME_ENDCOMPOSITION (long wParam, long lParam) {
	return null;
}

LRESULT WM_UNINITMENUPOPUP (long wParam, long lParam) {
	Menu hiddenMenu = menuShell ().findMenu (wParam);
	if (hiddenMenu != null) {
		Shell shell = getShell ();
		hiddenMenu.sendEvent (SWT.Hide);
		if (hiddenMenu == shell.activeMenu) shell.activeMenu = null;
	}
	return null;
}

LRESULT WM_INITMENUPOPUP (long wParam, long lParam) {

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

LRESULT WM_INPUTLANGCHANGE (long wParam, long lParam) {
	menuShell().destroyAccelerators();
	return null;
}

LRESULT WM_KEYDOWN (long wParam, long lParam) {
	return wmKeyDown (handle, wParam, lParam);
}

LRESULT WM_KEYUP (long wParam, long lParam) {
	return wmKeyUp (handle, wParam, lParam);
}

LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	/*
	 * Feature in Windows. File and directory dialogs might reset focus
	 * to NULL before they open. As a result, Shell is unable to save
	 * focus control in WM_ACTIVATE. The fix is to save focus here.
	 */
	if (wParam == 0) menuShell().setSavedFocus(this);
	return wmKillFocus (handle, wParam, lParam);
}

LRESULT WM_LBUTTONDBLCLK (long wParam, long lParam) {
	return wmLButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_LBUTTONDOWN (long wParam, long lParam) {
	return wmLButtonDown (handle, wParam, lParam);
}

LRESULT WM_LBUTTONUP (long wParam, long lParam) {
	return wmLButtonUp (handle, wParam, lParam);
}

LRESULT WM_MBUTTONDBLCLK (long wParam, long lParam) {
	return wmMButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_MBUTTONDOWN (long wParam, long lParam) {
	return wmMButtonDown (handle, wParam, lParam);
}

LRESULT WM_MBUTTONUP (long wParam, long lParam) {
	return wmMButtonUp (handle, wParam, lParam);
}

LRESULT WM_MEASUREITEM (long wParam, long lParam) {
	MEASUREITEMSTRUCT struct = new MEASUREITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, MEASUREITEMSTRUCT.sizeof);
	if (struct.CtlType == OS.ODT_MENU) {
		MenuItem item = display.getMenuItem (struct.itemID);
		if (item == null) return null;
		return item.wmMeasureChild (wParam, lParam);
	}
	long hwnd = OS.GetDlgItem (handle, struct.CtlID);
	Control control = display.getControl (hwnd);
	if (control == null) return null;
	return control.wmMeasureChild (wParam, lParam);
}

LRESULT WM_MENUCHAR (long wParam, long lParam) {
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

LRESULT WM_MENUSELECT (long wParam, long lParam) {
	int code = OS.HIWORD (wParam);
	Shell shell = getShell ();
	OS.KillTimer (this.handle, Menu.ID_TOOLTIP_TIMER);
	if (activeMenu != null)
		activeMenu.hideCurrentToolTip ();
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
				if (newMenu != null) {
					item = newMenu.cascade;
					activeMenu = newMenu;
					activeMenu.selectedMenuItem = newMenu.cascade;
					OS.SetTimer (this.handle, Menu.ID_TOOLTIP_TIMER, OS.TTM_GETDELAYTIME, 0);
				}
			}
		} else {
			Menu newMenu = menuShell.findMenu (lParam);
			if (newMenu != null) {
				int id = OS.LOWORD (wParam);
				item = display.getMenuItem (id);
			}
			activeMenu = (newMenu == null) ? menu : newMenu;
			if (item != null && activeMenu != null) {
				activeMenu.selectedMenuItem = item;
				OS.SetTimer (this.handle, Menu.ID_TOOLTIP_TIMER, OS.TTM_GETDELAYTIME, 0);
			}
		}
		if (item != null) item.sendEvent (SWT.Arm);
	}
	return null;
}

LRESULT WM_MOUSEACTIVATE (long wParam, long lParam) {
	return null;
}

LRESULT WM_MOUSEHOVER (long wParam, long lParam) {
	return wmMouseHover (handle, wParam, lParam);
}

LRESULT WM_MOUSELEAVE (long wParam, long lParam) {
	getShell ().fixToolTip ();
	return wmMouseLeave (handle, wParam, lParam);
}

LRESULT WM_MOUSEMOVE (long wParam, long lParam) {
	return wmMouseMove (handle, wParam, lParam);
}

LRESULT WM_MOUSEWHEEL (long wParam, long lParam) {
	return wmMouseWheel (handle, wParam, lParam);
}

LRESULT WM_MOUSEHWHEEL (long wParam, long lParam) {
	return wmMouseHWheel (handle, wParam, lParam);
}

LRESULT WM_MOVE (long wParam, long lParam) {
	state |= MOVE_OCCURRED;
	if (findImageControl () != null) {
		if (this != getShell ()) redrawChildren ();
	} else {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.IsAppThemed ()) {
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

LRESULT WM_NCACTIVATE (long wParam, long lParam) {
	return null;
}

LRESULT WM_NCCALCSIZE (long wParam, long lParam) {
	return null;
}

LRESULT WM_NCHITTEST (long wParam, long lParam) {
	if (!OS.IsWindowEnabled (handle)) return null;
	if (!isActive ()) return new LRESULT (OS.HTTRANSPARENT);
	return null;
}

LRESULT WM_NCLBUTTONDOWN (long wParam, long lParam) {
	return null;
}

LRESULT WM_NCPAINT (long wParam, long lParam) {
	return wmNCPaint (handle, wParam, lParam);
}

LRESULT WM_NOTIFY (long wParam, long lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	return wmNotify (hdr, wParam, lParam);
}

LRESULT WM_PAINT (long wParam, long lParam) {
	if ((state & DISPOSE_SENT) != 0) return LRESULT.ZERO;
	return wmPaint (handle, wParam, lParam);
}

LRESULT WM_PARENTNOTIFY (long wParam, long lParam) {
	return null;
}

LRESULT WM_PASTE (long wParam, long lParam) {
	return null;
}

LRESULT WM_PRINT (long wParam, long lParam) {
	return wmPrint (handle, wParam, lParam);
}

LRESULT WM_PRINTCLIENT (long wParam, long lParam) {
	return null;
}

LRESULT WM_QUERYENDSESSION (long wParam, long lParam) {
	return null;
}

LRESULT WM_QUERYOPEN (long wParam, long lParam) {
	return null;
}

LRESULT WM_RBUTTONDBLCLK (long wParam, long lParam) {
	return wmRButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_RBUTTONDOWN (long wParam, long lParam) {
	return wmRButtonDown (handle, wParam, lParam);
}

LRESULT WM_RBUTTONUP (long wParam, long lParam) {
	return wmRButtonUp (handle, wParam, lParam);
}

LRESULT WM_SETCURSOR (long wParam, long lParam) {
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

LRESULT WM_SETFOCUS (long wParam, long lParam) {
	return wmSetFocus (handle, wParam, lParam);
}

LRESULT WM_SETTINGCHANGE (long wParam, long lParam) {
	return null;
}

LRESULT WM_SETFONT (long wParam, long lParam) {
	return null;
}

LRESULT WM_SETREDRAW (long wParam, long lParam) {
	return null;
}

LRESULT WM_SHOWWINDOW (long wParam, long lParam) {
	return null;
}

LRESULT WM_SIZE (long wParam, long lParam) {
	state |= RESIZE_OCCURRED;
	if ((state & RESIZE_DEFERRED) == 0) sendEvent (SWT.Resize);
	// widget could be disposed at this point
	return null;
}

LRESULT WM_SYSCHAR (long wParam, long lParam) {
	return wmSysChar (handle, wParam, lParam);
}

LRESULT WM_SYSCOLORCHANGE (long wParam, long lParam) {
	return null;
}

LRESULT WM_SYSCOMMAND (long wParam, long lParam) {
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
	int cmd = (int)wParam & 0xFFF0;
	switch (cmd) {
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
							char key = (char) lParam;
							if (key != 0) {
								key = Character.toUpperCase (key);
								for (MenuItem item : menu.getItems ()) {
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

LRESULT WM_SYSKEYDOWN (long wParam, long lParam) {
	return wmSysKeyDown (handle, wParam, lParam);
}

LRESULT WM_SYSKEYUP (long wParam, long lParam) {
	return wmSysKeyUp (handle, wParam, lParam);
}

LRESULT WM_TABLET_FLICK (long wParam, long lParam) {
	if (!hooks (SWT.Gesture) && !filters (SWT.Gesture)) return null;
	Event event = new Event ();
	FLICK_DATA fData = new FLICK_DATA ();
	long [] source = new long [1];
	source[0] = wParam;
	OS.MoveMemory (fData, source, OS.FLICK_DATA_sizeof ());
	FLICK_POINT fPoint = new FLICK_POINT ();
	source [0] = lParam;
	OS.MoveMemory (fPoint, source, OS.FLICK_POINT_sizeof ());

	switch (fData.iFlickDirection) {
		case OS.FLICKDIRECTION_RIGHT:
			event.xDirection = 1;
			event.yDirection = 0;
			break;
		case OS.FLICKDIRECTION_UPRIGHT:
			event.xDirection = 1;
			event.yDirection = -1;
			break;
		case OS.FLICKDIRECTION_UP:
			event.xDirection = 0;
			event.yDirection = -1;
			break;
		case OS.FLICKDIRECTION_UPLEFT:
			event.xDirection = -1;
			event.yDirection = -1;
			break;
		case OS.FLICKDIRECTION_LEFT:
			event.xDirection = -1;
			event.yDirection = 0;
			break;
		case OS.FLICKDIRECTION_DOWNLEFT:
			event.xDirection = -1;
			event.yDirection = 1;
			break;
		case OS.FLICKDIRECTION_DOWN:
			event.xDirection = 0;
			event.yDirection = 1;
			break;
		case OS.FLICKDIRECTION_DOWNRIGHT:
			event.xDirection = 1;
			event.yDirection = 1;
			break;
	}
	event.setLocationInPixels(fPoint.x, fPoint.y);
	event.type = SWT.Gesture;
	event.detail = SWT.GESTURE_SWIPE;
	setInputState (event, SWT.Gesture);
	sendEvent (SWT.Gesture, event);
	return event.doit ? null : LRESULT.ONE;
}

LRESULT WM_TOUCH (long wParam, long lParam) {
	LRESULT result = null;
	if (hooks (SWT.Touch) || filters (SWT.Touch)) {
		int cInputs = OS.LOWORD (wParam);
		long hHeap = OS.GetProcessHeap ();
		long pInputs = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY,  cInputs * TOUCHINPUT.sizeof);
		if (pInputs != 0) {
			if (OS.GetTouchInputInfo (lParam, cInputs, pInputs, TOUCHINPUT.sizeof)) {
				TOUCHINPUT ti [] = new TOUCHINPUT [cInputs];
				for (int i = 0; i < cInputs; i++){
					ti [i] = new TOUCHINPUT ();
					OS.MoveMemory (ti [i], pInputs + i * TOUCHINPUT.sizeof, TOUCHINPUT.sizeof);
				}
				sendTouchEvent (ti);
				OS.CloseTouchInputHandle (lParam);
				result = LRESULT.ZERO;
			}
			OS.HeapFree (hHeap, 0, pInputs);
		}
	}
	return result;
}

LRESULT WM_TIMER (long wParam, long lParam) {
	if (wParam == Menu.ID_TOOLTIP_TIMER && activeMenu != null) {
		OS.KillTimer (this.handle, Menu.ID_TOOLTIP_TIMER);
		activeMenu.wmTimer (wParam, lParam);
	}
	return null;
}

LRESULT WM_UNDO (long wParam, long lParam) {
	return null;
}

LRESULT WM_UPDATEUISTATE (long wParam, long lParam) {
	return null;
}

LRESULT WM_VSCROLL (long wParam, long lParam) {
	Control control = display.getControl (lParam);
	if (control == null) return null;
	return control.wmScrollChild (wParam, lParam);
}

LRESULT WM_WINDOWPOSCHANGED (long wParam, long lParam) {
	try {
		display.resizeCount++;
		long code = callWindowProc (handle, OS.WM_WINDOWPOSCHANGED, wParam, lParam);
		return code == 0 ? LRESULT.ZERO : new LRESULT (code);
	} finally {
		--display.resizeCount;
	}
}

LRESULT WM_WINDOWPOSCHANGING (long wParam, long lParam) {
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
					long hwndParent = parent == null ? 0 : parent.handle;
					OS.MapWindowPoints (0, hwndParent, rect, 2);
					long rgn1 = OS.CreateRectRgn (rect.left, rect.top, rect.right, rect.bottom);
					long rgn2 = OS.CreateRectRgn (lpwp.x, lpwp.y, lpwp.x + lpwp.cx, lpwp.y + lpwp.cy);
					OS.CombineRgn (rgn1, rgn1, rgn2, OS.RGN_DIFF);
					int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
					OS.RedrawWindow (hwndParent, null, rgn1, flags);
					OS.DeleteObject (rgn1);
					OS.DeleteObject (rgn2);
				}
			}
		}
	}
	return null;
}

LRESULT WM_XBUTTONDBLCLK (long wParam, long lParam) {
	return wmXButtonDblClk (handle, wParam, lParam);
}

LRESULT WM_XBUTTONDOWN (long wParam, long lParam) {
	return wmXButtonDown (handle, wParam, lParam);
}

LRESULT WM_XBUTTONUP (long wParam, long lParam) {
	return wmXButtonUp (handle, wParam, lParam);
}

LRESULT wmColorChild (long wParam, long lParam) {
	Control control = findBackgroundControl ();
	if (control == null) {
		if ((state & THEME_BACKGROUND) != 0) {
			if (OS.IsAppThemed ()) {
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
		long hwnd = control.handle;
		long hBitmap = control.backgroundImage.handle;
		OS.MapWindowPoints (handle, hwnd, rect, 2);
		POINT lpPoint = new POINT ();
		OS.GetWindowOrgEx (wParam, lpPoint);
		OS.SetBrushOrgEx (wParam, -rect.left - lpPoint.x, -rect.top - lpPoint.y, lpPoint);
		long hBrush = findBrush (hBitmap, OS.BS_PATTERN);
		if ((state & DRAW_BACKGROUND) != 0) {
			long hOldBrush = OS.SelectObject (wParam, hBrush);
			OS.MapWindowPoints (hwnd, handle, rect, 2);
			OS.PatBlt (wParam, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
			OS.SelectObject (wParam, hOldBrush);
		}
		OS.SetBkMode (wParam, OS.TRANSPARENT);
		return new LRESULT (hBrush);
	}
	long hBrush = findBrush (backPixel, OS.BS_SOLID);
	if ((state & DRAW_BACKGROUND) != 0) {
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		long hOldBrush = OS.SelectObject (wParam, hBrush);
		OS.PatBlt (wParam, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
		OS.SelectObject (wParam, hOldBrush);
	}
	return new LRESULT (hBrush);
}

LRESULT wmCommandChild (long wParam, long lParam) {
	return null;
}

LRESULT wmDrawChild (long wParam, long lParam) {
	return null;
}

LRESULT wmMeasureChild (long wParam, long lParam) {
	return null;
}

LRESULT wmNotify (NMHDR hdr, long wParam, long lParam) {
	Control control = display.getControl (hdr.hwndFrom);
	if (control == null) return null;
	return control.wmNotifyChild (hdr, wParam, lParam);
}

LRESULT wmNotifyChild (NMHDR hdr, long wParam, long lParam) {
	return null;
}

LRESULT wmScrollChild (long wParam, long lParam) {
	return null;
}

}


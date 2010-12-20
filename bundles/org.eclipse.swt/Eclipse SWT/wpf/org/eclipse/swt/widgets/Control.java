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

import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.accessibility.*;

//TEMPORARY CODE
import org.eclipse.swt.effects.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * <dt><b>Events:</b>
 * <dd>DragDetect, FocusIn, FocusOut, Help, KeyDown, KeyUp, MenuDetect, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, MouseWheel, MouseHorizontalWheel, MouseVerticalWheel, Move,
 *     Paint, Resize, Traverse</dd>
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
	Cursor cursor;
	Menu menu;
	String toolTipText;
	Object layoutData;
	Accessible accessible;
	Image backgroundImage;
	Region region;
//	int drawCount;
	int foreground, background;
	int x, y, width, height;
	Font font;
	Composite parent;

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

void addWidget () {
	parent.addChild (this);
}

int borderHandle () {
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
	style |= SWT.DOUBLE_BUFFERED;
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
	return computeSize (handle, wHint, hHint, changed);
}

Point computeSize (int handle, int wHint, int hHint, boolean changed) {
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 0x7FFFFFFF;
	if (hHint == SWT.DEFAULT) height = 0x7FFFFFFF;
	width = Math.max (0, width);
	height = Math.max (0, height);
	int availSize = OS.gcnew_Size ((double)width, (double)height);
	if (availSize == 0) error (SWT.ERROR_NO_HANDLES);
	double requestWidth = OS.FrameworkElement_Width (handle);
	if (requestWidth >= 0) {
		int widthDP = OS.FrameworkElement_WidthProperty ();
		OS.DependencyObject_ClearValue (handle, widthDP);
		OS.GCHandle_Free (widthDP);
	}
	double requestHeight = OS.FrameworkElement_Height (handle);
	if (requestHeight >= 0) {
		int heightDP = OS.FrameworkElement_HeightProperty ();
		OS.DependencyObject_ClearValue (handle, heightDP);
		OS.GCHandle_Free (heightDP);
	}
	OS.UIElement_Measure (handle, availSize);
	OS.GCHandle_Free (availSize);
	int size = OS.UIElement_DesiredSize (handle);
	width = (int) OS.Size_Width (size);
	height = (int) OS.Size_Height (size);
	OS.GCHandle_Free (size);
	if (requestWidth >= 0) OS.FrameworkElement_Width (handle, requestWidth);
	if (requestHeight >= 0) OS.FrameworkElement_Height (handle, requestHeight);
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

Control computeTabGroup () {
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

Control [] computeTabList () {
	if (isTabGroup ()) {
		if (getVisible () && getEnabled ()) {
			return new Control [] {this};
		}
	}
	return new Control [0];
}

void createWidget () {
	state |= DRAG_DETECT;
	checkOrientation (parent);
	super.createWidget ();
	checkBackground ();
	checkBuffered ();
	setClipping ();
	if (defaultBackground () != 0 || (state & PARENT_BACKGROUND) != 0) {
		setBackground ();
	}
}

/**
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 */
public void setAlpha(int alpha) {
	checkWidget ();
	OS.UIElement_Opacity (handle, (alpha & 0xFF) / (double)0xFF);
}

void setClipping () {
	//accept default clipping
}

int defaultBackground () {
	return 0;
}

Font defaultFont () {
	return display.getSystemFont ();
}

int defaultForeground () {
	return display.getSystemColor (SWT.COLOR_BLACK).handle;
}

void deregister () {
	display.removeWidget (handle);
}

void destroyWidget () {
	parent.removeChild (this);
	releaseHandle ();
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
	return dragDetect (event.button, event.count, event.stateMask, event.x, event.y);
}

boolean dragDetect (int button, int count, int stateMask, int x, int y) {
	if (button != 1 || count != 1) return false;
	boolean dragging = dragDetect (x, y, false);
	if (dragging) return sendDragEvent (button, stateMask, x, y);
	return false;
}

void dragHandler () {
	int frame = display.dragDetectFrame = OS.gcnew_DispatcherFrame ();
	OS.Dispatcher_PushFrame (frame);
	OS.GCHandle_Free (frame);
	OS.GCHandle_Free (display.dragRect);
	if (display.dragMouseDown != 0) OS.GCHandle_Free (display.dragMouseDown);
	display.dragMouseDown = display.dragDetectFrame = display.dragRect = 0;
}

boolean dragDetect (double x, double y, boolean post) {
	display.dragging = false;
	double minH = OS.SystemParameters_MinimumHorizontalDragDistance ();
	double minV = OS.SystemParameters_MinimumVerticalDragDistance ();
	display.dragRect = OS.gcnew_Rect(x - minH, y - minV, minH * 2, minV * 2);
	if (post) {
		int handler = OS.gcnew_NoArgsDelegate (jniRef, "dragHandler");
		int operation = OS.Dispatcher_BeginInvoke (display.dispatcher, OS.DispatcherPriority_Send, handler);
		OS.GCHandle_Free (operation);
		OS.GCHandle_Free (handler);
	} else {
		dragHandler ();
	}
	return display.dragging;
}

void enableWidget (boolean enabled) {
	OS.UIElement_IsEnabled (handle, enabled);
}

boolean drawGripper (int x, int y, int width, int height, boolean vertical) {
	return false;
}

Control findBackgroundControl () {
	if (background != 0 || backgroundImage != null) return this;
	return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundControl () : null;
}

Control findThemeControl () {
	return background == 0 && backgroundImage == null ? parent.findThemeControl () : null;
}

Control findImageControl () {
	Control control = findBackgroundControl ();
	return control != null && control.backgroundImage != null ? control : null;
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

void fixFocus (Control focusControl) {
	Shell shell = getShell ();
	Control control = this;
	while (control != shell && (control = control.parent) != null) {
		if (control.setFocus ()) return;
	}
	shell.setSavedFocus (focusControl);
	OS.UIElement_Focus (shell.shellHandle);
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
//	if (display.focusEvent == SWT.FocusOut) return false;
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible () || !isActive ()) return false;
	if (isFocusControl ()) return true;
	shell.setSavedFocus (null);
	OS.UIElement_Focus (handle);
	if (isDisposed ()) return false;
	shell.setSavedFocus (this);
	return isFocusControl ();
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
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 */
public int getAlpha () {
	checkWidget ();
	return (int) (0XFF * OS.UIElement_Opacity (handle));
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
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return Color.wpf_new (display, control.getBackgroundColor ());
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

int getBackgroundColor () {
	int color = background;
	if (color == 0) color = defaultBackground ();
	if (color == 0) color = OS.SystemColors_ControlColor;//TODO
	return color;
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
	//FIXME
	return 2;
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
	int topHandle = topHandle ();
	Point location = parent.getLocation (this);
	int width = (int) OS.FrameworkElement_Width (topHandle);
	int height = (int) OS.FrameworkElement_Height (topHandle);
	return new Rectangle (location.x, location.y, width, height);
}

String getClipboardText () {
	String string = "";
	int text = OS.Clipboard_GetText ();
	if (text != 0) {
		string = createJavaString (text);
		OS.GCHandle_Free(text);
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
	return (state & DISABLED) == 0;
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
	return font != null ? font : defaultFont ();
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
	return Color.wpf_new (display, getForegroundColor ());
}

int getForegroundColor () {
	return foreground != 0 ? foreground : defaultForeground ();
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
	return parent.getLocation (this);
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
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget ();
	//TODO 
	return display.getPrimaryMonitor ();
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
	int topHandle = topHandle ();
	int width = (int) OS.FrameworkElement_Width (topHandle);
	int height = (int) OS.FrameworkElement_Height (topHandle);
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
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 */
public Transform getTransform () {
	int transform = OS.FrameworkElement_RenderTransform (handle);
	int matrix = OS.MatrixTransform_Matrix (transform);
	OS.GCHandle_Free (transform);
	float m11 = (float) OS.Matrix_M11 (matrix);
	float m12 = (float) OS.Matrix_M12 (matrix);
	float m21 = (float) OS.Matrix_M21 (matrix);
	float m22 = (float) OS.Matrix_M22 (matrix);
	float dx = (float) OS.Matrix_OffsetX (matrix);
	float dy = (float) OS.Matrix_OffsetY (matrix);
	OS.GCHandle_Free (matrix);
	//TODO - leaking
	return new Transform (display, m11, m12, m21, m22, dx, dy);
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
	return OS.UIElement_Visibility (topHandle ()) == OS.Visibility_Visible;
}

Control getWidgetControl () {
	return this;
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_KeyEventHandler (jniRef, "HandlePreviewKeyDown");
	OS.UIElement_PreviewKeyDown (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_KeyEventHandler (jniRef, "HandlePreviewKeyUp");
	OS.UIElement_PreviewKeyUp (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_TextCompositionEventHandler (jniRef, "HandlePreviewTextInput");
	OS.UIElement_PreviewTextInput (handle, handler);
	OS.GCHandle_Free (handler);
	
	int topHandle = topHandle ();
	handler = OS.gcnew_MouseButtonEventHandler (jniRef, "HandlePreviewMouseDown");
	OS.UIElement_PreviewMouseDown (topHandle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseButtonEventHandler (jniRef, "HandlePreviewMouseUp");
	OS.UIElement_PreviewMouseUp (topHandle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseEventHandler (jniRef, "HandleMouseEnter");
	OS.UIElement_MouseEnter (topHandle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseEventHandler (jniRef, "HandleMouseLeave");
	OS.UIElement_MouseLeave (topHandle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseEventHandler (jniRef, "HandlePreviewMouseMove");
	OS.UIElement_PreviewMouseMove (topHandle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseWheelEventHandler (jniRef, "HandlePreviewMouseWheel");
	OS.UIElement_PreviewMouseWheel (topHandle, handler);
	OS.GCHandle_Free (handler);

	handler = OS.gcnew_KeyboardFocusChangedEventHandler (jniRef, "HandlePreviewGotKeyboardFocus");
	OS.UIElement_PreviewGotKeyboardFocus (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_KeyboardFocusChangedEventHandler (jniRef, "HandleLostKeyboardFocus");
	OS.UIElement_LostKeyboardFocus (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_ContextMenuEventHandler (jniRef, "HandleContextMenuOpening");
	OS.FrameworkElement_ContextMenuOpening (handle, handler);
	OS.GCHandle_Free (handler);
	
	handler = OS.gcnew_SizeChangedEventHandler(jniRef, "HandleSizeChanged");
	OS.FrameworkElement_SizeChanged (topHandle, handler);
	OS.GCHandle_Free (handler);
	
	int typeid = OS.Canvas_typeid();
	handler = OS.gcnew_EventHandler(jniRef, "HandleTopChanged");
	int property = OS.Canvas_TopProperty();
	int dpd = OS.DependencyPropertyDescriptor_FromProperty(property, typeid);
	OS.DependencyPropertyDescriptor_AddValueChanged(dpd, topHandle, handler);
	OS.GCHandle_Free(handler);
	OS.GCHandle_Free(property);
	OS.GCHandle_Free(dpd);
	handler = OS.gcnew_EventHandler(jniRef, "HandleLeftChanged");
	property = OS.Canvas_LeftProperty();
	dpd = OS.DependencyPropertyDescriptor_FromProperty(property, typeid);
	OS.DependencyPropertyDescriptor_AddValueChanged(dpd, topHandle, handler);
	OS.GCHandle_Free(handler);
	OS.GCHandle_Free(property);
	OS.GCHandle_Free(dpd);
	OS.GCHandle_Free(typeid);
}

void HandleLeftChanged (int sender, int e) {
	int topHandle = topHandle();
	int x = (int)OS.Canvas_GetLeft(topHandle);
	int y = (int)OS.Canvas_GetTop(topHandle);
	if (x != this.x || y != this.y) {
		this.x = x;
		this.y = y;
		sendEvent(SWT.Move);
	}
}

void HandleTopChanged (int sender, int e) {
	int topHandle = topHandle();
	int x = (int)OS.Canvas_GetLeft(topHandle);
	int y = (int)OS.Canvas_GetTop(topHandle);
	if (x != this.x || y != this.y) {
		this.x = x;
		this.y = y;
		sendEvent(SWT.Move);
	}
}

void HandleContextMenuOpening (int sender, int e) {
	if (!checkEvent (e)) return;
	int mouse = OS.Mouse_GetPosition (handle);
	int x = (int) OS.Point_X (mouse);
	int y = (int) OS.Point_Y (mouse);
	OS.GCHandle_Free (mouse);
	Point point = display.map (this, null, x, y);
	showMenu (point.x, point.y);
	OS.RoutedEventArgs_Handled (e, true);
}

void HandlePreviewGotKeyboardFocus (int sender, int e) {
	if (!checkEvent (e)) return;
	if (OS.UIElement_IsKeyboardFocusWithin (handle)) return;
	sendFocusEvent (SWT.FocusIn);
}

void HandleLostKeyboardFocus (int sender, int e) {
	if (!checkEvent (e)) return;
	if (OS.UIElement_IsKeyboardFocusWithin (handle)) return;
	sendFocusEvent (SWT.FocusOut);
}

void HandlePreviewKeyDown (int sender, int e) {
	if (!checkEvent (e)) return;
	
	if (display.dragDetectFrame != 0) {
		if (OS.KeyEventArgs_Key (e) == OS.Key_Escape) {
			OS.DispatcherFrame_Continue (display.dragDetectFrame, false);
		}
	}

	/* Let OS handle mnemonics for now */	
//	if (translateMnemonic (e)) {
//		OS.RoutedEventArgs_Handled (e, true);
//		return;
//	}
	if (translateTraversal (e)) {
		OS.RoutedEventArgs_Handled (e, true);
		return;
	}
	sendKeyEvent (SWT.KeyDown, e, false);
}

void HandlePreviewKeyUp (int sender, int e) {
	if (!checkEvent (e)) return;
	sendKeyEvent (SWT.KeyUp, e, false);
}

void HandlePreviewMouseDown (int sender, int e) {
	if (!checkEvent (e)) return;
	if ((state & CANVAS) != 0) {
		OS.UIElement_CaptureMouse (handle);
	}
	
	if (OS.MouseButtonEventArgs_ChangedButton (e) == 0 && OS.MouseButtonEventArgs_ClickCount (e) == 1) {
		if ((state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect)) {
			int point = OS.MouseEventArgs_GetPosition (e, handle);
			double x = OS.Point_X (point);
			double y = OS.Point_Y (point);
			OS.GCHandle_Free (point);
			display.dragMouseDown = OS.GCHandle_Alloc (e);
			dragDetect (x, y, true);
		}
	}
	
	sendMouseEvent (SWT.MouseDown, e, false);
}

void HandlePreviewMouseUp (int sender, int e) {
	if (!checkEvent (e)) return;
	if ((state & CANVAS) != 0) {
		OS.UIElement_ReleaseMouseCapture (handle);
	}
	if (display.dragDetectFrame != 0) {
		OS.DispatcherFrame_Continue (display.dragDetectFrame, false);
	}
	sendMouseEvent (SWT.MouseUp, e, false);
}

void HandleMouseEnter (int sender, int e) {
	if (!checkEvent (e)) return;
	Control control = display.getCursorControl ();
	if (control != this) return;
	
	Control lastMouseControl = display.mouseControl;
	if (lastMouseControl != null && !lastMouseControl.isDisposed() && lastMouseControl != this) {
		if (OS.Visual_IsDescendantOf (topHandle (), lastMouseControl.topHandle())) {
			lastMouseControl.sendMouseEvent (SWT.MouseExit, e, false);
		}
	}
	display.mouseControl = this;
	sendMouseEvent (SWT.MouseEnter, e, false);
}

void HandleMouseLeave (int sender, int e) {
	if (!checkEvent (e)) return;
	if (this != display.mouseControl) return;
	sendMouseEvent (SWT.MouseExit, e, false);
}

void HandlePreviewMouseMove (int sender, int e) {
	if (!checkEvent (e)) return;
	if (display.dragDetectFrame != 0) {
		OS.RoutedEventArgs_Handled (e, true);
		int point = OS.MouseEventArgs_GetPosition (e, handle);
		boolean contains = OS.Rect_Contains (display.dragRect, point);
		OS.GCHandle_Free (point);
		if (!contains) {
			display.dragging = true;
			OS.DispatcherFrame_Continue (display.dragDetectFrame, false);
			if (display.dragMouseDown != 0) {
				sendDragEvent (display.dragMouseDown);
				OS.GCHandle_Free (display.dragMouseDown);
				display.dragMouseDown = 0;
			}
		}
		return;
	}
	Control lastMouseControl = display.mouseControl;
	if (lastMouseControl != null && !lastMouseControl.isDisposed() && lastMouseControl != this) {
		if (OS.Visual_IsAncestorOf (topHandle (), lastMouseControl.topHandle ())) {
			sendMouseEvent (SWT.MouseEnter, e, false);
		}
	}
	display.mouseControl = this;
	sendMouseEvent (SWT.MouseMove, e, false);
}

void HandlePreviewMouseWheel (int sender, int e) {
	if (!checkEvent (e)) return; 
	sendMouseEvent (SWT.MouseWheel, e, false);
}

void HandlePreviewTextInput(int sender, int e) {
	if (!checkEvent (e)) return;
	sendKeyEvent (SWT.KeyDown, e, true);
}

void HandleSizeChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	int topHandle = topHandle();
	int width = (int) OS.FrameworkElement_ActualWidth (topHandle);
	int height = (int) OS.FrameworkElement_ActualHeight (topHandle);
	if (this.width != width || this.height != height) {
		this.width = width;
		this.height = height; 
		resized ();
	}
}

void resized () {
	sendEvent (SWT.Resize);
}

boolean hasFocus () {
//	return OS.UIElement_IsFocused (handle);
//	return OS.UIElement_IsKeyboardFocused (handle);
	return display.getFocusControl() == this;
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
public int internal_new_GC (GCData data) {
	checkWidget();
	int drawingContext = data != null ? data.drawingContext : 0;
	int visual = handle;
	double width = OS.FrameworkElement_ActualWidth (handle);
	double height = OS.FrameworkElement_ActualHeight (handle);
	int rect = OS.gcnew_Rect (0, 0, width, height);
	int clip = OS.gcnew_RectangleGeometry (rect);
	if (drawingContext != 0) {
		OS.DrawingContext_PushClip(drawingContext, clip);
	} else {
		if ((state & CANVAS) != 0) {
			visual = OS.SWTCanvas_Visual (handle);
			if (visual == 0) {
				visual = OS.gcnew_DrawingVisual();
				if (visual == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				OS.SWTCanvas_Visual (handle, visual);
			}
		} else {
			visual = OS.gcnew_DrawingVisual();
			if (visual == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		}
		OS.ContainerVisual_Clip (visual, clip);
		int dc = OS.DrawingVisual_RenderOpen (visual);
		if (dc == 0) SWT.error (SWT.ERROR_NO_HANDLES);		
		drawingContext = dc;
		int drawing = OS.DrawingVisual_Drawing(visual);
		OS.DrawingContext_DrawDrawing(drawingContext, drawing);
		OS.GCHandle_Free(drawing);
	}
	OS.GCHandle_Free (rect);
	OS.GCHandle_Free (clip);
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= style & (mask | SWT.MIRRORED);
		}
		data.device = display;
		data.foreground = getForegroundColor ();
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		data.background = control.getBackgroundColor ();
		data.font = font != null ? font : defaultFont ();
		data.visual = visual;
	}
	return drawingContext;
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
public void internal_dispose_GC (int dc, GCData data) {
	checkWidget ();
	if (data != null && data.drawingContext == 0) {
		OS.DrawingContext_Close (dc);
		OS.GCHandle_Free (dc);
		OS.GCHandle_Free (data.visual);
	}
}

boolean isActive () {
//	Shell dialogShell = display.getModalDialogShell ();
//	if (dialogShell != null && dialogShell != getShell ()) {
//		return false;
//	}
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
	return false;
}

boolean isShowing () {
	/*
	* This is not complete.  Need to check if the
	* widget is obscurred by a parent or sibling.
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
//	int hDC = OS.GetDCEx (handle, 0, flags);
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
	int code = traversalCode (0, 0);
	if ((code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0) return false;
	return (code & (SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_TAB_NEXT)) != 0;
}

boolean isTabItem () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return false;
		}
	}
	int code = traversalCode (0, 0);
	return (code & (SWT.TRAVERSE_ARROW_PREVIOUS | SWT.TRAVERSE_ARROW_NEXT)) != 0;
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
	int topHandle = topHandle ();
	updateLayout (topHandle);
	return OS.UIElement_IsVisible (topHandle);
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
	if (parent == null) return; //TODO
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	int index;
	int parentHandle = parent.parentingHandle ();
	int children = OS.Panel_Children (parentHandle);
	if (control != null) {
		index = OS.UIElementCollection_IndexOf (children, control.topHandle ());
	} else {
		index = OS.UIElementCollection_Count (children) - 1;
	}
	int topHandle = topHandle ();
	if (OS.UIElementCollection_IndexOf (children, topHandle) < index) {
		OS.UIElementCollection_Remove (children, topHandle);
		OS.UIElementCollection_Insert (children, index, topHandle);
	}
	OS.GCHandle_Free (children);
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
	if (parent == null) return; //TODO
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	int index;
	int parentHandle = parent.parentingHandle ();
	int children = OS.Panel_Children (parentHandle);
	if (control != null) {
		index = Math.max (0, OS.UIElementCollection_IndexOf (children, control.topHandle ()) - 1);
	} else {
		if (parentHandle != parent.handle) {
			index = 1;
		} else {
			index = 0;
		}
	}
	int topHandle = topHandle ();
	if (OS.UIElementCollection_IndexOf (children, topHandle) > index) {
		OS.UIElementCollection_Remove (children, topHandle);
		OS.UIElementCollection_Insert (children, index, topHandle);
	}
	OS.GCHandle_Free (children);
}

Accessible new_Accessible (Control control) {
	return Accessible.internal_new_Accessible (this);
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
	return false;
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
	//TODO InvalidateVisual invalidates the entire visual tree
	OS.UIElement_InvalidateVisual (topHandle ()); 
	OS.UIElement_InvalidateVisual (handle);
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
	//TODO redraw only rect
	redraw(true);
}

void register () {
	display.addWidget (handle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	if (handle != 0) OS.GCHandle_Free (handle);
	handle = 0;
	parent = null;
}

void releaseParent () {
	parent.removeControl (this);
}

void releaseWidget () {
	super.releaseWidget ();
//	if (toolTipText != null) {
//		setToolTipText (getShell (), null);
//	}
	toolTipText = null;
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	cursor = null;
	layoutData = null;
	if (accessible != null) {
		accessible.internal_dispose_Accessible ();
	}
	accessible = null;
	region = null;
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
 * be notified when a gesture is performed on the control
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
 * @since 3.7
 */
public void removeGestureListener (GestureListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Gesture, listener);
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

boolean sendFocusEvent (int type) {
	Shell shell = getShell ();
	
	Display display = this.display;
	display.focusControl = this;
	sendEvent (type);
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
	if (color != null) {
		if (color.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (background != 0 && OS.Object_Equals (color.handle, background)) return;
		background = color.handle;
	} else {
		if (background == 0) return;
		background = 0;
	}
	setBackground ();
//	updateBackgroundColor ();
}

int backgroundHandle () {
	return handle;
}

int backgroundProperty () {
	return OS.Control_BackgroundProperty ();
} 

void setBackground () {
	int backgroundHandle = backgroundHandle ();
	int property = backgroundProperty ();
	int brush = 0;
	if ((state & PARENT_BACKGROUND) == 0) {
		if (backgroundImage != null) {
			int imageHandle = backgroundImage.handle;
			brush = OS.gcnew_ImageBrush (imageHandle);
			OS.TileBrush_TileMode (brush, OS.TileMode_Tile);
			OS.TileBrush_Stretch (brush, OS.Stretch_Fill);
			OS.TileBrush_ViewportUnits (brush, OS.BrushMappingMode_Absolute);
			int rect = OS.gcnew_Rect (0, 0, OS.BitmapSource_PixelWidth (imageHandle), OS.BitmapSource_PixelHeight (imageHandle));
			OS.TileBrush_Viewport (brush, rect);
			OS.GCHandle_Free (rect);
		} else {
			int color = background;
			if (color == 0) {
				color = defaultBackground ();
				if ((state & THEME_BACKGROUND) != 0) {
					Control themeControl = findThemeControl ();
					if (themeControl != null) {
						if (color != 0) {
							/*
							* Feature in WPF. If the control does not have a background
							* brush it does not receive input events.
							* The fix is to set a transparent background.  
							*/
							color = OS.Colors_Transparent;
						}
					}
				}
			}
			if (color != 0) {
				brush = OS.gcnew_SolidColorBrush (color);
			}
		}
	}
	if (brush != 0) {
		OS.DependencyObject_SetValue (backgroundHandle, property, brush);
		OS.GCHandle_Free (brush);
	} else {
		OS.DependencyObject_ClearValue (backgroundHandle, property);
	}
	OS.GCHandle_Free (property);
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
	setBackground ();
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
	int flags = MOVED | RESIZED;
	setBounds (x, y, Math.max (0, width), Math.max (0, height), flags);
}

int setBounds (int x, int y, int width, int height, int flags) {
	int result = 0;
	int topHandle = topHandle ();
	if ((flags & MOVED) != 0) {
		int oldX = (int) OS.Canvas_GetLeft (topHandle);
		int oldY = (int) OS.Canvas_GetTop (topHandle);
		if (oldX != x) {
			this.x = x;
			OS.Canvas_SetLeft (topHandle, x);
		}
		if (oldY != y) {
			this.y = y;
			OS.Canvas_SetTop (topHandle, y);
		}
		if (oldX != x || oldY != y) {
			sendEvent (SWT.Move);
			if (isDisposed ()) return 0;
			result |= MOVED;
		}
	}
	if ((flags & RESIZED) != 0) {
		int oldWidth = (int) OS.FrameworkElement_Width (topHandle);
		int oldHeight = (int) OS.FrameworkElement_Height (topHandle);
		if (oldWidth != width) {
			this.width = width;
			OS.FrameworkElement_Width (topHandle, width);
		}
		if (oldHeight != height) {
			this.height = height;
			OS.FrameworkElement_Height (topHandle, height);
		}
		if (oldWidth != width || oldHeight != height) {
			sendEvent (SWT.Resize);
			if (isDisposed ()) return 0;
			result |= RESIZED;
		}
	}
	return result;
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
		OS.UIElement_CaptureMouse (handle);
	} else {
		OS.UIElement_ReleaseMouseCapture (handle);
	}
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
	if (cursor != null) {
		OS.FrameworkElement_Cursor (handle, cursor.handle);
	} else {
		int property = OS.FrameworkElement_CursorProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
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
}

/**
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 */
public void setEffect(Effect effect){
	checkWidget ();
	if (effect != null && effect.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (effect != null) {
		OS.UIElement_BitmapEffect (handle, effect.handle);
		OS.UIElement_ClipToBounds (topHandle (), false);
	} else {
		OS.UIElement_BitmapEffect (handle, 0);
		setClipping();
	}
//	updateLayout(handle);
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
	if (((state & DISABLED) == 0) == enabled) return;
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
//		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
//		}
	}
	if (enabled) {
		state &= ~DISABLED;
	} else {
		state |= DISABLED;
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
	if (font != null) { 
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.font = font;
	if (font == null) font = defaultFont();
	setFont (font.handle, font.size);
}

void setFont (int font, double size) {
	if (font != 0) {
		int fontFamily = OS.Typeface_FontFamily( font);
		int style = OS.Typeface_Style (font);
		int weight = OS.Typeface_Weight (font);
		int stretch = OS.Typeface_Stretch (font);
		OS.Control_FontFamily (handle, fontFamily);
		OS.Control_FontStyle (handle, style);
		OS.Control_FontWeight (handle, weight);
		OS.Control_FontStretch (handle, stretch);
		OS.Control_FontSize (handle, size);
		OS.GCHandle_Free (fontFamily);
		OS.GCHandle_Free (style);
		OS.GCHandle_Free (weight);
		OS.GCHandle_Free (stretch);
	} else {
		int property = OS.Control_FontFamilyProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontStyleProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontWeightProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontStretchProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontSizeProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
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
	int brush = 0;
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (foreground != 0 && OS.Object_Equals (color.handle, foreground)) return;
		foreground = color.handle;
		brush = OS.gcnew_SolidColorBrush (foreground);
	} else {
		if (foreground == 0) return;
		foreground = 0;
	}
	setForegroundBrush (brush);
	if (brush != 0) OS.GCHandle_Free (brush);
}

void setForegroundBrush (int brush) {
	if (brush != 0) {
		OS.Control_Foreground (handle, brush);
	} else {
		int property = OS.Control_ForegroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
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
	int flags = MOVED;
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
	OS.FrameworkElement_ContextMenu(handle, menu != null ? menu.handle : 0);
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
//	if (drawCount == 0) {
//		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//		if ((bits & OS.WS_VISIBLE) == 0) state |= HIDDEN;
//	}
//	if (redraw) {
//		if (--drawCount == 0) {
//			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
//			if ((state & HIDDEN) != 0) {
//				state &= ~HIDDEN;
//				OS.ShowWindow (handle, OS.SW_HIDE);
//			} else {
//				if (OS.IsWinCE) {
//					OS.InvalidateRect (handle, null, true);
//				} else {
//					int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
//					OS.RedrawWindow (handle, null, 0, flags);
//				}
//			}
//		}
//	} else {
//		if (drawCount++ == 0) {
//			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
//		}
//	}
	redraw ();
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
	OS.UIElement_Clip (topHandle (), region.handle);
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
	int flags = RESIZED;
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

boolean setTabGroupFocus () {
	return setTabItemFocus ();
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
	if (string != null && string.length() == 0) string = null;
	int strPtr = createDotNetString (string, false);
	OS.FrameworkElement_ToolTip (handle, strPtr);
	if (strPtr != 0) OS.GCHandle_Free (strPtr);
}

/**
 * WARNING: THIS API IS UNDER CONSTRUCTION AND SHOULD NOT BE USED
 */
public void setTransform (Transform t) {
	checkWidget ();
	if (t == null) {
		OS.FrameworkElement_RenderTransform (handle, 0);
		return;
	}
	int matrixTransform = OS.gcnew_MatrixTransform (t.handle);
//	OS.FrameworkElement_LayoutTransform (handle, matrixTransform);
	OS.FrameworkElement_RenderTransform (handle, matrixTransform);
	OS.GCHandle_Free (matrixTransform);
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
	int topHandle = topHandle ();
	if ((OS.UIElement_Visibility (topHandle) == OS.Visibility_Visible) == visible) return;
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
//		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
//		}
	}
	OS.UIElement_Visibility (topHandle, visible ? OS.Visibility_Visible : OS.Visibility_Hidden);
	if (isDisposed ()) return;
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

void sortAscending (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] >= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
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
	return display.map (null, this, x, y);
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
	return display.map (this, null, x, y);
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

boolean translateMnemonic (Event event, Control control) {
	if (control == this) return false;
	if (!isVisible () || !isEnabled ()) return false;
	event.doit = mnemonicMatch (event.character);
	return traverse (event);
}

boolean translateMnemonic (int e) {
	int key = OS.KeyEventArgs_Key(e);
	if (key < 0x20) return false;
	int keyboardDevice = OS.KeyboardEventArgs_KeyboardDevice(e);
	int modifiers = OS.KeyboardDevice_Modifiers(keyboardDevice);
	OS.GCHandle_Free(keyboardDevice);
	if (modifiers == 0) {
		int code = traversalCode (key, e);
		if ((code & SWT.TRAVERSE_MNEMONIC) == 0) return false;
	} else {
		if ((modifiers & OS.ModifierKeys_Alt) == 0) return false;
	}
	Decorations shell = menuShell ();
	if (shell.isVisible () && shell.isEnabled ()) {
		Event event = new Event ();
		event.detail = SWT.TRAVERSE_MNEMONIC;
		if (setKeyState (event, SWT.Traverse, e)) {
			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
		}
	}
	return false;
}

int traversalCode (int key, int event) {
	int code = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_TAB_NEXT |  SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_PAGE_NEXT | SWT.TRAVERSE_PAGE_PREVIOUS;
	Shell shell = getShell ();
	if (shell.parent != null) code |= SWT.TRAVERSE_ESCAPE;
	return code;
}

boolean translateTraversal (int e) {
	int detail = SWT.TRAVERSE_NONE;
	int key = OS.KeyEventArgs_Key(e);
	int code = traversalCode (key, e);
	boolean all = false;
	switch (key) {
		case OS.Key_Escape: {
			all = true;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		}
		case OS.Key_Return: {
			all = true;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		case OS.Key_Tab: {
			int keyboardDevice = OS.KeyboardEventArgs_KeyboardDevice (e);
			int modifiers = OS.KeyboardDevice_Modifiers (keyboardDevice);
			OS.GCHandle_Free (keyboardDevice);
			boolean next = (modifiers & OS.ModifierKeys_Shift) == 0;
			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		}
		case OS.Key_Up:
		case OS.Key_Left: 
		case OS.Key_Down:
		case OS.Key_Right: {
			boolean next = key == OS.Key_Down || key == OS.Key_Right;
			detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		}
		case OS.Key_PageUp:
		case OS.Key_PageDown: {
			all = true;
			int keyboardDevice = OS.KeyboardEventArgs_KeyboardDevice (e);
			int modifiers = OS.KeyboardDevice_Modifiers (keyboardDevice);
			OS.GCHandle_Free (keyboardDevice);
			if ((modifiers & OS.ModifierKeys_Control) == 0) return false;
			detail = key == OS.Key_PageDown ? SWT.TRAVERSE_PAGE_NEXT : SWT.TRAVERSE_PAGE_PREVIOUS;
			break;
		}
		default:
			return false;
	}
	Event event = new Event ();
	event.doit = (code & detail) != 0;
	event.detail = detail;
	event.time = OS.InputEventArgs_Timestamp (e);
	if (!setKeyState (event, SWT.Traverse, e)) return false;
	Shell shell = getShell ();
	Control control = this;
	do {
		if (control.traverse (event)) return true;
		if (!event.doit && control.hooks (SWT.Traverse)) return false;
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
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
	// the following should work but is not tested

//	if (traversal == SWT.TRAVERSE_NONE) {
//		switch (keyCode) {
//			case SWT.ESC: {
//				traversal = SWT.TRAVERSE_ESCAPE;
//				doit = true;
//				break;
//			}
//			case SWT.CR: {
//				traversal = SWT.TRAVERSE_RETURN;
//				doit = true;
//				break;
//			}
//			case SWT.ARROW_DOWN:
//			case SWT.ARROW_RIGHT: {
//				traversal = SWT.TRAVERSE_ARROW_NEXT;
//				doit = false;
//				break;
//			}
//			case SWT.ARROW_UP:
//			case SWT.ARROW_LEFT: {
//				traversal = SWT.TRAVERSE_ARROW_PREVIOUS;
//				doit = false;
//				break;
//			}
//			case SWT.TAB: {
//				traversal = (stateMask & SWT.SHIFT) != 0 ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT;
//				doit = true;
//				break;
//			}
//			case SWT.PAGE_DOWN: {
//				if ((stateMask & SWT.CTRL) != 0) {
//					traversal = SWT.TRAVERSE_PAGE_NEXT;
//					doit = true;
//				}
//				break;
//			}
//			case SWT.PAGE_UP: {
//				if ((stateMask & SWT.CTRL) != 0) {
//					traversal = SWT.TRAVERSE_PAGE_PREVIOUS;
//					doit = true;
//				}
//				break;
//			}
//			default: {
//				if (character != 0 && (stateMask & (SWT.ALT | SWT.CTRL)) == SWT.ALT) {
//					traversal = SWT.TRAVERSE_MNEMONIC;
//					doit = true;
//				}
//				break;
//			}
//		}
//	}
//
//	Event event = new Event ();
//	event.character = character;
//	event.detail = traversal;
//	event.doit = doit;
//	event.keyCode = keyCode;
//	event.keyLocation = keyLocation;
//	event.stateMask = stateMask;
//	Shell shell = getShell ();
//
//	boolean all = false;
//	switch (traversal) {
//		case SWT.TRAVERSE_ESCAPE:
//		case SWT.TRAVERSE_RETURN:
//		case SWT.TRAVERSE_PAGE_NEXT:
//		case SWT.TRAVERSE_PAGE_PREVIOUS: {
//			all = true;
//			// FALL THROUGH
//		}
//		case SWT.TRAVERSE_ARROW_NEXT:
//		case SWT.TRAVERSE_ARROW_PREVIOUS:
//		case SWT.TRAVERSE_TAB_NEXT:
//		case SWT.TRAVERSE_TAB_PREVIOUS: {
//			/* traversal is a valid traversal action */
//			break;
//		}
//		case SWT.TRAVERSE_MNEMONIC: {
//			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
//		}
//		default: {
//			/* traversal is not a valid traversal action */
//			return false;
//		}
//	}
//
//	Control control = this;
//	do {
//		if (control.traverse (event)) return true;
//		if (!event.doit && control.hooks (SWT.Traverse)) return false;
//		if (control == shell) return false;
//		control = control.parent;
//	} while (all && control != null);
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

boolean traverseEscape () {
	return false;
}

boolean traverseGroup (boolean next) {
	Control root = computeTabRoot ();
	Control group = computeTabGroup ();
	Control [] list = root.computeTabList ();
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
		Control control = list [index];
		if (!control.isDisposed () && control.setTabGroupFocus ()) {
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
	return mnemonicHit (key);
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseReturn () {
	return false;
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
	checkWidget ();
	//TODO 
//	if (OS.IsWinCE) {
//		OS.UpdateWindow (handle);
//	} else {
//		int flags = OS.RDW_UPDATENOW;
//		if (all) flags |= OS.RDW_ALLCHILDREN;
//		OS.RedrawWindow (handle, null, 0, flags);
//	}
}

void updateBackgroundColor () {
//	Control control = findBackgroundControl ();
//	if (control == null) control = this;
//	setBackgroundPixel (control.background);
}

void updateBackgroundImage () {
//	Control control = findBackgroundControl ();
//	Image image = control != null ? control.backgroundImage : backgroundImage;
//	setBackgroundImage (image != null ? image.handle : 0);
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

int widgetParent () {
	return parent.handle;
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
	reskin (SWT.ALL);
	if (this.parent == parent) return true;
	if (!isReparentable ()) return false;
	return false;
}

}


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


import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
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
	int drawCount, redrawWindow;
	Composite parent;
	Cursor cursor;
	Menu menu;
	Image backgroundImage;
	Font font;
	Region region;
	String toolTipText;
	Object layoutData;
	Accessible accessible;

Control () {
	/* Do nothing */
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
	createWidget (0);
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
	checkWidget();
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
public void addFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.FocusIn,typedListener);
	addListener(SWT.FocusOut,typedListener);
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
	checkWidget();
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
public void addKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
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
public void addMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseDown,typedListener);
	addListener(SWT.MouseUp,typedListener);
	addListener(SWT.MouseDoubleClick,typedListener);
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
public void addMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseMove,typedListener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
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
public void addPaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Paint,typedListener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}
int borderHandle () {
	return topHandle ();
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
void checkBuffered () {
	style &= ~SWT.DOUBLE_BUFFERED;
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
	checkWidget();
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
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

void createWidget (int index) {
	state |= DRAG_DETECT;
	checkOrientation (parent);
	super.createWidget (index);
	checkBackground ();
	checkBuffered ();
	setParentTraversal ();
	overrideTranslations ();
	
	/*
	* Register for the IME.  This is necessary on single byte
	* platforms as well as double byte platforms in order to
	* get composed characters. For example, accented characters
	* on a German locale.
	*/
	if (!hasIMSupport()) {
		OS.XmImRegister (handle, 0);
		int focusHandle = focusHandle ();
		if (handle != focusHandle) {
			OS.XmImRegister (focusHandle, 0);
		}
	}
	
	/*
	* Feature in MOTIF.  When a widget is created before the
	* parent has been realized, the widget is created behind
	* all siblings in the Z-order.  When a widget is created
	* after the parent has been realized, it is created in
	* front of all siblings.  This is not incorrect but is
	* unexpected.  The fix is to force all widgets to always
	* be created behind their siblings.
	*/
	int topHandle = topHandle ();
	if (OS.XtIsRealized (topHandle) && !OS.XtIsSubclass (topHandle, OS.shellWidgetClass ())) {
		int window = OS.XtWindow (topHandle);
		if (window != 0) {
			int display = OS.XtDisplay (topHandle);
			if (display != 0) OS.XLowerWindow (display, window);
		}
		/*
		* Make sure that the widget has been properly realized
		* because the widget was created after the parent
		* has been realized.  This is not part of the fix
		* for Z-order in the code above. 
		*/
		realizeChildren ();
	}
	
	/*
	* Bug in Motif.  Under certain circumstances, when a
	* text widget is created as a child of another control
	* that has drag and drop, starting a drag in the text
	* widget causes a protection fault.  The fix is to
	* disable the built in drag and drop for all widgets
	* by overriding the drag start traslation.
	*/
	OS.XtOverrideTranslations (handle, display.dragTranslations);
	
	/*
	* Feature in Motif.  When the XmNfontList resource is set for
	* a widget, Motif creates a copy of the fontList and disposes
	* the copy when the widget is disposed.  This means that when
	* the programmer queries the font, not only will the handle be
	* different but the font will be unexpectedly disposed when
	* the widget is disposed.  This can cause GP's when the font
	* is set in another widget.  The fix is to cache the font that
	* the programmer provides.  The initial value of the cache is
	* the default font for the widget.
	*/
	font = defaultFont ();
}
int defaultBackground () {
	return display.defaultBackground;
}
Font defaultFont () {
	return display.defaultFont;
}
int defaultForeground () {
	return display.defaultForeground;
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
	if (button != 2 || count != 1) return false;
	if (!dragDetect (x, y, false, null)) return false;
	return sendDragEvent (button, stateMask, x, y, true);
}
boolean dragDetect (int x, int y, boolean force, boolean [] consume) {
	return true;
}
boolean drawGripper (int x, int y, int width, int height, boolean vertical) {
	return false;
}
void enableWidget (boolean enabled) {
	enableHandle (enabled, handle);
}
Control findBackgroundControl () {
	if ((state & BACKGROUND) != 0 || backgroundImage != null) return this;
	return (state & PARENT_BACKGROUND) != 0 ? parent.findBackgroundControl () : null;
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
}
int focusHandle () {
	return handle;
}
int fontHandle () {
	return handle;
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
	checkWidget();
	if (display.focusEvent == SWT.FocusOut) return false;
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	shell.bringToTop (false);
	int focusHandle = focusHandle ();
	if (handle != focusHandle) {
		int [] argList1 = {OS.XmNnumChildren, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		if (argList1 [1] > 1) {
			int [] argList = new int [] {OS.XmNtraversalOn, 0};
			OS.XtGetValues (focusHandle, argList, argList.length / 2);
			if (argList [1] == 0) {
				argList [1] = 1;
				OS.XtSetValues (focusHandle, argList, argList.length / 2);
				overrideTranslations ();
			}
		} else {
			focusHandle = handle;
		}
	}
	int [] argList = new int [] {OS.XmNtraversalOn, 0};
	OS.XtGetValues (focusHandle, argList, argList.length / 2);
	boolean force = argList [1] == 0;
	if (force) {
		state |= FOCUS_FORCED;
		argList [1] = 1;
		OS.XtSetValues (focusHandle, argList, argList.length / 2);
		overrideTranslations ();
	}
	if (XmProcessTraversal (focusHandle, OS.XmTRAVERSE_CURRENT)) return true;
	if (force) {
		state &= ~FOCUS_FORCED;
		argList [1] = 0;
		OS.XtSetValues (focusHandle, argList, argList.length / 2);
	}
	return false;
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
	if (accessible == null) {
		accessible = Accessible.internal_new_Accessible (this);
	}
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
	checkWidget();
	Control control = findBackgroundControl ();
	if (control == null) control = this;
	return Color.motif_new (display, getXColor (control.getBackgroundPixel ()));
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
	int [] argList = {OS.XmNbackground, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
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
	checkWidget();
	int borderHandle = borderHandle ();
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (borderHandle, argList, argList.length / 2);
	return argList [1];
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
	checkWidget();
	int topHandle = topHandle ();
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int borders = argList [9] * 2;
	return new Rectangle ((short) argList [1], (short) argList [3], argList [5] + borders, argList [7] + borders);
}
Point getClientLocation () {
	short [] handle_x = new short [1], handle_y = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, handle_x, handle_y);
	short [] topHandle_x = new short [1], topHandle_y = new short [1];
	OS.XtTranslateCoords (parent.handle, (short) 0, (short) 0, topHandle_x, topHandle_y);
	return new Point (handle_x [0] - topHandle_x [0], handle_y [0] - topHandle_y [0]);
}
String getCodePage () {
	return font.codePage;
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
	checkWidget();
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (topHandle (), argList, argList.length / 2);
	return argList [1] != 0;
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
	checkWidget();
	return font;
}

int getFontAscent (int font) {
	
	/* Create a font context to iterate over each element in the font list */
	int [] buffer = new int [1];
	if (!OS.XmFontListInitFontContext (buffer, font)) {
		error (SWT.ERROR_NO_HANDLES);
	}
	int context = buffer [0];
	
	/* Values discovering during iteration */
	int ascent = 0;
	XFontStruct fontStruct = new XFontStruct ();
	int fontListEntry;
	int [] fontStructPtr = new int [1];
	int [] fontNamePtr = new int [1];
	
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
		if (buffer [0] == 0) { 
			/* FontList contains a single font */
			OS.memmove (fontStruct, fontPtr, XFontStruct.sizeof);
			if (fontStruct.ascent > ascent) ascent = fontStruct.ascent;
		} else {
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int [nFonts];
			OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
			
			/* Go through each fontStruct in the font set */
			for (int i=0; i<nFonts; i++) { 
				OS.memmove (fontStruct, fontStructs[i], XFontStruct.sizeof);
				if (fontStruct.ascent > ascent) ascent = fontStruct.ascent;
			}
		}
	}
	
	OS.XmFontListFreeFontContext (context);
	return ascent;
}

int getFontHeight (int font) {

	/* Create a font context to iterate over each element in the font list */
	int [] buffer = new int [1];
	if (!OS.XmFontListInitFontContext (buffer, font)) {
		error (SWT.ERROR_NO_HANDLES);
	}
	int context = buffer [0];
	
	/* Values discovering during iteration */
	int height = 0;
	XFontStruct fontStruct = new XFontStruct ();
	int fontListEntry;
	int [] fontStructPtr = new int [1];
	int [] fontNamePtr = new int [1];
	
	/* Go through each entry in the font list. */
	while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
		if (buffer [0] == 0) { 
			/* FontList contains a single font */
			OS.memmove (fontStruct, fontPtr, XFontStruct.sizeof);
			int fontHeight = fontStruct.ascent + fontStruct.descent;
			if (fontHeight > height) height = fontHeight;
		} else {
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
			int [] fontStructs = new int [nFonts];
			OS.memmove (fontStructs, fontStructPtr [0], nFonts * 4);
			
			/* Go through each fontStruct in the font set */
			for (int i=0; i<nFonts; i++) { 
				OS.memmove (fontStruct, fontStructs[i], XFontStruct.sizeof);
				int fontHeight = fontStruct.ascent + fontStruct.descent;
				if (fontHeight > height) height = fontHeight;
			}
		}
	}
	
	OS.XmFontListFreeFontContext (context);
	return height;
}
//int getFontList () {
//	int fontHandle = fontHandle ();
//	int [] argList = {OS.XmNfontList, 0};
//	OS.XtGetValues (fontHandle, argList, argList.length / 2);
//	if (argList [1] != 0) return argList [1];
//	if (fontList == 0) fontList = defaultFont ();
//	return fontList;
//}
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
	checkWidget();
	return Color.motif_new (display, getXColor (getForegroundPixel ()));
}
int getForegroundPixel () {
	int [] argList = {OS.XmNforeground, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
Caret getIMCaret () {
	return null;
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
	checkWidget();
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
	checkWidget();
	int topHandle = topHandle ();
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	return new Point ((short) argList [1], (short) argList [3]);
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
	checkWidget();
	return menu;
}
int getMinimumHeight () {
	return 0;
}
/**
 * Returns the receiver's monitor.
 * 
 * @return the receiver's monitor
 * 
 * @since 3.0
 */
public Monitor getMonitor () {
	checkWidget();
	Monitor [] monitors = display.getMonitors ();
	if (monitors.length == 1) return monitors [0];
	int index = -1, value = -1;
	Rectangle bounds = getBounds ();
	if (this != getShell ()) {
		bounds = display.map (this.parent, null, bounds);
	}
	for (int i=0; i<monitors.length; i++) {
		Rectangle rect = bounds.intersection (monitors [i].getBounds ());
		int area = rect.width * rect.height;
		if (area > 0 && area > value) {
			index = i;
			value = area;
		}
	}
	if (index >= 0) return monitors [index];
	int centerX = bounds.x + bounds.width / 2, centerY = bounds.y + bounds.height / 2;
	for (int i=0; i<monitors.length; i++) {
		Rectangle rect = monitors [i].getBounds ();
		int x = centerX < rect.x ? rect.x - centerX : centerX > rect.x + rect.width ? centerX - rect.x - rect.width : 0;
		int y = centerY < rect.y ? rect.y - centerY : centerY > rect.y + rect.height ? centerY - rect.y - rect.height : 0;
		int distance = x * x + y * y;
		if (index == -1 || distance < value) {
			index = i;
			value = distance;
		} 
	}
	return monitors [index];
}
int getNavigationType () {
	int [] argList = {OS.XmNnavigationType, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
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
	checkWidget();
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
	checkWidget();
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
	checkWidget();
	int topHandle = topHandle ();
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	int borders = argList [5] * 2;
	return new Point (argList [1] + borders, argList [3] + borders);
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
	checkWidget();
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
	checkWidget();
	int topHandle = topHandle ();
	int [] argList = {OS.XmNmappedWhenManaged, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	return argList [1] != 0;
}
XColor getXColor (int pixel) {
	int display = OS.XtDisplay (handle);
	if (display == 0) return null;
	int [] argList = {OS.XmNcolormap, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int colormap = argList [1];
	if (colormap == 0) return null;
	XColor color = new XColor ();
	color.pixel = pixel;
	OS.XQueryColor (display, colormap, color);
	return color;
}
boolean hasFocus () {
	return this == display.getFocusControl ();
}
/**
 * Returns true if the widget has native IM support
 */
boolean hasIMSupport() {
	return false;
}
void hookEvents () {
	int windowProc = display.windowProc;
	OS.XtAddEventHandler (handle, OS.ButtonPressMask, false, windowProc, BUTTON_PRESS);
	OS.XtAddEventHandler (handle, OS.ButtonReleaseMask, false, windowProc, BUTTON_RELEASE);
	OS.XtAddEventHandler (handle, OS.PointerMotionMask, false, windowProc, POINTER_MOTION);
	OS.XtAddEventHandler (handle, OS.EnterWindowMask, false, windowProc, ENTER_WINDOW);
	OS.XtAddEventHandler (handle, OS.LeaveWindowMask, false, windowProc, LEAVE_WINDOW);
	OS.XtInsertEventHandler (handle, OS.ExposureMask, false, windowProc, EXPOSURE, OS.XtListTail);
	OS.XtAddCallback (handle, OS.XmNhelpCallback, windowProc, HELP_CALLBACK);
	OS.XtAddEventHandler (handle, OS.KeyPressMask, false, windowProc, KEY_PRESS);
	OS.XtAddEventHandler (handle, OS.KeyReleaseMask, false, windowProc, KEY_RELEASE);
	OS.XtInsertEventHandler (handle, OS.FocusChangeMask, false, windowProc, FOCUS_CHANGE, OS.XtListTail);
	int focusHandle = focusHandle ();
	if (handle != focusHandle) {
		OS.XtAddEventHandler (focusHandle, OS.KeyPressMask, false, windowProc, KEY_PRESS);
		OS.XtAddEventHandler (focusHandle, OS.KeyReleaseMask, false, windowProc, KEY_RELEASE);
		OS.XtInsertEventHandler (focusHandle, OS.FocusChangeMask, false, windowProc, FOCUS_CHANGE, OS.XtListTail);
	}
}
int hoverProc (int id) {
	return hoverProc (id, true);
}
int hoverProc (int id, boolean showTip) {
	if (showTip) display.showToolTip (handle, toolTipText);
	sendMouseEvent (SWT.MouseHover);
	return 0;
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
public int internal_new_GC (GCData data) {
	checkWidget();
	if (!OS.XtIsRealized (handle)) {
		Shell shell = getShell ();
		shell.realizeWidget ();
	}
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int xGC = OS.XCreateGC (xDisplay, xWindow, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XSetGraphicsExposures (xDisplay, xGC, false);
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= style & (mask | SWT.MIRRORED);
		}
		data.device = display;
		data.display = xDisplay;
		data.drawable = xWindow;
		XColor foreground = new XColor ();
		foreground.pixel = getForegroundPixel ();
		data.foreground = foreground;
		Control control = findBackgroundControl ();
		if (control == null) control = this;
		XColor background = new XColor ();
		background.pixel = control.getBackgroundPixel ();
		data.background = background;
		data.backgroundImage = control.backgroundImage;
		data.font = font;
		int [] argList = {OS.XmNcolormap, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		data.colormap = argList [1];
	}
	return xGC;
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
public void internal_dispose_GC (int xGC, GCData data) {
	checkWidget ();
	int xDisplay = 0;
	if (data != null) xDisplay = data.display;
	if (xDisplay == 0 && handle != 0) xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.XFreeGC (xDisplay, xGC);
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
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}
boolean isFocusAncestor (Control control) {
	while (control != null && control != this && !(control instanceof Shell)) {
		control = control.parent;
	}
	return control == this;
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
	checkWidget();
	return hasFocus ();
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
	checkWidget();
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
		if (size.x == 1 || size.y == 1) {
			return false;
		}
		control = control.parent;
	}
	return true;
}
boolean isTabGroup () {
	Control [] tabList = parent._getTabList ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			if (tabList [i] == this) return true;
		}
	}
	int code = traversalCode (0, null);
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
	int code = traversalCode (0, null);
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
	checkWidget();
	return getVisible () && parent.isVisible ();
}
void manageChildren () {
	OS.XtSetMappedWhenManaged (handle, false);
	OS.XtManageChild (handle);
	int [] argList3 = {OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList3, argList3.length / 2);
	OS.XtResizeWidget (handle, 1, 1, argList3 [1]);
	OS.XtSetMappedWhenManaged (handle, true);
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
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, true);
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
	checkWidget();
	if (control != null) {
		if (control.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
	}
	setZOrder (control, false);
}
void overrideTranslations () {
	OS.XtOverrideTranslations (handle, display.tabTranslations);
	OS.XtOverrideTranslations (handle, display.arrowTranslations);
	int focusHandle = focusHandle ();
	if (handle != focusHandle) {
		OS.XtOverrideTranslations (focusHandle, display.tabTranslations);
		OS.XtOverrideTranslations (focusHandle, display.arrowTranslations);
	}
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
	checkWidget();
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
	checkWidget();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}
void propagateChildren (boolean enabled) {
	propagateWidget (enabled);
}
void propagateWidget (boolean enabled) {
	int xCursor = enabled && cursor != null ? cursor.handle : OS.None;
	propagateHandle (enabled, handle, xCursor);
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
void realizeChildren () {
	if (isEnabled ()) {
		if (cursor != null) {
			int xWindow = OS.XtWindow (handle);
			if (xWindow == 0) return;
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			OS.XDefineCursor (xDisplay, xWindow, cursor.handle);
			OS.XFlush (xDisplay);
		}
	} else {
		propagateWidget (false);
	}
	if ((state & PARENT_BACKGROUND) != 0) {
		setParentBackground ();
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
	checkWidget();
	redrawWidget (0, 0, 0, 0, true, false, false);
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
	if (width > 0 && height > 0) {
		redrawWidget (x, y, width, height, false, all, false);
	}
}
void redrawChildren () {
}
void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean allChildren, boolean trim) {
	redrawHandle (x, y, width, height, redrawAll, handle);
}
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}
void releaseParent () {
	parent.removeControl (this);
}
void releaseWidget () {
	super.releaseWidget ();
	/*
	* Restore the default font for the widget in case the
	* application disposes the widget font in the dispose
	* callback.  If a font is disposed while it is still
	* in use in the widget, Motif GP's.
	*/
	int fontList = defaultFont ().handle;
	if (font.handle != fontList) {
		int fontHandle = fontHandle ();
		int [] argList2 = {OS.XmNfontList, fontList};
		OS.XtSetValues (fontHandle, argList2, argList2.length / 2);
		OS.XmImSetValues (fontHandle, argList2, argList2.length / 2);
	}
	display.releaseToolTipHandle (handle);
	toolTipText = null;
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	cursor = null;
	if (!hasIMSupport()) {
		OS.XmImUnregister (handle);
		int focusHandle = focusHandle ();
		if (handle != focusHandle) {
			OS.XmImUnregister (focusHandle);
		}
	}
	layoutData = null;
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
	checkWidget();
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.FocusIn, listener);
	eventTable.unhook(SWT.FocusOut, listener);
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
	checkWidget();
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.KeyUp, listener);
	eventTable.unhook(SWT.KeyDown, listener);
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
public void removeMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseDown, listener);
	eventTable.unhook(SWT.MouseUp, listener);
	eventTable.unhook(SWT.MouseDoubleClick, listener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseMove, listener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}/**
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}
boolean sendDragEvent (int button, int stateMask, int x, int y, boolean isStateMask) {
	Event event = new Event ();
	event.button = button;
	event.x = x;
	event.y = y;
	if (isStateMask) {
		event.stateMask = stateMask;
	} else {
		setInputState (event, stateMask);
	}
	postEvent (SWT.DragDetect, event);
	if (isDisposed ()) return false;
	return event.doit;
}
void sendFocusEvent (int type) {
	Display display = this.display;
	if (type == SWT.FocusIn) {
		Control focusedCombo = display.focusedCombo;
		display.focusedCombo = null;
		if (focusedCombo != null && focusedCombo != this && !focusedCombo.isDisposed ()) {
			display.sendFocusEvent (focusedCombo, SWT.FocusOut);
		}
	}
	display.sendFocusEvent (this, type);
}
void sendHelpEvent (int callData) {
	Control control = this;
	while (control != null) {
		if (control.hooks (SWT.Help)) {
			control.postEvent (SWT.Help);
			return;
		}
		control = control.parent;
	}
}
boolean sendMouseEvent (int type) {
	if (!hooks (type) && !filters (type)) return true;
	int xDisplay = OS.XtDisplay (handle), xWindow = OS.XtWindow (handle);
	int [] windowX = new int [1], windowY = new int [1], mask = new int [1], unused = new int [1];
	OS.XQueryPointer (xDisplay, xWindow, unused, unused, unused, unused, windowX, windowY, mask);
	return sendMouseEvent (type, 0, 0, 0, false, 0, windowX [0], windowY [0], mask [0]);
}
boolean sendMouseEvent (int type, int button, int count, int detail, boolean send, int time, int x, int y, int state) {
//	if (!hooks (type) && !filters (type)) return true;
	Event event = new Event ();
	event.time = time;
	event.button = button;
	event.count = count;
	event.detail = detail;
	event.x = x;
	event.y = y;
	setInputState (event, state);
	if (send) {
		sendEvent (type, event);
		if (isDisposed ()) return false;
	} else {
		postEvent (type, event);
	}
	return event.doit;
}
boolean sendMouseEvent (int type, XButtonEvent xEvent) {
	int button = xEvent.button;
	switch (button) {
		case 4:
		case 5:
		case 6:
		case 7:
			if (type != SWT.MouseDown) return true;
			type = button == 4 || button == 5 ? SWT.MouseWheel : SWT.MouseHorizontalWheel;
			if (!hooks (type) && !filters (type)) return true;
			int count = button == 4 || button == 6 ? 3 : -3;
			int detail = button == 4 || button == 5 ? SWT.SCROLL_LINE : SWT.NONE;
			short [] x_root = new short [1], y_root = new short [1];
			OS.XtTranslateCoords (handle, (short) 0, (short) 0, x_root, y_root);
			int x = xEvent.x_root - x_root [0], y = xEvent.y_root - y_root [0];
			Control control = this;
			Shell shell = getShell ();
			do {
				if (!control.sendMouseEvent (type, 0, count, detail, true, xEvent.time, x, y, xEvent.state)) {
					return false;
				}
				if (control == shell) break;
				control = control.parent;
				OS.XtTranslateCoords (control.handle, (short) 0, (short) 0, x_root, y_root);
				x = xEvent.x_root - x_root [0];
				y = xEvent.y_root - y_root [0];			
			} while (control != null);
			return true;
	}
	if (!hooks (type) && !filters (type)) return true;
	short [] x_root = new short [1], y_root = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, x_root, y_root);
	int x = xEvent.x_root - x_root [0], y = xEvent.y_root - y_root [0];
	return sendMouseEvent (type, button, display.clickCount, 0, false, xEvent.time, x, y, xEvent.state);
}
boolean sendMouseEvent (int type, XCrossingEvent xEvent) {
	if (!hooks (type) && !filters (type)) return true;
	short [] x_root = new short [1], y_root = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, x_root, y_root);
	int x = xEvent.x_root - x_root [0], y = xEvent.y_root - y_root [0];
	return sendMouseEvent (type, 0, 0, 0, false, xEvent.time, x, y, xEvent.state);
}
boolean sendMouseEvent (int type, XMotionEvent xEvent) {
	if (!hooks (type) && !filters (type)) return true;
	short [] x_root = new short [1], y_root = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, x_root, y_root);
	int x = xEvent.x_root - x_root [0], y = xEvent.y_root - y_root [0];
	return sendMouseEvent (type, 0, 0, 0, false, xEvent.time, x, y, xEvent.state);
}
void setBackground () {
	if ((state & PARENT_BACKGROUND) != 0 && (state & BACKGROUND) == 0 && backgroundImage == null) {
		setParentBackground ();
	} else {
		if (backgroundImage != null) {
			int [] argList = {OS.XmNbackgroundPixmap, backgroundImage.pixmap};
			OS.XtSetValues (handle, argList, argList.length / 2);
		} else {
			/* Ensure the resource value changes, otherwise XtSetValues() does nothing */
			int pixel = getBackgroundPixel ();
			setBackgroundPixel (pixel + 1);
			setBackgroundPixel (pixel);
		}
	}
	redrawWidget (0, 0, 0, 0, true, false, false);
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
	checkWidget();
	if (color == null) {
		state &= ~BACKGROUND;
		setBackgroundPixel (defaultBackground ());
	} else {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		state |= BACKGROUND;
		setBackgroundPixel (color.handle.pixel);
	}
	if ((state & PARENT_BACKGROUND) != 0 && (state & BACKGROUND) == 0 && backgroundImage == null) {
		setParentBackground ();
		redrawWidget (0, 0, 0, 0, true, false, false);
	}
	redrawChildren ();
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
	if (image != null && image.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (image == backgroundImage) return;
	this.backgroundImage = image;
	int pixmap = image != null ? image.pixmap : OS.XmUNSPECIFIED_PIXMAP;
	int [] argList = {OS.XmNbackgroundPixmap, pixmap};
	OS.XtSetValues (handle, argList, argList.length / 2);
	if ((state & PARENT_BACKGROUND) != 0 && (state & BACKGROUND) == 0 && backgroundImage == null) {
		setParentBackground ();
		redrawWidget (0, 0, 0, 0, true, false, false);
	}
	redrawChildren ();
}
void setBackgroundPixel (int pixel) {
	int [] argList = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmChangeColor (handle, pixel);
	OS.XtSetValues (handle, argList, argList.length / 2);
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int topHandle = topHandle ();
	if (move && resize) {
		int [] argList = {
			OS.XmNx, 0, 			/* 1 */
			OS.XmNy, 0, 			/* 3 */
			OS.XmNwidth, 0, 		/* 5 */
			OS.XmNheight, 0, 		/* 7 */
			OS.XmNborderWidth, 0, 	/* 9 */
		};
		OS.XtGetValues (topHandle, argList, argList.length / 2);
		/*
		* Feature in Motif.  Motif will not allow a window
		* to have a zero width or zero height.  The fix is
		* to ensure these values are never zero.
		*/
		width = Math.max (width - (argList [9] * 2), 1);
		height = Math.max (height - (argList [9] * 2), 1);
		boolean sameOrigin = (x == (short) argList [1]) && (y == (short) argList [3]);
		boolean sameExtent = (width == argList [5]) && (height == argList [7]);
		if (sameOrigin && sameExtent) return false;
		if (redrawWindow != 0) {
			int xDisplay = OS.XtDisplay (handle);
			OS.XResizeWindow (xDisplay, redrawWindow, width, height);
		}
		OS.XtConfigureWidget (topHandle, x, y, width, height, argList [9]);
		updateIM ();
		if (!sameOrigin) {
			Control control = findBackgroundControl ();
			if (control != null && control.backgroundImage != null) {
				if (isVisible ()) redrawWidget (0, 0, 0, 0, true, true, true);
			}
			sendEvent (SWT.Move);
		}
		if (!sameExtent) sendEvent (SWT.Resize);
		return true;
	}
	if (move) {
		int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
		OS.XtGetValues (topHandle, argList, argList.length / 2);
		if (x == (short) argList [1] && y == (short) argList [3]) return false;
		OS.XtMoveWidget (topHandle, x, y);
		Control control = findBackgroundControl ();
		if (control != null && control.backgroundImage != null) {
			if (isVisible ()) redrawWidget (0, 0, 0, 0, true, true, true);
		}
		sendEvent (SWT.Move);
		return true;
	}
	if (resize) {
		int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
		OS.XtGetValues (topHandle, argList, argList.length / 2);
		/*
		* Feature in Motif.  Motif will not allow a window
		* to have a zero width or zero height.  The fix is
		* to ensure these values are never zero.
		*/
		width = Math.max (width - (argList [5] * 2), 1);
		height = Math.max (height - (argList [5] * 2), 1);
		if (width == argList [1] && height == argList [3]) return false;
		if (redrawWindow != 0) {
			int xDisplay = OS.XtDisplay (handle);
			OS.XResizeWindow (xDisplay, redrawWindow, width, height);
		}
		OS.XtResizeWidget (topHandle, width, height, argList [5]);
		updateIM ();
		sendEvent (SWT.Resize);
		return true;
	}
	return false;
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
	checkWidget();
	setBounds (x, y, Math.max (0, width), Math.max (0, height), true, true);
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
	checkWidget();
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	if (capture) {
		int window = OS.XtWindow (handle);
		if (window == 0) return;
		OS.XGrabPointer (
			display,
			window,
			0,
			OS.ButtonPressMask | OS.ButtonReleaseMask | OS.PointerMotionMask,
			OS.GrabModeAsync,
			OS.GrabModeAsync,
			OS.None,
			OS.None,
			OS.CurrentTime);
	} else {
		OS.XUngrabPointer (display, OS.CurrentTime);
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
	checkWidget();
	if (cursor != null && cursor.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	if (!isEnabled ()) {
		if (this != getShell ()) return;
	}
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	if (cursor == null) {
		OS.XUndefineCursor (xDisplay, xWindow);
	} else {
		if (cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		OS.XDefineCursor (xDisplay, xWindow, cursor.handle);
	}
	OS.XFlush (xDisplay);
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
	checkWidget();
	if (enabled == getEnabled ()) return;
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
	if (!enabled || (isEnabled () && enabled)) {
		propagateChildren (enabled);
	}
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
	checkWidget();
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
	checkWidget();
	if (font == null) font = defaultFont ();
	if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.font = font;
	
	/*
	* Feature in Motif.  Setting the font in a widget
	* can cause the widget to automatically resize in
	* the OS.  This behavior is unwanted.  The fix is
	* to force the widget to resize to original size
	* after every font change.
	*/
	int [] argList1 = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);

	/* Set the font list */
	int fontHandle = fontHandle ();
	int [] argList2 = {OS.XmNfontList, font.handle};
	OS.XtSetValues (fontHandle, argList2, argList2.length / 2);
	updateIM ();

	/*
	* Feature in Motif.  When XtSetValues() is used to restore the width and
	* height of the widget, the new width and height are sometimes ignored.
	* The fix is to use XtResizeWidget().
	*/
	OS.XtResizeWidget (handle, argList1 [1], argList1 [3], argList1 [5]);
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
	checkWidget();
	if (color == null) {
		state &= ~FOREGROUND;
		setForegroundPixel (defaultForeground ());
	} else {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		state |= FOREGROUND;
		setForegroundPixel (color.handle.pixel);
	}
}
void setForegroundPixel (int pixel) {
	int [] argList = {OS.XmNforeground, pixel};
	OS.XtSetValues (handle, argList, argList.length / 2);
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
	checkWidget();
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
	checkWidget();
	setBounds (x, y, 0, 0, true, false);
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
	setBounds (location.x, location.y, 0, 0, true, false);
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
	checkWidget();
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
	checkWidget();
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	reskin (SWT.ALL);
	return false;
}
void setParentBackground () {
	setParentBackground (handle);
}
void setParentBackground (int widget) {
	int xDisplay = OS.XtDisplay (widget);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (widget);
	if (xWindow == 0) return;
	OS.XSetWindowBackgroundPixmap (xDisplay, xWindow, OS.ParentRelative);
}
void setParentTraversal () {
	/*
	* When the parent was created with NO_FOCUS, XmNtraversalOn was
	* set to false disallowing focus in the parent and all children.
	* In order to allow the new child to take focus like other platforms,
	* set XmNtraversalOn to true in the parent.
	*/
	if ((parent.style & SWT.NO_FOCUS) != 0) {
		int parentHandle = parent.handle;
		int [] argList = {OS.XmNtraversalOn, 0};
		OS.XtGetValues (parentHandle, argList, argList.length / 2);
		if (argList [1] == 0) {
			argList [1] = 1;
			OS.XtSetValues (parentHandle, argList, argList.length / 2);
			parent.overrideTranslations ();
		}
	}
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
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) {
			if (redrawWindow != 0) {
				int xDisplay = OS.XtDisplay (handle);
				if (xDisplay == 0) return;
				int xWindow = OS.XtWindow (handle);
				if (xWindow == 0) return;
				OS.XDestroyWindow (xDisplay, redrawWindow);
				OS.XSelectInput (xDisplay, xWindow, OS.XtBuildEventMask (handle));
				redrawWindow = 0;
			}
		}
	} else {
		if (drawCount++ == 0) {
			int xDisplay = OS.XtDisplay (handle);
			if (xDisplay == 0) return;
			int xWindow = OS.XtWindow (handle);
			if (xWindow == 0) return;
			Rectangle rect = getBounds();
			XSetWindowAttributes attributes = new XSetWindowAttributes ();
			attributes.background_pixmap = OS.None;
			attributes.event_mask = OS.ExposureMask;
			int mask = OS.CWDontPropagate | OS.CWEventMask | OS.CWBackPixmap;
			redrawWindow = OS.XCreateWindow (xDisplay, xWindow, 0, 0, rect.width, rect.height,
					0,OS.CopyFromParent, OS.CopyFromParent, OS.CopyFromParent, mask, attributes);
			if (redrawWindow != 0) {
				int mouseMask = OS.ButtonPressMask | OS.ButtonReleaseMask |
					OS.LeaveWindowMask | OS.PointerMotionMask |
					OS.PointerMotionMask | OS.PointerMotionHintMask |
					OS.ButtonMotionMask | OS.Button1MotionMask | OS.Button2MotionMask |
					OS.Button3MotionMask | OS.Button4MotionMask | OS.Button5MotionMask; 
				OS.XSelectInput (xDisplay, xWindow, OS.XtBuildEventMask (handle) & ~mouseMask);
				OS.XRaiseWindow (xDisplay, redrawWindow);
				OS.XMapWindow (xDisplay, redrawWindow);
			}
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
	int topHandle = topHandle ();
	Shell shell = getShell ();
	if (!OS.XtIsRealized (topHandle)) shell.realizeWidget ();
	int xDisplay = OS.XtDisplay (topHandle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (topHandle);
	if (xWindow == 0) return;
	if (region != null) {
		OS.XShapeCombineRegion (xDisplay, xWindow, OS.ShapeBounding, 0, 0, region.handle, OS.ShapeSet);
	} else {
		OS.XShapeCombineMask (xDisplay, xWindow, OS.ShapeBounding, 0, 0, 0, OS.ShapeSet);
	}
	this.region = region;
}
boolean setTabGroupFocus (boolean next) {
	return setTabItemFocus (next);
}
boolean setTabItemFocus (boolean next) {
	if (!isShowing ()) return false;
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
	checkWidget();
	setBounds (0, 0, Math.max (0, width), Math.max (0, height), false, true);
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
	checkWidget();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (0, 0, size.x, size.y, false, true);
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
	checkWidget();
	display.setToolTipText (handle, toolTipText = string);
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
	checkWidget();
	int topHandle = topHandle ();
	int [] argList = {OS.XmNmappedWhenManaged, 0};
	OS.XtGetValues (topHandle, argList, argList.length / 2);
	if ((argList [1] != 0) == visible) return;
	Control control = null;
	boolean fixFocus = false;
	if (!visible) {
		if (display.focusEvent != SWT.FocusOut) {
			control = display.getFocusControl ();
			fixFocus = isFocusAncestor (control);
		}
	}
	OS.XtSetMappedWhenManaged (topHandle, visible);	
	if (fixFocus) fixFocus (control);
	/*
	* It is possible (but unlikely) that application code could
	* have disposed the widget in the FocusOut event that is
	* triggered by invoking fixFocus() if the widget being hidden
	* has focus.  If this happens, just return; 
	*/
	if (isDisposed ()) return;
	sendEvent (visible ? SWT.Show : SWT.Hide);
}
void setZOrder (Control control, boolean above) {
	setZOrder (control, above, true);
}
void setZOrder (Control control, boolean above, boolean fixChildren) {
	/*
	* Feature in Xt.  We cannot use XtMakeGeometryRequest() to
	* restack widgets because this call can fail under certain
	* conditions.  For example, XtMakeGeometryRequest() answers
	* XtGeometryNo when attempting to bring a child widget that
	* is larger than the parent widget to the front.  The fix
	* is to use X calls instead.
	*/
	int topHandle1 = topHandle ();
	int display = OS.XtDisplay (topHandle1);
	if (display == 0) return;
	if (!OS.XtIsRealized (topHandle1)) {
		Shell shell = this.getShell ();
		shell.realizeWidget ();
	}
	int window1 = OS.XtWindow (topHandle1);
	if (window1 == 0) return;
	int redrawWindow = fixChildren ? parent.redrawWindow : 0;
	if (control == null && (!above || redrawWindow == 0)) {
		if (above) {
			OS.XRaiseWindow (display, window1);
			if (fixChildren) parent.moveAbove (topHandle1, 0);
		} else {
			OS.XLowerWindow (display, window1);
			if (fixChildren) parent.moveBelow (topHandle1, 0);
		}
		return;
	}
	int window2, topHandle2 = 0;
	if (control != null) {
		topHandle2 = control.topHandle ();
		if (display != OS.XtDisplay (topHandle2)) return;
		if (!OS.XtIsRealized (topHandle2)) {
			Shell shell = control.getShell ();
			shell.realizeWidget ();
		}
		window2 = OS.XtWindow (topHandle2);
	} else {
		window2 = redrawWindow;
	}
	if (window2 == 0) return;
	XWindowChanges struct = new XWindowChanges ();
	struct.sibling = window2;
	struct.stack_mode = above ? OS.Above : OS.Below;
	if (window2 == redrawWindow) struct.stack_mode = OS.Below;
	/*
	* Feature in X. If the receiver is a top level, XConfigureWindow ()
	* will fail (with a BadMatch error) for top level shells because top
	* level shells are reparented by the window manager and do not share
	* the same X window parent.  This is the correct behavior but it is
	* unexpected.  The fix is to use XReconfigureWMWindow () instead.
	* When the receiver is not a top level shell, XReconfigureWMWindow ()
	* behaves the same as XConfigureWindow ().
	*/
	int screen = OS.XDefaultScreen (display);
	int flags = OS.CWStackMode | OS.CWSibling;
	OS.XReconfigureWMWindow (display, window1, screen, flags, struct);
	if (above) {
		if (fixChildren) parent.moveAbove (topHandle1, topHandle2);
	} else {
		if (fixChildren) parent.moveBelow (topHandle1, topHandle2);
	}
}
void showMenu (int x, int y) {
	Event event = new Event ();
	event.x = x;
	event.y = y;
	sendEvent (SWT.MenuDetect, event);
	if (event.doit) {
		if (menu != null && !menu.isDisposed ()) {
			if (event.x != x || event.y != y) {
				menu.setLocation (event.x, event.y);
			}
			menu.setVisible (true);
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
	checkWidget();
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) 0, (short) 0, root_x, root_y);
	return new Point (x - root_x [0], y - root_y [0]);
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
	checkWidget();
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
	checkWidget();
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (handle, (short) x, (short) y, root_x, root_y);
	return new Point (root_x [0], root_y [0]);
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
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	return toDisplay (point.x, point.y);
}
boolean translateAccelerator (char key, int keysym, XKeyEvent xEvent, boolean doit) {
	return menuShell ().translateAccelerator (key, keysym, xEvent, doit);
}
boolean translateMnemonic (Event event, Control control) {
	if (control == this) return false;
	if (!isVisible () || !isEnabled ()) return false;
	event.doit = mnemonicMatch (event.character);
	return traverse (event);
}
boolean translateMnemonic (char key, int keysym, XKeyEvent xEvent) {
	if (key < 0x20) return false;
	if (xEvent.state == 0) {
		int code = traversalCode (key, xEvent);
		if ((code & SWT.TRAVERSE_MNEMONIC) == 0) return false;
	} else {
		int mask = OS.ControlMask | OS.ShiftMask | OS.Mod1Mask;
		if ((xEvent.state & mask) != OS.Mod1Mask) return false;
	}
	Decorations shell = menuShell ();
	if (shell.isVisible () && shell.isEnabled ()) {
		Event event = new Event();
		event.time = xEvent.time;
		event.detail = SWT.TRAVERSE_MNEMONIC;
		event.character = key;
		event.keyCode = keysym;
		if (setInputState (event, xEvent.state)) {
			return translateMnemonic (event, null) || shell.translateMnemonic (event, this);
		}
	}
	return false;
}
boolean translateTraversal (int key, XKeyEvent xEvent) {
	int detail = SWT.TRAVERSE_NONE;
	int code = traversalCode (key, xEvent);
	boolean all = false;
	switch (key) {
		case OS.XK_Escape: {
			all = true;
			detail = SWT.TRAVERSE_ESCAPE;
			break;
		}
		case OS.XK_KP_Enter:
		case OS.XK_Return: {
			all = true;
			detail = SWT.TRAVERSE_RETURN;
			break;
		}
		case OS.XK_Tab: {
			boolean next = (xEvent.state & OS.ShiftMask) == 0;
			detail = next ? SWT.TRAVERSE_TAB_NEXT : SWT.TRAVERSE_TAB_PREVIOUS;
			break;
		}
		case OS.XK_Up:
		case OS.XK_Left: 
		case OS.XK_Down:
		case OS.XK_Right: {
			boolean next = key == OS.XK_Down || key == OS.XK_Right;
			detail = next ? SWT.TRAVERSE_ARROW_NEXT : SWT.TRAVERSE_ARROW_PREVIOUS;
			break;
		}
		case OS.XK_Page_Up:
		case OS.XK_Page_Down: {
			all = true;
			if ((xEvent.state & OS.ControlMask) == 0) return false;
			detail = key == OS.XK_Page_Down ? SWT.TRAVERSE_PAGE_NEXT : SWT.TRAVERSE_PAGE_PREVIOUS;
			break;
		}
		default:
			return false;
	}
	Event event = new Event ();
	event.doit = (code & detail) != 0;
	event.detail = detail;
	event.time = xEvent.time;
	if (!setKeyState (event, xEvent)) return false;
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
int traversalCode (int key, XKeyEvent xEvent) {
	int [] argList = new int [] {OS.XmNtraversalOn, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == 0) return 0;
	int code = SWT.TRAVERSE_RETURN | SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS | SWT.TRAVERSE_PAGE_NEXT | SWT.TRAVERSE_PAGE_PREVIOUS;
	Shell shell = getShell ();
	if (shell.parent != null) code |= SWT.TRAVERSE_ESCAPE;
	if (getNavigationType () == OS.XmNONE) {
		code |= SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
	}
	return code;
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
 * @return true if the traversal succeeded
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
 * @return true if the traversal succeeded
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
		if (control.traverse (event)) return true;
		if (!event.doit && control.hooks (SWT.Traverse)) return false;
		if (control == shell) return false;
		control = control.parent;
	} while (all && control != null);
	return false;
}
boolean traverse (Event event) {
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return true;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:				return true;
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:			return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:		return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:			return traverseMnemonic (event.character);	
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
	checkWidget();
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
		if (!control.isDisposed () && control.setTabGroupFocus (next)) {
			return true;
		}
	}
	if (group.isDisposed ()) return false;
	return group.setTabGroupFocus (next);
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
			if (child.setTabItemFocus (next)) return true;
		}
	}
	return false;
}
boolean traversePage (boolean next) {
	return false;
}
boolean traverseMnemonic (char key) {
	return mnemonicHit (key);
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
	checkWidget();
	update (false);
}
void update (boolean all) {
//	checkWidget();
	if (all) {
		display.update ();		
	} else {
		int display = OS.XtDisplay (handle);
		if (display == 0) return;
		int window = OS.XtWindow (handle);
		if (window == 0) return;
		int event = OS.XtMalloc (XEvent.sizeof);
		OS.XSync (display, false);
		OS.XSync (display, false);
		while (OS.XCheckWindowEvent (display, window, OS.ExposureMask, event)) {
			OS.XtDispatchEvent (event);
		}
		OS.XtFree (event);
	}
}
void updateBackgroundMode () {
	int oldState = state & PARENT_BACKGROUND;
	checkBackground ();
	if (oldState != (state & PARENT_BACKGROUND)) {
		setBackground ();
	}
}
void updateIM () {
	if (!OS.IsDBLocale) return;
	if (!hasFocus ()) return;
	int[] argList2;
	int ptr1 = 0, ptr2 = 0;
	if (hasIMSupport ()) {
		argList2 = new int[]{
			OS.XmNfontList, font.handle,
//			OS.XmNforeground, getForegroundPixel (),
//			OS.XmNbackground, getBackgroundPixel (),
		};
	} else {
		int x = 0, y = 0;
		Font font = this.font;
		Caret caret = getIMCaret ();
		if (caret != null) {
			x += caret.x + (caret.width <= 0 ? 2 : caret.width);
			y += caret.y;
			if (caret.font != null) font = caret.font;
		}
		y += getFontAscent (font.handle);
		short [] point = new short[]{(short) x, (short) y};
		ptr1 = OS.XtMalloc (4);
		OS.memmove (ptr1, point, 4);
		int [] argList1 = {OS.XmNwidth, 0, OS.XmNheight, 0};
		OS.XtGetValues (handle, argList1, argList1.length / 2);
		short [] rect = new short[]{0, 0, (short) argList1 [1], (short) argList1 [3]};
		ptr2 = OS.XtMalloc (8);
		OS.memmove (ptr2, rect, 8);
		/*
		* Feature in Motif.  The XmNarea resource has to be set after
		* the XmNspotLocation.
		*/
		argList2 = new int[]{
			OS.XmNfontList, font.handle,
//			OS.XmNforeground, getForegroundPixel (),
//			OS.XmNbackground, getBackgroundPixel (),
			OS.XmNspotLocation, ptr1,
			OS.XmNarea, ptr2,
		};
	}
	OS.XmImSetValues (handle, argList2, argList2.length / 2);
	int focusHandle = focusHandle ();
	if (handle != focusHandle) {
		OS.XmImSetValues (focusHandle, argList2, argList2.length / 2);
	}
	if (ptr1 != 0) OS.XtFree (ptr1);
	if (ptr2 != 0) OS.XtFree (ptr2);
}
void updateLayout (boolean all) {
	/* Do nothing */
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	Display display = this.display;
	display.hideToolTip ();
	Shell shell = getShell ();
	/*
	* When a shell is created with SWT.ON_TOP and SWT.NO_FOCUS,
	* do not activate the shell when the user clicks on the
	* the client area or on the border or a control within the
	* shell that does not take focus.
	*/
	if (((shell.style & SWT.ON_TOP) != 0) && (((shell.style & SWT.NO_FOCUS) == 0) || ((style & SWT.NO_FOCUS) == 0))) {
		shell.forceActive();
	}
	boolean dispatch = true, dragging = false;
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	int clickTime = display.getDoubleClickTime ();
	int lastTime = display.lastTime, eventTime = xEvent.time;
	int lastButton = display.lastButton, eventButton = xEvent.button;
	if (lastButton == eventButton && lastTime != 0 && Math.abs (lastTime - eventTime) <= clickTime) {
		display.clickCount++;
	} else {
		display.clickCount = 1;
	}
	display.lastTime = eventTime == 0 ? 1 : eventTime;
	display.lastButton = eventButton;
	if (xEvent.button == 2) {
		if ((state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect)) {
			boolean [] consume = new boolean [1];
			if (dragDetect (xEvent.x, xEvent.y, true, consume)) {
				dragging = true;
				dispatch = !consume [0];
			}
			if (isDisposed ()) return 1;
		}
	}
	if (!sendMouseEvent (SWT.MouseDown, xEvent)) dispatch = false;
	if (isDisposed ()) return 1;
	if (display.clickCount == 2) {
		if (!sendMouseEvent (SWT.MouseDoubleClick, xEvent)) dispatch = false;
		if (isDisposed ()) return 1;
		// widget could be disposed at this point
	}
	if (dragging) {
		sendDragEvent (xEvent.button, xEvent.state, xEvent.x, xEvent.y, false);
		if (isDisposed ()) return 1;
	}
	if (xEvent.button == 3) {
		if (menu != null || hooks (SWT.MenuDetect)) {
			if (!isFocusControl ()) setFocus ();
		}
		showMenu (xEvent.x_root, xEvent.y_root);
		if (isDisposed ()) return 1;
	}
	if (!shell.isDisposed ()) shell.setActiveControl (this);
	if (!dispatch) {
		OS.memmove (continue_to_dispatch, new int [1], 4);
		return 1;
	}
	return 0;
}
int XButtonRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	display.hideToolTip ();
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	if (!sendMouseEvent (SWT.MouseUp, xEvent)) {
		OS.memmove (continue_to_dispatch, new int [1], 4);
		return 1;
	}
	return 0;
}
int XEnterWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, call_data, XCrossingEvent.sizeof);
	if (xEvent.mode != OS.NotifyNormal && xEvent.mode != OS.NotifyUngrab) return 0;
	if ((xEvent.state & (OS.Button1Mask | OS.Button2Mask | OS.Button3Mask)) != 0) return 0;
	if (xEvent.subwindow != 0) return 0;
	if (!sendMouseEvent (SWT.MouseEnter, xEvent)) {
		OS.memmove (continue_to_dispatch, new int [1], 4);
		return 1;
	}
	return 0;
}
int XExposure (int w, int client_data, int call_data, int continue_to_dispatch) {
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return 0;
	XExposeEvent xEvent = new XExposeEvent ();
	OS.memmove (xEvent, call_data, XExposeEvent.sizeof);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	int damageRgn = OS.XCreateRegion ();
	OS.XtAddExposureToRegion (call_data, damageRgn);
	Event event = new Event ();
	event.count = xEvent.count;
	event.x = xEvent.x;
	event.y = xEvent.y;
	event.width = xEvent.width;
	event.height = xEvent.height;
	GCData data = new GCData();
	data.damageRgn = damageRgn;
	GC gc = event.gc = GC.motif_new(this, data);
	OS.XSetRegion (xDisplay, gc.handle, damageRgn);
	sendEvent (SWT.Paint, event);
	event.gc = null;
	gc.dispose ();
	OS.XDestroyRegion(damageRgn);
	return 0;
}
int XFocusChange (int w, int client_data, int call_data, int continue_to_dispatch) {

	/* Get the focus change event */
	XFocusChangeEvent xEvent = new XFocusChangeEvent ();
	OS.memmove (xEvent, call_data, XFocusChangeEvent.sizeof);

	/* Ignore focus changes caused by grabbing and ungrabing */
	if (xEvent.mode != OS.NotifyNormal) return 0;

	/* Only process focus callbacks between windows */
	if (xEvent.detail != OS.NotifyAncestor &&
		xEvent.detail != OS.NotifyInferior &&
		xEvent.detail != OS.NotifyNonlinear) return 0;

	/* Process the focus change for the widget */
	Display display = this.display;
	Shell shell = getShell ();
	switch (xEvent.type) {
		case OS.FocusIn:
			xFocusIn (xEvent);
			// widget could be disposed at this point
			
			/*
			* It is possible that the shell may be
			* disposed at this point.  If this happens
			* don't send the activate and deactivate
			* events.
			*/	
			if (!shell.isDisposed ()) {
				shell.setActiveControl (this);
			}
			break;
		case OS.FocusOut:
			xFocusOut (xEvent);
			// widget could be disposed at this point
			
			/*
			* It is possible that the shell may be
			* disposed at this point.  If this happens
			* don't send the activate and deactivate
			* events.
			*/
			if (!shell.isDisposed ()) {
				if (shell != display.getActiveShell ()) {
					shell.setActiveControl (null);
				}
			}
			break;
	}
	return 0;
}
int xFocusIn (XFocusChangeEvent xEvent) {
	/*
	* Bug in Motif.  For some reason, when the widget font is
	* not the default font and the widget loses focus, the
	* X input method segment faults.  A BadFont (invalid font
	* parameter) error is printed.  This problem also happens
	* to XmText and XmTextField.  The fix is to change the
	* X input method font back to the default font when the
	* widget loses focus and restore it when the widget gets
	* focus.
	*/
	updateIM ();
	if (!hasIMSupport ()) {
		int focusHandle = OS.XtWindowToWidget (xEvent.display, xEvent.window);
		OS.XmImSetFocusValues (focusHandle, null, 0);
	} 
	sendFocusEvent (SWT.FocusIn);
	return 0;
}
int xFocusOut (XFocusChangeEvent xEvent) {
	int focusHandle = OS.XtWindowToWidget (xEvent.display, xEvent.window);	
	if (!hasIMSupport ()) OS.XmImUnsetFocus (focusHandle);

	/*
	* Bug in Motif.  For some reason, when the widget font is
	* not the default font and the widget loses focus, the
	* X input method segment faults.  A BadFont (invalid font
	* parameter) error is printed.  This problem also happens
	* to XmText and XmTextField.  The fix is to change the
	* X input method font back to the default font when the
	* widget loses focus and restore it when the widget gets
	* focus.
	* 
	* NOTE: On AIX, changing the IM font when focus is lost because
	* the shell was resized by the user causes the ConfigureNotify
	* event for the shell to be lost.  The event is not in the
	* event queue and therefore not dispatched.  The fix is to avoid
	* the workaround for AIX.
	*/
	if (!OS.IsAIX) {
		int fontList = defaultFont ().handle;
		if (font.handle != fontList) {
			int [] argList2 = {OS.XmNfontList, fontList};
			OS.XmImSetValues (focusHandle, argList2, argList2.length / 2);
		}
	}

	sendFocusEvent (SWT.FocusOut);

	/* Restore XmNtraversalOn if it was focus was forced */
	if (handle == 0) return 0;
	if ((style & SWT.NO_FOCUS) != 0) {
		int [] argList = new int [] {OS.XmNtraversalOn, 0};
		OS.XtGetValues (focusHandle, argList, argList.length / 2);
		if (argList [1] != 0 && (state & FOCUS_FORCED) != 0) {
			argList [1] = 0;
			OS.XtSetValues (focusHandle, argList, argList.length / 2);
		}
	}
	return 0;
}
int XKeyRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, call_data, XKeyEvent.sizeof);
	if (menu != null && xEvent.state == OS.ShiftMask) {
		byte [] buffer = new byte [1];
		int [] keysym = new int [1];	
		OS.XLookupString (xEvent, buffer, buffer.length, keysym, null);
		if (keysym [0] == OS.XK_F10) {
			showMenu (xEvent.x_root, xEvent.y_root);
		}
	}
	return super.XKeyRelease (w, client_data, call_data, continue_to_dispatch);
}
int XLeaveWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	display.removeMouseHoverTimeOut ();
	display.hideToolTip ();
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, call_data, XCrossingEvent.sizeof);
	if (xEvent.mode != OS.NotifyNormal && xEvent.mode != OS.NotifyUngrab) return 0;
	if ((xEvent.state & (OS.Button1Mask | OS.Button2Mask | OS.Button3Mask)) != 0) return 0;
	if (xEvent.subwindow != 0) return 0;
	if (!sendMouseEvent (SWT.MouseExit, xEvent)) {
		OS.memmove (continue_to_dispatch, new int [1], 4);
		return 1;
	}
	return 0;
}
int XmNhelpCallback (int w, int client_data, int call_data) {
	sendHelpEvent (call_data);
	return 0;
}
int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
	display.addMouseHoverTimeOut (handle);
	XMotionEvent xEvent = new XMotionEvent ();
	OS.memmove (xEvent, call_data, XMotionEvent.sizeof);
	if (!sendMouseEvent (SWT.MouseMove, xEvent)) {
		OS.memmove (continue_to_dispatch, new int [1], 4);
		return 1;
	}
	return 0;
}
}

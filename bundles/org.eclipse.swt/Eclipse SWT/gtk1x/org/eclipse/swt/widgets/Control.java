package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Control is the abstract superclass of all windowed user interface classes.
 * <p>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b>
 * <dd>FocusIn, FocusOut, Help, KeyDown, KeyUp, MouseDoubleClick, MouseDown, MouseEnter,
 *     MouseExit, MouseHover, MouseUp, MouseMove, Move, Paint, Resize</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public abstract class Control extends Widget implements Drawable {
	
	Composite parent;
	Menu menu;
	String toolTipText;
	Object layoutData;
	static private final int aux_info_quark = OS.g_quark_from_string (Converter.wcsToMbcs (null, "gtk-aux-info", true));


/*
 *   ===  CONSTRUCTORS  ===
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}

abstract void createHandle(int index);

int eventHandle () {
	return handle;
}

/**
 * Connect the appropriate signal handlers.
 * 
 * At a minimum, we must connect
 * <ul>
 * <li>expose_event
 * <li>button_press_event / button_release_event
 * <li>motion_notify_event
 * <li>enter_notify_event / leave_notify_event
 * <li>key_press_event / key_release_event
 * <li>focus_in_event / focus_out_event
 * </ul>
 * 
 * The possible mask bits are:
 * <ul>
 * GDK_EXPOSURE_MASK         	|
 * GDK_POINTER_MOTION_MASK    	|
 * GDK_POINTER_MOTION_HINT_MASK	|
 * GDK_ENTER_NOTIFY_MASK        |
 * GDK_LEAVE_NOTIFY_MASK     	|
 * GDK_BUTTON_PRESS_MASK
 * GDK_BUTTON_RELEASE_MASK
 * GDK_KEY_PRESS_MASK
 * GDK_KEY_RELEASE_MASK
 * GDK_FOCUS_CHANGE_MASK
 * </ul>
 */
void hookEvents () {
	signal_connect (handle, "expose_event", SWT.Paint, 3);
	int mask =
		OS.GDK_POINTER_MOTION_MASK | 
		OS.GDK_BUTTON_PRESS_MASK | OS.GDK_BUTTON_RELEASE_MASK | 
		OS.GDK_ENTER_NOTIFY_MASK | OS.GDK_LEAVE_NOTIFY_MASK | 
		OS.GDK_KEY_PRESS_MASK | OS.GDK_KEY_RELEASE_MASK |
		OS.GDK_FOCUS_CHANGE_MASK;
	int eventHandle = eventHandle ();
	if (!OS.GTK_WIDGET_NO_WINDOW (eventHandle)) {
		OS.gtk_widget_add_events (eventHandle, mask);
	}
	signal_connect_after (eventHandle, "motion_notify_event", SWT.MouseMove, 3);
	signal_connect_after (eventHandle, "button_press_event", SWT.MouseDown, 3);
	signal_connect_after (eventHandle, "button_release_event", SWT.MouseUp, 3);
	signal_connect_after (eventHandle, "enter_notify_event", SWT.MouseEnter, 3);
	signal_connect_after (eventHandle, "leave_notify_event", SWT.MouseExit, 3);
	signal_connect_after (eventHandle, "key_press_event", SWT.KeyDown, 3);
	signal_connect_after (eventHandle, "key_release_event", SWT.KeyUp, 3);
	signal_connect_after (eventHandle, "focus_in_event", SWT.FocusIn, 3);
	signal_connect_after (eventHandle, "focus_out_event", SWT.FocusOut, 3);
}

abstract void setHandleStyle  ();
void setInitialSize() { UtilFuncs.setZeroSize(topHandle()); }
void configure () {
	// Do NOT directly use gtk_fixed_put in configure(),
	// because not all composites have GtkFixed as their
	// parenting (bottom) handle.
	_connectParent();
}
void _connectParent() {
	parent._connectChild(topHandle());
}
/**
 * Every Control must implement this to map the gtk widgets,
 * and also realize those that have to be realized - this means
 * create the actual X window so that there are no surprizes
 * if the user calls a method expecting the X window to be there.
 * Widgets normally do it by invoking gtk_widget_show() on all
 * handles, and then doing gtk_widget_realize() on bottommost
 * handle, which will realize everything above as well.
 * An exception to this is the Shell, which we do NOT realize
 * at this point.
 */
abstract void showHandle();

int topHandle() {
	return handle;
}

int paintHandle() {
	return handle;
}

/*
 *   ===  GEOMETRY  ===
 */

int computeHandle () {
	return handle;
}

Point _computeSize (int wHint, int hHint, boolean changed) {
	int handle = computeHandle ();
	int aux_info = OS.gtk_object_get_data_by_id (handle, aux_info_quark);
	OS.gtk_object_set_data_by_id (handle, aux_info_quark, 0);
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
	OS.gtk_object_set_data_by_id (handle, aux_info_quark, aux_info);
	int width = wHint == SWT.DEFAULT ? requisition.width : wHint;
	int height = hHint == SWT.DEFAULT ? requisition.height : hHint;
	return new Point (width, height);	
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>prefered size</em> of a control is the size that it would
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
 */
public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>prefered size</em> of a control is the size that it would
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
 */
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	return _computeSize (wHint, hHint, changed);	
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent (or its display if its parent is null).
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
	return _getBounds();
}

/**
 * The actual implementation for getBounds().
 * Concrete controls implement their means to answer the location
 * and size by overriding _getLocation() and _getSize().
 */
final Rectangle _getBounds() {
	Point location = _getLocation();
	Point size = _getSize();
	return new Rectangle(location.x, location.y, size.x, size.y);
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
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Sets the receiver's size and location to the rectangular
 * area specified by the arguments. The <code>x</code> and 
 * <code>y</code> arguments are relative to the receiver's
 * parent (or its display if its parent is null).
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
	boolean differentOrigin = _setLocation(x,y);
	boolean differentExtent = _setSize (width,height);
	if (differentOrigin) sendEvent (SWT.Move);
	if (differentExtent) sendEvent (SWT.Resize);
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null).
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
	return _getLocation();
}

Point _getLocation () {
	return UtilFuncs.getLocation(topHandle());
}

/**
 * Sets the receiver's location to the point specified by
 * the argument which is relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param location the new location for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the receiver's location to the point specified by
 * the arguments which are relative to the receiver's
 * parent (or its display if its parent is null).
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation(int x, int y) {
	checkWidget();
	if (_setLocation(x,y)) sendEvent(SWT.Move);
}

boolean _setLocation(int x, int y) {
	Point old_loc = _getLocation();
	if ( (x != old_loc.x) || (y != old_loc.y) ) {
		UtilFuncs.setLocation(parent.parentingHandle(), topHandle(), x,y);
		return true;
	} else return false;
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
	return _getSize();
}
Point _getSize() {
	return UtilFuncs.getSize(topHandle());
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
 * @param height the new height for the receiver
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
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
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
	// Even though GTK+ will not let any widget be smaller
	// than 3@3, we don't care about it here, as this kind
	// of platform weirdness is handled in UtilFuncs.
	width  = Math.max(width,  0);
	height = Math.max(height, 0);
	if (_setSize(width, height)) sendEvent(SWT.Resize);
}
boolean _setSize(int width, int height) {
	Point old_size = _getSize();
	if ( (width==old_size.x) && (height==old_size.y) ) return false;
	return UtilFuncs.setSize(topHandle(), width, height);
}

/**
 * Moves the receiver above the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the top of the drawing order. The control at
 * the top of the drawing order will not be covered by other
 * controls even if they occupy intersecting areas.
 *
 * @param the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void moveAbove (Control control) {
	checkWidget();
	GtkWidget widget = new GtkWidget();
	OS.memmove (widget, topHandle(), GtkWidget.sizeof);
	int topGdkWindow = widget.window;
	if (topGdkWindow!=0) OS.gdk_window_raise (topGdkWindow);
}

/**
 * Moves the receiver below the specified control in the
 * drawing order. If the argument is null, then the receiver
 * is moved to the bottom of the drawing order. The control at
 * the bottom of the drawing order will be covered by all other
 * controls which occupy intersecting areas.
 *
 * @param the sibling control (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void moveBelow (Control control) {
	checkWidget();
	GtkWidget widget = new GtkWidget();
	OS.memmove (widget, topHandle(), GtkWidget.sizeof);
	int topGdkWindow = widget.window;
	if (topGdkWindow!=0) OS.gdk_window_lower (topGdkWindow);
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
 * @see #computeSize
 */
public void pack () {
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
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #computeSize
 */
public void pack (boolean changed) {
	checkWidget();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
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
 * Returns a point which is the result of converting the
 * argument, which is specified in display relative coordinates,
 * to coordinates relative to the receiver.
 * <p>
 * @param point the point to be translated (must not be null)
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
	int[] x = new int[1], y = new int[1];
	OS.gdk_window_get_origin(_gdkWindow(), x,y);
	int ctlX = point.x - x[0];
	int ctlY = point.y - y[0];
	return new Point (ctlX, ctlY);
}
/**
 * Returns a point which is the result of converting the
 * argument, which is specified in coordinates relative to
 * the receiver, to display relative coordinates.
 * <p>
 * @param point the point to be translated (must not be null)
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
	int[] x = new int[1], y = new int[1];
	OS.gdk_window_get_origin(_gdkWindow(), x,y);
	return new Point (x[0]+point.x, y[0]+point.y);
}
//   ===  End of GEOMETRY Category  ===


/*
 *   ==  ADD/REMOVE LISTENERS  ==
 */
 
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
 * be notified when the help events are generated for the control, by sending
 * it one of the messages defined in the <code>HelpListener</code>
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

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control gains or loses focus.
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
 * @see #addFocusListener
 */
public void removeFocusListener(FocusListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.FocusIn, listener);
	eventTable.unhook (SWT.FocusOut, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the help events are generated for the control.
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
 * @see #addHelpListener
 */
public void removeHelpListener (HelpListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when keys are pressed and released on the system keyboard.
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
 * @see KeyListener
 * @see #addKeyListener
 */
public void removeKeyListener(KeyListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.KeyUp, listener);
	eventTable.unhook (SWT.KeyDown, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when mouse buttons are pressed and released.
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
 * @see #addMouseListener
 */
public void removeMouseListener (MouseListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
 * @see #addMouseMoveListener
 */
public void removeMouseMoveListener(MouseMoveListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseMove, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the mouse passes or hovers over controls.
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
 * @see #addMouseTrackListener
 */
public void removeMouseTrackListener(MouseTrackListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver needs to be painted.
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
 * @see #addPaintListener
 */
public void removePaintListener(PaintListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when traversal events occur.
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
 * @see #addTraverseListener
 */
public void removeTraverseListener(TraverseListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}


/*
 * Return (GTKWIDGET)h->window.
 */
final int _gdkWindow(int h) {
	/* Temporary code.
	 * This check is not necessary as the (internal) callers
	 * always make sure h!=0.
	 */
	if (h==0) error(SWT.ERROR_CANNOT_BE_ZERO);
	
	GtkWidget widget = new GtkWidget();
	OS.memmove (widget, h, GtkWidget.sizeof);
	return widget.window;
}

int _gdkWindow() {
	int windowHandle = _gdkWindow(handle);
	if (windowHandle==0) error(SWT.ERROR_NO_HANDLES);
	return windowHandle;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.gtk_widget_grab_focus (handle);
	return true;
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
	checkWidget();
	return Color.gtk_new (_getBackgroundGdkColor());
}

/*
 *  Subclasses should override this to pass a meaningful handle
 */
GdkColor _getBackgroundGdkColor() {
	/* Override this */
	int h = paintHandle();
	
	int hStyle = OS.gtk_widget_get_style (handle);
	GtkStyle style = new GtkStyle ();
	OS.memmove (style, hStyle, GtkStyle.sizeof);
	GdkColor color = new GdkColor ();
	color.pixel = style.bg0_pixel;
	color.red = style.bg0_red;
	color.green = style.bg0_green;
	color.blue = style.bg0_blue;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return (style & SWT.BORDER) == 0 ? 0 : 1;
}

/**
 * Returns the display that the receiver was created on.
 *
 * @return the receiver's display
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Display getDisplay () {
	if (parent == null) {
		error (SWT.ERROR_WIDGET_DISPOSED);
	}
	return parent.getDisplay ();
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
 */
public boolean getEnabled () {
	checkWidget ();
	int topHandle = topHandle ();
	return OS.GTK_WIDGET_SENSITIVE (topHandle);
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
	return Font.gtk_new(_getFontHandle());
}
/*
 * Subclasses should override this, passing a meaningful handle
 */
int _getFontHandle () {
	return UtilFuncs.getFont(handle);
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
	checkWidget();
	return Color.gtk_new (_getForegroundGdkColor());
}

/*
 *  Subclasses should override this to pass a meaningful handle
 */
GdkColor _getForegroundGdkColor() {
	/* Override this */
	int h = paintHandle();
	
	int hStyle = OS.gtk_widget_get_style (handle);
	GtkStyle style = new GtkStyle ();
	OS.memmove (style, hStyle, GtkStyle.sizeof);
	GdkColor color = new GdkColor ();
	color.pixel = style.fg0_pixel;
	color.red = style.fg0_red;
	color.green = style.fg0_green;
	color.blue = style.fg0_blue;
	return color;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return layoutData;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return menu;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
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
public Shell getShell() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return _getShell();
}
Shell _getShell() {
	return parent._getShell();
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	return _getVisible(); 
}
boolean _getVisible() {
	return _getVisible(topHandle());
}
boolean _getVisible(int h) {
	return (OS.GTK_WIDGET_FLAGS(h) & OS.GTK_VISIBLE) != 0;
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
 * @private
 */
public int internal_new_GC (GCData data) {
	if (data == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (paintHandle() == 0) error(SWT.ERROR_UNSPECIFIED);
	
	// Create the GC with default values for this control
	GtkWidget w = new GtkWidget();
	OS.memmove (w, paintHandle(), GtkWidget.sizeof);

	if (w.window == 0) error(SWT.ERROR_UNSPECIFIED);
	int gc = OS.gdk_gc_new(w.window);
	
	OS.gdk_gc_set_font(gc, _getFontHandle());
	OS.gdk_gc_set_background(gc, _getBackgroundGdkColor());
	OS.gdk_gc_set_foreground(gc, _getForegroundGdkColor());
	
	data.drawable = w.window;
	return gc;
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
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data 
 *
 * @private
 */
public void internal_dispose_GC (int xGC, GCData data) {
	if(xGC == 0) error(SWT.ERROR_NO_HANDLES);
	OS.gdk_gc_unref(xGC);
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
 */
public boolean isEnabled () {
	checkWidget ();
	int topHandle = topHandle ();
	return OS.GTK_WIDGET_IS_SENSITIVE (topHandle);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return (OS.GTK_WIDGET_FLAGS(handle)&OS.GTK_HAS_FOCUS)!=0;
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
public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	boolean result = getVisible ();
	if (parent != null) 
		result = result && parent.isVisible();
	return result;
}
Decorations menuShell () {
	return parent.menuShell ();
}

int processKeyDown (int callData, int arg1, int int2) {
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, callData, GdkEventKey.sizeof);
	boolean accelResult = OS.gtk_accel_groups_activate(_getShell().topHandle, gdkEvent.keyval, gdkEvent.state);
	if (!accelResult) sendKeyEvent (SWT.KeyDown, gdkEvent);
	return 1;
}

int processKeyUp (int callData, int arg1, int int2) {
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, callData, GdkEventKey.sizeof);
	sendKeyEvent (SWT.KeyUp, gdkEvent);
	return 1;
}

int processMouseDown (int callData, int arg1, int int2) {
	OS.gtk_widget_grab_focus(handle);
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, callData, GdkEventButton.sizeof);
	int eventType = SWT.MouseDown;
	if (gdkEvent.type == OS.GDK_2BUTTON_PRESS) eventType = SWT.MouseDoubleClick;
	sendMouseEvent (eventType, gdkEvent.button, gdkEvent.state, gdkEvent.time, (int)gdkEvent.x, (int)gdkEvent.y);
	if (gdkEvent.button == 3 && menu != null) {
		menu.setVisible (true);
	}
	return 1;
}

int processMouseEnter (int arg0, int arg1, int int2) {
	//NOT IMPLEMENTED - event state
	sendEvent (SWT.MouseEnter);
	return 1;
}
int processMouseExit (int arg0, int arg1, int int2) {
	//NOT IMPLEMENTED - event state
	sendEvent (SWT.MouseExit);
	return 1;
}

int processMouseUp (int callData, int arg1, int int2) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, callData, GdkEventButton.sizeof); 
	sendMouseEvent (SWT.MouseUp, gdkEvent.button, gdkEvent.state, gdkEvent.time, (int)gdkEvent.x, (int)gdkEvent.y);
	return 1;
}

int processMouseMove (int callData, int arg1, int int2) {
	GdkEventMotion gdkEvent = new GdkEventMotion ();
	OS.memmove (gdkEvent, callData, GdkEventMotion.sizeof); 
	Point where = _gdkWindowGetPointer();
	sendMouseEvent (SWT.MouseMove, 0, gdkEvent.state, gdkEvent.time, where.x, where.y);
	return 1;
}
Point _gdkWindowGetPointer() {
	int[] px = new int[1], py = new int[1];
	OS.gdk_window_get_pointer(_gdkWindow(), px, py, 0);
	return new Point(px[0], py[0]);
}
int processFocusIn(int int0, int int1, int int2) {
	postEvent(SWT.FocusIn);
	return 0;
}
int processFocusOut(int int0, int int1, int int2) {
	postEvent(SWT.FocusOut);
	return 0;
}

int processPaint (int callData, int int2, int int3) {
	if (!hooks (SWT.Paint)) return 1;
	
	GdkEventExpose gdkEvent = new GdkEventExpose ();
	OS.memmove (gdkEvent, callData, GdkEventExpose.sizeof);
	Event event = new Event ();
	event.count = gdkEvent.count;
	event.x = gdkEvent.x;  event.y = gdkEvent.y;
	event.width = gdkEvent.width;  event.height = gdkEvent.height;
	GC gc = event.gc = new GC (this);
	GdkRectangle rect = new GdkRectangle ();
	rect.x = gdkEvent.x;  rect.y = gdkEvent.y;
	rect.width = gdkEvent.width;  rect.height = gdkEvent.height;
	OS.gdk_gc_set_clip_rectangle (gc.handle, rect);
	gc.fillRectangle(rect.x, rect.y, rect.width, rect.height);
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	return 1;
}

/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #update
 */
public void redraw () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Point size = _getSize();
	_redraw(0, 0, size.x, size.y, true);
//OS.gtk_widget_queue_draw(handle);
}
/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn. 
 * The next time a paint request is processed, that area of
 * the receiver will be painted. If the <code>all</code> flag
 * is <code>true</code>, any children of the receiver which
 * intersect with the specified area will also paint their
 * intersecting areas. If the <code>all</code> flag is 
 * <code>false</code>, the children will not be painted.
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
 * @see #update
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	_redraw(x, y, width, height, all);
}
protected void _redraw(int x, int y, int width, int height, boolean all) {
	OS.gdk_window_clear_area_e (_gdkWindow(), x, y, width, height);			

GdkRectangle rect = new GdkRectangle();
rect.x = (short)x;
rect.y = (short)y;
rect.width = (short)width;
rect.height =(short) height;
//OS.gtk_widget_draw(handle, rect);
OS.gtk_widget_queue_draw(handle);

//	OS.gtk_widget_queue_draw_area (handle, x, y, width, height);
}

void releaseWidget () {
	super.releaseWidget ();
	toolTipText = null;
	parent = null;
	menu = null;
	layoutData = null;
}
void sendKeyEvent (int type, GdkEventKey gdkEvent) {
	/* Look up the keysym and character(s) */
	int size = gdkEvent.length;
	if (gdkEvent.keyval == 0 && size == 0) return;

	/* If there is no composed string input by keypress, only send the keyvalue */ 
	if (size == 0 ) {
		Event event = new Event ();
		event.time = gdkEvent.time;
//		event.character = (char) 0;  //no character sent
		event.keyCode = Display.translateKey (gdkEvent.keyval);
		event.character = (char) event.keyCode;  //no character sent
		if ((gdkEvent.state & OS.GDK_MOD1_MASK) != 0) event.stateMask |= SWT.ALT;
		if ((gdkEvent.state & OS.GDK_SHIFT_MASK) != 0) event.stateMask |= SWT.SHIFT;
		if ((gdkEvent.state & OS.GDK_CONTROL_MASK) != 0) event.stateMask |= SWT.CONTROL;
		if ((gdkEvent.state & OS.GDK_BUTTON1_MASK) != 0) event.stateMask |= SWT.BUTTON1;
		if ((gdkEvent.state & OS.GDK_BUTTON2_MASK) != 0) event.stateMask |= SWT.BUTTON2;
		if ((gdkEvent.state & OS.GDK_BUTTON3_MASK) != 0) event.stateMask |= SWT.BUTTON3;
		postEvent (type, event);
	}
	else {	
		byte [] buffer = new byte [size];
		OS.memmove (buffer, gdkEvent.string, size);
		/* Convert from MBCS to UNICODE and send the event */
		char [] result = Converter.mbcsToWcs (null, buffer);
		for (int i=0; i<result.length; i++) {
			Event event = new Event ();
			event.time = gdkEvent.time;
			event.character = result [i];
			event.keyCode = result [i]; //0; //no keyCode sent
			if ((gdkEvent.state & OS.GDK_MOD1_MASK) != 0) event.stateMask |= SWT.ALT;
			if ((gdkEvent.state & OS.GDK_SHIFT_MASK) != 0) event.stateMask |= SWT.SHIFT;
			if ((gdkEvent.state & OS.GDK_CONTROL_MASK) != 0) event.stateMask |= SWT.CONTROL;
			if ((gdkEvent.state & OS.GDK_BUTTON1_MASK) != 0) event.stateMask |= SWT.BUTTON1;
			if ((gdkEvent.state & OS.GDK_BUTTON2_MASK) != 0) event.stateMask |= SWT.BUTTON2;
			if ((gdkEvent.state & OS.GDK_BUTTON3_MASK) != 0) event.stateMask |= SWT.BUTTON3;
			postEvent (type, event);
		}
	}	
}
void sendMouseEvent (int type, int button, int mask, int time, int x, int y) {
	Event event = new Event ();
	event.time = time;
	event.button = button;
	event.x = x;  event.y = y;
	if ((mask & OS.GDK_MOD1_MASK) != 0) event.stateMask |= SWT.ALT;
	if ((mask & OS.GDK_SHIFT_MASK) != 0) event.stateMask |= SWT.SHIFT;
	if ((mask & OS.GDK_CONTROL_MASK) != 0) event.stateMask |= SWT.CONTROL;
	if ((mask & OS.GDK_BUTTON1_MASK) != 0) event.stateMask |= SWT.BUTTON1;
	if ((mask & OS.GDK_BUTTON2_MASK) != 0) event.stateMask |= SWT.BUTTON2;
	if ((mask & OS.GDK_BUTTON3_MASK) != 0) event.stateMask |= SWT.BUTTON3;
	postEvent (type, event);
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int hDefaultStyle = OS.gtk_widget_get_default_style ();
	int hStyle = OS.gtk_widget_get_style (handle);
	boolean makeCopy = hStyle == hDefaultStyle;
	hStyle = OS.gtk_style_copy (makeCopy ? hDefaultStyle : hStyle);	
	GtkStyle style = new GtkStyle ();
	OS.memmove (style, hStyle, GtkStyle.sizeof);
	if (color == null) {
		GtkStyle defaultStyle = new GtkStyle ();
		OS.memmove (defaultStyle, hDefaultStyle, GtkStyle.sizeof);
		style.bg0_pixel = defaultStyle.bg0_pixel;
		style.bg0_red = defaultStyle.bg0_red;
		style.bg0_green = defaultStyle.bg0_green;
		style.bg0_blue = defaultStyle.bg0_blue;
		style.bg1_pixel = defaultStyle.bg1_pixel;
		style.bg1_red = defaultStyle.bg1_red;
		style.bg1_green = defaultStyle.bg1_green;
		style.bg1_blue = defaultStyle.bg1_blue;
		style.bg2_pixel = defaultStyle.bg2_pixel;
		style.bg2_red = defaultStyle.bg2_red;
		style.bg2_green = defaultStyle.bg2_green;
		style.bg2_blue = defaultStyle.bg2_blue;
		style.bg3_pixel = defaultStyle.bg3_pixel;
		style.bg3_red = defaultStyle.bg3_red;
		style.bg3_green = defaultStyle.bg3_green;
		style.bg3_blue = defaultStyle.bg3_blue;
		style.bg4_pixel = defaultStyle.bg4_pixel;
		style.bg4_red = defaultStyle.bg4_red;
		style.bg4_green = defaultStyle.bg4_green;
		style.bg4_blue = defaultStyle.bg4_blue;
	} else {
		style.bg0_pixel = color.handle.pixel;
		style.bg0_red = color.handle.red;
		style.bg0_green = color.handle.green;
		style.bg0_blue = color.handle.blue;
		style.bg1_pixel = color.handle.pixel;
		style.bg1_red = color.handle.red;
		style.bg1_green = color.handle.green;
		style.bg1_blue = color.handle.blue;
		style.bg2_pixel = color.handle.pixel;
		style.bg2_red = color.handle.red;
		style.bg2_green = color.handle.green;
		style.bg2_blue = color.handle.blue;
		style.bg3_pixel = color.handle.pixel;
		style.bg3_red = color.handle.red;
		style.bg3_green = color.handle.green;
		style.bg3_blue = color.handle.blue;
		style.bg4_pixel = color.handle.pixel;
		style.bg4_red = color.handle.red;
		style.bg4_green = color.handle.green;
		style.bg4_blue = color.handle.blue;
	}
	OS.memmove (hStyle, style, GtkStyle.sizeof);
	OS.gtk_widget_set_style (handle, hStyle);
	if (makeCopy) {
		OS.gtk_style_unref (hStyle);
	}
}

/**
 * If the argument is <code>true</code>, causes the receiver to have
 * all mouse events delivered to it until the method is called with
 * <code>false</code> as the argument.
 *
 * @param capture <code>true</code> to capture the mouse, and <code>false</code> to release it
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCapture (boolean capture) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	if (capture) {
		OS.gtk_widget_grab_focus (handle);
	} else {
		OS.gtk_widget_grab_default (handle);
	}
	*/
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
	GtkWidget widget = new GtkWidget ();
	OS.memmove (widget, paintHandle(), GtkWidget.sizeof);
	int window = widget.window;
	if (window == 0) return;
	int hCursor = 0;
	if (cursor != null) hCursor = cursor.handle;
	OS.gdk_window_set_cursor (window, hCursor);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int topHandle = topHandle ();
	OS.gtk_widget_set_sensitive (topHandle, enabled);
	/*
	* This code is intentionally commented
	*/
//	OS.gtk_widget_set_state (topHandle, enabled ? OS.GTK_STATE_NORMAL : OS.GTK_STATE_INSENSITIVE);
//	if (enabled) {
//		OS.GTK_WIDGET_SET_FLAGS (handle, OS.GTK_SENSITIVE);
//	} else {
//		OS.GTK_WIDGET_UNSET_FLAGS (handle, OS.GTK_SENSITIVE);
//	}
}
/**
 * Causes the receiver to have the <em>keyboard focus</em>, 
 * such that all keyboard events will be delivered to it.
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	
	/* The non-null font case */
	if (font != null) {
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		int fontHandle = OS.gdk_font_ref(font.handle);
		_setFontHandle(fontHandle);
		return;
	}
	
	/* The font argument is null, revert to default font */
	GtkStyle style = new GtkStyle();
	OS.memmove (style, OS.gtk_widget_get_default_style(), GtkStyle.sizeof);
	int fontHandle = OS.gdk_font_ref(style.font);
	if (fontHandle==0) error(SWT.ERROR_NO_HANDLES);
	_setFontHandle(fontHandle);
}

/**
 * Actually set the receiver's font in the OS.
 * Concrete subclasses may override this method to operate
 * on a different handle.
 */
void _setFontHandle (int f) {
	UtilFuncs.setFont(handle, f);
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int hStyle = OS.gtk_widget_get_style (handle);
	hStyle = OS.gtk_style_copy (hStyle);	
	GtkStyle style = new GtkStyle ();
	OS.memmove (style, hStyle, GtkStyle.sizeof);
	if (color == null) {
		int hDefaultStyle = OS.gtk_widget_get_default_style ();
		GtkStyle defaultStyle = new GtkStyle ();
		OS.memmove (defaultStyle, hDefaultStyle, GtkStyle.sizeof);
		style.fg0_pixel = defaultStyle.fg0_pixel;
		style.fg0_red = defaultStyle.fg0_red;
		style.fg0_green = defaultStyle.fg0_green;
		style.fg0_blue = defaultStyle.fg0_blue;
		style.fg1_pixel = defaultStyle.fg1_pixel;
		style.fg1_red = defaultStyle.fg1_red;
		style.fg1_green = defaultStyle.fg1_green;
		style.fg1_blue = defaultStyle.fg1_blue;
		style.fg2_pixel = defaultStyle.fg2_pixel;
		style.fg2_red = defaultStyle.fg2_red;
		style.fg2_green = defaultStyle.fg2_green;
		style.fg2_blue = defaultStyle.fg2_blue;
		style.fg3_pixel = defaultStyle.fg3_pixel;
		style.fg3_red = defaultStyle.fg3_red;
		style.fg3_green = defaultStyle.fg3_green;
		style.fg3_blue = defaultStyle.fg3_blue;
		style.fg4_pixel = defaultStyle.fg4_pixel;
		style.fg4_red = defaultStyle.fg4_red;
		style.fg4_green = defaultStyle.fg4_green;
		style.fg4_blue = defaultStyle.fg4_blue;
	} else {
		style.fg0_pixel = color.handle.pixel;
		style.fg0_red = color.handle.red;
		style.fg0_green = color.handle.green;
		style.fg0_blue = color.handle.blue;
		style.fg1_pixel = color.handle.pixel;
		style.fg1_red = color.handle.red;
		style.fg1_green = color.handle.green;
		style.fg1_blue = color.handle.blue;
		style.fg2_pixel = color.handle.pixel;
		style.fg2_red = color.handle.red;
		style.fg2_green = color.handle.green;
		style.fg2_blue = color.handle.blue;
		style.fg3_pixel = color.handle.pixel;
		style.fg3_red = color.handle.red;
		style.fg3_green = color.handle.green;
		style.fg3_blue = color.handle.blue;
		style.fg4_pixel = color.handle.pixel;
		style.fg4_red = color.handle.red;
		style.fg4_green = color.handle.green;
		style.fg4_blue = color.handle.blue;
	}
	OS.memmove (hStyle, style, GtkStyle.sizeof);
	OS.gtk_widget_set_style (handle, hStyle);
}

/**
 * Sets the receiver's pop up menu to the argument.
 * All controls may optionally have a pop up
 * menu that is displayed when the user requests one for
 * the control. The sequence of key strokes, button presses
 * and/or button releases that are used to request a pop up
 * menu is platform specific.
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (menu != null) {
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
 * Answers <code>true</code> if the parent is successfully changed.
 *
 * @param parent the new parent for the control.
 * @return <code>true</code> if the parent is changed and <code>false</code> otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 */
public boolean setParent (Composite parent) {
	checkWidget();
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

/**
 * If the argument is <code>false</code>, causes subsequent drawing
 * operations in the receiver to be ignored. No drawing of any kind
 * can occur in the receiver until the flag is set to true.
 * Graphics operations that occurred while the flag was
 * <code>false</code> are lost. When the flag is set to <code>true</code>,
 * the entire widget is marked as needing to be redrawn.
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
 * @see #redraw
 * @see #update
 */
public void setRedraw (boolean redraw) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}
/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	toolTipText = string;
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
	int topHandle = topHandle();
	if (visible) {
		sendEvent (SWT.Show);
		OS.gtk_widget_show (topHandle);
	} else {	
		OS.gtk_widget_hide (topHandle);
		sendEvent (SWT.Hide);
	}
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
	if (!isFocusControl () && !setFocus ()) return false;
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

boolean traverse (Event event) {
	/*
	 * It is possible (but unlikely), that application
	 * code could have disposed the widget in the traverse
	 * event.  If this happens, return true to stop further
	 * event processing.
	 */	
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return false;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:			return true;
		/*
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:	return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:		return traverseMnemonic (event.character);	
		case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
		*/
	}
	error(SWT.ERROR_NOT_IMPLEMENTED);
	return false;
}


/**
 * Forces all outstanding paint requests for the widget tree
 * to be processed before this method returns.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #redraw
 */
public void update () {
	checkWidget ();
	//NOT DONE - should only dispatch paint events
	OS.gdk_flush ();
	while ((OS.gtk_events_pending()) != 0) {
		OS.gtk_main_iteration ();
	}	
}
}

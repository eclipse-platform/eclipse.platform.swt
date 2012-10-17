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


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.Cairo;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.events.*;

/**
 * This class is the abstract superclass of all user interface objects.  
 * Widgets are created, disposed and issue notification to listeners
 * when events occur which affect them.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation. However, it has not been marked
 * final to allow those outside of the SWT development team to implement
 * patched versions of the class in order to get around specific
 * limitations in advance of when those limitations can be addressed
 * by the team.  Any class built using subclassing to access the internals
 * of this class will likely fail to compile or run between releases and
 * may be strongly platform specific. Subclassing should not be attempted
 * without an intimate and detailed understanding of the workings of the
 * hierarchy. No support is provided for user-written classes which are
 * implemented as subclasses of this class.
 * </p>
 *
 * @see #checkSubclass
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class Widget {
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
	public long /*int*/ handle;
	int style, state;
	Display display;
	EventTable eventTable;
	Object data;
	
	/* Global state flags */
	static final int DISPOSED = 1<<0;
	static final int CANVAS = 1<<1;
	static final int KEYED_DATA = 1<<2;
	static final int HANDLE = 1<<3;
	static final int DISABLED = 1<<4;
	static final int MENU = 1<<5;
	static final int OBSCURED = 1<<6;
	static final int MOVED = 1<<7;
	static final int RESIZED = 1<<8;
	static final int ZERO_WIDTH = 1<<9;
	static final int ZERO_HEIGHT = 1<<10;
	static final int HIDDEN = 1<<11;
	static final int FOREGROUND = 1<<12;
	static final int BACKGROUND = 1<<13;
	static final int FONT = 1<<14;
	static final int PARENT_BACKGROUND = 1<<15;
	static final int THEME_BACKGROUND = 1<<16;
	
	/* A layout was requested on this widget */
	static final int LAYOUT_NEEDED	= 1<<17;
	
	/* The preferred size of a child has changed */
	static final int LAYOUT_CHANGED = 1<<18;
	
	/* A layout was requested in this widget hierachy */
	static final int LAYOUT_CHILD = 1<<19;

	/* More global state flags */
	static final int RELEASED = 1<<20;
	static final int DISPOSE_SENT = 1<<21;
	static final int FOREIGN_HANDLE = 1<<22;
	static final int DRAG_DETECT = 1<<23;
	
	/* Notify of the opportunity to skin this widget */
	static final int SKIN_NEEDED = 1<<24;

	/* Should sub-windows be checked when EnterNotify received */
	static final int CHECK_SUBWINDOW = 1<<25;

	/* Default size for widgets */
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	
	/* GTK signals data */
	static final int ACTIVATE = 1;
	static final int BUTTON_PRESS_EVENT = 2;
	static final int BUTTON_PRESS_EVENT_INVERSE = 3;
	static final int BUTTON_RELEASE_EVENT = 4;
	static final int BUTTON_RELEASE_EVENT_INVERSE = 5;
	static final int CHANGED = 6;
	static final int CHANGE_VALUE = 7;
	static final int CLICKED = 8;
	static final int COMMIT = 9;
	static final int CONFIGURE_EVENT = 10;
	static final int DELETE_EVENT = 11;
	static final int DELETE_RANGE = 12;
	static final int DELETE_TEXT = 13;
	static final int ENTER_NOTIFY_EVENT = 14;
	static final int EVENT = 15;
	static final int EVENT_AFTER = 16;
	static final int EXPAND_COLLAPSE_CURSOR_ROW = 17;
	static final int EXPOSE_EVENT = 18;
	static final int EXPOSE_EVENT_INVERSE = 19;
	static final int FOCUS = 20;
	static final int FOCUS_IN_EVENT = 21;
	static final int FOCUS_OUT_EVENT = 22;
	static final int GRAB_FOCUS = 23;
	static final int HIDE = 24;
	static final int INPUT = 25;
	static final int INSERT_TEXT = 26;
	static final int KEY_PRESS_EVENT = 27;
	static final int KEY_RELEASE_EVENT = 28;
	static final int LEAVE_NOTIFY_EVENT = 29;
	static final int MAP = 30;
	static final int MAP_EVENT = 31;
	static final int MNEMONIC_ACTIVATE = 32;
	static final int MOTION_NOTIFY_EVENT = 33;
	static final int MOTION_NOTIFY_EVENT_INVERSE = 34;
	static final int MOVE_FOCUS = 35;
	static final int OUTPUT = 36;
	static final int POPULATE_POPUP = 37;
	static final int POPUP_MENU = 38;
	static final int PREEDIT_CHANGED = 39;
	static final int REALIZE = 40;
	static final int ROW_ACTIVATED = 41;
	static final int SCROLL_CHILD = 42;
	static final int SCROLL_EVENT = 43;
	static final int SELECT = 44;
	static final int SHOW = 45;
	static final int SHOW_HELP = 46;
	static final int SIZE_ALLOCATE = 47;
	static final int STYLE_SET = 48;
	static final int SWITCH_PAGE = 49;
	static final int TEST_COLLAPSE_ROW = 50;
	static final int TEST_EXPAND_ROW = 51;
	static final int TEXT_BUFFER_INSERT_TEXT = 52;
	static final int TOGGLED = 53;
	static final int UNMAP = 54;
	static final int UNMAP_EVENT = 55;
	static final int UNREALIZE = 56;
	static final int VALUE_CHANGED = 57;
	static final int VISIBILITY_NOTIFY_EVENT = 58;
	static final int WINDOW_STATE_EVENT = 59;
	static final int ACTIVATE_INVERSE = 60;
	static final int DAY_SELECTED = 61;
	static final int MONTH_CHANGED = 62;
	static final int STATUS_ICON_POPUP_MENU = 63;
	static final int ROW_INSERTED = 64;
	static final int ROW_DELETED = 65;
	static final int DAY_SELECTED_DOUBLE_CLICK = 66;
	static final int ICON_RELEASE = 67;
	static final int SELECTION_DONE = 68;
	static final int START_INTERACTIVE_SEARCH = 69;
	static final int BACKSPACE = 70;
	static final int BACKSPACE_INVERSE = 71;
	static final int COPY_CLIPBOARD = 72;
	static final int COPY_CLIPBOARD_INVERSE = 73;
	static final int CUT_CLIPBOARD = 74;
	static final int CUT_CLIPBOARD_INVERSE = 75;
	static final int PASTE_CLIPBOARD = 76;
	static final int PASTE_CLIPBOARD_INVERSE = 77;
	static final int DELETE_FROM_CURSOR = 78;
	static final int DELETE_FROM_CURSOR_INVERSE = 79;
	static final int MOVE_CURSOR = 80;
	static final int MOVE_CURSOR_INVERSE = 81;
	static final int DIRECTION_CHANGED = 82;
	static final int CREATE_MENU_PROXY = 83;
	static final int LAST_SIGNAL = 84;
	
	static final String IS_ACTIVE = "org.eclipse.swt.internal.control.isactive"; //$NON-NLS-1$
	static final String KEY_CHECK_SUBWINDOW = "org.eclipse.swt.internal.control.checksubwindow"; //$NON-NLS-1$

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Widget () {}

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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see #checkSubclass
 * @see #getStyle
 */
public Widget (Widget parent, int style) {
	checkSubclass ();
	checkParent (parent);
	this.style = style;
	display = parent.display;
	reskinWidget ();
}

void _addListener (int eventType, Listener listener) {
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an event of the given type occurs. When the
 * event does occur in the widget, the listener is notified by
 * sending it the <code>handleEvent()</code> message. The event
 * type is one of the event constants defined in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #getListeners(int)
 * @see #removeListener(int, Listener)
 * @see #notifyListeners
 */
public void addListener (int eventType, Listener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	_addListener (eventType, listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the widget is disposed. When the widget is
 * disposed, the listener is notified by sending it the
 * <code>widgetDisposed()</code> message.
 *
 * @param listener the listener which should be notified when the receiver is disposed
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see DisposeListener
 * @see #removeDisposeListener
 */
public void addDisposeListener (DisposeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Dispose, typedListener);
}

long /*int*/ paintWindow () {
	return 0;
}

static int checkBits (int style, int int0, int int1, int int2, int int3, int int4, int int5) {
	int mask = int0 | int1 | int2 | int3 | int4 | int5;
	if ((style & mask) == 0) style |= int0;
	if ((style & int0) != 0) style = (style & ~mask) | int0;
	if ((style & int1) != 0) style = (style & ~mask) | int1;
	if ((style & int2) != 0) style = (style & ~mask) | int2;
	if ((style & int3) != 0) style = (style & ~mask) | int3;
	if ((style & int4) != 0) style = (style & ~mask) | int4;
	if ((style & int5) != 0) style = (style & ~mask) | int5;
	return style;
}

long /*int*/ cellDataProc (long /*int*/ tree_column, long /*int*/ cell, long /*int*/ tree_model, long /*int*/ iter, long /*int*/ data) {
	return 0;
}

void checkOpen () {
	/* Do nothing */
}

void checkOrientation (Widget parent) {
	style &= ~SWT.MIRRORED;
	if ((style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)) == 0) {
		if (parent != null) {
			if ((parent.style & SWT.LEFT_TO_RIGHT) != 0) style |= SWT.LEFT_TO_RIGHT;
			if ((parent.style & SWT.RIGHT_TO_LEFT) != 0) style |= SWT.RIGHT_TO_LEFT;
		}
	}
	style = checkBits (style, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT, 0, 0, 0, 0);
	/* Versions of GTK prior to 2.8 do not render RTL text properly */
	if (OS.GTK_VERSION < OS.VERSION (2, 8, 0)) {
		style &= ~SWT.RIGHT_TO_LEFT;
		style |= SWT.LEFT_TO_RIGHT;			
	}
}

/**
 * Throws an exception if the specified widget can not be
 * used as a parent for the receiver.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 */
void checkParent (Widget parent) {
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (parent.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	parent.checkWidget ();
	parent.checkOpen ();
}

/**
 * Checks that this class can be subclassed.
 * <p>
 * The SWT class library is intended to be subclassed 
 * only at specific, controlled points (most notably, 
 * <code>Composite</code> and <code>Canvas</code> when
 * implementing new widgets). This method enforces this
 * rule unless it is overridden.
 * </p><p>
 * <em>IMPORTANT:</em> By providing an implementation of this
 * method that allows a subclass of a class which does not 
 * normally allow subclassing to be created, the implementer
 * agrees to be fully responsible for the fact that any such
 * subclass will likely fail between SWT releases and will be
 * strongly platform specific. No support is provided for
 * user-written classes which are implemented in this fashion.
 * </p><p>
 * The ability to subclass outside of the allowed SWT classes
 * is intended purely to enable those not on the SWT development
 * team to implement patches in order to get around specific
 * limitations in advance of when those limitations can be
 * addressed by the team. Subclassing should not be attempted
 * without an intimate and detailed understanding of the hierarchy.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Throws an <code>SWTException</code> if the receiver can not
 * be accessed by the caller. This may include both checks on
 * the state of the receiver and more generally on the entire
 * execution context. This method <em>should</em> be called by
 * widget implementors to enforce the standard SWT invariants.
 * <p>
 * Currently, it is an error to invoke any method (other than
 * <code>isDisposed()</code>) on a widget that has had its 
 * <code>dispose()</code> method called. It is also an error
 * to call widget methods from any thread that is different
 * from the thread that created the widget.
 * </p><p>
 * In future releases of SWT, there may be more or fewer error
 * checks and exceptions may be thrown for different reasons.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
protected void checkWidget () {
	Display display = this.display;
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (display.thread != Thread.currentThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if ((state & DISPOSED) != 0) error (SWT.ERROR_WIDGET_DISPOSED);
}

void createHandle (int index) {
}

void createWidget (int index) {
	createHandle (index);
	setOrientation (true);
	hookEvents ();
	register ();
}

void deregister () {
	if (handle == 0) return;
	if ((state & HANDLE) != 0) display.removeWidget (handle);
}

void destroyWidget () {
	long /*int*/ topHandle = topHandle ();
	releaseHandle ();
	if (topHandle != 0 && (state & HANDLE) != 0) {
		OS.gtk_widget_destroy (topHandle);
	}
}

/**
 * Disposes of the operating system resources associated with
 * the receiver and all its descendants. After this method has
 * been invoked, the receiver and all descendants will answer
 * <code>true</code> when sent the message <code>isDisposed()</code>.
 * Any internal connections between the widgets in the tree will
 * have been removed to facilitate garbage collection.
 * This method does nothing if the widget is already disposed.
 * <p>
 * NOTE: This method is not called recursively on the descendants
 * of the receiver. This means that, widget implementers can not
 * detect when a widget is being disposed of by re-implementing
 * this method, but should instead listen for the <code>Dispose</code>
 * event.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #addDisposeListener
 * @see #removeDisposeListener
 * @see #checkWidget
 */
public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (isDisposed ()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	release (true);
}

void error (int code) {
	SWT.error (code);
}

/**
 * Returns the application defined widget data associated
 * with the receiver, or null if it has not been set. The
 * <em>widget data</em> is a single, unnamed field that is
 * stored with every widget. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the widget data needs to be notified
 * when the widget is disposed of, it is the application's
 * responsibility to hook the Dispose event on the widget and
 * do so.
 * </p>
 *
 * @return the widget data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 *
 * @see #setData(Object)
 */
public Object getData () {
	checkWidget();
	return (state & KEYED_DATA) != 0 ? ((Object []) data) [0] : data;
}
/**
 * Returns the application defined property of the receiver
 * with the specified name, or null if it has not been set.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the widget is disposed
 * of, it is the application's responsibility to hook the
 * Dispose event on the widget and do so.
 * </p>
 *
 * @param	key the name of the property
 * @return the value of the property or null if it has not been set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setData(String, Object)
 */
public Object getData (String key) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (key.equals (KEY_CHECK_SUBWINDOW)) {
		return new Boolean ((state & CHECK_SUBWINDOW) != 0);
	}
	if (key.equals(IS_ACTIVE)) return new Boolean(isActive ());
	if ((state & KEYED_DATA) != 0) {
		Object [] table = (Object []) data;
		for (int i=1; i<table.length; i+=2) {
			if (key.equals (table [i])) return table [i+1];
		}
	}
	return null;
}

/**
 * Returns the <code>Display</code> that is associated with
 * the receiver.
 * <p>
 * A widget's display is either provided when it is created
 * (for example, top level <code>Shell</code>s) or is the
 * same as its parent's display.
 * </p>
 *
 * @return the receiver's display
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Display getDisplay () {
	Display display = this.display;
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

/**
 * Returns an array of listeners who will be notified when an event 
 * of the given type occurs. The event type is one of the event constants 
 * defined in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
 * @return an array of listeners that will be notified when the event occurs
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #addListener(int, Listener)
 * @see #removeListener(int, Listener)
 * @see #notifyListeners
 * 
 * @since 3.4
 */
public Listener[] getListeners (int eventType) {
	checkWidget();
	if (eventTable == null) return new Listener[0];
	return eventTable.getListeners(eventType);
}

String getName () {
//	String string = getClass ().getName ();
//	int index = string.lastIndexOf ('.');
//	if (index == -1) return string;	
	String string = getClass ().getName ();
	int index = string.length ();
	while ((--index > 0) && (string.charAt (index) != '.')) {}
	return string.substring (index + 1, string.length ());
}

String getNameText () {
	return "";
}

/**
 * Returns the receiver's style information.
 * <p>
 * Note that the value which is returned by this method <em>may
 * not match</em> the value which was provided to the constructor
 * when the receiver was created. This can occur when the underlying
 * operating system does not support a particular combination of
 * requested styles. For example, if the platform widget used to
 * implement a particular SWT widget always has scroll bars, the
 * result of calling this method would always have the
 * <code>SWT.H_SCROLL</code> and <code>SWT.V_SCROLL</code> bits set.
 * </p>
 *
 * @return the style bits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getStyle () {
	checkWidget ();
	return style;
}


long /*int*/ gtk_activate (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_button_press_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_button_release_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_changed (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_change_value (long /*int*/ widget, long /*int*/ scroll, long /*int*/ value1, long /*int*/ value2) {
	return 0;
}

long /*int*/ gtk_clicked (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_commit (long /*int*/ imcontext, long /*int*/ text) {
	return 0;
}

long /*int*/ gtk_configure_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_create_menu_proxy (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_day_selected (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_day_selected_double_click (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_delete_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_delete_range (long /*int*/ widget, long /*int*/ iter1, long /*int*/ iter2) {
	return 0;
}

long /*int*/ gtk_delete_text (long /*int*/ widget, long /*int*/ start_pos, long /*int*/ end_pos) {
	return 0;
}

long /*int*/ gtk_enter_notify_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_event_after (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_expand_collapse_cursor_row (long /*int*/ widget, long /*int*/ logical, long /*int*/ expand, long /*int*/ open_all) {
	return 0;
}

long /*int*/ gtk_expose_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_focus (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_focus_in_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_focus_out_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_grab_focus (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_hide (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_icon_release (long /*int*/ widget, long /*int*/ icon_pos, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_input (long /*int*/ widget, long /*int*/ arg1) {
	return 0;
}

long /*int*/ gtk_insert_text (long /*int*/ widget, long /*int*/ new_text, long /*int*/ new_text_length, long /*int*/ position) {
	return 0;
}

long /*int*/ gtk_key_press_event (long /*int*/ widget, long /*int*/ event) {
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
	return sendKeyEvent (SWT.KeyDown, gdkEvent) ? 0 : 1;
}

long /*int*/ gtk_key_release_event (long /*int*/ widget, long /*int*/ event) {
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
	return sendKeyEvent (SWT.KeyUp, gdkEvent) ? 0 : 1;
}

long /*int*/ gtk_leave_notify_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_map (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_map_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_mnemonic_activate (long /*int*/ widget, long /*int*/ arg1) {
	return 0;
}

long /*int*/ gtk_month_changed (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_motion_notify_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_move_focus (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_output (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_populate_popup (long /*int*/ widget, long /*int*/ menu) {
	return 0;
}

long /*int*/ gtk_popup_menu (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_preedit_changed (long /*int*/ imcontext) {
	return 0;
}

long /*int*/ gtk_realize (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_row_activated (long /*int*/ tree, long /*int*/ path, long /*int*/ column) {
	return 0;
}

long /*int*/ gtk_row_deleted (long /*int*/ model, long /*int*/ path) {
	return 0;
}

long /*int*/ gtk_row_inserted (long /*int*/ model, long /*int*/ path, long /*int*/ iter) {
	return 0;
}

long /*int*/ gtk_scroll_child (long /*int*/ widget, long /*int*/ scrollType, long /*int*/ horizontal) {
	return 0;
}

long /*int*/ gtk_scroll_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_select (long /*int*/ item) {
	return 0;
}

long /*int*/ gtk_selection_done (long /*int*/ menushell) {
	return 0;
} 

long /*int*/ gtk_show (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_show_help (long /*int*/ widget, long /*int*/ helpType) {
	return 0;
}

long /*int*/ gtk_size_allocate (long /*int*/ widget, long /*int*/ allocation) {
	return 0;
}

long /*int*/ gtk_status_icon_popup_menu (long /*int*/ handle, long /*int*/ button, long /*int*/ activate_time) {
	return 0;
}

long /*int*/ gtk_start_interactive_search (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_style_set (long /*int*/ widget, long /*int*/ previousStyle) {
	return 0;
}

long /*int*/ gtk_switch_page (long /*int*/ widget, long /*int*/ page, long /*int*/ page_num) {
	return 0;
}

long /*int*/ gtk_test_collapse_row (long /*int*/ tree, long /*int*/ iter, long /*int*/ path) {
	return 0;
}

long /*int*/ gtk_test_expand_row (long /*int*/ tree, long /*int*/ iter, long /*int*/ path) {
	return 0;
}

long /*int*/ gtk_text_buffer_insert_text (long /*int*/ widget, long /*int*/ iter, long /*int*/ text, long /*int*/ length) {
	return 0;
}

long /*int*/ gtk_timer () {
	return 0;
}

long /*int*/ gtk_toggled (long /*int*/ renderer, long /*int*/ pathStr) {
	return 0;
}

long /*int*/ gtk_unmap (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_unmap_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

long /*int*/ gtk_unrealize (long /*int*/ widget) {
	return 0;
}

long /*int*/ gtk_value_changed (long /*int*/ adjustment) {
	return 0;
}

long /*int*/ gtk_visibility_notify_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

void gtk_widget_get_allocation (long /*int*/ widget, GtkAllocation allocation) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		OS.gtk_widget_get_allocation (widget, allocation);
	} else {
		allocation.x = OS.GTK_WIDGET_X (widget);
		allocation.y = OS.GTK_WIDGET_Y (widget);
		allocation.width = OS.GTK_WIDGET_WIDTH (widget);
		allocation.height = OS.GTK_WIDGET_HEIGHT (widget);
	}
}

boolean gtk_widget_get_mapped (long /*int*/ widget) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 20, 0)) {
		return OS.gtk_widget_get_mapped (widget);
	} else {
		return OS.GTK_WIDGET_MAPPED (widget);
	}
}

boolean gtk_widget_has_focus (long /*int*/ widget) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		return OS.gtk_widget_has_focus (widget);
	} else {
		return OS.GTK_WIDGET_HAS_FOCUS (widget);
	}
}

long /*int*/ gtk_window_state_event (long /*int*/ widget, long /*int*/ event) {
	return 0;
}

int fontHeight (long /*int*/ font, long /*int*/ widgetHandle) {
	long /*int*/ context = OS.gtk_widget_get_pango_context (widgetHandle);
	long /*int*/ lang = OS.pango_context_get_language (context);
	long /*int*/ metrics = OS.pango_context_get_metrics (context, font, lang);
	int ascent = OS.pango_font_metrics_get_ascent (metrics);
	int descent = OS.pango_font_metrics_get_descent (metrics);
	OS.pango_font_metrics_unref (metrics);
	return OS.PANGO_PIXELS (ascent + descent);
}

long /*int*/ filterProc(long /*int*/ xEvent, long /*int*/ gdkEvent, long /*int*/ data2) {
	return 0;
}

boolean filters (int eventType) {
	return display.filters (eventType);
}

long /*int*/ fixedMapProc (long /*int*/ widget) {
	return 0;
}

long /*int*/ fixedSizeAllocateProc(long /*int*/ widget, long /*int*/ allocationPtr) {
	return OS.Call (Display.oldFixedSizeAllocateProc, widget, allocationPtr);
}

char [] fixMnemonic (String string) {
	return fixMnemonic (string, true);
}

char [] fixMnemonic (String string, boolean replace) {
	int length = string.length ();
	char [] text = new char [length];
	string.getChars (0, length, text, 0);
	int i = 0, j = 0;
	char [] result = new char [length * 2];
	while (i < length) {
		switch (text [i]) {
			case '&':
				if (i + 1 < length && text [i + 1] == '&') {
					result [j++] = text [i++];
				} else {
					if (replace) result [j++] = '_';
				}
				i++;
				break;
			case '_':
				if (replace) result [j++] = '_';
				//FALL THROUGH
			default:
				result [j++] = text [i++];
		}
	}
	return result;
}

boolean isActive () {
	return true;
}

/**
 * Returns <code>true</code> if the widget has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the widget.
 * When a widget has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the widget.
 * </p>
 *
 * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
 */
public boolean isDisposed () {
	return (state & DISPOSED) != 0;
}

/**
 * Returns <code>true</code> if there are any listeners
 * for the specified event type associated with the receiver,
 * and <code>false</code> otherwise. The event type is one of
 * the event constants defined in class <code>SWT</code>.
 *
 * @param eventType the type of event
 * @return true if the event is hooked
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 */
public boolean isListening (int eventType) {
	checkWidget ();
	return hooks (eventType);
}

boolean isValidThread () {
	return getDisplay ().isValidThread ();
}

boolean isValidSubclass() {
	return Display.isValidClass(getClass());
}

void hookEvents () {
}

/*
 * Returns <code>true</code> if the specified eventType is
 * hooked, and <code>false</code> otherwise. Implementations
 * of SWT can avoid creating objects and sending events
 * when an event happens in the operating system but
 * there are no listeners hooked for the event.
 *
 * @param eventType the event to be checked
 *
 * @return <code>true</code> when the eventType is hooked and <code>false</code> otherwise
 *
 * @see #isListening
 */
boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}

long /*int*/ hoverProc (long /*int*/ widget) {
	return 0;
}

long /*int*/ menuPositionProc (long /*int*/ menu, long /*int*/ x, long /*int*/ y, long /*int*/ push_in, long /*int*/ user_data) {
	return 0;
}

boolean mnemonicHit (long /*int*/ mnemonicHandle, char key) {
	if (!mnemonicMatch (mnemonicHandle, key)) return false;
	OS.g_signal_handlers_block_matched (mnemonicHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, MNEMONIC_ACTIVATE);
	boolean result = OS.gtk_widget_mnemonic_activate (mnemonicHandle, false);
	OS.g_signal_handlers_unblock_matched (mnemonicHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, MNEMONIC_ACTIVATE);
	return result;
}

boolean mnemonicMatch (long /*int*/ mnemonicHandle, char key) {
	int keyval1 = OS.gdk_keyval_to_lower (OS.gdk_unicode_to_keyval (key));
	int keyval2 = OS.gdk_keyval_to_lower (OS.gtk_label_get_mnemonic_keyval (mnemonicHandle)); 
	return keyval1 == keyval2;
}

void modifyStyle (long /*int*/ handle, long /*int*/ style) {
	OS.gtk_widget_modify_style (handle, style);
}

/**
 * Notifies all of the receiver's listeners for events
 * of the given type that one such event has occurred by
 * invoking their <code>handleEvent()</code> method.  The
 * event type is one of the event constants defined in class
 * <code>SWT</code>.
 *
 * @param eventType the type of event which has occurred
 * @param event the event data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT
 * @see #addListener
 * @see #getListeners(int)
 * @see #removeListener(int, Listener)
 */
public void notifyListeners (int eventType, Event event) {
	checkWidget();
	if (event == null) event = new Event ();
	sendEvent (eventType, event);
}

void postEvent (int eventType) {
	sendEvent (eventType, null, false);
}

void postEvent (int eventType, Event event) {
	sendEvent (eventType, event, false);
}

void register () {
	if (handle == 0) return;
	if ((state & HANDLE) != 0) display.addWidget (handle, this);
}

void release (boolean destroy) {
	if ((state & DISPOSE_SENT) == 0) {
		state |= DISPOSE_SENT;
		sendEvent (SWT.Dispose);
	}
	if ((state & DISPOSED) == 0) {
		releaseChildren (destroy);
	}
	if ((state & RELEASED) == 0) {
		state |= RELEASED;
		if (destroy) {
			releaseParent ();
			releaseWidget ();
			destroyWidget ();
		} else {
			releaseWidget ();
			releaseHandle ();
		}
	}
}

void releaseChildren (boolean destroy) {
}

void releaseHandle () {
	handle = 0;
	state |= DISPOSED;
	display = null;
}

void releaseParent () {
	/* Do nothing */
}

void releaseWidget () {
	deregister ();
	eventTable = null;
	data = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when an event of the given type occurs. The event
 * type is one of the event constants defined in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
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
 * @see Listener
 * @see SWT
 * @see #addListener
 * @see #getListeners(int)
 * @see #notifyListeners
 */
public void removeListener (int eventType, Listener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when an event of the given type occurs.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 *
 * @param eventType the type of event to listen for
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
 * @see Listener
 * @see #addListener
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
protected void removeListener (int eventType, SWTEventListener handler) {
	checkWidget ();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

long /*int*/ rendererGetSizeProc (long /*int*/ cell, long /*int*/ handle, long /*int*/ cell_area, long /*int*/ x_offset, long /*int*/ y_offset, long /*int*/ width, long /*int*/ height) {
	return 0;
}

long /*int*/ rendererRenderProc (long /*int*/ cell, long /*int*/ window, long /*int*/ handle, long /*int*/ background_area, long /*int*/ cell_area, long /*int*/ expose_area, long /*int*/ flags) {
	return 0;
}

/**
 * Marks the widget to be skinned. 
 * <p>
 * The skin event is sent to the receiver's display when appropriate (usually before the next event
 * is handled). Widgets are automatically marked for skinning upon creation as well as when its skin
 * id or class changes. The skin id and/or class can be changed by calling <code>Display.setData(String, Object)</code> 
 * with the keys SWT.SKIN_ID and/or SWT.SKIN_CLASS. Once the skin event is sent to a widget, it 
 * will not be sent again unless <code>reskin(int)</code> is called on the widget or on an ancestor 
 * while specifying the <code>SWT.ALL</code> flag.  
 * </p>
 * <p>
 * The parameter <code>flags</code> may be either:
 * <dl>
 * <dt><b>SWT.ALL</b></dt>
 * <dd>all children in the receiver's widget tree should be skinned</dd>
 * <dt><b>SWT.NONE</b></dt>
 * <dd>only the receiver should be skinned</dd>
 * </dl>
 * </p>
 * @param flags the flags specifying how to reskin
 * 
 * @exception SWTException 
 * <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.6
 */
public void reskin (int flags) {
	checkWidget ();
	reskinWidget ();
	if ((flags & SWT.ALL) != 0) reskinChildren (flags);
}

void reskinChildren (int flags) {	
}

void reskinWidget() {
	if ((state & SKIN_NEEDED) != SKIN_NEEDED) {
		this.state |= SKIN_NEEDED;
		display.addSkinnableWidget(this);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the widget is disposed.
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
 * @see DisposeListener
 * @see #addDisposeListener
 */
public void removeDisposeListener (DisposeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}

void sendEvent (Event event) {
	Display display = event.display;
	if (!display.filterEvent (event)) {
		if (eventTable != null) eventTable.sendEvent (event);
	}
}

void sendEvent (int eventType) {
	sendEvent (eventType, null, true);
}

void sendEvent (int eventType, Event event) {
	sendEvent (eventType, event, true);
}

void sendEvent (int eventType, Event event, boolean send) {
	if (eventTable == null && !display.filters (eventType)) {
		return;
	}
	if (event == null) event = new Event ();
	event.type = eventType;
	event.display = display;
	event.widget = this;
	if (event.time == 0) {
		event.time = display.getLastEventTime ();
	}
	if (send) {
		sendEvent (event);
	} else {
		display.postEvent (event);
	}
}

boolean sendKeyEvent (int type, GdkEventKey keyEvent) {
	int length = keyEvent.length;
	if (keyEvent.string == 0 || OS.g_utf16_strlen (keyEvent.string, length) <= 1) {
		Event event = new Event ();
		event.time = keyEvent.time;
		if (!setKeyState (event, keyEvent)) return true;
		sendEvent (type, event);
		// widget could be disposed at this point
	
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning false.
		*/
		if (isDisposed ()) return false;
		return event.doit;
	}
	byte [] buffer = new byte [length];
	OS.memmove (buffer, keyEvent.string, length);
	char [] chars = Converter.mbcsToWcs (null, buffer);
	return sendIMKeyEvent (type, keyEvent, chars) != null;
}

char [] sendIMKeyEvent (int type, GdkEventKey keyEvent, char [] chars) {
	int index = 0, count = 0, state = 0;
	long /*int*/ ptr = 0;
	if (keyEvent == null) {
		ptr = OS.gtk_get_current_event ();
		if (ptr != 0) {
			keyEvent = new GdkEventKey ();
			OS.memmove (keyEvent, ptr, GdkEventKey.sizeof);
			switch (keyEvent.type) {
				case OS.GDK_KEY_PRESS:
				case OS.GDK_KEY_RELEASE:
					state = keyEvent.state;
					break;
				default:
					keyEvent = null;
					break;
			}
		}
	}
	if (keyEvent == null) {
		int [] buffer = new int [1];
		OS.gtk_get_current_event_state (buffer);
		state = buffer [0];
	}
	while (index < chars.length) {
		Event event = new Event ();
		if (keyEvent != null && chars.length <= 1) {
			setKeyState (event, keyEvent);
		} else {
			setInputState (event, state);
		}
		event.character = chars [index];
		sendEvent (type, event);
	
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning null.
		*/
		if (isDisposed ()) {
			if (ptr != 0) OS.gdk_event_free (ptr);
			return null;
		}
		if (event.doit) chars [count++] = chars [index];
		index++;
	}
	if (ptr != 0) OS.gdk_event_free (ptr);
	if (count == 0) return null;
	if (index != count) {
		char [] result = new char [count];
		System.arraycopy (chars, 0, result, 0, count);
		return result;
	}
	return chars;
}

void sendSelectionEvent (int eventType) {
	sendSelectionEvent (eventType, null, false);
}

void sendSelectionEvent (int eventType, Event event, boolean send) {
	if (eventTable == null && !display.filters (eventType)) {
		return;
	}
	if (event == null) event = new Event ();
	long /*int*/ ptr = OS.gtk_get_current_event ();
	if (ptr != 0) {
		GdkEvent gdkEvent = new GdkEvent ();
		OS.memmove (gdkEvent, ptr, GdkEvent.sizeof);
		switch (gdkEvent.type) {
			case OS.GDK_KEY_PRESS:
			case OS.GDK_KEY_RELEASE: 
			case OS.GDK_BUTTON_PRESS:
			case OS.GDK_2BUTTON_PRESS: 
			case OS.GDK_BUTTON_RELEASE: {
				int [] state = new int [1];
				OS.gdk_event_get_state (ptr, state);
				setInputState (event, state [0]);
				break;
			}
		}
		OS.gdk_event_free (ptr);
	}
	sendEvent (eventType, event, send);
}

/**
 * Sets the application defined widget data associated
 * with the receiver to be the argument. The <em>widget
 * data</em> is a single, unnamed field that is stored
 * with every widget. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the widget data needs to be notified
 * when the widget is disposed of, it is the application's
 * responsibility to hook the Dispose event on the widget and
 * do so.
 * </p>
 *
 * @param data the widget data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - when the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 * 
 * @see #getData()
 */
public void setData (Object data) {
	checkWidget();
	if ((state & KEYED_DATA) != 0) {
		((Object []) this.data) [0] = data;
	} else {
		this.data = data;
	}
}

/**
 * Sets the application defined property of the receiver
 * with the specified name to the given value.
 * <p>
 * Applications may associate arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the widget is disposed
 * of, it is the application's responsibility to hook the
 * Dispose event on the widget and do so.
 * </p>
 *
 * @param key the name of the property
 * @param value the new value for the property
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getData(String)
 */
public void setData (String key, Object value) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);

	if (key.equals (KEY_CHECK_SUBWINDOW)) {
		if (value != null && value instanceof Boolean) {
			if (((Boolean)value).booleanValue ()) {
				state |= CHECK_SUBWINDOW;
			} else {
				state &= ~CHECK_SUBWINDOW;
			}
		}
		return;
	}

	int index = 1;
	Object [] table = null;
	if ((state & KEYED_DATA) != 0) {
		table = (Object []) data;
		while (index < table.length) {
			if (key.equals (table [index])) break;
			index += 2;
		}
	}
	if (value != null) {
		if ((state & KEYED_DATA) != 0) {
			if (index == table.length) {
				Object [] newTable = new Object [table.length + 2];
				System.arraycopy (table, 0, newTable, 0, table.length);
				data = table = newTable;
			}
		} else {
			table = new Object [3];
			table [0] = data;
			data = table;
			state |= KEYED_DATA;
		}
		table [index] = key;
		table [index + 1] = value;
	} else {
		if ((state & KEYED_DATA) != 0) {
			if (index != table.length) {
				int length = table.length - 2;
				if (length == 1) {
					data = table [0];
					state &= ~KEYED_DATA;
				} else {
					Object [] newTable = new Object [length];
					System.arraycopy (table, 0, newTable, 0, index);
					System.arraycopy (table, index + 2, newTable, index, length - index);
					data = newTable;
				}
			}
		}
	}
	if (key.equals(SWT.SKIN_CLASS) || key.equals(SWT.SKIN_ID)) this.reskin(SWT.ALL);
}

void setForegroundColor (long /*int*/ handle, GdkColor color) {
	long /*int*/ style = OS.gtk_widget_get_modifier_style (handle);
	OS.gtk_rc_style_set_fg (style, OS.GTK_STATE_NORMAL, color);
	OS.gtk_rc_style_set_fg (style, OS.GTK_STATE_ACTIVE, color);
	OS.gtk_rc_style_set_fg (style, OS.GTK_STATE_PRELIGHT, color);
	int flags = OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_NORMAL);
	flags = (color == null) ? flags & ~OS.GTK_RC_FG: flags | OS.GTK_RC_FG;
	OS.gtk_rc_style_set_color_flags (style, OS.GTK_STATE_NORMAL, flags);
	flags = OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_ACTIVE);
	flags = (color == null) ? flags & ~OS.GTK_RC_FG: flags | OS.GTK_RC_FG;
	OS.gtk_rc_style_set_color_flags (style, OS.GTK_STATE_ACTIVE, flags);
	flags = OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_PRELIGHT);
	flags = (color == null) ? flags & ~OS.GTK_RC_FG: flags | OS.GTK_RC_FG;
	OS.gtk_rc_style_set_color_flags (style, OS.GTK_STATE_PRELIGHT, flags);

	OS.gtk_rc_style_set_text (style, OS.GTK_STATE_NORMAL, color);
	OS.gtk_rc_style_set_text (style, OS.GTK_STATE_ACTIVE, color);
	OS.gtk_rc_style_set_text (style, OS.GTK_STATE_PRELIGHT, color);
	flags = OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_NORMAL);
	flags = (color == null) ? flags & ~OS.GTK_RC_TEXT: flags | OS.GTK_RC_TEXT;
	OS.gtk_rc_style_set_color_flags (style, OS.GTK_STATE_NORMAL, flags);
	flags = OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_PRELIGHT);	
	flags = (color == null) ? flags & ~OS.GTK_RC_TEXT: flags | OS.GTK_RC_TEXT;
	OS.gtk_rc_style_set_color_flags (style, OS.GTK_STATE_PRELIGHT, flags);	
	flags = OS.gtk_rc_style_get_color_flags (style, OS.GTK_STATE_ACTIVE);
	flags = (color == null) ? flags & ~OS.GTK_RC_TEXT: flags | OS.GTK_RC_TEXT;
	OS.gtk_rc_style_set_color_flags (style, OS.GTK_STATE_ACTIVE, flags);
	modifyStyle (handle, style);	
}

boolean setInputState (Event event, int state) {
	if ((state & OS.GDK_MOD1_MASK) != 0) event.stateMask |= SWT.ALT;
	if ((state & OS.GDK_SHIFT_MASK) != 0) event.stateMask |= SWT.SHIFT;
	if ((state & OS.GDK_CONTROL_MASK) != 0) event.stateMask |= SWT.CONTROL;
	if ((state & OS.GDK_BUTTON1_MASK) != 0) event.stateMask |= SWT.BUTTON1;
	if ((state & OS.GDK_BUTTON2_MASK) != 0) event.stateMask |= SWT.BUTTON2;
	if ((state & OS.GDK_BUTTON3_MASK) != 0) event.stateMask |= SWT.BUTTON3;
	return true;
}

boolean setKeyState (Event event, GdkEventKey keyEvent) {
	if (keyEvent.string != 0 && OS.g_utf16_strlen (keyEvent.string, keyEvent.length) > 1) return false;
	boolean isNull = false;
	event.keyCode = Display.translateKey (keyEvent.keyval);
	switch (keyEvent.keyval) {
		case OS.GDK_BackSpace:		event.character = SWT.BS; break;
		case OS.GDK_Linefeed:		event.character = SWT.LF; break;
		case OS.GDK_KP_Enter:
		case OS.GDK_Return: 		event.character = SWT.CR; break;
		case OS.GDK_KP_Delete:
		case OS.GDK_Delete:			event.character = SWT.DEL; break;
		case OS.GDK_Escape:			event.character = SWT.ESC; break;
		case OS.GDK_Tab:
		case OS.GDK_ISO_Left_Tab: 	event.character = SWT.TAB; break;
		default: {
			if (event.keyCode == 0) {
				int [] keyval = new int [1], effective_group = new int [1], level = new int [1], consumed_modifiers = new int [1];
				if (OS.gdk_keymap_translate_keyboard_state (OS.gdk_keymap_get_default (), keyEvent.hardware_keycode, 0, keyEvent.group, keyval, effective_group, level, consumed_modifiers)) {
					event.keyCode = OS.gdk_keyval_to_unicode (keyval [0]);
				}
			}
			int key = keyEvent.keyval;
			if ((keyEvent.state & OS.GDK_CONTROL_MASK) != 0 && (0 <= key && key <= 0x7F)) {
				if ('a'  <= key && key <= 'z') key -= 'a' - 'A';
				if (64 <= key && key <= 95) key -= 64;
				event.character = (char) key;
				isNull = keyEvent.keyval == '@' && key == 0;
			} else {
				event.character = (char) OS.gdk_keyval_to_unicode (key);
			}
		}
	}
	setLocationState (event, keyEvent);
	if (event.keyCode == 0 && event.character == 0) {
		if (!isNull) return false;
	}
	return setInputState (event, keyEvent.state);
}

void setLocationState (Event event, GdkEventKey keyEvent) {
	switch (keyEvent.keyval) {
		case OS.GDK_Alt_L:
		case OS.GDK_Shift_L:
		case OS.GDK_Control_L:
			event.keyLocation = SWT.LEFT;
			break;
		case OS.GDK_Alt_R:
		case OS.GDK_Shift_R:
		case OS.GDK_Control_R:
				event.keyLocation = SWT.RIGHT;
			break;
		case OS.GDK_KP_0:
		case OS.GDK_KP_1:
		case OS.GDK_KP_2:
		case OS.GDK_KP_3:
		case OS.GDK_KP_4:
		case OS.GDK_KP_5:
		case OS.GDK_KP_6:
		case OS.GDK_KP_7:
		case OS.GDK_KP_8:
		case OS.GDK_KP_9:
		case OS.GDK_KP_Add:
		case OS.GDK_KP_Decimal:
		case OS.GDK_KP_Delete:
		case OS.GDK_KP_Divide:
		case OS.GDK_KP_Down:
		case OS.GDK_KP_End:
		case OS.GDK_KP_Enter:
		case OS.GDK_KP_Equal:
		case OS.GDK_KP_Home:
		case OS.GDK_KP_Insert:
		case OS.GDK_KP_Left:
		case OS.GDK_KP_Multiply:
		case OS.GDK_KP_Page_Down:
		case OS.GDK_KP_Page_Up:
		case OS.GDK_KP_Right:
		case OS.GDK_KP_Subtract:
		case OS.GDK_KP_Up:
		case OS.GDK_Num_Lock:
			event.keyLocation = SWT.KEYPAD;
			break;
	}
}

void setOrientation (boolean create) {
}

boolean setTabGroupFocus (boolean next) {
	return setTabItemFocus (next);
}

boolean setTabItemFocus (boolean next) {
	return false;
}

long /*int*/ shellMapProc (long /*int*/ handle, long /*int*/ arg0, long /*int*/ user_data) {
	return 0;
}

long /*int*/ sizeAllocateProc (long /*int*/ handle, long /*int*/ arg0, long /*int*/ user_data) {
	return 0;
}

long /*int*/ sizeRequestProc (long /*int*/ handle, long /*int*/ arg0, long /*int*/ user_data) {
	return 0;
}
long /*int*/ g_object_ref_sink (long /*int*/ object) {
	if (OS.GLIB_VERSION >= OS.VERSION (2, 10, 0)) { 
		return OS.g_object_ref_sink (object);
	} else {
		OS.gtk_object_sink (object);
	}
	return 0;
}

boolean gtk_widget_get_sensitive (long /*int*/ widget) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		return OS.gtk_widget_get_sensitive (widget);
	} else {
		return OS.GTK_WIDGET_SENSITIVE (widget);
	}
}

boolean gtk_widget_get_visible (long /*int*/ widget) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		return OS.gtk_widget_get_visible (widget);
	} else {
		return (OS.GTK_WIDGET_FLAGS (widget) & OS.GTK_VISIBLE) != 0;
	}
}

boolean gtk_widget_get_realized (long /*int*/ widget) {
	 if (OS.GTK_VERSION >= OS.VERSION (2, 20, 0)) {
		 return OS.gtk_widget_get_realized (widget);
	 } else {
		 return (OS.GTK_WIDGET_FLAGS (widget) & OS.GTK_REALIZED) != 0;
	 }
}

boolean gtk_widget_get_can_default (long /*int*/ widget){
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		return OS.gtk_widget_get_can_default (widget);
	} else {
		return (OS.GTK_WIDGET_FLAGS (widget) & OS.GTK_CAN_DEFAULT) != 0;
	}
}

boolean gtk_widget_get_has_window (long /*int*/ widget) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		return OS.gtk_widget_get_has_window (widget);
	} else {
		return (OS.GTK_WIDGET_FLAGS (widget) & OS.GTK_NO_WINDOW) == 0;
	}
}

long /*int*/ gtk_widget_get_window (long /*int*/ widget){
	if (OS.GTK_VERSION >= OS.VERSION(2, 14, 0)){
		return OS.gtk_widget_get_window (widget);
	} else {
		return OS.GTK_WIDGET_WINDOW (widget);
	}
}

void gtk_widget_set_can_default (long /*int*/ widget, boolean can_default) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		OS.gtk_widget_set_can_default (widget, can_default);
	} else {
		if (can_default) {
			OS.GTK_WIDGET_SET_FLAGS (widget, OS.GTK_CAN_DEFAULT);
		} else {
			OS.GTK_WIDGET_UNSET_FLAGS (widget, OS.GTK_CAN_DEFAULT);
		}
	}
}

void gtk_widget_set_can_focus (long /*int*/ widget, boolean can_focus) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		OS.gtk_widget_set_can_focus (widget, can_focus);
	} else {
		if (can_focus) {
			OS.GTK_WIDGET_SET_FLAGS (widget, OS.GTK_CAN_FOCUS);
		} else {
			OS.GTK_WIDGET_UNSET_FLAGS (widget, OS.GTK_CAN_FOCUS);
		}
	}
}

void gtk_widget_set_mapped (long /*int*/ widget, boolean mapped) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 20, 0)) {
		OS.gtk_widget_set_mapped (widget, mapped);
	} else {
		if (mapped) {
			OS.GTK_WIDGET_SET_FLAGS (widget, OS.GTK_MAPPED);
		} else {
			OS.GTK_WIDGET_UNSET_FLAGS (widget, OS.GTK_MAPPED);
		}
	}
}

void gtk_widget_set_visible (long /*int*/ widget, boolean visible) {
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		OS.gtk_widget_set_visible (widget,visible);
	} else {
		if (visible) {
			OS.GTK_WIDGET_SET_FLAGS (widget, OS.GTK_VISIBLE);
		} else {
			OS.GTK_WIDGET_UNSET_FLAGS (widget, OS.GTK_VISIBLE);
		}
	}
}

void gtk_widget_set_receives_default (long /*int*/ widget, boolean receives_default) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 18, 0)) {
		OS.gtk_widget_set_receives_default (widget, receives_default);	
	} else {
		if (receives_default) {
			OS.GTK_WIDGET_SET_FLAGS (widget, OS.GTK_RECEIVES_DEFAULT);
		} else {
			OS.GTK_WIDGET_UNSET_FLAGS (widget, OS.GTK_RECEIVES_DEFAULT);
		}
	}
}

void gdk_pixmap_get_size (long /*int*/ pixmap, int[] width, int[] height) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 24, 0)) {
		OS.gdk_pixmap_get_size (pixmap, width, height);
	} else {
		OS.gdk_drawable_get_size (pixmap, width, height);
	}
}

void gdk_window_get_size (long /*int*/ drawable, int[] width, int[] height) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 24, 0)) {
		width[0] = OS.gdk_window_get_width (drawable);
		height[0] = OS.gdk_window_get_height (drawable);
	} else {
		OS.gdk_drawable_get_size (drawable, width, height);
	}
}

long /*int*/ gtk_box_new (int orientation, boolean homogeneous, int spacing) {
	long /*int*/ box = 0;
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		box = OS.gtk_box_new (orientation, spacing);
		OS.gtk_box_set_homogeneous (box, homogeneous);
	} else {
		if (orientation == OS.GTK_ORIENTATION_HORIZONTAL) {
			box = OS.gtk_hbox_new (homogeneous, spacing);
		} else {
			box = OS.gtk_vbox_new (homogeneous, spacing);
		}
	}
	return box;
}

int gdk_pointer_grab (long /*int*/ window, int grab_ownership, boolean owner_events, int event_mask, long /*int*/ confine_to, long /*int*/ cursor, int time_) {
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		long /*int*/ display = 0;
		if( window != 0) {
			display = OS.gdk_window_get_display (window);
		} else {
			window = OS.gdk_get_default_root_window ();
			display = OS.gdk_window_get_display (window);
		}
		long /*int*/ device_manager = OS.gdk_display_get_device_manager (display);
		long /*int*/ pointer = OS.gdk_device_manager_get_client_pointer (device_manager);
		return OS.gdk_device_grab (pointer, window, grab_ownership, owner_events, event_mask, cursor, time_);
	} else {
		return OS.gdk_pointer_grab (window, owner_events, event_mask, confine_to, cursor, time_);
	}
}

void gdk_pointer_ungrab (long /*int*/ window, int time_) {
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		long /*int*/ display = OS.gdk_window_get_display (window);
		long /*int*/ device_manager = OS.gdk_display_get_device_manager (display);
		long /*int*/ pointer = OS.gdk_device_manager_get_client_pointer (device_manager);
		OS.gdk_device_ungrab (pointer, time_);
	} else {
		OS.gdk_pointer_ungrab (time_);
	}
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	String string = "*Disposed*";
	if (!isDisposed ()) {
		string = "*Wrong Thread*";
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}";
}

long /*int*/ topHandle () {
	return handle;
}

long /*int*/ timerProc (long /*int*/ widget) {
	return 0;
}

boolean translateTraversal (int event) {
	return false;
}

long /*int*/ windowProc (long /*int*/ handle, long /*int*/ user_data) {
	switch ((int)/*64*/user_data) {
		case ACTIVATE: return gtk_activate (handle);
		case CHANGED: return gtk_changed (handle);
		case CLICKED: return gtk_clicked (handle);
		case CREATE_MENU_PROXY: return gtk_create_menu_proxy (handle);
		case DAY_SELECTED: return gtk_day_selected (handle);
		case DAY_SELECTED_DOUBLE_CLICK: return gtk_day_selected_double_click (handle);
		case HIDE: return gtk_hide (handle);
		case GRAB_FOCUS: return gtk_grab_focus (handle);
		case MAP: return gtk_map (handle);
		case MONTH_CHANGED: return gtk_month_changed (handle);
		case OUTPUT: return gtk_output (handle);
		case POPUP_MENU: return gtk_popup_menu (handle);
		case PREEDIT_CHANGED: return gtk_preedit_changed (handle);
		case REALIZE: return gtk_realize (handle);
		case START_INTERACTIVE_SEARCH: return gtk_start_interactive_search (handle);
		case SELECT: return gtk_select (handle);
		case SELECTION_DONE: return gtk_selection_done (handle);
		case SHOW: return gtk_show (handle);
		case VALUE_CHANGED: return gtk_value_changed (handle);
		case UNMAP: return gtk_unmap (handle);
		case UNREALIZE: return gtk_unrealize (handle);
		default: return 0;
	}
}

long /*int*/ windowProc (long /*int*/ handle, long /*int*/ arg0, long /*int*/ user_data) {
	switch ((int)/*64*/user_data) {
		case EXPOSE_EVENT_INVERSE: {
			GdkEventExpose gdkEvent = new GdkEventExpose ();
			OS.memmove (gdkEvent, arg0, GdkEventExpose.sizeof);
			long /*int*/ paintWindow = paintWindow();
			long /*int*/ window = gdkEvent.window;
			if (window != paintWindow) return 0;
			return (state & OBSCURED) != 0 ? 1 : 0;
		}
		case BUTTON_PRESS_EVENT_INVERSE:
		case BUTTON_RELEASE_EVENT_INVERSE:
		case MOTION_NOTIFY_EVENT_INVERSE: {
			return 1;
		}
		case BUTTON_PRESS_EVENT: return gtk_button_press_event (handle, arg0);
		case BUTTON_RELEASE_EVENT: return gtk_button_release_event (handle, arg0);
		case COMMIT: return gtk_commit (handle, arg0);
		case CONFIGURE_EVENT: return gtk_configure_event (handle, arg0);
		case DELETE_EVENT: return gtk_delete_event (handle, arg0);
		case ENTER_NOTIFY_EVENT: return gtk_enter_notify_event (handle, arg0);
		case EVENT: return gtk_event (handle, arg0);
		case EVENT_AFTER: return gtk_event_after (handle, arg0);
		case EXPOSE_EVENT: return gtk_expose_event (handle, arg0);
		case FOCUS: return gtk_focus (handle, arg0);
		case FOCUS_IN_EVENT: return gtk_focus_in_event (handle, arg0);
		case FOCUS_OUT_EVENT: return gtk_focus_out_event (handle, arg0);
		case KEY_PRESS_EVENT: return gtk_key_press_event (handle, arg0);
		case KEY_RELEASE_EVENT: return gtk_key_release_event (handle, arg0);
		case INPUT: return gtk_input (handle, arg0);
		case LEAVE_NOTIFY_EVENT: return gtk_leave_notify_event (handle, arg0);
		case MAP_EVENT: return gtk_map_event (handle, arg0);
		case MNEMONIC_ACTIVATE: return gtk_mnemonic_activate (handle, arg0);
		case MOTION_NOTIFY_EVENT: return gtk_motion_notify_event (handle, arg0);
		case MOVE_FOCUS: return gtk_move_focus (handle, arg0);
		case POPULATE_POPUP: return gtk_populate_popup (handle, arg0);
		case SCROLL_EVENT:	return gtk_scroll_event (handle, arg0);
		case SHOW_HELP: return gtk_show_help (handle, arg0);
		case SIZE_ALLOCATE: return gtk_size_allocate (handle, arg0);
		case STYLE_SET: return gtk_style_set (handle, arg0);
		case TOGGLED: return gtk_toggled (handle, arg0);
		case UNMAP_EVENT: return gtk_unmap_event (handle, arg0);
		case VISIBILITY_NOTIFY_EVENT: return gtk_visibility_notify_event (handle, arg0);
		case WINDOW_STATE_EVENT: return gtk_window_state_event (handle, arg0);
		case ROW_DELETED: return gtk_row_deleted (handle, arg0);
		default: return 0;
	}
}

long /*int*/ windowProc (long /*int*/ handle, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ user_data) {
	switch ((int)/*64*/user_data) {
		case DELETE_RANGE: return gtk_delete_range (handle, arg0, arg1);
		case DELETE_TEXT: return gtk_delete_text (handle, arg0, arg1);
		case ICON_RELEASE: return gtk_icon_release (handle, arg0, arg1);
		case ROW_ACTIVATED: return gtk_row_activated (handle, arg0, arg1);
		case SCROLL_CHILD: return gtk_scroll_child (handle, arg0, arg1);
		case STATUS_ICON_POPUP_MENU: return gtk_status_icon_popup_menu (handle, arg0, arg1);
		case SWITCH_PAGE: return gtk_switch_page (handle, arg0, arg1);
		case TEST_COLLAPSE_ROW: return gtk_test_collapse_row (handle, arg0, arg1);
		case TEST_EXPAND_ROW: return gtk_test_expand_row(handle, arg0, arg1);
		case ROW_INSERTED: return gtk_row_inserted (handle, arg0, arg1);
		default: return 0;
	}
}

long /*int*/ windowProc (long /*int*/ handle, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ user_data) {
	switch ((int)/*64*/user_data) {
		case CHANGE_VALUE: return gtk_change_value (handle, arg0, arg1, arg2);
		case EXPAND_COLLAPSE_CURSOR_ROW: return gtk_expand_collapse_cursor_row (handle, arg0, arg1, arg2);
		case INSERT_TEXT: return gtk_insert_text (handle, arg0, arg1, arg2);
		case TEXT_BUFFER_INSERT_TEXT: return gtk_text_buffer_insert_text (handle, arg0, arg1, arg2);
		default: return 0;
	}
}

void gdk_cursor_unref (long /*int*/ cursor) {
	if (OS.GTK_VERSION >= OS.VERSION(3, 0, 0)) {
		OS.g_object_unref (cursor);
	} else {
		OS.gdk_cursor_unref(cursor);
	}
}

long /*int*/ gdk_window_get_device_position (long /*int*/ window, int[] x, int[] y, int[] mask) {
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		long /*int*/ display = 0;
		if( window != 0) {
			display = OS.gdk_window_get_display (window);
		} else {
			window = OS.gdk_get_default_root_window ();
			display = OS.gdk_window_get_display (window);
		}
		long /*int*/ device_manager = OS.gdk_display_get_device_manager (display);
		long /*int*/ pointer = OS.gdk_device_manager_get_client_pointer (device_manager);
		return OS.gdk_window_get_device_position(window, pointer, x, y, mask);
	} else {
		return OS.gdk_window_get_pointer (window, x, y, mask);
	}
}

void gtk_render_frame (long /*int*/ style, long /*int*/ window, int state_type, int shadow_type, GdkRectangle area, long /*int*/ widget, byte[] detail, int x , int y, int width, int height) {
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		long /*int*/ cairo = OS.gdk_cairo_create (window);
		long /*int*/ context = OS.gtk_widget_get_style_context (style);
		OS.gtk_render_frame (context, cairo, x, y, width, height);
		Cairo.cairo_destroy (cairo);
	} else {
		OS.gtk_paint_flat_box (style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	}
}

void gtk_cell_renderer_get_preferred_size (long /*int*/ cell, long /*int*/ widget,  int[] width, int[] height) {
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		GtkRequisition minimum_size = new GtkRequisition ();
		OS.gtk_cell_renderer_get_preferred_size (cell, widget, minimum_size, null);
		if (width != null) width [0] = minimum_size.width;
		if (height != null) height[0] = minimum_size.height;
	} else {
		OS.gtk_cell_renderer_get_size (cell, widget, null, null, null, width, height);
	}
}

void gtk_widget_get_preferred_size (long /*int*/ widget, GtkRequisition requisition){
	if (OS.GTK_VERSION >= OS.VERSION(3, 0, 0)) {
		OS.gtk_widget_get_preferred_size (widget, requisition, null);
	} else {
		OS.gtk_widget_size_request (widget, requisition);
	}
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	public long handle;
	int style, state;
	Display display;
	EventTable eventTable;
	Object data;

	/* Global state flags
	 *
	 * Common code pattern:
	 * & - think of AND as removing.
	 * | - think of OR as adding.
	 * state & ~flag  -- Think as "removing flag"
	 * state |  flag  -- Think as "adding flag"
	 *
	 * state |= flag  -- Flag is being added to state.
	 * state &= ~flag -- Flag is being removed from state.
	 * state & flag != 0 -- true if flag is present (think >0 = true)
	 * state & flag == 0 -- true if flag is absent  (think 0 = false)
	 *
	 * (state & (flag1 | flag2)) != 0 -- true if either of the flags are present.
	 * (state & (flag1 | flag2)) == 0 -- true if both flag1 & flag2 are absent.
	 */
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

	/* Bidi "auto" text direction */
	static final int HAS_AUTO_DIRECTION = 0;

	/* Bidi flag and for auto text direction */
	static final int AUTO_TEXT_DIRECTION = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;

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
	static final int DRAW = EXPOSE_EVENT;
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
	static final int STYLE_UPDATED = 48;
	static final int SWITCH_PAGE = 49;
	static final int TEST_COLLAPSE_ROW = 50;
	static final int TEST_EXPAND_ROW = 51;
	static final int TEXT_BUFFER_INSERT_TEXT = 52;
	static final int TOGGLED = 53;
	static final int UNMAP = 54;
	static final int UNMAP_EVENT = 55;
	static final int UNREALIZE = 56;
	static final int VALUE_CHANGED = 57;
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
	static final int ROW_HAS_CHILD_TOGGLED = 84;
	static final int POPPED_UP = 85;
	static final int FOCUS_IN = 86;
	static final int FOCUS_OUT = 87;
	static final int IM_UPDATE = 88;
	static final int KEY_PRESSED = 89;
	static final int KEY_RELEASED = 90;
	static final int DECELERATE = 91;
	static final int SCROLL = 92;
	static final int SCROLL_BEGIN = 93;
	static final int SCROLL_END = 94;
	static final int ENTER = 95;
	static final int LEAVE = 96;
	static final int MOTION = 97;
	static final int MOTION_INVERSE = 98;
	static final int CLOSE_REQUEST = 99;
	static final int GESTURE_PRESSED = 100;
	static final int GESTURE_RELEASED = 101;
	static final int NOTIFY_STATE = 102;
	static final int SIZE_ALLOCATE_GTK4 = 103;
	static final int DPI_CHANGED = 104;
	static final int NOTIFY_DEFAULT_HEIGHT = 105;
	static final int NOTIFY_DEFAULT_WIDTH = 106;
	static final int NOTIFY_MAXIMIZED = 107;
	static final int COMPUTE_SIZE = 108;
	static final int LAST_SIGNAL = 109;

	static final String IS_ACTIVE = "org.eclipse.swt.internal.control.isactive"; //$NON-NLS-1$
	static final String KEY_CHECK_SUBWINDOW = "org.eclipse.swt.internal.control.checksubwindow"; //$NON-NLS-1$
	static final String KEY_GTK_CSS = "org.eclipse.swt.internal.gtk.css"; //$NON-NLS-1$

	static Callback gdkSeatGrabPrepareFunc;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Widget () {
	notifyCreationTracker();
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
	notifyCreationTracker();
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

long paintWindow () {
	return 0;
}

long paintSurface () {
	return 0;
}

long cssHandle() {
	return handle;
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

long cellDataProc (long tree_column, long cell, long tree_model, long iter, long data) {
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
	long topHandle = topHandle ();
	releaseHandle ();
	if (topHandle != 0 && (state & HANDLE) != 0) {
		if (GTK.GTK4) {
			GTK.gtk_widget_unparent(topHandle);
		} else {
			GTK3.gtk_widget_destroy(topHandle);
		}
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

long dpiChanged (long object, long arg0) {
	int oldScaleFactor = DPIUtil.getDeviceZoom() / 100;
	int newScaleFactor = GTK.gtk_widget_get_scale_factor(object);

	if (oldScaleFactor != newScaleFactor) {
		display.dpiChanged(newScaleFactor);

		Event event = new Event();
		event.type = SWT.ZoomChanged;
		event.widget = this;
		event.detail = newScaleFactor;
		event.doit = true;
		notifyListeners(SWT.ZoomChanged, event);
	}

	return 0;
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
		return (state & CHECK_SUBWINDOW) != 0;
	}
	if (key.equals(IS_ACTIVE)) return isActive ();
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


long gtk_activate (long widget) {
	return 0;
}

void gtk_adjustment_get(long adjustmentHandle, GtkAdjustment adjustment) {
	adjustment.lower = GTK.gtk_adjustment_get_lower(adjustmentHandle);
	adjustment.upper = GTK.gtk_adjustment_get_upper(adjustmentHandle);
	adjustment.page_increment = GTK.gtk_adjustment_get_page_increment(adjustmentHandle);
	adjustment.step_increment = GTK.gtk_adjustment_get_step_increment(adjustmentHandle);
	adjustment.page_size = GTK.gtk_adjustment_get_page_size(adjustmentHandle);
	adjustment.value = GTK.gtk_adjustment_get_value(adjustmentHandle);
}

long gtk_button_press_event (long widget, long event) {
	return 0;
}

long gtk_button_release_event (long widget, long event) {
	return 0;
}

/**
 * @param gesture the corresponding controller responsible for capturing the event
 * @param n_press how many touch/button presses happened with this one
 * @param x the x coordinate, in widget allocation coordinates
 * @param y the y coordinate, in widget allocation coordinates
 * @param event the GdkEvent captured
 */
void gtk_gesture_press_event(long gesture, int n_press, double x, double y, long event) {}

/**
 * @param gesture the corresponding controller responsible for capturing the event
 * @param n_press how many touch/button presses happened with this one
 * @param x the x coordinate, in widget allocation coordinates
 * @param y the y coordinate, in widget allocation coordinates
 * @param event the GdkEvent captured
 */
void gtk_gesture_release_event(long gesture, int n_press, double x, double y, long event) {}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param x the x coordinate
 * @param y the y coordinate
 * @param event the GdkEvent captured
 */
void gtk4_motion_event(long controller, double x, double y, long event) {}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param keyval the pressed key
 * @param keycode raw code of the pressed key
 * @param state the bitmask, representing the state of the modifier keys and pointer buttons
 * @param event the GdkEvent captured
 * @return TRUE if the event has been fully/properly handled, otherwise FALSE
 */
boolean gtk4_key_press_event(long controller, int keyval, int keycode, int state, long event) {
	return !sendKeyEvent(SWT.KeyDown, event);
}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param keyval the released key
 * @param keycode raw code of the released key
 * @param state the bitmask, representing the state of the modifier keys and pointer buttons
 * @param event the GdkEvent captured
 */
void gtk4_key_release_event(long controller, int keyval, int keycode, int state, long event) {
	sendKeyEvent(SWT.KeyUp, event);
}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param event the GdkEvent captured
 */
void gtk4_focus_enter_event(long controller, long event) {}

/**
 * @param handle the handle of the window that caused the event
 * @param event the type of event, should be FocusIn or FocusOut
 */
void gtk4_focus_window_event(long handle, long event) {
	if(event == SWT.FocusIn) gtk_focus_in_event (handle, event);
	else gtk_focus_out_event(handle, event);
}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param event the GdkEvent captured
 */
void gtk4_focus_leave_event(long controller, long event) {}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param x x coordinate of pointer location
 * @param y y coordinate of pointer location
 * @param event the GdkEvent captured
 */
void gtk4_enter_event(long controller, double x, double y, long event) {}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param event the GdkEvent captured
 */
void gtk4_leave_event(long controller, long event) {}

/**
 * @param controller the corresponding controller responsible for capturing the event
 * @param dx x delta
 * @param dy y delta
 * @param event the GdkEvent captured
 * @return TRUE if the scroll event was handled, FALSE otherwise
 */
boolean gtk4_scroll_event(long controller, double dx, double dy, long event) {
	return false;
}

long gtk_changed (long widget) {
	return 0;
}

boolean gtk_change_value (long widget, int scroll, double value, long user_data) {
	return false;
}

long gtk_clicked (long widget) {
	return 0;
}

long gtk_close_request (long widget) {
	return 0;
}

long gtk_commit (long imcontext, long text) {
	return 0;
}

long gtk_configure_event (long widget, long event) {
	return 0;
}

long gtk_create_menu_proxy (long widget) {
	return 0;
}

long gtk_day_selected (long widget) {
	return 0;
}

long gtk_day_selected_double_click (long widget) {
	return 0;
}

long gtk_delete_event (long widget, long event) {
	return 0;
}

long gtk_delete_range (long widget, long iter1, long iter2) {
	return 0;
}

long gtk_delete_text (long widget, long start_pos, long end_pos) {
	return 0;
}

long gtk_enter_notify_event (long widget, long event) {
	return 0;
}

long gtk_event_after (long widget, long event) {
	return 0;
}

long gtk_expand_collapse_cursor_row (long widget, long logical, long expand, long open_all) {
	return 0;
}

long gtk_draw (long widget, long cairo) {
	return 0;
}

void gtk4_draw(long widget, long cairo, Rectangle bounds) {}

long gtk_focus (long widget, long event) {
	return 0;
}

long gtk_focus_in_event (long widget, long event) {
	return 0;
}

long gtk_focus_out_event (long widget, long event) {
	return 0;
}

long gtk_grab_focus (long widget) {
	return 0;
}

long gtk_hide (long widget) {
	return 0;
}

long gtk_icon_release (long widget, long icon_pos, long event) {
	return 0;
}

long gtk_input (long widget, long arg1) {
	return 0;
}

long gtk_insert_text (long widget, long new_text, long new_text_length, long position) {
	return 0;
}

long gtk_key_press_event (long widget, long event) {
	return sendKeyEvent (SWT.KeyDown, event) ? 0 : 1;
}

long gtk_key_release_event (long widget, long event) {
	return sendKeyEvent (SWT.KeyUp, event) ? 0 : 1;
}

long gtk_leave_notify_event (long widget, long event) {
	return 0;
}

long gtk_map (long widget) {
	return 0;
}

long gtk_map_event (long widget, long event) {
	return 0;
}

/**
 * <p>GTK3.22+ has API which allows clients of GTK to connect a menu to the "popped-up" signal.
 * This callback is triggered after the menu is popped up/shown to the user, and provides
 * information about the actual position and size of the menu, as shown to the user.</p>
 *
 * <p>SWT clients can enable this functionality by launching their application with the
 * SWT_MENU_LOCATION_DEBUGGING environment variable set to 1. If enabled, the previously mentioned
 * positioning and size information will be printed to the console. The information comes from GTK
 * internals and is stored in the method parameters.</p>
 *
 * @param widget the memory address of the menu which was popped up
 * @param flipped_rect a pointer to the GdkRectangle containing the flipped location and size of the menu
 * @param final_rect a pointer to the GdkRectangle containing the final (after all internal adjustments)
 * location and size of the menu
 * @param flipped_x a boolean flag indicating whether the menu has been inverted along the X-axis
 * @param flipped_y a boolean flag indicating whether the menu has been inverted along the Y-axis
 */
long gtk_menu_popped_up (long widget, long flipped_rect, long final_rect, long flipped_x, long flipped_y) {
	return 0;
}

long gtk_mnemonic_activate (long widget, long arg1) {
	return 0;
}

long gtk_month_changed (long widget) {
	return 0;
}

long gtk_motion_notify_event (long widget, long event) {
	return 0;
}

long gtk_move_focus (long widget, long event) {
	return 0;
}

long gtk_output (long widget) {
	return 0;
}

long gtk_populate_popup (long widget, long menu) {
	return 0;
}

long gtk_popup_menu (long widget) {
	return 0;
}

long gtk_preedit_changed (long imcontext) {
	return 0;
}

long gtk_realize (long widget) {
	return 0;
}

long gtk_row_activated (long tree, long path, long column) {
	return 0;
	// Note on SWT Tree/Table/List. This signal is no longer used for sending events, instead
	// Send DefaultSelection is manually emitted. We use this function to know whether a
	// 'row-activated' is triggered. See Bug 312568, 518414.
}

long gtk_row_has_child_toggled (long model, long path, long iter) {
	return 0;
}

long gtk_scroll_child (long widget, long scrollType, long horizontal) {
	return 0;
}

long gtk_scroll_event (long widget, long event) {
	return 0;
}

long gtk_select (long item) {
	return 0;
}

long gtk_selection_done (long menushell) {
	return 0;
}

long gtk_show (long widget) {
	return 0;
}

long gtk3_show_help (long widget, long helpType) {
	return 0;
}

long gtk_size_allocate (long widget, long allocation) {
	return 0;
}

long gtk_status_icon_popup_menu (long handle, long button, long activate_time) {
	return 0;
}

long gtk_start_interactive_search (long widget) {
	return 0;
}

long gtk_style_updated (long widget) {
	return 0;
}

long gtk_switch_page (long notebook, long page, int page_num) {
	return 0;
}

long gtk_test_collapse_row (long tree, long iter, long path) {
	return 0;
}

long gtk_test_expand_row (long tree, long iter, long path) {
	return 0;
}

long gtk_text_buffer_insert_text (long widget, long iter, long text, long length) {
	return 0;
}

long gtk_timer () {
	return 0;
}

long gtk_toggled (long renderer, long pathStr) {
	return 0;
}

/*
 * Bug 498165: gtk_tree_view_column_cell_get_position() sets off rendererGetPreferredWidthCallback in GTK3 which is an issue
 * if there is an ongoing MeasureEvent listener. Disabling it and re-enabling the callback after the method is called
 * prevents a stack overflow from occurring.
 */
boolean gtk_tree_view_column_cell_get_position (long column, long cell_renderer, int[] start_pos, int[] width) {
	Callback.setEnabled(false);
	boolean result = GTK.gtk_tree_view_column_cell_get_position (column, cell_renderer, start_pos, width);
	Callback.setEnabled(true);
	return result;
}

long gtk_unmap (long widget) {
	return 0;
}

long gtk_unmap_event (long widget, long event) {
	return 0;
}

long gtk_unrealize (long widget) {
	return 0;
}

long gtk_value_changed(long range) {
	return 0;
}

long gtk_window_state_event (long widget, long event) {
	return 0;
}

int fontHeight (long font, long widgetHandle) {
	long context = GTK.gtk_widget_get_pango_context (widgetHandle);
	long lang = OS.pango_context_get_language (context);
	long metrics = OS.pango_context_get_metrics (context, font, lang);
	int ascent = OS.pango_font_metrics_get_ascent (metrics);
	int descent = OS.pango_font_metrics_get_descent (metrics);
	OS.pango_font_metrics_unref (metrics);
	return OS.PANGO_PIXELS (ascent + descent);
}

long filterProc(long xEvent, long gdkEvent, long data2) {
	return 0;
}

boolean filters (int eventType) {
	return display.filters (eventType);
}

char [] fixMnemonic (String string) {
	return fixMnemonic (string, true);
}

char [] fixMnemonic (String string, boolean replace) {
	return fixMnemonic (string, replace, false);
}

char [] fixMnemonic (String string, boolean replace, boolean removeAppended) {
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
				/*
				 * In Japanese like languages where mnemonics are not taken from the
				 * source label text but appended in parentheses like "(&M)" at end. In order to
				 * allow the reuse of such label text as a tool-tip text as well, "(&M)" like
				 * character sequence has to be removed from the end of CJK-style mnemonics.
				 */
			case '(':
				if (removeAppended && i + 4 == string.length () && text [i + 1] == '&' && text [i + 3] == ')') {
					if (replace) result [j++] = ' ';
					i += 4;
					break; // break switch case only if we are removing the mnemonic
				}
				else {
					// otherwise fall through (default case applies)
					result [j++] = text [i++];
					break;
				}
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
 * Returns <code>true</code> if the widget has auto text direction,
 * and <code>false</code> otherwise.
 *
 * @return <code>true</code> when the widget has auto direction and <code>false</code> otherwise
 *
 * @see SWT#AUTO_TEXT_DIRECTION
 *
 * @since 3.105
 */
public boolean isAutoDirection () {
	return false;
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
	if (handle != 0) {
		OS.g_signal_connect (handle, OS.dpi_changed, display.notifyProc, Widget.DPI_CHANGED);
	}
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

long hoverProc (long widget) {
	return 0;
}

boolean mnemonicHit (long mnemonicHandle, char key) {
	if (!mnemonicMatch (mnemonicHandle, key)) return false;
	OS.g_signal_handlers_block_matched (mnemonicHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, MNEMONIC_ACTIVATE);
	boolean result = GTK.gtk_widget_mnemonic_activate (mnemonicHandle, false);
	OS.g_signal_handlers_unblock_matched (mnemonicHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, MNEMONIC_ACTIVATE);
	return result;
}

boolean mnemonicMatch (long mnemonicHandle, char key) {
	long keyval1 = GDK.gdk_keyval_to_lower (GDK.gdk_unicode_to_keyval (key));
	long keyval2 = GDK.gdk_keyval_to_lower (GTK.gtk_label_get_mnemonic_keyval (mnemonicHandle));
	return keyval1 == keyval2;
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
	try (ExceptionStash exceptions = new ExceptionStash ()) {
		if ((state & DISPOSE_SENT) == 0) {
			state |= DISPOSE_SENT;
			try {
				sendEvent (SWT.Dispose);
			} catch (Error | RuntimeException ex) {
				exceptions.stash (ex);
			}
		}
		if ((state & DISPOSED) == 0) {
			try {
				releaseChildren (destroy);
			} catch (Error | RuntimeException ex) {
				exceptions.stash (ex);
			}
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
		notifyDisposalTracker();
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
 * @nooverride This method is not intended to be re-implemented or extended by clients.
 */
protected void removeListener (int eventType, SWTEventListener handler) {
	checkWidget ();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

long rendererGetPreferredWidthProc (long cell, long handle, long minimun_size, long natural_size) {
	return 0;
}

long rendererRenderProc (long cell, long cr, long handle, long background_area, long cell_area, long flags) {
	return 0;
}

long rendererSnapshotProc (long cell, long snapshot, long handle, long background_area, long cell_area, long flags) {
	return 0;
}

/**
 * Marks the widget to be skinned.
 * <p>
 * The skin event is sent to the receiver's display when appropriate (usually before the next event
 * is handled). Widgets are automatically marked for skinning upon creation as well as when its skin
 * id or class changes. The skin id and/or class can be changed by calling {@link Display#setData(String, Object)}
 * with the keys {@link SWT#SKIN_ID} and/or {@link SWT#SKIN_CLASS}. Once the skin event is sent to a widget, it
 * will not be sent again unless <code>reskin(int)</code> is called on the widget or on an ancestor
 * while specifying the <code>SWT.ALL</code> flag.
 * </p>
 * <p>
 * The parameter <code>flags</code> may be either:
 * </p>
 * <dl>
 * <dt><b>{@link SWT#ALL}</b></dt>
 * <dd>all children in the receiver's widget tree should be skinned</dd>
 * <dt><b>{@link SWT#NONE}</b></dt>
 * <dd>only the receiver should be skinned</dd>
 * </dl>
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
		if (eventTable != null) display.sendEvent(eventTable, event);
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
	if (event == null) {
		event = new Event();
	}
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

boolean sendKeyEvent (int type, long event) {
	int length = 0;
	long string = 0;
	if (GTK.GTK4) {
		/* TODO: GTK4 no access to key event string */
	} else {
		GdkEventKey gdkEvent = new GdkEventKey ();
		GTK3.memmove(gdkEvent, event, GdkEventKey.sizeof);
		length = gdkEvent.length;
		string = gdkEvent.string;
	}

	if (string == 0 || OS.g_utf16_strlen (string, length) <= 1) {
		Event javaEvent = new Event ();
		javaEvent.time = GDK.gdk_event_get_time(event);
		if (!setKeyState (javaEvent, event)) return true;
		sendEvent (type, javaEvent);
		// widget could be disposed at this point

		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning false.
		*/
		if (isDisposed ()) return false;
		return javaEvent.doit;
	}
	byte [] buffer = new byte [length];
	C.memmove (buffer, string, length);
	char [] chars = Converter.mbcsToWcs (buffer);
	return sendIMKeyEvent (type, event, chars) != null;
}

char [] sendIMKeyEvent (int type, long event, char [] chars) {
	int index = 0, count = 0, state = 0;
	long ptr = 0;
	if (event == 0) {
		long controller = Control.getControl(this.handle).keyController;
		ptr = GTK.GTK4 ? GTK4.gtk_event_controller_get_current_event(controller):GTK3.gtk_get_current_event ();
		if (ptr != 0) {
			int eventType = GDK.gdk_event_get_event_type(ptr);
			eventType = Control.fixGdkEventTypeValues(eventType);
			switch (eventType) {
				case GDK.GDK_KEY_PRESS:
				case GDK.GDK_KEY_RELEASE:
					int [] eventState = new int[1];
					if (GTK.GTK4) {
						eventState[0] = GDK.gdk_event_get_modifier_state(event);
					} else {
						GDK.gdk_event_get_state(event, eventState);
					}
					state = eventState[0];
					break;
				default:
					event = 0;
					break;
			}
		} else {
			if(GTK.GTK4) {
				state = GTK4.gtk_event_controller_get_current_event_state(controller);
			} else {
				int [] buffer = new int [1];
				GTK3.gtk_get_current_event_state (buffer);
				state = buffer [0];
			}
		}
	} else {
		ptr = event;
	}
	while (index < chars.length) {
		Event javaEvent = new Event ();
		if (ptr != 0 && chars.length <= 1) {
			setKeyState (javaEvent, ptr);
		} else {
			setInputState (javaEvent, state);
		}
		javaEvent.character = chars [index];
		sendEvent (type, javaEvent);

		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning null.
		*/
		if (isDisposed ()) {
			if (ptr != 0 && ptr != event && !GTK.GTK4) gdk_event_free (ptr);
			return null;
		}
		if (javaEvent.doit) chars [count++] = chars [index];
		index++;
	}
	if (ptr != 0 && ptr != event && !GTK.GTK4) gdk_event_free (ptr);
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
	long ptr = GTK.GTK4 ? 0 : GTK3.gtk_get_current_event ();
	if (ptr != 0) {
		int currentEventType = GDK.gdk_event_get_event_type(ptr);
		currentEventType = Control.fixGdkEventTypeValues(currentEventType);
		switch (currentEventType) {
			case GDK.GDK_BUTTON_PRESS:
			case GDK.GDK_2BUTTON_PRESS:
			case GDK.GDK_BUTTON_RELEASE: {
				int [] eventButton = new int [1];
				if (GTK.GTK4) {
					eventButton[0] = GDK.gdk_button_event_get_button(ptr);
				} else {
					GDK.gdk_event_get_button(ptr, eventButton);
				}

				setButtonState(event, eventButton [0]);
			}
			//$FALL-THROUGH$
			case GDK.GDK_KEY_PRESS:
			case GDK.GDK_KEY_RELEASE: {
				int [] state = new int[1];
				if (GTK.GTK4) {
					state[0] = GDK.gdk_event_get_modifier_state(ptr);
				} else {
					GDK.gdk_event_get_state(ptr, state);
				}
				setInputState (event, state [0]);
				break;
			}
		}
		gdk_event_free (ptr);
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
	if (key.equals(KEY_GTK_CSS) && value instanceof String) {
		long context = GTK.gtk_widget_get_style_context (cssHandle());
		long provider = GTK.gtk_css_provider_new();
		if (context != 0 && provider != 0) {
			GTK.gtk_style_context_add_provider (context, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_USER);
			if (GTK.GTK4) {
				GTK4.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs ((String) value, true), -1);
			} else {
				GTK3.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs ((String) value, true), -1, null);
			}
			OS.g_object_unref (provider);
		}
	}
}

/**
 * @param fontDescription Font description in the form of
 *                        <code>PangoFontDescription*</code>. This pointer
 *                        will never be used by GTK after calling this
 *                        function, so it's safe to free it as soon as the
 *                        function completes.
 */
void setFontDescription(long widget, long fontDescription) {
	if (GTK.GTK4) {
		long styleContext = GTK.gtk_widget_get_style_context(widget);
		long provider = GTK.gtk_css_provider_new();
		GTK.gtk_style_context_add_provider(styleContext, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		OS.g_object_unref(provider);

		String css = convertPangoFontDescriptionToCss(fontDescription);
		GTK4.gtk_css_provider_load_from_data(provider, Converter.javaStringToCString(css), -1);
	} else {
		// gtk_widget_override_font() copies the fields from 'fontDescription'
		// and does not remember the pointer passed to it.
		GTK3.gtk_widget_override_font(widget, fontDescription);
		long context = GTK.gtk_widget_get_style_context(widget);
		GTK3.gtk_style_context_invalidate(context);
	}
}

String convertPangoFontDescriptionToCss(long fontDescription) {
	String css = "* { ";
	int fontMask = OS.pango_font_description_get_set_fields(fontDescription);

	if ((fontMask & OS.PANGO_FONT_MASK_FAMILY) != 0) {
		long fontFamily = OS.pango_font_description_get_family(fontDescription);
		css += "font-family: \"" + Converter.cCharPtrToJavaString(fontFamily, false) + "\";";
	}

	if ((fontMask & OS.PANGO_FONT_MASK_WEIGHT) != 0) {
		int fontWeight = OS.pango_font_description_get_weight(fontDescription);

		String weightString = fontWeight < OS.PANGO_WEIGHT_BOLD ? "normal" : "bold";
		css += "font-weight: " + weightString + ";";
	}

	if ((fontMask & OS.PANGO_FONT_MASK_STYLE) != 0) {
		int fontStyle = OS.pango_font_description_get_style(fontDescription);

		String styleString;
		switch (fontStyle) {
			case OS.PANGO_STYLE_NORMAL:
				styleString = "normal";
				break;
			case OS.PANGO_STYLE_ITALIC:
				styleString = "italic";
				break;
			default:
				styleString = "";
				break;
		}

		css += "font-style: " + styleString + ";";
	}

	if ((fontMask & OS.PANGO_FONT_MASK_SIZE) != 0) {
		int fontSize = OS.pango_font_description_get_size(fontDescription);
		css += "font-size: " + fontSize / OS.PANGO_SCALE + "pt;";
	}

	css += " } ";

	return css;
}

void setButtonState (Event event, int eventButton) {
	switch (eventButton) {
	case 1: event.stateMask |= SWT.BUTTON1; break;
	case 2: event.stateMask |= SWT.BUTTON2; break;
	case 3: event.stateMask |= SWT.BUTTON3; break;
	case 4: event.stateMask |= SWT.BUTTON4; break;
	case 5: event.stateMask |= SWT.BUTTON5; break;
	default:
	}
}

boolean setInputState (Event event, int state) {
	if ((state & GDK.GDK_MOD1_MASK) != 0) event.stateMask |= SWT.ALT;
	if ((state & GDK.GDK_SHIFT_MASK) != 0) event.stateMask |= SWT.SHIFT;
	if ((state & GDK.GDK_CONTROL_MASK) != 0) event.stateMask |= SWT.CONTROL;
	if ((state & GDK.GDK_BUTTON1_MASK) != 0) event.stateMask |= SWT.BUTTON1;
	if ((state & GDK.GDK_BUTTON2_MASK) != 0) event.stateMask |= SWT.BUTTON2;
	if ((state & GDK.GDK_BUTTON3_MASK) != 0) event.stateMask |= SWT.BUTTON3;
	return true;
}

/**
 * On Linux, the most common way to handle keyboard input is XKB.
 * The rest of the description explains XKB and related GTK stuff.
 *
 * XKB uses the following definitions:
 * <ul>
 *  <li>"group" - that's how they call keyboard layouts</li>
 *  <li>"keycode" - id of a physical key on a keyboard. For example,
 *   AB01 refers to 2nd row from the bottom (B), 1st key (01) </li>
 *  <li>"level" - Can be seen as a number that describes modifiers
 *   pressed together with the key. For example, in English US,
 *   pressing A would result in level 0, and pressing Shift+A would
 *   result in level 1. The other common levels are used for AltGr
 *   and Shift+AltGr, but a keyboard layout could have even more
 *   exotic levels.</li>
 *  <li>"modifiers" - a combination of modifier keys. Keyboard
 *   layouts could define their own modifiers.</li>
 *  <li>"keyval" - Can be seen as a final calculation of what was
 *   produced by a key press (by taking keycode, group, level, and
 *   other modifiers such as dead keys into account).
 * </ul>
 * Some examples:
 * <table>
 *  <tr><th>Layout</th><th>Key row</th><th>Key col</th><th>Keycode</th><th>Modifiers</th><th>Keyval</th><th>Character</th></tr>
 *  <tr><td>English US    </td><td>1</td><td>10</td><td>FK09</td><td></td><td>F9</td><td></td></tr>
 *  <tr><td>English US    </td><td>3</td><td>5</td><td>AD04</td><td></td><td>r</td><td>r</td></tr>
 *  <tr><td>English US    </td><td>3</td><td>5</td><td>AD04</td><td>Shift</td><td>R</td><td>R</td></tr>
 *  <tr><td>English US    </td><td>3</td><td>6</td><td>AD05</td><td></td><td>t</td><td>t</td></tr>
 *  <tr><td>English Dvorak</td><td>3</td><td>5</td><td>AD04</td><td></td><td>p</td><td>p</td></tr>
 *  <tr><td>English Dvorak</td><td>3</td><td>6</td><td>AD05</td><td></td><td>y</td><td>y</td></tr>
 *  <tr><td>Bulgarian     </td><td>3</td><td>5</td><td>AD04</td><td></td><td>Cyrillic_i</td><td>и</td></tr>
 *  <tr><td>Bulgarian     </td><td>3</td><td>6</td><td>AD05</td><td></td><td>Cyrillic_sha</td><td>ш</td></tr>
 * </table><br>
 *
 * XKB doesn't do two-step keyboard layout translation like Windows.
 * For this reason, binding keyboard shortcuts across keyboard layouts
 * quickly becomes ugly. Further, each major UI library (such as Qt
 * or GTK) and many major softwares (such as Firefox, LibreOffice)
 * has its own approach. Usually developed through trial, error and
 * pain.
 *
 * The common approach is to search all installed keyboard layouts
 * and find some that is latin. Then invoke keyboard shortcut using
 * that layout. That is, if current layout is Bulgarian:<br>
 * <ul>
 *  <li>notice that current layout is not latin</li>
 *  <li>search installed layouts to find a latin one</li>
 *  <li>map pressed key to a latin char using that layout</li>
 *  <li>invoke keyboard shortcut</li>
 * </ul>
 *
 * The details of how it's done differ across libraries and
 * softwares. For example:
 * <ul>
 *  <li>If currently pressed key produces latin keyval, some are
 *   happy with that and will not search for other layouts. Others
 *   do search anyway. This often results in multiple keys invoking
 *   the same shortcut (when there are multiple layouts with
 *   desired latin key in different positions). One example of
 *   affected software is 'gedit'.</li>
 *  <li>When they do search, some search all layouts, others
 *   search only the previous layouts (and insist that latin layout
 *   is installed before non-latin), some search for a first match,
 *   some search for the "most latin" layout, etc.</li>
 * </ul>
 * To my understanding, no matter which of these dark magics is
 * chosen, this always results in ugly corner cases, band-aided
 * with dirty workarounds which fix something while breaking
 * something else.
 *
 * For an example of how other software deals with all that mess, see
 * QXcbKeyboard::lookupLatinKeysym() in Qt.
 */
boolean setKeyState (Event javaEvent, long event) {
	long string = 0;
	int length = 0;
	int group;

	int [] eventKeyval = new int [1];
	int [] eventState = new int [1];
	if (GTK.GTK4) {
		eventKeyval[0] = GDK.gdk_key_event_get_keyval(event);
		eventState[0] = GDK.gdk_event_get_modifier_state(event);
	} else {
		GDK.gdk_event_get_keyval(event, eventKeyval);
		GDK.gdk_event_get_state(event, eventState);
	}

	if (GTK.GTK4) {
		group = GDK.gdk_key_event_get_layout(event);
	} else {
		GdkEventKey gdkEvent = new GdkEventKey ();
		GTK3.memmove(gdkEvent, event, GdkEventKey.sizeof);
		length = gdkEvent.length;
		string = gdkEvent.string;
		group = gdkEvent.group;
	}

	if (string != 0 && OS.g_utf16_strlen (string, length) > 1) return false;
	boolean isNull = false;
	javaEvent.keyCode = Display.translateKey (eventKeyval[0]);
	switch (eventKeyval[0]) {
		case GDK.GDK_BackSpace:		javaEvent.character = SWT.BS; break;
		case GDK.GDK_Linefeed:		javaEvent.character = SWT.LF; break;
		case GDK.GDK_KP_Enter:
		case GDK.GDK_Return: 		javaEvent.character = SWT.CR; break;
		case GDK.GDK_KP_Delete:
		case GDK.GDK_Delete:			javaEvent.character = SWT.DEL; break;
		case GDK.GDK_Escape:			javaEvent.character = SWT.ESC; break;
		case GDK.GDK_Tab:
		case GDK.GDK_ISO_Left_Tab: 	javaEvent.character = SWT.TAB; break;
		default: {
			if (javaEvent.keyCode == 0) {
				int [] keyval = new int [1];
				int [] effective_group = new int [1], level = new int [1], consumed_modifiers = new int [1];
				/* If current group is not a Latin layout, get the most Latin Layout group from input source. */
				Map<Integer, Integer> groupLatinKeysCount = display.getGroupKeysCount();
				if (!groupLatinKeysCount.containsKey(group)) {
					group = display.getLatinKeyGroup();
				}

				long keymap = 0;
				long display = GDK.gdk_display_get_default();
				if (GTK.GTK4) {
					//TODO: GTK4 Get keymap or find alternative for gdk_keymap_translate_keyboard_state (no longer exist in GTK4)
				} else {
					keymap = GDK.gdk_keymap_get_for_display(display);
				}

				short [] keyCode = new short [1];
				if (GTK.GTK4) {
					keyval[0] = (short) GDK.gdk_key_event_get_keyval(event);
					javaEvent.keyCode = (int) GDK.gdk_keyval_to_unicode (keyval [0]);
				} else {
					GDK.gdk_event_get_keycode(event, keyCode);
					if (GDK.gdk_keymap_translate_keyboard_state (keymap, keyCode[0],
							0, group, keyval, effective_group, level, consumed_modifiers)) {
						javaEvent.keyCode = (int) GDK.gdk_keyval_to_unicode (keyval [0]);
					}
				}
			}
			int key = eventKeyval[0];
			if ((eventState[0] & GDK.GDK_CONTROL_MASK) != 0 && (0 <= key && key <= 0x7F)) {
				if ('a'  <= key && key <= 'z') key -= 'a' - 'A';
				if (64 <= key && key <= 95) key -= 64;
				javaEvent.character = (char) key;
				isNull = eventKeyval[0] == '@' && key == 0;
			} else {
				javaEvent.character = (char) GDK.gdk_keyval_to_unicode (key);
			}
		}
	}
	setLocationState (javaEvent, event);
	if (javaEvent.keyCode == 0 && javaEvent.character == 0) {
		if (!isNull) return false;
	}
	return setInputState (javaEvent, eventState[0]);
}

void setLocationState (Event event, long eventPtr) {
	int [] eventKeyval = new int[1];
	if (GTK.GTK4) {
		eventKeyval[0] = GDK.gdk_key_event_get_keyval(eventPtr);
	} else {
		GDK.gdk_event_get_keyval(eventPtr, eventKeyval);
	}

	switch (eventKeyval[0]) {
		case GDK.GDK_Alt_L:
		case GDK.GDK_Shift_L:
		case GDK.GDK_Control_L:
			event.keyLocation = SWT.LEFT;
			break;
		case GDK.GDK_Alt_R:
		case GDK.GDK_Shift_R:
		case GDK.GDK_Control_R:
				event.keyLocation = SWT.RIGHT;
			break;
		case GDK.GDK_KP_0:
		case GDK.GDK_KP_1:
		case GDK.GDK_KP_2:
		case GDK.GDK_KP_3:
		case GDK.GDK_KP_4:
		case GDK.GDK_KP_5:
		case GDK.GDK_KP_6:
		case GDK.GDK_KP_7:
		case GDK.GDK_KP_8:
		case GDK.GDK_KP_9:
		case GDK.GDK_KP_Add:
		case GDK.GDK_KP_Decimal:
		case GDK.GDK_KP_Delete:
		case GDK.GDK_KP_Divide:
		case GDK.GDK_KP_Down:
		case GDK.GDK_KP_End:
		case GDK.GDK_KP_Enter:
		case GDK.GDK_KP_Equal:
		case GDK.GDK_KP_Home:
		case GDK.GDK_KP_Insert:
		case GDK.GDK_KP_Left:
		case GDK.GDK_KP_Multiply:
		case GDK.GDK_KP_Page_Down:
		case GDK.GDK_KP_Page_Up:
		case GDK.GDK_KP_Right:
		case GDK.GDK_KP_Subtract:
		case GDK.GDK_KP_Up:
		case GDK.GDK_Num_Lock:
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

long shellMapProc (long handle, long arg0, long user_data) {
	return 0;
}

long sizeAllocateProc (long handle, long arg0, long user_data) {
	return 0;
}

long sizeRequestProc (long handle, long arg0, long user_data) {
	return 0;
}

/**
 * Converts an incoming snapshot into a gtk_draw() call, complete with
 * a Cairo context.
 *
 * @param handle the widget receiving the snapshot
 * @param snapshot the actual GtkSnapshot
 */
void snapshotToDraw (long handle, long snapshot) {
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation(handle, allocation);
	long rect = Graphene.graphene_rect_alloc();
	Graphene.graphene_rect_init(rect, 0, 0, allocation.width, allocation.height);

	long cairo = GTK4.gtk_snapshot_append_cairo(snapshot, rect);
	if (cairo != 0) {
		Rectangle bounds = new Rectangle(0, 0, allocation.width, allocation.height);
		gtk4_draw(handle, cairo, bounds);
	}

	Graphene.graphene_rect_free(rect);
}

long gtk_widget_get_window (long widget){
	GTK.gtk_widget_realize(widget);
	return GTK3.gtk_widget_get_window (widget);
}

long gtk_widget_get_surface (long widget){
	GTK.gtk_widget_realize(widget);
	return GTK4.gtk_native_get_surface(GTK4.gtk_widget_get_native (widget));
}

void gdk_window_get_size (long drawable, int[] width, int[] height) {
	width[0] = GDK.gdk_window_get_width (drawable);
	height[0] = GDK.gdk_window_get_height (drawable);
}

void gdk_surface_get_size (long surface, int[] width, int[] height) {
	width[0] = GDK.gdk_surface_get_width (surface);
	height[0] = GDK.gdk_surface_get_height (surface);
}

/**
 * GTK4 does not hand out copies of events anymore, only references.
 * Call gdk_event_free() on GTK3 and g_object_unref() on GTK4.
 *
 * @param event the event to be freed
 */
void gdk_event_free (long event) {
	if (event == 0) return;
	if (GTK.GTK4) {
		GDK.gdk_event_unref(event);
	} else {
		GDK.gdk_event_free(event);
	}
}

/**
 * Wrapper function for gdk_event_get_surface() on GTK4,
 * and gdk_event_get_window() on GTK3.
 *
 * @param event the event whose window or surface to fetch
 * @return the GdkWindow or GdkSurface associated with the event
 */
long gdk_event_get_surface_or_window(long event) {
	if (event == 0) return 0;
	if (GTK.GTK4) {
		return GDK.gdk_event_get_surface(event);
	} else {
		return GDK.gdk_event_get_window(event);
	}
}

/**
 * Wrapper function for gdk_event_get_state()
 * @param event   pointer to the GdkEvent.
 * @return the keymask to be used with constants like
 *        OS.GDK_SHIFT_MASK / OS.GDK_CONTROL_MASK / OS.GDK_MOD1_MASK etc..
 */
int gdk_event_get_state (long event) {
	int [] state = new int[1];
	if (GTK.GTK4) {
		state[0] = GDK.gdk_event_get_modifier_state(event);
	} else {
		GDK.gdk_event_get_state(event, state);
	}

	return state[0];
}


long gtk_box_new (int orientation, boolean homogeneous, int spacing) {
	long box = GTK.gtk_box_new (orientation, spacing);
	GTK.gtk_box_set_homogeneous (box, homogeneous);
	return box;
}

void gtk_box_set_child_packing (long box, long child, boolean expand, boolean fill, int padding, int pack_type) {
	if (GTK.GTK4) {
		GTK.gtk_widget_set_hexpand(child, expand);
		GTK.gtk_widget_set_vexpand(child, expand);
		if (fill) {
			GTK.gtk_widget_set_halign(child, GTK.GTK_ALIGN_FILL);
			GTK.gtk_widget_set_valign(child, GTK.GTK_ALIGN_FILL);
		}
	} else {
		GTK3.gtk_box_set_child_packing(box, child, expand, fill, padding, pack_type);
	}
}

void gtk_box_pack_end (long box, long child, boolean expand, boolean fill, int padding) {
	if (GTK.GTK4) {
		GTK.gtk_widget_set_hexpand(child, expand);
		GTK.gtk_widget_set_vexpand(child, expand);
		if (fill) {
			GTK.gtk_widget_set_halign(child, GTK.GTK_ALIGN_FILL);
			GTK.gtk_widget_set_valign(child, GTK.GTK_ALIGN_FILL);
		}
		GTK4.gtk_box_append(box, child);
	} else {
		GTK3.gtk_box_pack_end(box, child, expand, fill, padding);
	}
}

int gdk_pointer_grab (long gdkResource, int grab_ownership, boolean owner_events, int event_mask, long confine_to, long cursor, int time_) {
	long display = 0;
	if (GTK.GTK4) {
		if( gdkResource != 0) {
			display = GDK.gdk_surface_get_display (gdkResource);
		}
	} else {
		if( gdkResource != 0) {
			display = GDK.gdk_window_get_display (gdkResource);
		} else {
			gdkResource = GDK.gdk_get_default_root_window ();
			display = GDK.gdk_window_get_display (gdkResource);
		}
	}
	long seat = GDK.gdk_display_get_default_seat(display);
	if (gdkSeatGrabPrepareFunc == null) {
		gdkSeatGrabPrepareFunc = new Callback(Widget.class, "GdkSeatGrabPrepareFunc", 3); //$NON-NLS-1$
	}
	return GDK.gdk_seat_grab(seat, gdkResource, GDK.GDK_SEAT_CAPABILITY_ALL_POINTING, owner_events, cursor, 0, gdkSeatGrabPrepareFunc.getAddress(), gdkResource);
}

void gdk_pointer_ungrab (long gdkResource, int time_) {
	long display = GTK.GTK4? GDK.gdk_surface_get_display(gdkResource) : GDK.gdk_window_get_display (gdkResource);
	long seat = GDK.gdk_display_get_default_seat(display);
	GDK.gdk_seat_ungrab(seat);
}

static long GdkSeatGrabPrepareFunc (long gdkSeat, long gdkResource, long userData_gdkResource) {
	if (userData_gdkResource != 0) {
		if (GTK.GTK4) {
			/* TODO: GTK does not provide a gdk_surface_show, probably will require use of the present api */
		} else {
			GDK.gdk_window_show(userData_gdkResource);
		}
	}
	return 0;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	String string = "*Disposed*";
	if (!isDisposed ()) {
		string = "*Wrong Thread*";
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}";
}

long topHandle () {
	return handle;
}

long timerProc (long widget) {
	return 0;
}

boolean translateTraversal (int event) {
	return false;
}

void enterMotionProc(long controller, double x, double y, long user_data) {
	long event = GTK4.gtk_event_controller_get_current_event(controller);

	switch ((int)user_data) {
		case ENTER:
			// Possible bug in GTK4, event = 0, therefore unable to access event information
			gtk4_enter_event(controller, x, y, event);
			break;
		case MOTION:
			gtk4_motion_event(controller, x, y, event);
			break;
		case MOTION_INVERSE:
			break;
	}
}

boolean scrollProc(long controller, double dx, double dy, long user_data) {
	long event = GTK4.gtk_event_controller_get_current_event(controller);

	switch ((int)user_data) {
		case SCROLL:
			return gtk4_scroll_event(controller, dx, dy, event);
	}

	return false;
}

void focusProc(long controller, long user_data) {
	long event = GTK4.gtk_event_controller_get_current_event(controller);

	switch ((int)user_data) {
		case FOCUS_IN:
			gtk4_focus_enter_event(controller, event);
			break;
		case FOCUS_OUT:
			gtk4_focus_leave_event(controller, event);
			break;
	}
}

void windowActiveProc(long handle, long user_data) {
	long eventType = GTK.gtk_window_is_active(handle) ? SWT.FocusIn:SWT.FocusOut;

	gtk4_focus_window_event(handle, eventType);
}

boolean keyPressReleaseProc(long controller, int keyval, int keycode, int state, long user_data) {
	long event = GTK4.gtk_event_controller_get_current_event(controller);

	switch ((int)user_data) {
		case KEY_PRESSED:
			return gtk4_key_press_event(controller, keyval, keycode, state, event);
		case KEY_RELEASED:
			gtk4_key_release_event(controller, keyval, keycode, state, event);
			break;
	}

	return false;
}

void gesturePressReleaseProc(long gesture, int n_press, double x, double y, long user_data) {
	long event = GTK4.gtk_event_controller_get_current_event(gesture);

	switch ((int)user_data) {
		case GESTURE_PRESSED:
			gtk_gesture_press_event(gesture, n_press, x, y, event);
			break;
		case GESTURE_RELEASED:
			gtk_gesture_release_event(gesture, n_press, x, y, event);
			break;
	}
}

void leaveProc(long controller, long handle, long user_data) {
	long event = GTK4.gtk_event_controller_get_current_event(controller);

	switch ((int)user_data) {
		case LEAVE:
			// Possible bug in GTK4, event = 0, therefore unable to access event information
			gtk4_leave_event(controller, event);
			break;
	}
}

long notifyProc (long object, long arg0, long user_data) {
	switch ((int)user_data) {
		case DPI_CHANGED: return dpiChanged(object, arg0);
		case NOTIFY_STATE: return notifyState(object, arg0);
		case NOTIFY_DEFAULT_HEIGHT:
		case NOTIFY_DEFAULT_WIDTH:
		case NOTIFY_MAXIMIZED:
			return gtk_size_allocate(object, 0);
	}
	return 0;
}

long notifyState (long object, long argo0) {
	return 0;
}

long windowProc (long handle, long user_data) {
	switch ((int)user_data) {
		case ACTIVATE: return gtk_activate (handle);
		case CHANGED: return gtk_changed (handle);
		case CLICKED: return gtk_clicked (handle);
		case CLOSE_REQUEST: return gtk_close_request (handle);
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
		case STYLE_UPDATED: return gtk_style_updated (handle);
		case SELECT: return gtk_select (handle);
		case SELECTION_DONE: return gtk_selection_done (handle);
		case SHOW: return gtk_show (handle);
		case VALUE_CHANGED: return gtk_value_changed(handle);
		case UNMAP: return gtk_unmap (handle);
		case UNREALIZE: return gtk_unrealize (handle);
		default: return 0;
	}
}

long windowProc (long handle, long arg0, long user_data) {
	switch ((int)user_data) {
		case EXPOSE_EVENT_INVERSE: {
			if (GTK.GTK_IS_CONTAINER (handle)) {
				return gtk_draw (handle, arg0);
			}
			return 0;
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
		case EVENT_AFTER: return gtk_event_after (handle, arg0);
		case EXPOSE_EVENT: {
			if (!GTK.GTK_IS_CONTAINER (handle)) {
				return gtk_draw (handle, arg0);
			}
			return 0;
		}
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
		case SHOW_HELP: return gtk3_show_help(handle, arg0);
		case SIZE_ALLOCATE: return gtk_size_allocate (handle, arg0);
		case TOGGLED: return gtk_toggled (handle, arg0);
		case UNMAP_EVENT: return gtk_unmap_event (handle, arg0);
		case WINDOW_STATE_EVENT: return gtk_window_state_event (handle, arg0);
		default: return 0;
	}
}

long windowProc (long handle, long arg0, long arg1, long user_data) {
	switch ((int)user_data) {
		case DELETE_RANGE: return gtk_delete_range (handle, arg0, arg1);
		case DELETE_TEXT: return gtk_delete_text (handle, arg0, arg1);
		case ICON_RELEASE: return gtk_icon_release (handle, arg0, arg1);
		case ROW_ACTIVATED: return gtk_row_activated (handle, arg0, arg1);
		case SCROLL_CHILD: return gtk_scroll_child (handle, arg0, arg1);
		case STATUS_ICON_POPUP_MENU: return gtk_status_icon_popup_menu (handle, arg0, arg1);
		case SWITCH_PAGE: return gtk_switch_page(handle, arg0, (int)arg1);
		case TEST_COLLAPSE_ROW: return gtk_test_collapse_row (handle, arg0, arg1);
		case TEST_EXPAND_ROW: return gtk_test_expand_row(handle, arg0, arg1);
		case ROW_HAS_CHILD_TOGGLED: return gtk_row_has_child_toggled(handle, arg0, arg1);
		default: return 0;
	}
}

long windowProc (long handle, long arg0, long arg1, long arg2, long user_data) {
	switch ((int)user_data) {
		case EXPAND_COLLAPSE_CURSOR_ROW: return gtk_expand_collapse_cursor_row (handle, arg0, arg1, arg2);
		case INSERT_TEXT: return gtk_insert_text (handle, arg0, arg1, arg2);
		case TEXT_BUFFER_INSERT_TEXT: return gtk_text_buffer_insert_text (handle, arg0, arg1, arg2);
		default: return 0;
	}
}

long windowProc (long handle, long arg0, long arg1, long arg2, long arg3, long user_data) {
	switch ((int)user_data) {
		case POPPED_UP: return gtk_menu_popped_up (handle, arg0, arg1, arg2, arg3);
		default: return 0;
	}
}

void gtk_cell_renderer_get_preferred_size (long cell, long widget,  int[] width, int[] height) {
	GtkRequisition minimum_size = new GtkRequisition ();
	GTK.gtk_cell_renderer_get_preferred_size (cell, widget, minimum_size, null);
	if (width != null) width [0] = minimum_size.width;
	if (height != null) height[0] = minimum_size.height;
}

void gtk_widget_get_preferred_size (long widget, GtkRequisition requisition){
	GTK.gtk_widget_get_preferred_size (widget, requisition, null);
}

/**
 * Retrieves the amount of space around the outside of the container.
 * On GTK3: this is done using gtk_container_get_border_width.
 * On GTK4: this is done by returning the max margin on any side.
 * @return amount of space around the outside of the container.
 */
int gtk_container_get_border_width_or_margin (long handle) {
	if (GTK.GTK4) {
		int marginTop = GTK.gtk_widget_get_margin_top(handle);
		int marginBottom = GTK.gtk_widget_get_margin_bottom(handle);
		int marginStart = GTK.gtk_widget_get_margin_start(handle);
		int marginEnd = GTK.gtk_widget_get_margin_end(handle);
		return Math.max(Math.max(marginTop, marginBottom), Math.max(marginStart, marginEnd));
	} else {
		return GTK3.gtk_container_get_border_width(handle);
	}
}
/**
 * Sets the border width of the container to all sides of the container.
 */
void gtk_container_set_border_width (long handle, int border_width) {
	if (GTK.GTK4) {
		GTK.gtk_widget_set_margin_top(handle, border_width);
		GTK.gtk_widget_set_margin_bottom(handle, border_width);
		GTK.gtk_widget_set_margin_start(handle, border_width);
		GTK.gtk_widget_set_margin_end(handle, border_width);
	} else {
		GTK3.gtk_container_set_border_width (handle, border_width);
	}
}

void setToolTipText(long tipWidget, String string) {
	byte[] buffer = null;
	if (string != null && !string.isEmpty()) {
		char[] chars = fixMnemonic(string, false, true);
		buffer = Converter.wcsToMbcs(chars, true);
	}

	GTK.gtk_widget_set_tooltip_text(tipWidget, buffer);
}

void gtk_widget_size_allocate (long widget, GtkAllocation allocation, int baseline) {
	if (GTK.GTK4) {
		GTK4.gtk_widget_size_allocate(widget, allocation, baseline);
	} else {
		GTK3.gtk_widget_size_allocate(widget, allocation);
	}
}

void notifyCreationTracker() {
	if (WidgetSpy.isEnabled) {
		WidgetSpy.getInstance().widgetCreated(this);
	}
}

void notifyDisposalTracker() {
	if (WidgetSpy.isEnabled) {
		WidgetSpy.getInstance().widgetDisposed(this);
	}
}

}

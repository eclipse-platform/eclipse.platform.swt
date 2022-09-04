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
 *     Pierre-Yves B., pyvesdev@gmail.com - Bug 219750: [styled text] Typing ~~ inserts é~~
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

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
	int style, state;
	Display display;
	EventTable eventTable;
	Object data;

	/* Global state flags */
	static final int DISPOSED		= 1<<0;
	static final int CANVAS			= 1<<1;
	static final int KEYED_DATA		= 1<<2;
	static final int DISABLED		= 1<<3;
	static final int HIDDEN			= 1<<4;

	/* A layout was requested on this widget */
	static final int LAYOUT_NEEDED	= 1<<5;

	/* The preferred size of a child has changed */
	static final int LAYOUT_CHANGED = 1<<6;

	/* A layout was requested in this widget hierarchy */
	static final int LAYOUT_CHILD = 1<<7;

	/* Background flags */
	static final int THEME_BACKGROUND = 1<<8;
	static final int DRAW_BACKGROUND = 1<<9;
	static final int PARENT_BACKGROUND = 1<<10;

	/* Dispose and release flags */
	static final int RELEASED		= 1<<11;
	static final int DISPOSE_SENT	= 1<<12;

	/* More global widget state flags */
	static final int TRACK_MOUSE	= 1<<13;
	static final int FOREIGN_HANDLE	= 1<<14;
	static final int DRAG_DETECT	= 1<<15;

	/* Move and resize state flags */
	static final int MOVE_OCCURRED		= 1<<16;
	static final int MOVE_DEFERRED		= 1<<17;
	static final int RESIZE_OCCURRED	= 1<<18;
	static final int RESIZE_DEFERRED	= 1<<19;

	/* Ignore WM_CHANGEUISTATE */
	static final int IGNORE_WM_CHANGEUISTATE = 1<<20;

	/* Notify of the opportunity to skin this widget */
	static final int SKIN_NEEDED = 1<<21;

	/* Bidi "auto" text direction */
	static final int HAS_AUTO_DIRECTION = 1<<22;

	/* Mouse cursor is over the widget flag */
	static final int MOUSE_OVER = 1<<23;

	/* Child item requires custom draw */
	static final int CUSTOM_DRAW_ITEM = 1<<24;

	/* Default size for widgets */
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;

	/* Bidi UCC to enforce text direction */
	static final char LRE = '\u202a';
	static final char RLE = '\u202b';

	/* Bidi flag and for auto text direction */
	static final int AUTO_TEXT_DIRECTION = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;

	/* Initialize the Common Controls DLL */
	static {
		INITCOMMONCONTROLSEX icce = new INITCOMMONCONTROLSEX ();
		icce.dwSize = INITCOMMONCONTROLSEX.sizeof;
		icce.dwICC = 0xffff;
		OS.InitCommonControlsEx (icce);
	}

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

void _removeListener (int eventType, Listener listener) {
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
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
	checkWidget();
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Dispose, typedListener);
}

long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	return 0;
}

/**
 * Returns a style with exactly one style bit set out of
 * the specified set of exclusive style bits. All other
 * possible bits are cleared when the first matching bit
 * is found. Bits that are not part of the possible set
 * are untouched.
 *
 * @param style the original style bits
 * @param int0 the 0th possible style bit
 * @param int1 the 1st possible style bit
 * @param int2 the 2nd possible style bit
 * @param int3 the 3rd possible style bit
 * @param int4 the 4th possible style bit
 * @param int5 the 5th possible style bit
 *
 * @return the new style bits
 */
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

void checkOpened () {
	/* Do nothing */
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
	parent.checkOpened ();
}

void maybeEnableDarkSystemTheme(long handle) {
	if (display.useDarkModeExplorerTheme) {
		OS.AllowDarkModeForWindow(handle, true);
		OS.SetWindowTheme(handle, Display.EXPLORER, null);
	}
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

/**
 * Destroys the widget in the operating system and releases
 * the widget's handle.  If the widget does not have a handle,
 * this method may hide the widget, mark the widget as destroyed
 * or do nothing, depending on the widget.
 * <p>
 * When a widget is destroyed in the operating system, its
 * descendants are also destroyed by the operating system.
 * This means that it is only necessary to call <code>destroyWidget</code>
 * on the root of the widget tree.
 * </p><p>
 * This method is called after <code>releaseWidget()</code>.
 * </p><p>
 * See also <code>releaseChild()</code>, <code>releaseWidget()</code>
 * and <code>releaseHandle()</code>.
 * </p>
 *
 * @see #dispose
 */
void destroyWidget () {
	releaseHandle ();
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

boolean dragDetect (long hwnd, int x, int y, boolean filter, boolean [] detect, boolean [] consume) {
	if (consume != null) consume [0] = false;
	if (detect != null) detect [0] = true;
	POINT pt = new POINT ();
	pt.x = x;
	pt.y = y;
	OS.ClientToScreen (hwnd, pt);
	return OS.DragDetect (hwnd, pt);
}

/**
 * Does whatever widget specific cleanup is required, and then
 * uses the code in <code>SWTError.error</code> to handle the error.
 *
 * @param code the descriptive error code
 *
 * @see SWT#error(int)
 */
void error (int code) {
	SWT.error(code);
}

boolean filters (int eventType) {
	return display.filters (eventType);
}

Widget findItem (long id) {
	return null;
}

char [] fixMnemonic (String string) {
	return fixMnemonic (string, false, false);
}

char [] fixMnemonic (String string, boolean spaces) {
	return fixMnemonic (string, spaces, false);
}

char [] fixMnemonic (String string, boolean spaces, boolean removeAppended) {
	// fixMnemonic must return a null-terminated array
	char [] buffer = new char [string.length () + 1];
	string.getChars (0, string.length (), buffer, 0);
	int i = 0, j = 0;
	while (i < buffer.length) {
		if (buffer [i] == '&') {
			if (i + 1 < buffer.length && buffer [i + 1] == '&') {
				buffer [j++] = spaces ? ' ' : buffer [i];
				i++;
			}
			i++;
		} else if (buffer [i] == '(' && removeAppended && i + 4 == string.length () && buffer [i + 1] == '&' && buffer [i + 3] == ')') {
			if (spaces) buffer [j++] = ' ';
			i += 4;
		} else {
			buffer [j++] = buffer [i++];
		}
	}
	while (j < buffer.length) buffer [j++] = 0;
	return buffer;
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

Menu getMenu () {
	return null;
}

/**
 * Returns the name of the widget. This is the name of
 * the class without the package name.
 *
 * @return the name of the widget
 */
String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}

/*
 * Returns a short printable representation for the contents
 * of a widget. For example, a button may answer the label
 * text. This is used by <code>toString</code> to provide a
 * more meaningful description of the widget.
 *
 * @return the contents string for the widget
 *
 * @see #toString
 */
String getNameText () {
	return ""; //$NON-NLS-1$
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
	checkWidget();
	return style;
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
	return (state & HAS_AUTO_DIRECTION) != 0;
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
	checkWidget();
	return hooks (eventType);
}

/*
 * Returns <code>true</code> when subclassing is
 * allowed and <code>false</code> otherwise
 *
 * @return <code>true</code> when subclassing is allowed and <code>false</code> otherwise
 */
boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}

/*
 * Returns <code>true</code> when the current thread is
 * the thread that created the widget and <code>false</code>
 * otherwise.
 *
 * @return <code>true</code> when the current thread is the thread that created the widget and <code>false</code> otherwise
 */
boolean isValidThread () {
	return getDisplay ().isValidThread ();
}

void mapEvent (long hwnd, Event event) {
}

GC new_GC (GCData data) {
	return null;
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

/*
 * Releases the widget hierarchy and optionally destroys
 * the receiver.
 * <p>
 * Typically, a widget with children will broadcast this
 * message to all children so that they too can release their
 * resources.  The <code>releaseHandle</code> method is used
 * as part of this broadcast to zero the handle fields of the
 * children without calling <code>destroyWidget</code>.  In
 * this scenario, the children are actually destroyed later,
 * when the operating system destroys the widget tree.
 * </p>
 *
 * @param destroy indicates that the receiver should be destroyed
 *
 * @see #dispose
 * @see #releaseHandle
 * @see #releaseParent
 * @see #releaseWidget
*/
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

/*
 * Releases the widget's handle by zero'ing it out.
 * Does not destroy or release any operating system
 * resources.
 * <p>
 * This method is called after <code>releaseWidget</code>
 * or from <code>destroyWidget</code> when a widget is being
 * destroyed to ensure that the widget is marked as destroyed
 * in case the act of destroying the widget in the operating
 * system causes application code to run in callback that
 * could access the widget.
 * </p>
 *
 * @see #dispose
 * @see #releaseChildren
 * @see #releaseParent
 * @see #releaseWidget
 */
void releaseHandle () {
	state |= DISPOSED;
	display = null;
}

/*
 * Releases the receiver, a child in a widget hierarchy,
 * from its parent.
 * <p>
 * When a widget is destroyed, it may be necessary to remove
 * it from an internal data structure of the parent. When
 * a widget has no handle, it may also be necessary for the
 * parent to hide the widget or otherwise indicate that the
 * widget has been disposed. For example, disposing a menu
 * bar requires that the menu bar first be released from the
 * shell when the menu bar is active.
 * </p>
 *
 * @see #dispose
 * @see #releaseChildren
 * @see #releaseWidget
 * @see #releaseHandle
 */
void releaseParent () {
}

/*
 * Releases any internal resources back to the operating
 * system and clears all fields except the widget handle.
 * <p>
 * When a widget is destroyed, resources that were acquired
 * on behalf of the programmer need to be returned to the
 * operating system.  For example, if the widget made a
 * copy of an icon, supplied by the programmer, this copy
 * would be freed in <code>releaseWidget</code>.  Also,
 * to assist the garbage collector and minimize the amount
 * of memory that is not reclaimed when the programmer keeps
 * a reference to a disposed widget, all fields except the
 * handle are zero'd.  The handle is needed by <code>destroyWidget</code>.
 * </p>
 *
 * @see #dispose
 * @see #releaseChildren
 * @see #releaseHandle
 * @see #releaseParent
 */
void releaseWidget () {
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	_removeListener (eventType, listener);
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
protected void removeListener (int eventType, SWTEventListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
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

boolean sendDragEvent (int button, int x, int y) {
	Event event = new Event ();
	event.button = button;
	event.setLocationInPixels(x, y); // In Pixels
	setInputState (event, SWT.DragDetect);
	postEvent (SWT.DragDetect, event);
	if (isDisposed ()) return false;
	return event.doit;
}

boolean sendDragEvent (int button, int stateMask, int x, int y) {
	Event event = new Event ();
	event.button = button;
	event.setLocationInPixels(x, y);
	event.stateMask = stateMask;
	postEvent (SWT.DragDetect, event);
	if (isDisposed ()) return false;
	return event.doit;
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


void sendSelectionEvent (int type) {
	sendSelectionEvent (type, null, false);
}

void sendSelectionEvent (int type, Event event, boolean send) {
	if (eventTable == null && !display.filters (type)) {
		return;
	}
	if (event == null) event = new Event ();
	setInputState (event, type);
	sendEvent (type, event, send);
}

boolean sendKeyEvent (int type, int msg, long wParam, long lParam) {
	Event event = new Event ();
	if (!setKeyState (event, type, wParam, lParam)) return true;
	return sendKeyEvent (type, msg, wParam, lParam, event);
}

boolean sendKeyEvent (int type, int msg, long wParam, long lParam, Event event) {
	sendEvent (type, event);
	if (isDisposed ()) return false;
	return event.doit;
}

boolean sendMouseEvent (int type, int button, long hwnd, long lParam) {
	return sendMouseEvent (type, button, display.getClickCount (type, button, hwnd, lParam), 0, false, hwnd, lParam);
}

boolean sendMouseEvent (int type, int button, int count, int detail, boolean send, long hwnd, long lParam) {
	if (!hooks (type) && !filters (type)) return true;
	Event event = new Event ();
	event.button = button;
	event.detail = detail;
	event.count = count;
	event.setLocationInPixels(OS.GET_X_LPARAM (lParam), OS.GET_Y_LPARAM (lParam));
	setInputState (event, type);
	mapEvent (hwnd, event);
	if (send) {
		sendEvent (type, event);
		if (isDisposed ()) return false;
	} else {
		postEvent (type, event);
	}
	return event.doit;
}

class MouseWheelData {
	MouseWheelData (boolean isVertical, ScrollBar scrollBar, long wParam, Point remainder) {
		/* WHEEL_DELTA is expressed in precision units, see OS.WHEEL_DELTA */
		int delta = OS.GET_WHEEL_DELTA_WPARAM (wParam);

		/* Wheel speed can be configured in Windows mouse settings */
		if (isVertical) {
			int [] wheelSpeed = new int [1];
			OS.SystemParametersInfo (OS.SPI_GETWHEELSCROLLLINES, 0, wheelSpeed, 0);
			if (wheelSpeed [0] == OS.WHEEL_PAGESCROLL) {
				detail = SWT.SCROLL_PAGE;
			} else {
				delta *= wheelSpeed [0];
				detail = SWT.SCROLL_LINE;
			}
		} else {
			int [] wheelSpeed = new int [1];
			OS.SystemParametersInfo (OS.SPI_GETWHEELSCROLLCHARS, 0, wheelSpeed, 0);
			delta *= wheelSpeed [0];

			/* For legacy compatibility reasons, detail is set to 0 here */
			detail = 0;
		}

		/* Take scrollbar scrolling speed into account */
		if (scrollBar != null) {
			if (detail == SWT.SCROLL_PAGE) {
				delta *= scrollBar.getPageIncrement ();
			} else {
				delta *= scrollBar.getIncrement ();
			}
		}

		/*
		 * Accumulate remainder to deal with fractional scrolls. This is only seen
		 * on some devices which support "smooth scrolling". MSDN also says:
		 *     The remainder must be zeroed when the wheel rotation switches
		 *     directions or when window focus changes.
		 */
		if (isVertical) {
			if ((delta ^ remainder.y) >= 0) delta += remainder.y;
			remainder.y = delta % OS.WHEEL_DELTA;
		} else {
			if ((delta ^ remainder.x) >= 0) delta += remainder.x;
			remainder.x = delta % OS.WHEEL_DELTA;
		}

		/* Finally, divide by WHEEL_DELTA */
		count = delta / OS.WHEEL_DELTA;
	}

	int count;		// lines or pages scrolled
	int detail;		// {0, SWT.SCROLL_PAGE, SWT.SCROLL_LINE}
}

boolean sendMouseWheelEvent (int type, long hwnd, long wParam, long lParam) {
	if (!hooks (type) && !filters (type)) return true;

	boolean vertical = (type == SWT.MouseWheel);
	MouseWheelData wheelData = new MouseWheelData (vertical, null, wParam, display.scrollRemainderEvt);

	if (wheelData.count == 0) return true;

	/* Legacy code. I wonder if any SWT application actually cares? */
	if (!vertical)
		wheelData.count = -wheelData.count;

	POINT pt = new POINT ();
	OS.POINTSTOPOINT (pt, lParam);
	OS.ScreenToClient (hwnd, pt);
	lParam = OS.MAKELPARAM (pt.x, pt.y);
	return sendMouseEvent (type, 0, wheelData.count, wheelData.detail, true, hwnd, lParam);
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

boolean sendFocusEvent (int type) {
	sendEvent (type);
	// widget could be disposed at this point
	return true;
}

boolean setInputState (Event event, int type) {
	if (OS.GetKeyState (OS.VK_MENU) < 0) event.stateMask |= SWT.ALT;
	if (OS.GetKeyState (OS.VK_SHIFT) < 0) event.stateMask |= SWT.SHIFT;
	if (OS.GetKeyState (OS.VK_CONTROL) < 0) event.stateMask |= SWT.CONTROL;
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) event.stateMask |= SWT.BUTTON1;
	if (OS.GetKeyState (OS.VK_MBUTTON) < 0) event.stateMask |= SWT.BUTTON2;
	if (OS.GetKeyState (OS.VK_RBUTTON) < 0) event.stateMask |= SWT.BUTTON3;
	/*
	* Bug in Windows.  On some machines that do not have XBUTTONs,
	* the MK_XBUTTON1 and OS.MK_XBUTTON2 bits are sometimes set,
	* causing mouse capture to become stuck.  The fix is to test
	* for the extra buttons only when they exist.
	*/
	if (display.xMouse) {
		if (OS.GetKeyState (OS.VK_XBUTTON1) < 0) event.stateMask |= SWT.BUTTON4;
		if (OS.GetKeyState (OS.VK_XBUTTON2) < 0) event.stateMask |= SWT.BUTTON5;
	}
	switch (type) {
		case SWT.MouseDown:
		case SWT.MouseDoubleClick:
			if (event.button == 1) event.stateMask &= ~SWT.BUTTON1;
			if (event.button == 2) event.stateMask &= ~SWT.BUTTON2;
			if (event.button == 3) event.stateMask &= ~SWT.BUTTON3;
			if (event.button == 4) event.stateMask &= ~SWT.BUTTON4;
			if (event.button == 5) event.stateMask &= ~SWT.BUTTON5;
			break;
		case SWT.MouseUp:
			if (event.button == 1) event.stateMask |= SWT.BUTTON1;
			if (event.button == 2) event.stateMask |= SWT.BUTTON2;
			if (event.button == 3) event.stateMask |= SWT.BUTTON3;
			if (event.button == 4) event.stateMask |= SWT.BUTTON4;
			if (event.button == 5) event.stateMask |= SWT.BUTTON5;
			break;
		case SWT.KeyDown:
		case SWT.Traverse:
			if (event.keyCode == SWT.ALT) event.stateMask &= ~SWT.ALT;
			if (event.keyCode == SWT.SHIFT) event.stateMask &= ~SWT.SHIFT;
			if (event.keyCode == SWT.CONTROL) event.stateMask &= ~SWT.CONTROL;
			break;
		case SWT.KeyUp:
			if (event.keyCode == SWT.ALT) event.stateMask |= SWT.ALT;
			if (event.keyCode == SWT.SHIFT) event.stateMask |= SWT.SHIFT;
			if (event.keyCode == SWT.CONTROL) event.stateMask |= SWT.CONTROL;
			break;
	}
	return true;
}

/**
 * On Windows, keyboard layout translates a key press twice:
 * <ol>
 * <li>First translation is made from "scan code" (can be thought of
 * as geometrical location of key on keyboard) to "virtual key"
 * (can be thought of as a key meaning on latin keyboard). This
 * happens even for layouts that have no latin keys (Bulgarian,
 * Hebrew, Japanese, etc).</li>
 * <li>Second translation is made from "virtual key" to character(s).
 * A virtual key can produce zero chars (dead keys), one char
 * (usual keys), or more than one char (ligatures).</li>
 * </ol>
 *
 * Such two-step translation allows to answer both which character
 * to produce, and which keyboard shortcut to invoke in app.
 *
 * Let's see some examples:<br>
 * <table>
 *  <tr><th>Layout</th><th>Key row</th><th>Key col</th><th>Scan code</th><th>Virt key</th><th>Character</th></tr>
 *  <tr><td>English US    </td><td>3</td><td>5</td><td>0x13</td><td>VK_R</td><td>r</td></tr>
 *  <tr><td>English US    </td><td>3</td><td>6</td><td>0x14</td><td>VK_T</td><td>t</td></tr>
 *  <tr><td>English Dvorak</td><td>3</td><td>5</td><td>0x13</td><td>VK_P</td><td>p</td></tr>
 *  <tr><td>English Dvorak</td><td>3</td><td>6</td><td>0x14</td><td>VK_Y</td><td>y</td></tr>
 *  <tr><td>Bulgarian     </td><td>3</td><td>5</td><td>0x13</td><td>VK_R</td><td>и</td></tr>
 *  <tr><td>Bulgarian     </td><td>3</td><td>6</td><td>0x14</td><td>VK_T</td><td>ш</td></tr>
 * </table><br>
 * In these examples, it can be seen how
 * <ul>
 *  <li>Same physical key always has the same scan code</li>
 *  <li>Same physical key can produce different characters (that's
 *   what keyboard layouts are for)</li>
 *  <li>Same physical key can produce different virtual keys
 *   (see English US, Dvorak)</li>
 *  <li>Same virtual key can produce different characters (see
 *   English, Bulgarian). Bulgarian only has cyrillic characters
 *   and no latin ones, but still has standard virtual keys.</li>
 * </ul>
 *
 * Note that it's valid for a "latin" virtual key to produce some
 * other latin character. For example, VK_C could produce latin J.
 * This can be used in Dvorak-QWERTY keyboard layout that types
 * Dvorak, but has QWERTY keyboard shortcuts.
 * <br>
 * Due to two-step translation, almost every common keyboard layout
 * has virtual keys VK_A ... VK_Z mapped on it, even if layout
 * doesn't type a single latin character. On Windows, keyboard
 * shortcuts bind to virtual keys. Therefore:
 * <ul>
 * <li>In English US, Ctrl+C will be produced by Ctrl and key labeled 'C'.</li>
 * <li>In Dvorak, Ctrl+C will be produced by Ctrl and key labeled 'C'.
 *  Note that Dvorak 'C' is where English US 'I' is.</li>
 * <li>In Bulgarian, Ctrl+C will be produced by Ctrl and key labeled 'Ъ'.
 *  Because this is the key to which VK_C is mapped.</li>
 * </ul>
 */
boolean setKeyState (Event event, int type, long wParam, long lParam) {

	/*
	* Feature in Windows.  When the user presses Ctrl+Backspace
	* or Ctrl+Enter, Windows sends a WM_CHAR with Delete (0x7F)
	* and '\n' instead of '\b' and '\r'.  This is the correct
	* platform behavior but is not portable.  The fix is to detect
	* these cases and convert the character.
	*/
	switch (display.lastAscii) {
		case SWT.DEL:
			if (display.lastKey == SWT.BS) display.lastAscii = SWT.BS;
			break;
		case SWT.LF:
			if (display.lastKey == SWT.CR) display.lastAscii = SWT.CR;
			break;
	}

	/*
	* Feature in Windows.  When the user presses either the Enter
	* key or the numeric keypad Enter key, Windows sends a WM_KEYDOWN
	* with wParam=VK_RETURN in both cases.  In order to distinguish
	* between the keys, the extended key bit is tested. If the bit
	* is set, assume that the numeric keypad Enter was pressed.
	*/
	if (display.lastKey == SWT.CR && display.lastAscii == SWT.CR) {
		if ((lParam & 0x1000000) != 0) display.lastKey = SWT.KEYPAD_CR;
	}

	setLocationMask(event, type, wParam, lParam);

	if (display.lastVirtual) {
		/*
		* Feature in Windows.  The virtual key VK_DELETE is not
		* treated as both a virtual key and an ASCII key by Windows.
		* Therefore, we will not receive a WM_CHAR for this key.
		* The fix is to treat VK_DELETE as a special case and map
		* the ASCII value explicitly (Delete is 0x7F).
		*/
		if (display.lastKey == OS.VK_DELETE) display.lastAscii = 0x7F;

		/*
		* Feature in Windows.  When the user presses Ctrl+Pause, the
		* VK_CANCEL key is generated and a WM_CHAR is sent with 0x03,
		* possibly to allow an application to look for Ctrl+C and the
		* the Break key at the same time.  This is unexpected and
		* unwanted.  The fix is to detect the case and set the character
		* to zero.
		*/
		if (display.lastKey == OS.VK_CANCEL) display.lastAscii = 0x0;

		event.keyCode = Display.translateKey (display.lastKey);
	} else {
		event.keyCode = display.lastKey;
	}
	event.character = (char) display.lastAscii;
	if (event.keyCode == 0 && event.character == 0) {
		return false;
	}
	return setInputState (event, type);
}

int setLocationMask (Event event, int type, long wParam, long lParam) {
	int location = SWT.NONE;
	if (display.lastVirtual) {
		switch (display.lastKey) {
			case OS.VK_SHIFT:
				if (OS.GetKeyState(OS.VK_LSHIFT) < 0) location = SWT.LEFT;
				if (OS.GetKeyState(OS.VK_RSHIFT) < 0) location = SWT.RIGHT;
				break;
			case OS.VK_NUMLOCK:
				location = SWT.KEYPAD;
				break;
			case OS.VK_CONTROL:
			case OS.VK_MENU:
				location = (lParam & 0x1000000) == 0 ? SWT.LEFT : SWT.RIGHT;
				break;
			case OS.VK_INSERT:
			case OS.VK_DELETE:
			case OS.VK_HOME:
			case OS.VK_END:
			case OS.VK_PRIOR:
			case OS.VK_NEXT:
			case OS.VK_UP:
			case OS.VK_DOWN:
			case OS.VK_LEFT:
			case OS.VK_RIGHT:
				if ((lParam & 0x1000000) == 0) {
					location = SWT.KEYPAD;
				}
				break;
		}
		if (display.numpadKey(display.lastKey) != 0) {
			location = SWT.KEYPAD;
		}
	} else {
		if (display.lastKey == SWT.KEYPAD_CR) {
			location = SWT.KEYPAD;
		}
	}
	event.keyLocation = location;
	return location;
}

boolean setTabGroupFocus () {
	return setTabItemFocus ();
}

boolean setTabItemFocus () {
	return false;
}

boolean showMenu (int x, int y) {
	return showMenu (x, y, SWT.MENU_MOUSE);
}

boolean showMenu (int x, int y, int detail) {
	Event event = new Event ();
	event.setLocationInPixels(x, y);
	event.detail = detail;
	if (event.detail == SWT.MENU_KEYBOARD) {
		updateMenuLocation (event);
	}
	sendEvent (SWT.MenuDetect, event);
	// widget could be disposed at this point
	if (isDisposed ()) return false;
	if (!event.doit) return true;
	Menu menu = getMenu ();
	if (menu != null && !menu.isDisposed ()) {
		Point loc = event.getLocationInPixels(); // In Pixels
		if (x != loc.x || y != loc.y) {
			menu.setLocation (event.getLocation());
		}
		menu.setVisible (true);
		return true;
	}
	return false;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	String string = "*Disposed*"; //$NON-NLS-1$
	if (!isDisposed ()) {
		string = "*Wrong Thread*"; //$NON-NLS-1$
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}"; //$NON-NLS-1$ //$NON-NLS-2$
}

void updateMenuLocation (Event event) {
	/* Do nothing */
}

LRESULT wmCaptureChanged (long hwnd, long wParam, long lParam) {
	display.captureChanged = true;
	return null;
}

LRESULT wmChar (long hwnd, long wParam, long lParam) {
	display.lastAscii = (int)wParam;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_CHAR, wParam, lParam)) {
		return LRESULT.ONE;
	}
	// widget could be disposed at this point
	return null;
}

LRESULT wmContextMenu (long hwnd, long wParam, long lParam) {
	if (wParam != hwnd) return null;

	/*
	* Feature in Windows.  When the user presses  WM_NCRBUTTONUP,
	* a WM_CONTEXTMENU message is generated.  This happens when
	* the user releases the mouse over a scroll bar.  Normally,
	* window displays the default scrolling menu but applications
	* can process WM_CONTEXTMENU to display a different menu.
	* Typically, an application does not want to supply a special
	* scroll menu.  The fix is to look for a WM_CONTEXTMENU that
	* originated from a mouse event and display the menu when the
	* mouse was released in the client area.
	*/
	int x = 0, y = 0, detail = 0;
	if (lParam != -1) {
		POINT pt = new POINT ();
		OS.POINTSTOPOINT (pt, lParam);
		x = pt.x;
		y = pt.y;
		detail = SWT.MENU_MOUSE;
		OS.ScreenToClient (hwnd, pt);
		RECT rect = new RECT ();
		OS.GetClientRect (hwnd, rect);
		if (!OS.PtInRect (rect, pt)) return null;
	} else {
		int pos = OS.GetMessagePos ();
		x = OS.GET_X_LPARAM (pos);
		y = OS.GET_Y_LPARAM (pos);
		detail = SWT.MENU_KEYBOARD;
	}

	/* Show the menu */
	return showMenu (x, y, detail) ? LRESULT.ZERO : null;
}

LRESULT wmIMEChar (long hwnd, long wParam, long lParam) {
	Display display = this.display;
	display.lastKey = 0;
	display.lastAscii = (int)wParam;
	display.lastVirtual = display.lastDead = false;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
		return LRESULT.ONE;
	}
	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	return LRESULT.ONE;
}

int mapVirtualKey (int virtualKey) {
	if (('0' <= virtualKey) && (virtualKey <= '9')) {
		// Some keyboard layouts have non-latin digits. For example,
		// Devanagari and Bengali. Some keyboard layouts repurpose
		// digit keys for something else. For example, French and
		// Lithuanian. Yet still, applications expect to see digits
		// in 'Event.keyCode' to be able to match these to hot keys.
		// Note that on Windows, most native applications bind hot
		// keys to virtual code of the key and not the character
		// produced by it. For them, this problem doesn't even exist.
		// Note that virtual key codes for 0...9 match corresponding
		// chars.
		return virtualKey;
	} else if (('A' <= virtualKey) && (virtualKey <= 'Z')) {
		// See above about digits. Also note that on Windows,
		// 'MapVirtualKey()' is hardcoded to always return 'A'...'Z'
		// for corresponding virtual key codes (undocumented but has
		// been this way for ages). Note that virtual key codes for
		// A...Z match corresponding chars.
		return virtualKey;
	} else {
		return OS.MapVirtualKey (virtualKey, 2);
	}
}

LRESULT wmKeyDown (long hwnd, long wParam, long lParam) {

	/* Ignore repeating modifier keys by testing key down state */
	switch ((int)wParam) {
		case OS.VK_SHIFT:
		case OS.VK_MENU:
		case OS.VK_CONTROL:
		case OS.VK_CAPITAL:
		case OS.VK_NUMLOCK:
		case OS.VK_SCROLL:
			if ((lParam & 0x40000000) != 0) return null;
	}

	/* Clear last key and last ascii because a new key has been typed */
	display.lastAscii = display.lastKey = 0;
	display.lastVirtual = display.lastDead = false;

	int mapKey = mapVirtualKey ((int)wParam);

	/*
	* Dead keys are special keys that modify next keys pressed. For
	* example, in German, pressing ^ and then E produces Ê. SWT is
	* designed to not report the dead key and only report the final
	* character(s). Note that there might be multiple characters,
	* for example, ^^ will produce nothing on first key and ^^ when
	* second key is pressed. The most reliable way of detecting dead
	* keys is by peeking for 'WM_DEADCHAR'. See similar code block
	* below for a detailed explanation.
	*/
	MSG msg = new MSG ();
	int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD;
	if (OS.PeekMessage (msg, hwnd, OS.WM_DEADCHAR, OS.WM_DEADCHAR, flags)) {
		display.lastDead = true;
		display.lastVirtual = mapKey == 0;
		display.lastKey = display.lastVirtual ? (int)wParam : mapKey;
		return null;
	}

	/*
	 * SWT is designed to deliver both 'WM_KEYDOWN' and 'WM_CHAR' in a
	 * single 'SWT.KeyDown' event. However, 'WM_KEYDOWN' is followed by
	 * 'WM_CHAR' only if pressed key types some character. For example,
	 * A produces a character, while F1 does not.
	 * Figuring if 'WM_KEYDOWN' is going to be followed up by 'WM_CHAR'
	 * is hard in Windows:
	 * 1) Everything depends on keyboard layout, and there are pretty
	 *    exotic layouts out there.
	 * 2) MapVirtualKey() API ignores modifier keys, and therefore
	 *    can't be used if any modifier keys are present.
	 * 3) ToUnicode() API is quite good, but it alters internal keyboard
	 *    state for dead keys. For example, in German, pressing ^ and
	 *    then E produces Ê. However, if ToUnicode() is called in
	 *    between, it will produce E instead. The internal state is
	 *    kept in kernel and I didn't find any reasonable ways of
	 *    restoring it after calling ToUnicode().
	 * 4) Win10 introduces new flag for ToUnicode() to preserve
	 *    internal state, but SWT has to support earlier Windows at
	 *    the moment.
	 * The workaround is to peek for 'WM_CHAR'. This works because when
	 * SWT calls 'TranslateMessage()', Windows posts corresponding
	 * 'WM_CHAR' message(s). Only then SWT calls 'DispatchMessage()' to
	 * actually handle 'WM_KEYDOWN'. So at this moment, if 'WM_CHAR' is
	 * expected, it will already be present in the queue. One other
	 * thing to notice is that 'GetMessage()' returns all sent/posted
	 * messages before input events (see MSDN), so even if some app
	 * sends 'WM_CHAR' to SWT window, it will be processed before
	 * 'WM_KEYDOWN' even if key event occurred before.
	 */
	boolean isCharPending = false;
	if (OS.PeekMessage (msg, hwnd, OS.WM_CHAR, OS.WM_CHAR, flags)) {
		isCharPending = true;
	}

	/*
	* Bug 88281: Sometimes, 'PeekMessage()' could result in widget
	* being disposed. Most likely this was due to 'WH_MSGFILTER' hook
	* that previously incorrectly reacted to 'PM_NOREMOVE' as well
	* as 'PM_REMOVE'. Some SWT hooks can do no-trivial things. I'm
	* not sure if this can still happen now that hooks are fixed to
	* only react to 'PM_REMOVE'. I'm also not 100% sure that hooks
	* are the only way to trigger this problem.
	*/
	if (isDisposed ()) return LRESULT.ONE;

	/*
	* If we are going to get a WM_CHAR, ensure that last key has
	* the correct character value for the key down and key up
	* events.  It is not sufficient to ignore the WM_KEYDOWN
	* (when we know we are going to get a WM_CHAR) and compute
	* the key in WM_CHAR because there is not enough information
	* by the time we get the WM_CHAR.  For example, when the user
	* types Ctrl+Shift+6 on a US keyboard, we get a WM_CHAR with
	* wParam=30.  When the user types Ctrl+Shift+6 on a German
	* keyboard, we also get a WM_CHAR with wParam=30.  On the US
	* keyboard Shift+6 is ^, on the German keyboard Shift+6 is &.
	* There is no way to map wParam=30 in WM_CHAR to the correct
	* value.  Also, on international keyboards, the control key
	* may be down when the user has not entered a control character.
	*/
	display.lastVirtual = mapKey == 0 || display.numpadKey ((int)wParam) != 0;
	if (display.lastVirtual) {
		display.lastKey = (int)wParam;
		/*
		* Feature in Windows.  The virtual key VK_DELETE is not
		* treated as both a virtual key and an ASCII key by Windows.
		* Therefore, we will not receive a WM_CHAR for this key.
		* The fix is to treat VK_DELETE as a special case and map
		* the ASCII value explicitly (Delete is 0x7F).
		*/
		if (display.lastKey == OS.VK_DELETE) display.lastAscii = 0x7F;

		/*
		* It is possible to get a WM_CHAR for a virtual key when
		* Num Lock is on.  If the user types Home while Num Lock
		* is down, a WM_CHAR is issued with WPARM=55 (for the
		* character 7).  If we are going to get a WM_CHAR we need
		* to ensure that the last key has the correct value.  Note
		* that Ctrl+Home does not issue a WM_CHAR when Num Lock is
		* down.
		*/
		if (OS.VK_NUMPAD0 <= display.lastKey && display.lastKey <= OS.VK_DIVIDE) {
			display.lastAscii = display.numpadKey (display.lastKey);
		}
	} else {
		/*
		* Convert LastKey to lower case because Windows non-virtual
		* keys that are also ASCII keys, such as like VK_A, are have
		* upper case values in WM_KEYDOWN despite the fact that the
		* Shift was not pressed.
		*/
		display.lastKey = (int)OS.CharLower (OS.LOWORD (mapKey));

		/*
		* Feature in Windows. The virtual key VK_CANCEL is treated
		* as both a virtual key and ASCII key by Windows.  This
		* means that a WM_CHAR with WPARAM=3 will be issued for
		* this key.  In order to distinguish between this key and
		* Ctrl+C, mark the key as virtual.
		*/
		if (wParam == OS.VK_CANCEL) display.lastVirtual = true;

		if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
			/*
			 * Get the shifted state or convert to lower case if necessary.
			 * If the user types Ctrl+A, LastAscii should be 'a', not 'A'.
			 * If the user types Ctrl+Shift+A, LastAscii should be 'A'.
			 * If the user types Ctrl+Shift+6, the value of LastAscii will
			 * depend on the international keyboard.
			 */
			if (OS.GetKeyState (OS.VK_SHIFT) < 0) {
				display.lastAscii = display.shiftedKey ((int)wParam);
				if (display.lastAscii == 0) display.lastAscii = mapKey;
			} else {
				display.lastAscii = (int)OS.CharLower (OS.LOWORD (mapKey));
			}

			display.lastAscii = display.controlKey (display.lastAscii);
		}
	}

	if (isCharPending) {
		return null;
	}

	if (!sendKeyEvent (SWT.KeyDown, OS.WM_KEYDOWN, wParam, lParam)) {
		return LRESULT.ONE;
	}
	// widget could be disposed at this point
	return null;
}

LRESULT wmKeyUp (long hwnd, long wParam, long lParam) {
	Display display = this.display;

	/*
	* If the key up is not hooked, reset last key
	* and last ascii in case the key down is hooked.
	*/
	if (!hooks (SWT.KeyUp) && !display.filters (SWT.KeyUp)) {
		display.lastKey = display.lastAscii = 0;
		display.lastVirtual = display.lastDead = false;
		return null;
	}

	/* Map the virtual key. */
	int mapKey = OS.MapVirtualKey ((int)wParam, 2);

	if (display.lastDead) return null;

	/*
	* NOTE: On Windows 98, keypad keys are virtual despite the
	* fact that a WM_CHAR is issued.  On Windows 2000 and XP,
	* they are not virtual.  Therefore it is necessary to force
	* numeric keypad keys to be virtual.
	*/
	display.lastVirtual = mapKey == 0 || display.numpadKey ((int)wParam) != 0;
	if (display.lastVirtual) {
		display.lastKey = (int)wParam;
	} else {
		/*
		* Feature in Windows. The virtual key VK_CANCEL is treated
		* as both a virtual key and ASCII key by Windows.  This
		* means that a WM_CHAR with WPARAM=3 will be issued for
		* this key.  In order to distinguish between this key and
		* Ctrl+C, mark the key as virtual.
		*/
		if (wParam == OS.VK_CANCEL) display.lastVirtual = true;
		if (display.lastKey == 0) {
			display.lastAscii = 0;
			display.lastDead = false;
			return null;
		}
	}
	LRESULT result = null;
	if (!sendKeyEvent (SWT.KeyUp, OS.WM_KEYUP, wParam, lParam)) {
		result = LRESULT.ONE;
	}
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	display.lastVirtual = display.lastDead = false;
	return result;
}

LRESULT wmKillFocus (long hwnd, long wParam, long lParam) {
	/*
	 * MSDN says: The remainder must be zeroed when the wheel rotation switches
	 * directions or when window focus changes.
	 */
	display.scrollRemainderEvt.x = 0;
	display.scrollRemainderEvt.y = 0;
	display.scrollRemainderBar.x = 0;
	display.scrollRemainderBar.y = 0;

	long code = callWindowProc (hwnd, OS.WM_KILLFOCUS, wParam, lParam);
	sendFocusEvent (SWT.FocusOut);
	// widget could be disposed at this point

	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the focus
	* or deactivate events.  If this happens, end the
	* processing of the Windows message by returning
	* zero as the result of the window proc.
	*/
	if (isDisposed ()) return LRESULT.ZERO;
	if (code == 0) return LRESULT.ZERO;
	return new LRESULT (code);
}

LRESULT wmLButtonDblClk (long hwnd, long wParam, long lParam) {
	/*
	* Feature in Windows. Windows sends the following
	* messages when the user double clicks the mouse:
	*
	*	WM_LBUTTONDOWN		- mouse down
	*	WM_LBUTTONUP		- mouse up
	*	WM_LBUTTONDBLCLK	- double click
	*	WM_LBUTTONUP		- mouse up
	*
	* Applications that expect matching mouse down/up
	* pairs will not see the second mouse down.  The
	* fix is to send a mouse down event.
	*/
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	sendMouseEvent (SWT.MouseDown, 1, hwnd, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, 1, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_LBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmLButtonDown (long hwnd, long wParam, long lParam) {
	Display display = this.display;
	LRESULT result = null;
	int x = OS.GET_X_LPARAM (lParam);
	int y = OS.GET_Y_LPARAM (lParam);
	boolean [] consume = null, detect = null;
	boolean dragging = false, mouseDown = true;
	int count = display.getClickCount (SWT.MouseDown, 1, hwnd, lParam);
	if (count == 1 && (state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect)) {
		/*
		* Feature in Windows.  It's possible that the drag
		* operation will not be started while the mouse is
		* down, meaning that the mouse should be captured.
		* This can happen when the user types the ESC key
		* to cancel the drag.  The fix is to query the state
		* of the mouse and capture the mouse accordingly.
		*/
		detect = new boolean [1];
		consume = new boolean [1];
		dragging = dragDetect (hwnd, x, y, true, detect, consume);
		if (isDisposed ()) return LRESULT.ZERO;
		mouseDown = OS.GetKeyState (OS.VK_LBUTTON) < 0;
	}
	display.captureChanged = false;
	boolean dispatch = sendMouseEvent (SWT.MouseDown, 1, count, 0, false, hwnd, lParam);
	if (dispatch && (consume == null || !consume [0])) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_LBUTTONDOWN, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (mouseDown) {
		if (!display.captureChanged && !isDisposed ()) {
			if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
		}
	}
	if (dragging) {
		sendDragEvent (1, x, y);
	} else {
		if (detect != null && detect [0]) {
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
			if (OS.GetKeyState (OS.VK_ESCAPE) >= 0) {
				OS.SendMessage (hwnd, OS.WM_LBUTTONUP, wParam, lParam);
			}
		}
	}
	return result;
}

LRESULT wmLButtonUp (long hwnd, long wParam, long lParam) {
	Display display = this.display;
	LRESULT result = null;
	if (sendMouseEvent (SWT.MouseUp, 1, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_LBUTTONUP, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	/*
	* Bug in Windows.  On some machines that do not have XBUTTONs,
	* the MK_XBUTTON1 and OS.MK_XBUTTON2 bits are sometimes set,
	* causing mouse capture to become stuck.  The fix is to test
	* for the extra buttons only when they exist.
	*/
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON;
	if (display.xMouse) mask |= OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if ((wParam & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return result;
}

LRESULT wmMButtonDblClk (long hwnd, long wParam, long lParam) {
	/*
	* Feature in Windows. Windows sends the following
	* messages when the user double clicks the mouse:
	*
	*	WM_MBUTTONDOWN		- mouse down
	*	WM_MBUTTONUP		- mouse up
	*	WM_MLBUTTONDBLCLK	- double click
	*	WM_MBUTTONUP		- mouse up
	*
	* Applications that expect matching mouse down/up
	* pairs will not see the second mouse down.  The
	* fix is to send a mouse down event.
	*/
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	sendMouseEvent (SWT.MouseDown, 2, hwnd, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, 2, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_MBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmMButtonDown (long hwnd, long wParam, long lParam) {
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	if (sendMouseEvent (SWT.MouseDown, 2, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_MBUTTONDOWN, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmMButtonUp (long hwnd, long wParam, long lParam) {
	Display display = this.display;
	LRESULT result = null;
	if (sendMouseEvent (SWT.MouseUp, 2, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_MBUTTONUP, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	/*
	* Bug in Windows.  On some machines that do not have XBUTTONs,
	* the MK_XBUTTON1 and OS.MK_XBUTTON2 bits are sometimes set,
	* causing mouse capture to become stuck.  The fix is to test
	* for the extra buttons only when they exist.
	*/
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON;
	if (display.xMouse) mask |= OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if ((wParam & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return result;
}

LRESULT wmMouseHover (long hwnd, long wParam, long lParam) {
	if (!sendMouseEvent (SWT.MouseHover, 0, hwnd, lParam)) {
		return LRESULT.ZERO;
	}
	return null;
}

LRESULT wmMouseLeave (long hwnd, long wParam, long lParam) {
	state &= ~MOUSE_OVER;
	if (!hooks (SWT.MouseExit) && !filters (SWT.MouseExit)) return null;
	int pos = OS.GetMessagePos ();
	POINT pt = new POINT ();
	OS.POINTSTOPOINT (pt, pos);
	OS.ScreenToClient (hwnd, pt);
	lParam = OS.MAKELPARAM (pt.x, pt.y);
	if (!sendMouseEvent (SWT.MouseExit, 0, hwnd, lParam)) {
		return LRESULT.ZERO;
	}
	return null;
}

LRESULT wmMouseMove (long hwnd, long wParam, long lParam) {
	LRESULT result = null;
	Display display = this.display;
	int pos = OS.GetMessagePos ();
	if (pos != display.lastMouse || display.captureChanged) {
		boolean trackMouse = (state & TRACK_MOUSE) != 0;
		boolean mouseEnter = hooks (SWT.MouseEnter) || display.filters (SWT.MouseEnter);
		boolean mouseExit = hooks (SWT.MouseExit) || display.filters (SWT.MouseExit);
		boolean mouseHover = hooks (SWT.MouseHover) || display.filters (SWT.MouseHover);
		if (trackMouse || mouseEnter || mouseExit || mouseHover) {
			TRACKMOUSEEVENT lpEventTrack = new TRACKMOUSEEVENT ();
			lpEventTrack.cbSize = TRACKMOUSEEVENT.sizeof;
			lpEventTrack.dwFlags = OS.TME_LEAVE | OS.TME_HOVER;
			lpEventTrack.hwndTrack = hwnd;
			OS.TrackMouseEvent (lpEventTrack);
			if (mouseEnter && (state & MOUSE_OVER) == 0) {
				/*
				 * Force all outstanding WM_MOUSELEAVE messages to be dispatched before
				 * issuing a mouse enter.  This causes mouse exit events to be processed
				 * before mouse enter events.  Note that WM_MOUSELEAVE is posted to the
				 * event queue by TrackMouseEvent().
				 */
				MSG msg = new MSG ();
				int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
				while (OS.PeekMessage (msg, 0, OS.WM_MOUSELEAVE, OS.WM_MOUSELEAVE, flags)) {
					OS.TranslateMessage (msg);
					OS.DispatchMessage (msg);
				}
				sendMouseEvent (SWT.MouseEnter, 0, hwnd, lParam);
			}
			state |= MOUSE_OVER;
		}
		if (pos != display.lastMouse) {
			display.lastMouse = pos;
			if (!sendMouseEvent (SWT.MouseMove, 0, hwnd, lParam)) {
				result = LRESULT.ZERO;
			}
		}
	}
	display.captureChanged = false;
	return result;
}

LRESULT wmMouseWheel (long hwnd, long wParam, long lParam) {
	return sendMouseWheelEvent(SWT.MouseWheel, hwnd, wParam, lParam) ? null : LRESULT.ZERO;
}

LRESULT wmMouseHWheel (long hwnd, long wParam, long lParam) {
	return sendMouseWheelEvent(SWT.MouseHorizontalWheel, hwnd, wParam, lParam) ? null : LRESULT.ZERO;
}

LRESULT wmNCPaint (long hwnd, long wParam, long lParam) {
	return null;
}

LRESULT wmPaint (long hwnd, long wParam, long lParam) {

	/* Exit early - don't draw the background */
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) {
		return null;
	}

	/* Issue a paint event */
	long rgn = OS.CreateRectRgn (0, 0, 0, 0);
	OS.GetUpdateRgn (hwnd, rgn, false);
	long result = callWindowProc (hwnd, OS.WM_PAINT, wParam, lParam);
	GCData data = new GCData ();
	data.hwnd = hwnd;
	GC gc = new_GC (data);
	if (gc != null) {
		OS.HideCaret (hwnd);
		RECT rect = new RECT();
		OS.GetRgnBox (rgn, rect);
		int width = rect.right - rect.left;
		int height = rect.bottom - rect.top;
		if (width != 0 && height != 0) {
			long hDC = gc.handle;
			OS.SelectClipRgn (hDC, rgn);
			OS.SetMetaRgn (hDC);
			Event event = new Event ();
			event.gc = gc;
			event.setBoundsInPixels(new Rectangle(rect.left, rect.top, width, height));
			sendEvent (SWT.Paint, event);
			// widget could be disposed at this point
			event.gc = null;
		}
		gc.dispose ();
		OS.ShowCaret (hwnd);
	}
	OS.DeleteObject (rgn);
	if (result == 0) return LRESULT.ZERO;
	return new LRESULT (result);
}

LRESULT wmPrint (long hwnd, long wParam, long lParam) {
	/*
	* Bug in Windows.  When WM_PRINT is used to print the contents
	* of a control that has WS_EX_CLIENTEDGE, the old 3D border is
	* drawn instead of the theme border.  The fix is to call the
	* default window proc and then draw the theme border on top.
	*/
	if ((lParam & OS.PRF_NONCLIENT) != 0) {
		if (OS.IsAppThemed ()) {
			int bits = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
			if ((bits & OS.WS_EX_CLIENTEDGE) != 0) {
				long code = callWindowProc (hwnd, OS.WM_PRINT, wParam, lParam);
				RECT rect = new RECT ();
				OS.GetWindowRect (hwnd, rect);
				rect.right -= rect.left;
				rect.bottom -= rect.top;
				rect.left = rect.top = 0;
				int border = OS.GetSystemMetrics (OS.SM_CXEDGE);
				OS.ExcludeClipRect (wParam, border, border, rect.right - border, rect.bottom - border);
				OS.DrawThemeBackground (display.hEditTheme (), wParam, OS.EP_EDITTEXT, OS.ETS_NORMAL, rect, null);
				return new LRESULT (code);
			}
		}
	}
	return null;
}

LRESULT wmRButtonDblClk (long hwnd, long wParam, long lParam) {
	/*
	* Feature in Windows. Windows sends the following
	* messages when the user double clicks the mouse:
	*
	*	WM_RBUTTONDOWN		- mouse down
	*	WM_RBUTTONUP		- mouse up
	*	WM_RBUTTONDBLCLK	- double click
	*	WM_LBUTTONUP		- mouse up
	*
	* Applications that expect matching mouse down/up
	* pairs will not see the second mouse down.  The
	* fix is to send a mouse down event.
	*/
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	sendMouseEvent (SWT.MouseDown, 3, hwnd, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, 3, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_RBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmRButtonDown (long hwnd, long wParam, long lParam) {
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	if (sendMouseEvent (SWT.MouseDown, 3, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_RBUTTONDOWN, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmRButtonUp (long hwnd, long wParam, long lParam) {
	Display display = this.display;
	LRESULT result = null;
	if (sendMouseEvent (SWT.MouseUp, 3, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_RBUTTONUP, wParam, lParam));
	} else {
		/* Call the DefWindowProc() to support WM_CONTEXTMENU */
		OS.DefWindowProc (hwnd, OS.WM_RBUTTONUP, wParam, lParam);
		result = LRESULT.ZERO;
	}
	/*
	* Bug in Windows.  On some machines that do not have XBUTTONs,
	* the MK_XBUTTON1 and OS.MK_XBUTTON2 bits are sometimes set,
	* causing mouse capture to become stuck.  The fix is to test
	* for the extra buttons only when they exist.
	*/
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON;
	if (display.xMouse) mask |= OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if ((wParam & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return result;
}

LRESULT wmSetFocus (long hwnd, long wParam, long lParam) {
	long code = callWindowProc (hwnd, OS.WM_SETFOCUS, wParam, lParam);
	sendFocusEvent (SWT.FocusIn);
	// widget could be disposed at this point

	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the focus
	* or activate events.  If this happens, end the
	* processing of the Windows message by returning
	* zero as the result of the window proc.
	*/
	if (isDisposed ()) return LRESULT.ZERO;
	if (code == 0) return LRESULT.ZERO;
	return new LRESULT (code);
}

LRESULT wmSysChar (long hwnd, long wParam, long lParam) {
	Display display = this.display;
	display.lastAscii = (int)wParam;

	/* Do not issue a key down if a menu bar mnemonic was invoked */
	if (!hooks (SWT.KeyDown) && !display.filters (SWT.KeyDown)) {
		return null;
	}

	/* Call the window proc to determine whether it is a system key or mnemonic */
	boolean oldKeyHit = display.mnemonicKeyHit;
	display.mnemonicKeyHit = true;
	long result = callWindowProc (hwnd, OS.WM_SYSCHAR, wParam, lParam);
	boolean consumed = false;
	if (!display.mnemonicKeyHit) {
		consumed = !sendKeyEvent (SWT.KeyDown, OS.WM_SYSCHAR, wParam, lParam);
		// widget could be disposed at this point
	}
	consumed |= display.mnemonicKeyHit;
	display.mnemonicKeyHit = oldKeyHit;
	return consumed ? LRESULT.ONE : new LRESULT (result);
}

LRESULT wmSysKeyDown (long hwnd, long wParam, long lParam) {
	/*
	* Feature in Windows.  When WM_SYSKEYDOWN is sent,
	* the user pressed ALT+<key> or F10 to get to the
	* menu bar.  In order to issue events for F10 but
	* ignore other key presses when the ALT is not down,
	* make sure that either F10 was pressed or that ALT
	* is pressed.
	*/
	if (wParam != OS.VK_F10) {
		/* Make sure WM_SYSKEYDOWN was sent by ALT-<aKey>. */
		if ((lParam & 0x20000000) == 0) return null;
	}

	/* Ignore well known system keys */
	switch ((int)wParam) {
		case OS.VK_F4: {
			long hwndShell = hwnd;
			while (OS.GetParent (hwndShell) != 0) {
				if (OS.GetWindow (hwndShell, OS.GW_OWNER) != 0) break;
				hwndShell = OS.GetParent (hwndShell);
			}
			int bits = OS.GetWindowLong (hwndShell, OS.GWL_STYLE);
			if ((bits & OS.WS_SYSMENU) != 0) return null;
		}
	}

	/* Ignore repeating modifier keys by testing key down state */
	switch ((int)wParam) {
		case OS.VK_SHIFT:
		case OS.VK_MENU:
		case OS.VK_CONTROL:
		case OS.VK_CAPITAL:
		case OS.VK_NUMLOCK:
		case OS.VK_SCROLL:
			if ((lParam & 0x40000000) != 0) return null;
	}

	/* Clear last key and last ascii because a new key has been typed */
	display.lastAscii = display.lastKey = 0;
	display.lastVirtual = display.lastDead = false;

	int mapKey = mapVirtualKey ((int)wParam);

	// See corresponding code block in 'WM_KEYDOWN' for an explanation.
	MSG msg = new MSG ();
	int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD;
	if (OS.PeekMessage (msg, hwnd, OS.WM_SYSDEADCHAR, OS.WM_SYSDEADCHAR, flags)) {
		display.lastDead = true;
		display.lastVirtual = mapKey == 0;
		display.lastKey = display.lastVirtual ? (int)wParam : mapKey;
		return null;
	}

	// See corresponding code block in 'WM_KEYDOWN' for an explanation.
	boolean isCharPending = false;
	if (OS.PeekMessage (msg, hwnd, OS.WM_SYSCHAR, OS.WM_SYSCHAR, flags)) {
		isCharPending = true;
	}

	// See corresponding code block in 'WM_KEYDOWN' for an explanation.
	if (isDisposed ()) return LRESULT.ONE;

	display.lastVirtual = mapKey == 0 || display.numpadKey ((int)wParam) != 0;
	if (display.lastVirtual) {
		display.lastKey = (int)wParam;
		/*
		* Feature in Windows.  The virtual key VK_DELETE is not
		* treated as both a virtual key and an ASCII key by Windows.
		* Therefore, we will not receive a WM_SYSCHAR for this key.
		* The fix is to treat VK_DELETE as a special case and map
		* the ASCII value explicitly (Delete is 0x7F).
		*/
		if (display.lastKey == OS.VK_DELETE) display.lastAscii = 0x7F;

		if (OS.VK_NUMPAD0 <= display.lastKey && display.lastKey <= OS.VK_DIVIDE) {
			display.lastAscii = display.numpadKey (display.lastKey);
		}
	} else {
		/*
		* Convert LastKey to lower case because Windows non-virtual
		* keys that are also ASCII keys, such as like VK_A, are have
		* upper case values in WM_SYSKEYDOWN despite the fact that the
		* Shift was not pressed.
		*/
		display.lastKey = (int)OS.CharLower (OS.LOWORD (mapKey));
	}

	if (isCharPending) {
		return null;
	}

	if (!sendKeyEvent (SWT.KeyDown, OS.WM_SYSKEYDOWN, wParam, lParam)) {
		return LRESULT.ONE;
	}
	// widget could be disposed at this point
	return null;
}

LRESULT wmSysKeyUp (long hwnd, long wParam, long lParam) {
	return wmKeyUp (hwnd, wParam, lParam);
}

LRESULT wmXButtonDblClk (long hwnd, long wParam, long lParam) {
	/*
	* Feature in Windows. Windows sends the following
	* messages when the user double clicks the mouse:
	*
	*	WM_XBUTTONDOWN		- mouse down
	*	WM_XBUTTONUP		- mouse up
	*	WM_XLBUTTONDBLCLK	- double click
	*	WM_XBUTTONUP		- mouse up
	*
	* Applications that expect matching mouse down/up
	* pairs will not see the second mouse down.  The
	* fix is to send a mouse down event.
	*/
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	int button = OS.HIWORD (wParam) == OS.XBUTTON1 ? 4 : 5;
	sendMouseEvent (SWT.MouseDown, button, hwnd, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, button, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_XBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmXButtonDown (long hwnd, long wParam, long lParam) {
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	display.xMouse = true;
	int button = OS.HIWORD (wParam) == OS.XBUTTON1 ? 4 : 5;
	if (sendMouseEvent (SWT.MouseDown, button, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_XBUTTONDOWN, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmXButtonUp (long hwnd, long wParam, long lParam) {
	Display display = this.display;
	LRESULT result = null;
	int button = OS.HIWORD (wParam) == OS.XBUTTON1 ? 4 : 5;
	if (sendMouseEvent (SWT.MouseUp, button, hwnd, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_XBUTTONUP, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	/*
	* Bug in Windows.  On some machines that do not have XBUTTONs,
	* the MK_XBUTTON1 and OS.MK_XBUTTON2 bits are sometimes set,
	* causing mouse capture to become stuck.  The fix is to test
	* for the extra buttons only when they exist.
	*/
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON;
	if (display.xMouse) mask |= OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if ((wParam & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return result;
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

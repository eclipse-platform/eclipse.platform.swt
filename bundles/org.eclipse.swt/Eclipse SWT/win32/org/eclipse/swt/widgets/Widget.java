/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
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
	
	/* Default size for widgets */
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;

	/* Check and initialize the Common Controls DLL */
	static final int MAJOR = 5, MINOR = 80;
	static {
		if (!OS.IsWinCE) {
			if (OS.COMCTL32_VERSION < OS.VERSION (MAJOR, MINOR)) {
				System.out.println ("***WARNING: SWT requires comctl32.dll version " + MAJOR + "." + MINOR + " or greater"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				System.out.println ("***WARNING: Detected: " + OS.COMCTL32_MAJOR + "." + OS.COMCTL32_MINOR); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		OS.InitCommonControls ();
	}
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Widget () {
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

int /*long*/ callWindowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
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
	if (display.thread != Thread.currentThread ()) {
		/*
		* Bug in IBM JVM 1.6.  For some reason, under
		* conditions that are yet to be full understood,
		* Thread.currentThread() is either returning null
		* or a different instance from the one that was
		* saved when the Display was created.  This is
		* possibly a JIT problem because modifying this
		* method to print logging information when the
		* error happens seems to fix the problem.  The
		* fix is to use operating system calls to verify
		* that the current thread is not the Display thread.
		* 
		* NOTE: Despite the fact that Thread.currentThread()
		* is used in other places, the failure has not been
		* observed in all places where it is called. 
		*/
		if (display.threadId != OS.GetCurrentThreadId ()) {
			error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
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

int /*long*/ DeferWindowPos(int /*long*/ hWinPosInfo, int /*long*/ hWnd, int /*long*/ hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags){
	if (OS.IsWinCE) {
		/*
		* Feature in Windows.  On Windows CE, DeferWindowPos always causes
		* a WM_SIZE message, even when the new size is the same as the old
		* size.  The fix is to detect that the size has not changed and set
		* SWP_NOSIZE.
		*/
		if ((uFlags & OS.SWP_NOSIZE) == 0) {
			RECT lpRect = new RECT ();
			OS.GetWindowRect (hWnd, lpRect);
			if (cy == lpRect.bottom - lpRect.top && cx == lpRect.right - lpRect.left) {
				/*
				* Feature in Windows.  On Windows CE, DeferWindowPos when called
				* with SWP_DRAWFRAME always causes a WM_SIZE message, even
				* when SWP_NOSIZE is set and when the new size is the same as the
				* old size.  The fix is to clear SWP_DRAWFRAME when the size is
				* the same.
				*/
				uFlags &= ~OS.SWP_DRAWFRAME;
				uFlags |= OS.SWP_NOSIZE;
			}
		}
	}
	return OS.DeferWindowPos (hWinPosInfo, hWnd, hWndInsertAfter, X, Y, cx, cy, uFlags);
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

boolean dragDetect (int /*long*/ hwnd, int x, int y, boolean filter, boolean [] detect, boolean [] consume) {
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

Widget findItem (int /*long*/ id) {
	return null;
}

char [] fixMnemonic (String string) {
	return fixMnemonic (string, false);
}

char [] fixMnemonic (String string, boolean spaces) {
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

void mapEvent (int /*long*/ hwnd, Event event) {
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

boolean sendDragEvent (int button, int x, int y) {
	Event event = new Event ();
	event.button = button;
	event.x = x;
	event.y = y;
	setInputState (event, SWT.DragDetect);
	postEvent (SWT.DragDetect, event);
	if (isDisposed ()) return false;
	return event.doit;
}

boolean sendDragEvent (int button, int stateMask, int x, int y) {
	Event event = new Event ();
	event.button = button;
	event.x = x;
	event.y = y;
	event.stateMask = stateMask;
	postEvent (SWT.DragDetect, event);
	if (isDisposed ()) return false;
	return event.doit;
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

boolean sendKeyEvent (int type, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	Event event = new Event ();
	if (!setKeyState (event, type, wParam, lParam)) return true;
	return sendKeyEvent (type, msg, wParam, lParam, event);
}

boolean sendKeyEvent (int type, int msg, int /*long*/ wParam, int /*long*/ lParam, Event event) {
	sendEvent (type, event);
	if (isDisposed ()) return false;
	return event.doit;
}

boolean sendMouseEvent (int type, int button, int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	return sendMouseEvent (type, button, display.getClickCount (type, button, hwnd, lParam), 0, false, hwnd, msg, wParam, lParam);
}

boolean sendMouseEvent (int type, int button, int count, int detail, boolean send, int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (!hooks (type) && !filters (type)) return true;
	Event event = new Event ();
	event.button = button;
	event.detail = detail;
	event.count = count;
	event.x = OS.GET_X_LPARAM (lParam);
	event.y = OS.GET_Y_LPARAM (lParam);
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

boolean sendMouseWheelEvent (int type, int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	int delta = OS.GET_WHEEL_DELTA_WPARAM (wParam);
	int detail = 0;
	if (type == SWT.MouseWheel) {
		int [] linesToScroll = new int [1];
		OS.SystemParametersInfo (OS.SPI_GETWHEELSCROLLLINES, 0, linesToScroll, 0);
		if (linesToScroll [0] == OS.WHEEL_PAGESCROLL) {
			detail = SWT.SCROLL_PAGE;
		} else {
			detail = SWT.SCROLL_LINE;
			delta *= linesToScroll [0];
		}
		/* Check if the delta and the remainder have the same direction (sign) */
		if ((delta ^ display.scrollRemainder) >= 0) delta += display.scrollRemainder;
		display.scrollRemainder = delta % OS.WHEEL_DELTA;
	} else {
		/* Check if the delta and the remainder have the same direction (sign) */
		if ((delta ^ display.scrollHRemainder) >= 0) delta += display.scrollHRemainder;
		display.scrollHRemainder = delta % OS.WHEEL_DELTA;
		
		delta = -delta;
	}

	if (!hooks (type) && !filters (type)) return true;
	int count = delta / OS.WHEEL_DELTA;
	POINT pt = new POINT ();
	OS.POINTSTOPOINT (pt, lParam);
	OS.ScreenToClient (hwnd, pt);
	lParam = OS.MAKELPARAM (pt.x, pt.y);
	return sendMouseEvent (type, 0, count, detail, true, hwnd, OS.WM_MOUSEWHEEL, wParam, lParam);
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

boolean setKeyState (Event event, int type, int /*long*/ wParam, int /*long*/ lParam) {
	
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
	if (display.lastAscii != 0 || display.lastNull) {
		event.character = Display.mbcsToWcs ((char) display.lastAscii);
	}
	if (event.keyCode == 0 && event.character == 0) {
		if (!display.lastNull) return false;
	}
	return setInputState (event, type);
}

int setLocationMask (Event event, int type, int /*long*/ wParam, int /*long*/ lParam) {
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

boolean SetWindowPos (int /*long*/ hWnd, int /*long*/ hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags) {
	if (OS.IsWinCE) {
		/*
		* Feature in Windows.  On Windows CE, SetWindowPos() always causes
		* a WM_SIZE message, even when the new size is the same as the old
		* size.  The fix is to detect that the size has not changed and set
		* SWP_NOSIZE.
		*/
		if ((uFlags & OS.SWP_NOSIZE) == 0) {
			RECT lpRect = new RECT ();
			OS.GetWindowRect (hWnd, lpRect);
			if (cy == lpRect.bottom - lpRect.top && cx == lpRect.right - lpRect.left) {
				/*
				* Feature in Windows.  On Windows CE, SetWindowPos() when called
				* with SWP_DRAWFRAME always causes a WM_SIZE message, even
				* when SWP_NOSIZE is set and when the new size is the same as the
				* old size.  The fix is to clear SWP_DRAWFRAME when the size is
				* the same.
				*/
				uFlags &= ~OS.SWP_DRAWFRAME;
				uFlags |= OS.SWP_NOSIZE;
			}
		}
	}
	return OS.SetWindowPos (hWnd, hWndInsertAfter, X, Y, cx, cy, uFlags);
}

boolean showMenu (int x, int y) {
	Event event = new Event ();
	event.x = x;
	event.y = y;
	sendEvent (SWT.MenuDetect, event);
	// widget could be disposed at this point
	if (isDisposed ()) return false;
	if (!event.doit) return true;
	Menu menu = getMenu ();
	if (menu != null && !menu.isDisposed ()) {
		if (x != event.x || y != event.y) {
			menu.setLocation (event.x, event.y);
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
public String toString () {
	String string = "*Disposed*"; //$NON-NLS-1$
	if (!isDisposed ()) {
		string = "*Wrong Thread*"; //$NON-NLS-1$
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}"; //$NON-NLS-1$ //$NON-NLS-2$
}

LRESULT wmCaptureChanged (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	display.captureChanged = true;
	return null;
}

LRESULT wmChar (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Do not report a lead byte as a key pressed.
	*/
	if (!OS.IsUnicode && OS.IsDBLocale) {
		byte lead = (byte) (wParam & 0xFF);
		if (OS.IsDBCSLeadByte (lead)) return null;
	}
	display.lastAscii = (int)/*64*/wParam;
	display.lastNull = wParam == 0;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_CHAR, wParam, lParam)) {
		return LRESULT.ONE;
	}
	// widget could be disposed at this point
	return null;
}

LRESULT wmContextMenu (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	if (wParam != hwnd) return null;
	
	/*
	* Feature in Windows.  SHRecognizeGesture() sends an undocumented
	* WM_CONTEXTMENU notification when the flag SHRG_NOTIFY_PARENT is
	* not set.  This causes the context menu to be displayed twice,
	* once by the caller of SHRecognizeGesture() and once from this
	* method.  The fix is to ignore WM_CONTEXTMENU notifications on
	* all WinCE platforms.
	* 
	* NOTE: This only happens on WM2003.  Previous WinCE versions did
	* not support WM_CONTEXTMENU.
	*/
	if (OS.IsWinCE) return null;
	
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
	int x = 0, y = 0;
	if (lParam != -1) {
		POINT pt = new POINT ();
		OS.POINTSTOPOINT (pt, lParam);
		x = pt.x;
		y = pt.y;
		OS.ScreenToClient (hwnd, pt);
		RECT rect = new RECT ();
		OS.GetClientRect (hwnd, rect);
		if (!OS.PtInRect (rect, pt)) return null;
	} else {
		int pos = OS.GetMessagePos ();
		x = OS.GET_X_LPARAM (pos);
		y = OS.GET_Y_LPARAM (pos);
	}

	/* Show the menu */
	return showMenu (x, y) ? LRESULT.ZERO : null;
}

LRESULT wmIMEChar (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	display.lastKey = 0;
	display.lastAscii = (int)/*64*/wParam;
	display.lastVirtual = display.lastNull = display.lastDead = false;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
		return LRESULT.ONE;
	}
	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	return LRESULT.ONE;
}

LRESULT wmKeyDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	
	/* Ignore repeating modifier keys by testing key down state */
	switch ((int)/*64*/wParam) {
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
	display.lastVirtual = display.lastNull = display.lastDead = false;
	
	/*
	* Do not report a lead byte as a key pressed.
	*/
	if (!OS.IsUnicode && OS.IsDBLocale) {
		byte lead = (byte) (wParam & 0xFF);
		if (OS.IsDBCSLeadByte (lead)) return null;
	}
	
	/* Map the virtual key */
	/*
	* Bug in WinCE.  MapVirtualKey() returns incorrect values.
	* The fix is to rely on a key mappings table to determine
	* whether the key event must be sent now or if a WM_CHAR
	* event will follow.  The key mappings table maps virtual
	* keys to SWT key codes and does not contain mappings for
	* Windows virtual keys like VK_A.  Virtual keys that are
	* both virtual and ASCII are a special case.
	*/
	int mapKey = 0;
	if (OS.IsWinCE) {
		switch ((int)/*64*/wParam) {
			case OS.VK_BACK: mapKey = SWT.BS; break;
			case OS.VK_RETURN: mapKey = SWT.CR; break;
			case OS.VK_DELETE: mapKey = SWT.DEL; break;
			case OS.VK_ESCAPE: mapKey = SWT.ESC; break;
			case OS.VK_TAB: mapKey = SWT.TAB; break;
		}
	} else {
		mapKey = OS.MapVirtualKey ((int)/*64*/wParam, 2);
		/*
		* Feature in Windows.  For Devanagari and Bengali numbers,
		* MapVirtualKey() returns the localized number instead of 
		* the ASCII equivalent.  For example, MapVirtualKey()
		* maps VK_1 on the numbers keyboard to \u0967, which is
		* the Devanagari digit '1', but not ASCII.
		* The fix is to test for Devanagari and Bengali digits and 
		* map these explicitly.
		* 
		* NOTE: VK_0 to VK_9 are the same as ASCII.
		*/
		if (('\u09e6' <= mapKey && mapKey <= '\u09ef') || ('\u0966' <= mapKey && mapKey <= '\u096f')) {
			mapKey = (int)/*64*/wParam;
		}
	}

	/*
	* Bug in Windows 95 and NT.  When the user types an accent key such
	* as ^ to get an accented character on a German keyboard, the accent
	* key should be ignored and the next key that the user types is the
	* accented key.  The fix is to detect the accent key stroke (called
	* a dead key) by testing the high bit of the value returned by
	* MapVirtualKey().  A further problem is that the high bit on
	* Windows NT is bit 32 while the high bit on Windows 95 is bit 16.
	* They should both be bit 32.
	*
	* When the user types an accent key that does not correspond to a
	* virtual key, MapVirtualKey() won't set the high bit to indicate
	* a dead key.  This happens when an accent key, such as '^' is the
	* result of a modifier such as Shift key and MapVirtualKey() always
	* returns the unshifted key.  The fix is to peek for a WM_DEADCHAR
	* and avoid issuing the event. 
	*/
	if (OS.IsWinNT) {
		if ((mapKey & 0x80000000) != 0) return null;
	} else {
		if ((mapKey & 0x8000) != 0) return null;
	}
	MSG msg = new MSG ();
	int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
	if (OS.PeekMessage (msg, hwnd, OS.WM_DEADCHAR, OS.WM_DEADCHAR, flags)) {
		display.lastDead = true;
		display.lastVirtual = mapKey == 0;
		display.lastKey = display.lastVirtual ? (int)/*64*/wParam : mapKey;
		return null;
	}
	
	/*
	*  Bug in Windows.  Somehow, the widget is becoming disposed after
	*  calling PeekMessage().  In rare circumstances, it seems that
	*  PeekMessage() can allow SWT listeners to run that might contain
	*  application code that disposes the widget.  It is not exactly
	*  clear how this can happen.  PeekMessage() is only looking for
	*  WM_DEADCHAR.  It is not dispatching any message that it finds
	*  or removing any message from the queue.  Cross-thread messages
	*  are disabled.  The fix is to check for a disposed widget and
	*  return without calling the window proc.
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
	* 
	* NOTE: On Windows 98, keypad keys are virtual despite the
	* fact that a WM_CHAR is issued.  On Windows 2000 and XP,
	* they are not virtual.  Therefore it is necessary to force
	* numeric keypad keys to be virtual.
	*/
	display.lastVirtual = mapKey == 0 || display.numpadKey ((int)/*64*/wParam) != 0;
	if (display.lastVirtual) {
		display.lastKey = (int)/*64*/wParam;
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
			/*
			* Feature in Windows.  Calling to ToAscii() or ToUnicode(), clears
			* the accented state such that the next WM_CHAR loses the accent.
			* This makes is critical that the accent key is detected.  Also,
			* these functions clear the character that is entered using the
			* special Windows keypad sequence when NumLock is down (ie. typing 
			* ALT+0231 should gives 'c' with a cedilla when NumLock is down).
			*/
			if (display.asciiKey (display.lastKey) != 0) return null;
			display.lastAscii = display.numpadKey (display.lastKey);
		}
	} else {
		/*
		* Convert LastKey to lower case because Windows non-virtual
		* keys that are also ASCII keys, such as like VK_A, are have
		* upper case values in WM_KEYDOWN despite the fact that the 
		* Shift was not pressed.
		*/
	 	display.lastKey = (int)/*64*/OS.CharLower ((short) mapKey);

		/*
		* Feature in Windows. The virtual key VK_CANCEL is treated
		* as both a virtual key and ASCII key by Windows.  This
		* means that a WM_CHAR with WPARAM=3 will be issued for
		* this key.  In order to distinguish between this key and
		* Ctrl+C, mark the key as virtual.
		*/
		if (wParam == OS.VK_CANCEL) display.lastVirtual = true;
		
		/*
		* Some key combinations map to Windows ASCII keys depending
		* on the keyboard.  For example, Ctrl+Alt+Q maps to @ on a
		* German keyboard.  If the current key combination is special,
		* the correct character is placed in wParam for processing in
		* WM_CHAR.  If this is the case, issue the key down event from
		* inside WM_CHAR.
		*/
		int asciiKey = display.asciiKey ((int)/*64*/wParam);
		if (asciiKey != 0) {
			/*
			* When the user types Ctrl+Space, ToAscii () maps this to
			* Space.  Normally, ToAscii () maps a key to a different
			* key if both a WM_KEYDOWN and a WM_CHAR will be issued.
			* To avoid the extra SWT.KeyDown, look for a space and
			* issue the event from WM_CHAR.
			*/
			if (asciiKey == ' ') return null;
			if (asciiKey != (int)/*64*/wParam) return null;
			/*
			* Feature in Windows. The virtual key VK_CANCEL is treated
			* as both a virtual key and ASCII key by Windows.  This
			* means that a WM_CHAR with WPARAM=3 will be issued for
			* this key. To avoid the extra SWT.KeyDown, look for
			* VK_CANCEL and issue the event from WM_CHAR.
			*/
			if (wParam == OS.VK_CANCEL) return null;
		}
		
		/*
		* If the control key is not down at this point, then
		* the key that was pressed was an accent key or a regular
		* key such as 'A' or Shift+A.  In that case, issue the
		* key event from WM_CHAR.
		*/
		if (OS.GetKeyState (OS.VK_CONTROL) >= 0) return null;
		
		/*
		* Get the shifted state or convert to lower case if necessary.
		* If the user types Ctrl+A, LastAscii should be 'a', not 'A'. 
		* If the user types Ctrl+Shift+A, LastAscii should be 'A'.
		* If the user types Ctrl+Shift+6, the value of LastAscii will
		* depend on the international keyboard.
		*/
	 	if (OS.GetKeyState (OS.VK_SHIFT) < 0) {
			display.lastAscii = display.shiftedKey ((int)/*64*/wParam);
			if (display.lastAscii == 0) display.lastAscii = mapKey;
	 	} else {
	 		display.lastAscii = (int)/*64*/OS.CharLower ((short) mapKey);
	 	}
	 			
		/* Note that Ctrl+'@' is ASCII NUL and is delivered in WM_CHAR */
		if (display.lastAscii == '@') return null;
		display.lastAscii = display.controlKey (display.lastAscii);
	}
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_KEYDOWN, wParam, lParam)) {
		return LRESULT.ONE;
	}
	// widget could be disposed at this point
	return null;
}

LRESULT wmKeyUp (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	
	/* Check for hardware keys */
	if (OS.IsWinCE) {
		if (OS.VK_APP1 <= wParam && wParam <= OS.VK_APP6) {
			display.lastKey = display.lastAscii = 0;
			display.lastVirtual = display.lastNull = display.lastDead = false;
			Event event = new Event ();
			event.detail = (int)/*64*/wParam - OS.VK_APP1 + 1;
			/* Check the bit 30 to get the key state */
			int type = (lParam & 0x40000000) != 0 ? SWT.HardKeyUp : SWT.HardKeyDown;
			if (setInputState (event, type)) sendEvent (type, event);
			// widget could be disposed at this point
			return null;
		}
	}
	
	/*
	* If the key up is not hooked, reset last key
	* and last ascii in case the key down is hooked.
	*/
	if (!hooks (SWT.KeyUp) && !display.filters (SWT.KeyUp)) {
		display.lastKey = display.lastAscii = 0;
		display.lastVirtual = display.lastNull = display.lastDead = false;
		return null;
	}
	
	/* Map the virtual key. */
	/*
	* Bug in WinCE.  MapVirtualKey() returns incorrect values.
	* The fix is to rely on a key mappings table to determine
	* whether the key event must be sent now or if a WM_CHAR
	* event will follow.  The key mappings table maps virtual
	* keys to SWT key codes and does not contain mappings for
	* Windows virtual keys like VK_A.  Virtual keys that are
	* both virtual and ASCII are a special case.
	*/
	int mapKey = 0;
	if (OS.IsWinCE) {
		switch ((int)/*64*/wParam) {
			case OS.VK_BACK: mapKey = SWT.BS; break;
			case OS.VK_RETURN: mapKey = SWT.CR; break;
			case OS.VK_DELETE: mapKey = SWT.DEL; break;
			case OS.VK_ESCAPE: mapKey = SWT.ESC; break;
			case OS.VK_TAB: mapKey = SWT.TAB; break;
		}
	} else {
		mapKey = OS.MapVirtualKey ((int)/*64*/wParam, 2);
	}

	/*
	* Bug in Windows 95 and NT.  When the user types an accent key such
	* as ^ to get an accented character on a German keyboard, the accent
	* key should be ignored and the next key that the user types is the
	* accented key. The fix is to detect the accent key stroke (called
	* a dead key) by testing the high bit of the value returned by
	* MapVirtualKey ().  A further problem is that the high bit on
	* Windows NT is bit 32 while the high bit on Windows 95 is bit 16.
	* They should both be bit 32.
	*/
	if (OS.IsWinNT) {
		if ((mapKey & 0x80000000) != 0) return null;
	} else {
		if ((mapKey & 0x8000) != 0) return null;
	}
	if (display.lastDead) return null;

	/*
	* NOTE: On Windows 98, keypad keys are virtual despite the
	* fact that a WM_CHAR is issued.  On Windows 2000 and XP,
	* they are not virtual.  Therefore it is necessary to force
	* numeric keypad keys to be virtual.
	*/
	display.lastVirtual = mapKey == 0 || display.numpadKey ((int)/*64*/wParam) != 0;
	if (display.lastVirtual) {
		display.lastKey = (int)/*64*/wParam;
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
			display.lastNull = display.lastDead = false;
			return null;
		}
	}
	LRESULT result = null;
	if (!sendKeyEvent (SWT.KeyUp, OS.WM_KEYUP, wParam, lParam)) {
		result = LRESULT.ONE;
	}
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	display.lastVirtual = display.lastNull = display.lastDead = false;
	return result;
}

LRESULT wmKillFocus (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	display.scrollRemainder = display.scrollHRemainder = 0;
	int /*long*/ code = callWindowProc (hwnd, OS.WM_KILLFOCUS, wParam, lParam);
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

LRESULT wmLButtonDblClk (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
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
	sendMouseEvent (SWT.MouseDown, 1, hwnd, OS.WM_LBUTTONDOWN, wParam, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, 1, hwnd, OS.WM_LBUTTONDBLCLK, wParam, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_LBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmLButtonDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	LRESULT result = null;
	int x = OS.GET_X_LPARAM (lParam);
	int y = OS.GET_Y_LPARAM (lParam);
	boolean [] consume = null, detect = null;
	boolean dragging = false, mouseDown = true;
	int count = display.getClickCount (SWT.MouseDown, 1, hwnd, lParam);
	if (count == 1 && (state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect)) {
		if (!OS.IsWinCE) {
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
	}
	display.captureChanged = false;
	boolean dispatch = sendMouseEvent (SWT.MouseDown, 1, count, 0, false, hwnd, OS.WM_LBUTTONDOWN, wParam, lParam);
	if (dispatch && (consume == null || !consume [0])) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_LBUTTONDOWN, wParam, lParam));	
	} else {
		result = LRESULT.ZERO;
	}
	if (OS.IsPPC) {
		/*
		* Note: On WinCE PPC, only attempt to recognize the gesture for
		* a context menu when the control contains a valid menu or there
		* are listeners for the MenuDetect event.
		*/
		Menu menu = getMenu ();
		boolean hasMenu = menu != null && !menu.isDisposed ();
		if (hasMenu || hooks (SWT.MenuDetect)) {
			SHRGINFO shrg = new SHRGINFO ();
			shrg.cbSize = SHRGINFO.sizeof;
			shrg.hwndClient = hwnd;
			shrg.ptDown_x = x;
			shrg.ptDown_y = y; 
			shrg.dwFlags = OS.SHRG_RETURNCMD;
			int type = OS.SHRecognizeGesture (shrg);
			if (type == OS.GN_CONTEXTMENU) showMenu (x, y);
		}
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

LRESULT wmLButtonUp (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	LRESULT result = null;
	if (sendMouseEvent (SWT.MouseUp, 1, hwnd, OS.WM_LBUTTONUP, wParam, lParam)) {
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

LRESULT wmMButtonDblClk (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
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
	sendMouseEvent (SWT.MouseDown, 2, hwnd, OS.WM_MBUTTONDOWN, wParam, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, 2, hwnd, OS.WM_MBUTTONDBLCLK, wParam, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_MBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmMButtonDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	if (sendMouseEvent (SWT.MouseDown, 2, hwnd, OS.WM_MBUTTONDOWN, wParam, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_MBUTTONDOWN, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmMButtonUp (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	LRESULT result = null;
	if (sendMouseEvent (SWT.MouseUp, 2, hwnd, OS.WM_MBUTTONUP, wParam, lParam)) {
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

LRESULT wmMouseHover (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	if (!sendMouseEvent (SWT.MouseHover, 0, hwnd, OS.WM_MOUSEHOVER, wParam, lParam)) {
		return LRESULT.ZERO;
	}
	return null;
}

LRESULT wmMouseLeave (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	if (!hooks (SWT.MouseExit) && !filters (SWT.MouseExit)) return null;
	int pos = OS.GetMessagePos ();
	POINT pt = new POINT ();
	OS.POINTSTOPOINT (pt, pos);
	OS.ScreenToClient (hwnd, pt);
	lParam = OS.MAKELPARAM (pt.x, pt.y);
	if (!sendMouseEvent (SWT.MouseExit, 0, hwnd, OS.WM_MOUSELEAVE, wParam, lParam)) {
		return LRESULT.ZERO;
	}
	return null;
}

LRESULT wmMouseMove (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = null;
	Display display = this.display;
	int pos = OS.GetMessagePos ();
	if (pos != display.lastMouse || display.captureChanged) {
		if (!OS.IsWinCE) {
			boolean trackMouse = (state & TRACK_MOUSE) != 0;
			boolean mouseEnter = hooks (SWT.MouseEnter) || display.filters (SWT.MouseEnter);
			boolean mouseExit = hooks (SWT.MouseExit) || display.filters (SWT.MouseExit);
			boolean mouseHover = hooks (SWT.MouseHover) || display.filters (SWT.MouseHover);
			if (trackMouse || mouseEnter || mouseExit || mouseHover) {
				TRACKMOUSEEVENT lpEventTrack = new TRACKMOUSEEVENT ();
				lpEventTrack.cbSize = TRACKMOUSEEVENT.sizeof;
				lpEventTrack.dwFlags = OS.TME_QUERY;
				lpEventTrack.hwndTrack = hwnd;
				OS.TrackMouseEvent (lpEventTrack);
				if (lpEventTrack.dwFlags == 0) {
					lpEventTrack.dwFlags = OS.TME_LEAVE | OS.TME_HOVER;
					lpEventTrack.hwndTrack = hwnd;
					OS.TrackMouseEvent (lpEventTrack);
					if (mouseEnter) {
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
						sendMouseEvent (SWT.MouseEnter, 0, hwnd, OS.WM_MOUSEMOVE, wParam, lParam);
					}
				} else {
					lpEventTrack.dwFlags = OS.TME_HOVER;
					OS.TrackMouseEvent (lpEventTrack);
				}
			}
		}
		if (pos != display.lastMouse) {
			display.lastMouse = pos;
			if (!sendMouseEvent (SWT.MouseMove, 0, hwnd, OS.WM_MOUSEMOVE, wParam, lParam)) {
				result = LRESULT.ZERO;
			}
		}
	} 
	display.captureChanged = false;
	return result;
}

LRESULT wmMouseWheel (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	return sendMouseWheelEvent(SWT.MouseWheel, hwnd, wParam, lParam) ? null : LRESULT.ZERO;
}

LRESULT wmMouseHWheel (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	return sendMouseWheelEvent(SWT.MouseHorizontalWheel, hwnd, wParam, lParam) ? null : LRESULT.ZERO;
}

LRESULT wmNCPaint (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	return null;
}

LRESULT wmPaint (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {

	/* Exit early - don't draw the background */
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) {
		return null;
	}
	
	/* Issue a paint event */
	int /*long*/ result = 0;
	if (OS.IsWinCE) {
		RECT rect = new RECT ();
		OS.GetUpdateRect (hwnd, rect, false);
		result = callWindowProc (hwnd, OS.WM_PAINT, wParam, lParam);
		/*
		* Bug in Windows.  When InvalidateRgn(), InvalidateRect()
		* or RedrawWindow() with RDW_INVALIDATE is called from
		* within WM_PAINT to invalidate a region for a further
		* BeginPaint(), the caret is not properly erased causing
		* pixel corruption.  The fix is to hide and show the
		* caret.
		*/
		OS.HideCaret (hwnd);
		OS.InvalidateRect (hwnd, rect, false);
		OS.ShowCaret (hwnd);
		PAINTSTRUCT ps = new PAINTSTRUCT ();
		GCData data = new GCData ();
		data.ps = ps;
		data.hwnd = hwnd;
		GC gc = new_GC (data);
		if (gc != null) {
			int width = ps.right - ps.left;
			int height = ps.bottom - ps.top;
			if (width != 0 && height != 0) {
				Event event = new Event ();
				event.gc = gc;
				event.x = ps.left;
				event.y = ps.top;
				event.width = width;
				event.height = height;
				sendEvent (SWT.Paint, event);
				// widget could be disposed at this point
				event.gc = null;
			}
			gc.dispose ();
		}
	} else {
		int /*long*/ rgn = OS.CreateRectRgn (0, 0, 0, 0);
		OS.GetUpdateRgn (hwnd, rgn, false);
		result = callWindowProc (hwnd, OS.WM_PAINT, wParam, lParam);
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
				int /*long*/ hDC = gc.handle;
				OS.SelectClipRgn (hDC, rgn);
				OS.SetMetaRgn (hDC);
				Event event = new Event ();
				event.gc = gc;
				event.x = rect.left;
				event.y = rect.top;
				event.width = width;
				event.height = height;
				sendEvent (SWT.Paint, event);
				// widget could be disposed at this point
				event.gc = null;
			}
			gc.dispose ();
			OS.ShowCaret (hwnd);
		}
		OS.DeleteObject (rgn);
	}
	if (result == 0) return LRESULT.ZERO;
	return new LRESULT (result);
}

LRESULT wmPrint (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Bug in Windows.  When WM_PRINT is used to print the contents
	* of a control that has WS_EX_CLIENTEDGE, the old 3D border is
	* drawn instead of the theme border.  The fix is to call the
	* default window proc and then draw the theme border on top.
	*/
	if ((lParam & OS.PRF_NONCLIENT) != 0) {
		if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
			int bits = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
			if ((bits & OS.WS_EX_CLIENTEDGE) != 0) {
				int /*long*/ code = callWindowProc (hwnd, OS.WM_PRINT, wParam, lParam);
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

LRESULT wmRButtonDblClk (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
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
	sendMouseEvent (SWT.MouseDown, 3, hwnd, OS.WM_RBUTTONDOWN, wParam, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, 3, hwnd, OS.WM_RBUTTONDBLCLK, wParam, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_RBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmRButtonDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	if (sendMouseEvent (SWT.MouseDown, 3, hwnd, OS.WM_RBUTTONDOWN, wParam, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_RBUTTONDOWN, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmRButtonUp (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	LRESULT result = null;
	if (sendMouseEvent (SWT.MouseUp, 3, hwnd, OS.WM_RBUTTONUP, wParam, lParam)) {
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

LRESULT wmSetFocus (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	int /*long*/ code = callWindowProc (hwnd, OS.WM_SETFOCUS, wParam, lParam);
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

LRESULT wmSysChar (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	display.lastAscii = (int)/*64*/wParam;
	display.lastNull = wParam == 0;

	/* Do not issue a key down if a menu bar mnemonic was invoked */
	if (!hooks (SWT.KeyDown) && !display.filters (SWT.KeyDown)) {
		return null;
	}
	
	/* Call the window proc to determine whether it is a system key or mnemonic */
	boolean oldKeyHit = display.mnemonicKeyHit;
	display.mnemonicKeyHit = true;
	int /*long*/ result = callWindowProc (hwnd, OS.WM_SYSCHAR, wParam, lParam);
	boolean consumed = false;
	if (!display.mnemonicKeyHit) {
		consumed = !sendKeyEvent (SWT.KeyDown, OS.WM_SYSCHAR, wParam, lParam);
		// widget could be disposed at this point
	}
	consumed |= display.mnemonicKeyHit;
	display.mnemonicKeyHit = oldKeyHit;
	return consumed ? LRESULT.ONE : new LRESULT (result);
}

LRESULT wmSysKeyDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
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
	switch ((int)/*64*/wParam) {
		case OS.VK_F4: {
			int /*long*/ hwndShell = hwnd;
			while (OS.GetParent (hwndShell) != 0) {
				if (OS.GetWindow (hwndShell, OS.GW_OWNER) != 0) break;
				hwndShell = OS.GetParent (hwndShell);
			}
			int bits = OS.GetWindowLong (hwndShell, OS.GWL_STYLE);
			if ((bits & OS.WS_SYSMENU) != 0) return null;
		}
	}
	
	/* Ignore repeating modifier keys by testing key down state */
	switch ((int)/*64*/wParam) {
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
	display.lastVirtual = display.lastNull = display.lastDead = false;

	/* If are going to get a WM_SYSCHAR, ignore this message. */
	/*
	* Bug in WinCE.  MapVirtualKey() returns incorrect values.
	* The fix is to rely on a key mappings table to determine
	* whether the key event must be sent now or if a WM_CHAR
	* event will follow.  The key mappings table maps virtual
	* keys to SWT key codes and does not contain mappings for
	* Windows virtual keys like VK_A.  Virtual keys that are
	* both virtual and ASCII are a special case.
	*/
	int mapKey = 0;
	if (OS.IsWinCE) {
		switch ((int)/*64*/wParam) {
			case OS.VK_BACK: mapKey = SWT.BS; break;
			case OS.VK_RETURN: mapKey = SWT.CR; break;
			case OS.VK_DELETE: mapKey = SWT.DEL; break;
			case OS.VK_ESCAPE: mapKey = SWT.ESC; break;
			case OS.VK_TAB: mapKey = SWT.TAB; break;
		}
	} else {
		mapKey = OS.MapVirtualKey ((int)/*64*/wParam, 2);
	}
	display.lastVirtual = mapKey == 0 || display.numpadKey ((int)/*64*/wParam) != 0;
	if (display.lastVirtual) {
	 	display.lastKey = (int)/*64*/wParam;
		/*
		* Feature in Windows.  The virtual key VK_DELETE is not
		* treated as both a virtual key and an ASCII key by Windows.
		* Therefore, we will not receive a WM_SYSCHAR for this key.
		* The fix is to treat VK_DELETE as a special case and map
		* the ASCII value explicitly (Delete is 0x7F).
		*/
		if (display.lastKey == OS.VK_DELETE) display.lastAscii = 0x7F;

		/* When a keypad key is typed, a WM_SYSCHAR is not issued */
		if (OS.VK_NUMPAD0 <= display.lastKey && display.lastKey <= OS.VK_DIVIDE) {
			/*
			* A WM_SYSCHAR will be issued for '*', '+', '-', '.' and '/'
			* on the numeric keypad.  Avoid issuing the key event twice
			* by checking for these keys.  Note that calling to ToAscii()
			* or ToUnicode(), clear the character that is entered using
			* the special Windows keypad sequence when NumLock is down
			* (ie. typing ALT+0231 should gives 'c' with a cedilla when
			* NumLock is down).  Do not call either of these from here.
			*/
			switch (display.lastKey) {
				case OS.VK_MULTIPLY:
				case OS.VK_ADD:
				case OS.VK_SUBTRACT:
				case OS.VK_DECIMAL:
				case OS.VK_DIVIDE: return null;
			}
			display.lastAscii = display.numpadKey (display.lastKey);
		}
	} else {
		/*
		* Convert LastKey to lower case because Windows non-virtual
		* keys that are also ASCII keys, such as like VK_A, are have
		* upper case values in WM_SYSKEYDOWN despite the fact that the 
		* Shift was not pressed.
		*/
	 	display.lastKey = (int)/*64*/OS.CharLower ((short) mapKey);

		/*
		* Feature in Windows 98.  MapVirtualKey() indicates that
		* a WM_SYSCHAR message will occur for Alt+Enter but
		* this message never happens.  The fix is to issue the
		* event from WM_SYSKEYDOWN and map VK_RETURN to '\r'.
		*/
		if (OS.IsWinNT) return null;
		if (wParam != OS.VK_RETURN) return null;
		display.lastAscii = '\r';
	}

	if (!sendKeyEvent (SWT.KeyDown, OS.WM_SYSKEYDOWN, wParam, lParam)) {
		return LRESULT.ONE;
	}
	// widget could be disposed at this point
	return null;
}

LRESULT wmSysKeyUp (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	return wmKeyUp (hwnd, wParam, lParam);
}

LRESULT wmXButtonDblClk (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
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
	sendMouseEvent (SWT.MouseDown, button, hwnd, OS.WM_XBUTTONDOWN, wParam, lParam);
	if (sendMouseEvent (SWT.MouseDoubleClick, button, hwnd, OS.WM_XBUTTONDBLCLK, wParam, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_XBUTTONDBLCLK, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmXButtonDown (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = null;
	Display display = this.display;
	display.captureChanged = false;
	display.xMouse = true;
	int button = OS.HIWORD (wParam) == OS.XBUTTON1 ? 4 : 5;
	if (sendMouseEvent (SWT.MouseDown, button, hwnd, OS.WM_XBUTTONDOWN, wParam, lParam)) {
		result = new LRESULT (callWindowProc (hwnd, OS.WM_XBUTTONDOWN, wParam, lParam));
	} else {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	return result;
}

LRESULT wmXButtonUp (int /*long*/ hwnd, int /*long*/ wParam, int /*long*/ lParam) {
	Display display = this.display;
	LRESULT result = null;
	int button = OS.HIWORD (wParam) == OS.XBUTTON1 ? 4 : 5;
	if (sendMouseEvent (SWT.MouseUp, button, hwnd, OS.WM_XBUTTONUP, wParam, lParam)) {
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
}

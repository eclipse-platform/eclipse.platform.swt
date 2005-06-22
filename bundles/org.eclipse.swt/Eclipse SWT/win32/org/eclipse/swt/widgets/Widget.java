/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
	static final int LAYOUT_NEEDED	= 1<<5;
	static final int LAYOUT_CHANGED = 1<<6;
	
	/* Default widths for widgets */
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
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs. When the
 * event does occur in the widget, the listener is notified by
 * sending it the <code>handleEvent()</code> message.
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
 * @see #removeListener
 */
public void addListener (int eventType, Listener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when the widget is disposed. When the widget is
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

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
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
 * descendents are also destroyed by the operating system.
 * This means that it is only necessary to call <code>destroyWidget</code>
 * on the root of the widget tree.
 * </p><p>
 * This method is called after <code>releaseWidget</code>.
 * </p>
 * @see #dispose
 * @see #releaseChild
 * @see #releaseWidget
 * @see #releaseHandle
 */
void destroyWidget () {
	releaseHandle ();
}

int DeferWindowPos(int hWinPosInfo, int hWnd, int hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags){
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
 * the receiver and all its descendents. After this method has
 * been invoked, the receiver and all descendents will answer
 * <code>true</code> when sent the message <code>isDisposed()</code>.
 * Any internal connections between the widgets in the tree will
 * have been removed to facilitate garbage collection.
 * <p>
 * NOTE: This method is not called recursively on the descendents
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
	releaseChild ();
	releaseWidget ();
	destroyWidget ();
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

Widget findItem (int id) {
	return null;
}

char [] fixMnemonic (String string) {
	char [] buffer = new char [string.length ()];
	string.getChars (0, string.length (), buffer, 0);
	int i = 0, j = 0;
	while (i < buffer.length) {
		if (buffer [i] == '&') {
			if (i + 1 < buffer.length && buffer [i + 1] == '&') {
				buffer [j++] = ' ';
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
 * invoke any other method using the widget.
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
 * and <code>false</code> otherwise.
 *
 * @param	eventType the type of event
 * @return true if the event is hooked
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
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

void mapEvent (int hwnd, Event event) {
}

GC new_GC (GCData data) {
	return null;
}

/**
 * Notifies all of the receiver's listeners for events
 * of the given type that one such event has occurred by
 * invoking their <code>handleEvent()</code> method.
 *
 * @param eventType the type of event which has occurred
 * @param event the event data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
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
 * Releases the receiver, a child in a widget hierarchy,
 * from its parent.
 * <p>
 * When a widget is destroyed, it may be necessary to remove
 * it from an internal data structure of the parent. When
 * a widget has no handle, it may also be necessary for the
 * parent to hide the widget or otherwise indicate that the
 * widget has been disposed. For example, disposing a menu
 * bar requires that the menu bar first be released from the
 * shell when the menu bar is active.  This could not be done
 * in <code>destroyWidget</code> for the menu bar because the
 * parent shell as well as other fields have been null'd out
 * already by <code>releaseWidget</code>.
 * </p>
 * This method is called first when a widget is disposed.
 * 
 * @see #dispose
 * @see #releaseChild
 * @see #releaseWidget
 * @see #releaseHandle
 */
void releaseChild () {
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
 * @see #releaseChild
 * @see #releaseWidget
 * @see #releaseHandle
 */
void releaseHandle () {
	state |= DISPOSED;
	display = null;
}

void releaseResources () {
	releaseWidget ();
	releaseHandle ();
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
 * <p>
 * Typically, a widget with children will broadcast this
 * message to all children so that they too can release their
 * resources.  The <code>releaseHandle</code> method is used
 * as part of this broadcast to zero the handle fields of the
 * children without calling <code>destroyWidget</code>.  In
 * this scenario, the children are actually destroyed later,
 * when the operating system destroys the widget tree.
 * </p>
 * This method is called after <code>releaseChild</code>.
 * 
 * @see #dispose
 * @see #releaseChild
 * @see #releaseWidget
 * @see #releaseHandle
 */
void releaseWidget () {
	sendEvent (SWT.Dispose);
	eventTable = null;
	data = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
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
 */
public void removeListener (int eventType, Listener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the SWT
 * public API. It is marked public only so that it can be shared
 * within the packages provided by SWT. It should never be
 * referenced from application code.
 * </p>
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
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
 */
protected void removeListener (int eventType, SWTEventListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when the widget is disposed.
 *
 * @param listener the listener which should no longer be notified when the receiver is disposed
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

boolean sendKeyEvent (int type, int msg, int wParam, int lParam) {
	Event event = new Event ();
	if (!setKeyState (event, type, wParam, lParam)) return true;
	return sendKeyEvent (type, msg, wParam, lParam, event);
}

boolean sendKeyEvent (int type, int msg, int wParam, int lParam, Event event) {
	sendEvent (type, event);
	if (isDisposed ()) return false;
	return event.doit;
}

boolean sendMouseEvent (int type, int button, int hwnd, int msg, int wParam, int lParam) {
	return sendMouseEvent (type, button, 0, 0, false, hwnd, msg, wParam, lParam);
}

boolean sendMouseEvent (int type, int button, int count, int detail, boolean send, int hwnd, int msg, int wParam, int lParam) {
	if (!hooks (type) && !filters (type)) return true;
	Event event = new Event ();
	event.button = button;
	event.detail = detail;
	event.count = count;
	event.x = (short) (lParam & 0xFFFF);
	event.y = (short) (lParam >> 16);
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
	if (OS.GetKeyState (OS.VK_XBUTTON1) < 0) event.stateMask |= SWT.BUTTON4;
	if (OS.GetKeyState (OS.VK_XBUTTON2) < 0) event.stateMask |= SWT.BUTTON5;
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

boolean setKeyState (Event event, int type, int wParam, int lParam) {
	
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
	
	if (display.lastVirtual) {
		/*
		* Feature in Windows.  The virtual key VK_DELETE is not
		* treated as both a virtual key and an ASCII key by Windows.
		* Therefore, we will not receive a WM_CHAR for this key.
		* The fix is to treat VK_DELETE as a special case and map
		* the ASCII value explictly (Delete is 0x7F).
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

boolean SetWindowPos (int hWnd, int hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags) {
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

LRESULT wmCaptureChanged (int hwnd, int wParam, int lParam) {
	display.captureChanged = true;
	return null;
}

LRESULT wmChar (int hwnd, int wParam, int lParam) {
	/*
	* Do not report a lead byte as a key pressed.
	*/
	if (!OS.IsUnicode && OS.IsDBLocale) {
		byte lead = (byte) (wParam & 0xFF);
		if (OS.IsDBCSLeadByte (lead)) return null;
	}
	display.lastAscii = wParam;
	display.lastNull = wParam == 0;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_CHAR, wParam, lParam)) {
		return LRESULT.ONE;
	}
	// widget could be disposed at this point
	return null;
}

LRESULT wmContextMenu (int hwnd, int wParam, int lParam) {
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
		x = pt.x = (short) (lParam & 0xFFFF);
		y = pt.y = (short) (lParam >> 16);
		OS.ScreenToClient (hwnd, pt);
		RECT rect = new RECT ();
		OS.GetClientRect (hwnd, rect);
		if (!OS.PtInRect (rect, pt)) return null;
	} else {
		int pos = OS.GetMessagePos ();
		x = (short) (pos & 0xFFFF);
		y = (short) (pos >> 16);
	}

	/* Show the menu */
	return showMenu (x, y) ? LRESULT.ZERO : null;
}

LRESULT wmIMEChar (int hwnd, int wParam, int lParam) {
	Display display = this.display;
	display.lastKey = 0;
	display.lastAscii = wParam;
	display.lastVirtual = display.lastNull = display.lastDead = false;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
		return LRESULT.ONE;
	}
	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	return LRESULT.ONE;
}

LRESULT wmKeyDown (int hwnd, int wParam, int lParam) {
	
	/* Ignore repeating modifier keys by testing key down state */
	switch (wParam) {
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
		switch (wParam) {
			case OS.VK_BACK: mapKey = SWT.BS; break;
			case OS.VK_RETURN: mapKey = SWT.CR; break;
			case OS.VK_DELETE: mapKey = SWT.DEL; break;
			case OS.VK_ESCAPE: mapKey = SWT.ESC; break;
			case OS.VK_TAB: mapKey = SWT.TAB; break;
		}
	} else {
		mapKey = OS.MapVirtualKey (wParam, 2);
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
		display.lastKey = display.lastVirtual ? wParam : mapKey;
		return null;
	}
	
	/*
	*  Bug in Windows.  Somehow, the widget is becoming disposed after
	*  calling PeekMessage().  In rare cirucmstances, it seems that
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
	display.lastVirtual = mapKey == 0 || display.numpadKey (wParam) != 0;
	if (display.lastVirtual) {
		display.lastKey = wParam;
		/*
		* Feature in Windows.  The virtual key VK_DELETE is not
		* treated as both a virtual key and an ASCII key by Windows.
		* Therefore, we will not receive a WM_CHAR for this key.
		* The fix is to treat VK_DELETE as a special case and map
		* the ASCII value explictly (Delete is 0x7F).
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
	 	display.lastKey = OS.CharLower ((short) mapKey);

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
		int asciiKey = display.asciiKey (wParam);
		if (asciiKey != 0) {
			/*
			* When the user types Ctrl+Space, ToAscii () maps this to
			* Space.  Normally, ToAscii () maps a key to a different
			* key if both a WM_KEYDOWN and a WM_CHAR will be issued.
			* To avoid the extra SWT.KeyDown, look for a space and
			* issue the event from WM_CHAR.
			*/
			if (asciiKey == ' ') return null;
			if (asciiKey != wParam) return null;
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
			display.lastAscii = display.shiftedKey (wParam);
			if (display.lastAscii == 0) display.lastAscii = mapKey;
	 	} else {
	 		display.lastAscii = OS.CharLower ((short) mapKey);
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

LRESULT wmKeyUp (int hwnd, int wParam, int lParam) {
	Display display = this.display;
	
	/* Check for hardware keys */
	if (OS.IsWinCE) {
		if (OS.VK_APP1 <= wParam && wParam <= OS.VK_APP6) {
			display.lastKey = display.lastAscii = 0;
			display.lastVirtual = display.lastNull = display.lastDead = false;
			Event event = new Event ();
			event.detail = wParam - OS.VK_APP1 + 1;
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
		switch (wParam) {
			case OS.VK_BACK: mapKey = SWT.BS; break;
			case OS.VK_RETURN: mapKey = SWT.CR; break;
			case OS.VK_DELETE: mapKey = SWT.DEL; break;
			case OS.VK_ESCAPE: mapKey = SWT.ESC; break;
			case OS.VK_TAB: mapKey = SWT.TAB; break;
		}
	} else {
		mapKey = OS.MapVirtualKey (wParam, 2);
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
	display.lastVirtual = mapKey == 0 || display.numpadKey (wParam) != 0;
	if (display.lastVirtual) {
		display.lastKey = wParam;
	} else {
		/*
		* Feature in Windows. The virtual key VK_CANCEL is treated
		* as both a virtual key and ASCII key by Windows.  This
		* means that a WM_CHAR with WPARAM=3 will be issued for
		* this key.  In order to distingush between this key and
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

LRESULT wmKillFocus (int hwnd, int wParam, int lParam) {
	int code = callWindowProc (hwnd, OS.WM_KILLFOCUS, wParam, lParam);
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

LRESULT wmLButtonDblClk (int hwnd, int wParam, int lParam) {
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
	sendMouseEvent (SWT.MouseDown, 1, hwnd, OS.WM_LBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, hwnd, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	return new LRESULT (result);
}

LRESULT wmLButtonDown (int hwnd, int wParam, int lParam) {
	boolean dragging = false, mouseDown = true;
	boolean dragDetect = hooks (SWT.DragDetect);
	if (dragDetect) {
		if (!OS.IsWinCE) {
			/*
			* Feature in Windows.  It's possible that the drag
			* operation will not be started while the mouse is
			* down, meaning that the mouse should be captured.
			* This can happen when the user types the ESC key
			* to cancel the drag.  The fix is to query the state
			* of the mouse and capture the mouse accordingly.
			*/
			POINT pt = new POINT ();
			pt.x = (short) (lParam & 0xFFFF);
			pt.y = (short) (lParam >> 16);
			OS.ClientToScreen (hwnd, pt);
			dragging = OS.DragDetect (hwnd, pt);
			mouseDown = OS.GetKeyState (OS.VK_LBUTTON) < 0;
		}
	}
	sendMouseEvent (SWT.MouseDown, 1, hwnd, OS.WM_LBUTTONDOWN, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_LBUTTONDOWN, wParam, lParam);	
	if (OS.IsPPC) {
		/*
		* Note: On WinCE PPC, only attempt to recognize the gesture for
		* a context menu when the control contains a valid menu or there
		* are listeners for the MenuDetect event.
		*/
		Menu menu = getMenu ();
		boolean hasMenu = menu != null && !menu.isDisposed ();
		if (hasMenu || hooks (SWT.MenuDetect)) {
			int x = (short) (lParam & 0xFFFF);
			int y = (short) (lParam >> 16);
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
		if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	}
	if (dragging) {
		Event event = new Event ();
		event.x = (short) (lParam & 0xFFFF);
		event.y = (short) (lParam >> 16);
		postEvent (SWT.DragDetect, event);
	} else {
		if (dragDetect) {
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
	return new LRESULT (result);
}

LRESULT wmLButtonUp (int hwnd, int wParam, int lParam) {
	sendMouseEvent (SWT.MouseUp, 1, hwnd, OS.WM_LBUTTONUP, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_LBUTTONUP, wParam, lParam);
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON | OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if (((wParam & 0xFFFF) & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return new LRESULT (result);
}

LRESULT wmMButtonDblClk (int hwnd, int wParam, int lParam) {
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
	sendMouseEvent (SWT.MouseDown, 2, hwnd, OS.WM_MBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 2, hwnd, OS.WM_MBUTTONDBLCLK, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_MBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	return new LRESULT (result);
}

LRESULT wmMButtonDown (int hwnd, int wParam, int lParam) {
	sendMouseEvent (SWT.MouseDown, 2, hwnd, OS.WM_MBUTTONDOWN, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_MBUTTONDOWN, wParam, lParam);
	if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	return new LRESULT (result);
}

LRESULT wmMButtonUp (int hwnd, int wParam, int lParam) {
	sendMouseEvent (SWT.MouseUp, 2, hwnd, OS.WM_MBUTTONUP, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_MBUTTONUP, wParam, lParam);
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON | OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if (((wParam & 0xFFFF) & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return new LRESULT (result);
}

LRESULT wmMouseHover (int hwnd, int wParam, int lParam) {
	sendMouseEvent (SWT.MouseHover, 0, hwnd, OS.WM_MOUSEHOVER, wParam, lParam);
	return null;
}

LRESULT wmMouseLeave (int hwnd, int wParam, int lParam) {
	if (!hooks (SWT.MouseExit) && !filters (SWT.MouseExit)) return null;
	int pos = OS.GetMessagePos ();
	POINT pt = new POINT ();
	pt.x = (short) (pos & 0xFFFF);
	pt.y = (short) (pos >> 16); 
	OS.ScreenToClient (hwnd, pt);
	lParam = pt.x | (pt.y << 16);
	sendMouseEvent (SWT.MouseExit, 0, hwnd, OS.WM_MOUSELEAVE, wParam, lParam);
	return null;
}

LRESULT wmMouseMove (int hwnd, int wParam, int lParam) {
	int pos = OS.GetMessagePos ();
	if (pos != display.lastMouse || display.captureChanged) {
		if (!OS.IsWinCE) {
			boolean mouseEnter = hooks (SWT.MouseEnter) || display.filters (SWT.MouseEnter);
			boolean mouseExit = hooks (SWT.MouseExit) || display.filters (SWT.MouseExit);
			boolean mouseHover = hooks (SWT.MouseHover) || display.filters (SWT.MouseHover);
			if (mouseEnter || mouseExit || mouseHover) {
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
			sendMouseEvent (SWT.MouseMove, 0, hwnd, OS.WM_MOUSEMOVE, wParam, lParam);
		}
	} 
	display.captureChanged = false;
	return null;
}

LRESULT wmMouseWheel (int hwnd, int wParam, int lParam) {
	if (!hooks (SWT.MouseWheel) && !filters (SWT.MouseWheel)) return null;
	int delta = wParam >> 16;
	int [] value = new int [1];
	int count, detail;
	OS.SystemParametersInfo (OS.SPI_GETWHEELSCROLLLINES, 0, value, 0);
	if (value [0] == OS.WHEEL_PAGESCROLL) {
		detail = SWT.SCROLL_PAGE;
		count = delta / OS.WHEEL_DELTA;
	} else {
		detail = SWT.SCROLL_LINE;
		count = value [0] * delta / OS.WHEEL_DELTA;
	}
	POINT pt = new POINT ();
	pt.x = (short) (lParam & 0xFFFF);
	pt.y = (short) (lParam >> 16); 
	OS.ScreenToClient (hwnd, pt);
	lParam = pt.x | (pt.y << 16);
	if (!sendMouseEvent (SWT.MouseWheel, 0, count, detail, true, hwnd, OS.WM_MOUSEWHEEL, wParam, lParam)) {
		return LRESULT.ZERO;
	}
	return null;
}

LRESULT wmPaint (int hwnd, int wParam, int lParam) {

	/* Exit early - don't draw the background */
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) {
		return null;
	}
	
	/* Issue a paint event */
	int result = 0;
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
		int rgn = OS.CreateRectRgn (0, 0, 0, 0);
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
				int hDC = gc.handle;
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

LRESULT wmRButtonDblClk (int hwnd, int wParam, int lParam) {
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
	sendMouseEvent (SWT.MouseDown, 3, hwnd, OS.WM_RBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 3, hwnd, OS.WM_RBUTTONDBLCLK, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_RBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	return new LRESULT (result);
}

LRESULT wmRButtonDown (int hwnd, int wParam, int lParam) {
	sendMouseEvent (SWT.MouseDown, 3, hwnd, OS.WM_RBUTTONDOWN, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_RBUTTONDOWN, wParam, lParam);
	if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	return new LRESULT (result);
}

LRESULT wmRButtonUp (int hwnd, int wParam, int lParam) {
	sendMouseEvent (SWT.MouseUp, 3, hwnd, OS.WM_RBUTTONUP, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_RBUTTONUP, wParam, lParam);
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON | OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if (((wParam & 0xFFFF) & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return new LRESULT (result);
}

LRESULT wmSetFocus (int hwnd, int wParam, int lParam) {
	int code = callWindowProc (hwnd, OS.WM_SETFOCUS, wParam, lParam);
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

LRESULT wmSysChar (int hwnd, int wParam, int lParam) {
	Display display = this.display;
	display.lastAscii = wParam;
	display.lastNull = wParam == 0;

	/* Do not issue a key down if a menu bar mnemonic was invoked */
	if (!hooks (SWT.KeyDown) && !display.filters (SWT.KeyDown)) {
		return null;
	}
	
	/* Call the window proc to determine whether it is a system key or mnemonic */
	boolean oldKeyHit = display.mnemonicKeyHit;
	display.mnemonicKeyHit = true;
	int result = callWindowProc (hwnd, OS.WM_SYSCHAR, wParam, lParam);
	boolean consumed = false;
	if (!display.mnemonicKeyHit) {
		consumed = !sendKeyEvent (SWT.KeyDown, OS.WM_SYSCHAR, wParam, lParam);
		// widget could be disposed at this point
	}
	consumed |= display.mnemonicKeyHit;
	display.mnemonicKeyHit = oldKeyHit;
	return consumed ? LRESULT.ONE : new LRESULT (result);
}

LRESULT wmSysKeyDown (int hwnd, int wParam, int lParam) {
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
	switch (wParam) {
		case OS.VK_F4: return null;
	}
	
	/* Ignore repeating modifier keys by testing key down state */
	switch (wParam) {
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
		switch (wParam) {
			case OS.VK_BACK: mapKey = SWT.BS; break;
			case OS.VK_RETURN: mapKey = SWT.CR; break;
			case OS.VK_DELETE: mapKey = SWT.DEL; break;
			case OS.VK_ESCAPE: mapKey = SWT.ESC; break;
			case OS.VK_TAB: mapKey = SWT.TAB; break;
		}
	} else {
		mapKey = OS.MapVirtualKey (wParam, 2);
	}
	display.lastVirtual = mapKey == 0 || display.numpadKey (wParam) != 0;
	if (display.lastVirtual) {
	 	display.lastKey = wParam;
		/*
		* Feature in Windows.  The virtual key VK_DELETE is not
		* treated as both a virtual key and an ASCII key by Windows.
		* Therefore, we will not receive a WM_SYSCHAR for this key.
		* The fix is to treat VK_DELETE as a special case and map
		* the ASCII value explictly (Delete is 0x7F).
		*/
		if (display.lastKey == OS.VK_DELETE) display.lastAscii = 0x7F;

		/* When a keypad key is typed, a WM_SYSCHAR is not issued */
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
	 	display.lastKey = OS.CharLower ((short) mapKey);

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

LRESULT wmSysKeyUp (int hwnd, int wParam, int lParam) {
	return wmKeyUp (hwnd, wParam, lParam);
}

LRESULT wmXButtonDblClk (int hwnd, int wParam, int lParam) {
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
	int button = (wParam >> 16 == OS.XBUTTON1) ? 4 : 5;
	sendMouseEvent (SWT.MouseDown, button, hwnd, OS.WM_XBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, button, hwnd, OS.WM_XBUTTONDBLCLK, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_XBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	return new LRESULT (result);
}

LRESULT wmXButtonDown (int hwnd, int wParam, int lParam) {
	int button = (wParam >> 16 == OS.XBUTTON1) ? 4 : 5;
	sendMouseEvent (SWT.MouseDown, button, hwnd, OS.WM_XBUTTONDOWN, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_XBUTTONDOWN, wParam, lParam);
	if (OS.GetCapture () != hwnd) OS.SetCapture (hwnd);
	return new LRESULT (result);
}

LRESULT wmXButtonUp (int hwnd, int wParam, int lParam) {
	int button = (wParam >> 16 == OS.XBUTTON1) ? 4 : 5;
	sendMouseEvent (SWT.MouseUp, button, hwnd, OS.WM_XBUTTONUP, wParam, lParam);
	int result = callWindowProc (hwnd, OS.WM_XBUTTONUP, wParam, lParam);
	int mask = OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON | OS.MK_XBUTTON1 | OS.MK_XBUTTON2;
	if (((wParam & 0xFFFF) & mask) == 0) {
		if (OS.GetCapture () == hwnd) OS.ReleaseCapture ();
	}
	return new LRESULT (result);
}
}

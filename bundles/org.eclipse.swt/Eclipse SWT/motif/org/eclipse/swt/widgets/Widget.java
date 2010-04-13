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


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
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
	public int handle;
	int style, state;
	Display display;
	EventTable eventTable;
	Object data;
	
	/* Global state flags */
	static final int DISPOSED = 1<<0;
	static final int CANVAS = 1<<1;
	static final int KEYED_DATA = 1<<2;
	static final int FOCUS_FORCED = 1<<3;
	static final int BACKGROUND = 1<<4;
	static final int FOREGROUND = 1<<5;
	static final int PARENT_BACKGROUND = 1<<6;
	static final int THEME_BACKGROUND = 1<<7;
	
	/* A layout was requested on this widget */
	static final int LAYOUT_NEEDED	= 1<<8;
	
	/* The preferred size of a child has changed */
	static final int LAYOUT_CHANGED = 1<<9;
	
	/* A layout was requested in this widget hierachy */
	static final int LAYOUT_CHILD = 1<<10;

	/* More global state flags */
	static final int RELEASED = 1<<11;
	static final int DISPOSE_SENT = 1<<12;
	static final int FOREIGN_HANDLE = 1<<13;
	static final int DRAG_DETECT = 1<<14;

	/* Notify of the opportunity to skin this widget */
	static final int SKIN_NEEDED = 1<<15;
	
	/* Default size for widgets */
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	
	/* Events and Callback constants */		
	static final int BUTTON_PRESS = 1;
	static final int BUTTON_RELEASE = 2;
	static final int EXPOSURE = 3;
	static final int ENTER_WINDOW = 4;
	static final int FOCUS_CHANGE = 5;
	static final int KEY_PRESS = 6;
	static final int KEY_RELEASE = 7;
	static final int LEAVE_WINDOW = 8;
	static final int ACTIVATE_CALLBACK = 9;
	static final int ARM_CALLBACK = 10;
	static final int BROWSE_SELECTION_CALLBACK = 11;
	static final int CASCADING_CALLBACK = 12;
	static final int DECREMENT_CALLBACK = 13;
	static final int DEFAULT_ACTION_CALLBACK = 14;
	static final int DRAG_CALLBACK = 15;
	static final int EXTENDED_SELECTION_CALLBACK = 16;
	static final int HELP_CALLBACK = 17;
	static final int INCREMENT_CALLBACK = 18;
	static final int MODIFY_VERIFY_CALLBACK = 19;
	static final int PAGE_DECREMENT_CALLBACK = 20;
	static final int PAGE_INCREMENT_CALLBACK = 21;
	static final int TO_BOTTOM_CALLBACK = 22;
	static final int TO_TOP_CALLBACK = 23;
	static final int VALUE_CHANGED_CALLBACK = 24;
	static final int NON_MASKABLE  = 25;
	static final int POINTER_MOTION  = 26;
	static final int STRUCTURE_NOTIFY  = 27;
	static final int MAP_CALLBACK = 28;
	static final int UNMAP_CALLBACK  = 29;
	static final int DELETE_WINDOW = 30;
	static final int EXPOSURE_CALLBACK  = 31;
	static final int MULTIPLE_SELECTION_CALLBACK  = 32;
	static final int PROPERTY_CHANGE = 33;

Widget () {
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
public void addListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, handler);
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
	/* Do nothing */
}
void createWidget (int index) {
	createHandle (index);
	hookEvents ();
	register ();
	manageChildren ();
}
void deregister () {
	if (handle == 0) return;
	display.removeWidget (handle);
}
void destroyWidget () {
	int topHandle = topHandle ();
	releaseHandle ();
	if (topHandle != 0) {
		OS.XtDestroyWidget (topHandle);
	}
}
/**
 * Disposes of the operating system resources associated with
 * the receiver and all its descendants. After this method has
 * been invoked, the receiver and all descendants will answer
 * <code>true</code> when sent the message <code>isDisposed()</code>.
 * Any internal connections between the widgets in the tree will
 * have been removed to facilitate garbage collection.
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
void enableHandle (boolean enabled, int widgetHandle) {
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (widgetHandle, argList, argList.length / 2);
}
void error (int code) {
	SWT.error(code);
}
boolean filters (int eventType) {
	return display.filters (eventType);
}
static char fixMnemonic (char [] buffer) {
	int i=0, j=0;
	char mnemonic=0;
	while (i < buffer.length) {
		if ((buffer [j++] = buffer [i++]) == '&') {
			if (i == buffer.length) {continue;}
			if (buffer [i] == '&') {i++; continue;}
			if (mnemonic == 0) mnemonic = buffer [i];
			j--;
		}
	}
	while (j < buffer.length) buffer [j++] = 0;
	return mnemonic;
}
int focusProc (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
String getCodePage () {
	return null;
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
String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
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
	checkWidget();
	return style;
}
void hookEvents () {
	/* Do nothing */
}
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
boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}
boolean isValidThread () {
	return getDisplay ().isValidThread ();
}
void manageChildren () {
	/* Do nothing */
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
void propagateHandle (boolean enabled, int widgetHandle, int cursor) {
	int xDisplay = OS.XtDisplay (widgetHandle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (widgetHandle);
	if (xWindow == 0) return;
	/*
	* Get the event mask from the widget.  The event mask
	* returned by XtBuildEventMask () includes the masks
	* associated with all callbacks and event handlers
	* that have been hooked on the widget.
	*/
	int event_mask = OS.XtBuildEventMask (widgetHandle);
	int do_not_propagate_mask = 
		OS.KeyPressMask | OS.KeyReleaseMask | OS.ButtonPressMask | 
		OS.ButtonReleaseMask | OS.PointerMotionMask;
	if (!enabled) {
		/*
		* Attempting to propogate EnterWindowMask and LeaveWindowMask
		* causes an X error so these must be specially cleared out from
		* the event mask, not included in the propogate mask.
		*/
		event_mask &= ~(do_not_propagate_mask | OS.EnterWindowMask | OS.LeaveWindowMask);
		do_not_propagate_mask = 0;
	}
	int mask = OS.CWDontPropagate | OS.CWEventMask | OS.CWCursor;
	XSetWindowAttributes attributes = new XSetWindowAttributes ();
	attributes.event_mask = event_mask;
	attributes.do_not_propagate_mask = do_not_propagate_mask;
	attributes.cursor = cursor;
	OS.XChangeWindowAttributes (xDisplay, xWindow, mask, attributes);
}
void redrawHandle (int x, int y, int width, int height, boolean redrawAll, int widgetHandle) {
	int display = OS.XtDisplay (widgetHandle);
	if (display == 0) return;
	int window = OS.XtWindow (widgetHandle);
	if (window == 0) return;
	if (redrawAll) {
		OS.XClearArea (display, window, 0, 0, 0, 0, true);
	} else {
		if (width > 0 && height > 0) {
			int [] argList = {
				OS.XmNwidth, 0, 		/* 1 */
				OS.XmNheight, 0, 		/* 3 */
			};
			OS.XtGetValues (widgetHandle, argList, argList.length / 2);
			if ((x < argList [1]) && (y < argList [3]) && (x + width > 0) && (y + height > 0)) {
				OS.XClearArea (display, window, x, y, width, height, true);
			}
		}
	}
}
void register () {
	if (handle == 0) return;
	display.addWidget (handle, this);
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
public void removeListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
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
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}
/**
 * 
 * @param flags
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}
boolean setInputState (Event event, int state) {
	if ((state & OS.Mod1Mask) != 0) event.stateMask |= SWT.ALT;
	if ((state & OS.ShiftMask) != 0) event.stateMask |= SWT.SHIFT;
	if ((state & OS.ControlMask) != 0) event.stateMask |= SWT.CONTROL;
	if ((state & OS.Button1Mask) != 0) event.stateMask |= SWT.BUTTON1;
	if ((state & OS.Button2Mask) != 0) event.stateMask |= SWT.BUTTON2;
	if ((state & OS.Button3Mask) != 0) event.stateMask |= SWT.BUTTON3;
	return true;
}
boolean setKeyState (Event event, XKeyEvent xEvent) {
	if (xEvent.keycode == 0) return false;
	byte [] buffer = new byte [5];
	int [] keysym = new int [1];
	OS.XLookupString (xEvent, buffer, buffer.length, keysym, null);
	boolean isNull = display.fixKey (keysym, buffer, xEvent.state);
	setLocationState (event, keysym [0]);
	if (keysym [0] != 0) {
		event.keyCode = Display.translateKey (keysym [0]);
	}
	if (event.keyCode == 0) {
		byte [] buffer1 = new byte [5];
		int [] keysym1 = new int [1];
		int oldState = xEvent.state;
		xEvent.state = 0;
		OS.XLookupString (xEvent, buffer1, buffer1.length, keysym1, null);
		xEvent.state = oldState;
		if (buffer1 [0] != 0) {
			char [] result = Converter.mbcsToWcs (null, buffer1);
			if (result.length != 0) event.keyCode = result [0];
		}
	}
	if (buffer [0] != 0) {
		char [] result = Converter.mbcsToWcs (null, buffer);
		if (result.length != 0) event.character = result [0];
	}
	if (event.keyCode == 0 && event.character == 0) {
		if (!isNull) return false;
	}
	return setInputState (event, xEvent.state);
}
void setLocationState (Event event, int keysym) {
	switch (keysym) {
		case OS.XK_Alt_L:
		case OS.XK_Meta_L:
		case OS.XK_Control_L:
		case OS.XK_Shift_L:
			event.keyLocation = SWT.LEFT;
			break;
		case OS.XK_Alt_R:
		case OS.XK_Meta_R:
		case OS.XK_Control_R:
		case OS.XK_Shift_R:
			event.keyLocation = SWT.RIGHT;
			break;
		case OS.XK_KP_Enter:
		case OS.XK_KP_F1:
		case OS.XK_KP_F2:
		case OS.XK_KP_F3:
		case OS.XK_KP_F4:
		case OS.XK_KP_Home:
		case OS.XK_KP_Left:
		case OS.XK_KP_Up:
		case OS.XK_KP_Right:
		case OS.XK_KP_Down:
		case OS.XK_KP_Page_Up:
		case OS.XK_KP_Page_Down:
		case OS.XK_KP_End:
		case OS.XK_KP_Insert:
		case OS.XK_KP_Delete:
		case OS.XK_KP_Equal:
		case OS.XK_KP_Multiply:
		case OS.XK_KP_Add:
		case OS.XK_KP_Subtract:
		case OS.XK_KP_Decimal:
		case OS.XK_KP_Divide:
		case OS.XK_KP_0:
		case OS.XK_KP_1:
		case OS.XK_KP_2:
		case OS.XK_KP_3:
		case OS.XK_KP_4:
		case OS.XK_KP_5:
		case OS.XK_KP_6:
		case OS.XK_KP_7:
		case OS.XK_KP_8:
		case OS.XK_KP_9:
		case OS.XK_Num_Lock:
			event.keyLocation = SWT.KEYPAD;
			break;
	}
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
boolean sendIMKeyEvent (int type, XKeyEvent xEvent) {
	return sendIMKeyEvent (type, xEvent, 0);
}
boolean sendIMKeyEvent (int type, XKeyEvent xEvent, int textHandle) {
	/*
	* Bug in Motif. On Linux only, XmImMbLookupString () does not return 
	* XBufferOverflow as the status if the buffer is too small. The fix
	* is to pass a large buffer.
	*/
	byte [] buffer = new byte [512];
	int [] status = new int [1], unused = new int [1];
	int focusHandle = OS.XtWindowToWidget (xEvent.display, xEvent.window);
	int length = OS.XmImMbLookupString (focusHandle, xEvent, buffer, buffer.length, unused, status);
	if (status [0] == OS.XBufferOverflow) {
		buffer = new byte [length];
		length = OS.XmImMbLookupString (focusHandle, xEvent, buffer, length, unused, status);
	}
	if (length == 0) return true;
	
	/* Convert from MBCS to UNICODE and send the event */
	/* Use the character encoding for the default locale */
	char [] chars = Converter.mbcsToWcs (null, buffer);
	int index = 0, count = 0;
	while (index < chars.length) {
		if (chars [index] == 0) {
			chars [count] = 0;
			break;
		}
		Event event = new Event ();
		event.time = xEvent.time;
		event.character = chars [index];
		setInputState (event, xEvent.state);
		sendEvent (type, event);
		// widget could be disposed at this point
	
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning false.
		*/
		if (isDisposed ()) return false;
		if (event.doit) chars [count++] = chars [index];
		index++;
	}
	if (count == 0) return false;
	if (textHandle != 0) {
		/*
		* Bug in Motif. On Solaris and Linux, XmImMbLookupString() clears
		* the characters from the IME. This causes the characters to be
		* stolen from the text widget. The fix is to detect that the IME
		* has been cleared and use XmTextInsert() to insert the stolen
		* characters. This problem does not happen on AIX.
		*/
		byte [] testBuffer = new byte [5];
		int testLength = OS.XmImMbLookupString (textHandle, xEvent, testBuffer, testBuffer.length, unused, unused);
		if (testLength == 0 || index != count) {
			int [] start = new int [1], end = new int [1];
			OS.XmTextGetSelectionPosition (textHandle, start, end);
			if (start [0] == end [0]) {
				start [0] = end [0] = OS.XmTextGetInsertionPosition (textHandle);
			}
			boolean warnings = display.getWarnings ();
			display.setWarnings (false);
			if (index != count) {
				buffer = Converter.wcsToMbcs (getCodePage (), chars, true);
			}
			OS.XmTextReplace (textHandle, start [0], end [0], buffer);
			int position = start [0] + count;
			OS.XmTextSetInsertionPosition (textHandle, position);
			display.setWarnings (warnings);
			return false;
		}
	}
	return true;
}
boolean sendKeyEvent (int type, XKeyEvent xEvent) {
	Event event = new Event ();
	event.time = xEvent.time;
	if (!setKeyState (event, xEvent)) return true;
	Widget control = this;
	if ((state & CANVAS) != 0) {
		if ((style & SWT.NO_FOCUS) != 0) {
			control = display.getFocusControl ();
		}
	}
	if (control != null) {
		control.sendEvent (type, event);
		// widget could be disposed at this point
	
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the key
		* events.  If this happens, end the processing of
		* the key by returning false.
		*/
		if (isDisposed ()) return false;
	}
	return event.doit;
}
void sendSelectionEvent (int eventType) {
	sendSelectionEvent (eventType, null, false);
}
void sendSelectionEvent (int eventType, Event event, boolean send) {
	if (eventTable == null && !display.filters (eventType)) {
		return;
	}
	if (event == null) event = new Event ();
//	setInputState (event, state);
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
int topHandle () {
	return handle;
}
boolean translateAccelerator (char key, int keysym, XKeyEvent xEvent, boolean doit) {
	return false;
}
boolean translateMnemonic (char key, int keysym, XKeyEvent xEvent) {
	return false;
}
boolean translateTraversal (int key, XKeyEvent xEvent) {
	return false;
}
boolean XmProcessTraversal (int widget, int direction) {
	/*
	* Bug in Motif.  When XtDestroyWidget() is called from
	* within a FocusOut event handler, Motif GP's.  The fix
	* is to post focus events and run them when the handler
	* has returned.
	*/
	Display display = this.display;
	boolean oldFocusOut = display.postFocusOut;
	display.postFocusOut = true;
	boolean result = OS.XmProcessTraversal (widget, direction);
	display.postFocusOut = oldFocusOut;
	if (!display.postFocusOut) {
		display.focusEvent = SWT.FocusOut;
		display.runFocusOutEvents ();
		display.focusEvent = SWT.None;
	}
	return result;
}
int hoverProc (int widget) {
	return 0;
}
int timerProc (int id) {
	return 0;
}
int windowProc (int w, int client_data, int call_data, int continue_to_dispatch) {
	switch (client_data) {
		case BUTTON_PRESS:					return XButtonPress (w, client_data, call_data, continue_to_dispatch);	
		case BUTTON_RELEASE:				return XButtonRelease (w, client_data, call_data, continue_to_dispatch);
		case ENTER_WINDOW:					return XEnterWindow (w, client_data, call_data, continue_to_dispatch);
		case EXPOSURE:						return XExposure (w, client_data, call_data, continue_to_dispatch);
		case FOCUS_CHANGE:					return XFocusChange (w, client_data, call_data, continue_to_dispatch);
		case KEY_PRESS:					return XKeyPress (w, client_data, call_data, continue_to_dispatch);
		case KEY_RELEASE:					return XKeyRelease (w, client_data, call_data, continue_to_dispatch);
		case LEAVE_WINDOW:					return XLeaveWindow (w, client_data, call_data, continue_to_dispatch);
		case PROPERTY_CHANGE:				return XPropertyChange (w, client_data, call_data, continue_to_dispatch);
		case ACTIVATE_CALLBACK:			return XmNactivateCallback (w, client_data, call_data);
		case ARM_CALLBACK:					return XmNarmCallback (w, client_data, call_data);
		case BROWSE_SELECTION_CALLBACK:	return XmNbrowseSelectionCallback (w, client_data, call_data);
		case CASCADING_CALLBACK:			return XmNcascadingCallback (w, client_data, call_data);
		case DECREMENT_CALLBACK:			return XmNdecrementCallback (w, client_data, call_data);
		case DEFAULT_ACTION_CALLBACK:		return XmNdefaultActionCallback (w, client_data, call_data);
		case DRAG_CALLBACK:				return XmNdragCallback (w, client_data, call_data);
		case EXTENDED_SELECTION_CALLBACK:	return XmNextendedSelectionCallback (w, client_data, call_data);
		case HELP_CALLBACK:				return XmNhelpCallback (w, client_data, call_data);
		case INCREMENT_CALLBACK:			return XmNincrementCallback (w, client_data, call_data);
		case MODIFY_VERIFY_CALLBACK:		return XmNmodifyVerifyCallback (w, client_data, call_data);
		case MULTIPLE_SELECTION_CALLBACK:		return XmNmultipleSelectionCallback (w, client_data, call_data);
		case PAGE_DECREMENT_CALLBACK:		return XmNpageDecrementCallback (w, client_data, call_data);
		case PAGE_INCREMENT_CALLBACK:		return XmNpageIncrementCallback (w, client_data, call_data);
		case TO_BOTTOM_CALLBACK:			return XmNtoBottomCallback (w, client_data, call_data);
		case TO_TOP_CALLBACK:				return XmNtoTopCallback (w, client_data, call_data);
		case VALUE_CHANGED_CALLBACK:		return XmNvalueChangedCallback (w, client_data, call_data);
		case NON_MASKABLE:					return XNonMaskable (w, client_data, call_data, continue_to_dispatch);
		case POINTER_MOTION :				return XPointerMotion (w, client_data, call_data, continue_to_dispatch);
		case STRUCTURE_NOTIFY:				return XStructureNotify (w, client_data, call_data, continue_to_dispatch);
		case MAP_CALLBACK:					return XmNmapCallback (w, client_data, call_data);
		case UNMAP_CALLBACK:				return XmNunmapCallback (w, client_data, call_data);
		case DELETE_WINDOW:				return WM_DELETE_WINDOW (w, client_data, call_data);
		case EXPOSURE_CALLBACK:				return XmNexposureCallback (w, client_data, call_data);
	}
	return 0;
}
int WM_DELETE_WINDOW (int w, int client_data, int call_data) {
	return 0;
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XButtonRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XEnterWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XExposure (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XFocusChange (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XKeyPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, call_data, XKeyEvent.sizeof);
	boolean doit = true;
	if (xEvent.keycode != 0) {
		doit = sendKeyEvent (SWT.KeyDown, xEvent);
	} else {
		doit = sendIMKeyEvent (SWT.KeyDown, xEvent);
	}
	if (!doit) {
		OS.memmove (continue_to_dispatch, new int [1], 4);
		return 1;
	}
	return 0;
}
int XKeyRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, call_data, XKeyEvent.sizeof);
	if (!sendKeyEvent (SWT.KeyUp, xEvent)) {
		OS.memmove (continue_to_dispatch, new int [1], 4);
		return 1;
	}
	return 0;
}
int XLeaveWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XPropertyChange (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XmNactivateCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNarmCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNbrowseSelectionCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNcascadingCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNdecrementCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNdefaultActionCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNdragCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNexposureCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNextendedSelectionCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNhelpCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNincrementCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNmapCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNmodifyVerifyCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNmultipleSelectionCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNpageDecrementCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNpageIncrementCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNtoBottomCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNtoTopCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNunmapCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNvalueChangedCallback (int w, int client_data, int call_data) {
	return 0;
}
int XNonMaskable (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XStructureNotify (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
}

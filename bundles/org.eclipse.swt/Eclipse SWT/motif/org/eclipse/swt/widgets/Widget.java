/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
 */
public abstract class Widget {
	public int handle;
	int style, state;
	EventTable eventTable;
	Object data;
	String [] keys;
	Object [] values;
	
	/* Global state flags */
//	static final int AUTOMATIC		= 0x00000001;
//	static final int ACTIVE			= 0x00000002;
//	static final int AUTOGRAB		= 0x00000004;
//	static final int MULTIEXPOSE	= 0x00000008;
//	static final int RESIZEREDRAW	= 0x00000010;
//	static final int WRAP			= 0x00000020;
//	static final int DISABLED		= 0x00000040;
//	static final int HIDDEN			= 0x00000080;
//	static final int FOREGROUND		= 0x00000100;
//	static final int BACKGROUND		= 0x00000200;
	static final int DISPOSED		= 0x00000400;
	static final int HANDLE			= 0x00000800;
	static final int CANVAS			= 0x00001000;
	
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	
	/* Global widget variables */
	static final char Mnemonic = '&';
	
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
	static final int SELECTION_CALLBACK = 22;
	static final int TO_BOTTOM_CALLBACK = 23;
	static final int TO_TOP_CALLBACK = 24;
	static final int VALUE_CHANGED_CALLBACK = 25;
	static final int NON_MASKABLE  = 26;
	static final int POINTER_MOTION  = 27;
	static final int STRUCTURE_NOTIFY  = 28;
	static final int MAP_CALLBACK = 29;
	static final int UNMAP_CALLBACK  = 30;
	static final int DELETE_WINDOW = 31;
	static final int EXPOSURE_CALLBACK  = 32;	

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
public void addListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, handler);
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
void checkParent (Widget parent) {
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!parent.isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (parent.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	WidgetTable.remove (handle);
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
	if (isDisposed()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	releaseChild ();
	releaseWidget ();
	destroyWidget ();
}
void enableHandle (boolean enabled, int widgetHandle) {
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (widgetHandle, argList, argList.length / 2);
}
void error (int code) {
	SWT.error(code);
}
boolean filters (int eventType) {
	Display display = getDisplay ();
	return display.filters (eventType);
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
 * @see #setData
 */
public Object getData () {
	checkWidget();
	return data;
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
 * @see #setData
 */
public Object getData (String key) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
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
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public abstract Display getDisplay ();
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
	if (handle != 0) return false;
	if ((state & HANDLE) != 0) return true;
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
protected boolean isListening (int eventType) {
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
char mbcsToWcs (int ch) {
	return mbcsToWcs (ch, null);
}
char mbcsToWcs (int ch, String codePage) {
	int key = ch & 0xFFFF;
	if (key <= 0x7F) return (char) ch;
	byte [] buffer;
	if (key <= 0xFF) {
		buffer = new byte [1];
		buffer [0] = (byte) key;
	} else {
		buffer = new byte [2];
		buffer [0] = (byte) ((key >> 8) & 0xFF);
		buffer [1] = (byte) (key & 0xFF);
	}
	char [] result = Converter.mbcsToWcs (codePage, buffer);
	if (result.length == 0) return 0;
	return result [0];
}
/**
 * Notifies all of the receiver's listeners for events
 * of the given type that one such event has occurred by
 * invoking their <code>handleEvent()</code> method.
 *
 * @param eventType the type of event which has occurred
 * @param event the event data
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void notifyListeners (int eventType, Event event) {
	checkWidget();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
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
void redrawHandle (int x, int y, int width, int height, int widgetHandle) {
	int display = OS.XtDisplay (widgetHandle);
	if (display == 0) return;
	int window = OS.XtWindow (widgetHandle);
	if (window == 0) return;
	int [] argList = {OS.XmNborderWidth, 0, OS.XmNborderColor, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	if (argList [1] != 0) {
		/* Force the border to repaint by setting the color */
		OS.XtSetValues (widgetHandle, argList, argList.length / 2);
	}
	OS.XClearArea (display, window, x, y, width, height, true);
}
void register () {
	if (handle == 0) return;
	WidgetTable.put (handle, this);
}
void releaseChild () {
	/* Do nothing */
}
void releaseHandle () {
	handle = 0;
	state |= DISPOSED;
}
void releaseResources () {
	releaseWidget ();
	releaseHandle ();
}
void releaseWidget () {
	sendEvent (SWT.Dispose);
	deregister ();
	eventTable = null;
	data = null;
	keys = null;
	values = null;
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
public void removeListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
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
protected void removeListener (int eventType, SWTEventListener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
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
void setInputState (Event event, int state) {
	if ((state & OS.Mod1Mask) != 0) event.stateMask |= SWT.ALT;
	if ((state & OS.ShiftMask) != 0) event.stateMask |= SWT.SHIFT;
	if ((state & OS.ControlMask) != 0) event.stateMask |= SWT.CONTROL;
	if ((state & OS.Button1Mask) != 0) event.stateMask |= SWT.BUTTON1;
	if ((state & OS.Button2Mask) != 0) event.stateMask |= SWT.BUTTON2;
	if ((state & OS.Button3Mask) != 0) event.stateMask |= SWT.BUTTON3;	
}
void setInputState (Event event, XInputEvent xEvent) {
	setInputState (event, xEvent.state);	
}
void setKeyState (Event event, XKeyEvent xEvent) {
	if (xEvent.keycode != 0) {
		byte [] buffer = new byte [1];
		int [] keysym = new int [1];
		OS.XLookupString (xEvent, buffer, buffer.length, keysym, null);
		
		/*
		* Bug in MOTIF.  On Solaris only, XK_F11 and XK_F12 are not
		* translated correctly by XLookupString().  They are mapped
		* to 0x1005FF10 and 0x1005FF11 respectively.  The fix is to
		* look for these values explicitly and correct them.
		*/
		if (OS.IsSunOS && keysym [0] != 0) {
			switch (keysym [0]) {
				case 0x1005FF10: 
					keysym [0] = OS.XK_F11;
					buffer [0] = 0;
					break;
				case 0x1005FF11:
					keysym [0] = OS.XK_F12;
					buffer [0] = 0;
					break;
			}
			/*
			* Bug in MOTIF.  On Solaris only, there is garbage in the
			* high 16-bits for Keysyms such as XK_Down.  Since Keysyms
			* must be 16-bits to fit into a Character, mask away the
			* high 16-bits on all platforms.
			*/
			keysym [0] &= 0xFFFF;
		}
		
		/*
		* Feature in MOTIF. For some reason, XLookupString() fails 
		* to translate both the keysym and the character when the
		* control key is down.  For example, Ctrl+2 has the correct
		* keysym value (50) but no character value, while Ctrl+/ has
		* the keysym value (2F) but an invalid character value
		* (1F).  It seems that Motif is applying the algorithm to
		* convert a character to a control character for characters
		* that are not valid control characters.  The fix is to test
		* for 7-bit ASCII keysym values that fall outside of the
		* the valid control character range and use the keysym value
		* as the character, not the incorrect value that XLookupString()
		* returns.  Even though lower case values are not strictly
		* valid control characters, they are included in the range.
		* 
		* Some other cases include Ctrl+3..Ctr+8, Ctrl+[.
		*/
		if ((xEvent.state & OS.ControlMask) != 0) {
			int key = keysym [0];
			if (0 <= key && key <= 0x7F) {
				if ('a' <= key && key <= 'z') {
					key -= 'a' - 'A';
				}
				if (!(64 <= key && key <= 95)) {
					buffer [0] = (byte) key;
				}
			}
		}
		
		/*
		* Bug in Motif.  On HP-UX only, Shift+F9, Shift+F10, Shift+F11
		* and Shift+F12 are not translated correctly by XLookupString.
		* The fix is to look for these values explicitly and correct them.
		*/
		if (OS.IsHPUX && keysym [0] != 0) {
			switch (keysym [0]) {
				case 0xFF91:
					keysym [0] = OS.XK_F9;
					break;
				case 0xFF92:
					keysym [0] = OS.XK_F10;
					break;
				case 0xFF93:
					keysym [0] = OS.XK_F11;
					break;
				case 0xFF94:
					keysym [0] = OS.XK_F12;
					break;
			}
		}
		
		/*
		* Bug in Motif.  There are some keycodes for which 
		* XLookupString() does not translate the character.
		* Some of examples are Shift+Tab and Ctrl+Space.
		*/
		switch (keysym [0]) {
			case OS.XK_ISO_Left_Tab: buffer [0] = '\t'; break;
			case OS.XK_space: buffer [0] = ' '; break;
		}
			
		/* Fill in the event keyCode or character */
		if (keysym [0] != 0) {
			event.keyCode = Display.translateKey (keysym [0]);
		}
		if (buffer [0] != 0) {
			event.character = mbcsToWcs (buffer [0] & 0xFF);
		}
	}
	setInputState (event, xEvent);
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
	Display display = getDisplay ();
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
 */
public void setData (Object data) {
	checkWidget();
	this.data = data;
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
 * @see #getData
 */
public void setData (String key, Object value) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	/* Remove the key/value pair */
	if (value == null) {
		if (keys == null) return;
		int index = 0;
		while (index < keys.length && !keys [index].equals (key)) index++;
		if (index == keys.length) return;
		if (keys.length == 1) {
			keys = null;
			values = null;
		} else {
			String [] newKeys = new String [keys.length - 1];
			Object [] newValues = new Object [values.length - 1];
			System.arraycopy (keys, 0, newKeys, 0, index);
			System.arraycopy (keys, index + 1, newKeys, index, newKeys.length - index);
			System.arraycopy (values, 0, newValues, 0, index);
			System.arraycopy (values, index + 1, newValues, index, newValues.length - index);
			keys = newKeys;
			values = newValues;
		}
		return;
	}
	
	/* Add the key/value pair */
	if (keys == null) {
		keys = new String [] {key};
		values = new Object [] {value};
		return;
	}
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) {
			values [i] = value;
			return;
		}
	}
	String [] newKeys = new String [keys.length + 1];
	Object [] newValues = new Object [values.length + 1];
	System.arraycopy (keys, 0, newKeys, 0, keys.length);
	System.arraycopy (values, 0, newValues, 0, values.length);
	newKeys [keys.length] = key;
	newValues [values.length] = value;
	keys = newKeys;
	values = newValues;
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
boolean translateAccelerator (int key, int keysym, XKeyEvent xEvent, boolean doit) {
	return false;
}
boolean translateMnemonic (int key, XKeyEvent xEvent) {
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
	Display display = getDisplay ();
	boolean oldFocusOut = display.postFocusOut;
	display.postFocusOut = true;
	boolean result = OS.XmProcessTraversal (widget, direction);
	display.postFocusOut = oldFocusOut;
	if (!display.postFocusOut) display.runFocusOutEvents ();
	return result;
}
int wcsToMbcs (char ch) {
	return wcsToMbcs (ch, null);
}
int wcsToMbcs (char ch, String codePage) {
	int key = ch & 0xFFFF;
	if (key <= 0x7F) return ch;
	byte [] buffer = Converter.wcsToMbcs (codePage, new char [] {ch}, false);
	if (buffer.length == 1) return (char) buffer [0];
	if (buffer.length == 2) {
		return (char) (((buffer [0] & 0xFF) << 8) | (buffer [1] & 0xFF));
	}
	return 0;
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
		case PAGE_DECREMENT_CALLBACK:		return XmNpageDecrementCallback (w, client_data, call_data);
		case PAGE_INCREMENT_CALLBACK:		return XmNpageIncrementCallback (w, client_data, call_data);
		case SELECTION_CALLBACK:			return XmNselectionCallback (w, client_data, call_data);
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
	return 0;
}
int XKeyRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XLeaveWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	return 0;
}
int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
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
int XmNpageDecrementCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNpageIncrementCallback (int w, int client_data, int call_data) {
	return 0;
}
int XmNselectionCallback (int w, int client_data, int call_data) {
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

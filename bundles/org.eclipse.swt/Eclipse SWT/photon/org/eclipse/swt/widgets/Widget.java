/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.internal.photon.*;
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
	 * the handle to the resource 
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
	static final int DISPOSED		= 1 << 0;
	static final int CANVAS			= 1 << 1;
	static final int KEYED_DATA		= 1 << 2;
	static final int HANDLE			= 1 << 3;
	static final int DISABLED		= 1 << 4;
	static final int MOVED			= 1 << 5;
	static final int RESIZED		= 1 << 6;
	static final int GRAB			= 1 << 7;
	
	/* A layout was requested on this widget */
	static final int LAYOUT_NEEDED	= 1 << 8;
	
	/* The preferred size of a child has changed */
	static final int LAYOUT_CHANGED	= 1 << 9;
	
	/* A layout was requested in this widget hierachy */
	static final int LAYOUT_CHILD	= 1 << 10;

	/* More global state flags */
	static final int RELEASED		= 1<<11;
	static final int DISPOSE_SENT	= 1<<12;
	
	/* Notify of the opportunity to skin this widget */
	static final int SKIN_NEEDED = 1<<13;
	
	/* Default size for widgets */
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;

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
	if (parent.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	parent.checkWidget ();
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

int copyPhImage(int image) {
	if (image == 0) return 0;
	int newImage = OS.PiDuplicateImage (image, 0);

	/*
	* Bug in Photon.  The image returned by PiDuplicateImage might
	* have the same mask_bm/alpha as the original image.  The fix
	* is to detect this case and copy mask_bm/alpha if necessary.
	*/
	PhImage_t phImage = new PhImage_t();
	OS.memmove (phImage, image, PhImage_t.sizeof);
	PhImage_t newPhImage = new PhImage_t();
	OS.memmove(newPhImage, newImage, PhImage_t.sizeof);
	if (newPhImage.mask_bm != 0 && phImage.mask_bm == newPhImage.mask_bm) {
		int length = newPhImage.mask_bpl * newPhImage.size_h;
		int ptr = OS.malloc(length);
		OS.memmove(ptr, newPhImage.mask_bm, length);
		newPhImage.mask_bm = ptr;
	}
	if (newPhImage.alpha != 0 && phImage.alpha == newPhImage.alpha) {
		PgAlpha_t alpha = new PgAlpha_t();
		OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
		if (alpha.src_alpha_map_map != 0) {
			int length = alpha.src_alpha_map_bpl * alpha.src_alpha_map_dim_h;
			int ptr = OS.malloc(length);
			OS.memmove(ptr, alpha.src_alpha_map_map, length);
			alpha.src_alpha_map_map = ptr;
		}
		int ptr = OS.malloc(PgAlpha_t.sizeof);
		OS.memmove(ptr, alpha, PgAlpha_t.sizeof);
		newPhImage.alpha = ptr;
	}
	OS.memmove(newImage, newPhImage, PhImage_t.sizeof);
	return newImage;
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

void click (int widget) {
	int rid = OS.PtWidgetRid (widget);
	if (rid == 0) return;
	PhEvent_t event = new PhEvent_t ();
	event.emitter_rid = rid;
	event.emitter_handle = widget;
	event.collector_rid = rid;
	event.collector_handle = widget;
	event.flags = (short) OS.Ph_EVENT_DIRECT;
	event.processing_flags = (short) OS.Ph_FAKE_EVENT;
	event.type = OS.Ph_EV_BUT_PRESS;
	event.num_rects = 1;
	PhPointerEvent_t pe = new PhPointerEvent_t ();
	pe.click_count = 1;
	pe.buttons = (short) OS.Ph_BUTTON_SELECT;
	PhRect_t rect = new PhRect_t ();
	int ptr = OS.malloc (PhEvent_t.sizeof + PhPointerEvent_t.sizeof + PhRect_t.sizeof);
	OS.memmove (ptr, event, PhEvent_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof,  rect, PhRect_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof + PhRect_t.sizeof, pe, PhPointerEvent_t.sizeof);
	OS.PtSendEventToWidget (widget, ptr);	
	OS.PtFlush ();
	event.type = OS.Ph_EV_BUT_RELEASE;
	event.subtype = (short) OS.Ph_EV_RELEASE_REAL;
	OS.memmove (ptr, event, PhEvent_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof,  rect, PhRect_t.sizeof);
	OS.memmove (ptr + PhEvent_t.sizeof + PhRect_t.sizeof, pe, PhPointerEvent_t.sizeof);
	OS.PtSendEventToWidget (widget, ptr);	
	OS.free (ptr);
}

void createHandle (int index) {
	/* Do nothing */
}

int createToolTip (String string, int handle, byte [] font) {
	if (string == null || string.length () == 0 || handle == 0) {
		return 0;
	}

	int shellHandle = OS.PtFindDisjoint (handle);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int fill = display.INFO_BACKGROUND;
	int text_color = display.INFO_FOREGROUND;
	int toolTipHandle = OS.PtInflateBalloon (shellHandle, handle, OS.Pt_BALLOON_RIGHT, buffer, font, fill, text_color);

	/*
	* Feature in Photon. The position of the inflated balloon
	* is relative to the widget position and not to the cursor
	* position. The fix is to re-position the balloon.
	*/
	int ig = OS.PhInputGroup (0);
	PhCursorInfo_t info = new PhCursorInfo_t ();
	OS.PhQueryCursor ((short)ig, info);
	short [] absX = new short [1], absY = new short [1];
	OS.PtGetAbsPosition (shellHandle, absX, absY);
	int x = info.pos_x - absX [0] + 16;
	int y = info.pos_y - absY [0] + 16;
	PhArea_t shellArea = new PhArea_t ();
	OS.PtWidgetArea (shellHandle, shellArea);
	PhArea_t toolTipArea = new PhArea_t ();
	OS.PtWidgetArea (toolTipHandle, toolTipArea);
	x = Math.max (0, Math.min (x, shellArea.size_w - toolTipArea.size_w));
	y = Math.max (0, Math.min (y, shellArea.size_h - toolTipArea.size_h));
	PhPoint_t pt = new PhPoint_t ();
	pt.x = (short) x;
	pt.y = (short) y;
	int ptr = OS.malloc (PhPoint_t.sizeof);
	OS.memmove (ptr, pt, PhPoint_t.sizeof);
	OS.PtSetResource (toolTipHandle, OS.Pt_ARG_POS, ptr, 0);
	OS.free (ptr);

	return toolTipHandle;
}

void createWidget (int index) {
	createHandle (index);
	hookEvents ();
	register ();
}

void deregister () {
	if (handle == 0) return;
	WidgetTable.remove (handle);
}

void destroyToolTip (int toolTipHandle) {
	if (toolTipHandle != 0) OS.PtDestroyWidget (toolTipHandle);
}

void destroyWidget () {
	int topHandle = topHandle ();
	releaseHandle ();
	if (topHandle != 0) {
		OS.PtDestroyWidget (topHandle);
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

int drawProc (int widget, int damage) {
	return OS.Pt_CONTINUE;
}

static void error (int code) {
	SWT.error(code);
}

boolean filters (int eventType) {
	return display.filters (eventType);
}

char fixMnemonic (char [] buffer) {
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
	int index = string.length ();
	while (--index > 0 && string.charAt (index) != '.') {/* empty */}
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

boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}

int hotkeyProc (int widget, int data, int info) {
	return OS.Pt_CONTINUE;
}

void hookEvents () {
	/* Do nothing */
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
	if (handle != 0) return false;
	if ((state & HANDLE) != 0) return true;
	return (state & DISPOSED) != 0;
}

boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
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

boolean isValidThread () {
	return getDisplay ().isValidThread ();
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

int Ph_EV_BOUNDARY (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Ph_EV_BUT_PRESS (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Ph_EV_BUT_RELEASE (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Ph_EV_DRAG (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Ph_EV_KEY (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Ph_EV_PTR_MOTION (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_ACTIVATE (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_ARM (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_GOT_FOCUS (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_LOST_FOCUS (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_MODIFY_VERIFY (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_NUMERIC_CHANGED (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_OUTBOUND (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_PG_PANEL_SWITCHING (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_REALIZED (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_RESIZE (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_SCROLL_MOVE (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_SLIDER_MOVE (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_SELECTION (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_TEXT_CHANGED (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_TIMER_ACTIVATE (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_UNREALIZED (int widget, int info) {
	return OS.Pt_CONTINUE;
}

int Pt_CB_WINDOW (int widget, int info) {
	return OS.Pt_CONTINUE;
}


void register () {
	if (handle == 0) return;
	WidgetTable.put (handle, this);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}

void replaceMnemonic (int mnemonic, boolean normal, boolean alt) {
	int [] args = {OS.Pt_ARG_ACCEL_KEY, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] != 0) {
		int length = OS.strlen (args [1]);
		if (length > 0) {
			byte [] buffer = new byte [length];
			OS.memmove (buffer, args [1], length);
			char [] accelText = Converter.mbcsToWcs (null, buffer);
			if (accelText.length > 0) {
				char key = Character.toLowerCase (accelText [0]);
				if (normal) {
					//TEMPORARY CODE
//					OS.PtRemoveHotkeyHandler (handle, key, 0, (short)0, handle, display.hotkeyProc);
				}
				if (alt) {
					OS.PtRemoveHotkeyHandler (handle, key, OS.Pk_KM_Alt, (short)0, handle, display.hotkeyProc);
				}
			}
		}
	}
	if (mnemonic == 0) return;
	char key = Character.toLowerCase ((char)mnemonic);
	if (normal) {
		//TEMPORARY CODE
//		OS.PtAddHotkeyHandler (handle, key, 0, (short)0, SWT.Activate, display.windowProc);
	}
	if (alt) {
		OS.PtAddHotkeyHandler (handle, key, OS.Pk_KM_Alt, (short)0, handle, display.hotkeyProc);
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

boolean setInputState (Event event, int type, int key_mods, int button_state) {
	if ((key_mods & OS.Pk_KM_Alt) != 0) event.stateMask |= SWT.ALT;
	if ((key_mods & OS.Pk_KM_Shift) != 0) event.stateMask |= SWT.SHIFT;
	if ((key_mods & OS.Pk_KM_Ctrl) != 0) event.stateMask |= SWT.CONTROL;
	if ((button_state & OS.Ph_BUTTON_SELECT) != 0) event.stateMask |= SWT.BUTTON1;
	if ((button_state & OS.Ph_BUTTON_ADJUST) != 0) event.stateMask |= SWT.BUTTON2;
	if ((button_state & OS.Ph_BUTTON_MENU) != 0) event.stateMask |= SWT.BUTTON3;
	switch (type) {
		case SWT.MouseDown:
		case SWT.MouseDoubleClick:
			if (event.button == 1) event.stateMask &= ~SWT.BUTTON1;
			if (event.button == 2) event.stateMask &= ~SWT.BUTTON2;
			if (event.button == 3) event.stateMask &= ~SWT.BUTTON3;
			break;
		case SWT.MouseUp:
			if (event.button == 1) event.stateMask |= SWT.BUTTON1;
			if (event.button == 2) event.stateMask |= SWT.BUTTON2;
			if (event.button == 3) event.stateMask |= SWT.BUTTON3;
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

boolean setKeyState (Event event, int type, PhKeyEvent_t ke) {
	int key = 0;
	boolean isNull = false;
	if ((ke.key_flags & OS.Pk_KF_Cap_Valid) != 0) {
		key = ke.key_cap;
		if ((ke.key_mods & OS.Pk_KM_Num_Lock) == 0) {
			switch (key) {
				case OS.Pk_KP_0: key = OS.Pk_Insert; break;
				case OS.Pk_KP_1: key = OS.Pk_End; break;
				case OS.Pk_KP_2: key = OS.Pk_Down; break;
				case OS.Pk_KP_3: key = OS.Pk_Pg_Down; break;
				case OS.Pk_KP_4: key = OS.Pk_Left; break;
				case OS.Pk_KP_5: break;
				case OS.Pk_KP_6: key = OS.Pk_Right; break;
				case OS.Pk_KP_7: key = OS.Pk_Home; break;
				case OS.Pk_KP_8: key = OS.Pk_Up; break;
				case OS.Pk_KP_9: key = OS.Pk_Pg_Up; break;
				case OS.Pk_KP_Decimal: key = OS.Pk_Delete; break;
			}
			
		}
		event.keyCode = Display.translateKey (key);
	}
	switch (key) {
		case OS.Pk_BackSpace:		event.character = '\b'; break;
		case OS.Pk_Linefeed:		event.character = '\n'; break;
		case OS.Pk_KP_Enter:
		case OS.Pk_Return: 		event.character = '\r'; break;
		case OS.Pk_Delete:		event.character = 0x7F; break;
		case OS.Pk_Escape:		event.character = 0x1B; break;
		case OS.Pk_KP_Tab:
		case OS.Pk_Tab: 	event.character = '\t'; break;
		/* These keys have no mapping in SWT yet */
		case OS.Pk_Clear:
		case OS.Pk_Menu:
		case OS.Pk_Hyper_L:
		case OS.Pk_Hyper_R:
			break;
		default: {
			if (event.keyCode == 0) {
				if ((ke.key_flags & OS.Pk_KF_Cap_Valid) != 0) {
					event.keyCode = ke.key_cap;
				}
			}
			/*
			* Fetuare in Photon.  The key_sym value is not valid when Ctrl
			* or Alt is pressed. The fix is to detect this case and try to
			* use the key_cap value.
			*/
			if ((ke.key_mods & (OS.Pk_KM_Alt | OS.Pk_KM_Ctrl)) != 0) {
				if (0 <= key && key <= 0x7F) {
					if ((ke.key_mods & OS.Pk_KM_Ctrl) != 0) {
						isNull = key == '@';
						if ('a' <= key && key <= 'z') key -= 'a' - 'A';
						if (64 <= key && key <= 95) key -= 64;
						event.character = (char) key;
						isNull &= key == 0;
					} else {
						if ((ke.key_flags & OS.Pk_KF_Sym_Valid) != 0) {
							event.character = (char) ke.key_sym;
						}
					}
				}
			} else {
				byte [] buffer = new byte [6];
				int length = OS.PhKeyToMb (buffer, ke);
				if (length > 0) {
					char [] unicode = Converter.mbcsToWcs (null, buffer);
					if (unicode.length > 0) event.character = unicode [0];
				}
			}
		}
	}
	if (event.keyCode == 0 && event.character == 0) {
		if (!isNull) return false;
	}
	return setInputState (event, type, ke.key_mods, ke.button_state);
}

boolean setMouseState(Event event, int type, PhPointerEvent_t pe, PhEvent_t ev) {
	int buttons = pe.buttons;
	event.x = pe.pos_x + ev.translation_x;
	event.y = pe.pos_y + ev.translation_y;
	if (ev.type == OS.Ph_EV_BUT_PRESS || ev.type == OS.Ph_EV_BUT_RELEASE) {
		switch (buttons) {
			case OS.Ph_BUTTON_SELECT:	event.button = 1; break;
			case OS.Ph_BUTTON_ADJUST:	event.button = 2; break;
			case OS.Ph_BUTTON_MENU:		event.button = 3; break;
		}
	}
	return setInputState(event, type, pe.key_mods, pe.button_state);
}

int topHandle () {
	return handle;
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

int windowProc (int handle, int data, int info) {
	switch (data) {
		case OS.Ph_EV_BOUNDARY:			return Ph_EV_BOUNDARY (handle, info);
		case OS.Ph_EV_BUT_PRESS:			return Ph_EV_BUT_PRESS (handle, info);
		case OS.Ph_EV_BUT_RELEASE:			return Ph_EV_BUT_RELEASE (handle, info);
		case OS.Ph_EV_DRAG:			return Ph_EV_DRAG (handle, info);
		case OS.Ph_EV_KEY:			return Ph_EV_KEY (handle, info);
		case OS.Ph_EV_PTR_MOTION:			return Ph_EV_PTR_MOTION (handle, info);
		case OS.Pt_CB_ACTIVATE:			return Pt_CB_ACTIVATE (handle, info);
		case OS.Pt_CB_ARM:			return Pt_CB_ARM (handle, info);
		case OS.Pt_CB_GOT_FOCUS:			return Pt_CB_GOT_FOCUS (handle, info);
		case OS.Pt_CB_LOST_FOCUS:			return Pt_CB_LOST_FOCUS (handle, info);
		case OS.Pt_CB_MODIFY_VERIFY:			return Pt_CB_MODIFY_VERIFY (handle, info);
		case OS.Pt_CB_NUMERIC_CHANGED:			return Pt_CB_NUMERIC_CHANGED (handle, info);
		case OS.Pt_CB_OUTBOUND:			return Pt_CB_OUTBOUND (handle, info);
		case OS.Pt_CB_PG_PANEL_SWITCHING:			return Pt_CB_PG_PANEL_SWITCHING (handle, info);
		case OS.Pt_CB_REALIZED:			return Pt_CB_REALIZED (handle, info);
		case OS.Pt_CB_RESIZE:			return Pt_CB_RESIZE (handle, info);
		case OS.Pt_CB_SCROLL_MOVE:			return Pt_CB_SCROLL_MOVE (handle, info);
		case OS.Pt_CB_SLIDER_MOVE:			return Pt_CB_SLIDER_MOVE (handle, info);
		case OS.Pt_CB_SELECTION:			return Pt_CB_SELECTION (handle, info);
		case OS.Pt_CB_TEXT_CHANGED:			return Pt_CB_TEXT_CHANGED (handle, info);
		case OS.Pt_CB_TIMER_ACTIVATE:			return Pt_CB_TIMER_ACTIVATE (handle, info);
		case OS.Pt_CB_UNREALIZED:			return Pt_CB_UNREALIZED (handle, info);
		case OS.Pt_CB_WINDOW:			return Pt_CB_WINDOW (handle, info);
	}
	return OS.Pt_CONTINUE;
}

}

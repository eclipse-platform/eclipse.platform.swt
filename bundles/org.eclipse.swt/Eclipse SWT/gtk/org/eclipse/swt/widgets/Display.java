package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are responsible for managing the
 * connection between SWT and the underlying operating
 * system. Their most important function is to implement
 * the SWT event loop in terms of the platform event model.
 * They also provide various methods for accessing information
 * about the operating system, and have overall control over
 * the operating system resources which SWT allocates.
 * <p>
 * Applications which are built with SWT will <em>almost always</em>
 * require only a single display. In particular, some platforms
 * which SWT supports will not allow more than one <em>active</em>
 * display. In other words, some platforms do not support
 * creating a new display if one already exists that has not been
 * sent the <code>dispose()</code> message.
 * <p>
 * In SWT, the thread which creates a <code>Display</code>
 * instance is distinguished as the <em>user-interface thread</em>
 * for that display.
 * </p>
 * The user-interface thread for a particular display has the
 * following special attributes:
 * <ul>
 * <li>
 * The event loop for that display must be run from the thread.
 * </li>
 * <li>
 * Some SWT API methods (notably, most of the public methods in
 * <code>Widget</code> and its subclasses), may only be called
 * from the thread. (To support multi-threaded user-interface
 * applications, class <code>Display</code> provides inter-thread
 * communication methods which allow threads other than the 
 * user-interface thread to request that it perform operations
 * on their behalf.)
 * </li>
 * <li>
 * The thread is not allowed to construct other 
 * <code>Display</code>s until that display has been disposed.
 * (Note that, this is in addition to the restriction mentioned
 * above concerning platform support for multiple displays. Thus,
 * the only way to have multiple simultaneously active displays,
 * even on platforms which support it, is to have multiple threads.)
 * </li>
 * </ul>
 * Enforcing these attributes allows SWT to be implemented directly
 * on the underlying operating system's event model. This has 
 * numerous benefits including smaller footprint, better use of 
 * resources, safer memory management, clearer program logic,
 * better performance, and fewer overall operating system threads
 * required. The down side however, is that care must be taken
 * (only) when constructing multi-threaded applications to use the
 * inter-thread communication mechanisms which this class provides
 * when required.
 * </p><p>
 * All SWT API methods which may only be called from the user-interface
 * thread are distinguished in their documentation by indicating that
 * they throw the "<code>ERROR_THREAD_INVALID_ACCESS</code>"
 * SWT exception.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Close, Dispose</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * @see #syncExec
 * @see #asyncExec
 * @see #wake
 * @see #readAndDispatch
 * @see #sleep
 * @see #dispose
 */
public class Display extends Device {

	/* Events Dispatching and Callback */
	Event [] eventQueue;
	int windowProc2, windowProc3, windowProc4, windowProc5;
	Callback windowCallback2, windowCallback3, windowCallback4, windowCallback5;
	EventTable eventTable, filterTable;
	static String APP_NAME = "SWT";

	/* Input method resources */
	int preeditWindow, preeditLabel;

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread;

	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Timers */
	int [] timerIds;
	Runnable [] timerList;
	Callback timerCallback;
	int timerProc;
	Callback windowTimerCallback;
	int windowTimerProc;
	
	/* Caret */
	Caret currentCaret;
	Callback caretCallback;
	int caretId, caretProc;
	
	/* Mouse hover */
	int mouseHoverId, mouseHoverHandle, mouseHoverProc;
	Callback mouseHoverCallback;
	
	/* GtkTreeView callbacks */
	int[] treeSelection;
	int treeSelectionLength;
	int treeSelectionProc;
	Callback treeSelectionCallback;
	
	/* Drag Detect */
	int dragStartX,dragStartY;
	boolean dragging;
	
	/* Fonts */
	int defaultFont;
	
	/* Colors */
	GdkColor COLOR_WIDGET_DARK_SHADOW, COLOR_WIDGET_NORMAL_SHADOW, COLOR_WIDGET_LIGHT_SHADOW;
	GdkColor COLOR_WIDGET_HIGHLIGHT_SHADOW, COLOR_WIDGET_BACKGROUND, COLOR_WIDGET_FOREGROUND, COLOR_WIDGET_BORDER;
	GdkColor COLOR_LIST_FOREGROUND, COLOR_LIST_BACKGROUND, COLOR_LIST_SELECTION, COLOR_LIST_SELECTION_TEXT;
	GdkColor COLOR_TEXT_FOREGROUND, COLOR_TEXT_BACKGROUND, COLOR_INFO_BACKGROUND, COLOR_INFO_FOREGROUND;
	GdkColor COLOR_TITLE_FOREGROUND, COLOR_TITLE_BACKGROUND, COLOR_TITLE_BACKGROUND_GRADIENT;
	GdkColor COLOR_TITLE_INACTIVE_FOREGROUND, COLOR_TITLE_INACTIVE_BACKGROUND, COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT;
		
	/* Key Mappings */
	static final int [] [] KeyTable = {
		
		/* Keyboard and Mouse Masks */
		{OS.GDK_Alt_L,		SWT.ALT},
		{OS.GDK_Alt_R,		SWT.ALT},
		{OS.GDK_Shift_L,	SWT.SHIFT},
		{OS.GDK_Shift_R,	SWT.SHIFT},
		{OS.GDK_Control_L,	SWT.CONTROL},
		{OS.GDK_Control_R,	SWT.CONTROL},
		
		/* Non-Numeric Keypad Keys */
		{OS.GDK_Up,			SWT.ARROW_UP},
		{OS.GDK_Down,		SWT.ARROW_DOWN},
		{OS.GDK_Left,		SWT.ARROW_LEFT},
		{OS.GDK_Right,		SWT.ARROW_RIGHT},
		{OS.GDK_Page_Up,	SWT.PAGE_UP},
		{OS.GDK_Page_Down,	SWT.PAGE_DOWN},
		{OS.GDK_Home,		SWT.HOME},
		{OS.GDK_End,		SWT.END},
		{OS.GDK_Insert,		SWT.INSERT},
		
		/* Virtual and Ascii Keys */
		{OS.GDK_BackSpace,		SWT.BS},
		{OS.GDK_Return,			SWT.CR},
		{OS.GDK_Delete,			SWT.DEL},
		{OS.GDK_Escape,			SWT.ESC},
		{OS.GDK_Cancel,			SWT.ESC},
		{OS.GDK_Linefeed,		SWT.LF},
		{OS.GDK_Tab,			SWT.TAB},
		{OS.GDK_ISO_Left_Tab, 	SWT.TAB},
	
		/* Functions Keys */
		{OS.GDK_F1,		SWT.F1},
		{OS.GDK_F2,		SWT.F2},
		{OS.GDK_F3,		SWT.F3},
		{OS.GDK_F4,		SWT.F4},
		{OS.GDK_F5,		SWT.F5},
		{OS.GDK_F6,		SWT.F6},
		{OS.GDK_F7,		SWT.F7},
		{OS.GDK_F8,		SWT.F8},
		{OS.GDK_F9,		SWT.F9},
		{OS.GDK_F10,	SWT.F10},
		{OS.GDK_F11,	SWT.F11},
		{OS.GDK_F12,	SWT.F12},
		
		/* Numeric Keypad Constants */
//		{OS.GDK_KP_Add,		SWT.KP_PLUS},
//		{OS.GDK_KP_Subtract,	SWT.KP_MINUS},
//		{OS.GDK_KP_Multiply,	SWT.KP_TIMES},
//		{OS.GDK_KP_Divide,	SWT.KP_DIVIDE},
//		{OS.GDK_KP_Decimal,	SWT.KP_PERIOD},
//		{OS.GDK_KP_Enter,	SWT.CR},
//		{OS.GDK_KP_0,		SWT.KP_0},
//		{OS.GDK_KP_1,		SWT.KP_1},
//		{OS.GDK_KP_2,		SWT.KP_2},
//		{OS.GDK_KP_3,		SWT.KP_3},
//		{OS.GDK_KP_4,		SWT.KP_4},
//		{OS.GDK_KP_5,		SWT.KP_5},
//		{OS.GDK_KP_6,		SWT.KP_6},
//		{OS.GDK_KP_7,		SWT.KP_7},
//		{OS.GDK_KP_8,		SWT.KP_8},
//		{OS.GDK_KP_9,		SWT.KP_9},
		
	};

	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Package name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";
	/* This code is intentionally commented.
	 * ".class" can not be used on CLDC.
	 */
	/*static {
		String name = Display.class.getName ();
		int index = name.lastIndexOf ('.');
		PACKAGE_NAME = name.substring (0, index + 1);
	}*/

	/* #define in gdkevents.h */
	static final int DOUBLE_CLICK_TIME = 250;

	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;
	
	/* Initial Guesses for Shell Trimmings. */
	int borderTrimWidth = 4, borderTrimHeight = 4;
	int resizeTrimWidth = 6, resizeTrimHeight = 6;
	int titleBorderTrimWidth = 5, titleBorderTrimHeight = 28;
	int titleResizeTrimWidth = 6, titleResizeTrimHeight = 29;
	int titleTrimWidth = 0, titleTrimHeight = 23;
	
	/*
	* TEMPORARY CODE.  Install the runnable that
	* gets the current display. This code will
	* be removed in the future.
	*/
	static {
		DeviceFinder = new Runnable () {
			public void run () {
				Device device = getCurrent ();
				if (device == null) {
					device = getDefault ();
				}
				setDevice (device);
			}
		};
	}

/*
* TEMPORARY CODE.
*/
static void setDevice (Device device) {
	CurrentDevice = device;
}

/**
 * Constructs a new instance of this class.
 * <p>
 * Note: The resulting display is marked as the <em>current</em>
 * display. If this is the first display which has been 
 * constructed since the application started, it is also
 * marked as the <em>default</em> display.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see #getCurrent
 * @see #getDefault
 * @see Widget#checkSubclass
 * @see Shell
 */
public Display () {
	this (null);
}

public Display (DeviceData data) {
	super (data);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs anywhere
 * in SWT. When the event does occur, the listener is notified
 * by sending it the <code>handleEvent()</code> message.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #removeFilter
 * @see #removeListener
 * 
 * @since 2.1 
 */
public void addFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) filterTable = new EventTable ();
	filterTable.hook (eventType, listener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notifed when an event of the given type occurs. When the
 * event does occur in the display, the listener is notified by
 * sending it the <code>handleEvent()</code> message.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #removeListener
 * 
 * @since 2.0 
 */
public void addListener (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, listener);
}

void addMouseHoverTimeout (int handle) {
	if (mouseHoverId != 0) OS.gtk_timeout_remove (mouseHoverId);
	mouseHoverId = OS.gtk_timeout_add (400, mouseHoverProc, handle);
	mouseHoverHandle = handle;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next 
 * reasonable opportunity. The caller of this method continues 
 * to run in parallel, and is not notified when the
 * runnable has completed.
 *
 * @param runnable code to run on the user-interface thread.
 *
 * @see #syncExec
 */
public void asyncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.asyncExec (runnable);
}

/**
 * Causes the system hardware to emit a short sound
 * (if it supports this capability).
 */
public void beep () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	OS.gdk_beep();
	OS.gdk_flush();
}

protected void checkDevice () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

static synchronized void checkDisplay (Thread thread) {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] != null && Displays [i].thread == thread) {
			SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
}

protected void checkSubclass () {
	if (!isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Requests that the connection between SWT and the underlying
 * operating system be closed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #dispose
 * 
 * @since 2.0
 */
public void close () {
	checkDevice ();
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit) dispose ();
}

protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay(thread = Thread.currentThread ());
	createDisplay (data);
	register ();
	if (Default == null) Default = this;
}

synchronized void createDisplay (DeviceData data) {
	/*
	* This code is intentionally commneted.	*/
//	if (!OS.g_thread_supported ()) {
//		OS.g_thread_init (0);
//		OS.gdk_threads_init ();
//	}
	OS.gtk_set_locale();
	if (!OS.gtk_init_check (new int [] {0}, null)) {
		SWT.error (SWT.ERROR_DEVICE_DISPOSED);
		return;
	}
	OS.gdk_rgb_init ();
	int ptr = OS.gtk_check_version (2, 0, 1);
	if (ptr != 0) {
		int length = OS.strlen (ptr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		System.out.println ("***WARNING: " + new String (Converter.mbcsToWcs (null, buffer)));
	}
	byte [] buffer = Converter.wcsToMbcs (null, APP_NAME, true);
	OS.gdk_set_program_class(buffer);
}

synchronized void deregister () {
	for (int i=0; i<Displays.length; i++) {
		if (this == Displays [i]) Displays [i] = null;
	}
}

protected void destroy () {
	if (this == Default) Default = null;
	deregister ();
	destroyDisplay ();
}

void destroyDisplay () {
}

/**
 * Returns the display which the given thread is the
 * user-interface thread for, or null if the given thread
 * is not a user-interface thread for any display.
 *
 * @param thread the user-interface thread
 * @return the display for the given thread
 */
public static synchronized Display findDisplay (Thread thread) {
	for (int i=0; i<Displays.length; i++) {
		Display display = Displays [i];
		if (display != null && display.thread == thread) {
			return display;
		}
	}
	return null;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread just before the
 * receiver is disposed.
 *
 * @param runnable code to run at dispose time.
 */
public void disposeExec (Runnable runnable) {
	checkDevice ();
	if (disposeList == null) disposeList = new Runnable [4];
	for (int i=0; i<disposeList.length; i++) {
		if (disposeList [i] == null) {
			disposeList [i] = runnable;
			return;
		}
	}
	Runnable [] newDisposeList = new Runnable [disposeList.length + 4];
	System.arraycopy (disposeList, 0, newDisposeList, 0, disposeList.length);
	newDisposeList [disposeList.length] = runnable;
	disposeList = newDisposeList;
}

/**
 * Does whatever display specific cleanup is required, and then
 * uses the code in <code>SWTError.error</code> to handle the error.
 *
 * @param code the descriptive error code
 *
 * @see SWTError#error
 */
void error (int code) {
	SWT.error (code);
}

/**
 * Given the operating system handle for a widget, returns
 * the instance of the <code>Widget</code> subclass which
 * represents it in the currently running application, if
 * such exists, or null if no matching widget can be found.
 *
 * @param handle the handle for the widget
 * @return the SWT widget that the handle represents
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Widget findWidget (int handle) {
	checkDevice ();
	// In 0.058 and before, this used to go up the parent
	// chain if the handle was not found in the widget
	// table, up to the root.  Now, we require that all
	// widgets register ALL their handles.
	// If somebody creates their own handles outside
	// SWT, it's their problem.
	return WidgetTable.get (handle);
}

/*
 * In SWT, we force all widgets to have real Gdk windows,
 * thus getting rid of the concept of "lightweight" widgets.
 * Given a GdkWindow, answer a handle to the GtkWidget realized
 * through that window.
 * If the argument is not the pointe rto a GdkWindow, the
 * universe will be left in an inconsistent state.
 */
int findGtkWidgetByGdkWindow (int gdkWindow) {
	int[] pwidget = new int[1];
	OS.gdk_window_get_user_data(gdkWindow, pwidget);
	return pwidget[0];
}	
 
/**
 * Returns the currently active <code>Shell</code>, or null
 * if no shell belonging to the currently running application
 * is active.
 *
 * @return the active shell or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Shell getActiveShell () {
	checkDevice ();
	Shell [] shells = getShells();
	for (int i=0; i<shells.length; i++) {
	   if (shells [i].hasFocus) return shells [i];
	}
	return null;
}

/**
 * Returns a rectangle describing the receiver's size and location.
 *
 * @return the bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkDevice ();
	return new Rectangle(0, 0, OS.gdk_screen_width (), OS.gdk_screen_height ());
}

/**
 * Returns the display which the currently running thread is
 * the user-interface thread for, or null if the currently
 * running thread is not a user-interface thread for any display.
 *
 * @return the current display
 */
public static synchronized Display getCurrent () {
	Thread current = Thread.currentThread ();
	for (int i=0; i<Displays.length; i++) {
		Display display = Displays [i];
		if (display != null && display.thread == current) return display;
	}
	return null;
}

/**
 * Returns the control which the on-screen pointer is currently
 * over top of, or null if it is not currently over one of the
 * controls built by the currently running application.
 *
 * @return the control under the cursor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getCursorControl () {
	checkDevice();
	int[] x = new int[1], y = new int[1];
	int cursorWindowHandle = OS.gdk_window_at_pointer(x,y);
	if (cursorWindowHandle==0) return null;
	int handle = findGtkWidgetByGdkWindow(cursorWindowHandle);
	if (handle==0) return null;
	return findControl(handle);
}

boolean filterEvent (Event event) {
	if (filterTable != null) filterTable.sendEvent (event);
	return false;
}

boolean filters (int eventType) {
	if (filterTable == null) return false;
	return filterTable.hooks (eventType);
}

Control findControl(int h) {
	Widget w = findWidget(h);
	if (w==null) return null;
	if (w instanceof Control) return (Control)w;

	/* w is something like an Item.  Go for the parent */
	return findControl(OS.gtk_widget_get_parent (h));
}

/**
 * Returns the location of the on-screen pointer relative
 * to the top left corner of the screen.
 *
 * @return the cursor location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getCursorLocation () {
	checkDevice ();
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_pointer (0, x, y, null);
	return new Point (x [0], y [0]);
}

/**
 * Returns the application defined property of the receiver
 * with the specified name, or null if it has not been set.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the display is disposed
 * of, it is the application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param key the name of the property
 * @return the value of the property or null if it has not been set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setData
 * @see #disposeExec
 */
public Object getData (String key) {
	checkDevice ();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

/**
 * Returns the application defined, display specific data
 * associated with the receiver, or null if it has not been
 * set. The <em>display specific data</em> is a single,
 * unnamed field that is stored with every display. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the display specific data needs to
 * be notified when the display is disposed of, it is the
 * application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @return the display specific data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 *
 * @see #setData
 * @see #disposeExec
 */
public Object getData () {
	checkDevice ();
	return data;
}

/**
 * Returns a point whose x coordinate is the horizontal
 * dots per inch of the display, and whose y coordinate
 * is the vertical dots per inch of the display.
 *
 * @return the horizontal and vertical DPI
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getDPI () {
	checkDevice ();
	/* Apparently, SWT believes pixels are always square */
	int widthMM = OS.gdk_screen_width_mm ();
	int width = OS.gdk_screen_width ();
	int dpi = Compatibility.round(254 * width, widthMM * 10);
	return new Point (dpi, dpi);
}

/**
 * Returns the default display. One is created (making the
 * thread that invokes this method its user-interface thread)
 * if it did not already exist.
 *
 * @return the default display
 */
public static synchronized Display getDefault () {
	if (Default == null) Default = new Display ();
	return Default;
}

static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
}

/**
 * Returns the longest duration, in milliseconds, between
 * two mouse button clicks that will be considered a
 * <em>double click</em> by the underlying operating system.
 *
 * @return the double click time
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDoubleClickTime () {
	checkDevice ();
	return DOUBLE_CLICK_TIME;
}

/**
 * Returns the control which currently has keyboard focus,
 * or null if keyboard events are not currently going to
 * any of the controls built by the currently running
 * application.
 *
 * @return the control under the cursor
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getFocusControl () {
	checkDevice ();
	Shell active = getActiveShell();
	if (active==null) return null;
	return active.getFocusControl();
}

public int getDepth () {
	checkDevice ();
	GdkVisual visual = new GdkVisual ();
	OS.memmove (visual, OS.gdk_visual_get_system());
	return visual.depth;
}

/**
 * Returns the maximum allowed depth of icons on this display.
 * On some platforms, this may be different than the actual
 * depth of the display.
 *
 * @return the maximum icon depth
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getIconDepth () {
	checkDevice ();
	return getDepth ();
}

int getLastEventTime () {
	return OS.gtk_get_current_event_time ();
}

/**
 * Returns an array containing all shells which have not been
 * disposed and have the receiver as their display.
 *
 * @return the receiver's shells
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Shell [] getShells () {
	checkDevice ();
	int count = 0;
	Shell [] shells = WidgetTable.shells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && (this == shell.getDisplay ())) {
			count++;
		}
	}
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && (this == shell.getDisplay ())) {
			result [index++] = shell;
		}
	}
	return result;
}

/**
 * Returns the thread that has invoked <code>syncExec</code>
 * or null if no such runnable is currently being invoked by
 * the user-interface thread.
 * <p>
 * Note: If a runnable invoked by asyncExec is currently
 * running, this method will return null.
 * </p>
 *
 * @return the receiver's sync-interface thread
 */
public Thread getSyncThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return synchronizer.syncThread;
}

/**
 * Returns the matching standard color for the given
 * constant, which should be one of the color constants
 * specified in class <code>SWT</code>. Any value other
 * than one of the SWT color constants which is passed
 * in will result in the color black. This color should
 * not be free'd because it was allocated by the system,
 * not the application.
 *
 * @param id the color constant
 * @return the matching color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 */
public Color getSystemColor (int id) {
	checkDevice ();
	GdkColor gdkColor = null;
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: 					gdkColor = COLOR_INFO_FOREGROUND; break;
		case SWT.COLOR_INFO_BACKGROUND: 					gdkColor = COLOR_INFO_BACKGROUND; break;
		case SWT.COLOR_TITLE_FOREGROUND:					gdkColor = COLOR_TITLE_FOREGROUND; break;
		case SWT.COLOR_TITLE_BACKGROUND:					gdkColor = COLOR_TITLE_BACKGROUND; break;
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:			gdkColor = COLOR_TITLE_BACKGROUND_GRADIENT; break;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:			gdkColor = COLOR_TITLE_INACTIVE_FOREGROUND; break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:			gdkColor = COLOR_TITLE_INACTIVE_BACKGROUND; break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:	gdkColor = COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT; break;
		case SWT.COLOR_WIDGET_DARK_SHADOW:					gdkColor = COLOR_WIDGET_DARK_SHADOW; break;
		case SWT.COLOR_WIDGET_NORMAL_SHADOW:				gdkColor = COLOR_WIDGET_NORMAL_SHADOW; break;
		case SWT.COLOR_WIDGET_LIGHT_SHADOW: 				gdkColor = COLOR_WIDGET_LIGHT_SHADOW; break;
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:				gdkColor = COLOR_WIDGET_HIGHLIGHT_SHADOW; break;
		case SWT.COLOR_WIDGET_BACKGROUND: 					gdkColor = COLOR_WIDGET_BACKGROUND; break;
		case SWT.COLOR_WIDGET_FOREGROUND: 					gdkColor = COLOR_WIDGET_FOREGROUND; break;
		case SWT.COLOR_WIDGET_BORDER: 						gdkColor = COLOR_WIDGET_BORDER; break;
		case SWT.COLOR_LIST_FOREGROUND: 					gdkColor = COLOR_LIST_FOREGROUND; break;
		case SWT.COLOR_LIST_BACKGROUND: 					gdkColor = COLOR_LIST_BACKGROUND; break;
		case SWT.COLOR_LIST_SELECTION: 						gdkColor = COLOR_LIST_SELECTION; break;
		case SWT.COLOR_LIST_SELECTION_TEXT: 				gdkColor = COLOR_LIST_SELECTION_TEXT; break;
		default:
			return super.getSystemColor (id);	
	}
	if (gdkColor == null) return super.getSystemColor (SWT.COLOR_BLACK);
	return Color.gtk_new (this, gdkColor);
}

void initializeSystemResources () {
	int shellHandle = OS.gtk_window_new (OS.GTK_WINDOW_TOPLEVEL);
	if (shellHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.gtk_widget_realize (shellHandle);
	
	int tooltipShellHandle = OS.gtk_window_new (OS.GTK_WINDOW_POPUP);
	if (tooltipShellHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	byte[] gtk_tooltips = Converter.wcsToMbcs (null, "gtk-tooltips", true);
	OS.gtk_widget_set_name (tooltipShellHandle, gtk_tooltips);
	OS.gtk_widget_realize (tooltipShellHandle);

	GdkColor gdkColor;
	GtkStyle style = new GtkStyle();
	OS.memmove (style, OS.gtk_widget_get_style (shellHandle));	
	GtkStyle tooltipStyle = new GtkStyle();
	OS.memmove (tooltipStyle, OS.gtk_widget_get_style (tooltipShellHandle));
	
	defaultFont = OS.pango_font_description_copy (style.font_desc);

	gdkColor = new GdkColor();
	gdkColor.pixel = style.black_pixel;
	gdkColor.red   = style.black_red;
	gdkColor.green = style.black_green;
	gdkColor.blue  = style.black_blue;
	COLOR_WIDGET_DARK_SHADOW = gdkColor;
	
	gdkColor = new GdkColor();
	gdkColor.pixel = style.dark0_pixel;
	gdkColor.red   = style.dark0_red;
	gdkColor.green = style.dark0_green;
	gdkColor.blue  = style.dark0_blue;
	COLOR_WIDGET_NORMAL_SHADOW = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.bg0_pixel;
	gdkColor.red   = style.bg0_red;
	gdkColor.green = style.bg0_green;
	gdkColor.blue  = style.bg0_blue;
	COLOR_WIDGET_LIGHT_SHADOW = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.light0_pixel;
	gdkColor.red   = style.light0_red;
	gdkColor.green = style.light0_green;
	gdkColor.blue  = style.light0_blue;
	COLOR_WIDGET_HIGHLIGHT_SHADOW = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.fg0_pixel;
	gdkColor.red   = style.fg0_red;
	gdkColor.green = style.fg0_green;
	gdkColor.blue  = style.fg0_blue;
	COLOR_WIDGET_FOREGROUND = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.bg0_pixel;
	gdkColor.red   = style.bg0_red;
	gdkColor.green = style.bg0_green;
	gdkColor.blue  = style.bg0_blue;
	COLOR_WIDGET_BACKGROUND = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.text0_pixel;
	gdkColor.red   = style.text0_red;
	gdkColor.green = style.text0_green;
	gdkColor.blue  = style.text0_blue;
	COLOR_TEXT_FOREGROUND = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.base0_pixel;
	gdkColor.red   = style.base0_red;
	gdkColor.green = style.base0_green;
	gdkColor.blue  = style.base0_blue;
	COLOR_TEXT_BACKGROUND = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.text0_pixel;
	gdkColor.red   = style.text0_red;
	gdkColor.green = style.text0_green;
	gdkColor.blue  = style.text0_blue;
	COLOR_LIST_FOREGROUND = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.base0_pixel;
	gdkColor.red   = style.base0_red;
	gdkColor.green = style.base0_green;
	gdkColor.blue  = style.base0_blue;
	COLOR_LIST_BACKGROUND = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.text3_pixel;
	gdkColor.red   = style.text3_red;
	gdkColor.green = style.text3_green;
	gdkColor.blue  = style.text3_blue;
	COLOR_LIST_SELECTION_TEXT = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = style.base3_pixel;
	gdkColor.red   = style.base3_red;
	gdkColor.green = style.base3_green;
	gdkColor.blue  = style.base3_blue;
	COLOR_LIST_SELECTION = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = tooltipStyle.fg0_pixel;
	gdkColor.red   = tooltipStyle.fg0_red;
	gdkColor.green = tooltipStyle.fg0_green;
	gdkColor.blue  = tooltipStyle.fg0_blue;
	COLOR_INFO_FOREGROUND = gdkColor;

	gdkColor = new GdkColor();
	gdkColor.pixel = tooltipStyle.bg0_pixel;
	gdkColor.red   = tooltipStyle.bg0_red;
	gdkColor.green = tooltipStyle.bg0_green;
	gdkColor.blue  = tooltipStyle.bg0_blue;
	COLOR_INFO_BACKGROUND = gdkColor;
	
	gdkColor = new GdkColor();
	gdkColor.pixel = style.bg3_pixel;
	gdkColor.red   = style.bg3_red;
	gdkColor.green = style.bg3_green;
	gdkColor.blue  = style.bg3_blue;
	COLOR_TITLE_BACKGROUND = gdkColor;
	
	gdkColor = new GdkColor();
	gdkColor.pixel = style.fg3_pixel;
	gdkColor.red   = style.fg3_red;
	gdkColor.green = style.fg3_green;
	gdkColor.blue  = style.fg3_blue;
	COLOR_TITLE_FOREGROUND = gdkColor;
	
	gdkColor = new GdkColor();
	gdkColor.pixel = style.light3_pixel;
	gdkColor.red   = style.light3_red;
	gdkColor.green = style.light3_green;
	gdkColor.blue  = style.light3_blue;
	COLOR_TITLE_BACKGROUND_GRADIENT = gdkColor;
	
	gdkColor = new GdkColor();
	gdkColor.pixel = style.bg4_pixel;
	gdkColor.red   = style.bg4_red;
	gdkColor.green = style.bg4_green;
	gdkColor.blue  = style.bg4_blue;
	COLOR_TITLE_INACTIVE_BACKGROUND = gdkColor;
	
	gdkColor = new GdkColor();
	gdkColor.pixel = style.fg4_pixel;
	gdkColor.red   = style.fg4_red;
	gdkColor.green = style.fg4_green;
	gdkColor.blue  = style.fg4_blue;
	COLOR_TITLE_INACTIVE_FOREGROUND = gdkColor;
	
	gdkColor = new GdkColor();
	gdkColor.pixel = style.light4_pixel;
	gdkColor.red   = style.light4_red;
	gdkColor.green = style.light4_green;
	gdkColor.blue  = style.light4_blue;
	COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT = gdkColor;

	OS.gtk_widget_destroy (tooltipShellHandle);
	OS.gtk_widget_destroy (shellHandle);
}

/**
 * Returns a reasonable font for applications to use.
 * On some platforms, this will match the "default font"
 * or "system font" if such can be found.  This font
 * should not be free'd because it was allocated by the
 * system, not the application.
 * <p>
 * Typically, applications which want the default look
 * should simply not set the font on the widgets they
 * create. Widgets are always created with the correct
 * default font for the class of user-interface component
 * they represent.
 * </p>
 *
 * @return a font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Font getSystemFont () {
	checkDevice ();
	return Font.gtk_new (this, defaultFont);
}

/**
 * Returns the user-interface thread for the receiver.
 *
 * @return the receiver's user-interface thread
 */
public Thread getThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return thread;
}

protected void init () {
	super.init ();
	initializeCallbacks ();
	initializeSystemResources ();
}

void initializeCallbacks () {
	windowCallback2 = new Callback (this, "windowProc", 2);
	windowProc2 = windowCallback2.getAddress ();
	if (windowProc2 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		
	windowCallback3 = new Callback (this, "windowProc", 3);
	windowProc3 = windowCallback3.getAddress ();
	if (windowProc3 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);	
	
	windowCallback4 = new Callback (this, "windowProc", 4);
	windowProc4 = windowCallback4.getAddress ();
	if (windowProc4 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);	
	
	windowCallback5 = new Callback (this, "windowProc", 5);
	windowProc5 = windowCallback5.getAddress ();
	if (windowProc5 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	timerCallback = new Callback (this, "timerProc", 1);
	timerProc = timerCallback.getAddress ();
	if (timerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	windowTimerCallback = new Callback (this, "windowTimerProc", 1);
	windowTimerProc = windowTimerCallback.getAddress ();
	if (windowTimerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	mouseHoverCallback = new Callback (this, "mouseHoverProc", 1);
	mouseHoverProc = mouseHoverCallback.getAddress ();
	if (mouseHoverProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	caretCallback = new Callback(this, "caretProc", 1);
	caretProc = caretCallback.getAddress();
	if (caretProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	treeSelectionCallback = new Callback(this, "treeSelectionProc", 4);
	treeSelectionProc = treeSelectionCallback.getAddress();
	if (treeSelectionProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
}

/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Display</code>. It is marked public only so that it
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
public void internal_dispose_GC (int gdkGC, GCData data) {
	OS.g_object_unref(gdkGC);
}

/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Display</code>. It is marked public only so that it
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
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	int root = OS.GDK_ROOT_PARENT();
	int gdkGC = OS.gdk_gc_new(root);
	if (gdkGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (data != null) {
		data.device = this;
		data.drawable = root;
		data.font = defaultFont;
	}
	return gdkGC;
}

boolean isValidThread () {
	return thread == Thread.currentThread ();
}

int mouseHoverProc (int handle) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.hoverProc (handle);
}

void postEvent (Event event) {
	/*
	* Place the event at the end of the event queue.
	* This code is always called in the Display's
	* thread so it must be re-enterant but does not
	* need to be synchronized.
	*/
	if (eventQueue == null) eventQueue = new Event [4];
	int index = 0;
	int length = eventQueue.length;
	while (index < length) {
		if (eventQueue [index] == null) break;
		index++;
	}
	if (index == length) {
		Event [] newQueue = new Event [length + 4];
		System.arraycopy (eventQueue, 0, newQueue, 0, length);
		eventQueue = newQueue;
	}
	eventQueue [index] = event;
}

/**
 * Reads an event from the operating system's event queue,
 * dispatches it appropriately, and returns <code>true</code>
 * if there is potentially more work to do, or <code>false</code>
 * if the caller can sleep until another event is placed on
 * the event queue.
 * <p>
 * In addition to checking the system event queue, this method also
 * checks if any inter-thread messages (created by <code>syncExec()</code>
 * or <code>asyncExec()</code>) are waiting to be processed, and if
 * so handles them before returning.
 * </p>
 *
 * @return <code>false</code> if the caller can sleep upon return from this method
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #sleep
 * @see #wake
 */
public boolean readAndDispatch () {
	checkDevice ();
	int status = OS.gtk_events_pending ();
	if (status != 0) {
//		if ((status & OS.XtIMTimer) != 0) OS.XtAppProcessEvent (context, OS.XtIMTimer);
//		if ((status & OS.XtIMAlternateInput) != 0) OS.XtAppProcessEvent (context, OS.XtIMAlternateInput);
//		if ((status & OS.XtIMXEvent) != 0) {
//			OS.XtAppNextEvent (context, event);
//			OS.XtDispatchEvent (event);
//		}
/*		int eventPtr = OS.gdk_event_get();
		if (eventPtr != 0) {
			OS.gtk_main_do_event(eventPtr);
		}*/
		OS.gtk_main_iteration ();
		runDeferredEvents ();
		return true;
	}
	return runAsyncMessages ();
}

synchronized void register () {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] == null) {
			Displays [i] = this;
			return;
		}
	}
	Display [] newDisplays = new Display [Displays.length + 4];
	System.arraycopy (Displays, 0, newDisplays, 0, Displays.length);
	newDisplays [Displays.length] = this;
	Displays = newDisplays;
}

protected void release () {
	sendEvent (SWT.Dispose, new Event ());
	Shell [] shells = WidgetTable.shells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			if (this == shell.getDisplay ()) shell.dispose ();
		}
	}
	while (readAndDispatch ()) {};
	if (disposeList != null) {
		for (int i=0; i<disposeList.length; i++) {
			if (disposeList [i] != null) disposeList [i].run ();
		}
	}
	disposeList = null;
	synchronizer.releaseSynchronizer ();
	synchronizer = null;
	releaseDisplay ();
	super.release ();
}

void releaseDisplay () {
	windowCallback2.dispose ();  windowCallback2 = null;
	windowCallback3.dispose ();  windowCallback3 = null;
	windowCallback4.dispose ();  windowCallback4 = null;
	windowCallback5.dispose ();  windowCallback5 = null;
	
	/* Dispose preedit window */
	if (preeditWindow != 0) OS.gtk_widget_destroy (preeditWindow);

	/* Dispose GtkTreeView callbacks */
	treeSelectionCallback.dispose (); treeSelectionCallback = null;
	treeSelectionProc = 0;

	/* Dispose the caret callback */
	if (caretId != 0) OS.gtk_timeout_remove (caretId);
	caretId = caretProc = 0;
	caretCallback.dispose ();
	caretCallback = null;
	
	/* Dispose the timer callback */
	if (timerIds != null) {
		for (int i=0; i<timerIds.length; i++) {
			if (timerIds [i] != 0) OS.gtk_timeout_remove (timerIds [i]);
		}
	}
	timerIds = null;
	timerList = null;
	timerProc = 0;
	timerCallback.dispose ();
	timerCallback = null;
	windowTimerProc = 0;
	windowTimerCallback.dispose ();
	windowTimerCallback = null;
	
	/* Dispose mouse hover callback */
	if (mouseHoverId != 0) OS.gtk_timeout_remove (mouseHoverId);
	mouseHoverId = mouseHoverHandle = mouseHoverProc = 0;
	mouseHoverCallback.dispose ();
	mouseHoverCallback = null;

	thread = null;
	windowProc2 = windowProc3 = windowProc4 = windowProc5 = 0;
	
	/* Dispose the default font */
	if (defaultFont != 0) OS.pango_font_description_free (defaultFont);
	defaultFont = 0;
	
	COLOR_WIDGET_DARK_SHADOW = COLOR_WIDGET_NORMAL_SHADOW = COLOR_WIDGET_LIGHT_SHADOW =
	COLOR_WIDGET_HIGHLIGHT_SHADOW = COLOR_WIDGET_BACKGROUND = COLOR_WIDGET_BORDER =
	COLOR_LIST_FOREGROUND = COLOR_LIST_BACKGROUND = COLOR_LIST_SELECTION = COLOR_LIST_SELECTION_TEXT =
	COLOR_INFO_BACKGROUND = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notifed when an event of the given type occurs anywhere in SWT.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #addFilter
 * @see #addListener
 * 
 * @since 2.1 
 */
public void removeFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) return;
	filterTable.unhook (eventType, listener);
	if (filterTable.size () == 0) filterTable = null;
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
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Listener
 * @see #addListener
 * 
 * @since 2.0 
 */
public void removeListener (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
}

void removeMouseHoverTimeout (int handle) {
	if (handle != mouseHoverHandle) return;
	if (mouseHoverId != 0) OS.gtk_timeout_remove (mouseHoverId);
	mouseHoverId = mouseHoverHandle = 0;
}

boolean runAsyncMessages () {
	return synchronizer.runAsyncMessages ();
}

boolean runDeferredEvents () {
	/*
	* Run deferred events.  This code is always
	* called in the Display's thread so it must
	* be re-enterant but need not be synchronized.
	*/
	while (eventQueue != null) {
		
		/* Take an event off the queue */
		Event event = eventQueue [0];
		if (event == null) break;
		int length = eventQueue.length;
		System.arraycopy (eventQueue, 1, eventQueue, 0, --length);
		eventQueue [length] = null;

		/* Run the event */
		Widget widget = event.widget;
		if (widget != null && !widget.isDisposed ()) {
			Widget item = event.item;
			if (item == null || !item.isDisposed ()) {
				widget.sendEvent (event);
				
				/* Ask for the next mouse event */
				if (event.type == SWT.MouseMove) {
					OS.gdk_window_get_pointer (0, null, null, null);
				}
			}
		}

		/*
		* At this point, the event queue could
		* be null due to a recursive invokation
		* when running the event.
		*/
	}

	/* Clear the queue */
	eventQueue = null;
	return true;
}

/**
 * On platforms which support it, sets the application name
 * to be the argument. On Motif, for example, this can be used
 * to set the name used for resource lookup.
 *
 * @param name the new app name
 */
public static void setAppName (String name) {
	APP_NAME = name;
}

/**
 * Sets the location of the on-screen pointer relative to the top left corner
 * of the screen.  <b>Note: It is typically considered bad practice for a
 * program to move the on-screen pointer location.</b>
 *
 * @param point new position 
 * @since 2.0
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null
 * </ul>
 */
public void setCursorLocation (Point point) {
	checkDevice ();
	/* This is not supported on GTK */
}

/**
 * Sets the application defined property of the receiver
 * with the specified name to the given argument.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the display is disposed
 * of, it is the application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param key the name of the property
 * @param value the new value for the property
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setData
 * @see #disposeExec
 */
public void setData (String key, Object value) {
	checkDevice ();
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
 * Sets the application defined, display specific data
 * associated with the receiver, to the argument.
 * The <em>display specific data</em> is a single,
 * unnamed field that is stored with every display. 
 * <p>
 * Applications may put arbitrary objects in this field. If
 * the object stored in the display specific data needs to
 * be notified when the display is disposed of, it is the
 * application's responsibility provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @param data the new display specific data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - when called from the wrong thread</li>
 * </ul>
 *
 * @see #getData
 * @see #disposeExec
 */
public void setData (Object data) {
	checkDevice ();
	this.data = data;
}

/**
 * Sets the synchronizer used by the display to be
 * the argument, which can not be null.
 *
 * @param synchronizer the new synchronizer for the display (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the synchronizer is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSynchronizer (Synchronizer synchronizer) {
	checkDevice ();
	if (synchronizer == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (this.synchronizer != null) {
		this.synchronizer.runAsyncMessages();
	}
	this.synchronizer = synchronizer;
}

void showIMWindow (Control control) {
	if (preeditWindow == 0) { 
		preeditWindow = OS.gtk_window_new (OS.GTK_WINDOW_POPUP);
		if (preeditWindow == 0) error (SWT.ERROR_NO_HANDLES);
		preeditLabel = OS.gtk_label_new (null);
		if (preeditLabel == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (preeditWindow, preeditLabel);
		OS.gtk_widget_show (preeditLabel);
	}
	int [] preeditString = new int [1];
	int [] pangoAttrs = new int [1];
	int imHandle = control.imHandle ();
	OS.gtk_im_context_get_preedit_string (imHandle, preeditString, pangoAttrs, null);
	if (preeditString [0] != 0 && OS.strlen (preeditString [0]) > 0) {
		OS.gtk_widget_modify_bg (preeditWindow, 0, control.getBackgroundColor ());
		OS.gtk_widget_modify_fg (preeditWindow, 0, control.getForegroundColor ());		
		OS.gtk_widget_modify_font (preeditLabel, control.getFontDescription ());
		if (pangoAttrs [0] != 0) OS.gtk_label_set_attributes (preeditLabel, pangoAttrs[0]);
		OS.gtk_label_set_text (preeditLabel, preeditString [0]);
		Point point = control.toDisplay (control.getIMCaretPos ());
		OS.gtk_window_move (preeditWindow, point.x, point.y);		
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (preeditLabel, requisition);
		OS.gtk_window_resize (preeditWindow, requisition.width, requisition.height);
		OS.gtk_widget_show (preeditWindow);
	} else {
		OS.gtk_widget_hide (preeditWindow);
	}		
	if (preeditString [0] != 0) OS.g_free (preeditString [0]);
	if (pangoAttrs [0] != 0) OS.pango_attr_list_unref (pangoAttrs [0]);	
}

/**
 * Causes the user-interface thread to <em>sleep</em> (that is,
 * to be put in a state where it does not consume CPU cycles)
 * until an event is received or it is otherwise awakened.
 *
 * @return <code>true</code> if an event requiring dispatching was placed on the queue.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #wake
 */
public boolean sleep () {
	checkDevice ();
	/* Temporary code - need to sleep waiting for the next message */
	try { Thread.sleep(50); } catch (Exception e) {};
	return true;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread after the specified
 * number of milliseconds have elapsed. If milliseconds is less
 * than zero, the runnable is not executed.
 *
 * @param milliseconds the delay before running the runnable
 * @param runnable code to run on the user-interface thread
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void timerExec (int milliseconds, Runnable runnable) {
	checkDevice ();
	if (runnable == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (timerList == null) timerList = new Runnable [4];
	if (timerIds == null) timerIds = new int [4];	
	int index = 0;
	while (index < timerList.length) {
		if (timerList [index] == runnable) break;
		index++;
	}
	if (index != timerList.length) {
		OS.gtk_timeout_remove (timerIds [index]);
		timerList [index] = null;
		timerIds [index] = 0;
		if (milliseconds < 0) return;
	} else {
		if (milliseconds < 0) return;
		index = 0;
		while (index < timerList.length) {
			if (timerList [index] == null) break;
			index++;
		}
		if (index == timerList.length) {
			Runnable [] newTimerList = new Runnable [timerList.length + 4];
			System.arraycopy (timerList, 0, newTimerList, 0, timerList.length);
			timerList = newTimerList;
			int [] newTimerIds = new int [timerIds.length + 4];
			System.arraycopy (timerIds, 0, newTimerIds, 0, timerIds.length);
			timerIds = newTimerIds;
		}
	}
	int timerId = OS.gtk_timeout_add (milliseconds, timerProc, index);
	if (timerId != 0) {
		timerIds [index] = timerId;
		timerList [index] = runnable;
	}
}

int timerProc (int index) {
	if (timerList == null) return 0;
	if (0 <= index && index < timerList.length) {
		Runnable runnable = timerList [index];
		timerList [index] = null;
		timerIds [index] = 0;
		if (runnable != null) runnable.run ();
	}
	return 0;
}

int caretProc (int clientData) {
	caretId = 0;
	if (currentCaret == null) {
		return 0;
	}
	if (currentCaret.blinkCaret()) {
		int blinkRate = currentCaret.blinkRate;
		caretId = OS.gtk_timeout_add (blinkRate, caretProc, 0);
	} else {
		currentCaret = null;
	}
	return 0;
}

int treeSelectionProc (int model, int path, int iter, int data) {
	Widget widget = WidgetTable.get (data);
	if (widget == null) return 0;
	return widget.treeSelectionProc (model, path, iter, treeSelection, treeSelectionLength++);
}

void sendEvent (int eventType, Event event) {
	if (eventTable == null && filterTable == null) {
		return;
	}
	if (event == null) event = new Event ();
	event.display = this;
	event.type = eventType;
	if (event.time == 0) event.time = getLastEventTime ();
	if (!filterEvent (event)) {
		if (eventTable != null) eventTable.sendEvent (event);
	}
}

void setCurrentCaret (Caret caret) {
	if (caretId != 0) OS.gtk_timeout_remove(caretId);
	caretId = 0;
	currentCaret = caret;
	if (caret == null) return;
	int blinkRate = currentCaret.blinkRate;
	caretId = OS.gtk_timeout_add (blinkRate, caretProc, 0); 
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next 
 * reasonable opportunity. The thread which calls this method
 * is suspended until the runnable completes.
 *
 * @param runnable code to run on the user-interface thread.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_FAILED_EXEC - if an exception occured when executing the runnable</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void syncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.syncExec (runnable);
}

static int translateKey (int key) {
	for (int i=0; i<KeyTable.length; i++) {
		if (KeyTable [i] [0] == key) return KeyTable [i] [1];
	}
	return 0;
}

static int untranslateKey (int key) {
	for (int i=0; i<KeyTable.length; i++) {
		if (KeyTable [i] [1] == key) return KeyTable [i] [0];
	}
	return 0;
}

/**
 * Forces all outstanding paint requests for the display
 * to be processed before this method returns.
 *
 * @see Control#update
 */
public void update () {
	checkDevice ();
	/* NOT IMPLEMENTED - Need to flush only pending draws */
	OS.gdk_flush ();
	while ((OS.gtk_events_pending ()) != 0) {
		OS.gtk_main_iteration ();
	}	
}

/**
 * If the receiver's user-interface thread was <code>sleep</code>'ing, 
 * causes it to be awakened and start running again. Note that this
 * method may be called from any thread.
 *
 * @see #sleep
 */
public void wake () {
	/* NOT IMPLEMENTED - Need to wake up the event loop */
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	if (thread == Thread.currentThread ()) return;
}

int windowProc (int handle, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, user_data);
}

int windowProc (int handle, int arg0, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, arg0, user_data);
}

int windowProc (int handle, int arg0, int arg1, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, arg0, arg1, user_data);
}

int windowProc (int handle, int arg0, int arg1, int arg2, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, arg0, arg1, arg2, user_data);
}

int windowTimerProc (int handle) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.timerProc (handle);
}

}

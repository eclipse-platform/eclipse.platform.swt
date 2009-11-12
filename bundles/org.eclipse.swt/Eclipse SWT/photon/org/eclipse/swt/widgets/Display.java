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
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
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
 * <dd>Close, Dispose, Settings</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * @see #syncExec
 * @see #asyncExec
 * @see #wake
 * @see #readAndDispatch
 * @see #sleep
 * @see Device#dispose
 * @see <a href="http://www.eclipse.org/swt/snippets/#display">Display snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Display extends Device {

	/* TEMPORARY CODE FOR EMULATED TABLE */
	int textHighlightThickness = 0;
	
	/* TEMPORARY CODE FOR EMBEDDED */
	public boolean embedded;
	
	/* Photon Only Public Fields */
	public int app_context;
//	public int phEventSize = PhEvent_t.sizeof + 1024;
//	public int phEvent = OS.malloc (phEventSize);
	
	/* Deferred Events */
	Event [] eventQueue;
	EventTable eventTable, filterTable;
	
	/* Events Dispatching and Callback */
	Callback windowCallback, drawCallback, workCallback, inputCallback, hotkeyCallback;
	int windowProc, drawProc, workProc, inputProc, hotkeyProc, input, pulse;
	boolean idle;

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread;
	
	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Deferred Layout list */
	Composite[] layoutDeferred;
	int layoutDeferredCount;

	/* System Tray */
	Tray tray;
	
	/* Drag origin */
	int dragStartX, dragStartY;

	/* Timers */
	int [] timerIds;
	Runnable [] timerList;
	Callback timerCallback;
	int timerProc, timerHandle;
	
	/* Keyboard */
	int lastKey, lastAscii;
	
	/* Key Mappings. */
	private static final int [] [] KeyTable = {
		
		/* Keyboard and Mouse Masks */
		{OS.Pk_Alt_L,     SWT.ALT},
		{OS.Pk_Alt_R,     SWT.ALT},
		{OS.Pk_Shift_L,   SWT.SHIFT},
		{OS.Pk_Shift_R,   SWT.SHIFT},
		{OS.Pk_Control_L, SWT.CONTROL},
		{OS.Pk_Control_R, SWT.CONTROL},
//		{OS.Pk_????, SWT.COMMAND},
//		{OS.Pk_????, SWT.COMMAND},
			
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},
		
		/* Non-Numeric Keypad Keys */
		{OS.Pk_Up,		SWT.ARROW_UP},
		{OS.Pk_Down,	SWT.ARROW_DOWN},
		{OS.Pk_Left,	SWT.ARROW_LEFT},
		{OS.Pk_Right,	SWT.ARROW_RIGHT},
		{OS.Pk_Prior,	SWT.PAGE_UP},
		{OS.Pk_Next,	SWT.PAGE_DOWN},
		{OS.Pk_Home,	SWT.HOME},
		{OS.Pk_End,		SWT.END},
		{OS.Pk_Insert,	SWT.INSERT},

		/* Virtual and Ascii Keys */
		{OS.Pk_BackSpace,	SWT.BS},
		{OS.Pk_Return,		SWT.CR},
		{OS.Pk_Delete,		SWT.DEL},
		{OS.Pk_Escape,		SWT.ESC},
		{OS.Pk_Linefeed,	SWT.LF},
		{OS.Pk_Tab,			SWT.TAB},
		{OS.Pk_KP_Tab,		SWT.TAB},
	
		/* Functions Keys */
		{OS.Pk_F1,	SWT.F1},
		{OS.Pk_F2,	SWT.F2},
		{OS.Pk_F3,	SWT.F3},
		{OS.Pk_F4,	SWT.F4},
		{OS.Pk_F5,	SWT.F5},
		{OS.Pk_F6,	SWT.F6},
		{OS.Pk_F7,	SWT.F7},
		{OS.Pk_F8,	SWT.F8},
		{OS.Pk_F9,	SWT.F9},
		{OS.Pk_F10,	SWT.F10},
		{OS.Pk_F11,	SWT.F11},
		{OS.Pk_F12,	SWT.F12},
		{OS.Pk_F13,	SWT.F13},
		{OS.Pk_F14,	SWT.F14},
		{OS.Pk_F15,	SWT.F15},

		/* Numeric Keypad Keys */
		{OS.Pk_KP_Multiply,	SWT.KEYPAD_MULTIPLY},
		{OS.Pk_KP_Add,		SWT.KEYPAD_ADD},
		{OS.Pk_KP_Enter,	SWT.KEYPAD_CR},
		{OS.Pk_KP_Subtract,	SWT.KEYPAD_SUBTRACT},
		{OS.Pk_KP_Decimal,	SWT.KEYPAD_DECIMAL},
		{OS.Pk_KP_Divide,	SWT.KEYPAD_DIVIDE},
		{OS.Pk_KP_0,		SWT.KEYPAD_0},
		{OS.Pk_KP_1,		SWT.KEYPAD_1},
		{OS.Pk_KP_2,		SWT.KEYPAD_2},
		{OS.Pk_KP_3,		SWT.KEYPAD_3},
		{OS.Pk_KP_4,		SWT.KEYPAD_4},
		{OS.Pk_KP_5,		SWT.KEYPAD_5},
		{OS.Pk_KP_6,		SWT.KEYPAD_6},
		{OS.Pk_KP_7,		SWT.KEYPAD_7},
		{OS.Pk_KP_8,		SWT.KEYPAD_8},
		{OS.Pk_KP_9,		SWT.KEYPAD_9},
		{OS.Pk_KP_Equal,	SWT.KEYPAD_EQUAL},

		/* Other keys */
		{OS.Pk_Caps_Lock,	SWT.CAPS_LOCK},
		{OS.Pk_Num_Lock,	SWT.NUM_LOCK},
		{OS.Pk_Scroll_Lock,	SWT.SCROLL_LOCK},
		{OS.Pk_Pause,		SWT.PAUSE},
		{OS.Pk_Break,		SWT.BREAK},
		{OS.Pk_Print,		SWT.PRINT_SCREEN},
		{OS.Pk_Help,		SWT.HELP},

	};
	
	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Window Classes */
	int ClassesPtr;
	int PtButton;
	int PtList;
	int PtLabel;
	int PtToggleButton;
	int PtComboBox;
	int PtText;
	int PtMultiText;
	int PtScrollbar;
	int PtContainer;
	int PtProgress;
	int PtPanelGroup;
	int PtSlider;
	int PtSeparator;
	int PtToolbar;
	int PtNumericInteger;
				
	/* Colors */
	int WIDGET_DARK_SHADOW, WIDGET_NORMAL_SHADOW, WIDGET_LIGHT_SHADOW;
	int WIDGET_HIGHLIGHT_SHADOW, WIDGET_BACKGROUND, WIDGET_FOREGROUND, WIDGET_BORDER;
	int LIST_FOREGROUND, LIST_BACKGROUND, LIST_SELECTION, LIST_SELECTION_TEXT;
	int INFO_FOREGROUND, INFO_BACKGROUND, TEXT_FOREGROUND, TEXT_BACKGROUND;
	
	/* Fonts */
	byte [] defaultFont;
	byte [] TEXT_FONT, LIST_FONT, TITLE_FONT, GAUGE_FONT, GROUP_FONT;

	/* System Cursors */
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];
	
	/* Images */
	int nullImage;
	
	/* ScrollBars */
	int SCROLLBAR_WIDTH;
	int SCROLLBAR_HEIGHT;
	int SCROLLBAR_VERTICAL_BASIC_FLAGS;
	int SCROLLBAR_HORIZONTAL_BASIC_FLAGS;

	/* Skinning support */
	static final int GROW_SIZE = 1024;
	Widget [] skinList = new Widget [GROW_SIZE];
	int skinCount;
	
	/* Package name */
	static final String PACKAGE_NAME;
	static {
		String name = Display.class.getName ();
		int index = name.lastIndexOf ('.');
		PACKAGE_NAME = name.substring (0, index + 1);
	}

	/* Photon Draw Buffer - shared by all widgets */
	static int DrawBufferSize = 1024 * 48;

	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;
	
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
 *    <li>ERROR_THREAD_INVALID_ACCESS - if called from a thread that already created an existing display</li>
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

/**
 * Constructs a new instance of this class using the parameter.
 * 
 * @param data the device data
 */
public Display (DeviceData data) {
	super (data);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an event of the given type occurs anywhere
 * in a widget. The event type is one of the event constants
 * defined in class <code>SWT</code>. When the event does occur,
 * the listener is notified by sending it the <code>handleEvent()</code>
 * message.
 * <p>
 * Setting the type of an event to <code>SWT.None</code> from
 * within the <code>handleEvent()</code> method can be used to
 * change the event type and stop subsequent Java listeners
 * from running. Because event filters run before other listeners,
 * event filters can both block other listeners and set arbitrary
 * fields within an event. For this reason, event filters are both
 * powerful and dangerous. They should generally be avoided for
 * performance, debugging and code maintenance reasons.
 * </p>
 * 
 * @param eventType the type of event to listen for
 * @param listener the listener which should be notified when the event occurs
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
 * @see #removeFilter
 * @see #removeListener
 * 
 * @since 3.0 
 */
public void addFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) filterTable = new EventTable ();
	filterTable.hook (eventType, listener);
}

void addLayoutDeferred (Composite comp) {
	if (layoutDeferred == null) layoutDeferred = new Composite [64];
	if (layoutDeferredCount == layoutDeferred.length) {
		Composite [] temp = new Composite [layoutDeferred.length + 64];
		System.arraycopy (layoutDeferred, 0, temp, 0, layoutDeferred.length);
		layoutDeferred = temp;
	}
	layoutDeferred[layoutDeferredCount++] = comp;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an event of the given type occurs. The event
 * type is one of the event constants defined in class <code>SWT</code>.
 * When the event does occur in the display, the listener is notified by
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
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

void addSkinnableWidget (Widget widget) {
	if (skinCount >= skinList.length) {
		Widget[] newSkinWidgets = new Widget [skinList.length + GROW_SIZE];
		System.arraycopy (skinList, 0, newSkinWidgets, 0, skinList.length);
		skinList = newSkinWidgets;
	}
	skinList [skinCount++] = widget;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next 
 * reasonable opportunity. The caller of this method continues 
 * to run in parallel, and is not notified when the
 * runnable has completed.  Specifying <code>null</code> as the
 * runnable simply wakes the user-interface thread when run.
 * <p>
 * Note that at the time the runnable is invoked, widgets 
 * that have the receiver as their display may have been
 * disposed. Therefore, it is necessary to check for this
 * case inside the runnable before accessing the widget.
 * </p>
 *
 * @param runnable code to run on the user-interface thread or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #syncExec
 */
public void asyncExec (Runnable runnable) {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		synchronizer.asyncExec (runnable);
	}
}

/**
 * Causes the system hardware to emit a short sound
 * (if it supports this capability).
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void beep () {
	checkDevice ();
	OS.PtBeep ();
}

protected void checkDevice () {
	if (thread == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (thread != Thread.currentThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

static void checkDisplay (Thread thread, boolean multiple) {
	synchronized (Device.class) {
		for (int i=0; i<Displays.length; i++) {
			if (Displays [i] != null) {
				if (!multiple) SWT.error (SWT.ERROR_NOT_IMPLEMENTED, null, " [multiple displays]");
				if (Displays [i].thread == thread) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
			}
		}
	}
}

/**
 * Checks that this class can be subclassed.
 * <p>
 * IMPORTANT: See the comment in <code>Widget.checkSubclass()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Widget#checkSubclass
 */
protected void checkSubclass () {
	if (!isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Requests that the connection between SWT and the underlying
 * operating system be closed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Device#dispose
 * 
 * @since 2.0
 */
public void close () {
	checkDevice ();
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit) dispose ();
}

String convertToLf (String text) {
	int length = text.length ();
	if (length == 0) return text;
	
	/* Check for an LF or CR/LF.  Assume the rest of the string 
	 * is formated that way.  This will not work if the string 
	 * contains mixed delimiters. */
	int i = text.indexOf ('\n', 0);
	if (i == -1 || i == 0) return text;
	if (text.charAt (i - 1) != '\r') return text;

	/* The string is formatted with CR/LF.
	 * Create a new string with the LF line delimiter. */
	i = 0;
	StringBuffer result = new StringBuffer ();
	while (i < length) {
		int j = text.indexOf ('\r', i);
		if (j == -1) j = length;
		String s = text.substring (i, j);
		result.append (s);
		i = j + 2;
		result.append ('\n');
	}
	return result.toString ();
}

/**
 * Creates the device in the operating system.  If the device
 * does not have a handle, this method may do nothing depending
 * on the device.
 * <p>
 * This method is called before <code>init</code>.
 * </p>
 *
 * @param data the DeviceData which describes the receiver
 *
 * @see #init
 */
protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay (thread = Thread.currentThread (), false);
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
	OS.PtInit (null);
	OS.PgSetDrawBufferSize (DrawBufferSize);
	app_context = OS.PtCreateAppContext ();
}

static void deregister (Display display) {
	synchronized (Device.class) {
		for (int i=0; i<Displays.length; i++) {
			if (display == Displays [i]) Displays [i] = null;
		}
	}
}

/**
 * Destroys the device in the operating system and releases
 * the device's handle.  If the device does not have a handle,
 * this method may do nothing depending on the device.
 * <p>
 * This method is called after <code>release</code>.
 * </p>
 * @see Device#dispose
 * @see #release
 */
protected void destroy () {
	if (this == Default) Default = null;
	deregister (this);
	destroyDisplay ();
}

void destroyDisplay () {
	// NEED to destroy app_context ???
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread just before the
 * receiver is disposed.  Specifying a <code>null</code> runnable
 * is ignored.
 *
 * @param runnable code to run at dispose time.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
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

int drawProc (int handle, int damage) {
	/*
	* Feature in Photon. On QNX 6.2, if a widget is damaged, PtBlit() will
	* call its draw function before blitting pixels. This is not wrong
	* but it is unwanted, since the callback might happen in a thread other
	* than the display thread. The fix is to detect that the callback happened
	* in the wrong thread and return right away.
	*/
	//TEMPORARY CODE
	if (thread != Thread.currentThread()) return 0;
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.drawProc (handle, damage);
}

void error (int code) {
	SWT.error(code);
}

/**
 * Returns the display which the given thread is the
 * user-interface thread for, or null if the given thread
 * is not a user-interface thread for any display.  Specifying
 * <code>null</code> as the thread will return <code>null</code>
 * for the display. 
 *
 * @param thread the user-interface thread
 * @return the display for the given thread
 */
public static Display findDisplay (Thread thread) {
	synchronized (Device.class) {
		for (int i=0; i<Displays.length; i++) {
			Display display = Displays [i];
			if (display != null && display.thread == thread) {
				return display;
			}
		}
		return null;
	}
}

boolean filterEvent (Event event) {
	if (filterTable != null) filterTable.sendEvent (event);
	return false;
}

boolean filters (int eventType) {
	if (filterTable == null) return false;
	return filterTable.hooks (eventType);
}

/**
 * Given the operating system handle for a widget, returns
 * the instance of the <code>Widget</code> subclass which
 * represents it in the currently running application, if
 * such exists, or null if no matching widget can be found.
 * <p>
 * <b>IMPORTANT:</b> This method should not be called from
 * application code. The arguments are platform-specific.
 * </p>
 *
 * @param handle the handle for the widget
 * @return the SWT widget that the handle represents
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Widget findWidget (int handle) {
	checkDevice ();
	return WidgetTable.get (handle);
}

/**
 * Given the operating system handle for a widget,
 * and widget-specific id, returns the instance of
 * the <code>Widget</code> subclass which represents
 * the handle/id pair in the currently running application,
 * if such exists, or null if no matching widget can be found.
 * <p>
 * <b>IMPORTANT:</b> This method should not be called from
 * application code. The arguments are platform-specific.
 * </p>
 *
 * @param handle the handle for the widget
 * @param id the id for the subwidget (usually an item)
 * @return the SWT widget that the handle/id pair represents
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public Widget findWidget (int handle, int id) {
	checkDevice ();
	return null;
}

/**
 * Given a widget and a widget-specific id, returns the
 * instance of the <code>Widget</code> subclass which represents
 * the widget/id pair in the currently running application,
 * if such exists, or null if no matching widget can be found.
 *
 * @param widget the widget
 * @param id the id for the subwidget (usually an item)
 * @return the SWT subwidget (usually an item) that the widget/id pair represents
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.3
 */
public Widget findWidget (Widget widget, int id) {
	checkDevice ();
	return null;
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Shell getActiveShell () {
	checkDevice ();
	int handle = 0;
	while ((handle = OS.PtNextTopLevelWidget (handle)) != 0) {
		int state = OS.PtWindowGetState (handle);
		if (state != -1 && (state & OS.Ph_WM_STATE_ISFOCUS) != 0) {
			Widget widget = WidgetTable.get (handle);
			if (widget instanceof Shell) return (Shell) widget;
		}
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Control getCursorControl () {
	checkDevice ();
	int ig = OS.PhInputGroup (0);
	PhCursorInfo_t info = new PhCursorInfo_t ();
	OS.PhQueryCursor ((short) ig, info);
	PhRect_t rect = new PhRect_t ();
	rect.ul_x = rect.lr_x = info.pos_x;
	rect.ul_y = rect.lr_y = info.pos_y;
	int handle = 0;
	//DOESN'T WORK WHEN SHELLS OVERLAP (NEED Z ORDER)
	while ((handle = OS.PtNextTopLevelWidget (handle)) != 0) {
		int child = handle, parent = 0;
		short [] x = new short [1], y = new short [1];
		do {
			OS.PtGetAbsPosition (child, x, y);
			rect.ul_x = rect.lr_x = (short) (info.pos_x - x [0]);
			rect.ul_y = rect.lr_y = (short) (info.pos_y - y [0]);
			if ((child = OS.PtHit (child, 1, rect)) == 0) break;
			parent = child;
			if (OS.PtWidgetIsClassMember (child, OS.PtContainer ()) == 0) break;
		} while (child != 0);
		if (parent != 0) {
			do {
				Widget widget = WidgetTable.get (parent);
				if (widget != null && widget instanceof Control) {
					Control control = (Control) widget;
					if (control.getEnabled ()) return control;
					return control;
				}
			} while ((parent = OS.PtWidgetParent (parent)) != 0);
		}
	}
	return null;
}

/**
 * Returns an array containing the recommended cursor sizes.
 *
 * @return the array of cursor sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public Point [] getCursorSizes () {
	checkDevice ();
	return new Point [] {new Point (16, 16), new Point (32, 32)};
}

/**
 * Returns the location of the on-screen pointer relative
 * to the top left corner of the screen.
 *
 * @return the cursor location
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point getCursorLocation () {
	checkDevice ();
	int ig = OS.PhInputGroup (0);
	PhCursorInfo_t info = new PhCursorInfo_t ();
	OS.PhQueryCursor ((short)ig, info);
	return new Point (info.pos_x, info.pos_y);
}

/**
 * Returns the display which the currently running thread is
 * the user-interface thread for, or null if the currently
 * running thread is not a user-interface thread for any display.
 *
 * @return the current display
 */
public static Display getCurrent () {
	return findDisplay (Thread.currentThread ());
}

/**
 * Returns the default display. One is created (making the
 * thread that invokes this method its user-interface thread)
 * if it did not already exist.
 *
 * @return the default display
 */
public static Display getDefault () {
	synchronized (Device.class) {
		if (Default == null) Default = new Display ();
		return Default;
	}
}

/**
 * On platforms which support it, sets the application name
 * to be the argument. On Motif, for example, this can be used
 * to set the name used for resource lookup.  Specifying
 * <code>null</code> for the name clears it.
 *
 * @param name the new app name or <code>null</code>
 */
public static void setAppName (String name) {
	/* Do nothing */
}

/**
 * Returns the button dismissal alignment, one of <code>LEFT</code> or <code>RIGHT</code>.
 * The button dismissal alignment is the ordering that should be used when positioning the
 * default dismissal button for a dialog.  For example, in a dialog that contains an OK and
 * CANCEL button, on platforms where the button dismissal alignment is <code>LEFT</code>, the
 * button ordering should be OK/CANCEL.  When button dismissal alignment is <code>RIGHT</code>,
 * the button ordering should be CANCEL/OK.
 *
 * @return the button dismissal order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1
 */
public int getDismissalAlignment () {
	checkDevice ();
	return SWT.RIGHT;
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getDoubleClickTime () {
	checkDevice ();
	//NOT DONE
	return 250;
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Control getFocusControl () {
	checkDevice ();
	int handle = 0;
	while ((handle = OS.PtNextTopLevelWidget (handle)) != 0) {
		int state = OS.PtWindowGetState (handle);
		if (state != -1 && (state & OS.Ph_WM_STATE_ISFOCUS) != 0) {
			int focusHandle = OS.PtContainerFindFocus (handle);
			if (focusHandle != 0) {
				Widget widget = WidgetTable.get (focusHandle);
				if (widget instanceof Control) return (Control) widget;
			}
			return null;
		}
	}
	return null;
}

int getLastEventTime () {
	return (int) System.currentTimeMillis ();
}

/**
 * Returns true when the high contrast mode is enabled.
 * Otherwise, false is returned.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the high contrast mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public boolean getHighContrast () {
	checkDevice ();
	return false;
}

/**
 * Returns the maximum allowed depth of icons on this display, in bits per pixel.
 * On some platforms, this may be different than the actual depth of the display.
 *
 * @return the maximum icon depth
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Device#getDepth
 */
public int getIconDepth () {
	return getDepth ();
}

/**
 * Returns an array containing the recommended icon sizes.
 *
 * @return the array of icon sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Decorations#setImages(Image[])
 * 
 * @since 3.0
 */
public Point [] getIconSizes () {
	checkDevice ();
	return new Point [] {new Point (15, 15), new Point (43, 43)};	
}

int getMessageCount () {
	return synchronizer.getMessageCount ();
}

/**
 * Returns an array of monitors attached to the device.
 * 
 * @return the array of monitors
 * 
 * @since 3.0
 */
public Monitor [] getMonitors () {
	checkDevice ();	
	int cnt = OS.PhQueryRids (0, 0, 0, OS.Ph_GRAFX_REGION, 0, 0, null, null, 0);
	int [] rids = new int [cnt];
	cnt = OS.PhQueryRids (0, 0, 0, OS.Ph_GRAFX_REGION, 0, 0, null, rids, rids.length);
	PhRect_t rect = new PhRect_t ();
	Monitor [] monitors = new Monitor [cnt];
	for (int i = 0; i < cnt; i++) {
		Monitor monitor = new Monitor ();
		monitor.handle = rids [i];
		OS.PhWindowQueryVisible (OS.Ph_QUERY_CONSOLE, rids [i], OS.PhInputGroup (0), rect);
		monitor.x = rect.ul_x;
		monitor.y = rect.ul_y;
		monitor.width = rect.lr_x - rect.ul_x + 1;
		monitor.height = rect.lr_y - rect.ul_y + 1;
		OS.PhWindowQueryVisible (OS.Ph_QUERY_WORKSPACE, rids [i], OS.PhInputGroup (0), rect);
		monitor.clientX = rect.ul_x;
		monitor.clientY = rect.ul_y;
		monitor.clientWidth = rect.lr_x - rect.ul_x + 1;
		monitor.clientHeight = rect.lr_y - rect.ul_y + 1;
		monitors [i] = monitor;
	}
	return monitors;
}

/**
 * Returns the primary monitor for that device.
 * 
 * @return the primary monitor
 * 
 * @since 3.0
 */
public Monitor getPrimaryMonitor () {
	checkDevice ();
	/*
	* Note.  Photon does not define a primary monitor.
	* The workaround is to arbitrarily return the first
	* monitor whose coordinates are (0, 0), or the first
	* monitor returned by getMonitors().
	*/
	Monitor [] monitors = getMonitors ();
	if (monitors.length == 1) return monitors [0];
	for (int i = 0; i < monitors.length; i++) {
		Monitor monitor = monitors [i];
		if (monitor.x == 0 && monitor.y == 0) return monitor;
	}
	return monitors [0];
}

/**
 * Returns a (possibly empty) array containing all shells which have
 * not been disposed and have the receiver as their display.
 *
 * @return the receiver's shells
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Shell [] getShells () {
	checkDevice ();
	/*
	* NOTE:  Need to check that the shells that belong
	* to another display have not been disposed by the
	* other display's thread as the shells list is being
	* processed.
	*/
	int count = 0;
	Shell [] shells = WidgetTable.shells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && this == shell.display) {
			count++;
		}
	}
	if (count == shells.length) return shells;
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && this == shell.display) {
			result [index++] = shell;
		}
	}
	return result;
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT
 */
public Color getSystemColor (int id) {
	checkDevice ();
	int color = 0x000000;
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: 					color = INFO_FOREGROUND; break;
		case SWT.COLOR_INFO_BACKGROUND: 					color = INFO_BACKGROUND; break;
		case SWT.COLOR_TITLE_FOREGROUND: 					color = 0xFFFFFF; break;
		case SWT.COLOR_TITLE_BACKGROUND:					color = 0x5281D5; break;
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:			color = 0x74A3FF; break;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:			color = 0x000000; break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND: 			color = 0xABBBD3; break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:	color = 0xCDDDFF; break;
		case SWT.COLOR_WIDGET_DARK_SHADOW:					color = WIDGET_DARK_SHADOW; break;
		case SWT.COLOR_WIDGET_NORMAL_SHADOW:				color = WIDGET_NORMAL_SHADOW; break;
		case SWT.COLOR_WIDGET_LIGHT_SHADOW: 				color = WIDGET_LIGHT_SHADOW; break;
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:				color = WIDGET_HIGHLIGHT_SHADOW; break;
		case SWT.COLOR_WIDGET_BACKGROUND: 					color = WIDGET_BACKGROUND; break;
		case SWT.COLOR_WIDGET_FOREGROUND:					color = WIDGET_FOREGROUND; break;
		case SWT.COLOR_WIDGET_BORDER: 						color = WIDGET_BORDER; break;
		case SWT.COLOR_LIST_FOREGROUND: 					color = LIST_FOREGROUND; break;
		case SWT.COLOR_LIST_BACKGROUND: 					color = LIST_BACKGROUND; break;
		case SWT.COLOR_LIST_SELECTION: 						color = LIST_SELECTION; break;
		case SWT.COLOR_LIST_SELECTION_TEXT: 				color = LIST_SELECTION_TEXT; break;
		default:
			return super.getSystemColor (id);
	}
	return Color.photon_new (this, color);
}

/**
 * Returns the matching standard platform cursor for the given
 * constant, which should be one of the cursor constants
 * specified in class <code>SWT</code>. This cursor should
 * not be free'd because it was allocated by the system,
 * not the application.  A value of <code>null</code> will
 * be returned if the supplied constant is not an SWT cursor
 * constant. 
 *
 * @param id the SWT cursor constant
 * @return the corresponding cursor or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT#CURSOR_ARROW
 * @see SWT#CURSOR_WAIT
 * @see SWT#CURSOR_CROSS
 * @see SWT#CURSOR_APPSTARTING
 * @see SWT#CURSOR_HELP
 * @see SWT#CURSOR_SIZEALL
 * @see SWT#CURSOR_SIZENESW
 * @see SWT#CURSOR_SIZENS
 * @see SWT#CURSOR_SIZENWSE
 * @see SWT#CURSOR_SIZEWE
 * @see SWT#CURSOR_SIZEN
 * @see SWT#CURSOR_SIZES
 * @see SWT#CURSOR_SIZEE
 * @see SWT#CURSOR_SIZEW
 * @see SWT#CURSOR_SIZENE
 * @see SWT#CURSOR_SIZESE
 * @see SWT#CURSOR_SIZESW
 * @see SWT#CURSOR_SIZENW
 * @see SWT#CURSOR_UPARROW
 * @see SWT#CURSOR_IBEAM
 * @see SWT#CURSOR_NO
 * @see SWT#CURSOR_HAND
 * 
 * @since 3.0
 */
public Cursor getSystemCursor (int id) {
	checkDevice ();
	if (!(0 <= id && id < cursors.length)) return null;
	if (cursors [id] == null) {
		cursors [id] = new Cursor (this, id);
	}
	return cursors [id];
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getSystemFont () {
	checkDevice ();
	byte [] font = defaultFont != null ? defaultFont : TEXT_FONT;
	return Font.photon_new (this, font);
}

/**
 * Returns the matching standard platform image for the given
 * constant, which should be one of the icon constants
 * specified in class <code>SWT</code>. This image should
 * not be free'd because it was allocated by the system,
 * not the application.  A value of <code>null</code> will
 * be returned either if the supplied constant is not an
 * SWT icon constant or if the platform does not define an
 * image that corresponds to the constant. 
 *
 * @param id the SWT icon constant
 * @return the corresponding image or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT#ICON_ERROR
 * @see SWT#ICON_INFORMATION
 * @see SWT#ICON_QUESTION
 * @see SWT#ICON_WARNING
 * @see SWT#ICON_WORKING
 * 
 * @since 3.0
 */
public Image getSystemImage (int id) {
	checkDevice ();
	return null;
}

/**
 * Returns the single instance of the system tray or null
 * when there is no system tray available for the platform.
 *
 * @return the system tray or <code>null</code>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public Tray getSystemTray () {
	checkDevice ();
	return null;
}

/**
 * Gets the synchronizer used by the display.
 *
 * @return the receiver's synchronizer
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.4
 */
public Synchronizer getSynchronizer () {
	checkDevice ();
	return synchronizer;
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
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Thread getSyncThread () {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		return synchronizer.syncThread;
	}
}

/**
 * Returns the user-interface thread for the receiver.
 *
 * @return the receiver's user-interface thread
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Thread getThread () {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		return thread;
	}
}

int hotkeyProc (int handle, int data, int info) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return OS.Pt_CONTINUE;
	return widget.hotkeyProc (handle, data, info);
}

/**
 * Initializes any internal resources needed by the
 * device.
 * <p>
 * This method is called after <code>create</code>.
 * </p>
 * 
 * @see #create
 */
protected void init () {
	super.init ();
	initializeDisplay ();
	initializeWidgetClasses ();
	initializeWidgetColors ();
	initializeWidgetFonts ();
	initializeScrollbars ();
	initializeImages ();
}

void initializeDisplay () {
	windowCallback = new Callback (this, "windowProc", 3);
	windowProc = windowCallback.getAddress ();
	if (windowProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	drawCallback = new Callback (this, "drawProc", 2);
	drawProc = drawCallback.getAddress ();
	if (drawProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	workCallback = new Callback (this, "workProc", 1);
	workProc = workCallback.getAddress ();
	if (workProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	inputCallback = new Callback (this, "inputProc", 4);
	inputProc = inputCallback.getAddress ();
	if (inputProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	timerCallback = new Callback (this, "timerProc", 3);
	timerProc = timerCallback.getAddress ();
	if (timerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	hotkeyCallback = new Callback (this, "hotkeyProc", 3);
	hotkeyProc = hotkeyCallback.getAddress ();
	if (hotkeyProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	pulse = OS.PtAppCreatePulse (app_context, -1);
	input = OS.PtAppAddInput (app_context, pulse, inputProc, 0);
	int [] args = {
		OS.Pt_ARG_REGION_OPAQUE, 0, ~0,
		OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
		OS.Pt_ARG_REGION_SENSE, OS.Ph_EV_TIMER, ~0,
	};
	OS.PtSetParentWidget (0);
	timerHandle = OS.PtCreateWidget (OS.PtRegion (), 0, args.length / 3, args);
	if (timerHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.PtRealizeWidget (timerHandle);
}

void initializeScrollbars () {
	OS.PtSetParentWidget (0);
	int shellHandle = OS.PtCreateWidget (OS.PtWindow (), 0, 0, null);
	int textHandle = OS.PtCreateWidget (OS.PtMultiText (), shellHandle, 0, null);
	int child = OS.PtWidgetChildFront (textHandle);
	while (child != 0) {
		if (OS.PtWidgetClass (child) == OS.PtScrollbar ()) {
			int [] args = new int [] {
				OS.Pt_ARG_ORIENTATION, 0, 0,
				OS.Pt_ARG_WIDTH, 0, 0,
				OS.Pt_ARG_HEIGHT, 0, 0,
				OS.Pt_ARG_BASIC_FLAGS, 0, 0,
			};
			OS.PtGetResources (child, args.length / 3, args);
			switch (args [1]) {
				case OS.Pt_HORIZONTAL:
					SCROLLBAR_HEIGHT = args [7];
					SCROLLBAR_HORIZONTAL_BASIC_FLAGS = args [10];
					break;
				case OS.Pt_VERTICAL:
					SCROLLBAR_WIDTH = args [4];
					SCROLLBAR_VERTICAL_BASIC_FLAGS = args [10];
					break;
			}
		}
		child = OS.PtWidgetBrotherBehind (child);
	}
	OS.PtDestroyWidget (shellHandle);
}

void initializeWidgetClasses () {
	int [] args = {OS.Pt_SET_DRAW_F, drawProc, 0};
	int [] buffer = {
		OS.PtCreateWidgetClass (OS.PtButton (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtList (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtLabel (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtWindow (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtToggleButton (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtComboBox (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtText (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtMultiText (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtScrollbar (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtScrollContainer (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtScrollArea (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtContainer (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtProgress (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtPanelGroup (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtPane (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtSlider (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtSeparator (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtToolbar (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtNumericInteger (), 0, args.length / 3, args), 0, 0,
	};
	ClassesPtr = OS.malloc (buffer.length * 4);
	OS.memmove (ClassesPtr, buffer, buffer.length * 4);
	PtButton = ClassesPtr;
	PtList = ClassesPtr + 12;
	PtLabel = ClassesPtr + 24;
	//PtWindow = ClassesPtr + 36;
	PtToggleButton = ClassesPtr + 48;
	PtComboBox = ClassesPtr + 60;
	PtText = ClassesPtr + 72;
	PtMultiText = ClassesPtr + 84;	
	PtScrollbar = ClassesPtr + 96;
	//PtScrollContainer = ClassesPtr + 108;
	//PtScrollArea = ClassesPtr + 120;
	PtContainer = ClassesPtr + 132;
	PtProgress = ClassesPtr + 144;
	PtPanelGroup = ClassesPtr + 156;
	//PtPane = ClassesPtr + 168;
	PtSlider = ClassesPtr + 180;
	PtSeparator = ClassesPtr + 192;
	PtToolbar = ClassesPtr + 204;
	PtNumericInteger = ClassesPtr + 216;
}

void initializeWidgetColors () {
	OS.PtSetParentWidget (0);
	int [] args = {
		OS.Pt_ARG_WINDOW_STATE, OS.Ph_WM_STATE_ISHIDDEN, ~0,
	};
	int shellHandle = OS.PtCreateWidget (OS.PtWindow (), 0, args.length / 3, args);
	args = new int [] {
		OS.Pt_ARG_COLOR, 0, 0,
		OS.Pt_ARG_FILL_COLOR, 0, 0,
	};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	WIDGET_FOREGROUND = args [1];
	WIDGET_BACKGROUND = args [4];

	int listHandle = OS.PtCreateWidget (OS.PtList (), shellHandle, 0, null);
	args = new int [] {	
		OS.Pt_ARG_COLOR, 0, 0,
		OS.Pt_ARG_FILL_COLOR, 0, 0,
		OS.Pt_ARG_SELECTION_FILL_COLOR, 0, 0,
		OS.Pt_ARG_SELECTION_TEXT_COLOR, 0, 0,
	};
	OS.PtGetResources (listHandle, args.length / 3, args);
	LIST_FOREGROUND = args [1];
	LIST_BACKGROUND = args [4];
	LIST_SELECTION = args [7];
	LIST_SELECTION_TEXT = args [10];
	
	int textHandle = OS.PtCreateWidget (OS.PtText (), shellHandle, 0, null);
	args = new int [] {	
		OS.Pt_ARG_COLOR, 0, 0,
		OS.Pt_ARG_FILL_COLOR, 0, 0,
	};
	OS.PtGetResources (textHandle, args.length / 3, args);
	TEXT_FOREGROUND = args [1];
	TEXT_BACKGROUND = args [4];

	/*
	* Feature in Photon. The values of Pt_ARG_DARK_BEVEL_COLOR and
	* Pt_ARG_LIGHT_BEVEL_COLOR are not initialized until the widget
	* is realized.  The fix is to realize the shell without displaying
	* it.
	*/
	int buttonHandle = OS.PtCreateWidget (OS.PtButton (), shellHandle, 0, null);
	OS.PtRealizeWidget(shellHandle);
	args = new int [] {	
		OS.Pt_ARG_OUTLINE_COLOR, 0, 0,
		OS.Pt_ARG_OUTLINE_COLOR, 0, 0,
		OS.Pt_ARG_DARK_BEVEL_COLOR, 0, 0,
		OS.Pt_ARG_BEVEL_COLOR, 0, 0,
		OS.Pt_ARG_LIGHT_BEVEL_COLOR, 0, 0,
		OS.Pt_ARG_BALLOON_COLOR, 0, 0,
		OS.Pt_ARG_BALLOON_FILL_COLOR, 0, 0,
	};
	OS.PtGetResources (buttonHandle, args.length / 3, args);
	WIDGET_BORDER = args [1];
	WIDGET_DARK_SHADOW = args [4];
	WIDGET_NORMAL_SHADOW = args [7];
	WIDGET_LIGHT_SHADOW = args [10];
	WIDGET_HIGHLIGHT_SHADOW = args [13];
	INFO_FOREGROUND = args [16];
	INFO_BACKGROUND = args [19];

	OS.PtDestroyWidget (shellHandle);
}

void initializeWidgetFonts () {
	String property = System.getProperty ("swt.system.font");
	if (property != null) {
		defaultFont = Converter.wcsToMbcs (null, property, true);
		TEXT_FONT = LIST_FONT = GAUGE_FONT = TITLE_FONT = defaultFont;
		GROUP_FONT = Converter.wcsToMbcs (null, property + "b", true);
		return;
	}
	OS.PtSetParentWidget (0);
	
	int shellHandle = OS.PtCreateWidget (OS.PtWindow (), 0, 0, null);
	int [] args = new int [] {OS.Pt_ARG_TITLE_FONT, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int length = OS.strlen (args [1]);
	GROUP_FONT = TITLE_FONT = new byte [length + 1];
	OS.memmove (TITLE_FONT, args [1], length);
	
	int listHandle = OS.PtCreateWidget (OS.PtList (), shellHandle, 0, null);
	args = new int [] {OS.Pt_ARG_LIST_FONT, 0, 0};
	OS.PtGetResources (listHandle, args.length / 3, args);
	length = OS.strlen (args [1]);
	LIST_FONT = new byte [length + 1];
	OS.memmove (LIST_FONT, args [1], length);

	int textHandle = OS.PtCreateWidget (OS.PtText (), shellHandle, 0, null);
	args = new int [] {OS.Pt_ARG_TEXT_FONT, 0, 0};
	OS.PtGetResources (textHandle, args.length / 3, args);
	length = OS.strlen (args [1]);
	TEXT_FONT = new byte [length + 1];
	OS.memmove (TEXT_FONT, args [1], length);
	
	int scrollHandle = OS.PtCreateWidget (OS.PtScrollbar (), shellHandle, 0, null);
	args = new int [] {OS.Pt_ARG_GAUGE_FONT, 0, 0};
	OS.PtGetResources (scrollHandle, args.length / 3, args);
	length = OS.strlen (args [1]);
	GAUGE_FONT = new byte [length + 1];
	OS.memmove (GAUGE_FONT, args [1], length);
	
	OS.PtDestroyWidget (shellHandle);
}

void initializeImages () {
	nullImage = OS.PhCreateImage (null, (short) 1, (short) 1, OS.Pg_IMAGE_DIRECT_888, 0, 0, 0);
	if (nullImage == 0) SWT.error (SWT.ERROR_NO_HANDLES);
}

int inputProc (int data, int rcvid, int message, int size) {
	if (embedded) {
		runDeferredEvents ();
		if (runAsyncMessages (false)) wakeThread ();
	}
	return OS.Pt_CONTINUE;
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
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for gc creation</li>
 * </ul>
 */
public int internal_new_GC (GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	int phGC = OS.PgCreateGC(0); // NOTE: PgCreateGC ignores the parameter
	if (phGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	if ((data.style & mask) == 0) {
		data.style |= SWT.LEFT_TO_RIGHT;
	}

	data.device = this;
	data.rid = OS.Ph_DEV_RID;
	return phGC;
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
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data 
 */
public void internal_dispose_GC (int phGC, GCData data) {
	OS.PgDestroyGC(phGC);
}

boolean isValidThread () {
	return thread == Thread.currentThread ();
}

/**
 * Returns the application defined property of the receiver
 * with the specified name, or null if it has not been set.
 * <p>
 * Applications may have associated arbitrary objects with the
 * receiver in this fashion. If the objects stored in the
 * properties need to be notified when the display is disposed
 * of, it is the application's responsibility to provide a
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setData(String, Object)
 * @see #disposeExec(Runnable)
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
 * application's responsibility to provide a
 * <code>disposeExec()</code> handler which does so.
 * </p>
 *
 * @return the display specific data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setData(Object)
 * @see #disposeExec(Runnable)
 */
public Object getData () {
	checkDevice ();
	return data;
}
static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_NAME);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param point to be mapped 
 * @return point with mapped coordinates 
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Point map (Control from, Control to, Point point) {
	checkDevice ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);	
	return map (from, to, point.x, point.y);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param x coordinates to be mapped
 * @param y coordinates to be mapped
 * @return point with mapped coordinates
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Point map (Control from, Control to, int x, int y) {
	checkDevice ();
	if (from != null && from.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	Point point = new Point (x, y);
	if (from == to) return point;
	if (from != null) {
		short [] position_x = new short [1], position_y = new short [1];
		OS.PtGetAbsPosition (from.handle, position_x, position_y);
		point.x += position_x [0];
		point.y += position_y [0];
	}
	if (to != null) {
		short [] position_x = new short [1], position_y = new short [1];
		OS.PtGetAbsPosition (to.handle, position_x, position_y);
		point.x -= position_x [0];
		point.y -= position_y [0];
	}
	return point;
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param rectangle to be mapped
 * @return rectangle with mapped coordinates
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Rectangle map (Control from, Control to, Rectangle rectangle) {
	checkDevice ();
	if (rectangle == null) error (SWT.ERROR_NULL_ARGUMENT);	
	return map (from, to, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
}

/**
 * Maps a point from one coordinate system to another.
 * When the control is null, coordinates are mapped to
 * the display.
 * <p>
 * NOTE: On right-to-left platforms where the coordinate
 * systems are mirrored, special care needs to be taken
 * when mapping coordinates from one control to another
 * to ensure the result is correctly mirrored.
 * 
 * Mapping a point that is the origin of a rectangle and
 * then adding the width and height is not equivalent to
 * mapping the rectangle.  When one control is mirrored
 * and the other is not, adding the width and height to a
 * point that was mapped causes the rectangle to extend
 * in the wrong direction.  Mapping the entire rectangle
 * instead of just one point causes both the origin and
 * the corner of the rectangle to be mapped.
 * </p>
 * 
 * @param from the source <code>Control</code> or <code>null</code>
 * @param to the destination <code>Control</code> or <code>null</code>
 * @param x coordinates to be mapped
 * @param y coordinates to be mapped
 * @param width coordinates to be mapped
 * @param height coordinates to be mapped
 * @return rectangle with mapped coordinates
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public Rectangle map (Control from, Control to, int x, int y, int width, int height) {
	checkDevice ();
	if (from != null && from.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	Rectangle rect = new Rectangle (x, y, width, height);
	if (from == to) return rect;
	if (from != null) {
		short [] position_x = new short [1], position_y = new short [1];
		OS.PtGetAbsPosition (from.handle, position_x, position_y);
		rect.x += position_x [0];
		rect.y += position_y [0];
	}
	if (to != null) {
		short [] position_x = new short [1], position_y = new short [1];
		OS.PtGetAbsPosition (to.handle, position_x, position_y);
		rect.x -= position_x [0];
		rect.y -= position_y [0];
	}
	return rect;
}

/**
 * Generate a low level system event.
 * 
 * <code>post</code> is used to generate low level keyboard
 * and mouse events. The intent is to enable automated UI
 * testing by simulating the input from the user.  Most
 * SWT applications should never need to call this method.
 * <p>
 * Note that this operation can fail when the operating system
 * fails to generate the event for any reason.  For example,
 * this can happen when there is no such key or mouse button
 * or when the system event queue is full.
 * </p>
 * <p>
 * <b>Event Types:</b>
 * <p>KeyDown, KeyUp
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type KeyDown or KeyUp</li>
 * <p> Either one of:
 * <li>(in) character a character that corresponds to a keyboard key</li>
 * <li>(in) keyCode the key code of the key that was typed,
 *          as defined by the key code constants in class <code>SWT</code></li>
 * </ul>
 * <p>MouseDown, MouseUp</p>
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type MouseDown or MouseUp
 * <li>(in) button the button that is pressed or released
 * </ul>
 * <p>MouseMove</p>
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type MouseMove
 * <li>(in) x the x coordinate to move the mouse pointer to in screen coordinates
 * <li>(in) y the y coordinate to move the mouse pointer to in screen coordinates
 * </ul>
 * <p>MouseWheel</p>
 * <p>The following fields in the <code>Event</code> apply:
 * <ul>
 * <li>(in) type MouseWheel
 * <li>(in) detail either SWT.SCROLL_LINE or SWT.SCROLL_PAGE
 * <li>(in) count the number of lines or pages to scroll
 * </ul>
 * </dl>
 * 
 * @param event the event to be generated
 * 
 * @return true if the event was generated or false otherwise
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the event is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.0
 * 
 */
public boolean post (Event event) {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
		return false;
	}
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
	if (embedded) wakeThread ();
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred while running an inter-thread message</li>
 * </ul>
 *
 * @see #sleep
 * @see #wake
 */
public boolean readAndDispatch () {
	checkDevice ();
	runSkin ();
	runDeferredLayouts ();
	idle = false;
	OS.PtRelease ();
	OS.PtHold ();
	int id = OS.PtAppAddWorkProc (app_context, workProc, 0);
	OS.PtAppProcessEvent (app_context);
	OS.PtAppRemoveWorkProc (app_context, id);
	boolean result = true;
	if (idle) {
		result = runAsyncMessages (false);
	} else {
		runDeferredEvents ();
	}
	OS.PtRelease ();
	OS.PtHold ();
	return isDisposed () || result;
}

static void register (Display display) {
	synchronized (Device.class) {
		for (int i=0; i<Displays.length; i++) {
			if (Displays [i] == null) {
				Displays [i] = display;
				return;
			}
		}
		Display [] newDisplays = new Display [Displays.length + 4];
		System.arraycopy (Displays, 0, newDisplays, 0, Displays.length);
		newDisplays [Displays.length] = display;
		Displays = newDisplays;
	}
}

/**
 * Releases any internal resources back to the operating
 * system and clears all fields except the device handle.
 * <p>
 * Disposes all shells which are currently open on the display. 
 * After this method has been invoked, all related related shells
 * will answer <code>true</code> when sent the message
 * <code>isDisposed()</code>.
 * </p><p>
 * When a device is destroyed, resources that were acquired
 * on behalf of the programmer need to be returned to the
 * operating system.  For example, if the device allocated a
 * font to be used as the system font, this font would be
 * freed in <code>release</code>.  Also,to assist the garbage
 * collector and minimize the amount of memory that is not
 * reclaimed when the programmer keeps a reference to a
 * disposed device, all fields except the handle are zero'd.
 * The handle is needed by <code>destroy</code>.
 * </p>
 * This method is called before <code>destroy</code>.
 * 
 * @see Device#dispose
 * @see #destroy
 */
protected void release () {
	sendEvent (SWT.Dispose, new Event ());
	Shell [] shells = WidgetTable.shells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			if (this == shell.display) shell.dispose ();
		}
	}
	if (tray != null) tray.dispose ();
	tray = null;
	while (readAndDispatch ()) {}
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
	
	OS.PtDestroyWidget (timerHandle);
	
	/* Free the classes array */
	OS.free (ClassesPtr);
	
	/* Free pulses and input proc */
	OS.PtAppRemoveInput (app_context, input);
	OS.PtAppDeletePulse (app_context, pulse);
	
	/* Free the timers */
	if (timerIds != null) {
		for (int i=0; i<timerIds.length; i++) {
			 if (timerIds [i] != 0) OS.PtDestroyWidget (timerIds [i]);
		}
	}
	timerIds = null;
	timerList = null;
	timerProc = 0;
	timerCallback.dispose ();
	timerCallback = null;

	/* Free the window proc */
	windowCallback.dispose ();
	windowCallback = null;

	/* Free callbacks */
	drawCallback.dispose();
	drawCallback = null;
	workCallback.dispose();
	workCallback = null;
	inputCallback.dispose();
	inputCallback = null;
	hotkeyCallback.dispose();
	hotkeyCallback = null;
	
	if (nullImage != 0) {
		PhImage_t phImage = new PhImage_t();
		OS.memmove(phImage, nullImage, PhImage_t.sizeof);
		phImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
		OS.memmove(nullImage, phImage, PhImage_t.sizeof);
		OS.PhReleaseImage(nullImage);
		OS.free(nullImage);
		nullImage = 0;
	}

	/* Release the System Cursors */
	for (int i = 0; i < cursors.length; i++) {
		if (cursors [i] != null) cursors [i].dispose ();
	}
	cursors = null;

	/* Release references */
	thread = null;
	data = null;
	keys = null;
	values = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when an event of the given type occurs anywhere in
 * a widget. The event type is one of the event constants defined
 * in class <code>SWT</code>.
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
 * @see SWT
 * @see #addFilter
 * @see #addListener
 * 
 * @since 3.0
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
 * be notified when an event of the given type occurs. The event type
 * is one of the event constants defined in class <code>SWT</code>.
 *
 * @param eventType the type of event to listen for
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Listener
 * @see SWT
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

boolean runAsyncMessages (boolean all) {
	return synchronizer.runAsyncMessages (all);
}

boolean runDeferredEvents () {
	boolean run = false;
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
				run = true;
				widget.sendEvent (event);
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
	return run;
}

boolean runDeferredLayouts () {
	if (layoutDeferredCount != 0) {
		Composite[] temp = layoutDeferred;
		int count = layoutDeferredCount;
		layoutDeferred = null;
		layoutDeferredCount = 0;
		for (int i = 0; i < count; i++) {
			Composite comp = temp[i];
			if (!comp.isDisposed()) comp.setLayoutDeferred (false);
		}
		return true;
	}	
	return false;
}

boolean runSkin () {
	if (skinCount > 0) {
		Widget [] oldSkinWidgets = skinList;	
		int count = skinCount;	
		skinList = new Widget[GROW_SIZE];
		skinCount = 0;
		if (eventTable != null && eventTable.hooks(SWT.Skin)) {
			for (int i = 0; i < count; i++) {
				Widget widget = oldSkinWidgets[i];
				if (widget != null && !widget.isDisposed()) {
					widget.state &= ~Widget.SKIN_NEEDED;
					oldSkinWidgets[i] = null;
					Event event = new Event ();
					event.widget = widget;
					sendEvent (SWT.Skin, event);
				}
			}
		}
		return true;
	}	
	return false;
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

/**
 * Sets the location of the on-screen pointer relative to the top left corner
 * of the screen.  <b>Note: It is typically considered bad practice for a
 * program to move the on-screen pointer location.</b>
 *
 * @param x the new x coordinate for the cursor
 * @param y the new y coordinate for the cursor
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.1
 */
public void setCursorLocation (int x, int y) {
	checkDevice ();
	OS.PhMoveCursorAbs (OS.PhInputGroup (0), x, y);	
}

/**
 * Sets the location of the on-screen pointer relative to the top left corner
 * of the screen.  <b>Note: It is typically considered bad practice for a
 * program to move the on-screen pointer location.</b>
 *
 * @param point new position
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setCursorLocation (Point point) {
	checkDevice ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	setCursorLocation (point.x, point.y);
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getData(String)
 * @see #disposeExec(Runnable)
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
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getData()
 * @see #disposeExec(Runnable)
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred while running an inter-thread message</li>
 * </ul>
 */
public void setSynchronizer (Synchronizer synchronizer) {
	checkDevice ();
	if (synchronizer == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (synchronizer == this.synchronizer) return;
	Synchronizer oldSynchronizer;
	synchronized (Device.class) {
		oldSynchronizer = this.synchronizer;
		this.synchronizer = synchronizer;
	}
	if (oldSynchronizer != null) {
		oldSynchronizer.runAsyncMessages(true);
	}
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #wake
 */
public boolean sleep () {
	checkDevice ();
	if (getMessageCount () != 0) return true;
	OS.PtFlush ();
	OS.PtHold ();
	OS.PtAppProcessEvent (app_context);
	runDeferredEvents ();
	OS.PtRelease ();
	return true;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next 
 * reasonable opportunity. The thread which calls this method
 * is suspended until the runnable completes.  Specifying <code>null</code>
 * as the runnable simply wakes the user-interface thread.
 * <p>
 * Note that at the time the runnable is invoked, widgets 
 * that have the receiver as their display may have been
 * disposed. Therefore, it is necessary to check for this
 * case inside the runnable before accessing the widget.
 * </p>
 * 
 * @param runnable code to run on the user-interface thread or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_FAILED_EXEC - if an exception occurred when executing the runnable</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void syncExec (Runnable runnable) {
	Synchronizer synchronizer;
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		synchronizer = this.synchronizer;
	}
	synchronizer.syncExec (runnable);
}

int textWidth (String string, byte[] font) {
	if (string.length () == 0) return 0;
	byte [] textBuffer = Converter.wcsToMbcs (null, string, false);
	PhRect_t rect = new PhRect_t ();
	OS.PfExtentText(rect, null, font, textBuffer, textBuffer.length);
	if (rect.lr_x == rect.ul_x) return 0;
	return rect.lr_x - rect.ul_x + 1;
}

/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread after the specified
 * number of milliseconds have elapsed. If milliseconds is less
 * than zero, the runnable is not executed.
 * <p>
 * Note that at the time the runnable is invoked, widgets 
 * that have the receiver as their display may have been
 * disposed. Therefore, it is necessary to check for this
 * case inside the runnable before accessing the widget.
 * </p>
 *
 * @param milliseconds the delay before running the runnable
 * @param runnable code to run on the user-interface thread
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the runnable is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
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
		OS.PtDestroyWidget (timerIds [index]);
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
	int [] args = {OS.Pt_ARG_TIMER_INITIAL, milliseconds, 0};
	int timerId = OS.PtCreateWidget (OS.PtTimer (), timerHandle, args.length / 3, args);
	if (timerId != 0) {
		OS.PtRealizeWidget (timerId);
		OS.PtAddCallback (timerId, OS.Pt_CB_TIMER_ACTIVATE, timerProc, index);
		timerIds [index] = timerId;
		timerList [index] = runnable;
	}
}

int timerProc (int handle, int index, int info) {
	if (timerList == null) return 0;
	if (0 <= index && index < timerList.length) {
		int timerId = timerIds [index];
		Runnable runnable = timerList [index];
		timerList [index] = null;
		timerIds [index] = 0;
		if (runnable != null) runnable.run ();
		OS.PtDestroyWidget (timerId);
	}
	return 0;
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
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Control#update()
 */
public void update() {
	checkDevice ();
	Shell[] shells = WidgetTable.shells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && this == shell.display) {
			shell.update ();
		}
	}
}

/**
 * If the receiver's user-interface thread was <code>sleep</code>ing, 
 * causes it to be awakened and start running again. Note that this
 * method may be called from any thread.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #sleep
 */
public void wake () {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		if (thread == Thread.currentThread ()) return;
		wakeThread ();
	}
}

void wakeThread () {
//	int flags = OS.PtEnter (0);	
	OS.PtAppPulseTrigger (app_context, pulse);
//	if (flags >= 0) OS.PtLeave (flags);
}

int windowProc (int handle, int data, int info) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return OS.Pt_CONTINUE;
	return widget.windowProc (handle, data, info);
}

int workProc (int data) {
	idle = true;
	return OS.Pt_CONTINUE;
}

String wrapText (String text, byte[] font, int width) {
	text = convertToLf (text);
	int length = text.length ();
	if (width <= 0 || length == 0 || length == 1) return text;
	StringBuffer result = new StringBuffer ();
	int lineStart = 0, lineEnd = 0;
	while (lineStart < length) {
		lineEnd = text.indexOf ('\n', lineStart);
		boolean noLf = lineEnd == -1;
		if (noLf) lineEnd = length;
		int nextStart = lineEnd + 1;
		while (lineEnd > lineStart + 1 && Compatibility.isWhitespace (text.charAt (lineEnd - 1))) {
			lineEnd--;
		}
		int wordStart = lineStart, wordEnd = lineStart;
		int i = lineStart;
		while (i < lineEnd) {
			int lastStart = wordStart, lastEnd = wordEnd;
			wordStart = i;
			while (i < lineEnd && !Compatibility.isWhitespace (text.charAt (i))) {
				i++;
			}
			wordEnd = i - 1;
			String line = text.substring (lineStart, wordEnd + 1);
			int lineWidth = textWidth (line, font);
			while (i < lineEnd && Compatibility.isWhitespace (text.charAt (i))) {
				i++;
			}
			if (lineWidth > width) {
				if (lastStart == wordStart) {
					while (wordStart < wordEnd) {
						line = text.substring (lineStart, wordStart + 1);
						lineWidth = textWidth (line, font);
						if (lineWidth >= width) break;
						wordStart++;
					}
					if (wordStart == lastStart) wordStart++;
					lastEnd = wordStart - 1;
				}
				line = text.substring (lineStart, lastEnd + 1);
				result.append (line); result.append ('\n');
				i = wordStart; lineStart = wordStart; wordEnd = wordStart;
			}
		}
		if (lineStart < lineEnd) {
			result.append (text.substring (lineStart, lineEnd));
		}
		if (!noLf) {
			result.append ('\n');
		}
		lineStart = nextStart;
	}
	return result.toString ();
}

}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
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

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	int messagesSize;
	RunnableLock [] messages;
	Object messageLock = new Object ();
	Thread thread = Thread.currentThread ();

	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Timers */
	int [] timerIDs;
	Runnable [] timerList;
	Callback timerCallback;
	int timerProc;
	
	/* Caret */
	Caret currentCaret;
	Callback caretCallback;
	int caretID, caretProc;
		
	/* Colors */
	Color NORMAL_fg,   NORMAL_bg,   NORMAL_dark,   NORMAL_mid,   NORMAL_light,   NORMAL_text,   NORMAL_base;
	Color SELECTED_bg, SELECTED_dark, SELECTED_light, SELECTED_text, SELECTED_base;
	Color INSENSITIVE_fg, INSENSITIVE_bg, INSENSITIVE_dark, INSENSITIVE_mid, INSENSITIVE_light, INSENSITIVE_text;
	
	/* Key Mappings */
	static final int [] [] KeyTable = {
		
		/* Keyboard and Mouse Masks */
		{OS.GDK_Alt_L,		SWT.ALT},
		{OS.GDK_Alt_R,		SWT.ALT},
		{OS.GDK_Shift_L,	SWT.SHIFT},
		{OS.GDK_Shift_R,	SWT.SHIFT},
		{OS.GDK_Control_L,	SWT.CONTROL},
		{OS.GDK_Control_R,	SWT.CONTROL},
		
		/* Non-Numeric Keypad Constants */
		{OS.GDK_Up,			SWT.ARROW_UP},
		{OS.GDK_Down,		SWT.ARROW_DOWN},
		{OS.GDK_Left,		SWT.ARROW_LEFT},
		{OS.GDK_Right,		SWT.ARROW_RIGHT},
		{OS.GDK_Page_Up,	SWT.PAGE_UP},
		{OS.GDK_Page_Down,	SWT.PAGE_DOWN},
		{OS.GDK_Home,		SWT.HOME},
		{OS.GDK_End,		SWT.END},
		{OS.GDK_Insert,		SWT.INSERT},
		
		/* NOT CURRENTLY USED */
//		{OS.GDK_Delete,		SWT.DELETE},
		{OS.GDK_Return,		SWT.CR},
	
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
		/* NOT CURRENTLY USED */
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

void addLast (RunnableLock entry) {
	synchronized (messageLock) {
		if (messages == null) messages = new RunnableLock [4];
		if (messagesSize == messages.length) {
			RunnableLock[] newMessages = new RunnableLock [messagesSize + 4];
			System.arraycopy (messages, 0, newMessages, 0, messagesSize);
			messages = newMessages;
		}
		messages [messagesSize++] = entry;
	}
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

protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay(thread = Thread.currentThread ());
	createDisplay (data);
	register ();
	if (Default == null) Default = this;
}

synchronized void createDisplay (DeviceData data) {
	if (!OS.gtk_init_check (new int [] {0}, null)) {
		/*
		* This code is intentionally commented.
		*/
//		disposed = true;
//		SWT.error (SWT.ERROR_DEVICE_DISPOSED);
		return;
	}
	OS.gdk_rgb_init ();
	int ptr = OS.gtk_check_version (1, 2, 8);
	if (ptr != 0) {
		System.out.println ("***WARNING: SWT requires GTK version 1.2.8 or greater");
		int length = OS.strlen (ptr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		System.out.println ("***WARNING: " + new String (Converter.mbcsToWcs (null, buffer)));
	}
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
	throw new SWTError (code);
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

Control findControl(int h) {
	Widget w = findWidget(h);
	if (w==null) return null;
	if (w instanceof Control) return (Control)w;
	// w is something like an Item.  Go for the parent
	
	GtkWidget widget = new GtkWidget();
	OS.memmove(widget, h, GtkWidget.sizeof);
	return findControl(widget.parent);
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
	OS.gdk_window_get_pointer (0, x, y, 0);
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
	int widthMM = OS.gdk_screen_width_mm ();
	int width = OS.gdk_screen_width ();
	// compute round(25.4 * width / widthMM)
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
	OS.memmove(visual, OS.gdk_visual_get_system(), GdkVisual.sizeof);
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
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: 		return NORMAL_fg;
		case SWT.COLOR_INFO_BACKGROUND: 		return NORMAL_bg;
		
		case SWT.COLOR_TITLE_FOREGROUND:		return SELECTED_text;
		case SWT.COLOR_TITLE_BACKGROUND:		return SELECTED_bg;
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:	return SELECTED_light;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:	return INSENSITIVE_fg;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:	return INSENSITIVE_bg;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT: return INSENSITIVE_light;
		
		case SWT.COLOR_WIDGET_DARK_SHADOW:		return NORMAL_dark;
		case SWT.COLOR_WIDGET_NORMAL_SHADOW:	return NORMAL_mid;
		case SWT.COLOR_WIDGET_LIGHT_SHADOW: 	return NORMAL_light;
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:	return NORMAL_light;
		case SWT.COLOR_WIDGET_BACKGROUND: 	return NORMAL_bg;
		case SWT.COLOR_WIDGET_FOREGROUND:	return NORMAL_fg;
		case SWT.COLOR_WIDGET_BORDER: 		return super.getSystemColor (SWT.COLOR_BLACK);
		
		case SWT.COLOR_LIST_FOREGROUND: 	return super.getSystemColor (SWT.COLOR_BLACK);
		case SWT.COLOR_LIST_BACKGROUND: 	return super.getSystemColor (SWT.COLOR_WHITE);
		case SWT.COLOR_LIST_SELECTION: 		return SELECTED_bg;
		case SWT.COLOR_LIST_SELECTION_TEXT: 	return SELECTED_text;
	}
	return super.getSystemColor (id);
}

void initializeSystemColors() {
	
	/* Get the theme colors */
	GtkStyle defaultStyle = new GtkStyle();
	OS.memmove (defaultStyle, OS.gtk_widget_get_default_style(), GtkStyle.sizeof);
	
	GdkColor gdk_NORMAL_dark = new GdkColor();
	gdk_NORMAL_dark.pixel = defaultStyle.dark0_pixel;
	gdk_NORMAL_dark.red   = defaultStyle.dark0_red;
	gdk_NORMAL_dark.green = defaultStyle.dark0_green;
	gdk_NORMAL_dark.blue  = defaultStyle.dark0_blue;
	NORMAL_dark = Color.gtk_new_system(gdk_NORMAL_dark);

	GdkColor gdk_NORMAL_mid = new GdkColor();
	gdk_NORMAL_mid.pixel = defaultStyle.mid0_pixel;
	gdk_NORMAL_mid.red   = defaultStyle.mid0_red;
	gdk_NORMAL_mid.green = defaultStyle.mid0_green;
	gdk_NORMAL_mid.blue  = defaultStyle.mid0_blue;
	NORMAL_mid = Color.gtk_new_system(gdk_NORMAL_mid);

	GdkColor gdk_NORMAL_light = new GdkColor();
	gdk_NORMAL_light.pixel = defaultStyle.light0_pixel;
	gdk_NORMAL_light.red   = defaultStyle.light0_red;
	gdk_NORMAL_light.green = defaultStyle.light0_green;
	gdk_NORMAL_light.blue  = defaultStyle.light0_blue;
	NORMAL_light = Color.gtk_new_system(gdk_NORMAL_light);

	GdkColor gdk_NORMAL_fg = new GdkColor();
	gdk_NORMAL_fg.pixel = defaultStyle.fg0_pixel;
	gdk_NORMAL_fg.red   = defaultStyle.fg0_red;
	gdk_NORMAL_fg.green = defaultStyle.fg0_green;
	gdk_NORMAL_fg.blue  = defaultStyle.fg0_blue;
	NORMAL_fg = Color.gtk_new_system(gdk_NORMAL_fg);

	GdkColor gdk_NORMAL_bg = new GdkColor();
	gdk_NORMAL_bg.pixel = defaultStyle.bg0_pixel;
	gdk_NORMAL_bg.red   = defaultStyle.bg0_red;
	gdk_NORMAL_bg.green = defaultStyle.bg0_green;
	gdk_NORMAL_bg.blue  = defaultStyle.bg0_blue;
	NORMAL_bg = Color.gtk_new_system(gdk_NORMAL_bg);

	GdkColor gdk_NORMAL_text = new GdkColor();
	gdk_NORMAL_text.pixel = defaultStyle.text0_pixel;
	gdk_NORMAL_text.red   = defaultStyle.text0_red;
	gdk_NORMAL_text.green = defaultStyle.text0_green;
	gdk_NORMAL_text.blue  = defaultStyle.text0_blue;
	NORMAL_text = Color.gtk_new_system(gdk_NORMAL_text);

	GdkColor gdk_NORMAL_base = new GdkColor();
	gdk_NORMAL_base.pixel = defaultStyle.base0_pixel;
	gdk_NORMAL_base.red   = defaultStyle.base0_red;
	gdk_NORMAL_base.green = defaultStyle.base0_green;
	gdk_NORMAL_base.blue  = defaultStyle.base0_blue;
	NORMAL_base = Color.gtk_new_system(gdk_NORMAL_base);

	GdkColor gdk_SELECTED_text = new GdkColor();
	gdk_SELECTED_text.pixel = defaultStyle.text3_pixel;
	gdk_SELECTED_text.red   = defaultStyle.text3_red;
	gdk_SELECTED_text.green = defaultStyle.text3_green;
	gdk_SELECTED_text.blue  = defaultStyle.text3_blue;
	SELECTED_text = Color.gtk_new_system(gdk_SELECTED_text);

	GdkColor gdk_SELECTED_bg = new GdkColor();
	gdk_SELECTED_bg.pixel = defaultStyle.bg3_pixel;
	gdk_SELECTED_bg.red   = defaultStyle.bg3_red;
	gdk_SELECTED_bg.green = defaultStyle.bg3_green;
	gdk_SELECTED_bg.blue  = defaultStyle.bg3_blue;
	SELECTED_bg = Color.gtk_new_system(gdk_SELECTED_bg);

	GdkColor gdk_SELECTED_base = new GdkColor();
	gdk_SELECTED_base.pixel = defaultStyle.base3_pixel;
	gdk_SELECTED_base.red   = defaultStyle.base3_red;
	gdk_SELECTED_base.green = defaultStyle.base3_green;
	gdk_SELECTED_base.blue  = defaultStyle.base3_blue;
	SELECTED_base = Color.gtk_new_system(gdk_SELECTED_base);

	GdkColor gdk_SELECTED_light = new GdkColor();
	gdk_SELECTED_light.pixel = defaultStyle.light3_pixel;
	gdk_SELECTED_light.red   = defaultStyle.light3_red;
	gdk_SELECTED_light.green = defaultStyle.light3_green;
	gdk_SELECTED_light.blue  = defaultStyle.light3_blue;
	SELECTED_light = Color.gtk_new_system(gdk_SELECTED_light);
	
	GdkColor gdk_SELECTED_dark = new GdkColor();
	gdk_SELECTED_dark.pixel = defaultStyle.dark3_pixel;
	gdk_SELECTED_dark.red   = defaultStyle.dark3_red;
	gdk_SELECTED_dark.green = defaultStyle.dark3_green;
	gdk_SELECTED_dark.blue  = defaultStyle.dark3_blue;
	SELECTED_dark = Color.gtk_new_system(gdk_SELECTED_dark);
	

	GdkColor gdk_INSENSITIVE_light = new GdkColor();
	gdk_INSENSITIVE_light.pixel = defaultStyle.light4_pixel;
	gdk_INSENSITIVE_light.red   = defaultStyle.light4_red;
	gdk_INSENSITIVE_light.green = defaultStyle.light4_green;
	gdk_INSENSITIVE_light.blue  = defaultStyle.light4_blue;
	INSENSITIVE_light = Color.gtk_new_system(gdk_INSENSITIVE_light);

	GdkColor gdk_INSENSITIVE_dark = new GdkColor();
	gdk_INSENSITIVE_dark.pixel = defaultStyle.light4_pixel;
	gdk_INSENSITIVE_dark.red   = defaultStyle.light4_red;
	gdk_INSENSITIVE_dark.green = defaultStyle.light4_green;
	gdk_INSENSITIVE_dark.blue  = defaultStyle.light4_blue;
	INSENSITIVE_dark = Color.gtk_new_system(gdk_INSENSITIVE_dark);

	GdkColor gdk_INSENSITIVE_fg = new GdkColor();
	gdk_INSENSITIVE_fg.pixel = defaultStyle.fg4_pixel;
	gdk_INSENSITIVE_fg.red   = defaultStyle.fg4_red;
	gdk_INSENSITIVE_fg.green = defaultStyle.fg4_green;
	gdk_INSENSITIVE_fg.blue  = defaultStyle.fg4_blue;
	INSENSITIVE_fg = Color.gtk_new_system(gdk_INSENSITIVE_fg);

	GdkColor gdk_INSENSITIVE_bg = new GdkColor();
	gdk_INSENSITIVE_bg.pixel = defaultStyle.bg4_pixel;
	gdk_INSENSITIVE_bg.red   = defaultStyle.bg4_red;
	gdk_INSENSITIVE_bg.green = defaultStyle.bg4_green;
	gdk_INSENSITIVE_bg.blue  = defaultStyle.bg4_blue;
	INSENSITIVE_bg = Color.gtk_new_system(gdk_INSENSITIVE_bg);

	GdkColor gdk_INSENSITIVE_mid = new GdkColor();
	gdk_INSENSITIVE_mid.pixel = defaultStyle.bg4_pixel;
	gdk_INSENSITIVE_mid.red   = defaultStyle.bg4_red;
	gdk_INSENSITIVE_mid.green = defaultStyle.bg4_green;
	gdk_INSENSITIVE_mid.blue  = defaultStyle.bg4_blue;
	INSENSITIVE_mid = Color.gtk_new_system(gdk_INSENSITIVE_mid);

	GdkColor gdk_INSENSITIVE_text = new GdkColor();
	gdk_INSENSITIVE_text.pixel = defaultStyle.bg4_pixel;
	gdk_INSENSITIVE_text.red   = defaultStyle.bg4_red;
	gdk_INSENSITIVE_text.green = defaultStyle.bg4_green;
	gdk_INSENSITIVE_text.blue  = defaultStyle.bg4_blue;
	INSENSITIVE_text = Color.gtk_new_system(gdk_INSENSITIVE_text);

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
	GtkStyle style = new GtkStyle();
	OS.memmove (style, OS.gtk_widget_get_default_style(), GtkStyle.sizeof);
	int gdkFont = style.font;  // gives a GdkFont*
	return Font.gtk_new (gdkFont);
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
	initializeSystemColors ();
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
	
	timerCallback = new Callback (this, "timerProc", 2);
	timerProc = timerCallback.getAddress ();
	if (timerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	caretCallback = new Callback(this, "caretProc", 2);
	caretProc = caretCallback.getAddress();
	if (caretProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
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
public void internal_dispose_GC (int handle, GCData data) {
	OS.gdk_gc_destroy(handle);
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
	int gc = OS.gdk_gc_new(root);
	data.drawable = root;
	return gc;
}

final boolean isValidThread () {
	return thread == Thread.currentThread ();
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

	/* Release shells */
	Shell [] shells = WidgetTable.shells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			if (this == shell.getDisplay ()) shell.dispose ();
		}
	}
	while (readAndDispatch ()) {};
	
	/* Run dispose list */
	if (disposeList != null) {
		for (int i=0; i<disposeList.length; i++) {
			if (disposeList [i] != null) disposeList [i].run ();
		}
	}
	disposeList = null;
	
	/* Release synchronizer */
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

	/* Dispose the caret callback */
	if (caretID != 0) OS.gtk_timeout_remove (caretID);
	caretID = caretProc = 0;
	caretCallback.dispose ();
	caretCallback = null;
	
	/* Dispose the timer callback */
	if (timerIDs != null) {
		for (int i=0; i<timerIDs.length; i++) {
			if (timerIDs [i] != 0) OS.gtk_timeout_remove (timerIDs [i]);
		}
	}
	timerIDs = null;
	timerList = null;
	timerProc = 0;
	timerCallback.dispose ();
	timerCallback = null;

	messages = null;  messageLock = null; thread = null;
	messagesSize = windowProc2 = windowProc3 = windowProc4 = windowProc5 = 0;
	
	NORMAL_fg = NORMAL_bg = NORMAL_dark = NORMAL_mid = NORMAL_light = NORMAL_text = NORMAL_base =
	SELECTED_bg = SELECTED_dark = SELECTED_light = SELECTED_text = SELECTED_base =
	INSENSITIVE_fg = INSENSITIVE_bg = INSENSITIVE_dark = INSENSITIVE_mid = INSENSITIVE_light = INSENSITIVE_text =null;
}

RunnableLock removeFirst () {
	synchronized (messageLock) {
		if (messagesSize == 0) return null;
		RunnableLock lock = messages [0];
		System.arraycopy (messages, 1, messages, 0, --messagesSize);
		messages [messagesSize] = null;
		if (messagesSize == 0) messages = null;
		return lock;
	}
}

boolean runAsyncMessages () {
	return synchronizer.runAsyncMessages ();
}

boolean runDeferredEvents () {
	/*
	* Run deferred events.  This code is always
	* called  in the Display's thread so it must
	* be re-enterant need not be synchronized.
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
				widget.notifyListeners (event.type, event);
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
	/* Do nothing - Gtk doesn't have the concept of application name. */
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
 * number of milliseconds have elapsed.
 *
 * @param milliseconds the delay before running the runnable
 * @param runnable code to run on the user-interface thread
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #asyncExec
 */
public void timerExec (int milliseconds, Runnable runnable) {
	checkDevice ();
	if (timerList == null) timerList = new Runnable [4];
	if (timerIDs == null) timerIDs = new int [4];
	int index = 0;
	while (index < timerList.length) {
		if (timerList [index] == null) break;
		index++;
	}
	if (index == timerList.length) {
		Runnable [] newTimerList = new Runnable [timerList.length + 4];
		System.arraycopy (timerList, 0, newTimerList, 0, timerList.length);
		timerList = newTimerList;
		int [] newTimerIDs = new int [timerIDs.length + 4];
		System.arraycopy (timerIDs, 0, newTimerIDs, 0, timerIDs.length);
		timerIDs = newTimerIDs;
	}
	int timerID = OS.gtk_timeout_add (milliseconds, timerProc, index);
	if (timerID != 0) {
		timerIDs [index] = timerID;
		timerList [index] = runnable;
	}
}

int timerProc (int index, int id) {
	if (timerList == null) return 0;
	if (0 <= index && index < timerList.length) {
		Runnable runnable = timerList [index];
		timerList [index] = null;
		timerIDs [index] = 0;
		if (runnable != null) runnable.run ();
	}
	return 0;
}

int caretProc (int clientData, int id) {
	caretID = 0;
	if (currentCaret == null) {
		return 0;
	}
	if (currentCaret.blinkCaret()) {
		int blinkRate = currentCaret.blinkRate;
		caretID = OS.gtk_timeout_add (blinkRate, caretProc, 0);
	} else {
		currentCaret = null;
	}
	return 0;
}

void setCurrentCaret (Caret caret) {
	if (caretID != 0) OS.gtk_timeout_remove(caretID);
	caretID = 0;
	currentCaret = caret;
	if (caret == null) return;
	int blinkRate = currentCaret.blinkRate;
	caretID = OS.gtk_timeout_add (blinkRate, caretProc, 0); 
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
}

int windowProc (int handle, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (user_data, 0, 0, 0);
}

int windowProc (int handle, int int0, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (user_data, int0, 0, 0);
}

int windowProc (int handle, int int0, int int1, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (user_data, int0, int1, 0);
}

int windowProc (int handle, int int0, int int1, int int2, int user_data) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (user_data, int0, int1, int2);
}

}

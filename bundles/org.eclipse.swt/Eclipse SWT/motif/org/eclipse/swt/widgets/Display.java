package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
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

	/* Motif Only Public Fields */
	public XAnyEvent xEvent = new XAnyEvent ();
	
	/* Windows, Events and Callbacks */
	Callback windowCallback;
	int windowProc, shellHandle;
	static boolean XtInitialized;
	static String APP_NAME = "SWT";
	byte [] displayName, appName, appClass;
	Event [] eventQueue;
	
	/* Default Fonts, Colors, Insets, Widths and Heights. */
	int defaultFont, defaultFontList;
	int listFont, textFont, buttonFont, labelFont;
	int dialogBackground, dialogForeground;
	int buttonBackground, buttonForeground, buttonShadowThickness;
	int compositeBackground, compositeForeground;
	int compositeTopShadow, compositeBottomShadow, compositeBorder;
	int listBackground, listForeground, textBackground, textForeground;
	int labelBackground, labelForeground, scrollBarBackground, scrollBarForeground;
	int scrolledInsetX, scrolledInsetY, scrolledMarginX, scrolledMarginY;
	int defaultBackground, defaultForeground;
	int textHighlightThickness;
	
	/* System Colors */
	XColor COLOR_WIDGET_DARK_SHADOW, COLOR_WIDGET_NORMAL_SHADOW, COLOR_WIDGET_LIGHT_SHADOW;
	XColor COLOR_WIDGET_HIGHLIGHT_SHADOW, COLOR_WIDGET_BACKGROUND, COLOR_WIDGET_BORDER;
	XColor COLOR_LIST_FOREGROUND, COLOR_LIST_BACKGROUND, COLOR_LIST_SELECTION, COLOR_LIST_SELECTION_TEXT;
	Color COLOR_INFO_BACKGROUND;
	
	/* Initial Guesses for Shell Trimmings. */
	int borderTrimWidth = 4, borderTrimHeight = 4;
	int resizeTrimWidth = 6, resizeTrimHeight = 6;
	int titleBorderTrimWidth = 5, titleBorderTrimHeight = 28;
	int titleResizeTrimWidth = 6, titleResizeTrimHeight = 29;
	int titleTrimWidth = 0, titleTrimHeight = 23;
	
	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread;
	
	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Timers */
	int [] timerIDs;
	Runnable [] timerList;
	Callback timerCallback;
	int timerProc;
	
	/* Key Mappings. */
	static int [] [] KeyTable = {
		
		/* Keyboard and Mouse Masks */
		{OS.XK_Alt_L,		SWT.ALT},
		{OS.XK_Alt_R,		SWT.ALT},
		{OS.XK_Shift_L,		SWT.SHIFT},
		{OS.XK_Shift_R,		SWT.SHIFT},
		{OS.XK_Control_L,	SWT.CONTROL},
		{OS.XK_Control_R,	SWT.CONTROL},
		
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},
		
		/* Non-Numeric Keypad Constants */
		{OS.XK_Up,			SWT.ARROW_UP},
		{OS.XK_Down,		SWT.ARROW_DOWN},
		{OS.XK_Left,		SWT.ARROW_LEFT},
		{OS.XK_Right,		SWT.ARROW_RIGHT},
		{OS.XK_Page_Up,		SWT.PAGE_UP},
		{OS.XK_Page_Down,	SWT.PAGE_DOWN},
		{OS.XK_Home,		SWT.HOME},
		{OS.XK_End,			SWT.END},
		{OS.XK_Insert,		SWT.INSERT},
//		{OS.XK_Delete,		SWT.DELETE},
	
		/* Functions Keys */
		{OS.XK_F1,	SWT.F1},
		{OS.XK_F2,	SWT.F2},
		{OS.XK_F3,	SWT.F3},
		{OS.XK_F4,	SWT.F4},
		{OS.XK_F5,	SWT.F5},
		{OS.XK_F6,	SWT.F6},
		{OS.XK_F7,	SWT.F7},
		{OS.XK_F8,	SWT.F8},
		{OS.XK_F9,	SWT.F9},
		{OS.XK_F10,	SWT.F10},
		{OS.XK_F11,	SWT.F11},
		{OS.XK_F12,	SWT.F12},
		
		/* Numeric Keypad Constants */
		/*
		{OS.XK_KP_Add,		SWT.KP_PLUS},
		{OS.XK_KP_Subtract,	SWT.KP_MINUS},
		{OS.XK_KP_Multiply,	SWT.KP_TIMES},
		{OS.XK_KP_Divide,	SWT.KP_DIVIDE},
		{OS.XK_KP_Decimal,	SWT.KP_PERIOD},
		{OS.XK_KP_Enter,	SWT.KP_ENTER},
		{OS.XK_KP_0,		SWT.KP_0},
		{OS.XK_KP_1,		SWT.KP_1},
		{OS.XK_KP_2,		SWT.KP_2},
		{OS.XK_KP_3,		SWT.KP_3},
		{OS.XK_KP_4,		SWT.KP_4},
		{OS.XK_KP_5,		SWT.KP_5},
		{OS.XK_KP_6,		SWT.KP_6},
		{OS.XK_KP_7,		SWT.KP_7},
		{OS.XK_KP_8,		SWT.KP_8},
		{OS.XK_KP_9,		SWT.KP_9},
		*/
	};

	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Double Click */
	int lastTime, lastButton;
	
	/* Current caret */
	Caret currentCaret;
	Callback caretCallback;
	int caretID, caretProc;

	/* Workaround for GP when disposing a display */
	static boolean DisplayDisposed;
			
	/* Package name */
	static final String PACKAGE_NAME;
	static {
		String name = Display.class.getName ();
		int index = name.lastIndexOf ('.');
		PACKAGE_NAME = name.substring (0, index + 1);
	}
	
	/* Mouse Hover */
	Callback mouseHoverCallback;
	int mouseHoverID, mouseHoverProc;
	int mouseHoverHandle, toolTipHandle;
	
	/* Parse Tables */
	int [] parseTable;
	int crPointer, tabPointer;
	
	/* Xt Translations */
	int dragTranslations;
	int arrowTranslations, tabTranslations;
	
	/* Check Expose Proc */
	Callback checkExposeCallback;
	int checkExposeProc;
	int exposeCount;
	
	/* Wake */
	Callback wakeCallback;
	int wakeProc, read_fd, write_fd, inputID;
	byte [] wake_buffer = new byte [1];
	
	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;

	static final byte[] _MOTIF_DEFAULT_LOCALE = Converter.wcsToMbcs(null, "_MOTIF_DEFAULT_LOCALE");

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
	super (checkNull (data));
}
static DeviceData checkNull (DeviceData data) {
	if (data == null) data = new DeviceData ();
	if (data.application_name == null) {
		data.application_name = APP_NAME;
	}
	if (data.application_class == null) {
		data.application_class = APP_NAME;
	}
	return data;
}
protected void checkDevice () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}
void addMouseHoverTimeOut (int handle) {
	if (mouseHoverID != 0) OS.XtRemoveTimeOut (mouseHoverID);		
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	mouseHoverID = OS.XtAppAddTimeOut (xtContext, 400, mouseHoverProc, handle);
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
	checkDevice ();
	OS.XBell (xDisplay, 100);
	OS.XFlush (xDisplay);
}
int caretProc (int clientData, int id) {
	caretID = 0;
	if (currentCaret == null) return 0;
	if (currentCaret.blinkCaret ()) {
		int blinkRate = currentCaret.blinkRate;
		int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
		caretID = OS.XtAppAddTimeOut (xtContext, blinkRate, caretProc, 0);
	} else {
		currentCaret = null;
	}
	return 0;
}
int checkExposeProc (int display, int event, int window) {
	XExposeEvent xEvent  = new XExposeEvent ();
	OS.memmove (xEvent, event, XExposeEvent.sizeof);
	if (xEvent.window == window) {
		if (xEvent.type == OS.Expose || xEvent.type == OS.GraphicsExpose) {
			exposeCount++;
		}
	}
	return 0;
}
synchronized void checkDisplay () {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] != null && Displays [i].thread == thread) {
			error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
}
protected void checkSubclass () {
	if (!Display.isValidClass (getClass ())) {
		error (SWT.ERROR_INVALID_SUBCLASS);
	}
}
protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay ();
	thread = Thread.currentThread ();
	createDisplay (data);
	register ();
	if (Default == null) Default = this;
}
void createDisplay (DeviceData data) {
	
	/* Initialize X and Xt */
	synchronized (Display.class) {
		if (!XtInitialized) {
			OS.XInitThreads ();
			OS.XtToolkitThreadInitialize ();
			OS.XtToolkitInitialize ();
	
			/* Bug in XpExtention. If XInitThreads is called before
			* any Xp functions then XpCheckExtInit hangs.  The workaround
			* is to create the printer display before calling XInitThreads.
			*/
//			int xtContext = OS.XtCreateApplicationContext ();
//			byte[] buffer = Converter.wcsToMbcs ( null, Device.XDefaultPrintServer, true );
//			xPrinter = OS.XtOpenDisplay (xtContext, buffer, null, null, 0, 0, new int [] {0}, 0);
//			if (xPrinter != 0) {
//				OS.XpQueryVersion (xPrinter, new short [1], new short [1]);
//			}
//			//OS.XInitThreads ();
		}
		XtInitialized = true;
	}

	/* Create the AppContext */
	int [] argc = new int [] {0};
	int xtContext = OS.XtCreateApplicationContext ();
	OS.XtSetLanguageProc (xtContext, 0, 0);
	
	/* Compute the display name, application name and class */
	String display_name = null;
	String application_name = APP_NAME;
	String application_class = APP_NAME;
	if (data != null) {
		if (data.display_name != null) display_name = data.display_name;
		if (data.application_name != null) application_name = data.application_name;
		if (data.application_class != null) application_class = data.application_class;
	}
	if (display_name != null) displayName = Converter.wcsToMbcs (null, display_name, true);
	if (application_name != null) appName = Converter.wcsToMbcs (null, application_name, true);
	if (application_class != null) appClass = Converter.wcsToMbcs (null, application_class, true);
	
	/* Create the XDisplay */
	xDisplay = OS.XtOpenDisplay (xtContext, displayName, appName, appClass, 0, 0, argc, 0);
	DisplayDisposed = false;
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
	/*
	* Destroy AppContext (this destroys the display)
	*/
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	OS.XtDestroyApplicationContext (xtContext);
	DisplayDisposed = true;
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
void error (int code) {
	SWT.error(code);
}
boolean filterEvent (XAnyEvent event) {

	/* Check the event and find the widget */
	if (event.type != OS.KeyPress) return false;
	if (OS.XFilterEvent(event, OS.None)) return true;
	XKeyEvent keyEvent = new XKeyEvent ();
	
	/* Move the any event into the key event */
	OS.memmove (keyEvent, event, XAnyEvent.sizeof);
	if (keyEvent.keycode == 0) return false;
	int xWindow = keyEvent.window;
	if (xWindow == 0) return false;
	int handle = OS.XtWindowToWidget (xDisplay, xWindow);
	if (handle == 0) return false;
	handle = OS.XmGetFocusWidget (handle);
	if (handle == 0) return false;
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return false;
	if (!(widget instanceof Control)) return false;
	Control control = (Control) widget;
	
	/* Get the unaffected character and keysym */
	int oldState = keyEvent.state;
	keyEvent.state = 0;
	byte [] buffer1 = new byte [4];
	int [] buffer2 = new int [1];
	int key = 0;
	if (OS.XLookupString (keyEvent, buffer1, 1, buffer2, null) != 0) {
		key = buffer1 [0] & 0xFF;
	}
	int keysym = buffer2 [0] & 0xFFFF;
	keyEvent.state = oldState;
	
	/* Check for a mnemonic key */
	if (key != 0) {
		if (control.translateMnemonic (key, keyEvent)) return true;
	}
	
	/* Check for a traversal key */
	if (keysym == 0) return false;
	if (keysym == OS.XK_Escape || keysym == OS.XK_Cancel ||
		keysym == OS.XK_Tab || keysym == OS.XK_Return ||
		keysym == OS.XK_Up || keysym == OS.XK_Down ||
		keysym == OS.XK_Left || keysym == OS.XK_Right) {
			if (control.translateTraversal (keysym, keyEvent)) return true;
	}

	/* Answer false because the event was not processed */
	return false;
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
	return WidgetTable.get (handle);
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
	Control control = getFocusControl ();
	if (control == null) return null;
	return control.getShell ();
}
/**
 * Returns the display which the currently running thread is
 * the user-interface thread for, or null if the currently
 * running thread is not a user-interface thread for any display.
 *
 * @return the current display
 */
public static synchronized Display getCurrent () {
	return findDisplay (Thread.currentThread ());
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
	checkDevice ();
	int [] unused = new int [1], buffer = new int [1];
	int xWindow, xParent = OS.XDefaultRootWindow (xDisplay);
	do {
		if (OS.XQueryPointer (
			xDisplay, xParent, unused, buffer,
			unused, unused, unused, unused, unused) == 0) return null;
		if ((xWindow = buffer [0]) != 0) xParent = xWindow;
	} while (xWindow != 0);
	int handle = OS.XtWindowToWidget (xDisplay, xParent);
	if (handle == 0) return null;
	do {
		Widget widget = WidgetTable.get (handle);
		if (widget != null && widget instanceof Control) {
			Control control = (Control) widget;
			if (control.getEnabled ()) return control;
		}
	} while ((handle = OS.XtParent (handle)) != 0);
	return null;
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
	int window = OS.XDefaultRootWindow (xDisplay);
	int [] rootX = new int [1];
	int [] rootY = new int [1];
	int [] unused = new int [1];
	OS.XQueryPointer(xDisplay, window, unused, unused, rootX, rootY, unused, unused, unused);
	return new Point (rootX [0], rootY [0]);
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
	return OS.XtGetMultiClickTime (xDisplay);
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
	int [] buffer1 = new int [1], buffer2 = new int [1];
	OS.XGetInputFocus (xDisplay, buffer1, buffer2);
	int xWindow = buffer1 [0];
	if (xWindow == 0) return null;
	int handle = OS.XtWindowToWidget (xDisplay, xWindow);
	if (handle == 0) return null;
	handle = OS.XmGetFocusWidget (handle);
	if (handle == 0) return null;
	do {
		Widget widget = WidgetTable.get (handle);
		if (widget != null && widget instanceof Control) {
			Control window = (Control) widget;
			if (window.getEnabled ()) return window;
		}
	} while ((handle = OS.XtParent (handle)) != 0);
	return null;
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
		if (!shell.isDisposed () && this == shell.getDisplay ()) {
			count++;
		}
	}
	if (count == shells.length) return shells;
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && this == shell.getDisplay ()) {
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
	XColor xColor = null;
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: 		return super.getSystemColor (SWT.COLOR_BLACK);
		case SWT.COLOR_INFO_BACKGROUND: 		return COLOR_INFO_BACKGROUND;
		case SWT.COLOR_TITLE_FOREGROUND:		return super.getSystemColor (SWT.COLOR_WHITE);
		case SWT.COLOR_TITLE_BACKGROUND:		return super.getSystemColor (SWT.COLOR_DARK_BLUE);
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:	return super.getSystemColor (SWT.COLOR_BLUE);
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:	return super.getSystemColor (SWT.COLOR_BLACK);
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:	return super.getSystemColor (SWT.COLOR_DARK_GRAY);
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:	return super.getSystemColor (SWT.COLOR_GRAY);
		case SWT.COLOR_WIDGET_DARK_SHADOW:	xColor = COLOR_WIDGET_DARK_SHADOW; break;
		case SWT.COLOR_WIDGET_NORMAL_SHADOW:	xColor = COLOR_WIDGET_NORMAL_SHADOW; break;
		case SWT.COLOR_WIDGET_LIGHT_SHADOW: 	xColor = COLOR_WIDGET_LIGHT_SHADOW; break;
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:	xColor = COLOR_WIDGET_HIGHLIGHT_SHADOW; break;
		case SWT.COLOR_WIDGET_BACKGROUND: 	xColor = COLOR_WIDGET_BACKGROUND; break;
		case SWT.COLOR_WIDGET_FOREGROUND:
		case SWT.COLOR_WIDGET_BORDER: 		xColor = COLOR_WIDGET_BORDER; break;
		case SWT.COLOR_LIST_FOREGROUND: 	xColor = COLOR_LIST_FOREGROUND; break;
		case SWT.COLOR_LIST_BACKGROUND: 	xColor = COLOR_LIST_BACKGROUND; break;
		case SWT.COLOR_LIST_SELECTION: 		xColor = COLOR_LIST_SELECTION; break;
		case SWT.COLOR_LIST_SELECTION_TEXT: 	xColor = COLOR_LIST_SELECTION_TEXT; break;
		default:
			return super.getSystemColor (id);	
	}
	if (xColor == null) return super.getSystemColor (SWT.COLOR_BLACK);
	return Color.motif_new (this, xColor);
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
	return Font.motif_new (this, defaultFontList);
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
void hideToolTip () {
	if (toolTipHandle != 0) {
		int shellHandle = OS.XtParent(toolTipHandle);
		OS.XtDestroyWidget(shellHandle);
	}
	toolTipHandle = 0;
}
protected void init () {
	super.init ();
	initializeDisplay ();
	initializeButton ();
	initializeComposite ();
	initializeDialog ();
	initializeLabel ();
	initializeList ();
	initializeScrollBar ();
	initializeText ();
	initializeSystemColors ();
	initializeDefaults ();
	initializeParseTable ();
	initializeTranslations ();
}
void initializeButton () {

	int shellHandle, widgetHandle;
	int widgetClass = OS.TopLevelShellWidgetClass ();

	/* Get the push button information */
	shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	widgetHandle = OS.XmCreatePushButton (shellHandle, null, null, 0);
	OS.XtManageChild (widgetHandle);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
	int [] argList = {
		OS.XmNforeground, 0, 	/* 1 */
		OS.XmNbackground, 0,	/* 3 */
		OS.XmNshadowThickness, 0, 	/* 5 */
		OS.XmNfontList, 0,		/* 7 */
	};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	buttonForeground = argList [1];  buttonBackground = argList [3];
	buttonShadowThickness = argList [5];
	/**
	 * Feature in Motif. Querying the font list from the widget and
	 * then destroying the shell (and the widget) could cause the
	 * font list to be freed as well. The fix is to make a copy of
	 * the font list, then to free it when the display is disposed.
	 */ 
	buttonFont = OS.XmFontListCopy (argList [7]);
	OS.XtDestroyWidget (shellHandle);
}
void initializeComposite () {
	int widgetClass = OS.TopLevelShellWidgetClass ();
	int shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	int scrolledHandle = OS.XmCreateMainWindow (shellHandle, null, null, 0);
	int [] argList1 = {OS.XmNorientation, OS.XmHORIZONTAL};
	int hScrollHandle = OS.XmCreateScrollBar (scrolledHandle, null, argList1, argList1.length / 2);
	OS.XtManageChild (hScrollHandle);
	int [] argList2 = {OS.XmNorientation, OS.XmVERTICAL};
	int vScrollHandle = OS.XmCreateScrollBar (scrolledHandle, null, argList2, argList2.length / 2);
	OS.XtManageChild (vScrollHandle);
	OS.XtManageChild (scrolledHandle);
	int [] argList5 = {
		OS.XmNmarginWidth, 3,
		OS.XmNmarginHeight, 3, 
	};
	int formHandle = OS.XmCreateForm (scrolledHandle, null, argList5, argList5.length / 2);
	OS.XtManageChild (formHandle);
	int [] argList6 = {
		OS.XmNmarginWidth, 0,
		OS.XmNmarginHeight, 0,
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNtopAttachment, OS.XmATTACH_FORM,
		OS.XmNbottomAttachment, OS.XmATTACH_FORM,
		OS.XmNleftAttachment, OS.XmATTACH_FORM,
		OS.XmNrightAttachment, OS.XmATTACH_FORM,
	};
	int widgetHandle = OS.XmCreateDrawingArea (formHandle, null, argList6, argList6.length / 2);
	OS.XtManageChild (widgetHandle);
	OS.XmMainWindowSetAreas (scrolledHandle, 0, 0, hScrollHandle, vScrollHandle, formHandle);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	int screen = OS.XDefaultScreen (xDisplay);
	OS.XtResizeWidget (shellHandle, OS.XDisplayWidth (xDisplay, screen), OS.XDisplayHeight (xDisplay, screen), 0);
	OS.XtRealizeWidget (shellHandle);
	int [] argList3 = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (scrolledHandle, argList3, argList3.length / 2);
	int [] argList8 = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (formHandle, argList8, argList8.length / 2);
	int [] argList4 = {
		OS.XmNx, 0,			/* 1 */
		OS.XmNy, 0,			/* 3 */
		OS.XmNwidth, 0,			/* 5 */
		OS.XmNheight, 0,		/* 7 */
		OS.XmNforeground, 0,		/* 9 */
		OS.XmNbackground, 0,		/* 11 */
		OS.XmNtopShadowColor, 0,	/* 13 */
		OS.XmNbottomShadowColor, 0,	/* 15 */
		OS.XmNborderColor, 0,		/* 17 */
	};
	OS.XtGetValues (widgetHandle, argList4, argList4.length / 2);
	scrolledInsetX = argList4 [1] + argList8 [1];
	scrolledInsetY = argList4 [3] + argList8 [3];
	scrolledMarginX = argList3 [1] - argList8 [1] - argList4 [1] - argList4 [5];
	scrolledMarginY = argList3 [3] - argList8 [3] - argList4 [3] - argList4 [7];
	compositeForeground = argList4 [9];  compositeBackground = argList4 [11];
	compositeTopShadow = argList4 [13];  compositeBottomShadow = argList4 [15];
	compositeBorder = argList4 [17];
	OS.XtDestroyWidget (shellHandle);
}
void initializeDefaults () {
	defaultFontList = labelFont;
	defaultForeground = compositeForeground;
	defaultBackground = compositeBackground;
	
	/**
	 * Initialize the default font id to the first
	 * font in the default font list
	 */
	int [] buffer = new int [1];
	if (!OS.XmFontListInitFontContext (buffer, defaultFontList)) {
		return;
	}
	int context = buffer [0];
	XFontStruct fontStruct = new XFontStruct ();
	int [] fontStructPtr = new int [1];
	int [] fontNamePtr = new int [1];
	
	/* Take the first entry from the font list */
	int fontListEntry = OS.XmFontListNextEntry (context);
	int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
	if (buffer [0] == 0) { 
		/* FontList contains a single font */
		OS.memmove (fontStruct, fontPtr, XFontStruct.sizeof);
		defaultFont = fontStruct.fid;
	} else { 
		/* FontList contains a fontSet */
		/* Take the first font in the font set */
		int nFonts = OS.XFontsOfFontSet (fontPtr, fontStructPtr, fontNamePtr);
		if (nFonts > 0) {
			int [] fontStructs = new int [1];
			OS.memmove (fontStructs, fontStructPtr [0], 4);
			OS.memmove (fontStruct, fontStructs [0], XFontStruct.sizeof);
			defaultFont = fontStruct.fid;
		}
	}
	OS.XmFontListFreeFontContext (context);
}
void initializeDialog () {
	int shellHandle, widgetHandle;
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	widgetHandle = OS.XmCreateDialogShell (shellHandle, null, null, 0);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	dialogForeground = argList [1];  dialogBackground = argList [3];
	OS.XtDestroyWidget (shellHandle);
}
void initializeDisplay () {
	
	/* Create the callbacks */
	windowCallback = new Callback (this, "windowProc", 4);
	windowProc = windowCallback.getAddress ();
	if (windowProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	timerCallback = new Callback (this, "timerProc", 2);
	timerProc = timerCallback.getAddress ();
	if (timerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	caretCallback = new Callback (this, "caretProc", 2);
	caretProc = caretCallback.getAddress ();
	if (caretProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	mouseHoverCallback = new Callback (this, "mouseHoverProc", 2);
	mouseHoverProc = mouseHoverCallback.getAddress ();
	if (mouseHoverProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	checkExposeCallback = new Callback (this, "checkExposeProc", 3);
	checkExposeProc = checkExposeCallback.getAddress ();
	if (checkExposeProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	wakeCallback = new Callback (this, "wakeProc", 3);
	wakeProc = wakeCallback.getAddress ();
	if (wakeProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	/* Create and install the pipe used to wake up from sleep */
	int [] filedes = new int [2];
	if (OS.pipe (filedes) != 0) error (SWT.ERROR_NO_HANDLES);
	read_fd = filedes [0];  write_fd = filedes [1];
	int xtAppContext = OS.XtDisplayToApplicationContext (xDisplay);
	inputID = OS.XtAppAddInput (xtAppContext, read_fd, OS.XtInputReadMask, wakeProc, 0);
	
	/*
	* Use dynamic Drag and Drop Protocol styles.
	* Preregistered protocol is not supported.
	*/
	int xmDisplay = OS.XmGetXmDisplay (xDisplay);
	int [] args = new int [] {
		OS.XmNenableThinThickness, 1,
		OS.XmNdragInitiatorProtocolStyle, OS.XmDRAG_DYNAMIC,
		OS.XmNdragReceiverProtocolStyle, OS.XmDRAG_DYNAMIC,
	};
	OS.XtSetValues (xmDisplay, args, args.length / 2);

	/* Create the hidden Override shell parent */
	int xScreen = OS.XDefaultScreen (xDisplay);
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtResizeWidget (shellHandle, OS.XDisplayWidth (xDisplay, xScreen), OS.XDisplayHeight (xDisplay, xScreen), 0);
	OS.XtRealizeWidget (shellHandle);
	
	/*
	* Bug in MOTIF.  For some reason, calls to XmGetPixmap ()
	* and XmGetPixmapByDepth fail to find the pixmap unless at
	* least one message box has been created.  The fix is to
	* create and destroy a message box.
	*/
//	int dialog = OS.XmCreateInformationDialog (shellHandle, null, null, 0);
//	OS.XtDestroyWidget (dialog);
}
void initializeLabel () {
	int shellHandle, widgetHandle;
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	widgetHandle = OS.XmCreateLabel (shellHandle, null, null, 0);
	OS.XtManageChild (widgetHandle);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0, OS.XmNfontList, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	labelForeground = argList [1];  labelBackground = argList [3]; 
	/**
	 * Feature in Motif. Querying the font list from the widget and
	 * then destroying the shell (and the widget) could cause the
	 * font list to be freed as well. The fix is to make a copy of
	 * the font list, then to free it when the display is disposed.
	 */ 
	labelFont = OS.XmFontListCopy (argList [5]);
	OS.XtDestroyWidget (shellHandle);
}
void initializeList () {
	int shellHandle, widgetHandle;
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	widgetHandle = OS.XmCreateScrolledList (shellHandle, null, null, 0);
	OS.XtManageChild (widgetHandle);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0, OS.XmNfontList, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	listForeground = argList [1];
	listBackground = argList [3];
	/**
	 * Feature in Motif. Querying the font list from the widget and
	 * then destroying the shell (and the widget) could cause the
	 * font list to be freed as well. The fix is to make a copy of
	 * the font list, then to free it when the display is disposed.
	 */ 
	listFont = OS.XmFontListCopy (argList [5]); 
	OS.XtDestroyWidget (shellHandle);
}
void initializeParseTable () {
	byte[] tabBuffer = {(byte) '\t', 0};
	tabPointer = OS.XtMalloc (tabBuffer.length);
	OS.memmove (tabPointer, tabBuffer, tabBuffer.length);		
	int tabString = OS.XmStringComponentCreate(OS.XmSTRING_COMPONENT_TAB, 0, null);
	int [] argList = {
		OS.XmNpattern, tabPointer,
		OS.XmNsubstitute, tabString,
	};
	int tabMapping = OS.XmParseMappingCreate(argList, argList.length / 2);
	OS.XmStringFree(tabString);
	
	byte[] crBuffer = {(byte) '\n', 0};
	crPointer = OS.XtMalloc (crBuffer.length);		
	OS.memmove (crPointer, crBuffer, crBuffer.length);		
	int crString = OS.XmStringComponentCreate(OS.XmSTRING_COMPONENT_SEPARATOR, 0, null);
	argList = new int[] {
		OS.XmNpattern, crPointer,
		OS.XmNsubstitute, crString,
	};
	int crMapping = OS.XmParseMappingCreate(argList, argList.length / 2);
	OS.XmStringFree(crString);
				
	parseTable = new int[2];
	parseTable[0] = tabMapping;
	parseTable[1] = crMapping;
}
void initializeScrollBar () {
	int shellHandle, widgetHandle;
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	widgetHandle = OS.XmCreateScrollBar (shellHandle, null, null, 0);
	OS.XtManageChild (widgetHandle);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	scrollBarForeground = argList [1];  scrollBarBackground = argList [3];
	OS.XtDestroyWidget (shellHandle);
}
void initializeSystemColors () {
	int [] argList = {OS.XmNcolormap, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	int colormap = argList [1];
	
	COLOR_WIDGET_DARK_SHADOW = new XColor();
	COLOR_WIDGET_DARK_SHADOW.pixel = compositeBottomShadow;
	OS.XQueryColor (xDisplay, colormap, COLOR_WIDGET_DARK_SHADOW);
	
	COLOR_WIDGET_NORMAL_SHADOW = new XColor();
	COLOR_WIDGET_NORMAL_SHADOW.pixel = compositeBottomShadow;
	OS.XQueryColor (xDisplay, colormap, COLOR_WIDGET_NORMAL_SHADOW);
	
	COLOR_WIDGET_LIGHT_SHADOW = new XColor();
	COLOR_WIDGET_LIGHT_SHADOW.pixel = compositeTopShadow;
	OS.XQueryColor (xDisplay, colormap, COLOR_WIDGET_LIGHT_SHADOW);
	
	COLOR_WIDGET_HIGHLIGHT_SHADOW = new XColor();
	COLOR_WIDGET_HIGHLIGHT_SHADOW.pixel = compositeTopShadow;
	OS.XQueryColor (xDisplay, colormap, COLOR_WIDGET_HIGHLIGHT_SHADOW);
	
	COLOR_WIDGET_BACKGROUND = new XColor();
	COLOR_WIDGET_BACKGROUND.pixel = compositeBackground;
	OS.XQueryColor (xDisplay, colormap, COLOR_WIDGET_BACKGROUND);
	
	COLOR_WIDGET_BORDER = new XColor();
	COLOR_WIDGET_BORDER.pixel = compositeBorder;
	OS.XQueryColor (xDisplay, colormap, COLOR_WIDGET_BORDER);
	
	COLOR_LIST_FOREGROUND = new XColor();
	COLOR_LIST_FOREGROUND.pixel = listForeground;
	OS.XQueryColor (xDisplay, colormap, COLOR_LIST_FOREGROUND);
	
	COLOR_LIST_BACKGROUND = new XColor();
	COLOR_LIST_BACKGROUND.pixel = listBackground;
	OS.XQueryColor (xDisplay, colormap, COLOR_LIST_BACKGROUND);
	
	COLOR_LIST_SELECTION = new XColor();
	COLOR_LIST_SELECTION.pixel = listForeground;
	OS.XQueryColor (xDisplay, colormap, COLOR_LIST_SELECTION);
	
	COLOR_LIST_SELECTION_TEXT = new XColor();
	COLOR_LIST_SELECTION_TEXT.pixel = listBackground;
	OS.XQueryColor (xDisplay, colormap, COLOR_LIST_SELECTION_TEXT);
	
	COLOR_INFO_BACKGROUND = new Color (this, 0xFF, 0xFF, 0xE1);
}
void initializeText () {
	int shellHandle, widgetHandle;
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (appName, appClass, widgetClass, xDisplay, null, 0);
	widgetHandle = OS.XmCreateScrolledText (shellHandle, null, null, 0);
	OS.XtManageChild (widgetHandle);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
	int [] argList = {OS.XmNforeground, 0, OS.XmNbackground, 0, OS.XmNfontList, 0, OS.XmNhighlightThickness, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	textForeground = argList [1];  textBackground = argList [3];  
	textHighlightThickness = argList[7];
	/**
	 * Feature in Motif. Querying the font list from the widget and
	 * then destroying the shell (and the widget) could cause the
	 * font list to be freed as well. The fix is to make a copy of
	 * the font list, then to free it when the display is disposed.
	 */ 
	textFont = OS.XmFontListCopy (argList [5]); 
	OS.XtDestroyWidget (shellHandle);

}
void initializeTranslations () {
	byte [] buffer1 = Converter.wcsToMbcs (null, "<Key>osfUp:\n<Key>osfDown:\n<Key>osfLeft:\n<Key>osfRight:\0");
	arrowTranslations = OS.XtParseTranslationTable (buffer1);
	byte [] buffer2 = Converter.wcsToMbcs (null, "~Meta ~Alt <Key>Tab:\nShift ~Meta ~Alt <Key>Tab:\0");
	tabTranslations = OS.XtParseTranslationTable (buffer2);
	byte [] buffer3 = Converter.wcsToMbcs (null, "<Btn2Down>:\0");
	dragTranslations = OS.XtParseTranslationTable (buffer3);
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
	int xDrawable = OS.XDefaultRootWindow (xDisplay);
	int xGC = OS.XCreateGC (xDisplay, xDrawable, 0, null);
	if (xGC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.XSetSubwindowMode (xDisplay, xGC, OS.IncludeInferiors);
	if (data != null) {
		data.device = this;
		data.display = xDisplay;
		data.drawable = xDrawable;
		data.fontList = defaultFontList;
		data.colormap = OS.XDefaultColormap (xDisplay, OS.XDefaultScreen (xDisplay));
	}
	return xGC;
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
public void internal_dispose_GC (int gc, GCData data) {
	OS.XFreeGC(xDisplay, gc);
}
boolean isValidThread () {
	return thread == Thread.currentThread ();
}
static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_NAME);
}
int mouseHoverProc (int handle, int id) {
	mouseHoverID = mouseHoverHandle = 0;
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processMouseHover(id);
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
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	int status = OS.XtAppPending (xtContext);
	if (status != 0) {
		if ((status & OS.XtIMTimer) != 0) {
			OS.XtAppProcessEvent (xtContext, OS.XtIMTimer);
			status = OS.XtAppPending (xtContext);
		}
		if ((status & OS.XtIMAlternateInput) != 0) {
			OS.XtAppProcessEvent (xtContext, OS.XtIMAlternateInput);
			status = OS.XtAppPending (xtContext);
		}
		if ((status & OS.XtIMXEvent) != 0) {
			OS.XtAppNextEvent (xtContext, xEvent);
			if (filterEvent (xEvent)) return false;
			OS.XtDispatchEvent (xEvent);
		}
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

	/* Destroy the hidden Override shell parent */
	if (shellHandle != 0) OS.XtDestroyWidget (shellHandle);
	shellHandle = 0;
	
	/* Dispose the caret callback */
	if (caretID != 0) OS.XtRemoveTimeOut (caretID);
	caretID = caretProc = 0;
	caretCallback.dispose ();
	caretCallback = null;
	
	/* Dispose the timer callback */
	if (timerIDs != null) {
		for (int i=0; i<timerIDs.length; i++) {
			if (timerIDs [i] != 0) OS.XtRemoveTimeOut (timerIDs [i]);
		}
	}
	timerIDs = null;
	timerList = null;
	timerProc = 0;
	timerCallback.dispose ();
	timerCallback = null;
	
	/* Dispose the mouse hover callback */
	if (mouseHoverID != 0) OS.XtRemoveTimeOut (mouseHoverID);
	mouseHoverID = mouseHoverProc = mouseHoverHandle = toolTipHandle = 0;
	mouseHoverCallback.dispose ();
	mouseHoverCallback = null;

	/* Dispose window and expose callbacks */
	windowCallback.dispose (); windowCallback = null;
	checkExposeCallback.dispose (); checkExposeCallback = null;
	checkExposeProc = 0;
	
	/* Dispose the wake callback, id and pipe */
	if (inputID != 0) OS.XtRemoveInput (inputID);
	wakeCallback.dispose (); wakeCallback = null;
	wakeProc = 0;
	OS.close (read_fd);
	OS.close (write_fd);
		
	/* Free the font lists */
	if (buttonFont != 0) OS.XmFontListFree (buttonFont);
	if (labelFont != 0) OS.XmFontListFree (labelFont);
	if (textFont != 0) OS.XmFontListFree (textFont);
	if (listFont != 0) OS.XmFontListFree (listFont);
	listFont = textFont = labelFont = buttonFont = 0;
	defaultFontList = defaultFont = 0;

	/* Free the parse table */
	OS.XtFree(tabPointer);
	OS.XtFree(crPointer);
	OS.XmParseTableFree(parseTable, parseTable.length);
	parseTable = null;
	
	/* Free the translations (no documentation describes how to do this) */
	//OS.XtFree (arrowTranslations);
	//OS.XtFree (tabTranslations);
	//OS.XtFree (dragTranslations);

	/* Release references */
	thread = null;
	xEvent = null;
	buttonBackground = buttonForeground = 0;
	defaultBackground = defaultForeground = 0;
	COLOR_WIDGET_DARK_SHADOW = COLOR_WIDGET_NORMAL_SHADOW = COLOR_WIDGET_LIGHT_SHADOW =
	COLOR_WIDGET_HIGHLIGHT_SHADOW = COLOR_WIDGET_BACKGROUND = COLOR_WIDGET_BORDER =
	COLOR_LIST_FOREGROUND = COLOR_LIST_BACKGROUND = COLOR_LIST_SELECTION = COLOR_LIST_SELECTION_TEXT = null;
	COLOR_INFO_BACKGROUND = null;
}
void releaseToolTipHandle (int handle) {
	if (mouseHoverHandle == handle) removeMouseHoverTimeOut ();
	if (toolTipHandle != 0) {
		int shellHandle = OS.XtParent(toolTipHandle);
		int shellParent = OS.XtParent(shellHandle);
		if (handle == shellParent) toolTipHandle = 0;
	}
}
void removeMouseHoverTimeOut () {
	if (mouseHoverID != 0) OS.XtRemoveTimeOut (mouseHoverID);
	mouseHoverID = mouseHoverHandle = 0;
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
	APP_NAME = name;
}
void setCurrentCaret (Caret caret) {
	if (caretID != 0) OS.XtRemoveTimeOut (caretID);
	caretID = 0;
	currentCaret = caret;
	if (currentCaret != null) {
		int blinkRate = currentCaret.blinkRate;
		int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
		caretID = OS.XtAppAddTimeOut (xtContext, blinkRate, caretProc, 0);
	}
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
void showToolTip (int handle, String toolTipText) {
	if (toolTipText == null || toolTipText.length() == 0 || toolTipHandle != 0) {
		 return;
	}
	
	/* Create the tooltip widgets */	
	int widgetClass = OS.OverrideShellWidgetClass ();
	int [] argList1 = {OS.XmNmwmDecorations, 0, OS.XmNborderWidth, 1};
	int shellHandle = OS.XtCreatePopupShell (null, widgetClass, handle, argList1, argList1.length / 2);
	toolTipHandle = OS.XmCreateLabel(shellHandle, null, null, 0);
	
	/* Set the tooltip foreground and background */
	Color infoForeground = getSystemColor(SWT.COLOR_INFO_FOREGROUND);
	Color infoBackground = getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	int foregroundPixel = (infoForeground == null) ? defaultForeground : infoForeground.handle.pixel;
	int backgroundPixel = (infoBackground == null) ? defaultBackground : infoBackground.handle.pixel;
	int [] argList2 = {OS.XmNforeground, foregroundPixel, OS.XmNbackground, backgroundPixel};
	OS.XtSetValues (toolTipHandle, argList2, argList2.length / 2);
	OS.XtManageChild (toolTipHandle);		
	
	/* Set the tooltip label string */
	byte [] buffer = Converter.wcsToMbcs (null, toolTipText, true);
	int xmString = OS.XmStringParseText (
		buffer,
		0,
		OS.XmFONTLIST_DEFAULT_TAG, 
		OS.XmCHARSET_TEXT, 
		null,
		0,
		0);
	int [] argList3 = {OS.XmNlabelString, xmString};
	OS.XtSetValues (toolTipHandle, argList3, argList3.length / 2);
	if (xmString != 0) OS.XmStringFree (xmString);	
		
	/*
	* Feature in X.  There is no way to query the size of a cursor.
	* The fix is to use the default cursor size which is 16x16.
	*/
	int xWindow = OS.XDefaultRootWindow (xDisplay);
	int [] rootX = new int [1], rootY = new int [1], unused = new int [1];
	OS.XQueryPointer (xDisplay, xWindow, unused, unused, rootX, rootY, unused, unused, unused);
	int x = rootX [0] + 16, y = rootY [0] + 16;
	
	/*
	* Ensure that the tooltip is on the screen.
	*/
	int screen = OS.XDefaultScreen (xDisplay);
	int width = OS.XDisplayWidth (xDisplay, screen);
	int height = OS.XDisplayHeight (xDisplay, screen);
	int [] argList4 = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (toolTipHandle, argList4, argList4.length / 2);
	x = Math.max (0, Math.min (x, width - argList4 [1]));
	y = Math.max (0, Math.min (y, height - argList4 [3]));
	OS.XtMoveWidget (shellHandle, x, y);
	OS.XtPopup (shellHandle, OS.XtGrabNone);
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
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	return OS.XtAppPeekEvent (xtContext, xEvent);
}
/**
 * Causes the <code>run()</code> method of the runnable to
 * be invoked by the user-interface thread at the next 
 * reasonable opportunity. The thread which calls this method
 * is suspended until the runnable completes.
 *
 * @param runnable code to run on the user-interface thread.
 *
 * @see #asyncExec
 */
public void syncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.syncExec (runnable);
}
int textWidth (String string, int fontList) {
	if (string.length () == 0) return 0;
	byte [] textBuffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringGenerate (textBuffer, null, OS.XmCHARSET_TEXT, _MOTIF_DEFAULT_LOCALE);
	int width = OS.XmStringWidth (fontList, xmString);
	OS.XmStringFree (xmString);
	return width;
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
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	int timerID = OS.XtAppAddTimeOut (xtContext, milliseconds, timerProc, index);
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
	XAnyEvent event = new XAnyEvent ();
	int mask = OS.ExposureMask | OS.ResizeRedirectMask |
		OS.StructureNotifyMask | OS.SubstructureNotifyMask |
		OS.SubstructureRedirectMask;
	OS.XSync (xDisplay, false); OS.XSync (xDisplay, false);
	while (OS.XCheckMaskEvent (xDisplay, mask, event)) OS.XtDispatchEvent (event);
}
/**
 * If the receiver's user-interface thread was <code>sleep</code>'ing, 
 * causes it to be awakened and start running again. Note that this
 * method may be called from any thread.
 *
 * @see #sleep
 */
public void wake () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	/* Write a single byte to the wake up pipe */
	while (OS.write (write_fd, wake_buffer, 1) != 1);
}
int wakeProc (int closure, int source, int id) {
	/* Read a single byte from the wake up pipe */
	while (OS.read (read_fd, wake_buffer, 1) != 1);
	return 0;
}
int windowProc (int handle, int clientData, int callData, int unused) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (clientData, callData);
}
String wrapText (String text, int fontList, int width) {
	String Lf = "\n";
	text = convertToLf (text);
	int length = text.length ();
	if (width <= 0 || length == 0 || length == 1) return text;
	StringBuffer result = new StringBuffer ();
	int lineStart = 0, lineEnd = 0;
	while (lineStart < length) {
		lineEnd = text.indexOf (Lf, lineStart);
		boolean noLf = lineEnd == -1;
		if (noLf) lineEnd = length;
		int nextStart = lineEnd + Lf.length ();
		while (lineEnd > lineStart + 1 && Character.isWhitespace (text.charAt (lineEnd - 1))) {
			lineEnd--;
		}
		int wordStart = lineStart, wordEnd = lineStart;
		int i = lineStart;
		while (i < lineEnd) {
			int lastStart = wordStart, lastEnd = wordEnd;
			wordStart = i;
			while (i < lineEnd && !Character.isWhitespace (text.charAt (i))) {
				i++;
			}
			wordEnd = i - 1;
			String line = text.substring (lineStart, wordEnd + 1);
			int lineWidth = textWidth (line, fontList);
			while (i < lineEnd && Character.isWhitespace (text.charAt (i))) {
				i++;
			}
			if (lineWidth > width) {
				if (lastStart == wordStart) {
					while (wordStart < wordEnd) {
						line = text.substring (lineStart, wordStart + 1);
						lineWidth = textWidth (line, fontList);
						if (lineWidth >= width) break;
						wordStart++;
					}
					if (wordStart == lastStart) wordStart++;
					lastEnd = wordStart - 1;
				}
				line = text.substring (lineStart, lastEnd + 1);
				result.append (line); result.append (Lf);
				i = wordStart; lineStart = wordStart; wordEnd = wordStart;
			}
		}
		if (lineStart < lineEnd) {
			result.append (text.substring (lineStart, lineEnd));
		}
		if (!noLf) {
			result.append (Lf);
		}
		lineStart = nextStart;
	}
	return result.toString ();
}

String convertToLf(String text) {
	char Cr = '\r';
	char Lf = '\n';
	int length = text.length ();
	if (length == 0) return text;
	
	/* Check for an LF or CR/LF.  Assume the rest of the string 
	 * is formated that way.  This will not work if the string 
	 * contains mixed delimiters. */
	int i = text.indexOf (Lf, 0);
	if (i == -1 || i == 0) return text;
	if (text.charAt (i - 1) != Cr) return text;

	/* The string is formatted with CR/LF.
	 * Create a new string with the LF line delimiter. */
	i = 0;
	StringBuffer result = new StringBuffer ();
	while (i < length) {
		int j = text.indexOf (Cr, i);
		if (j == -1) j = length;
		String s = text.substring (i, j);
		result.append (s);
		i = j + 2;
		result.append (Lf);
	}
	return result.toString ();
}
}

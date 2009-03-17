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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

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
 */
public class Display extends Device {
	
	/* Windows and Events */
	Event [] eventQueue;
	EventTable eventTable, filterTable;
	boolean disposing;

	/* Key event management */
	int [] deadKeyState = new int[1];
	int currentKeyboardUCHRdata;
	
	/* Sync/Async Widget Communication */
	Synchronizer synchronizer;
	Thread thread;
	boolean allowTimers, runAsyncMessages;

	GCData[] contexts;

	Caret currentCaret;
	
	boolean sendEvent;
	Control currentControl, trackingControl, tooltipControl;
	Widget tooltipTarget;
	
	NSMutableArray isPainting, needsDisplay, needsDisplayInRect;

	NSDictionary markedAttributes;
	
	/* Fonts */
	boolean smallFonts;
	NSFont buttonFont, popUpButtonFont, textFieldFont, secureTextFieldFont;
	NSFont searchFieldFont, comboBoxFont, sliderFont, scrollerFont;
	NSFont textViewFont, tableViewFont, outlineViewFont, datePickerFont;
	NSFont boxFont, tabViewFont, progressIndicatorFont;

	Shell [] modalShells;
	
	Menu menuBar;
	Menu[] menus, popups;

	NSApplication application;
	int /*long*/ applicationClass;
	NSImage dockImage;
	boolean isEmbedded;
	static boolean launched = false;
	
	Control focusControl;
	
	NSWindow screenWindow;
	NSAutoreleasePool pool;
	int loopCounter = 0;

	int[] screenID = new int[32];
	NSPoint[] screenCascade = new NSPoint[32];
	
	int /*long*/ runLoopObserver;
	Callback observerCallback;
	
	boolean lockCursor = true;
	int /*long*/ oldCursorSetProc;
	Callback cursorSetCallback;

	// the following Callbacks are never freed
	static Callback applicationDelegateCallback3;
	static Callback windowDelegateCallback2, windowDelegateCallback3, windowDelegateCallback4, windowDelegateCallback5;
	static Callback windowDelegateCallback6;
	static Callback dialogCallback3;
	static Callback applicationCallback2, applicationCallback3, applicationCallback6;
	static Callback fieldEditorCallback3, fieldEditorCallback4;
	
	/* Menus */
//	Menu menuBar;
//	static final int ID_TEMPORARY = 1000;
//	static final int ID_START = 1001;
	
	/* Display Shutdown */
	Runnable [] disposeList;

	/* System Tray */
	Tray tray;
	
	/* System Resources */
	Image errorImage, infoImage, warningImage;
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];
	
	/* Key Mappings. */
	static int [] [] KeyTable = {

		/* Keyboard and Mouse Masks */
		{58,	SWT.ALT},
		{56,	SWT.SHIFT},
		{59,	SWT.CONTROL},
		{55,	SWT.COMMAND},		
		{61,	SWT.ALT},
		{62,	SWT.CONTROL},
		{60,	SWT.SHIFT},
		{54,	SWT.COMMAND},

		/* Non-Numeric Keypad Keys */
		{126, SWT.ARROW_UP},
		{125, SWT.ARROW_DOWN},
		{123, SWT.ARROW_LEFT},
		{124, SWT.ARROW_RIGHT},
		{116, SWT.PAGE_UP},
		{121, SWT.PAGE_DOWN},
		{115, SWT.HOME},
		{119, SWT.END},
//		{??,	SWT.INSERT},

		/* Virtual and Ascii Keys */
		{51,	SWT.BS},
		{36,	SWT.CR},
		{117, 	SWT.DEL},
		{53,	SWT.ESC},
		{76,	SWT.LF},
		{48,	SWT.TAB},	
		
		/* Functions Keys */
		{122, SWT.F1},
		{120, SWT.F2},
		{99,	SWT.F3},
		{118, SWT.F4},
		{96,	SWT.F5},
		{97,	SWT.F6},
		{98,	SWT.F7},
		{100, SWT.F8},
		{101, SWT.F9},
		{109, SWT.F10},
		{103, SWT.F11},
		{111, SWT.F12},
		{105, SWT.F13},
		{107, SWT.F14},
		{113, SWT.F15},
		
		/* Numeric Keypad Keys */
		{67, SWT.KEYPAD_MULTIPLY},
		{69, SWT.KEYPAD_ADD},
		{76, SWT.KEYPAD_CR},
		{78, SWT.KEYPAD_SUBTRACT},
		{65, SWT.KEYPAD_DECIMAL},
		{75, SWT.KEYPAD_DIVIDE},
		{82, SWT.KEYPAD_0},
		{83, SWT.KEYPAD_1},
		{84, SWT.KEYPAD_2},
		{85, SWT.KEYPAD_3},
		{86, SWT.KEYPAD_4},
		{87, SWT.KEYPAD_5},
		{88, SWT.KEYPAD_6},
		{89, SWT.KEYPAD_7},
		{91, SWT.KEYPAD_8},
		{92, SWT.KEYPAD_9},
		{81, SWT.KEYPAD_EQUAL},

		/* Other keys */
		{57,	SWT.CAPS_LOCK},
		{71,	SWT.NUM_LOCK},
//		{??,	SWT.SCROLL_LOCK},
//		{??,	SWT.PAUSE},
//		{??,	SWT.BREAK},
//		{??,	SWT.PRINT_SCREEN},
		{114, SWT.HELP},
		
	};

	static String APP_NAME = "SWT";
	static final String ADD_WIDGET_KEY = "org.eclipse.swt.internal.addWidget"; //$NON-NLS-1$
	static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
	static final byte[] SWT_IMAGE = {'S', 'W', 'T', '_', 'I', 'M', 'A', 'G', 'E', '\0'};
	static final byte[] SWT_ROW = {'S', 'W', 'T', '_', 'R', 'O', 'W', '\0'};
	static final byte[] SWT_COLUMN = {'S', 'W', 'T', '_', 'C', 'O', 'L', 'U', 'M', 'N', '\0'};

	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [4];
				
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";
			
	/* Timer */
	Runnable timerList [];
	NSTimer nsTimers [];
	SWTWindowDelegate timerDelegate;
	SWTApplicationDelegate applicationDelegate;
	static final int DEFAULT_BUTTON_INTERVAL = 30;
	
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

static byte [] ascii (String name) {
	int length = name.length ();
	char [] chars = new char [length];
	name.getChars (0, length, chars, 0);
	byte [] buffer = new byte [length + 1];
	for (int i=0; i<length; i++) {
		buffer [i] = (byte) chars [i];
	}
	return buffer;
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

void addContext (GCData context) {
	if (contexts == null) contexts = new GCData [12];
	for (int i=0; i<contexts.length; i++) {
		if (contexts[i] != null && contexts [i] == context) {
			contexts [i] = context;
			return;
		}
	}
	GCData [] newContexts = new GCData [contexts.length + 12];
	newContexts [contexts.length] = context;
	System.arraycopy (contexts, 0, newContexts, 0, contexts.length);
	contexts = newContexts;
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

void addMenu (Menu menu) {
	if (menus == null) menus = new Menu [12];
	for (int i=0; i<menus.length; i++) {
		if (menus [i] == null) {
			menus [i] = menu;
			return;
		}
	}
	Menu [] newMenus = new Menu [menus.length + 12];
	newMenus [menus.length] = menu;
	System.arraycopy (menus, 0, newMenus, 0, menus.length);
	menus = newMenus;
}

void addPopup (Menu menu) {
	if (popups == null) popups = new Menu [4];
	int length = popups.length;
	for (int i=0; i<length; i++) {
		if (popups [i] == menu) return;
	}
	int index = 0;
	while (index < length) {
		if (popups [index] == null) break;
		index++;
	}
	if (index == length) {
		Menu [] newPopups = new Menu [length + 4];
		System.arraycopy (popups, 0, newPopups, 0, length);
		popups = newPopups;
	}
	popups [index] = menu;
}

void addWidget (NSObject view, Widget widget) {
	if (view == null) return;
	OS.object_setInstanceVariable (view.id, SWT_OBJECT, widget.jniRef);
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
	OS.NSBeep ();
}

void cascadeWindow (NSWindow window, NSScreen screen) {
	NSDictionary dictionary = screen.deviceDescription();
	int screenNumber = new NSNumber(dictionary.objectForKey(NSString.stringWith("NSScreenNumber")).id).intValue();
	int index = 0;
	while (screenID[index] != 0 && screenID[index] != screenNumber) index++;
	screenID[index] = screenNumber;
	NSPoint cascade = screenCascade[index];
	if (cascade == null) {
		NSRect frame = screen.frame();
		cascade = new NSPoint();
		cascade.x = frame.x;
		cascade.y = frame.y + frame.height;
	}
	screenCascade[index] = window.cascadeTopLeftFromPoint(cascade);
}

protected void checkDevice () {
	if (thread == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (thread != Thread.currentThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

void checkEnterExit (Control control, NSEvent nsEvent, boolean send) {
	if (control != currentControl) {
		if (currentControl != null && !currentControl.isDisposed()) {
			currentControl.sendMouseEvent (nsEvent, SWT.MouseExit, send);
		}
		if (control != null && control.isDisposed()) control = null;
		currentControl = control;
		if (control != null) {
			control.sendMouseEvent (nsEvent, SWT.MouseEnter, send);
		}
		setCursor (control);
	}
	timerExec (control != null && !control.isDisposed() ? getToolTipTime () : -1, hoverTimer);
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
	if (!Display.isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
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

static String convertToLf(String text) {
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

void clearModal (Shell shell) {
	if (modalShells == null) return;
	int index = 0, length = modalShells.length;
	while (index < length) {
		if (modalShells [index] == shell) break;
		if (modalShells [index] == null) return;
		index++;
	}
	if (index == length) return;
	System.arraycopy (modalShells, index + 1, modalShells, index, --length - index);
	modalShells [length] = null;
	if (index == 0 && modalShells [0] == null) modalShells = null;
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) shells [i].updateModal ();
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
	synchronizer = new Synchronizer (this);
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
	if (OS.VERSION < 0x1050) {
		System.out.println ("***WARNING: SWT requires MacOS X version " + 10 + "." + 5 + " or greater"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		System.out.println ("***WARNING: Detected: " + Integer.toHexString((OS.VERSION & 0xFF00) >> 8) + "." + Integer.toHexString((OS.VERSION & 0xF0) >> 4) + "." + Integer.toHexString(OS.VERSION & 0xF)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		error(SWT.ERROR_NOT_IMPLEMENTED);
	}

	NSThread nsthread = NSThread.currentThread();
	NSMutableDictionary dictionary = nsthread.threadDictionary();
	NSString key = NSString.stringWith("SWT_NSAutoreleasePool");
	NSNumber id = new NSNumber(dictionary.objectForKey(key));
	pool = new NSAutoreleasePool(id.integerValue());

	application = NSApplication.sharedApplication();

	/*
	 * TODO: If an NSApplication is already running we don't want to create another NSApplication.
	 * But if we don't we won't get mouse events, since we currently need to subclass NSApplication and intercept sendEvent to
	 * deliver mouse events correctly to widgets.   
	 */
	if (!application.isRunning()) {
		/*
		 * Feature in the Macintosh.  On OS 10.2, it is necessary
		 * to explicitly check in with the Process Manager and set
		 * the current process to be the front process in order for
		 * windows to come to the front by default.  The fix is call
		 * both GetCurrentProcess() and SetFrontProcess().
		 * 
		 * NOTE: It is not actually necessary to use the process
		 * serial number returned by GetCurrentProcess() in the
		 * call to SetFrontProcess() (ie. kCurrentProcess can be
		 * used) but both functions must be called in order for
		 * windows to come to the front.
		 */
		int [] psn = new int [2];
		if (OS.GetCurrentProcess (psn) == OS.noErr) {
			int pid = OS.getpid ();
			int /*long*/ ptr = OS.getenv (ascii ("APP_NAME_" + pid));
			if (ptr  == 0 && APP_NAME != null) {
				ptr = NSString.stringWith(APP_NAME).UTF8String();	
			}
			if (ptr != 0) OS.CPSSetProcessName (psn, ptr);
			OS.TransformProcessType (psn, OS.kProcessTransformToForegroundApplication);
			OS.SetFrontProcess (psn);
			ptr = OS.getenv (ascii ("APP_ICON_" + pid));
			if (ptr != 0) {
				NSString path = NSString.stringWithUTF8String (ptr);
				NSImage image = (NSImage) new NSImage().alloc();
				image = image.initByReferencingFile(path);
				dockImage = image;
				application.setApplicationIconImage(image);
			}
		}

		String className = "SWTApplication";
		int /*long*/ cls;
		if ((cls = OS.objc_lookUpClass (className)) == 0) {
			Class clazz = getClass();
			applicationCallback2 = new Callback(clazz, "applicationProc", 2);
			int /*long*/ proc2 = applicationCallback2.getAddress();
			if (proc2 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
			applicationCallback3 = new Callback(clazz, "applicationProc", 3);
			int /*long*/ proc3 = applicationCallback3.getAddress();
			if (proc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
			applicationCallback6 = new Callback(clazz, "applicationProc", 6);
			int /*long*/ proc6 = applicationCallback6.getAddress();
			if (proc6 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
			cls = OS.objc_allocateClassPair(OS.class_NSApplication, className, 0);
			OS.class_addMethod(cls, OS.sel_sendEvent_, proc3, "@:@");
			OS.class_addMethod(cls, OS.sel_nextEventMatchingMask_untilDate_inMode_dequeue_, proc6, "@:i@@B");
			OS.class_addMethod(cls, OS.sel_isRunning, proc2, "@:");
			OS.objc_registerClassPair(cls);
		}
		applicationClass = OS.object_setClass(application.id, cls);
	} else {
		isEmbedded = true;
	}
}

int /*long*/ cursorSetProc (int /*long*/ id, int /*long*/ sel) {
	if (lockCursor) {
		if (currentControl != null) {
			Cursor cursor = currentControl.findCursor ();
			if (cursor != null && cursor.handle.id != id) return 0;
		}
	}
	OS.call (oldCursorSetProc, id, sel);
	return 0;
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

	Runtime.getRuntime().addShutdownHook(new Thread() {
		// Any top-level autorelease pool cannot be destroyed until the absolute end of the application.
		// terminate is that absolute end of the app; it will also clean up any remaining pools.
		public void run() {
			NSApplication.sharedApplication().terminate(null);
		}
	});

	application = null;
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

void error (int code) {
	SWT.error(code);
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
public Widget findWidget (int /*long*/ handle) {
	checkDevice ();
	return getWidget (handle);
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
public Widget findWidget (int /*long*/ handle, int id) {
	checkDevice ();
	return getWidget (handle);
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
	NSArray windows = application.windows();
	int count = (int)/*64*/windows.count();
	for (int i = 0; i < count; i++) {
		NSWindow win = new NSWindow(windows.objectAtIndex(i));
		if (win.isKeyWindow()) {
			Widget widget = getWidget(win.contentView());
			if (widget instanceof Shell) {
				return (Shell)widget;
			}
		}
	}
	return null;
}

/**
 * Returns a rectangle describing the receiver's size and location. Note that
 * on multi-monitor systems the origin can be negative.
 *
 * @return the bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkDevice ();
	NSArray screens = NSScreen.screens();
	return getBounds (screens);
}

Rectangle getBounds (NSArray screens) {
	NSRect primaryFrame = new NSScreen(screens.objectAtIndex(0)).frame();
	float /*double*/ minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
	float /*double*/ minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;
	int /*long*/ count = screens.count();
	for (int i = 0; i < count; i++) {
		NSScreen screen = new NSScreen(screens.objectAtIndex(i));
		NSRect frame = screen.frame();
		float /*double*/ x1 = frame.x, x2 = frame.x + frame.width;
		float /*double*/ y1 = primaryFrame.height - frame.y, y2 = primaryFrame.height - (frame.y + frame.height);
		if (x1 < minX) minX = x1;
		if (x2 < minX) minX = x2;
		if (x1 > maxX) maxX = x1;
		if (x2 > maxX) maxX = x2;
		if (y1 < minY) minY = y1;
		if (y2 < minY) minY = y2;
		if (y1 > maxY) maxY = y1;
		if (y2 > maxY) maxY = y2;
	}
	return new Rectangle ((int)minX, (int)minY, (int)(maxX - minX), (int)(maxY - minY));
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

int getCaretBlinkTime () {
//	checkDevice ();
	return 560;
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 */
public Rectangle getClientArea () {
	checkDevice ();
	NSArray screens = NSScreen.screens();
	if (screens.count() != 1) return getBounds (screens);
	NSScreen screen = new NSScreen(screens.objectAtIndex(0));
	NSRect frame = screen.frame();
	NSRect visibleFrame = screen.visibleFrame();
	float /*double*/ y = frame.height - (visibleFrame.y + visibleFrame.height);
	return new Rectangle((int)visibleFrame.x, (int)y, (int)visibleFrame.width, (int)visibleFrame.height);
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
	checkDevice();
	return findControl(false);
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
	NSPoint location = NSEvent.mouseLocation();
	NSRect primaryFrame = getPrimaryFrame();
	return new Point ((int) location.x, (int) (primaryFrame.height - location.y));
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
	return new Point [] {new Point (16, 16)};
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
	return OS.GetDblTime () * 1000 / 60; 
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
	NSWindow window = application.keyWindow();
	return GetFocusControl(window);
}

static Control GetFocusControl(NSWindow window) {
	if (window != null) {
		NSResponder responder = window.firstResponder();
		if (responder != null && !responder.respondsToSelector(OS.sel_superview)) {
			return null;
		}
		NSView view = new NSView(responder.id);
		if (view != null) {
			do {
				Widget widget = GetWidget (view.id);
				if (widget instanceof Control) {
					return (Control)widget;
				}
				view = view.superview();
			} while (view != null);
		}
	}
	return null;
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
	return new Point [] { 
		new Point (16, 16), new Point (32, 32), 
		new Point (64, 64), new Point (128, 128)};	
}

int getLastEventTime () {
	NSEvent event = application.currentEvent();
	return event != null ? (int)(event.timestamp() * 1000) : 0;
}

Menu [] getMenus (Decorations shell) {
	if (menus == null) return new Menu [0];
	int count = 0;
	for (int i = 0; i < menus.length; i++) {
		Menu menu = menus[i];
		if (menu != null && menu.parent == shell) count++;
	}
	int index = 0;
	Menu[] result = new Menu[count];
	for (int i = 0; i < menus.length; i++) {
		Menu menu = menus[i];
		if (menu != null && menu.parent == shell) {
			result[index++] = menu;
		}
	}
	return result;
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
	NSArray screens = NSScreen.screens();
	NSRect primaryFrame = new NSScreen(screens.objectAtIndex(0)).frame();
	int count = (int)/*64*/screens.count();
	Monitor [] monitors = new Monitor [count];
	for (int i=0; i<count; i++) {
		Monitor monitor = new Monitor ();
		NSScreen screen = new NSScreen(screens.objectAtIndex(i));
		NSRect frame = screen.frame();
		monitor.x = (int)frame.x;
		monitor.y = (int)(primaryFrame.height - (frame.y + frame.height));
		monitor.width = (int)frame.width;
		monitor.height = (int)frame.height;
		NSRect visibleFrame = screen.visibleFrame();
		monitor.clientX = (int)visibleFrame.x;
		monitor.clientY = (int)(primaryFrame.height - (visibleFrame.y + visibleFrame.height));
		monitor.clientWidth = (int)visibleFrame.width;
		monitor.clientHeight = (int)visibleFrame.height;
		monitors [i] = monitor;
	}
	return monitors;
}

NSRect getPrimaryFrame () {
	NSArray screens = NSScreen.screens();
	return new NSScreen(screens.objectAtIndex(0)).frame();
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
	Monitor monitor = new Monitor ();
	NSArray screens = NSScreen.screens();
	NSScreen screen = new NSScreen(screens.objectAtIndex(0));
	NSRect frame = screen.frame();
	monitor.x = (int)frame.x;
	monitor.y = (int)(frame.height - (frame.y + frame.height));
	monitor.width = (int)frame.width;
	monitor.height = (int)frame.height;
	NSRect visibleFrame = screen.visibleFrame();
	monitor.clientX = (int)visibleFrame.x;
	monitor.clientY = (int)(frame.height - (visibleFrame.y + visibleFrame.height));
	monitor.clientWidth = (int)visibleFrame.width;
	monitor.clientHeight = (int)visibleFrame.height;
	return monitor;
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
	NSArray windows = application.windows();
	int index = 0;
	Shell [] result = new Shell [(int)/*64*/windows.count()];
	for (int i = 0; i < result.length; i++) {
		NSWindow window = new NSWindow(windows.objectAtIndex(i));
		Widget widget = getWidget(window.contentView());
		if (widget instanceof Shell) {
			result[index++] = (Shell)widget;
		}
	}
	if (index == result.length) return result;
	Shell [] newResult = new Shell [index];
	System.arraycopy (result, 0, newResult, 0, index);
	return newResult;
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
	Color color = getWidgetColor (id);
	if (color != null) return color;
	return super.getSystemColor (id);	
}

Color getWidgetColor (int id) {
	NSColor color = null;
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: color = NSColor.blackColor (); break;
		case SWT.COLOR_INFO_BACKGROUND: return Color.cocoa_new (this, new float /*double*/ [] {0xFF / 255f, 0xFF / 255f, 0xE1 / 255f, 1});
		case SWT.COLOR_TITLE_FOREGROUND: color = NSColor.windowFrameTextColor(); break;
		case SWT.COLOR_TITLE_BACKGROUND: color = NSColor.alternateSelectedControlColor(); break;
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT: color = NSColor.selectedControlColor(); break;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND: color = NSColor.disabledControlTextColor();  break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND: color = NSColor.secondarySelectedControlColor(); break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT: color = NSColor.secondarySelectedControlColor(); break;
		case SWT.COLOR_WIDGET_DARK_SHADOW: color = NSColor.controlDarkShadowColor(); break;
		case SWT.COLOR_WIDGET_NORMAL_SHADOW: color = NSColor.controlShadowColor(); break;
		case SWT.COLOR_WIDGET_LIGHT_SHADOW: color = NSColor.controlHighlightColor(); break;
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW: color = NSColor.controlLightHighlightColor(); break;
		case SWT.COLOR_WIDGET_BACKGROUND: color = NSColor.controlHighlightColor(); break;
		case SWT.COLOR_WIDGET_FOREGROUND: color = NSColor.controlTextColor(); break;
		case SWT.COLOR_WIDGET_BORDER: color = NSColor.blackColor (); break;
		case SWT.COLOR_LIST_FOREGROUND: color = NSColor.textColor(); break;
		case SWT.COLOR_LIST_BACKGROUND: color = NSColor.textBackgroundColor(); break;
		case SWT.COLOR_LIST_SELECTION_TEXT: color = NSColor.selectedTextColor(); break;
		case SWT.COLOR_LIST_SELECTION: color = NSColor.selectedTextBackgroundColor(); break;
	}
	if (color == null) return null;
	color = color.colorUsingColorSpace(NSColorSpace.deviceRGBColorSpace());
	if (color == null) return null;
	float /*double*/[] components = new float /*double*/[(int)/*64*/color.numberOfComponents()];
	color.getComponents(components);	
	return Color.cocoa_new (this, new float /*double*/ []{components[0], components[1], components[2], components[3]});
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
	switch(id) {
		case SWT.ICON_ERROR: {	
			if (errorImage != null) return errorImage;
			NSImage nsImage = NSWorkspace.sharedWorkspace ().iconForFileType (new NSString (OS.NSFileTypeForHFSTypeCode (OS.kAlertStopIcon)));
			if (nsImage == null) return null;
			nsImage.retain ();
			return errorImage = Image.cocoa_new (this, SWT.ICON, nsImage);
		}
		case SWT.ICON_INFORMATION:
		case SWT.ICON_QUESTION:
		case SWT.ICON_WORKING: {
			if (infoImage != null) return infoImage;
			NSImage nsImage = NSWorkspace.sharedWorkspace ().iconForFileType (new NSString (OS.NSFileTypeForHFSTypeCode (OS.kAlertNoteIcon)));
			if (nsImage == null) return null;
			nsImage.retain ();
			return infoImage = Image.cocoa_new (this, SWT.ICON, nsImage);
		}
		case SWT.ICON_WARNING: {
			if (warningImage != null) return warningImage;
			NSImage nsImage = NSWorkspace.sharedWorkspace ().iconForFileType (new NSString (OS.NSFileTypeForHFSTypeCode (OS.kAlertCautionIcon)));
			if (nsImage == null) return null;
			nsImage.retain ();
			return warningImage = Image.cocoa_new (this, SWT.ICON, nsImage);
		}
	}
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
	if (tray != null) return tray;
	return tray = new Tray (this, SWT.NONE);
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

int getToolTipTime () {
	checkDevice ();
	//TODO get OS value (NSTooltipManager?)
	return 560;
}

Widget getWidget (int /*long*/ id) {
	return GetWidget (id);
}

static Widget GetWidget (int /*long*/ id) {
	if (id == 0) return null;
	int /*long*/ [] jniRef = new int /*long*/ [1];
	OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
	if (jniRef[0] == 0) return null;
	return (Widget)OS.JNIGetObject(jniRef[0]);
}

Widget getWidget (NSView view) {
	if (view == null) return null;
	return getWidget(view.id);
}

boolean hasDefaultButton () {
	NSArray windows = application.windows();
	int /*long*/ count = windows.count();
	for (int i = 0; i < count; i++) {
		NSWindow window  = new NSWindow(windows.objectAtIndex(i));
		if (window.defaultButtonCell() != null) {
			return true;
		}
	}
	return false;
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
	initClasses ();
	initFonts ();
	
	if (!isEmbedded) {
		initApplicationDelegate();
		
		/*
		 * Feature in Cocoa:  NSApplication.finishLaunching() adds an apple menu to the menu bar that isn't accessible via NSMenu.
		 * If Display objects are created and disposed of multiple times in a single process, another apple menu is added to the menu bar.
		 * It must be called or the dock icon will continue to bounce. So, it should only be called once per process, not just once per
		 * creation of a Display.  Use a static so creation of additional Display objects won't affect the menu bar. 
		 */
		if (!Display.launched) {
			application.finishLaunching();
			Display.launched = true;
		}
	}
	
	observerCallback = new Callback (this, "observerProc", 3); //$NON-NLS-1$
	int /*long*/ observerProc = observerCallback.getAddress ();
	if (observerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	int activities = OS.kCFRunLoopBeforeWaiting;
	runLoopObserver = OS.CFRunLoopObserverCreate (0, activities, true, 0, observerProc, 0);
	if (runLoopObserver == 0) error (SWT.ERROR_NO_HANDLES);
	OS.CFRunLoopAddObserver (OS.CFRunLoopGetCurrent (), runLoopObserver, OS.kCFRunLoopCommonModes ());
	
	cursorSetCallback = new Callback(this, "cursorSetProc", 2);
	int /*long*/ cursorSetProc = cursorSetCallback.getAddress();
	if (cursorSetProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	int /*long*/ method = OS.class_getInstanceMethod(OS.class_NSCursor, OS.sel_set);
	if (method != 0) oldCursorSetProc = OS.method_setImplementation(method, cursorSetProc);
		
	timerDelegate = (SWTWindowDelegate)new SWTWindowDelegate().alloc().init();
	
	NSTextView textView = (NSTextView)new NSTextView().alloc();
	textView.init ();
	markedAttributes = textView.markedTextAttributes ();
	markedAttributes.retain ();
	textView.release ();
	
	isPainting = (NSMutableArray)new NSMutableArray().alloc();
	isPainting = isPainting.initWithCapacity(12);
}

void initApplicationDelegate() {
	String className = "SWTApplicationDelegate";
	if (OS.objc_lookUpClass (className) == 0) {
		Class clazz = getClass ();
		applicationDelegateCallback3 = new Callback(clazz, "applicationDelegateProc", 3);
		int /*long*/ appProc3 = applicationDelegateCallback3.getAddress();
		if (appProc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		int /*long*/ cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
		OS.class_addMethod(cls, OS.sel_applicationWillFinishLaunching_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_terminate_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_quitRequested_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_orderFrontStandardAboutPanel_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_hideOtherApplications_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_hide_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_unhideAllApplications_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_applicationDidBecomeActive_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_applicationDidResignActive_, appProc3, "@:@");
		OS.objc_registerClassPair(cls);
	}	
	applicationDelegate = (SWTApplicationDelegate)new SWTApplicationDelegate().alloc().init();
	application.setDelegate(applicationDelegate);
}

void addEventMethods (int /*long*/ cls, int /*long*/ proc2, int /*long*/ proc3, int /*long*/ drawRectProc, int /*long*/ hitTestProc, int /*long*/ needsDisplayInRectProc) {
	if (proc3 != 0) {
		OS.class_addMethod(cls, OS.sel_mouseDown_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_mouseUp_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_scrollWheel_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_rightMouseDown_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_rightMouseUp_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_rightMouseDragged_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_otherMouseDown_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_otherMouseUp_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_otherMouseDragged_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_mouseDragged_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_mouseMoved_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_mouseEntered_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_mouseExited_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_menuForEvent_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_keyDown_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_keyUp_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_flagsChanged_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_cursorUpdate_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_setNeedsDisplay_, proc3, "@:B");
		OS.class_addMethod(cls, OS.sel_shouldDelayWindowOrderingForEvent_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_acceptsFirstMouse_, proc3, "@:@");
	}
	if (proc2 != 0) {
		OS.class_addMethod(cls, OS.sel_resignFirstResponder, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_becomeFirstResponder, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_resetCursorRects, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_updateTrackingAreas, proc2, "@:");
	}
	if (needsDisplayInRectProc != 0) {
		OS.class_addMethod(cls, OS.sel_setNeedsDisplayInRect_, needsDisplayInRectProc, "@:{NSRect}");
	}
	if (drawRectProc != 0) {
		OS.class_addMethod(cls, OS.sel_drawRect_, drawRectProc, "@:{NSRect}");
	}
	if (hitTestProc != 0) {
		OS.class_addMethod(cls, OS.sel_hitTest_, hitTestProc, "@:{NSPoint}");		
	}
}

void addFrameMethods(int /*long*/ cls, int /*long*/ setFrameOriginProc, int /*long*/ setFrameSizeProc) {
	OS.class_addMethod(cls, OS.sel_setFrameOrigin_, setFrameOriginProc, "@:{NSPoint}");	
	OS.class_addMethod(cls, OS.sel_setFrameSize_, setFrameSizeProc, "@:{NSSize}");	
}

void addAccessibilityMethods(int /*long*/ cls, int /*long*/ proc2, int /*long*/ proc3, int /*long*/ proc4, int /*long*/ accessibilityHitTestProc) {
	OS.class_addMethod(cls, OS.sel_accessibilityActionNames, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_accessibilityAttributeNames, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_accessibilityParameterizedAttributeNames, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_accessibilityFocusedUIElement, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_accessibilityIsIgnored, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_accessibilityAttributeValue_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_accessibilityIsAttributeSettable_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_accessibilityHitTest_, accessibilityHitTestProc, "@:{NSPoint}");
	OS.class_addMethod(cls, OS.sel_accessibilityAttributeValue_forParameter_, proc4, "@:@@");	
	OS.class_addMethod(cls, OS.sel_accessibilityPerformAction_, proc3, "@:@");	
	OS.class_addMethod(cls, OS.sel_accessibilityActionDescription_, proc3, "@:@");	
}

int /*long*/ registerCellSubclass(int /*long*/ cellClass, int size, int align, byte[] types) {
	String cellClassName = OS.class_getName(cellClass);
	int /*long*/ cls = OS.objc_allocateClassPair(cellClass, "SWTAccessible" + cellClassName, 0);	
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.objc_registerClassPair(cls);
	return cls;
}

void initClasses () {
	if (OS.objc_lookUpClass ("SWTView") != 0) return;
	
	Class clazz = getClass ();
	dialogCallback3 = new Callback(clazz, "dialogProc", 3);
	int /*long*/ dialogProc3 = dialogCallback3.getAddress();
	if (dialogProc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);	
	windowDelegateCallback3 = new Callback(clazz, "windowDelegateProc", 3);
	int /*long*/ proc3 = windowDelegateCallback3.getAddress();
	if (proc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowDelegateCallback2 = new Callback(clazz, "windowDelegateProc", 2);
	int /*long*/ proc2 = windowDelegateCallback2.getAddress();
	if (proc2 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowDelegateCallback4 = new Callback(clazz, "windowDelegateProc", 4);
	int /*long*/ proc4 = windowDelegateCallback4.getAddress();
	if (proc4 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowDelegateCallback5 = new Callback(clazz, "windowDelegateProc", 5);
	int /*long*/ proc5 = windowDelegateCallback5.getAddress();
	if (proc5 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowDelegateCallback6 = new Callback(clazz, "windowDelegateProc", 6);
	int /*long*/ proc6 = windowDelegateCallback6.getAddress();
	if (proc6 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	fieldEditorCallback3 = new Callback(clazz, "fieldEditorProc", 3);
	int /*long*/ fieldEditorProc3 = fieldEditorCallback3.getAddress();
	if (fieldEditorProc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	fieldEditorCallback4 = new Callback(clazz, "fieldEditorProc", 4);
	int /*long*/ fieldEditorProc4 = fieldEditorCallback4.getAddress();
	if (fieldEditorProc4 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	int /*long*/ isFlippedProc = OS.isFlipped_CALLBACK();
	int /*long*/ drawRectProc = OS.drawRect_CALLBACK(proc3);
	int /*long*/ drawInteriorWithFrameInViewProc = OS.drawInteriorWithFrame_inView_CALLBACK (proc4);
	int /*long*/ drawWithFrameInViewProc = OS.drawWithFrame_inView_CALLBACK (proc4);
	int /*long*/ imageRectForBoundsProc = OS.imageRectForBounds_CALLBACK (proc3);
	int /*long*/ titleRectForBoundsProc = OS.titleRectForBounds_CALLBACK (proc3);
	int /*long*/ hitTestForEvent_inRect_ofViewProc = OS.hitTestForEvent_inRect_ofView_CALLBACK (proc5);
	int /*long*/ cellSizeProc = OS.cellSize_CALLBACK (proc2);
	int /*long*/ drawImageWithFrameInViewProc = OS.drawImage_withFrame_inView_CALLBACK (proc5);
	int /*long*/ setFrameOriginProc = OS.setFrameOrigin_CALLBACK(proc3);
	int /*long*/ setFrameSizeProc = OS.setFrameSize_CALLBACK(proc3);
	int /*long*/ hitTestProc = OS.hitTest_CALLBACK(proc3);
	int /*long*/ markedRangeProc = OS.markedRange_CALLBACK(proc2);
	int /*long*/ selectedRangeProc = OS.selectedRange_CALLBACK(proc2);
	int /*long*/ highlightSelectionInClipRectProc = OS.highlightSelectionInClipRect_CALLBACK(proc3);
	int /*long*/ setMarkedText_selectedRangeProc = OS.setMarkedText_selectedRange_CALLBACK(proc4);
	int /*long*/ attributedSubstringFromRangeProc = OS.attributedSubstringFromRange_CALLBACK(proc3);
	int /*long*/ characterIndexForPointProc = OS.characterIndexForPoint_CALLBACK(proc3);
	int /*long*/ firstRectForCharacterRangeProc = OS.firstRectForCharacterRange_CALLBACK(proc3);	
	int /*long*/ textWillChangeSelectionProc = OS.textView_willChangeSelectionFromCharacterRange_toCharacterRange_CALLBACK(proc5);
	int /*long*/ accessibilityHitTestProc = OS.accessibilityHitTest_CALLBACK(proc3);
	int /*long*/ shouldChangeTextInRange_replacementString_Proc = OS.shouldChangeTextInRange_replacementString_CALLBACK(fieldEditorProc4);
	int /*long*/ shouldChangeTextInRange_replacementString_fieldEditorProc = shouldChangeTextInRange_replacementString_Proc;
	int /*long*/ view_stringForToolTip_point_userDataProc = OS.view_stringForToolTip_point_userData_CALLBACK(proc6);
	int /*long*/ setNeedsDisplayInRectProc = OS.setNeedsDisplayInRect_CALLBACK(proc3);
	
	byte[] types = {'*','\0'};
	int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;

	String className = "SWTWindowDelegate";
	int /*long*/ cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_windowDidResize_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidMove_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowShouldClose_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowWillClose_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidResignKey_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidBecomeKey_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_timerProc_, proc3, "@:@");
	OS.objc_registerClassPair(cls);
	
	className = "SWTPanelDelegate";
	cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_windowWillClose_, dialogProc3, "@:@");
	OS.class_addMethod(cls, OS.sel_changeColor_, dialogProc3, "@:@");
	OS.class_addMethod(cls, OS.sel_changeFont_, dialogProc3, "@:@");
	OS.objc_registerClassPair(cls);
	
	className = "SWTMenu";
	cls = OS.objc_allocateClassPair(OS.class_NSMenu, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_menuWillOpen_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_menuDidClose_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_menu_willHighlightItem_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_menuNeedsUpdate_, proc3, "@:@");
	OS.objc_registerClassPair(cls);

	className = "SWTView";
	cls = OS.objc_allocateClassPair(OS.class_NSView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_isFlipped, isFlippedProc, "@:");
	OS.class_addMethod(cls, OS.sel_acceptsFirstResponder, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_isOpaque, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	className = "SWTCanvasView";
	cls = OS.objc_allocateClassPair(OS.class_NSView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	
	//NSTextInput protocol
	OS.class_addProtocol(cls, OS.objc_getProtocol("NSTextInput"));
	OS.class_addMethod(cls, OS.sel_hasMarkedText, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_markedRange, markedRangeProc, "@:");
	OS.class_addMethod(cls, OS.sel_selectedRange, selectedRangeProc, "@:");
	OS.class_addMethod(cls, OS.sel_setMarkedText_selectedRange_, setMarkedText_selectedRangeProc, "@:@{NSRange}");
	OS.class_addMethod(cls, OS.sel_unmarkText, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_validAttributesForMarkedText, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_attributedSubstringFromRange_, attributedSubstringFromRangeProc, "@:{NSRange}");
	OS.class_addMethod(cls, OS.sel_insertText_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_characterIndexForPoint_, characterIndexForPointProc, "@:{NSPoint}");
	OS.class_addMethod(cls, OS.sel_firstRectForCharacterRange_, firstRectForCharacterRangeProc, "@:{NSRange}");
	OS.class_addMethod(cls, OS.sel_doCommandBySelector_, proc3, "@::");
	OS.class_addMethod(cls, OS.sel_nextValidKeyView, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_previousValidKeyView, proc2, "@:");
	
	OS.class_addMethod(cls, OS.sel_isFlipped, isFlippedProc, "@:");
	OS.class_addMethod(cls, OS.sel_acceptsFirstResponder, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_isOpaque, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTScrollView";
	cls = OS.objc_allocateClassPair(OS.class_NSScrollView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendVerticalSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_sendHorizontalSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_pageDown_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_pageUp_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_reflectScrolledClipView_, proc3, "@:@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTButton";
	cls = OS.objc_allocateClassPair(OS.class_NSButton, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSButton.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	OS.class_addMethod(cls, OS.sel_nextState, proc2, "@:");
	NSButton.setCellClass(cls);

	className = "SWTTableView";
	cls = OS.objc_allocateClassPair(OS.class_NSTableView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_highlightSelectionInClipRect_, highlightSelectionInClipRectProc, "@:{NSRect}");
	OS.class_addMethod(cls, OS.sel_sendDoubleSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_numberOfRowsInTableView_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableView_objectValueForTableColumn_row_, proc5, "@:@:@:@");
	OS.class_addMethod(cls, OS.sel_tableView_shouldEditTableColumn_row_, proc5, "@:@:@:@");
	OS.class_addMethod(cls, OS.sel_tableViewSelectionDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableView_willDisplayCell_forTableColumn_row_, proc6, "@:@@@i");
	OS.class_addMethod(cls, OS.sel_tableView_setObjectValue_forTableColumn_row_, proc6, "@:@@@i");
	OS.class_addMethod(cls, OS.sel_tableViewColumnDidMove_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableViewColumnDidResize_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableView_didClickTableColumn_, proc4, "@:@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	className = "SWTTableHeaderCell";
	cls = OS.objc_allocateClassPair (OS.class_NSTableHeaderCell, className, 0);
	OS.class_addIvar (cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod (cls, OS.sel_drawInteriorWithFrame_inView_, drawInteriorWithFrameInViewProc, "@:{NSRect}@");
	OS.objc_registerClassPair (cls);

	className = "SWTButtonCell";
	cls = OS.objc_allocateClassPair (OS.class_NSButtonCell, className, 0);
	OS.class_addIvar (cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod (cls, OS.sel_drawImage_withFrame_inView_, drawImageWithFrameInViewProc, "@:@{NSFrame}@");
	OS.objc_registerClassPair (cls);

	className = "SWTImageTextCell";
	cls = OS.objc_allocateClassPair (OS.class_NSTextFieldCell, className, 0);
	OS.class_addIvar (cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addIvar (cls, SWT_IMAGE, size, (byte)align, types);
	OS.class_addIvar (cls, SWT_ROW, size, (byte)align, types);
	OS.class_addIvar (cls, SWT_COLUMN, size, (byte)align, types);
	OS.class_addMethod (cls, OS.sel_drawInteriorWithFrame_inView_, drawInteriorWithFrameInViewProc, "@:{NSRect}@");
	OS.class_addMethod (cls, OS.sel_drawWithFrame_inView_, drawWithFrameInViewProc, "@:{NSRect}@");
	OS.class_addMethod (cls, OS.sel_imageRectForBounds_, imageRectForBoundsProc, "@:{NSRect}");
	OS.class_addMethod (cls, OS.sel_titleRectForBounds_, titleRectForBoundsProc, "@:{NSRect}");
	OS.class_addMethod (cls, OS.sel_hitTestForEvent_inRect_ofView_, hitTestForEvent_inRect_ofViewProc, "@:@{NSRect}@");
	OS.class_addMethod (cls, OS.sel_cellSize, cellSizeProc, "@:");
	OS.class_addMethod (cls, OS.sel_image, proc2, "@:");
	OS.class_addMethod (cls, OS.sel_setImage_, proc3, "@:@");
	OS.objc_registerClassPair (cls);

	className = "SWTTableHeaderView";
	cls = OS.objc_allocateClassPair(OS.class_NSTableHeaderView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_mouseDown_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_resetCursorRects, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_updateTrackingAreas, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_menuForEvent_, proc3, "@:@");
	//TODO hitTestProc and drawRectProc should be set Control.setRegion()? 
	OS.objc_registerClassPair(cls);

	className = "SWTOutlineView";
	cls = OS.objc_allocateClassPair(OS.class_NSOutlineView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_highlightSelectionInClipRect_, highlightSelectionInClipRectProc, "@:{NSRect}");
	OS.class_addMethod(cls, OS.sel_sendDoubleSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_outlineViewSelectionDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineViewItemDidExpand_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineView_shouldCollapseItem_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_outlineView_shouldExpandItem_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_outlineView_child_ofItem_, proc5, "@:@i@");
	OS.class_addMethod(cls, OS.sel_outlineView_isItemExpandable_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_outlineView_numberOfChildrenOfItem_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_outlineView_objectValueForTableColumn_byItem_, proc5, "@:@@@");
	OS.class_addMethod(cls, OS.sel_outlineView_willDisplayCell_forTableColumn_item_, proc6, "@:@@@@");
	OS.class_addMethod(cls, OS.sel_outlineView_setObjectValue_forTableColumn_byItem_, proc6, "@:@@@@");
	OS.class_addMethod(cls, OS.sel_outlineViewColumnDidMove_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineViewColumnDidResize_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineView_didClickTableColumn_, proc4, "@:@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	className = "SWTTreeItem";
	cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.objc_registerClassPair(cls);

	className = "SWTTabView";
	cls = OS.objc_allocateClassPair(OS.class_NSTabView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_tabView_willSelectTabViewItem_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_tabView_didSelectTabViewItem_, proc4, "@:@@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTBox";
	cls = OS.objc_allocateClassPair(OS.class_NSBox, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTProgressIndicator";
	cls = OS.objc_allocateClassPair(OS.class_NSProgressIndicator, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_viewDidMoveToWindow, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls); 

	className = "SWTSlider";
	cls = OS.objc_allocateClassPair(OS.class_NSSlider, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls); 
	
	cls = registerCellSubclass(NSSlider.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSSlider.setCellClass(cls);

	className = "SWTPopUpButton";
	cls = OS.objc_allocateClassPair(OS.class_NSPopUpButton, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSPopUpButton.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSPopUpButton.setCellClass(cls);

	className = "SWTComboBox";
	cls = OS.objc_allocateClassPair(OS.class_NSComboBox, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_comboBoxSelectionDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_textDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textViewDidChangeSelection_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_, textWillChangeSelectionProc, "@:@{NSRange}{NSRange}");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSComboBox.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSComboBox.setCellClass(cls);

	className = "SWTDatePicker";
	cls = OS.objc_allocateClassPair(OS.class_NSDatePicker, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_isFlipped, isFlippedProc, "@:");
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	className = "SWTImageView";
	cls = OS.objc_allocateClassPair(OS.class_NSImageView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_isFlipped, isFlippedProc, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	cls = registerCellSubclass(NSImageView.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSImageView.setCellClass(cls);

	className = "SWTStepper";
	cls = OS.objc_allocateClassPair(OS.class_NSStepper, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	cls = registerCellSubclass(NSStepper.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSStepper.setCellClass(cls);

	className = "SWTScroller";
	cls = OS.objc_allocateClassPair(OS.class_NSScroller, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTMenuItem";
	cls = OS.objc_allocateClassPair(OS.class_NSMenuItem, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	OS.objc_registerClassPair(cls);

	className = "SWTTextView";
	cls = OS.objc_allocateClassPair(OS.class_NSTextView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.class_addMethod(cls, OS.sel_insertText_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_doCommandBySelector_, proc3, "@::");
	OS.class_addMethod(cls, OS.sel_textDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textView_clickedOnLink_atIndex_, proc5, "@:@@@");
	OS.class_addMethod(cls, OS.sel_dragSelectionWithEvent_offset_slideBack_, proc5, "@:@@@");
	OS.class_addMethod(cls, OS.sel_shouldChangeTextInRange_replacementString_, shouldChangeTextInRange_replacementString_Proc, "@:{NSRange}@");
	OS.objc_registerClassPair(cls);
	
	className = "SWTEditorView";
	cls = OS.objc_allocateClassPair(OS.class_NSTextView, className, 0);
	//TODO hitTestProc and drawRectProc should be set Control.setRegion()? 
	addEventMethods(cls, 0, fieldEditorProc3, 0, 0, 0);
	OS.class_addMethod(cls, OS.sel_insertText_, fieldEditorProc3, "@:@");
	OS.class_addMethod(cls, OS.sel_doCommandBySelector_, fieldEditorProc3, "@::");
	OS.class_addMethod(cls, OS.sel_shouldChangeTextInRange_replacementString_, shouldChangeTextInRange_replacementString_fieldEditorProc, "@:{NSRange}@");
	OS.objc_registerClassPair(cls);
	
	className = "SWTTextField";
	cls = OS.objc_allocateClassPair(OS.class_NSTextField, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.class_addMethod(cls, OS.sel_acceptsFirstResponder, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_textDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textDidEndEditing_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textViewDidChangeSelection_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_, textWillChangeSelectionProc, "@:@{NSRange}{NSRange}");
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSTextField.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSTextField.setCellClass(cls);

	className = "SWTSearchField";
	cls = OS.objc_allocateClassPair(OS.class_NSSearchField, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.class_addMethod(cls, OS.sel_textDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textViewDidChangeSelection_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_, textWillChangeSelectionProc, "@:@{NSRange}{NSRange}");
	OS.class_addMethod(cls, OS.sel_sendSearchSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_sendCancelSelection, proc2, "@:");
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSSearchField.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSSearchField.setCellClass(cls);

	className = "SWTSecureTextField";
	cls = OS.objc_allocateClassPair(OS.class_NSSecureTextField, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.class_addMethod(cls, OS.sel_textDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textViewDidChangeSelection_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_, textWillChangeSelectionProc, "@:@{NSRange}{NSRange}");
	OS.objc_registerClassPair(cls);

	// Don't subclass NSSecureTextFieldCell -- you'll get an NSException from [NSSecureTextField setCellClass:]!
	
	className = "SWTWindow";
	cls = OS.objc_allocateClassPair(OS.class_NSWindow, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendEvent_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_helpRequested_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_canBecomeKeyWindow, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_makeFirstResponder_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_noResponderFor_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_view_stringForToolTip_point_userData_, view_stringForToolTip_point_userDataProc, "@:@i{NSPoint}@");
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
}

NSFont getFont (int /*long*/ cls, int /*long*/ sel) {
	int /*long*/ widget = OS.objc_msgSend (OS.objc_msgSend (cls, OS.sel_alloc), OS.sel_initWithFrame_, new NSRect());
	int /*long*/ font = 0;
	if (OS.objc_msgSend_bool (widget, OS.sel_respondsToSelector_, sel)) {
		font = OS.objc_msgSend (widget, sel);
	}
	NSFont result = null;
	if (font != 0) {
		result = new NSFont (font);
	} else {
		result = NSFont.systemFontOfSize (NSFont.systemFontSizeForControlSize (OS.NSRegularControlSize));
	}
	result.retain ();	
	OS.objc_msgSend (widget, OS.sel_release);
	return result;
}

void initFonts () {
	smallFonts = System.getProperty("org.eclipse.swt.internal.carbon.smallFonts") != null;
	buttonFont = getFont (OS.class_NSButton, OS.sel_font);
	popUpButtonFont = getFont (OS.class_NSPopUpButton, OS.sel_font);
	textFieldFont = getFont (OS.class_NSTextField, OS.sel_font);
	secureTextFieldFont = getFont (OS.class_NSSecureTextField, OS.sel_font);
	searchFieldFont = getFont (OS.class_NSSearchField, OS.sel_font);
	comboBoxFont = getFont (OS.class_NSComboBox, OS.sel_font);
	sliderFont = getFont (OS.class_NSSlider, OS.sel_font);
	scrollerFont = getFont (OS.class_NSScroller, OS.sel_font);
	textViewFont = getFont (OS.class_NSTextView, OS.sel_font);
	tableViewFont = getFont (OS.class_NSTableView, OS.sel_font);
	outlineViewFont = getFont (OS.class_NSOutlineView, OS.sel_font);
	datePickerFont = getFont (OS.class_NSDatePicker, OS.sel_font);
	boxFont = getFont (OS.class_NSBox, OS.sel_titleFont);
	tabViewFont = getFont (OS.class_NSTabView, OS.sel_font);
	progressIndicatorFont = getFont (OS.class_NSProgressIndicator, OS.sel_font);
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
public int /*long*/ internal_new_GC (GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	if (screenWindow == null) {
		NSWindow window = (NSWindow) new NSWindow ().alloc ();
		NSRect rect = new NSRect();
		window = window.initWithContentRect(rect, OS.NSBorderlessWindowMask, OS.NSBackingStoreBuffered, false);
		window.setReleasedWhenClosed(false);
		screenWindow = window;
	}
	NSGraphicsContext context = screenWindow.graphicsContext();
//	NSAffineTransform transform = NSAffineTransform.transform();
//	NSSize size = handle.size();
//	transform.translateXBy(0, size.height);
//	transform.scaleXBy(1, -1);
//	transform.set();
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = this;
		data.background = getSystemColor(SWT.COLOR_WHITE).handle;
		data.foreground = getSystemColor(SWT.COLOR_BLACK).handle;
		data.font = getSystemFont();
	}
	return context.id;
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
public void internal_dispose_GC (int /*long*/ context, GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	
}

static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
}

boolean isValidThread () {
	return thread == Thread.currentThread ();
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
public boolean post(Event event) {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
//		int type = event.type;
//		switch (type) {
//			case SWT.KeyDown:
//			case SWT.KeyUp: {
//				int vKey = Display.untranslateKey (event.keyCode);
//				if (vKey != 0) {
//					return OS.CGPostKeyboardEvent (0, vKey, type == SWT.KeyDown) == 0;
//				} else {
//					vKey = -1;
//					int kchrPtr = OS.GetScriptManagerVariable ((short) OS.smKCHRCache);
//					int key = -1;
//					int [] state = new int [1];
//					int [] encoding = new int [1];
//					short keyScript = (short) OS.GetScriptManagerVariable ((short) OS.smKeyScript);
//					short regionCode = (short) OS.GetScriptManagerVariable ((short) OS.smRegionCode);
//					if (OS.UpgradeScriptInfoToTextEncoding (keyScript, (short) OS.kTextLanguageDontCare, regionCode, null, encoding) == OS.paramErr) {
//						if (OS.UpgradeScriptInfoToTextEncoding (keyScript, (short) OS.kTextLanguageDontCare, (short) OS.kTextRegionDontCare, null, encoding) == OS.paramErr) {
//							encoding [0] = OS.kTextEncodingMacRoman;
//						}
//					}
//					int [] encodingInfo = new int [1];
//					OS.CreateUnicodeToTextInfoByEncoding (encoding [0], encodingInfo);
//					if (encodingInfo [0] != 0) {
//						char [] input = {event.character};
//						byte [] buffer = new byte [2];
//						OS.ConvertFromUnicodeToPString (encodingInfo [0], 2, input, buffer);
//						OS.DisposeUnicodeToTextInfo (encodingInfo);
//						key = buffer [1] & 0x7f;
//					}
//					if (key == -1) return false;				
//					for (int i = 0 ; i <= 0x7F ; i++) {
//						int result1 = OS.KeyTranslate (kchrPtr, (short) (i | 512), state);
//						int result2 = OS.KeyTranslate (kchrPtr, (short) i, state);
//						if ((result1 & 0x7f) == key || (result2 & 0x7f) == key) {
//							vKey = i;
//							break;
//						}
//					}
//					if (vKey == -1) return false;
//					return OS.CGPostKeyboardEvent (key, vKey, type == SWT.KeyDown) == 0;
//				}
//			}
//			case SWT.MouseDown:
//			case SWT.MouseMove: 
//			case SWT.MouseUp: {
//				CGPoint mouseCursorPosition = new CGPoint ();
//				int chord = OS.GetCurrentEventButtonState ();
//				if (type == SWT.MouseMove) {
//					mouseCursorPosition.x = event.x;
//					mouseCursorPosition.y = event.y;
//					return OS.CGPostMouseEvent (mouseCursorPosition, true, 5, (chord & 0x1) != 0, (chord & 0x2) != 0, (chord & 0x4) != 0, (chord & 0x8) != 0, (chord & 0x10) != 0) == 0;
//				} else {
//					int button = event.button;
//					if (button < 1 || button > 5) return false;
//					boolean button1 = false, button2 = false, button3 = false, button4 = false, button5 = false;
//	 				switch (button) {
//						case 1: {
//							button1 = type == SWT.MouseDown;
//							button2 = (chord & 0x4) != 0;
//							button3 = (chord & 0x2) != 0;
//							button4 = (chord & 0x8) != 0;
//							button5 = (chord & 0x10) != 0;
//							break;
//						}
//						case 2: {
//							button1 = (chord & 0x1) != 0;
//							button2 = type == SWT.MouseDown;
//							button3 = (chord & 0x2) != 0;
//							button4 = (chord & 0x8) != 0;
//							button5 = (chord & 0x10) != 0;
//							break;
//						}
//						case 3: {
//							button1 = (chord & 0x1) != 0;
//							button2 = (chord & 0x4) != 0;
//							button3 = type == SWT.MouseDown;
//							button4 = (chord & 0x8) != 0;
//							button5 = (chord & 0x10) != 0;
//							break;
//						}
//						case 4: {
//							button1 = (chord & 0x1) != 0;
//							button2 = (chord & 0x4) != 0;
//							button3 = (chord & 0x2) != 0;
//							button4 = type == SWT.MouseDown;
//							button5 = (chord & 0x10) != 0;
//							break;
//						}
//						case 5: {
//							button1 = (chord & 0x1) != 0;
//							button2 = (chord & 0x4) != 0;
//							button3 = (chord & 0x2) != 0;
//							button4 = (chord & 0x8) != 0;
//							button5 = type == SWT.MouseDown;
//							break;
//						}
//					}
//					org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
//					OS.GetGlobalMouse (pt);
//					mouseCursorPosition.x = pt.h;
//					mouseCursorPosition.y = pt.v;
//					return OS.CGPostMouseEvent (mouseCursorPosition, true, 5, button1, button3, button2, button4, button5) == 0;
//				}
//			}
//			case SWT.MouseWheel: {
//				return OS.CGPostScrollWheelEvent (1, event.count) == 0;
//			}
//		} 
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
	NSPoint pt = new NSPoint();
	pt.x = x;
	pt.y = y;
	NSWindow fromWindow = from != null ? from.view.window() : null;
	NSWindow toWindow = to != null ? to.view.window() : null;
	if (toWindow != null && fromWindow != null && toWindow.id == fromWindow.id) {
		pt = from.view.convertPoint_toView_(pt, to.view);
	} else {
		NSRect primaryFrame = getPrimaryFrame();
		if (from != null) {
			NSView view = from.eventView ();
			pt = view.convertPoint_toView_(pt, null);
			pt = fromWindow.convertBaseToScreen(pt);
			pt.y = primaryFrame.height - pt.y;
		}
		if (to != null) {
			NSView view = to.eventView ();
			pt.y = primaryFrame.height - pt.y;
			pt = toWindow.convertScreenToBase(pt);
			pt = view.convertPoint_fromView_(pt, null);
		}
	}
	point.x = (int)pt.x;
	point.y = (int)pt.y;
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
	Rectangle rectangle = new Rectangle (x, y, width, height);
	if (from == to) return rectangle;
	NSPoint pt = new NSPoint();
	pt.x = x;
	pt.y = y;
	NSWindow fromWindow = from != null ? from.view.window() : null;
	NSWindow toWindow = to != null ? to.view.window() : null;
	if (toWindow != null && fromWindow != null && toWindow.id == fromWindow.id) {
		pt = from.view.convertPoint_toView_(pt, to.view);
	} else {
		NSRect primaryFrame = getPrimaryFrame();
		if (from != null) {
			NSView view = from.eventView ();
			pt = view.convertPoint_toView_(pt, null);
			pt = fromWindow.convertBaseToScreen(pt);
			pt.y = primaryFrame.height - pt.y;
		}
		if (to != null) {
			NSView view = to.eventView ();
			pt.y = primaryFrame.height - pt.y;
			pt = toWindow.convertScreenToBase(pt);
			pt = view.convertPoint_fromView_(pt, null);
		}
	}
	rectangle.x = (int)pt.x;
	rectangle.y = (int)pt.y;
	return rectangle;
}

int /*long*/ observerProc (int /*long*/ observer, int /*long*/ activity, int /*long*/ info) {
	switch ((int)/*64*/activity) {
		case OS.kCFRunLoopBeforeWaiting:
			if (runAsyncMessages) {
				if (runAsyncMessages (false)) wakeThread ();
			}
			break;
	}
	return 0;
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
	if (loopCounter == 0) {
		pool.release();
		pool = (NSAutoreleasePool)new NSAutoreleasePool().alloc().init();
	}
	loopCounter ++;
	try {
		boolean events = false;
		events |= runTimers ();
		events |= runContexts ();
		events |= runPopups ();
		NSEvent event = application.nextEventMatchingMask(0, null, OS.NSDefaultRunLoopMode, true);
		if (event != null) {
			events = true;
			application.sendEvent(event);
		}
		events |= runPaint ();
		if (events || runDeferredEvents ()) {
			return true;
		}
		return runAsyncMessages (false);
	} finally {
		loopCounter --;
	}
	
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
	disposing = true;
	sendEvent (SWT.Dispose, new Event ());
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.dispose ();
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
	/* Release the System Images */
	if (errorImage != null) errorImage.dispose ();
	if (infoImage != null) infoImage.dispose ();
	if (warningImage != null) warningImage.dispose ();
	errorImage = infoImage = warningImage = null;
	
	currentCaret = null;
	
	/* Release Timers */
	if (hoverTimer != null) timerExec(-1, hoverTimer);
	hoverTimer = null;
	if (caretTimer != null) timerExec(-1, caretTimer);
	caretTimer = null;
	if (nsTimers != null) {
		for (int i=0; i<nsTimers.length; i++) {
			if (nsTimers [i] != null) {
				nsTimers [i].invalidate();
				nsTimers [i].release();
			}
		}
	}
	nsTimers = null;
	if (timerDelegate != null) timerDelegate.release();
	timerDelegate = null;
	
	/* Release the System Cursors */
	for (int i = 0; i < cursors.length; i++) {
		if (cursors [i] != null) cursors [i].dispose ();
	}
	cursors = null;
	
	/* Release default fonts */
	if (buttonFont != null) buttonFont.release ();
	if (popUpButtonFont != null) popUpButtonFont.release ();
	if (textFieldFont != null) textFieldFont.release ();
	if (secureTextFieldFont != null) secureTextFieldFont.release ();
	if (searchFieldFont != null) searchFieldFont.release ();
	if (comboBoxFont != null) comboBoxFont.release ();
	if (sliderFont != null) sliderFont.release ();
	if (scrollerFont != null) scrollerFont.release ();
	if (textViewFont != null) textViewFont.release ();
	if (tableViewFont != null) tableViewFont.release ();
	if (outlineViewFont != null) outlineViewFont.release ();
	if (datePickerFont != null) datePickerFont.release ();
	if (boxFont != null) boxFont.release ();
	if (tabViewFont != null) tabViewFont.release ();
	if (progressIndicatorFont != null) progressIndicatorFont.release ();
	buttonFont = popUpButtonFont = textFieldFont = secureTextFieldFont = null;
	searchFieldFont = comboBoxFont = sliderFont = scrollerFont;
	textViewFont = tableViewFont = outlineViewFont = datePickerFont = null;
	boxFont = tabViewFont = progressIndicatorFont = null;
	
	/* Release Dock image */
	if (dockImage != null) dockImage.release();
	dockImage = null;

	if (screenWindow != null) screenWindow.release();
	screenWindow = null;
	
	if (needsDisplay != null) needsDisplay.release();
	if (needsDisplayInRect != null) needsDisplayInRect.release();
	if (isPainting != null) isPainting.release();
	needsDisplay = needsDisplayInRect = isPainting = null;
	
	modalShells = null;
	menuBar = null;
	menus = null;
	
	if (markedAttributes != null) markedAttributes.release();
	markedAttributes = null;
	
	if (oldCursorSetProc != 0) {
		int /*long*/ method = OS.class_getInstanceMethod(OS.class_NSCursor, OS.sel_set);
		OS.method_setImplementation(method, oldCursorSetProc);
	}
	if (cursorSetCallback != null) cursorSetCallback.dispose();
	cursorSetCallback = null;

	deadKeyState = null;
	
	// Clear the menu bar if we created it.
	if (!isEmbedded) {
		//remove all existing menu items except the application menu
		NSMenu menubar = application.mainMenu();
		int /*long*/ count = menubar.numberOfItems();
		while (count > 1) {
			menubar.removeItemAtIndex(count - 1);
			count--;
		}

		application.setDelegate(null);
		applicationDelegate.release();
		applicationDelegate = null;
	}
	
	// The autorelease pool is cleaned up when we call NSApplication.terminate().

	if (application != null && applicationClass != 0) {
		OS.object_setClass (application.id, applicationClass);
	}
	application = null;
	applicationClass = 0;
	
	if (runLoopObserver != 0) {
		OS.CFRunLoopObserverInvalidate (runLoopObserver);
		OS.CFRelease (runLoopObserver);
	}
	runLoopObserver = 0;
	if (observerCallback != null) observerCallback.dispose();
	observerCallback = null;
}

void removeContext (GCData context) {
	if (contexts == null) return;
	int count = 0;
	for (int i = 0; i < contexts.length; i++) {
		if (contexts[i] != null) {
			if (contexts [i] == context) {
				contexts[i] = null;
			} else {
				count++;
			}
		}
	}
	if (count == 0) contexts = null;
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

Widget removeWidget (NSObject view) {
	if (view == null) return null;
	int /*long*/ [] jniRef = new int /*long*/ [1];
	OS.object_getInstanceVariable(view.id, SWT_OBJECT, jniRef);
	if (jniRef[0] == 0) return null;
	Widget widget = (Widget)OS.JNIGetObject(jniRef[0]);
	OS.object_setInstanceVariable(view.id, SWT_OBJECT, 0);
	return widget;
}

void removeMenu (Menu menu) {
	if (menus == null) return;
	for (int i = 0; i < menus.length; i++) {
		if (menus [i] == menu) {
			menus[i] = null;
			break;
		}
	}
}

void removePopup (Menu menu) {
	if (popups == null) return;
	for (int i=0; i<popups.length; i++) {
		if (popups [i] == menu) {
			popups [i] = null;
			return;
		}
	}
}

boolean runAsyncMessages (boolean all) {
	return synchronizer.runAsyncMessages (all);
}

boolean runContexts () {
	if (contexts != null) {
		for (int i = 0; i < contexts.length; i++) {
			if (contexts[i] != null && contexts[i].flippedContext != null) {
				contexts[i].flippedContext.flushGraphics();
			}
		}
	}
	return false;
}

boolean runDeferredEvents () {
	boolean run = false;
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
				run = true;
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
	return run;
}

boolean runPaint () {
	if (needsDisplay == null && needsDisplayInRect == null) return false;
	if (needsDisplay != null) {
		int /*long*/ count = needsDisplay.count();
		for (int i = 0; i < count; i++) {
			OS.objc_msgSend(needsDisplay.objectAtIndex(i).id, OS.sel_setNeedsDisplay_, true);
		}
		needsDisplay.release();
		needsDisplay = null;
	}
	if (needsDisplayInRect != null) {
		int /*long*/ count = needsDisplayInRect.count();
		for (int i = 0; i < count; i+=2) {
			NSValue value = new NSValue(needsDisplayInRect.objectAtIndex(i+1));
			OS.objc_msgSend(needsDisplayInRect.objectAtIndex(i).id, OS.sel_setNeedsDisplayInRect_, value.rectValue());
		}
		needsDisplayInRect.release();
		needsDisplayInRect = null;
	}
	return true;
}

boolean runPopups () {
	if (popups == null) return false;
	boolean result = false;
	while (popups != null) {
		Menu menu = popups [0];
		if (menu == null) break;
		runDeferredEvents ();
		int length = popups.length;
		System.arraycopy (popups, 1, popups, 0, --length);
		popups [length] = null;
		if (!menu.isDisposed ()) menu._setVisible (true);
		result = true;
	}
	popups = null;
	return result;
}

boolean runTimers () {
	if (timerList == null) return false;
	boolean result = false;
	for (int i=0; i<timerList.length; i++) {
		if (nsTimers [i] == null && timerList [i] != null) {
			Runnable runnable = timerList [i];
			timerList [i] = null;
			if (runnable != null) {
				result = true;
				runnable.run ();
			}
		}
	}
	return result;
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
 * On platforms which support it, sets the application name
 * to be the argument. On Motif, for example, this can be used
 * to set the name used for resource lookup.  Specifying
 * <code>null</code> for the name clears it.
 *
 * @param name the new app name or <code>null</code>
 */
public static void setAppName (String name) {
	APP_NAME = name;
}

//TODO use custom timer instead of timerExec
Runnable hoverTimer = new Runnable () {
	public void run () {
		if (currentControl != null && !currentControl.isDisposed()) {
			currentControl.sendMouseEvent (null, SWT.MouseHover, trackingControl != null && !trackingControl.isDisposed());
		}
	}
};
//TODO - use custom timer instead of timerExec
Runnable caretTimer = new Runnable () {
	public void run () {
		if (currentCaret != null) {
			if (currentCaret == null || currentCaret.isDisposed()) return;
			if (currentCaret.blinkCaret ()) {
				int blinkRate = currentCaret.blinkRate;
				if (blinkRate != 0) timerExec (blinkRate, this);
			} else {
				currentCaret = null;
			}
		}
		
	}
};

//TODO - use custom timer instead of timerExec
Runnable defaultButtonTimer = new Runnable() {
	public void run() {
		if (isDisposed ()) return;
		Shell shell = getActiveShell();
		if (shell != null && !shell.isDisposed()) {
			Button defaultButton = shell.defaultButton;
			if (defaultButton != null && !defaultButton.isDisposed()) {
				NSView view = defaultButton.view;
				view.display();
			}
		}
		if (isDisposed ()) return;
		if (hasDefaultButton()) timerExec(DEFAULT_BUTTON_INTERVAL, this);
	}
};

void setCurrentCaret (Caret caret) {
	currentCaret = caret;
	int blinkRate = currentCaret != null ? currentCaret.blinkRate : -1;
	timerExec (blinkRate, caretTimer);
}

void setCursor (Control control) {
	Cursor cursor = null;
	if (control != null && !control.isDisposed()) cursor = control.findCursor ();
	if (cursor == null) {
		NSWindow window = application.keyWindow();
		if (window != null) {
			if (window.areCursorRectsEnabled ()) {
				window.disableCursorRects ();
				window.enableCursorRects ();
			}
			return;
		}
		cursor = getSystemCursor (SWT.CURSOR_ARROW);
	}
	lockCursor = false;
	cursor.handle.set ();
	lockCursor = true;
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
	CGPoint pt = new CGPoint ();
	pt.x = x;  pt.y = y;
	OS.CGWarpMouseCursorPosition (pt);
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
	
	if (key.equals (ADD_WIDGET_KEY)) {
		Object [] data = (Object [])value;
		NSObject object = (NSObject)data [0];
		Widget widget = (Widget)data [1];
		if (widget == null) {
			removeWidget (object);
		} else {
			addWidget (object, widget);
		}
	}
	
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

void setMenuBar (Menu menu) {
	if (menu == menuBar) return;
	menuBar = menu;
	//remove all existing menu items except the application menu
	NSMenu menubar = application.mainMenu();
	int /*long*/ count = menubar.numberOfItems();
	while (count > 1) {
		menubar.removeItemAtIndex(count - 1);
		count--;
	}
	//set parent of each item to NULL and add them to menubar
	if (menu != null) {
		MenuItem[] items = menu.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].nsItem.setMenu(null);
			menubar.addItem(items[i].nsItem);
		}
	}
}

void setModalShell (Shell shell) {
	if (modalShells == null) modalShells = new Shell [4];
	int index = 0, length = modalShells.length;
	while (index < length) {
		if (modalShells [index] == shell) return;
		if (modalShells [index] == null) break;
		index++;
	}
	if (index == length) {
		Shell [] newModalShells = new Shell [length + 4];
		System.arraycopy (modalShells, 0, newModalShells, 0, length);
		modalShells = newModalShells;
	}
	modalShells [index] = shell;
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) shells [i].updateModal ();
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
	if (loopCounter == 0) {
		pool.release();
		pool = (NSAutoreleasePool)new NSAutoreleasePool().alloc().init();
	}
	allowTimers = runAsyncMessages = false;
	NSRunLoop.currentRunLoop().runMode(OS.NSDefaultRunLoopMode, NSDate.distantFuture());
	allowTimers = runAsyncMessages = true;
	return true;
}

int sourceProc (int info) {
	return 0;
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
	//TODO - remove a timer, reschedule a timer not tested
	if (runnable == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (timerList == null) timerList = new Runnable [4];
	if (nsTimers == null) nsTimers = new NSTimer [4];
	int index = 0;
	while (index < timerList.length) {
		if (timerList [index] == runnable) break;
		index++;
	}
	if (index != timerList.length) {
		NSTimer timer = nsTimers [index];
		if (timer == null) {
			timerList [index] = null;
		} else {
			if (milliseconds < 0) {
				timer.invalidate();
				timer.release();
				timerList [index] = null;
				nsTimers [index] = null;
			} else {
				timer.setFireDate(NSDate.dateWithTimeIntervalSinceNow (milliseconds / 1000.0));
			}
			return;
		}
	} 
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
		NSTimer [] newTimerIds = new NSTimer [nsTimers.length + 4];
		System.arraycopy (nsTimers, 0, newTimerIds, 0, nsTimers.length);
		nsTimers = newTimerIds;
	}
	NSNumber userInfo = NSNumber.numberWithInt(index);
	NSTimer timer = NSTimer.scheduledTimerWithTimeInterval(milliseconds / 1000.0, timerDelegate, OS.sel_timerProc_, userInfo, false);
	NSRunLoop.currentRunLoop().addTimer(timer, OS.NSEventTrackingRunLoopMode);
	timer.retain();
	if (timer != null) {
		nsTimers [index] = timer;
		timerList [index] = runnable;
	}
}

int /*long*/ timerProc (int /*long*/ id, int /*long*/ sel, int /*long*/ timerID) {
	NSTimer timer = new NSTimer (timerID);
	NSNumber number = new NSNumber(timer.userInfo());
	int index = number.intValue();
	if (timerList == null) return 0;
	if (0 <= index && index < timerList.length) {
		if (allowTimers) {
			Runnable runnable = timerList [index];
			timerList [index] = null;
			nsTimers [index] = null;
			if (runnable != null) runnable.run ();
		} else {
			nsTimers [index] = null;
			wakeThread ();
		}
	}
	timer.invalidate();
	timer.release();
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
public void update () {
	checkDevice ();	
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.update (true);
	}
}

void updateDefaultButton () {
	timerExec(hasDefaultButton() ? DEFAULT_BUTTON_INTERVAL : -1, defaultButtonTimer);
}

void updateQuitMenu () {
	boolean enabled = true;
	Shell [] shells = getShells ();
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if ((shell.style & mask) != 0 && shell.isVisible ()) {
			enabled = false;
			break;
		}
	}
	
	NSMenu mainmenu = application.mainMenu();
	NSMenuItem appitem = mainmenu.itemAtIndex(0);
	if (appitem != null) {
		NSMenu sm = appitem.submenu();

		// Normally this would be sel_terminate_ but we changed it so terminate: doesn't kill the app.
		int /*long*/ quitIndex = sm.indexOfItemWithTarget(applicationDelegate, OS.sel_quitRequested_);
		
		if (quitIndex != -1) {
			NSMenuItem quitItem = sm.itemAtIndex(quitIndex);
			quitItem.setEnabled(enabled);
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
	NSObject object = new NSObject().alloc().init();
	object.performSelectorOnMainThread(OS.sel_release, null, false);
}

Control findControl (boolean checkTrim) {
	return findControl(checkTrim, null);
}

Control findControl (boolean checkTrim, NSView[] hitView) {
	NSView view = null;
	NSPoint screenLocation = NSEvent.mouseLocation();
	NSArray windows = application.orderedWindows();
	for (int i = 0, count = (int)/*64*/windows.count(); i < count && view == null; i++) {
		NSWindow window = new NSWindow(windows.objectAtIndex(i));
		NSView contentView = window.contentView();
		if (contentView != null && OS.NSPointInRect(screenLocation, window.frame())) {
			NSPoint location = window.convertScreenToBase(screenLocation);
			view = contentView.hitTest (location);
			if (view == null && !checkTrim) {
				view = contentView;
			}
			break;
		}
	}
	Control control = null;
	if (view != null) {
		do {
			Widget widget = getWidget (view);
			if (widget instanceof Control) {
				control = (Control)widget;
				break;
			}
			view = view.superview();
		} while (view != null);
	}
	if (checkTrim) {
		if (control != null && control.isTrim (view)) control = null;
	}
	if (control != null && hitView != null) hitView[0] = view;
	return control;
}

int /*long*/ applicationNextEventMatchingMask (int /*long*/ id, int /*long*/ sel, int /*long*/ mask, int /*long*/ expiration, int /*long*/ mode, int /*long*/ dequeue) {
	if (dequeue != 0 && trackingControl != null && !trackingControl.isDisposed()) runDeferredEvents();
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	int /*long*/ result = OS.objc_msgSendSuper(super_struct, sel, mask, expiration, mode, dequeue != 0);
	if (result != 0) {
		if (dequeue != 0 && trackingControl != null && !trackingControl.isDisposed()) {
			applicationSendTrackingEvent(new NSEvent(result), trackingControl);
		}
	}
	return result;
}

void applicationSendTrackingEvent (NSEvent nsEvent, Control trackingControl) {
	int type = (int)/*64*/nsEvent.type();
	switch (type) {
		case OS.NSLeftMouseDown:
		case OS.NSRightMouseDown:
		case OS.NSOtherMouseDown:
			trackingControl.sendMouseEvent (nsEvent, SWT.MouseDown, true);
			break;
		case OS.NSLeftMouseUp:
		case OS.NSRightMouseUp:
		case OS.NSOtherMouseUp:
			checkEnterExit (findControl (true), nsEvent, true);
			if (trackingControl.isDisposed()) return;
			trackingControl.sendMouseEvent (nsEvent, SWT.MouseUp, true);
			break;
		case OS.NSLeftMouseDragged:
		case OS.NSRightMouseDragged:
		case OS.NSOtherMouseDragged:
			checkEnterExit (trackingControl, nsEvent, true);
			if (trackingControl.isDisposed()) return;
			//FALL THROUGH
		case OS.NSMouseMoved:
			trackingControl.sendMouseEvent (nsEvent, SWT.MouseMove, true);
			break;
	}
}

void applicationSendEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ event) {
	NSEvent nsEvent = new NSEvent(event);
	NSWindow window = nsEvent.window ();
	int type = (int)/*64*/nsEvent.type ();
	boolean beep = false;
	switch (type) {
		case OS.NSLeftMouseDown:
		case OS.NSRightMouseDown:
		case OS.NSOtherMouseDown:
			beep = true;
		case OS.NSLeftMouseUp:
		case OS.NSRightMouseUp:
		case OS.NSOtherMouseUp:
		case OS.NSLeftMouseDragged:
		case OS.NSRightMouseDragged:
		case OS.NSOtherMouseDragged:
		case OS.NSMouseMoved:
		case OS.NSMouseEntered:
		case OS.NSMouseExited:
		case OS.NSKeyDown:
		case OS.NSKeyUp:
		case OS.NSScrollWheel:
			if (window != null) {
				Shell shell = (Shell) getWidget (window.id);
				if (shell != null) {
					Shell modalShell = shell.getModalShell ();
					if (modalShell != null) {
						if (beep) {
							if (!application.isActive()) {
								application.activateIgnoringOtherApps(true);
							} else {
								beep ();
							}
						}
						return;
					}
				}
			}
			break;
	}
	sendEvent = true;
	
	/*
	 * Feature in Cocoa. The help key triggers context-sensitive help but doesn't get forwarded to the window as a key event.
	 * If the event is destined for the key window, is the help key, and is an NSKeyDown, send it directly to the window first.
	 */
	if (window != null && window.isKeyWindow() && nsEvent.type() == OS.NSKeyDown && (nsEvent.modifierFlags() & OS.NSHelpKeyMask) != 0)	{
		window.sendEvent(nsEvent);
	}

	/*
	 * Feature in Cocoa. NSKeyUp events are not delivered to the window if the command key is down.
	 * If the event is destined for the key window, and it's a key up and the command key is down, send it directly to the window.
	 */
	if (window != null && window.isKeyWindow() && nsEvent.type() == OS.NSKeyUp && (nsEvent.modifierFlags() & OS.NSCommandKeyMask) != 0)	{
		window.sendEvent(nsEvent);
	} else {
		objc_super super_struct = new objc_super ();
		super_struct.receiver = id;
		super_struct.super_class = OS.objc_msgSend (id, OS.sel_superclass);
		OS.objc_msgSendSuper (super_struct, sel, event);
	}
	sendEvent = false;
}

// #245724: [NSApplication isRunning] must return true to allow the AWT to load correctly.
static int /*long*/ applicationProc(int /*long*/ id, int /*long*/ sel) {
	//TODO optimize getting the display
	Display display = getCurrent ();
	if (display == null) return 0;
	if (sel == OS.sel_isRunning) {
		return display.isDisposed() ? 0 : 1;
	}
	return 0;
}

static int /*long*/ applicationProc(int /*long*/ id, int /*long*/ sel, int /*long*/ event) {
	//TODO optimize getting the display
	Display display = getCurrent ();
	if (display == null) return 0;
	if (sel == OS.sel_sendEvent_) {
		display.applicationSendEvent (id, sel, event);
		return 0;
	}
	return 0;
}

static int /*long*/ applicationProc(int /*long*/ id, int /*long*/sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3) {
	//TODO optimize getting the display
	Display display = getCurrent ();
	if (display == null) return 0;
	if (sel == OS.sel_nextEventMatchingMask_untilDate_inMode_dequeue_) {
		return display.applicationNextEventMatchingMask(id, sel, arg0, arg1, arg2, arg3);
	}
	return 0;
}

static int /*long*/ applicationDelegateProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	//TODO optimize getting the display
	Display display = getCurrent ();
	if (display == null) return 0;
	id applicationDelegate = display.applicationDelegate;
	NSApplication application = display.application;
	if (sel == OS.sel_applicationWillFinishLaunching_) {
		NSDictionary dict = NSDictionary.dictionaryWithObject(applicationDelegate, NSString.stringWith("NSOwner"));
		NSString nibFile = NSString.stringWith("/System/Library/Frameworks/JavaVM.framework/Resources/English.lproj/DefaultApp.nib");
		if (!NSBundle.loadNibFile(nibFile, dict, 0)) {
			nibFile = NSString.stringWith("/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Resources/English.lproj/DefaultApp.nib");
			NSBundle.loadNibFile(nibFile, dict, 0);	
		}
		//replace %@ with application name
		NSMenu mainmenu = application.mainMenu();
		NSMenuItem appitem = mainmenu.itemAtIndex(0);
		if (appitem != null) {
			NSMenu sm = appitem.submenu();
			NSArray ia = sm.itemArray();
			for(int i = 0; i < ia.count(); i++) {
				NSMenuItem ni = new NSMenuItem(ia.objectAtIndex(i));
				NSString title = ni.title().stringByReplacingOccurrencesOfString(NSString.stringWith("%@"), NSString.stringWith(APP_NAME));
				ni.setTitle(title);
			}

			int /*long*/ quitIndex = sm.indexOfItemWithTarget(applicationDelegate, OS.sel_terminate_);
			
			if (quitIndex != -1) {
				NSMenuItem quitItem = sm.itemAtIndex(quitIndex);
				quitItem.setAction(OS.sel_quitRequested_);
			}
		}		
	} else if (sel == OS.sel_terminate_) {
		// Do nothing here -- without a definition of sel_terminate we get a warning dumped to the console.
	} else if (sel == OS.sel_orderFrontStandardAboutPanel_) {
//		Event event = new Event ();
//		sendEvent (SWT.ABORT, event);
	} else if (sel == OS.sel_hideOtherApplications_) {
		application.hideOtherApplications(application);
	} else if (sel == OS.sel_hide_) {
		application.hide(application);
	} else if (sel == OS.sel_unhideAllApplications_) {
		application.unhideAllApplications(application);
	} else if (sel == OS.sel_quitRequested_) {
		if (!display.disposing) {
			Event event = new Event ();
			display.sendEvent (SWT.Close, event);
			if (event.doit) {
				display.dispose();
			}
		}
	} else if (sel == OS.sel_applicationDidBecomeActive_) {
		Control control = display.getFocusControl();
		if (control != null && !control.isDisposed()) {
			display.focusControl = control;
			control.sendFocusEvent(SWT.FocusIn, false);
		}
		display.checkEnterExit(display.findControl(true), null, false);
	} else if (sel == OS.sel_applicationDidResignActive_) {
		Control control = display.focusControl;
		if (control != null && !control.isDisposed()) {
			display.focusControl = null;
			control.sendFocusEvent(SWT.FocusOut, false);
		}
		display.checkEnterExit(null, null, false);
	}
 	return 0;
}

static int /*long*/ dialogProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	int /*long*/ [] jniRef = new int /*long*/ [1];
	OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
	if (jniRef[0] == 0) return 0;
	if (sel == OS.sel_changeColor_) {
		ColorDialog dialog = (ColorDialog)OS.JNIGetObject(jniRef[0]);
		if (dialog == null) return 0;
		dialog.changeColor(id, sel, arg0);
	} else if (sel == OS.sel_changeFont_) {
		FontDialog dialog = (FontDialog)OS.JNIGetObject(jniRef[0]);
		if (dialog == null) return 0;
		dialog.changeFont(id, sel, arg0);
	} else if (sel == OS.sel_windowWillClose_) {
		Object object = OS.JNIGetObject(jniRef[0]);
		if (object instanceof FontDialog) {
			((FontDialog)object).windowWillClose(id, sel, arg0);
		} else if (object instanceof ColorDialog) {
			((ColorDialog)object).windowWillClose(id, sel, arg0);
		}
	}
	return 0;
}

static int /*long*/ fieldEditorProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	Widget widget = null;
	NSView view = new NSView (id);
	do {
		widget = GetWidget (view.id);
		if (widget != null) break;
		view = view.superview ();
	} while (view != null);
	if (widget == null) return 0;
	if (sel == OS.sel_keyDown_) {
		widget.keyDown (id, sel, arg0);
	} else if (sel == OS.sel_keyUp_) {
		widget.keyUp (id, sel, arg0);
	} else if (sel == OS.sel_flagsChanged_) {
		widget.flagsChanged(id, sel, arg0);
	} else if (sel == OS.sel_insertText_) {
		return widget.insertText (id, sel, arg0) ? 1 : 0;
	} else if (sel == OS.sel_doCommandBySelector_) {
		widget.doCommandBySelector (id, sel, arg0);
	} else if (sel == OS.sel_menuForEvent_) {
		return widget.menuForEvent (id, sel, arg0);
	} else if (sel == OS.sel_mouseDown_) {
		widget.mouseDown(id, sel, arg0);
	} else if (sel == OS.sel_mouseUp_) {
		widget.mouseUp(id, sel, arg0);
	} else if (sel == OS.sel_mouseMoved_) {
		widget.mouseMoved(id, sel, arg0);
	} else if (sel == OS.sel_mouseDragged_) {
		widget.mouseDragged(id, sel, arg0);
	} else if (sel == OS.sel_mouseEntered_) {
		widget.mouseEntered(id, sel, arg0);
	} else if (sel == OS.sel_mouseExited_) {
		widget.mouseExited(id, sel, arg0);
	} else if (sel == OS.sel_cursorUpdate_) {
		widget.cursorUpdate(id, sel, arg0);
	} else if (sel == OS.sel_rightMouseDown_) {
		widget.rightMouseDown(id, sel, arg0);
	} else if (sel == OS.sel_rightMouseDragged_) {
		widget.rightMouseDragged(id, sel, arg0);
	} else if (sel == OS.sel_rightMouseUp_) {
		widget.rightMouseUp(id, sel, arg0);
	} else if (sel == OS.sel_otherMouseDown_) {
		widget.otherMouseDown(id, sel, arg0);
	} else if (sel == OS.sel_otherMouseUp_) {
		widget.otherMouseUp(id, sel, arg0);
	} else if (sel == OS.sel_otherMouseDragged_) {
		widget.otherMouseDragged(id, sel, arg0);
	}
	return 0;
}

static int /*long*/ fieldEditorProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	Widget widget = null;
	NSView view = new NSView (id);
	do {
		widget = GetWidget (view.id);
		if (widget != null) break;
		view = view.superview ();
	} while (view != null);
	if (sel == OS.sel_shouldChangeTextInRange_replacementString_) {
		return widget.shouldChangeTextInRange_replacementString(id, sel, arg0, arg1) ? 1 : 0;
	}
	return 0;
}

static int /*long*/ windowDelegateProc(int /*long*/ id, int /*long*/ sel) {
	/*
	* Feature in Cocoa.  In Cocoa, the default button animation is done
	* in a separate thread that calls drawRect() and isOpaque() from
	* outside the UI thread.  This means that those methods, and application
	* code that runs as a result of those methods, must be thread safe.
	* In SWT, paint events must happen in the UI thread.  The fix is
	* to detect a non-UI thread and avoid the drawing. Instead, the
	* default button is animated by a timer.
	*/
	if (!NSThread.isMainThread()) {
		if (sel == OS.sel_isOpaque) {
			return 1;
		}
	}
	Widget widget = GetWidget(id);
	if (widget == null) return 0;
	if (sel == OS.sel_sendSelection) {
		widget.sendSelection();
	} else if (sel == OS.sel_sendDoubleSelection) {
		widget.sendDoubleSelection();
	} else if (sel == OS.sel_sendVerticalSelection) {
		widget.sendVerticalSelection();
	} else if (sel == OS.sel_sendHorizontalSelection) {
		widget.sendHorizontalSelection();
	} else if (sel == OS.sel_sendSearchSelection) {
		widget.sendSearchSelection();
	} else if (sel == OS.sel_sendCancelSelection) {
		widget.sendCancelSelection();
	} else if (sel == OS.sel_acceptsFirstResponder) {
		return widget.acceptsFirstResponder(id, sel) ? 1 : 0;
	} else if (sel == OS.sel_becomeFirstResponder) {
		return widget.becomeFirstResponder(id, sel) ? 1 : 0;
	} else if (sel == OS.sel_resignFirstResponder) {
		return widget.resignFirstResponder(id, sel) ? 1 : 0;
	} else if (sel == OS.sel_isOpaque) {
		return widget.isOpaque(id, sel) ? 1 : 0;
	} else if (sel == OS.sel_unmarkText) {
		//TODO not called?
	} else if (sel == OS.sel_validAttributesForMarkedText) {
		return widget.validAttributesForMarkedText (id, sel);
	} else if (sel == OS.sel_markedRange) {
		NSRange range = widget.markedRange (id, sel);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRange.sizeof);
		OS.memmove (result, range, NSRange.sizeof);
		return result;
	} else if (sel == OS.sel_selectedRange) {
		NSRange range = widget.selectedRange (id, sel);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRange.sizeof);
		OS.memmove (result, range, NSRange.sizeof);
		return result;
	} else if (sel == OS.sel_cellSize) {
		NSSize size = widget.cellSize (id, sel);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSSize.sizeof);
		OS.memmove (result, size, NSSize.sizeof);
		return result;
	} else if (sel == OS.sel_hasMarkedText) {
		return widget.hasMarkedText (id, sel) ? 1 : 0;
	} else if (sel == OS.sel_canBecomeKeyWindow) {
		return widget.canBecomeKeyWindow (id, sel) ? 1 : 0;
	} else if (sel == OS.sel_accessibilityActionNames) {
		return widget.accessibilityActionNames(id, sel);
	} else if (sel == OS.sel_accessibilityAttributeNames) {
		return widget.accessibilityAttributeNames(id, sel);
	} else if (sel == OS.sel_accessibilityParameterizedAttributeNames) {
		return widget.accessibilityParameterizedAttributeNames(id, sel);
	} else if (sel == OS.sel_accessibilityFocusedUIElement) {
		return widget.accessibilityFocusedUIElement(id, sel);
	} else if (sel == OS.sel_accessibilityIsIgnored) {
		return (widget.accessibilityIsIgnored(id, sel) ? 1 : 0);
	} else if (sel == OS.sel_nextState) {
		return widget.nextState(id, sel);
	} else if (sel == OS.sel_resetCursorRects) {
		widget.resetCursorRects(id, sel);
	} else if (sel == OS.sel_nextValidKeyView) {
		return widget.nextValidKeyView(id, sel);
	} else if (sel == OS.sel_previousValidKeyView) {
		return widget.previousValidKeyView(id, sel);
	} else if (sel == OS.sel_updateTrackingAreas) {
		widget.updateTrackingAreas(id, sel);
	} else if (sel == OS.sel_viewDidMoveToWindow) {
		widget.viewDidMoveToWindow(id, sel);
	} else if (sel == OS.sel_image) {
		return widget.image(id, sel);
	}
	return 0;
}

static int /*long*/ windowDelegateProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	/*
	* Feature in Cocoa.  In Cocoa, the default button animation is done
	* in a separate thread that calls drawRect() and isOpaque() from
	* outside the UI thread.  This means that those methods, and application
	* code that runs as a result of those methods, must be thread safe.
	* In SWT, paint events must happen in the UI thread.  The fix is
	* to detect a non-UI thread and avoid the drawing. Instead, the
	* default button is animated by a timer.
	*/
	if (!NSThread.isMainThread()) {
		if (sel == OS.sel_drawRect_) {
			return 0;
		}
	}
	if (sel == OS.sel_timerProc_) {
		//TODO optimize getting the display
		Display display = getCurrent ();
		if (display == null) return 0;
		return display.timerProc (id, sel, arg0);
	}
	Widget widget = GetWidget(id);
	if (widget == null) return 0;
	if (sel == OS.sel_windowWillClose_) {
		widget.windowWillClose(id, sel, arg0);
	} else if (sel == OS.sel_drawRect_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		widget.drawRect(id, sel, rect);
	} else if (sel == OS.sel_setFrameOrigin_) {
		NSPoint point = new NSPoint();
		OS.memmove(point, arg0, NSPoint.sizeof);
		widget.setFrameOrigin(id, sel, point);
	} else if (sel == OS.sel_setFrameSize_) {
		NSSize size = new NSSize();
		OS.memmove(size, arg0, NSSize.sizeof);
		widget.setFrameSize(id, sel, size);
	} else if (sel == OS.sel_hitTest_) {
		NSPoint point = new NSPoint();
		OS.memmove(point, arg0, NSPoint.sizeof);
		return widget.hitTest(id, sel, point);
	} else if (sel == OS.sel_windowShouldClose_) {
		return widget.windowShouldClose(id, sel, arg0) ? 1 : 0;
	} else if (sel == OS.sel_mouseDown_) {
		widget.mouseDown(id, sel, arg0);
	} else if (sel == OS.sel_keyDown_) {
		widget.keyDown(id, sel, arg0);
	} else if (sel == OS.sel_keyUp_) {
		widget.keyUp(id, sel, arg0);
	} else if (sel == OS.sel_flagsChanged_) {
		widget.flagsChanged(id, sel, arg0);
	} else if (sel == OS.sel_mouseUp_) {
		widget.mouseUp(id, sel, arg0);
	} else if (sel == OS.sel_rightMouseDown_) {
		widget.rightMouseDown(id, sel, arg0);
	} else if (sel == OS.sel_rightMouseDragged_) {
		widget.rightMouseDragged(id, sel, arg0);
	} else if (sel == OS.sel_rightMouseUp_) {
		widget.rightMouseUp(id, sel, arg0);
	} else if (sel == OS.sel_otherMouseDown_) {
		widget.otherMouseDown(id, sel, arg0);
	} else if (sel == OS.sel_otherMouseUp_) {
		widget.otherMouseUp(id, sel, arg0);
	} else if (sel == OS.sel_otherMouseDragged_) {
		widget.otherMouseDragged(id, sel, arg0);
	} else if (sel == OS.sel_mouseMoved_) {
		widget.mouseMoved(id, sel, arg0);
	} else if (sel == OS.sel_mouseDragged_) {
		widget.mouseDragged(id, sel, arg0);
	} else if (sel == OS.sel_mouseEntered_) {
		widget.mouseEntered(id, sel, arg0);
	} else if (sel == OS.sel_mouseExited_) {
		widget.mouseExited(id, sel, arg0);
	} else if (sel == OS.sel_cursorUpdate_) {
		widget.cursorUpdate(id, sel, arg0);
	} else if (sel == OS.sel_menuForEvent_) {
		return widget.menuForEvent(id, sel, arg0);
	} else if (sel == OS.sel_noResponderFor_) {
		widget.noResponderFor(id, sel, arg0);
	} else if (sel == OS.sel_shouldDelayWindowOrderingForEvent_) {
		return widget.shouldDelayWindowOrderingForEvent(id, sel, arg0) ? 1 : 0;
	} else if (sel == OS.sel_acceptsFirstMouse_) {
		return widget.acceptsFirstMouse(id, sel, arg0) ? 1 : 0;
	} else if (sel == OS.sel_numberOfRowsInTableView_) {
		return widget.numberOfRowsInTableView(id, sel, arg0);
	} else if (sel == OS.sel_comboBoxSelectionDidChange_) {
		widget.comboBoxSelectionDidChange(id, sel, arg0);
	} else if (sel == OS.sel_tableViewSelectionDidChange_) {
		widget.tableViewSelectionDidChange(id, sel, arg0);
	} else if (sel == OS.sel_windowDidResignKey_) {
		widget.windowDidResignKey(id, sel, arg0);
	} else if (sel == OS.sel_windowDidBecomeKey_) {
		widget.windowDidBecomeKey(id, sel, arg0);
	} else if (sel == OS.sel_windowDidResize_) {
		widget.windowDidResize(id, sel, arg0);
	} else if (sel == OS.sel_windowDidMove_) {
		widget.windowDidMove(id, sel, arg0);
	} else if (sel == OS.sel_menuWillOpen_) {
		widget.menuWillOpen(id, sel, arg0);
	} else if (sel == OS.sel_menuDidClose_) {
		widget.menuDidClose(id, sel, arg0);
	} else if (sel == OS.sel_menuNeedsUpdate_) {
		widget.menuNeedsUpdate(id, sel, arg0);
	} else if (sel == OS.sel_outlineViewSelectionDidChange_) {
		widget.outlineViewSelectionDidChange(id, sel, arg0);
	} else if (sel == OS.sel_outlineViewItemDidExpand_) {
		widget.outlineViewItemDidExpand(id, sel, arg0);
	} else if (sel == OS.sel_sendEvent_) {
		widget.windowSendEvent(id, sel, arg0);
	} else if (sel == OS.sel_helpRequested_) {
		widget.helpRequested(id, sel, arg0);
	} else if (sel == OS.sel_scrollWheel_) {
		widget.scrollWheel(id, sel, arg0);
	} else if (sel == OS.sel_pageDown_) {
		widget.pageDown(id, sel, arg0);
	} else if (sel == OS.sel_pageUp_) {
		widget.pageUp(id, sel, arg0);
	} else if (sel == OS.sel_textViewDidChangeSelection_) {
		widget.textViewDidChangeSelection(id, sel, arg0);
	} else if (sel == OS.sel_textDidChange_) {
		widget.textDidChange(id, sel, arg0);
	} else if (sel == OS.sel_textDidEndEditing_) {
		widget.textDidEndEditing(id, sel, arg0);
	} else if (sel == OS.sel_attributedSubstringFromRange_) {
		return widget.attributedSubstringFromRange (id, sel, arg0);
	} else if (sel == OS.sel_characterIndexForPoint_) {
		return widget.characterIndexForPoint (id, sel, arg0);
	} else if (sel == OS.sel_firstRectForCharacterRange_) {
		NSRect rect = widget.firstRectForCharacterRange (id, sel, arg0);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRect.sizeof);
		OS.memmove (result, rect, NSRect.sizeof);
		return result;
	} else if (sel == OS.sel_insertText_) {
		return widget.insertText (id, sel, arg0) ? 1 : 0;
	} else if (sel == OS.sel_doCommandBySelector_) {
		widget.doCommandBySelector (id, sel, arg0);
	} else if (sel == OS.sel_highlightSelectionInClipRect_) {
		widget.highlightSelectionInClipRect (id, sel, arg0);
	} else if (sel == OS.sel_reflectScrolledClipView_) {
		widget.reflectScrolledClipView (id, sel, arg0);
	} else if (sel == OS.sel_accessibilityHitTest_) {
		NSPoint point = new NSPoint();
		OS.memmove(point, arg0, NSPoint.sizeof);
		return widget.accessibilityHitTest(id, sel, point);
	} else if (sel == OS.sel_accessibilityAttributeValue_) {
		return widget.accessibilityAttributeValue(id, sel, arg0);
	} else if (sel == OS.sel_accessibilityIsAttributeSettable_) {
		return (widget.accessibilityIsAttributeSettable(id, sel, arg0) ? 1 : 0);
	} else if (sel == OS.sel_accessibilityPerformAction_) {
		widget.accessibilityPerformAction(id, sel, arg0);
	} else if (sel == OS.sel_accessibilityActionDescription_) {
		widget.accessibilityActionDescription(id, sel, arg0);
	} else if (sel == OS.sel_makeFirstResponder_) {
		return widget.makeFirstResponder(id, sel, arg0) ? 1 : 0;
	} else if (sel == OS.sel_tableViewColumnDidMove_) {
		widget.tableViewColumnDidMove(id, sel, arg0);
	} else if (sel == OS.sel_tableViewColumnDidResize_) {
		widget.tableViewColumnDidResize(id, sel, arg0);
	} else if (sel == OS.sel_outlineViewColumnDidMove_) {
		widget.outlineViewColumnDidMove(id, sel, arg0);
	} else if (sel == OS.sel_outlineViewColumnDidResize_) {
		widget.outlineViewColumnDidResize(id, sel, arg0);
	} else if (sel == OS.sel_setNeedsDisplay_) {
		widget.setNeedsDisplay(id, sel, arg0 != 0);
	} else if (sel == OS.sel_setNeedsDisplayInRect_) {
		widget.setNeedsDisplayInRect(id, sel, arg0);
	} else if (sel == OS.sel_setImage_) {
		widget.setImage(id, sel, arg0);
	} else if (sel == OS.sel_imageRectForBounds_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		rect = widget.imageRectForBounds(id, sel, rect);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRect.sizeof);
		OS.memmove (result, rect, NSRect.sizeof);
		return result;
	} else if (sel == OS.sel_titleRectForBounds_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		rect = widget.titleRectForBounds(id, sel, rect);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRect.sizeof);
		OS.memmove (result, rect, NSRect.sizeof);
		return result;
	}
	return 0;
}

static int /*long*/ windowDelegateProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	Widget widget = GetWidget(id);
	if (widget == null) return 0;
	if (sel == OS.sel_tabView_willSelectTabViewItem_) {
		widget.tabView_willSelectTabViewItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_tabView_didSelectTabViewItem_) {
		widget.tabView_didSelectTabViewItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_outlineView_isItemExpandable_) {
		return widget.outlineView_isItemExpandable(id, sel, arg0, arg1) ? 1 : 0;
	} else if (sel == OS.sel_outlineView_numberOfChildrenOfItem_) {
		return widget.outlineView_numberOfChildrenOfItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_outlineView_shouldCollapseItem_) {
		return widget.outlineView_shouldCollapseItem(id, sel, arg0, arg1) ? 1 : 0;
	} else if (sel == OS.sel_outlineView_shouldExpandItem_) {
		return widget.outlineView_shouldExpandItem(id, sel, arg0, arg1) ? 1 : 0;
	} else if (sel == OS.sel_menu_willHighlightItem_) {
		widget.menu_willHighlightItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_setMarkedText_selectedRange_) {
		widget.setMarkedText_selectedRange (id, sel, arg0, arg1);
	} else if (sel == OS.sel_drawInteriorWithFrame_inView_) {
		widget.drawInteriorWithFrame_inView (id, sel, arg0, arg1);
	} else if (sel == OS.sel_drawWithFrame_inView_) {
		widget.drawWithFrame_inView (id, sel, arg0, arg1);
	} else if (sel == OS.sel_accessibilityAttributeValue_forParameter_) {
		return widget.accessibilityAttributeValue_forParameter(id, sel, arg0, arg1);
	} else if (sel == OS.sel_tableView_didClickTableColumn_) {
		widget.tableView_didClickTableColumn (id, sel, arg0, arg1);
	} else if (sel == OS.sel_outlineView_didClickTableColumn_) {
		widget.outlineView_didClickTableColumn (id, sel, arg0, arg1);
	} else if (sel == OS.sel_shouldChangeTextInRange_replacementString_) {
		return widget.shouldChangeTextInRange_replacementString(id, sel, arg0, arg1) ? 1 : 0;
	}
	return 0;
}

static int /*long*/ windowDelegateProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	Widget widget = GetWidget(id);
	if (widget == null) return 0;
	if (sel == OS.sel_tableView_objectValueForTableColumn_row_) {
		return widget.tableView_objectValueForTableColumn_row(id, sel, arg0, arg1, arg2);
	} else if (sel == OS.sel_tableView_shouldEditTableColumn_row_) {
		return widget.tableView_shouldEditTableColumn_row(id, sel, arg0, arg1, arg2) ? 1 : 0;
	} else if (sel == OS.sel_textView_clickedOnLink_atIndex_) {
		 return widget.textView_clickOnLink_atIndex(id, sel, arg0, arg1, arg2) ? 1 : 0;
	} else if (sel == OS.sel_outlineView_child_ofItem_) {
		 return widget.outlineView_child_ofItem(id, sel, arg0, arg1, arg2);
	} else if (sel == OS.sel_outlineView_objectValueForTableColumn_byItem_) {
		 return widget.outlineView_objectValueForTableColumn_byItem(id, sel, arg0, arg1, arg2);
	} else if (sel == OS.sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_) {
		NSRange range = widget.textView_willChangeSelectionFromCharacterRange_toCharacterRange(id, sel, arg0, arg1, arg2);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRange.sizeof);
		OS.memmove (result, range, NSRange.sizeof);
		return result;
	} else if (sel == OS.sel_dragSelectionWithEvent_offset_slideBack_) {
		NSSize offset = new NSSize();
		OS.memmove(offset, arg0, NSSize.sizeof);
		return (widget.dragSelectionWithEvent(id, sel, arg0, arg1, arg2) ? 1 : 0);
	} else if (sel == OS.sel_drawImage_withFrame_inView_) {
		NSRect rect = new NSRect ();
		OS.memmove (rect, arg1, NSRect.sizeof);
		widget.drawImageWithFrameInView (id, sel, arg0, rect, arg2);
	} else if (sel == OS.sel_hitTestForEvent_inRect_ofView_) {
		NSRect rect = new NSRect ();
		OS.memmove (rect, arg1, NSRect.sizeof);
		return widget.hitTestForEvent (id, sel, arg0, rect, arg2);
	}
	return 0;
}

static int /*long*/ windowDelegateProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3) {
	Widget widget = GetWidget(id);
	if (widget == null) return 0;
	if (sel == OS.sel_tableView_willDisplayCell_forTableColumn_row_) {
		widget.tableView_willDisplayCell_forTableColumn_row(id, sel, arg0, arg1, arg2, arg3);
	} else if (sel == OS.sel_outlineView_willDisplayCell_forTableColumn_item_) {
		widget.outlineView_willDisplayCell_forTableColumn_item(id, sel, arg0, arg1, arg2, arg3);
	} else  if (sel == OS.sel_outlineView_setObjectValue_forTableColumn_byItem_) {
		widget.outlineView_setObjectValue_forTableColumn_byItem(id, sel, arg0, arg1, arg2, arg3);
	} else if (sel == OS.sel_tableView_setObjectValue_forTableColumn_row_) {
		widget.tableView_setObjectValue_forTableColumn_row(id, sel, arg0, arg1, arg2, arg3);
	} else if (sel == OS.sel_view_stringForToolTip_point_userData_) {
		return widget.view_stringForToolTip_point_userData(id, sel, arg0, arg1, arg2, arg3);
	}
	return 0;
}

}

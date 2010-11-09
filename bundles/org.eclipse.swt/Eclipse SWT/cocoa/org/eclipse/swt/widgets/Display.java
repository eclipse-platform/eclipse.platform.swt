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
 * <dd>Close, Dispose, OpenDocument, Settings, Skin</dd>
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
	
	static byte[] types = {'*','\0'};
	static int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;

	/* Windows and Events */
	Event [] eventQueue;
	EventTable eventTable, filterTable;
	boolean disposing;
	int sendEventCount;

	/* Key event management */
	int [] deadKeyState = new int[1];
	int currentKeyboardUCHRdata;
	
	/* Sync/Async Widget Communication */
	Synchronizer synchronizer;
	Thread thread;
	boolean allowTimers = true, runAsyncMessages = true;

	GCData[] contexts;

	Caret currentCaret;
	
	boolean sendEvent;
	int clickCountButton, clickCount;

	Control currentControl, trackingControl, tooltipControl;
	Widget tooltipTarget;
	
	NSMutableArray isPainting, needsDisplay, needsDisplayInRect, runLoopModes;
	
	NSDictionary markedAttributes;
	
	/* Fonts */
	boolean smallFonts;
	NSFont buttonFont, popUpButtonFont, textFieldFont, secureTextFieldFont;
	NSFont searchFieldFont, comboBoxFont, sliderFont, scrollerFont;
	NSFont textViewFont, tableViewFont, outlineViewFont, datePickerFont;
	NSFont boxFont, tabViewFont, progressIndicatorFont;

	Shell [] modalShells;
	Dialog modalDialog;
	
	Menu menuBar;
	Menu[] menus, popups;

	NSApplication application;
	int /*long*/ applicationClass;
	NSImage dockImage;
	boolean isEmbedded;
	static boolean launched = false;
	int systemUIMode, systemUIOptions;
	
	/* Focus */
	Control focusControl, currentFocusControl;
	int focusEvent;
	
	NSWindow screenWindow, keyWindow;

	NSAutoreleasePool[] pools;
	int poolCount, loopCount;

	int[] screenID = new int[32];
	NSPoint[] screenCascade = new NSPoint[32];
	
	int /*long*/ runLoopObserver;
	Callback observerCallback;
	
	boolean lockCursor = true;
	int /*long*/ oldCursorSetProc;
	Callback cursorSetCallback;

	// the following Callbacks are never freed
	static Callback windowCallback2, windowCallback3, windowCallback4, windowCallback5, windowCallback6;
	static Callback dialogCallback3, dialogCallback4, dialogCallback5;
	static Callback applicationCallback2, applicationCallback3, applicationCallback4, applicationCallback6;
	static Callback performKeyEquivalentCallback;

	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Deferred Layout list */
	Composite[] layoutDeferred;
	int layoutDeferredCount;

	/* System Tray */
	Tray tray;
	TrayItem currentTrayItem;
	Menu trayItemMenu;
	
	/* Main menu bar and application menu */
	Menu appMenuBar;

	/* TaskBar */
	TaskBar taskBar;
	
	/* System Resources */
	Image errorImage, infoImage, warningImage;
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];
	
	/* System Colors */
	float /*double*/ [][] colors;
	float /*double*/ [] alternateSelectedControlTextColor, selectedControlTextColor;
	float /*double*/ [] alternateSelectedControlColor, secondarySelectedControlColor;

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
		{106, SWT.F16},
		{64, SWT.F17},
		{79, SWT.F18},
		{80, SWT.F19},
//		{??, SWT.F20},
		
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

	static String APP_NAME;
	static String APP_VERSION = ""; //$NON-NLS-1$
	static final String ADD_WIDGET_KEY = "org.eclipse.swt.internal.addWidget"; //$NON-NLS-1$
	static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
	static final byte[] SWT_EMBED_FRAMES = {'S', 'W', 'T', '_', 'E', 'M', 'B', 'E', 'D', '_', 'F', 'R', 'A', 'M', 'E', 'S', '\0'};
	static final byte[] SWT_IMAGE = {'S', 'W', 'T', '_', 'I', 'M', 'A', 'G', 'E', '\0'};
	static final byte[] SWT_ROW = {'S', 'W', 'T', '_', 'R', 'O', 'W', '\0'};
	static final byte[] SWT_COLUMN = {'S', 'W', 'T', '_', 'C', 'O', 'L', 'U', 'M', 'N', '\0'};
	
	static final String SET_MODAL_DIALOG = "org.eclipse.swt.internal.modalDialog"; //$NON-NLS-1$

	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [4];
	
	/* Skinning support */
	static final int GROW_SIZE = 1024;
	Widget [] skinList = new Widget [GROW_SIZE];
	int skinCount;
	
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";
			
	/* Timer */
	Runnable timerList [];
	NSTimer nsTimers [];
	SWTWindowDelegate timerDelegate;
	static SWTApplicationDelegate applicationDelegate;
	static NSObject currAppDelegate;
	
	/* Settings */
	boolean runSettings;
	SWTWindowDelegate settingsDelegate;

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
		if (contexts[i] == null || contexts [i] == context) {
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

void addPool () {
	addPool ((NSAutoreleasePool)new NSAutoreleasePool().alloc().init());
}

void addPool (NSAutoreleasePool pool) {
	if (pools == null) pools = new NSAutoreleasePool [4];
	if (poolCount == pools.length) {
		NSAutoreleasePool[] temp = new NSAutoreleasePool [poolCount + 4];
		System.arraycopy (pools, 0, temp, 0, poolCount);
		pools = temp;
	}
	if (poolCount == 0) {
		NSMutableDictionary dictionary = NSThread.currentThread().threadDictionary();
		dictionary.setObject(NSNumber.numberWithInteger(pool.id), NSString.stringWith("SWT_NSAutoreleasePool"));
	}
	pools [poolCount++] = pool;
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

void addSkinnableWidget (Widget widget) {
	if (skinCount >= skinList.length) {
		Widget[] newSkinWidgets = new Widget [skinList.length + GROW_SIZE];
		System.arraycopy (skinList, 0, newSkinWidgets, 0, skinList.length);
		skinList = newSkinWidgets;
	}
	skinList [skinCount++] = widget;
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

void checkFocus () {
	Control oldControl = currentFocusControl;
	Control newControl = getFocusControl ();
	if (oldControl != newControl) {
		if (oldControl != null && !oldControl.isDisposed ()) {
			oldControl.sendFocusEvent (SWT.FocusOut);
		}
		currentFocusControl = newControl;
		if (newControl != null && !newControl.isDisposed ()) {
			newControl.sendFocusEvent (SWT.FocusIn);
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

static NSRect convertRect(NSScreen screen, NSRect frame) {
	float /*double*/ scaleFactor = screen.userSpaceScaleFactor();
	frame.x /= scaleFactor;
	frame.y /= scaleFactor;
	frame.width /= scaleFactor;
	frame.height /= scaleFactor;
	return frame;
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

void clearPool () {
	if (sendEventCount == 0 && loopCount == poolCount - 1 && Callback.getEntryCount () == 0) {
		removePool ();
		addPool ();
	}
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
	
	if (!NSThread.isMainThread()) {
		System.out.println ("***WARNING: Display must be created on main thread due to Cocoa restrictions."); //$NON-NLS-1$
		error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	
	NSMutableDictionary dictionary = nsthread.threadDictionary();
	NSString key = NSString.stringWith("SWT_NSAutoreleasePool");
	NSNumber id = new NSNumber(dictionary.objectForKey(key));
	addPool(new NSAutoreleasePool(id.integerValue()));

	application = NSApplication.sharedApplication();
	isEmbedded = application.isRunning();

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
		int /*long*/ ptr = getApplicationName().UTF8String();
		if (ptr != 0) OS.CPSSetProcessName (psn, ptr);
		if (!isBundled ()) {
			OS.TransformProcessType (psn, OS.kProcessTransformToForegroundApplication);
			OS.SetFrontProcess (psn);
		}
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
		applicationCallback4 = new Callback(clazz, "applicationProc", 4);
		int /*long*/ proc4 = applicationCallback4.getAddress();
		if (proc4 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		applicationCallback6 = new Callback(clazz, "applicationProc", 6);
		int /*long*/ proc6 = applicationCallback6.getAddress();
		if (proc6 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		cls = OS.objc_allocateClassPair(OS.object_getClass(application.id), className, 0);
		OS.class_addMethod(cls, OS.sel_sendEvent_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_nextEventMatchingMask_untilDate_inMode_dequeue_, proc6, "@:i@@B");
		OS.class_addMethod(cls, OS.sel_isRunning, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_finishLaunching, proc2, "@:");
		OS.objc_registerClassPair(cls);
	}
	applicationClass = OS.object_setClass(application.id, cls);
		
	className = "SWTApplicationDelegate";
	if (OS.objc_lookUpClass (className) == 0) {
		int /*long*/ appProc3 = applicationCallback3.getAddress();
		if (appProc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
		int /*long*/ appProc4 = applicationCallback4.getAddress();
		if (appProc4 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
		OS.class_addMethod(cls, OS.sel_applicationWillFinishLaunching_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_terminate_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_orderFrontStandardAboutPanel_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_hideOtherApplications_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_hide_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_unhideAllApplications_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_applicationDidBecomeActive_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_applicationDidResignActive_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_applicationDockMenu_, appProc3, "@:@");
		OS.class_addMethod(cls, OS.sel_application_openFile_, appProc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_application_openFiles_, appProc4, "@:@@");
		OS.class_addMethod(cls, OS.sel_applicationShouldHandleReopen_hasVisibleWindows_, appProc4, "@:@B");
		OS.class_addMethod(cls, OS.sel_applicationShouldTerminate_, appProc3, "@:@");
		OS.objc_registerClassPair(cls);
	}

	int[] bufferMode = new int[1], bufferOptions = new int[1];
	OS.GetSystemUIMode(bufferMode, bufferOptions);
	systemUIMode = bufferMode[0];
	systemUIOptions = bufferOptions[0];	
}

void createMainMenu () {
	NSString appName = getApplicationName();
	NSString emptyStr = NSString.string();
	NSMenu mainMenu = (NSMenu)new NSMenu().alloc();
	mainMenu.initWithTitle(emptyStr);
	
	NSMenuItem menuItem;
	NSMenu appleMenu;
	NSString format = NSString.stringWith("%@ %@"), title;
	
	NSMenuItem appItem = menuItem = mainMenu.addItemWithTitle(emptyStr, 0, emptyStr);
	appleMenu = (NSMenu)new NSMenu().alloc();
	appleMenu.initWithTitle(emptyStr);	
	OS.objc_msgSend(application.id, OS.sel_registerName("setAppleMenu:"), appleMenu.id);
	
	title = new NSString(OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithFormat_, format.id, NSString.stringWith(SWT.getMessage("About")).id, appName.id));
	menuItem = appleMenu.addItemWithTitle(title, OS.sel_orderFrontStandardAboutPanel_, emptyStr);
	menuItem.setTarget(applicationDelegate);
	
	appleMenu.addItem(NSMenuItem.separatorItem());
	
	title = NSString.stringWith(SWT.getMessage("Preferences..."));
	menuItem = appleMenu.addItemWithTitle(title, 0, NSString.stringWith(","));

	/* 
	 * Through the magic of nib decompilation, the prefs item must have a tag of 42
	 * or else the AWT won't be able to find it.
	 */
	menuItem.setTag(42);

	appleMenu.addItem(NSMenuItem.separatorItem());
	
	title = NSString.stringWith(SWT.getMessage("Services"));
	menuItem = appleMenu.addItemWithTitle(title, 0, emptyStr);
	NSMenu servicesMenu = (NSMenu)new NSMenu().alloc();
	servicesMenu.initWithTitle(emptyStr);
	appleMenu.setSubmenu(servicesMenu, menuItem);
	servicesMenu.release();	
	application.setServicesMenu(servicesMenu);
	
	appleMenu.addItem(NSMenuItem.separatorItem());
	
	title = new NSString(OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithFormat_, format.id, NSString.stringWith(SWT.getMessage("Hide")).id, appName.id));
	menuItem = appleMenu.addItemWithTitle(title, OS.sel_hide_, NSString.stringWith("h"));
	menuItem.setTarget(applicationDelegate);
	
	title = NSString.stringWith(SWT.getMessage("Hide Others"));
	menuItem = appleMenu.addItemWithTitle(title, OS.sel_hideOtherApplications_, NSString.stringWith("h"));
	menuItem.setKeyEquivalentModifierMask(OS.NSCommandKeyMask | OS.NSAlternateKeyMask);
	menuItem.setTarget(applicationDelegate);
	
	title = NSString.stringWith(SWT.getMessage("Show All"));
	menuItem = appleMenu.addItemWithTitle(title, OS.sel_unhideAllApplications_, emptyStr);
	menuItem.setTarget(applicationDelegate);
	
	appleMenu.addItem(NSMenuItem.separatorItem());
	
	title = new NSString(OS.objc_msgSend(OS.class_NSString, OS.sel_stringWithFormat_, format.id, NSString.stringWith(SWT.getMessage("Quit")).id, appName.id));
	menuItem = appleMenu.addItemWithTitle(title, OS.sel_applicationShouldTerminate_, NSString.stringWith("q"));
	menuItem.setTarget(applicationDelegate);
	
	mainMenu.setSubmenu(appleMenu, appItem);
	appleMenu.release();
	application.setMainMenu(mainMenu);
	mainMenu.release();
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
 * 
 * @noreference This method is not intended to be referenced by clients.
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
 * @noreference This method is not intended to be referenced by clients.
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
	NSWindow window = keyWindow != null ? keyWindow : application.keyWindow();
	if (window != null) {
		Widget widget = getWidget(window.contentView());
		if (widget instanceof Shell) {
			return (Shell)widget;
		}
		
		// Embedded shell test: If the NSWindow isn't an SWTWindow walk up the
		// hierarchy from the hit view to see if some view maps to a Shell.
		NSPoint windowLocation = window.mouseLocationOutsideOfEventStream();
		NSView hitView = window.contentView().hitTest(windowLocation);
		while (hitView != null) {
			widget = getWidget(hitView.id);
			if (widget instanceof Shell) {
				break;
			}
			hitView = hitView.superview();
		}
		return (Shell)widget;
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
	NSScreen screen = new NSScreen(screens.objectAtIndex(0));
	NSRect primaryFrame = convertRect(screen, screen.frame());
	float /*double*/ minX = Float.MAX_VALUE, maxX = Float.MIN_VALUE;
	float /*double*/ minY = Float.MAX_VALUE, maxY = Float.MIN_VALUE;
	int /*long*/ count = screens.count();
	for (int i = 0; i < count; i++) {
		screen = new NSScreen(screens.objectAtIndex(i));
		NSRect frame = convertRect(screen, screen.frame());
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
	NSRect frame = convertRect(screen, screen.frame());;
	NSRect visibleFrame = convertRect(screen, screen.visibleFrame());
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
	if (focusControl != null && !focusControl.isDisposed ()) {
		return focusControl;
	}
	NSWindow window = keyWindow != null ? keyWindow : application.keyWindow();
	return _getFocusControl(window);
}

Control _getFocusControl (NSWindow window) {
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
	if (event == null) return 0;
	double timestamp = event.timestamp() * 1000;
	while (timestamp > 0x7FFFFFFF) {
		timestamp -= 0x7FFFFFFF;
	}
	return (int)timestamp;
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

Dialog getModalDialog () {
	return modalDialog;
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
	NSScreen screen = new NSScreen(screens.objectAtIndex(0));
	NSRect primaryFrame = convertRect(screen, screen.frame());
	int count = (int)/*64*/screens.count();
	Monitor [] monitors = new Monitor [count];
	for (int i=0; i<count; i++) {
		Monitor monitor = new Monitor ();
		screen = new NSScreen(screens.objectAtIndex(i));
		NSRect frame = convertRect(screen, screen.frame());
		monitor.handle = screen.id;
		monitor.x = (int)frame.x;
		monitor.y = (int)(primaryFrame.height - (frame.y + frame.height));
		monitor.width = (int)frame.width;
		monitor.height = (int)frame.height;
		NSRect visibleFrame = convertRect(screen, screen.visibleFrame());
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
	NSRect frame = convertRect(screen, screen.frame());
	monitor.handle = screen.id;
	monitor.x = (int)frame.x;
	monitor.y = (int)(frame.height - (frame.y + frame.height));
	monitor.width = (int)frame.width;
	monitor.height = (int)frame.height;
	NSRect visibleFrame = convertRect(screen, screen.visibleFrame());
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

static boolean getSheetEnabled () {
	return !"false".equals(System.getProperty("org.eclipse.swt.sheet"));
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
	if (0 <= id && id < colors.length && colors [id] != null) {
		return Color.cocoa_new (this, colors [id]);
	}
	return null;
}

float /*double*/ [] getWidgetColorRGB (int id) {
	NSColor color = null;
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: color = NSColor.blackColor (); break;
		case SWT.COLOR_INFO_BACKGROUND: return new float /*double*/ [] {.984f, .988f, 0.773f, 1};
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
	return getWidgetColorRGB (color);
}

float /*double*/ [] getWidgetColorRGB (NSColor color) {
	if (color == null) return null;
	color = color.colorUsingColorSpaceName(OS.NSCalibratedRGBColorSpace);
	if (color == null) return null;
	float /*double*/[] components = new float /*double*/[(int)/*64*/color.numberOfComponents()];
	color.getComponents(components);	
	return new float /*double*/ []{components[0], components[1], components[2], components[3]};
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

NSImage getSystemImageForID(int osType) {
	int /*long*/ iconRef[] = new int /*long*/ [1];
	OS.GetIconRefFromTypeInfo(OS.kSystemIconsCreator, osType, 0, 0, 0, iconRef);
	NSImage nsImage = (NSImage)new NSImage().alloc();
	nsImage = nsImage.initWithIconRef(iconRef[0]);
	/*
	 * Feature in Cocoa. GetIconRefFromTypeInfo returns a huge icon that scales well.  Resize
	 * it to 32x32, which is what NSWorkspace does.
	 */
	NSSize size = new NSSize();
	size.width = size.height = 32.0f;
	nsImage.setSize(size);
	nsImage.setScalesWhenResized(true);
	return nsImage;
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
			NSImage img = getSystemImageForID(OS.kAlertStopIcon);
			return errorImage = Image.cocoa_new (this, SWT.ICON, img);
		}
		case SWT.ICON_INFORMATION:
		case SWT.ICON_QUESTION:
		case SWT.ICON_WORKING: {
			if (infoImage != null) return infoImage;
			NSImage img = getSystemImageForID(OS.kAlertNoteIcon);
			return infoImage = Image.cocoa_new (this, SWT.ICON, img);
		}
		case SWT.ICON_WARNING: {
			if (warningImage != null) return warningImage;
			NSImage img = getSystemImageForID(OS.kAlertCautionIcon);
			return warningImage = Image.cocoa_new (this, SWT.ICON, img);
		}
	}
	return null;
}

/**
 * Returns the single instance of the application menu bar or null
 * when there is no application menu bar for the platform.
 *
 * @return the application menu bar or <code>null</code>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.7
 */
public Menu getAppMenuBar () {
	checkDevice ();
	if (appMenuBar != null) return appMenuBar;
	appMenuBar = new Menu (this);
	
	// If there is an active menu bar don't replace it.
	// It will be updated when the next Shell without a menu bar activates.
	if (menuBar == null) setMenuBar(appMenuBar);
	
	return appMenuBar;
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
 * Returns the single instance of the system taskBar or null
 * when there is no system taskBar available for the platform.
 *
 * @return the system taskBar or <code>null</code>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.6
 */
public TaskBar getSystemTaskBar () {
	checkDevice ();
	if (taskBar != null) return taskBar;
	taskBar = new TaskBar (this, SWT.NONE);
	return taskBar;
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
	initColors ();
	initFonts ();
	
	/*
	 * Create an application delegate for app-level notifications.  The AWT may have already set a delegate;
	 * if so, hold on to it so messages can be forwarded to it.
	 */
	if (applicationDelegate == null) {
		applicationDelegate = (SWTApplicationDelegate)new SWTApplicationDelegate().alloc().init();
		
		if (currAppDelegate == null) {
			if (OS.class_JRSAppKitAWT != 0) {
				int /*long*/ currDelegatePtr = OS.objc_msgSend(OS.class_JRSAppKitAWT, OS.sel_awtAppDelegate);
				if (currDelegatePtr != 0) {
					currAppDelegate = new NSObject(currDelegatePtr);
					currAppDelegate.retain();
				}
			}
			application.setDelegate(applicationDelegate);
		} else {
			// TODO: register for notification to find out when AWT finishes loading.  Waiting on new value from Apple.
		}
	}
	
	/*
	 * Feature in Cocoa:  NSApplication.finishLaunching() adds an apple menu to the menu bar that isn't accessible via NSMenu.
	 * If Display objects are created and disposed of multiple times in a single process, another apple menu is added to the menu bar.
	 * It must be called or the dock icon will continue to bounce. So, it should only be called once per process, not just once per
	 * creation of a Display.  Use a static so creation of additional Display objects won't affect the menu bar. 
	 */
	if (!Display.launched) {
		application.finishLaunching();
		Display.launched = true;

		/* only add the shutdown hook once */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				NSApplication.sharedApplication().terminate(null);
			}
		});
	}
	
	/*
	 * Call init to force the AWT delegate to re-attach itself to the application menu. 
	 */
	if (currAppDelegate != null) {
		currAppDelegate.init();
	} 
	
	observerCallback = new Callback (this, "observerProc", 3); //$NON-NLS-1$
	int /*long*/ observerProc = observerCallback.getAddress ();
	if (observerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	int activities = OS.kCFRunLoopBeforeWaiting;
	runLoopObserver = OS.CFRunLoopObserverCreate (0, activities, true, 0, observerProc, 0);
	if (runLoopObserver == 0) error (SWT.ERROR_NO_HANDLES);
	OS.CFRunLoopAddObserver (OS.CFRunLoopGetCurrent (), runLoopObserver, OS.kCFRunLoopCommonModes ());

	// Add AWT Runloop mode for SWT/AWT.
	int /*long*/ cls = OS.objc_lookUpClass("JNFRunLoop"); //$NON-NLS-1$
	if (cls != 0) {
		int /*long*/ mode = OS.objc_msgSend(cls, OS.sel_javaRunLoopMode);
		if (mode != 0) {
			OS.CFRunLoopAddObserver (OS.CFRunLoopGetCurrent (), runLoopObserver, mode);
		}
	}
	
	cursorSetCallback = new Callback(this, "cursorSetProc", 2);
	int /*long*/ cursorSetProc = cursorSetCallback.getAddress();
	if (cursorSetProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	int /*long*/ method = OS.class_getInstanceMethod(OS.class_NSCursor, OS.sel_set);
	if (method != 0) oldCursorSetProc = OS.method_setImplementation(method, cursorSetProc);
		
	timerDelegate = (SWTWindowDelegate)new SWTWindowDelegate().alloc().init();

	settingsDelegate = (SWTWindowDelegate)new SWTWindowDelegate().alloc().init();
	NSNotificationCenter defaultCenter = NSNotificationCenter.defaultCenter();
	defaultCenter.addObserver(settingsDelegate, OS.sel_systemSettingsChanged_, OS.NSSystemColorsDidChangeNotification, null);
	defaultCenter.addObserver(settingsDelegate, OS.sel_systemSettingsChanged_, OS.NSApplicationDidChangeScreenParametersNotification, null);
	
	NSTextView textView = (NSTextView)new NSTextView().alloc();
	textView.init ();
	markedAttributes = textView.markedTextAttributes ();
	markedAttributes.retain ();
	textView.release ();
	
	isPainting = (NSMutableArray)new NSMutableArray().alloc();
	isPainting = isPainting.initWithCapacity(12);
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
		OS.class_addMethod(cls, OS.sel_changeColor_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_cancelOperation_, proc3, "@:@");
	}
	if (proc2 != 0) {
		OS.class_addMethod(cls, OS.sel_resignFirstResponder, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_becomeFirstResponder, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_resetCursorRects, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_updateTrackingAreas, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_getImageView, proc2, "@:");
		OS.class_addMethod(cls, OS.sel_mouseDownCanMoveWindow, proc2, "@:");
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
	OS.class_addMethod(cls, OS.sel_accessibilityHitTest_, accessibilityHitTestProc, "@:{NSPoint}");
	OS.class_addMethod(cls, OS.sel_accessibilityAttributeValue_forParameter_, proc4, "@:@@");	
	OS.class_addMethod(cls, OS.sel_accessibilityPerformAction_, proc3, "@:@");	
	OS.class_addMethod(cls, OS.sel_accessibilityActionDescription_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_accessibilityIsAttributeSettable_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_accessibilitySetValue_forAttribute_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_accessibleHandle, proc2, "@:");
}

int /*long*/ registerCellSubclass(int /*long*/ cellClass, int size, int align, byte[] types) {
	String cellClassName = OS.class_getName(cellClass);
	int /*long*/ cls = OS.objc_allocateClassPair(cellClass, "SWTAccessible" + cellClassName, 0);	
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.objc_registerClassPair(cls);
	return cls;
}

int /*long*/ createWindowSubclass(int /*long*/ baseClass, String newClass) {
	int /*long*/ cls = OS.objc_lookUpClass(newClass);
	if (cls != 0) return cls;
	cls = OS.objc_allocateClassPair(baseClass, newClass, 0);
	int /*long*/ proc3 = windowCallback3.getAddress();
	int /*long*/ proc2 = windowCallback2.getAddress();
	int /*long*/ proc4 = windowCallback4.getAddress();
	int /*long*/ proc6 = windowCallback6.getAddress();
	int /*long*/ view_stringForToolTip_point_userDataProc = OS.CALLBACK_view_stringForToolTip_point_userData_(proc6);
	int /*long*/ accessibilityHitTestProc = OS.CALLBACK_accessibilityHitTest_(proc3);

	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addIvar(cls, SWT_EMBED_FRAMES, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendEvent_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_helpRequested_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_canBecomeKeyWindow, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_becomeKeyWindow, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_makeFirstResponder_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_noResponderFor_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_view_stringForToolTip_point_userData_, view_stringForToolTip_point_userDataProc, "@:@i{NSPoint}@");
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	return cls;	
}

void initClasses () {
	if (OS.objc_lookUpClass ("SWTView") != 0) return;
	
	Class clazz = getClass ();
	dialogCallback3 = new Callback(clazz, "dialogProc", 3);
	int /*long*/ dialogProc3 = dialogCallback3.getAddress();
	if (dialogProc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	dialogCallback4 = new Callback(clazz, "dialogProc", 4);
	int /*long*/ dialogProc4 = dialogCallback4.getAddress();
	if (dialogProc4 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);	
	dialogCallback5 = new Callback(clazz, "dialogProc", 5);
	int /*long*/ dialogProc5 = dialogCallback5.getAddress();
	if (dialogProc5 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);	
	windowCallback3 = new Callback(clazz, "windowProc", 3);
	int /*long*/ proc3 = windowCallback3.getAddress();
	if (proc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowCallback2 = new Callback(clazz, "windowProc", 2);
	int /*long*/ proc2 = windowCallback2.getAddress();
	if (proc2 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowCallback4 = new Callback(clazz, "windowProc", 4);
	int /*long*/ proc4 = windowCallback4.getAddress();
	if (proc4 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowCallback5 = new Callback(clazz, "windowProc", 5);
	int /*long*/ proc5 = windowCallback5.getAddress();
	if (proc5 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowCallback6 = new Callback(clazz, "windowProc", 6);
	int /*long*/ proc6 = windowCallback6.getAddress();
	if (proc6 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	int /*long*/ isFlippedProc = OS.isFlipped_CALLBACK();
	int /*long*/ drawRectProc = OS.CALLBACK_drawRect_(proc3);
	int /*long*/ drawInteriorWithFrameInViewProc = OS.CALLBACK_drawInteriorWithFrame_inView_ (proc4);
	int /*long*/ drawWithExpansionFrameProc = OS.CALLBACK_drawWithExpansionFrame_inView_ (proc4);
	int /*long*/ imageRectForBoundsProc = OS.CALLBACK_imageRectForBounds_ (proc3);
	int /*long*/ titleRectForBoundsProc = OS.CALLBACK_titleRectForBounds_ (proc3);
	int /*long*/ cellSizeForBoundsProc = OS.CALLBACK_cellSizeForBounds_ (proc3);
	int /*long*/ hitTestForEvent_inRect_ofViewProc = OS.CALLBACK_hitTestForEvent_inRect_ofView_ (proc5);
	int /*long*/ cellSizeProc = OS.CALLBACK_cellSize (proc2);
	int /*long*/ drawImageWithFrameInViewProc = OS.CALLBACK_drawImage_withFrame_inView_ (proc5);
	int /*long*/ drawTitleWithFrameInViewProc = OS.CALLBACK_drawTitle_withFrame_inView_ (proc5);
	int /*long*/ setFrameOriginProc = OS.CALLBACK_setFrameOrigin_(proc3);
	int /*long*/ setFrameSizeProc = OS.CALLBACK_setFrameSize_(proc3);
	int /*long*/ hitTestProc = OS.CALLBACK_hitTest_(proc3);
	int /*long*/ markedRangeProc = OS.CALLBACK_markedRange (proc2);
	int /*long*/ selectedRangeProc = OS.CALLBACK_selectedRange (proc2);
	int /*long*/ highlightSelectionInClipRectProc = OS.CALLBACK_highlightSelectionInClipRect_ (proc3);
	int /*long*/ setMarkedText_selectedRangeProc = OS.CALLBACK_setMarkedText_selectedRange_(proc4);
	int /*long*/ attributedSubstringFromRangeProc = OS.CALLBACK_attributedSubstringFromRange_(proc3);
	int /*long*/ characterIndexForPointProc = OS.CALLBACK_characterIndexForPoint_(proc3);
	int /*long*/ firstRectForCharacterRangeProc = OS.CALLBACK_firstRectForCharacterRange_(proc3);	
	int /*long*/ textWillChangeSelectionProc = OS.CALLBACK_textView_willChangeSelectionFromCharacterRange_toCharacterRange_(proc5);
	int /*long*/ accessibilityHitTestProc = OS.CALLBACK_accessibilityHitTest_(proc3);
	int /*long*/ shouldChangeTextInRange_replacementString_Proc = OS.CALLBACK_shouldChangeTextInRange_replacementString_(proc4);
	int /*long*/ view_stringForToolTip_point_userDataProc = OS.CALLBACK_view_stringForToolTip_point_userData_(proc6);
	int /*long*/ canDragRowsWithIndexes_atPoint_Proc = OS.CALLBACK_canDragRowsWithIndexes_atPoint_(proc4);
	int /*long*/ setNeedsDisplayInRectProc = OS.CALLBACK_setNeedsDisplayInRect_(proc3);
	int /*long*/ expansionFrameWithFrameProc = OS.CALLBACK_expansionFrameWithFrame_inView_ (proc4);
	int /*long*/ sizeOfLabelProc = OS.CALLBACK_sizeOfLabel_ (proc3);
	int /*long*/ drawLabelInRectProc = OS.CALLBACK_drawLabel_inRect_ (proc4);
	int /*long*/ drawViewBackgroundInRectProc = OS.CALLBACK_drawViewBackgroundInRect_(proc3);
	int /*long*/ drawBackgroundInClipRectProc = OS.CALLBACK_drawBackgroundInClipRect_(proc3);
	int /*long*/ scrollClipView_ToPointProc = OS.CALLBACK_scrollClipView_toPoint_(proc4);
	int /*long*/ headerRectOfColumnProc = OS.CALLBACK_headerRectOfColumn_(proc3);
	int /*long*/ columnAtPointProc = OS.CALLBACK_columnAtPoint_(proc3);

	String className;
	int /*long*/ cls;
	
	className = "SWTBox";
	cls = OS.objc_allocateClassPair(OS.class_NSBox, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTButton";
	cls = OS.objc_allocateClassPair(OS.class_NSButton, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_validateMenuItem_, proc3, "@:@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSButton.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	OS.class_addMethod(cls, OS.sel_nextState, proc2, "@:");
	NSButton.setCellClass(cls);

	className = "SWTButtonCell";
	cls = OS.objc_allocateClassPair (OS.class_NSButtonCell, className, 0);
	OS.class_addIvar (cls, SWT_OBJECT, size, (byte)align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	OS.class_addMethod (cls, OS.sel_drawImage_withFrame_inView_, drawImageWithFrameInViewProc, "@:@{NSRect}@");
	OS.class_addMethod (cls, OS.sel_drawTitle_withFrame_inView_, drawTitleWithFrameInViewProc, "@:@{NSRect}@");
	OS.class_addMethod(cls, OS.sel_drawInteriorWithFrame_inView_, drawInteriorWithFrameInViewProc, "@:{NSRect}@");
	OS.class_addMethod(cls, OS.sel_titleRectForBounds_, titleRectForBoundsProc, "@:{NSRect}");
	OS.class_addMethod(cls, OS.sel_cellSizeForBounds_, cellSizeForBoundsProc, "@:{NSRect}");
	OS.objc_registerClassPair (cls);

	className = "SWTCanvasView";
	cls = OS.objc_allocateClassPair(OS.class_NSView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	//NSTextInput protocol
	OS.class_addProtocol(cls, OS.protocol_NSTextInput);
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
	//NSTextInput protocol end
	OS.class_addMethod(cls, OS.sel_canBecomeKeyView, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_isFlipped, isFlippedProc, "@:");
	OS.class_addMethod(cls, OS.sel_acceptsFirstResponder, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_isOpaque, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_updateOpenGLContext_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_clearDeferFlushing, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_validRequestorForSendType_returnType_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_readSelectionFromPasteboard_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_writeSelectionToPasteboard_types_, proc4, "@:@@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	className = "SWTComboBox";
	cls = OS.objc_allocateClassPair(OS.class_NSComboBox, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_textDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textViewDidChangeSelection_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_comboBoxSelectionDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_comboBoxWillDismiss_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_comboBoxWillPopUp_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_, textWillChangeSelectionProc, "@:@{NSRange}{NSRange}");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSComboBox.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	OS.class_addMethod(cls, OS.sel_setObjectValue_, proc3, "@:@");
	NSComboBox.setCellClass(cls);

	className = "SWTDatePicker";
	cls = OS.objc_allocateClassPair(OS.class_NSDatePicker, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_isFlipped, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_sendVerticalSelection, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTEditorView";
	cls = OS.objc_allocateClassPair(OS.class_NSTextView, className, 0);
	//TODO hitTestProc should be set Control.setRegion()? 
	addEventMethods(cls, 0, proc3, drawRectProc, 0, 0);
	OS.class_addMethod(cls, OS.sel_insertText_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_doCommandBySelector_, proc3, "@::");
	OS.class_addMethod(cls, OS.sel_shouldChangeTextInRange_replacementString_, shouldChangeTextInRange_replacementString_Proc, "@:{NSRange}@");
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

	className = "SWTImageTextCell";
	cls = OS.objc_allocateClassPair (OS.class_NSTextFieldCell, className, 0);
	OS.class_addIvar (cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addIvar (cls, SWT_IMAGE, size, (byte)align, types);
	OS.class_addIvar (cls, SWT_ROW, size, (byte)align, types);
	OS.class_addIvar (cls, SWT_COLUMN, size, (byte)align, types);
	OS.class_addMethod (cls, OS.sel_drawInteriorWithFrame_inView_, drawInteriorWithFrameInViewProc, "@:{NSRect}@");
	OS.class_addMethod (cls, OS.sel_drawWithExpansionFrame_inView_, drawWithExpansionFrameProc, "@:{NSRect}@");
	OS.class_addMethod (cls, OS.sel_imageRectForBounds_, imageRectForBoundsProc, "@:{NSRect}");
	OS.class_addMethod (cls, OS.sel_titleRectForBounds_, titleRectForBoundsProc, "@:{NSRect}");
	OS.class_addMethod (cls, OS.sel_hitTestForEvent_inRect_ofView_, hitTestForEvent_inRect_ofViewProc, "@:@{NSRect}@");
	OS.class_addMethod (cls, OS.sel_cellSize, cellSizeProc, "@:");
	OS.class_addMethod (cls, OS.sel_image, proc2, "@:");
	OS.class_addMethod (cls, OS.sel_setImage_, proc3, "@:@");
	OS.class_addMethod (cls, OS.sel_expansionFrameWithFrame_inView_, expansionFrameWithFrameProc, "@:{NSRect}@");
	OS.objc_registerClassPair (cls);

	className = "SWTMenu";
	cls = OS.objc_allocateClassPair(OS.class_NSMenu, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_menuWillOpen_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_menuDidClose_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_menu_willHighlightItem_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_menuNeedsUpdate_, proc3, "@:@");
	OS.objc_registerClassPair(cls);
	
	className = "SWTMenuItem";
	cls = OS.objc_allocateClassPair(OS.class_NSMenuItem, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	OS.objc_registerClassPair(cls);

	className = "SWTOutlineView";
	cls = OS.objc_allocateClassPair(OS.class_NSOutlineView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_highlightSelectionInClipRect_, highlightSelectionInClipRectProc, "@:{NSRect}");
	OS.class_addMethod(cls, OS.sel_sendDoubleSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_outlineViewSelectionDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineViewSelectionIsChanging_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineView_child_ofItem_, proc5, "@:@i@");
	OS.class_addMethod(cls, OS.sel_outlineView_isItemExpandable_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_outlineView_numberOfChildrenOfItem_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_outlineView_selectionIndexesForProposedSelection_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_outlineView_objectValueForTableColumn_byItem_, proc5, "@:@@@");
	OS.class_addMethod(cls, OS.sel_outlineView_willDisplayCell_forTableColumn_item_, proc6, "@:@@@@");
	OS.class_addMethod(cls, OS.sel_outlineView_setObjectValue_forTableColumn_byItem_, proc6, "@:@@@@");
	OS.class_addMethod(cls, OS.sel_outlineView_shouldEditTableColumn_item_, proc5, "@:@@@");
	OS.class_addMethod(cls, OS.sel_outlineViewColumnDidMove_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineViewColumnDidResize_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_outlineView_didClickTableColumn_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_canDragRowsWithIndexes_atPoint_, canDragRowsWithIndexes_atPoint_Proc, "@:@{NSPoint=ff}");
	OS.class_addMethod(cls, OS.sel_outlineView_writeItems_toPasteboard_, proc5, "@:@@@");
	OS.class_addMethod(cls, OS.sel_expandItem_expandChildren_, proc4, "@:@Z");
	OS.class_addMethod(cls, OS.sel_collapseItem_collapseChildren_, proc4, "@:@Z");
	OS.class_addMethod(cls, OS.sel_drawBackgroundInClipRect_, drawBackgroundInClipRectProc, "@:{NSRect}");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

	className = "SWTPanelDelegate";
	cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_windowWillClose_, dialogProc3, "@:@");
	OS.class_addMethod(cls, OS.sel_changeColor_, dialogProc3, "@:@");
	OS.class_addMethod(cls, OS.sel_setColor_forAttribute_, dialogProc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_changeFont_, dialogProc3, "@:@");
	OS.class_addMethod(cls, OS.sel_sendSelection_, dialogProc3, "@:@");
	OS.class_addMethod(cls, OS.sel_panel_shouldShowFilename_, dialogProc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_panelDidEnd_returnCode_contextInfo_, dialogProc5, "@:@i@");
	OS.objc_registerClassPair(cls);

	className = "SWTPopUpButton";
	cls = OS.objc_allocateClassPair(OS.class_NSPopUpButton, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_menuWillOpen_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_menuDidClose_, proc3, "@:@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	cls = registerCellSubclass(NSPopUpButton.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSPopUpButton.setCellClass(cls);
	
	className = "SWTProgressIndicator";
	cls = OS.objc_allocateClassPair(OS.class_NSProgressIndicator, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_viewDidMoveToWindow, proc2, "@:");
	OS.class_addMethod(cls, OS.sel__drawThemeProgressArea_, proc3, "@:c");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls); 

	className = "SWTScroller";
	cls = OS.objc_allocateClassPair(OS.class_NSScroller, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
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
	OS.class_addMethod(cls, OS.sel_scrollClipView_toPoint_, scrollClipView_ToPointProc, "@:@{NSPoint}");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);

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

	// Don't subclass NSSecureTextFieldCell -- you'll get an NSException from [NSSecureTextField setCellClass:]!
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
	
	int /*long*/ nsSecureTextViewClass = OS.objc_lookUpClass("NSSecureTextView");
	if (nsSecureTextViewClass != 0) {
		className = "SWTSecureEditorView";
		cls = OS.objc_allocateClassPair(nsSecureTextViewClass, className, 0);
		//TODO hitTestProc and drawRectProc should be set Control.setRegion()? 
		addEventMethods(cls, 0, proc3, drawRectProc, 0, 0);
		OS.class_addMethod(cls, OS.sel_insertText_, proc3, "@:@");
		OS.class_addMethod(cls, OS.sel_doCommandBySelector_, proc3, "@::");
		OS.class_addMethod(cls, OS.sel_shouldChangeTextInRange_replacementString_, shouldChangeTextInRange_replacementString_Proc, "@:{NSRange}@");
		OS.objc_registerClassPair(cls);
	}
	
	className = "SWTSlider";
	cls = OS.objc_allocateClassPair(OS.class_NSSlider, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendSelection, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls); 
	
	cls = registerCellSubclass(NSSlider.cellClass(), size, align, types);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSSlider.setCellClass(cls);

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

	className = "SWTTableHeaderCell";
	cls = OS.objc_allocateClassPair (OS.class_NSTableHeaderCell, className, 0);
	OS.class_addIvar (cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod (cls, OS.sel_drawInteriorWithFrame_inView_, drawInteriorWithFrameInViewProc, "@:{NSRect}@");
	OS.objc_registerClassPair (cls);

	className = "SWTTableHeaderView";
	cls = OS.objc_allocateClassPair(OS.class_NSTableHeaderView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_mouseDown_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_resetCursorRects, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_updateTrackingAreas, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_menuForEvent_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_headerRectOfColumn_, headerRectOfColumnProc, "@:i");
	OS.class_addMethod(cls, OS.sel_columnAtPoint_, columnAtPointProc, "@:{NSPoint}");
	//TODO hitTestProc and drawRectProc should be set Control.setRegion()? 
	OS.objc_registerClassPair(cls);

	className = "SWTTableView";
	cls = OS.objc_allocateClassPair(OS.class_NSTableView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_highlightSelectionInClipRect_, highlightSelectionInClipRectProc, "@:{NSRect}");
	OS.class_addMethod(cls, OS.sel_sendDoubleSelection, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_numberOfRowsInTableView_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableView_objectValueForTableColumn_row_, proc5, "@:@@i");
	OS.class_addMethod(cls, OS.sel_tableView_shouldEditTableColumn_row_, proc5, "@:@@i");
	OS.class_addMethod(cls, OS.sel_tableViewSelectionIsChanging_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableViewSelectionDidChange_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableView_willDisplayCell_forTableColumn_row_, proc6, "@:@@@i");
	OS.class_addMethod(cls, OS.sel_tableView_setObjectValue_forTableColumn_row_, proc6, "@:@@@i");
	OS.class_addMethod(cls, OS.sel_tableViewColumnDidMove_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableViewColumnDidResize_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_tableView_didClickTableColumn_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_tableView_selectionIndexesForProposedSelection_, proc4, "@:@@");
	OS.class_addMethod(cls, OS.sel_canDragRowsWithIndexes_atPoint_, canDragRowsWithIndexes_atPoint_Proc, "@:@{NSPoint=ff}");
	OS.class_addMethod(cls, OS.sel_tableView_writeRowsWithIndexes_toPasteboard_, proc5, "@:@@@");
	OS.class_addMethod(cls, OS.sel_drawBackgroundInClipRect_, drawBackgroundInClipRectProc, "@:{NSRect}");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
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
	
	className = "SWTTabViewItem";
	cls = OS.objc_allocateClassPair(OS.class_NSTabViewItem, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sizeOfLabel_, sizeOfLabelProc, "@::");
	OS.class_addMethod(cls, OS.sel_drawLabel_inRect_, drawLabelInRectProc, "@::{NSRect}");
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
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
	OS.class_addMethod(cls, OS.sel_drawViewBackgroundInRect_, drawViewBackgroundInRectProc, "@:{NSRect}");
	OS.class_addMethod(cls, OS.sel_shouldDrawInsertionPoint, proc2, "@:");
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
	OS.class_addMethod(cls, OS.sel_drawInteriorWithFrame_inView_, drawInteriorWithFrameInViewProc, "@:{NSRect}@");
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);	
	NSTextField.setCellClass(cls);

	className = "SWTTreeItem";
	cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.objc_registerClassPair(cls);

	className = "SWTView";
	cls = OS.objc_allocateClassPair(OS.class_NSView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_canBecomeKeyView, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_isFlipped, isFlippedProc, "@:");
	OS.class_addMethod(cls, OS.sel_acceptsFirstResponder, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_isOpaque, proc2, "@:");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTWindow";
	createWindowSubclass(OS.class_NSWindow, className);
	
	className = "SWTPanel";
	cls = OS.objc_allocateClassPair(OS.class_NSPanel, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_sendEvent_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_helpRequested_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_canBecomeKeyWindow, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_becomeKeyWindow, proc2, "@:");
	OS.class_addMethod(cls, OS.sel_makeFirstResponder_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_noResponderFor_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_view_stringForToolTip_point_userData_, view_stringForToolTip_point_userDataProc, "@:@i{NSPoint}@");
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTToolbar";
	cls = OS.objc_allocateClassPair(OS.class_NSToolbar, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_toolbar_itemForItemIdentifier_willBeInsertedIntoToolbar_, proc5, "@:@@Z");
	OS.class_addMethod(cls, OS.sel_toolbarAllowedItemIdentifiers_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_toolbarDefaultItemIdentifiers_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_toolbarSelectableItemIdentifiers_, proc3, "@:@");
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	addAccessibilityMethods(cls, proc2, proc3, proc4, accessibilityHitTestProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTToolbarView";
	cls = OS.objc_allocateClassPair(OS.class_NSToolbarView, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	addEventMethods(cls, proc2, proc3, drawRectProc, hitTestProc, setNeedsDisplayInRectProc);
	addFrameMethods(cls, setFrameOriginProc, setFrameSizeProc);
	OS.objc_registerClassPair(cls);
	
	className = "SWTWindowDelegate";
	cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_windowDidResize_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidMove_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowShouldClose_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowWillClose_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidResignKey_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidBecomeKey_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_timerProc_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_systemSettingsChanged_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidMiniaturize_, proc3, "@:@");
	OS.class_addMethod(cls, OS.sel_windowDidDeminiaturize_, proc3, "@:@");
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

void initColors () {
	colors = new float /*double*/ [SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT + 1][];
	colors[SWT.COLOR_INFO_FOREGROUND] = getWidgetColorRGB(SWT.COLOR_INFO_FOREGROUND);
	colors[SWT.COLOR_INFO_BACKGROUND] = getWidgetColorRGB(SWT.COLOR_INFO_BACKGROUND);
	colors[SWT.COLOR_TITLE_FOREGROUND] = getWidgetColorRGB(SWT.COLOR_TITLE_FOREGROUND);
	colors[SWT.COLOR_TITLE_BACKGROUND] = getWidgetColorRGB(SWT.COLOR_TITLE_BACKGROUND);
	colors[SWT.COLOR_TITLE_BACKGROUND_GRADIENT] = getWidgetColorRGB(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	colors[SWT.COLOR_TITLE_INACTIVE_FOREGROUND] = getWidgetColorRGB(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
	colors[SWT.COLOR_TITLE_INACTIVE_BACKGROUND] = getWidgetColorRGB(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	colors[SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT] = getWidgetColorRGB(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
	colors[SWT.COLOR_WIDGET_DARK_SHADOW] = getWidgetColorRGB(SWT.COLOR_WIDGET_DARK_SHADOW);
	colors[SWT.COLOR_WIDGET_NORMAL_SHADOW] = getWidgetColorRGB(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	colors[SWT.COLOR_WIDGET_LIGHT_SHADOW] = getWidgetColorRGB(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	colors[SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW] = getWidgetColorRGB(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	colors[SWT.COLOR_WIDGET_BACKGROUND] = getWidgetColorRGB(SWT.COLOR_WIDGET_BACKGROUND);
	colors[SWT.COLOR_WIDGET_FOREGROUND] = getWidgetColorRGB(SWT.COLOR_WIDGET_FOREGROUND);
	colors[SWT.COLOR_WIDGET_BORDER] = getWidgetColorRGB(SWT.COLOR_WIDGET_BORDER);
	colors[SWT.COLOR_LIST_FOREGROUND] = getWidgetColorRGB(SWT.COLOR_LIST_FOREGROUND);
	colors[SWT.COLOR_LIST_BACKGROUND] = getWidgetColorRGB(SWT.COLOR_LIST_BACKGROUND);
	colors[SWT.COLOR_LIST_SELECTION_TEXT] = getWidgetColorRGB(SWT.COLOR_LIST_SELECTION_TEXT);
	colors[SWT.COLOR_LIST_SELECTION] = getWidgetColorRGB(SWT.COLOR_LIST_SELECTION);

	alternateSelectedControlColor = getWidgetColorRGB(NSColor.alternateSelectedControlColor());
	alternateSelectedControlTextColor = getWidgetColorRGB(NSColor.alternateSelectedControlTextColor());
	secondarySelectedControlColor = getWidgetColorRGB(NSColor.secondarySelectedControlColor());
	selectedControlTextColor = getWidgetColorRGB(NSColor.selectedControlTextColor());
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
 * 
 * @noreference This method is not intended to be referenced by clients.
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
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public void internal_dispose_GC (int /*long*/ context, GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	
}

boolean isBundled () {
	NSBundle mainBundle = NSBundle.mainBundle();
	if (mainBundle != null) {
		NSDictionary info = mainBundle.infoDictionary();
		if (info != null) {
			return NSString.stringWith("APPL").isEqual(info.objectForKey(NSString.stringWith("CFBundlePackageType"))); //$NON-NLS-1$ $NON-NLS-2$
		}
	}
	return false;
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
public boolean post(Event event) {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
		int /*long*/ eventRef = 0;
		int /*long*/ eventSource = OS.CGEventSourceCreate(OS.kCGEventSourceStateHIDSystemState);
		if (eventSource == 0) return false;

		int type = event.type;
		switch (type) {
			case SWT.KeyDown:
			case SWT.KeyUp: {
				short vKey = (short)Display.untranslateKey (event.keyCode);
				if (vKey == 0) {
					int /*long*/ uchrPtr = 0;
					int /*long*/ currentKbd = OS.TISCopyCurrentKeyboardInputSource();
					int /*long*/ uchrCFData = OS.TISGetInputSourceProperty(currentKbd, OS.kTISPropertyUnicodeKeyLayoutData());
					
					if (uchrCFData == 0) return false;
					uchrPtr = OS.CFDataGetBytePtr(uchrCFData);
					if (uchrPtr == 0) return false;
					if (OS.CFDataGetLength(uchrCFData) == 0) return false;
					int maxStringLength = 256;
					vKey = -1;
					char [] output = new char [maxStringLength];
					int [] actualStringLength = new int [1];
					for (short i = 0 ; i <= 0x7F ; i++) {
						OS.UCKeyTranslate (uchrPtr, i, (short)(type == SWT.KeyDown ? OS.kUCKeyActionDown : OS.kUCKeyActionUp), 0, OS.LMGetKbdType(), 0, deadKeyState, maxStringLength, actualStringLength, output);
						if (output[0] == event.character) {
							vKey = i;
							break;
						}
					}
					if (vKey == -1) {
						for (short i = 0 ; i <= 0x7F ; i++) {
							OS.UCKeyTranslate (uchrPtr, i, (short)(type == SWT.KeyDown ? OS.kUCKeyActionDown : OS.kUCKeyActionUp), OS.shiftKey, OS.LMGetKbdType(), 0, deadKeyState, maxStringLength, actualStringLength, output);
							if (output[0] == event.character) {
								vKey = i;
								break;
							}
						}
					}
				}

				/**
				 * Bug(?) in UCKeyTranslate:  If event.keyCode doesn't map to a valid SWT constant and event.characer is 0 we still need to post an event.
				 * In Carbon, KeyTranslate eventually found a key that generated 0 but UCKeyTranslate never generates 0.
				 * When that happens, post an event from key 127, which does nothing.
				 */
				if (vKey == -1 && event.character == 0) {
					vKey = 127;
				}
				
				if (vKey != -1) {
					eventRef = OS.CGEventCreateKeyboardEvent(eventSource, vKey, type == SWT.KeyDown);
				}
				break;
			}
			case SWT.MouseDown:
			case SWT.MouseMove: 
			case SWT.MouseUp: {
				CGPoint mouseCursorPosition = new CGPoint ();

				if (type == SWT.MouseMove) {
					mouseCursorPosition.x = event.x;
					mouseCursorPosition.y = event.y;
					eventRef = OS.CGEventCreateMouseEvent(eventSource, OS.kCGEventMouseMoved, mouseCursorPosition, 0);
				} else {
	 				NSPoint nsCursorPosition = NSEvent.mouseLocation();
	 				NSRect primaryFrame = getPrimaryFrame();
	 				mouseCursorPosition.x = nsCursorPosition.x;
	 				mouseCursorPosition.y = (int) (primaryFrame.height - nsCursorPosition.y);
	 				int eventType = 0;
	 				int button = event.button;
	 				switch (button) {
	 				case 1:
	 					eventType = (event.type == SWT.MouseDown ? OS.kCGEventLeftMouseDown : OS.kCGEventLeftMouseUp);
	 					break;
	 				case 2:
	 					eventType = (event.type == SWT.MouseDown ? OS.kCGEventRightMouseDown : OS.kCGEventRightMouseUp);
	 					break;
	 				default:
	 					eventType = (event.type == SWT.MouseDown ? OS.kCGEventOtherMouseDown : OS.kCGEventOtherMouseUp);
	 					break;
	 				}

	 				// SWT buttons are 1-based; CG buttons are 0 based.
	 				button -= 1;
					eventRef = OS.CGEventCreateMouseEvent(eventSource, eventType, mouseCursorPosition, button);
				}
				break;
			}
			case SWT.MouseWheel: {
				// CG does not support scrolling a page at a time. Technically that is a page up/down, but not a scroll-wheel event. 
				eventRef = OS.CGEventCreateScrollWheelEvent(eventSource, OS.kCGScrollEventUnitLine, 1, event.count);
				break;
			}
		} 
		
		boolean returnValue = false;
		
		if (eventRef != 0) {
			OS.CGEventPost(0, eventRef);
			OS.CFRelease(eventRef);
			returnValue = true;
		}
		
		if (eventSource != 0) OS.CFRelease(eventSource);		
		return returnValue;
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
		if (!from.view.isFlipped ()) {
			pt.y = from.view.bounds().height - pt.y;
		}
		pt = from.view.convertPoint_toView_(pt, to.view);
		if (!to.view.isFlipped ()) {
			pt.y = to.view.bounds().height - pt.y;
		}
	} else {
		NSRect primaryFrame = getPrimaryFrame();
		if (from != null) {
			NSView view = from.eventView ();
			if (!view.isFlipped ()) {
				pt.y = view.bounds().height - pt.y;
			}
			pt = view.convertPoint_toView_(pt, null);
			pt = fromWindow.convertBaseToScreen(pt);
			pt.y = primaryFrame.height - pt.y;
			float /*double*/ scaleFactor = fromWindow.userSpaceScaleFactor();
			pt.x /= scaleFactor;
			pt.y /= scaleFactor;
		}
		if (to != null) {
			NSView view = to.eventView ();
			float /*double*/ scaleFactor = toWindow.userSpaceScaleFactor();
			pt.x *= scaleFactor;
			pt.y = primaryFrame.height - (pt.y * scaleFactor);
			pt = toWindow.convertScreenToBase(pt);
			pt = view.convertPoint_fromView_(pt, null);
			if (!view.isFlipped ()) {
				pt.y = view.bounds().height - pt.y;
			}
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
		if (!from.view.isFlipped ()) {
			pt.y = from.view.bounds().height - pt.y;
		}
		pt = from.view.convertPoint_toView_(pt, to.view);
		if (!to.view.isFlipped ()) {
			pt.y = to.view.bounds().height - pt.y;
		}
	} else {
		NSRect primaryFrame = getPrimaryFrame();
		if (from != null) {
			NSView view = from.eventView ();
			if (!view.isFlipped ()) {
				pt.y = view.bounds().height - pt.y;
			}
			pt = view.convertPoint_toView_(pt, null);
			pt = fromWindow.convertBaseToScreen(pt);
			pt.y = primaryFrame.height - pt.y;
			float /*double*/ scaleFactor = fromWindow.userSpaceScaleFactor();
			pt.x /= scaleFactor;
			pt.y /= scaleFactor;
		}
		if (to != null) {
			NSView view = to.eventView ();
			float /*double*/ scaleFactor = toWindow.userSpaceScaleFactor();
			pt.x *= scaleFactor;
			pt.y = primaryFrame.height - (pt.y * scaleFactor);
			pt = toWindow.convertScreenToBase(pt);
			pt = view.convertPoint_fromView_(pt, null);
			if (!view.isFlipped ()) {
				pt.y = view.bounds().height - pt.y;
			}
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

static int /*long*/ performKeyEquivalent(int /*long*/ id, int /*long*/ sel, int /*long*/ arg) {
	NSEvent nsEvent = new NSEvent(arg);
	int stateMask = 0;
	int /*long*/ selector = 0;
	int /*long*/ modifierFlags = nsEvent.modifierFlags();
	if ((modifierFlags & OS.NSAlternateKeyMask) != 0) stateMask |= SWT.ALT;
	if ((modifierFlags & OS.NSShiftKeyMask) != 0) stateMask |= SWT.SHIFT;
	if ((modifierFlags & OS.NSControlKeyMask) != 0) stateMask |= SWT.CONTROL;
	if ((modifierFlags & OS.NSCommandKeyMask) != 0) stateMask |= SWT.COMMAND;
	if (stateMask == SWT.COMMAND) {
		short keyCode = nsEvent.keyCode ();
		switch (keyCode) {
			case 7: /* X */
				selector = OS.sel_cut_;
				break;
			case 8: /* C */
				selector = OS.sel_copy_;
				break;
			case 9: /* V */
				selector = OS.sel_paste_;
				break;
			case 0: /* A */
				selector = OS.sel_selectAll_;
				break;
		}
		
		if (selector != 0) {
			NSApplication.sharedApplication().sendAction(selector, null, NSApplication.sharedApplication());
			return 1;
		}
	}

	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	return OS.objc_msgSendSuper(super_struct, sel, arg);
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
	if (sendEventCount == 0 && loopCount == poolCount - 1 && Callback.getEntryCount () == 0) removePool ();
	addPool ();
	runSkin ();
	runDeferredLayouts ();
	loopCount++;
	boolean events = false;
	try {
		events |= runSettings ();
		events |= runTimers ();
		events |= runContexts ();
		events |= runPopups ();
		NSEvent event = application.nextEventMatchingMask(0, null, OS.NSDefaultRunLoopMode, true);
		if (event != null) {
			events = true;
			application.sendEvent(event);
		}
		events |= runPaint ();
		events |= runDeferredEvents ();
		if (!events) {
			events = isDisposed () || runAsyncMessages (false);
		}
	} finally {
		removePool ();
		loopCount--;
		if (sendEventCount == 0 && loopCount == poolCount && Callback.getEntryCount () == 0) addPool ();
	}
	return events;
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
	if (taskBar != null) taskBar.dispose ();
	taskBar = null;
	while (readAndDispatch ()) {}
	if (disposeList != null) {
		for (int i=0; i<disposeList.length; i++) {
			if (disposeList [i] != null) disposeList [i].run ();
		}
	}
	disposeList = null;
	synchronizer.releaseSynchronizer ();
	synchronizer = null;
	if (appMenuBar != null) appMenuBar.dispose();
	appMenuBar = null;
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
	if (runLoopModes != null) runLoopModes.release();
	needsDisplay = needsDisplayInRect = isPainting = runLoopModes = null;
	
	modalShells = null;
	modalDialog = null;
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

	if (settingsDelegate != null) {
		NSNotificationCenter.defaultCenter().removeObserver(settingsDelegate);
		settingsDelegate.release();
	}
	settingsDelegate = null;
	
	// Clear the menu bar if we created it.
	if (!isEmbedded) {
		//remove all existing menu items except the application menu
		NSMenu menubar = application.mainMenu();
		int /*long*/ count = menubar.numberOfItems();
		while (count > 1) {
			menubar.removeItemAtIndex(count - 1);
			count--;
		}
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

void removePool () {
	NSAutoreleasePool pool = pools [poolCount - 1];
	pools [--poolCount] = null;
	if (poolCount == 0) {
		NSMutableDictionary dictionary = NSThread.currentThread().threadDictionary();
		dictionary.removeObjectForKey(NSString.stringWith("SWT_NSAutoreleasePool"));
	}
	pool.release ();
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

NSArray runLoopModes() {
	if (runLoopModes == null) {
		runLoopModes = NSMutableArray.arrayWithCapacity(3);
		runLoopModes.addObject(OS.NSEventTrackingRunLoopMode);
		runLoopModes.addObject(OS.NSDefaultRunLoopMode);
		runLoopModes.addObject(OS.NSModalPanelRunLoopMode);
		runLoopModes.retain();
	}
	
	runLoopModes.retain();
	runLoopModes.autorelease();
	return runLoopModes;
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
		int length = popups.length;
		System.arraycopy (popups, 1, popups, 0, --length);
		popups [length] = null;
		runDeferredEvents ();
		if (!menu.isDisposed ()) menu._setVisible (true);
		result = true;
	}
	popups = null;
	return result;
}

boolean runSettings () {
	if (!runSettings) return false;
	runSettings = false;
	initColors ();
	sendEvent (SWT.Settings, null);
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			shell.redraw (true);
			shell.layout (true, true);
		}
	}
	return true;
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
	sendEvent(eventTable, event);
}

void sendEvent (EventTable table, Event event) {
	try {
		sendEventCount++;
		if (!filterEvent (event)) {
			if (table != null) table.sendEvent (event);
		}
	} finally {
		sendEventCount--;
	}
}

void subclassPanel(id panel, String swtClassName) {
	if (performKeyEquivalentCallback == null) performKeyEquivalentCallback = new Callback(getClass(), "performKeyEquivalent", 3);
	int /*long*/ proc = performKeyEquivalentCallback.getAddress();
	if (proc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	int /*long*/ cls = OS.object_getClass(panel.id);
	if (cls == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	// If the implementation is the callback's the class is already subclassed, so don't do it again.
	int /*long*/ procPtr = OS.class_getMethodImplementation(cls, OS.sel_performKeyEquivalent_);
	if (procPtr == proc) return;
	int /*long*/ swtPanelClass = OS.objc_getClass(swtClassName);
	if (swtPanelClass == 0) {
		swtPanelClass = OS.objc_allocateClassPair(OS.object_getClass(panel.id), swtClassName, 0);
		OS.class_addMethod(swtPanelClass, OS.sel_performKeyEquivalent_, proc, "@:@");
		OS.objc_registerClassPair(swtPanelClass);
	}
	OS.object_setClass(panel.id, swtPanelClass);
}

static NSString getApplicationName() {
	NSString name = null;
	int pid = OS.getpid ();
	int /*long*/ ptr = OS.getenv (ascii ("APP_NAME_" + pid));
	if (ptr != 0) name = NSString.stringWithUTF8String(ptr);
	if (name == null && APP_NAME != null) name = NSString.stringWith(APP_NAME);
	if (name == null) {
		id value = NSBundle.mainBundle().objectForInfoDictionaryKey(NSString.stringWith("CFBundleName"));
		if (value != null) {
			name = new NSString(value);
		}
	}
	if (name == null) {
		String macAppName = System.getProperty("com.apple.mrj.application.apple.menu.about.name");
		if (macAppName != null) name = NSString.stringWith(macAppName);
	}
	if (name == null) name = NSString.stringWith("SWT");
	return name;
}

/**
 * Returns the application name.
 *
 * @return the application name
 * 
 * @see #setAppName(String)
 * 
 * @since 3.6
 */
public static String getAppName () {
	return APP_NAME;
}

/**
 * Returns the application version.
 *
 * @return the application version
 * 
 * @see #setAppVersion(String)
 * 
 * @since 3.6
 */
public static String getAppVersion () {
	return APP_VERSION;
}

/**
 * Sets the application name to the argument.
 * <p>
 * The application name can be used in several ways,
 * depending on the platform and tools being used.
 * On Motif, for example, this can be used to set
 * the name used for resource lookup. Accessibility
 * tools may also ask for the application name.
 * </p><p>
 * Specifying <code>null</code> for the name clears it.
 * </p>
 *
 * @param name the new app name or <code>null</code>
 */
public static void setAppName (String name) {
	APP_NAME = name;
}

/**
 * Sets the application version to the argument.
 *
 * @param version the new app version
 * 
 * @since 3.6
 */
public static void setAppVersion (String version) {
	APP_VERSION = version;
}

//TODO use custom timer instead of timerExec
Runnable hoverTimer = new Runnable () {
	public void run () {
		if (currentControl != null && !currentControl.isDisposed()) {
			currentControl.sendMouseEvent (NSApplication.sharedApplication().currentEvent(), SWT.MouseHover, trackingControl != null && !trackingControl.isDisposed());
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
	
	if (key.equals(SET_MODAL_DIALOG)) {
		if (value == null) {
			this.modalDialog = null;
		} else {
			this.modalDialog = (Dialog) value;
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
	// If passed a null menu bar don't clear out the menu bar, but switch back to the 
	// application menu bar instead, if it exists.  If the app menu bar is already active
	// we jump out without harming the current menu bar.
	if (menu == null) menu = appMenuBar;
	if (menu == menuBar) return;
	menuBar = menu;
	//remove all existing menu items except the application menu
	NSMenu menubar = application.mainMenu();
	/*
	* For some reason, NSMenu.cancelTracking() does not dismisses
	* the menu right away when the menu bar is set in a stacked
	* event loop. The fix is to use CancelMenuTracking() instead.
	*/
//	menubar.cancelTracking();
	OS.CancelMenuTracking (OS.AcquireRootMenu (), true, 0);
	int /*long*/ count = menubar.numberOfItems();
	while (count > 1) {
		menubar.removeItemAtIndex(count - 1);
		count--;
	}
	//set parent of each item to NULL and add them to menubar
	if (menu != null) {
		MenuItem[] items = menu.getItems();
		for (int i = 0; i < items.length; i++) {
			MenuItem item = items[i];
			NSMenuItem nsItem = item.nsItem;
			nsItem.setMenu(null);
			menubar.addItem(nsItem);
			
			/*
			* Bug in Cocoa: Calling NSMenuItem.setEnabled() for menu item of a menu bar only
			* works when the menu bar is the current menu bar.  The underline OS menu does get
			* enabled/disable when that menu is set later on.  The fix is to toggle the
			* item enabled state to force the underline menu to be updated.  
			*/
			boolean enabled = menu.getEnabled () && item.getEnabled ();
			nsItem.setEnabled(!enabled);
			nsItem.setEnabled(enabled);
		}
	}
}

void setModalDialog (Dialog modalDialog) {
	this.modalDialog = modalDialog;
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
	try {
		addPool();
		allowTimers = runAsyncMessages = false;
		NSRunLoop.currentRunLoop().runMode(OS.NSDefaultRunLoopMode, NSDate.distantFuture());
		allowTimers = runAsyncMessages = true;
	} finally {
		removePool();
	}
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
	NSRunLoop runLoop = NSRunLoop.currentRunLoop();
	runLoop.addTimer(timer, OS.NSModalPanelRunLoopMode);
	runLoop.addTimer(timer, OS.NSEventTrackingRunLoopMode);
	timer.retain();
	if (timer != null) {
		nsTimers [index] = timer;
		timerList [index] = runnable;
	}
}

int /*long*/ timerProc (int /*long*/ id, int /*long*/ sel, int /*long*/ timerID) {
	NSTimer timer = new NSTimer (timerID);
	try {
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
	} finally {
		timer.invalidate();
		timer.release();
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
	// If we did not create the menu bar, don't modify it.
	if (isEmbedded) return;
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
		int /*long*/ quitIndex = sm.indexOfItemWithTarget(applicationDelegate, OS.sel_applicationShouldTerminate_);
		
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
	//new pool?
	NSObject object = new NSObject().alloc().init();
	object.performSelectorOnMainThread(OS.sel_release, null, false);
}

Control findControl (boolean checkTrim) {
	return findControl(checkTrim, null);
}

Control findControl (boolean checkTrim, NSView[] hitView) {
	NSView view = null;
	NSPoint screenLocation = NSEvent.mouseLocation();
	
	// Use NSWindowList instead of [NSApplication orderedWindows] because orderedWindows
	// skips NSPanels. See bug 321614.
	int /*long*/ outCount[] = new int /*long*/ [1];
	OS.NSCountWindows(outCount);
	int /*long*/ windowIDs[] = new int /*long*/ [(int)outCount[0]];
	OS.NSWindowList(outCount[0], windowIDs);

	for (int i = 0, count = windowIDs.length; i < count && view == null; i++) {
		NSWindow window = application.windowWithWindowNumber(windowIDs[i]);
		// NSWindowList returns all window numbers for all processes. If the window 
		// number passed to windowWithWindowNumber returns nil the window doesn't belong to
		// this process.
		if (window != null) {
			NSView contentView = window.contentView();
			if (contentView != null) contentView = contentView.superview();
			if (contentView != null && OS.NSPointInRect(screenLocation, window.frame())) {
				NSPoint location = window.convertScreenToBase(screenLocation);
				view = contentView.hitTest (location);
				if (view == null && !checkTrim) {
					view = contentView;
				}
				break;
			}
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

void finishLaunching (int /*long*/ id, int /*long*/ sel) {
	/* 
	* [NSApplication finishLaunching] cannot run multiple times otherwise
	* multiple main menus are added.
	*/
	if (launched) return;
	launched = true;
	objc_super super_struct = new objc_super();
	super_struct.receiver = id;
	super_struct.super_class = OS.objc_msgSend(id, OS.sel_superclass);
	OS.objc_msgSendSuper(super_struct, sel);
}

void applicationDidBecomeActive (int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	checkFocus();
	checkEnterExit(findControl(true), null, false);
}

void applicationDidResignActive (int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	checkFocus();
	checkEnterExit(null, null, false);
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
			clickCount = (int)(clickCountButton == nsEvent.buttonNumber() ? nsEvent.clickCount() : 1);
			clickCountButton = (int)nsEvent.buttonNumber();
			trackingControl.sendMouseEvent (nsEvent, SWT.MouseDown, true);
			break;
		case OS.NSLeftMouseUp:
		case OS.NSRightMouseUp:
		case OS.NSOtherMouseUp:
			checkEnterExit (findControl (true), nsEvent, true);
			if (trackingControl.isDisposed()) return;
			Control control = trackingControl;
			this.trackingControl = null;
			if (clickCount == 2) {
				control.sendMouseEvent (nsEvent, SWT.MouseDoubleClick, false);
			}
			control.sendMouseEvent (nsEvent, SWT.MouseUp, false);
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
	boolean down = false;
	switch (type) {
		case OS.NSLeftMouseDown:
		case OS.NSRightMouseDown:
		case OS.NSOtherMouseDown:
			down = true;
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
						if (down) {
							if (!application.isActive()) {
								application.activateIgnoringOtherApps(true);
							}
							NSRect rect = window.contentView().frame();
							NSPoint pt = window.convertBaseToScreen(nsEvent.locationInWindow());
							if (OS.NSPointInRect(pt, rect)) beep ();
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

void applicationWillFinishLaunching (int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	boolean loaded = false;
	
	/*
	 * Bug in AWT:  If the AWT starts up first when the VM was started on the first thread it assumes that
	 * a Carbon-based SWT will be used, so it calls NSApplicationLoad().  This causes the Carbon menu
	 * manager to create an application menu that isn't accessible via NSMenu.  It is, however, accessible
	 * via the Carbon menu manager, so find and delete the menu items it added. 
	 * 
	 * Note that this code will continue to work if Apple does change this. GetIndMenuWithCommandID will
	 * return a non-zero value indicating failure, which we ignore.
	 */
	if (isEmbedded) {
		int /*long*/ outMenu [] = new int /*long*/ [1];
		short outIndex[] = new short[1];
		int status = OS.GetIndMenuItemWithCommandID(0, OS.kHICommandHide, 1, outMenu, outIndex);
		if (status == 0) OS.DeleteMenuItem(outMenu[0], outIndex[0]);
		status = OS.GetIndMenuItemWithCommandID(0, OS.kHICommandHideOthers, 1, outMenu, outIndex);
		if (status == 0) OS.DeleteMenuItem(outMenu[0], outIndex[0]);
		status = OS.GetIndMenuItemWithCommandID(0, OS.kHICommandShowAll, 1, outMenu, outIndex);
		if (status == 0) OS.DeleteMenuItem(outMenu[0], outIndex[0]);
		status = OS.GetIndMenuItemWithCommandID(0, OS.kHICommandQuit, 1, outMenu, outIndex);
		if (status == 0) OS.DeleteMenuItem(outMenu[0], outIndex[0]);
		status = OS.GetIndMenuItemWithCommandID(0, OS.kHICommandServices, 1, outMenu, outIndex);
		if (status == 0) OS.DeleteMenuItem(outMenu[0], outIndex[0]);
	}

	NSBundle bundle = NSBundle.bundleWithIdentifier(NSString.stringWith("com.apple.JavaVM"));
	NSDictionary dict = NSDictionary.dictionaryWithObject(applicationDelegate, NSString.stringWith("NSOwner"));
	NSString path = bundle.pathForResource(NSString.stringWith("DefaultApp"), NSString.stringWith("nib"));
	if (!loaded) loaded = path != null && NSBundle.loadNibFile(path, dict, 0);
	if (!loaded) {
		NSString resourcePath = bundle.resourcePath();
		path = resourcePath != null ? resourcePath.stringByAppendingString(NSString.stringWith("/English.lproj/DefaultApp.nib")) : null;
		loaded = path != null && NSBundle.loadNibFile(path, dict, 0);
	}
	if (!loaded) {
		path = NSString.stringWith(System.getProperty("java.home") + "/../Resources/English.lproj/DefaultApp.nib");
		loaded = path != null && NSBundle.loadNibFile(path, dict, 0);
	}
	if (!loaded) {
		createMainMenu();
	}
	//replace %@ with application name
	NSMenu mainmenu = application.mainMenu();
	NSMenuItem appitem = mainmenu.itemAtIndex(0);
	if (appitem != null) {
		NSString name = getApplicationName();
		NSString match = NSString.stringWith("%@");
		appitem.setTitle(name);
		NSMenu sm = appitem.submenu();
		NSArray ia = sm.itemArray();
		for(int i = 0; i < ia.count(); i++) {
			NSMenuItem ni = new NSMenuItem(ia.objectAtIndex(i));
			NSString title = ni.title().stringByReplacingOccurrencesOfString(match, name);
			ni.setTitle(title);
		}

		int /*long*/ quitIndex = sm.indexOfItemWithTarget(applicationDelegate, OS.sel_terminate_);
		
		if (quitIndex != -1) {
			NSMenuItem quitItem = sm.itemAtIndex(quitIndex);
			quitItem.setAction(OS.sel_applicationShouldTerminate_);
		}
	}
}

static int /*long*/ applicationProc(int /*long*/ id, int /*long*/ sel) {
	//TODO optimize getting the display
	Display display = getCurrent ();
	if (display == null) {
		objc_super super_struct = new objc_super ();
		super_struct.receiver = id;
		super_struct.super_class = OS.objc_msgSend (id, OS.sel_superclass);
		return OS.objc_msgSendSuper (super_struct, sel);
	}
	if (sel == OS.sel_isRunning) {
		// #245724: [NSApplication isRunning] must return true to allow the AWT to load correctly.
		return display.isDisposed() ? 0 : 1;
	}
	if (sel == OS.sel_finishLaunching) {
		display.finishLaunching (id, sel);
	}
	return 0;
}

static int /*long*/ applicationProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	//TODO optimize getting the display
	Display display = getCurrent ();
	if (display == null && id != applicationDelegate.id) {
		objc_super super_struct = new objc_super ();
		super_struct.receiver = id;
		super_struct.super_class = OS.objc_msgSend (id, OS.sel_superclass);
		return OS.objc_msgSendSuper (super_struct, sel, arg0);
	}

	if (currAppDelegate != null) {
		if (currAppDelegate.respondsToSelector(sel)) OS.objc_msgSend(currAppDelegate.id, sel, arg0);
	}
	
	NSApplication application = display.application;
	if (sel == OS.sel_sendEvent_) {
		display.applicationSendEvent (id, sel, arg0);
	} else if (sel == OS.sel_applicationWillFinishLaunching_) {
		display.applicationWillFinishLaunching(id, sel, arg0);
	} else if (sel == OS.sel_applicationShouldTerminate_) {
		int returnVal = OS.NSTerminateCancel;
		if (!display.disposing) {
			Event event = new Event ();
			display.sendEvent (SWT.Close, event);
			if (event.doit) {
				display.dispose();
				returnVal = OS.NSTerminateNow;
			}
		}
		return returnVal;
	} else if (sel == OS.sel_orderFrontStandardAboutPanel_) {
//		application.orderFrontStandardAboutPanel(application);
	} else if (sel == OS.sel_hideOtherApplications_) {
		application.hideOtherApplications(application);
	} else if (sel == OS.sel_hide_) {
		application.hide(application);
	} else if (sel == OS.sel_unhideAllApplications_) {
		application.unhideAllApplications(application);
	} else if (sel == OS.sel_applicationDidBecomeActive_) {
		display.applicationDidBecomeActive(id, sel, arg0);
	} else if (sel == OS.sel_applicationDidResignActive_) {
		display.applicationDidResignActive(id, sel, arg0);
	} else if (sel == OS.sel_applicationDockMenu_) {
		TaskBar taskbar = display.taskBar;
		if (taskbar != null && taskbar.itemCount != 0) {
			TaskItem item = taskbar.getItem(null);
			if (item != null) {
				Menu menu = item.getMenu();
				if (menu != null && !menu.isDisposed()) {
					return menu.nsMenu.id;
				}
			}
		}
	}
	return 0;
}

static int /*long*/ applicationProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	Display display = getCurrent();

	if (display == null && id != applicationDelegate.id) {
		objc_super super_struct = new objc_super ();
		super_struct.receiver = id;
		super_struct.super_class = OS.objc_msgSend (id, OS.sel_superclass);
		return OS.objc_msgSendSuper (super_struct, sel, arg0, arg1);
	}

	// Forward to the AWT, if necessary.
	if (currAppDelegate != null) {
		if (currAppDelegate.respondsToSelector(sel)) OS.objc_msgSend(currAppDelegate.id, sel, arg0, arg1);
	} 

	if (sel == OS.sel_application_openFile_) {
		String file = new NSString(arg1).getString();
		Event event = new Event();
		event.text = file;
		display.sendEvent(SWT.OpenDocument, event);
		return 1;
	} else if (sel == OS.sel_application_openFiles_) {
		NSArray files = new NSArray(arg1);
		int /*long*/ count = files.count();
		for (int i=0; i<count; i++) {
			String file = new NSString(files.objectAtIndex(i)).getString();
			Event event = new Event();
			event.text = file;
			display.sendEvent(SWT.OpenDocument, event);
		}
		new NSApplication(arg0).replyToOpenOrPrint(OS.NSApplicationDelegateReplySuccess);
	}  else if (sel == OS.sel_applicationShouldHandleReopen_hasVisibleWindows_) {
		return 1;
	}
	return 0;
}

static int /*long*/ applicationProc(int /*long*/ id, int /*long*/sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3) {
	//TODO optimize getting the display
	Display display = getCurrent ();
	if (display == null && id != applicationDelegate.id) {
		objc_super super_struct = new objc_super ();
		super_struct.receiver = id;
		super_struct.super_class = OS.objc_msgSend (id, OS.sel_superclass);
		return OS.objc_msgSendSuper (super_struct, sel, arg0, arg1, arg2, arg3 != 0);
	}
	if (sel == OS.sel_nextEventMatchingMask_untilDate_inMode_dequeue_) {
		return display.applicationNextEventMatchingMask(id, sel, arg0, arg1, arg2, arg3);
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
	} else if (sel == OS.sel_sendSelection_) {
		FileDialog dialog = (FileDialog)OS.JNIGetObject(jniRef[0]);
		if (dialog == null) return 0;
		dialog.sendSelection(id, sel, arg0);
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

static int /*long*/ dialogProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	int /*long*/ [] jniRef = new int /*long*/ [1];
	OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
	if (jniRef[0] == 0) return 0;
	if (sel == OS.sel_panel_shouldShowFilename_) {
		FileDialog dialog = (FileDialog)OS.JNIGetObject(jniRef[0]);
		if (dialog == null) return 0;
		return dialog.panel_shouldShowFilename(id, sel, arg0, arg1);
	}
	if (sel == OS.sel_setColor_forAttribute_) {
		FontDialog dialog = (FontDialog)OS.JNIGetObject(jniRef[0]);
		if (dialog == null) return 0;
		dialog.setColor_forAttribute(id, sel, arg0, arg1);
		return 0;
	}
	return 0;
}

static int /*long*/ dialogProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	int /*long*/ [] jniRef = new int /*long*/ [1];
	OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
	if (jniRef[0] == 0) return 0;
	if (sel == OS.sel_panelDidEnd_returnCode_contextInfo_) {
		MessageBox dialog = (MessageBox)OS.JNIGetObject(jniRef[0]);
		if (dialog == null) return 0;
		dialog.panelDidEnd_returnCode_contextInfo(id, sel, arg0, arg1, arg2);
	}
	return 0;
}

static Widget LookupWidget (int /*long*/ id, int /*long*/ sel) {
	Widget widget = GetWidget(id);
	if (widget == null) {
		NSView view = new NSView (id);
		if (view.isKindOfClass(OS.class_NSView)) {
			while (widget == null && (view = view.superview ()) != null) {
				widget = GetWidget (view.id);
			}
		}
	}
	return widget;
}

static int /*long*/ windowProc(int /*long*/ id, int /*long*/ sel) {
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
	Widget widget = LookupWidget(id, sel);
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
	} else if (sel == OS.sel_isFlipped) {
		return widget.isFlipped(id, sel) ? 1 : 0;
	} else if (sel == OS.sel_canBecomeKeyView) {
		return widget.canBecomeKeyView(id,sel) ? 1 : 0;
	} else if (sel == OS.sel_becomeKeyWindow) {
		widget.becomeKeyWindow(id, sel);
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
	} else if (sel == OS.sel_getImageView) {
		return widget.imageView();
	} else if (sel == OS.sel_mouseDownCanMoveWindow) {
		return (widget.mouseDownCanMoveWindow(id, sel) ? 1 : 0);
	} else if (sel == OS.sel_accessibilityFocusedUIElement) {
		return widget.accessibilityFocusedUIElement(id, sel);
	} else if (sel == OS.sel_accessibilityIsIgnored) {
		return (widget.accessibilityIsIgnored(id, sel) ? 1 : 0);
	} else if (sel == OS.sel_nextState) {
		return widget.nextState(id, sel);
	} else if (sel == OS.sel_resetCursorRects) {
		widget.resetCursorRects(id, sel);
	} else if (sel == OS.sel_updateTrackingAreas) {
		widget.updateTrackingAreas(id, sel);
	} else if (sel == OS.sel_viewDidMoveToWindow) {
		widget.viewDidMoveToWindow(id, sel);
	} else if (sel == OS.sel_image) {
		return widget.image(id, sel);
	} else if (sel == OS.sel_shouldDrawInsertionPoint) {
		return widget.shouldDrawInsertionPoint(id, sel) ? 1 : 0;
	} else if (sel == OS.sel_accessibleHandle) {
		return widget.accessibleHandle();
	} else if (sel == OS.sel_clearDeferFlushing) {
		widget.clearDeferFlushing(id, sel);
	}
	return 0;
}

static int /*long*/ windowProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
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
	if (sel == OS.sel_changeColor_) {
		NSColorPanel panel = NSColorPanel.sharedColorPanel();
		id delegate = panel.delegate();
		if (delegate != null) {
			if (OS.objc_msgSend_bool(delegate.id, OS.sel_isKindOfClass_, OS.objc_getClass("SWTPanelDelegate"))) {
				OS.objc_msgSend(delegate.id, OS.sel_changeColor_, arg0);
			}
		}
		return 0;
	}
	if (sel == OS.sel_timerProc_) {
		//TODO optimize getting the display
		Display display = getCurrent ();
		if (display == null) return 0;
		return display.timerProc (id, sel, arg0);
	}
	if (sel == OS.sel_systemSettingsChanged_) {
		//TODO optimize getting the display
		Display display = getCurrent ();
		if (display == null) return 0;
		display.runSettings = true;
		return 0;
	}
	Widget widget = LookupWidget(id, sel);
	if (widget == null) return 0;
	if (sel == OS.sel_windowWillClose_) {
		widget.windowWillClose(id, sel, arg0);
	} else if (sel == OS.sel_drawRect_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		widget.drawRect(id, sel, rect);
	} else if (sel == OS.sel_columnAtPoint_) {
		NSPoint point = new NSPoint();
		OS.memmove(point, arg0, NSPoint.sizeof);
		return widget.columnAtPoint(id, sel, point);
	} else if (sel == OS.sel__drawThemeProgressArea_) {
		widget._drawThemeProgressArea(id, sel, arg0);
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
	} else if (sel == OS.sel_tableViewSelectionDidChange_) {
		widget.tableViewSelectionDidChange(id, sel, arg0);
	} else if (sel == OS.sel_tableViewSelectionIsChanging_) {
		widget.tableViewSelectionIsChanging(id, sel, arg0);
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
	} else if (sel == OS.sel_outlineViewSelectionIsChanging_) {
		widget.outlineViewSelectionIsChanging(id, sel, arg0);
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
	} else if (sel == OS.sel_accessibilityPerformAction_) {
		widget.accessibilityPerformAction(id, sel, arg0);
	} else if (sel == OS.sel_accessibilityActionDescription_) {
		return widget.accessibilityActionDescription(id, sel, arg0);
	} else if (sel == OS.sel_accessibilityIsAttributeSettable_) {
		return widget.accessibilityIsAttributeSettable(id, sel, arg0) ? 1 : 0;
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
	} else if (sel == OS.sel_headerRectOfColumn_) {
		NSRect rect = widget.headerRectOfColumn(id, sel, arg0);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRect.sizeof);
		OS.memmove (result, rect, NSRect.sizeof);
		return result;
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
	} else if (sel == OS.sel_cellSizeForBounds_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		NSSize size = widget.cellSizeForBounds(id, sel, rect);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSSize.sizeof);
		OS.memmove (result, size, NSSize.sizeof);
		return result;
	} else if (sel == OS.sel_setObjectValue_) {
		widget.setObjectValue(id, sel, arg0);
	} else if (sel == OS.sel_updateOpenGLContext_) {
		widget.updateOpenGLContext(id, sel, arg0);
	} else if (sel == OS.sel_sizeOfLabel_) {
		NSSize size = widget.sizeOfLabel(id, sel, arg0 != 0);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc(NSSize.sizeof);
		OS.memmove(result, size, NSSize.sizeof);
		return result;
	} else if (sel == OS.sel_comboBoxSelectionDidChange_) {
		widget.comboBoxSelectionDidChange(id, sel, arg0);
	} else if (sel == OS.sel_comboBoxWillDismiss_) {
		widget.comboBoxWillDismiss(id, sel, arg0);
	} else if (sel == OS.sel_comboBoxWillPopUp_) {
		widget.comboBoxWillPopUp(id, sel, arg0);
	} else if (sel == OS.sel_drawViewBackgroundInRect_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		widget.drawViewBackgroundInRect(id, sel, rect);
	} else if (sel == OS.sel_drawBackgroundInClipRect_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		widget.drawBackgroundInClipRect(id, sel, rect);
	} else if (sel == OS.sel_windowDidMiniaturize_) {
		widget.windowDidMiniturize(id, sel, arg0);
	} else if (sel == OS.sel_windowDidDeminiaturize_) {
		widget.windowDidDeminiturize(id, sel, arg0);
	} else if (sel == OS.sel_toolbarAllowedItemIdentifiers_) {
		return widget.toolbarAllowedItemIdentifiers(id, sel, arg0);
	} else if (sel == OS.sel_toolbarDefaultItemIdentifiers_) {
		return widget.toolbarDefaultItemIdentifiers(id, sel, arg0);
	} else if (sel == OS.sel_toolbarSelectableItemIdentifiers_) {
		return widget.toolbarSelectableItemIdentifiers(id, sel, arg0);
	} else if (sel == OS.sel_validateMenuItem_) {
		return (widget.validateMenuItem(id, sel, arg0) ? 1 : 0);
	} else if (sel == OS.sel_readSelectionFromPasteboard_) {
		return (widget.readSelectionFromPasteboard(id, sel, arg0) ? 1 : 0);
	} else if (sel == OS.sel_cancelOperation_) {
		widget.cancelOperation(id, sel, arg0);
	}
	return 0;
}

static int /*long*/ windowProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1) {
	Widget widget = LookupWidget(id, sel);
	if (widget == null) return 0;
	if (sel == OS.sel_tabView_willSelectTabViewItem_) {
		widget.tabView_willSelectTabViewItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_tabView_didSelectTabViewItem_) {
		widget.tabView_didSelectTabViewItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_outlineView_isItemExpandable_) {
		return widget.outlineView_isItemExpandable(id, sel, arg0, arg1) ? 1 : 0;
	} else if (sel == OS.sel_outlineView_numberOfChildrenOfItem_) {
		return widget.outlineView_numberOfChildrenOfItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_menu_willHighlightItem_) {
		widget.menu_willHighlightItem(id, sel, arg0, arg1);
	} else if (sel == OS.sel_setMarkedText_selectedRange_) {
		widget.setMarkedText_selectedRange (id, sel, arg0, arg1);
	} else if (sel == OS.sel_drawInteriorWithFrame_inView_) {
		NSRect rect = new NSRect ();
		OS.memmove (rect, arg0, NSRect.sizeof);
		widget.drawInteriorWithFrame_inView (id, sel, rect, arg1);
	} else if (sel == OS.sel_drawWithExpansionFrame_inView_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		widget.drawWithExpansionFrame_inView (id, sel, rect, arg1);
	} else if (sel == OS.sel_accessibilityAttributeValue_forParameter_) {
		return widget.accessibilityAttributeValue_forParameter(id, sel, arg0, arg1);
	} else if (sel == OS.sel_tableView_didClickTableColumn_) {
		widget.tableView_didClickTableColumn (id, sel, arg0, arg1);
	} else if (sel == OS.sel_tableView_selectionIndexesForProposedSelection_) {
		return widget.tableView_selectionIndexesForProposedSelection(id, sel, arg0, arg1);
	} else if (sel == OS.sel_outlineView_didClickTableColumn_) {
		widget.outlineView_didClickTableColumn (id, sel, arg0, arg1);
	} else if (sel == OS.sel_outlineView_selectionIndexesForProposedSelection_) {
		return widget.outlineView_selectionIndexesForProposedSelection(id, sel, arg0, arg1);
	} else if (sel == OS.sel_shouldChangeTextInRange_replacementString_) {
		return widget.shouldChangeTextInRange_replacementString(id, sel, arg0, arg1) ? 1 : 0;
	} else if (sel == OS.sel_canDragRowsWithIndexes_atPoint_) {
		return widget.canDragRowsWithIndexes_atPoint(id, sel, arg0, arg1) ? 1 : 0;
	} else if (sel == OS.sel_expandItem_expandChildren_) {
		widget.expandItem_expandChildren(id, sel, arg0, arg1 != 0);
	} else if (sel == OS.sel_collapseItem_collapseChildren_) {
		widget.collapseItem_collapseChildren(id, sel, arg0, arg1 != 0);
	} else if (sel == OS.sel_expansionFrameWithFrame_inView_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg0, NSRect.sizeof);
		rect = widget.expansionFrameWithFrame_inView(id, sel, rect, arg1);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRect.sizeof);
		OS.memmove (result, rect, NSRect.sizeof);
		return result;
	} else if (sel == OS.sel_drawLabel_inRect_) {
		NSRect rect = new NSRect();
		OS.memmove(rect, arg1, NSRect.sizeof);
		widget.drawLabelInRect(id, sel, arg0==1, rect);
	} else if (sel == OS.sel_scrollClipView_toPoint_) {
		NSPoint point = new NSPoint();
		OS.memmove(point, arg1, NSPoint.sizeof);
		widget.scrollClipViewToPoint (id, sel, arg0, point);
	} else if (sel == OS.sel_accessibilitySetValue_forAttribute_) {
		widget.accessibilitySetValue_forAttribute(id, sel, arg0, arg1);
	} else if (sel == OS.sel_validRequestorForSendType_returnType_) {
		return widget.validRequestorForSendType(id, sel, arg0, arg1);
	} else if (sel == OS.sel_writeSelectionToPasteboard_types_) {
		return (widget.writeSelectionToPasteboard(id, sel, arg0, arg1) ? 1 : 0);
	}
	return 0;
}

static int /*long*/ windowProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	Widget widget = LookupWidget(id, sel);
	if (widget == null) return 0;
	if (sel == OS.sel_tableView_objectValueForTableColumn_row_) {
		return widget.tableView_objectValueForTableColumn_row(id, sel, arg0, arg1, arg2);
	} else if (sel == OS.sel_tableView_shouldEditTableColumn_row_) {
		return widget.tableView_shouldEditTableColumn_row(id, sel, arg0, arg1, arg2) ? 1 : 0;
	} else if (sel == OS.sel_outlineView_shouldEditTableColumn_item_) {
		return widget.outlineView_shouldEditTableColumn_row(id, sel, arg0, arg1, arg2) ? 1 : 0;
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
	} else if (sel == OS.sel_drawTitle_withFrame_inView_) {
		NSRect rect = new NSRect ();
		OS.memmove (rect, arg1, NSRect.sizeof);
		rect = widget.drawTitleWithFrameInView (id, sel, arg0, rect, arg2);
		/* NOTE that this is freed in C */
		int /*long*/ result = OS.malloc (NSRect.sizeof);
		OS.memmove (result, rect, NSRect.sizeof);
		return result;
	} else if (sel == OS.sel_hitTestForEvent_inRect_ofView_) {
		NSRect rect = new NSRect ();
		OS.memmove (rect, arg1, NSRect.sizeof);
		return widget.hitTestForEvent (id, sel, arg0, rect, arg2);
	} else if (sel == OS.sel_tableView_writeRowsWithIndexes_toPasteboard_) {
		return (widget.tableView_writeRowsWithIndexes_toPasteboard(id, sel, arg0, arg1, arg2) ? 1 : 0);
	} else if (sel == OS.sel_outlineView_writeItems_toPasteboard_) {
		return (widget.outlineView_writeItems_toPasteboard(id, sel, arg0, arg1, arg2) ? 1 : 0);
	} else if (sel == OS.sel_toolbar_itemForItemIdentifier_willBeInsertedIntoToolbar_) {
		return widget.toolbar_itemForItemIdentifier_willBeInsertedIntoToolbar(id, sel, arg0, arg1, arg2 != 0);
	}
	return 0;
}

static int /*long*/ windowProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3) {
	Widget widget = LookupWidget(id, sel);
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

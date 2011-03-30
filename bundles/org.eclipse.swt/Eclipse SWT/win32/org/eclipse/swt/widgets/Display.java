/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

	/**
	 * the handle to the OS message queue
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
	public MSG msg = new MSG ();

	static String APP_NAME = "SWT"; //$NON-NLS-1$
	static String APP_VERSION = ""; //$NON-NLS-1$
	
	/* Windows and Events */
	Event [] eventQueue;
	Callback windowCallback;
	int /*long*/ windowProc;
	int threadId;
	TCHAR windowClass, windowShadowClass, windowOwnDCClass;
	static int WindowClassCount;
	static final String WindowName = "SWT_Window"; //$NON-NLS-1$
	static final String WindowShadowName = "SWT_WindowShadow"; //$NON-NLS-1$
	static final String WindowOwnDCName = "SWT_WindowOwnDC"; //$NON-NLS-1$
	EventTable eventTable, filterTable;
	boolean useOwnDC;

	/* Widget Table */
	int freeSlot;
	int [] indexTable;
	Control lastControl, lastGetControl;
	int /*long*/ lastHwnd, lastGetHwnd;
	Control [] controlTable;
	static final int GROW_SIZE = 1024;
	static final int SWT_OBJECT_INDEX;
	static final boolean USE_PROPERTY = !OS.IsWinCE;
	static {
		if (USE_PROPERTY) {
			SWT_OBJECT_INDEX = OS.GlobalAddAtom (new TCHAR (0, "SWT_OBJECT_INDEX", true)); //$NON-NLS-1$
		} else {
			SWT_OBJECT_INDEX = 0;
		}
	}
	
	/* Startup info */
	static STARTUPINFO lpStartupInfo;
	static {
		if (!OS.IsWinCE) {
			lpStartupInfo = new STARTUPINFO ();
			lpStartupInfo.cb = STARTUPINFO.sizeof;
			OS.GetStartupInfo (lpStartupInfo);
		}
	}
	
	/* XP Themes */
	int /*long*/ hButtonTheme, hEditTheme, hExplorerBarTheme, hScrollBarTheme, hTabTheme;
	static final char [] BUTTON = new char [] {'B', 'U', 'T', 'T', 'O', 'N', 0};
	static final char [] EDIT = new char [] {'E', 'D', 'I', 'T', 0};
	static final char [] EXPLORER = new char [] {'E', 'X', 'P', 'L', 'O', 'R', 'E', 'R', 0};
	static final char [] EXPLORERBAR = new char [] {'E', 'X', 'P', 'L', 'O', 'R', 'E', 'R', 'B', 'A', 'R', 0};
	static final char [] SCROLLBAR = new char [] {'S', 'C', 'R', 'O', 'L', 'L', 'B', 'A', 'R', 0};
	static final char [] LISTVIEW = new char [] {'L', 'I', 'S', 'T', 'V', 'I', 'E', 'W', 0};
	static final char [] TAB = new char [] {'T', 'A', 'B', 0};
	static final char [] TREEVIEW = new char [] {'T', 'R', 'E', 'E', 'V', 'I', 'E', 'W', 0};
	
	/* Focus */
	int focusEvent;
	Control focusControl;
	boolean fixFocus;
	
	/* Menus */
	Menu [] bars, popups;
	MenuItem [] items;
	
	/*
	* The start value for WM_COMMAND id's.
	* Windows reserves the values 0..100.
	* 
	* The SmartPhone SWT resource file reserves
	* the values 101..107.
	*/
	static final int ID_START = 108;
	
	/* Filter Hook */
	Callback msgFilterCallback;
	int /*long*/ msgFilterProc, filterHook;
	MSG hookMsg = new MSG ();
	boolean runDragDrop = true, dragCancelled = false;
	
	/* Idle Hook */
	Callback foregroundIdleCallback;
	int /*long*/ foregroundIdleProc, idleHook;
	
	/* Message Hook and Embedding */
	boolean ignoreNextKey;
	Callback getMsgCallback, embeddedCallback;
	int /*long*/ getMsgProc, msgHook, embeddedHwnd, embeddedProc;
	static final String AWT_WINDOW_CLASS = "SunAwtWindow"; //$NON-NLS-1$
	static final short [] ACCENTS = new short [] {'~', '`', '\'', '^', '"'};

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	boolean runMessages = true, runMessagesInIdle = false, runMessagesInMessageProc = true;
	static final String RUN_MESSAGES_IN_IDLE_KEY = "org.eclipse.swt.internal.win32.runMessagesInIdle"; //$NON-NLS-1$
	static final String RUN_MESSAGES_IN_MESSAGE_PROC_KEY = "org.eclipse.swt.internal.win32.runMessagesInMessageProc"; //$NON-NLS-1$
	static final String USE_OWNDC_KEY = "org.eclipse.swt.internal.win32.useOwnDC"; //$NON-NLS-1$
	static final String ACCEL_KEY_HIT = "org.eclipse.swt.internal.win32.accelKeyHit"; //$NON-NLS-1$
	Thread thread;

	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Deferred Layout list */
	Composite[] layoutDeferred;
	int layoutDeferredCount;

	/* System Tray */
	Tray tray;
	int nextTrayId;
	
	/* TaskBar */
	TaskBar taskBar;
	static final String TASKBAR_EVENT = "/SWTINTERNAL_ID"; //$NON-NLS-1$
	static final String LAUNCHER_PREFIX = "--launcher.openFile "; //$NON-NLS-1$
	
	/* Timers */
	int /*long*/ [] timerIds;
	Runnable [] timerList;
	int /*long*/ nextTimerId = SETTINGS_ID + 1;
	
	/* Settings */
	static final int /*long*/ SETTINGS_ID = 100;
	static final int SETTINGS_DELAY = 2000;
	boolean lastHighContrast, sendSettings;
	
	/* Keyboard and Mouse */
	RECT clickRect;
	int clickCount, lastTime, lastButton;
	int /*long*/ lastClickHwnd;
	int scrollRemainder, scrollHRemainder;
	int lastKey, lastMouse, lastAscii;
	boolean lastVirtual, lastNull, lastDead;
	byte [] keyboard = new byte [256];
	boolean accelKeyHit, mnemonicKeyHit;
	boolean lockActiveWindow, captureChanged, xMouse;
	
	/* Gesture state */
	double magStartDistance, lastDistance;
	double rotationAngle;
	int lastX, lastY;
	
	/* Touch state */
	TouchSource [] touchSources;
	
	/* Tool Tips */
	int nextToolTipId;
	
	/* MDI */
	boolean ignoreRestoreFocus;
	Control lastHittestControl;
	int lastHittest;
	
	/* Message Only Window */
	Callback messageCallback;
	int /*long*/ hwndMessage, messageProc;
	
	/* System Resources */
	LOGFONT lfSystemFont;
	Font systemFont;
	Image errorImage, infoImage, questionImage, warningIcon;
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];
	Resource [] resources;
	static final int RESOURCE_SIZE = 1 + 4 + SWT.CURSOR_HAND + 1;

	/* ImageList Cache */	
	ImageList[] imageList, toolImageList, toolHotImageList, toolDisabledImageList;

	/* Custom Colors for ChooseColor */
	int /*long*/ lpCustColors;

	/* Sort Indicators */
	Image upArrow, downArrow;
	
	/* Table */
	char [] tableBuffer;
	NMHDR hdr = new NMHDR ();
	NMLVDISPINFO plvfi = new NMLVDISPINFO ();
	int /*long*/ hwndParent;
	int columnCount;
	boolean [] columnVisible;
	
	/* Resize and move recursion */
	int resizeCount;
	static final int RESIZE_LIMIT = 4;
	
	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;
	
	/* Key Mappings */
	static final int [] [] KeyTable = {
		
		/* Keyboard and Mouse Masks */
		{OS.VK_MENU,	SWT.ALT},
		{OS.VK_SHIFT,	SWT.SHIFT},
		{OS.VK_CONTROL,	SWT.CONTROL},
//		{OS.VK_????,	SWT.COMMAND},

		/* NOT CURRENTLY USED */		
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},
		
		/* Non-Numeric Keypad Keys */
		{OS.VK_UP,		SWT.ARROW_UP},
		{OS.VK_DOWN,	SWT.ARROW_DOWN},
		{OS.VK_LEFT,	SWT.ARROW_LEFT},
		{OS.VK_RIGHT,	SWT.ARROW_RIGHT},
		{OS.VK_PRIOR,	SWT.PAGE_UP},
		{OS.VK_NEXT,	SWT.PAGE_DOWN},
		{OS.VK_HOME,	SWT.HOME},
		{OS.VK_END,		SWT.END},
		{OS.VK_INSERT,	SWT.INSERT},

		/* Virtual and Ascii Keys */
		{OS.VK_BACK,	SWT.BS},
		{OS.VK_RETURN,	SWT.CR},
		{OS.VK_DELETE,	SWT.DEL},
		{OS.VK_ESCAPE,	SWT.ESC},
		{OS.VK_RETURN,	SWT.LF},
		{OS.VK_TAB,		SWT.TAB},
	
		/* Functions Keys */
		{OS.VK_F1,	SWT.F1},
		{OS.VK_F2,	SWT.F2},
		{OS.VK_F3,	SWT.F3},
		{OS.VK_F4,	SWT.F4},
		{OS.VK_F5,	SWT.F5},
		{OS.VK_F6,	SWT.F6},
		{OS.VK_F7,	SWT.F7},
		{OS.VK_F8,	SWT.F8},
		{OS.VK_F9,	SWT.F9},
		{OS.VK_F10,	SWT.F10},
		{OS.VK_F11,	SWT.F11},
		{OS.VK_F12,	SWT.F12},
		{OS.VK_F13,	SWT.F13},
		{OS.VK_F14,	SWT.F14},
		{OS.VK_F15,	SWT.F15},
		{OS.VK_F16,	SWT.F16},
		{OS.VK_F17,	SWT.F17},
		{OS.VK_F18,	SWT.F18},
		{OS.VK_F19,	SWT.F19},
		{OS.VK_F20,	SWT.F20},
		
		/* Numeric Keypad Keys */
		{OS.VK_MULTIPLY,	SWT.KEYPAD_MULTIPLY},
		{OS.VK_ADD,			SWT.KEYPAD_ADD},
		{OS.VK_RETURN,		SWT.KEYPAD_CR},
		{OS.VK_SUBTRACT,	SWT.KEYPAD_SUBTRACT},
		{OS.VK_DECIMAL,		SWT.KEYPAD_DECIMAL},
		{OS.VK_DIVIDE,		SWT.KEYPAD_DIVIDE},
		{OS.VK_NUMPAD0,		SWT.KEYPAD_0},
		{OS.VK_NUMPAD1,		SWT.KEYPAD_1},
		{OS.VK_NUMPAD2,		SWT.KEYPAD_2},
		{OS.VK_NUMPAD3,		SWT.KEYPAD_3},
		{OS.VK_NUMPAD4,		SWT.KEYPAD_4},
		{OS.VK_NUMPAD5,		SWT.KEYPAD_5},
		{OS.VK_NUMPAD6,		SWT.KEYPAD_6},
		{OS.VK_NUMPAD7,		SWT.KEYPAD_7},
		{OS.VK_NUMPAD8,		SWT.KEYPAD_8},
		{OS.VK_NUMPAD9,		SWT.KEYPAD_9},
//		{OS.VK_????,		SWT.KEYPAD_EQUAL},

		/* Other keys */
		{OS.VK_CAPITAL,		SWT.CAPS_LOCK},
		{OS.VK_NUMLOCK,		SWT.NUM_LOCK},
		{OS.VK_SCROLL,		SWT.SCROLL_LOCK},
		{OS.VK_PAUSE,		SWT.PAUSE},
		{OS.VK_CANCEL,		SWT.BREAK},
		{OS.VK_SNAPSHOT,	SWT.PRINT_SCREEN},
//		{OS.VK_????,		SWT.HELP},
		
	};

	/* Multiple Displays */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Multiple Monitors */
	Monitor[] monitors = null;
	int monitorCount = 0;
	
	/* Modality */
	Shell [] modalShells;
	Dialog modalDialog;
	static boolean TrimEnabled = false;

	/* Private SWT Window Messages */
	static final int SWT_GETACCELCOUNT	= OS.WM_APP;
	static final int SWT_GETACCEL 		= OS.WM_APP + 1;
	static final int SWT_KEYMSG	 		= OS.WM_APP + 2;
	static final int SWT_DESTROY	 	= OS.WM_APP + 3;
	static final int SWT_TRAYICONMSG	= OS.WM_APP + 4;
	static final int SWT_NULL			= OS.WM_APP + 5;
	static final int SWT_RUNASYNC		= OS.WM_APP + 6;
	static int TASKBARCREATED;
	static int TASKBARBUTTONCREATED;
	static int SWT_RESTORECARET;
	static int DI_GETDRAGIMAGE;
	static int SWT_OPENDOC;
	
	/* Workaround for Adobe Reader 7.0 */
	int hitCount;
	
	/* Skinning support */
	Widget [] skinList = new Widget [GROW_SIZE];
	int skinCount;
	
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets."; //$NON-NLS-1$
	/*
	* This code is intentionally commented.  In order
	* to support CLDC, .class cannot be used because
	* it does not compile on some Java compilers when
	* they are targeted for CLDC.
	*/
//	static {
//		String name = Display.class.getName ();
//		int index = name.lastIndexOf ('.');
//		PACKAGE_PREFIX = name.substring (0, index + 1);
//	}

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

Control _getFocusControl () {
	return findControl (OS.GetFocus ());
}

void addBar (Menu menu) {
	if (bars == null) bars = new Menu [4];
	int length = bars.length;
	for (int i=0; i<length; i++) {
		if (bars [i] == menu) return;
	}
	int index = 0;
	while (index < length) {
		if (bars [index] == null) break;
		index++;
	}
	if (index == length) {
		Menu [] newBars = new Menu [length + 4];
		System.arraycopy (bars, 0, newBars, 0, length);
		bars = newBars;
	}
	bars [index] = menu;
}

void addControl (int /*long*/ handle, Control control) {
	if (handle == 0) return;
	if (freeSlot == -1) {
		int length = (freeSlot = indexTable.length) + GROW_SIZE;
		int [] newIndexTable = new int [length];
		Control [] newControlTable = new Control [length];
		System.arraycopy (indexTable, 0, newIndexTable, 0, freeSlot);
		System.arraycopy (controlTable, 0, newControlTable, 0, freeSlot);
		for (int i=freeSlot; i<length-1; i++) newIndexTable [i] = i + 1;
		newIndexTable [length - 1] = -1;
		indexTable = newIndexTable;
		controlTable = newControlTable;
	}
	if (USE_PROPERTY) {
		OS.SetProp (handle, SWT_OBJECT_INDEX, freeSlot + 1);
	} else {
		OS.SetWindowLongPtr (handle, OS.GWLP_USERDATA, freeSlot + 1);
	}
	int oldSlot = freeSlot;
	freeSlot = indexTable [oldSlot];
	indexTable [oldSlot] = -2;
	controlTable [oldSlot] = control;
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

void addMenuItem (MenuItem item) {
	if (items == null) items = new MenuItem [64];
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) {
			item.id = i + ID_START;
			items [i] = item;
			return;
		}
	}
	item.id = items.length + ID_START;
	MenuItem [] newItems = new MenuItem [items.length + 64];
	newItems [items.length] = item;
	System.arraycopy (items, 0, newItems, 0, items.length);
	items = newItems;
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

int asciiKey (int key) {
	if (OS.IsWinCE) return 0;
	
	/* Get the current keyboard. */
	for (int i=0; i<keyboard.length; i++) keyboard [i] = 0;
	if (!OS.GetKeyboardState (keyboard)) return 0;
		
	/* Translate the key to ASCII or UNICODE using the virtual keyboard */
	if (OS.IsUnicode) {
		char [] result = new char [1];
		if (OS.ToUnicode (key, key, keyboard, result, 1, 0) == 1) return result [0];
	} else {
		short [] result = new short [1];
		if (OS.ToAscii (key, key, keyboard, result, 0) == 1) return result [0];
	}
	return 0;
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
	OS.MessageBeep (OS.MB_OK);
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

protected void checkDevice () {
	if (thread == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (thread != Thread.currentThread ()) {
		/*
		* Bug in IBM JVM 1.6.  For some reason, under
		* conditions that are yet to be full understood,
		* Thread.currentThread() is either returning null
		* or a different instance from the one that was
		* saved when the Display was created.  This is
		* possibly a JIT problem because modifying this
		* method to print logging information when the
		* error happens seems to fix the problem.  The
		* fix is to use operating system calls to verify
		* that the current thread is not the Display thread.
		* 
		* NOTE: Despite the fact that Thread.currentThread()
		* is used in other places, the failure has not been
		* observed in all places where it is called. 
		*/
		if (threadId != OS.GetCurrentThreadId ()) {
			error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

static void checkDisplay (Thread thread, boolean multiple) {
	synchronized (Device.class) {
		for (int i=0; i<Displays.length; i++) {
			if (Displays [i] != null) {
				if (!multiple) SWT.error (SWT.ERROR_NOT_IMPLEMENTED, null, " [multiple displays]"); //$NON-NLS-1$
				if (Displays [i].thread == thread) SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
			}
		}
	}
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

int controlKey (int key) {
	int upper = (int)/*64*/OS.CharUpper ((short) key);
	if (64 <= upper && upper <= 95) return upper & 0xBF;
	return key;
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
	checkDisplay (thread = Thread.currentThread (), true);
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
}

static int /*long*/ create32bitDIB (Image image) {
	int transparentPixel = -1, alpha = -1;
	int /*long*/ hMask = 0, hBitmap = 0;
	byte[] alphaData = null;
	switch (image.type) {
		case SWT.ICON:
			ICONINFO info = new ICONINFO ();
			OS.GetIconInfo (image.handle, info);
			hBitmap = info.hbmColor;
			hMask = info.hbmMask;
			break;
		case SWT.BITMAP:
			ImageData data = image.getImageData ();
			hBitmap = image.handle;
			alpha = data.alpha;
			alphaData = data.alphaData;
			transparentPixel = data.transparentPixel;
			break;
	}
	BITMAP bm = new BITMAP ();
	OS.GetObject (hBitmap, BITMAP.sizeof, bm);
	int imgWidth = bm.bmWidth;
	int imgHeight = bm.bmHeight;
	int /*long*/ hDC = OS.GetDC (0);
	int /*long*/ srcHdc = OS.CreateCompatibleDC (hDC);
	int /*long*/ oldSrcBitmap = OS.SelectObject (srcHdc, hBitmap);
	int /*long*/ memHdc = OS.CreateCompatibleDC (hDC);
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER ();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = imgWidth;
	bmiHeader.biHeight = -imgHeight;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = (short)32;
	bmiHeader.biCompression = OS.BI_RGB;
	byte []	bmi = new byte [BITMAPINFOHEADER.sizeof];
	OS.MoveMemory (bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	int /*long*/ [] pBits = new int /*long*/ [1];
	int /*long*/ memDib = OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
	if (memDib == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int /*long*/ oldMemBitmap = OS.SelectObject (memHdc, memDib);
	BITMAP dibBM = new BITMAP ();
	OS.GetObject (memDib, BITMAP.sizeof, dibBM);
	int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;
 	OS.BitBlt (memHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, 0, OS.SRCCOPY);
 	byte red = 0, green = 0, blue = 0;
 	if (transparentPixel != -1) {
		if (bm.bmBitsPixel <= 8) {
			byte [] color = new byte [4];
			OS.GetDIBColorTable (srcHdc, transparentPixel, 1, color);
			blue = color [0];
			green = color [1];
			red = color [2];
		} else {
			switch (bm.bmBitsPixel) {
				case 16:
					blue = (byte)((transparentPixel & 0x1F) << 3);
					green = (byte)((transparentPixel & 0x3E0) >> 2);
					red = (byte)((transparentPixel & 0x7C00) >> 7);
					break;
				case 24:
					blue = (byte)((transparentPixel & 0xFF0000) >> 16);
					green = (byte)((transparentPixel & 0xFF00) >> 8);
					red = (byte)(transparentPixel & 0xFF);
					break;
				case 32:
					blue = (byte)((transparentPixel & 0xFF000000) >>> 24);
					green = (byte)((transparentPixel & 0xFF0000) >> 16);
					red = (byte)((transparentPixel & 0xFF00) >> 8);
					break;
			}
		}
 	}
 	byte [] srcData = new byte [sizeInBytes];
	OS.MoveMemory (srcData, pBits [0], sizeInBytes);
	if (hMask != 0) {
		OS.SelectObject(srcHdc, hMask);
		for (int y = 0, dp = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				if (OS.GetPixel(srcHdc, x, y) != 0) {
					srcData [dp + 0] = srcData [dp + 1] = srcData [dp + 2] = srcData[dp + 3] = (byte)0;
				} else {
					srcData[dp + 3] = (byte)0xFF;
				}
				dp += 4;
			}
		}
	} else if (alpha != -1) {
		for (int y = 0, dp = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				int r = ((srcData[dp + 0] & 0xFF) * alpha) + 128;
				r = (r + (r >> 8)) >> 8;
				int g = ((srcData[dp + 1] & 0xFF) * alpha) + 128;
				g = (g + (g >> 8)) >> 8;
				int b = ((srcData[dp + 2] & 0xFF) * alpha) + 128;
				b = (b + (b >> 8)) >> 8;
				srcData[dp+0] = (byte)r;
				srcData[dp+1] = (byte)g;
				srcData[dp+2] = (byte)b;
				srcData[dp+3] = (byte)alpha;
				dp += 4;
			}
		}
	} else if (alphaData != null) {
		for (int y = 0, dp = 0, ap = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				int a = alphaData[ap++] & 0xFF;
				int r = ((srcData[dp + 0] & 0xFF) * a) + 128;
				r = (r + (r >> 8)) >> 8;
				int g = ((srcData[dp + 1] & 0xFF) * a) + 128;
				g = (g + (g >> 8)) >> 8;
				int b = ((srcData[dp + 2] & 0xFF) * a) + 128;
				b = (b + (b >> 8)) >> 8;
				srcData[dp+0] = (byte)r;
				srcData[dp+1] = (byte)g;
				srcData[dp+2] = (byte)b;
				srcData[dp+3] = (byte)a;
				dp += 4;
			}
		}
	} else if (transparentPixel != -1) {
		for (int y = 0, dp = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				if (srcData [dp] == blue && srcData [dp + 1] == green && srcData [dp + 2] == red) {
					srcData [dp + 0] = srcData [dp + 1] = srcData [dp + 2] = srcData [dp + 3] = (byte)0;
				} else {
					srcData [dp + 3] = (byte)0xFF;
				}
				dp += 4;
			}
		}
	} else {
		for (int y = 0, dp = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				srcData [dp + 3] = (byte)0xFF;
				dp += 4;
			}
		}
	}
	OS.MoveMemory (pBits [0], srcData, sizeInBytes);
	OS.SelectObject (srcHdc, oldSrcBitmap);
	OS.SelectObject (memHdc, oldMemBitmap);
	OS.DeleteObject (srcHdc);
	OS.DeleteObject (memHdc);
	OS.ReleaseDC (0, hDC);
	if (hBitmap != image.handle && hBitmap != 0) OS.DeleteObject (hBitmap);
	if (hMask != 0) OS.DeleteObject (hMask);
	return memDib;
}
static int /*long*/ create32bitDIB (int /*long*/ hBitmap, int alpha, byte [] alphaData, int transparentPixel) {
	BITMAP bm = new BITMAP ();
	OS.GetObject (hBitmap, BITMAP.sizeof, bm);
	int imgWidth = bm.bmWidth;
	int imgHeight = bm.bmHeight;
	int /*long*/ hDC = OS.GetDC (0);
	int /*long*/ srcHdc = OS.CreateCompatibleDC (hDC);
	int /*long*/ oldSrcBitmap = OS.SelectObject (srcHdc, hBitmap);
	int /*long*/ memHdc = OS.CreateCompatibleDC (hDC);
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER ();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = imgWidth;
	bmiHeader.biHeight = -imgHeight;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = (short)32;
	bmiHeader.biCompression = OS.BI_RGB;
	byte []	bmi = new byte [BITMAPINFOHEADER.sizeof];
	OS.MoveMemory (bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	int /*long*/ [] pBits = new int /*long*/ [1];
	int /*long*/ memDib = OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
	if (memDib == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int /*long*/ oldMemBitmap = OS.SelectObject (memHdc, memDib);
	BITMAP dibBM = new BITMAP ();
	OS.GetObject (memDib, BITMAP.sizeof, dibBM);
	int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;
 	OS.BitBlt (memHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, 0, OS.SRCCOPY);
 	byte red = 0, green = 0, blue = 0;
 	if (transparentPixel != -1) {
		if (bm.bmBitsPixel <= 8) {
			byte [] color = new byte [4];
			OS.GetDIBColorTable (srcHdc, transparentPixel, 1, color);
			blue = color [0];
			green = color [1];
			red = color [2];
		} else {
			switch (bm.bmBitsPixel) {
				case 16:
					blue = (byte)((transparentPixel & 0x1F) << 3);
					green = (byte)((transparentPixel & 0x3E0) >> 2);
					red = (byte)((transparentPixel & 0x7C00) >> 7);
					break;
				case 24:
					blue = (byte)((transparentPixel & 0xFF0000) >> 16);
					green = (byte)((transparentPixel & 0xFF00) >> 8);
					red = (byte)(transparentPixel & 0xFF);
					break;
				case 32:
					blue = (byte)((transparentPixel & 0xFF000000) >>> 24);
					green = (byte)((transparentPixel & 0xFF0000) >> 16);
					red = (byte)((transparentPixel & 0xFF00) >> 8);
					break;
			}
		}
 	}
	OS.SelectObject (srcHdc, oldSrcBitmap);
	OS.SelectObject (memHdc, oldMemBitmap);
	OS.DeleteObject (srcHdc);
	OS.DeleteObject (memHdc);
	OS.ReleaseDC (0, hDC);
 	byte [] srcData = new byte [sizeInBytes];
	OS.MoveMemory (srcData, pBits [0], sizeInBytes);
	if (alpha != -1) {
		for (int y = 0, dp = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				srcData [dp + 3] = (byte)alpha;
				dp += 4;
			}
		}
	} else if (alphaData != null) {
		for (int y = 0, dp = 0, ap = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				srcData [dp + 3] = alphaData [ap++];
				dp += 4;
			}
		}
	} else if (transparentPixel != -1) {
		for (int y = 0, dp = 0; y < imgHeight; ++y) {
			for (int x = 0; x < imgWidth; ++x) {
				if (srcData [dp] == blue && srcData [dp + 1] == green && srcData [dp + 2] == red) {
					srcData [dp + 3] = (byte)0;
				} else {
					srcData [dp + 3] = (byte)0xFF;
				}
				dp += 4;
			}
		}
	}
	OS.MoveMemory (pBits [0], srcData, sizeInBytes);
	return memDib;
}

static Image createIcon (Image image) {
	Device device = image.getDevice ();
	ImageData data = image.getImageData ();
	if (data.alpha == -1 && data.alphaData == null) {
		ImageData mask = data.getTransparencyMask ();
		return new Image (device, data, mask);
	}
	int width = data.width, height = data.height;
	int /*long*/ hMask, hBitmap;
	int /*long*/ hDC = device.internal_new_GC (null);
	int /*long*/ dstHdc = OS.CreateCompatibleDC (hDC), oldDstBitmap;
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		hBitmap = Display.create32bitDIB (image.handle, data.alpha, data.alphaData, data.transparentPixel);
		hMask = OS.CreateBitmap (width, height, 1, 1, null);
		oldDstBitmap = OS.SelectObject (dstHdc, hMask);
		OS.PatBlt (dstHdc, 0, 0, width, height, OS.BLACKNESS);
	} else {
		hMask = Display.createMaskFromAlpha (data, width, height);
		/* Icons need black pixels where the mask is transparent */
		hBitmap = OS.CreateCompatibleBitmap (hDC, width, height);
		oldDstBitmap = OS.SelectObject (dstHdc, hBitmap);
		int /*long*/ srcHdc = OS.CreateCompatibleDC (hDC);
		int /*long*/ oldSrcBitmap = OS.SelectObject (srcHdc, image.handle);
		OS.PatBlt (dstHdc, 0, 0, width, height, OS.BLACKNESS);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCINVERT);
		OS.SelectObject (srcHdc, hMask);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCAND);
		OS.SelectObject (srcHdc, image.handle);
		OS.BitBlt (dstHdc, 0, 0, width, height, srcHdc, 0, 0, OS.SRCINVERT);
		OS.SelectObject (srcHdc, oldSrcBitmap);
		OS.DeleteDC (srcHdc);
	}
	OS.SelectObject (dstHdc, oldDstBitmap);
	OS.DeleteDC (dstHdc);
	device.internal_dispose_GC (hDC, null);
	ICONINFO info = new ICONINFO ();
	info.fIcon = true;
	info.hbmColor = hBitmap;
	info.hbmMask = hMask;
	int /*long*/ hIcon = OS.CreateIconIndirect (info);
	if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.DeleteObject (hBitmap);
	OS.DeleteObject (hMask);
	return Image.win32_new (device, SWT.ICON, hIcon);
}

static int /*long*/ createMaskFromAlpha (ImageData data, int destWidth, int destHeight) {
	int srcWidth = data.width;
	int srcHeight = data.height;
	ImageData mask = ImageData.internal_new (srcWidth, srcHeight, 1,
			new PaletteData(new RGB [] {new RGB (0, 0, 0), new RGB (0xff, 0xff, 0xff)}),
			2, null, 1, null, null, -1, -1, -1, 0, 0, 0, 0);
	int ap = 0;
	for (int y = 0; y < mask.height; y++) {
		for (int x = 0; x < mask.width; x++) {
			mask.setPixel (x, y, (data.alphaData [ap++] & 0xff) <= 127 ? 1 : 0);
		}
	}
	int /*long*/ hMask = OS.CreateBitmap (srcWidth, srcHeight, 1, 1, mask.data);
	if (srcWidth != destWidth || srcHeight != destHeight) {
		int /*long*/ hdc = OS.GetDC (0);
		int /*long*/ hdc1 = OS.CreateCompatibleDC (hdc);
		OS.SelectObject (hdc1, hMask);
		int /*long*/ hdc2 = OS.CreateCompatibleDC (hdc);
		int /*long*/ hMask2 = OS.CreateBitmap (destWidth, destHeight, 1, 1, null);
		OS.SelectObject (hdc2, hMask2);
		if (!OS.IsWinCE) OS.SetStretchBltMode(hdc2, OS.COLORONCOLOR);
		OS.StretchBlt (hdc2, 0, 0, destWidth, destHeight, hdc1, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
		OS.DeleteDC (hdc1);
		OS.DeleteDC (hdc2);
		OS.ReleaseDC (0, hdc);
		OS.DeleteObject(hMask);
		hMask = hMask2;
	}
	return hMask;
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

void drawMenuBars () {
	if (bars == null) return;
	for (int i=0; i<bars.length; i++) {
		Menu menu = bars [i];
		if (menu != null && !menu.isDisposed ()) menu.update ();
	}
	bars = null;
}

int /*long*/ embeddedProc (int /*long*/ hwnd, int /*long*/ msg, int /*long*/ wParam, int /*long*/ lParam) {
	switch ((int)/*64*/msg) {
		case SWT_KEYMSG: {
			MSG keyMsg = new MSG ();
			OS.MoveMemory (keyMsg, lParam, MSG.sizeof);
			OS.TranslateMessage (keyMsg);
			OS.DispatchMessage (keyMsg);
			int /*long*/ hHeap = OS.GetProcessHeap ();
			OS.HeapFree (hHeap, 0, lParam);
			break;
		}
		case SWT_DESTROY: {
			OS.DestroyWindow (hwnd);
			if (embeddedCallback != null) embeddedCallback.dispose ();
			if (getMsgCallback != null) getMsgCallback.dispose ();
			embeddedCallback = getMsgCallback = null;
			embeddedProc = getMsgProc = 0;
			break;
		}
	}
	return OS.DefWindowProc (hwnd, (int)/*64*/msg, wParam, lParam);
}

/**
 * Does whatever display specific cleanup is required, and then
 * uses the code in <code>SWTError.error</code> to handle the error.
 *
 * @param code the descriptive error code
 *
 * @see SWT#error(int)
 */
void error (int code) {
	SWT.error (code);
}

boolean filterEvent (Event event) {
	if (filterTable != null) filterTable.sendEvent (event);
	return false;
}

boolean filters (int eventType) {
	if (filterTable == null) return false;
	return filterTable.hooks (eventType);
}

boolean filterMessage (MSG msg) {
	int message = msg.message;
	if (OS.WM_KEYFIRST <= message && message <= OS.WM_KEYLAST) {
		Control control = findControl (msg.hwnd);
		if (control != null) {
			if (translateAccelerator (msg, control) || translateMnemonic (msg, control) || translateTraversal (msg, control)) {	
				lastAscii = lastKey = 0;
				lastVirtual = lastNull = lastDead = false;
				return true;
			}
		}
	}
	return false;
}

Control findControl (int /*long*/ handle) {
	if (handle == 0) return null;
	int /*long*/ hwndOwner = 0;
	do {
		Control control = getControl (handle);
		if (control != null) return control;
		hwndOwner = OS.GetWindow (handle, OS.GW_OWNER);
		handle = OS.GetParent (handle);
	} while (handle != 0 && handle != hwndOwner);
	return null;
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
	return getControl (handle);
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
public Widget findWidget (int /*long*/ handle, int /*long*/ id) {
	checkDevice ();
	//TODO - should ids be long
	Control control = getControl (handle);
	return control != null ? control.findItem (id) : null;
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
public Widget findWidget (Widget widget, int /*long*/ id) {
	checkDevice ();
	//TODO - should ids be long
	if (widget instanceof Control) {
		return findWidget (((Control) widget).handle, id);
	}
	return null;
}

int /*long*/ foregroundIdleProc (int /*long*/ code, int /*long*/ wParam, int /*long*/ lParam) {
	if (code >= 0) {
		if (runMessages && getMessageCount () != 0) {
			if (runMessagesInIdle) {
				if (runMessagesInMessageProc) {
					OS.PostMessage (hwndMessage, SWT_RUNASYNC, 0, 0);
				} else {
					runAsyncMessages (false);
				}
			}
			/*
			* Bug in Windows.  For some reason, input events can be lost
			* when a message is posted to the queue from a foreground idle
			* hook.  The fix is to detect that there are outstanding input
			* events and avoid posting the wake event.
			* 
			* Note that PeekMessage() changes the state of events on the
			* queue to no longer be considered new. If we peek for input
			* events and posted messages (PM_QS_INPUT | PM_QS_POSTMESSAGE),
			* it is possible to cause WaitMessage() to sleep until a new
			* input event is generated causing sync runnables not to be
			* executed.
			*/
			MSG msg = new MSG();
			int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT;
			if (!OS.PeekMessage (msg, 0, 0, 0, flags)) wakeThread ();
		}
	}
	return OS.CallNextHookEx (idleHook, (int)/*64*/code, wParam, lParam);
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

TouchSource findTouchSource (int /*long*/ touchDevice, Monitor monitor) {
	if (touchSources == null) touchSources = new TouchSource [4];
	int length = touchSources.length;
	for (int i=0; i<length; i++) {
		if (touchSources [i] != null && touchSources [i].handle == touchDevice) {
			return touchSources [i];
		}
	}
	int index = 0;
	while (index < length) {
		if (touchSources [index] == null) break;
		index++;
	}
	if (index == length) {
		TouchSource [] newTouchSources = new TouchSource [length + 4];
		System.arraycopy (touchSources, 0, newTouchSources, 0, length);
		touchSources = newTouchSources;
	}
	return touchSources [index] = new TouchSource (touchDevice, true, monitor.getBounds ()); 
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
	Control control = findControl (OS.GetActiveWindow ());
	return control != null ? control.getShell () : null;
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
public Menu getMenuBar () {
	checkDevice ();
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
	if (OS.GetSystemMetrics (OS.SM_CMONITORS) < 2) {
		int width = OS.GetSystemMetrics (OS.SM_CXSCREEN);
		int height = OS.GetSystemMetrics (OS.SM_CYSCREEN);
		return new Rectangle (0, 0, width, height);
	}
	int x = OS.GetSystemMetrics (OS.SM_XVIRTUALSCREEN);
	int y = OS.GetSystemMetrics (OS.SM_YVIRTUALSCREEN);	
	int width = OS.GetSystemMetrics (OS.SM_CXVIRTUALSCREEN);
	int height = OS.GetSystemMetrics (OS.SM_CYVIRTUALSCREEN);
	return new Rectangle (x, y, width, height);
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

int getClickCount (int type, int button, int /*long*/ hwnd, int /*long*/ lParam) {
	switch (type) {
		case SWT.MouseDown:
			int doubleClick = OS.GetDoubleClickTime ();
			if (clickRect == null) clickRect = new RECT ();
			int deltaTime = Math.abs (lastTime - getLastEventTime ());
			POINT pt = new POINT ();
			OS.POINTSTOPOINT (pt, lParam);
			if (lastClickHwnd == hwnd && lastButton == button && (deltaTime <= doubleClick) && OS.PtInRect (clickRect, pt)) {
				clickCount++;
			} else {
				clickCount = 1;
			}
			//FALL THROUGH
		case SWT.MouseDoubleClick:
			lastButton = button;
			lastClickHwnd = hwnd;
			lastTime = getLastEventTime ();
			int xInset = OS.GetSystemMetrics (OS.SM_CXDOUBLECLK) / 2;
			int yInset = OS.GetSystemMetrics (OS.SM_CYDOUBLECLK) / 2;
			int x = OS.GET_X_LPARAM (lParam), y = OS.GET_Y_LPARAM (lParam);
			OS.SetRect (clickRect, x - xInset, y - yInset, x + xInset, y + yInset);
			//FALL THROUGH
		case SWT.MouseUp:
			return clickCount;
	}
	return 0;
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
	if (OS.GetSystemMetrics (OS.SM_CMONITORS) < 2) {
		RECT rect = new RECT ();
		OS.SystemParametersInfo (OS.SPI_GETWORKAREA, 0, rect, 0);
		int width = rect.right - rect.left;
		int height = rect.bottom - rect.top;
		return new Rectangle (rect.left, rect.top, width, height);
	}
	int x = OS.GetSystemMetrics (OS.SM_XVIRTUALSCREEN);
	int y = OS.GetSystemMetrics (OS.SM_YVIRTUALSCREEN);	
	int width = OS.GetSystemMetrics (OS.SM_CXVIRTUALSCREEN);
	int height = OS.GetSystemMetrics (OS.SM_CYVIRTUALSCREEN);
	return new Rectangle (x, y, width, height);
}

Control getControl (int /*long*/ handle) {
	if (handle == 0) return null;
	if (lastControl != null && lastHwnd == handle) {
		return lastControl;
	}
	if (lastGetControl != null && lastGetHwnd == handle) {
		return lastGetControl;
	}
	int index;
	if (USE_PROPERTY) {
		index = (int)/*64*/OS.GetProp (handle, SWT_OBJECT_INDEX) - 1;
	} else {
		index = (int)/*64*/OS.GetWindowLongPtr (handle, OS.GWLP_USERDATA) - 1;
	}
	if (0 <= index && index < controlTable.length) {
		Control control = controlTable [index];
		/*
		* Because GWL_USERDATA can be used by native widgets that
		* do not belong to SWT, it is possible that GWL_USERDATA
		* could return an index that is in the range of the table,
		* but was not put there by SWT.  Therefore, it is necessary
		* to check the handle of the control that is in the table
		* against the handle that provided the GWL_USERDATA.
		*/
		if (control != null && control.checkHandle (handle)) {
			lastGetHwnd = handle;
			lastGetControl = control;
			return control;
		}
	}
	return null;
}

/**
 * Returns the control which the on-screen pointer is currently
 * over top of, or null if it is not currently over one of the
 * controls built by the currently running application.
 *
 * @return the control under the cursor or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Control getCursorControl () {
	checkDevice ();
	POINT pt = new POINT ();
	if (!OS.GetCursorPos (pt)) return null;
	return findControl (OS.WindowFromPoint (pt));
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
	POINT pt = new POINT ();
	OS.GetCursorPos (pt);
	return new Point (pt.x, pt.y);
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
	return new Point [] {
		new Point (OS.GetSystemMetrics (OS.SM_CXCURSOR), OS.GetSystemMetrics (OS.SM_CYCURSOR))};
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

static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
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
	if (key.equals (RUN_MESSAGES_IN_IDLE_KEY)) {
		return new Boolean (runMessagesInIdle);
	}
	if (key.equals (RUN_MESSAGES_IN_MESSAGE_PROC_KEY)) {
		return new Boolean (runMessagesInMessageProc);
	}
	if (key.equals (USE_OWNDC_KEY)) {
		return new Boolean (useOwnDC);
	}
	if (key.equals (ACCEL_KEY_HIT)) {
		return new Boolean (accelKeyHit);
	}
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
	return SWT.LEFT;
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
	return OS.GetDoubleClickTime ();
}

/**
 * Returns the control which currently has keyboard focus,
 * or null if keyboard events are not currently going to
 * any of the controls built by the currently running
 * application.
 *
 * @return the focus control or <code>null</code>
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
	return _getFocusControl ();
}

String getFontName (LOGFONT logFont) {
	char[] chars;
	if (OS.IsUnicode) {
		chars = ((LOGFONTW)logFont).lfFaceName;
	} else {
		chars = new char[OS.LF_FACESIZE];
		byte[] bytes = ((LOGFONTA)logFont).lfFaceName;
		OS.MultiByteToWideChar (OS.CP_ACP, OS.MB_PRECOMPOSED, bytes, bytes.length, chars, chars.length);
	}
	int index = 0;
	while (index < chars.length) {
		if (chars [index] == 0) break;
		index++;
	}
	return new String (chars, 0, index);
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
	if (OS.IsWinCE) return false;
	HIGHCONTRAST pvParam = new HIGHCONTRAST ();
	pvParam.cbSize = HIGHCONTRAST.sizeof;
	OS.SystemParametersInfo (OS.SPI_GETHIGHCONTRAST, 0, pvParam, 0);
	return (pvParam.dwFlags & OS.HCF_HIGHCONTRASTON) != 0;
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
	checkDevice ();
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		if (getDepth () >= 24) return 32;
	}

	/* Use the character encoding for the default locale */
	TCHAR buffer1 = new TCHAR (0, "Control Panel\\Desktop\\WindowMetrics", true); //$NON-NLS-1$

	int /*long*/ [] phkResult = new int /*long*/ [1];
	int result = OS.RegOpenKeyEx (OS.HKEY_CURRENT_USER, buffer1, 0, OS.KEY_READ, phkResult);
	if (result != 0) return 4;
	int depth = 4;
	int [] lpcbData = new int [1];
	
	/* Use the character encoding for the default locale */
	TCHAR buffer2 = new TCHAR (0, "Shell Icon BPP", true); //$NON-NLS-1$
	result = OS.RegQueryValueEx (phkResult [0], buffer2, 0, null, (TCHAR) null, lpcbData);
	if (result == 0) {
		TCHAR lpData = new TCHAR (0, lpcbData [0] / TCHAR.sizeof);
		result = OS.RegQueryValueEx (phkResult [0], buffer2, 0, null, lpData, lpcbData);
		if (result == 0) {
			try {
				depth = Integer.parseInt (lpData.toString (0, lpData.strlen ()));
			} catch (NumberFormatException e) {}
		}
	}
	OS.RegCloseKey (phkResult [0]);
	return depth;
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
		new Point (OS.GetSystemMetrics (OS.SM_CXSMICON), OS.GetSystemMetrics (OS.SM_CYSMICON)),
		new Point (OS.GetSystemMetrics (OS.SM_CXICON), OS.GetSystemMetrics (OS.SM_CYICON)),
	};	
}

ImageList getImageList (int style, int width, int height) {
	if (imageList == null) imageList = new ImageList [4];
	
	int i = 0;
	int length = imageList.length; 
	while (i < length) {
		ImageList list = imageList [i];
		if (list == null) break;
		Point size = list.getImageSize();
		if (size.x == width && size.y == height) {
			if (list.getStyle () == style) {
				list.addRef();
				return list;
			}
		}
		i++;
	}
	
	if (i == length) {
		ImageList [] newList = new ImageList [length + 4];
		System.arraycopy (imageList, 0, newList, 0, length);
		imageList = newList;
	}
	
	ImageList list = new ImageList (style, width, height);
	imageList [i] = list;
	list.addRef();
	return list;
}

ImageList getImageListToolBar (int style, int width, int height) {
	if (toolImageList == null) toolImageList = new ImageList [4];
	
	int i = 0;
	int length = toolImageList.length; 
	while (i < length) {
		ImageList list = toolImageList [i];
		if (list == null) break;
		Point size = list.getImageSize();
		if (size.x == width && size.y == height) {
			if (list.getStyle () == style) {
				list.addRef();
				return list;
			}
		}
		i++;
	}
	
	if (i == length) {
		ImageList [] newList = new ImageList [length + 4];
		System.arraycopy (toolImageList, 0, newList, 0, length);
		toolImageList = newList;
	}
	
	ImageList list = new ImageList (style, width, height);
	toolImageList [i] = list;
	list.addRef();
	return list;
}

ImageList getImageListToolBarDisabled (int style, int width, int height) {
	if (toolDisabledImageList == null) toolDisabledImageList = new ImageList [4];
	
	int i = 0;
	int length = toolDisabledImageList.length; 
	while (i < length) {
		ImageList list = toolDisabledImageList [i];
		if (list == null) break;
		Point size = list.getImageSize();
		if (size.x == width && size.y == height) {
			if (list.getStyle () == style) {
				list.addRef();
				return list;
			}
		}
		i++;
	}
	
	if (i == length) {
		ImageList [] newList = new ImageList [length + 4];
		System.arraycopy (toolDisabledImageList, 0, newList, 0, length);
		toolDisabledImageList = newList;
	}
	
	ImageList list = new ImageList (style, width, height);
	toolDisabledImageList [i] = list;
	list.addRef();
	return list;
}

ImageList getImageListToolBarHot (int style, int width, int height) {
	if (toolHotImageList == null) toolHotImageList = new ImageList [4];
	
	int i = 0;
	int length = toolHotImageList.length; 
	while (i < length) {
		ImageList list = toolHotImageList [i];
		if (list == null) break;
		Point size = list.getImageSize();
		if (size.x == width && size.y == height) {
			if (list.getStyle () == style) {
				list.addRef();
				return list;
			}
		}
		i++;
	}
	
	if (i == length) {
		ImageList [] newList = new ImageList [length + 4];
		System.arraycopy (toolHotImageList, 0, newList, 0, length);
		toolHotImageList = newList;
	}
	
	ImageList list = new ImageList (style, width, height);
	toolHotImageList [i] = list;
	list.addRef();
	return list;
}

int getLastEventTime () {
	return OS.IsWinCE ? OS.GetTickCount () : OS.GetMessageTime ();
}

MenuItem getMenuItem (int id) {
	if (items == null) return null;
	id = id - ID_START;
	if (0 <= id && id < items.length) return items [id];
	return null;
}

int getMessageCount () {
	return synchronizer.getMessageCount ();
}


Shell getModalShell () {
	if (modalShells == null) return null;
	int index = modalShells.length;
	while (--index >= 0) {
		Shell shell = modalShells [index];
		if (shell != null) return shell;
	}
	return null;
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
	if (OS.IsWinCE || OS.WIN32_VERSION < OS.VERSION (4, 10)) {
		return new Monitor [] {getPrimaryMonitor ()};
	}
	monitors = new Monitor [4];
	Callback callback = new Callback (this, "monitorEnumProc", 4); //$NON-NLS-1$
	int /*long*/ lpfnEnum = callback.getAddress ();
	if (lpfnEnum == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.EnumDisplayMonitors (0, null, lpfnEnum, 0);
	callback.dispose ();
	Monitor [] result = new Monitor [monitorCount];
	System.arraycopy (monitors, 0, result, 0, monitorCount);
	monitors = null;
	monitorCount = 0;
	return result;
}

int /*long*/ getMsgProc (int /*long*/ code, int /*long*/ wParam, int /*long*/ lParam) {
	if (embeddedHwnd == 0) {
		int /*long*/ hInstance = OS.GetModuleHandle (null);
		embeddedHwnd = OS.CreateWindowEx (0,
			windowClass,
			null,
			OS.WS_OVERLAPPED,
			0, 0, 0, 0,
			0,
			0,
			hInstance,
			null);
		embeddedCallback = new Callback (this, "embeddedProc", 4); //$NON-NLS-1$
		embeddedProc = embeddedCallback.getAddress ();
		if (embeddedProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		OS.SetWindowLongPtr (embeddedHwnd, OS.GWLP_WNDPROC, embeddedProc);
	}
	if (code >= 0 && (wParam & OS.PM_REMOVE) != 0) {
		MSG msg = new MSG ();
		OS.MoveMemory (msg, lParam, MSG.sizeof);
		switch (msg.message) {
			case OS.WM_KEYDOWN:
			case OS.WM_KEYUP:
			case OS.WM_SYSKEYDOWN:
			case OS.WM_SYSKEYUP: {
				Control control = findControl (msg.hwnd);
				if (control != null) {
					int /*long*/ hHeap = OS.GetProcessHeap ();
					int /*long*/ keyMsg = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, MSG.sizeof);
					OS.MoveMemory (keyMsg, msg, MSG.sizeof);
					OS.PostMessage (hwndMessage, SWT_KEYMSG, wParam, keyMsg);
					switch ((int)/*64*/msg.wParam) {
						case OS.VK_SHIFT:
						case OS.VK_MENU:
						case OS.VK_CONTROL:
						case OS.VK_CAPITAL:
						case OS.VK_NUMLOCK:
						case OS.VK_SCROLL:
							break;
						default:
							msg.message = OS.WM_NULL;
							OS.MoveMemory (lParam, msg, MSG.sizeof);
					}
				}
			}
		}
	}
	return OS.CallNextHookEx (msgHook, (int)/*64*/code, wParam, lParam);
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
	if (OS.IsWinCE || OS.WIN32_VERSION < OS.VERSION (4, 10)) {
		Monitor monitor = new Monitor();
		int width = OS.GetSystemMetrics (OS.SM_CXSCREEN);
		int height = OS.GetSystemMetrics (OS.SM_CYSCREEN);
		monitor.width = width;
		monitor.height = height;
		RECT rect = new RECT ();
		OS.SystemParametersInfo (OS.SPI_GETWORKAREA, 0, rect, 0);
		monitor.clientX = rect.left;
		monitor.clientY = rect.top;
		monitor.clientWidth = rect.right - rect.left;
		monitor.clientHeight = rect.bottom - rect.top;
		return monitor;
	}
	monitors = new Monitor [4];
	Callback callback = new Callback (this, "monitorEnumProc", 4); //$NON-NLS-1$
	int /*long*/ lpfnEnum = callback.getAddress ();
	if (lpfnEnum == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.EnumDisplayMonitors (0, null, lpfnEnum, 0);
	callback.dispose ();
	Monitor result = null;
	MONITORINFO lpmi = new MONITORINFO ();
	lpmi.cbSize = MONITORINFO.sizeof;
	for (int i = 0; i < monitorCount; i++) {
		Monitor monitor = monitors [i];
		OS.GetMonitorInfo (monitors [i].handle, lpmi);
		if ((lpmi.dwFlags & OS.MONITORINFOF_PRIMARY) != 0) {
			result = monitor;
			break;
		} 			
	}
	monitors = null;
	monitorCount = 0;
	return result;		
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
	int index = 0;
	Shell [] result = new Shell [16];
	for (int i = 0; i < controlTable.length; i++) {
		Control control = controlTable [i];
		if (control != null && control instanceof Shell) {
			int j = 0;
			while (j < index) {
				if (result [j] == control) break;
				j++;
			}
			if (j == index) {
				if (index == result.length) {
					Shell [] newResult = new Shell [index + 16];
					System.arraycopy (result, 0, newResult, 0, index);
					result = newResult;
				}
				result [index++] = (Shell) control;	
			}
		}
	}
	if (index == result.length) return result;
	Shell [] newResult = new Shell [index];
	System.arraycopy (result, 0, newResult, 0, index);
	return newResult;
}

Image getSortImage (int direction) {
	switch (direction) {
		case SWT.UP: {
			if (upArrow != null) return upArrow;
			Color c1 = getSystemColor (SWT.COLOR_WIDGET_NORMAL_SHADOW);
			Color c2 = getSystemColor (SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
			Color c3 = getSystemColor (SWT.COLOR_WIDGET_BACKGROUND);
			PaletteData palette = new PaletteData(new RGB [] {c1.getRGB (), c2.getRGB (), c3.getRGB ()});
			ImageData imageData = new ImageData (8, 8, 4, palette);
			imageData.transparentPixel = 2;
			upArrow = new Image (this, imageData);
			GC gc = new GC (upArrow);
			gc.setBackground (c3);
			gc.fillRectangle (0, 0, 8, 8);
			gc.setForeground (c1);
			int [] line1 = new int [] {0,6, 1,6, 1,4, 2,4, 2,2, 3,2, 3,1};
			gc.drawPolyline (line1);
			gc.setForeground (c2);
			int [] line2 = new int [] {0,7, 7,7, 7,6, 6,6, 6,4, 5,4, 5,2, 4,2, 4,1};
			gc.drawPolyline (line2);
			gc.dispose ();
			return upArrow;
		}
		case SWT.DOWN: {
			if (downArrow != null) return downArrow;
			Color c1 = getSystemColor (SWT.COLOR_WIDGET_NORMAL_SHADOW);
			Color c2 = getSystemColor (SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
			Color c3 = getSystemColor (SWT.COLOR_WIDGET_BACKGROUND);
			PaletteData palette = new PaletteData (new RGB [] {c1.getRGB (), c2.getRGB (), c3.getRGB ()});
			ImageData imageData = new ImageData (8, 8, 4, palette);
			imageData.transparentPixel = 2;
			downArrow = new Image (this, imageData);
			GC gc = new GC (downArrow);
			gc.setBackground (c3);
			gc.fillRectangle (0, 0, 8, 8);
			gc.setForeground (c1);
			int [] line1 = new int [] {7,0, 0,0, 0,1, 1,1, 1,3, 2,3, 2,5, 3,5, 3,6};
			gc.drawPolyline (line1);
			gc.setForeground (c2);
			int [] line2 = new int [] {4,6, 4,5, 5,5, 5,3, 6,3, 6,1, 7,1};
			gc.drawPolyline (line2);
			gc.dispose ();
			return downArrow;
		}
	}
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
	int pixel = 0x00000000;
	switch (id) {
		case SWT.COLOR_WIDGET_DARK_SHADOW:		pixel = OS.GetSysColor (OS.COLOR_3DDKSHADOW);	break;
		case SWT.COLOR_WIDGET_NORMAL_SHADOW:	pixel = OS.GetSysColor (OS.COLOR_3DSHADOW); 	break;
		case SWT.COLOR_WIDGET_LIGHT_SHADOW: 	pixel = OS.GetSysColor (OS.COLOR_3DLIGHT);  	break;
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:	pixel = OS.GetSysColor (OS.COLOR_3DHIGHLIGHT);  break;
		case SWT.COLOR_WIDGET_BACKGROUND: 		pixel = OS.GetSysColor (OS.COLOR_3DFACE);  	break;
		case SWT.COLOR_WIDGET_BORDER: 		pixel = OS.GetSysColor (OS.COLOR_WINDOWFRAME);  break;
		case SWT.COLOR_WIDGET_FOREGROUND:
		case SWT.COLOR_LIST_FOREGROUND: 		pixel = OS.GetSysColor (OS.COLOR_WINDOWTEXT);	break;
		case SWT.COLOR_LIST_BACKGROUND: 		pixel = OS.GetSysColor (OS.COLOR_WINDOW);  	break;
		case SWT.COLOR_LIST_SELECTION: 		pixel = OS.GetSysColor (OS.COLOR_HIGHLIGHT);	break;
		case SWT.COLOR_LIST_SELECTION_TEXT: 	pixel = OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT);break;
		case SWT.COLOR_INFO_FOREGROUND:		pixel = OS.GetSysColor (OS.COLOR_INFOTEXT);	break;
		case SWT.COLOR_INFO_BACKGROUND:		pixel = OS.GetSysColor (OS.COLOR_INFOBK);		break;
		case SWT.COLOR_TITLE_FOREGROUND: 		pixel = OS.GetSysColor (OS.COLOR_CAPTIONTEXT);	break;
		case SWT.COLOR_TITLE_BACKGROUND:		pixel = OS.GetSysColor (OS.COLOR_ACTIVECAPTION);		break;
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT: 
			pixel = OS.GetSysColor (OS.COLOR_GRADIENTACTIVECAPTION);
			if (pixel == 0) pixel = OS.GetSysColor (OS.COLOR_ACTIVECAPTION);
			break;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND: 		pixel = OS.GetSysColor (OS.COLOR_INACTIVECAPTIONTEXT);	break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:			pixel = OS.GetSysColor (OS.COLOR_INACTIVECAPTION);		break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:	
			pixel = OS.GetSysColor (OS.COLOR_GRADIENTINACTIVECAPTION);
			if (pixel == 0) pixel = OS.GetSysColor (OS.COLOR_INACTIVECAPTION);
			break;
		default:
			return super.getSystemColor (id);
	}
	return Color.win32_new (this, pixel);
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
	if (systemFont != null) return systemFont;
	int /*long*/ hFont = 0;
	if (!OS.IsWinCE) {
		NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
		info.cbSize = NONCLIENTMETRICS.sizeof;
		if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
			LOGFONT logFont = OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfMessageFont : ((NONCLIENTMETRICSA)info).lfMessageFont;
			hFont = OS.CreateFontIndirect (logFont);
			lfSystemFont = hFont != 0 ? logFont : null;
		}
	}
	if (hFont == 0) hFont = OS.GetStockObject (OS.DEFAULT_GUI_FONT);
	if (hFont == 0) hFont = OS.GetStockObject (OS.SYSTEM_FONT);
	return systemFont = Font.win32_new (this, hFont);
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
	switch (id) {
		case SWT.ICON_ERROR: {
			if (errorImage != null) return errorImage;
			int /*long*/ hIcon = OS.LoadImage (0, OS.OIC_HAND, OS.IMAGE_ICON, 0, 0, OS.LR_SHARED);
			return errorImage = Image.win32_new (this, SWT.ICON, hIcon);
		}
		case SWT.ICON_WORKING:
		case SWT.ICON_INFORMATION: {
			if (infoImage != null) return infoImage;
			int /*long*/ hIcon = OS.LoadImage (0, OS.OIC_INFORMATION, OS.IMAGE_ICON, 0, 0, OS.LR_SHARED);
			return infoImage = Image.win32_new (this, SWT.ICON, hIcon);
		}
		case SWT.ICON_QUESTION: {
			if (questionImage != null) return questionImage;
			int /*long*/ hIcon = OS.LoadImage (0, OS.OIC_QUES, OS.IMAGE_ICON, 0, 0, OS.LR_SHARED);
			return questionImage = Image.win32_new (this, SWT.ICON, hIcon);
		}
		case SWT.ICON_WARNING: {
			if (warningIcon != null) return warningIcon;
			int /*long*/ hIcon = OS.LoadImage (0, OS.OIC_BANG, OS.IMAGE_ICON, 0, 0, OS.LR_SHARED);
			return warningIcon = Image.win32_new (this, SWT.ICON, hIcon);
		}
	}
	return null;
}

/**
 * Returns the single instance of the system-provided menu for the application.
 * On platforms where no menu is provided for the application this method returns null.
 *
 * @return the system menu or <code>null</code>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.7
 */
public Menu getSystemMenu () {
	checkDevice();
	return null;
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
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 1)) {
		taskBar = new TaskBar (this, SWT.NONE);
	}
	return taskBar;
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
	if (!OS.IsWinCE) tray = new Tray (this, SWT.NONE);
	return tray;
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

/**	 
 * Returns true if a touch-aware input device is attached to the system,
 * enabled, and ready for use.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.7
 */
public boolean getTouchEnabled () {
	checkDevice();
	int value = OS.GetSystemMetrics (OS.SM_DIGITIZER);
	return (value & (OS.NID_READY | OS.NID_MULTI_INPUT)) == (OS.NID_READY | OS.NID_MULTI_INPUT);
}

int /*long*/ hButtonTheme () {
	if (hButtonTheme != 0) return hButtonTheme;
	return hButtonTheme = OS.OpenThemeData (hwndMessage, BUTTON);
}

int /*long*/ hEditTheme () {
	if (hEditTheme != 0) return hEditTheme;
	return hEditTheme = OS.OpenThemeData (hwndMessage, EDIT);
}

int /*long*/ hExplorerBarTheme () {
	if (hExplorerBarTheme != 0) return hExplorerBarTheme;
	return hExplorerBarTheme = OS.OpenThemeData (hwndMessage, EXPLORERBAR);
}

int /*long*/ hScrollBarTheme () {
	if (hScrollBarTheme != 0) return hScrollBarTheme;
	return hScrollBarTheme = OS.OpenThemeData (hwndMessage, SCROLLBAR);
}

int /*long*/ hTabTheme () {
	if (hTabTheme != 0) return hTabTheme;
	return hTabTheme = OS.OpenThemeData (hwndMessage, TAB);
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
	int /*long*/ hDC = OS.GetDC (0);
	if (hDC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) != 0) {
			data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
		} else {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = this;
		data.font = getSystemFont ();
	}
	return hDC;
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
	
	/* Set the application user model ID */
	char [] appName = null;
	if (APP_NAME != null) {
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 1)) {
			int length = APP_NAME.length ();
			appName = new char [length + 1];
			APP_NAME.getChars (0, length, appName, 0);
			OS.SetCurrentProcessExplicitAppUserModelID (appName);
		}
	}
	
	/* Create the callbacks */
	windowCallback = new Callback (this, "windowProc", 4); //$NON-NLS-1$
	windowProc = windowCallback.getAddress ();
	if (windowProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	/* Remember the current thread id */
	threadId = OS.GetCurrentThreadId ();
	
	/* Use the character encoding for the default locale */
	windowClass = new TCHAR (0, WindowName + WindowClassCount, true);
	windowShadowClass = new TCHAR (0, WindowShadowName + WindowClassCount, true);
	windowOwnDCClass = new TCHAR (0, WindowOwnDCName + WindowClassCount, true);
	WindowClassCount++;
	
	/* Register the SWT window class */
	int /*long*/ hHeap = OS.GetProcessHeap ();
	int /*long*/ hInstance = OS.GetModuleHandle (null);
	WNDCLASS lpWndClass = new WNDCLASS ();
	lpWndClass.hInstance = hInstance;
	lpWndClass.lpfnWndProc = windowProc;
	lpWndClass.style = OS.CS_BYTEALIGNWINDOW | OS.CS_DBLCLKS;
	lpWndClass.hCursor = OS.LoadCursor (0, OS.IDC_ARROW);
	/*
	* Set the default icon for the window class to IDI_APPLICATION.
	* This is not necessary for native Windows applications but
	* versions of Java starting at JDK 1.6 set the icon in the
	* executable instead of leaving the default.
	*/
	if (!OS.IsWinCE && Library.JAVA_VERSION >= Library.JAVA_VERSION (1, 6, 0)) {
		TCHAR lpszFile = new TCHAR (0, OS.MAX_PATH);
		while (OS.GetModuleFileName (0, lpszFile, lpszFile.length ()) == lpszFile.length ()) {
			lpszFile = new TCHAR (0, lpszFile.length () + OS.MAX_PATH);
		}
		if (OS.ExtractIconEx (lpszFile, -1, null, null, 1) != 0) {
			String fileName = lpszFile.toString (0, lpszFile.strlen ());
			if (fileName.endsWith ("java.exe") || fileName.endsWith ("javaw.exe")) { //$NON-NLS-1$ //$NON-NLS-2$
				lpWndClass.hIcon = OS.LoadIcon (0, OS.IDI_APPLICATION);
			}
		}
	}
	int byteCount = windowClass.length () * TCHAR.sizeof;
	lpWndClass.lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpWndClass.lpszClassName, windowClass, byteCount);
	OS.RegisterClass (lpWndClass);
	OS.HeapFree (hHeap, 0, lpWndClass.lpszClassName);

	/* Register the SWT drop shadow window class */
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		lpWndClass.style |= OS.CS_DROPSHADOW;
	}
	byteCount = windowShadowClass.length () * TCHAR.sizeof;
	lpWndClass.lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpWndClass.lpszClassName, windowShadowClass, byteCount);
	OS.RegisterClass (lpWndClass);
	OS.HeapFree (hHeap, 0, lpWndClass.lpszClassName);
	
	/* Register the CS_OWNDC window class */
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
		lpWndClass.style |= OS.CS_OWNDC;
	}
	byteCount = windowOwnDCClass.length () * TCHAR.sizeof;
	lpWndClass.lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpWndClass.lpszClassName, windowOwnDCClass, byteCount);
	OS.RegisterClass (lpWndClass);
	OS.HeapFree (hHeap, 0, lpWndClass.lpszClassName);
	
	/* Create the message only HWND */
	hwndMessage = OS.CreateWindowEx (0,
		windowClass,
		null,
		OS.WS_OVERLAPPED,
		0, 0, 0, 0,
		0,
		0,
		hInstance,
		null);
	String title = "SWT_Window_"+APP_NAME;
	OS.SetWindowText(hwndMessage, new TCHAR(0, title, true));
	messageCallback = new Callback (this, "messageProc", 4); //$NON-NLS-1$
	messageProc = messageCallback.getAddress ();
	if (messageProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.SetWindowLongPtr (hwndMessage, OS.GWLP_WNDPROC, messageProc);

	/* Create the filter hook */
	if (!OS.IsWinCE) {
		msgFilterCallback = new Callback (this, "msgFilterProc", 3); //$NON-NLS-1$
		msgFilterProc = msgFilterCallback.getAddress ();
		if (msgFilterProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		filterHook = OS.SetWindowsHookEx (OS.WH_MSGFILTER, msgFilterProc, 0, threadId);
	}
	
	/* Create the idle hook */
	if (!OS.IsWinCE) {
		foregroundIdleCallback = new Callback (this, "foregroundIdleProc", 3); //$NON-NLS-1$
		foregroundIdleProc = foregroundIdleCallback.getAddress ();
		if (foregroundIdleProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		idleHook = OS.SetWindowsHookEx (OS.WH_FOREGROUNDIDLE, foregroundIdleProc, 0, threadId);
	}
	
	/* Register window messages */
	TASKBARCREATED = OS.RegisterWindowMessage (new TCHAR (0, "TaskbarCreated", true)); //$NON-NLS-1$
	TASKBARBUTTONCREATED = OS.RegisterWindowMessage (new TCHAR (0, "TaskbarButtonCreated", true)); //$NON-NLS-1$
	SWT_RESTORECARET = OS.RegisterWindowMessage (new TCHAR (0, "SWT_RESTORECARET", true)); //$NON-NLS-1$
	DI_GETDRAGIMAGE = OS.RegisterWindowMessage (new TCHAR (0, "ShellGetDragImage", true)); //$NON-NLS-1$
	SWT_OPENDOC = OS.RegisterWindowMessage(new TCHAR (0, "SWT_OPENDOC", true)); //$NON-NLS-1$

	/* Initialize OLE */
	if (!OS.IsWinCE) OS.OleInitialize (0);
	
	if (appName != null) {
		/* Delete any old jump list set for the ID */
		int /*long*/ [] ppv = new int /*long*/ [1];
		int hr = OS.CoCreateInstance (TaskBar.CLSID_DestinationList, 0, OS.CLSCTX_INPROC_SERVER, TaskBar.IID_ICustomDestinationList, ppv);
		if (hr == OS.S_OK) {
			/*ICustomDestinationList::DeleteList*/
			OS.VtblCall (10, ppv [0], appName);
			/*IUnknown::Release*/
			OS.VtblCall (2, ppv [0]);
		}
	}
	
	/* Initialize buffered painting */
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)){
		OS.BufferedPaintInit ();
	}
	
	/* Initialize the Widget Table */
	indexTable = new int [GROW_SIZE];
	controlTable = new Control [GROW_SIZE];
	for (int i=0; i<GROW_SIZE-1; i++) indexTable [i] = i + 1;
	indexTable [GROW_SIZE - 1] = -1;
	
	/* Remember the last high contrast state */
	lastHighContrast = getHighContrast ();
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
public void internal_dispose_GC (int /*long*/ hDC, GCData data) {
	OS.ReleaseDC (0, hDC);
}

boolean isXMouseActive () {
	/*
	* NOTE: X-Mouse is active when bit 1 of the UserPreferencesMask is set.
  	*/
	boolean xMouseActive = false;
	TCHAR key = new TCHAR (0, "Control Panel\\Desktop", true); //$NON-NLS-1$
	int /*long*/ [] phKey = new int /*long*/ [1];
	int result = OS.RegOpenKeyEx (OS.HKEY_CURRENT_USER, key, 0, OS.KEY_READ, phKey);
	if (result == 0) {
		TCHAR lpValueName = new TCHAR (0, "UserPreferencesMask", true); //$NON-NLS-1$
		int [] lpcbData = new int [] {4}, lpData = new int [1];
		result = OS.RegQueryValueEx (phKey [0], lpValueName, 0, null, lpData, lpcbData);
		if (result == 0) xMouseActive = (lpData [0] & 0x01) != 0;
		OS.RegCloseKey (phKey [0]);
	}
	return xMouseActive;
}

boolean isValidThread () {
	return thread == Thread.currentThread ();
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
	if (from == to) return new Point (x, y);
	int /*long*/ hwndFrom = from != null ? from.handle : 0;
	int /*long*/ hwndTo = to != null ? to.handle : 0;
	POINT point = new POINT ();
	point.x = x;
	point.y = y;
	OS.MapWindowPoints (hwndFrom, hwndTo, point, 1);
	return new Point (point.x, point.y);
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
	if (from == to) return new Rectangle (x, y, width, height); 
	int /*long*/ hwndFrom = from != null ? from.handle : 0;
	int /*long*/ hwndTo = to != null ? to.handle : 0;
	RECT rect = new RECT ();
	rect.left = x;
	rect.top  = y;
	rect.right = x + width;
	rect.bottom = y + height;
	OS.MapWindowPoints (hwndFrom, hwndTo, rect, 2);
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

/*
 * Returns a single character, converted from the default
 * multi-byte character set (MBCS) used by the operating
 * system widgets to a wide character set (WCS) used by Java.
 *
 * @param ch the MBCS character
 * @return the WCS character
 */
static char mbcsToWcs (int ch) {
	return mbcsToWcs (ch, 0);
}

/*
 * Returns a single character, converted from the specified
 * multi-byte character set (MBCS) used by the operating
 * system widgets to a wide character set (WCS) used by Java.
 *
 * @param ch the MBCS character
 * @param codePage the code page used to convert the character
 * @return the WCS character
 */
static char mbcsToWcs (int ch, int codePage) {
	if (OS.IsUnicode) return (char) ch;
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
	char [] unicode = new char [1];
	int cp = codePage != 0 ? codePage : OS.CP_ACP;
	int count = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, buffer.length, unicode, 1);
	if (count == 0) return 0;
	return unicode [0];
}

int /*long*/ messageProc (int /*long*/ hwnd, int /*long*/ msg, int /*long*/ wParam, int /*long*/ lParam) {
	switch ((int)/*64*/msg) {
		case SWT_RUNASYNC: {
			if (runMessagesInIdle) runAsyncMessages (false);
			break;
		}
		case SWT_KEYMSG: {
			boolean consumed = false;
			MSG keyMsg = new MSG ();
			OS.MoveMemory (keyMsg, lParam, MSG.sizeof);
			Control control = findControl (keyMsg.hwnd);
			if (control != null) {
				/*
				* Feature in Windows.  When the user types an accent key such
				* as ^ in order to get an accented character on a German keyboard,
				* calling TranslateMessage(), ToUnicode() or ToAscii() consumes
				* the key.  This means that a subsequent call to TranslateMessage()
				* will see a regular key rather than the accented key.  The fix
				* is to use MapVirtualKey() and VkKeyScan () to detect an accent
				* and avoid calls to TranslateMessage().
				*/
				boolean accentKey = false;
				switch (keyMsg.message) {
					case OS.WM_KEYDOWN:
					case OS.WM_SYSKEYDOWN: {
						if (!OS.IsWinCE) {
							switch ((int)/*64*/keyMsg.wParam) {
								case OS.VK_SHIFT:
								case OS.VK_MENU:
								case OS.VK_CONTROL:
								case OS.VK_CAPITAL:
								case OS.VK_NUMLOCK:
								case OS.VK_SCROLL:
									break;
								default: {
									/* 
									* Bug in Windows. The high bit in the result of MapVirtualKey() on
									* Windows NT is bit 32 while the high bit on Windows 95 is bit 16.
									* They should both be bit 32.  The fix is to test the right bit.
									*/
									int mapKey = OS.MapVirtualKey ((int)/*64*/keyMsg.wParam, 2);
									if (mapKey != 0) {
										accentKey = (mapKey & (OS.IsWinNT ? 0x80000000 : 0x8000)) != 0;
										if (!accentKey) {
											for (int i=0; i<ACCENTS.length; i++) {
												int value = OS.VkKeyScan (ACCENTS [i]);
												if (value != -1 && (value & 0xFF) == keyMsg.wParam) {
													int state = value >> 8;
													if ((OS.GetKeyState (OS.VK_SHIFT) < 0) == ((state & 0x1) != 0) &&
														(OS.GetKeyState (OS.VK_CONTROL) < 0) == ((state & 0x2) != 0) &&
														(OS.GetKeyState (OS.VK_MENU) < 0) == ((state & 0x4) != 0)) {
															if ((state & 0x7) != 0) accentKey = true;
															break;
													}
												}
											}
										}
									}
									break;
								}
							}
						}
						break;
					}
				}
				if (!accentKey && !ignoreNextKey) {
					keyMsg.hwnd = control.handle;
					int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
					do {
						if (!(consumed |= filterMessage (keyMsg))) {
							OS.TranslateMessage (keyMsg);
							consumed |= OS.DispatchMessage (keyMsg) == 1;	
						}
					} while (OS.PeekMessage (keyMsg, keyMsg.hwnd, OS.WM_KEYFIRST, OS.WM_KEYLAST, flags));
				}
				switch (keyMsg.message) {
					case OS.WM_KEYDOWN:
					case OS.WM_SYSKEYDOWN: {
						switch ((int)/*64*/keyMsg.wParam) {
							case OS.VK_SHIFT:
							case OS.VK_MENU:
							case OS.VK_CONTROL:
							case OS.VK_CAPITAL:
							case OS.VK_NUMLOCK:
							case OS.VK_SCROLL:
								break;
							default: {
								ignoreNextKey = accentKey;
								break;
							}
						}
					}
				}
			}
			switch ((int)/*64*/keyMsg.wParam) {
				case OS.VK_SHIFT:
				case OS.VK_MENU:
				case OS.VK_CONTROL:
				case OS.VK_CAPITAL:
				case OS.VK_NUMLOCK:
				case OS.VK_SCROLL:
					consumed = true;
			}
			if (consumed) {
				int /*long*/ hHeap = OS.GetProcessHeap ();
				OS.HeapFree (hHeap, 0, lParam);
			} else {
				OS.PostMessage (embeddedHwnd, SWT_KEYMSG, wParam, lParam);
			}
			return 0;
		}
		case SWT_TRAYICONMSG: {
			if (tray != null) {
				TrayItem [] items = tray.items;
				for (int i=0; i<items.length; i++) {
					TrayItem item = items [i];
					if (item != null && item.id == wParam) {
						return item.messageProc (hwnd, (int)/*64*/msg, wParam, lParam);
					}
				}
			}
			return 0;
		}
		case OS.WM_ACTIVATEAPP: {
			/*
			* Feature in Windows.  When multiple shells are
			* disabled and one of the shells has an enabled
			* dialog child and the user selects a disabled
			* shell that does not have the enabled dialog
			* child using the Task bar, Windows brings the
			* disabled shell to the front.  As soon as the
			* user clicks on the disabled shell, the enabled
			* dialog child comes to the front.  This behavior
			* is unspecified and seems strange.  Normally, a
			* disabled shell is frozen on the screen and the
			* user cannot change the z-order by clicking with
			* the mouse.  The fix is to look for WM_ACTIVATEAPP
			* and force the enabled dialog child to the front.
			* This is typically what the user is expecting.
			* 
			* NOTE: If the modal shell is disabled for any
			* reason, it should not be brought to the front.
			*/
			if (wParam != 0) {
				if (!isXMouseActive ()) {
					int /*long*/ hwndActive = OS.GetActiveWindow ();
					if (hwndActive != 0 && OS.IsWindowEnabled (hwndActive)) break;
					Shell modal = modalDialog != null ? modalDialog.parent : getModalShell ();
					if (modal != null && !modal.isDisposed ()) {
						int /*long*/ hwndModal = modal.handle;
						if (OS.IsWindowEnabled (hwndModal)) {
							modal.bringToTop ();
							if (modal.isDisposed ()) break;
						}
						int /*long*/ hwndPopup = OS.GetLastActivePopup (hwndModal);
						if (hwndPopup != 0 && hwndPopup != modal.handle) {
							if (getControl (hwndPopup) == null) {
								if (OS.IsWindowEnabled (hwndPopup)) {
									OS.SetActiveWindow (hwndPopup);
								}
							}
						}
					}
				}
			}
			break;
		}
		case OS.WM_ENDSESSION: {
			if (wParam != 0) {
				dispose ();
				/*
				* When the session is ending, no SWT program can continue
				* to run.  In order to avoid running code after the display
				* has been disposed, exit from Java.
				*/
				/* This code is intentionally commented */
//				System.exit (0);
			}
			break;
		}
		case OS.WM_QUERYENDSESSION: {
			Event event = new Event ();
			sendEvent (SWT.Close, event);
			if (!event.doit) return 0;
			break;
		}
		case OS.WM_DWMCOLORIZATIONCOLORCHANGED: {
			sendSettings = true;
			//FALL THROUGH
		}
		case OS.WM_SETTINGCHANGE: {
			/*
			* Bug in Windows.  When high contrast is cleared using
			* the key sequence, Alt + Left Shift + Print Screen, the
			* system parameter is set to false, but WM_SETTINGCHANGE
			* is not sent with SPI_SETHIGHCONTRAST.  The fix is to
			* detect the change when any WM_SETTINGCHANGE message
			* is sent.
			*/
			if (lastHighContrast != getHighContrast ()) {
				sendSettings = true;
				lastHighContrast = getHighContrast ();
			}
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
				sendSettings = true;
			} 
			switch ((int)/*64*/wParam) {
				case 0:
				case 1:
				case OS.SPI_SETHIGHCONTRAST: {
					sendSettings = true;
					lastHighContrast = getHighContrast ();
				}
			}
			/* Set the initial timer or push the time out period forward */
			if (sendSettings) {
				OS.SetTimer (hwndMessage, SETTINGS_ID, SETTINGS_DELAY, 0);
			}
			break;
		}
		case OS.WM_THEMECHANGED: {
			if (OS.COMCTL32_MAJOR >= 6) {
				if (hButtonTheme != 0) OS.CloseThemeData (hButtonTheme);
				if (hEditTheme != 0) OS.CloseThemeData (hEditTheme);
				if (hExplorerBarTheme != 0) OS.CloseThemeData (hExplorerBarTheme);
				if (hScrollBarTheme != 0) OS.CloseThemeData (hScrollBarTheme);
				if (hTabTheme != 0) OS.CloseThemeData (hTabTheme);
				hButtonTheme = hEditTheme = hExplorerBarTheme = hScrollBarTheme = hTabTheme = 0;
			}
			break;
		}
		case OS.WM_TIMER: {
			if (wParam == SETTINGS_ID) {
				sendSettings = false;
				OS.KillTimer (hwndMessage, SETTINGS_ID);
				runSettings ();
			} else {
				runTimer (wParam);
			}
			break;
		}
		default: {
			if ((int)/*64*/msg == TASKBARCREATED) {
				if (tray != null) {
					TrayItem [] items = tray.items;
					for (int i=0; i<items.length; i++) {
						TrayItem item = items [i];
						if (item != null) item.recreate ();
					}
				}
			}
			if ((int)/*64*/msg == SWT_OPENDOC) {
				String filename = getSharedData((int)/*64*/wParam, (int)/*64*/lParam);
				if (filename != null) {
					if (filename.startsWith (TASKBAR_EVENT)) {
						String text = filename.substring (TASKBAR_EVENT.length ());
						int id = Integer.parseInt (text);
						MenuItem item = getMenuItem (id);
						if (item != null) {
							item.sendSelectionEvent (SWT.Selection);
						}
					} else {
						Event event = new Event();
						event.text = filename;
						sendEvent(SWT.OpenDocument, event);
					}
					wakeThread ();
				}
			}
		}
	}
	return OS.DefWindowProc (hwnd, (int)/*64*/msg, wParam, lParam);
}

String getSharedData(int pid, int  handle) {
	if (OS.IsWinCE) return null;
	int /*long*/ [] mapHandle = new int /*long*/ [1];
	if (pid == OS.GetCurrentProcessId()) {
		mapHandle[0] = handle;
	} else {
		int /*long*/ processHandle = OS.OpenProcess(OS.PROCESS_VM_READ|OS.PROCESS_DUP_HANDLE, false, pid);
		if (processHandle == 0) return null;
		OS.DuplicateHandle(processHandle, handle, OS.GetCurrentProcess(), mapHandle, OS.DUPLICATE_SAME_ACCESS, false, OS.DUPLICATE_SAME_ACCESS);
		OS.CloseHandle(processHandle);
	}
	
	int /*long*/ sharedData = OS.MapViewOfFile(mapHandle[0], OS.FILE_MAP_READ, 0, 0, 0);
	if (sharedData == 0) return null;
	int length = OS.IsUnicode ? OS.wcslen (sharedData) : OS.strlen (sharedData);
	TCHAR buffer = new TCHAR (0, length);
	int byteCount = buffer.length () * TCHAR.sizeof;
	OS.MoveMemory (buffer, sharedData, byteCount);
	String result = buffer.toString (0, length);
	OS.UnmapViewOfFile(sharedData);
	if (handle != mapHandle[0]) {
		OS.CloseHandle(mapHandle[0]);
	}
	return result;
}

int /*long*/ monitorEnumProc (int /*long*/ hmonitor, int /*long*/ hdc, int /*long*/ lprcMonitor, int /*long*/ dwData) {
	if (monitorCount >= monitors.length) {
		Monitor[] newMonitors = new Monitor [monitors.length + 4];
		System.arraycopy (monitors, 0, newMonitors, 0, monitors.length);
		monitors = newMonitors;
	}
	MONITORINFO lpmi = new MONITORINFO ();
	lpmi.cbSize = MONITORINFO.sizeof;
	OS.GetMonitorInfo (hmonitor, lpmi);
	Monitor monitor = new Monitor ();
	monitor.handle = hmonitor;
	monitor.x = lpmi.rcMonitor_left;
	monitor.y = lpmi.rcMonitor_top;
	monitor.width = lpmi.rcMonitor_right - lpmi.rcMonitor_left;
	monitor.height = lpmi.rcMonitor_bottom - lpmi.rcMonitor_top;
	monitor.clientX = lpmi.rcWork_left;
	monitor.clientY = lpmi.rcWork_top;
	monitor.clientWidth = lpmi.rcWork_right - lpmi.rcWork_left;
	monitor.clientHeight = lpmi.rcWork_bottom - lpmi.rcWork_top;
	monitors [monitorCount++] = monitor;
	return 1;
}

int /*long*/ msgFilterProc (int /*long*/ code, int /*long*/ wParam, int /*long*/ lParam) {
	switch ((int)/*64*/code) {
		case OS.MSGF_COMMCTRL_BEGINDRAG: {
			if (!runDragDrop && !dragCancelled) {
				OS.MoveMemory (hookMsg, lParam, MSG.sizeof);
				if (hookMsg.message == OS.WM_MOUSEMOVE) {
					dragCancelled = true;
					OS.SendMessage (hookMsg.hwnd, OS.WM_CANCELMODE, 0, 0);
				}
			}
			break;
		}
		/*
		* Feature in Windows.  For some reason, when the user clicks
		* a table or tree, the Windows hook WH_MSGFILTER is sent when
		* an input event from a dialog box, message box, menu, or scroll
		* bar did not occur, causing async messages to run at the wrong
		* time.  The fix is to check the message filter code.
		*/
		case OS.MSGF_DIALOGBOX:
		case OS.MSGF_MAINLOOP:
		case OS.MSGF_MENU:
		case OS.MSGF_MOVE:
		case OS.MSGF_MESSAGEBOX:
		case OS.MSGF_NEXTWINDOW:
		case OS.MSGF_SCROLLBAR:
		case OS.MSGF_SIZE: {
			if (runMessages) {
				OS.MoveMemory (hookMsg, lParam, MSG.sizeof);
				if (hookMsg.message == OS.WM_NULL) {
					MSG msg = new MSG ();
					int flags = OS.PM_NOREMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
					if (!OS.PeekMessage (msg, 0, 0, 0, flags)) {
						if (runAsyncMessages (false)) wakeThread ();
					}
				}
			}
			break;
		}
	}
	return OS.CallNextHookEx (filterHook, (int)/*64*/code, wParam, lParam);
}

int numpadKey (int key) {
	switch (key) {
		case OS.VK_NUMPAD0:	return '0';
		case OS.VK_NUMPAD1:	return '1';
		case OS.VK_NUMPAD2:	return '2';
		case OS.VK_NUMPAD3:	return '3';
		case OS.VK_NUMPAD4:	return '4';
		case OS.VK_NUMPAD5:	return '5';
		case OS.VK_NUMPAD6:	return '6';
		case OS.VK_NUMPAD7:	return '7';
		case OS.VK_NUMPAD8:	return '8';
		case OS.VK_NUMPAD9:	return '9';
		case OS.VK_MULTIPLY:	return '*';
		case OS.VK_ADD: 		return '+';
		case OS.VK_SEPARATOR:	return '\0';
		case OS.VK_SUBTRACT:	return '-';
		case OS.VK_DECIMAL:	return '.';
		case OS.VK_DIVIDE:		return '/';
	}
	return 0;
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
		int type = event.type;
		switch (type){
			case SWT.KeyDown:
			case SWT.KeyUp: {
				KEYBDINPUT inputs = new KEYBDINPUT ();
				inputs.wVk = (short) untranslateKey (event.keyCode);
				if (inputs.wVk == 0) {
					char key = event.character;
					switch (key) {
						case SWT.BS: inputs.wVk = (short) OS.VK_BACK; break;
						case SWT.CR: inputs.wVk = (short) OS.VK_RETURN; break;
						case SWT.DEL: inputs.wVk = (short) OS.VK_DELETE; break;
						case SWT.ESC: inputs.wVk = (short) OS.VK_ESCAPE; break;
						case SWT.TAB: inputs.wVk = (short) OS.VK_TAB; break;
						/*
						* Since there is no LF key on the keyboard, do not attempt
						* to map LF to CR or attempt to post an LF key.
						*/
//						case SWT.LF: inputs.wVk = (short) OS.VK_RETURN; break;
						case SWT.LF: return false;
						default: {
							if (OS.IsWinCE) {
								inputs.wVk = (short)/*64*/OS.CharUpper ((short) key);
							} else {
								inputs.wVk = OS.VkKeyScan ((short) wcsToMbcs (key, 0));
								if (inputs.wVk == -1) return false;
								inputs.wVk &= 0xFF;
							}
						}
					}
				}
				inputs.dwFlags = type == SWT.KeyUp ? OS.KEYEVENTF_KEYUP : 0;
				switch (inputs.wVk) {
					case OS.VK_INSERT:
					case OS.VK_DELETE:
					case OS.VK_HOME:
					case OS.VK_END:
					case OS.VK_PRIOR:
					case OS.VK_NEXT:
					case OS.VK_UP:
					case OS.VK_DOWN:
					case OS.VK_LEFT:
					case OS.VK_RIGHT:
					case OS.VK_NUMLOCK:
					case OS.VK_SNAPSHOT:
					case OS.VK_CANCEL:
					case OS.VK_DIVIDE:
						inputs.dwFlags |= OS.KEYEVENTF_EXTENDEDKEY;
				}
				int /*long*/ hHeap = OS.GetProcessHeap ();
				int /*long*/ pInputs = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, INPUT.sizeof);
				OS.MoveMemory(pInputs, new int[] {OS.INPUT_KEYBOARD}, 4);
				//TODO - DWORD type of INPUT structure aligned to 8 bytes on 64 bit
				OS.MoveMemory (pInputs + OS.PTR_SIZEOF, inputs, KEYBDINPUT.sizeof);
				boolean result = OS.SendInput (1, pInputs, INPUT.sizeof) != 0;
				OS.HeapFree (hHeap, 0, pInputs);
				return result;
			}
			case SWT.MouseDown:
			case SWT.MouseMove:
			case SWT.MouseUp:
			case SWT.MouseWheel: {
				MOUSEINPUT inputs = new MOUSEINPUT ();
				if (type == SWT.MouseMove){
					inputs.dwFlags = OS.MOUSEEVENTF_MOVE | OS.MOUSEEVENTF_ABSOLUTE;
					int x= 0, y = 0, width = 0, height = 0;
					if (OS.WIN32_VERSION >= OS.VERSION (5, 0)) {
						inputs.dwFlags |= OS.MOUSEEVENTF_VIRTUALDESK;
						x = OS.GetSystemMetrics (OS.SM_XVIRTUALSCREEN);
						y = OS.GetSystemMetrics (OS.SM_YVIRTUALSCREEN);	
						width = OS.GetSystemMetrics (OS.SM_CXVIRTUALSCREEN);
						height = OS.GetSystemMetrics (OS.SM_CYVIRTUALSCREEN);
					} else {
						width = OS.GetSystemMetrics (OS.SM_CXSCREEN);
						height = OS.GetSystemMetrics (OS.SM_CYSCREEN);
					}
					inputs.dx = ((event.x - x) * 65535 + width - 2) / (width - 1);
					inputs.dy = ((event.y - y) * 65535 + height - 2) / (height - 1);
				} else {
					if (type == SWT.MouseWheel) {
						if (OS.WIN32_VERSION < OS.VERSION (5, 0)) return false;
						inputs.dwFlags = OS.MOUSEEVENTF_WHEEL;
						switch (event.detail) {
							case SWT.SCROLL_PAGE:
								inputs.mouseData = event.count * OS.WHEEL_DELTA;
								break;
							case SWT.SCROLL_LINE:
								int [] value = new int [1];
								OS.SystemParametersInfo (OS.SPI_GETWHEELSCROLLLINES, 0, value, 0);
								inputs.mouseData = event.count * OS.WHEEL_DELTA / value [0];
								break;
							default: return false;
						}
					} else {
						switch (event.button) {
							case 1: inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_LEFTDOWN : OS.MOUSEEVENTF_LEFTUP; break;
							case 2: inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_MIDDLEDOWN : OS.MOUSEEVENTF_MIDDLEUP; break;
							case 3: inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_RIGHTDOWN : OS.MOUSEEVENTF_RIGHTUP; break;
							case 4: {
								if (OS.WIN32_VERSION < OS.VERSION (5, 0)) return false;
								inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_XDOWN : OS.MOUSEEVENTF_XUP;
								inputs.mouseData = OS.XBUTTON1;
								break;
							}
							case 5: {
								if (OS.WIN32_VERSION < OS.VERSION (5, 0)) return false;
								inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_XDOWN : OS.MOUSEEVENTF_XUP;
								inputs.mouseData = OS.XBUTTON2;
								break;
							}
							default: return false;
						}
					}
				}
				int /*long*/ hHeap = OS.GetProcessHeap ();
				int /*long*/ pInputs = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, INPUT.sizeof);
				OS.MoveMemory(pInputs, new int[] {OS.INPUT_MOUSE}, 4);
				//TODO - DWORD type of INPUT structure aligned to 8 bytes on 64 bit
				OS.MoveMemory (pInputs + OS.PTR_SIZEOF, inputs, MOUSEINPUT.sizeof);
				boolean result = OS.SendInput (1, pInputs, INPUT.sizeof) != 0;
				OS.HeapFree (hHeap, 0, pInputs);
				return result;
			}
		} 
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
	lpStartupInfo = null;
	drawMenuBars ();
	runSkin ();
	runDeferredLayouts ();
	runPopups ();
	if (OS.PeekMessage (msg, 0, 0, 0, OS.PM_REMOVE)) {
		if (!filterMessage (msg)) {
			OS.TranslateMessage (msg);
			OS.DispatchMessage (msg);
		}
		runDeferredEvents ();
		return true;
	}
	return isDisposed () || (runMessages && runAsyncMessages (false));
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
	releaseDisplay ();
	super.release ();
}

void releaseDisplay () {
	if (embeddedHwnd != 0) {
		OS.PostMessage (embeddedHwnd, SWT_DESTROY, 0, 0);
	}
	
	/* Release XP Themes */
	if (OS.COMCTL32_MAJOR >= 6) {
		if (hButtonTheme != 0) OS.CloseThemeData (hButtonTheme);
		if (hEditTheme != 0) OS.CloseThemeData (hEditTheme);
		if (hExplorerBarTheme != 0) OS.CloseThemeData (hExplorerBarTheme);
		if (hScrollBarTheme != 0) OS.CloseThemeData (hScrollBarTheme);
		if (hTabTheme != 0) OS.CloseThemeData (hTabTheme);
		hButtonTheme = hEditTheme = hExplorerBarTheme = hScrollBarTheme = hTabTheme = 0;
	}
	
	/* Unhook the message hook */
	if (!OS.IsWinCE) {
		if (msgHook != 0) OS.UnhookWindowsHookEx (msgHook);
		msgHook = 0;
	}

	/* Unhook the filter hook */
	if (!OS.IsWinCE) {
		if (filterHook != 0) OS.UnhookWindowsHookEx (filterHook);
		filterHook = 0;
		msgFilterCallback.dispose ();
		msgFilterCallback = null;
		msgFilterProc = 0;
	}
	
	/* Unhook the idle hook */
	if (!OS.IsWinCE) {
		if (idleHook != 0) OS.UnhookWindowsHookEx (idleHook);
		idleHook = 0;
		foregroundIdleCallback.dispose ();
		foregroundIdleCallback = null;
		foregroundIdleProc = 0;
	}
	
	/* Stop the settings timer */
	OS.KillTimer (hwndMessage, SETTINGS_ID);
	
	/* Destroy the message only HWND */
	if (hwndMessage != 0) OS.DestroyWindow (hwndMessage);
	hwndMessage = 0;
	messageCallback.dispose ();
	messageCallback = null;
	messageProc = 0;
	
	/* Unregister the SWT window class */
	int /*long*/ hHeap = OS.GetProcessHeap ();
	int /*long*/ hInstance = OS.GetModuleHandle (null);
	OS.UnregisterClass (windowClass, hInstance);
	
	/* Unregister the SWT drop shadow and CS_OWNDC window class */
	OS.UnregisterClass (windowShadowClass, hInstance);
	OS.UnregisterClass (windowOwnDCClass, hInstance);
	windowClass = windowShadowClass = windowOwnDCClass = null;
	windowCallback.dispose ();
	windowCallback = null;
	windowProc = 0;
	
	/* Release the System fonts */
	if (systemFont != null) systemFont.dispose ();
	systemFont = null;
	lfSystemFont = null;
	
	/* Release the System Images */
	if (errorImage != null) errorImage.dispose ();
	if (infoImage != null) infoImage.dispose ();
	if (questionImage != null) questionImage.dispose ();
	if (warningIcon != null) warningIcon.dispose ();
	errorImage = infoImage = questionImage = warningIcon = null;
	
	/* Release Sort Indicators */
	if (upArrow != null) upArrow.dispose ();
	if (downArrow != null) downArrow.dispose ();
	upArrow = downArrow = null;
	
	/* Release the System Cursors */
	for (int i = 0; i < cursors.length; i++) {
		if (cursors [i] != null) cursors [i].dispose ();
	}
	cursors = null;

	/* Release Acquired Resources */
	if (resources != null) {
		for (int i=0; i<resources.length; i++) {
			if (resources [i] != null) resources [i].dispose ();
		}
		resources = null;
	}

	/* Release Custom Colors for ChooseColor */
	if (lpCustColors != 0) OS.HeapFree (hHeap, 0, lpCustColors);
	lpCustColors = 0;
	
	/* Uninitialize OLE */
	if (!OS.IsWinCE) OS.OleUninitialize ();

	/* Uninitialize buffered painting */
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
		OS.BufferedPaintUnInit ();
	}
	
	/* Release references */
	thread = null;
	msg = hookMsg = null;
	keyboard = null;
	modalDialog = null;
	modalShells = null;
	data = null;
	keys = null;
	values = null;
	bars = popups = null;
	indexTable = null; 
	timerIds = null;
	controlTable = null;
	lastControl = lastGetControl = lastHittestControl = null;
	imageList = toolImageList = toolHotImageList = toolDisabledImageList = null;
	timerList = null;
	tableBuffer = null;
	columnVisible = null;
	eventTable = filterTable = null;
	items = null;
	clickRect = null;
	hdr = null;
	plvfi = null;
	monitors = null;
	touchSources = null;
	
	/* Release handles */
	threadId = 0;
}

void releaseImageList (ImageList list) {
	int i = 0;
	int length = imageList.length; 
	while (i < length) {
		if (imageList [i] == list) {
			if (list.removeRef () > 0) return;
			list.dispose ();
			System.arraycopy (imageList, i + 1, imageList, i, --length - i);
			imageList [length] = null;
			for (int j=0; j<length; j++) {
				if (imageList [j] != null) return;
			}
			imageList = null;
			return;
		}
		i++;
	}
}

void releaseToolImageList (ImageList list) {
	int i = 0;
	int length = toolImageList.length; 
	while (i < length) {
		if (toolImageList [i] == list) {
			if (list.removeRef () > 0) return;
			list.dispose ();
			System.arraycopy (toolImageList, i + 1, toolImageList, i, --length - i);
			toolImageList [length] = null;
			for (int j=0; j<length; j++) {
				if (toolImageList [j] != null) return;
			}
			toolImageList = null;
			return;
		}
		i++;
	}
}

void releaseToolHotImageList (ImageList list) {
	int i = 0;
	int length = toolHotImageList.length; 
	while (i < length) {
		if (toolHotImageList [i] == list) {
			if (list.removeRef () > 0) return;
			list.dispose ();
			System.arraycopy (toolHotImageList, i + 1, toolHotImageList, i, --length - i);
			toolHotImageList [length] = null;
			for (int j=0; j<length; j++) {
				if (toolHotImageList [j] != null) return;
			}
			toolHotImageList = null;
			return;
		}
		i++;
	}
}

void releaseToolDisabledImageList (ImageList list) {
	int i = 0;
	int length = toolDisabledImageList.length; 
	while (i < length) {
		if (toolDisabledImageList [i] == list) {
			if (list.removeRef () > 0) return;
			list.dispose ();
			System.arraycopy (toolDisabledImageList, i + 1, toolDisabledImageList, i, --length - i);
			toolDisabledImageList [length] = null;
			for (int j=0; j<length; j++) {
				if (toolDisabledImageList [j] != null) return;
			}
			toolDisabledImageList = null;
			return;
		}
		i++;
	}
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

void removeBar (Menu menu) {
	if (bars == null) return;
	for (int i=0; i<bars.length; i++) {
		if (bars [i] == menu) {
			bars [i] = null;
			return;
		}
	}
}

Control removeControl (int /*long*/ handle) {
	if (handle == 0) return null;
	lastControl = lastGetControl = null;
	Control control = null;
	int index;
	if (USE_PROPERTY) {
		index = (int)/*64*/OS.RemoveProp (handle, SWT_OBJECT_INDEX) - 1;
	} else {
		index = (int)/*64*/OS.GetWindowLongPtr (handle, OS.GWLP_USERDATA) - 1;
		OS.SetWindowLongPtr (handle, OS.GWLP_USERDATA, 0);
	}
	if (0 <= index && index < controlTable.length) {
		control = controlTable [index];
		controlTable [index] = null;
		indexTable [index] = freeSlot;
		freeSlot = index;
	}
	return control;
}

void removeMenuItem (MenuItem item) {
	if (items == null) return;
	items [item.id - ID_START] = null;
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
		* be null due to a recursive invocation
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

void runSettings () {
	Font oldFont = getSystemFont ();
	saveResources ();
	updateImages ();
	sendEvent (SWT.Settings, null);
	Font newFont = getSystemFont ();
	boolean sameFont = oldFont.equals (newFont);
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			if (!sameFont) {
				shell.updateFont (oldFont, newFont);
			}
			/* This code is intentionally commented */
			//shell.redraw (true);
			shell.layout (true, true);
		}
	}
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
	
boolean runTimer (int /*long*/ id) {
	if (timerList != null && timerIds != null) {
		int index = 0;
		while (index <timerIds.length) {
			if (timerIds [index] == id) {
				OS.KillTimer (hwndMessage, timerIds [index]);
				timerIds [index] = 0;
				Runnable runnable = timerList [index];
				timerList [index] = null;
				if (runnable != null) runnable.run ();
				return true;
			}
			index++;
		}
	}
	return false;
}

void saveResources () {
	int resourceCount = 0;
	if (resources == null) {
		resources = new Resource [RESOURCE_SIZE];
	} else {
		resourceCount = resources.length;
		Resource [] newResources = new Resource [resourceCount + RESOURCE_SIZE];
		System.arraycopy (resources, 0, newResources, 0, resourceCount);
		resources = newResources;
	}
	if (systemFont != null) {
		if (!OS.IsWinCE) {
			NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
			info.cbSize = NONCLIENTMETRICS.sizeof;
			if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
				LOGFONT logFont = OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfMessageFont : ((NONCLIENTMETRICSA)info).lfMessageFont;
				if (lfSystemFont == null ||
					logFont.lfCharSet != lfSystemFont.lfCharSet ||
					logFont.lfHeight != lfSystemFont.lfHeight ||
					logFont.lfWidth != lfSystemFont.lfWidth ||
					logFont.lfEscapement != lfSystemFont.lfEscapement ||
					logFont.lfOrientation != lfSystemFont.lfOrientation ||
					logFont.lfWeight != lfSystemFont.lfWeight ||
					logFont.lfItalic != lfSystemFont.lfItalic ||
					logFont.lfUnderline != lfSystemFont.lfUnderline ||
					logFont.lfStrikeOut != lfSystemFont.lfStrikeOut ||
					logFont.lfCharSet != lfSystemFont.lfCharSet ||
					logFont.lfOutPrecision != lfSystemFont.lfOutPrecision ||
					logFont.lfClipPrecision != lfSystemFont.lfClipPrecision ||
					logFont.lfQuality != lfSystemFont.lfQuality ||
					logFont.lfPitchAndFamily != lfSystemFont.lfPitchAndFamily ||
					!getFontName (logFont).equals (getFontName (lfSystemFont))) {
						resources [resourceCount++] = systemFont;
						lfSystemFont = logFont;
						systemFont = null;
				}
			}
		}
	}
	if (errorImage != null) resources [resourceCount++] = errorImage;
	if (infoImage != null) resources [resourceCount++] = infoImage;
	if (questionImage != null) resources [resourceCount++] = questionImage;
	if (warningIcon != null) resources [resourceCount++] = warningIcon;
	errorImage = infoImage = questionImage = warningIcon = null;
	for (int i=0; i<cursors.length; i++) {
		if (cursors [i] != null) resources [resourceCount++] = cursors [i];
		cursors [i] = null;
	}
	if (resourceCount < RESOURCE_SIZE) {
		Resource [] newResources = new Resource [resourceCount];
		System.arraycopy (resources, 0, newResources, 0, resourceCount);
		resources = newResources;
	}
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
	OS.SetCursorPos (x, y);
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

	if (key.equals (RUN_MESSAGES_IN_IDLE_KEY)) {
		Boolean data = (Boolean) value;
		runMessagesInIdle = data != null && data.booleanValue ();
		return;
	}
	if (key.equals (RUN_MESSAGES_IN_MESSAGE_PROC_KEY)) {
		Boolean data = (Boolean) value;
		runMessagesInMessageProc = data != null && data.booleanValue ();
		return;
	}
	if (key.equals (USE_OWNDC_KEY)) {
		Boolean data = (Boolean) value;
		useOwnDC = data != null && data.booleanValue ();
		return;
	}
	if (key.equals (ACCEL_KEY_HIT)) {
		Boolean data = (Boolean) value;
		accelKeyHit = data != null && data.booleanValue ();
		return;
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

void setModalDialog (Dialog modalDailog) {
	this.modalDialog = modalDailog;
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) shells [i].updateModal ();
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

int shiftedKey (int key) {
	if (OS.IsWinCE) return 0;
	
	/* Clear the virtual keyboard and press the shift key */
	for (int i=0; i<keyboard.length; i++) keyboard [i] = 0;
	keyboard [OS.VK_SHIFT] |= 0x80;

	/* Translate the key to ASCII or UNICODE using the virtual keyboard */
	if (OS.IsUnicode) {
		char [] result = new char [1];
		if (OS.ToUnicode (key, key, keyboard, result, 1, 0) == 1) return result [0];
	} else {
		short [] result = new short [1];
		if (OS.ToAscii (key, key, keyboard, result, 0) == 1) return result [0];
	}
	return 0;
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
	if (runMessages && getMessageCount () != 0) return true;
	if (OS.IsWinCE) {
		OS.MsgWaitForMultipleObjectsEx (0, 0, OS.INFINITE, OS.QS_ALLINPUT, OS.MWMO_INPUTAVAILABLE);
		return true;
	}
	return OS.WaitMessage ();
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
	if (runnable == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (timerList == null) timerList = new Runnable [4];
	if (timerIds == null) timerIds = new int /*long*/ [4];
	int index = 0;
	while (index < timerList.length) {
		if (timerList [index] == runnable) break;
		index++;
	}
	int /*long*/ timerId = 0;
	if (index != timerList.length) {
		timerId = timerIds [index];
		if (milliseconds < 0) {			
			OS.KillTimer (hwndMessage, timerId);
			timerList [index] = null;
			timerIds [index] = 0;
			return;
		}
	} else {
		if (milliseconds < 0) return;
		index = 0;
		while (index < timerList.length) {
			if (timerList [index] == null) break;
			index++;
		}
		timerId = nextTimerId++;
		if (index == timerList.length) {
			Runnable [] newTimerList = new Runnable [timerList.length + 4];
			System.arraycopy (timerList, 0, newTimerList, 0, timerList.length);
			timerList = newTimerList;
			int /*long*/ [] newTimerIds = new int /*long*/ [timerIds.length + 4];
			System.arraycopy (timerIds, 0, newTimerIds, 0, timerIds.length);
			timerIds = newTimerIds;
		}
	}
	int /*long*/ newTimerID = OS.SetTimer (hwndMessage, timerId, milliseconds, 0);
	if (newTimerID != 0) {
		timerList [index] = runnable;
		timerIds [index] = newTimerID;
	}
}

boolean translateAccelerator (MSG msg, Control control) {
	accelKeyHit = true;
	boolean result = control.translateAccelerator (msg);
	accelKeyHit = false;
	return result;
}

static int translateKey (int key) {
	for (int i=0; i<KeyTable.length; i++) {
		if (KeyTable [i] [0] == key) return KeyTable [i] [1];
	}
	return 0;
}

boolean translateMnemonic (MSG msg, Control control) {
	switch (msg.message) {
		case OS.WM_CHAR:
		case OS.WM_SYSCHAR:
			return control.translateMnemonic (msg);
	}
	return false;
}

boolean translateTraversal (MSG msg, Control control) {
	switch (msg.message) {
		case OS.WM_KEYDOWN:
			switch ((int)/*64*/msg.wParam) {
				case OS.VK_RETURN:
				case OS.VK_ESCAPE:
				case OS.VK_TAB:
				case OS.VK_UP:
				case OS.VK_DOWN:
				case OS.VK_LEFT:
				case OS.VK_RIGHT:
				case OS.VK_PRIOR:
				case OS.VK_NEXT:
					return control.translateTraversal (msg);
			}
			break;
		case OS.WM_SYSKEYDOWN:
			switch ((int)/*64*/msg.wParam) {
				case OS.VK_MENU:
					return control.translateTraversal (msg);
			}
			break;
	}
	return false;
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
	/*
	* Feature in Windows.  When an application does not remove
	* events from the event queue for some time, Windows assumes
	* the application is not responding and no longer sends paint
	* events to the application.  The fix is to detect that the
	* application is not responding and call PeekMessage() with
	* PM_REMOVE to tell Windows that the application is ready
	* to dispatch events.  Note that the message does not have
	* to be found or dispatched in order to wake Windows up.
	* 
	* NOTE: This allows other cross thread messages to be delivered,
	* most notably WM_ACTIVATE.
	*/	
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
		if (OS.IsHungAppWindow (hwndMessage)) {
			MSG msg = new MSG ();
			int flags = OS.PM_REMOVE | OS.PM_NOYIELD;
			OS.PeekMessage (msg, hwndMessage, SWT_NULL, SWT_NULL, flags);
		}
	}
	Shell[] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.update (true);
	}
}

void updateImages () {
	if (upArrow != null) upArrow.dispose ();
	if (downArrow != null) downArrow.dispose ();
	upArrow = downArrow = null;
	for (int i=0; i<controlTable.length; i++) {
		Control control = controlTable [i];
		if (control != null) control.updateImages ();
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
	if (OS.IsWinCE) {
		OS.PostMessage (hwndMessage, OS.WM_NULL, 0, 0);
	} else {
		OS.PostThreadMessage (threadId, OS.WM_NULL, 0, 0);
	}
}

/*
 * Returns a single character, converted from the wide
 * character set (WCS) used by Java to the specified
 * multi-byte character set used by the operating system
 * widgets.
 *
 * @param ch the WCS character
 * @param codePage the code page used to convert the character
 * @return the MBCS character
 */
static int wcsToMbcs (char ch, int codePage) {
	if (OS.IsUnicode) return ch;
	if (ch <= 0x7F) return ch;
	TCHAR buffer = new TCHAR (codePage, ch, false);
	return buffer.tcharAt (0);
}

/*
 * Returns a single character, converted from the wide
 * character set (WCS) used by Java to the default
 * multi-byte character set used by the operating system
 * widgets.
 *
 * @param ch the WCS character
 * @return the MBCS character
 */
static int wcsToMbcs (char ch) {
	return wcsToMbcs (ch, 0);
}

int /*long*/ windowProc (int /*long*/ hwnd, int /*long*/ msg, int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Feature in Windows.  On Vista only, it is faster to
	* compute and answer the data for the visible columns
	* of a table when scrolling, rather than just return
	* the data for each column when asked.
	*/
	if (columnVisible != null) {
		if (msg == OS.WM_NOTIFY && hwndParent == hwnd) {
			OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
			switch (hdr.code) {
				case OS.LVN_GETDISPINFOA:
				case OS.LVN_GETDISPINFOW: {
					OS.MoveMemory (plvfi, lParam, NMLVDISPINFO.sizeof);
					if (0 <= plvfi.iSubItem && plvfi.iSubItem < columnCount) {
						if (!columnVisible [plvfi.iSubItem]) return 0;
					}
					break;
				}
			}
		}
	}
	if ((int)/*64*/msg == TASKBARBUTTONCREATED) {
		if (taskBar != null) {
			TaskItem [] items = taskBar.items;
			for (int i=0; i<items.length; i++) {
				TaskItem item = items [i];
				if (item != null && item.shell != null && item.shell.handle == hwnd) {
					item.recreate ();
					break;
				}
			}
		}
	}
	/*
	* Bug in Adobe Reader 7.0.  For some reason, when Adobe
	* Reader 7.0 is deactivated from within Internet Explorer,
	* it sends thousands of consecutive WM_NCHITTEST messages
	* to the control that is under the cursor.  It seems that
	* if the control takes some time to respond to the message,
	* Adobe stops sending them.  The fix is to detect this case
	* and sleep.
	* 
	* NOTE: Under normal circumstances, Windows will never send
	* consecutive WM_NCHITTEST messages to the same control without
	* another message (normally WM_SETCURSOR) in between.
	*/
	if ((int)/*64*/msg == OS.WM_NCHITTEST) {
		if (hitCount++ >= 1024) {
			try {Thread.sleep (1);} catch (Throwable t) {}
		}
	} else {
		hitCount = 0;
	}
	if (lastControl != null && lastHwnd == hwnd) {
		return lastControl.windowProc (hwnd, (int)/*64*/msg, wParam, lParam);
	}
	int index;
	if (USE_PROPERTY) {
		index = (int)/*64*/OS.GetProp (hwnd, SWT_OBJECT_INDEX) - 1;
	} else {
		index = (int)/*64*/OS.GetWindowLongPtr (hwnd, OS.GWLP_USERDATA) - 1;
	}
	if (0 <= index && index < controlTable.length) {
		Control control = controlTable [index];
		if (control != null) {
			lastHwnd = hwnd;
			lastControl = control;
			return control.windowProc (hwnd, (int)/*64*/msg, wParam, lParam);
		}
	}
	return OS.DefWindowProc (hwnd, (int)/*64*/msg, wParam, lParam);
}

static String withCrLf (String string) {

	/* If the string is empty, return the string. */
	int length = string.length ();
	if (length == 0) return string;
	
	/*
	* Check for an LF or CR/LF and assume the rest of
	* the string is formated that way.  This will not
	* work if the string contains mixed delimiters.
	*/
	int i = string.indexOf ('\n', 0);
	if (i == -1) return string;
	if (i > 0 && string.charAt (i - 1) == '\r') {
		return string;
	}

	/*
	* The string is formatted with LF.  Compute the
	* number of lines and the size of the buffer
	* needed to hold the result
	*/
	i++;	
	int count = 1;
	while (i < length) {
		if ((i = string.indexOf ('\n', i)) == -1) break;
		count++; i++;
	}
	count += length;

	/* Create a new string with the CR/LF line terminator. */
	i = 0;
	StringBuffer result = new StringBuffer (count);
	while (i < length) {
		int j = string.indexOf ('\n', i);
		if (j == -1) j = length;
		result.append (string.substring (i, j));
		if ((i = j) < length) {
			result.append ("\r\n"); //$NON-NLS-1$
			i++;
		}
	}
	return result.toString ();
}

static char [] withCrLf (char [] string) {
	/* If the string is empty, return the string. */
	int length = string.length;
	if (length == 0) return string;
	
	/*
	* Check for an LF or CR/LF and assume the rest of
	* the string is formated that way.  This will not
	* work if the string contains mixed delimiters.
	* Also, compute the number of lines.
	*/
	int count = 0;
	for (int i = 0; i < string.length; i++) {
		if (string [i] == '\n') {
			count++;
			if (count == 1 && i > 0 && string [i - 1] == '\r') return string;
		}
	}
	if (count == 0) return string;

	/*
	* The string is formatted with LF.  
	*/
	count += length;

	/* Create a new string with the CR/LF line terminator. */
	char [] result = new char [count];
	for (int i = 0, j = 0; i < length && j < count; i++) {
		if (string [i] == '\n') {
			result [j++] = '\r';
		}
		result [j++] = string [i];
	}
	
	return result;
}

}

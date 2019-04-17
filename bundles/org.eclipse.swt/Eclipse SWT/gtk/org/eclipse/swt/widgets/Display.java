/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.regex.Pattern;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.GDBus.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;

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
 * <p>
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

	static boolean strictChecks = System.getProperty("org.eclipse.swt.internal.gtk.enableStrictChecks") != null;

	private static final int SLOT_IN_USE = -2;
	private static final int LAST_TABLE_INDEX = -1;

	/* Events Dispatching and Callback */
	int gdkEventCount;
	long [] gdkEvents;
	Widget [] gdkEventWidgets;
	int [] dispatchEvents;
	Event [] eventQueue;
	long fds;
	int allocated_nfds;
	boolean wake;
	int [] max_priority = new int [1], timeout = new int [1];
	Callback eventCallback;
	long eventProc, windowProc2, windowProc3, windowProc4, windowProc5, windowProc6;
	long snapshotDrawProc;
	long keyPressReleaseProc, focusProc, enterMotionScrollProc, leaveProc;
	long gesturePressReleaseProc;
	long notifyStateProc;
	Callback windowCallback2, windowCallback3, windowCallback4, windowCallback5, windowCallback6;
	Callback snapshotDraw;
	Callback keyPressReleaseCallback, focusCallback, enterMotionScrollCallback, leaveCallback;
	Callback gesturePressReleaseCallback;
	Callback notifyStateCallback;
	EventTable eventTable, filterTable;
	static String APP_NAME = "SWT"; //$NON-NLS-1$
	static String APP_VERSION = ""; //$NON-NLS-1$
	static final String DISPATCH_EVENT_KEY = "org.eclipse.swt.internal.gtk.dispatchEvent"; //$NON-NLS-1$
	static final String ADD_WIDGET_KEY = "org.eclipse.swt.internal.addWidget"; //$NON-NLS-1$
	long [] closures, closuresProc;
	int [] closuresCount;
	int [] signalIds;
	long shellMapProcClosure;

	/* Widget Table */
	int [] indexTable;
	int freeSlot;
	long lastHandle;
	Widget lastWidget;
	Widget [] widgetTable;
	final static int GROW_SIZE = 1024;
	static final int SWT_OBJECT_INDEX;
	static final int SWT_OBJECT_INDEX1;
	static final int SWT_OBJECT_INDEX2;
	static {
		byte [] buffer = Converter.wcsToMbcs ("SWT_OBJECT_INDEX", true); //$NON-NLS-1$
		SWT_OBJECT_INDEX = OS.g_quark_from_string (buffer);
		buffer = Converter.wcsToMbcs ("SWT_OBJECT_INDEX1", true); //$NON-NLS-1$
		SWT_OBJECT_INDEX1 = OS.g_quark_from_string (buffer);
		buffer = Converter.wcsToMbcs ("SWT_OBJECT_INDEX2", true); //$NON-NLS-1$
		SWT_OBJECT_INDEX2 = OS.g_quark_from_string (buffer);
	}

	/* Modality */
	Shell [] modalShells;
	Dialog modalDialog;
	static final String GET_MODAL_DIALOG = "org.eclipse.swt.internal.gtk.getModalDialog"; //$NON-NLS-1$
	static final String SET_MODAL_DIALOG = "org.eclipse.swt.internal.gtk.setModalDialog"; //$NON-NLS-1$

	/* Focus */
	int focusEvent;
	Control focusControl;
	Shell activeShell;
	boolean activePending;
	boolean ignoreActivate, ignoreFocus;

	Tracker tracker;

	/* Input method resources */
	Control imControl;
	long preeditWindow, preeditLabel;

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Consumer<RuntimeException> runtimeExceptionHandler = DefaultExceptionHandler.RUNTIME_EXCEPTION_HANDLER;
	Consumer<Error> errorHandler = DefaultExceptionHandler.RUNTIME_ERROR_HANDLER;
	Thread thread;

	/* Display Shutdown */
	private class SessionManagerListener implements SessionManagerDBus.IListener {
		Display parent;

		public SessionManagerListener(Display parent) {
			this.parent = parent;
		}

		public boolean isReadyToExit() {
			Event event = new Event ();
			parent.sendEvent(SWT.Close, event);
			return event.doit;
		}

		public void stop() {
			parent.dispose();
		}
	}

	SessionManagerDBus sessionManagerDBus;
	SessionManagerListener sessionManagerListener;
	Runnable [] disposeList;

	/* Deferred Layout list */
	Composite[] layoutDeferred;
	int layoutDeferredCount;

	/* System Tray */
	Tray tray;
	TrayItem currentTrayItem;

	/* Timers */
	int [] timerIds;
	Runnable [] timerList;
	Callback timerCallback;
	long timerProc;
	Callback windowTimerCallback;
	long windowTimerProc;

	/* Caret */
	Caret currentCaret;
	Callback caretCallback;
	int caretId;
	long caretProc;

	/* Mnemonics */
	Control mnemonicControl;

	/* Mouse hover */
	int mouseHoverId;
	long mouseHoverHandle, mouseHoverProc;
	Callback mouseHoverCallback;

	/* Tooltip size allocate callback */
	long sizeAllocateProc;
	Callback sizeAllocateCallback;
	long sizeRequestProc;
	Callback sizeRequestCallback;

	/* Shell map callback */
	long shellMapProc;
	Callback shellMapCallback;

	/* Idle proc callback */
	long idleProc;
	int idleHandle;
	Callback idleCallback;
	static final String ADD_IDLE_PROC_KEY = "org.eclipse.swt.internal.gtk.addIdleProc"; //$NON-NLS-1$
	static final String REMOVE_IDLE_PROC_KEY = "org.eclipse.swt.internal.gtk.removeIdleProc"; //$NON-NLS-1$
	Object idleLock = new Object();
	boolean idleNeeded;

	/* GtkTreeView callbacks */
	long cellDataProc;
	Callback cellDataCallback;


	/* Set direction callback */
	long setDirectionProc;
	Callback setDirectionCallback;
	static final String GET_DIRECTION_PROC_KEY = "org.eclipse.swt.internal.gtk.getDirectionProc"; //$NON-NLS-1$

	/* Set emissionProc callback */
	long emissionProc;
	Callback emissionProcCallback;
	static final String GET_EMISSION_PROC_KEY = "org.eclipse.swt.internal.gtk.getEmissionProc"; //$NON-NLS-1$

	/* Get all children callback */
	long allChildrenProc, allChildren;
	Callback allChildrenCallback;

	/* Settings callbacks */
	long signalProc;
	Callback signalCallback;
	long shellHandle;
	boolean settingsChanged, runSettings;
	static final int STYLE_UPDATED = 1;
	static final int PROPERTY_NOTIFY = 2;

	/* Entry focus behaviour */
	boolean entrySelectOnFocus;

	/* Enter/Exit events */
	Control currentControl;

	/* Flush exposes */
	long checkIfEventProc;
	Callback checkIfEventCallback;
	long flushWindow;
	boolean flushAll;
	GdkRectangle flushRect = new GdkRectangle ();
	XExposeEvent exposeEvent = new XExposeEvent ();
	long [] flushData = new long [1];

	/* System Resources */
	Image errorImage, infoImage, questionImage, warningImage;
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];
	Resource [] resources;
	static final int RESOURCE_SIZE = 1 + 4 + SWT.CURSOR_HAND + 1;

	/* Colors, GTK3 */
	GdkRGBA COLOR_WIDGET_DARK_SHADOW_RGBA, COLOR_WIDGET_NORMAL_SHADOW_RGBA, COLOR_WIDGET_LIGHT_SHADOW_RGBA;
	GdkRGBA COLOR_WIDGET_HIGHLIGHT_SHADOW_RGBA, COLOR_WIDGET_BACKGROUND_RGBA, COLOR_WIDGET_FOREGROUND_RGBA, COLOR_WIDGET_BORDER_RGBA;
	GdkRGBA COLOR_LIST_FOREGROUND_RGBA, COLOR_LIST_BACKGROUND_RGBA, COLOR_LIST_SELECTION_RGBA, COLOR_LIST_SELECTION_TEXT_RGBA;
	GdkRGBA COLOR_LIST_SELECTION_INACTIVE_RGBA, COLOR_LIST_SELECTION_TEXT_INACTIVE_RGBA;
	GdkRGBA COLOR_INFO_BACKGROUND_RGBA, COLOR_INFO_FOREGROUND_RGBA, COLOR_LINK_FOREGROUND_RGBA;
	GdkRGBA COLOR_TITLE_FOREGROUND_RGBA, COLOR_TITLE_BACKGROUND_RGBA, COLOR_TITLE_BACKGROUND_GRADIENT_RGBA;
	GdkRGBA COLOR_TITLE_INACTIVE_FOREGROUND_RGBA, COLOR_TITLE_INACTIVE_BACKGROUND_RGBA, COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT_RGBA;

	/* Initialize color list */
	ArrayList<String> colorList;

	/* Placeholder color ints since SWT system colors is missing them */
	final int SWT_COLOR_LIST_SELECTION_TEXT_INACTIVE = 38;
	final int SWT_COLOR_LIST_SELECTION_INACTIVE = 39;

	/* Popup Menus */
	Menu [] popups;

	/* Click count*/
	int clickCount = 1;

	/* Entry inner border */
	static final int INNER_BORDER = 2;

	/* Timestamp of the Last Received Events */
	int lastEventTime, lastUserEventTime;

	/* Pango layout constructor */
	long pangoLayoutNewProc;
	long pangoFontFamilyNewProc;
	long pangoFontFaceNewProc;

	/* IM Context constructor */
	long imContextNewProc;

	/* GtkPrinterOptionWidget constructor */
	long printerOptionWidgetNewProc;

	/* Custom Resize */
	double resizeLocationX, resizeLocationY;
	int resizeBoundsX, resizeBoundsY, resizeBoundsWidth, resizeBoundsHeight;
	int resizeMode;

	/* Fixed Subclass */
	static long fixed_type;

	/* Renderer Subclass */
	static long text_renderer_type, pixbuf_renderer_type, toggle_renderer_type;
	static long text_renderer_info_ptr, pixbuf_renderer_info_ptr, toggle_renderer_info_ptr;
	static Callback rendererClassInitCallback, rendererRenderCallback, rendererSnapshotCallback;
	static Callback rendererGetPreferredWidthCallback;
	static long rendererClassInitProc, rendererRenderProc, rendererSnapshotProc;
	static long rendererGetPreferredWidthProc;

	/* Key Mappings */
	static final int [] [] KeyTable = {

		/* Keyboard and Mouse Masks */
		{GDK.GDK_Alt_L,		SWT.ALT},
		{GDK.GDK_Alt_R,		SWT.ALT},
		{GDK.GDK_Meta_L,	SWT.ALT},
		{GDK.GDK_Meta_R,	SWT.ALT},
		{GDK.GDK_Shift_L,		SWT.SHIFT},
		{GDK.GDK_Shift_R,		SWT.SHIFT},
		{GDK.GDK_Control_L,	SWT.CONTROL},
		{GDK.GDK_Control_R,	SWT.CONTROL},
		{GDK.GDK_ISO_Level3_Shift, SWT.ALT_GR},
//		{OS.GDK_????,		SWT.COMMAND},
//		{OS.GDK_????,		SWT.COMMAND},

		/* Non-Numeric Keypad Keys */
		{GDK.GDK_Up,						SWT.ARROW_UP},
		{GDK.GDK_KP_Up,					SWT.ARROW_UP},
		{GDK.GDK_Down,					SWT.ARROW_DOWN},
		{GDK.GDK_KP_Down,			SWT.ARROW_DOWN},
		{GDK.GDK_Left,						SWT.ARROW_LEFT},
		{GDK.GDK_KP_Left,				SWT.ARROW_LEFT},
		{GDK.GDK_Right,					SWT.ARROW_RIGHT},
		{GDK.GDK_KP_Right,				SWT.ARROW_RIGHT},
		{GDK.GDK_Page_Up,				SWT.PAGE_UP},
		{GDK.GDK_KP_Page_Up,		SWT.PAGE_UP},
		{GDK.GDK_Page_Down,			SWT.PAGE_DOWN},
		{GDK.GDK_KP_Page_Down,	SWT.PAGE_DOWN},
		{GDK.GDK_Home,					SWT.HOME},
		{GDK.GDK_KP_Home,			SWT.HOME},
		{GDK.GDK_End,						SWT.END},
		{GDK.GDK_KP_End,				SWT.END},
		{GDK.GDK_Insert,					SWT.INSERT},
		{GDK.GDK_KP_Insert,			SWT.INSERT},

		/* Virtual and Ascii Keys */
		{GDK.GDK_BackSpace,		SWT.BS},
		{GDK.GDK_Return,				SWT.CR},
		{GDK.GDK_Delete,				SWT.DEL},
		{GDK.GDK_KP_Delete,		SWT.DEL},
		{GDK.GDK_Escape,			SWT.ESC},
		{GDK.GDK_Linefeed,			SWT.LF},
		{GDK.GDK_Tab,					SWT.TAB},
		{GDK.GDK_ISO_Left_Tab, 	SWT.TAB},

		/* Functions Keys */
		{GDK.GDK_F1,		SWT.F1},
		{GDK.GDK_F2,		SWT.F2},
		{GDK.GDK_F3,		SWT.F3},
		{GDK.GDK_F4,		SWT.F4},
		{GDK.GDK_F5,		SWT.F5},
		{GDK.GDK_F6,		SWT.F6},
		{GDK.GDK_F7,		SWT.F7},
		{GDK.GDK_F8,		SWT.F8},
		{GDK.GDK_F9,		SWT.F9},
		{GDK.GDK_F10,		SWT.F10},
		{GDK.GDK_F11,		SWT.F11},
		{GDK.GDK_F12,		SWT.F12},
		{GDK.GDK_F13,		SWT.F13},
		{GDK.GDK_F14,		SWT.F14},
		{GDK.GDK_F15,		SWT.F15},
		{GDK.GDK_F16,		SWT.F16},
		{GDK.GDK_F17,		SWT.F17},
		{GDK.GDK_F18,		SWT.F18},
		{GDK.GDK_F19,		SWT.F19},
		{GDK.GDK_F20,		SWT.F20},

		/* Numeric Keypad Keys */
		{GDK.GDK_KP_Multiply,		SWT.KEYPAD_MULTIPLY},
		{GDK.GDK_KP_Add,			SWT.KEYPAD_ADD},
		{GDK.GDK_KP_Enter,			SWT.KEYPAD_CR},
		{GDK.GDK_KP_Subtract,	SWT.KEYPAD_SUBTRACT},
		{GDK.GDK_KP_Decimal,	SWT.KEYPAD_DECIMAL},
		{GDK.GDK_KP_Divide,		SWT.KEYPAD_DIVIDE},
		{GDK.GDK_KP_0,			SWT.KEYPAD_0},
		{GDK.GDK_KP_1,			SWT.KEYPAD_1},
		{GDK.GDK_KP_2,			SWT.KEYPAD_2},
		{GDK.GDK_KP_3,			SWT.KEYPAD_3},
		{GDK.GDK_KP_4,			SWT.KEYPAD_4},
		{GDK.GDK_KP_5,			SWT.KEYPAD_5},
		{GDK.GDK_KP_6,			SWT.KEYPAD_6},
		{GDK.GDK_KP_7,			SWT.KEYPAD_7},
		{GDK.GDK_KP_8,			SWT.KEYPAD_8},
		{GDK.GDK_KP_9,			SWT.KEYPAD_9},
		{GDK.GDK_KP_Equal,	SWT.KEYPAD_EQUAL},

		/* Other keys */
		{GDK.GDK_Caps_Lock,		SWT.CAPS_LOCK},
		{GDK.GDK_Num_Lock,		SWT.NUM_LOCK},
		{GDK.GDK_Scroll_Lock,		SWT.SCROLL_LOCK},
		{GDK.GDK_Pause,				SWT.PAUSE},
		{GDK.GDK_Break,				SWT.BREAK},
		{GDK.GDK_Print,					SWT.PRINT_SCREEN},
		{GDK.GDK_Help,					SWT.HELP},

	};

	/* Cache pressed modifier state. See Bug 537025. */
	int cachedModifierState = 0;

	/* Latin layout key group */
	private int latinKeyGroup;

	/* Mapping from layout key group to number of Latin alphabet keys. See Bug 533395, Bug 61190. */
	Map<Integer, Integer> groupKeysCount;

	/* Keymap "keys-changed" callback */
	long keysChangedProc;
	Callback keysChangedCallback;

	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Skinning support */
	Widget [] skinList = new Widget [GROW_SIZE];
	int skinCount;

	/* Package name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets."; //$NON-NLS-1$
	/* This code is intentionally commented.
	 * ".class" can not be used on CLDC.
	 */
//	static {
//		String name = Display.class.getName ();
//		int index = name.lastIndexOf ('.');
//		PACKAGE_NAME = name.substring (0, index + 1);
//	}

	/* GTK Version */
	static final int GTK3_MAJOR = 3;
	static final int GTK3_MINOR = 10;
	static final int GTK3_MICRO = 0;

	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;

	/* Initial Guesses for Shell Trimmings. */
	static final int TRIM_NONE = 0;
	static final int TRIM_BORDER = 1;
	static final int TRIM_RESIZE = 2;
	static final int TRIM_TITLE_BORDER = 3;
	static final int TRIM_TITLE_RESIZE = 4;
	static final int TRIM_TITLE = 5;
	int [] trimWidths = new int [6];
	int [] trimHeights = new int [6];
	{
		trimWidths [TRIM_NONE] = 0; trimHeights [TRIM_NONE] = 0;
		trimWidths [TRIM_BORDER] = 4; trimHeights [TRIM_BORDER] = 4;
		trimWidths [TRIM_RESIZE] = 6; trimHeights [TRIM_RESIZE] = 6;
		trimWidths [TRIM_TITLE_BORDER] = 5; trimHeights [TRIM_TITLE_BORDER] = 28;
		trimWidths [TRIM_TITLE_RESIZE] = 6; trimHeights [TRIM_TITLE_RESIZE] = 29;
		trimWidths [TRIM_TITLE] = 0; trimHeights [TRIM_TITLE] = 23;

		String path = System.getProperty ("user.home"); //$NON-NLS-1$
		if (path != null) {
			File file = new File (path, ".swt/trims.prefs");
			if (file.exists () && file.isFile ()) {
				Properties props = new Properties ();
				try (FileInputStream fis = new FileInputStream (file)) {
					props.load (fis);
					String trimWidthsString = props.getProperty ("trimWidths");
					String trimHeightsString = props.getProperty ("trimHeights");
					if (trimWidthsString != null && trimHeightsString != null) {
						StringTokenizer tok = new StringTokenizer (trimWidthsString);
						for (int i = 0; i < trimWidths.length && tok.hasMoreTokens (); i++) {
							trimWidths [i] = Integer.parseInt (tok.nextToken ());
						}
						tok = new StringTokenizer (trimHeightsString);
						for (int i = 0; i < trimHeights.length && tok.hasMoreTokens (); i++) {
							trimHeights [i] = Integer.parseInt (tok.nextToken ());
						}
					}
				} catch (IOException | NumberFormatException e) {
					// use default values
				}
			}
		}
	}

	boolean ignoreTrim;

	/* Window Manager */
	String windowManager;

	/*
	* TEMPORARY CODE.  Install the runnable that
	* gets the current display. This code will
	* be removed in the future.
	*/
	static {
		DeviceFinder = () -> {
			Device device = getCurrent ();
			if (device == null) {
				device = getDefault ();
			}
			setDevice (device);
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

void addGdkEvent (long event) {
	if (gdkEvents == null) {
		int length = GROW_SIZE;
		gdkEvents = new long [length];
		gdkEventWidgets = new Widget [length];
		gdkEventCount = 0;
	}
	if (gdkEventCount == gdkEvents.length) {
		int length = gdkEventCount + GROW_SIZE;
		long [] newEvents = new long [length];
		System.arraycopy (gdkEvents, 0, newEvents, 0, gdkEventCount);
		gdkEvents = newEvents;
		Widget [] newWidgets = new Widget [length];
		System.arraycopy (gdkEventWidgets, 0, newWidgets, 0, gdkEventCount);
		gdkEventWidgets = newWidgets;
	}
	Widget widget = null;
	long handle = GTK.gtk_get_event_widget (event);
	if (handle != 0) {
		do {
			widget = getWidget (handle);
		} while (widget == null && (handle = GTK.gtk_widget_get_parent (handle)) != 0);
	}
	gdkEvents [gdkEventCount] = event;
	gdkEventWidgets [gdkEventCount] = widget;
	gdkEventCount++;
}

void addIdleProc() {
	synchronized (idleLock){
		this.idleNeeded = true;
		if (idleHandle == 0) {
			idleHandle = OS.g_idle_add (idleProc, 0);
		}
	}
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
	if (eventType == SWT.OpenDocument || eventType == SWT.OpenUrl) {
		gdbus_init_methods();
	}
	eventTable.hook (eventType, listener);
}

/**
 * Handle gdbus on 'org.eclipse.swt' DBus session.
 * E.g equinox launcher passes files/urls to SWT via gdbus. "./eclipse myFile" or "./eclipse http://www.google.com"
 *
 * Only one SWT instance can hold the unique and well-known name at one time, so we have to be mindful
 * of the case where an SWT app could steal the well-known name and make the equinox launcher confused.
 *
 * For equinox launcher, See eclipseGtk.c:gtkPlatformJavaSystemProperties
 */
private void gdbus_init_methods() {
	GDBusMethod[] methods = {
		new GDBusMethod(
			// FileOpen call can be reached via:
			// gdbus call --session --dest org.eclipse.swt --object-path /org/eclipse/swt --method org.eclipse.swt.FileOpen "['/tmp/hi','http://www.eclipse.org']"
			// See Bug525305_Browser_OpenUrl.java test snippet for testing/verification.
			// In a child eclipse, this will open the files in a new editor.
			// This is reached by equinox launcher from eclipseGtk.c. Look for "g_dbus_proxy_call_sync"
			"FileOpen",
			new String [][] {{OS.DBUS_TYPE_STRING_ARRAY,"A String array containing file paths or URLs for OpenDocument/OpenUrl signal"}},
			new String [0][0],
			(args) -> {
				String[] fileNames = (String[]) args[0];
				for (int i = 0; i < fileNames.length; i++) {
					Event event = new Event ();
					event.text = fileNames[i];
					try {
						if (new URI (fileNames[i]).getScheme() != null) { // For specs, see: https://docs.oracle.com/javase/8/docs/api/java/net/URI.html
							// E.g: eclipse http://www.google.com
							sendEvent (SWT.OpenUrl, event);
						} else {
							throw new URISyntaxException(fileNames[i], "Not a valid Url. Probably file.");
						}
					} catch (URISyntaxException e) {
						// E.g eclipse /tmp/myfile (absolute)   or  eclipse myfile  (relative)
						sendEvent (SWT.OpenDocument, event);
					}
				}
				return null;
			})
		};
	GDBus.init(methods);
}

long allChildrenProc (long widget, long recurse) {
	allChildren = OS.g_list_append (allChildren, widget);
	if (recurse != 0 && GTK.GTK_IS_CONTAINER (widget)) {
		GTK.gtk_container_forall (widget, allChildrenProc, recurse);
	}
	return 0;
}

void addMouseHoverTimeout (long handle) {
	if (mouseHoverId != 0) OS.g_source_remove (mouseHoverId);
	mouseHoverId = OS.g_timeout_add (400, mouseHoverProc, handle);
	mouseHoverHandle = handle;
}

void addPopup (Menu menu) {
	if (popups == null) popups = new Menu [4];
	int length = popups.length;
	for (int i=0; i<length; i++) {
		if (popups [i] == menu) return;
		/* Bug 530577, 528998: Clicking on button that triggers popup menus causes
		 * menu.setVisible to be called twice, once due to MouseDown, and once due to ButtonClicked.
		 * This causes two duplicate menus (with different handles) to popup at the same time.
		 * The fix is to avoid having two identical popup menus in the queue.
		 */
		if (popups[i] != null && popups[i].getNameText().equals(menu.getNameText())) return;
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

void addWidget (long handle, Widget widget) {
	if (handle == 0) return;
	// Last element in the indexTable is -1, so if freeSlot == -1 we have no place anymore
	if (freeSlot == LAST_TABLE_INDEX) {
		int length = (freeSlot = indexTable.length) + GROW_SIZE;
		int[] newIndexTable = new int[length];
		Widget[] newWidgetTable = new Widget [length];
		System.arraycopy (indexTable, 0, newIndexTable, 0, freeSlot);
		System.arraycopy (widgetTable, 0, newWidgetTable, 0, freeSlot);
		for (int i = freeSlot; i < length - 1; i++) {
			newIndexTable[i] = i + 1;
		}
		// mark last slot as "need resize"
		newIndexTable[length - 1] = LAST_TABLE_INDEX;
		indexTable = newIndexTable;
		widgetTable = newWidgetTable;
	}
	int index = freeSlot + 1;
	if(strictChecks) {
		long data = OS.g_object_get_qdata (handle, SWT_OBJECT_INDEX);
		if(data > 0 && data != index) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, ". Potential leak of " + widget + debugInfoForIndex(data - 1));
		}
	}
	OS.g_object_set_qdata (handle, SWT_OBJECT_INDEX, index);
	int oldSlot = freeSlot;
	freeSlot = indexTable[oldSlot];
	// Mark old index slot as used
	indexTable [oldSlot] = SLOT_IN_USE;
	if(widgetTable [oldSlot] != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, ". Trying to override non empty slot with " + widget + debugInfoForIndex(oldSlot));
	}
	widgetTable [oldSlot] = widget;
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
		synchronized (idleLock) {
			if (idleNeeded && idleHandle == 0) {
	 			//NOTE: calling unlocked function in OS
				idleHandle = OS._g_idle_add (idleProc, 0);
			}
		}
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	GDK.gdk_display_beep(GDK.gdk_display_get_default());
}

long cellDataProc (long tree_column, long cell, long tree_model, long iter, long data) {
	Widget widget = getWidget (data);
	if (widget == null) return 0;
	return widget.cellDataProc (tree_column, cell, tree_model, iter, data);
}


@Override
protected void checkDevice () {
	if (thread == null) error (SWT.ERROR_WIDGET_DISPOSED);
	if (thread != Thread.currentThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
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

long checkIfEventProc (long display, long xEvent, long userData) {
	int type = OS.X_EVENT_TYPE (xEvent);
	switch (type) {
		case OS.Expose:
		case OS.GraphicsExpose:
			break;
		default:
			return 0;
	}
	long window = GDK.gdk_x11_window_lookup_for_display(GDK.gdk_display_get_default(), OS.X_EVENT_WINDOW (xEvent));
	if (window == 0) return 0;
	if (flushWindow != 0) {
		if (flushAll) {
			long tempWindow = window;
			do {
				if (tempWindow == flushWindow) break;
			} while ((tempWindow = GDK.gdk_window_get_parent (tempWindow)) != 0);
			if (tempWindow != flushWindow) return 0;
		} else {
			if (window != flushWindow) return 0;
		}
	}
	OS.memmove (exposeEvent, xEvent, XExposeEvent.sizeof);
	switch (type) {
		case OS.Expose:
		case OS.GraphicsExpose: {
			flushRect.x = exposeEvent.x;
			flushRect.y = exposeEvent.y;
			flushRect.width = Math.max (0, exposeEvent.width);
			flushRect.height = Math.max (0, exposeEvent.height);
			GDK.gdk_window_invalidate_rect (window, flushRect, true);
			exposeEvent.type = -1;
			OS.memmove (xEvent, exposeEvent, XExposeEvent.sizeof);
			break;
		}
	}
	return 0;
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
@Override
protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay(thread = Thread.currentThread (), false);
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}

/**
 * Check if the XIM module is present and generates a warning for potential graphical issues
 * if GTK_IM_MODULE=xim is detected. See Bug 517671.
 */
void checkXimModule () {
	Map<String, String> env = System.getenv();
	String module = env.get("GTK_IM_MODULE");
	if (module != null && module.equals("xim")) {
		System.err.println("***WARNING: Detected: GTK_IM_MODULE=xim. This input method is unsupported and can cause graphical issues.");
		System.err.println("***WARNING: Unset GTK_IM_MODULE or set GTK_IM_MODULE=ibus if flicking is experienced. ");
	}
}

void createDisplay (DeviceData data) {
	boolean init = GTK.GTK4 ? GTK.gtk_init_check () : GTK.gtk_init_check (new long [] {0}, null);
	if (!init) SWT.error (SWT.ERROR_NO_HANDLES, null, " [gtk_init_check() failed]"); //$NON-NLS-1$
	checkXimModule();
	//set GTK+ Theme name as property for introspection purposes
	if (OS.GTK_THEME_SET) {
		String themeName = OS.GTK_THEME_NAME + (OS.GTK_THEME_DARK ? ":dark" : "");
		System.setProperty("org.eclipse.swt.internal.gtk.theme", themeName);
	} else {
		System.setProperty("org.eclipse.swt.internal.gtk.theme", OS.getThemeName());
	}
	if (OS.isX11()) {
		xDisplay = GTK.GTK4 ? 0 : GDK.gdk_x11_get_default_xdisplay();
	}
	if (OS.SWT_DEBUG) Device.DEBUG = true;
	long ptr = GTK.gtk_check_version (GTK3_MAJOR, GTK3_MINOR, GTK3_MICRO);
	if (ptr != 0) {
		int length = C.strlen (ptr);
		byte [] buffer = new byte [length];
		C.memmove (buffer, ptr, length);
		System.out.println ("***WARNING: " + new String (Converter.mbcsToWcs (buffer))); //$NON-NLS-1$
		System.out.println ("***WARNING: SWT requires GTK " + GTK3_MAJOR+ "." + GTK3_MINOR + "." + GTK3_MICRO); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		int major = GTK.gtk_get_major_version(), minor = GTK.gtk_get_minor_version (), micro = GTK.gtk_get_micro_version ();
		System.out.println ("***WARNING: Detected: " + major + "." + minor + "." + micro); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	fixed_type = OS.swt_fixed_get_type();
	if (rendererClassInitProc == 0) {
		rendererClassInitCallback = new Callback (getClass (), "rendererClassInitProc", 2); //$NON-NLS-1$
		rendererClassInitProc = rendererClassInitCallback.getAddress ();
		if (rendererClassInitProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	}
	if (GTK.GTK4) {
		if (rendererSnapshotProc == 0) {
			rendererSnapshotCallback = new Callback (getClass (), "rendererSnapshotProc", 6); //$NON-NLS-1$
			rendererSnapshotProc = rendererSnapshotCallback.getAddress ();
			if (rendererSnapshotProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
	} else {
		if (rendererRenderProc == 0) {
			rendererRenderCallback = new Callback (getClass (), "rendererRenderProc", 6); //$NON-NLS-1$
			rendererRenderProc = rendererRenderCallback.getAddress ();
			if (rendererRenderProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
	}
	if (rendererGetPreferredWidthProc == 0) {
		rendererGetPreferredWidthCallback = new Callback (getClass (), "rendererGetPreferredWidthProc", 4); //$NON-NLS-1$
		rendererGetPreferredWidthProc = rendererGetPreferredWidthCallback.getAddress ();
		if (rendererGetPreferredWidthProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	}
	if (text_renderer_type == 0) {
		GTypeInfo renderer_info = new GTypeInfo ();
		renderer_info.class_size = (short) GTK.GtkCellRendererTextClass_sizeof ();
		renderer_info.class_init = rendererClassInitProc;
		renderer_info.instance_size = (short) GTK.GtkCellRendererText_sizeof ();
		text_renderer_info_ptr = OS.g_malloc (GTypeInfo.sizeof);
		OS.memmove (text_renderer_info_ptr, renderer_info, GTypeInfo.sizeof);
		byte [] type_name = Converter.wcsToMbcs ("SwtTextRenderer", true); //$NON-NLS-1$
		text_renderer_type = OS.g_type_register_static (GTK.GTK_TYPE_CELL_RENDERER_TEXT (), type_name, text_renderer_info_ptr, 0);
	}
	if (pixbuf_renderer_type == 0) {
		GTypeInfo renderer_info = new GTypeInfo ();
		renderer_info.class_size = (short) GTK.GtkCellRendererPixbufClass_sizeof ();
		renderer_info.class_init = rendererClassInitProc;
		renderer_info.instance_size = (short) GTK.GtkCellRendererPixbuf_sizeof ();
		pixbuf_renderer_info_ptr = OS.g_malloc (GTypeInfo.sizeof);
		OS.memmove (pixbuf_renderer_info_ptr, renderer_info, GTypeInfo.sizeof);
		byte [] type_name = Converter.wcsToMbcs ("SwtPixbufRenderer", true); //$NON-NLS-1$
		pixbuf_renderer_type = OS.g_type_register_static (GTK.GTK_TYPE_CELL_RENDERER_PIXBUF (), type_name, pixbuf_renderer_info_ptr, 0);
	}
	if (toggle_renderer_type == 0) {
		GTypeInfo renderer_info = new GTypeInfo ();
		renderer_info.class_size = (short) GTK.GtkCellRendererToggleClass_sizeof ();
		renderer_info.class_init = rendererClassInitProc;
		renderer_info.instance_size = (short) GTK.GtkCellRendererToggle_sizeof ();
		toggle_renderer_info_ptr = OS.g_malloc (GTypeInfo.sizeof);
		OS.memmove (toggle_renderer_info_ptr, renderer_info, GTypeInfo.sizeof);
		byte [] type_name = Converter.wcsToMbcs ("SwtToggleRenderer", true); //$NON-NLS-1$
		toggle_renderer_type = OS.g_type_register_static (GTK.GTK_TYPE_CELL_RENDERER_TOGGLE (), type_name, toggle_renderer_info_ptr, 0);
	}

	GTK.gtk_widget_set_default_direction (GTK.GTK_TEXT_DIR_LTR);
	byte [] buffer = Converter.wcsToMbcs (APP_NAME, true);
	OS.g_set_prgname (buffer);
	if (OS.isX11() && !GTK.GTK4) {
		GDK.gdk_set_program_class (buffer);
	}

	/* Initialize the hidden shell */
	shellHandle = GTK.gtk_window_new (GTK.GTK_WINDOW_TOPLEVEL);
	if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
	GTK.gtk_widget_realize (shellHandle);

	/* Initialize the filter and event callback */
	eventCallback = new Callback (this, "eventProc", 2); //$NON-NLS-1$
	eventProc = eventCallback.getAddress ();
	if (eventProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	GDK.gdk_event_handler_set (eventProc, 0, 0);

	signalCallback = new Callback (this, "signalProc", 3); //$NON-NLS-1$
	signalProc = signalCallback.getAddress ();
	if (signalProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	if (!GTK.GTK4) {
		byte[] atomName = Converter.wcsToMbcs ("SWT_Window_" + APP_NAME, true); //$NON-NLS-1$
		long atom = GDK.gdk_atom_intern(atomName, false);
		GDK.gdk_selection_owner_set(GTK.gtk_widget_get_window(shellHandle), atom, OS.CurrentTime, false);
		GDK.gdk_selection_owner_get(atom);

		// No GdkWindow on GTK4
		GTK.gtk_widget_add_events (shellHandle, GDK.GDK_PROPERTY_CHANGE_MASK);
		OS.g_signal_connect (shellHandle, OS.property_notify_event, signalProc, PROPERTY_NOTIFY);
	}

	latinKeyGroup = findLatinKeyGroup ();
	keysChangedCallback = new Callback (this, "keysChangedProc", 2); //$NON-NLS-1$
	keysChangedProc = keysChangedCallback.getAddress ();
	if (keysChangedProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	long keymap;
	long display = GDK.gdk_display_get_default();
	if (GTK.GTK4) {
		keymap = GDK.gdk_display_get_keymap(display);
	} else {
		keymap = GDK.gdk_keymap_get_for_display(display);
	}
	OS.g_signal_connect (keymap, OS.keys_changed, keysChangedProc, 0);
}

/**
 * Determine key group of Latin layout, and update the layout group to key count map.
 * If there are multiple Latin keyboard layout group, return the first one.
 *
 * @return the most Latin keyboard layout group (i.e. group holding the max number of Latin alphabet keys)
 */
private int findLatinKeyGroup () {
	int result = 0;
	groupKeysCount = new HashMap<> ();
	long keymap;
	long display = GDK.gdk_display_get_default();
	if (GTK.GTK4) {
		keymap = GDK.gdk_display_get_keymap(display);
	} else {
		keymap = GDK.gdk_keymap_get_for_display(display);
	}

	// count all key groups for Latin alphabet
	for (int keyval = GDK.GDK_KEY_a; keyval <= GDK.GDK_KEY_z; keyval++) {
		long [] keys = new long [1];
		int [] n_keys = new int [1];

		if (GDK.gdk_keymap_get_entries_for_keyval (keymap, keyval, keys, n_keys)) {
			GdkKeymapKey key_entry = new GdkKeymapKey ();
			for (int key = 0; key < n_keys [0]; key++) {
				OS.memmove (key_entry, keys [0] + key * GdkKeymapKey.sizeof, GdkKeymapKey.sizeof);
				Integer keys_count = (Integer) groupKeysCount.get (key_entry.group);
				if (keys_count != null) {
					keys_count++;
				} else {
					keys_count = 1;
				}
				groupKeysCount.put (key_entry.group, keys_count);
			}
			OS.g_free (keys [0]);
		}
	}

	// group with maximum keys count is Latin
	int max_keys_count = 0;
	for (Map.Entry<Integer, Integer> entry : groupKeysCount.entrySet()) {
		Integer group = entry.getKey ();
		Integer keys_count = entry.getValue ();
		if (keys_count > max_keys_count) {
			result = group;
			max_keys_count = keys_count;
		}
	}

	return result;
}

/**
 * Return the most Latin keyboard layout group.
 */
int getLatinKeyGroup () {
	return latinKeyGroup;
}

/**
 * Return a mapping from layout group to the number of Latin alphabet (a-z) in each group
 */
Map<Integer, Integer> getGroupKeysCount () {
	return groupKeysCount;
}

/**
 * 'keys-changed' event handler.
 * Updates the most Latin keyboard layout group field.
 */
long keysChangedProc (long keymap, long user_data) {
	latinKeyGroup = findLatinKeyGroup ();
	return 0;
}

Image createImage (String name) {
	byte[] buffer = Converter.wcsToMbcs (name, true);
	long pixbuf = GTK.gtk_icon_theme_load_icon(GTK.gtk_icon_theme_get_default(), buffer, 48, GTK.GTK_ICON_LOOKUP_FORCE_SIZE, 0);
	if (pixbuf == 0) return null;
	int width = GDK.gdk_pixbuf_get_width (pixbuf);
	int height = GDK.gdk_pixbuf_get_height (pixbuf);
	int stride = GDK.gdk_pixbuf_get_rowstride (pixbuf);
	boolean hasAlpha = GDK.gdk_pixbuf_get_has_alpha (pixbuf);
	long pixels = GDK.gdk_pixbuf_get_pixels (pixbuf);
	byte [] data = new byte [stride * height];
	C.memmove (data, pixels, data.length);
	OS.g_object_unref (pixbuf);
	ImageData imageData = null;
	if (hasAlpha) {
		PaletteData palette = new PaletteData (0xFF000000, 0xFF0000, 0xFF00);
		imageData = new ImageData (width, height, 32, palette);
		byte [] alpha = new byte [stride * height];
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				alpha [y*width+x] = data [y*stride+x*4+3];
				data [y*stride+x*4+3] = 0;
			}
		}
		imageData.setAlphas (0, 0, width * height, alpha, 0);
	} else {
		PaletteData palette = new PaletteData (0xFF0000, 0xFF00, 0xFF);
		imageData = new ImageData (width, height, 24, palette);
	}
	imageData.data = data;
	imageData.bytesPerLine = stride;
	return new Image (this, imageData);
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
@Override
protected void destroy () {
	if (this == Default) Default = null;
	deregister (this);
	destroyDisplay ();
}

void destroyDisplay () {
}

long emissionProc (long ihint, long n_param_values, long param_values, long data) {
	if (GTK.gtk_widget_get_toplevel (OS.g_value_peek_pointer(param_values)) == data) {
		GTK.gtk_widget_set_direction (OS.g_value_peek_pointer(param_values), GTK.GTK_TEXT_DIR_RTL);
	}
	return 1;
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

long eventProc (long event, long data) {
	/*
	* Use gdk_event_get_time() rather than event.time or
	* gtk_get_current_event_time().  If the event does not
	* have a time stamp, then the field will contain garbage.
	* Note that calling gtk_get_current_event_time() from
	* outside of gtk_main_do_event() seems to always
	* return zero.
	*/
	int time = GDK.gdk_event_get_time (event);
	if (time != 0) lastEventTime = time;

	int eventType = GTK.GTK4 ? GDK.gdk_event_get_event_type(event) : GDK.GDK_EVENT_TYPE (event);
	Control.fixGdkEventTypeValues(eventType);
	switch (eventType) {
		case GDK.GDK_BUTTON_PRESS:
		case GDK.GDK_KEY_PRESS:
			lastUserEventTime = time;
	}
	boolean dispatch = true;
	if (dispatchEvents != null) {
		dispatch = false;
		for (int i = 0; i < dispatchEvents.length; i++) {
			if (eventType == dispatchEvents [i]) {
				dispatch = true;
				break;
			}
		}
	}
	if (!dispatch) {
		addGdkEvent (GDK.gdk_event_copy (event));
		return 0;
	}
	dispatch = true;
	if (tracker != null) {
		dispatch = tracker.processEvent (event);
	}
	if (dispatch) GTK.gtk_main_do_event (event);
	if (dispatchEvents == null) putGdkEvents ();
	return 0;
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
public Widget findWidget (long handle) {
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
public Widget findWidget (long handle, long id) {
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
 * @noreference This method is not intended to be referenced by clients.
 *
 * @since 3.3
 */
public Widget findWidget (Widget widget, long id) {
	checkDevice ();
	return null;
}

static long rendererClassInitProc (long g_class, long class_data) {
	GtkCellRendererClass klass = new GtkCellRendererClass ();
	OS.memmove (klass, g_class);
	if (GTK.GTK4) {
		klass.snapshot = rendererSnapshotProc;
	} else {
		klass.render = rendererRenderProc;
	}
	klass.get_preferred_width = rendererGetPreferredWidthProc;
	OS.memmove (g_class, klass);
	return 0;
}

static long snapshotDrawProc (long handle, long snapshot) {
	Display display = getCurrent ();
	Widget widget = display.getWidget (handle);
	if (widget != null) widget.snapshotToDraw(handle, snapshot);
	long child = GTK.gtk_widget_get_first_child(handle);
	// Propagate the snapshot down the widget tree
	while (child != 0) {
		GTK.gtk_widget_snapshot_child(handle, child, snapshot);
		child = GTK.gtk_widget_get_next_sibling(child);
	}
	return 0;
}

static long rendererGetPreferredWidthProc (long cell, long handle, long minimun_size, long natural_size) {
	Display display = getCurrent ();
	Widget widget = display.getWidget (handle);
	if (widget != null) return widget.rendererGetPreferredWidthProc (cell, handle, minimun_size, natural_size);
	return 0;
}

static long rendererRenderProc (long cell, long cr, long handle, long background_area, long cell_area, long flags) {
	Display display = getCurrent ();
	Widget widget = display.getWidget (handle);
	if (widget != null) return widget.rendererRenderProc (cell, cr, handle, background_area, cell_area, flags);
	return 0;
}

static long rendererSnapshotProc (long cell, long snapshot, long handle, long background_area, long cell_area, long flags) {
	Display display = getCurrent ();
	Widget widget = display.getWidget (handle);
	if (widget != null) return widget.rendererSnapshotProc (cell, snapshot, handle, background_area, cell_area, flags);
	return 0;
}

void flushExposes (long window, boolean all) {
	if (OS.isX11()) {
		this.flushWindow = window;
		this.flushAll = all;
		long xDisplay = GDK.gdk_x11_display_get_xdisplay(GDK.gdk_display_get_default());
		long xEvent = OS.g_malloc (XEvent.sizeof);
		OS.XCheckIfEvent (xDisplay, xEvent, checkIfEventProc, 0);
		OS.g_free (xEvent);
		this.flushWindow = 0;
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
	return activeShell;
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
@Override
public Rectangle getBounds () {
	checkDevice ();
	return DPIUtil.autoScaleDown (getBoundsInPixels ());
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
@Override
public Rectangle getClientArea () {
	if (OS.isX11()) {
		Rectangle workArea = getWorkArea();
		/*
		 * getWorkArea() can return null if the WM isn't running Xorg (i.e. x11 on Wayland).
		 * To workaround these cases, return the workArea only if getWorkArea() succeeds,
		 * otherwise call super.getClientArea(). See bug 33659.
		 */
		if (workArea != null) return workArea;
	}
	return super.getClientArea();
}

Rectangle getBoundsInPixels () {
	checkDevice ();
	int monitorCount;
	Rectangle bounds = new Rectangle(0, 0, 0, 0);
	if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0)) {
		long display = GDK.gdk_display_get_default ();
		monitorCount = GDK.gdk_display_get_n_monitors (display);
		if (monitorCount > 0) {
			GdkRectangle dest = new GdkRectangle ();
			for (int i = 0; i < monitorCount; i++) {
				long monitor = GDK.gdk_display_get_monitor(display, i);
				GDK.gdk_monitor_get_geometry (monitor, dest);
				if (i == 0) {
					bounds.width = dest.width;
					bounds.height = dest.height;
				} else {
					bounds.width += dest.x;
					bounds.height += dest.y;
				}
			}
			return bounds;
		}
	} else {
		long screen = GDK.gdk_screen_get_default();
		monitorCount = GDK.gdk_screen_get_n_monitors(screen);
		if (monitorCount > 0) {
			GdkRectangle dest = new GdkRectangle ();
			for (int i = 0; i < monitorCount; i++) {
				GDK.gdk_screen_get_monitor_geometry (screen, i, dest);
				if (i == 0) {
					bounds.width = dest.width;
					bounds.height = dest.height;
				} else {
					bounds.width += dest.x;
					bounds.height += dest.y;
				}
			}
			return bounds;
		}
	}
	if (GTK.GTK4) {
		return new Rectangle (0, 0, 0, 0);
	} else {
		return new Rectangle (0, 0, GDK.gdk_screen_width (), GDK.gdk_screen_height ());
	}
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
	long settings = GTK.gtk_settings_get_default ();
	if (settings == 0) return 500;
	int [] buffer = new int [1];
	OS.g_object_get (settings, GTK.gtk_cursor_blink, buffer, 0);
	if (buffer [0] == 0) return 0;
	OS.g_object_get (settings, GTK.gtk_cursor_blink_time, buffer, 0);
	if (buffer [0] == 0) return 500;
	/*
	* By experimentation, GTK application don't use the whole
	* blink cycle time.  Instead, they divide up the time, using
	* an effective blink rate of about 1/2 the total time.
	*/
	return buffer [0] / 2;
}

long getClosure (int id) {
	if (OS.GLIB_VERSION >= OS.VERSION(2, 36, 0) && ++closuresCount [id] >= 255) {
		if (closures [id] != 0) OS.g_closure_unref (closures [id]);
		closures [id] = OS.g_cclosure_new (closuresProc [id], id, 0);
		OS.g_closure_ref (closures [id]);
		OS.g_closure_sink (closures [id]);
		closuresCount [id] = 0;
	}
	return closures [id];
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
	checkDevice();
	int[] x = new int[1], y = new int[1];
	long handle = 0;
	long [] user_data = new long [1];
	long gdkResource;
	if (GTK.GTK4) {
		gdkResource = gdk_device_get_surface_at_position (x,y);
	} else {
		gdkResource = gdk_device_get_window_at_position (x,y);
	}
	if (gdkResource != 0) {
		if (GTK.GTK4) {
			GDK.gdk_surface_get_user_data (gdkResource, user_data);
		} else {
			GDK.gdk_window_get_user_data (gdkResource, user_data);
		}
		handle = user_data [0];
	} else {
		/*
		* Feature in GTK. gdk_window_at_pointer() will not return a window
		* if the pointer is over a foreign embedded window. The fix is to use
		* XQueryPointer to find the containing GDK window.
		*/
		if (!OS.isX11()) return null;
		long gdkDisplay = GDK.gdk_display_get_default();
		if (OS.isX11()) {
			GDK.gdk_x11_display_error_trap_push(gdkDisplay);
		}
		int[] unusedInt = new int[1];
		long [] unusedPtr = new long [1], buffer = new long [1];
		long xWindow, xParent = OS.XDefaultRootWindow (xDisplay);
		do {
			if (OS.XQueryPointer (xDisplay, xParent, unusedPtr, buffer, unusedInt, unusedInt, unusedInt, unusedInt, unusedInt) == 0) {
				handle = 0;
				break;
			}
			if ((xWindow = buffer [0]) != 0) {
				xParent = xWindow;
				long gdkWindow = GDK.gdk_x11_window_lookup_for_display(gdkDisplay, xWindow);
				if (gdkWindow != 0)	{
					GDK.gdk_window_get_user_data (gdkWindow, user_data);
					if (user_data[0] != 0) handle = user_data[0];
				}
			}
		} while (xWindow != 0);
		if (OS.isX11()) {
			GDK.gdk_x11_display_error_trap_pop_ignored(gdkDisplay);
		}
	}
	if (handle == 0) return null;
	do {
		Widget widget = getWidget (handle);
		if (widget != null && widget instanceof Control) {
			Control control = (Control) widget;
			if (control.isEnabled ()) return control;
		}
	} while ((handle = GTK.gtk_widget_get_parent (handle)) != 0);
	return null;
}

boolean filterEvent (Event event) {
	if (filterTable != null) {
		int type = event.type;
		sendPreEvent (type);
		try {
			filterTable.sendEvent (event);
		} finally {
			sendPostEvent (type);
		}
	}
	return false;
}

boolean filters (int eventType) {
	if (filterTable == null) return false;
	return filterTable.hooks (eventType);
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
	return DPIUtil.autoScaleDown(getCursorLocationInPixels());
}

Point getCursorLocationInPixels () {
	checkDevice ();
	int [] x = new int [1], y = new int [1];
	if (GTK.GTK4) {
		/*
		 * TODO: calling gdk_window_get_device_position() with a 0
		 * for the GdkWindow uses gdk_get_default_root_window(),
		 * which doesn't exist on GTK4.
		 */
	} else {
		gdk_window_get_device_position (0, x, y, null);
	}
	/*
	 * Wayland feature: There is no global x/y coordinates in Wayland for security measures, so they
	 * all return relative coordinates dependant to the root window. If there is a popup window (type SWT.ON_TOP),
	 * the return position is relative to the new popup window and not relative to the parent if its
	 * active. Using that as an offset and adding all parent shell relative coordinates will give the
	 * user the correct mouse position in Wayland. This only supports popups that are type
	 * SWT.ON_TOP as any other type of window is not tied to the parent window through
	 * a subsurface. There is currently no support for global coordinates
	 * in Wayland. See Bug 514483.
	 */
	if (!OS.isX11() && activeShell != null) {
		Shell tempShell = activeShell;
		int [] offsetX = new int [1], offsetY = new int [1];
		while (tempShell.getParent() != null) {
			GTK.gtk_window_get_position(tempShell.shellHandle, offsetX, offsetY);
			x[0]+= offsetX[0];
			y[0]+= offsetY[0];
			tempShell = tempShell.getParent().getShell();
		}
	}
	return new Point (x [0], y [0]);
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
	if (key.equals (DISPATCH_EVENT_KEY)) {
		return dispatchEvents;
	}
	if (key.equals (GET_MODAL_DIALOG)) {
		return modalDialog;
	}
	if (key.equals (GET_DIRECTION_PROC_KEY)) {
		return new LONG (setDirectionProc);
	}
	if (key.equals (GET_EMISSION_PROC_KEY)) {
		return new LONG (emissionProc);
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

long gtk_fixed_get_type () {
	return fixed_type;
}

long gtk_cell_renderer_text_get_type () {
	return text_renderer_type;
}

long gtk_cell_renderer_pixbuf_get_type () {
	return pixbuf_renderer_type;
}

long gtk_cell_renderer_toggle_get_type () {
	return toggle_renderer_type;
}

String gtk_css_create_css_color_string (String background, String foreground, int property) {
	switch (property) {
		case SWT.FOREGROUND:
			if (foreground != null && background != null) {
				return foreground + "\n" + background;
			} else if (foreground != null) {
				return foreground;
			} else {
				return "";
			}
		case SWT.BACKGROUND:
			if (foreground != null && background != null) {
				return background + "\n" + foreground;
			} else if (background != null) {
				return background;
			} else {
				return "";
			}
		default:
			return "";
	}
}

/**
 * This method fetches GTK theme values/properties. This is accomplished
 * by determining the name of the current system theme loaded, giving that
 * name to GTK, and then parsing values from the returned theme contents.
 *
 * The idea here is that SWT variables that have corresponding GTK theme
 * elements can be fetched easily by supplying the SWT variable as an
 * parameter to this method.
 *
 * @param swt an Integer corresponding to the SWT color
 * @param cssOutput the gtk theme represented as css string.
 *
 * @return a String representation of the color parsed or "parsed" if the color was assigned
 * directly
 */
String gtk_css_default_theme_values (int swt, String cssOutput) {

	// Parse the theme values based on the corresponding SWT value
	// i.e. theme_selected_bg_color in GTK is SWT.COLOR_LIST_SELECTION in SWT
	int tSelected;
	int selected;
	/*
	 * These strings are the GTK named colors we are looking for. Once they are
	 * found they are sent to a parser which finds the actual values.
	 */
	String color = "";
	switch (swt) {
		case SWT.COLOR_LINK_FOREGROUND:
			return gtk_css_default_theme_values_irregular(swt, cssOutput);
		case SWT.COLOR_LIST_BACKGROUND:
			tSelected = cssOutput.indexOf ("@define-color theme_base_color");
			selected = cssOutput.indexOf ("@define-color base_color");
			if (tSelected != -1) {
				color =  simple_color_parser(cssOutput, "@define-color theme_base_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			} else if (selected != -1) {
				color = simple_color_parser(cssOutput, "@define-color base_color", selected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT.COLOR_LIST_FOREGROUND:
			tSelected = cssOutput.indexOf ("@define-color theme_text_color");
			selected = cssOutput.indexOf ("@define-color text_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color theme_text_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			} else if (selected != -1) {
				color = simple_color_parser(cssOutput, "@define-color text_color", selected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT.COLOR_LIST_SELECTION:
			tSelected = cssOutput.indexOf ("@define-color theme_selected_bg_color");
			selected = cssOutput.indexOf ("@define-color selected_bg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color theme_selected_bg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			} else if (selected != -1) {
				color = simple_color_parser(cssOutput, "@define-color selected_bg_color", selected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT_COLOR_LIST_SELECTION_INACTIVE:
			tSelected = cssOutput.indexOf ("@define-color theme_unfocused_selected_bg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color theme_unfocused_selected_bg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT.COLOR_LIST_SELECTION_TEXT:
			tSelected = cssOutput.indexOf ("@define-color theme_selected_fg_color");
			selected = cssOutput.indexOf ("@define-color selected_fg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color theme_selected_fg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			} else if (selected != -1) {
				color = simple_color_parser(cssOutput, "@define-color selected_fg_color", selected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT_COLOR_LIST_SELECTION_TEXT_INACTIVE:
			tSelected = cssOutput.indexOf ("@define-color theme_unfocused_selected_fg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color theme_unfocused_selected_fg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:
			tSelected = cssOutput.indexOf ("@define-color insensitive_fg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color insensitive_fg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:
			tSelected = cssOutput.indexOf ("@define-color insensitive_bg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color insensitive_bg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT.COLOR_WIDGET_BACKGROUND:
			tSelected = cssOutput.indexOf ("@define-color theme_bg_color");
			selected = cssOutput.indexOf ("@define-color bg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color theme_bg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			} else if (selected != -1) {
				color = simple_color_parser(cssOutput, "@define-color bg_color", selected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		case SWT.COLOR_WIDGET_FOREGROUND:
			tSelected = cssOutput.indexOf ("@define-color theme_fg_color");
			selected = cssOutput.indexOf ("@define-color fg_color");
			if (tSelected != -1) {
				color = simple_color_parser(cssOutput, "@define-color theme_fg_color", tSelected);
				if (!color.isEmpty()) {
					break;
				}
			} else if (selected != -1) {
				color = simple_color_parser(cssOutput, "@define-color fg_color", selected);
				if (!color.isEmpty()) {
					break;
				}
			}
			break;
		default:
			return "";
	}
	return color;
}

/**
 * Certain colors don't match up nicely to a "@define-color" tag in certain GTK themes.
 * For example Adwaita is one of the few themes that does not use a "@define-color"
 * tag for tooltip colors. It is therefore necessary to parse a tooltip CSS class.
 *
 * Since this varies from theme to theme, we first check if a "@define-color" tag
 * exists, if it is: parse it. If not, check for the tooltip class definition.
 *
 * @param swt an Integer corresponding to the SWT color
 * @param cssOutput a String representation of the currently loaded CSS theme
 * currently loaded CSS theme
 *
 * @return a String representation of the color parsed or "parsed" if the color was assigned
 * directly
 */
String gtk_css_default_theme_values_irregular(int swt, String cssOutput) {
	int tSelected, selected, classDef;
	String color = "";
	switch (swt) {
		case SWT.COLOR_LINK_FOREGROUND:
			selected = cssOutput.indexOf("@define-color link_color");
			tSelected = cssOutput.indexOf("@define-color theme_link_color");
			classDef = cssOutput.indexOf ("*:link {");
			// On Ubuntu and somenon-Adwaita themes, the link color is sometimes set to the
			// same as COLOR_LIST_SELECTION.
			int selectedBg = cssOutput.indexOf("@define-color link_color @selected_bg_color");
			if (selected != -1 || tSelected != -1) {
				if (selected != -1) {
					color = simple_color_parser(cssOutput, "@define-color link_color", selected);
				} else if (tSelected != -1) {
					color = simple_color_parser(cssOutput, "@define-color theme_link_color", tSelected);
				}
				if (!color.isEmpty()) {
					break;
				}
			} else if (selectedBg != -1) {
				COLOR_LINK_FOREGROUND_RGBA = COLOR_LIST_SELECTION_RGBA;
				return "parsed";
			} else if (classDef != -1) {
				COLOR_LINK_FOREGROUND_RGBA = gtk_css_parse_foreground(cssOutput, "*:link {");
				return "parsed";
			}
			break;
	}
	return color;
}

/**
 * This method allows for parsing of background colors from a GTK CSS string.
 * It allows for specific search input, such as a selector or tag, or for parsing
 * the first (and usually only) background color in a given GtkCssProvider.
 *
 * For example: given the string GtkWidget {background-color: rgba(255, 0, 0, 255);}
 * this method will return a GdkRGBA object with the color red. Supported formats
 * include "background-color" and just "background".
 *
 * @param css a CSS being parsed
 * @param precise a String representation of a selector to search for, or NULL if
 * the entire GtkCssProvider is to be parsed
 *
 * @return a GdkRGBA object representing the background color, or COLOR_WIDGET_BACKGROUND
 * if the background color could not be parsed or isn't set
 */
GdkRGBA gtk_css_parse_background (String css, String precise) {
	String shortOutput;
	int startIndex;
	GdkRGBA rgba = new GdkRGBA ();
	if (css.isEmpty()) return COLOR_WIDGET_BACKGROUND_RGBA;
	String searched = "";
	/*
	 * This section allows for finer searching: for example
	 * specifying precise as null will cause the whole GtkCssProvider
	 * to be searched and return the first background color value.
	 *
	 * Specifying a string for precise causes only the subset of the
	 * GtkCssProvider to be searched, provided that precise is a subset
	 * of the provider.
	 */
	if (precise != null) {
		if (css.contains(precise)) {
			searched = css.substring(css.indexOf(precise));
		}
	} else {
		searched = css;
	}
	if (searched.isEmpty()) {
		return COLOR_WIDGET_BACKGROUND_RGBA;
	}

	/* Although we only set the property "background-color", we can handle
	 * the "background" property as well. We check for either of these cases
	 * and extract a GdkRGBA object from the parsed CSS string.
	 */
	if (searched.contains ("background-color:")) {
		startIndex = searched.indexOf ("background-color:");
		shortOutput = searched.substring (startIndex + 18);
		rgba = gtk_css_property_to_rgba (shortOutput);
	} else if (searched.contains ("background:")) {
		startIndex = searched.indexOf ("background:");
		shortOutput = searched.substring (startIndex + 13);
		rgba = gtk_css_property_to_rgba (shortOutput);
	}
	return rgba;
}

String gtk_css_provider_to_string (long provider) {
	// Fetch the CSS in char/string format from the provider.
	if (provider == 0) {
		return "";
	}
	long str = GTK.gtk_css_provider_to_string(provider);
	if (str == 0) return "";
	int length = C.strlen (str);
	byte [] buffer = new byte [length];
	C.memmove (buffer, str, length);
	OS.g_free(str);
	return new String (Converter.mbcsToWcs (buffer));
}

/**
 * This method allows for parsing of foreground colors from a GTK CSS string.
 * It allows for specific search input, such as a selector or tag, or for parsing
 * the first (and usually only) foreground color in a given GtkCssProvider.
 *
 * For example: given the string GtkWidget {color: rgba(255, 0, 0, 255);}
 * this method will return a GdkRGBA object with the color red.
 *
 * @param css a CSS representation of the gtk theme
 * @param precise a String representation of a selector to search for, or NULL if
 * the entire GtkCssProvider is to be parsed
 *
 * @return a GdkRGBA object representing the foreground color or COLOR_WIDGET_FOREGROUND
 * if the foreground color could not be parsed or isn't set
 */
GdkRGBA gtk_css_parse_foreground (String css, String precise) {
	if (css.isEmpty()) return COLOR_WIDGET_FOREGROUND_RGBA;
	String shortOutput;
	int startIndex;
	GdkRGBA rgba = new GdkRGBA ();
	String searched = "";
	/*
	 * This section allows for finer searching: for example
	 * specifying precise as null will cause the whole GtkCssProvider
	 * to be searched and return the first foreground color value.
	 *
	 * Specifying a string for precise causes only the subset of the
	 * GtkCssProvider to be searched, provided that precise is a subset
	 * of the provider.
	 */
	if (precise != null) {
		if (css.contains(precise)) {
			searched = css.substring(css.indexOf(precise));
		}
	} else {
		searched = css;
	}
	if (searched.isEmpty()) {
		return COLOR_WIDGET_FOREGROUND_RGBA;
	}
	/*
	 * Because background-color and color have overlapping characters,
	 * a simple String.contains() check will not suffice. This means
	 * that a more encompassing regex is needed to capture "pure" color
	 * properties and filter out things like background-color, border-color,
	 * etc.
	 */
	String pattern = "[^-]color: rgba?\\((\\d+(,\\s?)?){3,4}\\)";
	Pattern r = Pattern.compile(pattern);
	Matcher m = r.matcher(searched);
	if (m.find()) {
		String match = m.group(0);
		if (match.contains("color:")) {
			startIndex = match.indexOf("color:");
			shortOutput = match.substring(startIndex + 7);
			rgba = gtk_css_property_to_rgba(shortOutput);
		}
	} else {
		return COLOR_WIDGET_FOREGROUND_RGBA;
	}
	return rgba;
}

/**
 * This method parses a string representation of a color
 * and returns a GdkRGBA object of that color.
 *
 * Supported formats:
 *  -a standard X11 color
 *  -a hex value in the form "#rgb", "#rrggbb", "#rrrgggbbb" or "rrrrggggbbbb"
 *  -an RGB color in the for "rgb(r,g,b)"
 *  -an RGBA color in the form "rgba(r,g,b,a)"
 *
 * @param property a String representation of the color
 *
 * @return a GdkRGBA object representing the color, or transparent (empty GdkRGBA)
 * if the color could not be parsed
 */
GdkRGBA gtk_css_property_to_rgba(String property) {
	GdkRGBA rgba = new GdkRGBA ();
	String [] propertyParsed = new String [1];
	//Note that we still need to remove the ";" character from the input string
	propertyParsed = property.split (";");
	GDK.gdk_rgba_parse (rgba, Converter.wcsToMbcs (propertyParsed[0], true));
	return rgba;
}

/**
 * In GdkRGBA, values are a double between 0-1. In CSS,
 * values are integers between 0-255 for r, g, and b.
 * Alpha is still a double between 0-1.
 *
 * The final CSS format is: rgba(int, int, int, double)
 * Due to this, there is a slight loss of precision.
 * Setting/getting with CSS *might* yield slight differences.
 *
 * @param rgba a GdkRGBA object containing the color to be parsed
 * or NULL
 *
 * @return a String representation of the color or COLOR_WIDGET_BACKGROUND
 * if NULL is specified as a parameter
 */
String gtk_rgba_to_css_string (GdkRGBA rgba) {
	/*
	 * In GdkRGBA, values are a double between 0-1.
	 * In CSS, values are integers between 0-255 for r, g, and b.
	 * Alpha is still a double between 0-1.
	 * The final CSS format is: rgba(int, int, int, double)
	 * Due to this, there is a slight loss of precision.
	 * Setting/getting with CSS *might* yield slight differences.
	 */
	GdkRGBA toConvert;
	if (rgba != null) {
		toConvert = rgba;
	} else {
		// If we have a null RGBA, set it to the default COLOR_WIDGET_BACKGROUND.
		toConvert = COLOR_WIDGET_BACKGROUND_RGBA;
	}
	long str = GDK.gdk_rgba_to_string (toConvert);
	int length = C.strlen (str);
	byte [] buffer = new byte [length];
	C.memmove (buffer, str, length);
	OS.g_free(str);
	return new String (Converter.mbcsToWcs (buffer));
}

/**
 * Gets the name of the widget in String format.
 *
 * @param handle a pointer to the GtkWidget resource
 *
 * @return a String representation of the widget's name
 */
String gtk_widget_get_name(long handle) {
	long str = GTK.gtk_widget_get_name (handle);
	String name;
	if (str == 0) {
		name = "*";
	} else {
		int length = C.strlen (str);
		byte [] buffer = new byte [length];
		C.memmove (buffer, str, length);
		name = new String (Converter.mbcsToWcs (buffer));
	}
	return name;
}

/**
 * Gets the CSS name of the widget provided. This
 * only works on GTK3.20+.
 *
 * @param handle a pointer to the GtkWidget resource
 *
 * @return a String representation of the widget's CSS name
 */
String gtk_widget_class_get_css_name(long handle) {
	long str = GTK.gtk_widget_class_get_css_name (GTK.GTK_WIDGET_GET_CLASS(handle));
	String name;
	if (str == 0) {
		name = "*";
	} else {
		int length = C.strlen (str);
		byte [] buffer = new byte [length];
		C.memmove (buffer, str, length);
		name = new String (Converter.mbcsToWcs (buffer));
	}
	return name;
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

static boolean isValidClass (Class<?> clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
}

/**
 * Returns the single instance of the application menu bar, or
 * <code>null</code> if there is no application menu bar for the platform.
 *
 * @return the application menu bar, or <code>null</code>
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
	int [] buffer = new int [1];
	long settings = GTK.gtk_settings_get_default ();
	OS.g_object_get (settings, GTK.gtk_alternative_button_order, buffer, 0);
	return buffer [0] == 1 ? SWT.LEFT : SWT.RIGHT;
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
	long settings = GTK.gtk_settings_get_default ();
	int [] buffer = new int [1];
	OS.g_object_get (settings, GTK.gtk_double_click_time, buffer, 0);
	return buffer [0];
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
	if (activeShell == null) return null;
	long shellHandle = activeShell.shellHandle;
	long handle = GTK.gtk_window_get_focus (shellHandle);
	if (handle == 0) return null;
	do {
		Widget widget = getWidget (handle);
		if (widget != null && widget instanceof Control) {
			Control control = (Control) widget;
			return control.isEnabled () ? control : null;
		}
	} while ((handle = GTK.gtk_widget_get_parent (handle)) != 0);
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

@Override
public int getDepth () {
	checkDevice ();
	if (GTK.GTK4) {
		// Bit depth is always 32 in GTK4
		return 32;
	} else {
		long screen = GDK.gdk_screen_get_default();
		long visual = GDK.gdk_screen_get_system_visual(screen);
		return GDK.gdk_visual_get_depth(visual);
	}
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
	return new Point [] {new Point (16, 16), new Point (32, 32)};
}

int getLastEventTime () {
	return lastEventTime;
}

int getMessageCount () {
	return synchronizer.getMessageCount ();
}

Dialog getModalDialog () {
	return modalDialog;
}

/**
 * Returns the work area, an EWMH property to store the size
 * and position of the screen not covered by dock and panel
 * windows.  See http://freedesktop.org/Standards/wm-spec.
 */
Rectangle getWorkArea() {
	if (OS.IsWin32) {
		return null;
	}
	byte[] name = Converter.wcsToMbcs ("_NET_WORKAREA", true); //$NON-NLS-1$
	long atom = GDK.gdk_atom_intern (name, true);
	if (atom == GDK.GDK_NONE) return null;
	long [] actualType = new long [1];
	int[] actualFormat = new int[1];
	int[] actualLength = new int[1];
	long [] data = new long [1];
	if (!GDK.gdk_property_get (GDK.gdk_get_default_root_window(), atom, GDK.GDK_NONE, 0, 16, 0, actualType, actualFormat, actualLength, data)) {
		return null;
	}
	Rectangle result = null;
	if (data [0] != 0) {
		if (actualLength [0] == 16) {
			int values [] = new int [4];
			C.memmove (values, data[0], 16);
			result = new Rectangle (values [0],values [1],values [2],values [3]);
		} else if (actualLength [0] == 32) {
			long values [] = new long [4];
			C.memmove (values, data[0], 32);
			result = new Rectangle ((int)values [0],(int)values [1],(int)values [2],(int)values [3]);
		}
		OS.g_free (data [0]);
	}
	return result;
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
	Monitor [] monitors = null;
	Rectangle workArea = DPIUtil.autoScaleDown (getWorkArea ());
	if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0)) {
		long display = GDK.gdk_display_get_default ();
		if (display != 0) {
			int monitorCount = GDK.gdk_display_get_n_monitors (display);
			if (monitorCount > 0) {
				monitors = new Monitor [monitorCount];
				GdkRectangle dest = new GdkRectangle ();
				for (int i = 0; i < monitorCount; i++) {
					long gdkMonitor = GDK.gdk_display_get_monitor(display, i);
					GDK.gdk_monitor_get_geometry (gdkMonitor, dest);
					Monitor monitor = new Monitor ();
					monitor.handle = i;
					monitor.x = DPIUtil.autoScaleDown (dest.x);
					monitor.y = DPIUtil.autoScaleDown (dest.y);
					monitor.width = DPIUtil.autoScaleDown (dest.width);
					monitor.height = DPIUtil.autoScaleDown (dest.height);
					if (!OS.isX11()) {
						int scale_factor = (int) GDK.gdk_monitor_get_scale_factor (
								GDK.gdk_display_get_monitor (GDK.gdk_display_get_default (),  (int) monitor.handle));
						monitor.zoom = scale_factor * 100;
					} else {
						monitor.zoom = Display._getDeviceZoom(monitor.handle);
					}

					// workarea was defined in GTK 3.4. If present, it will return the best results
					// since it takes into account per-monitor trim
					GDK.gdk_monitor_get_workarea (gdkMonitor, dest);
					monitor.clientX = DPIUtil.autoScaleDown (dest.x);
					monitor.clientY = DPIUtil.autoScaleDown (dest.y);
					monitor.clientWidth = DPIUtil.autoScaleDown (dest.width);
					monitor.clientHeight = DPIUtil.autoScaleDown (dest.height);
					monitors [i] = monitor;
				}
			}
		}
	} else {
		long screen = GDK.gdk_screen_get_default ();
		if (screen != 0) {
			int monitorCount = GDK.gdk_screen_get_n_monitors (screen);
			if (monitorCount > 0) {
				monitors = new Monitor [monitorCount];
				GdkRectangle dest = new GdkRectangle ();
				for (int i = 0; i < monitorCount; i++) {
					GDK.gdk_screen_get_monitor_geometry (screen, i, dest);
					Monitor monitor = new Monitor ();
					monitor.handle = i;
					monitor.x = DPIUtil.autoScaleDown (dest.x);
					monitor.y = DPIUtil.autoScaleDown (dest.y);
					monitor.width = DPIUtil.autoScaleDown (dest.width);
					monitor.height = DPIUtil.autoScaleDown (dest.height);
					monitor.zoom = Display._getDeviceZoom(monitor.handle);

					// workarea was defined in GTK 3.4. If present, it will return the best results
					// since it takes into account per-monitor trim
					GDK.gdk_screen_get_monitor_workarea (screen, i, dest);
					monitor.clientX = DPIUtil.autoScaleDown (dest.x);
					monitor.clientY = DPIUtil.autoScaleDown (dest.y);
					monitor.clientWidth = DPIUtil.autoScaleDown (dest.width);
					monitor.clientHeight = DPIUtil.autoScaleDown (dest.height);
					monitors [i] = monitor;
				}
			}
		}
	}

	if (monitors == null) {
		/* No multimonitor support detected, default to one monitor */
		Monitor monitor = new Monitor ();
		Rectangle bounds = getBounds ();
		monitor.x = bounds.x;
		monitor.y = bounds.y;
		monitor.width = bounds.width;
		monitor.height = bounds.height;
		if (workArea != null) {
			monitor.clientX = workArea.x;
			monitor.clientY = workArea.y;
			monitor.clientWidth = workArea.width;
			monitor.clientHeight = workArea.height;
		} else {
			monitor.clientX = monitor.x;
			monitor.clientY = monitor.y;
			monitor.clientWidth = monitor.width;
			monitor.clientHeight = monitor.height;
		}
		monitors = new Monitor [] { monitor };
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
	//Developer note, for testing see:
	//org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Display.test_getPrimaryMonitor()

	checkDevice ();
	Monitor [] monitors = getMonitors ();
	int primaryMonitorIndex = 0;

	//attempt to find actual primary monitor if one is configured:
	if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0)) {
		long display = GDK.gdk_display_get_default();
		long monitor = GDK.gdk_display_get_primary_monitor(display);
		long toCompare;
		for (int i = 0; i < monitors.length; i++) {
			toCompare = GDK.gdk_display_get_monitor(display, i);
			if (toCompare == monitor) {
				return monitors[i];
			}
		}
	} else {
		long screen = GDK.gdk_screen_get_default ();
		if (screen != 0) {
			//if no primary monitor is configured by the user, this returns 0.
			primaryMonitorIndex = GDK.gdk_screen_get_primary_monitor (screen);
		}
	}
	return monitors [primaryMonitorIndex];
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
	for (int i = 0; i < widgetTable.length; i++) {
		Widget widget = widgetTable [i];
		if (!(widget instanceof Shell)) {
			continue;
		}
		if (!widget.isDisposed()) {
			int j = 0;
			while (j < index) {
				if (result [j] == widget) break;
				j++;
			}
			if (j == index) {
				if (index == result.length) {
					Shell [] newResult = new Shell [index + 16];
					System.arraycopy (result, 0, newResult, 0, index);
					result = newResult;
				}
				result [index++] = (Shell) widget;
			}
		} else {
			// bug 532632: somehow widgetTable got corrupted and we have disposed
			// shell entries in the table (at least one).

			// We don't throw an error here because it was not broken here, but
			// we at least try to report an error (we have no logging context).
			System.err.println ("SWT ERROR: disposed shell detected in the table" + debugInfoForIndex(i));

			// As of today widgetTable contains *four* entries for the *same*
			// Shell instance. If we found one broken, there can be others...
			// So we clean here all the occurencies of the leaked shell.
			for (int j = i; j < widgetTable.length; j++) {
				if(widgetTable[j] == widget) {
					widgetTable[j] = null;
				}
			}
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
@Override
public Color getSystemColor (int id) {
	checkDevice ();
	GdkRGBA gdkRGBA = null;
	switch (id) {
	case SWT.COLOR_LINK_FOREGROUND: 					gdkRGBA = copyRGBA(COLOR_LINK_FOREGROUND_RGBA); break;
	case SWT.COLOR_INFO_FOREGROUND: 					gdkRGBA = copyRGBA(COLOR_INFO_FOREGROUND_RGBA); break;
	case SWT.COLOR_INFO_BACKGROUND: 					gdkRGBA = copyRGBA(COLOR_INFO_BACKGROUND_RGBA); break;
	case SWT.COLOR_TITLE_FOREGROUND:					gdkRGBA = copyRGBA(COLOR_TITLE_FOREGROUND_RGBA); break;
	case SWT.COLOR_TITLE_BACKGROUND:					gdkRGBA = copyRGBA(COLOR_TITLE_BACKGROUND_RGBA); break;
	case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:			gdkRGBA = copyRGBA(COLOR_TITLE_BACKGROUND_GRADIENT_RGBA); break;
	case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:			gdkRGBA = copyRGBA(COLOR_TITLE_INACTIVE_FOREGROUND_RGBA); break;
	case SWT.COLOR_TITLE_INACTIVE_BACKGROUND:			gdkRGBA = copyRGBA(COLOR_TITLE_INACTIVE_BACKGROUND_RGBA); break;
	case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:	gdkRGBA = copyRGBA(COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT_RGBA); break;
	case SWT.COLOR_WIDGET_DARK_SHADOW:					gdkRGBA = copyRGBA(COLOR_WIDGET_DARK_SHADOW_RGBA); break;
	case SWT.COLOR_WIDGET_NORMAL_SHADOW:				gdkRGBA = copyRGBA(COLOR_WIDGET_NORMAL_SHADOW_RGBA); break;
	case SWT.COLOR_WIDGET_LIGHT_SHADOW: 				gdkRGBA = copyRGBA(COLOR_WIDGET_LIGHT_SHADOW_RGBA); break;
	case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:				gdkRGBA = copyRGBA(COLOR_WIDGET_HIGHLIGHT_SHADOW_RGBA); break;
	case SWT.COLOR_WIDGET_BACKGROUND: 					gdkRGBA = copyRGBA(COLOR_WIDGET_BACKGROUND_RGBA); break;
	case SWT.COLOR_WIDGET_FOREGROUND: 					gdkRGBA = copyRGBA(COLOR_WIDGET_FOREGROUND_RGBA); break;
	case SWT.COLOR_WIDGET_BORDER: 						gdkRGBA = copyRGBA(COLOR_WIDGET_BORDER_RGBA); break;
	case SWT.COLOR_LIST_FOREGROUND: 					gdkRGBA = copyRGBA(COLOR_LIST_FOREGROUND_RGBA); break;
	case SWT.COLOR_LIST_BACKGROUND: 					gdkRGBA = copyRGBA(COLOR_LIST_BACKGROUND_RGBA); break;
	case SWT.COLOR_LIST_SELECTION: 						gdkRGBA = copyRGBA(COLOR_LIST_SELECTION_RGBA); break;
	case SWT.COLOR_LIST_SELECTION_TEXT: 				gdkRGBA = copyRGBA(COLOR_LIST_SELECTION_TEXT_RGBA); break;
	default:
		return super.getSystemColor (id);
	}
	if (gdkRGBA == null) return super.getSystemColor (SWT.COLOR_BLACK);
	return Color.gtk_new (this, gdkRGBA);
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
	switch (id) {
		case SWT.ICON_ERROR:
			if (errorImage == null) {
				errorImage = createImage ("dialog-error"); //$NON-NLS-1$
			}
			return errorImage;
		case SWT.ICON_INFORMATION:
		case SWT.ICON_WORKING:
			if (infoImage == null) {
				infoImage = createImage ("dialog-information"); //$NON-NLS-1$
			}
			return infoImage;
		case SWT.ICON_QUESTION:
			if (questionImage == null) {
				questionImage = createImage ("dialog-question"); //$NON-NLS-1$
			}
			return questionImage;
		case SWT.ICON_WARNING:
			if (warningImage == null) {
				warningImage = createImage ("dialog-warning"); //$NON-NLS-1$
			}
			return warningImage;
	}
	return null;
}

/**
 * Returns the single instance of the system-provided menu for the application, or
 * <code>null</code> on platforms where no menu is provided for the application.
 *
 * @return the system menu, or <code>null</code>
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
 * This method converts from RGB to HSV, from HSV
 * to HSL (including brightness), from HSL back to HSV
 * and from HSV back to RGB. In short (ordered 1 - 5):
 *
 * 1. RGB -> HSV
 * 2. HSV -> HSL
 * 3. HSL -> HSL (with brightness)
 * 4. HSL -> HSV
 * 5. HSV -> RGB
 *
 * NOTE: hue, saturation, and luminosity are values
 * from 0 to 1. Brightness can be any number from
 * 0 to Double.MAX_VALUE, but is only practical
 * when luminosity and saturation are <= 1.
 *
 * The higher the brightness, the lighter a color gets.
 * Lighter here means "more white", i.e. saturation
 * and luminosity values of 1.0 means the color is pure white.
 *
 * More info on the conversion can be found here:
 *
 * https://en.wikipedia.org/wiki/HSL_and_HSV
 * http://codeitdown.com/hsl-hsb-hsv-color/
 *
 * @param rgba the source GdkRGBA from which RGB values are copied from
 * @param brightness a value between 0 and Double.MAX_VALUE which modifies the brightness of saturation and luminosity
 * @return GdkRGBA object with calculated RGB values
 */
GdkRGBA toGdkRGBA (GdkRGBA rgba, double brightness) {
	// Copy RGB values into a new object
	GdkRGBA newRGBA = new GdkRGBA ();
	newRGBA.red = rgba.red;
	newRGBA.green = rgba.green;
	newRGBA.blue = rgba.blue;
	newRGBA.alpha = rgba.alpha;

	// Instantiate hue, saturation, and value doubles for HSV.
	// Perform the RGB to HSV conversion.
	double[] hue = new double[1];
	double[] saturationHSV = new double[1];
	double[] value = new double[1];
	GTK.gtk_rgb_to_hsv(newRGBA.red, newRGBA.green, newRGBA.blue, hue, saturationHSV, value);

	// Calculate luminosity
	double luminosity = ((2 - saturationHSV[0]) * value[0]) / 2;

	// Calculate saturation
	double saturationHSL = saturationHSV[0] * value[0];
	saturationHSL /= (luminosity <= 1) ? luminosity : 2 - luminosity;

	// Calculate saturation and luminosity with brightness.
	saturationHSL = Math.max(0f, Math.min(1f, saturationHSL * brightness));
	luminosity = Math.max(0f, Math.min(1f, luminosity * brightness));

	// Convert from HSL (with brightness) back to HSV
	luminosity *= 2;
	saturationHSL *= luminosity <= 1 ? luminosity : 2 - luminosity;
	value[0] = (luminosity + saturationHSL) / 2;
	saturationHSV[0] = (2 * saturationHSL) / (luminosity + saturationHSL);

	// Convert from HSV back to RGB and return the GdkRGBA object
	GTK.gtk_hsv_to_rgb(hue[0], saturationHSV[0], value[0], hue, saturationHSV, value);
	newRGBA.red = hue[0];
	newRGBA.green = saturationHSV[0];
	newRGBA.blue = value[0];
	return newRGBA;
}

/**
 * Calculates original color from RGBA with premultiplied alpha.
 *
 * NOTE: Calculating inverse gives a range of possible colors due to rounding that
 * occurs with integer calculations. However, alpha-blend formula only has the
 * multiplied component, so all of those inverses are equivalent.
 */
static int inversePremultipliedColor(int color, int alpha) {
	if (alpha == 0) return 0;
	return (255*color + alpha-1) / alpha;
}

/**
 * What user sees is a combination of multiple layers.
 * This is only important when top layer is semi-transparent.
 */
private static void renderAllBackgrounds(long styleContext, long cairo) {
	long parentStyleContext = GTK.gtk_style_context_get_parent (styleContext);
	if (parentStyleContext != 0) renderAllBackgrounds (parentStyleContext, cairo);

	GTK.gtk_render_background (styleContext, cairo, -50, -50, 100, 100);
}

/**
 * Background in GTK theme can be more complex then just solid color:
 * 1) Due to 'background-image', 'background-position', 'background-repeat', etc.
 *    Example: 'tooltip' in 'Ambiance' theme uses 'background-image'.
 * 2) If background is semi-transparent, user actually sees a combination of layers.
 *    Example: 'tooltip' in 'HighContrast' theme has transparent label.
 * Both problems are solved by drawing to a temporary image and getting
 * the color of the pixel in the middle.
 */
GdkRGBA styleContextEstimateBackgroundColor(long context, int state) {
	// Render to a temporary image
	GTK.gtk_style_context_save (context);
	GTK.gtk_style_context_set_state (context, state);
	long surface = Cairo.cairo_image_surface_create (Cairo.CAIRO_FORMAT_ARGB32, 1, 1);
	long cairo = Cairo.cairo_create (surface);
	renderAllBackgrounds (context, cairo);
	Cairo.cairo_fill (cairo);
	Cairo.cairo_surface_flush (surface);
	byte[] buffer = new byte[4];
	C.memmove (buffer, Cairo.cairo_image_surface_get_data(surface), buffer.length);
	Cairo.cairo_surface_destroy (surface);
	Cairo.cairo_destroy (cairo);
	GTK.gtk_style_context_restore (context);

	// CAIRO_FORMAT_ARGB32 means a-r-g-b order, 1 byte per value.
	int a = Byte.toUnsignedInt(buffer[3]);
	int r = Byte.toUnsignedInt(buffer[2]);
	int g = Byte.toUnsignedInt(buffer[1]);
	int b = Byte.toUnsignedInt(buffer[0]);

	// NOTE: cairo uses premultiplied alpha (see CAIRO_FORMAT_ARGB32)
	GdkRGBA rgba = new GdkRGBA ();
	rgba.alpha = a / 255f;
	rgba.red   = inversePremultipliedColor(r, a) / 255f;
	rgba.green = inversePremultipliedColor(g, a) / 255f;
	rgba.blue  = inversePremultipliedColor(b, a) / 255f;

	return rgba;
}

GdkRGBA copyRGBA (GdkRGBA source) {
	GdkRGBA retRGBA =  new GdkRGBA ();
	if (source != null) {
		retRGBA.alpha = source.alpha;
		retRGBA.red = source.red;
		retRGBA.green = source.green;
		retRGBA.blue = source.blue;
	}
	return retRGBA;
}

void initializeSystemColors () {
	COLOR_WIDGET_DARK_SHADOW_RGBA = new GdkRGBA ();
	COLOR_WIDGET_DARK_SHADOW_RGBA.alpha = 1.0;

	// Initialize and create a list of X11 named colors
	initializeColorList();

	/*
	 * Feature in GTK: previously SWT fetched system colors using
	 * GtkStyleContext machinery. This machinery is largely deprecated
	 * and will all together stop functioning eventually. Instead, we
	 * can parse the GTK system theme and use the values stored there to
	 * generate SWT's system colors.
	 *
	 * The functionality works for GTK3.14 and above as follows:
	 *
	 * 1) load and parse the system theme
	 * 2) check to see if the value needed exists in the theme
	 * 3a) if the value exists, parse it and convert it to a GdkColor object
	 * 3b) if the value doesn't exist, use the old GtkStyleContext machinery
	 *     to fetch and return it as a GdkColor object
	 *
	 * Some colors have multiple different theme values that correspond to
	 * them, while some colors only have one potential match. Therefore
	 * some colors will have better theme coverage than others.
	 */
	/*
	 * Find current GTK theme: either use the system theme,
	 * or one provided using the GTK_THEME environment variable.
	 * See bug 534007.
	 */
	byte [] buffer = OS.GTK_THEME_SET ? Converter.wcsToMbcs (OS.GTK_THEME_NAME, true) : OS.getThemeNameBytes();
	// Load the dark variant if specified
	byte [] darkBuffer = OS.GTK_THEME_DARK ? darkBuffer = Converter.wcsToMbcs ("dark", true) : null;

	// Fetch the actual theme in char/string format
	long themeProvider = GTK.gtk_css_provider_get_named(buffer, darkBuffer);

	String cssOutput = gtk_css_provider_to_string(themeProvider);

	// Load Widget colors first, because 'COLOR_WIDGET_BACKGROUND_RGBA'
	// can be used as substitute for other missing colors.
	initializeSystemColorsWidget(cssOutput);

	initializeSystemColorsList(cssOutput);
	initializeSystemColorsTitle(cssOutput);
	initializeSystemColorsLink(cssOutput);
	initializeSystemColorsTooltip();

	COLOR_TITLE_FOREGROUND_RGBA = COLOR_LIST_SELECTION_TEXT_RGBA;
	COLOR_TITLE_BACKGROUND_RGBA = COLOR_LIST_SELECTION_RGBA;
	COLOR_TITLE_BACKGROUND_GRADIENT_RGBA = toGdkRGBA (COLOR_LIST_SELECTION_RGBA, 1.3);
	COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT_RGBA = toGdkRGBA (COLOR_TITLE_INACTIVE_BACKGROUND_RGBA, 1.3);
}

void initializeSystemColorsWidget(String cssOutput) {
	long context = GTK.gtk_widget_get_style_context (shellHandle);
	GdkRGBA rgba = new GdkRGBA();

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorWidgetForeground = gtk_css_default_theme_values(SWT.COLOR_WIDGET_FOREGROUND, cssOutput);
		if (!colorWidgetForeground.isEmpty()) {
			COLOR_WIDGET_FOREGROUND_RGBA = gtk_css_property_to_rgba (colorWidgetForeground);
		} else {
			COLOR_WIDGET_FOREGROUND_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_NORMAL);
		}
	} else {
		COLOR_WIDGET_FOREGROUND_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_NORMAL);
	}

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorWidgetBackground = gtk_css_default_theme_values(SWT.COLOR_WIDGET_BACKGROUND, cssOutput);
		if (!colorWidgetBackground.isEmpty()) {
			COLOR_WIDGET_BACKGROUND_RGBA = gtk_css_property_to_rgba (colorWidgetBackground);
		} else {
			GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_NORMAL, rgba);
			COLOR_WIDGET_BACKGROUND_RGBA = copyRGBA (rgba);
		}
	} else {
		GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_NORMAL, rgba);
		COLOR_WIDGET_BACKGROUND_RGBA = copyRGBA (rgba);
	}
	COLOR_WIDGET_LIGHT_SHADOW_RGBA = COLOR_WIDGET_BACKGROUND_RGBA;
	COLOR_WIDGET_NORMAL_SHADOW_RGBA = toGdkRGBA (COLOR_WIDGET_BACKGROUND_RGBA, 0.7);
	COLOR_WIDGET_HIGHLIGHT_SHADOW_RGBA = toGdkRGBA (COLOR_WIDGET_BACKGROUND_RGBA, 1.3);
}

void initializeSystemColorsList(String cssOutput) {
	long context = GTK.gtk_widget_get_style_context (shellHandle);
	GdkRGBA rgba = new GdkRGBA();

	// Apply temporary styles
	GTK.gtk_style_context_save (context);
	GTK.gtk_style_context_add_class(context, GTK.GTK_STYLE_CLASS_VIEW);
	GTK.gtk_style_context_add_class(context, GTK.GTK_STYLE_CLASS_CELL);
	GTK.gtk_style_context_invalidate(context);

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorListForeground = gtk_css_default_theme_values(SWT.COLOR_LIST_FOREGROUND, cssOutput);
		if (!colorListForeground.isEmpty()) {
			COLOR_LIST_FOREGROUND_RGBA = gtk_css_property_to_rgba (colorListForeground);
		} else {
			COLOR_LIST_FOREGROUND_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_NORMAL);
		}
	} else {
		COLOR_LIST_FOREGROUND_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_NORMAL);
	}

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorListBackground = gtk_css_default_theme_values(SWT.COLOR_LIST_BACKGROUND, cssOutput);
		if (!colorListBackground.isEmpty()) {
			COLOR_LIST_BACKGROUND_RGBA = gtk_css_property_to_rgba (colorListBackground);
		} else {
			GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_NORMAL, rgba);
			COLOR_LIST_BACKGROUND_RGBA = copyRGBA(rgba);
		}
	} else {
		GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_NORMAL, rgba);
		COLOR_LIST_BACKGROUND_RGBA = copyRGBA(rgba);
	}

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorListSelectionText = gtk_css_default_theme_values(SWT.COLOR_LIST_SELECTION_TEXT, cssOutput);
		if (!colorListSelectionText.isEmpty()) {
			COLOR_LIST_SELECTION_TEXT_RGBA = gtk_css_property_to_rgba (colorListSelectionText);
		} else {
			COLOR_LIST_SELECTION_TEXT_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_SELECTED);
		}
	} else {
		COLOR_LIST_SELECTION_TEXT_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_SELECTED);
	}

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorListSelection = gtk_css_default_theme_values(SWT.COLOR_LIST_SELECTION, cssOutput);
		if (!colorListSelection.isEmpty()) {
			COLOR_LIST_SELECTION_RGBA = gtk_css_property_to_rgba (colorListSelection);
		} else {
			GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_SELECTED, rgba);
			COLOR_LIST_SELECTION_RGBA = copyRGBA (rgba);
		}
	} else {
		GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_SELECTED, rgba);
		COLOR_LIST_SELECTION_RGBA = copyRGBA (rgba);
	}

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorListSelectionTextInactive = gtk_css_default_theme_values(SWT_COLOR_LIST_SELECTION_TEXT_INACTIVE, cssOutput);
		if (!colorListSelectionTextInactive.isEmpty()) {
			COLOR_LIST_SELECTION_TEXT_INACTIVE_RGBA = gtk_css_property_to_rgba (colorListSelectionTextInactive);
		} else {
			COLOR_LIST_SELECTION_TEXT_INACTIVE_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_ACTIVE);
		}
	} else {
		COLOR_LIST_SELECTION_TEXT_INACTIVE_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_ACTIVE);
	}

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorListSelectionInactive = gtk_css_default_theme_values(SWT_COLOR_LIST_SELECTION_INACTIVE, cssOutput);
		if (!colorListSelectionInactive.isEmpty()) {
			COLOR_LIST_SELECTION_INACTIVE_RGBA = gtk_css_property_to_rgba (colorListSelectionInactive);
		} else {
			GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_ACTIVE, rgba);
			COLOR_LIST_SELECTION_INACTIVE_RGBA = copyRGBA (rgba);
		}
	} else {
		GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_ACTIVE, rgba);
		COLOR_LIST_SELECTION_INACTIVE_RGBA = copyRGBA (rgba);
	}

	// Revert temporary styles
	GTK.gtk_style_context_restore (context);
}

void initializeSystemColorsTitle(String cssOutput) {
	long context = GTK.gtk_widget_get_style_context (shellHandle);
	GdkRGBA rgba = new GdkRGBA();

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorTitleInactiveForeground = gtk_css_default_theme_values(SWT.COLOR_TITLE_INACTIVE_FOREGROUND, cssOutput);
		if (!colorTitleInactiveForeground.isEmpty()) {
			COLOR_TITLE_INACTIVE_FOREGROUND_RGBA = gtk_css_property_to_rgba (colorTitleInactiveForeground);
		} else {
			COLOR_TITLE_INACTIVE_FOREGROUND_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_INSENSITIVE);
		}
	} else {
		COLOR_TITLE_INACTIVE_FOREGROUND_RGBA = styleContextGetColor (context, GTK.GTK_STATE_FLAG_INSENSITIVE);
	}

	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorTitleInactiveBackground = gtk_css_default_theme_values(SWT.COLOR_TITLE_INACTIVE_BACKGROUND, cssOutput);
		if (!colorTitleInactiveBackground.isEmpty()) {
			COLOR_TITLE_INACTIVE_BACKGROUND_RGBA = gtk_css_property_to_rgba (colorTitleInactiveBackground);
		} else {
			GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_INSENSITIVE, rgba);
			COLOR_TITLE_INACTIVE_BACKGROUND_RGBA = copyRGBA (rgba);
		}
	} else {
		GTK.gtk_style_context_get_background_color (context, GTK.GTK_STATE_FLAG_INSENSITIVE, rgba);
		COLOR_TITLE_INACTIVE_BACKGROUND_RGBA = copyRGBA (rgba);
	}
}

private void initializeSystemColorsLink(String cssOutput) {
	// NOTE: If COLOR_LINK_FOREGROUND cannot be found from the GTK CSS theme then there is no reliable
	// way to find it on GTK3 using GtkStyleContext machinery. Use COLOR_LIST_SELECTION instead
	// as they are often the same.
	if (GTK.GTK_VERSION >= OS.VERSION(3, 14, 0)) {
		String colorLinkForeground = gtk_css_default_theme_values(SWT.COLOR_LINK_FOREGROUND, cssOutput);
		if (!colorLinkForeground.isEmpty()) {
			if (colorLinkForeground != "parsed") {
				COLOR_LINK_FOREGROUND_RGBA = gtk_css_property_to_rgba (colorLinkForeground);
			}
		} else {
			COLOR_LINK_FOREGROUND_RGBA = COLOR_LIST_SELECTION_RGBA;
		}
	} else {
		COLOR_LINK_FOREGROUND_RGBA = COLOR_LIST_SELECTION_RGBA;
	}
}

void initializeSystemColorsTooltip() {
	// Create a temporary tooltip
	long tooltip = OS.g_object_new(GTK.gtk_tooltip_get_type(), 0);

	// Add temporary label as custom control into tooltip
	long customLabel = OS.g_object_new(GTK.gtk_label_get_type(), 0);
	GTK.gtk_tooltip_set_custom(tooltip, customLabel);

	// Just use temporary label; this is easier then finding the original label
	long styleContextLabel = GTK.gtk_widget_get_style_context(customLabel);
	COLOR_INFO_FOREGROUND_RGBA = styleContextGetColor(styleContextLabel, GTK.GTK_STATE_FLAG_NORMAL);
	COLOR_INFO_BACKGROUND_RGBA = styleContextEstimateBackgroundColor(styleContextLabel, GTK.GTK_STATE_FLAG_NORMAL);

	// Cleanup
	// customLabel is owned by tooltip and will be destroyed automatically
	OS.g_object_unref(tooltip);
}

GdkRGBA styleContextGetColor(long context, int flag) {
	/*
	* Feature in GTK: we need to handle calls to gtk_style_context_get_color()
	* differently due to changes in GTK3.18+. This solves failing test cases
	* which started failing after GTK3.16. See Bug 481122 for more info.
	* Reference: https://blogs.gnome.org/mclasen/2015/11/20/a-gtk-update/
	*/
	GdkRGBA rgba = new GdkRGBA ();
	if (GTK.GTK4) {
		GTK.gtk_style_context_get_color(context, rgba);
	} else if (GTK.GTK_VERSION >= OS.VERSION(3, 18, 0)) {
		GTK.gtk_style_context_save(context);
		GTK.gtk_style_context_set_state(context, flag);
		GTK.gtk_style_context_get_color (context, flag, rgba);
		GTK.gtk_style_context_restore(context);
	} else {
		GTK.gtk_style_context_get_color (context, flag, rgba);
	}
	return rgba;
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

/**
 * Returns a boolean indicating whether a touch-aware input device is
 * attached to the system and is ready for use.
 *
 * @return <code>true</code> if a touch-aware input device is detected, or <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.7
 */
public boolean getTouchEnabled() {
	checkDevice();
	return false;
}

Widget getWidget (long handle) {
	if (handle == 0) return null;
	if (lastWidget != null && lastHandle == handle) return lastWidget;
	long index = OS.g_object_get_qdata (handle, SWT_OBJECT_INDEX) - 1;
	if (0 <= index && index < widgetTable.length) {
		lastHandle = handle;
		return lastWidget = widgetTable [(int)index];
	}
	return null;
}

long idleProc (long data) {
	boolean result = runAsyncMessages (false);
	if (!result) {
		synchronized (idleLock) {
			idleHandle = 0;
		}
	}
	return result ? 1 : 0;
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
@Override
protected void init () {
	super.init ();
	initializeCallbacks ();
	initializeSubclasses ();
	initializeSystemColors ();
	initializeSystemSettings ();
	initializeWidgetTable ();
	initializeWindowManager ();
	initializeSessionManager ();
}

void initializeCallbacks () {
	closures = new long [Widget.LAST_SIGNAL];
	closuresCount = new int[Widget.LAST_SIGNAL];
	closuresProc = new long [Widget.LAST_SIGNAL];
	signalIds = new int [Widget.LAST_SIGNAL];

	/* Cache signals for GtkWidget */
	signalIds [Widget.BUTTON_PRESS_EVENT] = OS.g_signal_lookup (OS.button_press_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.BUTTON_RELEASE_EVENT] = OS.g_signal_lookup (OS.button_release_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.CONFIGURE_EVENT] = OS.g_signal_lookup (OS.configure_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.DELETE_EVENT] = OS.g_signal_lookup (OS.delete_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.ENTER_NOTIFY_EVENT] = OS.g_signal_lookup (OS.enter_notify_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.EVENT] = OS.g_signal_lookup (OS.event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.EVENT_AFTER] = OS.g_signal_lookup (OS.event_after, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.EXPOSE_EVENT] = OS.g_signal_lookup (OS.draw, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.EXPOSE_EVENT_INVERSE] = OS.g_signal_lookup (OS.draw, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.FOCUS] = OS.g_signal_lookup (OS.focus, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.FOCUS_IN_EVENT] = OS.g_signal_lookup (OS.focus_in_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.FOCUS_OUT_EVENT] = OS.g_signal_lookup (OS.focus_out_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.GRAB_FOCUS] = OS.g_signal_lookup (OS.grab_focus, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.HIDE] = OS.g_signal_lookup (OS.hide, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.KEY_PRESS_EVENT] = OS.g_signal_lookup (OS.key_press_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.KEY_RELEASE_EVENT] = OS.g_signal_lookup (OS.key_release_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.LEAVE_NOTIFY_EVENT] = OS.g_signal_lookup (OS.leave_notify_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.MAP] = OS.g_signal_lookup (OS.map, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.MAP_EVENT] = OS.g_signal_lookup (OS.map_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.MNEMONIC_ACTIVATE] = OS.g_signal_lookup (OS.mnemonic_activate, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.MOTION_NOTIFY_EVENT] = OS.g_signal_lookup (OS.motion_notify_event, GTK.GTK_TYPE_WIDGET ());
	/*
	 * Connect to the "popped-up" signal on GTK3.22+ if the user has specified the
	 * SWT_MENU_LOCATION_DEBUGGING environment variable.
	 */
	if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0) && OS.SWT_MENU_LOCATION_DEBUGGING) {
		long menuType = GTK.GTK_TYPE_MENU ();
		OS.g_type_class_ref (menuType);
		signalIds [Widget.POPPED_UP] = OS.g_signal_lookup (OS.popped_up, menuType);
	} else {
		signalIds [Widget.POPPED_UP] = 0;
	}
	signalIds [Widget.POPUP_MENU] = OS.g_signal_lookup (OS.popup_menu, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.REALIZE] = OS.g_signal_lookup (OS.realize, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.SCROLL_EVENT] = OS.g_signal_lookup (OS.scroll_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.SHOW] = OS.g_signal_lookup (OS.show, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.SHOW_HELP] = OS.g_signal_lookup (OS.show_help, GTK.GTK_TYPE_WIDGET ());
	if (GTK.GTK4) {
		signalIds [Widget.SIZE_ALLOCATE_GTK4] = OS.g_signal_lookup (OS.size_allocate, GTK.GTK_TYPE_WIDGET ());
	} else {
		signalIds [Widget.SIZE_ALLOCATE] = OS.g_signal_lookup (OS.size_allocate, GTK.GTK_TYPE_WIDGET ());
	}
	signalIds [Widget.STYLE_UPDATED] = OS.g_signal_lookup (OS.style_updated, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.UNMAP] = OS.g_signal_lookup (OS.unmap, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.UNMAP_EVENT] = OS.g_signal_lookup (OS.unmap_event, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.UNREALIZE] = OS.g_signal_lookup (OS.realize, GTK.GTK_TYPE_WIDGET ());
	signalIds [Widget.WINDOW_STATE_EVENT] = OS.g_signal_lookup (OS.window_state_event, GTK.GTK_TYPE_WIDGET ());

	if (GTK.GTK4) {
		snapshotDraw = new Callback (getClass (), "snapshotDrawProc", 2); //$NON-NLS-1$
		snapshotDrawProc = snapshotDraw.getAddress ();
		if (snapshotDrawProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	}

	windowCallback2 = new Callback (this, "windowProc", 2); //$NON-NLS-1$
	windowProc2 = windowCallback2.getAddress ();
	if (windowProc2 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	if (GTK.GTK4) {
		keyPressReleaseCallback = new Callback (this, "keyPressReleaseProc", long.class, new Type[] {
				long.class, int.class, int.class, int.class, long.class}); //$NON-NLS-1$
		keyPressReleaseProc = keyPressReleaseCallback.getAddress ();
		if (keyPressReleaseProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

		/*
		 * Usually GTK4 signals will be connected via g_signal_connect(),
		 * i.e. without closures, but we will assign the closures anyways
		 * just to be safe.
		 */
		closuresProc [Widget.KEY_PRESSED] = keyPressReleaseProc;
		closuresProc [Widget.KEY_RELEASED] = keyPressReleaseProc;

		focusCallback = new Callback (this, "focusProc", long.class, new Type[] {
				long.class, long.class}); //$NON-NLS-1$
		focusProc = focusCallback.getAddress ();
		if (focusProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

		closuresProc [Widget.FOCUS_IN] = focusProc;
		closuresProc [Widget.FOCUS_OUT] = focusProc;

		enterMotionScrollCallback = new Callback (this, "enterMotionScrollProc", long.class, new Type[] {
				long.class, double.class, double.class, long.class}); //$NON-NLS-1$
		enterMotionScrollProc = enterMotionScrollCallback.getAddress ();
		if (enterMotionScrollProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

		closuresProc [Widget.ENTER] = enterMotionScrollProc;
		closuresProc [Widget.MOTION] = enterMotionScrollProc;
		closuresProc [Widget.SCROLL] = enterMotionScrollProc;

		gesturePressReleaseCallback = new Callback (this, "gesturePressReleaseProc", long.class, new Type[] {
				long.class, int.class, double.class, double.class, long.class}); //$NON-NLS-1$
		gesturePressReleaseProc = gesturePressReleaseCallback.getAddress();
		if (gesturePressReleaseProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

		closuresProc [Widget.GESTURE_PRESSED] = gesturePressReleaseProc;
		closuresProc [Widget.GESTURE_RELEASED] = gesturePressReleaseProc;

		leaveCallback = new Callback (this, "leaveProc", long.class, new Type[] {
				long.class, long.class}); //$NON-NLS-1$
		leaveProc = leaveCallback.getAddress ();
		if (leaveProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

		closuresProc [Widget.LEAVE] = leaveProc;

		notifyStateCallback = new Callback(this, "notifyStateProc", long.class, new Type[] {
				long.class, long.class, long.class}); //$NON-NLS-1$
		notifyStateProc = notifyStateCallback.getAddress();
		if (notifyStateProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

		closuresProc [Widget.NOTIFY_STATE] = notifyStateProc;
	}

	closuresProc [Widget.ACTIVATE] = windowProc2;
	closuresProc [Widget.ACTIVATE_INVERSE] = windowProc2;
	closuresProc [Widget.CHANGED] = windowProc2;
	closuresProc [Widget.CLICKED] = windowProc2;
	closuresProc [Widget.CLOSE_REQUEST] = windowProc2;
	closuresProc [Widget.CREATE_MENU_PROXY] = windowProc2;
	closuresProc [Widget.DAY_SELECTED] = windowProc2;
	closuresProc [Widget.DAY_SELECTED_DOUBLE_CLICK] = windowProc2;
	closuresProc [Widget.HIDE] = windowProc2;
	closuresProc [Widget.GRAB_FOCUS] = windowProc2;
	closuresProc [Widget.MAP] = windowProc2;
	closuresProc [Widget.MONTH_CHANGED] = windowProc2;
	closuresProc [Widget.OUTPUT] = windowProc2;
	closuresProc [Widget.POPUP_MENU] = windowProc2;
	closuresProc [Widget.PREEDIT_CHANGED] = windowProc2;
	closuresProc [Widget.REALIZE] = windowProc2;
	closuresProc [Widget.SELECT] = windowProc2;
	closuresProc [Widget.SELECTION_DONE] = windowProc2;
	closuresProc [Widget.SHOW] = windowProc2;
	closuresProc [Widget.START_INTERACTIVE_SEARCH] = windowProc2;
	closuresProc [Widget.STYLE_UPDATED] = windowProc2;
	closuresProc [Widget.VALUE_CHANGED] = windowProc2;
	closuresProc [Widget.UNMAP] = windowProc2;
	closuresProc [Widget.UNREALIZE] = windowProc2;
	closuresProc [Widget.BACKSPACE] = windowProc2;
	closuresProc [Widget.BACKSPACE_INVERSE] = windowProc2;
	closuresProc [Widget.COPY_CLIPBOARD] = windowProc2;
	closuresProc [Widget.COPY_CLIPBOARD_INVERSE] = windowProc2;
	closuresProc [Widget.CUT_CLIPBOARD] = windowProc2;
	closuresProc [Widget.CUT_CLIPBOARD_INVERSE] = windowProc2;
	closuresProc [Widget.PASTE_CLIPBOARD] = windowProc2;
	closuresProc [Widget.PASTE_CLIPBOARD_INVERSE] = windowProc2;

	windowCallback3 = new Callback (this, "windowProc", 3); //$NON-NLS-1$
	windowProc3 = windowCallback3.getAddress ();
	if (windowProc3 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	closuresProc [Widget.BUTTON_PRESS_EVENT] = windowProc3;
	closuresProc [Widget.BUTTON_PRESS_EVENT_INVERSE] = windowProc3;
	closuresProc [Widget.BUTTON_RELEASE_EVENT] = windowProc3;
	closuresProc [Widget.BUTTON_RELEASE_EVENT_INVERSE] = windowProc3;
	closuresProc [Widget.COMMIT] = windowProc3;
	closuresProc [Widget.CONFIGURE_EVENT] = windowProc3;
	closuresProc [Widget.DELETE_EVENT] = windowProc3;
	closuresProc [Widget.ENTER_NOTIFY_EVENT] = windowProc3;
	closuresProc [Widget.EVENT] = windowProc3;
	closuresProc [Widget.EVENT_AFTER] = windowProc3;
	closuresProc [Widget.EXPOSE_EVENT] = windowProc3;
	closuresProc [Widget.EXPOSE_EVENT_INVERSE] = windowProc3;
	closuresProc [Widget.FOCUS] = windowProc3;
	closuresProc [Widget.FOCUS_IN_EVENT] = windowProc3;
	closuresProc [Widget.FOCUS_OUT_EVENT] = windowProc3;
	closuresProc [Widget.KEY_PRESS_EVENT] = windowProc3;
	closuresProc [Widget.KEY_RELEASE_EVENT] = windowProc3;
	closuresProc [Widget.INPUT] = windowProc3;
	closuresProc [Widget.LEAVE_NOTIFY_EVENT] = windowProc3;
	closuresProc [Widget.MAP_EVENT] = windowProc3;
	closuresProc [Widget.MNEMONIC_ACTIVATE] = windowProc3;
	closuresProc [Widget.MOTION_NOTIFY_EVENT] = windowProc3;
	closuresProc [Widget.MOTION_NOTIFY_EVENT_INVERSE] = windowProc3;
	closuresProc [Widget.MOVE_FOCUS] = windowProc3;
	closuresProc [Widget.POPULATE_POPUP] = windowProc3;
	closuresProc [Widget.SCROLL_EVENT] = windowProc3;
	closuresProc [Widget.SHOW_HELP] = windowProc3;
	closuresProc [Widget.SIZE_ALLOCATE] = windowProc3;
	closuresProc [Widget.TOGGLED] = windowProc3;
	closuresProc [Widget.UNMAP_EVENT] = windowProc3;
	closuresProc [Widget.WINDOW_STATE_EVENT] = windowProc3;
	closuresProc [Widget.ROW_DELETED] = windowProc3;
	closuresProc [Widget.DIRECTION_CHANGED] = windowProc3;

	windowCallback4 = new Callback (this, "windowProc", 4); //$NON-NLS-1$
	windowProc4 = windowCallback4.getAddress ();
	if (windowProc4 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	closuresProc [Widget.DELETE_RANGE] = windowProc4;
	closuresProc [Widget.DELETE_TEXT] = windowProc4;
	closuresProc [Widget.ICON_RELEASE] = windowProc4;
	closuresProc [Widget.ROW_ACTIVATED] = windowProc4;
	closuresProc [Widget.SCROLL_CHILD] = windowProc4;
	closuresProc [Widget.STATUS_ICON_POPUP_MENU] = windowProc4;
	closuresProc [Widget.SWITCH_PAGE] = windowProc4;
	closuresProc [Widget.TEST_COLLAPSE_ROW] = windowProc4;
	closuresProc [Widget.TEST_EXPAND_ROW] = windowProc4;
	closuresProc [Widget.ROW_INSERTED] = windowProc4;
	closuresProc [Widget.ROW_HAS_CHILD_TOGGLED] = windowProc4;
	closuresProc [Widget.DELETE_FROM_CURSOR] = windowProc4;
	closuresProc [Widget.DELETE_FROM_CURSOR_INVERSE] = windowProc4;
	closuresProc [Widget.SIZE_ALLOCATE_GTK4] = windowProc4;

	windowCallback5 = new Callback (this, "windowProc", 5); //$NON-NLS-1$
	windowProc5 = windowCallback5.getAddress ();
	if (windowProc5 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	closuresProc [Widget.CHANGE_VALUE] = windowProc5;
	closuresProc [Widget.EXPAND_COLLAPSE_CURSOR_ROW] = windowProc5;
	closuresProc [Widget.INSERT_TEXT] = windowProc5;
	closuresProc [Widget.TEXT_BUFFER_INSERT_TEXT] = windowProc5;
	closuresProc [Widget.MOVE_CURSOR] = windowProc5;
	closuresProc [Widget.MOVE_CURSOR_INVERSE] = windowProc5;

	if (signalIds [Widget.POPPED_UP] != 0) {
		windowCallback6 = new Callback (this, "windowProc", 6); //$NON-NLS-1$
		windowProc6 = windowCallback6.getAddress ();
		if (windowProc6 == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		closuresProc [Widget.POPPED_UP] = windowProc6;
	}

	for (int i = 0; i < Widget.LAST_SIGNAL; i++) {
		if (closuresProc[i] != 0) {
			closures [i] = OS.g_cclosure_new(closuresProc [i], i, 0);
		}
		if (closures [i] != 0) {
			OS.g_closure_ref (closures [i]);
			OS.g_closure_sink (closures [i]);
		}
	}

	timerCallback = new Callback (this, "timerProc", 1); //$NON-NLS-1$
	timerProc = timerCallback.getAddress ();
	if (timerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	windowTimerCallback = new Callback (this, "windowTimerProc", 1); //$NON-NLS-1$
	windowTimerProc = windowTimerCallback.getAddress ();
	if (windowTimerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	mouseHoverCallback = new Callback (this, "mouseHoverProc", 1); //$NON-NLS-1$
	mouseHoverProc = mouseHoverCallback.getAddress ();
	if (mouseHoverProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	caretCallback = new Callback(this, "caretProc", 1); //$NON-NLS-1$
	caretProc = caretCallback.getAddress();
	if (caretProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	sizeAllocateCallback = new Callback(this, "sizeAllocateProc", 3); //$NON-NLS-1$
	sizeAllocateProc = sizeAllocateCallback.getAddress();
	if (sizeAllocateProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	sizeRequestCallback = new Callback(this, "sizeRequestProc", 3); //$NON-NLS-1$
	sizeRequestProc = sizeRequestCallback.getAddress();
	if (sizeRequestProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	shellMapCallback = new Callback(this, "shellMapProc", 3); //$NON-NLS-1$
	shellMapProc = shellMapCallback.getAddress();
	if (shellMapProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	shellMapProcClosure = OS.g_cclosure_new (shellMapProc, 0, 0);
	OS.g_closure_ref (shellMapProcClosure);

	cellDataCallback = new Callback (this, "cellDataProc", 5); //$NON-NLS-1$
	cellDataProc = cellDataCallback.getAddress ();
	if (cellDataProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	setDirectionCallback = new Callback (this, "setDirectionProc", 2); //$NON-NLS-1$
	setDirectionProc = setDirectionCallback.getAddress ();
	if (setDirectionProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	emissionProcCallback = new Callback (this, "emissionProc", 4); //$NON-NLS-1$
	emissionProc = emissionProcCallback.getAddress ();
	if (emissionProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	allChildrenCallback = new Callback (this, "allChildrenProc", 2); //$NON-NLS-1$
	allChildrenProc = allChildrenCallback.getAddress ();
	if (allChildrenProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	checkIfEventCallback = new Callback (this, "checkIfEventProc", 3); //$NON-NLS-1$
	checkIfEventProc = checkIfEventCallback.getAddress ();
	if (checkIfEventProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);

	idleCallback = new Callback (this, "idleProc", 1); //$NON-NLS-1$
	idleProc = idleCallback.getAddress ();
	if (idleProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
}

void initializeColorList() {
	colorList = new ArrayList<>();
	colorList.add("black");
	colorList.add("darkred");
	colorList.add("darkgreen");
	colorList.add("darkyellow");
	colorList.add("darkblue");
	colorList.add("darkmagenta");
	colorList.add("darkcyan");
	colorList.add("darkgray");
	colorList.add("gray");
	colorList.add("red");
	colorList.add("green");
	colorList.add("yellow");
	colorList.add("blue");
	colorList.add("magenta");
	colorList.add("cyan");
	colorList.add("white");

}

void initializeSubclasses () {
	if (!GTK.GTK4) {
		long pangoLayoutType = OS.PANGO_TYPE_LAYOUT ();
		long pangoLayoutClass = OS.g_type_class_ref (pangoLayoutType);
		pangoLayoutNewProc = OS.G_OBJECT_CLASS_CONSTRUCTOR (pangoLayoutClass);
		OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (pangoLayoutClass, OS.pangoLayoutNewProc_CALLBACK(pangoLayoutNewProc));
		OS.g_type_class_unref (pangoLayoutClass);

		long imContextType = GTK.GTK_TYPE_IM_MULTICONTEXT ();
		long imContextClass = OS.g_type_class_ref (imContextType);
		imContextNewProc = OS.G_OBJECT_CLASS_CONSTRUCTOR (imContextClass);
		OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (imContextClass, OS.imContextNewProc_CALLBACK(imContextNewProc));
		OS.g_type_class_unref (imContextClass);

		long pangoFontFamilyType = OS.PANGO_TYPE_FONT_FAMILY ();
		long pangoFontFamilyClass = OS.g_type_class_ref (pangoFontFamilyType);
		pangoFontFamilyNewProc = OS.G_OBJECT_CLASS_CONSTRUCTOR (pangoFontFamilyClass);
		OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (pangoFontFamilyClass, OS.pangoFontFamilyNewProc_CALLBACK(pangoFontFamilyNewProc));
		OS.g_type_class_unref (pangoFontFamilyClass);

		long pangoFontFaceType = OS.PANGO_TYPE_FONT_FACE ();
		long pangoFontFaceClass = OS.g_type_class_ref (pangoFontFaceType);
		pangoFontFaceNewProc = OS.G_OBJECT_CLASS_CONSTRUCTOR (pangoFontFaceClass);
		OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (pangoFontFaceClass, OS.pangoFontFaceNewProc_CALLBACK(pangoFontFaceNewProc));
		OS.g_type_class_unref (pangoFontFaceClass);

		if (!OS.IsWin32) { /* TODO [win32] replace unixprint */
			long printerOptionWidgetType = GTK.gtk_printer_option_widget_get_type();
			long printerOptionWidgetClass = OS.g_type_class_ref (printerOptionWidgetType);
			printerOptionWidgetNewProc = OS.G_OBJECT_CLASS_CONSTRUCTOR (printerOptionWidgetClass);
			OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (printerOptionWidgetClass, OS.printerOptionWidgetNewProc_CALLBACK(printerOptionWidgetNewProc));
			OS.g_type_class_unref (printerOptionWidgetClass);
		}
	}
}

void initializeSystemSettings () {
	OS.g_signal_connect (shellHandle, OS.style_updated, signalProc, STYLE_UPDATED);

	/*
	* Feature in GTK.  Despite the fact that the
	* gtk-entry-select-on-focus property is a global
	* setting, it is initialized when the GtkEntry
	* is initialized.  This means that it cannot be
	* accessed before a GtkEntry is created.  The
	* fix is to for the initializaion by creating
	* a temporary GtkEntry.
	*/
	long entry = GTK.gtk_entry_new ();
	GTK.gtk_widget_destroy (entry);
	int [] buffer2 = new int [1];
	long settings = GTK.gtk_settings_get_default ();
	OS.g_object_get (settings, GTK.gtk_entry_select_on_focus, buffer2, 0);
	entrySelectOnFocus = buffer2 [0] != 0;
}

void initializeWidgetTable () {
	indexTable = new int [GROW_SIZE];
	widgetTable = new Widget [GROW_SIZE];
	for (int i=0; i<GROW_SIZE-1; i++) indexTable [i] = i + 1;
	indexTable [GROW_SIZE - 1] = -1;
}

void initializeWindowManager () {
	/* Get the window manager name */
	windowManager = ""; //$NON-NLS-1$
	if (OS.isX11()) {
		long screen = GDK.gdk_screen_get_default ();
		if (screen != 0) {
			long ptr2 = GDK.gdk_x11_screen_get_window_manager_name (screen);
			if (ptr2 != 0) {
				int length = C.strlen (ptr2);
				if (length > 0) {
					byte [] buffer2 = new byte [length];
					C.memmove (buffer2, ptr2, length);
					windowManager = new String (Converter.mbcsToWcs (buffer2));
				}
			}
		}
	}
}

void initializeSessionManager() {
	sessionManagerDBus = new SessionManagerDBus();
	sessionManagerListener = new SessionManagerListener(this);
	sessionManagerDBus.addListener(sessionManagerListener);
}

void releaseSessionManager() {
	if (sessionManagerDBus != null) {
		sessionManagerDBus.dispose();
		sessionManagerDBus = null;
	}

	sessionManagerListener = null;
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
@Override
public void internal_dispose_GC (long hDC, GCData data) {
	Cairo.cairo_destroy (hDC);
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
@Override
public long internal_new_GC (GCData data) {
	if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
	long gc = 0;
	long root = 0;
	if (GTK.GTK4) {
		long surface = Cairo.cairo_image_surface_create(Cairo.CAIRO_FORMAT_A8, data.width, data.height);
		gc = Cairo.cairo_create(surface);
	} else {
		root = GDK.gdk_get_default_root_window();
		if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0)) {
			long surface = GDK.gdk_window_create_similar_surface(root, Cairo.CAIRO_CONTENT_COLOR_ALPHA, data.width, data.height);
			gc = Cairo.cairo_create(surface);
		} else {
			gc = GDK.gdk_cairo_create(root);
		}
	}
	if (gc == 0) error (SWT.ERROR_NO_HANDLES);
	//TODO how gdk_gc_set_subwindow is done in cairo?
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = this;
		data.drawable = root;
		data.backgroundRGBA = getSystemColor (SWT.COLOR_WHITE).handle;
		data.foregroundRGBA = getSystemColor (SWT.COLOR_BLACK).handle;
		data.font = getSystemFont ();
	}
	return gc;
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
	Point point = new Point (x, y);
	if (from == to) return point;
	if (from != null) {
		Point origin = DPIUtil.autoScaleDown (GTK.GTK4 ? from.getSurfaceOrigin() : from.getWindowOrigin ());
		if ((from.style & SWT.MIRRORED) != 0) point.x = DPIUtil.autoScaleDown (from.getClientWidth ()) - point.x;
		point.x += origin.x;
		point.y += origin.y;
	}
	if (to != null) {
		Point origin = DPIUtil.autoScaleDown (GTK.GTK4 ? to.getSurfaceOrigin() : to.getWindowOrigin ());
		point.x -= origin.x;
		point.y -= origin.y;
		if ((to.style & SWT.MIRRORED) != 0) point.x = DPIUtil.autoScaleDown (to.getClientWidth ()) - point.x;
	}
	return point;
}

Point mapInPixels (Control from, Control to, int x, int y) {
	checkDevice ();
	if (from != null && from.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	Point point = new Point (x, y);
	if (from == to) return point;
	if (from != null) {
		Point origin = GTK.GTK4 ? from.getSurfaceOrigin() : from.getWindowOrigin ();
		if ((from.style & SWT.MIRRORED) != 0) point.x = from.getClientWidth () - point.x;
		point.x += origin.x;
		point.y += origin.y;
	}
	if (to != null) {
		Point origin = GTK.GTK4 ? to.getSurfaceOrigin() : to.getWindowOrigin ();
		point.x -= origin.x;
		point.y -= origin.y;
		if ((to.style & SWT.MIRRORED) != 0) point.x = to.getClientWidth () - point.x;
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
	checkDevice();
	if (rectangle == null) error (SWT.ERROR_NULL_ARGUMENT);
	return map (from, to, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
}

Rectangle mapInPixels (Control from, Control to, Rectangle rectangle) {
	checkDevice();
	if (rectangle == null) error (SWT.ERROR_NULL_ARGUMENT);
	return mapInPixels (from, to, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
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
	checkDevice();
	if (from != null && from.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	Rectangle rect = new Rectangle (x, y, width, height);
	if (from == to) return rect;
	boolean fromRTL = false, toRTL = false;
	if (from != null) {
		Point origin = DPIUtil.autoScaleDown (GTK.GTK4 ? from.getSurfaceOrigin () : from.getWindowOrigin ());
		if (fromRTL = (from.style & SWT.MIRRORED) != 0) rect.x = DPIUtil.autoScaleDown (from.getClientWidth ()) - rect.x;
		rect.x += origin.x;
		rect.y += origin.y;
	}
	if (to != null) {
		Point origin = DPIUtil.autoScaleDown (GTK.GTK4 ? to.getSurfaceOrigin() : to.getWindowOrigin ());
		rect.x -= origin.x;
		rect.y -= origin.y;
		if (toRTL = (to.style & SWT.MIRRORED) != 0) rect.x = DPIUtil.autoScaleDown (to.getClientWidth ()) - rect.x;
	}

	if (fromRTL != toRTL) rect.x -= rect.width;
	return rect;
}

Rectangle mapInPixels (Control from, Control to, int x, int y, int width, int height) {
	checkDevice();
	if (from != null && from.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	Rectangle rect = new Rectangle (x, y, width, height);
	if (from == to) return rect;
	boolean fromRTL = false, toRTL = false;
	if (from != null) {
		Point origin = GTK.GTK4 ? from.getSurfaceOrigin() : from.getWindowOrigin ();
		if (fromRTL = (from.style & SWT.MIRRORED) != 0) rect.x = from.getClientWidth () - rect.x;
		rect.x += origin.x;
		rect.y += origin.y;
	}
	if (to != null) {
		Point origin = GTK.GTK4 ? to.getSurfaceOrigin() : to.getWindowOrigin ();
		rect.x -= origin.x;
		rect.y -= origin.y;
		if (toRTL = (to.style & SWT.MIRRORED) != 0) rect.x = to.getClientWidth () - rect.x;
	}

	if (fromRTL != toRTL) rect.x -= rect.width;
	return rect;
}

long mouseHoverProc (long handle) {
	Widget widget = getWidget (handle);
	// null the GSource id as our implementation always returns 0 so the hover is
	// hidden and this leads to the GSource to be destroyed
	mouseHoverId = 0;
	if (widget == null) {
		return 0;
	}
	return widget.hoverProc (handle);
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
 * <li>(in) type KeyDown or KeyUp</li></ul>
 * <p> Either one of:</p>
 * <ul><li>(in) character a character that corresponds to a keyboard key</li>
 * <li>(in) keyCode the key code of the key that was typed,
 *          as defined by the key code constants in class <code>SWT</code></li></ul>
 * <p> Optional (on some platforms): </p>
 * <ul><li>(in) stateMask the state of the keyboard modifier,
 * 			as defined by the key code constants in class <code>SWT</code>
 * </li>
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
 * <li>(in) type MouseMove</li>
 * <li>(in) x the x coordinate to move the mouse pointer to in screen coordinates</li>
 * <li>(in) y the y coordinate to move the mouse pointer to in screen coordinates</li>
 * </ul>
 * <p>MouseWheel</p>
 * <p>The following fields in the <code>Event</code> apply:</p>
 * <ul>
 * <li>(in) type MouseWheel</li>
 * <li>(in) detail either SWT.SCROLL_LINE or SWT.SCROLL_PAGE</li>
 * <li>(in) count the number of lines or pages to scroll</li>
 * </ul>
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
	/*
	* Get the operating system lock before synchronizing on the device
	* lock so that the device lock will not be held should another
	* thread already be in the operating system.  This avoids deadlock
	* should the other thread need the device lock.
	*/
	Lock lock = Platform.lock;
	lock.lock();
	try {
		synchronized (Device.class) {
			if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
			if (event == null) error (SWT.ERROR_NULL_ARGUMENT);

			int type = event.type;

			if (type == SWT.MouseMove) {
				Rectangle loc = DPIUtil.autoScaleUp(event.getBounds());
				setCursorLocationInPixels(new Point(loc.x, loc.y));
				return true;
			}

			long gdkDisplay = GDK.gdk_display_get_default();
			long gdkSeat = GDK.gdk_display_get_default_seat(gdkDisplay);
			long gdkKeyboardDevice = GDK.gdk_seat_get_keyboard(gdkSeat);
			long gdkPointerDevice = GDK.gdk_seat_get_pointer(gdkSeat);
			long gdkWindow = GDK.gdk_get_default_root_window();
			long window_list = GDK.gdk_window_get_children(gdkWindow);
			if (window_list != 0) {
				long windows = window_list;
				while (windows != 0) {
					long curr_window = OS.g_list_data(windows);
					int state = GDK.gdk_window_get_state(curr_window);
					if ((state & GDK.GDK_WINDOW_STATE_FOCUSED) != 0) {
						gdkWindow = curr_window;
						OS.g_object_ref(gdkWindow);
						break;
					}
					windows = OS.g_list_next(windows);
				}
				OS.g_list_free(window_list);
			}
			if (gdkWindow == 0) return false;
			long eventPtr = 0;

			switch (type) {
				case SWT.KeyDown:
				case SWT.KeyUp:
					/* Translate the SWT Event fields to GDK fields
					 * We need hardware_keycode, GdkModifierType, group to get keyval
					 * 1) event.character -> hardware_keycode
					 * 2) event.stateMask -> GdkModifierType(state)
					 * 3) event.keyCode -> hardware_keycode, GdkModifierType(state) (i.e. SWT.SHIFT)
					 */
					int state = type == SWT.KeyDown ? GDK.GDK_KEY_PRESS_MASK : GDK.GDK_KEY_RELEASE_MASK;
					if (cachedModifierState != 0) {
						state |= cachedModifierState;
					}
					int is_modifier = 1;
					int modifier = event.stateMask != 0 ? event.stateMask : event.keyCode;
					switch (modifier) {
						case SWT.SHIFT: state |= GDK.GDK_SHIFT_MASK; break;
						case SWT.ALT: state |= GDK.GDK_MOD1_MASK; break;
						case SWT.CONTROL: state |= GDK.GDK_CONTROL_MASK; break;
						case SWT.ALT_GR: state |= GDK.GDK_MOD5_MASK; break;
						default:
							is_modifier = cachedModifierState == 0 ? 0 : 1;
					}

					// Save the modifier state pressed until it is released
					if (is_modifier == 1 && type == SWT.KeyDown) {
						cachedModifierState = state & (~GDK.GDK_KEY_PRESS_MASK | ~GDK.GDK_KEY_RELEASE_MASK);
					} else {
						cachedModifierState = 0;
					}

					long gdkKeymap = GDK.gdk_keymap_get_for_display(gdkDisplay);
					int hardware_keycode = 0;
					int raw_keyval = untranslateKey(event.keyCode);
					if (raw_keyval == 0) raw_keyval = event.character;

					long [] keys_list = new long [1];
					int [] n_keys = new int [1];
					int [] keyval = new int [1], effective_group = new int [1], level = new int [1], consumed_modifiers = new int[1];
					int final_keyval = raw_keyval;

					if (GDK.gdk_keymap_get_entries_for_keyval(gdkKeymap, raw_keyval, keys_list, n_keys)) {
						GdkKeymapKey key_entry = new GdkKeymapKey ();
						if (n_keys[0] > 0) {
							OS.memmove(key_entry, keys_list[0], GdkKeymapKey.sizeof);
							hardware_keycode = key_entry.keycode;
						}
						OS.g_free(keys_list[0]);

						GDK.gdk_keymap_translate_keyboard_state(gdkKeymap, hardware_keycode, state, 0, keyval, effective_group, level, consumed_modifiers);
						if (is_modifier == 1) final_keyval = keyval[0];
					}

					/* Construct GdkEventKey */
					eventPtr = GDK.gdk_event_new(type == SWT.KeyDown ? GDK.GDK_KEY_PRESS : GDK.GDK_KEY_RELEASE);
					GdkEventKey newKeyEvent = new GdkEventKey ();
					newKeyEvent.type = type == SWT.KeyDown ? GDK.GDK_KEY_PRESS : GDK.GDK_KEY_RELEASE;
					newKeyEvent.window = gdkWindow;
					newKeyEvent.send_event = 1;
					newKeyEvent.time = GDK.GDK_CURRENT_TIME;
					newKeyEvent.keyval = final_keyval;
					newKeyEvent.state = state;
					newKeyEvent.hardware_keycode = (short) hardware_keycode;
					newKeyEvent.group = (byte) effective_group[0];
					newKeyEvent.is_modifier = is_modifier;

					OS.memmove(eventPtr, newKeyEvent, GdkEventKey.sizeof);
					GDK.gdk_event_set_device (eventPtr, gdkKeyboardDevice);
					if (GTK.GTK4) {
						long display = GDK.gdk_display_get_default();
						GDK.gdk_display_put_event(display, eventPtr);
					} else {
						GDK.gdk_event_put (eventPtr);
					}
					if (GTK.GTK4) {
						OS.g_object_unref(eventPtr);
					} else {
						GDK.gdk_event_free (eventPtr);
					}
					return true;
				case SWT.MouseDown:
				case SWT.MouseUp:
					int[] x = new int[1], y = new int[1], mask = new int[1];
					gdkWindow = GDK.gdk_device_get_window_at_position(gdkPointerDevice, x, y);
					// Under Wayland or some window managers, gdkWindow is not known to GDK and null is returned,
					// cannot post mouse events as it will lead to crash
					if (gdkWindow == 0) return false;
					OS.g_object_ref(gdkWindow);

					/* Construct GdkEventButton */
					eventPtr = GDK.gdk_event_new(type == SWT.MouseDown ? GDK.GDK_BUTTON_PRESS : GDK.GDK_BUTTON_RELEASE);
					GdkEventButton newButtonEvent = new GdkEventButton ();
					newButtonEvent.type = type == SWT.MouseDown ? GDK.GDK_BUTTON_PRESS : GDK.GDK_BUTTON_RELEASE;
					newButtonEvent.window = gdkWindow;
					newButtonEvent.send_event = 1;
					newButtonEvent.time = GDK.GDK_CURRENT_TIME;
					newButtonEvent.x = x[0];
					newButtonEvent.y = y[0];
					newButtonEvent.state = mask[0];
					newButtonEvent.button = event.button;
					newButtonEvent.device = gdkPointerDevice;

					OS.memmove(eventPtr, newButtonEvent, GdkEventButton.sizeof);
					GDK.gdk_event_set_device(eventPtr, gdkPointerDevice);

					GDK.gdk_event_put(eventPtr);
					if (GTK.GTK4) {
						OS.g_object_unref(eventPtr);
					} else {
						GDK.gdk_event_free (eventPtr);
					}
					return true;
			}
			return false;
		}
	} finally {
		lock.unlock();
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

void putGdkEvents () {
	if (gdkEventCount != 0) {
		for (int i = 0; i < gdkEventCount; i++) {
			long event = gdkEvents [i];
			Widget widget = gdkEventWidgets [i];
			if (widget == null || !widget.isDisposed ()) {
				if (GTK.GTK4) {
					long display = GDK.gdk_display_get_default();
					GDK.gdk_display_put_event(display, event);
				} else {
					GDK.gdk_event_put (event);
				}
			}
			if (GTK.GTK4) {
				OS.g_object_unref (event);
			} else {
				GDK.gdk_event_free (event);
			}
			gdkEvents [i] = 0;
			gdkEventWidgets [i] = null;
		}
		gdkEventCount = 0;
	}
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
	boolean events = false;
	events |= runSettings ();
	events |= runPopups ();
	/*
	* This call to gdk_threads_leave() is a temporary work around
	* to avoid deadlocks when gdk_threads_init() is called by native
	* code outside of SWT (i.e AWT, etc). It ensures that the current
	* thread leaves the GTK lock before calling the function below.
	*/
	if (!GTK.GTK4) GDK.gdk_threads_leave();
	events |= OS.g_main_context_iteration (0, false);
	if (events) {
		runDeferredEvents ();
		return true;
	}
	return isDisposed () || runAsyncMessages (false);
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
@Override
protected void release () {
	sendEvent (SWT.Dispose, new Event ());
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ())  shell.dispose ();
	}
	if (tray != null) tray.dispose ();
	tray = null;
	while (readAndDispatch ()) {}
	if (disposeList != null) {
		for (int i=0; i<disposeList.length; i++) {
			Runnable next = disposeList [i];
			if (next != null) {
				try {
					next.run ();
				} catch (RuntimeException exception) {
					runtimeExceptionHandler.accept (exception);
				} catch (Error error) {
					errorHandler.accept (error);
				}
			}
		}
	}
	disposeList = null;
	synchronizer.releaseSynchronizer ();
	synchronizer = null;
	releaseSessionManager ();
	releaseDisplay ();
	super.release ();
}

void releaseDisplay () {
	windowCallback2.dispose ();  windowCallback2 = null;
	windowCallback3.dispose ();  windowCallback3 = null;
	windowCallback4.dispose ();  windowCallback4 = null;
	windowCallback5.dispose ();  windowCallback5 = null;
	if (windowCallback6 != null) {
		windowCallback6.dispose ();  windowCallback6 = null;
	}
	windowProc2 = windowProc3 = windowProc4 = windowProc5 = windowProc6 = 0;

	if (GTK.GTK4) {
		keyPressReleaseCallback.dispose();
		keyPressReleaseProc = 0;
		focusCallback.dispose();
		focusProc = 0;
		enterMotionScrollCallback.dispose();
		enterMotionScrollProc = 0;
		leaveCallback.dispose();
		leaveProc = 0;
		gesturePressReleaseCallback.dispose();
		gesturePressReleaseProc = 0;
		notifyStateCallback.dispose();
		notifyStateProc = 0;
	}

	/* Dispose checkIfEvent callback */
	checkIfEventCallback.dispose(); checkIfEventCallback = null;
	checkIfEventProc = 0;

	/* Dispose preedit window */
	if (preeditWindow != 0) GTK.gtk_widget_destroy (preeditWindow);
	imControl = null;

	/* Dispose the tooltip map callback */
	sizeAllocateCallback.dispose (); sizeAllocateCallback = null;
	sizeAllocateProc = 0;
	sizeRequestCallback.dispose (); sizeRequestCallback = null;
	sizeRequestProc = 0;

	/* Dispose the shell map callback */
	shellMapCallback.dispose (); shellMapCallback = null;
	shellMapProc = 0;

	/* Dispose the run async messages callback */
	idleCallback.dispose (); idleCallback = null;
	idleProc = 0;
	if (idleHandle != 0) OS.g_source_remove (idleHandle);
	idleHandle = 0;

	/* Dispose GtkTreeView callbacks */
	cellDataCallback.dispose (); cellDataCallback = null;
	cellDataProc = 0;

	/* Dispose the set direction callback */
	setDirectionCallback.dispose (); setDirectionCallback = null;
	setDirectionProc = 0;

	/* Dispose the emission proc callback */
	emissionProcCallback.dispose (); emissionProcCallback = null;
	emissionProc = 0;

	/* Dispose the set direction callback */
	allChildrenCallback.dispose (); allChildrenCallback = null;
	allChildrenProc = 0;

	/* Dispose the caret callback */
	if (caretId != 0) OS.g_source_remove (caretId);
	caretId = 0;
	caretProc = 0;
	caretCallback.dispose ();
	caretCallback = null;

	/* Release closures */
	for (int i = 0; i < Widget.LAST_SIGNAL; i++) {
		if (closures [i] != 0) OS.g_closure_unref (closures [i]);
	}
	if (shellMapProcClosure != 0) OS.g_closure_unref (shellMapProcClosure);

	/* Dispose the timer callback */
	if (timerIds != null) {
		for (int i=0; i<timerIds.length; i++) {
			if (timerIds [i] != 0) OS.g_source_remove (timerIds [i]);
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
	if (mouseHoverId != 0) OS.g_source_remove (mouseHoverId);
	mouseHoverId = 0;
	mouseHoverHandle = mouseHoverProc = 0;
	mouseHoverCallback.dispose ();
	mouseHoverCallback = null;

	/* Dispose the System Images */
	if (errorImage != null) errorImage.dispose();
	if (infoImage != null) infoImage.dispose();
	if (questionImage != null) questionImage.dispose();
	if (warningImage != null) warningImage.dispose();
	errorImage = infoImage = questionImage = warningImage = null;

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

	/* Release the System Colors */
	COLOR_WIDGET_DARK_SHADOW_RGBA = COLOR_WIDGET_NORMAL_SHADOW_RGBA = COLOR_WIDGET_LIGHT_SHADOW_RGBA =
	COLOR_WIDGET_HIGHLIGHT_SHADOW_RGBA = COLOR_WIDGET_BACKGROUND_RGBA = COLOR_WIDGET_BORDER_RGBA =
	COLOR_LIST_FOREGROUND_RGBA = COLOR_LIST_BACKGROUND_RGBA = COLOR_LIST_SELECTION_RGBA = COLOR_LIST_SELECTION_TEXT_RGBA =
	COLOR_LIST_SELECTION_INACTIVE_RGBA = COLOR_LIST_SELECTION_TEXT_INACTIVE_RGBA =
	COLOR_WIDGET_FOREGROUND_RGBA = COLOR_TITLE_FOREGROUND_RGBA = COLOR_TITLE_BACKGROUND_RGBA = COLOR_TITLE_BACKGROUND_GRADIENT_RGBA =
	COLOR_TITLE_INACTIVE_FOREGROUND_RGBA = COLOR_TITLE_INACTIVE_BACKGROUND_RGBA = COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT_RGBA =
	COLOR_INFO_BACKGROUND_RGBA = COLOR_INFO_FOREGROUND_RGBA = COLOR_LINK_FOREGROUND_RGBA = null;

	/* Dispose the event callback */
	GDK.gdk_event_handler_set (0, 0, 0);
	eventCallback.dispose ();  eventCallback = null;

	/* Dispose the hidden shell */
	if (shellHandle != 0) GTK.gtk_widget_destroy (shellHandle);
	shellHandle = 0;

	/* Dispose the settings callback */
	signalCallback.dispose(); signalCallback = null;
	signalProc = 0;

	/* Dispose the "keys-changed" callback */
	keysChangedCallback.dispose(); keysChangedCallback = null;
	keysChangedProc = 0;

	/* Dispose subclass */
	long pangoLayoutType = OS.PANGO_TYPE_LAYOUT ();
	long pangoLayoutClass = OS.g_type_class_ref (pangoLayoutType);
	OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (pangoLayoutClass, pangoLayoutNewProc);
	OS.g_type_class_unref (pangoLayoutClass);
	pangoLayoutNewProc = 0;
	long imContextType = GTK.GTK_TYPE_IM_MULTICONTEXT ();
	long imContextClass = OS.g_type_class_ref (imContextType);
	OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (imContextClass, imContextNewProc);
	OS.g_type_class_unref (imContextClass);
	imContextNewProc = 0;
	long pangoFontFamilyType = OS.PANGO_TYPE_FONT_FAMILY ();
	long pangoFontFamilyClass = OS.g_type_class_ref (pangoFontFamilyType);
	OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (pangoFontFamilyClass, pangoFontFamilyNewProc);
	OS.g_type_class_unref (pangoFontFamilyClass);
	pangoFontFamilyNewProc = 0;
	long pangoFontFaceType = OS.PANGO_TYPE_FONT_FACE ();
	long pangoFontFaceClass = OS.g_type_class_ref (pangoFontFaceType);
	OS.G_OBJECT_CLASS_SET_CONSTRUCTOR (pangoFontFaceClass, pangoFontFaceNewProc);
	OS.g_type_class_unref (pangoFontFaceClass);
	pangoFontFaceNewProc = 0;

	/* Release the sleep resources */
	max_priority = timeout = null;
	if (fds != 0) OS.g_free (fds);
	fds = 0;

	/* Release references */
	popups = null;
	thread = null;
	lastWidget = activeShell = null;
	flushData = closures = null;
	indexTable = signalIds = null;
	widgetTable = modalShells = null;
	data = null;
	values = keys = null;
	windowManager = null;
	eventTable = filterTable = null;
	modalDialog = null;
	flushRect = null;
	exposeEvent = null;
	idleLock = null;

	/* Save window trim caches */
	String userHome = System.getProperty ("user.home"); //$NON-NLS-1$
	if (userHome != null) {
		File dir = new File (userHome, ".swt");
		if ((dir.exists () && dir.isDirectory ()) || dir.mkdirs ()) {
			File file = new File (dir, "trims.prefs");
			Properties props = new Properties ();
			StringBuilder buf = new StringBuilder ();
			for (int w : trimWidths) buf.append (w).append (' ');
			props.put ("trimWidths", buf.toString ());
			buf = new StringBuilder();
			for (int h : trimHeights) buf.append (h).append (' ');
			props.put ("trimHeights", buf.toString ());
			try (FileOutputStream fos = new FileOutputStream (file)){
				props.store (fos, null);
			} catch (IOException e) {
				// skip saving trim caches
			}
		}
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

long removeGdkEvent () {
	if (gdkEventCount == 0) return 0;
	long event = gdkEvents [0];
	--gdkEventCount;
	System.arraycopy (gdkEvents, 1, gdkEvents, 0, gdkEventCount);
	System.arraycopy (gdkEventWidgets, 1, gdkEventWidgets, 0, gdkEventCount);
	gdkEvents [gdkEventCount] = 0;
	gdkEventWidgets [gdkEventCount] = null;
	if (gdkEventCount == 0) {
		gdkEvents = null;
		gdkEventWidgets = null;
	}
	return event;
}

void removeIdleProc () {
	synchronized (idleLock) {
		if (idleHandle != 0) OS.g_source_remove (idleHandle);
		idleNeeded = false;
		idleHandle = 0;
	}
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

void removeMouseHoverTimeout (long handle) {
	if (handle != mouseHoverHandle) return;
	if (mouseHoverId != 0) OS.g_source_remove (mouseHoverId);
	mouseHoverId = 0;
	mouseHoverHandle = 0;
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

Widget removeWidget (long handle) {
	if (handle == 0) return null;
	lastWidget = null;
	Widget widget = null;
	int index;
	long data = OS.g_object_get_qdata (handle, SWT_OBJECT_INDEX) - 1;
	if(data < 0 || data > Integer.MAX_VALUE) {
		SWT.error(SWT.ERROR_INVALID_RETURN_VALUE, null, ". g_object_get_qdata returned unexpected index value" +  debugInfoForIndex(data));
	}
	index = (int)data;
	if (0 <= index && index < widgetTable.length) {
		widget = widgetTable [index];
		widgetTable [index] = null;
		indexTable [index] = freeSlot;
		freeSlot = index;
		OS.g_object_set_qdata (handle, SWT_OBJECT_INDEX, 0);

		if(widget == null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, ". Widget already released" + debugInfoForIndex(index));
		}
	} else {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, ". Invalid index for handle " + handle + debugInfoForIndex(index));
	}
	return widget;
}

String debugInfoForIndex(long index) {
	String s = ", index: " + index;
	int idx = (int) index;
	if (idx >= 0 && idx < widgetTable.length) {
		s += ", current value at: " + widgetTable[idx];
	}
	s += dumpWidgetTableInfo();
	return s;
}

String dumpWidgetTableInfo() {
	StringBuilder sb = new StringBuilder(", table size: ");
	sb.append(widgetTable.length);
	IdentityHashMap<Widget, Collection<Integer>> disposed = new IdentityHashMap<>();
	for (int i = 0; i < widgetTable.length; i++) {
		Widget w = widgetTable[i];
		if (w != null && w.isDisposed()) {
			Collection<Integer> list = disposed.get(w);
			if (list == null) {
				list = new ArrayList<>();
				disposed.put(w, list);
			}
			list.add(Integer.valueOf(i));
		}
	}
	if (!disposed.isEmpty()) {
		sb.append(", leaked elements:");
		Set<Entry<Widget,Collection<Integer>>> set = disposed.entrySet();
		for (Entry<Widget, Collection<Integer>> entry : set) {
			sb.append(" ").append(entry.getKey()).append(" at ").append(entry.getValue()).append(",");
		}
	}
	return sb.toString();
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
		update ();
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

boolean runSettings () {
	if (!runSettings) return false;
	runSettings = false;
	saveResources ();
	initializeSystemColors ();
	sendEvent (SWT.Settings, null);
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			shell.fixStyle ();
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
 * Accessibility tools could ask for the application
 * name. On Windows, if the application name is set
 * to any value other than "SWT" (case insensitive),
 * it is used to set the application user model ID
 * which is used by the OS for taskbar grouping.
 * @see <a href="http://msdn.microsoft.com/en-us/library/windows/desktop/dd378459%28v=vs.85%29.aspx#HOW">AppUserModelID (Windows)</a>
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
	setCursorLocation(new Point (x, y));
}

void setCursorLocationInPixels (Point location) {
	long gdkDisplay = GDK.gdk_display_get_default();
	long gdkPointer = GDK.gdk_get_pointer(gdkDisplay);
	if (GTK.GTK4) {
		GDK.gdk_device_warp(gdkPointer, location.x, location.y);
	} else {
		long gdkScreen = GDK.gdk_screen_get_default();
		GDK.gdk_device_warp(gdkPointer, gdkScreen, location.x, location.y);
	}
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
	point = DPIUtil.autoScaleUp(point);
	setCursorLocationInPixels(point);
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

	if (key.equals (DISPATCH_EVENT_KEY)) {
		if (value == null || value instanceof int []) {
			dispatchEvents = (int []) value;
			if (value == null) putGdkEvents ();
			return;
		}
	}
	if (key.equals (SET_MODAL_DIALOG)) {
		setModalDialog ((Dialog) value);
		return;
	}
	if (key.equals (ADD_WIDGET_KEY)) {
		Object [] data = (Object []) value;
		long handle = ((LONG) data [0]).value;
		Widget widget = (Widget) data [1];
		if (widget != null) {
			addWidget (handle, widget);
		} else {
			removeWidget (handle);
		}
	}
	if (key.equals (ADD_IDLE_PROC_KEY)) {
		addIdleProc ();
		return;
	}
	if (key.equals (REMOVE_IDLE_PROC_KEY)) {
		removeIdleProc ();
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

long setDirectionProc (long widget, long direction) {
	GTK.gtk_widget_set_direction (widget, (int)direction);
	if (GTK.GTK_IS_MENU_ITEM (widget)) {
		long submenu = GTK.gtk_menu_item_get_submenu (widget);
		if (submenu != 0) {
			GTK.gtk_widget_set_direction (submenu, (int)direction);
			GTK.gtk_container_forall (submenu, setDirectionProc, direction);
		}
	}
	if (GTK.GTK_IS_CONTAINER (widget)) {
		GTK.gtk_container_forall (widget, setDirectionProc, direction);
	}
	return 0;
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
		oldSynchronizer.moveAllEventsTo(synchronizer);
	}
}

/**
 * Sets a callback that will be invoked whenever an exception is thrown by a listener or external
 * callback function. The application may use this to set a global exception handling policy:
 * the most common policies are either to log and discard the exception or to re-throw the
 * exception.
 * <p>
 * The default SWT error handling policy is to rethrow exceptions.
 *
 * @param runtimeExceptionHandler new exception handler to be registered.
 * @since 3.106
 */
public final void setRuntimeExceptionHandler (Consumer<RuntimeException> runtimeExceptionHandler) {
	checkDevice();
	this.runtimeExceptionHandler = Objects.requireNonNull (runtimeExceptionHandler);
}

/**
 * Returns the current exception handler. It will receive all exceptions thrown by listeners
 * and external callbacks in this display. If code wishes to temporarily replace the exception
 * handler (for example, during a unit test), it is common practice to invoke this method prior
 * to replacing the exception handler so that the old handler may be restored afterward.
 *
 * @return the current exception handler. Never <code>null</code>.
 * @since 3.106
 */
public final Consumer<RuntimeException> getRuntimeExceptionHandler () {
	return runtimeExceptionHandler;
}

/**
 * Sets a callback that will be invoked whenever an error is thrown by a listener or external
 * callback function. The application may use this to set a global exception handling policy:
 * the most common policies are either to log and discard the exception or to re-throw the
 * exception.
 * <p>
 * The default SWT error handling policy is to rethrow exceptions.
 *
 * @param errorHandler new error handler to be registered.
 * @since 3.106
 */
public final void setErrorHandler (Consumer<Error> errorHandler) {
	checkDevice();
	this.errorHandler = Objects.requireNonNull (errorHandler);
}

/**
 * Returns the current exception handler. It will receive all errors thrown by listeners
 * and external callbacks in this display. If code wishes to temporarily replace the error
 * handler (for example, during a unit test), it is common practice to invoke this method prior
 * to replacing the error handler so that the old handler may be restored afterward.
 *
 * @return the current error handler. Never <code>null</code>.
 * @since 3.106
 */
public final Consumer<Error> getErrorHandler () {
	return errorHandler;
}

void showIMWindow (Control control) {
	imControl = control;
	if (preeditWindow == 0) {
		preeditWindow = GTK.gtk_window_new (GTK.GTK_WINDOW_POPUP);
		if (preeditWindow == 0) error (SWT.ERROR_NO_HANDLES);
		preeditLabel = GTK.gtk_label_new (null);
		if (preeditLabel == 0) error (SWT.ERROR_NO_HANDLES);
		GTK.gtk_container_add (preeditWindow, preeditLabel);
		GTK.gtk_widget_show (preeditLabel);
	}
	long [] preeditString = new long [1];
	long [] pangoAttrs = new long [1];
	long imHandle = control.imHandle ();
	GTK.gtk_im_context_get_preedit_string (imHandle, preeditString, pangoAttrs, null);
	if (preeditString [0] != 0 && C.strlen (preeditString [0]) > 0) {
		Control widget = control.findBackgroundControl ();
		if (widget == null) widget = control;
		widget.setBackgroundGdkRGBA (preeditWindow, control.getBackgroundGdkRGBA());
		widget.setForegroundGdkRGBA (preeditLabel, control.getForegroundGdkRGBA());
		widget.setFontDescription (preeditLabel, control.getFontDescription ());
		if (pangoAttrs [0] != 0) GTK.gtk_label_set_attributes (preeditLabel, pangoAttrs[0]);
		GTK.gtk_label_set_text (preeditLabel, preeditString [0]);
		Point point = control.toDisplayInPixels (control.getIMCaretPos ());
		GTK.gtk_window_move (preeditWindow, point.x, point.y);
		GtkRequisition requisition = new GtkRequisition ();
		GTK.gtk_widget_get_preferred_size (preeditLabel, requisition, null);
		GTK.gtk_window_resize (preeditWindow, requisition.width, requisition.height);
		GTK.gtk_widget_show (preeditWindow);
	} else {
		GTK.gtk_widget_hide (preeditWindow);
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #wake
 */
public boolean sleep () {
	checkDevice ();
	if (gdkEventCount == 0) {
		gdkEvents = null;
		gdkEventWidgets = null;
	}
	if (settingsChanged) {
		settingsChanged = false;
		runSettings = true;
		return false;
	}
	if (getMessageCount () != 0) return true;
	sendPreExternalEventDispatchEvent ();
	if (fds == 0) {
		allocated_nfds = 2;
		fds = OS.g_malloc (OS.GPollFD_sizeof () * allocated_nfds);
	}
	max_priority [0] = timeout [0] = 0;
	long context = OS.g_main_context_default ();
	boolean result = false;
	do {
		if (OS.g_main_context_acquire (context)) {
			result = OS.g_main_context_prepare (context, max_priority);
			int nfds;
			while ((nfds = OS.g_main_context_query (context, max_priority [0], timeout, fds, allocated_nfds)) > allocated_nfds) {
				OS.g_free (fds);
				allocated_nfds = nfds;
				fds = OS.g_malloc (OS.GPollFD_sizeof() * allocated_nfds);
			}
			long poll = OS.g_main_context_get_poll_func (context);
			if (poll != 0) {
				if (nfds > 0 || timeout [0] != 0) {
					/*
					* Bug in GTK. For some reason, g_main_context_wakeup() may
					* fail to wake up the UI thread from the polling function.
					* The fix is to sleep for a maximum of 50 milliseconds.
					*/
					if (timeout [0] < 0) timeout [0] = 50;

					/* Exit the OS lock to allow other threads to enter GTK */
					Lock lock = Platform.lock;
					int count = lock.lock ();
					for (int i = 0; i < count; i++) lock.unlock ();
					try {
						wake = false;
						OS.Call (poll, fds, nfds, timeout [0]);
					} finally {
						for (int i = 0; i < count; i++) lock.lock ();
						lock.unlock ();
					}
				}
			}
			OS.g_main_context_check (context, max_priority [0], fds, nfds);
			OS.g_main_context_release (context);
		}
	} while (!result && getMessageCount () == 0 && !wake);
	wake = false;
	sendPostExternalEventDispatchEvent ();
	return true;
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
		OS.g_source_remove (timerIds [index]);
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
	int timerId = OS.g_timeout_add (milliseconds, timerProc, index);
	if (timerId != 0) {
		timerIds [index] = timerId;
		timerList [index] = runnable;
	}
}

long timerProc (long i) {
	if (timerList == null) return 0;
	int index = (int)i;
	if (0 <= index && index < timerList.length) {
		Runnable runnable = timerList [index];
		timerList [index] = null;
		timerIds [index] = 0;
		if (runnable != null) {
			try {
				runnable.run ();
			} catch (RuntimeException exception) {
				runtimeExceptionHandler.accept (exception);
			} catch (Error exception) {
				errorHandler.accept (exception);
			}
		}
	}
	return 0;
}

long caretProc (long clientData) {
	caretId = 0;
	if (currentCaret == null) {
		return 0;
	}
	if (currentCaret.blinkCaret()) {
		int blinkRate = currentCaret.blinkRate;
		if (blinkRate == 0) return 0;
		/*
		 * blink is set to true so that when gtk_draw() is called, we know it is the correct
		 * state to draw/redraw the caret. See Bug 517487.
		 */
		currentCaret.getParent().blink = true;
		caretId = OS.g_timeout_add (blinkRate, caretProc, 0);
	} else {
		currentCaret = null;
	}
	return 0;
}

/*
 * Causes the caretProc method timing interval to be reset. This is useful as the blink rate
 * intervals should always reset when the caret is moved. See Bug 517487.
 */
void resetCaretTiming() {
	if (caretId != 0) {
		OS.g_source_remove(caretId);
		caretId = OS.g_timeout_add(currentCaret.blinkRate, caretProc, 0);
	}
}

long sizeAllocateProc (long handle, long arg0, long user_data) {
	Widget widget = getWidget (user_data);
	if (widget == null) return 0;
	return widget.sizeAllocateProc (handle, arg0, user_data);
}

long sizeRequestProc (long handle, long arg0, long user_data) {
	Widget widget = getWidget (user_data);
	if (widget == null) return 0;
	return widget.sizeRequestProc (handle, arg0, user_data);
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
	if (errorImage != null) resources [resourceCount++] = errorImage;
	if (infoImage != null) resources [resourceCount++] = infoImage;
	if (questionImage != null) resources [resourceCount++] = questionImage;
	if (warningImage != null) resources [resourceCount++] = warningImage;
	errorImage = infoImage = questionImage = warningImage = null;
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
		if (eventTable != null) sendEvent (eventTable, event);
	}
}

void sendEvent (EventTable eventTable, Event event) {
	int type = event.type;
	sendPreEvent (type);
	try {
		eventTable.sendEvent (event);
	} finally {
		sendPostEvent (type);
	}
}

void sendPreEvent (int eventType) {
	if (eventType != SWT.PreEvent && eventType != SWT.PostEvent
			&& eventType != SWT.PreExternalEventDispatch
			&& eventType != SWT.PostExternalEventDispatch) {
		if (eventTable != null && eventTable.hooks (SWT.PreEvent)) {
			Event event = new Event ();
			event.detail = eventType;
			sendEvent (SWT.PreEvent, event);
		}
	}
}

void sendPostEvent (int eventType) {
	if (eventType != SWT.PreEvent && eventType != SWT.PostEvent
			&& eventType != SWT.PreExternalEventDispatch
			&& eventType != SWT.PostExternalEventDispatch) {
		if (eventTable != null && eventTable.hooks (SWT.PostEvent)) {
			Event event = new Event ();
			event.detail = eventType;
			sendEvent (SWT.PostEvent, event);
		}
	}
}

/**
 * Sends a SWT.PreExternalEventDispatch event.
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public void sendPreExternalEventDispatchEvent () {
	if (eventTable != null && eventTable.hooks (SWT.PreExternalEventDispatch)) {
		sendEvent (SWT.PreExternalEventDispatch, null);
	}
}

/**
 * Sends a SWT.PostExternalEventDispatch event.
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public void sendPostExternalEventDispatchEvent () {
	if (eventTable != null && eventTable.hooks (SWT.PostExternalEventDispatch)) {
		sendEvent (SWT.PostExternalEventDispatch, null);
	}
}

void setCurrentCaret (Caret caret) {
	if (caretId != 0) OS.g_source_remove(caretId);
	caretId = 0;
	currentCaret = caret;
	if (caret == null) return;
	int blinkRate = currentCaret.blinkRate;
	caretId = OS.g_timeout_add (blinkRate, caretProc, 0);
}

long shellMapProc (long handle, long arg0, long user_data) {
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.shellMapProc (handle, arg0, user_data);
}

String simple_color_parser (String output, String value, int index) {
	/*
	 * This method takes a color value (rgb(...), #rgb, an X11 color, etc.)
	 * and makes sure it's input we can handle. We can handle rgb/rgba values,
	 * X11 colors, or colors in the format #rgb or #rrggbb.
	 *
	 * We cannot handle shade/gradient functions or references to other colors.
	 * Because of this we strip out values that start with "@" and check
	 * non rgb values against X11 named colors.
	 *
	 * I.e.: the following would be invalid input:
	 *
	 * shade(@bg_color, 0,7)
	 * or
	 * define-color error_bg_color @bg_color
	 */
	if (output != null && value != null) {
		int position;
		position = index + value.length() + 1;
		String color = output.substring(position);
		// Check for rgb color case
		if (color.startsWith("#") || color.startsWith("rgb")) {
			return color;
		} else if (!color.startsWith("@")) {
			// Check for an X11 color
			String [] cut = color.split(";");
			if (colorList.contains(cut[0])) {
				return color;
			}
		}
	}
	return "";
}

long signalProc (long gobject, long arg1, long user_data) {
	switch((int)user_data) {
		case STYLE_UPDATED:
			settingsChanged = true;
			break;
	}
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
		synchronized (idleLock) {
			if (idleNeeded && idleHandle == 0) {
				//NOTE: calling unlocked function in OS
				idleHandle = OS._g_idle_add (idleProc, 0);
			}
		}
	}
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
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Control#update()
 */
public void update () {
	checkDevice ();
	flushExposes (0, true);
	/*
	 * Do not send expose events on GTK 3.16.0+
	 * It's worth checking whether can be removed on all GTK 3 versions.
	 */
	if (GTK.GTK_VERSION < OS.VERSION(3, 16, 0)) {
		GDK.gdk_window_process_all_updates ();
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
	OS.g_main_context_wakeup (0);
	wake = true;
}

long enterMotionScrollProc (long controller, double x, double y, long user_data) {
	long handle = GTK.gtk_event_controller_get_widget(controller);
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.enterMotionScrollProc(handle, x, y, user_data);
}

long focusProc (long controller, long user_data) {
	long handle = GTK.gtk_event_controller_get_widget(controller);
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.focusProc(handle, user_data);
}

long keyPressReleaseProc (long controller, int keyval, int keycode, int state, long user_data) {
	long handle = GTK.gtk_event_controller_get_widget(controller);
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.keyPressReleaseProc(handle, keyval, keycode, state, user_data);
}

long gesturePressReleaseProc (long gesture, int n_press, double x, double y, long user_data) {
	long handle = GTK.gtk_event_controller_get_widget(gesture);
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.getsurePressReleaseProc (gesture, n_press, x, y, user_data);
}

long leaveProc (long controller, long user_data) {
	long handle = GTK.gtk_event_controller_get_widget(controller);
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.leaveProc(handle, user_data);
}

long notifyStateProc (long gdk_handle, long param_spec, long user_data) {
	Widget widget = getWidget (user_data);
	if (widget == null) return 0;
	return widget.notifyStateProc(gdk_handle, user_data);
}

long windowProc (long handle, long user_data) {
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, user_data);
}

long windowProc (long handle, long arg0, long user_data) {
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, arg0, user_data);
}

long windowProc (long handle, long arg0, long arg1, long user_data) {
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, arg0, arg1, user_data);
}

long windowProc (long handle, long arg0, long arg1, long arg2, long user_data) {
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, arg0, arg1, arg2, user_data);
}

long windowProc (long handle, long arg0, long arg1, long arg2, long arg3, long user_data) {
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.windowProc (handle, arg0, arg1, arg2, arg3, user_data);
}

long windowTimerProc (long handle) {
	Widget widget = getWidget (handle);
	if (widget == null) return 0;
	return widget.timerProc (handle);
}

long gdk_window_get_device_position (long window, int[] x, int[] y, int[] mask) {
	long display = 0;
	if( window != 0) {
		display = GDK.gdk_window_get_display (window);
	} else {
		window = GDK.gdk_get_default_root_window ();
		display = GDK.gdk_window_get_display (window);
	}
	long pointer = GDK.gdk_get_pointer (display);
	return GDK.gdk_window_get_device_position(window, pointer, x, y, mask);
}

long gdk_surface_get_device_position (long surface, int[] x, int[] y, int[] mask) {
	long display = 0;
	if (surface != 0) {
		display = GDK.gdk_surface_get_display (surface);
	}
	long pointer = GDK.gdk_get_pointer(display);
	return GDK.gdk_surface_get_device_position(surface, pointer, x, y, mask);
}

long gdk_device_get_window_at_position (int[] win_x, int[] win_y) {
	long display = GDK.gdk_display_get_default ();
	long device = GDK.gdk_get_pointer(display);
	return GDK.gdk_device_get_window_at_position (device, win_x, win_y);
}

long gdk_device_get_surface_at_position (int[] win_x, int[] win_y) {
	long display = GDK.gdk_display_get_default ();
	long device = GDK.gdk_get_pointer(display);
	return GDK.gdk_device_get_surface_at_position (device, win_x, win_y);
}

/**
 * @noreference This method is not intended to be referenced by clients.
 * @nooverride This method is not intended to be re-implemented or extended by clients.
 */
@Override
protected long gsettingsProc (long gobject, long arg1, long user_data) {
	switch((int)user_data) {
		case CHANGE_SCALEFACTOR:
			this.scaleFactor = getDeviceZoom ();
			DPIUtil.setDeviceZoom (scaleFactor);
			Shell[] shells = getShells();
			for (int i = 0; i < shells.length; i++) {
				shells[i].layout(true, true);
			}
	}
	return 0;
}

static int _getDeviceZoom (long monitor_num) {
	/*
	 * We can hard-code 96 as gdk_screen_get_resolution will always return -1
	 * if gdk_screen_set_resolution has not been called.
	 */
	int dpi = 96;
	if (GTK.GTK_VERSION >= OS.VERSION(3, 22, 0)) {
		long display = GDK.gdk_display_get_default();
		long monitor = GDK.gdk_display_get_monitor_at_point(display, 0, 0);
		int scale = GDK.gdk_monitor_get_scale_factor(monitor);
		dpi = dpi * scale;
	} else {
		long screen = GDK.gdk_screen_get_default ();
		dpi = (int) GDK.gdk_screen_get_resolution (screen);
		if (dpi <= 0) dpi = 96; // gdk_screen_get_resolution returns -1 in case of error
		int scale = GDK.gdk_screen_get_monitor_scale_factor (screen, (int) monitor_num);
		dpi = dpi * scale;
	}
	return DPIUtil.mapDPIToZoom (dpi);
}
}

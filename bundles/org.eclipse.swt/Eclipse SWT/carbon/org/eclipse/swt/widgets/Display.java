package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.EventRecord;
import org.eclipse.swt.internal.carbon.HICommand;

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
 * 
 * @see #syncExec
 * @see #asyncExec
 * @see #wake
 * @see #readAndDispatch
 * @see #sleep
 * @see #dispose
 */
public class Display extends Device {

	/* Windows, Events and Callbacks */
	static String APP_NAME = "SWT";
	Event [] eventQueue;
	EventTable eventTable, filterTable;
	
	/* Default Fonts, Colors, Insets, Widths and Heights. */
	Font defaultFont;
	Font listFont, textFont, buttonFont, labelFont, groupFont;		
	private short fHoverThemeFont;

	int dialogBackground, dialogForeground;
	int buttonBackground, buttonForeground, buttonShadowThickness;
	int compositeBackground, compositeForeground;
	int compositeTopShadow, compositeBottomShadow, compositeBorder;
	int listBackground, listForeground, listSelect, textBackground, textForeground;
	int labelBackground, labelForeground, scrollBarBackground, scrollBarForeground;
	int scrolledInsetX, scrolledInsetY, scrolledMarginX, scrolledMarginY;
	int defaultBackground, defaultForeground;
	int textHighlightThickness;
	
	/* System Colors */
	Color COLOR_WIDGET_DARK_SHADOW, COLOR_WIDGET_NORMAL_SHADOW, COLOR_WIDGET_LIGHT_SHADOW;
	Color COLOR_WIDGET_HIGHLIGHT_SHADOW, COLOR_WIDGET_BACKGROUND, COLOR_WIDGET_BORDER;
	Color COLOR_LIST_FOREGROUND, COLOR_LIST_BACKGROUND, COLOR_LIST_SELECTION, COLOR_LIST_SELECTION_TEXT;
	Color COLOR_INFO_FOREGROUND, COLOR_INFO_BACKGROUND;
	
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
	int timerProc;	
	
	/* Key Mappings. */
	static int [] [] KeyTable = {
	
		/* Keyboard and Mouse Masks */
//		{OS.XK_Alt_L,		SWT.ALT},
//		{OS.XK_Alt_R,		SWT.ALT},
//		{OS.XK_Shift_L,		SWT.SHIFT},
//		{OS.XK_Shift_R,		SWT.SHIFT},
//		{OS.XK_Control_L,	SWT.CONTROL},
//		{OS.XK_Control_R,	SWT.CONTROL},

		/* NOT CURRENTLY USED */
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},

		/* Non-Numeric Keypad Keys */
		{126,	SWT.ARROW_UP},
		{125,	SWT.ARROW_DOWN},
		{123,	SWT.ARROW_LEFT},
		{124,	SWT.ARROW_RIGHT},
		{116,	SWT.PAGE_UP},
		{121,	SWT.PAGE_DOWN},
		{115,	SWT.HOME},
		{119,	SWT.END},
		{71,	SWT.INSERT},

		/* Virtual and Ascii Keys */
		{51,	SWT.BS},
		{36,	SWT.CR},
		{117,	SWT.DEL},
		{53,	SWT.ESC},
		{76,	SWT.LF},
		{48,	SWT.TAB},	
		
		/* Functions Keys */
		{122,		SWT.F1},
		{120,		SWT.F2},
		{99,		SWT.F3},
		{118,		SWT.F4},
		{96,		SWT.F5},
		{97,		SWT.F6},
		{98,		SWT.F7},
		{100,		SWT.F8},
		{101,		SWT.F9},
		{109,		SWT.F10},
		{103,		SWT.F11},
		{111,		SWT.F12},
		
		/* Numeric Keypad Keys */
	};

	/* Multiple Displays. */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Double Click */
	int lastTime, lastButton;
	short lastGlobalMouseXPos, lastGlobalMouseYPos;
	
	/* Current caret */
	Caret currentCaret;
	int caretID, caretProc;
				
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";
	
	/* Mouse Hover */
	int mouseHoverID, mouseHoverProc;
	int mouseHoverHandle, toolTipWindowHandle;
			
	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;
	
	/* AW Mac */
	private static final int TOOLTIP_MARGIN= 3;
	private static final int HOVER_TIMEOUT= 500;	// in milli seconds
	private static final int SWT_USER_EVENT= ('S'<<24) + ('W'<<16) + ('T'<<8) + '1';

	private short fMenuId= 5000;
	
	// Callbacks
	private ArrayList fCallbacks;
	// callback procs
	int fApplicationProc;
	int fWindowProc;
	int fTooltipWindowProc;
	int fMouseProc;
	int fMenuProc;
	int fControlActionProc;
	int fUserPaneHitTestProc;
	int fDataBrowserDataProc, fDataBrowserCompareProc, fDataBrowserItemNotificationProc;
	int fControlProc;
	
	private boolean fMenuIsVisible;
	private int fTrackedControl;
	private int fFocusControl;
	private int fCurrentControl;
	private String fToolTipText;
	private int fLastHoverHandle;
	private boolean fInContextMenu;	// true while tracking context menu
	public int fCurrentCursor;
	private Shell fMenuRootShell;
	int fLastModifiers;
	MacMouseEvent fLastMouseEvent;
	
	private static boolean fgCarbonInitialized;
	private static boolean fgInitCursorCalled;
	/* end AW */
	
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

void addMouseHoverTimeOut (int handle) {
	if (mouseHoverID != 0) OS.RemoveEventLoopTimer(mouseHoverID);
	mouseHoverID = 0;
	if (handle == fLastHoverHandle) return;
	int[] timer= new int[1];
	OS.InstallEventLoopTimer(OS.GetCurrentEventLoop(), HOVER_TIMEOUT / 1000.0, 0.0, mouseHoverProc, handle, timer);
	mouseHoverID = timer[0];
	mouseHoverHandle = handle;
}
static DeviceData checkNull (DeviceData data) {
	if (data == null) data = new DeviceData ();
	return data;
}
protected void checkDevice () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
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
	OS.SysBeep((short)100);
}
int caretProc (int id, int clientData) {
	if (id != caretID) {
		return 0;
	}
	OS.RemoveEventLoopTimer(id);
	caretID = 0;
	if (currentCaret == null) return 0;
	if (currentCaret.blinkCaret ()) {
		int blinkRate = currentCaret.blinkRate;
		int[] timer= new int[1];
		OS.InstallEventLoopTimer(OS.GetCurrentEventLoop(), blinkRate / 1000.0, 0.0, caretProc, 0, timer);
		caretID = timer[0];
	} else {
		currentCaret = null;
	}
	return 0;
}
static synchronized void checkDisplay (Thread thread) {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] != null && Displays [i].thread == thread) {
			SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
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
	checkDisplay (thread = Thread.currentThread ());
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}
void createDisplay (DeviceData data) {
	
	/* Initialize Carbon */
	synchronized (Display.class) {
		if (!fgCarbonInitialized) {
			OS.RegisterAppearanceClient();
			OS.TXNInitTextension(0, 0, 0);
			//OS.InitCursor();
			OS.QDSwapTextFlags(OS.kQDUseCGTextRendering + OS.kQDUseCGTextMetrics);
			if (OS.InitContextualMenus() != OS.noErr)
				System.out.println("Display.createDisplay: error in OS.InitContextualMenus");
			int[] psn= new int[2];
			if (OS.GetCurrentProcess(psn) == OS.noErr)
	    		OS.SetFrontProcess(psn);
				
			// workaround for Register problem
			Rect bounds= new Rect();
			int[] ctl = new int[1];
			OS.CreatePushButtonControl(0, bounds, 0, ctl);
			OS.DisposeControl(ctl[0]);
	    }
		fgCarbonInitialized = true;
	}
	
	fGDeviceHandle= OS.GetMainDevice();
}
synchronized static void deregister (Display display) {
	for (int i=0; i<Displays.length; i++) {
		if (display == Displays [i]) Displays [i] = null;
	}
}
protected void destroy () {
	if (this == Default) Default = null;
	deregister (this);
	destroyDisplay ();
}
void destroyDisplay () {
	// dispose Callbacks
	Iterator iter= fCallbacks.iterator();
	while (iter.hasNext()) {
		Callback cb= (Callback) iter.next();
		cb.dispose();
	}
	fCallbacks= null;
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
	System.out.println("Display.getCursorControl: nyi");
	
	/* AW
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
	*/
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
	org.eclipse.swt.internal.carbon.Point loc= new org.eclipse.swt.internal.carbon.Point();
	OS.GetGlobalMouse(loc);
	return new Point (loc.h, loc.v);
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
	return (OS.GetDblTime() * 1000) / 60; 
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
	/* AW
	int [] buffer1 = new int [1], buffer2 = new int [1];
	OS.XGetInputFocus (xDisplay, buffer1, buffer2);
	int xWindow = buffer1 [0];
	if (xWindow == 0) return null;
	int handle = OS.XtWindowToWidget (xDisplay, xWindow);
	if (handle == 0) return null;
	handle = OS.XmGetFocusWidget (handle);
	*/
	int handle= fFocusControl;
	if (handle == 0) return null;
	do {
		Widget widget = WidgetTable.get (handle);
		if (widget instanceof Control) {
			Control window = (Control) widget;
			if (window.getEnabled ()) return window;
		}
	} while ((handle = MacUtil.getSuperControl (handle)) != 0);
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
	return 8;	// we don't support direct icons yet
}

int getLastEventTime () {
//	return (int) (OS.GetLastUserEventTime () * 1000.0);
	return (int) System.currentTimeMillis ();
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
	Color xColor = null;
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: 		return COLOR_INFO_FOREGROUND;
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
	if (xColor == null)
		System.out.println("Display.getSystemColor: color null " + id);
	if (xColor == null) return super.getSystemColor (SWT.COLOR_BLACK);
	//return Color.carbon_new (this, xColor);
	return xColor;
	// return getSystemColor(this, id);
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
	return defaultFont;
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
	if (toolTipWindowHandle != 0) {
		OS.HideWindow(toolTipWindowHandle);
		OS.DisposeWindow(toolTipWindowHandle);
		toolTipWindowHandle = 0;
	}
}
protected void init () {
	super.init ();
				
	/* Create the callbacks */
	timerProc= createCallback("timerProc", 2);
	caretProc= createCallback("caretProc", 2);
	mouseHoverProc= createCallback("mouseHoverProc", 2);

	fWindowProc= createCallback("handleWindowCallback", 3);
	fTooltipWindowProc= createCallback("handleTooltipWindowCallback", 3);
	fMouseProc= createCallback("handleMouseCallback", 3);
	
	fControlActionProc= createCallback("handleControlAction", 2);
	fControlProc= createCallback("handleControlProc", 3);
	
	//fUserPaneDrawProc= createCallback("handleUserPaneDraw", 2);
	fUserPaneHitTestProc= createCallback("handleUserPaneHitTest", 2);
	
	fDataBrowserDataProc= createCallback("handleDataBrowserDataCallback", 5);
	fDataBrowserCompareProc= createCallback("handleDataBrowserCompareCallback", 4);
	fDataBrowserItemNotificationProc= createCallback("handleDataBrowserItemNotificationCallback", 3);
	
	fMenuProc= createCallback("handleMenuCallback", 3);
	
	// create standard event handler
	fApplicationProc= createCallback("handleApplicationCallback", 3);
	int[] mask= new int[] {
		OS.kEventClassCommand, OS.kEventProcessCommand,
		
		//OS.kEventClassAppleEvent, OS.kAEQuitApplication,
		OS.kEventClassAppleEvent, OS.kEventAppleEvent,
		
		// we track down events here because we need to know when the user 
		// has clicked in the menu bar
		OS.kEventClassMouse, OS.kEventMouseDown,
		// we track up, dragged, and moved events because
		// we need to get these events even if the mouse is outside of the window.
		OS.kEventClassMouse, OS.kEventMouseDragged,
		OS.kEventClassMouse, OS.kEventMouseUp,
		OS.kEventClassMouse, OS.kEventMouseMoved,
			
		SWT_USER_EVENT, 54321,
		SWT_USER_EVENT, 54322,
	};
	if (OS.InstallEventHandler(OS.GetApplicationEventTarget(), fApplicationProc, mask.length / 2, mask, 0, null) != OS.noErr)
		error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	
	int textInputProc= createCallback("handleTextCallback", 3);
	mask= new int[] {
		OS.kEventClassKeyboard, OS.kEventRawKeyDown,
		OS.kEventClassKeyboard, OS.kEventRawKeyModifiersChanged,
		OS.kEventClassKeyboard, OS.kEventRawKeyRepeat,
		OS.kEventClassKeyboard, OS.kEventRawKeyUp,
	};
	if (OS.InstallEventHandler(OS.GetUserFocusEventTarget(), textInputProc, mask.length / 2, mask, 0, null) != OS.noErr)
		error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	
	buttonFont = Font.carbon_new (this, getThemeFont(OS.kThemeSmallSystemFont));
	buttonShadowThickness= 1;

	//scrolledInsetX = scrolledInsetY = 15;
	scrolledMarginX= scrolledMarginY= 15;
	compositeForeground = 0x000000;
	compositeBackground = -1; // 0xEEEEEE;
	
	groupFont = Font.carbon_new (this, getThemeFont(OS.kThemeSmallEmphasizedSystemFont));
	
	dialogForeground= 0x000000;
	dialogBackground= 0xffffff;
	
	labelForeground = 0x000000;
	labelBackground = -1; 
	labelFont = Font.carbon_new (this, getThemeFont(OS.kThemeSmallSystemFont));
	
	listForeground = 0x000000;
	listBackground = 0xffffff;
	listSelect = listForeground;	// if reversed colors
	listFont= Font.carbon_new (this, new MacFont((short)1)); // Mac Appl Font

	scrollBarForeground = 0x000000;
	scrollBarBackground = 0xffffff;

	textForeground = 0x000000;
	textBackground = 0xffffff;
	textHighlightThickness = 1; // ???
	textFont= Font.carbon_new (this, new MacFont((short)1));	// Mac Appl Font

	COLOR_WIDGET_DARK_SHADOW = 		Color.carbon_new(this, 0x333333, true);	
	COLOR_WIDGET_NORMAL_SHADOW = 	Color.carbon_new(this, 0x666666, true);	
	COLOR_WIDGET_LIGHT_SHADOW = 	Color.carbon_new(this, 0x999999, true);
	COLOR_WIDGET_HIGHLIGHT_SHADOW = Color.carbon_new(this, 0xCCCCCC, true);	
	COLOR_WIDGET_BACKGROUND = 		Color.carbon_new(this, 0xFFFFFF, true);	
	COLOR_WIDGET_BORDER = 			Color.carbon_new(this, 0x000000, true);	
	COLOR_LIST_FOREGROUND = 		Color.carbon_new(this, 0x000000, true);	
	COLOR_LIST_BACKGROUND = 		Color.carbon_new(this, 0xFFFFFF, true);	
	COLOR_LIST_SELECTION = 			Color.carbon_new(this, 0x6666CC, true);
	COLOR_LIST_SELECTION_TEXT = 	Color.carbon_new(this, 0xFFFFFF, true);
	COLOR_INFO_BACKGROUND = 		Color.carbon_new(this, 0xFFFFE1, true);
	COLOR_INFO_FOREGROUND = 		Color.carbon_new(this, 0x000000, true);
	
	fHoverThemeFont= (short) OS.kThemeSmallSystemFont;

	defaultFont = Font.carbon_new (this, getThemeFont(OS.kThemeSmallSystemFont));
	
	defaultForeground = compositeForeground;
	defaultBackground = compositeBackground;
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
	/* AW
	int xDrawable = OS.XDefaultRootWindow (xDisplay);
	int xGC = OS.XCreateGC (xDisplay, xDrawable, 0, null);
	if (xGC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.XSetSubwindowMode (xDisplay, xGC, OS.IncludeInferiors);
	if (data != null) {
		data.device = this;
		data.display = xDisplay;
		data.drawable = xDrawable;
		data.fontList = defaultFont;
		data.colormap = OS.XDefaultColormap (xDisplay, OS.XDefaultScreen (xDisplay));
	}
	return xGC;
	*/

	if (data != null) {
		data.device = this;
		/* AW
		data.display = xDisplay;
		data.drawable = xWindow;
		data.foreground = argList [1];
		data.background = argList [3];
		data.fontList = fontList;
		data.colormap = argList [5];
		*/
		data.foreground = 0x000000;
		data.background = 0xffffff;
		data.font = new MacFont((short)1);
		data.controlHandle = 0;
	}

	int wHandle= OS.FrontWindow();
	int xGC= OS.GetWindowPort(wHandle);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	
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
}
boolean isValidThread () {
	return thread == Thread.currentThread ();
}
static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
}
int mouseHoverProc (int id, int handle) {
	if (mouseHoverID != 0) OS.RemoveEventLoopTimer(mouseHoverID);
	mouseHoverID = mouseHoverHandle = 0;
	if (fLastMouseEvent == null) return OS.noErr;
	int rc= windowProc (handle, SWT.MouseHover, fLastMouseEvent);
	sendUserEvent(54321);
	return rc;
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
	
	if (!fgInitCursorCalled) {
		OS.InitCursor();
		fgInitCursorCalled= true;
	}
	
	int[] evt= new int[1];
	int rc= OS.ReceiveNextEvent(0, null, OS.kEventDurationNoWait, true, evt);
	
	switch (rc) {
	case OS.noErr:
		int event= evt[0];
		if (OS.GetEventClass(event) == SWT_USER_EVENT && OS.GetEventKind(event) == 54322) {
			//System.out.println("aha");
			OS.ReleaseEvent(event);
			break;
		}
		//System.out.println("event: " + MacUtil.toString(OS.GetEventClass(event)));
		OS.SendEventToEventTarget(event, OS.GetEventDispatcherTarget());
		OS.ReleaseEvent(event);
		runDeferredEvents();
		return true;
		
	case OS.eventLoopTimedOutErr:
		// System.out.println("readAndDispatch: eventLoopTimedOutErr");
		break;	// no event: run async
		
	default:
		System.out.println("readAndDispatch: error " + rc);
		break;
	}
	return runAsyncMessages ();
}
static synchronized void register (Display display) {
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
protected void release () {
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
	
	/* Dispose the caret callback */
	/* AW
	if (caretID != 0) OS.XtRemoveTimeOut (caretID);
	*/
	if (caretID != 0) OS.RemoveEventLoopTimer(caretID);
	caretID = caretProc = 0;
	
	/* Dispose the timer callback */
	if (timerIDs != null) {
		for (int i=0; i<timerIDs.length; i++) {
			/* AW
			if (timerIDs [i] != 0) OS.XtRemoveTimeOut (timerIDs [i]);
			*/
			if (timerIDs [i] != 0) OS.RemoveEventLoopTimer (timerIDs [i]);
		}
	}
	timerIDs = null;
	timerList = null;
	timerProc = 0;

	/* Dispose the mouse hover callback */
	if (mouseHoverID != 0) OS.RemoveEventLoopTimer(mouseHoverID);
	mouseHoverID = mouseHoverProc = mouseHoverHandle = toolTipWindowHandle = 0;

	/* Free the font lists */
	/* AW
	if (buttonFont != 0) OS.XmFontListFree (buttonFont);
	if (labelFont != 0) OS.XmFontListFree (labelFont);
	if (textFont != 0) OS.XmFontListFree (textFont);
	if (listFont != 0) OS.XmFontListFree (listFont);
	listFont = textFont = labelFont = buttonFont = 0;
	*/
	defaultFont = null;	
	
	/* Release references */
	thread = null;
	buttonBackground = buttonForeground = 0;
	defaultBackground = defaultForeground = 0;
	COLOR_WIDGET_DARK_SHADOW = COLOR_WIDGET_NORMAL_SHADOW = COLOR_WIDGET_LIGHT_SHADOW =
	COLOR_WIDGET_HIGHLIGHT_SHADOW = COLOR_WIDGET_BACKGROUND = COLOR_WIDGET_BORDER =
	COLOR_LIST_FOREGROUND = COLOR_LIST_BACKGROUND = COLOR_LIST_SELECTION = COLOR_LIST_SELECTION_TEXT = null;
	COLOR_INFO_BACKGROUND = null;
}
void releaseToolTipHandle (int handle) {
	if (mouseHoverHandle == handle) removeMouseHoverTimeOut ();
	if (toolTipWindowHandle != 0) {
		/* AW
		int shellParent = OS.XtParent(toolTipWindowHandle);
		if (handle == shellParent) toolTipWindowHandle = 0;
		*/
	}
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

void removeMouseHoverTimeOut () {
	if (mouseHoverID != 0) OS.RemoveEventLoopTimer(mouseHoverID);
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
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	/* AW
	int x = point.x;
	int y = point.y;
	int xWindow = OS.XDefaultRootWindow (xDisplay);	
	OS.XWarpPointer (xDisplay, OS.None, xWindow, 0, 0, 0, 0, x, y);
	*/
	System.out.println("Display.setCursorLocation: nyi");
}

void setCurrentCaret (Caret caret) {
	if (caretID != 0) OS.RemoveEventLoopTimer(caretID);
	caretID = 0;
	currentCaret = caret;
	if (currentCaret != null) {
		int blinkRate = currentCaret.blinkRate;
		int[] timer= new int[1];
		OS.InstallEventLoopTimer(OS.GetCurrentEventLoop(), blinkRate / 1000.0, 0.0, caretProc, 0, timer);
		caretID = timer[0];
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
void setToolTipText (int handle, String toolTipText) {
/* AW
	if (toolTipHandle == 0) return;
	int shellHandle = OS.XtParent (toolTipHandle);
	int shellParent = OS.XtParent (shellHandle);
	if (handle != shellParent) return;
*/
	showToolTip (handle, toolTipText);
}
void showToolTip (int handle, String toolTipText) {

	if (toolTipText == null || toolTipText.length () == 0) {
		if (toolTipWindowHandle != 0)
			OS.HideWindow(toolTipWindowHandle);
		return;
	}

	if (toolTipWindowHandle != 0)
		 return;
	
	if (handle != fCurrentControl) {
		//System.out.println("Display.showToolTip: handle is not current");
		//beep();
		return;
	}
	if (fMenuIsVisible) {
		//System.out.println("Display.showToolTip: menu is visible");
		//beep();
		return;
	}	
	if (OS.StillDown()) {
		//System.out.println("Display.showToolTip: button is down");
		//beep();
		return;
	}	
	
	toolTipText= MacUtil.removeMnemonics(toolTipText);
	
	// remember text
	fToolTipText= toolTipText;
	
	// calculate text bounding box
	short[] bounds= new short[2];
	short[] baseLine= new short[1];
	int sHandle= OS.CFStringCreateWithCharacters(toolTipText);
	OS.GetThemeTextDimensions(sHandle, fHoverThemeFont, OS.kThemeStateActive, false, bounds, baseLine);
	if (bounds[1] > 200) {	// too wide -> wrap text
		bounds[1]= (short) 200;
		OS.GetThemeTextDimensions(sHandle, fHoverThemeFont, OS.kThemeStateActive, true, bounds, baseLine);
	}
	OS.CFRelease(sHandle);
	int width= bounds[1] + 2*TOOLTIP_MARGIN;
	int height= bounds[0] + 2*TOOLTIP_MARGIN;
	
	// position just below mouse cursor
	org.eclipse.swt.internal.carbon.Point loc= new org.eclipse.swt.internal.carbon.Point();
	OS.GetGlobalMouse(loc);
	int x= loc.h + 16;
	int y= loc.v + 16;

	// Ensure that the tool tip is on the screen.
	Rect screenBounds= new Rect();
	OS.GetAvailableWindowPositioningBounds(OS.GetMainDevice(), screenBounds);
	x = Math.max (0, Math.min (x, screenBounds.right - screenBounds.left - width ));
	y = Math.max (0, Math.min (y, screenBounds.bottom - screenBounds.top - height ));

	// create window
	int[] wHandle= new int[1];
	Rect rect = new Rect();
	rect.left = (short)x;
	rect.top = (short)y;
	rect.right = (short)(x + width);
	rect.bottom = (short)(y + height);
	if (OS.CreateNewWindow(OS.kHelpWindowClass, 0, rect, wHandle) == OS.noErr) {
		toolTipWindowHandle= wHandle[0];
		int[] mask= new int[] {
			OS.kEventClassWindow, OS.kEventWindowDrawContent
		};
		OS.InstallEventHandler(OS.GetWindowEventTarget(toolTipWindowHandle), fTooltipWindowProc,
					mask.length / 2, mask, toolTipWindowHandle, null);
		OS.ShowWindow(toolTipWindowHandle);
		fLastHoverHandle= handle;
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
 * </ul>
 *
 * @see #wake
 */
public boolean sleep () {
	checkDevice ();
	int rc= OS.ReceiveNextEvent(0, null, OS.kEventDurationForever, false, null);
	if (rc != OS.noErr)
		System.out.println("oha: " + rc);
	return rc == OS.noErr;
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
	int[] timer= new int[1];
	OS.InstallEventLoopTimer(OS.GetCurrentEventLoop(), milliseconds / 1000.0, 0.0, timerProc, index, timer);
	int timerID = timer[0];
	
	if (timerID != 0) {
		timerIDs [index] = timerID;
		timerList [index] = runnable;
	}
}
int timerProc (int id, int index) {
	if (id != 0)
		OS.RemoveEventLoopTimer(id);
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
/**
 * Forces all outstanding paint requests for the display
 * to be processed before this method returns.
 *
 * @see Control#update
 */
public void update () {
	checkDevice ();
	/* AW
	XAnyEvent event = new XAnyEvent ();
	int mask = OS.ExposureMask | OS.ResizeRedirectMask |
		OS.StructureNotifyMask | OS.SubstructureNotifyMask |
		OS.SubstructureRedirectMask;
	OS.XSync (xDisplay, false); OS.XSync (xDisplay, false);
	while (OS.XCheckMaskEvent (xDisplay, mask, event)) OS.XtDispatchEvent (event);
	*/
	int[] mask= new int[] {
		OS.kEventClassWindow, OS.kEventWindowDrawContent
	};
	int[] evt= new int[1];
	while (OS.ReceiveNextEvent(mask.length/2, mask, 0.01, true, evt) == OS.noErr) {
		int rc= OS.SendEventToEventTarget(evt[0], OS.GetEventDispatcherTarget());
        if (rc != OS.noErr)
			System.out.println("Display.update: SendEventToEventTarget: " + rc);
		OS.ReleaseEvent(evt[0]);
	}
	/*
	if (wHandle != 0) {
		int port= OS.GetWindowPort(wHandle);
		if (port != 0)
			OS.QDFlushPortBuffer(port, 0);
	}
	*/
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
	if (thread == Thread.currentThread ()) return;
	/* Send a user event to wake up in ReceiveNextEvent */
	sendUserEvent(54322);
}
public int windowProc (int handle, int clientData) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (clientData);
}
public int windowProc (int handle, boolean callData) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processSetFocus (new Boolean(callData));
}
public int windowProc (int handle, int clientData, int callData) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processResize (new Integer(callData));
}
public int windowProc (int handle, int clientData, MacEvent callData) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (clientData, callData);
}
public int windowProc (int handle, int clientData, MacMouseEvent mme) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (clientData, mme);
}
public int windowProc (int handle, int clientData, MacControlEvent mce) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processEvent (clientData, mce);
}
static String convertToLf(String text) {
	char Cr = '\r';
	char Lf = '\n';
	int length = text.length ();
	if (length == 0) return text;
	
	/* Check for an LF or CR/LF.  Assume the rest of the string 
	 * is formatted that way.  This will not work if the string 
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

////////////////////////////////////////////////////////////////////////////
// Some Mac helper functions
////////////////////////////////////////////////////////////////////////////

	short nextMenuId() {
		//FIXME - menu id's are 16-bit and wrap at 0xFFFF
		return fMenuId++;
	}

	void flush (int cHandle) {
		int wHandle= OS.GetControlOwner(cHandle);
		if (wHandle != 0) {
			int port= OS.GetWindowPort(wHandle);
			if (port != 0)
				OS.QDFlushPortBuffer(port, 0);
		}
	}
	
	//---- callbacks
	
	private int handleControlAction(int cHandle, int partCode) {
		return windowProc(cHandle, SWT.Selection, new MacControlEvent(cHandle, partCode, true));
	}
	
	private int handleControlProc(int inCallRef, int inEvent, int cHandle) {
		int clazz= OS.GetEventClass(inEvent);
		
		if (OS.GetEventClass(inEvent) == OS.kEventClassControl) {
			int kind= OS.GetEventKind(inEvent);
			switch (kind) {
				
			case OS.kEventControlDraw:
				int[] gccontext= new int[1];
				OS.GetEventParameter(inEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, gccontext);		
				int[] region= new int[1];
				if (OS.GetEventParameter(inEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle, null, 4, null, region) != OS.noErr)
					System.err.println("kEventControlDraw: couldn't retrieve region");	
				windowProc(cHandle, SWT.Paint, new MacControlEvent(inEvent, region[0], gccontext[0]));				
				return OS.noErr;
				
			case OS.kEventControlHit:
				short[] part= new short[1];
				OS.GetEventParameter(inEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
				return windowProc(cHandle, SWT.Selection, new MacControlEvent(cHandle, part[0], true));
				
			default:
				System.out.println("Display.handleControlProc: wrong event kind: " + kind);
				break;
			}
		} else {
			System.out.println("Display.handleControlProc: wrong event class: " + clazz);
		}
		return OS.eventNotHandledErr;
	}
			
	private int handleUserPaneHitTest(int cHandle, int where) {
		return 111;
	}
			
	private int handleDataBrowserDataCallback(int cHandle, int item, int property, int itemData, int setValue) {
		Widget widget= WidgetTable.get(cHandle);
		if (widget instanceof List) {
			List list= (List) widget;
			return list.handleItemCallback(item, property, itemData);
		}
		if (widget instanceof Tree2) {
			Tree2 tree= (Tree2) widget;
			return tree.handleItemCallback(item, property, itemData, setValue);
		}
		return OS.noErr;
	}
	
	private int handleDataBrowserCompareCallback(int cHandle, int item1ID, int item2ID, int sortID) {
		Widget widget= WidgetTable.get(cHandle);
		if (widget instanceof List) {
			List list= (List) widget;
			return list.handleCompareCallback(item1ID, item2ID, sortID);
		}
		if (widget instanceof Tree2) {
			Tree2 tree= (Tree2) widget;
			return tree.handleCompareCallback(item1ID, item2ID, sortID);
		}
		return OS.noErr;
	}
	
	private int handleDataBrowserItemNotificationCallback(int cHandle, int item, int message) {
		Widget widget= WidgetTable.get(cHandle);
		if (widget instanceof List) {
			List list= (List) widget;
			return list.handleItemNotificationCallback(item, message);
		}
		if (widget instanceof Tree2) {
			Tree2 tree= (Tree2) widget;
			return tree.handleItemNotificationCallback(item, message);
		}
		return OS.noErr;
	}
	
	private int handleMenuCallback(int nextHandler, int eHandle, int mHandle) {
		switch (OS.GetEventKind(eHandle)) {
			
		case OS.kEventMenuPopulate:
		case OS.kEventMenuOpening:
			if (fInContextMenu)
				OS.SetMenuFont(mHandle, (short)1024, (short)11);	// AW todo: FIXME menu id
			/*
			// copy the menu's font
			short[] fontID= new short[1];
			short[] size= new short[1];
			OS.GetMenuFont(hMenu, fontID, size);
			OS.SetMenuFont(menu.handle, fontID[0], size[0]);
			*/ 
			windowProc(mHandle, SWT.Show, new MacEvent(eHandle, nextHandler));
			break;
			
		case OS.kEventMenuClosed:
			windowProc(mHandle, SWT.Hide, new MacEvent(eHandle, nextHandler));
			break;
		}
		return OS.noErr;
	}
	
	private int handleTextCallback(int nextHandler, int eRefHandle, int userData) {
		
		int eventClass= OS.GetEventClass(eRefHandle);
		int eventKind= OS.GetEventKind(eRefHandle);
		
		switch (eventClass) {
			
		case OS.kEventClassTextInput:
			switch (eventKind) {
			case OS.kEventTextInputUnicodeForKeyEvent:
				return OS.eventNotHandledErr;
			default:
				System.out.println("Display.handleTextCallback: kEventClassTextInput: unexpected event kind");
				break;
			}
			break;
			
		case OS.kEventClassKeyboard:
		
			// decide whether a SWT control has the focus
			Control focus= getFocusControl();
			if (focus == null || focus.handle == 0)
				return OS.eventNotHandledErr;
				
			int frontWindow= OS.FrontWindow();
			if (findWidget(frontWindow) == null) {
				int w= OS.GetControlOwner(focus.handle);
				if (w != OS.FrontWindow())	// its probably a standard dialog
					return OS.eventNotHandledErr;
			}
						
			switch (eventKind) {
			case OS.kEventRawKeyDown:
				if (MacEvent.getKeyCode(eRefHandle) == 114) {	// help key
					windowProc(focus.handle, SWT.Help);
					return OS.noErr;
				}
				// fall through!
			case OS.kEventRawKeyRepeat:
				return focus.processEvent(SWT.KeyDown, new MacEvent(eRefHandle, nextHandler));
				
			case OS.kEventRawKeyUp:
				return focus.processEvent(SWT.KeyUp, new MacEvent(eRefHandle, nextHandler));
				
			case OS.kEventRawKeyModifiersChanged:
				MacEvent macEvent = new MacEvent(eRefHandle, nextHandler);
				int modifiers = macEvent.getModifiers();
				int eventType = SWT.KeyUp;
				if ((modifiers & OS.shiftKey) != 0 && (fLastModifiers & OS.shiftKey) == 0) eventType = SWT.KeyDown;
				if ((modifiers & OS.controlKey) != 0 && (fLastModifiers & OS.controlKey) == 0) eventType = SWT.KeyDown;
				if ((modifiers & OS.cmdKey) != 0 && (fLastModifiers & OS.cmdKey) == 0) eventType = SWT.KeyDown;
				if ((modifiers & OS.optionKey) != 0 && (fLastModifiers & OS.optionKey) == 0) eventType = SWT.KeyDown;
				int result = focus.processEvent(eventType, macEvent);
				fLastModifiers = modifiers;
				return result;				
			default:
				System.out.println("Display.handleTextCallback: kEventClassKeyboard: unexpected event kind");
				break;
			}
			break;
			
		default:
			System.out.println("Display.handleTextCallback: unexpected event class");
			break;
		}
		return OS.eventNotHandledErr;
	}
	
	private int handleWindowCallback(int nextHandler, int eRefHandle, int whichWindow) {
		
		int eventClass= OS.GetEventClass(eRefHandle);
		int eventKind= OS.GetEventKind(eRefHandle);
		
		switch (eventClass) {
			
		case OS.kEventClassMouse:
			return handleMouseCallback(nextHandler, eRefHandle, whichWindow);
			
		case OS.kEventClassWindow:
			switch (eventKind) {
			case OS.kEventWindowActivated:
				Widget widget = WidgetTable.get(whichWindow);
				if (widget instanceof Shell)
					fMenuRootShell= (Shell) widget;
				windowProc(whichWindow, true);
				break; //return OS.noErr;
				
			case OS.kEventWindowDeactivated:
				fMenuRootShell= null;
				windowProc(whichWindow, false);
				break; // return OS.noErr;
				
			case OS.kEventWindowBoundsChanged:
				int[] attr= new int[1];
				OS.GetEventParameter(eRefHandle, OS.kEventParamAttributes, OS.typeUInt32, null, attr.length*4, null, attr);
				windowProc(whichWindow, SWT.Resize, attr[0]);
				return OS.noErr;
				
			case OS.kEventWindowClose:
				windowProc(whichWindow, SWT.Dispose);
				return OS.noErr;
							
			default:
				System.out.println("handleWindowCallback: kEventClassWindow kind:" + eventKind);
				break;
			}
			break;
			
		default:
			System.out.println("handleWindowCallback: unexpected event class: " + MacUtil.toString(eventClass));
			break;
		}
		return OS.eventNotHandledErr;
	}
	
	private int handleTooltipWindowCallback(int nextHandler, int eRefHandle, int whichWindow) {
		
		int eventClass= OS.GetEventClass(eRefHandle);
		int eventKind= OS.GetEventKind(eRefHandle);

		if (eventClass == OS.kEventClassWindow && eventKind == OS.kEventWindowDrawContent) {
			Rect bounds= new Rect();
			OS.GetWindowBounds(whichWindow, (short)OS.kWindowContentRgn, bounds);
			int width= bounds.right - bounds.left;
			int height= bounds.bottom - bounds.top;
			OS.SetRect(bounds, (short)0, (short)0, (short)width, (short)height);
			MacUtil.RGBBackColor(COLOR_INFO_BACKGROUND.handle);
			MacUtil.RGBForeColor(COLOR_INFO_FOREGROUND.handle);
			OS.EraseRect(bounds);
			if (fToolTipText != null) {
				OS.SetRect(bounds, (short)TOOLTIP_MARGIN, (short)TOOLTIP_MARGIN, (short)(width-TOOLTIP_MARGIN), (short)(height-TOOLTIP_MARGIN));
				int sHandle= OS.CFStringCreateWithCharacters(fToolTipText);
				OS.DrawThemeTextBox(sHandle, fHoverThemeFont, OS.kThemeStateActive, true, bounds, (short)0, 0);
				OS.CFRelease(sHandle);
			}
			return OS.noErr;
		}
		return OS.eventNotHandledErr;
	}
	
	private int handleApplicationCallback(int nextHandler, int eRefHandle, int userData) {
	
		int eventClass= OS.GetEventClass(eRefHandle);
		int eventKind= OS.GetEventKind(eRefHandle);
		
		switch (eventClass) {
			
		case OS.kEventClassAppleEvent:
			// check for 'quit' events
			int[] aeclass= new int[1];
			if (OS.GetEventParameter(eRefHandle, OS.kEventParamAEEventClass, OS.typeType, null, aeclass.length*4, null, aeclass) == OS.noErr) {
				// System.out.println("kEventClassAppleEvent: " + MacUtil.toString(aeclass[0]));
				int[] aetype= new int[1];
				if (OS.GetEventParameter(eRefHandle, OS.kEventParamAEEventID, OS.typeType, null, aetype.length*4, null, aetype) == OS.noErr) {
					//System.out.println("kEventParamAEEventID: " + MacUtil.toString(aetype[0]));
					if (aetype[0] == OS.kAEQuitApplication)
						close();
				}
			}
			
			EventRecord eventRecord= new EventRecord();
			OS.ConvertEventRefToEventRecord(eRefHandle, eventRecord);
			OS.AEProcessAppleEvent(eventRecord);
			break;
			
		case OS.kEventClassCommand:
			if (eventKind == OS.kEventProcessCommand) {
				HICommand command= new HICommand();
				OS.GetEventParameter(eRefHandle, OS.kEventParamDirectObject, OS.typeHICommand, null, HICommand.sizeof, null, command);
				if (command.commandID == OS.kAEQuitApplication) {
					close();
					OS.HiliteMenu((short)0);	// unhighlight what MenuSelect (or MenuKey) hilited
					return OS.noErr;
				}
				// try to map the MenuRef to a SWT Menu
				Widget w= findWidget (command.menu_menuRef);
				if (w instanceof Menu) {
					Menu menu= (Menu) w;
					menu.handleMenu(command.menu_menuItemIndex);
					OS.HiliteMenu((short)0);	// unhighlight what MenuSelect (or MenuKey) hilited
					return OS.noErr;
				}
				OS.HiliteMenu((short)0);	// unhighlight what MenuSelect (or MenuKey) hilited
				// we do not return noErr here so that the default handler
				// takes care of special menus like the Combo menu.
			}
			break;
		
		case OS.kEventClassMouse:
			switch (eventKind) {
				
			case OS.kEventMouseDown:	// clicks in menu bar
			
				fTrackedControl= 0;
				
				hideToolTip();
				
				MacEvent mEvent= new MacEvent(eRefHandle);
				org.eclipse.swt.internal.carbon.Point where= mEvent.getWhere();
				int[] w= new int[1];
				short part= OS.FindWindow(where, w);
				if (part == OS.inMenuBar) {
					org.eclipse.swt.internal.carbon.Point loc= mEvent.getWhere();
					OS.QDGlobalToLocalPoint(OS.GetWindowPort(w[0]), loc);
					OS.MenuSelect(loc);
					return OS.noErr;
				}
				break;
				
			case OS.kEventMouseDragged:
			case OS.kEventMouseUp:
			case OS.kEventMouseMoved:
				return handleMouseCallback(nextHandler, eRefHandle, 0);
			}
			break;
						
		case SWT_USER_EVENT:	// SWT1 user event
			//System.out.println("handleApplicationCallback: user event " + eventKind);
			return OS.noErr;
			
		default:
			System.out.println("handleApplicationCallback: unknown event class" + MacUtil.toString(eventClass));
			break;
		}
		return OS.eventNotHandledErr;
	}
	
	private int handleMouseCallback(int nextHandler, int eRefHandle, int whichWindow) {
		
		int eventClass= OS.GetEventClass(eRefHandle);
		if (eventClass != OS.kEventClassMouse) {
			System.out.println("handleMouseCallback: unexpected event class: " + MacUtil.toString(eventClass));
			return OS.eventNotHandledErr;
		}
				
		int eventKind= OS.GetEventKind(eRefHandle);

		if (eventKind == OS.kEventMouseDown) {
			fTrackedControl= 0;
		}
		
		MacEvent me= new MacEvent(eRefHandle);
		org.eclipse.swt.internal.carbon.Point where= me.getWhere();
		lastGlobalMouseXPos= where.h;
		lastGlobalMouseYPos= where.v;
		
		// retrieve window and window part from event
		if (whichWindow == 0) {
			if (fTrackedControl != 0) {
				// in tracking mode: get window from control
				whichWindow= OS.GetControlOwner(fTrackedControl);
			} else {
				int[] w= new int[1];
				OS.FindWindow(where, w);
				whichWindow= w[0];
				if (whichWindow == 0) {
					// try to retrieve window from event
					int rc= OS.GetEventParameter(eRefHandle, OS.kEventParamWindowRef, OS.typeWindowRef, null, 4, null, w);
					if (rc == OS.noErr)
						whichWindow= w[0];
					else {
						// the event is a MouseMoved event:
					}
				}
			}
		}
				
		if (whichWindow == 0) {
			// give up
			return OS.eventNotHandledErr;
		}
			
		MacEvent.trackStateMask(eRefHandle, eventKind);
				
		// determine control under mouse
		short[] cpart= new short[1];		
		int whichControl= MacUtil.findControlUnderMouse(whichWindow, me, cpart);				
		Widget widget= WidgetTable.get(whichControl);
		
		MacMouseEvent mme= fLastMouseEvent = new MacMouseEvent(me);
		
		switch (eventKind) {
		
		case OS.kEventMouseDown:			
			
			Shell shell= null;
			Widget w= findWidget(whichWindow);
			if (w instanceof Shell)
				shell= (Shell) w;
				
			// first click in window -> activation
			if (!OS.IsWindowActive(whichWindow)) {
				if (shell != null && (shell.getStyle() & SWT.ON_TOP) == 0) {
					// let the default handler activate the window
					break;
				}
			}
			
			// whatever we do, we hide the tooltip
			hideToolTip();

			// focus handling
			if (shell != null && (shell.getStyle() & SWT.ON_TOP) == 0)
				setMacFocusHandle(whichWindow, whichControl);
									
			if (whichControl != 0) {
			
				// deal with the context menu
				if (widget instanceof Control) {
					Menu cm= ((Control)widget).getMenu();	// is a context menu installed?
					if (cm != null && me.isShowContextualMenuClick()) {
						try {
							fInContextMenu= true;
							// AW: not ready for primetime
							// OS.ContextualMenuSelect(cm.handle, globalPos.getData(), new short[1], new short[1]);
							org.eclipse.swt.internal.carbon.Point pos= me.getWhere();
							OS.PopUpMenuSelect(cm.handle, pos.v, pos.h, (short)1);
						} finally {
							fInContextMenu= false;
						}
						return OS.noErr;
					}
				}
				
				if (cpart[0] == 111) { 	// a user pane
					if (!(widget instanceof Text)) 
						fTrackedControl= whichControl;	// starts mouse tracking
					windowProc(whichControl, SWT.MouseDown, mme);
					return OS.noErr;
				} else {
					windowProc(whichControl, SWT.MouseDown, mme);
				}
			}
			break;
		
		case OS.kEventMouseDragged:
			if (fTrackedControl != 0) {	// continue mouse tracking
				windowProc(fTrackedControl, SWT.MouseMove, mme);
				return OS.noErr;
			}
			break;

		case OS.kEventMouseUp:
			if (fTrackedControl != 0) {
				windowProc(fTrackedControl, SWT.MouseUp, mme);
				fTrackedControl= 0;		// continue mouse tracking
				return OS.noErr;
			}	
			break;
			
		case OS.kEventMouseMoved:
		
			fTrackedControl= 0;			
					
			if (fCurrentControl != whichControl) {
			
				if (fCurrentControl != 0) {
					fLastHoverHandle= 0;
					windowProc(fCurrentControl, SWT.MouseExit, mme);
				}
				
				fCurrentControl= whichControl;
				
				if (widget instanceof Control) {
					Control c= (Control) widget;
					if (c.cursor != null)
						c.cursor.install(this);	
					else
						setCursor(0);		
				} else
					setCursor(0);
				
				windowProc(fCurrentControl, SWT.MouseMove, mme);
				
				if (fCurrentControl != 0) {
					windowProc(fCurrentControl, SWT.MouseEnter, mme);
				}
				return OS.noErr;
				
			} else {
				if (fCurrentControl != 0) {
					windowProc(fCurrentControl, SWT.MouseMove, mme);
					return OS.noErr;
				}
			}
			break;
			
		case OS.kEventMouseWheelMoved:
			if (widget instanceof Composite) {
				ScrollBar sb= ((Composite) widget).getVerticalBar();
				if (sb != null)
					return sb.processWheel(eRefHandle);
			}
			break;
		}
					
		return OS.eventNotHandledErr;
	}

	void setMacFocusHandle(int wHandle, int focusHandle) {
	
		Widget w= findWidget(focusHandle);
		if (w == null) {
			int[] parent= new int[1];
			OS.GetSuperControl(focusHandle, parent);
			focusHandle= parent[0];
			w= findWidget(focusHandle);
			if (w == null)
				return;
		}

		if (fFocusControl != focusHandle) {
			int oldFocus= fFocusControl;
			fFocusControl= focusHandle;
			
			if (oldFocus != 0)
				windowProc(oldFocus, false);
			
			//fFocusControl= focusHandle;
			
			int[] focusControl= new int[1];
			OS.GetKeyboardFocus(wHandle, focusControl);
			if (focusControl[0] != fFocusControl) {
				OS.SetKeyboardFocus(wHandle, focusHandle, (short)-1);
				//if (rc != OS.noErr)
				//	System.out.println("Display.setMacFocusHandle: SetKeyboardFocus " + rc);
			}

			if (fFocusControl != 0)
				windowProc(fFocusControl, true);
		}
	}
		
	private void sendUserEvent(int kind) {
		int[] event= new int[1];
		OS.CreateEvent(0, SWT_USER_EVENT, kind, 0.0, OS.kEventAttributeUserEvent, event);
		if (event[0] != 0)
			OS.PostEventToQueue(OS.GetMainEventQueue(), event[0], (short)2);
	}
	
	static MacFont getThemeFont(int themeFontId) {
		byte[] fontName= new byte[256];
		short[] fontSize= new short[1];
		byte[] style= new byte[1];
		OS.GetThemeFont((short)themeFontId, (short)OS.smSystemScript, fontName, fontSize, style);
		return new MacFont(MacUtil.toString(fontName), fontSize[0], style[0]);
	}
	
	void menuIsVisible(boolean menuIsVisible) {
		fMenuIsVisible= menuIsVisible;
	}
	
	public void setCursor(int cursor) {
		if (fCurrentCursor != cursor) {
			fCurrentCursor= cursor;
			if (cursor == 0)
				OS.InitCursor();
			else
				OS.SetCursor(cursor);
		}
	}
	
	private int createCallback(String method, int argCount) {
		Callback cb= new Callback(this, method, argCount);
		if (fCallbacks == null)
			fCallbacks= new ArrayList();
		fCallbacks.add(cb);
		int proc= cb.getAddress();
		if (proc == 0)
			error (SWT.ERROR_NO_MORE_CALLBACKS);
		return proc;
	}
	
	private int testProc (int id, int clientData) {
		System.out.println("testProc");
		return 0;
	}
	
}

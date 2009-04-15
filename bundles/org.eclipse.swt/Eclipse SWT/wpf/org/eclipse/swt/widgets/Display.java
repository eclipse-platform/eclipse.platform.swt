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

import org.eclipse.swt.internal.wpf.*;
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

	int application, dispatcher, frame, jniRef, nameScope;
	boolean idle;
	int sleepOperation, operation;
	int operationCount;
	
	/* Windows and Events */
	Event [] eventQueue;
	EventTable eventTable, filterTable;
	
	int lastKey;
	char lastChar;
	boolean deadChar;

	/* Track Mouse Control */
	Control mouseControl;
	
	/* Focus */
	Control focusControl;

	/* Menus */
	Menu []  popups;

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread;

	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* System Tray */
	Tray tray;
	
	/* Timers */
	int timerHandler;
	int [] timerHandles;
	Runnable [] timerList;

//	/* System Resources */
	Font systemFont;
	Image errorImage, infoImage, questionImage, warningIcon;
	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];
	Color [] colors;

	/* Sort Indicators */
//	Image upArrow, downArrow;
	
	/* Color dialog custom dialgos */
	int customColors;
	
	/* Display Data */
	Object data;
	String [] keys;
	Object [] values;
	
	/* DragDetect */
	boolean dragging;
	int dragDetectFrame, dragRect, dragMouseDown;

	Control[] invalidate;
	int invalidateHandler;
	boolean ignoreRender;
	
	Shell [] shells;

	/* Key Mappings */
	static final int [] [] KeyTable = {
		
		/* Keyboard and Mouse Masks */
		{OS.Key_LeftAlt,	SWT.ALT},
		{OS.Key_RightAlt,	SWT.ALT},
		{OS.Key_LeftShift,	SWT.SHIFT},
		{OS.Key_RightShift,	SWT.SHIFT},
		{OS.Key_LeftCtrl,	SWT.CONTROL},
		{OS.Key_RightCtrl,	SWT.CONTROL},
//		{OS.VK_????,	SWT.COMMAND},

		/* NOT CURRENTLY USED */		
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},
		
		/* Non-Numeric Keypad Keys */
		{OS.Key_Up,		SWT.ARROW_UP},
		{OS.Key_Down,	SWT.ARROW_DOWN},
		{OS.Key_Left,	SWT.ARROW_LEFT},
		{OS.Key_Right,	SWT.ARROW_RIGHT},
		{OS.Key_PageUp,	SWT.PAGE_UP},
		{OS.Key_PageDown,	SWT.PAGE_DOWN},
		{OS.Key_Home,	SWT.HOME},
		{OS.Key_End,		SWT.END},
		{OS.Key_Insert,	SWT.INSERT},

		/* Virtual and Ascii Keys */
		{OS.Key_Back,	SWT.BS},
		{OS.Key_Return,	SWT.CR},
		{OS.Key_Delete,	SWT.DEL},
		{OS.Key_Escape,	SWT.ESC},
		{OS.Key_Return,	SWT.LF},
		{OS.Key_Tab,		SWT.TAB},
	
		/* Functions Keys */
		{OS.Key_F1,	SWT.F1},
		{OS.Key_F2,	SWT.F2},
		{OS.Key_F3,	SWT.F3},
		{OS.Key_F4,	SWT.F4},
		{OS.Key_F5,	SWT.F5},
		{OS.Key_F6,	SWT.F6},
		{OS.Key_F7,	SWT.F7},
		{OS.Key_F8,	SWT.F8},
		{OS.Key_F9,	SWT.F9},
		{OS.Key_F10,	SWT.F10},
		{OS.Key_F11,	SWT.F11},
		{OS.Key_F12,	SWT.F12},
		{OS.Key_F13,	SWT.F13},
		{OS.Key_F14,	SWT.F14},
		{OS.Key_F15,	SWT.F15},
		
		/* Numeric Keypad Keys */
		{OS.Key_Multiply,	SWT.KEYPAD_MULTIPLY},
		{OS.Key_Add,			SWT.KEYPAD_ADD},
		{OS.Key_Return,		SWT.KEYPAD_CR},
		{OS.Key_Subtract,	SWT.KEYPAD_SUBTRACT},
		{OS.Key_Decimal,		SWT.KEYPAD_DECIMAL},
		{OS.Key_Divide,		SWT.KEYPAD_DIVIDE},
		{OS.Key_NumPad0,		SWT.KEYPAD_0},
		{OS.Key_NumPad1,		SWT.KEYPAD_1},
		{OS.Key_NumPad2,		SWT.KEYPAD_2},
		{OS.Key_NumPad3,		SWT.KEYPAD_3},
		{OS.Key_NumPad4,		SWT.KEYPAD_4},
		{OS.Key_NumPad5,		SWT.KEYPAD_5},
		{OS.Key_NumPad6,		SWT.KEYPAD_6},
		{OS.Key_NumPad7,		SWT.KEYPAD_7},
		{OS.Key_NumPad8,		SWT.KEYPAD_8},
		{OS.Key_NumPad9,		SWT.KEYPAD_9},
//		{OS.VK_????,		SWT.KEYPAD_EQUAL},

		/* Other keys */
		{OS.Key_CapsLock,		SWT.CAPS_LOCK},
		{OS.Key_NumLock,		SWT.NUM_LOCK},
		{OS.Key_Scroll,		SWT.SCROLL_LOCK},
		{OS.Key_Pause,		SWT.PAUSE},
		{OS.Key_Cancel,		SWT.BREAK},
		{OS.Key_PrintScreen,	SWT.PRINT_SCREEN},
//		{OS.VK_????,		SWT.HELP},
		

		{OS.Key_D0, '0'},
		{OS.Key_D1, '1'},
		{OS.Key_D2, '2'},
		{OS.Key_D3, '3'},
		{OS.Key_D4, '4'},
		{OS.Key_D5, '5'},
		{OS.Key_D6, '6'},
		{OS.Key_D7, '7'},
		{OS.Key_D8, '8'},
		{OS.Key_D9, '9'},
		{OS.Key_A, 'a'},
		{OS.Key_B, 'b'},
		{OS.Key_C, 'c'},
		{OS.Key_D, 'd'},
		{OS.Key_E, 'e'},
		{OS.Key_F, 'f'},
		{OS.Key_G, 'g'},
		{OS.Key_H, 'h'},
		{OS.Key_I, 'i'},
		{OS.Key_J, 'j'},
		{OS.Key_K, 'k'},
		{OS.Key_L, 'l'},
		{OS.Key_M, 'm'},
		{OS.Key_N, 'n'},
		{OS.Key_O, 'o'},
		{OS.Key_P, 'p'},
		{OS.Key_Q, 'q'},
		{OS.Key_R, 'r'},
		{OS.Key_S, 's'},
		{OS.Key_T, 't'},
		{OS.Key_U, 'u'},
		{OS.Key_V, 'v'},
		{OS.Key_W, 'w'},
		{OS.Key_X, 'x'},
		{OS.Key_Y, 'y'},
		{OS.Key_Z, 'z'},
		
		{OS.Key_OemTilde, '`'},
		{OS.Key_OemMinus, '-'},
		{OS.Key_OemPlus, '='},
		{OS.Key_Oem4, '['},
		{OS.Key_Oem6, ']'},
		{OS.Key_OemPipe, '\\'},
		{OS.Key_OemSemicolon, ';'},
		{OS.Key_Oem7, '\''},
		{OS.Key_OemComma, ','},
		{OS.Key_OemPeriod, '.'},
		{OS.Key_Oem2, '/'},
		
	};

	/* Multiple Displays */
	static Display Default;
	static Display [] Displays = new Display [4];

	/* Multiple Monitors */
	static Monitor[] monitors = null;
	static int monitorCount = 0;
	
	/* Modality */
	Shell [] modalShells;
//	Shell modalDialogShell;

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
	int focusedElement = OS.Keyboard_FocusedElement ();
	Control control = null;
	if (focusedElement != 0) {
		Widget widget = getWidget (focusedElement);
		if (widget instanceof Menu) {
			Shell shell = ((Menu)widget).getShell();
			OS.GCHandle_Free (focusedElement);
			focusedElement = OS.FocusManager_GetFocusedElement (shell.shellHandle);
			if (focusedElement == 0) return null;
			widget = getWidget (focusedElement);
		}
		if (widget != null) control = widget.getWidgetControl ();
		OS.GCHandle_Free (focusedElement);
	}
	return control;
}

void addWidget (int handle, Widget widget) {
	if (handle == 0) return;
	int tag = OS.gcnew_IntPtr (widget.jniRef);
	OS.FrameworkElement_Tag (handle, tag);
	OS.GCHandle_Free (tag);
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

void addInvalidate (Control control) {
	if (invalidate == null) invalidate = new Control [4];
	int length = invalidate.length;
	for (int i=0; i<length; i++) {
		if (invalidate [i] == control) return;
	}
	int index = 0;
	while (index < length) {
		if (invalidate [index] == null) break;
		index++;
	}
	if (index == length) {
		Control [] temp = new Control [length + 4];
		System.arraycopy (invalidate, 0, temp, 0, length);
		invalidate = temp;
	}
	invalidate [index] = control;
	if (invalidateHandler == 0) {
		int handler = invalidateHandler = OS.gcnew_NoArgsDelegate (jniRef, "invalidateHandler");
		int operation = OS.Dispatcher_BeginInvoke (dispatcher, OS.DispatcherPriority_Send, handler);
		OS.GCHandle_Free (operation);
		OS.GCHandle_Free (handler);
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
	eventTable.hook (eventType, listener);
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

void addShell (Shell shell) {
	if (shells == null) shells = new Shell [4];
	int length = shells.length;
	for (int i=0; i<length; i++) {
		if (shells [i] == shell) return;
	}
	int index = 0;
	while (index < length) {
		if (shells [index] == null) break;
		index++;
	}
	if (index == length) {
		Shell [] temp = new Shell [length + 4];
		System.arraycopy (shells, 0, temp, 0, length);
		shells = temp;
	}
	shells [index] = shell;
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
	OS.Console_Beep ();
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
	checkDisplay (thread = Thread.currentThread (), true);
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
	Win32.OleInitialize (0);
	application = OS.gcnew_Application();
	if (application == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.Application_ShutdownMode (application, OS.ShutdownMode_OnExplicitShutdown);
	nameScope = OS.gcnew_NameScope ();
	if (nameScope == 0) SWT.error (SWT.ERROR_NO_HANDLES);
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
	OS.GCHandle_Dump();
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
	int windows = OS.Application_Windows (application);
	int count = OS.WindowCollection_Count (windows);
	int activeWindow = 0;
	if (count != 0) {
		int enumerator = OS.WindowCollection_GetEnumerator (windows);
		while (OS.IEnumerator_MoveNext (enumerator)) {
			int window = OS.WindowCollection_Current (enumerator);
			if (OS.Window_IsActive (window)) {
				activeWindow = window;
				break;
			}
			OS.GCHandle_Free (window);
		}
		OS.GCHandle_Free (enumerator);
	}
	OS.GCHandle_Free (windows);
	Shell shell = (Shell)getWidget (activeWindow);
	if (activeWindow != 0) OS.GCHandle_Free (activeWindow);
	return shell;
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
	int x = (int) OS.SystemParameters_VirtualScreenLeft ();
	int y = (int) OS.SystemParameters_VirtualScreenTop ();
	int width = (int) OS.SystemParameters_VirtualScreenWidth ();
	int height = (int) OS.SystemParameters_VirtualScreenHeight ();
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
//	if (true/*OS.GetSystemMetrics (OS.SM_CMONITORS) < 2*/) {
		int rect = OS.SystemParameters_WorkArea ();
		int x = (int) OS.Rect_X (rect);
		int y = (int) OS.Rect_Y (rect);
		int width = (int) OS.Rect_Width (rect);
		int height = (int) OS.Rect_Height (rect);
		OS.GCHandle_Free (rect);
		return new Rectangle (x, y, width, height);
//	}
//	int x = (int) OS.SystemParameters_VirtualScreenLeft ();
//	int y = (int) OS.SystemParameters_VirtualScreenTop ();
//	int width = (int) OS.SystemParameters_VirtualScreenWidth ();
//	int height = (int) OS.SystemParameters_VirtualScreenHeight ();
//	return new Rectangle (x, y, width, height);
}

Widget getWidget (int handle) {
	if (handle == 0) return null;
	int frameworkElementType = OS.FrameworkElement_typeid ();
	int frameworkContentElementType = OS.FrameworkContentElement_typeid ();
	int widget = handle;
	int jniRef = 0;
	do {
		int parent = 0;
		if (OS.Type_IsInstanceOfType (frameworkElementType, widget)) {
			int tag = OS.FrameworkElement_Tag (widget);
			if (tag != 0) {
				jniRef = OS.IntPtr_ToInt32 (tag);
				OS.GCHandle_Free (tag);
				break;
			}
			parent = OS.FrameworkElement_Parent (widget);
			if (parent == 0) { 
				parent = OS.VisualTreeHelper_GetParent (widget);
			}
		} else {
			if (OS.Type_IsInstanceOfType (frameworkContentElementType, widget)) {
				parent = OS.FrameworkContentElement_Parent (widget);
			}
		}
		if (widget != handle) OS.GCHandle_Free (widget);
		widget = parent;
	} while (widget != 0);
	if (widget != handle && widget != 0) OS.GCHandle_Free (widget);
	OS.GCHandle_Free (frameworkElementType);
	OS.GCHandle_Free (frameworkContentElementType);
	return jniRef != 0 ? (Widget) OS.JNIGetObject (jniRef) : null;
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
	int inputElement = 0;
	int captured = OS.Mouse_Captured ();
	if (captured != 0) {
		int sources = OS.PresentationSource_CurrentSources ();
		int enumerator = OS.IEnumerable_GetEnumerator (sources);
		while (OS.IEnumerator_MoveNext (enumerator) && inputElement == 0) {
			int current = OS.IEnumerator_Current (enumerator);
			int root = OS.PresentationSource_RootVisual (current);
			if (root != 0) {
				int pt = OS.Mouse_GetPosition (root);
				inputElement = OS.UIElement_InputHitTest (root, pt);
				OS.GCHandle_Free (pt);
				OS.GCHandle_Free (root);
			}
			OS.GCHandle_Free (current);
		}
		OS.GCHandle_Free (enumerator);
		OS.GCHandle_Free (sources);
		OS.GCHandle_Free (captured);
	} else {
		inputElement = OS.Mouse_DirectlyOver ();
	}
	if (inputElement != 0) {
		Widget widget = getWidget (inputElement);
		OS.GCHandle_Free (inputElement);
		if (widget != null) return widget.getWidgetControl ();
	}
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point getCursorLocation () {
	checkDevice ();	
	POINT pt = new POINT ();
	Win32.GetCursorPos (pt);
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
//	return new Point [] {
//		new Point (OS.GetSystemMetrics (OS.SM_CXCURSOR), OS.GetSystemMetrics (OS.SM_CYCURSOR))};
	return null;
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
//	if (key.equals (RUN_MESSAGES_IN_IDLE_KEY)) {
//		return new Boolean (runMessagesInIdle);
//	}
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
	//TODO
	return 500;
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
	return _getFocusControl ();
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
	return OS.SystemParameters_HighContrast ();
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
//	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
//		if (getDepth () >= 24) return 32;
//	}
//
//	/* Use the character encoding for the default locale */
//	TCHAR buffer1 = new TCHAR (0, "Control Panel\\Desktop\\WindowMetrics", true); //$NON-NLS-1$
//
//	int [] phkResult = new int [1];
//	int result = OS.RegOpenKeyEx (OS.HKEY_CURRENT_USER, buffer1, 0, OS.KEY_READ, phkResult);
//	if (result != 0) return 4;
	int depth = 4;
//	int [] lpcbData = new int [1];
//	
//	/* Use the character encoding for the default locale */
//	TCHAR buffer2 = new TCHAR (0, "Shell Icon BPP", true); //$NON-NLS-1$
//	result = OS.RegQueryValueEx (phkResult [0], buffer2, 0, null, (TCHAR) null, lpcbData);
//	if (result == 0) {
//		TCHAR lpData = new TCHAR (0, lpcbData [0] / TCHAR.sizeof);
//		result = OS.RegQueryValueEx (phkResult [0], buffer2, 0, null, lpData, lpcbData);
//		if (result == 0) {
//			try {
//				depth = Integer.parseInt (lpData.toString (0, lpData.strlen ()));
//			} catch (NumberFormatException e) {}
//		}
//	}
//	OS.RegCloseKey (phkResult [0]);
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
	//TODO
//	return new Point [] {
//		new Point (OS.GetSystemMetrics (OS.SM_CXSMICON), OS.GetSystemMetrics (OS.SM_CYSMICON)),
//		new Point (OS.GetSystemMetrics (OS.SM_CXICON), OS.GetSystemMetrics (OS.SM_CYICON)),
//	};	
	return null;
}

int getLastEventTime () {
	//TODO - use OS
	return (int)System.currentTimeMillis();
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

//Shell getModalDialogShell () {
//	if (modalDialogShell != null && modalDialogShell.isDisposed ()) modalDialogShell = null;
//	return modalDialogShell;
//}

/**
 * Returns an array of monitors attached to the device.
 * 
 * @return the array of monitors
 * 
 * @since 3.0
 */
public Monitor [] getMonitors () {
	checkDevice ();
	int screens = OS.Screen_AllScreens ();
	if (screens == 0) error (SWT.ERROR_NO_HANDLES);
	int screenCount = OS.ICollection_Count (screens);
	Monitor [] monitors = new Monitor [screenCount];
	for (int i=0; i<screenCount; i++) {
		int screen = OS.IList_default (screens, i);
		int bounds = OS.Screen_Bounds (screen);
		int workingArea = OS.Screen_WorkingArea (screen);
		Monitor monitor = new Monitor ();
		monitor.x = OS.Rectangle_X (bounds); 
		monitor.y = OS.Rectangle_Y (bounds);
		monitor.width = OS.Rectangle_Width (bounds);
		monitor.height = OS.Rectangle_Height (bounds); 
		monitor.clientX = OS.Rectangle_X (workingArea);
		monitor.clientY = OS.Rectangle_Y (workingArea);
		monitor.clientWidth = OS.Rectangle_Width (workingArea);
		monitor.clientHeight = OS.Rectangle_Height (workingArea);
		monitors [i] = monitor;
		OS.GCHandle_Free (workingArea);
		OS.GCHandle_Free (bounds);
		OS.GCHandle_Free (screen);
	}
	OS.GCHandle_Free (screens);
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
	int screen = OS.Screen_PrimaryScreen ();
	if (screen == 0) error (SWT.ERROR_NO_HANDLES);
	int bounds = OS.Screen_Bounds (screen);
	int workingArea = OS.Screen_WorkingArea (screen);
	Monitor monitor = new Monitor ();
	monitor.x = OS.Rectangle_X (bounds); 
	monitor.y = OS.Rectangle_Y (bounds);
	monitor.width = OS.Rectangle_Width (bounds);
	monitor.height = OS.Rectangle_Height (bounds); 
	monitor.clientX = OS.Rectangle_X (workingArea);
	monitor.clientY = OS.Rectangle_Y (workingArea);
	monitor.clientWidth = OS.Rectangle_Width (workingArea);
	monitor.clientHeight = OS.Rectangle_Height (workingArea);
	OS.GCHandle_Free (workingArea);
	OS.GCHandle_Free (bounds);
	OS.GCHandle_Free (screen);
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
	if (shells == null) return new Shell [0];
	int length = 0;
	for (int i=0; i<shells.length; i++) {
		if (shells [i] != null) length++;
	}
	int index = 0;
	Shell [] result = new Shell [length];
	for (int i=0; i<shells.length; i++) {
		Shell widget = shells [i];
		if (widget != null) result [index++] = widget;
	}
	return result;
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
	Color color = null;
	if (0 <= id && id < colors.length) {
		color = colors [id];
	}
	return color != null ? color : super.getSystemColor (id);
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
		case SWT.ICON_ERROR: {
			if (errorImage != null) return errorImage;
			int hIcon = Win32.LoadImage (0, Win32.OIC_HAND, Win32.IMAGE_ICON, 0, 0, Win32.LR_SHARED);
			int empty = OS.Int32Rect_Empty ();
			int source = OS.Imaging_CreateBitmapSourceFromHIcon (hIcon, empty, 0);
			errorImage = Image.wpf_new (this, SWT.BITMAP, source);
			OS.GCHandle_Free (empty);
			Win32.DestroyIcon (hIcon);
			return errorImage;
		}
		case SWT.ICON_WORKING:
		case SWT.ICON_INFORMATION: {
			if (infoImage != null) return infoImage;
			int hIcon = Win32.LoadImage (0, Win32.OIC_INFORMATION, Win32.IMAGE_ICON, 0, 0, Win32.LR_SHARED);
			int empty = OS.Int32Rect_Empty ();
			int source = OS.Imaging_CreateBitmapSourceFromHIcon (hIcon, empty, 0);
			infoImage = Image.wpf_new (this, SWT.BITMAP, source);
			OS.GCHandle_Free (empty);
			Win32.DestroyIcon (hIcon);
			return infoImage;
		}
		case SWT.ICON_QUESTION: {
			if (questionImage != null) return questionImage;
			int hIcon = Win32.LoadImage (0, Win32.OIC_QUES, Win32.IMAGE_ICON, 0, 0, Win32.LR_SHARED);
			int empty = OS.Int32Rect_Empty ();
			int source = OS.Imaging_CreateBitmapSourceFromHIcon (hIcon, empty, 0);
			questionImage = Image.wpf_new (this, SWT.BITMAP, source);
			OS.GCHandle_Free (empty);
			Win32.DestroyIcon (hIcon);
			return questionImage;
		}
		case SWT.ICON_WARNING: {
			if (warningIcon != null) return warningIcon;
			int hIcon = Win32.LoadImage (0, Win32.OIC_BANG, Win32.IMAGE_ICON, 0, 0, Win32.LR_SHARED);
			int empty = OS.Int32Rect_Empty ();
			int source = OS.Imaging_CreateBitmapSourceFromHIcon (hIcon, empty, 0);
			warningIcon = Image.wpf_new (this, SWT.BITMAP, source);
			OS.GCHandle_Free (empty);
			Win32.DestroyIcon (hIcon);
			return warningIcon;
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
	tray = new Tray (this, SWT.NONE);
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
	//TODO - for now, return a drawing context that can measure text
	if (data == null) return 0;
	int visual = OS.gcnew_DrawingVisual();
	if (visual == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int dc = OS.DrawingVisual_RenderOpen (visual);
	if (dc == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	data.visual = visual;
	data.font = getSystemFont ();
	return dc;
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
		
	dispatcher = OS.Application_Dispatcher (application);
	if (dispatcher == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	frame = OS.gcnew_DispatcherFrame ();
	if (frame == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	jniRef = OS.NewGlobalRef (this);
	if (jniRef == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int hooks = OS.Dispatcher_Hooks (dispatcher);
	int handler = OS.gcnew_EventHandler (jniRef, "HandleDispatcherInactive");
	OS.DispatcherHooks_DispatcherInactive (hooks, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_DispatcherHookEventHandler (jniRef, "HandleOperationCompleted");
	OS.DispatcherHooks_OperationCompleted (hooks, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_DispatcherHookEventHandler (jniRef, "HandleOperationPosted");
	OS.DispatcherHooks_OperationPosted (hooks, handler);
	OS.GCHandle_Free (handler);
	OS.GCHandle_Free (hooks);
	timerHandler = OS.gcnew_TimerHandler(jniRef, "timerProc");
	

	/* Create the standard colors */
	colors = new Color [SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT + 1];
	colors [SWT.COLOR_WIDGET_DARK_SHADOW] = Color.wpf_new( this, OS.SystemColors_ControlDarkDarkColor ());
	colors [SWT.COLOR_WIDGET_NORMAL_SHADOW] = Color.wpf_new (this, OS.SystemColors_ControlDarkColor ());
	colors [SWT.COLOR_WIDGET_LIGHT_SHADOW] = Color.wpf_new (this, OS.SystemColors_ControlLightColor ());
	colors [SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW] = Color.wpf_new (this, OS.SystemColors_ControlLightLightColor ());
	colors [SWT.COLOR_WIDGET_FOREGROUND] = Color.wpf_new (this, OS.SystemColors_ControlTextColor ());
	colors [SWT.COLOR_WIDGET_BACKGROUND] = Color.wpf_new (this, OS.SystemColors_ControlColor ());
	colors [SWT.COLOR_WIDGET_BORDER] = Color.wpf_new (this, OS.SystemColors_ActiveBorderColor ());
	colors [SWT.COLOR_LIST_FOREGROUND] = Color.wpf_new (this, OS.SystemColors_WindowTextColor ());
	colors [SWT.COLOR_LIST_BACKGROUND] = Color.wpf_new (this, OS.SystemColors_WindowColor ());
	colors [SWT.COLOR_LIST_SELECTION] = Color.wpf_new (this, OS.SystemColors_HighlightColor ());
	colors [SWT.COLOR_LIST_SELECTION_TEXT] = Color.wpf_new (this, OS.SystemColors_HighlightTextColor ());
	colors [SWT.COLOR_INFO_FOREGROUND] = Color.wpf_new (this, OS.SystemColors_InfoTextColor ());
	colors [SWT.COLOR_INFO_BACKGROUND] = Color.wpf_new (this, OS.SystemColors_InfoColor ());
	colors [SWT.COLOR_TITLE_FOREGROUND] = Color.wpf_new (this, OS.SystemColors_ActiveCaptionTextColor ());
	colors [SWT.COLOR_TITLE_BACKGROUND] = Color.wpf_new (this, OS.SystemColors_ActiveCaptionColor ());
	colors [SWT.COLOR_TITLE_BACKGROUND_GRADIENT] = Color.wpf_new (this, OS.SystemColors_GradientActiveCaptionColor ());
	colors [SWT.COLOR_TITLE_INACTIVE_FOREGROUND] = Color.wpf_new (this, OS.SystemColors_InactiveCaptionTextColor ());
	colors [SWT.COLOR_TITLE_INACTIVE_BACKGROUND] = Color.wpf_new (this, OS.SystemColors_InactiveCaptionColor ());
	colors [SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT] = Color.wpf_new (this, OS.SystemColors_GradientInactiveCaptionColor ());
		
}

void invalidateHandler () {
	invalidateHandler = 0;
	if (invalidate != null) {
		Control[] invalidate = this.invalidate;
		this.invalidate = null;
		for (int i = 0; i < invalidate.length; i++) {
			Control control = invalidate [i];
			if (control != null && !control.isDisposed()) {
				control.redraw (true);
			}
		}
	}
}

void HandleDispatcherInactive (int sender, int e) {
	if (runAsyncMessages (false)) wakeThread ();
}

void HandleOperationCompleted (int sender, int e) {
	if (operation != 0) {
		int current = OS.DispatcherHookEventArgs_Operation(e);
		int priority = OS.DispatcherOperation_Priority(current);
		if (priority == 5) OS.DispatcherOperation_Abort(operation);
		OS.GCHandle_Free(current);
	}
}

void HandleOperationPosted (int sender, int e) {
	if (sleepOperation != 0) OS.DispatcherOperation_Priority(sleepOperation, OS.DispatcherPriority_Send);
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
public void internal_dispose_GC (int dc, GCData data) {
	//TODO - the wrong drawing context was created
	if (data != null && data.drawingContext == 0) {
		OS.DrawingContext_Close (dc);
		OS.GCHandle_Free (dc);
		OS.GCHandle_Free (data.visual);
	}
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
	if (from != null && from.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (to != null && to.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (from == to) return new Point (x, y);
	int newX, newY;
	if (from != null && to != null) {
		int point = OS.gcnew_Point (x, y);
		int newPoint = OS.UIElement_TranslatePoint (from.handle, point, to.handle);
		newX = (int) (OS.Point_X (newPoint) + 0.5);
		newY = (int) (OS.Point_Y (newPoint) + 0.5);
		OS.GCHandle_Free (point);
		OS.GCHandle_Free (newPoint);
	} else {
		if (from == null) {
			int toHandle = to.handle;
			to.updateLayout (toHandle);
			int source = OS.PresentationSource_FromVisual (toHandle);
			int window = OS.PresentationSource_RootVisual (source);
			OS.GCHandle_Free (source);
			int point = OS.gcnew_Point (x, y);
			int temp = OS.Visual_PointFromScreen (window, point);
			int newPoint = OS.UIElement_TranslatePoint (window, temp, toHandle);
			newX = (int) OS.Point_X (newPoint);
			newY = (int) OS.Point_Y (newPoint);
			OS.GCHandle_Free (temp);
			OS.GCHandle_Free (point);
			OS.GCHandle_Free (newPoint);
			OS.GCHandle_Free (window);
		} else {
			int fromHandle = from.handle;
			from.updateLayout (fromHandle);
			int source = OS.PresentationSource_FromVisual (fromHandle);
			int window = OS.PresentationSource_RootVisual (source);
			OS.GCHandle_Free (source);
			int point = OS.gcnew_Point (x, y);
			int temp = OS.UIElement_TranslatePoint (fromHandle, point, window);
			int newPoint = OS.Visual_PointToScreen (window, temp);
			newX = (int) OS.Point_X (newPoint);
			newY = (int) OS.Point_Y (newPoint);
			OS.GCHandle_Free (temp);
			OS.GCHandle_Free (point);
			OS.GCHandle_Free (newPoint);
			OS.GCHandle_Free (window);
		}
	}
	return new Point (newX, newY);
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
	//TODO
	Point point = map (from, to, x, y);
	return new Rectangle (point.x, point.y, width, height);
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
public boolean post (Event event) {
	synchronized (Device.class) {
		if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
		if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
//		int type = event.type;
//		switch (type){
//			case SWT.KeyDown:
//			case SWT.KeyUp: {
//				KEYBDINPUT inputs = new KEYBDINPUT ();
//				inputs.wVk = (short) untranslateKey (event.keyCode);
//				if (inputs.wVk == 0) {
//					char key = event.character;
//					switch (key) {
//						case SWT.BS: inputs.wVk = (short) OS.VK_BACK; break;
//						case SWT.CR: inputs.wVk = (short) OS.VK_RETURN; break;
//						case SWT.DEL: inputs.wVk = (short) OS.VK_DELETE; break;
//						case SWT.ESC: inputs.wVk = (short) OS.VK_ESCAPE; break;
//						case SWT.TAB: inputs.wVk = (short) OS.VK_TAB; break;
//						/*
//						* Since there is no LF key on the keyboard, do not attempt
//						* to map LF to CR or attempt to post an LF key.
//						*/
//	//					case SWT.LF: inputs.wVk = (short) OS.VK_RETURN; break;
//						case SWT.LF: return false;
//						default: {
//							if (OS.IsWinCE) {
//								inputs.wVk = OS.CharUpper ((short) key);
//							} else {
//								inputs.wVk = OS.VkKeyScan ((short) wcsToMbcs (key, 0));
//								if (inputs.wVk == -1) return false;
//								inputs.wVk &= 0xFF;
//							}
//						}
//					}
//				}
//				inputs.dwFlags = type == SWT.KeyUp ? OS.KEYEVENTF_KEYUP : 0;
//				int hHeap = OS.GetProcessHeap ();
//				int pInputs = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, INPUT.sizeof);
//				OS.MoveMemory(pInputs, new int[] {OS.INPUT_KEYBOARD}, 4);
//				OS.MoveMemory (pInputs + 4, inputs, KEYBDINPUT.sizeof);
//				boolean result = OS.SendInput (1, pInputs, INPUT.sizeof) != 0;
//				OS.HeapFree (hHeap, 0, pInputs);
//				return result;
//			}
//			case SWT.MouseDown:
//			case SWT.MouseMove: 
//			case SWT.MouseUp: {
//				MOUSEINPUT inputs = new MOUSEINPUT ();
//				if (type == SWT.MouseMove){
//					inputs.dwFlags = OS.MOUSEEVENTF_MOVE | OS.MOUSEEVENTF_ABSOLUTE;
//					inputs.dx = event.x * 65535 / (OS.GetSystemMetrics (OS.SM_CXSCREEN) - 1);
//					inputs.dy = event.y * 65535 / (OS.GetSystemMetrics (OS.SM_CYSCREEN) - 1);
//				} else {
//					switch (event.button) {
//						case 1: inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_LEFTDOWN : OS.MOUSEEVENTF_LEFTUP; break;
//						case 2: inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_MIDDLEDOWN : OS.MOUSEEVENTF_MIDDLEUP; break;
//						case 3: inputs.dwFlags = type == SWT.MouseDown ? OS.MOUSEEVENTF_RIGHTDOWN : OS.MOUSEEVENTF_RIGHTUP; break;
//						default: return false;
//					}
//				}
//				int hHeap = OS.GetProcessHeap ();
//				int pInputs = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, INPUT.sizeof);
//				OS.MoveMemory(pInputs, new int[] {OS.INPUT_MOUSE}, 4);
//				OS.MoveMemory (pInputs + 4, inputs, MOUSEINPUT.sizeof);
//				boolean result = OS.SendInput (1, pInputs, INPUT.sizeof) != 0;
//				OS.HeapFree (hHeap, 0, pInputs);
//				return result;
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

void removePopup (Menu menu) {
	if (popups == null) return;
	for (int i=0; i<popups.length; i++) {
		if (popups [i] == menu) {
			popups [i] = null;
			return;
		}
	}
}

void removeShell (Shell shell) {
	if (shells == null) return;
	for (int i=0; i<shells.length; i++) {
		if (shells [i] == shell) {
			shells [i] = null;
			return;
		}
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
	runPopups ();
	idle = false;
	int handler = OS.gcnew_NoArgsDelegate(jniRef, "setIdleHandler");
	operation = OS.Dispatcher_BeginInvoke(dispatcher, 2, handler);
	OS.DispatcherOperation_Wait(operation);
	OS.GCHandle_Free(handler);
	OS.GCHandle_Free(operation);
	operation = 0;
	if (!idle) {
		runDeferredEvents();
		return true;
	}
	return isDisposed () || runAsyncMessages(false);
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
	
	//hack... readAndDispatch Always returns true right now, prevents apps from shutting down.
	//while (readAndDispatch ()) {}
	
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
	/* Release the timers */
	OS.GCHandle_Free (timerHandler);
	timerHandler = 0;
	if (timerHandles != null) {
		for (int i = 0; i < timerHandles.length; i++) {
			int timer = timerHandles [i];
			if (timer != 0) {
				OS.DispatcherTimer_Stop (timer);
				OS.GCHandle_Free (timer);
			}
		}
	}
	timerHandles = null;
	timerList = null;
	if (nameScope != 0) OS.GCHandle_Free (nameScope);
	nameScope = 0;
	if (application != 0) {
		OS.Application_Shutdown (application);
		OS.GCHandle_Free (application);
	}
	application = 0;
	if (dispatcher != 0) OS.GCHandle_Free (dispatcher);
	dispatcher = 0;
	if (frame != 0) OS.GCHandle_Free (frame);
	frame = 0;
	if (jniRef != 0) OS.DeleteGlobalRef (jniRef);
	jniRef = 0;
	
	for (int i = 0; i < colors.length; i++) {
		if (colors [i] != null) colors [i].dispose ();
	}
	colors = null;
	
//	/* Release the System fonts */
//	if (systemFont != null) systemFont.dispose ();
//	systemFont = null;
//	lfSystemFont = null;
		
	/* Release the System Images */
	if (errorImage != null) errorImage.dispose ();
	if (infoImage != null) infoImage.dispose ();
	if (questionImage != null) questionImage.dispose ();
	if (warningIcon != null) warningIcon.dispose ();
	errorImage = infoImage = questionImage = warningIcon = null;
	
	/* Release the System Cursors */
	for (int i = 0; i < cursors.length; i++) {
		if (cursors [i] != null) cursors [i].dispose ();
	}
	cursors = null;
	
//	/* Release Acquired Resources */
//	if (resources != null) {
//		for (int i=0; i<resources.length; i++) {
//			if (resources [i] != null) resources [i].dispose ();
//		}
//		resources = null;
//	}
//
	/* Release Custom Colors for ChooseColor */
	if (customColors != 0) OS.GCHandle_Free (customColors);
	customColors = 0;

	/* Release references */
	thread = null;
//	modalDialogShell = null;
	modalShells = null;
	data = null;
	keys = null;
	values = null;
	popups = null;
	mouseControl = null;
	shells = null;
	eventTable = filterTable = null;

	/* Uninitialize OLE */
	Win32.OleUninitialize ();
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

Widget removeWidget (int handle) {
	if (handle == 0) return null;
	int tag = OS.FrameworkElement_Tag (handle), ref = 0;
	if (tag != 0) {
		ref = OS.IntPtr_ToInt32(tag);
		OS.GCHandle_Free(tag);
	}
	Widget widget = null;
	if (ref != 0) {
		OS.FrameworkElement_Tag (handle, 0);
		widget = (Widget) OS.JNIGetObject (ref);
	}
	return widget;
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
		if (!menu.isDisposed()) menu._setVisible (true);
		result = true;
	}
	popups = null;
	return result;
}

void runSettings () {
	Font oldFont = getSystemFont ();
	saveResources ();
//	updateImages ();
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

void saveResources () {
//	int resourceCount = 0;
//	if (resources == null) {
//		resources = new Resource [RESOURCE_SIZE];
//	} else {
//		resourceCount = resources.length;
//		Resource [] newResources = new Resource [resourceCount + RESOURCE_SIZE];
//		System.arraycopy (resources, 0, newResources, 0, resourceCount);
//		resources = newResources;
//	}
//	if (systemFont != null) {
//		if (!OS.IsWinCE) {
//			NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
//			info.cbSize = NONCLIENTMETRICS.sizeof;
//			if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
//				LOGFONT logFont = OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfMessageFont : ((NONCLIENTMETRICSA)info).lfMessageFont;
//				if (lfSystemFont == null ||
//					logFont.lfCharSet != lfSystemFont.lfCharSet ||
//					logFont.lfHeight != lfSystemFont.lfHeight ||
//					logFont.lfWidth != lfSystemFont.lfWidth ||
//					logFont.lfEscapement != lfSystemFont.lfEscapement ||
//					logFont.lfOrientation != lfSystemFont.lfOrientation ||
//					logFont.lfWeight != lfSystemFont.lfWeight ||
//					logFont.lfItalic != lfSystemFont.lfItalic ||
//					logFont.lfUnderline != lfSystemFont.lfUnderline ||
//					logFont.lfStrikeOut != lfSystemFont.lfStrikeOut ||
//					logFont.lfCharSet != lfSystemFont.lfCharSet ||
//					logFont.lfOutPrecision != lfSystemFont.lfOutPrecision ||
//					logFont.lfClipPrecision != lfSystemFont.lfClipPrecision ||
//					logFont.lfQuality != lfSystemFont.lfQuality ||
//					logFont.lfPitchAndFamily != lfSystemFont.lfPitchAndFamily ||
//					!getFontName (logFont).equals (getFontName (lfSystemFont))) {
//						resources [resourceCount++] = systemFont;
//						lfSystemFont = logFont;
//						systemFont = null;
//				}
//			}
//		}
//	}
//	if (errorImage != null) resources [resourceCount++] = errorImage;
//	if (infoImage != null) resources [resourceCount++] = infoImage;
//	if (questionImage != null) resources [resourceCount++] = questionImage;
//	if (warningIcon != null) resources [resourceCount++] = warningIcon;
//	errorImage = infoImage = questionImage = warningIcon = null;
//	for (int i=0; i<cursors.length; i++) {
//		if (cursors [i] != null) resources [resourceCount++] = cursors [i];
//		cursors [i] = null;
//	}
//	if (resourceCount < RESOURCE_SIZE) {
//		Resource [] newResources = new Resource [resourceCount];
//		System.arraycopy (resources, 0, newResources, 0, resourceCount);
//		resources = newResources;
//	}
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
	Win32.SetCursorPos (x, y);
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

//	if (key.equals (RUN_MESSAGES_IN_IDLE_KEY)) {
//		Boolean data = (Boolean) value;
//		runMessagesInIdle = data != null && data.booleanValue ();
//		return;
//	}
	
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

//void setModalDialogShell (Shell modalDailog) {
//	if (modalDialogShell != null && modalDialogShell.isDisposed ()) modalDialogShell = null;
//	this.modalDialogShell = modalDailog;
//	Shell [] shells = getShells ();
//	for (int i=0; i<shells.length; i++) shells [i].updateModal ();
//}

void setIdleHandler () {
	idle = true;
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
	int handler = OS.gcnew_NoArgsDelegate (jniRef, "sleep_noop");
	sleepOperation = OS.Dispatcher_BeginInvoke(dispatcher, OS.DispatcherPriority_Inactive, handler);
	OS.DispatcherOperation_Wait(sleepOperation);
	OS.GCHandle_Free(handler);
	OS.GCHandle_Free(sleepOperation);
	sleepOperation = 0;
	return true;
}

void sleep_noop() {
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
	if (timerHandles == null) timerHandles = new int [4];
	int index = 0;
	while (index < timerList.length) {
		if (timerList [index] == runnable) break;
		index++;
	}
	int timer = 0;
	if (index != timerList.length) {
		timer = timerHandles [index];
		if (milliseconds < 0) {
			OS.DispatcherTimer_Stop (timer);
			timerList [index] = null;
			OS.GCHandle_Free (timer);
			timerHandles [index] = 0;
			return;
		}
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
			int [] newTimerHandles = new int [timerHandles.length + 4];
			System.arraycopy (timerHandles, 0, newTimerHandles, 0, timerHandles.length);
			timerHandles = newTimerHandles;
		}
	}
	if (timer == 0) timer = OS.gcnew_DispatcherTimer();
	if (timer != 0) {
		OS.DispatcherTimer_Tag (timer, index);
		int timeSpan = OS.TimeSpan_FromMilliseconds (milliseconds);
		OS.DispatcherTimer_Interval(timer, timeSpan);
		OS.DispatcherTimer_Tick (timer, timerHandler);
		OS.DispatcherTimer_Start (timer);
		timerList [index] = runnable;
		timerHandles [index] = timer;
		OS.GCHandle_Free (timeSpan);
	}
}

void timerProc (int index, int e) {
	if (0 <= index && index < timerHandles.length) {
		int timer = timerHandles [index];
		if (timer != 0) {
			OS.DispatcherTimer_Stop (timer);
			OS.GCHandle_Free (timer);
			timerHandles [index] = 0;
			Runnable runnable = timerList [index];
			timerList [index] = null;
			runnable.run ();
		}
	}
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
	Shell[] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.update (true);
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

void wakeThreadHandler () {
}

void wakeThread () {
	int handler = OS.gcnew_NoArgsDelegate (jniRef, "wakeThreadHandler");
	int operation = OS.Dispatcher_BeginInvoke (dispatcher, OS.DispatcherPriority_Normal, handler);
	OS.GCHandle_Free (operation);
	OS.GCHandle_Free (handler);
}

}

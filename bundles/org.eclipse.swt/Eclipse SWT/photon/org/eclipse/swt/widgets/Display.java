package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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

	/**** TEMPORARY CODE FOR EMULATED TABLE ***/
	static int textHighlightThickness = 0;
	
	/* TEMPORARY HACK FOR PHOTON */
	public boolean embedded;
	
	/* Photon Only Public Fields */
	public int app_context;
//	public int phEventSize = PhEvent_t.sizeof + 1024;
//	public int phEvent = OS.malloc (phEventSize);
	
	/* Deferred Events */
	Event [] eventQueue;
	
	/* Events Dispatching and Callback */
	Callback windowCallback, drawCallback, workCallback, inputCallback, hotkeyCallback;
	int windowProc, drawProc, workProc, inputProc, hotkeyProc, input, pulse;
	boolean idle;

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread = Thread.currentThread ();
	
	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Timers */
	int [] timers;
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
			
//		{OS.VK_LBUTTON, SWT.BUTTON1},
//		{OS.VK_MBUTTON, SWT.BUTTON3},
//		{OS.VK_RBUTTON, SWT.BUTTON2},
		
		/* Non-Numeric Keypad Constants */
		{OS.Pk_Up,		SWT.ARROW_UP},
		{OS.Pk_Down,	SWT.ARROW_DOWN},
		{OS.Pk_Left,	SWT.ARROW_LEFT},
		{OS.Pk_Right,	SWT.ARROW_RIGHT},
		{OS.Pk_Prior,	SWT.PAGE_UP},
		{OS.Pk_Next,	SWT.PAGE_DOWN},
		{OS.Pk_Home,	SWT.HOME},
		{OS.Pk_End,		SWT.END},
		{OS.Pk_Insert,	SWT.INSERT},
//		{OS.Pk_Delete,	SWT.DELETE},
//		{OS.Pk_Escape, 0x001B},
	
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

	/* Window Classes */
	static int ClassesPtr;
	static int PtButton;
	static int PtList;
	static int PtLabel;
	static int PtWindow;
	static int PtToggleButton;
	static int PtComboBox;
	static int PtText;
	static int PtMultiText;
	static int PtScrollbar;
	static int PtScrollContainer;
	static int PtScrollArea;
	static int PtContainer;
	static int PtProgress;
	static int PtPanelGroup;
	static int PtPane;
	static int PtTree;
	static int PtSlider;
	static int PtSeparator;
				
	/* Colors */
	int WIDGET_DARK_SHADOW, WIDGET_NORMAL_SHADOW, WIDGET_LIGHT_SHADOW;
	int WIDGET_HIGHLIGHT_SHADOW, WIDGET_BACKGROUND, WIDGET_FOREGROUND, WIDGET_BORDER;
	int LIST_FOREGROUND, LIST_BACKGROUND, LIST_SELECTION, LIST_SELECTION_TEXT;
	int INFO_FOREGROUND, INFO_BACKGROUND;
	
	/* Fonts */
	byte [] TEXT_FONT, LIST_FONT;
	
	/* ScrollBars */
	int SCROLLBAR_WIDTH;
	int SCROLLBAR_HEIGHT;
	int SCROLLBAR_VERTICAL_BASIC_FLAGS;
	int SCROLLBAR_HORIZONTAL_BASIC_FLAGS;

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
	OS.PtBeep ();
}

protected void checkDevice () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

synchronized void checkDisplay () {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] != null && Displays [i].thread == thread) {
			error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
}

protected void checkSubclass () {
	if (!isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
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

protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay ();
	createDisplay (data);
	register ();
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
	OS.PtInit (null);
	OS.PgSetDrawBufferSize (DrawBufferSize);
	app_context = OS.PtCreateAppContext ();
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
	// NEED to destroy app_context ???
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

int drawProc (int handle, int damage) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return 0;
	return widget.processPaint (damage);
}

void error (int code) {
	SWT.error(code);
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
public static synchronized Display getCurrent () {
	Thread current = Thread.currentThread ();
	for (int i=0; i<Displays.length; i++) {
		Display display = Displays [i];
		if (display != null && display.thread == current) return display;
	}
	return null;
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
 * On platforms which support it, sets the application name
 * to be the argument. On Motif, for example, this can be used
 * to set the name used for resource lookup.
 *
 * @param name the new app name
 */
public static void setAppName (String name) {
	/* Do nothing */
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
	int color = 0x000000;
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: 					color = INFO_FOREGROUND; break;
		case SWT.COLOR_INFO_BACKGROUND: 					color = INFO_BACKGROUND; break;
		case SWT.COLOR_TITLE_FOREGROUND: 					color = 0x000000; break;
		case SWT.COLOR_TITLE_BACKGROUND:					color = 0x6493E7; break;
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT:			color = 0x0000FF; break;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:			color = 0x000000; break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND: 			color = 0xABBBD3; break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT:	color = 0x0000FF; break;
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
	return Font.photon_new (this, TEXT_FONT);
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
 * Returns the user-interface thread for the receiver.
 *
 * @return the receiver's user-interface thread
 */
public Thread getThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return thread;
}

int hotkeyProc (int handle, int data, int info) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return OS.Pt_CONTINUE;
	return widget.processHotkey (data, info);
}

protected void init () {
	super.init ();
	initializeDisplay ();
	initializeWidgetClasses ();
	initializeWidgetColors ();
	initializeWidgetFonts ();
	initializeScrollbars ();
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
		OS.PtCreateWidgetClass (OS.PtTree (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtSlider (), 0, args.length / 3, args), 0, 0,
		OS.PtCreateWidgetClass (OS.PtSeparator (), 0, args.length / 3, args), 0, 0,
	};
	ClassesPtr = OS.malloc (buffer.length * 4);
	OS.memmove (ClassesPtr, buffer, buffer.length * 4);
	PtButton = ClassesPtr;
	PtList = ClassesPtr + 12;
	PtLabel = ClassesPtr + 24;
	PtWindow = ClassesPtr + 36;
	PtToggleButton = ClassesPtr + 48;
	PtComboBox = ClassesPtr + 60;
	PtText = ClassesPtr + 72;
	PtMultiText = ClassesPtr + 84;	
	PtScrollbar = ClassesPtr + 96;
	PtScrollContainer = ClassesPtr + 108;
	PtScrollArea = ClassesPtr + 120;
	PtContainer = ClassesPtr + 132;
	PtProgress = ClassesPtr + 144;
	PtPanelGroup = ClassesPtr + 156;
	PtPane = ClassesPtr + 168;
	PtTree = ClassesPtr + 180;
	PtSlider = ClassesPtr + 192;
	PtSeparator = ClassesPtr + 204;
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

	int handle = OS.PtCreateWidget (OS.PtList (), shellHandle, 0, null);
	args = new int [] {	
		OS.Pt_ARG_COLOR, 0, 0,
		OS.Pt_ARG_FILL_COLOR, 0, 0,
		OS.Pt_ARG_SELECTION_FILL_COLOR, 0, 0,
		OS.Pt_ARG_SELECTION_TEXT_COLOR, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	LIST_FOREGROUND = args [1];
	LIST_BACKGROUND = args [4];
	LIST_SELECTION = args [7];
	LIST_SELECTION_TEXT = args [10];

	/*
	* Feature in Photon. The values of Pt_ARG_DARK_BEVEL_COLOR and
	* Pt_ARG_LIGHT_BEVEL_COLOR are not initialized until the widget
	* is realized.  The fix is to realize the shell, but don't
	* display it.
	*/
	handle = OS.PtCreateWidget (OS.PtButton (), shellHandle, 0, null);
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
	OS.PtGetResources (handle, args.length / 3, args);
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
	OS.PtSetParentWidget (0);
	int shellHandle = OS.PtCreateWidget (OS.PtWindow (), 0, 0, null);
	int listHandle = OS.PtCreateWidget (OS.PtList (), shellHandle, 0, null);
	int [] args = {OS.Pt_ARG_LIST_FONT, 0, 0};
	OS.PtGetResources (listHandle, args.length / 3, args);
	int count = OS.strlen (args [1]);
	LIST_FONT = new byte [count + 1];
	OS.memmove (LIST_FONT, args [1], count);
	int textHandle = OS.PtCreateWidget (OS.PtText (), shellHandle, 0, null);
	args = new int [] {OS.Pt_ARG_TEXT_FONT, 0, 0};
	OS.PtGetResources (textHandle, args.length / 3, args);
	count = OS.strlen (args [1]);
	TEXT_FONT = new byte [count + 1];
	OS.memmove (TEXT_FONT, args [1], count);
	OS.PtDestroyWidget (shellHandle);
}

int inputProc (int data, int rcvid, int message, int size) {
	if (embedded) {
		runDeferredEvents ();
		runAsyncMessages ();
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
 * @private
 */
public int internal_new_GC (GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	int phGC = OS.PgCreateGC(0); // NOTE: PgCreateGC ignores the parameter
	if (phGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);

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
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data 
 *
 * @private
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
static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_NAME);
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
	if (embedded) wake ();
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
	idle = false;
	int id = OS.PtAppAddWorkProc (app_context, workProc, 0);
	OS.PtAppProcessEvent (app_context);
	OS.PtAppRemoveWorkProc (app_context, id);
	if (!idle) {
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
	super.release ();

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
}

void releaseDisplay () {
	
	OS.PtDestroyWidget (timerHandle);
	
	/* Free the classes array */
	OS.free (ClassesPtr);
	
	/* Free pulses and input proc */
	OS.PtAppRemoveInput (app_context, input);
	OS.PtAppDeletePulse (app_context, pulse);
	
	/* Free the timers */
	if (timers != null) {
		for (int i=0; i<timers.length; i++) {
			 if (timers [i] != 0) OS.PtDestroyWidget (timers [i]);
		}
	}
	timers = null;
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
		
	/* Release references */
	thread = null;
	data = null;
	keys = null;
	values = null;
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
	OS.PtAppProcessEvent (app_context);
	runDeferredEvents ();
	return true;
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
	if (timers == null) timers = new int [4];
	int index = 0;
	while (index < timerList.length) {
		if (timerList [index] == null) break;
		index++;
	}
	if (index == timerList.length) {
		Runnable [] newTimerList = new Runnable [timerList.length + 4];
		System.arraycopy (timerList, 0, newTimerList, 0, timerList.length);
		timerList = newTimerList;
		int [] newTimers = new int [timers.length + 4];
		System.arraycopy (timers, 0, newTimers, 0, timers.length);
		timers = newTimers;
	}
	int [] args = {OS.Pt_ARG_TIMER_INITIAL, milliseconds, 0};
	int handle = OS.PtCreateWidget (OS.PtTimer (), timerHandle, args.length / 3, args);
	OS.PtRealizeWidget (handle);
	if (handle != 0) {
		OS.PtAddCallback (handle, OS.Pt_CB_TIMER_ACTIVATE, timerProc, index);
		timerList [index] = runnable;
		timers [index] = handle;
	}

}
int timerProc (int handle, int index, int info) {
	if (timerList == null) return 0;
	if (0 <= index && index < timerList.length) {
		Runnable runnable = timerList [index];
		timerList [index] = null;
		OS.PtDestroyWidget (timers [index]);
		timers [index] = 0;
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

public void update() {
	checkDevice ();
	Shell[] shells = WidgetTable.shells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && this == shell.getDisplay ()) {
			shell.update ();
		}
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
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
//	int flags = OS.PtEnter (0);	
	OS.PtAppPulseTrigger (app_context, pulse);
//	if (flags >= 0) OS.PtLeave (flags);
}

int windowProc (int handle, int data, int info) {
	Widget widget = WidgetTable.get (handle);
	if (widget == null) return OS.Pt_CONTINUE;
	return widget.processEvent (handle, data, info);
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

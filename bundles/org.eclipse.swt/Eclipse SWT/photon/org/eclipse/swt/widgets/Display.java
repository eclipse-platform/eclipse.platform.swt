package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public /*final*/ class Display extends Device {

	/**** TEMPORARY CODE FOR MOTIF TABLE ***/
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
				CurrentDevice = getCurrent ();
				if (CurrentDevice == null) {
					CurrentDevice = getDefault ();
				}
			}
		};
	}
			
public Display () {
	this (null);
}

public Display (DeviceData data) {
	super (data);
}

public void asyncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.asyncExec (runnable);
}

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

public static synchronized Display findDisplay (Thread thread) {
	for (int i=0; i<Displays.length; i++) {
		Display display = Displays [i];
		if (display != null && display.thread == thread) {
			return display;
		}
	}
	return null;
}

public Widget findWidget (int handle) {
	checkDevice ();
	return WidgetTable.get (handle);
}

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

public Rectangle getBounds () {
	checkDevice ();
	PhRect_t rect = new PhRect_t ();
	OS.PhWindowQueryVisible (OS.Ph_QUERY_GRAPHICS, 0, 1, rect);
	int width = rect.lr_x - rect.ul_x + 1;
	int height = rect.lr_y - rect.ul_y + 1;
	return new Rectangle (rect.ul_x, rect.ul_y, width, height);
}

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

public Point getCursorLocation () {
	checkDevice ();
	int ig = OS.PhInputGroup (0);
	PhCursorInfo_t info = new PhCursorInfo_t ();
	OS.PhQueryCursor ((short)ig, info);
	return new Point (info.pos_x, info.pos_y);
}

public static synchronized Display getCurrent () {
	Thread current = Thread.currentThread ();
	for (int i=0; i<Displays.length; i++) {
		Display display = Displays [i];
		if (display != null && display.thread == current) return display;
	}
	return null;
}

public static synchronized Display getDefault () {
	if (Default == null) Default = new Display ();
	return Default;
}

public static void setAppName (String name) {
	/* Do nothing */
}

public int getDoubleClickTime () {
	checkDevice ();
	//NOT DONE
	return 250;
}

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

public int getIconDepth () {
	return getDepth ();
}

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

public Font getSystemFont () {
	checkDevice ();
	return Font.photon_new (this, TEXT_FONT);
}

public Thread getSyncThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return synchronizer.syncThread;
}

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

public int internal_new_GC (GCData data) {
	int phGC = OS.PgCreateGC(0); // NOTE: PgCreateGC ignores the parameter
	if (phGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	data.device = this;
	data.rid = OS.Ph_DEV_RID;
	return phGC;
}

public void internal_dispose_GC (int phGC, GCData data) {
	OS.PgDestroyGC(phGC);
}

boolean isValidThread () {
	return thread == Thread.currentThread ();
}

public Object getData (String key) {
	checkDevice ();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

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

public void setData (Object data) {
	checkDevice ();
	this.data = data;
}

public void setSynchronizer (Synchronizer synchronizer) {
	checkDevice ();
	if (synchronizer == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (this.synchronizer != null) {
		this.synchronizer.runAsyncMessages();
	}
	this.synchronizer = synchronizer;
}

public boolean sleep () {
	checkDevice ();
	OS.PtAppProcessEvent (app_context);
	runDeferredEvents ();
	return true;
}

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
			int lineWidth = textWidth (line, font);
			while (i < lineEnd && Character.isWhitespace (text.charAt (i))) {
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

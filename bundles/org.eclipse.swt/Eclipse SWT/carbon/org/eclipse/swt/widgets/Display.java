package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.CGRect;
import org.eclipse.swt.internal.carbon.HICommand;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.RGBColor;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class Display extends Device {

	//TEMPORARY
	int textHighlightThickness = 4;
	
	/* Windows and Events */
	int [] wakeEvent;
	static final int WAKE_CLASS = 0;
	static final int WAKE_KIND = 0;
	Event [] eventQueue;
	Callback actionCallback, commandCallback, controlCallback;
	Callback drawItemCallback, itemDataCallback, itemNotificationCallback, helpCallback;
	Callback hitTestCallback, keyboardCallback, menuCallback, mouseHoverCallback;
	Callback mouseCallback, trackingCallback, windowCallback;
	int actionProc, commandProc, controlProc;
	int drawItemProc, itemDataProc, itemNotificationProc, helpProc;
	int hitTestProc, keyboardProc, menuProc, mouseHoverProc;
	int mouseProc, trackingProc, windowProc;
	EventTable eventTable, filterTable;
	int queue, lastModifiers;
	
	/* Sync/Async Widget Communication */
	Synchronizer synchronizer = new Synchronizer (this);
	Thread thread;
	
	/* Display Shutdown */
	Runnable [] disposeList;
	
	/* Timers */
	int [] timerIds;
	Runnable [] timerList;
	Callback timerCallback;
	int timerProc;
	boolean allowTimers;
		
	/* Current caret */
	Caret currentCaret;
	Callback caretCallback;
	int caretID, caretProc;
	
	/* Grabs */
	Control grabControl;

	/* Hover Help */
	int helpString;
	Control helpControl;
	int lastHelpX, lastHelpY;
	
	/* Mouse Enter/Exit/Hover */
	Control currentControl;
	int mouseHoverID;
	
	/* Menus */
	Menu menuBar;
	Menu [] menus, popups;
	MenuItem [] items;
	static final int ID_TEMPORARY = 1000;
	static final int ID_START = 1001;
	
	/* Insets */
	Rect buttonInset, tabFolderInset, comboInset;
	
	/* Focus */
	boolean ignoreFocus;
	
	/* Key Mappings. */
	static int [] [] KeyTable = {

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
				
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";
			
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
	
	static {
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
			OS.SetFrontProcess (psn);
		}
	}
	
/*
* TEMPORARY CODE.
*/
static void setDevice (Device device) {
	CurrentDevice = device;
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

int actionProc (int theControl, int partCode) {
	Widget widget = WidgetTable.get (theControl);
	if (widget != null) return widget.actionProc (theControl, partCode);
	return OS.noErr;
}

public void addFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) filterTable = new EventTable ();
	filterTable.hook (eventType, listener);
}

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
			menu.id = (short)(ID_START + i);
			menus [i] = menu;
			return;
		}
	}
	Menu [] newMenus = new Menu [menus.length + 12];
	menu.id = (short)(ID_START + menus.length);
	newMenus [menus.length] = menu;
	System.arraycopy (menus, 0, newMenus, 0, menus.length);
	menus = newMenus;
}

void addMenuItem (MenuItem item) {
	if (items == null) items = new MenuItem [12];
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) {
			item.id = ID_START + i;
			items [i] = item;
			return;
		}
	}
	MenuItem [] newItems = new MenuItem [items.length + 12];
	item.id = ID_START + items.length;
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

public void asyncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.asyncExec (runnable);
}

public void beep () {
	checkDevice ();
	OS.SysBeep ((short) 100);
}

int caretProc (int id, int clientData) {
	if (!allowTimers) return 0;
	if (currentCaret == null) return 0;
	if (currentCaret.blinkCaret ()) {
		int blinkRate = currentCaret.blinkRate;
		OS.SetEventLoopTimerNextFireTime (id, blinkRate / 1000.0);
	} else {
		currentCaret = null;
	}
	return 0;
}

protected void checkDevice () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
}

protected void checkSubclass () {
	if (!Display.isValidClass (getClass ())) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Display () {
	this (null);
}

public Display (DeviceData data) {
	super (data);
}

static synchronized void checkDisplay (Thread thread) {
	for (int i=0; i<Displays.length; i++) {
		if (Displays [i] != null && Displays [i].thread == thread) {
			SWT.error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
	}
}

int commandProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	HICommand command = new HICommand ();
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeHICommand, null, HICommand.sizeof, null, command);
	switch (eventKind) {
		case OS.kEventProcessCommand: {
			if (command.commandID == OS.kAEQuitApplication) {
				close ();
				return OS.noErr;
			}
			if ((command.attributes & OS.kHICommandFromMenu) != 0) {
				if (userData != 0) {
					Widget widget = WidgetTable.get (userData);
					if (widget != null) return widget.commandProc (nextHandler, theEvent, userData);
				} else {
					int menuRef = command.menu_menuRef;
					short menuID = OS.GetMenuID (menuRef);
					Menu menu = findMenu (menuID);
					if (menu != null) {
						int [] outCommandID = new int [1];
						short menuIndex = command.menu_menuItemIndex;
						OS.GetMenuItemCommandID (menuRef, menuIndex, outCommandID);
						MenuItem item = findMenuItem (outCommandID [0]);
						if (item != null) {
							return item.kEventProcessCommand (nextHandler, theEvent, userData);
						} 
					}
					OS.HiliteMenu ((short) 0);
				}
			}
		}
	}
	return OS.eventNotHandledErr;
}

Rect computeInset (int control) {
	int tempRgn = OS.NewRgn ();
	Rect rect = new Rect ();
	OS.GetControlRegion (control, (short) OS.kControlStructureMetaPart, tempRgn);
	OS.GetControlBounds (control, rect);
	Rect rgnRect = new Rect ();
	OS.GetRegionBounds (tempRgn, rgnRect);
	OS.DisposeRgn (tempRgn);
	rect.left -= rgnRect.left;
	rect.top -= rgnRect.top;
	rect.right = (short) (rgnRect.right - rect.right);
	rect.bottom = (short) (rgnRect.bottom - rect.bottom);
	return rect; 
}

int controlProc (int nextHandler, int theEvent, int userData) {
	Widget widget = WidgetTable.get (userData);
	if (widget != null) return widget.controlProc (nextHandler, theEvent, userData);
	return OS.eventNotHandledErr;
}

public void close () {
	checkDevice ();
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit) dispose ();
}

protected void create (DeviceData data) {
	checkSubclass ();
	checkDisplay (thread = Thread.currentThread ());
	createDisplay (data);
	register (this);
	if (Default == null) Default = this;
}

void createDisplay (DeviceData data) {
	queue = OS.GetCurrentEventQueue ();
	wakeEvent = new int [1];
	OS.CreateEvent (0, WAKE_CLASS, WAKE_KIND, 0.0, OS.kEventAttributeUserEvent, wakeEvent);
	OS.RetainEvent (wakeEvent [0]);
	OS.TXNInitTextension (0, 0, 0);
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

int drawItemProc (int browser, int item, int property, int itemState, int theRect, int gdDepth, int colorDevice) {
	Widget widget = WidgetTable.get (browser);
	if (widget != null) return widget.drawItemProc (browser, item, property, itemState, theRect, gdDepth, colorDevice);
	return OS.noErr;
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

Menu findMenu (int id) {
	if (menus == null) return null;
	int index = id - ID_START;
	if (0 <= index && index < menus.length) return menus [index];
	return null;
}

MenuItem findMenuItem (int id) {
	if (items == null) return null;
	int index = id - ID_START;
	if (0 <= index && index < items.length) return items [index];
	return null;
}

public Widget findWidget (int handle) {
	checkDevice ();
	return WidgetTable.get (handle);
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

public Shell getActiveShell () {
	checkDevice ();
	int theWindow = OS.ActiveNonFloatingWindow ();
	if (theWindow == 0) return null;
	int [] theControl = new int [1];
	OS.GetRootControl (theWindow, theControl);
	Widget widget = WidgetTable.get (theControl [0]);
	if (widget instanceof Shell) return (Shell) widget;
	return null;
}

public static synchronized Display getCurrent () {
	return findDisplay (Thread.currentThread ());
}

int getCaretBlinkTime () {
	return OS.GetCaretTime () * 1000 / 60;
}

Control getCursorControl (boolean includeTrim) {
	org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetGlobalMouse (where);
	int [] theWindow = new int [1];
	if (OS.FindWindow (where, theWindow) != OS.inContent) return null;
	if (theWindow [0] == 0) return null;
	Rect rect = new Rect ();
	OS.GetWindowBounds (theWindow [0], (short) OS.kWindowContentRgn, rect);
	CGPoint inPoint = new CGPoint ();
	inPoint.x = where.h - rect.left;
	inPoint.y = where.v - rect.top;
	int [] theRoot = new int [1];
	OS.GetRootControl (theWindow [0], theRoot);
	int [] theControl = new int [1];
	OS.HIViewGetSubviewHit (theRoot [0], inPoint, true, theControl);
	if (theControl [0] != 0) {
		do {
			Widget widget = WidgetTable.get (theControl [0]);
			if (!includeTrim && widget != null) {
				if (widget.isTrimHandle (theControl [0])) return null;
			}
			if (widget != null && widget instanceof Control) {
				Control control = (Control) widget;
				if (control.getEnabled ()) return control;
			}
			OS.GetSuperControl (theControl [0], theControl);
		} while (theControl [0] != 0);
	}
	Widget widget = WidgetTable.get (theRoot [0]);
	if (widget != null && widget instanceof Control) return (Control) widget;
	return null;
}

public Control getCursorControl () {
	checkDevice ();
	return getCursorControl (true);
}

public Point getCursorLocation () {
	checkDevice ();
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetGlobalMouse (pt);
	return new Point (pt.h, pt.v);
}

public static synchronized Display getDefault () {
	if (Default == null) Default = new Display ();
	return Default;
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

public int getDoubleClickTime () {
	checkDevice ();
	return OS.GetDblTime (); 
}

public Control getFocusControl () {
	checkDevice ();
	int theWindow = OS.ActiveNonFloatingWindow ();
	if (theWindow == 0) return null;
	return getFocusControl (theWindow);
}

Control getFocusControl (int window) {
	int [] theControl = new int [1];
	OS.GetKeyboardFocus (window, theControl);
	if (theControl [0] == 0) return null;
	do {
		Widget widget = WidgetTable.get (theControl [0]);
		if (widget != null && widget instanceof Control) {
			Control control = (Control) widget;
			if (control.getEnabled ()) return control;
		}
		OS.GetSuperControl (theControl [0], theControl);
	} while (theControl [0] != 0);
	return null;
}

public int getIconDepth () {
	return getDepth ();
}

int getLastEventTime () {
	/*
	* This code is intentionally commented.  Event time is
	* in seconds and we need an accurate time in milliseconds.
	*/
//	return (int) (OS.GetLastUserEventTime () * 1000.0);
	return (int) System.currentTimeMillis ();
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

Menu getMenuBar () {
	return menuBar;
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

public Thread getSyncThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return synchronizer.syncThread;
}

public Color getSystemColor (int id) {
	checkDevice ();
	//NOT DONE
	
	RGBColor rgb = new RGBColor ();
	switch (id) {
		case SWT.COLOR_INFO_FOREGROUND: 						return super.getSystemColor (SWT.COLOR_BLACK);
		case SWT.COLOR_INFO_BACKGROUND: 						return Color.carbon_new (this, new float [] {0xFF / 255f, 0xFF / 255f, 0xE1 / 255f, 1});
		case SWT.COLOR_TITLE_FOREGROUND:						OS.GetThemeTextColor((short)OS.kThemeTextColorDocumentWindowTitleActive, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_TITLE_BACKGROUND:						OS.GetThemeBrushAsColor((short)-5/*undocumented darker highlight color*/, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_TITLE_BACKGROUND_GRADIENT: 	OS.GetThemeBrushAsColor((short)OS.kThemeBrushPrimaryHighlightColor, (short)getDepth(), true, rgb) ; break;
		case SWT.COLOR_TITLE_INACTIVE_FOREGROUND:	OS.GetThemeTextColor((short)OS.kThemeTextColorDocumentWindowTitleInactive, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND: 	OS.GetThemeBrushAsColor((short)OS.kThemeBrushSecondaryHighlightColor, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT: OS.GetThemeBrushAsColor((short)OS.kThemeBrushSecondaryHighlightColor, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_WIDGET_DARK_SHADOW:				return Color.carbon_new (this, new float [] {0x33 / 255f, 0x33 / 255f, 0x33 / 255f, 1});
		case SWT.COLOR_WIDGET_NORMAL_SHADOW:			return Color.carbon_new (this, new float [] {0x66 / 255f, 0x66 / 255f, 0x66 / 255f, 1});
		case SWT.COLOR_WIDGET_LIGHT_SHADOW: 				return Color.carbon_new (this, new float [] {0x99 / 255f, 0x99 / 255f, 0x99 / 255f, 1});
		case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:		return Color.carbon_new (this, new float [] {0xCC / 255f, 0xCC / 255f, 0xCC / 255f, 1});
		case SWT.COLOR_WIDGET_BACKGROUND: 					OS.GetThemeBrushAsColor((short)OS.kThemeBrushButtonFaceActive, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_WIDGET_FOREGROUND:					OS.GetThemeTextColor((short)OS.kThemeTextColorPushButtonActive, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_WIDGET_BORDER: 							return super.getSystemColor (SWT.COLOR_BLACK);
		case SWT.COLOR_LIST_FOREGROUND: 						OS.GetThemeTextColor((short)OS.kThemeTextColorListView, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_LIST_BACKGROUND: 						OS.GetThemeBrushAsColor((short)OS.kThemeBrushListViewBackground, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_LIST_SELECTION_TEXT: 					OS.GetThemeTextColor((short)OS.kThemeTextColorListView, (short)getDepth(), true, rgb); break;
		case SWT.COLOR_LIST_SELECTION:								OS.GetThemeBrushAsColor((short)OS.kThemeBrushPrimaryHighlightColor, (short)getDepth(), true, rgb); break;
		default:
			return super.getSystemColor (id);	
	}
	float red = ((rgb.red >> 8) & 0xFF) / 255f;
	float green = ((rgb.green >> 8) & 0xFF) / 255f;
	float blue = ((rgb.blue >> 8) & 0xFF) / 255f;
	return Color.carbon_new (this, new float[]{red, green, blue, 1});
}

public Thread getThread () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	return thread;
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
	Widget widget = WidgetTable.get (inControl);
	if (widget != null) return widget.helpProc (inControl, inGlobalMouse, inRequest, outContentProvided, ioHelpContent);
	return OS.eventNotHandledErr;
}

int hitTestProc (int browser, int item, int property, int theRect, int mouseRect) {
	Widget widget = WidgetTable.get (browser);
	if (widget != null) return widget.hitTestProc (browser, item, property, theRect, mouseRect);
	return OS.noErr;
}

protected void init () {
	super.init ();
	initializeCallbacks ();
	initializeInsets ();	
}
	
void initializeCallbacks () {
	/* Create Callbacks */
	actionCallback = new Callback (this, "actionProc", 2);
	actionProc = actionCallback.getAddress ();
	if (actionProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	caretCallback = new Callback(this, "caretProc", 2);
	caretProc = caretCallback.getAddress();
	if (caretProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	commandCallback = new Callback (this, "commandProc", 3);
	commandProc = commandCallback.getAddress ();
	if (commandProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	controlCallback = new Callback (this, "controlProc", 3);
	controlProc = controlCallback.getAddress ();
	if (controlProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	drawItemCallback = new Callback (this, "drawItemProc", 7);
	drawItemProc = drawItemCallback.getAddress ();
	if (drawItemProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	itemDataCallback = new Callback (this, "itemDataProc", 5);
	itemDataProc = itemDataCallback.getAddress ();
	if (itemDataProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	itemNotificationCallback = new Callback (this, "itemNotificationProc", 3);
	itemNotificationProc = itemNotificationCallback.getAddress ();
	if (itemNotificationProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	helpCallback = new Callback (this, "helpProc", 5);
	helpProc = helpCallback.getAddress ();
	if (helpProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	hitTestCallback = new Callback (this, "hitTestProc", 5);
	hitTestProc = hitTestCallback.getAddress ();
	if (hitTestProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	keyboardCallback = new Callback (this, "keyboardProc", 3);
	keyboardProc = keyboardCallback.getAddress ();
	if (keyboardProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	menuCallback = new Callback (this, "menuProc", 3);
	menuProc = menuCallback.getAddress ();
	if (menuProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	mouseHoverCallback = new Callback (this, "mouseHoverProc", 2);
	mouseHoverProc = mouseHoverCallback.getAddress ();
	if (mouseHoverProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	mouseCallback = new Callback (this, "mouseProc", 3);
	mouseProc = mouseCallback.getAddress ();
	if (mouseProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	timerCallback = new Callback (this, "timerProc", 2);
	timerProc = timerCallback.getAddress ();
	if (timerProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	trackingCallback = new Callback (this, "trackingProc", 6);
	trackingProc = trackingCallback.getAddress ();
	if (trackingProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
	windowCallback = new Callback (this, "windowProc", 3);
	windowProc = windowCallback.getAddress ();
	if (windowProc == 0) error (SWT.ERROR_NO_MORE_CALLBACKS);
		
	/* Install Event Handlers */
	int[] mask1 = new int[] {
		OS.kEventClassCommand, OS.kEventProcessCommand,
	};
	int appTarget = OS.GetApplicationEventTarget ();
	OS.InstallEventHandler (appTarget, commandProc, mask1.length / 2, mask1, 0, null);
	int[] mask2 = new int[] {
		OS.kEventClassMouse, OS.kEventMouseDown,
		OS.kEventClassMouse, OS.kEventMouseDragged,
//		OS.kEventClassMouse, OS.kEventMouseEntered,
//		OS.kEventClassMouse, OS.kEventMouseExited,
		OS.kEventClassMouse, OS.kEventMouseMoved,
		OS.kEventClassMouse, OS.kEventMouseUp,
		OS.kEventClassMouse, OS.kEventMouseWheelMoved,
	};
	OS.InstallEventHandler (appTarget, mouseProc, mask2.length / 2, mask2, 0, null);
	int [] mask3 = new int[] {
		OS.kEventClassKeyboard, OS.kEventRawKeyDown,
		OS.kEventClassKeyboard, OS.kEventRawKeyModifiersChanged,
		OS.kEventClassKeyboard, OS.kEventRawKeyRepeat,
		OS.kEventClassKeyboard, OS.kEventRawKeyUp,
	};
	int focusTarget = OS.GetUserFocusEventTarget ();
	OS.InstallEventHandler (focusTarget, keyboardProc, mask3.length / 2, mask3, 0, null);
}

void initializeInsets () {
	int [] outControl = new int [1];
	Rect rect = new Rect ();
	rect.right = rect.bottom = (short) 200;
	
	OS.CreatePushButtonControl (0, rect, 0, outControl);
	buttonInset = computeInset (outControl [0]);
	OS.DisposeControl (outControl [0]);
	
	OS.CreateTabsControl (0, rect, (short)OS.kControlTabSizeLarge, (short)OS.kControlTabDirectionNorth, (short) 0, 0, outControl);
	tabFolderInset = computeInset (outControl [0]);
	OS.DisposeControl (outControl [0]);

	CGRect cgRect = new CGRect ();
	cgRect.width = cgRect.height = 200;
	int inAttributes = OS.kHIComboBoxAutoCompletionAttribute | OS.kHIComboBoxAutoSizeListAttribute;
	OS.HIComboBoxCreate (cgRect, 0, null, 0, inAttributes, outControl);
	comboInset = computeInset (outControl [0]);
	 //FIXME - 
	comboInset.bottom = comboInset.top;
	OS.DisposeControl (outControl [0]);
}

public int internal_new_GC (GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	// NEEDS WORK
	int window = OS.FrontWindow ();
	int port = OS.GetWindowPort (window);
	int [] buffer = new int [1];
	OS.CreateCGContextForPort (port, buffer);
	int context = buffer [0];
	if (context == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		data.device = this;
		data.background = getSystemColor (SWT.COLOR_WHITE).handle;
		data.foreground = getSystemColor (SWT.COLOR_BLACK).handle;
		data.font = getSystemFont ();
	}
	return context;
}

public void internal_dispose_GC (int context, GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	// NEEDS WORK
	OS.CGContextFlush (context);
	OS.CGContextRelease (context);
}

static boolean isValidClass (Class clazz) {
	String name = clazz.getName ();
	int index = name.lastIndexOf ('.');
	return name.substring (0, index + 1).equals (PACKAGE_PREFIX);
}

boolean isValidThread () {
	return thread == Thread.currentThread ();
}

int itemDataProc (int browser, int item, int property, int itemData, int setValue) {
	Widget widget = WidgetTable.get (browser);
	if (widget != null) return widget.itemDataProc (browser, item, property, itemData, setValue);
	return OS.noErr;
}

int itemNotificationProc (int browser, int item, int message) {
	Widget widget = WidgetTable.get (browser);
	if (widget != null) return widget.itemNotificationProc (browser, item, message);
	return OS.noErr;
}

int keyboardProc (int nextHandler, int theEvent, int userData) {
	Widget widget = WidgetTable.get (userData);
	if (widget == null) {
		int theWindow = OS.ActiveNonFloatingWindow ();
		if (theWindow == 0) return OS.eventNotHandledErr;
		int [] theControl = new int [1];
		OS.GetKeyboardFocus (theWindow, theControl);
		if (theControl [0] == 0) {
			OS.GetRootControl (theWindow, theControl);
		}
		widget = WidgetTable.get (theControl [0]);
	}
	if (widget != null) return widget.keyboardProc (nextHandler, theEvent, userData);
	return OS.eventNotHandledErr;
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
	
int menuProc (int nextHandler, int theEvent, int userData) {
	if (userData != 0) {
		Widget widget = WidgetTable.get (userData);
		if (widget != null) return widget.menuProc (nextHandler, theEvent, userData);
	} else {
		int [] theMenu = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeMenuRef, null, 4, null, theMenu);
		short menuID = OS.GetMenuID (theMenu [0]);
		Menu menu = findMenu (menuID);
		if (menu != null) return menu.menuProc (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int mouseProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
	org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, where);
	int [] theWindow = new int [1];
	int part = OS.FindWindow (where, theWindow);
	switch (part) {
		case OS.inMenuBar: {
			if (eventKind == OS.kEventMouseDown) {
				OS.MenuSelect (where);
				return OS.noErr;
			}
			break;
		}
		case OS.inContent: {
			Rect windowRect = new Rect ();
			OS.GetWindowBounds (theWindow [0], (short) OS.kWindowContentRgn, windowRect);
			CGPoint inPoint = new CGPoint ();
			inPoint.x = where.h - windowRect.left;
			inPoint.y = where.v - windowRect.top;
			int [] theRoot = new int [1];
			OS.GetRootControl (theWindow [0], theRoot);
			int [] theControl = new int [1];
			OS.HIViewGetSubviewHit (theRoot [0], inPoint, true, theControl);
			if (theControl [0] == 0) theControl [0] = theRoot [0];
			Widget widget = WidgetTable.get (theControl [0]);
			switch (eventKind) {
				case OS.kEventMouseDragged:
				case OS.kEventMouseMoved: {
					org.eclipse.swt.internal.carbon.Point localPoint = new org.eclipse.swt.internal.carbon.Point ();
					OS.SetPt (localPoint, (short) inPoint.x, (short) inPoint.y);
					int [] modifiers = new int [1];
					OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
					boolean [] cursorWasSet = new boolean [1];
					OS.HandleControlSetCursor (theControl [0], localPoint, (short) modifiers [0], cursorWasSet);
					if (!cursorWasSet [0]) OS.SetThemeCursor (OS.kThemeArrowCursor);
					if (widget != null) {
						int [] outDelay = new int [1];
						OS.HMGetTagDelay (outDelay);
						if (widget == currentControl && mouseHoverID != 0) {
							OS.SetEventLoopTimerNextFireTime (mouseHoverID, outDelay [0] / 1000.0);
						} else {
							if (mouseHoverID != 0) OS.RemoveEventLoopTimer (mouseHoverID);
							int [] id = new int [1];
							int eventLoop = OS.GetCurrentEventLoop ();
							OS.InstallEventLoopTimer (eventLoop, outDelay [0] / 1000.0, 0.0, mouseHoverProc, 0, id);
							mouseHoverID = id [0];
						}
					}
				}
			}
			if (widget != null) {
				return userData != 0 ? widget.mouseProc (nextHandler, theEvent, userData) : OS.eventNotHandledErr;
			}
			break;
		}
	}
	switch (eventKind) {
		case OS.kEventMouseDragged:
		case OS.kEventMouseMoved:  OS.InitCursor ();
	}
	return OS.eventNotHandledErr;
}

int mouseHoverProc (int id, int handle) {
	if (currentControl == null) return 0;
	if (!currentControl.isDisposed ()) {
		//OPTIMIZE - use OS calls
		int chord = OS.GetCurrentEventButtonState ();
		int modifiers = OS.GetCurrentEventKeyModifiers ();
		Point pt = currentControl.toControl (getCursorLocation ());
		currentControl.sendMouseEvent (SWT.MouseHover, (short)0, chord, (short)pt.x, (short)pt.y, modifiers);
	}
	return 0;
}

public boolean readAndDispatch () {
	checkDevice ();
	if (runTimers () || runEnterExit ()) return true;
	allowTimers = true;
	int [] outEvent  = new int [1];
	int status = OS.ReceiveNextEvent (0, null, OS.kEventDurationNoWait, true, outEvent);
	allowTimers = false;
	if (status == OS.noErr) {
		int eventClass = OS.GetEventClass (outEvent [0]);
		int eventKind = OS.GetEventKind (outEvent [0]);
		OS.SendEventToEventTarget (outEvent [0], OS.GetEventDispatcherTarget ());
		OS.ReleaseEvent (outEvent [0]);
		runPopups ();
		runDeferredEvents ();
		runGrabs ();
		if (eventClass != WAKE_CLASS || eventKind != WAKE_KIND) return true;
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
	actionCallback.dispose ();
	caretCallback.dispose ();
	commandCallback.dispose ();
	controlCallback.dispose ();
	drawItemCallback.dispose ();
	itemDataCallback.dispose ();
	itemNotificationCallback.dispose ();
	helpCallback.dispose ();
	hitTestCallback.dispose ();
	keyboardCallback.dispose ();
	menuCallback.dispose ();
	mouseHoverCallback.dispose ();
	mouseCallback.dispose ();
	trackingCallback.dispose ();
	windowCallback.dispose ();
	actionCallback = caretCallback = commandCallback = null;
	controlCallback = drawItemCallback = itemDataCallback = itemNotificationCallback = null;
	helpCallback = hitTestCallback = keyboardCallback = menuCallback = null;
	mouseHoverCallback = mouseCallback = trackingCallback = windowCallback = null;
	actionProc = caretProc = commandProc = 0;
	controlProc = drawItemProc = itemDataProc = itemNotificationProc = 0;
	helpProc = hitTestProc = keyboardProc = menuProc = 0;
	mouseHoverProc = mouseProc = trackingProc = windowProc = 0;
	timerCallback.dispose ();
	timerCallback = null;
	timerProc = 0;
	grabControl = helpControl = currentControl = null;
	if (helpString != 0) OS.CFRelease (helpString);
	helpString = 0;
	if (wakeEvent [0] != 0) OS.ReleaseEvent (wakeEvent [0]);
	wakeEvent = null;	
	//NOT DONE - call terminate TXN if this is the last display 
	//NOTE: - display create and dispose needs to be synchronized on all platforms
//	 TXNTerminateTextension ();

}

public void removeFilter (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (filterTable == null) return;
	filterTable.unhook (eventType, listener);
	if (filterTable.size () == 0) filterTable = null;
}

public void removeListener (int eventType, Listener listener) {
	checkDevice ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, listener);
}

void removeMenu (Menu menu) {
	if (menus == null) return;
	menus [menu.id - ID_START] = null;
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

boolean runAsyncMessages () {
	return synchronizer.runAsyncMessages ();
}

boolean runEnterExit () {
	//OPTIMIZE - use OS calls, no garbage, widget already hit tested in mouse move
	boolean eventSent = false;
	Control control = getCursorControl (false);
	if (control != currentControl) {
		if (currentControl != null && !currentControl.isDisposed ()) {
			eventSent = true;
			Point point = getCursorLocation ();
			int chord = OS.GetCurrentEventButtonState ();
			int modifiers = OS.GetCurrentEventKeyModifiers ();
			Point pt = currentControl.toControl (point);
			currentControl.sendMouseEvent (SWT.MouseExit, (short)0, chord, (short)pt.x, (short)pt.y, modifiers);
			if (mouseHoverID != 0) OS.RemoveEventLoopTimer (mouseHoverID);
			mouseHoverID = 0;
		}
		if ((currentControl = control) != null) {
			eventSent = true;
			Point point = getCursorLocation ();
			int chord = OS.GetCurrentEventButtonState ();
			int modifiers = OS.GetCurrentEventKeyModifiers ();
			Point controlPt = currentControl.toControl (point);
			currentControl.sendMouseEvent (SWT.MouseEnter, (short)0, chord, (short)controlPt.x, (short)controlPt.y, modifiers);
			if (mouseHoverID != 0) OS.RemoveEventLoopTimer (mouseHoverID);
			int [] id = new int [1], outDelay = new int [1];
			OS.HMGetTagDelay (outDelay);
			int eventLoop = OS.GetCurrentEventLoop ();
			OS.InstallEventLoopTimer (eventLoop, outDelay [0] / 1000.0, 0.0, mouseHoverProc, 0, id);
			mouseHoverID = id [0];
		}
	}
	if (control != null) {
		Point point = getCursorLocation ();
		int modifiers = OS.GetCurrentEventKeyModifiers ();
		Point windowPt = control.getShell ().toControl (point);
		org.eclipse.swt.internal.carbon.Point localPoint = new org.eclipse.swt.internal.carbon.Point ();
		OS.SetPt (localPoint, (short) windowPt.x, (short) windowPt.y);
		boolean [] cursorWasSet = new boolean [1];
		OS.HandleControlSetCursor (control.handle, localPoint, (short) modifiers, cursorWasSet);
		if (!cursorWasSet [0]) OS.SetThemeCursor (OS.kThemeArrowCursor);
	}
	return eventSent;
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

void runGrabs () {
	if (grabControl == null) return;
	Rect rect = new Rect ();
	int [] outModifiers = new int [1];
	short [] outResult = new short [1];
	org.eclipse.swt.internal.carbon.Point outPt = new org.eclipse.swt.internal.carbon.Point ();
	try {
		while (grabControl != null && !grabControl.isDisposed () && outResult [0] != OS.kMouseTrackingMouseUp) {
			lastModifiers = OS.GetCurrentEventKeyModifiers ();
			int oldState = OS.GetCurrentEventButtonState ();
			OS.TrackMouseLocationWithOptions (0, 0, OS.kEventDurationForever, outPt, outModifiers, outResult);
			int type = 0, button = 0;
			switch ((int)outResult [0]) {
				case OS.kMouseTrackingMouseDown: {
					type = SWT.MouseDown;
					int newState = OS.GetCurrentEventButtonState ();
					if ((oldState & 0x1) == 0 && (newState & 0x1) != 0) button = 1;
					if ((oldState & 0x2) == 0 && (newState & 0x2) != 0) button = 2;
					if ((oldState & 0x4) == 0 && (newState & 0x4) != 0) button = 3;
					break;
				}
				case OS.kMouseTrackingMouseUp: {
					type = SWT.MouseUp;
					int newState = OS.GetCurrentEventButtonState ();
					if ((oldState & 0x1) != 0 && (newState & 0x1) == 0) button = 1;
					if ((oldState & 0x2) != 0 && (newState & 0x2) == 0) button = 2;
					if ((oldState & 0x4) != 0 && (newState & 0x4) == 0) button = 3;
					break;
				}
				case OS.kMouseTrackingMouseExited: 				type = SWT.MouseExit; break;
				case OS.kMouseTrackingMouseEntered: 				type = SWT.MouseEnter; break;
				case OS.kMouseTrackingMouseDragged: 				type = SWT.MouseMove; break;
				case OS.kMouseTrackingMouseKeyModifiersChanged:	break;
				case OS.kMouseTrackingUserCancelled:				break;
				case OS.kMouseTrackingTimedOut: 					break;
				case OS.kMouseTrackingMouseMoved: 					type = SWT.MouseMove; break;
			}
			if (type != 0) {	
				int handle = grabControl.handle;
				int window = OS.GetControlOwner (handle);
				OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
				int x = outPt.h - rect.left;
				int y = outPt.v - rect.top;
				OS.GetControlBounds (handle, rect);
				x -= rect.left;
				y -=  rect.top;
				int chord = OS.GetCurrentEventButtonState ();
				grabControl.sendMouseEvent (type, (short)button, chord, (short)x, (short)y, outModifiers [0]);
				//TEMPORARY CODE
				update ();
			}
		}
	} finally {
		grabControl = null;
	}
}

boolean runPopups () {
	if (popups == null) return false;
	grabControl = null;
	boolean result = false;
	while (popups != null) {
		Menu menu = popups [0];
		if (menu == null) break;
		int length = popups.length;
		System.arraycopy (popups, 1, popups, 0, --length);
		popups [length] = null;
		menu._setVisible (true);
		result = true;
	}
	popups = null;
	return result;
}

boolean runTimers () {
	if (timerList == null || timerIds == null) return false;
	boolean result = false;
	for (int i=0; i<timerList.length; i++) {
		if (timerIds [i] == -1) {
			Runnable runnable = timerList [i];
			timerList [i] = null;
			timerIds [i] = 0;
			if (runnable != null) {
				runnable.run ();
				result = true;
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
 * to set the name used for resource lookup.
 *
 * @param name the new app name
 */
public static void setAppName (String name) {
}

void setCurrentCaret (Caret caret) {
	if (caretID != 0) OS.RemoveEventLoopTimer (caretID);
	caretID = 0;
	currentCaret = caret;
	if (currentCaret != null) {
		int blinkRate = currentCaret.blinkRate;
		int [] timerId = new int [1];
		double time = blinkRate / 1000.0;
		int eventLoop = OS.GetCurrentEventLoop ();
		OS.InstallEventLoopTimer (eventLoop, time, time, caretProc, 0, timerId);
		caretID = timerId [0];
	}
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
	/* Not possible on the MAC */
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

void setMenuBar (Menu menu) {
	/*
	* Feature in the Macintosh.  SetRootMenu() does not
	* accept NULL to indicate that their should be no
	* menu bar. The fix is to create a temporary empty
	* menu, set that to be the menu bar, clear the menu
	* bar and then delete the temporary menu.
	*/
	if (menu == menuBar) return;
	int theMenu = 0;
	if (menu == null) {
		int outMenuRef [] = new int [1];
		OS.CreateNewMenu ((short) ID_TEMPORARY, 0, outMenuRef);
		theMenu = outMenuRef [0];
	} else {
		theMenu = menu.handle;
	}
	OS.SetRootMenu (theMenu);
	if (menu == null) {
		OS.ClearMenuBar ();
		OS.DeleteMenu (OS.GetMenuID (theMenu));
		OS.DisposeMenu (theMenu);
	}
	menuBar = menu;
}

public boolean sleep () {
	checkDevice ();
	//TEMPORARY CODE
	return OS.ReceiveNextEvent (0, null, 50 / 1000.0, false, null) == OS.noErr;
//	return OS.ReceiveNextEvent (0, null, OS.kEventDurationForever, false, null) == OS.noErr;
}

public void syncExec (Runnable runnable) {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	synchronizer.syncExec (runnable);
}

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
		int timerId = timerIds [index];
		if (milliseconds < 0) {
			OS.RemoveEventLoopTimer (timerId);
			timerList [index] = null;
			timerIds [index] = 0;
		} else {
			OS.SetEventLoopTimerNextFireTime (timerId, milliseconds / 1000.0);
		}
		return;
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
		int [] newTimerIds = new int [timerIds.length + 4];
		System.arraycopy (timerIds, 0, newTimerIds, 0, timerIds.length);
		timerIds = newTimerIds;
	}
	int [] timerId = new int [1];
	int eventLoop = OS.GetCurrentEventLoop ();
	OS.InstallEventLoopTimer (eventLoop, milliseconds / 1000.0, 0.0, timerProc, index, timerId);
	if (timerId [0] != 0) {
		timerIds [index] = timerId [0];
		timerList [index] = runnable;
	}
}

int timerProc (int id, int index) {
	if (timerList == null) return 0;
	if (0 <= index && index < timerList.length) {
		if (allowTimers) {
			Runnable runnable = timerList [index];
			timerList [index] = null;
			timerIds [index] = 0;
			if (runnable != null) runnable.run ();
		} else {
			timerIds [index] = -1;
			OS.PostEventToQueue (queue, wakeEvent [0], (short) OS.kEventPriorityStandard);
		}
	}
	return 0;
}

int trackingProc (int browser, int itemID, int property, int theRect, int startPt, int modifiers) {
	Widget widget = WidgetTable.get (browser);
	if (widget != null) return widget.trackingProc (browser, itemID, property, theRect, startPt, modifiers);
	return OS.noErr;
}

public void update () {
	checkDevice ();
	int [] outEvent = new int [1];
	int [] mask = new int [] {OS.kEventClassWindow, OS.kEventWindowUpdate};
	while (OS.ReceiveNextEvent (mask.length / 2, mask, OS.kEventDurationNoWait, true, outEvent) == OS.noErr) {
		/*
		* Bug in the Macintosh.  For some reason, when a hierarchy of
		* windows is disposed from kEventWindowClose, despite the fact
		* that DisposeWindow() has been called, the window is not
		* disposed and there are outstandings kEventWindowUpdate events
		* in the event queue.  Dispatching these events will cause a
		* segment fault.  The fix is to dispatch events to visible
		* window only.
		*/
		int [] theWindow = new int [1];
		OS.GetEventParameter (outEvent [0], OS.kEventParamDirectObject, OS.typeWindowRef, null, 4, null, theWindow);
		if (OS.IsWindowVisible (theWindow [0])) OS.SendEventToEventTarget (outEvent [0], OS.GetEventDispatcherTarget ());
		OS.ReleaseEvent (outEvent [0]);
	}
}

void updateMenuBar () {
	updateMenuBar (getActiveShell ());
}

void updateMenuBar (Shell shell) {
	if (shell == null) shell = getActiveShell ();
	boolean modal = false;
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	Shell menuShell = shell;
	while (menuShell != null) {
		if (menuShell.menuBar != null) break;
		if ((menuShell.style & mask) != 0) modal = true;
		menuShell = (Shell) menuShell.parent;
	}
	/*
	* Feature in the Macintosh.  For some reason, when a modal shell
	* is active, DisableMenuItem() when called with zero (indicating
	* that the entire menu is to be disabled) will not disable the
	* current menu bar.  The fix is to disable each individual menu
	* item.
	*/
	if (menuBar != null) {
		MenuItem [] items = menuBar.getItems ();
		for (int i=0; i<items.length; i++) {
			if (items [i].getEnabled ()) items [i]._setEnabled (true);
		}
	}
	setMenuBar (menuShell != null ? menuShell.menuBar : null);
	if (menuBar != null && (modal || menuShell != shell)) {
		MenuItem [] items = menuBar.getItems ();
		for (int i=0; i<items.length; i++) items [i]._setEnabled (false);
	}
}

public void wake () {
	if (isDisposed ()) error (SWT.ERROR_DEVICE_DISPOSED);
	if (thread == Thread.currentThread ()) return;
	OS.PostEventToQueue (queue, wakeEvent [0], (short) OS.kEventPriorityStandard);
}

int windowProc (int nextHandler, int theEvent, int userData) {
	Widget widget = WidgetTable.get (userData);
	if (widget == null) {
		int [] theWindow = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeWindowRef, null, 4, null, theWindow);
		int [] theRoot = new int [1];
		OS.GetRootControl (theWindow [0], theRoot);
		widget = WidgetTable.get (theRoot [0]);
	}
	if (widget != null)  return widget.windowProc (nextHandler, theEvent, userData); 
	return OS.eventNotHandledErr;
}

}

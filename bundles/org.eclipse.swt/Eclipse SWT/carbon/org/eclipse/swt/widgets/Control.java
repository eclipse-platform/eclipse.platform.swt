package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.CGRect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public abstract class Control extends Widget implements Drawable {
	/**
	* the handle to the OS resource
	* (Warning: This field is platform dependent)
	*/
	public int handle;
	Composite parent;
	String toolTipText;
	Object layoutData;
	int drawCount;
	Menu menu;

Control () {
	/* Do nothing */
}

public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
}

int actionProc (int theControl, int partCode) {
	return 0;
}

public void addControlListener(ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

public void addFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.FocusIn,typedListener);
	addListener(SWT.FocusOut,typedListener);
}

public void addHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Help, typedListener);
}

public void addKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.KeyUp,typedListener);
	addListener(SWT.KeyDown,typedListener);
}

public void addMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseDown,typedListener);
	addListener(SWT.MouseUp,typedListener);
	addListener(SWT.MouseDoubleClick,typedListener);
}

public void addMouseTrackListener (MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.MouseEnter,typedListener);
	addListener (SWT.MouseExit,typedListener);
	addListener (SWT.MouseHover,typedListener);
}

public void addMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.MouseMove,typedListener);
}

public void addPaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Paint,typedListener);
}

public void addTraverseListener (TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Traverse,typedListener);
}

int controlProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventControlActivate:				return kEventControlActivate (nextHandler, theEvent, userData);
		case OS.kEventControlBoundsChanged:		return kEventControlBoundsChanged (nextHandler, theEvent, userData);
		case OS.kEventControlClick:				return kEventControlClick (nextHandler, theEvent, userData);
		case OS.kEventControlContextualMenuClick:	return kEventControlContextualMenuClick (nextHandler, theEvent, userData);
		case OS.kEventControlDeactivate:			return kEventControlDeactivate (nextHandler, theEvent, userData);
		case OS.kEventControlDraw:					return kEventControlDraw (nextHandler, theEvent, userData);
		case OS.kEventControlHit:					return kEventControlHit (nextHandler, theEvent, userData);
		case OS.kEventControlSetFocusPart:			return kEventControlSetFocusPart (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

public Point computeSize (int wHint, int hHint) {
	return computeSize (wHint, hHint, true);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

Control computeTabGroup () {
	if (isTabGroup()) return this;
	return parent.computeTabGroup ();
}

Control[] computeTabList() {
	if (isTabGroup()) {
		if (getVisible() && getEnabled()) {
			return new Control[] {this};
		}
	}
	return new Control[0];
}

Control computeTabRoot () {
	Control[] tabList = parent._getTabList();
	if (tabList != null) {
		int index = 0;
		while (index < tabList.length) {
			if (tabList [index] == this) break;
			index++;
		}
		if (index == tabList.length) {
			if (isTabGroup ()) return this;
		}
	}
	return parent.computeTabRoot ();
}

void createHandle () {
}

void createWidget () {
	createHandle ();
	register ();
	hookEvents ();
}

void deregister () {
	if (handle == 0) return;
	WidgetTable.remove (handle);
}

void destroyWidget () {
	int theControl = topHandle ();
	releaseHandle ();
	if (theControl != 0) {
		OS.DisposeControl (theControl);
	}
}

public boolean forceFocus () {
	checkWidget();
	int window = OS.GetControlOwner (handle);
	return OS.SetKeyboardFocus (window, handle, (short)OS.kControlFocusNextPart) == OS.noErr;
}

public Color getBackground () {
	checkWidget();
	//WRONG
	return getDisplay ().getSystemColor (SWT.COLOR_WHITE);
}

public int getBorderWidth () {
	checkWidget();
    return 0;
}

public Rectangle getBounds () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetControlBounds (topHandle (), rect);
	return new Rectangle (rect.top, rect.left, rect.right - rect.left, rect.bottom - rect.top);
}

public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

public Font getFont () {
	checkWidget();
	return null;
}

public Color getForeground () {
	checkWidget();
	//WRONG
	return getDisplay ().getSystemColor (SWT.COLOR_BLACK);
}

public Object getLayoutData () {
	checkWidget();
	return layoutData;
}

public Point getLocation () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetControlBounds (topHandle (), rect);
	return new Point (rect.top, rect.left);
}

public Menu getMenu () {
	checkWidget();
	return menu;
}

public Composite getParent () {
	checkWidget();
	return parent;
}

Control [] getPath () {
	int count = 0;
	Shell shell = getShell ();
	Control control = this;
	while (control != shell) {
		count++;
		control = control.parent;
	}
	control = this;
	Control [] result = new Control [count];
	while (control != shell) {
		result [--count] = control;
		control = control.parent;
	}
	return result;
}

public Shell getShell () {
	checkWidget();
	return parent.getShell ();
}

public Point getSize () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetControlBounds (topHandle (), rect);
	return new Point (rect.right - rect.left, rect.bottom - rect.top);
}

public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

public boolean getVisible () {
	checkWidget();
	return (state & HIDDEN) == 0;
}

boolean hasFocus () {
	return (this == getDisplay ().getFocusControl ());
}

void hookEvents () {
	Display display = getDisplay ();
	int controlProc = display.controlProc;
	int [] mask = new int [] {
		OS.kEventClassControl, OS.kEventControlActivate,
		OS.kEventClassControl, OS.kEventControlClick,
		OS.kEventClassControl, OS.kEventControlContextualMenuClick,
		OS.kEventClassControl, OS.kEventControlDeactivate,
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlHit,
		OS.kEventClassControl, OS.kEventControlSetFocusPart,
		//MUST BE LAST
		OS.kEventClassControl, OS.kEventControlBoundsChanged,
	};
	//TEMPORARY CODE - hooking kEventControlBoundsChanged on the root control draws garbage
	int length = mask.length - (this instanceof Shell ? 1 : 0);
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, controlProc, length / 2, mask, handle, null);
}

public int internal_new_GC (GCData data) {
	checkWidget();
	int context = 0;
	if (data.paintEvent != 0) {
		int inEvent = data.paintEvent;
		int [] buffer = new int [1];
		OS.GetEventParameter (inEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, buffer);
		context = buffer [0];
	} else {
	}
	if (data != null) {
		data.device = getDisplay ();
//		data.foreground = getForegroundPixel ();
//		data.background = getBackgroundPixel ();
//		data.hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
//		data.hwnd = handle;
		Rect rect = new Rect ();
		int wHandle = OS.GetControlOwner (handle);
		int port = OS.GetWindowPort (wHandle);
		OS.GetPortBounds (port, rect);
		data.portRect = rect;
	}
	return context;
}

public void internal_dispose_GC (int context, GCData data) {
	checkWidget ();
	if (data.paintEvent != 0) {
	} else {
	}
}

public boolean isEnabled () {
	checkWidget();
	return OS.IsControlEnabled (topHandle ());
}

public boolean isFocusControl () {
	checkWidget();
	return hasFocus ();
}

public boolean isReparentable () {
	checkWidget();
	return false;
}

boolean isTabGroup () {
	return false;
}

boolean isTabItem () {
	return false;
}

public boolean isVisible () {
	checkWidget();
	return OS.HIViewIsVisible (topHandle ());
}

int menuProc (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int mouseProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventMouseDown: 		return kEventMouseDown (nextHandler, theEvent, userData);
		case OS.kEventMouseUp: 		return kEventMouseUp (nextHandler, theEvent, userData);
		case OS.kEventMouseDragged:	return kEventMouseDragged (nextHandler, theEvent, userData);
//		case OS.kEventMouseEntered:		return kEventMouseEntered (nextHandler, theEvent, userData);
//		case OS.kEventMouseExited:		return kEventMouseExited (nextHandler, theEvent, userData);
		case OS.kEventMouseMoved:		return kEventMouseMoved (nextHandler, theEvent, userData);
		case OS.kEventMouseWheelMoved:	return kEventMouseWheelMoved (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

Decorations menuShell () {
	return parent.menuShell ();
}

int itemDataProc (int browser, int item, int property, int itemData, int setValue) {
	return OS.noErr;
}

int itemNotificationProc (int browser, int item, int message) {
	return OS.noErr;
}

int keyboardProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventRawKeyDown:				return kEventRawKeyDown (nextHandler, theEvent, userData);
		case OS.kEventRawKeyModifiersChanged:	return kEventRawKeyModifiersChanged (nextHandler, theEvent, userData);
		case OS.kEventRawKeyRepeat:			return kEventRawKeyRepeat (nextHandler, theEvent, userData);
		case OS.kEventRawKeyUp:				return kEventRawKeyUp (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int kEventControlActivate (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlBoundsChanged (int nextHandler, int theEvent, int userData) {
	int [] attributes = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAttributes, OS.typeUInt32, null, attributes.length * 4, null, attributes);
	if ((attributes [0] & OS.kControlBoundsChangePositionChanged) != 0) {
		sendEvent (SWT.Move);
		if (isDisposed ()) return OS.noErr;
	}
	if ((attributes [0] & OS.kControlBoundsChangeSizeChanged) != 0) {
		sendEvent (SWT.Resize);
		if (isDisposed ()) return OS.noErr;
	}
	return OS.eventNotHandledErr;
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlContextualMenuClick (int nextHandler, int theEvent, int userData) {
	if (menu != null && !menu.isDisposed ()) {
		CGPoint pt = new CGPoint ();
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, pt.sizeof, null, pt);
		menu.setLocation ((int) pt.x, (int) pt.y);
		menu.setVisible (true);
		return OS.noErr;
	}
	return OS.eventNotHandledErr;
}

int kEventControlDeactivate (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	/* Exit early - don't draw the background */
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) {
		return OS.eventNotHandledErr;
	}
	int result = OS.CallNextEventHandler (nextHandler, theEvent);

	/* Retrieve the damage region */
	int [] region = new int [1];	
	OS.GetEventParameter(theEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle, null, 4, null, region);
	Rect bounds = new Rect();
	OS.GetRegionBounds(region [0], bounds);

	GCData data = new GCData ();
	data.paintEvent = theEvent;
	GC gc = GC.carbon_new (this, data);
	
	/* Send the paint event */
	Event event = new Event ();
	event.gc = gc;
	event.x = bounds.left;
	event.y = bounds.top;
	event.width = bounds.right - bounds.left;
	event.height = bounds.bottom - bounds.top;
//	gc.setClipping (Region.carbon_new (region [0]));
	sendEvent (SWT.Paint, event);
	event.gc = null;
	gc.dispose ();

	return result;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	short [] part = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
	if (part [0] == 0) {
		sendEvent (SWT.FocusOut);
	} else {
		sendEvent (SWT.FocusIn);
	}
	return OS.eventNotHandledErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	if ((state & GRAB) != 0) {
		int [] clickCount = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamClickCount, OS.typeUInt32, null, 4, null, clickCount);
		sendMouseEvent (SWT.MouseDown, theEvent);
		if (clickCount [0] == 2) sendMouseEvent (SWT.MouseDoubleClick, theEvent);
		Display display = getDisplay ();
		display.grabControl = this;
	}
//	if ((state & CANVAS) != 0 && userData != 0) return OS.noErr;
	return OS.eventNotHandledErr;
}

int kEventMouseDragged (int nextHandler, int theEvent, int userData) {
	sendMouseEvent (SWT.MouseMove, theEvent);
	return OS.eventNotHandledErr;
}

int kEventMouseMoved (int nextHandler, int theEvent, int userData) {
	sendMouseEvent (SWT.MouseMove, theEvent);
	return OS.eventNotHandledErr;
}

int kEventMouseUp (int nextHandler, int theEvent, int userData) {
	sendMouseEvent (SWT.MouseUp, theEvent);
	return OS.eventNotHandledErr;
}

int kEventMouseWheelMoved (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyUp (int nextHandler, int theEvent, int userData) {
	if (!sendKeyEvent (SWT.KeyUp, theEvent)) return OS.noErr;
	return OS.eventNotHandledErr;
}

int kEventRawKeyRepeat (int nextHandler, int theEvent, int userData) {
	if (!sendKeyEvent (SWT.KeyDown, theEvent)) return OS.noErr;
	return OS.eventNotHandledErr;
}

int kEventRawKeyModifiersChanged (int nextHandler, int theEvent, int userData) {
	int [] modifiers = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, modifiers.length * 4, null, modifiers);
	Display display = getDisplay ();
	int lastModifiers = display.lastModifiers;
	int type = SWT.KeyUp;
	if ((modifiers [0] & OS.shiftKey) != 0 && (lastModifiers & OS.shiftKey) == 0) type = SWT.KeyDown;
	if ((modifiers [0] & OS.controlKey) != 0 && (lastModifiers & OS.controlKey) == 0) type = SWT.KeyDown;
	if ((modifiers [0] & OS.cmdKey) != 0 && (lastModifiers & OS.cmdKey) == 0) type = SWT.KeyDown;
	if ((modifiers [0] & OS.optionKey) != 0 && (lastModifiers & OS.optionKey) == 0) type = SWT.KeyDown;
	boolean result = sendKeyEvent (SWT.KeyDown, theEvent);
	display.lastModifiers = modifiers [0];
	return result ? OS.eventNotHandledErr : OS.noErr;
}

int kEventRawKeyDown (int nextHandler, int theEvent, int userData) {
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	if (keyCode [0] == 114) {
		//HELP KEY
	}
	if (!sendKeyEvent (SWT.KeyDown, theEvent)) return OS.noErr;
	return OS.eventNotHandledErr;
}

int kEventWindowActivated (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowBoundsChanged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowClose (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowCollapsed (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowDeactivated (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowExpanded (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowFocusAcquired (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowFocusRelinquish (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

public void moveAbove (Control control) {
	checkWidget();
	int inOther = 0;
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		inOther = control.topHandle ();
	}
	OS.HIViewSetZOrder (topHandle (), OS.kHIViewZOrderAbove, inOther);
}

public void moveBelow (Control control) {
	checkWidget();
	int inOther = 0;
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		inOther = control.topHandle ();
	}
	OS.HIViewSetZOrder (topHandle (), OS.kHIViewZOrderBelow, inOther);
}

public void pack () {
	checkWidget();
	pack (true);
}

public void pack (boolean changed) {
	checkWidget();
	setSize (computeSize (SWT.DEFAULT, SWT.DEFAULT, changed));
}

public void redraw () {
	checkWidget();
	OS.HIViewSetNeedsDisplay (handle, true);
}

public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget ();
	Rect rect = new Rect ();
	OS.SetRect (rect, (short)x, (short)y, (short)(x + width), (short)(y + height));
	int inRgn = OS.NewRgn ();
	OS.RectRgn (inRgn, rect);
	OS.HIViewSetNeedsDisplayInRegion (handle, inRgn, true);
	OS.DisposeRgn (inRgn);
}

void register () {
	if (handle == 0) return;
	WidgetTable.put (handle, this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
	deregister ();
	parent = null;
	layoutData = null;
}

public void removeControlListener (ControlListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

public void removeFocusListener(FocusListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.FocusIn, listener);
	eventTable.unhook(SWT.FocusOut, listener);
}

public void removeHelpListener (HelpListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Help, listener);
}

public void removeKeyListener(KeyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.KeyUp, listener);
	eventTable.unhook(SWT.KeyDown, listener);
}

public void removeMouseListener(MouseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseDown, listener);
	eventTable.unhook(SWT.MouseUp, listener);
	eventTable.unhook(SWT.MouseDoubleClick, listener);
}

public void removeMouseMoveListener(MouseMoveListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.MouseMove, listener);
}

public void removeMouseTrackListener(MouseTrackListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.MouseEnter, listener);
	eventTable.unhook (SWT.MouseExit, listener);
	eventTable.unhook (SWT.MouseHover, listener);
}

public void removePaintListener(PaintListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Paint, listener);
}

public void removeTraverseListener(TraverseListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Traverse, listener);
}

int scrollBarActionProc (int theControl, int partCode) {
	return OS.eventNotHandledErr;
}

boolean sendKeyEvent (int type, int theEvent) {
	Event event = new Event ();
	event.type = type;
	setKeyState (event, theEvent);
	postEvent (type, event);
	return sendKeyEvent (type, event);
}

boolean sendKeyEvent (int type, Event event) {
	postEvent (type, event);
	return true;
}

boolean sendMouseEvent (int type, int theEvent) {
	short [] button = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseButton, OS.typeMouseButton, null, 2, null, button);
	return sendMouseEvent (type, button [0], theEvent);
}

boolean sendMouseEvent (int type, short button, int theEvent) {
	Event event = new Event ();
	event.type = type;
	CGPoint pt = new CGPoint ();
	if (OS.GetEventParameter (theEvent, OS.kEventParamWindowMouseLocation, OS.typeHIPoint, null, pt.sizeof, null, pt) != OS.noErr) {
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeHIPoint, null, pt.sizeof, null, pt);
		Rect rect = new Rect ();
		int window = OS.GetControlOwner (handle);
		OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
		pt.x -= rect.left;
		pt.y -= rect.top;
	}
	OS.HIViewConvertPoint (pt, 0, handle);
	event.x = (int) pt.x;
	event.y = (int) pt.y;
	setInputState (event, theEvent);
	postEvent (type, event);
	return true;
}

boolean sendMouseEvent (int type, short button, int chord, short x, short y, int modifiers) {
	Event event = new Event ();
	event.type = type;
	event.x = x;
	event.y = y;
	setInputState (event, button, chord, modifiers);
	sendEvent (type, event);
	return true;
}

public void setBackground (Color color) {
	checkWidget();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	width = Math.max (0, width);
	height = Math.max (0, height);
	Rect rect = new Rect ();
	OS.SetRect (rect, (short)x, (short)y, (short) (x + width), (short) (y + height));
	OS.SetControlBounds (topHandle (), rect);
}

public void setBounds (Rectangle rect) {
	if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
	setBounds (rect.x, rect.y, rect.width, rect.height);
}

public void setCapture (boolean capture) {
	checkWidget();
}

public void setCursor (Cursor cursor) {
	checkWidget();
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (enabled) {
		if ((state & DISABLED) == 0) return;
		state &= ~DISABLED;
		OS.EnableControl (topHandle ());
	} else {
		if ((state & DISABLED) != 0) return;
		state |= DISABLED;
		OS.DisableControl (topHandle ());
	}
}

public boolean setFocus () {
	checkWidget();
	return forceFocus ();
}

public void setFont (Font font) {
	checkWidget();
}

public void setForeground (Color color) {
	checkWidget();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
}

public void setLayoutData (Object layoutData) {
	checkWidget();
	this.layoutData = layoutData;
}

public void setLocation (int x, int y) {
	checkWidget();
    OS.MoveControl (topHandle (), (short)x, (short)y);
}

public void setLocation (Point location) {
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

public void setMenu (Menu menu) {
	checkWidget();
	if (menu != null) {
		if (menu.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
		if (menu.parent != menuShell ()) {
			error (SWT.ERROR_INVALID_PARENT);
		}
	}
	this.menu = menu;
}

public boolean setParent (Composite parent) {
	checkWidget();
	if (parent.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return false;
}

public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) {
			OS.HIViewSetDrawingEnabled (handle, true);
			OS.HIViewSetNeedsDisplay (handle, true);
		}
	} else {
		if (drawCount++ == 0) {
			OS.HIViewSetDrawingEnabled (handle, false);
		}
	}
}

boolean setRadioSelection (boolean value){
	return false;
}

public void setSize (int width, int height) {
	checkWidget();
	width = Math.max (0, width);
	height = Math.max (0, height);
	OS.SizeControl (topHandle (), (short) width, (short) height);
}

public void setSize (Point size) {
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}

boolean setTabGroupFocus () {
	return false;
}

boolean setTabItemFocus () {
	return false;
}

public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}

public void setVisible (boolean visible) {
	checkWidget();
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	OS.HIViewSetVisible (topHandle (), visible);
	sendEvent (visible ? SWT.Show : SWT.Hide);
}

public Point toControl (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
	CGPoint ioPoint = new CGPoint ();
	ioPoint.x = point.x - rect.left;
	ioPoint.y = point.y - rect.top;
	OS.HIViewConvertPoint (ioPoint, 0, handle); 
    return new Point ((int) ioPoint.x, (int) ioPoint.y);
}

public Point toDisplay (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	CGPoint ioPoint = new CGPoint ();
	ioPoint.x = point.x;
	ioPoint.y = point.y;
	OS.HIViewConvertPoint (ioPoint, handle, 0);
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	OS.GetWindowBounds (window, (short) OS.kWindowStructureRgn, rect);
    return new Point (rect.left + (int) ioPoint.x, rect.top + (int) ioPoint.y);
}

int toolItemProc (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int topHandle () {
	return handle;
}

boolean traverseMnemonic (char key) {
	return false;
}

public boolean traverse (int traversal) {
	checkWidget();
	if (!isFocusControl () && !setFocus ()) return false;
	Event event = new Event ();
	event.doit = true;
	event.detail = traversal;
	return traverse (event);
}

boolean traverse (Event event) {
	sendEvent (SWT.Traverse, event);
	if (isDisposed ()) return false;
	if (!event.doit) return false;
	switch (event.detail) {
		case SWT.TRAVERSE_NONE:				return true;
		case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
		case SWT.TRAVERSE_RETURN:			return traverseReturn ();
		case SWT.TRAVERSE_TAB_NEXT:			return traverseGroup (true);
		case SWT.TRAVERSE_TAB_PREVIOUS:		return traverseGroup (false);
		case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
		case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
		case SWT.TRAVERSE_MNEMONIC:			return traverseMnemonic (event);	
		case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
		case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
	}
	return false;
}

boolean traverseEscape () {
	return false;
}

boolean traverseGroup (boolean next) {
	return false;
}

boolean traverseItem (boolean next) {
	return false;
}

boolean traverseReturn () {
	return false;
}

boolean traversePage (boolean next) {
	return false;
}

boolean traverseMnemonic (Event event) {
	return false;
}

public void update () {
	checkWidget();
}

int windowProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventWindowActivated:			return kEventWindowActivated (nextHandler, theEvent, userData);	
		case OS.kEventWindowBoundsChanged:		return kEventWindowBoundsChanged (nextHandler, theEvent, userData);
		case OS.kEventWindowClose:				return kEventWindowClose (nextHandler, theEvent, userData);
		case OS.kEventWindowCollapsed:			return kEventWindowCollapsed (nextHandler, theEvent, userData);
		case OS.kEventWindowDeactivated:		return kEventWindowDeactivated (nextHandler, theEvent, userData);
		case OS.kEventWindowExpanded:			return kEventWindowExpanded (nextHandler, theEvent, userData);
		case OS.kEventWindowFocusAcquired:		return kEventWindowFocusAcquired (nextHandler, theEvent, userData);
		case OS.kEventWindowFocusRelinquish:	return kEventWindowFocusRelinquish (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

}
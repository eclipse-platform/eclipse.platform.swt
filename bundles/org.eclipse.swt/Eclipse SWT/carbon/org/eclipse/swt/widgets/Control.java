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

Control () {
	/* Do nothing */
}

public Control (Composite parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
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
	int theControl = handle;
	releaseHandle ();
	if (theControl != 0) {
		OS.DisposeControl (theControl);
	}
}

public boolean forceFocus () {
	checkWidget();
	int window = OS.GetControlOwner (handle);
	OS.SetKeyboardFocus (window, handle, (short)-1 /*???OS.kControlFocusNoPart*/);
	return isFocusControl ();
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
	OS.GetControlBounds (handle, rect);
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
	OS.GetControlBounds (handle, rect);
	return new Point (rect.top, rect.left);
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
	OS.GetControlBounds (handle, rect);
	return new Point (rect.bottom - rect.top, rect.right - rect.left);
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
	int [] mask = new int [] {
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlBoundsChanged,
		OS.kEventClassControl, OS.kEventControlHit,
	};
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, display.windowProc, mask.length / 2, mask, handle, null);
}

public int internal_new_GC (GCData data) {
	checkWidget();
	return 0;
}

public void internal_dispose_GC (int xGC, GCData data) {
	checkWidget ();
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
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
	return OS.HIViewIsVisible (handle);
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	if ((state & GRAB) != 0) {
		sendMouseEvent (SWT.MouseDown, theEvent);
		Display display = getDisplay ();
		display.grabControl = this;
	}
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

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyUp (int nextHandler, int theEvent, int userData) {
	sendKeyEvent (SWT.KeyUp, theEvent);
	return OS.eventNotHandledErr;
}

int kEventRawKeyRepeat (int nextHandler, int theEvent, int userData) {
	sendKeyEvent (SWT.KeyDown, theEvent);
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
	sendKeyEvent (type, theEvent);
	display.lastModifiers = modifiers [0];
	return OS.eventNotHandledErr;
}

int kEventRawKeyDown (int nextHandler, int theEvent, int userData) {
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	if (keyCode [0] == 114) {	// help key
//		windowProc(focus.handle, SWT.Help);
//		return OS.noErr;
	}
	sendKeyEvent (SWT.KeyDown, theEvent);
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

public void moveAbove (Control control) {
	checkWidget();
	int inOther = 0;
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		inOther = control.handle;
	}
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderAbove, inOther);
}

public void moveBelow (Control control) {
	checkWidget();
	int inOther = 0;
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		inOther = control.handle;
	}
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderBelow, inOther);
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

boolean sendKeyEvent (int type, int theEvent) {
	Event event = new Event ();
	event.type = type;
	setKeyState (event, theEvent);
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
	event.button = button;
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

boolean sendMouseEvent (int type, short button, short x, short y, int modifiers) {
	Event event = new Event ();
	event.type = type;
	event.button = button;
	event.x = x;
	event.y = y;
	setInputState (event, button, modifiers);
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
	OS.SetControlBounds (handle, rect);
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
		OS.EnableControl(handle);
	} else {
		if ((state & DISABLED) != 0) return;
		state |= DISABLED;
		OS.DisableControl(handle);
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
    OS.MoveControl (handle, (short)x, (short)y);
}

public void setLocation (Point location) {
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
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

public void setSize (int width, int height) {
	checkWidget();
	width = Math.max (0, width);
	height = Math.max (0, height);
	OS.SizeControl (handle, (short) width, (short) height);
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
	OS.HIViewSetVisible (handle, visible);
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

}
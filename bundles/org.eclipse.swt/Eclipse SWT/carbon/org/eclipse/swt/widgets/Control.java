package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CGPoint;
import org.eclipse.swt.internal.carbon.ControlFontStyleRec;
import org.eclipse.swt.internal.carbon.HMHelpContentRec;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.accessibility.Accessible;

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
	float [] foreground, background;
	Font font;
	Cursor cursor;
	Accessible accessible;

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

void createWidget () {
	super.createWidget ();
	setZOrder ();
}

Font defaultFont () {
	byte [] family = new byte [256];
	short [] size = new short [1];
	byte [] style = new byte [1];
	OS.GetThemeFont ((short) defaultThemeFont (), (short) OS.smSystemScript, family, size, style);
	short id = OS.FMGetFontFamilyFromName (family);
	int [] font = new int [1]; 
	OS.FMGetFontFromFontFamilyInstance (id, style [0], font, null);
	return Font.carbon_new (getDisplay (), font [0], id, style [0], size [0]);
}

int defaultThemeFont () {	
	return OS.kThemeSystemFont;
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (handle);
}

void destroyWidget () {
	int theControl = topHandle ();
	releaseHandle ();
	if (theControl != 0) {
		OS.DisposeControl (theControl);
	}
}

Cursor findCursor () {
	if (cursor != null) return cursor;
	return parent.findCursor ();
}

void fixFocus () {
	Shell shell = getShell ();
	Control control = this;
	while ((control = control.parent) != null) {
		if (control.setFocus () || control == shell) return;
	}
	int window = OS.GetControlOwner (handle);
	OS.ClearKeyboardFocus (window);
}

public boolean forceFocus () {
	checkWidget();
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	if (!isEnabled () || !isVisible ()/* || !isActive ()*/) return false;
	if (isFocusControl ()) return true;
	shell.bringToTop ();
	int window = OS.GetControlOwner (handle);
	return OS.SetKeyboardFocus (window, handle, (short)OS.kControlFocusNextPart) == OS.noErr;
}

public Accessible getAccessible () {
	checkWidget ();
	if (accessible == null) {
		accessible = Accessible.internal_new_Accessible (this);
	}
	return accessible;
}
	
public Color getBackground () {
	checkWidget();
	//WRONG
	if (background == null) return getDisplay ().getSystemColor (SWT.COLOR_WHITE);
	return Color.carbon_new (getDisplay (), background);
}

public int getBorderWidth () {
	checkWidget();
    return 0;
}

public Rectangle getBounds () {
	checkWidget();
	Rect rect = getControlBounds (topHandle ());
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

int getDrawCount () {
	if (drawCount > 0) return drawCount;
	return parent.getDrawCount ();
}

public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

public Font getFont () {
	checkWidget();
	return font != null ? font : defaultFont ();
}

public Color getForeground () {
	checkWidget();
	//WRONG
	if (foreground == null) return getDisplay ().getSystemColor (SWT.COLOR_BLACK);
	return Color.carbon_new (getDisplay (), foreground);
}

public Object getLayoutData () {
	checkWidget();
	return layoutData;
}

public Point getLocation () {
	checkWidget();
	Rect rect = getControlBounds (topHandle ());
	return new Point (rect.left, rect.top);
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
	Rect rect = getControlSize (topHandle ());
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
	return this == getDisplay ().getFocusControl ();
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
	Display display = getDisplay ();
    switch (inRequest) {
		case OS.kHMSupplyContent: {
			int [] contentProvided = new int [] {OS.kHMContentNotProvidedDontPropagate};
			if (toolTipText != null && toolTipText.length () != 0) {
				char [] buffer = new char [toolTipText.length ()];
				toolTipText.getChars (0, buffer.length, buffer, 0);
				int i=0, j=0;
				while (i < buffer.length) {
					if ((buffer [j++] = buffer [i++]) == Mnemonic) {
						if (i == buffer.length) {continue;}
						if (buffer [i] == Mnemonic) {i++; continue;}
						j--;
					}
				}
				if (display.helpString != 0) OS.CFRelease (display.helpString);
		    	display.helpString = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
				HMHelpContentRec helpContent = new HMHelpContentRec ();
				OS.memcpy (helpContent, ioHelpContent, HMHelpContentRec.sizeof);
		        helpContent.version = OS.kMacHelpVersion;
		        
		        /*
		        * Feature in the Macintosh.  Despite the fact that the Mac
		        * provides 23 different types of alignment for the help text,
		        * it does not allow the text to be positioned at the current
		        * mouse position.  The fix is to center the text in a rectangle
				* that surrounds the original position of the mouse.  As the
				* mouse is moved, this rectangle is grown to include the new
				* location of the mouse.  The help text is then centered by
				* the  Mac in the new rectangle that was carefully constructed
				* such that the help text will stay in the same position.
		        */
		        int cursorHeight = 16;
		        helpContent.tagSide = (short) OS.kHMAbsoluteCenterAligned;
				int x = (short) (inGlobalMouse & 0xFFFF);
				int y = (short) (inGlobalMouse >> 16);
				if (display.helpControl != this) {
					display.lastHelpX = x + cursorHeight / 2;
					display.lastHelpY = y + cursorHeight + cursorHeight / 2;			
				}
				int jitter = 4;
				int deltaX = Math.abs (display.lastHelpX - x) + jitter;
				int deltaY = Math.abs (display.lastHelpY - y) + jitter;
				x = display.lastHelpX - deltaX;
				y = display.lastHelpY - deltaY;
				int width = deltaX * 2;
				int height = deltaY * 2;
				display.helpControl = this;
		        helpContent.absHotRect_left = (short) x;
		     	helpContent.absHotRect_top = (short) y;
		        helpContent.absHotRect_right = (short) (x + width);
		        helpContent.absHotRect_bottom = (short) (y + height);
		        
		        helpContent.content0_contentType = OS.kHMCFStringContent;
		        helpContent.content0_tagCFString = display.helpString;
		        helpContent.content1_contentType = OS.kHMCFStringContent;
		        helpContent.content1_tagCFString = display.helpString;
				OS.memcpy (ioHelpContent, helpContent, HMHelpContentRec.sizeof);
				contentProvided [0] = OS.kHMContentProvided;
			}
			OS.memcpy (outContentProvided, contentProvided, 4);
			break;
		}
		case OS.kHMDisposeContent: {
			if (display.helpString != 0) OS.CFRelease (display.helpString);
			display.helpString = 0;
			break;
		}
	}
	return OS.noErr;
}

void hookEvents () {
	super.hookEvents ();
	Display display = getDisplay ();
	int controlProc = display.controlProc;
	int [] mask = new int [] {
		OS.kEventClassControl, OS.kEventControlActivate,
		OS.kEventClassControl, OS.kEventControlBoundsChanged,
		OS.kEventClassControl, OS.kEventControlClick,
		OS.kEventClassControl, OS.kEventControlContextualMenuClick,
		OS.kEventClassControl, OS.kEventControlDeactivate,
		OS.kEventClassControl, OS.kEventControlDraw,
		OS.kEventClassControl, OS.kEventControlHit,
		OS.kEventClassControl, OS.kEventControlSetCursor,
		OS.kEventClassControl, OS.kEventControlSetFocusPart,
		OS.kEventClassControl, OS.kEventControlTrack,
	};
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, controlProc, mask.length / 2, mask, handle, null);
	int helpProc = display.helpProc;
	OS.HMInstallControlContentCallback (handle, helpProc);
}

public int internal_new_GC (GCData data) {
	checkWidget();
	int [] buffer = new int [1];
	int context = 0, paintRgn = 0, visibleRgn = 0;
	if (data.paintEvent != 0) {
		int theEvent = data.paintEvent;
		OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, buffer);
		context = buffer [0];	
		OS.GetEventParameter (theEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle, null, 4, null, buffer);
		visibleRgn = paintRgn = buffer [0];
	}
	if (context == 0) {
		int window = OS.GetControlOwner (handle);
		int port = OS.GetWindowPort (window);
		OS.CreateCGContextForPort (port, buffer);
		context = buffer [0];
		if (context != 0) {
			Rect rect = new Rect ();
			OS.GetControlBounds (handle, rect);
			Rect portRect = new Rect ();
			OS.GetPortBounds (port, portRect);
			visibleRgn = getVisibleRegion (handle);
			if (paintRgn != 0) OS.SectRgn (paintRgn, visibleRgn, visibleRgn);
			OS.ClipCGContextToRegion (context, portRect, visibleRgn);
			int portHeight = portRect.bottom - portRect.top;
			OS.CGContextScaleCTM (context, 1, -1);
			OS.CGContextTranslateCTM (context, rect.left, -portHeight + rect.top);
		}
	}
	if (context == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		Display display = getDisplay ();
		data.device = display;
		data.foreground = foreground != null ? foreground : display.getSystemColor (SWT.COLOR_BLACK).handle;
		data.background = background != null ? background : display.getSystemColor (SWT.COLOR_WHITE).handle;
		data.font = font != null ? font : defaultFont ();
		data.visibleRgn = visibleRgn;
		data.control = handle;
	} else {
		if (visibleRgn != paintRgn) OS.DisposeRgn (visibleRgn);
	}
	return context;
}

public void internal_dispose_GC (int context, GCData data) {
	checkWidget ();
	if (data != null) {
		int paintContext = 0, paintRgn = 0;
		if (data.paintEvent != 0) {
			int theEvent = data.paintEvent;
			int [] buffer = new int [1];
			OS.GetEventParameter (theEvent, OS.kEventParamCGContextRef, OS.typeCGContextRef, null, 4, null, buffer);
			paintContext = buffer [0];	
			OS.GetEventParameter (theEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle, null, 4, null, buffer);
			paintRgn = buffer [0];
		}
		if (data.visibleRgn != 0 && data.visibleRgn != paintRgn) {
			OS.DisposeRgn (data.visibleRgn);
			data.visibleRgn = 0;
		}
		if (paintContext == context) return;
	}
	OS.CGContextFlush (context);
	OS.CGContextRelease (context);
}

public boolean isEnabled () {
	checkWidget();
	return OS.IsControlEnabled (topHandle ());
}

boolean isEnabledModal () {
	Display display = getDisplay ();
	//NOT DONE - fails for multiple APP MODAL shells
	Shell [] shells = display.getShells ();
	for (int i = 0; i < shells.length; i++) {
		Shell modal = shells [i];
		if (modal != this && modal.isVisible ()) {
			if ((modal.style & SWT.PRIMARY_MODAL) != 0) {
				Shell shell = getShell ();
				if (modal.parent == shell) {
					return false;
				}
			}
			int bits = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
			if ((modal.style & bits) != 0) {
				Control control = this;
				while (control != null) {
					if (control == modal) break;
					control = control.parent;
				}
				if (control != modal) return false;
			}
		}
	}
	return true;
}

boolean isFocusAncestor () {
	Display display = getDisplay ();
	Control control = display.getFocusControl ();
	while (control != null && control != this) {
		control = control.parent;
	}
	return control == this;
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
	return OS.IsControlVisible (topHandle ());
}

Decorations menuShell () {
	return parent.menuShell ();
}

int kEventControlContextualMenuClick (int nextHandler, int theEvent, int userData) {
	if (menu != null && !menu.isDisposed ()) {
		int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
		org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, pt);
		Rect rect = new Rect ();
		int window = OS.GetControlOwner (handle);
		OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
		menu.setLocation (pt.h + rect.left, pt.v + rect.top);
		menu.setVisible (true);
		return OS.noErr;
	}
	return OS.eventNotHandledErr;
}

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlDraw (nextHandler, theEvent, userData);
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
	if (theControl [0] != handle) return result;
	if (!hooks (SWT.Paint) && !filters (SWT.Paint)) return result;

	/* Retrieve the damage region */
	int [] region = new int [1];	
	OS.GetEventParameter (theEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle, null, 4, null, region);
	Rect bounds = new Rect ();
	OS.GetRegionBounds (region [0], bounds);
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	if (!OS.SectRect (rect, bounds, bounds)) return result;
	OS.OffsetRect (bounds, (short) -rect.left, (short) -rect.top);

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

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	if (!isEnabled () || !isEnabledModal ()) return OS.noErr;
	Cursor cursor = findCursor ();
	if (cursor != null) {
		setCursor (cursor.handle);
		return OS.noErr;
	}
	return OS.eventNotHandledErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	Display display = getDisplay ();
	if (!display.ignoreFocus) {
		short [] part = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
		sendFocusEvent (part [0] != 0);
	}
	return OS.eventNotHandledErr;
}	

int kEventControlTrack (int nextHandler, int theEvent, int userData) {
//	if (isEnabledModal ()) sendMouseEvent (SWT.MouseMove, theEvent);
	return OS.eventNotHandledErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	Shell shell = getShell ();
	if ((state & GRAB) != 0) {
		int [] clickCount = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamClickCount, OS.typeUInt32, null, 4, null, clickCount);
		sendMouseEvent (SWT.MouseDown, theEvent);
		if (clickCount [0] == 2) sendMouseEvent (SWT.MouseDoubleClick, theEvent);
		Display display = getDisplay ();
		display.grabControl = this;
	}
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/	
	if (!shell.isDisposed ()) {
		shell.setActiveControl (this);
	}
	return OS.eventNotHandledErr;
}

int kEventMouseDragged (int nextHandler, int theEvent, int userData) {
	if (isEnabledModal ()) sendMouseEvent (SWT.MouseMove, theEvent);
	return OS.eventNotHandledErr;
}

int kEventMouseMoved (int nextHandler, int theEvent, int userData) {
	if (isEnabledModal ()) sendMouseEvent (SWT.MouseMove, theEvent);
	return OS.eventNotHandledErr;
}

int kEventMouseUp (int nextHandler, int theEvent, int userData) {
	sendMouseEvent (SWT.MouseUp, theEvent);
	return OS.eventNotHandledErr;
}

int kEventRawKeyDown (int nextHandler, int theEvent, int userData) {
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	//NOT DONE
	if (keyCode [0] == 114) {
		//HELP KEY
	}
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
	boolean result = sendKeyEvent (type, theEvent);
	display.lastModifiers = modifiers [0];
	return result ? OS.eventNotHandledErr : OS.noErr;
}

int kEventRawKeyRepeat (int nextHandler, int theEvent, int userData) {
	if (!sendKeyEvent (SWT.KeyDown, theEvent)) return OS.noErr;
	return OS.eventNotHandledErr;
}

int kEventRawKeyUp (int nextHandler, int theEvent, int userData) {
	if (!sendKeyEvent (SWT.KeyUp, theEvent)) return OS.noErr;
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
	setZOrder (control, true);
}

public void moveBelow (Control control) {
	checkWidget();
	int inOther = 0;
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (parent != control.parent) return;
		inOther = control.topHandle ();
	}
	setZOrder (control, false);
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
	redrawWidget (handle);
}

public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget ();
	if (!OS.IsControlVisible (handle)) return;
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	x += rect.left;
	y += rect.top;
	OS.SetRect (rect, (short) x, (short) y, (short)(x + width), (short)(y + height));
	int window = OS.GetControlOwner (handle);
	OS.InvalWindowRect (window, rect);
}

void register () {
	super.register ();
	WidgetTable.put (handle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	handle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	if (menu != null && !menu.isDisposed ()) {
		menu.dispose ();
	}
	menu = null;
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
	
void sendFocusEvent (boolean focusIn) {
	Shell shell = getShell ();
	if (focusIn) {
		sendEvent (SWT.FocusIn);
	} else {
		sendEvent (SWT.FocusOut);
	}
	
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/
	if (focusIn) {
		if (!shell.isDisposed ()) {
			shell.setActiveControl (this);
		}
	} else {
		if (!shell.isDisposed ()) {
			Display display = shell.getDisplay ();
			Control control = display.getFocusControl ();
			if (control == null || shell != control.getShell () ) {
				shell.setActiveControl (null);
			}
		}
	}
}

boolean sendKeyEvent (int type, int theEvent) {
	Event event = new Event ();
	event.type = type;
	setKeyState (event, theEvent);
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
	int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, pt);
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
	event.x = pt.h - rect.left;
	event.y = pt.v - rect.top;
	OS.GetControlBounds (handle, rect);
	event.x -= rect.left;
	event.y -= rect.top;
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
	background = color != null ? color.handle : null;
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	setBounds (topHandle (), x, y, width, height, true, true, true);
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
	if (cursor != null && cursor.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	if (!OS.IsControlEnabled (topHandle ())) return;
	org.eclipse.swt.internal.carbon.Point where = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetGlobalMouse (where);
	int [] theWindow = new int [1];
	if (OS.FindWindow (where, theWindow) != OS.inContent) return;
	if (theWindow [0] == 0) return;
	Rect rect = new Rect ();
	OS.GetWindowBounds (theWindow [0], (short) OS.kWindowContentRgn, rect);
	CGPoint inPoint = new CGPoint ();
	inPoint.x = where.h - rect.left;
	inPoint.y = where.v - rect.top;
	int [] theRoot = new int [1];
	OS.GetRootControl (theWindow [0], theRoot);
	int [] theControl = new int [1];
	OS.HIViewGetSubviewHit (theRoot [0], inPoint, true, theControl);
	int cursorControl = theControl [0];
	while (theControl [0] != 0 && theControl [0] != handle) {
		OS.GetSuperControl (theControl [0], theControl);
	}
	if (theControl [0] == 0) return;
	org.eclipse.swt.internal.carbon.Point localPoint = new org.eclipse.swt.internal.carbon.Point ();
	localPoint.h = (short) inPoint.x;
	localPoint.v = (short) inPoint.y;
	int modifiers = OS.GetCurrentEventKeyModifiers ();
	boolean [] cursorWasSet = new boolean [1];
	OS.HandleControlSetCursor (cursorControl, localPoint, (short) modifiers, cursorWasSet);
	if (!cursorWasSet [0]) OS.SetThemeCursor (OS.kThemeArrowCursor);
}

void setCursor (int cursor) {
	switch (cursor) {
		case OS.kThemePointingHandCursor:
		case OS.kThemeArrowCursor:
		case OS.kThemeSpinningCursor:
		case OS.kThemeCrossCursor:
		case OS.kThemeWatchCursor:
		case OS.kThemeIBeamCursor:
		case OS.kThemeNotAllowedCursor:
		case OS.kThemeResizeLeftRightCursor:
		case OS.kThemeResizeLeftCursor:
		case OS.kThemeResizeRightCursor:
			OS.SetThemeCursor (cursor);
			break;
		default:
			OS.SetCursor (cursor);
	}
}

public void setEnabled (boolean enabled) {
	checkWidget();
	if (enabled) {
		if ((state & DISABLED) == 0) return;
		state &= ~DISABLED;
		OS.EnableControl (topHandle ());
	} else {
		if ((state & DISABLED) != 0) return;
		/*
		* Feature in the Macintosh.  If the receiver has focus,
		* disabling the receiver causes no cotnrol to have focus. 
		* The fix is to assign focus to the first ancestor control
		* that takes focus.  If no control will take focus, clear
		* the focus control.
		*/
		boolean fixFocus = isFocusAncestor ();
		state |= DISABLED;
		OS.DisableControl (topHandle ());
		if (fixFocus) fixFocus ();
	}
}

public boolean setFocus () {
	checkWidget();
	return forceFocus ();
}

public void setFont (Font font) {
	checkWidget();
	if (font != null) { 
		if (font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.font = font;
	setFontStyle (font);
}

void setFontStyle (Font font) {
	ControlFontStyleRec fontStyle = new ControlFontStyleRec ();
	if (font != null) {
		fontStyle.flags |= OS.kControlUseFontMask | OS.kControlUseSizeMask | OS.kControlUseFaceMask;
		fontStyle.font = font.id;
		fontStyle.style = font.style;
		fontStyle.size = font.size;
	} else {
		fontStyle.flags |= OS.kControlUseThemeFontIDMask;
		fontStyle.font = (short) defaultThemeFont ();
	}
	OS.SetControlFontStyle (handle, fontStyle);
}

public void setForeground (Color color) {
	checkWidget();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	foreground = color != null ? color.handle : null;
}

public void setLayoutData (Object layoutData) {
	checkWidget();
	this.layoutData = layoutData;
}

public void setLocation (int x, int y) {
	checkWidget();
	setBounds (topHandle (), x, y, 0, 0, true, false, true);
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
			redrawWidget (topHandle ());
		}
	} else {
		drawCount++;
	}
}

boolean setRadioSelection (boolean value){
	return false;
}

public void setSize (int width, int height) {
	checkWidget();
	setBounds (topHandle (), 0, 0, width, height, false, true, true);
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
	if (visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
	}
	
	/*
	* Feature in the Macintosh.  If the receiver has focus, hiding
	* the receiver causes no control to have focus.  Also, the focus
	* needs to be cleared from any TXNObject so that it stops blinking
	* the caret.  The fix is to assign focus to the first ancestor
	* control that takes focus.  If no control will take focus, clear
	* the focus control.
	*/
	boolean fixFocus = false;
	if (!visible) fixFocus = isFocusAncestor ();
	OS.HIViewSetVisible (topHandle (), visible);
	if (!visible) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the show
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Hide);
		if (isDisposed ()) return;
	}
	if (fixFocus) fixFocus ();
}

void setZOrder () {
	int topHandle = topHandle ();
	int parentHandle = parent.handle;
	OS.HIViewAddSubview (parentHandle, topHandle);
	//OS.EmbedControl (topHandle, parentHandle);
	/* Place the child at (0, 0) in the parent */
	Rect rect = new Rect ();
	OS.GetControlBounds (parentHandle, rect);
	rect.right = rect.left;
	rect.bottom = rect.top;
	OS.SetControlBounds (topHandle, rect);
}

void setZOrder (Control control, boolean above) {
	int inOp = above ?  OS.kHIViewZOrderBelow :  OS.kHIViewZOrderAbove;
	int inOther = control == null ? 0 : control.topHandle ();
	OS.HIViewSetZOrder (topHandle (), inOp, inOther);
}

void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

Point toControl (int x, int y) {
//	checkWidget();
	Rect rect = new Rect ();
	int window = OS.GetControlOwner (handle);
	OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
	x -= rect.left;
	y -= rect.top;
	OS.GetControlBounds (handle, rect);
    return new Point (x - rect.left, y - rect.top);
}

public Point toControl (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
    return toControl (point.x, point.y);
}

public Point toDisplay (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	int x = point.x + rect.left; 
	int y = point.y + rect.top; 
	int window = OS.GetControlOwner (handle);
	OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
    return new Point (x + rect.left, y + rect.top);
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
	if (!isVisible ()) return;
	Display display = getDisplay ();
	display.update ();
}

}
package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.RGBColor;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.PixMap;
import org.eclipse.swt.internal.carbon.BitMap;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public abstract class Widget {
	int style, state;
	EventTable eventTable;
	Object data;
	String [] keys;
	Object [] values;

	/* Global state flags */
//	static final int AUTOMATIC		= 1 << 0;
//	static final int ACTIVE			= 1 << 1;
	static final int GRAB		= 1 << 2;
//	static final int MULTIEXPOSE	= 1 << 3;
//	static final int RESIZEREDRAW	= 1 << 4;
//	static final int WRAP			= 1 << 5;
	static final int DISABLED	= 1 << 6;
	static final int HIDDEN		= 1 << 7;
//	static final int FOREGROUND		= 1 << 8;
//	static final int BACKGROUND		= 1 << 9;
	static final int DISPOSED	= 1 << 10;
//	static final int HANDLE		= 1 << 11;
	static final int CANVAS		= 1 << 12;
	static final int MOVED		= 1 << 13;
	static final int RESIZED	= 1 << 14;

	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	static final char Mnemonic = '&';
	
	static final Rect EMPTY_RECT = new Rect ();

Widget () {
	/* Do nothing */
}

public Widget (Widget parent, int style) {
	checkSubclass ();
	checkParent (parent);
	this.style = style;
}

int actionProc (int theControl, int partCode) {
	return OS.noErr;
}

public void addListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new EventTable ();
	eventTable.hook (eventType, handler);
}

public void addDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Dispose, typedListener);
}

static int checkBits (int style, int int0, int int1, int int2, int int3, int int4, int int5) {
	int mask = int0 | int1 | int2 | int3 | int4 | int5;
	if ((style & mask) == 0) style |= int0;
	if ((style & int0) != 0) style = (style & ~mask) | int0;
	if ((style & int1) != 0) style = (style & ~mask) | int1;
	if ((style & int2) != 0) style = (style & ~mask) | int2;
	if ((style & int3) != 0) style = (style & ~mask) | int3;
	if ((style & int4) != 0) style = (style & ~mask) | int4;
	if ((style & int5) != 0) style = (style & ~mask) | int5;
	return style;
}

void checkParent (Widget parent) {
	if (parent == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!parent.isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (parent.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

protected void checkWidget () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed ()) error (SWT.ERROR_WIDGET_DISPOSED);
}

int controlProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventControlActivate:				return kEventControlActivate (nextHandler, theEvent, userData);
		case OS.kEventControlBoundsChanged:			return kEventControlBoundsChanged (nextHandler, theEvent, userData);
		case OS.kEventControlClick:					return kEventControlClick (nextHandler, theEvent, userData);
		case OS.kEventControlContextualMenuClick:	return kEventControlContextualMenuClick (nextHandler, theEvent, userData);
		case OS.kEventControlDeactivate:			return kEventControlDeactivate (nextHandler, theEvent, userData);
		case OS.kEventControlDraw:					return kEventControlDraw (nextHandler, theEvent, userData);
		case OS.kEventControlHit:					return kEventControlHit (nextHandler, theEvent, userData);
		case OS.kEventControlSetCursor:				return kEventControlSetCursor (nextHandler, theEvent, userData);
		case OS.kEventControlSetFocusPart:			return kEventControlSetFocusPart (nextHandler, theEvent, userData);
		case OS.kEventControlTrack:					return kEventControlTrack (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

int createCIcon (Image image) {
	int imageHandle = image.handle;
	int width = OS.CGImageGetWidth(imageHandle);
	int height = OS.CGImageGetHeight(imageHandle);
	int bpr = OS.CGImageGetBytesPerRow(imageHandle);
	int bpp = OS.CGImageGetBitsPerPixel(imageHandle);
	int bpc = OS.CGImageGetBitsPerComponent(imageHandle);
	int alphaInfo = OS.CGImageGetAlphaInfo(imageHandle);
	
	int maskBpl = (((width + 7) / 8) + 3) / 4 * 4;
	int maskSize = height * maskBpl;
	int pixmapSize = height * bpr;
	
	/* Create the icon */
	int iconSize = PixMap.sizeof + BitMap.sizeof * 2 + 4 + maskSize;
	int iconHandle = OS.NewHandle(iconSize);
	if (iconHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.HLock(iconHandle);
	int[] iconPtr = new int[1];
	OS.memcpy(iconPtr, iconHandle, 4);

	/* Initialize the pixmap */
	PixMap iconPMap = new PixMap();
	iconPMap.rowBytes = (short)(bpr | 0x8000);
	iconPMap.right = (short)width;
	iconPMap.bottom = (short)height;
	iconPMap.cmpCount = 3;
	iconPMap.cmpSize = (short)bpc;
	iconPMap.pmTable = OS.NewHandle(0);
	iconPMap.hRes = 72 << 16;
	iconPMap.vRes = 72 << 16;
	iconPMap.pixelType = (short)OS.RGBDirect;
	iconPMap.pixelSize = (short)bpp;
	iconPMap.pixelFormat = (short)bpp;
	OS.memcpy(iconPtr[0], iconPMap, PixMap.sizeof);

	/* Initialize the mask */
	BitMap iconMask = new BitMap();
	iconMask.rowBytes = (short)maskBpl;
	iconMask.right = (short)width;
	iconMask.bottom = (short)height;
	OS.memcpy(iconPtr[0] + PixMap.sizeof, iconMask, BitMap.sizeof);

	/* Initialize the icon data */
	int iconData = OS.NewHandle(pixmapSize);
	OS.HLock(iconData);
	int[] iconDataPtr = new int[1];
	OS.memcpy(iconDataPtr, iconData, 4);
	OS.memcpy(iconDataPtr[0], image.data, pixmapSize);
	OS.HUnlock(iconData);
	OS.memcpy(iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof, new int[]{iconData}, 4);

	/* Initialize the mask data */
	if (alphaInfo != OS.kCGImageAlphaFirst) {
		OS.memset(iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof + 4, -1, maskSize);
	} else {
		byte[] srcData = new byte[pixmapSize];
		OS.memcpy(srcData, image.data, pixmapSize);
		byte[] maskData = new byte[maskSize];
		int offset = 0, maskOffset = 0;
		for (int y = 0; y<height; y++) {
			for (int x = 0; x<width; x++) {
				if ((srcData[offset] & 0xFF) > 128) {
					maskData[maskOffset + (x >> 3)] |= (1 << (7 - (x & 0x7)));
				} else {
					maskData[maskOffset + (x >> 3)] &= ~(1 << (7 - (x & 0x7)));
				}
				offset += 4;
			}
			maskOffset += maskBpl;
		}
		OS.memcpy(iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof + 4, maskData, maskData.length);
	}
	
	OS.HUnlock(iconHandle);	
	return iconHandle;
}

void createHandle () {
}

void createWidget () {
	createHandle ();
	register ();
	hookEvents ();
}

int commandProc (int nextHandler, int theEvent, int userData) {
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventProcessCommand:	return kEventProcessCommand (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}
	
void deregister () {
}

void destroyWidget () {
	releaseHandle ();
}

void destroyCIcon (int iconHandle) {
	OS.HLock(iconHandle);
	
	/* Dispose the ColorTable */
	int[] iconPtr = new int[1];
	OS.memcpy(iconPtr, iconHandle, 4);	
	PixMap iconPMap = new PixMap();
	OS.memcpy(iconPMap, iconPtr[0], PixMap.sizeof);
	if (iconPMap.pmTable != 0) OS.DisposeHandle(iconPMap.pmTable);

	/* Dispose the icon data */
	int[] iconData = new int[1];
	OS.memcpy(iconData, iconPtr[0] + PixMap.sizeof + 2 * BitMap.sizeof, 4);
	if (iconData[0] != 0) OS.DisposeHandle(iconData[0]);
	
	OS.HUnlock(iconHandle);
	
	/* Dispose the icon */
	OS.DisposeHandle(iconHandle);
}

int drawItemProc (int browser, int item, int property, int itemState, int theRect, int gdDepth, int colorDevice) {
	return OS.noErr;
}

public void dispose () {
	/*
	* Note:  It is valid to attempt to dispose a widget
	* more than once.  If this happens, fail silently.
	*/
	if (isDisposed()) return;
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	releaseChild ();
	releaseWidget ();
	destroyWidget ();
}

void drawBackground (int control, float [] background) {
	Rect rect = new Rect ();
	OS.GetControlBounds (control, rect);
	if (background != null) {
		int red = (short) (background [0] * 255);
		int green = (short) (background [1] * 255);
		int blue = (short) (background [2] * 255);
		RGBColor color = new RGBColor ();
		color.red = (short) (red << 8 | red);
		color.green = (short) (green << 8 | green);
		color.blue = (short) (blue << 8 | blue);
		OS.RGBForeColor (color);
		OS.PaintRect (rect);
	} else {
		OS.EraseRect (rect);
	}
}

void drawWidget (int control) {
}

void error (int code) {
	SWT.error(code);
}

boolean filters (int eventType) {
	Display display = getDisplay ();
	return display.filters (eventType);
}

Rect getControlBounds (int control) {
	Rect rect = new Rect();
	OS.GetControlBounds (control, rect);
	int window = OS.GetControlOwner (control);
	int [] theRoot = new int [1];
	OS.GetRootControl (window, theRoot);
	int [] parentHandle = new int [1];
	OS.GetSuperControl (control, parentHandle);
	if (parentHandle [0] != theRoot [0]) {
		Rect parentRect = new Rect ();
		OS.GetControlBounds (parentHandle [0], parentRect);
		OS.OffsetRect (rect, (short) -parentRect.left, (short) -parentRect.top);
	}
	Rect inset = getInset ();
	rect.left -= inset.left;
	rect.top -= inset.top;
	rect.right += inset.right;
	rect.bottom += inset.bottom;
	return rect;
}

Rect getControlSize (int control) {
	Rect rect = new Rect ();
	OS.GetControlBounds (control, rect);
	Rect inset = getInset ();
	rect.left -= inset.left;
	rect.top -= inset.top;
	rect.right += inset.right;
	rect.bottom += inset.bottom;
	return rect;
}

public Object getData () {
	checkWidget();
	return data;
}

public Object getData (String key) {
	checkWidget();
	if (key == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (keys == null) return null;
	for (int i=0; i<keys.length; i++) {
		if (keys [i].equals (key)) return values [i];
	}
	return null;
}

public abstract Display getDisplay ();

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf(".");
	if (index == -1) return string;
	return string.substring(index + 1, string.length ());
}

String getNameText () {
	return "";
}

public int getStyle () {
	checkWidget();
	return style;
}

int getVisibleRegion (int control) {
	int visibleRgn = OS.NewRgn ();
	if (getDrawCount () > 0) return visibleRgn;
	int childRgn = OS.NewRgn (), tempRgn = OS.NewRgn ();
	int window = OS.GetControlOwner (control);
	int port = OS.GetWindowPort (window);
	OS.GetPortVisibleRegion (port, visibleRgn);
	short [] count = new short [1];
	int [] outControl = new int [1];
	int tempControl = control, lastControl = 0;
	while (tempControl != 0) {
		OS.GetControlRegion (tempControl, (short) OS.kControlStructureMetaPart, tempRgn);
		OS.SectRgn (tempRgn, visibleRgn, visibleRgn);
		if (OS.EmptyRgn (visibleRgn)) break;
		OS.CountSubControls (tempControl, count);
		for (int i = 0; i < count [0]; i++) {
			OS.GetIndexedSubControl (tempControl, (short)(i + 1), outControl);
			int child = outControl [0];
			if (child == lastControl) break;
			if (!OS.IsControlVisible (child)) continue;
			OS.GetControlRegion (child, (short) OS.kControlStructureMetaPart, tempRgn);
			OS.UnionRgn (tempRgn, childRgn, childRgn);
		}
		lastControl = tempControl;
		OS.GetSuperControl (tempControl, outControl);
		tempControl = outControl [0];
	}
	OS.DiffRgn (visibleRgn, childRgn, visibleRgn);
	OS.DisposeRgn (childRgn);
	OS.DisposeRgn (tempRgn);
	return visibleRgn;
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
	return OS.eventNotHandledErr;
}

int hitTestProc (int browser, int item, int property, int theRect, int mouseRect) {
	/* Return true to indicate that the item can be selected */
	return 1;
}

void hookEvents () {
}

boolean hooks (int eventType) {
	if (eventTable == null) return false;
	return eventTable.hooks (eventType);
}

int getDrawCount () {
	return 0;
}

Rect getInset () {
	return EMPTY_RECT;
}

public boolean isDisposed () {
	return (state & DISPOSED) != 0;
}

protected boolean isListening (int eventType) {
	checkWidget();
	return hooks (eventType);
}

boolean isTrimHandle (int trimHandle) {
	return false;
}

boolean isValidSubclass () {
	return Display.isValidClass (getClass ());
}

boolean isValidThread () {
	return getDisplay ().isValidThread ();
}

int itemDataProc (int browser, int item, int property, int itemData, int setValue) {
	return OS.noErr;
}

int itemNotificationProc (int browser, int item, int message) {
	return OS.noErr;
}

int kEventProcessCommand (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}
	
int kEventControlActivate (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlBoundsChanged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlClick (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlContextualMenuClick (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlDeactivate (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	int [] theControl = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamDirectObject, OS.typeControlRef, null, 4, null, theControl);
	int [] region = new int [1];	
	OS.GetEventParameter (theEvent, OS.kEventParamRgnHandle, OS.typeQDRgnHandle, null, 4, null, region);
	int visibleRgn = getVisibleRegion (theControl [0]);
	int oldClip = OS.NewRgn ();
	OS.GetClip (oldClip);
	OS.SectRgn(region [0], visibleRgn, visibleRgn);
	OS.SetClip (visibleRgn);
	drawWidget (theControl [0]);
	int result = OS.CallNextEventHandler (nextHandler, theEvent);
	OS.SetClip (oldClip);
	OS.DisposeRgn (visibleRgn);
	OS.DisposeRgn (oldClip);
	return result;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventControlTrack (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuClosed (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuOpening (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMenuTargetItem (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseDragged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseMoved (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseUp (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventMouseWheelMoved (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyUp (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyRepeat (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyModifiersChanged (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventRawKeyDown (int nextHandler, int theEvent, int userData) {
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

int kEventWindowHidden (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
}

int kEventWindowShown (int nextHandler, int theEvent, int userData) {
	return OS.eventNotHandledErr;
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

int menuProc (int nextHandler, int theEvent, int userData) {	
	int eventKind = OS.GetEventKind (theEvent);
	switch (eventKind) {
		case OS.kEventMenuClosed:		return kEventMenuClosed (nextHandler, theEvent, userData);
		case OS.kEventMenuOpening:		return kEventMenuOpening (nextHandler, theEvent, userData);
		case OS.kEventMenuTargetItem:	return kEventMenuTargetItem (nextHandler, theEvent, userData);
	}
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

public void notifyListeners (int eventType, Event event) {
	checkWidget();
	if (event == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	event.type = eventType;
	event.widget = this;
	eventTable.sendEvent (event);
}

void postEvent (int eventType) {
	sendEvent (eventType, null, false);
}

void postEvent (int eventType, Event event) {
	sendEvent (eventType, event, false);
}

void redrawWidget (int control) {
	if (!OS.IsControlVisible (control)) return;
	Rect rect = new Rect ();
	OS.GetControlBounds (control, rect);
	int window = OS.GetControlOwner (control);
	OS.InvalWindowRect (window, rect);
}

void register () {
}

void releaseChild () {
	/* Do nothing */
}

void releaseHandle () {
	state |= DISPOSED;
}

void releaseResources () {
	releaseWidget ();
	releaseHandle ();
}

void releaseWidget () {
	sendEvent (SWT.Dispose);
	deregister ();
	eventTable = null;
	data = null;
	keys = null;
	values = null;
}

public void removeListener (int eventType, Listener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

protected void removeListener (int eventType, SWTEventListener handler) {
	checkWidget();
	if (handler == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventType, handler);
}

public void removeDisposeListener (DisposeListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Dispose, listener);
}

void sendEvent (Event event) {
	Display display = event.display;
	if (!display.filterEvent (event)) {
		if (eventTable != null) eventTable.sendEvent (event);
	}
}

void sendEvent (int eventType) {
	sendEvent (eventType, null, true);
}

void sendEvent (int eventType, Event event) {
	sendEvent (eventType, event, true);
}

void sendEvent (int eventType, Event event, boolean send) {
	Display display = getDisplay ();
	if (eventTable == null && !display.filters (eventType)) {
		return;
	}
	if (event == null) event = new Event ();
	event.type = eventType;
	event.display = display;
	event.widget = this;
	if (event.time == 0) {
		event.time = display.getLastEventTime ();
	}
	if (send) {
		sendEvent (event);
	} else {
		display.postEvent (event);
	}
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	Rect inset = getInset ();
	Rect oldBounds = new Rect ();
	OS.GetControlBounds (control, oldBounds);
	oldBounds.left -= inset.left;
	oldBounds.top -= inset.top;
	oldBounds.right += inset.right;
	oldBounds.bottom += inset.bottom;
	boolean visible = OS.IsControlVisible (control);
	int window = OS.GetControlOwner (control);
	if (visible) OS.InvalWindowRect (window, oldBounds);
	x += inset.left;
	y += inset.top;
	width -= (inset.left + inset.right);
	height -= (inset.top + inset.bottom);
	if (move) {
		int [] theRoot = new int [1];
		OS.GetRootControl (window, theRoot);
		int [] parentHandle = new int [1];
		OS.GetSuperControl (control, parentHandle);
		if (parentHandle [0] != theRoot [0]) {
			Rect rect = new Rect ();
			OS.GetControlBounds (parentHandle [0], rect);
			x += rect.left;
			y += rect.top;
		}
	} else {
		x = oldBounds.left;
		y = oldBounds.top;
	}
	if (!resize) {
		width = oldBounds.right - oldBounds.left;
		height = oldBounds.bottom - oldBounds.top;
	}
	width = Math.max (0, width);
	height = Math.max (0, height);
	boolean sameOrigin = x == oldBounds.left && y == oldBounds.top;
	boolean sameExtent = width == (oldBounds.right - oldBounds.left) && height == (oldBounds.bottom - oldBounds.top);
	Rect newBounds = new Rect ();
	newBounds.left = (short) x;
	newBounds.top = (short) y;
	newBounds.right = (short) (x + width);
	newBounds.bottom = (short) (y + height);
	OS.SetControlBounds (control, newBounds);
	if (visible) OS.InvalWindowRect (window, newBounds);
	int result = 0;
	if (move && !sameOrigin) {
		if (events) sendEvent (SWT.Move);
		result |= MOVED;
	}
	if (resize && !sameExtent) {
		if (events) sendEvent (SWT.Move);
		result |= RESIZED;
	}
	return result;
}

public void setData (Object data) {
	checkWidget();
	this.data = data;
}

public void setData (String key, Object value) {
	checkWidget();
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

void setInputState (Event event, int theEvent) {
	short [] button = new short [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseButton, OS.typeMouseButton, null, 2, null, button);
	int [] chord = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamMouseChord, OS.typeUInt32, null, 4, null, chord);
	int [] modifiers = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyModifiers, OS.typeUInt32, null, 4, null, modifiers);
	setInputState (event, button [0], chord [0], modifiers [0]);
}

void setInputState (Event event, short button, int chord, int modifiers) {
	switch (button) {
		case 1: event.button = 1; break;
		case 2: event.button = 3; break;
		case 3: event.button = 2; break;
	}
	if ((chord & 0x01) != 0) event.stateMask |= SWT.BUTTON1;
	if ((chord & 0x02) != 0) event.stateMask |= SWT.BUTTON3;
	if ((chord & 0x04) != 0) event.stateMask |= SWT.BUTTON2;
	if ((modifiers & OS.optionKey) != 0) event.stateMask |= SWT.ALT;
	if ((modifiers & OS.shiftKey) != 0) event.stateMask |= SWT.SHIFT;
	if ((modifiers & OS.controlKey) != 0) event.stateMask |= SWT.CONTROL;
	if ((modifiers & OS.cmdKey) != 0) event.stateMask |= SWT.COMMAND;
	switch (event.type) {
		case SWT.MouseDown:
		case SWT.MouseDoubleClick:
			if (event.button == 1) event.stateMask &= ~SWT.BUTTON1;
			if (event.button == 2) event.stateMask &= ~SWT.BUTTON2;
			if (event.button == 3)  event.stateMask &= ~SWT.BUTTON3;
			break;
		case SWT.MouseUp:
			if (event.button == 1) event.stateMask |= SWT.BUTTON1;
			if (event.button == 2) event.stateMask |= SWT.BUTTON2;
			if (event.button == 3) event.stateMask |= SWT.BUTTON3;
			break;
		case SWT.KeyDown:
		case SWT.Traverse: {
			if (event.keyCode != 0 || event.character != 0) return;
			Display display = getDisplay ();
			int lastModifiers = display.lastModifiers;
			if ((modifiers & OS.shiftKey) != 0 && (lastModifiers & OS.shiftKey) == 0) {
				event.stateMask &= ~SWT.SHIFT;
				event.keyCode = SWT.SHIFT;
				return;
			}
			if ((modifiers & OS.controlKey) != 0 && (lastModifiers & OS.controlKey) == 0) {
				event.stateMask &= ~SWT.CONTROL;
				event.keyCode = SWT.CONTROL;
				return;
			}
			if ((modifiers & OS.cmdKey) != 0 && (lastModifiers & OS.cmdKey) == 0) {
				event.stateMask &= ~SWT.COMMAND;
				event.keyCode = SWT.COMMAND;
				return;
			}	
			if ((modifiers & OS.optionKey) != 0 && (lastModifiers & OS.optionKey) == 0) {
				event.stateMask &= ~SWT.ALT;
				event.keyCode = SWT.ALT;
				return;
			}
			break;
		}
		case SWT.KeyUp: {
			if (event.keyCode != 0 || event.character != 0) return;
			Display display = getDisplay ();
			int lastModifiers = display.lastModifiers;
			if ((modifiers & OS.shiftKey) == 0 && (lastModifiers & OS.shiftKey) != 0) {
				event.stateMask |= SWT.SHIFT;
				event.keyCode = SWT.SHIFT;
				return;
			}
			if ((modifiers & OS.controlKey) == 0 && (lastModifiers & OS.controlKey) != 0) {
				event.stateMask |= SWT.CONTROL;
				event.keyCode = SWT.CONTROL;
				return;
			}
			if ((modifiers & OS.cmdKey) == 0 && (lastModifiers & OS.cmdKey) != 0) {
				event.stateMask |= SWT.COMMAND;
				event.keyCode = SWT.COMMAND;
				return;
			}	
			if ((modifiers & OS.optionKey) != 0 && (lastModifiers & OS.optionKey) == 0) {
				event.stateMask |= SWT.ALT;
				event.keyCode = SWT.ALT;
				return;
			}
			break;
		}
	}
}

void setKeyState (Event event, int theEvent) {
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	event.keyCode = Display.translateKey (keyCode [0]);
	switch (event.keyCode) {
		case 0:
		case SWT.BS:
		case SWT.CR:
		case SWT.DEL:
		case SWT.ESC:
		case SWT.TAB: {
			byte [] charCode = new byte [1];
			OS.GetEventParameter (theEvent, OS.kEventParamKeyMacCharCodes, OS.typeChar, null, charCode.length, null, charCode);
			event.character = (char) charCode [0];
			break;
		}
		case SWT.LF:
			event.character = '\n';
			break;
	}
	setInputState (event, theEvent);
}

public String toString () {
	String string = "*Disposed*";
	if (!isDisposed ()) {
		string = "*Wrong Thread*";
		if (isValidThread ()) string = getNameText ();
	}
	return getName () + " {" + string + "}";
}

int trackingProc (int browser, int itemID, int property, int theRect, int startPt, int modifiers) {
	/* Return one to indicate that the data browser should process the click */
	return 1;
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
		case OS.kEventWindowHidden:			return kEventWindowHidden (nextHandler, theEvent, userData);
		case OS.kEventWindowShown:				return kEventWindowShown (nextHandler, theEvent, userData);
	}
	return OS.eventNotHandledErr;
}

}

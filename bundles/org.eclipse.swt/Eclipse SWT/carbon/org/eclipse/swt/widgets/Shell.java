package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class Shell extends Decorations {
	Display display;
	int shellHandle;

public Shell () {
	this ((Display) null);
}

public Shell (int style) {
	this ((Display) null, style);
}

public Shell (Display display) {
	this (display, SWT.SHELL_TRIM);
}

public Shell (Display display, int style) {
	this (display, null, style, 0);
}

Shell (Display display, Shell parent, int style, int handle) {
	super ();
	checkSubclass ();
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = checkStyle (style);
	this.parent = parent;
	this.display = display;
	this.handle = handle;
	createWidget ();
}

public Shell (Shell parent) {
	this (parent, SWT.DIALOG_TRIM);
}

public Shell (Shell parent, int style) {
	this (parent != null ? parent.getDisplay () : null, parent, style, 0);
}

static int checkStyle (int style) {
	style = Decorations.checkStyle (style);
	int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
	int bits = style & ~mask;
	if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
	if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
	return bits;
}

public void addShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Activate,typedListener);
	addListener(SWT.Close,typedListener);
	addListener(SWT.Deactivate,typedListener);
	addListener(SWT.Iconify,typedListener);
	addListener(SWT.Deiconify,typedListener);
}

public void close () {
	checkWidget();
	closeWidget ();
}

void closeWidget () {
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	Rectangle trim = super.computeTrim (x, y, width, height);
	Rect rect = new Rect ();
	OS.GetWindowStructureWidths (shellHandle, rect);
	trim.x -= rect.left;
	trim.y -= rect.top;
	trim.width += rect.left + rect.right;
	trim.height += rect.top + rect.bottom;
	return trim;
}

void createWidget () {
	super.createWidget ();
	if ((style & SWT.H_SCROLL) != 0) horizontalBar = createScrollBar (SWT.H_SCROLL);
	if ((style & SWT.V_SCROLL) != 0) verticalBar = createScrollBar (SWT.V_SCROLL);
	layoutControl ();
}

void createHandle () {
	state |= CANVAS | GRAB;
	int attributes = OS.kWindowCompositingAttribute | OS.kWindowStandardHandlerAttribute;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.CLOSE) != 0) attributes |= OS.kWindowCloseBoxAttribute;
		if ((style & SWT.MIN) != 0) attributes |= OS.kWindowCollapseBoxAttribute;
		if ((style & SWT.MAX) != 0) attributes |= OS.kWindowFullZoomAttribute;
		if ((style & SWT.RESIZE) != 0) {
			attributes |= OS.kWindowResizableAttribute | OS.kWindowLiveResizeAttribute;
		}
	}
	int windowActivationScope= -1;
	int windowClass= 0;
	int themeBrush= OS.kThemeBrushDialogBackgroundActive;
	if ((style & SWT.ON_TOP) == 0) {
		if ((style & SWT.NO_TRIM) != 0) {
			windowClass= OS.kSheetWindowClass;
			windowActivationScope= OS.kWindowActivationScopeNone;
		} else {
			windowClass= OS.kDocumentWindowClass;
		}
	} else {
		if (style == SWT.NONE) {
			windowClass= OS.kSheetWindowClass;
			windowActivationScope= OS.kWindowActivationScopeNone;
		} else if ((style & SWT.NO_TRIM) != 0 && (style & SWT.ON_TOP) != 0) {
			windowClass= OS.kSheetWindowClass;
			windowActivationScope= OS.kWindowActivationScopeNone;
		} else if ((style & SWT.NO_TRIM) != 0) {
			windowClass= OS.kSheetWindowClass;
			windowActivationScope= OS.kWindowActivationScopeNone;
		} else if ((style & SWT.APPLICATION_MODAL) != 0) {
			windowClass= OS.kMovableModalWindowClass;
		} else if ((style & SWT.SYSTEM_MODAL) != 0) {
			windowClass= OS.kModalWindowClass;
		} else if ((style & SWT.ON_TOP) != 0) {
			windowClass= OS.kSheetWindowClass;
			windowActivationScope= OS.kWindowActivationScopeNone;
			attributes= 0;
		} else {
			windowClass= OS.kDocumentWindowClass;
		}
	}
	int [] outWindow = new int[1];
	Rect rect = new Rect ();
	OS.SetRect (rect, (short)100, (short)100, (short)400, (short)400);

//	int kWindowStandardDocumentAttributes = OS.kWindowCloseBoxAttribute | OS.kWindowFullZoomAttribute | OS.kWindowCollapseBoxAttribute | OS.kWindowResizableAttribute;
//	int windowAttrs = kWindowStandardDocumentAttributes | OS.kWindowStandardHandlerAttribute;
//	OS.CreateNewWindow (OS.kDocumentWindowClass, attributes, rect, outWindow);
	OS.CreateNewWindow (windowClass, attributes, rect, outWindow);

	if (outWindow [0] == 0) error (SWT.ERROR_NO_HANDLES);
	shellHandle = outWindow [0];

	int inputMode = OS.kWindowModalityNone;
	if ((style & SWT.PRIMARY_MODAL) != 0) inputMode = OS.kWindowModalityWindowModal;
	if ((style & SWT.APPLICATION_MODAL) != 0) inputMode = OS.kWindowModalityAppModal;
	if ((style & SWT.SYSTEM_MODAL) != 0) inputMode = OS.kWindowModalitySystemModal;
	if (inputMode != OS.kWindowModalityNone) {
		int parentHandle = 0;
		if (parent != null) parentHandle = parent.getShell ().shellHandle;
		OS.SetWindowModality (shellHandle, inputMode, parentHandle);
	}
	
	int [] theRoot = new int [1];
	OS.HIViewFindByID (OS.HIViewGetRoot (shellHandle), OS.kHIViewWindowContentID (), theRoot);
	if (theRoot [0] == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		scrolledHandle = theRoot [0];
		createHandle (scrolledHandle);
	} else {
		handle = theRoot [0];
	}
}

public void dispose () {
	if (isDisposed()) return;
	super.dispose ();
}

void destroyWidget () {
	int theWindow = shellHandle;
	OS.HideWindow (shellHandle);
	releaseHandle ();
	if (theWindow != 0) OS.DisposeWindow (theWindow);
}

public void forceActive () {
	checkWidget ();
	OS.SelectWindow (shellHandle);
}

public Rectangle getClientArea () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowContentRgn, rect);
	return new Rectangle (0, 0, rect.right - rect.left, rect.bottom - rect.top);
}

public Rectangle getBounds () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Rectangle (rect.top, rect.left, rect.right - rect.left, rect.bottom - rect.top);
}

public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

public int getImeInputMode () {
	checkWidget();
	return SWT.NONE;
}

public Point getLocation () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Point (rect.top, rect.left);
}

public Shell getShell () {
	checkWidget();
	return this;
}

public Shell [] getShells () {
	checkWidget();
	int count = 0;
	Shell [] shells = display.getShells ();
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.parent;
		} while (shell != null && shell != this);
		if (shell == this) count++;
	}
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.parent;
		} while (shell != null && shell != this);
		if (shell == this) {
			result [index++] = shells [i];
		}
	}
	return result;
}

public Point getSize () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Point (rect.right - rect.left, rect.bottom - rect.top);
}

public boolean getVisible () {
	checkWidget();
    return OS.IsWindowVisible (shellHandle);
}

int kEventWindowActivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowActivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	//NOT DONE
	Display display = getDisplay ();
	display.setMenuBar (menuBar);
	sendEvent (SWT.Activate);
	return OS.eventNotHandledErr;
}

int kEventWindowBoundsChanged (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowBoundsChanged (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] attributes = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAttributes, OS.typeUInt32, null, attributes.length * 4, null, attributes);
	if ((attributes [0] & OS.kWindowBoundsChangeOriginChanged) != 0) {
		sendEvent (SWT.Move);
	}
	if ((attributes [0] & OS.kWindowBoundsChangeSizeChanged) != 0) {
		layoutControl ();
		sendEvent (SWT.Resize);
		if (layout != null) layout.layout (this, false);
	}
	return OS.eventNotHandledErr;
}

int kEventWindowCollapsed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowCollapsed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendEvent (SWT.Iconify);
	return OS.eventNotHandledErr;
}

int kEventWindowDeactivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowDeactivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendEvent (SWT.Deactivate);
	return OS.eventNotHandledErr;
}

int kEventWindowExpanded (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowExpanded (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendEvent (SWT.Deiconify);
	return OS.eventNotHandledErr;
}

int kEventWindowClose (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowClose (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	closeWidget ();
	return OS.noErr;
}

void hookEvents () {
	super.hookEvents ();
	int[] mask= new int [] {
		OS.kEventClassMouse, OS.kEventMouseDown,
		OS.kEventClassWindow, OS.kEventWindowActivated,
		OS.kEventClassWindow, OS.kEventWindowBoundsChanged,
		OS.kEventClassWindow, OS.kEventWindowClose,
		OS.kEventClassWindow, OS.kEventWindowCollapsed,
		OS.kEventClassWindow, OS.kEventWindowDeactivated,
		OS.kEventClassWindow, OS.kEventWindowExpanded,
	};
	int windowTarget = OS.GetWindowEventTarget (shellHandle);
	OS.InstallEventHandler (windowTarget, display.windowProc, mask.length / 2, mask, shellHandle, null);
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}

public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

public void open () {
	checkWidget();
	OS.ShowWindow (shellHandle);
	OS.BringToFront (shellHandle);
//	OS.SetKeyboardFocus (shellHandle, handle, (short)-1 /*???OS.kControlFocusNoPart*/)));
}

public void removeShellListener(ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Activate, listener);
	eventTable.unhook(SWT.Close, listener);
	eventTable.unhook(SWT.Deactivate, listener);
	eventTable.unhook(SWT.Iconify,listener);
	eventTable.unhook(SWT.Deiconify,listener);
}

public void setActive () {
	checkWidget ();
	OS.SelectWindow (shellHandle);
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget ();
	Rect rect = new Rect ();
	OS.SetRect (rect, (short) x, (short) y, (short) (x + width), (short) (y + height));
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
}

public void setImeInputMode (int mode) {
	checkWidget();
}

public void setLocation (int x, int y) {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	OS.SetRect (rect, (short) x, (short) y, rect.right, rect.bottom);
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
}

public void setMaximized (boolean maximized) {
	checkWidget();
	super.setMaximized (maximized);
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	short inPartCode = (short) (maximized ? OS.inZoomOut : OS.inZoomIn);
	//FIXME - returns -50 errParam
	OS.ZoomWindowIdeal (shellHandle, inPartCode, pt);
}

public void setMinimized (boolean minimized) {
	checkWidget();
	super.setMinimized (minimized);
	OS.CollapseWindow (shellHandle, true);
}

public void setSize (int width, int height) {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	OS.SetRect (rect, rect.left, rect.top, (short)(rect.left + width), (short)(rect.top + height));
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	char [] buffer = new char [string.length ()];
	string.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetWindowTitleWithCFString (shellHandle, ptr);
	OS.CFRelease (ptr);
}

public void setVisible (boolean visible) {
	checkWidget();
	if (visible) {
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		OS.ShowWindow (shellHandle);
	} else {
    	OS.HideWindow(shellHandle);
		sendEvent (SWT.Hide);
	}
}
}

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

void createHandle () {
	state |= CANVAS;
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
	Rect contentBounds = new Rect ();
	OS.SetRect (contentBounds, (short)100, (short)100, (short)200, (short)200);
	OS.CreateNewWindow (windowClass, attributes, contentBounds, outWindow);
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
	handle = theRoot [0];
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

public int getBorderWidth () {
	checkWidget();
    return 0;
}

public Rectangle getBounds () {
	checkWidget();
	return new Rectangle (0, 0, 0, 0);
}

public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

public boolean getEnabled () {
	checkWidget();
	return false;
}

public Point getLocation () {
	checkWidget();
	return new Point(0, 0);
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
	return new Point (0, 0);
}

public boolean getVisible () {
	checkWidget();
    return false;
}

int kEventWindowActivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowActivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
//	OS.SetRootMenu (outMenuRef [0]);
	sendEvent (SWT.Activate);
	return OS.eventNotHandledErr;
}

int kEventWindowDeactivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowDeactivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendEvent (SWT.Deactivate);
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
	Display display = getDisplay ();
	int[] mask= new int[] {
		OS.kEventClassWindow, OS.kEventWindowActivated,
		OS.kEventClassWindow, OS.kEventWindowDeactivated,
		OS.kEventClassWindow, OS.kEventWindowBoundsChanged,
		OS.kEventClassWindow, OS.kEventWindowClose,
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
	checkWidget();
}

public void setLocation (int x, int y) {
	checkWidget();
}

public void setMinimized (boolean minimized) {
	checkWidget();
}

public void setSize (int width, int height) {
	checkWidget();
}

public void setText (String string) {
	checkWidget();
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

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
	int shellHandle, windowGroup;
	boolean resized;
	Control lastActive;

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

void bringToTop () {
	OS.SelectWindow (shellHandle);
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

void createHandle () {
	state |= CANVAS | GRAB | HIDDEN;
	int attributes = OS.kWindowStandardHandlerAttribute; // | OS.kWindowCompositingAttribute;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.CLOSE) != 0) attributes |= OS.kWindowCloseBoxAttribute;
		if ((style & SWT.MIN) != 0) attributes |= OS.kWindowCollapseBoxAttribute;
		if ((style & SWT.MAX) != 0) attributes |= OS.kWindowFullZoomAttribute;
		if ((style & SWT.RESIZE) != 0) {
			attributes |= OS.kWindowResizableAttribute; // | OS.kWindowLiveResizeAttribute;
		}
	}
	int windowClass = OS.kDocumentWindowClass;
	if ((style & (SWT.CLOSE | SWT.TITLE)) == 0) windowClass = OS.kSheetWindowClass;
//	int windowClass = parent == null ? OS.kDocumentWindowClass : OS.kSheetWindowClass;
//	if ((style & SWT.APPLICATION_MODAL) != 0) windowClass = OS.kMovableModalWindowClass;
//	if ((style & SWT.SYSTEM_MODAL) != 0) windowClass = OS.kModalWindowClass;
	Rect rect = new Rect ();
	OS.GetAvailableWindowPositioningBounds (OS.GetMainDevice (), rect);
	int width = (rect.right - rect.left) * 5 / 8;
	int height = (rect.bottom - rect.top) * 5 / 8;
	OS.SetRect (rect, (short) 0, (short) 0, (short) width, (short) height);
	int [] outWindow = new int [1];
	attributes &= OS.GetAvailableWindowAttributes (windowClass);
	OS.CreateNewWindow (windowClass, attributes, rect, outWindow);
	if (outWindow [0] == 0) error (SWT.ERROR_NO_HANDLES);
	shellHandle = outWindow [0];
	if ((style & SWT.ON_TOP) != 0) {
		OS.SetWindowActivationScope (shellHandle, OS.kWindowActivationScopeNone);
	}
	OS.RepositionWindow (shellHandle, 0, OS.kWindowCascadeOnMainScreen);
//	OS.SetThemeWindowBackground (shellHandle, (short) OS.kThemeBrushDialogBackgroundActive, false);
	int [] theRoot = new int [1];
	OS.CreateRootControl (shellHandle, theRoot);
	OS.GetRootControl (shellHandle, theRoot);
	if (theRoot [0] == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		createScrolledHandle (theRoot [0]);
	} else {
		createHandle (theRoot [0]);
	}
	OS.SetControlVisibility (topHandle (), false, false);
	int [] outGroup = new int [1];
	OS.CreateWindowGroup (OS.kWindowGroupAttrHideOnCollapse, outGroup);
	if (outGroup [0] == 0) error (SWT.ERROR_NO_HANDLES);
	windowGroup = outGroup [0];
	if (parent != null) {
		Shell shell = parent.getShell ();
		int parentGroup = shell.windowGroup;
		OS.SetWindowGroup (shellHandle, parentGroup);
		OS.SetWindowGroupParent (windowGroup, parentGroup);
	} else {
		int parentGroup = OS.GetWindowGroupOfClass (windowClass);
		OS.SetWindowGroupParent (windowGroup, parentGroup);
	}
	OS.SetWindowGroupOwner (windowGroup, shellHandle);
}

void createWidget () {
	super.createWidget ();
	layoutControl ();
}

void deregister () {
	super.deregister ();
	int [] theRoot = new int [1];
	OS.GetRootControl (shellHandle, theRoot);
	WidgetTable.remove (theRoot [0]);
}

void destroyWidget () {
	int theWindow = shellHandle;
//	OS.HideWindow (shellHandle);
	releaseHandle ();
	if (theWindow != 0) OS.DisposeWindow (theWindow);
}

Cursor findCursor () {
	return cursor;
}

public void forceActive () {
	checkWidget ();
	OS.SetFrontProcess (new int [] {0, OS.kCurrentProcess});
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
	return new Rectangle (rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
}

public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

int getDrawCount (int control) {
	if (!isTrimHandle (control)) return drawCount;
	return 0;
}

public int getImeInputMode () {
	checkWidget();
	return SWT.NONE;
}

public Point getLocation () {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	return new Point (rect.left, rect.top);
}

public boolean getMaximized () {
	checkWidget();
	//NOT DONE
	return super.getMaximized ();
}

public boolean getMinimized () {
	checkWidget();
	return OS.IsWindowCollapsed (shellHandle);
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

boolean hasBorder () {
	return false;
}

void hookEvents () {
	super.hookEvents ();
	int mouseProc = display.mouseProc;
	int windowProc = display.windowProc;
	int[] mask1 = new int [] {
		OS.kEventClassWindow, OS.kEventWindowActivated,
		OS.kEventClassWindow, OS.kEventWindowBoundsChanged,
		OS.kEventClassWindow, OS.kEventWindowClose,
		OS.kEventClassWindow, OS.kEventWindowCollapsed,
		OS.kEventClassWindow, OS.kEventWindowDeactivated,
		OS.kEventClassWindow, OS.kEventWindowExpanded,
		OS.kEventClassWindow, OS.kEventWindowHidden,
		OS.kEventClassWindow, OS.kEventWindowShown,
	};
	int windowTarget = OS.GetWindowEventTarget (shellHandle);
	OS.InstallEventHandler (windowTarget, windowProc, mask1.length / 2, mask1, shellHandle, null);
	int [] mask2 = new int [] {
		OS.kEventClassMouse, OS.kEventMouseDown,
		OS.kEventClassMouse, OS.kEventMouseDragged,
//		OS.kEventClassMouse, OS.kEventMouseEntered,
//		OS.kEventClassMouse, OS.kEventMouseExited,
		OS.kEventClassMouse, OS.kEventMouseMoved,
		OS.kEventClassMouse, OS.kEventMouseUp,
		OS.kEventClassMouse, OS.kEventMouseWheelMoved,
	};
	OS.InstallEventHandler (windowTarget, mouseProc, mask2.length / 2, mask2, shellHandle, null);
}

public boolean isEnabled () {
	checkWidget();
	return getEnabled ();
}

public boolean isVisible () {
	checkWidget();
	return getVisible ();
}

int kEventWindowActivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowActivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Bug in the Macintosh.  Despite the that a window has scope
	* kWindowActivationScopeNone, it gets kEventWindowActivated
	* events but does not get kEventWindowDeactivated events.  The
	* fix is to ignore kEventWindowActivated events.
	*/
	int [] outScope = new int [1];
	OS.GetWindowActivationScope (shellHandle, outScope); 
	if (outScope [0] == OS.kWindowActivationScopeNone) return result;
	Display display = getDisplay ();
	display.setMenuBar (menuBar);
	sendEvent (SWT.Activate);
	restoreFocus ();
	return result;
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
		resized = true;
		layoutControl ();
		sendEvent (SWT.Resize);
		if (layout != null) layout.layout (this, false);
	}
	return result;
}

int kEventWindowClose (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowClose (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	closeWidget ();
	return OS.noErr;
}

int kEventWindowCollapsed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowCollapsed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendEvent (SWT.Iconify);
	return result;
}

int kEventWindowDeactivated (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowDeactivated (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendEvent (SWT.Deactivate);
	saveFocus ();
	if (savedFocus != null) {
		/*
		* Bug in the Macintosh.  When ClearKeyboardFocus() is called,
		* the control that has focus gets two kEventControlSetFocus
		* events indicating that focus was lost.  The fix is to ignore
		* both of these and send the focus lost event explicitly.		*/
		display.ignoreFocus = true;
		OS.ClearKeyboardFocus (shellHandle);
		display.ignoreFocus = false;
		if (!savedFocus.isDisposed ()) savedFocus.sendFocusEvent (false);
	}
	Display display = getDisplay ();
	display.setMenuBar (null);
	return result;
}

int kEventWindowExpanded (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowExpanded (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendEvent (SWT.Deiconify);
	return result;
}

int kEventWindowHidden (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowHidden (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.setWindowVisible (false);
	}
	return OS.eventNotHandledErr;
}

int kEventWindowShown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventWindowShown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed () && shell.getVisible ()) {
			shell.setWindowVisible (true);
		}
	}
	return OS.eventNotHandledErr;
}

void layoutControl () {
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short)  OS.kWindowContentRgn, rect);
	int control = scrolledHandle != 0 ? scrolledHandle : handle;
	setBounds (control, 0, 0, rect.right - rect.left, rect.bottom - rect.top, false, true, false);
	super.layoutControl ();
}

public void open () {
	checkWidget();
	OS.SelectWindow (shellHandle);
	setVisible (true);
	if (!restoreFocus ()) traverseGroup (true);
}

void register () {
	super.register ();
	int [] theRoot = new int [1];
	OS.GetRootControl (shellHandle, theRoot);
	WidgetTable.put (theRoot [0], this);
}

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

void releaseShells () {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) shell.dispose ();
	}
}

void releaseWidget () {
	releaseShells ();
	super.releaseWidget ();
	if (windowGroup != 0) OS.ReleaseWindowGroup (windowGroup);
	windowGroup = 0;
	lastActive = null;
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

void setActiveControl (Control control) {
	if (control != null && control.isDisposed ()) control = null;
	if (lastActive != null && lastActive.isDisposed ()) lastActive = null;
	if (lastActive == control) return;
	
	/*
	* Compute the list of controls to be activated and
	* deactivated by finding the first common parent
	* control.
	*/
	Control [] activate = (control == null) ? new Control[0] : control.getPath ();
	Control [] deactivate = (lastActive == null) ? new Control[0] : lastActive.getPath ();
	lastActive = control;
	int index = 0, length = Math.min (activate.length, deactivate.length);
	while (index < length) {
		if (activate [index] != deactivate [index]) break;
		index++;
	}
	
	/*
	* It is possible (but unlikely), that application
	* code could have destroyed some of the widgets. If
	* this happens, keep processing those widgets that
	* are not disposed.
	*/
	for (int i=deactivate.length-1; i>=index; --i) {
		if (!deactivate [i].isDisposed ()) {
			deactivate [i].sendEvent (SWT.Deactivate);
		}
	}
	for (int i=activate.length-1; i>=index; --i) {
		if (!activate [i].isDisposed ()) {
			activate [i].sendEvent (SWT.Activate);
		}
	}
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget ();
	width = Math.max (0, width);
	height = Math.max (0, height);
	Rect rect = new Rect ();
	OS.SetRect (rect, (short) x, (short) y, (short) (x + width), (short) (y + height));
	OS.SetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
}

public void setMenuBar (Menu menu) {
	checkWidget();
	super.setMenuBar (menu);
	Display display = getDisplay ();
	if (display.getActiveShell () == this) {
		display.setMenuBar (menuBar);
	}
}

public void setImeInputMode (int mode) {
	checkWidget();
}

public void setLocation (int x, int y) {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetWindowBounds (shellHandle, (short) OS.kWindowStructureRgn, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	OS.SetRect (rect, (short) x, (short) y, (short) (x + width), (short) (y + height));
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
	width = Math.max (0, width);
	height = Math.max (0, height);
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
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	if (parent != null && !parent.isVisible ()) return;
	setWindowVisible (visible);
}

void setWindowVisible (boolean visible) {
	if (OS.IsWindowVisible (shellHandle) == visible) return;
	if (visible) {
		if (!resized) {
			sendEvent (SWT.Resize);
			if (layout != null) layout.layout (this, false);
		}
		sendEvent (SWT.Show);
		if (isDisposed ()) return;
		int inModalKind = OS.kWindowModalityNone;
		if ((style & SWT.PRIMARY_MODAL) != 0) inModalKind = OS.kWindowModalityWindowModal;
		if ((style & SWT.APPLICATION_MODAL) != 0) inModalKind = OS.kWindowModalityAppModal;
		if ((style & SWT.SYSTEM_MODAL) != 0) inModalKind = OS.kWindowModalitySystemModal;
		if (inModalKind != OS.kWindowModalityNone) {
			int inUnavailableWindow = 0;
			if (parent != null) inUnavailableWindow = OS.GetControlOwner (parent.handle);
			OS.SetWindowModality (shellHandle, inModalKind, inUnavailableWindow);
		}
		OS.SetControlVisibility (topHandle (), true, false);
		OS.ShowWindow (shellHandle);
	} else {
    	OS.HideWindow (shellHandle);
		OS.SetControlVisibility (topHandle (), false, false);
		sendEvent (SWT.Hide);
	}
}

void setZOrder () {
	if (scrolledHandle != 0) OS.HIViewAddSubview (scrolledHandle, handle);
}

void setZOrder (Control control, boolean above) {
	if (above) {
		//NOT DONE - move one window above another
	 	OS.BringToFront (shellHandle);
	 } else {
		int window = control == null ? 0 : OS.GetControlOwner (control.handle);
		OS.SendBehind (shellHandle, window);
	}
}

}

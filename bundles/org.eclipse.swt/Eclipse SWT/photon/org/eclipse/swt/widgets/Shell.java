package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class Shell extends Decorations {
	int shellHandle;
	Display display;
	int modal, blockedList;
	Control lastFocus;

public Shell () {
	this ((Display) null);
}

public Shell (int style) {
	this ((Display) null, style);
}

public Shell (Display display) {
	this (display, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE);
}

public Shell (Display display, int style) {
	this (display, null, style, 0);
}

Shell (Display display, Shell parent, int style, int handle) {
	super ();
	if (display == null) display = Display.getCurrent ();
	if (display == null) display = Display.getDefault ();
	if (!display.isValidThread ()) {
		error (SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.style = checkStyle (style);
	this.parent = parent;
	this.display = display;
	this.handle = handle;
	createWidget (0);
}

public Shell (Shell parent) {
	this (parent, SWT.TITLE | SWT.CLOSE | SWT.BORDER);
}

public Shell (Shell parent, int style) {
	this (parent != null ? parent.getDisplay () : null, parent, style, 0);
}

public static Shell photon_new (Display display, int handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle);
}

public void addShellListener (ShellListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Close,typedListener);
	addListener (SWT.Iconify,typedListener);
	addListener (SWT.Deiconify,typedListener);
	addListener (SWT.Activate, typedListener);
	addListener (SWT.Deactivate, typedListener);
}

void bringToTop () {
	OS.PtWidgetToFront (shellHandle);
}

void closeWidget () {
	Event event = new Event ();
	event.time = (int) System.currentTimeMillis ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}

public void close () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	closeWidget ();
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_WINDOW_RENDER_FLAGS, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int flags = args [1];
	int [] left = new int [1], top = new int [1];
	int [] right = new int [1], bottom = new int [1];
	OS.PtFrameSize (flags, 0, left, top, right, bottom);
	int trimX = x - left [0];
	int trimY = y - top [0];
	int trimWidth = width + left [0] + right [0];
	int trimHeight = height + top [0] + bottom [0];
	if (menuBar != null) {
		PhDim_t dim = new PhDim_t ();
		int menuHandle = menuBar.handle;
		if (!OS.PtWidgetIsRealized (menuHandle)) {
			OS.PtExtentWidgetFamily (menuHandle);
		}
		OS.PtWidgetPreferredSize (menuHandle, dim);
		trimHeight += dim.h;
		trimY -= dim.h;
	}
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}

void createHandle (int index) {
	state |= HANDLE | CANVAS;
	if (handle != 0) {
		int clazz = display.PtContainer;
		int [] args = {
			OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		shellHandle = OS.PtCreateWidget (clazz, handle, args.length / 3, args);
		if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		int parentHandle = 0;
		if (parent != null) parentHandle = parent.topHandle ();
		PhRect_t rect = new PhRect_t ();
		OS.PhWindowQueryVisible (OS.Ph_QUERY_GRAPHICS, 0, 1, rect);
		int width = (short) ((rect.lr_x - rect.ul_x + 1) * 5 / 8);
		int height = (short) ((rect.lr_y - rect.ul_y + 1) * 5 / 8);
		int decorations = 0;
		int flags =
			OS.Ph_WM_RENDER_MIN | OS.Ph_WM_RENDER_MAX | OS.Ph_WM_RENDER_RESIZE |
			OS.Ph_WM_RENDER_BORDER | OS.Ph_WM_RENDER_MENU | OS.Ph_WM_RENDER_MIN |
			OS.Ph_WM_RENDER_TITLE;
		if ((style & SWT.NO_TRIM) == 0) {
			if ((style & SWT.MIN) != 0) decorations |= OS.Ph_WM_RENDER_MIN;
			if ((style & SWT.MAX) != 0) decorations |= OS.Ph_WM_RENDER_MAX;
			if ((style & SWT.RESIZE) != 0) {
				decorations |= OS.Ph_WM_RENDER_BORDER | OS.Ph_WM_RENDER_RESIZE;
			}
			if ((style & SWT.BORDER) != 0) decorations |= OS.Ph_WM_RENDER_BORDER;
			if ((style & SWT.MENU) != 0) decorations |= OS.Ph_WM_RENDER_MENU;
			if ((style & SWT.TITLE) != 0) decorations |= OS.Ph_WM_RENDER_TITLE;
		}
		int notifyFlags =
			OS.Ph_WM_ICON | OS.Ph_WM_FOCUS | 
			OS.Ph_WM_MOVE | OS.Ph_WM_RESIZE;
		int windowState = OS.Ph_WM_STATE_ISFOCUS;
		if ((style & SWT.ON_TOP) != 0) windowState = OS.Ph_WM_STATE_ISFRONT;
		int titlePtr = OS.malloc (1);
		int [] args = {
			OS.Pt_ARG_WIDTH, width, 0,
			OS.Pt_ARG_HEIGHT, height, 0,
			OS.Pt_ARG_WINDOW_TITLE, titlePtr, 0,
			OS.Pt_ARG_WINDOW_RENDER_FLAGS, decorations, flags,
			OS.Pt_ARG_WINDOW_MANAGED_FLAGS, 0, OS.Ph_WM_CLOSE,
			OS.Pt_ARG_WINDOW_NOTIFY_FLAGS, notifyFlags, notifyFlags,
			OS.Pt_ARG_WINDOW_STATE, windowState, ~0,
			OS.Pt_ARG_FLAGS, OS.Pt_DELAY_REALIZE, OS.Pt_DELAY_REALIZE,
			OS.Pt_ARG_FILL_COLOR, OS.Pg_TRANSPARENT, 0,
			OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		};
		OS.PtSetParentWidget (parentHandle);
		shellHandle = OS.PtCreateWidget (OS.PtWindow (), parentHandle, args.length / 3, args);
		OS.free (titlePtr);
		if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	createScrolledHandle (shellHandle);
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.RESIZE)) == 0) {
		int [] args = {
			OS.Pt_ARG_FLAGS, OS.Pt_HIGHLIGHTED, OS.Pt_HIGHLIGHTED,
			OS.Pt_ARG_BASIC_FLAGS, OS.Pt_ALL_OUTLINES, OS.Pt_ALL_OUTLINES,
		};
		OS.PtSetResources (scrolledHandle, args.length / 3, args);
	}
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	resizeBounds (args [1], args [4]);
}

void deregister () {
	super.deregister ();
	WidgetTable.remove (shellHandle);
}


public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

public int getImeInputMode () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return 0;
}

public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled ();
}

public Point getLocation () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//NOT DONE - shell location is 0,0 when queried before event loop
	return super.getLocation ();
}

public boolean getMaximized () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//ONLY WORKS WHEN SET in setMaximized
	int [] args = {OS.Pt_ARG_WINDOW_STATE, 0, OS.Ph_WM_STATE_ISMAX};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	return (args [1] & OS.Ph_WM_STATE_ISMAX) != 0;
}

public boolean getMinimized () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//ONLY WORKS WHEN SET in setMinimized
	int [] args = {OS.Pt_ARG_WINDOW_STATE, 0, OS.Ph_WM_STATE_ISHIDDEN};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	return (args [1] & OS.Ph_WM_STATE_ISHIDDEN) != 0;
}

public Shell getShell () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return this;
}

public Shell [] getShells () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {
		OS.Pt_ARG_WINDOW_RENDER_FLAGS, 0, 0,
		OS.Pt_ARG_WIDTH, 0, 0,
		OS.Pt_ARG_HEIGHT, 0, 0,
	};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int flags = args [1];
	int [] left = new int [1], top = new int [1];
	int [] right = new int [1], bottom = new int [1];
	OS.PtFrameSize (flags, 0, left, top, right, bottom);
	int width = args [4] + left [0] + right [0];
	int height = args [7] + top [0] + bottom [0];
	return new Point (width, height);
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (shellHandle, OS.Pt_CB_WINDOW, windowProc, SWT.Move);
	OS.PtAddCallback (shellHandle, OS.Pt_CB_RESIZE, windowProc, SWT.Resize);
}

public void open () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	bringToTop ();
	setVisible (true);
}

int processEvent (int widget, int data, int info) {
	if (widget == shellHandle && data == SWT.Resize) {
		return processShellResize (info);
	}
	return super.processEvent (widget, data, info);;
}

int processHotkey (int data, int info) {
	if (data != 0) {
		Widget widget = WidgetTable.get (data);
		if (widget instanceof MenuItem) {
			MenuItem item = (MenuItem) widget;
			if (item.isEnabled ()) item.processSelection (info);
		}
	}
	return OS.Pt_CONTINUE;
}

int processMove (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	PhWindowEvent_t we = new PhWindowEvent_t ();
	OS.memmove (we, cbinfo.cbdata, PhWindowEvent_t.sizeof);
	switch (we.event_f) {
		case OS.Ph_WM_CLOSE:
			closeWidget ();
			break;
		case OS.Ph_WM_ICON:
			if ((we.state_f & OS.Ph_WM_STATE_ISICONIFIED) != 0) {
				sendEvent (SWT.Iconify);
			} else {
				sendEvent (SWT.Deiconify);
			}
			break;
		case OS.Ph_WM_FOCUS:
			switch (we.event_state) {
				case OS.Ph_WM_EVSTATE_FOCUS: sendEvent (SWT.Activate); break;
				case OS.Ph_WM_EVSTATE_FOCUSLOST: sendEvent (SWT.Deactivate); break;
			}
			break;
		case OS.Ph_WM_MOVE:
			sendEvent (SWT.Move);
			break;	
	}
	return OS.Pt_CONTINUE;
}

int processShellResize (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_CONTINUE;
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	resizeBounds (args [1], args [4]);
	return OS.Pt_CONTINUE;
}

void register () {
	super.register ();
	WidgetTable.put (shellHandle, this);
}

void realizeWidget() {
	/* Do nothing */
}

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = 0;
}

void releaseShells () {
	Shell [] shells = getShells ();
	for (int i=0; i<shells.length; i++) {
		Shell shell = shells [i];
		if (!shell.isDisposed ()) {
			/*
			* Feature in Photon.  A shell may have child shells that have been
			* temporarily reparented to NULL because they were shown without
			* showing the parent.  In this case, Photon will not destroy the
			* child shells because they are not in the widget hierarchy.
			* The fix is to detect this case and destroy the shells.
			*/
			if (shell.parent != null && OS.PtWidgetParent (shell.shellHandle) == 0) {
				shell.dispose ();
			} else {
				shell.releaseWidget ();
				shell.releaseHandle ();
			}
		}
	}
}

void releaseWidget () {
	releaseShells ();
	super.releaseWidget ();
	if (blockedList != 0) OS.PtUnblockWindows (blockedList);
	blockedList = 0;
	lastFocus = null;
	display = null;
}

public void removeShellListener (ShellListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Close, listener);
	eventTable.unhook (SWT.Iconify,listener);
	eventTable.unhook (SWT.Deiconify,listener);
	eventTable.unhook (SWT.Activate, listener);
	eventTable.unhook (SWT.Deactivate, listener);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (OS.PtWidgetClass (shellHandle) != OS.PtWindow ()) {
		super.setBounds (x, y, width, height, move, resize);
		if (resize) resizeBounds (width, height);
		return;
	}
	int [] args = {OS.Pt_ARG_WINDOW_RENDER_FLAGS, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int flags = args [1];
	int [] left = new int [1], top = new int [1];
	int [] right = new int [1], bottom = new int [1];
	OS.PtFrameSize (flags, 0, left, top, right, bottom);
	PhArea_t area = new PhArea_t ();
	OS.PtWidgetArea (shellHandle, area);
	int frameWidth = area.size_w + left [0] + right [0];
	int frameHeight = area.size_h + top [0] + bottom [0];
	if (!move) {
		x = area.pos_x;
		y = area.pos_y;
	}
	if (!resize) {
		width = frameWidth;
		height = frameHeight;
	}
	boolean sameOrigin = x == area.pos_x && y == area.pos_y;
	boolean sameExtent = width == frameWidth && height == frameHeight;
	area.pos_x = (short) x;
	area.pos_y = (short) y;
	area.size_w = (short) (Math.max (width - left [0] - right [0], 0));
	area.size_h = (short) (Math.max (height - top [0] - bottom [0], 0));
//TO DO - for some reason shell will move but won't resize after realize
	int ptr = OS.malloc (PhArea_t.sizeof);
	OS.memmove (ptr, area, PhArea_t.sizeof);
	args = new int [] {OS.Pt_ARG_AREA, ptr, 0};
	OS.PtSetResources (shellHandle, args.length / 3, args);
	OS.free (ptr);
	/*
	* Feature in Photon.  The shell does not issue WM_SIZE
	* event notificatoin until it is realized.  The fix is
	* to detect size changes and send the events.
	*/
	if (!OS.PtWidgetIsRealized (shellHandle)) {
		if (!sameOrigin & move) sendEvent (SWT.Move);
		if (!sameExtent & resize) {
			resizeBounds (width, height);
			sendEvent (SWT.Resize);
		}
	}
}

public void setImage (Image image) {
}

public void setImeInputMode (int mode) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}

public void setMaximized (boolean maximized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (OS.PtWidgetIsRealized (shellHandle)) {
		// RESTORE DOESN'T WORK
		PhWindowEvent_t event = new PhWindowEvent_t ();
		event.event_f = maximized ? OS.Ph_WM_MAX : OS.Ph_WM_RESTORE;
		event.event_state = (short) (maximized ? OS.Ph_WM_EVSTATE_HIDE : OS.Ph_WM_EVSTATE_UNHIDE);
		event.rid = OS.PtWidgetRid (shellHandle);
		OS.PtForwardWindowEvent (event);
	} else {
		int bits = 0;
		if (maximized) bits = OS.Ph_WM_STATE_ISMAX;
		int [] args = {OS.Pt_ARG_WINDOW_STATE, bits, OS.Ph_WM_STATE_ISMAX};
		OS.PtSetResources (shellHandle, args.length / 3, args);
	}
}

public void setMenuBar (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (menuBar == menu) return;
	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}
	if (menuBar != null) {
		int menuHandle = menuBar.handle;
		int [] args = {
			OS.Pt_ARG_FLAGS, OS.Pt_DELAY_REALIZE, OS.Pt_DELAY_REALIZE,
		};
		OS.PtSetResources (menuHandle, args.length / 3, args);
		OS.PtUnrealizeWidget (menuBar.handle);
	}
	menuBar = menu;
	int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
	OS.PtGetResources (shellHandle, args.length / 3, args);
	int width = args [1], height = args [4];
	if (menuBar != null) {
		int menuHandle = menu.handle;
		args = new int [] {
			OS.Pt_ARG_WIDTH, width, 0,
			OS.Pt_ARG_FLAGS, 0, OS.Pt_DELAY_REALIZE,
		};
		OS.PtSetResources (menuHandle, args.length / 3, args);	
		OS.PtRealizeWidget (menuHandle);
	}
	resizeBounds(width, height);
}

public void setMinimized (boolean minimized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (OS.PtWidgetIsRealized (shellHandle)) {
		// RESTORE DOESN'T WORK
		PhWindowEvent_t event = new PhWindowEvent_t ();
		event.event_f = minimized ? OS.Ph_WM_HIDE : OS.Ph_WM_RESTORE;
		event.event_state = (short) (minimized ? OS.Ph_WM_EVSTATE_HIDE : OS.Ph_WM_EVSTATE_UNHIDE);
		event.rid = OS.PtWidgetRid (shellHandle);
		OS.PtForwardWindowEvent (event);
	} else {
		int bits = 0;
		if (minimized) bits = OS.Ph_WM_STATE_ISHIDDEN;
		int [] args = {OS.Pt_ARG_WINDOW_STATE, bits, OS.Ph_WM_STATE_ISHIDDEN};
		OS.PtSetResources (shellHandle, args.length / 3, args);
	}
}

public void setModal (int modal) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	switch (modal) {
		case SWT.MODELESS:
		case SWT.PRIMARY_MODAL:
		case SWT.APPLICATION_MODAL:
		case SWT.SYSTEM_MODAL:
			this.modal = modal;
			break;
	}
}

public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = string;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] args = {OS.Pt_ARG_WINDOW_TITLE, ptr, 0};
	OS.PtSetResources (shellHandle, args.length / 3, args);
	OS.free (ptr);
}

public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (visible == OS.PtWidgetIsRealized (shellHandle)) return;
	/*
	* Feature in Photon.  It is not possible to show a PtWindow
	* whose parent is not realized.  The fix is to temporarily
	* reparent the child shell to NULL and then realize the child
	* shell.
	*/
	if (parent != null) {
		Shell shell = parent.getShell ();
		int parentHandle = shell.shellHandle;
		if (!OS.PtWidgetIsRealized (parentHandle)) {
			OS.PtReParentWidget (shellHandle, visible ? 0 : parentHandle);
		}
	}
	switch (modal) {
		case SWT.PRIMARY_MODAL:
			//NOT DONE: should not disable all windows
		case SWT.APPLICATION_MODAL:
		case SWT.SYSTEM_MODAL:
			if (visible) {
				blockedList = OS.PtBlockAllWindows (shellHandle, (short) 0, 0);
			} else {
				if (blockedList != 0) OS.PtUnblockWindows (blockedList);
				blockedList = 0;
			}
	}
	super.setVisible (visible);
	/*
	* Feature in Photon.  When a shell is shown, it may have child
	* shells that have been temporarily reparented to NULL because
	* the child was shown before the parent.  The fix is to reparent
	* the child shells back to the correct parent.
	*/
	if (visible) {
		Shell [] shells = getShells ();
		for (int i=0; i<shells.length; i++) {
			int childHandle = shells [i].shellHandle;
			if (OS.PtWidgetParent (childHandle) == 0) {
				OS.PtReParentWidget (childHandle, shellHandle);
			}
		}
	}
	OS.PtSyncWidget (shellHandle);
	OS.PtFlush ();
}

int topHandle () {
	return shellHandle;
}

}

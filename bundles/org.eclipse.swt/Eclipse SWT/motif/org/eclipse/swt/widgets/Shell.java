package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/**
*	The shell class implements a top level window
* and dialog windows.
*
* Styles
*
*	ON_TOP
*	DBCS
*
* Events
*
**/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/* Class Definition */
public /*final*/ class Shell extends Decorations {
	Display display;
	int shellHandle;
	boolean reparented, realized;
	int oldX, oldY, oldWidth, oldHeight;
	Control lastFocus;
/**
* Creates a widget.
*/
public Shell () {
	this ((Display) null);
}
/**
* Creates a widget.
*/
public Shell (int style) {
	this ((Display) null, style);
}
/**
* Creates a widget.
*/
public Shell (Display display) {
	this (display, SWT.SHELL_TRIM);
}
/**
* Creates a widget.
* <p>
*	This method creates a top level shell widget on a
* display using style bits to select a particular look
* or set of properties. 
*
* @param display the Display (or null for the default)
* @param style	the bitwise OR'ing of widget styles
*
* @exception SWTError(ERROR_ERROR_INVALID_PARENT)
*	when the parent is invalid
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
*/
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
/**
* Creates a widget.
*/
public Shell (Shell parent) {
	this (parent, SWT.DIALOG_TRIM);
}
/**
* Creates a widget.
*/
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

public static Shell motif_new (Display display, int handle) {
	return new Shell (display, null, SWT.NO_TRIM, handle);
}

/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addShellListener(ShellListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener(SWT.Activate,typedListener);
	addListener(SWT.Close,typedListener);
	addListener(SWT.Deactivate,typedListener);
	addListener(SWT.Iconify,typedListener);
	addListener(SWT.Deiconify,typedListener);
}
void adjustTrim () {
	if ((style & SWT.ON_TOP) != 0) return;
	
	/* Query the trim insets */
	int shellWindow = OS.XtWindow (shellHandle);
	if (shellWindow == 0) return;
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
	
	/* Find the direct child of the root window */
	int [] unused = new int [1];
	int [] rootWindow = new int [1];
	int [] parent = new int [1];
	int [] ptr = new int [1];
	int trimWindow = shellWindow;
	OS.XQueryTree (xDisplay, trimWindow, rootWindow, parent, ptr, unused);
	if (ptr [0] != 0) OS.XFree (ptr [0]);
	if (parent [0] == 0) return;
	while (parent [0] != rootWindow [0]) {
		trimWindow = parent [0];
		OS.XQueryTree (xDisplay, trimWindow, unused, parent, ptr, unused);
		if (ptr [0] != 0) OS.XFree (ptr [0]);
		if (parent [0] == 0) return;	
	}
	
	/**
	 * Translate the coordinates of the shell window to the
	 * coordinates of the direct child of the root window
	 */
	if (shellWindow == trimWindow) return;

	/* Query the border width of the direct child of the root window */
	int [] trimBorder = new int [1];
	int [] trimWidth = new int [1];
	int [] trimHeight = new int [1];
	OS.XGetGeometry (xDisplay, trimWindow, unused, unused, unused, trimWidth, trimHeight, trimBorder, unused);

	/* Query the border width of the direct child of the shell window */
	int [] shellBorder = new int [1];
	int [] shellWidth = new int [1];
	int [] shellHeight = new int [1];
	OS.XGetGeometry (xDisplay, shellWindow, unused, unused, unused, shellWidth, shellHeight, shellBorder, unused);

	/* Calculate the trim */
	int width = (trimWidth [0] + (trimBorder [0] * 2)) - (shellWidth [0] + (shellBorder [0] * 2));
	int height = (trimHeight [0] + (trimBorder [0] * 2)) - (shellHeight [0] + (shellBorder [0] * 2));
	
	/* Update the trim guesses to match the query */
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	if ((style & SWT.NO_TRIM) == 0) {
		hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
		hasResize = (style & SWT.RESIZE) != 0;
		hasBorder = (style & SWT.BORDER) != 0;
	}
	if (hasTitle) {
		if (hasResize)  {
			display.titleResizeTrimWidth = width;
			display.titleResizeTrimHeight = height;
			return;
		}
		if (hasBorder) {
			display.titleBorderTrimWidth = width;
			display.titleBorderTrimHeight = height;
			return;
		}
		display.titleTrimWidth = width;
		display.titleTrimHeight = height;
		return;
	}
	if (hasResize) {
		display.resizeTrimWidth = width;
		display.resizeTrimHeight = height;
		return;
	}
	if (hasBorder) {
		display.borderTrimWidth = width;
		display.borderTrimHeight = height;
		return;
	}
}
public void close () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	closeWidget ();
}
void closeWidget () {
	if (!isEnabled ()) return;
	Control widget = parent;
	while (widget != null && !(widget.getShell ().isModal ())) {
		widget = widget.parent;
	}
	if (widget == null) {
		Shell [] shells = getShells ();
		for (int i=0; i<shells.length; i++) {
			Shell shell = shells [i];
			if (shell != this && shell.isModal () && shell.isVisible ()) {
				shell.bringToTop ();
				return;
			}
		}
	}
	Event event = new Event ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Rectangle trim = super.computeTrim (x, y, width, height);
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	trim.x -= trimWidth / 2; trim.y -= trimHeight - (trimWidth / 2);
	trim.width += trimWidth; trim.height += trimHeight;
	return trim;
}
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	int decorations = 0;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.MIN) != 0) decorations |= OS.MWM_DECOR_MINIMIZE;
		if ((style & SWT.MAX) != 0) decorations |= OS.MWM_DECOR_MAXIMIZE;
		if ((style & SWT.RESIZE) != 0) decorations |= OS.MWM_DECOR_RESIZEH;
		if ((style & SWT.BORDER) != 0) decorations |= OS.MWM_DECOR_BORDER;
		if ((style & SWT.MENU) != 0) decorations |= OS.MWM_DECOR_MENU;
		if ((style & SWT.TITLE) != 0) decorations |= OS.MWM_DECOR_TITLE;
		/*
		* Feature in Motif.  Under some Window Managers (Sawmill), in order
		* to get any border at all from the window manager it is necessary
		* to set MWM_DECOR_BORDER.  The fix is to force these bits when any
		* kind of border is requested.
		*/
		if ((style & SWT.RESIZE) != 0) decorations |= OS.MWM_DECOR_BORDER;
	}
	
	/*
	* Note: Motif treats the modal values as hints to the Window Manager.
	* For example, Enlightenment treats all modes except for SWT.MODELESS
	* as SWT.APPLICATION_MODAL.  The Motif Window Manager honours all modes.
	*/
	int inputMode = OS.MWM_INPUT_MODELESS;
	if ((style & SWT.PRIMARY_MODAL) != 0) inputMode = OS.MWM_INPUT_PRIMARY_APPLICATION_MODAL;
	if ((style & SWT.APPLICATION_MODAL) != 0) inputMode = OS.MWM_INPUT_FULL_APPLICATION_MODAL;
	if ((style & SWT.SYSTEM_MODAL) != 0) inputMode = OS.MWM_INPUT_SYSTEM_MODAL;
	
	/* 
	* Bug in Motif.  For some reason, if the title string
	* length is not a multiple of 4, Motif occasionally
	* draws garbage after the last character in the title.
	* The fix is to pad the title.
	*/
	byte [] buffer = {(byte)' ', 0, 0, 0};
	int ptr = OS.XtMalloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] argList = {
		OS.XmNmwmInputMode, inputMode,
		OS.XmNmwmDecorations, decorations,
		OS.XmNtitle, ptr,
	};
	byte [] appClass = display.appClass;
	if (parent == null && (style & SWT.ON_TOP) == 0) {
		int xDisplay = display.xDisplay;
		int widgetClass = OS.TopLevelShellWidgetClass ();
		shellHandle = OS.XtAppCreateShell (display.appName, appClass, widgetClass, xDisplay, argList, argList.length / 2);
	} else {
		int widgetClass = OS.TransientShellWidgetClass ();
		if ((style & SWT.ON_TOP) != 0) widgetClass = OS.OverrideShellWidgetClass ();
		int parentHandle = display.shellHandle;
		if (parent != null) parentHandle = parent.handle;
		shellHandle = OS.XtCreatePopupShell (appClass, widgetClass, parentHandle, argList, argList.length / 2);
	}
	OS.XtFree (ptr);
	if (shellHandle == 0) error (SWT.ERROR_NO_HANDLES);

	/* Create scrolled handle */
	createScrolledHandle (shellHandle);

	/*
	* Feature in Motif.  There is no way to get the single pixel
	* border surrounding a TopLevelShell or a TransientShell.
	* Also, attempts to set a border on either the shell handle
	* or the main window handle fail.  The fix is to set the border
	* on the client area.
	*/
	if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.RESIZE)) == 0) {
		int [] argList1 = {OS.XmNborderWidth, 1};
		OS.XtSetValues (handle, argList1, argList1.length / 2);
	}
}
void deregister () {
	super.deregister ();
	WidgetTable.remove (shellHandle);
}
void destroyWidget () {
	/*
	* Hide the shell before calling XtDestroyWidget ()
	* so that the shell will disappear without having
	* to dispatch events.  Otherwise, the user will be
	* able to interact with the trimmings between the
	* time that the shell is destroyed and the next
	* event is dispatched.
	*/
	if (OS.XtIsRealized (shellHandle)) {
		if (OS.XtIsTopLevelShell (shellHandle)) {
			OS.XtUnmapWidget (shellHandle);
		} else {
			OS.XtPopdown (shellHandle);
		}
	}
	super.destroyWidget ();
}

public void dispose () {
	/*
	* This code is intentionally commented.  On some
	* platforms, the owner window is repainted right
	* away when the dialog window exits.  This behavior
	* is currently unspecified.
	*/
//	/*
//	* Note:  It is valid to attempt to dispose a widget
//	* more than once.  If this happens, fail silently.
//	*/
//	if (!isValidWidget ()) return;
//	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
//	Display oldDisplay = display;
	super.dispose ();
//	if (oldDisplay != null) oldDisplay.update ();
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	enableHandle (enabled, shellHandle);
}
public int getBorderWidth () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	return argList [1];
}
public Rectangle getBounds () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, root_x, root_y);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	int border = argList [5];
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	int width = argList [1] + trimWidth + (border * 2);
	int height = argList [3] + trimHeight + (border * 2);
	return new Rectangle (root_x [0], root_y [0], width, height);
}
/**
* Gets the Display.
*/
public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}
public int getImeInputMode () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return SWT.NONE;
}
public Point getLocation () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, root_x, root_y);
	return new Point (root_x [0], root_y [0]);
}
public Shell getShell () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return this;
}
/**
* Get dialogs shells.
* <p>
* @return the dialog shells
*
* @exception SWTError(ERROR_ERROR_INVALID_PARENT)
*	when the parent is invalid
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
*/
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
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	int border = argList [5];
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	int width = argList [1] + trimWidth + (border * 2);
	int height = argList [3] + trimHeight + (border * 2);
	return new Point (width, height);
}
public boolean getVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!OS.XtIsRealized (handle)) return false;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return false;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return false;
	XWindowAttributes attributes = new XWindowAttributes ();
	OS.XGetWindowAttributes (xDisplay, xWindow, attributes);
	if (attributes.map_state == OS.IsViewable) return true;
	int [] argList = {OS.XmNmappedWhenManaged, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	return minimized && attributes.map_state == OS.IsUnviewable && argList [1] != 0;
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	OS.XtAddEventHandler (shellHandle, OS.StructureNotifyMask, false, windowProc, SWT.Resize);
	if ((style & SWT.ON_TOP) != 0) return;
	OS.XtAddEventHandler (shellHandle, OS.FocusChangeMask, false, windowProc, SWT.FocusIn);
	int [] argList = {OS.XmNdeleteResponse, OS.XmDO_NOTHING};
	OS.XtSetValues (shellHandle, argList, argList.length / 2);
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay != 0) {
		byte [] WM_DETELE_WINDOW = Converter.wcsToMbcs (null, "WM_DELETE_WINDOW\0", false);
		int atom = OS.XmInternAtom (xDisplay, WM_DETELE_WINDOW, false);	
		OS.XmAddWMProtocolCallback (shellHandle, atom, windowProc, SWT.Dispose);
	}
}
int inputContext () {
	//NOT DONE
	return 0;
}
public boolean isEnabled () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getEnabled ();
}
boolean isModal () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNmwmInputMode, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	return (argList [1] != -1 && argList [1] != OS.MWM_INPUT_MODELESS);
}
public boolean isVisible () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return getVisible ();
}
void manageChildren () {
	OS.XtSetMappedWhenManaged (shellHandle, false);
	super.manageChildren ();
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
	int width = OS.XDisplayWidth (xDisplay, OS.XDefaultScreen (xDisplay)) * 5 / 8;
	int height = OS.XDisplayHeight (xDisplay, OS.XDefaultScreen (xDisplay)) * 5 / 8;
	OS.XtResizeWidget (shellHandle, width, height, 0);
}
public void open () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setVisible (true);
}
int processDispose (int callData) {
	closeWidget ();
	return 0;
}

int processResize (int callData) {
	XConfigureEvent xEvent = new XConfigureEvent ();
	OS.memmove (xEvent, callData, XConfigureEvent.sizeof);
	switch (xEvent.type) {
		case OS.ReparentNotify: {
			if (reparented) return 0;
			reparented = true;
			short [] root_x = new short [1], root_y = new short [1];
			OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, root_x, root_y);
			int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
			OS.XtGetValues (scrolledHandle, argList, argList.length / 2);	
			xEvent.x = root_x [0];  xEvent.y = root_y [0];
			xEvent.width = argList [1];  xEvent.height = argList [3];
			// fall through
		}
		case OS.ConfigureNotify:
			if (!reparented) return 0;
			if (oldX != xEvent.x || oldY != xEvent.y) sendEvent (SWT.Move);
			if (oldWidth != xEvent.width || oldHeight != xEvent.height) {
				sendEvent (SWT.Resize);
				if (layout != null) layout (false);
			}
			if (xEvent.x != 0) oldX = xEvent.x;
			if (xEvent.y != 0) oldY = xEvent.y;
			oldWidth = xEvent.width;
			oldHeight = xEvent.height;
			return 0;
		case OS.UnmapNotify:
			int [] argList = {OS.XmNmappedWhenManaged, 0};
			OS.XtGetValues (shellHandle, argList, argList.length / 2);
			if (argList [1] != 0) {
				minimized = true;
				sendEvent (SWT.Iconify);
			}
			return 0;
		case OS.MapNotify:
			if (minimized) {
				minimized = false;
				sendEvent (SWT.Deiconify);
			}
			return 0;
	}
	return 0;
}

int processSetFocus (int callData) {
	XFocusChangeEvent xEvent = new XFocusChangeEvent ();
	OS.memmove (xEvent, callData, XFocusChangeEvent.sizeof);
	int handle = OS.XtWindowToWidget (xEvent.display, xEvent.window);
	if (handle != shellHandle) return super.processSetFocus (callData);
	if (xEvent.mode != OS.NotifyNormal) return 0;
	if (xEvent.detail != OS.NotifyNonlinear) return 0;
	switch (xEvent.type) {
		case OS.FocusIn:
			postEvent (SWT.Activate);
			break;
		case OS.FocusOut:
			postEvent (SWT.Deactivate);
			break;
	}
	return 0;
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	propagateHandle (enabled, shellHandle);
}
void realizeWidget () {
	if (realized) return;
	OS.XtRealizeWidget (shellHandle);
	realizeChildren ();
	realized = true;
}
void register () {
	super.register ();
	WidgetTable.put (shellHandle, this);
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
			shell.releaseWidget ();
			shell.releaseHandle ();
		}
	}
}
void releaseWidget () {
	releaseShells ();
	super.releaseWidget ();
	display = null;
	lastFocus = null;
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeShellListener(ShellListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Activate, listener);
	eventTable.unhook(SWT.Close, listener);
	eventTable.unhook(SWT.Deactivate, listener);
	eventTable.unhook(SWT.Iconify,listener);
	eventTable.unhook(SWT.Deiconify,listener);
}
void saveBounds () {
	if (!reparented) return;
	short [] root_x = new short [1], root_y = new short [1];
	OS.XtTranslateCoords (scrolledHandle, (short) 0, (short) 0, root_x, root_y);
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	int trimWidth = trimWidth (), trimHeight = trimHeight ();
	oldX = root_x [0] - trimWidth; oldY = root_y [0] - trimHeight;
	oldWidth = argList [1];  oldHeight = argList [3];
}
public void setBounds (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	saveBounds ();
	int newWidth = Math.max (width - trimWidth (), 1);
	int newHeight = Math.max (height - trimHeight (), 1);
	if (!reparented) {
		super.setBounds (x, y, newWidth, newHeight);
		return;
	}
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	OS.XtConfigureWidget (shellHandle, x, y, newWidth, newHeight, 0);
	if (isFocus) caret.setFocus ();
}
public void setImeInputMode (int mode) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
}
public void setLocation (int x, int y) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	saveBounds ();
	if (!reparented) {
		super.setLocation(x, y);
		return;
	}	
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	OS.XtMoveWidget (shellHandle, x, y);
	if (isFocus) caret.setFocus ();
}
public void setMinimized (boolean minimized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	/* 
	* Bug in MOTIF.  For some reason, the receiver does not keep the
	* value of the XmNiconic resource up to date when the user minimizes
	* and restores the window.  As a result, a window that is minimized
	* by the user and then restored by the programmer is not restored.
	* This happens because the XmNiconic resource is unchanged when the
	* window is minimized by the user and subsequent attempts to set the
	* resource fail because the new value of the resource is the same as
	* the old value.  The fix is to force XmNiconic to be up to date
	* before setting the desired value.
	*/
	int [] argList = {OS.XmNiconic, 0};
	OS.XtGetValues (shellHandle, argList, argList.length / 2);
	if ((argList [1] != 0) != this.minimized) {
		argList [1] = this.minimized ? 1 : 0;
		OS.XtSetValues (shellHandle, argList, argList.length / 2);
	}
	
	/* Minimize or restore the shell */
	argList [1] = (this.minimized = minimized) ? 1 : 0;
	OS.XtSetValues (shellHandle, argList, argList.length / 2);

	/* Force the XWindowAttributes to be up to date */
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay != 0) OS.XSync (xDisplay, false);
}

public void setSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	saveBounds ();
	int newWidth = Math.max (width - trimWidth (), 1);
	int newHeight = Math.max (height - trimHeight (), 1);
	if (!reparented) {
		super.setSize(newWidth, newHeight);
		return;
	}	
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	OS.XtResizeWidget (shellHandle, newWidth, newHeight, 0);
	if (isFocus) caret.setFocus ();
}
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	
	/*
	* Feature in Motif.  It is not possible to set a shell
	* title to an empty string.  The fix is to set the title
	* to be a single space.
	*/
	if (string.length () == 0) string = " ";
	byte [] buffer1 = Converter.wcsToMbcs (null, string, true);
	int length = buffer1.length - 1;
	
	/* 
	* Bug in Motif.  For some reason, if the title string
	* length is not a multiple of 4, Motif occasionally
	* draws garbage after the last character in the title.
	* The fix is to pad the title.
	*/
	byte [] buffer2 = buffer1;
	if ((length % 4) != 0) {
		buffer2 = new byte [(length + 3) / 4 * 4];
		System.arraycopy (buffer1, 0, buffer2, 0, length);
	}

	/* Set the title for the shell */
	int ptr = OS.XtMalloc (buffer2.length + 1);
	OS.memmove (ptr, buffer2, buffer2.length);
	int [] argList = {OS.XmNtitle, ptr};
	OS.XtSetValues (shellHandle, argList, argList.length / 2);
	OS.XtFree (ptr);
}
public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	realizeWidget ();

	/* Show the shell */
	if (visible) {

		/* Map the widget */
		OS.XtSetMappedWhenManaged (shellHandle, true);
		if (OS.XtIsTopLevelShell (shellHandle)) {
			OS.XtMapWidget (shellHandle);
		} else {
			OS.XtPopup (shellHandle, OS.XtGrabNone);
		}
		
		/*
		* Force the shell to be fully exposed before returning.
		* This ensures that the shell coordinates are correct
		* when queried directly after showing the shell.
		*/
		do {
			display.update ();
		} while (!isVisible ());
		adjustTrim ();

		/* Set the saved focus widget */
		if (savedFocus != null && !savedFocus.isDisposed ()) {
			savedFocus.setFocus ();
		}
		savedFocus = null;
		
		sendEvent (SWT.Show);
		return;
	}

	/* Hide the shell */
	OS.XtSetMappedWhenManaged (shellHandle, false);
	if (OS.XtIsTopLevelShell (shellHandle)) {
		OS.XtUnmapWidget (shellHandle);
	} else {
		OS.XtPopdown (shellHandle);
	}

	/* If the shell is iconified, hide the icon */
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) return;
	OS.XWithdrawWindow (xDisplay, xWindow, OS.XDefaultScreen (xDisplay));

	sendEvent (SWT.Hide);
}
int topHandle () {
	return shellHandle;
}
int trimHeight () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.titleResizeTrimHeight;
		if (hasBorder) return display.titleBorderTrimHeight;
		return display.titleTrimHeight;
	}
	if (hasResize) return display.resizeTrimHeight;
	if (hasBorder) return display.borderTrimHeight;
	return 0;
}
int trimWidth () {
	if ((style & SWT.NO_TRIM) != 0) return 0;
	boolean hasTitle = false, hasResize = false, hasBorder = false;
	hasTitle = (style & (SWT.MIN | SWT.MAX | SWT.TITLE | SWT.MENU)) != 0;
	hasResize = (style & SWT.RESIZE) != 0;
	hasBorder = (style & SWT.BORDER) != 0;
	if (hasTitle) {
		if (hasResize) return display.titleResizeTrimWidth;
		if (hasBorder) return display.titleBorderTrimWidth;
		return display.titleTrimWidth;
	}
	if (hasResize) return display.resizeTrimWidth;
	if (hasBorder) return display.borderTrimWidth;
	return 0;
}
}

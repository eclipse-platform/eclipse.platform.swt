package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent the "windows"
 * which the desktop or "window manager" is managing.
 * Instances which do not have a parent (that is, they
 * are built using the constructor which takes a 
 * <code>Display</code> as the argument) are described
 * as <em>top level</em> shells. Instances which do have
 * a parent, are described as <em>secondary</em> or
 * <em>dialog</em> shells.
 * <p>
 * Instances are always displayed in one of the maximized, 
 * minimized or normal states:
 * <ul>
 * <li>
 * When an instance is marked as <em>maximized</em>, the
 * window manager will typically resize it to fill the
 * entire visible area of the display, and the instance
 * is usually put in a state where it can not be resized 
 * (even if it has style <code>RESIZE</code>) until it is
 * no longer maximized.
 * </li><li>
 * When an instance is in the <em>normal</em> state (neither
 * maximized or minimized), its appearance is controlled by
 * the style constants which were specified when it was created
 * and the restrictions of the window manager (see below).
 * </li><li>
 * When an instance has been marked as <em>minimized</em>,
 * its contents (client area) will usually not be visible,
 * and depending on the window manager, it may be
 * "iconified" (that is, replaced on the desktop by a small
 * simplified representation of itself), relocated to a
 * distinguished area of the screen, or hidden. Combinations
 * of these changes are also possible.
 * </li>
 * </ul>
 * </p>
 * Note: The styles supported by this class must be treated
 * as <em>HINT</em>s, since the window manager for the
 * desktop on which the instance is visible has ultimate
 * control over the appearance and behavior of decorations.
 * For example, some window managers only support resizable
 * windows and will always assume the RESIZE style, even if
 * it is not set.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_TRIM, RESIZE, TITLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Activate, Close, Deactivate, Deiconify, Iconify</dd>
 * </dl>
 * Class <code>SWT</code> provides two "convenience constants"
 * for the most commonly required style combinations:
 * <dl>
 * <dt><code>SHELL_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application top level shell: (that 
 * is, <code>CLOSE | TITLE | MIN | MAX | RESIZE</code>)
 * </dd>
 * <dt><code>DIALOG_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application dialog shell: (that 
 * is, <code>TITLE | CLOSE | BORDER</code>)
 * </dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is not intended to be subclassed.
 * </p>
 *
 * @see Decorations
 * @see SWT
 */

public class Shell extends Decorations {
	Display display;
	int shellHandle, vboxHandle;
	int modal;
	int accelGroup;
	Rectangle lastClientArea;
	boolean hasFocus;

/*
 *   ===  CONSTRUCTORS  ===
 */

/**
 * Constructs a new instance of this class. This is equivalent
 * to calling <code>Shell((Display) null)</code>.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell () {
	this ((Display) null);
}
/**
 * Constructs a new instance of this class given only the style
 * value describing its behavior and appearance. This is equivalent
 * to calling <code>Shell((Display) null, style)</code>.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell (int style) {
	this ((Display) null, style);
}

/**
 * Constructs a new instance of this class given only the display
 * to create it on. It is created with style <code>SWT.SHELL_TRIM</code>.
 * <p>
 * Note: Currently, null can be passed in for the display argument.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the display argument is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param display the display to create the shell on
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell (Display display) {
	this (display, SWT.SHELL_TRIM);
}
/**
 * Constructs a new instance of this class given the display
 * to create it on and a style value describing its behavior
 * and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p><p>
 * Note: Currently, null can be passed in for the display argument.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the display argument is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param display the display to create the shell on
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell (Display display, int style) {
	this (display, null, style);
}
Shell (Display display, Shell parent, int style) {
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
 * Constructs a new instance of this class given only its
 * parent. It is created with style <code>SWT.DIALOG_TRIM</code>.
 * <p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell (Shell parent) {
	this (parent, SWT.TITLE | SWT.CLOSE | SWT.BORDER);
}
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p><p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the shell on the currently active
 * display if there is one. If there is no current display, the 
 * shell is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of control to construct
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public Shell (Shell parent, int style) {
	this (null, parent, style);
}



/**
 * Adds the listener to the collection of listeners who will
 * be notified when operations are performed on the receiver,
 * by sending the listener one of the messages defined in the
 * <code>ShellListener</code> interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ShellListener
 * @see #removeShellListener
 */
public void addShellListener (ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Close,typedListener);
	addListener (SWT.Iconify,typedListener);
	addListener (SWT.Deiconify,typedListener);
	addListener (SWT.Activate, typedListener);
	addListener (SWT.Deactivate, typedListener);
}
void bringToTop () {
//	OS.gtk_window_activate_focus (shellHandle);
}
/**
 * Requests that the window manager close the receiver in
 * the same way it would be closed when the user clicks on
 * the "close box" or performs some other platform specific
 * key or mouse combination that indicates the window
 * should be removed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void close () {
	checkWidget ();
	closeWidget ();
}
void closeWidget () {
	Event event = new Event ();
	event.time = OS.gdk_time_get ();
	sendEvent (SWT.Close, event);
	if (event.doit && !isDisposed ()) dispose ();
}


/*
 *  ===  Handle code I: The createWidget() cycle.
 */

void createHandle (int index) {
	state |= HANDLE;
	shellHandle = OS.gtk_window_new((parent==null)? OS.GTK_WINDOW_TOPLEVEL:OS.GTK_WINDOW_DIALOG);
	if (shellHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (parent!=null) OS.gtk_window_set_transient_for(shellHandle, ((Shell)parent).shellHandle);
	
	vboxHandle = OS.gtk_vbox_new(false,0);
	if (vboxHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	eventBoxHandle = OS.gtk_event_box_new ();
	if (eventBoxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_drawing_area_new();
	if (handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	accelGroup = OS.gtk_accel_group_new ();
	OS.gtk_window_add_accel_group (shellHandle, accelGroup);
	OS.gtk_window_set_title (shellHandle, new byte [1]);
}

void configure () {
	OS.gtk_container_add (shellHandle, vboxHandle);
	OS.gtk_box_pack_end(vboxHandle, eventBoxHandle, true,true,0);
	OS.gtk_container_add (eventBoxHandle, fixedHandle);
	OS.gtk_fixed_put(fixedHandle, handle, (short)0,(short)0);
}

void showHandle() {
	OS.gtk_widget_realize (shellHandle);  // careful: NOT show
	_setStyle();
	
	OS.gtk_widget_realize (vboxHandle);
	OS.gtk_widget_show_now (vboxHandle);

	OS.gtk_widget_realize (eventBoxHandle);
	OS.gtk_widget_show_now (eventBoxHandle);
	
	OS.gtk_widget_realize  (fixedHandle);
	OS.gtk_widget_show_now (fixedHandle);

	OS.gtk_widget_realize (handle);
	OS.gtk_widget_show_now (handle);
}

void hookEvents () {
	super.hookEvents ();
	signal_connect(shellHandle, "map_event",     SWT.Deiconify, 3);
	signal_connect(shellHandle, "unmap_event",   SWT.Iconify, 3);
	signal_connect(shellHandle, "size_allocate", SWT.Resize, 3);
	signal_connect(shellHandle, "delete_event",  SWT.Dispose, 3);
}

void register () {
	super.register ();
	WidgetTable.put (shellHandle, this);
	WidgetTable.put (vboxHandle, this);
}

private void _setStyle() {
	boolean modal = (
	   ((style&SWT.PRIMARY_MODAL)     != 0) ||
	   ((style&SWT.APPLICATION_MODAL) != 0) ||
	   ((style&SWT.SYSTEM_MODAL)      != 0));
	OS.gtk_window_set_modal(shellHandle, modal);
	
	int decorations = 0;
	if ((style & SWT.NO_TRIM) == 0) {
		if ((style & SWT.MIN) != 0) decorations |= OS.GDK_DECOR_MINIMIZE;
		if ((style & SWT.MAX) != 0) decorations |= OS.GDK_DECOR_MAXIMIZE;
		if ((style & SWT.RESIZE) != 0) decorations |= OS.GDK_DECOR_RESIZEH;
		if ((style & SWT.BORDER) != 0) decorations |= OS.GDK_DECOR_BORDER;
		if ((style & SWT.MENU) != 0) decorations |= OS.GDK_DECOR_MENU;
		if ((style & SWT.TITLE) != 0) decorations |= OS.GDK_DECOR_TITLE;
		/*
		 * Under some Window Managers (Sawmill), in order
		 * to get any border at all from the window manager it is necessary
		 * to set GDK_DECOR_BORDER.  The fix is to force these bits when any
		 * kind of border is requested.
		 */
		if ((style & SWT.RESIZE) != 0) decorations |= OS.GDK_DECOR_BORDER;
	}
	GtkWidget widget = new GtkWidget();
	OS.memmove(widget, shellHandle, GtkWidget.sizeof);
	int w = widget.window;
	// PANIC - this must absolutely never happen, so it's not NO_HANDLES actually
	if (w == 0) error(SWT.ERROR_NO_HANDLES);
	OS.gdk_window_set_decorations(w, decorations);
}

void _connectChild (int h) {
	OS.gtk_fixed_put (fixedHandle, h, (short)0, (short)0);
}

int topHandle () {
	return shellHandle;
}

int parentingHandle() {
	return fixedHandle;
}

boolean isMyHandle(int h) {
	if (h == shellHandle)    return true;
	if (h == vboxHandle)     return true;
	if (h == eventBoxHandle) return true;
	if (h == fixedHandle)    return true;
	if (h == handle)         return true;
	return false;
}


/*
 *   ===  GEOMETRY  ===
 */

public Point _getLocation() {
	GtkWidget widget = new GtkWidget();
	OS.memmove (widget, shellHandle, GtkWidget.sizeof);
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_origin(widget.window, x,y);
	return new Point(x[0], y[0]);
}

public Rectangle _getClientArea () {
	Point clientSize = UtilFuncs.getSize(eventBoxHandle);
	return new Rectangle (0, 0, clientSize.x, clientSize.y);
}

boolean _setSize(int width, int height) {
	boolean differentExtent = UtilFuncs.setSize(shellHandle, width,height);
	Point clientSize = UtilFuncs.getSize(eventBoxHandle);
	UtilFuncs.setSize(fixedHandle, clientSize.x, clientSize.y);
	UtilFuncs.setSize(handle,      clientSize.x, clientSize.y);
	return differentExtent;
}

// Unreliable
boolean _setLocation (int x, int y) {
	OS.gtk_widget_set_uposition (shellHandle, x, y);
	return true;
}

void setInitialSize() {
	int width  = OS.gdk_screen_width () * 5 / 8;
	int height = OS.gdk_screen_height () * 5 / 8;
	_setSize(width, height);
	OS.gtk_window_set_policy (shellHandle, 1,1,0);
}

public Display getDisplay () {
	if (display == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return display;
}

/*
 * Return the control inside this shell that has the focus,
 * if there is one.  Return <code>null</code> if there is no
 * such control - e.g., this shell is not active, or it is active
 * but the user clicked in a no-entry widget (like Label).
 */
Control getFocusControl() {
	GtkWindow shell = new GtkWindow();
	OS.memmove(shell, shellHandle, GtkWindow.sizeof);
	int focusHandle = shell.focus_widget;
	if (focusHandle==0) return null;
	return (Control)this.getDisplay().findWidget(focusHandle);
}

/**
 * Returns the receiver's input method editor mode. This
 * will be the result of bitwise OR'ing together one or
 * more of the following constants defined in class
 * <code>SWT</code>:
 * <code>NONE</code>, <code>ROMAN</code>, <code>DBCS</code>, 
 * <code>PHONETIC</code>, <code>NATIVE</code>, <code>ALPHA</code>.
 *
 * @return the IME mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 */
public int getImeInputMode () {
	checkWidget();
	return SWT.NONE;
}

/**
* Get the modal state.
* <p>
* @return the modal state
*
* @exception SWTError(ERROR_ERROR_INVALID_PARENT)
*	when the parent is invalid
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*/	
public int getModal () {
	checkWidget();
	return modal;
}

Shell _getShell () {
	return this;
}
/**
 * Returns an array containing all shells which are 
 * descendents of the receiver.
 * <p>
 * @return the dialog shells
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Shell [] getShells () {
	checkWidget();
	int count = 0;
	Shell [] shells = display.getShells ();
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.getParent ();
		} while (shell != null && shell != this);
		if (shell == this) count++;
	}
	int index = 0;
	Shell [] result = new Shell [count];
	for (int i=0; i<shells.length; i++) {
		Control shell = shells [i];
		do {
			shell = shell.getParent ();
		} while (shell != null && shell != this);
		if (shell == this) {
			result [index++] = shells [i];
		}
	}
	return result;
}

public void layout (boolean changed) {
	checkWidget();
//	if (!resizedSinceLastLayout()) return;
	lastClientArea=getClientArea();
	if (layout == null) return;
	layout.layout (this, changed);
}

/*
 * Returns whether the shell has been resized since the last layout()
 */
boolean resizedSinceLastLayout() {
	return !getClientArea().equals(lastClientArea);
}

/**
 * Moves the receiver to the top of the drawing order for
 * the display on which it was created (so that all other
 * shells on that display, which are not the receiver's
 * children will be drawn behind it), marks it visible,
 * and sets focus to its default button (if it has one).
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Control#setVisible
 * @see Decorations#setDefaultButton
*/
public void open () {
	checkWidget();
	setVisible (true);
}

int processDispose (int int0, int int1, int int2) {
	closeWidget ();
	return 1;
}

int processFocusIn(int int0, int int1, int int2) {
	hasFocus=true;
	postEvent(SWT.Activate);
	return 0;
}

int processFocusOut(int int0, int int1, int int2) {
	hasFocus=false;
	postEvent(SWT.Deactivate);
	return 0;
}

int processPaint (int callData, int int2, int int3) {
	//if (!hooks (SWT.Paint)) return 1;
	
	GdkEventExpose gdkEvent = new GdkEventExpose ();
	OS.memmove (gdkEvent, callData, GdkEventExpose.sizeof);
	Event event = new Event ();
	event.count = gdkEvent.count;
	event.x = gdkEvent.x;  event.y = gdkEvent.y;
	event.width = gdkEvent.width;  event.height = gdkEvent.height;
	GC gc = event.gc = new GC (this);
	GdkRectangle rect = new GdkRectangle ();
	rect.x = gdkEvent.x;  rect.y = gdkEvent.y;
	rect.width = gdkEvent.width;  rect.height = gdkEvent.height;
	OS.gdk_gc_set_clip_rectangle (gc.handle, rect);
	gc.fillRectangle(rect.x, rect.y, rect.width, rect.height);
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	return 1;
/*}else{
	GdkRectangle gdkEvent = new GdkRectangle ();
	OS.memmove (gdkEvent, callData, GdkRectangle.sizeof);
	Event event = new Event ();
//	event.count = gdkEvent.count;
	event.x = gdkEvent.x;  event.y = gdkEvent.y;
	event.width = gdkEvent.width;  event.height = gdkEvent.height;
	GC gc = event.gc = new GC (this);
	OS.gdk_gc_set_clip_rectangle (gc.handle, gdkEvent);
	sendEvent (SWT.Paint, event);
	gc.dispose ();
	event.gc = null;
	return 1;
}	*/
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when operations are performed on the receiver.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ShellListener
 * @see #addShellListener
 */
public void removeShellListener (ShellListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Close, listener);
	eventTable.unhook (SWT.Iconify,listener);
	eventTable.unhook (SWT.Deiconify,listener);
	eventTable.unhook (SWT.Activate, listener);
	eventTable.unhook (SWT.Deactivate, listener);
}

/**
 * Sets the input method editor mode to the argument which 
 * should be the result of bitwise OR'ing together one or more
 * of the following constants defined in class <code>SWT</code>:
 * <code>NONE</code>, <code>ROMAN</code>, <code>DBCS</code>, 
 * <code>PHONETIC</code>, <code>NATIVE</code>, <code>ALPHA</code>.
 *
 * @param mode the new IME mode
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT
 */
public void setImeInputMode (int mode) {
	checkWidget();
}

public void setMaximized (boolean maximized) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
//	GtkWidget widget = new GtkWidget ();
//	OS.memmove (widget, shellHandle, GtkWidget.sizeof);
//	OS.gdk_window_set_functions (window, OS.GDK_FUNC_MAXIMIZE);
}
public void setMenuBar (Menu menu) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (menuBar == menu) return;
	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error (SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error (SWT.ERROR_INVALID_PARENT);
	}
	if (menu == null) {
		if (menuBar != null) {
			OS.gtk_object_ref (menuBar.handle);
			OS.gtk_container_remove (vboxHandle, menuBar.handle);
		}
	}
	menuBar = menu;
	if (menuBar != null) {
		int menuHandle = menu.handle;
		OS.gtk_box_pack_start (vboxHandle, menuHandle, false, false, 0);
	}
}

public void setMinimized (boolean minimized) {
	checkWidget();
	
	/*
	 * In GDK, there is no way to iconify a shell.
	 * If we wanted it really badly, on pure X this is done
	 * by sending a client message - see ICCCM L.4.1.4.
	 */
	if (minimized) return;
	
	/*
	 * At least we can force a deiconify
	 */
	GtkWidget w = new GtkWidget();
	OS.memmove(w, shellHandle, w.sizeof);
	OS.gdk_window_show(w.window);
}

/**
* Set the modal state.
* <p>
* @param modal the new modal state
*
* @exception SWTError(ERROR_ERROR_INVALID_PARENT)
*	when the parent is invalid
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*/	
public void setModal (int modal) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	this.modal = modal;
}

public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.gtk_window_set_title (shellHandle, buffer);
}
public void setVisible (boolean visible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (visible) {
		OS.gtk_widget_show_now (shellHandle);
		display.update();
		sendEvent (SWT.Show);
	} else {	
		OS.gtk_widget_hide (shellHandle);
		sendEvent (SWT.Hide);
	}
}


/*
 *   ===  DESTRUCTION  ===
 */

void deregister () {
	super.deregister ();
	WidgetTable.remove (shellHandle);
	WidgetTable.remove (vboxHandle);
}

void releaseHandle () {
	super.releaseHandle ();
	shellHandle = vboxHandle = 0;
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
	if (accelGroup != 0) OS.gtk_accel_group_unref (accelGroup);
	accelGroup = 0;
}
}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are controls which are capable
 * of containing other controls.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>NO_BACKGROUND, NO_FOCUS, NO_MERGE_PAINTS, NO_REDRAW_RESIZE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are constructed from aggregates
 * of other controls.
 * </p>
 *
 * @see Canvas
 */
public class Composite extends Scrollable {
	/* boxHandle is temporarily here, until eclipsefixed gets fixed */
	int boxHandle, fixedHandle, radioHandle;
	Layout layout;

/*
 *   ===  CONSTRUCTORS  ===
 */

Composite () {
	/* Do nothing */
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
 * </p>
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Composite (Composite parent, int style) {
	super (parent, style);
}

/*
 * === Handle code begins ===
 */
void createHandle (int index) {
	state |= HANDLE | CANVAS;
	
	scrolledHandle = OS.gtk_scrolled_window_new(0,0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	
	boxHandle = OS.gtk_event_box_new();
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	
	fixedHandle = OS.eclipse_fixed_new();
	if (fixedHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	
	handle = OS.gtk_drawing_area_new();
	if (handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.GTK_WIDGET_SET_FLAGS(handle, OS.GTK_CAN_FOCUS);
}

void configure() {
	parent._connectChild(scrolledHandle);
	OS.gtk_container_add(scrolledHandle, boxHandle);
	OS.gtk_container_add(boxHandle, fixedHandle);
	OS.gtk_container_add(fixedHandle, handle);
}

void setHandleStyle() {
	setScrollingPolicy();
}

void showHandle() {
	OS.gtk_widget_show_now (scrolledHandle);
	OS.gtk_widget_show_now (boxHandle);
	OS.gtk_widget_show_now (fixedHandle);
	OS.gtk_widget_realize (handle);
	OS.gtk_widget_show_now (handle);
}

void hookEvents () {
	signal_connect_after (handle, "expose_event", SWT.Paint, 3);
	int mask =
		OS.GDK_POINTER_MOTION_MASK | 
		OS.GDK_BUTTON_PRESS_MASK | OS.GDK_BUTTON_RELEASE_MASK | 
		OS.GDK_ENTER_NOTIFY_MASK | OS.GDK_LEAVE_NOTIFY_MASK | 
		OS.GDK_KEY_PRESS_MASK | OS.GDK_KEY_RELEASE_MASK |
		OS.GDK_FOCUS_CHANGE_MASK;
	OS.gtk_widget_add_events (handle, mask);
	signal_connect_after (handle, "motion_notify_event", SWT.MouseMove, 3);
	signal_connect_after (handle, "button_press_event", SWT.MouseDown, 3);
	signal_connect_after (handle, "button_release_event", SWT.MouseUp, 3);
	signal_connect_after (handle, "enter_notify_event", SWT.MouseEnter, 3);
	signal_connect_after (handle, "leave_notify_event", SWT.MouseExit, 3);
	signal_connect_after (handle, "key_press_event", SWT.KeyDown, 3);
	signal_connect_after (handle, "key_release_event", SWT.KeyUp, 3);
	signal_connect_after (handle, "focus_in_event", SWT.FocusIn, 3);
	signal_connect_after (handle, "focus_out_event", SWT.FocusOut, 3);
}

int topHandle() { return scrolledHandle; }
int parentingHandle() { return fixedHandle; }
boolean isMyHandle(int h) {
	if (h==boxHandle) return true;
	if (h==fixedHandle) return true;
	return super.isMyHandle(h);
}


/*
 *   ===  GEOMETRY - PHYSICAL  ===
 */


public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	layout();
}

public void setSize (int width, int height) {
	super.setSize(width, height);
	layout();
}
Point getScrollableTrim() {
	/*
	 * This is very tricky.
	 * In SWT/GTK, layout is not managed by the GtkContainers.
	 * Therefore, the native preferred minimum sizes are
	 * generally ignored.  This allows us to set the native
	 * size of the fixed to whatever value we want, and it is
	 * guaranteed to be disregarded by the layout code.
	 * At this point, that size is requested by the Scrollable,
	 * gets added to by whatever is between the scrollable
	 * and the fixed, and serves as the basis for the native
	 * Scrollable size requisition (which is also guaranteed
	 * to be thrown away).
	 */
	OS.GTK_WIDGET_SET_FLAGS(parentingHandle(), OS.GTK_VISIBLE);
	OS.GTK_WIDGET_SET_FLAGS(boxHandle, OS.GTK_VISIBLE);
	GtkRequisition clientReq = new GtkRequisition();
	GtkRequisition req = new GtkRequisition();
	OS.gtk_widget_size_request(parentingHandle(), clientReq);
	OS.gtk_widget_size_request(scrolledHandle, req);
	if ((style&SWT.H_SCROLL&SWT.V_SCROLL)!=0) return new Point (req.width-clientReq.width, req.height-clientReq.height);
	if ((style&SWT.H_SCROLL)!=0) return new Point (0, req.height-clientReq.height);
	if ((style&SWT.V_SCROLL)!=0) return new Point (req.width-clientReq.width, 0);
	return new Point (0,0);
}
void _setSize(int width, int height) {
	OS.eclipse_fixed_set_size(parent.parentingHandle(), topHandle(), width, height);
	/* This is the trim on the right caused by the scrollbars */
	Point st = getScrollableTrim();
	if ((style&SWT.V_SCROLL) != 0) width  -= st.x;  width  = Math.max(width, 0);
	if ((style&SWT.H_SCROLL) != 0) height -= st.y;  height = Math.max(height, 0);
	OS.eclipse_fixed_set_size(fixedHandle, handle, width, height);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size;
	if (layout != null) {
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			size = layout.computeSize (this, wHint, hHint, changed);
		} else {
			size = new Point (wHint, hHint);
		}
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}


/*
 *   ===  GEOMETRY - LAYOUT  ===
 */

/**
 * Returns layout which is associated with the receiver, or
 * null if one has not been set.
 *
 * @return the receiver's layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Layout getLayout () {
	checkWidget();
	return layout;
}

/**
 * Gets the last specified tabbing order for the control.
 *
 * @return tabList the ordered list of controls representing the tab order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setTabList
 */
public Control [] getTabList () {
	return new Control [0];
}

/**
 * Sets the layout which is associated with the receiver to be
 * the argument which may be null.
 *
 * @param layout the receiver's new layout or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLayout (Layout layout) {
	checkWidget();
	this.layout = layout;
}

/**
 * Returns an array containing the receiver's children.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of children, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return an array of children
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control [] getChildren () {
	checkWidget();
	return _getChildren(parentingHandle());
}

/**
 * Answer the array of the children of the specified handle,
 * filtering out widgets we don't consider our children.
 * That is, the OS may return some children that don't qualify
 * as such under SWT terminology - e.g., Items are not children.
 */
Control [] _getChildren (int h) {
	if (h==0) {
		error(SWT.ERROR_UNSPECIFIED);
	}
	int list = OS.gtk_container_children (h);
	int count = OS.g_list_length (list);
	java.util.Vector children = new java.util.Vector();
	for (int i=0; i<count; i++) {
		int data = OS.g_list_nth_data (list, i);
		if (!isMyHandle(data)) {
			Control child = _childFromHandle(data);
			if (child != null) children.addElement(child);
		}
	}
	Control[] answer = new Control[children.size()];
	children.copyInto(answer);
	return answer;
}
/**
 * Consider the argument a handle of one of the receiver's children.
 * If the argument is not a handle to a widget, or the widget is
 * not our child in SWT (not OS) terminology, return null.
 */
Control _childFromHandle(int h) {
	Widget child = WidgetTable.get(h);
	return (Control)child;
}

public Rectangle getClientArea () {
	checkWidget();
	/* We can not measure the actual size of the client area,
	 * because it may not have propagated down yet.
	 */
	Point size = _getSize();
	/* FIXME - this code assumes the scrollbars are to the right */
	/* FIXME - I just measured the size on one particular theme. */
	if ((style & SWT.V_SCROLL) != 0) size.x -= 18;
	if ((style & SWT.H_SCROLL) != 0) size.y -= 18;
	return new Rectangle(0,0, size.x, size.y);
}


/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the receiver does not have a layout, do nothing.
 * <p>
 * This is equivalent to calling <code>layout(true)</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout () {
	layout (true);
}
/**
 * If the receiver has a layout, asks the layout to <em>lay out</em>
 * (that is, set the size and location of) the receiver's children. 
 * If the the argument is <code>true</code> the layout must not rely
 * on any cached information it is keeping about the children. If it
 * is <code>false</code> the layout may (potentially) simplify the
 * work it is doing by assuming that the state of the none of the
 * receiver's children has changed since the last layout.
 * If the receiver does not have a layout, do nothing.
 *
 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void layout (boolean changed) {
	checkWidget();
	if (layout == null) return;
	layout.layout (this, changed);
}

Point minimumSize () {
	Control [] children = _getChildren (parentingHandle());
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = children [i].getBounds ();
		width = Math.max (width, rect.x + rect.width);
		height = Math.max (height, rect.y + rect.height);
	}
	return new Point (width, height);
}
int radioGroup() {
	if (radioHandle==0) _initializeRadioGroup();
	return OS.gtk_radio_button_group(radioHandle);
}

public void redraw () {
	checkWidget();
	OS.gtk_widget_queue_draw(paintHandle());
}

void _initializeRadioGroup() {
	radioHandle = OS.gtk_radio_button_new(0);
}

/**
 * Adopt the widget h as our child.
 */
void _connectChild (int h) {
	OS.gtk_container_add (parentingHandle(), h);
}

void releaseChildren () {
	Control [] children = _getChildren (parentingHandle());
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null && !child.isDisposed ()) {
			child.releaseWidget ();
			child.releaseHandle ();
		}
	}
}
void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	layout = null;
}
void releaseHandle () {
	super.releaseHandle ();
	boxHandle = radioHandle = 0;
}

int processMouseDown (int callData, int arg1, int int2) {
	//NOT DONE - only grab when not already grabbing
	if ((state & CANVAS) != 0) OS.gtk_grab_add (handle);
	return super.processMouseDown (callData, arg1, int2);
}

int processMouseUp (int callData, int arg1, int int2) {
	//NOT DONE - only release when last button goes up
	if ((state & CANVAS) != 0) OS.gtk_grab_remove (handle);
	return super.processMouseUp (callData, arg1, int2);
}

int processFocusIn(int int0, int int1, int int2) {
	OS.GTK_WIDGET_SET_FLAGS(handle, OS.GTK_HAS_FOCUS);
	return super.processFocusIn(int0, int1, int2);
}
int processFocusOut(int int0, int int1, int int2) {
	OS.GTK_WIDGET_UNSET_FLAGS(handle, OS.GTK_HAS_FOCUS);
	return super.processFocusOut(int0, int1, int2);
}

public boolean setFocus () {
	checkWidget();
	Control [] children = _getChildren (parentingHandle());
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.getVisible () && child.setFocus ()) return true;
	}
	return super.setFocus ();
}

/**
 * Sets the tabbing order for the specified controls to
 * match the order that they occur in the argument list.
 *
 * @param tabList the ordered list of controls representing the tab order; must not be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the tabList is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if a widget in the tabList is null or has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if widget in the tabList is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabList (Control [] tabList) {
}

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
}

}

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
	int radioHandle;
	Layout layout;

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

Control [] _getChildren () {
	int parentHandle = parentingHandle ();
	int list = OS.gtk_container_get_children (parentHandle);
	if (list == 0) return new Control [0];
	int count = OS.g_list_length (list);
	Control [] children = new Control [count];
	int i = 0, j = 0;
	while (i < count) {
		int handle = OS.g_list_nth_data (list, i);
		if (handle != 0) {
			Widget widget = WidgetTable.get (handle);
			if (widget != null && widget != this) {
				if (widget instanceof Control) {
					children [j++] = (Control) widget;
				}
			}
		}
		i++;
	}
	OS.g_list_free (list);
	if (i == j) return children;
	Control [] newChildren = new Control [j];
	System.arraycopy (children, 0, newChildren, 0, j);
	return newChildren;
}

protected void checkSubclass () {
	/* Do nothing - Subclassing is allowed */
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

void createHandle (int index) {
	state |= HANDLE | CANVAS;
	createScrolledHandle (parent.parentingHandle ());
}

void createScrolledHandle (int parentHandle) {
	//TEMPORARY CODE
//	boolean isScrolled = (style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0;
	boolean isScrolled = true;
	if (isScrolled) {
		fixedHandle = OS.gtk_fixed_new ();
		if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_fixed_set_has_window (fixedHandle, true);
		int vadj = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 10);
		if (vadj == 0) error (SWT.ERROR_NO_HANDLES);
		int hadj = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 10);
		if (hadj == 0) error (SWT.ERROR_NO_HANDLES);
		scrolledHandle = OS.gtk_scrolled_window_new (hadj, vadj);
		if (scrolledHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	}
	handle = OS.gtk_fixed_new ();
	if (handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (handle, true);
	OS.GTK_WIDGET_SET_FLAGS(handle, OS.GTK_CAN_FOCUS);
	if (isScrolled) {
		OS.gtk_container_add (parentHandle, fixedHandle);
		OS.gtk_container_add (fixedHandle, scrolledHandle);
		/*
		* Force the scrolledWindow to have a single child that is
		* not scrolled automatically.  Calling gtk_container_add
		* seems to add the child correctly but cause a warning.
		*/
		Display display = getDisplay ();
		boolean warnings = display.getWarnings ();
		display.setWarnings (false);
		OS.gtk_container_add (scrolledHandle, handle);
		display.setWarnings (warnings);
		
		OS.gtk_widget_show (fixedHandle);
		OS.gtk_widget_show (scrolledHandle);
		int hsp = (style & SWT.H_SCROLL) == 0 ? OS.GTK_POLICY_NEVER : OS.GTK_POLICY_ALWAYS;
		int vsp = (style & SWT.V_SCROLL) == 0 ? OS.GTK_POLICY_NEVER : OS.GTK_POLICY_ALWAYS;
		OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
		//CHECK WIDTH IS THERE ALREADY THEN DON'T SET
		if ((style & SWT.BORDER) != 0) {
			OS.gtk_scrolled_window_set_shadow_type(scrolledHandle, OS.GTK_SHADOW_ETCHED_IN);
		}
	} else {
		OS.gtk_container_add (parentHandle, handle);		
	}
	OS.gtk_widget_show (handle);
	
	//DOESN'T WORK RIGHT NOW
	if ((style & SWT.NO_REDRAW_RESIZE) != 0) {
		OS.gtk_widget_set_redraw_on_allocate (handle, false);
	}
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
	return _getChildren ();
}

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

void hookEvents () {
	super.hookEvents ();
	if ((state & CANVAS) != 0) {
		OS.gtk_widget_add_events (handle, OS.GDK_POINTER_MOTION_HINT_MASK);
	}
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
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Rectangle rect = children [i].getBounds ();
		width = Math.max (width, rect.x + rect.width);
		height = Math.max (height, rect.y + rect.height);
	}
	return new Point (width, height);
}

int parentingHandle () {
	if ((state & CANVAS) != 0) return handle;
	return fixedHandle != 0 ? fixedHandle : handle;
}

int processFocusIn(int int0, int int1, int int2) {
	return super.processFocusIn(int0, int1, int2);
}

int processFocusOut(int int0, int int1, int int2) {
	return super.processFocusOut(int0, int1, int2);
}

int processMouseDown (int callData, int arg1, int int2) {
	int result = super.processMouseDown (callData, arg1, int2);
	if ((state & CANVAS) != 0) {
		//NOT DONE - only grab when not already grabbing
		OS.gtk_grab_add (handle);
		if ((style & SWT.NO_FOCUS) == 0) {
			int list = OS.gtk_container_get_children (handle);
			if (list != 0) {
				int count = OS.g_list_length (list);
				if (count == 0) OS.gtk_widget_grab_focus (handle);
				OS.g_list_free (list);
			}
		}
	}
	return result;
}

int processMouseUp (int callData, int arg1, int int2) {
	int result = super.processMouseUp (callData, arg1, int2);
	//NOT DONE - only release when last button goes up
	if ((state & CANVAS) != 0) {
		OS.gtk_grab_remove (handle);
	}
	return result;
}

int radioGroup() {
	if (radioHandle == 0) {
		radioHandle = OS.gtk_radio_button_new (0);
		if (radioHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	}
	return OS.gtk_radio_button_group (radioHandle);
}

void releaseChildren () {
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child != null && !child.isDisposed ()) {
			child.releaseWidget ();
			child.releaseHandle ();
		}
	}
}

void releaseHandle () {
	super.releaseHandle ();
	radioHandle = 0;
}

void releaseWidget () {
	releaseChildren ();
	super.releaseWidget ();
	layout = null;
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize && layout != null) layout.layout (this, false);
	return changed;
}

public boolean setFocus () {
	checkWidget();
	Control [] children = _getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (child.getVisible () && child.setFocus ()) return true;
	}
	return super.setFocus ();
}

void setInitialSize () {
	if (scrolledHandle != 0) {
		super.setInitialSize ();
		return;
	}
	/*
	* Bug in GTK. The scrollbars are not visible when a scrolled window
	* is resize and then shown. The fix is to change the scrolling policy
	* before and after resizing.
	*/
	if ((state & CANVAS) != 0) {
		OS.gtk_scrolled_window_set_policy (scrolledHandle, OS.GTK_POLICY_NEVER, OS.GTK_POLICY_NEVER);
	}
	super.setInitialSize ();
	if ((state & CANVAS) != 0) {
		int hsp = (style & SWT.H_SCROLL) == 0 ? OS.GTK_POLICY_NEVER : OS.GTK_POLICY_ALWAYS;
		int vsp = (style & SWT.V_SCROLL) == 0 ? OS.GTK_POLICY_NEVER : OS.GTK_POLICY_ALWAYS;
		OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
	}
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

int traversalCode(int key, int event) {
	return SWT.TRAVERSE_ESCAPE;
}

}

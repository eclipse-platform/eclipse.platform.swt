package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that represent the dynamically positionable
 * areas of a <code>CoolBar</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class CoolItem extends Item {
	CoolBar parent;
	Control control;
	int id;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CoolBar</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
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
public CoolItem (CoolBar parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CoolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
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
public CoolItem (CoolBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners that will
 * be notified when the control is selected, by sending it one
 * of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * If <code>widgetSelected</code> is called when the mouse is over
 * the drop-down arrow (or 'chevron') portion of the cool item,
 * the event object detail field contains the value <code>SWT.ARROW</code>,
 * and the x and y fields in the event object represent the point at
 * the bottom left of the chevron, where the menu should be popped up.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
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
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 * 
 * @since 2.0
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Returns the preferred size of the receiver.
 * <p>
 * The <em>prefered size</em> of a <code>CoolItem</code> is the size that
 * it would best be displayed at. The width hint and height hint arguments
 * allow the caller to ask the instance questions such as "Given a particular
 * width, how high does it need to be to show all of the contents?"
 * To indicate that the caller does not wish to constrain a particular 
 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint. 
 * </p>
 *
 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
 * @return the preferred size
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Layout
 */
public Point computeSize (int wHint, int hHint) {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return new Point (0, 0);
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 32;
	if (hHint == SWT.DEFAULT) height = 32;
	int hwnd = parent.handle;
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETBANDBORDERS, index, rect);
	width += rect.left + rect.right;
	return new Point (width, height);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return new Rectangle (0, 0, 0, 0);
	int hwnd = parent.handle;
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETRECT, index, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

/*
* Not currently used.
*/
Rectangle getClientArea () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return new Rectangle (0, 0, 0, 0);
	int hwnd = parent.handle;
	RECT insetRect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETBANDBORDERS, index, insetRect);
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETRECT, index, rect);
	int x = rect.left + insetRect.left;
	int y = rect.top + insetRect.top;
	int width = rect.right - rect.left - (insetRect.left + insetRect.right);
	int height = rect.bottom - rect.top - (insetRect.top + insetRect.bottom);
	if (index == 0) {
		REBARBANDINFO rbBand = new REBARBANDINFO ();
		rbBand.cbSize = REBARBANDINFO.sizeof;
		rbBand.fMask = OS.RBBIM_HEADERSIZE;
		OS.SendMessage (hwnd, OS.RB_GETBANDINFO, index, rbBand);
		width = width - rbBand.cxHeader + 1;
	}
	return new Rectangle (x, y, width, height);
}

/**
 * Gets the control which is associated with the receiver.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget ();
	return control;
}

public Display getDisplay () {
	CoolBar parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

/**
 * Returns the receiver's parent, which must be a <code>CoolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CoolBar getParent () {
	checkWidget ();
	return parent;
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	control = null;
	parent = null;
}

/**
 * Sets the control which is associated with the receiver
 * to the argument.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget ();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	int index = parent.indexOf (this);
	if (index == -1) return;
	Control newControl = control;
	Control oldControl = this.control;
	int hwnd = parent.handle;
	int hwndChild = 0;
	if (newControl != null) hwndChild = control.handle;
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_CHILD;
	rbBand.hwndChild = hwndChild;
	this.control = newControl;

	/*
	* Feature in Windows.  When Windows sets the rebar band child,
	* it makes the new child visible and hides the old child.  The
	* fix is to save and restore the visibility of the controls.
	*/		
	boolean hideNew = newControl != null && !newControl.getVisible ();
	boolean showOld = oldControl != null && oldControl.getVisible ();
	OS.SendMessage (hwnd, OS.RB_SETBANDINFO, index, rbBand);
	if (hideNew) newControl.setVisible (false);
	if (showOld) oldControl.setVisible (true);
}

public Point getPreferredSize () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return new Point (0, 0);
	int hwnd = parent.handle;
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_CHILDSIZE | OS.RBBIM_IDEALSIZE;
	OS.SendMessage (hwnd, OS.RB_GETBANDINFO, index, rbBand);
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETBANDBORDERS, index, rect);
	int width = rbBand.cxIdeal + rect.left + rect.right;
	return new Point (width, rbBand.cyMinChild);
}

public void setPreferredSize (int width, int height) {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return;
	int hwnd = parent.handle;
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETBANDBORDERS, index, rect);
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	
	/* Get the child size fields first so we don't overwrite them. */
	rbBand.fMask = OS.RBBIM_CHILDSIZE;
	OS.SendMessage (hwnd, OS.RB_GETBANDINFO, index, rbBand);
	
	/* Set the size fields we are currently modifying. */
	rbBand.fMask = OS.RBBIM_CHILDSIZE | OS.RBBIM_IDEALSIZE;
	rbBand.cxIdeal = width - rect.left - rect.right;
	rbBand.cyMinChild = rbBand.cyMaxChild = height;
	OS.SendMessage (hwnd, OS.RB_SETBANDINFO, index, rbBand);
}

public void setPreferredSize (Point size) {
	checkWidget ();
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setPreferredSize (size.x, size.y);
}

public Point getSize() {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) new Point (0, 0);
	int hwnd = parent.handle;
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETRECT, index, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Point (width, height);
}

public void setSize (int width, int height) {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return;
	int hwnd = parent.handle;
	RECT rect = new RECT ();
	OS.SendMessage (hwnd, OS.RB_GETBANDBORDERS, index, rect);
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	
	/* Get the child size fields first so we don't overwrite them. */
	rbBand.fMask = OS.RBBIM_CHILDSIZE;
	OS.SendMessage (hwnd, OS.RB_GETBANDINFO, index, rbBand);
	
	/* Set the size fields we are currently modifying. */
	rbBand.fMask = OS.RBBIM_CHILDSIZE | OS.RBBIM_SIZE | OS.RBBIM_IDEALSIZE;
	rbBand.cx = width;
	rbBand.cxIdeal = width - rect.left - rect.right;
	rbBand.cyChild = rbBand.cyMinChild = rbBand.cyMaxChild = height;
	OS.SendMessage (hwnd, OS.RB_SETBANDINFO, index, rbBand);
}

public void setSize (Point size) {
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}

/**
 * Returns the minimum width that the cool item can
 * be resized to using the cool item's gripper.
 * 
 * @return the minimum width of the cool item, in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public int getMinimumWidth () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return 0;
	int hwnd = parent.handle;
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_CHILDSIZE;
	OS.SendMessage (hwnd, OS.RB_GETBANDINFO, index, rbBand);
	return rbBand.cxMinChild;
}

/**
 * Sets the minimum width that the cool item can
 * be resized to using the cool item's gripper.
 * 
 * @param width the minimum width of the cool item, in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setMinimumWidth (int width) {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return;
	int hwnd = parent.handle;
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	
	/* Get the child size fields first so we don't overwrite them. */
	rbBand.fMask = OS.RBBIM_CHILDSIZE;
	OS.SendMessage (hwnd, OS.RB_GETBANDINFO, index, rbBand);
	
	/* Set the size fields we are currently modifying. */
	rbBand.cxMinChild = width;
	OS.SendMessage (hwnd, OS.RB_SETBANDINFO, index, rbBand);
}

boolean getWrap() {
	int index = parent.indexOf (this);
	int hwnd = parent.handle;
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_STYLE;
	OS.SendMessage (hwnd, OS.RB_GETBANDINFO, index, rbBand);
	return (rbBand.fStyle & OS.RBBS_BREAK) != 0;
}

void setWrap(boolean wrap) {
	int index = parent.indexOf (this);
	int hwnd = parent.handle;
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_STYLE;
	if (wrap) {
		rbBand.fStyle = OS.RBBS_VARIABLEHEIGHT | OS.RBBS_GRIPPERALWAYS | OS.RBBS_BREAK;
	} else {
		rbBand.fStyle = OS.RBBS_VARIABLEHEIGHT | OS.RBBS_GRIPPERALWAYS;
	}
	OS.SendMessage (hwnd, OS.RB_SETBANDINFO, index, rbBand);
}

/**
 * Removes the listener from the collection of listeners that
 * will be notified when the control is selected.
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
 * @see SelectionListener
 * @see #addSelectionListener
 * 
 * @since 2.0
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.motif.*;
 
/**
 * Instances of this class are selectable user interface
 * objects that represent the dynamically positionable
 * areas of a <code>CoolBar</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public /*final*/ class CoolItem extends Item {
	Control control;
	CoolBar parent;
	boolean dragging;
	int mouseXOffset;
	int preferredWidth = -1;
	int id;
	
	static final int MARGIN_WIDTH = 4;
	static final int MARGIN_HEIGHT = 2;
	static final int MINIMUM_WIDTH = 11;
	static final int MINIMUM_CHILD_HEIGHT = 22;
		
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
	super(parent, style);
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
	super(parent, style);
	this.parent = parent;
	parent.createItem (this, index);
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
	Point size;
	if (control != null) {
		size = control.computeSize (wHint, hHint);
	}
	else {
		size = new Point (0,0);	
	}
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	int width = size.x + MINIMUM_WIDTH + MARGIN_WIDTH;
	int height = Math.max (MINIMUM_CHILD_HEIGHT, size.y) + MARGIN_HEIGHT;
	return new Point (width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	int [] argList = {
		OS.XmNancestorSensitive, 1,
		OS.XmNwidth, MINIMUM_WIDTH,
		OS.XmNheight, DEFAULT_HEIGHT,
		OS.XmNpositionIndex, index,
		OS.XmNtraversalOn, 0,
	};
	int parentHandle = parent.handle;
	handle = OS.XmCreateDrawingArea(parentHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int pixel = parent.getBackgroundPixel ();
	setBackgroundPixel (pixel);
}
public void dispose () {
	if (isDisposed()) return;
	CoolBar parent = this.parent;
	super.dispose ();
	parent.relayout ();
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
	checkWidget();
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Rectangle ((short) argList [1], (short) argList [3], argList [5], argList [7]);
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
	checkWidget();
	return control;
}
public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
Rectangle getGrabberArea () {
	int [] argList = {OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Rectangle(0, 0, MINIMUM_WIDTH, argList[1]);	
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
	checkWidget();
	return parent;
}
public Point getSize () {
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Point (argList [1], argList [3]);
}
void hookEvents () {
	int windowProc = getDisplay ().windowProc;
	OS.XtAddEventHandler (handle, OS.ExposureMask, false, windowProc, SWT.Paint);
	OS.XtAddEventHandler (handle, OS.ButtonPressMask, false, windowProc, SWT.MouseDown);
	OS.XtAddEventHandler (handle, OS.ButtonReleaseMask, false, windowProc, SWT.MouseUp);
	OS.XtAddEventHandler (handle, OS.PointerMotionMask, false, windowProc, SWT.MouseMove);
	OS.XtAddEventHandler (handle, OS.LeaveWindowMask, false, windowProc, SWT.MouseExit);
}
void manageChildren () {
	OS.XtManageChild (handle);
}
int processMouseDown (int callData) {
	Shell shell = parent.getShell();
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	if (getGrabberArea().contains(xEvent.x, xEvent.y)) {
		dragging = true;
		mouseXOffset = xEvent.x;
		parent.setCursor(parent.dragCursor);
	}
	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/	
	if (!shell.isDisposed()) {
		shell.setActiveControl(parent);
	}
	return 0;
}
int processMouseExit (int callData) {
	if (!dragging) parent.setCursor(null);
	return 0;
}
int processMouseMove (int callData) {
	XMotionEvent xEvent = new XMotionEvent ();
	OS.memmove (xEvent, callData, XMotionEvent.sizeof);
	if (dragging) {
		int [] argList = {OS.XmNheight, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);		
		int left_root = Math.max(0, xEvent.x_root - mouseXOffset);
		if (xEvent.y < 0) {
			parent.moveUp(this, left_root);				
			return 0;
		} 
		if (xEvent.y > argList[1]){
			parent.moveDown(this, left_root);
			return 0;
		}		
		int delta = Math.abs(xEvent.x - mouseXOffset);
		if (xEvent.x < mouseXOffset) {
			parent.moveLeft(this, delta);
			return 0;
		}
		if (xEvent.x > mouseXOffset) {
			parent.moveRight(this, delta);
			return 0;
		}
		return 0;
	}
	if (getGrabberArea().contains(xEvent.x, xEvent.y)) {
		parent.setCursor(parent.hoverCursor);
	} else {
		parent.setCursor(null);
	}
	return 0;
}
int processMouseUp (int callData) {
	dragging = false;
	parent.setCursor(parent.hoverCursor);
	return 0;
}
int processPaint (int callData) {
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return 0;	
	int xGC = OS.XCreateGC (xDisplay, xWindow, 0, null);

	Display display = getDisplay();
	int shadowPixel = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).handle.pixel;
	int highlightPixel = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW).handle.pixel;
	int lightShadowPixel = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW).handle.pixel;	
	
	int[] argList = {OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int height = argList[1];
	int grabberHeight = height - (2 * MARGIN_HEIGHT);

	/* Draw separator. */
	OS.XSetForeground(xDisplay, xGC, shadowPixel);
	OS.XDrawLine (xDisplay, xWindow, xGC, 0, 0, 0, height);
	OS.XSetForeground(xDisplay, xGC, highlightPixel);
	OS.XDrawLine (xDisplay, xWindow, xGC, 1, 0, 1, height);

	/* Draw grabber. */
	OS.XSetForeground(xDisplay, xGC, highlightPixel);
	OS.XSetBackground(xDisplay, xGC, lightShadowPixel);
	OS.XFillRectangle(xDisplay, xWindow, xGC, 4, MARGIN_HEIGHT, 3, grabberHeight);
	OS.XSetForeground(xDisplay, xGC, shadowPixel);
	int grabberBottom = MARGIN_HEIGHT + grabberHeight - 1;
	OS.XDrawLine (xDisplay, xWindow, xGC, 6, MARGIN_HEIGHT, 6, grabberBottom);
	OS.XDrawLine (xDisplay, xWindow, xGC, 4, grabberBottom, 6, grabberBottom);		
	
	OS.XFreeGC(xDisplay, xGC);
	return 0;
}
void propagateWidget (boolean enabled) {
	propagateHandle (enabled, handle);
	/*
	* CoolItems never participate in focus traversal when
	* either enabled or disabled.
	*/
	if (enabled) {
		int [] argList = {OS.XmNtraversalOn, 0};
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
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
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	Control oldControl = this.control;
	if (oldControl != null) oldControl.setVisible(false);
	this.control = control;
	if (control != null && !control.isDisposed ()) {
		int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		Point controlSize = control.getSize ();
		control.setBounds (
			((short) argList [1]) + MINIMUM_WIDTH, 
			((short) argList[3]) + MARGIN_HEIGHT, 
			controlSize.x, 
			controlSize.y);
		if (OS.XtIsRealized (handle)) {
			int window = OS.XtWindow (handle);
			if (window == 0) return;
			int display = OS.XtDisplay (handle);
			if (display == 0) return;
			OS.XLowerWindow (display, window);
		}
		control.setVisible(true);
	}
	Point size = computeSize(SWT.DEFAULT, SWT.DEFAULT);
	setSize(size);
	setPreferredSize(size);
}
public void setSize (int width, int height) {
	checkWidget();
	width = Math.max (width, MINIMUM_WIDTH);
	height = Math.max (height, MINIMUM_CHILD_HEIGHT + MARGIN_HEIGHT);
	OS.XtResizeWidget (handle, width, height, 0);
	if (control != null) {
		/*
		 * Feature in Motif.  Motif will not allow a window
		 * to have a zero width or zero height.  The fix is
		 * to ensure these values are never zero.
		 */
		int controlWidth = Math.max (1, width - MINIMUM_WIDTH - MARGIN_WIDTH);
		int controlHeight = Math.max (MINIMUM_CHILD_HEIGHT, height - MARGIN_HEIGHT);
		control.setSize (controlWidth, controlHeight);
	}
	parent.relayout();
}
void redraw() {
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	int window = OS.XtWindow (handle);
	if (window == 0) return;
	OS.XClearArea (display, window, 0, 0, 0, 0, true);
}
void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	control = null;
}
void setLocation (int x, int y) {
	OS.XtMoveWidget (handle, x, y);
	if (control != null) {
		int [] argList = {OS.XmNheight, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		control.setLocation(x + MINIMUM_WIDTH, y + MARGIN_HEIGHT);
	}
}
public void setSize (Point size) {
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}
void setBackgroundPixel(int pixel) {
	int [] argList = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmChangeColor (handle, pixel);
	OS.XtSetValues (handle, argList, argList.length / 2);
}
void setBounds (int x, int y, int width, int height) {
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	int newWidth = Math.max (width, MINIMUM_WIDTH);
	int newHeight = Math.max (height, MINIMUM_CHILD_HEIGHT + MARGIN_HEIGHT);
	OS.XtConfigureWidget (handle, x, y, newWidth, newHeight, 0);
	if (control != null) {
		int controlWidth = Math.max (1, newWidth - MINIMUM_WIDTH - MARGIN_WIDTH);
		int controlHeight = Math.max (MINIMUM_CHILD_HEIGHT, height - MARGIN_HEIGHT);
		control.setBounds (x + MINIMUM_WIDTH, y + MARGIN_HEIGHT, controlWidth, controlHeight);
	}
}
public Point getPreferredSize () {
	checkWidget();
	int[] argList = new int[] { OS.XmNheight, 0 };
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Point (preferredWidth, argList[1]);
}
public void setPreferredSize (int width, int height) {
	checkWidget();
	preferredWidth = width;
	int newHeight = Math.max (height, MINIMUM_CHILD_HEIGHT + MARGIN_HEIGHT);
	int[] argList = new int[] { OS.XmNwidth, 0 };
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XtResizeWidget (handle, argList[1], newHeight, 0);
	if (control != null) {
		control.setSize(control.getSize().x, newHeight - MARGIN_HEIGHT);
	}
}
public void setPreferredSize (Point size) {
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setPreferredSize(size.x, size.y);
}
	
}

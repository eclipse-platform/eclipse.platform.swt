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
	int preferredHeight = -1;
	int id;
	
	static final int MARGIN_WIDTH = 4;
	static final int MARGIN_HEIGHT = 2;
	static final int DEFAULT_HEIGHT = (2 * MARGIN_HEIGHT) + 28;
	static final int GRABBER_WIDTH = 2;
	static final int MINIMUM_WIDTH = (2 * MARGIN_WIDTH) + GRABBER_WIDTH;
		
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CoolBar</code> and a style value
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
 * (which must be a <code>CoolBar</code>, a style value
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (preferredWidth > -1) return new Point(preferredWidth, preferredHeight);
	int width = MINIMUM_WIDTH;
	int height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint + MINIMUM_WIDTH + MARGIN_WIDTH; 
	if (hHint != SWT.DEFAULT) height = hHint + (2 + MARGIN_HEIGHT);
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
}
public void dispose () {
	if (!isValidWidget ()) return;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}
Point getSize () {
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
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, callData, XButtonEvent.sizeof);
	if (getGrabberArea().contains(xEvent.x, xEvent.y)) {
		dragging = true;
		mouseXOffset = xEvent.x;
		parent.setCursor(parent.dragCursor);
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
		int left_root = xEvent.x_root - mouseXOffset;
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
	OS.XFillRectangle(xDisplay, xWindow, xGC, MARGIN_WIDTH, MARGIN_HEIGHT, GRABBER_WIDTH, grabberHeight);
	OS.XSetForeground(xDisplay, xGC, shadowPixel);
	int right = MARGIN_WIDTH + GRABBER_WIDTH;
	int bottom = MARGIN_HEIGHT + grabberHeight;
	OS.XDrawLine (xDisplay, xWindow, xGC, right, MARGIN_HEIGHT, right, bottom);
	OS.XDrawLine (xDisplay, xWindow, xGC, MARGIN_WIDTH, bottom, right, bottom);		
	
	OS.XFreeGC(xDisplay, xGC);
	return 0;
}
/**
 * Sets the control which is associated with the receiver
 * to the argument.
 *
 * @param control the new control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (control != null && control.parent != parent) {
		error (SWT.ERROR_INVALID_PARENT);
	}
	Control oldControl = this.control;
	if (oldControl != null) oldControl.setVisible(false);
	this.control = control;
	if (control != null && !control.isDisposed ()) {
		int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int controlY = ((short) argList[3]) + MARGIN_HEIGHT;
		control.setBounds (
			((short) argList [1]) + MINIMUM_WIDTH, 
			controlY, 
			argList [5] - MINIMUM_WIDTH - MARGIN_WIDTH, 
			argList [7] - (2 * MARGIN_HEIGHT));
			
		if (OS.XtIsRealized (handle)) {
			int window = OS.XtWindow (handle);
			if (window == 0) return;
			int display = OS.XtDisplay (handle);
			if (display == 0) return;
			OS.XLowerWindow (display, window);
		}
		control.setVisible(true);
	}
}
public void setSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	width = Math.max (width, MINIMUM_WIDTH);
	height = Math.max (height, DEFAULT_HEIGHT);
	OS.XtResizeWidget (handle, width, height, 0);			
	if (control != null) {
		int controlWidth = width - MINIMUM_WIDTH - MARGIN_WIDTH;
		int controlHeight = height - (2 * MARGIN_HEIGHT);
		control.setSize(controlWidth, controlHeight);
	}
	parent.relayout();
}
int getControlOffset(int height) {
	return ((height - control.getSize().y - (2 * MARGIN_HEIGHT)) / 2) + MARGIN_HEIGHT;
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
		int controlY = y + getControlOffset(argList[1]);
		control.setLocation(x + MINIMUM_WIDTH, controlY);
	}
}
public void setSize (Point size) {
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}
void setBounds (int x, int y, int width, int height) {
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	int newWidth = Math.max (width, 1), newHeight = Math.max (height, 1);
	OS.XtConfigureWidget (handle, x, y, newWidth, newHeight, 0);
	if (control != null) {
		int controlY = y + getControlOffset(newHeight);
		control.setBounds(
			x + MINIMUM_WIDTH, 
			controlY, 
			newWidth - MINIMUM_WIDTH - MARGIN_WIDTH, 
			control.getSize().y);
	}
}
public Point getPreferredSize () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return new Point(preferredWidth, preferredHeight);
}
public void setPreferredSize (int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	preferredWidth = Math.max (width, MINIMUM_WIDTH);
	preferredHeight = Math.max (height, DEFAULT_HEIGHT);
	OS.XtResizeWidget (handle, preferredWidth, preferredHeight, 0);
	if (control != null) {
		int controlWidth = preferredWidth - MINIMUM_WIDTH - MARGIN_WIDTH;
		int controlHeight = preferredHeight - (2 * MARGIN_HEIGHT);
		control.setSize(controlWidth, controlHeight);
	}
}
public void setPreferredSize (Point size) {
	setPreferredSize(size.x, size.y);
}
	
}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
 
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
public class CoolItem extends Item {
	Composite composite;
	Control control;
	CoolBar parent;
	boolean dragging;
	int mouseXOffset;
	int preferredWidth = 0;
	int requestedWidth = 0;
	int id;
	
	static final int MARGIN_WIDTH = 4;
	static final int MARGIN_HEIGHT = 2;
	static final int GRABBER_WIDTH = 2;
	static final int MINIMUM_WIDTH = (2 * MARGIN_WIDTH) + GRABBER_WIDTH;
		
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
	super(parent, 0);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount());
	createWidget();
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
	super(parent, 0);
	this.parent = parent;
	parent.createItem (this, index);
	createWidget();
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void createWidget () {
	composite = new Composite(parent, 0);
	Color color = parent.getBackground ();
	composite.setBackground (color);
		
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Paint:		processPaint(event.gc);		break;
				case SWT.MouseDown:	processMouseDown(event);	break;
				case SWT.MouseUp:	processMouseUp(event);		break;
				case SWT.MouseExit:	processMouseExit(event);	break;
				case SWT.MouseMove:	processMouseMove(event);	break;
			}
		}
	};
	int[] events = new int[]{
		SWT.Paint, 
		SWT.MouseDown, 
		SWT.MouseUp, 
		SWT.MouseExit, 
		SWT.MouseMove
	};
	for (int i = 0; i < events.length; i++) {
		composite.addListener(events[i], listener);
	}
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
	checkWidget();
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 32;
	if (hHint == SWT.DEFAULT) height = 32;
	width += MINIMUM_WIDTH + MARGIN_WIDTH;
	height += 2 * MARGIN_HEIGHT;
	return new Point (width, height);
}
public void dispose () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (isDisposed()) return;
	composite.dispose();
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
	return composite.getBounds();
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
	return new Rectangle(0, 0, MINIMUM_WIDTH, composite.getSize().y);	
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
	checkWidget();
	return composite.getSize();
}
void processMouseDown (Event event) {
	Shell shell = parent.getShell();
	if (getGrabberArea().contains(event.x, event.y)) {
		dragging = true;
		mouseXOffset = event.x;
		parent.setCursor(parent.dragCursor);
	}
}
void processMouseExit (Event event) {
	if (!dragging) parent.setCursor(null);
}
void processMouseMove (Event event) {
	if (dragging) {
		int height = composite.getSize().y;
		int left_root = composite.toDisplay(new Point(event.x, event.y)).x;
		if (event.y < 0) {
			parent.moveUp(this, left_root);				
			return;
		} 
		if (event.y > height){
			parent.moveDown(this, left_root);
			return;
		}		
		int delta = Math.abs(event.x - mouseXOffset);
		if (event.x < mouseXOffset) {
			parent.moveLeft(this, delta);
			return;
		}
		if (event.x > mouseXOffset) {
			parent.moveRight(this, delta);
			return;
		}
		return;
	}
	if (getGrabberArea().contains(event.x, event.y)) {
		parent.setCursor(parent.hoverCursor);
	} else {
		parent.setCursor(null);
	}
}
void processMouseUp (Event event) {
	if (dragging) {
		dragging = false;
		parent.setCursor(parent.hoverCursor);
	}
}
void processPaint (GC gc) {
	Display display = getDisplay();
	Color shadowColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color highlightColor = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	Color lightShadowColor = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	
	int height = composite.getSize().y;
	int grabberHeight = height - (2 * MARGIN_HEIGHT) - 1;

	/* Draw separator. */
	gc.setForeground(shadowColor);
	gc.drawLine(0, 0, 0, height);
	gc.setForeground(highlightColor);
	gc.drawLine(1, 0, 1, height);

	/* Draw grabber. */
	gc.setForeground(shadowColor);
	gc.drawRectangle(MARGIN_WIDTH, MARGIN_HEIGHT, GRABBER_WIDTH, grabberHeight);
	gc.setForeground(highlightColor);
	gc.drawLine(MARGIN_WIDTH, MARGIN_HEIGHT + 1, MARGIN_WIDTH, MARGIN_HEIGHT + grabberHeight - 1);
	gc.drawLine(MARGIN_WIDTH, MARGIN_HEIGHT, MARGIN_WIDTH + 1, MARGIN_HEIGHT);	
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
		Rectangle bounds = composite.getBounds();
		control.setBounds (
			bounds.x + MINIMUM_WIDTH, 
			bounds.y + MARGIN_HEIGHT, 
			bounds.width - MINIMUM_WIDTH - MARGIN_WIDTH, 
			bounds.height - (2 * MARGIN_HEIGHT));
			
		control.setVisible(true);
	}
}
public void setSize (int width, int height) {
	checkWidget();
	width = Math.max (width, MINIMUM_WIDTH);
	preferredWidth = requestedWidth = width;
	composite.setSize(width, height);		
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
void releaseWidget () {
	super.releaseWidget ();
	composite.dispose();
	parent = null;
	control = null;
}
public void setSize (Point size) {
	checkWidget();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}
void setBounds (int x, int y, int width, int height) {
	composite.setBounds(x, y, width, height);
	if (control != null) {
		control.setBounds(
			x + MINIMUM_WIDTH, 
			y + MARGIN_HEIGHT, 
			width - MINIMUM_WIDTH - MARGIN_WIDTH, 
			control.getSize().y);
	}
}
public Point getPreferredSize () {
	checkWidget();
	int height = composite.getSize().y;
	return new Point(preferredWidth, height + (2 * MARGIN_HEIGHT));
}
public void setPreferredSize (int width, int height) {
	checkWidget();
	preferredWidth = Math.max (width, MINIMUM_WIDTH);
	Rectangle bounds = composite.getBounds();
	setBounds(bounds.x, bounds.y, bounds.width, height);
}
public void setPreferredSize (Point size) {
	checkWidget();
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setPreferredSize(size.x, size.y);
}
	
}

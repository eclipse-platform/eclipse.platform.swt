package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
 
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
	Control control;
	CoolBar parent;
	int preferredWidth = -1, requestedWidth;
	Point minimumSize = new Point (MINIMUM_WIDTH, 2 * MARGIN_HEIGHT);
	Rectangle itemBounds = new Rectangle(0, 0, 0, 0);
	
	static final int MARGIN_WIDTH = 4;
	static final int MARGIN_HEIGHT = 2;
	static final int GRABBER_WIDTH = 2;
	
	private int CHEVRON_HORIZONTAL_TRIM = -1;			//platform dependent values
	private int CHEVRON_VERTICAL_TRIM = -1;	
	private static final int CHEVRON_LEFT_MARGIN = 2;
	private static final int CHEVRON_IMAGE_WIDTH = 8;	//Width to draw the double arrow
	
	static final int MINIMUM_WIDTH = (2 * MARGIN_WIDTH) + GRABBER_WIDTH;

	ToolBar chevron;
	Image arrowImage = null;
	
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
	this (parent, style, parent.getItemCount());
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
	calculatedBorders();
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

/* 
 * Find the trim size of the Toolbar widget in the current platform.
 */
void calculatedBorders() {
	ToolBar tb = new ToolBar (parent, SWT.FLAT);
	ToolItem ti = new ToolItem (tb, SWT.PUSH);
	Image image = new Image (getDisplay(), 1, 1);
	ti.setImage (image);
	Point size = tb.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	CHEVRON_HORIZONTAL_TRIM = size.x - 1;
	CHEVRON_VERTICAL_TRIM = size.y - 1;
	tb.dispose ();
	ti.dispose ();
	image.dispose ();
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
	if (isDisposed()) return;
	
	/*
	 * Must call parent.destroyItem() before super.dispose(), since it needs to
	 * query the bounds to properly remove the item.
	 */
	parent.destroyItem(this);
	super.dispose ();
	parent = null;
	control = null;
	
	/* 
	 * Although the parent for the chevron is the CoolBar (CoolItem can not be the parent)
	 * it has to be disposed with the item 
	 */
	if (chevron != null && !chevron.isDisposed()) chevron.dispose();
	chevron = null;
	if (arrowImage != null && !arrowImage.isDisposed()) arrowImage.dispose();
	arrowImage = null;
}

Image getArrowImage () {

	int height = Math.min (control.getSize ().y, itemBounds.height) - CHEVRON_VERTICAL_TRIM;
	if (arrowImage != null) {
		if (arrowImage.getBounds().height == height) {
			return arrowImage;
		} else {
			arrowImage.dispose();
			arrowImage = null;
		}
	}
	int width = CHEVRON_IMAGE_WIDTH; 
	Display display = getDisplay ();
	Color foreground = parent.getForeground ();
	Color black = display.getSystemColor (SWT.COLOR_BLACK);
	Color background = parent.getBackground ();
	
	PaletteData palette = new PaletteData (new RGB[]{foreground.getRGB(), background.getRGB(), black.getRGB()});
	ImageData imageData = new ImageData (width, height, 4, palette);
	imageData.transparentPixel = 1;
	arrowImage = new Image (display, imageData);
		
	GC gc = new GC (arrowImage);
	gc.setBackground (background);
	gc.fillRectangle (0, 0, width, height);
	gc.setForeground (black);
	
	int startX = 0 ;
	int startY = height / 6; 
	int step = 2;	
	gc.drawLine (startX, startY, startX + step, startY + step);
	gc.drawLine (startX, startY + (2 * step), startX + step, startY + step);
	startX++;
	gc.drawLine (startX, startY, startX + step, startY + step);
	gc.drawLine (startX, startY + (2 * step), startX + step, startY + step);
	startX += 3;
	gc.drawLine (startX, startY, startX + step, startY + step);
	gc.drawLine (startX, startY + (2 * step), startX + step, startY + step);
	startX++;
	gc.drawLine (startX, startY, startX + step, startY + step);
	gc.drawLine (startX, startY + (2 * step), startX + step, startY + step);
	gc.dispose ();
	return arrowImage;
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
	return new Rectangle(itemBounds.x, itemBounds.y, itemBounds.width, itemBounds.height);
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
/**
 * Returns the minimum size that the cool item can
 * be resized to using the cool item's gripper.
 * 
 * @return a point containing the minimum width and height of the cool item, in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public Point getMinimumSize () {
	checkWidget();
	return minimumSize;
}
/**
 * @deprecated use getMinimumSize
 */
public int getMinimumWidth () {
	return getMinimumSize().x;
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
public Point getPreferredSize () {
	checkWidget();
	int height = getSize().y;
	return new Point(preferredWidth, height + (2 * MARGIN_HEIGHT));
}
public Point getSize () {
	checkWidget();
	return new Point (itemBounds.width, itemBounds.height);
}
int internalGetMinimumWidth () {
	int width = minimumSize.x;
	width += MINIMUM_WIDTH + MARGIN_WIDTH;
	if (width < preferredWidth) {
		width += CHEVRON_IMAGE_WIDTH + CHEVRON_HORIZONTAL_TRIM + CHEVRON_LEFT_MARGIN;
	}
	return width;
}
/*
 *  Called when the chevron is selected.
 */
void onSelection (Event ev) {
	Rectangle bounds = chevron.getBounds();
	Event event = new Event();
	event.detail = SWT.ARROW;
	event.x = bounds.x;
	event.y = bounds.y + bounds.height;
	postEvent (SWT.Selection, event);
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
void setBounds (int x, int y, int width, int height) {
	itemBounds.x = x;
	itemBounds.y = y;
	itemBounds.width = width;
	itemBounds.height = height;
	if (control != null) {
		int controlHeight = Math.min (height, control.getSize().y);
		int controlWidth = width - MINIMUM_WIDTH - MARGIN_WIDTH;
		if (width < preferredWidth) {
			controlWidth -= CHEVRON_IMAGE_WIDTH + CHEVRON_HORIZONTAL_TRIM + CHEVRON_LEFT_MARGIN;
		}
		control.setBounds (
			x + MINIMUM_WIDTH, 
			y + MARGIN_HEIGHT, 
			controlWidth, 
			controlHeight);
	}
	updateChevron();
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
		Rectangle bounds = getBounds();
		control.setBounds (
			bounds.x + MINIMUM_WIDTH, 
			bounds.y + MARGIN_HEIGHT, 
			bounds.width - MINIMUM_WIDTH - MARGIN_WIDTH, 
			bounds.height - (2 * MARGIN_HEIGHT));
			
		control.setVisible(true);
		if (preferredWidth == -1) {
			Point size = control.computeSize (SWT.DEFAULT, SWT.DEFAULT, false);
			preferredWidth = size.x;
		}
	}
}
/**
 * Sets the minimum size that the cool item can
 * be resized to using the cool item's gripper.
 * 
 * @param width the minimum width of the cool item, in pixels
 * @param height the minimum height of the cool item, in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setMinimumSize (int width, int height) {
	checkWidget ();
	setMinimumSize(new Point(width, height));
}
/**
 * Sets the minimum size that the cool item can
 * be resized to using the cool item's gripper.
 * 
 * @param size a point representing the minimum width and height of the cool item, in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setMinimumSize (Point size) {
	checkWidget ();
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);	
	minimumSize = size;
}
/**
 * @deprecated use setMinimumSize
 */
public void setMinimumWidth (int width) {
	checkWidget ();
	setMinimumSize (width, getMinimumSize().y);
}
public void setPreferredSize (int width, int height) {
	checkWidget();
	preferredWidth = Math.max (width, MINIMUM_WIDTH);
	Rectangle bounds = getBounds();
	setBounds(bounds.x, bounds.y, bounds.width, height);
	if (height != bounds.height) parent.relayout();
}
public void setPreferredSize (Point size) {
	checkWidget();
	if (size == null) error(SWT.ERROR_NULL_ARGUMENT);
	setPreferredSize(size.x, size.y);
}
public void setSize (int width, int height) {
	checkWidget();
	int newWidth = Math.max (width, MINIMUM_WIDTH);
	itemBounds.width = requestedWidth = newWidth;
	if (preferredWidth == -1) preferredWidth = newWidth;
	itemBounds.height = height;
	if (control != null) {
		int controlWidth = newWidth - MINIMUM_WIDTH - MARGIN_WIDTH;
		int controlHeight = height - (2 * MARGIN_HEIGHT);
		control.setSize(controlWidth, controlHeight);
	}
	parent.relayout();
	updateChevron();
}
public void setSize (Point size) {
	checkWidget();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}
void updateChevron() {
	if (control != null) {
		int width = itemBounds.width;
		if (width < preferredWidth) {
			int height = Math.min (control.getSize ().y, itemBounds.height);
			if (chevron == null) {
				chevron = new ToolBar (parent, SWT.FLAT);
				ToolItem toolItem = new ToolItem (chevron, SWT.PUSH);
				if (height > 6) {
					toolItem.setImage (getArrowImage ());
				}	
				chevron.setBackground(parent.getBackground());
				toolItem.addListener (SWT.Selection, new Listener() {
					public void handleEvent (Event event) {
						CoolItem.this.onSelection (event);
					}
				});
			}
			chevron.setBounds (
				itemBounds.x + width - CHEVRON_LEFT_MARGIN - CHEVRON_IMAGE_WIDTH - CHEVRON_HORIZONTAL_TRIM,
				itemBounds.y + MARGIN_HEIGHT,
				CHEVRON_IMAGE_WIDTH + CHEVRON_HORIZONTAL_TRIM,
				height);
			chevron.setVisible(true);
		} else {
			if (chevron != null) {
				chevron.setVisible(false);
			}
		}
	}
}
}

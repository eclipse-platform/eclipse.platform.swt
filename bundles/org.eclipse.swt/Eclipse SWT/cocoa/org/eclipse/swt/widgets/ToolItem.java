/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.cocoa.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ToolItem extends Item {
	NSView view;
	NSButton button, arrow;
	int width = DEFAULT_SEPARATOR_WIDTH;
	ToolBar parent;
	Image hotImage, disabledImage;
	String toolTipText;
	Control control;
	boolean selection;

	static final int DEFAULT_WIDTH = 24;
	static final int DEFAULT_HEIGHT = 22;
	static final int DEFAULT_SEPARATOR_WIDTH = 6;
	static final int INSET = 5;
	static final int ARROW_WIDTH = 5;
	static final int ARROW_INSET = 2;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
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
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user,
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
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

Point computeSize () {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((parent.style & SWT.HORIZONTAL) != 0) {
			width = getWidth ();
			height = DEFAULT_HEIGHT;
		} else {
			width = DEFAULT_WIDTH;
			height = getWidth ();
		}
		if (control != null) {
			height = Math.max (height, control.getMininumHeight ());
		}
	} else {
		((NSButton)button).sizeToFit ();
		NSRect frame = button.frame();
		width = (int)frame.width + INSET;
		height = (int)frame.height;
		if (arrow != null) {
			width += ARROW_INSET + ARROW_WIDTH;
		}
		view.setNeedsDisplay(true);
	}
	return new Point (width, height);
}

void createWidget() {
	createJNIRef ();
	if ((style & SWT.SEPARATOR) != 0) {
		SWTBox widget = (SWTBox)new SWTBox().alloc();
		widget.initWithFrame(new NSRect());
		widget.setBoxType(OS.NSBoxSeparator);
		widget.setTag(jniRef);
		view = widget;
	} else {
		SWTView widget = (SWTView)new SWTView().alloc();
		widget.initWithFrame(new NSRect());
		widget.setTag(parent.jniRef);
		parent.contentView().addSubview_(widget);
		button = (NSButton)new SWTButton().alloc();
		button.initWithFrame(new NSRect());
		button.setBordered(false);
		button.setAction(OS.sel_sendSelection);
		button.setTarget(button);
		button.setTag(jniRef);
		button.setImagePosition(OS.NSImageOverlaps);
		button.setTitle(NSString.stringWith(""));
		button.setEnabled(parent.getEnabled());
		widget.addSubview_(button);
		if ((style & SWT.DROP_DOWN) != 0) {
			arrow = (NSButton)new SWTButton().alloc();
			arrow.initWithFrame(new NSRect());
			arrow.setBordered(false);
			arrow.setAction(OS.sel_sendArrowSelection);
			arrow.setTarget(arrow);
			arrow.setTag(jniRef);
			arrow.setEnabled(parent.getEnabled());
			widget.addSubview_(arrow);
		}
		view = widget;
	}
}
void destroyWidget() {
	parent.destroyItem(this);
	super.destroyWidget();
}

void drawRect(int id, NSRect rect) {
	super.drawRect(id, rect);
	if (getSelection ()) {
		NSRect bounds = view.bounds();
		NSGraphicsContext context = NSGraphicsContext.currentContext();
		context.saveGraphicsState();
		NSColor.colorWithDeviceRed(0.1f, 0.1f, 0.1f, 0.1f).setFill();
		NSColor.colorWithDeviceRed(0.2f, 0.2f, 0.2f, 0.2f).setStroke();
		NSBezierPath.fillRect(bounds);
		bounds.x += 0.5f;
		bounds.y += 0.5f;
		bounds.width -= 1;
		bounds.height -= 1;
		NSBezierPath.strokeRect(bounds);
		context.restoreGraphicsState();
	}
	if (arrow != null && id == arrow.id) {
		NSRect frame = arrow.frame();
		NSGraphicsContext context = NSGraphicsContext.currentContext();
		context.saveGraphicsState();
		NSPoint p1 = new NSPoint();
		p1.y = (float)Math.ceil(frame.height / 2 - frame.width / 2);
		NSPoint p2 = new NSPoint();
		p2.x = frame.width;
		p2.y = p1.y;
		NSPoint p3 = new NSPoint();
		p3.x = frame.width / 2;
		p3.y = (float)(p2.y + Math.sqrt(Math.pow(frame.width, 2) - Math.pow(frame.width / 2, 2)));
		NSBezierPath path = NSBezierPath.bezierPath();
		path.moveToPoint(p1);
		path.lineToPoint(p2);
		path.lineToPoint(p3);
		path.closePath();
		NSColor color = isEnabled() ? NSColor.blackColor() : NSColor.disabledControlTextColor();
		color.set();
		path.fill();
		context.restoreGraphicsState();
	}
}

void enableWidget(boolean enabled) {
	if ((style & SWT.SEPARATOR) == 0) {
		((NSButton)button).setEnabled(enabled);
		if (arrow != null) {
			((NSButton)arrow).setEnabled(enabled);
		}
	}
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
	NSRect rect = view.frame();
	return new Rectangle((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
}

/**
 * Returns the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
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

/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}

/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	return (state & DISABLED) == 0;
}

/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}

/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	return parent;
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return selection;
}

/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	return width;
}

/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}

void releaseParent () {
	super.releaseParent ();
	setVisible (false);
}

void releaseHandle () {
	super.releaseHandle ();
	if (view != null) {
		OS.objc_msgSend(view.id, OS.sel_setTag_1, -1);
		view.release();
	}
	if (button != null) {
		OS.objc_msgSend(button.id, OS.sel_setTag_1, -1);
		button.release();
	}
	if (arrow != null) {
		OS.objc_msgSend(arrow.id, OS.sel_setTag_1, -1);
		arrow.release();
	}
	view = button = arrow = null;
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	control = null;
	toolTipText = null;
	image = disabledImage = hotImage = null; 
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}

void selectRadio () {
	int index = 0;
	ToolItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}

void sendArrowSelection () {
	NSRect frame = view.frame();
	Event event = new Event ();
	event.detail = SWT.ARROW;
	event.x = (int)frame.x;
	event.y = (int)(frame.y + arrow.frame().height);
	postEvent (SWT.Selection, event);
}

void sendSelection () {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	if ((style & SWT.CHECK) != 0) setSelection (!getSelection ());
	postEvent (SWT.Selection);
}

void setBounds (int x, int y, int width, int height) {
	NSRect rect = new NSRect();
	rect.x = x;
	rect.y = y;
	rect.width = width;
	rect.height = height;
	view.setFrame(rect);
	if (arrow != null) {
		rect = button.frame();
		NSRect arrowRect = new NSRect();
		arrowRect.x = rect.width + ARROW_INSET;
		arrowRect.width = ARROW_WIDTH;
		arrowRect.height = rect.height;
		arrow.setFrame(arrowRect);
	}
}

/**
 * Sets the control that is used to fill the bounds of
 * the item when the item is a <code>SEPARATOR</code>.
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
	if ((style & SWT.SEPARATOR) == 0) return;
	if (this.control == control) return;
	this.control = control;
	view.setHidden(control != null);
	if (control != null && !control.isDisposed ()) {
		control.moveAbove (null);
	}
	parent.relayout ();
}

/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	if ((state & DISABLED) == 0 && enabled) return;
	if (enabled) {
		state &= ~DISABLED;		
	} else {
		state |= DISABLED;
	}
	enableWidget(enabled);
}

/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
	updateImage (true);
}

/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
	updateImage (true);
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	updateImage (true);
}

boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}

/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	this.selection = selected;
	view.setNeedsDisplay(true);
}

/**
 * Sets the receiver's text. The string may include
 * the mnemonic character.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	NSString str = NSString.stringWith(string);
	((NSButton)button).setTitle(str);
	parent.relayout ();
	if (text.length() != 0 && image != null) {
		if ((parent.style & SWT.RIGHT) != 0) {
			((NSButton)button).setImagePosition(OS.NSImageLeft);
		} else {
			((NSButton)button).setImagePosition(OS.NSImageAbove);		
		}
	} else {
		((NSButton)button).setImagePosition(OS.NSImageOverlaps);			
	}
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
	view.setToolTip(NSString.stringWith(string));
}

void setVisible (boolean visible) {
	if (visible) {
		if ((state & HIDDEN) == 0) return;
		state &= ~HIDDEN;
	} else {
		if ((state & HIDDEN) != 0) return;
		state |= HIDDEN;
	}
	view.setHidden(!visible);
}

/**
 * Sets the width of the receiver, for <code>SEPARATOR</code> ToolItems.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0 || this.width == width) return;
	this.width = width;
	parent.relayout();
}

void updateImage (boolean layout) {
	if ((style & SWT.SEPARATOR) != 0) return;
	Image image = null;
	if (hotImage != null) {
		image = hotImage;
	} else {
		if (this.image != null) {
			image = this.image;
		} else {
			image = disabledImage;
		}
	}
	((NSButton)button).setImage(image != null ? image.handle : null);
	if (text.length() != 0 && image != null) {
		if ((parent.style & SWT.RIGHT) != 0) {
			((NSButton)button).setImagePosition(OS.NSImageLeft);
		} else {
			((NSButton)button).setImagePosition(OS.NSImageAbove);		
		}
	} else {
		((NSButton)button).setImagePosition(OS.NSImageOverlaps);			
	}
	parent.relayout();
}

}

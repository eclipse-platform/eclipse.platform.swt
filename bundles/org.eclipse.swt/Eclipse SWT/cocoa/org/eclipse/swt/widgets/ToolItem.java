/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolItem extends Item {
	NSView view;
	NSButton button;
	NSToolbarItem nsItem;
	NSMenuItem nsMenuRep;
	NSString id;
	int width = DEFAULT_SEPARATOR_WIDTH;
	ToolBar parent;
	Image hotImage, disabledImage;
	String toolTipText;
	Control control;
	boolean selection;

	static final int DEFAULT_WIDTH = 24;
	static final int DEFAULT_HEIGHT = 22;
	static final int DEFAULT_SEPARATOR_WIDTH = 6;
	static final int INSET = 3;
	static final int ARROW_WIDTH = 5;

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

int /*long*/ accessibleHandle() {
	if (button != null && button.cell() != null) return button.cell().id;
	if (view != null) return view.id;
	return 0;
}

int /*long*/ accessibilityActionNames(int /*long*/ id, int /*long*/ sel) {

	int /*long*/ returnValue = super.accessibilityActionNames(id, sel);
	
	if (id == accessibleHandle()) {
		if ((style & SWT.DROP_DOWN) != 0) {
			NSArray baseArray = new NSArray(returnValue);
			NSMutableArray ourNames = NSMutableArray.arrayWithCapacity(baseArray.count() + 1);
			ourNames.addObjectsFromArray(baseArray);
			ourNames.addObject(OS.NSAccessibilityShowMenuAction);
			returnValue = ourNames.id;
		}
	} else {
		returnValue = super.accessibilityActionNames(id, sel);
	}
	return returnValue;
}

int /*long*/ accessibilityAttributeNames(int /*long*/ id, int /*long*/ sel) {

	int /*long*/ returnValue = super.accessibilityAttributeNames(id, sel);
	
	if (id == accessibleHandle()) {
		if ((style & (SWT.CHECK|SWT.RADIO)) !=0) {
			NSArray baseArray = new NSArray(returnValue);
			NSMutableArray ourNames = NSMutableArray.arrayWithCapacity(baseArray.count() + 1);
			ourNames.addObjectsFromArray(baseArray);
			ourNames.addObject(OS.NSAccessibilityValueAttribute);
			returnValue = ourNames.id;
		} else if ((style & SWT.DROP_DOWN) != 0) {
			NSArray baseArray = new NSArray(returnValue);
			NSMutableArray ourNames = NSMutableArray.arrayWithCapacity(baseArray.count() + 1);
			ourNames.addObjectsFromArray(baseArray);
			ourNames.addObject(OS.NSAccessibilityChildrenAttribute);
			returnValue = ourNames.id;
		}
	} else {
		returnValue = super.accessibilityAttributeNames(id, sel);
	}
	return returnValue;
}

int /*long*/ accessibilityAttributeValue(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	NSString nsAttributeName = new NSString(arg0);

	if (nsAttributeName.isEqualToString (OS.NSAccessibilityRoleAttribute) || nsAttributeName.isEqualToString (OS.NSAccessibilityRoleDescriptionAttribute)) {
		NSString roleText = ((style & SWT.PUSH) != 0) ? OS.NSAccessibilityButtonRole
				: ((style & SWT.RADIO) != 0) ? OS.NSAccessibilityRadioButtonRole
				: ((style & SWT.CHECK) != 0) ? OS.NSAccessibilityCheckBoxRole
				: ((style & SWT.DROP_DOWN) != 0) ? OS.NSAccessibilityMenuButtonRole
				: null; // SEPARATOR
		if (roleText != null) {
			if (nsAttributeName.isEqualToString (OS.NSAccessibilityRoleAttribute)) {
				return roleText.id;
			} else { // NSAccessibilityRoleDescriptionAttribute
				int /*long*/ description = OS.NSAccessibilityRoleDescription (roleText.id, 0);
				return description;
			}
		}
	} else if (nsAttributeName.isEqualToString (OS.NSAccessibilityTitleAttribute)) {
		String accessibleText = text.length() > 0 ? text : toolTipText;
		if (accessibleText != null) {
			return NSString.stringWith(accessibleText).id;
		} else {
			return NSString.string().id;
		}
	} else if (nsAttributeName.isEqualToString (OS.NSAccessibilityHelpAttribute)) {
		String accessibleText = toolTipText;
		if (accessibleText != null) {
			return NSString.stringWith(accessibleText).id;
		} else {
			return NSString.string().id;
		}
	} else if (nsAttributeName.isEqualToString (OS.NSAccessibilityValueAttribute) && (style & (SWT.CHECK | SWT.RADIO)) != 0) {
		NSNumber value = NSNumber.numberWithInt(selection ? 1 : 0);
		return value.id;
	} else if (nsAttributeName.isEqualToString(OS.NSAccessibilityEnabledAttribute)) {
		NSNumber value = NSNumber.numberWithBool(getEnabled());
		return value.id;
	} else if (nsAttributeName.isEqualToString(OS.NSAccessibilityChildrenAttribute)) {
		// 
		NSArray value = NSArray.array();
		return value.id;
	} else if (nsAttributeName.isEqualToString(OS.NSAccessibilityParentAttribute)) {
		// Parent of the toolitem is always its toolbar.
		return parent.view.id;
	}

	return super.accessibilityAttributeValue(id, sel, arg0);
}

boolean accessibilityIsIgnored(int /*long*/ id, int /*long*/ sel) {
	// The interesting part of a ToolItem is its button, if it has one.
	if (id == accessibleHandle()) return false;
	return super.accessibilityIsIgnored(id, sel);
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
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group 
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
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

boolean handleKeyDown() {
	if ((style & SWT.DROP_DOWN) != 0) {
		NSRect frame = view.frame();
		Event event = new Event ();
		event.detail = SWT.ARROW;
		event.x = (int)frame.x;
		event.y = (int)(frame.y + frame.height);
		sendSelectionEvent (SWT.Selection, event, false);
		return true;
	} else {
		return false;
	}
}

Point computeSize () {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.SEPARATOR) != 0) {
		// In the unified toolbar case the width is ignored if 0, DEFAULT, or SEPARATOR_FILL.
		if ((parent.style & SWT.HORIZONTAL) != 0) {
			width = getWidth ();
			if (width <= 0) width = DEFAULT_SEPARATOR_WIDTH;
			height = DEFAULT_HEIGHT;
		} else {
			width = DEFAULT_WIDTH;
			height = getWidth ();
			if (height <= 0) height = DEFAULT_SEPARATOR_WIDTH;
		}
		if (control != null) {
			height = Math.max (height, control.getMininumHeight ());
		}
	} else {
		if (text.length () != 0 || image != null) {
			NSSize size = button.cell().cellSize();
			width = (int)Math.ceil(size.width);
			height = (int)Math.ceil(size.height);
		} else {
			width = DEFAULT_WIDTH;
			height = DEFAULT_HEIGHT;
		}
		if ((style & SWT.DROP_DOWN) != 0) {
			width += ARROW_WIDTH + INSET;
		}
		
		if (parent.nsToolbar == null || image != null) {
			width += INSET * 2;
			height += INSET * 2;
		} else {
			height -= 2;
		}
	}
	return new Point (width, height);
}

void createHandle () {
	if (parent.nsToolbar != null) {
		id = NSString.stringWith(String.valueOf(ToolBar.NEXT_ID++));
		id.retain();
		nsItem = ((NSToolbarItem)new NSToolbarItem().alloc()).initWithItemIdentifier(id);
		nsItem.setAction(OS.sel_sendSelection);
		nsMenuRep = ((NSMenuItem)new SWTMenuItem().alloc()).initWithTitle(NSString.string(), OS.sel_sendSelection, NSString.string());
		nsItem.setMenuFormRepresentation(nsMenuRep);
	} 

	if ((style & SWT.SEPARATOR) != 0) {
		if (parent.nsToolbar != null) {
			view = (NSView)new SWTView().alloc();
			view.init();
		} else {
			NSBox widget = (NSBox)new SWTBox().alloc();
			widget.init();
			widget.setBoxType(OS.NSBoxSeparator);
			widget.setBorderWidth(0);
			view = widget;
		}
	} else {
		NSView widget = (NSView)new SWTView().alloc();
		widget.init();
		button = (NSButton)new SWTButton().alloc();
		button.init();
		/*
		* Feature in Cocoa.  NSButtons without borders do not leave any margin
		* between their edge and their image.  The workaround is to provide a
		* custom cell that displays the image in a better position. 
		*/
		NSButtonCell cell = (NSButtonCell)new SWTButtonCell ().alloc ().init ();
		button.setCell (cell);
		cell.release();
		cell.setHighlightsBy(OS.NSContentsCellMask);
		cell.setBackgroundStyle(OS.NSBackgroundStyleRaised);
		button.setBordered(false);
		button.setAction(OS.sel_sendSelection);
		button.setTarget(button);
		if (nsMenuRep != null) nsMenuRep.setTarget(button);
		Font font = parent.font != null ? parent.font : parent.defaultFont ();
		button.setFont(font.handle);
		button.setImagePosition(OS.NSImageOverlaps);
		button.setTitle(NSString.string());
		button.setEnabled(parent.getEnabled());
		widget.addSubview(button);
		view = widget;
	}
}

NSAttributedString createString() {
	NSAttributedString attribStr = parent.createString(text, null, parent.foreground, SWT.CENTER, false, true, true);
	attribStr.autorelease();
	return attribStr;
}

void deregister () {
	super.deregister ();
	display.removeWidget(view);
	
	if (button != null) {
		display.removeWidget (button);
		display.removeWidget (button.cell());
	}
}

void destroyWidget() {
	parent.destroyItem(this);
	super.destroyWidget();
}

void drawImageWithFrameInView (int /*long*/ id, int /*long*/ sel, int /*long*/ image, NSRect rect, int /*long*/ view) {
	if (text.length () > 0) {
		if ((parent.style & SWT.RIGHT) != 0) {
			rect.x += 3;
		} else {
			rect.y += 3;			
		}
	}
	int /*long*/ cgContext = NSGraphicsContext.currentContext().graphicsPort();
	NSCell cell = new NSCell(id);
	boolean drawSelected = (parent.nsToolbar != null) && getSelection() && ((style & SWT.CHECK) != 0) && !cell.isHighlighted();

	if (drawSelected) {
		NSGraphicsContext.currentContext().saveGraphicsState();
		CGRect cgRect = new CGRect();
		cgRect.origin.x = rect.x;
		cgRect.origin.y = rect.y;
		cgRect.size.width = rect.width;
		cgRect.size.height = rect.height;
		OS.CGContextBeginTransparencyLayerWithRect(cgContext, cgRect, 0);
	}

	super.drawImageWithFrameInView(id, sel, image, rect, view);

	if (drawSelected) {
		NSColor.colorWithCalibratedRed(0,0,0,.3f).setFill();
		OS.NSRectFillUsingOperation(rect, OS.NSCompositeSourceAtop);
		OS.CGContextEndTransparencyLayer(cgContext);
		NSGraphicsContext.currentContext().restoreGraphicsState();
	}
}

NSRect drawTitleWithFrameInView (int /*long*/ id, int /*long*/ sel, int /*long*/ title, NSRect titleRect, int /*long*/ view) {
    boolean hiliteShadow = new NSButtonCell(id).isHighlighted() && text.length() > 0 && image == null;
    
    // An unbordered cell doesn't draw any highlighting when pushed or selected, so we have to do it here.
    if (hiliteShadow) {
    	NSColor transWhiteColor = NSColor.colorWithCalibratedRed(1.0f, 1.0f, 1.0f, .8f);
		NSAttributedString attribStr = new NSAttributedString(title);
		NSMutableAttributedString tmpString = new NSMutableAttributedString(attribStr.mutableCopy());
		NSRange range = new NSRange();
		range.location = 0;
		range.length = attribStr.length();
		tmpString.addAttribute(OS.NSForegroundColorAttributeName, transWhiteColor, range);
		tmpString.autorelease();
		title = tmpString.id;
    }
	return super.drawTitleWithFrameInView(id, sel, title, titleRect, view);
}

void drawWidget (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
	if (id == view.id) {
		boolean drawSelected = getSelection() && (parent.nsToolbar == null); 
		if (drawSelected) {
			NSRect bounds = view.bounds();
			context.saveGraphicsState();
			NSColor.colorWithCalibratedRed(0.1f, 0.1f, 0.1f, 0.1f).setFill();
			NSColor.colorWithCalibratedRed(0.2f, 0.2f, 0.2f, 0.2f).setStroke();
			NSBezierPath.fillRect(bounds);
			bounds.x += 0.5f;
			bounds.y += 0.5f;
			bounds.width -= 1;
			bounds.height -= 1;
			NSBezierPath.strokeRect(bounds);
			context.restoreGraphicsState();
		}
		if ((style & SWT.DROP_DOWN) != 0) {
			NSRect bounds = view.bounds();
			context.saveGraphicsState();
			NSBezierPath path = NSBezierPath.bezierPath();
			NSPoint pt = new NSPoint();
			path.moveToPoint(pt);
			pt.x += ARROW_WIDTH;
			path.lineToPoint(pt);
			pt.y += ARROW_WIDTH - 1;
			pt.x -= ARROW_WIDTH / 2f;
			path.lineToPoint(pt);
			path.closePath();
			NSAffineTransform transform = NSAffineTransform.transform();
			transform.translateXBy((int)bounds.width - ARROW_WIDTH - INSET, (int)(bounds.height - ARROW_WIDTH / 2) / 2);
			transform.concat();
			NSColor color = isEnabled() ? NSColor.blackColor() : NSColor.disabledControlTextColor();
			color.set();
			path.fill();
			context.restoreGraphicsState();
		}
	}
}

void enableWidget(boolean enabled) {
	if (parent.nsToolbar != null) {
		nsItem.setEnabled(enabled);
	} 

	if ((style & SWT.SEPARATOR) == 0) {
		((NSButton)button).setEnabled(enabled);
		updateImage(true);
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
	if (parent.nsToolbar != null) {
		NSRect rect = view.frame();
		// ToolItems in the unified toolbar are not contained directly within the Toolbar.
		// Convert the toolitem rect from toolitem-relative coordinates to its
		// parent, the toolbar, relative coordinates.
		rect = parent.view.convertRect_fromView_(rect, view);
		return new Rectangle((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
	} 

	NSRect rect = view.frame();
	return new Rectangle((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
}

void setClipRegion (NSView view) {
	parent.setClipRegion(view);
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

boolean getDrawing () {
	return parent.getDrawing ();
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

NSString getItemID() {
    NSString itemID = id;
    
    // For separators, return a Cocoa constant for the tool item ID.
    if ((style & SWT.SEPARATOR) != 0) {
    	// If we are using a non-default width or control use that instead.  
    	if (control == null) {
    		if (width == DEFAULT_SEPARATOR_WIDTH || width == 0) {
    			itemID = OS.NSToolbarSeparatorItemIdentifier;
    		} else if (width == SWT.DEFAULT) {
    			itemID = OS.NSToolbarSpaceItemIdentifier;
    		} else if (width == SWT.SEPARATOR_FILL) {
    			itemID = OS.NSToolbarFlexibleSpaceItemIdentifier;
    		}
    	}
    }
    
    return itemID;
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

boolean isDrawing () {
	return getDrawing() && parent.isDrawing ();
}

int /*long*/ menuForEvent (int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	return parent.menuForEvent (id, sel, theEvent);
}

void mouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseDown)) return;
	Display display = this.display;
	display.trackingControl = parent;
	super.mouseDown(id, sel, theEvent);
	display.trackingControl = null;
	if ((style & SWT.DROP_DOWN) != 0 && id == view.id) {
		NSRect frame = view.frame();
		Event event = new Event ();
		event.detail = SWT.ARROW;
		event.x = (int)frame.x;
		event.y = (int)(frame.y + frame.height);
		sendSelectionEvent (SWT.Selection, event, false);
	}
}

boolean mouseDownCanMoveWindow(int /*long*/ id, int /*long*/ sel) {
	if (id == view.id) return false;
	return super.mouseDownCanMoveWindow(id, sel);
}

void mouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseUp)) return;
	super.mouseUp(id, sel, theEvent);
}

void mouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseMove)) return;
	super.mouseDragged(id, sel, theEvent);
}

void rightMouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseDown)) return;
	super.rightMouseDown(id, sel, theEvent);
}

void rightMouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseUp)) return;
	super.rightMouseUp(id, sel, theEvent);
}

void rightMouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseMove)) return;
	super.rightMouseDragged(id, sel, theEvent);
}

void otherMouseDown(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseDown)) return;
	super.otherMouseDown(id, sel, theEvent);
}

void otherMouseUp(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseUp)) return;
	super.otherMouseUp(id, sel, theEvent);
}

void otherMouseDragged(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (!parent.mouseEvent(parent.view.id, sel, theEvent, SWT.MouseMove)) return;
	super.otherMouseDragged(id, sel, theEvent);
}

void register () {
	super.register ();
	display.addWidget (view, this);
	
	if (button != null) {
		display.addWidget (button, this);
		display.addWidget (button.cell(), this);
	}
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

void releaseParent () {
	super.releaseParent ();
	setVisible (false);
}

void releaseHandle () {
	super.releaseHandle ();
	if (view != null) view.release ();
	if (button != null) button.release ();
	view = button = null;
	if (nsItem != null) {
	    nsItem.release();
	    nsItem = null;
	}
	if (id != null) {
	    id.release();
	    id = null;
	}
	if (nsMenuRep != null) {
		nsMenuRep.release();
		nsMenuRep = null;
	}
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	control = null;
	toolTipText = null;
	image = disabledImage = hotImage = null; 
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

void sendSelection () {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	if ((style & SWT.CHECK) != 0) setSelection (!getSelection ());
	sendSelectionEvent (SWT.Selection);
}

void setBounds (int x, int y, int width, int height) {
	NSRect rect = new NSRect();
	if (parent.nsToolbar == null) {
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		view.setFrame(rect);
		if (button != null) {
			rect.x = 0;
			rect.y = 0;
			rect.width = width;
			rect.height = height;
			if ((style & SWT.DROP_DOWN) != 0) rect.width -= ARROW_WIDTH + INSET;
			button.setFrame(rect);
		}
	} else {
		// Let the NSToolbar manage the position of the view in the toolbar.
		NSSize newSize = new NSSize();
		newSize.height = height;
		newSize.width = width;
		// Temporarily clear the view. This will force the item and toolbar to re-layout
		// when the view is set again.
		nsItem.setView(null);
		view.setFrameSize(newSize);
		if ((style & SWT.DROP_DOWN) != 0) newSize.width -= ARROW_WIDTH + INSET;
		if (button != null) button.setFrameSize(newSize);
		if ((style & SWT.DROP_DOWN) != 0) newSize.width += ARROW_WIDTH + INSET;
		nsItem.setMinSize(newSize);
		nsItem.setMaxSize(newSize);
		nsItem.setView(view);
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

	if (parent.nsToolbar == null) {
        NSBox widget = (NSBox)view;
        if (control == null) {
        	widget.setBoxType(OS.NSBoxSeparator);
        } else {
        	widget.setBoxType(OS.NSBoxCustom);
        }
	} else {
		nsItem.setMenuFormRepresentation(control == null ? nsMenuRep : NSMenuItem.separatorItem());
	}
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

boolean setFocus () {
	if (button == null) return false;
	if (!isEnabled ()) return false;
	NSWindow window = view.window ();
	if(window == null) { 
		return false;
	}
	return window.makeFirstResponder (button);
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
		sendSelectionEvent (SWT.Selection);
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
	
	if (parent.nsToolbar != null) {
		if ((style & SWT.RADIO) != 0 && selection) {
			parent.nsToolbar.setSelectedItemIdentifier(nsItem.itemIdentifier());
		}
	}
	
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
	NSButton widget = (NSButton)button;
	if (parent.nsToolbar != null) {
		char [] chars = new char [text.length ()];
		text.getChars (0, chars.length, chars, 0);
		int length = fixMnemonic (chars);
		nsMenuRep.setTitle(NSString.stringWithCharacters(chars, length));
	}

	widget.setAttributedTitle(createString());
	if (text.length() != 0 && image != null) {
		if ((parent.style & SWT.RIGHT) != 0) {
			widget.setImagePosition(OS.NSImageLeft);
		} else {
			widget.setImagePosition(OS.NSImageAbove);		
		}
	} else {
		widget.setImagePosition(text.length() != 0 ? OS.NSNoImage : OS.NSImageOnly);
	}
	parent.relayout ();
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be 
 * escaped by doubling it in the string.
 * </p>
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
	if (parent.nsToolbar != null) {
        char[] chars = new char [toolTipText.length ()];
        string.getChars (0, chars.length, chars, 0);
        int length = fixMnemonic (chars);
        nsItem.setToolTip(NSString.stringWithCharacters (chars, length));
	} else {
        parent.checkToolTip (this);
	}
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
 * @param width the new width. If the new value is <code>SWT.DEFAULT</code>,
 * the width is a fixed-width area whose amount is determined by the platform.
 * If the new value is 0 a vertical or horizontal line will be drawn, depending
 * on the setting of the corresponding style bit (<code>SWT.VERTICAL</code> or 
 * <code>SWT.HORIZONTAL</code>). If the new value is <code>SWT.SEPARATOR_FILL</code>
 * a variable-width space is inserted that acts as a spring between the two adjoining
 * items which will push them out to the extent of the containing ToolBar.
 * 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < SWT.SEPARATOR_FILL || this.width == width) return;
	this.width = width;
	if (parent.nsToolbar != null) {
		NSToolbar toolbar = parent.nsToolbar;
		int index = parent.indexOf(this);
		toolbar.removeItemAtIndex(index);
		toolbar.insertItemWithItemIdentifier(getItemID(), index);
	}
	parent.relayout();
}

String tooltipText () {
	return toolTipText;
}

void updateImage (boolean layout) {
	if ((style & SWT.SEPARATOR) != 0) return;
	Image newImage = null;

	if ((state & DISABLED) == DISABLED && disabledImage != null) {
		newImage = disabledImage;
	} else {
		if ((state & HOT) == HOT && hotImage != null) {
			newImage = hotImage;
		} else {
			newImage = image;
		}
	}

	NSButton widget = (NSButton)button;
	/*
	 * Feature in Cocoa.  If the NSImage object being set into the button is
	 * the same NSImage object that is already there then the button does not
	 * redraw itself.  This results in the button's image not visually updating
	 * if the NSImage object's content has changed since it was last set
	 * into the button.  The workaround is to explicitly redraw the button.
	 */
	widget.setImage(newImage != null ? newImage.handle : null);
	widget.setNeedsDisplay(true);
	if (text.length() != 0 && newImage != null) {
		if ((parent.style & SWT.RIGHT) != 0) {
			widget.setImagePosition(OS.NSImageLeft);
		} else {
			widget.setImagePosition(OS.NSImageAbove);		
		}
	} else {	
		widget.setImagePosition(text.length() != 0 ? OS.NSNoImage : OS.NSImageOnly);		
	}
	parent.relayout();
}

boolean validateMenuItem(int /*long*/ id, int /*long*/ sel, int /*long*/ menuItem) {
	return isEnabled();
}

}

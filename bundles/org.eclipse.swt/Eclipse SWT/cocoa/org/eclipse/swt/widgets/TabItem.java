/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent a selectable user interface object
 * corresponding to a tab for a page in a tab folder.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder, TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabItem extends Item {
	static final int IMAGE_GAP = 2;
	TabFolder parent;
	Control control;
	String toolTipText;
	NSTabViewItem nsItem;
	NSAttributedString attriStr;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>TabFolder</code>) and a style value
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TabItem (TabFolder parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>TabFolder</code>), a style value
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TabItem (TabFolder parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, index);
}

int /*long*/ accessibilityAttributeValue (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
	
	if (id == nsItem.id) {
		NSString nsAttributeName = new NSString(arg0);

		if (nsAttributeName.isEqualToString(OS.NSAccessibilityTitleAttribute)) {
			if (text != null) {
				return NSString.stringWith(text).id;
			}
		} else if (nsAttributeName.isEqualToString(OS.NSAccessibilityHelpAttribute)) {
			if (toolTipText != null) {
				return NSString.stringWith(toolTipText).id;
			}
		}
	}
	
	return super.accessibilityAttributeValue(id, sel, arg0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void deregister () {
	super.deregister ();
	display.removeWidget (nsItem);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

void drawLabelInRect(int /*long*/ id, int /*long*/ sel, boolean shouldTruncateLabel, NSRect rect) {
	if (image != null && !image.isDisposed()) {
		NSSize imageSize = image.handle.size();
		NSRect destRect = new NSRect();
		destRect.x = rect.x;
		destRect.y = rect.y;
		destRect.width = imageSize.width;
		destRect.height = imageSize.height;
		NSGraphicsContext.static_saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.scaleXBy(1, -1);
		transform.translateXBy(0, -(destRect.height + 2 * destRect.y));
		transform.concat();
		image.handle.drawInRect(destRect, new NSRect(), OS.NSCompositeSourceOver, 1);
		NSGraphicsContext.static_restoreGraphicsState();
		rect.x += imageSize.width + IMAGE_GAP;
		rect.width -= imageSize.width + IMAGE_GAP;		
	}
	if (attriStr != null) {
		attriStr.drawInRect(rect);
	}
	super.drawLabelInRect(id, sel, shouldTruncateLabel, rect);
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
 * 
 * @since 3.4
 */
public Rectangle getBounds() {
	checkWidget();
	Rectangle result = new Rectangle (0, 0, 0, 0);
	if (nsItem.respondsToSelector (OS.sel_accessibilityAttributeValue_)) {
		int /*long*/ posValue = OS.objc_msgSend (nsItem.id, OS.sel_accessibilityAttributeValue_, OS.NSAccessibilityPositionAttribute ());
		int /*long*/ sizeValue = OS.objc_msgSend (nsItem.id, OS.sel_accessibilityAttributeValue_, OS.NSAccessibilitySizeAttribute ());		
		NSValue val = new NSValue (posValue);
		NSPoint pt = val.pointValue ();
		NSWindow window = parent.view.window ();
		// pt is the lower-left corner of the control, convert from screen based to window based
		pt = window.convertScreenToBase (pt);
		// convert from window based to view based
		pt = parent.view.convertPoint_fromView_ (pt, null);
		val = new NSValue (sizeValue);
		NSSize size = val.sizeValue ();
		result.width = (int) Math.ceil (size.width);
		result.height = (int) Math.ceil (size.height);
		result.x = (int) pt.x;
		result.y = (int) pt.y - result.height;
	}
	return result;
}

/**
 * Returns the control that is used to fill the client area of
 * the tab folder when the user selects the tab item.  If no
 * control has been set, return <code>null</code>.
 * <p>
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

/**
 * Returns the receiver's parent, which must be a <code>TabFolder</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TabFolder getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}

void register () {
	super.register ();
	display.addWidget (nsItem, this);
}

void releaseHandle () {
	super.releaseHandle ();
	if (nsItem != null) nsItem.release();
	nsItem = null;
	if (attriStr != null) attriStr.release();
	attriStr = null;
	parent = null;
}

void releaseParent () {
	super.releaseParent ();
	int index = parent.indexOf (this);
	if (index == parent.getSelectionIndex ()) {
		if (control != null) control.setVisible (false);
	}
}

void releaseWidget () {
	super.releaseWidget ();
	control = null;
}

/**
 * Sets the control that is used to fill the client area of
 * the tab folder when the user selects the tab item.
 * <p>
 * @param control the new control (or null)
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
	if (this.control != null && this.control.isDisposed ()) {
		this.control = null;
	}
	Control oldControl = this.control, newControl = control;
	this.control = control;
	int index = parent.indexOf (this), selectionIndex = parent.getSelectionIndex();;
	if (index != selectionIndex) {
		if (newControl != null) {
			boolean hideControl = true;
			if (selectionIndex != -1) {
				Control selectedControl = parent.getItem(selectionIndex).getControl();
				if (selectedControl == newControl) hideControl=false;
			} 
			if (hideControl) newControl.setVisible(false);
		}
	} else {
		if (newControl != null) {
			newControl.setVisible (true);
		}
		if (oldControl != null) oldControl.setVisible (false);
	}
	NSView view;
	if (newControl != null) {
		view = newControl.topView();
	} else {
		view = (NSView)new NSView().alloc();
		view.init ();
		view.autorelease();
	}
	nsItem.setView (view);
	/*
	* Feature in Cocoa.  The method setView() removes the old view from
	* its parent.  The fix is to detected it has been removed and add
	* it back.
	*/
	if (oldControl != null) {
		NSView topView = oldControl.topView ();
		if (topView.superview () == null) {
			parent.contentView ().addSubview (topView, OS.NSWindowBelow, null);
		}
	}
}

public void setImage (Image image) {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setImage (image);
	//force parent to resize
	nsItem.setLabel(NSString.string());
}

/**
 * Sets the receiver's text.  The string may include
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
 * 
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setText (string);
	updateText ();
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
	parent.checkToolTip (this);
}

NSSize sizeOfLabel(int /*long*/ id, int /*long*/ sel, boolean shouldTruncateLabel) {
	NSSize size = super.sizeOfLabel(id, sel, shouldTruncateLabel);
	if (image != null && !image.isDisposed()) {
		NSSize imageSize = image.handle.size();
		size.width += imageSize.width + IMAGE_GAP;
	}
	if (attriStr != null) {
		NSSize textSize = attriStr.size();
		size.width += textSize.width;
	}
	return size;
}

String tooltipText () {
	return toolTipText;
}

void updateText () {
	NSTabViewItem selected = ((NSTabView)parent.view).selectedTabViewItem();
	updateText(selected != null && selected.id == nsItem.id);
}

void updateText (boolean selected) {
	if (attriStr != null) {
		attriStr.release();
	}
	float /*double*/ [] foreground = parent.foreground;
	if (foreground == null && selected && OS.VERSION >= 0x1070) {
		foreground = display.getNSColorRGB(NSColor.alternateSelectedControlTextColor());
	}
	attriStr = parent.createString(text, null, foreground, 0, false, true, true);
	//force parent to resize
	nsItem.setLabel(NSString.string());
}

}

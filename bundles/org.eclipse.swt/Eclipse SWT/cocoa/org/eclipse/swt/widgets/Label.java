/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent a non-selectable
 * user interface object that displays a string or image.
 * When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a separator label
 * with the default shadow style for the platform,
 * do not specify a shadow style.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SEPARATOR, HORIZONTAL, VERTICAL</dd>
 * <dd>SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of SHADOW_IN, SHADOW_OUT and SHADOW_NONE may be specified.
 * SHADOW_NONE is a HINT. Only one of HORIZONTAL and VERTICAL may be specified.
 * Only one of CENTER, LEFT and RIGHT may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#label">Label snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Label extends Control {
	String text;
	Image image;
	boolean isImage;
	NSTextField textView;
	NSImageView imageView;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @see SWT#SEPARATOR
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see SWT#CENTER
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int /*long*/ accessibilityAttributeNames(int /*long*/ id, int /*long*/ sel) {
	if (accessible != null) {		
		if ((textView != null && (id == textView.id || id == textView.cell().id)) || (imageView != null && (id == imageView.id || id == imageView.cell().id))) {
			// See if the accessible will override or augment the standard list.
			// Help, title, and description can be overridden.
			NSMutableArray extraAttributes = NSMutableArray.arrayWithCapacity(3);
			extraAttributes.addObject(OS.NSAccessibilityHelpAttribute);
			extraAttributes.addObject(OS.NSAccessibilityDescriptionAttribute);
			extraAttributes.addObject(OS.NSAccessibilityTitleAttribute);

			for (int i = (int)/*64*/extraAttributes.count() - 1; i >= 0; i--) {
				NSString attribute = new NSString(extraAttributes.objectAtIndex(i).id);
				if (accessible.internal_accessibilityAttributeValue(attribute, ACC.CHILDID_SELF) == null) {
					extraAttributes.removeObjectAtIndex(i);
				}
			}

			if (extraAttributes.count() > 0) {
				int /*long*/ superResult = super.accessibilityAttributeNames(id, sel);
				NSArray baseAttributes = new NSArray(superResult);
				NSMutableArray mutableAttributes = NSMutableArray.arrayWithCapacity(baseAttributes.count() + 1);
				mutableAttributes.addObjectsFromArray(baseAttributes);
				
				for (int i = 0; i < extraAttributes.count(); i++) {
					id currAttribute = extraAttributes.objectAtIndex(i);
					if (!mutableAttributes.containsObject(currAttribute)) {
						mutableAttributes.addObject(currAttribute);
					}
				}
				
				return mutableAttributes.id;
			}
		}
	}

	return super.accessibilityAttributeNames(id, sel);
}

boolean accessibilityIsIgnored(int /*long*/ id, int /*long*/ sel) {
	if (id == view.id) return true;
	return super.accessibilityIsIgnored(id, sel);	
}

void addRelation (Control control) {
	if (!control.isDescribedByLabel ()) return;
	
	if (textView != null) {
		NSObject accessibleElement = control.focusView();
		
		if (accessibleElement instanceof NSControl) {
			NSControl viewAsControl = (NSControl)accessibleElement;
			if (viewAsControl.cell() != null) accessibleElement = viewAsControl.cell();
		}
		
		accessibleElement.accessibilitySetOverrideValue(textView.cell(), OS.NSAccessibilityTitleUIElementAttribute);
		NSArray controlArray = NSArray.arrayWithObject(accessibleElement);
		textView.cell().accessibilitySetOverrideValue(controlArray, OS.NSAccessibilityServesAsTitleForUIElementsAttribute);
	}
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	if ((style & SWT.SEPARATOR) != 0) {
		style = checkBits (style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
		return checkBits (style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
	} 
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = DEFAULT_WIDTH;
	int height = DEFAULT_HEIGHT;
	if ((style & SWT.SEPARATOR) != 0) {
		float /*double*/ lineWidth = ((NSBox)view).borderWidth ();
		if ((style & SWT.HORIZONTAL) != 0) {
			height = (int)Math.ceil (lineWidth * 2);
		} else {
			width = (int)Math.ceil (lineWidth * 2);
		}
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		int border = getBorderWidth ();
		width += border * 2; height += border * 2;
		return new Point (width, height);
	}
	if (isImage) {
		if (image != null) {
			NSImage nsimage = image.handle;
			NSSize size = nsimage.size ();
			width = (int)size.width;
			height = (int)size.height;
		} else {
			width = height = 0;
		}
	} else {
		NSSize size = null;
		if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
			NSRect rect = new NSRect ();
			rect.width = wHint;
			rect.height = hHint != SWT.DEFAULT ? hHint : Float.MAX_VALUE;
			size = textView.cell ().cellSizeForBounds (rect);
		} else {
			size = textView.cell ().cellSize ();
		}
		width = (int)Math.ceil (size.width);
		height = (int)Math.ceil (size.height);
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	state |= THEME_BACKGROUND;
	NSBox widget = (NSBox)new SWTBox().alloc();
	widget.init();
	widget.setTitle(NSString.stringWith(""));
	if ((style & SWT.SEPARATOR) != 0) {
		widget.setBoxType(OS.NSBoxSeparator);
		NSView child = (NSView) new SWTView().alloc().init();
		widget.setContentView(child);
		child.release();
	} else {
		widget.setBorderType(OS.NSNoBorder);
		widget.setBorderWidth (0);
		widget.setBoxType (OS.NSBoxCustom);
		NSSize offsetSize = new NSSize ();
		widget.setContentViewMargins (offsetSize);

		NSImageView imageWidget = (NSImageView) new SWTImageView ().alloc ();
		imageWidget.init();
		imageWidget.setImageScaling (OS.NSScaleNone);
		
		NSTextField textWidget = (NSTextField)new SWTTextField().alloc();
		textWidget.init();
		textWidget.setBordered(false);
		textWidget.setEditable(false);
		textWidget.setDrawsBackground(false);
		NSTextFieldCell cell = new NSTextFieldCell(textWidget.cell());
		cell.setWraps ((style & SWT.WRAP) != 0);
		
		widget.addSubview(imageWidget);
		widget.addSubview(textWidget);
		widget.setContentView(textWidget);
		
		imageView = imageWidget;
		textView = textWidget;
		_setAlignment();
	}
	view = widget;
}

void createWidget() {
	text = "";
	super.createWidget ();
}

NSAttributedString createString() {
	NSAttributedString attribStr = createString(text, null, foreground, (style & SWT.WRAP) == 0 ? style : 0, true, true);
	attribStr.autorelease();
	return attribStr;
}

NSFont defaultNSFont () {
	return display.textFieldFont;
}

void deregister () {
	super.deregister ();
	if (textView != null) {
		display.removeWidget(textView);
		display.removeWidget(textView.cell());
	}
	if (imageView != null) {
		display.removeWidget (imageView);
		display.removeWidget (imageView.cell());
	}
}

NSView eventView () {
	return ((NSBox)view).contentView();
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is a <code>SEPARATOR</code> label, in 
 * which case, <code>NONE</code> is returned.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget();
	return image;
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * a <code>SEPARATOR</code> label.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return "";
	return text;
}

boolean isDescribedByLabel () {
	return false;
}

void register () {
	super.register ();
	if (textView != null) {
		display.addWidget (textView, this);
		display.addWidget (textView.cell(), this);
	}
	if (imageView != null) {
		display.addWidget (imageView, this);
		display.addWidget (imageView.cell(), this);
	}
}

void releaseHandle () {
	super.releaseHandle ();
	if (textView != null) textView.release();
	if (imageView != null) imageView.release();
	textView = null;
	imageView = null;
}

/*
 * Remove "Labeled by" relations from the receiver.
 */
void removeRelation () {
	if (textView != null) {
		textView.cell().accessibilitySetOverrideValue(null, OS.NSAccessibilityServesAsTitleForUIElementsAttribute);
	}
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.  If the receiver is a <code>SEPARATOR</code>
 * label, the argument is ignored and the alignment is not changed.
 *
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	_setAlignment();
}

void updateBackground () {
	if ((style & SWT.SEPARATOR) != 0) return;
	NSColor nsColor = null;
	if (backgroundImage != null) {
		nsColor = NSColor.colorWithPatternImage(backgroundImage.handle);
	} else if (background != null) {
		nsColor = NSColor.colorWithDeviceRed(background[0], background[1], background[2], background[3]);
	} else {
		nsColor = NSColor.clearColor();
	}
	((NSBox)view).setFillColor(nsColor);
}

void _setAlignment() {
	if (image != null) {
		if ((style & SWT.RIGHT) != 0) imageView.setImageAlignment(OS.NSImageAlignRight);
		if ((style & SWT.LEFT) != 0) imageView.setImageAlignment(OS.NSImageAlignLeft);
		if ((style & SWT.CENTER) != 0) imageView.setImageAlignment(OS.NSImageAlignCenter);
	}
	if (text != null) {
		NSCell cell = new NSCell(textView.cell());
		cell.setAttributedStringValue(createString());
	}
}

void setFont(NSFont font) {
	if (textView != null) {
		NSCell cell = new NSCell(textView.cell());
		cell.setAttributedStringValue(createString());
		textView.setFont (font);
	}
}

void setForeground (float /*double*/ [] color) {
	if ((style & SWT.SEPARATOR) != 0) return;
	NSCell cell = new NSCell(textView.cell());
	cell.setAttributedStringValue(createString());
}

boolean setTabItemFocus () {
	return false;
}

/**
 * Sets the receiver's image to the argument, which may be
 * null indicating that no image should be displayed.
 *
 * @param image the image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.image = image;
	isImage = true;

	/*
	 * Feature in Cocoa.  If the NSImage object being set into the view is
	 * the same NSImage object that is already there then the new image is
	 * not taken.  This results in the view's image not changing even if the
	 * NSImage object's content has changed since it was last set into the
	 * view.  The workaround is to temporarily set the view's image to null
	 * so that the new image will then be taken.
	 */
	if (image != null) {
		NSImage current = imageView.image ();
		if (current != null && current.id == image.handle.id) {
			imageView.setImage (null);
		}
	}
	imageView.setImage(image != null ? image.handle : null);
	((NSBox)view).setContentView(imageView);
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic character and line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the control that follows the label. On most platforms,
 * the mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
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
	isImage = false;
	text = string;
	NSCell cell = new NSCell(textView.cell());
	cell.setAttributedStringValue(createString());
	((NSBox)view).setContentView(textView);
}


}

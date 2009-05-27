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
import org.eclipse.swt.graphics.*;
 
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
	TabFolder parent;
	int x,y,width,height = 0;
	String toolTipText;
	Control control;									// the tab page
	
	// internal constants
	static final int LEFT_HORIZONTAL_MARGIN = 8;
	static final int RIGHT_HORIZONTAL_MARGIN = 2;	
	static final int VERTICAL_MARGIN = 1;			// space between tab shadow and tab content
	static final int ICON_MARGIN = 6;
	static final int SHADOW_WIDTH = 2;				// width of the tab shadow
	static final int DEFAULT_TEXT_WIDTH = 36;		// preferred text width if there is no text. 
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
	parent.createChild (this, parent.getItemCount());
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
	parent.createChild (this, index);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public void dispose() {
	if (isDisposed()) return;
	super.dispose();
	parent.destroyChild(this);
	parent = null;
	control = null;
	toolTipText = null;
}
/**
 * Expand the receiver's bounds by the specified number of pixels on 
 * the left,top,right,and bottom.
 */
void expand(int left, int top, int right, int bottom) {
	if (hasLocation()) {
		x = x - left;
		y = y - top;
		width = width + left + right;
		height = height + top + bottom;
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
 * 
 * @since 3.4
 */
public Rectangle getBounds () {
	checkWidget();
	return new Rectangle(x, y, width, height);
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
	checkWidget();
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
	checkWidget();
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
	checkWidget();
	return toolTipText;
}
/**
 * Answer true if the receiver has been laid out.
 */
boolean hasLocation() {
	return !(width == 0 && height == 0 && x == 0 && y == 0);
}
/**
 * Answer the image height.
 */
private int imageHeight() {
	Image image = getImage();
	
	if (parent.getImageHeight() != -1) {
		return parent.getImageHeight();
	} else if (image != null && !image.isDisposed()) {
		return image.getBounds().height;
	}
	else {
		return 0;
	}
}
/**
 * Answer the icon width.
 */
private int imageWidth() {
	Image image = getImage();
	
	if (image != null && !image.isDisposed()) {
		return image.getBounds().width;
	} else {
		return 0;
	}
}
/**
 * Paint the receiver.
 */
void paint(GC gc, boolean isSelected) {
	// high light colored line across left and top 
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
	gc.drawLine(x, y + height - 2, x, y + 2);
	gc.drawLine(x, y + 2, x + 2, y);
	gc.drawLine(x + 2, y, x + width - 3, y);

	// light color next to the left and below the top line.  
	// Since tabs expand horizontally when selected, we actually draw 
	// the background color to erase any debris from a selected tab.
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	gc.drawLine(x + 1, y + height - 2, x + 1, y + 2);
	gc.drawLine(x + 2, y + 1, x + width - 3, y + 1);

	// dark colored line at right
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
	gc.drawLine(x + width - 1, y + 2, x + width - 1, y + height - 1);
	// dark pixel on top of shadowed line, inside dark line
	gc.drawLine(x + width - 2, y + 1, x + width - 2, y + 1);

	// shadowed line on right inside the dark line
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
	gc.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 1);

	if (parent.isFocusControl() && isSelected) {
		// draw a focus rectangle
		gc.drawFocus(x + 3, y + 3, width - 6, height - 3);
	}
		
	// icon and bitmap.  Should probably be checking style bits to determine
	// exactly what to paint.  Do we just draw the icon when the icon/text combination
	// is too wide to fit all tabs?
	gc.setForeground(parent.getForeground());
	int xDraw = x + LEFT_HORIZONTAL_MARGIN;
	int yCenter;
	int decorationHeight = SHADOW_WIDTH * 2 + VERTICAL_MARGIN * 2;
	Image image = getImage();
	Rectangle sourceBounds = null;
	if (image != null) {
		sourceBounds = image.getBounds();
		yCenter = y + SHADOW_WIDTH + VERTICAL_MARGIN + (height - decorationHeight - imageHeight()) / 2;
		gc.drawImage(
			image, 
			sourceBounds.x, sourceBounds.y, sourceBounds.width, sourceBounds.height,
			xDraw, yCenter, sourceBounds.width, parent.getImageHeight());
	}
	xDraw = xDraw + ICON_MARGIN;
	if (sourceBounds != null) {
		xDraw += sourceBounds.width;
	}
	yCenter = y + SHADOW_WIDTH + VERTICAL_MARGIN + (height - decorationHeight - textHeight(gc)) / 2; 	
	int flags = SWT.DRAW_MNEMONIC | SWT.DRAW_TRANSPARENT;
	gc.drawText(getText(), xDraw, yCenter, flags);
}
/**
 * Answer the preferred height of the receiver for the GC.
 */
int preferredHeight(GC gc) {
	int height = textHeight(gc);
	if (imageHeight() > height) height = imageHeight();
	height += VERTICAL_MARGIN * 2 + SHADOW_WIDTH * 2;
	return height;
}
/**
 * Answer the preferred width of the receiver for the GC.
 */
int preferredWidth(GC gc) {
	return imageWidth() + textWidth(gc) + LEFT_HORIZONTAL_MARGIN + 
		RIGHT_HORIZONTAL_MARGIN + ICON_MARGIN + SHADOW_WIDTH * 2;
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
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if (this.control != null && this.control.isDisposed ()) {
		this.control = null;
	}
	Control oldControl = this.control, newControl = control;
	this.control = control;
	int index = parent.indexOf (this), selectionIndex = parent.getSelectionIndex();
	if (index != selectionIndex) {
		if (newControl != null) {
			if (selectionIndex != -1) {
				Control selectedControl = parent.getItem(selectionIndex).getControl();
				if (selectedControl == newControl) return;
			}
			newControl.setVisible(false);
			return;
		}
	}
	if (newControl != null) {
		newControl.setBounds (parent.getClientArea ());
		newControl.setVisible (true);
	}
	if (oldControl != null) oldControl.setVisible (false);
}
public void setImage (Image image) {
	checkWidget();
	Image oldImage = this.image;
	
	super.setImage(image);
	if (image == null || !image.equals(oldImage)) {
		parent.itemChanged(this);
	}
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
	checkWidget();
	String oldText = text;
	
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText(string);
	if (!string.equals(oldText)) {
		parent.itemChanged(this);
	}	
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
}
/**
 * Answer the text height.
 */
private int textHeight(GC gc) {
	String text = getText();
	
	if (text == null) {
		return 0;
	} else {
		int flags = SWT.DRAW_MNEMONIC;
		return gc.textExtent(text, flags).y;
	}
}
/**
 * Answer the text width.
 */
private int textWidth(GC gc) {
	String text = getText();	
	int textWidth = 0;
	
	if (text != null) {
		int flags = SWT.DRAW_MNEMONIC;
		textWidth = gc.textExtent(text, flags).x;
	}
	if (textWidth == 0) {
		textWidth = DEFAULT_TEXT_WIDTH;
	}
	return textWidth;
}
}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
 */

public /*final*/ class TabItem extends Item {
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
													// Used for preferred item width calculation
/**
 * Construct a TabItem with the specified parent and style.
 */
public TabItem (TabFolder parent, int style) {
	this(parent, style, parent.getItemCount());
}
/**
 * Construct a TabItem with the specified parent and style, inserted at
 * the specified index.
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
 * Return the bounds of the TabItem.
 */
Rectangle getBounds () {
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
public Display getDisplay() {
	if (parent == null) error(SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay();
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
 * Answer true if the receiver has been layed out.
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
	} else if (image != null) {
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
	
	if (image != null) {
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
	gc.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
	gc.drawLine(x, y + height - 2, x, y + 2);
	gc.drawLine(x, y + 2, x + 2, y);
	gc.drawLine(x + 2, y, x + width - 3, y);

	// light color next to the left and below the top line.  
	// Since tabs expand horizontally when selected, we actually draw 
	// the background color to erase any debris from a selected tab.
	gc.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	gc.drawLine(x + 1, y + height - 2, x + 1, y + 2);
	gc.drawLine(x + 2, y + 1, x + width - 3, y + 1);

	// dark colored line at right
	gc.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
	gc.drawLine(x + width - 1, y + 2, x + width - 1, y + height - 1);
	// dark pixel on top of shadowed line, inside dark line
	gc.drawLine(x + width - 2, y + 1, x + width - 2, y + 1);

	// shadowed line on right inside the dark line
	gc.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
	gc.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 1);

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
	gc.drawString(getText(), xDraw, yCenter);
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
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget();
	if (control != null && control.parent != parent) {
		error (SWT.ERROR_INVALID_PARENT);
	}
	Control oldControl = this.control, newControl = control;
	this.control = control;
	int index = parent.indexOf (this);
	if (index != parent.getSelectionIndex ()) {
		if (newControl != null) newControl.setVisible(false);
		return;
	}
	if (newControl != null) {
		newControl.setBounds (parent.getClientArea ());
		newControl.setVisible (true);
	}
	if (oldControl != null) oldControl.setVisible (false);
}
/**
* Sets the image.
* <p>
* @param image the new image (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setImage (Image image) {
	checkWidget();
	Image oldImage = this.image;
	
	super.setImage(image);
	if (image == null || !image.equals(oldImage)) {
		parent.itemChanged(this);
	}
}
/**
 * Sets the receiver's text.
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
	String oldText = text;
	
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText(string);
	if (!string.equals(oldText)) {
		parent.itemChanged(this);
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
}
/**
 * Answer the text height.
 */
private int textHeight(GC gc) {
	String text = getText();
	
	if (text == null) {
		return 0;
	} else {
		return gc.stringExtent(text).y;
	}
}
/**
 * Answer the text width.
 */
private int textWidth(GC gc) {
	String text = getText();	
	int textWidth = 0;
	
	if (text != null) {
		textWidth = gc.stringExtent(text).x;
	}
	if (textWidth == 0) {
		textWidth = DEFAULT_TEXT_WIDTH;
	}
	return textWidth;
}
}

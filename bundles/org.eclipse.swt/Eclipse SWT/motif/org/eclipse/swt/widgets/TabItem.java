package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/** 
 * This class emulates a windows TabItem, which represents a tab
 * that can be used to switch to a page in the TabbedFolder.
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
	addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event event) {disposeItem();}});
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
/**
 * Dispose the receiver.
 */
void disposeItem () {
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
* Gets the control.
* <p>
* @return the control
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Control getControl () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return control;
}
/**
 * Answer the display of the receiver's parent widget.
 */
public Display getDisplay() {
	if (parent == null) error(SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay();
}
/**
 * Return the parent of the TabItem.
 */
public TabFolder getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	return parent;
}
/**
* Gets the tool tip text.
* <p>
* @return the tool tip text.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public String getToolTipText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
* Sets the control.
* <p>
* @param control the new control
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setControl (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Image oldImage = this.image;
	
	super.setImage(image);
	if (image == null || !image.equals(oldImage)) {
		parent.itemChanged(this);
	}
}
/**
* Set the widget text.
*
* PARAMETERS
*
* 	string - the new label for the widget
*
* REMARKS
*
*	This method sets the widget label.  The label may include
* the mnemonic characters but must not contain line delimiters.
*
**/
public void setText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	String oldText = text;
	
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText(string);
	if (!string.equals(oldText)) {
		parent.itemChanged(this);
	}	
}
/**
* Sets the tool tip text.
* <p>
* @param string the new tool tip text (or null)
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setToolTipText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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

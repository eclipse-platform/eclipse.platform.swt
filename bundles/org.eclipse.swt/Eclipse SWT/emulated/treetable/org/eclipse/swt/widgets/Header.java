/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
 
/**
 * A header draws one or more header items. Each item may have a text 
 * label. Each item is identified by an index and can be resized.
 */
class Header extends Canvas {
	private static final int DEFAULT_WIDTH = 64;		// used in computeSize if width could not be calculated
	private static final int DEFAULT_HEIGHT = 64;		// used in computeSize if height could not be calculated	
	private static final int VERTICAL_MARGIN = 6;		// space added to the height of the header label		
	private static final int DEFAULT_ITEM_WIDTH = 9;	// default width of a header item
	private static final int HORIZONTAL_MARGIN = 6;		// space in front and behind header text
	
	Table parent;
/**
 * Create a Header widget as a child of 'parent'.
 * @param parent - the parent of the new instance
 */
Header(Table parent) {
	super(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_FOCUS);
	this.parent = parent;
	addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event event) {paint(event);}
	});
	setHeaderHeight();
}
/**
 * Answer the size of the receiver needed to display all items.
 */
public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0;
	int height = 0;

	for (int i = 0; i < getItemCount(); i++) {
		width += getBounds(i).width;
		if (height == 0) {
			height = getBounds(i).height;
		}
	}
	if (width == 0) {
		width = DEFAULT_WIDTH;
	}
	if (height == 0) {
		height = DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) {
		width = wHint;
	}
	if (hHint != SWT.DEFAULT) {
		height = hHint;
	}
	return new Point(width, height);
}
/**
 * Draw the bright shadow on the upper and left sides of a header item.
 * @param gc - GC to draw on
 * @param itemIndex - specifies the item to draw
 */
void drawHighlightShadow(GC gc, int itemIndex) {
	Rectangle bounds = getBounds(itemIndex);
	Color oldForeground = getForeground();

	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));	
	// draw top horizontal line
	gc.drawLine(bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y);
	// draw left vertical line
	gc.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
	gc.setForeground(oldForeground);
}
/**
 * Draw the dark shadow on the lower and right side of a header item.
 * @param gc - GC to draw on
 * @param itemIndex - specifies the item to draw
 */
void drawLowlightShadows(GC gc, int itemIndex) {
	Rectangle bounds = getBounds(itemIndex);
	Point bottomShadowStart = new Point(bounds.x + 1, bounds.height - 2);
	Point bottomShadowStop = new Point(bottomShadowStart.x + bounds.width - 2, bottomShadowStart.y);	
	Point rightShadowStart = null;
	Point rightShadowStop = null;
	Color oldForeground = getForeground();	

	// light inner shadow
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
	gc.drawLine(
		bottomShadowStart.x, bottomShadowStart.y,
		bottomShadowStop.x, bottomShadowStop.y);
	if(itemIndex != TableColumn.FILL) {
		rightShadowStart = new Point(bounds.x + bounds.width - 2, bounds.y + 1);
		rightShadowStop = new Point(rightShadowStart.x, bounds.height - 2);
		gc.drawLine(
			rightShadowStart.x, rightShadowStart.y,
			rightShadowStop.x, rightShadowStop.y);
	}	
	// dark outer shadow 
	bottomShadowStart.x--;
	bottomShadowStart.y++;
	bottomShadowStop.y++;
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
	gc.drawLine(
		bottomShadowStart.x, bottomShadowStart.y,
		bottomShadowStop.x, bottomShadowStop.y);
	if(itemIndex != TableColumn.FILL) {
		rightShadowStart.x++;
		rightShadowStart.y--;
		rightShadowStop.y++;
		rightShadowStop.x++;	
		gc.drawLine(
			rightShadowStart.x, rightShadowStart.y,
			rightShadowStop.x, rightShadowStop.y);
	}	
	gc.setForeground(oldForeground);
}
/**
 * Answer the bounding rectangle of the item identified by 'itemIndex'.
 * @param itemIndex - specifies the item whose bounding rectangle 
 *	should be returned.
 * @return the bouding rectangle of the item identified by 'itemIndex'.
 */
Rectangle getBounds(int itemIndex) {
	Rectangle bounds = null;
	int itemCount = getItemCount();

	if (itemIndex >= 0 && itemIndex < itemCount) {
		bounds = parent.internalGetColumn(itemIndex).getBounds();
		bounds.y = 0;
		bounds.height = getBounds().height;
	}
	else
	if (itemIndex == TableColumn.FILL) {
		if (itemCount > 0) {
			bounds = parent.internalGetColumn(itemCount - 1).getBounds();
			bounds.x += bounds.width;
		}
		else {
			bounds = new Rectangle(0, 0, 0, 0);
		}
		bounds.width = Math.max(0, getBounds().width - bounds.x);
		bounds.y = 0;
		bounds.height = getBounds().height;		
	}
	return bounds;
}
/**
 * Answer the image that is going to be drawn in the header item 
 * identified by 'itemIndex'. 
 * @param gc - GC to use for measuring the label width.
 * @param itemIndex - specifies the item whose label should be returned.
 */
Image getImage(int itemIndex) {
	if (itemIndex >= 0 && itemIndex < getItemCount()) {
		return parent.internalGetColumn(itemIndex).getImage();
	}
	return null;
}
/**
 * Answer the size of item images. 
 */
Point getImageExtent(){
	Image image = null;
	int labelCount = getItemCount();
	for (int i = 0; i < labelCount && image==null; i++) {
		image = getImage(i);
	}
	if (image!= null){
		return new Point(image.getBounds().width,image.getBounds().height);
	}
	return 	new Point(0, 0);
}
/**
 * Answer the number of items in the receiver.
 */
int getItemCount() {
	return parent.internalGetColumnCount();
}
/**
 * Answer the width required to display the complete label of the header 
 * item at position 'index'.
 * @param index - position of the header item whose preferred width should 
 *	be returned.
 */
int getPreferredWidth(int index) {
	Image image = getImage(index);
	String text = getText(index);
	
	int headerWidth = HORIZONTAL_MARGIN;

	if (image != null){
		headerWidth += getImageExtent().x + HORIZONTAL_MARGIN;
	}
	if (text != null) {
		headerWidth += getTextWidth(text) + HORIZONTAL_MARGIN;
	}	
	return headerWidth;
}
/**
 * Answer the label of the item identified by 'itemIndex'.
 */
String getText(int itemIndex) {
	String itemLabel = null;
	
	if (itemIndex >= 0 && itemIndex < getItemCount()) {
		itemLabel = parent.internalGetColumn(itemIndex).getText();
	}
	return itemLabel;
}
/**
 * Answer the width of 'text' in pixel.
 * Answer 0 if 'text' is null.
 */
int getTextWidth(String text) {
	int textWidth = 0;
	if (text != null) {
		GC gc = new GC(parent);
		//gc.setFont(parent.getFont());
		textWidth = gc.stringExtent(text).x;
		gc.dispose();
	}
	return textWidth;
}
/**
 * Draw the header item identified by 'itemIndex'.
 * @param gc - GC to draw on
 * @param itemIndex - item that should be drawn
 */
void paint(GC gc, int itemIndex) {
	gc.setFont(parent.getFont());
	Rectangle bounds = getBounds(itemIndex);
	// draw header background
	gc.fillRectangle(bounds.x, bounds.y + 1, bounds.width, bounds.height - 3);
	
	if (itemIndex != TableColumn.FILL) {
		int extent = Math.min(bounds.width - 2*HORIZONTAL_MARGIN, getPreferredWidth(itemIndex));
			
		int x = bounds.x;
		int alignment = parent.internalGetColumn(itemIndex).getAlignment();	
		if ((alignment & SWT.CENTER) != 0) {
			x += (bounds.width - extent) / 2;
		}
		else if ((alignment & SWT.RIGHT) != 0) {
			x += bounds.width - extent - HORIZONTAL_MARGIN;
		} else {
			x += HORIZONTAL_MARGIN;
		}
		
		Image image = getImage(itemIndex);
		if (image != null) {
			Rectangle imageBounds = image.getBounds();
			Point imageExtent = getImageExtent();
			int y = bounds.y + (bounds.height - imageExtent.y) / 2;
			gc.drawImage(image, 0, 0, imageBounds.width, imageBounds.height, 
			                    x, y, imageExtent.x, imageExtent.y);
			x += imageExtent.x + HORIZONTAL_MARGIN;
		}
		String label = getText(itemIndex);
		if (label != null) {
			int maxWidth = bounds.x + bounds.width - x - HORIZONTAL_MARGIN;
			String trimLabel = parent.trimItemText(label, maxWidth, gc);
			Point textExtent = gc.stringExtent(trimLabel);
			int y = bounds.y + (bounds.height - textExtent.y) / 2;
			gc.drawString(trimLabel, x, y);
		}
	}
	
	drawHighlightShadow(gc, itemIndex);
	drawLowlightShadows(gc, itemIndex);	
}
/**
 * Draw all header items.
 * @param event - Paint event triggering the drawing operation.
 */
void paint(Event event) {
	int labelCount = getItemCount();

	for (int i = 0; i < labelCount; i++) {
		paint(event.gc, i);
	}
	paint(event.gc, TableColumn.FILL);					// paint empty fill item behind last item
}
/**
 * Redraw the item identified by 'itemIndex'.
 * @param itemIndex - specifies the header item that should be redrawn
 */
void redraw(int itemIndex) {
	Rectangle bounds = getBounds(itemIndex);

	if (bounds != null) {
		redraw(bounds.x, 0, bounds.width, bounds.height, false);
	}
}

/**
 * Set a new font. Recalculate the header height and redraw the header.
 */
public void setFont(Font font) {
	checkWidget();

	if (font == null || font.equals(getFont()) == true) {
		return;
	}
	super.setFont(font);
	setHeaderHeight();
	redraw();
}
/**
 * Calculate and store the height of the receiver.
 */
void setHeaderHeight() {
	int textHeight = parent.getFontHeight() + VERTICAL_MARGIN; 
	int imageHeight = getImageExtent().y + VERTICAL_MARGIN;		
	Rectangle bounds = getBounds();
	bounds.height = Math.max(textHeight,imageHeight);
	setBounds(bounds);	
}
/**
 * The width of the header item at position 'itemIndex' is about to change.
 * Adjust the width of the header. Scroll and redraw all header items
 * starting behind the item identified by 'itemIndex'.
 * @param itemIndex - specifies the item after which the redraw
 *	should begin.
 * @param widthDiff - the width change of the item. 
 *	> 0 = item width increased. < 0 = item width decreased
 */
void widthChange(int itemIndex, int widthDiff) {
	Rectangle bounds = getBounds(itemIndex);
	Rectangle headerBounds = getBounds();
	
	if (bounds != null) {
		if (itemIndex != TableColumn.FILL) {						// ignore the fill column header item - there's nothing to redraw anyway
			scroll(
				bounds.x + bounds.width + widthDiff, 0,				// destination x, y
				bounds.x + bounds.width, 0,							// source x, y
				headerBounds.width + widthDiff, headerBounds.height, false);
			redraw(bounds.x, 0, bounds.width, bounds.height, false);
		}
	}
	headerBounds.width += widthDiff;
	setBounds(headerBounds);
}

}

package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
 
/**
 * A header draws one or more header items. Each item may have a text 
 * label. Each item is identified by an index and can be resized.
 */
class Header extends Canvas {
	private static final int DEFAULT_WIDTH = 64;		// used in computeSize if width could not be calculated
	private static final int DEFAULT_HEIGHT = 64;		// used in computeSize if height could not be calculated	
	private static final int VERTICAL_MARGIN = 4;		// space added to the height of the header label		
	private static final int TEXT_Y_OFFSET = 2;			// space between the header label and the lower header boundary
	private static final int DEFAULT_ITEM_WIDTH = 9;	// default width of a header item
	private static final int TEXT_MARGIN = 6;			// space in front and behind header text
	private static final int SHADOW_WIDTH = 2;			// width of the right side header shadow
/**
 * Create a Header widget as a child of 'parent'.
 * @param parent - the parent of the new instance
 */
Header(Table parent) {
	super(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_FOCUS);
	
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

	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));	
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
	Display display = getDisplay();
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
 * Draw the item text of the item identified by 'itemIndex'.
 * @param gc - GC to draw on
 * @param itemIndex - specifies the item to draw
 */
void drawText(GC gc, int itemIndex) {
	String label = getText(gc, itemIndex);
	Point textExtent;
	Rectangle bounds = getBounds(itemIndex);
	int yPosition;
	int xPosition = 0;
	int alignment;

	if (label != null) {
		alignment = ((Table) getParent()).internalGetColumn(itemIndex).getAlignment();
		textExtent = gc.stringExtent(label);
		yPosition = bounds.height - textExtent.y - TEXT_Y_OFFSET;

		if ((alignment & SWT.CENTER) != 0) {
			xPosition = (bounds.width - textExtent.x) / 2;
		}
		else
		if ((alignment & SWT.RIGHT) != 0) {
			xPosition = bounds.width - textExtent.x - TEXT_MARGIN;
		}
		xPosition = Math.max(TEXT_MARGIN, xPosition);
		xPosition += bounds.x;
		gc.drawString(label, xPosition, yPosition);
	}	
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
	Table parent = (Table) getParent();

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
 * Answer the number of items in the receiver.
 */
int getItemCount() {
	return ((Table) getParent()).internalGetColumnCount();
}
/**
 * Answer the maximum label width that fits into the item identified by
 * 'itemIndex'.
 */
int getMaxLabelWidth(int itemIndex) {
	return getBounds(itemIndex).width - 2 * TEXT_MARGIN;	
}
/**
 * Answer the width required to display the complete label of the header 
 * item at position 'index'.
 * @param index - position of the header item whose preferred width should 
 *	be returned.
 */
int getPreferredWidth(int index) {
	Table parent = (Table) getParent();
	String text = getText(index);
	int headerWidth = 0;

	if (text != null) {
		headerWidth = parent.getTextWidth(text) + 2 * TEXT_MARGIN + 1;
	}	
	return headerWidth;
}
/**
 * Answer the label of the item identified by 'itemIndex'.
 */
String getText(int itemIndex) {
	String itemLabel = null;
	
	if (itemIndex >= 0 && itemIndex < getItemCount()) {
		itemLabel = ((Table) getParent()).internalGetColumn(itemIndex).getText();
	}
	return itemLabel;
}
/**
 * Answer the text that is going to be drawn in the header item 
 * identified by 'itemIndex'. This may be truncated to fit the item
 * width.
 * @param gc - GC to use for measuring the label width.
 * @param itemIndex - specifies the item whose label should be returned.
 */
String getText(GC gc, int itemIndex) {
	String label = getText(itemIndex);
	int maxWidth;

	if (label != null) {
		maxWidth = getMaxLabelWidth(itemIndex);
		label = ((Table) getParent()).trimItemText(label, maxWidth, gc);
	}
	return label;
}
/**
 * Draw the header item identified by 'itemIndex'.
 * @param gc - GC to draw on
 * @param itemIndex - item that should be drawn
 */
void paint(GC gc, int itemIndex) {
	Rectangle bounds = getBounds(itemIndex);

	// draw header background
	gc.fillRectangle(bounds.x, bounds.y + 1, bounds.width, bounds.height - 3);
	if (itemIndex != TableColumn.FILL) {
		drawText(gc, itemIndex);
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
	GC gc = new GC(this);
	Rectangle bounds = getBounds();

	bounds.height = gc.stringExtent("aString").y + VERTICAL_MARGIN;
	setBounds(bounds);
	gc.dispose();
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

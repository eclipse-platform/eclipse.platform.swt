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
package org.eclipse.swt.custom;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;


/**
* DO NOT USE - UNDER CONSTRUCTION
*
* @ since 3.0
*/

public class CTabItem2 extends Item {
	CTabFolder2 parent;
	int x,y,width,height = 0;
	Control control; // the tab page
	
	String toolTipText;
	String shortenedText;
	int shortenedTextWidth;
	
	// Appearance
	Color foreground;
	Font font;
	Color background;
	Image bgImage;
	Color[] gradientColors;
	int[] gradientPercents;
	boolean gradientVertical;
	
	Rectangle closeRect = new Rectangle(0, 0, 0, 0);
	int closeImageState = CTabFolder2.NONE;
	boolean showClose = false;
	
	// internal constants
	static final int LEFT_MARGIN = 7;
	static final int RIGHT_MARGIN = 6;
	static final int TOP_MARGIN = 2;
	static final int BOTTOM_MARGIN = 2;
	static final int INTERNAL_SPACING = 2;
	static final int FLAGS = SWT.DRAW_TRANSPARENT | SWT.DRAW_MNEMONIC;
	static final String ellipsis = "..."; //$NON-NLS-1$
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CTabFolder</code>) and a style value
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
 * @param parent a CTabFolder which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#getStyle()
 */
public CTabItem2 (CTabFolder2 parent, int style) {
	this(parent, style, parent.getItemCount());
}
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>CTabFolder</code>), a style value
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
 * @param parent a CTabFolder which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#getStyle()
 */
public CTabItem2 (CTabFolder2 parent, int style, int index) {
	super (parent, checkStyle(style));
	showClose = (style & SWT.CLOSE) != 0;
	parent.createItem (this, index);
}
static int checkStyle(int style) {
	return SWT.NONE;
}
static String shortenText(GC gc, String text, int width) {
	if (gc.textExtent(text, FLAGS).x <= width) return text;
	
	int ellipseWidth = gc.textExtent(ellipsis, FLAGS).x;
	int length = text.length();
	int end = length - 1;
	while (end > 0) {
		text = text.substring(0, end);
		int l1 = gc.textExtent(text, FLAGS).x;
		if (l1 + ellipseWidth <= width) {
			return text + ellipsis;
		}
		end--;
	}
	return text + ellipsis;
}
public void dispose() {
	if (isDisposed ()) return;
	//if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	parent.destroyItem(this);
	super.dispose();
	parent = null;
	control = null;
	toolTipText = null;
	shortenedText = null;
	foreground = null;
	font = null;
	background = null;
	bgImage = null;
	gradientColors = null;
	gradientPercents = null;
}
void drawClose(GC gc) {
	if (closeRect.width == 0 || closeRect.height == 0) return;
	Display display = getDisplay();

	// draw X 9x9
	int indent = Math.max(1, (CTabFolder2.BUTTON_SIZE-9)/2);
	int x = closeRect.x + indent;
	int y = closeRect.y + indent;
	y += parent.onBottom ? -1 : 1;
	
	switch (closeImageState) {
		case CTabFolder2.NORMAL: {
			int[] shape = new int[] {x,y, x+2,y, x+4,y+2, x+5,y+2, x+7,y, x+9,y, 
					                 x+9,y+2, x+7,y+4, x+7,y+5, x+9,y+7, x+9,y+9,
			                         x+7,y+9, x+5,y+7, x+4,y+7, x+2,y+9, x,y+9,
			                         x,y+7, x+2,y+5, x+2,y+4, x,y+2};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			gc.setForeground(CTabFolder2.borderColor);
			gc.drawPolygon(shape);
			break;
		}
		case CTabFolder2.HOT: {
			int[] shape = new int[] {x,y, x+2,y, x+4,y+2, x+5,y+2, x+7,y, x+9,y, 
					                 x+9,y+2, x+7,y+4, x+7,y+5, x+9,y+7, x+9,y+9,
			                         x+7,y+9, x+5,y+7, x+4,y+7, x+2,y+9, x,y+9,
			                         x,y+7, x+2,y+5, x+2,y+4, x,y+2};
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(shape);
			Color border = new Color(display, CTabFolder2.CLOSE_BORDER);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			border.dispose();
			break;
		}
		case CTabFolder2.SELECTED: {
			int[] shape = new int[] {x+1,y+1, x+3,y+1, x+5,y+3, x+6,y+3, x+8,y+1, x+10,y+1, 
					                 x+10,y+3, x+8,y+5, x+8,y+6, x+10,y+8, x+10,y+10,
			                         x+8,y+10, x+6,y+8, x+5,y+8, x+3,y+10, x+1,y+10,
			                         x+1,y+8, x+3,y+6, x+3,y+5, x+1,y+3};
			Color fill = new Color(display, CTabFolder2.CLOSE_FILL);
			gc.setBackground(fill);
			gc.fillPolygon(shape);
			fill.dispose();
			Color border = new Color(display, CTabFolder2.CLOSE_BORDER);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			border.dispose();
			break;
		}
		case CTabFolder2.NONE: {
			int[] shape = new int[] {x,y, x+10,y, x+10,y+10, x,y+10};
			if (background != null || bgImage != null || gradientColors != null) {
				Color defaultBackground = background != null ? background : parent.getBackground();
				parent.drawBackground(gc, shape, defaultBackground, bgImage, gradientColors, gradientPercents, gradientVertical);
			}else {
				parent.drawBackground(gc, shape, false);
			}
			break;
		}
	}
}
void drawSelected(GC gc ) {
	Point size = parent.getSize();
	
	// Draw selection border across all tabs
	int xx = parent.borderLeft;
	int yy = parent.onBottom ? size.y - parent.borderBottom - parent.tabHeight - CTabFolder2.HIGHLIGHT_HEADER : parent.borderTop + parent.tabHeight + 1;
	int ww = size.x - parent.borderLeft - parent.borderRight;
	int hh = CTabFolder2.HIGHLIGHT_HEADER - 1;
	int[] shape = new int[] {xx,yy, xx+ww,yy, xx+ww,yy+hh, xx,yy+hh};
	parent.drawBackground(gc, shape, true);

	int rightTabEdge = parent.getRightItemEdge();
	// if selected tab scrolled out of view or partially out of view
	// just draw bottom line
	if (!parent.single && parent.selectedIndex != parent.topTabIndex && x + width > rightTabEdge){
		int x1 = Math.max(0, parent.borderLeft - 1);
		int y1 = (parent.onBottom) ? y - 1 : y + height;
		int x2 = size.x - parent.borderRight;
		gc.setForeground(CTabFolder2.borderColor);
		gc.drawLine(x1, y1, x2, y1);
		return;
	}
	
	// draw selected tab background and outline
	int extra = CTabFolder2.CURVE_WIDTH/2 + 4; // +4 to avoid overlapping with text in next tab
	shape = null;
	if (this.parent.onBottom) {
		int[] left = CTabFolder2.BOTTOM_LEFT_CORNER;
		int[] right = parent.curve;
		shape = new int[left.length+right.length+8];
		int index = 0;
		shape[index++] = x; // first point repeated here because below we reuse shape to draw outline
		shape[index++] = y - 1;
		shape[index++] = x;
		shape[index++] = y - 1;
		for (int i = 0; i < left.length/2; i++) {
			shape[index++] = x + left[2*i];
			shape[index++] = y + height + left[2*i+1] - 1;
		}
		for (int i = 0; i < right.length/2; i++) {
			shape[index++] = x + width - extra + right[2*i];
			shape[index++] = y + right[2*i+1] - 2;
		}
		int temp = 0;
		for (int i = 0; i < shape.length/2; i++) {
			if (shape[2*i] > rightTabEdge) {
				if (temp == 0 && i > 0) {
					temp = shape[2*i-1];
				} else {
					temp = y - 1;
				}
				shape[2*i] = rightTabEdge;
				shape[2*i+1] = temp;
			}
		}
		shape[index++] = rightTabEdge;
		shape[index++] = y - 1;
		shape[index++] = x + width + extra;
		shape[index++] = y - 1;
	} else {
		int[] left = CTabFolder2.TOP_LEFT_CORNER;
		int[] right = parent.curve;
		shape = new int[left.length+right.length+8];
		int index = 0;
		shape[index++] = x; // first point repeated here because below we reuse shape to draw outline
		shape[index++] = y + height + 1;
		shape[index++] = x;
		shape[index++] = y + height + 1;
		for (int i = 0; i < left.length/2; i++) {
			shape[index++] = x + left[2*i];
			shape[index++] = y + left[2*i+1];
		}
		for (int i = 0; i < right.length/2; i++) {
			shape[index++] = x + width - extra + right[2*i];
			shape[index++] = y + right[2*i+1];
		}
		int temp = 0;
		for (int i = 0; i < shape.length/2; i++) {
			if (shape[2*i] > rightTabEdge) {
				if (temp == 0 && i > 0) {
					temp = shape[2*i-1];
				} else {
					temp = y + height + 1;
				}
				shape[2*i] = rightTabEdge;
				shape[2*i+1] = temp;
			}
		}
		shape[index++] = rightTabEdge;
		shape[index++] = y + height + 1;
		shape[index++] = x + width + extra;
		shape[index++] = y + height + 1;
	}
	parent.drawBackground(gc, shape, true);
	
	if ( x < rightTabEdge) {
		// Limit drawing area of tab
		Region r = new Region();
		r.subtract(r); //clear
		Region clipping = new Region();
		gc.getClipping(clipping);
		r.add(clipping);
		r.intersect(new Rectangle(x, y, Math.min(width, rightTabEdge-x), height));
		gc.setClipping(r);
	
		// draw Image
		int xDraw = x + LEFT_MARGIN;
		Image image = getImage();
		if (image != null) {
			Rectangle imageBounds = image.getBounds();
			int imageX = xDraw;
			int imageHeight = imageBounds.height;
			int imageY = y + (height - imageHeight) / 2;
			imageY += parent.onBottom ? -1 : 1;
			int imageWidth = imageBounds.width * imageHeight / imageBounds.height;
			gc.drawImage(image, 
				         imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height,
				         imageX, imageY, imageWidth, imageHeight);
			xDraw += imageWidth + INTERNAL_SPACING;
		}
		
		// draw Text
		int textWidth = x + width - xDraw - RIGHT_MARGIN;
		if (closeRect.width > 0) textWidth -= closeRect.width + INTERNAL_SPACING;
		Font gcFont = gc.getFont();
		if (font != null) {
			gc.setFont(font);
		}
		if (shortenedText == null || shortenedTextWidth != textWidth) {
			shortenedText = shortenText(gc, getText(), textWidth);
			shortenedTextWidth = textWidth;
		}
		Point extent = gc.textExtent(shortenedText, FLAGS);	
		int textY = y + (height - extent.y) / 2;
		textY += parent.onBottom ? -1 : 1;
		
		gc.setForeground(parent.selectionForeground);
		gc.drawText(shortenedText, xDraw, textY, FLAGS);
		gc.setFont(gcFont);
		
		if (parent.showClose || showClose) drawClose(gc);
		
		// draw a Focus rectangle
		if (parent.isFocusControl()) {
			Display display = getDisplay();
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.drawFocus(xDraw-3, textY-2, extent.x+6, extent.y+4);
		}
		
		gc.setClipping(clipping);
		r.dispose();
		clipping.dispose();
	}
	
	// draw outline
	shape[0] = Math.max(0, parent.borderLeft - 1);
	shape[shape.length - 2] = size.x - parent.borderRight + 1;
	for (int i = 0; i < shape.length/2; i++) {
		if (shape[2*i + 1] == y + height + 1) shape[2*i + 1] -= 1;
	}
	RGB inside = parent.selectionBackground.getRGB();
	if (parent.selectionBgImage != null || 
	    (parent.selectionGradientColors != null && parent.selectionGradientColors.length > 1)) {
	    inside = null;
	}
	RGB outside = parent.getBackground().getRGB();		
	if (parent.bgImage != null || 
	    (parent.gradientColors != null && parent.gradientColors.length > 1)) {
	    outside = null;
	}
	parent.antialias(shape, CTabFolder2.borderColor.getRGB(), inside, outside, gc);
	gc.setForeground(CTabFolder2.borderColor);
	gc.drawPolyline(shape);
}
void drawUnselected(GC gc) {
	int rightTabEdge = parent.getRightItemEdge();
	// Do not draw partial items
	if (parent.items[parent.topTabIndex] != this && x + width > rightTabEdge){
		return;
	}
	if (background != null || bgImage != null || gradientColors != null) {
		int x1 = x, y1 = parent.onBottom ? y : y+1;
		int x2 = x + width, y2 = parent.onBottom ? y+height-1 : y+height;
		int index = parent.indexOf(this);
		if (!parent.single && parent.selectedIndex != -1) {
			if (parent.selectedIndex + 1 == index) {
				x1 -= CTabFolder2.CURVE_WIDTH/2;
			}
			if (parent.selectedIndex - 1 == index) {
				x2 += CTabFolder2.CURVE_WIDTH/2;
			}
		}
		int[] shape = null;
		if (index == parent.topTabIndex) {
			if (parent.borderLeft != 0) x1 += 1;
			int[] left = parent.onBottom ? CTabFolder2.BOTTOM_LEFT_CORNER : CTabFolder2.TOP_LEFT_CORNER;
			shape = new int[left.length+6];
			int i = 0;
			shape[i++] = x1;
			shape[i++] = parent.onBottom ? y1 : y2;
			for (int j = 0; j < left.length/2; j++) {
				shape[i++] = x1 + left[2*j];
				shape[i++] = parent.onBottom ? y2 + left[2*j+1] : y1 + left[2*j+1] - 1;
				if (parent.borderLeft == 0) shape[i-1] +=  parent.onBottom ? 1 : -1;
			}
			shape[i++] = x2;
			shape[i++] = parent.onBottom ? y2 : y1;
			shape[i++] = x2;
			shape[i++] = parent.onBottom ? y1 : y2;
		} else {
			shape = new int[] {x1,y1, x2,y1, x2,y2, x1,y2};
		}
		Color defaultBackground = background != null ? background : parent.getBackground();
		parent.drawBackground(gc, shape, defaultBackground, bgImage, gradientColors, gradientPercents, gradientVertical);
	}
	// draw border
	if (parent.indexOf(this) != parent.selectedIndex - 1) {
		gc.setForeground(CTabFolder2.borderColor);
		gc.drawLine(x + width - 1, y, x + width - 1, y + height);
	}
	// draw Text
	int textWidth = width - LEFT_MARGIN - RIGHT_MARGIN;
	if (closeRect.width > 0) textWidth -= closeRect.width + INTERNAL_SPACING;
	Font gcFont = gc.getFont();
	if (font != null) {
		gc.setFont(font);
	}
	if (shortenedText == null || shortenedTextWidth != textWidth) {
		shortenedText = shortenText(gc, getText(), textWidth);
		shortenedTextWidth = textWidth;
	}	
	Point extent = gc.textExtent(shortenedText, FLAGS);
	int textY = y + (height - extent.y) / 2;
	textY += parent.onBottom ? -1 : 1;
	gc.setForeground(foreground != null ? foreground : parent.getForeground());
	gc.drawText(shortenedText, x + LEFT_MARGIN, textY, FLAGS);
	gc.setFont(gcFont);
	// draw close
	if (parent.showClose || showClose) drawClose(gc);
}
/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	//checkWidget();
	return new Rectangle(x, y, width, height);
}
/**
* Gets the control that is displayed in the content are of the tab item.
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
 * Get the image displayed in the tab if the tab is disabled.
 * 
 * @return the disabled image or null
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @deprecated
 */
public Image getDisabledImage(){
	//checkWidget();
	return null;
}
/**
 * Returns the receiver's parent, which must be a <code>CTabFolder</code>.
 *
 * @return the receiver's parent
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CTabFolder2 getParent () {
	//checkWidget();
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
	if (toolTipText == null && shortenedText != null) {
		String text = getText();
		if (!shortenedText.equals(text)) return text;
	}
	return toolTipText;
}
void onPaint(GC gc, boolean isSelected) {
	if (width == 0 || height == 0) return;
	if (isSelected) {
		drawSelected(gc);
	} else {
		drawUnselected(gc);
	}
}
int preferredHeight(GC gc) {
	Image image = getImage();
	int h = (image == null) ? 0 : image.getBounds().height;
	String text = getText();
	if (font == null) {
		h = Math.max(h, gc.textExtent(text, FLAGS).y);
	} else {
		Font gcFont = gc.getFont();
		gc.setFont(font);
		h = Math.max(h, gc.textExtent(text, FLAGS).y);
		gc.setFont(gcFont);
	}
	return h + TOP_MARGIN + BOTTOM_MARGIN;
}
int preferredWidth(GC gc, boolean isSelected) {
	int w = 0;
	Image image = getImage();
	if (isSelected && image != null) w += image.getBounds().width;
	String text = getText();
	if (text != null) {
		if (w > 0) w += INTERNAL_SPACING;
		if (font == null) {
			w += gc.textExtent(text, FLAGS).x;
		} else {
			Font gcFont = gc.getFont();
			gc.setFont(font);
			w += gc.textExtent(text, FLAGS).x;
			gc.setFont(gcFont);
		}
	}
	if (parent.showClose || showClose) {
		if (w > 0) w += INTERNAL_SPACING;
		w += CTabFolder2.BUTTON_SIZE;
	}
	if (isSelected) w += 8; // why 8?
	return w + LEFT_MARGIN + RIGHT_MARGIN;
}
/**
 * Sets the background color at the given column index in the receiver 
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 * 
 */
public void setBackground(Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	background = color;
	int edge = parent.getRightItemEdge();
	int index = parent.indexOf(this);
	if (x + width < edge && parent.selectedIndex != index) {
		parent.redraw(x, y, width, height, false);
	}
}
/**
 * Specify a gradient of colours to be draw in the background of the unselected tab.
 * For example to draw a vertical gradient that varies from dark blue to blue and then to
 * white, use the following call to setBackground:
 * <pre>
 *	cfolder.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE), 
 *		                           display.getSystemColor(SWT.COLOR_BLUE),
 *		                           display.getSystemColor(SWT.COLOR_WHITE), 
 *		                           display.getSystemColor(SWT.COLOR_WHITE)},
 *		                  new int[] {25, 50, 100}, true);
 * </pre>
 *
 * @param colors an array of Color that specifies the colors to appear in the gradient 
 *               in order of appearance left to right.  The value <code>null</code> clears the
 *               background gradient. The value <code>null</code> can be used inside the array of 
 *               Color to specify the background color.
 * @param percents an array of integers between 0 and 100 specifying the percent of the width 
 *                 of the widget at which the color should change.  The size of the percents array must be one 
 *                 less than the size of the colors array.
 * 
 * @param vertical indicate the direction of the gradient.  True is vertical and false is horizontal. 
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 *@since 3.0
 */
public void setBackground(Color[] colors, int[] percents, boolean vertical) {
	checkWidget();
	if (colors != null) {
		if (percents == null || percents.length != colors.length - 1) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int i = 0; i < percents.length; i++) {
			if (percents[i] < 0 || percents[i] > 100) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (i > 0 && percents[i] < percents[i-1]) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		if (getDisplay().getDepth() < 15) {
			// Don't use gradients on low color displays
			colors = new Color[] {colors[0]};
			percents = new int[] {};
		}
	}
	
	// Are these settings the same as before?
	if (bgImage == null) {
		if ((gradientColors != null) && (colors != null) && 
			(gradientColors.length == colors.length)) {
			boolean same = false;
			for (int i = 0; i < gradientColors.length; i++) {
				if (gradientColors[i] == null) {
					same = colors[i] == null;
				} else {
					same = gradientColors[i].equals(colors[i]);
				}
				if (!same) break;
			}
			if (same) {
				for (int i = 0; i < gradientPercents.length; i++) {
					same = gradientPercents[i] == percents[i];
					if (!same) break;
				}
			}
			if (same && this.gradientVertical == vertical) return;
		}
	} else {
		bgImage = null;
	}
	// Store the new settings
	if (colors == null) {
		gradientColors = null;
		gradientPercents = null;
		gradientVertical = false;
		setBackground((Color)null);
	} else {
		gradientColors = new Color[colors.length];
		for (int i = 0; i < colors.length; ++i) {
			gradientColors[i] = colors[i];
		}
		gradientPercents = new int[percents.length];
		for (int i = 0; i < percents.length; ++i) {
			gradientPercents[i] = percents[i];
		}
		gradientVertical = vertical;
		setBackground(gradientColors[gradientColors.length-1]);
	}

	// Refresh with the new settings
	int edge = parent.getRightItemEdge();
	int index = parent.indexOf(this);
	if (x + width < edge && parent.selectedIndex != index) {
		parent.redraw(x, y, width, height, false);
	}
}

/**
 * Set the image to be drawn in the background of the unselected tab.  Image
 * is stretched or compressed to cover entire unselected tab area.
 * 
 * @param image the image to be drawn in the background
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setBackground(Image image) {
	checkWidget();
	if (image == bgImage) return;
	if (image != null) {
		gradientColors = null;
		gradientPercents = null;
	}
	bgImage = image;
	int edge = parent.getRightItemEdge();
	int index = parent.indexOf(this);
	if (x + width < edge && parent.selectedIndex != index) {
		parent.redraw(x, y, width, height, false);
	}
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
		if (control.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.getParent() != parent) SWT.error (SWT.ERROR_INVALID_PARENT);
	}
	if (this.control != null && !this.control.isDisposed()) {
		this.control.setVisible(false);
	}
	this.control = control;
	if (this.control != null) {
		int index = parent.indexOf (this);
		if (index == parent.getSelectionIndex ()){
			this.control.setBounds(parent.getClientArea ());
			this.control.setVisible(true);
		} else {
			this.control.setVisible(false);
		}
	}
}
/**
 * Sets the image that is displayed if the tab item is disabled.
 * Null will clear the image.
 * 
 * @param image the image to be displayed when the item is disabled or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @deprecated
 */
public void setDisabledImage (Image image) {
	checkWidget();
}
/**
 * Sets the font that the receiver will use to paint textual information
 * for this item to the font specified by the argument, or to the default font
 * for that kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setFont (Font font){
	checkWidget();
	if (font != null && font.equals(this.font)) return;
	this.font = font;
	if (!parent.updateTabHeight(parent.tabHeight, false)) {
		parent.updateItems();
		parent.redraw();
	}
}
/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public void setForeground (Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	foreground = color;
	int edge = parent.getRightItemEdge();
	int index = parent.indexOf(this);
	if (x + width < edge && parent.selectedIndex != index) {
		parent.redraw(x, y, width, height, false);
	}
}
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.equals(getImage())) return;
	super.setImage(image);
	if (!parent.updateTabHeight(parent.tabHeight, false)) {
		parent.updateItems();
	}
	parent.redraw();
}
/**
 * Set the widget text.
 * <p>
 * This method sets the widget label.  The label may include
 * mnemonic characters but must not contain line delimiters.
 *
 * @param string the new label for the widget
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
	if (string.equals(getText())) return;
	super.setText(string);
	shortenedText = null;
	shortenedTextWidth = 0;
	parent.updateItems();
	parent.redraw();
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
}

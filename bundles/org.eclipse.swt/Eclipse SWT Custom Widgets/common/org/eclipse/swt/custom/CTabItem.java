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
* UNDER CONSTRUCTION
*/

public class CTabItem extends Item {
	CTabFolder parent;
	int x,y,width,height = 0;
	Control control; // the tab page
	
	String toolTipText;
	String shortenedText;
	int shortenedTextWidth;
	
	// Appearance
	Font font;
	Image disabledImage; 
	
	Rectangle closeRect = new Rectangle(0, 0, 0, 0);
	int closeImageState = CTabFolder.NONE;
	boolean showClose = false;

	// internal constants
	static final int TOP_MARGIN = 2;
	static final int BOTTOM_MARGIN = 2;
	static final int LEFT_MARGIN = 4;
	static final int RIGHT_MARGIN = 4;
	static final int INTERNAL_SPACING = 2;
	static final int FLAGS = SWT.DRAW_TRANSPARENT | SWT.DRAW_MNEMONIC;
	static final String ELLIPSIS = "..."; //$NON-NLS-1$
	
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
public CTabItem (CTabFolder parent, int style) {
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
public CTabItem (CTabFolder parent, int style, int index) {
	super (parent, checkStyle(style));
	showClose = (style & SWT.CLOSE) != 0;
	parent.createItem (this, index);
}
static int checkStyle(int style) {
	return SWT.NONE;
}
static String shortenText(GC gc, String text, int width) {
	if (gc.textExtent(text, FLAGS).x <= width) return text;
	int ellipseWidth = gc.textExtent(ELLIPSIS, FLAGS).x;
	int length = text.length();
	int end = length - 1;
	while (end > 0) {
		text = text.substring(0, end);
		int l = gc.textExtent(text, FLAGS).x;
		if (l + ellipseWidth <= width) {
			return text + ELLIPSIS;
		}
		end--;
	}
	return text.substring(0,1);
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
	font = null;
}
void drawClose(GC gc) {
	if (closeRect.width == 0 || closeRect.height == 0) return;
	Display display = getDisplay();

	// draw X 9x9
	int indent = Math.max(1, (CTabFolder.BUTTON_SIZE-9)/2);
	int x = closeRect.x + indent;
	int y = closeRect.y + indent;
	y += parent.onBottom ? -1 : 1;
	
	int index = parent.indexOf(this);
	Color closeBorder = display.getSystemColor(CTabFolder.BUTTON_BORDER);
	switch (closeImageState) {
		case CTabFolder.NORMAL: {
			int[] shape = new int[] {x,y, x+2,y, x+4,y+2, x+5,y+2, x+7,y, x+9,y, 
					                 x+9,y+2, x+7,y+4, x+7,y+5, x+9,y+7, x+9,y+9,
			                         x+7,y+9, x+5,y+7, x+4,y+7, x+2,y+9, x,y+9,
			                         x,y+7, x+2,y+5, x+2,y+4, x,y+2};
			gc.setBackground(display.getSystemColor(CTabFolder.BUTTON_FILL));
			gc.fillPolygon(shape);
			gc.setForeground(closeBorder);
			gc.drawPolygon(shape);
			break;
		}
		case CTabFolder.HOT: {
			int[] shape = new int[] {x,y, x+2,y, x+4,y+2, x+5,y+2, x+7,y, x+9,y, 
					                 x+9,y+2, x+7,y+4, x+7,y+5, x+9,y+7, x+9,y+9,
			                         x+7,y+9, x+5,y+7, x+4,y+7, x+2,y+9, x,y+9,
			                         x,y+7, x+2,y+5, x+2,y+4, x,y+2};
			Color fill = new Color(display, CTabFolder.CLOSE_FILL);
			gc.setBackground(fill);
			gc.fillPolygon(shape);
			fill.dispose();
			gc.setForeground(closeBorder);
			gc.drawPolygon(shape);
			break;
		}
		case CTabFolder.SELECTED: {
			int[] shape = new int[] {x+1,y+1, x+3,y+1, x+5,y+3, x+6,y+3, x+8,y+1, x+10,y+1, 
					                 x+10,y+3, x+8,y+5, x+8,y+6, x+10,y+8, x+10,y+10,
			                         x+8,y+10, x+6,y+8, x+5,y+8, x+3,y+10, x+1,y+10,
			                         x+1,y+8, x+3,y+6, x+3,y+5, x+1,y+3};
			Color fill = new Color(display, CTabFolder.CLOSE_FILL);
			gc.setBackground(fill);
			gc.fillPolygon(shape);
			fill.dispose();
			gc.setForeground(closeBorder);
			gc.drawPolygon(shape);
			break;
		}
		case CTabFolder.NONE: {
			int[] shape = new int[] {x,y, x+10,y, x+10,y+10, x,y+10};
			parent.drawBackground(gc, shape, false);
			break;
		}
	}
}
void drawSelected(GC gc ) {
	Point size = parent.getSize();
	int deadspace = parent.simple || parent.single ? 0 : parent.curveWidth - parent.curveIndent;
	int rightEdge = Math.min (x + width - deadspace, parent.getRightItemEdge());
	
	//	 Draw selection border across all tabs
	int xx = parent.borderLeft;
	int yy = parent.onBottom ? size.y - parent.borderBottom - parent.tabHeight - parent.highlight_header : parent.borderTop + parent.tabHeight + 1;
	int ww = size.x - parent.borderLeft - parent.borderRight;
	int hh = parent.highlight_header - 1;
	int[] shape = new int[] {xx,yy, xx+ww,yy, xx+ww,yy+hh, xx,yy+hh};
	parent.drawBackground(gc, shape, true);
	
	if (parent.single) {
		if (!isShowing()) return;
	} else {
		// if selected tab scrolled out of view or partially out of view
		// just draw bottom line
		if (!isShowing()){
			int x1 = Math.max(0, parent.borderLeft - 1);
			int y1 = (parent.onBottom) ? y - 1 : y + height;
			int x2 = size.x - parent.borderRight;
			gc.setForeground(CTabFolder.borderColor);
			gc.drawLine(x1, y1, x2, y1);
			return;
		}
			
		// draw selected tab background and outline
		shape = null;
		if (this.parent.onBottom) {
			int[] left = parent.simple ? CTabFolder.SIMPLE_BOTTOM_LEFT_CORNER : CTabFolder.BOTTOM_LEFT_CORNER;
			int[] right = parent.simple ? CTabFolder.SIMPLE_BOTTOM_RIGHT_CORNER : parent.curve;
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
				shape[index++] = parent.simple ? rightEdge - 1 + right[2*i] : rightEdge - parent.curveIndent + right[2*i];
				shape[index++] = parent.simple ? y + height + right[2*i+1] - 1 : y + right[2*i+1] - 2;
			}
			shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + parent.curveWidth - parent.curveIndent;
			shape[index++] = y - 1;
			shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + parent.curveWidth - parent.curveIndent;
			shape[index++] = y - 1;
		} else {
			int[] left = parent.simple ? CTabFolder.SIMPLE_TOP_LEFT_CORNER : CTabFolder.TOP_LEFT_CORNER;
			int[] right = parent.simple ? CTabFolder.SIMPLE_TOP_RIGHT_CORNER : parent.curve;
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
				shape[index++] = parent.simple ? rightEdge - 1 + right[2*i] : rightEdge - parent.curveIndent + right[2*i];
				shape[index++] = y + right[2*i+1];
			}
			shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + parent.curveWidth - parent.curveIndent;
			shape[index++] = y + height + 1;
			shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + parent.curveWidth - parent.curveIndent;
			shape[index++] = y + height + 1;
		}
		parent.drawBackground(gc, shape, true);
		
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
		parent.antialias(shape, CTabFolder.borderColor.getRGB(), inside, outside, gc);
		gc.setForeground(CTabFolder.borderColor);
		gc.drawPolyline(shape);
	}
	
	// draw Image
	int xDraw = x + LEFT_MARGIN;
	Image image = getImage();
	if (image != null) {
		Rectangle imageBounds = image.getBounds();
		// only draw image if it won't overlap with close button
		int maxImageWidth = rightEdge - xDraw - RIGHT_MARGIN;
		if (closeRect.width > 0) maxImageWidth -= closeRect.width + INTERNAL_SPACING;
		if (imageBounds.width < maxImageWidth) {
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
	}
	
	// draw Text
	int textWidth = rightEdge - xDraw - RIGHT_MARGIN;
	if (closeRect.width > 0) textWidth -= closeRect.width + INTERNAL_SPACING;
	if (textWidth > 0) {
		Font gcFont = gc.getFont();
		gc.setFont(font == null ? parent.getFont() : font);
		
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
		
		// draw a Focus rectangle
		if (parent.isFocusControl()) {
			Display display = getDisplay();
			if (parent.simple || parent.single) {
				gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
				gc.drawFocus(xDraw-1, textY-1, extent.x+2, extent.y+2);
			} else {
				gc.setForeground(display.getSystemColor(CTabFolder.BUTTON_BORDER));
				gc.drawLine(xDraw, textY+extent.y+1, xDraw+extent.x+1, textY+extent.y+1);
			}
		}
	}
	if (parent.showClose || showClose) drawClose(gc);
}
void drawUnselected(GC gc) {
	// Do not draw partial items
	if (!isShowing()) return;
	
	// draw border
	if (parent.indexOf(this) != parent.selectedIndex - 1) {
		gc.setForeground(CTabFolder.borderColor);
		gc.drawLine(x + width - 1, y, x + width - 1, y + height);
	}
	// draw Image
	int xDraw = x + LEFT_MARGIN;
	Image image = getImage();
	if (image != null && parent.showUnselectedImage) {
		Rectangle imageBounds = image.getBounds();
		// only draw image if it won't overlap with close button
		int maxImageWidth = x + width - xDraw - RIGHT_MARGIN;
		if (parent.showUnselectedClose && (parent.showClose || showClose)) {
			maxImageWidth -= closeRect.width + INTERNAL_SPACING;
		}
		if (imageBounds.width < maxImageWidth) {		
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
	}
	// draw Text
	int textWidth = x + width - xDraw - RIGHT_MARGIN;
	if (parent.showUnselectedClose && (parent.showClose || showClose)) {
		textWidth -= closeRect.width + INTERNAL_SPACING;
	}
	if (textWidth > 0) {
		Font gcFont = gc.getFont();
		gc.setFont(font == null ? parent.getFont() : font);
		if (shortenedText == null || shortenedTextWidth != textWidth) {
			shortenedText = shortenText(gc, getText(), textWidth);
			shortenedTextWidth = textWidth;
		}	
		Point extent = gc.textExtent(shortenedText, FLAGS);
		int textY = y + (height - extent.y) / 2;
		textY += parent.onBottom ? -1 : 1;
		gc.setForeground(parent.getForeground());
		gc.drawText(shortenedText, xDraw, textY, FLAGS);
		gc.setFont(gcFont);
	}
	// draw close
	if (parent.showUnselectedClose && (parent.showClose || showClose)) drawClose(gc);
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
	checkWidget();
	return disabledImage;
}
/**
 * UNDER CONSTRUCTION
 * @since 3.0
 */
public Font getFont() {
	checkWidget();
	if (font != null) return font;
	return parent.getFont();
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
public CTabFolder getParent () {
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
/**
* UNDER CONSTRUCTION
* @since 3.0
*/
public boolean isShowing () {
	int index = parent.indexOf(this);
	int rightEdge = parent.getRightItemEdge();
	if (parent.single) {
		if (index == parent.selectedIndex) {
			return x <= rightEdge;
		}
		return false;
	}
	if (index < parent.firstIndex) return false;
	if (parent.firstIndex == index) {
		return x <= rightEdge;
	}
	return x + width < rightEdge;
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
	// NOTE: preferred width does not include the "dead space" caused
	// by the curve.
	if (isDisposed()) return 0;
	int w = 0;
	Image image = getImage();
	if (image != null && (isSelected || parent.showUnselectedImage)) {
		w += image.getBounds().width;
	}
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
		if (isSelected || parent.showUnselectedClose) {
			if (w > 0) w += INTERNAL_SPACING;
			w += CTabFolder.BUTTON_SIZE;
		}
	}
	return w + LEFT_MARGIN + RIGHT_MARGIN;
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
	if (image != null && image.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.disabledImage = image;
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
 * UNDER CONSTRUCTION
 * @since 3.0
 */
public void setFont (Font font){
	checkWidget();
	if (font != null && font.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (font != null && font.equals(this.font)) return;
	this.font = font;
	if (!parent.updateTabHeight(parent.tabHeight, false)) {
		parent.updateItems();
		parent.redraw();
	}
}
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
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
	if (string == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
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

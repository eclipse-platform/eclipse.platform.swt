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
	Image disabledImage;
	String shortenedText;
	int shortenedTextWidth;
	
	// internal constants
	static final int LEFT_MARGIN = 6;
	static final int RIGHT_MARGIN = 6;
	static final int TOP_MARGIN = 3;
	static final int BOTTOM_MARGIN = TOP_MARGIN + CTabFolder2.SELECTION_BORDER;
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
public void dispose () {
	if (isDisposed ()) return;
	//if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	parent.destroyItem(this);
	super.dispose();
	parent = null;
	control = null;
	toolTipText = null;
	shortenedText = null;
}

void drawSelected(GC gc ) {
	Display display = getDisplay();
	int[] shape = null;
	int extra = CTabFolder2.CURVE_WIDTH/2;
	if (this.parent.onBottom) {
		int[] left = CTabFolder2.BOTTOM_LEFT_CORNER;
		int[] right = parent.curve;
		shape = new int[left.length+right.length+4];
		int index = 0;
		shape[index++] = x;
		shape[index++] = y + CTabFolder2.SELECTION_BORDER - 1;
		for (int i = 0; i < left.length/2; i++) {
			shape[index++]=x+left[2*i];
			shape[index++]=y+height+left[2*i+1]-1;
		}
		for (int i = 0; i < right.length/2; i++) {
			shape[index++]=x+width-extra+right[2*i];
			shape[index++]=y+right[2*i+1]-2;
		}
		shape[index++] = x + width + extra;
		shape[index++] = y + CTabFolder2.SELECTION_BORDER - 1;
	} else {
		int[] left = CTabFolder2.TOP_LEFT_CORNER;
		int[] right = parent.curve;
		shape = new int[left.length+right.length+4];
		int index = 0;
		shape[index++] = x;
		shape[index++] = y + height - CTabFolder2.SELECTION_BORDER;
		for (int i = 0; i < left.length/2; i++) {
			shape[index++]=x+left[2*i];
			shape[index++]=y+left[2*i+1];
		}
		for (int i = 0; i < right.length/2; i++) {
			shape[index++]=x+width-extra+right[2*i];
			shape[index++]=y+right[2*i+1];
		}
		shape[index++] = x + width + extra;
		shape[index++] = y + height - CTabFolder2.SELECTION_BORDER+1;
	}
	parent.drawSelectionBackground(gc, y, shape);
	
	// Shape is non-rectangular
	Region r = new Region();
	if (parent.onBottom) {
		r.add(new Rectangle(x, y + CTabFolder2.SELECTION_BORDER, width, height - CTabFolder2.SELECTION_BORDER));
	} else {
		r.add(new Rectangle(x, y, width, height - CTabFolder2.SELECTION_BORDER));
	}
	r.subtract(shape);
	if (parent.single) {
		// for a single tab,  fill in gaps with background colour
		gc.setBackground(parent.getBackground());
	} else {
		// for mutliple tabs, fill in gaps with parent colours
		gc.setBackground(parent.getParent().getBackground());
	}
	CTabFolder2.fillRegion(gc, r);
	r.dispose();
	
	// draw Image
	int xDraw = x + LEFT_MARGIN;
	Image image = getImage();
	if (image != null) {
		Rectangle imageBounds = image.getBounds();
		int imageX = xDraw;
		int imageHeight = Math.min(height - 2*TOP_MARGIN, imageBounds.height);
		int imageY = y;
		imageY += TOP_MARGIN + (height - 2*TOP_MARGIN - imageHeight) / 2;
		int imageWidth = imageBounds.width * imageHeight / imageBounds.height;
		gc.drawImage(image, 
			         imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height,
			         imageX, imageY, imageWidth, imageHeight);
		xDraw += imageWidth + INTERNAL_SPACING;
	}
	
	// draw Text
	int textWidth = x + width - xDraw - RIGHT_MARGIN;
	if (shortenedText == null || shortenedTextWidth != textWidth) {
		shortenedText = shortenText(gc, getText(), textWidth);
		shortenedTextWidth = textWidth;
	}
	Point extent = gc.textExtent(shortenedText, FLAGS);
	int textY = y + TOP_MARGIN + (height - 2*TOP_MARGIN - extent.y) / 2;	
	gc.setForeground(parent.selectionForeground);
	gc.drawText(shortenedText, xDraw, textY, FLAGS);
	
	// draw a Focus rectangle
	if (parent.isFocusControl()) {
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawFocus(xDraw-2, textY-2, extent.x+3, extent.y+4);
	}
}
void drawUnselected(GC gc) {
	int[] shape = null;
	if (this.parent.onBottom) {
		int[] left = CTabFolder2.BOTTOM_LEFT_CORNER;
		int[] right = CTabFolder2.BOTTOM_RIGHT_CORNER;
		shape = new int[left.length+right.length+4];
		int index = 0;
		shape[index++]=x;
		shape[index++]=y;
		for(int i = 0; i < left.length/2; i++) {
			shape[index++] = x+left[2*i]; 
			shape[index++] = y+height-1+left[2*i+1]; // -1 = unselected tab 1 pixel shorter than selected tab
		}
		for(int i = 0; i < right.length/2; i++) {
			shape[index++] = x+width-3+right[2*i]; // -2 = 2 pixel gap between tabs, gap on right side
			shape[index++] = y+height-1+right[2*i+1]; // -1 = unselected tab 1 pixel shorter than selected tab
		}
		shape[index++]=x+width-3; // -2 = 2 pixel gap between tabs, gap on right side + 1 pixel for border
		shape[index++]=y;
	} else {
		int[] left = CTabFolder2.TOP_LEFT_CORNER;
		int[] right = CTabFolder2.TOP_RIGHT_CORNER;
		shape = new int[left.length+right.length+4];
		int index = 0;
		shape[index++]=x;
		shape[index++]=y+height;
		for(int i = 0; i < left.length/2; i++) {
			shape[index++] = x+left[2*i];
			shape[index++] = y+1+left[2*i+1]; // +1 = unselected tab 1 pixel shorter than selected tab
		}
		for(int i = 0; i < right.length/2; i++) {
			shape[index++] = x+width-2+right[2*i]; // -2 = 2 pixel gap between tabs, gap on right side
			shape[index++] = y+1+right[2*i+1]; // +1 = unselected tab 1 pixel shorter than selected tab
		}
		shape[index++]=x+width-2; // -2 = 2 pixel gap between tabs, gap on right side
		shape[index++]=y+height;
	}
	gc.setBackground(parent.getBackground());
	gc.fillPolygon(shape);
	
	// Shape is non-rectangular, fill in gaps with parent colours
	Region r = new Region();
	r.add(new Rectangle(x, y, width, height));
	r.subtract(shape);
	gc.setBackground(parent.getParent().getBackground());
	CTabFolder2.fillRegion(gc, r);
	r.dispose();
	
	// draw border
	shape = null;
	if (this.parent.onBottom) {
		int[] left = CTabFolder2.BOTTOM_LEFT_CORNER;
		int[] right = CTabFolder2.BOTTOM_RIGHT_CORNER;
		shape = new int[left.length+right.length+4];
		int index = 0;
		shape[index++]=x;
		shape[index++]=y;
		for(int i = 0; i < left.length/2; i++) {
			shape[index++] = x+left[2*i]; 
			shape[index++] = y+height-2+left[2*i+1]; // -1 = unselected tab 1 pixel shorter than selected tab
		}
		for(int i = 0; i < right.length/2; i++) {
			shape[index++] = x+width-3+right[2*i]; // -2 = 2 pixel gap between tabs, gap on right side
			shape[index++] = y+height-2+right[2*i+1]; // -1 = unselected tab 1 pixel shorter than selected tab
		}
		shape[index++]=x+width-3; // -2 = 2 pixel gap between tabs, gap on right side
		shape[index++]=y;
	} else {
		int[] left = CTabFolder2.TOP_LEFT_CORNER;
		int[] right = CTabFolder2.TOP_RIGHT_CORNER;
		shape = new int[left.length+right.length+4];
		int index = 0;
		shape[index++]=x;
		shape[index++]=y+height;
		for(int i = 0; i < left.length/2; i++) {
			shape[index++] = x+left[2*i];
			shape[index++] = y+1+left[2*i+1]; // +1 = unselected tab 1 pixel shorter than selected tab
		}
		for(int i = 0; i < right.length/2; i++) {
			shape[index++] = x+width-3+right[2*i]; // -2 = 2 pixel gap between tabs, gap on right side
			shape[index++] = y+1+right[2*i+1]; // +1 = unselected tab 1 pixel shorter than selected tab
		}
		shape[index++]=x+width-3; // -2 = 2 pixel gap between tabs, gap on right side
		shape[index++]=y+height;
	}
	gc.setForeground(CTabFolder2.border1Color);
	gc.drawPolyline(shape);
	
	// draw Text
	int textWidth = width - LEFT_MARGIN - RIGHT_MARGIN;
	if (shortenedText == null || shortenedTextWidth != textWidth) {
		shortenedText = shortenText(gc, getText(), textWidth);
		shortenedTextWidth = textWidth;
	}	
	Point extent = gc.textExtent(shortenedText, FLAGS);
	int textY = y;
	if (parent.onBottom) {
		textY+= BOTTOM_MARGIN + (height - TOP_MARGIN - BOTTOM_MARGIN - extent.y) / 2;
	} else {
		textY+= TOP_MARGIN + (height - TOP_MARGIN - BOTTOM_MARGIN - extent.y) / 2;
	}
	gc.setForeground(parent.getForeground());
	gc.drawText(shortenedText, x + LEFT_MARGIN, textY, FLAGS);
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
 */
public Image getDisabledImage(){
	//checkWidget();
	return disabledImage;
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
	h = Math.max(h, gc.textExtent(text, FLAGS).y);
	return h + TOP_MARGIN + BOTTOM_MARGIN;
}
int preferredWidth(GC gc, boolean isSelected) {
	int w = 0;
	Image image = getImage();
	if (isSelected) {
		if (image != null) w += image.getBounds().width;
		w += 5;
	}
	String text = getText();
	if (text != null) {
		if (isSelected && image != null) w += INTERNAL_SPACING;
		w += gc.textExtent(text, FLAGS).x;
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
 */
public void setDisabledImage (Image image) {
	checkWidget();
	// !!! this image is never being used
	if (image != null && image.equals(getDisabledImage())) return;
	disabledImage = image;
	//parent.redraw();
}
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.equals(getImage())) return;
	super.setImage(image);
	if (!parent.updateTabHeight(parent.tabHeight)) {
		parent.updateItems();
	}
	parent.redrawTabArea();
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
	parent.redrawTabArea();
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

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

public class CTabItem extends Item {
	CTabFolder parent;
	int x,y,width,height = 0;
	String toolTipText;
	Control control; // the tab page
		
	private Image disabledImage;
	
	// internal constants
	static final int LEFT_MARGIN = 4;
	static final int RIGHT_MARGIN = 4;	
	static final int TOP_MARGIN = 3;
	static final int BOTTOM_MARGIN = 3;
	private static final int INTERNAL_SPACING = 2;
	
	private static final String ellipsis = "..."; //$NON-NLS-1$
	
	String shortenedText;
	int shortenedTextWidth;
	
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
 * @see Widget#getStyle
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
 * @see Widget#getStyle
 */
public CTabItem (CTabFolder parent, int style, int index) {
	super (parent, checkStyle(style));
	parent.createItem (this, index);
}
private static int checkStyle(int style) {
	return SWT.NONE;
}

public void dispose () {
	if (isDisposed()) return;
	parent.destroyItem(this);
	super.dispose();
	parent = null;
	control = null;
	toolTipText = null;
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @param index the index that specifies the column
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
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public Control getControl () {
	checkWidget();
	return control;
}
/**
 * Get the image displayed in the tab if the tab is disabled.
 * 
 * @return the disabled image or null
 */
public Image getDisabledImage(){
	//checkWidget();
	return disabledImage;
}
/**
 * Returns the receiver's parent, which must be a <code>CTabFolder</code>.
 *
 * @return the receiver's parent
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
	return toolTipText;
}
/**
 * Paint the receiver.
 */
void onPaint(GC gc, boolean isSelected) {
	
	if (width == 0 || height == 0) return;
	
	Display display = getDisplay();
	Color highlightShadow = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	Color normalShadow = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);		

	int index = parent.indexOf(this);
	
	if (isSelected) {

		Rectangle bounds = null;
		if (!parent.onBottom) {
			if (index == parent.topTabIndex) {
				bounds = new Rectangle(x + 1, y + 1, width - 2, height - 1);
			} else {
				bounds = new Rectangle(x + 2, y + 1, width - 3, height - 1);
			}
		} else {
			if (index == parent.topTabIndex) {
				bounds = new Rectangle(x + 1, y + 1, width - 2, height - 2);
			} else {
				bounds = new Rectangle(x + 2, y + 1, width - 3, height - 2);
			}
		}
		if (parent.backgroundImage != null) {
			// draw a background image behind the text
			Rectangle imageRect = parent.backgroundImage.getBounds();
			gc.drawImage(parent.backgroundImage, 0, 0, imageRect.width, imageRect.height,
				bounds.x, bounds.y, bounds.width, bounds.height);
		} else if (parent.gradientColors != null) {
			// draw a gradient behind the text
			Color oldBackground = gc.getBackground();
			if (parent.gradientColors.length == 1) {
				if (parent.gradientColors[0] != null) gc.setBackground(parent.gradientColors[0]);
				gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
			} else {
				Color oldForeground = gc.getForeground();
				Color lastColor = parent.gradientColors[0];
				if (lastColor == null) lastColor = oldBackground;
				for (int i = 0, pos = 0; i < parent.gradientPercents.length; ++i) {
					gc.setForeground(lastColor);
					lastColor = parent.gradientColors[i + 1];
					if (lastColor == null) lastColor = oldBackground;
					gc.setBackground(lastColor);
					int gradientWidth = (parent.gradientPercents[i] * bounds.width / 100) - pos;
					gc.fillGradientRectangle(bounds.x + pos, bounds.y, gradientWidth, bounds.height, false);
					pos += gradientWidth;
				}
				gc.setForeground(oldForeground);
			}
			gc.setBackground(oldBackground);
		}

		// draw tab lines
		if (!parent.onBottom) {
			gc.setForeground(normalShadow);
			if (index != parent.topTabIndex) {
				gc.drawLine(x + 1, y,              x + 1, y);
				gc.drawLine(x,     y + 1,          x,     y + height - 2);
				gc.drawLine(x,     y + height - 1, x,     y + height - 1);
			}
			gc.drawLine(x + width - 1, y,              x + width - 1, y);
			gc.drawLine(x + width,     y + 1,          x + width,     y + height - 2);
			gc.drawLine(x + width,     y + height - 1, x + width,     y + height - 1);
	
			gc.setForeground(highlightShadow);
			if (index != parent.topTabIndex) {
				gc.drawLine(x + 2, y,              x + 2, y);
				gc.drawLine(x + 1, y + 1,          x + 1, y + height - 2);
				gc.drawLine(x + 1, y + height - 1, x + 1, y + height - 1);
			} else {
				gc.drawLine(x, y, x, y + height - 1);
			}
			
			gc.drawLine(x + width - 2, y,              x + width - 2, y);
			gc.drawLine(x + width - 1, y + 1,          x + width - 1, y + height - 2);
			gc.drawLine(x + width - 1, y + height - 1, x + width - 1, y + height - 1);
	
			// light line across top
			if (index != parent.topTabIndex) {
				gc.drawLine(x + 3, y, x + width - 3, y);
			} else {
				gc.drawLine(x + 1, y, x + width - 3, y);
			}
		} else {
			gc.setForeground(normalShadow);
			if (index != parent.topTabIndex) {
				gc.drawLine(x,     y,              x,     y);
				gc.drawLine(x,     y + 1,          x,     y + height - 2);
				gc.drawLine(x + 1, y + height - 1, x + 1, y + height - 1);
			}
			gc.drawLine(x + width,     y,              x + width,     y);
			gc.drawLine(x + width,     y + 1,          x + width,     y + height - 2);
			gc.drawLine(x + width - 1, y + height - 1, x + width - 1, y + height - 1);
	
			gc.setForeground(highlightShadow);
			if (index != parent.topTabIndex) {
				gc.drawLine(x + 1, y,              x + 1, y);
				gc.drawLine(x + 1, y + 1,          x + 1, y + height - 2);
				gc.drawLine(x + 2, y + height - 1, x + 2, y + height - 1);
			} else {
				gc.drawLine(x, y, x, y + height - 1);
			}
			
			gc.drawLine(x + width - 1, y,              x + width - 1, y);
			gc.drawLine(x + width - 1, y + 1,          x + width - 1, y + height - 2);
			gc.drawLine(x + width - 2, y + height - 1, x + width - 2, y + height - 1);
	
			// light line across top and bottom
			if (index != parent.topTabIndex) {
				gc.drawLine(x + 1, y, x + width - 2, y);
				gc.drawLine(x + 2, y + height - 1, x + width - 3, y + height - 1);
			} else {
				gc.drawLine(x + 1, y, x + width - 2, y);
				gc.drawLine(x + 1, y + height - 1, x + width - 3, y + height - 1);
			}			
		}
		if (parent.isFocusControl()) {
			// draw a focus rectangle
			int x1, y1, width1, height1;
			if (!parent.onBottom) {
				if (index == parent.topTabIndex) {
					x1 = x + 1; y1 = y + 1; width1 = width - 2; height1 = height - 1;
				} else {
					x1 = x + 2; y1 = y + 1; width1 = width - 3; height1 = height - 1;
				}
			} else {
				if (index == parent.topTabIndex) {
					x1 = x + 1; y1 = y + 1; width1 = width - 2; height1 = height - 2;
				} else {
					x1 = x + 2; y1 = y + 1; width1 = width - 3; height1 = height - 2;
				}
			}
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.drawFocus(x1, y1, width1, height1);
		}
	} else {
		// draw tab lines for unselected items
		gc.setForeground(normalShadow);
		if (!parent.onBottom) {
			if (index != parent.topTabIndex && index != parent.getSelectionIndex() + 1) {
				gc.drawLine(x, y, x, y + (height / 2));
			}
		} else {
			if (index != parent.topTabIndex && index != parent.getSelectionIndex() + 1) {
				gc.drawLine(x, y + (height / 2), x, y + height - 1);
			}
		}
		
	}

	// draw Image
	int xDraw = x + LEFT_MARGIN;
	
	Image image = getImage();
	if (!isSelected && image != null) {
		Image temp = getDisabledImage();
		if (temp != null){
			image = temp;
		}
	}
	if (image != null) {
		Rectangle imageBounds = image.getBounds();
		int imageX = xDraw;
		int imageHeight = Math.min(height - BOTTOM_MARGIN - TOP_MARGIN, imageBounds.height);
		int imageY = y + (height - imageHeight) / 2;
		int imageWidth = imageBounds.width * imageHeight / imageBounds.height;
		gc.drawImage(image, 
			         imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height,
			         imageX, imageY, imageWidth, imageHeight);
		xDraw += imageWidth + INTERNAL_SPACING;
	}
	
	// draw Text
	int textWidth = x + width - xDraw - RIGHT_MARGIN;
	if (isSelected && parent.showClose) {
		textWidth = x + width - xDraw - parent.closeBar.getSize().x - RIGHT_MARGIN;
	}
	if (shortenedText == null || shortenedTextWidth != textWidth) {
		shortenedText = shortenText(gc, getText(), textWidth);
		shortenedTextWidth = textWidth;
	}
	String text = shortenedText;
	
	if (isSelected && parent.selectionForeground != null) {
		gc.setForeground(parent.selectionForeground);
	} else {
		gc.setForeground(parent.getForeground());
	}
	int textY = y + (height - gc.textExtent(text, SWT.DRAW_MNEMONIC).y) / 2; 	
	gc.drawText(text, xDraw, textY, SWT.DRAW_TRANSPARENT | SWT.DRAW_MNEMONIC);
	
	gc.setForeground(parent.getForeground());
}
private static String shortenText(GC gc, String text, int width) {
	if (gc.textExtent(text, SWT.DRAW_MNEMONIC).x <= width) return text;
	
	int ellipseWidth = gc.textExtent(ellipsis, SWT.DRAW_MNEMONIC).x;
	int length = text.length();
	int end = length - 1;
	while (end > 0) {
		text = text.substring(0, end);
		int l1 = gc.textExtent(text, SWT.DRAW_MNEMONIC).x;
		if (l1 + ellipseWidth <= width) {
			return text + ellipsis;
		}
		end--;
	}
	return text + ellipsis;
}
/**
 * Answer the preferred height of the receiver for the GC.
 */
int preferredHeight(GC gc) {
	Image image = getImage();
	int height = 0;
	if (image != null) height = image.getBounds().height;
	String text = getText();
	height = Math.max(height, gc.textExtent(text, SWT.DRAW_MNEMONIC).y);
	return height + TOP_MARGIN + BOTTOM_MARGIN;
}
/**
 * Answer the preferred width of the receiver for the GC.
 */
int preferredWidth(GC gc) {
	int width = 0;
	Image image = getImage();
	if (image != null) width += image.getBounds().width;
	String text = getText();
	if (text != null) {
		if (image != null) width += INTERNAL_SPACING;
		width += gc.textExtent(text, SWT.DRAW_MNEMONIC).x;
	}
	if (parent.showClose) width += INTERNAL_SPACING + preferredHeight(gc); // closebar will be square and will fill preferred height
	return width + LEFT_MARGIN + RIGHT_MARGIN;
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
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.equals(getImage())) return;
	super.setImage(image);
	parent.resetTabSize(true);
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
	if (image != null && image.equals(getDisabledImage())) return;
	disabledImage = image;
	parent.redraw();
}

/**
 * Set the widget text.
 * <p>
 * This method sets the widget label.  The label may include
 * mnemonic characters but must not contain line delimiters.
 * The mnemonic indicator character '&amp' can be escaped by
 * doubling it in the string, causing a single '&amp' to be
 * displayed.
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
	parent.resetTabSize(false);	
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

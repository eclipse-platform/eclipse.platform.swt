package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
	
	private static final String ellipsis = "...";
	
/**
 * Construct a CTabItem with the specified parent and style.
 */
public CTabItem (CTabFolder parent, int style) {
	this(parent, style, parent.getItemCount());
}
/**
 * Construct a CTabItem with the specified parent and style, inserted at
 * the specified index.
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
	super.dispose();
	parent.destroyItem(this);
	parent = null;
	control = null;
	toolTipText = null;
}

/**
 * Return the bounds of the CTabItem.
 */
public Rectangle getBounds () {
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
	return control;
}
/**
 * Answer the display of the receiver's parent widget.
 */
public Display getDisplay() {
	if (parent == null) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay();
}
public Image getDisabledImage(){
	return disabledImage;
}
/**
 * Return the parent of the CTabItem.
 */
public CTabFolder getParent () {
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
	String text = shortenText(gc, getText(), textWidth);
	
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
	return "";
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
	if (control != null && control.getParent() != parent) {
		SWT.error (SWT.ERROR_INVALID_PARENT);
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
	if (image != null && image.equals(getImage())) return;
	int oldHeight = parent.getTabHeight();
	super.setImage(image);
	if (oldHeight != parent.getTabHeight()) {
		parent.notifyListeners(SWT.Resize, new Event());
	} else {
		parent.layoutItems();
		parent.redraw();
	}
}
public void setDisabledImage (Image image) {
	if (image != null && image.equals(getDisabledImage())) return;
	disabledImage = image;
	parent.redraw();
}

/**
 * Set the widget text.
 * <p>
 * This method sets the widget label.  The label may include
 * the mnemonic characters but must not contain line delimiters.
 *
 * @param string the new label for the widget
 *
 */
public void setText (String string) {
	if (string.equals(getText())) return;
	super.setText(string);
	parent.layoutItems();
	parent.redraw();	
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
	toolTipText = string;
}


}

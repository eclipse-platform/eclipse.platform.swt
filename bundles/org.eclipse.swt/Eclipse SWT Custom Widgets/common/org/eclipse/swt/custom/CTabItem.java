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
	private static final int LEFT_MARGIN = 3;
	private static final int RIGHT_MARGIN = 3;	
	static final int TOP_MARGIN = 2;
	static final int BOTTOM_MARGIN = 2;
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
private int imageHeight() {
	int imageHeight = 0;
	if (parent.getImageHeight() != -1) {
		imageHeight = parent.getImageHeight();
	} else {
		Image image = getImage();
		if (image != null) {
			imageHeight = image.getBounds().height;
		}
	}
	return imageHeight;
}
private int imageWidth() {
	int imageWidth = 0;
	Image image = getImage();
	if (image != null) {
		imageWidth = image.getBounds().width;
	}
	return imageWidth;
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
		
		if (parent.isFocusControl()) {
			// draw a focus rectangle
			gc.drawFocus(x + 2, y + 1, width - 3, height - 1);
		}
		final Rectangle bounds;
		if (index == parent.topTabIndex) {
			bounds = new Rectangle(x + 1, y, width - 2, height);
		} else {
			bounds = new Rectangle(x + 2, y, width - 3, height);
		}
		if (parent.backgroundImage != null) {
			// draw a background image behind the text
			Rectangle imageRect = parent.backgroundImage.getBounds();
			gc.drawImage(parent.backgroundImage, 0, 0, imageRect.width, imageRect.height,
				bounds.x, bounds.y, bounds.width, bounds.height);
		} else if (parent.gradientColors != null) {
			// draw a gradient behind the text
			final Color oldBackground = gc.getBackground();
			if (parent.gradientColors.length == 1) {
				if (parent.gradientColors[0] != null) gc.setBackground(parent.gradientColors[0]);
				gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
			} else {
				final Color oldForeground = gc.getForeground();
				Color lastColor = parent.gradientColors[0];
				if (lastColor == null) lastColor = oldBackground;
				for (int i = 0, pos = 0; i < parent.gradientPercents.length; ++i) {
					gc.setForeground(lastColor);
					lastColor = parent.gradientColors[i + 1];
					if (lastColor == null) lastColor = oldBackground;
					gc.setBackground(lastColor);
					final int gradientWidth = (parent.gradientPercents[i] * bounds.width / 100) - pos;
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
		int imageHeight = imageHeight();
		int imageY = y + (height - parent.getImageHeight()) / 2;
		gc.drawImage(image, 
			       imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height,
			       xDraw, imageY, imageBounds.width, imageHeight);
		xDraw += imageBounds.width + INTERNAL_SPACING;
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
	int textY = y + (height - textHeight(gc)) / 2; 	
	gc.drawString(text, xDraw, textY, true);
	//gc.drawText(text, xDraw, textY, true, true);
	
	gc.setForeground(parent.getForeground());
}
private String shortenText(GC gc, String text, int width) {
	if (gc.textExtent(text).x <= width) return text;
	
	int ellipseWidth = gc.textExtent(ellipsis).x;
	int length = text.length();
	int end = length - 1;
	while (end > 0) {
		text = text.substring(0, end);
		int l1 = gc.textExtent(text).x;
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
	return  Math.max(textHeight(gc), imageHeight()) + TOP_MARGIN + BOTTOM_MARGIN; 
}
/**
 * Answer the preferred width of the receiver for the GC.
 */
int preferredWidth(GC gc) {
	int tabWidth = LEFT_MARGIN + RIGHT_MARGIN;
	Image image = getImage();
	if (image != null) tabWidth += imageWidth() + INTERNAL_SPACING;
	tabWidth += textWidth(gc);
	if (parent.showClose) tabWidth += parent.closeBar.getSize().x;
	return tabWidth;
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
	Image oldImage = getImage();
		
	super.setImage(image);
	if (image == null || !image.equals(oldImage)) {
		parent.itemChanged(this);
	}
}
public void setDisabledImage (Image image) {
	Image oldImage = getDisabledImage();
	
	disabledImage = image;
	if (disabledImage == null || !disabledImage.equals(oldImage)) {
		parent.itemChanged(this);
	}
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
	String oldText = getText();

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
	toolTipText = string;
}
/**
 * Answer the text height.
 */
private int textHeight(GC gc) {
	int textHeight = 0;
	
	if (isDisposed()) return textHeight;
	
	String text = getText();
	if (text != null) {
		textHeight = gc.stringExtent(text).y;
	}
	return textHeight;
}
/**
 * Answer the text width.
 */
private int textWidth(GC gc) {	
	int textWidth = 0;
	
	if (isDisposed()) return 0;
	
	String text = getText();
	if (text != null) {
		textWidth = gc.stringExtent(text).x;
	}
	return textWidth;
}
}

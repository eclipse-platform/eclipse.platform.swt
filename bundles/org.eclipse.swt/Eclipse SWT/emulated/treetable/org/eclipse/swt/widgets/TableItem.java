/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import java.util.Vector;

/**
 * Instances of this class represent a selectable user interface object
 * that represents an item in a table.
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
public class TableItem extends SelectableItem {
	private static final int FIRST_COLUMN_IMAGE_INDENT = 2;	// Space in front of image - first column only
	private static final int FIRST_COLUMN_TEXT_INDENT = 4;	// Space in front of text - first column only	
	private static final int TEXT_INDENT_NO_IMAGE = 2;		// Space in front of item text when no item in the column has an image - first column only
	private static final int TEXT_INDENT = 6;				// Space in front of item text - all other columns
	private static final int SELECTION_PADDING = 6;			// Space behind text in a selected item

	private Vector dataLabels = new Vector();				// Original text set by the user. Items that don't 
															// have a label are represented by a null slot
	private String[] trimmedLabels;							// Text that is actually displayed, may be trimmed 
															// to fit the column
	private Vector images = new Vector();					// Item images. Items that don't have an image 
															// are represented by a null slot
	private Point selectionExtent;							// Size of the rectangle drawn to indicate a 
															// selected item.
	private int imageIndent = 0;							// the factor by which the item image and check box, if any, 
															// are indented. The multiplier is the image width.
	private int index;										// index of the item in the parent widget

	Color background = null;
	Color foreground = null;
	Font font = null;
	Color [] cellBackground, cellForeground;
	Font [] cellFont;
	int [] textWidths;
	boolean cached;
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableItem(Table parent, int style) {
	this(parent, style, checkNull(parent).getItemCount());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableItem(Table parent, int style, int index) {
	super(parent, style);
	trimmedLabels = new String[parent.internalGetColumnCount()];
	parent.addItem(this, index);
}

/**
 * Calculate the size of the rectangle drawn to indicate a selected 
 * item. This is also used to draw the selection focus rectangle. 
 * The selection extent is calculated for the first column only (the 
 * only column the selection is drawn in).
 */
void calculateSelectionExtent() {
	Table parent = getParent();
	TableColumn column = parent.internalGetColumn(TableColumn.FIRST);
	GC gc = new GC(parent);
	gc.setFont(getFont());
	String trimmedText = getText(gc, column);
	int gridLineWidth = parent.getGridLineWidth();
	
	if (trimmedText != null) {
		selectionExtent = new Point(gc.stringExtent(trimmedText).x, parent.getItemHeight());
		selectionExtent.x += getTextIndent(TableColumn.FIRST) + SELECTION_PADDING;
		selectionExtent.x = Math.min(
			selectionExtent.x, column.getWidth() - getImageStopX(column.getIndex()) - gridLineWidth);
		if (parent.getLinesVisible() == true) {
			selectionExtent.y -= gridLineWidth;
		}
	}
	gc.dispose();
}
/**
 * Throw an SWT.ERROR_NULL_ARGUMENT exception if 'table' is null.
 * Otherwise return 'table'
 */
static Table checkNull(Table table) {
	if (table == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return table;
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void clear() {
	super.clear();
	dataLabels = new Vector();
	trimmedLabels = new String[getParent().internalGetColumnCount()];
	images = new Vector();
	selectionExtent = null;
	background = foreground = null;
	font = null;
	cellBackground = cellForeground = null;
	cellFont = null;
	textWidths = null;
	cached = false;
}
void clearTextWidths() {
	textWidths = null;
}
void clearTextWidths(int columnIndex) {
	if (textWidths != null) {
		textWidths [columnIndex] = 0;
	}
}
public void dispose() {
	if (isDisposed()) return;
	Table parent = getParent();
	parent.removeItem(this);
	super.dispose();
}
void doDispose() {
	dataLabels = null;
	trimmedLabels = null;
	images = null;
	selectionExtent = null;
	foreground = background = null;
	font = null;
	cellForeground = cellBackground = null;
	cellFont = null;
	textWidths = null;
	super.doDispose();
}

/**
 * Draw the image of the receiver for column 'index' at
 * 'destinationPosition' using 'gc'.
 * Stretch/shrink the image to the fixed image size of the receiver's 
 * parent.
 * @param gc - GC to draw on. 
 * @param destinationPosition - position on the GC to draw at.
 * @param index - index of the image to draw
 * @return Answer the position where drawing stopped.
 */
Point drawImage(GC gc, Point destinationPosition, int index) {
	Table parent = getParent();
	Image image = getImage(index);
	Rectangle sourceImageBounds;
	Point destinationImageExtent = parent.getImageExtent();
	int imageOffset;
	int itemHeight = parent.getItemHeight();
	
	if (image != null) {
		sourceImageBounds = image.getBounds();
		if (index == TableColumn.FIRST){
			gc.setBackground(getBackground(index));
			gc.fillRectangle(
				destinationPosition.x, destinationPosition.y,			
				destinationImageExtent.x, itemHeight);
		} else {
			// full row select would obscure transparent images in all but the first column
			// so always clear the image area in this case. Fixes 1FYNITC
			if ((parent.getStyle() & SWT.FULL_SELECTION) != 0) {
				gc.fillRectangle(
					destinationPosition.x, destinationPosition.y,			
					destinationImageExtent.x, destinationImageExtent.y);
			}
		}
		imageOffset = (itemHeight - destinationImageExtent.y) / 2;
		gc.drawImage(
			image, 0, 0, 													// source x, y
			sourceImageBounds.width, sourceImageBounds.height, 				// source width, height
			destinationPosition.x, destinationPosition.y + imageOffset,										// destination x, y
			destinationImageExtent.x, destinationImageExtent.y);			// destination width, height
	}
	if (((index == TableColumn.FIRST &&										// always add the image width for the first column 
 	 	  parent.hasFirstColumnImage() == true) ||							// if any item in the first column has an image
		 (index != TableColumn.FIRST && 									// add the image width if it's not the first column
		  image != null)) &&										 		// only when the item actually has an image
		destinationImageExtent != null) {									
		destinationPosition.x += destinationImageExtent.x;
	}
	return destinationPosition;
}
/**
 * Draw the label of the receiver for column 'index' at 'position'
 * using 'gc'. 
 * The background color is set to the selection background color if 
 * the item is selected and the text is drawn for the first column.
 * @param gc - GC to draw on. 
 * @param position - position on the GC to draw at.
 * @param index - specifies which subitem text to draw
 */
void drawText(String label, GC gc, Point position, int index) {
	Table parent = getParent();
	int textOffset, alignmentOffset;

	if (label != null) {
		gc.setFont(getFont(index));
		boolean drawSelection = (index == TableColumn.FIRST || (parent.getStyle() & SWT.FULL_SELECTION) != 0) &&
			((parent.style & SWT.HIDE_SELECTION) == 0 || parent.isFocusControl());
		if (isSelected() == true && drawSelection == true) {
			gc.setForeground(getSelectionForegroundColor());
		} else {
			gc.setForeground(getForeground(index));
		}
		alignmentOffset = getAlignmentOffset (index, getBounds(index).width, gc);
		textOffset = (parent.getItemHeight() - parent.getFontHeight()) / 2;			// vertically center the text
		gc.drawString(label, position.x + alignmentOffset, position.y + textOffset, true);
	}
}
int getAlignmentOffset(int columnIndex, int columnWidth, GC gc) {
	Table parent = getParent();
	TableColumn column = parent.internalGetColumn (columnIndex);
	Image image = getImage(columnIndex);	
	int alignmentOffset = 0;
	int alignment = column.getAlignment();
	String label  = getText(gc, column);
	int imageWidth = 0;
	int textWidth = gc.stringExtent (label).x;
	Point imageExtent = parent.getImageExtent();
	if (((columnIndex == TableColumn.FIRST &&								// always add the image width for the first column 
 	 	  parent.hasFirstColumnImage() == true) ||							// if any item in the first column has an image
		 (columnIndex != TableColumn.FIRST && 								// add the image width if it's not the first column
		  image != null)) &&										 		// only when the item actually has an image
		imageExtent != null) {									
		textWidth += imageExtent.x;
	}
	if ((alignment & SWT.RIGHT) != 0) {
		alignmentOffset = columnWidth - textWidth - imageWidth - TEXT_INDENT - TEXT_INDENT;
	}
	if ((alignment & SWT.CENTER) != 0) {
		alignmentOffset = ((columnWidth - textWidth) / 2) - imageWidth - TEXT_INDENT;
	}
	if (alignmentOffset < 0) alignmentOffset = 0;
	return alignmentOffset;
}
/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public Color getBackground(){
	checkWidget ();
	if (background != null) return background;
	Table parent = getParent ();
	return parent.getBackground ();
}
/**
 * Returns the background color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Color getBackground (int index) {
	checkWidget ();
	Table parent = getParent ();
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return getBackground ();
	if (cellBackground == null || cellBackground [index] == null) return getBackground ();
	return cellBackground [index];
}
/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent at a column in the table.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds(int index) {
	checkWidget();
	Rectangle itemBounds;
	Rectangle columnBounds;
	Rectangle checkboxBounds;
	Table parent = getParent();
	TableColumn column;
	int itemIndex = parent.indexOf(this);
	int itemHeight = parent.getItemHeight();
	int gridLineWidth = parent.getLinesVisible() ? parent.getGridLineWidth() : 0;
	int itemYPos;
	
	if (itemIndex == -1 || index < 0 || index >= parent.internalGetColumnCount()) {
		itemBounds = new Rectangle(0, 0, 0, 0);
	}
	else {
		column = parent.internalGetColumn(index);
		columnBounds = column.getBounds();
		itemYPos = columnBounds.y + itemHeight * itemIndex;
		itemBounds = new Rectangle(
			columnBounds.x, itemYPos, 
			columnBounds.width - gridLineWidth, itemHeight - gridLineWidth);
		if (index == TableColumn.FIRST) {
			if (isCheckable() == true) {
				checkboxBounds = getCheckboxBounds();
				itemBounds.x = checkboxBounds.x + checkboxBounds.width + CHECKBOX_PADDING;	// add checkbox start, width and space behind checkbox
				itemBounds.width -= itemBounds.x;
			}
			else {
				int imageIndent = getImageIndentPixel();
				itemBounds.x += imageIndent;
				itemBounds.width -= imageIndent;
			}
		}
	}
	return itemBounds;
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the checked state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked() {
	checkWidget();
	return super.getChecked();
}
/**
 * Answer the x position of the item check box
 */
int getCheckboxXPosition() {
	return getImageIndentPixel();
}
/**
 * Answer the item labels set by the user.
 * These may not be the same as those drawn on the screen. The latter 
 * may be trimmed to fit the column. Items that don't have a label are 
 * represented by a null slot in the vector.
 * @return Vector - the item labels set by the user.
 */
Vector getDataLabels() {
	return dataLabels;
}
/**
 * Return the position at which the string starts that is used 
 * to indicate a truncated item text.
 * @param columnIndex - index of the column for which the position of 
 *	the truncation replacement should be calculated
 * @param columnWidth - width of the column for which the position of 
 *	the truncation replacement should be calculated
 * @return -1 when the item text is not truncated
 */
int getDotStartX(int columnIndex, int columnWidth) {
	GC gc;
	Table parent = getParent();
	String label = getText(columnIndex);
	int alignment = parent.internalGetColumn (columnIndex).getAlignment();
	int dotStartX = -1;
	int maxWidth;

	if (label != null) {
		gc = new GC(parent);
		gc.setFont (getFont());
		dotStartX = getAlignmentOffset(columnIndex, columnWidth, gc);
		if ((alignment & SWT.LEFT) != 0) {
			maxWidth = getMaxTextWidth(columnIndex, columnWidth);
			label = parent.trimItemText(label, maxWidth, gc);
			if (label.endsWith(Table.DOT_STRING)) {
				int dotsWidth = gc.stringExtent(Table.DOT_STRING).x;
				dotStartX = gc.stringExtent(label).x - dotsWidth;
				// add indents, margins and image width
				dotStartX += getImageStopX(columnIndex);
				dotStartX += getTextIndent(columnIndex);
			}
		}
		gc.dispose();		
	}
	return dotStartX;
}

/**
 * Returns the font that the receiver will use to paint textual information for this item.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Font getFont () {
	checkWidget ();
	if (font != null) return font;
	Table parent = getParent ();
	return parent.getFont ();
}

/**
 * Returns the font that the receiver will use to paint textual information
 * for the specified cell in this item.
 *
 * @param index the column index
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Font getFont (int index) {
	checkWidget ();
	Table parent = getParent ();
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return getFont ();
	if (cellFont == null || cellFont [index] == null) return getFont ();
	return cellFont [index];
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public Color getForeground(){
	checkWidget ();
	if (foreground != null) return foreground;
	Table parent = getParent ();
	return parent.getForeground ();
}
/**
 * 
 * Returns the foreground color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public Color getForeground (int index) {
	checkWidget ();
	Table parent = getParent ();
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return getForeground ();
	if (cellForeground == null || cellForeground [index] == null) return getForeground ();
	return cellForeground [index];
}
/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed() {
	checkWidget();
	return super.getGrayed();
}
public Image getImage() {
	checkWidget();
	return getImage(0);
}
/**
 * Returns the image stored at the given column index in the receiver,
 * or null if the image has not been set or if the column does not exist.
 *
 * @param index the column index
 * @return the image stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage(int columnIndex) {
	checkWidget();
	Image image = null;
	Vector images = getImages();
	int itemIndex = getIndex();
	
	if (itemIndex != -1 && columnIndex >= 0 && columnIndex < images.size()) {
		image = (Image) images.elementAt(columnIndex);
	}
	return image;
}
/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * table.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding image rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getImageBounds(int index) {
	checkWidget();
	Table parent = getParent();
	int itemIndex = parent.indexOf (this);
	int imageWidth = 0;
	Point imageExtent = parent.getImageExtent();
	Rectangle imageBounds = getBounds(index);
	
	if (itemIndex == -1) {
		imageBounds = new Rectangle(0, 0, 0, 0);
	}
	else
	if (imageExtent != null) {
		if (index == TableColumn.FIRST || getImage(index) != null) {
			imageWidth = imageExtent.x;
		}
	}
	imageBounds.width = imageWidth;
	return imageBounds;
}
/**
 * Gets the image indent.
 *
 * @return the indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getImageIndent() {
	checkWidget();
	int index = getParent().indexOf(this);
	
	if (index == -1) {
		return 0;
	}
	return imageIndent;
}
/**
 * Answer the number of pixels the image in the first column is 
 * indented. Calculation starts at the column start and counts 
 * all pixels except the check box.
 */
int getImageIndentPixel() {
	int indentPixel = FIRST_COLUMN_IMAGE_INDENT;
	Point imageExtent = getParent().getImageExtent();
	
	if (imageExtent != null) {
		indentPixel += imageExtent.x * getImageIndent();
	}
	return indentPixel;
}
/**
 * Answer the item images set by the user. Items that don't have an 
 * image are represented by a null slot in the vector.
 */
Vector getImages() {
	return images;
}
/**
 * Calculate the x coordinate where the item image of column 
 * 'columnIndex' stops.
 * @param columnIndex - the column for which the stop position of the 
 *	image should be calculated.
 */
int getImageStopX(int columnIndex) {
	int imageStopX = 0;
	Table parent = getParent();
	Rectangle checkboxBounds;

	if (columnIndex == TableColumn.FIRST) {
		if (isCheckable() == true) {
			checkboxBounds = getCheckboxBounds();
			imageStopX += checkboxBounds.x + checkboxBounds.width + CHECKBOX_PADDING;
		}
		else {
			imageStopX = getImageIndentPixel();
		}
	}
	if (((columnIndex == TableColumn.FIRST &&				// always add the image width for the first column 
 	 	  parent.hasFirstColumnImage() == true) ||			// if any item in the first column has an image
		 (columnIndex != TableColumn.FIRST && 				// add the image width if it's not the first column
		  getImage(columnIndex) != null)) &&		 		// only when the item actually has an image
		parent.getImageExtent() != null) {									
		imageStopX += parent.getImageExtent().x;
	}
	return imageStopX;
}
/**
 * Return the index of the item in its parent widget.
 */
int getIndex() {
	return index;
}
/**
 * Return the item extent in the specified column
 * The extent includes the actual width of the item including checkbox, 
 * image and text.
 */
Point getItemExtent(TableColumn column) {
	Table parent = getParent();
	int columnIndex = column.getIndex();
	Point extent = new Point(getImageStopX(columnIndex), parent.getItemHeight() - parent.getGridLineWidth());
	GC gc = new GC(parent);
	gc.setFont(getFont());
	String trimmedText = getText(gc, column);

	if (trimmedText != null && trimmedText.length() > 0) {
		extent.x += gc.stringExtent(trimmedText).x + getTextIndent(columnIndex);
	}
	if (columnIndex == TableColumn.FIRST) {
		extent.x += SELECTION_PADDING;
	}
	gc.dispose();		
	return extent;
}
/**
 * Answer the maximum width in pixel of the text that fits in the 
 * column identified by 'columnIndex' without trimming the text.
 * @param columnIndex - the column for which the maximum text width
 *	should be calculated.
 * @param columnWidth - width of the column 'columnIndex'
 */
int getMaxTextWidth(int columnIndex, int columnWidth) {
	int itemWidth = getImageStopX(columnIndex) + getTextIndent(columnIndex) * 2;
	return columnWidth - itemWidth;
}
/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent() {
	checkWidget();
	return (Table) super.getSelectableParent();
}
/**
 * Answer the width of the item required to display the complete contents.
 */
int getPreferredWidth(int index) {
	int size = getImageStopX(index);
	String text = (String) getDataLabels().elementAt (index);
	if (text != null) {
		size += getTextWidth (index) + getTextIndent (index) * 2 + 1;
	}
	return size;
}
/**
 * Return the size of the rectangle drawn to indicate a selected item.
 * This is also used to draw the selection focus rectangle and drop 
 * insert marker. 
 * Implements SelectableItem#getSelectionExtent
 */
Point getSelectionExtent() {
	Table parent = getParent();
	Point extent;
	
	if ((parent.getStyle() & SWT.FULL_SELECTION) == 0) {			// regular, first column, selection?
		if (selectionExtent == null) {
			calculateSelectionExtent();
		}
		extent = selectionExtent;
	}
	else {
		extent = parent.getFullSelectionExtent(this);
	}
	return extent;
}
/**
 * Return the x position of the selection rectangle
 * Implements SelectableItem#getSelectionX
 */
int getSelectionX() {
	return getImageStopX(TableColumn.FIRST) + getParent().getHorizontalOffset();
}
public String getText() {
	checkWidget();
	return getText(0);
}
/**
 * Returns the text stored at the given column index in the receiver,
 * or empty string if the text has not been set.
 *
 * @param index the column index
 * @return the text stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_TEXT - if the column at index does not exist</li>
 * </ul>
 */
public String getText(int columnIndex) {
	checkWidget();
	int itemIndex = getParent().indexOf(this);
	Vector labels = getDataLabels();
	String label = null;
	
	if (itemIndex == -1) {
		error(SWT.ERROR_CANNOT_GET_TEXT);
	}	
	if (columnIndex >= 0 && columnIndex < labels.size()) {
		label = (String) labels.elementAt(columnIndex);
	}
	if (label == null) {
		label = "";			// label vector is initialized with null instead of empty Strings
	}
	return label;
}
/**
 * Answer the text that is going to be drawn in 'column'. This 
 * text may be a trimmed copy of the original text set by the 
 * user if it doesn't fit into the column. In that case the last 
 * characters are replaced with Table.DOT_STRING.
 * A cached copy of the trimmed text is returned if available.
 * @param gc - GC to use for measuring the text extent
 * @param column - TableColumn for which the text should be returned
 */
String getText(GC gc, TableColumn column) {
	int columnIndex = column.getIndex();
	String label = getTrimmedText(columnIndex);
	int maxWidth;

	if (label == null) {
		gc.setFont(getFont());
		maxWidth = getMaxTextWidth(columnIndex, column.getWidth());
		label = getParent().trimItemText(getText(columnIndex), maxWidth, gc);
	}
	return label;
}
/**
 * Answer the indent of the text in column 'columnIndex' in pixel.
 * This indent is used in front of and behind the item text.
 * @param columnIndex - specifies the column for which the indent 
 *	should be calculated.
 */
int getTextIndent(int columnIndex) {
	int textIndent;

	if (columnIndex == TableColumn.FIRST) {
		if (getParent().hasFirstColumnImage() == false) {
			textIndent = TEXT_INDENT_NO_IMAGE;
		}
		else {
			textIndent = FIRST_COLUMN_TEXT_INDENT;
		}
	}
	else {
		textIndent = TEXT_INDENT;
	}
	return textIndent;
}
int getTextWidth (int columnIndex) {
	if (textWidths == null) {
		int count = Math.max (1, getParent ().getColumnCount ());
		textWidths = new int [count];
	}
	String text = (String) getDataLabels().elementAt (columnIndex);
	if (text != null && textWidths [columnIndex] == 0 && text.length () > 0) {
		GC gc = new GC (getParent ());
		gc.setFont (getFont (columnIndex));
		textWidths [columnIndex] = gc.stringExtent (text).x;
		gc.dispose ();
	}
	return textWidths [columnIndex];
}
/**
 * Answer the cached trimmed text for column 'columnIndex'. 
 * Answer null if it hasn't been calculated yet.
 * @param columnIndex - specifies the column for which the 
 *	trimmed text should be answered.
 */
String getTrimmedText(int columnIndex) {
	String label = null;
	String labels[] = getTrimmedTexts();

	if (columnIndex < labels.length) {
		label = labels[columnIndex];
	}
	return label;
}
/**
 * Answer an array of cached trimmed labels.
 */
String [] getTrimmedTexts() {
	return trimmedLabels;
}
/**
 * Ensure that the image and label vectors have at least 
 * 'newSize' number of elements.
 */
void growVectors(int newSize) {
	Vector images = getImages();
	Vector labels = getDataLabels();

	if (newSize > images.size()){
		images.setSize(newSize);
	}
	if (newSize > labels.size()){
		labels.setSize(newSize);
	}
}
/**
 * Insert 'column' into the receiver.
 */
void insertColumn(TableColumn column) {	
	Vector data = getDataLabels();
	Vector images = getImages();
	String stringData[];
	Image imageData[];
	int index = column.getIndex();

	if (index < data.size()) {
		data.insertElementAt(null, index);
	}
	else {
		data.addElement(null);
	}
	stringData = new String[data.size()];
	data.copyInto(stringData);
	setText(stringData);

	if (index < images.size()) {	
		images.insertElementAt(null, index);
	}
	else {
		images.addElement(null);
	}
	imageData = new Image[images.size()];
	images.copyInto(imageData);
	setImage(imageData);
	
	String[] tempTrimmed = new String[trimmedLabels.length + 1];
	System.arraycopy(trimmedLabels, 0, tempTrimmed, 0, index);
	System.arraycopy(trimmedLabels, index, tempTrimmed, index + 1, trimmedLabels.length - index);
	trimmedLabels = tempTrimmed; 
	
}
/**
 * Sets the image at an index.
 *
 * @param image the new image (or null)
 *
 * @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
 *	when called from the wrong thread
 * @exception SWTError(ERROR_WIDGET_DISPOSED)
 *	when the widget has been disposed
 */
void internalSetImage(int columnIndex, Image image) {
	Vector images = getImages();
	boolean imageWasNull = false;
	Table parent = getParent();		
	
	if (columnIndex >= 0 && 
		columnIndex < parent.internalGetColumnCount()) {
		if (columnIndex >= images.size()) {
			growVectors(columnIndex + 1);
		}
		Image oldImage = (Image) images.elementAt(columnIndex);
		if (oldImage == null && image != null) {
			imageWasNull = true;
		}
		if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (image != null && image.type == SWT.ICON && image.equals (oldImage)) return;
		images.setElementAt(image, columnIndex);
		reset(columnIndex);						// new image may cause text to no longer fit in the column
		notifyImageChanged(columnIndex, imageWasNull);
	}
}
/**
 * Sets the widget text.
 *
 * The widget text for an item is the label of the
 * item or the label of the text specified by a column
 * number.
 *
 * @param index the column number
 * @param text the new text
 *
 */
void internalSetText(int columnIndex, String string) {
	Vector labels = getDataLabels();
	Table parent = getParent();
	String oldText;
	
	if (columnIndex >= 0 &&
		columnIndex < parent.internalGetColumnCount()) {
		if (columnIndex >= labels.size()) {
			growVectors(columnIndex + 1);
		}
		oldText = (String) labels.elementAt(columnIndex);
		if (string.equals(oldText) == false) {
			labels.setElementAt(string, columnIndex);
			clearTextWidths(columnIndex);
			reset(columnIndex);
			notifyTextChanged(columnIndex, oldText == null);
		}
	}
}
/**
 * Answer whether the click at 'xPosition' on the receiver is a 
 * selection click.
 * A selection click occurred when the click was behind the image
 * and before the end of the item text.
 * @return 
 *	true - 'xPosition' is a selection click.
 *	false - otherwise
 */
boolean isSelectionHit(int xPosition) {
	int itemStopX = getImageStopX(TableColumn.FIRST);
	Point selectionExtent = getSelectionExtent();

	if (selectionExtent != null) {
		itemStopX += selectionExtent.x;
	}
	return (xPosition > getCheckboxBounds().x + getCheckboxBounds().width) && (xPosition <= itemStopX);
}
/** 
 * The image for the column identified by 'columnIndex' has changed.
 * Notify the parent widget and supply redraw coordinates, if possible.
 * @param columnIndex - index of the column that has a new image.
 */
void notifyImageChanged(int columnIndex, boolean imageWasNull) {	
	Table parent = getParent();
	Rectangle changedColumnBounds;
	Image currentImage;
	int redrawStartX = 0;
	int redrawWidth = 0;
	int columnCount = parent.internalGetColumnCount();	

	if (columnIndex >= 0 && columnIndex < columnCount && parent.getVisibleRedrawY(this) != -1) {
		changedColumnBounds = parent.internalGetColumn(columnIndex).getBounds();
		currentImage = getImage(columnIndex);		
		redrawStartX = Math.max(0, getImageBounds(columnIndex).x);
		if (parent.getImageExtent() != null && imageWasNull == false && currentImage != null) {
			redrawWidth = getImageStopX(columnIndex);
		}
		else {
			redrawWidth = changedColumnBounds.width;
		}
		redrawWidth += changedColumnBounds.x - redrawStartX;
	}
	cached = true;
	parent.itemChanged(this, redrawStartX, redrawWidth);
}

/**
 * The label for the column identified by 'columnIndex' has changed.
 * Notify the parent widget and supply redraw coordinates, if possible.
 * @param columnIndex - index of the column that has a new label.
 */
void notifyTextChanged(int columnIndex, boolean textWasNull) {	
	Table parent = getParent();
	String text;
	Rectangle columnBounds;
	int redrawStartX = 0;
	int redrawWidth = 0;
	int columnCount = parent.internalGetColumnCount();

	if (columnIndex >= 0 && columnIndex < columnCount && parent.getVisibleRedrawY(this) != -1) {
		text = (String) getDataLabels().elementAt(columnIndex);
		columnBounds = parent.internalGetColumn(columnIndex).getBounds();
		redrawStartX = columnBounds.x;
		if (getImage(columnIndex) != null) {
			redrawStartX += getImageStopX(columnIndex);
		}
		redrawStartX = Math.max(0, redrawStartX);
		// don't redraw if text changed from null to empty string
		if (textWasNull == false || text.length() > 0) {
			redrawWidth = columnBounds.x + columnBounds.width - redrawStartX;
		}
	}
	cached = true;
	parent.itemChanged(this, redrawStartX, redrawWidth);
}
/**
 * Draw the receiver at 'paintPosition' in the column identified by 
 * 'columnIndex' using 'gc'.
 * @param gc - GC to use for drawing
 * @param paintPosition - position where the receiver should be drawing.
 * @param column - the column to draw in
 */
void paint(GC gc, Point paintPosition, TableColumn column) {
	int columnIndex = column.getIndex();
	String label = getText(gc, column);
	String oldLabel = getTrimmedText(columnIndex);
	Table parent = getParent ();
	int itemHeight = parent.getItemHeight ();
	
	if (label != null && label.equals(oldLabel) == false) {
		setTrimmedText(label, columnIndex);
		selectionExtent = null;		// force a recalculation next time the selection extent is needed
	}
	Color background = gc.getBackground();
	if (!isSelected() || ((parent.getStyle() & SWT.HIDE_SELECTION) != 0 && !parent.isFocusControl())) {
		int width = column.getBounds().width;
		int height = itemHeight;
		gc.setBackground(getBackground(columnIndex));
		gc.fillRectangle(paintPosition.x, paintPosition.y, width, height);
	} else {
		if (columnIndex == TableColumn.FIRST) {
			int width = getImageIndentPixel();
			int height = itemHeight;
			gc.setBackground(getBackground (columnIndex));
			gc.fillRectangle(paintPosition.x, paintPosition.y, width, height);
		} else {
			if ((parent.getStyle() & SWT.FULL_SELECTION) == 0) {
				int width = column.getBounds ().width;
				int height = itemHeight;
				gc.setBackground(getBackground (columnIndex));
				gc.fillRectangle(paintPosition.x, paintPosition.y, width, height);
			}
		}
	}
	if (columnIndex == TableColumn.FIRST) {
		paintPosition.x += getImageIndentPixel();
		if (isCheckable() == true) {
			paintPosition = drawCheckbox(gc, paintPosition);
		}		
	}
	paintPosition = drawImage(gc, paintPosition, columnIndex);
	paintPosition.x += getTextIndent(columnIndex);
	drawText(label, gc, paintPosition, columnIndex);
	gc.setBackground(background);
}
/**
 * Remove 'column' from the receiver.
 */
void removeColumn(TableColumn column) {
	Vector data = getDataLabels();
	Vector images = getImages();
	String stringData[];
	Image imageData[];
	int index = column.getIndex();

	if (index < data.size()) {
		data.removeElementAt(index);
		stringData = new String[data.size()];
		data.copyInto(stringData);
		setText(stringData);
	}
	if (index < images.size()) {
		images.removeElementAt(index);
		imageData = new Image[images.size()];
		images.copyInto(imageData);
		setImage(imageData);
	}
	
	if (trimmedLabels.length == 1) {
		trimmedLabels = new String[0];
	} else {
		String[] tempTrimmed = new String[trimmedLabels.length - 1];
		System.arraycopy(trimmedLabels, 0, tempTrimmed, 0, index);	
		System.arraycopy(trimmedLabels, index +1, tempTrimmed, index, trimmedLabels.length - index -1);
		trimmedLabels = tempTrimmed; 
	}

}
/**
 * Reset the cached trimmed label for the sub item identified by 
 * 'index'.
 * @param index - index of the label that should be reset.
 */
void reset(int index) {
	String trimmedLabels[] = getTrimmedTexts();

	if (index >= 0 && index < trimmedLabels.length) {
		trimmedLabels[index] = null;
	}
	if (index == TableColumn.FIRST) {
		selectionExtent = null;
	}
}

void redraw(){
	cached = true;
	Table parent = getParent();
	int y = parent.getRedrawY(this);
	parent.redraw(0, y, parent.getClientArea().width, parent.getItemHeight(), false);
}
/**
 * Sets the receiver's background color to the color specified
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
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (background == color) return;
	if (background != null && background.equals (color)) return;
	background = color;
	redraw();
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
public void setBackground (int index, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Table parent = getParent ();
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (cellBackground == null) {
		cellBackground = new Color [count];
	}
	if (cellBackground [index] == color) return;
	if (cellBackground [index] != null && cellBackground [index].equals (color)) return;
	cellBackground [index] = color;
	redraw ();
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
public void setFont (Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.font == font) return;
	if (this.font != null && this.font.equals (font)) return;
	this.font = font;
	clearTextWidths ();
	redraw ();
}

/**
 * Sets the font that the receiver will use to paint textual information
 * for the specified cell in this item to the font specified by the 
 * argument, or to the default font for that kind of control if the 
 * argument is null.
 *
 * @param index the column index
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
public void setFont (int index, Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Table parent = getParent ();
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (cellFont == null) {
		cellFont = new Font [count];
	}
	if (cellFont [index] == font) return;
	if (cellFont [index] != null && cellFont [index].equals (font)) return;
	cellFont [index] = font;
	clearTextWidths (index);
	redraw ();
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
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (foreground == color) return;
	if (foreground != null && foreground.equals (color)) return;
	foreground = color;
	redraw(); 
}
/**
 * Sets the foreground color at the given column index in the receiver 
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
public void setForeground (int index, Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Table parent = getParent ();
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return;
	if (cellForeground == null) {
		cellForeground = new Color [count];
	}
	if (cellForeground [index] == color) return;
	if (cellForeground [index] != null && cellForeground [index].equals (color)) return;
	cellForeground [index] = color;
	redraw ();
}
/**
 * Sets the image for multiple columns in the Table. 
 * 
 * @param images the array of new images
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the images has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage(Image [] images) {
	checkWidget();
	if (images == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	if (getParent().indexOf(this) == -1) {	
		return;
	}	
	for (int i = 0; i < images.length; i++) {
		internalSetImage(i, images[i]);
	}
}
/**
 * Sets the receiver's image at a column.
 *
 * @param index the column index
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage(int index, Image image) {
	checkWidget();	
	if (getParent().indexOf(this) != -1) {
		internalSetImage(index, image);
	}
}
public void setImage(Image image) {
	checkWidget();
	setImage(0, image);
}
/**
 * Sets the indent of the first column's image, expressed in terms of the image's width.
 *
 * @param indent the new indent
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImageIndent(int indent) {
	checkWidget();
	Table parent = getParent();
	TableColumn column;
	int index = parent.indexOf(this);

	if (index != -1 && indent >= 0 && indent != imageIndent) {
		imageIndent = indent;
		column = parent.internalGetColumn(TableColumn.FIRST);
		parent.redraw(
			0, parent.getRedrawY(this), 
			column.getWidth(), parent.getItemHeight(), false);
		cached = true;
	}
}
/**
 * Sets the text for multiple columns in the table. 
 * 
 * @param strings the array of new strings
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText(String [] strings) {
	checkWidget();
	if (strings == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	if (getParent().indexOf(this) == -1) {	
		return;
	}
	for (int i = 0; i < strings.length; i++) {
		String string = strings[i];
		if (string != null) {
			internalSetText(i, string);
		}
	}
}
/**
 * Sets the receiver's text at a column
 *
 * @param index the column index
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
public void setText (int index, String string) {
	checkWidget();
	if (string == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	if (getParent().indexOf(this) != -1) {
		internalSetText(index, string);
	}
}
public void setText(String text) {
	checkWidget();
	if (text == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	setText(0, text);
}
/**
 * Set the trimmed text of column 'columnIndex' to label. The trimmed 
 * text is the one that is displayed in a column. It may be shorter than
 * the text originally set by the user via setText(...) to fit the 
 * column.
 * @param label - the text label of column 'columnIndex'. May be trimmed
 *	to fit the column.
 * @param columnIndex - specifies the column whose text label should be 
 *	set.
 */
void setTrimmedText(String label, int columnIndex) {
	String labels[] = getTrimmedTexts();

	if (columnIndex < labels.length) {
		labels[columnIndex] = label;
	}
}
/**
 * Sets the checked state of the checkbox for this item.  This state change 
 * only applies if the Table was created with the SWT.CHECK style.
 *
 * @param checked the new checked state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked(boolean checked) {
	checkWidget();
	super.setChecked(checked);
}
/**
 * Sets the grayed state of the checkbox for this item.  This state change 
 * only applies if the Table was created with the SWT.CHECK style.
 *
 * @param grayed the new grayed state of the checkbox; 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget();
	super.setGrayed(grayed);
}
/**
 * Set the index of this item in its parent widget to 'newIndex'.
 */
void setIndex(int newIndex) {
	index = newIndex;
}

}


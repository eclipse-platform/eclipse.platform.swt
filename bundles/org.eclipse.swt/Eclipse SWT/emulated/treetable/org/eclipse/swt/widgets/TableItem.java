/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TableItem extends Item {
	Table parent;
	int index = -1;
	boolean checked, grayed, cached;

	String[] texts;
	int[] textWidths = new int [1];	/* cached string measurements */
	int customWidth = -1;				/* width specified by Measure callback */
	int fontHeight;						/* cached item font height */
	int[] fontHeights;
	int imageIndent;
	Image[] images;
	Color foreground, background;
	String[] displayTexts;
	Color[] cellForegrounds, cellBackgrounds;
	Font font;
	Font[] cellFonts;
	
	static final int MARGIN_TEXT = 3;			/* the left and right margins within the text's space */

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
public TableItem (Table parent, int style) {
	this (parent, style, checkNull (parent).itemsCount);
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
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
public TableItem (Table parent, int style, int index) {
	this (parent, style, index, true);
}
TableItem (Table parent, int style, int index, boolean notifyParent) {
	super (parent, style);
	int validItemIndex = parent.itemsCount;
	if (!(0 <= index && index <= validItemIndex)) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	this.index = index;
	int columnCount = parent.columns.length;
	if (columnCount > 0) {
		displayTexts = new String [columnCount];
		if (columnCount > 1) {
			texts = new String [columnCount];
			textWidths = new int [columnCount];
			images = new Image [columnCount];
		}
	}
	if (notifyParent) parent.createItem (this);
}
/*
 * Updates internal structures in the receiver and its child items to handle the creation of a new column.
 */
void addColumn (TableColumn column) {
	int index = column.getIndex ();
	int columnCount = parent.columns.length;

	if (columnCount > 1) {
		if (columnCount == 2) {
			texts = new String [2];
		} else {
			String[] newTexts = new String [columnCount];
			System.arraycopy (texts, 0, newTexts, 0, index);
			System.arraycopy (texts, index, newTexts, index + 1, columnCount - index - 1);
			texts = newTexts;
		}
		if (index == 0) {
			texts [1] = text;
			text = "";	//$NON-NLS-1$
		}

		if (columnCount == 2) {
			images = new Image [2];
		} else {
			Image[] newImages = new Image [columnCount];
			System.arraycopy (images, 0, newImages, 0, index);
			System.arraycopy (images, index, newImages, index + 1, columnCount - index - 1);
			images = newImages;
		}
		if (index == 0) {
			images [1] = image;
			image = null;
		}
		
		int[] newTextWidths = new int [columnCount];
		System.arraycopy (textWidths, 0, newTextWidths, 0, index);
		System.arraycopy (textWidths, index, newTextWidths, index + 1, columnCount - index - 1);
		textWidths = newTextWidths;
	} else {
		customWidth = -1;		/* columnCount == 1 */
	}

	/*
	 * The length of displayTexts always matches the parent's column count, unless this
	 * count is zero, in which case displayTexts is null.  
	 */
	String[] newDisplayTexts = new String [columnCount];
	if (columnCount > 1) {
		System.arraycopy (displayTexts, 0, newDisplayTexts, 0, index);
		System.arraycopy (displayTexts, index, newDisplayTexts, index + 1, columnCount - index - 1);
	}
	displayTexts = newDisplayTexts;

	if (cellBackgrounds != null) {
		Color[] newCellBackgrounds = new Color [columnCount];
		System.arraycopy (cellBackgrounds, 0, newCellBackgrounds, 0, index);
		System.arraycopy (cellBackgrounds, index, newCellBackgrounds, index + 1, columnCount - index - 1);
		cellBackgrounds = newCellBackgrounds;
	}
	if (cellForegrounds != null) {
		Color[] newCellForegrounds = new Color [columnCount];
		System.arraycopy (cellForegrounds, 0, newCellForegrounds, 0, index);
		System.arraycopy (cellForegrounds, index, newCellForegrounds, index + 1, columnCount - index - 1);
		cellForegrounds = newCellForegrounds;
	}
	if (cellFonts != null) {
		Font[] newCellFonts = new Font [columnCount];
		System.arraycopy (cellFonts, 0, newCellFonts, 0, index);
		System.arraycopy (cellFonts, index, newCellFonts, index + 1, columnCount - index - 1);
		cellFonts = newCellFonts;

		int[] newFontHeights = new int [columnCount];
		System.arraycopy (fontHeights, 0, newFontHeights, 0, index);
		System.arraycopy (fontHeights, index, newFontHeights, index + 1, columnCount - index - 1);
		fontHeights = newFontHeights;
	}

	if (index == 0 && columnCount > 1) {
		/* 
		 * The new second column may have more width available to it than it did when it was
		 * the first column if checkboxes are being shown, so recompute its displayText if needed. 
		 */
		if ((parent.style & SWT.CHECK) != 0) {
			GC gc = new GC (parent);
			gc.setFont (getFont (1, false));
			computeDisplayText (1, gc);
			gc.dispose ();
		}
	}
}
static Table checkNull (Table table) {
	if (table == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return table;
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void clear () {
	checked = grayed = false;
	texts = null;
	textWidths = new int [1];
	fontHeight = 0;
	fontHeights = null;
	images = null;
	foreground = background = null;
	displayTexts = null;
	cellForegrounds = cellBackgrounds = null;
	font = null;
	cellFonts = null;
	cached = false;
	text = "";
	image = null;

	int columnCount = parent.columns.length;
	if (columnCount > 0) {
		displayTexts = new String [columnCount];
		if (columnCount > 1) {
			texts = new String [columnCount];
			textWidths = new int [columnCount];
			images = new Image [columnCount];
		}
	}
}
void computeDisplayText (int columnIndex, GC gc) {
	if ((parent.style & SWT.VIRTUAL) != 0 && !cached) return;	/* nothing to do */

	int columnCount = parent.columns.length;
	if (columnCount == 0) {
		String text = getText (0, false);
		textWidths [columnIndex] = gc.stringExtent (text).x;
		return;
	}

	TableColumn column = parent.columns [columnIndex];
	int availableWidth = column.width - 2 * parent.getCellPadding () - 2 * MARGIN_TEXT;
	if (columnIndex == 0) {
		availableWidth -= parent.col0ImageWidth;
		if (parent.col0ImageWidth > 0) availableWidth -= Table.MARGIN_IMAGE;
		if ((parent.style & SWT.CHECK) != 0) {
			availableWidth -= parent.checkboxBounds.width;
			availableWidth -= Table.MARGIN_IMAGE;
		}
	} else {
		Image image = getImage (columnIndex, false);
		if (image != null) {
			availableWidth -= image.getBounds ().width;
			availableWidth -= Table.MARGIN_IMAGE;
		}
	}

	String text = getText (columnIndex, false);
	int textWidth = gc.stringExtent (text).x;
	if (textWidth <= availableWidth) {
		displayTexts [columnIndex] = text;
		textWidths [columnIndex] = textWidth;
		return;
	}

	/* Ellipsis will be needed, so subtract their width from the available text width */
	int ellipsisWidth = gc.stringExtent (Table.ELLIPSIS).x;
	availableWidth -= ellipsisWidth;
	if (availableWidth <= 0) {
		displayTexts [columnIndex] = Table.ELLIPSIS;
		textWidths [columnIndex] = ellipsisWidth;
		return;
	}

	/* Make initial guess. */
	int index = Math.min (availableWidth / gc.getFontMetrics ().getAverageCharWidth (), text.length ());
	textWidth = gc.stringExtent (text.substring (0, index)).x;

	/* Initial guess is correct. */
	if (availableWidth == textWidth) {
		displayTexts [columnIndex] = text.substring (0, index) + Table.ELLIPSIS;
		textWidths [columnIndex] = textWidth + ellipsisWidth;
		return;
	}

	/* Initial guess is too high, so reduce until fit is found. */
	if (availableWidth < textWidth) {
		do {
			index--;
			if (index < 0) {
				displayTexts [columnIndex] = Table.ELLIPSIS;
				textWidths [columnIndex] = ellipsisWidth;
				return;
			}
			text = text.substring (0, index);
			textWidth = gc.stringExtent (text).x;
		} while (availableWidth < textWidth);
		displayTexts [columnIndex] = text + Table.ELLIPSIS;
		textWidths [columnIndex] = textWidth + ellipsisWidth;
		return;
	}
	
	/* Initial guess is too low, so increase until overrun is found. */
	int previousWidth = 0;
	while (textWidth < availableWidth) {
		index++;
		previousWidth = textWidth;
		textWidth = gc.stringExtent (text.substring (0, index)).x;
	}
	displayTexts [columnIndex] = text.substring (0, index - 1) + Table.ELLIPSIS;
	textWidths [columnIndex] = previousWidth + ellipsisWidth;
}
void computeDisplayTexts (GC gc) {
	if ((parent.style & SWT.VIRTUAL) != 0 && !cached) return;	/* nothing to do */

	int columnCount = parent.columns.length;
	if (columnCount == 0) return;

	for (int i = 0; i < columnCount; i++) {
		gc.setFont (getFont (i, false));
		computeDisplayText (i, gc);
	}
}
/*
 * Computes the cached text widths.
 */
void computeTextWidths (GC gc) {
	if ((parent.style & SWT.VIRTUAL) != 0 && !cached) return;	/* nothing to do */

	int validColumnCount = Math.max (1, parent.columns.length);
	textWidths = new int [validColumnCount];
	for (int i = 0; i < textWidths.length; i++) {
		String value = getDisplayText (i);
		if (value != null) {
			gc.setFont (getFont (i, false));
			textWidths [i] = gc.stringExtent (value).x;
		}
	}
}
public void dispose () {
	if (isDisposed ()) return;
	Table parent = this.parent;
	int startIndex = index;
	int endIndex = parent.itemsCount - 1;
	dispose (true);
	parent.redrawItems (startIndex, endIndex, false);
}
void dispose (boolean notifyParent) {
	if (isDisposed ()) return;
	if (notifyParent) parent.destroyItem (this);
	super.dispose ();	/* super is intentional here */
	background = foreground = null;
	cellBackgrounds = cellForegrounds = null;
	font = null;
	cellFonts = null;
	images = null;
	texts = displayTexts = null;
	textWidths = fontHeights = null;
	parent = null;
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
 */
public Color getBackground () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (background != null) return background;
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
public Color getBackground (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getBackground ();
	if (cellBackgrounds == null || cellBackgrounds [columnIndex] == null) return getBackground ();
	return cellBackgrounds [columnIndex];
}
/**
 * Returns a rectangle describing the size and location of the receiver's
 * text relative to its parent.
 *
 * @return the bounding rectangle of the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Rectangle getBounds () {
	checkWidget ();
	return getBounds (true);
}
Rectangle getBounds (boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int x = getTextX (0);
	int width = textWidths [0] + 2 * MARGIN_TEXT;
	if (parent.columns.length > 0) {
		TableColumn column = parent.columns [0];
		int right = column.getX () + column.width;
		if (x + width > right) {
			width = Math.max (0, right - x);
		}
	}
	return new Rectangle (x, parent.getItemY (this), width, parent.itemHeight);
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
public Rectangle getBounds (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	TableColumn[] columns = parent.columns;
	int columnCount = columns.length;
	int validColumnCount = Math.max (1, columnCount);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) {
		return new Rectangle (0, 0, 0, 0);
	}
	/*
	 * If there are no columns then this is the bounds of the receiver's content.
	 */
	if (columnCount == 0) {
		int width = getContentWidth (0);
		return new Rectangle (
			getContentX (0),
			parent.getItemY (this),
			width,
			parent.itemHeight - 1);
	}
	
	TableColumn column = columns [columnIndex];
	if (columnIndex == 0) {
		/* 
		 * For column 0 this is bounds from the beginning of the content to the
		 * end of the column.
		 */
		int x = getContentX (0);
		int offset = x - column.getX ();
		int width = Math.max (0, column.width - offset - 1);		/* max is for columns with small widths */
		return new Rectangle (x, parent.getItemY (this) + 1, width, parent.itemHeight - 1);
	}
	/*
	 * For columns > 0 this is the bounds of the table cell.
	 */
	return new Rectangle (column.getX (), parent.getItemY (this) + 1, column.width, parent.itemHeight - 1);
}
/*
 * Returns the full bounds of a cell in a table, regardless of its content.
 */
Rectangle getCellBounds (int columnIndex) {
	int y = parent.getItemY (this);
	if (parent.columns.length == 0) {
		int width;
		if (customWidth != -1) {
			width = getContentX (0) + customWidth + parent.horizontalOffset;
		} else {
			int textPaintWidth = textWidths [0] + 2 * MARGIN_TEXT;
			width = getTextX (0) + textPaintWidth + parent.horizontalOffset;
		}
		return new Rectangle (-parent.horizontalOffset, y, width, parent.itemHeight);
	}
	TableColumn column = parent.columns [columnIndex];
	return new Rectangle (column.getX (), y, column.width, parent.itemHeight);
}
/*
 * Returns the bounds of the receiver's checkbox, or null if the parent's style does not
 * include SWT.CHECK.
 */
Rectangle getCheckboxBounds () {
	if ((parent.getStyle () & SWT.CHECK) == 0) return null;
	Rectangle result = parent.checkboxBounds;
	if (parent.columns.length == 0) {
		result.x = parent.getCellPadding () - parent.horizontalOffset;
	} else {
		result.x = parent.columns [0].getX () + parent.getCellPadding ();
	}
	result.y = parent.getItemY (this) + (parent.itemHeight - result.height) / 2;
	return result;
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
public boolean getChecked () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return checked;
}
int getContentWidth (int columnIndex) {
	int width = textWidths [columnIndex] + 2 * MARGIN_TEXT;
	if (columnIndex == 0) {
		width += parent.col0ImageWidth;
		if (parent.col0ImageWidth > 0) width += Table.MARGIN_IMAGE;
	} else {
		Image image = getImage (columnIndex, false);
		if (image != null) {
			width += image.getBounds ().width + Table.MARGIN_IMAGE;
		}
	}
	return width;
}
/*
 * Returns the x value where the receiver's content (ie.- its image or text) begins
 * for the specified column.
 */
int getContentX (int columnIndex) {
	int minX = parent.getCellPadding ();
	if (columnIndex == 0) {
		Rectangle checkboxBounds = getCheckboxBounds ();
		if (checkboxBounds != null) {
			minX += checkboxBounds.width + Table.MARGIN_IMAGE;
		}
	}

	if (parent.columns.length == 0) return minX - parent.horizontalOffset;	/* free first column */

	TableColumn column = parent.columns [columnIndex];
	int columnX = column.getX ();
	if ((column.style & SWT.LEFT) != 0) return columnX + minX;

	/* column is not left-aligned */
	int contentWidth = getContentWidth (columnIndex);
	int contentX = 0;
	if ((column.style & SWT.RIGHT) != 0) {
		contentX = column.width - parent.getCellPadding () - contentWidth;	
	} else {	/* SWT.CENTER */
		contentX = (column.width - contentWidth) / 2;
	}
	return Math.max (columnX + minX, columnX + contentX);
}
String getDisplayText (int columnIndex) {
	if (parent.columns.length == 0) return getText (0, false);
	String result = displayTexts [columnIndex];
	return result != null ? result : "";	//$NON-NLS-1$
}
/*
 * Returns the bounds that should be used for drawing a focus rectangle on the receiver
 */
Rectangle getFocusBounds () {
	int x = 0;
	TableColumn[] columns = parent.columns;
	int[] columnOrder = parent.getColumnOrder ();
	if ((parent.style & SWT.FULL_SELECTION) != 0) {
		int col0index = columnOrder.length == 0 ? 0 : columnOrder [0];
		if (col0index == 0) {
			x = getTextX (0);
		} else {
			x = -parent.horizontalOffset;
		}
	} else {
		x = getTextX (0);
	}

	if (columns.length > 0) {
		/* ensure that the focus x does not start beyond the right bound of column 0 */
		int rightX = columns [0].getX () + columns [0].width;
		x = Math.min (x, rightX - 1);
	}

	int width;
	if (columns.length == 0) {
		if (customWidth != -1) {
			width = customWidth;
		} else {
			width = textWidths [0] + 2 * MARGIN_TEXT;
		}
	} else {
		TableColumn column;
		if ((parent.style & SWT.FULL_SELECTION) != 0) {
			column = columns [columnOrder [columnOrder.length - 1]];
		} else {
			column = columns [0];
		}
		width = column.getX () + column.width - x - 1;
	}
	return new Rectangle (
		x,
		parent.getItemY (this) + (parent.linesVisible ? 1 : 0),
		width,
		parent.itemHeight - (parent.linesVisible ? 1 : 0));
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
	return getFont (true);
}
Font getFont (boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (font != null) return font;
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
public Font getFont (int columnIndex) {
	checkWidget ();
	return getFont (columnIndex, true);
}
Font getFont (int columnIndex, boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getFont (checkData);
	if (cellFonts == null || cellFonts [columnIndex] == null) return getFont (checkData);
	return cellFonts [columnIndex];
}
int getFontHeight () {
	if (fontHeight != 0) return fontHeight;
	return parent.fontHeight;
}
int getFontHeight (int columnIndex) {
	if (fontHeights == null || fontHeights [columnIndex] == 0) return getFontHeight ();
	return fontHeights [columnIndex];
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
 */
public Color getForeground () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (foreground != null) return foreground;
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
public Color getForeground (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getForeground ();
	if (cellForegrounds == null || cellForegrounds [columnIndex] == null) return getForeground ();
	return cellForegrounds [columnIndex];
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
public boolean getGrayed () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return grayed;
}
/*
 * Returns the bounds representing the clickable region that should select the receiver.
 */
Rectangle getHitBounds () {
	int[] columnOrder = parent.getColumnOrder ();
	int contentX = 0;
	if ((parent.style & SWT.FULL_SELECTION) != 0) {
		int col0index = columnOrder.length == 0 ? 0 : columnOrder [0];
		if (col0index == 0) {
			contentX = getContentX (0);
		} else {
			contentX = 0;
		}
	} else {
		contentX = getContentX (0);
	}
	
	int width = 0;
	TableColumn[] columns = parent.columns;
	if (columns.length == 0) {
		width = getContentWidth (0); 
	} else {
		/* 
		 * If there are columns then this spans from the beginning of the receiver's column 0
		 * image or text to the end of either column 0 or the last column (FULL_SELECTION).
		 */
		TableColumn column;
		if ((parent.style & SWT.FULL_SELECTION) != 0) {
			column = columns [columnOrder [columnOrder.length - 1]];
		} else {
			column = columns [0];
		}
		width = column.getX () + column.width - contentX;
	}
	return new Rectangle (contentX, parent.getItemY (this), width, parent.itemHeight);
}
public Image getImage () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getImage ();
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
public Image getImage (int columnIndex) {
	checkWidget ();
	return getImage (columnIndex, true);
}
Image getImage (int columnIndex, boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return null;
	if (columnIndex == 0) return super.getImage ();		/* super is intentional here */
	return images [columnIndex];
}
/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * table.  An empty rectangle is returned if index exceeds
 * the index of the table's last column.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding image rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getImageBounds (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return new Rectangle (0,0,0,0);

	int padding = parent.getCellPadding ();
	int startX = getContentX (columnIndex);
	int itemHeight = parent.itemHeight;
	int imageSpaceY = itemHeight - 2 * padding;
	int y = parent.getItemY (this);
	Image image = getImage (columnIndex, false); 
	int drawWidth = 0;
	if (columnIndex == 0) {
		/* for column 0 all images have the same width */
		drawWidth = parent.col0ImageWidth;
	} else {
		if (image != null) drawWidth = image.getBounds ().width;
	}
	return new Rectangle (startX, y + padding, drawWidth, imageSpaceY);
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
public int getImageIndent () {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return imageIndent;	// TODO
}
String getNameText () {
	if ((parent.style & SWT.VIRTUAL) != 0) {
		if (!cached) return "*virtual*"; //$NON-NLS-1$
	}
	return super.getNameText ();
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
public Table getParent () {
	checkWidget ();
	return parent;
}
/*
 * Returns the receiver's ideal width for the specified columnIndex.
 */
int getPreferredWidth (int columnIndex) {
	int width = 0;
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex, false));
	width += gc.stringExtent (getText (columnIndex, false)).x + 2 * MARGIN_TEXT;
	if (columnIndex == 0) {
		if (parent.col0ImageWidth > 0) {
			width += parent.col0ImageWidth;
			width += Table.MARGIN_IMAGE;
		}
	} else {
		Image image = getImage (columnIndex, false);
		if (image != null) {
			width += image.getBounds ().width;
			width += Table.MARGIN_IMAGE;
		}
	}

	if (parent.hooks (SWT.MeasureItem)) {
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		event.x = getContentX (columnIndex);
		event.y = parent.getItemY (this);
		event.width = width;
		event.height = parent.itemHeight;
		event.detail = isSelected () ? SWT.SELECTED : 0;
		parent.sendEvent (SWT.MeasureItem, event);
		if (parent.itemHeight != event.height) {
			parent.customHeightSet = true;
			boolean update = parent.setItemHeight (event.height + 2 * parent.getCellPadding ());
			if (update) parent.redraw ();
		}
		width = event.width;
	}

	gc.dispose ();
	if (columnIndex == 0 && (parent.style & SWT.CHECK) != 0) {
		width += parent.checkboxBounds.width;
		width += Table.MARGIN_IMAGE;
	}
	return width + 2 * parent.getCellPadding ();
}
public String getText () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getText ();
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
 */
public String getText (int columnIndex) {
	checkWidget ();
	return getText (columnIndex, true);
}
String getText (int columnIndex, boolean checkData) {
	if (checkData && !parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return "";	//$NON-NLS-1$
	if (columnIndex == 0) return super.getText (); /* super is intentional here */
	if (texts [columnIndex] == null) return "";	//$NON-NLS-1$
	return texts [columnIndex];
}
/**
 * Returns a rectangle describing the size and location
 * relative to its parent of the text at a column in the
 * table.  An empty rectangle is returned if index exceeds
 * the index of the table's last column.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding text rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public Rectangle getTextBounds (int columnIndex) {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	TableColumn[] columns = parent.columns;
	int columnCount = columns.length;
	int validColumnCount = Math.max (1, columnCount);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) {
		return new Rectangle (0, 0, 0, 0);
	}
	/*
	 * If there are no columns then this is the bounds of the receiver's content,
	 * starting from the text.
	 */
	if (columnCount == 0) {
		int x = getTextX (0) + MARGIN_TEXT;
		int width = Math.max (0, getContentX(0) + getContentWidth (0) - x);
		return new Rectangle (
			x,
			parent.getItemY (this),
			width,
			parent.itemHeight - 1);
	}
	
	TableColumn column = columns [columnIndex];
	if (columnIndex == 0) {
		/* 
		 * For column 0 this is bounds from the beginning of the content to the
		 * end of the column, starting from the text.
		 */
		int x = getTextX (0) + MARGIN_TEXT;
		int offset = x - column.getX ();
		int width = Math.max (0, column.width - offset - 1);		/* max is for columns with small widths */
		return new Rectangle (x, parent.getItemY (this) + 1, width, parent.itemHeight - 1);
	}
	/*
	 * For columns > 0 this is the bounds of the table cell, starting from the text.
	 */
	int x = getTextX (columnIndex) + MARGIN_TEXT;
	int offset = x - column.getX ();
	int width = Math.max (0, column.width - offset - MARGIN_TEXT);
	return new Rectangle (x, parent.getItemY (this) + 1, width, parent.itemHeight - 1);
}
/*
 * Returns the x value where the receiver's text begins.
 */
int getTextX (int columnIndex) {
	int textX = getContentX (columnIndex);
	if (columnIndex == 0) {
		textX += parent.col0ImageWidth;
		if (parent.col0ImageWidth > 0) textX += Table.MARGIN_IMAGE;
	} else {
		Image image = getImage (columnIndex, false);
		if (image != null) {
			textX += image.getBounds ().width + Table.MARGIN_IMAGE;	
		}
	}
	return textX;
}
/*
 * Answers a boolean indicating whether the receiver's y is within the current
 * viewport of the parent.
 */
boolean isInViewport () {
	int topIndex = parent.topIndex;
	if (index < topIndex) return false;
	int visibleCount = parent.clientArea.height / parent.itemHeight;
	return index <= topIndex + visibleCount;
}
boolean isSelected () {
	return parent.getSelectionIndex (this) != -1;
}
/*
 * The backgroundOnly argument indicates whether the item should only
 * worry about painting its background color and selection.
 *
 * Returns a boolean indicating whether to abort drawing focus on the item.
 * If the receiver is not the current focus item then this value is irrelevant.
 */
boolean paint (GC gc, TableColumn column, boolean backgroundOnly) {
	if (!parent.checkData (this, true)) return false;
	int columnIndex = 0, x = 0;
	if (column != null) {
		columnIndex = column.getIndex ();
		x = column.getX ();
	}

	/* 
	 * Capture GC attributes that will need to be restored later in the paint
	 * process to ensure that the item paints as intended without being affected
	 * by GC changes made in MeasureItem/EraseItem/PaintItem callbacks.
	 */
	int oldAlpha = gc.getAlpha ();
	boolean oldAdvanced = gc.getAdvanced ();
	int oldAntialias = gc.getAntialias ();
	Pattern oldBackgroundPattern = gc.getBackgroundPattern ();
	Pattern oldForegroundPattern = gc.getForegroundPattern ();
	int oldInterpolation = gc.getInterpolation ();
	int oldTextAntialias = gc.getTextAntialias ();
	boolean isSelected = isSelected ();

	if (parent.hooks (SWT.MeasureItem)) {
		int contentWidth = getContentWidth (columnIndex);
		int contentX = getContentX (columnIndex);
		gc.setFont (getFont (columnIndex, false));
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		event.x = contentX;
		event.y = parent.getItemY (this);
		event.width = contentWidth;
		event.height = parent.itemHeight;
		event.detail = isSelected ? SWT.SELECTED : 0;
		parent.sendEvent (SWT.MeasureItem, event);
		event.gc = null;
		if (gc.isDisposed ()) return false;
		gc.setAlpha (oldAlpha);
		gc.setAntialias (oldAntialias);
		gc.setBackgroundPattern (oldBackgroundPattern);
		gc.setForegroundPattern (oldForegroundPattern);
		gc.setInterpolation (oldInterpolation);
		gc.setTextAntialias (oldTextAntialias);
		gc.setAdvanced (oldAdvanced);
		if (isDisposed ()) return false;
		if (parent.itemHeight != event.height) {
			parent.customHeightSet = true;
			boolean update = parent.setItemHeight (event.height + 2 * parent.getCellPadding ());
			if (update) parent.redraw ();
		}
		if (parent.columns.length == 0) {
			int change = event.width - (customWidth != -1 ? customWidth : contentWidth);
			if (event.width != contentWidth || customWidth != -1) customWidth = event.width;
			if (change != 0) {	/* scrollbar may be affected since no columns */
				parent.updateHorizontalBar (contentX + event.width, change);
				// TODO what if clip is too small now?
			}
		}
	}

	/* if this cell is completely to the right of the client area then there's no need to paint it */
	Rectangle clientArea = parent.clientArea;
	if (clientArea.x + clientArea.width < x) return false;

	Rectangle cellBounds = getCellBounds (columnIndex);
	if (parent.linesVisible) {
		cellBounds.y++;
		cellBounds.height--;
	}
	int cellRightX = 0;
	if (column != null) {
		cellRightX = column.getX () + column.width;
	} else {
		cellRightX = cellBounds.x + cellBounds.width;
	}

	/* restrict the clipping region to the cell */
	gc.setClipping (x, cellBounds.y, clientArea.width - x, cellBounds.height);
	
	int y = parent.getItemY (this);
	int itemHeight = parent.itemHeight;

	/* draw the parent background color/image of this cell */
	if (column == null) {
		parent.drawBackground (gc, 0, y, clientArea.width, itemHeight, 0, 0);
	} else {
		int fillWidth = cellBounds.width;
		if (parent.linesVisible) fillWidth--;
		parent.drawBackground (gc, cellBounds.x, cellBounds.y, fillWidth, cellBounds.height, 0, 0);
	}

	boolean isFocusItem = parent.focusItem == this && parent.isFocusControl ();
	boolean drawBackground = true;
	boolean drawForeground = true;
	boolean drawSelection = isSelected;
	boolean drawFocus = isFocusItem;
	if (parent.hooks (SWT.EraseItem)) {
		drawBackground = background != null || (cellBackgrounds != null && cellBackgrounds [columnIndex] != null);
		gc.setFont (getFont (columnIndex, false));
		if (isSelected && (columnIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
			gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
			gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		} else {
			gc.setForeground (getForeground (columnIndex));
			gc.setBackground (getBackground (columnIndex));
		}
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		event.doit = true;
		event.detail = SWT.FOREGROUND;
		if (drawBackground) event.detail |= SWT.BACKGROUND;
		if (isSelected) event.detail |= SWT.SELECTED;
		if (isFocusItem) event.detail |= SWT.FOCUSED;
		event.x = cellBounds.x;
		event.y = cellBounds.y;
		event.width = cellBounds.width;
		event.height = cellBounds.height;
		gc.setClipping (cellBounds);
		parent.sendEvent (SWT.EraseItem, event);
		event.gc = null;
		if (gc.isDisposed ()) return false;
		gc.setAlpha (oldAlpha);
		gc.setAntialias (oldAntialias);
		gc.setBackgroundPattern (oldBackgroundPattern);
		gc.setClipping (cellBounds);
		gc.setForegroundPattern (oldForegroundPattern);
		gc.setInterpolation (oldInterpolation);
		gc.setTextAntialias (oldTextAntialias);
		gc.setAdvanced (oldAdvanced);
		if (isDisposed ()) return false;
		if (!event.doit) {
			drawBackground = drawForeground = drawSelection = drawFocus = false;
		} else {
			drawBackground = drawBackground && (event.detail & SWT.BACKGROUND) != 0;
			drawForeground = (event.detail & SWT.FOREGROUND) != 0;
			drawSelection = isSelected && (event.detail & SWT.SELECTED) != 0;
			drawFocus = isFocusItem && (event.detail & SWT.FOCUSED) != 0;
		}
	}

	/* draw the cell's set background if appropriate */
	if (drawBackground) {
		gc.setBackground (getBackground (columnIndex));
		if (columnIndex == 0 && (column == null || column.getOrderIndex () == 0)) {
			Rectangle focusBounds = getFocusBounds ();
			int fillWidth = 0;
			if (column == null) {
				fillWidth = focusBounds.width;
			} else {
				fillWidth = column.width - focusBounds.x;
				if (parent.linesVisible) fillWidth--;
			}
			gc.fillRectangle (focusBounds.x, focusBounds.y, fillWidth, focusBounds.height);
		} else {
			int fillWidth = cellBounds.width;
			gc.fillRectangle (cellBounds.x, cellBounds.y, fillWidth, cellBounds.height);
		}
	}

	/* draw the selection bar if the receiver is selected */
	if (drawSelection && (columnIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
		if (parent.hasFocus () || (parent.style & SWT.HIDE_SELECTION) == 0) {
			gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
			if (columnIndex == 0) {
				Rectangle focusBounds = getFocusBounds ();
				int startX, fillWidth;
				if (column == null || column.getOrderIndex () == 0 || (parent.style & SWT.FULL_SELECTION) == 0) {
					startX = focusBounds.x + 1;		/* space for left bound of focus rect */
				} else {
					startX = column.getX ();
				}
				if (column == null) {
					fillWidth = focusBounds.width - 2;
				} else {
					fillWidth = column.getX () + column.width - startX;
					if (column.getOrderIndex () == parent.columns.length - 1 || (parent.style & SWT.FULL_SELECTION) == 0) {
						fillWidth -= 2;	/* space for right bound of focus rect */
					}
				}
				if (fillWidth > 0) {
					gc.fillRectangle (startX, focusBounds.y + 1, fillWidth, focusBounds.height - 2);
				}
			} else {
				int startX = column.getX ();
				int fillWidth = column.width;
				if (column.getOrderIndex () == 0) {
					startX += 1;	/* space for left bound of focus rect */
					fillWidth -= 1;
				}
				if (column.getOrderIndex () == parent.columns.length - 1) {
					fillWidth -= 2;		/* space for right bound of focus rect */
				}
				if (fillWidth > 0) {
					gc.fillRectangle (
						startX, 
						cellBounds.y + 1,
						fillWidth,
						cellBounds.height - 2);
				}
			}
		}
	}

	if (backgroundOnly) return false;

	/* Draw checkbox if drawing column 0 and parent has style SWT.CHECK */
	if (columnIndex == 0 && (parent.style & SWT.CHECK) != 0) {
		Image baseImage = grayed ? parent.getGrayUncheckedImage () : parent.getUncheckedImage ();
		Rectangle checkboxBounds = getCheckboxBounds ();
		gc.drawImage (baseImage, checkboxBounds.x, checkboxBounds.y);
		/* Draw checkmark if item is checked */
		if (checked) {
			Image checkmarkImage = parent.getCheckmarkImage ();
			Rectangle checkmarkBounds = checkmarkImage.getBounds ();
			int xInset = (checkboxBounds.width - checkmarkBounds.width) / 2;
			int yInset = (checkboxBounds.height - checkmarkBounds.height) / 2;
			gc.drawImage (checkmarkImage, checkboxBounds.x + xInset, checkboxBounds.y + yInset);
		}
	}

	if (drawForeground) {
		Image image = getImage (columnIndex, false);
		String text = getDisplayText (columnIndex);
		Rectangle imageArea = getImageBounds (columnIndex);
		int startX = imageArea.x;
		
		/* while painting the cell's content restrict the clipping region */
		int padding = parent.getCellPadding ();
		gc.setClipping (
			startX,
			cellBounds.y + padding - (parent.linesVisible ? 1 : 0),
			cellRightX - startX - padding,
			cellBounds.height - 2 * (padding - (parent.linesVisible ? 1 : 0)));
	
		/* draw the image */
		if (image != null) {
			Rectangle imageBounds = image.getBounds ();
			gc.drawImage (
				image,
				0, 0,									/* source x, y */
				imageBounds.width, imageBounds.height,	/* source width, height */
				imageArea.x, imageArea.y,				/* dest x, y */
				imageArea.width, imageArea.height);		/* dest width, height */
		}
	
		/* draw the text */
		if (text.length () > 0) {
			gc.setFont (getFont (columnIndex, false));
			int fontHeight = getFontHeight (columnIndex);
			if (drawSelection && (columnIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
				if (parent.hasFocus () || (parent.style & SWT.HIDE_SELECTION) == 0) {
					gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
				}
			} else {
				if (!isSelected || drawSelection) {
					gc.setForeground (getForeground (columnIndex));
				}
			}
			x = getTextX (columnIndex) + MARGIN_TEXT;
			gc.drawString (text, x, y + (itemHeight - fontHeight) / 2, true);
		}
	}

	if (parent.hooks (SWT.PaintItem)) {
		int contentWidth = getContentWidth (columnIndex);
		int contentX = getContentX (columnIndex);
		gc.setFont (getFont (columnIndex, false));
		if (isSelected && (columnIndex == 0 || (parent.style & SWT.FULL_SELECTION) != 0)) {
			gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
			gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		} else {
			gc.setForeground (getForeground (columnIndex));
			gc.setBackground (getBackground (columnIndex));
		}
		Event event = new Event ();
		event.item = this;
		event.gc = gc;
		event.index = columnIndex;
		if (isSelected) event.detail |= SWT.SELECTED;
		if (drawFocus) event.detail |= SWT.FOCUSED;
		event.x = contentX;
		event.y = cellBounds.y;
		event.width = contentWidth;
		event.height = cellBounds.height;
		gc.setClipping (cellBounds);
		parent.sendEvent (SWT.PaintItem, event);
		event.gc = null;
		if (gc.isDisposed ()) return false;
		gc.setAlpha (oldAlpha);
		gc.setAntialias (oldAntialias);
		gc.setBackgroundPattern (oldBackgroundPattern);
		gc.setClipping (cellBounds);
		gc.setForegroundPattern (oldForegroundPattern);
		gc.setInterpolation (oldInterpolation);
		gc.setTextAntialias (oldTextAntialias);
		gc.setAdvanced (oldAdvanced);
		drawFocus = isFocusItem && (event.detail & SWT.FOCUSED) != 0;
	}

	return isFocusItem && !drawFocus;
}
/*
 * Redraw part of the receiver.  If either EraseItem or PaintItem is hooked then
 * only full cells should be damaged, so adjust accordingly.  If neither of these
 * events are hooked then the exact bounds given for damaging can be used.
 */
void redraw (int x, int y, int width, int height, int columnIndex) {
	if (!parent.hooks (SWT.EraseItem) && !parent.hooks (SWT.PaintItem)) {
		parent.redraw (x, y, width, height, false);
		return;
	}
	Rectangle cellBounds = getCellBounds (columnIndex);
	parent.redraw (cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height, false);
}
void redrawItem () {
	parent.redraw (0, parent.getItemY (this), parent.clientArea.width, parent.itemHeight, false);
}
/*
 * Updates internal structures in the receiver and its child items to handle the removal of a column.
 */
void removeColumn (TableColumn column, int index) {
	int columnCount = parent.columns.length;

	if (columnCount == 0) {
		/* reverts to normal table when last column disposed */
		cellBackgrounds = cellForegrounds = null;
		displayTexts = null;
		cellFonts = null;
		fontHeights = null;
		GC gc = new GC (parent);
		computeTextWidths (gc);
		gc.dispose ();
		return;
	}

	String[] newTexts = new String [columnCount];
	System.arraycopy (texts, 0, newTexts, 0, index);
	System.arraycopy (texts, index + 1, newTexts, index, columnCount - index);
	texts = newTexts;
	
	Image[] newImages = new Image [columnCount];
	System.arraycopy (images, 0, newImages, 0, index);
	System.arraycopy (images, index + 1, newImages, index, columnCount - index);
	images = newImages;

	int[] newTextWidths = new int [columnCount];
	System.arraycopy (textWidths, 0, newTextWidths, 0, index);
	System.arraycopy (textWidths, index + 1, newTextWidths, index, columnCount - index);
	textWidths = newTextWidths;

	String[] newDisplayTexts = new String [columnCount];
	System.arraycopy (displayTexts, 0, newDisplayTexts, 0, index);
	System.arraycopy (displayTexts, index + 1, newDisplayTexts, index, columnCount - index);
	displayTexts = newDisplayTexts;

	if (cellBackgrounds != null) {
		Color[] newCellBackgrounds = new Color [columnCount];
		System.arraycopy (cellBackgrounds, 0, newCellBackgrounds, 0, index);
		System.arraycopy (cellBackgrounds, index + 1, newCellBackgrounds, index, columnCount - index);
		cellBackgrounds = newCellBackgrounds;
	}
	if (cellForegrounds != null) {
		Color[] newCellForegrounds = new Color [columnCount];
		System.arraycopy (cellForegrounds, 0, newCellForegrounds, 0, index);
		System.arraycopy (cellForegrounds, index + 1, newCellForegrounds, index, columnCount - index);
		cellForegrounds = newCellForegrounds;
	}
	if (cellFonts != null) {
		Font[] newCellFonts = new Font [columnCount];
		System.arraycopy (cellFonts, 0, newCellFonts, 0, index);
		System.arraycopy (cellFonts, index + 1, newCellFonts, index, columnCount - index);
		cellFonts = newCellFonts;

		int[] newFontHeights = new int [columnCount];
		System.arraycopy (fontHeights, 0, newFontHeights, 0, index);
		System.arraycopy (fontHeights, index + 1, newFontHeights, index, columnCount - index);
		fontHeights = newFontHeights;
	}

	if (index == 0) {
		text = texts [0] != null ? texts [0] : "";	//$NON-NLS-1$
		texts [0] = null;
		image = images [0];
		images [0] = null;
		/* 
		 * The new first column may not have as much width available to it as it did when it was
		 * the second column if checkboxes are being shown, so recompute its displayText if needed. 
		 */
		if ((parent.style & SWT.CHECK) != 0) {
			GC gc = new GC (parent);
			gc.setFont (getFont (0, false));
			computeDisplayText (0, gc);
			gc.dispose ();
		}
	}
	if (columnCount < 2) {
		texts = null;
		images = null;
	}
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
 */
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = background;
	if (oldColor == color) return;
	background = color;
	if (oldColor != null && oldColor.equals (color)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redrawItem ();
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
 */
public void setBackground (int columnIndex, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellBackgrounds == null) {
		if (color == null) return;
		cellBackgrounds = new Color [validColumnCount];
	}
	Color oldColor = cellBackgrounds [columnIndex];
	if (oldColor == color) return;
	cellBackgrounds [columnIndex] = color;
	if (oldColor != null && oldColor.equals (color)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	if (isInViewport ()) {
		Rectangle bounds = getCellBounds (columnIndex);
		parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
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
public void setChecked (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (checked == value) return;
	checked = value;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	if (isInViewport ()) {
		if (parent.hooks (SWT.EraseItem) || parent.hooks (SWT.PaintItem)) {
			redrawItem ();
		} else {
			Rectangle bounds = getCheckboxBounds ();
			parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
		}
	}
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
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals (font)) return;
	
	Rectangle bounds = getBounds (false);
	int oldRightX = bounds.x + bounds.width;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	/* recompute cached values for string measurements */
	GC gc = new GC (parent);
	gc.setFont (getFont (false));
	fontHeight = gc.getFontMetrics ().getHeight ();
	computeDisplayTexts (gc);
	computeTextWidths (gc);
	gc.dispose ();
	
	/* horizontal bar could be affected if table has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds (false);
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
	redrawItem ();
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
public void setFont (int columnIndex, Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}

	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellFonts == null) {
		if (font == null) return;
		cellFonts = new Font [validColumnCount];
	}
	Font oldFont = cellFonts [columnIndex];
	if (oldFont == font) return;
	cellFonts [columnIndex] = font;
	if (oldFont != null && oldFont.equals (font)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	/* recompute cached values for string measurements */
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex, false));
	if (fontHeights == null) fontHeights = new int [validColumnCount];
	fontHeights [columnIndex] = gc.getFontMetrics ().getHeight ();
	computeDisplayText (columnIndex, gc);
	gc.dispose ();

	if (isInViewport ()) {
		Rectangle bounds = getCellBounds (columnIndex);
		parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
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
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = foreground;
	if (oldColor == color) return;
	foreground = color;
	if (oldColor != null && oldColor.equals (color)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redrawItem ();
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
 */
public void setForeground (int columnIndex, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellForegrounds == null) {
		if (color == null) return;
		cellForegrounds = new Color [validColumnCount];
	}
	Color oldColor = cellForegrounds [columnIndex];
	if (oldColor == color) return;
	cellForegrounds [columnIndex] = color;
	if (oldColor != null && oldColor.equals (color)) return;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	if (isInViewport ()) {
		redraw (
			getTextX (columnIndex),
			parent.getItemY (this),
			textWidths [columnIndex] + 2 * MARGIN_TEXT,
			parent.itemHeight,
			columnIndex);
	}
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
public void setGrayed (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (grayed == value) return;
	grayed = value;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	if (isInViewport ()) {
		Rectangle bounds = getCheckboxBounds ();
		parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
	}
}
public void setImage (Image value) {
	checkWidget ();
	setImage (0, value);
}
/**
 * Sets the image for multiple columns in the table. 
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
public void setImage (Image[] value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	// TODO make a smarter implementation of this
	for (int i = 0; i < value.length; i++) {
		if (value [i] != null) setImage (i, value [i]);
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
public void setImage (int columnIndex, Image value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);

	TableColumn[] columns = parent.columns;
	int validColumnCount = Math.max (1, columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	Image image = getImage (columnIndex, false);
	if (value == image) return;
	if (value != null && value.equals (image)) return;
	if (columnIndex == 0) {
		super.setImage (value);
	} else {
		images [columnIndex] = value;
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	/* 
	 * An image width change may affect the space available for the item text, so
	 * recompute the displayText if there are columns.
	 */
	if (columns.length > 0) {
		GC gc = new GC (parent);
		gc.setFont (getFont (columnIndex, false));
		computeDisplayText (columnIndex, gc);
		gc.dispose ();
	}
	
	if (value == null) {
		redrawItem ();	// TODO why the whole item?
		return;
	}

	/*
	 * If this is the first image being put into the table then its item height
	 * may be adjusted, in which case a full redraw is needed.
	 */
	if (parent.imageHeight == 0) {
		int oldItemHeight = parent.itemHeight;
		parent.setImageHeight (value.getBounds ().height);
		if (oldItemHeight != parent.itemHeight) {
			if (columnIndex == 0) {
				parent.col0ImageWidth = value.getBounds ().width;
				if (columns.length > 0) {
					/* 
					 * All column 0 cells will now have less room available for their texts,
					 * so all items must now recompute their column 0 displayTexts.
					 */
					GC gc = new GC (parent);
					TableItem[] rootItems = parent.items;
					for (int i = 0; i < parent.itemsCount; i++) {
						rootItems [i].updateColumnWidth (columns [0], gc);
					}
					gc.dispose ();
				}
			}
			parent.redraw ();
			return;
		}
	}

	/* 
	 * If this is the first image being put into column 0 then all cells
	 * in the column should also indent accordingly. 
	 */
	if (columnIndex == 0 && parent.col0ImageWidth == 0) {
		parent.col0ImageWidth = value.getBounds ().width;
		/* redraw the column */
		if (columns.length == 0) {
			parent.redraw ();
		} else {
			/* 
			 * All column 0 cells will now have less room available for their texts,
			 * so all items must now recompute their column 0 displayTexts.
			 */
			GC gc = new GC (parent);
			TableItem[] rootItems = parent.items;
			for (int i = 0; i < parent.itemsCount; i++) {
				rootItems [i].updateColumnWidth (columns [0], gc);
			}
			gc.dispose ();
			parent.redraw (
				columns [0].getX (), 0,
				columns [0].width,
				parent.clientArea.height,
				false);
		}
		return;
	}
	redrawItem ();	// TODO why the whole item?
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
 * 
 * @deprecated this functionality is not supported on most platforms
 */
public void setImageIndent (int indent) {
	checkWidget();
	if (indent < 0) return;
	if (imageIndent == indent) return;
	imageIndent = indent;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
public void setText (int columnIndex, String value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (value.equals (getText (columnIndex, false))) return;
	if (columnIndex == 0) {
		super.setText (value);
	} else {
		texts [columnIndex] = value;		
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;

	int oldWidth = textWidths [columnIndex];
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex, false));
	computeDisplayText (columnIndex, gc);
	gc.dispose ();

	if (parent.columns.length == 0) {
		Rectangle bounds = getBounds (false);
		int rightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (rightX, textWidths [columnIndex] - oldWidth);
	}
	if (isInViewport ()) {
		redraw (
			getTextX (columnIndex),
			parent.getItemY (this),
			Math.max (oldWidth, textWidths [columnIndex]) + 2 * MARGIN_TEXT,
			parent.itemHeight,
			columnIndex);
	}
}
public void setText (String value) {
	checkWidget ();
	Rectangle bounds = getBounds (false);
	int oldRightX = bounds.x + bounds.width;
	setText (0, value);
	/* horizontal bar could be affected if table has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds (false);
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
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
public void setText (String[] value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	Rectangle bounds = getBounds (false);
	int oldRightX = bounds.x + bounds.width;
	// TODO make a smarter implementation of this
	for (int i = 0; i < value.length; i++) {
		if (value [i] != null) setText (i, value [i]);
	}
	/* horizontal bar could be affected if table has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds (false);
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
}
/*
 * Perform any internal changes necessary to reflect a changed column width.
 */
void updateColumnWidth (TableColumn column, GC gc) {
	int columnIndex = column.getIndex ();
	gc.setFont (getFont (columnIndex, false));
	String oldDisplayText = displayTexts [columnIndex];
	computeDisplayText (columnIndex, gc);

	/* the cell must be damaged if there is custom drawing being done or if the alignment is not LEFT */
	if (isInViewport ()) {
		boolean columnIsLeft = (column.style & SWT.LEFT) != 0;
		if (!columnIsLeft || parent.hooks (SWT.EraseItem) || parent.hooks (SWT.PaintItem)) {
			Rectangle cellBounds = getCellBounds (columnIndex);
			parent.redraw (cellBounds.x, cellBounds.y, cellBounds.width, cellBounds.height, false);
			return;
		}
		/* if the display text has changed then the cell text must be damaged in order to repaint */	
		if (oldDisplayText == null || !oldDisplayText.equals (displayTexts [columnIndex])) {
			Rectangle cellBounds = getCellBounds (columnIndex);
			int textX = getTextX (columnIndex);
			parent.redraw (textX, cellBounds.y, cellBounds.x + cellBounds.width - textX, cellBounds.height, false);
		}
	}
}
/*
 * The parent's font has changed, so if this font was being used by the receiver then
 * recompute its cached text sizes using the gc argument.
 */
void updateFont (GC gc) {
	if (font == null) {		/* receiver is using the Table's font */
		computeDisplayTexts (gc);
		computeTextWidths (gc);
	}
}
}

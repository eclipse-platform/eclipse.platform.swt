package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Compatibility;

public class TreeItem extends Item {
	Tree parent;
	TreeItem parentItem;
	TreeItem[] items = NO_ITEMS;
	int availableIndex = -1;	/* index in parent's flat list of available (though not necessarily within viewport) items */
	int depth = 0;				/* cached for performance, does not change after instantiation */
	boolean checked, grayed, expanded;

	String[] texts;
	int[] textWidths = new int [1];		/* cached string measurements */
	int fontHeight;						/* cached item font height */
	int[] fontHeights;
	Image[] images;
	Color foreground, background;
	String[] displayTexts;
	Color[] cellForegrounds, cellBackgrounds;
	Font font;
	Font[] cellFonts;
	
	static final int INDENT_HIERARCHY = 6;	/* the margin between an item's expander and its checkbox or content */
	static final int MARGIN_TEXT = 3;			/* the left and right margins within the text's space */
	static final TreeItem[] NO_ITEMS = new TreeItem [0];

public TreeItem (Tree parent, int style) {
	this (parent, style, checkNull (parent).items.length);
}
public TreeItem (Tree parent, int style, int index) {
	super (parent, style);
	int validItemIndex = parent.items.length;
	if (!(0 <= index && index <= validItemIndex)) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	parent.createItem (this, index);
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
public TreeItem (TreeItem parentItem, int style) {
	this (parentItem, style, checkNull (parentItem).items.length);
}
public TreeItem (TreeItem parentItem, int style, int index) {
	super (checkNull (parentItem).parent, style);
	this.parentItem = parentItem;
	parent = parentItem.parent;
	depth = parentItem.depth + 1;
	int validItemIndex = parentItem.items.length;
	if (!(0 <= index && index <= validItemIndex)) error (SWT.ERROR_INVALID_RANGE);
	parentItem.addItem (this, index);
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
/*
 * Updates internal structures in the receiver and its child items to handle the creation of a new column.
 */
void addColumn (TreeColumn column) {
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
			text = "";
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
		 * The new second column now has more space available to it than it did while it
		 * was the first column since it no longer has to show hierarchy decorations, so
		 * recompute its displayText.
		 */
		GC gc = new GC (parent);
		gc.setFont (getFont (1));
		computeDisplayText (1, gc);
		gc.dispose ();
	}
	
	/* notify all child items as well */
	for (int i = 0; i < items.length; i++) {
		items[i].addColumn (column);
	}
}
/*
 * Adds a child item to the receiver.
 */
void addItem (TreeItem item, int index) {
	TreeItem[] newChildren = new TreeItem [items.length + 1];
	System.arraycopy (items, 0, newChildren, 0, index);
	newChildren [index] = item;
	System.arraycopy (items, index, newChildren, index + 1, items.length - index);
	items = newChildren;

	if (!item.isAvailable ()) {
		/* receiver will now need an expander box if this is its first child */
		if (isAvailable () && items.length == 1) {
			Rectangle bounds = getExpanderBounds ();
			parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
		}
		return;
	}
	
	/* item should be available immediately so update parent */
	parent.makeAvailable (item);
	
	/* update scrollbars */
	Rectangle bounds = item.getBounds ();
	int rightX = bounds.x + bounds.width;
	parent.updateHorizontalBar (rightX, rightX);
	parent.updateVerticalBar ();
	/* 
	 * If new item is above viewport then adjust topIndex and the vertical scrollbar
	 * so that the current viewport items will not change. 
	 */
	if (item.availableIndex < parent.topIndex) {
		parent.topIndex++;
		parent.getVerticalBar ().setSelection (parent.topIndex);
		return;
	}
	
	parent.redrawFromItemDownwards (availableIndex);
}
static Tree checkNull (Tree tree) {
	if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return tree;
}
static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}
/*
 * Returns a collection of all tree items descending from the receiver, including
 * the receiver.  The order of the items in this collection are receiver, child0tree,
 * child1tree, ..., childNtree. 
 */
TreeItem[] computeAllDescendents () {
	int childCount = items.length;
	TreeItem[][] childResults = new TreeItem [childCount][];
	int count = 1;	/* receiver */
	for (int i = 0; i < childCount; i++) {
		childResults [i] = items [i].computeAllDescendents ();
		count += childResults [i].length;
	}
	TreeItem[] result = new TreeItem [count];
	int index = 0;
	result [index++] = this;
	for (int i = 0; i < childCount; i++) {
		System.arraycopy (childResults [i], 0, result, index, childResults [i].length);
		index += childResults [i].length;
	}
	return result;
}
/*
 * Returns the number of tree items descending from the receiver, including the
 * receiver, that are currently available.  It is assumed that the receiver is
 * currently available. 
 */
int computeAvailableDescendentCount () {
	int result = 1;		/* receiver */
	if (!expanded) return result;
	for (int i = 0; i < items.length; i++) {
		result += items [i].computeAvailableDescendentCount ();
	}
	return result;
}
/*
 * Returns a collection of the tree items descending from the receiver, including
 * the receiver, that are currently available.  It is assumed that the receiver is
 * currently available.  The order of the items in this collection are receiver,
 * child0tree, child1tree, ..., childNtree. 
 */
TreeItem[] computeAvailableDescendents () {
	if (!expanded) return new TreeItem[] {this};
	int childCount = items.length;
	TreeItem[][] childResults = new TreeItem [childCount][];
	int count = 1;	/* receiver */
	for (int i = 0; i < childCount; i++) {
		childResults [i] = items [i].computeAvailableDescendents ();
		count += childResults [i].length;
	}
	TreeItem[] result = new TreeItem [count];
	int index = 0;
	result [index++] = this;
	for (int i = 0; i < childCount; i++) {
		System.arraycopy (childResults [i], 0, result, index, childResults [i].length);
		index += childResults [i].length;
	}
	return result;
}
void computeDisplayText (int columnIndex, GC gc) {
	int columnCount = parent.columns.length;
	if (columnCount == 0) return;
	
	TreeColumn column = parent.columns [columnIndex];
	int availableWidth;
	if (columnIndex == 0) {
		/* column 0 is always LEFT and must consider hierarchy decorations */
		availableWidth = column.getX () + column.width - getTextX (columnIndex) - 2 * MARGIN_TEXT;
	} else {
		/* columns > 0 may not be LEFT so cannot use getTextX (int) */
		availableWidth = column.width - 2 * parent.getCellPadding () - 2 * MARGIN_TEXT;
		if (images [columnIndex] != null) {
			availableWidth -= image.getBounds ().width;
			availableWidth -= Tree.MARGIN_IMAGE;
		}
	}
	String text = getText (columnIndex);
	int textWidth = gc.textExtent (text).x;
	if (textWidth <= availableWidth) {
		displayTexts [columnIndex] = text;
		return;
	}
	
	/* Ellipsis will be needed, so subtract their width from the available text width */
	int ellipsisWidth = gc.textExtent (Tree.ELLIPSIS).x;
	availableWidth -= ellipsisWidth;
	if (availableWidth <= 0) {
		displayTexts [columnIndex] = Tree.ELLIPSIS;
		return;
	}
	
	/* Make initial guess. */
	int index = availableWidth / gc.getFontMetrics ().getAverageCharWidth ();
	textWidth = gc.textExtent (text.substring (0, index)).x;

	/* Initial guess is correct. */
	if (availableWidth == textWidth) {
		displayTexts [columnIndex] = text.substring (0, index) + Tree.ELLIPSIS;
		return;
	}

	/* Initial guess is too high, so reduce until fit is found. */
	if (availableWidth < textWidth) {
		do {
			index--;
			if (index < 0) {
				displayTexts [columnIndex] = Tree.ELLIPSIS;
				return;
			}
			text = text.substring (0, index);
			textWidth = gc.textExtent (text).x;
		} while (availableWidth < textWidth);
		displayTexts [columnIndex] = text + Tree.ELLIPSIS;
		return;
	}
	
	/* Initial guess is too low, so increase until overrun is found. */
	while (textWidth < availableWidth) {
		index++;
		textWidth = gc.textExtent (text.substring (0, index)).x;
	}
	displayTexts [columnIndex] = text.substring (0, index - 1) + Tree.ELLIPSIS;
}
void computeDisplayTexts (GC gc) {
	int columnCount = parent.columns.length;
	if (columnCount == 0) return;
	
	Font oldFont = gc.getFont ();
	for (int i = 0; i < columnCount; i++) {
		boolean fontChanged = false;
		Font font = getFont (i);
		if (!font.equals (oldFont)) {
			gc.setFont (font);
			fontChanged = true;
		}
		computeDisplayText (i, gc);
		if (fontChanged) gc.setFont (oldFont);
	}
}
public void dispose () {
	if (isDisposed ()) return;
	int startIndex = -1, endIndex = -1;
	Tree parent = this.parent;
	int index = getIndex ();
	
	/* determine the indices, if any, that will need to be visually updated */
	if (isAvailable ()) {
		if (isLastChild () && index > 0) {
			/* vertical connector lines no longer needed for this item */
			if (parentItem != null) {
				startIndex = parentItem.items [index - 1].availableIndex;
			} else {
				startIndex = parent.items [index - 1].availableIndex;
			}
		} else {
			startIndex = availableIndex;
		}
		endIndex = parent.availableItems.length - 1;
	}

	/* for performance do this upfront for whole descendent chain */
	TreeItem focusItem = parent.focusItem; 
	if (focusItem != null && focusItem.hasAncestor (this)) {
		parent.setFocusItem (this, false);
		parent.reassignFocus ();
		focusItem = parent.focusItem;
		if (focusItem != null) {
			parent.redrawItem (focusItem.availableIndex, true);
		}
	}
	if (parentItem != null) parentItem.removeItem (this, index);
	dispose (true);
	if (startIndex != -1) {
		parent.redrawItems (startIndex, endIndex, false);
	}
}
void dispose (boolean notifyParent) {
	if (isDisposed ()) return;
	if (notifyParent) parent.destroyItem (this);
	super.dispose ();	/* super is intentional here */
	for (int i = 0; i < items.length; i++) {
		items [i].dispose (notifyParent);
	}
	background = foreground = null;
	cellBackgrounds = cellForegrounds = null;
	font = null;
	cellFonts = null;
	images = null;
	texts = displayTexts = null;
	textWidths = fontHeights = null;
	parent = null;
	parentItem = null;
	items = null;
}
/*
 * Ensure that all ancestors of the receiver are expanded
 */
void expandAncestors () {
	if (parentItem != null) parentItem.expandAncestors ();
	setExpanded (true);
}
public Color getBackground () {
	checkWidget ();
	if (background != null) return background;
	return parent.getBackground ();
}
public Color getBackground (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getBackground ();
	if (cellBackgrounds == null || cellBackgrounds [columnIndex] == null) return getBackground ();
	return cellBackgrounds [columnIndex];
}
public Rectangle getBounds () {
	checkWidget ();
	if (!isAvailable()) return new Rectangle (0, 0, 0, 0);
	int textPaintWidth = textWidths [0] + 2 * MARGIN_TEXT;
	return new Rectangle (getTextX (0), parent.getItemY (this), textPaintWidth, parent.itemHeight - 1);
}
public Rectangle getBounds (int columnIndex) {
	checkWidget ();
	if (!isAvailable ()) return new Rectangle (0, 0, 0, 0);
	TreeColumn[] columns = parent.columns;
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
	
	TreeColumn column = columns [columnIndex];
	if (columnIndex == 0) {
		/* 
		 * For column 0 this is bounds from the beginning of the content to the
		 * end of the column.
		 */
		int x = getContentX (0);
		int offset = x - column.getX ();
		int width = Math.max (0, column.width - offset);		/* max is for columns with small widths */
		return new Rectangle (x, parent.getItemY (this), width, parent.itemHeight - 1);
	}
	/*
	 * For columns > 0 this is the bounds of the tree cell.
	 */
	return new Rectangle (column.getX (), parent.getItemY (this), column.width, parent.itemHeight - 1);
}
/*
 * Returns the full bounds of a cell in a tree, regardless of its content.
 */
Rectangle getCellBounds (int columnIndex) {
	int y = parent.getItemY (this);
	if (parent.columns.length == 0) {
		int textPaintWidth = textWidths [0] + 2 * MARGIN_TEXT;
		int width = getTextX (0) + textPaintWidth + parent.horizontalOffset;
		return new Rectangle (-parent.horizontalOffset, y, width, parent.itemHeight);
	}
	TreeColumn column = parent.columns [columnIndex];
	return new Rectangle (column.getX (), y, column.width, parent.itemHeight);
}
/*
 * Returns the bounds of the receiver's checkbox, or null if the parent's style does not
 * include SWT.CHECK.
 */
Rectangle getCheckboxBounds () {
	if ((parent.getStyle () & SWT.CHECK) == 0) return null;
	int itemHeight = parent.itemHeight;
	Rectangle result = parent.checkboxBounds;
	Point[] hLinePoints = getHconnectorEndpoints ();
	result.x = hLinePoints [1].x;
	result.y = parent.getItemY (this) + (itemHeight - result.height) / 2;
	return result;
}
public boolean getChecked () {
	checkWidget ();
	return checked;
}
int getContentWidth (int columnIndex) {
	int width = textWidths [columnIndex] + 2 * MARGIN_TEXT;
	Image image = getImage (columnIndex);
	if (image != null) {
		width += Tree.MARGIN_IMAGE + image.getBounds ().width;
	}
	return width;
}
String getDisplayText (int columnIndex) {
	if (parent.columns.length == 0) return getText (0);
	String result = displayTexts [columnIndex];
	return result != null ? result : "";
}
/*
 * Returns the x value where the receiver's content (ie.- its image or text) begins
 * for the specified column.  For columns > 0 this is dependent upon column alignment,
 * and for column 0 this is dependent upon the receiver's depth in the tree item
 * hierarchy and the presence/absence of a checkbox.
 */
int getContentX (int columnIndex) {
	if (columnIndex > 0) {
		TreeColumn column = parent.columns [columnIndex];
		int contentX = column.getX () + MARGIN_TEXT;
		if ((column.style & SWT.LEFT) != 0) return contentX;
		
		/* column is not left-aligned */
		int contentWidth = getContentWidth (columnIndex);
		if ((column.style & SWT.RIGHT) != 0) {
			int padding = parent.getCellPadding ();
			contentX = Math.max (contentX, column.getX () + column.width - padding - contentWidth);	
		} else {	/* SWT.CENTER */
			contentX = Math.max (contentX, column.getX () + (column.width - contentWidth) / 2);
		}
		return contentX;
	}

	/* column 0 (always left-aligned) */
	if ((parent.style & SWT.CHECK) != 0) {
		Rectangle checkBounds = getCheckboxBounds ();
		return checkBounds.x + checkBounds.width + Tree.MARGIN_IMAGE;
	}
	
	int contentX = parent.getCellPadding () - parent.horizontalOffset;
	if (parentItem != null) {
		int expanderWidth = parent.expanderBounds.width + INDENT_HIERARCHY;
		contentX += expanderWidth * depth;
	}
	contentX += parent.expanderBounds.width;
	return contentX + Tree.MARGIN_IMAGE + INDENT_HIERARCHY;
}
public boolean getExpanded () {
	checkWidget ();
	return expanded;
}
/*
 * Returns the bounds of the receiver's expander box, regardless of whether the
 * receiver currently has children or not.
 */ 
Rectangle getExpanderBounds () {
	int itemHeight = parent.itemHeight;
	int x = parent.getCellPadding () - parent.horizontalOffset;
	int y = parent.getItemY (this);
	if (parentItem != null) {
		int expanderWidth = parent.expanderBounds.width + INDENT_HIERARCHY;
		x += expanderWidth * depth;
	}
	return new Rectangle (
		x, y + (itemHeight - parent.expanderBounds.height) / 2,
		parent.expanderBounds.width, parent.expanderBounds.height);
}
/*
 * Returns the bounds that should be used for drawing a focus rectangle on the receiver
 */
Rectangle getFocusBounds () {
	int x = getFocusX ();
	int width;
	TreeColumn[] columns = parent.columns;
	if (columns.length == 0) {
		width = textWidths [0] + 2 * MARGIN_TEXT;
	} else {
		width = columns [0].width - parent.horizontalOffset - x - 2;
	}
	return new Rectangle (x, parent.getItemY (this) + 1, width, parent.itemHeight - 1);
}
/*
 * Returns the x value of the receiver's focus rectangle.
 */
int getFocusX () {
	int result = getContentX (0);
	int imageSpace = parent.col0ImageWidth;
	if (imageSpace > 0) {
		result += imageSpace + Tree.MARGIN_IMAGE;
	}
	return result;
}
public Font getFont () {
	checkWidget ();
	if (font != null) return font;
	return parent.getFont ();
}
public Font getFont (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getFont ();
	if (cellFonts == null || cellFonts [columnIndex] == null) return getFont ();
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
public Color getForeground () {
	checkWidget ();
	if (foreground != null) return foreground;
	return parent.getForeground ();
}
public Color getForeground (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getForeground ();
	if (cellForegrounds == null || cellForegrounds [columnIndex] == null) return getForeground ();
	return cellForegrounds [columnIndex];
}
public boolean getGrayed () {
	checkWidget ();
	return grayed;
}
/*
 * Answers the start and end points of the horizontal connector line that is
 * drawn between an item's expander box and its checkbox or content.
 */
Point[] getHconnectorEndpoints () {
	Rectangle expanderBounds = getExpanderBounds ();
	int x, width;
	if (items.length == 0) {	/* no child items, so no expander box */
		x = expanderBounds.x + Compatibility.ceil (expanderBounds.width, 2);
		width = Compatibility.floor (expanderBounds.width, 2) + INDENT_HIERARCHY;
	} else {					/* has child items */
		x = expanderBounds.x + expanderBounds.width;
		width = INDENT_HIERARCHY;
	}
	int y = expanderBounds.y + expanderBounds.height / 2;
	return new Point[] {
		new Point (x, y),
		new Point (x + width, y)
	};
}
/*
 * Returns the bounds representing the clickable region that should select the receiver.
 */
Rectangle getHitBounds () {
	int contentX = getContentX (0);
	int width = 0;
	TreeColumn[] columns = parent.columns;
	if (columns.length == 0) {
		/* 
		 * If there are no columns then this spans from the beginning of the receiver's
		 * image or text to the end of its text.
		 */
		int textPaintWidth = textWidths [0] + 2 * MARGIN_TEXT;
		width = getFocusX () + textPaintWidth - contentX; 
	} else {
		/* 
		 * If there are columns then this spans from the beginning of the receiver's column 0
		 * image or text to the end of column 0.
		 */
		width = columns [0].width - contentX - parent.horizontalOffset;
	}
	return new Rectangle (contentX, parent.getItemY (this), width, parent.itemHeight);
}
public Image getImage () {
	checkWidget ();
	return super.getImage ();
}
public Image getImage (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return null;
	if (columnIndex == 0) return getImage ();
	return images [columnIndex];
}
public Rectangle getImageBounds (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return new Rectangle (0,0,0,0);

	int padding = parent.getCellPadding ();
	int startX = getContentX (columnIndex);
	int itemHeight = parent.itemHeight;
	int imageSpaceY = itemHeight - 2 * padding;
	int y = parent.getItemY (this);
	Image image = getImage (columnIndex); 
	if (image == null) {
		return new Rectangle (startX, y + padding, 0, imageSpaceY);
	}
	
	Rectangle imageBounds = image.getBounds ();
	/* 
	 * For column 0 all images have the same width, which may be larger or smaller
	 * than the image to be drawn here.  Therefore the image bounds to draw must be
	 * specified.
	 */
	int drawWidth;
	if (columnIndex == 0) {
		int imageSpaceX = parent.col0ImageWidth;
		drawWidth = Math.min (imageSpaceX, imageBounds.width);
	} else {
		drawWidth = imageBounds.width;
	}
	int drawHeight = Math.min (imageSpaceY, imageBounds.height);
	return new Rectangle (
		startX, y + (itemHeight - drawHeight) / 2,
		drawWidth, drawHeight);
}
int getIndex () {
	TreeItem[] items;
	if (parentItem != null) {
		items = parentItem.items;
	} else {
		items = parent.items;
	}
	for (int i = 0; i < items.length; i++) {
		if (items [i] == this) return i;
	}
	return -1;
}
public int getItemCount () {
	checkWidget ();
	return items.length;
}
public TreeItem [] getItems () {
	checkWidget ();
	TreeItem[] result = new TreeItem [items.length];
	System.arraycopy (items, 0, result, 0, items.length);
	return result;
}
public Tree getParent () {
	checkWidget ();
	return parent;
}
public TreeItem getParentItem () {
	checkWidget ();
	return parentItem;
}
/*
 * Returns the receiver's ideal width for the specified columnIndex.
 */
int getPreferredWidth (int columnIndex) {
	int textPaintWidth = textWidths [columnIndex] + 2 * MARGIN_TEXT;
	int result = getTextX (columnIndex) + textPaintWidth;
	result -= parent.columns [columnIndex].getX ();
	return result;
}
public String getText () {
	checkWidget ();
	return super.getText ();
}
public String getText (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return "";
	if (columnIndex == 0) return getText ();
	if (texts [columnIndex] == null) return "";
	return texts [columnIndex];
}
/*
 * Returns the x value where the receiver's text begins.
 */
int getTextX (int columnIndex) {
	if (columnIndex > 0) {
		int textX = getContentX (columnIndex);
		Image image = images [columnIndex];
		if (image != null) {
			textX += Tree.MARGIN_IMAGE + image.getBounds ().width;
		}
		return textX;
	}
	/* column 0 */
	return getFocusX () + MARGIN_TEXT;
}
/*
 * Returns true if the receiver descends from (or is identical to) the item.
 */
boolean hasAncestor (TreeItem item) {
	if (this == item) return true;
	if (parentItem == null) return false;
	return parentItem.hasAncestor (item);
}
/*
 * Returns true if the receiver is currently available (though not necessary in the viewport).
 */
boolean isAvailable () {
	if (parentItem == null) return true; 	/* root items are always available */
	if (!parentItem.expanded) return false;
	return parentItem.isAvailable ();
}
/*
 * Returns true if the receiver is the last child of its parent item, or of its parent
 * if the receiver is a root item, and false otherwise.
 */
boolean isLastChild () {
	if (parentItem != null) {
		return getIndex () == parentItem.items.length - 1;
	}
	return getIndex () == parent.items.length - 1;
}
boolean isSelected () {
	return parent.getSelectionIndex (this) != -1;
}
/*
 * The paintCellContent argument indicates whether the item should paint
 * its cell contents (ie.- its text, image, check and hierarchy) in addition
 * to its item-level attributes (ie.- background color and selection).
 */
void paint (GC gc, TreeColumn column, boolean paintCellContent) {
	int columnIndex = 0, x = 0;
	if (column != null) {
		columnIndex = column.getIndex ();
		x = column.getX ();
	}
	/* if this cell is completely to the right of the client area then there's no need to paint it */
	Rectangle clientArea = parent.getClientArea ();
	if (clientArea.x + clientArea.width < x) return;

	Rectangle cellBounds = getCellBounds (columnIndex);
	int cellRightX = 0;
	if (column != null) {
		cellRightX = column.getX () + column.width - 1;
	} else {
		cellRightX = cellBounds.x + cellBounds.width - 1;
	}

	/* if this cell is completely to the left of the client area then there's no need to paint it */
	if (cellRightX < 0) return;

	/* restrict the clipping region to the cell */
	gc.setClipping (x, cellBounds.y, cellRightX - x, cellBounds.height);
	
	int y = parent.getItemY (this);
	int padding = parent.getCellPadding ();
	int itemHeight = parent.itemHeight;

	/* draw the background color if this item has a custom color set */
	Color background = getBackground (columnIndex);
	if (!background.equals (parent.getBackground ())) {
		Color oldBackground = gc.getBackground ();
		gc.setBackground (background);
		if (columnIndex == 0) {
			int focusX = getFocusX ();
			gc.fillRectangle (focusX, y + 1, cellRightX - focusX, itemHeight - 1);
		} else {
			gc.fillRectangle (cellBounds.x, cellBounds.y + 1, cellBounds.width - 1, cellBounds.height - 1);
		}
		gc.setBackground (oldBackground);
	}

	/* draw the selection bar if the receiver is selected */
	if (isSelected () && columnIndex == 0) {
		Color oldBackground = gc.getBackground ();
		gc.setBackground (parent.selectionBackgroundColor);
		Rectangle focusBounds = getFocusBounds ();
		gc.fillRectangle (focusBounds.x + 1, focusBounds.y + 1, focusBounds.width - 2, focusBounds.height - 2);
		gc.setBackground (oldBackground);
	}
		
	if (!paintCellContent) return;

	/* Draw column 0 decorations */
	if (columnIndex == 0) {
		/* Draw hierarchy connector lines */
		Rectangle expanderBounds = getExpanderBounds ();
		Color oldForeground = gc.getForeground ();
		gc.setForeground (parent.connectorLineColor);

		/* Draw vertical line above expander */
		int lineX = expanderBounds.x + expanderBounds.width / 2;
		int y2 = expanderBounds.y;
		if (items.length == 0) {
			y2 += expanderBounds.height / 2;
		}
		/* Do not draw this line iff this is the very first item in the tree */ 
		if (parentItem != null || getIndex () != 0) {
			gc.drawLine (lineX, y, lineX, y2);
		}

		/* Draw vertical line below expander if the receiver has lower siblings */
		if (!isLastChild ()) {
			if (items.length != 0) y2 += expanderBounds.height;
			gc.drawLine (lineX, y2, lineX, y + itemHeight);
		}

		/* Draw horizontal line to right of expander */
		Point[] endpoints = getHconnectorEndpoints ();
		gc.drawLine (endpoints [0].x, endpoints [0].y, endpoints [1].x - Tree.MARGIN_IMAGE, endpoints [1].y);
		
		/* 
		 * Draw hierarchy lines that are needed by other items that are shown below
		 * this item but whose parents are shown above (ie.- lines to the left of
		 * this item's connector line).
		 */
		TreeItem item = parentItem;
		while (item != null) {
			if (!item.isLastChild ()) {
				Rectangle itemExpanderBounds = item.getExpanderBounds ();
				lineX = itemExpanderBounds.x + itemExpanderBounds.width / 2;
				gc.drawLine (lineX, y, lineX, y + itemHeight);
			}
			item = item.parentItem;
		}
		gc.setForeground (oldForeground);
		
		/* Draw expand/collapse image if receiver has children */
		if (items.length > 0) {
			Image image = expanded ? parent.getExpandedImage () : parent.getCollapsedImage ();
			gc.drawImage (image, expanderBounds.x, expanderBounds.y);
		}
		
		/* Draw checkbox if parent Tree has style SWT.CHECK */
		if ((parent.style & SWT.CHECK) != 0) {
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
	}

	Image image = getImage (columnIndex);
	String text = getDisplayText (columnIndex);
	Rectangle imageArea = getImageBounds (columnIndex);
	int startX = imageArea.x;
	
	/* while painting the cell's content restrict the clipping region */
	gc.setClipping (
		startX,
		cellBounds.y + padding,
		cellRightX - startX - padding,
		cellBounds.height - (2 * padding));

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
		boolean fontChanged = false, foregroundChanged = false;
		Font oldFont = gc.getFont ();
		Font font = getFont (columnIndex);
		if (!font.equals (oldFont)) {
			gc.setFont (font);
			fontChanged = true;
		}
		int fontHeight = getFontHeight (columnIndex);
		Color oldForeground = gc.getForeground ();
		if (isSelected () && columnIndex == 0) {
			gc.setForeground (parent.selectionForegroundColor);
			foregroundChanged = true;
		} else {
			Color foreground = getForeground (columnIndex);
			if (!foreground.equals (oldForeground)) {
				gc.setForeground (foreground);
				foregroundChanged = true;
			}
		}
		gc.drawString (text, getTextX (columnIndex), y + (itemHeight - fontHeight) / 2, true);
		if (foregroundChanged) gc.setForeground (oldForeground);
		if (fontChanged) gc.setFont (oldFont);
	}
}
/*
 * Recomputes the cached text widths.
 */
void recomputeTextWidths (GC gc) {
	int validColumnCount = Math.max (1, parent.columns.length);
	textWidths = new int [validColumnCount];
	Font oldFont = gc.getFont ();
	for (int i = 0; i < textWidths.length; i++) {
		String value = getDisplayText (i);
		if (value != null) {
			boolean fontChanged = false;
			Font font = getFont (i);
			if (!font.equals (oldFont)) {
				gc.setFont (font);
				fontChanged = true;
			}
			textWidths [i] = gc.textExtent (value).x;
			if (fontChanged) gc.setFont (oldFont);
		}
	}
}
void redrawItem () {
	if (!isAvailable ()) return;
	parent.redraw (0, parent.getItemY (this), parent.getClientArea ().width, parent.itemHeight, false);
}
/*
 * Updates internal structures in the receiver and its child items to handle the removal of a column.
 */
void removeColumn (TreeColumn column, int index) {
	int columnCount = parent.columns.length;

	if (columnCount == 0) {
		/* reverts to normal tree when last column disposed */
		cellBackgrounds = cellForegrounds = null;
		displayTexts = null;
		cellFonts = null;
		fontHeights = null;
		GC gc = new GC (parent);
		gc.setFont (getFont ());
		recomputeTextWidths (gc);
		gc.dispose ();
		/* notify all child items as well */
		for (int i = 0; i < items.length; i++) {
			items [i].removeColumn (column, index);
		}
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
		text = texts [0] != null ? texts [0] : "";
		texts [0] = null;
		image = images [0];
		images [0] = null;
		/* 
		 * The new first column will not have as much width available to it as it did when it was
		 * the second column since it now has to show hierarchy decorations as well, so recompute
		 * its displayText. 
		 */
		GC gc = new GC (parent);
		gc.setFont (getFont (0));
		computeDisplayText (0, gc);
		gc.dispose ();
	}
	if (columnCount < 2) {
		texts = null;
		images = null;
	}

	/* notify all child items as well */
	for (int i = 0; i < items.length; i++) {
		items [i].removeColumn (column, index);
	}
}
/*
 * Removes a child item from the receiver.
 */
void removeItem (TreeItem item, int index) {
	if (isDisposed ()) return;
	if (items.length == 0) {
		items = NO_ITEMS;
		/* condition below handles creation of item within Expand callback */
		if (!parent.inExpand) {
			expanded = false;
			if (availableIndex == -1) return;
			Rectangle bounds = getExpanderBounds ();	/* expander box no longer needed */
			parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
		}
		return;
	}
	TreeItem[] newItems = new TreeItem [items.length - 1];
	System.arraycopy (items, 0, newItems, 0, index);
	System.arraycopy (items, index + 1, newItems, index, newItems.length - index);
	items = newItems;
}
public void setBackground (Color value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (background == value) return;
	if (background != null && background.equals (value)) return;
	background = value;
	redrawItem ();
}
public void setBackground (int columnIndex, Color value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellBackgrounds == null) {
		cellBackgrounds = new Color [validColumnCount];
	}
	if (cellBackgrounds [columnIndex] == value) return;
	if (cellBackgrounds [columnIndex] != null && cellBackgrounds [columnIndex].equals (value)) return;
	cellBackgrounds [columnIndex] = value;
	if (availableIndex == -1) return;
	Rectangle bounds = getCellBounds (columnIndex);
	parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
}
public void setChecked (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (checked == value) return;
	checked = value;
	if (availableIndex == -1) return;
	Rectangle bounds = getCheckboxBounds ();
	parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
}
public void setExpanded (boolean value) {
	checkWidget ();
	if (expanded == value) return;
	if (items.length == 0) return;
	if (parent.inExpand) return;
	if (value) {
		expanded = value;
		if (availableIndex == -1) return;

		TreeItem[] availableDescendents = computeAvailableDescendents ();
		int descendentsCount = availableDescendents.length;
		if (availableIndex != parent.availableItems.length - 1) {
			/* the receiver is not the last available item */
			Rectangle clientArea = parent.getClientArea ();
			int y = parent.getItemY (this) + parent.itemHeight;
			if (0 < y && y < clientArea.height) {
				parent.update ();
				GC gc = new GC (parent);
				gc.copyArea (
					0, y,
					clientArea.width, clientArea.height - y,
					0, y + ((descendentsCount - 1) * parent.itemHeight));				
				gc.dispose ();
			}
		}

		parent.makeDescendentsAvailable (this, availableDescendents);

		/* update scrollbars */
		int rightX = 0;
		for (int i = 1; i < availableDescendents.length; i++) {
			Rectangle bounds = availableDescendents [i].getBounds ();
			rightX = Math.max (rightX, bounds.x + bounds.width);
		}
		parent.updateHorizontalBar (rightX, rightX);
		parent.updateVerticalBar ();
		/* 
		 * If new item is above viewport then adjust topIndex and the vertical scrollbar
		 * so that the current viewport items will not change. 
		 */
		if (availableIndex < parent.topIndex) {
			parent.topIndex += descendentsCount - 1;
			parent.getVerticalBar ().setSelection (parent.topIndex);
			return;
		}

		int redrawStart = availableIndex + 1;
		int redrawEnd = redrawStart + descendentsCount - 2;
		parent.redrawItems (redrawStart, redrawEnd, false);
	} else {
		TreeItem[] descendents = computeAvailableDescendents ();
		expanded = value;
		if (availableIndex == -1) return;
		Rectangle clientArea = parent.getClientArea ();

		int y = parent.getItemY (this) + parent.itemHeight;
		int startY = y + (descendents.length - 1) * parent.itemHeight;
		parent.update ();
		GC gc = new GC (parent);
		gc.copyArea (0, startY, clientArea.width, clientArea.height - startY, 0, y);
		gc.dispose ();
		int redrawY = clientArea.height - startY + y;
		parent.redraw (0, redrawY, clientArea.width, clientArea.height - redrawY, false);

		parent.makeDescendentsUnavailable (this, descendents);

		parent.updateHorizontalBar ();
		int oldTopIndex = parent.topIndex;
		parent.updateVerticalBar ();

		/* move focus (and selection if SWT.SINGLE) to item if a descendent had focus */
		TreeItem focusItem = parent.focusItem;
		if (focusItem != null && focusItem != this && focusItem.hasAncestor (this)) {
			parent.setFocusItem (this, false);
			if ((parent.style & SWT.SINGLE) != 0) {
				parent.selectItem (this, false);
			}
			/* Fire an event since the selection is being changed automatically */
			Event newEvent = new Event ();
			newEvent.item = this;
			parent.sendEvent (SWT.Selection, newEvent);
			if (isDisposed ()) return;
			parent.showItem (this);
			parent.redrawItem (availableIndex, true);
		} else {
			/* 
			 * If all collapsed items are above the viewport then adjust topIndex and
			 * the vertical scrollbar so that the current viewport items will not change.
			 */
			int bottomIndex = availableIndex + descendents.length - 1;
			if (bottomIndex < parent.topIndex) {
				parent.topIndex = oldTopIndex - descendents.length + 1;
				parent.getVerticalBar ().setSelection (parent.topIndex);
				return;
			}
		}
	}
	/* redraw the receiver's expander box */
	Rectangle bounds = getExpanderBounds ();
	parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
}
public void setFont (Font value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (font == value) return;
	if (value != null && value.equals (font)) return;
	
	Rectangle bounds = getBounds ();
	int oldRightX = bounds.x + bounds.width;
	font = value;
	
	/* recompute cached values for string measurements */
	GC gc = new GC (parent);
	gc.setFont (getFont ());
	computeDisplayTexts (gc);
	recomputeTextWidths (gc);
	fontHeight = gc.getFontMetrics ().getHeight ();
	gc.dispose ();
	
	/* horizontal bar could be affected if tree has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds ();
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
	redrawItem ();
}
public void setFont (int columnIndex, Font value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}

	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellFonts == null) cellFonts = new Font [validColumnCount];
	if (cellFonts [columnIndex] == value) return;
	if (cellFonts [columnIndex] != null && cellFonts [columnIndex].equals (value)) return;
	cellFonts [columnIndex] = value;
	
	/* recompute cached values for string measurements */
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex));
	if (fontHeights == null) fontHeights = new int [validColumnCount];
	fontHeights [columnIndex] = gc.getFontMetrics ().getHeight ();
	computeDisplayText (columnIndex, gc);
	textWidths [columnIndex] = gc.textExtent (getDisplayText (columnIndex)).x;
	gc.dispose ();

	if (availableIndex == -1) return;
	Rectangle bounds = getCellBounds (columnIndex);
	parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
}
public void setForeground (Color value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (foreground == value) return;
	if (foreground != null && foreground.equals (value)) return;
	foreground = value;
	redrawItem ();
}
public void setForeground (int columnIndex, Color value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellForegrounds == null) {
		cellForegrounds = new Color [validColumnCount];
	}
	if (cellForegrounds [columnIndex] == value) return;
	if (cellForegrounds [columnIndex] != null && cellForegrounds [columnIndex].equals (value)) return;
	cellForegrounds [columnIndex] = value;
	if (availableIndex == -1) return;
	parent.redraw (
		getTextX (columnIndex),
		parent.getItemY (this),
		textWidths [columnIndex] + 2 * MARGIN_TEXT,
		parent.itemHeight,
		false);
}
public void setGrayed (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (grayed == value) return;
	grayed = value;
	if (availableIndex == -1) return;
	Rectangle bounds = getCheckboxBounds ();
	parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
}
public void setImage (Image value) {
	checkWidget ();
	setImage (0, value);
}
public void setImage (Image[] value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	// TODO make a smarter implementation of this
	for (int i = 0; i < value.length; i++) {
		if (value [i] != null) setImage (i, value [i]);
	}
}
public void setImage (int columnIndex, Image value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);

	TreeColumn[] columns = parent.columns;
	int validColumnCount = Math.max (1, columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	Image image = getImage (columnIndex);
	if (value == image) return;
	if (value != null && value.equals (image)) return;
	if (columnIndex == 0) {
		super.setImage (value);
	} else {
		images [columnIndex] = value;
	}
	
	/* 
	 * An image width change may affect the space available for the item text, so
	 * recompute the displayText if there are columns.
	 */
	if (columns.length > 0) {
		GC gc = new GC (parent);
		gc.setFont (getFont (columnIndex));
		computeDisplayText (columnIndex, gc);
		textWidths [columnIndex] = gc.textExtent (getDisplayText (columnIndex)).x;
		gc.dispose ();
	}
	
	if (value == null) {
		redrawItem ();
		return;
	}

	/*
	 * If this is the first image being put into the tree then its item height
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
					TreeItem[] rootItems = parent.items;
					for (int i = 0; i < rootItems.length; i++) {
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
			TreeItem[] rootItems = parent.items;
			for (int i = 0; i < rootItems.length; i++) {
				rootItems [i].updateColumnWidth (columns [0], gc);
			}
			gc.dispose ();
			parent.redraw (
				0, 0,
				columns [0].width,
				parent.getClientArea ().height,
				true);
		}
		return;
	}
	redrawItem ();
}
public void setText (String value) {
	checkWidget ();
	Rectangle bounds = getBounds ();
	int oldRightX = bounds.x + bounds.width;
	setText (0, value);
	/* horizontal bar could be affected if tree has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds ();
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
}
public void setText (String[] value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	Rectangle bounds = getBounds ();
	int oldRightX = bounds.x + bounds.width;
	// TODO make a smarter implementation of this
	for (int i = 0; i < value.length; i++) {
		if (value [i] != null) setText (i, value [i]);
	}
	/* horizontal bar could be affected if tree has no columns */
	if (parent.columns.length == 0) {
		bounds = getBounds ();
		int newRightX = bounds.x + bounds.width;
		parent.updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
}
public void setText (int columnIndex, String value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	int validColumnCount = Math.max (1, parent.columns.length);
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (value.equals (getText (columnIndex))) return;
	if (columnIndex == 0) {
		super.setText (value);
	} else {
		texts [columnIndex] = value;		
	}
	
	int oldWidth = textWidths [columnIndex];
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex));
	computeDisplayText (columnIndex, gc);
	textWidths [columnIndex] = gc.textExtent (getDisplayText (columnIndex)).x;
	gc.dispose ();
	if (availableIndex == -1) return;
	parent.redraw (
		getTextX (columnIndex),
		parent.getItemY (this),
		Math.max (oldWidth, textWidths [columnIndex]) + 2 * MARGIN_TEXT,
		parent.itemHeight,
		false);
}
void updateColumnWidth (TreeColumn column, GC gc) {
	int columnIndex = column.getIndex ();
	boolean fontChanged = false;
	Font oldFont = gc.getFont ();
	Font font = getFont (columnIndex);
	if (!font.equals(oldFont)) {
		gc.setFont (font);
		fontChanged = true;
	}
	computeDisplayText (columnIndex, gc);
	textWidths [columnIndex] = gc.textExtent (getDisplayText (columnIndex)).x;
	if (fontChanged) gc.setFont (oldFont);
	for (int i = 0; i < items.length; i++) {
		items [i].updateColumnWidth (column, gc);
	}
}
/*
 * The parent's font has changed, so if this font was being used by the receiver then
 * recompute its cached text sizes using the gc argument.  Pass this notification on to
 * all child items as well.
 */
void updateFont (GC gc) {
	if (font == null) {		/* receiver is using the Tree's font */
		computeDisplayTexts (gc);
		recomputeTextWidths (gc);
	}
	/* pass notification on to all children */
	for (int i = 0; i < items.length; i++) {
		items [i].updateFont (gc);
	}
}
}

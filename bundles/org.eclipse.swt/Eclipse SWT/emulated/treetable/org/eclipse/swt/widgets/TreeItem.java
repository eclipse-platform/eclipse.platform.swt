package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Compatibility;

public class TreeItem extends Item {
	Tree parent;
	TreeItem parentItem;
	TreeItem[] items = new TreeItem [0];
	/* index in parent's flat list of available (though not necessarily visible) items */
	int availableIndex = -1;
	boolean checked, grayed, expanded;

	String[] texts = new String [1];
	int[] textWidths = new int [1];		/* cached string measurements */
	int fontHeight;						/* cached item font height */
	int[] fontHeights;
	Image[] images = new Image [1];
	Color foreground, background;
	Color[] cellForegrounds, cellBackgrounds;
	Font font;
	Font[] cellFonts;

	// TODO these cannot be static
	static Color LinesColor, SelectionBackgroundColor, SelectionForegroundColor;
	static Image ExpandedImage, CollapsedImage;
	static Image UncheckedImage, GrayUncheckedImage, CheckmarkImage;	

	static final int INDENT_HIERARCHY = 6;
	static final int MARGIN_TEXT = 3;			/* the left and right margins within the text's space */
	static final ImageData IMAGEDATA_CHECKMARK;
	static final ImageData IMAGEDATA_GRAY_UNCHECKED;
	static final ImageData IMAGEDATA_UNCHECKED;
	static final ImageData IMAGEDATA_COLLAPSED;
	static final ImageData IMAGEDATA_EXPANDED;
	
	static {
		PaletteData fourBit = new PaletteData (new RGB[] {
			new RGB (0, 0, 0), new RGB (128, 0, 0), new RGB (0, 128, 0), new RGB (128, 128, 0),
			new RGB (0, 0, 128), new RGB (128, 0, 128), new RGB (0, 128, 128), new RGB (128, 128, 128),
			new RGB (192, 192, 192), new RGB (255, 0, 0), new RGB (0, 255, 0), new RGB (255, 255, 0),
			new RGB (0, 0, 255), new RGB (255, 0, 255), new RGB (0, 255, 255), new RGB (255, 255, 255)});	
		IMAGEDATA_COLLAPSED = new ImageData (
			9, 9, 4, 										/* width, height, depth */
			fourBit, 4,
			new byte[] {
				119, 119, 119, 119, 112, 0, 0, 0, 127, -1, -1, -1,
				112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0,
				127, -1, 15, -1, 112, 0, 0, 0, 127, 0, 0, 15,
				112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0,
				127, -1, 15, -1, 112, 0, 0, 0, 127, -1, -1, -1,
				112, 0, 0, 0, 119, 119, 119, 119, 112, 0, 0, 0});
		IMAGEDATA_COLLAPSED.transparentPixel = 15;			/* white for transparency */
		IMAGEDATA_EXPANDED = new ImageData (
			9, 9, 4, 										/* width, height, depth */
			fourBit, 4,
			new byte[] {
				119, 119, 119, 119, 112, 0, 0, 0, 127, -1, -1, -1,
				112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0, 127, 0, 0, 15,
				112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0, 127, -1, -1, -1,
				112, 0, 0, 0, 119, 119, 119, 119, 112, 0, 0, 0});
		IMAGEDATA_EXPANDED.transparentPixel = 15;			/* use white for transparency */
		
		PaletteData uncheckedPalette = new PaletteData (	
			new RGB[] {new RGB (128, 128, 128), new RGB (255, 255, 255)});
		PaletteData grayUncheckedPalette = new PaletteData (	
			new RGB[] {new RGB (128, 128, 128), new RGB (192, 192, 192)});
		PaletteData checkMarkPalette = new PaletteData (	
			new RGB[] {new RGB (0, 0, 0), new RGB (252, 3, 251)});
		byte[] checkbox = new byte[] {0, 0, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 0, 0};
		IMAGEDATA_UNCHECKED = new ImageData (11, 11, 1, uncheckedPalette, 2, checkbox);
		IMAGEDATA_GRAY_UNCHECKED = new ImageData (11, 11, 1, grayUncheckedPalette, 2, checkbox);
		IMAGEDATA_CHECKMARK = new ImageData (7, 7, 1, checkMarkPalette, 1, new byte[] {-4, -8, 112, 34, 6, -114, -34});
		IMAGEDATA_CHECKMARK.transparentPixel = 1;
	}
public TreeItem (Tree parent, int style) {
	this (parent, style, checkNull (parent).getItemCount ());
}
public TreeItem (Tree parent, int style, int index) {
	super (parent, style);
	int validItemIndex = parent.getItemCount ();
	if (!(0 <= index && index <= validItemIndex)) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	initialize ();
	parent.addItem (this, index);
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (validColumnCount > 1) {
		texts = new String [validColumnCount];
		textWidths = new int [validColumnCount];
		images = new Image [validColumnCount];
	}
}
public TreeItem (TreeItem parentItem, int style) {
	this (parentItem, style, checkNull (parentItem).getItemCount ());
}
public TreeItem (TreeItem parentItem, int style, int index) {
	super (checkNull (parentItem).parent, style);
	this.parentItem = parentItem;
	parent = parentItem.getParent ();
	int validItemIndex = parentItem.getItemCount ();
	if (!(0 <= index && index <= validItemIndex)) error (SWT.ERROR_INVALID_RANGE);
	parentItem.addItem (this, index);
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (validColumnCount > 1) {
		texts = new String [validColumnCount];
		textWidths = new int [validColumnCount];
		images = new Image [validColumnCount];
	}
}
void addItem (TreeItem item, int index) {
	/* adds a child item to the receiver */
	TreeItem[] newChildren = new TreeItem [items.length + 1];
	System.arraycopy (items, 0, newChildren, 0, index);
	newChildren[index] = item;
	System.arraycopy (items, index, newChildren, index + 1, items.length - index);
	items = newChildren;

	/* if item should be available immediately then update parent accordingly */
	if (item.isAvailable ()) {
		parent.makeAvailable (item);
		parent.redrawFromItemDownwards (availableIndex);
	} else {
		/* receiver will need update if this is its first child */
		if (isAvailable () && items.length == 1) redrawItem ();
	}
}
static Tree checkNull (Tree tree) {
	if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return tree;
}

static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}
void columnAdded (TreeColumn column) {
	int index = column.getIndex ();
	int columnCount = parent.getColumnCount ();

	/*
	 * The texts, textWidths and images arrays always maintain at least one index, representing
	 * the automatic column that a Tree with no specified TreeColumns gets.
	 */
	if (columnCount > 1) {
		String[] newTexts = new String [columnCount];
		System.arraycopy (texts, 0, newTexts, 0, index);
		System.arraycopy (texts, index, newTexts, index + 1, columnCount - index - 1);
		texts = newTexts;
	
		Image[] newImages = new Image [columnCount];
		System.arraycopy (images, 0, newImages, 0, index);
		System.arraycopy (images, index, newImages, index + 1, columnCount - index - 1);
		images = newImages;
		
		int[] newTextWidths = new int [columnCount];
		System.arraycopy (textWidths, 0, newTextWidths, 0, index);
		System.arraycopy (textWidths, index, newTextWidths, index + 1, columnCount - index - 1);
		textWidths = newTextWidths;
	}

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
	
	/* notify all child items as well */
	for (int i = 0; i < items.length; i++) {
		items[i].columnAdded (column);
	}
}
void columnRemoved (TreeColumn column, int index) {
	int columnCount = parent.getColumnCount ();
	if (columnCount == 0) {
		/*
		 * The texts, textWidths and images arrays always maintain at least one index, representing
		 * the free column that a Tree with no specified TreeColumns gets.
		 */
		texts [0] = null;
		textWidths [0] = 0;
		images [0] = null;
		cellBackgrounds = cellForegrounds = null;
		cellFonts = null;
		fontHeights = null;
		/* notify all child items as well */
		for (int i = 0; i < items.length; i++) {
			items[i].columnRemoved (column, index);
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

	/* notify all child items as well */
	for (int i = 0; i < items.length; i++) {
		items[i].columnRemoved (column, index);
	}
}
/*
 * Returns a collection of all tree items descending from the receiver, including
 * the receiver.  The order of the items in this collection are receiver, child0tree,
 * child1tree, ..., childNtree. 
 */
TreeItem[] computeAllDescendents () {
	int childCount = items.length;
	TreeItem[][] childResults = new TreeItem[childCount][];
	int count = 1;	/* self */
	for (int i = 0; i < childCount; i++) {
		childResults[i] = items[i].computeAllDescendents ();
		count += childResults[i].length;
	}
	TreeItem[] result = new TreeItem[count];
	int index = 0;
	result[index++] = this;
	for (int i = 0; i < childCount; i++) {
		System.arraycopy (childResults[i], 0, result, index, childResults[i].length);
		index += childResults[i].length;
	}
	return result;
}
/*
 * Returns the number of tree items descending from the receiver (including the
 * receiver) that are currently available.  It is assumed that the receiver is
 * currently available. 
 */
int computeAvailableDescendentCount () {
	int result = 1;		/* receiver */
	if (!expanded) return result;
	for (int i = 0; i < items.length; i++) {
		result += items[i].computeAvailableDescendentCount ();
	}
	return result;
}
/*
 * Returns a collection of the tree items descending from the receiver (including
 * the receiver) that are currently available.  It is assumed that the receiver is
 * currently available.  The order of the items in this collection are receiver,
 * child0tree, child1tree, ..., childNtree. 
 */
TreeItem[] computeAvailableDescendents () {
	if (!expanded) return new TreeItem[] {this};
	int childCount = items.length;
	TreeItem[][] childResults = new TreeItem[childCount][];
	int count = 1;	/* self */
	for (int i = 0; i < childCount; i++) {
		childResults[i] = items[i].computeAvailableDescendents ();
		count += childResults[i].length;
	}
	TreeItem[] result = new TreeItem[count];
	int index = 0;
	result[index++] = this;
	for (int i = 0; i < childCount; i++) {
		System.arraycopy (childResults[i], 0, result, index, childResults[i].length);
		index += childResults[i].length;
	}
	return result;
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
				startIndex = parentItem.getItems ()[index - 1].availableIndex;
			} else {
				startIndex = parent.getItems ()[index - 1].availableIndex;
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
			parent.redrawItem (focusItem.availableIndex);
		}
	}
	if (parentItem != null) {
		parentItem.removeItem (this, index);
	}
	dispose (true);
	if (startIndex != -1) {
		parent.redrawItems (startIndex, endIndex);
	}
}
void dispose (boolean notifyParent) {
	if (isDisposed ()) return;
	super.dispose ();	/* the use of super is intentional here */
	if (notifyParent) parent.itemDisposing (this);
	for (int i = 0; i < items.length; i++) {
		items[i].dispose (notifyParent);
	}
	background = foreground = null;
	cellBackgrounds = cellForegrounds = null;
	font = null;
	cellFonts = null;
	images = null;
	texts = null;
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
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return getBackground ();
	if (cellBackgrounds == null || cellBackgrounds [columnIndex] == null) return getBackground ();
	return cellBackgrounds [columnIndex];
}
public Rectangle getBounds () {
	int columnCount = parent.getColumnCount ();
	int focusX = getFocusX ();
	
	/*
	 * If there are no columns then this is essentially the bounds of the text
	 */
	if (columnCount == 0) {
		return new Rectangle (
			focusX,
			parent.getItemY (this),
			getTextPaintWidth (0),
			parent.getItemHeight ());
	}
	
	/*
	 * If there are columns then this runs from the beginning of the column 0
	 * text to the end of the last column.
	 */
	TreeColumn lastColumn = parent.getColumn (columnCount - 1);
	return new Rectangle (
		focusX,
		parent.getItemY (this),
		lastColumn.getX () + lastColumn.getWidth () - focusX,
		parent.getItemHeight ());
}
public Rectangle getBounds (int columnIndex) {
	checkWidget ();
	int columnCount = parent.getColumnCount ();
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) {
		return new Rectangle (0, 0, 0, 0);
	}
	
	/*
	 * If there are no columns then this is the bounds of the receiver from the
	 * beginning of its expander to the end of its text.
	 */
	if (columnCount == 0) {
		int x = getExpanderBounds ().x;
		int width = getFocusX () + getTextPaintWidth (0) - x;
		return new Rectangle (x, parent.getItemY (this), width, parent.getItemHeight ());
	}
	TreeColumn column = parent.getColumn (columnIndex);
	return new Rectangle (column.getX (), parent.getItemY (this), column.getWidth (), parent.getItemHeight ());
}
Rectangle getCheckboxBounds () {
	if ((parent.getStyle () & SWT.CHECK) == 0) return null;
	int itemHeight = parent.getItemHeight ();
	Rectangle result = UncheckedImage.getBounds ();
	Point[] hLinePoints = getHconnectorEndpoints ();
	result.x = hLinePoints[1].x;
	result.y = parent.getItemY (this) + (itemHeight - result.height) / 2;
	return result;
}
public boolean getChecked () {
	checkWidget ();
	return checked;
}
/*
 * Returns the x value where the receiver's content (ie.- its image or text) begins
 * for the specified column.  For columns > 0 this must consider column alignment, and
 * for column 0 this is dependent upon the receiver's depth in the tree item hierarchy
 * and the presence/absence of a checkbox.
 */
int getContentX (int columnIndex) {
	if (columnIndex > 0) {
		TreeColumn column = parent.getColumn (columnIndex);
		int contentX = column.getX () + MARGIN_TEXT;
		if ((column.style & SWT.LEFT) != 0) return contentX;
		
		int contentWidth = textWidths [columnIndex];
		Image image = images [columnIndex];
		if (image != null) {
			contentWidth += Tree.MARGIN_IMAGE + image.getBounds ().width;
		}
		if ((column.style & SWT.RIGHT) != 0) {
			int padding = parent.getCellPadding ();
			contentX = Math.max (contentX, column.getX () + column.getWidth () - padding - contentWidth);	
		} else {	/* SWT.CENTER */
			contentX = Math.max (contentX, column.getX () + (column.getWidth () - contentWidth) / 2);
		}
		return contentX;
	}
	/* column 0 */
	if ((parent.style & SWT.CHECK) != 0) {
		Rectangle checkBounds = getCheckboxBounds ();
		return checkBounds.x + checkBounds.width + Tree.MARGIN_IMAGE;
	}
	return getHconnectorEndpoints ()[1].x + Tree.MARGIN_IMAGE;
}
int getDepth () {
	if (parentItem == null) return 0;
	return 1 + parentItem.getDepth ();
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
	int itemHeight = parent.getItemHeight ();
	int x = parent.getCellPadding () - parent.horizontalOffset;
	int y = parent.getItemY (this);
	if (!isRoot ()) {
		int expanderWidth = ExpandedImage.getBounds ().width + INDENT_HIERARCHY;
		x += expanderWidth * getDepth ();
	}
	Rectangle result = ExpandedImage.getBounds ();
	result.x = x;
	result.y = y + (itemHeight - result.height) / 2;
	return result;
}
/*
 * Returns the bounds that should be used for drawing a focus rectangle on the receiver
 */
Rectangle getFocusBounds () {
	int x = getFocusX ();
	int width;
	if (parent.getColumnCount () == 0) {
		width = getTextPaintWidth (0) - 1;
	} else {
		width = parent.getColumn (0).getWidth () - x - 2;
	}
	return new Rectangle (x, parent.getItemY (this) + 1, width, parent.getItemHeight () - 1);
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
	int validColumnCount = Math.max (1, parent.getColumnCount ());
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
	int validColumnCount = Math.max (1, parent.getColumnCount ());
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
	if (getItemCount () == 0) {
		x = expanderBounds.x + Compatibility.ceil (expanderBounds.width, 2);
		width = Compatibility.floor (expanderBounds.width, 2) + INDENT_HIERARCHY;
	} else {
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
 * Returns the bounds representing the clickable region that should select the receiver
 */
Rectangle getHitBounds () {
	int contentX = getContentX (0);
	int width = 0;
	if (parent.getColumnCount () == 0) {
		width = getFocusX () + getTextPaintWidth (0) - contentX; 
	} else {
		TreeColumn column = parent.getColumn (0);
		width = column.getWidth () - contentX;
	}
	return new Rectangle (contentX, parent.getItemY (this), width, parent.getItemHeight ());
}
public Image getImage () {
	checkWidget ();
	return getImage (0);
}
public Image getImage (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return null;
	return images [columnIndex];
}
public Rectangle getImageBounds (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return new Rectangle (0,0,0,0);

	int padding = parent.getCellPadding ();
	int startX = getContentX (columnIndex);
	int itemHeight = parent.getItemHeight ();
	int y = parent.getItemY (this);
	Image image = images [columnIndex]; 
	if (image == null) {
		return new Rectangle (startX, y + padding, 0, itemHeight - 2 * padding);
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
	int imageSpaceY = itemHeight - (2 * padding);
	int drawHeight = Math.min (imageSpaceY, imageBounds.height);
	return new Rectangle (
		startX, y + (itemHeight - drawHeight) / 2,
		drawWidth, drawHeight);
}
int getIndex () {
	TreeItem[] items;
	if (parentItem != null) {
		items = parentItem.getItems ();
	} else {
		items = parent.getItems ();
	}
	for (int i = 0; i < items.length; i++) {
		if (items[i] == this) return i;
	}
	return -1;
}
public int getItemCount () {
	checkWidget ();
	return items.length;
}
public TreeItem [] getItems () {
	checkWidget ();
	TreeItem result[] = new TreeItem[items.length];
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
public String getText () {
	checkWidget ();
	return getText (0);
}
public String getText (int columnIndex) {
	checkWidget ();
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return "";
	if (texts [columnIndex] == null) return "";
	return texts [columnIndex];
}
/*
 * Returns the full width required for painting the receiver's text for the specified
 * column, including the margins on the ends of the text. 
 */
int getTextPaintWidth (int columnIndex) {
	int result = textWidths [columnIndex];
	if (result > 0) result += 2 * MARGIN_TEXT;
	return result;
}
/*
 * Returns the x value at which the receiver's text begins.
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
void initialize () {
	if (ExpandedImage == null) {
		Display display = getDisplay ();
		ExpandedImage = new Image (display, IMAGEDATA_EXPANDED);
		CollapsedImage = new Image (display, IMAGEDATA_COLLAPSED);
		UncheckedImage = new Image (display, IMAGEDATA_UNCHECKED);
		GrayUncheckedImage = new Image (display, IMAGEDATA_GRAY_UNCHECKED);
		CheckmarkImage = new Image (display, IMAGEDATA_CHECKMARK);
		LinesColor = display.getSystemColor (SWT.COLOR_GRAY);
		SelectionBackgroundColor = display.getSystemColor (SWT.COLOR_LIST_SELECTION);
		SelectionForegroundColor = display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT);
	}
}
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
		return getIndex () == parentItem.getItemCount () - 1;
	}
	return getIndex () == parent.getItemCount () - 1;
}
boolean isRoot () {
	return parentItem == null;
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

	Rectangle cellBounds = getBounds (columnIndex);
	int cellRightX = 0;
	if (column != null) {
		cellRightX = column.getX () + column.getWidth ();
	} else {
		cellRightX = cellBounds.x + cellBounds.width;
	}

	/* if this cell is completely to the left of the client area then there's no need to paint it */
	if (cellRightX < 0) return;

	/* restrict the clipping region to the full cell */
	gc.setClipping (x, cellBounds.y, cellRightX - x, cellBounds.height);
	
	int y = parent.getItemY (this);
	int padding = parent.getCellPadding ();
	int itemHeight = parent.getItemHeight ();

	/* draw the background color if this item has a custom color set */
	Color background = getBackground (columnIndex);
	if (background != parent.getBackground ()) {
		Color oldBackground = gc.getBackground ();
		gc.setBackground (background);
		if (columnIndex == 0) {
			int focusX = getFocusX ();
			gc.fillRectangle (focusX, parent.getItemY (this) + 1, cellRightX - focusX - 1, parent.getItemHeight () - 1);
		} else {
			gc.fillRectangle (cellBounds.x, cellBounds.y + 1, cellBounds.width - 1, cellBounds.height - 1);
		}
		gc.setBackground (oldBackground);
	}

	/* draw the selection bar if the receiver is selected */
	if (isSelected () && columnIndex == 0) {
		Color oldBackground = gc.getBackground ();
		gc.setBackground (SelectionBackgroundColor);
		int startX = getFocusX () + 1;
		gc.fillRectangle (
			startX,
			cellBounds.y + padding,
			Math.max (0, cellRightX - startX - 2),
			Math.max (0, cellBounds.height - (padding * 2) + 1));
		gc.setBackground (oldBackground);
	}
		
	if (!paintCellContent) return;

	/* Draw column 0 decorations */
	if (columnIndex == 0) {
		/* Draw hierarchy connector lines */
		Rectangle expanderBounds = getExpanderBounds ();
		Color oldForeground = gc.getForeground ();
		gc.setForeground (LinesColor);

		/* Draw vertical line above expander */
		int lineX = expanderBounds.x + expanderBounds.width / 2;
		int itemCount = getItemCount ();
		int y2 = expanderBounds.y;
		if (itemCount == 0) {
			y2 += expanderBounds.height / 2;
		}
		
		/* Do not draw this line iff this is the very first item in the tree */ 
		if (!isRoot () || getIndex () != 0) {
			gc.drawLine (lineX, y, lineX, y2);
		}

		/* Draw vertical line below expander if the receiver has lower siblings */
		if (!isLastChild ()) {
			if (itemCount != 0) y2 += expanderBounds.height;
			gc.drawLine (lineX, y2, lineX, y + itemHeight);
		}

		/* Draw horizontal line to right of expander */
		Point[] endpoints = getHconnectorEndpoints ();
		gc.drawLine (endpoints[0].x, endpoints[0].y, endpoints[1].x - Tree.MARGIN_IMAGE, endpoints[1].y);
		
		/* 
		 * Draw hierarchy lines that are needed by other items that are shown below
		 * this item but whose parents are shown above.
		 */
		TreeItem item = getParentItem ();
		while (item != null) {
			if (!item.isLastChild ()) {
				Rectangle itemExpanderBounds = item.getExpanderBounds ();
				lineX = itemExpanderBounds.x + itemExpanderBounds.width / 2;
				gc.drawLine (lineX, y, lineX, y + itemHeight);
			}
			item = item.getParentItem ();
		}
		gc.setForeground (oldForeground);
		
		/* Draw expand/collapse image if receiver has children */
		if (items.length > 0) {
			Image image = expanded ? ExpandedImage : CollapsedImage;
			gc.drawImage (image, expanderBounds.x, expanderBounds.y);
		}
		
		/* Draw checkbox if parent Tree has style SWT.CHECK */
		if ((parent.style & SWT.CHECK) != 0) {
			Image baseImage = grayed ? GrayUncheckedImage : UncheckedImage;
			Rectangle checkboxBounds = getCheckboxBounds ();
			gc.drawImage (baseImage, checkboxBounds.x, checkboxBounds.y);

			if (checked) {
				Rectangle checkmarkBounds = CheckmarkImage.getBounds ();
				int xInset = (checkboxBounds.width - checkmarkBounds.width) / 2;
				int yInset = (checkboxBounds.height - checkmarkBounds.height) / 2;
				gc.drawImage (CheckmarkImage, checkboxBounds.x + xInset, checkboxBounds.y + yInset);
			}
		}
	}

	Image image = images [columnIndex];
	String text = getText (columnIndex);
	Rectangle imageArea = getImageBounds (columnIndex);
	int startX = imageArea.x;
	
	/* while painting the cell's content restrict the clipping region */
	gc.setClipping (
		startX,
		cellBounds.y + padding,
		Math.max (0, cellRightX - startX - padding),
		Math.max (0, cellBounds.height - (2 * padding)));

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
			gc.setForeground (SelectionForegroundColor);
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
void recomputeTextWidths (GC gc) {
	textWidths = new int [texts.length];
	Font oldFont = gc.getFont ();
	for (int i = 0; i < texts.length; i++) {
		String value = texts [i];
		if (value != null) {
			boolean fontChanged = false;
			Font font = getFont (i);
			if (!font.equals (oldFont)) {
				gc.setFont (font);
				fontChanged = true;
			}
			textWidths[i] = gc.textExtent (value).x;
			if (fontChanged) gc.setFont (oldFont);
		}
	}
}
void redrawItem () {
	parent.redraw (0, parent.getItemY (this), parent.getClientArea ().width, parent.getItemHeight (), false);
}
/*
 * Make changes that are needed to handle the disposal of a child item.
 */
void removeItem (TreeItem item, int index) {
	if (isDisposed ()) return;
	TreeItem[] newItems = new TreeItem[items.length - 1];
	System.arraycopy (items, 0, newItems, 0, index);
	System.arraycopy (items, index + 1, newItems, index, newItems.length - index);
	items = newItems;
	// TODO second condition below is ugly, handles creation of item within Expand callback
	if (items.length == 0 && !parent.inExpand) {
		expanded = false;
		if (isAvailable ()) redrawItem ();	/* expander no longer needed */
	}
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
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellBackgrounds == null) {
		cellBackgrounds = new Color [validColumnCount];
	}
	if (cellBackgrounds [columnIndex] == value) return;
	if (cellBackgrounds [columnIndex] != null && cellBackgrounds [columnIndex].equals (value)) return;
	cellBackgrounds [columnIndex] = value;
	redrawItem ();
}
public void setChecked (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (checked == value) return;
	checked = value;
	Rectangle bounds = getCheckboxBounds ();
	parent.redraw (bounds.x, bounds.y, bounds.width, bounds.height, false);
}
public void setExpanded (boolean value) {
	checkWidget ();
	if (expanded == value) return;
	if (items.length == 0) return;
	// TODO the next line seems to match other platforms, test case is lazy Tree snippet
	if (parent.inExpand) return;
	if (value) {
		expanded = value;
		if (availableIndex == -1) return;
		parent.makeDescendentsAvailable (this);
		parent.redrawFromItemDownwards (availableIndex);
	} else {
		int oldAvailableLength = parent.availableItems.length;
		TreeItem[] descendents = computeAvailableDescendents ();
		expanded = value;
		if (availableIndex == -1) return;
		parent.makeDescendentsUnavailable (this, descendents);
		/* move focus (and selection if SWT.SINGLE) to item if a descendent had focus */
		TreeItem focusItem = parent.focusItem;
		if (focusItem != null && focusItem != this && focusItem.hasAncestor (this)) {
			parent.setFocusItem (this, true);
			if ((style & SWT.SINGLE) != 0) {
				parent.selectedItems = new TreeItem[] {this};
			}
			/* Fire an event since the selection is being changed automatically */
			Event newEvent = new Event ();
			newEvent.item = this;
			parent.sendEvent (SWT.Selection, newEvent);
			if (isDisposed ()) return;
			parent.showItem (this);
		}
		parent.redrawItems (availableIndex, oldAvailableLength - 1);
	}
}
public void setFont (Font value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (font == value) return;
	if (value != null && value.equals (font)) return;
	
	font = value;
	/* recompute cached values for string measurements */
	GC gc = new GC (parent);
	gc.setFont (getFont ());
	recomputeTextWidths (gc);
	fontHeight = gc.getFontMetrics ().getHeight ();
	gc.dispose ();
	redrawItem ();
}
public void setFont (int columnIndex, Font value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}

	int validColumnCount = Math.max (1, parent.getColumnCount ());
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
	String string = getText (columnIndex);
	if (string.length () > 0) {
		textWidths [columnIndex] = gc.textExtent (string).x;
	}
	gc.dispose ();

	redrawItem ();
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
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (cellForegrounds == null) {
		cellForegrounds = new Color [validColumnCount];
	}
	if (cellForegrounds [columnIndex] == value) return;
	if (cellForegrounds [columnIndex] != null && cellForegrounds [columnIndex].equals (value)) return;
	cellForegrounds [columnIndex] = value;
	redrawItem ();
}
public void setGrayed (boolean value) {
	checkWidget ();
	if ((parent.getStyle () & SWT.CHECK) == 0) return;
	if (grayed == value) return;
	grayed = value;
	redrawItem ();
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
		if (value[i] != null) setImage (i, value[i]);
	}
}
public void setImage (int columnIndex, Image value) {
	checkWidget ();
	if (value != null && value.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (value == images [columnIndex]) return;
	if (value != null && value.equals (images [columnIndex])) return;
	images [columnIndex] = value;
	if (value == null) {
		redrawItem ();
		return;
	}

	/*
	 * If this is the first image being put into the tree then its item height
	 * may be adjusted, in which case a full redraw is needed.
	 */
	if (parent.imageHeight == 0) {
		int oldItemHeight = parent.getItemHeight ();
		parent.setImageHeight (value.getBounds ().height);
		if (oldItemHeight != parent.getItemHeight ()) {
			if (columnIndex == 0) {
				parent.col0ImageWidth = value.getBounds ().width;
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
		if (parent.getColumnCount () == 0) {
			parent.redraw ();
		} else {
			parent.redraw (
				0, 0,
				parent.getColumn (0).getWidth (),
				parent.getClientArea ().height,
				true);
		}
	}
	redrawItem ();
}
public void setText (String value) {
	checkWidget ();
	setText (0, value);
}
public void setText (String[] value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);

	// TODO make a smarter implementation of this
	for (int i = 0; i < value.length; i++) {
		if (value[i] != null) setText (i, value[i]);
	}
}
public void setText (int columnIndex, String value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	int validColumnCount = Math.max (1, parent.getColumnCount ());
	if (!(0 <= columnIndex && columnIndex < validColumnCount)) return;
	if (value.equals (getText (columnIndex))) return;
	texts [columnIndex] = value;
	if (columnIndex == 0) super.setText (value);	// TODO can remove this
	
	GC gc = new GC (parent);
	gc.setFont (getFont (columnIndex));
	textWidths [columnIndex] = gc.textExtent (value).x;
	gc.dispose ();
	redrawItem ();
}
/*
 * The parent's font has changed, so if this font was being used by the receiver then
 * recompute its cached text sizes using the gc argument.  Pass this notification on to
 * all child items as well.
 */
void updateFont (GC gc) {
	if (font == null) {		/* receiver is using the Tree's font */
		recomputeTextWidths (gc);
	}
	/* pass notification on to all children */
	for (int i = 0; i < items.length; i++) {
		items[i].updateFont (gc);
	}
}
}

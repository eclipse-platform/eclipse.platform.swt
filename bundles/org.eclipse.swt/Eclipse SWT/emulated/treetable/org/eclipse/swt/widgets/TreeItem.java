package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.util.Enumeration;
import java.util.Vector;
 
/**
 * Instances of this class represent a selectable user interface object
 * that represents a hierarchy of tree items in a tree widget.
 * 
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
public class TreeItem extends AbstractTreeItem {
/*
 * This class caches geometric data for drawing.
 * A description of the cached data follows:
 *
 *  |      1    ||      5        |  
 *  |  2  |               |   6  |   
 *  |3                          7|  
 *   _____  | 4 |f|          |8      
 *  |     |                            ____
 *  |  -  | ===== {image}    root      9
 *  |_____|          |
 *                       |b|c|  |d|
 *               | e |
 * 
 * Widths are measured between vertical lines.
 *
 * Cached item rendering data:
 * 1 = getDecorationsWidth
 * 2 = getHierarchyIndicatorRect
 * 3 = getPaintStartX
 * 4 = getItemConnectorWidth
 * 5 = getItemWidth
 * 6 = getSelectionWidth
 * 7 = getPaintStopX
 * 8 - getTextXPos
 * 9 = getTextYPosition
 *
 * Rendering constants:
 * 4 = DEFAULT_ITEM_CONNECTOR_WIDTH, used when no image is set in the tree.
 *	Otherwise it is the image width.
 * b = IMAGE_PADDING
 * c = TEXT_INDENT
 * d = SELECTION_PADDING
 * e = ITEM_NOIMAGE_OFFSET
 * f = ITEM_CONNECTOR_PADDING;
 */
	private static final int DEFAULT_ITEM_CONNECTOR_WIDTH = 8;	// Default width of the horizontal line connecting 
															// items with the vertical lines. Only used when
															// no image is set in the tree. Normally connector 
															// line width is half the image width.
	private static final int ITEM_CONNECTOR_PADDING = 2;	// Added to the calculated item connector width
	private static final int IMAGE_PADDING = 3;				// Space behind bitmap
	private static final int ITEM_NOIMAGE_OFFSET = 8;		// Offset added to the calculated paint position where 
															// an item starts drawing. To be used when no item 
															// image has been set. Otherwise children would start 
															// drawing at the end of the horizontal item connector 
															// of their parent.
	private static final int ROOT_INDENT = 5;				// Indent of root items
	private static final int SELECTION_PADDING = 2;			// Space behind text
	private static final int TEXT_INDENT = 2;				// Identation of the item label
	
	// basic item info
	private TreeItem parentItem;
	private int index;										// index in the parent item
	
	// geometrical item info
	private int paintStartX = -1;							// X coordinate of the upper-left corner of the 
															// receivers bounding rectangle
	private Point itemExtent;								// Size of the item (image + label)
	private Point imageExtent;								// original size of the item image	
	private int textYPosition = -1;							// Centered y position of the item text	


	//Determine whether the item is being expanded
	private boolean isExpanding = false;
	Color background = null;
	Color foreground = null;
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
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
public TreeItem(Tree parent, int style) {
	this(parent, style, checkNull(parent).getItemCount());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
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
public TreeItem(Tree parent, int style, int index) {
	super(parent, style);
	parent.addItem(this, index);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
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
 * @param parentItem a composite control which will be the parent of the new instance (cannot be null)
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
public TreeItem(TreeItem parentItem, int style) {
	this(parentItem, style, checkNull(parentItem).getItemCount());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
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
 * @param parentItem a composite control which will be the parent of the new instance (cannot be null)
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
public TreeItem(TreeItem parentItem, int style, int index) {
	super(checkNull(parentItem).getParent(), style);
	setParentItem(parentItem);	
	parentItem.add(this, index);
}

/**
 * Calculate the number of expanded children.
 * Recurse up in the tree to the root item.
 */
void calculateVisibleItemCount() {
	Vector children;
	TreeItem child;
	int visibleItemCount = 0;
	
	// check isExpanded field directly for performance
	if (internalGetExpanded() == true) {
		children = getChildren();
		visibleItemCount = children.size();
		for (int i = 0; i < children.size(); i++) {
			child = (TreeItem) children.elementAt(i);
			visibleItemCount += child.getVisibleItemCount();
		}
	}
	setVisibleItemCount(visibleItemCount);
	calculateVisibleItemCountParent();
}
/**
 * Calculate the number of expanded children for the parent item
 * of this item.
 */
void calculateVisibleItemCountParent() {
	TreeItem parentItem = getParentItem();

	if (parentItem != null) {
		parentItem.calculateVisibleItemCount();
	}
	else {
		getParent().getRoot().calculateVisibleItemCount();
	}
}
/**
 * Throw an SWT.ERROR_NULL_ARGUMENT exception if 'tree' is null.
 * Otherwise return 'tree'
 */
static Tree checkNull(Tree tree) {
	if (tree == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return tree;
}
/**
 * Throw an SWT.ERROR_NULL_ARGUMENT exception if 'item' is null.
 * Otherwise return 'item'
 */
static TreeItem checkNull(TreeItem item) {
	if (item == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return item;
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Draw the hierarchy indicator at 'position'.
 *
 * Note:
 * Assumes that the hierarchy indicators for the expanded and 
 * collapsed state are the same size.
 * @param gc - GC to draw on. 
 * @param position - position on the GC to draw at.
 * @return position to continue drawing 
 */
Point drawHierarchyIndicator(GC gc, Point position) {
	Tree parent = getParent();
	Image hierarchyImage;
	Rectangle indicatorRectangle = parent.getHierarchyIndicatorRect();
	int x = position.x;
	int y = position.y;
	int yCenter = y + parent.getItemHeight() / 2;
	Point connectorLinePosition;	

	if (isLeaf() == false) {
		if (getExpanded() == true) {
			hierarchyImage = parent.getExpandedImage();
		}
		else {
			hierarchyImage = parent.getCollapsedImage();
		}
		if (hierarchyImage != null) {
			gc.drawImage(hierarchyImage, x + indicatorRectangle.x, y + indicatorRectangle.y);
		}
		connectorLinePosition = new Point(x + indicatorRectangle.width, yCenter);		
	}	
	else {
		connectorLinePosition = new Point(
			x + indicatorRectangle.width / 2 				
			+ indicatorRectangle.width % 2, yCenter);		// % 2 in order to not start the next hierarchy 
															// component at the middle of the icon but after.	
	}
	return connectorLinePosition;
}
/**
 * Draw a horizontal line connecting the item image (or label 
 * if there is no image) to the vertical line connecting to 
 * the parent.
 * @param gc - GC to draw on. 
 * @param position - position on the GC to draw at.
 * @return position to continue drawing 
 */
Point drawHorizontalItemConnector(GC gc, Point position) {
	int itemConnectorEndPos = position.x + getItemConnectorWidth() - 1;	// -1 because the position of the last pixel needs to be calculated

	gc.drawLine(position.x, position.y, itemConnectorEndPos, position.y);
	return new Point(itemConnectorEndPos + 1, position.y);		// + 1 in order to resume drawing after line not on end of line
}
/** 
 * Display the item image at 'position' using 'gc'.
 * @param gc - GC to draw on
 * @param position - position on the GC to draw at
 * @return position to continue drawing 
 */
Point drawImage(GC gc, Point destinationPosition) {
	Tree parent = getParent();
	Image image = getImage();
	Point sourceImageExtent;
	Point destinationImageExtent = parent.getImageExtent();
	int yCenter;
	
	if (image != null) {
		sourceImageExtent = getImageExtent();
		yCenter = (parent.getItemHeight() - destinationImageExtent.y) / 2;
		gc.drawImage(
			image, 
			0, 0, 														// source x, y
			sourceImageExtent.x, sourceImageExtent.y,					// source width, height
			destinationPosition.x, destinationPosition.y + yCenter,		// destination x, y
			destinationImageExtent.x, destinationImageExtent.y);		// destination width, height
	}
	if (destinationImageExtent != null) {
		destinationPosition.x += destinationImageExtent.x + IMAGE_PADDING;
	}
	return destinationPosition;
}
/**
 * Draw a rectangle enclosing the item label. The rectangle
 * indicates that the receiver was selected last and that it has
 * the input focus.
 * The rectangle will only be drawn if the receiver is selected.
 * @param gc - GC to draw on. 
 * @param position - position on the GC to draw at.
 */
void drawSelectionFocus(GC gc, Point position) {
	Point selectionExtent = getSelectionExtent();

	if (selectionExtent == null) {
		return;
	}
	if (getParent().hasFocus(this) == true) {
		gc.drawFocus(
			position.x, position.y, 
			selectionExtent.x, selectionExtent.y);
	}
}
/**
 * Draw a vertical line connecting the horizontal connector line 
 * with that of the previous item.
 * Called recursively to draw the lines on all tree levels.
 * @param gc - GC to draw on. 
 * @param yPosition - y position of the upper side of the 
 *	receiver's bounding box.
 * @param isFirstChild - method is called to draw a vertical 
 *	line for the first child. Leave room for the hierarchy icon.
 */
void drawVerticalItemConnector(GC gc, int yPosition, boolean isFirstChild) {
	Tree parent = getParent();
	TreeItem nextDrawItem = getParentItem();
	AbstractTreeItem parentItem = nextDrawItem;
	Rectangle indicatorRectangle = parent.getHierarchyIndicatorRect();
	int itemHeight = parent.getItemHeight();	
	int itemHeightDiv2 = itemHeight / 2 + itemHeight % 2;
	int indicatorHeightDiv2 = indicatorRectangle.height / 2 + indicatorRectangle.height % 2;
	int lineX = getPaintStartX() + indicatorRectangle.width / 2;
	int lineStartY = yPosition - itemHeightDiv2;	
	int lineEndY = yPosition + itemHeightDiv2;

	if (parentItem == null) {
		parentItem = parent.getRoot();
	}
	if (getIndex() != parentItem.getItemCount()-1) {		// if item is not the last child
		if (isFirstChild == true) {
			lineStartY += indicatorHeightDiv2;				// leave space for the hierarchy image
		}	
		gc.drawLine(lineX, lineStartY, lineX, lineEndY);
	}
	
	if (nextDrawItem != null) {
		nextDrawItem.drawVerticalItemConnector(gc, yPosition, false);
	}	
}
/**
 * Draw a vertical line connecting the horizontal connector line
 * with that of the previous item.
 * Do this on all tree levels up to the root level.
 * @param gc - GC to draw on. 
 * @param position - position on the GC to draw at.
 * @return position to continue drawing 
 */
Point drawVerticalItemConnector(GC gc, Point position) {
	Tree parent = getParent();
	TreeItem parentItem = getParentItem();	
	Rectangle indicatorRectangle = parent.getHierarchyIndicatorRect();
	int itemHeight = parent.getItemHeight();
	int itemHeightDiv2 = itemHeight / 2 + itemHeight % 2;
	int indicatorHeightDiv2 = indicatorRectangle.height / 2 + indicatorRectangle.height % 2;
	int lineX = position.x + indicatorRectangle.width / 2;
	int lineStartY = position.y - itemHeightDiv2;	
	int lineEndY = position.y + itemHeightDiv2 - itemHeight % 2;
	TreeItem predecessor;
	boolean isFirstChild = false;

	if (isRoot() == true) {
		if (getIndex() == 0) {
			return position;									// first root, don't draw vertical line
		}
	}
	else	
	if (getIndex() == 0) {										// if item is first child
		lineStartY += itemHeightDiv2;
		isFirstChild = true;
	}
	predecessor = getPredecessor();
	if (predecessor != null && predecessor.isLeaf() == false) {
		lineStartY += indicatorHeightDiv2;						// leave space for the hierarchy image
	}
	if (isLeaf() == false) {
		lineEndY -= indicatorHeightDiv2;
	}
	gc.drawLine(lineX, lineStartY, lineX, lineEndY);
	if (parentItem != null) {
		parentItem.drawVerticalItemConnector(gc, position.y, isFirstChild);
	}
	return position;
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
public Color getBackground () {
	checkWidget ();
	if (background != null) return background;
	Tree parent = getParent();
	return parent.getBackground();
}
/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds() {
	checkWidget();
	Tree parent = getParent();
	Point extent = getItemExtent();
	int x = getTextXPos() - TEXT_INDENT;
	
	return new Rectangle(x, parent.getRedrawY(this), extent.x - (x - getItemStartX()), extent.y);	
}

/**
 * Answer the x position of the item check box
 */
int getCheckboxXPosition() {
	return getPaintStartX() + getDecorationsWidth();
}
/**
 * Answer the combined width of the hierarchy indicator and 
 * the horizontal item connector line.
 */
int getDecorationsWidth() {
	int indicatorWidth = getParent().getHierarchyIndicatorRect().width;
	int width = indicatorWidth + getItemConnectorWidth();

	if (isLeaf() == true) {
		width -= indicatorWidth / 2;
	}
	return width;
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
public Color getForeground () {
	checkWidget ();
	if (foreground != null) return foreground;
	Tree parent = getParent();
	return parent.getForeground();
}
/**
 * Answer the index of the receiver relative to the first root 
 * item.
 * @return
 *	The index of the receiver relative to the first root item.
 */
int getGlobalIndex() {
	int globalItemIndex = getIndex();
	AbstractTreeItem item = null;

	if (isRoot() == false) {
		item = getParentItem();
		globalItemIndex++;						// adjust for 0-based non-root items
	}
	else {	
		item = getParent().getRoot();
	}

	globalItemIndex += item.getVisibleIndex(getIndex());
	return globalItemIndex;
}
/**
 * Answer the original size of the image of the receiver.
 */
Point getImageExtent() {
	Image image = getImage();
	Rectangle imageBounds;
	
	if (imageExtent == null && image != null) {
		imageBounds = image.getBounds();
		imageExtent = new Point(imageBounds.width, imageBounds.height);
	}
	return imageExtent;
}
/**
 * Answer the receiver's index into its parent's list of children
 */
int getIndex() {
	return index;
}
/**
 * Answer the width of the horizontal item connector line.
 */
int getItemConnectorWidth() {
	Tree parent = getParent();
	Point imageExtent = parent.getImageExtent();
	int itemConnectorWidth;
	int indicatorWidth = parent.getHierarchyIndicatorRect().width;

	if (imageExtent != null) {
		itemConnectorWidth = imageExtent.x / 2 + ITEM_CONNECTOR_PADDING;
	}
	else {
		itemConnectorWidth = DEFAULT_ITEM_CONNECTOR_WIDTH;
	}
	if (isLeaf() == false) {	// has children = has hierarchy indicator = shorter connector
		itemConnectorWidth -= indicatorWidth / 2;
	}
	return itemConnectorWidth;
}
/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount() {
	checkWidget();
	return super.getItemCount();
}
/**
 * Answer the size of the receiver as displayed on the screen.
 */
Point getItemExtent() {
	Tree parent;
	Point imageExtent;
	String text;
	int itemWidth;
	
	if (itemExtent == null) {
		parent = getParent();
		imageExtent = parent.getImageExtent();
		text = getText();
		itemWidth = SELECTION_PADDING;
		if (text != null) {
			itemWidth += parent.getTextWidth(text) + TEXT_INDENT;
		}
		if (imageExtent != null) {
			itemWidth += imageExtent.x + IMAGE_PADDING;
		}
		itemExtent = new Point(itemWidth, parent.getItemHeight());
	}
	return itemExtent;
}
/**
 * Answer the x position at which painting of the receiver's 
 * contents (ie. image, text) can begin.
 */
int getItemStartX() {
	int itemStartX = getPaintStartX() + getDecorationsWidth();
	
	if (isCheckable() == true) {
		itemStartX += getCheckboxBounds().width + CHECKBOX_PADDING;
	}
	return itemStartX;
}
/**
 * Returns an array of <code>TreeItem</code>s which are the
 * direct item children of the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems() {
	checkWidget();
	TreeItem childrenArray[] = new TreeItem[getItemCount()];

	getChildren().copyInto(childrenArray);
	return childrenArray;	
}
/**
 * Answer the x position where the receiver is drawn.
 */
int getPaintStartX() {
	Tree parent = getParent();
	Point imageExtent;
	TreeItem parentItem;

	if (paintStartX == -1) {
		if (isRoot() == true) {
			paintStartX = ROOT_INDENT;
		}
		else {
			parentItem = getParentItem();
			// subtract parent.getHorizontalOffset() to calculate the cached start 
			// position independent of the horizontal scroll offset. Fixes 1G1L7EU.
			paintStartX = parentItem.getPaintStartX() 
				- parent.getHorizontalOffset()	
				+ parentItem.getDecorationsWidth()
				- parent.getHierarchyIndicatorRect().width / 2;
			imageExtent = parent.getImageExtent();
			if (imageExtent != null) {
				paintStartX += imageExtent.x / 2;
			}
			else {
				paintStartX += ITEM_NOIMAGE_OFFSET;
			}
		}
	}
	return paintStartX + parent.getHorizontalOffset();
}
/**
 * Answer the pixel at which the receiver stops drawing.
 */
int getPaintStopX() {
	return (getItemStartX() + getItemExtent().x - getParent().getHorizontalOffset());
}
/**
 * Returns the receiver's parent, which must be a <code>Tree</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Tree getParent() {
	checkWidget();
	return (Tree) super.getSelectableParent();
}
/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getParentItem() {
	checkWidget();
	return parentItem;
}
/**
 * Answer the item that directly precedes the receiver.
 * Answer null if this is the first item in a hierarchy level
 * or if there are expanded children in the previous item.
 */
TreeItem getPredecessor() {
	AbstractTreeItem parentItem = getParentItem();
	Vector children;
	int previousIndex = getIndex() - 1;
	TreeItem previousItem = null;

	if (parentItem == null) {
		parentItem = getParent().getRoot();
	}
	if (previousIndex >= 0) {
		children = parentItem.getChildren();
		previousItem = (TreeItem) children.elementAt(previousIndex);
		if (previousItem.isLeaf() == false && previousItem.getExpanded() == true) {
			previousItem = null;	// no immediate predecessor because there are expanded children
		}
	}
	return previousItem;	
}
/**
 * Answer the size of the rectangle drawn to indicate the
 * selected state of the receiver.
 * This is also used to draw the selection focus rectangle.
 */
Point getSelectionExtent() {
	Point selectionExtent = getItemExtent();
	Point imageExtent = getParent().getImageExtent();
	int x = selectionExtent.x;

	if (imageExtent != null) {
		x -= imageExtent.x + IMAGE_PADDING;
	}
	return new Point(x, selectionExtent.y);
}
/**
 * Return the x position of the selection rectangle
 */
int getSelectionX() {
	return getTextXPos() - TEXT_INDENT;
}
/**
 * Answer the x position where the receiver draws the item text.
 * This position is relative to the item start position.
 */
int getTextXPos() {
	Point imageExtent = getParent().getImageExtent();
	int textXPos = getItemStartX() + TEXT_INDENT;

	if (imageExtent != null) {
		textXPos += imageExtent.x + IMAGE_PADDING;
	}
	return textXPos;
}
/**
 * Answer the y position of the receiver's text.
 * @param 
 *	gc - GC to use for calculating the text y position 
 */
int getTextYPosition(GC gc) {
	String text;

	if (textYPosition == -1) {
		text = getText();
		if (text != null) {
			textYPosition = (getParent().getItemHeight() - gc.stringExtent(text).y) / 2;
		}
		else {
			textYPosition = 0;
		}
	}
	return textYPosition;
}
/**
 * Answer the index of the receiver relative to the first root 
 * item.
 * If 'anIndex' is the index of the expanded item 'anItem' 
 * then the following expressions are true:
 * 'anItem  == theRoot.getVisibleItem(anIndex)' and
 * 'anIndex == anItem.getVisibleIndex()'
 * @return
 *	The index of the receiver relative to the first root item.
 *	Answer -1 if the receiver is not visible (because the parent 
 *	is collapsed).
 */
int getVisibleIndex() {
	int visibleItemIndex = getIndex();
	AbstractTreeItem item = null;

	if (isRoot() == false) {
		if (isVisible() == false) {
			return -1;		
		}
		item = getParentItem();
		visibleItemIndex++;						// adjust for 0-based non-root items
	}
	else {	
		item = getParent().getRoot();
	}

	visibleItemIndex += item.getVisibleIndex(getIndex());
	return visibleItemIndex;
}
/**
 * Answer the index of the child item identified by 'childIndex' 
 * relative to the first root item.
 */
int getVisibleIndex(int childIndex) {
	Enumeration children = getChildren().elements();
	TreeItem child;
	int visibleItemIndex = getIndex();

	if (isRoot() == false) {
		visibleItemIndex++;									// adjust for 0-based non-root items
	}

	while (children.hasMoreElements() == true) {
		child = (TreeItem) children.nextElement();
		if (child.getIndex() == childIndex) {
			if (isRoot() == false) {
				visibleItemIndex += getParentItem().getVisibleIndex(getIndex());
			}
			else {
				visibleItemIndex += getParent().getRoot().getVisibleIndex(getIndex());
			}
			break;
		}
		visibleItemIndex += child.getVisibleItemCount();		
	}	
	return visibleItemIndex;
}
/**
 * Answer the item at 'searchIndex' relativ to the receiver.
 * When this method is called for the root item, 'searchIndex' 
 * represents the global index into all items of the tree.
 * searchIndex=0 returns the receiver. 
 * searchIndex=1 returns the first visible child.
 * Note: searchIndex must be >= 0
 *
 * Note: 
 * Visible in this context does not neccessarily mean that the 
 * item is displayed on the screen. Visible here means that all 
 * the parents of the item are expanded. An item is only 
 * visible on screen if it is within the widget client area.
 */
TreeItem getVisibleItem(int searchIndex) {
	TreeItem child;
	TreeItem foundItem = null;
	Enumeration children = getChildren().elements();

	if (searchIndex == 0) {
		return this;
	}
	else					
	if (getExpanded() == false) { 		// trying to find a child when this item isn't expanded ? 
		return null;
	}

	// Search for expanded items first. Count all subitems in the process.
	while (children.hasMoreElements() == true && foundItem == null) {
		child = (TreeItem) children.nextElement();
		searchIndex--;
		if (child.getExpanded() == true) {
			searchIndex -= child.getVisibleItemCount();	// count children of all expanded items
		}
		if (searchIndex <= 0) {								// is searched item past child ?
			// add back children of current item (that's what we want to search)			
			foundItem = child.getVisibleItem(searchIndex + child.getVisibleItemCount());
		}
	}

	return foundItem;
}
/**
 * Answer whether 'item' is a child, direct or indirect, of the receiver.
 * It is an indirect child if it is a child of one of the receiver's children.
 */
boolean isChild(TreeItem item) {
	Vector children = getChildren();
	TreeItem child;
	
	if (children.contains(item) == true) {
		return true;
	}
	for (int i = 0; i < children.size(); i++) {
		child = (TreeItem) children.elementAt(i);
		if (child.isChild(item) == true) {
			return true;
		}
	}
	return false;
}
/**
 * Answer whether the receiver is a root item.
 * The receiver is a root item when it does not have a parent item.
 * @return 
 *	true - the receiver is a root item.
 * 	false - the receiver is not a root item.
 */
boolean isRoot() {
	return (getParentItem() == null);
}
/**
 * Answer whether the click at 'position' on the receiver is a selection 
 * click.
 * @param position - location of the mouse click relative to the 
 *	upper left corner of the receiver.
 * @return true - receiver was clicked.
 *	false - receiver was not clicked.
 */
boolean isSelectionHit(Point position) {
	Point itemExtent = getItemExtent();

	if (itemExtent == null) {		// neither image nor text have been set
		return false;
	}
	return (new Rectangle(
		getItemStartX() - getPaintStartX(), 0, 
		itemExtent.x, itemExtent.y)).contains(position);
}
/**
 * Answer whether the receiver is visible
 * An item is visible when its parent item is visible and 
 * expanded. Root items are always visible.
 *
 * Note: 
 * Visible in this context does not neccessarily mean that the 
 * item is displayed on the screen. Visible here means that all 
 * the parents of the item are expanded. An item is only 
 * visible on screen if it is within the receiver's parent's 
 * client area.
 * @return 
 *	true - the receiver is visible
 * 	false - the receiver is not visible
 */
boolean isVisible() {
	boolean isVisible = true;
	TreeItem parentItem = getParentItem();

	if (isRoot() == false) {
		isVisible = parentItem.getExpanded();
		if (isVisible == true) {
			isVisible = parentItem.isVisible();
		}
	}
	return isVisible;		
}
/**
 * Make this item visible by expanding its parent item.
 */
void makeVisible() {
	TreeItem parentItem = getParentItem();
	
	if (isVisible() == false && parentItem != null) {
		getParent().expand(parentItem, true);			// have to call Tree.expand directly in order to trigger Expand event
		parentItem.makeVisible();
	}
}
/** 
 * Draw the receiver at 'yPosition' in the client area of the parent.
 * @param gc - GC to draw on.
 * @param yPosition - y coordinate where the receiver should draw at.
 */
void paint(GC gc, int yPosition) {
	if (isVisible() == false) {
		return;
	}
	
	Tree parent = getParent();
	Point paintPosition = new Point(getPaintStartX(), yPosition);
	Point extent = getSelectionExtent();
	gc.setForeground(parent.CONNECTOR_LINE_COLOR);
	paintPosition = drawVerticalItemConnector(gc, paintPosition);
	paintPosition = drawHierarchyIndicator(gc, paintPosition);
	paintPosition = drawHorizontalItemConnector(gc, paintPosition);
	gc.setForeground(parent.getForeground());
	// paint the rest
	if (isCheckable() == true) {
		paintPosition = drawCheckbox(gc, new Point(paintPosition.x, yPosition));
	}
	paintPosition = drawImage(gc, new Point(paintPosition.x, yPosition));
	if (isSelected() == true) {
		gc.setBackground(getSelectionBackgroundColor());
		gc.setForeground(getSelectionForegroundColor());
		gc.fillRectangle(paintPosition.x, paintPosition.y, extent.x, extent.y);
	} else {
		gc.setBackground(getBackground());
		gc.setForeground(getForeground());
		if(getBackground() != parent.getBackground()){
			gc.fillRectangle(paintPosition.x, paintPosition.y, extent.x, extent.y);		
		}
	}	
	if (text != null) {		
		gc.drawString(text, getTextXPos(), paintPosition.y + getTextYPosition(gc), true);
	}
	if (this == parent.getInsertItem()) {
		drawInsertMark(gc, paintPosition);
	}
	drawSelectionFocus(gc, paintPosition);
}

void redraw(){
	Rectangle bounds = getBounds();
	getParent().redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
}

/**
 * Update the display to reflect the expanded state of the
 * receiver.
 * @param itemIndex - index position in the receiver's client 
 *	area where should be drawn.
 */
void redrawExpanded(int itemIndex) {
	Tree parent = getParent();
	int indicatorWidth = parent.getHierarchyIndicatorRect().width;
	int itemHeight = parent.getItemHeight();

	parent.redraw(
		getPaintStartX(), itemIndex * itemHeight,
		indicatorWidth, itemHeight, false);
}
/**
 * Reset cached size and position data.
 */
void reset() {
	super.reset();
	setImageExtent(null);
	setItemExtent(null);	
	setPaintStartX(-1);
	setTextYPosition(-1);	
}
/**
 * Sets the expanded state of the receiver.
 * <p>
 *
 * @param expanded the new expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpanded(boolean expand) {
	checkWidget();
	if (isLeaf() == false && expand == true) {
		getParent().expand(this, false);
	}
	else {
		getParent().collapse(this, false);
	}
}
public void setImage(Image newImage) {
	checkWidget();
	Tree parent = getParent();
	Image oldImage = getImage();
	boolean isSameImage;
	int imageWidth = 0;
	int redrawX = 0;

	super.setImage(newImage);	
	if (newImage != null && oldImage != null) {
		isSameImage = newImage.equals(oldImage);
	}
	else {
		isSameImage = newImage == oldImage;
	}
	if (isSameImage == false) {
		if (parent.getVisibleRedrawY(this) != -1) {
			if (parent.getImageExtent() != null) {
				imageWidth = parent.getImageExtent().x;
			}
			else
			if (newImage != null) {
				imageWidth = newImage.getBounds().x;
			}
			redrawX = getItemStartX();
		}
		parent.itemChanged(this, redrawX, imageWidth);
	}
}
/**
 * Set the size of the original image of the receiver to 'imageExtent'. 
 */
void setImageExtent(Point imageExtent) {
	this.imageExtent = imageExtent;
}
/**
 * Set the index of the receiver to 'index'.
 * This index is used to reference children in their parent.
 */
void setIndex(int index) {
	this.index = index;
}
/**
 * Set the size of the receiver to 'extent'.
 */
void setItemExtent(Point extent) {
	itemExtent = extent;
}
/**
 * Set the x position where the receiver is drawn to 'startX'.
 * @param startX - the x position where the receiver is drawn
 */
void setPaintStartX(int startX) {
	paintStartX = startX;
}
/**
 * Set the parent item of the receiver to 'parentItem'.
 * @param parentItem - the receiver's parent item. 
 *	Receiver is a root if this is null.
 */
void setParentItem(TreeItem parentItem) {
	this.parentItem = parentItem;
}
/**
 * This label will be displayed to the right of the bitmap, 
 * or, if the receiver doesn't have a bitmap to the right of 
 * the horizontal hierarchy connector line.
 */
public void setText(String newText) {
	checkWidget();
	Tree parent = getParent();	
	String oldText = getText();
	int redrawX = 0;
	int redrawWidth = 0;

	if (newText == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	super.setText(newText);	
	if (newText.equals(oldText) == false) {
		if (parent.getVisibleRedrawY(this) != -1) {
			redrawX = getTextXPos();
			redrawWidth = parent.getClientArea().width - redrawX;
		}
		parent.itemChanged(this, redrawX, redrawWidth);
	}
}
/**
 * Set the y position of the receiver's text to 'yPosition'.
 */
void setTextYPosition(int yPosition) {
	textYPosition = yPosition;
}

public void dispose() {
	if (isDisposed()) return;
	// if the tree is being disposed don't bother collapsing the item since all 
	// items in the tree will be deleted and redraws will not be processed anyway
	Tree parent = getParent();
	if (parent.isRemovingAll() == false) {
		parent.collapseNoRedraw(this);
	}	
	
	if (parentItem != null) {
		parentItem.removeItem(this);
	}
	else {
		parent.removeItem(this);
	}
	
	super.dispose();
}

void doDispose() {	
	// Notify the parent that the receiver is being removed.
	// Reset cached data.
	setParentItem(null);
	setImageExtent(null);
	setItemExtent(null);	
	setIndex(-1);
	setPaintStartX(-1);
	setTextYPosition(-1);
	
	super.doDispose();
}
/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the checked state
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
public Display getDisplay() {
	return super.getDisplay();
}
boolean getExpanding(){
	return isExpanding;
}
/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the grayed state
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
	if (color != null && color.isDisposed ())
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	background = color;	
	redraw();
}
/**
 * Sets the checked state of the receiver.
 * <p>
 *
 * @param checked the new checked state
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
void setExpanding(boolean expanding){
	isExpanding = expanding;
}
/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @since 2.0
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
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ())
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	foreground = color;
	redraw(); 
}
/**
 * Sets the grayed state of the receiver.
 * <p>
 *
 * @param checked the new grayed state
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

}

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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import java.util.Enumeration;
import java.util.Vector;
 
/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issue notificiation when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tree2 extends SelectableItemWidget {
	// These constants are used internally for item hit test on mouse click
	private static final int ActionNone = 0;			// The mouse event was not handled
	private static final int ActionExpandCollapse = 1;	// Do an expand/collapse
	private static final int ActionSelect = 2;			// Select the item
	private static final int ActionCheck = 3;			// Toggle checked state of the item
	private static ImageData CollapsedImageData;		// collapsed sub tree image data. used to create an image at run time
	private static ImageData ExpandedImageData;			// expanded sub tree image data. used to create an image at run time
	static {
		initializeImageData();
	}
	
	private TreeRoots root;
	private TreeItem2 expandingItem;
	
	private Image collapsedImage;
	private Image expandedImage;

	// The following fields are needed for painting tree items
	final Color CONNECTOR_LINE_COLOR;					// Color constant used during painting. Can't keep this in TreeItem 
														// because we only need one instance per tree widget/display and can't 
														// have it static. Initialized in c'tor and freed in dispose();
	Rectangle hierarchyIndicatorRect = null;			// bounding rectangle of the hierarchy indication image (plus/minus)

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#CHECK
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree2(Composite parent, int style) {
	super(parent, checkStyle (style));
	CONNECTOR_LINE_COLOR = new Color(display, 170, 170, 170);	// Light gray;
}
/**
 * Add 'item' to the list of root items.
 * @param 'item' - the tree item that should be added as a root.
 * @param index - position that 'item' will be inserted at
 *	in the receiver.
 */
void addItem(TreeItem2 item, int index) {
	if (index < 0 || index > getItemCount()) {
		error(SWT.ERROR_INVALID_RANGE);
	}
	getRoot().add(item, index);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the reciever has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	TypedListener typedListener;

	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	typedListener = new TypedListener(listener);	
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>TreeListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see TreeListener
 * @see #removeTreeListener
 */
public void addTreeListener(TreeListener listener) {
	checkWidget();
	TypedListener typedListener;

	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	typedListener = new TypedListener(listener);	
	addListener(SWT.Expand, typedListener);
	addListener(SWT.Collapse, typedListener);
}
/**
 * The SelectableItem 'item' has been added to the tree.
 * Prevent screen updates when 'item' is inserted due to an 
 * expand operation.
 * @param item - item that has been added to the receiver.
 */
void addedItem(SelectableItem item, int index) {
	super.addedItem(item, index);				
	redrawAfterModify(item, index);		// redraw plus/minus image, hierarchy lines
}
/**
 * Answer the y position of both the first child of 'item' and 
 * the item following the last child of 'item'.
 * Used to scroll items on expand/collapse.
 * @param item - TreeItem to use for calculating the y boundary 
 *	of child items.
 * @return Array - first element is the position of the first 
 *	child of 'item'. Second element is the position of the item 
 *	following the last child of 'item'.
 *	Both elements are -1 if 'item' is not a child of the receiver.
 */
int[] calculateChildrenYPos(TreeItem2 item) {
	int itemIndex = item.getVisibleIndex();
	int itemCount = item.getVisibleItemCount();
	int itemHeight = getItemHeight();
	int yPos;
	int[] yPosition = new int[] {-1, -1};

	if (itemIndex != -1) {
		itemIndex -= getTopIndex();															
		yPos = (itemIndex + itemCount + 1) * itemHeight;	// y position of the item following 
															// the last child of 'item'
		yPosition = new int[] {yPos - (itemCount * itemHeight), yPos};
	}
	return yPosition;
}
/**
 * Calculate the widest of the children of 'item'.
 * Items that are off screen and that may be scrolled into view are 
 * included in the calculation.
 * @param item - the tree item that was expanded
 */
void calculateWidestExpandingItem(TreeItem2 item) {
	int itemIndex = item.getVisibleIndex();
	int newMaximumItemWidth = getContentWidth();
	int stopIndex = itemIndex + item.getVisibleItemCount();

	for (int i = itemIndex + 1; i <= stopIndex; i++) {
		newMaximumItemWidth = Math.max(newMaximumItemWidth, getContentWidth(i));
	}
	setContentWidth(newMaximumItemWidth);
}
/**
 * Calculate the width of new items as they are scrolled into view.
 * Precondition: 
 * topIndex has already been set to the new index.
 * @param topIndexDifference - difference between old and new top 
 *	index.
 */
void calculateWidestScrolledItem(int topIndexDifference) {
	int visibleItemCount = getItemCountTruncated(getClientArea());	
	int newMaximumItemWidth = getContentWidth();
	int topIndex = getTopIndex();
	int stopIndex = topIndex;

	if (topIndexDifference < 0) {								// scrolled up?
		if (Math.abs(topIndexDifference) > visibleItemCount) {	// scrolled down more than one page (via quick thumb dragging)?
			topIndexDifference = visibleItemCount * -1;
		}
		for (int i = stopIndex - topIndexDifference; i >= stopIndex; i--) {	// check item width from old top index up to new one
			newMaximumItemWidth = Math.max(newMaximumItemWidth, getContentWidth(i));
		}
	}
	else
	if (topIndexDifference > 0) {								// scrolled down?
		if (topIndexDifference > visibleItemCount) {			// scrolled down more than one page (via quick thumb dragging)?
			topIndexDifference = visibleItemCount;
		}
		stopIndex += visibleItemCount;		
		for (int i = stopIndex - topIndexDifference; i < stopIndex; i++) {
			newMaximumItemWidth = Math.max(newMaximumItemWidth, getContentWidth(i));
		}
	}
	setContentWidth(newMaximumItemWidth);
}
/**
 * Calculate the maximum item width of all displayed items.
 */
void calculateWidestShowingItem() {
	TreeItem2 visibleItem;
	int newMaximumItemWidth = 0;
	int bottomIndex = getBottomIndex();
	int paintStopX;

	// add one to the loop end index because otherwise an item covered 
	// by the horizontal scroll bar would not be taken into acount and 
	// may become visible after this calculation. We're in trouble if
	// that item is wider than the client area.
	if (getHorizontalBar().getVisible() == true) {
		bottomIndex++;
	}
	for (int i = getTopIndex(); i < bottomIndex; i++) {
		visibleItem = getRoot().getVisibleItem(i);
		if (visibleItem != null) {
			paintStopX = visibleItem.getPaintStopX();
			newMaximumItemWidth = Math.max(newMaximumItemWidth, paintStopX);
		}
	}
	setContentWidth(newMaximumItemWidth);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
/**
 * Collapse the tree item identified by 'item' if it is not 
 * already collapsed. Move the selection to the parent item 
 * if one of the collapsed items is currently selected.
 * @param item - item that should be collapsed.
 * @param notifyListeners - 
 *	true=a Collapse event is sent 
 *	false=no event is sent
 */
void collapse(TreeItem2 item, boolean notifyListeners) {
	Event event;
	int itemIndex;
	
	if (item.getExpanded() == false) {
		return;
	}
	if (notifyListeners == true) {
		event = new Event();
		event.item = item;
		notifyListeners(SWT.Collapse, event);
		if (isDisposed()) return;
	}
	collapseNoRedraw(item);
	itemIndex = item.getVisibleIndex();
	if (itemIndex != -1) {						// if the item's parent is not collapsed (and the item is thus visible) do the screen updates
		item.redrawExpanded(itemIndex - getTopIndex());
		showSelectableItem(item);
		calculateVerticalScrollbar();
		calculateWidestShowingItem();
		claimRightFreeSpace();
		claimBottomFreeSpace();		
	}
}

/**
 * Collapse the tree item identified by 'item' if it is not 
 * already collapsed. Move the selection to the parent item 
 * if one of the collapsed items is currently selected.
 * This method is used to hide the children if an item is deleted.
 * certain redraw and scroll operations are not needed for this 
 * case.
 * @param item - item that should be collapsed.
 */
void collapseNoRedraw(TreeItem2 item) {
	
	if (item.getExpanded() == false) {
		return;
	}
	if (isSelectedItemCollapsing(item) == true) {
		deselectAllExcept(item);
		selectNotify(item);
		update();								// call update to make sure that new selection is 
												// drawn before items are collapsed (looks better)
	}
	scrollForCollapse(item);
	item.internalSetExpanded(false);
}

public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	Point size = super.computeSize(wHint, hHint, changed);
	GC gc;
	final int WidthCalculationCount = 50;		// calculate item width for the first couple of items only
	TreeRoots root = getRoot();
	TreeItem2 item;
	Image itemImage;
	String itemText;
	int width;
	int newItemWidth = 0;
		
	if (wHint == SWT.DEFAULT && getContentWidth() == 0 && getItemCount() > 0) {
		gc = new GC(this);
		for (int i = 0; i < WidthCalculationCount; i++) {
			item = root.getVisibleItem(i);
			if (item == null) {
				break;											// no more items
			}
			itemImage = item.getImage();
			itemText = item.getText();
			width = 0;
			if (itemImage != null) {
				width += itemImage.getBounds().width;
			}
			if (itemText != null) {
				gc.setFont(item.getFont());
				width += gc.stringExtent(itemText).x;
			}
			newItemWidth = Math.max(newItemWidth, width);
		}
		if (newItemWidth > 0) {
			size.x = newItemWidth;
		}		
		gc.dispose();
	}
	return size;
}
/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll() {
	checkWidget();
	getRoot().deselectAll();
	getSelectionVector().removeAllElements();
	redraw();
}
/**
 * Modifier Key		Action
 * None				Collapse the selected item if expanded. Select 
 * 					parent item if selected item is already 
 * 					collapsed and if it's not the root item.
 * Ctrl				super.doArrowLeft(int);
 * Shift			see None above
 * @param keyMask - the modifier key that was pressed
 */
void doArrowLeft(int keyMask) {
	TreeItem2 focusItem = (TreeItem2) getLastFocus();
	TreeItem2 parentItem;

	if (focusItem == null) {
		return;
	}
	if (keyMask == SWT.MOD1) {
		super.doArrowLeft(keyMask);
	}
	else
	if (focusItem.getExpanded() == true) {			// collapse if expanded
		collapse(focusItem, true);
	}
	else
	if (focusItem.isRoot() == false) {				// go to the parent if there is one
		parentItem = focusItem.getParentItem();
		deselectAllExcept(parentItem);
		selectNotify(parentItem);
	}
}
/**
 * Modifier Key		Action
 * None				Expand selected item if collapsed. Select 
 * 					first child item if selected item is 
 *					already expanded and there is a child item.
 * Ctrl				super.doArrowRight(keyMask);
 * Shift			see None above
 * @param keyMask - the modifier key that was pressed
 */
void doArrowRight(int keyMask) {
	TreeItem2 focusItem = (TreeItem2) getLastFocus();
	TreeItem2 childItem;

	if (focusItem == null) {
		return;
	}	
	if (keyMask == SWT.MOD1) {
		super.doArrowRight(keyMask);
	}
	else
	if (focusItem.isLeaf() == false) {
		if (focusItem.getExpanded() == false) {			// expand if collapsed
			expand(focusItem, true);
		} 
		else {											// go to the first child if there is one
			childItem = focusItem.getItems()[0];
			deselectAllExcept(childItem);
			selectNotify(childItem);
		}
	}
}
/**
 * Expand the selected item and all of its children.
 */
void doAsterisk() {
	expandAll((TreeItem2) getLastFocus());
}
/**
 * Free resources.
 */
void doDispose() {
	super.doDispose();	
	if (collapsedImage != null) {
		collapsedImage.dispose();
	}
	if (expandedImage != null) {
		expandedImage.dispose();
	}
	getRoot().dispose();
	CONNECTOR_LINE_COLOR.dispose();
	resetHierarchyIndicatorRect();
}
/**
 * Collapse the selected item if it is expanded.
 */
void doMinus() {
	TreeItem2 selectedItem = (TreeItem2) getLastFocus();

	if (selectedItem != null) {
		collapse(selectedItem, true);
	}
}
/**
 * Expand the selected item if it is collapsed and if it 
 * has children.
 */
void doPlus() {
	TreeItem2 selectedItem = (TreeItem2) getLastFocus();

	if (selectedItem != null && selectedItem.isLeaf() == false) {
		expand(selectedItem, true);
	}
}
/**
 * Expand the tree item identified by 'item' if it is not already 
 * expanded. Scroll the expanded items into view.
 * @param item - item that should be expanded
 * @param notifyListeners - 
 *	true=an Expand event is sent 
 *	false=no event is sent
 */
void expand(TreeItem2 item, boolean notifyListeners) {
	Event event = new Event();
	boolean nestedExpand = expandingItem != null;

	if (item.getExpanded() == true || item.getExpanding() == true) {
		return;
	}
	item.setExpanding(true);
	if (nestedExpand == false) {
		setExpandingItem(item);
	}
	if (notifyListeners == true) {
		event.item = item;
		notifyListeners(SWT.Expand, event);
	}
	scrollForExpand(item);
	item.internalSetExpanded(true);
	// redraw hierarchy image
	item.redrawExpanded(item.getVisibleIndex() - getTopIndex());
	calculateVerticalScrollbar();
	if (nestedExpand == false && isVisible() == true) {
		showSelectableItem(item);	// make expanded item visible. Could be invisible if the expand was caused by a key press.		
		calculateWidestExpandingItem(item);
		scrollExpandedItemsIntoView(item);
	}
	if (nestedExpand == false) {
		setExpandingItem(null);
	}
	item.setExpanding(false);
}
/**
 * Expand 'item' and all its children.
 */
void expandAll(TreeItem2 item) {
	TreeItem2 items[];

	if (item != null && item.isLeaf() == false) {
		expand(item, true);
		update();
		items = item.getItems(); 
		for (int i = 0; i < items.length; i++) {
			expandAll(items[i]);
		}
	}
}
/**
 * Answer the image that is used as a hierarchy indicator 
 * for a collapsed hierarchy.
 */
Image getCollapsedImage() {
	if (collapsedImage == null) {
		collapsedImage = new Image(display, CollapsedImageData);
	}
	return collapsedImage;
}
/**
 * Answer the width of the item identified by 'itemIndex'.
 */
int getContentWidth(int itemIndex) {
	TreeItem2 item = getRoot().getVisibleItem(itemIndex);
	int paintStopX = 0;

	if (item != null) {
		paintStopX = item.getPaintStopX();
	}
	return paintStopX;
}
/**
 * Answer the image that is used as a hierarchy indicator 
 * for an expanded hierarchy.
 */
Image getExpandedImage() {
	if (expandedImage == null) {
		expandedImage = new Image(display, ExpandedImageData);
	}
	return expandedImage;
}
/**
 * Answer the rectangle enclosing the hierarchy indicator of a tree item.
 * 
 * Note:
 * Assumes that the hierarchy indicators for expanded and 
 * collapsed state are the same size.
 * @return
 *	The rectangle enclosing the hierarchy indicator.
 */
Rectangle getHierarchyIndicatorRect() {
	int itemHeight = getItemHeight();
	Image hierarchyImage;
	Rectangle imageBounds;
	
	if (hierarchyIndicatorRect == null && itemHeight != -1) {
		hierarchyImage = getCollapsedImage();
		if (hierarchyImage != null) {
		 	imageBounds = hierarchyImage.getBounds();
		}
		else {
			imageBounds = new Rectangle(0, 0, 0, 0);
		}
		hierarchyIndicatorRect = new Rectangle(
			0,
			(itemHeight - imageBounds.height) / 2 + (itemHeight - imageBounds.height) % 2,
			imageBounds.width,
			imageBounds.height);
	}
	return hierarchyIndicatorRect;
}
/**
 * Answer the index of 'item' in the receiver.
 */
int getIndex(SelectableItem item) {
	int index = -1;

	if (item != null) {
		index = ((TreeItem2) item).getGlobalIndex();
	}
	return index;
}
/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  The
 * number that is returned is the number of roots in the
 * tree.
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
	return getRoot().getItemCount();
}
/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight() {
	checkWidget();
	return super.getItemHeight();
}
/**
 * Returns the items contained in the receiver
 * that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem2 [] getItems() {
	checkWidget();
	TreeItem2 childrenArray[] = new TreeItem2[getItemCount()];

	getRoot().getChildren().copyInto(childrenArray);
	return childrenArray;	
}
/**
 * Answer the number of sub items of 'item' that do not fit in the 
 * tree client area.
 */
int getOffScreenItemCount(TreeItem2 item) {
	int itemIndexFromTop = item.getVisibleIndex() - getTopIndex();
	int spaceRemaining = getItemCountWhole()-(itemIndexFromTop+1);
	int expandedItemCount = item.getVisibleItemCount();

	return expandedItemCount - spaceRemaining;	
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
public TreeItem2 getParentItem() {
	checkWidget();
	return null;
}
/**
 * Answer the object that holds the root items of the receiver.
 */
TreeRoots getRoot() {
	return root;
}
/**
 * Returns an array of <code>TreeItem</code>s that are currently
 * selected in the receiver. An empty array indicates that no
 * items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver. 
 * </p>
 * @return an array representing the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem2 [] getSelection() {
	checkWidget();
	Vector selectionVector = getSelectionVector();
	TreeItem2[] selectionArray = new TreeItem2[selectionVector.size()];

	selectionVector.copyInto(selectionArray);
	sort(selectionArray, 0, selectionArray.length);
	return selectionArray;
}
/**
 * Answer the index of 'item' in the receiver.
 * Answer -1 if the item is not visible.
 * The returned index must refer to a visible item.
 * Note: 
 * 	Visible in this context does not neccessarily mean that the 
 * 	item is displayed on the screen. It only means that the item 
 * 	would be displayed if it is located inside the receiver's 
 * 	client area.
 *	Collapsed items are not visible.
 */
int getVisibleIndex(SelectableItem item) {
	int index = -1;

	if (item != null) {
		index = ((AbstractTreeItem) item).getVisibleIndex();
	}
	return index;
}
/**
 * Answer the SelectableItem located at 'itemIndex' 
 * in the receiver.
 * @param itemIndex - location of the SelectableItem 
 *	object to return
 */
SelectableItem getVisibleItem(int itemIndex) {
	return getRoot().getVisibleItem(itemIndex);
}
/**
 * Answer the number of visible items of the receiver.
 * Note: 
 * 	Visible in this context does not neccessarily mean that the 
 * 	item is displayed on the screen. It only means that the item 
 * 	would be displayed if it is located inside the receiver's 
 * 	client area.
 *	Collapsed items are not visible.
 */
int getVisibleItemCount() {
	return getRoot().getVisibleItemCount();
}
/**
 * Answer the y coordinate at which 'item' is drawn. 
 * @param item - SelectableItem for which the paint position 
 *	should be returned
 * @return the y coordinate at which 'item' is drawn.
 *	Return -1 if 'item' is null or outside the client area
 */
int getVisibleRedrawY(SelectableItem item) {
	int redrawY = getRedrawY(item);
	
	if (redrawY < 0 || redrawY > getClientArea().height) {
		redrawY = -1;
	}
	return redrawY;
}
/**
 * Handle the events the receiver is listening to.
 */
void handleEvents(Event event) {
	switch (event.type) {
		case SWT.Paint:
			paint(event);
			break;
		case SWT.MouseDown:
			mouseDown(event);
			break;
		case SWT.MouseDoubleClick:
			mouseDoubleClick(event);
			break;
		default:
			super.handleEvents(event);
	}	
}
/**
 * Initialize the receiver.
 */
void initialize() {
	resetRoot();					// has to be at very top because super class uses 
									// functionality that relies on the TreeRoots object
	super.initialize();
}
/**
 * Initialize the ImageData used for the expanded/collapsed images.
 */
static void initializeImageData() {
	PaletteData fourBit = new PaletteData(
		new RGB[] {new RGB(0, 0, 0), new RGB (128, 0, 0), new RGB (0, 128, 0), new RGB (128, 128, 0), new RGB (0, 0, 128), new RGB (128, 0, 128), new RGB (0, 128, 128), new RGB (128, 128, 128), new RGB (192, 192, 192), new RGB (255, 0, 0), new RGB (0, 255, 0), new RGB (255, 255, 0), new RGB (0, 0, 255), new RGB (255, 0, 255), new RGB (0, 255, 255), new RGB (255, 255, 255)});
	
	CollapsedImageData = new ImageData(
		9, 9, 4, 										// width, height, depth
		fourBit, 4,
		new byte[] {119, 119, 119, 119, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0, 127, 0, 0, 15, 112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0, 127, -1, 15, -1, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 119, 119, 119, 119, 112, 0, 0, 0});
	CollapsedImageData.transparentPixel = 15;			// use white for transparency
	ExpandedImageData = new ImageData(
		9, 9, 4, 										// width, height, depth
		fourBit, 4,
		new byte[] {119, 119, 119, 119, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 127, 0, 0, 15, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 127, -1, -1, -1, 112, 0, 0, 0, 119, 119, 119, 119, 112, 0, 0, 0});
	ExpandedImageData.transparentPixel = 15;			// use white for transparency
}
/**
 * Set event listeners for the receiver.
 */
void installListeners() {
	Listener listener = getListener();

	super.installListeners();
	addListener(SWT.Paint, listener);
	addListener(SWT.MouseDown, listener);
	addListener(SWT.MouseDoubleClick, listener);
}
/**
 * Answer whether the receiver is currently expanding a sub tree 
 * with 'item' in it.
 * Used for performance optimizations.
 */
boolean isExpandingItem(SelectableItem item) {
	TreeItem2 parentItem;
	
	if (expandingItem == null || item == null || (item instanceof TreeItem2) == false) {
		return false;
	}
	parentItem = ((TreeItem2) item).getParentItem();
	return (parentItem == expandingItem || isExpandingItem(parentItem));
}
/**
 * Answer whether the children of 'collapsingItem' contain 
 * at least one selected item.
 */
boolean isSelectedItemCollapsing(TreeItem2 collapsingItem) {
	Enumeration selection = getSelectionVector().elements();
	TreeItem2 item;
	int selectedItemIndex;
	int collapsingItemIndex = collapsingItem.getVisibleIndex();
	int lastCollapsedItemIndex = collapsingItemIndex + collapsingItem.getVisibleItemCount();

	if (collapsingItemIndex == -1) {					// is the collapsing item in a collapsed subtree?
		return false;									// then neither it nor its children are selected
	}
	while (selection.hasMoreElements() == true) {
		item = (TreeItem2) selection.nextElement();
		selectedItemIndex = item.getVisibleIndex();
		if ((selectedItemIndex > collapsingItemIndex) &&
			(selectedItemIndex <= lastCollapsedItemIndex)) {
			return true;
		}
	}
	return false;
}
/**
 * Test whether the mouse click specified by 'event' was a 
 * valid selection or expand/collapse click.
 * @return 
 *  One of ActionExpandCollapse, ActionSelect, ActionNone, ActionCheck
 *	specifying the action to be taken on the click.
 */
int itemAction(TreeItem2 item, int x, int y) {
	int action = ActionNone;
	int itemHeight = getItemHeight();
	int offsetX;
	int offsetY;
	Point offsetPoint;

	if (item != null) {
		offsetX = x - item.getPaintStartX();
		offsetY = y - itemHeight * (y / itemHeight);	
		offsetPoint = new Point(offsetX, offsetY);	
		if ((item.isLeaf() == false) &&
			(getHierarchyIndicatorRect().contains(offsetPoint) == true)) {
			action |= ActionExpandCollapse;
		}
		else
		if (item.isSelectionHit(offsetPoint) == true) {
			action |= ActionSelect;
		}
		else
		if (item.isCheckHit(new Point(x, y)) == true) {
			action |= ActionCheck;
		}
	}
	return action;
}
/**
 * The table item 'changedItem' has changed. Redraw the whole 
 * item in that column. Include the text in the redraw because 
 * an image set to null requires a redraw of the whole item anyway. 
 */
void itemChanged(SelectableItem changedItem, int repaintStartX, int repaintWidth) {
	int oldItemHeight = getItemHeight();	
	Point oldImageExtent = getImageExtent();
	
	if (isExpandingItem(changedItem) == false) {
		super.itemChanged(changedItem, repaintStartX, repaintWidth);
	}
	else {
		calculateItemHeight(changedItem);
	}
	if ((oldItemHeight != getItemHeight()) ||			// only reset items if the item height or
		(oldImageExtent != getImageExtent())) {			// image size has changed. The latter will only change once, 
														// from null to a value-so it's safe to test using !=
		getRoot().reset();								// reset cached data of all items in the receiver
		resetHierarchyIndicatorRect();
		redraw();										// redraw all items if the image extent has changed. Fixes 1FRIHPZ		
	}
	else {
		((AbstractTreeItem) changedItem).reset();		// reset the item that has changed when the tree item 
														// height has not changed (otherwise the item caches old data)
														// Fixes 1FF6B42
	}
	if (repaintWidth != 0) {
		calculateWidestShowingItem();
		claimRightFreeSpace();								// otherwise scroll bar may be reset, but not horizontal offset
															// Fixes 1G4SBJ3
	}
}
/**
 * A key was pressed.
 * Call the appropriate key handler method.
 * @param event - the key event
 */
void keyDown(Event event) {
	super.keyDown(event);
	switch (event.character) {
		case '+':
			doPlus();
			break;
		case '-':
			doMinus();
			break;
		case '*':
			doAsterisk();
			break;
	}
}

/**
 * A mouse double clicked occurred over the receiver.
 * Expand/collapse the clicked item. Do nothing if no item was clicked.
 */
void mouseDoubleClick(Event event) {
	int hitItemIndex = event.y / getItemHeight();
	TreeItem2 hitItem = getRoot().getVisibleItem(hitItemIndex + getTopIndex());
	Event newEvent;
	
	if (hitItem == null || getIgnoreDoubleClick() || itemAction(hitItem, event.x, event.y) != ActionSelect) {
		return;
	}
	if (isListening(SWT.DefaultSelection) == true) {
		newEvent = new Event();
		newEvent.item = hitItem;
		postEvent(SWT.DefaultSelection, newEvent);
	}
	else
	if (hitItem.isLeaf() == false) {		// item with children was hit. Default behavior is expand/collapse item
		if (hitItem.getExpanded() == true) {
			collapse(hitItem, true);
		}
		else {
			expand(hitItem, true);
		}
	}
}
/**
 * The mouse pointer was pressed down on the receiver.
 * Handle the event according to the position of the mouse click.
 */
void mouseDown(Event event) {
	int hitItemIndex;
	TreeItem2 hitItem;
	SelectableItem selectionItem = getLastSelection();
	int itemAction;

	hitItemIndex = event.y / getItemHeight();
	hitItem = getRoot().getVisibleItem(hitItemIndex + getTopIndex());
	if (hitItem == null) {
		return;
	}
	if (!isFocusControl()) forceFocus();
	switch (itemAction = itemAction(hitItem, event.x, event.y)) {
		case ActionExpandCollapse:
			if (event.button != 1) return;
			if (hitItem.getExpanded() == true) {
				collapse(hitItem, true);
			}
			else {
				expand(hitItem, true);
			}
			break;
		case ActionSelect:
			doMouseSelect(hitItem, hitItemIndex + getTopIndex(), event.stateMask, event.button);
			break;
		case ActionCheck:
			if (event.button != 1) return;
			doCheckItem(hitItem);
			break;
	}
	if (itemAction != ActionSelect && selectionItem == null) {
		selectionItem = getRoot().getVisibleItem(getTopIndex());	// select the top item if no item was selected before
		selectNotify(selectionItem);								
	}
}
/**
 * A paint event has occurred. Display the invalidated items.
 * @param event - expose event specifying the invalidated area.
 */
void paint(Event event) {
	int visibleRange[] = getIndexRange(event.getBounds());
	
	paintItems(event.gc, visibleRange[0], visibleRange[1] + 1); // + 1 to paint the vertical line 
																// connection the last item we really 
																// want to paint with the item after that.
}
/**
 * Paint tree items on 'gc' starting at index 'topPaintIndex' and 
 * stopping at 'bottomPaintIndex'.
 * @param gc - GC to draw tree items on.
 * @param topPaintIndex - index of the first item to draw
 * @param bottomPaintIndex - index of the last item to draw 
 */
void paintItems(GC gc, int topPaintIndex, int bottomPaintIndex) {
	TreeItem2 visibleItem;
	int itemHeight = getItemHeight();

	for (int i = topPaintIndex; i <= bottomPaintIndex; i++) {
		visibleItem = getRoot().getVisibleItem(i + getTopIndex());
		if (visibleItem != null) {
			visibleItem.paint(gc, i * itemHeight);
		}
	}
}
/**
 * 'item' has been added to or removed from the receiver. 
 * Repaint part of the tree to update the vertical hierarchy 
 * connectors and hierarchy image.
 * @param modifiedItem - the added/removed item 
 * @param modifiedIndex - index of the added/removed item
 */
void redrawAfterModify(SelectableItem modifiedItem, int modifiedIndex) {
	int redrawStartY;
	int redrawStopY;
	int itemChildIndex = ((TreeItem2) modifiedItem).getIndex();
	int topIndex = getTopIndex();
	int itemHeight = getItemHeight();
	int redrawItemIndex;
	int itemCount;
	AbstractTreeItem parentItem = ((TreeItem2) modifiedItem).getParentItem();
	AbstractTreeItem redrawItem = null;

	if (redrawParentItem(modifiedItem) == false) {
		return;
	}
	if (parentItem == null) {							// a root item is added/removed
		parentItem = getRoot();
	}
	itemCount = parentItem.getItemCount();
	// redraw hierarchy decorations of preceeding item if the last item at a tree 
	// level was added/removed
	// otherwise, if the first item was removed, redraw the parent to update hierarchy icon
	if (itemChildIndex > 0) {							// more than one item left at this tree level
		// added/removed last item at this tree level? have to test >=.
		// when removing last item, item index is outside itemCount 
		if (itemChildIndex >= itemCount - 1) { 
			redrawItem = (AbstractTreeItem) parentItem.getChildren().elementAt(itemChildIndex - 1);
		}
	}
	else 
	if (getVisibleItemCount() > 0 && itemCount < 2) {	// last item at this level removed/first item added?
		redrawItem = parentItem;						// redraw parent item to update hierarchy icon
	}
	if (redrawItem != null) {
		redrawItemIndex = redrawItem.getVisibleIndex();
		if (modifiedIndex == -1) {
			modifiedIndex = redrawItemIndex + 1;
		}
		redrawStartY = (redrawItemIndex - topIndex) * itemHeight;
		redrawStopY = (modifiedIndex - topIndex) * itemHeight;
		redraw(
			0, 
			redrawStartY, 
			redrawItem.getCheckboxXPosition(), 			// only redraw up to and including hierarchy icon to avoid flashing
			redrawStopY - redrawStartY, false);
	}	
	if (modifiedIndex == 0) {											// added/removed first item ?
		redraw(0, 0, getClientArea().width, getItemHeight() * 2, false);// redraw new first two items to 
																		// fix vertical hierarchy line
	}
}

/**
 * Determine if part of the tree hierarchy needs to be redrawn.
 * The hierarchy icon of the parent item of 'item' needs to be redrawn if 
 * 'item' is added as the first child or removed as the last child.
 * Hierarchy lines need to be redrawn if 'item' is the last in a series of 
 * children.
 * @param item - tree item that is added or removed.
 * @return true=tree hierarchy needs to be redrawn. false=no redraw necessary
 */
boolean redrawParentItem(SelectableItem item) {
	TreeItem2 parentItem = ((TreeItem2) item).getParentItem();
	TreeItem2 parentItem2; 
	boolean redraw = false;

	// determine if only the hierarchy icon needs to be redrawn
	if (parentItem != null) {
		parentItem2 = parentItem.getParentItem();
		if ((parentItem2 == null || parentItem2.getExpanded() == true) && parentItem.getChildren().size() < 2) {
			redraw = true;
		}
	}
	// redraw is only neccessary when the receiver is not currently	
	// expanding 'item' or a parent item or if the parent item is expanded 
	// or if the hierarchy icon of the parent item needs to be redrawn
	if (isExpandingItem(item) == false && parentItem == null || parentItem.getExpanded() == true || redraw == true) {
		redraw = true;
	}
	else {
		redraw = false;
	}
	return redraw;
}

/**
 * Removes all of the items from the receiver.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll() {
	checkWidget();
	setRedraw(false);
	getRoot().dispose();
	resetRoot();
	reset();
	calculateWidestShowingItem();
	calculateVerticalScrollbar();
	setRedraw(true);	
}
/** 
 * Remove 'item' from the receiver. 
 * @param item - tree item that should be removed from the 
 *	receiver-must be a root item.
 */
void removeItem(TreeItem2 item) {
	getRoot().removeItem(item);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's selection changes.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}	
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);	
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed..
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see TreeListener
 * @see #addTreeListener
 */
public void removeTreeListener(TreeListener listener) {
	checkWidget();
	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener (SWT.Expand, listener);
	removeListener (SWT.Collapse, listener);
}
/**
 * 'item' has been removed from the receiver. 
 * Recalculate the content width.
 */
void removedItem(SelectableItem item) {
	if (isExpandingItem(item) == false) {
		super.removedItem(item);				
	}	
	calculateWidestShowingItem();
	claimRightFreeSpace();
}
/**
 * Notification that 'item' is about to be removed from the tree.
 * Update the item selection if neccessary.
 * @param item - item that is about to be removed from the tree.
 */
void removingItem(SelectableItem item) {
	Vector selection = getSelectionVector();
	TreeItem2 parentItem = ((TreeItem2) item).getParentItem();
	TreeItem2 newSelectionItem = null;
	boolean isLastSelected = (selection.size() == 1) && (selection.elementAt(0) == item);
	int itemIndex = getVisibleIndex(item);
	
	if (isLastSelected == true) {
		// try selecting the following item
		newSelectionItem = (TreeItem2) getVisibleItem(itemIndex + 1);
		if (newSelectionItem == null || newSelectionItem.getParentItem() != parentItem) {
			// select parent item if there is no item following the removed  
			// one on the same tree level
			newSelectionItem = parentItem;
		}
		if (newSelectionItem != null) {
			selectNotify(newSelectionItem, true);
		}
	}
	super.removingItem(item);
	if (isExpandingItem(item) == false) {
		// redraw plus/minus image, hierarchy lines,
		// redrawing here assumes that no update happens between now and 
		// after the item has actually been removed. Otherwise this call 
		// would need to be in removedItem and we would need to store the
		// "itemIndex" here to redraw correctly.
		redrawAfterModify(item, itemIndex);
	}	
}
/**
 * Reset the rectangle enclosing the hierarchy indicator to null.
 * Forces a recalculation next time getHierarchyIndicatorRect is called.
 */
void resetHierarchyIndicatorRect() {
	hierarchyIndicatorRect = null;
}
/**
 * Reset state that is dependent on or calculated from the items
 * of the receiver.
 */
void resetItemData() {
	setContentWidth(0);
	resetHierarchyIndicatorRect();	
	super.resetItemData();	
}
/**
 * Reset the object holding the root items of the receiver.
 */
void resetRoot() {
	root = new TreeRoots(this);
}
/**
 * The receiver has been resized. Recalculate the content width.
 */
void resize(Event event) {
	int oldItemCount = getVerticalBar().getPageIncrement();

	super.resize(event);
	if (getItemCountWhole() > oldItemCount) {		// window resized higher?
		calculateWidestShowingItem();				// recalculate widest item since a longer item may be visible now
	}
}
/**
 * Display as many expanded tree items as possible.
 * Scroll the last expanded child to the bottom if all expanded 
 * children can be displayed.
 * Otherwise scroll the expanded item to the top.
 * @param item - the tree item that was expanded
 */
void scrollExpandedItemsIntoView(TreeItem2 item) {
	int itemCountOffScreen = getOffScreenItemCount(item);
	int newTopIndex = getTopIndex() + itemCountOffScreen;

	if (itemCountOffScreen > 0) {
		newTopIndex = Math.min(item.getVisibleIndex(), newTopIndex);	// make sure the expanded item is never scrolled out of view
		setTopIndex(newTopIndex, true);								
	}
}
/**
 * Scroll the items following the children of 'collapsedItem'
 * below 'collapsedItem' to cover the collapsed children.
 * @param collapsedItem - item that has been collapsed
 */
void scrollForCollapse(TreeItem2 collapsedItem) {
	Rectangle clientArea = getClientArea();	
	int topIndex = getTopIndex();
	int itemCount = collapsedItem.getVisibleItemCount();
	int scrollYPositions[] = calculateChildrenYPos(collapsedItem);

	if (scrollYPositions[0] == -1 && scrollYPositions[1] == -1) {
		return;
	}
	if (topIndex + getItemCountWhole() == getVisibleItemCount() && itemCount < topIndex) {
		// scroll from top if last item is at bottom and will stay at 
		// bottom after collapse. Avoids flash caused by too much bit 
		// blitting (which force update and thus premature redraw)
		int height = scrollYPositions[1] - scrollYPositions[0];
		scroll(
			0, 0,					// destination x, y
			0, -height,				// source x, y		
			clientArea.width, scrollYPositions[0]+height, true);
		setTopIndexNoScroll(topIndex - itemCount, true);
	}	
	else {
		scroll(
			0, scrollYPositions[0],				// destination x, y
			0, scrollYPositions[1],				// source x, y		
			clientArea.width, clientArea.height - scrollYPositions[0], true);
	}
}
/**
 * Scroll the items following 'expandedItem' down to make 
 * space for the children of 'expandedItem'.
 * @param expandedItem - item that has been expanded.
 */
void scrollForExpand(TreeItem2 expandedItem) {
	int scrollYPositions[];
	Rectangle clientArea = getClientArea();

	expandedItem.internalSetExpanded(true);		
	scrollYPositions = calculateChildrenYPos(expandedItem);	
	expandedItem.internalSetExpanded(false);	
	if (scrollYPositions[0] == -1 && scrollYPositions[1] == -1) {
		return;
	}	
	scroll(
		0, scrollYPositions[1],				// destination x, y
		0, scrollYPositions[0],				// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Scroll horizontally by 'numPixel' pixel.
 * @param numPixel - the number of pixel to scroll
 *	< 0 = columns are going to be moved left.
 *	> 0 = columns are going to be moved right.
 */
void scrollHorizontal(int numPixel) {
	Rectangle clientArea = getClientArea();

	scroll(
		numPixel, 0, 								// destination x, y
		0, 0, 										// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Scroll vertically by 'scrollIndexCount' items.
 * @param scrollIndexCount - the number of items to scroll.
 *	scrollIndexCount > 0 = scroll up. scrollIndexCount < 0 = scroll down
 */
void scrollVertical(int scrollIndexCount) {
	Rectangle clientArea = getClientArea();

	scroll(
		0, 0, 										// destination x, y
		0, scrollIndexCount * getItemHeight(),		// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Selects all of the items in the receiver.
 * <p>
 * If the receiver is single-select, do nothing.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll() {
	checkWidget();
	Vector selection = getSelectionVector();

	if (isMultiSelect() == true) {
		selection = getRoot().selectAll(selection);
		setSelectionVector(selection);
	}
}
/**
 * Set the item that is currently being expanded to 'item'.
 * Used for performance optimizations.
 */
void setExpandingItem(TreeItem2 item) {
	expandingItem = item;
}
public void setFont(Font font) {
	checkWidget();
	Vector children = new Vector();
	Enumeration elements;
	AbstractTreeItem item;

	if (font != null && font.equals(getFont()) == true) {
		return;
	}
	setRedraw(false);									// disable redraw because itemChanged() triggers undesired redraw
	resetItemData();	
	super.setFont(font);

	// Call itemChanged for all tree items
	elements = getRoot().getChildren().elements();
	while (elements.hasMoreElements() == true) {
		children.addElement(elements.nextElement());
	}
	// traverse the tree depth first	
	int size;
	while ((size = children.size()) != 0) {
		item = (AbstractTreeItem)children.elementAt(size - 1);
		children.removeElementAt(size - 1);
		itemChanged(item, 0, getClientArea().width);
		elements = item.getChildren().elements();
		while (elements.hasMoreElements() == true) {
			children.addElement(elements.nextElement());
		}			
	}
	setRedraw(true);									// re-enable redraw
}
/**
 * Display a mark indicating the point at which an item will be inserted.
 * The drop insert item has a visual hint to show where a dragged item 
 * will be inserted when dropped on the tree.
 * 
 * @param item the insert item.  Null will clear the insertion mark.
 * @param before true places the insert mark above 'item'. false places 
 *	the insert mark below 'item'.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark(TreeItem2 item, boolean before){
	checkWidget();
	if (item != null && item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	motif_setInsertMark(item, !before);
}
/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#deselectAll()
 */
public void setSelection(TreeItem2 items[]) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
		deselectAll ();
		return;
	}
	setSelectableSelection(items);
}
/**
 * Set the index of the first visible item in the tree client area 
 * to 'index'.
 * Scroll the new top item to the top of the tree.
 * @param index - 0-based index of the first visible item in the 
 *	tree's client area.
 * @param adjustScrollbar - 
 *	true = the vertical scroll bar is set to reflect the new top index.
 *	false = the vertical scroll bar position is not modified.
 */
void setTopIndex(int index, boolean adjustScrollbar) {
	int indexDiff = index-getTopIndex();

	super.setTopIndex(index, adjustScrollbar);
	calculateWidestScrolledItem(indexDiff);
}
/**
 * Sets the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @param item the item to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#getTopItem()
 * 
 * @since 2.1
 */
public void setTopItem(TreeItem2 item) {
	checkWidget();
	if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.isVisible() == false) {
		item.makeVisible();
	}
	scrollExpandedItemsIntoView(item);
}
/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled
 * and expanded until the item is visible.
 *
 * @param item the item to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showSelection()
 */
public void showItem(TreeItem2 item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	showSelectableItem(item);
}
/**
 * Make 'item' visible by expanding its parent items and scrolling 
 * it into the receiver's client area if necessary.
 * An SWT.Expand event is going to be sent for every parent item 
 * that is expanded to make 'item' visible.
 * @param item - the item that should be made visible to the
 *	user.
 */
void showSelectableItem(SelectableItem item) {
	if (item.getSelectableParent() != this) {
		return;
	}
	if (((TreeItem2) item).isVisible() == false) {
		((TreeItem2) item).makeVisible();
	}
	super.showSelectableItem(item);
}
/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the item at the given point
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem2 getItem(Point point) {
	checkWidget();
	if (point == null) error(SWT.ERROR_NULL_ARGUMENT);
	int itemHeight;
	int hitItemIndex;
	TreeItem2 hitItem;

	if (getClientArea().contains(point) == false) {
		return null;
	}	
	itemHeight = getItemHeight();
	hitItemIndex = point.y / itemHeight;
	hitItem = getRoot().getVisibleItem(hitItemIndex + getTopIndex());
	if (hitItem != null) {
		Point pt = new Point(point.x, point.y);
		pt.x -= hitItem.getPaintStartX();
		pt.y -= itemHeight * hitItemIndex;			
		if (hitItem.isSelectionHit(pt) == false) {
			hitItem = null;
		}
	}
	return hitItem;
}
/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount() {
	checkWidget();
	return super.getSelectionCount();
}
/**
 * Returns the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @return the item at the top of the receiver 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public TreeItem2 getTopItem() {
	checkWidget();
	return (TreeItem2)getVisibleItem(getTopIndex());
}
/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showItem(TreeItem)
 */
public void showSelection() {
	checkWidget();
	super.showSelection();
}

}

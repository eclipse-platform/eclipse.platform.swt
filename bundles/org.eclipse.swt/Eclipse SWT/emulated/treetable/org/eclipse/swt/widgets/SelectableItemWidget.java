package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.io.*;
import java.util.*;
 
/**
 * This class is intended for widgets that display data of 
 * type Item. It provides a framework for scrolling and 
 * handles the screen refresh required when adding and 
 * removing items. 
 */
abstract class SelectableItemWidget extends Composite {
	private static final int DEFAULT_WIDTH = 64;		// used in computeSize if width could not be calculated
	private static final int DEFAULT_HEIGHT = 64;		// used in computeSize if height could not be calculated
	private static final int HORIZONTAL_SCROLL_INCREMENT = 5;	// number of pixel the tree is moved
														// during horizontal line scrolling
	private static ImageData UncheckedImageData;		// deselected check box image data. used to create an image at run time
	private static ImageData GrayUncheckedImageData;		// grayed deselected check box image data. used to create an image at run time
	private static ImageData CheckMarkImageData;		// check mark image data for check box. used to create an image at run time
	static {
		initializeImageData();
	}

	private int topIndex = 0;							// index of the first visible item
	private int itemHeight = 0;							// height of a table item
	private Point itemImageExtent = null;				// size of the item images. Null unless an image is set for any item
	private int textHeight = -1;
	private int contentWidth = 0;						// width of the widget data (ie. table rows/tree items)
	private int horizontalOffset = 0;
	private Vector selectedItems;						// indices of the selected items																
	private SelectableItem lastSelectedItem;			// item that was selected last
	private SelectableItem lastFocusItem;				// item that had the focus last. Always equals lastSelectedItem 
														// for mouse selection but may differ for keyboard selection
	private SelectableItem insertItem;					// item that draws the insert marker to indicate the drop location in a drag and drop operation
	private boolean isInsertAfter;						// indicates where the insert marker is rendered, at the top or bottom of 'insertItem'
	private boolean isCtrlSelection = false;			// the most recently selected item was 
														// selected using the Ctrl modifier key
	private boolean isRemovingAll = false;				// true=all items are removed. Used to optimize screen updates and to control item selection on dispose.
	private boolean hasFocus;					// workaround for 1FMITIE
	private Image uncheckedImage;					// deselected check box
	private Image grayUncheckedImage;						// grayed check box
	private Image checkMarkImage;					// check mark for selected check box
	private Point checkBoxExtent = null;				// width, height of the item check box
	private Listener listener;					// event listener used for all events. Events are dispatched 
														// to handler methods in handleEvents(Event)
	private int drawCount = 0;					// used to reimplement setRedraw(boolean)
/**
 * Create a new instance of ScrollableItemWidget.
 * @param parent - the parent window of the new instance
 * @param style - window style for the new instance
 */
SelectableItemWidget(Composite parent, int style) {
	super(parent, style | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_REDRAW_RESIZE);
	initialize();
}
/**
 * The SelectableItem 'item' has been added to the tree.
 * Calculate the vertical scroll bar.
 * Update the screen to display the new item.
 * @param item - item that has been added to the receiver.
 */
void addedItem(SelectableItem item, int index) {
	calculateVerticalScrollbar();
	if (getLastFocus() == null) {						// if no item has the focus
		setLastFocus(item, true);								// set focus to new (must be first) item 
	}	
}
/**
 * The SelectableItem 'item' is about to be added to the tree.
 * @param item - item that is about to be added to the receiver.
 */
void addingItem(SelectableItem item, int index) {
	if (index >= 0 && index <= getBottomIndex()) {
		scrollVerticalAddingItem(index);
	}
}
/**
 * Set the scroll range of the horizontal scroll bar.
 * Resize the scroll bar if the scroll range maximum 
 * has changed.
 */
void calculateHorizontalScrollbar() {
	int newMaximum = getContentWidth();
	ScrollBar horizontalBar = getHorizontalBar();

	if (horizontalBar.getMaximum() != newMaximum) {
		// The call to setMaximum is ignored if newMaximum is 0.
		// Therefore we can not rely on getMaximum to subsequently return the number of 
		// items in the receiver. We always have to use getVisibleItemCount().
		// Never rely on getMaximum to return what you set. It may not accept the 
		// value you set. Even if you use a valid value now the implementation may change 
		// later. That's what caused 1FRLOSG.
		horizontalBar.setMaximum(newMaximum);
		if (getVerticalBar().getVisible() == false) {			// remove these lines 
			horizontalBar.setMaximum(newMaximum);				// when PR 1FIG5CG 
		}														// is fixed
		resizeHorizontalScrollbar();
	}	
}
/**
 * Calculate the height of items in the receiver.
 * Only the image height is calculated if an item height 
 * has already been calculated. Do nothing if both the item 
 * height and the image height have already been calculated
 */
void calculateItemHeight(SelectableItem item) {
	GC gc;
	String itemText;
	int itemHeight = -1;

	if (itemImageExtent != null && textHeight != -1) {
		return;
	}
	itemText = item.getText();
	if (itemText != null && textHeight == -1) {
		gc = new GC(this);
		itemHeight = gc.stringExtent(itemText).y;
		textHeight = itemHeight;
		gc.dispose();
	}
	if (itemImageExtent == null) {
		itemImageExtent = getImageExtent(item);
		if (itemImageExtent != null) {
			if (itemImageExtent.y > textHeight) {
				itemHeight = itemImageExtent.y;
			}
			else {
				itemHeight = textHeight;
			}
		}
	}
	itemHeight += getItemPadding();									// make sure that there is empty space below the image/text
	if (itemHeight > getItemHeight()) {								// only set new item height if it's higher because new, 
		setItemHeight(itemHeight);									// smaller item height may not include an icon
	}
}
/**
 * Calculate the range of items that need to be selected given
 * the clicked item identified by 'hitItemIndex'
 * @param hitItemIndex - item that was clicked and that the new
 *	selection range will be based on. This index is relative to 
 *	the top index.
 */
int [] calculateShiftSelectionRange(int hitItemIndex) {
	int selectionRange[] = new int[] {-1, -1};
	SelectableItem closestItem = null;
	SelectableItem selectedItem;
	Enumeration selectedItems = getSelectionVector().elements();
	
	while (selectedItems.hasMoreElements() == true) {
		selectedItem = (SelectableItem) selectedItems.nextElement();
		if (closestItem == null) {
			closestItem = selectedItem;
		}
		else
		if (Math.abs(hitItemIndex - getVisibleIndex(selectedItem)) < 
			Math.abs(hitItemIndex - getVisibleIndex(closestItem))) {
			closestItem = selectedItem;
		}
	}
	if (closestItem == null) {								// no item selected
		closestItem = getLastSelection();					// item selected last may still have the focus
	}
	if (closestItem != null) {
		selectionRange[0] = getVisibleIndex(closestItem);
		selectionRange[1] = hitItemIndex;
	}
	return selectionRange;
}
/**
 * Set the scroll range of the vertical scroll bar.
 * Resize the scroll bar if the scroll range maximum 
 * has changed.
 */
void calculateVerticalScrollbar() {
	int newMaximum = getVisibleItemCount();
	ScrollBar verticalBar = getVerticalBar();

	// The call to setMaximum is ignored if newMaximum is 0.
	// Therefore we can not rely on getMaximum to subsequently return the number of 
	// items in the receiver. We always have to use getVisibleItemCount().
	// Never rely on getMaximum to return what you set. It may not accept the 
	// value you set. Even if you use a valid value now the implementation may change 
	// later. That's what caused 1FRLOSG.
	verticalBar.setMaximum(newMaximum);
	if (getHorizontalBar().getVisible() == false) {			// remove these lines 
		verticalBar.setMaximum(newMaximum);					// when PR 1FIG5CG 
	}														// is fixed	
	resizeVerticalScrollbar();
}

/**
 * Answer the size of the receiver needed to display all items.
 * The length of the longest item in the receiver is used for the 
 * width.
 */
public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = getContentWidth();
	int height = getItemCount() * getItemHeight();
	int style = getStyle();
	int scrollBarWidth = computeTrim(0, 0, 0, 0).width;
				
	if (width == 0) {
		width = DEFAULT_WIDTH;
	}
	if (height == 0) {
		height = DEFAULT_HEIGHT;
	}
	if (wHint != SWT.DEFAULT) {
		width = wHint;
	}
	if (hHint != SWT.DEFAULT) {
		height = hHint;
	}
	if ((getStyle() & SWT.V_SCROLL) != 0) {
		width += scrollBarWidth; 
	}
	if ((getStyle() & SWT.H_SCROLL) != 0) {
		height += scrollBarWidth;
	}
	return new Point(width, height);
}
/**
 * Do a ctrl+shift selection meaning the ctrl and shift keys 
 * were pressed when the mouse click on an item occurred. 
 * If an already selected item was clicked the focus is moved to 
 * that item.
 * If the previous selection was a ctrl or ctrl+shift selection
 * the range between the last selected item and the clicked item
 * is selected.
 * Otherwise a regular shift selection is performed.
 * @param hitItem - specifies the clicked item
 * @param hitItemIndex - specifies the index of the clicked item 
 *	relative to the first item.
 */
void ctrlShiftSelect(SelectableItem hitItem, int hitItemIndex) {
	int fromIndex = -1;
	int toIndex = -1;
	int lastSelectionIndex = -1;
	int selectionRange[];
	SelectableItem lastSelection = getLastSelection();

	if (lastSelection != null) {
		lastSelectionIndex = getVisibleIndex(lastSelection);
	}
	if ((getSelectionVector().contains(hitItem) == true) &&		// clicked an already selected item?
		(hitItemIndex != lastSelectionIndex)) {				// and click was not on last selected item?
		setLastSelection(hitItem, true);							// set last selection which also sets the focus
	}
	else
	if (isCtrlSelection() == true) {						// was last selection ctrl/ctrl+shift selection? 
		fromIndex = lastSelectionIndex;						// select from last selection
		toIndex = hitItemIndex;
	}
	else {													// clicked outside existing selection range
		selectionRange = calculateShiftSelectionRange(hitItemIndex);
		fromIndex = selectionRange[0];
		toIndex = selectionRange[1];
	}
	if (fromIndex != -1 && toIndex != -1) {
		selectRange(fromIndex, toIndex);
	}
}
/**
 * Deselect 'item'.
 * @param item - item that should be deselected
 */
void deselect(SelectableItem item) {
	Vector selectedItems = getSelectionVector();
	
	if ((item != null) && (item.isSelected() == true)) {
		item.setSelected(false);
		redrawSelection(item);
		selectedItems.removeElement(item);
	}
}
/**
 * Deselect all item except 'keepSelected'. 
 * @param keepSelected - item that should remain selected
 */
void deselectAllExcept(SelectableItem keepSelected) {
	Vector selectedItems = getSelectionVector();
	Vector deselectedItems = new Vector(selectedItems.size());
	Enumeration elements = selectedItems.elements();
	SelectableItem item;

	// deselect and repaint previously selected items
	while (elements.hasMoreElements() == true) {
		item = (SelectableItem) elements.nextElement();
		if (item.isSelected() == true && item != keepSelected) {		
			item.setSelected(false);
			// always redraw the selection, even if item is redrawn again 
			// in setLastSelection. Fixes 1G0GQ8W
			redrawSelection(item);
			deselectedItems.addElement(item);
		}
	}
	elements = deselectedItems.elements();
	while (elements.hasMoreElements() == true) {
		item = (SelectableItem) elements.nextElement();
		selectedItems.removeElement(item);
	}
	setLastSelection(keepSelected, false);
}
/**
 * Deselect all items except those in 'keepSelected'. 
 * @param keepSelected - items that should remain selected
 */
void deselectAllExcept(Vector keepSelected) {
	Vector selectedItems = getSelectionVector();
	Vector deselectedItems = new Vector(selectedItems.size());
	Enumeration elements = selectedItems.elements();
	SelectableItem item;

	// deselect and repaint previously selected items
	while (elements.hasMoreElements() == true) {
		item = (SelectableItem) elements.nextElement();
		if (item.isSelected() == true && keepSelected.contains(item) == false) {		
			item.setSelected(false);
			// always redraw the selection, even if item is redrawn again 
			// in setLastSelection. Fixes 1G0GQ8W
			redrawSelection(item);
			deselectedItems.addElement(item);
		}
	}
	elements = deselectedItems.elements();
	while (elements.hasMoreElements() == true) {
		item = (SelectableItem) elements.nextElement();
		selectedItems.removeElement(item);
	}
	if (keepSelected.size() > 0) {
		setLastSelection((SelectableItem) keepSelected.firstElement(), false);
	}
}
/**
 * Deselect 'item'. Notify listeners.
 * @param item - item that should be deselected
 */
void deselectNotify(SelectableItem item) {
	Event event = new Event();

	if (item.isSelected() == true) {
		deselect(item);
		setLastSelection(item, true);		
		update();												// looks better when event notification takes long to return
	}
	event.item = item;
	notifyListeners(SWT.Selection, event);
}
/**
 * Deselect all items starting at and including 'fromIndex'
 * stopping at and including 'toIndex'.
 * @param fromIndex - index relative to the first item where 
 *	deselection should start. Deselecion includes 'fromIndex'.
 * @param toIndex - index relative to the first item where
 *	deselection should stop. Deselecion includes 'toIndex'.
 */
void deselectRange(int fromIndex, int toIndex) {
	if (fromIndex > toIndex) {
		for (int i = toIndex; i <= fromIndex; i++) {
			deselect(getVisibleItem(i));
		}
	}
	else
	if (fromIndex < toIndex) {		
		for (int i = toIndex; i >= fromIndex; i--) {
			deselect(getVisibleItem(i));
		}
	}
	setLastSelection(getVisibleItem(fromIndex), true);
}
/**
 * Modifier Key		Action
 * None				Remove old selection, move selection down one item
 * Ctrl				Keep old selection, move input focus down one item
 * Shift			Extend selection by one item.
 * Modifier Key is ignored when receiver has single selection style.
 * @param keyMask - the modifier key that was pressed
 */
void doArrowDown(int keyMask) {
	SelectableItem lastFocus = getLastFocus();
	SelectableItem newFocus;
	int focusItemIndex = getVisibleIndex(lastFocus);
	
	if (focusItemIndex < (getVisibleItemCount() - 1)) { 			// - 1 because indices are 0 based
		focusItemIndex++;
		newFocus = getVisibleItem(focusItemIndex);
		if (keyMask == SWT.CTRL && isMultiSelect() == true) {
			setLastFocus(newFocus, true);
		}
		else
		if (keyMask == SWT.SHIFT && isMultiSelect() == true) {
			shiftSelect(newFocus, focusItemIndex);
		}		
		else {
			deselectAllExcept(newFocus);
			selectNotify(newFocus);
		}
	}
}
/**
 * Modifier Key		Action
 * None				Scroll receiver to the left
 * Ctrl				See None above
 * Shift			See None above
 * @param keyMask - the modifier key that was pressed
 */
void doArrowLeft(int keyMask) {
	ScrollBar horizontalBar = getHorizontalBar();
	int scrollSelection = horizontalBar.getSelection();
	int scrollAmount;

	if (horizontalBar.getVisible() == false) {
		return;
	}	
	scrollAmount = Math.min(HORIZONTAL_SCROLL_INCREMENT, scrollSelection);
	horizontalBar.setSelection(scrollSelection - scrollAmount);
	setHorizontalOffset(horizontalBar.getSelection() * -1);
}
/**
 * Modifier Key		Action
 * None				Scroll receiver to the right
 * Ctrl				See None above
 * Shift			See None above
 * @param keyMask - the modifier key that was pressed
 */
void doArrowRight(int keyMask) {
	ScrollBar horizontalBar = getHorizontalBar();
	int scrollSelection = horizontalBar.getSelection();
	int scrollAmount;

	if (horizontalBar.getVisible() == false) {
		return;
	}
	scrollAmount = Math.min(									// scroll by the smaller of 
		HORIZONTAL_SCROLL_INCREMENT, 							// the scroll increment
		horizontalBar.getMaximum() 								// and the remaining scroll range
			- horizontalBar.getPageIncrement() 
			- scrollSelection);	
	horizontalBar.setSelection(scrollSelection + scrollAmount);
	setHorizontalOffset(horizontalBar.getSelection() * -1);
}
/**
 * Modifier Key		Action
 * None				Remove old selection, move selection up one item
 * Ctrl				Keep old selection, move input focus up one item
 * Shift			Extend selection by one item.
 * Modifier Key is ignored when receiver has single selection style. 
 * @param keyMask - the modifier key that was pressed
 */
void doArrowUp(int keyMask) {
	SelectableItem lastFocus = getLastFocus();
	SelectableItem newFocus;
	int focusItemIndex = getVisibleIndex(lastFocus);
	
	if (focusItemIndex > 0) {
		focusItemIndex--;
		newFocus = getVisibleItem(focusItemIndex);
		if (keyMask == SWT.CTRL && isMultiSelect() == true) {
			setLastFocus(newFocus, true);
		}
		else
		if (keyMask == SWT.SHIFT && isMultiSelect() == true) {
			shiftSelect(newFocus, focusItemIndex);
		}		
		else {
			deselectAllExcept(newFocus);
			selectNotify(newFocus);
		}
	}
}
/**
 * Perform a selection operation on the item check box.
 * @param item - the item that was clicked
 */
void doCheckItem(SelectableItem item) {
	Event event = new Event();
	
	item.setChecked(!item.getChecked());
	event.item = item;
	event.detail = SWT.CHECK;
	notifyListeners(SWT.Selection, event);
}
/**
 * Free resources.
 */
void doDispose() {
	setRemovingAll(true);
	getSelectionVector().removeAllElements();
	lastFocusItem = null;
	lastSelectedItem = null;		
	if (uncheckedImage != null) {
		uncheckedImage.dispose();
	}	
	if (grayUncheckedImage != null) {
		grayUncheckedImage.dispose();
	}
	if (checkMarkImage != null) {
		checkMarkImage.dispose();
	}
}
/**
 * Modifier Key		Action
 * None				Remove old selection, move selection to the 
 *					last item
 * Ctrl				Keep old selection, move input focus to the 
 *					last item
 * Shift			Extend selection to the last item.
 * Modifier Key is ignored when receiver has single selection style.
 * @param keyMask - the modifier key that was pressed
 */
void doEnd(int keyMask) {
	SelectableItem lastFocus = getLastFocus();
	SelectableItem newFocus;
	int focusItemIndex = getVisibleIndex(lastFocus);
	int lastItemIndex = getVisibleItemCount() - 1;				// - 1 because indices are 0 based
		
	if (focusItemIndex < lastItemIndex) {
		newFocus = getVisibleItem(lastItemIndex);
		if (keyMask == SWT.CTRL && isMultiSelect() == true) {
			setLastFocus(newFocus, true);
		}
		else
		if (keyMask == SWT.SHIFT && isMultiSelect() == true) {
			shiftSelect(newFocus, lastItemIndex);
		}
		else {
			deselectAllExcept(newFocus);
			selectNotify(newFocus);
		}
	}
}
/**
 * Modifier Key		Action
 * None				Remove old selection, move selection to the 
 *					first item
 * Ctrl				Keep old selection, move input focus to the 
 *					first item
 * Shift			Extend selection to the first item.
 * Modifier Key is ignored when receiver has single selection style. 
 * @param keyMask - the modifier key that was pressed
 */
void doHome(int keyMask) {
	SelectableItem lastFocus = getLastFocus();
	SelectableItem newFocus;
	int firstItemIndex = 0;

	if (getVisibleIndex(lastFocus) > firstItemIndex) {
		newFocus = getVisibleItem(firstItemIndex);
		if (keyMask == SWT.CTRL && isMultiSelect() == true) {
			setLastFocus(newFocus, true);
		}
		else
		if (keyMask == SWT.SHIFT && isMultiSelect() == true) {
			shiftSelect(newFocus, firstItemIndex);
		}		
		else {
			deselectAllExcept(newFocus);
			selectNotify(newFocus);
		}
	}
}
/**
 * Perform a mouse select action according to the key state 
 * mask in 'eventStateMask'.
 * Key state mask is ignored when receiver has the single selection 
 * style.
 * @param item - the item that was clicked
 * @param itemIndex - the index of the clicked item relative 
 *	to the first item of the receiver
 * @param eventStateMask - the key state mask of the mouse event
 * @param button - the mouse button that was pressed
 */
void doMouseSelect(SelectableItem item, int itemIndex, int eventStateMask, int button) {
	if (((eventStateMask & SWT.CTRL) != 0) &&
		((eventStateMask & SWT.SHIFT) != 0) &&
		(isMultiSelect() == true)) {
		if (getSelectionVector().size() == 0) {			// no old selection?
			selectNotify(item);							// do standard CTRL selection
		}
		else {
			ctrlShiftSelect(item, itemIndex);
		}
		setCtrlSelection(true);
	}
	else 
	if (((eventStateMask & SWT.SHIFT) != 0) && (isMultiSelect() == true)) {
		shiftSelect(item, itemIndex);
		setCtrlSelection(false);
	}
	else
	if (((eventStateMask & SWT.CTRL) != 0) && (isMultiSelect() == true)) {
		toggleSelectionNotify(item);
		setCtrlSelection(true);
	}
	else
	if (button != 3 || item.isSelected() == false) {
		// only select the item (and deselect all others) if the mouse click is 
		// not a button 3 click or if a previously unselected item was clicked.
		// Fixes 1G97L65
		deselectAllExcept(item);
		selectNotify(item);
		setCtrlSelection(false);
	}
}
/**
 * Modifier Key		Action
 * None				Remove old selection, move selection one page down
 * Ctrl				Keep old selection, move input focus one page down
 * Shift			Extend selection one page down
 * One page is the number of items that can be displayed in the 
 * receiver's canvas without truncating the last item.
 * The selection is set to the last item if there is no full page 
 * of items left.
 * Modifier Key is ignored when receiver has single selection style.  
 * @param keyMask - the modifier key that was pressed
 */
void doPageDown(int keyMask) {
	SelectableItem newFocus;
	int focusItemIndex = getVisibleIndex(getLastFocus());
	int lastItemIndex = getVisibleItemCount() - 1;				// - 1 because indices are 0 based
	int visibleItemCount;
		
	if (focusItemIndex < lastItemIndex) {
		visibleItemCount = getItemCountWhole();
		focusItemIndex = Math.min(
			lastItemIndex, 
			focusItemIndex + (visibleItemCount - 1));
		newFocus = getVisibleItem(focusItemIndex);
		if (newFocus == null) {
			return;
		}
		if (keyMask == SWT.CTRL && isMultiSelect() == true) {
			setLastFocus(newFocus, true);
		}
		else
		if (keyMask == SWT.SHIFT && isMultiSelect() == true) {
			shiftSelect(newFocus, focusItemIndex);
		}		
		else {
			deselectAllExcept(newFocus);		
			selectNotify(newFocus);
		}
	}
}
/**
 * Modifier Key		Action
 * None				Remove old selection, move selection one page up
 * Ctrl				Keep old selection, move input focus one page up
 * Shift			Extend selection one page up
 * One page is the number of items that can be displayed in the 
 * receiver's canvas without truncating the last item.
 * The selection is set to the first item if there is no full page 
 * of items left.
 * Modifier Key is ignored when receiver has single selection style.  
 * @param keyMask - the modifier key that was pressed
 */
void doPageUp(int keyMask) {
	SelectableItem newFocus;	
	int focusItemIndex = getVisibleIndex(getLastFocus());
	int visibleItemCount;

	if (focusItemIndex > 0) {
		visibleItemCount = getItemCountWhole();
		focusItemIndex = Math.max(
			0, 
			focusItemIndex - (visibleItemCount - 1));
		newFocus = getVisibleItem(focusItemIndex);
		if (keyMask == SWT.CTRL && isMultiSelect() == true) {
			setLastFocus(newFocus, true);
		}
		else
		if (keyMask == SWT.SHIFT && isMultiSelect() == true) {
			shiftSelect(newFocus, focusItemIndex);
		}		
		else {
			deselectAllExcept(newFocus);		
			selectNotify(newFocus);
		}
	}
}
/**
 * Modifier Key		Action
 * Ctrl				Keep old selection, toggle selection of the item 
 *					that has the input focus
 * Shift			Extend selection to the item that has the input 
 *					focus
 * Ctrl & Shift		Set selection to the item that has input focus
 * Do nothing if receiver has single selection style.
 * @param keyMask - the modifier key that was pressed
 */

void doSpace(int keyMask) {
	SelectableItem item = getLastFocus();
	if (item == null) return;
	int itemIndex = getVisibleIndex(item);

	if (keyMask == SWT.NULL && item.isSelected() == false) {	// do simple space select in SINGLE and MULTI mode
		deselectAllExcept(item);
		selectNotify(item);
		return;
	}
	if (isMultiSelect() == false) {
		return;
	}
	if (keyMask == SWT.CTRL) {
		toggleSelectionNotify(item);
	}
	else
	if (((keyMask & SWT.CTRL) != 0) && ((keyMask & SWT.SHIFT) != 0)) {
		deselectAllExcept(item);
		selectNotify(item);
	}
	else
	if (keyMask == SWT.SHIFT) {
		shiftSelect(item, itemIndex);
	}
}
/**
 * Make sure that free space at the bottom of the receiver is 
 * occupied.
 * There will be new space available below the last item when the 
 * receiver's height is increased. In this case the receiver
 * is scrolled down to occupy the new space.if the top item is 
 * not the first item of the receiver.
 */
void claimBottomFreeSpace() {
	int clientAreaItemCount = getItemCountWhole();
	int topIndex = getTopIndex();
	int newTopIndex;
	int lastItemIndex = getVisibleItemCount() - topIndex;

	if ((topIndex > 0) && 
		(lastItemIndex > 0) &&
		(lastItemIndex < clientAreaItemCount)) {
		newTopIndex = Math.max(0, topIndex-(clientAreaItemCount-lastItemIndex));
		setTopIndex(newTopIndex, true);
	}
}
/**
 * Make sure that free space at the right side of the receiver is 
 * occupied. 
 * There will be new space available at the right side of the receiver 
 * when the receiver's width is increased. In this case the receiver 
 * is scrolled to the right to occupy the new space.if possible.
 */
void claimRightFreeSpace() {
	int clientAreaWidth = getClientArea().width;
	int newHorizontalOffset = clientAreaWidth - getContentWidth();
	
	if (newHorizontalOffset - getHorizontalOffset() > 0) {			// item is no longer drawn past the right border of the client area
		newHorizontalOffset = Math.min(0, newHorizontalOffset);		// align the right end of the item with the right border of the 
		setHorizontalOffset(newHorizontalOffset);					// client area (window is scrolled right)	
	}
}
/**	Not used right now. Replace focusIn/focusOut with this method once 
 *	Display.getFocusWindow returns the new focus window on FocusOut event 
 *	(see 1FMITIE)
 * The focus has moved in to or out of the receiver.
 * Redraw the item selection to reflect the focus change.
 * @param event - the focus change event
 */
void focusChange(Event event) {
	Enumeration items = getSelectionVector().elements();
	SelectableItem lastFocusItem = getLastFocus();
	SelectableItem item;

	while (items.hasMoreElements() == true) {
		item = (SelectableItem) items.nextElement();
		redrawSelection(item);
	}
	if (lastFocusItem != null) {
		redrawSelection(lastFocusItem);
	}
}
/**
 * The focus has moved in to or out of the receiver.
 * Redraw the item selection to reflect the focus change.
 * @param event - the focus change event
 */
void focusIn(Event event) {
	Enumeration items = getSelectionVector().elements();
	SelectableItem lastFocusItem = getLastFocus();
	SelectableItem item;

	// Workaround for 1FMITIE
	hasFocus = true;
	while (items.hasMoreElements() == true) {
		item = (SelectableItem) items.nextElement();
		redrawSelection(item);
	}
	if (lastFocusItem != null) {
		redrawSelection(lastFocusItem);
	}
	// Fix blank item on slow machines/VMs. Also fixes 1G0IFMZ. 
	update();
}
/**
 * The focus has moved in to or out of the receiver.
 * Redraw the item selection to reflect the focus change.
 * @param event - the focus change event
 */
void focusOut(Event event) {
	Enumeration items = getSelectionVector().elements();
	SelectableItem lastFocusItem = getLastFocus();
	SelectableItem item;

	// Workaround for 1FMITIE
	hasFocus = false;
	while (items.hasMoreElements() == true) {
		item = (SelectableItem) items.nextElement();
		redrawSelection(item);
	}
	if (lastFocusItem != null) {
		redrawSelection(lastFocusItem);
	}
	// Fix blank item on slow machines/VMs. Also fixes 1G0IFMZ. 
	update();								
}
/**
 * Answer the index of the last item position in the receiver's 
 * client area.
 * @return 0-based index of the last item position in the tree's 
 * 	client area.
 */
int getBottomIndex() {
	return getTopIndex() + getItemCountTruncated(getClientArea());
}
/**
 * Answer the size of the check box image.
 * The calculation is cached and assumes that the images for 
 * the checked and unchecked state are the same size.
 */
Point getCheckBoxExtent() {
	Image checkedImage;
	Rectangle imageBounds;
	
	if (checkBoxExtent == null) {
		checkedImage = getUncheckedImage();
		if (checkedImage != null) {
			imageBounds = checkedImage.getBounds();
			checkBoxExtent = new Point(imageBounds.width, imageBounds.height);
		}
		else {
			checkBoxExtent = new Point(0, 0);
		}
	}
	return checkBoxExtent;
}
/**
 * Answer the image for the selected check box
 * Answer null if the image couldn't be loaded.
 */
Image getCheckMarkImage() {
	InputStream resourceStream;
	
	if (checkMarkImage == null) {
		checkMarkImage = new Image(getDisplay(), CheckMarkImageData);
	}
	return checkMarkImage;
}
/**
 * Answer the width of the receiver's content. 
 * Needs to be set by subclasses.
 */
int getContentWidth() {
	return contentWidth;
}
/**
 * Answer the horizontal drawing offset used for scrolling.
 * This is < 0 if the receiver has been scrolled to the left,
 * > 0 if the receiver has been scrolled to the right and 0
 * if the receiver has not been scrolled.
 */
int getHorizontalOffset() {
	return horizontalOffset;
}

/**
 * Answer the size of item images. Calculated during the item 
 * height calculation.
 */
Point getImageExtent() {
	return itemImageExtent;
}
/**
 * Answer the image extent of 'item'. Overridden by subclasses.
 */
Point getImageExtent(SelectableItem item) {
	Image image = item.getImage();
	Rectangle imageBounds;
	Point imageExtent = null;

	if (image != null) {
		imageBounds = image.getBounds();
		imageExtent = new Point(imageBounds.width, imageBounds.height);
	}
	return imageExtent;
}
/**
 * Answer the index of 'item' in the receiver.
 */
abstract int getIndex(SelectableItem item);
/** 
 * Answer the first and last index of items that can be displayed in 
 * the area defined by 'clipRectangle'. This includes partial items.
 * @return
 *	Array - 
 *		First element is the index of the first item in 'clipRectangle'.
 *		Second element is the index of the last item in 'clipRectangle'.
 */
int[] getIndexRange(Rectangle clipRectangle) {
	int visibleRange[] = new int[] {0, 0};

	visibleRange[0] = clipRectangle.y / getItemHeight();
	visibleRange[1] = 
		visibleRange[0] + 
		getItemCountTruncated(clipRectangle) - 1;			// - 1 because item index is 0 based
	return visibleRange;
}
/**
 * Return the item that draws the marker indicating the insert location 
 * in a drag and drop operation
 */
SelectableItem getInsertItem() {
	return insertItem;
}
/**
 * Answer the number of items in the receiver.
 */
public abstract int getItemCount();
/**
 * Answer the number of items that can be displayed in 'rectangle'.
 * The result includes partially visible items.
 */
int getItemCountTruncated(Rectangle rectangle) {
	int itemHeight = getItemHeight();
	int itemCount = 0;
	int startIndex;

	startIndex = rectangle.y / itemHeight;
	itemCount = (int) Math.ceil(((float) rectangle.y + rectangle.height) / itemHeight)-startIndex;
	return itemCount;
}
/**
 * Answer the number of items that can be displayed in the client 
 * area of the receiver.
 * The result only includes items that completely fit into the 
 * client area.
 */
int getItemCountWhole() {
	return getClientArea().height / getItemHeight();
}
/**
 * Answer the height of an item in the receiver.
 * The item height is the greater of the item icon height and 
 * text height of the first item that has text or an image 
 * respectively.
 * Calculate a default item height based on the font height if
 * no item height has been calculated yet.
 */
public int getItemHeight() {
	checkWidget();
	GC gc;
	if (itemHeight == 0) {
		gc = new GC(this);
		itemHeight = gc.stringExtent("String").y + getItemPadding();		// initial item height=font height + item spacing
											// use real font height here when available in SWT, instead of GC.textExtent
		gc.dispose();
	}		
	return itemHeight;
}
/**
 * Answer the number of pixels that should be added to the item height.
 */
int getItemPadding() {
	return 2 + getDisplay().textHighlightThickness;
}
/**
 * Answer the item that most recently received the input focus.
 */
SelectableItem getLastFocus() {
	return lastFocusItem;
}
/**
 * Answer the item that was selected most recently.
 */ 
SelectableItem getLastSelection() {
	return lastSelectedItem;
}
/**
 * Answer the event listener used for all events. Events are 
 * dispatched to handler methods in handleEvents(Event).
 * This scheme saves a lot of inner classes.
 */
Listener getListener() {
	return listener;
}
/**
 * Answer the y coordinate at which 'item' is drawn. 
 * @param item - SelectableItem for which the paint position 
 *	should be returned
 * @return the y coordinate at which 'item' is drawn.
 *	Return -1 if 'item' is not an item of the receiver
 */
int getRedrawY(SelectableItem item) {
	int redrawIndex = getVisibleIndex(item);
	int redrawY = -1;

	if (redrawIndex != -1) {
		redrawY = (redrawIndex - getTopIndex()) * getItemHeight();
	}
	return redrawY;
}
/**
 * Answer the number of selected items in the receiver.
 */
public int getSelectionCount() {
	checkWidget();
	return getSelectionVector().size();
}
/**
 * Answer the selected items of the receiver. 
 * @return The selected items of the receiver stored in a Vector.
 *	Returned Vector is empty if no items are selected.
 */
Vector getSelectionVector() {
	return selectedItems;
}
/**
 * Answer the width of 'text' in pixel.
 * Answer 0 if 'text' is null.
 */
int getTextWidth(String text) {
	int textWidth = 0;
	GC gc;

	if (text != null) {
		gc = new GC(this);
		textWidth = gc.stringExtent(text).x;
		gc.dispose();
	}
	return textWidth;
}
/**
 * Answer the index of the first visible item in the receiver's 
 * client area.
 * @return 0-based index of the first visible item in the 
 * 	receiver's client area.
 */
int getTopIndex() {
	return topIndex;
}
/**
 * Answer the image for the deselected check box.
 */
Image getUncheckedImage() {
	InputStream resourceStream;
	
	if (uncheckedImage == null) {
		uncheckedImage = new Image(getDisplay(), UncheckedImageData);
	}
	return uncheckedImage;
}

/**
 * Answer the image for the grayed eck box.
 */
Image getGrayUncheckedImage() {
	InputStream resourceStream;
	
	if (grayUncheckedImage == null) {
		grayUncheckedImage = new Image(getDisplay(), GrayUncheckedImageData);
	}
	return grayUncheckedImage;
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
 *	Normally, every item of the receiver is visible.
 */
abstract int getVisibleIndex(SelectableItem item);
/**
 * Answer the SelectableItem located at 'itemIndex' in the 
 * receiver.
 * @param itemIndex - location of the SelectableItem object 
 *	to return
 */
abstract SelectableItem getVisibleItem(int itemIndex);
/**
 * Answer the number of visible items of the receiver.
 * Note: 
 * 	Visible in this context does not neccessarily mean that the 
 * 	item is displayed on the screen. It only means that the item 
 * 	would be displayed if it is located inside the receiver's 
 * 	client area.
 *	Normally every item of the receiver is visible.
 */
int getVisibleItemCount() {
	return getItemCount();
}
/**
 * Answer the y coordinate at which 'item' is drawn. 
 * @param item - SelectableItem for which the paint position 
 *	should be returned
 * @return the y coordinate at which 'item' is drawn.
 *	Return -1 if 'item' is null or outside the client area
 */
abstract int getVisibleRedrawY(SelectableItem item);
/**
 * Handle the events the receiver is listening to.
 */
void handleEvents(Event event) {
	switch (event.type) {
		case SWT.Dispose:
			doDispose();
			break;		
		case SWT.KeyDown:
			keyDown(event);
			break;
		case SWT.Resize:
			resize(event);
			break;
		case SWT.Selection:
			if (event.widget == getVerticalBar()) {
				scrollVertical(event);
			}
			else
			if (event.widget == getHorizontalBar()) {
				scrollHorizontal(event);
			}
			break;
		case SWT.FocusOut:
			focusOut(event);
			break;
		case SWT.FocusIn:
			focusIn(event);
			break;	
		case SWT.Traverse:
			switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
					event.doit = true;
					break;
			}
			break;			
	}	
}



/**
 * Answer whether 'item' has the input focus.
 */
boolean hasFocus(SelectableItem item) {
	return (isFocusControl() && item == getLastFocus());
}
/**
 * Initialize the receiver. Add event listeners and set widget 
 * colors.
 */
void initialize() {
	Display display = getDisplay();	
	ScrollBar horizontalBar = getHorizontalBar();
	ScrollBar verticalBar = getVerticalBar();

	// listener may be needed by overridden installListeners()
	listener = new Listener() {
		public void handleEvent(Event event) {handleEvents(event);}
	};	
	setSelectionVector(new Vector());
	installListeners();
	calculateVerticalScrollbar();
	calculateHorizontalScrollbar();
	horizontalBar.setMinimum(0);
	verticalBar.setMinimum(0);
	horizontalBar.setIncrement(HORIZONTAL_SCROLL_INCREMENT);
	setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
	setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
}
/**
 * Initialize the ImageData used for the checked/unchecked images.
 */ 
static void initializeImageData() {
	PaletteData uncheckedPalette = new PaletteData(	
		new RGB[] {new RGB(128, 128, 128), new RGB(255, 255, 255)});
	PaletteData grayUncheckedPalette = new PaletteData(	
		new RGB[] {new RGB(128, 128, 128), new RGB(192, 192, 192)});
	PaletteData checkMarkPalette = new PaletteData(	
		new RGB[] {new RGB(0, 0, 0), new RGB(252, 3, 251)});
	byte[] checkbox = new byte[] {0, 0, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 0, 0};

	// Each pixel is represented by one bit in the byte data. 
	// The bit references the palette position (0 or 1). Each pixel row of an image is padded to one byte.
	// Arguments: width, height, depth, palette, scanline padding, data
	UncheckedImageData =		new ImageData(11, 11, 1, uncheckedPalette, 2, checkbox);
	GrayUncheckedImageData = 	new ImageData(11, 11, 1, grayUncheckedPalette, 2, checkbox);
	CheckMarkImageData =		new ImageData(7, 7, 1, checkMarkPalette, 1, new byte[] {-4, -8, 112, 34, 6, -114, -34});
	CheckMarkImageData.transparentPixel = 1;
}
/**
 * Add event listeners to the tree widget and its scroll bars.
 */
void installListeners() {
	Listener listener = getListener();
	
	addListener(SWT.Dispose, listener);	
	addListener(SWT.Resize, listener);
	addListener(SWT.KeyDown, listener);
	addListener(SWT.FocusOut, listener);
	addListener(SWT.FocusIn, listener);
	addListener(SWT.Traverse, listener);
	
	getVerticalBar().addListener(SWT.Selection, listener);
	getHorizontalBar().addListener(SWT.Selection, listener);
}
/**
 * Answer whether the currently selected items were selected 
 * using the ctrl key.
 */
boolean isCtrlSelection() {
	return isCtrlSelection;
}
/**
 * Answer true if all items in the widget are disposed.
 * Used to optimize item disposal. Prevents unnecessary screen 
 * updates.
 */
boolean isRemovingAll() {
	return isRemovingAll;
}
/**
 * Answer whether the receiver has the input focus.
 * Workaround for 1FMITIE
 */
public boolean isFocusControl() {
	return hasFocus;
}
/**
 * Return whether the drop insert position is before or after the 
 * item set using motif_setInsertMark.
 * @return 
 *	true=insert position is after the insert item
 *	false=insert position is before the insert item
 */
boolean isInsertAfter() {
	return isInsertAfter;
}
/**
 * Answer whether the receiver has the MULTI selection style set.
 * @return
 *	true = receiver is in multiple selection mode.
 *	false = receiver is in single selection mode.
 */
boolean isMultiSelect() {
	return ((getStyle() & SWT.MULTI) != 0);
}
/**
 * The item identified by 'changedItem' has changed. 
 * Calculate the item height based on the new item data (it might 
 * now have an image which could also be the first image in the 
 * receiver)
 * Redraw the whole window if the item height has changed. Otherwise 
 * redraw only the changed item or part of it depending on the 
 * 'repaintStartX' and 'repaintWidth' parameters.
 * @param changedItem - the item that has changed
 * @param repaintStartX - x position of the item redraw. 
 * @param repaintWidth - width of the item redraw.
 */
void itemChanged(SelectableItem changedItem, int repaintStartX, int repaintWidth) {
	int yPosition;
	int itemHeight;
	int oldItemHeight = getItemHeight();
	Point oldImageExtent = getImageExtent();
	
	calculateItemHeight(changedItem);					// make sure that the item height is recalculated
	// no redraw necessary if redraw width is 0 or item is not visible
	if (repaintWidth == 0 || (yPosition = getVisibleRedrawY(changedItem)) == -1) {
		return;
	}														// if changedItem is the first item with image.
	itemHeight = getItemHeight();
	if ((oldItemHeight == itemHeight) &&				// only redraw changed item if the item height and 
	    (oldImageExtent == getImageExtent())) {			// image size has not changed. The latter will only change once,
														// from null to a value-so it's safe to test using !=
		// redrawing outside the client area redraws the widget border on Motif.
		// adjust the redraw width if necessary. Workaround for 1G4TQRW
		repaintWidth = Math.min(repaintWidth, getClientArea().width - repaintStartX);
		if (repaintWidth > 0) {
			redraw(repaintStartX, yPosition, repaintWidth, itemHeight, true);
		}
	}
	else {
		redraw();										// redraw all items if the item height has changed
	}
}
/**
 * A key was pressed. Call the appropriate handler method.
 * @param event - the key event
 */
void keyDown(Event event) {
	boolean isCtrlSelection = isCtrlSelection();
	
	if (event.stateMask != SWT.CTRL) {
		isCtrlSelection = false;
	}
	switch (event.keyCode) {
		case SWT.ARROW_UP:
			doArrowUp(event.stateMask);
			break;
		case SWT.ARROW_DOWN:
			doArrowDown(event.stateMask);
			break;
		case SWT.ARROW_LEFT:
			doArrowLeft(event.stateMask);
			break;
		case SWT.ARROW_RIGHT:
			doArrowRight(event.stateMask);
			break;			
		case SWT.PAGE_UP:
			doPageUp(event.stateMask);
			break;		
		case SWT.PAGE_DOWN:
			doPageDown(event.stateMask);
			break;
		case SWT.HOME:
			doHome(event.stateMask);
			break;
		case SWT.END:
			doEnd(event.stateMask);
			break;
		default:										// no selection occurred, keep previous 
			isCtrlSelection = isCtrlSelection();		// selection type information
	}
	if (event.character == ' ') {
		doSpace(event.stateMask);
		isCtrlSelection = (event.stateMask == SWT.CTRL);
	}
	setCtrlSelection(isCtrlSelection);
}
/**
 * Sets the drop insert item.
 * The drop insert item has a visual hint to show where a dragged item 
 * will be inserted when dropped on the tree.
 * <p>
 * @param item the insert item 
 * @param after true places the insert mark below 'item'. false places 
 *	the insert mark above 'item'.
 */
void motif_setInsertMark(SelectableItem item, boolean after) {
	SelectableItem currentItem = getInsertItem();
	int redrawY;
	
	setInsertItem(item);
	setInsertAfter(after);
	if (currentItem != null) {
		redrawY = getVisibleRedrawY(currentItem);
		if (redrawY != -1) {
			currentItem.redrawInsertMark(redrawY);
		}		
	}
	if (item != null) {
		redrawY = getVisibleRedrawY(item);
		if (redrawY != -1) {
			item.redrawInsertMark(redrawY);
		}		
	}
}


/**
 * Overridden to implement setRedraw(). Redraw is ignored if setRedraw was 
 * set to false.
 */
public void redraw () {
	checkWidget();
	if (drawCount == 0) {
		super.redraw();
	}
}
/**
 * Overridden to implement setRedraw(). Redraw is ignored if setRedraw was 
 * set to false.
 */
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (drawCount == 0) {
		super.redraw(x, y, width, height, all);
	}
}

/**
 * Redraw the selection of 'item'
 * @param item - SelectableItem that should have the selection redrawn.
 */
void redrawSelection(SelectableItem item) {
	int redrawPosition = getVisibleRedrawY(item);
	if (redrawPosition != -1) {
		item.redrawSelection(redrawPosition);
	}	
}
/**
 * 'item' has been removed from the receiver. 
 * Update the display and the scroll bars.
 */
void removedItem(SelectableItem item) {
	claimBottomFreeSpace();
	calculateVerticalScrollbar();
	if (getItemCount() == 0) {
		reset();
	}
}
/**
 * 'item' is about to be removed from the tree.
 * Move the selection/input focus if 'item' is selected or has the 
 * input focus, 
 * @param item - item that is about to be removed from the tree.
 */
void removingItem(SelectableItem item) {
	SelectableItem nextFocusItem = null;
	int itemIndex = getVisibleIndex(item);
	int itemCount = getVisibleItemCount();
	
	// deselect item and remove from selection
	if (item.isSelected() == true) {
		getSelectionVector().removeElement(item);
	}
	if (item == getLastFocus() && itemCount > 1) {
		// select previous item if removed item is bottom item
		// otherwise select next item. Fixes 1GA6L85
		if (itemIndex == itemCount - 1) {
			nextFocusItem = getVisibleItem(itemIndex - 1);
		}
		else {
			nextFocusItem = getVisibleItem(itemIndex + 1);
		}
		setLastFocus(nextFocusItem, true);
	}
	// ignore items below widget client area
	if (itemIndex != -1 && itemIndex <= getBottomIndex()) {			
		scrollVerticalRemovedItem(itemIndex);
	}
}
/**
 * Reset state that is dependent on or calculated from the state 
 * of the receiver.
 */
void reset() {
	setSelectionVector(new Vector());
	setTopIndexNoScroll(0, true);
	lastSelectedItem = null;
	lastFocusItem = null;
	resetItemData();
}
/**
 * Reset state that is dependent on or calculated from the items
 * of the receiver.
 */
void resetItemData() {
	setHorizontalOffset(0);
	setItemHeight(0);
	itemImageExtent = null;
	textHeight = -1;	
	claimRightFreeSpace();
}
/**
 * The receiver has been resized. Update the scroll bars and
 * make sure that new space is being occupied by items.
 */
void resize(Event event) {
	int horizontalPageSize = getHorizontalBar().getPageIncrement();

	resizeHorizontalScrollbar();
	resizeVerticalScrollbar();
	if (getClientArea().width > horizontalPageSize) {		// window resized wider? - Do this check here 
		claimRightFreeSpace();							// because claimRightFreeSpace is called elsewhere.
	}
	claimBottomFreeSpace();
}
/**
 * Display the horizontal scroll bar if items are drawn off 
 * screen. Update the page size.
 */
void resizeHorizontalScrollbar() {
	ScrollBar horizontalBar = getHorizontalBar();
	int clientAreaWidth = getClientArea().width;

	if (clientAreaWidth < getContentWidth()) {
		if (horizontalBar.getVisible() == false) {
			horizontalBar.setVisible(true);
			horizontalBar.setSelection(0);
		}
	}
	else 
	if (horizontalBar.getVisible() == true) {
		horizontalBar.setVisible(false);
	}
	horizontalBar.setThumb(clientAreaWidth);
	horizontalBar.setPageIncrement(clientAreaWidth);
}
/**
 * Display the vertical scroll bar if items are drawn off screen.
 * Update the page size.
 */
void resizeVerticalScrollbar() {
	int clientAreaItemCount = getItemCountWhole();
	ScrollBar verticalBar = getVerticalBar();

	if (clientAreaItemCount == 0) {
		return;
	}
	if (clientAreaItemCount < getVisibleItemCount()) {
		if (verticalBar.getVisible() == false) {
			verticalBar.setVisible(true);
		}
		// Only set the page size to something smaller than the scroll 
		// range maximum. Otherwise the scroll selection will be reset.
		verticalBar.setPageIncrement(clientAreaItemCount);
		verticalBar.setThumb(clientAreaItemCount);
	}
	else
	if (verticalBar.getVisible() == true) {
		verticalBar.setVisible(false);
	}
}
/**
 * Scroll the rectangle specified by x, y, width, height to the destination 
 * position. Do nothing if redraw is set to false using setRedraw().
 *
 * @param destX - destination x position of the scrolled rectangle
 * @param destY - destination y position of the scrolled rectangle
 * @param x - x location of the upper left corner of the scroll rectangle
 * @param y - y location of the upper left corner of the scroll rectangle
 * @param width - width of the scroll rectangle
 * @param height - height of the scroll rectangle
 * @param all - not used. Used to be true=scroll children intersecting the 
 *	scroll rectangle.
 */
void scroll(int destX, int destY, int x, int y, int width, int height, boolean all) {
	if (drawCount == 0) {
		update();
		GC gc = new GC(this);
		gc.copyArea(x, y, width, height, destX, destY);
		gc.dispose();
	}
}
/**
 * Scroll horizontally by 'numPixel' pixel.
 * @param numPixel - the number of pixel to scroll
 * 	numPixel > 0 = scroll to left. numPixel < 0 = scroll to right
 */
abstract void scrollHorizontal(int numPixel);
/**
 * The position of the horizontal scroll bar has been modified 
 * by the user. 
 * Adjust the horizontal offset to trigger a horizontal scroll.
 * @param event-the scroll event
 */
void scrollHorizontal(Event event) {
	setHorizontalOffset(getHorizontalBar().getSelection() * -1);
}
/**
 * Scroll 'item' into the receiver's client area if necessary.
 * @param item - the item that should be scrolled.
 */
void scrollShowItem(SelectableItem item) {
	int itemIndexFromTop = getIndex(item) - getTopIndex();
	int clientAreaWholeItemCount = getItemCountWhole();
	int scrollAmount = 0;

	if (itemIndexFromTop >= clientAreaWholeItemCount) {		// show item below visible items?
		scrollAmount = itemIndexFromTop;
		if (clientAreaWholeItemCount > 0) {					// will be 0 if showItem is called and receiver hasn't been displayed yet
			scrollAmount -= clientAreaWholeItemCount - 1;
		}
	}
	else
	if (itemIndexFromTop < 0) {								// show item above visible items?
		scrollAmount = itemIndexFromTop;
	}
	setTopIndex(getTopIndex() + scrollAmount, true);
}
/**
 * Scroll vertically by 'scrollIndexCount' items.
 * @param scrollIndexCount - the number of items to scroll.
 *	scrollIndexCount > 0 = scroll up. scrollIndexCount < 0 = scroll down
 */
abstract void scrollVertical(int scrollIndexCount);
/**
 * The position of the horizontal scroll bar has been modified 
 * by the user.
 * Adjust the index of the top item to trigger a vertical scroll.
 * @param event-the scroll event
 */
void scrollVertical(Event event) {
	setTopIndex(getVerticalBar().getSelection(), false);
}
/**
 * Scroll items down to make space for a new item added to 
 * the receiver at position 'index'.
 * @param index - position at which space for one new item
 *	should be made. This index is relative to the first item 
 *	of the receiver.
 */
void scrollVerticalAddingItem(int index) {
	Rectangle clientArea = getClientArea();
	int itemHeight = getItemHeight();
	int sourceY = Math.max(0, (index - getTopIndex()) * itemHeight);	// need to scroll in visible area

	scroll(
		0, sourceY + itemHeight, 						// destination x, y
		0, sourceY, 									// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Scroll the items below the item at position 'index' up 
 * so that they cover the removed item.
 * @param index - index of the removed item
 */
void scrollVerticalRemovedItem(int index) {
	Rectangle clientArea = getClientArea();
	int itemHeight = getItemHeight();
	int destinationY = Math.max(0, (index - getTopIndex()) * itemHeight);

	scroll(
		0, destinationY, 						// destination x, y
		0, destinationY + itemHeight, 			// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Select 'item' if it is not selected.
 * @param item - item that should be selected
 */
void select(SelectableItem item) {
	Vector selectedItems = getSelectionVector();
	
	if (item != null && item.isSelected() == false && isRemovingAll() == false) {
		item.setSelected(true);
		redrawSelection(item);
		selectedItems.addElement(item);
	}
}
/**
 * Select 'item' if it is not selected. Send a Selection event 
 * if the selection was changed.
 * @param item - item that should be selected
 * @param asyncNotify 
 *  true=send the selection event asynchronously
 *  false=send the selection event immediately
 */
void selectNotify(final SelectableItem item, boolean asyncNotify) {
	if (isRemovingAll() == false) {
		if (item.isSelected() == false) {
			select(item);
			setLastSelection(item, true);
			update();												// looks better when event notification takes long to return
		}
		if (asyncNotify == false) {
			Event event = new Event();
			event.item = item;
			notifyListeners(SWT.Selection, event);
		}
		else {
			getDisplay().asyncExec(new Runnable() {
				public void run() {
					// Only send a selection event when the item has not been disposed.
					// Fixes 1GE6XQA
					if (item.isDisposed() == false) {
						Event event = new Event();
						event.item = item;
						notifyListeners(SWT.Selection, event);
					}
				}
			});
		}
	}
}
/**
 * Select 'item' if it is not selected. Send a Selection event 
 * if the selection was changed.
 * @param item - item that should be selected
 */
void selectNotify(SelectableItem item) {
	selectNotify(item, false);
}
/**
 * Select all items of the receiver starting at 'fromIndex' 
 * and including 'toIndex'.
 */
void selectRange(int fromIndex, int toIndex) {
	if (fromIndex > toIndex) {
		for (int i = fromIndex; i > toIndex; i--) {
			select(getVisibleItem(i));
		}
	}
	else {
		for (int i = fromIndex; i < toIndex; i++) {
			select(getVisibleItem(i));			
		}
	}
	selectNotify(getVisibleItem(toIndex));				// select last item, notifying listeners
}
/**
 * Set the width of the receiver's contents to 'newWidth'.
 * Content width is used to calculate the horizontal scrollbar.
 */
void setContentWidth(int newWidth) {
	ScrollBar horizontalBar;
	boolean scrollBarVisible;
	
	if (contentWidth != newWidth) {
		horizontalBar = getHorizontalBar();
		scrollBarVisible = horizontalBar.getVisible();
		contentWidth = newWidth;
		calculateHorizontalScrollbar();
		if (scrollBarVisible != horizontalBar.getVisible()) {
			resizeVerticalScrollbar();									// the vertical scroll bar needs to be resized if the horizontal 
		}																// scroll bar was hidden or made visible during recalculation
	}
}
/**
 * Set whether the currently selected items were selected using the 
 * ctrl key.
 * @param isCtrlSelection - 
 *	true = currently selected items were selected using the ctrl key.
 *	false = currently selected items were not selected using the 
 *	ctrl key.
 */
void setCtrlSelection(boolean isCtrlSelection) {
	this.isCtrlSelection = isCtrlSelection;
}
/**
 * The font is changing. Reset the text height to force a 
 * recalculation during itemChanged().
 */
public void setFont(Font font) {
	checkWidget();
	super.setFont(font);
	textHeight = -1;
}
/**
 * Set the horizontal drawing offset to 'offset'.
 * Scroll the receiver's contents according to the offset change.
 * @param offset - value < 0 = widget contents is drawn left of the client area.
 */
void setHorizontalOffset(int offset) {
	int offsetChange = offset - horizontalOffset;
	if (offsetChange != 0) {
		scrollHorizontal(offsetChange);
		horizontalOffset = offset;
	}
}

/**
 * Set whether the drop insert position is before or after the 
 * item set using motif_setInsertMark.
 * @param after true=insert position is after the insert item
 *	false=insert position is before the insert item
 */
void setInsertAfter(boolean after) {
	isInsertAfter = after;
}

/**
 * Set the item that draws the marker indicating the insert location 
 * in a drag and drop operation.
 * @param item the item that draws the insert marker
 */
void setInsertItem(SelectableItem item) {
	insertItem = item;
}
/** 
 * Set the height of the receiver's items to 'height'.
 */
void setItemHeight(int height) {
	itemHeight = height;
}
/**
 * Set the item that most recently received the input focus
 * to 'focusItem'. Redraw both, the item that lost focus 
 * and the one that received focus.
 * @param focusItem - the item that most recently received 
 *	the input focus
 * @param showItem true=new focus item, if any, should be scrolled 
 *	into view. false=don't scroll
 */
void setLastFocus(SelectableItem focusItem, boolean showItem) {
	SelectableItem oldFocusItem = lastFocusItem;
	
	if (focusItem != lastFocusItem) {
		lastFocusItem = focusItem;	
		if (oldFocusItem != null) {
			redrawSelection(oldFocusItem);
		}
		if (lastFocusItem != null && isFocusControl() == true) {
			redrawSelection(lastFocusItem);
		}
	}
	if (focusItem != null && showItem == true) {
		showSelectableItem(focusItem);
	}
}
/**
 * Set the item that was selected most recently to 'selectedItem'.
 * Set the input focus to that item.
 * @param selectedItem - the item that was selected most recently
 * @param showItem true=new focus item, if any, should be scrolled 
 *	into view. false=don't scroll
 */ 
void setLastSelection(SelectableItem selectedItem, boolean showItem) {
	if (selectedItem == null) {							// always store the item selected last
		return;
	}
	setLastFocus(selectedItem, showItem);
	lastSelectedItem = selectedItem;
}
/**
 * Sets the redraw flag.
 * @param redraw - 
 *	true = redraw and scroll operations are performed normally
 * 	false = redraw and scroll operations are ignored
 */
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) redraw();
	} else {
		drawCount++;
	}
}
/**
 * Set whether all items in the widget are disposed.
 * Used to optimize item disposal. Prevents unnecessary screen 
 * updates.
 * @param removingAll 
 *	true=all items are removed. 
 *	false=normal state, no items or not all items are removed.
 */
void setRemovingAll(boolean removingAll) {
	isRemovingAll = removingAll;
}
/**
 * Select the items stored in 'selectionItems'. 
 * A SWT.Selection event is not going to be sent.
 * @param selectionItems - Array containing the items that should 
 *	be selected
 */
void setSelectableSelection(SelectableItem selectionItems[]) {
	SelectableItem item = null;
	int selectionCount = selectionItems.length;
	Vector keepSelected;
	
	if (isMultiSelect() == false && selectionCount > 1) {
		selectionCount = 1;
	}
	keepSelected = new Vector(selectionItems.length);
	for (int i = 0; i < selectionCount; i++) {
		if (selectionItems[i] != null) {
			if (selectionItems[i].isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			keepSelected.addElement(selectionItems[i]);
		}
	}
	deselectAllExcept(keepSelected);
	// select in the same order as all the other selection and deslection methods.
	// Otherwise setLastSelection repeatedly changes the lastSelectedItem for repeated 
	// selections of the items, causing flash.	
	for (int i = selectionCount - 1; i >= 0; i--) {
		item = selectionItems[i];
		if (item != null) {
			select(item);
		}
	}
	if (item != null) {
		setLastSelection(item, true);
	}	
}
/**
 * Set the vector used to store the selected items of the receiver 
 * to 'newVector'.
 * @param newVector - the vector used to store the selected items 
 *	of the receiver
 */
void setSelectionVector(Vector newVector) {
	selectedItems = newVector;
}
/**
 * Scroll the item at 'index' to the top.
 * @param index - 0-based index of the first visible item in the 
 *	receiver's client area.
 * @param adjustScrollbar - true=set the position of the vertical 
 *	scroll bar to the new top index. 
 *	false=don't adjust the vertical scroll bar
 */
void setTopIndex(int index, boolean adjustScrollbar) {
	int indexDiff = index-topIndex;

	if (indexDiff != 0) {
		scrollVertical(indexDiff);
		setTopIndexNoScroll(index, adjustScrollbar);
	}
}
/**
 * Set the index of the first visible item in the receiver's client 
 * area to 'index'.
 * @param index - 0-based index of the first visible item in the 
 *	receiver's client area.
 * @param adjustScrollbar - true=set the position of the vertical 
 *	scroll bar to the new top index. 
 *	false=don't adjust the vertical scroll bar
 */
void setTopIndexNoScroll(int index, boolean adjustScrollbar) {
	topIndex = index;
	if (adjustScrollbar == true) {
		getVerticalBar().setSelection(index);
	}
}
/**
 * The shift key was pressed when the mouse click on an item 
 * occurred. Do a shift selection. If an already selected item was 
 * clicked the selection is expanded/reduced to that item
 * @param hitItem - specifies the clicked item
 * @param hitItemIndex - specifies the index of the clicked item 
 *	relative to the first item.
 */
void shiftSelect(SelectableItem hitItem, int hitItemIndex) {
	int fromIndex = -1;
	int toIndex = -1;
	int lastSelectionIndex = -1;
	int selectionRange[];
	SelectableItem lastSelection = getLastSelection();

	if (lastSelection != null) {
		lastSelectionIndex = getVisibleIndex(lastSelection);
	}
	if (isCtrlSelection() == true) {						// was last selection ctrl selection? 
		deselectAllExcept(lastSelection);					
		fromIndex = lastSelectionIndex;						// select from last selection
		toIndex = hitItemIndex;
	}
	else
	if (getSelectionVector().contains(hitItem) == true) {	// clicked an item already selected?
		deselectRange(hitItemIndex, lastSelectionIndex);	// reduce selection
	}
	else {													// clicked outside existing selection range
		selectionRange = calculateShiftSelectionRange(hitItemIndex);
		fromIndex = selectionRange[0];
		toIndex = selectionRange[1];
	}
	if (hitItemIndex == lastSelectionIndex) {				// was click on last selected item?
		return;
	}
	if (fromIndex == -1 || toIndex == -1) { 				// are there previously selected items?
		toggleSelectionNotify(hitItem);						// do a single select.
	}
	else {
		if (((lastSelectionIndex < fromIndex) && (hitItemIndex > fromIndex)) ||	// does selection reverse direction?
			((lastSelectionIndex > fromIndex) && (hitItemIndex < fromIndex))) {
			deselectAllExcept((SelectableItem) null);											// remove old selection 
		}
		selectRange(fromIndex, toIndex);
	}					
}
/**
 * Make 'item' visible by scrolling it into the receiver's client
 * area if necessary.
 * @param item - the item that should be made visible to the user.
 */
void showSelectableItem(SelectableItem item) {
	if (item.getSelectableParent() != this) {
		return;
	}
	scrollShowItem(item);
	scrollShowItem(item);						// second call makes sure that the item is still visible
												// even if the first scroll caused the horizontal scroll
												// to be displayed and the item to be hidden again.
}
/**
 * Show the selection. If there is no selection or the 
 * selection is already visible, this method does nothing. 
 * If the selection is not visible, the top index of the 
 * widget is changed such that the selection becomes visible.
 */
public void showSelection() {
	checkWidget();
	Vector selection = getSelectionVector();
	SelectableItem selectionItem;

	if (selection.size() > 0) {
		selectionItem = (SelectableItem) selection.firstElement();
		showSelectableItem(selectionItem);
	}
}
/**
 * Sorts the specified range in the array.
 *
 * @param array the SelectableItem array to be sorted
 * @param start the start index to sort
 * @param end the last + 1 index to sort
 */
void sort(SelectableItem[] array, int start, int end) {
	int middle = (start + end) / 2;
	if (start + 1 < middle) sort(array, start, middle);
	if (middle + 1 < end) sort(array, middle, end);
	if (start + 1 >= end) return;	// this case can only happen when this method is called by the user
	if (getVisibleIndex(array[middle-1]) <= getVisibleIndex(array[middle])) return;
	if (start + 2 == end) {
		SelectableItem temp = array[start];
		array[start] = array[middle];
		array[middle] = temp;
		return;
	}
	int i1 = start, i2 = middle, i3 = 0;
	SelectableItem[] merge = new SelectableItem[end - start];
	while (i1 < middle && i2 < end) {
		merge[i3++] = getVisibleIndex(array[i1]) <= getVisibleIndex(array[i2]) ?
			array[i1++] : array[i2++];
	}
	if (i1 < middle) System.arraycopy(array, i1, merge, i3, middle - i1);
	System.arraycopy(merge, 0, array, start, i2 - start);
}
/**
 * Toggle the selection of 'item'.
 * @param item - item that should be selected/deselected
 */
void toggleSelectionNotify(SelectableItem item) {
	if (item.isSelected() == true) {
		deselectNotify(item);
	}
	else {
		selectNotify(item);
	}
}
}

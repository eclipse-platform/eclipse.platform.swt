package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.util.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/** 
 * This class stores and manages child items of a tree item.
 * It provides protocol to query the index of an item relative 
 * to the root and to retrieve items by index.
 * The TreeItem class implements this protocol for general
 * tree items.
 * TreeRoots provides a special implementation that allows the 
 * Tree class to treat trees with one root and with multiple 
 * roots equally.
 */
abstract class AbstractTreeItem extends SelectableItem {
	private Vector children;
	private boolean isExpanded = false;		
	// number of children. 
	// includes all expanded items down to the leafs.
	private int visibleItemCount = 0;

/**
 * Create a new instance of the receiver.
 * @param parent - widget the receiver belongs to
 * @param swtStyle - widget style. see Widget class for details
 */
AbstractTreeItem(Tree parent, int swtStyle) {
	super(parent, swtStyle);
}
/**
 * Insert 'item' in the list of child items. Notify the 
 * parent about the new item.
 * @param 'item' - the item that should be added to the 
 *	receiver's children.
 * @param index - position that 'item' will be inserted at
 *	in the receiver.
 */
void add(TreeItem item, int index) {
	Vector items = getChildren();
	int visibleIndex = getVisibleIndex();
	
	if (index < 0 || index > items.size()) {
		error(SWT.ERROR_INVALID_RANGE);
	}
	if (item.isRoot()) {
		visibleIndex = index;
	}
	else
	if (isExpanded == false) {
		visibleIndex = -1;
	}
	if (visibleIndex != -1) {
		if (index > 0) {
			TreeItem previousChild = (TreeItem) getChildren().elementAt(index - 1);
			visibleIndex = previousChild.getVisibleIndex() + previousChild.getVisibleItemCount() + 1;
		}
		else {
			 visibleIndex = getVisibleIndex() + 1;
		}
	}
	getSelectableParent().addingItem(item, visibleIndex);
	item.setIndex(index);
	resetChildIndices(index, true);
	items.insertElementAt(item, index);
	if (isExpanded == true) {
		visibleItemCount++;
		calculateVisibleItemCountParent();
	}
	getSelectableParent().addedItem(item, visibleIndex);
}
/** 
 * Set whether the receiver is expanded or not. 
 * If the receiver is expanded its child items are visible.
 * @param expanded - 
 *	true=the receiver is expanded, making its child items visible.
 * 	false=the receiver is collapsed, making its child items invisible 
 */
void internalSetExpanded(boolean expanded) {
	isExpanded = expanded;
	calculateVisibleItemCount();
}
/**
 * Calculate the number of expanded children.
 * Recurse up in the tree to the root item.
 */
abstract void calculateVisibleItemCount();
/**
 * Calculate the number of expanded children for the parent item
 * of this item.
 */
abstract void calculateVisibleItemCountParent();
/**
 * Deselect the receiver and all children
 */
void deselectAll() {
	Enumeration children = getChildren().elements();
	AbstractTreeItem treeItem;

	setSelected(false);
	while (children.hasMoreElements() == true) {
		treeItem = (AbstractTreeItem) children.nextElement();
		treeItem.deselectAll();
	}
}
/** 
 * Destroy all children of the receiver	
 */
void disposeItem() {
	Vector children = getChildren();
	AbstractTreeItem child;
	while (children.size() > 0) {		// TreeItem objects are removed from vector during dispose
		child = (AbstractTreeItem) children.firstElement();
		child.dispose();
	}
	doDispose();
	super.disposeItem();
}
/**
 * Subclasses should free resources here
 */
void doDispose() {
	setChildren(null);
	visibleItemCount = 0;
}
/**
 * Answer the Vector containing the child items of the receiver.
 */
Vector getChildren() {
	if (children == null) {
		children = new Vector(4);
	}
	return children;
}
/**
 * Answer whether the receiver is expanded or not. 
 * If the receiver is expanded its children are visible.
 * @return 
 *	true - the receiver is expanded, making its children visible
 * 	false - the receiver is collapsed, making its children invisible 
 */
public boolean getExpanded() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	return isExpanded;
}
/**
 * Answer the number of children.
 */
public int getItemCount() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	return getChildren().size();
}
/**
 * Answer the index of the receiver relative to the first root 
 * item.
 * If 'anIndex' is the global index of the expanded item 'anItem' 
 * then the following expressions are true:
 * 'anItem  == theRoot.getVisibleItem(anIndex)' and
 * 'anIndex == anItem.getVisibleIndex()'
 * @return
 *	The index of the receiver relative to the first root item.
 *	Answer -1 if the receiver is not visible (because the parent 
 *	is collapsed).
 */
abstract int getVisibleIndex();
/**
 * Answer the index of the child item identified by 'childIndex' 
 * relative to the first root item.
 */
abstract int getVisibleIndex(int childIndex);
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
 * visible on screen if it is within the receiver's parent's 
 * client area.
 */
abstract TreeItem getVisibleItem(int searchIndex);
/**
 * Answer the number of expanded children, direct and indirect.
 */
int getVisibleItemCount() {
	return visibleItemCount;
}
/**
 * Returns the expanded state. Circumvent widget/thread check 
 * for performance. For non-API callers only.
 */
boolean internalGetExpanded() {
	return isExpanded;
}
/**
 * Answer whether the receiver is a leaf item.
 * An item is a leaf when it has no child items.
 * @return
 *	true - receiver is a leaf item
 *	false - receiver is not a leaf item.
 */
boolean isLeaf() {
	return (getChildren().size() == 0);
}
/**
 * Answer whether the receiver is a root item.
 * The receiver is a root item when it doesn't have a parent item.
 * @return 
 *	true - the receiver is a root item.
 * 	false - the receiver is not a root item.
 */
boolean isRoot() {
	return false;
}
/** 
 * Remove 'child' from the receiver. 
 * Notify the parent widget only if it is not being disposed itself.
 */
void removeItem(SelectableItem child) {
	Vector children = getChildren();
	SelectableItemWidget parent = getSelectableParent();
	int childIndex = children.indexOf(child);

	if (childIndex != -1) {
		if (((Tree) parent).isRemovingAll() == true) {
			children.removeElementAt(childIndex);		// just remove the item from the list if the whole tree is being disposed
			if (isExpanded == true) {
				visibleItemCount--;
				calculateVisibleItemCountParent();
			}
		}
		else {
			parent.removingItem(child);	
			children.removeElementAt(childIndex);
			if (isExpanded == true) {
				visibleItemCount--;
				calculateVisibleItemCountParent();
			}
			resetChildIndices(childIndex, false);						// mark child index dirty
			parent.removedItem(child);
		}
	}
}
/**
 * Allow subclasses to reset any cached data.
 * Called for all children of the receiver.
 */
void reset() {
	Enumeration children = getChildren().elements();
	AbstractTreeItem treeItem;

	while (children.hasMoreElements() == true) {
		treeItem = (AbstractTreeItem) children.nextElement();
		treeItem.reset();
	}
}
/**
 * Mark all child indices dirty starting with the child at 
 * 'startIndex'. This causes getIndex to recalculate the index.
 * @param startIndex - index in the list of children at which 
 *	and after which the indices are reset.
 */
void resetChildIndices(int startIndex, boolean addItem) {
	Vector children = getChildren();
	TreeItem child;
	int increment = addItem ? 1 : 0;

	for (int i = startIndex; i < children.size(); i++) {
		child = (TreeItem) children.elementAt(i);
		child.setIndex(i + increment);								// mark child index dirty
	}
}
/**
 * Select the receiver and all children.
 * Return a Vector containing all the items that have been selected 
 * (and that have not been selected before).
 */
Vector selectAll(Vector selectedItems) {
	Enumeration children = getChildren().elements();
	AbstractTreeItem treeItem;

	if (isSelected() == false) {
		selectedItems.addElement(this);
		setSelected(true);
		getSelectableParent().redrawSelection(this);
	}
	while (children.hasMoreElements() == true) {
		treeItem = (AbstractTreeItem) children.nextElement();
		selectedItems = treeItem.selectAll(selectedItems);
	}
	return selectedItems;
}
/**
 * Set the Array containing the receiver's child items to 'children'.
 */
void setChildren(Vector children) {
	this.children = children;
}

void setVisibleItemCount(int count) {
	visibleItemCount = count;
}
}

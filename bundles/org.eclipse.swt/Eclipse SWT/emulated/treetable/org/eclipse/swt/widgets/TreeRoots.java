package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.graphics.*;
import java.util.*;
 
/** 
 * This class is used to store tree root items.
 * Instances of this class are never displayed.
 */
class TreeRoots extends AbstractTreeItem {
/**
 * Create a tree item that holds one or more root items 
 * @param parent - Tree widget the receiver belongs to
 */
TreeRoots(Tree parent) {
	super(parent, 0);
	initialize();
}
/**
 * Calculate the number of expanded children.
 * Recurse up in the tree to the root item.
 */
void calculateVisibleItemCount() {
	Vector children = getChildren();
	TreeItem child;
	int visibleItemCount = children.size();
	
	for (int i = 0; i < children.size(); i++) {
		child = (TreeItem) children.elementAt(i);
		visibleItemCount += child.getVisibleItemCount();
	}
	setVisibleItemCount(visibleItemCount);
}
/**
 * Calculate the number of expanded children for the parent item
 * of this item.
 */
void calculateVisibleItemCountParent() {}

public void dispose() {
	if (isDisposed()) return;
	Tree parent = (Tree) getSelectableParent();
	
	// all tree items are removed so we don't need to do
	// time consuming screen updates for each removed item
	parent.setRemovingAll(true);
	super.dispose();
	parent.setRemovingAll(false);
}
/**
 * Answer the x position of the item check box
 */
int getCheckboxXPosition() {
	return 0;
}
/**
 * Implements SelectableItem#getSelectionExtent
 * Should never be called since objects of this type are never 
 * rendered
 */
Point getSelectionExtent() {
	return new Point(0, 0);
}
/**
 * Implements SelectableItem#getSelectionX
 * Should never be called since objects of this type are never 
 * rendered
 */
int getSelectionX() {
	return 0;
}
/**
 * Always answer -1 to indicate that the receiver is not visible.
 */
int getVisibleIndex() {
	return -1;		
}
/**
 * Answer the index of the child item identified by 'childIndex' 
 * relative to the first root item.
 */
int getVisibleIndex(int childIndex) {
	Enumeration children = getChildren().elements();
	TreeItem child;
	int globalItemIndex = 0;

	while (children.hasMoreElements() == true) {
		child = (TreeItem) children.nextElement();
		if (child.getIndex() == childIndex) {
			break;
		}
		globalItemIndex += child.getVisibleItemCount();		
	}	
	return globalItemIndex;
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

	searchIndex++;						// skip this fake root item

	// Search for expanded items first. Count all subitems in the process.
	while (children.hasMoreElements() == true && foundItem == null) {
		child = (TreeItem) children.nextElement();
		searchIndex--;
		if (child.internalGetExpanded() == true) {
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
 * Initialize the receiver
 */
void initialize() {
	internalSetExpanded(true);
}

/**
 * Select the receiver and all children
 */
Vector selectAll(Vector selectedItems) {
	Enumeration children = getChildren().elements();
	AbstractTreeItem treeItem;

	while (children.hasMoreElements() == true) {
		treeItem = (AbstractTreeItem) children.nextElement();
		selectedItems = treeItem.selectAll(selectedItems);
	}
	return selectedItems;
}
}

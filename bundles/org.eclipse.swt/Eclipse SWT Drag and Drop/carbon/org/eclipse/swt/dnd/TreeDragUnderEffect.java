/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

 
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TreeDragUnderEffect extends DragUnderEffect {

	Tree tree;
	TreeItem currentItem = null;
	int currentEffect = DND.FEEDBACK_NONE;
	TreeItem[] selection = new TreeItem[0];
	private TreeItem scrollItem;	
	private long scrollBeginTime;
	private TreeItem expandIndex;
	private long expandBeginTime;
	
	private static final int SCROLL_HYSTERESIS = 150; // milli seconds
	private static final int EXPAND_HYSTERESIS = 300; // milli seconds

TreeDragUnderEffect(Tree tree) {
	this.tree = tree;
}
int checkEffect(int effect) {
	// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
	if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
	if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
	return effect;
}
Widget getItem(int x, int y) {
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	TreeItem item = tree.getItem(coordinates);
	if (item == null) {
		Rectangle area = tree.getClientArea();
		if (area.contains(coordinates)) {
			// Scan across the width of the tree.
			for (int x1 = area.x; x1 < area.x + area.width; x1++) {
				Point pt = new Point(x1, coordinates.y);
				item = tree.getItem(pt);
				if (item != null) {
					break;
				}
			}
		}
	}
	return item;
}
TreeItem nextItem(TreeItem item) {
	if (item == null) return null;
	if (item.getExpanded()) {
		return item.getItem(0);
	} else {
		TreeItem childItem = item;
		TreeItem parentItem = childItem.getParentItem();
		int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
		int count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
		while (true) {
			if (index + 1 < count) {
				return parentItem == null ? tree.getItem(index + 1) : parentItem.getItem(index + 1);
			} else {
				if (parentItem == null) {
					return null;
				} else {
					childItem = parentItem;
					parentItem = childItem.getParentItem();
					index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
					count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
				}
			}
		}
	}
}
TreeItem previousItem(TreeItem item) {
	if (item == null) return null;
	TreeItem childItem = item;
	TreeItem parentItem = childItem.getParentItem();
	int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
	if (index == 0) {
		return parentItem;
	} else {
		TreeItem nextItem = parentItem == null ? tree.getItem(index-1) : parentItem.getItem(index-1);
		int count = nextItem.getItemCount();
		while (count > 0 && nextItem.getExpanded()) {
			nextItem = nextItem.getItem(count - 1);
			count = nextItem.getItemCount();
		}
		return nextItem;
	}
}
void show(int effect, int x, int y) {
	effect = checkEffect(effect);
	TreeItem item = (TreeItem)getItem(x, y);
	if ((effect & DND.FEEDBACK_EXPAND) == 0) {
		expandBeginTime = 0;
		expandIndex = null;
	} else {
		if (item != null && item.equals(expandIndex) && expandBeginTime != 0) {
			if (System.currentTimeMillis() >= expandBeginTime) {
				item.setExpanded(true);
				expandBeginTime = 0;
				expandIndex = null;
			}
		} else {
			expandBeginTime = System.currentTimeMillis() + EXPAND_HYSTERESIS;
			expandIndex = item;
		}
	}
	
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollItem = null;
	} else {
		if (item != null && item.equals(scrollItem)  && scrollBeginTime != 0) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				Rectangle area = tree.getClientArea();
				int headerHeight = tree.getHeaderHeight();
				int itemHeight= tree.getItemHeight();
				Point pt = new Point(x, y);
				pt = tree.getDisplay().map(null, tree, pt);
				TreeItem nextItem = null;
				if (pt.y < area.y + headerHeight + 2 * itemHeight) {
					nextItem = previousItem(item);
				}
				if (pt.y > area.y + area.height - 2 * itemHeight) {
					nextItem = nextItem(item);
				}
				if (nextItem != null) tree.showItem(nextItem);
				scrollBeginTime = 0;
				scrollItem = null;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollItem = item;
		}
	}
	
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		if ((currentEffect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
		    (currentEffect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
			setInsertMark(null, false);
			currentEffect = DND.FEEDBACK_NONE;
			currentItem = null;
		}
		if (currentEffect != effect || currentItem != item) { 
			setDropSelection(item); 
			currentEffect = DND.FEEDBACK_SELECT;
			currentItem = item;
		}
	}
	
	if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
		(effect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
		if (currentEffect == DND.FEEDBACK_SELECT) {
			setDropSelection(null);
			currentEffect = DND.FEEDBACK_NONE;
			currentItem = null;
		}
		if (currentEffect != effect || currentItem != item) { 
			setInsertMark(item, effect == DND.FEEDBACK_INSERT_AFTER);
			currentEffect = effect;
			currentItem = item;
		}
	}
}
void setDropSelection (TreeItem item) {
	if (item == null) {
		tree.setSelection(new TreeItem[0]);
	} else {
		tree.setSelection(new TreeItem[]{item});
	}
}
void setInsertMark (TreeItem item, boolean after) {
	// not currently implemented
}
}

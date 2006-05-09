/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TreeDragAndDropEffect extends DragAndDropEffect {
	Tree tree;
	
	int currentEffect = DND.FEEDBACK_NONE;
	TreeItem currentItem;
	
	PaintListener paintListener;
	TreeItem dropSelection = null;
	
	TreeItem insertItem = null;
	boolean insertBefore = false;

	TreeItem scrollItem;
	long scrollBeginTime;

	TreeItem expandItem;
	long expandBeginTime;
	
	static final int SCROLL_HYSTERESIS = 150; // milli seconds
	static final int EXPAND_HYSTERESIS = 300; // milli seconds

TreeDragAndDropEffect(Tree tree) {
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
	if (item.getExpanded()) return item.getItem(0);
	TreeItem childItem = item;
	TreeItem parentItem = childItem.getParentItem();
	int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
	int count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
	while (true) {
		if (index + 1 < count) return parentItem == null ? tree.getItem(index + 1) : parentItem.getItem(index + 1);
		if (parentItem == null) return null;
		childItem = parentItem;
		parentItem = childItem.getParentItem();
		index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
		count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
	}
}
TreeItem previousItem(TreeItem item) {
	if (item == null) return null;
	TreeItem childItem = item;
	TreeItem parentItem = childItem.getParentItem();
	int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
	if (index == 0) return parentItem;
	TreeItem nextItem = parentItem == null ? tree.getItem(index-1) : parentItem.getItem(index-1);
	int count = nextItem.getItemCount();
	while (count > 0 && nextItem.getExpanded()) {
		nextItem = nextItem.getItem(count - 1);
		count = nextItem.getItemCount();
	}
	return nextItem;
}
void setDropSelection (TreeItem item) {	
	if (item == dropSelection) return;
	if (dropSelection != null && !dropSelection.isDisposed()) {
		Rectangle bounds = dropSelection.getBounds();
		tree.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
	}
	dropSelection = item;
	if (dropSelection != null && !dropSelection.isDisposed()) {
		Rectangle bounds = dropSelection.getBounds();
		tree.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
	}
	if (dropSelection == null) {
		if (paintListener != null) {
			tree.removePaintListener(paintListener);
			paintListener = null;
		}
	} else {
		if (paintListener == null) {
			paintListener = new PaintListener() {
				public void paintControl(PaintEvent e) {
					if (dropSelection == null  || dropSelection.isDisposed()) return;
					GC gc = e.gc;
					boolean xor = gc.getXORMode();
					gc.setXORMode(true);
					Rectangle bounds = dropSelection.getBounds();
					gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
					gc.setXORMode(xor);
				}
			};
			tree.addPaintListener(paintListener);
		}
	}
}
void setInsertMark(TreeItem item, boolean before) {
	if (item == insertItem && before == insertBefore) return;
	insertItem = item;
	insertBefore = before;
	tree.setInsertMark(item, before);
}
void showDropTargetEffect(int effect, int x, int y) {
	effect = checkEffect(effect);
	TreeItem item = (TreeItem)getItem(x, y);
	
	if ((effect & DND.FEEDBACK_EXPAND) == 0) {
		expandBeginTime = 0;
		expandItem = null;
	} else {
		if (item != null && item.equals(expandItem) && expandBeginTime != 0) {
			if (System.currentTimeMillis() >= expandBeginTime) {
				if (item.getItemCount() > 0 && !item.getExpanded()) {
					Event event = new Event();
					event.x = x;
					event.y = y;
					event.item = item;
					event.time = (int) System.currentTimeMillis();
					tree.notifyListeners(SWT.Expand, event);
					if (item.isDisposed()) return;
					item.setExpanded(true);
				}
				expandBeginTime = 0;
				expandItem = null;
			}
		} else {
			expandBeginTime = System.currentTimeMillis() + EXPAND_HYSTERESIS;
			expandItem = item;
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
		if (currentItem != item || (currentEffect & DND.FEEDBACK_SELECT) == 0) { 
			setDropSelection(item); 
			currentEffect = effect;
			currentItem = item;
		}
	} else {
		setDropSelection(null);
	}
	
	if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
		(effect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
		if (currentItem != item || 
			 ((effect & DND.FEEDBACK_INSERT_AFTER) != (currentEffect & DND.FEEDBACK_INSERT_AFTER)) ||
			 ((effect & DND.FEEDBACK_INSERT_BEFORE) != (currentEffect & DND.FEEDBACK_INSERT_BEFORE))) { 
			setInsertMark(item, (effect & DND.FEEDBACK_INSERT_BEFORE) != 0);
			currentEffect = effect;
			currentItem = item;
		}
	} else {
		setInsertMark(null, false);
	}
}
}

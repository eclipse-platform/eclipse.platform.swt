/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;

class TreeDragUnderEffect extends DragUnderEffect {
	private Tree tree;
	private int currentEffect = DND.FEEDBACK_NONE;
	
	private TreeItem dropSelection = null;
	private PaintListener paintListener;
	
	private TreeItem insertMark = null;
	private boolean insertBefore = false;
	
	private TreeItem scrollItem;
	private long scrollBeginTime;

	private TreeItem expandItem;
	private long expandBeginTime;
	
	private static final int SCROLL_HYSTERESIS = 150; // milli seconds
	private static final int EXPAND_HYSTERESIS = 300; // milli seconds

TreeDragUnderEffect(Tree tree) {
	this.tree = tree;
	paintListener = new PaintListener() {
		public void paintControl(PaintEvent e) {
			if (dropSelection == null  || dropSelection.isDisposed()) return;
			GC gc = e.gc;
			Color foreground = gc.getForeground();
			Display display = e.widget.getDisplay();
			gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			Rectangle bounds = dropSelection.getBounds();
			gc.drawRectangle(bounds.x + 1, bounds.y + 1, bounds.width - 3, bounds.height - 3);
			gc.setForeground(foreground);
		}
	};
}
void show(int effect, int x, int y) {
	effect = checkEffect(effect);
	TreeItem item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (currentEffect == DND.FEEDBACK_NONE && effect != DND.FEEDBACK_NONE) {
		tree.addPaintListener(paintListener);
	}
	scrollHover(effect, item, x, y);
	expandHover(effect, item, x, y);
	setDragUnderEffect(effect, item);
	if (currentEffect != DND.FEEDBACK_NONE && effect == DND.FEEDBACK_NONE) {
		tree.removePaintListener(paintListener);
	}
	currentEffect = effect;
}
private int checkEffect(int effect) {
	// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
	int mask = DND.FEEDBACK_INSERT_AFTER | DND.FEEDBACK_INSERT_BEFORE | DND.FEEDBACK_SELECT;
	int bits = effect & mask;
	if (bits == DND.FEEDBACK_INSERT_AFTER || bits == DND.FEEDBACK_INSERT_BEFORE || bits == DND.FEEDBACK_SELECT) return effect;
	return (effect & ~mask);
}
private TreeItem findItem(int x, int y){
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	Rectangle area = tree.getClientArea();
	if (!area.contains(coordinates)) return null;
	
	TreeItem item = tree.getItem(coordinates);
	if (item != null) return item;

	// Scan across the width of the tree.
	for (int x1 = area.x; x1 < area.x + area.width; x1++) {
		Point pt = new Point(x1, coordinates.y);
		item = tree.getItem(pt);
		if (item != null) return item;
	}
	// Check if we are just below the last item of the tree
	if (coordinates.y > area.y + area.height - tree.getItemHeight()) {
		int y1 = area.y + area.height - tree.getItemHeight();
		Point pt = new Point(coordinates.x, y1);
		item = tree.getItem(pt);	
		if (item != null) return item;
		
		// Scan across the width of the tree just above the bottom..
		for (int x1 = area.x; x1 < area.x + area.width; x1++) {
			pt = new Point(x1, y1);
			item = tree.getItem(pt);
			if (item != null) return item;
		}
	}
	return null;
}
private void setDragUnderEffect(int effect, TreeItem item) {
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		if ((currentEffect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
		    (currentEffect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
			tree.setInsertMark(null, false);
		}
		setDropSelection(item); 
		return;
	}
	if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
	    (effect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
		if ((currentEffect & DND.FEEDBACK_SELECT) != 0) {
			setDropSelection(null);
		}
		setInsertMark(item, (effect & DND.FEEDBACK_INSERT_BEFORE) != 0);
		return;
	}
	if ((currentEffect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
	    (currentEffect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
		tree.setInsertMark(null, false);
	}
	if ((currentEffect & DND.FEEDBACK_SELECT) != 0) {
		setDropSelection(null);
	}
}
private void setDropSelection (TreeItem item) {	
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
}
private void setInsertMark(TreeItem item, boolean before) {
	if (item == insertMark && before == insertBefore) return;
	insertMark = item;
	insertBefore = before;
	tree.setInsertMark(item, before);
}
private void scrollHover (int effect, TreeItem item, int x, int y) {
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollItem = null;
		return;
	}
	if (scrollItem == item && scrollBeginTime != 0) {
		if (System.currentTimeMillis() >= scrollBeginTime) {
			scroll(item, x, y);
			scrollBeginTime = 0;
			scrollItem = null;
		}
		return;
	}
	scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
	scrollItem = item;
}
private void scroll(TreeItem item, int x, int y) {
	if (item == null) return;
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	Rectangle area = tree.getClientArea();
	
// INTENTIONALLY COMMENTED - Tree.setTopItem does not work
//
//	TreeItem top = tree.getTopItem();
//	TreeItem newTop = null;
//	// scroll if two lines from top or bottom
//	int scroll_width = 2*tree.getItemHeight();
//	if (coordinates.y < area.y + scroll_width) {
//		 newTop = getPreviousVisibleItem(top);
//	} else if (coordinates.y > area.y + area.height - scroll_width) {
//		newTop = getNextVisibleItem(top, true);
//	}
//	if (newTop != null && newTop != top) {
//		System.out.println("set top item "+newTop);
//		tree.setTopItem(newTop);
//	}

	TreeItem showItem = null;
	// scroll if two lines from top or bottom
	int scroll_width = 2*tree.getItemHeight();
	if (coordinates.y < area.y + scroll_width) {
		showItem = getPreviousVisibleItem(item);
	} else if (coordinates.y > area.y + area.height - scroll_width) {
		showItem = getNextVisibleItem(item, true);
	}
	if (showItem != null) {
		tree.showItem(showItem);
	}		
}
private void expandHover (int effect, TreeItem item, int x, int y) {
	if ((effect & DND.FEEDBACK_EXPAND) == 0) {
		expandBeginTime = 0;
		expandItem = null;
		return;
	}
	if (expandItem == item && expandBeginTime != 0) {
		if (System.currentTimeMillis() >= expandBeginTime) {
			expand(item, x, y);
			expandBeginTime = 0;
			expandItem = null;
		}
		return;
	}
	expandBeginTime = System.currentTimeMillis() + EXPAND_HYSTERESIS;
	expandItem = item;
}
private void expand(TreeItem item, int x, int y) {
	if (item == null || item.getExpanded()) return;
	Event event = new Event();
	event.x = x;
	event.y = y;
	event.item = item;
	event.time = (int) System.currentTimeMillis();
	tree.notifyListeners(SWT.Expand, event);
	if (item.isDisposed()) return;
	item.setExpanded(true);
}
private TreeItem getNextVisibleItem(TreeItem item, boolean includeChildren) {
	// look down
	// neccesary on the first pass only
	if (includeChildren && item.getItemCount() > 0 && item.getExpanded()) {
		return item.getItems()[0];
	}
	// look sideways
	TreeItem parent = item.getParentItem();
	TreeItem[] peers = (parent != null) ? parent.getItems() : tree.getItems();
	for (int i = 0; i < peers.length - 1; i++) {
		if (peers[i] == item) return peers[i + 1];
	}
	// look up
	if (parent != null) return getNextVisibleItem(parent, false);
	return null;
}
private TreeItem getPreviousVisibleItem(TreeItem item) {
	// look sideways
	TreeItem parent = item.getParentItem();
	TreeItem[] peers = (parent != null) ? parent.getItems() : tree.getItems();
	for (int i = peers.length - 1; i > 0; i--) {
		if (peers[i] == item) {
			TreeItem peer = peers[i-1];
			if (!peer.getExpanded() || peer.getItemCount() == 0) return peer;
			TreeItem[] peerItems = peer.getItems();
			return peerItems[peerItems.length - 1];
		}
	}
	// look up
	return parent;
}
}

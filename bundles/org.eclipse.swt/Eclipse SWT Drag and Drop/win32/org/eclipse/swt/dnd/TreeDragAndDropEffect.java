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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

class TreeDragAndDropEffect extends DragAndDropEffect {
	Tree tree;
	int dropIndex;
	int scrollIndex;	
	long scrollBeginTime;
	int expandIndex;
	long expandBeginTime;
	TreeItem insertItem;
	boolean insertBefore;
	
	static final int SCROLL_HYSTERESIS = 200; // milli seconds
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

ImageData getDragSourceImage(int x, int y) {
	TreeItem[] selection = tree.getSelection();
	if (selection.length == 0) return null;
	int treeImageList = OS.SendMessage (tree.handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
	if (treeImageList != 0) {
		int count = Math.min(selection.length, 10);
		Rectangle bounds = selection[0].getBounds(0);
		for (int i = 1; i < count; i++) {
			bounds = bounds.union(selection[i].getBounds(0));
		}
		int hDC = OS.GetDC(0);
		int hDC1 = OS.CreateCompatibleDC(hDC);
		int bitmap = OS.CreateCompatibleBitmap(hDC, bounds.width, bounds.height);
		int hOldBitmap = OS.SelectObject(hDC1, bitmap);
		RECT rect = new RECT();
		rect.right = bounds.width;
		rect.bottom = bounds.height;
		int hBrush = OS.GetStockObject(OS.WHITE_BRUSH);
		OS.FillRect(hDC1, rect, hBrush);
		for (int i = 0; i < count; i++) {
			TreeItem selected = selection[i];
			Rectangle cell = selected.getBounds(0);
			int imageList = OS.SendMessage(tree.handle, OS.TVM_CREATEDRAGIMAGE, 0, selected.handle);
			OS.ImageList_Draw(imageList, 0, hDC1, cell.x - bounds.x, cell.y - bounds.y, OS.ILD_SELECTED);
			OS.ImageList_Destroy(imageList);
		}
		OS.SelectObject(hDC1, hOldBitmap);
		OS.DeleteDC (hDC1);
		OS.ReleaseDC (0, hDC);
		Display display = tree.getDisplay();
		Image image = Image.win32_new(display, SWT.BITMAP, bitmap);
		ImageData imageData  = image.getImageData();
		image.dispose();
		return imageData;
	}
	return null;
}

void showDropTargetEffect(int effect, int x, int y) {
	effect = checkEffect(effect);
	int handle = tree.handle;
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = coordinates.x;
	lpht.y = coordinates.y;
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	int hItem = lpht.hItem;
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollIndex = -1;
	} else {
		if (hItem != -1 && scrollIndex == hItem && scrollBeginTime != 0) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				int topItem = OS.SendMessage(handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
				int nextItem = OS.SendMessage(handle, OS.TVM_GETNEXTITEM, hItem == topItem ? OS.TVGN_PREVIOUSVISIBLE : OS.TVGN_NEXTVISIBLE, hItem);
				boolean scroll = true;
				if (hItem == topItem) {
					scroll = nextItem != 0;
				} else {
					RECT itemRect = new RECT ();
					itemRect.left = nextItem;
					if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, itemRect) != 0) {
						RECT rect = new RECT ();
						OS.GetClientRect (handle, rect);
						POINT pt = new POINT ();
						pt.x = itemRect.left;
						pt.y = itemRect.top;
						if (OS.PtInRect (rect, pt)) {
							pt.y = itemRect.bottom;
							if (OS.PtInRect (rect, pt)) scroll = false;
						}
					}
				}
				if (scroll) {
					OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, nextItem);
					tree.redraw();
				}
				scrollBeginTime = 0;
				scrollIndex = -1;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollIndex = hItem;
		}
	}
	if ((effect & DND.FEEDBACK_EXPAND) == 0) {
		expandBeginTime = 0;
		expandIndex = -1;
	} else {
		if (hItem != -1 && expandIndex == hItem && expandBeginTime != 0) {
			if (System.currentTimeMillis() >= expandBeginTime) {
				if (OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem) != 0) {
					TVITEM tvItem = new TVITEM ();
					tvItem.hItem = hItem;
					tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_STATE;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					if ((tvItem.state & OS.TVIS_EXPANDED) == 0) {
						OS.SendMessage (handle, OS.TVM_EXPAND, OS.TVE_EXPAND, hItem);
						tree.redraw();
					}
				}
				expandBeginTime = 0;
				expandIndex = -1;
			}
		} else {
			expandBeginTime = System.currentTimeMillis() + EXPAND_HYSTERESIS;
			expandIndex = hItem;
		}
	}
	if (dropIndex != -1 && (dropIndex != hItem || (effect & DND.FEEDBACK_SELECT) == 0)) {
		TVITEM tvItem = new TVITEM ();
		tvItem.hItem = dropIndex;
		tvItem.mask = OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_DROPHILITED;
		tvItem.state = 0;
		OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		dropIndex = -1;
	}
	if (hItem != -1 && hItem != dropIndex && (effect & DND.FEEDBACK_SELECT) != 0) {
		TVITEM tvItem = new TVITEM ();
		tvItem.hItem = hItem;
		tvItem.mask = OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_DROPHILITED;
		tvItem.state = OS.TVIS_DROPHILITED;
		OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		dropIndex = hItem;
	}
	if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0 || (effect & DND.FEEDBACK_INSERT_AFTER) != 0) {
		boolean before = (effect & DND.FEEDBACK_INSERT_BEFORE) != 0;
		/*
		* Bug in Windows.  When TVM_SETINSERTMARK is used to set
		* an insert mark for a tree and an item is expanded or
		* collapsed near the insert mark, the tree does not redraw
		* the insert mark properly.  The fix is to hide and show
		* the insert mark whenever an item is expanded or collapsed.
		* Since the insert mark can not be queried from the tree,
		* use the Tree API rather than calling the OS directly.
		*/
		TreeItem item = (TreeItem)tree.getDisplay().findWidget(tree.handle, hItem);
		if (item != null) {
			if (item != insertItem || before != insertBefore) {
				tree.setInsertMark(item, before);
			}
			insertItem = item;
			insertBefore = before;
		} else {
			if (insertItem != null) {
				tree.setInsertMark(null, false);
			}
			insertItem = null;
		}
	} else {
		if (insertItem != null) {
			tree.setInsertMark(null, false);
		}
		insertItem = null;
	}
	return;
}
}

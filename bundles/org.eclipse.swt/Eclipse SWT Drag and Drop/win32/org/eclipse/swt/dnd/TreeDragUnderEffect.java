package org.eclipse.swt.dnd;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TVITEM;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

class TreeDragUnderEffect extends DragUnderEffect {

	private Tree tree;
	private TreeItem currentItem = null;
	private int currentEffect = DND.FEEDBACK_NONE;
	private TreeItem[] selection = new TreeItem[0];

TreeDragUnderEffect(Tree tree) {
	this.tree = tree;
}
void show(int effect, int x, int y) {
	TreeItem item = findItem(x, y);
	if (item == null) {
		effect = DND.FEEDBACK_NONE;
	}
	if (currentEffect != effect && currentEffect == DND.FEEDBACK_NONE) {
		selection = tree.getSelection();
		tree.setSelection(new TreeItem[0]);
	}
	boolean restoreSelection = currentEffect != effect && effect == DND.FEEDBACK_NONE;
	setDragUnderEffect(effect, item);
	if (restoreSelection) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_STATE;
		tvItem.stateMask = OS.TVIS_SELECTED;
		tvItem.state = OS.TVIS_SELECTED;
		for (int i = 0; i < selection.length; i++) {
			tvItem.hItem = selection[i].handle;
			OS.SendMessage (tree.handle, OS.TVM_SETITEM, 0, tvItem);
			OS.SendMessage (tree.handle, OS.TVM_ENSUREVISIBLE, 0, selection[i].handle);
		}
		selection = new TreeItem[0];
	}
}
private TreeItem findItem(int x , int y){
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	TreeItem item = tree.getItem(coordinates);
	if (item != null) return item;

	Rectangle area = tree.getClientArea();
	for (int x1 = area.x; x1 < area.x + area.width; x1++) {
		coordinates = new Point(x1, y);
		coordinates = tree.toControl(coordinates);
		item = tree.getItem(coordinates);
		if (item != null) return item;
	}
	return null;
}
private void setDragUnderEffect(int effect, TreeItem item) {
	switch (effect) {				
		case DND.FEEDBACK_SELECT:
			if (currentEffect == DND.FEEDBACK_INSERT_AFTER ||
			    currentEffect == DND.FEEDBACK_INSERT_BEFORE) {
				tree.setInsertMark(null, false);
				currentEffect = DND.FEEDBACK_NONE;
				currentItem = null;
			}
			if (currentEffect != effect || currentItem != item) { 
				setDropSelection(item); 
				currentEffect = DND.FEEDBACK_SELECT;
				currentItem = item;
			}
			break;
		case DND.FEEDBACK_INSERT_AFTER:
		case DND.FEEDBACK_INSERT_BEFORE:
			if (currentEffect == DND.FEEDBACK_SELECT) {
				setDropSelection(null);
				currentEffect = DND.FEEDBACK_NONE;
				currentItem = null;
			}
			if (currentEffect != effect || currentItem != item) { 
				tree.setInsertMark(item, effect == DND.FEEDBACK_INSERT_BEFORE);
				currentEffect = effect;
				currentItem = item;
			}
			break;			
		default :
			if (currentEffect == DND.FEEDBACK_INSERT_AFTER ||
			    currentEffect == DND.FEEDBACK_INSERT_BEFORE) {
				tree.setInsertMark(null, false);
			}
			if (currentEffect == DND.FEEDBACK_SELECT) {
				setDropSelection(null);
			}
			currentEffect = DND.FEEDBACK_NONE;
			currentItem = null;
			break;
	}
}
private void setDropSelection (TreeItem item) {	
	int hNewItem = 0;
	if (item != null) hNewItem = item.handle;
	OS.SendMessage (tree.handle, OS.TVM_SELECTITEM, OS.TVIS_DROPHILITED, hNewItem);
}
}
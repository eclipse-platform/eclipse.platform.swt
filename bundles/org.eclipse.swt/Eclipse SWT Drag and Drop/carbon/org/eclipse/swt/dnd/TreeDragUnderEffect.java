package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TreeDragUnderEffect extends DragUnderEffect {

	private Tree tree;
	private TreeItem currentItem = null;
	private int currentEffect = DND.FEEDBACK_NONE;
	private TreeItem[] selection = new TreeItem[0];

TreeDragUnderEffect(Tree tree) {
	this.tree = tree;
}
void show(int effect, int x, int y) {
	TreeItem item = null;
	if (effect != DND.FEEDBACK_NONE) item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (currentEffect != effect && currentEffect == DND.FEEDBACK_NONE) {
		selection = tree.getSelection();
		tree.setSelection(new TreeItem[0]);
	}
	boolean restoreSelection = currentEffect != effect && effect == DND.FEEDBACK_NONE;
	setDragUnderEffect(effect, item);
	if (restoreSelection) {
		tree.setSelection(selection);
		selection = new TreeItem[0];
	}
}
private TreeItem findItem(int x , int y){
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	return tree.getItem(coordinates);
}
private void setDragUnderEffect(int effect, TreeItem item) {
	switch (effect) {				
		case DND.FEEDBACK_SELECT:
			if (currentEffect == DND.FEEDBACK_INSERT_AFTER ||
			    currentEffect == DND.FEEDBACK_INSERT_BEFORE) {
				setInsertMark(null, false);
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
				setInsertMark(item, effect == DND.FEEDBACK_INSERT_AFTER);
				currentEffect = effect;
				currentItem = item;
			}
			break;			
		default :
			if (currentEffect == DND.FEEDBACK_INSERT_AFTER ||
			    currentEffect == DND.FEEDBACK_INSERT_BEFORE) {
				setInsertMark(null, false);
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
	if (item == null) {
		tree.setSelection(new TreeItem[0]);
	} else {
		tree.setSelection(new TreeItem[]{item});
	}
}
private void setInsertMark (TreeItem item, boolean after) {
	// not currently implemented
}
}

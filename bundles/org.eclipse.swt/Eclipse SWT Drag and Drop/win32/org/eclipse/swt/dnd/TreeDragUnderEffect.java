package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TVITEM;

class TreeDragUnderEffect extends DragUnderEffect {

	private Tree tree;
	private int currentEffect = DND.FEEDBACK_NONE;
	private TreeItem[] selection = new TreeItem[0];
//	private TreeItem dropSelection = null;

TreeDragUnderEffect(Tree tree) {
	this.tree = tree;
}
void show(int effect, int x, int y) {
	TreeItem item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (currentEffect == DND.FEEDBACK_NONE && effect != DND.FEEDBACK_NONE) {
		selection = tree.getSelection();
		tree.deselectAll();
	}
	int previousEffect = currentEffect;
	setDragUnderEffect(effect, item);
	if (previousEffect != DND.FEEDBACK_NONE && currentEffect == DND.FEEDBACK_NONE) {
		tree.setSelection(selection);
		selection = new TreeItem[0];
	}
}
private TreeItem findItem(int x , int y){
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	Rectangle area = tree.getClientArea();
	if (!area.contains(coordinates)) return null;
	
	TreeItem item = tree.getItem(coordinates);
	if (item != null) return item;

	// Scan across the width of the tree.
	for (int x1 = area.x; x1 < area.x + area.width; x1++) {
		coordinates = new Point(x1, coordinates.y);
		item = tree.getItem(coordinates);
		if (item != null) return item;
	}
	// Check if we are just below the last item of the tree
	coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	if (coordinates.y > area.y + area.height - tree.getItemHeight()) {;
		int y1 = area.y + area.height - tree.getItemHeight();
		coordinates = new Point(coordinates.x, y1);
		
		item = tree.getItem(coordinates);	
		if (item != null) return item;
		
		// Scan across the width of the tree just above the bottom..
		for (int x1 = area.x; x1 < area.x + area.width; x1++) {
			coordinates = new Point(x1, y1);
			item = tree.getItem(coordinates);
			if (item != null) return item;
		}
	}
	return null;
}
private void setDragUnderEffect(int effect, TreeItem item) {
	switch (effect) {				
		case DND.FEEDBACK_SELECT:
			if (currentEffect == DND.FEEDBACK_INSERT_AFTER ||
			    currentEffect == DND.FEEDBACK_INSERT_BEFORE) {
				tree.setInsertMark(null, false);
			}
			setDropSelection(item); 
			currentEffect = DND.FEEDBACK_SELECT;
			break;
		case DND.FEEDBACK_INSERT_AFTER:
		case DND.FEEDBACK_INSERT_BEFORE:
			if (currentEffect == DND.FEEDBACK_SELECT) {
				setDropSelection(null);
			}
			tree.setInsertMark(item, effect == DND.FEEDBACK_INSERT_BEFORE);
			currentEffect = effect;
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
			break;
	}
}
private void setDropSelection (TreeItem item) {	
//	if (item == dropSelection) return;
//	if (dropSelection != null) tree.deselectAll();
//	dropSelection = item;
//	if (dropSelection != null) tree.setSelection(new TreeItem[]{dropSelection});
	
	int hNewItem = 0;
	if (item != null) hNewItem = item.handle;
	OS.SendMessage (tree.handle, OS.TVM_SELECTITEM, OS.TVIS_DROPHILITED, hNewItem);

}
}

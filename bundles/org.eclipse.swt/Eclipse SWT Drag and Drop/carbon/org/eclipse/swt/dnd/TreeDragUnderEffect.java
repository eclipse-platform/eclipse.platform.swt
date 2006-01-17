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

TreeDragUnderEffect(Tree tree) {
	this.tree = tree;
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
TreeItem findItem(int x , int y){
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	return tree.getItem(coordinates);
}
void setDragUnderEffect(int effect, TreeItem item) {
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

package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TableDragUnderEffect extends DragUnderEffect {
	private Table table;
	private TableItem[] selection = new TableItem[0];
	int currentEffect = DND.FEEDBACK_NONE;
	private TableItem currentItem;
	private TableItem dropSelection;
	
TableDragUnderEffect(Table table) {
	this.table = table;
}
void show(int effect, int x, int y) {
	TableItem item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (effect == currentEffect && item == currentItem) return;
	currentItem = item;
	if (currentEffect == DND.FEEDBACK_NONE && effect != DND.FEEDBACK_NONE) {
		selection = table.getSelection();
		table.deselectAll();
	}
	int previousEffect = currentEffect;
	setDragUnderEffect(effect, item);
	if (previousEffect != DND.FEEDBACK_NONE && currentEffect == DND.FEEDBACK_NONE) {
		table.setSelection(selection);
		selection = new TableItem[0];
	}
}
private TableItem findItem(int x, int y){
	if (table == null) return null;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	TableItem item = table.getItem(coordinates);
	if (item != null) return item;
	
	Rectangle area = table.getClientArea();
	for (int x1 = area.x; x1 < area.x + area.width; x1++) {
		coordinates = new Point(x1, y);
		coordinates = table.toControl(coordinates);
		item = table.getItem(coordinates);
		if (item != null) return item;
	}
	return null;

}
private void setDragUnderEffect(int effect, TableItem item) {	
	switch (effect) {				
		case DND.FEEDBACK_SELECT:
			setDropSelection(item); 
			currentEffect = DND.FEEDBACK_SELECT;
			break;		
		default:
			if (currentEffect == DND.FEEDBACK_SELECT) {
				setDropSelection(null);
			}
			currentEffect = DND.FEEDBACK_NONE;
			break;
	}
}
private void setDropSelection (TableItem item) {
	if (item == dropSelection) return;
	if (dropSelection != null) table.deselectAll();
	dropSelection = item;
	if (dropSelection != null) table.setSelection(new TableItem[]{dropSelection});
}
}

package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TableDragUnderEffect extends DragUnderEffect {
	private Table table;
	private TableItem currentItem;
	private TableItem[] selection = new TableItem[0];
	private int currentEffect = DND.FEEDBACK_NONE;
	
TableDragUnderEffect(Table table) {
	this.table = table;
}
void show(int effect, int x, int y) {
	TableItem item = null;
	if (effect != DND.FEEDBACK_NONE) item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (currentEffect != effect && currentEffect == DND.FEEDBACK_NONE) {
		selection = table.getSelection();
		table.setSelection(new TableItem[0]);
	}
	boolean restoreSelection = currentEffect != effect && effect == DND.FEEDBACK_NONE;
	setDragUnderEffect(effect, item);
	if (restoreSelection) {
		table.setSelection(selection);
		selection = new TableItem[0];
	}
}
private TableItem findItem(int x, int y){
	if (table == null) return null;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	return table.getItem(coordinates);
}
private void setDragUnderEffect(int effect, TableItem item) {	
	if (currentItem != item) {
		if (item == null) {
			table.setSelection(new TableItem[0]);
		} else {
			table.setSelection(new TableItem[] {item});
		}
		currentItem = item;
	}
	currentEffect = effect;
}
}

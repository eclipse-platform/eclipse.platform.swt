package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.LVITEM;
import org.eclipse.swt.internal.win32.OS;

class TableDragUnderEffect extends DragUnderEffect {
	private Table table;
	private TableItem currentItem;
	
TableDragUnderEffect(Table table) {
	this.table = table;
}
void show(int effect, int x, int y) {
	TableItem item = findItem(x, y);
	if (item == null) {
		effect = DND.FEEDBACK_NONE;
	}
	setDragUnderEffect(effect, item);
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
	if (currentItem != item) {
		setDropSelection(item);
		currentItem = item;		
	}
}
private void setDropSelection (TableItem item) {
	LVITEM lvItem = new LVITEM ();
	lvItem.stateMask = OS.LVIS_DROPHILITED;
	// remove all drop highlighting
	OS.SendMessage (table.handle, OS.LVM_SETITEMSTATE, -1, lvItem);
	if (item != null) { 
		lvItem.state = OS.LVIS_DROPHILITED;
		int index = table.indexOf(item);
		OS.SendMessage (table.handle, OS.LVM_SETITEMSTATE, index, lvItem);
	}
}
}

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.LVITEM;
import org.eclipse.swt.internal.win32.OS;

class TableDragUnderEffect extends DragUnderEffect {
	private Table table;
	private TableItem[] selection = new TableItem[0];
	int currentEffect = DND.FEEDBACK_NONE;
//	private TableItem dropSelection
	private TableItem scrollItem;
	private long scrollBeginTime;
	private static final int SCROLL_HYSTERESIS = 400; // milli seconds
	private static final int SCROLL_WIDTH = 100; // pixels
	
TableDragUnderEffect(Table table) {
	this.table = table;
}
void show(int effect, int x, int y) {
	TableItem item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (currentEffect == DND.FEEDBACK_NONE && effect != DND.FEEDBACK_NONE) {
		selection = table.getSelection();
		table.deselectAll();
	}
	scrollHover(effect, item, x, y);
	setDragUnderEffect(effect, item);
	if (currentEffect != DND.FEEDBACK_NONE && effect == DND.FEEDBACK_NONE) {
		table.setSelection(selection);
		selection = new TableItem[0];
	}
	currentEffect = effect;
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
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		setDropSelection(item); 
	} else {
		if ((currentEffect & DND.FEEDBACK_SELECT) != 0) {
			setDropSelection(null);
		}
	}
}
private void setDropSelection (TableItem item) {
//	if (item == dropSelection) return;
//	if (dropSelection != null) table.deselectAll();
//	dropSelection = item;
//	if (dropSelection != null) table.setSelection(new TableItem[]{dropSelection});
	
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
private void scrollHover (int effect, TableItem item, int x, int y) {
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
private void scroll(TableItem item, int x, int y) {
	if (item == null) return;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	Rectangle area = table.getClientArea();
	TableItem showItem = null;
	int itemIndex = table.indexOf(item);
	if (coordinates.y - area.y < SCROLL_WIDTH) {
		showItem = table.getItem(Math.max(0, itemIndex - 1));
	} else if ((area.y + area.height - coordinates.y) < SCROLL_WIDTH) {
		showItem = table.getItem(Math.min(table.getItemCount() - 1, itemIndex + 1));
	}
	if (showItem != null) {
		table.showItem(showItem);
	}		
}
}

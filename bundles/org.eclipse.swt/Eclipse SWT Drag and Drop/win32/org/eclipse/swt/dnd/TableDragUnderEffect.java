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
	private int currentEffect = DND.FEEDBACK_NONE;
	private int[] selection = new int[0];
	private TableItem scrollItem;
	private long scrollBeginTime;
	private static final int SCROLL_HYSTERESIS = 600; // milli seconds
	
TableDragUnderEffect(Table table) {
	this.table = table;
}
void show(int effect, int x, int y) {
	TableItem item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (currentEffect == DND.FEEDBACK_NONE && effect != DND.FEEDBACK_NONE) {
		selection = table.getSelectionIndices();
		table.deselectAll();
	}
	scrollHover(effect, item, x, y);
	setDragUnderEffect(effect, item);
	if (currentEffect != DND.FEEDBACK_NONE && effect == DND.FEEDBACK_NONE) {
		table.select(selection);
		selection = new int[0];
	}
	currentEffect = effect;
}
private TableItem findItem(int x, int y){
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	Rectangle area = table.getClientArea();
	if (!area.contains(coordinates)) return null;

	TableItem item = table.getItem(coordinates);
	if (item != null) return item;

	// Scan across the width of the table
	for (int x1 = area.x; x1 < area.x + area.width; x1++) {
		Point pt = new Point(x1, coordinates.y);
		item = table.getItem(pt);
		if (item != null) return item;
	}
	return null;
}
private void setDragUnderEffect(int effect, TableItem item) {	
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		setDropSelection(item);
		return;
	}
	if ((currentEffect & DND.FEEDBACK_SELECT) != 0) {
		setDropSelection(null);
	}
}
private void setDropSelection (TableItem item) {
	LVITEM lvItem = new LVITEM ();
	lvItem.stateMask = OS.LVIS_DROPHILITED;
	// remove all drop highlighting
	OS.SendMessage (table.handle, OS.LVM_SETITEMSTATE, -1, lvItem);
	if (item != null) {
		int index = table.indexOf(item);
		lvItem.state = OS.LVIS_DROPHILITED;
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
	int top = table.getTopIndex();
	int newTop = -1;
	// scroll if two lines from top or bottom
	int scroll_width = 2*table.getItemHeight();
	if (coordinates.y < area.y + scroll_width) {
		newTop = Math.max(0, top - 1);
	} else if (coordinates.y > area.y + area.height - scroll_width) {
		newTop = Math.min(table.getItemCount() - 1, top + 1);
	}
	if (newTop != -1 && newTop != top) {
		table.setTopIndex(newTop);
	}
}
}

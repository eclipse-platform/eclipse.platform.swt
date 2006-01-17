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

class TableDragUnderEffect extends DragUnderEffect {
	Table table;
	TableItem currentItem;
	TableItem[] selection = new TableItem[0];
	int currentEffect = DND.FEEDBACK_NONE;
	
TableDragUnderEffect(Table table) {
	this.table = table;
}
Widget getItem(int x, int y) {
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	TableItem item = table.getItem(coordinates);
	if (item == null) {
		Rectangle area = table.getClientArea();
		if (area.contains(coordinates)) {
			// Scan across the width of the tree.
			for (int x1 = area.x; x1 < area.x + area.width; x1++) {
				Point pt = new Point(x1, coordinates.y);
				item = table.getItem(pt);
				if (item != null) {
					break;
				}
			}
		}
	}
	return item;
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
TableItem findItem(int x, int y){
	if (table == null) return null;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	return table.getItem(coordinates);
}
void setDragUnderEffect(int effect, TableItem item) {	
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

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TableDragAndDropEffect extends DragAndDropEffect {
	Table table;
	
	int currentEffect = DND.FEEDBACK_NONE;
	TableItem currentItem;
	
	PaintListener paintListener;
	TableItem dropSelection = null;
	
	TableItem scrollItem;
	long scrollBeginTime;

	static final int SCROLL_HYSTERESIS = 150; // milli seconds

TableDragAndDropEffect(Table table) {
	this.table = table;
}
int checkEffect(int effect) {
	// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
	if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
	if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
	return effect;
}
Widget getItem(int x, int y) {
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	TableItem item = table.getItem(coordinates);
	if (item == null) {
		Rectangle area = table.getClientArea();
		if (area.contains(coordinates)) {
			// Scan across the width of the table.
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
void setDropSelection (TableItem item) {	
	if (item == dropSelection) return;
	if (dropSelection != null && !dropSelection.isDisposed()) {
		Rectangle bounds = dropSelection.getBounds(0);
		table.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
	}
	dropSelection = item;
	if (dropSelection != null && !dropSelection.isDisposed()) {
		Rectangle bounds = dropSelection.getBounds(0);
		table.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
	}
	if (dropSelection == null) {
		if (paintListener != null) {
			table.removePaintListener(paintListener);
			paintListener = null;
		}
	} else {
		if (paintListener == null) {
			paintListener = new PaintListener() {
				public void paintControl(PaintEvent e) {
					if (dropSelection == null  || dropSelection.isDisposed()) return;
					GC gc = e.gc;
					boolean xor = gc.getXORMode();
					gc.setXORMode(true);
					Rectangle bounds = dropSelection.getBounds(0);
					gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
					gc.setXORMode(xor);
				}
			};
			table.addPaintListener(paintListener);
		}
	}
}

void showDropTargetEffect(int effect, int x, int y) {
	effect = checkEffect(effect);
	TableItem item = (TableItem)getItem(x, y);

	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollItem = null;
	} else {
		if (item != null && item.equals(scrollItem)  && scrollBeginTime != 0) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				Rectangle area = table.getClientArea();
				int headerHeight = table.getHeaderHeight();
				int itemHeight= table.getItemHeight();
				Point pt = new Point(x, y);
				pt = table.getDisplay().map(null, table, pt);
				TableItem nextItem = null;
				if (pt.y < area.y + headerHeight + 2 * itemHeight) {
					int index = Math.max(0, table.indexOf(item)-1);
					nextItem = table.getItem(index);
				}
				if (pt.y > area.y + area.height - 2 * itemHeight) {
					int index = Math.min(table.getItemCount()-1, table.indexOf(item)+1);
					nextItem = table.getItem(index);
				}
				if (nextItem != null) table.showItem(nextItem);
				scrollBeginTime = 0;
				scrollItem = null;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollItem = item;
		}
	}
	
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		if (currentItem != item || (currentEffect & DND.FEEDBACK_SELECT) == 0) { 
			setDropSelection(item); 
			currentEffect = effect;
			currentItem = item;
		}
	} else {
		setDropSelection(null);
	}
}
}

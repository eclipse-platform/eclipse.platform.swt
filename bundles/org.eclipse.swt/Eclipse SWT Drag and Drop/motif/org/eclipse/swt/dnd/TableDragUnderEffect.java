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
import org.eclipse.swt.events.*;
import org.eclipse.swt.SWT;

class TableDragUnderEffect extends DragUnderEffect {
	private Table table;
	private int currentEffect = DND.FEEDBACK_NONE;
	
	private TableItem dropSelection;
	private PaintListener paintListener;
	
	private TableItem scrollItem;
	private long scrollBeginTime;
	private static final int SCROLL_HYSTERESIS = 600; // milli seconds

TableDragUnderEffect(Table table) {
	this.table = table;
	paintListener = new PaintListener() {
		public void paintControl(PaintEvent e) {
			if (dropSelection == null || dropSelection.isDisposed()) return;
			GC gc = e.gc;
			Color foreground = gc.getForeground();
			Display display = e.widget.getDisplay();
			gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			Rectangle bounds = dropSelection.getBounds(0);
			Rectangle area = TableDragUnderEffect.this.table.getClientArea();
			gc.drawRectangle(area.x, bounds.y + 1, area.width - 1, bounds.height - 3);
			gc.setForeground(foreground);
		}
	};
}
void show(int effect, int x, int y) {
	TableItem item = findItem(x, y);
	if (item == null) effect = DND.FEEDBACK_NONE;
	if (currentEffect == DND.FEEDBACK_NONE && effect != DND.FEEDBACK_NONE) {
		table.addPaintListener(paintListener);
	}
	scrollHover(effect, item, x, y);
	setDragUnderEffect(effect, item);
	if (currentEffect != DND.FEEDBACK_NONE && effect == DND.FEEDBACK_NONE) {
		table.removePaintListener(paintListener);
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
	if (item == dropSelection) return;
	Rectangle area = table.getClientArea();
	if (dropSelection != null && !dropSelection.isDisposed()) {
		Rectangle bounds = dropSelection.getBounds(0);
		table.redraw(area.x, bounds.y, area.width, bounds.height, true);
	}
	dropSelection = item;
	if (dropSelection != null && !dropSelection.isDisposed()) {
		Rectangle bounds = dropSelection.getBounds(0);
		table.redraw(area.x, bounds.y, area.width, bounds.height, true);
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

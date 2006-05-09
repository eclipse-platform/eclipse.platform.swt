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


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.*;

class TableDragAndDropEffect extends DragAndDropEffect {
	Table table;
	TableItem scrollItem;
	long scrollBeginTime;
	int currentEffect = DND.FEEDBACK_NONE;

	static Callback AcceptDragProc;
	static {
		AcceptDragProc = new Callback(TableDragAndDropEffect.class, "AcceptDragProc", 5); //$NON-NLS-1$
		int acceptDragProc = AcceptDragProc.getAddress();
		if (acceptDragProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	}
	static final int SCROLL_HYSTERESIS = 150; // milli seconds

TableDragAndDropEffect(Table table) {
	this.table = table;
	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	OS.GetDataBrowserCallbacks (table.handle, callbacks);
	callbacks.v1_acceptDragCallback = AcceptDragProc.getAddress();
	OS.SetDataBrowserCallbacks(table.handle, callbacks);
}

static int AcceptDragProc(int theControl, int itemID, int property, int theRect, int theDrag) {
	DropTarget target = FindDropTarget(theControl, theDrag);
	if (target == null || target.effect == null) return 0;
	TableDragAndDropEffect effect = (TableDragAndDropEffect)target.effect;
	return effect.acceptDragProc(theControl, itemID, property, theRect, theDrag);
}

static DropTarget FindDropTarget(int theControl, int theDrag) {
	if (theControl == 0) return null;
	Display display = Display.findDisplay(Thread.currentThread());
	if (display == null || display.isDisposed()) return null;
	Widget widget = display.findWidget(theControl);
	if (widget == null) return null;
	return (DropTarget)widget.getData(DropTarget.DROPTARGETID); 
}

int acceptDragProc(int theControl, int itemID, int property, int theRect, int theDrag) {
	return (currentEffect & DND.FEEDBACK_SELECT) != 0 ? 1 : 0;
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
	
	// store current effect for selection feedback
	currentEffect = effect;

}
}

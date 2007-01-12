/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

/*public*/ class TableDropTargetEffect extends DropTargetEffect {
	static final int SCROLL_HYSTERESIS = 150; // milli seconds
	
	int currentEffect = DND.FEEDBACK_NONE;
	TableItem currentItem;
	
	PaintListener paintListener;
	TableItem dropSelection = null;
	
	TableItem scrollItem;
	long scrollBeginTime;

	int checkEffect(int effect) {
		// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
		if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
		return effect;
	}

	boolean checkWidget(DropTargetEvent event) {
		return ((DropTarget) event.widget).getControl() instanceof Table;
	}

	/**
	 * This implementation of <code>dragEnter</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>.
	 * 
	 * For additional information see <code>DropTargetAdapter.dragEnter</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragEnter(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag enter event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 */
	public void dragEnter(DropTargetEvent event) {
		scrollBeginTime = 0;
		scrollItem = null;
		currentItem = null;
		dropSelection = null;
	}
	
	/**
	 * This implementation of <code>dragLeave</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>. 
	 * 
	 * For additional information see <code>DropTargetAdapter.dragLeave</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragLeave(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag leave event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 */
	public void dragLeave(DropTargetEvent event) {
		if (!checkWidget(event)) return;
		Table table = (Table)((DropTarget)event.widget).getControl();
		if (currentItem != null) {
			setDropSelection(table, null);
			currentItem = null;
		}
		scrollBeginTime = 0;
		scrollItem = null;
	}

	/**
	 * This implementation of <code>dragOver</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>. The class description 
	 * lists the FEEDBACK constants that are applicable to the class.
	 * 
	 * For additional information see <code>DropTargetAdapter.dragOver</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragOver(event)</code>
	 * to get the default drag under effect implementation.
	 *
	 * @param event  the information associated with the drag over event
	 * 
	 * @see DropTargetAdapter
	 * @see DropTargetEvent
	 * @see DND#FEEDBACK_SELECT
	 * @see DND#FEEDBACK_SCROLL
	 */
	public void dragOver(DropTargetEvent event) {
		if (!checkWidget(event)) return;
		Table table = (Table)((DropTarget)event.widget).getControl();
		int effect = checkEffect(event.feedback);
		TableItem item = (TableItem)getItem(table, event.x, event.y);

		if ((effect & DND.FEEDBACK_SCROLL) == 0) {
			scrollBeginTime = 0;
			scrollItem = null;
		} else {
			if (item != null && item.equals(scrollItem)  && scrollBeginTime != 0) {
				if (System.currentTimeMillis() >= scrollBeginTime) {
					Rectangle area = table.getClientArea();
					int headerHeight = table.getHeaderHeight();
					int itemHeight= table.getItemHeight();
					Point pt = new Point(event.x, event.y);
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
				setDropSelection(table, item); 
				currentEffect = effect;
				currentItem = item;
			}
		} else {
			setDropSelection(table, null);
		}
	}

	Widget getItem(Table table, int x, int y) {
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
	
	void setDropSelection (Table table, TableItem item) {	
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
}

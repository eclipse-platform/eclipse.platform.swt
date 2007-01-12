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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.*;

/*public*/ class TableDropTargetEffect extends DropTargetEffect {
	static final int SCROLL_HYSTERESIS = 150; // milli seconds

	TableItem scrollItem;
	long scrollBeginTime;
	DataBrowserCallbacks callbacks = null;

	static Callback AcceptDragProc;
	static {
		AcceptDragProc = new Callback(TableDropTargetEffect.class, "AcceptDragProc", 5); //$NON-NLS-1$
		int acceptDragProc = AcceptDragProc.getAddress();
		if (acceptDragProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	}

	static int AcceptDragProc(int theControl, int itemID, int property, int theRect, int theDrag) {
		DropTarget target = FindDropTarget(theControl, theDrag);
		if (target == null) return 0;
		return (target.feedback & DND.FEEDBACK_SELECT) != 0 ? 1 : 0;
	}
	
	static DropTarget FindDropTarget(int theControl, int theDrag) {
		if (theControl == 0) return null;
		Display display = Display.findDisplay(Thread.currentThread());
		if (display == null || display.isDisposed()) return null;
		Widget widget = display.findWidget(theControl);
		if (widget == null) return null;
		return (DropTarget)widget.getData(DropTarget.DROPTARGETID); 
	}
	
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
		if (!checkWidget(event)) return;
		if (callbacks == null) {
			Table table = (Table)((DropTarget)event.widget).getControl();
			DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
			OS.GetDataBrowserCallbacks (table.handle, callbacks);
			callbacks.v1_acceptDragCallback = AcceptDragProc.getAddress();
			OS.SetDataBrowserCallbacks(table.handle, callbacks);
		}
		scrollBeginTime = 0;
		scrollItem = null;
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
		
		// store current effect for selection feedback
		((DropTarget)event.widget).feedback = effect;
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
}

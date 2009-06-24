/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

/**
 * This class provides a default drag under effect (eg. select, insert and scroll) 
 * when a drag occurs over a <code>Table</code>.
 * 
 * <p>Classes that wish to provide their own drag under effect for a <code>Table</code>
 * can extend the <code>TableDropTargetEffect</code> and override any applicable methods 
 * in <code>TableDropTargetEffect</code> to display their own drag under effect.</p>
 * 
 * Subclasses that override any methods of this class must call the corresponding
 * <code>super</code> method to get the default drag under effect implementation.
 *
 * <p>The feedback value is either one of the FEEDBACK constants defined in 
 * class <code>DND</code> which is applicable to instances of this class, 
 * or it must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> effect constants. 
 * </p>
 * <p>
 * <dl>
 * <dt><b>Feedback:</b></dt>
 * <dd>FEEDBACK_SELECT, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.3
 */
public class TableDropTargetEffect extends DropTargetEffect {
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
		return (DropTarget)widget.getData(DND.DROP_TARGET_KEY); 
	}
	
	/**
	 * Creates a new <code>TableDropTargetEffect</code> to handle the drag under effect on the specified 
	 * <code>Table</code>.
	 * 
	 * @param table the <code>Table</code> over which the user positions the cursor to drop the data
	 */
	public TableDropTargetEffect(Table table) {
		super(table);
	}

	int checkEffect(int effect) {
		// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
		if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
		return effect;
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
		if (callbacks == null) {
			Table table = (Table) control;
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
		Table table = (Table) control;
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
}

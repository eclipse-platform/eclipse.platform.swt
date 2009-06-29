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

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides a default drag under effect (eg. select, insert, scroll and expand) 
 * when a drag occurs over a <code>Tree</code>.
 * 
 * <p>Classes that wish to provide their own drag under effect for a <code>Tree</code>
 * can extend the <code>TreeDropTargetEffect</code> class and override any applicable methods 
 * in <code>TreeDropTargetEffect</code> to display their own drag under effect.</p>
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
 * <dd>FEEDBACK_SELECT, FEEDBACK_INSERT_BEFORE, FEEDBACK_INSERT_AFTER, FEEDBACK_EXPAND, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles FEEDBACK_SELECT, FEEDBACK_INSERT_BEFORE or
 * FEEDBACK_INSERT_AFTER may be specified.
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.3
 */
public class TreeDropTargetEffect extends DropTargetEffect {
	static final int SCROLL_HYSTERESIS = 150; // milli seconds
	static final int EXPAND_HYSTERESIS = 1000; // milli seconds
	
	int currentEffect = DND.FEEDBACK_NONE;
	TreeItem currentItem;
	
	PaintListener paintListener;
	TreeItem dropSelection = null;
	
	TreeItem insertItem = null;
	boolean insertBefore = false;

	TreeItem scrollItem;
	long scrollBeginTime;

	TreeItem expandItem;
	long expandBeginTime;
	
	/**
	 * Creates a new <code>TreeDropTargetEffect</code> to handle the drag under effect on the specified 
	 * <code>Tree</code>.
	 * 
	 * @param tree the <code>Tree</code> over which the user positions the cursor to drop the data
	 */
	public TreeDropTargetEffect(Tree tree) {
		super(tree);
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
		insertItem = null;
		currentItem = null;
		dropSelection = null;
		expandBeginTime = 0;
		expandItem = null;
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
		Tree tree = (Tree) control;
		if (insertItem != null) {
			tree.setInsertMark(null, false);
			insertItem = null;
		}	
		if (currentItem != null) {
			setDropSelection(tree, null);
			currentItem = null;
		}
		expandBeginTime = 0;
		expandItem = null;
		scrollBeginTime = 0;
		scrollItem = null;
	}

	/**
	 * This implementation of <code>dragOver</code> provides a default drag under effect
	 * for the feedback specified in <code>event.feedback</code>.
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
	 * @see DND#FEEDBACK_INSERT_BEFORE
	 * @see DND#FEEDBACK_INSERT_AFTER
	 * @see DND#FEEDBACK_SCROLL
	 */
	public void dragOver(DropTargetEvent event) {
		Tree tree = (Tree) control;
		int effect = checkEffect(event.feedback);

		TreeItem item = (TreeItem)getItem(tree, event.x, event.y);
		
		if ((effect & DND.FEEDBACK_EXPAND) == 0) {
			expandBeginTime = 0;
			expandItem = null;
		} else {
			if (item != null && item.equals(expandItem) && expandBeginTime != 0) {
				if (System.currentTimeMillis() >= expandBeginTime) {
					if (item.getItemCount() > 0 && !item.getExpanded()) {
						Event e = new Event();
						e.x = event.x;
						e.y = event.y;
						e.item = item;
						e.time = (int) System.currentTimeMillis();
						tree.notifyListeners(SWT.Expand, e);
						if (item.isDisposed()) return;
						item.setExpanded(true);
					}
					expandBeginTime = 0;
					expandItem = null;
				}
			} else {
				expandBeginTime = System.currentTimeMillis() + EXPAND_HYSTERESIS;
				expandItem = item;
			}
		}
		
		if ((effect & DND.FEEDBACK_SCROLL) == 0) {
			scrollBeginTime = 0;
			scrollItem = null;
		} else {
			if (item != null && item.equals(scrollItem)  && scrollBeginTime != 0) {
				if (System.currentTimeMillis() >= scrollBeginTime) {
					Rectangle area = tree.getClientArea();
					int headerHeight = tree.getHeaderHeight();
					int itemHeight= tree.getItemHeight();
					Point pt = new Point(event.x, event.y);
					pt = tree.getDisplay().map(null, tree, pt);
					TreeItem nextItem = null;
					if (pt.y < area.y + headerHeight + 2 * itemHeight) {
						nextItem = previousItem(tree, item);
					}
					if (pt.y > area.y + area.height - 2 * itemHeight) {
						nextItem = nextItem(tree, item);
					}
					if (nextItem != null) tree.showItem(nextItem);
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
				setDropSelection(tree, item); 
				currentEffect = effect;
				currentItem = item;
			}
		} else {
			setDropSelection(tree, null);
		}
		
		if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0 ||
			(effect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
			if (currentItem != item || 
				 ((effect & DND.FEEDBACK_INSERT_AFTER) != (currentEffect & DND.FEEDBACK_INSERT_AFTER)) ||
				 ((effect & DND.FEEDBACK_INSERT_BEFORE) != (currentEffect & DND.FEEDBACK_INSERT_BEFORE))) { 
				setInsertMark(tree, item, (effect & DND.FEEDBACK_INSERT_BEFORE) != 0);
				currentEffect = effect;
				currentItem = item;
			}
		} else {
			setInsertMark(tree, null, false);
		}
	}
	
	void setDropSelection (Tree tree, TreeItem item) {	
		if (item == dropSelection) return;
		if (dropSelection != null && !dropSelection.isDisposed()) {
			Rectangle bounds = dropSelection.getBounds();
			tree.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
		}
		dropSelection = item;
		if (dropSelection != null && !dropSelection.isDisposed()) {
			Rectangle bounds = dropSelection.getBounds();
			tree.redraw(bounds.x, bounds.y, bounds.width, bounds.height, true);
		}
		if (dropSelection == null) {
			if (paintListener != null) {
				tree.removePaintListener(paintListener);
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
						Rectangle bounds = dropSelection.getBounds();
						gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
						gc.setXORMode(xor);
					}
				};
				tree.addPaintListener(paintListener);
			}
		}
	}
	
	void setInsertMark(Tree tree, TreeItem item, boolean before) {
		if (item == insertItem && before == insertBefore) return;
		insertItem = item;
		insertBefore = before;
		tree.setInsertMark(item, before);
	}

}

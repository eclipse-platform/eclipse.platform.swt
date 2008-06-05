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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gtk.*;
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
	
	int scrollIndex;
	long scrollBeginTime;

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
		scrollBeginTime = 0;
		scrollIndex = -1;
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
		Table table = (Table) control;
		int /*long*/ handle = table.handle;
		OS.gtk_tree_view_set_drag_dest_row(handle, 0, OS.GTK_TREE_VIEW_DROP_BEFORE);

		scrollBeginTime = 0;
		scrollIndex = -1;
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
		int /*long*/ handle = table.handle;
		int effect = checkEffect(event.feedback);
		Point coordinates = new Point(event.x, event.y);
		coordinates = table.toControl(coordinates);
		int /*long*/ [] path = new int /*long*/ [1];
		OS.gtk_tree_view_get_path_at_pos (handle, coordinates.x, coordinates.y, path, null, null, null);
		int index = -1;
		if (path[0] != 0) {
			int /*long*/ indices = OS.gtk_tree_path_get_indices (path[0]);
			if (indices != 0) {
				int[] temp = new int[1];
				OS.memmove (temp, indices, 4);
				index = temp[0];
			}
		}
		if ((effect & DND.FEEDBACK_SCROLL) == 0) {
			scrollBeginTime = 0;
			scrollIndex = -1;
		} else {
			if (index != -1 && scrollIndex == index && scrollBeginTime != 0) {
				if (System.currentTimeMillis() >= scrollBeginTime) {
					if (coordinates.y < table.getItemHeight()) {
						OS.gtk_tree_path_prev(path[0]);
					} else {
						OS.gtk_tree_path_next(path[0]);
					}
					if (path[0] != 0) {
						OS.gtk_tree_view_scroll_to_cell(handle, path[0], 0, false, 0, 0);
						OS.gtk_tree_path_free(path[0]);
						path[0] = 0;
						OS.gtk_tree_view_get_path_at_pos (handle, coordinates.x, coordinates.y, path, null, null, null);
					}
					scrollBeginTime = 0;
					scrollIndex = -1;
				}
			} else {
				scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
				scrollIndex = index;
			}
		}
		if (path[0] != 0) {
			int position = 0;
			if ((effect & DND.FEEDBACK_SELECT) != 0) position = OS.GTK_TREE_VIEW_DROP_INTO_OR_BEFORE;
			//if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) position = OS.GTK_TREE_VIEW_DROP_BEFORE;
			//if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0) position = OS.GTK_TREE_VIEW_DROP_AFTER;
			if (position != 0) {
				OS.gtk_tree_view_set_drag_dest_row(handle, path[0], OS.GTK_TREE_VIEW_DROP_INTO_OR_BEFORE);
			} else {
				OS.gtk_tree_view_set_drag_dest_row(handle, 0, OS.GTK_TREE_VIEW_DROP_BEFORE);
			}
		} else {
			OS.gtk_tree_view_set_drag_dest_row(handle, 0, OS.GTK_TREE_VIEW_DROP_BEFORE);
		}
		if (path[0] != 0) OS.gtk_tree_path_free (path [0]);
	}
}
